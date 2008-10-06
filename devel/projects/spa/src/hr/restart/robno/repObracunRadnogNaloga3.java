/****license*****************************************************************
**   file: repObracunRadnogNaloga3.java
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

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repObracunRadnogNaloga3 implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
//  hr.restart.util.sysoutTEST sys = new hr.restart.util.sysoutTEST(false);
  frmProizvodnja rnl = frmProizvodnja.getInstance();
  DataSet prijava = rnl.getrepQDSprijava();
  DataSet detalji = rnl.getrepQDSnorm();
  DataSet ds = prijava;
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();

//  String[] colname = new String[] {""};
//  String[] colname1 = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();

  public repObracunRadnogNaloga3() {
    ru.setDataSet(detalji);
  }

  public repObracunRadnogNaloga3(int idx) {
    detalji.goToRow(idx);
    prijava.goToRow(0);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        detalji = rnl.getrepQDSnorm();
        prijava.open();
        detalji.open();
      }
      int indx=0;
      public Object nextElement() {
        return new repObracunRadnogNaloga3(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < detalji.getRowCount());
      }
    };
  }

  public void close() {
  }

  /*
      Sekcija za detalje radnog naloga
  */

  public String getVRDOK() {
    return detalji.getString("VRDOK");
  }
  public int getBRDOK() {
    return detalji.getInt("BRDOK");
  }
  public String getGOD() {
    return detalji.getString("GOD");
  }
  public String getJEDANBROJ(){
    if (detalji.getString("VRDOK").equals("RNU") || detalji.getString("VRDOK").equals("RNL")) return "";
    ru.setDataSet(detalji);
    return "Izdatnica " + ru.getFormatBroj();
  }
  public String getRADOVIMATERIJAL() {
    if (detalji.getString("VRDOK").equals("RNU") || detalji.getString("VRDOK").equals("RNL")) return "Izvršeni radovi";
    return "Utrošeni materijal";
  }
  public String getCART() {
      return Aut.getAut().getIzlazCARTdep(detalji);
  }
  public String getNAZART() {
    return detalji.getString("NAZART");
  }
  public String getJM() {
    return detalji.getString("JM");
  }
  public BigDecimal getKOL() {
    return detalji.getBigDecimal("KOL");
  }

  /*
      Sekcija za dio radnog naloga koji je isti kao u prijavi radnog naloga.
  */

  public int getBRDOKRNL() {
    return prijava.getInt("BRDOK");
  }
  public String getOPIS() {
    return prijava.getString("OPIS");
  }
  public int getCPAR() {
    return prijava.getInt("CPAR");
  }
  public String getNAZPAR() {
    if (ld.raLocate(dm.getPartneri(), "CPAR", String.valueOf(prijava.getInt("CPAR"))))
      return dm.getPartneri().getString("NAZPAR");
    else return "";
  }
  public String getNAZNAP01() {
    if (ld.raLocate(dm.getNapomene(), "CNAP", prijava.getString("CNAP1"))) {
      return dm.getNapomene().getString("NAZNAP");
    } else return "";
  }
  public String getNAZNAP02() {
    if (ld.raLocate(dm.getNapomene(), "CNAP", prijava.getString("CNAP2"))) {
      return dm.getNapomene().getString("NAZNAP");
    } else return "";
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

