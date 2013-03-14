/****license*****************************************************************
**   file: repPrkProvider.java
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
import hr.restart.baza.VTZtr;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;

public class repPrkProvider implements raReportData { //implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  repMemo rpm = repMemo.getrepMemo();
  DataSet ds; // = reportsQuerysCollector.getRQCModule().getQueryDataSet();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  protected hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  
  String lastDok = "";
  
  static boolean isMC;

  public repPrkProvider() {
    ds = reportsQuerysCollector.getRQCModule().getQueryDataSet();
    lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", ds.getString("CSKL"));
    if (dm.getSklad().getString("VRZAL").equals("M")) isMC = true;
    else isMC = false;
    rekapZav();
    lastDok = getFormatBroj();
  }
  
  public raReportData getRow(int i) {
    ds.goToRow(i);
    String nowDok = getFormatBroj();
    if (lastDok != null && !nowDok.equals(lastDok)) {
      lastDok = nowDok;
      rekapZav();
    }
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    ds = null;
  }
  
  
/*
  public repPrkProvider(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
        ru.setDataSet(ds);
      }
      int indx=0;
      public Object nextElement() {

        return new repPrkProvider(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }
  public void close() {}

*/
  
  public double getUIPRPOR(){
    return ds.getBigDecimal("UIPRPOR").doubleValue();
  }
  public String getBRDOKUL(){
    return ds.getString("BRDOKUL");
  }
  public String getDATDOKUL(){
    return rdu.dataFormatter(ds.getTimestamp("DATDOKUL"));
  }
  public String getBRRAC(){
    return ds.getString("BRRAC");
  }
  public String getDATRAC(){
    return rdu.dataFormatter(ds.getTimestamp("DATRAC"));
  }


  //// dobavljac
  public int getCPAR() {
    return ds.getInt("CPAR");
  }

  public String getNAZPAR() {
    ru.setDataSet(ds);
    colname[0] = "CPAR";
    return ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString();
  }
  public String getCSKL() {
    return ds.getString("CSKL");
  }
  public String getNAZSKL(){
    ru.setDataSet(ds);
    colname[0] = "CSKL";
    return ru.getSomething(colname,dm.getSklad(),"NAZSKL").getString();
  }

  public String getDATDOK() {
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }
  public String getDATDOSP() {
    return rdu.dataFormatter(ds.getTimestamp("DATDOSP"));
  }
  public short getRBR() {
    return ds.getShort("RBR");
  }
  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
  }
  public String getNAZART() {
    return ds.getString("NAZART");
  }
  public BigDecimal getPOSTOPOREZA(){
	 //ru.setDataSet(ds);
	 //getPorezPos();
	 //colname[0] ="CPOR";
	 //return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"ukupor").getBigDecimal();
    
    return ds.getBigDecimal("MC").divide(ds.getBigDecimal("VC"), 2, BigDecimal.ROUND_HALF_UP).subtract(Aus.one0).movePointRight(2).setScale(2);
    
  }
  public String getPorezPos() {
	 ru.setDataSet(ds);
	 colname[0]="CART";
	 return ru.getSomething(colname,dm.getArtikli(),"CPOR").getString();
  }
  public String getJM() {
    return ds.getString("JM");
  }
  public double getPZT() {
    return ds.getBigDecimal("PZT").doubleValue();
  }
  public BigDecimal getKOL() {
    return ds.getBigDecimal("KOL");
  }
  public BigDecimal getSKOL() {
    return ds.getBigDecimal("SKOL");
  }
  public BigDecimal getDC() {
    return ds.getBigDecimal("DC");
  }
  public BigDecimal getDC_VAL() {
    return ds.getBigDecimal("DC_VAL");
  }
  public double getIRAB() {
    return ds.getBigDecimal("IRAB").doubleValue();
  }
  public double getIPOR() {
    return ds.getBigDecimal("ISP").subtract(ds.getBigDecimal("IBP")).doubleValue();
  }
  public double getIZT() {
    return ds.getBigDecimal("IZT").doubleValue();
  }
  public double getNC() {
    return ds.getBigDecimal("NC").doubleValue();
  }
  public double getINAB() {
    return ds.getBigDecimal("INAB").doubleValue();
  }
  public double getFAKEINAB(){
    return (ds.getBigDecimal("NC").multiply(ds.getBigDecimal("KOL"))).doubleValue();
  }
  public double getPMAR() {
    return ds.getBigDecimal("PMAR").doubleValue();
  }
  public double getIMAR() {
    return ds.getBigDecimal("IBP").subtract(ds.getBigDecimal("INAB")).doubleValue();
  }
  public double getVC() {
    return ds.getBigDecimal("VC").doubleValue();
  }
  public BigDecimal getZC() {
    return ds.getBigDecimal("ZC");
  }
  public double getIZAD() {
    return ds.getBigDecimal("IZAD").doubleValue();
  }
  public double getIBP() {
    return ds.getBigDecimal("IBP").doubleValue();
  }
  public double getPRAB() {
    return ds.getBigDecimal("PRAB").doubleValue();
  }
  public double getIDOB() {
    return ds.getBigDecimal("IDOB").doubleValue();
  }
  public double getIDOB_VAL() {
    return ds.getBigDecimal("IDOB_VAL").doubleValue();
  }
  public double getMC() {
    return ds.getBigDecimal("MC").doubleValue();
  }
  public double getISP() {
    return ds.getBigDecimal("ISP").doubleValue();
  }
  public double getUKPOR() {
    return (ds.getBigDecimal("POR1").add(ds.getBigDecimal("POR2"))).add(ds.getBigDecimal("POR3")).doubleValue();
  }
  public int getBRDOK(){
    return ds.getInt("BRDOK");
  }
  public String getFormatBroj(){
    ru.setDataSet(ds);
    return ru.getFormatBroj();
  }
  public String getFirstLine(){
    return rpm.getFirstLine();
  }
  public BigDecimal getTECAJ(){
    // TODO ako bude potrebno teèaj postaviti na 1,000000
    //if (ds.getBigDecimal("TECAJ").compareTo(Aus.zero2) == 0) return new BigDecimal("1.000000");
     return ds.getBigDecimal("TECAJ");
  }
  public String getOZNVAL(){
     return ds.getString("OZNVAL");
  }
  public String getCVAL() {
    if (hr.restart.util.lookupData.getlookupData().raLocate(
        hr.restart.baza.dM.getDataModule().getValute(),
        "OZNVAL",ds.getString("OZNVAL"))){
      return String.valueOf(hr.restart.baza.dM.getDataModule().getValute().getShort("CVAL"));
    }
    return "";
  }
  public String getNAZVAL(){
    if (hr.restart.util.lookupData.getlookupData().raLocate(
        hr.restart.baza.dM.getDataModule().getValute(),
        "OZNVAL",ds.getString("OZNVAL"))){
      return hr.restart.baza.dM.getDataModule().getValute().getString("NAZVAL");
    }
    return "";
  }
  public String getDOMVAL(){
    com.borland.dx.sql.dataset.QueryDataSet domval = hr.restart.baza.Valute.getDataModule().getTempSet("STRVAL = 'N'");
    domval.open();
    return domval.getString("OZNVAL");
  }
  public String getSecondLine(){
    return rpm.getSecondLine();
  }
  public String getThirdLine(){
    return rpm.getThirdLine();
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(val.getToday());
  }
  
  /// NIVELACIJA - PORAVNANJE

  public BigDecimal getSTARACIJENA(){
    if (isMC) return ds.getBigDecimal("SMC");
    return ds.getBigDecimal("SVC");
  }

  public BigDecimal getNOVACIJENA(){
    if (isMC) return ds.getBigDecimal("MC");
    return ds.getBigDecimal("VC");
  }

  public double getPORAV(){
//    return ((getSTARACIJENA().subtract(getNOVACIJENA())).multiply(getKOL())).doubleValue();
     return ds.getBigDecimal("PORAV").doubleValue();
  }
  
  public String getZavLab() {
    return ZavLab;
  }
  
  public String getZavNazivLab() {
    return ZavNazivLab;
  }
  
  public String getZavIznosLab() {
    return ZavIznosLab;
  }
  
  public String getZavPorezLab() {
    return ZavPorezLab;
  }
  
  public String getZavOsnovicaLab() {
    return ZavOsnovicaLab;
  }
  
  public String getZavBrojLab() {
    return ZavBrojLab;
  }
  
  public String getZavDatumLab() {
    return ZavDatumLab;
  }
  
  public String getZavPartnerLab() {
    return ZavPartnerLab;
  }
  
  public String getZavNaziv() {
    return ZavNaziv;
  }
  
  public String getZavIznos() {
    return ZavIznos;
  }
  
  public String getZavPorez() {
    return ZavPorez;
  }
  
  public String getZavOsnovica() {
    return ZavOsnovica;
  }
  
  public String getZavBroj() {
    return ZavBroj;
  }
  
  public String getZavDatum() {
    return ZavDatum;
  }
  
  public String getZavPartner() {
    return ZavPartner;
  }
  
  String ZavLab, ZavNazivLab, ZavNaziv, ZavIznosLab, ZavIznos; 
  String ZavPorezLab, ZavPorez, ZavOsnovicaLab, ZavOsnovica;
  String ZavBrojLab, ZavDatumLab, ZavPartnerLab;
  String ZavBroj, ZavDatum, ZavPartner;
  
  protected void rekapZav(){
    ZavLab = ZavNazivLab = ZavNaziv = ZavIznosLab = ZavIznos = "";
    ZavPorezLab = ZavPorez = ZavOsnovicaLab = ZavOsnovica = "";
    ZavBrojLab = ZavDatumLab = ZavPartnerLab = "";
    ZavBroj = ZavDatum = ZavPartner = "";
    
    if (!ds.getString("CSHZT").equals("YES")) return;
    
    DataSet vt = VTZtr.getDataModule().getTempSet(
        Condition.whereAllEqual(Util.mkey, ds).and(
            Condition.equal("RBR", 0)));
    vt.open();
    if (vt.rowCount() == 0) return;
    
    ZavLab = "Rekapitulacija zavisnih troškova";
    ZavNazivLab = "Zavisni trošak";
    ZavIznosLab = "Iznos";
    ZavPorezLab = "Pretporez";
    ZavOsnovicaLab = "Osnovica";
    ZavBrojLab = "Broj raèuna";
    ZavDatumLab = "Datum";
    ZavPartnerLab = "Partner";
    
    vt.setSort(new SortDescriptor(new String[] {"LRBR"}));
    for (vt.first(); vt.inBounds(); vt.next()) {
      lD.raLocate(dm.getZtr(), "CZT", ""+ vt.getShort("CZT"));
      ZavNaziv = ZavNaziv + dm.getZtr().getString("NZT") + "\n";
      ZavIznos = ZavIznos + Aus.formatBigDecimal(
          vt.getBigDecimal("IZT").add(vt.getBigDecimal("PRPOR"))) + "\n";
      ZavPorez = ZavPorez + Aus.formatBigDecimal(vt.getBigDecimal("PRPOR")) + "\n";
      ZavOsnovica = ZavOsnovica + Aus.formatBigDecimal(vt.getBigDecimal("IZT")) + "\n";
      if (vt.getString("BRRAC").length() == 0) {
        ZavBroj = ZavBroj + "\n";
        ZavDatum = ZavDatum + "\n";
        ZavPartner = ZavPartner + "\n";
      } else {
        ZavBroj = ZavBroj + vt.getString("BRRAC") + "\n";
        ZavDatum = ZavDatum + Aus.formatTimestamp(vt.getTimestamp("DATRAC")) + "\n";
        ZavPartner = ZavPartner + vt.getInt("CPAR") + "\n";
      }
    }
  }
}