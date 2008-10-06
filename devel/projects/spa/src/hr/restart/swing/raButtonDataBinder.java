/****license*****************************************************************
**   file: raButtonDataBinder.java
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
import hr.restart.db.raDataChangeAdapter;
import hr.restart.db.raDataSetSupport;
import hr.restart.db.raVariant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import com.borland.dx.dataset.DataSet;

public class raButtonDataBinder implements ActionListener, raDataChangeAdapter, raDataBinder {
  private AbstractButton button;
  private raDataSetSupport dsSupport = new raDataSetSupport();
  private Object selectedValue;
  private Object unselectedValue;
  public raButtonDataBinder(AbstractButton butn) {
    button = butn;
    button.getModel().addActionListener(this);
  }

  public void setSelectedValue(Object v) {
    selectedValue = v;
  }

  public void setUnselectedValue(Object v) {
    unselectedValue = v;
  }

  public Object getSelectedValue() {
    return selectedValue;
  }

  public Object getUnselectedValue() {
    return unselectedValue;
  }
//raDataBinder
  public void setDataSet(DataSet ds) {
    dsSupport.setDataSet(ds,this);
  }

  public DataSet getDataSet() {
    return dsSupport.getDataSet();
  }

  public void setColumnName(String colName) {
    dsSupport.setColumnName(colName);
  }

  public String getColumnName() {
    return dsSupport.getColumnName();
  }
/*
  public void setSelected(boolean sel) {
    Object oldVal = button.isSelected() ? selectedValue:unselectedValue;
    if (sel) dataChanged(oldVal,selectedValue);
    else dataChanged(oldVal,unselectedValue);
  }
*/
  public java.awt.Component getBindedComponent() {
    return button;
  }

  public Object getBindedValue() {
    if (button.isSelected())
      return selectedValue;
    else
      return unselectedValue;
  }

//raDataChangeAdapter
  public void dataChanged(Object oldVal,Object newVal) {
    button.setSelected(selectedValue.equals(newVal));
  }
//ActionListener
  public void actionPerformed(ActionEvent ev) {
    if (getDataSet()==null || getColumnName()==null) return;
    if (button.isSelected())
      raVariant.setDataSetValue(dsSupport.getDataSet(),dsSupport.getColumnName(),selectedValue);
    else if (unselectedValue != null)
      raVariant.setDataSetValue(dsSupport.getDataSet(),dsSupport.getColumnName(),unselectedValue);
  }
}
/*
      if (bds.isEmpty()) return;
      JraRadioButton rb;
      JraRadioButton sel = (JraRadioButton) buttons.firstElement();
      Iterator i = buttons.iterator();
      while (i.hasNext()) {
        rb = (JraRadioButton) i.next();
        if (rb.getSelectedValue().equals(bds.getString(colname))) {
          sel = rb;
          break;
        }
      }
      sel.setSelected(true);

*/