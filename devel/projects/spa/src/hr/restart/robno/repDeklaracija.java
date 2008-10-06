/****license*****************************************************************
**   file: repDeklaracija.java
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

import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repDeklaracija implements raReportData {

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  DataSet ds;
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  private static boolean ispC;

  public repDeklaracija() {
    ds = frmDeklaracija.getInstance().getTds();
    ds.open();
    ispC = frmDeklaracija.getInstance().ispisCijene;
    ru.setDataSet(ds);
  }

  public void close() {
    ds = null;
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.getRowCount();
  }

  public String getCart() {
    return ds.getString("CART");
  }

  public String getBC() {
    ld.raLocate(dm.getArtikli(), new String[]{"CART"}, new String[]{getCart()});
    return dm.getArtikli().getString("BC");
  }

  public String getNazArt() {
    ld.raLocate(dm.getArtikli(), new String[]{"CART"}, new String[]{getCart()});
    return dm.getArtikli().getString("NAZART");
  }

  public String getJM() {
    ld.raLocate(dm.getArtikli(), new String[]{"CART"}, new String[]{getCart()});
    return dm.getArtikli().getString("JM");
  }

  public String getProiz() {
    return ds.getString("PROIZ");
  }

  public String getUvoz() {
    return ds.getString("UVOZ");
  }

  public String getZemlja() {
    return ds.getString("ZEMLJA");
  }

  public String getGodina() {
    return ds.getString("GODINA");
  }

  public String getCijena() {
    if (ispC)
      return ds.getBigDecimal("CIJENA").toString() + " kn";
    return "";
  }

  public String getCijenaLab() {
    if (ispC)
      return "Cijena";
    return "";
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
}