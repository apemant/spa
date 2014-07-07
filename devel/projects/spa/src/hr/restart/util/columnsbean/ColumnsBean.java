/****license*****************************************************************
**   file: ColumnsBean.java
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
package hr.restart.util.columnsbean;



import hr.restart.start;
import hr.restart.swing.ColumnChangeListener;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTableInterface;
import hr.restart.swing.raSelectTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.FileHandler;
import hr.restart.util.IntParam;
import hr.restart.util.VarStr;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raRowSume;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumnModel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author

 * @version 1.0

 */



public class ColumnsBean extends JPanel {

//T - Za testiranje, najbolje poslije maknuti radi performansi (search replace 'ST.' sa '// ST.')

  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

//ET

  hr.restart.util.Util Ut = hr.restart.util.Util.getUtil();

//  ComBoxCellRenderer myRenderer = new ComBoxCellRenderer(); // postoji jednostavnije rjesenje ali zbog ljepote postojanja jbuildera radim tako

  // (ab.f)  24.04.2003.

  ListCellRenderer myRenderer = new ColumnsCellRenderer();

  com.borland.dx.dataset.DataSet raDataSet;

  private String PROPERTIESFILE = "columns.properties";

  private int[] aditionalCols = new int[] {-1}; //defaultna vrijednost
//  String sacols = "";
  
  //private int[] colWidths = null;
  
  private ColumnWidths cw = null;
  
  
  private int tabheight;
  
  private Point locationOnScreen = null;

  private JraTable2 raJdbTable;

  private TableColumnModel SavedTable=null;
  
//  private PropertyChangeListener pcl;
  
  private String saveName=null;

  //private String jCBSelectedText;
  //int jcbidx = 0;

  private boolean clicked = false;
  private boolean saveSettings = true;

  private boolean usrSaveSettings = true;

  private raRowSume sumRow = null;

//  searchDialog srchDiag = new searchDialog();

  JraComboBox jComboB = new JraComboBox() {    
    public boolean selectWithKeyChar(char ch) {
      if (!jComboB.isPopupVisible()) return false;
      return super.selectWithKeyChar(ch);
    }
    public void setPopupVisible(boolean vis) {
      if (vis && raJdbTable instanceof JraTable2)
        ((JraTable2) raJdbTable).clearSpeedHistory();
      super.setPopupVisible(vis);
    }
  };

  private java.awt.event.ItemListener jComboB_ItemListener = new java.awt.event.ItemListener() {

    public void itemStateChanged(ItemEvent e) {

      jComboB_itemStateChanged(e);

    }

  };

//  JraNavField jdbNavFld = new JraNavField();

//  GridBagLayout cbLayout = new GridBagLayout();

  com.borland.jbcl.layout.XYLayout cbLayout = new com.borland.jbcl.layout.XYLayout();

//  JCheckBox jCheckBVisible = new JCheckBox()
  
//  boolean invokedToggle = false;

  public raNavAction rnvVisible = new raNavAction("Promijeni vidljivost kolone",raImages.IMGCHECK,KeyEvent.VK_K,KeyEvent.CTRL_MASK) {

    public void actionPerformed(ActionEvent e) {
      
/*      try {
        invokedToggle = true;*/
        checkedAction();        
/*      } finally {
        invokedToggle = false;
      }*/

      

    }

  };

  public raNavAction rnvTotal = new raNavAction("Prikaži kumulative",raImages.IMGSUM,KeyEvent.VK_M,KeyEvent.CTRL_MASK) {

    public void actionPerformed(ActionEvent e) {

      kumulAction();

    }

  };

  public raNavAction rnvFind = new raNavAction("Prona\u0111i",raImages.IMGFIND,KeyEvent.VK_F,KeyEvent.CTRL_MASK) {

    public void actionPerformed(ActionEvent e) {

      findAction();

    }

  };

  public raNavAction rnvRefresh = new raNavAction("Napuni opet",raImages.IMGREFRESH,KeyEvent.VK_R,KeyEvent.CTRL_MASK) {

    public void actionPerformed(ActionEvent e) {
      
      try {
        clicked = true;
        
        refreshAction();        
      } finally {
        clicked = false;
      }

    }

  };

/**

 * defaultni konstruktor za hr.restart.util.columnsbean.ColumnsBean

 */

  public ColumnsBean() {

    try {

      jbInit();

    }

    catch(Exception ex) {

      ex.printStackTrace();

    }

  }

  public void hidePopup() {
    jComboB.hidePopup();
  }
  
  public void focusCombo() {
    jComboB.requestFocus();
  }
    
  private void jbInit() throws Exception {

    this.setLayout(cbLayout);

//    jComboB.setNextFocusableComponent(jdbNavFld);

//    jComboB.setPreferredSize(new Dimension(8, 21));

    jComboB.setEditable(false);

//    jComboB.setRenderer(new ComBoxCellRenderer());

// Ovo iznad zamjenjujem sa ovim ispod tako da jbuilder moze kreirati live value iz toga

//    ComBoxCellRenderer myRenderer = new ComBoxCellRenderer(); // to staviti gore u classu

    jComboB.setRenderer(myRenderer);

//    jComboB.addItemListener(jComboB_ItemListener);

//kraj navoda



    jComboB.addAncestorListener(new javax.swing.event.AncestorListener() {

      public void ancestorAdded(AncestorEvent e) {

//        eventInit(); zove se rucno jer se rusi

      }

      public void ancestorMoved(AncestorEvent e) {}

      public void ancestorRemoved(AncestorEvent e) {

        if (!isMaximized(e.getAncestor()) && 
            (e.getAncestor() instanceof Window || 
                !isMaximized(e.getAncestorParent())))
          saveSettings();

      }

    });





//    jdbNavFld.setEditable(false);

/*

    jCheckBVisible.setToolTipText("Obriši ili napravi kolonu");

    jCheckBVisible.setHorizontalTextPosition(SwingConstants.CENTER);

    jCheckBVisible.setHorizontalAlignment(SwingConstants.CENTER);

    jCheckBVisible.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        checkedAction();

      }

    });

*/

//    this.add(jdbNavFld, new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0

//            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(1, 0, 3, 0), 125, 5));

    this.add(jComboB,new XYConstraints(0,2,150,22));/*, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0

            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 0, 3, 0), 0, 0));

*/

    this.add(rnvVisible, new XYConstraints(151,0,raNavAction.ACTSIZE,raNavAction.ACTSIZE));

    this.add(rnvTotal, new XYConstraints(151+raNavAction.ACTSIZE,0,raNavAction.ACTSIZE,raNavAction.ACTSIZE));

    this.add(rnvFind, new XYConstraints(151+raNavAction.ACTSIZE*2,0,raNavAction.ACTSIZE,raNavAction.ACTSIZE));

    this.add(rnvRefresh, new XYConstraints(151+raNavAction.ACTSIZE*3,0,raNavAction.ACTSIZE,raNavAction.ACTSIZE));

//    this.add(jCheckBVisible, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0

//            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    /*rnvVisible.addKeyStroke(this);
    rnvTotal.addKeyStroke(this);
    rnvFind.addKeyStroke(this);
    rnvRefresh.addKeyStroke(this);*/
    
    jComboB.addItemListener(jComboB_ItemListener);
    
    /*JraKeyListener myk = new hr.restart.swing.JraKeyListener(new int[] {
        KeyEvent.VK_ESCAPE, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_PAGE_UP,
        KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_HOME, KeyEvent.VK_END, KeyEvent.VK_F10});*/

    /*JraKeyListener myk = new hr.restart.swing.JraKeyListener(new int[] {}, true) {
      public void keyPressed(KeyEvent e) {
        if (jComboB.isPopupVisible()) return;
        super.keyPressed(e);
      }
    };
    jComboB.addKeyListener(myk);*/
    /* Workaround za opako gadan feature gdje neki look&feel-ovi instaliraju svoje
     * komponente za non-editable comboboxove, koje mu ukradu fokus, bar na java 1.3.1.
     * Zato se ovdje dodaju keylisteneri i na tu potencijalnu komponentu-child od
     * combobox-a. (ab.f)
     */
    /*Component c = jComboB.getComponent(0);
    if (c != null) c.addKeyListener(myk);*/

  } //End of jbInit
  
/**

 * Pozvati nakon dodavanja u maticni container <- interno

 */
  
  HashMap intern = new HashMap();
  
  /*public void registerComboKeyListener(final KeyListener kl) {
    KeyListener wrap = new KeyListener() {
      public void keyTyped(KeyEvent e) {
        if (jComboB.isPopupVisible() || e.isConsumed() || e.getKeyCode() == e.VK_SPACE) return;
        kl.keyTyped(e);
      }
      public void keyPressed(KeyEvent e) {
        if (jComboB.isPopupVisible() || e.isConsumed() || e.getKeyCode() == e.VK_SPACE) return;
        kl.keyPressed(e);
      }
      public void keyReleased(KeyEvent e) {
        if (jComboB.isPopupVisible() || e.isConsumed() || e.getKeyCode() == e.VK_SPACE) return;
        kl.keyReleased(e);
      }
    };
    intern.put(kl, wrap);
    jComboB.addKeyListener(wrap);
    Component c = jComboB.getComponent(0);
    if (c != null) c.addKeyListener(wrap);
  }
  
  public void unregisterComboKeyListener(KeyListener kl) {
    KeyListener wrap = (KeyListener) intern.get(kl);
    if (wrap != null) jComboB.removeKeyListener(wrap);
    Component c = jComboB.getComponent(0);
    if (c != null && wrap != null) c.removeKeyListener(wrap);
  }*/

  public void registerColumnsBeanKeys(Component comp) {

    rnvVisible.registerNavKey(comp);

    rnvTotal.registerNavKey(comp);

    rnvFind.registerNavKey(comp);

    rnvRefresh.registerNavKey(comp);

  }

  public void unregisterColumnsBeanKeys(Component comp) {

    rnvVisible.unregisterNavKey(comp);

    rnvTotal.unregisterNavKey(comp);

    rnvFind.unregisterNavKey(comp);

    rnvRefresh.unregisterNavKey(comp);

  }

  public JraTableInterface getRaJdbTable() {

    return raJdbTable;

  }

  public raNavAction[] getNavActions() {

    return new raNavAction[] {rnvVisible,rnvTotal,rnvFind,rnvRefresh};

  }


  boolean isMaximized(Container c) {
    Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
    return (c != null && c instanceof Window && 
        c.getWidth() >= scr.width * 0.95 && c.getHeight() >= scr.height * 0.90);
  }

  public raRowSume getSumRow() {

    return sumRow;

  }



  public void setSumRow(raRowSume sRow) {

    sumRow = sRow;

  }



/**

 * Da li je initialize vec pozvan odnosno da li ce eventInit() pozvati initialize

 */

  public boolean isInitialized() {

    if (SavedTable==null) return false;

    if (SavedTable.getColumnCount()<=1) return false;

    return true;

    //return (SavedTable==null)||(SavedTable.getColumnModel().getColumnCount()<=1);

  }

/**

 * Zove initialize ako nije jos inicijalizirano

 */

  public void eventInit() {

    if (!isInitialized()) {

      initialize(); // izvrsava se ovdje jer se mora pozvati nakon sto je jtable kreiran, a ne u setteru

    }

  }
  
  public void clearDataSet() {
    raDataSet = null;
  }

  public void setRaJdbTable(JraTable2 newRaJdbTable) {

    if (raJdbTable != null) {

      raJdbTable.removeColumnChangeListener();
/*      //makni listener od COLNAMEPROPERTY

      JraTable2 jt2 = (JraTable2)raJdbTable;
      
      if (pcl != null) jt2.removePropertyChangeListener(jt2.COLNAMEPROPERTY, pcl);
      
      System.out.println("listeners "+jt2.getListeners(java.beans.PropertyChangeListener.class).length);

      java.beans.PropertyChangeListener[] propchListeners;

// since 1.4:

//      propchListeners = jt2.getPropertyChangeListeners(jt2.COLNAMEPROPERTY)

// since 1.3:

      propchListeners = (java.beans.PropertyChangeListener[])jt2.getListeners(java.beans.PropertyChangeListener.class);

      for (int i=0; i<propchListeners.length; i++) {

        jt2.removePropertyChangeListener(jt2.COLNAMEPROPERTY,propchListeners[i]);

      }*/

    }

    raJdbTable = newRaJdbTable;

    if (raJdbTable != null) {

       raJdbTable.addColumnChangeListener(new ColumnChangeListener() {
          public void columnChanged(Object source, String oldColumn, String newColumn) {
            if (source == raJdbTable)
              jComboB.setSelectedItem(newColumn);
          }
        });

/*        jt2.addPropertyChangeListener(jt2.COLNAMEPROPERTY,

          pcl = new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {

              if (!evt.getNewValue().equals(evt.getOldValue())) {

                jComboB.setSelectedItem(evt.getNewValue());

              }

            }

          });*/

      raDataSet = raJdbTable.getDataSet();

    } else {

      raDataSet = null;

    }
    
    

//    jdbNavFld.setDataSet(raDataSet);

  }
  
  public void setAditionalCols(int[] newAditionalCols) {

    if (newAditionalCols!=null) {

      aditionalCols = newAditionalCols;

    } else {

      aditionalCols = new int[] {-1};

    }
    //colWidths = null;
    cw = null;
  }

  public void setSaveSettings(boolean p1) {

    usrSaveSettings = p1;

  }

  public boolean getSaveSettings() {

    return usrSaveSettings;

  }



/**

 * Jedinstveno definira instancu columnsBeana - u raMatPodaci automatski stavlja naslov ekrana-panela,

 * a u pregledima "Pregled"??. Za snimanje se konkatinira sa imenom tabele iz baze

 */

  public void setSaveName(String newSaveName) {

    saveName = newSaveName;

  }

  public String getSaveName() {

    return saveName;

  }

  private String getSaveTag() {

//ST.prn("saveName is: "+"ColB-".concat(saveName.concat("-=-").concat(raDataSet.getTableName())));

    return "ColB-" + saveName + "-=-" + getTableName();
  }

  private String getTableName() {
    try {
      if (raJdbTable.getDataSet().getTableName() != null)
        return raJdbTable.getDataSet().getTableName();
      String q = ((QueryDataSet) raJdbTable.getDataSet()).getQuery().getQueryString();
      int from = q.toLowerCase().indexOf(" from ");
      int where = q.toLowerCase().indexOf(" where ", from);
      VarStr tbs = new VarStr(q.substring(from + 6, where));
      return tbs.removeWhitespace().toString().toUpperCase();
    } catch (Exception e) {
      // fallthrough
    }
    return "null";
  }

  public boolean chkSaveSettings() {

    try {

      if (saveName == null || raJdbTable == null || raJdbTable.getDataSet() == null) {

        return false;

      }

      return true;

    } catch (Exception e) {

      return false;

    }

  }



  public void saveSettings() {
    if (isMaximized(getTopLevelAncestor())) return;
    System.err.println("save settings?");

    if (!usrSaveSettings) return;

    saveSettings = chkSaveSettings();

    if (!saveSettings) return;
    
    System.err.println("yes: " + getSaveTag());

    Properties colpr = FileHandler.getProperties(PROPERTIESFILE);

    String s = makeSaveValue();
    colpr.setProperty(getSaveTag(),s);
    System.err.println(s);

    FileHandler.storeProperties(PROPERTIESFILE,colpr);

  }
  
  boolean updating = false;
  public void updateColumnWidths() {
  	if (cw == null || raJdbTable == null || updating || !isInitialized()) return;
  	updating = true;
  	
  	try {
    	for (int i = 0; i < raJdbTable.getColumnCount(); i++) {
        int width = cw.get(raJdbTable.getRealColumnName(i));
        
        if (width > 0) {
        	raJdbTable.getColumnModel().getColumn(i).setWidth(width);
        	raJdbTable.getColumnModel().getColumn(i).setPreferredWidth(width);
        }
      }
    	raJdbTable.setPreferredScrollableViewportSize(getPreferredTableSize());
  	} finally {
  	  updating = false;
  	}
  }
  
  public Point getPreferredLocationOnScreen() {
    return locationOnScreen;
  }
  
  public Dimension getPreferredTableSize() {
/*    if (colWidths == null) 
      return new Dimension(400, 400);*/
  	if (cw == null || raJdbTable == null)
  		return new Dimension(400, 300);
    
    int width = 0;
    for (int i = 0; i < raJdbTable.getColumnCount(); i++)
    	width += cw.get(raJdbTable.getRealColumnName(i));

    Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
    return new Dimension(
        Math.min(scr.width, Math.max(400, width)),
        Math.min(scr.height, Math.max(200, tabheight)));
  }

  private void loadSettings() {
    if (!usrSaveSettings) return;
    saveSettings = chkSaveSettings();
    if (!saveSettings) return;
//    String sett = IntParam.VratiSadrzajTaga(getSaveTag());

    System.out.println("tag "+getSaveTag());
    String sett = FileHandler.getProperties(PROPERTIESFILE).getProperty(getSaveTag());

    System.out.println("sett "+sett);
    if (sett == null) return;

    if (sett.equals("")) return;
    String[] cols = new VarStr(sett).split(',');
    int[] savedcols = new int[cols.length - 1];
    ColumnWidths tcw = new ColumnWidths();
    boolean wid = false;
    int pc = 0;
    for (int i = 0; i < cols.length - 1; i++)
      if ((pc = cols[i].indexOf(':')) > 0) {
        wid = true;
        savedcols[i] = getColNum(cols[i].substring(0, pc));
        if (savedcols[i] >= 0) 
        	tcw.put(raJdbTable.getModelColumnName(savedcols[i]),
        			Aus.getNumber(cols[i].substring(pc + 1)));
      } else savedcols[i] = getColNum(cols[i].substring(0, pc));

    String last = cols[cols.length - 1];
    if (last.startsWith("*"))
      tabheight = Aus.getAnyNumber(last.substring(1));
    System.out.println("table height "+tabheight);
    if (last.indexOf('@') > 0 && last.indexOf('|') > 0)
      locationOnScreen = new Point(
          Aus.getAnyNumber(last.substring(last.indexOf('@') + 1)),
          Aus.getAnyNumber(last.substring(last.indexOf('|') + 1)));
    else locationOnScreen = null;
    setAditionalCols(savedcols);
    cw = wid ? tcw : null;
  }
  
  private int getColNum(String s) {
    return raJdbTable.getModelColumnIndex(s) < 0 ? Aus.getNumber(s) : raJdbTable.getModelColumnIndex(s);
  	//return Aus.isNumber(s) ? Aus.getNumber(s) : raJdbTable.getModelColumnIndex(s);
  }

/*  private int[] makeSavedVisCols(String par) {

    String str = new String(par);

    int numComas = getNumChars(str,',');

    int[] retValue = new int[numComas];

    for (int i=0;i<retValue.length;i++) {

      try {

        int pos = str.indexOf(",");

        retValue[i] = Integer.parseInt(str.substring(0,pos));

        str = str.substring(pos+1);

      } catch (Exception e) {

        System.out.println("makeSavedVisCols member "+Integer.toString(i)+" :"+e.toString());

      }

    }

    return retValue;

  }

  private int getNumChars(String s1,char c1) {

    char[] ca1 = s1.toCharArray();

    int retVal=0;

    for (int i=0;i<ca1.length;i++) {

      if (ca1[i]==c1) retVal=retVal+1;

    }

    return retVal;

  } */

  private String makeSaveValue() {
    VarStr ret = new VarStr();
    int total = 0;
    for (int i = 0; i < raJdbTable.getColumnCount(); i++) {
    	String cn = raJdbTable.getRealColumnName(i);
    	int wid = raJdbTable.getColumnModel().getColumn(i).getWidth();
    	if (cw != null) cw.put(cn, wid);
    	total = total + wid;
    	ret.append(cn).append(':').append(wid).append(',');
    }
    if (cw == null) System.out.println("Cw je null? Zašto");
    
    JTable tab = (JTable) raJdbTable;
    
    if (tab.getParent() instanceof JViewport) {
      tabheight = ((JViewport) tab.getParent()).getHeight();
      tab.setPreferredScrollableViewportSize(new Dimension(total, tabheight));
    } else if (tab.getParent().getParent() instanceof JViewport) {
      tabheight = ((JViewport) tab.getParent().getParent()).getHeight();
      tab.setPreferredScrollableViewportSize(new Dimension(total, tabheight));
    } else tabheight = tab.getPreferredScrollableViewportSize().height;
    if (tabheight > start.getSCREENSIZE().height-50)
      tabheight = start.getSCREENSIZE().height-50;
    ret.append('*').append(tabheight);
    Container w = tab.getTopLevelAncestor();
    if (w instanceof Window) {
      String off = IntParam.getTag("window.offset");
      if (off.length() == 0)
        IntParam.setTag("window.offset", off = "0");
      locationOnScreen = w.getLocation();
      locationOnScreen.y -= Aus.getNumber(off);
      if (locationOnScreen.y < 0)
        locationOnScreen.y = 0;
      ret.append('@').append(w.getX()).append('|').append(locationOnScreen.y);
    }
    return ret.toString();
  }

/*  private int[] getCurrentVisCols() {

    int[] retVal = new int[raJdbTable.getColumnModel().getColumnCount()];

    for (int i=0;i<retVal.length;i++) {

      retVal[i] = findColIndex(raJdbTable.getColumnModel().getColumn(i).getHeaderValue().toString(),SavedTable);

    }

    return retVal;

  }*/

  public void checkFilter() {
    rnvFind.setLockedLoweredBorder(
        raJdbTable != null && raJdbTable.getDataSet() != null &&
        raJdbTable.getDataSet().getRowFilterListener() != null);
  }

  public void initialize() {
    
    loadSettings();
    if (raJdbTable==null) return;

    raDataSet = raJdbTable.getDataSet();
    System.out.println("colbean.initialize:raDataSet = "+raDataSet);
    setVisible(raDataSet != null);
    if (raDataSet == null) {
      SavedTable = null;
      return;
    }
    checkFilter();
    ((JraTable2) raJdbTable).createDefaultColumnsFromModel();

    //Punjenje comboboxa checkboxima (vidi klasu ComBoxCellRenderer)

    jComboB.removeAllItems();

    for (int i=0;i<raJdbTable.getColumnModel().getColumnCount();i++) {

//      jComboB.addItem(new JLabel(raJdbTable.getColumnModel().getColumn(i).getHeaderValue().toString()));

      // (ab.f) 24.04.2003 nema potrebe za ubacivanjem labela.. dovoljno je ubaciti nazive kolona

      jComboB.addItem(raJdbTable.getColumnModel().getColumn(i).getHeaderValue().toString());

    }
    
    if (cw != null && aditionalCols != null && aditionalCols[0] >= 0) {
      int total = 0;
      for (int i = 0; i < aditionalCols.length; i++) {
        int idx = aditionalCols[i];
        if (idx >= 0 && idx < raJdbTable.getColumnCount()) {
          int wid = cw.get(raJdbTable.getModelColumnName(idx));
          total = total + wid;
          /*if (idx >= raJdbTable.getColumnModel().getColumnCount()) {
            System.out.println("Kolona "+idx+" ne postoji u tablici");
            continue;
          }*/
          raJdbTable.getColumnModel().getColumn(idx).setPreferredWidth(wid);
          raJdbTable.getColumnModel().getColumn(idx).setWidth(wid);
        }
      }
      System.out.println("set widths "+cw);
      System.out.println("height "+tabheight);
      raJdbTable.setPreferredScrollableViewportSize(new Dimension(total, tabheight));
      if (locationOnScreen != null) {
        Container w = getTopLevelAncestor();
        if (w instanceof Window && !w.isShowing())
          w.setLocation(locationOnScreen.x, locationOnScreen.y);
      }
    }

    //Snimam orginalnu tabelu da bih mogao poslije dodavati removane kolone iz nje

//    SavedTable = new JraTable(raJdbTable.getModel());

    SavedTable = new DefaultTableColumnModel();
//    SavedTable = raJdbTable.clone(raJdbTable.getModel());
    for (int i = 0; i < raJdbTable.getColumnCount(); i++) {
      SavedTable.addColumn(raJdbTable.getColumnModel().getColumn(i));
  //    int wid = raJdbTable.getColumnModel().getColumn(i).getPreferredWidth();
  //    SavedTable.getColumnModel().getColumn(i).setPreferredWidth(wid);
  //    SavedTable.getColumnModel().getColumn(i).setWidth(wid);
    }

//ST.prn("SavedTable.getColumnCount = ",SavedTable.getColumnCount());

//    test();

    makeCols(aditionalCols);

    jComboB.setSelectedItem(null);

//  jComboB.setSelectedIndex(0);

//    jComboB.setSelectedItem(jComboB.getItemAt(0));

//    raJdbTable.setTableColumnsUI();

//

  }



/*private void test() {

for (int i=0;i<SavedTable.getColumnModel().getColumnCount();i++) {

System.out.print(SavedTable.getColumnModel().getColumn(i).getHeaderValue().toString()+" = ");

System.out.println(SavedTable.getColumnModel().getColumn(i).getWidth());

System.out.print(raDataSet.getColumn(i).getColumnName()+" = ");

System.out.println(raDataSet.getColumn(i).getWidth());

}

}*/

/* ab.f poštelavam selektiranu kolonu za speed search, zovem metodu na itemstatechanged
   i na togglevisibility. Treba li još gdje? */

  private void comboFocused() {

    // Treba kreirati odabranu komponentu u comboboxu da bih izvukao iz njega naziv kolone...

    // ...i ubaciti ga u static varijablu za daljnje operacije

    try {

//      jCBSelectedText = ((JLabel)jComboB.getSelectedItem()).getText();

      // (ab.f) 24.04.2003.

      //jCBSelectedText = jComboB.getSelectedItem().toString();
    	int idx = jComboB.getSelectedIndex();

    // Podesavanje navigacije u jdbNavFld

//      jdbNavFld.setEditable(findNavFldColumnName(jCBSelectedText));

//      jdbNavFld.requestFocus();

    // Prikazi vrijednost checkboxa IZVAN comboboxa u odnosu na vrijednost komponente U comboboxu

//      jCheckBVisible.setSelected(chkIsColInTable(jCBSelectedText));
    	if (raJdbTable != null) 
    		raJdbTable.setSpeedColumn(raJdbTable.convertColumnIndexToView(idx));

      rnvVisible.setIcon(colDisplayed(idx) ? check : uncheck);

    } catch (NullPointerException ne) {}

  }



  public String getSelectedColumnName() {
  	if (raJdbTable == null) return "";
  	
  	return raJdbTable.getModelColumnName(jComboB.getSelectedIndex());
  }



  Column getSelectedColumn(String caption) {

    for (int i=0; i<raDataSet.columnCount(); i++) {

      if (raDataSet.getColumn(i).getCaption().equals(caption)) {

        return raDataSet.getColumn(i);

      }

    }

    return null;

  }

  /**

   * Vraca sve kolone iz dataseta koje su u tom trenutku prikazane u tablici

   */

  public Column[] getColumnsInTable() {
  	if (raJdbTable == null || raDataSet == null) return new Column[0];

    Column[] retVal = new Column[raJdbTable.getColumnCount()];
    for (int i = 0; i < retVal.length; i++)
    	retVal[i] = raDataSet.getColumn(raJdbTable.getRealColumnName(i));
    
    return retVal;
  }
  
  boolean colDisplayed(int idx) {
  	return raJdbTable != null && raJdbTable.convertColumnIndexToView(idx) >= 0;
  }

/*  private boolean chkIsColInTable(String columnCapt) {

  // Provjerava da li kolona sa naslovom columnCapt postoji u tabeli

    try {

      for (int i=0;i<raJdbTable.getColumnModel().getColumnCount();i++) {

        if (columnCapt.equals(raJdbTable.getColumnModel().getColumn(i).getHeaderValue().toString())) {

//ST.prn("Column found in table on index ",i);

        return true;

        }

      }

//ST.prn("Column not found in table!");

      return false;

    }

    catch (NullPointerException e) { //mogucnost da raJdbTable uopce nije setirana

//ST.prn("Column not found in table! (NullPointerException)");

      return false;

    }

  }
*/


  private void addTableColumn(int colIndex, boolean toggle) {

//ST.prn("adding column "+SavedTable.getColumnModel().getColumn(colIndex).getHeaderValue());
    if (colIndex < 0 || colIndex >= SavedTable.getColumnCount()) return;

    raJdbTable.addColumn(SavedTable.getColumn(colIndex));

    //String colHeader = SavedTable.getColumn(colIndex).getHeaderValue().toString();

    //Da je stavi otprilike tamo di je bila

    /*if (colIndex <= raJdbTable.getColumnModel().getColumnCount()-1)

    raJdbTable.moveColumn(findColIndex(colHeader,raJdbTable),colIndex);*/
    
    if (toggle) {
      int prev = raJdbTable.getColumnCount() - 1;
      while (prev > 0 && raJdbTable.convertColumnIndexToModel(prev - 1) > colIndex) --prev;
      if (prev != raJdbTable.getColumnCount() - 1)
        raJdbTable.moveColumn(raJdbTable.getColumnCount() - 1, prev);

    //System.out.println(cw);
    //Da sacuva sirinu
      int wid = cw == null ? 0 : cw.get(raJdbTable.getModelColumnName(colIndex));
      if (wid <= 0) 
      	raJdbTable.updateTableColumn(raJdbTable.convertColumnIndexToView(colIndex));
      /*else {
	      TableColumn tc = raJdbTable.getColumnModel().getColumn(prev);
	      tc.setPreferredWidth(wid);
	      tc.setWidth(wid);
      }*/
    }
  }


/*  private int getRealColumn(int active) {
    return findColIndex(raJdbTable.getColumnModel().
        getColumn(active).getHeaderValue().toString(), SavedTable);
  }*/

  private void delTableColumn(int colIndex) {

//ST.prn("removing column "+raJdbTable.getColumnModel().getColumn(colIndex).getHeaderValue());

    raJdbTable.removeColumn(raJdbTable.getColumnModel().getColumn(colIndex));

  }



  private void toggleColVisibility(int idx) {

  	if (raJdbTable == null || idx < 0) return;
  // Ako postoji kolona u tablici sa naslovom columnName brise je, a ako ne postoji

  // Dodaje je iz SavedTable

//ST.prn("Column name passed = "+columnName);

      if (colDisplayed(idx)) {
      	int vi = raJdbTable.convertColumnIndexToView(idx);
      	SavedTable.getColumn(idx).setPreferredWidth(
      			raJdbTable.getColumnModel().getColumn(vi).getPreferredWidth());
      	SavedTable.getColumn(idx).setWidth(raJdbTable.getColumnModel().getColumn(vi).getWidth());
        delTableColumn(vi);
      } else addTableColumn(idx, true);

      raJdbTable.setSpeedColumn(idx);
      rnvVisible.setIcon(colDisplayed(idx) ? check : uncheck);

  }

  void makeCols(int[] colIdxList) {

  //Iz niza indexa kolona cita intove i ovisno o drugom parametru mice te kolone iz tabele

  //ili mice sve osim njih

    if (colIdxList[0]<0) return; //ako nije setirana varijabla additionalCols bjezi van

/*
    TableColumnModel tmpMod = new DefaultTableColumnModel();
    for (int i = 0; i < raJdbTable.getColumnCount(); i++)
      tmpMod.addColumn(raJdbTable.getColumnModel().getColumn(i));*/
    
    //JraTableInterface TmpTable = raJdbTable.clone(raJdbTable.getModel()); // sacuvaj tablicu kakva je jer kad removas kolone indexi se razjebu

    /*if (toRemove) {

      for (int i=0;i<colIdxList.length;i++) {

        String colcapt = tmpMod.getColumn(colIdxList[i]).getHeaderValue().toString();

        if (chkIsColInTable(colcapt)) delTableColumn(findColIndex(colcapt,raJdbTable));

      }

    }

    else {*/

    //prvo sve maknemo

/*

      System.out.println(TmpTable.getColumnModel().getColumnCount());

      System.out.println(TmpTable.getColumnCount());

      System.out.println(raJdbTable.getColumnModel().getColumnCount());

      System.out.println(raJdbTable.getColumnCount());

*/

      int _columnCount = raJdbTable.getColumnModel().getColumnCount();

      for (int i=0;i<_columnCount;i++) delTableColumn(0);

    //onda dodamo koje su zadane

      for (int i=0; i<colIdxList.length; i++) {

        addTableColumn(colIdxList[i], false);

      }
      if (raJdbTable.getColumnCount() == 0) addTableColumn(0, false);

    //}

    //TmpTable = null;

  }

/*  private int findColIndex(String colname, JraTable2 tableToSearch) {
    return findColIndex(colname, tableToSearch.getColumnModel());
  }
*/

/*  private int findColIndex(String colname, TableColumnModel tableToSearch) {

  // Vraca index kolone sa naslovom colname u tablici tableToSearch

  // ako je nije nasao vraca -1

//ST.prn("tableToSearch = null? ",(tableToSearch == null));

//ST.prn("colname = null? ",(colname == null));

      for (int i=0;i<tableToSearch.getColumnCount();i++) {

        if (colname.equals(tableToSearch.getColumn(i).getHeaderValue().toString())) {

//ST.prn("Column found in table on index = ",i);

          return i;

        }

      }

//ST.prn("column NOT found in saved table, returning -1");

      return -1;

  }*/


  private void checkedAction() {

    //sisnuo je na vanjski checkbox

    toggleColVisibility(jComboB.getSelectedIndex());

    makeSaveValue();
    // treba refreshati combo tako da se applaya nevidljiva kolona

    jComboB.repaint();

  }



  public void kumulAction() {

    kumAction(getSelectedColumnName());

  }



  public void kumAction(String kumColName) {

  }



  private void findAction() {

    raDataSet = raJdbTable.getDataSet();
    
    RowFilterListener filter = raDataSet.getRowFilterListener();

    if (filter != null) {

      raDataSet.removeRowFilterListener(filter);

      raDataSet.setSort(raDataSet.getSort());

      if (getRaJdbTable() instanceof JraTable2) {
        JraTable2 tab = (JraTable2) getRaJdbTable();
        raSelectTableModifier stm = tab.hasSelectionTrackerInstalled();
        if (stm != null && stm.isNatural()) stm.clearSelection();
        tab.fireTableDataChanged();
      }



    } else {

      if (getTopLevelAncestor() instanceof java.awt.Frame) {

        new searchDialog((java.awt.Frame)getTopLevelAncestor()).go(this);

      } else if (getTopLevelAncestor() instanceof java.awt.Dialog) {

        new searchDialog((java.awt.Dialog)getTopLevelAncestor()).go(this);

      } else new searchDialog().go(this);

    }

    checkFilter();

  }

  private void showQuery() {

    try {

      if (raDataSet instanceof com.borland.dx.sql.dataset.QueryDataSet) {

        com.borland.dx.sql.dataset.QueryDataSet qds = (com.borland.dx.sql.dataset.QueryDataSet)raDataSet;

        System.out.println("cb_query = "+qds.getQuery().getQueryString());
        
        // idea by Andrei: kod refresha automatski ubaci query u clipboard! Genijalno
        if (clicked) {
          JTextField clip = new JTextField();
          clip.setText(qds.getQuery().getQueryString());
          clip.selectAll();
          clip.cut();
          clicked = false;
        }
      }

    }

    catch (Exception ex) {



    }

  }

  public void refreshAction() {
    
    raDataSet = raJdbTable.getDataSet();

    if (raDataSet.refreshSupported()) {

      //saveSettings();

      raDataSet.refresh();

      showQuery();

    }

 /*   if (raJdbTable instanceof hr.restart.swing.JraTable) {

      initialize();

    } else if (raJdbTable instanceof hr.restart.swing.JraTable2) {*/

//      ((hr.restart.swing.dataSetTableModel)raJdbTable.getModel()).fireTableDataChanged();

      raJdbTable.fireTableDataChanged();

    //}

  }

  void jComboB_itemStateChanged(ItemEvent e) {

    comboFocused();

  }

  public String getComboSelectedItem() {
//    return jCBSelectedText;
    if (jComboB.getSelectedItem()==null) return "";
    return jComboB.getSelectedItem().toString();
  }
  public void setComboSelectedItem(String txt) {
    jComboB.setSelectedItem(txt);
  }
  
  public void setFirstItem() {
  	try {
  		jComboB.setSelectedIndex(raJdbTable.convertColumnIndexToModel(0));
  	} catch (Exception e) {
  		//
  	}
  }
  
  static class ColumnWidths {
  	Map storage;
  	
  	public ColumnWidths() {
  		storage = new HashMap();
  	}
  	
  	public void put(String name, int wid) {
  		storage.put(name.toLowerCase(), new Integer(wid));
  	}
  	
  	public int get(String name) {
  		Integer wid = (Integer) storage.get(name.toLowerCase());
  		return wid == null ? 0 : wid.intValue();
  	}
  	
  	public String toString() {
  		return storage.toString();
  	}
  }

static ImageIcon check = raImages.getImageIcon(raImages.IMGCHECK);

static ImageIcon uncheck = raImages.getImageIcon(raImages.IMGUNCHECK);

// (ab.f)  24.04.2003.  renderer.

class ColumnsCellRenderer extends DefaultListCellRenderer {

  // empty border koji pomice komponentu 5 piksela udesno

  Border emptyBorder = BorderFactory.createEmptyBorder(0, 5, 0, 0);

  // optimizacija boldanja fontova

  Font normal, bold;

  public void setFont(Font f) {}

  public Component getListCellRendererComponent (

        JList list,

        Object value,

        int index,

        boolean isSelected,

        boolean cellHasFocus)

  {

    setComponentOrientation(list.getComponentOrientation());

    if (isSelected) {

      setBackground(list.getSelectionBackground());

      setForeground(list.getSelectionForeground());

    }

    else {

      setBackground(list.getBackground());

      setForeground(list.getForeground());

    }

    if (value == null || (list.getSelectedValue() == null && index < 0)) {

      setText("");

      setIcon(null);

      return this;

    }

    setText(value.toString());

    // trebam li raditi bold font?

    // Da, ako jos nije setiran ili ako se promijenio font liste

    if (list.getFont() != normal || bold == null)

      bold = list.getFont().deriveFont(Font.BOLD);

    normal = list.getFont();

    // pomakni komponentu 5 piksela udesno

    setBorder(emptyBorder);

    boolean vis = index >= 0 ? colDisplayed(index) :
                  colDisplayed(list.getSelectedIndex());

    try {

      super.setFont(vis ? bold : normal);

      setIcon(vis ? check : uncheck);

    } catch (Exception e) {}

    return this;

  }
  
  public boolean isOpaque() {
    return true;
  }

}//end of class ComBoxCellRenderer

} //EOC (end of class Columnsbean)

