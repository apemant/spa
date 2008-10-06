/****license*****************************************************************
**   file: raRowSume.java
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

import hr.restart.swing.dataSetTableModel;
import hr.restart.swing.raTableSumRow;

import javax.swing.JTable;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raRowSume {//airtsum extends JPanel {
//sysoutTEST ST = new sysoutTEST(false);
//  com.borland.dx.dataset.TableDataSet sumRow;
  Util UT = Util.getUtil();
  private hr.restart.swing.JraTableInterface dbTable=null;
//airtsum  int colX=1;
//airtsum  JToggleButton jTBSum = new JToggleButton(raImages.getImageIcon(raImages.IMGSUM));
//airtsum  rowSumeScrollChangeListener rowSumeScrollCL = new rowSumeScrollChangeListener();
//airtsum  rowSumeColumnModelListener rowSumeColumnML = new rowSumeColumnModelListener();
//airtsum  JViewport tabView;
  private String[] columns4Sum;
  public raRowSume(hr.restart.swing.JraTableInterface jraTable) {
    this();
    setDbTable(jraTable);
  }
  public raRowSume() {
//airtsum    jTBSum.addActionListener(new java.awt.event.ActionListener() {
//airtsum      public void actionPerformed(java.awt.event.ActionEvent e) {
//airtsum        show_sum();
//airtsum        ((JToggleButton)e.getSource()).setSelected(false);
//airtsum      }
//airtsum    });
//airtsum    jTBSum.addKeyListener(new hr.restart.swing.JraKeyListener(null,true));
  }

  /**
   * @param columns4SumC
   */
  public void setColumns4Sum(String[] columns4SumC) {
    columns4Sum = columns4SumC;
    //airtsum
    initColsForSum();
    //
  }
  private void initColsForSum() {
    if (columns4Sum == null) return;
    raTableSumRow tableSumRow = ((dataSetTableModel)dbTable.getModel()).getTableSumRow();
    tableSumRow.getColsForSum().clear();
    for (int i = 0; i < columns4Sum.length; i++) {
      tableSumRow.addSumCol(columns4Sum[i]);
    }
  }
  private boolean isColumn4Sum(String colNm) {
    if (columns4Sum==null) {
//    System.out.println("columns4Sum je null kaj me jebeš");
      return false;
    }
    return UT.containsArr(columns4Sum,colNm);
  }

  public java.awt.Component getComponent(int i) {
    try {
      return (java.awt.Component)((JTable)dbTable).getCellRenderer(dbTable.getRowCount()-1,i);
    }
    catch (Exception ex) {
      return null;
    }
  }

  public void show_sum() {};

  public void setDbTable(hr.restart.swing.JraTableInterface newDbTable) {
//    if (dbTable != null) dbTable.getColumnModel().removeColumnModelListener(rowSumeColumnML);
    dbTable = newDbTable;
/* airtsum
    dbTable.getColumnModel().addColumnModelListener(rowSumeColumnML);
    if (dbTable.getAutoResizeMode() == JTable.AUTO_RESIZE_OFF) trackScrollChangesOn();
*/
  }
/* airtsum
  private void trackScrollChangesOn() {
    tabView = (JViewport)dbTable.getParent();
    tabView.removeChangeListener(rowSumeScrollCL);
    tabView.addChangeListener(rowSumeScrollCL);
  }
*/
  public hr.restart.swing.JraTableInterface getDbTable() {
    return dbTable;
  }

  public void removeSum() {
    ((dataSetTableModel)dbTable.getModel()).getTableSumRow().setSumingEnabled(false);
  }

  public void initialize() {
    if (dbTable==null) return;
    ((dataSetTableModel)dbTable.getModel()).getTableSumRow().setSumingEnabled(true);

/* airtsum
    if (dbTable==null) return;
    removeAll();
    if (dbTable.getDataSet() == null) return;
    this.setLayout(null);
//    this.setBackground(dbTable.getBackground());
    this.setPreferredSize(new Dimension(dbTable.getSize().width,dbTable.getRowHeight()));
    sumRow = new com.borland.dx.dataset.TableDataSet();
    sumRow.setColumns(UT.cloneCols(dbTable.getDataSet().getColumns()));
    addColsToRow();
*/
  }

/*  private com.borland.dx.dataset.Column[] cloneCols(com.borland.dx.dataset.Column[] cols) {
    com.borland.dx.dataset.Column[] clonedCols = new com.borland.dx.dataset.Column[cols.length];
    for (int i=0;i<cols.length;i++) {
      clonedCols[i] = (com.borland.dx.dataset.Column)cols[i].clone();
      clonedCols[i].setDefaultValue(com.borland.dx.dataset.Variant.nullVariant);
    }
    return clonedCols;
  }*/
//  private void addColChangeListener(com.borland.dx.dataset.Column col) {
/*      try {
        col.addColumnChangeListener(new com.borland.dx.dataset.ColumnChangeListener() {
          public void validate(com.borland.dx.dataset.DataSet ds, com.borland.dx.dataset.Column cl, com.borland.dx.dataset.Variant vv) {
          }
          public void changed(com.borland.dx.dataset.DataSet ds, com.borland.dx.dataset.Column cl, com.borland.dx.dataset.Variant vv) {
            if (vv.getAsBigDecimal().compareTo(new java.math.BigDecimal(0))==0) {
            System.out.println("AAAAAAAAAAAA");
            } else {
            System.out.println(vv.getAsBigDecimal().compareTo(new java.math.BigDecimal(0)));
            }
          }
        });
      } catch (Exception e) {e.printStackTrace();}*/
//  }
/* airtsum
  private void addColsToRow() {
    colX=1;
    for (int i=0;i<dbTable.getColumnCount();i++) {
      addColumn(i);
    }
    jTBSum.setSize(20,20);
    jTBSum.setBounds(colX,0,20,20);
    this.add(jTBSum,null);
    updateColumnsData();
  }
*/

  private com.borland.dx.dataset.Column getSetCol(int idx) {
    if (dbTable.getModel() instanceof com.borland.dbswing.DBTableModel) {
      return ((com.borland.dbswing.DBTableModel)dbTable.getModel()).getColumn(dbTable.convertColumnIndexToModel(idx));
    } else if (dbTable instanceof hr.restart.swing.JraTable2) {
      return ((hr.restart.swing.JraTable2)dbTable).getDataSetColumn(idx);
    } else {
      return null;
    }
  }
/* airtsum
  private javax.swing.table.TableColumn getTabCol(int idx) {
    return dbTable.getColumnModel().getColumn(idx);
  }
*/
/*
  private hr.restart.swing.JraTextField getSumCol(int idx) {
    return (hr.restart.swing.JraTextField)getComponent(idx);
  }
*/
/* airtsum
  private hr.restart.swing.JraLabel getSumCol(int idx) {
    try {
      return (hr.restart.swing.JraLabel)getComponent(idx);
    }
    catch (Exception ex) {
      return null;
    }
  }
*/
/* airtsum
  public com.borland.dx.dataset.DataSet getSumDataRow() {
    return sumRow;
  }
*/

//  private java.util.LinkedList sumColPool;
//  int poolIdx = 0;

/* airtsum
  public void removeAll() {
//    if (this.getComponentCount() > 0) sumColPool = UT.getDBComps(this);
//    poolIdx = 0;
    super.removeAll();
  }
*/

//  private JraLabel getNewSumCol() {
//    if (sumColPool == null) {
//      System.out.println("sumColPool == null returning new JraLabel");
//      return new hr.restart.swing.JraLabel();
//    }
//    if (poolIdx >= (sumColPool.size()-1)) {
//      System.out.println("poolIdx >= (sumColPool.size()-1) returning new JraLabel");
//      return new hr.restart.swing.JraLabel();
//    }
//    Object el = sumColPool.get(poolIdx);
//    if (el instanceof hr.restart.swing.JraLabel) {
//      hr.restart.swing.JraLabel scol = (hr.restart.swing.JraLabel)el;
//      scol.setDataSet(null);
//      scol.setColumnName(null);
//      System.out.println("Returning pool member "+poolIdx);
//      poolIdx++;
//      return scol;
//    } else {
//      poolIdx++;
//      return getNewSumCol();
//    }
//  }
/* airtsum
  private void addColumn(int idx) {
//    hr.restart.swing.JraTextField sumCol = new hr.restart.swing.JraTextField();
    if (getSetCol(idx) == null) return;
    hr.restart.swing.JraLabel sumCol = new JraLabel();//getNewSumCol();
    if (isColumn4Sum(getSetCol(idx).getColumnName())) {
      sumCol.setDataSet(sumRow);
      sumCol.setColumnName(getSetCol(idx).getColumnName());
    } else {
      sumCol.setText("");
    }
    Dimension dim = new Dimension(getTabCol(idx).getWidth(),this.getPreferredSize().height);
    Rectangle colBounds = new Rectangle(colX,0,getTabCol(idx).getWidth(),this.getPreferredSize().height);
//    raCommonClass.getraCommonClass().setLabelLaF(sumCol,false);
    sumCol.setOpaque(false);
    sumCol.setPreferredSize(dim);
    sumCol.setBounds(colBounds);
    sumCol.setBorder(BorderFactory.createRaisedBevelBorder());
    sumCol.setFont(sumCol.getFont().deriveFont(Font.BOLD));
    this.add(sumCol,null);
    colX=colX+getTabCol(idx).getWidth();
  }
  private void calcSize() {
    if (stopSizing) return;
    setVisible(false);
    colX=1;

    for (int i=0;i<dbTable.getColumnCount();i++) {
      try {
        getComponent(i).setSize(dbTable.getColumnModel().getColumn(i).getWidth(),this.getPreferredSize().height);
        getComponent(i).setBounds(colX,0,dbTable.getColumnModel().getColumn(i).getWidth(),this.getPreferredSize().height);
        colX=colX+dbTable.getColumnModel().getColumn(i).getWidth();
      }
      catch (Exception ex) {
        System.out.println("raRowSume.calcSize()::Exception ="+ex);
        System.out.println("dbTable.getColumnCount() = "+dbTable.getColumnCount());
        System.out.println("Column(i) = "+dbTable.getColumnModel().getColumn(i).getHeaderValue());
//        ex.printStackTrace();
      }
    }
    jTBSum.setBounds(colX,0,20,20);
    setVisible(true);
  }
  private void calcLocation(int idx) {
    if (getSumCol(idx)==null) return;
    if (getSetCol(idx).getColumnName().equals(getSumCol(idx).getColumnName())) return;
    calcAll();
    moveAll();
  }
  private boolean isStopped() {
    return ((dataSetTableModel)dbTable.getModel()).isFireStopped();
  }
  private void moveAll() {
    int visX = dbTable.getVisibleRect().x;
//    System.out.println("visX = "+visX);
//    if (visX == 0) return;
    int startColX = 0;
    for (int i=0; i<this.getComponentCount();i++) {
      Component currComp = this.getComponent(i);
      Rectangle cBounds = currComp.getBounds();
      currComp.setBounds(startColX-visX,cBounds.y,cBounds.width,cBounds.height);
      startColX = startColX + currComp.getSize().width;
    }
  }

  private void calcAll() {
    stopSizing = true;
    setVisible(false);
    removeAll();
    addColsToRow();
//    validate();
    setVisible(true);
    stopSizing = false;
  }
*/
/* airtsum
  public void refresh() {
//    setVisible(false);
    updateColumnsData();
//    validate();
//    setVisible(true);
  }
*/
/* airtsum
  private void updateColumnsData() {
    sumRow.open();
    sumRow.enableDataSetEvents(true);
  }
  private boolean stopSizing = false;

  class rowSumeColumnModelListener implements javax.swing.event.TableColumnModelListener {
    public void columnAdded(javax.swing.event.TableColumnModelEvent e) {
      if (!isStopped()) calcAll();
    }
    public void columnMarginChanged(javax.swing.event.ChangeEvent e) {
      if (!isStopped()) calcSize();
    }
    public void columnMoved(javax.swing.event.TableColumnModelEvent e) {
      if (!isStopped()) calcLocation(e.getFromIndex());
    }
    public void columnRemoved(javax.swing.event.TableColumnModelEvent e) {
      if (!isStopped()) calcAll();
    }
    public void columnSelectionChanged(javax.swing.event.ListSelectionEvent e) {
    }
  }

  class rowSumeScrollChangeListener implements javax.swing.event.ChangeListener {
    public void stateChanged(javax.swing.event.ChangeEvent e) {
      System.out.println("stateChanged isStoped = "+isStopped());
      if (!isStopped()) moveAll();
//      ((JComponent)e.getSource()).repaint();
    }
  }
*/
}