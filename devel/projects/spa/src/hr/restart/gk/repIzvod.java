/****license*****************************************************************
**   file: repIzvod.java
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
package hr.restart.gk;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repIzvod implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmIzvodi fizv = frmIzvodi.getFrmIzvodi();
  DataSet ds = fizv.getDetailSet();
  DataSet pp = gkQuerys.getParZiro();
  DataSet ms = fizv.getMasterSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  public static double iznosGrupe;
  public static int rb;

  public repIzvod() {
    if (!pp.isOpen()) pp.open();
    ru.setDataSet(ds);
  }

  public repIzvod(int idx) {
    if(idx==0){
      rb = 0;
    }
    ds.goToRow(idx);
    lookupData.getlookupData().raLocate(pp,new String[] {"CPAR"},new String[] {""+ds.getInt("CPAR")});
    lookupData.getlookupData().raLocate(ms,new String[] {"CNALOGA"},new String[] {ds.getString("CNALOGA")});
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
      }
      int indx=0;
      public Object nextElement() {

        return new repIzvod(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getNAZIVPARTNERA(){
    if (ds.getInt("CPAR")==0) return "";
    return pp.getString("NAZPAR");
  }

  public String getZIROPARTNERA(){
    if (ds.getInt("CPAR")==0) return "";
    return pp.getString("ZIRO");
  }

  public String getOPIS(){
    return ds.getString("OPIS");
  }

  public BigDecimal getID(){
    return ds.getBigDecimal("ID");
  }

  public BigDecimal getIP(){
    return ds.getBigDecimal("IP");
  }

  public String getBROJDOK(){
    if (ds.hasColumn("BROJDOK") != null)return ds.getString("BROJDOK");
    return raObrIzvoda.getBrojDok_GKS(ds)[0];
  }

  public String getEXTBROJDOK(){
    if (ds.hasColumn("BROJDOK") != null) return ds.getString("EXTBRDOK");
    return raObrIzvoda.getBrojDok_GKS(ds)[1];
  }

  public int getRBS(){
    return ds.getInt("RBS");
  }


  public String getNASLOV(){
    return "\nRA\u010CUN " + fizv.idizvod + ", IZVOD br. " + ms.getInt("BROJIZV");
  }

  public String getZaPeriod(){
    return "Datum " + rdu.dataFormatter(ms.getTimestamp("DATUM"));
  }

  public BigDecimal getPRETHODNOSTANJE(){
    return ms.getBigDecimal("PRETHSTANJE");
  }

  public BigDecimal getNOVOSTANJE(){
    return ms.getBigDecimal("NOVOSTANJE");
  }

  public BigDecimal getUkDnID(){
    return ms.getBigDecimal("ID");
  }

  public BigDecimal getUkDnIP(){
    return ms.getBigDecimal("IP");
  }

  public int getBROJSTAVKI(){
    return ms.getInt("BROJSTAVKI");
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