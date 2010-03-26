/****license*****************************************************************
**   file: repIspOS.java
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
package hr.restart.os;

import hr.restart.robno._Main;
import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;

// kolektivni provider za ispis OS-a

public class repIspOS implements raReportData {

  _Main main;
  ispOS_NextGeneration iosng = ispOS_NextGeneration.getInstance();
  DataSet ds;
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();

//  boolean poPripCORG = ispOS.poPripCorg;
  double osn;
  double isp;
//  int oblikListe = ispOS.getSelectedRB();
  //static int sumIdx = 0;
  int rowCount=0;

  public repIspOS() {
//    System.out.println("initializir!!");
    ds = iosng.getQdsIspis();
    ds.open();
    ru.setDataSet(ds);
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(ds);
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

  public String getFake() {
    return "Abrakadabra";
  }

  public String getCOrg() {
//    System.out.println("corg - " + ds.getString("corg"));
    return ds.getString("corg");
  }

  public String getInvBr() {
    return ds.getString("invbr");
  }

  public String getNazSredstva() {
    return ds.getString("nazsredstva");
  }

  public String getDatumOS() {
    String datum = rdu.dataFormatter(ds.getTimestamp("datum"));
    if (datum.equals("01.01.1970"))
      return "";
    else
      return datum;
  }

  public String getFirstLine(){
    return rpm.getFirstLine();
  }

  public double getOsnVr() {
    if(iosng.getPocetnoStanje()) { //ispOS.getSelectedRB2()==0) {
      osn = ds.getBigDecimal("OSNPOCETAK").doubleValue();
    }
    else if(iosng.getTrenutnoStanje() || iosng.getStanjeNaDan()) { // ispOS.getSelectedRB2()==1 || ispOS.getSelectedRB2()==2) {
      osn = (ds.getBigDecimal("OSNDUGUJE").add(ds.getBigDecimal("OSNPOTRAZUJE").negate())).doubleValue();
    }
    return osn;
  }

  public double getIspVr() {
    if(iosng.getPocetnoStanje()) { //ispOS.getSelectedRB2()==0) {
      isp = ds.getBigDecimal("ISPPOCETAK").doubleValue();
    }
    else if (iosng.getTrenutnoStanje() || iosng.getStanjeNaDan()) { // ispOS.getSelectedRB2()==1 || ispOS.getSelectedRB2()==2) {
      try {
        isp = (ds.getBigDecimal("ISPPOTRAZUJE").add(ds.getBigDecimal("ISPDUGUJE").negate()).add(ds.getBigDecimal("AMORTIZACIJA")).add(ds.getBigDecimal("PAMORTIZACIJA"))).doubleValue();
      }
      catch (Exception ex) {
//        System.err.println("EXCEPTION - radim bez amortizacije!!");
        isp = (ds.getBigDecimal("ISPPOTRAZUJE").add(ds.getBigDecimal("ISPDUGUJE").negate())).doubleValue();
      }
    }
    return isp;
  }

  public String getNazOrg() {
    ru.setDataSet(ds);
    colname[0] = "corg";
    String rez = ru.getSomething(colname,dm.getOrgstruktura() ,"naziv").toString().trim();
    return rez;
  }

  public double getSadVr() {
    return (osn-isp);
  }

  public String getOblikListe() {
    String ol="";
    if (iosng.getOblikListe()){
      switch (iosng.getOblikIspisa()) {
        case 0:
          ol = ds.getString("brojkonta");
          break;
        case 1:
          ol= ds.getString("cgrupe");
          break;
        case 2:
          ol= ds.getString("clokacije");
          break;
        case 3:
          ol= ds.getString("cskupine");
          break;
        case 4:
          ol= ds.getString("cartikla");
          break;
      }
    }
    return ol;
  }

  public String getLabelaListe() {
    String ll="Šifra";
    if (iosng.getOblikListe()){
      switch (iosng.getOblikIspisa()) {
        case 0:
          ll= "Br. konta";
          break;
        case 1:
          ll= "Amor. grupa";
          break;
        case 2:
          ll= "Lokacija";
          break;
        case 3:
          ll= "Rev. skupina";
          break;
        case 4:
          ll= "Artikl";
          break;
      }
    }
    return ll;
  }


  public String getNazOblikListe() {
    String rez="";
    if (iosng.getOblikListe()){
      switch (iosng.getOblikIspisa()) {
        case 0:
          colname[0] = "brojkonta";
          rez = ru.getSomething(colname,dm.getKonta() ,"nazivkonta").toString().trim();
          rez = ru.getSomething(colname,hr.restart.zapod.raKonta.getAnalitickaKonta() ,"nazivkonta").toString().trim();
          break;
        case 1:
          colname[0] = "cgrupe";
          rez = ru.getSomething(colname,dm.getOS_Amgrupe() ,"nazgrupe").toString().trim();
          break;
        case 2:
          colname[0] = "clokacije";
          rez = ru.getSomething(colname,dm.getOS_Lokacije() ,"nazlokacije").toString().trim();
          break;
        case 3:
          colname[0] = "cskupine";
          rez = ru.getSomething(colname,dm.getOS_Revskupine() ,"nazskupine").toString().trim();
          break;
        case 4:
          colname[0] = "cartikla";
          rez = ru.getSomething(colname,dm.getOS_Artikli() ,"nazartikla").toString().trim();
          break;
      }
    }
    return rez;
  }

 /* public String getSum()
  {
    if(sumIdx==(rowCount - 1))
      return getLabelaListe() + " " + getOblikListe();
    return "";
  }*/


  public int getRowNum() {
    return rowCount;
  }

  public String getSumLabela() {
    return "S V E U K U P N O";
  }

  public double getSumIsp() {
    return iosng.getSume()[1];
  }

  public double getSumOsn_Isp() {
    return iosng.getSume()[2];
  }

  public double getSumOsn() {
    return iosng.getSume()[0];
  }


  private String convertDouble(String valueStr) {
    String decPart = "";
    String intPart = "";
    String temp="";
    String parseStr="";
    int i = 0;
    do {
      parseStr = valueStr.substring(i,i+1);

      if (!(parseStr.equals(".") || (parseStr.equals(","))))
        temp = temp + parseStr;
      if (parseStr.equals(".")) {
        intPart = temp;
        temp = "";
      }
      i++;
    }while(i<valueStr.length());
    decPart = temp;
    if (decPart.length()==1)
      decPart = decPart +"0";
    String returnValue = intPart + "," + decPart;
    return returnValue;
  }


  public String getSecondLine(){
    return rpm.getSecondLine();
  }

  public String getThirdLine(){
    return rpm.getThirdLine();
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(val.getToday());
  }

  public String getCaption() {
    return "ISPIS OSNOVNIH SREDSTAVA";
  }

  public String getCaption2() {
    String tempOL="";
    switch (iosng.getOblikIspisa()) {
      case 0:
        tempOL="kontu";
        break;
      case 1:
        tempOL="amortizacijskim grupama";
        break;
      case 2:
        tempOL="lokaciji";
        break;
      case 3:
        tempOL="revalorizacijskim skupinama";
        break;
      case 4:
        tempOL="artiklu";
        break;
    }
//    if (iosng.getOblikListe()){
//      if(iosng.getPripadnostOrgJedinici()) {
//        if (iosng.getPoOrgJedinici())
//          return "( po org. jedinicama i "+tempOL+" s pripadajuæim org. jedinicama )";
//        return "(po "+tempOL+" s pripadajuæim org. jedinicama )";
//      }
//      if (iosng.getPoOrgJedinici())
//        return "( po org. jedinicama i "+tempOL+" )";
//      return "(po "+tempOL+" )";
//    } else {
//      if(iosng.getPripadnostOrgJedinici())
//        return "( po org. jedinicama s pripadajuæim org. jedinicama )";
//      return "( po org. jedinicama )";
//    }
    String saprip = iosng.getPripadnostOrgJedinici()?"s pripadajuæim org. jedinicama":"";
    String poorg = iosng.getPoOrgJedinici()?"po org. jedinicama":"";
    String sve = iosng.getOblikListe()? (poorg + " i "+tempOL+" "+saprip) : (poorg + " " + saprip); 
    return "( "+sve.trim()+" )";
  }

  public String getcaption3() {
    String vl="Poèetno stanje";
    if (iosng.getTrenutnoStanje()) vl="Trenutno stanje";
    else if (iosng.getStanjeNaDan()) vl="Stanje na dan " + rdu.dataFormatter(iosng.getNaDan());
    return vl;
  }

  public String getStatus() {
    String strStatus = "Sva OS";
    if (iosng.getStatus().equals("I")) strStatus = "Investicije u tijeku";
    else if (iosng.getStatus().equals("P")) strStatus = "OS u pripremi";
    else if (iosng.getStatus().equals("A")) strStatus = "OS u upotrebi";
    return strStatus;
  }

  public String getPorijeklo() {
    String strPorijeklo = "Sva porijekla";
    if (iosng.getPorjeklo().equals("1")) strPorijeklo = "Tuzemstvo";
    else if (iosng.getPorjeklo().equals("2")) strPorijeklo = "Inozemstvo";
    else if (iosng.getPorjeklo().equals("3")) strPorijeklo = "Vrijednosnice";
    return strPorijeklo;
  }

  public String getAktivnost() {
    String strAktivnost = "Sva OS";
    if (iosng.getAktivnost().equals("D")) strAktivnost = "Aktivna OS";
    else if (iosng.getAktivnost().equals("N")) strAktivnost = "Neaktivna OS";
    return strAktivnost;
  }

//  private String formatDatum(String datum) {
//    StringBuffer sb = new StringBuffer(datum);
//    sb.replace(2,3,".");
//    sb.replace(5,6,".");
//    return sb.toString();
//  }

/////--------------------------------------------------------------------------------------------------------


}