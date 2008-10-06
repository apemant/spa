/****license*****************************************************************
**   file: repTemeljSK.java
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

import hr.restart.util.lookupData;

import com.borland.dx.dataset.DataSet;

public class repTemeljSK implements sg.com.elixir.reportwriter.datasource.IDataProvider {
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  String[] colname = new String[]{""};
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  DataSet ds = frmNalozi.getFrmNalozi().getStavkeSK();

  hr.restart.robno._Main main;
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  hr.restart.robno.repMemo rpm = hr.restart.robno.repMemo.getrepMemo();
  hr.restart.robno.repUtil ru = hr.restart.robno.repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();

  public repTemeljSK() {
    ru.setDataSet(ds);
  }

  public repTemeljSK(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return
    new java.util.Enumeration() {
    {
      ds.open();
    }
    int indx = 0;
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    public Object nextElement() {

      return new repTemeljSK(indx++);
    }
    };
  }

  public void close() {}

 public String getCaption1() {
  String s1 = frmNalozi.getFrmNalozi().getRepMasterSet().getString("CNALOGA");
//  String s2 = rdu.dataFormatter(ds.getTimestamp("DATUMKNJ"));
  return "Br. " + s1 + " do " + rdu.dataFormatter(frmNalozi.getFrmNalozi().getDatumKnjizenja());
 }

  public String getBROJKONTA(){
     return ds.getString("BROJKONTA");
  }

  public String getBROJDOK(){
     return ds.getString("BROJDOK");
  }

  public String getNAZIVKONTA(){
    String naziv = "";
    if (lookupData.getlookupData().raLocate(dm.getKonta(),"BROJKONTA",ds.getString("BROJKONTA"))){
      naziv = dm.getKonta().getString("NAZIVKONTA");
    }
    return naziv;
  }

  public String getEXTBR(){
     return ds.getString("EXTBRDOK");
  }

  public int getCPAR(){
    return ds.getInt("CPAR");
  }

  public String getNAZPAR(){
    lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",String.valueOf(ds.getInt("CPAR")));
    return dm.getPartneri().getString("NAZPAR");
  }

  public String getDATDOK(){
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }

//  public BigDecimal getID(){
//     return ds.getBigDecimal("ID");
//  }
//
//  public BigDecimal getIP(){
//     return ds.getBigDecimal("IP");
//  }

  public double getIZNOS(){
    return (ds.getBigDecimal("ID").add(ds.getBigDecimal("IP"))).doubleValue();
  }

  public String getFirstLine() {
    return rpm.getFirstLine();
  }

  public String getSecondLine() {
    return rpm.getSecondLine();
  }

  public String getThirdLine() {
    return rpm.getThirdLine();
  }

 public String getDatumIsp() {
  return rdu.dataFormatter(val.getToday());
 }
}