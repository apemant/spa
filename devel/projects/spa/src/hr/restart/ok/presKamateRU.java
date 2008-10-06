/****license*****************************************************************
**   file: presKamateRU.java
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
package hr.restart.ok;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class presKamateRU extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();

  JraButton jbSelCpar = new JraButton();

  JraTextField jraKnjig = new JraTextField();

  raComboBox rcbCpar = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      if (this.getSelectedIndex() == 0) kupSelected();
      else dobSelected();
    }
  };

  JlrNavField jlrCpar = new JlrNavField();
  JlrNavField jlrNazpar = new JlrNavField();

  JLabel jlDatum = new JLabel();
  JraTextField jraDatumOd = new JraTextField();
  JraTextField jraDatumDo = new JraTextField();

  frmKamateRU frm;

  public presKamateRU() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus() {
    getSelRow().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG(false));
    if (rcbCpar.getSelectedIndex() == 0) kupSelected();
    else dobSelected();
    getSelRow().setTimestamp("DATDOK-from", ut.getFirstDayOfYear());
    getSelRow().setTimestamp("DATDOK-to", vl.getToday());
//    jlrCpar.setText("");
//    jlrCpar.forceFocLost();
    jlrCpar.requestFocus();
  }

  public void setOwner(frmKamateRU owner) {
    this.frm = owner;
  }

  public boolean Validacija() {
    if (!Aus.checkDateRange(jraDatumOd, jraDatumDo)) return false;
    frm.jpMaster.jlrCpar.setRaDataSet(jlrCpar.getRaDataSet());
    frm.jpDetail.jlrCpar.setRaDataSet(jlrCpar.getRaDataSet());
    return true;
  }

  private void jbInit() throws Exception {
    this.setSelDataSet(dm.getKamRac());

    jpDetail.setLayout(lay);
    lay.setWidth(570);
    lay.setHeight(75);

    rcbCpar.setRaDataSet(getSelDataSet());
    rcbCpar.setRaColumn("DUGPOT");
    rcbCpar.setRaItems(new String[][] {
      {"Kupac", "D"},
      {"Dobavlja\u010D", "P"}
    });
    rcbCpar.setSelectedIndex(0);

    jbSelCpar.setText("...");

    jraKnjig.setColumnName("KNJIG");
    jraKnjig.setDataSet(getSelDataSet());
    jraKnjig.setVisible(false);
    jraKnjig.setEnabled(false);

    jlrCpar.setColumnName("CPAR");
    jlrCpar.setDataSet(getSelDataSet());
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
    jlrCpar.setVisCols(new int[] {0, 1});
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setNavButton(jbSelCpar);

    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1);

    jlDatum.setText("Datum (od - do)");
    jraDatumOd.setColumnName("DATDOK");
    jraDatumOd.setDataSet(getSelDataSet());
    jraDatumDo.setColumnName("DATDOK");
    jraDatumDo.setDataSet(getSelDataSet());

    jpDetail.add(rcbCpar, new XYConstraints(15, 20, 130, -1));
    jpDetail.add(jlrCpar, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrNazpar, new XYConstraints(255, 20, 290, -1));
    jpDetail.add(jbSelCpar, new XYConstraints(550, 20, 21, 21));
    jpDetail.add(jraKnjig, new XYConstraints(5, 45, 5, -1));
    jpDetail.add(jlDatum, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jraDatumOd, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraDatumDo, new XYConstraints(255, 45, 100, -1));

//    sysoutTEST sys = new sysoutTEST(false);
//    sys.prn(getSelRow());
    this.addSelRange(jraDatumOd, jraDatumDo);
    this.setSelPanel(jpDetail);
  }
  protected void kupSelected() {
    jlrCpar.setRaDataSet(dm.getPartneriKup());
//    jlrCpar.setText("");
    jlrCpar.forceFocLost();
  }

  protected void dobSelected() {
    jlrCpar.setRaDataSet(dm.getPartneriDob());
//    jlrCpar.setText("");
    jlrCpar.forceFocLost();
  }
}
