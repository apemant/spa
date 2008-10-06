/****license*****************************************************************
**   file: repRadniNalog2.java
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

import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

public class repRadniNalog2 implements raReportData {//sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  frmProizvodnja rnl = frmProizvodnja.getInstance();
  DataSet prijava = rnl.getrepQDSprijava();
  DataSet ds = prijava;
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();

  String[] colname = new String[] {""};
  String[] colname1 = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();

  public repRadniNalog2() {
	 ru.setDataSet(prijava);
//   hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//   syst.prn(prijava);
  }
/*
  public repRadniNalog2(int idx) {
	 prijava.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        prijava.open();
      }
      int indx=0;
      public Object nextElement() {
        return new repRadniNalog2(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < prijava.getRowCount());
      }
    };
  }

  public void close() {
  }
*/
  public raReportData getRow(int i) {
    prijava.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return prijava.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    prijava = null;
  }

  public int getBRDOK() {
    return prijava.getInt("BRDOK");
  }
  public String getOPIS() {
    return prijava.getString("OPIS");
  }
  public int getCPAR() {
    return prijava.getInt("CPAR");
  }
  public String getNAZPAR() {
    ru.setDataSet(prijava);
    colname[0] = "CPAR";
    if (getCPAR()!=0) return ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString();
    return "";
  }
  public String getNAZNAP01() {
    ru.setDataSet(prijava);
    colname[0] = "CNAP1";
    colname1[0] = "CNAP";
    return ru.getSomething2(colname1,colname,prijava,dm.getNapomene(),"NAZNAP").getString();
  }
  public String getNAZNAP02() {
    ru.setDataSet(prijava);
    colname[0] = "CNAP2";
    colname1[0] = "CNAP";
    return ru.getSomething2(colname1,colname,prijava,dm.getNapomene(),"NAZNAP").getString();
  }
  public String getNAZNAP() {
    String napomena = getNAZNAP01();
    if (getNAZNAP01().equals("")) napomena = getNAZNAP02();
    if (!getNAZNAP01().equals("") && !getNAZNAP02().equals("")) napomena = getNAZNAP01() + "\n" + getNAZNAP02();
    if (ds.getString("NAPOM").length() > 0) {
      if (napomena.length() == 0) napomena = ds.getString("NAPOM");
      else napomena = napomena + "\n" + ds.getString("NAPOM");
    }

    return napomena;
  }

  public String getFormatBroj(){
    ru.setDataSet(prijava);
    return ru.getFormatBroj();
  }

  private boolean test() {
    return hr.restart.sisfun.frmParam.getParam("robno","ispLogo","N","Ispis loga na dokumentima (D/N)").equals("D");
  }

  public int getLogo() {
    if (test()) return 1;
    else return 0;
  }

  public String getFirstLine(){
    return test() ? rm.getFirstLine() : "";
  }
  public String getSecondLine(){
    return test() ? rm.getSecondLine() : "";
  }
  public String getThirdLine(){
    return test() ? rm.getThirdLine() : "";
  }
  public String getDatumIsp(){
    return test() ? rdu.dataFormatter(hr.restart.util.Valid.getValid().getToday()) : "";
  }
}