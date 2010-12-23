/****license*****************************************************************
**   file: raProcess.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.util;

import hr.restart.sisfun.dlgErrors;
import hr.restart.swing.JraDialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

/**
 * <p>Title: raProcess</p>
 * <p>Description: Klasa za maskiranje dugog procesa modalnim JDialogom.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * Polubrat klase raDelayWindow. Namjena je slicna, ali princip koristenja drugaciji.
 * Koristi se tako da se pozove staticka metoda runChild() s Runnable-om koji predstavlja
 * dugi proces (i kojem treba implementirati metodu run()). Metoda runChild() moze se
 * pozvati i iz EventDispatchThreada (tipicno iz AWT event handlera) jer se pokrece
 * nezavisni thread koji se vrti maskiran modalnim dialogom. Zbog toga se moze pozvati
 * i, npr., unutar metoda tipa Validacija() koje trebaju vratiti nekakav flag. Poziv
 * runChild() blokira izvodjenje dok se modalni dialog ne zatvori, a on ce se zatvoriti
 * kad paralelni Thread zavrsi izvodjenje, ili kad korisnik pokusa zatvoriti dialog
 * (to je ekvivalentno prekidu dugog procesa). Dialog se nece automatski zatvoriti (i
 * prekinuti proces) nego je potrebno nesto rucnog podesavanja da bi to bilo moguce.
 * Konkretno, unutar dugog procesa pozeljno je tu i tamo zvati metodu checkClosing()
 * koja provjerava je li korisnik pokusao zatvoriti prozor. Takodjer, eventualne
 * DataSet-ove zgodno je otvarati metodom openDataSet() jer ista ima mehanizam za
 * prekidanje otvaranja dataseta (DataSet.cancelOperation()). Primjer upotrebe:
 * <pre>
 *   raProcess.runChild(this, new Runnable() {
 *     public void run() {
 *       // dugi proces
 *       for (...) {
 *         raProcess.checkClosing();
 *         ....
 *       }
 *       // eventualna promjena poruke u dialogu
 *       raProcess.setMessage("Otvaranje dataseta ...", false);
 *       raProcess.openDataSet(ds = ut.getNewQueryDataSet("SELECT * FROM ...", false));
 *       //
 *     }
 *   });
 *   // izvodjenje se nastavlja tek kad je dialog zatvoren
 *   if (raProcess.isInterrupted()) ...
 *   if (raProcess.isFailed()) ...
 * </pre>
 *
 * @author ab.f
 * @version 1.0
 */

public class raProcess {
  public static final int COMPLETED = 0;
  public static final int INTERRUPTED = -1;
  public static final int FAILED = -2;

  protected static int status;

  public static final String DEF_MESS = "Pri\u010Dekajte trenutak ...";
  public static final String DEF_TITLE = "Operacija u tijeku";
  public static final String DEF_CANCEL = "Prekid operacije ...";
  protected JraDialog win;
  protected Runnable proc;
  
  protected DataSet data;
  protected String queryString;
  protected Object loadLock = new Object();
  static Exception failException = null;
  static Object returnValue;
  
  private raStatusBar st;
  boolean shouldInterrupt, interruptEnabled;
  private static raProcess current = null;
  private static dlgErrors errs;
  private static boolean openWait = !System.getProperties().containsKey("raProcess.NOOPENWAIT");

  private raProcess(Container parent, String title, Object message, Runnable process, int steps) {
    Container realparent = null;

    if (parent instanceof JComponent)
      realparent = ((JComponent) parent).getTopLevelAncestor();
    else if (parent instanceof Window)
      realparent = parent;

    if (realparent instanceof Dialog)
      win = new JraDialog((Dialog) realparent, title, true);
    else if (realparent instanceof Frame)
      win = new JraDialog((Frame) realparent, title, true);
    else win = new JraDialog((Frame) null, title, true);
    setMessageImpl(message, true);
    win.setDefaultCloseOperation(win.DO_NOTHING_ON_CLOSE);
    win.addWindowListener(new WindowAdapter() {
      public void windowOpened(WindowEvent e) {
        beginOpeningSequence();
      }
      public void windowClosing(WindowEvent e) {
        if (interruptEnabled) beginClosingSequence();
      }
    });
    win.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_ESCAPE && interruptEnabled)
          beginClosingSequence();
      }
    });
    this.proc = process;
    errs = null;
    st = null;
    interruptEnabled = true;
    if (steps > 0) {
      st = raStatusBar.getStatusBar();
      st.setShowDelayWindow(false);
      st.getProgressBar().setDelay(1000);
      st.startTask(steps, message instanceof String ? (String) message : DEF_MESS);
    }
    win.show();
    if (errs != null && errs.countErrors() == 0) errs.hide();
    if (st != null) {
      st.finnishTask();
      st.setShowDelayWindow(true);
    }
  }  
  
  public static boolean isRunning() {
    return current != null;
  }

  private static boolean isCurrentNull() {
    if (current == null)
      new NullPointerException("No active process").printStackTrace();
    return (current == null);
  }

  private static boolean isErrorNull() {
    if (errs == null)
      new NullPointerException("Error tracing not installed").printStackTrace();
    return (errs == null);
  }

  void beginOpeningSequence() {
    if (current == null) {
      current = this;
      new Thread() {
        public void run() {
          status = FAILED;
          returnValue = null;
          failException = null;
          try {
            proc.run();
            status = COMPLETED;
          } catch (ProcessInterruptException re) {
            //
          } catch (ProcessYieldException ye) {
            status = COMPLETED;
          } catch (Exception e) {
          	failException = e;
            e.printStackTrace();
          }
          close();
        }
      }.start();
    }
  }

  void beginClosingSequence() {
    shouldInterrupt = true;
    status = INTERRUPTED;
    setMessageImpl(DEF_CANCEL, false);
    try {
      data.cancelLoading();
      synchronized (loadLock) {
        data = null;
        loadLock.notifyAll();
      }
    } catch (Exception e) {
      //
    }
  }

  private void setMessageImpl(Object obj, boolean pack) {
    JComponent m;
    if (obj instanceof String) {
      m = new JLabel((String) obj);
      ((JLabel) m).setHorizontalAlignment(SwingConstants.CENTER);
      m.setPreferredSize(new Dimension(m.getPreferredSize().width + 100,
          m.getPreferredSize().height + 20));
    } else if (obj instanceof JComponent)
      m = (JComponent) obj;
    else return;
    switchMessages(m, pack);
  }
  
  private void switchMessages(JComponent m, boolean pack) {
    win.getContentPane().removeAll();
    win.getContentPane().add(m, BorderLayout.CENTER);
    
    if (pack) {  
      Dimension size = m.getPreferredSize();
      Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
      win.pack();
      win.setLocation((scr.width - size.width) / 2, (scr.height - size.height) / 2);
    }    
    win.validate();
  }

  void close() {
    if (win != null) {
      win.dispose();
      win = null;
    }
  }
  
  public static JDialog getDialog() {
    if (isCurrentNull()) return null;
    return current.win;
  }
  

  public static void disableInterrupt() {
    if (isCurrentNull()) return;
    current.interruptEnabled = false;
  }

  public static void installErrorTrace(DataSet ds, String title) {
    installErrorTrace(ds, new String[] {}, title);
  }

  public static void installErrorTrace(DataSet ds, String col, String title) {
    installErrorTrace(ds, new String[] {col}, title);
  }

  public static void installErrorTrace(DataSet ds, String[] cols, String title) {
    if (isCurrentNull()) return;
    errs = new dlgErrors(current.win.getOwner(), title, false);
    errs.setData(ds, cols);
    errs.initWindow();
  }

  /**
   * Inicijalizira mehanizam za pamcenje gresaka nastalih u radu dugackog procesa.
   * Koristi se ako je dugi proces sastavljen od mnogo pojedinacnih akcija ad kojih
   * neke mogu biti uspjesne a druge ne. Neuspjesne akcije mogu se zapamtiti i nastaviti
   * sa izvodjenjem; na kraju procesa onda se sve greske zajedno prikazuju unutar
   * posebnog dialoga (ne-modalnog, da bi se moglo otvarati druge prozore i provjeriti
   * zbog cega su greske nastale). Interno, koristi se klasa dlgErrors.Primjer upotrebe:
   * <pre>
   *   raProcess.runChild(this, new Runnable() {
   *     public void run() {
   *       installErrorTrace(dm.getSomething(), new String[] {"CPAR", "ID", "IP"},
   *                         "Greške prilikom prijenosa");
   *       // nekakav prijenos
   *       ...
   *     }
   *   });
   *   raProcess.report("Proknjizeno " + countKnjiz + " od " + total + " dokumenata");
   * </pre>
   * Metoda se moze pozvati jedino nakon sto je proces pokrenut, dakle, unutar run() metode
   * Runnable-a specificiranog prilikom poziva raProcess.runChild().<p>
   * @param ds DataSet iz kojeg se uzimaju kolone koje ce se prikazati za svaku pogresku,
   * osim samog opisa greske. Moze biti null ako dodatne kolone osim opisa nisu potrebne.
   * @param cols dodatne kolone iz gornjeg DataSet-a, koje na neki nacin opisuju gresku.
   * @param title Naslov dialoga s greskama, ako do njih dodje, i ako korisnik odabere
   * prikaz gresaka.
   */
  public static void installErrorTrace(DataSet ds, String[] cols, int[] widths, String title) {
    if (isCurrentNull()) return;
    errs = new dlgErrors(current.win.getOwner(), title, false);
    errs.setData(ds, cols);
    errs.initWindow();
    if (widths == null) return;
    if (widths.length > cols.length + 1)
      new IllegalArgumentException("Width array too large").printStackTrace();
    for (int i = 0; i < Math.min(widths.length - 1, cols.length); i++)
      errs.setColumnWidth(cols[i], widths[i + 1]);
    if (widths.length > 0) errs.setColumnWidth(widths[0]);
  }

  public static void installErrorTrace(DataSet ds, String[] cols, Column[] other, String title) {
    if (isCurrentNull()) return;
    errs = new dlgErrors(current.win.getOwner(), title, false);
    errs.setData(ds, cols, other);
    errs.initWindow();
  }

  /**
   * Instalira error-tracing mehanizam (dlgErrors) bez inicijalizacije kolona errorSeta.
   * U to slucaju potrebno je raditi direktno sa klasom dlgErrors, koja se moze
   * dobiti metodom getErrors().
   * @param title
   */
  public static void installErrorTrace(String title) {
    if (isCurrentNull()) return;
    errs = new dlgErrors(current.win.getOwner(), title, false);
    errs.initWindow();
  }

  public static dlgErrors getErrors() {
//    if (isCurrentNull()) return null;
    return errs;
  }

  /**
   * Postavlja sirinu kolone za opis greske, ako je potrebno samo nju
   * prosiriti ili suziti.<p>
   * @param width sirina kolone opisa greske.
   */
  public static void setErrorColumnWidth(int width) {
    if (isCurrentNull() || isErrorNull()) return;
    errs.setColumnWidth(width);
  }

  /**
   * Postavlja sirinu bilo koje kolone iz DataSet-a koji opisuje greske.
   * Ako treba promijeniti sirinu svih kolona, to je zgodnije unutar metode
   * installErrorTrace().<p>
   * @param col ime kolone.
   * @param width sirina kolone.
   */
  public static void setErrorColumnWidth(String col, int width) {
    if (isCurrentNull() || isErrorNull()) return;
    errs.setColumnWidth(col, width);
  }

  /**
   * Dodaje jednu gresku u popis gresaka.<p>
   * @param opis opis greske.
   */
  public static void addError(String opis) {
    if (isCurrentNull() || isErrorNull()) return;
    errs.addError(opis);
  }

  /**
   * Dodaje jednu gresku u popis gresaka, zajedno sa vrijednostima iz
   * trenuta\u010Dnog reda DataSeta u kojem je doslo do greske. Ovaj DataSet mora
   * imati sve one kolone koje su navedene prilikom poziva metode installErrorTrace(),
   * najzgodnije je da to bude jedan te isti DataSet.<p>
   * @param opis opis greske.
   * @param ds DataSet sa vrijednostima sloga u kojem je doslo do greske.
   */
  public static void addError(String opis, DataSet ds) {
    if (isCurrentNull() || isErrorNull()) return;
    errs.addError(opis, ds);
  }

  public static void addError(String opis, DataSet ds, Object[] other) {
    if (isCurrentNull() || isErrorNull()) return;
    errs.addError(opis, ds, other);
  }
  
  public static void addError(String opis, DataSet ds, Object other) {
    if (isCurrentNull() || isErrorNull()) return;
    errs.addError(opis, ds, other);
  }

  /**
   * Prikazuje modalni dialog sa odgovarajucom porukom o zavrsetku procesa.
   * Naslov ovog dialoga je ili 'Proces zavrsen' ili 'Proces prekinut', ovisno
   * o tome kako je proces zavrsio. Ako je prilikom procesa doslo do neke
   * greske registrirane metodom addError, prikazat ce se drugaciji dialog
   * koji omogucuje prikaz nastalih pogresaka. Ovu metodu treba zvati
   * nakon sto raProcess.runChild() zavrsi (a ne unutar run() metode).<p>
   * @param text poruka koja ce biti prikazana u dialogu.
   */
  public static void report(String text) {
    report(isCompleted() ? "Proces završen" : "Proces prekinut", text);
  }

  /**
   * Isto kao report(String), s tim sto je prvi parametar naslov modalnog dialoga.
   * Najcesce je potrebno ovaj naslov (kao i poruku) prilagoditi rezultatu procesa
   * (raProcess.isInterrupted(), raProcess.isFailed(), raProcess.isCompleted()).<p>
   * @param title naslov dialoga.
   * @param text poruka dialoga.
   */
  public static void report(String title, String text) {
    String[] butts;
    if (errs == null || errs.countErrors() == 0)
      JOptionPane.showMessageDialog(null, text, title, JOptionPane.INFORMATION_MESSAGE);
    else if (JOptionPane.showOptionDialog(errs.getOwner(), text, title, JOptionPane.DEFAULT_OPTION,
      JOptionPane.ERROR_MESSAGE, null, butts = new String[] {"Greške", "Prekini"}, butts[0]) == 0)
        errs.show();
    if (errs != null && errs.countErrors() == 0) errs.hide();
    errs = null;
  }

  /**
   * Prikazuje ne-modalni dialog sa popisom gresaka, ukoliko ih uopce ima.
   * (Ako su registrirane metodom addError() prilikom izvodjenja procesa).
   * Zvati *nakon* sto raProcess.runChild() zavrsi.<p>
   */
  public static void showErrors() {
    if (isErrorNull()) return;
    if (errs.countErrors() > 0) errs.show();
    errs = null;
  }

  /**
   * @return true ako je proces uspjesno zavrsio.
   */
  public static boolean isCompleted() {
    return status == COMPLETED;
  }

  /**
   * @return true ako je korisnik prekinuo proces.
   */
  public static boolean isInterrupted() {
    return status == INTERRUPTED;
  }

  /**
   * @return true ako je proces sam bacio Exception (oznaka greske).
   */
  public static boolean isFailed() {
    return status == FAILED;
  }

  /**
   * Vra\u0107a status procesa.<p>
   * @return COMPLETED, INTERRUPTED ili FAILED.
   */
  public static int getStatus() {
    return status;
  }

  /**
   * Mijenja poruku prikazanu u dialogu.<p>
   * @param message JComponent ili String (umata se u JLabel) koji predstavlja poruku.
   * @param pack oznaka hoce li se zvati metoda pack() na dialogu. (prilagodjavanje
   * velicine dialoga novoj poruci).
   */
  public static void setMessage(Object message, boolean pack) {
    if (isCurrentNull()) return;
//    checkClosing();
    current.setMessageImpl(message, pack);
    if (current.st != null && message instanceof String)
      current.st.next((String) message);
  }

  public static void nextStep() {
    if (isCurrentNull()) return;
    checkClosing();
    if (current.st != null)
      current.st.next();
  }

  /**
   * Otvara DataSet ds uz automatsko pozivanje ds.cancelOperation() ako korisnik
   * zatvori dialog u toku punjenja tog DataSeta.<p>
   * @param ds DataSet kojeg treba otvoriti.
   */
  public static void openDataSet(DataSet ds) {
    if (isCurrentNull()) return;
    checkClosing();
    synchronized (current.loadLock) {
      current.data = ds;
    }
    ds.open();
    synchronized (current.loadLock) {
      current.data = null;
    }
    checkClosing();
  }

  /**
   * Kao prosla metoda, no ne ceka uopce da cancelLoading() zaista zavrsi
   * nego izlazi odmah. Koristiti jedino za temporary datasetove, dakle one
   * koji se koriste samo sada i rekreiraju se svaki put iznova. Tipican primjer
   * su datasetovi koje vraca vl.ExecSQL, ili KreirDrop.getTempSet(). Ukoliko
   * zaista dodje do prekida, thread u kojem se otvara taj dataset ostat ce
   * visiti sve dok cancelLoading 'prodje', stoga se taj dataset ne smije
   * u medjuvremenu koristiti ni na koji nacin.
   * @param ds Scratch dataset koji se otvara u posebnom threadu.
   */

  public static void openScratchDataSet(DataSet ds) {
    if (isCurrentNull()) return;
    checkClosing();
//AI:BLOKA!!!
    if (openWait) {
//System.out.println("openScratchDataSet:openAndWait");      
      current.openAndWait(ds);
    } else {
System.out.println("openScratchDataSet:ds.open");      
      ds.open();
    }
    checkClosing();
  }

  private void openAndWait(DataSet ds) {
    synchronized (loadLock) {
      data = ds;
    }
    new Thread() {
      public void run() {
        DataSet localData;
        synchronized (loadLock) {
          localData = data;
        }
        try {
          data.open();
        } catch (Exception e) {
          e.printStackTrace();
        }
        synchronized (loadLock) {
          if (localData == data) {
            data = null;
            loadLock.notifyAll();
          }
        }
      }
    }.start();
    try {
      synchronized (loadLock) {
        loadLock.wait();
        data = null;
      }
    } catch (Exception e) {
      //
    }
  }
  
  public static void fillDataSet(StorageDataSet ds, String query) {
    if (isCurrentNull()) return;
    checkClosing();
    synchronized (current.loadLock) {
      current.data = ds;
    }
    Util.fillReadonlyData(ds, query);
    synchronized (current.loadLock) {
      current.data = null;
    }
    checkClosing();
  }

  public static void fillScratchDataSet(StorageDataSet ds, String query) {
    if (isCurrentNull()) return;
    checkClosing();
    current.fillAndWait(ds, query);
    checkClosing();
  }
  
  private void fillAndWait(StorageDataSet ds, String query) {
    synchronized (loadLock) {
      data = ds;
      queryString = query;
    }
    new Thread() {
      public void run() {
        StorageDataSet localData;
        synchronized (loadLock) {
          localData = (StorageDataSet) data;
        }
        try {
          Util.fillReadonlyData(localData, queryString);
        } catch (Exception e) {
          e.printStackTrace();
        }
        synchronized (loadLock) {
          if (localData == data) {
            data = null;
            loadLock.notifyAll();
          }
        }
      }
    }.start();
    try {
      synchronized (loadLock) {
        loadLock.wait();
        data = null;
      }
    } catch (Exception e) {
      //
    }
  }
  
  /**
   * Metoda koja provjerava je li korisnik zatvorio dialog, te baca odgovarajuci Exception
   * koji se interno hvata i postavlja status na INTERRUPTED. Potrebno je regularno zvati
   * jer u suprotnom korisnik nece moci zatvoriti dialog (prekinuti proces).<p>
   */
  public static void checkClosing() {
    if (isCurrentNull()) return;
    if (current.shouldInterrupt)
      throw new ProcessInterruptException();
  }

  /**
   * Prekida proces i postavlja status na FAILED (interno, baca ProcessInterruptException).
   * Buduci da metoda run() interface-a Runnable ne vraca nista, povratna vrijednost moze
   * se simulirati na ovaj nacin. Ako metoda run() normalno zavrsi (sa return, ili izvodjenjem
   * zadnje naredbe bez greske), raProcess.getStatus() ce biti postavljen na COMPLETED. Ako
   * se pozove metoda fail(), status ce biti postavljen na FAILED.<p>
   */
  public static void fail() {
    if (isCurrentNull()) return;
    checkClosing();
    status = FAILED;
    throw new ProcessInterruptException();
  }
  
  /**
   * Izlazi odmah iz procesa, postavlja status COMPLETED i pamti vrijednost objekta,
   * kojeg se onda moze dobiti sa raProcess.getReturnValue(). Metoda zapravo
   * simulira return naredbu.<p>
   * @param obj Objekt kojeg raProcess treba vratiti.
   */
  public static void yield(Object obj) {
    if (isCurrentNull()) return;
    checkClosing();
    returnValue = obj;
    throw new ProcessYieldException();
  }
  
  /**
   * Vraca vrijednost postavljenu unutar procesa pomocu yield() metode.
   * Ovo je pozeljan nacin implementiranja povratne vrijednosti unutar
   * run() metode Runnable-a kojeg se daje raProcess-u, buduci da
   * ta metoda tipa void.<p>
   * @return povratnu vrijednost unutar raProcess Runnable-a.
   */
  public static Object getReturnValue() {
    return returnValue;
  }
  
  public static Exception getLastException() {
  	return failException;
  }

  /**
   * Pokrece proces maskiran dialogom. Izvodjenje ovog threada se zaustavlja dok proces
   * ne zavrsi (ili normalno, ili korisnickim prekidom, ili internom greskom). Buduci da
   * run() metoda interface-a Runnable ne vraca nikakvu vrijednost, ako je potrebno na
   * neki nacin oznaciti neuspjesnost ovog procesa, to se moze postici ili nekakvim vanjskim
   * flag-om koji se postavlja unutar run() metode, ili pak bacanjem ProcessInterruptException-a
   * (pozivom metode raProcess.fail()). U potonjem slucaju nakon izlaza iz ove metode getStatus()
   * ce biti postavljen na raProcess.FAILED (isFailed() ce vratiti true). Zapravo se moze baciti
   * bilo koji Exception ali samo gorespomenuti se smatra 'normalnim' nacinom oznacavanja
   * neuspjesnosti; ostali ce takodjer ispravno raditi ali ce se ispisati njihov StackTrace.<p>
   * @param parent owner za dialog (default: raLLFrames.getRaLLFrames().getMsgStartFrame()).
   * @param title naslov dialoga (default: 'Operacija u tijeku')
   * @param message inicijalna poruka (default: 'Pricekajte trenutak ...').
   * @param proc Runnable ciju run() metodu treba implementirati da izvodi trazeni
   * dugacki proces. Pokrece se u posebnom threadu nakon sto se pojavi modalni dialog. Thread
   * mora zavrsiti na bilo koji nacin prije nego se dialog zatvori.
   * @param steps broj koraka za raStatusBar. Ako je 0 (default), raStatusBar je inaktivan.
   */
  public static void runChild(Container parent, String title, Object message, int steps, Runnable proc) {
    if (current != null) {
      System.err.println("raProcess.runChild(): already running.");
      return;
    }
    new raProcess(parent, title, message, proc, steps);
    current = null;
  }

  public static void runChild(Container parent, String title, Object message, Runnable proc) {
    runChild(parent, title, message, 0, proc);
  }

  public static void runChild(Container parent, Runnable proc) {
    runChild(parent, DEF_TITLE, DEF_MESS, 0, proc);
  }

  public static void runChild(Container parent, int steps, Runnable proc) {
    runChild(parent, DEF_TITLE, DEF_MESS, steps, proc);
  }

  public static void runChild(String title, Object message, Runnable proc) {
    runChild(null, title, message, 0, proc);
  }

  public static void runChild(String title, Object message, int steps, Runnable proc) {
    runChild(null, title, message, steps, proc);
  }

  public static void runChild(Runnable proc) {
    runChild(null, DEF_TITLE, DEF_MESS, 0, proc);
  }

  public static void runChild(int steps, Runnable proc) {
    runChild(null, DEF_TITLE, DEF_MESS, steps, proc);
  }

}
