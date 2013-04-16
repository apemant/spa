/****license*****************************************************************
**   file: presKartica.java
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

import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;


public class presKartica extends presCommonSk {

  frmKartica frm;

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

  private boolean insideCall, outsideCall, firstReset = false;

  protected presKartica() {
    this(null);
  }
  
  public presKartica(frmKartica fk) {
    try {
      frm = fk;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setInsideCall() {
    insideCall = true;
  }

  public void setForceCpar(boolean kupac, int cpar, String corg, String kon, Timestamp first, Timestamp last) {
    jpp.setKupci(kupac);
    jpp.setCpar(cpar);
    jpc.setCorg(corg);
    if (raSaldaKonti.isDirect()) {
      getSelRow().setString("BROJKONTA", kon == null ? "" : kon);
      jpk.setKonto(kon);
    }
    getSelRow().setTimestamp("DATDOK-from", first);
    getSelRow().setTimestamp("DATDOK-to", last);
    getSelRow().setInt("CPAR", cpar);
    getSelRow().setString("CORG", corg);
    getSelRow().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG(false));
    rcbDat.setSelectedIndex(1);
    rcbVrsta.setSelectedIndex(0);
    rcbStatus.setSelectedIndex(0);
    outsideCall = true;
    datDosp = false;
    firstReset = true;
//    System.out.println(getSelRow().getTimestamp("DATDOK-to"));
//    System.out.println(getSelRow().getInt("CPAR"));
//    System.out.println(getSelRow().getString("CORG"));
  }

  boolean datDosp;
  public void setForceDosp(int showMode, boolean datdok) {
    rcbVrsta.setSelectedIndex(showMode);
    rcbStatus.setSelectedIndex(1);
    datDosp = true;    
    rcbDat.setSelectedIndex(datdok ? 0 : 1);
    /*if (datdok) {
      
    }
    String god = Valid.getValid().getLastKnjigYear("gk");
    if (god != null) {
      getSelRow().setTimestamp("DATDOK-from", ut.getYearBegin(god));
    }*/
  }
  
  public void resetDefaults() {
    getSelRow().setTimestamp("DATDOK-from", ut.getYearBegin(Aus.getFreeYear()));
    getSelRow().setTimestamp("DATDOK-to", vl.getToday());
    jpc.init();
    jpp.setKupci(true);
    jpp.clear();
    jpk.setKontaAllow(false);
    rcbDat.setSelectedIndex(1);
    rcbVrsta.setSelectedIndex(0);
    rcbStatus.setSelectedIndex(0);
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
    if (!insideCall) {
      jpp.setKupci(jpp.isKupci());
/*      jlrCorg.setText(hr.restart.zapod.OrgStr.getKNJCORG(false));
      jlrCorg.forceFocLost(); */
      jpp.focusCombo();
    } else if (outsideCall) {
      rcbVrsta.requestFocus();
    } else {
      jpp.focusCpar();
    }
    insideCall = outsideCall = datDosp = false;
  }

  public boolean Validacija() {
     System.out.println("Validacija!");
    if (!jpp.Validacija() || !jpc.Validacija()) return false;
    if (rcbDat.getSelectedIndex() == 1 && 
        !Aus.checkGKDateRange(jraDatumfrom, jraDatumto)) return false;
    if (rcbDat.getSelectedIndex() == 0 &&
        !Aus.checkDateRange(jraDatumfrom, jraDatumto)) return false;
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
    //if (succ) jraDatumfrom.requestFocus();
  }

  public Condition getPresCondition() {
    Condition[][] dokum = {
        {Condition.in("VRDOK", "IRN UPL OKK"), Condition.in("VRDOK", "URN IPL OKD")},
        {Aus.getVrdokCond(true, true), Aus.getVrdokCond(false, true)},
        {Aus.getVrdokCond(true, false), Aus.getVrdokCond(false, false)},
        {Condition.equal("VRDOK", "OKK"), Condition.equal("VRDOK", "OKD")},
        {Condition.in("VRDOK", "IRN UPL"), Condition.in("VRDOK", "URN IPL")}
    };
    Condition c_hack = Condition.where("POKRIVENO", Condition.NOT_EQUAL, "X");
    Condition[] pok = {c_hack, c_hack, c_hack};
    // hack: pokrivnost/nepokrivenost se ne moze testirati ovako jer
    // treba provjeriti s cime je dokument pokriven i izlazi li to
    // izvan granica perioda. U frmKartica.
    /*Condition[] pok = {Condition.where(pcol, Condition.NOT_EQUAL, "X"),
        Condition.equal(pcol, "N"), Condition.equal(pcol, "D")
    };*/
    return super.getPresCondition().and(Condition.equal("CPAR", jpp.getCpar())).
      and(dokum[rcbVrsta.getSelectedIndex()][jpp.isKupci() ? 0 : 1]).
      and(pok[rcbStatus.getSelectedIndex()]).and(
          rcbDat.getSelectedIndex() == 0 
              ? Condition.from("DATDOK", getSelRow().getTimestamp("DATDOK-from")).
                and(Aus.getCurrGKDatumCond(getSelRow().getTimestamp("DATDOK-to")))
              : Condition.between("DATUMKNJ",
              getSelRow().getTimestamp("DATDOK-from"),
              getSelRow().getTimestamp("DATDOK-to")));
  }
}
