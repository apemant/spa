/****license*****************************************************************
**   file: CalcRazPor.java
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
/*
 * Created on Nov 29, 2004
 */
package hr.restart.pl;

import hr.restart.baza.Condition;
import hr.restart.baza.FondSati;
import hr.restart.baza.Kumulrad;
import hr.restart.baza.Kumulradarh;
import hr.restart.baza.Odbici;
import hr.restart.baza.Parametripl;
import hr.restart.baza.Radnici;
import hr.restart.baza.dM;
import hr.restart.robno.dlgKupac;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraDialog;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.dlgGetKnjig;

import java.awt.Frame;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author andrej
 * Izracunava razliku poreza za godinu i za radnika. Koristi temp StorageDataSet (Key, String , BigDecimal) u koji ubacuje razliku
 * za radnika i iz kojeg poslije ubacuje u odbici ("RA","P","2","0") za porez i ("RA","R","2","0") za prirez
 * u odbici.
 * za svaki gorenavedeni StorageDataSet postoji i jedan detail StorageDataSet slicne strukture (Sort, Key, String , BigDecimal) sa detaljima
 * o obraèunu 
 *
//TODO napraviti ekran za ovo + opcija
//TODO napraviti ubacivanje u odbitke
 */
public class CalcRazPor {
  private static Logger log = Logger.getLogger(CalcRazPor.class);
  private StorageDataSet masterCaPo;
  private StorageDataSet detailCaPo;
  private StorageDataSet _radnici;
  private BigDecimal sto = new BigDecimal("100.00");
  private boolean realolak;
  private Boolean cleanOdbici = null;
  private BigDecimal limit;
  private int _godina;
  private boolean preview = false;
  /**
   * @param radnici bitno je samo da radnici imaju kolonu CRADNIK i CORG
   * @param godina godina za koju se radi konacni obracun poreza
   */
  public CalcRazPor() {
    realolak = new Boolean(frmParam.getParam("pl","rporrolak","true","Koristiti prave olakšice pri obraèunu razlike poreza")).booleanValue();
    initSets();
  }
  public void calcRazl() {
    _radnici.open();
    for (_radnici.first(); _radnici.inBounds(); _radnici.next()) {
      calc(_radnici.getString("CRADNIK"), _radnici.getString("CORG"));
    }
  }
  public CalcRazPor(int godina) {
    this();
    setGodina(godina);
    setRadnici(Radnici.getDataModule().getFilteredDataSet(Condition.equal("CORG",OrgStr.getKNJCORG())));
    //calcRazl();    
//    Radnici.getDataModule().getFilteredDataSet(Condition.equal("CORG",OrgStr.getKNJCORG())),godina);
  }
  public void showDetail() {
//    sysoutTEST st = new sysoutTEST(false);
//    st.showInFrame(detailCaPo,"detail");  
    showInFrame(detailCaPo, "Detalji godišnjeg obraèuna poreza");
    //dw.show();
    
  }
  public void showMaster() {
//    sysoutTEST st = new sysoutTEST(false);
//    st.showInFrame(masterCaPo,"master"); 
    showInFrame(masterCaPo, "Pregled razlike po god. obraèunu poreza");
    
  }
  private void showInFrame(StorageDataSet set, String title) {
    frmTableDataView dw = new frmTableDataView(true, true, false);
    dw.setDataSet(set);
    JraDialog ddw = new JraDialog((Frame)null,title,true);
    ddw.setContentPane(dw.getContentPane());
    ddw.pack();
    ddw.show();
  }
  /**
   * 
   */
  private void calc(String cradnik, String corg) {
    //get data
    String inqrange = raPlObrRange.getInQueryIsp(_godina,1,_godina,12,"");
    Condition condcradnik = Condition.equal("CRADNIK",cradnik);
    if (cradnik.contains("@")) {//hackchuga samo takva za oj4
      condcradnik = Condition.in("CRADNIK", new String[] {cradnik,cradnik.substring(0,cradnik.indexOf("@"))});
    }
    QueryDataSet raddata = Kumulradarh.getDataModule().getTempSet(condcradnik+" and "+inqrange);
    if (log.isDebugEnabled()) {
      log.debug(raddata.getQuery().getQueryString());
    }
    raddata.open();
    raOdbici.getInstance().setObrRange(new MyObrRange(inqrange));
    StorageDataSet orgdata = new StorageDataSet();
    QueryDataSet orgdataqry = Util.getNewQueryDataSet(
        "SELECT godobr, mjobr, rbrobr, datumispl, MINPL FROM kumulorgarh WHERE "
        +Condition.equal("CORG",corg)+" AND "+inqrange
        );
      orgdata.setColumns(orgdataqry.cloneColumns());
      String oldmonth="##";
      orgdata.open();
      for (orgdataqry.first(); orgdataqry.inBounds(); orgdataqry.next()) {
        String currmonth = Util.getUtil().getMonth(orgdataqry.getTimestamp("DATUMISPL"));
        if (!oldmonth.equals(currmonth)) {
          orgdata.insertRow(false);
          orgdataqry.copyTo(orgdata);
          orgdata.post();
        }
      }
    StorageDataSet kumrad = Kumulrad.getDataModule().getTempSet(condcradnik);
    kumrad.open();
    //get values
    Kumulrad.getDataModule().getQueryDataSet().open();
    BigDecimal neto = getSum(raddata, "NETO").add(kumrad.getBigDecimal("NETO"));
    BigDecimal neop = getGodTotalNeop(cradnik, orgdata);
    BigDecimal premije = getPremije(cradnik);
    BigDecimal poros = neto.add(neop.negate()).add(premije.negate());
    if (poros.signum()<0) poros = Aus.zero2;
    StorageDataSet orgosn = new StorageDataSet();
    orgosn.setColumns(new Column[] {dM.createShortColumn("GODOBR"), dM.createShortColumn("MJOBR"),
        dM.createBigDecimalColumn("OSNPOR1"),dM.createBigDecimalColumn("OSNPOR2"),dM.createBigDecimalColumn("OSNPOR3"),dM.createBigDecimalColumn("OSNPOR4"),dM.createBigDecimalColumn("OSNPOR5")});
    Util.fillReadonlyData(orgosn,"SELECT godobr, mjobr, max(osnpor1) as osnpor1, max(osnpor2) as osnpor2, max(osnpor3) as osnpor3, max(osnpor4) as osnpor4, max(osnpor5)  as osnpor5 " +
    		"from kumulorgarh where "+Condition.equal("CORG",corg)+" AND "+inqrange+" group by godobr, mjobr");
    BigDecimal[] osn; 
    BigDecimal[] stpo;
    switch (_godina) {
    case 2010:
      osn = getOsnovice2010(poros, orgosn);
      stpo = getStopePo2010();
      break;

    case 2012:
      osn = getOsnovice2012(poros, orgosn);
      stpo = getStopePo2012();
      break;
      
    default:
      osn = getOsnovice(poros, orgosn);
      stpo = raObracunPL.getInstance().getStopePoreza(cradnik);
      break;
    }
//    = (_godina==2010)?getOsnovice2010(poros, orgosn):getOsnovice(poros, orgosn);
//    = (_godina==2010)?getStopePo2010():raObracunPL.getInstance().getStopePoreza(cradnik);
    BigDecimal por1 = osn[0].multiply(stpo[0]);
    BigDecimal por2 = osn[1].multiply(stpo[1]);
    BigDecimal por3 = osn[2].multiply(stpo[2]);
    BigDecimal por4 = osn[3].multiply(stpo[3]);
    BigDecimal por5 = osn[4].multiply(stpo[4]);
    BigDecimal poruk = por1.add(por2).add(por3).add(por4).add(por5);
    BigDecimal stpr = getPrirez(cradnik);
    BigDecimal prirez = poruk.multiply(stpr);
    BigDecimal porgod = getSum(raddata,"PORUK").add(kumrad.getBigDecimal("PORUK"));
    BigDecimal prirgod = getSum(raddata,"PRIR").add(kumrad.getBigDecimal("PRIR"));
    BigDecimal porprirgod = getSum(raddata,"PORIPRIR").add(kumrad.getBigDecimal("PORIPRIR"));
    //add values
    addDetail(cradnik,10,"NETO","Ukupni godišnji dohodak", neto);
    addDetail(cradnik,20,"NEOP","Ukupni godišnji osobni odbitak", neop);
    addDetail(cradnik,30,"PREMIJE","Ukupne premije osiguranja u godini", premije);
    addDetail(cradnik,40,"POROSN","Ukupna godišnja porezna osnovica", poros);
    addDetail(cradnik,51,"OSN1", "Osnovica za primjenu stope "+stpo[0], osn[0]);
    addDetail(cradnik,52,"OSN2", "Osnovica za primjenu stope "+stpo[1], osn[1]);
    addDetail(cradnik,53,"OSN3", "Osnovica za primjenu stope "+stpo[2], osn[2]);
    addDetail(cradnik,54,"OSN4", "Osnovica za primjenu stope "+stpo[3], osn[3]);
    //2010
    addDetail(cradnik,55,"OSN5", "Osnovica za primjenu stope "+stpo[4], osn[4]);
    
    addDetail(cradnik,61,"POR1", "Godišnji porez po stopi "+stpo[0], por1);
    addDetail(cradnik,62,"POR2", "Godišnji porez po stopi "+stpo[1], por2);
    addDetail(cradnik,63,"POR3", "Godišnji porez po stopi "+stpo[2], por3);
    addDetail(cradnik,64,"POR4", "Godišnji porez po stopi "+stpo[3], por4);
    //2010
    addDetail(cradnik,65,"POR5", "Godišnji porez po stopi "+stpo[4], por5);

    addDetail(cradnik,70,"PORUK", "Ukupni godišnji porez ", poruk);
    addDetail(cradnik,80,"PRIREZ", "Ukupni godišnji prirez ", prirez);
    addDetail(cradnik,90,"PORIPRIR", "Ukupna godišnja obveza poreza i prireza ", poruk.add(prirez));
    addDetail(cradnik,101,"PORGOD", "Ukupno uplaæeni porez ", porgod);
    addDetail(cradnik,101,"PRIRGOD", "Ukupno uplaæeni prirez ", prirgod);
    addDetail(cradnik,102,"PORPRIRGOD", "Ukupno uplaæeni porez i prirez ", porprirgod);
    addDetail(cradnik,111,"RAZPOR", "RAZLIKA POREZA ", poruk.add(porgod.negate()));
    addDetail(cradnik,112,"RAZPRIR", "RAZLIKA PRIREZA ", prirez.add(prirgod.negate()));
    addDetail(cradnik,113,"RAZPORPRIR", "RAZLIKA POREZA I PRIREZA ", poruk.add(prirez).add(porprirgod.negate()));
    addMaster(cradnik,cradnik+": razlika poreza za "+_godina, 
        					getDetailValue(cradnik,"RAZPOR"), 
        					getDetailValue(cradnik,"RAZPRIR"), 
        					getDetailValue(cradnik,"RAZPORPRIR"));
  }
  
  private BigDecimal getDetailValue(String cradnik, String key) {
    if (lookupData.getlookupData().raLocate(detailCaPo,new String[] {"CRADNIK","KEY"}, new String[] {cradnik,key})) {
      return detailCaPo.getBigDecimal("IZNOS");
    }
    return null;
  }
  
  private BigDecimal[] getOsnovice(BigDecimal poros, StorageDataSet orgosn) {
    
    BigDecimal[] ret = new BigDecimal[5];
    Parametripl.getDataModule().getQueryDataSet().open();
    
    // osnpor1
//    BigDecimal parporos1 = Parametripl.getDataModule().getQueryDataSet().getBigDecimal("OSNPOR1").multiply(new BigDecimal(12));
    BigDecimal parporos1 = Parametripl.getDataModule().getQueryDataSet().getBigDecimal("OSNPOR1").add(getSum(orgosn, "OSNPOR1"));
    if (poros.compareTo(parporos1)>=0) {
      ret[0] = parporos1;
      //osnpor2
//      BigDecimal parporos2 = Parametripl.getDataModule().getQueryDataSet().getBigDecimal("OSNPOR2").multiply(new BigDecimal(12))
//      .add(parporos1.negate());
      BigDecimal parporos2 = Parametripl.getDataModule().getQueryDataSet().getBigDecimal("OSNPOR2").add(getSum(orgosn, "OSNPOR2"))
      .add(parporos1.negate());
      if (poros.add(parporos1.negate()).compareTo(parporos2) >= 0) {
        ret[1] = parporos2;
        //osnpor3 
//        BigDecimal parporos3 = Parametripl.getDataModule().getQueryDataSet().getBigDecimal("OSNPOR3").multiply(new BigDecimal(12))
//    	  .add(parporos1.negate()).add(parporos2.negate());
        BigDecimal parporos3 = Parametripl.getDataModule().getQueryDataSet().getBigDecimal("OSNPOR3").add(getSum(orgosn, "OSNPOR3"))
        .add(parporos1.negate()).add(parporos2.negate());
        if (poros.add(parporos1.negate()).add(parporos2.negate()).compareTo(parporos3) >= 0) {
          ret[2] = parporos3;
          ret[3] = poros.add(parporos1.negate()).add(parporos2.negate()).add(parporos3.negate());
        } else {
          ret[2] = poros.add(parporos1.negate()).add(parporos2.negate());
          ret[3] = Aus.zero2;
        }
      } else {
        ret[1] = poros.add(parporos1.negate());
        ret[2] = Aus.zero2;
        ret[3] = Aus.zero2;
      }
    } else {
      ret[0] = poros;
      ret[1] = Aus.zero2;
      ret[2] = Aus.zero2;
      ret[3] = Aus.zero2;
    }
    ret[4]=Aus.zero2;
    return ret;
  }
  private BigDecimal[] getStopePo2012() {
    return new BigDecimal[] {
        new BigDecimal("0.1200"),
        new BigDecimal("0.2283"),
        new BigDecimal("0.2500"),
        new BigDecimal("0.3750"),
        new BigDecimal("0.4000")
    };
  }
  /*
0-26.400,00 kn        26.400,00   12%
26.400,00-43.200,00   16.800,00   22,83%
43.200,00-105.600,00  62.400,00   25%
105.600,00-129.600,00 24.000,00   37.5%
129.600,00-beskonacno             40%      

bravo Zoki, bravo Linicu
   */
  private BigDecimal[] getOsnovice2012(BigDecimal poros, StorageDataSet orgosn) {
    BigDecimal[] ret = new BigDecimal[5];
    Arrays.fill(ret, Aus.zero2);
    Parametripl.getDataModule().getQueryDataSet().open();
    
    // osnpor1
//    BigDecimal parporos1 = Parametripl.getDataModule().getQueryDataSet().getBigDecimal("OSNPOR1").multiply(new BigDecimal(12));
//    BigDecimal parporos1 = Parametripl.getDataModule().getQueryDataSet().getBigDecimal("OSNPOR1").add(getSum(orgosn, "OSNPOR1"));
    BigDecimal parporos1 = new BigDecimal("26400.00"); //d1
    if (poros.compareTo(parporos1)>=0) {
      ret[0] = parporos1;
      BigDecimal parporos2 = new BigDecimal("16800.00");// d2
      if (poros.add(parporos1.negate()).compareTo(parporos2) >= 0) {
        ret[1] = parporos2;
        BigDecimal parporos3 = new BigDecimal("62400.00");// d3  
        if (poros.add(parporos1.negate()).add(parporos2.negate()).compareTo(parporos3) >= 0) {
          ret[2] = parporos3;
          BigDecimal parporos4 = new BigDecimal("24000.00");// d4 
          if (poros.add(parporos1.negate()).add(parporos2.negate()).add(parporos3.negate()).compareTo(parporos4) >= 0) {
            ret[3] = parporos4;
            ret[4] = poros.add(parporos1.negate()).add(parporos2.negate()).add(parporos3.negate()).add(parporos4.negate());
          } else {
            ret[3] = poros.add(parporos1.negate()).add(parporos2.negate()).add(parporos3.negate());
            ret[4] = Aus.zero2;
          }
        } else {
          ret[2] = poros.add(parporos1.negate()).add(parporos2.negate());
          ret[3] = Aus.zero2;
          ret[4] = Aus.zero2;
        }
      } else {
        ret[1] = poros.add(parporos1.negate());
        ret[2] = Aus.zero2;
        ret[3] = Aus.zero2;
        ret[4] = Aus.zero2;
      }
    } else {
      ret[0] = poros;
      ret[1] = Aus.zero2;
      ret[2] = Aus.zero2;
      ret[3] = Aus.zero2;
      ret[4] = Aus.zero2;
    }
    return ret;
  }
  private BigDecimal[] getStopePo2010() {
    return new BigDecimal[] {
        new BigDecimal("0.135"),
        new BigDecimal("0.250"),
        new BigDecimal("0.300"),
        new BigDecimal("0.375"),
        new BigDecimal("0.425")
    };
  }
  /*
          0 - 43.200,00   13,50%
  43.200,01 - 108.000,00  25,00%
 108.000,01 - 129.600,00  30,00%
 129.600,01 - 302.400,00  37,50%
 302.400,01 - ...         42,50% 
 bravo jaca! bravo shuki!
   */
  private BigDecimal[] getOsnovice2010(BigDecimal poros, StorageDataSet orgosn) {
    
    BigDecimal[] ret = new BigDecimal[5];
    Arrays.fill(ret, Aus.zero2);
    Parametripl.getDataModule().getQueryDataSet().open();
    
    // osnpor1
//    BigDecimal parporos1 = Parametripl.getDataModule().getQueryDataSet().getBigDecimal("OSNPOR1").multiply(new BigDecimal(12));
//    BigDecimal parporos1 = Parametripl.getDataModule().getQueryDataSet().getBigDecimal("OSNPOR1").add(getSum(orgosn, "OSNPOR1"));
    BigDecimal parporos1 = new BigDecimal("43200.00");
    if (poros.compareTo(parporos1)>=0) {
      ret[0] = parporos1;
      BigDecimal parporos2 = new BigDecimal("64800.00");//108000-43200
      if (poros.add(parporos1.negate()).compareTo(parporos2) >= 0) {
        ret[1] = parporos2;
        BigDecimal parporos3 = new BigDecimal("21600.00");//129600-108000        
        if (poros.add(parporos1.negate()).add(parporos2.negate()).compareTo(parporos3) >= 0) {
          ret[2] = parporos3;
          BigDecimal parporos4 = new BigDecimal("172800.00");//302400-129600        
          if (poros.add(parporos1.negate()).add(parporos2.negate()).add(parporos3.negate()).compareTo(parporos4) >= 0) {
            ret[3] = parporos4;
            ret[4] = poros.add(parporos1.negate()).add(parporos2.negate()).add(parporos3.negate()).add(parporos4.negate());
          } else {
            ret[3] = poros.add(parporos1.negate()).add(parporos2.negate()).add(parporos3.negate());
            ret[4] = Aus.zero2;
          }
        } else {
          ret[2] = poros.add(parporos1.negate()).add(parporos2.negate());
          ret[3] = Aus.zero2;
          ret[4] = Aus.zero2;
        }
      } else {
        ret[1] = poros.add(parporos1.negate());
        ret[2] = Aus.zero2;
        ret[3] = Aus.zero2;
        ret[4] = Aus.zero2;
      }
    } else {
      ret[0] = poros;
      ret[1] = Aus.zero2;
      ret[2] = Aus.zero2;
      ret[3] = Aus.zero2;
      ret[4] = Aus.zero2;
    }
    return ret;
  }
  private BigDecimal getPrirez(String _crad) {
    //prirez
    QueryDataSet qprirez = null;
    BigDecimal prir = Aus.zero2;
    raOdbici.getInstance().setRadniciPL(null);
    qprirez = raOdbici.getInstance().getPrirez(_crad,raOdbici.DEF);
System.out.println("******"+_crad+"******"+qprirez);
      if (qprirez.getRowCount() != 0) {
        qprirez.first();
        prir = qprirez.getBigDecimal("STOPA").divide(sto,4,BigDecimal.ROUND_HALF_UP);
      }
    return prir;
  }
  /**
   * @param cradnik
   * @return
   */
  private BigDecimal getPremije(String cradnik) {
    BigDecimal premije = null;
    //arhiva
    StorageDataSet prems = raOdbici.getInstance().getPremije(cradnik,raOdbici.ARH);
    premije = getSum(prems, "OBRIZNOS");
    //radni
    prems = raOdbici.getInstance().getPremije(cradnik,raOdbici.OBR);
    premije = premije.add(getSum(prems, "OBRIZNOS"));
    return premije;
  }
  private BigDecimal getGodTotalNeop(String cradnik, StorageDataSet orgdata) {
    //orgdata godobr, mjobr, rbrobr, datumispl, MINPL FROM kumulorgarh
    String olakqry = hr.restart.pl.raOdbici.getInstance().getOdbiciWhereQuery("RA","K","1","4",cradnik, null , "odbiciarh"); //npr."CVRODB in (6) and CKEY='cradnik')
    //osnovni odbitak iz fonda sati
    QueryDataSet fondsati = FondSati.getDataModule().getTempSet(Condition.equal("KNJIG", dlgGetKnjig.getKNJCORG()).and(Condition.equal("GODINA", (short)_godina)));
    //trenutni osobni odbitak
    StorageDataSet currolaks = raOdbici.getInstance().getOlaksice(cradnik,raOdbici.DEF);
    BigDecimal currkoef = getSum(currolaks,"STOPA");
    QueryDataSet pars = Parametripl.getDataModule().getTempSet();
    pars.open();
    BigDecimal currminpl = pars.getBigDecimal("MINPL");
    //zadnji koeficijent prije zadanog perioda
    Calendar c = Calendar.getInstance();
    c.set(_godina-1, 11, 1);
    Timestamp lastdaybefore = Util.getUtil().getLastDayOfYear(new Timestamp(c.getTimeInMillis()));
    String _q;
    QueryDataSet lastarh = Util.getNewQueryDataSet(_q = "SELECT max(DATUMISPL) as DATUMISPL FROM kumulorgarh WHERE " + Condition.equal("KNJIG", dlgGetKnjig.getKNJCORG())
        .and(Condition.till("DATUMISPL", lastdaybefore)));
System.out.println(_q);
    lastarh.open();
    QueryDataSet lastodbici = Util.getNewQueryDataSet(_q = "SELECT odbiciarh.obrstopa FROM odbiciarh,kumulorgarh WHERE " +
        "odbiciarh.godobr = kumulorgarh.godobr AND odbiciarh.mjobr = kumulorgarh.mjobr AND odbiciarh.rbrobr = kumulorgarh.rbrobr AND " +
        Condition.equal("DATUMISPL", lastarh) + " AND "+olakqry);
System.out.println(_q);
    lastodbici.open();
    BigDecimal lastkoef = getSum(lastodbici, "OBRSTOPA");
    BigDecimal lastminpl = currminpl;
//    		"KNJIG = '"+dlgGetKnjig.getKNJCORG()+"' and DATUMISPL < ";
    BigDecimal godtotalneop = Aus.zero2;
    for (int mji = 1; mji < 13; mji++) {
      //ima li orgdata u mji
      boolean orgdataLocated = false;
      for (orgdata.first(); orgdata.inBounds(); orgdata.next()) {
        if (Integer.parseInt(Util.getUtil().getMonth(orgdata.getTimestamp("DATUMISPL")))==mji) {
          orgdataLocated = true;
          break;
        }
      }
System.out.println("orgdatalocated = "+orgdataLocated);
      if (lookupData.getlookupData().raLocate(fondsati, "MJESEC", mji+"")) {
System.out.println("fondsati.isNull(MINPL) = "+fondsati.isNull("MINPL"));
System.out.println("fondsati.getBigDecimal(MINPL).signum() == 0 = "+(fondsati.getBigDecimal("MINPL").signum()));
        if (!(fondsati.isNull("MINPL") || fondsati.getBigDecimal("MINPL").signum() == 0)) {
          lastminpl = fondsati.getBigDecimal("MINPL");
        }
      }
System.out.println(fondsati);
System.out.println(orgdata);

      if (orgdataLocated) {
        //ako ima pokupi MINPL (!realolak) i obrstopa 
        //lastminpl = orgdata.getBigDecimal("MINPL");
        QueryDataSet odbicimj = Util.getNewQueryDataSet(_q = "SELECT odbiciarh.obrstopa FROM odbiciarh WHERE " +
            Condition.whereAllEqual(new String[] {"GODOBR","MJOBR","RBROBR"}, orgdata)
             + " AND "+olakqry);
System.out.println(_q);
        lastkoef = getSum(odbicimj, "OBRSTOPA"); 
      } else {
        //ako nema uzmi MINPL iz fondsati i lastkoef
      }
System.out.println("Za "+mji+":: godtotalneop = godtotalneop + "+lastminpl+" + "+lastminpl+"*"+lastkoef+" = "+godtotalneop+" + "+lastminpl.add(lastminpl.multiply(lastkoef)));
      godtotalneop = godtotalneop.add(lastminpl).add(lastminpl.multiply(lastkoef));
System.out.println("Za "+mji+":: godtotalneop = "+godtotalneop);     
    }
    return godtotalneop;
  }
  /**
   * @param cradnik
   * @return
   */
  private BigDecimal getGodTotalNeop__old(String cradnik, StorageDataSet orgdata) {
    BigDecimal dvanaest = new BigDecimal(12);
    StorageDataSet currolaks = raOdbici.getInstance().getOlaksice(cradnik,raOdbici.DEF);
    //BigDecimal currkoef = new BigDecimal("1.00");
    //currkoef = currkoef.add(getSum(currolaks,"STOPA"));
    BigDecimal currkoef = getSum(currolaks,"STOPA");
    QueryDataSet arholaks = raOdbici.getInstance()
      .getOdbici(raOdbici.OLAK_param, "odbiciarh", cradnik, raOdbici.ARH);
    BigDecimal arhkoef = Aus.zero2;//getSum(arholaks, "OBRSTOPA");
    HashMap corg_godmjrbr_mjesispl = new HashMap();
    String oldmjobr = "##";
    int olakcnt = 0;
    for (arholaks.first(); arholaks.inBounds(); arholaks.next()) {
      //ako ima vise isplata/obracuna u istom mjesecu da ne zbraja duplo
      //mjobr
      String keyobr = _radnici.getString("CORG")
                     +"-"+arholaks.getShort("GODOBR")
                     +"-"+arholaks.getShort("MJOBR")
                     +"-"+arholaks.getShort("RBROBR");
      if (corg_godmjrbr_mjesispl.get(keyobr) == null) {//cashin'n'poolin'
        Timestamp datispl = Util.getNewQueryDataSet("SELECT DATUMISPL from kumulorgarh WHERE " +
                "CORG = '"+_radnici.getString("CORG")+"' AND GODOBR = "+arholaks.getShort("GODOBR")
                +" AND MJOBR = "+arholaks.getShort("MJOBR")+" AND RBROBR = "+arholaks.getShort("RBROBR"))
                .getTimestamp("DATUMISPL");
         corg_godmjrbr_mjesispl.put(keyobr,Util.getUtil().getMonth(datispl)); 
      }
      String mjobr = (String)corg_godmjrbr_mjesispl.get(keyobr);
      if (!oldmjobr.equals(mjobr)) {
        arhkoef = arhkoef.add(arholaks.getBigDecimal("OBRSTOPA"));
        oldmjobr = mjobr;
        olakcnt++;
      }
    }
    //ako ima manje od 12 isplata u godini dodaj jos 12-n puta tekuci koef olaksice
    while (olakcnt<12) {
      arhkoef = arhkoef.add(currkoef);
      olakcnt++;
    }
    System.out.println("**** "+cradnik+" **** arhkoef = "+arhkoef);
    
    
    BigDecimal minpl;
    if (realolak) {
      if (log.isDebugEnabled()) {
        log.debug("koristim prave olaksice iz kumulorgarh ...");
      }
      minpl = getSum(orgdata, "MINPL");
      //ako ima manje od 12 isplata u godini dodaj jos 12-n puta tekucu olaksicu
      int minplcnt = orgdata.getRowCount();
      Parametripl.getDataModule().getQueryDataSet().open();
      while (minplcnt<12) {
        minpl = minpl.add(Parametripl.getDataModule().getQueryDataSet().getBigDecimal("MINPL"));
        minplcnt++;
      }
      System.out.println("*** "+cradnik+" godminpl = "+minpl);
    } else {
	    Parametripl.getDataModule().getQueryDataSet().open();
	    minpl = Parametripl.getDataModule().getQueryDataSet().getBigDecimal("MINPL").multiply(dvanaest);
    }
    System.out.println("*** "+cradnik+"godolak = ("+minpl+"/12)*(12+"+arhkoef+")");
    return minpl.divide(dvanaest,BigDecimal.ROUND_HALF_UP).multiply(dvanaest.add(arhkoef));
  }

  private void addDetail(String cradnik, int rbr, String key, String opis, BigDecimal iznos) {
    detailCaPo.insertRow(false);
    detailCaPo.setString("CRADNIK",cradnik);
    detailCaPo.setShort("RBR",(short) rbr);
    detailCaPo.setString("KEY",key);
    detailCaPo.setString("OPIS",opis);
    detailCaPo.setBigDecimal("IZNOS",iznos);
    detailCaPo.post();
  }
  private void addMaster(String cradnik, String opis, BigDecimal rpor, BigDecimal rprir, BigDecimal rporprir) {
    masterCaPo.insertRow(false);
    masterCaPo.setString("CRADNIK",cradnik);
    masterCaPo.setString("OPIS",opis);
    masterCaPo.setBigDecimal("RPOR",rpor);
    masterCaPo.setBigDecimal("RPRIR",rprir);
    masterCaPo.setBigDecimal("RPORPRIR",rporprir);
    if (masterCaPo.getBigDecimal("RPORPRIR").abs().compareTo(getLimit())<=0) {
      masterCaPo.setString("STATUS","<");
    } else if (!workedAllYear(cradnik)) {
      masterCaPo.setString("STATUS","G");
    } else {
      masterCaPo.setString("STATUS","D");
    }
    masterCaPo.post();
  }
  private boolean workedAllYear(String cradnik) {
    Calendar c = Calendar.getInstance();
    c.set(_godina, 0, 1);
    Timestamp prvi = new Timestamp(c.getTimeInMillis());
    c.set(_godina+1, 0, 1);
    Timestamp zadnji = new Timestamp(c.getTimeInMillis());
    QueryDataSet ayset = Aus.q("SELECT DATDOL, DATODL from radnicipl where cradnik = '"+cradnik+"'");
    ayset.open();
    if (ayset.getRowCount() == 0) return false;
    ayset.first();
    
    if (!ayset.isNull("DATDOL")) {
      if (ayset.getTimestamp("DATDOL").after(prvi)) return false;
    }
    if (!ayset.isNull("DATODL")) {
      if (ayset.getTimestamp("DATODL").before(zadnji)) return false;
    }
    return true;
  }
  /**
   * Dodaje izracunate razlike poreza u odbitke
   * ("RA","P","2","0") za porez 
   * ("RA","R","2","0") za prirez
   * pronalazi pripadajuce cvrodb, ako ih nema - exception 
   */
  public void addOdbici() throws Exception {
    new raLocalTransaction() {
      public boolean transaction() throws Exception {
		    raOdbici raodbici = raOdbici.getInstance();
		    String[] p = raOdbici.RAZPOREZA_param;
		    String[] r = raOdbici.RAZPRIREZA_param;
		    String cvrodb_rpor = raodbici.getVrsteOdbKeysQuery(p[0],p[1],p[2],p[3],false)[0];
		    String cvrodb_rprir = raodbici.getVrsteOdbKeysQuery(r[0],r[1],r[2],r[3],false)[0];
		    
		    if (cvrodb_rpor == null || cvrodb_rprir == null) throw new Exception("Potrebno je dodati dvije vrste odbitaka na nivou radnika, tipa 'Razlika poreza' i 'Razlika prireza', i fiksne osnovice !");
		    if (isCleanOdbici()) {
			    cleanOdbici(cvrodb_rpor);
			    cleanOdbici(cvrodb_rprir);
		    }
		    for (masterCaPo.first(); masterCaPo.inBounds(); masterCaPo.next()) {
		      String cr = masterCaPo.getString("CRADNIK");
//		      if (masterCaPo.getBigDecimal("RPORPRIR").abs().compareTo(getLimit())>=0) {
		      if (masterCaPo.getString("STATUS").equals("D")) {
		        addOdbitak(cvrodb_rpor, cr, p, masterCaPo.getBigDecimal("RPOR"));
		        addOdbitak(cvrodb_rprir, cr, r, masterCaPo.getBigDecimal("RPRIR"));
		      }
		    }
		    return true;
      }
    }.execTransaction();
  }

  private void cleanOdbici(String cvrodb) throws Exception {
    raTransaction.runSQL("DELETE FROM odbici where CVRODB = '"+cvrodb+"'");
  }

  /**
   * Transaction dependent
   * @param cradnik
   * @param pr
   * @param iznos
   * @throws Exception
   */
  private void addOdbitak(String cvrodb, String cradnik, String[] pr, BigDecimal iznos) throws Exception {
    QueryDataSet odbitak = raOdbici.getInstance().getOdbici(pr, "odbici", cradnik, raOdbici.DEF);
    if (log.isDebugEnabled()) {
      log.debug("addOdbitak: ");
      log.debug("**cvrodb = "+cvrodb);
      log.debug("**pr = "+pr);
      log.debug("**cradnik = "+cradnik);
      log.debug("**odbitak.getRowCount() = "+odbitak.getRowCount());
      log.debug("**odbitak.query = "+odbitak.getQuery().getQueryString());
    }
    if (odbitak.getRowCount() == 0) {
      //add
      odbitak.insertRow(false);
      odbitak.setShort("CVRODB",new Short(cvrodb).shortValue());
      odbitak.setString("CKEY",cradnik);
      odbitak.setString("CKEY2","");
      odbitak.setShort("RBRODB", (short)1);
    } else if (odbitak.getRowCount() == 1) {
      //update iznos i rata -> ispod if bloka
    } else throw new Exception("Ne bi trebalo biti više od jednog odbitka razlike poreza i prireza po radniku (maticni broj: "+cradnik+")");
    
    odbitak.setBigDecimal("IZNOS",iznos);
    odbitak.setBigDecimal("RATA",iznos);
    if (log.isDebugEnabled()) {
      log.debug("Saving odbitak: "+odbitak);
    }
    raTransaction.saveChanges(odbitak);
  }
  private BigDecimal getSum(DataSet ds, String col) {
    ds.open();
    BigDecimal sum = Aus.zero2;
    for (ds.first(); ds.inBounds(); ds.next()) {
      sum = sum.add(ds.getBigDecimal(col));
    }
    return sum;
  }
  
  /**
   * 
   */
  private void initSets() {
    masterCaPo = new StorageDataSet();
    masterCaPo.addColumn(dM.createStringColumn("STATUS", "STATUS",1));
    masterCaPo.addColumn(dM.createStringColumn("CRADNIK", "Radnik",6));
    masterCaPo.addColumn(dM.createStringColumn("OPIS", "Opis",50));
    masterCaPo.addColumn(dM.createBigDecimalColumn("RPOR","Razl.poreza",2));
    masterCaPo.addColumn(dM.createBigDecimalColumn("RPRIR","Razl.prireza",2));
    masterCaPo.addColumn(dM.createBigDecimalColumn("RPORPRIR","Ukupno",2));
    for (int i = 0; i < masterCaPo.getColumnCount(); i++) 
      masterCaPo.getColumn(i).setTableName("masterCapo");
    masterCaPo.open();
    detailCaPo = new StorageDataSet();
    detailCaPo.addColumn(dM.createStringColumn("CRADNIK", "Radnik",6));
    detailCaPo.addColumn(dM.createShortColumn("RBR", "RBR"));
    detailCaPo.addColumn(dM.createStringColumn("KEY", "Oznaka",20));
    detailCaPo.addColumn(dM.createStringColumn("OPIS", "Opis",50));
    detailCaPo.addColumn(dM.createBigDecimalColumn("IZNOS","Iznos",2));
    for (int i = 0; i < detailCaPo.getColumnCount(); i++) 
      detailCaPo.getColumn(i).setTableName("detailCaPo");
    detailCaPo.open();
  }
  
  public BigDecimal getLimit() {
    if (limit == null) {
      try {
        limit = new BigDecimal(frmParam.getParam("pl","razplimit","1.00","limit za razliku poreza i prireza"));
      } catch (Exception e) {
        log.warn("Invalid property razplimit ("+frmParam.getParam("pl","razplimit")+") !!! Limit set to 1.00");
        limit = new BigDecimal("1.00");
      }
    }
    return limit;
  }
  /**
   * Limit za dodavanje razlike poreza i prireza u odbitke
   * @param limit
   */
  public void setLimit(BigDecimal limit) {
    this.limit = limit;
  }
  
  public boolean isCleanOdbici() {
    if (cleanOdbici == null) {
      try {
        cleanOdbici = new Boolean(frmParam.getParam("pl","cleanrazpl","true","Obrisati odbitke od prošlog izraèuna razlike poreza i prireza"));
      } catch (Exception e) {
        log.warn("Invalid property cleanrazpl ("+frmParam.getParam("pl","cleanrazpl")+") !!! Clean set to true");
        cleanOdbici = Boolean.TRUE;
      }
    }
    return cleanOdbici.booleanValue();
  }
  
  public void setCleanOdbici(boolean _cleanOdbici) {
    this.cleanOdbici = new Boolean(_cleanOdbici);
  }

  /**
   * @return set u kojem su detalji o godisnjem obracunu poreza
   */
  public StorageDataSet getDetailCaPo() {
    return detailCaPo;
  }
  /**
   * @return set u kojem su sadrzani iznosi razlike poreza, prireza i ukupno
   */
  public StorageDataSet getMasterCaPo() {
    return masterCaPo;
  }
  
  /**
   * za zajebat raOdbici
   */
  private class MyObrRange extends raPlObrRange {
    String q;
    public MyObrRange(String query) {
      q=query;
    }
    public String getQuery() {
      return q;
    }
    public String getQuery(String s) {
      return q;
    }
  }
  public int getGodina() {
    return _godina;
  }
  public void setGodina(int godina) {
    this._godina = godina;
  }
  public StorageDataSet getRadnici() {
    return _radnici;
  }
  public void setRadnici(StorageDataSet radnici) {
    this._radnici = radnici;
  }
  public boolean isRealolak() {
    return realolak;
  }
  public void setRealolak(boolean realolak) {
    this.realolak = realolak;
  }
  public boolean isPreview() {
    return preview;
  }
  public void setPreview(boolean preview) {
    this.preview = preview;
  }
}
