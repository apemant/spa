/****license*****************************************************************
**   file: repKarticaOS.java
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
import hr.restart.util.Aus;
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repKarticaOS implements sg.com.elixir.reportwriter.datasource.IDataProvider
{

  _Main main;
  DataSet ds = osTemplate.getQuery();

  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  String[] colnamex = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  hr.restart.util.sysoutTEST ST= new hr.restart.util.sysoutTEST(false);

  public repKarticaOS() {
    ru.setDataSet(ds);
  }

  public repKarticaOS(int idx) {
    if (idx == 0) saldo = Aus.zero2;
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
//        sysoutTEST ST = new sysoutTEST(false);
//        ST.prn(ds);
      }
      int indx=0;
      public Object nextElement() {
        return new repKarticaOS(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getInvBroj()
  {
    return ds.getString("invbroj");
  }

  public String getCOrg()
  {
    return ds.getString("corg2");
  }

  public String getCObjekt()
  {
    return ds.getString("COBJEKT");
  }

  public String getCRadnik()
  {
    return ds.getString("CRADNIK");
  }

  public String getStatus()
  {
    String aktivnost ="";
    String status ="";
    System.out.println("aktiv:" + ds.getString("AKTIV"));
    System.out.println("status:" + ds.getString("STATUS"));


    if (ds.getString("AKTIV").equals("D")) {
      aktivnost = "Aktiv.";
      if (ds.getString("STATUS").equals("A"))
        status = "OS u upotrebi";
      else
        status = "OS u pripremi";

      return (status+"/"+aktivnost);
    } else {
      status = "Likvidirano OS";
      return status;
    }



//    if(ds.getString("AKTIV").equals("D"))
//      aktivnost = "Aktiv.";
//    else
//      aktivnost = "Neakt.";
//
//    if(ds.getString("STATUS").equals("A"))
//      status = "OS u upotrebi";
//    else
//      status= "OS u pripremi";

  }


  public String getObjekt()
  {
    String cobjekt = ds.getString("COBJEKT");
    if (lookupData.getlookupData().raLocate(dm.getOS_Objekt(), new String[] {"COBJEKT"}, new String[]{cobjekt}))
        return dm.getOS_Objekt().getString("NAZOBJEKT");
    else return "";
  }

  public String getRadnik()
  {
    String cradnik = ds.getString("CRADNIK");
    if (lookupData.getlookupData().raLocate(dm.getRadnici(), new String[] {"CRADNIK"}, new String[]{cradnik}))
        return dm.getRadnici().getString("IME") +" "+ dm.getRadnici().getString("PREZIME");
    else
      return "";
  }


  public String getDatNab()
  {
    if(rdu.dataFormatter(ds.getTimestamp("datnab")).equals("01.01.1970"))
      return"";
    return rdu.dataFormatter(ds.getTimestamp("datnab"));
  }

  public String getDatAkt()
  {
    if(rdu.dataFormatter(ds.getTimestamp("datakt")).equals("01.01.1970"))
      return"";
    return rdu.dataFormatter(ds.getTimestamp("datakt"));
  }

  public String getNazSredstva()
  {
    return ds.getString("nazsredstva");
  }

  public int getRbr()
  {
    return ds.getInt("rbr");
  }

  public String getCLokacije()
  {
    return ds.getString("clokacije");
  }

  public String getCGrupe()
  {
    return ds.getString("cgrupe");
  }

  public String getCSkupine()
  {
    return ds.getString("cskupine");
  }

  public String getBrojKonta()
  {
    return ds.getString("brojkonta");
  }

  public String getCPartner()
  {
    String cPar = ds.getInt("cpar")+"";
    if (cPar.equals("0"))
      cPar = "";
    return cPar;
  }

  public String getCArtikla()
  {
    return ds.getString("cartikla");
  }

  public String getDokument()
  {
    ru.setDataSet(ds);
    colname[0] = "cpromjene";
    String rez = ru.getSomething(colname,dm.getOS_Vrpromjene() ,"nazpromjene").toString();
    return rez;
  }

  public String getDatumPromjene()
  {
    return rdu.dataFormatter(ds.getTimestamp("datumpromjene"));
  }

  public String getPorijeklo()
  {
    String temp = ds.getString("porijeklo");
    if (temp.equals("2"))
      return "Inozemstvo";
    else if(temp.equals("3"))
      return "Vrijednosnice";
    return "Tuzemstvo";
  }

  public BigDecimal getNabVrijed()
  {
    return ds.getBigDecimal("nabvrijed");
  }

//

  public String getDatPromjene()
  {
    if(rdu.dataFormatter(ds.getTimestamp("datpromjene")).equals("01.01.1970"))
      return"";
    return rdu.dataFormatter(ds.getTimestamp("datpromjene"));
  }

  public String getDatLikvidacije()
  {
    if(rdu.dataFormatter(ds.getTimestamp("datlikvidacije")).equals("01.01.1970"))
      return"";
    return rdu.dataFormatter(ds.getTimestamp("datlikvidacije"));
  }

  public String getCPromjene()
  {
    return ds.getString("cpromjene");
  }

   public double getOsnDug()
  {
    return ds.getBigDecimal("osnduguje").doubleValue();
  }

   public double getOsnPot()
  {
    return ds.getBigDecimal("osnpotrazuje").doubleValue();
  }

   public double getIspDug()
  {
    return ds.getBigDecimal("ispduguje").doubleValue();
  }

   public double getIspPot()
  {
    return ds.getBigDecimal("isppotrazuje").doubleValue();
  }

  private static BigDecimal saldo;

  public double getSaldo()
  {
    saldo = saldo.add((ds.getBigDecimal("osnduguje").subtract(ds.getBigDecimal("osnpotrazuje"))).
                      add(ds.getBigDecimal("ispduguje").subtract(ds.getBigDecimal("isppotrazuje"))));
    return saldo.doubleValue();

//    return ds.getBigDecimal("saldo").doubleValue();
  }

  public String getNazLokacije()
  {
    ru.setDataSet(ds);
    colname[0] = "clokacije";
    String rez = ru.getSomething(colname,dm.getOS_Lokacije() ,"nazlokacije").toString();
    return rez;
  }

  public String getNazAmGrup()
  {
    ru.setDataSet(ds);
    colname[0] = "cgrupe";
    String rez = ru.getSomething(colname,dm.getOS_Amgrupe() ,"nazgrupe").toString();
    return rez;
  }

  public String getNazRevSk()
  {
    ru.setDataSet(ds);
    colname[0] = "cskupine";
    String rez = ru.getSomething(colname,dm.getOS_Revskupine() ,"nazskupine").toString();
    return rez;
  }

 public String getNazKonta()
  {
    ru.setDataSet(ds);
    colname[0] = "brojkonta";
    String rez = ru.getSomething(colname,hr.restart.zapod.raKonta.getAnalitickaKonta() ,"nazivkonta").toString();
    return rez;
  }

  public String getDobavljac()
  {
    String dobavljac = ds.getInt("CPAR")+"";
    if (lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", ds.getInt("CPAR")+""))
        return dm.getPartneri().getString("NAZPAR");
    else
      return "";
//    ru.setDataSet(ds);
//    colname[0] = "cpar";
//    String rez = ru.getSomething(colname,dm.getPartneri() ,"nazpar").toString();
//    return rez;
  }

   public String getNazArt()
  {
    ru.setDataSet(ds);
    colname[0] = "cartikla";
    String rez = ru.getSomething(colname,dm.getOS_Artikli() ,"nazartikla").toString();
    return rez;
  }

  public String getCOrgNaz()
  {
    ru.setDataSet(ds);
    colname[0] = "corg";
    colnamex[0] = "corg2";
    String rez = ru.getSomething(colname, colnamex ,dm.getOrgstruktura() ,"naziv").toString();
    return rez;
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
  public String getDatumIsp(){
    return rdu.dataFormatter(val.getToday());
  }
}
