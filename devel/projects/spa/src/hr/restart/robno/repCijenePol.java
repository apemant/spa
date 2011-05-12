/****license*****************************************************************
**   file: repCijenePol.java
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

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repCijenePol implements raReportData { //sg.com.elixir.reportwriter.datasource.IDataProvider {

  frmCijenePolDva fcp = frmCijenePolDva.getInstance();
  _Main main;
  repMemo rpm = repMemo.getrepMemo();

  DataSet ds = fcp.getQds();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
//  String param = fcp.param;
//  String strCSKL = fcp.strCSKL;
  String strCSKLIz = fcp.rpcsklIzl.getCSKL();
//  boolean meskla = fcp.meskla;
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  lookupData ld = lookupData.getlookupData();


  public repCijenePol() {
    ds.open();
    ru.setDataSet(ds);
    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
    syst.prn(ds);
    dm.getKPR().open();
    //dm.getKPR().refresh();
  }

  /*public repCijenePol(int idx) {
//    if (idx == 0) {
//      sysoutTEST syst = new sysoutTEST(false);
//      syst.prn(ds);
//    }
    ds.goToRow(idx);
  }*/

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

  /*public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
        ru.setDataSet(ds);
      }
      int indx=0;
      public Object nextElement() {

        return new repCijenePol(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }
  public void close() {}*/


  public String getNaziv()
  {
    return "\n"+ds.getString("NAZART") + " (" + ds.getString("JM") + ")";
  }

  public BigDecimal getCijena()
  {
    return ds.getBigDecimal("ZC");
  }

  public String getSifra()
  {

   return Aut.getAut().getCARTdependable(ds);
  }

  public String getSifraLab()
  {
    return Aut.getAut().getCARTdependable("Šifra:", "Oznaka:", "Barcode:");
  }

  public String getBrKPR(){
//    System.out.println("getBrKpr");
    try {
//      System.out.println("jesam li nasao kljuc - ("+getKey(ds)+") : " + lookupData.getlookupData().raLocate(dm.getKPR(),"KLJUC",getKey(ds)));
//      System.out.println("kljuc " + getKey(ds));
      if (lookupData.getlookupData().raLocate(dm.getKPR(),"KLJUC",getKey(ds))){
//        System.out.println("RBR iz KPR - " + dm.getKPR().getInt("RBR")+"");
        return dm.getKPR().getInt("RBR")+"";
      } else
        return "\\\\\\";
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return "///";
    }
  }

  public String getUlBrDok() {
    try {
      return getKey(ds);
    }
    catch (Exception ex) {
      return "";
    }
    /*try {
      if (!ds.getString("VRDOK").equals("") && !ds.getString("CSKL").equals("") &&
          !ds.getString("GOD").equals("") && (ds.getInt("BRDOK") != 0 || ds.getString("BRDOK") != null)) {
        if(ds.getString("VRDOK").equals("MES") || ds.getString("VRDOK").equals("MEU") || ds.getString("VRDOK").equals("MEI"))
          return ds.getString("VRDOK")+"-"+strCSKLIz+"-"+ds.getString("CSKL")+"-"+ds.getString("GOD")+"-"+ds.getInt("BRDOK");
        else
          return ds.getString("VRDOK")+"-"+ds.getString("CSKL")+"-"+ds.getString("GOD")+"-"+ds.getInt("BRDOK");
      } else if (!ds.getString("TKAL").equals("") && !ds.getString("TKAL").equals("0")) {
        return ds.getString("TKAL");
      }
    }
    catch (Exception ex) {
      System.err.println("Jope exepshen");
      ex.printStackTrace();
    }
    return "";*/
  }

  public String getDokument()
  {/*
    String dokument ="";
    ld.raLocate(dm.getStdoki(), new String[]{"CSKL","CART"}, new String[]{strCSKL, ds.getInt("CART")+""});
    if(meskla)
    {
      dokument = dm.getStdoki().getString("VRDOK")+"-"+strCSKLIz+"-"+strCSKL+"/"
               +dm.getStdoki().getString("GOD")+"-"+dm.getStdoki().getInt("BRDOK");
    }
    else
    {
      dokument = dm.getStdoki().getString("VRDOK")+"-"+strCSKL+"/"
               +dm.getStdoki().getString("GOD")+"-"+dm.getStdoki().getInt("BRDOK");
    }
    return dokument;*/
    return "";
  }

  public String getFirstLine(){
    return rpm.getFirstLine();
  }
  public String getSecondLine(){
    return rpm.getSecondLine();
  }
  public String getThirdLine(){
    return rpm.getThirdLine();
  }

  public String getDatumIsp()
  {
    return rdu.dataFormatter(val.getToday());
  }

  public int getFake()
  {
    int fake= ds.getRow()%16;
    return fake;
  }

  private String getKey(com.borland.dx.dataset.DataSet data) {
    String forReturn= data.getString("VRDOK").trim();
    try {
      try {
        if (!data.getString("CSKLIZ").equalsIgnoreCase("")) {
          forReturn=forReturn+
                    "-"+
                    data.getString("CSKLIZ").trim();
        }
        if (!data.getString("CSKLUL").equalsIgnoreCase("")) {
          forReturn=forReturn+
                    "-"+
                    data.getString("CSKLUL").trim();
        }
      }
      catch (Exception ex) {
//        System.out.println("nije fuckin medjuskljadisnjic, i to drito iz dekumenata");
      }
      try {
        if (!data.getString("CSKL").equalsIgnoreCase("")) {
          forReturn=forReturn+
                    "-"+
                    data.getString("CSKL").trim();
        }
      }
      catch (Exception ex) {
//        System.out.println("fuckin medjuskljadisnjic, i to drito iz dekumenata");
      }
    }
    catch (Exception ex) {
      if (!data.getString("CSKL").equalsIgnoreCase("")) {
        forReturn=forReturn+
                  "-"+
                  data.getString("CSKL").trim();
      }
    }
    forReturn=forReturn+
              "/"+
              ds.getString("GOD")+ // fcp.getGodina().trim()+
              "-"+
              data.getInt("BRDOK");
//    System.out.println("forreturn " + forReturn);
    return forReturn;
  }

}
