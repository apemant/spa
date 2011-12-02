/****license*****************************************************************
**   file: raObracunPL.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Odbici;
import hr.restart.baza.Odbiciobr;
import hr.restart.baza.Povjerioci;
import hr.restart.baza.Vrsteodb;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;
import hr.restart.zapod.OrgStr;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raObracunPL {
sysoutTEST ST = new sysoutTEST(false);
  private static raObracunPL _this;
  private Util ut = Util.getUtil();
  private lookupData ld = lookupData.getlookupData();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private BigDecimal nula = new BigDecimal(0.00);
  private BigDecimal sto = new BigDecimal(100.00);
  raOdbici odbici = raOdbici.getInstance();
  short godina;
  short mjesec;
  short rbr;
  private String currCorg;
  String corg;
  Timestamp datumispl;
  String[] orgs;
  private boolean obrdoprinosa;
  private boolean obrporeza;
  private boolean obrprireza;
  private boolean obrkredita;
  boolean uselimitsPOR;
  boolean uselimitsDOP;
  boolean calcrazpor;
  //u loopPrimanja(3) formira osnovicu za kredite u odnosu na flag na vrstama primanja
  private BigDecimal osnovicaZaKredit = nula.setScale(8);
  private BigDecimal osnovicaZaHarach = nula.setScale(8);
  /**
   * vrijednosti ranije isplacenih placa u mjesecu isplate ovog obracuna
   * mjVals[0] = porosn; mjVals[1] = iskneop
   */
  private BigDecimal[] mjVals;//mjVals[0] = porosn; mjVals[1] = iskneop; mjVals[2] = por1; mjVals[3] = por2; mjVals[4] = por3...,por4,por5; mjVals[7] = bruto

  //setovi
  QueryDataSet vrsteprim;
  QueryDataSet radnici;
  QueryDataSet primanja;
  QueryDataSet kumulrad;
  QueryDataSet kumulorg;
  QueryDataSet odbiciobr;
  //statementi
  //raPreparedStatement addodbitak = new raPreparedStatement("odbiciobr",raPreparedStatement.INSERT);
  private String cvrodbKoefOlak[];

  private raIniciranje inicir = raIniciranje.getInstance();
  protected raObracunPL() {
    cvrodbKoefOlak = odbici.getVrsteOdbKeysQuery("RA","K","1","4",true);
  }
  public static raObracunPL getInstance() {
    if (_this==null) _this = new raObracunPL();
    return _this;
  }

  public void initObracun(short _godina, short _mjesec, short _rbr, String _corg, java.sql.Timestamp _datumispl) {
    godina = _godina;
    mjesec = _mjesec;
    rbr = _rbr;
    corg = _corg;
    datumispl = _datumispl;
  }

  public boolean ponobracun() {
//    System.out.println("ponistavam obracun za "+godina+"/"+mjesec+"-"+rbr+" oj: "+corg);
    return new raLocalTransaction() {
      public boolean transaction() throws Exception {
        ponObracunTrans();
        return true;
      }
    }.execTransaction();
  }
  public boolean obracun(final boolean doprinosa, final boolean poreza, final boolean kredita, final boolean prireza, final boolean _uselimitsPOR, final boolean _uselimitsDOP, final boolean _calcrazpor) {
    obrdoprinosa = doprinosa;
    obrporeza = poreza;
    obrkredita = kredita;
    obrprireza = prireza;
    uselimitsPOR = _uselimitsPOR;
    uselimitsDOP = _uselimitsDOP;
    calcrazpor = _calcrazpor;
    dm.getParametripl().open();
//    System.out.println("pravim obracun za "+godina+"/"+mjesec+"-"+rbr+" oj: "+corg);
//    return true;
    return new raLocalTransaction() {
      public boolean transaction() throws Exception {
        try {
          return makeObracun();
        }
        catch (Exception ex) {
          ex.printStackTrace();
          dm.getOrgpl().refresh();
          throw ex;
//          return false;
        }
      }
    }.execTransaction();
  }

  private void ponObracunTrans() throws Exception {
    try {
      initOrgs();
      PreparedStatement delkumulorg = raTransaction.getPreparedStatement("DELETE FROM kumulorg where kumulorg.corg = ?");
      PreparedStatement delkumulrad = raTransaction.getPreparedStatement("DELETE FROM kumulrad where kumulrad.cradnik = ?");
      PreparedStatement delodbiciobr = raTransaction.getPreparedStatement("DELETE FROM odbiciobr where odbiciobr.cradnik = ?");
      PreparedStatement delrsperiodobr = raTransaction.getPreparedStatement("DELETE FROM rsperiodobr where rsperiodobr.cradnik = ?");
      for (int i = 0; i < orgs.length; i++) {
        inicir.posOrgsPl(orgs[i]);
        String parObr = raParam.getParam(dm.getOrgpl(),1);

        if (parObr.equals("O")) {
          delkumulorg.setString(1,orgs[i]);
          delkumulorg.executeUpdate();
          QueryDataSet cradniks = Util.getNewQueryDataSet("SELECT cradnik FROM radnici where corg = '"+orgs[i]+"'");
          cradniks.first();
          do {
            delodbiciobr.setString(1,cradniks.getString("CRADNIK"));
            delodbiciobr.executeUpdate();
            delkumulrad.setString(1,cradniks.getString("CRADNIK"));
            delkumulrad.executeUpdate();
            delrsperiodobr.setString(1,cradniks.getString("CRADNIK"));
            delrsperiodobr.executeUpdate();
            backPrimanja(cradniks.getString("CRADNIK"));
          } while (cradniks.next());
        }
      }
      for (int i = 0; i < orgs.length; i++) {
        inicir.posOrgsPl(orgs[i]);
        raParam.setParam(dm.getOrgpl(),1,"I");
      }
      raTransaction.saveChanges(dm.getOrgpl());
    }
    catch (SQLException ex) {
      dm.getOrgpl().refresh();
      ex.printStackTrace();
      throw ex;
    }
  }
  private void backPrimanja(String crad) throws Exception {
/** @todo
     String nacobrb = radnici.getString("NACOBRB");
     if (nacobrb.equals("0")) {
      nacobrb = dm.getOrgpl().getString("NACOBRB");
    }
    (nacobrb.equals("6") || nacobrb.equals("7"));
 */
    String nacobrb = dm.getOrgpl().getString("NACOBRB");
    if (!(nacobrb.equals("6") || nacobrb.equals("7"))) return;
    QueryDataSet _primanja = Util.getNewQueryDataSet("SELECT * FROM primanjaobr where cradnik = '"+crad+"'");
    if (_primanja.getRowCount() == 0) return;
    _primanja.first();
    do {
      _primanja.setBigDecimal("BRUTO",_primanja.getBigDecimal("NETO"));
    } while (_primanja.next());
    raTransaction.saveChanges(_primanja);
  }
  private boolean makeObracun() throws Exception {
    //get orgove
    //loop orgove i orgspl
    //get radnike
    //loop radnike
      //get zarade
      //loop zarade
        // find osnovice za odbitke
      //
      // if doprinosa obrdop, obrdopNa, azurZar
      // if poreza obrpor
      // if kredita obrkred
    // end loop radnike

    initOrgs();
    kumulorg = Util.getNewQueryDataSet("SELECT * FROM kumulorg");
    for (int i = 0; i < orgs.length; i++) {
      inicir.posOrgsPl(orgs[i]);
      currCorg = orgs[i];
      radnici = Util.getNewQueryDataSet("SELECT * FROM radnicipl where corg = '".concat(currCorg).concat("'"));
// ---------------- loop radnici -----------
      loopRadnici();
      raParam.setParam(dm.getOrgpl(),1,"O");
      dm.getOrgpl().setTimestamp("DATUMISPL",datumispl);
      raTransaction.saveChanges(dm.getOrgpl());
    }
    raTransaction.saveChanges(kumulorg);
    //cleaning
    vrsteprim = null;
    radnici = null;
    primanja = null;
    kumulrad = null;
    kumulorg = null;
    odbiciobr = null;

    return true;
  }
  private void posKumulOrg(String corg, String cvro) {
    if (!ld.raLocate(kumulorg,new String[] {"CORG","CVRO"},new String[] {corg,cvro})) {
      kumulorg.insertRow(false);
      kumulorg.setString("CORG",corg);
      kumulorg.setString("KNJIG",OrgStr.getKNJCORG());
      kumulorg.setString("CVRO",cvro);
      kumulorg.post();
//      posKumulOrg(corg,cvro);
    }
  }
  private void loopRadnici() throws Exception {
    int radrc = radnici.getRowCount();
    int cnt = 1;
    if (radrc == 0) return;
    radnici.first();
    do {
//System.out.println("Rolling shljaker "+radnici.getString("CRADNIK"));
      firePropertyChange("msg", "", "Org.jed. "+radnici.getString("CORG")+" - obraèun plaæe za radnika "+cnt+" od "+radrc);
      cnt++;
      primanja = Util.getNewQueryDataSet("SELECT * FROM primanjaobr where cradnik = '"+radnici.getString("CRADNIK")+"'");
      odbiciobr = Util.getNewQueryDataSet("SELECT * FROM odbiciobr where cradnik = '"+radnici.getString("CRADNIK")+"'");
      if (primanja.getRowCount() != 0) {
        odbici.setRadniciPL(radnici);
        kumulrad = Util.getNewQueryDataSet("SELECT * FROM kumulrad where cradnik = '"+radnici.getString("CRADNIK")+"'");
        if (kumulrad.getRowCount() > 0) kumulrad.empty();
        kumulrad.insertRow(true);
        kumulrad.setString("CRADNIK",radnici.getString("CRADNIK"));
        kumulrad.post();
        kumulrad.first();
        if (obrdoprinosa) {
          loopPrimanja(1); // zbraja za osnovice radnika (bruto)
/** @todo neto2 -> bruto */
          calcNetoUBruto();
          calcDoprinosiR(); // racuna doprinose radnika i azurira kumulrad
          calcDoprinosiNa(); // racuna doprinose poduzeca i azurira kumulorg
          loopPrimanja(2); // rasporedjuje proporcionalno doprinose po zaradama
        }
        if (obrporeza) {
          calcPorez();
          calcRazPor(); //Godisnji obracun poreza           
          calcDoNetaPK();
          loopPrimanja(3); // rasporedjuje proporcionalno neto2 po zaradama
        }
        kumulrad.setBigDecimal("NARUKE",kumulrad.getBigDecimal("NETOPK"));
        calcHarach();
        if (obrkredita) {
          calcKrediti();
        }
        addNetoOdb(); //dodaje sistemski odbitak neta za virmane
//debug
//ST.prn(odbiciobr);
//gubed
        raTransaction.saveChanges(odbiciobr);
        raTransaction.saveChanges(primanja);
//System.out.println("SAD STISNI CTRL-C !!!!!!!!!!!");
//Thread.sleep(1500);
        raTransaction.saveChanges(kumulrad);
        makeRS(); // provjerava rspedriod i puni rsperiodobr
      }
//System.out.println("gotov shljaker "+radnici.getString("CRADNIK"));
    } while (radnici.next());
  }
 
  /**
   * 
   */
  private QueryDataSet posVrsteprim(DataSet prim) {
    if (vrsteprim == null) vrsteprim = Util.getNewQueryDataSet("SELECT * FROM vrsteprim");
    ld.raLocate(vrsteprim,new String[] {"CVRP"},new String[] {""+prim.getShort("CVRP")});
    return vrsteprim;
  }
  private void loopPrimanja(int mode) throws Exception {
    if (primanja.getRowCount() == 0) return;
    BigDecimal radbruto = null;
    BigDecimal raddopr = null;
    if (mode > 1) {
      radbruto = kumulrad.getBigDecimal("BRUTO");
    }
    if (mode == 2) {
      raddopr = kumulrad.getBigDecimal("DOPRINOSI");
    } else if (mode == 3) {
      raddopr = kumulrad.getBigDecimal("NETO2");
      osnovicaZaKredit = nula.setScale(8);
      osnovicaZaHarach = nula.setScale(8);
    }
    primanja.first();
    do {
      DataSet _vrsteprim = posVrsteprim(primanja);
      if (mode == 1) {// zbrojiti za osnovice radnika
        if (raParam.getParam(_vrsteprim,1).equals("D")) { //obr doprinosa
          addBigDec_kumulrad("BRUTO",primanja.getBigDecimal("BRUTO"));
          addBigDec_kumulrad("SATI",primanja.getBigDecimal("SATI"));
        } else {
          if (raParam.getParam(_vrsteprim,2).equals("D")) {//ide samo u neto
            addBigDec_kumulrad("NETO",primanja.getBigDecimal("BRUTO"));
          } else {//naknada
            addBigDec_kumulrad("NAKNADE",primanja.getBigDecimal("BRUTO"));
          }
        }

      } else if (mode == 2) {/* rasporediti proporcionalno doprinose po zaradama */
        // dopZ/dopR = brutZ/brutR => dopZ = dopR * (brutZ/brutR)
        if (raParam.getParam(_vrsteprim,1).equals("D")) {
          BigDecimal propkoef;
          if (radbruto.signum() == 0) {
            propkoef = nula;
          } else {
            propkoef = ut.setScale(primanja.getBigDecimal("BRUTO"),8).divide(radbruto,8,BigDecimal.ROUND_HALF_UP);
          }
          BigDecimal doprinosi = raddopr.multiply(propkoef);
//System.out.println("primanja.setBigDecimal('DOPRINOSI',"+ut.setScale(doprinosi,2)+");");
          primanja.setBigDecimal("DOPRINOSI",ut.setScale(doprinosi,2));
        } else {
          primanja.setBigDecimal("DOPRINOSI",nula);
        }
      } else if (mode == 3) {// proprocionalno netopk po zaradama
        if (raParam.getParam(_vrsteprim,2).equals("N")) {//ne obracunava se porez NETO = BRUTO - DOPRINOSI
          if (!isNeto_Bruto()) {
            primanja.setBigDecimal("NETO",primanja.getBigDecimal("BRUTO").add(primanja.getBigDecimal("DOPRINOSI").negate()));
          } else {//neto = bruto(=neto2); bruto = bruto(=neto2)+doprinosi
            primanja.setBigDecimal("NETO",primanja.getBigDecimal("BRUTO"));
            primanja.setBigDecimal("BRUTO",primanja.getBigDecimal("BRUTO").add(primanja.getBigDecimal("DOPRINOSI")));
          }
        } else {
          if (!isNeto_Bruto()) {
            BigDecimal propkoef;
            if (radbruto.signum() == 0) {
              propkoef = nula;
            } else {
              propkoef = ut.setScale(primanja.getBigDecimal("BRUTO"),8).divide(radbruto,8,BigDecimal.ROUND_HALF_UP);
            }
            BigDecimal neto2 = raddopr.multiply(propkoef);//raddopr = kumulrad.NETO2
            primanja.setBigDecimal("NETO",neto2);
          } else {//                           pr.bruto=neto2                        raddopr = kumulrad.NETO2
            primanja.setBigDecimal("NETO",primanja.getBigDecimal("BRUTO"));
            BigDecimal propkoef;
            if (raddopr.signum() == 0) {
              propkoef = nula;
            } else {
              propkoef = ut.setScale(primanja.getBigDecimal("BRUTO"),8).divide(raddopr,8,BigDecimal.ROUND_HALF_UP);
            }
            BigDecimal bruto = radbruto.multiply(propkoef);//pr.bruto = radbruto*(pr.bruto(=neto2)/rad.neto2)
            primanja.setBigDecimal("BRUTO",bruto);
          }
        }
        primanja.post();
        //osnovicaZaKredit
        if (raParam.getParam(_vrsteprim,3).equals("D")) {
          osnovicaZaKredit = osnovicaZaKredit.add(primanja.getBigDecimal("NETO"));
        }
        if (raParam.getParam(_vrsteprim,4).equals("D") || raParam.getParam(_vrsteprim,4).trim().equals("")) {
          osnovicaZaHarach = osnovicaZaHarach.add(primanja.getBigDecimal("NETO"));
        }
        addRSsum();
      }
    } while (primanja.next());
  }
  private void calcDoprinosiR() throws Exception {
    /*
    odbici.setPreSum_calcOdbitak("bruto",kumulrad.getBigDecimal("BRUTO"));
    //osnovica za doprinose
    QueryDataSet kalk_odbici_bruto = odbici.getOdbici("RA","K","1","1",
                  kumulrad.getString("CRADNIK"),null,"odbici",null,odbici.DEF);
    */
    //doprinosi
    QueryDataSet doprinosi = odbici.getDoprinosiRadnik(radnici.getString("CRADNIK"),raOdbici.DEF);
    addBigDec_kumulrad("NETO",kumulrad.getBigDecimal("BRUTO"));//dodaje zato sto postoji mogucnost zarada kojima se ne racunaju doprinosi
    BigDecimal[] sumStopaIznos = calcOdbiciRadnik(doprinosi,"NETO",true);
    //napunim kumulrad.doprinosi
    kumulrad.setBigDecimal("DOPRINOSI",sumStopaIznos[1]); // = bruto - neto
    posKumulOrg(currCorg,radnici.getString("CVRO"));
    // dodajem na kumulorg
    addBigDec_kumulorg("SATI",kumulrad.getBigDecimal("SATI"));
    addBigDec_kumulorg("BRUTO",kumulrad.getBigDecimal("BRUTO"));
    addBigDec_kumulorg("DOPRINOSI",kumulrad.getBigDecimal("DOPRINOSI"));
    addBigDec_kumulorg("NETO",kumulrad.getBigDecimal("NETO"));
  }

  private void calcDoprinosiNa() throws Exception {
    QueryDataSet doprinosi = odbici.getDoprinosiNa(radnici.getString("CRADNIK"),raOdbici.DEF);
    BigDecimal[] sumStopaIznos = calcOdbiciRadnik(doprinosi,null,false);
    posKumulOrg(currCorg,radnici.getString("CVRO"));
    //dodajem na kumulorg
    addBigDec_kumulorg("DOPRPOD",sumStopaIznos[1]);
  }

  /**
   * racuna odbitke za tekuci kumulrad
   * @param odbici odbici za izracunati
   * @param negcol kolona kumulrad od koje treba oduzeti odbitak
   * @param update da li oduzeti taj odbitak? ako je false negcol se ignorira
   * @return BigDecimal[0] = suma stopa i BigDecimal[1] = suma iznosa obracunatih zadanih doprinosa
   * @throws Exception
   */
  private BigDecimal[] calcOdbiciRadnik(QueryDataSet qodbici, String negcol,boolean update) throws Exception {
    BigDecimal sumstopa = new BigDecimal("0.00000000");
    BigDecimal sumiznos = Aus.zero2;
    mjVals = getMjVals();
    if (uselimitsDOP) {
      //prijasnja osnovica za doprinose
      odbici.setPreSum_calcOdbitak("preosn",mjVals[7]);
      //
    } else {
      odbici.setPreSum_calcOdbitak("preosn",ut.setScale(nula,2));
    }

    qodbici.first();
    do {
      raOdbici.CalcRes clcres = odbici.calcOdbitak(kumulrad,qodbici,negcol,update); //umanji kolonu kumulrad.neto za
                                                                                   //iznos obracunatog odbitka
                                                                                  //i vrati obracunate vrijednosti
      if (clcres != null) {
        //sa queryDataSetom
        odbiciobr.insertRow(false);
        setValues(qodbici,odbiciobr);
        odbiciobr.setString("CRADNIK",kumulrad.getString("CRADNIK"));
        odbiciobr.setShort("CVRP",Short.parseShort("0"));
        odbiciobr.setShort("RBR",Short.parseShort("0"));
        odbiciobr.setBigDecimal("OBRIZNOS",ut.setScale(clcres.obriznos,2));
        odbiciobr.setBigDecimal("OBRSTOPA",ut.setScale(clcres.obrstopa,2));
        odbiciobr.setBigDecimal("OBROSN",ut.setScale(clcres.obrosn,2));
        odbiciobr.setBigDecimal("SALDO",ut.setScale(clcres.saldo,2));
        odbiciobr.post();
        /*sa prepareStatmentom
        addodbitak.setValues(qodbici);
        addodbitak.setString("CRADNIK",kumulrad.getString("CRADNIK"),false);
        addodbitak.setShort("CVRP",Short.parseShort("0"),false);
        addodbitak.setShort("RBR",Short.parseShort("0"),false);
        addodbitak.setBigDecimal("OBRIZNOS",clcres.obriznos,false);
        addodbitak.setBigDecimal("OBRSTOPA",clcres.obrstopa,false);
        addodbitak.setBigDecimal("OBROSN",clcres.obrosn,false);
        addodbitak.executeUpdate();
        */
        sumstopa = sumstopa.add(clcres.obrstopa);
        sumiznos = sumiznos.add(ut.setScale(clcres.obriznos,2));
      }
    } while (qodbici.next());
    return new BigDecimal[] {sumstopa,sumiznos};
  }
  private void setValues(ReadRow src, ReadWriteRow dest) {
    String[] dcolNames = dest.getColumnNames(dest.getColumnCount());
    String[] scolNames = src.getColumnNames(src.getColumnCount());
    Variant v = new Variant();
    for (int i = 0; i < scolNames.length; i++) {
      if (ut.containsArr(dcolNames,scolNames[i])) {
        src.getVariant(scolNames[i],v);
        dest.setVariant(scolNames[i],v);
      }
    }
  }
  private boolean isOlakIncluded(QueryDataSet qol) {
    for (int i = 0; i < cvrodbKoefOlak.length; i++) {
      if (ld.raLocate(qol,"CVRODB",cvrodbKoefOlak[i])) return true;
    }
    return false;
  }
  private void calcPremije() throws Exception {
    posKumulOrg(currCorg,radnici.getString("CVRO"));
    QueryDataSet qpremije = odbici.getPremije(radnici.getString("CRADNIK"),raOdbici.DEF);
    if (qpremije.getRowCount() == 0) return;
    mjVals = getMjVals();
    if (mjVals[7].compareTo(nula)>0) {
      if (frmParam.getParam("pl","forceprem","N","Forsirati obraèun premija kod obraèuna").equals("N")) {
        return;
      }
    }
    BigDecimal[] sumStopaIznos = calcOdbiciRadnik(qpremije,"POROSN", true);
  }
  
  private BigDecimal[] calcPausOdbitak() throws Exception {
    posKumulOrg(currCorg,radnici.getString("CVRO"));
    QueryDataSet qpausodb = odbici.getPausalniOdbitak(radnici.getString("CRADNIK"),raOdbici.DEF);
    if (qpausodb.getRowCount() == 0) return null;
    BigDecimal[] sumStopaIznos = calcOdbiciRadnik(qpausodb,"POROSN", true);
    return sumStopaIznos;
  }
  
  private void calcOlaksice() throws Exception {
    posKumulOrg(currCorg,radnici.getString("CVRO"));
    kumulrad.setBigDecimal("POROSN",kumulrad.getBigDecimal("NETO"));//ovo bi trebao umanjiti calcPremije()
    calcPremije();
    BigDecimal[] pausStopaIznos = calcPausOdbitak(); 
    if (pausStopaIznos!=null) {
      kumulrad.setBigDecimal("NEOP",pausStopaIznos[1]);
      kumulrad.setBigDecimal("ISKNEOP",pausStopaIznos[1]);
    } else {
      QueryDataSet qolaksice = odbici.getOlaksice(radnici.getString("CRADNIK"),raOdbici.DEF);
      BigDecimal neoporezivo;
      if (qolaksice.getRowCount() == 0) {
        neoporezivo = dm.getParametripl().getBigDecimal("MINPL");
        kumulrad.setBigDecimal("POROSN",kumulrad.getBigDecimal("POROSN").add(neoporezivo.negate()));
      } else {
        BigDecimal[] sumStopaIznos = calcOdbiciRadnik(qolaksice,"POROSN", true);
        if (sumStopaIznos[0].compareTo(nula) == 0 && isOlakIncluded(qolaksice)) {
          neoporezivo = sumStopaIznos[1];
        } else {
          neoporezivo = sumStopaIznos[1].add(dm.getParametripl().getBigDecimal("MINPL"));
          addBigDec_kumulrad("POROSN",dm.getParametripl().getBigDecimal("MINPL").negate());
        }
      }
      kumulrad.setBigDecimal("NEOP",neoporezivo);
      //prosli mjesec:
      addBigDec_kumulrad("POROSN",mjVals[1]); //porosn = neto - neop + iskneop_prosli_obracuni
      //
      if (kumulrad.getBigDecimal("POROSN").compareTo(nula) < 0) { //ako ima vise olaksica nego placu
        kumulrad.setBigDecimal("ISKNEOP",kumulrad.getBigDecimal("NEOP").add(mjVals[1]).add(kumulrad.getBigDecimal("POROSN"))); //=neto
        kumulrad.setBigDecimal("POROSN",nula);
      } else {
        kumulrad.setBigDecimal("ISKNEOP",kumulrad.getBigDecimal("NEOP").add(mjVals[1].negate()));
      }
      if (kumulrad.getBigDecimal("ISKNEOP").compareTo(nula) < 0) {
        kumulrad.setBigDecimal("ISKNEOP",ut.setScale(nula,2));
      }
      //hack za listic
      kumulrad.setBigDecimal("NEOP",dm.getParametripl().getBigDecimal("MINPL").multiply(getKoefOlak(kumulrad.getString("CRADNIK"))));
    } //paus
    addBigDec_kumulorg("NEOP",kumulrad.getBigDecimal("NEOP"));
    addBigDec_kumulorg("ISKNEOP",kumulrad.getBigDecimal("ISKNEOP"));
    addBigDec_kumulorg("POROSN",kumulrad.getBigDecimal("POROSN"));
  }
  private boolean isNeto_Bruto() {
    String nacobrb = radnici.getString("NACOBRB");
    if (nacobrb.equals("0")) {
      nacobrb = dm.getOrgpl().getString("NACOBRB");
    }
    return (nacobrb.equals("6") || nacobrb.equals("7"));
  }
  // neto2 -> neto1 -> bruto
  private void calcNetoUBruto() throws Exception {
    // if nacobrb in (6,7)
    if (!isNeto_Bruto()) return;
    /**
     * neto2 = kumulrad.BRUTO
     * neto1 = calcNBR();
     * bruto = neto1/(1-ukstdop/100)
     * kumulrad.bruto = bruto
     */
    String _cradnik = kumulrad.getString("CRADNIK");
    BigDecimal neto2 = kumulrad.getBigDecimal("BRUTO");
    BigDecimal neto1 = getBrutoIzNeta(neto2, _cradnik);
    ukstdop = null;    
    getUkStDop(_cradnik);//da napravi stopedop i maxosndop
    BigDecimal bruto = raCalcPorez.neto1ToBruto(neto1, stopedop, maxosndop);
/*
    BigDecimal stdopdiv = new BigDecimal("1.00").add(getUkStDop(_cradnik).negate()).setScale(8);
    BigDecimal bruto = neto1.setScale(8,BigDecimal.ROUND_HALF_UP).divide(stdopdiv,BigDecimal.ROUND_HALF_UP);
    //a ako je bruto veci od 31860 ?
    String maxosn1 = hr.restart.sisfun.frmParam.getParam("pl","maxosn1", "0", "Maksimalna osnovica za doprinos 1");
    if (!maxosn1.equals("0") && bruto.compareTo(new BigDecimal(maxosn1)) > 0) {
      //e onda je sranje, najpametnije je nariktati na neki nacin ...:
      try {
        String nbkmxs1 = hr.restart.sisfun.frmParam.getParam("pl","nbkmxs-".concat(_cradnik), "1", "Koef. za nto u bto kod 31860 sindroma za ".concat(_cradnik));
        bruto = bruto.multiply(new BigDecimal(nbkmxs1));
      } catch (Exception ex) {
      }
    }
*/
    kumulrad.setBigDecimal("BRUTO",bruto);
  }
  private BigDecimal getBrutoIzNeta(BigDecimal neto2,String _cradnik) {
    mjVals = getMjVals();
    return getBrutoIzNeta(neto2,_cradnik,mjVals);
  }
  private BigDecimal getBrutoIzNeta(BigDecimal neto2,String _cradnik, BigDecimal[] _mjVls) {
    Object[] vfcp = getValuesForCalcPor(_cradnik);
    BigDecimal[] stope = (BigDecimal[])vfcp[0];
    BigDecimal[] oldpor = (BigDecimal[])vfcp[1];
    BigDecimal prir = (BigDecimal)vfcp[2];
    BigDecimal[] limits = (BigDecimal[])vfcp[3];
    QueryDataSet qprirez = (QueryDataSet)vfcp[4];
    BigDecimal[] oldpor2 = new BigDecimal[7];
    for (int i=0; i<oldpor.length; i++) {
      oldpor2[i] = oldpor[i];
    }
    oldpor2[5] = ut.setScale(dm.getParametripl().getBigDecimal("MINPL"),8).multiply(getKoefOlak(_cradnik));//lodbitak /**@todo: ukljuciti premije tu ?*/
    oldpor2[6] = dm.getParametripl().getBigDecimal("MINPL");//lporminpl
    raCalcPorez calcporez = new raCalcPorez();
    calcporez.init(neto2, stope, _mjVls[0], oldpor2, prir, limits);
    calcporez.calcBack();
    return calcporez.getIzlaz();
  }

  BigDecimal[] getStopePoreza(String _cradnik) {
    BigDecimal[] ret = new BigDecimal[] {nula.setScale(0),nula.setScale(0),nula.setScale(0),nula.setScale(0),nula.setScale(0)};
    QueryDataSet porezi = odbici.getPorez(_cradnik,raOdbici.DEF);
    if (porezi.getRowCount() == 0) return ret;
    short[] cpor = getCpors(porezi);
    ret[0] = getPorez_x(porezi,cpor[0]).getBigDecimal("STOPA").divide(sto,BigDecimal.ROUND_HALF_UP);
    ret[1] = getPorez_x(porezi,cpor[1]).getBigDecimal("STOPA").divide(sto,BigDecimal.ROUND_HALF_UP);
    ret[2] = getPorez_x(porezi,cpor[2]).getBigDecimal("STOPA").divide(sto,BigDecimal.ROUND_HALF_UP);
    ret[3] = getPorez_x(porezi,cpor[3]).getBigDecimal("STOPA").divide(sto,BigDecimal.ROUND_HALF_UP);
    return ret;
  }
  private BigDecimal ukstdop = null;
  private BigDecimal[] stopedop;
  private BigDecimal[] maxosndop;
  private BigDecimal getUkStDop(String _cradnik) {
    if (ukstdop != null) return ukstdop;//ovo je ubrzanje ako i samo ako svi imaju iste doprinose (za sada imaju)
    ukstdop = nula.setScale(8);
    ArrayList arstopedop = new ArrayList();
    ArrayList armaxosndop = new ArrayList();
    QueryDataSet _doprinosi = odbici.getDoprinosiRadnik(kumulrad.getString("CRADNIK"),raOdbici.DEF);
    _doprinosi.first();
    do {
      ukstdop = ukstdop.add(_doprinosi.getBigDecimal("STOPA"));
      arstopedop.add(_doprinosi.getBigDecimal("STOPA").divide(sto,BigDecimal.ROUND_HALF_UP));
      armaxosndop.add(new BigDecimal(
          hr.restart.sisfun.frmParam.getParam("pl","maxosn"+_doprinosi.getShort("CVRODB"), "0", "Maksimalna osnovica za doprinos "+_doprinosi.getShort("CVRODB"))
        ));
    } while (_doprinosi.next());
    ukstdop = ut.setScale(ukstdop,8).divide(sto,BigDecimal.ROUND_HALF_UP);
    stopedop = (BigDecimal[])arstopedop.toArray(new BigDecimal[arstopedop.size()]);
    maxosndop = (BigDecimal[])armaxosndop.toArray(new BigDecimal[armaxosndop.size()]);
    return ukstdop;
  }

  private BigDecimal getKoefOlak(String _cradnik) {
    BigDecimal koefOlak = new BigDecimal("1.00").setScale(8);
    QueryDataSet _olaksice = odbici.getOlaksice(_cradnik,raOdbici.DEF);
    if (_olaksice.getRowCount() == 0) return koefOlak;
    do {
      QueryDataSet vrsteodb = odbici.get_vrsteodb();
      String cvrodb = Short.toString(_olaksice.getShort("CVRODB"));
      if (ld.raLocate(vrsteodb,new String[]{"CVRODB"},new String[]{cvrodb})) {
        if (vrsteodb.getString("VRSTAOSN").equals("1")) {
          koefOlak = koefOlak.add(_olaksice.getBigDecimal("STOPA"));
        }
      }
    } while (_olaksice.next());
    if (koefOlak.compareTo(new BigDecimal("1.00")) == 0 && isOlakIncluded(_olaksice)) {
      koefOlak = ut.setScale(nula,8);
    }
    return koefOlak;
  }
  private BigDecimal getPostoPrirez(String _cradnik) {
    BigDecimal pprir = nula.setScale(8);
    QueryDataSet _prirezi = odbici.getPrirez(_cradnik,raOdbici.DEF);
    if (_prirezi.getRowCount() == 0) return pprir;
    _prirezi.first();
    do {
      pprir = pprir.add(_prirezi.getBigDecimal("STOPA"));
    } while (_prirezi.next());
    return ut.setScale(pprir,8).divide(sto,BigDecimal.ROUND_HALF_UP);
  }
  private String lastSrchdMjVals_cradnik = null;
  private BigDecimal[] getMjVals() {
    String _crad = kumulrad.getString("CRADNIK");
    if (lastSrchdMjVals_cradnik!=null && mjVals != null && _crad.equals(lastSrchdMjVals_cradnik)) {
      return mjVals;
    }
    BigDecimal[] ret = new BigDecimal[] {nula,nula,nula,nula,nula,nula,nula,nula,nula};
    String qmjvls = "SELECT sum(kumulradarh.POROSN), sum(kumulradarh.ISKNEOP), "
                  +"sum(kumulradarh.por1), sum(kumulradarh.por2), sum(kumulradarh.por3), sum(kumulradarh.por4), sum(kumulradarh.por5), "
                  +"sum(kumulradarh.bruto),"
                  +"sum(kumulradarh.neto2)"
                  +" FROM kumulradarh,kumulorgarh"
                  +" WHERE kumulradarh.cradnik = '"+_crad
                  +"' AND kumulorgarh.godobr = kumulradarh.godobr"
                  +" AND kumulorgarh.mjobr = kumulradarh.mjobr"
                  +" AND kumulorgarh.rbrobr = kumulradarh.rbrobr"
                  +" AND kumulradarh.cvro = kumulorgarh.cvro"
                  +" AND kumulradarh.corg = kumulorgarh.corg"
                  +" AND "+Condition.between("kumulorgarh.datumispl", 
                        Util.getUtil().getFirstDayOfMonth(datumispl), 
                        Util.getUtil().getLastDayOfMonth(datumispl));
//                  +" AND kumulorgarh.datumispl between '"
//                  +new java.sql.Date(Util.getUtil().getFirstDayOfMonth(datumispl).getTime()).toString()
//                  +"' AND '"
//                  +new java.sql.Date(Util.getUtil().getLastDayOfMonth(datumispl).getTime()).toString()+"'";
    QueryDataSet oldmjsums = Util.getNewQueryDataSet(qmjvls);
    for (int i = 0; i < 9; i++) {
      ret[i] = ut.setScale(oldmjsums.getBigDecimal(i),2);
    }
    return ret;
  }
  /*
   * prije poziva ove metode za svaki slucaj mjVals = getMjVals()
   */
  private Object[] getValuesForCalcPor(String _crad) {
    //stope
    BigDecimal[] stope = getStopePoreza(_crad);
    //stari porez
    BigDecimal[] oldpor = new BigDecimal[5];
    oldpor[0] = mjVals[2];
    oldpor[1] = mjVals[3];
    oldpor[2] = mjVals[4];
    oldpor[3] = mjVals[5];
    oldpor[4] = mjVals[6];
    //prirez
    QueryDataSet qprirez = null;
    BigDecimal prir = null;
    if (obrprireza) {
      qprirez = odbici.getPrirez(_crad,raOdbici.DEF);
      if (qprirez.getRowCount() != 0) {
        qprirez.first();
        prir = qprirez.getBigDecimal("STOPA").divide(sto,4,BigDecimal.ROUND_HALF_UP);
      }
    }
    //limiti
    BigDecimal[] limits = null;
    if (uselimitsPOR) {
      limits = new BigDecimal[3];
      limits[0] = dm.getParametripl().getBigDecimal("OSNPOR1");
      limits[1] = dm.getParametripl().getBigDecimal("OSNPOR2");
      limits[2] = dm.getParametripl().getBigDecimal("OSNPOR3");
    }
/////////////////////
//    System.out.println("getValuesForCalcPor");
//    System.out.println("stope:");
//    ST.prn(stope);
//    System.out.println("oldpor:");
//    ST.prn(oldpor);
//    System.out.println("limits:");
//    ST.prn(limits);
//    System.out.println("prir: "+prir);
//    System.out.println("qprirez");
//    ST.prn(qprirez);
/////////////////////
    return new Object[] {stope,oldpor,prir,limits,qprirez};
  }
  
  private void calcPorez() throws Exception {
    mjVals = getMjVals();//mjVals[0] = porosn; mjVals[1] = iskneop
    //olaksice
    calcOlaksice();
    //porez
    if (kumulrad.getBigDecimal("POROSN").compareTo(nula) <= 0) return;
    Object[] vfcp = getValuesForCalcPor(kumulrad.getString("CRADNIK"));
    BigDecimal[] stope = (BigDecimal[])vfcp[0];
    BigDecimal[] oldpor = (BigDecimal[])vfcp[1];
    BigDecimal prir = (BigDecimal)vfcp[2];
    BigDecimal[] limits = (BigDecimal[])vfcp[3];
    QueryDataSet qprirez = (QueryDataSet)vfcp[4];

 /*
    //stope
    BigDecimal[] stope = getStopePoreza(kumulrad.getString("CRADNIK"));
    //stari porez
    BigDecimal[] oldpor = new BigDecimal[5];
    oldpor[0] = mjVals[2];
    oldpor[1] = mjVals[3];
    oldpor[2] = mjVals[4];
    oldpor[3] = mjVals[5];
    oldpor[4] = mjVals[6];
    //prirez
    BigDecimal prir = null;
    QueryDataSet qprirez = null;
    if (obrprireza) {
      qprirez = odbici.getPrirez(radnici.getString("CRADNIK"),odbici.DEF);
      if (qprirez.getRowCount() != 0) {
        qprirez.first();
        prir = qprirez.getBigDecimal("STOPA").divide(sto,2,BigDecimal.ROUND_HALF_UP);
      }
    }
    //limiti
    BigDecimal[] limits = null;
    if (uselimitsPOR) {
      limits = new BigDecimal[3];
      limits[0] = dm.getParametripl().getBigDecimal("OSNPOR1");
      limits[1] = dm.getParametripl().getBigDecimal("OSNPOR2");
      limits[2] = dm.getParametripl().getBigDecimal("OSNPOR3");
    }
 */
    //obracun
    raCalcPorez calcporez = new raCalcPorez();
    calcporez.init(kumulrad.getBigDecimal("POROSN"),stope,mjVals[0],oldpor,prir,limits);
    calcporez.calc();
    // kumulrad
    kumulrad.setBigDecimal("POR1",calcporez.getRes_por()[0].setScale(2, BigDecimal.ROUND_HALF_UP));
    kumulrad.setBigDecimal("POR2",calcporez.getRes_por()[1].setScale(2, BigDecimal.ROUND_HALF_UP));
    kumulrad.setBigDecimal("POR3",calcporez.getRes_por()[2].setScale(2, BigDecimal.ROUND_HALF_UP));
    kumulrad.setBigDecimal("POR4",calcporez.getRes_por()[3].setScale(2, BigDecimal.ROUND_HALF_UP));
    kumulrad.setBigDecimal("POR5",calcporez.getRes_por()[4].setScale(2, BigDecimal.ROUND_HALF_UP));
    kumulrad.setBigDecimal("PORUK",Aus.zero2); // za svaki slucaj
//    kumulrad.setBigDecimal("PORUK",
//                           calcporez.getRes_por()[0]
//                           .add(calcporez.getRes_por()[1])
//                           .add(calcporez.getRes_por()[2])
//                           .add(calcporez.getRes_por()[3])
//                           .add(calcporez.getRes_por()[4]));
    Aus.addTo(kumulrad, "PORUK", new String[] {"POR1","POR2","POR3","POR4","POR5"});
    //dodaj odbiciobr za porez
    QueryDataSet _qodbici = odbici.getPorez(kumulrad.getString("CRADNIK"),raOdbici.DEF);
    for (int i = 0; i < 5; i++) {
      if (calcporez.getRes_por()[i].compareTo(nula) > 0) {
        ld.raLocate(_qodbici,"RBRODB",Integer.toString(i+1));
        addOdbitakPorez(_qodbici,calcporez.getRes_por()[i], calcporez.getRes_osn()[i]);
      }
    }
    // kumulorg
    addBigDec_kumulorg("POR1",kumulrad.getBigDecimal("POR1"));
    addBigDec_kumulorg("POR2",kumulrad.getBigDecimal("POR2"));
    addBigDec_kumulorg("POR3",kumulrad.getBigDecimal("POR3"));
    addBigDec_kumulorg("POR4",kumulrad.getBigDecimal("POR4"));
    addBigDec_kumulorg("POR5",kumulrad.getBigDecimal("POR5"));
    addBigDec_kumulorg("PORUK",kumulrad.getBigDecimal("PORUK"));
    if (kumulrad.getBigDecimal("PORUK").compareTo(nula) <= 0) return;
    //prirez
    if (obrprireza) addOdbitakPorez(qprirez,calcporez.getRes_prir(),null,true);
    //kumulrad
    kumulrad.setBigDecimal("PRIR",calcporez.getRes_prir().setScale(2, BigDecimal.ROUND_HALF_UP));
    kumulrad.setBigDecimal("PORIPRIR",kumulrad.getBigDecimal("PORUK")
                           .add(calcporez.getRes_prir().setScale(2, BigDecimal.ROUND_HALF_UP)));
    //kumulorg
    addBigDec_kumulorg("PRIR",kumulrad.getBigDecimal("PRIR"));
    addBigDec_kumulorg("PORIPRIR",kumulrad.getBigDecimal("PORIPRIR"));
  }
  
  private void addOdbitakPorez(QueryDataSet _qodbici,BigDecimal _porez, BigDecimal _osnovica) {
    addOdbitakPorez(_qodbici,_porez,_osnovica,false);
  }
  private void addOdbitakPorez(QueryDataSet _qodbici,BigDecimal _porez,BigDecimal _osnovica, boolean prirez) {
    if (_porez.compareTo(nula) <= 0) return;
    if (_qodbici.getRowCount() == 0) return;
    odbiciobr.insertRow(false);
    setValues(_qodbici,odbiciobr);
    odbiciobr.setString("CRADNIK",kumulrad.getString("CRADNIK"));
    odbiciobr.setShort("CVRP",Short.parseShort("0"));
    odbiciobr.setShort("RBR",Short.parseShort("0"));
    odbiciobr.setBigDecimal("OBRIZNOS",ut.setScale(_porez,2));
    odbiciobr.setBigDecimal("OBRSTOPA",ut.setScale(_qodbici.getBigDecimal("STOPA"),2));
    if (prirez) {
      odbiciobr.setBigDecimal("OBROSN",ut.setScale(kumulrad.getBigDecimal("PORUK"),2));
    } else {
      odbiciobr.setBigDecimal("OBROSN",ut.setScale(_osnovica,2));
    }
    odbiciobr.post();
  }

  private void calcPrirez() throws Exception {
    QueryDataSet qprirez = odbici.getPrirez(radnici.getString("CRADNIK"),raOdbici.DEF);
    BigDecimal[] stopaIznos = calcOdbiciRadnik(qprirez,null,false);
    //kumulrad
    kumulrad.setBigDecimal("PRIR",stopaIznos[1]);
    kumulrad.setBigDecimal("PORIPRIR",kumulrad.getBigDecimal("PORUK").add(stopaIznos[1].setScale(2,BigDecimal.ROUND_HALF_UP)));
    //kumulorg
    addBigDec_kumulorg("PRIR",stopaIznos[1]);
    addBigDec_kumulorg("PORIPRIR",kumulrad.getBigDecimal("PORIPRIR"));
  }

  private void calcDoNetaPK() throws Exception {
    //kumulrad
    //neto2 = neto - poriprir
    kumulrad.setBigDecimal("NETO2",kumulrad.getBigDecimal("NETO").setScale(2, BigDecimal.ROUND_HALF_UP).add(kumulrad.getBigDecimal("PORIPRIR").setScale(2, BigDecimal.ROUND_HALF_UP).negate()));
    //netopk = neto2 + naknade
    kumulrad.setBigDecimal("NETOPK",kumulrad.getBigDecimal("NETO2").add(kumulrad.getBigDecimal("NAKNADE").setScale(2, BigDecimal.ROUND_HALF_UP)));
    addBigDec_kumulorg("NETO2",kumulrad.getBigDecimal("NETO2"));
    addBigDec_kumulorg("NAKNADE",kumulrad.getBigDecimal("NAKNADE"));
    addBigDec_kumulorg("NETOPK",kumulrad.getBigDecimal("NETOPK"));
  }

  private void checkRSOO() {
    if (primanja.getRowCount() == 1) return;
    primanja.first();
    java.util.HashSet rsOOs = new java.util.HashSet();
    do {
      String _OO = posVrsteprim(primanja).getString("RSOO");
      if (!_OO.equals("")) {
        rsOOs.add(_OO);
      }
    } while (primanja.next());
    if (rsOOs.size()>1) { // ima vise razlicitih RSOO po zaradama
//error handling
    }
  }
  /**
   * provjerava rspedriod i puni rsperiodobr za tekuci kumulrad, radnici (radnicipl) i zagrabljena primanja
   */
  private void makeRS() {
    String rsOO_rad = radnici.getString("RSOO");
    String rsOO_def = hr.restart.sisfun.frmParam.getParam("pl","def_rsOO");
    if (rsOO_def == null) rsOO_def = "10";
    if (rsOO_def.equals("")) rsOO_def = "10";
    if (rsOO_rad.equals("")) rsOO_rad = rsOO_def;
    //najjaci je rsOO_prim
    primanja.first();
    String rsOO_prim = posVrsteprim(primanja).getString("RSOO");
    if (rsOO_prim.equals("")) rsOO_prim = rsOO_rad;
    //
    QueryDataSet rsperiod = Util.getNewQueryDataSet("SELECT * FROM rsperiod WHERE rsperiod.cradnik = '"+radnici.getString("CRADNIK")+"' ORDER BY rsperiod.ODDANA");
    QueryDataSet rsperiodobr = Util.getNewQueryDataSet("SELECT * FROM rsperiodobr WHERE rsperiodobr.cradnik = '"+radnici.getString("CRADNIK")+"'");
    
    if (rsperiod.getRowCount() == 0) {
//      checkRSOO();
      rsperiodobr.insertRow(false);
      fillDefRSPeriodObr(rsperiodobr,rsOO_prim,true);
      rsperiodobr.post();
    } else {
      BigDecimal _propOSNMIORS = getPropOSNMIORS();
      int rbr = 0;
      java.util.HashSet usedRSOO = new java.util.HashSet();
      rsperiod.first();
      do {
        String p_rsOO = rsperiod.getString("RSOO");
        rbr = rbr + 1;
        rsperiodobr.insertRow(false);
        boolean _lN = p_rsOO.equals(rsOO_def) && !numsLoaded;
//System.out.println("rsOO_def = "+rsOO_def+", p_rsOO = "+p_rsOO+", _lN = "+_lN+", numsLoaded = "+numsLoaded+", p_rsOO.equals(rsOO_def) = "+p_rsOO.equals(rsOO_def));
        fillDefRSPeriodObr(rsperiodobr,rsOO_rad,_lN);
        if (!(rsperiod.getString("COPCINE").equals("") || rsperiod.isNull("COPCINE"))) {
//          System.out.println("****** Mijenjam opcinu iz "+rsperiodobr.getString("COPCINE")+" u "+rsperiod.getString("COPCINE"));
          rsperiodobr.setString("COPCINE", rsperiod.getString("COPCINE"));
        } else {
//          System.out.println("****** NE Mijenjam opcinu iz "+rsperiodobr.getString("COPCINE")+" u "+rsperiod.getString("COPCINE"));
        }
        rsperiodobr.setShort("ODDANA",rsperiod.getShort("ODDANA"));
        rsperiodobr.setShort("DODANA",rsperiod.getShort("DODANA"));
        rsperiodobr.setString("RSOO",p_rsOO.equals(rsOO_def)?rsOO_rad:rsperiod.getString("RSOO"));
        BigDecimal[] _sums = (BigDecimal[])rsSums.get(p_rsOO);
        if (_sums!=null && !usedRSOO.contains(p_rsOO)) {
          rsperiodobr.setBigDecimal("SATI",_sums[0]);
          rsperiodobr.setBigDecimal("BRUTO",_sums[1].multiply(_propOSNMIORS));//ai05: OSNOVICA ZA MIO
          rsperiodobr.setBigDecimal("BRUTOMJ",_sums[1]);
          rsperiodobr.setBigDecimal("NETOPK",_sums[2]);
        } /*else {
          rsperiodobr.setBigDecimal("SATI",nula);
          rsperiodobr.setBigDecimal("BRUTO",nula);
          rsperiodobr.setBigDecimal("NETOPK",nula);
        }*/
        rsperiodobr.setInt("RBR",rbr);
        rsperiodobr.post();
        usedRSOO.add(p_rsOO);
      } while (rsperiod.next());
/** @todo  */
    }
    //ocisti smece
    rsSums.clear();
    numsLoaded = false;
    //snimi
    raTransaction.saveChanges(rsperiodobr);
  }
  /**
   * omjer osnovice za mio i ukupnog bruta (za punjenje rsperiodobr)
   * vraca 1.00000000 za sve koji imaju bruto manji od maksimalne osnovice za MIO
   * @return omjer osnovice za mio i ukupnog bruta
   */
  private BigDecimal getPropOSNMIORS() {
    //DataSet _doprrad = odbici.getDoprinosiRadnik(radnici.getString("CRADNIK"),raOdbici.OBR);
    BigDecimal __osnmio = getSumOsnovDoprinos(radnici.getString("CRADNIK"),raIzvjestaji.RS_IV_8);
    BigDecimal __bto = kumulrad.getBigDecimal("BRUTO");
    if (__bto.signum() == 0) return new BigDecimal("1.00000000");
//    System.out.println("cradnik = "+radnici.getString("CRADNIK"));
//    System.out.println("__osnmio = "+__osnmio);
//    System.out.println("__bto = "+__bto);
//    System.out.println("__osnmio.divide(__bto ... "+__osnmio.divide(__bto,8,BigDecimal.ROUND_HALF_UP));
    return __osnmio.divide(__bto,8,BigDecimal.ROUND_HALF_UP);
  }
  public static boolean isOIB() {
    return !frmParam.getParam("pl", "forceJMBG", "N", "Forsirati punjenje periodaRS JMBG-om").equals("D");
  }
  private boolean numsLoaded = false;
  private void fillDefRSPeriodObr(QueryDataSet rsp, String rsoo, boolean fillNumbers) {
    String _cradnik = kumulrad.getString("CRADNIK");
    rsp.setString("CRADNIK",_cradnik);
    rsp.setInt("RBR",1);
    rsp.setString("RSOO",rsoo);
    rsp.setString("JMBG",radnici.getString(isOIB()?"OIB":"JMBG"));
    rsp.setString("COPCINE",dm.getOrgpl().getString("COPCINE"));//RSm 2005+
    String rsinv = radnici.getString("RSINV");
    rsp.setString("RSINV",rsinv.equals("")?"0":rsinv);
    String rsb = radnici.getString("RSB");
    rsp.setString("RSB",rsb.equals("")?"0":rsb);
    String rsz = radnici.getString("rsz");
    rsp.setString("RSZ",rsz.equals("")?dm.getOrgpl().getString("RSZ"):rsz);
    rsp.setShort("ODDANA",(short)1);
    rsp.setShort("DODANA",frmRSPeriod.getZadnjiDan(godina,mjesec));
    if (fillNumbers) {
      numsLoaded = true;
      //sati
      rsp.setBigDecimal("SATI",kumulrad.getBigDecimal("SATI"));
      //nije bruto nego osnovica za mio
      BigDecimal pravi_bruto = kumulrad.getBigDecimal("BRUTO");
      BigDecimal bruto = getSumOsnovDoprinos(_cradnik,raIzvjestaji.RS_IV_8);
/*      if (pravi_bruto.compareTo(bruto) != 0) {
        mjVals = getMjVals();
        if (mjVals[7].compareTo(nula) == 0) {
          bruto = pravi_bruto;
        }
      }*/
      rsp.setBigDecimal("BRUTO",bruto);
      rsp.setBigDecimal("BRUTOMJ",pravi_bruto);
      //mio1
      BigDecimal mio1=getSumDoprinos(_cradnik,raIzvjestaji.RS_IV_8);
      rsp.setBigDecimal("MIO1",mio1);
      rsp.setBigDecimal("MIO1MJ",mio1);
      //mio2
      BigDecimal mio2=getSumDoprinos(_cradnik,raIzvjestaji.RS_IV_9);
      rsp.setBigDecimal("MIO2",mio2);
      rsp.setBigDecimal("MIO2MJ",mio2);
      //zo
      BigDecimal zo = getSumDoprinos(_cradnik,raIzvjestaji.RS_III_5);
      rsp.setBigDecimal("ZO",zo);
      rsp.setBigDecimal("ZOMJ",godina>=2003?getSumOsnovDoprinos(_cradnik,raIzvjestaji.RS_III_5):zo);
      //zapos
      BigDecimal zapos = getSumDoprinos(_cradnik,raIzvjestaji.RS_III_6);
      rsp.setBigDecimal("ZAPOS",zapos);
      rsp.setBigDecimal("ZAPOSMJ",godina>=2003?getSumOsnovDoprinos(_cradnik,raIzvjestaji.RS_III_6):zapos);
      //premije
      BigDecimal premos = getSumDoprinos(_cradnik,raIzvjestaji.RS_IV_12);

      /* staro trkeljanje :
      String prefix = " AND (odbiciobr.";
      String q3_1 = prefix+raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.ID_3_1);
      q3_1 = q3_1.equals(prefix)?"":q3_1;
      if (!q3_1.equals("")) prefix = " OR odbiciobr.";
      String q3_2 = prefix+raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.ID_3_2);
      q3_2 = q3_2.equals(prefix)?"":q3_2;
      if (!q3_1.concat(q3_2).equals("")) prefix = " OR odbiciobr.";
      String q3_3 = prefix+raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.ID_3_3);
      q3_3 = q3_3.equals(prefix)?"":q3_3;
      String qz = ")";
      BigDecimal premos = nula;
      if (!q3_1.concat(q3_2.concat(q3_3)).trim().equals("")) {
        String q = "SELECT sum(odbiciobr.obriznos) from odbiciobr where odbiciobr.cradnik = "+_cradnik
                 + q3_1 + q3_2 + q3_3 + qz;
//        System.out.println("sprem = "+q);
        premos = Util.getNewQueryDataSet(q).getBigDecimal(0);
      }
      */
      //osobni odbitak
      BigDecimal iskneop = kumulrad.getBigDecimal("ISKNEOP");//.add(premos.negate());
      if (iskneop.compareTo(nula) < 0) {
        iskneop = ut.setScale(nula,2);
//        premos = ut.setScale(nula,2);
      }
      rsp.setBigDecimal("OSODB",iskneop);
      rsp.setBigDecimal("PREMOS",premos);
      //porez
      BigDecimal porez = kumulrad.getBigDecimal("PORUK");
      rsp.setBigDecimal("POREZ",porez);
      rsp.setBigDecimal("POREZMJ",porez);
      //prirez
      BigDecimal prirez = kumulrad.getBigDecimal("PRIR");
      rsp.setBigDecimal("PRIREZ",prirez);
      rsp.setBigDecimal("PRIREZMJ",prirez);
      //netopk
      rsp.setBigDecimal("NETOPK",kumulrad.getBigDecimal("NETOPK").add(getSumPrimNeto(_cradnik, raIzvjestaji.RS_NE_99).negate()).add(getSumDoprinos(_cradnik, raIzvjestaji.RS_NE_99).negate()));
    } else {
      rsp.setBigDecimal("SATI",nula);
      rsp.setBigDecimal("BRUTO",nula);
      rsp.setBigDecimal("BRUTOMJ",nula);
      rsp.setBigDecimal("MIO1",nula);
      rsp.setBigDecimal("MIO1MJ",nula);
      rsp.setBigDecimal("MIO2",nula);
      rsp.setBigDecimal("MIO2MJ",nula);
      rsp.setBigDecimal("ZO",nula);
      rsp.setBigDecimal("ZOMJ",nula);
      rsp.setBigDecimal("ZAPOS",nula);
      rsp.setBigDecimal("ZAPOSMJ",nula);
      rsp.setBigDecimal("PREMOS",nula);
      rsp.setBigDecimal("OSODB",nula);
      rsp.setBigDecimal("POREZ",nula);
      rsp.setBigDecimal("POREZMJ",nula);
      rsp.setBigDecimal("PRIREZ",nula);
      rsp.setBigDecimal("PRIREZMJ",nula);
      rsp.setBigDecimal("NETOPK",nula);
    }
    //mjesec i godina
    rsp.setShort("MJESEC",dm.getOrgpl().getShort("MJOBR"));
    rsp.setShort("GODINA",dm.getOrgpl().getShort("GODOBR"));
  }
  
  private BigDecimal getSumPrimNeto(String _cradnik, short[] raIzvjFlag) {
    String raIzvjQuery = raIzvjestaji.getPrimanjaWhQueryIzv(raIzvjFlag);
    if (raIzvjQuery.equals("")) return nula;
    String q = "SELECT cradnik, cvrp, rbr, neto from primanjaobr where primanjaobr.cradnik = "+_cradnik
               +" AND primanjaobr."+raIzvjQuery;
    QueryDataSet _qds = Util.getNewQueryDataSet(q);
    if (_qds.getRowCount() == 0) return nula;
    BigDecimal sumNet = nula.setScale(2,BigDecimal.ROUND_HALF_UP);
    _qds.first();
    do {
      sumNet = sumNet.add(_qds.getBigDecimal("NETO").setScale(2,BigDecimal.ROUND_HALF_UP));
    } while (_qds.next());
    return sumNet;
   
  }
  
  private BigDecimal getSumDoprinos(String _cradnik,short[] raIzvjFlag) {
    return getSumDoprinos(_cradnik,raIzvjFlag,false);
  }

  private BigDecimal getSumOsnovDoprinos(String _cradnik,short[] raIzvjFlag) {
    return getSumDoprinos(_cradnik,raIzvjFlag,true);
  }

  //cachiranje getSumDoprinos
  private QueryDataSet sumDoprinosSet;
  private short[] sumDoprinosSet_oldRaIzvjFlag = null;
  private String sumDoprinosSet_old_cradnik = null;
  private boolean newCacheSumDoprinosSet(String _cradnik,short[] raIzvjFlag) {
    if (sumDoprinosSet != null
        && sumDoprinosSet_old_cradnik != null
        && sumDoprinosSet_oldRaIzvjFlag != null
        && sumDoprinosSet_old_cradnik.equals(_cradnik)
        && java.util.Arrays.equals(sumDoprinosSet_oldRaIzvjFlag,raIzvjFlag)) {
//System.out.println("newCacheSumDoprinosSet("+_cradnik+","+VarStr.join(raIzvjFlag,';')+") = false");
      return false;
    }
//System.out.println("newCacheSumDoprinosSet("+_cradnik+","+VarStr.join(raIzvjFlag,';')+") = true");
    sumDoprinosSet_old_cradnik = new String(_cradnik);
    sumDoprinosSet_oldRaIzvjFlag = (short[])raIzvjFlag.clone();
//System.out.println(VarStr.join(sumDoprinosSet_oldRaIzvjFlag,','));
    return true;
  }
  //
  private BigDecimal getSumDoprinos(String _cradnik,short[] raIzvjFlag, boolean osnov) {
    if (newCacheSumDoprinosSet(_cradnik,raIzvjFlag)) {
      String raIzvjQuery = raIzvjestaji.getOdbiciWhQueryIzv(raIzvjFlag);
      if (raIzvjQuery.equals("")) return nula;
      String q = "SELECT odbiciobr.obriznos,odbiciobr.obrosn from odbiciobr where odbiciobr.cradnik = '"+_cradnik
               +"' AND odbiciobr."+raIzvjQuery;
//System.out.println("getSumDoprinos :: "+q);
      sumDoprinosSet = Util.getNewQueryDataSet(q);
    }
    if (sumDoprinosSet.getRowCount() == 0) return nula;
    if (osnov) {
      return sumDoprinosSet.getBigDecimal("OBROSN");
    }
    BigDecimal sumDopr = nula.setScale(2,BigDecimal.ROUND_HALF_UP);
    sumDoprinosSet.first();
    do {
      sumDopr = sumDopr.add(sumDoprinosSet.getBigDecimal("OBRIZNOS").setScale(2,BigDecimal.ROUND_HALF_UP));
    } while (sumDoprinosSet.next());
    return sumDopr;
  }

  private java.util.HashMap rsSums = new java.util.HashMap();
  
  /**
   * ubacuje u HashMap suma za rs primanja po rsOO. Poziva se loopPrimanja(3)
   */
  private void addRSsum() {
    BigDecimal[] rsSumVals; //{SATI,BRUTO.NETO}
    QueryDataSet _vrstepr = posVrsteprim(primanja);
    String rsOO_prim = _vrstepr.getString("RSOO");
    if (rsSums.containsKey(rsOO_prim)) {
      rsSumVals = (BigDecimal[])rsSums.get(rsOO_prim);
    } else {
      rsSumVals = new BigDecimal[] {nula,nula,nula};
    }
    rsSumVals[0] = rsSumVals[0].add(primanja.getBigDecimal("SATI"));
    if (raParam.getParam(_vrstepr,1).equals("D")) rsSumVals[1] = rsSumVals[1].add(primanja.getBigDecimal("BRUTO"));
    rsSumVals[2] = rsSumVals[2].add(primanja.getBigDecimal("NETO"));
    rsSums.put(rsOO_prim,rsSumVals);
  }
  private void addNetoOdb() {
    dm.getIsplMJ().open();
    if (!ld.raLocate(dm.getIsplMJ(),"CISPLMJ",radnici.getShort("CISPLMJ")+"")) return;
    dm.getBankepl().open();
    if (!ld.raLocate(dm.getBankepl(),"CBANKE",dm.getIsplMJ().getInt("CBANKE")+"")) return;
    dm.getVrsteodb().open();
    if (!ld.raLocate(dm.getVrsteodb(),"CPOV",dm.getBankepl().getInt("CPOV")+"")) return;
    short cvrodb = dm.getVrsteodb().getShort("CVRODB");
    odbiciobr.insertRow(false);
    odbiciobr.setString("CRADNIK",kumulrad.getString("CRADNIK"));
    odbiciobr.setShort("CVRODB",cvrodb);
    odbiciobr.setShort("RBRODB",Short.parseShort("0"));
    odbiciobr.setString("CKEY",kumulrad.getString("CRADNIK"));
    odbiciobr.setString("CKEY2","$SYS");
    odbiciobr.setShort("CVRP",Short.parseShort("0"));
    odbiciobr.setShort("RBR",Short.parseShort("0"));
    odbiciobr.setBigDecimal("OBRIZNOS",kumulrad.getBigDecimal("NARUKE"));
    odbiciobr.setBigDecimal("OBRSTOPA",nula);
    odbiciobr.setBigDecimal("OBROSN",nula);
    odbiciobr.post();
  }
  private void calcRazPor() {
    if (calcrazpor) {
	    calcRazPorOrPrir(false);
	    calcRazPorOrPrir(true);
    }
  }
  /**
   * Uzima u obzir RA prir?R:P 2 0 odbitke i DODAJE IH prir?PRIREZU:POREZU, tz. ako je u korist posloprimca treba biti u minus
   * @param prir jel prirez il porez
   */
  private void calcRazPorOrPrir(boolean prir) {
    String cradnik = kumulrad.getString("CRADNIK");
    QueryDataSet qrazlpor = odbici.getOdbici("RA",prir?"R":"P","2","0",cradnik,null,"odbici",cradnik,raOdbici.DEF);
    BigDecimal razpor;
    try {
      razpor = calcOdbiciRadnik(qrazlpor,"NETO",false)[1];
    } catch (Exception e) {
      e.printStackTrace();
      razpor = nula;
    }
    addBigDec_kumulrad(prir?"PRIR":"PORUK",ut.setScale(razpor,2));
    addBigDec_kumulrad("PORIPRIR",ut.setScale(razpor,2));
    addBigDec_kumulorg(prir?"PRIR":"PORUK",ut.setScale(razpor,2));
    addBigDec_kumulorg("PORIPRIR",ut.setScale(razpor,2));
  }
  private void calcHarach() {
    if (frmParam.getParam("pl", "obrkrizb","D","Obracunati krizni porez (D/N)").equalsIgnoreCase("N")) return;
    
    String harachCVRODB = Harach.getHarachParam("CVRODB");
    String harachCPOV = Harach.getHarachParam("CPOV");
    String harach1limit = Harach.getHarachParam("limit1");
    String harach2limit = Harach.getHarachParam("limit2");
    String harach1stopa = Harach.getHarachParam("stopa1");
    String harach2stopa = Harach.getHarachParam("stopa2");
    if (Harach.createHarachOsnPod(harachCVRODB,harachCPOV)) odbici.null_vrsteodb();;
    BigDecimal harachosnovica = getMjVals()[8].add(osnovicaZaHarach);
    QueryDataSet harachset = Odbici.getDataModule().getTempSet(
        Condition.equal("CVRODB", Short.parseShort(harachCVRODB)).and(Condition.equal("CKEY",radnici.getString("CRADNIK"))));
    harachset.open();
    if (harachset.getRowCount() == 0 && (harachosnovica.compareTo(new BigDecimal(harach1limit)) > 0)) {//automatiche
      
      ReadRow _odbiciobr = Harach.addHaracOdbitak(kumulrad.getString("CRADNIK"), harachosnovica, harachset);
      odbiciobr.insertRow(false);
      setValues(_odbiciobr, odbiciobr);
      odbiciobr.post();
      kumulrad.setBigDecimal("KREDITI",kumulrad.getBigDecimal("KREDITI").add(odbiciobr.getBigDecimal("OBRIZNOS")));
      kumulrad.setBigDecimal("NARUKE",kumulrad.getBigDecimal("NARUKE").add(odbiciobr.getBigDecimal("OBRIZNOS").negate()));
    } else if (harachset.getRowCount() > 0 && !obrkredita && harachosnovica.signum() > 0) {//obracun fixnog haracha ali ne i kredita, 
      try {
        BigDecimal[] stopaIznos = calcOdbiciRadnik(harachset,"NARUKE",true);
        kumulrad.setBigDecimal("KREDITI",kumulrad.getBigDecimal("KREDITI").add(stopaIznos[1]));
//        kumulrad.setBigDecimal("NARUKE",kumulrad.getBigDecimal("NARUKE").add(stopaIznos[1].negate()));
        addBigDec_kumulorg("KREDITI",kumulrad.getBigDecimal("KREDITI"));
        addBigDec_kumulorg("NARUKE",kumulrad.getBigDecimal("NARUKE"));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
  }

  

  private void calcKrediti() throws Exception {
    if (osnovicaZaKredit.compareTo(nula) > 0) {
      QueryDataSet krediti = odbici.getKrediti(radnici.getString("CRADNIK"),raOdbici.DEF);
      BigDecimal[] stopaIznos = calcOdbiciRadnik(krediti,"NARUKE",true);
      kumulrad.setBigDecimal("KREDITI",kumulrad.getBigDecimal("KREDITI").add(stopaIznos[1]));
    } else {
      kumulrad.setBigDecimal("KREDITI",nula);
    }
    addBigDec_kumulorg("KREDITI",kumulrad.getBigDecimal("KREDITI"));
    addBigDec_kumulorg("NARUKE",kumulrad.getBigDecimal("NARUKE"));
  }
  private short[] getCpors(QueryDataSet porezi) {
    short[] ret = new short[] {999,999,999,999,999};
    porezi.setSort(new SortDescriptor(new String[] {"RBRODB"}));
    porezi.first();
    int i = 0;
    do {
      ret[i] = porezi.getShort("RBRODB");
      i++;
    } while (porezi.next());
    return ret;
  }

  private StorageDataSet getPorez_x(QueryDataSet porezi,short key) {
    if (ld.raLocate(porezi,new String[] {"RBRODB"},new String[] {Short.toString(key)})) {
      return porezi;
    } else {
      StorageDataSet emptyporezi = porezi.cloneDataSetStructure();
      emptyporezi.open();
      emptyporezi.insertRow(true);
      emptyporezi.post();
      emptyporezi.first();
      return emptyporezi;
    }
  }

  /**
   * uvecava iznos bigdecimalne kolone kumulrad za iznos bigdecimala
   * @param colkumul naziv kolone u kumulrad
   * @param bd bigdecimal za uvecati
   */
  private void addBigDec_kumulrad(String colkumul,BigDecimal bd) {
    int sc = kumulorg.getColumn(colkumul).getScale();
    kumulrad.setBigDecimal(colkumul,kumulrad.getBigDecimal(colkumul).add(Util.getUtil().setScale(bd,sc)));
/*    kumulrad.setBigDecimal(colkumul,
                           kumulrad.getBigDecimal(colkumul).setScale(2,BigDecimal.ROUND_HALF_UP)
                           .add(bd.setScale(2,BigDecimal.ROUND_HALF_UP)));*/
  }
  private void addBigDec_kumulorg(String colkumul,BigDecimal bd) {
    int sc = kumulorg.getColumn(colkumul).getScale();
    kumulorg.setBigDecimal(colkumul,kumulorg.getBigDecimal(colkumul).add(Util.getUtil().setScale(bd,sc)));
/*    kumulorg.setBigDecimal(colkumul,
                           kumulorg.getBigDecimal(colkumul).setScale(2,BigDecimal.ROUND_HALF_UP)
                           .add(bd.setScale(2,BigDecimal.ROUND_HALF_UP)));*/
  }

  public void initOrgs() {
    com.borland.dx.dataset.StorageDataSet orgset = hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(corg);
    orgs = new String[orgset.getRowCount()];
    int i = 0;
    orgset.first();
    do {
      orgs[i] = orgset.getString("CORG");
      i++;
    } while (orgset.next());
  }

//messaging
  private LinkedList propertyChangeListeners = new LinkedList();
  public void addPropertyChangeListener(PropertyChangeListener l) {
    propertyChangeListeners.add(l);
  }
  public void firePropertyChange(String prop, Object oldVal, Object newVal) {
    for (Iterator iterator = propertyChangeListeners.iterator(); iterator.hasNext();) {
      PropertyChangeListener lis = (PropertyChangeListener) iterator.next();
      lis.propertyChange(new PropertyChangeEvent(this, prop, oldVal, newVal));
    }
  }
  


//test
public static void main(String[] args) {
  System.out.println("r.. "+raPlObrRange.getInQueryIsp(2002,1,2002,12,"kumulorgarh"));
  short[] s = raPlObrRange.getMinAndMaxObrada();
  for (int i = 0; i < s.length; i++) {
    System.out.println("s["+i+"] = "+s[i]);
  }

}

//  System.out.println("SELECT * FROM kumulradarh where "+raPlObrRange.getInQueryIsp(2002,7,"kumulradarh"));
//  System.out.println("r1 = "+raPlObrRange.getInQueryIsp(2002,7));
//  System.out.println("SELECT * FROM kumulradarh where "+new raPlObrRange((short)2002,(short)3,(short)1,(short)2002,(short)10,(short)99).getQuery("KUMULRADarh"));
//  System.out.println("r2a = "+new raPlObrRange((short)2002,(short)3,(short)1,(short)2002,(short)10,(short)99).getQuery());
//  System.out.println("x="+raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.ID_3_1));
//  System.out.println("x="+raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.ID_3_2));
//  System.out.println("x="+raIzvjestaji.getOdbiciWhQueryIzv(raIzvjestaji.ID_3_3));
//}
//  System.out.println("R=... "+raIzvjestaji.getOdbiciWhQueryIzv((short)10001,(short)1));
//  System.out.println("R2=... "+raIzvjestaji.getPrimanjaWhQueryIzv((short)10001,(short)20));
//}
//  System.out.println("neto = 5200 bruto = "+raObracunPL.getInstance().getBrutoIzNeta(new BigDecimal("5200.00"),"1"));
/*
  sysoutTEST ST = new sysoutTEST(false);
  hr.restart.pl.raOdbici opl = hr.restart.pl.raOdbici.getInstance();
  String cradnik = javax.swing.JOptionPane.showInputDialog("Radnik:");
  int mode = opl.OBR;
  ST.showInFrame(Util.getNewQueryDataSet(
      "SELECT * FROM odbiciobr where ("+
      opl.getOdbiciWhereQuery(opl.POREZ_param,"odbiciobr")
      +") or ("+opl.getOdbiciWhereQuery(opl.PRIREZ_param,"odbiciobr")
      +")"),"Svi doprinosi");
//"select <*> from odbici<..> where ("+getOdbiciWhereQuery(DOPR_param1,odbici<..>)+") or ("+getOdbiciWhereQuery(DOPR_param2,odbici<..>)+") AND ..."
/*
  if (cradnik == null) System.exit(0);
  do {
    System.out.println("OLAKŠICE "+cradnik);
    ST.showInFrame(opl.getOlaksice(cradnik,mode),"OLAKŠICE");
    System.out.println("DOPRINOSI R"+cradnik);
    ST.showInFrame(opl.getDoprinosiRadnik(cradnik,mode),"DOPRINOSI R");
    System.out.println("POREZI "+cradnik);
    ST.showInFrame(opl.getPorez(cradnik,mode),"POREZI");
    System.out.println("PRIREZ "+cradnik);
    ST.showInFrame(opl.getPrirez(cradnik,mode),"PRIREZ");
    System.out.println("KREDITI "+cradnik);
    ST.showInFrame(opl.getKrediti(cradnik,mode),"KREDITI");
    System.out.println("DOPRINOSI NA"+cradnik);
    ST.showInFrame(opl.getDoprinosiNa(cradnik,mode),"DOPRINOSI NA");
    System.out.println("KUMULRAD");
    ST.showInFrame(Util.getNewQueryDataSet("SELECT * FROM kumulrad where cradnik = '"+cradnik+"'"),"KUMULRAD");
    QueryDataSet radpl = Util.getNewQueryDataSet("SELECT cvro FROM radnicipl where cradnik = '"+cradnik+"'");
    radpl.first();
    QueryDataSet rad = Util.getNewQueryDataSet("SELECT corg FROM radnici where cradnik = '"+cradnik+"'");
    rad.first();
    ST.showInFrame(Util.getNewQueryDataSet("SELECT * FROM kumulorg where "
                                   +"corg = '"+rad.getString("CORG")
                                   +"' AND cvro = '"+radpl.getString("CVRO")+"'"),"KUMULORG");

//    cradnik = javax.swing.JOptionPane.showInputDialog("Radnik:");
    cradnik = null;
  } while (cradnik != null);
  */
//  System.exit(0);
//}
//
}


////old stuff
/*
  private void calcPorez_old() throws Exception {
    //prosli mjesec
    mjVals = getMjVals();//mjVals[0] = porosn; mjVals[1] = iskneop
    //olaksice
    calcOlaksice();
    //porez
    if (kumulrad.getBigDecimal("POROSN").compareTo(nula) <= 0) return;

//    QueryDataSet porezi = odbici.getPorez(radnici.getString("CRADNIK"),odbici.DEF);
//    if (porezi.getRowCount() == 0) return;
//    short[] cpor = getCpors(porezi);
//    BigDecimal stpor1 = getPorez_x(porezi,cpor[0]).getBigDecimal("STOPA").divide(sto,BigDecimal.ROUND_HALF_UP);
//    BigDecimal stpor2 = getPorez_x(porezi,cpor[1]).getBigDecimal("STOPA").divide(sto,BigDecimal.ROUND_HALF_UP);
//    BigDecimal stpor3 = getPorez_x(porezi,cpor[2]).getBigDecimal("STOPA").divide(sto,BigDecimal.ROUND_HALF_UP);
///replacamo ovo gore s ovim dolje:))
    BigDecimal[] stope = getStopePoreza(kumulrad.getString("CRADNIK"));
    BigDecimal stpor1 = stope[0];
    BigDecimal stpor2 = stope[1];
    BigDecimal stpor3 = stope[2];
    BigDecimal stpor4 = stope[3];
    if (stpor1.add(stpor2).add(stpor3).add(stpor4).compareTo(nula) == 0) return;
    BigDecimal oldporosnmj = mjVals[0]; // za prosle obracune 
    BigDecimal porosn = kumulrad.getBigDecimal("POROSN").add(oldporosnmj);
    BigDecimal porosnmj = kumulrad.getBigDecimal("POROSN");
    BigDecimal minpl = dm.getParametripl().getBigDecimal("MINPL");
    BigDecimal osnpor1 = dm.getParametripl().getBigDecimal("OSNPOR1");
    BigDecimal osnpor2 = dm.getParametripl().getBigDecimal("OSNPOR2");
    BigDecimal osnpor3 = dm.getParametripl().getBigDecimal("OSNPOR3");
    BigDecimal porez1 = nula;
    BigDecimal porez2 = nula;
    BigDecimal porez3 = nula;
    BigDecimal porez4 = nula;
    //ispod osn1
    if (porosn.compareTo(osnpor1) <= 0) { //if $tonumber(losnovica,2)<=$tonumber(lporminpl*2,2)
      porez1 = porosnmj.multiply(stpor1);   //  let lporez15=lporosnovica*l15
      porez2 = nula;                      //  let lporez25=0
      porez3 = nula;                      //  let lporez35=0
    }                                     //endif
    //izmedju osn1 i osn2
    if ((porosn.compareTo(osnpor1) > 0) && (porosn.compareTo(osnpor2) <= 0)) {//if $tonumber(losnovica,2)>$tonumber(lporminpl*2,2) and $tonumber(losnovica,2)<=$tonumber(lporminpl*5,2)
      //por1                                                                  //  %%% 15%
      if (oldporosnmj.compareTo(osnpor1) <= 0) {                              //  if $tonumber(losnmj,2)<=$tonumber(lporminpl*2,2)
        porez1 = osnpor1.add(oldporosnmj.negate()).multiply(stpor1);          //    let lporez15=(lporminpl*2-losnmj)*l15
      } else {                                                                //  else
        porez1 = nula;                                                        //    let lporez15=0
      }                                                                       //  endif
      //por2 i por3                                                           //  %%% 25% i 35%
      if (oldporosnmj.compareTo(osnpor1) > 0) {                               //  if $tonumber(losnmj,2)>$tonumber(2*lporminpl,2)
        porez2 = porosn.add(oldporosnmj.negate()).multiply(stpor2);           //    let lporez25=(losnovica-losnmj)*l20
      } else {                                                                //  else
        porez2 = porosn.add(osnpor1.negate()).multiply(stpor2);               //    let lporez25=(losnovica-2*lporminpl)*l20
      }                                                                       //  endif
      porez3 = nula;                                                          //  let lporez35=0
    }                                                                         //endif

    if (porosn.compareTo(osnpor2) > 0) {                              //if $tonumber(losnovica,2)>$tonumber(lporminpl*5,2)
      //por1                                                          //  %%% 15%
      if (oldporosnmj.compareTo(osnpor1) <= 0) {                      //  if $tonumber(losnmj,2)<=$tonumber(lporminpl*2,2)
        porez1 = osnpor1.add(oldporosnmj.negate()).multiply(stpor1);  //    let lporez15=(lporminpl*2-losnmj)*l15
      } else {                                                        //  else
        porez1 = nula;                                                //    let lporez15=0
      }                                                               //  endif
      //por2                                                          //  %%% 25%
      if (oldporosnmj.compareTo(osnpor2) <= 0) {                      //  if $tonumber(losnmj,2)<=$tonumber(lporminpl*5,2)
        if (oldporosnmj.compareTo(osnpor1) > 0) {                     //    if $tonumber(losnmj,2)>$tonumber(lporminpl*2,2)
          porez2 = osnpor2.add(oldporosnmj.negate()).multiply(stpor2);//      let lporez25=(lporminpl*5-losnmj)*l20
        } else {                                                      //    else
          porez2 = osnpor2.add(osnpor1.negate()).multiply(stpor2);    //      let lporez25=(lporminpl*5-lporminpl*2)*l20
        }                                                             //    endif
      } else {                                                        //  else
        porez2 = nula;                                                //    let lporez25=0
      }                                                               //  endif
      //por3                                                          //  %%% 35%
      if (oldporosnmj.compareTo(osnpor2) > 0) {                       //  if $tonumber(losnmj,2)>$tonumber(5*lporminpl,2)

//oldporosnmj = 23482.96
//porosn = 36085.5440000000
//porosnmj = 12602.5840000000

//      if (porosn.add(oldporosnmj.negate()).compareTo(osnpor3.add(osnpor2.negate()))>0) {  //   if (losnovica - losnmj) > lporminpl_35
        if (porosn.compareTo(osnpor3.add(osnpor2.negate()))>0) {  //   if (losnovica - losnmj) > lporminpl_35
          if (oldporosnmj.compareTo(osnpor3)>0) { //potrosio je 3 osnovicu prosli mjesec
            porez3 = nula;
            porez4 = porosnmj.multiply(stpor4);
          } else {
//          porez3 = osnpor3.add(osnpor2.negate()).multiply(stpor3);
            porez3 = osnpor3.add(osnpor2.negate()).multiply(stpor3);
            porez3 = porez3.add(mjVals[4].negate());
            BigDecimal potroseno_na_3 = osnpor3.add(osnpor2.negate());
            if (mjVals[4].compareTo(nula) > 0) potroseno_na_3 = porez3.divide(stpor3,8,BigDecimal.ROUND_HALF_UP);
            porez4 = porosn.add(potroseno_na_3.negate()).multiply(stpor4);
//            porez4 = porosn.add(oldporosnmj.negate().add(osnpor3.add(osnpor2.negate()).negate()))
//                   .multiply(stpor4);
          }
        } else {                                                      //   else
          porez3 = porosn.add(oldporosnmj.negate()).multiply(stpor3); //    let lporez35=(losnovica-losnmj)*l35
        }                                                             //   endif
      } else {                                                        //  else
        if (porosn.compareTo(osnpor3)>0) {                           //   if (losnovica-lporminpl_5) > lporminpl_35
          porez3 = osnpor3.add(osnpor2.negate()).multiply(stpor3);
          porez4 = porosn.add(osnpor3.negate())
                   .multiply(stpor4);
        } else {
          porez3 = porosn.add(osnpor2.negate()).multiply(stpor3);       //    let lporez35=(losnovica-5*lporminpl)*l35
        }
      }                                                               //  endif
    }                                                                 //endif - kraj obr. poreza
    // kumulrad
    kumulrad.setBigDecimal("POR1",porez1);
    kumulrad.setBigDecimal("POR2",porez2);
    kumulrad.setBigDecimal("POR3",porez3);
    kumulrad.setBigDecimal("POR4",porez4);
    kumulrad.setBigDecimal("PORUK",porez1.add(porez2).add(porez3).add(porez4));
    //dodaj odbiciobr za porez
    QueryDataSet _qodbici = odbici.getPorez(kumulrad.getString("CRADNIK"),raOdbici.DEF);
    if (porez1.compareTo(nula) > 0) {
      ld.raLocate(_qodbici,"RBRODB","1");
      addOdbitakPorez(_qodbici,porez1);
    }
    if (porez2.compareTo(nula) > 0) {
      ld.raLocate(_qodbici,"RBRODB","2");
      addOdbitakPorez(_qodbici,porez2);
    }
    if (porez3.compareTo(nula) > 0) {
      ld.raLocate(_qodbici,"RBRODB","3");
      addOdbitakPorez(_qodbici,porez3);
    }
    if (porez4.compareTo(nula) > 0) {
      ld.raLocate(_qodbici,"RBRODB","4");
      addOdbitakPorez(_qodbici,porez4);
    }
    // kumulorg
    addBigDec_kumulorg("POR1",porez1);
    addBigDec_kumulorg("POR2",porez2);
    addBigDec_kumulorg("POR3",porez3);
    addBigDec_kumulorg("POR4",porez4);
    addBigDec_kumulorg("PORUK",kumulrad.getBigDecimal("PORUK"));
    if (kumulrad.getBigDecimal("PORUK").compareTo(nula) <= 0) return;
    calcPrirez();
  }
 */