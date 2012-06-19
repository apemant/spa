/****license*****************************************************************
**   file: frmIspKartica.java
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
package hr.restart.gk;

import hr.restart.swing.JraTextField;
import hr.restart.util.raImages;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.jbcl.layout.XYConstraints;

public class frmIspKartica extends frmKarticeGK {

//  raPanKonto kontoPanel = new raPanKonto();

  public frmIspKartica(){
    super(true);
    this.getContentPane().remove(getJPTV());
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  JraTextField jtfPocKonto = new JraTextField()/*{
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      checkBrKonOdDo("ODKONTA","0");
    }
  }*/;

  JraTextField jtfZavKonto = new JraTextField()/*{
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      checkBrKonOdDo("DOKONTA","9");
    }
  }*/;

  JLabel jlPeriodKonto = new JLabel();

  public void componentShow() {
//    super.componentShow();
    initialValues();
    clearKontoFields();
    setZakljucnoKnjizenje("N");
  }

  public void initialValues() {
//    setSortPanelLabelLaf();
    if (solo) {
      stds.setTimestamp("pocDatum", hr.restart.robno.Util.getUtil().findFirstDayOfYear());
      stds.setTimestamp("zavDatum", hr.restart.util.Util.getUtil().getLastDayOfMonth());
//      kontoPanel.jlrCorg.setText(knjigDifolt);
//      kontoPanel.jlrCorg.forceFocLost();

//      jcbPrivremeni.setSelected(false);

//      rcbPrivremenost.setSelectedIndex(0);
//      stds.setString("Privremeno","0");
      setPripadnostOJ("0");
      setPrivremeno("0");
      setSaldo("N");

//      jcbSaldo.setSelected(false);
      kontoPanel.setcORG(knjigDifolt);
//      rcc.setLabelLaF(kontoPanel.jlrCorg, false);
//      rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
//      rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
//      kontoPanel.jlrKontoBroj.setText("");
//      kontoPanel.jlrKontoNaziv.setText("");
//      kontoPanel.setBrojKonta("");
      kontoPanel.jlrKontoBroj.setText("");
      kontoPanel.jlrKontoBroj.emptyTextFields();
//      buttonGroup1.setSelected(jrbDatKnjiz);
      setSljed("K");
      setDonos("N");
      outSet.empty();
      outSet.close();
      this.getJPTV().setDataSet(null);
      kontoPanel.jlrKontoBroj.requestFocus();
    } else {
      if (Validacija()) okPress();
      this.getOKPanel().jBOK.setText("Ispis");
      this.getOKPanel().jBOK.setIcon(raImages.getImageIcon(raImages.IMGPRINT));
    }
  }

  private void clearKontoFields(){
    jtfPocKonto.setText("");
    stds.setString("ODKONTA","");
    jtfZavKonto.setText("");
    stds.setString("DOKONTA","");
  }

  public boolean runFirstESC() {
    boolean combine = false;
    if (!jtfPocKonto.getText().equals("")) {
      jtfPocKonto.setText("");
      jtfZavKonto.setText("");


      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          jtfPocKonto.requestFocus();
        }
      });
      
      combine = true;
    }
    return combine;
  }

  public void firstESC() {
    System.out.println("okpress " + okpresssed);
    if (solo) {
      rcc.EnabDisabAll(jpDetail, true);
//    setSortPanelLabelLaf();
//    rcc.setLabelLaF(kontoPanel.jlrCorg, false);
//    rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
//    rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
    kontoPanel.jlrKontoBroj_lookup();
    kontoPanel.jlrKontoBroj.requestFocus();
    outSet.empty();
    outSet.close();
    killAllReports();
    this.getJPTV().setDataSet(null);
//      showDefaultValues();
    if (!okpresssed){
//      kontoPanel.setcORG(hr.restart.zapod.OrgStr.getKNJCORG());
//      kontoPanel.jlrKontoBroj.setText("");
//      kontoPanel.jlrKontoBroj.emptyTextFields();
//      rcc.setLabelLaF(kontoPanel.jlrCorg, false);
//      rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
//      rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
    } else okpresssed = false;
    
    } else {
      solo = true;
      super.cancelPress();
    }
  }

  public void showDefaultValues() {
    if (!okpresssed){
      kontoPanel.setcORG(hr.restart.zapod.OrgStr.getKNJCORG());
      clearKontoFields();
    } else {
      setPrivremeno("0");
      setSaldo("N");
      setSljed("K");
      setZakljucnoKnjizenje("N");
      okpresssed = false;
    }
    rcc.EnabDisabAll(jpDetail, true);
    rcc.EnabDisabAll(kontoPanel, true);
//    rcc.setLabelLaF(kontoPanel.jlrCorg, false);
//    rcc.setLabelLaF(kontoPanel.jlrNazorg, false);
//    rcc.setLabelLaF(kontoPanel.jbSelCorg, false);
    jtfPocKonto.requestFocus();
    outSet.empty();
    outSet.close();
  }

  private void jbInit() throws Exception {

    rcbZakKnj.setDataSet(stds);
    rcbZakKnj.setRaColumn("ZAKLJKNJ");
    rcbZakKnj.setRaItems(new String[][] {
      {"Bez zakljuènog knjiženja","N"},
      {"Sa zakljuènim knjiženjem","D"}
    });

    jtfPocKonto.setHorizontalAlignment(SwingConstants.LEFT);
    jtfPocKonto.setDataSet(stds);
    jtfPocKonto.setColumnName("ODKONTA");

    jtfZavKonto.setHorizontalAlignment(SwingConstants.LEFT);
    jtfZavKonto.setDataSet(stds);
    jtfZavKonto.setColumnName("DOKONTA");

    jlPeriodKonto.setText("Broj konta (od - do)");
    jlSljed.setText("Sljed / Zaklj. knj.");
    
    kontoPanel.setNoLookup(true);

    kontoPanel.remove(kontoPanel.jlKonto);
    kontoPanel.remove(kontoPanel.jlrKontoBroj);
    kontoPanel.remove(kontoPanel.jlrKontoNaziv);
    kontoPanel.remove(kontoPanel.jbSelBrKon);

    kontoPanel.add(jlPeriodKonto,  new XYConstraints(0, 25, -1, -1));
    kontoPanel.add(jtfPocKonto,  new XYConstraints(135, 25, 100, -1));
    kontoPanel.add(jtfZavKonto,  new XYConstraints(240, 25, 100, -1));

    jpDetail.add(rcbZakKnj, new XYConstraints(360, 120, 180, -1));
//    jpDetail.add(new JLabel("Sljed / Zaklj. knj."), new XYConstraints(15, 120, 100, -1));

    kontoPanel.add(kontoPanel.jlCorg, new XYConstraints(0, 0, -1, -1));
    kontoPanel.add(kontoPanel.jlrCorg, new XYConstraints(135, 0, 100, -1));
    kontoPanel.add(kontoPanel.jlrNazorg,  new XYConstraints(240, 0, 285, -1));
    kontoPanel.add(kontoPanel.jbSelCorg,  new XYConstraints(530, 0, 21, 21));
  }

  public boolean Validacija(){
    if (vl.isEmpty(jtfPocKonto) || vl.isEmpty(jtfZavKonto)) return false;
//    System.out.println("Integer.parseInt(jtfPocKonto.getText()) - " + Integer.parseInt(jtfPocKonto.getText()));
//    System.out.println("Integer.parseInt(jtfZavKonto.getText()) - " + Integer.parseInt(jtfZavKonto.getText()));
//    if (Integer.parseInt(jtfPocKonto.getText()) > Integer.parseInt(jtfZavKonto.getText())) {
//      System.out.println("pocetni veci od krajnjeg");
//      return false;
//    }
//    if(!vl.isValidRange(jtfPocDatum,jtfZavDatum)) return false;
    return true;
  }

  public void okPress() {
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(stds);
    super.okPress();
//    System.out.println("radi li SADA ovaj select report????");
    killAllReports();
    if (stds.getString("SALDO").equals("D")) { //jcbSaldo.isSelected()) {
//      this.addReport("hr.restart.gk.repKarticeGK_Saldirana", "Ispis izvještaja konto kartica", 2);
      this.addReport("hr.restart.gk.repKarticeGK_Saldirana","hr.restart.gk.repKarticeGK_Saldirana","KarticeGK_Saldirana", "Ispis izvještaja konto kartica");
      this.addReport("hr.restart.gk.repKarticeGK_Saldirana_single","hr.restart.gk.repKarticeGK_Saldirana","KarticeGK_Saldirana_single", "Ispis izvještaja konto kartica - kompaktno");
    } else {
//      this.addReport("hr.restart.gk.repKarticeGK", "Ispis izvještaja konto kartica", 2);
      this.addReport("hr.restart.gk.repKarticeGK", "hr.restart.gk.repKarticeGK", "KarticeGK", "Ispis izvještaja konto kartica");
      this.addReport("hr.restart.gk.repKarticeGK_single", "hr.restart.gk.repKarticeGK", "KarticeGK_single", "Ispis izvještaja konto kartica - kompaktno");
    }
    this.addReport("hr.restart.gk.repKarticeGKpar", "hr.restart.gk.repKarticeGKpar", "KarticeGKpartneri", "Ispis izvještaja konto kartica po partnerima");
    this.addReport("hr.restart.gk.repKarticeGKcorg", "hr.restart.gk.repKarticeGKcorg", "KarticeGKcorg", "Ispis izvještaja konto kartica po org. jedinicama");
  }


  void checkBrKonOdDo(String brkonta, String fillChar){
    if(stds.getString(brkonta).length()<6 && stds.getString(brkonta).length()>0) {
      int kr = 6 - stds.getString(brkonta).length();
      String tmkon = stds.getString(brkonta);
      for (int i=1;i<=kr ;i++ ) {
        tmkon = tmkon+fillChar;
      }
      stds.setString(brkonta, tmkon);
    }
  }

  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow(){
    return true;
  }
}