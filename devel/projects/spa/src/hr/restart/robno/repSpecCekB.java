/****license*****************************************************************
**   file: repSpecCekB.java
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

import hr.restart.baza.dM;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;

import java.sql.Date;
import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;

public class repSpecCekB implements sg.com.elixir.reportwriter.datasource.IDataProvider {
  String[] colname = new String[]{""};
  frmSpecCek fsc = frmSpecCek.getInstance();
  Timestamp datDo = fsc.datDo;
  Timestamp datOd = fsc.datOd;
  dM dm = dM.getDataModule();
  DataSet ds = fsc.getQDS();

  public repSpecCekB() {
  }

  public repSpecCekB(int idx) {
//    if (idx == 0){
//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(ds);
//    }
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return
      new java.util.Enumeration() {
        {
          ds.open();
          ru.setDataSet(ds);
          rowCount = ds.getRowCount();
        }
        public boolean hasMoreElements() {
          return (indx < ds.getRowCount());
        }
        int indx = 0;
        public Object nextElement() {

          return new repSpecCekB(indx++);
        }
      };
  }
  public void close() { }

  static int stin, intBroj;
  static String OzBan;

  private static final int BROJ_REDOVA = 27;

  public int getStrana(){

    /*
      Ovaj geter je podlozan promjenama s promjenama u dizajnu templatea!!!!!
    */

    if (fsc.cbankaPopunjen()) return ds.getRow()/BROJ_REDOVA;
    if (ds.getRow()==0){
      stin = 0;
      intBroj = 0;
      OzBan = "";
    }
    if (!OzBan.equals(ds.getString("CBANKA"))) {
      OzBan = ds.getString("CBANKA");
      stin++;
      intBroj = 0;
    } else {
      intBroj++;
    }
    return ((stin*BROJ_REDOVA)+intBroj)/BROJ_REDOVA;
  }

  public String getBrRac() {
    return ds.getString("CSKL") + "-" + ds.getString("VRDOK") + "-" +
        ds.getString("GOD") + "-" + val.maskZeroInteger(Integer.valueOf(String.valueOf(ds.getInt("BRDOK"))), 6);
  }

  public String getCBanke() {
    return ds.getString("CBANKA");
  }

  public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getCaption() {
    if (param.equals("prim")) {
      return "po datumu primitka za period " + getPeriod();
    }
    return "po datumu naplate za period " + getPeriod();
  }

  public String getDatdok() {
    try {
      return rdu.dataFormatter(ds.getTimestamp("DATPRIM"));
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public Date getDatnap() {
    return Date.valueOf(ds.getTimestamp("DATNAP").toString().substring(0,10));
  }

  public String getDatumIsp() {
    return rdu.dataFormatter(val.getToday());
  }

  public String getFirstLine() {
    return rpm.getFirstLine();
  }

  public int getIdx() {
    return ds.getRow() + 1;
  }

  public double getIznos() {
    return ds.getBigDecimal("IZNOS").doubleValue();
  }

  public String getMjesto() {
    lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", ds.getString("CSKL"));
    lookupData.getlookupData().raLocate(dm.getOrgstruktura(), "CORG", dm.getSklad().getString("CORG"));
    return dm.getOrgstruktura().getString("MJESTO");
  }

  public String getNazBanke() {
    String cbanka = getCBanke();
    if (lookupData.getlookupData().raLocate(dm.getBanke(), new String[]{"CBANKA"}, new String[]{cbanka}))
    return dm.getBanke().getString("NAZIV");
    return "";
  }

  public String getNazSklad() {
    String cskl = getCSKL();
    lookupData.getlookupData().raLocate(dm.getSklad(), new String[]{"CSKL"}, new String[]{cskl});
    return cskl + " - " + dm.getSklad().getString("NAZSKL");
  }

  private String getPeriod() {
    return rdu.dataFormatter(datOd) + "-" + rdu.dataFormatter(datDo);
  }

  public int getRowNum() {
    return rowCount;
  }

  public String getSBC() {
    return ds.getString("SBC");
  }
  public String getSecondLine() {
    return rpm.getSecondLine();
  }

  public String getSumLabela() {
    return "S V E U K U P N O";
  }

  public double getSuma() {
    return frmSpecCek.getInstance().suma;
  }

  public String getTBG() {
    return ds.getString("TRG");
  }
  public String getThirdLine() {
    return rpm.getThirdLine();
  }

  public String getUmjesuDana() {
    return getMjesto() + " dana " + getDatumIsp() + ".";
  }

  public String getZiro() {
    lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", ds.getString("CSKL"));
    lookupData.getlookupData().raLocate(dm.getOrgstruktura(), "CORG", dm.getSklad().getString("CORG"));
    if (dm.getOrgstruktura().getString("ZIRO").equals("")) {
      String prip = dm.getOrgstruktura().getString("PRIPADNOST");
      lookupData.getlookupData().raLocate(dm.getOrgstruktura(), "CORG", prip);
      return dm.getOrgstruktura().getString("ZIRO");
    }
    return dm.getOrgstruktura().getString("ZIRO");
  }

  public String getZiroBanke() {
    String cbanka = getCBanke();
    if (lookupData.getlookupData().raLocate(dm.getBanke(), new String[]{"CBANKA"}, new String[]{cbanka}))
    return dm.getBanke().getString("ZIRO");
    return "";
  }
  _Main main;
  String param = frmSpecCek.getInstance().param;
  raDateUtil rdu = raDateUtil.getraDateUtil();
  int rowCount = 0;
  repMemo rpm = repMemo.getrepMemo();
  repUtil ru = repUtil.getrepUtil();
  Valid val = Valid.getValid();
}
