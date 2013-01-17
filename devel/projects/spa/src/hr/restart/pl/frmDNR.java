/****license*****************************************************************
**   file: frmDNR.java
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
package hr.restart.pl;

import hr.restart.baza.Radnici;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmDNR extends raUpitLite {

  dM dm = dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  hr.restart.zapod.OrgStr orgs;
  Util ut = Util.getUtil();

  JPanel mainPanel = new JPanel();

  StorageDataSet fieldSet = new StorageDataSet();
  QueryDataSet repSet = new QueryDataSet();
  QueryDataSet knjigovodstvo;
  QueryDataSet corgradnici = new QueryDataSet();
  String oldcorg = "";
  int brr;

  Column colRadnikOd = new Column();
  Column colRadnikDo = new Column();
  Column colDatumOd = new Column();
  Column colDatumDo = new Column();
  Column colCorg = new Column();

  JraTextField jraDatOd = new JraTextField(){
    public void valueChanged() {
      fieldSet.setTimestamp("DATISPLOD", ut.getFirstSecondOfDay(fieldSet.getTimestamp("DATISPLOD")));
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      fieldSet.setTimestamp("DATISPLOD", ut.getFirstSecondOfDay(fieldSet.getTimestamp("DATISPLOD")));
    }*/
  };

  JraTextField jraDatDo = new JraTextField(){
    public void valueChanged() {
      fieldSet.setTimestamp("DATISPLDO", ut.getLastSecondOfDay(fieldSet.getTimestamp("DATISPLDO")));
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      fieldSet.setTimestamp("DATISPLDO", ut.getLastSecondOfDay(fieldSet.getTimestamp("DATISPLDO")));
    }*/
  };

  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {aft_lookCorg();}
  };
  JlrNavField jlrNazorg = new JlrNavField() {
    public void after_lookUp() {aft_lookCorg();}
  };
  JlrNavField jlrCradnikOd = new JlrNavField() {
    public void after_lookUp() {
      if (jlrCradnikDo.getText().equals("")) {
        jlrCradnikDo.setText(jlrCradnikOd.getText());
        jlrCradnikDo.forceFocLost();
      }
    }
  };
  JlrNavField jlrCradnikDo = new JlrNavField() {
    public void after_lookUp() {
      }
  };
  JlrNavField jlrPrezime = new JlrNavField();
  JlrNavField jlrIme = new JlrNavField();
  JlrNavField jlrPrezime2 = new JlrNavField();
  JlrNavField jlrIme2 = new JlrNavField();

  JraButton jbSelCorg = new JraButton();
  JraButton jbSelCradnikOd = new JraButton();
  JraButton jbSelCradnikDo = new JraButton();
  JLabel jlCorg = new JLabel();
  JLabel jlRadnikOd = new JLabel();
  JLabel jlRadnikDo = new JLabel();
  JLabel jlPeriod = new JLabel();

  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel jPanel1 = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();

  static frmDNR thisFrmDNR;

  public frmDNR() {
    try {
      jbInit();
      thisFrmDNR = this;
    }
    catch (Exception ex) {

    }
  }

  public static frmDNR getInstance(){
    return thisFrmDNR;
  }

  public void componentShow() {
    difolt();
  }

  private void difolt() {
    fieldSet.emptyAllRows();

    fieldSet.setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
    /*if (!oldcorg.equals(hr.restart.zapod.OrgStr.getKNJCORG()))*/ jlrCorg.forceFocLost();

    jlrCradnikDo.setText("");
    jlrCradnikDo.forceFocLost();

    jlrCradnikOd.setText("");
    jlrCradnikOd.forceFocLost();
    java.sql.Timestamp lastyear = new java.sql.Timestamp(vl.getNowMS()-(1000*60*60*24*365l));
    fieldSet.setTimestamp("DATISPLOD", ut.getFirstSecondOfDay(ut.getFirstDayOfYear(lastyear)));
    fieldSet.setTimestamp("DATISPLDO", ut.getLastSecondOfDay(ut.getLastDayOfYear(lastyear)));

    rcc.setLabelLaF(jlrIme, false);
    rcc.setLabelLaF(jlrPrezime, false);
    rcc.setLabelLaF(jlrIme2, false);
    rcc.setLabelLaF(jlrPrezime2, false);

    jlrCorg.requestFocus();
    jlrCorg.selectAll();
  }

  public void firstESC() {
    rcc.EnabDisabAll(jPanel1, true);
    difolt();
  }

  public boolean runFirstESC() {  /** @todo 2B || !2B */
    if (!jlrCradnikOd.getText().equals("") || !jlrCradnikDo.getText().equals("")){
      return true;
    }
    return false;
  }

  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow() {
    return true;
  }

  public String getKnjigovodstvoSQL() {
    String knjigovodstvo = "SELECT Orgstruktura.naziv, Orgstruktura.mjesto, Orgstruktura.adresa, Orgstruktura.hpbroj, " +
    		(raObracunPL.isOIB()?"Logotipovi.oib":"Logotipovi.matbroj") +" AS MATBROJ " +
                           "FROM Orgstruktura, Logotipovi "+
                           "WHERE orgstruktura.corg = logotipovi.corg and Orgstruktura.corg ='" + orgs.getKNJCORG() + "'"; // fieldSet.getString("CORG") + "'";
    return knjigovodstvo;
  }

  public String getRepQdsString() {
    String qstr = "select kumulorgarh.godobr, kumulorgarh.mjobr, kumulorgarh.rbrobr,"+
                  " kumulorgarh.datumispl, kumulradarh.bruto, kumulradarh.doprinosi,"+ // odakle se vade ( i zbrajaju ) doprinosi???
                  " kumulradarh.iskneop, kumulradarh.porosn,"+
                  " kumulradarh.por1, kumulradarh.por2, kumulradarh.por3,"+
                  " kumulradarh.por4, kumulradarh.por5, kumulradarh.poruk,"+
                  " kumulradarh.prir, kumulradarh.poriprir,"+
                  " radnici.ime, radnici.prezime, radnici.cradnik, radnicipl.jmbg, radnicipl.oib,"+
                  " radnicipl.adresa," +
                  " kumulradarh.copcine, kumulradarh.netopk"+//ip
                  " from Kumulorgarh, Kumulradarh, Radnici, Radnicipl"+
                  " WHERE kumulorgarh.godobr = kumulradarh.godobr"+
                  " AND kumulorgarh.mjobr = kumulradarh.mjobr"+
                  " AND kumulorgarh.rbrobr = kumulradarh.rbrobr"+
                  " AND kumulradarh.cvro = kumulorgarh.cvro"+
                  " AND kumulradarh.corg = kumulorgarh.corg"+
                  " AND kumulradarh.cradnik = radnici.cradnik"+
//                  " AND radnici.corg = kumulorgarh.corg"+
                  " AND kumulradarh.cradnik = radnicipl.cradnik"+
                  " AND radnici.cradnik = radnicipl.cradnik"+
//                  " AND radnicipl.cvro = kumulorgarh.cvro"+
//                  " AND radnicipl.corg = kumulorgarh.corg"+
                  " and kumulorgarh.datumispl between '"+ fieldSet.getTimestamp("DATISPLOD") + "' and '" + ut.getLastSecondOfDay(fieldSet.getTimestamp("DATISPLDO")) + "'" +
                  " and (kumulradarh.corg in " + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG")),"kumulradarh.corg")+" OR kumulradarh.corg in " + orgs.getInQuery(orgs.getOrgstrAndKnjig(frmID.getOjWith()),"kumulradarh.corg")+")";
    String nadoprc = " and kumulradarh.cradnik between '"+ fieldSet.getInt("CRADNIKOD") + "' and '" + fieldSet.getInt("CRADNIKDO") + "'";
    if (!jlrCradnikOd.getText().equals("")) return qstr.concat(nadoprc);
    qstr+=" ORDER BY radnici.ime, radnici.prezime, radnici.cradnik, kumulorgarh.godobr, kumulorgarh.mjobr, kumulorgarh.rbrobr";
    System.out.println(qstr);
    return qstr;
  }

  public boolean Validacija(){
    return !hr.restart.util.Valid.getValid().isEmpty(jlrCorg);
//
//    if (fieldSet.getInt("CRADNIKOD") > fieldSet.getInt("CRADNIKDO")){
//      int i= fieldSet.getInt("CRADNIKOD");
//      fieldSet.setInt("CRADNIKOD", fieldSet.getInt("CRADNIKDO"));
//      jlrCradnikOd.forceFocLost();
//      fieldSet.setInt("CRADNIKDO", i);
//      jlrCradnikDo.forceFocLost();
//    }
//    knjigovodstvo = ut.getNewQueryDataSet(getKnjigovodstvoSQL());
//
//    System.out.println(getRepQdsString());
//
//    repSet = ut.getNewQueryDataSet(getRepQdsString(),false);
//    return true;
  }

//  private String getWhereSQL( short godina, short mjesec, short rbr, String cradnika, String table) {
//    String neki = " and ";
//    table = new java.util.StringTokenizer(table,",").nextToken();
//
//    neki = neki.concat(raPlObrRange.getInQueryIsp(godina, mjesec, table.trim())) + " and ";
//
//    return neki.concat(" corg in ").concat(
//        hr.restart.zapod.OrgStr.getOrgStr().getInQuery(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig()));
//  }

  protected java.math.BigDecimal getHartAttack(short godina, short mjesec, short rbr, String cradnika, String rmiParam){
    if (rmiParam.equals("")) return new java.math.BigDecimal(0);
    String pok = "select OBRIZNOS as OBRIZNOS from Odbiciarh " +
                 "where cradnik ='" + cradnika + "' and godobr*10000+mjobr*100+rbrobr = "+
                 (godina*10000+mjesec*100+rbr) + " " +
                 rmiParam;
//    if (godina == 2002 && mjesec == 12 && rbr == 1){
//      System.out.println("\n"+pok);
//    }

    QueryDataSet tidamtidamtidam = ut.getNewQueryDataSet(pok);
    tidamtidamtidam.first();
    java.math.BigDecimal sumum = new java.math.BigDecimal(0);
    do {
      sumum = sumum.add(tidamtidamtidam.getBigDecimal("OBRIZNOS"));
    } while (tidamtidamtidam.next());
    return sumum;
  }

  public void okPress() {
    getOKPanel().jPrekid.requestFocus();
    disejblajPanel();
    if (fieldSet.getInt("CRADNIKOD") > fieldSet.getInt("CRADNIKDO")){
      int i= fieldSet.getInt("CRADNIKOD");
      fieldSet.setInt("CRADNIKOD", fieldSet.getInt("CRADNIKDO"));
      jlrCradnikOd.forceFocLost();
      fieldSet.setInt("CRADNIKDO", i);
      jlrCradnikDo.forceFocLost();
    }
    knjigovodstvo = ut.getNewQueryDataSet(getKnjigovodstvoSQL());

//    System.out.println(getRepQdsString());

    repSet = ut.getNewQueryDataSet(getRepQdsString(),false);

    repSet.addColumn(dm.createBigDecimalColumn("DOP1"));
    repSet.addColumn(dm.createBigDecimalColumn("DOP2"));
    repSet.addColumn(dm.createBigDecimalColumn("OSIG"));
    repSet.setMetaDataUpdate(repSet.getMetaDataUpdate() & ~MetaDataUpdate.ROWID);
    repSet.open();
    repSet.getColumn("CRADNIK").setRowId(true);
    repSet.first();
    do {
      repSet.setBigDecimal("DOP1", getHartAttack(
          repSet.getShort("GODOBR"),
          repSet.getShort("MJOBR"),
          repSet.getShort("RBROBR"),
          repSet.getString("CRADNIK"),odbiciWh(raIzvjestaji.ID_1_1)));

      if (repSet.getShort("GODOBR") <= 2002){
//        System.out.println("u 2002-goj smo");
        repSet.setBigDecimal("DOP1", repSet.getBigDecimal("DOP1").add(getHartAttack(
            repSet.getShort("GODOBR"),
            repSet.getShort("MJOBR"),
            repSet.getShort("RBROBR"),
            repSet.getString("CRADNIK"),odbiciWh(raIzvjestaji.ID_1_2002))));
      }

      repSet.setBigDecimal("DOP2", getHartAttack(
          repSet.getShort("GODOBR"),
          repSet.getShort("MJOBR"),
          repSet.getShort("RBROBR"),
          repSet.getString("CRADNIK"),odbiciWh(raIzvjestaji.ID_1_2)));
//      /** @todo ode nesto nevalja..........
//      ? andrej
/*new java.math.BigDecimal("0");*/

      java.math.BigDecimal osig_ =  getHartAttack(
          repSet.getShort("GODOBR"),
          repSet.getShort("MJOBR"),
          repSet.getShort("RBROBR"),
          repSet.getString("CRADNIK"),odbiciWh(raIzvjestaji.ID_3_1)).add(
          getHartAttack(
          repSet.getShort("GODOBR"),
          repSet.getShort("MJOBR"),
          repSet.getShort("RBROBR"),
          repSet.getString("CRADNIK"),odbiciWh(raIzvjestaji.ID_3_2))).add(
          getHartAttack(
          repSet.getShort("GODOBR"),
          repSet.getShort("MJOBR"),
          repSet.getShort("RBROBR"),
          repSet.getString("CRADNIK"),odbiciWh(raIzvjestaji.ID_3_3)))/*.add(
          getHartAttack(
          repSet.getShort("GODOBR"),
          repSet.getShort("MJOBR"),
          repSet.getShort("RBROBR"),
          repSet.getString("CRADNIK"),odbiciWh(raIzvjestaji.ID03_3_6)))*/;
      repSet.setBigDecimal("OSIG",osig_);
    } while (repSet.next());

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(repSet);
//    System.out.println("\n"+repSet.getOriginalQueryString()+"\n");
    setBrojRadnika();
//    SwingUtilities.invokeLater(new Runnable() {
//      public void run() {
//
//        rcc.EnabDisabAll(jPanel1, false);
//      }
//    });
//hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//syst.prn(repSet);
  }

  protected void disejblajPanel() {
    rcc.EnabDisabAll(jPanel1, false);
  }

  protected String odbiciWh(short[] id){
    String rigowq = raIzvjestaji.getOdbiciWhQueryIzv(id);
    if (!rigowq.equals("")){
        return " and ".concat(rigowq);
    } else {
      return "";
    }
  }

  private void jbInit() throws Exception {
    orgs = hr.restart.zapod.OrgStr.getOrgStr();
//    addJasper("repIPP", "hr.restart.pl.repIPP", "ipp.jrxml", "IPP Obrazac");

    this.addJasper("repDNR11","hr.restart.pl.repDNR", "dnr11.jrxml", "Obrazac DNR 2011");
    this.addJasper("repPOTVRDA","hr.restart.pl.repDNR","potvrda.jrxml", "POTVRDA o isplaæenom primitku");
    this.addJasper("repDNR","hr.restart.pl.repDNR", "dnr.jrxml", "Obrazac DNR");
//    this.addReport("hr.restart.pl.repDNR", "DNR", 2);

    colRadnikOd = dm.createIntColumn("CRADNIKOD");
    colRadnikDo = dm.createIntColumn("CRADNIKDO");
    colDatumOd = dm.createTimestampColumn("DATISPLOD");
    colDatumDo = dm.createTimestampColumn("DATISPLDO");
    colCorg = dm.createStringColumn("CORG",0);

    try {
      fieldSet.setColumns(new Column[] {colCorg, colRadnikOd, colRadnikDo, colDatumOd, colDatumDo});
      fieldSet.open();
    }
    catch (Exception ex) {
    }

    jbSelCradnikDo.setText("...");
    jbSelCradnikOd.setText("...");
    jbSelCorg.setText("...");
    jlCorg.setText("Org. jedinica");
    jlRadnikOd.setText("Od radnika");
    jlRadnikDo.setText("Do radnika");
    jlPeriod.setText("Za period");

    bindJlrFields();

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fieldSet);
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazorg});
    jlrCorg.setVisCols(new int[] {0, 1, 2});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNazorg.setSearchMode(1);
    jlrNazorg.setNavProperties(jlrCorg);
    jlrNazorg.setColumnName("NAZIV");

    jraDatOd.setDataSet(fieldSet);
    jraDatOd.setColumnName("DATISPLOD");

    jraDatDo.setDataSet(fieldSet);
    jraDatDo.setColumnName("DATISPLDO");

    jraDatOd.setDataSet(fieldSet);
    jraDatOd.setColumnName("DATISPLOD");

    jraDatDo.setDataSet(fieldSet);
    jraDatDo.setColumnName("DATISPLDO");

    jlrIme.setDataSet(fieldSet);
    jlrIme.setColumnName("IMOD");

    jlrPrezime.setDataSet(fieldSet);
    jlrPrezime.setColumnName("PROD");

    mainPanel.setLayout(borderLayout1);
    this.setJPan(mainPanel);
    jPanel1.setLayout(xYLayout1);
    xYLayout1.setHeight(125);
    xYLayout1.setWidth(600);
    jPanel1.add(jlCorg, new XYConstraints(15,15,-1,-1));
    jPanel1.add(jlrCorg, new XYConstraints(150, 15, 100, -1));
    jPanel1.add(jlrNazorg, new XYConstraints(255, 15, 300, -1));
    jPanel1.add(jbSelCorg, new XYConstraints(560, 15, 21, 21));
    jPanel1.add(jlRadnikOd, new XYConstraints(15,40,-1,-1));
    jPanel1.add(jlrCradnikOd, new XYConstraints(150,40,100,-1));

    jPanel1.add(jlrPrezime, new XYConstraints(255,40,150,-1));
    jPanel1.add(jlrIme, new XYConstraints(410,40,145,-1));

    jPanel1.add(jbSelCradnikOd, new XYConstraints(560, 40, 21, 21));
    jPanel1.add(jlRadnikDo, new XYConstraints(15,65,-1,-1));
    jPanel1.add(jlrCradnikDo, new XYConstraints(150,65,100,-1));

    jPanel1.add(jlrPrezime2, new XYConstraints(255,65,150,-1));
    jPanel1.add(jlrIme2, new XYConstraints(410,65,145,-1));

    jPanel1.add(jbSelCradnikDo, new XYConstraints(560, 65, 21, 21));
    jPanel1.add(jlPeriod, new XYConstraints(15,90,-1,-1));
    jPanel1.add(jraDatOd, new XYConstraints(150, 90, 100, -1));
    jPanel1.add(jraDatDo, new XYConstraints(255, 90, 100, -1));
    mainPanel.add(jPanel1, BorderLayout.CENTER);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        orgs = hr.restart.zapod.OrgStr.getOrgStr();
      }
    });

  }

  private void bindJlrFields() {
    jlrCradnikOd.setHorizontalAlignment(SwingConstants.LEFT);
    jlrCradnikOd.setColumnName("CRADNIKOD");
    jlrCradnikOd.setNavColumnName("CRADNIK");
    jlrCradnikOd.setDataSet(fieldSet);
    jlrCradnikOd.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnikOd.setTextFields(new javax.swing.text.JTextComponent[] {jlrIme, jlrPrezime});
    jlrCradnikOd.setVisCols(new int[] {0, 1, 2});
    jlrCradnikOd.setSearchMode(0);
    jlrCradnikOd.setRaDataSet(corgradnici);
    jlrCradnikOd.setNavButton(jbSelCradnikOd);

    jlrIme.setSearchMode(1);
    jlrIme.setNavProperties(jlrCradnikOd);
    jlrIme.setColumnName("IME");

    jlrPrezime.setSearchMode(1);
    jlrPrezime.setNavProperties(jlrCradnikOd);
    jlrPrezime.setColumnName("PREZIME");

    jlrCradnikDo.setHorizontalAlignment(SwingConstants.LEFT);
    jlrCradnikDo.setColumnName("CRADNIKDO");
    jlrCradnikDo.setNavColumnName("CRADNIK");
    jlrCradnikDo.setDataSet(fieldSet);
    jlrCradnikDo.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnikDo.setTextFields(new javax.swing.text.JTextComponent[] {jlrIme2, jlrPrezime2});
    jlrCradnikDo.setVisCols(new int[] {0, 1, 2});
    jlrCradnikDo.setSearchMode(0);
    jlrCradnikDo.setRaDataSet(corgradnici);
    jlrCradnikDo.setNavButton(jbSelCradnikDo);

    jlrIme2.setColumnName("IME");
    jlrPrezime2.setColumnName("PREZIME");
  }

  public DataSet getRepSet(){
    return repSet;
  }

  public String getKnjNaziv(){
    return knjigovodstvo.getString("NAZIV");
  }

  public String getKnjAdresa(){
    return knjigovodstvo.getString("ADRESA");
  }

  public String getKnjHpBroj(){
    return knjigovodstvo.getString("HPBROJ");
  }

  public String getKnjMjesto(){
    return knjigovodstvo.getString("MJESTO");
  }

  public String getKnjZiro(){
    return knjigovodstvo.getString("ZIRO");
  }

  public String getKnjMatbroj(){
    return knjigovodstvo.getString("MATBROJ");
  }

  public String getKnjSifdjel(){
    return knjigovodstvo.getString("SIFDJEL");
  }

  public void setBrojRadnika(){
    String a1 = "select count(distinct cradnik) as br " +
                "from Radnici "+
                "WHERE (corg in " + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG")))+")";
    String a2 = "AND cradnik between '"+ fieldSet.getInt("CRADNIKOD") + "' and '" + fieldSet.getInt("CRADNIKDO") + "'";
    String a;
    if (!jlrCradnikOd.getText().equals("")) a = a1.concat(a2);
    else a = a1;
    QueryDataSet alagada = ut.getNewQueryDataSet(a);
    brr = alagada.getInt("BR");
  }

  public int getBrojRadnika(){
    return brr;
  }

  public void aft_lookCorg() {
    if (!jlrCorg.getText().equals(oldcorg)) {
      jlrCradnikDo.setText("");
      jlrCradnikOd.setText("");
      oldcorg = jlrCorg.getText();
      corgradnici.close();
      Radnici.getDataModule().setFilter(corgradnici,
        "corg in" + orgs.getInQuery(orgs.getOrgstrAndKnjig(oldcorg))
      );
      corgradnici.open();
    }
  }
}