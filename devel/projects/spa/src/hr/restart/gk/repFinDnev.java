/****license*****************************************************************
**   file: repFinDnev.java
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

import hr.restart.robno._Main;
import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class repFinDnev implements raReportData { //implements
                                                  // sg.com.elixir.reportwriter.datasource.IDataProvider
                                                  // {

  _Main main;
  DataSet ds = frmFinDnev.getQds();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  int rowCount = 0;
  
  public repFinDnev() {
    ru.setDataSet(ds);
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

  public String getCOrg() {
    return ds.getString("corg");
  }

  public int getRBS() {
    return ds.getInt("RBS");
  }

  public double getIP() {
    return ds.getBigDecimal("IP").doubleValue();
  }

  public double getID() {
    return ds.getBigDecimal("ID").doubleValue();
  }

  public String getBrojKonta() {
    return ds.getString("BROJKONTA");
  }

  public String getCaption1() {
    String s1 = ds.getString("CNALOGA");
    String s2 = rdu.dataFormatter(ds.getTimestamp("DATUMKNJ"));
    return "Br. " + s1 + " od " + s2;
  }

  public String getOpis() {
    return ds.getString("OPIS");
  }

  public String getPotrazni() {
    return "POTRAŽNI";
  }

  public String getDugovni() {
    return "DUGOVNI";
  }

  public String getDonos() {
    return "Donos:";
  }

  public String getCNaloga() {
    return ds.getString("CNALOGA");
  }

  public String getFake() {
    return "a";
  }

  public int getRowNum() {
    return rowCount;
  }

  public String getCaption() {                                                                            // needed
    return rdu.dataFormatter(frmFinDnev.datumOd) + " do " + rdu.dataFormatter(frmFinDnev.datumDo);
  }

  public String getSumLabela() {
    return "S V E U K U P N O";
  }

  public double getDonDug() {
    return frmFinDnev.sume[0];
  }

  public double getDonPot() {
    return frmFinDnev.sume[1];
  }

  public double getSveDug() {
    return frmFinDnev.sume[2];
  }

  public double getSvePot() {
    return frmFinDnev.sume[3];
  }

  public int getDummy() {
    return 1;
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