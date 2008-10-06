/****license*****************************************************************
**   file: raTableSumRow.java
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



import hr.restart.util.Aus;

import java.awt.Rectangle;
import java.math.BigDecimal;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;



public class raTableSumRow {

  private final BigDecimal nula = Aus.zero0;

  private boolean userEnabledSumming = false;

  private int rowCount;

  private javax.swing.table.TableModel model;

  private JTable table;

  private boolean sumingEnabled = false;

  private HashSet sumCols;
  private CustomSummer cs = null;


  public raTableSumRow(TableModel _model) {

    this();

    setModel(_model);

  }

  public raTableSumRow(JTable _table) {

    this();

    setTable(_table);

  }

  public raTableSumRow() {

    sumCols = new HashSet();

  }

  public void setCustomSummer(CustomSummer custom) {
    cs = custom;
  }

  public boolean addSumCol(String colName) {

    if (!userEnabledSumming) sumingEnabled = true;

    boolean ret = sumCols.add(colName.toUpperCase());

    if (table != null && table.isShowing()) table.repaint();

    return ret;

  }

  

  public HashSet getColsForSum() {

    return sumCols;

  }



  public boolean removeSumcol(String colName) {

    boolean b = sumCols.remove(colName);

    if (!userEnabledSumming && sumCols.size() == 0) sumingEnabled = false;

    if (table != null && table.isShowing()) table.repaint();

    return b;

  }



  private boolean isSumCol(int col) {

    if (table == null) {

      return true;

    }

    try {

      String colName = getTableColumnName(col);

      boolean issum = sumCols.contains(colName.toUpperCase());

      return issum;

    }

    catch (Exception ex) {

      return false;

    }

//    return true;//test

  }

  private String getTableColumnName(int idx) {

    if (model instanceof dataSetTableModel) {

      dataSetTableModel dsModel = (dataSetTableModel)model;

      return dsModel.getCols()[idx].getColumnName();

    }

    return model.getColumnName(idx);

  }

  private BigDecimal getSum(int col) {

    if (!isSumCol(col)) {

      return null;

    }
    
    if (cs != null) {
      BigDecimal sum = cs.getSum(getTableColumnName(col));
      if (sum != null) return sum;
    }

    BigDecimal sum = new BigDecimal(0) {
      public String toString() {
        return "";
      }
    };

    for (int i = 0; i < rowCount; i++) {

      try {

        sum = sum.add(new BigDecimal(model.getValueAt(i,col).toString()));

      }

      catch (Exception ex) {

//        System.out.println("getSum("+col+").ex = "+ex);

//        return "";

      }

    }

    return sum;

  }



  public Object getValueAt(Object valueAt, int rowIndex, int columnIndex) {

    if (isSumingEnabled() && (rowIndex == (rowCount))) {

      return getSum(columnIndex);

    }

    return valueAt;

  }



  public int getRowCount(int _rowCount) {

    rowCount = _rowCount;

    return isSumingEnabled()?rowCount+1:rowCount;

  }



  public void setModel(javax.swing.table.TableModel _model) {

    model = _model;

  }



  public void setTable(JTable _table) {

    table = _table;

    setModel(table.getModel());

  }

  public javax.swing.table.TableModel getModel() {

    return model;

  }



  public void setSumingEnabled(boolean _sumingEnabled) {

    userEnabledSumming = true;

    sumingEnabled = _sumingEnabled;

    if (model != null && model instanceof AbstractTableModel)

      ((AbstractTableModel)model).fireTableDataChanged();

  }



  public void clearUserEnabledSumming() {

    userEnabledSumming = false;

  }



  public boolean isSumingEnabled() {

    return sumingEnabled;

  }

//renderer

  public java.awt.Component getTableCellRendererComponent(

    javax.swing.JLabel comp,

    javax.swing.JTable table,

    Object value,

    boolean isSelected,

    boolean hasFocus,

        int row,int column) {



    if (comp.getComponentCount() > 0) comp.remove(comp.getComponent(0));



    if (row != rowCount) {

      return null;

    }



    addLine(comp, table, row, column);



    comp.setBackground(table.getTableHeader().getBackground());

    comp.setForeground(table.getTableHeader().getForeground());

    comp.setFont(comp.getFont().deriveFont(java.awt.Font.BOLD));

    return comp;

  }



  private void addLine(JLabel comp, JTable table, int row, int column) {

    java.awt.Component line = new java.awt.Component() {

      public void paint(java.awt.Graphics g) {

        g.drawLine(0,1,getWidth(),1);

      }

    };

    line.setBounds(0,0,table.getCellRect(row,column,true).width,2);

    //System.out.println("bounds = "+line.getBounds());

    //    System.out.println("compscount = "+comp.getComponentCount());

    comp.add(line,java.awt.BorderLayout.NORTH);

  }

//visibleRectangle

  public Rectangle getRectToVisible(javax.swing.JTable table, Rectangle rect) {

    if (!sumingEnabled) return rect;
    int visibleRowCount = table.getParent().getSize().height/table.getRowHeight();
    int restRowCount;
    try {
      restRowCount = table.getRowCount() - ((JraTable2) table).getDataSet().getRow();
    } catch (Exception e) {
      restRowCount  = table.getRowCount() - table.getSelectedRow();
    }
    if (visibleRowCount >= restRowCount && restRowCount <= 2)
      return table.getCellRect(table.getRowCount() - 1, table.getSelectedColumn(), true);

    return visibleRowCount > 0 ? rect : null;

  }
  
  public static abstract class CustomSummer {
    public abstract BigDecimal getSum(String col);
  }
}
