/****license*****************************************************************
**   file: frmKlijentStatus.java
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
package hr.restart.crm;

import hr.restart.baza.KlijentStat;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.raSifraNaziv;

import java.awt.Color;
import java.lang.reflect.Field;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmKlijentStatus extends raSifraNaziv {
  
  public frmKlijentStatus() {
    try {
        jbInit();
    }
    catch(Exception e) {
        e.printStackTrace();
    }
  }
  
  JraTextField jraBoja = new JraTextField() {
    public void valueChanged() {
      checkColor();
    };
  };
  JLabel disp = new JLabel();
  Color defColor = disp.getBackground();
  
  private void jbInit() throws Exception {
    this.setRaDataSet(KlijentStat.getDataModule().getFilteredDataSet(""));
    this.setRaColumnSifra("SID");
    this.setRaColumnNaziv("NAZIV");
    this.setRaText("Status");
    
    jraBoja.setColumnName("BOJA");
    jraBoja.setDataSet(getRaDataSet());
    disp.setOpaque(true);

    
    JPanel dod = new JPanel(new XYLayout(555, 40));
    dod.add(new JLabel("Boja"), new XYConstraints(15, 0, -1, -1));
    dod.add(jraBoja, new XYConstraints(150, 0, 100, -1));
    dod.add(disp, new XYConstraints(260, 0, 100, 21));
    this.jpRoot.add(dod, java.awt.BorderLayout.SOUTH);
  }
  
  public void raQueryDataSet_navigated(NavigationEvent e) {
    checkColor();
  }
  
  void checkColor() {
    disp.setBackground(findColor(getRaDataSet().getString("BOJA")));
  }
  
  private Color findColor(String col) {
    if (col.startsWith("#") || col.startsWith("0x") || col.startsWith("0X") || Aus.isDigit(col))
      try {
        return Color.decode(col);
      } catch (Exception e) {
        //
      }
    else 
      try {
        Field f = Color.class.getDeclaredField(col);
        return (Color) f.get(null);
      } catch (Exception e) {
        System.out.println(e);
      }
    return defColor;
  }
}
