/****license*****************************************************************
**   file: repPregledRacunaTbl.java
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
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repPregledRacunaTbl implements raReportData {

  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  //test
  frmPregledRacunaTbl prt = frmPregledRacunaTbl.getInstance();
  //test
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  reportsQuerysCollector repQC = reportsQuerysCollector.getRQCModule();
  DataSet ds;
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();

  public repPregledRacunaTbl() {
    //test
    ds = raUpitRekapProd.getInstance().getQds();
    //test
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(ds);
//    ds = repQC.getQueryDataSet();
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    ds = null;
  }

  public String getCSKL(){
     return ds.getString("CSKL");
  }

  public String getPRODMJ(){
//    lookupData.getlookupData().raLocate(dm.getOrgstruktura(), "CORG", ds.getString("CSKL"));
//    return dm.getOrgstruktura().getString("NAZIV");
    lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", ds.getString("CSKL"));
    return dm.getSklad().getString("NAZSKL");
  }

  public double getIZNOSRACUNA(){
     return ds.getBigDecimal("IZNOSRACUNA").doubleValue();
  }

  public double getVIRMAN(){
     return ds.getBigDecimal("VIRMAN").doubleValue();
  }

  public double getCEK(){
     return ds.getBigDecimal("CEK").doubleValue();
  }

  public double getKREDKART(){
     return ds.getBigDecimal("KREDKART").doubleValue();
  }

  public String getDATUMOD(){
     return rdu.dataFormatter(ds.getTimestamp("DATOD"));
  }

  public String getDATUMDO(){
     return rdu.dataFormatter(ds.getTimestamp("DATDO"));
  }



  public String getODDO(){
    return "Od "+ rdu.dataFormatter(ds.getTimestamp("DATOD"))+ " do "+rdu.dataFormatter(ds.getTimestamp("DATDO"));
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