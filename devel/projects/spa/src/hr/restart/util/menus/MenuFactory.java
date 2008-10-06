/****license*****************************************************************
**   file: MenuFactory.java
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
 * Created on Nov 5, 2004
 */
package hr.restart.util.menus;

import hr.restart.baza.Menus;
import hr.restart.util.Aus;
import hr.restart.util.FileHandler;
import hr.restart.util.Valid;
import hr.restart.util.startFrame;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;

import com.borland.dx.dataset.ReadRow;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

/**
 * @author andrej
 * 1. Kreira i vraca menije koji su definirani u tablici menus, a nazivi u menus.properties
 * <pre>
 * table menus
 * -----------------------------
 * field	   		type  	len   opis
 * -----------------------------
 * cmenu        char    100   Unique identifier / key u properties file-u
 * sort         int           Redoslijed prikaza
 * parentcmenu  char    100   cmenu od parenta
 * menutype     char    1     I=item, M=JMenu, S=Separator
 * method       char    100   klasa i metoda u njoj koja se izvršava na actionPerformed 
 *                            npr. hr.restart.menuOp#jmSkupArt_actionPerformed
 *                            s tim da parametar moze biti actionevent ili bez parametra
 *
 *
 * </pre>
 * 2. helper utilityji
 * 
 */
public class MenuFactory {
  public static final String ZAPOD_CMENU = "zapod.hr.restart.zapod.jzpMenu";
  private static Logger log = Logger.getLogger(MenuFactory.class);
  static final String MENUPROPERTIESTMP = "menu.properties.tmp";
  private static final String MENUPROPERTIES = "menu.properties";
  private static TreeMap menus;
  static QueryDataSet _qmenus;
  private static Properties _menuprops;
  private static HashMap instancesPool = new HashMap();
  //created menu items are stored is this map for later call. 
  //see addToCreatedMenuItems(MenuValue val, JMenuItem m, Object sf)
  private static HashMap createdMenuItems = new HashMap();
  //cmenu parameter of getMenu(String cmenu, Object sf, boolean menuBar)
  private static String _called_cmenu;
  private static Properties menuProperties;
  private static File _propertiesFile;
  /**
   * 
   */
  protected MenuFactory() {
  }
  private static String createSQLFromMenuDump() {
    String sql = "";
    //koristi dumpmenu
    return sql;
  }
  /**
   * Vraca menubar sa opcijama zadanog cmenua ukljucivo menu zajednickih podataka; 
   * primjer (klasa hr.restart.pl.frmPL):
   * <pre>
   * 
   * this.setRaJMenuBar(getMenuBarWithZapod("pl.hr.restart.pl.frmPL", this));
   * 
   * </pre>
   * @param cmenu Unique identifier / key u properties file-u /menus.cmenu menija
   * @param sf parametar za klasu u menus.method
   * @return menubar sa opcijama zadanog cmenua ukljucivo menu zajednickih podataka
   */
  public static JMenuBar getMenuBarWithZapod(String cmenu, Object sf) {
    JMenuBar bar = (JMenuBar)getMenu(cmenu,sf,true);
    JMenu zapodbar = (JMenu)getMenu(ZAPOD_CMENU, sf, false);
    bar.add(zapodbar,0);
    return bar;
  }
  /**
   * Vraca menubar sa menuom zajednickih podataka pogodan za ovo:
   * <pre>
   * JMenuBar bar = getMenuBarWithZapod(this);
   * bar.add(getMenu("bla.hr.restart.bla.BlaBla",false));
   * bar.add(getMenu("bla2.hr.restart.bla2.Bla2Bla2",false));
   * ....
   * setRaJMenuBar(bar);
   * </pre>
   * @param cmenu Unique identifier / key u properties file-u /menus.cmenu menija
   * @param sf parametar za klasu u menus.method
   * @return menubar sa opcijama zadanog cmenua ukljucivo menu zajednickih podataka
   */
  public static JMenuBar getMenuBarWithZapod(Object sf) {
    JMenuBar bar = new JMenuBar();
    JMenu zapodbar = (JMenu)getMenu(ZAPOD_CMENU, sf, false);
    bar.add(zapodbar,0);
    return bar;
  }
  /**
   * Vraca novoiskreirani menu i njegove parente (ako ih ima):
   * <pre>
   * 1. konstruira klase iz menus.method sa sf parametrom, ako je != null
   * 2. trazi metodu iz menus.method prvo sa ActionEventom kao parametrom, a onda
   * bez parametara i zove je u actionPreformed
   * 3. vraca JMenu ili JMenuitem ovisno o tome ima li zadani cmenu childova
   * 4. Cita imena iz menus.properties
   * </pre> 
   * @param cmenu Unique identifier / key u properties file-u /menus.cmenu menija
   * @param sf parametar za klasu u menus.method
   * @param menubar da li da vrati JMenuBar ili JMenu  
   * @return JMenu ili JMenuitem ovisno o tome ima li zadani cmenu childova
   * 
   */
  public static JComponent getMenu(String cmenu, Object sf, boolean menuBar) {
    _called_cmenu = cmenu;
    boolean createTopLevel = !getMenuData(cmenu);
    TreeMap createdMenus = new TreeMap();
    //create JMenu containers
    for (Iterator iter = menus.keySet().iterator(); iter.hasNext();) {
      Object key = iter.next();
      MenuValue val = (MenuValue)menus.get(key);
      if (log.isDebugEnabled()) {
        log.debug("add "+val+" to createdMenus ? "+
            (val.getMenutype().equals("M") || val.getParentcmenu().equals(cmenu) ) );
      }
      //if (val.getMenutype().equals("M") || (!createTopLevel && val.getCmenu().equals(cmenu))) {
      if (val.getMenutype().equals("M") || val.getParentcmenu().equals(cmenu)) {
        createdMenus.put(getCreatedMenusKey(val),createMenuItem(val, sf));
      }
    }
    //create and add JMenuItems to JMenus in createdMenus
    for (Iterator iter = createdMenus.keySet().iterator(); iter.hasNext();) {
      String key = (String) iter.next();
      JComponent jmnucomponent = (JComponent)createdMenus.get(key);
      JMenu jmnu;
      if (log.isDebugEnabled()) {
        log.debug("iterating createdMenus:: "+key+" je na tapeti");
      }
      if (jmnucomponent instanceof JMenu) {// && !key.endsWith("//"+cmenu)) {//not top level
        jmnu = (JMenu)jmnucomponent;
	      StringTokenizer tk = new StringTokenizer(key,"//");
	      tk.nextToken();
	      String cpmenu = tk.nextToken();
	      
	      if (log.isDebugEnabled()) log.debug("ParentCmenu = "+cpmenu);
	      
	      for (Iterator miter = menus.keySet().iterator(); miter.hasNext();) {
	        String mkey = (String)miter.next();
	        if (mkey.startsWith(cpmenu+"-")) {//see addToMap(QueryDataSet)
	          MenuValue mvalue = (MenuValue)menus.get(mkey);
	          if (mvalue.getMenutype().equals("S")) {
	            if (log.isDebugEnabled()) {
	              log.debug("Adding separator "+mvalue.getCmenu()+" to "+jmnu.getText());
	            }
	            jmnu.addSeparator();
	          } else {
	            if (log.isDebugEnabled()) {
	              log.debug("Adding submenu "+mvalue.getCmenu()+" to "+jmnu.getText());
	            }
		          JMenuItem item = (JMenuItem)createdMenus.get(getCreatedMenusKey(mvalue));
		          if (item == null) {
		            item = createMenuItem(mvalue,sf);
		          }
		          jmnu.add(item);
	          }
	        }
	      }
      }//jmnucomponent instanceof JMenu
    }
    JComponent topLevel = null;
    if (!createTopLevel) {
      log.debug("!createTopLevel");
      
      String key = getCreatedMenusKey((MenuValue)getMenuValue(getQMenu(cmenu,false)));
      if (key != null) {
	      topLevel = (JComponent) createdMenus.get(key);
      } else {
        if (log.isDebugEnabled()) {
          log.debug("********** menus ***********");
	        for (Iterator iter = menus.keySet().iterator(); iter.hasNext();) {
	          Object k = iter.next();
	          log.debug(k+" = "+menus.get(k));
	        }
          /*
          log.debug("********** createdMenus ***********");
	        for (Iterator iter = createdMenus.keySet().iterator(); iter.hasNext();) {
	          Object k = iter.next();
	          log.debug(k+" = "+createdMenus.get(k));
	        }
	        */
        }
      }
      createTopLevel = topLevel == null;
    }
    if (createTopLevel) {
      if (menuBar) {
        topLevel = new JMenuBar();
      } else {
        topLevel = new JMenu();
        ((JMenu)topLevel).setText(cmenu);
      }
      for (Iterator iterator = createdMenus.keySet().iterator(); iterator.hasNext();) {
        String cmcm = (String) iterator.next();
        // is it son of a ...
        boolean isCreatedMenuFirstChild = false;
        StringTokenizer st_cmcm = new StringTokenizer(cmcm,"//");
        st_cmcm.nextToken();
        String cmcm_cmenu = st_cmcm.nextToken();
        try {
          for (Iterator iter = menus.values().iterator(); iter.hasNext();) {
            MenuValue mvp = (MenuValue)iter.next();
            if (mvp.getCmenu().equals(cmcm_cmenu)) {
              if (mvp.getParentcmenu().equals(cmenu)) {
                isCreatedMenuFirstChild = true;
                break;
              }
            }
          }
        } catch (Exception e) {
          log.fatal("isCreatedMenuFirstChild ("+cmcm_cmenu+"):"+e);
        }
        if (isCreatedMenuFirstChild) {
	        if (log.isDebugEnabled()) log.debug("adding "+cmcm);
	        try {
            topLevel.add((Component) createdMenus.get(cmcm));
          } catch (Exception e1) {
            if (log.isDebugEnabled()) log.debug("Unable to add menu from createdmenus to topLevel. Probably a separator ("+e1);
            if (topLevel instanceof JMenu) {
              ((JMenu)topLevel).addSeparator();
            } else {
              log.warn("topLevel is not JMenu - nothing added"); 
            }
          }
        }
      }
    }
    //Rezime
    log.debug("creatredMenus count :: "+createdMenus.size());
    log.debug("returned menu(bar) component count :: "+topLevel.getComponentCount());
    return topLevel;
  }
  /**
   * Sprema propertiese u file Aus.findFileAnywhere(MENUPROPERTIES)
   * Ako se pozove prije getMenuProperties bit ce Exceptiona
   * @return jel uspio il nije
   */
  public static boolean storeProperties() {
    //FileHandler.storeProperties(MenuFactory.MENUPROPERTIES,_menuprops);
    try {
      _menuprops.store(new FileOutputStream(_propertiesFile),null);
      return true;
    } catch (Exception e) {
      log.warn("storeProperties: ",e);
    }
    return false;
  }
  
  private static String getCreatedMenusKey(MenuValue val) {
    if (val == null) {
      if (log.isDebugEnabled()) {
        log.debug("getCreatedMenusKey:: MenuValue is null, returning null");
      }
      return null;
    }
    return Valid.getValid().maskZeroInteger(new Integer(val.getSort()),8)+"//"+val.getCmenu();
  }
  
  private static JMenuItem createMenuItem(MenuValue val, Object sf) {
    JMenuItem m = null;
    String text = getMenuProperties().getProperty(val.getCmenu()+"#"+val.getParentcmenu(),val.getCmenu());
    if (val.getMenutype().equals("M")) {
      m = new JMenu(text);
    } else {
	    StringTokenizer tokens = new StringTokenizer(val.getMethod(),"#");
	    String classInstanceName = null;
	    try {
	      classInstanceName = tokens.nextToken();
	    } catch (NoSuchElementException e) {
	      log.fatal("Menu "+val+" could not be created! Could not resolve class name! Invalid menus.method format");
	      return null;
	    }
	    Object oparent = getInstance(classInstanceName, sf); // class
	    if (oparent == null) {
	      log.warn("Menu "+val+" could not be created");
	      return null;
	    }
	    String action_method;
	    Method omethod;
	    try {
	      action_method = tokens.nextToken();
	    } catch (NoSuchElementException e) {
	      log.fatal("Menu "+val+" could not be created! Could not resolve method name! Invalid menus.method format");
	      return null;
	    }
	    
	    try {
	      omethod = oparent.getClass().getMethod(action_method, new Class[] {ActionEvent.class});
	    } catch (SecurityException e) {
	      log.warn("Method "+action_method+"(ActionEvent) in "+oparent.getClass()+" must be declared public!!!");
	      omethod = null;
	    } catch (NoSuchMethodException e) {
	      try {
	        omethod = oparent.getClass().getMethod(action_method, null);
	      } catch (Exception e1) {
	        log.warn("Methods "+action_method+"() and "+action_method+"(ActionEvent) in "+oparent.getClass()+" does not exist or not declared public !!!");
	        omethod = null;
	      }
	    }
	    //create menu option
	    m = new JMenuItem(text);
	    if (omethod != null) {
		    final Method fmethod = omethod;
		    final Object fparent = oparent;
		    m.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          try {
              if (fmethod.getParameterTypes().length == 0) {
                fmethod.invoke(fparent,null);
              } else {
                fmethod.invoke(fparent,new Object[] {e});
              }
            } catch (Exception e1) {
              log.fatal("Could not invoke method "+fmethod);
              e1.printStackTrace();
            }
	        }
		    });
	    }
    }
    addToCreatedMenuItems(val,m,sf);
    return m;
  }
  /**
   * @param val
   * @param m
   * @param sf
   */
  private static void addToCreatedMenuItems(MenuValue val, JMenuItem m, Object sf) {
    //1.level key = sf.getClass().getName() + "-" + called cmenu
    //2.level new HashMap with cmenu and created menu item
    HashMap level2 = null;
    Object olev2 = createdMenuItems.get(getFirstLevelKey(sf,_called_cmenu));
    if (olev2 == null || !(olev2 instanceof HashMap)) { 
      level2 = new HashMap();
      createdMenuItems.put(getFirstLevelKey(sf,_called_cmenu),level2);
    } else {
      level2 = (HashMap)olev2;
    }
    level2.put(val.getCmenu(),m);
  }
  
  /**
   * Dohvaca vec iskreiranu meni opciju (ali ne i JMenu)
   * <pre>
   * PRIMJER KODA
   * class _Main extends startFrame 
   * ...
   * JMenuBar mnubar = new JMenuBar();
   * mnubar.add(getMenu("hr.restart.robno.menuOp",this,false));
   * mnubar.add(getMenu("hr.restart.robno.menuInv",this,false));
   * ...
   *   if (...) {
   *     getCreatedMenu("hr.restart.robno.menuOp",this,"jmVlasnici").setEnabled(..);
   *   }
   * </pre>
   * @param cmenu cmenu trazenog menija
   * @param cmenuConstructor cmenu sa kojim je pozvan menu vidi <code>getMenu(String cmenu, Object sf, boolean menuBar)</code>
   * @param sfConstructor objekt (startframe) sa kojim je pozvan menu vidi <code>getMenu(String cmenu, Object sf, boolean menuBar)</code>
   * @return meni opciju za razne operacije na njoj, moze vratiti null ili baciti exception (ClassCastException), dakle to treba handlati
   */
  public static JMenuItem getCreatedMenu(String cmenu, String cmenuConstructor, Object sfConstructor) {
    return (JMenuItem) ((HashMap)createdMenuItems.get(getFirstLevelKey(sfConstructor,cmenuConstructor))).get(cmenu);
  }
  /**
   * @param sf
   * @return
   */
  private static Object getFirstLevelKey(Object sf, String callcmenu) {
    return sf.getClass().getName()+"-"+callcmenu;
  }
  private static Object getInstance(String icn, Object sf) {
    Class icc;
    Object ico = instancesPool.get(icn+"#"+sf.getClass().getName());
    if (ico != null) {
      if (log.isDebugEnabled()) {
        log.debug("Object "+icn+"("+sf.getClass().getName()+") found in pool :)");
      }
      return ico;
    }
    try {
      icc = Class.forName(icn);
    } catch (ClassNotFoundException e) {
      log.warn("Menu.class = "+icn+" NOT FOUND!! Exception: "+e);
      return null;
    }
    try {
      log.debug("searching for constructor "+icc.getName()+"(startFrame)");
      Constructor constr = icc.getConstructor(new Class[] {sf.getClass()});
      log.debug("constr = "+constr);
      ico = constr.newInstance(new Object[] {sf});
      log.debug("constr "+constr+" instantiated successfully");
    } catch (Exception e1) {
      e1.printStackTrace();
      if (log.isDebugEnabled()) {
        log.debug("Couldn't instantiate constructor "+e1);
      }
    }
    if (ico == null) {
      try {
        ico = icc.newInstance();
      } catch (Exception e2) {
        e2.printStackTrace();
        log.warn("Menu.class = "+icn+" CANNOT BE CREATED!! Exception: "+e2);
        return null;
      }
    }
    //bazenanje:
    if (ico != null) {
      instancesPool.put(icn+"#"+sf.getClass().getName(), ico);
    }
    return ico;
  }
  /**
   * 
   * @param cmenu
   * @return TopLevelMenu exists or not
   */
  private static boolean getMenuData(String cmenu) {
    boolean topLevelMenuExists = false;
    menus = new TreeMap();
    createMenuDataSet();
    QueryDataSet qmnu = getQMenu(cmenu, false);
    if (qmnu.getRowCount() != 0) {
      addToMap(qmnu);
      topLevelMenuExists = true;
    }
    findChildren(cmenu);
    return topLevelMenuExists;
  }
  
  static void createMenuDataSet() {
    //normalne okolnosti
    _qmenus = Menus.getDataModule().getQueryDataSet();
/*
    //testne okolnosti
    _qmenus = new QueryDataSet();
    Database db = new Database();
    //ddl: CREATE table menus (cmenu char(100), sort int, parentcmenu char(100), menutype char(1), method char(100))
    //ddl: CREATE UNIQUE INDEX unqmenus ON menus (cmenu, parentcmenu)
    db.setConnection(new com.borland.dx.sql.dataset.ConnectionDescriptor("jdbc:hsqldb:/home/andrej/eworkspace/devel/projects/spa/startdir/menudb","sa","",false,"org.hsqldb.jdbcDriver"
        )
        );
    _qmenus.setQuery(new QueryDescriptor(db,"SELECT * FROM menus"));
*/
    _qmenus.open();
  }
  /**
   * @param cmenu
   * @param menus
   */
  private static void findChildren(String cmenu) {
    QueryDataSet qbranch = getQMenu(cmenu, true);
    for (qbranch.first(); qbranch.inBounds(); qbranch.next()) {
      addToMap(qbranch);                                                    // || ?
      //if (!qbranch.getString("CMENU").equals(qbranch.getString("PARENTCMENU"))) {//not top menu
      if (qbranch.getInt("CSORT")!=-1) {
        findChildren(qbranch.getString("CMENU"));
      }
    }
  }
  /**
   * @param cmenu
   * @return
   */
  static QueryDataSet getQMenu(String cmenu, boolean branch) {
    if (_qmenus == null) createMenuDataSet();
    QueryDataSet _qbranch = new QueryDataSet();
    String menufield = branch?"PARENTCMENU":"CMENU";
    _qbranch.setQuery(new QueryDescriptor(_qmenus.getDatabase(),"SELECT * FROM MENUS WHERE "+menufield+" = '"+cmenu+"'"));
    log.debug(_qbranch.getQuery().getQueryString());
    _qbranch.open();
    return _qbranch;
  }
  /**
   * @param cmenu
   * @return
   */
/*  private static String getStrippedCmenu(String cmenu) {
    //String ret = new StringTokenizer(cmenu,"[IGN]").nextToken();
    String ret = cmenu;
    if (log.isDebugEnabled()) {
      log.debug(" stripping "+cmenu+" -> "+ret);
    }
    return ret;
  }*/
  /**
   * @param menus
   * @param qbranch
   */
  private static void addToMap(QueryDataSet qbranch) {
    if (log.isDebugEnabled()) log.debug("adding to menus -> "+getMenuValue(qbranch));
    menus.put(qbranch.getString("PARENTCMENU")+"-"+Valid.getValid().maskZeroInteger(new Integer(qbranch.getInt("CSORT")),8)+"-"+qbranch.getString("CMENU"), getMenuValue(qbranch));
  }
  /**
   * @param qbranch
   * @return
   */
  private static Object getMenuValue(ReadRow r) {
    MenuValue val = new MenuValue();
    val.setCmenu(r.getString("CMENU"));
    val.setSort(r.getInt("CSORT"));
    val.setMenutype(r.getString("MENUTYPE"));
    val.setMethod(r.getString("METHOD"));
    val.setParentcmenu(r.getString("PARENTCMENU"));
    return val;
  }
  static Properties getTMPMenuProperties() {
    if (_menuprops != null) return _menuprops;
    _menuprops = FileHandler.getProperties(MENUPROPERTIESTMP);
    return _menuprops;
  }
  public static Properties getMenuProperties() {
    if (_menuprops != null) return _menuprops;
    _menuprops = new Properties();
    try {
      _propertiesFile = Aus.findFileAnywhere(MENUPROPERTIES);
      _menuprops.load(new FileInputStream(_propertiesFile));      
    } catch (Exception e) {
      log.debug("getMenuProperties error: "+e);
    }
    //_menuprops = FileHandler.getProperties(MENUPROPERTIES);
    return _menuprops;
  }

  /**
   * MAIN METHOD ZA TEST
   * @param args
   */
  public static void main(String args[]) {
    try {
      startFrame sfr = new startFrame();
      //JMenuBar bar = getMenuBarWithZapod("pl.hr.restart.pl.frmPL", sfr);
      JMenuBar bar = getMenuBarWithZapod(sfr);
      /*
      JMenuBar bar = (JMenuBar)getMenu("pl.hr.restart.pl.frmPL", sfr, true);
      JMenu zapod = (JMenu)getMenu(ZAPOD_CMENU, sfr, false);
      bar.add(zapod,0);
      */
///* meni klasa      
      JMenu mnu = (JMenu)getMenu("robno.hr.restart.robno.menuOp", sfr, false);
      bar.add(mnu);
//*/
///* onako podmeni odusred pl-a
//      JMenu mnu2 = (JMenu)getMenu("jmObrasciArh", sfr, false);
//      bar.add(mnu2);
//*/
///* toolmeni od pl-a
      //JMenu mnu3 = (JMenu)getMenu("pl.hr.restart.pl.frmPL-tools", sfr, false);
      //bar.add(mnu3);
//*/
      printCreatedMenus();
//      log.debug("getCreatedMenu(jmiDNR..) = " +getCreatedMenu("jmiDNR","jmObrasciArh", sfr).getText());
      sfr.setRaJMenuBar(bar);
      sfr.pack();
      sfr.show();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  /**
   * 
   */
  private static void printCreatedMenus() {
    for (Iterator iter = createdMenuItems.keySet().iterator(); iter.hasNext();) {
      Object key = iter.next();
      log.debug("** "+key);
      //log.debug("**  "+createdMenuItems.get(key));
      if (createdMenuItems.get(key) instanceof HashMap) {
        HashMap l2 = (HashMap)createdMenuItems.get(key);
        for (Iterator iterator = l2.keySet().iterator(); iterator.hasNext();) {
          Object kl2 = iterator.next();
          log.debug("**** "+kl2);
          try {
            log.debug("****   "+((JMenuItem)l2.get(kl2)).getText());
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      } else {
        try {
          log.debug("**  JMenuItem::: "+((JMenuItem)createdMenuItems.get(key)).getText());
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
}
