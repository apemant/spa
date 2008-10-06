/****license*****************************************************************
**   file: repKarticaKupcaArtikl.java
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
import hr.restart.util.sysoutTEST;
import hr.restart.util.reports.raReportData;
import hr.restart.util.reports.raStringCache;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

/**
 * @author S.G.
 *
 * Started 2005.05.17
 * 
 */

public class repKarticaKupcaArtikl implements hr.restart.util.reports.raReportData{

  DataSet ds;
  upKarticaKupca upk = upKarticaKupca.getInstance();
  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  protected raStringCache cache = new raStringCache();
  
  public repKarticaKupcaArtikl(){
    ds = upk.getReportSet();
    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
    st.prn(ds);
  }

  public void close() {
    ds = null;
  }
  
  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }
  
  public int getRowCount() {
    return ds.rowCount();
  }
  
  /*
                     (Column) dm.getStdoki().getColumn("CART").clone(),
                     (Column) dm.getStdoki().getColumn("CART1").clone(),
                     (Column) dm.getStdoki().getColumn("BC").clone(),
                     (Column) dm.getStdoki().getColumn("NAZART").clone(),2
                     (Column) dm.getStdoki().getColumn("KOL").clone(),
                     (Column) dm.getStdoki().getColumn("VC").clone()*/

  public String getCART() {
    String izlaz = upk.getCart();
    if (hr.restart.sisfun.frmParam.getParam("robno","chForSpecial","N","Provjerava specijalne znakove (*,_,+,-) i odrezuje njih plus sve iza njih").equalsIgnoreCase("D")){
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
  
  public String getNAZART() {
    return  upk.getNazart();
  }
  
  public String getCPAR() {
    return upk.getCpar();
  }
  
  public String getPeriod(){
    return "U periodu od "+rdu.dataFormatter(upk.getOdDatuma())+" do " + rdu.dataFormatter(upk.getDoDatuma()); 
  }

  public String getPARTNER(){   
    String cached = cache.getValue("NAZPAR", upk.getCpar());
    if (cached != null) return cached;
    colname[0] = "CPAR";
    String np;
    try {
    np = ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString();
    } catch (Exception e) {
//      e.printStackTrace();
      return upk.getNazPar();
    }
    return cache.returnValue(np);
  }
  
  public String getDATUMDOK(){
   return rdu.dataFormatter(ds.getTimestamp("DATDOK")); 
  }
  
  public int getBRDOK(){
    return ds.getInt("BRDOK");
  }
  
  public String getVRDOK(){
    return ds.getString("VRDOK");
  }
  
  public double getKOL(){
    return ds.getBigDecimal("KOL").doubleValue();
  }
  
  public BigDecimal getVC(){
    return ds.getBigDecimal("VC");
  }
  
  public double getIZNOS(){
    return (ds.getBigDecimal("KOL").multiply(ds.getBigDecimal("VC"))).doubleValue();
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
}



/*package hr.restart.robno;

public class repKarticaKupcaArtikl {

}
*/