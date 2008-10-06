/****license*****************************************************************
**   file: repKartica2Reda.java
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
import hr.restart.util.Util;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;
import java.sql.Date;

import com.borland.dx.dataset.DataSet;

public class repKartica2Reda implements raReportData { //implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  upKartica upk = upKartica.getupKartica();
  DataSet ds = upk.getQds();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  int brDokTemp;
  
  private DataSet doki_ ;
  private DataSet doku_ ;

  lookupData ld= lookupData.getlookupData();

  public repKartica2Reda() {
        
      doku_=Util.getNewQueryDataSet("select doku.cskl,doku.god,doku.vrdok,doku.brdok,doku.cpar,stdoku.cart "+
        "from doku,stdoku WHERE doku.cskl = stdoku.cskl AND doku.vrdok = stdoku.vrdok "+
        "AND doku.god = stdoku.god AND doku.brdok = stdoku.brdok and "+
        "doku.cskl='"+upk.getCskl()+"' and doku.god='"+upk.getGodina()+
        "' and stdoku.cart = "+upk.rpcart.getCART());
    
      doki_=Util.getNewQueryDataSet("select doki.corg, doki.cskl,doki.god,doki.vrdok,doki.brdok,doki.cpar,stdoki.cart "+
              "from doki,stdoki WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok "+
              "AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok and "+
              "doki.cskl='"+upk.getCskl()+"' and doki.god='"+upk.getGodina()+
              "' and stdoki.cart = "+upk.rpcart.getCART());
      
      /*
      doki_ = doki.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK CPAR",
              Condition.whereAllEqual(new String[]{"CSKL","GOD","CART"},
              new Object[]{upk.getCskl(),upk.getGodina(),new Integer(ds.getInt("CART"))}));
      
      doku_ = Doku.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK CPAR",
              Condition.whereAllEqual(new String[]{"CSKL","GOD","CART"},
              new Object[]{upk.getCskl(),upk.getGodina(),new Integer(ds.getInt("CART"))}));
    */
      
    ru.setDataSet(ds);
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
  
  
  public BigDecimal getKOL() {
    return ds.getBigDecimal("KOL");
  }
  public String getCskl(){
    return upk.getCskl();
  }

    hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
  public String getNazivCskl(){
// tomo update
    lD.raLocate(dm.getSklad(),"CSKL",upk.getCskl());
//    dm.getSklad().interactiveLocate(upk.getCskl(),"CSKL",com.borland.dx.dataset.Locate.FIRST,false);
    return dm.getSklad().getString("NAZSKL");
  }

  public String getCart() {
    return Aut.getAut().getCARTdependable(upk.rpcart.getCART(), upk.rpcart.getCART1(), upk.rpcart.getBC());
//    return upk.getCart();
  }
  public String getNazart() {
//    dm.getArtikli().interactiveLocate(String.valueOf(getCart()),"CART",com.borland.dx.dataset.Locate.FIRST,false);
    lookupData.getlookupData().raLocate(dm.getArtikli(),"CART",upk.rpcart.getCART());
    return dm.getArtikli().getString("NAZART");
  }
  public String getPocDat(){
    return rdu.dataFormatter(upk.getPocDatum());
  }
  public String getZavDat(){
    return rdu.dataFormatter(upk.getZavDatum());
  }
  public String getCaption2(){
    return (getPocDat()+" do "+getZavDat());
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(val.getToday());
  }
  public String getDatDok(){
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }
  public Date getSortDatDok(){
    return Date.valueOf(ds.getTimestamp("DATDOK").toString().substring(0,10));
//    return  ds.getTimestamp("DATDOK");
  }
  public int getBrDok() {
    brDokTemp = ds.getInt("BRDOK");
    return ds.getInt("BRDOK");
  }
  public String getVrDok(){
    return ds.getString("VRDOK");
  }
  public double getKolUl(){
// return (ds.getBigDecimal("KOL").intValue()>0? ds.getBigDecimal("KOL").doubleValue():(double) 0);
    return (ds.getBigDecimal("KOLUL").doubleValue());
  }
  public double getKolIz(){
//    System.out.println("KOLIZ: " + (ds.getBigDecimal("KOLIZ").doubleValue()));
    return (ds.getBigDecimal("KOLIZ").doubleValue());
//    return (ds.getBigDecimal("KOL").intValue()<0? ds.getBigDecimal("KOL").negate().doubleValue():(double) 0);
  }
  public double getZc(){
    return (ds.getBigDecimal("ZC").doubleValue());
  }
  public double getIZAD(){
    return (ds.getBigDecimal("KOLZAD").doubleValue());
//    return (ds.getBigDecimal("IZAD").intValue()>0? ds.getBigDecimal("IZAD").doubleValue():(double) 0);
  }
  public double getIRAZ(){
    return (ds.getBigDecimal("KOLRAZ").doubleValue());
// return (ds.getBigDecimal("IZAD").intValue()<0? ds.getBigDecimal("IZAD").negate().doubleValue():(double) 0);
  }
//  public double getSKOL(){
//    return (ds.getBigDecimal("SKOL").doubleValue());
//  }

  public double getSKOL(){
//    System.out.println("SKOL: " + (ds.getBigDecimal("KOLIZ").doubleValue()));
    return (ds.getBigDecimal("SKOL")).doubleValue();
  }
  public double getSIZN(){
    return (ds.getBigDecimal("SIZN").doubleValue());
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

  public String get2RedSifra()
  {
//    String sifra2Red ="";
//    int myCPar=0;
    String myVrDok = ds.getString("VRDOK");
//    int myCart= upk.getCart();
    
    if (myVrDok.equalsIgnoreCase("IZD")){
      ld.getlookupData().raLocate(doki_,
          new String[] {"CSKL","VRDOK","GOD","BRDOK"},
          new String[] {upk.getCskl(),myVrDok,upk.getGodina(),ds.getInt("BRDOK")+""});
      return doki_.getString("CORG");
    }

    if(TypeDoc.getTypeDoc().isDocMeskla(myVrDok)) {
//      int myBrDok = getBrDok();
//      System.out.println("brdok "+myBrDok+" je meskla i to ulaz = "+ds.getString("SKLUL")+", izlaz = "+ds.getString("SKLIZ")+"");

      if(ds.getString("SKLUL").equals(getCskl()))
        return ds.getString("SKLIZ");
      return ds.getString("SKLUL");

//      ld.raLocate(dm.getStmeskla(), new String[] {"CSKLUL","CSKLIZ","CART", "VRDOK", "BRDOK"}, new String[] {ds.getString("SKLUL"), ds.getString("SKLIZ"), getCart()+"", myVrDok, myBrDok+""});
//      if(dm.getStmeskla().getString("CSKLUL").equals(getCskl()))
//        return dm.getStmeskla().getString("CSKLIZ");
//      return dm.getStmeskla().getString("CSKLUL");

    } else if (TypeDoc.getTypeDoc().isDocStdoku(myVrDok)) {

//      return ds.getString("SKLUL");
      ld.getlookupData().raLocate(doku_,
                                  new String[] {"CSKL","VRDOK","GOD","BRDOK"},
                                  new String[] {upk.getCskl(),myVrDok,upk.getGodina(),ds.getInt("BRDOK")+""});
      if(doku_.getInt("CPAR")<=0 )
        return hr.restart.zapod.OrgStr.getKNJCORG();
      else
        return doku_.getInt("CPAR")+"";

    } else if (TypeDoc.getTypeDoc().isDocStdoki(myVrDok)) {

//      return ds.getString("SKLIZ");
      ld.getlookupData().raLocate(doki_,
                                  new String[] {"CSKL","VRDOK","GOD","BRDOK"},
                                  new String[] {upk.getCskl(),myVrDok,upk.getGodina(),ds.getInt("BRDOK")+""});
      if(doki_.getInt("CPAR")<=0 )
        return hr.restart.zapod.OrgStr.getKNJCORG();
      else
        return doki_.getInt("CPAR")+"";
    }
    if (myVrDok.equals("DON")) return hr.restart.zapod.OrgStr.getKNJCORG();
    return "";
  }

  public String get2redNaziv() {
//    int myCPar=0;
    String myVrDok = ds.getString("VRDOK");
//    int myCart= upk.getCart();
    
    if (myVrDok.equalsIgnoreCase("IZD")){
      ld.getlookupData().raLocate(doki_,
          new String[] {"CSKL","VRDOK","GOD","BRDOK"},
          new String[] {upk.getCskl(),myVrDok,upk.getGodina(),ds.getInt("BRDOK")+""});
      
      return getNazCorg(doki_.getString("CORG"));
    }
    
    if(TypeDoc.getTypeDoc().isDocMeskla(myVrDok)) {
//      int myBrDok = getBrDok();

      if(ds.getString("SKLUL").equals(getCskl()))
        return getNazSklad(ds.getString("SKLIZ"));
      return getNazSklad(ds.getString("SKLUL"));

//      ld.raLocate(dm.getStmeskla(), new String[] {"CART", "VRDOK", "BRDOK"}, new String[] {getCart()+"", myVrDok, myBrDok+""});
//      if(dm.getMeskla().getString("CSKLUL").equals(getCskl()))
//      ld.raLocate(dm.getStmeskla(), new String[] {"CSKLUL","CSKLIZ","CART", "VRDOK", "BRDOK"}, new String[] {ds.getString("SKLUL"), ds.getString("SKLIZ"), getCart()+"", myVrDok, myBrDok+""});
//      if(dm.getStmeskla().getString("CSKLUL").equals(getCskl()))
//        return getNazSklad(dm.getStmeskla().getString("CSKLIZ"));
//      return getNazSklad(dm.getStmeskla().getString("CSKLUL"));

    } else if (TypeDoc.getTypeDoc().isDocStdoku(myVrDok)) {
      ld.getlookupData().raLocate(doku_,
                                  new String[] {"CSKL","VRDOK","GOD","BRDOK"},
                                  new String[] {upk.getCskl(),myVrDok,upk.getGodina(),ds.getInt("BRDOK")+""});
      if(doku_.getInt("CPAR")<=0 )
        return getNazCorg(hr.restart.zapod.OrgStr.getKNJCORG());
      else
        return getParNaz(doku_.getInt("CPAR"));
    } else if (TypeDoc.getTypeDoc().isDocStdoki(myVrDok)) {
      ld.getlookupData().raLocate(doki_,
                                  new String[] {"CSKL","VRDOK","GOD","BRDOK"},
                                  new String[] {upk.getCskl(),myVrDok,upk.getGodina(),ds.getInt("BRDOK")+""});
      if(doki_.getInt("CPAR")<=0 )
        return getNazCorg(hr.restart.zapod.OrgStr.getKNJCORG());
      else
        return getParNaz(doki_.getInt("CPAR"));
    }
    if (myVrDok.equals("DON")) return getNazCorg(hr.restart.zapod.OrgStr.getKNJCORG());
    return "";
  }

  private String getParNaz(int cPar)
  {
    ld.raLocate(dm.getPartneri(), new String []{"CPAR"}, new String[]{cPar+""});
    return dm.getPartneri().getString("NAZPAR");
  }

  private String getNazCorg(String cOrg) {
    ld.raLocate(dm.getOrgstruktura(), new String []{"CORG"}, new String[]{cOrg});
    return dm.getOrgstruktura().getString("NAZIV");
  }

  private String getNazSklad(String cSkl)
  {
    ld.raLocate(dm.getSklad(), new String []{"CSKL"}, new String[]{cSkl});
    return dm.getSklad().getString("NAZSKL");
  }

  public int getDummy()
  {
    return 1;
  }
}