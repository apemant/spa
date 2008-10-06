/****license*****************************************************************
**   file: presProizvodnja.java
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
package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class presProizvodnja extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlStatus = new JLabel();
  JLabel jlCorg = new JLabel();
  JLabel jlDatumfrom = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JraTextField jraDatumfrom = new JraTextField();
  JraTextField jraDatumto = new JraTextField();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JraTextField jraSerpr = new JraTextField();
  raComboBox jcbStatus = new raComboBox();

  public presProizvodnja() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private boolean initialized;

  public void resetDefaults() {
    initialized = true;
    jcbStatus.setSelectedIndex(0);
    getSelRow().setString("SERPR", "P");
    getSelRow().setString("CSKL", hr.restart.sisfun.raUser.getInstance().getDefCorg());
    jlrCorg.forceFocLost();
    getSelRow().setTimestamp("DATDOK-from", hr.restart.util.Util.getUtil().getFirstDayOfMonth());  /**@todo: Provjeri datum */
    getSelRow().setTimestamp("DATDOK-to", hr.restart.util.Valid.getValid().getToday());
  }
  
  public void SetFokus() {
    if (!initialized) resetDefaults();
    jcbStatus.setSelectedIndex(jcbStatus.getSelectedIndex());
    jcbStatus.requestFocus();
  }

  public boolean Validacija() {
    if (vl.isEmpty(jlrCorg))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setSelDataSet(dm.getRNpro());
    jpDetail.setLayout(lay);
    lay.setWidth(531);
    lay.setHeight(100);

    jlCorg.setText("Org. jedinica");
    jlStatus.setText("Status");
    jlDatumfrom.setText("Datum (od - do)");
    jraDatumfrom.setColumnName("DATDOK");
    jraDatumfrom.setDataSet(getSelDataSet());
    jraDatumto.setColumnName("DATDOK");
    jraDatumto.setDataSet(getSelDataSet());

    jraSerpr.setDataSet(getSelDataSet());
    jraSerpr.setColumnName("SERPR");
    jraSerpr.setEnabled(false);
    jraSerpr.setVisible(false);

    jlrCorg.setColumnName("CSKL");
    jlrCorg.setNavColumnName("CORG");
    jlrCorg.setDataSet(getSelDataSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jcbStatus.setRaDataSet(getSelDataSet());
    jcbStatus.setRaColumn("STATUS");
    jcbStatus.setRaItems(new String[][] {{" ",""},{"Prijavljen","P"},{"Obra\u0111en","O"},{"Zatvoren","Z"}});

    jpDetail.add(jbSelCorg, new XYConstraints(510, 20, 21, 21));
    jpDetail.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 20, 250, -1));
    jpDetail.add(jlStatus, new XYConstraints(15, 45, 100, -1));
    jpDetail.add(jcbStatus, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlDatumfrom, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jraDatumfrom, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jraDatumto, new XYConstraints(255, 70, 100, -1));
    jpDetail.add(jraSerpr, new XYConstraints(360, 70, 5, -1));

    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldk, String newk) {
        jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
    installResetButton();
    this.addSelRange(jraDatumfrom, jraDatumto);
    this.setSelPanel(jpDetail);
  }
}
