/****license*****************************************************************
**   file: presKarticaDev.java
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
import hr.restart.zapod.Tecajevi;
import hr.restart.zapod.jpGetValute;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;


public class presKarticaDev extends presCommonSk {

  jpGetValute jpval = new jpGetValute();

  frmKarticaDev frm;

  JLabel jlDok = new JLabel();

  JraTextField jraDatumfrom = new JraTextField();
  JraTextField jraDatumto = new JraTextField();

  raComboBox rcbVrsta = new raComboBox() {
    public void this_itemStateChanged() {
    }
  };
  raComboBox rcbStatus = new raComboBox() {
    public void this_itemStateChanged() {
    }
  };
  raComboBox rcbDat = new raComboBox() {
    public void this_itemStateChanged() {
    }
  };

  private boolean firstReset = false;

  public presKarticaDev(frmKarticaDev fkd) {
    try {
      frm = fkd;
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
    if (!firstReset) {
      firstReset = true;
      resetDefaults();
    }
//    System.out.println(getSelRow().getTimestamp("DATDOK-to"));
//    System.out.println(getSelRow().getInt("CPAR"));
//    System.out.println(getSelRow().getString("CORG"));
    jpp.setKupci(jpp.isKupci());
/*      jlrCorg.setText(hr.restart.zapod.OrgStr.getKNJCORG(false));
      jlrCorg.forceFocLost(); */
      jpp.focusCombo();
  }

  public boolean Validacija() {
     System.out.println("Validacija!");
    if (!jpp.Validacija() || !jpc.Validacija()) return false;
    if (!Aus.checkGKDateRange(jraDatumfrom, jraDatumto)) return false;
    return true;
  }

  private void jbInit() throws Exception {
    setSelDataSet(frm.getRaQueryDataSet());
    rcbDat.setRaItems(new String[][] {
      {"Datum dokumenta", "D"},
      {"Datum knjiženja", "K"}
    });
    rcbDat.setSelectedIndex(1);

    rcbVrsta.setRaItems(new String[][] {
      {"Kartica", "S"},
      {"Ra\u010Duni", "R"},
      {"Uplate", "U"},
      {"Knjižne obavijesti", "K"},
      {"Ra\u010Duni i uplate", "O"}
    });
    rcbVrsta.setSelectedIndex(0);

    rcbStatus.setRaItems(new String[][] {
      {"Svi", "S"},
      {"Otvoreni", "O"},
      {"Zatvoreni", "Z"}
    });
    rcbStatus.setSelectedIndex(0);

    jlDok.setText("Dokumenti");

    jraDatumfrom.setColumnName("DATDOK");
    jraDatumfrom.setDataSet(getSelDataSet());
    jraDatumfrom.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumto.setColumnName("DATDOK");
    jraDatumto.setDataSet(getSelDataSet());
    jraDatumto.setHorizontalAlignment(SwingConstants.CENTER);
    
    lay.setHeight(180 + dkAdd);
    jpval.setAlwaysSelected(true);
    jpval.setTecajVisible(false);
    
    jpDetail.add(jpval, new XYConstraints(0, 130 + dkAdd, -1, -1));
    jpDetail.add(rcbDat, new XYConstraints(15, 70 + dkAdd, 130, -1));
    jpDetail.add(jraDatumfrom, new XYConstraints(150, 70 + dkAdd, 100, -1));
    jpDetail.add(jraDatumto, new XYConstraints(255, 70 + dkAdd, 100, -1));
    jpDetail.add(jlDok, new XYConstraints(15, 97 + dkAdd, -1, -1));
    jpDetail.add(rcbVrsta, new XYConstraints(150, 95 + dkAdd, 100, -1));
    jpDetail.add(rcbStatus, new XYConstraints(255, 95 + dkAdd, 100, -1));

    this.addSelRange(jraDatumfrom, jraDatumto);
    this.setSelPanel(jpDetail);
//    sysoutTEST sys = new sysoutTEST(false);
//    sys.prn(getSelRow());
  }
  
  protected void afterPartner(boolean succ) {
    if (succ) jraDatumfrom.requestFocus();
  }

  public Condition getPresCondition() {
    Condition[][] dokum = {
        {Condition.in("VRDOK", "IRN UPL OKK"), Condition.in("VRDOK", "URN IPL OKD")},
        {Condition.raw("VRDOK='IRN' OR (VRDOK='OKK' AND ID != 0)"),
         Condition.raw("VRDOK='URN' OR (VRDOK='OKD' AND IP != 0)")},
        {Condition.raw("VRDOK='UPL' OR (VRDOK='OKK' AND IP != 0)"),
         Condition.raw("VRDOK='IPL' OR (VRDOK='OKD' AND ID != 0)")},
        {Condition.equal("VRDOK", "OKK"), Condition.equal("VRDOK", "OKD")},
        {Condition.in("VRDOK", "IRN UPL"), Condition.in("VRDOK", "URN IPL")}
    };
    Condition c_hack = Condition.where("PVPOK", Condition.NOT_EQUAL, "X");
    Condition[] pok = {c_hack, c_hack, c_hack};
    // hack: pokrivnost/nepokrivenost se ne moze testirati ovako jer
    // treba provjeriti s cime je dokument pokriven i izlazi li to
    // izvan granica perioda. U frmKartica.
    /*Condition[] pok = {Condition.where(pcol, Condition.NOT_EQUAL, "X"),
        Condition.equal(pcol, "N"), Condition.equal(pcol, "D")
    };*/
    Condition val = Condition.equal("OZNVAL", jpval.jtOZNVAL.getText()); 
    if (jpval.jtOZNVAL.getText().length() == 0) {
      /*String dval = Tecajevi.getDomOZNVAL();
      if (dval == null || dval.length() == 0)
        val = Condition.where("OZNVAL", Condition.NOT_EQUAL, "").andNotNull();
      else val = Condition.where("OZNVAL", Condition.NOT_EQUAL, "").
        and(Condition.where("OZNVAL", Condition.NOT_EQUAL, dval)).andNotNull("OZNVAL");*/
      val = Condition.none;
    }
    return super.getPresCondition().and(Condition.equal("CPAR", jpp.getCpar())).
      and(dokum[rcbVrsta.getSelectedIndex()][jpp.isKupci() ? 0 : 1]).
      and(pok[rcbStatus.getSelectedIndex()]).and(Condition.between(
          rcbDat.getSelectedIndex() == 0 ? "DATDOK" : "DATUMKNJ",
              getSelRow().getTimestamp("DATDOK-from"),
              getSelRow().getTimestamp("DATDOK-to"))).and(val);
  }

}
