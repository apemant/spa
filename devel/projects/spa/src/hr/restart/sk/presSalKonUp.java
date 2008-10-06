/****license*****************************************************************
**   file: presSalKonUp.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.sysoutTEST;
import hr.restart.zapod.OrgStr;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class presSalKonUp extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  sysoutTEST sys = new sysoutTEST(false);

  frmSalKonUp fSalKonUp;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlDatumfrom = new JLabel();
  JLabel jlZiro = new JLabel();
  JraButton jbSelZiro = new JraButton();
  JraTextField jraDatumfrom = new JraTextField();
  JraTextField jraDatumto = new JraTextField();
  JlrNavField jlrZiro = new JlrNavField();

  public presSalKonUp(frmSalKonUp md) {
    try {
      fSalKonUp = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus() {
    getSelRow().setTimestamp("DATUM-from",ut.getFirstDayOfMonth());
    getSelRow().setTimestamp("DATUM-to",vl.getToday());
    getSelRow().post();
    jlrZiro.requestFocus();
  }

  public boolean Validacija() {
     return (Aus.checkDateRange(jraDatumfrom, jraDatumto) &&
             Aus.checkGKDateRange(jraDatumfrom, jraDatumto));
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(385);
    lay.setHeight(85);

    jbSelZiro.setText("...");
    jlDatumfrom.setText("Datum (od - do)");
    jlZiro.setText("Žiro ra\u010Dun");
    jraDatumfrom.setColumnName("DATUM");
    jraDatumfrom.setDataSet(fSalKonUp.getMasterSet());
    jraDatumfrom.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumto.setColumnName("DATUM");
    jraDatumto.setDataSet(fSalKonUp.getMasterSet());
    jraDatumto.setHorizontalAlignment(SwingConstants.CENTER);
    new raDateRange(jraDatumfrom, jraDatumto);

    jlrZiro.setColumnName("ZIRO");
    jlrZiro.setDataSet(fSalKonUp.getMasterSet());
    jlrZiro.setVisCols(new int[] {1});
    jlrZiro.setSearchMode(0);
    jlrZiro.setRaDataSet(OrgStr.getOrgStr().getKnjigziro(OrgStr.getKNJCORG()));
    jlrZiro.setNavButton(jbSelZiro);

//    sys.prn(fSalKonUp.getMasterSet());

    jpDetail.add(jbSelZiro, new XYConstraints(360, 20, 21, 21));
    jpDetail.add(jlDatumfrom, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlZiro, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrZiro, new XYConstraints(150, 20, 205, -1));
    jpDetail.add(jraDatumfrom, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraDatumto, new XYConstraints(255, 45, 100, -1));

    this.addSelRange(jraDatumfrom, jraDatumto);
  }
}
