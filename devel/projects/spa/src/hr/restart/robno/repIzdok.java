/****license*****************************************************************
**   file: repIzdok.java
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

import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;

public class repIzdok implements raReportData {

//  _Main main;
//  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  DataSet ds = upUlazIzlaz.getInstance().getQds();
  raDateUtil rdu = raDateUtil.getraDateUtil();
//  String[] colname = new String[] {""};
//  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();

  public repIzdok() {
//    upUlazIzlaz.getInstance().getJPTV().enableEvents(false);
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.getRowCount();
  }

  public void close() {
//    upUlazIzlaz.getInstance().getJPTV().enableEvents(true);
    ds = null;
  }

  public Timestamp getPocDatum() {
    return upUlazIzlaz.getInstance().getPocDatum();
  }

  public Timestamp getZavDatum() {
    return upUlazIzlaz.getInstance().getZavDatum();
  }

  public String getRANGE() {
    return rdu.dataFormatter(getPocDatum()) + " - " + rdu.dataFormatter(getZavDatum());
  }

  public String getLABELCSKL() {
    return upUlazIzlaz.getInstance().getAllLabels();
  }

  public String getCSKL() {
    return upUlazIzlaz.getInstance().getAllSifre();
  }

  public String getNAZSKL() {
    return upUlazIzlaz.getInstance().getAllOpisi();
  }

//  public String getNAZPAR() {
//    return (upUlazIzlaz.getInstance().getNazPar().equals("") ? "ZA SVE PARTNERE" :
//            upUlazIzlaz.getInstance().getNazPar());
//  }
//
//  public String getCPAR() {
//    return upUlazIzlaz.getInstance().getCpar();
//  }

/*  public Timestamp getDATDOK() {
    System.out.println(ds.getTimestamp("DATDOK"));
    return ds.getTimestamp("DATDOK");
  } */

  public String getDATDOK() {
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }

  public String getVRDOK() {
    return ds.getString("VRDOK");
  }

  public String getBRDOK() {
    return "" + ds.getInt("BRDOK");
  }

  public double getIPRODSP() {
    return ds.getBigDecimal("IPRODSP").doubleValue();
  }

  public double getPorez() {
    return ds.getBigDecimal("POREZ").doubleValue();
  }
  public double getIPRODBP() {
    return ds.getBigDecimal("IPRODBP").doubleValue();
  }
  public double getUIRAB() {
    return ds.getBigDecimal("UIRAB").doubleValue();
  }
  public double getIZAR() {
    return ds.getBigDecimal("ZARADA").doubleValue();
  }
  public double getINAB() {
    return ds.getBigDecimal("INAB").doubleValue();
  }
  public double getIMAR() {
    return ds.getBigDecimal("IMAR").doubleValue();
  }
  public double getIPOR() {
    return ds.getBigDecimal("IPOR").doubleValue();
  }
  public double getIRAZ() {
    return ds.getBigDecimal("IRAZ").doubleValue();
  }

//  public int getLogo() {
//    if (test()) return 1;
//    else return 0;
//  }

  public String getFirstLine(){
    return rpm.getFirstLine();
  }
  public String getSecondLine(){
    return rpm.getSecondLine();
  }
  public String getThirdLine(){
    return rpm.getThirdLine();
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}
