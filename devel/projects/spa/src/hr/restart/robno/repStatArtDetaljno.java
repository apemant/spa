/****license*****************************************************************
**   file: repStatArtDetaljno.java
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

public class repStatArtDetaljno implements raReportData {

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
  ispStatArt isa2 = ispStatArt.getInstance();
  DataSet ds;
  DataSet pds;
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  private static int art;


  public repStatArtDetaljno() {
    ds = isa2.getReportQDSDet();
    pds = isa2.getReportQDS();
    art = isa2.getWhatIsCart();
//    sysoutTEST syst = new sysoutTEST(false);
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
    ds = null;
  }

  public int getCPAR(){
    return ds.getInt("CPAR");
  }

  public String getNAZPAR(){
//    if (!isa2.getPjCkup().equals("")){
//      if (lookupData.getlookupData().raLocate(dm.getPjpar(),new String[] {"CPAR","PJ"},new String[] {ds.getInt("CPAR")+"",isa2.getPjCkup()})){
//        return ds.getString("NAZPAR") + " poslovna jedinica " + dm.getPjpar().getString("NAZPJ") + " " + dm.getPjpar().getString("ADRPJ") + " " + dm.getPjpar().getString("PBRPJ") + " " + dm.getPjpar().getString("MJPJ");
//      }
//    }
     return ds.getString("NAZPAR");
  }

  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
  }
  
  public int getCartInt(){
    int fr = 0;
    try {
      fr = Integer.parseInt(Aut.getAut().getCARTdependable(ds));
    } catch (NumberFormatException nfSex){
      fr = 0;
    }
    return fr;
  }

  public String getNAZART() {
    return ds.getString("NAZART");
  }
  
  public String getCSKL() {
    return isa2.getCskl();
  }
  public String getNAZSKL() {
      if (isa2.getCskl().equals("")) return "Sva skladišta";
    lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL", isa2.getCskl());
    return dm.getSklad().getString("NAZSKL");
  }
  
  public String getCORG() {
    return isa2.getCorg();
  }
  public String getNAZORG() {
    return isa2.getCorgNaziv();
  }

  /*public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getNAZSKL() {
    lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL", ds.getString("CSKL"));
    return dm.getSklad().getString("NAZSKL");
  }*/

  public String getJM() {
    return ds.getString("JM");
  }

  public double getKOL() {
    return ds.getBigDecimal("KOL").doubleValue();
  }

  public double getIRAZ() {
    return ds.getBigDecimal("IRAZ").doubleValue();
  }

  public double getINAB() {
    return ds.getBigDecimal("INAB").doubleValue();
  }

  public double getRUC() {
    return ds.getBigDecimal("RUC").doubleValue();
  }

  public double getNETTO(){
    return (ds.getBigDecimal("INAB").add(ds.getBigDecimal("RUC"))).doubleValue();
  }

  public BigDecimal getPostoRUC() {
    return ds.getBigDecimal("PostoRUC");
//    double prac = ds.getBigDecimal("RUC").doubleValue()/ds.getBigDecimal("INAB").doubleValue() * 100;
//    return prac;
  }

  public double getpostoSumRuc() {
    return isa2.getPostoSumRuc();
  }

//  public BigDecimal getPostoRUC_BD() {
//    java.math.BigDecimal prac = ds.getBigDecimal("RUC").divide(ds.getBigDecimal("INAB"),4,java.math.BigDecimal.ROUND_HALF_UP).multiply(new java.math.BigDecimal("100,00"));
//    return prac;
//  }

  public double getIPRODBP() {
    return ds.getBigDecimal("IPRODBP").doubleValue();
  }

  public double getIPRODSP() {
    return ds.getBigDecimal("IPRODSP").doubleValue();
  }

  public double getPOR() {
    return ds.getBigDecimal("POR").doubleValue();
  }

  public String getODDATUMA() {
    return rdu.dataFormatter(isa2.getPocDatum());
  }

  public String getDODATUMA() {
    return rdu.dataFormatter(isa2.getZavDatum());
  }

  public String getPodnaslov() {
    return "Za razdoblje od " + getODDATUMA() + " do " + getDODATUMA();
  }

  /*public Object getSORTER(){
    String sort = isa2.getSorter();
    if (sort.equals("CART")) return ds.getBigDecimal("CART");
    if (sort.equals("NAZART")) return ds.getString("NAZART");
    if (sort.equals("KOL")) return ds.getBigDecimal("KOL");
    if (sort.equals("RUC")) return ds.getBigDecimal("RUC");
    if (sort.equals("RUCPOSTO")) return getPostoRUC_BD();
    if (sort.equals("VRI")) return ds.getBigDecimal("VRI");
    return "";
  }*/

  public String getPoCemu() {
    /*{"Šifra","CART"},           \
      {"Naziv","NAZART"},          \
      {"Kolièina","KOL"},           }- zasad vako, a kasnije vidicemo kako
      {"RuC","RUC"},               /
      {"RuC %","PostoRUC"},       /
      {"Vrijednost","IPRODSP"}   /    */

    String sort = isa2.getSorter();
    if (sort.equals("CART")) return "\nPO ŠIFRAMA";
    if (sort.equals("NAZART")) return "\nPO NAZIVIMA";
    if (sort.equals("KOL")) return "\nPO KOLIÈINAMA";
    if (sort.equals("RUC")) return "\nPO RAZLICI U CIJENI";
    if (sort.equals("PostoRUC")) return "\nPO POSTOTKU RAZLIKE U CIJENI";
    if (sort.equals("IPRODSP")) return "\nPO VRIJEDNOSTI";
    return "";
  }
  
  public String getSLIJED() {
    String sort = isa2.getSorter();
    if (sort.equals("CART")) return "Šifra artikla";
    if (sort.equals("NAZART")) return "Naziv artikla";
    if (sort.equals("KOL")) return "Kolièina";
    if (sort.equals("RUC")) return "Razlika u cijeni";
    if (sort.equals("PostoRUC")) return "% razlike u cijeni";
    if (sort.equals("IPRODSP")) return "Vrijednost";
    return "";
  }
  
  public String getVRART() {
    return isa2.getVrsteArt();
  }

  public double getPrucCart(){
    lookupData.getlookupData().raLocate(pds,"CART",ds.getInt("CART")+"");
    return pds.getBigDecimal("PostoRUC").doubleValue();
  }

//  public double getSortSum(){
//    lookupData.getlookupData().raLocate(pds,"CPAR",ds.getInt("CPAR")+"");
//    return pds.getBigDecimal("IPRODSP").doubleValue();
//  }

  public String getCpar() {
    return isa2.getCpar();
  }

  public String getNazPar() {
    return isa2.getNazPar();
  }

  public String getCkup() {
    return isa2.getCkup();
  }

  public String getNazKupca() {
    if (!isa2.getPjCkup().equals("")){
//      System.out.println("REPSTATARTDVA - isa2.getPjCkupac postoji!!!   overwiew: ckup - '"+isa2.getCkup()+"' pjCkup - '"+isa2.getPjCkup()+"'");
      if (lookupData.getlookupData().raLocate(dm.getPjpar(),new String[] {"CPAR","PJ"},new String[] {isa2.getCkup(),isa2.getPjCkup()})){
//        System.out.println("locirana poslovna jedinica u tablicipjpar ritrnam " + isa2.getNazKup() + " poslovna jedinica " + dm.getPjpar().getString("NAZPJ") + " " + dm.getPjpar().getString("ADRPJ") + " " + dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ"));
        return isa2.getNazKup() + " poslovna jedinica " + dm.getPjpar().getString("NAZPJ") + " " + dm.getPjpar().getString("ADRPJ") + " " + dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ");
      } //else System.out.println("nista u lookupu");
    }
    return isa2.getNazKup();


    /*if (!isa2.getPjCkup().equals("")){
      if (lookupData.getlookupData().raLocate(dm.getPjpar(),new String[] {"CPAR","PJ"},new String[] {isa2.getCkup(),isa2.getPjCkup()})){
        return isa2.getNazKup() + " poslovna jedinica " + dm.getPjpar().getString("NAZPJ") + " " + dm.getPjpar().getString("ADRPJ") + " " + dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ");
      }
    }
    return isa2.getNazKup();*/
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

  public String getLogoMjesto(){
    return rpm.getLogoMjesto();
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }

  public String getLABELARTIKL(){
//    if (art == 1) return "Artikl";
    if (art == 2) return "Grupa";
    if (art == 3) return "Dio šifre";
    return "";
  }

  public String getOZNAKAARTIKL(){
//    if (art == 1) return isa2.rpcart.getCART();
    if (art == 2) return isa2.rpcart.getCGRART();
    if (art == 3) return isa2.rpcart.getCART1();
    return "";
  }

  public String getNAZIVARTIKL(){
//    if (art == 1) return isa2.rpcart.getNAZART();
    if (art == 2) {
      if (lookupData.getlookupData().raLocate(dm.getGrupart(),"CGRART",isa2.rpcart.getCGRART())){
        return dm.getGrupart().getString("NAZGRART");
      }
    }
    if (art == 3) return "";
    return "";
  }

}
