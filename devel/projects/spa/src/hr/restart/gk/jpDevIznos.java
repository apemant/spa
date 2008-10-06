/****license*****************************************************************
**   file: jpDevIznos.java
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
/*
 * jpDevIznos.java
 *
 * Created on April 14, 2004, 10:31 AM
 */

package hr.restart.gk;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.zapod.Tecajevi;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
/**
 *
 * @author  andrej
 */
public class jpDevIznos extends JPanel {
  String IDcol = "ID";
  String IPcol = "IP";
  String devIDcol = "DEVID";
  String devIPcol = "DEVIP";
  String tecajcol = "TECAJ";
  DataSet dataSet;
  StorageDataSet uiSet;
  XYLayout xyl = new XYLayout();
  JlrNavField jlrOZNVAL = new JlrNavField() {
    public void after_lookUp() {
      calcValues(true);
    }
  };
  JraButton jbGetVal = new JraButton();
  JraTextField jtTECAJ = new JraTextField() {
    public void valueChanged() {
      calcValues(false);
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      calcValues();
    }*/
  };
  JraTextField jtDEVIZNOS = new JraTextField() {
    public void valueChanged() {
      calcValues(true);
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      calcValues();
    }*/
  };
  JLabel jlab = new JLabel();
  BigDecimal pvVal = new BigDecimal(0);
  Timestamp tecDate = new Timestamp(System.currentTimeMillis());
  /** Creates a new instance of jpDevIznos */
  public jpDevIznos() {
    jpInit();
  }
  private void jpInit() {
    uiSet = new StorageDataSet();
    uiSet.setColumns(new Column[] {
      dM.createStringColumn("OZNV",3), 
      dM.createBigDecimalColumn("DEVIZNOS"),
      dM.createBigDecimalColumn("TEC",6),
    });
    uiSet.open();
    jlrOZNVAL.setColumnName("OZNV");
    jlrOZNVAL.setNavColumnName("OZNVAL");
    jlrOZNVAL.setDataSet(uiSet);
    jlrOZNVAL.setRaDataSet(dM.getDataModule().getAllValute());
    jlrOZNVAL.setSearchMode(0);
    jlrOZNVAL.setNavButton(jbGetVal);
    jlrOZNVAL.setVisCols(new int[] {0,1});
    jbGetVal.setText("...");
    jtTECAJ.setColumnName("TEC");
    jtTECAJ.setDataSet(uiSet);
    jtDEVIZNOS.setColumnName("DEVIZNOS");
    jtDEVIZNOS.setDataSet(uiSet);
    jlab.setText("Iznos u valuti");
    setLayout(xyl);
    xyl.setWidth(630);
    add(jlab, new XYConstraints(15, 0, -1, -1));
    add(jtDEVIZNOS, new XYConstraints(190, 0, 140, -1));
//    add(jlrOZNVAL, new XYConstraints(335, 0, 54, -1));
//    add(jbGetVal, new XYConstraints(394, 0, 21, 21));
    add(jtTECAJ, new XYConstraints(420, 0, 140, -1));
    add(jbGetVal, new XYConstraints(605, 0, 21, 21));
    add(jlrOZNVAL, new XYConstraints(565, 0, 35, -1));
  }
  public void setIDcol(String s) {
    IDcol = s;
  }
  public void setIPcol(String s) {
    IPcol = s;
  }
  public void setDevIDcol(String s) {
    devIDcol = s;
  }
  public void setDevIPcol(String s) {
    devIPcol = s;
  }
  public void setTecajcol(String s) {
    tecajcol = s;
  }
  public void setDataSet(DataSet ds) {
    dataSet = ds;
  }
  private String oldOZNVAL = "";
  public void setFokus(char mode) {
    if (dataSet == null) return;
    if (dataSet.hasColumn("OZNVAL")==null) return;
    uiSet.emptyAllRows();
    uiSet.insertRow(false);
//    jtTECAJ.setEditable(false);
    if (mode == 'N') {
      uiSet.setString("OZNV", oldOZNVAL);
    } else if (mode == 'I') {
      fillValues();
    } else if (mode == 'B') {
      fillValues();
    }
  }
  public void setPVval(BigDecimal pv) {
    pvVal = pv;
    calcValues(true);
  }
  public void setTecDate(Timestamp td) {
    tecDate = td;
  }
  public void fillValues() {
    if (dataSet == null) return;
    uiSet.setBigDecimal("DEVIZNOS", dataSet.getBigDecimal(devIDcol).add(dataSet.getBigDecimal(devIPcol)));
    uiSet.setString("OZNV", dataSet.getString("OZNVAL"));
    uiSet.setBigDecimal("TEC", dataSet.getBigDecimal(tecajcol));    
  }
  public boolean validate(char mode) {
    oldOZNVAL = uiSet.getString("OZNV");
    if (uiSet.getString("OZNV").equals(Tecajevi.getDomOZNVAL())) uiSet.setString("OZNV","");
    if (Valid.getValid().chkIsEmpty(jtDEVIZNOS)) uiSet.setString("OZNV",""); // ako nije unio devizni iznos - valutu na prazan string pa da vidis dalje
    if (Valid.getValid().chkIsEmpty(jlrOZNVAL)) return true;//ako nije unio valutu nije unio nista :)
    if (Valid.getValid().isEmpty(jtDEVIZNOS)) return false;
    //if (Valid.getValid().isEmpty(jlrOZNVAL)) return false;
    if (Valid.getValid().isEmpty(jtTECAJ)) return false;
    BigDecimal devid = dataSet.getBigDecimal(IDcol).signum()==0? new BigDecimal(0):uiSet.getBigDecimal("DEVIZNOS");
    BigDecimal devip = dataSet.getBigDecimal(IPcol).signum()==0? new BigDecimal(0):uiSet.getBigDecimal("DEVIZNOS");
    dataSet.setBigDecimal(devIDcol, devid);
    dataSet.setBigDecimal(devIPcol, devip);
    dataSet.setString("OZNVAL", uiSet.getString("OZNV"));
    dataSet.setBigDecimal(tecajcol, uiSet.getBigDecimal("TEC"));
    return true;
  }
  public void calcValues(boolean ntec) {
    if (pvVal.signum() == 0) return;
    if (uiSet.getBigDecimal("DEVIZNOS").signum() == 0) return;
    if (uiSet.getString("OZNV").equals("")) return;
    if (ntec || dataSet == null)
      uiSet.setBigDecimal("TEC", pvVal.setScale(6).divide(uiSet.getBigDecimal("DEVIZNOS"),BigDecimal.ROUND_HALF_UP)
      .multiply(Tecajevi.getJedVal(uiSet.getString("OZNV"))));
    else {
      String dest = dataSet.getBigDecimal("ID").signum() != 0 ? "ID" : "IP";
      dataSet.setBigDecimal(dest, 
        uiSet.getBigDecimal("DEVIZNOS").multiply(uiSet.getBigDecimal("TEC")).divide(
          Tecajevi.getJedVal(uiSet.getString("OZNV")), 2, BigDecimal.ROUND_HALF_UP));
    }
   /*
    System.out.println("calcValues");
    System.out.println("   pv = "+pvVal);
    System.out.println("   td = "+tecDate);
    System.out.println("   val= "+uiSet.getString("OZNV"));
    //prioritet uneseni podaci DEVIZNOS, TECAJ
    if (!uiSet.getString("OZNV").equals("")) {//upisao valutu
      if (uiSet.getBigDecimal("DEVIZNOS").signum() != 0) {//racunaj tecaj
System.out.println(uiSet.getBigDecimal("DEVIZNOS")+" != 0");
        uiSet.setBigDecimal("TEC", uiSet.getBigDecimal("DEVIZNOS")
          .multiply(Tecajevi.getJedVal(uiSet.getString("OZNV")))
          .divide(pvVal,BigDecimal.ROUND_HALF_UP));
      } else if (uiSet.getBigDecimal("TEC").signum() != 0) {//racunam deviznos
System.out.println(uiSet.getBigDecimal("TEC")+" != 0");
        uiSet.setBigDecimal("DEVIZNOS", uiSet.getBigDecimal("TEC").multiply(pvVal)
                .divide(Tecajevi.getJedVal(uiSet.getString("OZNV")),BigDecimal.ROUND_HALF_UP));
      } else {
System.out.println(" ELSE ");
        uiSet.setBigDecimal("DEVIZNOS", Tecajevi.getTecaj1(tecDate, uiSet.getString("OZNV")).multiply(pvVal));
      }
    }
    jtTECAJ.getDataBinder().updateText();
    System.out.println("tecaj = "+uiSet.getBigDecimal("TEC"));
   */
  }
}
