/****license*****************************************************************
**   file: presPregled.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.raComboBox;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class presPregled extends presCommonSk {
  frmPregledArhive frm;

  JLabel jlDok = new JLabel();

  JraTextField jraDatumfrom = new JraTextField();
  JraTextField jraDatumto = new JraTextField();

  raComboBox rcbDat = new raComboBox() {
    public void this_itemStateChanged() {
    }
  };
  raComboBox rcbVrsta = new raComboBox() {
    public void this_itemStateChanged() {
    }
  };

  boolean firstReset = false;

  public presPregled(frmPregledArhive fpa) {
    try {
      frm = fpa;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void resetDefaults() {
    getSelRow().setTimestamp("DATDOK-from", ut.getFirstDayOfYear());
    getSelRow().setTimestamp("DATDOK-to", vl.getToday());
    jpc.init();
    jpp.setKupci(true);
    jpp.clear();
    jpk.setKontaAllow(false);
    rcbDat.setSelectedIndex(1);
    rcbVrsta.setSelectedIndex(0);
  }
  
  public void SetFokus() {
    getSelRow().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG(false));
//    System.out.println(getSelRow().getTimestamp("DATDOK-to"));
//    System.out.println(getSelRow().getInt("CPAR"));
//    System.out.println(getSelRow().getString("CORG"));
    jpp.setKupci(jpp.isKupci());
    if (!firstReset) {
      firstReset = true;
      resetDefaults();
    }
/*    jlrCorg.setText(hr.restart.zapod.OrgStr.getKNJCORG(false));
    jlrCorg.forceFocLost(); */
    jpp.focusCombo();
  }

  public boolean Validacija() {
    if (!jpc.Validacija()) return false;
    if (!Aus.checkGKDateRange(jraDatumfrom, jraDatumto)) return false;
    return true;
  }

  private void jbInit() throws Exception {
    setSelDataSet(frm.getMasterSet());
    rcbDat.setRaItems(new String[][] {
      {"Datum dokumenta", "D"},
      {"Datum knjiženja", "K"}
    });
    rcbDat.setSelectedIndex(0);

    rcbVrsta.setRaItems(new String[][] {
      {"Svi", "S"},
      {"Ra\u010Duni", "R"},
      {"Uplate", "U"},
      {"Knjižne obavijesti", "K"},
      {"Ra\u010Duni i uplate", "O"}
    });
    rcbVrsta.setSelectedIndex(0);

    jlDok.setText("Dokumenti");

    jraDatumfrom.setColumnName("DATDOK");
    jraDatumfrom.setDataSet(getSelDataSet());
    jraDatumfrom.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumto.setColumnName("DATDOK");
    jraDatumto.setDataSet(getSelDataSet());
    jraDatumto.setHorizontalAlignment(SwingConstants.CENTER);

    jpDetail.add(rcbDat, new XYConstraints(15, 70 + dkAdd, 130, -1));
    jpDetail.add(jraDatumfrom, new XYConstraints(150, 70 + dkAdd, 100, -1));
    jpDetail.add(jraDatumto, new XYConstraints(255, 70 + dkAdd, 100, -1));
    jpDetail.add(jlDok, new XYConstraints(15, 97 + dkAdd, -1, -1));
    jpDetail.add(rcbVrsta, new XYConstraints(150, 95 + dkAdd, 100, -1));

    this.addSelRange(jraDatumfrom, jraDatumto);
    this.setSelPanel(jpDetail);
  }
  
  protected void afterPartner(boolean succ) {
    if (succ) jraDatumfrom.requestFocus();
  }
  
  public Condition getPresCondition() {
    Condition[][] dokum = {
        {Condition.in("VRDOK", "IRN UPL OKK"), Condition.in("VRDOK", "URN IPL OKD")},
        {Condition.equal("VRDOK", "IRN"), Condition.equal("VRDOK", "URN")},
        {Condition.equal("VRDOK", "UPL"), Condition.equal("VRDOK", "IPL")},
        {Condition.equal("VRDOK", "OKK"), Condition.equal("VRDOK", "OKD")},
        {Condition.in("VRDOK", "IRN UPL"), Condition.in("VRDOK", "URN IPL")}
    };    
    return super.getPresCondition().
      and(jpp.isEmpty() ? Condition.none : Condition.equal("CPAR", jpp.getCpar())).
      and(dokum[rcbVrsta.getSelectedIndex()][jpp.isKupci() ? 0 : 1]).
      and(Condition.between(rcbDat.getSelectedIndex() == 0 ? "DATDOK" : "DATUMKNJ",
              getSelRow().getTimestamp("DATDOK-from"),
              getSelRow().getTimestamp("DATDOK-to")));
  }
}
