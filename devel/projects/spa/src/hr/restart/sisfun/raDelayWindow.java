/****license*****************************************************************
**   file: raDelayWindow.java
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
package hr.restart.sisfun;

import hr.restart.swing.JraDialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * <p>Title: raDelayWindow</p>
 * <p>Description: Klasa koja prikazuje prozor s porukom za vrijeme trajanja
 * nekog procesa.</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: REST-ART</p>
 * Klasa koja prikazuje prozor sa porukom za vrijeme trajanja nekog dužeg
 * (i nepredvidivog) procesa. Primjer upotrebe:
 * <pre>
 *   raDelayWindow proc = raDelayWindow.show(this.getWindow());
 *   ...
 *   (duga\u010Dki proces)
 *   ..
 *   proc.close();
 * <pre>
 * Sam prozor \u0107e se prikazati 100 ms nakon poziva metode show(), tako da
 * se izbjegne njegovo prikazivanje ako se proces vrlo brzo završi.<p>
 * Klasa se NIKAKO ne smije pozivati iz event-dispatch threada!
 * (Što je i logi\u010Dno, jer ako smo u event-dispatch threadu onda je
 * osvježavanje GUI-ja blokirano).<p>
 * Unutar duga\u010Dkog procesa može se po potrebi pozivati metoda setMessage(),
 * kojom se može promijeniti poruka u prozoru (sa kašnjenjem od 100 ms).<p>
 * Implementirano je mnogo statickih show() metoda koje vra\u0107aju referncu na
 * objekt tipa raDelayWindow, a razlikuju se po broju i vrsti parametara.
 * Verzije u kojima se neki parametar ne šalje, koriste default vrijednosti za
 * njega. Npr. bez ijednog parametra prozor \u0107e imati naslov "Operacija u tijeku",
 * tekst poruke "Pri\u010Dekajte trenutak ...", a kašnjenje u prikazu prozora 100 ms.<p>
 * Parametar poruke može biti String, koji \u0107e se automatski wrapati u JLabel, ili
 * izravno JComponent koji \u0107e se prikazati.
 *
 * @author abf
 * @version 1.0
 */

public class raDelayWindow implements Runnable {
  private static final String DEF_MESS = "Pri\u010Dekajte trenutak ...";
  private static final String DEF_TITLE = "Operacija u tijeku";
  private static final int DEF_DELAY = 100;
  private JDialog win;
  private boolean open, interruptable, interrupted;
  private int delay, vis, invis, serial;
  private Thread process;

  private raDelayWindow() {
  }

  private raDelayWindow(Container parent, String title, Object message, int delay) {
    Container realparent = null;

    if (SwingUtilities.isEventDispatchThread())
      throw new IllegalStateException("raDelayWindow cannot be called from the EventDispatchThread");

    if (parent instanceof JComponent)
      realparent = ((JComponent) parent).getTopLevelAncestor();
    else if (parent instanceof Window)
      realparent = parent;

    this.delay = Math.max(delay, 50);
    this.open = true;
    this.interruptable = false;

    if (realparent instanceof Dialog)
      win = new JraDialog((Dialog) realparent, title, true);
    else if (realparent instanceof Frame)
      win = new JraDialog((Frame) realparent, title, true);
    else win = new JraDialog((Frame) null, title, true);
    setMessage(message, true, 0);
    win.setDefaultCloseOperation(win.DO_NOTHING_ON_CLOSE);
    win.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        if (process != null && process.isAlive()) {
          interrupted = true;
          process.interrupt();
          close();
        } else if (interruptable) {
          interrupted = true;
          close();
        }
      }
    });
    process = null;
    new Thread(this).start();
  }

  /**
   * Prikazuje (nakon 100 ms) prozor s porukom i vra\u0107a referencu na njega.<p>
   * @param parent owner komponenta (ili prozor).
   * @param title naslov prozora.
   * @param message poruka.
   * @return referencu na raDelayWindow.
   */
  public static raDelayWindow show(Container parent, String title, Object message) {
    return new raDelayWindow(parent, title, message, DEF_DELAY);
  }

  /**
   * Prikazuje (nakon delay ms) prozor s porukom i vra\u0107a referencu na njega.<p>
   * @param parent owner komponenta (ili prozor).
   * @param title naslov prozora.
   * @param message poruka.
   * @param delay kašnjenje prikazivanja prozora u ms.
   * @return referencu na raDelayWindow.
   */
  public static raDelayWindow show(Container parent, String title, Object message, int delay) {
    return new raDelayWindow(parent, title, message, delay);
  }

  /**
   * Prikazuje (nakon 100 ms) prozor s porukom "Pri\u010Dekajte trenutak ..."
   * i vra\u0107a referencu na njega.<p>
   * @param parent owner komponenta (ili prozor).
   * @param title naslov prozora.
   * @return referencu na raDelayWindow.
   */
  public static raDelayWindow show(Container parent, String title) {
    return new raDelayWindow(parent, title, DEF_MESS, DEF_DELAY);
  }

  /**
   * Prikazuje (nakon delay ms) prozor s porukom "Pri\u010Dekajte trenutak ..."
   * i vra\u0107a referencu na njega.<p>
   * @param parent owner komponenta (ili prozor).
   * @param title naslov prozora.
   * @param delay kašnjenje prikazivanja prozora u ms.
   * @return referencu na raDelayWindow.
   */
  public static raDelayWindow show(Container parent, String title, int delay) {
    return new raDelayWindow(parent, title, DEF_MESS, delay);
  }

  /**
   * Prikazuje (nakon 100 ms) prozor naslovljen "Operacija u tijeku" s porukom
   * "Pri\u010Dekajte trenutak ..." i vra\u0107a referencu na njega.<p>
   * @param parent owner komponenta (ili prozor).
   * @return referencu na raDelayWindow.
   */
  public static raDelayWindow show(Container parent) {
    return new raDelayWindow(parent, DEF_TITLE, DEF_MESS, DEF_DELAY);
  }

  /**
   * Prikazuje (nakon delay ms) prozor naslovljen "Operacija u tijeku" s porukom
   * "Pri\u010Dekajte trenutak ..." i vra\u0107a referencu na njega.<p>
   * @param parent owner komponenta (ili prozor).
   * @param delay kašnjenje prikazivanja prozora u ms.
   * @return referencu na raDelayWindow.
   */
  public static raDelayWindow show(Container parent, int delay) {
    return new raDelayWindow(parent, DEF_TITLE, DEF_MESS, delay);
  }

  /**
   * Prikazuje prozor (nakon 100 ms) bez ownera nalovljen "Operacija u tijeku"
   * s porukom "Pri\u010Dekajte trenutak ..." i vra\u0107a referencu na njega.<p>
   * @return referencu na raDelayWindow.
   */
  public static raDelayWindow show() {
    return new raDelayWindow(null, DEF_TITLE, DEF_MESS, DEF_DELAY);
  }

  /**
   * Prikazuje prozor (nakon delay ms) bez ownera nalovljen "Operacija u tijeku"
   * s porukom "Pri\u010Dekajte trenutak ..." i vra\u0107a referencu na njega.<p>
   * @param delay kašnjenje prikazivanja prozora u ms.
   * @return referencu na raDelayWindow.
   */
  public static raDelayWindow show(int delay) {
    return new raDelayWindow(null, DEF_TITLE, DEF_MESS, delay);
  }

  /**
   * Implementacija Runnable interface-a. Koristi se interno.
   */
  public void run() {
    try {
      Thread.sleep(delay);
    } catch (Exception e) {}
    if (open) {
      win.show();
    }
  }
  /**
   * Odre\u0111uje ho\u0107e li prikazani dialog biti modalni (default) ili ne. Ako
   * je modalni, GUI \u0107e biti blokiran sve dok se dialog ne zatvori, što je
   * u ve\u0107ini slu\u010Dajeva zgodno. Medotu je najzgodnije pozivati direktno
   * pri otvaranju, jer vra\u0107a referencu na isti objekt:<p>
   * <code>raDelayWindow load = raDelayWindow.show().setModal(false);</code><p>
   * @param modal true za modalni dialog.
   * @return referencu na ovaj raDelayWindow.
   */

  public raDelayWindow setModal(boolean modal) {
    if (!win.isShowing()) {
      win.setModal(modal);
      if (!modal)
        win.setDefaultCloseOperation(win.HIDE_ON_CLOSE);
    }
    return this;
  }

  /**
   * Omogucuje "prekidanje" procesa maskiranog ovim raDelayWindow-om.
   * Ukoliko korisnik zatvori prozor (sto po defaultu nije moguce)
   * postavit ce se odredjeni flag. Na korisniku je da dovoljno cesto
   * provjerava ovaj flag metodom isInterrupted i na odgovarajuci nacin
   * prekine izvodjenje procesa. Druga mogucnost je metoda setProcessingThread().<p>
   * @param inter true da se omoguci zatvaranje procesa.
   * @return this, za ulancavanje.
   */
  public raDelayWindow setInterruptable(boolean inter) {
    interruptable = inter;
    return this;
  }

  /**
   * Vraca indikator je li korisnik zatvorio prozor. Vidi metodu
   * setInterruptable().<p>
   * @return ditto.
   */
  public boolean isInterrupted() {
    return interrupted;
  }

  /**
   * Zadaje thread koji se vrti dok je delayWindow prikazan.
   * Ako se delayWindow zatvori, poslat ce ovom threadu
   * interrupt signal. Hvatanje ovog interrupta je odgovornost
   * pozivajuce klase, a za primjer pogledati metodu
   * setProcessingThreadCurrent(). Drugi nacin prekidanja procesa
   * moguce je postici sa setInterruptable(true) i onda periodicki
   * provjeravati je li prozor zatvoren metodom isInterrupted().
   * @param t receni Thread.
   * @return this instancu, za ulancavanje.
   */

  public raDelayWindow setProcessingThread(Thread t) {
    process = t;
    return this;
  }

  /**
   * Postavlja trenutacno aktivni Thread kao thread koji se vrti dok
   * je delayWindow prikazan. Vidi setProcessingThread(Thread) metodu.
   * Primjer: <pre>
   * Thread process = new Thread() {
   *   public void run() {
   *     someLenghtyProcessing();
   *   }
   * };
   * process.start();
   * raDelayWindow dw = raDelayWindow.show().setProcessingThreadCurrent();
   * try {
   *   process.join();   // Ceka da Thread process zavrsi
   *   dw.close();       // Zavrsio je, zatvori delayWindow
   * } catch (InterruptedException e) {
   *   // Korisnik je zatvorio prozor, uradi sto vec treba.
   *   // npr dataset.cancelLoading(), itd.
   * }
   * </pre>
   * @return this instancu, za ulancavanje.
   */

  public raDelayWindow setProcessingThreadCurrent() {
    process = Thread.currentThread();
    return this;
  }


  /**
   * Mijenja poruku na prozoru bez promjene dimenzija prozora.<p>
   * @param obj nova poruka.
   */
  public void setMessage(Object obj) {
    setMessage(obj, false, 0);
  }

  /**
   * Mijenja poruku na prozoru.<p>
   * @param obj nova poruka.
   * @param pack flag koji odre\u0111uje zove li se nakon promjene poruke metoda
   * pack() (promjena veli\u010Dine) na prozoru ili ne.
   * @param delay odre\u0111uje kašnjenje promjene poruke u ms. Ako prije isteka ovog
   * kašnjenja bude opet pozvana metoda setMessage, prošla \u0107e biti zanemarena.
   */
  public void setMessage(Object obj, boolean pack, int delay) {
    JComponent m;
    if (obj instanceof String) {
      m = new JLabel((String) obj);
      ((JLabel) m).setHorizontalAlignment(SwingConstants.CENTER);
      m.setPreferredSize(new Dimension(m.getPreferredSize().width + 100,
          m.getPreferredSize().height + 20));
    } else if (obj instanceof JComponent)
      m = (JComponent) obj;
    else return;
    ++serial;
    if (delay == 0)
      switchMessages(m, pack);
    else
      new Thread(new Delayed(m, serial, delay, pack)).start();
  }

  public void close() {
    open = false;
//    win.hide();
    if (win != null) {
      win.dispose();
      win = null;
    }
  }

  private void switchMessages(JComponent m, boolean pack) {
//    System.out.println("win = "+win);
//    System.out.println("win.getContentPane() = " +win.getContentPane());
    win.getContentPane().removeAll();
    win.getContentPane().add(m, BorderLayout.CENTER);
    if (pack) {
      win.pack();
      Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
      win.setLocation((scr.width - win.getSize().width) / 2, (scr.height - win.getSize().height) / 2);
    } else
      win.validate();
  }

  private class Delayed implements Runnable {
    private int serial, delayed;
    private boolean pack;
    private JComponent mess;
    public Delayed(JComponent m, int serial, int delay, boolean pack) {
      this.serial = serial;
      delayed = delay;
      mess = m;
      this.pack = pack;
    }
    public void run() {
      try {
        Thread.sleep(delayed);
      } catch (Exception e) {}
      if (raDelayWindow.this.open && raDelayWindow.this.serial == serial)
        switchMessages(mess, pack);
    }
  }
}
