/****license*****************************************************************
**   file: repDevKarticaZbirno.java
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

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;


public class repDevKarticaZbirno implements raReportData {
  DataSet ds;
  
  upZbirnoPeriodVal up = upZbirnoPeriodVal.getInstance();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  String[] colname = new String[] {""};
  
  upZbirnoPeriodVal.ValTotals tot;

  public repDevKarticaZbirno() {
//     ru.setDataSet(ds);
    ds = up.getKarQDS();
  }

  public raReportData getRow(int idx) {
    ds.goToRow(idx);
    tot = (upZbirnoPeriodVal.ValTotals) up.rekap.get(new Integer(ds.getInt("CPAR")));
    return this;
  }
  
  public int getRowCount() {
    return ds.getRowCount();
  }

  public void close() {
    ds = null;
  }
  
  public String getDatKnj()
  {
    return rdu.dataFormatter(ds.getTimestamp("DATUMKNJ"));
  }

  public String getDatDok()
  {
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }

  public String getDatDosp()
  {
    String vr = ds.getString("VRDOK");
    if (vr.equals("UPL") || vr.equals("IPL")) return "";
    return rdu.dataFormatter(ds.getTimestamp("DATDOSP"));
  }

  public String getBrojDok()
  {
    return ds.getString("BROJDOK");
  }

  public double getID()
  {
    return ds.getBigDecimal("ID").doubleValue();
  }

  public double getIP()
  {
    return ds.getBigDecimal("IP").doubleValue();
  }

  public double getRSaldo()
  {
    return ds.getBigDecimal("SALDO").doubleValue();
  }
  
  public double getDEVID()
  {
    return ds.getBigDecimal("PVID").doubleValue();
  }

  public double getDEVIP()
  {
    return ds.getBigDecimal("PVIP").doubleValue();
  }

  public double getDEVSALDO()
  {
    return ds.getBigDecimal("PVSALDO").doubleValue();
  }
  
  public String getValuta()
  {
    return ds.getString("OZNVAL");
  }
  
  public String getSUMVAL()
  {
    return tot.svals;
  }
  
  public String getDEVIDSUM()
  {
    return tot.svalid;
  }

  public String getDEVIPSUM()
  {
    return tot.svalip;
  }

  public String getDEVSALDOSUM()
  {
    return tot.svalsal;
  }

  public String getVD() {
    if (raVrdokMatcher.isUplata(ds)) return "U";
    else if (raVrdokMatcher.isRacun(ds)) return "R";
    return "K";
  }

  public String getNaslov()
  {
    return "\nDEVIZNA KARTICA SALDA KONTI " + (up.isKupci() ? "KUPACA" : "DOBAVLJAÈA");
  }

  public int getPartner()
  {
    return tot.order;
  }

  public String getNazivPartnera() {
    ld.raLocate(dm.getPartneri(), "CPAR", String.valueOf(ds.getInt("CPAR")));
    return dm.getPartneri().getString("NAZPAR");
  }
  
  public String getPeriod()
  {
    return up.getPodnaslov();
  }

  public String getGlobPartner()
  {
    return (ds.getInt("CPAR")+"    "+getNazivPartnera());
  }
  public String getParAdress()
  {
    int cpar = ds.getInt("CPAR");
    // Nije potrebno ako se prvo zove getNazivPartnera(), jer se tamo pozicionira partner (ab.f)
//    lookupData.getlookupData().raLocate(dm.getPartneri(), new String[] {"CPAR"}, new String[] {""+cpar});
    return (dm.getPartneri().getInt("PBR") + " " +dm.getPartneri().getString("MJ")+
        ", "+dm.getPartneri().getString("ADR"));
  }

  public String getPartnerLab()
  {
    if (frmKarticaDev.getInstance().kupci)
      return ("Kupac ");
    return ("Dobavlja\u010D ");
  }

  public String getPartnerAdresa()
  {
    return (getGlobPartner()+"   "+getParAdress());
  }
  
  public String getKontoLab() {
    return up.jpk.getKonto().length() == 0 ? "" : "Konto";
  }
  
  public String getKonto() {
    return up.jpk.getKonto().length() == 0 ? "" : up.jpk.getKonto();
  }
  
  public String getFirstLine(){
    return rpm.getFirstLine();
  }
  public String getSecondLine(){
    return rpm.getSecondLine();
  }
  public String getThirdLine(){
    return rpm.getThirdLine();
  }
  public String getOneLine(){
    return rpm.getOneLine();
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(val.getToday());
  }
}
