/****license*****************************************************************
**   file: JraTable2.java
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
package hr.restart.swing;



import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.NavigationAdapter;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.startFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author

 * @version 1.0

 */



public class JraTable2 extends JTable implements JraTableInterface {
  
  public static final int SPEED_ASTERISK = 0;
  public static final int SPEED_AUTOMATIC = 1;
  public static final int SPEED_ALWAYS = 2;

  private static String dsSortType;

  dataSetTableModel tabModel;
  
  private ColumnChangeListener clisten = null;

  TableDataSetNavigationListener navListener = new TableDataSetNavigationListener(this);

  public boolean ignoreNavigation = false;

  boolean addNotifyCalled = false;

  private java.util.Hashtable modifiers = new java.util.Hashtable();

  protected int selectedCol = -1;

  java.awt.event.MouseAdapter tableHeaderMouseListener = new java.awt.event.MouseAdapter() {
    String oldcolname = null;
      public void mouseClicked(java.awt.event.MouseEvent e) {

        if (!isEnabled()) return;

        if (getDataSet() == null) return;

        int idx = getTableHeader().columnAtPoint(e.getPoint());

        String newTableSort = getDataSetColumn(idx).getColumnName();

        if (tableSort!=null && newTableSort.equals(tableSort)) {

          descendingTableSort = !descendingTableSort;

        } else descendingTableSort = false;

        tableSort = newTableSort;

        //tabModel.stopFire();

        sortDataset(tabModel.getDataSet(), tableSort, descendingTableSort);

        //tabModel.startFire();

        //tabModel.fireTableDataChanged();

        //tabModel.getDataSet().first();

        String newcolname = getColumnModel().getColumn(idx).getHeaderValue().toString();

        fireColumnChanged(oldcolname, newcolname);
        //JraTable2.this.firePropertyChange(COLNAMEPROPERTY,oldcolname,newcolname);

        oldcolname = newcolname;
        setSpeedColumn(idx);

        //tabModel.getDataSet().setSort(new SortDescriptor(new String[] {tableSort},true,descendingTableSort));

      }

  };

  java.awt.event.MouseAdapter table2ClickMouseListener = new java.awt.event.MouseAdapter() {

//      String oldcolname = null;

      public void mouseClicked(java.awt.event.MouseEvent e) {

        if (!isEnabled()) return;

        if ((e.getModifiers() & e.BUTTON1_MASK) == 0) return;

/*        int newcol = getSelectedColumn();

        if (newcol >= 0) {
          String newcolname = getColumnModel().getColumn(newcol).getHeaderValue().toString();
          JraTable2.this.firePropertyChange(COLNAMEPROPERTY,oldcolname,newcolname);
          oldcolname = newcolname;
          setSpeedColumn(newcol);
        } */

        if (e.getClickCount()==2) tableDoubleClicked();

      }

  };
  
  private static int speedType = SPEED_ASTERISK;

  private String tableSort;

  private boolean descendingTableSort = true;

  public JraTable2() {
    this(false);
  }
  boolean offline;
  public JraTable2(boolean _offline) {
    offline = _offline;
    tabModel = new dataSetTableModel();
    setModel(tabModel);
    tabModel.setTableSumRow(new raTableSumRow(this));
    table2Init();
  }

  public String getTableSortColumnName() {

    return tableSort;

  }

  public boolean isDescendingTableSort() {

    return descendingTableSort;

  }

  public static void setSpeedType(int speedType) {
    JraTable2.speedType = speedType;
  }

  /* ab.f   popravljen nacin na koji se rukuje selekcijom redova i kolona na
    tablici: umjesto MouseListenera, ovverrida se metoda changeSelection kroz
    koju interno prolazi selektiranje table misem (a koja je i namijenjena
    overridanju). Na ovaj nacin mogu znati koji red je prethodno bio selektiran,
    a mogu i iskljuciti onu funkcionalnost da Ctrl tipka selektira vise kolona
    odjednom. */

  String oldcolname = null;

  public void changeSelection(int row, int col, boolean toggle, boolean extend) {
    int oldrow = getSelectedRow();
    int oldcol = getSelectedColumn();
    
    if (!altEnabled) return;

    // ako se pokušava promijeniti redak, prvo provjeri je li navigacija dopuštena
    if (oldrow != row)
      if (!allowRowChange(oldrow, row)) return;
    // ignoriram toggle i extend: de facto tipke shift i ctrl nemaju uobicajenu
    // funkciju, a bas to mi treba.
    super.changeSelection(row, col, false, false);

    String oldc = oldcol < 0 ? null : getColumnModel().getColumn(oldcol).getHeaderValue().toString();
    String newc = col < 0 ? null : getColumnModel().getColumn(col).getHeaderValue().toString();

    if (oldcol != col)
      fireColumnChanged(oldc, newc);
//      firePropertyChange(COLNAMEPROPERTY, oldc, newc);
    setSpeedColumn(col);

    // pozivam metodu za overridanje:
    rowChanged(oldrow, row, toggle, extend);
//    fireTableDataChanged();
    repaint();

  }
  
  public boolean allowRowChange(int oldrow, int newrow) {
    return true;
  }

  public void stopFire() {

    tabModel.stopFire();

  }

  public void startFire() {

    tabModel.startFire();

  }
  
  public void addColumnChangeListener(ColumnChangeListener l) {
    clisten = l;
  }
  
  public void removeColumnChangeListener() {
    clisten = null;
  }
  
  protected void fireColumnChanged(String oldCol, String newCol) {
    if (clisten != null)
      clisten.columnChanged(this, oldCol, newCol);
  }

  public Column getDataSetColumn(int tabColIndex) {

    if (getDataSet()==null) return null;

    try {

      Column[] dsCols = getDataSet().getColumns();

      String headValue = getColumnModel().getColumn(tabColIndex).getHeaderValue().toString();

      for (int i=0;i<dsCols.length;i++) {

        if (dsCols[i].getCaption().equals(headValue)) return dsCols[i];

      }

    }

    catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;

  }
  
  public String getRealColumnName(int idx) {
    /*String capt = getModel().getColumnName(convertColumnIndexToModel(idx));
    for (int i = 0; i < getDataSet().getColumnCount(); i++)
      if (getDataSet().getColumn(i).getVisible() != 0 &&
          capt.equals(getDataSet().getColumn(i).getCaption()))
        return getDataSet().getColumn(i).getColumnName();
    return null;*/
  	return tabModel.getRealColumnName(convertColumnIndexToModel(idx));
  }
  
  public int getModelColumnIndex(String colName) {
    return tabModel.getColumnIndex(colName);
  }
  
  public String getModelColumnName(int idx) {
  	if (idx < 0 || idx >= tabModel.getColumnCount()) return null;
    return tabModel.getRealColumnName(idx);
  }

  public void table2Init() {

//    setFont(getFont().deriveFont(java.awt.Font.BOLD));

    getColumnModel().setColumnMargin(2);

    setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    /*addFocusListener(new FocusAdapter() {

      public void focusGained(FocusEvent e) {

//        if (e.getSource() != getTableHeader())

        killFocus(e);

      }

    });*/

    addAncestorListener(new javax.swing.event.AncestorListener() {

      public void ancestorAdded(javax.swing.event.AncestorEvent e) {

        tabModel.fireTableDataChanged();

        pos();

      }

      public void ancestorMoved(javax.swing.event.AncestorEvent e) {

      }

      public void ancestorRemoved(javax.swing.event.AncestorEvent e) {
        clearSpeedHistory();
      }

    });

    addMouseListener(table2ClickMouseListener);
    
    addKeyListener(new JraKeyListener(JraKeyListener.allFuncKeys));

    if (!offline ) 
      raTableCopyPopup.installFor(this);

    this.setDefaultRenderer(Object.class,new dataSetTableCellRenderer());

    this.getTableHeader().setDefaultRenderer(new HeaderTableCellRenderer());

    this.setRowHeight(ADDITIONAL_ROWHEIGHT + startFrame.getFontDelta());

  }
  public static int ADDITIONAL_ROWHEIGHT = 21;
  public void pos() {

    if (getRowCount()==0) return;

    if (getDataSet() == null) return;

    getDataSet().goToClosestRow(getDataSet().getRow());

  }

  public void fireTableDataChanged() {
    int rowSel = getDataSet().getRow();
    int colSel = getColumnModel().getSelectionModel().getMinSelectionIndex();
    clearSpeedHistory();
    tabModel.fireTableDataChanged();
    try {
      if (colSel >= 0) getColumnModel().getSelectionModel().setSelectionInterval(colSel, colSel);
    } catch (Exception e) {}
    if (rowSel >= 0 && rowSel < getDataSet().getRowCount())
      getDataSet().goToClosestRow(rowSel);
    else pos();
  }

  public void tableChanged(javax.swing.event.TableModelEvent ev) {

    super.tableChanged(ev);

    javax.swing.table.JTableHeader h = getTableHeader();

    if (h==null) return;

    h.removeMouseListener(tableHeaderMouseListener);

    h.addMouseListener(tableHeaderMouseListener);

  }

  /**

   * Overridati za event doubleclick na tablicu

   */

  public void tableDoubleClicked() {

  }



  public void setEnabled(boolean b) {

    if (b && !isEnabled()) //getDataSet().goToClosestRow(getDataSet().getRow());
      repaint();

    super.setEnabled(b);

  }



  private void printColSizes() {

    for (int i=0;i<getColumnModel().getColumnCount();i++) {

      TableColumn tabCol = getColumnModel().getColumn(i);

      System.out.print(tabCol.getHeaderValue());

      System.out.print(": W = "+tabCol.getWidth());

      System.out.print("; PW= "+tabCol.getPreferredWidth());

      System.out.print("; MW= "+tabCol.getMinWidth());

      System.out.println("; XW= "+tabCol.getMaxWidth());

    }

  }

  /** Postavlja locale na DataSet ako je instanceof StorageDataSet.

   * Nakon toga potrebno je refreshati DataSet ako je vec otvoren

   * sto ova metoda, za sada, ne radi iz optimizacijskih razloga.

   * Poziva se u JraTable2.setDataSet i u lookupData neposredno

   * poslije jedinog opena na setToSearch

   * (metoda: lookupData.raLocate(DataSet,String[],String[],int))

   * PS: Sve zakomentirano jer radi uzasno uzasavajuce bage. Najbolje napraviti vlastiiti sort dataseta

   * @param ds Po mogucnosti nesto instanceof StorageDataSet

   */

  public static void setLocaleForCollate(DataSet ds) {

    /*

    if (ds instanceof com.borland.dx.dataset.StorageDataSet) {

//      System.out.println("ds for locale: \n"+ds);

      StorageDataSet sds = (StorageDataSet)ds;

      if (sds.getLocale() == null) {

        sds.setLocale(java.util.Locale.getDefault());

//        if (sds.refreshSupported()) sds.refresh();

      }

    } //else System.out.println(ds+" \n not instance of StorageDataSet");

    */

  }



  /**

   * Sortira dataset po zadanoj koloni postujuci lokalizacijske kriterije

   * @param ds DataSet za sortiranje

   * @param columnName Kolona po kojoj se sortira

   * @param descending da li je sort obrnut (od veceg prema manjem)

   */

  public static void sortDataset(DataSet ds, String columnName, boolean descending) {

    Column colSrt = ds.hasColumn(columnName);

    if (colSrt == null) return;

    //ds.enableDataSetEvents(false);

    if (dsSortType == null) {

//      dsSortType = hr.restart.sisfun.frmParam.getParam("sisfun", "dsSortType", "sql", "sql ili mem Naèin sortiranja podataka u datasetu metodom JraTable2.sortDataset");
      dsSortType = "mem";
    }

    if (dsSortType.equals("sql") && (ds instanceof QueryDataSet) &&
        colSrt.getSqlType() == java.sql.Types.CHAR &&
        ((QueryDataSet) ds).getQuery() != null) {

      QueryDataSet qds = (QueryDataSet) ds;

      //query

      VarStr qry = new VarStr(qds.getQuery().getQueryString());

      //database

      com.borland.dx.sql.dataset.Database db = qds.getQuery().getDatabase();

      //rowID-ovi se izgube iz nekog razloga

      ArrayList rowids = null;

      if (qds.hasRowIds()) {

        rowids = new ArrayList();

        for (int i=0; i<qds.getColumnCount(); i++) {

          Column clmn = qds.getColumn(i);

          if (clmn.isRowId()) rowids.add(clmn.getColumnName());

        }

      }

      //novi query string

      int idx = qry.toString().toUpperCase().indexOf("ORDER BY");

      if (idx > 0) qry = qry.truncate(idx-1);

      String srtsx = " ORDER BY "+columnName+" "+Valid.getCollateSQL()+(descending?" DESC":"");

      qry = qry.append(srtsx);

      //postavljanje querya
      
      
      Aus.setFilter(qds, qry.toString());
      qds.setSort(null);
      qds.open();

      //vracanje rowID-a

      if (rowids != null) {

        for (Iterator i=rowids.iterator();i.hasNext();) {

          String rcn = i.next().toString();

          qds.setRowId(rcn, true);

        }

      }

    } else {

      ds.setSort(new SortDescriptor(new String[] {columnName},true,descending));

    }

    //ds.enableDataSetEvents(true);

  }

  private boolean ignoreRepaint;
  public void setIgnoreRepaint(boolean ignore) {
    ignoreRepaint = ignore;
  }
  public void repaint(int tm, int x, int y, int w, int h) {
    if (!ignoreRepaint && raRepaintManager.isSwingAllowed(this))
      super.repaint(tm, x, y, w, h);
  }
  public void paint(java.awt.Graphics g) {
    if (!ignoreRepaint && raRepaintManager.isSwingAllowed(this))
      super.paint(g);
  }

/* public void setDataSetInternal(DataSet ds) {
    DataSet oldDataSet = tabModel.getDataSet();
    if (oldDataSet!=null) oldDataSet.removeNavigationListener(navListener);
    tabModel.setDataSet(ds);
    if (ds!=null) {
      ds.addNavigationListener(navListener);
      updateTableColumns();
    }
//    if (ds != null)
//      setRowSelectionInterval(ds.getRow(), ds.getRow());
    tableSort = null;
    
    BOOK   KORICE   36 MM   PLAVO-T.
    BOOK   KORICE     3 MM   ALUMINIJ
  } */

  // ab.f


  public void setDataSet(DataSet ds) {

    clearSpeedHistory();

    DataSet oldDataSet = tabModel.getDataSet();

    if (oldDataSet!=null) navListener.uninstall(oldDataSet); 

    setVisible(false);

//    setLocaleForCollate(ds);

    tabModel.setDataSet(ds);

    if (ds!=null) {

      navListener.install(ds);

      updateTableColumns();

    }

    tableSort = null;
    updateModifiers();

    setVisible(true);

  }
  
  public void updateModifiers() {
    raTableModifier[] mods = getTableModifiers();
    for (int i = 0; i < mods.length; i++)
      mods[i].dataChanged();
  }
/*

  public void addNotify() {

    super.addNotify();

    if (addNotifyCalled) return;

    addNotifyCalled = true;

    System.out.println("addNotify...");

    setTableColumnsUI();

  }

*/

  public void doLayout() {

    if (!isShowing()) updateTableColumns();
    
    if (!isShowing()) setAlternateColor();

    super.doLayout();

  }
  
  
  protected Color alterCol = null;
  boolean altEnabled = true;
  public void setAlternateColor(boolean enable) {
    altEnabled = enable;
  }
  void setAlternateColor() {
    if (!altEnabled) return;
    String col = offline?"gray":frmParam.getParam("sisfun", "alterCol", "gray", 
        "Boja pozadine svakog drugog reda (ime ili hex)", true);
    String tone = offline?"10":frmParam.getParam("sisfun", "alterAlpha", "10",
        "Faktor zastupljenosti alterCol boje u odnosu na original (1-100)", true);
    alterCol = null;
    
    try {
      alterCol = Color.decode(col);
    } catch (Exception e) {
    }
    if (alterCol == null)      
      try {
        Field f = Color.class.getField(col.toLowerCase());
        alterCol = (Color) f.get(null);
      } catch (Exception e) {
        e.printStackTrace();
      }
     
    //System.out.println(alterCol + " " + tone);
    if (alterCol != null) {
      int t = Aus.getAnyNumber(tone);
      if (t > 0 && t <= 100)
        alterCol = halfTone(this.getBackground(), alterCol, t / 100f);
      //System.out.println(alterCol);
    }
  }
  
  private Color halfTone(Color cFrom, Color cTo, float factor) {
    return new Color((int) (cFrom.getRed() * (1 - factor) + cTo.getRed() * factor),
                     (int) (cFrom.getGreen() * (1 - factor) + cTo.getGreen() * factor),
                     (int) (cFrom.getBlue() * (1 - factor) + cTo.getBlue() * factor));
  }

  public void updateTableColumns() {

    if (tabModel==null) return;

    if (tabModel.getDataSet() !=null) {

      setTableColumnsUI();

    }

  }
  
  public void updateTableColumn(int modelCol) {
    if (tabModel==null || tabModel.getDataSet() == null) return;

    TableColumn tabCol = getColumnModel().getColumn(modelCol);

    Column dataCol = getDataSetColumn(modelCol);

    raTableColumnModifier cModifier = getTableModifierForColumn(dataCol.getColumnName());

    int width = 0;

    if (cModifier == null) {

      if (dataCol.getDataType() == Variant.BIGDECIMAL) {
        int w = dataCol.getWidth();
        if (dataCol.getScale() == 3)
          w = (w < 15) ? w : 11;
        else if (dataCol.getScale() == 2)
          w = (w < 15) ? w : 14;
        width = w * getFontWidth();
      } else
        width = dataCol.getWidth()*getFontWidth();

    } else {

      width = cModifier.getMaxModifiedTextLength()*getFontWidth();

    }

    tabCol.setMinWidth(Math.max(width / 4, 20));

    tabCol.setPreferredWidth(width);

    tabCol.setMaxWidth(width * 5 + 200);

    tabCol.setWidth(width);

  }



  public void setTableColumnsUI() {

    if (getDataSet()==null) return;

    for (int i = 0; i < getColumnCount(); i++)
      updateTableColumn(i);
    
  };



  private int getFontWidth() {

    return 9;

  }



  public void valueChanged(javax.swing.event.ListSelectionEvent e) {

    if (ignoreNavigation) return;
    
    if (!altEnabled) return;

    if (getSelectedRow() == -1) return;

    if (getSelectedRow()>=tabModel.getDataSet().getRowCount()) {

      setRowSelectionInterval(tabModel.getDataSet().getRow(),tabModel.getDataSet().getRow());

//      setColumnSelectionInterval(0,getColumnCount()-1);

    } else {

      if (tabModel.getDataSet().getRow() != getSelectedRow())
        tabModel.getDataSet().goToRow(getSelectedRow());

    }

  }



  public boolean isCellSelected(int row, int column) {

    return isRowSelected(row);

  }



  public DataSet getDataSet() {

    return tabModel.getDataSet();

  }



/**

 * <pre>

 * Handla key evente nad tablicom Up, Down, Page Up, Page Down, Home, End

 *

 * Ova tablica mice fokuse sa sebe cim uzmogne zbog estetike i overridanja bolesnih JdbTable evenata

 * pa zato treba dodati keylistener na slijedecu fokusabilnu komponentu ili window i na keyTyped pozvati ovu funkciju

 * </pre>

 *

 */

  private VarStr speed = new VarStr();

  private boolean anywhere = false;

  public void setSpeedColumn(int idx) {
    selectedCol = idx;
    clearSpeedHistory();
  }

  public void clearSpeedHistory() {
    raTypeaheadWindow.hideTip();
    speed.clear();
    anywhere = false;
  }

  private void showTypeTip() {
    Point sel = getTableHeader().getHeaderRect(selectedCol).getLocation();
    sel.translate(getTableHeader().getLocationOnScreen().x + 5,
                  getTableHeader().getLocationOnScreen().y - 5);
    raTypeaheadWindow.setTip(this, "Traži: "+speed, sel);
  }

  public Component getRenderComponent(int row, int col) {
    return getDefaultRenderer(Object.class).getTableCellRendererComponent(this,
           getValueAt(row, col), false, false, row, col);
  }

  public String getRenderedValueAt(int row, int col) {
    return ((JLabel) getRenderComponent(row, col)).getText();
  }

  private int findStringBidirect(String s, int dir) {
    int beg = getSelectedRow();
    int i = beg;
    do {
      i += dir;
      if (i == getRowCount()) i = 0;
      if (i < 0) i = getRowCount() - 1;
      String val = getRenderedValueAt(i, selectedCol).toLowerCase();
      if (anywhere && val.indexOf(s) >= 0 ||
          !anywhere && val.startsWith(s)) return i;
    } while (i != beg);
    return -1;
  }

  private int findString(String s, boolean starting) {
    int beg = getSelectedRow();
    s = s.toLowerCase();
    for (int i = beg; i < getRowCount(); i++) {
      String val = getRenderedValueAt(i, selectedCol).toLowerCase();
      if (starting && val.startsWith(s) ||
          !starting && val.indexOf(s) >= 0)
        return i;
    }
    for (int i = 0; i < beg; i++) {
      String val = getRenderedValueAt(i, selectedCol).toLowerCase();
      if (starting && val.startsWith(s) ||
          !starting && val.indexOf(s) >= 0)
        return i;
    }
    return -1;
  }

  private boolean findFirstChar(char ch) {
    if (ch == '*')
      return anywhere = true;
    if (speedType == SPEED_ALWAYS) anywhere = true;
    if (anywhere) return findNextChar(ch);
    
    int row = findString(String.valueOf(ch), true);
    if (row < 0) {
      if (speedType == SPEED_AUTOMATIC) {
        anywhere = true;
        return findNextChar(ch);
      }
      return false;
    }

    speed.append(ch);
    
    // provjeri smije li se pomaknuti aktivni slog
    if (row != getDataSet().getRow() && 
        !allowRowChange(getDataSet().getRow(), row))
      return false;
    getDataSet().goToRow(row);
    return true;
  }

  private boolean findNextChar(char ch) {
    speed.append(ch);
    int row = findString(speed.toString(), !anywhere);
    if (row < 0) speed.chop();
    else {
      // provjeri smije li se pomaknuti aktivni slog
      if (row != getDataSet().getRow() && 
          !allowRowChange(getDataSet().getRow(), row))
        return false;
      getDataSet().goToRow(row);
    }
    return row >= 0;
  }

  public void processTableKeyTyped(KeyEvent e) {
    if (!altEnabled) return;
    if (AWTKeyboard.getFocusedComponent() instanceof JTextComponent) return;
    if (isShowing() && !e.isConsumed() && getDataSet() != null && isEnabled() && selectedCol != -1) {
      char ch = e.getKeyChar();
      if (!Character.isISOControl(ch) && (ch != ' ' || speed.length() > 0)) {
        if (!raTypeaheadWindow.isShowing(this)) clearSpeedHistory();
        if (speed.length() > 0) findNextChar(ch);
        else if (!findFirstChar(ch)) return;
        showTypeTip();
        e.consume();
      }
    }
  }

  public void processTableKeyEvent(KeyEvent e) {
    if (!altEnabled) return;
    if (AWTKeyboard.getFocusedComponent() instanceof JTextComponent) return;
    if (isShowing()&&!e.isConsumed()&&(getDataSet()!=null&&isEnabled())) {
      if (speed.length() > 0 && e.getKeyCode() == e.VK_SPACE)
        e.consume();
      if (e.getModifiers()==0) {
        JComponent sc = this;
        Rectangle vis = getVisibleRect();
        if (getParent() instanceof JPanel &&
            getParent().getParent() instanceof JViewport)
          vis = (sc = (JPanel) getParent()).getVisibleRect();
        
        if (e.getKeyCode()==e.VK_UP) {
          
          if (getDataSet().atFirst()) {
            vis.y = 0;
            scrollRectToVisible(vis);
          } else if (!getDataSet().atFirst() && allowRowChange(
              getDataSet().getRow(), getDataSet().getRow() - 1))
            if (!getDataSet().prior()) getDataSet().first();

          e.consume();

        } else if (e.getKeyCode()==e.VK_DOWN)  {

          if (getDataSet().atLast()) {
            vis.y += getRowHeight();
            scrollRectToVisible(vis);
          } else if (!getDataSet().atLast() && allowRowChange(
              getDataSet().getRow(), getDataSet().getRow() + 1))
            if (!getDataSet().next()) getDataSet().last();

          e.consume();

        } else if (e.getKeyCode()==e.VK_PAGE_DOWN)  {

          if (getDataSet().atLast()) {
            vis.y += vis.height;
            scrollRectToVisible(vis);
          } else if (!getDataSet().atLast() && allowRowChange(
              getDataSet().getRow(), getDataSet().getRow() + getVisibleTableRows()))
            if (!getDataSet().goToRow(getDataSet().getRow()+getVisibleTableRows()))
              getDataSet().last();

          e.consume();

        } else if (e.getKeyCode()==e.VK_PAGE_UP)  {

          if (getDataSet().atFirst()) {
            vis.y = 0;
            scrollRectToVisible(vis);
          } else if (!getDataSet().atFirst() && allowRowChange(
              getDataSet().getRow(), getDataSet().getRow() - getVisibleTableRows()))
            if (!getDataSet().goToRow(getDataSet().getRow()-getVisibleTableRows())) 
              getDataSet().first();

          e.consume();

        } else if (e.getKeyCode()==e.VK_HOME)  {

          if (!getDataSet().atFirst() && allowRowChange(
              getDataSet().getRow(), 1))
            getDataSet().first();
          
          vis.y = 0;
          scrollRectToVisible(vis);

          e.consume();

        } else if (e.getKeyCode()==e.VK_END)  {
          
          if (!getDataSet().atLast() && allowRowChange(
              getDataSet().getRow(), getDataSet().getRowCount() - 1))
            getDataSet().last();

          vis.y = sc.getHeight() - vis.height;
          scrollRectToVisible(vis);

          e.consume();

        } else if (getAutoResizeMode() == JTable.AUTO_RESIZE_OFF && e.getKeyCode() == e.VK_LEFT) {
          int col = getColumnModel().getColumnIndexAtX(vis.x - 1);
//        System.out.println(col);
          if (col >= 0) {
            vis.x = getCellRect(getSelectedRow(), col, true).x;
            scrollRectToVisible(vis);
            getColumnModel().getSelectionModel().setSelectionInterval(col, col);
          }
        } else if (getAutoResizeMode() == JTable.AUTO_RESIZE_OFF && e.getKeyCode() == e.VK_RIGHT) {
          int col = getColumnModel().getColumnIndexAtX(vis.x + vis.width);
//        System.out.println(col);
          if (col > 0) {
            Rectangle r = getCellRect(getSelectedRow(), col, true);
            vis.x = r.x + r.width - vis.width;
            scrollRectToVisible(vis);
            getColumnModel().getSelectionModel().setSelectionInterval(col, col);
          }
        } else if (e.getKeyCode()==e.VK_BACK_SPACE && speed.length() > 0) {
          speed.chop();
          if (speed.length() == 0) clearSpeedHistory();
          else showTypeTip();
          e.consume();
        } else if (e.getKeyCode()==e.VK_DELETE && speed.length() > 0) {
          clearSpeedHistory();
          e.consume();
        }
      } else if ((e.getModifiers() & e.SHIFT_MASK) != 0 && raTypeaheadWindow.isShowing(this)) {
        if (e.getKeyCode()==e.VK_UP) {
          int row = findStringBidirect(speed.toString().toLowerCase(), -1);
          if (row >= 0 && row != getDataSet().getRow() && 
                allowRowChange(getDataSet().getRow(), row))
            getDataSet().goToRow(row);
          showTypeTip();
          e.consume();
        } else if (e.getKeyCode()==e.VK_DOWN)  {
          int row = findStringBidirect(speed.toString().toLowerCase(), 1);
          if (row >= 0 && row != getDataSet().getRow() && 
              allowRowChange(getDataSet().getRow(), row))
            getDataSet().goToRow(row);

          showTypeTip();
          e.consume();
        }
      }
    }

  }

  private int getVisibleTableRows() {
    return getParent().getSize().height/getRowHeight();
  }

/**

 * zove hr.restart.swing.JraKeyListener.focusNext(e);

 */

  public void killFocus(java.util.EventObject e) {

    hr.restart.swing.JraKeyListener.focusNext(e);

  }

  /* za overridanje */
  public void rowChanged(int oldrow, int newrow, boolean toggle, boolean extend) {
  }

  public JraTableInterface clone(javax.swing.table.TableModel model) {

    JraTable2 newTable = new JraTable2();

    newTable.setModel(model);

//System.out.println("clone:model.getColumnCount()"+model.getColumnCount());

    return newTable;

  }



  public void addTableModifier(raTableModifier m) {

    Integer key = new Integer(modifiers.size());

    modifiers.put(key,m);
    m.dataChanged();

  }

  public void removeTableModifier(raTableModifier m) {

    if (modifiers.size()==0) return;

    if (!modifiers.containsValue(m)) return;

    raTableModifier[] modarr = getTableModifiers();

    modifiers.clear();

    int idx=0;

    for (int i=0;i<modarr.length;i++) {

      if (!modarr[i].equals(m)) {

        addTableModifier(modarr[i]);

      }

    }

  }

  public void removeAllTableModifiers() {

    modifiers.clear();

  }



  public raTableColumnModifier getTableModifierForColumn(String columnName) {

    raTableModifier[] modarr = getTableModifiers();

    for (int i = 0; i < modarr.length; i++) {

      if (modarr[i] instanceof raTableColumnModifier) {

        raTableColumnModifier cmodif = (raTableColumnModifier)modarr[i];

        if (cmodif.getModifiedColumn().equals(columnName)) return cmodif;

      }

    }

    return null;

  }



  public raTableModifier[] getTableModifiers() {

    raTableModifier[] modifArr = new raTableModifier[modifiers.size()];

    for (int i=0;i<modifiers.size();i++) {

      modifArr[i] = (raTableModifier)modifiers.get(new Integer(i));

    }

    return modifArr;

  }
  
  public raSelectTableModifier hasSelectionTrackerInstalled() {
    for (int i = 0; i < modifiers.size(); i++)
      if (modifiers.get(new Integer(i)) instanceof raSelectTableModifier)
        return (raSelectTableModifier) modifiers.get(new Integer(i));

    return null;
  }

  // ab.f pokazuje sumrow, ako moze
  public boolean showSumRow() {
    int row = getSelectedRow();
    if (getDataSet() != null) row = getDataSet().getRow();
    java.awt.Rectangle rect = getCellRect(row, getSelectedColumn(), true);
    rect = tabModel.getTableSumRow().getRectToVisible(this,rect);
    if (rect == null) return false;
    scrollRectToVisible(rect);
//    if (getDataSet() instanceof com.borland.dx.sql.dataset.QueryDataSet) {
//  System.out.println(rect+ " " + row);
//
//}
      return true;
  }

  class TableDataSetNavigationListener extends NavigationAdapter {

    JraTableInterface table;

    javax.swing.JTable t;

    public TableDataSetNavigationListener(JraTableInterface tab) {

      table = tab;

    }

    public void navigated(DataSet dataSet) {

      if (tabModel.stopFire) return;

      int row = dataSet.getRow();

      if(!ignoreNavigation) {

          ignoreNavigation = true;
//          if (((JraTable2) table).getDataSet() instanceof com.borland.dx.sql.dataset.QueryDataSet) {
//            new Throwable().printStackTrace(); }
         /*   try {
              Thread.sleep(2000);
            } catch (Exception ex) {}

          }*/
          try {
  
            if(table.getRowCount() > row) table.setRowSelectionInterval(row, row);
  
            if (((JraTable2) table).showSumRow()) table.repaint();
  
  //          java.awt.Rectangle rect = table.getCellRect(table.getSelectedRow(), table.getSelectedColumn(), true);
  //
  //          table.scrollRectToVisible(tabModel.getTableSumRow().getRectToVisible((JTable)table,rect));
  //
            table.repaint();
          } finally {
              ignoreNavigation = false;
          }

      }

    }

  }

  static javax.swing.border.Border emptyBorder =
      BorderFactory.createEmptyBorder(0, 2, 0, 2);

  class HeaderTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
    public HeaderTableCellRenderer() {
      setHorizontalAlignment(JLabel.CENTER);
    }
      public Component getTableCellRendererComponent(JTable table, Object value,
                         boolean isSelected, boolean hasFocus, int row, int column) {
          if (table != null) {
              JTableHeader header = table.getTableHeader();
              if (header != null) {
                  setForeground(header.getForeground());
                  setBackground(header.getBackground());
                  setFont(header.getFont());
              }
                }

                setText((value == null) ? "" : value.toString());
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    try {
        if (getPreferredSize().width > table.getCellRect(row, column, false).width)
          setToolTipText(getText());
        else setToolTipText(null);
      } catch (Exception e) {
        setToolTipText(null);
      }
          return this;
            }
	}
  
  public boolean shouldDrawCell(int row, int column) {
    return true;
  }

  class dataSetTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {


    public java.awt.Component getTableCellRendererComponent(

        JTable table,

        Object value,

        boolean isSelected,

        boolean hasFocus,

        int row,int column) {


      java.awt.Component this_component = this;

      if (table.getModel() instanceof dataSetTableModel) {

        if (!shouldDrawCell(row, column)) {
          setBackground(table.getParent().getBackground());
          setForeground(table.getForeground());
          setText("");
          return this;
        }

        com.borland.dx.dataset.Column dsCol = getDataSetColumn(column);        

//format

        com.borland.dx.text.VariantFormatter formater = 
          dsCol == null ? null : dsCol.getFormatter();

        if (formater != null && value != null) {

          Variant v1 = new Variant();

          if (value instanceof java.math.BigDecimal) {

            v1.setAsObject(value,v1.BIGDECIMAL);

            if (formater.getVariantType() != v1.BIGDECIMAL)

              formater = new com.borland.dx.text.BigDecimalFormatter(((java.math.BigDecimal)value).scale());

          } else {

            v1.setAsObject(value,formater.getVariantType());

          }

          try {

            value = formater.format(v1);

          }

          catch (Exception ex) {

            value = "";

          }

        }

        if (value == null) {

          this.setText("");

        } else {

          this.setText(value.toString());

        }

//alignment
        if (dsCol != null) {
          int alignment = com.borland.dbswing.DBUtilities.convertJBCLToSwingAlignment(dsCol.getAlignment(),true);
  
          // ab.f automatsko centriranje datuma i character polja do 4 znaka
          // osim ako ima modifiera
          if (getTableModifierForColumn(dsCol.getColumnName()) == null)
          if (dsCol.getDataType() == Variant.TIMESTAMP ||
              (dsCol.getDataType() == Variant.STRING && dsCol.getPrecision() < 5 &&
              dsCol.getPrecision() > 0))
            alignment = JLabel.CENTER;
  
          this.setHorizontalAlignment(alignment);
        }

        this.setFont(table.getFont());
        this.setBorder(emptyBorder);

//rowSume

        raTableSumRow summer = ((dataSetTableModel)table.getModel()).getTableSumRow();

        java.awt.Component summerComp = summer.getTableCellRendererComponent(this,table,value,isSelected,false,row,column);

        if (summerComp != null) return summerComp;

//color
        //isSelected = isSelected; 
        boolean popup = !offline && raTableCopyPopup.isPopupDisplayedFor(row, column);

        if (isSelected && altEnabled || popup) {

          setBackground(table.getSelectionBackground());

          setForeground(table.getSelectionForeground());

        } else {

          setBackground(alterCol != null && row % 2 == 1 ? alterCol : table.getBackground());

          setForeground(table.getForeground());

        }

// Applying table modifiers

        raTableModifier[] modifs = ((JraTable2)table).getTableModifiers();

        for (int i=0;i<modifs.length;i++) {

          modifs[i].setValues(table,value,isSelected,hasFocus,row,column,this_component);

          if (modifs[i].doModify()) {

            modifs[i].modify();

            this_component = modifs[i].renderComponent;

          }

        }

      }

      try {
        if (getPreferredSize().width > table.getCellRect(row, column, false).width)
          setToolTipText(getText());
        else setToolTipText(null);
      } catch (Exception e) {
        setToolTipText(null);
      }
      return this;

    }

    private String getStringAl(int al) {

      javax.swing.SwingConstants c=null;

      if (al==c.CENTER) return "CENTER";

      if (al==c.EAST) return "EAST";

      if (al==c.LEADING) return "LEADING";

      if (al==c.LEFT) return "LEFT";

      if (al==c.NORTH) return "NORTH";

      if (al==c.RIGHT) return "RIGHT";

      if (al==c.TRAILING) return "TRAILING";

      if (al==c.WEST) return "WEST";

      return "UNKNOWN";

    }

  }

//<test>

  public static void main(String[] args) {

    System.out.println(java.util.Locale.getDefault());

    java.util.TreeSet set = new java.util.TreeSet(java.text.Collator.getInstance());

    set.add("A");

    set.add("C");

    set.add("È");

    set.add("Æ");

    set.add("D");

    set.add("T");

    set.add("V");

    set.add("Z");

    set.add("Ž");

    set.add("E");

    set.add("Ð");

    set.add("P");

    set.add("R");

    set.add("S");

    set.add("Š");

    for (java.util.Iterator i = set.iterator();i.hasNext();) {

      System.out.println(":= "+i.next());

    }

  }

//</test>

}