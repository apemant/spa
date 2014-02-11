/****license*****************************************************************
**   file: repIspList.java
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
package hr.restart.pl;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;
import hr.restart.util.reports.raReportDescriptor;

import java.math.BigDecimal;
import java.util.Arrays;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repIspList implements raReportData {// implements sg.com.elixir.reportwriter.datasource.IDataProvider {
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  frmIspList fil = frmIspList.getInstance();
  DataSet radnici = fil.getRadnici();

  raDateUtil rdu = raDateUtil.getraDateUtil();
  repMemo rpm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  public repIspList() {
    radnici.setSort(new SortDescriptor(new String[] {"PREZIME"}));
//    fil = frmIspList.getInstance();
//    radnici = fil.getRadnici();
//    System.out.println("who is who : " + fil);
//    System.out.println("this " + this);
  }
/*
  public repIspList(int idx) {
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(radnici);
    if (idx == 0){
//      System.out.println("fil " + fil);
    }
    radnici.goToRow(idx);
    fil.findStrings(radnici.getString("CRADNIK"), radnici.getShort("RBROBR"), radnici.getShort("MJOBR"), radnici.getShort("GODOBR"));
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
      radnici.enableDataSetEvents(false);
      radnici.open();
    }
    int indx=0;
    public Object nextElement() {
      return new repIspList(indx++);
    }
    public boolean hasMoreElements() {
      if (indx < radnici.getRowCount()) return true;
      radnici.enableDataSetEvents(true);
      return false;
    }
    };
  }

  public void close() {
  }
*/



  public void close() {
    radnici = null;
  }
  public raReportData getRow(int i) {
    radnici.goToRow(i);
    fil.findStrings(radnici.getString("CRADNIK"), radnici.getShort("RBROBR"), radnici.getShort("MJOBR"), radnici.getShort("GODOBR"));
    return this;
  }
  public int getRowCount() {
    return radnici.rowCount();
  }
  
  
  
  public String getCORG() {
    return radnici.getString("CORG");
  }

  public String getNaziV() {
    fil.ld.raLocate(dm.getOrgstruktura(), new String[] {"CORG"}, new String[] {radnici.getString("CORG")});
    return dm.getOrgstruktura().getString("NAZIV");
  }

  public String getIsplataNa(){
    return fil.getIsplataString();
  }

  public String getRadnoMjesto() {
    return fil.getRadnoMjesto();
  }

  public String getNazivRadnogMjesta() {
    return fil.getNazivRadnogMjesta();
  }
  public String getInformationLine() {
    return fil.getInformLine();
  }

  public String getRadnik() {
    return radnici.getString("CRADNIK");
  }

  public String getPrezime() {
    return radnici.getString("PREZIME").concat(" " + radnici.getString("IME"));//.concat(" - " + radnici.getString("IMEOCA"));
  }

  public String getIme() {
    return radnici.getString("IME");
  }

  public String getImeOca() {
    return radnici.getString("IMEOCA");
  }

  // RASTEZLJIVI STRINGOVI ZA STAVKE

  public String getPrimanja() {
    return fil.getPrimanja();
  }

  public String getSati() {
    return fil.getSati();
  }

  public String getUcinak() {
    return fil.getUcinak();
  }

  public String getBruto() {
    return fil.getBruto();
  }

  public String getNeto() {
    return fil.getNeto();
  }

  public String getDoprinosi() {
    return fil.getDoprinosi();
  }
  
  public String getOsnovicaDop() {
    return fil.getOsnovicaDoprinosa();
  }
  
  public String getStopa() {
    return fil.getStopa();
  }
  
  public String getIznosDoprinosa() {
    return fil.getIznosDoprinosa();
  }
  
  public String getDoprinosiNa() {
    return fil.getDoprinosiNa();
  }

  public String getOsnovicaDopNa() {
    return fil.getOsnovicaDoprinosaNa();
  }

  public String getStopaNa() {
    return fil.getStopaNa();
  }

  public String getIznosDoprinosaNa() {
    return fil.getIznosDoprinosaNa();
  }

  public String getNaknade() {
    return fil.getNaknade();
  }

  public String getSatiNaknade() {
    return fil.getSatiNaknada();
  }

  public String getIznosNaknada() {
    return fil.getIznosNaknada();
  }

  public String getKrediti() {
    return fil.getKrediti();
  }

  public String getIznosKredita() {
    return fil.getIznosKredita();
  }

  // KRAJ - RASTEZLJIVI STRINGOVI


  public BigDecimal getTotalSati() {
    return radnici.getBigDecimal("SATI");
  }

  public BigDecimal getTotalBruto() {
    return radnici.getBigDecimal("BRUTO");
  }

  public BigDecimal getTotalNeto() {
    if (fil.getNetoColParam().equals("NETO")) {
      return radnici.getBigDecimal("NETO2");
    } else {
      return getDohodak();
    }
  }

  public BigDecimal getTotalDoprinosi() {
    return radnici.getBigDecimal("DOPRINOSI");
  }

  public BigDecimal getTotalStopa() {
    return fil.getTotalStopa();
  }
  public BigDecimal getTotalStopaNa() {
    return fil.getTotalStopaNa();
  }
  public BigDecimal getTotalDoprinosiNa() {
    return fil.getTotalIznosNa();
  }
  public BigDecimal getBruto2() {
    return getTotalDoprinosiNa().add(getTotalBruto());
  }
  public BigDecimal getDohodak() {
    return radnici.getBigDecimal("NETO");
  }

//  public BigDecimal getMinimalac() {
//    return dm.getParametripl().getBigDecimal("MINPL");
//  }
//
//  public BigDecimal getKoefOlaksice() {
//    return getNeoporezivo().divide(getMinimalac(), 2, BigDecimal.ROUND_HALF_UP);
//  }
  public BigDecimal getMinimalac() {
    return fil.getMinimalac(radnici); 
  }

  public BigDecimal getKoefOlaksice() {
    return fil.getKoefOlaksice(radnici);
  }
  
  public BigDecimal getNeoporezivo() {
    return radnici.getBigDecimal("NEOP");
  }

  public BigDecimal get3_1_odIDa(){
    return radnici.getBigDecimal("ZIVOTNOOSIG");
  }

  public BigDecimal get3_2_odIDa(){
    return radnici.getBigDecimal("ZDRAVSTVENOOSIG");
  }

  public BigDecimal get3_3_odIDa(){
    return radnici.getBigDecimal("MIROVINSKOOSIG");
  }

  public BigDecimal getNeoporezivoIskoristeno() {
    return radnici.getBigDecimal("ISKNEOP");
  }

  public BigDecimal getPorezOsnovica() {
    return radnici.getBigDecimal("POROSN");
  }

  public BigDecimal getPorez15() {
    return radnici.getBigDecimal("POR1");
  }

  public BigDecimal getPorez25() {
    return radnici.getBigDecimal("POR2");
  }

  public BigDecimal getPorez35() {
    return radnici.getBigDecimal("POR3");
  }

  public BigDecimal getPorez45() {
    return radnici.getBigDecimal("POR4");
  }

  public BigDecimal getTotalPorez() {
    return radnici.getBigDecimal("PORUK");
  }

  public BigDecimal getPrirez() {
    return radnici.getBigDecimal("PRIR");
  }

  public BigDecimal getTotalPorezPrirez() {
    return radnici.getBigDecimal("PORIPRIR");
  }

  public BigDecimal getTotalNaknade() {
    return radnici.getBigDecimal("NAKNADE");
  }

  public BigDecimal getNetoPlusNaknade() {
    return radnici.getBigDecimal("NETOPK");
  }

  public BigDecimal getTotalKrediti() {
    return radnici.getBigDecimal("KREDITI");
  }

  public String getLovaNaRukeKeshovina() {
    if (fil.getPrikazIsplate())
      return fil.format(radnici, "NARUKE");
    else
      return "";
  }

  public String getVIRMANINFO() {
    return 
        "DRŽAVNI PRORAÈUN REPUBLIKE HRVATSKE    RAÈUN: 1001005-1863000160 \n" +
        "DOPRINOS MIO I.STUP                    PNB:   (68) 8109-"+rpm.getLogoOIB()+"\n"+
        "\n"+
        "DRŽAVNI PRORAÈUN REPUBLIKE HRVATSKE    RAÈUN: 1001005-1700036001 \n" +
        "DOPRINOS MIO II.STUP                   PNB:   (68) 2003-"+rpm.getLogoOIB()+"\n"+
        "\n"+
        "POREZ I PRIREZ NA DOHODAK              RAÈUN: "+raVirPlMnWorker.getZiroOpc(_getCOpcine())+" \n" +
    		printMjestoPorez(39)+                  "PNB:   (68) 1406-"+rpm.getLogoOIB()+"\n"+
        "\n"+
    		
        "";
  }



  private String _getCOpcine() {
    QueryDataSet opset = Aus.q("select copcine from radnicipl where cradnik='"+getRadnik()+"'");
    opset.first();
    return opset.getString("COPCINE");
  }
  private String printMjestoPorez(int w) {
    String mj;
    QueryDataSet opset = Aus.q("select opcine.nazivop from opcine, radnicipl WHERE radnicipl.copcine = opcine.copcine and cradnik='"+getRadnik()+"'");
    if (opset.getRowCount()>0) {
      opset.first();
      mj = opset.getString("NAZIVOP");
    } else {
      mj="";
    }
    if (w>mj.length()) {
      char[] cont = new char[w-mj.length()];
      Arrays.fill(cont, ' ');
      return mj+new String(cont);
    } else {
      return mj.substring(0, w);
    }
  }
  public String getPor1txt() {
    return fil.getPor1txt(radnici);
  }
  public String getPor2txt() {
    return fil.getPor2txt(radnici);
  }
  public String getPor3txt() {
    return fil.getPor3txt(radnici);
  }
  public String getPor4txt() {
    return fil.getPor4txt(radnici);
  }
  public String getPrirtxt() {
    return fil.getPrirtxt(radnici);
  }
  
  public String getFirstLine(){
    return fil.shouldPrintLogo()?"":rpm.getOneLine()+"\nRN: "+rpm.getLogoZiro()/*+"  OIB: "+rpm.getLogoOIB()*/;
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
  public String getDatumIsplate(){
    return rdu.dataFormatter(radnici.getTimestamp("DATISP"));
  }
  public String getNADNASLOV(){
    if (fil.getRepMode() == 'A')
      return fil.getObracun(radnici.getShort("GODOBR"), radnici.getShort("MJOBR"), radnici.getShort("RBROBR"));
    return fil.getObracun();
  }
  public int getFINALSORTER(){
    int sort=0;
    if (fil.getRepMode() == 'A'){
      sort = radnici.getShort("GODOBR") * 10000 + radnici.getShort("MJOBR") * 100 + radnici.getShort("RBROBR");
    }
//    System.out.println("SORT : " + sort);
    return sort;
  }
}