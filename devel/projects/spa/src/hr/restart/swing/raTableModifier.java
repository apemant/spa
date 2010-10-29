/****license*****************************************************************
**   file: raTableModifier.java
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
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
/**
 *
 * <p>Title: raTableModifier</p>
 * <p>Description: abstract table modifier; see example below</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest Art</p>
 * @author svi mi, a special thanx to Ante koji je idejni zacetnik te modifier tehnologije tako da, ako se to pokaze lose, onda je on kriv
 * @version 0.0
*/
public abstract class raTableModifier {
  private JTable table;
  private Object value;
  private boolean selected;
  private boolean focus;
  private int row;
  private int column;
  public Component renderComponent;
/**
 * primjer izvucen i hr.restart.gk.frmNalozi extends raMasterDetail:
 * <pre>
 *   hr.restart.swing.raTableModifier statusColorModifier = new hr.restart.swing.raTableModifier() {
 *   public boolean doModify() {
 *     return (!isSelected()); // ako je selektiran boje ostaju iste
 *   }
 *   public void modify() {
 *     com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
 *     getMasterSet().getVariant("STATUS",getRow(),v); //vrijednost iz dataseta u tom trenutku moze se dobiti jedino na ovaj nacin
 *     JComponent jRenderComp = (JComponent)renderComponent;
 *     if (v.getString().equals("K")) {
 *       jRenderComp.setForeground(Color.blue);
 *     } else if (v.getString().equals("N")) {
 *       jRenderComp.setForeground(Color.red);
 *     } else {
 *       jRenderComp.setForeground(getTable().getForeground());
 *     }
 *   }
 * };
 * .
 * .
 * jbInit() {
 * .
 * .
 *  raMaster.getJpTableView().addTableModifier(statusColorModifier);
 * </pre>
 * Napomena: postoje hr.restart.swing klase koje extendaju ovu, a koje bi mogle posluziti
 */
  
  public abstract boolean doModify();
  public abstract void modify();
  
  protected boolean isColumn(String col) {
    if (table instanceof JraTable2) {
      JraTable2 tab = (JraTable2) getTable();
      Column c = tab.getDataSetColumn(column);
      return (c != null && c.getColumnName().equalsIgnoreCase(col));
    }
    return false;
  }
  
  protected void setValues(JTable v_table,Object v_value,boolean v_selected,boolean v_focus,
        int v_row,int v_column, Component v_rendcom) {
    table = v_table;
    value = v_value;
    selected = v_selected;
    focus = v_focus;
    row = v_row;
    column = v_column;
    renderComponent = v_rendcom;
  }
  
  public void dataChanged() {
    
  }
  
  public void setComponentText(String txt) {
    if (renderComponent instanceof JLabel) {
      ((JLabel)renderComponent).setText(txt);
    } else if (renderComponent instanceof JTextComponent) {
      ((JTextComponent)renderComponent).setText(txt);
    }
  }
  
  public JTable getTable() {
    return table;
  }
  public DataSet getDataSet() {
    return ((JraTable2) table).getDataSet();
  }
  public Object getValue() {
    return value;
  }
  public boolean isSelected() {
    return selected;
  }
  public boolean hasFocus() {
    return focus;
  }
  public int getRow() {
    return row;
  }
  public int getColumn() {
    return column;
  }
  public Component getComponent() {
    return renderComponent;
  }
}