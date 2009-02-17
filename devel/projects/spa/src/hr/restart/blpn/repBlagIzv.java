/****license*****************************************************************
**   file: repBlagIzv.java
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
package hr.restart.blpn;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Valid;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repBlagIzv implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmBlagIzv fui = frmBlagIzv.getBlagIzv();
  DataSet ds = frmBlagIzv.getBlagIzv().getrepQDS();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repBlagIzv() {
    ru.setDataSet(ds);
  }

  public repBlagIzv(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
      }
      int indx=0;
      public Object nextElement() {

        return new repBlagIzv(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getNASLOV() {
    return "\nBLAGAJNI\u010CKI IZVJEŠTAJ Br. " + getBRIZV();
  }
  public String getZaPeriod(){
    if (fui.bezgotovinska){
      if (ds.getTimestamp("DATOD").equals(ds.getTimestamp("DATDO")))
        return "Bezgotovinska blagajna br. " + getCBLAG() + " za " + getDATOD();
      return "Bezgotovinska blagajna br. " + getCBLAG() + " od " + getDATOD() + " do " + getDATDO();
    } else {
      if (ds.getTimestamp("DATOD").equals(ds.getTimestamp("DATDO")))
        return "Blagajna br. " + getCBLAG()  + " za " + getDATOD();
      return "Blagajna br. " + getCBLAG() + " od " + getDATOD() + " do " + getDATDO();
    }
  }
  public String getBROJKONTA() {
    try {
      //za direkt unos konta
      if (!ds.getString("BROJKONTA").trim().equals("")) return ds.getString("BROJKONTA");
      
      String stavka = ds.getString("STAVKA");
      String cskl = ds.getString("CSKL");
//    System.out.println("stavka : " + stavka);
      if (stavka.equals("")) return "";
//    System.out.println("brkonta : " + fui.ss.getBrojKonta(stavka));
      return fui.ss.getBrojKonta(stavka, cskl);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return "";
    }
  }
  public int getCBLAG() {
    return ds.getInt("CBLAG");
  }
  public String getKNJIG() {
    return ds.getString("CORG");
  }
  public String getOZNVAL() {
    return ds.getString("OZNVAL");
  }
  public int getBRIZV(){
    return ds.getInt("BRIZV");
  }
  public String getDATOD() {
    return rdu.dataFormatter(ds.getTimestamp("DATOD"));
  }
  public String getDATDO() {
    return rdu.dataFormatter(ds.getTimestamp("DATDO"));
  }
  public BigDecimal getPRIJENOS() {
    return ds.getBigDecimal("PRIJENOS");
  }
  public BigDecimal getSALDO() {
    return ds.getBigDecimal("SALDO");
  }
  public BigDecimal getUKSALDO() {
    return ds.getBigDecimal("UKSALDO");
  }
  public BigDecimal getPRIMITAK() {
    return ds.getBigDecimal("PRIMITAK");
  }
  public BigDecimal getUKPRIMITAK() {
    return ds.getBigDecimal("UKPRIMITAK");
  }
  public BigDecimal getIZDATAK() {
    return ds.getBigDecimal("IZDATAK");
  }
  public BigDecimal getUKIZDATAK() {
    return ds.getBigDecimal("UKIZDATAK");
  }
  public String getOPIS() {
    return sgStuff.getOPISBLIzv(ds);
    //return ds.getString("OPIS")+" "+ds.getString("TKO");
  }
  public String getDATUMSTAVKE() {
    return rdu.dataFormatter(ds.getTimestamp("DATUM"));
  }
  public String getVRSTA(){
    if (ds.getString("VRSTA").equals("I")) return "Isplatnica";
    return "Uplatnica";
  }
  public int getRBS(){
    return ds.getInt("RBS");
  }
  public String getFirstLine(){
    return re.getFirstLine();
  }
  public String getSecondLine(){
    return re.getSecondLine();
  }
  public String getThirdLine(){
    return re.getThirdLine();
  }
  public String getLogoMjesto(){
    return re.getLogoMjesto();
  }
  public String getDatumIsp(){
//    return rdu.dataFormatter(ds.getTimestamp("DATUM"));
    return rdu.dataFormatter(vl.getToday());
  }
}