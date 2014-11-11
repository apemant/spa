/****license*****************************************************************
**   file: start.java
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
package hr.restart;
import hr.restart.baza.BazaOper;
import hr.restart.baza.ConsoleCreator;
import hr.restart.baza.Verinfo;
import hr.restart.baza.dM;
import hr.restart.help.MsgDispatcher;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.IntParam;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.raCommonClass;
import hr.restart.util.raDbaseChooser;
import hr.restart.util.raDbaseCreator;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raLLFrames;
import hr.restart.util.raMiniBackup;
import hr.restart.util.raScreenHandler;
import hr.restart.util.startFrame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.borland.dx.sql.dataset.QueryDataSet;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class start {
  public static String RESBUNDLENAME="hr.restart.raRes";
  public static int FRAME_MODE=hr.restart.util.raFrame.FRAME;
  public static int MAIN_LEFT_MODE=2;
  public static boolean TOOLBAR=false;
  private static Dimension SCREENSIZE;
  private static start mystrt=null;
  private static raToolBar raTB;
  public static long STARTTIME = System.currentTimeMillis();
  mainFrame mainFr;
  static hr.restart.sisfun.frmPassword fpass;

  public start() {
    mystrt = this;
    setFRAME_MODE();
    hr.restart.util.startFrame.raLookAndFeel();
    raSplashAWT.splashMSG("Priprema autorizacije ...");
    fpass = getFrmPassword();
    raSplashAWT.splashMSG("Priprema ekrana ...");
    createMainFrame(1);
    raSplashAWT.splashMSG("Autorizacija ...");
    if (!checkLogin(true)) System.exit(0);    
    raSplashAWT.splashMSG("Priprema toolbara ...");
    raTB = raToolBar.getRaToolBar();
    raTB.showTB();
    raTB.showIfShowable();
    raSplashAWT.splashMSG("Priprema ekrana ...");
    dM.getDataModule().loadModules();
    createMainFrame(2);
    hr.restart.zapod.dlgGetKnjig.changeKnjig(getFlaggedArg("knjig:"),true);
    raSplashAWT.splashMSG("Prièekajte ...");
    MsgDispatcher.install(true);
  }

  public static hr.restart.sisfun.frmPassword getFrmPassword() {
    if (fpass == null) fpass = new hr.restart.sisfun.frmPassword();
    return fpass;
  }

  public static hr.restart.help.raUserDialog getUserDialog() {
    if (raTB == null) raTB = raToolBar.getRaToolBar();
    return raTB.getUserDialog();
  }

  public static hr.restart.help.raShortcutContainer getApplShortcuts() {
    if (raTB == null) raTB = raToolBar.getRaToolBar();
    return raTB.getAplShortcuts();
  }


  public static boolean checkLogin() {
    return checkLogin(false);
  }

  public static boolean checkLogin(boolean strtin) {
    String user = getFlaggedArg("usr:");
    String password = getFlaggedArg("pass:");
//    if (user.equals("")&&password.equals("")) {
//      return fpass.askLogin();
//    } else {
      if (!fpass.checkUserPassword(user,password)) {
        raSplashAWT.hideSplash();
        boolean granted = fpass.askLogin();
        if (strtin) {
          raToolBar rtb = raToolBar.getRaToolBar();
          if (granted && rtb.showable) rtb.showIfShowable();
          else {
            raSplashAWT.showSplash();
            SwingUtilities.invokeLater(new Runnable() {
              public void run() {
                if (raToolBar.getRaToolBar().showable)
                  raSplashAWT.hideSplash();
              }
            });
          }
        }
        return granted;
      } else return true;
//    }
  }
  public static void stripPermittedApps() {
    ArrayList items2remove = new ArrayList();
    hr.restart.help.raAbstractShortcutContainer appsh =
        (hr.restart.help.raAbstractShortcutContainer)raToolBar.getRaToolBar().getAplShortcuts();
    for (Iterator i = appsh.listelements.iterator(); i.hasNext(); ) {
//      Object item = i.next();
      hr.restart.help.raShortcutItem item = (hr.restart.help.raShortcutItem)i.next();
      if (!hr.restart.sisfun.raUser.getInstance().canAccessApp(item.getIndex(),"P")) {
        items2remove.add(item);
      }
    }
    for (Iterator i = items2remove.iterator(); i.hasNext(); ) {
      Object item = i.next();
      hr.restart.help.raShortcutItem sitem = (hr.restart.help.raShortcutItem)item;
      appsh.removeItem(sitem);
      //remove iz menuTree-a
      //appsh.getMenuTree().removeItem(hr.restart.help.raShortcutItem)item);
      raToolBar.getRaToolBar().getUserDialog().getUserPanel().getMenuTree()
          .removeItem(sitem.getIndex());
    }
  }
  public static void parseURL() {
    String rurl = getFlaggedArg("dburl:");
    if (rurl.equals("")) return;
    IntParam.SetajParametre();
    if (BazaOper.TestCon(IntParam.USER,IntParam.PASSWORD,rurl,IntParam.TIP)) {
      IntParam.UpisiUIni(rurl,"url");
    }
  }
  void createMainFrame(final int step) {
   if (FRAME_MODE==hr.restart.util.raFrame.FRAME) return;

    if (step==1) {
      mainFr = mainFrame.getMainFrame();
      hr.restart.util.startFrame.getStartFrame().makeDefMenu(mainFr.defaultMenuBar,false);
      javax.swing.SwingUtilities.updateComponentTreeUI(mainFr);
//      mainFr.menuTree.setEnabled(false);
    }
    if (step==2) {
//      mainFr.menuTree.setEnabled(true);
//      mainFr.menuTree.expandAll();
//      mainFr.showFull();
    }
  }

  public static boolean isFullMode() {
    return (mystrt!=null);
  }

  public static int getTabDetPlacement() {
    int plc = tag2placement(hr.restart.util.IntParam.getTag("tabdetpos"));
    if (plc == -1) {
      if (FRAME_MODE == hr.restart.util.raFrame.PANEL) {
        plc = JTabbedPane.BOTTOM;
      } else {
        plc = JTabbedPane.TOP;
      }
    }
    return plc;
  }
  public static boolean isRESIZABLELAYOUT() {
    String tagVal = IntParam.getTag("resizablelayout");
    if (tagVal.equals("false")) {
      return false;
    } else if (tagVal.equals("true")) {
      return true;
    } else { //dafault
      if (FRAME_MODE == raFrame.PANEL) {
        return true;
      } else {
        return false;
      }
    }
  }
  public static int getMainTabPlacement() {
    int plc = tag2placement(hr.restart.util.IntParam.getTag("maintabspos"));
    if (plc == -1) {
        plc = JTabbedPane.TOP;
    }
    return plc;
  }

  private static int tag2placement(String plc) {
    if (plc.equals("DOWN")) {
      return JTabbedPane.BOTTOM;
    } else if (plc.equals("RIGHT")) {
      return JTabbedPane.RIGHT;
    } else if (plc.equals("LEFT")) {
      return JTabbedPane.LEFT;
    } else if (plc.equals("UP")) {
      return JTabbedPane.TOP;
    } else {
      return -1;
    }
  }

  public static String[] runtimeArgs;
/**
 * Provjerava parametre passane pri pozivu aplikacije
 * <pre>
 * Moguci parametri do sada su:
 *  direct      - ne redirektira System.out u restart.log
 *  lazyload  - nakon startanja uloadava klase kojima je pristupljeno prije (zapisane u loader.properties)
 *  nooptimize  - ne uloadava framework na pocetku
 * </pre>
 */
  public static boolean checkArgs(String arg) {
    if (runtimeArgs == null) return false;
    if (runtimeArgs.length == 0) return false;
    for (int i=0;i<runtimeArgs.length;i++) {
      if (runtimeArgs[i].equals(arg)) return true;
    }
    return false;
  }
  public static String getRunArg() {
    return getFlaggedArg("R");
  }
  public static String getFlaggedArg(String flag) {
    if (runtimeArgs == null) return "";
    if (runtimeArgs.length == 0) return "";
    for (int i=0;i<runtimeArgs.length;i++) {
      if (runtimeArgs[i].startsWith("-"+flag)) {
        return runtimeArgs[i].substring(flag.length()+1);
      }
    }
    return "";
  }

  private static hr.restart.util.client.Client client;
  public static void startClient() {
    boolean connectServer = Boolean.valueOf(IntParam.getTag("connectServer")).booleanValue();
    if (!connectServer) {
      System.out.println("Konekcija na server je iskljucena u opcijama");
      return;
    }
    raSplashAWT.splashMSG("Pokretanje klijenta ...");
    String host = hr.restart.util.IntParam.getTag("host");
    int port;
    if (host.equals("")) host = getDefaultHost();
   //default -> windows/hosts || winnt/system32/drivers/etc/hosts || /etc/hosts
    try {
      port = Integer.parseInt(hr.restart.util.IntParam.getTag("port"));
    }
    catch (Exception ex) {
      port = 496; //default
    }
    try {
      client = new hr.restart.util.client.Client(host,port);
      client.startClient();
    }
    catch (Exception ex) {
      client = null;
    }
    raSplashAWT.splashMSG("Prièekajte...");
  }
  public static String getDefaultHost() {
    String dburl = IntParam.getTag("url");
    return Util.extractIPAddr(dburl);
  }
  public static void stopClient() {
    if (client == null) return;
    client.getConnection().disconnect();
    hr.restart.util.client.clientDisconnectCommObject c = new hr.restart.util.client.clientDisconnectCommObject(client.getConnection());
    client.sendCommObject(c);
  }
  public static hr.restart.util.client.Client getClient() {
    return client;
  }
  public static boolean isClientConnected() {
    if (client == null) return false;
    return client.getConnection().isConnected();

  }
  private static void setServerLog() {
    try {
      java.io.File file = new java.io.File("server.log");
      if (file.exists()) file.delete();
      file.createNewFile();
      final java.io.PrintStream servFile = new java.io.PrintStream(new java.io.FileOutputStream(file));
//      java.io.PrintStream servOut = new java.io.PrintStream(new java.io.FileOutputStream(java.io.FileDescriptor.out)) {
      java.io.PrintStream servOut = new java.io.PrintStream(new java.io.FileOutputStream(file)) {
        public void print(String s) {
          String newString = new java.sql.Timestamp(System.currentTimeMillis()).toString()+": "+s;
          super.print(newString);
//          super.print(s);
//          servFile.print(newString);
        }
      };
      System.setOut(servOut);
      System.setErr(servOut);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }
  static void startServer(String port) {
    setServerLog();
    if (port.equals("")) {
System.out.println("no port starting shell");
      hr.restart.util.server.ServerShell.main(null);
    } else {
      try {
        new hr.restart.util.server.Server(Integer.parseInt(port)).startServer();
      }
      catch (Exception ex) {
        System.out.println("could not start server at port "+port);
        ex.printStackTrace();
      }
    }
  }
  public static String[][] getResContents() {
    return (String[][])hr.restart.raRes.contents; ///!!!!!!!!!!!!
  }

  public static int checkInstaled(String raModule) {
    hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
    com.borland.dx.dataset.DataSet aplikacije = hr.restart.baza.dM.getDataModule().getAplikacija();
    String user = hr.restart.sisfun.raUser.getInstance().getUser();
//    System.out.println("user = "+user);
    aplikacije.open();
    if (!ld.raLocate(aplikacije,"APP",raModule)) {
      if (user.equals("test")||user.equals("root")) {
        return 1;
      } else {
        return -1;
      }
    }
    if (aplikacije.getString("INSTALIRANA").equals("D")) return 1;
    if (aplikacije.getString("INSTALIRANA").equals("N")) return 0;
    return -1;
  }
  
  public static String getName(String raModule) {
    hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
    com.borland.dx.dataset.DataSet aplikacije = hr.restart.baza.dM.getDataModule().getAplikacija();
    aplikacije.open();
    if (ld.raLocate(aplikacije,"APP",raModule)) {
      return aplikacije.getString("OPIS");
    }
    return null;
  }

  public static void setFRAME_MODE() {
    try {
      if (hr.restart.util.startFrame.SFMain) {
        FRAME_MODE=hr.restart.util.raFrame.FRAME;
      } else {
        FRAME_MODE = java.lang.Integer.parseInt(hr.restart.util.IntParam.VratiSadrzajTaga("framemode"));
      }
    } catch (Exception e) {
      FRAME_MODE = 0;
    }
  }

  public static boolean isMainFrame() {
    return (FRAME_MODE!=hr.restart.util.raFrame.FRAME);
  }

  public static Dimension getSCREENSIZE() {
    setFRAME_MODE();
    int userdw = 0;
    if (getUserDialog().isShowing()) {
      //userdw = getUserDialog().getWidth();
      userdw = Toolkit.getDefaultToolkit().getScreenSize().width-getUserDialog().getLocationOnScreen().x;
      if (userdw > getUserDialog().getWidth()) userdw = 0;
    }
      SCREENSIZE = new Dimension(
        Toolkit.getDefaultToolkit().getScreenSize().width-userdw,
        Toolkit.getDefaultToolkit().getScreenSize().height-50
        );
    return SCREENSIZE;
  }
  public static boolean checkModule(String runArg) {
    if (runArg.equals("Pilot")) return true;
    try {
      String resModule = "APL".concat(runArg);
      String raModule = runArg;
      java.util.ResourceBundle raRes = java.util.ResourceBundle.getBundle(RESBUNDLENAME);
      Class modStart = Class.forName(raRes.getString(resModule));
      return true;
    }
    catch (Exception ex) {
      System.out.println("invalid module call, starting main class");
      return false;
    }
  }
//exit

  public static void exit() {
    if (IntParam.getTag("silentexit").equals("true")) {
      if (start.exiting()) System.exit(0);
    } else {
      dlgExit.showExitMessage();
    }
/*
    int answ = javax.swing.JOptionPane.showOptionDialog(
        null,
        "Ovime \u0107ete zatvoriti sve poslovne aplikacije. Zatvoriti? ",
        "RestArt - POSLOVNE APLIKACIJE",
        javax.swing.JOptionPane.YES_NO_OPTION,
        javax.swing.JOptionPane.QUESTION_MESSAGE,
        null,new String[] {"Da","Ne"},"Ne");

    if (answ == 3) {
      try {
        if (exiting()) System.exit(0);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
 */
  }
  static boolean exiting() {
    boolean succ = true;
    for (Iterator i = raLLFrames.getRaLLFrames().getStartFrames().iterator(); i.hasNext(); ) {
      startFrame sfr = (startFrame)i.next();
      for (Iterator i2 = raLLFrames.getRaLLFrames().getChildFrames(sfr).iterator(); i2.hasNext(); ) {
        Object child = i2.next();
        if (child instanceof JFrame) {
          ((JFrame)child).hide();
          succ = !((JFrame)child).isShowing();
        } else if (child instanceof JDialog) {
          ((JDialog)child).hide();
          succ = !((JDialog)child).isShowing();
        } else if (child instanceof raFrame) {
          ((raFrame)child).hide();
          succ = !((raFrame)child).isShowing();
        }
      }
    }
    raScreenHandler.disableHandler();
//snimanje selektiranog taba u userdialogu
    IntParam.setTag(hr.restart.help.raUserPanel.IDXTAG,(raTB.getUserDialog().getUserPanel().getSelectedIndex()+""));
//snimanje ostalih tagova 
    try {
      IntParam.setTag(hr.restart.help.raUserPanel.MENUTAG, raTB.getUserDialog().getUserPanel().isMenuDriven()?"true":"false");
      IntParam.setTag(hr.restart.help.raUserPanel.TABSPOSTAG, raTB.getUserDialog().getUserPanel().getTabPlacement()+"");  
    } catch (Throwable t) {
      //za minibackup patch ako nema verziju sa novim raUserDialogom
      System.out.println("Error baca minibackup patch za verziju bez novog raUserDialoga");
      System.out.println("*** e = "+t);
    }
//snimanje shortcuta
    LinkedList ctree = raCommonClass.getraCommonClass().getComponentTree(raTB.getUserDialog().getUserPanel());
     for (int i=0;i<ctree.size();i++) {
      Object cmp = ctree.get(i);
      if (cmp instanceof hr.restart.help.raShortcutPanel) {
        ((hr.restart.help.raShortcutPanel)cmp).getShortcuts().saveSettings();
      }
    }
//hidanje
//    raTB.getUserDialog().hide();
    raScreenHandler.enableHandler();
    hr.restart.sisfun.raUser.getInstance().unlockUser();
    return succ;
  }
  private static boolean doExpirationCheck = false;
  public static void expirationCheck() {
    try {
      Class cexpc = Class.forName("hr.restart.raExpCheck");
      Object oexpc = cexpc.newInstance();
    } catch (Exception e) {
      
    }
  }
  public static void makeMiniBackup(final boolean force) {
    if (javax.swing.JOptionPane.showConfirmDialog(null, 
    "Izraditi sigurnosnu kopiju?", "Sigurnosna kopija", javax.swing.JOptionPane.OK_CANCEL_OPTION) != 0) {
      return;
    }
    new Thread() {
      public void run() {
        hr.restart.sisfun.raDelayWindow dlw = hr.restart.sisfun.raDelayWindow.show(null, "Kopija", "Izrada sigurnosne kopije u tijeku...", 0);
        new raMiniBackup(force);
        dlw.close();
        if (raMiniBackup.lastCopySucces) {
          JOptionPane.showMessageDialog(null, "Sigurnosna kopija napravljena ("+raMiniBackup.lastBackupFile+")!","Kopija",JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(null, "Sigurnosna kopija nije uspjela ("+raMiniBackup.lastLog+")!","Kopija",JOptionPane.ERROR_MESSAGE);
        }
      }
    }.start();
  }
  public static Object invokeAppleUtilMethod(String methodName, Object[] params, Class[] paramTypes) {
		if (!System.getProperty("os.name").startsWith("Mac")) return null;
		try {
			Class appleClass = Class.forName("hr.restart.util.AppleUtil");
			Method m = appleClass.getMethod(methodName,paramTypes);
			return m.invoke(null,params);
		} catch (Exception e) {
			//it's ok no apple, but banana is always here :)
//			e.printStackTrace();
			return null;
		}	  
  }
  private static void handleAppleStart() {
	  invokeAppleUtilMethod("bootInit", null, null);
  }
  private static void handleAlterIcons() {
    try {
      for (int i = 0; i < runtimeArgs.length; i++) {
        if (runtimeArgs[i].startsWith("-icon:")) {
          StringTokenizer st = new StringTokenizer(
              new VarStr(runtimeArgs[i]).leftChop(6).toString(),"=");
          raImages.setRelativePicResource(st.nextToken(), st.nextToken());
        }
        if (runtimeArgs[i].startsWith("-aicon:")) {
          StringTokenizer st = new StringTokenizer(
              new VarStr(runtimeArgs[i]).leftChop(6).toString(),"=");
          raImages.setPicResource(st.nextToken(), st.nextToken());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(
          "usage:\n" +
          "  -icon:desc=filename\n" +
          "  -aicon:desc=/path/to/filename/resource/in/jar\n" +
          "see hr/restart/start.java#handleAlterIcons() for more info");
    }
  }
//MAIN CLASS
  public static void main(String[] args) {
 //   System.setProperty("awt.toolkit", "hr.restart.MyXToolkit");
    expirationCheck();
//    LiveUtils.liveStart();
 // Dodano od strane Siniše na zahtjev Filipa
//    RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);
// end
    handleAppleStart();
    runtimeArgs = args;
    handleAlterIcons();
    if (checkArgs("-updatedb")) {
      new DbUpdater();
      //System.exit(0);
    }
    if (checkArgs("-adbchoose")) {
      if (!raDbaseChooser.showInstance(true)) System.exit(0);
    } else if (checkArgs("-cdbchoose")) {
      if (!raDbaseChooser.showInstance(true, true)) System.exit(0);
    } else if (checkArgs("-dbchoose")) {
      if (!raDbaseChooser.showInstance(false)) System.exit(0);
    } else if (checkArgs("-dbinit")) {
      try {
        ConsoleCreator.initDatabase();
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.exit(0);
      //if (!raDbaseChooser.initFrom()) System.exit(0);
    }
//ai: HTS init (temporary)
//     HTSZipcodes.initMe();
//
    
    String runArg = getRunArg();
    if (runArg.equals("initialize")) {
      System.out.println("flag -Rinitialize je zamijenjen sa -dbinit");
//      if (!checkArgs("direct")) hr.restart.util.Util.redirectSystemOut();
//      startFrame sfr = startFrame.getStartFrame();
//      sfr.ShowMe(true, "Inicijalizacija baze");
//      kreator.getKreator().initialize(sfr);
//      return;
    }
    if (runArg.startsWith("frame:")) {
      hr.restart.util.startFrame.raLookAndFeel();
      StringTokenizer frameClassTokens = new StringTokenizer(runArg,":");
      frameClassTokens.nextToken();
      String frameClass = frameClassTokens.nextToken();
      System.out.println("frameClass = "+frameClass);
      startFrame.getStartFrame().showFrame(frameClass,frameClass);
      return;
    }
    boolean isserver = runArg.equals("server");
    if (!isserver) raSplashAWT.showSplash();
    if (isserver) {
      startServer(getFlaggedArg("port"));
    } else if (runArg.equals("Pilot")) {
      hr.restart.util.startFrame.raLookAndFeel();
      if (!checkArgs("direct")) hr.restart.util.Util.redirectSystemOut();
      hr.restart.raSplashAWT.splashMSG("Pokreæem pilot ...");
      startFrame.getStartFrame().showFrame("hr.restart.sisfun.raPilot","SQL Pilot");
      return;
    } else {
      raSplashAWT.splashMSG("Izrada sigurnosne kopije...");
      new raMiniBackup();
      raSplashAWT.splashMSG("Provjera verzije baze...");
      dbVersionCheck();
      if (runArg.equals("") || !checkModule(runArg)) {
        raSplashAWT.splashMSG("Provjera parametara...");
        parseURL();
        if (!checkArgs("direct")) hr.restart.util.Util.redirectSystemOut();
        else frmParam.grab();
        startClient();
        new start();
      } else {
        hr.restart.util.startFrame.main(runtimeArgs);
      }
    }
  }

  private static void dbVersionCheck() {
    if (!IntParam.getTag("dbvcheck").equalsIgnoreCase("true")) return;
    boolean versioncurrent = false;
    String appver = raSplashWorker.verMF.trim();
    String dbver = "";
    try {
      QueryDataSet vi = Verinfo.getDataModule().getQueryDataSet();
      vi.open();
      vi.first();
      dbver = vi.getString("azversion").trim();
      if (appver.equals(dbver)) {
        versioncurrent = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!versioncurrent) {
      if (JOptionPane.showConfirmDialog(null,
          new raMultiLineMessage("Verzija programa (" + appver + ") ne odgovara verziji baze ("+dbver+") !! \n"
            +"Restartati program i ažurirati bazu?."), 
            "Upit", JOptionPane.OK_CANCEL_OPTION) 
                != JOptionPane.OK_OPTION) {
        return;
      }
      //ajsad
      Util.redirectSystemOut();
      int answ = JOptionPane.showConfirmDialog(null, "Napraviti sigurnosnu kopiju prije ažuriranja baze?", "Pitanje", JOptionPane.YES_NO_CANCEL_OPTION);
      if (answ == 0) {// da
        raDbaseCreator.displayDumpDialog(null);
      } else if (answ == 2) {
        return;
      }
      String startcommand = System.getProperty("user.dir")+File.separatorChar;
      if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
        startcommand += "starter.exe";
      } else {
        startcommand += "startSPA.sh";
      }
      startcommand += " -updatedb";
      final String sc = startcommand;
      Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
          try {
            Process p = Runtime.getRuntime().exec(sc,null,new File(System.getProperty("user.dir")));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
      System.exit(0);
    }
  }

 }