/****license*****************************************************************
**   file: repPregledR1Maloprodaja.java
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

import com.borland.dx.dataset.DataSet;

/**
 * @author S.G.
 *
 * Started 2005.02.11
 * 
 */

public class repPregledR1Maloprodaja implements raReportData {

  UpPregledR1Maloprodaja pr1 = UpPregledR1Maloprodaja.getInstance();
  DataSet ds = null;
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  
  public repPregledR1Maloprodaja(){
    ds = pr1.getReportSet();
  }
  
  public void close() {
    ds=null;
  }
  
  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }
  
  public int getRowCount() {
    return ds.rowCount();
  }
  
  public String getPodnaslov(){
    return "od "+rdu.dataFormatter(pr1.getDatumOd())+" do "+rdu.dataFormatter(pr1.getDatumDo());
  }
  
  public String getCSKL(){
    return pr1.getCSKL();
  }
  
  public String getNazSklad(){
    return pr1.getNazSkl();
  }
  
  public String getDATDOK(){
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }
  
  public String getBROJ() {
    return ds.getString("CSKL") + "-" + ds.getString("VRDOK") + "-" +
    ds.getString("GOD") + "-" + vl.maskZeroInteger(Integer.valueOf(String.valueOf(ds.getInt("BRDOK"))), 4);
  }
  
  public String getNAZIVKUPCA(){
    
    return ds.getString("IME")+ " " + ds.getString("PREZIME");
  }
  
  public String getJMBG(){
    try {
      return ds.getString("JMBG");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }
  
  public double getPBP(){
    return ds.getBigDecimal("IPRODBP").doubleValue();
  }
  
  public double getPSP(){
    return ds.getBigDecimal("IPRODSP").doubleValue();
  }
  
  public double getPOR(){
    return ds.getDouble("IPOR");
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
    return rdu.dataFormatter(Valid.getValid().getToday());
  }
}
