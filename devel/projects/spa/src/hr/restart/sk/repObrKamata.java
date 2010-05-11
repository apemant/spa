/****license*****************************************************************
**   file: repObrKamata.java
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
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repObrKamata implements raReportData {

  raObrKamata rok = raObrKamata.getInstance();
  DataSet ds = rok.getRepQDS();

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repObrKamata() {
  }

  public raReportData getRow(int idx) {
    ds.goToRow(idx);
    return this;
  }
  
  public int getRowCount() {
    return ds.getRowCount();
  }

  public void close() {
  }

  public int getCPAR() {
    return ds.getInt("CPAR");
  }

  public String getNAZPAR() {
    lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", String.valueOf(ds.getInt("CPAR")));
    return getCPAR() + "  " + dm.getPartneri().getString("NAZPAR") + ", " + dm.getPartneri().getString("ADR") + " " + dm.getPartneri().getInt("PBR")+ " " + dm.getPartneri().getString("MJ")+"; OIB:" + dm.getPartneri().getString("OIB");
  }

  public int getRBR() {
    return ds.getInt("RBR");
  }

  public int getLRBR() {
    return ds.getInt("LRBR");
  }

  public String getRANGE() {
    String[] met = {"konformna metoda", 
        "proporcionalna metoda", "kombinirana(*) metoda"};
    return "Za period od "+rdu.dataFormatter(rok.getDATUMOD())+
           " do "+rdu.dataFormatter(rok.getDATUMDO()) + "\n" + met[rok.obrMetoda-1];
  }
  
  public String getNAPOM() {
    return rok.obrMetoda != rok.METHOD_COMBINED ? "" :
      "(*) kombinirana metoda: konformna za kašnjenje do godine dana, iza toga proporcionalna.";
  }

  public String getRBROJRAC(){
    return ds.getString("BROJDOK");
  }

  public String getBROJRAC(){
    return ds.getString("FLAG").equals("R") ? ds.getString("BROJDOK") : "";
  }

  public String getBROJUPL(){
     return !ds.getString("FLAG").equals("O") ? ds.getString("BROJUPL") : "";
  }

  public String getDATDOSP() {
    return rdu.dataFormatter(ds.getTimestamp("DATDOSP"));
  }

  public String getDATPLAC() {
    return rdu.dataFormatter(ds.getTimestamp("DATPLAC"));
  }

  public String getKASNI() {
    return ds.getInt("DANA") < 0 ? "" : Integer.toString(ds.getInt("DANA"));
  }

  public BigDecimal getSTOPA() {
    return ds.getBigDecimal("STOPA");
  }

  public BigDecimal getKOEF() {
    return ds.getBigDecimal("KOEF").setScale(10, BigDecimal.ROUND_HALF_UP);
  }

  public double getGLAVNICA() {
    return ds.getBigDecimal("GLAVNICA").doubleValue();
  }

  public double getKAMATA() {
    return ds.getBigDecimal("KAMATA").doubleValue();
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

