/****license*****************************************************************
**   file: repPOS.java
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

import hr.restart.util.VarStr;
import hr.restart.util.reports.raStringCache;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;

public class repPOS implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  DataSet ds = reportsQuerysCollector.getRQCModule().getQueryDataSet();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();

  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();
  
  private raStringCache cache = new raStringCache();


  public repPOS() {
//  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
//  ST.showInFrame(ds,"taratarataratara");
  }

  public repPOS(int idx) {
    if (idx == 0) {
      ru.setDataSet(ds);
      rekapPorez();
//      hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
//      ST.showInFrame(ds,"taratarataratara");
    }
	 ds.goToRow(idx);

  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
        ru.setDataSet(ds);
      }
      int indx=0;
      public Object nextElement() {
        return new repPOS(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public int getCPAR() {
	 return ds.getInt("CPAR");
  }

  public String getNAZPAR(){   
    String cached = cache.getValue("NAZPAR", ds.getInt("CPAR")+"");
    if (cached != null) return cached;
    colname[0] = "CPAR";
    String np = ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString();

    return cache.returnValue(np);
  }

//  public String getNAZPAR() {
//	 ru.setDataSet(ds);
//	 colname[0] = "CPAR";
//	 return ru.getSomething(colname,dm.getPartneri(),"NAZPAR").getString();
//  }

  public String getMJ() {
    String cached = cache.getValue("MJ", ds.getInt("CPAR")+"");    
    if (cached != null) return cached;
	 colname[0] = "CPAR";
	 return cache.returnValue(ru.getSomething(colname,dm.getPartneri(),"MJ").getString());
  }

  public String getADR() {
    String cached = cache.getValue("ADR", ds.getInt("CPAR")+"");    
    if (cached != null) return cached;
	 colname[0] = "CPAR";
	 return cache.returnValue(ru.getSomething(colname,dm.getPartneri(),"ADR").getString());
  }

  public int getPBR() {
	 colname[0] = "CPAR";
	 return ru.getSomething(colname,dm.getPartneri(),"PBR").getAsInt();
  }

  public String getMB() {
    String cached = cache.getValue("MB", ds.getInt("CPAR")+"");    
    if (cached != null) return cached;
	 colname[0] = "CPAR";
	 return cache.returnValue(ru.getSomething(colname,dm.getPartneri(),"MB").getString());
  }

  public String getZR() {
    String cached = cache.getValue("ZR", ds.getInt("CPAR")+"");    
    if (cached != null) return cached;
	 colname[0] = "CPAR";
	 return cache.returnValue(ru.getSomething(colname,dm.getPartneri(),"ZR").getString());
  }

  public String getCSKL() {
	 return ds.getString("CSKL");
  }

  public String getVRDOK() {
	 return ds.getString("VRDOK");
  }

  public String getGOD() {
	 return ds.getString("GOD");
  }

  public int getBRDOK() {
	 return ds.getInt("BRDOK");
  }

  public String getUI() {
	 return ds.getString("UI");
  }

  public Timestamp getSYSDAT() {
	 return ds.getTimestamp("SYSDAT");
  }

  public String SgetSYSDAT() {
	 return rdu.dataFormatter(getSYSDAT());
  }

  public Timestamp getDATDOK() {
	 return ds.getTimestamp("DATDOK");
  }
  public String SgetDATDOK() {
	 return rdu.dataFormatter(getDATDOK());
  }

  public int getPJ() {
	 return ds.getInt("PJ");
  }

  public String getCORG() {
	 return ds.getString("CORG");
  }

  public String getCVRTR() {
	 return ds.getString("CVRTR");
  }

  public String getCUG() {
	 return ds.getString("CUG");
  }

  public Timestamp getDATUG() {
	 return ds.getTimestamp("DATUG");
  }
  public String SgetDATUG() {
	 return rdu.dataFormatter(getDATUG());
  }

  public Timestamp getDVO() {
	 return ds.getTimestamp("DVO");
  }

  public String SgetDVO() {
	 return rdu.dataFormatter(getDVO());
  }

  public short getDDOSP() {
	 return ds.getShort("DDOSP");
  }

  public Timestamp getDATDOSP() {
	 return ds.getTimestamp("DATDOSP");
  }

  public String SgetDATDOSP() {
	 return rdu.dataFormatter(getDATDOSP());
  }

  public String getBRDOKIZ() {
	 return ds.getString("BRDOKIZ");
  }

  public Timestamp getDATDOKIZ() {
	 return ds.getTimestamp("DATDOKIZ");
  }

  public String SgetDATDOKIZ() {
	 return rdu.dataFormatter(getDATDOKIZ());
  }

  public String getBRPRD() {
	 return ds.getString("BRPRD");
  }

  public Timestamp getDATPRD() {
	 return ds.getTimestamp("DATPRD");
  }

  public String SgetDATPRD() {
	 return rdu.dataFormatter(getDATPRD());
  }

  public String getBRNARIZ() {
	 return ds.getString("BRNARIZ");
  }

  public Timestamp getDATNARIZ() {
	 return ds.getTimestamp("DATNARIZ");
  }

  public String SgetDATNARIZ() {
	 return rdu.dataFormatter(getDATNARIZ());
  }

  public String getOZNVAL() {
	 return ds.getString("OZNVAL");
  }

  public BigDecimal getTECAJ() {
	 return ds.getBigDecimal("TECAJ");
  }

  public String getBRNAL() {
	 return ds.getString("BRNAL");
  }

  public Timestamp getDATKNJ() {
	 return ds.getTimestamp("DATKNJ");
  }

  public String SgetDATKNJ() {
	 return rdu.dataFormatter(getDATKNJ());
  }

  public String getCRADNAL() {
	 return ds.getString("CRADNAL");
  }

  public Timestamp getDATRADNAL() {
	 return ds.getTimestamp("DATRADNAL");
  }

  public String SgetDATRADNAL() {
	 return rdu.dataFormatter(getDATRADNAL());
  }

  public String getSTATUS1() {
	 return ds.getString("STATUS1");
  }

  public String getSTATKNJ() {
	 return ds.getString("STATKNJ");
  }

  public String getSTATPLA() {
	 return ds.getString("STATPLA");
  }

  public String getSTATIRA() {
	 return ds.getString("STATIRA");
  }

  public String getCFRA() {
	 return ds.getString("CFRA");
  }

  public String getCNACPL() {
	 return ds.getString("CNACPL");
  }

  public String getCNAMJ() {
	 return ds.getString("CNAMJ");
  }

  public String getCNAC() {
	 return ds.getString("CNAC");
  }

  public String getCNAP() {
	 return ds.getString("CNAP");
  }

  public BigDecimal getUPZT() {
	 return ds.getBigDecimal("UPZT");
  }

  public String getCSHZT() {
	 return ds.getString("CSHZT");
  }

  public BigDecimal getUPRAB() {
	 return ds.getBigDecimal("UPRAB");
  }

  public String getCSHRAB() {
	 return ds.getString("CSHRAB");
  }

  public String getOPIS() {
	 return ds.getString("OPIS");
  }

  public short getRBR() {
	 return ds.getShort("RBR");
  }

  public String getNAZSKL() {
    hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
    dm.getOrgstruktura().open();
    if (ld.raLocate(dm.getOrgstruktura(),"CORG",ds.getString("CSKL"))){
      return dm.getOrgstruktura().getString("NAZIV");
    }
    return "";
//    ru.setDataSet(ds);
//    colname[0] = "CSKL";
//    return ru.getSomething(colname,dm.getOrgstruktura(),"NAZIV").getString();
//    return dm.getSklad().getString("NAZSKL");
  }

  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
  }

  public String getNAZART() {
	 return ds.getString("NAZART");
  }

  public String getJM() {
	 return ds.getString("JM");
  }

  public BigDecimal getKOL() {
	 return ds.getBigDecimal("KOL");
  }

  public BigDecimal getUPRAB1() {
	 return ds.getBigDecimal("UPRAB1");
  }

  public double getUIRAB() {
	 return ds.getBigDecimal("UIRAB").doubleValue();
  }

  public BigDecimal getUPZT1() {
	 return ds.getBigDecimal("UPZT1");
  }

  public BigDecimal getUIZT() {
	 return ds.getBigDecimal("UIZT");
  }

  public BigDecimal getFC() {
	 return ds.getBigDecimal("FC");
  }

  public double getINETO() {
	 return ds.getBigDecimal("INETO").doubleValue();
  }

  public BigDecimal getFVC() {
	 return ds.getBigDecimal("FVC");
  }
/*
  public BigDecimal getIPRODBP() {
	 return ds.getBigDecimal("IPRODBP");
  }

  public BigDecimal getPOR1() {
	 return ds.getBigDecimal("POR1");
  }

  public BigDecimal getPOR2() {
	 return ds.getBigDecimal("POR2");
  }

  public BigDecimal getPOR3() {
	 return ds.getBigDecimal("POR3");
  }
*/
  public double getIPRODBP() {
	 return ds.getBigDecimal("IPRODBP").doubleValue();
  }

  public double getPOR1() {
	 return ds.getBigDecimal("POR1").doubleValue();
  }

  public double getPOR2() {
	 return ds.getBigDecimal("POR2").doubleValue();
  }

  public double getPOR3() {
	 return ds.getBigDecimal("POR3").doubleValue();
  }


  public double getUKPOR3() {
	 return getPOR1()+getPOR2()+getPOR3();
  }

  public BigDecimal getFMC() {
	 return ds.getBigDecimal("FMC");
  }

/*
  public BigDecimal getIPRODSP() {
	 return ds.getBigDecimal("IPRODSP");
  }
*/
  public double getIPRODSP() {
	 return ds.getBigDecimal("IPRODSP").doubleValue();
  }

  public BigDecimal getNC() {
	 return ds.getBigDecimal("NC");
  }

  public BigDecimal getINAB() {
	 return ds.getBigDecimal("INAB");
  }

  public BigDecimal getIMAR() {
	 return ds.getBigDecimal("IMAR");
  }

  public BigDecimal getVC() {
	 return ds.getBigDecimal("VC");
  }

  public BigDecimal getIBP() {
	 return ds.getBigDecimal("IBP");
  }

  public double getIPOR() {
	 return (ds.getBigDecimal("POR1").add(ds.getBigDecimal("POR2")).add(ds.getBigDecimal("POR3"))).doubleValue();
  }

  public BigDecimal getMC() {
	 return ds.getBigDecimal("MC");
  }

  public BigDecimal getISP() {
	 return ds.getBigDecimal("ISP");
  }

  public BigDecimal getZC() {
	 return ds.getBigDecimal("ZC");
  }

  public double getIRAZ() {
	 return ds.getBigDecimal("IRAZ").doubleValue();
  }

  public String getBRPRI() {
	 return ds.getString("BRPRI");
  }

  public short getRBRPRI() {
	 return ds.getShort("RBRPRI");
  }

  public String getNAZNAP() {
//	 ru.setDataSet(ds);
	 colname[0] = "CNAP";
	 return ru.getSomething(colname,dm.getNapomene(),"NAZNAP").getString();
  }

  public String getNAZNACPL() {
//	 ru.setDataSet(ds);
	 colname[0] = "CNACPL";
	 return ru.getSomething(colname,dm.getNacpl(),"NAZNACPL").getString();
  }

  public String getNAZNAC() {
//	 ru.setDataSet(ds);
	 colname[0] = "CNAC";
	 return ru.getSomething(colname,dm.getNacotp(),"NAZNAC").getString();
  }

  public String getNAZFRA() {
//	 ru.setDataSet(ds);
	 colname[0] = "CFRA";
	 return ru.getSomething(colname,dm.getFranka(),"NAZFRA").getString();
  }
  public String getFormatBroj(){
	 return ru.getFormatBroj();
  }

  public String getPorezPos() {
//	 ru.setDataSet(ds);
	 colname[0]="CART";
	 return ru.getSomething(colname,dm.getArtikli(),"CPOR").getString();
  }

  public String getPor1Naz(){
//	 getPorezPos();
	 colname[0] ="CPOR";
	 return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"nazpor1").getString();
  }
  public int getImaPor1(){
	 return (getPor1Naz().equals(""))?0:1;
  }
  public String getPor2Naz(){
//	 ru.setDataSet(ds);
	 getPorezPos();
	 colname[0] ="CPOR";
	 return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"nazpor2").getString();
  }
  public int getImaPor2(){
	 return (getPor2Naz().equals(""))?0:1;
  }
  public String getPor3Naz(){
//	 ru.setDataSet(ds);
	 getPorezPos();
	 colname[0] ="CPOR";
	 return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"nazpor3").getString();
  }
  public int getImaPor3(){
	 return (getPor3Naz().equals(""))?0:1;
  }
  public BigDecimal getPOSPor1(){
   /*ru.setDataSet(ds);
   getPorezPos();
   colname[0] ="CPOR";
   return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"por1").getBigDecimal();
   */
   return ds.getBigDecimal("PPOR1");
  }
  public BigDecimal getPOSPor3(){
   /*ru.setDataSet(ds);
   getPorezPos();
   colname[0] ="CPOR";
   return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"por3").getBigDecimal();
   */
   return ds.getBigDecimal("PPOR3");
  }
  public BigDecimal getPOSPor2(){
   /*ru.setDataSet(ds);
   getPorezPos();
   colname[0] ="CPOR";
   return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"por2").getBigDecimal();
   */
   return ds.getBigDecimal("PPOR2");
  }

  public BigDecimal getPor1p2p3Naz(){
    /*
   ru.setDataSet(ds);
   getPorezPos();
   colname[0] ="CPOR";
   return ru.getSomething2(colname,colname,dm.getArtikli(),dm.getPorezi(),"ukupor").getBigDecimal();
   */
   return ds.getBigDecimal("PPOR1").add(ds.getBigDecimal("PPOR2")).add(ds.getBigDecimal("PPOR3"));
  }
  public String getSLOVIMA() {
	 return ut.numToLet(ds.getBigDecimal("UIRAC").doubleValue());
  }
  public int getLogoPotvrda() {
	 if (rm.test()) return 1;
	 return 0;
  }
  public String getLogoFullAdr() {
	 if (!rm.test()) return "";
	 else return rm.getLogoAdresa() + " ,  " + rm.getLogoPbr() + " " + rm.getLogoMjesto();
  }
  public String getLineName() {
	 return ClassLoader.getSystemResource("hr/restart/robno/reports/line2.jpg").toString();
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
  public String getLogoCorg(){return rm.getLogoCorg();}
  public String getLogoNazivlog(){return rm.getLogoNazivlog();}
  public String getLogoMjesto(){  return rm.getLogoMjesto();}
  public String getLogoMjestoZarez(){  return dm.getLogotipovi().getString("MJESTO")+",";}
  public String getLogoPbr(){return rm.getLogoPbr();}
  public String getLogoAdresa(){return rm.getLogoAdresa();}
  public String getLogoZiro(){return   rm.getLogoZiro();}
  public String getLogoMatbroj(){return rm.getLogoMatbroj();}
  public String getLogoSifdjel(){return rm.getLogoSifdjel();}
  public String getLogoPorisp(){return rm.getLogoPorisp();}
  public String getLogoFax(){return rm.getLogoFax();}
  public String getLogoTel1(){return rm.getLogoTel1();}
  public String getLogoTel2(){return rm.getLogoTel2();}
  
  
  // rekapitulacija poreza :)
  

  reportsQuerysCollector repQC = reportsQuerysCollector.getRQCModule();

  static VarStr CPOR;
  static VarStr UKUPOR;
  static VarStr IPRODBP;
  static VarStr POR1;
  static VarStr separator;

  protected void rekapPorez(){
    CPOR=new VarStr();
    UKUPOR=new VarStr();
    IPRODBP=new VarStr();
    POR1=new VarStr();
    separator=new VarStr();
    DataSet qds_porez = repQC.getPoreziSet(ds.getString("CSKL"), ds.getString("VRDOK"), ds.getString("GOD"), ds.getInt("BRDOK"));
    if(!qds_porez.isOpen()) qds_porez.open();
    hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
    st.prn(qds_porez);
    qds_porez.first();
    do {
      CPOR.append(qds_porez.getString("CPOR")).append("\n");
      UKUPOR.append(sgQuerys.getSgQuerys().format(qds_porez, "UKUPOR")).append("\n");
      IPRODBP.append(sgQuerys.getSgQuerys().format(qds_porez, "IPRODBP")).append("\n");
      POR1.append(sgQuerys.getSgQuerys().format(qds_porez, "POR1")).append("\n");
      separator.append("-\n");
    } while (qds_porez.next());

  }
  public String getPorezDepartmentCPOR(){
    return CPOR.toString();
  }
  public String getPorezDepartmentUKUPOR(){
    return UKUPOR.toString();
  }
  public String getPorezDepartmentIPRODBP(){
    return IPRODBP.toString();
  }
  public String getPorezDepartmentPOR1(){
    return POR1.toString();
  }
  public String getPorezDepartmentCrtica(){
    return separator.toString();
  }

}
