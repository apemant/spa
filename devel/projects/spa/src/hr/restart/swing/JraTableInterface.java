/****license*****************************************************************
**   file: JraTableInterface.java
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

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import com.borland.dx.dataset.DataSet;

public interface JraTableInterface {
  public DataSet getDataSet();
  public void setDataSet(DataSet ds);
  public void processTableKeyEvent(java.awt.event.KeyEvent e);
  public javax.swing.table.TableColumnModel getColumnModel();
  public javax.swing.table.TableModel getModel();
  public JraTableInterface clone(javax.swing.table.TableModel model);
  public int getColumnCount();
  public int getRowCount();
  public void addColumn(javax.swing.table.TableColumn col);
  public void moveColumn(int x,int y);
  public void removeColumn(javax.swing.table.TableColumn col);
  public Object getValueAt(int x, int y);
  public void setRowSelectionInterval(int x, int y);
  public java.awt.Rectangle getCellRect(int x,int y,boolean b);
  public void scrollRectToVisible(java.awt.Rectangle r);
  public int getSelectedRow();
  public int getSelectedColumn();
  public void repaint();
  public void setTableColumnsUI();
  public int getAutoResizeMode();
  public java.awt.Container getParent();
  public java.awt.Dimension getSize();
  public int getRowHeight();
  public int convertColumnIndexToModel(int viewColumnIndex);
  public java.awt.Rectangle getVisibleRect();
  public boolean isShowing();
  public void setAutoResizeMode(int mode);
  public void requestFocus();
}