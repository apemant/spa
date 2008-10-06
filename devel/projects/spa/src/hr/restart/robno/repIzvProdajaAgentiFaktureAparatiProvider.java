/****license*****************************************************************
**   file: repIzvProdajaAgentiFaktureAparatiProvider.java
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
import hr.restart.util.VarStr;
import hr.restart.util.reports.raReportData;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;

import com.borland.dx.dataset.DataSet;


public class repIzvProdajaAgentiFaktureAparatiProvider implements raReportData {

  protected DataSet ds;
  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  protected repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  frmIzvProdajaAgentiFaktureAparati main = frmIzvProdajaAgentiFaktureAparati.getInstance();
  
  static HashMap agenti;
  static HashMap generalije;
  static HashMap rekapAparatz;
  
  static VarStr apars, kolic;
  
  public repIzvProdajaAgentiFaktureAparatiProvider() {
    ds = main.getReportSet();
    ru.setDataSet(ds);
    agenti = main.getAgente();
    generalije = main.getGeneralije();
    rekapAparatz = main.getRekapAparatz();
    makeRekapAparatStrings();
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.getRowCount();
  }

  public void close() {
    ds = null;
    ru.setDataSet(null);
  }
  
  public String getPodnaslov(){
    return "u periodu od " + rdu.dataFormatter((Timestamp)generalije.get("PDAT")) + " do " + rdu.dataFormatter((Timestamp)generalije.get("ZDAT"));
  }
  
  public String getNAZPAR(){
    return ds.getString("NAZPAR");
  }
  
  public String getAPARAT(){
//    return ds.getString("APARATI");
//    if (ds.getString("APARATI").equals("Nema aparata")) return "Nema aparata";
//   String vrati = "";
   VarStr razlom = new VarStr(ds.getString("APARATI"));
   razlom.replaceAll(", ",",\n");
   return razlom.toString(); 
  }
  
  public double getNOVI(){
    return ds.getBigDecimal("NOVI").doubleValue();
  }
  
  public double getPOTROSNI(){
    return ds.getBigDecimal("POTROSNI").doubleValue();
  }
  
  public double getUKUPNO(){
    return ds.getBigDecimal("UKUPNO").doubleValue();
  }
  public double getSVEUKUPNO(){
    return ds.getBigDecimal("SVEUKUPNO").doubleValue();
  }
  public double getPROSMJ(){
    return ds.getBigDecimal("PRPOMJ").doubleValue();
  }
  public String getKOD(){
    return ds.getString("KUPOD");
  }
  
  public String getCAGENT(){
    return ds.getString("CAGENT");
  }
  
  public String getNAZAGENT(){
    return agenti.get(ds.getString("CAGENT")).toString();
  }
  
  public String getRAP(){
    return apars.toString();
  }
  
  public String getKOLS(){
    return kolic.toString();
  }
  
  private void makeRekapAparatStrings(){
    String unszk = ""; 
    String unszv = "";
    for (Iterator iter = rekapAparatz.keySet().iterator(); iter.hasNext();) {
      Object ki = iter.next();
      unszk += ki.toString()+"\n";
      unszv += rekapAparatz.get(ki)+"\n";
    }
    
    apars = new VarStr(unszk);
    kolic = new VarStr(unszv);
  }
  
  public String getRekapAparatKol(){
    String unszv = ""; 
    return  unszv;
  }

  public String getFirstLine(){
    return rm.getFirstLine();
  }
  public String getSecondLine(){
    return rm.getSecondLine();
  }
  public String getThirdLine(){
    return rm.getThirdLine();
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}
