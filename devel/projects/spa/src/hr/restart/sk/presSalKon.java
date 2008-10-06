/****license*****************************************************************
**   file: presSalKon.java
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
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.KnjigeUI;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



public class presSalKon extends PreSelect {
//  sysoutTEST sys = new sysoutTEST(false);
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  int lastui = 0;
  DataSet dsm, knjU, knjI;
  JPanel jpDetail = new JPanel();

  JPanel jpK = new JPanel();
  XYLayout xyK = new XYLayout();

  XYLayout lay = new XYLayout();
  JLabel jlK = new JLabel();
  JLabel jlCknjige = new JLabel();
  JLabel jlDatum = new JLabel();
  JraButton jbSelCknjige = new JraButton();
  JraRadioButton jrbUraira1 = new JraRadioButton();
  JraRadioButton jrbUraira2 = new JraRadioButton();
  JraTextField jraRBS = new JraTextField();
  JraTextField jraKnjig = new JraTextField();
  JraTextField jraDatum = new JraTextField();
  JraTextField jraDatum1 = new JraTextField();
  JraTextField jraVrdok = new JraTextField();
  raButtonGroup bg1 = new raButtonGroup();
  JlrNavField jlrNazknjige = new JlrNavField();
  public JlrNavField jlrCknjige = new JlrNavField();

  String col, uval, ival;
  
  boolean firstReset = false;

  public presSalKon(DataSet ds, String col, String uval, String ival) {
    this.col = col;
    this.uval = uval;
    this.ival = ival;
    dsm = ds;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public presSalKon(DataSet ds) {
    this(ds, "VRDOK", "URN", "IRN");
  }
  
  public void resetDefaults() {
    getSelRow().setTimestamp("DATPRI-from",ut.getFirstDayOfYear());
    getSelRow().setTimestamp("DATPRI-to",vl.getToday());
    bg1.setSelected(jrbUraira1);
    jlrCknjige.setText("");
    jlrCknjige.forceFocLost();
  }

  public void SetFokus() {
    getSelRow().setString(col, ival);
//    getSelRow().setString("URAIRA", "I");
    getSelRow().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG(false));
    getSelRow().setInt("RBS", 1);
    if (!firstReset) {
      firstReset = true;
      resetDefaults();
    }
//    getSelRow().post();
//    jrbUraira2.setSelected();
//    iraSelected();
    if (jrbUraira1.isSelected()) uraSelected();
    else iraSelected();
    jlrCknjige.requestFocus();
  }

  public boolean Validacija() {
     getSelRow().setString(col, jrbUraira1.isSelected() ? uval : ival);
     return (Aus.checkDateRange(jraDatum, jraDatum1) &&
             Aus.checkGKDateRange(jraDatum, jraDatum1));
  }

  private void jbInit() throws Exception {
//    setSelDataSet(dm.get);
    jpDetail.setLayout(lay);
    lay.setWidth(536);
    lay.setHeight(110);

    bg1.setHorizontalAlignment(SwingConstants.LEADING);
    bg1.setHorizontalTextPosition(SwingConstants.TRAILING);
//    bg1.setColumnName("URAIRA");
//    bg1.setDataSet(fSalKon.getMasterSet());
    bg1.add(jrbUraira1, " Ulaznih ra\u010Duna ", "U");
    bg1.add(jrbUraira2, " Izlaznih ra\u010Duna ", "I");
    bg1.setSelected(jrbUraira1);
    jbSelCknjige.setText("...");
    jlK.setText("Knjiga");
    jlCknjige.setText("Vrsta knjige");
    jlDatum.setText("Period (od - do)");
    jraDatum.setColumnName("DATPRI");
    jraDatum.setDataSet(dsm);
    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatum1.setColumnName("DATPRI");
    jraDatum1.setDataSet(dsm);
    jraDatum1.setHorizontalAlignment(SwingConstants.CENTER);
    jraVrdok.setColumnName(col);
    jraVrdok.setDataSet(dsm);
    jraVrdok.setVisible(false);
    jraVrdok.setEnabled(false);
    jraKnjig.setColumnName("KNJIG");
    jraKnjig.setDataSet(dsm);
    jraKnjig.setVisible(false);
    jraKnjig.setEnabled(false);
    jraRBS.setColumnName("RBS");
    jraRBS.setDataSet(dsm);
    jraRBS.setVisible(false);
    jraRBS.setEnabled(false);

    jlrCknjige.setColumnName("CKNJIGE");
    jlrCknjige.setDataSet(dsm);
    jlrCknjige.setColNames(new String[] {"NAZKNJIGE"});
    jlrCknjige.setTextFields(new JTextComponent[] {jlrNazknjige});
    jlrCknjige.setVisCols(new int[] {0, 4});
    jlrCknjige.setSearchMode(0);
    jlrCknjige.setRaDataSet(dm.getKnjigeUI());
    jlrCknjige.setNavButton(jbSelCknjige);

    jlrNazknjige.setColumnName("NAZKNJIGE");
    jlrNazknjige.setNavProperties(jlrCknjige);
    jlrNazknjige.setSearchMode(1);

    jpDetail.add(jbSelCknjige, new XYConstraints(510, 50, 21, 21));
    jpDetail.add(jlK, new XYConstraints(15, 24, -1, -1));
    jpDetail.add(jlCknjige, new XYConstraints(15, 50, -1, -1));
    jpDetail.add(jlDatum, new XYConstraints(15, 75, -1, -1));
    jpDetail.add(jlrCknjige, new XYConstraints(150, 50, 100, -1));
    jpDetail.add(jlrNazknjige, new XYConstraints(255, 50, 250, -1));
    jpDetail.add(jraDatum, new XYConstraints(150, 75, 100, -1));
    jpDetail.add(jraDatum1, new XYConstraints(255, 75, 100, -1));
    jpDetail.add(jraVrdok, new XYConstraints(360, 75, 5, -1));
    jpDetail.add(jraKnjig, new XYConstraints(370, 75, 5, -1));
    jpDetail.add(jraRBS, new XYConstraints(380, 75, 5, -1));

    jpK.setLayout(xyK);
    xyK.setWidth(351);
    xyK.setHeight(25);
    jpK.setBorder(BorderFactory.createEtchedBorder());
    jpK.add(jrbUraira1, new XYConstraints(25, 0, 150, -1));
    jpK.add(jrbUraira2, new XYConstraints(200, 0, 150, -1));

    jpDetail.add(jpK, new XYConstraints(150, 17, -1, -1));

    jrbUraira1.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (jrbUraira1.isSelected())
          uraSelected();
      }
    });

    jrbUraira2.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (jrbUraira2.isSelected())
          iraSelected();
      }
    });
    installResetButton();
    this.addSelRange(jraDatum, jraDatum1);

  }

  private void uraSelected() {
    if (lastui != 1) {
      lastui = 1;
      jlrCknjige.setRaDataSet(getKnjigeU());
//      getSelRow().setString("CKNJIGE", "");
//      jlrNazknjige.setText("");
      jlrCknjige.forceFocLost();
      getSelRow().setString(col, uval);
      bg1.setSelected(jrbUraira1);
    }
  }

  private void iraSelected() {
    if (lastui != 2) {
      lastui = 2;
      jlrCknjige.setRaDataSet(getKnjigeI());
      //jlrCknjige.setText("");
//      getSelRow().setString("CKNJIGE", "");
//      jlrNazknjige.setText("");
      jlrCknjige.forceFocLost();
      getSelRow().setString(col, ival);
      bg1.setSelected(jrbUraira2);
    }
  }
  
  protected DataSet getKnjigeU() {
    if (knjU == null) {
      knjU = KnjigeUI.getDataModule().getFilteredDataSet(
          Condition.whereAllEqual(new String[] {"uraira", "virtua"}, new String[] {"U","N"}));
    }
    return knjU;
  }
  protected DataSet getKnjigeI() {
    if (knjI == null) {
      knjI = KnjigeUI.getDataModule().getFilteredDataSet(
          Condition.whereAllEqual(new String[] {"uraira", "virtua"}, new String[] {"I","N"}));
    }
    return knjI;
  }
}
