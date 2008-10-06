/****license*****************************************************************
**   file: raJPNoTablePanel.java
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

import hr.restart.swing.JraTextField;
import hr.restart.swing.layout.raXYConstraints;
import hr.restart.swing.layout.raXYLayout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

public class raJPNoTablePanel extends JPanel {
  private DataSet dataSet;
  private StorageDataSet entryDataSet;
  private String[] columns;
  private raCommonClass rCC;
  private JPanel jp;
  JLabel jlTitle = new JLabel("Odabir po klju\u010Du");
  JraTextField focusField;
  public raJPNoTablePanel(DataSet ds,String[] cols) {
    dataSet = ds;
    columns = cols;
    rCC = raCommonClass.getraCommonClass();
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
    this.setLayout(new BorderLayout());
    jlTitle.setFont(jlTitle.getFont().deriveFont(Font.ITALIC));
    add(getEntryPanel(),BorderLayout.CENTER);
  }

  private int getLineY() {
    return jlTitle.getHeight()+7;
  }

  private JPanel getEntryPanel() {
    jp = new JPanel(new raXYLayout()) {
      public void paint(Graphics g) {
        super.paint(g);
        g.drawLine(0,getLineY(),getWidth(),getLineY());
      }
    };
    jp.add(jlTitle,new raXYConstraints(15,5,-1,-1));
    JraTextField jt=null;
    entryDataSet = new StorageDataSet();
    for (int i = 0; i < columns.length; i++) {
      Column col = dataSet.hasColumn(columns[i]);
      if (col!=null) {
        entryDataSet.addColumn((Column)col.clone());
        JLabel jl = new JLabel(col.getCaption());
        jt = new JraTextField() {
          public void setEnabled(boolean b) {
            super.setEnabled(b);
//            if (b) breakPoint();
          }
        };
        jt.setDataSet(entryDataSet);
        jt.setColumnName(columns[i]);
//        rCC.setLabelLaF(jt,false);
//        jl.setHorizontalAlignment(JLabel.TRAILING);
        jp.add(jl,new raXYConstraints(15, (40+(i*25)), 130, -1));
        jp.add(jt,new raXYConstraints(150, (40+(i*25)), 200, -1));
      }
    }
    if (jt!=null) {
      rCC.setLabelLaF(jt,true);
      focusField = jt;
    }
    return jp;
  }
  public DataSet getEntrySet() {
    return entryDataSet;
  }

  public JraTextField getTextField(String colName) {
    for (int i = 0; i < jp.getComponentCount(); i++) {
      Component c = jp.getComponent(i);
      if (c instanceof JraTextField) {
        JraTextField jt = (JraTextField)c;
        if (jt.getColumnName().equals(colName)) return jt;
      }
    }
    return null;
  }

  public void setProtected(String colName,boolean protd) {
    JraTextField jt = getTextField(colName);
    rCC.setLabelLaF(jt,!protd);
    if (protd) {
      jt.setBorder(null);
      jt.setFont(jt.getFont().deriveFont(Font.BOLD));
      jt.setHorizontalAlignment(JTextField.LEADING);
    } else {
      JraTextField sample = new JraTextField();
      sample.setDataSet(jt.getDataSet());
      sample.setColumnName(jt.getColumnName());
      jt.setBorder(sample.getBorder());
      jt.setFont(sample.getFont());
      jt.setHorizontalAlignment(sample.getHorizontalAlignment());
    }
  }
  public void breakPoint() {
    System.out.println("enabla me netko majku mu");
  }
}