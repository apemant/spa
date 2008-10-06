/****license*****************************************************************
**   file: repM4tables.java
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
import hr.restart.util.sysoutTEST;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repM4tables implements raReportData {

  frmM4 fm4 = frmM4.getInstance();
  DataSet ds = fm4.getRep02Set();
  DataSet ps = fm4.getPodaciSet();
  DataSet ss = fm4.getSum01Set();

//  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

//  repUtil ru = repUtil.getrepUtil();
//  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repM4tables() {
    sysoutTEST syst = new sysoutTEST(false);
    syst.prn(ds);
    syst.prn(ps);
    syst.prn(ss);
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

  public short getMJESEC(){
    return ds.getShort("MJOBR");
  }

  public String getDATISPL(){
    return rdu.dataFormatter(ds.getTimestamp("DATUMISPL"));
  }

  public BigDecimal getBRUTPL_I(){
     return ds.getBigDecimal("BRUTO_I");
  }


  public BigDecimal getBRUTPL_II(){
     return ds.getBigDecimal("BRUTO_II");
  }

  public BigDecimal getDOPRINOSI_I(){
    return ds.getBigDecimal("UIZNDOP_I");
  }

  public BigDecimal getDOPRINOSI_II(){
    return ds.getBigDecimal("UIZNDOP_II");
  }

//  public BigDecimal getHZZO(){
//     return ds.getBigDecimal("BRUTO1");
//  }
//
//  public BigDecimal getRH(){
//     return ds.getBigDecimal("BRUTO2");
//  }
//
//  public BigDecimal getSS(){
//     return ds.getBigDecimal("BRUTO3");
//  }

  public BigDecimal getSumBRUTPL_I(){
     return ss.getBigDecimal("BRUTO_I");
  }

  public BigDecimal getSumBRUTPL_II(){
     return ss.getBigDecimal("BRUTO_II");
  }

  public BigDecimal getSumBRUTPL(){
     return ss.getBigDecimal("BRUTO_I").add(ss.getBigDecimal("BRUTO_II"));
  }

  public BigDecimal getSumDOPRINOSI_I(){
    return ss.getBigDecimal("UIZNDOP_I");
  }

  public BigDecimal getSumDOPRINOSI_II(){
    return ss.getBigDecimal("UIZNDOP_II");
  }

  public BigDecimal getSumDOPRINOSI(){
    return ss.getBigDecimal("UIZNDOP_I").add(ss.getBigDecimal("UIZNDOP_II"));
  }

  public BigDecimal getSumHZZO(){
     return ss.getBigDecimal("BRUTO1");
  }

  public BigDecimal getSumRH(){
     return ss.getBigDecimal("BRUTO2");
  }

  public BigDecimal getSumSS(){
     return ss.getBigDecimal("BRUTO3");
  }

  public String getMB(){
    return ps.getString("MATBROJ");
  }

  public String getSD(){
    return ps.getString("SIFDJEL");
  }

  public String getZIRORACUN(){
    return ps.getString("ZIRO");
  }

  public String getREGBRMIO(){
    return fm4.getRBMIO();
  }

  public short getGODINA(){
    return fm4.getGodina();
  }

  public String getZaMjesec(){
    String mj;
    if (ds.getShort("MJOBR") < 10){
      mj = "0".concat(String.valueOf(ds.getShort("MJOBR")));
      return mj + "/" + fm4.getGodina();
    }
    mj = String.valueOf(ds.getShort("MJOBR"));
    return mj + "/" + fm4.getGodina();
  }

  public String getNaslov(){
    return "REKAPITULACIJA ISPLA\u0106ENIH PLA\u0106A, NAKNADA I DOPRINOSA ZA MIROVINSKO OSIGURANJE \nNA TEMELJU GENERACIJSKE SOLIDARNOSTI ZA GODINU " + fm4.getGodina() + ".";
  }

  public String getNaslovT2(){
    return "REKAPITULACIJA PODATAKA SADRŽANIH NA PRIJAVAMA M-4P ZA " + fm4.getGodina() + ". GODINU";
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