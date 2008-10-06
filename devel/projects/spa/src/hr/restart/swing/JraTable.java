/****license*****************************************************************
**   file: JraTable.java
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

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.borland.dbswing.JdbTable;

/**
 * Naslijedjena od JdbTable samo sa nekim preinakama.
 * Vidi metodu {@link #processTableKeyEvent(KeyEvent) processTableKeyEvent}.
 */

public class JraTable extends JdbTable implements JraTableInterface {
  com.borland.dbswing.DBTableModel tableModel = new com.borland.dbswing.DBTableModel();
    {
//      setFont(getFont().deriveFont(java.awt.Font.BOLD));
      getColumnModel().setColumnMargin(2);
      setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
      setRowHeaderVisible(false);
      setEditable(false);
      addFocusListener(new FocusAdapter() {
        public void focusGained(FocusEvent e) {
          killFocus(e);
        }
      });
    }

    public JraTable() {
      setModel(tableModel);
    }

    public JraTable(javax.swing.table.TableModel model) {
      super(model);
    }

    public void setDataSet(com.borland.dx.dataset.DataSet ds) {
      tableModel.setDataSet(ds);
      super.setDataSet(ds);
    }

    public boolean isCellSelected(int row, int column) {
      return isRowSelected(row);
    }

    public void keyTyped(KeyEvent e) {
      e.consume();
    }
    public void keyReleased(KeyEvent e) {
      e.consume();
    }
    public void keyPressed(KeyEvent e){
      e.consume();
    }

/**
 * Ako je kliknuto na header zove super.mouseClicked zbog sorta a ako nije zove killFocus
 */
    public void mouseClicked(MouseEvent e) {
      if (e.getSource() == getTableHeader()) {
        super.mouseClicked(e);
        return;
      }
      killFocus(e);
      if (e.getClickCount()==2) tableDoubleClicked();
    }
/**
 * Zove samo defaultni popupmenu sa tendencijom stvaranja svojeg
 */
    public void mousePressed(MouseEvent e) {
      if (e.isPopupTrigger()) super.mousePressed(e);
      killFocus(e);
    }
/**
 * Zove samo defaultni popupmenu
 */
    public void mouseReleased(MouseEvent e) {
      if (e.isPopupTrigger()) super.mouseReleased(e);
      killFocus(e);
    }
/**
 *
 */
  public void tableDoubleClicked() {
  }
/**
 * zove hr.restart.swing.JraKeyListener.focusNext(e);
 */
    public void killFocus(java.util.EventObject e) {
      hr.restart.swing.JraKeyListener.focusNext(e);
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
  public void processTableKeyEvent(KeyEvent e) {
    if (isShowing()&&!e.isConsumed()&&(e.getModifiers()==0)&&(getDataSet()!=null)) {
      if (e.getKeyCode()==e.VK_UP) {
        if (!getDataSet().prior()) getDataSet().first();
        e.consume();
      } else if (e.getKeyCode()==e.VK_DOWN)  {
        if (!getDataSet().next()) getDataSet().last();
        e.consume();
      } else if (e.getKeyCode()==e.VK_PAGE_DOWN)  {
        if (!getDataSet().goToRow(getDataSet().getRow()+getVisibleTableRows())) getDataSet().last();
        e.consume();
      } else if (e.getKeyCode()==e.VK_PAGE_UP)  {
        if (!getDataSet().goToRow(getDataSet().getRow()-getVisibleTableRows())) getDataSet().first();
        e.consume();
      } else if (e.getKeyCode()==e.VK_HOME)  {
        getDataSet().first();
        e.consume();
      } else if (e.getKeyCode()==e.VK_END)  {
        getDataSet().last();
        e.consume();
      }
    }
  }

  private int getVisibleTableRows() {
    return getVisibleRect().height/getRowHeight();
  }

  public JraTableInterface clone(javax.swing.table.TableModel model) {
    return new JraTable(model);
  }
  public void setTableColumnsUI() {
  }
}