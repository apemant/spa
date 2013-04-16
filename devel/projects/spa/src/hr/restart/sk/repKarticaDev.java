/****license*****************************************************************
**   file: repKarticaDev.java
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
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;


public class repKarticaDev implements raReportData {
  DataSet ds;
  
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  String[] colname = new String[] {""};
  
  

  public repKarticaDev() {
//     ru.setDataSet(ds);
    ds = frmKarticaDev.getInstance().getRaQueryDataSet();
    frmKarticaDev.getInstance().getJpTableView().enableEvents(false);
    frmKarticaDev.getInstance().findTotals(ds);
  }

  public raReportData getRow(int idx) {
    ds.goToRow(idx);
    return this;
  }
  
  public int getRowCount() {
    return ds.getRowCount();
  }

  public void close() {
    frmKarticaDev.getInstance().getJpTableView().enableEvents(true);
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
    return ds.getBigDecimal("ID").signum() != 0 
       ? ds.getBigDecimal("SALDO").doubleValue()
       : ds.getBigDecimal("SALDO").negate().doubleValue();
  }
  
  public double getDEVID()
  {
    if (raSaldaKonti.isDomVal(ds)) return 0;
    return ds.getBigDecimal("PVID").doubleValue();
  }

  public double getDEVIP()
  {
    if (raSaldaKonti.isDomVal(ds)) return 0;
    return ds.getBigDecimal("PVIP").doubleValue();
  }

  public double getDEVSALDO()
  {
    if (raSaldaKonti.isDomVal(ds)) return 0;
    return ds.getBigDecimal("PVID").signum() != 0 
    ? ds.getBigDecimal("PVSALDO").doubleValue()
    : ds.getBigDecimal("PVSALDO").negate().doubleValue();
  }
  
  public String getValuta()
  {
    return ds.getString("OZNVAL");
  }
  
  public String getSUMVAL()
  {
    return frmKarticaDev.getInstance().getSUMVAL();
  }
  
  public String getDEVIDSUM()
  {
    return frmKarticaDev.getInstance().getDEVIDSUM();
  }

  public String getDEVIPSUM()
  {
    return frmKarticaDev.getInstance().getDEVIPSUM();
  }

  public String getDEVSALDOSUM()
  {
    return frmKarticaDev.getInstance().getDEVSALDOSUM();
  }

  public String getVD() {
    if (raVrdokMatcher.isUplata(ds)) return "U";
    else if (raVrdokMatcher.isRacun(ds)) return "R";
    return "K";
  }

  public String getNaslov()
  {
    return frmKarticaDev.getInstance().getNaslov();
  }

  public int getPartner()
  {
    return ds.getInt("CPAR");
  }

  public String getNazivPartnera() {
    ld.raLocate(dm.getPartneri(), "CPAR", String.valueOf(ds.getInt("CPAR")));
    return dm.getPartneri().getString("NAZPAR");
  }
  
  public String getPeriod()
  {
    return frmKarticaDev.getInstance().getPeriod();
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
    return frmKarticaDev.getInstance().konto == null || 
      frmKarticaDev.getInstance().konto.length() == 0 ? "" : "Konto";
  }
  
  public String getKonto() {
    return frmKarticaDev.getInstance().konto == null ? "" : frmKarticaDev.getInstance().konto;
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
