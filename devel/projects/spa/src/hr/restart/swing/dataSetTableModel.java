/****license*****************************************************************
**   file: dataSetTableModel.java
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

import javax.swing.table.AbstractTableModel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class dataSetTableModel extends AbstractTableModel {
//sysoutTEST ST = new sysoutTEST(false);
  private DataSet dataSet;
  private Column[] cols;
  boolean stopFire = false;
  raTableSumRow summer;

  public dataSetTableModel() {
  }
  public void setTableSumRow(raTableSumRow _summer) {
    summer = _summer;
  }
  public void setDataSet(DataSet ds) {
    dataSet = ds;
    if (dataSet != null) {
      init();
    } else {
      cols=new Column[0];
      fireTableStructureChanged();
    }
  }

  public DataSet getDataSet() {
    return dataSet;
  }

  public Column[] getCols() {
    return cols;
  }

  void init() {
    Column[] realCols = dataSet.getColumns();
    int rmvc = 0;
    for (int i=0;i<realCols.length;i++) {
      if (realCols[i].getVisible() == com.borland.jb.util.TriStateProperty.FALSE) rmvc++;
    }
    cols = new Column[realCols.length-rmvc];
    int idx=0;
    for (int i=0;i<realCols.length;i++) {
      if (realCols[i].getVisible() != com.borland.jb.util.TriStateProperty.FALSE) {
        cols[idx] = realCols[i];
        idx++;
      }
    }
    fireTableStructureChanged();
  }

  public int getColumnCount() {
    if (cols == null) {
      return 1;
    }
    return cols.length;
  }
/*
  public Column getDataSetColumn(int i) {
    return cols[i];
  }
*/
  public Object getValueAt(int row, int column) {
    Variant v1 = new Variant();
    dataSet.getVariant(cols[column].getColumnName(),row,v1);
    return summer.getValueAt(v1.getAsObject(),row,column);
  }
  
  public int getColumnIndex(String colName) {
    for (int i = 0; i < cols.length; i++)
      if (cols[i].getColumnName().equalsIgnoreCase(colName))
        return i;
    return -1;
  }

  public int getRowCount() {
    if (dataSet!=null) {
//      dataSet.open();
      if (!dataSet.isOpen()) return 0;
//      return dataSet.getRowCount();
      return summer.getRowCount(dataSet.getRowCount());
    } else if (summer == null) return 0;
    return summer.getRowCount(0);
  }

  public String getColumnName(int i) {
    try {
      return cols[i].getCaption();
    } catch (Exception e) {
//      e.printStackTrace();
      return "";
    }
  }
  
  public String getRealColumnName(int i) {
    try {
      return cols[i].getColumnName();
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }
  
  public void stopFire() {
    stopFire = true;
  }
  public boolean isFireStopped() {
    return stopFire;
  }
  public void startFire() {
    stopFire = false;
  }
////
private void debugPrint() {
//  if (getDataSet()==null) return;
//  if (getDataSet().getTableName() == null) return;
//  System.out.println("fireEvent: dataSet = "+getDataSet().getTableName());
}
  public void fireTableCellUpdated(int row, int column) {
    if (stopFire) return;
  debugPrint();
    super.fireTableCellUpdated(row,column);
  }

  public void fireTableChanged(javax.swing.event.TableModelEvent e) {
    if (stopFire) return;
  debugPrint();
    super.fireTableChanged(e);
  }

  public void fireTableDataChanged()  {
    if (stopFire) return;
  debugPrint();
    super.fireTableDataChanged();
  }

  public void fireTableRowsDeleted(int firstRow, int lastRow) {
    if (stopFire) return;
  debugPrint();
    super.fireTableRowsDeleted(firstRow,lastRow);
  }
  public void fireTableRowsInserted(int firstRow, int lastRow) {
    if (stopFire) return;
  debugPrint();
    super.fireTableRowsInserted(firstRow,lastRow);
  }
  public void fireTableRowsUpdated(int firstRow, int lastRow) {
    if (stopFire) return;
  debugPrint();
    super.fireTableRowsUpdated(firstRow,lastRow);
  }

  public void fireTableStructureChanged() {
    if (stopFire) return;
  debugPrint();
    super.fireTableStructureChanged();
  }

////
  public raTableSumRow getTableSumRow() {
    return summer;
  }
}