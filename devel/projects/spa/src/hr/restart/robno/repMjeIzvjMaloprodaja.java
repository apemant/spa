/****license*****************************************************************
**   file: repMjeIzvjMaloprodaja.java
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

public class repMjeIzvjMaloprodaja implements raReportData {

  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  reportsQuerysCollector repQC = reportsQuerysCollector.getRQCModule();
  DataSet ds;
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  static double _RUC, _UNABVRI, _UKUPOSTORUC;

  public repMjeIzvjMaloprodaja() {
    _RUC = 0.0;
    _UNABVRI = 0.0;
    _UKUPOSTORUC = 0.0;
    ds = raUpitMjeIzvjMal.getInstance().getQds();
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
    lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", ds.getString("CSKL"));
    return dm.getSklad().getString("NAZSKL");

  }

  public double getSTZALNC(){
     return ds.getBigDecimal("STZALNC").doubleValue();
  }

  public double getUPVNOPDV(){
     return ds.getBigDecimal("UPVNOPDV").doubleValue();
  }

  public double getUNABVRI(){
    _UNABVRI = _UNABVRI + ds.getBigDecimal("UNABVRI").doubleValue();
     return ds.getBigDecimal("UNABVRI").doubleValue();
  }

  public double getPROVKART(){
     return ds.getBigDecimal("PROVKART").doubleValue();
  }

  public double getRUC(){
    _RUC = _RUC + ds.getBigDecimal("RUC").doubleValue();
     return ds.getBigDecimal("RUC").doubleValue();
  }

  public double getPRUC(){
     return ds.getBigDecimal("PRUC").doubleValue();
  }

  public double getUKUPOSTORUC(){
    _UKUPOSTORUC = (_RUC/_UNABVRI)*100;
     return _UKUPOSTORUC;
  }

  public double getUKUTR(){
     return ds.getBigDecimal("UKUTR").doubleValue();
  }/*

  public String getDATUMOD(){
     return rdu.dataFormatter(raUpitMjeIzvjMal.getInstance().getDATUMOD());
  }

  public String getDATUMDO(){
     return rdu.dataFormatter(raUpitMjeIzvjMal.getInstance().getDATUMDO());
  }*/

  public String getPeriod(){
    return "  od "+rdu.dataFormatter(raUpitMjeIzvjMal.getInstance().getDATUMOD())+
           ". do "+rdu.dataFormatter(raUpitMjeIzvjMal.getInstance().getDATUMDO())+".";
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