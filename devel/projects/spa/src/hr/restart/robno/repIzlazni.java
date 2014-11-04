/****license*****************************************************************
**   file: repIzlazni.java
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
package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.RN;
import hr.restart.baza.RN_subjekt;
import hr.restart.baza.dM;
import hr.restart.baza.dokidod;
import hr.restart.baza.stdoki;
import hr.restart.baza.vtrabat;
import hr.restart.pos.presBlag;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.raSaldaKonti;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;
import hr.restart.util.reports.raStringCache;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.Variant;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repIzlazni implements raReportData {
  public static String vanizkuce = frmParam.getParam("robno", "vanizkuce","X",
  "Izbaciti maticni broj i sifru partnera iz okvira sa adresom kupca na racunima (D/N/X)");
  protected DataSet ds, orig;
  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  protected hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  reportsQuerysCollector repQC = reportsQuerysCollector.getRQCModule();
  protected String[] colname = new String[] {""};
  protected repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  protected raControlDocs rCD = new raControlDocs();
  protected hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  
  private String specGroup;
  private String specText, matText, radText;
  private String specForm, fiskForm;
  
  protected BigDecimal dineto, diprodbp, diprodsp;
    
  protected raStringCache cache = new raStringCache();
  
  protected String lastDok = null;
  
  protected String naps = "";
  protected HashSet allNaps = new HashSet();
  
  public repIzlazni() {
    this(true);
//    System.out.println("repIzlazni <- triba li ovaj ispis na stdio kome??");
  }

  protected repIzlazni(boolean init) {
    fiskForm = frmParam.getParam("robno", "fiskForm", "[FBR]-[FPP]-[FNU]",
        "Format fiskalnog broja izlaznog dokumenta na ispisu");
    if (init) {
      setCurrentDataset();
      ds.open();
      if (orig != null) orig.open();
//      sysoutTEST s = new sysoutTEST(false);
//      s.prn(ds);
      ru.setDataSet(ds);
      rekapPorez();
      nacPl();
      setZnacajkeSubjekta();
      checkSpecGroup();
      lastDok = getFormatBroj();
      naps = "";
      allNaps.clear();
      dineto = diprodbp = diprodsp = Aus.zero2;
    }
  	setParams();
    cache.clear();
    //dm.getVTText().refresh();    
  }
  
  public static boolean isReportValute() {

    String descriptor = hr.restart.util.reports.dlgRunReport
            .getCurrentDlgRunReport().getCurrentDescriptor().getName();

    return (descriptor.equals("hr.restart.robno.repPredracuniV")
            || descriptor.equals("hr.restart.robno.repRacuniV")
            || descriptor.equals("hr.restart.robno.repPredracuni2V")
            || descriptor.equals("hr.restart.robno.repRacuni2V")
            || descriptor.equals("hr.restart.robno.repRac2V")
            || descriptor.equals("hr.restart.robno.repRacV")
            || descriptor.equals("hr.restart.robno.repPonudaV")
            || descriptor.equals("hr.restart.robno.repPonuda2V") 
            || descriptor.equals("hr.restart.robno.repNarDobV")
            || descriptor.equals("hr.restart.robno.repOdobrenjaV")
            || descriptor.equals("hr.restart.robno.repTerecenjaV")
            || descriptor.equals("hr.restart.robno.repOdobrenjaPV")
            || descriptor.equals("hr.restart.robno.repInvoice")
            || descriptor.equals("hr.restart.robno.repOffer")
            || descriptor.equals("hr.restart.robno.repProformaInvoice")
            || descriptor.equals("hr.restart.robno.repPovratnicaOdobrenjeV")
            || descriptor.equals("hr.restart.robno.repInvoiceNew")
            );

  }
  
  public static boolean isReportForeign() {
    return hr.restart.util.reports.dlgRunReport
        .getCurrentDlgRunReport().getCurrentDescriptor().isForeignIzlaz();
  }
  
  protected void setCurrentDataset(){
    if (isReportValute())
      ds = reportsQuerysCollector.getRQCModule().getValuteQueryDataSet();
    else ds = reportsQuerysCollector.getRQCModule().getQueryDataSet();
    ds.setSort(new SortDescriptor(new String[] {"BRDOK", "RBR"}));
    if (isReportValute()) {
      orig = reportsQuerysCollector.getRQCModule().getQueryDataSet();
      orig.setSort(new SortDescriptor(new String[] {"BRDOK", "RBR"}));
    } else orig = null;
  }

  long tim;
  
  public raReportData getRow(int i) {
    ds.goToRow(i);
    if (orig != null) orig.goToRow(i);
    String nowDok = getFormatBroj();
    if (lastDok != null && !nowDok.equals(lastDok)) {
      lastDok = nowDok;
      naps = "";
      modParams();
      rekapPorez();
      nacPl();
      setZnacajkeSubjekta();
      dokChanged();
      dineto = diprodbp = diprodsp = Aus.zero2;
    }
    dineto = dineto.add(ds.getBigDecimal("INETO"));
    diprodbp = diprodbp.add(ds.getBigDecimal("IPRODBP"));
    diprodsp = diprodsp.add(ds.getBigDecimal("IPRODSP"));
    checkNap();
    return this;
  }
  
  protected void dokChanged() {
    // za override
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    ds = null;
  }
  
  public int getCPAR() {
    return ds.getInt("CPAR");
  }
  
  public String getLOT() {
    return ds.getString("LOT");
  }

  public String getPARTNER(){   
    String cached = cache.getValue("PARTNER", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    colname[0] = "CPAR";
    String np = ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString();

    return cache.returnValue(np);
//    String cached = cache.getValue("PARTNER", Integer.toString(ds.getInt("CPAR")));
//    if (cached != null) return cached; 
//    return cache.returnValue(ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString());
  }

  public String getMJPAR(){    
    String cached = cache.getValue("MJPAR", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    String mj = "";
    lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", ds.getInt("CPAR")+"");
    if(dm.getPartneri().getInt("PBR")>0) mj = dm.getPartneri().getInt("PBR")+" "+dm.getPartneri().getString("MJ");
    else mj = dm.getPartneri().getString("MJ");
    return cache.returnValue(mj);
  }

  public String getADRPAR(){    
    String cached = cache.getValue("ADRPAR", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    String adr = "";
    if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+""))
      adr = dm.getPartneri().getString("ADR");
    return cache.returnValue(adr);
  }
  
  public String getTEL(){
//    String cached1 = cache.getValue("TEL", Integer.toString(ds.getInt("CPAR")));
//    String cached2 = cache.getValue("TELFAX", Integer.toString(ds.getInt("CPAR")));
    String tf = "";
    String fx = "";    
//    if (cached1 != null && cached2 != null) {
//      if (!cached1.equals("")) tf = "Tel "+cached1;
//      if (!cached2.equals("")) tf = "Fax "+cached2;
//      System.out.println("u ifu " +tf + " "+ fx);
//      return tf + " "+ fx;
//    }
//    
    
    colname[0] = "CPAR";
    String t = ru.getSomething(colname,dm.getPartneri(),"TEL").getString();
    String f = ru.getSomething(colname,dm.getPartneri(),"TELFAX").getString();

    
    if (ispisPJ.equalsIgnoreCase("D") || ispisPJ.equalsIgnoreCase("O")) {
      if (ds.getInt("PJ") > 0){
        lookupData.getlookupData().raLocate(dm.getPjpar(),
            new String[] {"CPAR","PJ"},
            new String[] {ds.getInt("CPAR")+"",ds.getInt("PJ")+""}
        );
        t = dm.getPjpar().getString("TELPJ");
        f = dm.getPjpar().getString("TELFAXPJ");
      }
    }

    if (!t.equals("")) tf = "Tel "+t;
    if (!f.equals("")) fx = "Fax "+f;
    String tm = "";
    if (vanizkuce.equals("X")) {
      String mb = getMB();
      if (mb.equals("")) {
        tm = "";
      } else {
        tm = "  "+mb+" ("+getCPAR()+")";
      }
    }
//    System.out.println("van ifa " +tf + " "+ fx);
    return tf + "   " + fx + tm;
//      return cached;
//    return cache.returnValue("Tel "+ru.getSomething(colname,dm.getPartneri(),"TEL").getString());
  }

  public String getFAX(){
    String cached = cache.getValue("TELFAX", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    colname[0] = "CPAR";
    return cache.returnValue("Fax "+ru.getSomething(colname,dm.getPartneri(),"TELFAX").getString());
  }
  
  public String getNAZPARL() {
    return "\n" + prefn + getNAZPAR();
  }

  public String getNAZPAR() {    
    String cached = cache.getValue("NAZPAR", ds.getInt("CPAR")+"-"+ds.getInt("PJ"));
    if (cached != null) return cached;
    colname[0] = "CPAR";
    String np = ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString();
    if (ispisPJ.equalsIgnoreCase("D") || ispisPJ.equalsIgnoreCase("O")) {
      if (ds.getInt("PJ") > 0){
        lookupData.getlookupData().raLocate(dm.getPjpar(),
            new String[] {"CPAR","PJ"},
            new String[] {ds.getInt("CPAR")+"",ds.getInt("PJ")+""});
        np += "\n"+dm.getPjpar().getString("NAZPJ");
      }
    } else if (!conVl.equalsIgnoreCase("N")) {
      
      if (ru.getSomething(colname,dm.getPartneri(),"MB").getString().length() == 13) {
        String vlasnik = ru.getSomething(colname,dm.getPartneri(),"KO").getString();
        String prefix = " ";
        if (conVl.equalsIgnoreCase("V")) {
          prefix = " vl. ";
        }
        
        np += prefix+vlasnik;
      }
    }
    return cache.returnValue(np);
  }
  
  public String getPISP() {
    return getISPORUKA();
  }

  public String getISPORUKA(){    
    String cached = cache.getValue("ISPORUKA", ds.getInt("CPAR") + "-" + ds.getInt("PJ"));
    if (cached != null) return cached;
    if (ispisPJ.equalsIgnoreCase("I") || ispisPJ.equalsIgnoreCase("O")) {
      if (ds.getInt("PJ") > 0){
        lookupData.getlookupData().raLocate(dm.getPjpar(),
            new String[] {"CPAR","PJ"},
            new String[] {ds.getInt("CPAR")+"",ds.getInt("PJ")+""});
        if (dm.getPjpar().getInt("PBRPJ") >0) 
          return cache.returnValue(dm.getPjpar().getString("NAZPJ")+"\n"+dm.getPjpar().getString("ADRPJ") + "\n" + dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ"));
        return cache.returnValue(dm.getPjpar().getString("NAZPJ")+"\n"+dm.getPjpar().getString("ADRPJ"));
      } //else return "              /";
    }
    return cache.returnValue("");
  }

/*  public String getNAZPARPJ() {    
    String cached = cache.getValue("NAZPARPJ", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    colname[0] = "CPAR";
    String np = ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString();
    return cache.returnValue(np);
  }*/

  public String getNAZPJ() {
    String cached = cache.getValue("NAZPJ", ds.getInt("CPAR") + "-" + ds.getInt("PJ"));
    if (cached != null) return cached;
    String np = "";
    if (ds.getInt("PJ") > 0){
      lookupData.getlookupData().raLocate(dm.getPjpar(),
          new String[] {"CPAR","PJ"},
          new String[] {ds.getInt("CPAR")+"",ds.getInt("PJ")+""});
      np = dm.getPjpar().getString("NAZPJ");
    }
    return cache.returnValue(np);
  }

  public String getMJ() {
    String cached = cache.getValue("MJ", ds.getInt("CPAR") + "-" + ds.getInt("PJ"));    
    if (cached != null) return cached;
    colname[0] = "CPAR";
    String mj = "";
    if (ds.getInt("PJ") > 0 && (ispisPJ.equalsIgnoreCase("D") || ispisPJ.equalsIgnoreCase("O"))){
      lookupData.getlookupData().raLocate(dm.getPjpar(),
          new String[] {"CPAR","PJ"},
          new String[] {ds.getInt("CPAR")+"",ds.getInt("PJ")+""});
      if (dm.getPjpar().getInt("PBRPJ") >0)  mj = dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ");
      else mj = dm.getPjpar().getString("MJPJ");
    } else {
      if (!lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+"")) return "";
      if (dm.getPartneri().getInt("PBR") >0)  mj = dm.getPartneri().getInt("PBR") + " " + dm.getPartneri().getString("MJ");
      else mj = dm.getPartneri().getString("MJ");
    }    
    return cache.returnValue(mj);
  }

  public String getMJPJ(){
    String cached = cache.getValue("MJPJ", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    String mj = "";
    lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+"");
    if (dm.getPartneri().getInt("PBR") >0)  mj = dm.getPartneri().getInt("PBR") + " " + dm.getPartneri().getString("MJ");
    else mj = dm.getPartneri().getString("MJ");
    return cache.returnValue(mj);
  }

  public String getADR() {
    String cached = cache.getValue("ADR", ds.getInt("CPAR") + "-" + ds.getInt("PJ"));
    if (cached != null) return cached;
    String adr = "";
    if (ds.getInt("PJ") > 0 && (ispisPJ.equalsIgnoreCase("D") || ispisPJ.equalsIgnoreCase("O"))){
      lookupData.getlookupData().raLocate(dm.getPjpar(),
          new String[] {"CPAR","PJ"},
          new String[] {ds.getInt("CPAR")+"",ds.getInt("PJ")+""});
      adr += dm.getPjpar().getString("ADRPJ");
    } else {
      if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+""))
        adr = dm.getPartneri().getString("ADR");
      else
        adr = "";
    }
    return cache.returnValue(adr);
  }

  public String getADRPJ(){
    String cached = cache.getValue("ADRPJ", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    String adr = "";
    if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+""))
      adr = dm.getPartneri().getString("ADR");
    else
      adr = "";
    return cache.returnValue(adr);
  }

  public String getKONTOSOB() {
    String cached = cache.getValue("KONTOSOB", ds.getInt("CPAR") + "-" + ds.getInt("CKO"));
    if (cached != null) return cached;
    String koso = "";
    String telKosobe = "";
    if (ds.getInt("CKO") > 0){
      if (lookupData.getlookupData().raLocate(dm.getKosobe(),new String[]{"CPAR","CKO"},
          new String[]{String.valueOf(ds.getInt("CPAR")),String.valueOf(ds.getInt("CKO"))})){
            if (!dm.getKosobe().getString("TEL").equals("")) telKosobe = "\n" + dm.getKosobe().getString("TEL");
        koso = "n/r " + dm.getKosobe().getString("IME") + telKosobe;
      }
   }
    return cache.returnValue(koso);
  }

  public int getPBR() {
    colname[0] = "CPAR";
    return ru.getSomething(colname,dm.getPartneri(),"PBR").getAsInt();
  }
  
  public String getCDJEL() {
    String cached = cache.getValue("CDJEL", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    colname[0] = "CPAR";
    return cache.returnValue(ru.getSomething(colname,dm.getPartneri(),"CDJEL").getString());
  }

  public String getMB() {
    String cached = cache.getValue("MB", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    String result = "";
    if (!oib.equalsIgnoreCase("MB")) {
      colname[0] = "CPAR";
      String br = ru.getSomething(colname,dm.getPartneri(),"OIB").toString();
      if (br.length() == 0) result = "";
      else result = "OIB " + br;
    } 
    if (oib.equalsIgnoreCase("MB") || result.length() == 0) {
      colname[0] = "CPAR";
      String mb = ru.getSomething(colname,dm.getPartneri(),"MB").toString();
      if (mb.length() == 0) result = "";
      else result = "MB " + mb; 
    }    
    return cache.returnValue(result);
  }

  public String getZR() {
    String cached = cache.getValue("ZR", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    colname[0] = "CPAR";
    return cache.returnValue(ru.getSomething(colname,dm.getPartneri(),"ZR").getString());
  }

  public String getZIRORAC() {
    return ds.getString("ZIRO");
  }

  public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getVRDOK() {
    return ds.getString("VRDOK");
  }

  public String getGOD() {
    return ds.getString("GOD");
  }

//  public String getBRDOK() {
//    return ds.getInt("BRDOK")+ds.getString("CSKL")+ds.getString("GOD");
//  }

  public int getBRDOK() {
    return ds.getInt("BRDOK");
  }


  public String getUI() {
    return ds.getString("UI");
  }

  public Timestamp getSYSDAT() {
    return ds.getTimestamp("SYSDAT");
  }

  public String SgetSYSDAT() {
    return rdu.dataFormatter(getSYSDAT());
  }
  
  public String SgetSYSTIME() {
    return getSYSDAT().toString().substring(11,19);
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }

  public Timestamp getDATDOK() {
    return ds.getTimestamp("DATDOK");
  }
  public String SgetDATDOK() {
    return rdu.dataFormatter(getDATDOK());
  }
  
  public String SgetDATTIME() {
    return getDATDOK().toString().substring(11,19);
  }

  public int getPJ() {
    return ds.getInt("PJ");
  }

  public String getCORG() {
    return ds.getString("CORG");
  }

  public String getNAZORG(){
    String cached = cache.getValue("NAZORG", ds.getString("CORG"));
    if (cached != null) return cached;
    colname[0] = "CORG";
    String ret = cache.returnValue(ru.getSomething(colname,dm.getOrgstruktura(),"NAZIV").getString());
    if (ds.getString("VRDOK").equals("TRE")) {//ai hack za trebovanja
      return ret+" "+ds.getString("OPIS");
    } 
    return ret;
//    return "EEEEEEEEE";
  }

  public String getCVRTR() {
    return ds.getString("CVRTR");
  }

  public String getNAZVRTR() {
    String cached = cache.getValue("NAZVRTR", ds.getString("CVRTR"));
    if (cached != null) return cached;
    if (lookupData.getlookupData().raLocate(dm.getVrtros(),"CVRTR",ds.getString("CVRTR")))
      return cache.returnValue(dm.getVrtros().getString("NAZIV"));
    return cache.returnValue("");
  }

  public String getCUG() {
    return ds.getString("CUG");
  }

  public Timestamp getDATUG() {
    return ds.getTimestamp("DATUG");
  }
  public String SgetDATUG() {
    return rdu.dataFormatter(getDATUG());
  }

  public Timestamp getDVO() {
    return ds.getTimestamp("DVO");
  }

  public String SgetDVO() {
    return rdu.dataFormatter(getDVO());
  }

  public short getDDOSP() {
    return ds.getShort("DDOSP");
  }

  public String SgetDDOSP() {
    if (isReportForeign()) return (getDDOSP() == 0 ? "" : getDDOSP() +
        (getDDOSP() == 1 ? " day" : " days"));
    return (getDDOSP() == 0 ? "" : getDDOSP() +
            (getDDOSP() % 10 == 1 && getDDOSP() % 100 != 11 ? " dan" : " dana"));
  }

  public Timestamp getDATDOSP() {
    return ds.getTimestamp("DATDOSP");
  }

  public String SgetDATDOSP() {
    return rdu.dataFormatter(getDATDOSP());
  }

  public String getBRDOKIZ() {
    return ds.getString("BRDOKIZ");
  }

  public Timestamp getDATDOKIZ() {
    return ds.getTimestamp("DATDOKIZ");
  }

  public String SgetDATDOKIZ() {
    return rdu.dataFormatter(getDATDOKIZ());
  }

  public String getBRPRD() {
    return ds.getString("BRPRD");
  }

  public Timestamp getDATPRD() {
    return ds.getTimestamp("DATPRD");
  }

  public String SgetDATPRD() {
    return rdu.dataFormatter(getDATPRD());
  }

  public String getBRNARIZ() {
    return ds.getString("BRNARIZ");
  }

  public Timestamp getDATNARIZ() {
    return ds.getTimestamp("DATNARIZ");
  }

  public String SgetDATNARIZ() {
    return rdu.dataFormatter(getDATNARIZ());
  }

  public String getOZNVAL() {
    return ds.getString("OZNVAL");
  }

  public BigDecimal getTECAJ() {
    return ds.getBigDecimal("TECAJ");
  }
  
  public String getTECTRI() {
    return Aus.formatBigDecimal(ds.getBigDecimal("TECAJ")
        .setScale(3, BigDecimal.ROUND_HALF_UP));
  }

  public String getBRNAL() {
    return ds.getString("BRNAL");
  }

  public Timestamp getDATKNJ() {
    return ds.getTimestamp("DATKNJ");
  }

  public String SgetDATKNJ() {
    return rdu.dataFormatter(getDATKNJ());
  }

  public String getCRADNAL() {
    return ds.getString("CRADNAL");
  }

  public String getUSER(){
    if(lookupData.getlookupData().raLocate(dm.getUseri(), "CUSER", ds.getString("CUSER"))){
      return dm.getUseri().getString("NAZIV");
    } else{
      return "";
    }
  }
  
  public String getCCUSER(){
    try {
      if(lookupData.getlookupData().raLocate(dm.getUseri(), "CUSER", ds.getString("CUSER"))){
        return dm.getUseri().getString("CCERT");
      } else{
        return "";
      }
    } catch (RuntimeException e) {
      return "";
    }
  }

  public String getAGENT(){
    if(lookupData.getlookupData().raLocate(dm.getAgenti(), "CAGENT", ds.getInt("CAGENT")+"")){
      return dm.getAgenti().getString("NAZAGENT");
    } else{
      return "";
    }
  }

  public Timestamp getDATRADNAL() {
    return ds.getTimestamp("DATRADNAL");
  }

  public String SgetDATRADNAL() {
    return rdu.dataFormatter(ds.getTimestamp("DATRADNAL"));
  }

  public String getSTATUS1() {
    return ds.getString("STATUS1");
  }

  public String getSTATKNJ() {
    return ds.getString("STATKNJ");
  }

  public String getSTATPLA() {
    return ds.getString("STATPLA");
  }

  public String getSTATIRA() {
    return ds.getString("STATIRA");
  }

  public String getCFRA() {
    return ds.getString("CFRA");
  }

  public String getCNACPL() {
    return ds.getString("CNACPL");
  }

  public String getCNAMJ() {
    return ds.getString("CNAMJ");
  }

  public String getCNAC() {
    return ds.getString("CNAC");
  }

  public String getCNAP() {
    return ds.getString("CNAP");
  }

  public BigDecimal getUPZT() {
    return ds.getBigDecimal("UPZT");
  }

  public String getCSHZT() {
    return ds.getString("CSHZT");
  }

  public BigDecimal getUPRAB() {
    return ds.getBigDecimal("UPRAB");
  }

  public String getCSHRAB() {
    return ds.getString("CSHRAB");
  }

  public String getOPIS() {
    return ds.getString("OPIS");
  }

  public String getNAPOMENAOPIS(){
    String nazOpis = getNAZNAP() + "\n" + getOPIS();
    return nazOpis;
  }
  
  public String getLABDOD() {
    if (getTEXTDOD().length() == 0) return "";
    return labdod;
  }
  
  public String getTEXTDOD() {
    String cached = cache.getValue("TEXTDOD", ru.getFormatBroj());
    if (cached != null) return cached;
    
    DataSet ds = dokidod.getDataModule().getTempSet(
        Condition.equal("BRRAC", ru.getFormatBroj()));
    ds.open();
    if (ds.rowCount() == 0) return cache.returnValue("");
    
    ds.setSort(new SortDescriptor(new String[] {"RBS"}));
    VarStr buf = new VarStr();
    for (ds.first(); ds.inBounds(); ds.next())
      buf.append(ds.getString("VAL")).append('\n');
    return cache.returnValue(buf.chop().trim().toString());
  }

  public short getRBR() {
    return ds.getShort("RBR");
  }

  public String getARTIKL(){
//    return Aut.getAut().getCARTdependable(ds);
    return Aut.getAut().getIzlazCARTdep(ds);
  }

  public String getEANCODE() {
    return ds.getString("BC");
  }
  
  public String getCART() {
//    return Aut.getAut().getCARTdependable(ds);
//    return Aut.getAut().getIzlazCARTdep(ds);
//	 return "" + ds.getInt("CART");
    
    String izlaz = Aut.getAut().getIzlazCARTdep(ds);
    if (specChars){
      if (Aut.getAut().getIzlazCART().equalsIgnoreCase("BC") && (izlaz.indexOf("*") > 0 || izlaz.indexOf("_") > 0 || izlaz.indexOf("+") > 0 || izlaz.indexOf("-") > 0)){
        
        int index = 13;
        
        if (izlaz.indexOf("*") > 0) index = izlaz.indexOf("*");
        if (izlaz.indexOf("_") > 0) index = izlaz.indexOf("_");
        if (izlaz.indexOf("+") > 0) index = izlaz.indexOf("+");
        if (izlaz.indexOf("-") > 0) index = izlaz.indexOf("-");
        
        izlaz = izlaz.substring(0,index);
      }
    }

    return izlaz;
  }

  public String getCART1() {
    return ds.getString("CART1");
  }

  public String getBC() {
    return ds.getString("BC");
  }
  
  public String getNAZART() {
    if (!hr.restart.util.reports.dlgRunReport
        .getCurrentDlgRunReport().getCurrentDescriptor().isForeignIzlaz())
      return ds.getString("NAZART");
    
    if (!lD.raLocate(dm.getArtikli(), "CART1", getCART1())) 
      return ds.getString("NAZART");
    
    if (dm.getArtikli().getString("NAZLANG").length() == 0)
      return ds.getString("NAZART");
    
    return dm.getArtikli().getString("NAZLANG");
  }

  public String getNAZARText() {
    String ss = rCD.getKey(ds,new String[]{"CSKL","VRDOK","GOD","BRDOK","rbsid"},"stdoki");
//System.out.println(ss);
//    return ru.getSomething(new String[] {},dm.getOrgstruktura(),"NAZIV").getString();    
    if (lD.raLocate(dm.getVTText(),"CKEY",ss))
      return dm.getVTText().getString("TEXTFAK");

    return null;
  }

  public String getJM() {
    return ds.getString("JM");
  }

  public BigDecimal getKOL() {
    return ds.getBigDecimal("KOL");
  }

  public BigDecimal getKOL1() {
    return ds.getBigDecimal("KOL1");
  }

  public BigDecimal getUPRAB1() {
    return ds.getBigDecimal("UPRAB1");
  }

  public double getUIRAB() {
    return ds.getBigDecimal("UIRAB").doubleValue();
  }

  public double getTRUERAB() {
    return (ds.getBigDecimal("ISP").multiply(ds.getBigDecimal("UPRAB1")).divide(new BigDecimal(100.0000),4,BigDecimal.ROUND_HALF_UP)).doubleValue();
//    return (ds.getBigDecimal("UIRAC").subtract(ds.getBigDecimal(""))).doubleValue();
  }

  public BigDecimal getUPZT1() {
    return ds.getBigDecimal("UPZT1");
  }

  public BigDecimal getUIZT() {
    return ds.getBigDecimal("UIZT");
  }

  public BigDecimal getFC() {
    return ds.getBigDecimal("FC");
  }

  public double getINETO() {
    return ds.getBigDecimal("INETO").doubleValue();
  }
  
  public double getINETOV() {
    return orig == null ? getINETO() : orig.getBigDecimal("INETO").doubleValue();
  }
  
  public double getIPRODBPV() {
    return orig == null ? getIPRODBP() : orig.getBigDecimal("IPRODBP").doubleValue();
  }
  
  public double getUIRABV() {
    return orig == null ? getUIRAB() : orig.getBigDecimal("UIRAB").doubleValue();
  }
  
  public double getPOR1V() {
    return orig == null ? getPOR1() : orig.getBigDecimal("POR1").doubleValue();
  }
  
  public double getPOR2V() {
    return orig == null ? getPOR2() : orig.getBigDecimal("POR2").doubleValue();
  }
  
  public double getPOR3V() {
    return orig == null ? getPOR3() : orig.getBigDecimal("POR3").doubleValue();
  }
  
  public double getUIUV() {
    return orig == null ? getUIU() : orig.getBigDecimal("UIU").doubleValue();
  }
  
  public String getDOMNAZ() {
    return hr.restart.zapod.Tecajevi.getDomOZNVAL();
  }
  
  public String getVALNAZ() {
    return getOZNVAL();
  }
  
  /*public BigDecimal getIPRODBPV() {
    if (getTECAJ().signum() == 0) 
      return ds.getBigDecimal("IPRODBP");
    return ds.getBigDecimal("IPRODBP").
      multiply(raSaldaKonti.getJedVal(getOZNVAL())).
      divide(getTECAJ(), 2, BigDecimal.ROUND_HALF_UP);
  }*/
  
  public double getINETOP() {
//System.out.println("ds.getBigDecimal(INETO)        "+ds.getBigDecimal("INETO"));
//System.out.println("ds.getBigDecimal(UPRAB1)       "+ds.getBigDecimal("UPRAB1"));
//System.out.println("ds.getBigDecimal(UPRAB1)divide "+ds.getBigDecimal("UPRAB1").divide(new BigDecimal("100.00"),BigDecimal.ROUND_HALF_UP));
//System.out.println("ds.getBigDecimal(INETO) s p 1  "+ds.getBigDecimal("INETO").multiply(new BigDecimal("1.00").subtract(ds.getBigDecimal("UPRAB1").divide(new BigDecimal("100.00"),BigDecimal.ROUND_HALF_UP))));
//System.out.println("što je ovo -                   " + (ds.getBigDecimal("ISP").multiply(ds.getBigDecimal("UPRAB1")).divide(new BigDecimal(100.0000),4,BigDecimal.ROUND_HALF_UP)).doubleValue()); //XDEBUG delete when no more needed
    if (iznosPop) return ds.getBigDecimal("INETO").multiply(Aus.one0.subtract(
          ds.getBigDecimal("UPRAB1").movePointLeft(2))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    return ds.getBigDecimal("INETO").doubleValue();
  }

  public BigDecimal getFVC() {
    return ds.getBigDecimal("FVC");
  }
/*
public BigDecimal getIPRODBP() {
 return ds.getBigDecimal("IPRODBP");
}

public double getPOR1() {
  DataSet anotherOne = repQC.getPoreziSet(ds.getString("CSKL"), ds.getString("VRDOK"), ds.getString("GOD"), ds.getInt("BRDOK"));
  System.out.println("anotherOne : " + anotherOne.toString());
 return ds.getBigDecimal("POR1").doubleValue();
}

public BigDecimal getPOR2() {
 return ds.getBigDecimal("POR2");
}

public BigDecimal getPOR3() {
 return ds.getBigDecimal("POR3");
}
*/
  public double getIPRODBP() {
    return ds.getBigDecimal("IPRODBP").doubleValue();
  }

  public double getPOR1() {
    return ds.getBigDecimal("POR1").doubleValue();
  }

  public double getPOR2() {
    return ds.getBigDecimal("POR2").doubleValue();
  }

  public double getPOR3() {
    return ds.getBigDecimal("POR3").doubleValue();
  }


  public double getUKPOR3() {
    return getPOR1()+getPOR2()+getPOR3();
  }
  
  public double getUIU() {
    return ds.getBigDecimal("UIU").doubleValue();
  }
  
  public Boolean getUIUprint() {
    return new Boolean(getUIU() > 0);
  }
  
  public String getUIUlabel() {
    return "Plaæeno/uèešæe";
  }
  
  public String getUIUlabel2() {
    return "Za platiti";
  }

  public BigDecimal getFMC() {
    return ds.getBigDecimal("FMC");
  }
/*
  public BigDecimal getFMCPRP() {
    return ds.getBigDecimal("FMCPRP");
  }
*/
  public double getFMCPRP() {
    return ds.getBigDecimal("FMCPRP").doubleValue();
  }

  public double getIZNFMCPRP() {
    return ds.getBigDecimal("FMCPRP").multiply(ds.getBigDecimal("KOL")).doubleValue();
  }


/*
public BigDecimal getIPRODSP() {
 return ds.getBigDecimal("IPRODSP");
}
*/
  public double getIPRODSP() {
//System.out.println("ds.getBigDecimal(IPRODSP)"+ds.getBigDecimal("IPRODSP"));
    return ds.getBigDecimal("IPRODSP").doubleValue();
  }

  public BigDecimal getNC() {
    return ds.getBigDecimal("NC");
  }

  public BigDecimal getINAB() {
    return ds.getBigDecimal("INAB");
  }

  public double getINABreal() {
    return ds.getBigDecimal("INAB").doubleValue();
  }

  public BigDecimal getIMAR() {
    return ds.getBigDecimal("IMAR");
  }

  public BigDecimal getVC() {
    return ds.getBigDecimal("VC");
  }

  public BigDecimal getIBP() {
    return ds.getBigDecimal("IBP");
  }

  public BigDecimal getIPOR() {
    return ds.getBigDecimal("IPOR");
  }

  public BigDecimal getMC() {
    return ds.getBigDecimal("MC");
  }
/*
  public BigDecimal getISP() {
    return ds.getBigDecimal("ISP");
  }
*/
  public double getISP() {
    return ds.getBigDecimal("ISP").doubleValue();
  }

  public BigDecimal getZC() {
    return ds.getBigDecimal("ZC");
  }

  public double getIRAZ() {
    return ds.getBigDecimal("IRAZ").doubleValue();
  }

  public String getBRPRI() {
    return ds.getString("BRPRI");
  }

  public short getRBRPRI() {
    return ds.getShort("RBRPRI");
  }

  public String getNAZNAP() {
    colname[0] = "CNAP";
    return ru.getSomething(colname,dm.getNapomene(),"NAZNAP").getString();
  }

  public String getNAZNACPL() {
    colname[0] = "CNACPL";
    return ru.getSomething(colname,dm.getNacpl(),"NAZNACPL").getString();
  }

  public String getNAZNAC() {
    colname[0] = "CNAC";
    return ru.getSomething(colname,dm.getNacotp(),"NAZNAC").getString();
  }

  public String getNAZFRA() {
    colname[0] = "CFRA";
    return ru.getSomething(colname,dm.getFranka(),"NAZFRA").getString();
  }
  public String getFormatBroj(){
    if (ds.getString("FOK").equals("D"))
      return Aus.formatBroj(ds, fiskForm);
    
    return getOldFormatBroj();
  }
  
  public String getFISKRED() {
    System.out.println("getFISKRED");
    if (!ds.getString("FOK").equals("D")) return "";
    
    String user = getCCUSER();
    if (user == null || user.length() == 0) user = getUSER();
       
    String first = "Datum i vrijeme izrade: " + SgetSYSDAT() + "  u " + SgetSYSTIME() +
        "       Operater: " + user + "        Interni broj: " + getOldFormatBroj();
    
    if (hr.restart.util.reports.dlgRunReport
        .getCurrentDlgRunReport().getCurrentDescriptor().isForeignIzlaz())
      first = "Date and time of creation: " + SgetSYSDAT() + "  at " + SgetSYSTIME() +
      "       Operator ID: " + user + "        Internal number: " + getOldFormatBroj();
        
    System.out.println(first);
    if ("GOT|GRN|PRD".indexOf(getVRDOK()) < 0 ) return first;
    
    if (getVRDOK().equals("PRD") && (ds.hasColumn("PARAM") == null || !ds.getString("PARAM").equals("K"))) return first;
    
    return first + "\nZKI: " + getZKI() + "    JIR: " + getJIR();
  }
  
  public int getFBR() {
    return ds.getInt("FBR");
  }
  
  public int getFNU() {
    return ds.getInt("FNU");
  }
  
  public String getOldFormatBroj() {
    if (specForm == null || specForm.length() == 0)
      return ru.getFormatBroj();
    if (specForm.equalsIgnoreCase("pnbz2")) {
      if (ds.getString("PNBZ2").trim().length()>0)
        return ds.getString("PNBZ2");
      return ru.getFormatBroj();
    }
    return Aus.formatBroj(ds, specForm);
  }
  
  public String getZKI() {
    try {
      return presBlag.getFis(ds).generateZKI(raIzlazTemplate.getRacType(ds));
    } catch (Exception e) {
      return "";
    }
  }
    
  public String getFormatBrojTri(){
    return Valid.getValid().maskZeroInteger(new Integer(getBRDOK()),6) + 
            "/" + getGOD().substring(2);
  }

  public String getPerInvNo() {
    return ru.getFormatPerformaInvoice();
  }

  public String getPorezPos() {
    colname[0]="CART";
    return ru.getSomething(colname,dm.getArtikli(),"CPOR").getString();
  }

  public String getPor1Naz(){
    getPorezPos();
    colname[0] ="CPOR";
    return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"nazpor1").getString();
  }
  public int getImaPor1(){
    return (getPor1Naz().equals(""))?0:1;
  }
  public String getPor2Naz(){
    getPorezPos();
    colname[0] ="CPOR";
    return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"nazpor2").getString();
  }
  public int getImaPor2(){
    return (getPor2Naz().equals(""))?0:1;
  }
  public String getPor3Naz(){
    getPorezPos();
    colname[0] ="CPOR";
    return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"nazpor3").getString();
  }
  public int getImaPor3(){
    return (getPor3Naz().equals(""))?0:1;
  }
  public BigDecimal getPOSPor1(){
 /*ru.setDataSet(ds);
 getPorezPos();
 colname[0] ="CPOR";
 return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"por1").getBigDecimal();
 */
    return ds.getBigDecimal("PPOR1");
  }
  public BigDecimal getPOSPor3(){
 /*ru.setDataSet(ds);
 getPorezPos();
 colname[0] ="CPOR";
 return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"por3").getBigDecimal();
 */
    return ds.getBigDecimal("PPOR3");
  }
  public BigDecimal getPOSPor2(){
 /*ru.setDataSet(ds);
 getPorezPos();
 colname[0] ="CPOR";
 return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"por2").getBigDecimal();
 */
    return ds.getBigDecimal("PPOR2");
  }

  public BigDecimal getPor1p2p3Naz(){
  /*
 ru.setDataSet(ds);
 getPorezPos();
 colname[0] ="CPOR";
 return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"ukupor").getBigDecimal();
 */
    return ds.getBigDecimal("PPOR1").add(ds.getBigDecimal("PPOR2")).add(ds.getBigDecimal("PPOR3"));
  }
  public String getSLOVIMA() { /** @todo razlika kod deviznog */
//    if (ds.getString("OZNVAL").equals(""))
    if (frmParam.getParam("robno", "slovimaVAL", "N", "Ispisati slovima sa valutom na raèunu usprkos svemu").equalsIgnoreCase("D")) {
      if (!ds.getString("OZNVAL").equals(""))
      return ut.numToLet(ds.getBigDecimal("UIRAC").doubleValue(),ds.getString("OZNVAL"));
    }
    return ut.numToLet(ds.getBigDecimal("UIRAC").doubleValue(),
                       isReportValute() ? ds.getString("OZNVAL") : null);
  }
  public int getLogoPotvrda() {
    if (rm.test()) return 1;
    return 0;
  }
  public String getLogoFullAdr() {
    String cached = cache.getValue("LOGOFULLADR", "DUMMY");
    if (cached != null) return cached;
    if (!rm.test()) return "";
    else return cache.returnValue(rm.getLogoAdresa() + " ,  " + rm.getLogoPbr() + " " + rm.getLogoMjesto());
  }
//  public String getLineName() {
//    return ClassLoader.getSystemResource("hr/restart/robno/reports/line2.jpg").toString();
//  }
  public String getLogoCorg(){return rm.getLogoCorg();}
  public String getLogoNazivlog(){return rm.getLogoNazivlog();}
  public String getLogoMjesto(){  return rm.getLogoMjesto();}

  public String getLogoMjestoZarez(){
    String cached = cache.getValue("MJESTOZAREZ", ds.getString("CSKL"));
    if (cached != null) return cached;
//    System.out.println("ds.getstringCSKL = " + ds.getString("CSKL")); //XDEBUG delete when no more needed
    String corg = ds.getString("CSKL");
    
    if (TypeDoc.getTypeDoc().isDocSklad(ds.getString("VRDOK")) || 
        (ds.getString("VRDOK").equals("PON") && !ds.getString("PARAM").startsWith("OJ")) ||
        (ds.getString("VRDOK").equals("PRD") && ds.getString("PARAM").startsWith("K"))) {
        lookupData.getlookupData().raLocate(dm.getSklad(),new String[] {"CSKL"}, new String[] {ds.getString("CSKL")});
        corg = dm.getSklad().getString("CORG");
    }
    
//    System.out.println("* CORG ---> " + corg);
    lookupData.getlookupData().raLocate(dm.getOrgstruktura(),new String[] {"CORG"}, new String[] {corg});
//    System.out.println("dm.getOrgstruktura().getString(\"MJESTO\") " + dm.getOrgstruktura().getString("MJESTO"));
    String forreturn = dm.getOrgstruktura().getString("MJESTO");
    if (!forreturn.equalsIgnoreCase("")) forreturn = forreturn +",";
    return cache.returnValue(forreturn);
  }
  
  public String getGETRODOB() {
    return "";
  }
  
  public String getLABGETRODOB() {
    return "";
  }
  
  public String getLogoPbr(){return rm.getLogoPbr();}
  public String getLogoAdresa(){return rm.getLogoAdresa();}
  public String getLogoZiro(){return   rm.getLogoZiro();}
  public String getLogoMatbroj(){return rm.getLogoMatbroj();}
  public String getLogoSifdjel(){return rm.getLogoSifdjel();}
  public String getLogoPorisp(){return rm.getLogoPorisp();}
  public String getLogoFax(){return rm.getLogoFax();}
  public String getLogoTel1(){return rm.getLogoTel1();}
  public String getLogoTel2(){return rm.getLogoTel2();}

  static VarStr CPOR;
  static VarStr UKUPOR;
  static VarStr IPRODBP;
  static VarStr POR1;
  static VarStr separator;
  
  static BigDecimal DUIRAB;
  static BigDecimal DUIZT;
  static BigDecimal DPOR1;
  static BigDecimal DIPRODSP;
  static BigDecimal DPOSTO;
  
//  static DataSet qdsIzd_porez = null;
  
  static VarStr POPNASLOV;
  static VarStr POPPOST;
  static VarStr POPCRTICA;
  static VarStr POPNAZ;
  static VarStr POPIZNOS;
  static VarStr POPOSN;
  
  static class Rabat {
  	String ckey;
  	String naziv;
  	BigDecimal posto;
  	BigDecimal osn;
  	BigDecimal iznos;
  	
  	public Rabat(DataSet ds) {
  		ckey = ds.getString("CRAB");
  		posto = ds.getBigDecimal("PRAB");
  		iznos = ds.getBigDecimal("IRAB");
  		osn = Aus.zero2;
  		naziv = "";
  	}

  	public String getKey() {
  		return ckey + "-" + posto;
  	}
  }
  
  protected void rekapPopust() {
  	POPNASLOV=new VarStr();
  	POPPOST=new VarStr();
  	POPCRTICA=new VarStr();
  	POPNAZ=new VarStr();
  	POPIZNOS=new VarStr();
  	POPOSN=new VarStr();
  	if (!frmParam.getParam("robno", "showPopust", "N",
  		"Prikazati rekapitulaciju popusta na izlaznim raèunima (D,N)").equals("D")) return;
	
		DataSet pops = vtrabat.getDataModule().getTempSet(Condition.whereAllEqual(Util.mkey, ds));
		pops.open();
		System.out.println("RABATI: " + ds);
		pops.setSort(new SortDescriptor(new String[] {"RBR", "LRBR"}));
		DataSet stav = stdoki.getDataModule().getTempSet(Condition.whereAllEqual(Util.mkey, ds));
		stav.open();
		HashMap rabs = new HashMap();
		BigDecimal sumarabat = Aus.zero2;
		short lastrbr = -1;
		for (pops.first(); pops.inBounds(); pops.next()) {
			if (pops.getShort("RBR") == 0) continue;
			
			lD.raLocate(stav, "RBR", "" + pops.getShort("RBR"));
			if (lastrbr != pops.getShort("RBR")) {
				sumarabat = Aus.zero2;
				lastrbr = pops.getShort("RBR");
			}
			BigDecimal osnovica = stav.getBigDecimal("INETO");
			if (pops.getString("RABNARAB").equalsIgnoreCase("D"))
				osnovica = osnovica.subtract(sumarabat);
			sumarabat = sumarabat.add(pops.getBigDecimal("IRAB"));
			Rabat r = new Rabat(pops);
			Rabat o = (Rabat) rabs.get(r.getKey());
			if (o == null) {
				r.osn = osnovica;
				lD.raLocate(dm.getRabati(), "CRAB", pops.getString("CRAB"));
				r.naziv = dm.getRabati().getString("NRAB");
				rabs.put(r.getKey(), r);
			} else {
				o.iznos = o.iznos.add(pops.getBigDecimal("IRAB"));
				o.osn = o.osn.add(osnovica);
			}
		}
		System.out.println(rabs);
		if (rabs.size() == 0) return;
		
		POPNASLOV.append("REKAPITULACIJA POPUSTA");
		POPNAZ.append("Vrsta");
		POPPOST.append("%");
		POPOSN.append("Osnovica");
		POPIZNOS.append("Iznos");
		
		ArrayList sorted = new ArrayList();
		pops.setSort(new SortDescriptor(new String[] {"LRBR" , "CRAB"}));
		for (pops.first(); pops.inBounds(); pops.next()) {
			Rabat r = new Rabat(pops);
			if (rabs.containsKey(r.getKey()))
				sorted.add(rabs.remove(r.getKey()));
		}
		
		for (Iterator i = sorted.iterator(); i.hasNext(); ) {
			Rabat r = (Rabat) i.next();
			POPNAZ.append('\n').append(r.naziv);
			POPCRTICA.append("\n-");
			POPPOST.append('\n').append(Aus.formatBigDecimal(r.posto));
			POPOSN.append('\n').append(Aus.formatBigDecimal(r.osn));
			POPIZNOS.append('\n').append(Aus.formatBigDecimal(r.iznos));
		}
  }

  protected void rekapPorez(){
  	rekapPopust();
  	
    CPOR=new VarStr();
    UKUPOR=new VarStr();
    IPRODBP=new VarStr();
    POR1=new VarStr();
    separator=new VarStr();
    DataSet qds_porez = repQC.getPoreziSet(ds.getString("CSKL"), ds.getString("VRDOK"), ds.getString("GOD"), ds.getInt("BRDOK"));
//    qdsIzd_porez = repQC.getPoreziIzdSet(ds);
//    
//    sysoutTEST s = new sysoutTEST(false);
//    s.prn(qdsIzd_porez);
    
    if(!qds_porez.isOpen()) qds_porez.open();
    qds_porez.first();
    do {
      CPOR.append(qds_porez.getString("CPOR")).append("\n");
      UKUPOR.append(sgQuerys.getSgQuerys().format(qds_porez, "UKUPOR")).append("\n");
      IPRODBP.append(sgQuerys.getSgQuerys().format(qds_porez, "IPRODBP")).append("\n");
      POR1.append(sgQuerys.getSgQuerys().format(qds_porez, "POR1")).append("\n");
      separator.append("-\n");
    } while (qds_porez.next());
    
    if (ds.getString("VRDOK").equals("IZD")){
      
      String nowDok = getFormatBroj();
      
      DUIRAB = Aus.zero2;
      DUIZT = Aus.zero2;
      DPOR1 = Aus.zero2;
      DIPRODSP = Aus.zero2;
      DPOSTO = Aus.zero2;
      /*int dsRow = ds.getRow();
      do {
      DUIRAB = DUIRAB.add(ds.getBigDecimal("UIRAB"));
      DUIZT = DUIZT.add(ds.getBigDecimal("UIZT"));
      DPOR1 = DPOR1.add(ds.getBigDecimal("POR1"));
      DIPRODSP = DIPRODSP.add(ds.getBigDecimal("IPRODSP"));
      DPOSTO = ds.getBigDecimal("UPZT1");
      } while (ds.next() && nowDok.equals(ru.getFormatBroj()));
      ds.goToRow(dsRow);*/
      
    }

  }
  
  public BigDecimal getDUIRAB(){
    return DUIRAB;
  }
  public BigDecimal getDUIZT(){
    return DUIZT;
  }
  public BigDecimal getDPOR1(){
    return DPOR1;
  }
  public BigDecimal getDIPRODSP(){
    return DIPRODSP;
  }
  public BigDecimal getDPOSTO(){
    //System.out.println("DPOSTO - "+DPOSTO);
    return DPOSTO;
  }
  
  public String getPorezDepartmentCPOR(){
    return CPOR.toString();
  }
  public String getPorezDepartmentUKUPOR(){
    return UKUPOR.toString();
  }
  public String getPorezDepartmentIPRODBP(){
    return IPRODBP.toString();
  }
  public String getPorezDepartmentPOR1(){
    return POR1.toString();
  }
  public String getPorezDepartmentCrtica(){
    return separator.toString();
  }

  public String getPOPNASLOV() {
  	return POPNASLOV.toString();
  }
  
  public String getPOPNAZ() {
  	return POPNAZ.toString();
  }
  
  public String getPOPCRTICA() {
  	return POPCRTICA.toString();
  }
  
  public String getPOPPOST() {
  	return POPPOST.toString();
  }
  
  public String getPOPOSN() {
  	return POPOSN.toString();
  }
  
  public String getPOPIZNOS() {
  	return POPIZNOS.toString();
  }
  
  public String getPNBZ2(){
    return ds.getString("PNBZ2");
  }
  public String getZIRO(){
    return ds.getString("ZIRO");
  }
  
  public String getIBAN() {
    if (!lD.raLocate(dm.getZirorn(), "ZIRO", getZIRO())) return "";
    
    return dm.getZirorn().getString("IBAN");
  }
  
  public String getSWIFT() {
    if (!lD.raLocate(dm.getZirorn(), "ZIRO", getZIRO())) return "";
    
    return dm.getZirorn().getString("SWIFT");
  }
  
  public String getBANKA() {
    if (!lD.raLocate(dm.getZirorn(), "ZIRO", getZIRO())) return "";
    
    return dm.getZirorn().getString("BANKA");
  }
  
  public String getDINETO() {
    return Aus.formatBigDecimal(dineto);
  }
  
  public String getDIBP() {
    return Aus.formatBigDecimal(diprodbp);
  }
  
  public String getDISP() {
    return Aus.formatBigDecimal(diprodsp);
  }
  
  public String getDKINETO() {
    return Aus.formatBigDecimal(dineto.multiply(getTECAJ()).divide(raSaldaKonti.getJedVal(getOZNVAL()), 2, BigDecimal.ROUND_HALF_UP));
  }
  
  public String getDKIBP() {
    return Aus.formatBigDecimal(diprodbp.multiply(getTECAJ()).divide(raSaldaKonti.getJedVal(getOZNVAL()), 2, BigDecimal.ROUND_HALF_UP));
  }
  
  public String getDKISP() {
    return Aus.formatBigDecimal(diprodsp.multiply(getTECAJ()).divide(raSaldaKonti.getJedVal(getOZNVAL()), 2, BigDecimal.ROUND_HALF_UP));
  }
  
  public String getDVINETO() {
    if (raSaldaKonti.isDomVal(ds)) return "";
    
    String pref = "(" + getOZNVAL() + ") ";
    if (lD.raLocate(dm.getValute(), "OZNVAL", getOZNVAL()))
      if (dm.getValute().getString("CHV").length() > 0)
        pref = dm.getValute().getString("CHV") + " ";
    if (!prefv) pref = "";
    
    return pref + Aus.formatBigDecimal(dineto.
        multiply(raSaldaKonti.getJedVal(getOZNVAL())).
        divide(getTECAJ(), 2, BigDecimal.ROUND_HALF_UP)) +
        (ispTecaj ? "\n" + Aus.formatBigDecimal(getTECAJ()) : "");
  }
  
  public String getDVIBP() {
    if (raSaldaKonti.isDomVal(ds)) return "";
    
    String pref = "(" + getOZNVAL() + ") ";
    if (lD.raLocate(dm.getValute(), "OZNVAL", getOZNVAL()))
      if (dm.getValute().getString("CHV").length() > 0)
        pref = dm.getValute().getString("CHV") + " ";
    if (!prefv) pref = "";
    
    return pref + Aus.formatBigDecimal(dineto.
        multiply(raSaldaKonti.getJedVal(getOZNVAL())).
        divide(getTECAJ(), 2, BigDecimal.ROUND_HALF_UP)) +
        (ispTecaj ? "\n" + Aus.formatBigDecimal(getTECAJ()) : "");
  }
  
  public String getDVISP() {
    if (raSaldaKonti.isDomVal(ds)) return "";
    
    String pref = "(" + getOZNVAL() + ") ";
    if (lD.raLocate(dm.getValute(), "OZNVAL", getOZNVAL()))
      if (dm.getValute().getString("CHV").length() > 0)
        pref = dm.getValute().getString("CHV") + " ";
    if (!prefv) pref = "";
    
    return pref + Aus.formatBigDecimal(dineto.
        multiply(raSaldaKonti.getJedVal(getOZNVAL())).
        divide(getTECAJ(), 2, BigDecimal.ROUND_HALF_UP)) +
        (ispTecaj ? "\n" + Aus.formatBigDecimal(getTECAJ()) : "");
  }
  
  public String getDVIZNOS() {
    if (raSaldaKonti.isDomVal(ds)) return "";
    return "Iznos u valuti:" + (ispTecaj ? "\nTeèaj:" : "");
  }

  boolean isHide() {
    return hideKup && (ds.getString("VRDOK").equals("GOT") ||
        ds.getString("VRDOK").equals("GRN")) &&
        ds.getString("AKTIV").equals("N");
  }
  
  boolean isPar() {
    return gotpar && ds.getInt("CPAR") > 0;
  }
  
  public int getCKUPAC() {
    if (isHide()) return -99;
    if (isPar()) return getCPAR();
   return ds.getInt("CKUPAC");
  }

  public String getIME() {
    String cached = cache.getValue("IME", Integer.toString(ds.getInt("CKUPAC")));
    if (cached != null) return cached;
    if (isHide()) return cache.returnValue("");
   colname[0] = "CKUPAC";
   return cache.returnValue(ru.getSomething(colname,dm.getKupci(),"IME").getString());
  }
  
  public String getLEOSS() {
    if (ds.getInt("PJ") <= 0)
      return ds.getInt("CPAR") + "/" + ds.getString("BRNARIZ");
    return ds.getInt("CPAR") + "-" + ds.getInt("PJ") + "/" + ds.getString("BRNARIZ");
  }

  public String getDLEOSS() {
    if (ds.getInt("PJ") <= 0)
      return ds.getInt("CPAR") + ";" + 
        Aus.formatTimestamp(ds.getTimestamp("DATDOK")) + ";" + 
        ds.getString("BRNARIZ");
    return ds.getInt("CPAR") + "-" + ds.getInt("PJ") + ";" + 
        Aus.formatTimestamp(ds.getTimestamp("DATDOK")) + ";" +
        ds.getString("BRNARIZ");
  }
  
  public String getPREZIME() {
    String cached = cache.getValue("PREZIME", Integer.toString(ds.getInt("CKUPAC")));
    if (cached != null) return cached; 
    if (isHide()) return cache.returnValue("");
   colname[0] = "CKUPAC";
   return cache.returnValue(ru.getSomething(colname,dm.getKupci(),"PREZIME").getString());
  }

  public String getNAZKUPCA() {
    if (isPar()) return getNAZPARL();
         String ime = getIME();
         String pre = getPREZIME();
         return ime + " " + pre;
  }
  
  public String getNAZKUPCAL() {
    return "\n"+getNAZKUPCA();
  }

  public String getMJKUPCA() {
    String cached = cache.getValue("MJKUPCA", Integer.toString(ds.getInt("CKUPAC")));
    if (cached != null) return cached;
    if (isHide()) return cache.returnValue("");
    if (isPar()) return cache.returnValue(getMJ());
   colname[0] = "CKUPAC";
   return cache.returnValue(ru.getSomething(colname,dm.getKupci(),"MJ").getString());
  }

  public String getADRKUPCA() {
    String cached = cache.getValue("ADRKUPCA", Integer.toString(ds.getInt("CKUPAC")));
    if (cached != null) return cached;
    if (isHide()) return cache.returnValue("");
    if (isPar()) return cache.returnValue(getADR());
   colname[0] = "CKUPAC";
   return cache.returnValue(ru.getSomething(colname,dm.getKupci(),"ADR").getString());
  }

  public int getPBRKUPCA() {
    
   colname[0] = "CKUPAC";
   return ru.getSomething(colname,dm.getKupci(),"PBR").getAsInt();
  }
  
  public String getKUPTEL() {
    String cached = cache.getValue("KUPTEL", Integer.toString(ds.getInt("CKUPAC")));
    if (cached != null) return cached;
    if (isHide()) return cache.returnValue("");
    if (isPar()) return cache.returnValue(getTEL());
    colname[0] = "CKUPAC";
    return "Tel "+cache.returnValue(ru.getSomething(colname,dm.getKupci(),"TEL").getString());
  }
  
  public String getKUPEMADR() {
    String cached = cache.getValue("KUPEMADR", Integer.toString(ds.getInt("CKUPAC")));
    if (cached != null) return cached;
/*    if (isHide()) return cache.returnValue("");
    if (isPar()) return cache.returnValue(getTEL());*/
    colname[0] = "CKUPAC";
    return cache.returnValue(ru.getSomething(colname,dm.getKupci(),"EMADR").getString());
  }

  public String getPbrMjestoKupca() {
    String cached = cache.getValue("PBRMJKUPCA", Integer.toString(ds.getInt("CKUPAC")));
    if (cached != null) return cached;
    if (isHide()) return cache.returnValue("");
    if (isPar()) return cache.returnValue(getMJ());
    String pm ="";
    colname[0] = "CKUPAC";
    if (ru.getSomething(colname,dm.getKupci(),"PBR").getAsInt() != 0){
      pm = ru.getSomething(colname,dm.getKupci(),"PBR").getAsInt()+" " + ru.getSomething(colname,dm.getKupci(),"MJ").getString();
    } else {
      pm = ru.getSomething(colname,dm.getKupci(),"MJ").getString();
    }
    return cache.returnValue(pm);
  }

  public String getJMBG() {
    String cached = cache.getValue("JMBG", Integer.toString(ds.getInt("CKUPAC")));
    if (cached != null) return cached;
    if (isHide()) return cache.returnValue("");
    if (isPar()) return cache.returnValue(getMB());
   colname[0] = "CKUPAC";
   String result = "";
   if (!oib.equalsIgnoreCase("MB")) {
     String br = ru.getSomething(colname,dm.getKupci(),"OIB").toString();
     if (br.length() == 0) result = "";
     else result = "OIB " + br;
   } 
   if (oib.equalsIgnoreCase("MB") || result.length() == 0) {
     String mb = ru.getSomething(colname,dm.getKupci(),"JMBG").toString();
     if (mb.length() == 0) result = "";
     else result = "MB " + mb; 
   }   
   return cache.returnValue(result);
  }

  public String getRADNOMJESTO(){
    String cached = cache.getValue("RADMJ", ds.getString("CSKL"));
    if (cached != null) return cached;
    colname[0] = "CSKL";
    return cache.returnValue(ru.getSomething(colname,dm.getSklad(),"NAZSKL").getString());
  }
  
  public String getCRADNIKA() {
    return ds.getString("CRADNIK");
  }

  public String getIMEIBEZIME() {
    String cached = cache.getValue("IMEIBEZIME", ds.getString("CRADNIK"));
    if (cached != null) return cached;
    if (!ds.getString("CRADNIK").equals("")){
      ru.setDataSet(ds);
      String ime = ru.getSomething(new String[] {"CRADNIK"},dm.getRadnici(),"IME").getString();
      String prezime = ru.getSomething(new String[] {"CRADNIK"},dm.getRadnici(),"PREZIME").getString();
      return cache.returnValue(ime.concat(" ".concat(prezime)));
    }
    return cache.returnValue("");
  }
  //ai trebovanja CKO = CRADNIK    lnydrn
  public String getNaruciteljCKO() {
    String cached = cache.getValue("IMEIBEZIME", ds.getInt("CKO")+"");
    if (cached != null) return cached;
    if (ds.getInt("CKO")>0){
      if (lookupData.getlookupData().raLocate(dm.getRadnici(), "CRADNIK", ds.getInt("CKO")+"")) {
        String ime =  dm.getRadnici().getString("IME");
        String prezime =  dm.getRadnici().getString("PREZIME");
        return cache.returnValue(ime.concat(" ".concat(prezime)));
      }
    }
    return cache.returnValue("");    
  }

  static VarStr nacpl;
  static VarStr irata;
  static VarStr datnp;

  protected void nacPl(){
    nacpl=new VarStr();
    irata=new VarStr();
    datnp=new VarStr();
    com.borland.dx.sql.dataset.QueryDataSet rate = ut.getNewQueryDataSet(
        "SELECT rbr, cnacpl, cbanka, datum, irata FROM rate "+
        "WHERE cskl='"+ds.getString("CSKL")+
        "' AND vrdok='"+ds.getString("VRDOK")+"' AND brdok=" + ds.getInt("BRDOK") +
        " and god='"+ds.getString("GOD")+"' order by rbr");
    if (rate.rowCount() == 0){
      nacpl.append("Gotovina");
      String jednaRata = sgQuerys.getSgQuerys().format(ds, "UIRAC");
      if (jednaRata.indexOf(',') <0)
        irata.append(jednaRata).append(",00");
    } else {
      rate.first();
      do {
        try {
          if (lookupData.getlookupData().raLocate(dm.getNacpl(), "CNACPL", rate.getString("CNACPL"))){
            if(dm.getNacpl().getString("FL_KARTICA").equals("D")){
              if (lookupData.getlookupData().raLocate(dm.getKartice(), "CBANKA", rate.getString("CBANKA"))){
                nacpl.append(dm.getNacpl().getString("NAZNACPL")).append(" - ").append(dm.getKartice().getString("NAZIV")).append("\n");
              } else {
                nacpl.append(dm.getNacpl().getString("NAZNACPL")).append("\n");
              }
            } else if(dm.getNacpl().getString("FL_CEK").equals("D")){
              if (lookupData.getlookupData().raLocate(dm.getBanke(), "CBANKA", rate.getString("CBANKA"))){
                nacpl.append(dm.getNacpl().getString("NAZNACPL")).append(" - ").append(dm.getBanke().getString("NAZIV")).append("\n");
              } else {
                nacpl.append(dm.getNacpl().getString("NAZNACPL")).append("\n");
              }
            } else{
              nacpl.append(dm.getNacpl().getString("NAZNACPL")).append("\n");
            }
          }
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        String jednaRata = sgQuerys.getSgQuerys().format(rate, "IRATA");
        irata.append(jednaRata).append("\n");
        if (!dm.getNacpl().getString("FL_GOT").equals("D"))
          datnp.append(rdu.dataFormatter(rate.getTimestamp("DATUM"))).append("\n");
        else
          datnp.append("\n");

      } while (rate.next());
    }
  }

  static String znacajkeSubjekta;
  static String subjekt;

  protected void setZnacajkeSubjekta(){
    znacajkeSubjekta = "";
    subjekt = "";
    String crn = ds.getString("CRADNAL");
    if (crn.length() == 0) return;
    
    DataSet rn = RN.getDataModule().getTempSet(Condition.equal("CRADNAL", crn));
    rn.open();
    if (rn.rowCount() == 0) return;
    DataSet sub = RN_subjekt.getDataModule().getTempSet(Condition.equal("CSUBRN", rn));
    sub.open();
    if (sub.rowCount() == 0) return;
    
      String upitString = "SELECT RN_znacsub.vriznac, RN_znacajke.znacopis, RN_znacajke.znactip, "+
        "RN_znacajke.znacsif, RN_znacajke.cznac, RN_znacajke.znacdoh FROM RN_znacsub, RN_znacajke WHERE "+
        "RN_znacsub.cvrsubj = RN_znacajke.cvrsubj AND "+
        "RN_znacsub.cznac = RN_znacajke.cznac and RN_znacsub.cradnal = '"+crn+"' AND csubrn='"+
        rn.getString("CSUBRN")+"' and RN_znacsub.VRIZNAC != ''";

      DataSet zns = ut.getNewQueryDataSet(upitString, true);

      System.out.println("upitString = " + upitString);

      String cvrs = String.valueOf(rn.getShort("CVRSUBJ"));
      lookupData.getlookupData().raLocate(dm.getRN_vrsub(), "CVRSUBJ", cvrs);

      subjekt = dm.getRN_vrsub().getString("NAZVRSUBJ")+"\n";
      VarStr v = new VarStr();
      
      String csub = frmParam.getParam("rn", "ispisCsub", "",
          "Opis šifre subjekta (ostaviti prazno za neispisivanje šifre)");
      
      if (csub != null && csub.trim().length() > 0) {
        v.append(csub);
        v.append(" - ");
        v.append(rn.getString("CSUBRN"));
        v.append(", ");
      }
      if (frmParam.getParam("rn", "ispisBusBroj", "D",
         "Ispis serijskog broja subjekta na raèunu (D,N)").equalsIgnoreCase("D")) {
        v.append(dm.getRN_vrsub().getString("nazserbr")).append(" - ");
        v.append(sub.getString("BROJ")).append(", ");
      }

      for (zns.first(); zns.inBounds(); zns.next()) {
        v.append(zns.getString("ZNACOPIS")).append(" - ");
        if (zns.getString("ZNACSIF").equalsIgnoreCase("D")) {
          if (zns.getString("ZNACDOH").trim().length() == 0) {
            lookupData.getlookupData().raLocate(dm.getRN_sifznac(), new String[]
                {"CVRSUBJ", "CZNAC", "VRIZNAC"}, new String[] {String.valueOf(zns.getShort("CVRSUBJ")),
                String.valueOf(zns.getShort("CZNAC")), zns.getString("VRIZNAC")});
            v.append(dm.getRN_sifznac().getString("OPIS"));
          } else
            v.append(getDohValue(zns.getString("ZNACDOH").trim(), zns.getString("VRIZNAC")));
        } else if (zns.getString("ZNACTIP").equalsIgnoreCase("S")) {
          v.append(zns.getString("VRIZNAC"));
        } else if (zns.getString("ZNACTIP").equalsIgnoreCase("I")) {
          v.append(zns.getString("VRIZNAC"));
        } else if (zns.getString("ZNACTIP").equalsIgnoreCase("D")) {
          v.append(rdu.dataFormatter(Timestamp.valueOf(zns.getString("VRIZNAC"))));
        } else if (zns.getString("ZNACTIP").equalsIgnoreCase("2")) {
          v.append(sgQuerys.getSgQuerys().format(zns, "VRIZNAC", 2));
        } else if (zns.getString("ZNACTIP").equalsIgnoreCase("3")) {
          v.append(sgQuerys.getSgQuerys().format(zns, "VRIZNAC", 3));
        }
        v.append(", ");
      }
      znacajkeSubjekta = v.chop(2).toString();
  }
  
  private String getDohValue(String dohDef, String keyVal) {
    int colon = dohDef.indexOf(':');
    if (colon >= 0) dohDef = dohDef.substring(0, colon);
    
    String[] parts = new VarStr(dohDef).splitTrimmed('+');
    DataSet ds = null;
    try {
      java.lang.reflect.Method m = dM.class.getMethod(parts[2], null);
      ds = (DataSet) m.invoke(dM.getDataModule(), null);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
    if (!lookupData.getlookupData().raLocate(ds, parts[0], keyVal)) return "";
    Variant v = new Variant();
    ds.getVariant(parts[1], v);
    return v.toString();
  }

//  private String getFormatted(DataSet set,String colName) {
//    com.borland.dx.text.VariantFormatter formater = set.getColumn(colName).getFormatter(); // dm.getStavkepn().getColumn("IZNOS").getFormatter();
//    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();
//    set.getVariant(colName,v);
//    return formater.format(v);
//  }


  public String getNACINPLACANJA(){
    return nacpl.toString();
  }

  public String getDATUMNAPLATE(){
    return datnp.toString();
  }

  public String getIZNOSRATE(){
    return irata.toString();
  }
  
  public String getARTNAP() {
    return naps;
  }

  // radni nalozi handlers

  public String getSUBJEKTRN() {
    return subjekt;
  }

  public String getZnacajkeSubjekta() {
    return znacajkeSubjekta;
  }
  
  public String getBrojZap(){
    try {
      if (ds.getInt("BRNARIZ") != 0)
        return ds.getInt("BRNARIZ") + "";
    } catch (Exception e) {
      return ds.getString("BRNARIZ");
    }
    return "";
  }
  
  public String getDatZap(){
    return rdu.dataFormatter(ds.getTimestamp("DATNARIZ"));
  }
  
  private void checkSpecGroup() {
    specGroup = frmParam.getParam("rn", "specGrupa", "", 
        "Grupa artikala za odvojeno prikazana na raèunima iz radnog naloga (npr. ulje)");
    if (specGroup == null || specGroup.length() == 0) specGroup = "#^";
    specText = frmParam.getParam("rn", "specTekst", "", 
        "Tekst za opis odvojene grupe artikala na raèunu iz radnog naloga");
    matText = frmParam.getParam("rn", "matTekst", "Ugraðeni dijelovi", 
        "Tekst za opis potrošenog materijala na raèunu iz radnog naloga");
    radText = frmParam.getParam("rn", "radTekst", "Radne operacije", 
        "Tekst za opis usluga na raèunu iz radnog naloga");
  }

  private void checkNap() {
    if (lD.raLocate(dm.getArtikli(), "CART", 
        Integer.toString(ds.getInt("CART")))) {
      if (dm.getArtikli().getString("CNAP").length() > 0 &&
          lD.raLocate(dm.getNapomene(), "CNAP",
              dm.getArtikli().getString("CNAP"))) {
        if (allNaps.add(dm.getArtikli().getString("CNAP"))) {
          if (naps != null && naps.length() > 0)
            naps = naps + "\n";
          naps = naps + dm.getNapomene().getString("NAZNAP");
        }
      }
    }
  }
  
  public int getMatUslGrouping() {
    int grrr;
    String cartik = ds.getInt("CART")+"";
    lookupData.getlookupData().raLocate(dm.getArtikli(),"CART",cartik);
    if (specGroup.equalsIgnoreCase(dm.getArtikli().getString("CGRART"))) grrr = 2;
    else if (raVart.isUsluga(dm.getArtikli())) grrr = 3;
    else grrr = 1;
//    System.out.println("grr = " + grrr);
    return grrr;
  }

  public String getTelFax(){
    String cached = cache.getValue("TELFAX", ds.getString("CVRTR"));
    if (cached != null) return cached;
    String telNfax = "";
    boolean imaTelefon = false;
    if (!getLogoTel1().equals("") || !getLogoTel2().equals("")) {
      telNfax = "Tel ";
      imaTelefon = true;
      if (!getLogoTel1().equals("")) telNfax = telNfax+getLogoTel1();
      if (!getLogoTel2().equals("")){
        if (!getLogoTel1().equals("")){
          telNfax = telNfax+", "+getLogoTel2();
        } else {
          telNfax = telNfax+getLogoTel2();
        }
      }
    }
    if (!getLogoFax().equals("")) {
      if (imaTelefon){
        telNfax = telNfax+", Fax "+getLogoFax();
      } else {
        telNfax = "Fax "+getLogoFax();
      }
    }
    return cache.returnValue(telNfax);
  }

  public String getRADOVIMATERIJAL() {
    String grrr;
    String cartik = ds.getInt("CART")+"";
    lookupData.getlookupData().raLocate(dm.getArtikli(),"CART",cartik);
    if (specGroup.equalsIgnoreCase(dm.getArtikli().getString("CGRART"))) grrr = specText;
    else if (raVart.isUsluga(dm.getArtikli())) grrr = radText; //"Izvedeni radovi";
    else grrr = matText; //"Utrošeni materijal";
    return grrr;
  }
  
  public String getKOLICINEJEDINICE(){
    String grrr;
    String cartik = ds.getInt("CART")+"";
    lookupData.getlookupData().raLocate(dm.getArtikli(),"CART",cartik);
    if (raVart.isUsluga(dm.getArtikli())) grrr = "Vr. jed."; //"Izvedeni radovi";
    else grrr = "Kolièina"; //"Utrošeni materijal";
    return grrr;
  }

  // radni nalozi handlers

  public String getFirstLine(){
    return rm.getFirstLine();
  }
  public String getSecondLine(){
    return rm.getSecondLine();
  }
  public String getThirdLine(){
    return rm.getThirdLine();
  }

  // PONUDE
  //-------------------------------------------------------------

  public double getPonIznos(){
    return getFMCPRP() * getKOL().doubleValue();
  }

  // METRO UVLACENJE
  //-------------------------------------------------------------

  public String getMETRODOBAVLJAC(){
    return metroDob;
  }

  public String getCARTMETRO(){
    if (!lookupData.getlookupData().raLocate(dm.getArtikli(),"CART",ds.getInt("CART")+""))
    return "";
    return dm.getArtikli().getString("SIFZANAR");
  }

  public String getMetroBrOtp(){
    String brotp = vl.maskString(ds.getInt("BRDOK")+"",'0',6)+"-"+ds.getString("CSKL")+"/"+ds.getString("GOD").substring(2,ds.getString("GOD").length());
    return brotp;
  }

  public String getMetroBrRac(){
    String brrac = vl.maskString(ds.getInt("BRDOK")+"",'0',6)+"/"+ds.getString("GOD").substring(2,ds.getString("GOD").length());
    return brrac;
  }
  
  public String getVEZANIDOKUMENTI(){    
    return raPrenosVT.getPrenosText(ds, cache);
  }
  
  public String getVEZANIDOKUMENTIDEST(){
    return raPrenosVT.getPrenosText(ds,true, cache);
  }
  public String getVEZANIDOKUMENTISRC(){
    return raPrenosVT.getPrenosText(ds,false, cache);
  }
  public String getDESTVEZANIDOKUMENTI(){
    return getVEZANIDOKUMENTIDEST();
  }
  public String getSRCVEZANIDOKUMENTI(){
    return getVEZANIDOKUMENTISRC();
  }  
  public String getIZDATNICE(){
    try {
    return raPrenosVT.getPrenosTextRnlIzd(ds,cache);
    } catch (Exception e) {
//      e.printStackTrace();
      return "";
    }
  }
  
  public String getJIR() {
    try {
      if (repFISBIH.isFISBIH() && (ds.hasColumn("FBR") != null) && (ds.getInt("FBR") > 0)) {
        //return Valid.getValid().maskZeroInteger(new Integer(ds.getInt("FBR")), 6);
        return ds.getInt("FBR")+"";
      } else if (ds.hasColumn("JIR") != null) {
        return ds.getString("JIR").trim();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }
  
  public String getDODATNIOPIS() {
    String dep = ds.getString("CSKL")+ds.getString("VRDOK")+ds.getString("GOD")+ds.getInt("BRDOK");
    String cached = cache.getValue("DODATNIOPIS", dep);
    if (cached != null) return cached;
    String ss = rCD.getKey(ds,new String[]{"CSKL","VRDOK","GOD","BRDOK"},"doki");    
    if (lD.raLocate(dm.getVTText(),"CKEY",ss)) {
      return cache.returnValue(dm.getVTText().getString("TEXTFAK"));
    } else {
      return cache.returnValue("");
    }
  }

  String ispisPJ = "D";
  String conVl = "N";
  String metroDob = "";
  String oib = "";
  String prefn = "";
  String labdod = "";
  
  boolean gotpar = false;
  boolean hideKup = false;
  boolean specChars = false;
  boolean iznosPop = false;
  boolean showPop = false;
  boolean ispTecaj = false;
  boolean prefv = false;
  
  private void setParams() {
    modParams();
    conVl = frmParam.getParam("robno","ConVl","N",
            "Dodati vlasnika u naziv partnera ako je obrt(D,V,N)");
    specChars = frmParam.getParam("robno","chForSpecial","N","Provjerava specijalne " +
            "znakove (*,_,+,-) i odrezuje sve iza njih").equalsIgnoreCase("D");
    iznosPop = frmParam.getFrmParam().getParam("robno","ukuSpop","N",
        "Prikaz ukupnog iznosa stavke izaza s popustom (D/N)").equalsIgnoreCase("D");
    metroDob = frmParam.getParam("robno","MetroCpar","20196",
        "Šifra dobavljaèa na raèunu i otpremnici za Metro");
    oib = frmParam.getParam("robno", "oibMode", "MB", 
        "Staviti matièni broj (MB) ili OIB?");
    hideKup = frmParam.getParam("robno", "kupacHack", "N",
        "Omoguæiti skrivanje kupca na gotovinskim raèunima (D,N)").equals("D");
    showPop = frmParam.getParam("robno", "showPopust", "N",
    	"Prikazati rekapitulaciju popusta na izlaznim raèunima (D,N)").equals("D");
    prefn = frmParam.getParam("robno", "prefixPar", "",
        "Prefiks ispred imena partnera");
    specForm = frmParam.getParam("robno", "specForm", "",
      "Custom format broja izlaznog dokumenta na ispisu");
    ispTecaj = frmParam.getParam("robno", "ispTecaj", "N",
      "Ispis teèaja zajedno s iznosom u valuti (D,N)").equalsIgnoreCase("D");
    prefv = frmParam.getParam("robno", "prefVal", "N",
      "Ispis prefiksa ispred iznosa u valuti (D,N)").equalsIgnoreCase("D");
    gotpar = "D".equalsIgnoreCase(
        frmParam.getParam("robno", "gotPar", "N",
        "Gotovinski raèuni za partnere (D,N)"));
    
    String npref = frmParam.getParam("robno", "prefixPar" + getCSKL());
    if (npref != null) prefn = npref;
    
    if (prefn.length() > 0) prefn = prefn + "\n";
    
    
    try {
      DataSet ds = dokidod.getDataModule().getTempSet(
          Condition.equal("BRRAC", "LABEL"));
      ds.open();
      if (ds.rowCount() == 0) labdod = "";
      else {
        ds.setSort(new SortDescriptor(new String[] {"RBS"}));
        VarStr buf = new VarStr();
        for (ds.first(); ds.inBounds(); ds.next())
          buf.append(ds.getString("VAL")).append('\n');
        labdod = buf.toString();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void modParams() {
    ispisPJ = frmParam.getParam("robno","ispisPJ","D","Ispis poslovne jedinice na ROT-u " +
    "(D-u adresi, I-kao isporuka, O-na oba mjesta, N-bez P.J.)");
    String ispisPJCPAR = getIspisPJCPAR(getCPAR());
    ispisPJ = (ispisPJCPAR.equals("")?ispisPJ:ispisPJCPAR);
    System.err.println("modParams: lastdok = "+lastDok+" cpar = "+getCPAR()+" ispisPJ = "+ispisPJ);
    
    String npref = frmParam.getParam("robno", "prefixPar" + getCSKL());
    if (npref != null) {
      prefn = npref;
      if (prefn.length() > 0) prefn = prefn + "\n";
    }
  }
  public static String getIspisPJCPAR(int cpar) {
    String ispisPJCPAR = frmParam.getParam("robno", "ispisPJCPAR", "", "Vrijednost parametra ispisPJ u odnosu na partnera (cpar1:value,cpar2:value,cparN:value...");
    try {      
      String[] pars = new VarStr(ispisPJCPAR).split(',');
      for (int i = 0; i < pars.length; i++) {
        String[] p = new VarStr(pars[i]).split(':');
        int _cpar = Integer.parseInt(p[0]);
        if (_cpar == cpar) return p[1];
      }
    } catch (Exception e) {
      // 
      //e.printStackTrace();
    }
    return "";
  }

  //-------------------------------------------------------------

}