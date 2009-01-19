/****license*****************************************************************
**   file: osUtil.java
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
package hr.restart.os;

import hr.restart.baza.Condition;
import hr.restart.baza.KreirDrop;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.zapod.OrgStr;

import java.util.Calendar;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class osUtil {
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
  public java.sql.Timestamp nulDate = new java.sql.Timestamp(0);
  Calendar cal = Calendar.getInstance();
  public java.math.BigDecimal sto = new java.math.BigDecimal(100);
  public java.math.BigDecimal nul = new java.math.BigDecimal(0);
  public java.math.BigDecimal one = new java.math.BigDecimal(1);
  public java.math.BigDecimal _sto = new java.math.BigDecimal(0.01);
  hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  private static osUtil myUtil;

  public static osUtil getUtil() {
    if (myUtil==null) myUtil=new osUtil();
    return myUtil;
  }
  public void AktivToPriprema() {

  }
/**
 * Spremanje podataka u OS_Promjene
 * @param tds
 * @param mode
 */
  public void beforeUpdateOS(com.borland.dx.dataset.TableDataSet tds, char mode) {
    System.out.println("beforeupdateOS");
    com.borland.dx.sql.dataset.QueryDataSet qdsDetail;
    if (mode=='S' || mode=='L' || mode=='N' || mode=='O' || mode=='P') { // Ako su OSNOVNA SREDSTVA
      qdsDetail=dm.getOS_Promjene();
    }
    else { // SITNI INVENTAR
      qdsDetail=dm.getOS_StSI();
    }
    if (mode=='S') {
      qdsDetail.setBigDecimal("OSNPOCETAK", tds.getBigDecimal("OSNOVICA"));
      qdsDetail.setBigDecimal("ISPPOCETAK", tds.getBigDecimal("ISPRAVAK"));
    }
    if (mode=='L' || mode =='Z') {
      qdsDetail.setBigDecimal("OSNPOTRAZUJE", tds.getBigDecimal("OSNOVICA"));
      qdsDetail.setBigDecimal("ISPDUGUJE", tds.getBigDecimal("ISPRAVAK"));
    }
    else {
      qdsDetail.setBigDecimal("OSNDUGUJE", tds.getBigDecimal("OSNOVICA"));
      qdsDetail.setBigDecimal("ISPPOTRAZUJE", tds.getBigDecimal("ISPRAVAK"));
    }
    qdsDetail.setBigDecimal("SALDO", util.negateValue(util.negateValue(qdsDetail.getBigDecimal("OSNDUGUJE"), qdsDetail.getBigDecimal("OSNPOTRAZUJE")), util.negateValue(qdsDetail.getBigDecimal("ISPPOTRAZUJE"), qdsDetail.getBigDecimal("ISPDUGUJE"))));
  }
/**
 * Spremanje podataka u OS_Sredstvo
 * @param oldOsnovica
 * @param oldIspravak
 * @param mode
 */
  public void afterUpdateOS(java.math.BigDecimal oldOsnovica, java.math.BigDecimal oldIspravak, char mode) {
    System.out.println("afterUpdate");
    com.borland.dx.sql.dataset.QueryDataSet qdsMaster;
    com.borland.dx.sql.dataset.QueryDataSet qdsDetail;
    if (mode=='S' || mode=='L' || mode=='N' || mode=='O' || mode=='P') {
      qdsMaster=dm.getOS_Sredstvo();
      qdsDetail=dm.getOS_Promjene();
    }
    else {
      qdsMaster=dm.getOS_SI();
      qdsDetail=dm.getOS_StSI();
    }
    if (mode=='S' || mode=='Y') {
      System.out.println("Ovo je poèetno stanje");
      qdsMaster.setBigDecimal("OSNPOCETAK",   util.sumValue(qdsMaster.getBigDecimal("OSNPOCETAK"), qdsDetail.getBigDecimal("OSNDUGUJE"), oldOsnovica.negate()));
      qdsMaster.setBigDecimal("ISPPOCETAK",   util.sumValue(qdsMaster.getBigDecimal("ISPPOCETAK"), qdsDetail.getBigDecimal("ISPPOTRAZUJE"), oldIspravak.negate()));
//      qdsMaster.setBigDecimal("NABVRIJED", util.sumValue(qdsMaster.getBigDecimal("NABVRIJED"), qdsDetail.getBigDecimal("OSNDUGUJE"), oldOsnovica.negate()));
      qdsMaster.setBigDecimal("NABVRIJED", qdsMaster.getBigDecimal("OSNPOCETAK"));
      qdsMaster.setString("DOKUMENT", qdsDetail.getString("DOKUMENT"));
      qdsMaster.setInt("CPAR", qdsDetail.getInt("CPAR"));
      qdsMaster.setString("CORG2", qdsDetail.getString("CORG2"));
      qdsMaster.setBigDecimal("OSNOVICA", util.sumValue(qdsMaster.getBigDecimal("OSNDUGUJE"), qdsMaster.getBigDecimal("OSNPOCETAK"), qdsMaster.getBigDecimal("OSNPOTRAZUJE").negate()));
      qdsMaster.setBigDecimal("ISPRAVAK", util.sumValue(qdsMaster.getBigDecimal("ISPPOTRAZUJE"), qdsMaster.getBigDecimal("ISPPOCETAK"), qdsMaster.getBigDecimal("ISPDUGUJE").negate())
                              .add(qdsMaster.getBigDecimal("AMORTIZACIJA")));
      findDates(qdsMaster, qdsDetail);
    }
/*    else if (mode=='O') {
      System.out.println("Ovo je promjena org. jedinice");
      qdsMaster.setBigDecimal("OSNDUGUJE",    util.sumValue(qdsMaster.getBigDecimal("OSNDUGUJE"), oldOsnovica));
      qdsMaster.setBigDecimal("ISPPOTRAZUJE", util.sumValue(qdsMaster.getBigDecimal("ISPPOTRAZUJE"),  oldIspravak));
    }*/
    else if (mode=='L' || mode=='Z') {
      System.out.println("Ovo je likvidacija");
      System.out.println("Osnovica: "+qdsDetail.getBigDecimal("OSNPOTRAZUJE"));
      qdsMaster.setString("AKTIV", "N");
      qdsMaster.setBigDecimal("OSNPOTRAZUJE", qdsDetail.getBigDecimal("OSNPOTRAZUJE"));
      qdsMaster.setBigDecimal("ISPDUGUJE", qdsDetail.getBigDecimal("ISPDUGUJE"));
      qdsMaster.setBigDecimal("ISPPOTRAZUJE", util.negateValue(qdsMaster.getBigDecimal("ISPPOTRAZUJE"), oldIspravak));
      qdsMaster.setTimestamp("DATLIKVIDACIJE", qdsDetail.getTimestamp("DATPROMJENE"));
      qdsMaster.setBigDecimal("OSNOVICA", util.sumValue(qdsMaster.getBigDecimal("OSNDUGUJE"), qdsMaster.getBigDecimal("OSNPOCETAK"), qdsMaster.getBigDecimal("OSNPOTRAZUJE").negate()));
      qdsMaster.setBigDecimal("ISPRAVAK", util.sumValue(qdsMaster.getBigDecimal("ISPPOTRAZUJE"), qdsMaster.getBigDecimal("ISPPOCETAK"), qdsMaster.getBigDecimal("ISPDUGUJE").negate())
                              .add(qdsMaster.getBigDecimal("AMORTIZACIJA")));

      //      if (mode=='Z') qdsMaster.saveChanges();
//      qdsMaster.setBigDecimal("AMORTIZACIJA", util.nul);
//      qdsMaster.setBigDecimal("PAMORTIZACIJA", util.nul);
    }
    else if (qdsDetail.rowCount() == 1) {
      System.out.println("Ovo je rowCount==1");
      qdsMaster.setBigDecimal("OSNDUGUJE", qdsDetail.getBigDecimal("OSNDUGUJE"));
      qdsMaster.setBigDecimal("ISPPOTRAZUJE", qdsDetail.getBigDecimal("ISPPOTRAZUJE"));
      qdsMaster.setInt("CPAR", qdsDetail.getInt("CPAR"));
      qdsMaster.setString("DOKUMENT", qdsDetail.getString("DOKUMENT"));
      qdsMaster.setString("CORG2", qdsDetail.getString("CORG2"));
//      qdsMaster.setBigDecimal("OSNPOCETAK",   util.sumValue(qdsMaster.getBigDecimal("OSNPOCETAK"), qdsDetail.getBigDecimal("OSNDUGUJE"), oldOsnovica.negate()));
//      qdsMaster.setBigDecimal("ISPPOCETAK",   util.sumValue(qdsMaster.getBigDecimal("ISPPOCETAK"), qdsDetail.getBigDecimal("ISPPOTRAZUJE"), oldIspravak.negate()));
      qdsMaster.setBigDecimal("NABVRIJED", qdsMaster.getBigDecimal("OSNDUGUJE"));
      qdsMaster.setBigDecimal("OSNOVICA", util.sumValue(qdsMaster.getBigDecimal("OSNPOCETAK"), qdsDetail.getBigDecimal("OSNDUGUJE"), qdsMaster.getBigDecimal("OSNPOTRAZUJE").negate()));
      qdsMaster.setBigDecimal("ISPRAVAK", util.sumValue(qdsMaster.getBigDecimal("ISPPOCETAK"), qdsDetail.getBigDecimal("ISPPOTRAZUJE"), qdsMaster.getBigDecimal("ISPDUGUJE").negate())
          .add(qdsMaster.getBigDecimal("AMORTIZACIJA")));
      findDates(qdsMaster, qdsDetail);
    }
    else {
      System.out.println("Ovo je zadnji else");
      qdsMaster.setBigDecimal("OSNDUGUJE",    util.sumValue(qdsMaster.getBigDecimal("OSNDUGUJE"), qdsDetail.getBigDecimal("OSNDUGUJE"), oldOsnovica.negate()));
      qdsMaster.setBigDecimal("ISPPOTRAZUJE", util.sumValue(qdsMaster.getBigDecimal("ISPPOTRAZUJE"), qdsDetail.getBigDecimal("ISPPOTRAZUJE"), oldIspravak.negate()));
      qdsMaster.setBigDecimal("OSNOVICA", util.sumValue(qdsMaster.getBigDecimal("OSNDUGUJE"), qdsMaster.getBigDecimal("OSNPOCETAK"), qdsMaster.getBigDecimal("OSNPOTRAZUJE").negate()));
      qdsMaster.setBigDecimal("ISPRAVAK", util.sumValue(qdsMaster.getBigDecimal("ISPPOTRAZUJE"), qdsMaster.getBigDecimal("ISPPOCETAK"), qdsMaster.getBigDecimal("ISPDUGUJE").negate())
          .add(qdsMaster.getBigDecimal("AMORTIZACIJA")));
    }
    qdsMaster.setBigDecimal("SALDO", util.negateValue(qdsMaster.getBigDecimal("OSNOVICA"), qdsMaster.getBigDecimal("ISPRAVAK")));
//    qdsMaster.setString("CORG2", qdsDetail.getString("CORG2"));
    if (mode!='L' && mode!='O')
      qdsMaster.setTimestamp("DATPROMJENE", qdsDetail.getTimestamp("DATPROMJENE"));
    if (mode=='L') insertObrada4(qdsMaster);
  }
/**
 * Azuriranje podataka u OS_SREDSTVO kod brisanja stavke promjene
 * @param oldOsnovica
 * @param oldIspravak
 * @param mode
 */
  public void afterDeleteOS(java.math.BigDecimal oldOsnovica, java.math.BigDecimal oldIspravak, char mode) {
    System.out.println("after deleteos");
    System.out.println("Osnovica: "+oldOsnovica+", Ispravak: "+oldIspravak);
    com.borland.dx.sql.dataset.QueryDataSet qdsMaster;
    com.borland.dx.sql.dataset.QueryDataSet qdsDetail;
    if (mode=='S' || mode=='L' || mode=='N' || mode=='O' || mode=='P') {
      qdsMaster=dm.getOS_Sredstvo();
      qdsDetail=dm.getOS_Promjene();
    }
    else {
      qdsMaster=dm.getOS_SI();
      qdsDetail=dm.getOS_StSI();
    }
    if (mode=='S') {
      System.out.println("Staro");
      qdsMaster.setBigDecimal("NABVRIJED", nul);
      qdsMaster.setBigDecimal("OSNPOCETAK", nul);
      qdsMaster.setBigDecimal("ISPPOCETAK", nul);
      qdsMaster.setString("DOKUMENT", "");
      qdsMaster.setTimestamp("DATPROMJENE", nulDate);
    }
    else if (mode=='L') {
      System.out.println("Likvidacija: "+nulDate);
      qdsMaster.setString("AKTIV", "D");
      qdsMaster.setBigDecimal("OSNPOTRAZUJE", qdsMaster.getBigDecimal("OSNPOTRAZUJE").subtract(oldOsnovica));
      qdsMaster.setBigDecimal("ISPDUGUJE", qdsMaster.getBigDecimal("ISPDUGUJE").subtract(oldIspravak));
      qdsMaster.setTimestamp("DATLIKVIDACIJE", nulDate);
      qdsMaster.setBigDecimal("AMORTIZACIJA", nul);
      qdsMaster.setBigDecimal("PAMORTIZACIJA", nul);
    }
    else if (mode=='O') {
      System.out.println("Promjena orgstr");
      qdsMaster.setBigDecimal("ISPPOTRAZUJE", util.negateValue(qdsMaster.getBigDecimal("ISPPOTRAZUJE"), oldIspravak));
    }
    else {
      System.out.println("Ostalo");
      qdsMaster.setBigDecimal("OSNDUGUJE",    util.negateValue(qdsMaster.getBigDecimal("OSNDUGUJE"), oldOsnovica));
      qdsMaster.setBigDecimal("ISPPOTRAZUJE", util.negateValue(qdsMaster.getBigDecimal("ISPPOTRAZUJE"), oldIspravak));
    }
/*UNIFICIRATI FORMULU SA afterUpdateOS*/    qdsMaster.setBigDecimal("OSNOVICA", util.sumValue(qdsMaster.getBigDecimal("OSNDUGUJE"), qdsMaster.getBigDecimal("OSNPOCETAK"), qdsMaster.getBigDecimal("OSNPOTRAZUJE").negate()));
/*UNIFICIRATI FORMULU SA afterUpdateOS*/    qdsMaster.setBigDecimal("ISPRAVAK", util.sumValue(qdsMaster.getBigDecimal("ISPPOTRAZUJE"), qdsMaster.getBigDecimal("ISPPOCETAK"), qdsMaster.getBigDecimal("ISPDUGUJE").negate()));
/*UNIFICIRATI FORMULU SA afterUpdateOS*/    qdsMaster.setBigDecimal("SALDO", util.negateValue(qdsMaster.getBigDecimal("OSNOVICA"), qdsMaster.getBigDecimal("ISPRAVAK")));
    qdsMaster.post();
    qdsMaster.saveChanges();
  }
/**
 * Vraca vrijednost za stari iznos osnovice
 * @param mode
 * @return
 */
  public java.math.BigDecimal getOldOSN(char mode) {

    if (mode=='L') {
      return dm.getOS_Promjene().getBigDecimal("OSNPOTRAZUJE");
   }
    else {
      return dm.getOS_Promjene().getBigDecimal("OSNDUGUJE");
    }
  }
/**
 * Vraca vrijednost za stari iznos ispravka
 * @param mode
 * @return
 */
  public java.math.BigDecimal getOldISP(char mode) {
    if (mode=='L') {
      return dm.getOS_Promjene().getBigDecimal("ISPDUGUJE");
    }
    else {
      return dm.getOS_Promjene().getBigDecimal("ISPPOTRAZUJE");
    }
  }
  public java.math.BigDecimal calcAmortizacija(java.math.BigDecimal osnovica, int mes1, int mes2, java.math.BigDecimal stopa) {
    return util.multiValue(
                            util.divideValue
                            (
                              osnovica,
                              new java.math.BigDecimal(12)
                            ),
                            util.multiValue
                            (
                              new java.math.BigDecimal((mes2+1-mes1)/100.0)
                            , stopa));
  }
/*  public java.math.BigDecimal calcAmortizacija(java.math.BigDecimal osnovica, int mes1, int mes2, java.math.BigDecimal stopa) {
    dm.getOS_Obrada4().setBigDecimal("Amortizacija",
      dm.getOS_Obrada4().getBigDecimal("Osnovica") * mjesec.doubleValue() / 12 *
      vl.RezSet.getDouble("zStopa") / 100);

    return util.multiValue(
    );
  }*/
  // SITNIO INVENTAR -> Rade

  /**
 * Vraca vrijednost za stari iznos osnovice
 * @param mode
 * @return
 */
  public java.math.BigDecimal getOldSIOSN(char mode) {

    if (mode=='Z') {
      return dm.getOS_StSI().getBigDecimal("OSNPOTRAZUJE");
   }
    else {
      return dm.getOS_StSI().getBigDecimal("OSNDUGUJE");
    }
  }
/**
 * Vraca vrijednost za stari iznos ispravka
 * @param mode
 * @return
 */
  public java.math.BigDecimal getOldSIISP(char mode) {
    if (mode=='Z') {
      return dm.getOS_StSI().getBigDecimal("ISPDUGUJE");
    }
    else {
      return dm.getOS_StSI().getBigDecimal("ISPPOTRAZUJE");
    }
  }

  public int getOS_PromjeneMaxRBR(String corg, String invbroj, char stat) {    
    String qStr= " select max(rbr) as rbr from os_promjene where os_promjene.corg2='"+corg+"' and os_promjene.invbroj='"+invbroj+"' and status='"+stat+"'";    
    System.out.println("QSTR: " + qStr);
    DataSet ds = Util.getNewQueryDataSet(qStr);
    return ds.getInt("RBR")+1;
  }

  public String getSifraObrAmor() {
    String qStr = "select cpromjene as cpromjene from os_vrpromjene where tippromjene='A'";
    DataSet ds = Util.getNewQueryDataSet(qStr);    
    return ds.getString("CPROMJENE");
  }
  public String getSifraPriprema() {    
    String qStr = "select cpromjene as cpromjene from os_vrpromjene where tippromjene='U'";
    DataSet ds = Util.getNewQueryDataSet(qStr);    
    return ds.getString("CPROMJENE");
  }
  public java.math.BigDecimal getAmor(java.sql.Timestamp datum, java.math.BigDecimal osnovica, String ibr, String org) {
    double mjesec;
    int mjesecL, mjesecP, godinaL, godinaP;
    java.math.BigDecimal amor;
    String qStr;
    qStr="select os_amgrupe.zakstopa as zs, os_amgrupe.odlstopa, os_sredstvo.datpromjene as dat from os_sredstvo, os_amgrupe "+
    "where os_sredstvo.cgrupe=os_amgrupe.cgrupe and os_sredstvo.invbroj='"+ibr+"' and os_sredstvo.corg='"+org+"'";
    vl.execSQL(qStr);
    vl.RezSet.open();
    cal.setTime(datum);
    mjesecL=cal.get(Calendar.MONTH);
    godinaL=cal.get(Calendar.YEAR);
    cal.setTime(vl.RezSet.getTimestamp("dat"));
    mjesecP=cal.get(Calendar.MONTH);
    godinaP=cal.get(Calendar.YEAR);
    if (godinaL == godinaP) {
      mjesec=(double) (mjesecL-mjesecP);
    }
    else {
      mjesec=mjesecL+1;
    }
    amor = (util.multiValue(util.multiValue(
        util.divideValue(osnovica, new java.math.BigDecimal(12)), 
        util.divideValue(vl.RezSet.getBigDecimal("zs"), util.sto)), new java.math.BigDecimal(mjesec)));
    System.out.println("Mjesec: ");
    System.out.println("Amortizacija: "+amor);
    return amor;
  }
  /**
   * Puni datumska polja u tablici OS_SREDSTVO ili OS_SI
   * @param qdsMaster
   * @param qdsDetail
   */
  private void findDates(DataSet qdsMaster, DataSet qdsDetail) {
    qdsMaster.setTimestamp("DATNABAVE", qdsDetail.getTimestamp("DATPROMJENE"));
    if (qdsMaster.getString("STATUS").equals("A")) {
      qdsMaster.setTimestamp("DATAKTIVIRANJA", qdsDetail.getTimestamp("DATPROMJENE"));
    }
  }
  public void emptyAmorTable() {
    emptyAmorTable(true);
  }
  public void emptyAllAmorTable() {

  }
  public boolean emptyAmorTable(boolean poruka) {
//    vl.runSQL("update os_sredstvo SET Amortizacija=0, PAmortizacija=0, RevOsn=0, RevIsp=0, RevAmor=0 where datLikvidacije is null");
//    String qStrTest = "delete from os_promjene where os_promjene.CPROMJENE='"+cProm+"' and os_promjene.DATPROMJENE='"+dm.getOS_Metaobrada().getTimestamp("DATUMDO").toString()+"'";
//    vl.runSQL(qStrTest);
//    System.out.println("TEST: " + qStrTest);
    
    dm.getOS_Obrada1().refresh();
    dm.getOS_Obrada2().refresh();
    dm.getOS_Obrada3().refresh();
    dm.getOS_Obrada4().refresh();
    dm.getOS_Metaobrada().refresh();
    
    hr.restart.robno.Util.getUtil().emptyTable(dm.getOS_Obrada1());
    hr.restart.robno.Util.getUtil().emptyTable(dm.getOS_Obrada2());
    hr.restart.robno.Util.getUtil().emptyTable(dm.getOS_Obrada3());
//    hr.restart.robno.Util.getUtil().emptyTable(dm.getOS_Obrada4());
    hr.restart.robno.Util.getUtil().emptyTable(dm.getOS_Obrada5());
    hr.restart.robno.Util.getUtil().emptyTable(dm.getOS_Metaobrada());
    if (poruka) {
      JOptionPane.showConfirmDialog(null,"Obraèun amortizacije je poništen !","Informacija",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
    }
    return true;
  }

  /**
   * Poništenje zadnje amortizacije
   */


  public void deleteAmortizacija() {
    if (!checkOSKontrola()) {
      return;
    }
    String cProm = osUtil.getUtil().getSifraObrAmor();
    if (cProm.equals("")) {
      JOptionPane.showConfirmDialog(null,"Ne postoji definirana vrsta promjene za amortizaciju !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
    dm.getOS_Log().open();
    if (dm.getOS_Log().getRowCount()==0) {
      JOptionPane.showConfirmDialog(null,"Nema aktivnih obraèuna !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
    dm.getOS_Log().last();

    vl.runSQL(sjQuerys.updatePromjeneAmor(cProm, dm.getOS_Log().getTimestamp("DATDO")));
    vl.runSQL(sjQuerys.updateSredstvaAmor());
    emptyAmorTable(false);
  }

  /**
   * Poništenje svih amortizacija
   */

  public void deleteAllAmortizacija() {
    if (!checkOSKontrola()) {
      return;
    }
    String cProm = osUtil.getUtil().getSifraObrAmor(); // Šifra vrste promjene za amortizaciju
    if (cProm.equals("")) {
      JOptionPane.showConfirmDialog(null,"Ne postoji definirana vrsta promjene za amortizaciju !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
    dm.getOS_Log().open();
    if (dm.getOS_Log().getRowCount()==0) {
      JOptionPane.showConfirmDialog(null,"Nema aktivnih obraèuna !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return;
    }
    dm.getOS_Log().last();
    if (JOptionPane.showConfirmDialog(null,"Da li \u017Eelite poništiti sve obrade ?","Brisanje svih obrada",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
      if (!emptyAmorTable(false)) {
        return;
      }
      vl.runSQL(sjQuerys.delPromjeneAmor(cProm, dm.getOS_Log().getTimestamp("DATDO")));
      vl.runSQL(sjQuerys.delSredstvaAmor());
      hr.restart.robno.Util.getUtil().emptyTable(dm.getOS_Log());
      hr.restart.robno.Util.getUtil().emptyTable(dm.getOS_Arhiva());
      dm.getOS_Kontrola().setString("MJESEC", "  ");
      dm.getOS_Kontrola().saveChanges();
      JOptionPane.showConfirmDialog(null,"Svi obraèuni amortizacija su poništeni !","Informacija",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
    }

  }
  public String getInvBroj() {
    if ("D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","kalkchVC","D"))) {
    }
    String cVrati="";
    try {
      vl.execSQL(sjQuerys.getMaxInvBroj(hr.restart.zapod.OrgStr.getKNJCORG()));
      vl.RezSet.open();
    } catch (Exception e) {
      vl.execSQL(sjQuerys.getMaxInvBroj(hr.restart.zapod.OrgStr.getKNJCORG(),true));
      vl.RezSet.open();
    }
    cVrati = "0000000000" +
      String.valueOf(Aus.getAnyNumber(vl.RezSet.getString(0)) + 1);
    try {
      vl.execSQL("select INVBROJ from os_sredstvo where corg='"+hr.restart.zapod.OrgStr.getKNJCORG()+"'");
      vl.RezSet.open();
      vl.RezSet.first();
      int len = vl.RezSet.getString("INVBROJ").length();
 //     System.out.println("Int: " + vl.RezSet.getString(0).length());
      cVrati = cVrati.substring(cVrati.length() - len, cVrati.length());
      System.out.println("Vrati: " + cVrati);
    }
    catch (Exception ex) {
      cVrati="00001";
    }
    return cVrati;
  }
  public int getAmorRBR() {
    int nVrati=0;
    vl.execSQL(sjQuerys.getAmorRBR(hr.restart.zapod.OrgStr.getKNJCORG()));
    vl.RezSet.open();
    nVrati=vl.RezSet.getInt(0)+1;
    return nVrati;
  }
  public boolean checkOSKontrola() {
    if (ld.raLocate(dm.getOS_Kontrola(),
      new java.lang.String[] {"CORG"},
      new java.lang.String[] {hr.restart.zapod.OrgStr.getKNJCORG()})) {
      return true;
    }
    else {
      JOptionPane.showConfirmDialog(null ,"Odabrano knjigovodstvo nije aktivno u osnovnim sredstvima !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }
  void insertObrada4(com.borland.dx.sql.dataset.QueryDataSet qds) {
    System.out.println("iznosi: "+qds.getBigDecimal("OSNPOCETAK")+", "+qds.getBigDecimal("OSNDUGUJE"));
    int mjesecL, mjesecP, godinaL, godinaP;
    cal.setTime(qds.getTimestamp("DatLikvidacije"));
    mjesecL=cal.get(Calendar.MONTH);
    godinaL=cal.get(Calendar.YEAR);
    cal.setTime(qds.getTimestamp("DATAKTIVIRANJA"));
    mjesecP=cal.get(Calendar.MONTH);
    godinaP=cal.get(Calendar.YEAR);
    dm.getOS_Obrada4().open();
    dm.getOS_Obrada4().refresh();
    dm.getOS_Obrada4().insertRow(true);
    dm.getOS_Obrada4().setString("CORG",            qds.getString("cOrg"));
    dm.getOS_Obrada4().setString("CORG",            qds.getString("cOrg2"));
    dm.getOS_Obrada4().setString("InvBroj",         qds.getString("InvBroj"));
    dm.getOS_Obrada4().setString("NazSredstva",     qds.getString("NazSredstva"));
    dm.getOS_Obrada4().setString("cLokacije",       qds.getString("cLokacije"));
    dm.getOS_Obrada4().setString("cArtikla",        qds.getString("cArtikla"));
    dm.getOS_Obrada4().setInt("CPAR",               qds.getInt("CPAR"));
    dm.getOS_Obrada4().setString("BROJKONTA",       qds.getString("BrojKonta"));
    dm.getOS_Obrada4().setString("cGrupe",          qds.getString("cGrupe"));
//    dm.getOS_Obrada4().setBigDecimal("zakStopa",    qds.getBigDecimal("zakStopa"));
//    dm.getOS_Obrada4().setBigDecimal("odlStopa",    qds.getBigDecimal("odlStopa"));
    dm.getOS_Obrada4().setString("cSkupine",        qds.getString("cSkupine"));
//    dm.getOS_Obrada4().setBigDecimal("Koeficijent", qds.getBigDecimal("Koeficijent"));
    dm.getOS_Obrada4().setTimestamp("DatPromjene",  qds.getTimestamp("DATPROMJENE"));
    dm.getOS_Obrada4().setTimestamp("DatLikvidacije",qds.getTimestamp("DatLikvidacije"));
    dm.getOS_Obrada4().setBigDecimal("Osnovica",    util.sumValue(qds.getBigDecimal("OsnPocetak"), qds.getBigDecimal("OsnDuguje")));
    dm.getOS_Obrada4().setBigDecimal("Ispravak",    /*util.negateValue(*/util.sumValue(qds.getBigDecimal("IspPocetak"), qds.getBigDecimal("IspPotrazuje"))/*, qds.getBigDecimal("AMORTIZACIJA"))*/);
    dm.getOS_Obrada4().setBigDecimal("SadVrijed",   util.negateValue(dm.getOS_Obrada4().getBigDecimal("Osnovica"), dm.getOS_Obrada4().getBigDecimal("Ispravak")));
    dm.getOS_Obrada4().setBigDecimal("UIspravak", util.sumValue(dm.getOS_Obrada4().getBigDecimal("Ispravak"),qds.getBigDecimal("AMORTIZACIJA")));

    dm.getOS_Obrada4().setBigDecimal("Amortizacija", qds.getBigDecimal("AMORTIZACIJA"));
    dm.getOS_Obrada4().setBigDecimal("RevOsn", util.nul);
    dm.getOS_Obrada4().setBigDecimal("RevIsp", util.nul);
    dm.getOS_Obrada4().setBigDecimal("RevSad", util.nul);
    if (dm.getOS_Obrada4().getBigDecimal("Amortizacija").doubleValue() > dm.getOS_Obrada4().getBigDecimal("SadVrijed").doubleValue()) {
      dm.getOS_Obrada4().setBigDecimal("Amortizacija", dm.getOS_Obrada4().getBigDecimal("SadVrijed"));
      dm.getOS_Obrada4().setBigDecimal("UIspravak", dm.getOS_Obrada4().getBigDecimal("Ispravak").add(dm.getOS_Obrada4().getBigDecimal("Amortizacija")));
    }
    dm.getOS_Obrada4().setBigDecimal("USadVrijed",   util.negateValue(dm.getOS_Obrada4().getBigDecimal("Osnovica"), dm.getOS_Obrada4().getBigDecimal("UIspravak")));    
    if (godinaL == godinaP) {
      dm.getOS_Obrada4().setString("Mjesec", String.valueOf(mjesecL-mjesecP));
    }
    else {
      dm.getOS_Obrada4().setString("Mjesec", String.valueOf(mjesecL+1));
    }
   dm.getOS_Obrada4().post();
   dm.getOS_Obrada4().saveChanges();
  }
  public void deleteObrada4(com.borland.dx.sql.dataset.QueryDataSet qds) {
    vl.runSQL(sjQuerys.deleteOBR4(qds.getString("INVBROJ"), qds.getString("CORG2")));
  }
  public java.sql.Timestamp findOSDate() {
    if (Integer.parseInt(dm.getOS_Kontrola().getString("GODINA"))==Integer.parseInt(vl.findYear(vl.getToday()))) {
      return hr.restart.util.Valid.getValid().findDate(false, 0);
    }
    return hr.restart.util.Util.getUtil().getLastDayOfYear(util.findFirstDayOfYear(Integer.parseInt(dm.getOS_Kontrola().getString("GODINA"))));
  }
}