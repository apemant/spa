/****license*****************************************************************
**   file: frmSPL.java
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

import java.math.BigDecimal;

import hr.restart.sisfun.frmParam;
import hr.restart.util.Util;
import hr.restart.util.Valid;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmSPL extends frmIzvjestajiPL {

  QueryDataSet knjig;

  public frmSPL() {
    this('O');
  }

  public frmSPL(char mode){
    super(mode);
    frspl = this;
    setReportProviders();
    setDifolt();

    jlMjGodOd.setText("Isplata u (mj, god)");
    jPanel2.add(jraMjesecOd, new com.borland.jbcl.layout.XYConstraints(150, 0, 35, -1));
    jPanel2.add(jraGodinaOd, new com.borland.jbcl.layout.XYConstraints(190, 0, 60, -1));
    jraRbrOd.setVisible(false);
    jlRbr.setVisible(false);
    difoltMjGod();
    rcc.setLabelLaF(jraMjesecOd, true);
    rcc.setLabelLaF(jraGodinaOd, true);
  }

  private void difoltMjGod() throws NumberFormatException {
    raIniciranje.getInstance().posOrgsPl(hr.restart.zapod.OrgStr.getKNJCORG());
    fieldSet.setShort("GODINAOD", Short.parseShort(Util.getUtil().getYear(dm.getOrgpl().getTimestamp("DATUMISPL"))));
    fieldSet.setShort("MJESECOD", Short.parseShort(Util.getUtil().getMonth(dm.getOrgpl().getTimestamp("DATUMISPL"))));
  }

  public void componentShow() {
    try {
      difoltMjGod();
    }
    catch (NumberFormatException ex) {
      jraMjesecOd.setText("");
      jraGodinaOd.setText("");
    }
    jlrCorg.requestFocus();
  }

  static frmSPL frspl;

  public static frmSPL getInstance(){
    if (frspl == null){
      frspl = new frmSPL();
    }
    return frspl;
  }

  StorageDataSet repSet;
  java.math.BigDecimal fondSati;
  java.math.BigDecimal brojRadnika;
  java.math.BigDecimal bruto0;
  java.math.BigDecimal bruto10;
  java.math.BigDecimal neto12;

  int gomj;
  java.sql.Timestamp _od, _do;

  public boolean Validacija(){
    if (!super.Validacija()) return false;
    rcc.EnabDisabAll(jPanel1, false);
    fondSati = raIzvjestaji.getFond(fieldSet.getShort("GODINAOD"), fieldSet.getShort("MJESECOD"), true).satiUk;
    gomj = fieldSet.getShort("GODINAOD") * 100 + fieldSet.getShort("MJESECOD");
    _od = java.sql.Timestamp.valueOf(fieldSet.getShort("GODINAOD") + "-" +
                                     fieldSet.getShort("MJESECOD") + "-01 00:00:00.00");

    _do = Util.getUtil().getLastSecondOfDay(Util.getUtil().getLastDayOfMonth(_od));

    /*java.sql.Timestamp.valueOf(fieldSet.getShort("GODINAOD") + "-" +
                                     fieldSet.getShort("MJESECOD") + "-31 23:59:59.99");*/

    String qst1 = "select sum(kumulrad.sati) as sati, sum(kumulrad.bruto) as bruto, sum(kumulrad.neto) as neto,"+
                  " sum(kumulrad.doprinosi) as doprinosi,"+
                  " sum(kumulrad.neto2) as neto2, sum(kumulrad.poriprir) as poriprir,"+
                  " min(kumulrad.bruto) as bmin, max(kumulrad.bruto) as bmax, min(kumulrad.netopk) as nmin, "+
                  " max(kumulrad.netopk) as nmax"+
                  " from kumulrad, radnici where radnici.cradnik = kumulrad.cradnik " +
//                  "AND radnici.corg = orgpl.corg " +
                  "AND " +
                  getWhereQuery("radnici");
//                  +" AND (orgpl.datumispl between '" + _od + "'"+
//                  " AND '" + _do + "')" ;

    String qst2 = "select sum(kumulradarh.sati) as sati, sum(kumulradarh.bruto) as bruto, sum(kumulradarh.neto) as neto,"+
                  " sum(kumulradarh.doprinosi) as doprinosi,"+
                  " sum(kumulradarh.neto2) as neto2, sum(kumulradarh.poriprir) as poriprir,"+
                  " min(kumulradarh.bruto) as bmin, max(kumulradarh.bruto) as bmax, min(kumulradarh.netopk) as nmin, "+
                  " max(kumulradarh.netopk) as nmax"+
                  " from kumulradarh, radnici, Kumulorgarh WHERE kumulradarh.cradnik = radnici.cradnik" +
                  " AND kumulradarh.godobr = kumulorgarh.godobr AND kumulradarh.mjobr = kumulorgarh.mjobr" +
                  " AND kumulradarh.rbrobr = kumulorgarh.rbrobr AND kumulradarh.cvro = kumulorgarh.cvro" +
                  " AND kumulradarh.corg = kumulorgarh.corg AND radnici.corg = kumulorgarh.corg AND " +
                  getWhereQuery("radnici") +
                  " AND kumulorgarh.datumispl between '" + _od + "' AND '" + _do + "' ";

    System.out.println("\nobradaQDS query : " + qst1);
    System.out.println("\narhivaQDS query : " + qst2 + "\n");

    QueryDataSet obradaQDS = Util.getNewQueryDataSet(qst1);
    QueryDataSet arhivaQDS = Util.getNewQueryDataSet(qst2);


    makeRepSet(obradaQDS, arhivaQDS);

    maxBN();//_od, _do);

    bruto0 = arhivaQDS.getBigDecimal("BRUTO").setScale(0,java.math.BigDecimal.ROUND_HALF_UP);

    try {
      if (frmParam.getParam("pl", "splbrrad", "F", "SPL: broj radnika se dobiva iz (F)onda sati ili brojanjem (R)adnika").equalsIgnoreCase("R")) {
        brojRadnika = calcBrojRadnika(); 
      } else {
        brojRadnika = repSet.getBigDecimal("SATI").divide(fondSati, 2, java.math.BigDecimal.ROUND_HALF_UP);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      brojRadnika = new java.math.BigDecimal("0");
    }
    return true;
  }

  private BigDecimal calcBrojRadnika() {
    String q1 = "select count('') "+
    " from kumulrad, radnici, orgpl where radnici.cradnik = kumulrad.cradnik AND radnici.corg = orgpl.corg AND " +
    getWhereQuery("radnici") +
    " AND (orgpl.datumispl between '" + _od + "'"+
    " AND '" + _do + "')" ;

    String q2 = "select count(*) "+
    " from kumulradarh, radnici, Kumulorgarh WHERE kumulradarh.cradnik = radnici.cradnik" +
    " AND kumulradarh.godobr = kumulorgarh.godobr AND kumulradarh.mjobr = kumulorgarh.mjobr" +
    " AND kumulradarh.rbrobr = kumulorgarh.rbrobr AND kumulradarh.cvro = kumulorgarh.cvro" +
    " AND kumulradarh.corg = kumulorgarh.corg AND radnici.corg = kumulorgarh.corg AND " +
    getWhereQuery("radnici") +
    " AND kumulorgarh.datumispl between '" + _od + "' AND '" + _do + "' ";
System.out.println("4count:"+q1);
    
    return new BigDecimal(Valid.getValid().getSetCount(Util.getNewQueryDataSet(q1), 0));
  }

  public void okPress(){
    super.okPress();
    setKnjigovodstvo();
  }

  private void maxBN(){ //java.sql.Timestamp _od, java.sql.Timestamp _do) {
//    System.out.println("od " + _od);
//    System.out.println("do " + _do);

    String qst3Sub1 = "", qst4Sub1 = "";
    if(maxBrutoTableName.equals("kumulradarh")){
      qst3Sub1 = "SELECT max(primanjaarh.bruto) as bruto FROM primanjaarh, Kumulorgarh "+
                 "WHERE cradnik in ( "+
                 "SELECT cradnik FROM kumulradarh WHERE bruto = ( "+
                 "SELECT max(kumulradarh.bruto) "+
                 "FROM kumulradarh, Kumulorgarh "+
                 "WHERE kumulradarh.godobr = kumulorgarh.godobr "+
                 "AND kumulradarh.mjobr = kumulorgarh.mjobr "+
                 "AND kumulradarh.rbrobr = kumulorgarh.rbrobr "+
                 "AND kumulradarh.cvro = kumulorgarh.cvro "+
                 "AND kumulradarh.corg = kumulorgarh.corg "+
                 "AND kumulorgarh.datumispl between '" + _od + "' "+
                 "AND '" + _do + "' "+
                 ") group by cradnik) "+
                 "AND primanjaarh.godobr = kumulorgarh.godobr "+
                 "AND primanjaarh.mjobr = kumulorgarh.mjobr "+
                 "AND primanjaarh.rbrobr = kumulorgarh.rbrobr "+
                 "AND primanjaarh.corg = kumulorgarh.corg "+
                 "AND kumulorgarh.datumispl between '" + _od + "' "+
                 "    AND '" + _do + "'";

      qst4Sub1 = "SELECT max(primanjaarh.neto) as neto FROM primanjaarh, Kumulorgarh "+
                 "WHERE cradnik in ( "+
                 "SELECT cradnik FROM kumulradarh WHERE netopk = ( "+
                 "SELECT max(kumulradarh.netopk) "+
                 "FROM kumulradarh, Kumulorgarh "+
                 "WHERE kumulradarh.godobr = kumulorgarh.godobr "+
                 "AND kumulradarh.mjobr = kumulorgarh.mjobr "+
                 "AND kumulradarh.rbrobr = kumulorgarh.rbrobr "+
                 "AND kumulradarh.cvro = kumulorgarh.cvro "+
                 "AND kumulradarh.corg = kumulorgarh.corg "+
                 "AND kumulorgarh.datumispl between '" + _od + "' "+
                 "AND '" + _do + "' "+
                 ")) "+
                 "AND primanjaarh.godobr = kumulorgarh.godobr "+
                 "AND primanjaarh.mjobr = kumulorgarh.mjobr "+
                 "AND primanjaarh.rbrobr = kumulorgarh.rbrobr "+
                 "AND primanjaarh.corg = kumulorgarh.corg "+
                 "AND kumulorgarh.datumispl between '" + _od + "' "+
                 "    AND '" + _do + "'";
    } else {
      qst3Sub1 = "SELECT max(primanjaobr.bruto) as bruto FROM Primanjaobr, Orgpl "+
                 "WHERE primanjaobr.corg = orgpl.corg "+
                 "AND Orgpl.datumispl between '" + _od + "' "+
                 "AND '" + _do + "' ";

      qst4Sub1 = "SELECT max(primanjaobr.neto) as neto FROM Primanjaobr, Orgpl "+
                 "WHERE primanjaobr.corg = orgpl.corg "+
                 "AND Orgpl.datumispl between '" + _od + "' "+
                 "AND '" + _do + "' ";
    }

    String qst3=qst3Sub1;
    String qst4=qst4Sub1;

    if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_020).equals("")){
      qst3 = qst3.concat(" and not ".concat(raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_020)));
      qst4 = qst4.concat(" and not ".concat(raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_020)));
    }
    if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_030).equals("")){
      qst3 = qst3.concat(" and not ".concat(raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_030)));
      qst4 = qst4.concat(" and not ".concat(raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_030)));
    }
    if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_040).equals("")){
      qst3 = qst3.concat(" and not ".concat(raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_040)));
      qst4 = qst4.concat(" and not ".concat(raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_040)));
    }
    if (!raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_050).equals("")){
      qst3 = qst3.concat(" and not ".concat(raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_050)));
      qst4 = qst4.concat(" and not ".concat(raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjestaji.ID_050)));
    }

    System.out.println("\nqst3 - " +qst3);
    System.out.println("\nqst4 - " +qst4+"\n");

    QueryDataSet brut02QDS = Util.getNewQueryDataSet(qst3);
    QueryDataSet neto02QDS = Util.getNewQueryDataSet(qst4);

    bruto10 = brut02QDS.getBigDecimal("BRUTO").setScale(0,java.math.BigDecimal.ROUND_HALF_UP);
    neto12 = neto02QDS.getBigDecimal("NETO").setScale(0,java.math.BigDecimal.ROUND_HALF_UP);

//    System.out.println("\nbruto10 : " + bruto10);
//    System.out.println("neto12  : " + neto12);
  }

  String maxBrutoTableName, maxNetoTableName, primanjaBrutoTableName, primanjaNetoTableName, pomocnaBrut, pomocnaNet;

  private void makeRepSet(QueryDataSet obrada, QueryDataSet arhiva){
    repSet = new StorageDataSet();
    repSet.setColumns(obrada.cloneColumns());
    repSet.open();

    repSet.insertRow(false);

    repSet.setBigDecimal("SATI", (obrada.getBigDecimal("SATI").add(arhiva.getBigDecimal("SATI"))).setScale(0,java.math.BigDecimal.ROUND_HALF_UP)); // dadd
    repSet.setBigDecimal("BRUTO", (obrada.getBigDecimal("BRUTO")/*.add(arhiva.getBigDecimal("BRUTO"))*/).setScale(0,java.math.BigDecimal.ROUND_HALF_UP));
    repSet.setBigDecimal("NETO", (obrada.getBigDecimal("NETO").add(arhiva.getBigDecimal("NETO"))).setScale(0,java.math.BigDecimal.ROUND_HALF_UP)); //dadd
    repSet.setBigDecimal("DOPRINOSI", (obrada.getBigDecimal("DOPRINOSI").add(arhiva.getBigDecimal("DOPRINOSI"))).setScale(0,java.math.BigDecimal.ROUND_HALF_UP));
    repSet.setBigDecimal("NETO2", (obrada.getBigDecimal("NETO2").add(arhiva.getBigDecimal("NETO2"))).setScale(0,java.math.BigDecimal.ROUND_HALF_UP));
    repSet.setBigDecimal("PORIPRIR", (obrada.getBigDecimal("PORIPRIR").add(arhiva.getBigDecimal("PORIPRIR"))).setScale(0,java.math.BigDecimal.ROUND_HALF_UP));

//    System.out.println(obrada.getBigDecimal("BMIN") + "  obrada.getBigDecimal(\"BMIN\").compareTo(arhiva.getBigDecimal(\"BMIN\")) >= 0 "+ arhiva.getBigDecimal("BMIN")+ " ; " + (obrada.getBigDecimal("BMIN").compareTo(arhiva.getBigDecimal("BMIN")) >= 0));
//    System.out.println("arhiva.getBigDecimal(\"BMIN\").compareTo(new java.math.BigDecimal(\"0.00\")) != 0 " + (arhiva.getBigDecimal("BMIN").compareTo(new java.math.BigDecimal("0.00")) != 0));

    if (obrada.getBigDecimal("BMIN").compareTo(arhiva.getBigDecimal("BMIN")) <= 0 &&
        arhiva.getBigDecimal("BMIN").compareTo(new java.math.BigDecimal("0.00")) != 0){
//      System.out.println("obrada nema redova ili obrada manja oda arhive i arhiva nije nula");
      repSet.setBigDecimal("BMIN", arhiva.getBigDecimal("BMIN").setScale(0,java.math.BigDecimal.ROUND_HALF_UP));
    } else {
//      System.out.println("nesto ne stima!!! ili ???");
      repSet.setBigDecimal("BMIN", obrada.getBigDecimal("BMIN").setScale(0,java.math.BigDecimal.ROUND_HALF_UP));
    }

    if(obrada.getBigDecimal("BMAX").compareTo(arhiva.getBigDecimal("BMAX")) >= 0){
      repSet.setBigDecimal("BMAX", obrada.getBigDecimal("BMAX").setScale(0,java.math.BigDecimal.ROUND_HALF_UP));
      maxBrutoTableName = "kumulrad";
      primanjaBrutoTableName = "primanjaobr";
      pomocnaBrut = "orgpl";
    } else {
      repSet.setBigDecimal("BMAX", arhiva.getBigDecimal("BMAX").setScale(0,java.math.BigDecimal.ROUND_HALF_UP));
      maxBrutoTableName = "kumulradarh";
      primanjaBrutoTableName = "primanjaarh";
      pomocnaBrut = "Kumulorgarh";
    }

    if (obrada.getBigDecimal("NMIN").compareTo(arhiva.getBigDecimal("NMIN")) <= 0 &&
        arhiva.getBigDecimal("NMIN").compareTo(new java.math.BigDecimal("0")) != 0){
      repSet.setBigDecimal("NMIN", arhiva.getBigDecimal("NMIN").setScale(0,java.math.BigDecimal.ROUND_HALF_UP));
    } else {
      repSet.setBigDecimal("NMIN", obrada.getBigDecimal("NMIN").setScale(0,java.math.BigDecimal.ROUND_HALF_UP));
    }

    if(obrada.getBigDecimal("NMAX").compareTo(arhiva.getBigDecimal("NMAX")) >= 0){
      repSet.setBigDecimal("NMAX", obrada.getBigDecimal("NMAX").setScale(0,java.math.BigDecimal.ROUND_HALF_UP));
      maxNetoTableName = "kumulrad";
      primanjaNetoTableName = "primanjaobr";
      pomocnaNet = "orgpl";
    } else {
      repSet.setBigDecimal("NMAX", arhiva.getBigDecimal("NMAX").setScale(0,java.math.BigDecimal.ROUND_HALF_UP));
      maxNetoTableName = "kumulradarh";
      primanjaNetoTableName = "primanjaarh";
      pomocnaNet = "Kumulorgarh";
    }
  }
  
  private void setKnjigovodstvo() {
    String knjigovodstvo = "SELECT Orgstruktura.naziv, Orgstruktura.mjesto, Orgstruktura.adresa, Orgstruktura.hpbroj, Orgstruktura.ziro, " +
    		(raObracunPL.isOIB()?"Logotipovi.oib":"Logotipovi.matbroj") +" AS MATBROJ" +
    		", Logotipovi.sifdjel "+
                           "FROM Orgstruktura, Logotipovi "+
                           "WHERE orgstruktura.corg = logotipovi.corg and orgstruktura.corg ='" + hr.restart.zapod.OrgStr.getKNJCORG() + "'";
    knjig = Util.getNewQueryDataSet(knjigovodstvo);
  }

  public void firstESC(){
    try {
        rcc.EnabDisabAll(mainPanel, true);
      jlrCorg.requestFocus();
      jlrCorg.selectAll();
      firstesc = false;
    }
    catch (Exception ex) {}
  }

  protected void setReportProviders(){
    this.addReport("hr.restart.pl.repSPL", "SPL", 2);
  }

  public DataSet getRepSet(){
    return repSet;
  }

  public short getMjObr(){
    return fieldSet.getShort("MJESECOD");
  }

  public short getGdObr(){
    return fieldSet.getShort("GODINAOD");
  }

  public double getBrojRadnika(){
    return brojRadnika.doubleValue();
  }

  public double getBruto0(){
    return bruto0.doubleValue();
  }

  public double getBruto10(){
    return bruto10.doubleValue();
  }

  public double getNeto12(){
    return neto12.doubleValue();
  }

  public int getGodMj(){
    return gomj;
  }

  public String getKnjNaziv(){
    return knjig.getString("NAZIV");
  }

  public String getKnjAdresa(){
    return knjig.getString("ADRESA");
  }

  public String getKnjHpBroj(){
    return knjig.getString("HPBROJ");
  }

  public String getKnjMjesto(){
    return knjig.getString("MJESTO");
  }

  public String getKnjZiro(){
    return knjig.getString("ZIRO");
  }

  public String getKnjMatbroj(){
    return knjig.getString("MATBROJ");
  }

  public void aft_lookCorg() {}
}