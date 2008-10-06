/****license*****************************************************************
**   file: repTemelj.java
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

import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;

public class repTemelj implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  DataSet ds = frmTemelj.getInstance().getTemelj();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repMemo rpm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();

  public repTemelj() {
  }

  public repTemelj(int idx) {
    ds.goToRow(idx);
  }

  private boolean test() {
    return hr.restart.sisfun.frmParam.getParam("robno","ispLogo","N","Ispis loga na dokumentima (D/N)").equals("D");
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
      }
      int indx=0;
      public Object nextElement() {
        return new repTemelj(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public Timestamp getPocDatum() {
    return frmTemelj.getInstance().getPocDatum();
  }
  public Timestamp getZavDatum() {
    return frmTemelj.getInstance().getZavDatum();
  }

  public String getRANGE() {
    return "za razdoblje  od " + rdu.dataFormatter(getPocDatum()) + "  do " + rdu.dataFormatter(getZavDatum());
  }

  public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getNAZSKL() {
    return frmTemelj.getInstance().getNazSkl();
  }

  public String getBROJKONTA() {
    return ds.getString("BROJKONTA");
  }

  public String getNAZIVKONTA() {
    return ds.getString("NAZIVKONTA");
  }

  public double getIZNOSDUG() {
    return ds.getBigDecimal("IZNOSDUG").doubleValue();
  }
  public double getIZNOSPOT() {
    return ds.getBigDecimal("IZNOSPOT").doubleValue();
  }

  public int getLogo() {
    if (test()) return 1;
    else return 0;
  }

  public String getFirstLine(){
    return test() ? rpm.getFirstLine() : "";
  }
  public String getSecondLine(){
    return test() ? rpm.getSecondLine() : "";
  }
  public String getThirdLine(){
    return test() ? rpm.getThirdLine() : "";
  }
  public String getDatumIsp(){
    return test() ? rdu.dataFormatter(vl.getToday()) : "";
  }
}