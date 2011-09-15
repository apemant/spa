/****license*****************************************************************
**   file: repStatPar.java
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

public class repStatPar implements raReportData { //sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  ispStatPar/*Dva*/ isp = ispStatPar/*Dva*/.getInstance();
  DataSet ds = isp.getReportQDS();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  public static double iznosGrupe;
  public static double sumIzGrupe;
  private static java.math.BigDecimal ruc;
  private static java.math.BigDecimal inab;
  private static int art;
  
  boolean isprab = isp.isIspRab();

  public repStatPar() {
      sumIzGrupe = 0;
      art = isp.getWhatIsCart();
      ruc = new java.math.BigDecimal("0.00");
      inab = new java.math.BigDecimal("0.00");
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

  public String getPoCemu() {
    if(isp.isPoRacunima()) return "\n" + "PO RAÈUNIMA";
    return "\n" + "UKUPNO";
  }
  
  public String getPRIKAZ() {
    if(isp.isPoRacunima()) return "Po raèunima";
    return "Ukupno za kupca";
  }
  public String getSLIJED() {
    return isp.getSlijed();
  }
  public String getVRART() {
    return isp.getVrsteArt();
  }
  
  public String getODDATUMA() {
    return rdu.dataFormatter(isp.getPocDatum());
  }
  public String getDODATUMA() {
    return rdu.dataFormatter(isp.getZavDatum());
  }
  public String getPodnaslov() {
    return getODDATUMA() + " do " + getDODATUMA();
  }
  public String getCSKL() {
    return isp.getCskl();
  }
  public String getNAZSKL() {
      if (isp.getCskl().equals("")) return "Sva skladišta";
    lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL", isp.getCskl());
    return dm.getSklad().getString("NAZSKL");
  }
  public String getCORG() {
    return isp.getCorg();
  }
  public String getNAZORG() {
    return isp.getCorgNaziv();
  }

  public String getCpar() {
    return isp.getCpar();
  }

  public String getNazPar() {
    return isp.getNazPar();
  }

  public String getCkup() {
    return isp.getCkup();
  }
  public String getNazKupca() {
    if (!isp.getPjCkup().equals("")){
//      System.out.println("REPSTATARTDVA - isp.getPjCkupac postoji!!!   overwiew: ckup - '"+isp.getCkup()+"' pjCkup - '"+isp.getPjCkup()+"'");
      if (lookupData.getlookupData().raLocate(dm.getPjpar(),new String[] {"CPAR","PJ"},new String[] {isp.getCkup(),isp.getPjCkup()})){
//        System.out.println("locirana poslovna jedinica u tablicipjpar ritrnam " + isp.getNazKup() + " poslovna jedinica " + dm.getPjpar().getString("NAZPJ") + " " + dm.getPjpar().getString("ADRPJ") + " " + dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ"));
        return isp.getNazKup() + " poslovna jedinica " + dm.getPjpar().getString("NAZPJ") + " " + dm.getPjpar().getString("ADRPJ") + " " + dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ");
      } //else System.out.println("nista u lookupu");
    }
    return isp.getNazKup();


    /*if (!isa2.getPjCkup().equals("")){
      if (lookupData.getlookupData().raLocate(dm.getPjpar(),new String[] {"CPAR","PJ"},new String[] {isa2.getCkup(),isa2.getPjCkup()})){
        return isa2.getNazKup() + " poslovna jedinica " + dm.getPjpar().getString("NAZPJ") + " " + dm.getPjpar().getString("ADRPJ") + " " + dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ");
      }
    }
    return isa2.getNazKup();*/


//    if (!isp.getPjCkup().equals("")){
//      if (lookupData.getlookupData().raLocate(dm.getPjpar(),new String[] {"CPAR","PJ"},new String[] {isp.getCkup(),isp.getPjCkup()})){
//        return isp.getNazKup() + " poslovna jedinica " + dm.getPjpar().getString("NAZPJ") + " " + dm.getPjpar().getString("ADRPJ") + " " + dm.getPjpar().getString("PBRPJ") + " " + dm.getPjpar().getString("MJPJ");
//      }
//    }

//    return isp.getNazKup();
  }

  public int getCKUPAC() {
    return ds.getInt("CPAR");
  }
  public String getNAZKUPAC() {
    return ds.getString("NAZPAR");
  }
  
  public String getCGRART(){
    if (ds.hasColumn("CGRART") == null) return "";
    if (ds.getString("CGRART").equals("X_X")) return "";
    return ds.getString("CGRART");
  }
  
  public String getNAZGRART(){
    if (ds.hasColumn("CGRART") == null) return "";
    if (lookupData.getlookupData().raLocate(dm.getGrupart(),"CGRART",ds.getString("CGRART")))
      return dm.getGrupart().getString("NAZGRART");
    return "Ostalo";
  }

  public String getLABELARTIKL(){
    if (art == 1) return "Artikl:";
    if (art == 2) return "Grupa:";
    if (art == 3) return "Dio šifre:";
    return "";
  }

  public String getOZNAKAARTIKL(){
    if (art == 1) return isp.rpcart.getCART();
    if (art == 2) return isp.rpcart.getCGRART();
    if (art == 3) return isp.rpcart.getCART1();
    return "";
  }

  public String getNAZIVARTIKL(){
    if (art == 1) return isp.rpcart.getNAZART();
    if (art == 2) {
      if (lookupData.getlookupData().raLocate(dm.getGrupart(),"CGRART",isp.rpcart.getCGRART())){
//        if (isp.rpcart.grupa????){
//
//        }
        return dm.getGrupart().getString("NAZGRART");
      }
    }
    if (art == 3) return "";
    return "";
  }


  /**
   * Ovdje je primjer kako prikazati cisti broj dokumenta za razliku od <b>formatBroj</b> metode koja vraca
   * broj sa "br." prefiksom.<br>
   * @return (ds.getString("CSKL").trim()+"/"+ds.getString("GOD")+"-"+
   * vl.maskZeroInteger(new Integer(ds.getInt("BRDOK")),6));
  */

  public String getBRDOK() {
    if(!isp.isPoRacunima()) return " ----------- ";
    return ds.getString("BRDOK");


        /*(isp.getCskl().trim()+"/"+ hr.restart.util.Util.getUtil().getYear(isp.getZavDatum()) +"-"+
            vl.maskZeroInteger(new Integer(ds.getInt("BRDOK")),6));*/
  }

  public double getIZNOSNAB() {
    inab = inab.add(ds.getBigDecimal("INAB"));
    return ds.getBigDecimal("INAB").doubleValue();
  }

  public double getIZNOS() {
    return ds.getBigDecimal("IPRODBP").doubleValue();
  }

  public double getRUC() {
    ruc = ruc.add(ds.getBigDecimal("RUC"));
    return ds.getBigDecimal("RUC").doubleValue();
  }

  public double getPRUC() {
    return ds.getBigDecimal("PRUC").doubleValue();
  }

  /*public double getPDV() {
    return ds.getBigDecimal("POR1").doubleValue();
  }

  public double getPOR2() {
    return ds.getBigDecimal("POR2").doubleValue();
  }

  public double getPOR3() {
    return ds.getBigDecimal("POR3").doubleValue();
  }*/

  public double getPOREZ() {
    return ds.getBigDecimal(isprab ? "IPRODSP" : "POR").doubleValue();
  }

  public double getUKUPNO() {
    return ds.getBigDecimal(isprab ? "ITOT" : "IPRODSP").doubleValue();
  }

  public double getUkuPRUC(){
    double ukuPRUC = ruc.doubleValue()*100.00/inab.doubleValue();
    return ukuPRUC;
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
    return rdu.dataFormatter(vl.getToday());
  }
}
