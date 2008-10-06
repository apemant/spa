/****license*****************************************************************
**   file: repStatsMonths.java
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

import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;


public class repStatsMonths implements raReportData {
  DataSet ds;
  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  ispStatPar usm = ispStatPar.getInstance();
//  upStatsMonths usm = upStatsMonths.getInstance();
  private static int art;

  public repStatsMonths() {
    ds = usm.getReportSet();
    art = usm.getWhatIsCart();
    ru.setDataSet(ds);
    dm.getPartneri().open();
//    System.out.println("\n\nRIPORT\n\n");
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

  public String getCK(){
//      System.out.println("TLK _ - '" + usm.getCkup()+"'"); //XDEBUG delete when no more needed
    if (!usm.getCkup().equals("")) {
      return getCART();
    }
    return ds.getInt("CPAR")+"";
  }

  public String getNK(){
    if (!usm.getCkup().equals("")) return getNAZART();
    if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+"")){
      return dm.getPartneri().getString("NAZPAR");
    }
    return "";
  }

  public String getCARTNASLOV(){
    return Aut.getAut().getCARTdependable("Šifra artikla","Oznaka artikla","Barcode");
  }

  public String getCART(){
//    return Aut.getAut().getCARTdependable(ds.getInt("CART")+"",ds.getString("CART1"),ds.getString("BC"));
    String izlaz = Aut.getAut().getIzlazCARTdep(ds);
    if (hr.restart.sisfun.frmParam.getParam("robno","chForSpecial","N","Provjerava specijalne znakove (*,_,+,-) i odrezuje njih plus sve iza njih").equalsIgnoreCase("D")){
      if (Aut.getAut().getIzlazCART().equalsIgnoreCase("BC") && (izlaz.indexOf("*") > 0 || izlaz.indexOf("_") > 0 || izlaz.indexOf("+") > 0 || izlaz.indexOf("-") > 0)){
        
        int index = 13;
        
        if (izlaz.indexOf("*") > 0) index = izlaz.indexOf("*");
        if (izlaz.indexOf("_") > 0) index = izlaz.indexOf("_");
        if (izlaz.indexOf("+") > 0) index = izlaz.indexOf("+");
        if (izlaz.indexOf("-") > 0) index = izlaz.indexOf("-");
        
        izlaz = izlaz.substring(0,index);
      }
    }

    return izlaz;
  }

  public String getNAZART(){
    return ds.getString("NAZART");
  }

  public double get01(){
    try {
    return ds.getBigDecimal("01").doubleValue();
    } catch (Exception ex){
      return 0.00;
    }
  }


  public double get02(){
    try {
      return ds.getBigDecimal("02").doubleValue();
      } catch (Exception ex){
        return 0.00;
      }
  }


  public double get03(){
    try {
      return ds.getBigDecimal("03").doubleValue();
      } catch (Exception ex){
        return 0.00;
      }
  }


  public double get04(){
    try {
      return ds.getBigDecimal("04").doubleValue();
      } catch (Exception ex){
        return 0.00;
      }
  }


  public double get05(){
    try {
      return ds.getBigDecimal("05").doubleValue();
      } catch (Exception ex){
        return 0.00;
      }
  }


  public double get06(){
    try {
      return ds.getBigDecimal("06").doubleValue();
      } catch (Exception ex){
        return 0.00;
      }
  }


  public double get07(){
    try {
      return ds.getBigDecimal("07").doubleValue();
      } catch (Exception ex){
        return 0.00;
      }
  }


  public double get08(){
    try {
      return ds.getBigDecimal("08").doubleValue();
      } catch (Exception ex){
        return 0.00;
      }
  }


  public double get09(){
    try {
      return ds.getBigDecimal("09").doubleValue();
      } catch (Exception ex){
        return 0.00;
      }
  }


  public double get10(){
    try {
      return ds.getBigDecimal("10").doubleValue();
      } catch (Exception ex){
        return 0.00;
      }
  }


  public double get11(){
    try {
      return ds.getBigDecimal("11").doubleValue();
      } catch (Exception ex){
        return 0.00;
      }
  }


  public double get12(){
    try {
      return ds.getBigDecimal("12").doubleValue();
      } catch (Exception ex){
        return 0.00;
      }
  }


  public double getUKUPNO(){
    return ds.getBigDecimal("UKUPNO").doubleValue();
  }



  public String getFirstLine(){
    return rm.getFirstLine();
  }

  public String getSecondLine(){
    return rm.getSecondLine();
  }

  public String getThirdLine(){
    return rm.getThirdLine();
  }

  public String getLABELARTIKL(){
    if (art == 1) return "Artikl";
    if (art == 2) return "Grupa";
    if (art == 3) return "Dio šifre";
    return "";
  }

  public String getOZNAKAARTIKL(){
    if (art == 1) return usm.rpcart.getCART();
    if (art == 2) return usm.rpcart.getCGRART();
    if (art == 3) return usm.rpcart.getCART1();
    return "";
  }

  public String getNAZIVARTIKL(){
    if (art == 1) return usm.rpcart.getNAZART();
    if (art == 2) {
      if (lookupData.getlookupData().raLocate(dm.getGrupart(),"CGRART",usm.rpcart.getCGRART())){
//        if (isp.rpcart.grupa????){
//
//        }
        return dm.getGrupart().getString("NAZGRART");
      }
    }
    if (art == 3) return "";
    return "";
  }
  public String getCSKL() {
    return usm.getCskl();
  }
  public String getNAZSKL() {
    lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL", usm.getCskl());
    return dm.getSklad().getString("NAZSKL");
  }

  public String getCkup() {
    return usm.getCkup();
  }

  public String getNazKupca() {
    if (!usm.getPjCkup().equals("")){
      if (lookupData.getlookupData().raLocate(dm.getPjpar(),new String[] {"CPAR","PJ"},new String[] {usm.getCkup(),usm.getPjCkup()})){
        return usm.getNazKup() + " poslovna jedinica " + dm.getPjpar().getString("NAZPJ") + " " + dm.getPjpar().getString("ADRPJ") + " " + dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ");
      }
    }
    return usm.getNazKup();
  }

  public String getCpar() {
    return usm.getCpar();
  }

  public String getNazPar() {
    return usm.getNazPar();
  }
  
  public String getPoCemu(){
      return usm.getPoCemu();
  }


}
