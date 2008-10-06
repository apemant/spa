/****license*****************************************************************
**   file: presDoc.java
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
package hr.restart.ur;

import hr.restart.baza.Condition;
import hr.restart.baza.Urvrdok;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class presDoc extends PreSelect {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  
  JLabel jlVrdok = new JLabel();
  JlrNavField jlrVrdok = new JlrNavField();
  JlrNavField jlrNazdok = new JlrNavField();
  JraButton jbSelVrdok = new JraButton();
  
  JLabel jlDatum = new JLabel();
  JraTextField jraDatumod = new JraTextField();
  JraTextField jraDatumdo = new JraTextField();
  
  JLabel jlGod = new JLabel();
  JraTextField jraGod = new JraTextField();
  
  JPanel jpDetail = new JPanel();
  XYLayout lay = new XYLayout();
  
  private boolean firstReset = false;
  
  public presDoc() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void resetDefaults() {
    getSelRow().setTimestamp("DATPRI-from", vl.getToday());
    getSelRow().setTimestamp("DATPRI-to", vl.getToday());
    getSelRow().setString("GOD", vl.findYear(vl.getToday()));
  }

  public void SetFokus() {
    if (!firstReset ) {
      firstReset = true;
      resetDefaults();
    }
    jlrVrdok.requestFocusLater();
  }
  
  public boolean Validacija() {
    if (vl.isEmpty(jlrVrdok) || vl.isEmpty(jraGod)) return false;
    if (!Aus.checkDateRange(jraDatumod, jraDatumdo)) return false;
    /*if (!Util.getUtil().getYear(getSelRow().getTimestamp("DATPRI-from")).equals(
        Util.getUtil().getYear(getSelRow().getTimestamp("DATPRI-to")))) {
      jraDatumod.requestFocus();
      JOptionPane.showMessageDialog(jraDatumod.getTopLevelAncestor(),
          "Period obuhvaæa više od jedne godine!","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }*/
    int dgf = Aus.getNumber(Util.getUtil().getYear(getSelRow().getTimestamp("DATPRI-from")));
    int dgt = Aus.getNumber(Util.getUtil().getYear(getSelRow().getTimestamp("DATPRI-to")));
    if (dgf < dgt - 1) {
      jraDatumod.requestFocus();
      JOptionPane.showMessageDialog(jraDatumod.getTopLevelAncestor(),
          "Period obuhvaæa više od dvije godine!","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    int god = Aus.getNumber(getSelRow().getString("GOD"));
    if (god < dgf - 1 || god > dgf) {
      jraGod.requestFocus();
      JOptionPane.showMessageDialog(jraGod.getTopLevelAncestor(),
          "Pogrešna godina za ovaj period!","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  
  private void jbInit() throws Exception {
    setSelDataSet(frmDoc.getInstance().getRaQueryDataSet());
    
    jpDetail.setLayout(lay);
    lay.setWidth(536);
    lay.setHeight(130);
    
    jlVrdok.setText("Vrsta dokumenta");
    jlrVrdok.setColumnName("VRDOK");
    jlrVrdok.setDataSet(getSelDataSet());
    jlrVrdok.setColNames(new String[] {"NAZIV"});
    jlrVrdok.setTextFields(new JTextComponent[] {jlrNazdok});
    jlrVrdok.setVisCols(new int[] {0, 1});
    jlrVrdok.setSearchMode(0);
    jlrVrdok.setRaDataSet(Urvrdok.getDataModule().copyDataSet());
    jlrVrdok.setNavButton(jbSelVrdok);

    jlrNazdok.setColumnName("NAZIV");
    jlrNazdok.setNavProperties(jlrVrdok);
    jlrNazdok.setSearchMode(1);
    
    jlDatum.setText("Period (od - do)");
    jraDatumod.setColumnName("DATPRI");
    jraDatumod.setDataSet(getSelDataSet());
    jraDatumod.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumdo.setColumnName("DATPRI");
    jraDatumdo.setDataSet(getSelDataSet());
    jraDatumdo.setHorizontalAlignment(SwingConstants.CENTER);
    
    jlGod.setText("Godina");
    jraGod.setColumnName("GOD");
    jraGod.setDataSet(getSelDataSet());
    jraGod.setHorizontalAlignment(SwingConstants.CENTER);
    new raTextMask(jraGod, 4, false, raTextMask.DIGITS);
    
    jpDetail.add(jlVrdok, new XYConstraints(15, 25, -1, -1));
    jpDetail.add(jlrVrdok, new XYConstraints(150, 25, 100, -1));    
    jpDetail.add(jlrNazdok, new XYConstraints(255, 25, 250, -1));
    jpDetail.add(jbSelVrdok, new XYConstraints(510, 25, 21, 21));
    jpDetail.add(jlDatum, new XYConstraints(15, 50, -1, -1));
    jpDetail.add(jraDatumod, new XYConstraints(150, 50, 100, -1));
    jpDetail.add(jraDatumdo, new XYConstraints(255, 50, 100, -1));
    jpDetail.add(jlGod, new XYConstraints(15, 75, -1, -1));
    jpDetail.add(jraGod, new XYConstraints(150, 75, 100, -1));
    
    installResetButton();
    this.addSelRange(jraDatumod, jraDatumdo);
    this.setSelPanel(jpDetail);
  }
  
  public Condition getPresCondition() {
    return Aus.getKnjigCond().and(Condition.equal("VRDOK", getSelRow())).
              and(Condition.equal("GOD", getSelRow())).
              and(Condition.between("DATPRI",
              getSelRow().getTimestamp("DATPRI-from"),
              getSelRow().getTimestamp("DATPRI-to"))).and(getUserCondition());
  }
  
  public boolean applySQLFilter() {
    QueryDataSet selQDS = (QueryDataSet) getSelDataSet();    
    Aus.setFilter(selQDS, Valid.getValid().getNoWhereQuery(selQDS)+ 
        " WHERE " + getPresCondition());
    selQDS.open();
    return true;
  }
}
