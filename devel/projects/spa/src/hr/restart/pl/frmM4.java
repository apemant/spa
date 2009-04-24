/****license*****************************************************************
**   file: frmM4.java
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

import hr.restart.util.lookupData;

import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmM4 extends frmIzvjestajiPL{
  hr.restart.util.Util ut = new hr.restart.util.Util();

  lookupData ld = lookupData.getlookupData();
  java.math.BigDecimal NULA = new java.math.BigDecimal("0");

  QueryDataSet basic;
  QueryDataSet basic2;
  QueryDataSet hzzo;
  QueryDataSet rh;
  QueryDataSet ss;
  QueryDataSet hzzoB;
  QueryDataSet rhB;
  QueryDataSet ssB;
  QueryDataSet oosig;
  QueryDataSet podoobv;
  QueryDataSet dopr;

  StorageDataSet rep01 = new StorageDataSet();
  StorageDataSet rep02 = new StorageDataSet();
  StorageDataSet sum02 = new StorageDataSet();

  String regbrMIO;

  public frmM4() {
    super('A');
    xYLayout2.setWidth(590);
    xYLayout2.setHeight(32);
    jPanel2.remove(jraMjesecOd);
    jPanel2.remove(jraRbrOd);
    jPanel2.remove(jraGodinaDo);
    jPanel2.remove(jraMjesecDo);
    jPanel2.remove(jraRbrDo);
    jPanel2.remove(jlRbr);
    jlMjGodOd.setText("Godina");
    jraGodinaOd.setHorizontalAlignment(SwingConstants.CENTER);

    this.addReport("hr.restart.pl.repM4main", "hr.restart.pl.repM4main", "M4main", "Obrazac M-4");
    this.addReport("hr.restart.pl.repM4table1_V2_0", "hr.restart.pl.repM4tables", "M4table1_V2_0", "Tablica 01/6");
    this.addReport("hr.restart.pl.repM4table2_V2_0", "hr.restart.pl.repM4tables", "M4table2_V2_0", "Tablica 03/6");

//    this.addReport("hr.restart.pl.repM4main", "Obrazac M-4", 2);
//    this.addReport("hr.restart.pl.repM4table1", "Tablica 1", 2);
//    this.addReport("hr.restart.pl.repM4table2", "Tablica 2", 2);
    fm4 = this;
  }

  static frmM4 fm4;

  public static frmM4 getInstance(){
    if (fm4 == null) fm4 = new frmM4();
    return fm4;
  }

  public void componentShow(){
    fieldSet.setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
//    fieldSet.setShort("GODINAOD", Short.parseShort(vl.findYear(vl.getToday())));
    jlrCorg.forceFocLost();
    jraGodinaOd.setText("");
    jraGodinaOd.requestFocus();
  }

  public boolean Validacija(){
    if (!super.Validacija()) return false;
    if (vl.isEmpty(jraGodinaOd)) return false;
    kumulradarh();
    if (oosig.rowCount() == 0) {
      javax.swing.JOptionPane.showMessageDialog(this.getContentPane(),
          new hr.restart.swing.raMultiLineMessage(new String[]{"Nema podataka za " + fieldSet.getShort("GODINAOD") + ". godinu"}),
          "Pozor",
          javax.swing.JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  public void okPress(){
//    System.out.println("M4_1   : " + raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_1));
//    System.out.println("M4_2   : " + raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_2));
//    System.out.println("M4_3   : " + raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_3));
//    System.out.println("ID_1_1 : " + raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.ID_1_1));
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(fieldSet);

//    getStatusBar().statusMSG("Pritoka zapo\u0107inje :)");

    osnovno();
    osnovno2();
    qdsM4_1();
    qdsM4_2();
    qdsM4_3();
    qdsM4_1_b();
    qdsM4_2_b();
    qdsM4_3_b();
    qdsID_1_1();
    podaciOpod();
    orgReg();
    makeRepSet();
  }

  private void kumulradarh(){
    String kra = "select cradnik, sum(bruto) as osnosig from kumulradarh "+
                 "where godobr = " + fieldSet.getShort("GODINAOD") +
                 "and (corg in " + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG"))) + ") "+
                 "group by cradnik";
//    System.out.println("KUMULRADARH : " + kra);
    this.oosig = ut.getNewQueryDataSet(kra);
  }

  private void qdsM4_1(){
    String hzzo ="select cradnik, sum(bruto) as BRUTO from primanjaarh where godobr = " + fieldSet.getShort("GODINAOD") +
                 " and " + raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_1) +
                 " group by cradnik";
//    System.out.println("QDSM4_1 (HZZO) : " + hzzo);
    if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_1).equals(""))
    this.hzzo = ut.getNewQueryDataSet(hzzo);
  }

  private void qdsM4_2(){
    String rh ="select cradnik, sum(bruto) as BRUTO from primanjaarh where godobr = " + fieldSet.getShort("GODINAOD") +
                 " and " + raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_2) +
                 " group by cradnik";
//    System.out.println("QDSM4_2 (RH) : " + rh);
    if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_2).equals(""))
    this.rh = ut.getNewQueryDataSet(rh);
  }

  private void qdsM4_3(){
    String ss ="select cradnik, sum(bruto) as BRUTO from primanjaarh where godobr = " + fieldSet.getShort("GODINAOD") +
               " and " + raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_3) +
               " group by cradnik";
//    System.out.println("QDSM4_3 (SS) : " + ss);
    if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_3).equals(""))
    this.ss = ut.getNewQueryDataSet(ss);
  }

  private void qdsM4_1_b(){
    String hzzo ="select mjobr, sum(bruto) as BRUTO from primanjaarh where godobr = " + fieldSet.getShort("GODINAOD") +
                 " and " + raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_1) +
                 " group by mjobr";
//    System.out.println("QDSM4_1_b (HZZO) : " + hzzo);
    if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_1).equals(""))
    this.hzzoB = ut.getNewQueryDataSet(hzzo);
  }

  private void qdsM4_2_b(){
    String rh ="mjobr, sum(bruto) as BRUTO from primanjaarh where godobr = " + fieldSet.getShort("GODINAOD") +
                 " and " + raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_2) +
                 " group by mjobr";
//    System.out.println("QDSM4_2_b (RH) : " + rh);
    if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_2).equals(""))
    this.rhB = ut.getNewQueryDataSet(rh);
  }

  private void qdsM4_3_b(){
    String ss ="select mjobr, sum(bruto) as BRUTO from primanjaarh where godobr = " + fieldSet.getShort("GODINAOD") +
               " and " + raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_3) +
               " group by mjobr";
//    System.out.println("QDSM4_3_b (SS) : " + ss);
    if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_3).equals(""))
    this.ssB = ut.getNewQueryDataSet(ss);
  }

  private void qdsID_1_1(){
    String III_1_1 = "select cradnik, mjobr, sum(obriznos) as iznos from odbiciarh where godobr = " + fieldSet.getShort("GODINAOD") +
                     " and " + raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.ID_1_1) +
//                     " and " +
                     " group by mjobr, cradnik";
//    System.out.println("QDSID_1_1 (III_1_1) : " + III_1_1);
    if (!raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.ID_1_1).equals("")){
      dopr = ut.getNewQueryDataSet(III_1_1);
//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(dopr);
    }
  }

//  String radniciIn = "";

  private void osnovno(){
    String basic ="select radnici.corg, radnici.cradnik, radnici.ime, radnici.prezime, radmj.nazivrm, "+
                                                                       /** @todo benstaz???? */
                  "radnicipl.regbrmio, radnicipl.jmbg, radnicipl.godstaz, (0) as benstaz, radnicipl.clanomf "+
                  "from radnici, radnicipl, radmj "+
                  "WHERE radnici.cradnik = radnicipl.cradnik "+
                  "AND radnicipl.cradmj = radmj.cradmj "+
                  "AND (radnici.corg in " + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG")),"radnici.corg") +
                  ") ORDER BY radnici.prezime, radnici.ime, radnici.cradnik";
//    System.out.println("BASIC : " + basic);
    this.basic = ut.getNewQueryDataSet(basic);
//    radniciIn = " and Kumulradarh.cradnik in (";
//    this.basic.first();
//    do {
//      if (this.basic.getRow() != this.basic.rowCount()-1){
//        radniciIn += this.basic.getString("CRADNIK")+", ";
//      } else {
//        radniciIn += this.basic.getString("CRADNIK")+") ";
//      }
//    } while (this.basic.next());
//    System.out.println(radniciIn);
  }

  private void osnovno2(){

    String basicA = "select Kumulradarh.cradnik as cradnik, Kumulradarh.mjobr as mjobr,  "+
                    "sum(Kumulradarh.bruto) as bruto, max(Kumulorgarh.datumispl) as datumispl "+
                    "FROM Kumulradarh, Kumulorgarh WHERE kumulradarh.godobr = kumulorgarh.godobr "+
                    "AND kumulradarh.mjobr = kumulorgarh.mjobr AND kumulradarh.rbrobr = kumulorgarh.rbrobr "+
                    "AND kumulradarh.cvro = kumulorgarh.cvro AND kumulradarh.corg = kumulorgarh.corg "+
                    "and Kumulradarh.godobr= " + fieldSet.getShort("GODINAOD") + " " +
                    "and (Kumulradarh.corg in " + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG")),"Kumulradarh.corg") +
                    ") GROUP BY Kumulradarh.mjobr, Kumulradarh.cradnik";

//    String basicA = "select max(cradnik) as cradnik, max(mjobr) as mjobr,  sum(bruto) as bruto "+
//                    "FROM Kumulradarh where godobr= '" + fieldSet.getShort("GODINAOD") + "'" +
//                    "and corg in " + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG"))) +
//                   " GROUP BY mjobr";

//    String basic = "select max(mjobr) as mjobr, max(datumispl) as datumispl, sum(bruto) as bruto "+
//                   "FROM Kumulorgarh where godobr= '" + fieldSet.getShort("GODINAOD") + "'" +
//                   " and corg in " + orgs.getInQuery(orgs.getOrgstrAndKnjig(fieldSet.getString("CORG"))) +
//                   " GROUP BY mjobr";

//    System.out.println("BASIC2 : " + basic);
    this.basic2 = ut.getNewQueryDataSet(basicA);
  }

  private void orgReg(){
    raIniciranje.getInstance().posOrgsPl(fieldSet.getString("CORG"));
    regbrMIO = dm.getOrgpl().getString("REGBRMIO");
  }

  private void podaciOpod() {
    String knjigovodstvo = "SELECT Orgstruktura.naziv, Orgstruktura.mjesto, Orgstruktura.adresa, Orgstruktura.hpbroj, Orgstruktura.ziro, Logotipovi.matbroj, Logotipovi.sifdjel "+
                           "FROM Orgstruktura, Logotipovi "+
                           "WHERE orgstruktura.corg = logotipovi.corg and orgstruktura.corg ='" + hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(fieldSet.getString("CORG")) + "'";
//    System.out.println("KNJIGOVODSTVO : " + knjigovodstvo);
    podoobv = ut.getNewQueryDataSet(knjigovodstvo);
  }

  Column orgregmio = new Column();
  Column bruto = new Column();
  Column brutoA = new Column();
  Column brutoB = new Column();
  Column doprinosA = new Column();
  Column doprinosB = new Column();
  Column bruto1 = new Column();
  Column bruto2 = new Column();
  Column bruto3 = new Column();

  private void makeRepSet(){
    try{
//      orgregmio.setColumnName("ORGREGMIO");
//      orgregmio.setDataType(com.borland.dx.dataset.Variant.STRING);

      orgregmio = dm.createStringColumn("ORGREGMIO",0);

//      neto.setColumnName("BRUTO");
//      neto.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);


      /** @todo !!!!!!!!! PAZ OVO, neto=bruto!!! PREISPITATI I PO POTREBI PREODGOJITI!! */
      // jel' bi to mogla bit ona baza bruto za isplatu jednako netu za uplatu ili obrnuto,
      // ili ja to samo pricam pizdarije, ili je negdi suma od drveca???
      bruto = dm.createBigDecimalColumn("BRUTO");
      brutoA = dm.createBigDecimalColumn("BRUTO_I");
      brutoB = dm.createBigDecimalColumn("BRUTO_II");

//      doprinos.setColumnName("UIZNDOP");
//      doprinos.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      doprinosA = dm.createBigDecimalColumn("UIZNDOP_I");
      doprinosB = dm.createBigDecimalColumn("UIZNDOP_II");

//      bruto1.setColumnName("BRUTO1");
//      bruto1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      bruto1 = dm.createBigDecimalColumn("BRUTO1");

//      bruto2.setColumnName("BRUTO2");
//      bruto2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      bruto2 = dm.createBigDecimalColumn("BRUTO2");

//      bruto3.setColumnName("BRUTO3");
//      bruto3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);

      bruto3 = dm.createBigDecimalColumn("BRUTO3");
    }
    catch (Exception ex){

    }
    try {
      rep01.setColumns(basic.cloneColumns());
      rep01.addColumn((Column)orgregmio.clone());
      rep01.addColumn((Column)bruto.clone());
      rep01.addColumn((Column)bruto1.clone());
      rep01.addColumn((Column)bruto2.clone());
      rep01.addColumn((Column)bruto3.clone());

      rep01.open();
    }
    catch (Exception ex) {
      rep01.deleteAllRows();
    }

    try {
//      rep02.setColumns(basic2.cloneColumns());
//      rep02.addColumn((Column)doprinosA.clone());
//      rep02.addColumn((Column)doprinosB.clone());
//      rep02.addColumn((Column)bruto1.clone());
//      rep02.addColumn((Column)bruto2.clone());
//      rep02.addColumn((Column)bruto3.clone());
      rep02.setColumns(new Column[] {
        (Column)basic2.getColumn("MJOBR").clone(),
        (Column)basic2.getColumn("DATUMISPL").clone(),
        (Column)brutoA.clone(),
        (Column)brutoB.clone(),
        (Column)doprinosA.clone(),
        (Column)doprinosB.clone(),
        (Column)bruto1.clone(),
        (Column)bruto2.clone(),
        (Column)bruto3.clone()});


      rep02.open();
    }
    catch (Exception ex) {
      rep02.deleteAllRows();
    }

    try {
      sum02.setColumns(rep02.cloneColumns());
      sum02.open();
    }
    catch (Exception ex) {
      sum02.deleteAllRows();
    }

    /*QueryDataSet basic;
  QueryDataSet ;
  QueryDataSet ;
  QueryDataSet ;
  QueryDataSet ;
  QueryDataSet ;
  QueryDataSet ;
  QueryDataSet ;
  QueryDataSet ;
  QueryDataSet ;
  QueryDataSet ;*/

//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(basic);
//    syst.prn(basic2);
//    syst.prn(hzzo);
//    syst.prn(rh);
//    syst.prn(ss);
//    syst.prn(hzzoB);
//    syst.prn(rhB);
//    syst.prn(ssB);
//    syst.prn(rep01);
//    syst.prn(rep02);
//    syst.prn(sum02);

    setrepset();
    setrepset2();
  }

  private void setrepset(){
    basic.first();
    rep01.first();
    do {
      rep01.insertRow(false);
      rep01.setString("CORG", basic.getString("CORG"));
      rep01.setString("CRADNIK", basic.getString("CRADNIK"));
      rep01.setString("IME", basic.getString("IME"));
      rep01.setString("PREZIME", basic.getString("PREZIME"));
      rep01.setString("NAZIVRM", basic.getString("NAZIVRM"));
      rep01.setString("REGBRMIO", basic.getString("REGBRMIO"));
      rep01.setString("JMBG", basic.getString("JMBG"));
      rep01.setString("IME", basic.getString("IME"));
      rep01.setShort("GODSTAZ", basic.getShort("GODSTAZ"));
      rep01.setInt("BENSTAZ", basic.getInt("BENSTAZ"));
      rep01.setString("ORGREGMIO", regbrMIO);
    } while (basic.next());

    rep01.first();
    lookupData ld = lookupData.getlookupData();
    do {
      if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_1).equals("")){
        ld.raLocate(hzzo, new String[] {"CRADNIK"}, new String[] {rep01.getString("CRADNIK")});
        rep01.setBigDecimal("BRUTO1", hzzo.getBigDecimal("BRUTO"));
      } else {
        rep01.setBigDecimal("BRUTO1", NULA);
      }

      if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_2).equals("")){
        ld.raLocate(rh, new String[] {"CRADNIK"}, new String[] {rep01.getString("CRADNIK")});
        rep01.setBigDecimal("BRUTO2", rh.getBigDecimal("BRUTO"));
      } else {
        rep01.setBigDecimal("BRUTO2", NULA);
      }

      if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_3).equals("")){
        ld.raLocate(ss, new String[] {"CRADNIK"}, new String[] {rep01.getString("CRADNIK")});
        rep01.setBigDecimal("BRUTO3", ss.getBigDecimal("BRUTO"));
      } else {
        rep01.setBigDecimal("BRUTO3", NULA);
      }

      ld.raLocate(oosig, new String[] {"CRADNIK"}, new String[] {rep01.getString("CRADNIK")});
      rep01.setBigDecimal("BRUTO", oosig.getBigDecimal("OSNOSIG"));

    } while(rep01.next());

  }

  private void setrepset2(){
    basic2.first();
    sum02.insertRow(false);
    do {
      if (!ld.raLocate(rep02,"MJOBR",String.valueOf(basic2.getShort("MJOBR")))){
        rep02.insertRow(false);
        rep02.setShort("MJOBR", basic2.getShort("MJOBR"));
        rep02.setTimestamp("DATUMISPL", basic2.getTimestamp("DATUMISPL"));
      }
      if (ld.raLocate(basic,"CRADNIK",basic2.getString("CRADNIK")) && basic.getString("CLANOMF").equals("N")){
        rep02.setBigDecimal("BRUTO_I", rep02.getBigDecimal("BRUTO_I").add(basic2.getBigDecimal("BRUTO")));
        sum02.setBigDecimal("BRUTO_I", sum02.getBigDecimal("BRUTO_I").add(basic2.getBigDecimal("BRUTO")));
        dopr.first();
        do {
          if (dopr.getShort("MJOBR") == rep02.getShort("MJOBR") && dopr.getString("CRADNIK").equals(basic2.getString("CRADNIK"))){
            rep02.setBigDecimal("UIZNDOP_I", rep02.getBigDecimal("UIZNDOP_I").add(dopr.getBigDecimal("IZNOS")));
            sum02.setBigDecimal("UIZNDOP_I", sum02.getBigDecimal("UIZNDOP_I").add(dopr.getBigDecimal("IZNOS")));
          }
        } while (dopr.next());
      } else {
        rep02.setBigDecimal("BRUTO_II", rep02.getBigDecimal("BRUTO_II").add(basic2.getBigDecimal("BRUTO")));
        sum02.setBigDecimal("BRUTO_II", sum02.getBigDecimal("BRUTO_II").add(basic2.getBigDecimal("BRUTO")));
        dopr.first();
        do {
          if (dopr.getShort("MJOBR") == rep02.getShort("MJOBR") && dopr.getString("CRADNIK").equals(basic2.getString("CRADNIK"))){
            rep02.setBigDecimal("UIZNDOP_II", rep02.getBigDecimal("UIZNDOP_II").add(dopr.getBigDecimal("IZNOS")));
            sum02.setBigDecimal("UIZNDOP_II", sum02.getBigDecimal("UIZNDOP_II").add(dopr.getBigDecimal("IZNOS")));
          }
        } while (dopr.next());
      }
    } while (basic2.next());

    rep02.first();
    do {
      if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_1).equals("")){
        ld.raLocate(hzzoB, new String[] {"MJOBR"}, new String[] {String.valueOf(rep02.getShort("MJOBR"))});
        rep02.setBigDecimal("BRUTO1", hzzoB.getBigDecimal("BRUTO"));
        sum02.setBigDecimal("BRUTO1", sum02.getBigDecimal("BRUTO1").add(hzzoB.getBigDecimal("BRUTO")));
      } else {
        rep02.setBigDecimal("BRUTO1", NULA);
      }

      if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_2).equals("")){
        ld.raLocate(rhB, new String[] {"MJOBR"}, new String[] {String.valueOf(rep02.getShort("MJOBR"))});
        rep02.setBigDecimal("BRUTO2", rhB.getBigDecimal("BRUTO"));
        sum02.setBigDecimal("BRUTO2", sum02.getBigDecimal("BRUTO2").add(rhB.getBigDecimal("BRUTO")));
      } else {
        rep02.setBigDecimal("BRUTO2", NULA);
      }

      if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_3).equals("")){
        ld.raLocate(ssB, new String[] {"MJOBR"}, new String[] {String.valueOf(rep02.getShort("MJOBR"))});
        rep02.setBigDecimal("BRUTO3", ssB.getBigDecimal("BRUTO"));
        sum02.setBigDecimal("BRUTO3", sum02.getBigDecimal("BRUTO3").add(ssB.getBigDecimal("BRUTO")));
      } else {
        rep02.setBigDecimal("BRUTO3", NULA);
      }

    } while(rep02.next());


//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(rep02);
//    syst.prn(sum02);
  }

  private void setrepsdfgysdf(){

    rep02.first();
    do {
      if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_1).equals("")){
        ld.raLocate(hzzoB, new String[] {"MJOBR"}, new String[] {String.valueOf(rep02.getShort("MJOBR"))});
        rep02.setBigDecimal("BRUTO1", hzzoB.getBigDecimal("BRUTO"));
        sum02.setBigDecimal("BRUTO1", sum02.getBigDecimal("BRUTO1").add(hzzoB.getBigDecimal("BRUTO")));
      } else {
        rep02.setBigDecimal("BRUTO1", NULA);
      }

      if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_2).equals("")){
        ld.raLocate(rhB, new String[] {"MJOBR"}, new String[] {String.valueOf(rep02.getShort("MJOBR"))});
        rep02.setBigDecimal("BRUTO2", rhB.getBigDecimal("BRUTO"));
        sum02.setBigDecimal("BRUTO2", sum02.getBigDecimal("BRUTO2").add(rhB.getBigDecimal("BRUTO")));
      } else {
        rep02.setBigDecimal("BRUTO2", NULA);
      }

      if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.M4_3).equals("")){
        ld.raLocate(ssB, new String[] {"MJOBR"}, new String[] {String.valueOf(rep02.getShort("MJOBR"))});
        rep02.setBigDecimal("BRUTO3", ssB.getBigDecimal("BRUTO"));
        sum02.setBigDecimal("BRUTO3", sum02.getBigDecimal("BRUTO3").add(ssB.getBigDecimal("BRUTO")));
      } else {
        rep02.setBigDecimal("BRUTO3", NULA);
      }

    } while(rep02.next());
  }

  public DataSet getRep01Set(){
    return rep01;
  }

  public DataSet getRep02Set(){
    return rep02;
  }

  public DataSet getPodaciSet(){
    return podoobv;
  }

  public DataSet getSum01Set(){
    return sum02;
  }

  public short getGodina(){
    return fieldSet.getShort("GODINAOD");
  }

  public String getRBMIO(){
    return regbrMIO;
  }
}