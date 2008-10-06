/****license*****************************************************************
**   file: repFormatNorme.java
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
import hr.restart.util.reports.raReportData;

import java.util.HashMap;

import com.borland.dx.dataset.DataSet;

public class repFormatNorme implements raReportData {

  protected DataSet ds = null;
  frmNorme frn = frmNorme.getInstance();
  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  protected repUtil ru = repUtil.getrepUtil();
  HashMap hm = null;
  
  public repFormatNorme(){
    ds = frn.getRepSet();
    hm = frn.getNaNorm();
    ru.setDataSet(ds);
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
    hm = null;
    ru.setDataSet(null);
  }
  
  public int getCARTNOR(){
    return ds.getInt("CARTNOR");
  }
  
  public String getNORMATIV(){
    return hm.get(ds.getInt("CARTNOR")+"").toString();
  }
  
  public String getCNORMA(){
    return Aut.getAut().getIzlazCARTdep(ds);
  }
  
  public String getNAZART(){
    return ds.getString("NAZART");
  }
  
  public String getJM(){
    return ds.getString("JM");
  }
  
  public double getKOL(){
    return ds.getBigDecimal("KOL").doubleValue();
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
