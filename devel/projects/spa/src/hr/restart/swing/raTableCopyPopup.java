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

import hr.restart.help.raLiteBrowser;
import hr.restart.util.Aus;
import hr.restart.util.raDataFilter;
import hr.restart.util.columnsbean.ColumnsBean;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.Variant;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raTableCopyPopup extends JPopupMenu {

  private JraTable2 jt = null;
  private int selRow, selCol, memRow = -99, memCol = -99;
  private JTextField tx = new JTextField();
  static raTableCopyPopup inst = new raTableCopyPopup();
  private Action add, addAll, set, setAll, sub, subAll, reset, 
      fastAdd, filtShow, filtEq, filtNeq, filtRemove, search, searchAll;
  private JMenu calcMenu;
  
  raCalculator calc = raCalculator.getInstance();
  
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
      calcMenu.setEnabled(num);
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
      reset.setEnabled(calc.data.getBigDecimal("RESULT").signum() != 0);
      filtShow.setEnabled(jt instanceof raExtendedTable && jt.getDataSet() != null);
      filtEq.setEnabled(jt instanceof raExtendedTable && jt.getDataSet() != null);
      filtNeq.setEnabled(jt instanceof raExtendedTable && jt.getDataSet() != null);
      filtRemove.setEnabled(jt instanceof raExtendedTable && 
          jt.getDataSet() != null && jt.getDataSet().getRowFilterListener() != null);
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
    calcMenu.add(setAll = new AbstractAction("Naput zbroj oznaèenog isjeèka kolone") {
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
        if (!calc.isShowing()) calc.show();
        calc.setState(Frame.NORMAL);
        calc.toFront();
      }
    });
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
      stm.clearSelection();
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
      stm.clearSelection();
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
    if (!calc.isShowing()) calc.show();
    calc.setState(Frame.NORMAL);
  }
  
  void setAllValue() {
    try {
      calc.data.setBigDecimal("RESULT", getSum());
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!calc.isShowing()) calc.show();
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
    ColumnsBean cb = ((raExtendedTable) jt).owner.getColumnsBean();
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
    ColumnsBean cb = ((raExtendedTable) jt).owner.getColumnsBean();
    RowFilterListener filter = jt.getDataSet().getRowFilterListener();
    if (filter != null) jt.getDataSet().removeRowFilterListener(filter);
    jt.getDataSet().setSort(jt.getDataSet().getSort());
    updateFilterChange();
  }
}