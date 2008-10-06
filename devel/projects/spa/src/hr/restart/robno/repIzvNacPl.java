/****license*****************************************************************
**   file: repIzvNacPl.java
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

import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;

public class repIzvNacPl implements raReportData { //sg.com.elixir.reportwriter.datasource.IDataProvider {

//  _Main main;
  static String kar;
  static String kre;
  static String cek;
  static String bon;
  static String irataKar;
  static String irataKre;
  static String irataCek;
  static String irataBon;
  static String datnp;
  frmIzvNacPl fINP = frmIzvNacPl.getInstance();
  repMemo rpm = repMemo.getrepMemo();
  DataSet ds = fINP.getQDS();
//  String nacpl = fINP.nacPl;
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
//  String[] colname = new String[] {""};
  Timestamp datOd = frmIzvNacPl.getInstance().datOd;
  Timestamp datDo = frmIzvNacPl.getInstance().datDo;
//  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  int rowCount=0;

  public repIzvNacPl() {
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(ds);
    nacPl();
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close(){
    ds = null;
  }

//  public repIzvNacPl(int idx) {
//    if(idx == 0)nacPl();
//    ds.goToRow(idx);
//  }
//
//  public java.util.Enumeration getData() {
//    return new java.util.Enumeration() {
//    {
//      ds.open();
//      ru.setDataSet(ds);
//      rowCount = ds.getRowCount();
//    }
//    int indx=0;
//    public Object nextElement() {
//
//      return new repIzvNacPl(indx++);
//    }
//    public boolean hasMoreElements() {
//      return (indx < ds.getRowCount());
//    }
//    };
//  }
//
//  public void close() {}

  public int getIdx() {
    return ds.getRow()+1;
  }

  public String getCNacPl() {
    return ds.getString("NACPLR");
  }

  public String getBrRata() {
    return ds.getShort("BRRATA")+"";
  }

  public String getNazNacPl() {
    return ds.getString("NAZNACPL");
  }

  public String getBrRac() {
    return ds.getString("CSKL")+"-"+ds.getString("VRDOK")+"-"+
        ds.getString("GOD")+"-"+val.maskZeroInteger(Integer.valueOf(String.valueOf(ds.getInt("BRDOK"))),6);
  }

  public double getIznos() {
    return ds.getBigDecimal("IZNOS").doubleValue();
  }

  public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getCBanke() {
    return ds.getString("CBANKA");
  }

  public double getSuma() {
    return frmIzvNacPl.getInstance().suma;
  }

  public String getSumLabela() {
    return "S V E U K U P N O";
  }

  private void nacPl(){
    kar="";
    kre="";
    cek="";
    bon="";
    irataKar="";
    irataKre="";
    irataCek="";
    irataBon="";
    datnp="";
    String cBank = "";
    BigDecimal rataCeka = Aus.zero2;
    BigDecimal rataKartice = Aus.zero2;
    BigDecimal kreditirano = Aus.zero2;
    BigDecimal bonirano = Aus.zero2;
    int idxBanke = 0;

    ds.setSort(new SortDescriptor(new String[]{"NACPLR","CBANKA"}));
    ds.first();
    cBank = ds.getString("CBANKA");
    while(ds.inBounds()) {
      if (ds.getInt("FL_KARTICA")==1){
        if (ds.getString("NACPLR").equals("K")) {
          lookupData.getlookupData().raLocate(dm.getKartice(), "CBANKA", ds.getString("CBANKA"));
          if(kar.equals("")){
            kar = dm.getKartice().getString("NAZIV")+"\n";
            cBank = ds.getString("CBANKA");
          }
          if(!cBank.equals(ds.getString("CBANKA")) && (rataKartice.compareTo(new BigDecimal(0))!=0)) {
            irataKar += rdUtil.getUtil().hrFormatBigDecimal(rataKartice) +" kn\n";
            kar += dm.getKartice().getString("NAZIV")+"\n";
            rataKartice = ds.getBigDecimal("IZNOS");
            cBank = ds.getString("CBANKA");
          } else {
            rataKartice = rataKartice.add(ds.getBigDecimal("IZNOS"));
          }
        } else if (ds.getString("NACPLR").equals("KR")) {
          lookupData.getlookupData().raLocate(dm.getKartice(), "CBANKA", ds.getString("CBANKA"));
          if(kre.equals("")){
            kre = dm.getKartice().getString("NAZIV")+"\n";
            cBank = ds.getString("CBANKA");
          }
          if(!cBank.equals(ds.getString("CBANKA")) && (kreditirano.compareTo(new BigDecimal(0))!=0)) {
            irataKre += rdUtil.getUtil().hrFormatBigDecimal(kreditirano) +" kn\n";
            kre += dm.getKartice().getString("NAZIV")+"\n";
            kreditirano = ds.getBigDecimal("IZNOS");
            cBank = ds.getString("CBANKA");
          } else {
            kreditirano = kreditirano.add(ds.getBigDecimal("IZNOS"));
          }
        } else if (ds.getString("NACPLR").equals("B")) {
          lookupData.getlookupData().raLocate(dm.getKartice(), "CBANKA", ds.getString("CBANKA"));
          if(bon.equals("")){
            bon = dm.getKartice().getString("NAZIV")+"\n";
            cBank = ds.getString("CBANKA");
          }
          if(!cBank.equals(ds.getString("CBANKA")) && (bonirano.compareTo(new BigDecimal(0))!=0)) {
            irataBon += rdUtil.getUtil().hrFormatBigDecimal(bonirano) +" kn\n";
            bon += dm.getKartice().getString("NAZIV")+"\n";
            bonirano = ds.getBigDecimal("IZNOS");
            cBank = ds.getString("CBANKA");
          } else {
            bonirano = bonirano.add(ds.getBigDecimal("IZNOS"));
          }
        }
      } else if (ds.getInt("FL_CEK")==1){
        lookupData.getlookupData().raLocate(dm.getBanke(), "CBANKA", ds.getString("CBANKA"));
        if(cek.equals("")){
          cek = dm.getBanke().getString("NAZIV")+"\n";
          cBank = ds.getString("CBANKA");
        }
        if(!cBank.equals(ds.getString("CBANKA")) && (rataCeka.compareTo(new BigDecimal(0))!=0)) {
          irataCek += rdUtil.getUtil().hrFormatBigDecimal(rataCeka)+" kn\n";
          cek += dm.getBanke().getString("NAZIV")+"\n";
          rataCeka = ds.getBigDecimal("IZNOS");
          cBank = ds.getString("CBANKA");
        } else {
          rataCeka = rataCeka.add(ds.getBigDecimal("IZNOS"));
          cBank = ds.getString("CBANKA");
        }
      } else
        cBank = ds.getString("CBANKA");
      ds.next();
    }
    irataCek += rdUtil.getUtil().hrFormatBigDecimal(rataCeka)+" kn\n";
    irataKar += rdUtil.getUtil().hrFormatBigDecimal(rataKartice)+" kn\n";
    irataKre += rdUtil.getUtil().hrFormatBigDecimal(kreditirano)+" kn\n";
    irataBon += rdUtil.getUtil().hrFormatBigDecimal(bonirano)+" kn\n";

//    System.out.println("irataCek - " + irataCek);
//    System.out.println("irataKar - " + irataKar);
//    System.out.println("irataKre - " + irataKre);
//    System.out.println("irataBon - " + irataBon);
  }

  public int getFL_Cek() {
    return ds.getInt("FL_CEK");
  }

  public int getFL_Kartica() {
    if (ds.getString("NACPLR").equals("KR") || ds.getString("NACPLR").equals("B")) return 0;
    return ds.getInt("FL_KARTICA");
  }

  public int getFL_Kredit() {
    int fl = 0;
    if (ds.getString("NACPLR").equals("KR")) fl = 1;
    return fl;
  }

  public int getFL_Bonovi() {
    int fl = 0;
    if (ds.getString("NACPLR").equals("B")) fl = 1;
    return fl;
  }

  public String getKartica() {
    return kar;
  }

  public String getCekovi() {
    return cek;
  }

  public String getKredit() {
    return kre;
  }

  public String getBonovi() {
    return bon;
  }

  public String getKarticaIzn(){
    return irataKar;
  }

  public String getCekoviIzn(){
    return irataCek;
  }

  public String getKreditIzn(){
    return irataKre;
  }

  public String getBonoviIzn(){
    return irataBon;
  }

  public String getNazSklad() {
    String cskl = getCSKL();
    lookupData.getlookupData().raLocate(dm.getSklad(), new String[]{"CSKL"}, new String[]{cskl});
    return dm.getSklad().getString("NAZSKL");
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

  public String getDatdok() {
    return rdu.dataFormatter(ds.getTimestamp("DATPRIM"));
  }

  public String getDatNapl() {
    return rdu.dataFormatter(ds.getTimestamp("DATNAP"));
  }

  public int getRowNum() {
    return rowCount;
  }

  public String getDatumIsp() {
    return rdu.dataFormatter(val.getToday());
  }


  public String getPeriod() {
    return "Maloprodaja " + rdu.dataFormatter(datOd)+"-"+rdu.dataFormatter(datDo);
  }

  public String getDateLabela() {
    if(fINP.isSljedPoDatRac())
      return "po datumu raèuna";
    return "po datumu naplate";
  }

  public int getDummy() {
    return 1;
  }
}
