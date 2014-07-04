/****license*****************************************************************
**   file: startFrame.java
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

import hr.restart.start;
import hr.restart.help.raLiteBrowser;
import hr.restart.help.raSendMessage;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraFrame;
import hr.restart.swing.raCalculator;
import hr.restart.util.mail.LogMailer;
import hr.restart.util.menus.MenuFactory;
import hr.restart.util.versions.raVersionInfo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;

/**
 * Title:        Utilitys
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      RA
 * @author AI
 * @version 1.0
 */

public class startFrame extends JraFrame implements Cloneable {
  public static boolean STEALTH_MODE = false;
  static startFrame myStFr;
//  private static startFrame SFR;
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.util.aiRes");
  raStatusBar jpMsg = new raStatusBar();
  LinkedList visibleFrames = new LinkedList();
  LinkedList loadedFrames = new LinkedList();
  hr.restart.help.raUserDialog userDialog = hr.restart.start.getUserDialog();
  private javax.swing.JMenuBar raJMenuBar;
  private javax.swing.JCheckBoxMenuItem jmiStartFrHprHelp;
  /*
  optionsDialog optd = new optionsDialog(this,"Opcije",true);
  hr.restart.sisfun.frmParam fappar = hr.restart.sisfun.frmParam.getFrmParam();
  */
  public JDesktopPane jDesktop = new JDesktopPane();
//meniji
  javax.swing.JMenuItem jmiStartFrExit;
  javax.swing.JMenu jmStartFrWindow;
  javax.swing.JMenu jmStartFrHelp;
  javax.swing.JMenu jmStartFrSys;
  javax.swing.JMenuItem jmiStartFrSysFun;
  javax.swing.JMenuItem jmiKreator = new javax.swing.JMenuItem("kreator...");
//
  public startFrame() {
    try {
      jbInit();
      myStFr = this;
      raLLFrames.getRaLLFrames().add(null,this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    this.setIconImage(raImages.getImageIcon(raImages.IMGRAICON).getImage());
    
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        //CloseAll();
        frameExit();
      }
      public void windowIconified(WindowEvent e) {
        MinimizeWins();
      }
      public void windowDeiconified(WindowEvent e) {
        RestoreWins();
      }
      public void windowActivated(WindowEvent e) {
//        winActivated();
      }
    });
    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.getContentPane().add(jpMsg, BorderLayout.SOUTH);
    this.getContentPane().add(jDesktop, BorderLayout.CENTER);
    addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        sfShown();
      }
    });
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
        new hr.restart.zapod.raKnjigChangeListener() {
          public void knjigChanged(String oldKnjig,String newKnjig) {
            startFrame.this.setStartFrameTitle();
          }
        }
    );
    //addDefToolMenu();
/*
    centerFrame(fappar,0,res.getString("jmiStartFrSysApp"));
    centerFrame(optd,0,res.getString("jmiStartFrSysUsr"));
*/
    start.invokeAppleUtilMethod("startFrameInit", new startFrame[] {this}, new Class[] {startFrame.class});
  }

/*  public void setTitle(String tit) {
    System.out.println("Title = "+tit);
    super.setTitle(tit);
    new Throwable().printStackTrace();
  }*/

  private void sfShown() {
    setStartFrameTitle();
/*    if (jmiStartFrHprHelp == null) return;
    if (userDialog == null) return;
    if (jmiStartFrHprHelp.isSelected()) userDialog.show();
*/  try {
      jmiStartFrHprHelp.setSelected(userDialog.isShowing());
    } catch (Exception ex) {
      System.out.println("nema userDialoga, valjda je inicijalizacija");
    }

  }
  public void setStartFrameTitle() {
    setActiveFrameTitle(this);
  }

  public static void setActiveFrameTitle(Frame f) {
    f.setTitle(hr.restart.zapod.dlgGetKnjig.getTitleText(f.getTitle())
    .concat(" (")
    .concat(hr.restart.sisfun.raUser.getInstance().getImeUsera())
    .concat(")")
    );
    if (f instanceof startFrame) {
      ((startFrame)f).statusMSG();    
    }
  }
  /**
   * Vraca text menija iz menus.properties. Pogodno za setiranje naslova ekrana.
   * <code><pre>
   * PRIMJER:
   *  public void jmiNeobKred_actionPerformed(ActionEvent e) {
   * 		showFrame("hr.restart.pl.frmNeobKred",getMenuText("jmiNeobKred"));
   *	}
   * </pre></code>
   * @param cmenu
   */
  public static String getMenuText(String cmenu) {
    return MenuFactory.getMenuProperties().getProperty(cmenu,cmenu);
  }
/**
 * Pokazuje glavni frame na cijelom ekranu /ShowMe(true)/
 * ili samo u vrhu ekrana /ShowMe(true)/
 * Drugi parametar je naslov na ekranu
 */
  public void ShowMe(boolean isFullScreen,String frTitle) {
//    this.setTitle(frTitle);
//    setActiveFrameTitle(this);
    if (!isShowing()) this.pack();
    Dimension screenSize = hr.restart.start.getSCREENSIZE();
    Dimension frameSize = this.getSize();
    this.setLocation(0,raToolBarRelativeY());
    if (isFullScreen) {
      frameSize.setSize(screenSize.getSize().width-raToolBarRelativeWidth(),screenSize.height-50);
    }
    else {
      int h = getJMenuBar().getSize().height+getStatusBar().getSize().height+getInsets().top;//+getInsets().bottom;
      frameSize.setSize(screenSize.width-raToolBarRelativeWidth(),h);//h = frameSize.height
    }
    try {
      this.setSize(frameSize);
      this.setState(NORMAL);
      raScreenHandler.showingMainDialog(this);
      this.setVisible(true);
      statusMSG();
//    hideToolBar();
    } catch (Exception e){e.printStackTrace();}
  }
  public void setVisible(boolean b) {
    if (STEALTH_MODE) return;
    super.setVisible(b);
  }
/*
  private void hideToolBar() {
    if (hr.restart.raToolBar.rTB!=null) {
      hr.restart.raToolBar.rTB.setVisible(false);
    }
  }
*/
/*
  private void showToolBar() {
    if (hr.restart.raToolBar.rTB!=null) {
      hr.restart.raToolBar.rTB.setVisible(true);
    }
  }
*/
  public void ShowMeP(String frTitle) {
    setTitle(frTitle);
    hr.restart.mainFrame.getMainFrame().showModule(this);
  }
  
  private int raToolBarRelativeY() {
	return 0;
    /*
    try {
      Class.forName("hr.restart.raToolBar");//moguce da paketa uopce nema
      if (hr.restart.raToolBar.rTB!=null) {
        if (hr.restart.raToolBar.TPOSITION==hr.restart.raLoad.TOP) {
          return hr.restart.raToolBar.BUTTONSIZE;
        }
      }
      return 0;
    } catch (Exception e){
      return 0;
    }
    */
  }
  private int raToolBarRelativeWidth() {
    return 0;
    /*
    try {
      Class.forName("hr.restart.raToolBar");
      if (hr.restart.raToolBar.rTB!=null) {
        if (hr.restart.raToolBar.TPOSITION==hr.restart.raLoad.RIGHT) {
          return hr.restart.raToolBar.BUTTONSIZE;
        }
      }
      return 0;
    } catch (Exception e) {
      return 0;
    }
    */
  }
/**
 * Prikazuje text zadan u String msgText u status baru
 */
  public void statusMSG(String msgText){
    jpMsg.statusMSG(msgText);
  }
/**
 * Metoda statusMSG pozvana bez parametara brise text u status baru
 */
  public void statusMSG(){
    jpMsg.statusMSG();
  }

  public raStatusBar getStatusBar() {
    return jpMsg;
  }
/**
 * Gasi aplikaciju
 */
  public void frameExit() {
    raScreenHandler.hidingMainDialog(null);//bilo this
    if (hr.restart.start.getUserDialog().isShowing()) CloseAll();
//    setVisible(false);
//    showToolBar();
    if (SFMain && hr.restart.sisfun.raUser.getInstance().getUser().equals("test")) {
      hr.restart.sisfun.raUser.getInstance().unlockUser();
      System.exit(0);
    }
  }

  public void setRaJMenuBar(javax.swing.JMenuBar newRaJMenuBar) {
    raJMenuBar = newRaJMenuBar;
    if (hr.restart.start.isMainFrame()) {
      removeDummyMenu();
      raJMenuBar.setToolTipText(getTitle());
    } else {
      makeDefMenu(raJMenuBar);
    }
    this.setJMenuBar(raJMenuBar);
  }
  private void removeDummyMenu() {
    for (int i=0;i<raJMenuBar.getMenuCount();i++) {
      JMenu currMenu = raJMenuBar.getMenu(i);
      if (currMenu.getText().equals("")) {
        raJMenuBar.remove(currMenu);
      }
    }
  }
  /**
   * Vraca inicijalni JMenuBar (bez izmjena) koji je pusten u setteru
   */
  public javax.swing.JMenuBar getRaJMenuBar() {
    return raJMenuBar;
  }
  /**
   * Radi defaultne izmjene na zadanom JMenuBaru; dodaje opcije izlaz, prozori, pomoc, system
   */
  public void makeDefMenu(javax.swing.JMenuBar jMnuB) {
    makeDefMenu(jMnuB,true);
  }
  /**
   * Radi defaultne izmjene na zadanom JMenuBaru; dodaje opcije prozori, pomoc, system,
   * dok opciju Izlaz dodaje opcionalno :) ako je showExitOption=true
   */
  public void makeDefMenu(javax.swing.JMenuBar jMnuB,boolean showExitOption) {
    if (jmiStartFrExit != null) return; //nemoj dvaputa
    jmiStartFrExit = new javax.swing.JMenuItem("Zatvori aplikaciju");
    jmiStartFrExit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!hr.restart.start.isMainFrame()) frameExit();
        else CloseAll();
      }
    });
    jmStartFrWindow = new javax.swing.JMenu(res.getString("jmStartFrWindow"));
    javax.swing.JMenuItem jmiStartFrWinMin = new javax.swing.JMenuItem(res.getString("jmiStartFrWinMin"));
    jmiStartFrWinMin.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MinimizeWins();
      }
    });
    javax.swing.JMenuItem jmiStartFrWinArng = new javax.swing.JMenuItem(res.getString("jmiStartFrWinArng"));
    jmiStartFrWinArng.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ArangeWins();
      }
    });
    javax.swing.JMenuItem jmiStartFrWinCls = new javax.swing.JMenuItem(res.getString("jmiStartFrWinCls"));
    jmiStartFrWinCls.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        CloseWins();
      }
    });
    jmStartFrHelp = new javax.swing.JMenu(res.getString("jmStartFrHelp"));
    jmiStartFrHprHelp = new javax.swing.JCheckBoxMenuItem("Pomo\u0107nik");
    jmiStartFrHprHelp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ToggleHelper();
      }
    });
    javax.swing.JMenuItem jmiStartFrHlpHelp = new javax.swing.JMenuItem(res.getString("jmiStartFrHlpHelp"));
    jmiStartFrHlpHelp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ViewHelp();
      }
    });
    javax.swing.JMenuItem jmiStartFrHlpAbout = new javax.swing.JMenuItem(res.getString("jmiStartFrHlpAbout"));
    jmiStartFrHlpAbout.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        About();
      }
    });
    JMenuItem jmiStartFrHlpMailLog = new JMenuItem("Pošalji izvješæe o grešci");
    jmiStartFrHlpMailLog.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        new LogMailer().sendMailUI(null);
      }
    });
    JMenuItem jmiQuickMsg = new JMenuItem("Pošalji poruku");
    jmiQuickMsg.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        raSendMessage.show(null);
      }
    });
    JMenuItem jmiShowMsg = new JMenuItem("Primljene poruke");
    jmiShowMsg.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        startFrame.getStartFrame().showFrame("hr.restart.help.frmMessages", "Poruke");
      }
    });
    JMenuItem jmiStartFrCalc = new JMenuItem("Kalkulator");
    jmiStartFrCalc.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        raCalculator.getInstance().show();
      }
    });
    JMenuItem jmiSearch = new JMenuItem("Pretraga");
    jmiSearch.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        new raSearchTextFiles().show();
      }
    });
//Systemski menui
    jmStartFrSys = new javax.swing.JMenu(res.getString("jmStartFrSys"));
/*
//Look & Feel
    javax.swing.JMenuItem jmiStartFrSysLnF = new javax.swing.JMenuItem(res.getString("jmiStartFrSysLnF"));
    jmiStartFrSysLnF.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        LookAndFeel();
      }
    });
//Mod rada
    javax.swing.JMenuItem jmiStartFrSysMode = new javax.swing.JMenuItem(res.getString("jmiStartFrSysMode"));
    jmiStartFrSysMode.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ModRada();
      }
    });
*/
    javax.swing.JMenuItem jmiStartFrSysUsr = new javax.swing.JMenuItem("Alati");
    jmiStartFrSysUsr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SystemPar();
      }
    });
/*
    javax.swing.JMenuItem jmiStartFrSysApp = new javax.swing.JMenuItem(res.getString("jmiStartFrSysApp"));
    jmiStartFrSysApp.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        AppPar();
      }
    });
*/
/*
    jmiStartFrSysFun = new javax.swing.JMenuItem(res.getString("jmiStartFrSysFun"));
    jmiStartFrSysFun.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SystemFun();
      }
    });
*/
    jmiKreator.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        startFrame.this.centerFrame(hr.restart.baza.kreator.getKreator(),0,"Kreator");
        startFrame.this.showFrame(hr.restart.baza.kreator.getKreator());
      }
    });

/*
    javax.swing.JMenuItem jmiStartFrGetKnj = new javax.swing.JMenuItem(res.getString("jmiStartFrGetKnj"));
    jmiStartFrGetKnj.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GetKnjig();
      }
    });
*/
/*
    javax.swing.JMenuItem jmiStartFrChUsr = new javax.swing.JMenuItem("Promjena korisnika");
    jmiStartFrChUsr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        changeUser();
      }
    });
*/
  javax.swing.JMenuItem jmiStartFrAllExit = new javax.swing.JMenuItem("Izlaz ...");
    jmiStartFrAllExit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        hr.restart.start.exit();
      }
    });

/*
    javax.swing.JMenuItem jmiStartFrPilot = new javax.swing.JMenuItem("SQL Pilot");
    jmiStartFrPilot.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showFrame("hr.restart.sisfun.raPilot","SQL Pilot");
      }
    });
*/
/*
 javax.swing.JMenuItem jmiStartFrLog = new javax.swing.JMenuItem("Log");
    jmiStartFrLog.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showLog();
      }
    });
*/
//
    if (showExitOption) {
      int lastmenu = jMnuB.getMenuCount();
      // dodavanje opcije za izlaz
      if (lastmenu==0) { //ako nema ni jednog menija
        jMnuB.add(new javax.swing.JMenu(res.getString("jmStartFr1menu"))).add(jmiStartFrExit);
      } else {
        jMnuB.getMenu(0).addSeparator();
        jMnuB.getMenu(0).add(jmiStartFrExit);
      }
    }
    // dodavanje opcije Sistem
//    jmStartFrSys.add(jmiStartFrSysLnF);
//    jmStartFrSys.add(jmiStartFrSysMode);
    /*
     * 13-10-2003 potpuno izbacen 'sistem' menu zamjenjuje ga opcija 'alati' (jmiStartFrSysUsr)
    jmStartFrSys.add(jmiStartFrSysApp);
    jmStartFrSys.addSeparator();
    jmStartFrSys.add(jmiStartFrSysUsr);
    jmStartFrSys.add(jmiStartFrPilot);
    jmStartFrSys.add(jmiStartFrLog);
    jmStartFrSys.add(jmiStartFrSysFun);
    jMnuB.add(jmStartFrSys);
     */
    // dodavanje opcije 'window'
    jmStartFrWindow.add(jmiStartFrWinMin);
    jmStartFrWindow.add(jmiStartFrWinArng);
    jmStartFrWindow.add(jmiStartFrWinCls);
    jmStartFrWindow.addSeparator();
    //Teme
    JMenu jmTeme = raSkinDialog.getThemeMenu();
    if (jmTeme != null) jmStartFrWindow.add(jmTeme);
    //Emet
    jmStartFrWindow.add(jmiStartFrSysUsr);
    jmStartFrWindow.addSeparator();
    jmStartFrWindow.add(jmiStartFrAllExit);
//    jmStartFrWindow.add(jmiStartFrGetKnj);
//    jmStartFrWindow.add(jmiStartFrChUsr);

    jMnuB.add(jmStartFrWindow);
    // dodavanje opcije help
    jmStartFrHelp.add(jmiStartFrHprHelp);
    jmStartFrHelp.add(jmiStartFrHlpHelp);
    jmStartFrHelp.add(jmiStartFrHlpMailLog);
    jmStartFrHelp.addSeparator();
    jmStartFrHelp.add(jmiQuickMsg);
    jmStartFrHelp.add(jmiShowMsg);
    jmStartFrHelp.addSeparator();
    jmStartFrHelp.add(jmiStartFrCalc);
    if (raUser.getInstance().getUser().equals("restart") ||
    		raUser.getInstance().getUser().equals("test") ||
    		raUser.getInstance().getUser().equals("root")) jmStartFrHelp.add(jmiSearch);
    jmStartFrHelp.addSeparator();
    jmStartFrHelp.add(jmiStartFrHlpAbout);
    jMnuB.add(jmStartFrHelp);
//    jMnuB.setHelpMenu(jmStartFrHelp);
  }

  /**
   * Zatvara sve otvorene prozore i glavni prozor.
   * zove close evente svih otvorenih prozora
   */
  public void CloseAll() {
    if (hr.restart.start.isMainFrame()) {
      CloseWins();
      hr.restart.mainFrame.getMainFrame().closeModule();
    } else {
      CloseWins();
      setVisible(false);
    }
  }
  /**
   * Minimizira sve otvorene prozore
   */
  public void MinimizeWins() {
    createVisibleFramesList();
    for (int i=0;i<visibleFrames.size();i++) {
      setFrameState(visibleFrames.get(i),Frame.ICONIFIED);
    }
  }
  public void RestoreWins() {
    createVisibleFramesList();
    for (int i=0;i<visibleFrames.size();i++) {
      setFrameState(visibleFrames.get(i),Frame.NORMAL);
    }
  }
  private void setFrameState(java.lang.Object frame,int state) {
    if (frame instanceof javax.swing.JFrame) {
      ((javax.swing.JFrame)frame).setState(state);
    } else if (frame instanceof hr.restart.util.raFrame) {
      ((hr.restart.util.raFrame)frame).setState(state);
    }
  }
  private int getFrameState(java.lang.Object frame,int state) {
    if (frame instanceof javax.swing.JFrame) {
      return ((javax.swing.JFrame)frame).getState();
    } else if (frame instanceof hr.restart.util.raFrame) {
      return ((hr.restart.util.raFrame)frame).getState();
    } else {
      return -1;
    }
  }
  /**
   * Sredjuje prozore tako da svi budu vidljivi
   */
  public void ArangeWins() {
    RestoreWins();
  }
  /**
   * Zatvara sve otvorene prozore osim glavnog
   */
  public void CloseWins() {
    emptyList(visibleFrames);
    LoadedWinsVisible(false);
  }
  private void emptyList(LinkedList ll) {
    for (int i=0;i<ll.size();i++) {
      ll.remove(i);
    }
  }

  /**
   * Kada se startFrame aktivira onda se minimiziraju svi ostali startFrameovi iz tehnickih
   *  a nadam se i estetskih razloga. Ako se to nekom ne svidja neka je overrida. Zato i je public.
   *  Radi samo u full modu odnosno kad je startano sa hr.restart.start.main
   */
  public void winActivated() {
/*    if (checkModalDialogActive()) {
      this.setState(Frame.ICONIFIED);
      return;
    }*/
    if (!hr.restart.start.isFullMode()) return;
    java.util.LinkedList ll = raLLFrames.getRaLLFrames().getStartFrames();
    for (int i=0;i<ll.size();i++) {
      startFrame sf = (startFrame)ll.get(i);
      if (!sf.equals(this)) sf.setState(java.awt.Frame.ICONIFIED);
    }
    this.setState(java.awt.Frame.NORMAL);
  }

  private boolean checkModalDialogActive() {
    java.util.LinkedList ll = raLLFrames.getRaLLFrames().getStartFrames();
    for (int i=0;i<ll.size();i++) {
      startFrame sf = (startFrame)ll.get(i);
      if (isModalDialogActive(sf)) {
        sf.show();
        return true;
      }
    }
    return false;
  }
  private boolean isModalDialogActive(startFrame sf) {//ne koristi se osim u checkModalDialogActive()
    java.util.LinkedList ll = raLLFrames.getRaLLFrames().getChildFrames(sf);
    for (int i=0;i<ll.size();i++) {
      Object memb = ll.get(i);
      JDialog dial=null;
      if (memb instanceof javax.swing.JDialog) {
System.out.print(memb);
System.out.println(" je instanca od JDialog");
        dial = (JDialog)memb;
      } else if (memb instanceof raFrame) {
System.out.print(memb);
System.out.println(" je instanca od raFrame");
        try {
          dial = ((raFrame)memb).getJdialog();
        } catch (Exception e) {
System.out.println("ali nije dialog");
          dial = null;
        }
      }
      if (dial != null) {
System.out.print("dialog je modal?...");
System.out.print(dial.isModal());
System.out.print("dialog je visible?...");
System.out.print(dial.isVisible());
        if (dial.isModal() && dial.isVisible()) {
          dial.show();
          return true;
        }
      }
    }//end for
System.out.println("Nije modal dialog...Vracam false");
    return false;
  }
/**
 * vraca klasu (po uzoru na datamodul)
 */
  public static startFrame getStartFrame() {
    if (myStFr == null) {
      myStFr = new startFrame();
    }
    return myStFr;
  }
  /**
   * Svim prozorima u visibleFrames kaze setVisible(visible)
   */
  public void VisibleWins(boolean visible) {
    if (!visible) createVisibleFramesList();
    for (int i=0;i<visibleFrames.size();i++) {
      setFrameVisible(visibleFrames.get(i),visible);
    }
  }
  private void createVisibleFramesList() {
      emptyList(visibleFrames);
      for (int i=loadedFrames.size()-1;i>=0;i--) {
        if (getFrameVisible(loadedFrames.get(i))) visibleFrames.add(loadedFrames.get(i));
      }
  }
  private java.util.LinkedList getLoadedFrames() {
    return raLLFrames.getRaLLFrames().getChildFrames(this);
  }
  /**
   * Svim prozorima u LoadedFrames kaze setVisible(Visible)
   */
  public void LoadedWinsVisible(boolean visible) {
    for (int i=0;i<loadedFrames.size();i++) {
      java.lang.Object frm = loadedFrames.get(i);
      setFrameVisible(frm,visible);
    }
  }
  private void setFrameVisible(java.lang.Object frame, boolean visible) {
    if (frame instanceof hr.restart.util.raFrame) {
      if (visible) ((hr.restart.util.raFrame)frame).show();
        else ((hr.restart.util.raFrame)frame).hide();
    } else {
      ((java.awt.Window)frame).setVisible(visible);
    }
  }
  private boolean getFrameVisible(java.lang.Object frame) {
    if (frame instanceof hr.restart.util.raFrame) {
      return ((hr.restart.util.raFrame)frame).isVisible();
    } else {
      return ((java.awt.Window)frame).isVisible();
    }
  }
  /**
   * Postavlja look&feel
   */
  public void LookAndFeel() {
//    showFrame(skind);
//    applyLookAndFeel();
  }
  /**
   * postavlja mod rada
   */
  public void ModRada() {
//    showFrame(moded);
  }
  private static hr.restart.help.raLiteBrowser uputebrowser;
  /**
   * Poziva help, ma kakav on bio
   */
  public void ViewHelp() {
    if (uputebrowser==null) uputebrowser = new hr.restart.help.raLiteBrowser();
    uputebrowser.pack();
    uputebrowser.show();
  }

  public void ToggleHelper() {
//    if (hr.restart.start.isMainFrame()) {
//      hr.restart.mainFrame.getMainFrame().toggleHelp();
//    } else {
      toggleHelp();
//    }
  }
  /*
  void initHP(raMatPodaci rMP) {
    if (userDialog == null) return;
    if (!userDialog.isShowing()) return;
    userDialog.getUserPanel().getHelpPanel().initHP(rMP);
  }
  void clearHP() {
    if (userDialog == null) return;
    if (!userDialog.isShowing()) return;
    userDialog.getUserPanel().getHelpPanel().forceInitHP(null);
  }
   */
  private void toggleHelp() {
    if (userDialog.isShowing()) {
      userDialog.hide();
    } else {
      userDialog.show();
    }
  }
  
  /*
  public hr.restart.help.raUserDialog getUserDialog() {
    if (userDialog == null) {
//      userDialog = new hr.restart.help.raUserDialog(this);
//      userDialog.getUserPanel().getMenuTree().addMenuBar(getJMenuBar(),this);
      userDialog = hr.restart.start.getUserDialog();
    }
    return userDialog;
  }*/
  public void setHelpOptionChecked(boolean checked) {
    if (jmiStartFrHprHelp!=null) jmiStartFrHprHelp.setSelected(checked);
  }
  public boolean isHelpOptionChecked() {
    if (jmiStartFrHprHelp!=null)
      return jmiStartFrHprHelp.isSelected();
    else return false;
  }
  /**
   * Poziva splash screen o programu
   */
  public void About() {
    String msg = raVersionInfo.getVersionInfo(); 
      //"Sustav poslovnih aplikacija \n Ver: "+hr.restart.util.versions.raVersionInfo.getCurrentVersion();
    int ret = JOptionPane.showOptionDialog(this, msg, "O programu", 
        JOptionPane.PLAIN_MESSAGE,JOptionPane.PLAIN_MESSAGE,raImages.getImageIcon(raImages.IMGRAICON),
        new String[] {"Prekid","Patchevi"},null);
    if (ret == 1) {
      try {
        File patchinf = File.createTempFile("patches","info");
        FileHandler.writeConverted(raVersionInfo.getPatchesInfo(),patchinf.getAbsolutePath(),null);
        URL url = new URL("file","localhost",patchinf.getAbsolutePath());
        raLiteBrowser brw = new raLiteBrowser(url);
        brw.pack();
        brw.setTitle("SPA - Informacije o patchevima");
        brw.show();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  /**
   * Poziva parametre upisane u fileu "robno.ini"
   */
  public void SystemPar() {
//    showFrame(optd);
//    optionsDialog opd = new optionsDialog(this, res.getString("jmiStartFrSysUsr"), false);
//    centerFrame(opd, 0, opd.getTitle());
//    opd.show();
    showFrame("hr.restart.util.optionsDialog",0,"Alati");
  }
  /**
   * poziva parametre upisane u bazu koji se odnose na rad odredjene aplikacije
   */
  public void AppPar() {
    hr.restart.sisfun.frmParam fappar =
      (hr.restart.sisfun.frmParam)showFrame("hr.restart.sisfun.frmParam",0,res.getString("jmiStartFrSysApp"),false);
    fappar.psFrmParam.showPreselect(fappar,"Aplikacija");
//    showFrame(fappar);
  }

  /**
   * Poziva sistemske alate za sada alat za kreiranje JDO za reporte, MAKNUTI U PRODUKCIJI
   */
  public void SystemFun() {
    addToolMenu();
//TEST
//raLLFrames.getRaLLFrames().printMembers();
//ET
//    hr.restart.util.reports.raJDO.main(null);
  }
  public void GetKnjig() {
    hr.restart.zapod.dlgGetKnjig.showDlgGetKnjig();
  }
  /**
   * Prikazuje zadani JFrame. Molim pozivati ekrane sa tom funkcijom
   */
  public void showFrame(javax.swing.JFrame jFr) {
//    jFr.pack();
    showWithFrame();
    jFr.show();
    jFr.setState(Frame.NORMAL);
    LoadFrame(jFr);
  }
  public void showFrame(hr.restart.util.raFrame jFr) {
//    jFr.pack();
    showWithFrame();
    jFr.show();
    jFr.setState(Frame.NORMAL);
    LoadFrame(jFr);
  }
  /**
   * Prikazuje zadani JDialog. Molim pozivati ekrane sa tom funkcijom
   */
  public void showFrame(javax.swing.JDialog jFr) {
//    jFr.pack();
    showWithFrame();
    jFr.show();
    //LoadFrame(jFr);
  }

  private void showWithFrame() {
    if (hr.restart.start.isMainFrame()) return;
    if (getJMenuBar() == null) return;
    if (SFMain) return;
    if (raLLFrames.getRaLLFrames().findMsgStartFrame() != null) return;
    if (!isShowing()) ShowMe(false,getTitle());
  }
  /**
   * Ako je klasa definirana prvim parametrom instanca od JFrame, JDialog i raFrame (raMatPodaci)
   * vraca ekran centriran, pakiran... cak ga i prikaze ako je zadnji parametar = true.
   * Ako se ne radi ni o jednoj poznatoj instanci vraca instanciranu klasu spremnu za upotrebu
   * <pre>
   * npr. deklarira se:
   *
   * public class frmZapod extends startFrame {
   *    hr.restart.zapod.frmPartneri fpar;
   * a na kliku na meni napisemo:
   *  void jzpMenuPpar_actionPerformed(ActionEvent e) {
   *    fpar = (hr.restart.zapod.frmPartneri)showFrame("hr.restart.zapod.frmPartneri",0,"Poslovni partneri",true);
   *  }
   * sad u fpar imamo instanciran i prikazan ekran s kojim mozemo raditi svasta
   * </pre>
   */
  public Object showFrame(String classname, int rightMargin, String title, boolean toshow) {
    Object member = raLoader.load(classname);
    if (member instanceof javax.swing.JFrame) {
      javax.swing.JFrame frm = (javax.swing.JFrame)member;
      if (!loadedFrames.contains(frm)) centerFrame(frm,rightMargin,title);
      if (toshow) showFrame(frm);
    } else if (member instanceof javax.swing.JDialog) {
      javax.swing.JDialog frm = (javax.swing.JDialog)member;
      if (!loadedFrames.contains(frm)) centerFrame(frm,rightMargin,title);
      if (toshow) showFrame(frm);
    } else if (member instanceof hr.restart.util.raFrame) {
      hr.restart.util.raFrame frm = (hr.restart.util.raFrame)member;
      if (!loadedFrames.contains(frm)) centerFrame(frm,rightMargin,title);
      if (toshow) showFrame(frm);
    }
    return member;
  }

  /**
   * Zove {@link #showFrame(String,int,String,boolean)} sa zadnjim parametrom true
   */
  public Object showFrame(String classname, int rightMargin, String title) {
    return showFrame(classname,rightMargin,title,true);
  }
  /**
   * Zove {@link #showFrame(String,int,String,boolean)} sa marginom 0 i zadnjim parametrom true
   */
  public Object showFrame(String classname, String title) {
    return showFrame(classname,0,title,true);
  }
  /**
   * packa i centrira zadani JFrame na ekran sa zadanom desnom marginom
   */
  public void centerFrame(javax.swing.JFrame frame, int rightMargin, String title) {
    frame.setTitle(title);
    frame.pack();
    Dimension screenSize = hr.restart.start.getSCREENSIZE();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setSize(frameSize.width+rightMargin,frameSize.height);
//->showFrame    LoadFrame(frame);
  }
  /**
   * packa i centrira zadani JDialog na ekran sa zadanom desnom marginom
   */
  public void centerFrame(javax.swing.JDialog frame, int rightMargin,String title) {
    frame.setTitle(title);
    frame.pack();
//    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension screenSize = hr.restart.start.getSCREENSIZE();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setSize(frameSize.width+rightMargin,frameSize.height);
//->showFrame    LoadFrame(frame);
  }
  /**
   * packa i centrira zadani raFrame na ekran sa zadanom desnom marginom
   */
  public void centerFrame(hr.restart.util.raFrame frame, int rightMargin,String title) {
    frame.setTitle(title);
    frame.pack();
    Dimension screenSize;
/*    if (hr.restart.start.FRAME_MODE == raFrame.INTERNALFRAME)
      screenSize = hr.restart.mainFrame.getMainFrame().mainDesk.getSize();
    else*/
//  screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    screenSize = hr.restart.start.getSCREENSIZE();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setSize(frameSize.width+rightMargin,frameSize.height);
//->showFrame    LoadFrame(frame);
  }
  /**
   * dodaje ekrane u linkedlist
   */
   public void LoadFrame(javax.swing.JFrame frameToLoad) {
/*      if (loadedFrames.contains(frameToLoad)) loadedFrames.remove(frameToLoad);
      loadedFrames.add(frameToLoad);*/
      raLLFrames.getRaLLFrames().add(frameToLoad,this);
      loadedFrames = raLLFrames.getRaLLFrames().getChildFrames(this);
   }
   public void LoadFrame(javax.swing.JDialog frameToLoad) {
/*      if (loadedFrames.contains(frameToLoad)) loadedFrames.remove(frameToLoad);
      loadedFrames.add(frameToLoad);*/
      raLLFrames.getRaLLFrames().add(frameToLoad,this);
      loadedFrames = raLLFrames.getRaLLFrames().getChildFrames(this);
   }
   public void LoadFrame(hr.restart.util.raFrame frameToLoad) {
/*      if (loadedFrames.contains(frameToLoad)) loadedFrames.remove(frameToLoad);
      loadedFrames.add(frameToLoad);*/
// new!
      raLLFrames.getRaLLFrames().add(frameToLoad,this);
      loadedFrames = raLLFrames.getRaLLFrames().getChildFrames(this);
   }
  /**
   * packa i centrira zadani JFrame na ekran sa defoultnom desnom marginom=0
   */
  public void centerFrame(javax.swing.JFrame frame) {
    centerFrame(frame,0,"");
  }

  private JMenu toolMenu;

  public void setToolMenu(JMenu tmenu) {
    toolMenu = tmenu;
    //addDefToolMenu();//zakomentirati
  }

  private void addDefToolMenu() {
    if (toolMenu == null) toolMenu = new JMenu("Sistemski alati");
    if (Util.getUtil().containsArr(toolMenu.getMenuComponents(),jmiKreator)) return;
    if (toolMenu.getMenuComponentCount() > 0) toolMenu.addSeparator();
    toolMenu.add(jmiKreator);
  }

  public JMenu getToolMenu() {
    return toolMenu;
  }

  private void addToolMenu() {
    if (toolMenu == null) return;
    if (!checkToolAccess()) return;
    jmStartFrSys.remove(jmiStartFrSysFun);
    addSystemJMenuBar();
    jmStartFrSys.add(toolMenu);
    JOptionPane.showMessageDialog(this,"Sistemski alati aktivirani");
  }
  private void addSystemJMenuBar() {
    addSystemJMenuBar(toolMenu);
  }
  void addSystemJMenuBar(JMenu _menu) {
    if (hr.restart.sisfun.frmSistem.getFrmSistem() == null) new hr.restart.sisfun.frmSistem();
    JMenuBar sisMenuBar = hr.restart.sisfun.frmSistem.getFrmSistem().getJMenuBar();
    for (int i = 0; i < sisMenuBar.getMenuCount(); i++) {
      JMenu jm2Add = sisMenuBar.getMenu(i);
      if (!containsMenu(getJMenuBar(),jm2Add)) _menu.add(cloneMenu(jm2Add));
    }
  }
  private JMenuItem cloneMenu(final JMenuItem jm) {
    if (jm instanceof JMenu) {
      JMenu newJm = new JMenu();
      newJm.setText(jm.getText());
      JMenu jmnu = (JMenu)jm;
      for (int i = 0; i < jmnu.getItemCount(); i++) {
        if (jmnu.getItem(i) !=null && jmnu.getItem(i).getText()!=null) {
          newJm.add(cloneMenu(jmnu.getItem(i)));
        }
      }
      return newJm;
    } else {
      JMenuItem newJm = new JMenuItem();
      newJm.setText(jm.getText());
      newJm.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          jm.doClick();
        }
      });
      return newJm;
    }
  }

  private boolean containsMenu(JMenuBar jmb,JMenu jm) {
    try {
      for (int i = 0; i < jmb.getMenuCount(); i++) {
        if (jmb.getMenu(i).getText().equals(jm.getText())) return true;
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }
  public boolean checkToolAccess() {
    String usro = hr.restart.sisfun.raUser.getInstance().getUser();
    if (usro.equals("root") || usro.equals("test")) return true;
    String psw = JOptionPane.showInputDialog(this,"Unesite root lozinku","Autorizacija",JOptionPane.QUESTION_MESSAGE);
    return (psw != null && psw.equals(new hr.restart.sisfun.frmPassword().getRootPassword()));
  }
  /**
   * setira look&feel
   */
  public void applyLookAndFeel() {
    SwingUtilities.updateComponentTreeUI(this);
    for (int i=0;i<loadedFrames.size();i++) {
      java.lang.Object memb = loadedFrames.get(i);
      if (loadedFrames.get(i) instanceof hr.restart.util.raFrame) {
        ((raFrame)memb).updateUI();
      } else {
        SwingUtilities.updateComponentTreeUI((Component)memb);
      }
    }
//    applyLookAndFeelMenus();
  }
  private void applyLookAndFeelMenus() {
    //PROBALI SMO LIJEPO
    for (int i=0;i<raJMenuBar.getComponentCount();i++) {
      SwingUtilities.updateComponentTreeUI((JMenu)raJMenuBar.getComponent(i)); //govno
      Component[] comps = ((JMenu)raJMenuBar.getComponent(i)).getMenuComponents();
      for (int j=0;j<comps.length;j++) {
        if (comps[j].getClass().getName().equals("javax.swing.JMenuItem"))
          ((JMenuItem)comps[j]).updateUI();
        else
         ((JComponent)comps[j]).updateUI();
      }
    }
  }
  private void makeLookNFeel(String laf) {
//    System.out.println(laf);
  }
  /**
   * <pre>
   * Postavlja lookand feel zapisan u ini fileu.
   * Zove se iz main klase prije svega.
   * Metoda je staticna i poziva se BEZ instanciranja (getanja) startFrame-a
   *
   * Primjer:
   * hr.restart.util.startFrame.raLookAndFeel();
   *
   * </pre>
   */
  public static void raLookAndFeel() {
    raSkinDialog.makeLookAndFeel();

		switchFonts(getFontDelta(), getFontFamily());
    
		UIManager.put("ComboBox.disabledForeground", UIManager.get("ComboBox.foreground"));
	
		
/*    try {
      SkinDialog.getSkinDialog().makeLookAndFeel(hr.restart.util.IntParam.VratiSadrzajTaga("lookandfeel"));
    }
    catch (Throwable tr) {
      try {
        UIManager.setLookAndFeel(hr.restart.util.IntParam.VratiSadrzajTaga("lookandfeel"));
      }
      catch (Exception ex) {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex2) {

        }
      }
    }*/
  }
  
  public static String getFontFamily() {
  	String fam = IntParam.getTag("font.family");
  	if (fam == null || fam.trim().length() == 0)
    	IntParam.setTag("font.family", fam = "Arial");
  	try {
			if (new HashSet(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().
					getAvailableFontFamilyNames())).contains(fam)) return fam;
		} catch (Exception e) {
			e.printStackTrace();
		}
  	return null;
  }
  
  public static int getFontDelta() {
  	String delta = IntParam.getTag("font.delta");
    if (delta == null || delta.trim().length() == 0)
    	IntParam.setTag("font.delta", delta = "1");
    
    return Aus.getNumber(delta);
  }
  
  public static void switchFonts(int delta, String family) {
  	if (delta < -2 || delta > 5) delta = 0;
  	if (delta == 0 && family == null) return;
  	
  	System.out.println("Changing fonts, delta: " + delta + " family: " + family);
  	UIDefaults defs = UIManager.getLookAndFeelDefaults();
  	HashSet keys = new HashSet(defs.keySet());
  	HashMap fams = new HashMap();
  	String oldf = "";
  	int maxf = 0;
  	
  	for (Iterator i = keys.iterator(); i.hasNext(); ) {
  	  Object k = i.next();
  
  		Font font = defs.getFont(k);
      if (font != null) {
      	Integer in = (Integer) fams.get(font.getFamily());
      	if (in == null) in = new Integer(1);
      	else in = new Integer(in.intValue() + 1);
      	fams.put(font.getFamily(), in);
      	if (in.intValue() > maxf) {
      		maxf = in.intValue();
      		oldf = font.getFamily();
      	}
      }
      
  	}
  	System.out.println("Old family: " + oldf);

    for(Iterator i = keys.iterator(); i.hasNext(); ) {
      Object key = i.next();
      Font font = defs.getFont(key);
      if (font != null) {
      	if (font.getFamily().equals(oldf))
      		font = new FontUIResource(family, font.getStyle(), font.getSize());
        UIManager.put(key, font.deriveFont(font.getSize2D() + delta));
      }
    }
  }
  
  public static void showLog() {
    dlgFileViewer d = new dlgFileViewer(Util.getLogFileName(),false);
    d.setStandAlone(true);
    d.setSize(Toolkit.getDefaultToolkit().getScreenSize().width,300);
    d.setLocation(0,Toolkit.getDefaultToolkit().getScreenSize().height-350);
    d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    d.reload();
    d.show();
  }
  public static void changeUser() {
    Frame parent;
    if (hr.restart.start.isMainFrame())
      parent = hr.restart.mainFrame.getMainFrame();
    else
      parent = null;

    if (!hr.restart.start.getFrmPassword().askLogin()) {
      JOptionPane.showMessageDialog(parent,"Promjena korisnika nije uspjela!");
    } else {
      if (parent == null) parent = raLLFrames.getRaLLFrames().getMsgStartFrame();
      setActiveFrameTitle(parent);
    }
  }
  public static boolean SFMain = false;
  public static void main(String[] args) {
    hr.restart.start.expirationCheck();
    hr.restart.start.runtimeArgs = args;
    hr.restart.raSplashAWT.showSplash();
    String runArg = hr.restart.start.getRunArg();
    if (!hr.restart.start.checkModule(runArg)) {
      hr.restart.start.main(args);
      return;
    }
    try {

      if (!hr.restart.start.checkArgs("direct")) {
        System.out.println("Log je redirektiran u restart.log. \n"
                           +"Odaberi Sistem/Log opciju da bi vidio log. \n"
                           +"Da bi vidio log u standard outputu proslijedi parametar 'direct' u pozivu programa \n"
                           +"   primjer: java -cp ... hr.restart.util.startFrame direct -Rrobno ..."
                           );
        hr.restart.util.Util.redirectSystemOut();
      }

      SFMain = true;
      hr.restart.start.startClient();
      hr.restart.start.parseURL();
      hr.restart.raSplashAWT.splashMSG("Priprema ekrana ...");
      startFrame.raLookAndFeel();
      if (runArg.equals("Pilot")) {
        hr.restart.raSplashAWT.splashMSG("Pokreæem pilot ...");
        startFrame.getStartFrame().showFrame("hr.restart.sisfun.raPilot","SQL Pilot");
        return;
      }
//      hr.restart.sisfun.frmPassword fpass = new hr.restart.sisfun.frmPassword();
      hr.restart.raSplashAWT.splashMSG("Priprema autorizacije ...");
      hr.restart.start.getFrmPassword();
      hr.restart.raSplashAWT.splashMSG("Autorizacija ...");
      if (!hr.restart.start.checkLogin()) System.exit(0);
      if (hr.restart.start.checkInstaled(runArg) < 1) {
        JOptionPane.showMessageDialog(null,"Aplikacija nije instalirana!","SPA",JOptionPane.ERROR_MESSAGE);
        hr.restart.sisfun.raUser.getInstance().unlockUser();
        System.exit(0);
      }
      if (!hr.restart.sisfun.raUser.getInstance().canAccessApp(runArg,"P")) {
        JOptionPane.showMessageDialog(null,"Pristup aplikaciji "+runArg+" nije Vam dozvoljen!","SPA",JOptionPane.ERROR_MESSAGE);
        hr.restart.sisfun.raUser.getInstance().unlockUser();
        System.exit(0);
      }
//      hr.restart.zapod.dlgGetKnjig.getKNJCORG();
      hr.restart.raSplashAWT.splashMSG("Priprema ekrana ...");
      hr.restart.zapod.dlgGetKnjig.changeKnjig(hr.restart.start.getFlaggedArg("knjig:"),true);
      String resModule = "APL".concat(runArg);
      String raModule = runArg;
      ResourceBundle raRes = ResourceBundle.getBundle(hr.restart.start.RESBUNDLENAME);
      Class modStart = Class.forName(raRes.getString(resModule));
      String modTitle = raRes.getString("jB"+raModule+"_text");
      startFrame modFrame = (hr.restart.util.startFrame)modStart.newInstance();

      modFrame.getJMenuBar().setToolTipText(modTitle);
      hr.restart.start.getUserDialog().getUserPanel().getMenuTree().addMenuBar(modFrame.getJMenuBar(),modFrame);

      hr.restart.raSplashAWT.hideSplash(); // za svaki slucaj
      modFrame.ShowMe(false,modTitle);
      hr.restart.raToolBar.loadForOptimize();
    }
    catch (Exception ex) {
      javax.swing.JOptionPane.showMessageDialog(null,"Program nije mogu\u0107e pokrenuti na ovaj na\u010Din!");
      ex.printStackTrace();
    }
  }
}