/****license*****************************************************************
**   file: repRekapTrosPP.java
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

import com.borland.dx.dataset.DataSet;

public class repRekapTrosPP implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
//  DataSet ds = upPregledTroskova.getupPregletTroskova().getReportQDS();

  upRekapTros urt = upRekapTros.getInstance();
  DataSet ds = urt.getRepStDS();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  public static double iznosGrupe;
  public static double sumIzGrupe;

  public repRekapTrosPP() {
  }

  public repRekapTrosPP(int idx) {
    if(idx==0){
      sumIzGrupe = 0;
    }
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

      return new repRekapTrosPP(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }

  public void close() {
  }

  public String getCORG(){
     return ds.getString("CORG");
  }

  public String getNAZORG(){
    lookupData.getlookupData().raLocate(dm.getOrgstruktura(),new String[] {"CORG"}, new String[] {ds.getString("CORG")});
    return dm.getOrgstruktura().getString("NAZIV");
  }

  public double getPRVI(){
     return ds.getBigDecimal("MJESEC01").doubleValue();
  }

  public double getDRUGI(){
     return ds.getBigDecimal("MJESEC02").doubleValue();
  }

  public double getTRECI(){
     return ds.getBigDecimal("MJESEC03").doubleValue();
  }

  public double getCETVRTI(){
     return ds.getBigDecimal("MJESEC04").doubleValue();
  }

  public double getPETI(){
     return ds.getBigDecimal("MJESEC05").doubleValue();
  }

  public double getSESTI(){
     return ds.getBigDecimal("MJESEC06").doubleValue();
  }

  public double getPPGOD(){
     return ds.getBigDecimal("PPGOD").doubleValue();
  }

  public String getPodnaslov(){
    return "za prvu polovicu ".concat(urt.getGodinu().concat(". godine"));
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

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }

}