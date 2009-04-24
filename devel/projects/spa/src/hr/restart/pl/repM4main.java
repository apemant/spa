/****license*****************************************************************
**   file: repM4main.java
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
package hr.restart.pl;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repM4main implements raReportData { // sg.com.elixir.reportwriter.datasource.IDataProvider {

  frmM4 fm4 = frmM4.getInstance();
  DataSet ds = fm4.getRep01Set();
  DataSet ps = fm4.getPodaciSet();

//  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

//  repUtil ru = repUtil.getrepUtil();
//  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repM4main() {
//    ru.setDataSet(ds);
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

//  public repM4main(int idx) {
//    if (idx == 0){}
//    ds.goToRow(idx);
//  }
//
//  public java.util.Enumeration getData() {
//    return new java.util.Enumeration() {
//    {
//      ds.open();
//    }
//    int indx=0;
//    public Object nextElement() {
//
//      return new repM4main(indx++);
//    }
//    public boolean hasMoreElements() {
//      return (indx < ds.getRowCount());
//    }
//    };
//  }
//
//  public void close() {
//  }
  public String getTITLE() {
    return "PRIJAVA O UTVRÐENOM STAŽU I PLAÆI, OSNOVICAMA OSIGURANJA I NAKNADAMA PLAÆE ZA GODINU "+getGODINA();
  }
  public String getCRADNIKA(){
    return ds.getString("CRADNIK");
  }

  public String getRADNIK(){
     return ds.getString("PREZIME").concat(" "+ds.getString("IME"));
  }

  public String getRADNOMJ(){
     return ds.getString("NAZIVRM");
  }

  public String getREGBRMIO(){
     return ds.getString("REGBRMIO");
  }

  public String getJMBG(){
     return ds.getString("JMBG");
  }

  public String getORGREGMIO(){
     return ds.getString("ORGREGMIO");
  }

  public short getGODSTAZ(){
     return ds.getShort("GODSTAZ");
  }

  public int getBENEFICIRANI(){
    return ds.getInt("BENSTAZ");
  }

  public BigDecimal getBRUTOOSN(){
     return ds.getBigDecimal("BRUTO");
  }

  public BigDecimal getHZZO(){
     return ds.getBigDecimal("BRUTO1");
  }

  public BigDecimal getRH(){
     return ds.getBigDecimal("BRUTO2");
  }

  public BigDecimal getSS(){
     return ds.getBigDecimal("BRUTO3");
  }

  public short getGODINA(){
    return fm4.getGodina();
  }

  public String getMB(){
    return ps.getString("MATBROJ");
  }

  public String getSD(){
    return ps.getString("SIFDJEL");
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

  public String getSecondAndThirdLine(){
    return re.getSecondLine().concat(", "+re.getThirdLine());
  }

  public String getDatumIspisa(){
    return rdu.dataFormatter(vl.getToday());
  }
}