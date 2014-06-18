/****license*****************************************************************
**   file: MenuClassParser.java
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
/*
 * Created on Nov 12, 2004
 * 
       POSTUPAK MIGRACIJE NA MenuFactory
       
 0. Potegnuti najnoviji CVS
 1. run hr.restart.util.menus.MenuClassParser prv2public:../src
 2. run hr.restart.util.menus.MenuClassParser all
 3. u pilotu: SELECT * FROM menus where method like '%NOTFOUND%'
    unijeti nepronadjene metode
 4. Izmjeniti klase navedene u K_MENUCLASSNAMES da koriste MenuFactory
 5. ISTESTIRATI i usporediti sa starim sistemom
 6. Commitati na CVS
 7. Pomoliti se Bogu

 *
 */
package hr.restart.util.menus;

import hr.restart.util.FileHandler;
import hr.restart.util.VarStr;
import hr.restart.util.startFrame;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MenuClassParser {
  private static Logger log = Logger.getLogger(MenuClassParser.class);
  private static JMenu _parent = null;
  private static String _parentID = null;
  private static JComponent _sfbar;
  private static QueryDataSet _qmenus;
  private static HashMap _menuflds;
  private static Object _mo;
  private static Properties _menuprops;
  private static boolean _cleandb = false;
  private static boolean _makingTools;
  private static String toolsuffix = "-tools";
  private static int addtodbcalled = 0;
  /*
   * Class names and startFrame names in pairs for option all in main(String[])
   */
  private static final String[][] K_MENUCLASSNAMES = {
      //zapod
      {"hr.restart.zapod.jzpMenu","hr.restart.zapod.frmZapod"},
      //pl
      {"hr.restart.pl.frmPL","hr.restart.pl.frmPL"},
      //blagajna
      {"hr.restart.blpn.frmBLPN","hr.restart.blpn.frmBLPN"},
      //gk 
      {"hr.restart.gk.frmGK","hr.restart.gk.frmGK"},
      //mp
      {"hr.restart.mp.menuOpMp","hr.restart.mp.frmMp"},
      //ok
      {"hr.restart.ok.frmOK", "hr.restart.ok.frmOK"},
      //posl
      {"hr.restart.posl.poslMenu","hr.restart.posl.frmPosl"},
      {"hr.restart.posl.prometMenu","hr.restart.posl.frmPosl"},
      //rac
      {"hr.restart.rac.racMenu", "hr.restart.rac.frmRac"}, 
      {"hr.restart.rac.menuIzvRac", "hr.restart.rac.frmRac"},
      {"hr.restart.rac.menuOpRac", "hr.restart.rac.frmRac"},
      //sisfun
      {"hr.restart.sisfun.frmSistem", "hr.restart.sisfun.frmSistem"},
      //sk - IZBACITI POZIVE u menuitem_actionPerformed
      {"hr.restart.sk.frmSK","hr.restart.sk.frmSK"},
      //Vir 
      {"hr.restart.zapod.frmVir", "hr.restart.zapod.frmVir"},
      //zapod
      {"hr.restart.zapod.frmZapod","hr.restart.zapod.frmZapod"},
      {"hr.restart.zapod.jzpMenu","hr.restart.zapod.frmZapod"},
      //os
      {"hr.restart.os.osMain","hr.restart.os.osMain"},
      //pos
      {"hr.restart.pos.jposMenu","hr.restart.pos.posMain"},
      //crm
      {"hr.restart.pos.jcrmOpMenu","hr.restart.crm.crmMain"},
      //rn
      {"hr.restart.rn.jrnMenu","hr.restart.rn.rnMain"},
      {"hr.restart.rn.menuOpRn","hr.restart.rn.rnMain"},
      {"hr.restart.rn.menuObradeRn","hr.restart.rn.rnMain"},
      {"hr.restart.rn.menuPreglediRn","hr.restart.rn.rnMain"},
      {"hr.restart.rn.menuDocsRn","hr.restart.rn.rnMain"},
      //robno
      {"hr.restart.robno.menuOp     ","hr.restart.robno._Main"},
      {"hr.restart.robno.menuInv    ","hr.restart.robno._Main"},
      {"hr.restart.robno.menuUpit   ","hr.restart.robno._Main"},
      {"hr.restart.robno.menuSklad  ","hr.restart.robno._Main"},
      {"hr.restart.robno.menuNabava ","hr.restart.robno._Main"},
      {"hr.restart.robno.menuMalo   ","hr.restart.robno._Main"},
      {"hr.restart.robno.menuIzvMalo","hr.restart.robno._Main"},
      {"hr.restart.robno.menuVele   ","hr.restart.robno._Main"},
      {"hr.restart.robno.menuIzvVele","hr.restart.robno._Main"},
      {"hr.restart.robno.menuMeskla ","hr.restart.robno._Main"},
      {"hr.restart.robno.menuNar    ","hr.restart.robno._Main"},
      {"hr.restart.robno.menuObrade ","hr.restart.robno._Main"},
      {"hr.restart.robno.menuServis ","hr.restart.robno._Main"}
      };
  private static boolean _donothing = false;
  private static String _called_menuClassName;
  private static String _called_startFrameName;

  /**
   * 
   */
  public MenuClassParser() {
    super();
    // TODO Auto-generated constructor stub
  }
  /*
   * ***      ***  *************   ***   ***
   * ***      ***  *************   ***   ***
   * ***      ***       ***        ***   ***
   * ***      ***       ***        ***   ***
   * ***		  ***       ***        ***   ***
   *   ********         ***        ***   **********
   * 		******          ***        ***   **********
   */
  private static void menuClassToDB(String menuClassName, String startFrameName) throws Exception {
    _called_menuClassName = menuClassName;
    _called_startFrameName = startFrameName;
    Class sfc = Class.forName(startFrameName);
    startFrame sfo = (startFrame)sfc.newInstance();
    if (menuClassName.equals(startFrameName)) {
      _mo = sfo;
      log.debug("_mo = sfo :)");
    } else {
	    Class mc = Class.forName(menuClassName);
	    Constructor cmc;
	    try {
	      cmc = mc.getConstructor(new Class[] {startFrame.class});
	      _mo = cmc.newInstance(new Object[] {sfo});
	      log.debug(_mo.getClass()+"("+sfo.getClass()+") created");
	    } catch (NoSuchMethodException ex) {
	      _mo = mc.newInstance();
	    }
	    if (_mo instanceof JMenu) {
	      String _s = addMenuDataSet(menuClassName, -1, menuClassName, "M","");
	      if (_s!=null) MenuFactory.getTMPMenuProperties().setProperty(_s,((JMenu)_mo).getText());
	    }
    }
    _menuflds = new HashMap();
    Field[] flds = _mo.getClass().getDeclaredFields();
    //1. prolaz - ubaci sve 
    for (int i = 0; i < flds.length; i++) {
      log.debug("member "+i);
      if (flds[i].getType().equals(JMenu.class)) {
        log.debug(">   JMenu::"+flds[i].getName());
//        addMenuDataSet(flds[i].getName(), -1, "N/A", "M", "");
        _menuflds.put(flds[i].get(_mo),flds[i]);
      } else if (flds[i].getType().equals(JMenuItem.class)) {
        log.debug("JMenuItem::"+flds[i].getName());
//        addMenuDataSet(flds[i].getName(), -1, "N/A", "I", menuClassName+"#"+probeMethod(mo,flds[i]));
        _menuflds.put(flds[i].get(_mo),flds[i]);
      }
    }
    //drugi prolaz - nadji parente i tekstove
    _parent = null;
    _parentID = null;
    if (_mo instanceof JMenu) {
      _sfbar = (JMenu)_mo;
    } else {
      _sfbar = sfo.getJMenuBar();
    }
    //po menu baru 
    parseSwingMenu(menuClassName);
    //po tool baru
    if (!toolMenuExists(menuClassName)) {
	    _sfbar = sfo.getToolMenu();
	    _makingTools = true;
	    parseSwingMenu(menuClassName+toolsuffix);
    }
    log.debug("//storing properties ..");
    FileHandler.storeProperties(MenuFactory.MENUPROPERTIESTMP,MenuFactory.getTMPMenuProperties());
    log.info("menuClassToDB finished!");

  }
  /**
   * 
   */
  private static void clearMenuClassToDB() {
    _mo = null;
    _makingTools = false;
    _menuflds = null;
    _menuprops = null;
    _parent = null;
    _parentID = null;
    _qmenus = null;
    _sfbar = null;
  }

  private static boolean toolMenuExists(String menuClassName) {
    return MenuFactory.getQMenu(menuClassName+toolsuffix,true).getRowCount() > 0;
  }
  private static void parseSwingMenu(String menuClassName) {
    for (int m=0;m<getMenuCount(_sfbar);m++) {
      if (getMenu(m, _sfbar) == null) {
        log.debug("getMenu("+m+",_sfbar) returned null! It must be separator");
        addSeparator(menuClassName, m);
      } else {
        log.debug("inspectMenu(null,"+getMenu(m, _sfbar).getText()+")");
        inspectMenu(null,getMenu(m, _sfbar));
      }
    }
  }
  private static void addSeparator(String cmenuparent, int m) {
    addMenuDataSet("Separator-"+cmenuparent+((m+1)*10),((m+1)*10),cmenuparent,"S","");
  }
  private static int getMenuCount(JComponent sfbar) {
    if (sfbar instanceof JMenuBar) {
      return ((JMenuBar)sfbar).getMenuCount();
    } else if (sfbar instanceof JMenu) {
      return ((JMenu)sfbar).getMenuComponentCount();
    } else return 0;
  }
  private static JMenuItem getMenu(int c, JComponent sfbar ) {
    if (sfbar instanceof JMenuBar) {
      return ((JMenuBar)sfbar).getMenu(c);
    } else if (sfbar instanceof JMenu) {
      return ((JMenu)sfbar).getItem(c);
    } else return null;
  }
  /**
   * @param sfbar
   * @param menu
   */
  private static void inspectMenu(JMenuItem parent, JMenuItem child) {
    if (child instanceof JMenu) {
//      addMenu2DB(parent, child);
      JMenu cm = (JMenu)child; 
      for (int i = 0; i < cm.getMenuComponentCount(); i++) {
        if (cm!=null && cm.getItem(i) == null) {
          log.debug("*******Child "+i+" of parent "+cm.getText()+" is null! Adding separator");
          Field fld = (Field)_menuflds.get(cm);
          if (fld!=null) {
            addSeparator(fld.getName(),i);
          }
        }
        inspectMenu(cm,cm.getItem(i));
      }
    }
    addMenu2DB(parent, child);
  }
  private static void addMenu2DB(JMenuItem parent, JMenuItem child) {
    int sort = -1;
    String cmenu = null;
    String cmenuparent = null;
    String menutype = null;
    String method = null;
    String mtext = null;
    //sort
    log.debug("//sort");
    if (parent == null) {
      sort = (getItemPos(_sfbar, child)+1)*10;
    } else {
      try {
        sort = (getItemPos((JMenu)parent, child)+1)*10;
      } catch (ClassCastException e) {
        log.error("Parent "+parent.getClass()+"-"+parent.getText()+" is not JMenu");
        return;
      }
    }
    //cmenu
    log.debug("//cmenu");
    Field child_fld = null;
    try {
      child_fld = (Field)_menuflds.get(child);
    } catch (ClassCastException e) {
      log.fatal("nonField member found in _menuflds (child:"+child.getClass().getName()+" - "+child.getText()+")");
      return;
    }
    if (child_fld == null) {
      if (child == null) {
        log.warn("Field for child null NOT FOUND");
      }else {
        log.warn("Field for child "+child.getClass().getName()+" - "+child.getText()+" NOT FOUND");
      }
      return;
    } else {
      cmenu = child_fld.getName(); 
    }
    //cmenuparent
    log.debug("//cmenuparetn");
    if (parent == null) {
      cmenuparent = _mo.getClass().getName();
      if (_makingTools) {
        cmenuparent = cmenuparent+toolsuffix;
      }
    } else {
	    Field parent_fld = null;
	    try {
	      parent_fld = (Field)_menuflds.get(parent);
	    } catch (ClassCastException e) {
	      log.fatal("nonField member found in _menuflds (parent:"+parent.getClass().getName()+" - "+parent.getText()+")");
	      return;
	    }
	    if (parent_fld == null) {
	      log.warn("Field for parent "+parent.getClass().getName()+" - "+parent.getText()+" NOT FOUND");
	    } else {
	      cmenuparent = parent_fld.getName(); 
	    }
    }
    //menutype & method
    log.debug("//menutype & method");
    if (child_fld.getType().equals(JMenu.class)) {
      	menutype = "M";
      	method = "";
    } else if (child_fld.getType().equals(JMenuItem.class)) {
    	menutype = "I";
    	method = _mo.getClass().getName()+"#"+probeMethod(_mo,child_fld);      
    }
    //mtext
    log.debug("//mtext");
    mtext = child.getText();
    //write to db
    log.debug("//write to db");
    String _s = addMenuDataSet(cmenu,sort,cmenuparent,menutype,method);
    //write to properties
    log.debug("//write to properties");
    if (_s!=null) {
      MenuFactory.getTMPMenuProperties().setProperty(_s,mtext);
      log.debug("child "+child.getClass().getName()+" - "+child.getText()+" written in db and properties");
    } else log.debug("child "+child.getClass().getName()+" - "+child.getText()+" NOT WRITTEN in db and properties");
    
  }
  
  
  private static String probeMethod(Object mo, Field field) {
    String smethod=null;
    smethod = field.getName()+"_actionPerformed";
    Method[] meths = mo.getClass().getDeclaredMethods();
    for (int i = 0; i < meths.length; i++) {
      log.debug("probing method "+meths[i].getName()+" for "+smethod);
      if (smethod.equals(meths[i].getName())) {
        log.debug("*****Found! cool!");
        return smethod;
      }
    }
    log.debug(":((((((( not found");
    smethod = "!!!NOTFOUND!!!";
    return smethod;
  }
  private static String addMenuDataSet(String cmenu, int sort, String parentcmenu, String menutype, String method) {
    addtodbcalled++;
    if (_qmenus == null) {
      MenuFactory.createMenuDataSet();
      _qmenus = MenuFactory._qmenus;
      if (_cleandb) {
	      _qmenus.deleteAllRows();
	      _qmenus.saveChanges();
      }
    }
    String unqcmenu;
    String unqparentcmenu;
    unqcmenu = getPkgPrefix()+cmenu;
    unqparentcmenu = getPkgPrefix()+parentcmenu;
    //unqcmenu = getPkgPrefix()+cmenu;
//    String unqparentcmenu = getPkgPrefix()+parentcmenu;
    _qmenus.insertRow(false);
    _qmenus.setString("CMENU", unqcmenu);//+"[IGN]"+System.currentTimeMillis());
    _qmenus.setInt("CSORT", sort);
    _qmenus.setString("PARENTCMENU", unqparentcmenu);
    _qmenus.setString("MENUTYPE", menutype);
    _qmenus.setString("METHOD", method);
    _qmenus.post();
    try {
      String ret_cmenu = _qmenus.getString("CMENU")+"#"+_qmenus.getString("PARENTCMENU"); 
      _qmenus.saveChanges();
      return ret_cmenu;
    } catch (Exception e) {
      log.fatal("Error saving to db : menuClassName = "+_called_menuClassName
          	+"; startFrameName = "+_called_startFrameName+"; cmenu = "+cmenu+"; parentcmenu = "+parentcmenu);
e.printStackTrace();
      /*      log.fatal(_qmenus);
      e.printStackTrace();
      System.exit(-1);*/
			return null;
    }
  }

  /**
   * @return
   */
  private static String getPkgPrefix() {
    String lpkg = _mo.getClass().getPackage().getName();
    int iof = lpkg.lastIndexOf('.')+1;
    return lpkg.substring(iof)+"."; 
  }
  private static int getItemPos(JComponent parent,JMenuItem child) {
    int pos = -1;
    if (parent==null) return pos;
    if (child==null) return pos;
    for (int i = 0; i < getMenuCount(parent); i++) {
      try {
        if (getMenu(i,parent).equals(child)) return i;
      } catch (NullPointerException e) {
        log.warn("getMenu("+i+","+parent+") returned null");
        // e.printStackTrace();
      }
    }
    return pos;
  }
/*  private static int getItemPos(JMenuBar parent,JMenuItem child) {
    int pos = -1;
    if (parent==null) return pos;
    if (child==null) return pos;
    for (int i = 0; i < parent.getMenuCount(); i++) {
      if (parent.getMenu(i).equals(child)) return i;
    }
    return pos;
  }*/

  public static void main(String[] args) {
    try {
	    if (args.length > 0 && args[0].equals("view-mcn")) {
	      for (int i = 0; i < K_MENUCLASSNAMES.length; i++) {
	        System.out.println(K_MENUCLASSNAMES[i][0]+","+K_MENUCLASSNAMES[i][1]);
	      }
	      System.exit(0);
	    }
	    if (args.length > 0 && args[0].equals("all")) {
	      //clean db
	      MenuFactory.createMenuDataSet();
	      MenuFactory._qmenus.deleteAllRows();
	      MenuFactory._qmenus.saveChanges();
	      addtodbcalled = 0;
	      for (int i = 0; i < K_MENUCLASSNAMES.length; i++) {
	        String mcn = K_MENUCLASSNAMES[i][0].trim();
	        String sfn = K_MENUCLASSNAMES[i][1].trim();
	        menuClassToDB(mcn,sfn);
	        clearMenuClassToDB();
	      }
	      System.out.println("***** Add called "+addtodbcalled+" times, "+MenuFactory._qmenus.getRowCount()+" rows inserted ... done!");
	      System.exit(0);
	    }
	    if (args.length > 0 && (args[0].startsWith("prv2public#") || args[0].startsWith("striplf#"))) {
	      _donothing = args[0].startsWith("striplf#");
	      StringTokenizer params = new StringTokenizer(args[0],"#");
	      params.nextToken();
	      prv2public(new File(params.nextToken()));
	      System.exit(0);
	    }
	    if (args.length > 0 && (args[0].startsWith("prv2public:") || args[0].startsWith("prv2public_undo:"))) {
	      StringTokenizer params = new StringTokenizer(args[0],":");
	      boolean undo = params.nextToken().equals("prv2public_undo");  
	      String param_fn = params.nextToken();
	      String fsuffix = ".MCP_backup";
	      File srcdir = new File(param_fn);
	      if (!srcdir.isDirectory()) throw new IllegalArgumentException("Invalid source dir!!");
	      HashSet files = new HashSet();
	      for (int i = 0; i < K_MENUCLASSNAMES.length; i++) {
	        VarStr mcn = new VarStr(K_MENUCLASSNAMES[i][0].trim());
	        VarStr sfn = new VarStr(K_MENUCLASSNAMES[i][1].trim());
	        File fmcn = new File(srcdir.getAbsolutePath()+File.separator+mcn.replaceAll('.',File.separatorChar)+".java");
	        File fsfn = new File(srcdir.getAbsolutePath()+File.separator+sfn.replaceAll('.',File.separatorChar)+".java");
	        files.add(fmcn);
	        files.add(fsfn);
	      }
	      for (Iterator iter = files.iterator(); iter.hasNext();) {
	        File element = (File) iter.next();
	        if (undo) {
	          File backup = new File(element.getAbsolutePath()+fsuffix);
	          if (backup.exists()) {
	            element.delete();
	            backup.renameTo(element);
	          }
	        } else {
	          prv2public(element);
	        }
	      }
	      System.exit(0);
	    }
	    if (args.length < 2) {
	      throw new IllegalArgumentException("To few parameters");
	    }
	    if (args.length > 2) {
	      _cleandb = new Boolean(args[2]).booleanValue();
	      log.debug("cleandb = "+_cleandb);
	    }
	    
      menuClassToDB(args[0], args[1]);
      System.exit(0);
      
    } catch (Exception e) {
      System.out.println(e);
      usage();
      System.exit(-1);
    }
  }
  /**
 * @param files
 */
	private static void prv2public(File file) {
	  log.debug("processing "+file);
	  String encoding = "Cp1250";
	  String fcontent = FileHandler.readFile(file.getAbsolutePath(),encoding);
	  VarStr fchangedcontent = new VarStr();
	  for (StringTokenizer flines = new StringTokenizer(fcontent,"\n"); flines.hasMoreTokens();) {
	    String line = flines.nextToken();
	    VarStr vline = new VarStr(line);
	    String testline = new String(line).trim();
	    if (!testline.startsWith("//") && !testline.startsWith("/*") && !testline.startsWith("public") && !_donothing) { 
		    if ((testline.indexOf("new JMenu(")!=-1 || testline.indexOf("new JMenuItem(")!=-1)
		        && testline.endsWith(";")
		        ) {
		      log.debug("MENU FIELD ::> "+vline);
		      changeAccess(vline);
		    } else if (testline.indexOf("_actionPerformed(")!=-1 
		        && testline.indexOf("void")!=-1
		        ) {
		      log.debug("ACTION METHOD ::> "+vline);
		      changeAccess(vline);
		    }
	    }
	    vline = vline.append('\n');
	    fchangedcontent.append(vline);
    }
	  file.renameTo(new File(file.getAbsolutePath()+".MCP_backup"));
	  FileHandler.writeConverted(fchangedcontent.toString(),file.getAbsolutePath(),encoding);
/*	  System.out.println("****** CONVERTED FILE BEGIN *******");
	  System.out.print(fchangedcontent);
	  System.out.println("****** CONVERTED FILE END *******");*/
	}
  private static void changeAccess(VarStr vline) {
    //change to public
    vline = vline.replace("private","");
    String fc = new String(vline.toString()).trim().substring(0,1);
    int fi = vline.toString().indexOf(fc);
    vline = vline.insert(fi, "public ");
    log.debug("ALTERED LINE ***> "+vline);
  }
  private static void usage() {
    System.out.println("USAGE: MenuClassParser <menuclassname|all|view-mcn> [startframename] [boolean cleandb]");
    System.out.println(" - if 1st parameter is 'all' parser looks in K_MENUCLASSNAMES[][] and parses");
    System.out.println("   every entry pair in this string. Also cleans menus table before the operation (cleandb=true)");
    System.out.println(" - if 1st parameter is 'view-mcn' prints contents of K_MENUCLASSNAMES");
    System.out.println("example 1: MenuClassParser hr.restart.pl.frmPL hr.restart.pl.frmPL true");
    System.out.println("example 2: MenuClassParser hr.restart.robno.menuOp hr.restart.robno._Main");
    System.out.println("example 3: MenuClassParser all");
    System.out.println("example 4: MenuClassParser view-mcn");
  }
}
