/****license*****************************************************************
**   file: raUpitLite.java
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

import hr.restart.swing.JraButton;
import hr.restart.swing.JraScrollPane;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Sort;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;

public abstract class raUpitLite extends raFrame implements ResetEnabled {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();

  public OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {      
       ok_action_thread();
    }
    public void jPrekid_actionPerformed() {
//      firstESC();
      cancelPress();
    }
  };

  hr.restart.util.reports.raRunReport RepRun = hr.restart.util.reports.raRunReport.getRaRunReport();

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  private javax.swing.JPanel jPan;
  protected boolean busy = false;
  private boolean noData = false;
  private boolean interrupted = false;
  BorderLayout borderLayout2 = new BorderLayout();
  JraScrollPane jScroll = new JraScrollPane() {
    public boolean isFocusCycleRoot() {
      return true;
    }
  };

  /**
   * Support za raKeyActione
   */
  protected raKeyActionSupport keySupport;
  /**
   * Akcija koja se odvija pritiskom na ok button
   * sluzi samo za removeKeyAction ili replaceKeyAction
   * Automatski se replaca u changeIcon(mode)
   */
  public raKeyAction oKButtonKeyAction;
  /**
   * Akcija koja se odvija pritiskom na F5
   * sluzi samo za removeKeyAction ili replaceKeyAction
   * POZOR: ima vrijednost null kada je isIspis() == false
   */
  public raKeyAction oKButtonKeyActionF5;
  /**
   * Akcija pritiska na escape
   * sluzi samo za removeKeyAction ili replaceKeyAction
   */
  public raKeyAction raKeyEsc = new raKeyAction(KeyEvent.VK_ESCAPE, okp.jPrekid.getText()) {
      public void keyAction() {
        interrupted = false;
        saveFatChanges();
        if (runFirstESC()) {
          changeIcon(2);
          firstESC();
        }
        else {
          cancelPress();
        }
      }
    };
  /**
   * Akcija pritiska na F6
   * sluzi samo za removeKeyAction ili replaceKeyAction
   */
  public raKeyAction raKeyF6 = new raKeyAction(KeyEvent.VK_F6, "Detalji") {
      public void keyAction() {
        keyF6Press();
      }
    };

  public raUpitLite(int mode, Container owner) {
    super(mode,owner);
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public raUpitLite() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @deprecated koristi addKeyAction
   * @see #addKeyAction(raKeyAction)
   * @param l
   */
  public void addKeyListener(KeyListener l) {
    super.addKeyListener(l);
  }
  private void jbInit() throws Exception {
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
//        firstESC();
        cancelPress();
      }
    });
    this.getContentPane().setLayout(borderLayout1);
     /*//REPLACANO SA raKeyActionSupport
     this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        this_keyPressed(e);
      }
//      public void keyTyped(KeyEvent e) {
//        this_keyTyped(e);
//      }
    });
      */
    keySupport = new raKeyActionSupport(this);
    addKeys();
    jPanel1.setLayout(borderLayout2);
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        rcc.setLabelLaF(okp.jReset, true);
        this_componentShown(e);
      }
    });
    jPanel1.add(jScroll, BorderLayout.CENTER);
    getOKPanel().jPrekid.setFocusPainted(false);
    if (this instanceof hr.restart.util.raUpit) this.getContentPane().add(jPanel1, BorderLayout.NORTH);
    else if (this instanceof hr.restart.util.raTwoTableFrame) this.getContentPane().add(jPanel1, BorderLayout.NORTH);
    else this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
  }
/**
 * ako hoces da odmah nakon upita ode u ispis - returnaj true,
 * logicno bi bilo u kombinaciji da isIspis() returna false tako da se na F5 nista ne dogodi
 * @return
 */
  public boolean ispisNow() { 
    return false;
  }
  public void openDataSet(DataSet ds) {
    raProcess.openDataSet(ds);
  }
  public void openScratchDataSet(DataSet ds) {
    raProcess.openScratchDataSet(ds);
  }
  
  public void fillDataSet(StorageDataSet ds, String query) {
    raProcess.fillDataSet(ds, query);
  }
  
  public void fillScratchDataSet(StorageDataSet ds, String query) {
    raProcess.fillScratchDataSet(ds, query);
  }
  
  private boolean validacijaInProcess = false;
  /**
   * Ako netko hoce zavrtit validaciju u raProcess-u (neke klase i rade sve u validaciji)
   * @param validacijaInProcess
   */
  public void setValidacijaInProcess(boolean validacijaInProcess) {
    this.validacijaInProcess = validacijaInProcess;
  }
  /**
   * Dodano zbog raUpitFat-a zbog cudnog ponasanja prilikom ne prolazenja validacije
   */
  public boolean validationPass = true;

  
  boolean startUpit() {
    interrupted = false;
    if (!isIspis()) {
      if (validacijaInProcess) {
        raProcess.runChild(this.getWindow(), new Runnable() {
          public void run() {
            validationPass = Validacija();
          }
        });
      } else {
        validationPass = Validacija();
      }
      
      if (validationPass) {        
        getOKPanel().jPrekid.requestFocus();
        rcc.EnabDisabAllLater(getJPan(), false);
        raProcess.runChild(this.getWindow(), raProcess.DEF_TITLE, runMessage, stepsNo,
        new Runnable() {
          public void run() {
            raStatusBar status = getStatusBar();
            status.getProgressBar().setDelay(delay);
//            status.startTask(stepsNo,runMessage);
            try {
//              enableEvents(false);
              noData = false;
              okPress();
//              enableEvents(true);
            } catch (ProcessInterruptException re) {
              throw (ProcessInterruptException) re.fillInStackTrace();
            } catch (Exception ex) {
              ex.printStackTrace();
              raProcess.fail();
            } finally {
              enableEvents(true);
            }
          }
        });
        if (raProcess.isInterrupted()) interrupted();
        else if (raProcess.isCompleted()) {
          upitCompleted();
          showMessage();
//            setEnabled(true);
          if (ispisNow()){
            ispis();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                firstESC();
              }
            });
            rcc.setLabelLaF(okp.jReset, true);
          }
          if (isIspis()) {
            changeIcon(1);
          }
        }
        myAfterOKPress();
//        new okPressThread().start();
      }
      return true;
    } else return false;
  }
  protected void upitCompleted() {
    // za overridanje, sto uciniti kad dohvat usjesno zavrsi
  }
  public void ok_action() {
    interrupted = false;
    if (!startUpit()) {
      ispis();
    }
  }
  
  public void ok_action_thread() {
    if (!busy) {
      busy = true;
      new Thread() {
        public void run() {
          try {
            ok_action();
          } finally {
            busy = false;
          }
        }
      }.start();
    }
  }
  
  void saveFatChanges() {
    if (raUpitLite.this instanceof raUpitFat) {
      raUpitFat fat = (raUpitFat) raUpitLite.this;
      if (fat.getJPTV().getStorageDataSet() != null)
        fat.getJPTV().getColumnsBean().saveSettings();
    }
  }
  
  public void addKeyAction(raKeyAction ac) {
    keySupport.addKeyAction(ac);
  }
  public void removeKeyAction(raKeyAction ac) {
    keySupport.removeKeyAction(ac);
  }

  private void addOKButtonKeyAction() {
    raKeyAction newOKButtonKeyAction = getOKButtonKeyAction();
    if (oKButtonKeyAction != null) {
      keySupport.replaceKeyAction(oKButtonKeyAction, newOKButtonKeyAction);
    } else {
      keySupport.addKeyAction(newOKButtonKeyAction);
    }
    oKButtonKeyAction = newOKButtonKeyAction;
    if (oKButtonKeyActionF5!=null) {
      removeKeyAction(oKButtonKeyActionF5);
      oKButtonKeyActionF5 = null;
    }
    try {
      if (isIspis() && !okp.jBOK.getText().equals("OK")) {
        oKButtonKeyActionF5 = getOKButtonKeyActionF5();
        addKeyAction(oKButtonKeyActionF5);
      }
    } catch (Exception e) {
//      System.out.println("if (isIspis()) : ex :" + e);
    }
  }
  private void clearHelpContainer() {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        hr.restart.help.raHelpContainer.getInstance().clearHP();
      }
    });
  }
  private void initHelpContainer() {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        hr.restart.help.raHelpContainer.getInstance().clearHP();
        hr.restart.help.raHelpContainer.getInstance().initWith(keySupport);
      }
    });
  }
  private raKeyAction getOKButtonKeyAction() {
    raKeyAction rky = new raKeyAction(KeyEvent.VK_F10, okp.jBOK.getText()) {
      public void keyAction() {        
        if (okp.jBOK.isEnabled()) {
          okp.jBOK.requestFocus();
          ok_action_thread();
        }
      }
    };
    rky.setIcon(okp.jBOK.getIcon());
    return rky;
  }

  private raKeyAction getOKButtonKeyActionF5() {
    raKeyAction rky = new raKeyAction(KeyEvent.VK_F5, okp.jBOK.getText()) {
      public void keyAction() {
        if (isIspis()) {
          ispis();
        }
      }
    };
    rky.setIcon(okp.jBOK.getIcon());
    return rky;
  }

  //dodaje ove iz this_keyPressed
  private void addKeys() {
    //F10 i F5 inicijalni
    addOKButtonKeyAction();
    //Escape
    raKeyEsc.setIcon(okp.jPrekid.getIcon());
    addKeyAction(raKeyEsc);
    //F6
    addKeyAction(raKeyF6);
    keySupport.registerKeyActions(this.getWindow());
  }
  /**
   * @deprecated use raKeySupport
   * @param e
   */
  protected void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_F10) {
      e.consume();      
      if (okp.jBOK.isEnabled()) {
        okp.jBOK.requestFocus();
        ok_action_thread();
      }
    }
    if (e.getKeyCode()==e.VK_F5) {
      if (isIspis()) {
        ispis();
      }
    }
    else if (e.getKeyCode()==e.VK_ESCAPE) {
      e.consume();
      interrupted = false;
      saveFatChanges();
      if (runFirstESC()) {
        changeIcon(2);
        firstESC();
      }
      else {
        cancelPress();
      }
    }
    else if (e.getKeyCode()==e.VK_F6) {
      keyF6Press();
      e.consume();
    }
  }

/*  void this_keyTyped(KeyEvent e) {
    if (e.getKeyChar()==e.VK_ESCAPE) {
      e.consume();
      interrupted = false;
      if (runFirstESC()) {
        changeIcon(2);
        firstESC();
      }
      else {
        cancelPress();
      }
    }
  }*/
/**
 * Kaj da se desi kad pritisnemo OK
 */
  public abstract void okPress();
/**
 * Kaj da se desi kad pritisnemo Cancel
 */
  public void cancelPress() {
    saveFatChanges();
    this.setVisible(false);
    rcc.EnabDisabAllLater(getJPan(), true);
    interrupted = false;
    clearHelpContainer(); //dvaput je dvaput
  }
/**
 * Da li da potjera prvi escape
 */
  public abstract boolean runFirstESC();
/**
 * Kaj da se desi kad pritisnemo ESC, ako je runFirstESC()=true
 */
  public abstract void firstESC();

  public void interrupted() {
    interrupted = true;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        firstESC();
      }
    });
  }

  public boolean isInterrupted() {
    return interrupted;
  }
  
  public void installResetButton() {
    okp.addResetButton(this);
  }
  
  public void resetDefaults() {
    
  }

/**
 * Kaj da se radi na kad se pojavi na ekranu
 */
  public abstract void componentShow();
/**
 * Kaj da radi na pritisak F6
 */
  public void keyF6Press() {
  }
  /**
 * Setiranje panela
 */
  public void setJPan(javax.swing.JPanel newJPan) {
    jPan = newJPan;
//    jScroll.getViewport().setLayout(new GridBagLayout());
    jScroll.getViewport().add(jPan);
    RepRun.setOwner(jPan, getClass().getName());
  }
  /**
   * Vraca panel
   * @return
   */
  public javax.swing.JPanel getJPan() {
    return jPan;
  }
  void this_componentShown(ComponentEvent e) {
    changeIcon(2);
    componentShow();
  }
/*  public void show() {
    changeIcon(2);
    componentShow();
    super.show();
  }*/

  public hr.restart.util.reports.raRunReport getRepRunner() {
    return RepRun;
  }
  private String[] provider = new String[0];
  private String[] source = new String[0];
  private String[] design = new String[0];
  private String[] rTitle = new String[0];
  private int[] dataSrcIdx = new int[0];
/**
 * ne radi ama bas njente
 * @param newProvider
 * @param newRTitle
 */
  public void addReport(String newProvider,String newRTitle) {
    addReport(newProvider,newRTitle,0);
  }
  private String[] addTo(String[] ss, String s) {
    String[] newss = Util.getUtil().concatArrayStr(ss,s);
    return newss;
  }
  private int[] addTo(int[] is, int i) {
    int[] newis = new int[is.length+1];
    for (int x=0;x<is.length;x++) newis[x] = is[x];
    newis[is.length] = i;
    return newis;
  }
/**
 * Dodavanje reporta
 * @param newProvider
 * @param newRTitle
 * @param dsIdx
 */
  public void addReport(String newProvider,String newRTitle, int dsIdx) {
    provider = addTo(provider,newProvider);
    rTitle = addTo(rTitle,newRTitle);
    source = addTo(source,null);
    dataSrcIdx = addTo(dataSrcIdx,dsIdx);
    design = addTo(design, null);
  }
  public void addReport(String newProvider,String newSource, String newDesign, String newRTitle) {
    provider = addTo(provider,newProvider);
    rTitle = addTo(rTitle,newRTitle);
    source = addTo(source,newSource);
    dataSrcIdx = addTo(dataSrcIdx,0);
    design = addTo(design, newDesign);
  }
  public void addJasper(String newProvider,String newSource, String newDesign, String newRTitle) {
    provider = addTo(provider,newProvider);
    rTitle = addTo(rTitle,newRTitle);
    source = addTo(source,newSource);
    dataSrcIdx = addTo(dataSrcIdx,-1);
    design = addTo(design, newDesign);
  }
  public void addReport(String newProvider, String newSource, String newRTitle) {
    provider = addTo(provider,newProvider);
    rTitle = addTo(rTitle,newRTitle);
    source = addTo(source,newSource);
    dataSrcIdx = addTo(dataSrcIdx,0);
    design = addTo(design, null);
  }
  public void ispis(){

    beforeReport();
    getRepRunner().go();
    afterReport();
  }

  public void afterReport(){
    enableEvents(true);
  }

  public void beforeReport(){
    enableEvents(false);
    this.getRepRunner().clearAllCustomReports();
    if (provider == null) return;
    for (int i=0;i<provider.length;i++) {
      if (source[i]==null) {
        getRepRunner().addReport(provider[i],rTitle[i],dataSrcIdx[i]);
      } else if (design[i]==null) {
        getRepRunner().addReport(provider[i], source[i], rTitle[i]);
      } else if (dataSrcIdx[i] >= 0) {
        getRepRunner().addReport(provider[i], source[i], design[i], rTitle[i]);
      } else {
        getRepRunner().addJasper(provider[i], source[i], design[i], rTitle[i]);
      }
    }
    addHooks();
  }
  
  public void addHooks() {
    // for overriding
  }
  
  public void changeIcon(int mod) {
    if (mod==1) {
      okp.jBOK.setText("Ispis");
      okp.jBOK.setIcon(raImages.getImageIcon(raImages.IMGPRINT));
    }
    else if (mod==2) {
      okp.jBOK.setText("OK");
      okp.jBOK.setIcon(raImages.getImageIcon(raImages.IMGOK));
    }
    else if (mod==3) {
      okp.jBOK.setText("Spremi");
      okp.jBOK.setIcon(raImages.getImageIcon(raImages.IMGSAVE));
    }
    addOKButtonKeyAction();
    initHelpContainer();
  }
/**
 * Dohvat okPanela
 * @return
 */
  public hr.restart.util.OKpanel getOKPanel() {
    return okp;
  }
/**
 * Ponistavanje svih reporta
 */
  public void killAllReports() {
    design = new String[0];
    source = new String[0];
    provider = new String[0];
    rTitle = new String[0];
    dataSrcIdx = new int[0];
  }

//progressBar
  private int stepsNo = 10;
  private String runMessage = "Priprema podataka u tijeku...";
  private int delay = 100;
/**
 * Svakih koliko milisekundi ce se progressbar sam pomaknuti ako se ne dogodi nextStep()
 * default je 100 (desetinka sekunde)
 * @param milis
 */
  public void setProgressIdleDelay(int milis) {
    delay = milis;
  }
  public int getStepsNumber() {
    return stepsNo;
  }
  /**
   * Zadaje se u koliko stepova ce raditi upit (ako se nista ne dogodi za delay milisekundi)
   * progressbar se pomice sam. Default je 10 (bezveze)
   * @param n
   */
  public void setStepsNumber(int n) {
    stepsNo = n;
  }
  public String getRunMessage() {
    return runMessage;
  }
  /**
   * Poruka koja se pojavljuje u mesagebaru kad krene upit. Default = Priprema podataka u tijeku...
   * @param mess poruka
   */
  public void setRunMessage(String mess) {
    runMessage = mess;
  }
  /**
   * evo koda return raStatusBar.getStatusBar();
   * @return
   */
  public raStatusBar getStatusBar() {
    return raStatusBar.getStatusBar();
  }
  /**
   * Povecava se lenta na progressbaru (povecat ce se i sama nakon delay milisekundi)
   */
  public void nextStep() {
    getStatusBar().next();
  }
  /**
   * Povecava se lenta na progressbaru i mijenja se poruka u messagebaru
   * @param msg
   */
  public void nextStep(String msg) {
    getStatusBar().next(msg);
    if (raProcess.isRunning()) {//moram ovako jer raProcess.isCurrentNull printa stack trace 
      												  //u log ako nije pokrenut raProcess sto uvelike zbunjuje 
      													//onoga tko debugira nesto korisno  
      raProcess.setMessage(msg, true);
    }
  }
  protected void enableEvents(boolean en) {
  }
  public boolean isIspis() {
    return true;
  }

/*  class okPressThread extends Thread {
    public void run() {
      setEnabled(false);
      raStatusBar status = getStatusBar();
      status.getProgressBar().setDelay(delay);
      status.startTask(stepsNo,runMessage);
      try {
        enableEvents(false);
        okPress();
        enableEvents(true);
      }
      catch (Exception ex) {
        enableEvents(true);
        ex.printStackTrace();
      }
      status.finnishTask();
      showMessage();
      setEnabled(true);
      if (ispisNow()){
        ispis();
        firstESC();
      }
      if (isIspis()) {
        changeIcon(1);
      }
      myAfterOKPress();
    }
  } */
  public boolean Validacija() {
    return true;
  }
  public void showMessage() {
  }
  public String getNoDataMessage() {
    return "Nema podataka koji zadovoljavaju tražene uvjete!";
  }
  private String miscNoDataMessage = "";

  private void myAfterOKPress() {
//    SwingUtilities.invokeLater(new Runnable() {
//      public void run() {
//        raUpitLite.this.getWindow().toFront();
//      }
//    });
    if (noData) {
      noData = false;
      if (!miscNoDataMessage.equals("")) {
        JOptionPane.showMessageDialog(this.getWindow(), miscNoDataMessage,
            "Poruka", JOptionPane.INFORMATION_MESSAGE);
        miscNoDataMessage = "";
      } else {
      JOptionPane.showMessageDialog(this.getWindow(), getNoDataMessage(),
         "Poruka", JOptionPane.INFORMATION_MESSAGE);
      }
      interrupted();
    }
    afterOKPress();
  }
  public void setNoDataAndReturnImmediately() {
    noData = true;
    raProcess.fail();
  }
  
  public void setNoDataAndReturnImmediately(String oneTimeMessage) {
    miscNoDataMessage = oneTimeMessage;
    noData = true;
    raProcess.fail();
  }
  
  public void checkClosing() {
    raProcess.checkClosing();
  }
  
  public void setMessage(String msg) {
    raProcess.setMessage(msg, false);
  }
  
  public void setNewMessage(String msg) {
    raProcess.setMessage(msg, true);
  }
  
  public void afterOKPress() {
    //
  }


  // podrška za sort
  private boolean cummulative;
  private ArrayList sel;
  private JLabel sortLabel;
  private JPanel pd;
  private JraButton sortButton;
  private JPopupMenu pop;
  private JMenuItem remove;

  public void clearAllSorts() {
    if (pop != null) {
      okp.remove(pd);
      pd = null;
      pop = null;
    }
  }

  public void setSortVisible(boolean vis) {
    if (pop != null && okp.isAncestorOf(pd) != vis) {
      if (!vis) okp.remove(pd);
      else okp.add(pd);
      okp.repaint();
    }
  }

  /**
   * Dodaje jednu mogucnost sortiranja, odredjenu tekstom koji se prikazuje
   * u izborniku (razumljiv korisniku) te stvarnim imenom kolone u query-ju.
   * Upozorenje: nema nikakvih provjera! Bude pucalo ako se posalje neko
   * nepostojece ime. Za sortiranje nizbrdo staviti znak '-' ispred imena
   * kolone (npr.  '-SALDO'). Ako je polje tekstualno i treba ga sortirati
   * s nasim znakovima treba na pocetku staviti dolar, npr. NAZPAR. Nije
   * moguce kombinirati ova sva znaka a i ne znam cemu bi sluzilo sortiranje
   * od z do a?<p>
   * @param col ime kolone u bazi. Moze biti kvalificiran (tablica.kolona)
   * ali onda se ne smije koristiti getSortDescriptor() nego iskljucivo getOrderBy(),
   * sto je i bolje.
   * @param name tekst koji se prikazuje u izborniku.
   */
  public void addSortOption(String col, String name) {
    if (pop == null) {
      pd = new JPanel(null);
      pd.setLayout(new BoxLayout(pd, BoxLayout.X_AXIS));

      sortLabel = new JLabel("");
      sortLabel.setFont(sortLabel.getFont().deriveFont(Font.ITALIC));
      sortLabel.setMinimumSize(new Dimension(10, 10));
      sortButton = new JraButton();
      sortButton.setText("Slijed");
      sortButton.setIcon(raImages.getImageIcon(raImages.IMGALIGNCENTER));
      sortButton.setPreferredSize(new Dimension(100,25));

      pd.add(sortButton);
      pd.add(Box.createHorizontalStrut(10));
      pd.add(sortLabel);
      pd.add(Box.createHorizontalStrut(10));
      okp.add(pd, BorderLayout.CENTER);
      okp.revalidate();
      okp.repaint();

      pop = new JPopupMenu();
      remove = new JCheckBoxMenuItem("Prirodni poredak");
      remove.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          removeSort();
        }
      });
      remove.setSelected(true);
      pop.addSeparator();
      pop.add(remove);

      sortButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          pop.show(sortButton, 0, 0);
        }
      });
      sortLabel.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          if (e.isPopupTrigger()) pop.show(sortLabel, 0, 0);
        }
        public void mousePressed(MouseEvent e) {
          if (e.isPopupTrigger()) pop.show(sortLabel, 0, 0);
        }
        public void mouseReleased(MouseEvent e) {
          if (e.isPopupTrigger()) pop.show(sortLabel, 0, 0);
        }
      });
    }
    pop.insert(new AddSortAction(col, name), pop.getComponentCount() - 2);
  }
  
/*  public void setSortOptionEnabled(int opt, boolean enabled) {
    if (opt >= pop.getComponentCount() - 2)
      throw new IllegalArgumentException("Invalid sort option index");
    pop.getComponent(opt).setVisible(enabled);
    if 
  }*/

  class AddSortAction extends JCheckBoxMenuItem implements ActionListener {
    private String col;
    public AddSortAction(String col, String name) {
      super(name);
      this.col = col;
      addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
      addSortColumn(this);
    }
    public String getColumn() {
      return col;
    }
    public String toString() {
      return Character.toLowerCase(getText().charAt(0)) + getText().substring(1);
    }
  }

  public void addSortOptions(String[] cols, String[] names) {
    for (int i = 0; i < cols.length; i++)
      addSortOption(cols[i], names[i]);
  }

  public void addSortOptions(String[][] colname) {
    for (int i = 0; i < colname.length; i++)
      addSortOption(colname[i][0], colname[i][1]);
  }

  /**
   * Odredjuje moze li se postaviti vise polja za sort, ili samo jedno.
   * @param cum true za vise polja, false za samo jedno.
   */
  public void setSortCummulative(boolean cum) {
    cummulative = cum;
  }

  public String getSortColumn() {
    return sel == null ? null : ((AddSortAction) sel.get(0)).getColumn();
  }

  public String getSortColumn(int i) {
    return sel == null || sel.size() <= i ? null :
      ((AddSortAction) sel.get(i)).getColumn();
  }

  public int getSortColumnCount() {
    return sel == null ? 0 : sel.size();
  }

  /**
   * Vraca tekst sorta koji odgovara korisnickom izboru. Npr.
   * " ORDER BY cpar", ukoliko je korisnik odabrao kolonu cpar
   * za sortiranje. Taj tekst dovoljno je zalijepiti na kraj
   * query stringa u metodi okPress().<p>
   * @return ditto.
   */
  public String getOrderBy() {
    if (sel == null) return "";
    String collate = hr.restart.sisfun.frmParam.getParam("sisfun", "CollateSeq");
    collate = (collate == null ? "" : " " + collate);
    VarStr order = new VarStr(" ORDER BY ");
    for (Iterator i = sel.iterator(); i.hasNext(); ) {
      String col = ((AddSortAction) i.next()).getColumn();
      if (col.startsWith("-"))
        order.append(col.substring(1)).append(" DESC,");
      else if (col.startsWith("$"))
        order.append(col.substring(1)).append(collate).append(',');
      else order.append(col).append(',');
    }
    return order.chop().toString();
  }

  /**
   * Vraca SortDescriptor koji odgovara korisnickom izboru sortiranja.
   * Bolje je koristiti metodu getOrderBy() jer ista omogucuje sortiranje
   * po hr abecedi, za razliku od slampavog SortDescriptora.<p>
   * @return ditto.
   */

  public SortDescriptor getSelectedSort() {
    if (sel == null) return null;
    String[] cols = new String[sel.size()];
    boolean[] desc = new boolean[sel.size()];
    for (int i = 0; i < cols.length; i++) {
      cols[i] = ((AddSortAction) sel.get(i)).getColumn();
      if ((desc[i] = cols[i].startsWith("-")) || cols[i].startsWith("$"))
        cols[i] = cols[i].substring(1);
    }
    return new SortDescriptor(null, cols, desc, Locale.getDefault().toString(), Sort.CASEINSENSITIVE);
  }

  private void addSortColumn(AddSortAction src) {
    if (sel == null) sel = new ArrayList();
    if (!cummulative) {
      deselectAll();
      src.setSelected(true);
      sel.add(src);
    } else {
      if (sel.contains(src)) sel.remove(src);
      else sel.add(src);
    }
    if (sel.size() == 0) removeSort();
    else {
      String text = VarStr.join(sel, ", ").capitalize().toString();
      sortLabel.setText(text);
      sortLabel.setToolTipText("Poredak: "+text);
      remove.setSelected(false);
    }
  }

  private void removeSort() {
    deselectAll();
    sortLabel.setText("");
    sel.clear();
    sel = null;
    remove.setSelected(true);
  }

  private void deselectAll() {
    if (sel != null)
      while (sel.size() > 0)
        ((AddSortAction) sel.remove(0)).setSelected(false);
  }
}
