/****license*****************************************************************
**   file: repStatSkladista.java
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

import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class repStatSkladista implements raReportData{

  _Main main;
  
  upProdajaPoDucanima uppd = upProdajaPoDucanima.getInstance();
  DataSet ds = uppd.getDataset();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repMemo re = repMemo.getrepMemo();
  public static double iznosGrupe;
  public static double sumIzGrupe;
  private static java.math.BigDecimal ruc;
  private static java.math.BigDecimal inab;
  private static int art;
  
  public repStatSkladista() {
    sumIzGrupe = 0;
    art = uppd.getWhatIsCart();
    ruc = new java.math.BigDecimal("0.00");
    inab = new java.math.BigDecimal("0.00");
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
  /*
    inab = inab.add(ds.getBigDecimal("INAB"));
    return ds.getBigDecimal("INAB").doubleValue();
  }

  public double getIZNOS() {
    return ds.getBigDecimal("IPRODBP").doubleValue();
  }

  public double getRUC() {
    ruc = ruc.add(ds.getBigDecimal("RUC"));*/
  
  
  public String getODDATUMA() {
    return rdu.dataFormatter(uppd.getPocDatum());
  }
  public String getDODATUMA() {
    return rdu.dataFormatter(uppd.getZavDatum());
  }
  public String getPodnaslov() {
    return getODDATUMA() + " do " + getDODATUMA();
  }
  public String getCSKL() {
    return ds.getString("CSKL");
  }
  public String getNAZSKL() {
//    lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL", ds.getString("CSKL"));
    return ds.getString("NAZSKL");
  }
  
  public String getCAGENT() {
    return "";//uppd.getCagent();
  }
  
  public String getNAZAGENT() {
    lookupData.getlookupData().raLocate(dm.getAgenti(),"CAGENT", uppd.getCagent());
    return "";//dm.getAgenti().getString("NAZAGENT");
  }
  
  public String getLABELAGENT() {
    if (uppd.getCagent().equalsIgnoreCase("")) return "";
    return "";//"Agent";
  }
  
  public String getCpar() {
    return uppd.getCpar();
  }

  public String getNazPar() {
    return uppd.getNazPar();
  }

  public String getCkup() {
    return uppd.getCkup();
  }
  public String getNazKupca() {
    if (!uppd.getPjCkup().equals("")){
      if (lookupData.getlookupData().raLocate(dm.getPjpar(),new String[] {"CPAR","PJ"},new String[] {uppd.getCkup(),uppd.getPjCkup()})){
        return uppd.getNazKup() + " poslovna jedinica " + dm.getPjpar().getString("NAZPJ") + " " + dm.getPjpar().getString("ADRPJ") + " " + dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ");
      }
    }
    return uppd.getNazKup();
  }
  
//  dm.createStringColumn("CSKL","Oznaka",12),
//  dm.createStringColumn("NAZSKL","Naziv",50),
//  dm.createBigDecimalColumn("INAB", "Nabavni iznos", 2), 
//  dm.createBigDecimalColumn("RUC", "Razlika u cijeni", 2), 
//  dm.createBigDecimalColumn("PRUC", "% RUC", 2), 
//  dm.createBigDecimalColumn("IPRODBP", "Prodajni iznos bez poreza", 2), 
//  dm.createBigDecimalColumn("POR", "Porez", 2), 
//  dm.createBigDecimalColumn("IPRODSP", "Prodajni iznos s porezom", 2)

  public double getINAB(){
    inab = inab.add(ds.getBigDecimal("INAB"));
    return ds.getBigDecimal("INAB").doubleValue();
  }
  public double getRUC(){
    ruc = ruc.add(ds.getBigDecimal("RUC"));
    return ds.getBigDecimal("RUC").doubleValue();
  }
  public double getPRUC(){
    return ds.getBigDecimal("PRUC").doubleValue();
  }
  public double getIPRODBP(){
    return ds.getBigDecimal("IPRODBP").doubleValue();
  }
  public double getPOR(){
    return ds.getBigDecimal("POR").doubleValue();
  }
  public double getIPRODSP(){
    return ds.getBigDecimal("IPRODSP").doubleValue();
  }

  public double getUkuPRUC(){
    double ukuPRUC = ruc.doubleValue()*100.00/inab.doubleValue();
    return ukuPRUC;
  }
  
  public String getLABELARTIKL(){
    if (art == 1) return "Artikl";
    if (art == 2) return "Grupa";
    if (art == 3) return "Dio šifre";
    return "";
  }

  public String getOZNAKAARTIKL(){
    if (art == 1) return uppd.rpcart.getCART();
    if (art == 2) return uppd.rpcart.getCGRART();
    if (art == 3) return uppd.rpcart.getCART1();
    return "";
  }

  public String getNAZIVARTIKL(){
    if (art == 1) return uppd.rpcart.getNAZART();
    if (art == 2) {
      if (lookupData.getlookupData().raLocate(dm.getGrupart(),"CGRART",uppd.rpcart.getCGRART())){
//        if (isp.rpcart.grupa????){
//
//        }
        return dm.getGrupart().getString("NAZGRART");
      }
    }
    if (art == 3) return "";
    return "";
  }

  public String getFirstLine(){
    return re.getFirstLine();
  }

  public String getSecondLine(){
    return re.getSecondLine();
  }

  public String getThirdLine(){
    return re.getThirdLine();
  }

  public String getLogoMjesto(){
    return re.getLogoMjesto();
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
  
  
}
