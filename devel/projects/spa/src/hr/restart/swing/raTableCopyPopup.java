/****license*****************************************************************
**   file: raTableCopyPopup.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.help.raLiteBrowser;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raPilot;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.IntParam;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.raDataFilter;
import hr.restart.util.raGlob;
import hr.restart.util.columnsbean.ColumnsBean;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raTableCopyPopup extends JPopupMenu {

  protected JraTable2 jt = null;
  protected int selRow, selCol, memRow = -99, memCol = -99;
  private JTextField tx = new JTextField();
  static raTableCopyPopup inst = new raTableCopyPopup();
  private Action add, addAll, set, setAll, sub, subAll, reset,
      selClear, selAll, selectCol, fastAdd, filtShow, filtEq, filtNeq, 
      filtRemove, search, searchAll, tabCond, keyCond, inCond, 
      inColCond, copyAll, clearAll, replaceAll, memorize, compare, dups;
  private JMenu calcMenu;
  private JMenu adminMenu;
  
  private Map memo = new HashMap();
  
  
  raCalculator calc = raCalculator.getInstance();
  ReplaceDialog repDlg = new ReplaceDialog();
  
  static {
    AWTKeyboard.registerKeyStroke(AWTKeyboard.ESC, new KeyAction() {
      public boolean actionPerformed() {
        if (!inst.isVisible()) return false;
        inst.setVisible(false);
        return true;
      }
    });
  }

  public static void installFor(JraTable2 jt) {
    jt.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        inst.checkPopup(e);
      }
      public void mousePressed(MouseEvent e) {
        inst.checkPopup(e);
      }
      public void mouseReleased(MouseEvent e) {
        inst.checkPopup(e);
      }
    });
  }

  public static void hideInstance() {
    inst.setVisible(false);
  }
  
  public void setVisible(boolean vis) {
    super.setVisible(vis);
    if (!vis) {
      memRow = memCol = -99;
      if (jt != null && selRow >= 0 && selCol >= 0) {
        jt.repaint(jt.getCellRect(selRow, selCol, true));
      }
    }
  }
  
  public static boolean isPopupDisplayedFor(int row, int col) {
    return row == inst.memRow && col == inst.memCol;
  }

  public void checkPopup(MouseEvent e) {
    if (e.isPopupTrigger() && e.getSource() instanceof JraTable2) {
      if (jt != null && (selRow = memRow) >= 0 && (selCol = memCol) >= 0) {
        memRow = memCol = -99;
        jt.repaint(jt.getCellRect(selRow, selCol, true));
      }
      jt = (JraTable2) e.getSource();
      selRow = memRow = jt.rowAtPoint(e.getPoint());
      selCol = memCol = jt.columnAtPoint(e.getPoint());
      boolean num = (selRow >= 0 && selRow < jt.getRowCount() &&
          selCol >= 0 && selCol < jt.getColumnCount() && 
          jt.getValueAt(selRow, selCol) instanceof Number);
      raSelectTableModifier stm = jt.hasSelectionTrackerInstalled();
      boolean selMulti = (stm != null && stm.countSelected() > 1);
      boolean multi = (selRow != jt.getSelectedRow()) || selMulti;
      boolean extend = jt instanceof raExtendedTable;
      boolean dataset = jt.getDataSet() != null;
      boolean view = (jt.getTopLevelAncestor() instanceof frmTableDataView) &&
      							((frmTableDataView) jt.getTopLevelAncestor()).isEditable(); 
      boolean ed = extend && dataset && view;
      boolean admin = raUser.getInstance().isSuper() || view;
      boolean selectable = jt.getRowSelectionAllowed();
      calcMenu.setEnabled(num);
      adminMenu.setEnabled(admin && extend && dataset && selectable);
      add.setEnabled(num);
      fastAdd.setEnabled(num);
      addAll.setEnabled(num && multi);
      addAll.putValue(Action.NAME, selMulti ? 
          "Dodaj sve oznaèene vrijednosti u koloni" :
          "Dodaj oznaèeni isjeèak kolone");
      sub.setEnabled(num);
      subAll.setEnabled(num && multi);
      subAll.putValue(Action.NAME, selMulti ? 
          "Oduzmi sve oznaèene vrijednosti u koloni" :
          "Oduzmi oznaèeni isjeèak kolone");
      set.setEnabled(num);
      setAll.setEnabled(num && multi);
      setAll.putValue(Action.NAME, selMulti ? 
          "Napuni zbroj oznaèenih vrijednosti u koloni" :
          "Napuni zbroj oznaèenog isjeèka kolone");
      
      selClear.setEnabled(stm != null && stm.countSelected() > 0);
      selAll.setEnabled(stm != null);
      selectCol.setEnabled(stm != null && selRow != jt.getSelectedRow());
      keyCond.setEnabled(jt.getRowCount() > 0);
      inCond.setEnabled(jt.getRowCount() > 1);
      inColCond.setEnabled(multi && jt.getRowCount() > 1);
      inColCond.putValue(Action.NAME, selMulti ?
          "Generiraj upit za oznaèene vrijednosti kolone" :
          "Generiraj upit za isjeèak kolone");
      copyAll.setEnabled(ed && jt.getRowCount() > 1);
      copyAll.putValue(Action.NAME, selMulti ?
          "Kopiraj vrijednost u sve oznaèene redove" :
          "Kopiraj vrijednost u sve redove");
      clearAll.setEnabled(ed && jt.getRowCount() > 0);
      clearAll.putValue(Action.NAME, selMulti ?
          "Poništi vrijednosti u svim oznaèenim redovima" :
          "Poništi vrijednosti u svim redovima");
      replaceAll.setEnabled(ed && jt.getRowCount() > 0 && 
          jt.getDataSet().getColumn(jt.getRealColumnName(selCol)).
          getDataType() == Variant.STRING);
      replaceAll.putValue(Action.NAME, selMulti ?
          "Zamijeni uzorak teksta u svim oznaèenim redovima" :
          "Zamijeni uzorak teksta u svim redovima");
      memorize.setEnabled(jt.getRowCount() > 0);
      compare.setEnabled(memo.size() > 0);
      dups.setEnabled(jt.getRowCount() > 0);

      reset.setEnabled(calc.data.getBigDecimal("RESULT").signum() != 0);
      filtShow.setEnabled(extend && dataset && selectable);
      filtEq.setEnabled(extend && dataset && selectable);
      filtNeq.setEnabled(extend && dataset && selectable);
      filtRemove.setEnabled(extend && dataset && selectable &&
          jt.getDataSet().getRowFilterListener() != null);
      inst.jt.repaint(inst.jt.getCellRect(selRow, selCol, true));
      show(jt, e.getX(), e.getY());
    }
  }

  private raTableCopyPopup() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    add(new AbstractAction("Kopiraj tekst") {
      public void actionPerformed(ActionEvent e) {
        copyValue();
      }
    });
    addSeparator();
    add(fastAdd = new AbstractAction("Dodaj vrijednost u kalkulator") {
      public void actionPerformed(ActionEvent e) {
        addValue();
      }
    });
    add(calcMenu = new JMenu("Operacije s kalkulatorom"));
    addSeparator();
    add(filtShow = new AbstractAction("Prikaži filter") {
      public void actionPerformed(ActionEvent e) {
        showFilter();
      }
    });
    add(filtEq = new AbstractAction("Filtriraj po jednakim vrijednostima") {
      public void actionPerformed(ActionEvent e) {
        setFilter(true);
      }
    });
    add(filtNeq = new AbstractAction("Filtriraj po razlièitim vrijednostima") {
      public void actionPerformed(ActionEvent e) {
        setFilter(false);
      }
    });
    add(filtRemove = new AbstractAction("Iskljuèi postojeæi filter") {
      public void actionPerformed(ActionEvent e) {
        removeFilter();
      }
    });
    addSeparator();
    add(memorize = new AbstractAction("Zapamti sve vrijednosti u koloni") {
      public void actionPerformed(ActionEvent e) {
        memorize();
      }
    });
    add(compare = new AbstractAction("Usporedi kolonu sa zapamæenom") {
      public void actionPerformed(ActionEvent e) {
        compare();
      }
    });
    add(dups = new AbstractAction("Pronaði duplikate u koloni") {
      public void actionPerformed(ActionEvent e) {
        finddups();
      }
    });
    addSeparator();
    add(selClear  = new AbstractAction("Poništi odabir redova") {
      public void actionPerformed(ActionEvent e) {
        raSelectTableModifier stm = jt.hasSelectionTrackerInstalled();
        stm.clearSelection();
        jt.repaint();
      }
    });
    add(selAll  = new AbstractAction("Odaberi sve redove") {
      public void actionPerformed(ActionEvent e) {
        selRows(0, jt.getDataSet().getRowCount() - 1);
      }
    });
    add(selectCol  = new AbstractAction("Odaberi oznaèeni raspon redova") {
      public void actionPerformed(ActionEvent e) {
        selectCol();
      }
    });
    add(adminMenu = new JMenu("Administratorske operacije"));
    addSeparator();
    add(search = new AbstractAction("Traži na Internetu") {
      public void actionPerformed(ActionEvent e) {
        searchInternet(false);
      }
    });
    add(searchAll = new AbstractAction("Traži cijeli tekst na Internetu") {
      public void actionPerformed(ActionEvent e) {
        searchInternet(true);
      }
    });
    
    calcMenu.add(add = new AbstractAction("Dodaj oznaèenu vrijednost") {
      public void actionPerformed(ActionEvent e) {
        addValue();
      }
    });
    calcMenu.add(addAll = new AbstractAction("Dodaj oznaèeni isjeèak kolone") {
      public void actionPerformed(ActionEvent e) {
        addAllValue();
      }
    });
    calcMenu.addSeparator();
    calcMenu.add(sub = new AbstractAction("Oduzmi oznaèenu vrijednost") {
      public void actionPerformed(ActionEvent e) {
        subValue();
      }
    });
    calcMenu.add(subAll = new AbstractAction("Oduzmi oznaèeni isjeèak kolone") {
      public void actionPerformed(ActionEvent e) {
        subAllValue();
      }
    });
    calcMenu.addSeparator();
    calcMenu.add(set = new AbstractAction("Napuni oznaèenu vrijednost") {
      public void actionPerformed(ActionEvent e) {
        setValue();
      }
    });
    calcMenu.add(setAll = new AbstractAction("Napuni zbroj oznaèenog isjeèka kolone") {
      public void actionPerformed(ActionEvent e) {
        setAllValue();
      }
    });
    calcMenu.addSeparator();
    calcMenu.add(reset = new AbstractAction("Poništi vrijednost u kalkulatoru") {
      public void actionPerformed(ActionEvent e) {
        calc.data.setBigDecimal("RESULT", new BigDecimal(0));
      }
    });
    calcMenu.add(new AbstractAction("Prikaži kalkulator") {
      public void actionPerformed(ActionEvent e) {
        if (!calc.isShowing()) calc.setVisible(true);
        calc.setState(Frame.NORMAL);
        calc.toFront();
      }
    });
    adminMenu.add(tabCond = new AbstractAction("Generiraj upit za tablicu") {
      public void actionPerformed(ActionEvent e) {
        setupTabCond();
      }
    });
    adminMenu.add(keyCond = new AbstractAction("Generiraj upit za red") {
      public void actionPerformed(ActionEvent e) {
        setupKeyCond();
      }
    });
    adminMenu.add(inCond = new AbstractAction("Generiraj upit za kolonu") {
      public void actionPerformed(ActionEvent e) {
        setupInCond();
      }
    });
    adminMenu.add(inColCond = new AbstractAction("Generiraj upit za isjeèak kolone") {
      public void actionPerformed(ActionEvent e) {
        setupInColCond();
      }
    });
    adminMenu.addSeparator();
    adminMenu.add(copyAll = new AbstractAction("Kopiraj vrijednost u sve redove") {
      public void actionPerformed(ActionEvent e) {
        copyAll();
      }
    });
    adminMenu.add(clearAll = new AbstractAction("Poništi vrijednosti u svim redovima") {
      public void actionPerformed(ActionEvent e) {
        clearAll();
      }
    });
    adminMenu.add(replaceAll = new AbstractAction("Zamijeni uzorak teksta") {
      public void actionPerformed(ActionEvent e) {
        
        try {
          replaceAll();
        } catch (RuntimeException e1) {
          e1.printStackTrace();
        }
      }
    });
  }
  
  void selRows(int from, int to) {
    raSelectTableModifier stm = jt.hasSelectionTrackerInstalled();
    DataSet ds = jt.getDataSet();
    int row = ds.getRow();
    jt.stopFire();
    ds.enableDataSetEvents(false);
    ds.goToRow(from);
    for (int i = from; i <= to; ds.next(), i++)
      stm.toggleSelection(ds);
    ds.goToRow(row);
    ds.enableDataSetEvents(true);
    jt.startFire();
    jt.repaint();
  }
  
  void selectCol() {
    int b = jt.getSelectedRow();
    int e = selRow;
    if (e < b) {
      e = b;
      b = selRow;
    }
    selRows(b, e);
  }
  
  void checkPilot() {
    String p = IntParam.getTag("popup.pilot");
    if (p == null || p.length() == 0)
      IntParam.setTag("popup.pilot", p = "D");
    if (p.equals("D")) raPilot.open(true);
  }
  
  void setQuery(Condition where) {
    try {
      String tabname = "table";
      if (jt.getDataSet() instanceof QueryDataSet)
        tabname = Valid.getTableName(((QueryDataSet) 
            jt.getDataSet()).getOriginalQueryString());
      tx.setText("SELECT * FROM " + tabname + " WHERE " + where);
      tx.selectAll();
      tx.copy();
      checkPilot();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  Object getKey(int row) {
  	Object o = jt.getValueAt(row, selCol);
  	if (o == null) return null;
  	
  	if (o instanceof Number && ((Number) o).doubleValue() == 0) return null;
  	if (o instanceof String && ((String) o).length() == 0) return null;
  	
  	return o;
  }
  
  void memorize() {
  	memo.clear();
  	Integer one = new Integer(1);
  	
  	for (int i = 0; i < jt.getRowCount(); i++) {
  		Object key = getKey(i);
  		if (key == null) continue;
  		
  		Integer old = (Integer) memo.get(key);
  		memo.put(key, old == null ? one : new Integer(old.intValue() + 1));
  	}
  }
  
  void finddups() {
    Set all = new HashSet();
    Set dups = new HashSet();
    for (int i = 0; i < jt.getRowCount(); i++) {
      Object key = getKey(i);
      if (key == null) continue;
      
      if (all.contains(key)) dups.add(key);
      all.add(key);
    }
    
    if (dups.size() == 0) {
      JOptionPane.showMessageDialog(null, 
          "Nema duplikata.", "Duplikati", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    frmTableDataView old = showSet(dups);
    old.setTitle("Duplikati");
    old.pack();
    old.show();
  }
  
  void compare() {
  	Map over = new HashMap();
  	Integer one = new Integer(1);
  	int max = 0;
  	
  	for (int i = 0; i < jt.getRowCount(); i++) {
  		Object key = getKey(i);
  		if (key == null) continue;
  		
  		if (max == 0) {
  			if (key instanceof String) max = 10;
  			else if (key instanceof Date) max = 20;
  			else max = 50;
  		}
  		
  		Integer old = (Integer) memo.get(key);
  		if (old == null) {
  			old = (Integer) over.get(key);
    		over.put(key, old == null ? one : new Integer(old.intValue() + 1));
  		}	else if (old.intValue() == 1) memo.remove(key);
  		else memo.put(key, new Integer(old.intValue() - 1));
  	}
  	int total = 0;
  	for (Iterator i = memo.values().iterator(); i.hasNext(); )
  		total += ((Integer) i.next()).intValue();
  	int totalnew = 0;
  	for (Iterator i = over.values().iterator(); i.hasNext(); )
  		totalnew += ((Integer) i.next()).intValue();
  	
  	if (total + totalnew == 0) { 
  	    JOptionPane.showMessageDialog(null, "Kolone su identiène.", "Razlike", JOptionPane.INFORMATION_MESSAGE);
  		return;
  	}
  	
  	String report = "";
  	if (total > max) report = "Preostalih vrijednosti: " + total;
  	else if (total > 0) report = "Preostale vrijednosti: " + countMap(memo);
  	
  	if (totalnew > 0 && report.length() > 0) report = report + "\n\n";
  	
  	if (totalnew > max) report += "Novih vrijednosti: " + totalnew;
  	else if (totalnew > 0) report += "Nove vrijednosti: " + countMap(over);
  	
		String[] opt = {"OK", "Detalji"};
 		int ret = JOptionPane.showOptionDialog(null, 
 								new raMultiLineMessage(report, SwingConstants.LEADING, 120),
 								"Razlike", 0, JOptionPane.PLAIN_MESSAGE, null, opt, opt[0]);
 		if (ret != 1) return;

 		if (total > 0) {
 			frmTableDataView old = showMap(memo);
 			old.setTitle("Preostale vrijednosti");
 			old.pack();
 			old.show();
 			old.setLocation(old.getX() - 160, old.getY());
 		}
 		if (totalnew > 0) {
 			frmTableDataView ex = showMap(over);
 			ex.setTitle("Nove vrijednosti");
 			ex.pack();
 			ex.setLocation(ex.getX() + 240, ex.getY());
 			ex.show();
 		}
  }
  
  StorageDataSet createDataSet(Set values) {
    Column col = null;
    Object key = values.iterator().next();
    if (key instanceof BigDecimal) 
        col = dM.createBigDecimalColumn("VRI", "Vrijednost", ((BigDecimal) key).scale());
    else if (key instanceof Integer)
        col = dM.createIntColumn("VRI", "Vrijednost");
    else if (key instanceof Short)
      col = dM.createShortColumn("VRI", "Vrijednost");
    else if (key instanceof Number)
        col = dM.createBigDecimalColumn("VRI", "Vrijednost", 2);
    else if (key instanceof Date)
        col = dM.createTimestampColumn("VRI", "Vrijednost");
    else if (key instanceof String) {
        col = dM.createStringColumn("VRI", "Vrijednost", 30);
        col.setPrecision(-1);
    }   else return null;
    
    StorageDataSet ds = new StorageDataSet();
    ds.setColumns(new Column[] {col});
    ds.open();
    return ds;
  }
  
  frmTableDataView showSet(Set values) {
    StorageDataSet ds = createDataSet(values);
    Column col = ds.getColumn("VRI");
    Object key = null;
    
    try {
      Variant v = new Variant();
      for (Iterator i = values.iterator(); i.hasNext(); ) {
          key = i.next();
          ds.insertRow(false);
          v.setFromString(col.getDataType(), key.toString());
          ds.setVariant("VRI", v);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    
    frmTableDataView view = new frmTableDataView();
    view.setDataSet(ds);
    view.jp.setPreferredSize(new Dimension(360, 500));
    view.jp.getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    return view;
  }
  
  frmTableDataView showMap(Map values) {
    
  	StorageDataSet ds = createDataSet(values.keySet());
  	Column col = ds.getColumn("VRI");
    Object key = null;
  	
  	try {
	  	Variant v = new Variant();
	  	for (Iterator i = values.keySet().iterator(); i.hasNext(); ) {
	  		key = i.next();
	  		Integer cnt = (Integer) values.get(key);
	  		for (int c = 0; c < cnt.intValue(); c++) {
	  			ds.insertRow(false);
	  			v.setFromString(col.getDataType(), key.toString());
	  			ds.setVariant("VRI", v);
	  		}
	  	}
  	} catch (Exception e) {
  		e.printStackTrace();
  		return null;
  	}
  	
  	frmTableDataView view = new frmTableDataView();
    view.setDataSet(ds);
    view.jp.setPreferredSize(new Dimension(360, 500));
    view.jp.getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    return view;
  }
  
  String countMap(Map nums) {
  	VarStr ret = new VarStr();
  	for (Iterator i = nums.keySet().iterator(); i.hasNext(); ) {
  		Object key = i.next();
  		Integer cnt = (Integer) nums.get(key);
  		for (int c = 0; c < cnt.intValue(); c++)
  			ret.append(formatKey(key)).append(", ");
  	}
  	
  	return ret.chop(2).toString();
  }
  
  String formatKey(Object key) {
  	if (key instanceof BigDecimal) return Aus.formatBigDecimal((BigDecimal) key);
  	if (key instanceof Timestamp) return Aus.formatTimestamp((Timestamp) key);
  	return key.toString();
  }
  
  void setupTabCond() {
    tx.setText(((QueryDataSet) jt.getDataSet()).getOriginalQueryString());
    tx.selectAll();
    tx.copy();
    checkPilot();
  }
  
  void setupKeyCond() {
    try {
      String[] keys = ((raExtendedTable) jt).owner.getKeyColumns();
      DataRow dr = new DataRow(jt.getDataSet());
      jt.getDataSet().getDataRow(selRow, dr);
      setQuery(Condition.whereAllEqual(keys, dr));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  void setupInCond() {
    Object arr = Aus.toArray(jt.getDataSet(), jt.getRealColumnName(selCol));
    setQuery(Condition.in(jt.getRealColumnName(selCol), arr));
  }
  
  void setupInColCond() {
    raSelectTableModifier stm = jt.hasSelectionTrackerInstalled();
    if (stm == null || stm.countSelected() < 2) {
      int b = jt.getSelectedRow();
      int e = selRow;
      if (e < b) {
        e = b;
        b = selRow;
      }
      Object arr = Aus.toArray(jt.getDataSet(), jt.getRealColumnName(selCol));
      Object subarr = Array.newInstance(arr.getClass().getComponentType(), e - b + 1);
      System.arraycopy(arr, b, subarr, 0, e - b + 1);
      setQuery(Condition.in(jt.getRealColumnName(selCol), subarr));
    } else if (stm.isNatural()) {
      Integer[] sel = (Integer[]) stm.getSelection();
      Object arr = Aus.toArray(jt.getDataSet(), jt.getRealColumnName(selCol));
      Object subarr = Array.newInstance(arr.getClass().getComponentType(), sel.length);
      for (int i = 0; i < sel.length; i++)
        Array.set(subarr, i, Array.get(arr, sel[i].intValue()));
      setQuery(Condition.in(jt.getRealColumnName(selCol), subarr));
      jt.repaint();
    } else {
      DataSet ds = stm.getSelectedView();
      Object arr = Aus.toArray(ds, jt.getRealColumnName(selCol));
      setQuery(Condition.in(jt.getRealColumnName(selCol), arr));
      stm.destroySelectedView();
      jt.repaint();
    }
  }
  
  long[] generateList() {
    raSelectTableModifier stm = jt.hasSelectionTrackerInstalled();
    DataSet ds = jt.getDataSet();
    int row = ds.getRow();
    jt.stopFire();
    ds.enableDataSetEvents(false);
    if (stm != null && stm.countSelected() > 1) {
      long[] rows = new long[stm.countSelected()];
      if (stm.isNatural()) {
        Integer[] sel = (Integer[]) stm.getSelection();
        for (int i = 0; i < sel.length; i++) {
          ds.goToRow(sel[i].intValue());
          rows[i] = ds.getInternalRow();
        }
      } else {
        int i = 0;
        for (ds.first(); ds.inBounds(); ds.next())
          if (stm.isSelected(ds))
            rows[i++] = ds.getInternalRow();
      }
      ds.goToRow(row);
      return rows;
    }
    long[] rows = new long[ds.rowCount()];
    int i = 0;
    for (ds.first(); ds.inBounds(); ds.next())
      rows[i++] = ds.getInternalRow();
    ds.goToRow(row);
    return rows;
  }
  
  void copyAll() {
    long[] rows = generateList();
    String col = jt.getRealColumnName(selCol);
    DataSet ds = jt.getDataSet();
    int row = ds.getRow();
    Variant v = new Variant();
    ds.getVariant(col, selRow, v);
    for (int i = 0; i < rows.length; i++) {
      ds.goToInternalRow(rows[i]);
      ds.setVariant(col, v);
      ds.post();
    }
    ds.goToRow(row);
    ds.enableDataSetEvents(true);
    jt.startFire();
    jt.fireTableDataChanged();
  }
  
  void clearAll() {
    long[] rows = generateList();
    String col = jt.getRealColumnName(selCol);
    DataSet ds = jt.getDataSet();
    int row = ds.getRow();
    for (int i = 0; i < rows.length; i++) {
      ds.goToInternalRow(rows[i]);
      ds.setAssignedNull(col);
      ds.post();
    }
    ds.goToRow(row);
    ds.enableDataSetEvents(true);
    jt.startFire();
    jt.fireTableDataChanged();
  }
  
  void replaceAll() {
    if (repDlg.show(jt.getTopLevelAncestor(), 
        repDlg.pan, "Zamijeni uzorak teksta")) {
      raGlob glob = new raGlob(repDlg.orig.getText());
      VarStr buf = new VarStr();
      String repl = repDlg.repl.getText();
      long[] rows = generateList();
      String col = jt.getRealColumnName(selCol);
      DataSet ds = jt.getDataSet();
      int row = ds.getRow();
      for (int i = 0; i < rows.length; i++) {
        ds.goToInternalRow(rows[i]);
        if (glob.matches(ds.getString(col))) {
          buf.clear().append(glob.morphLastMatch(repl));
          replaceFields(buf, ds);
          ds.setString(col, buf.toString());
          ds.post();
        }
      }
      ds.goToRow(row);
      ds.enableDataSetEvents(true);
      jt.startFire();
      jt.fireTableDataChanged();
    }
  }
  
  void replaceFields(VarStr buf, DataSet ds) {
    int left = 0, right = 0;
    
    while ((left = buf.indexOf('{', left)) >= 0) {
      String col = buf.mid(left + 1, right = buf.indexOf('}'));
      if (ds.hasColumn(col) != null) {
        String repl = ds.format(col);
        buf.replace(left, right + 1, repl);
        left += repl.length();
      }
    }
  }
  
  private BigDecimal getValueAtRow(int row) {
    Object o = jt.getValueAt(row, selCol);
    if (o instanceof BigDecimal) return (BigDecimal) o;
    return new BigDecimal(((Number) o).doubleValue());
  }
  
  private BigDecimal getSum() {
    BigDecimal sum = new BigDecimal(0);
    
    raSelectTableModifier stm = jt.hasSelectionTrackerInstalled();
    if (stm == null || stm.countSelected() < 2) {
      int b = jt.getSelectedRow();
      int e = selRow;
      if (e < b) {
        e = b;
        b = selRow;
      }
      for (int i = b; i <= e; i++)
        sum = sum.add(getValueAtRow(i));
    } else if (stm.isNatural()) {
      Integer[] sel = (Integer[]) stm.getSelection();
      for (int i = 0; i < sel.length; i++)
        sum = sum.add(getValueAtRow(sel[i].intValue()));
      jt.repaint();
    } else {
      DataSet ds = stm.getSelectedView();
      String cname = jt.getRealColumnName(selCol);

      Variant v = new Variant();
      for (ds.first(); ds.inBounds(); ds.next()) {
        ds.getVariant(cname, v);
        sum = sum.add(v.getAsBigDecimal());
      }
      stm.destroySelectedView();
      jt.repaint();
    }
    return sum.setScale(calc.getPrecision(), BigDecimal.ROUND_HALF_UP);
  }
  
  void addValue() {
    calc.data.setBigDecimal("RESULT", calc.data.getBigDecimal("RESULT")
        .add(getValueAtRow(selRow)).setScale(calc.getPrecision(), BigDecimal.ROUND_HALF_UP));
  }
  
  void addAllValue() {
    try {
      calc.data.setBigDecimal("RESULT", calc.data.getBigDecimal("RESULT").add(getSum()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  void subValue() {
    calc.data.setBigDecimal("RESULT", calc.data.getBigDecimal("RESULT")
        .subtract(getValueAtRow(selRow)).setScale(calc.getPrecision(), BigDecimal.ROUND_HALF_UP));
  }
  
  void subAllValue() {
    try {
      calc.data.setBigDecimal("RESULT", calc.data.getBigDecimal("RESULT").subtract(getSum()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  void setValue() {
    calc.data.setBigDecimal("RESULT", getValueAtRow(selRow).
      setScale(calc.getPrecision(), BigDecimal.ROUND_HALF_UP));
    if (!calc.isShowing()) calc.setVisible(true);
    calc.setState(Frame.NORMAL);
  }
  
  void setAllValue() {
    try {
      calc.data.setBigDecimal("RESULT", getSum());
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!calc.isShowing()) calc.setVisible(true);
    calc.setState(Frame.NORMAL);
  }

  void copyValue() {
    if (selRow >= 0 && selRow < jt.getRowCount() &&
        selCol >= 0 && selCol < jt.getColumnCount()) {
      Component c = jt.getDefaultRenderer(Object.class).getTableCellRendererComponent(
          jt, jt.getValueAt(selRow, selCol), false, false, selRow, selCol);
      String text = ((JLabel) c).getText();
      if (raNumberMask.isFormattedDecNumber(text))
        text = raNumberMask.normalizeNumber(text);
      tx.setText(text);
      tx.selectAll();
      tx.copy();
    }
  }
  
  void searchInternet(boolean phrase) {
    try {
      String col = jt.getRealColumnName(selCol);
      Variant v = new Variant();
      jt.getDataSet().getVariant(col, selRow, v);
      String str = v.toString();
      if (phrase) str = "\"" + str + "\"";
      raLiteBrowser.openSystemBrowser(
          new URL("http://www.google.hr/search?q="+
              Aus.convertToURLFriendly(str)));
    } catch (MalformedURLException e1) {
      e1.printStackTrace();
    }
  }

  void updateFilterChange() {
    ColumnsBean cb = ((raExtendedTable) jt).owner.getColumnsBean();
    raSelectTableModifier stm = jt.hasSelectionTrackerInstalled();
    if (stm != null && stm.isNatural()) stm.clearSelection();
    jt.fireTableDataChanged();
    if (cb != null && cb.isShowing()) cb.checkFilter();
  }
  
  void showFilter() {
    if (dlgDataFilter.show(jt.getTopLevelAncestor(), jt.getDataSet(),
        jt.getRealColumnName(selCol), selRow))
      updateFilterChange();
  }
  
  void setFilter(boolean eq) {
    RowFilterListener filter = jt.getDataSet().getRowFilterListener();
    if (filter != null) jt.getDataSet().removeRowFilterListener(filter);
    String col = jt.getRealColumnName(selCol);
    DataRow dr = new DataRow(jt.getDataSet());
    jt.getDataSet().getDataRow(selRow, dr);
    raDataFilter nf = eq ? raDataFilter.equal(dr, col) 
            : raDataFilter.different(dr, col);
    if (filter instanceof raDataFilter)
      nf = ((raDataFilter) filter).and(nf);
    try {
      jt.getDataSet().addRowFilterListener(nf);
    } catch (Exception e) {
      e.printStackTrace();
    }
    jt.getDataSet().refilter();
    updateFilterChange();
  }
  
  void removeFilter() {
    RowFilterListener filter = jt.getDataSet().getRowFilterListener();
    if (filter != null) jt.getDataSet().removeRowFilterListener(filter);
    jt.getDataSet().setSort(jt.getDataSet().getSort());
    updateFilterChange();
  }
  
  class ReplaceDialog extends raOptionDialog {
    public JraPanel pan = new JraPanel(new BorderLayout());
    public JraPanel main = new JraPanel(new XYLayout(415, 75));
    public JraTextField orig = new JraTextField();
    public JraTextField repl = new JraTextField();
    
    public ReplaceDialog() {
      main.add(new JLabel("Uzorak teksta"), new XYConstraints(15, 15, -1, -1));
      main.add(orig, new XYConstraints(150, 15, 250, -1));
      main.add(new JLabel("Zamjenski uzorak"), new XYConstraints(15, 40, -1, -1));
      main.add(repl, new XYConstraints(150, 40, 250, -1));
      pan.add(main);
      pan.add(okp, BorderLayout.SOUTH);
    }
    
    protected void beforeShow() {
      try {
        orig.setText(jt.getValueAt(selRow, selCol).toString());
      } catch (NullPointerException e) {
        // nevermind
      }
      if (repl.isEmpty()) repl.setText(orig.getText());
    }
    
    protected boolean checkOk() {
      if (orig.getText().length() == 0) {
        orig.requestFocus();
        JOptionPane.showMessageDialog(win, "Uzorak je prazan!", 
            "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (repl.getText().length() == 0) {
        repl.requestFocus();
        JOptionPane.showMessageDialog(win, "Zamjenski uzorak je prazan!", 
            "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (orig.getText().equals(repl.getText())) {
        repl.requestFocus();
        JOptionPane.showMessageDialog(win, "Uzorci su jednaki!", 
            "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (!new raGlob(orig.getText()).compatibleWith(repl.getText())) {
        repl.requestFocus();
        JOptionPane.showMessageDialog(win, "Zamjenski uzorak je nekompatibilan izvornom!", 
            "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      return true;
    }
  }
}