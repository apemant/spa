/****license*****************************************************************
**   file: raJPTableView.java
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



import hr.restart.baza.Condition;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraPanel;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.dataSetTableModel;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raRepaintManager;
import hr.restart.swing.raSelectTableModifier;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.basic.BasicViewportUI;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;




/**

 * Panel na kojem se nalazi tablica, columnsbean i kumulativi i sve je handlano sa par settera

 * <pre>

 * PRIMJER KORISTENJA:

 * public class intFr2 extends JFrame {

 *  hr.restart.util.raJPTableView jptv = new hr.restart.util.raJPTableView();

 *  public intFr2() {

 *   try {

 *     jbInit();

 *   }

 *   catch(Exception e) {

 *     e.printStackTrace();

 *   }

 *  }

 *  private void jbInit() throws Exception {

 *    jptv.setDataSet(hr.restart.baza.dM.getDataModule().getPartneri());

 *    jptv.setVisibleCols(new int[] {0,1,2,21});            // {@link #setVisibleCols(int[])}

 *    jptv.setKumTak(true);                                 // {@link #setKumTak(boolean)}

 *    jptv.setStoZbrojiti(new String[] {"LIMKRED"});        // {@link #setStoZbrojiti(String[])}

 *    this.getContentPane().setLayout(new BorderLayout());

 *    this.getContentPane().add(jptv,BorderLayout.CENTER);

 *    jptv.initKeyListener(this);

 *  }

 * }

 * </pre>

 */



public class raJPTableView extends JPanel {





  private JPanel jPanelTable = new JPanel(new BorderLayout());

//NotablePanel

  private boolean toggleTableEnabled = true;

  private JPanel jpNoTablePanel = null;

  private JPanel showedNoTablePanel = null;

  private JraTextField focusedNoTablePanelField;

  private com.borland.dx.sql.dataset.QueryDataSet noTableDataSet;

  private com.borland.dx.sql.dataset.QueryDataSet yesTableDataSet;

  private Object[] noTablePanelValues;

  private int noTablePanelProtectPolicy = PROTECTALL;

  public static final int PROTECTALL = 0;

  public static final int PROTECTFOUND = 2;

  public static final int PROTECTHACKED = 4;

  public static final int PROTECTNONE = 8;

  private java.util.HashSet hackedKeys;

//

  private hr.restart.swing.JraTableScrollPane jScrollPaneTable = new hr.restart.swing.JraTableScrollPane();

/*

  private hr.restart.util.columnsbean.ColumnsBean colbean = new hr.restart.util.columnsbean.ColumnsBean() {

    public void kumAction(String kumColName) {

      kumPressed(kumColName);

    }

  };

*/

  private raNavBar navBar = new raNavBar(raNavBar.COLUMNSBEAN) {
    public void afterRefresh() {
      removeSelection();
      navBar_afterRefresh();
    }
  };

  private com.borland.dx.dataset.StorageDataSet raQueryDataSet;

  private String[] keyColumns = null;

  private boolean createCB = true;

  hr.restart.swing.JraTable2 mpTable = new hr.restart.swing.raExtendedTable() {

    public void killFocus(java.util.EventObject e) {
      mpTable_killFocus(e);
    }

    public void tableDoubleClicked() {
      mpTable_doubleClicked();
    }
    
    public boolean allowRowChange(int oldrow, int newrow) {
      return mpTable_allowRowChange(oldrow, newrow);
    }

    public void rowChanged(int oldrow, int newrow, boolean toggle, boolean extend) {
      mpTable_rowChanged(oldrow, newrow, toggle, extend);
    }
    
    public void setTableColumnsUI() {
      super.setTableColumnsUI();
      if (navBar.getColBean() != null)
      	navBar.getColBean().updateColumnWidths();
    }
  };
  
  JComponent summary = null;
  
  java.awt.event.KeyAdapter TableKeyAdapter = new java.awt.event.KeyAdapter() {

      public void keyPressed( KeyEvent e){

        mpTable.processTableKeyEvent(e);

        processSelectAction(e);

      }

      public void keyTyped( KeyEvent e){

        mpTable.processTableKeyTyped(e);

      }

  };

  //kum

  private TabelaSume tas = null;

  private String tasTitle = "";

  private boolean kum_tag=false;

  private boolean showingKum = false;

  javax.swing.JLabel [] fildovi; //???

  private java.math.BigDecimal suma[];

  private String[] stozbrojiti;

  private String[] Naslovi = null;

  private raRowSume sumrow;



/**

 * Konstruktor

 */

  public raJPTableView() {

    try {

//System.out.println("new raJPTableView");

      jbInit();

    } catch (Exception e) {

      e.printStackTrace();

    }

  }

  public raJPTableView(JraTable2 tab) {
    mpTable = tab;
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public raJPTableView(boolean createCBC) {

//System.out.println("new raJPTableView(boolean)");



    setCreateCB(createCBC);

    try {

      jbInit();

    } catch (Exception e) {

      e.printStackTrace();

    }

  }

  public void jbInit() throws Exception {

    this.setLayout(new BorderLayout());

    //jScrollPaneTable.setPreferredSize(new Dimension(300, 200));

    jScrollPaneTable.setBorder(BorderFactory.createEmptyBorder());

    jScrollPaneTable.setViewportBorder(BorderFactory.createEmptyBorder());

    setBorder(BorderFactory.createEmptyBorder());

    jScrollPaneTable.setVerticalScrollBarPolicy(JraScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    jScrollPaneTable.getVerticalScrollBar().setUnitIncrement(mpTable.getRowHeight());

    this.addAncestorListener(new javax.swing.event.AncestorListener() {

      public void ancestorAdded(AncestorEvent e) {

        init_kum();

        //makeKumul();

      }

      public void ancestorRemoved(AncestorEvent e) {

        hideTas();

      }

      public void ancestorMoved(AncestorEvent e) {}

    });

    jPanelTable.add(jScrollPaneTable, BorderLayout.CENTER);

    this.add(jPanelTable,BorderLayout.CENTER);

    jScrollPaneTable.setViewportView(mpTable);
    //jScrollPaneTable.setViewportBorder(BorderFactory.createEtchedBorder());
    //jScrollPaneTable.getViewport().add(mpTable, null);
    
    if (mpTable instanceof raExtendedTable) {
      raExtendedTable ext = (raExtendedTable) mpTable;
      ext.setOwner(this);
    }
    
    setSpeedType();
  }
  
  class TrackPanel extends JPanel implements Scrollable {
    public TrackPanel() {
      super(new BorderLayout());
    }
    public Dimension getPreferredScrollableViewportSize() {
      return mpTable.getPreferredScrollableViewportSize();
    }
    public int getScrollableBlockIncrement(Rectangle visibleRect,
        int orientation, int direction) {
      return mpTable.getScrollableBlockIncrement(visibleRect, orientation, direction);
    }
    public boolean getScrollableTracksViewportHeight() {
      return mpTable.getScrollableTracksViewportHeight();
    }
    public boolean getScrollableTracksViewportWidth() {
      return mpTable.getScrollableTracksViewportWidth();
    }
    public int getScrollableUnitIncrement(Rectangle visibleRect,
        int orientation, int direction) {
      return mpTable.getScrollableUnitIncrement(visibleRect, orientation, direction);
    }
  }
  
  public void installSummary(JComponent mpDod, int spacing, boolean trailing) {
    jScrollPaneTable.setViewportView(null);
    
    JPanel outer = new TrackPanel();
    JPanel lower = new JPanel(new BorderLayout());
    JPanel tab = new JPanel(new BorderLayout());

    outer.add(mpTable, BorderLayout.NORTH);
    outer.add(lower);
    lower.add(Box.createVerticalStrut(spacing), BorderLayout.NORTH);
    lower.add(Box.createGlue());
    lower.add(tab, trailing ? BorderLayout.EAST : BorderLayout.WEST);
    tab.add(mpDod, BorderLayout.NORTH);
    tab.add(Box.createGlue());
    jScrollPaneTable.setViewportView(outer);
    jScrollPaneTable.setColumnHeaderView(mpTable.getTableHeader());
    
    summary = mpDod;
  }
  
  public JComponent getSummary() {
    return summary;
  }

  public static boolean speedset = false;
  void setSpeedType() {
    if (speedset) return;
    speedset = true;
    String st = frmParam.getParam("sisfun", "speedSearch", "0", 
        "Naèin prekapèanja brze pretrage s poèetka na sredinu (0,1,2)", true);
    if ("1".equals(st)) mpTable.setSpeedType(mpTable.SPEED_AUTOMATIC);
    else if ("2".equals(st)) mpTable.setSpeedType(mpTable.SPEED_ALWAYS);
    else mpTable.setSpeedType(mpTable.SPEED_ASTERISK);
  }

  private void instanceTas() {

    if (tas != null) return;

    if (hr.restart.start.FRAME_MODE == raFrame.PANEL) {

      tas= new TabelaSume(hr.restart.mainFrame.getMainFrame());

      return;

    }

    if (getTopLevelAncestor() instanceof java.awt.Frame)

      tas= new TabelaSume((Frame)getTopLevelAncestor());

    else if (getTopLevelAncestor() instanceof java.awt.Dialog)

      tas= new TabelaSume((Dialog)getTopLevelAncestor());

    else

      tas= new TabelaSume((java.awt.Frame)null);

  }

  /**

   * Dodaje keyListener na zadanu komponentu za manipulaciju tabelom

   * (zove JraTable.processTableKeyEvent(e) na keyPressed).

   * pozvati u jbInit()

   */

  public void initKeyListener(Component comp) {
    AWTKeyboard.registerKeyListener(comp, TableKeyAdapter);
    navBar.registerNavBarKeys(comp);
    /*
    if (navBar.getColBean() != null) navBar.getColBean().registerComboKeyListener(TableKeyAdapter);
    comp.addKeyListener(TableKeyAdapter);
    mpTable.addKeyListener(TableKeyAdapter);
    navBar.registerNavBarKeys(comp);
    navBar.registerNavBarKeys(mpTable);*/
  }

  /**

   * Dodaje keyListener na zadani raFrame za manipulaciju tabelom

   * (zove JraTable.processTableKeyEvent(e) na keyPressed).

   * pozvati u jbInit()

   */

  public void initKeyListener(hr.restart.util.raFrame raframe) {
    AWTKeyboard.registerKeyListener(raframe.getWindow(), TableKeyAdapter);
    navBar.registerNavBarKeys(raframe.getWindow());
    /*if (navBar.getColBean() != null) navBar.getColBean().registerComboKeyListener(TableKeyAdapter);
    raframe.addKeyListener(TableKeyAdapter);
    mpTable.addKeyListener(TableKeyAdapter);
    navBar.registerNavBarKeys(raframe);
    navBar.registerNavBarKeys(mpTable);*/
  }

  /**

   * Brise keyListener na zadanu komponentu za manipulaciju tabelom

   */

  public void rmKeyListener(Component comp) {
    AWTKeyboard.unregisterKeyListener(comp, TableKeyAdapter);
    navBar.unregisterNavBarKeys(comp);
    /*if (navBar.getColBean() != null) navBar.getColBean().unregisterComboKeyListener(TableKeyAdapter);
    comp.removeKeyListener(TableKeyAdapter);
    mpTable.removeKeyListener(TableKeyAdapter);
    navBar.unregisterNavBarKeys(comp);
    navBar.unregisterNavBarKeys(mpTable);*/
  }

  /**

   * Brise keyListener na zadani raFrame za manipulaciju tabelom

   */

  public void rmKeyListener(hr.restart.util.raFrame raframe) {
    AWTKeyboard.unregisterKeyListener(raframe.getWindow(), TableKeyAdapter);
    navBar.unregisterNavBarKeys(raframe.getWindow());
    /*
    if (navBar.getColBean() != null) navBar.getColBean().unregisterComboKeyListener(TableKeyAdapter);
    raframe.removeKeyListener(TableKeyAdapter);
    mpTable.removeKeyListener(TableKeyAdapter);
    navBar.unregisterNavBarKeys(raframe);
    navBar.unregisterNavBarKeys(mpTable);*/
  }

  // af.b  ukljucuje ignoriranje repaint poziva

  private boolean ignoreRepaint = false;

  public void repaint(long tm, int x, int y, int width, int height) {
    if (!ignoreRepaint && raRepaintManager.isSwingAllowed(this)) super.repaint(tm, x, y, width, height);
  }

  public void paint(java.awt.Graphics g) {
    if (!ignoreRepaint && raRepaintManager.isSwingAllowed(this)) super.paint(g);
  }

  public boolean isShowing() {
    return (!ignoreRepaint && super.isShowing() && raRepaintManager.isSwingAllowed(this));
  }

  public void setIgnoreRepaint(boolean ignore) {
    ignoreRepaint = ignore;
    if (mpTable != null) mpTable.setIgnoreRepaint(ignore);
    jScrollPaneTable.setIgnoreRepaint(ignore);
  }

  public void setDataSetAndSums(com.borland.dx.dataset.StorageDataSet newDS, String[] cols) {
    if (raQueryDataSet != null) {
      try {
        raQueryDataSet.removeEditListener(editListener);
      } catch (Exception e) {
      };
    }
    raQueryDataSet = newDS;
    
    boolean vis = isShowing();
    setKumTak(true);
//    ((dataSetTableModel) mpTable.getModel()).getTableSumRow().setSumingEnabled(true);
//    jScrollPaneTable.setViewportView(null);
    if (vis) setIgnoreRepaint(true);
//    mpTable.ignoreNavigation = true;
//    mpTable.ignoreNavigation = true;
//    jScrollPaneTable.setViewportView(null);
    setKeyColumns(null);
    ((dataSetTableModel) mpTable.getModel()).getTableSumRow().setSumingEnabled(true);
    mpTable.setDataSet(raQueryDataSet);
    if (createCB) createColumnsBean();
    try {
      raQueryDataSet.addEditListener(editListener);
    } catch (Exception e) {};
//    if (this.isShowing()) init_kum();

    setStoZbrojiti(cols);

    init_kum();

//    mpTable.pos();

//    mpTable.getDataSet().last();
//    mpTable.setRowSelectionInterval(
//        mpTable.getDataSet().getRow(), mpTable.getDataSet().getRow());
//    mpTable.some.add("before\n");
//    System.err.println("before showrow");
//    mpTable.showSumRow();
//    System.err.println("after showrow");
//    mpTable.some.add("after\n");

//        repaint();
//      }
//    });

//    SwingUtilities.invokeLater(new Runnable() {
//      public void run() {
//        mpTable.pos();
//        mpTable.showSumRow();

    //setStoZbrojiti(new String[] {});
    if (vis) enableAndRepaint();
  }

  private void enableAndRepaint() {
    if (!SwingUtilities.isEventDispatchThread())
      try {
      SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          mpTable.pos();
          setIgnoreRepaint(false);
          repaint();
        }
      });
      } catch (Exception e) {
        e.printStackTrace();
      }
    else {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          mpTable.pos();
          setIgnoreRepaint(false);
          repaint();
        }
      });
      /*mpTable.pos();
      setIgnoreRepaint(false);
      repaint();*/
    }
  }

  public void clearDataSet() {
    /*
    setKumTak(true);
//    ((dataSetTableModel) mpTable.getModel()).getTableSumRow().setSumingEnabled(true);
    this.setVisible(false);
    raQueryDataSet = null;
    setKeyColumns(null);
    mpTable.setDataSet(null);
    destroy_kum();
    this.setVisible(true);*/
    setDataSet(null);
  }

  // ab.f

/**

 * Seter za dataset - OBAVEZAN

 */

  public void setDataSet(com.borland.dx.dataset.StorageDataSet newDS) {

    setDataSet(newDS,true);

  }

  private void setDataSet(com.borland.dx.dataset.StorageDataSet newDS, boolean fuckUPKeys) {
    if (raQueryDataSet != null) {
      try {
        raQueryDataSet.removeEditListener(editListener);
      } catch (Exception e) {
      };
    }
    raQueryDataSet = newDS;

    boolean vis = isShowing();
    if (vis) setIgnoreRepaint(true);

    if (fuckUPKeys) setKeyColumns(null);

    mpTable.setDataSet(raQueryDataSet);

    if (raQueryDataSet==null) {

      destroy_kum();
      if (getColumnsBean() != null)
        getColumnsBean().clearDataSet();

    } else {

      if (createCB) createColumnsBean();

        try {

        raQueryDataSet.addEditListener(editListener);

      } catch (Exception e) {

      };

      if (this.isShowing()) init_kum();

    }
    if (vis) enableAndRepaint();

  }

  public void setDataSet(com.borland.dx.sql.dataset.QueryDataSet newDS) {

    setDataSet((com.borland.dx.dataset.StorageDataSet)newDS);

  }

  private void setDataSet(com.borland.dx.sql.dataset.QueryDataSet newDS, boolean fuckUPKeys) {

    setDataSet((com.borland.dx.dataset.StorageDataSet)newDS,fuckUPKeys);

  }

  public com.borland.dx.sql.dataset.QueryDataSet getDataSet() {

    return (com.borland.dx.sql.dataset.QueryDataSet)raQueryDataSet;

  }

  public com.borland.dx.dataset.StorageDataSet getStorageDataSet() {

    return raQueryDataSet;

  }



/**

 * Kreirati colunsbean ili ne?

 */

  public void setCreateCB(boolean newCreateCB) {

    createCB=newCreateCB;

  }

/**

 * Vraca JraTable koji se nalazi na ovom panelu

 */

  public hr.restart.swing.JraTable2 getMpTable() {

    return mpTable;

  }

/**

 * Vidljive kolone za ColumnsBean

 */

  public void setVisibleCols(int[] newVisibleCols) {



//    visibleCols = newVisibleCols;

    navBar.getColBean().setAditionalCols(newVisibleCols);

  }

  void createColumnsBean(){

    navBar.getColBean().setRaJdbTable(mpTable);

    navBar.setJpTabView(this);

    if (!this.isAncestorOf(navBar))
      this.add(navBar, BorderLayout.NORTH);
    
/*    if (this.isShowing() && raQueryDataSet != null)
      navBar.getColBean().initialize();
*/
  }

/**

 * JraTable automatski mice focus sa sebe, a u ovoj metodu moze se navesti gdje (npr. nekiJComponent.requestFocus())

 * po defaultu zove hr.restart.swing.JraKeyListener.focusNext(EventObject e)

 */

  public void mpTable_killFocus(java.util.EventObject e) {

    hr.restart.swing.JraKeyListener.focusNext(e);

  }

/**

 * Vraca ColumnsBean ako netko ima nekog interesa mijenjati ga

 */

  public hr.restart.util.columnsbean.ColumnsBean getColumnsBean() {

    return navBar.getColBean();

  }



  public raNavBar getNavBar() {

    return navBar;

  }



  public void setNavBar(raNavBar bar) {

    navBar = bar;

  }

  public void hidePopups() {
    try {
      getColumnsBean().hidePopup();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showTas() {

    instanceTas();

    getStorageDataSet().last();

    tas.setTitle(tasTitle);

    tas.go(this);

  }

  private void hideTas() {

    if (tas!=null) tas.hide();

  }

  private void createKumTableRow() {

    sumrow = new raRowSume(mpTable) {

      public void show_sum() {

        showTas();

      }

    };

    sumrow.setColumns4Sum(stozbrojiti);
    
    int colSel = mpTable.getColumnModel().getSelectionModel().getMinSelectionIndex();
    
    sumrow.initialize();
    
    try {
      if (colSel >= 0) mpTable.getColumnModel().getSelectionModel().setSelectionInterval(colSel, colSel);
    } catch (Exception e) {}

//airtsum    this.add(sumrow,BorderLayout.SOUTH);

    if (getColumnsBean() != null) getColumnsBean().setSumRow(sumrow);

//    jScrollPaneTable.add(sumrow);

  }

/**

 * Pozvati pri prikazivanju odn. nakon setiranja dataseta, kum_tag i stozbrojiti

 */

  public void init_kum() {

    if (raQueryDataSet == null) return;

    if (this.kum_tag) {
      
      instanceTas();
      
      tas.transfer(getNaslovi());
      
      createKumTableRow();
      
      showingKum = true;
      
      Zbrajalo();
      
//      raQueryDataSet.last();


      fireTableDataChanged();


//        showTas();

    }

  }

  public void destroy_kum() {

    if (this.kum_tag) {

      if (tas != null) tas.dispose();

      tas = null;

      showingKum = false;

      try {

//airtsum        this.remove(sumrow);
        int colSel = mpTable.getColumnModel().getSelectionModel().getMinSelectionIndex();

        sumrow.removeSum();
        
        if (colSel >= 0) mpTable.getColumnModel().getSelectionModel().setSelectionInterval(colSel, colSel);

      } catch (Exception e) {

      }

      sumrow = null;

      this.validate();

    }

  }

/**

 * Setira se indikator da li postoje kumulativi true = postoje; false = ne postoje

 */

  public void setKumTak(boolean bulin){

    kum_tag=bulin;

  }

/**

 * Zadaje se niz stringova u kojem su navedena imena kolona za zbrajanje.

 * Automatski formira i Naslovi od captiona kolona u DataSet-u ako je prije toga setiran

 */

  public void setStoZbrojiti(String[] sto){

      if (!kum_tag) return;

      stozbrojiti=sto;

      Naslovi = null;

      makeDefNaslovi();

  }



  private void makeDefNaslovi() {

    if (!kum_tag) return;

    if (Naslovi!=null) return;

    if (raQueryDataSet==null) return;

    if (stozbrojiti == null) {

      setNaslovi(null);

      return;

    }

    String[] newNaslovi = new String[stozbrojiti.length];

    for (int i=0;i<stozbrojiti.length;i++) {

      com.borland.dx.dataset.Column col = raQueryDataSet.hasColumn(stozbrojiti[i]);

      if (col==null) return;

      newNaslovi[i] = col.getCaption();

    }

    setNaslovi(newNaslovi);

  }



  public void setFewRowSize() {

    this.setPreferredSize(getFewRowSize());

  }



  Dimension getFewRowSize() {

    int wdth = getWidth();

    int hght =

      raNavAction.ACTSIZE+

      getInsets().top + getInsets().bottom +

      getMpTable().getRowHeight()*(1+5);

    return new Dimension(wdth,hght);

  }



  public String[] getStoZbrojiti(){

    return stozbrojiti;

  }

/**

 * Zadaju se naslovi koji ce biti u posebnom ekranu za kumulative - ako nisu zadani uzima caption od dataset.Columna

 */

  public void setNaslovi(String[] Nas){

    if (!kum_tag) return;

    Naslovi=Nas;

  }



  public void enableEvents(boolean enab) {

    if (getStorageDataSet() == null) return;

    getStorageDataSet().enableDataSetEvents(enab);

    if (enab) {

      mpTable.startFire();

      mpTable.fireTableDataChanged();

    } else {

      mpTable.stopFire();

    }

  }

  /**

   * Vraca ime kolone na koju je user zadnje kliknuo za sort

   * PAZI!! ako nije kliknuo onda je null

   * @return

   */

  public String getTableSortColumnName() {

    return ((hr.restart.swing.JraTable2)mpTable).getTableSortColumnName();

  }

  public String[] getNaslovi(){

    if (Naslovi == null) makeDefNaslovi();

    return Naslovi;

  }

/**

 * Indikator da li je poseban ekran sa kumulativima prikazan ili nije

 */

  public boolean isShowingKum() {

    return showingKum;

  }

/**

 * Overridati da bi se handlao event double click na tablicu

 */

  public void mpTable_doubleClicked() {

  }

  public void fireTableDataChanged() {

    mpTable.fireTableDataChanged();

  }



  public raRowSume getSumRow() {

    return sumrow;

  }



  public void setTasTitle(String titl) {

    tasTitle = titl;

    if (tas!=null) tas.setTitle(tasTitle);

  }

  public java.math.BigDecimal getSum(String colname) {

    try {

      tas.TSSet.goToRow(Util.getUtil().indexOfArray(stozbrojiti,colname));

      return tas.TSSet.getBigDecimal("TOTAL");

    }

    catch (Exception ex) {

      boolean handlestozbrojiti = stozbrojiti == null;

      if (handlestozbrojiti) stozbrojiti = new String[] {colname};

      com.borland.dx.dataset.Variant[] vvsum = zbroji();

      if (handlestozbrojiti) {

        stozbrojiti = null;

        return vvsum[0].getBigDecimal();

      } else {

        return vvsum[Util.getUtil().indexOfArray(stozbrojiti,colname)].getBigDecimal();

      }



//      return new java.math.BigDecimal(0);

    }

  }

  void kumPressed(String kumColName) {

//    boolean isKumul = UT.containsArr(stozbrojiti,kumColName);

    showTas();

//    toggleKumul(kumColName);

  }



  public void toggleKumul(String kumColName) {

    Util UT = Util.getUtil();

    Valid VL = Valid.getValid();

    com.borland.dx.dataset.Column kumColumn = raQueryDataSet.getColumn(kumColName);

    /*

      provjeriti da li je polje kumulabilno:) po tipu, ako nije odjebat

      provjeriti da li se ita kumulira

        ako se nista ne kumulira novi stozbrojiti i setKum_tak()

        ako se nesto kumulira provjeriti da li se TO polje vec kumulira

          ako se kumulira maknuti ga

            ako se vise nista ne kumulira destroy_sum; stozbrojiti=null; setKum_tak(false)

          ako se ne kumulira dodati ga u kumulativ



      podesiti sumrow (destroy_kum, init_kum)



    */

    if (!VL.isNumeric(kumColumn)) {

      JOptionPane.showMessageDialog(getTopLevelAncestor(),

          "Podatak '"+kumColumn.getCaption()+"' nije mogu\u0107e zbrajati!",

          "Greka",JOptionPane.ERROR_MESSAGE);

      return;

    }



    if (stozbrojiti == null) {

      stozbrojiti = new String[] {kumColName};

      Naslovi = new String[] {kumColumn.getCaption()};

      setKumTak(true);

    } else {

      if (UT.containsArr(stozbrojiti,kumColName)) {

        int idx = UT.indexOfArray(stozbrojiti,kumColName);

        stozbrojiti = UT.rmFromString(stozbrojiti,kumColName);

        Naslovi = UT.rmFromString(Naslovi,Naslovi[idx]);

      } else {

        stozbrojiti = UT.concatArrayStr(stozbrojiti,kumColName);

        Naslovi = UT.concatArrayStr(Naslovi,kumColumn.getCaption());

      }

    }

    if (stozbrojiti.length==0) {

      stozbrojiti = null;

      Naslovi = null;

    }



//    makeKumul();

  }

  public void navBar_afterRefresh() {
  }


  void makeKumul() {

    destroy_kum();

    setKumTak(stozbrojiti != null);

    init_kum();

  }



/**

 * Zbrajalo zbraja kolone u DataSetu zadane sa stozbrojiti i puni u TabelaSume i raRowSume

 */

  public void Zbrajalo(){

/*

    if (raQueryDataSet instanceof com.borland.dx.sql.dataset.QueryDataSet) {

      System.out.println("Zbrajalo za dataset "+

                         ((com.borland.dx.sql.dataset.QueryDataSet)raQueryDataSet).getQuery().getQueryString());

    } else {

      System.out.println("Zbrajalo za StorageDataSet "+raQueryDataSet);

    }

*/

    if (!kum_tag) {

//      System.out.println("ne zbrajam jer je kum_tag = "+kum_tag);

      return;

    }

    if (stozbrojiti==null) {

//      System.out.println("ne zbrajam jer je stozbrojiti = "+stozbrojiti);

      return;

    }

    if ((sumrow==null)||(tas==null)) {

//      System.out.println("ne zbrajam jer je sumrow = "+sumrow);

//      System.out.println("           ili je tas    = "+tas);

      return;

    }

    if (!showingKum) {

//      System.out.println("ne zbrajam jer je showingKum = "+showingKum);

      return;

    }

    if (raQueryDataSet.getRowCount()==0) {

/* airtsum

      sumrow.sumRow.clearValues();

      sumrow.refresh();

*/

//      System.out.println("ne zbrajam jer je raQueryDataSet.getRowCount() = "+raQueryDataSet.getRowCount());

      return;

    }

//    System.out.println("zbrajam");

    com.borland.dx.dataset.Variant[] vvsum = zbroji();

    // u tablicu kumulativa

    for (int memb=0;memb<vvsum.length;memb++) tas.napuni(vvsum[memb],memb);

/* airtsum

    // u sumRow

    for (int memb=0;memb<vvsum.length;memb++) sumrow.sumRow.setVariant(stozbrojiti[memb],vvsum[memb]);

    sumrow.refresh();

*/

  }

  private com.borland.dx.dataset.Variant[] zbroji() {

    com.borland.dx.dataset.Variant[] vvsum = null;

    try {

      com.borland.dx.dataset.Variant vv = new com.borland.dx.dataset.Variant();

      vvsum = new com.borland.dx.dataset.Variant[stozbrojiti.length];

      for (int rownum=0;rownum<raQueryDataSet.getRowCount();rownum++) {

        for (int i=0;i<stozbrojiti.length;i++) {

          if (rownum==0) {

            vvsum[i] = new com.borland.dx.dataset.Variant();

            raQueryDataSet.getVariant(stozbrojiti[i],rownum,vvsum[i]);

          } else {

            raQueryDataSet.getVariant(stozbrojiti[i],rownum,vv);

            vvsum[i].add(vv,vvsum[i]);

          }

        }

      }

    } catch (Exception ex) {

      System.out.println("raJPTableView.zbroji exception "+ex);

      ex.printStackTrace();

    }

    return vvsum;

  }



  public String[] getKeyColumns() {

    if (keyColumns != null) return keyColumns;

    if (raQueryDataSet == null) {

      keyColumns = null;

      return keyColumns;

    }

    if (raQueryDataSet instanceof com.borland.dx.sql.dataset.QueryDataSet) {

      String tableName = Valid.getTableName(getDataSet().getQuery().getQueryString());

      if (tableName.indexOf(",") > -1) {

        keyColumns = null;

        return keyColumns;

      }

      keyColumns = hr.restart.db.raConnectionFactory.getKeyColumns(getDataSet().getDatabase().getJdbcConnection(),tableName);

      if (keyColumns.length == 0) return null;

      return keyColumns;

    }

    return null;

  }



  public void setKeyColumns(String[] kys) {

    keyColumns = kys;

  }



  /**

   * pogledaj dokumentaciju hr.resart.swing.JraTable2, raTableModifier, raTableUIModifier, raTableColumnModifier

   * @param m

   */

  public void addTableModifier(hr.restart.swing.raTableModifier m) {

    mpTable.addTableModifier(m);

  }

  protected raSelectTableModifier selMod = null;
  private boolean selAllToggles = true;

  public void setSelectAllToggle(boolean toggleAll) {
    selAllToggles = toggleAll;
  }
  
  public void installNaturalSelectionTracker() {
    uninstallSelectionTracker();
    addTableModifier(selMod = new raSelectTableModifier(getStorageDataSet(), ""));
  }
  
  public void installSelectionTracker(String[] keyCols) {
    uninstallSelectionTracker();
    addTableModifier(selMod = new raSelectTableModifier(getStorageDataSet(), keyCols));
  }

  public void installSelectionTracker(String column) {
    uninstallSelectionTracker();
    addTableModifier(selMod = new raSelectTableModifier(getStorageDataSet(), column));
  }

  public void uninstallSelectionTracker() {
    if (selMod != null) removeTableModifier(selMod);
    selMod = null;
  }

  public Condition getSelectCondition() {
    if (selMod == null) return null;
    return selMod.getSelectionCondition();
  }

  public int getSelectCount() {
    if (selMod == null) return 0;
    return selMod.countSelected();
  }

  public Object[] getSelection() {
    if (selMod == null) return null;
    return selMod.getSelection();
  }
  
  public DataSet getSelectionView() {
    if (getSelectCount() == 0) return getStorageDataSet();
    return selMod.getSelectedView();
  }
  
  public void destroySelectionView() {
    if (selMod != null) selMod.destroySelectedView();
  }

  public void removeSelection() {
    if (selMod != null) {
      selMod.clearSelection();
      fireTableDataChanged();
    }
  }

  void processSelectAction(KeyEvent e) {
    if (selMod != null && !e.isConsumed() && raQueryDataSet != null &&
        raQueryDataSet.rowCount() > 0 && mpTable.isShowing() && mpTable.isEnabled()) {
      if (((e.getKeyCode() == e.VK_ENTER && !raSelectTableModifier.space)
          || e.getKeyCode() == e.VK_SPACE) && 
          validateSelection(raQueryDataSet)) {
        e.consume();
        /*JraKeyListener.enterNow = false;*/
        if (e.getKeyCode() == e.VK_ENTER)
          AWTKeyboard.ignoreKeyRelease(AWTKeyboard.ENTER);
        selMod.toggleSelection(raQueryDataSet);
        if (!raQueryDataSet.atLast() && !mpTable_allowRowChange(
             raQueryDataSet.getRow(), raQueryDataSet.getRow() + 1))
          fireTableDataChanged();
        else if (!raQueryDataSet.next()) fireTableDataChanged();
      } else if (e.getKeyCode() == e.VK_A && e.isControlDown()) {
        e.consume();
        int row = raQueryDataSet.getRow();
        enableEvents(false);
        for (raQueryDataSet.first(); raQueryDataSet.inBounds(); raQueryDataSet.next())
          selectOrToggle(raQueryDataSet, selAllToggles);
        raQueryDataSet.goToRow(row);
        enableEvents(true);
      }
    }
  }
  public raSelectTableModifier getSelectionTracker() {
    return selMod;
  }
  public boolean validateSelection(ReadRow rowToSelect) {
    return true;
  }
  public boolean mpTable_allowRowChange(int oldrow, int newrow) {
    return true;
  }
  
  public void mpTable_rowChanged(int oldrow, int newrow, boolean toggle, boolean extend) {
    if (selMod != null && (toggle || extend)) {
      if (!extend) selectOrToggle(raQueryDataSet, true);
      else {
        int row = raQueryDataSet.getRow();
        int beg = (oldrow > newrow ? newrow : oldrow), end = oldrow + newrow - beg;
        if (beg == end && extend) return;
        enableEvents(false);
        raQueryDataSet.goToRow(beg);
        for (int i = beg; i <= end; raQueryDataSet.next(), i++)
          selectOrToggle(raQueryDataSet, toggle);
        raQueryDataSet.goToRow(row);
        enableEvents(true);
      }
    }
  }

  private void selectOrToggle(DataSet r, boolean toggle) {
    if (toggle && (selMod.isSelected(r) || validateSelection(r)))
      selMod.toggleSelection(r);
    else if (!toggle && !selMod.isSelected(r) && validateSelection(r))
      selMod.addToSelection(r);
  }

  /**

   * pogledaj dokumentaciju hr.resart.swing.JraTable2, raTableModifier, raTableUIModifier, raTableColumnModifier

   * @param m

   */

  public void removeTableModifier(hr.restart.swing.raTableModifier m) {

    mpTable.removeTableModifier(m);

  }

  /**

   * pogledaj dokumentaciju hr.resart.swing.JraTable2, raTableModifier, raTableUIModifier, raTableColumnModifier

   */

  public void removeAllTableModifiers() {

    mpTable.removeAllTableModifiers();

  }

  /**

   * pogledaj dokumentaciju hr.resart.swing.JraTable2, raTableModifier, raTableUIModifier, raTableColumnModifier

   * @return

   */

  public hr.restart.swing.raTableModifier[] getTableModifiers() {

    return mpTable.getTableModifiers();

  }

  private hr.restart.swing.raTableUIModifier tableColorModifier = new hr.restart.swing.raTableUIModifier();

  /**

   * Dodaje defaultni raTableUIModifier(no parameters) koji malo potamnjuje svaki drugi red tablice

   */

  public void addTableColorModifier() {

    removeTableModifier(tableColorModifier);

    addTableModifier(tableColorModifier);

  }



  public boolean isTableView() {

    return showedNoTablePanel == null;

  }



  public void setToggleTableEnabled(boolean enab) {

    toggleTableEnabled = enab;

  }



  public boolean isToggleTableEnabled() {

    return toggleTableEnabled;

  }



  public boolean toggleTableView() {

    if (!isToggleTableEnabled()) return false;

    if (getKeyColumns() == null) return false;

    getColumnsBean().saveSettings();

    if (isTableView()) {

      yesTableDataSet = getDataSet();

      noTableDataSet = new com.borland.dx.sql.dataset.QueryDataSet();

      noTableDataSet.setColumns(yesTableDataSet.cloneColumns());

      String nullQry = Valid.getValid().getNoWhereQuery(yesTableDataSet).concat(" WHERE 'A' = 'B'");

      Aus.setFilter(noTableDataSet, nullQry);
      
      noTableDataSet.setTableName(yesTableDataSet.getTableName());

      noTableDataSet.open();

      setDataSet(noTableDataSet,false);

      if (jpNoTablePanel == null) {

        setNoTablePanel(new raJPNoTablePanel(getDataSet(),getKeyColumns()));

        setFocusedNoTablePanelField(((raJPNoTablePanel)jpNoTablePanel).focusField);

      }

      showedNoTablePanel = jpNoTablePanel;

      jPanelTable.add(showedNoTablePanel,BorderLayout.NORTH);

      handleNoTablePanelFields();

      validateTree();

      focusedNoTablePanelField.requestFocus();

      focusedNoTablePanelField.setCaretPosition(0);

    } else {

      unHandleNoTablePanelFields();

      jPanelTable.remove(showedNoTablePanel);

      setDataSet(yesTableDataSet,false);

//      fireTableDataChanged();

      showedNoTablePanel = null;

      getMpTable().requestFocus();

      validateTree();

    }

    getNavBar().getColBean().initialize();

    return true;

  }



  public void setNoTablePanelValues(Object[] values) {

    noTablePanelValues = values;

  }



  public Object[] getNoTablePanelValues() {

    return noTablePanelValues;

  }



  public String addHackedKey(String columnKeyName) {

    if (hackedKeys == null) hackedKeys = new HashSet();

    if (hackedKeys.add(columnKeyName)) return columnKeyName;

    return null;

  }



  public boolean removeHackedKey(String columnKeyName) {

    if (hackedKeys == null) return false;

    return hackedKeys.remove(columnKeyName);

  }



  public void clearHackedKeys() {

    hackedKeys = null;

  }



  public boolean isHackedKey(String columnKeyName) {

    if (hackedKeys == null) return false;

    return hackedKeys.contains(columnKeyName);

  }



  public void findNoTablePanelValues(PreSelect pres) {

    if (getKeyColumns() == null) return;

//    if (isTableView()) return;

    String[] keys = getKeyColumns();

    hr.restart.db.raVariant rvar = null;

    Object[] values = new Object[keys.length];

    clearHackedKeys();

    for (int i = 0; i < keys.length; i++) {

      if (pres != null && pres.getSelRow().hasColumn(keys[i])!= null) {

        values[i] = rvar.getDataSetValue(pres.getSelRow(),keys[i]);

//        rvar.setDataSetValue(

//          ntPanel.getEntrySet(),

//          keys[i],

//          rvar.getDataSetValue(pres.getSelRow(),keys[i])

//        );

//        ntPanel.setProtected(keys[i],true);

      } else {

        com.borland.dx.dataset.Column hackCol = getStorageDataSet().hasColumn(keys[i]);

        Object obh;

        if ((obh = hackGodina(hackCol,pres))!=null) {

          values[i] = obh;

          addHackedKey(keys[i]);

        } else if ((obh = hackCorg(hackCol))!=null) {

          values[i] = obh;

          addHackedKey(keys[i]);

        }

      }

    }

    setNoTablePanelValues(values);

  }



  private Object hackCorg(com.borland.dx.dataset.Column col) {

    if (!(col.getColumnName().equalsIgnoreCase("CORG")

          || col.getColumnName().equalsIgnoreCase("KNJIG")

          || col.getColumnName().equalsIgnoreCase("CSKL")

          )) return null;

    if (col.getSqlType() != java.sql.Types.CHAR) return null;

    if (col.getPrecision() != 12) return null;

//    col.getDataSet().setString(col.getColumnName(),hr.restart.zapod.OrgStr.getKNJCORG(false));

    return hr.restart.zapod.OrgStr.getKNJCORG(false);

  }



  private Object hackGodina(com.borland.dx.dataset.Column col, PreSelect pres) {

    if (!col.getColumnName().toUpperCase().startsWith("GOD")) return null;

    boolean typeMatch = ((col.getSqlType() == java.sql.Types.CHAR && col.getPrecision() == 4)

        || col.getSqlType() == java.sql.Types.SMALLINT);

    if (!typeMatch) return null;

    String godina = Util.getUtil().getYear(new java.sql.Timestamp(System.currentTimeMillis()));

    if (pres != null) {

      com.borland.dx.dataset.Column[] selCols = pres.getSelRow().getColumns();

      for (int i = 0; i < selCols.length; i++) {

        if (selCols[i].getSqlType() == java.sql.Types.TIMESTAMP) {

          if (!selCols[i].getColumnName().endsWith(PreSelect.getFROMSUFIX())) {

            godina = Util.getUtil().getYear(selCols[i].getDataSet().getTimestamp(selCols[i].getColumnName()));

            break;

          }

        }

      }

    }

    if (col.getSqlType() == java.sql.Types.SMALLINT) {

      return new Short(godina);

    } else return godina;

  }



  private void handleNoTablePanelFields() {

    handleNoTablePanelFields(true);

  }



  private void handleNoTablePanelFields(boolean handl) {

    for (int i=0;i<noTablePanelDBcomponents.size(); i++) {

      Object item = noTablePanelDBcomponents.get(i);
      if (item instanceof JraTextField) {

        JraTextField jt = ((JraTextField)noTablePanelDBcomponents.get(i));

        rmvNoTablePanelFieldKeyListener(jt);

        if (handl) addNoTablePanelFieldKeyListener(jt);

      }

      if (item instanceof com.borland.dx.dataset.ColumnAware) {

        com.borland.dx.dataset.ColumnAware citem = (com.borland.dx.dataset.ColumnAware)item;

        int idx = Util.getUtil().indexOfArray(getKeyColumns(),citem.getColumnName());

        if (idx>-1) {

          try {

            if (noTablePanelValues != null && noTablePanelValues[idx] != null && handl) {

              hr.restart.db.raVariant.setDataSetValue(citem.getDataSet(),citem.getColumnName(),noTablePanelValues[idx]);

              if (getNoTablePanel() instanceof raJPNoTablePanel) {

                if (!citem.equals(getFocusedNoTablePanelField()) && checkPolicy(citem.getColumnName()))

                  ((raJPNoTablePanel)getNoTablePanel()).setProtected(citem.getColumnName(),true);

              }

            }

          }

          catch (Exception ex) {

            System.out.println("ex = "+ex);

          }

        }

      }

    }

  }



  public int getNoTablePanelProtectPolicy() {

    return noTablePanelProtectPolicy;

  }



  /**

   * Kako da enabla i disabla JraTextFieldove koji prezentiraju kljuceve za dohvat na noTablePanelu, nakon sto je

   * otkrio njihovu vrijednost iz predselekcije ili izmislio pomocu hack metoda (GOD,CORG,KNJIG,CSKL).

   * default je PROTECTALL

   * @param policy vrijednosti mogu biti

   * <pre>

   * PROTECTALL - ako je nasao na bilo koji nacin vrijednost za bilo koji kljuc odmah ga protektira na panelu

   * PROTECTFOUND - protektira samo za vrijednosti nadjene u preselectu

   * PROTECTHACKED - protektira samo za vrijednosti nadjene u preko hack metoda

   * PROTECTNONE - ne protektira nista

   * </pre>

   */

  public void setNoTablePanelProtectPolicy(int policy) {

    noTablePanelProtectPolicy = policy;

  }



  private boolean checkPolicy(String colNm) {

    if (getNoTablePanelProtectPolicy() == PROTECTALL) return true;

    if (getNoTablePanelProtectPolicy() == PROTECTNONE) return false;

    if (isHackedKey(colNm) && getNoTablePanelProtectPolicy() == PROTECTHACKED) return true;

    if (!isHackedKey(colNm) && getNoTablePanelProtectPolicy() == PROTECTFOUND) return true;

    return false;

  }



  private void unHandleNoTablePanelFields() {

    handleNoTablePanelFields(false);

  }

  private void rmvNoTablePanelFieldKeyListener(JraTextField jt) {

    try {

      EventListener[] evls = jt.getListeners(KeyListener.class);

      for (int i = 0; i < evls.length; i++) {

        if (evls[i] instanceof noTablePanelFieldKeyListener) {

          jt.removeKeyListener((KeyListener)evls[i]);

        }

      }

    }

    catch (Exception ex) {

      System.out.println("rmvKeyLis ex: "+ex);

    }

  }

  private void addNoTablePanelFieldKeyListener(JraTextField jt) {

    jt.addKeyListener(new noTablePanelFieldKeyListener());

  }



  private void noTablePanelField_keyAction(KeyEvent e) {

    if (e.getSource() instanceof JraTextField) {

      JraTextField jt = (JraTextField)e.getSource();

      jt.maskCheck();

      if (checkAllFieldsNotEmpty()) {

        e.consume();

        if (findRecord()) {

//          getNavBar().update_action();

          mpTable_doubleClicked();

        } else {

          notFoundMessage(jt);

        }

      }

    }

  }



  private void notFoundMessage(JraTextField jt) {

    jt.setErrText("Podatak sa tim klju\u010Dem nije prona\u0111en");

    jt.this_ExceptionHandling(null);

    fireTableDataChanged();

  }



  private boolean findRecord() {

    if (getStorageDataSet() instanceof com.borland.dx.sql.dataset.QueryDataSet) {

      String query = yesTableDataSet.getQuery().getQueryString();

      boolean isAnd = query.toUpperCase().indexOf(" WHERE") > 0;

      query = query.concat(getNoTablePanelQuery(isAnd));
      
      Aus.refilter(noTableDataSet, query);

      noTableDataSet.first();

      return noTableDataSet.getRowCount() > 0;

    }

    return false;

  }



  private String getNoTablePanelQuery(boolean isAnd) {

    String ret = isAnd?" AND (":" WHERE (";

    com.borland.dx.dataset.Variant variant = new com.borland.dx.dataset.Variant();

    for (int i=0;i<noTablePanelDBcomponents.size(); i++) {

      com.borland.dx.dataset.ColumnAware txCol = ((com.borland.dx.dataset.ColumnAware)noTablePanelDBcomponents.get(i));

      com.borland.dx.dataset.Column colmn = getDataSet().getColumn(txCol.getColumnName());

      txCol.getDataSet().getVariant(txCol.getColumnName(),variant);

      String colvalue = variant.toString();

      ret = ret.concat(Valid.getValid().getQuerySintax(colmn,colvalue,false));

    }

    ret = ret.substring(0,ret.length()-5);

    ret = ret.concat(")");

    return ret;

  }



  private boolean checkAllFieldsNotEmpty() {

    if (getNoTablePanel() == null) return false;

    for (Iterator i = noTablePanelDBcomponents.iterator(); i.hasNext(); ) {

      Object item = i.next();

      if (item instanceof javax.swing.text.JTextComponent) {

        if (Valid.getValid().chkIsEmpty((javax.swing.text.JTextComponent)item)) return false;

      }

    }

    return true;

  }

  public JPanel getNoTablePanel() {

    return jpNoTablePanel;

  }

  public void setNoTablePanel(JPanel jp) {

    jpNoTablePanel = jp;

    if (jpNoTablePanel == null) {

      noTablePanelDBcomponents = null;

    } else {

      fillNoTablePanelDBcomponents();

    }

  }

  private void fillNoTablePanelDBcomponents() {

    LinkedList allDBComps = Util.getUtil().getDBComps(jpNoTablePanel);

    noTablePanelDBcomponents = new LinkedList();

    for (Iterator i = allDBComps.iterator(); i.hasNext(); ) {

      com.borland.dx.dataset.ColumnAware item = (com.borland.dx.dataset.ColumnAware)i.next();

      if (Util.getUtil().containsArr(getKeyColumns(),item.getColumnName())) {

        noTablePanelDBcomponents.add(item);

      }

    }

  }

  private LinkedList noTablePanelDBcomponents = null;



  public void setFocusedNoTablePanelField(JraTextField jt) {

    focusedNoTablePanelField = jt;

  }

  public JraTextField getFocusedNoTablePanelField() {

    return focusedNoTablePanelField;

  }

/**

 * Metoda se izvrsava u EditListeneru koji je dodan DataSet-u i to nakon brisanja sloga

 */

  public void raEditListener_deleted(com.borland.dx.dataset.DataSet dc) {

  }

/**

 * Metoda se izvrsava u EditListeneru koji je dodan DataSet-u i to nakon dodavanja sloga

 */

  public void raEditListener_added(com.borland.dx.dataset.DataSet dc) {

  }

  raEditListener editListener = new raEditListener();

  class raEditListener extends com.borland.dx.dataset.EditAdapter {



    public void deleted(com.borland.dx.dataset.DataSet dc){

      raEditListener_deleted(dc);

      Zbrajalo();

    }



    public void added(com.borland.dx.dataset.DataSet dc){

        raEditListener_added(dc);

        Zbrajalo();

    }



    public void updated(com.borland.dx.dataset.DataSet dc){

        Zbrajalo();

    }


/*

//metode interfacea EditListener

    public void modifying(com.borland.dx.dataset.DataSet dc){}

    public void editError(com.borland.dx.dataset.DataSet ds,com.borland.dx.dataset.Column clm,com.borland.dx.dataset.Variant vt,com.borland.dx.dataset.DataSetException dse, com.borland.jb.util.ErrorResponse er){}

    public void addError(com.borland.dx.dataset.DataSet ds,com.borland.dx.dataset.ReadWriteRow rwr,com.borland.dx.dataset.DataSetException dse, com.borland.jb.util.ErrorResponse er){}

    public void updateError(com.borland.dx.dataset.DataSet ds,com.borland.dx.dataset.ReadWriteRow rwr,com.borland.dx.dataset.DataSetException dse, com.borland.jb.util.ErrorResponse er){}

    public void deleteError(com.borland.dx.dataset.DataSet ds,com.borland.dx.dataset.DataSetException dse, com.borland.jb.util.ErrorResponse er){}

    public void canceling(com.borland.dx.dataset.DataSet dc){}

    public void deleting(com.borland.dx.dataset.DataSet dc){}

    public void inserting(com.borland.dx.dataset.DataSet dc){}

    public void inserted(com.borland.dx.dataset.DataSet dc){}

    public void adding(com.borland.dx.dataset.DataSet dc,com.borland.dx.dataset.ReadWriteRow rwr){}

    public void updating(com.borland.dx.dataset.DataSet dc,com.borland.dx.dataset.ReadWriteRow rwr,com.borland.dx.dataset.ReadRow rw){}

*/

  }



  class noTablePanelFieldKeyListener extends KeyAdapter {

    public void keyPressed(KeyEvent e) {

      if (e.isConsumed()) return;

      if (e.getKeyCode() == e.VK_ENTER) noTablePanelField_keyAction(e);

    }

  };



}