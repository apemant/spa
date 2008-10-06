/****license*****************************************************************
**   file: repStatParArt.java
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

import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repStatParArt implements raReportData {

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  ispStatPar isa2 = ispStatPar.getInstance();
  DataSet ds;
//  DataSet pds;
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();

  public repStatParArt() {
    ds = isa2.getReportQDS();
//    pds = isa2.getReportQDS();
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(ds);
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ds = null;
  }

  public int getCPAR(){
    return ds.getInt("CPAR");
  }

  public String getNAZPAR(){
     return ds.getString("NAZPAR");
  }

  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
  }

  public String getNAZART() {
    return ds.getString("NAZART");
  }

  public String getCSKL() {
    return ds.getString("CSKL");
  }


  public String getNAZSKL() {
    lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL", ds.getString("CSKL"));
    return dm.getSklad().getString("NAZSKL");
  }

  public String getJM() {
    return ds.getString("JM");
  }

  public double getKOL() {
    return ds.getBigDecimal("KOL").doubleValue();
  }

  public double getIRAZ() {
    return ds.getBigDecimal("IRAZ").doubleValue();
  }

  public double getINAB() {
    return ds.getBigDecimal("INAB").doubleValue();
  }

  public double getRUC() {
    return ds.getBigDecimal("RUC").doubleValue();
  }

  public double getNETTO() {
    return (ds.getBigDecimal("INAB").add(ds.getBigDecimal("RUC"))).doubleValue();
  }


  public BigDecimal getPostoRUC() {
    return ds.getBigDecimal("PostoRUC");
//    double prac = ds.getBigDecimal("RUC").doubleValue()/ds.getBigDecimal("INAB").doubleValue() * 100;
//    return prac;
  }

  public BigDecimal getPrucCart(){
    if (lookupData.getlookupData().raLocate(isa2.getPrucQDS(),"CPAR",ds.getInt("CPAR")+"")) return isa2.getPrucQDS().getBigDecimal("PostoRUC");
    return Aus.zero2;
  }

  public double getpostoSumRuc() {
    return isa2.getPostoSumRuc();
  }

//  public BigDecimal getPostoRUC_BD() {
//    java.math.BigDecimal prac = ds.getBigDecimal("RUC").divide(ds.getBigDecimal("INAB"),4,java.math.BigDecimal.ROUND_HALF_UP).multiply(new java.math.BigDecimal("100,00"));
//    return prac;
//  }

  public double getIPRODBP() {
    return ds.getBigDecimal("IPRODBP").doubleValue();
  }

  public double getIPRODSP() {
    return ds.getBigDecimal("IPRODSP").doubleValue();
  }

  public double getPOR() {
    return ds.getBigDecimal("POR").doubleValue();
  }

  public String getODDATUMA() {
    return rdu.dataFormatter(isa2.getPocDatum());
  }

  public String getDODATUMA() {
    return rdu.dataFormatter(isa2.getZavDatum());
  }

  public String getPodnaslov() {
    return "Za razdoblje od " + getODDATUMA() + " do " + getDODATUMA();
  }

  public String getPoCemu() {
    return "\nPO ARTIKLIMA";
  }

  public String getCpar() {
    return isa2.getCpar();
  }

  public String getNazPar() {
    return isa2.getNazPar();
  }

  public String getCkup() {
    return isa2.getCkup();
  }

  public String getNazKupca() {
//    if (!isa2.getPjCkup().equals("")){
//      if (lookupData.getlookupData().raLocate(dm.getPjpar(),new String[] {"CPAR","PJ"},new String[] {isa2.getCkup(),isa2.getPjCkup()})){
//        return isa2.getNazKup() + " poslovna jedinica " + dm.getPjpar().getString("NAZPJ") + " " + dm.getPjpar().getString("ADRPJ") + " " + dm.getPjpar().getString("PBRPJ") + " " + dm.getPjpar().getString("MJPJ");
//      }
//    }
    return isa2.getNazKup();
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

  public String getLogoMjesto(){
    return rpm.getLogoMjesto();
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}
