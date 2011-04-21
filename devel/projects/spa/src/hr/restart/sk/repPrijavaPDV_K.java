/****license*****************************************************************
**   file: repPrijavaPDV_K.java
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
package hr.restart.sk;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repPrijavaPDV_K implements raReportData { // implements sg.com.elixir.reportwriter.datasource.IDataProvider {
// (jip) = jebo ime polja
  hr.restart.robno._Main main;
  frmPDV fPDV = frmPDV.getInstance();
  DataSet ds = null;      // = fPDV.getReportSet();


  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repPrijavaPDV_K() {
    ds = fPDV.getReportSet();
    ru.setDataSet(ds);
   }
/*
  public repPrijavaPDV_K(int idx) {
    if (idx == 0){
    }
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
      ds.open();
    }
    int indx=0;
    public Object nextElement() {
      return new repPrijavaPDV_K(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }
*/


  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }
  
  public void close() {
    ds = null;
    ru.setDataSet(null);
  }

  private String knjig(){
    return hr.restart.zapod.OrgStr.getKNJCORG();
  }

  public String getNAZIV(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return "\n"+dm.getLogotipovi().getString("NAZIVLOG");
  }

  public String getADRESA(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return dm.getLogotipovi().getString("ADRESA").concat("\n" + dm.getLogotipovi().getInt("PBR")).concat(" " + dm.getLogotipovi().getString("MJESTO"));
  }

  public String getPORISP(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return "\n"+dm.getLogotipovi().getString("PORISP");
  }

  public String getMB(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return dm.getLogotipovi().getString("MATBROJ");
  }
  
  public String getOIB(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return dm.getLogotipovi().getString("OIB");
  }

  public String getSIFDJEL(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return dm.getLogotipovi().getString("SIFDJEL");
  }

  public String getRAZDOBLJE(){
    String mjod = ut.getMonth(fPDV.getDatumOd());
    String mjdo = ut.getMonth(fPDV.getDatumDo());
    String danod = new java.util.StringTokenizer(rdu.dataFormatter(fPDV.getDatumOd()),".").nextToken();
    String dando = new java.util.StringTokenizer(rdu.dataFormatter(fPDV.getDatumDo()),".").nextToken();
    String god = ut.getYear(fPDV.getDatumOd());
    return "\nOD  " + danod + "  " + mjod + "  DO  " + dando + "  " + mjdo + "  GOD.  " + god;
  }
  
  public String getODLABEL() {
    return "OD";
  }
  
  public String getODDAN() {
    return ut.getDay(fPDV.getDatumOd());
  }

  public String getODMJ() {
    return ut.getMonth(fPDV.getDatumOd());
  }

  public String getDOLABEL() {
    return "DO";
  }

  public String getDODAN() {
    return ut.getDay(fPDV.getDatumDo());
  }

  public String getDOMJ() {
    return ut.getMonth(fPDV.getDatumDo());
  }
  
  public String getGODLABEL() {
    return "GOD";
  }
  
  public String getGOD() {
    return ut.getYear(fPDV.getDatumOd());
  }

  public BigDecimal getUKUPNO(){
     return ds.getBigDecimal("UKUPNO_I_II");
  }

  public BigDecimal getI(){
     return ds.getBigDecimal("UKUPNO_I");
  }

  public BigDecimal getI1(){
     return ds.getBigDecimal("BEZ_POREZA");
  }

  public BigDecimal getI2(){
     return ds.getBigDecimal("UKUPNO_I2");
  }

  public BigDecimal getI21(){
     return ds.getBigDecimal("TUZEMNE");//u stvari IZVOZNE od 2006 g. (jip)
  }

  public BigDecimal getI22(){
    return ds.getBigDecimal("PRIJEVOZ");//u stvari TUZEMNE od 2006 g. (jip)
  }
  
  public BigDecimal getI23(){
     return ds.getBigDecimal("IZVOZNE");//u stvari TUZEMNE od 2006 g. (jip)
  }
  
  public BigDecimal getI24(){
    return ds.getBigDecimal("OSTALO_I24");
  }
  
  public BigDecimal getI3(){
     return ds.getBigDecimal("STOPA_NULA");
  }

  public BigDecimal getIIv(){
     return ds.getBigDecimal("UKUPNO_II_V");
  }

  public BigDecimal getIIp(){
     return ds.getBigDecimal("UKUPNO_II_P");
  }

  public BigDecimal getII1v(){
     return ds.getBigDecimal("IZDANI_RACUNI_V");
  }

  public BigDecimal getII1p(){
     return ds.getBigDecimal("IZDANI_RACUNI_P");
  }

  public BigDecimal getII2v(){
     return ds.getBigDecimal("NEZARACUNANE_V");
  }

  public BigDecimal getII2p(){
     return ds.getBigDecimal("NEZARACUNANE_P");
  }
  
  public BigDecimal getII3v(){
    return ds.getBigDecimal("R23_V");
  }

  public BigDecimal getII3p(){
    return ds.getBigDecimal("R23_P");
  }

  public BigDecimal getII4v(){
     return ds.getBigDecimal("VLASTITA_POT_V");
  }

  public BigDecimal getII4p(){
     return ds.getBigDecimal("VLASTITA_POT_P");
  }

  public BigDecimal getII5v(){
     return ds.getBigDecimal("NENAP_IZVOZ_V");
  }

  public BigDecimal getII5p(){
     return ds.getBigDecimal("NENAP_IZVOZ_P");
  }

  /*public BigDecimal getII5v(){ //nema u 2005
     return ds.getBigDecimal("NAK_OSL_IZV_V");
  }

  public BigDecimal getII5p(){// nema u 2005
     return ds.getBigDecimal("NAK_OSL_IZV_P");
  }*/

  public BigDecimal getIIIv(){
     return ds.getBigDecimal("UKUPNO_III_V");
  }

  public BigDecimal getIIIp(){
     return ds.getBigDecimal("UKUPNO_III_P");
  }

  public BigDecimal getIII1v(){
     return ds.getBigDecimal("PPOR_PR_RAC_V");
  }

  public BigDecimal getIII1p(){
     return ds.getBigDecimal("PPOR_PR_RAC_P");
  }

  public BigDecimal getIII2v(){
     return ds.getBigDecimal("PL_PPOR_UVOZ_V");
  }

  public BigDecimal getIII2p(){
     return ds.getBigDecimal("PL_PPOR_UVOZ_P");
  }

  public BigDecimal getIII3v(){
     return ds.getBigDecimal("PL_PPOR_USLUGE_V");
  }

  public BigDecimal getIII3p(){
     return ds.getBigDecimal("PL_PPOR_USLUGE_P");
  }

  public BigDecimal getIII4(){//2005
     return ds.getBigDecimal("ISPRAVCI_PPORA");
  }
  
  public BigDecimal getIII4v(){ //2006
    return ds.getBigDecimal("III4V");
  }

  public BigDecimal getIII4p(){ //2006
    return ds.getBigDecimal("III4P");
  }

  public BigDecimal getIII5v(){ //2006
    return ds.getBigDecimal("III5V");
  }

  public BigDecimal getIII5p(){ //2006
    return ds.getBigDecimal("III5P");
  }
  
  public BigDecimal getIII6v(){ //2006
    return ds.getBigDecimal("III6V");
  }

  public BigDecimal getIII6p(){ //2006
    return ds.getBigDecimal("III6P");
  }
  
  public BigDecimal getIII7v(){ //2006
    return ds.getBigDecimal("III7V");
  }

  public BigDecimal getIII7p(){ //2006
    return ds.getBigDecimal("III7P");
  }
  
  public BigDecimal getIII8(){//2006
    return ds.getBigDecimal("ISPRAVCI_PPORA");
  }

  public BigDecimal getIV(){
     return ds.getBigDecimal("POR_OBV");
  }

  public BigDecimal getV(){
     return ds.getBigDecimal("PO_PRETHOD_OBR");
  }

  public BigDecimal getVI(){
     return ds.getBigDecimal("RAZLIKA");
  }

  public BigDecimal ifNul(BigDecimal bd) {
    if (bd!=null && bd.signum()==0) return null;
    return bd;
  }
  public BigDecimal getVII(){
     return ifNul(ds.getBigDecimal("UKUPNO_VII"));
  }

  public BigDecimal getVII1(){
     return ifNul(ds.getBigDecimal("UKUPNO_VII1"));
  }

  public BigDecimal getVII11(){
     return ifNul(ds.getBigDecimal("NAB_NEK_ISPOR"));
  }

  public BigDecimal getVII12(){
     return ifNul(ds.getBigDecimal("PRO_NEK_PRIM"));
  }

  public BigDecimal getVII13(){
     return ifNul(ds.getBigDecimal("NAB_OSOB_VOZIL"));
  }

  public BigDecimal getVII14(){
     return ifNul(ds.getBigDecimal("PRO_OSOB_VOZIL"));
  }

  public BigDecimal getVII15(){
     return ifNul(ds.getBigDecimal("NAB_OSTALO")/*ds.getBigDecimal("NAB_RAB_VOZIL")*/);
  }

  public BigDecimal getVII16(){
     return ifNul(ds.getBigDecimal("PRO_OSTALO"));
  }

  public BigDecimal getVII17(){
     return ifNul(ds.getBigDecimal("NAB_OSTALO"));
  }

  public BigDecimal getVII2(){
     return ifNul(ds.getBigDecimal("OTUDJ_STJEC"));
  }

  public BigDecimal getVII3(){
    return ifNul(ds.getBigDecimal("VII3"));
  }
  public BigDecimal getVII4(){
    return ifNul(ds.getBigDecimal("VII4"));
  }
  public BigDecimal getVII5(){
    return ifNul(ds.getBigDecimal("VII5"));
  }
  public BigDecimal getVII_KONTR(){
    return ifNul(ds.getBigDecimal("UKUPNO_VII1").add(ds.getBigDecimal("OTUDJ_STJEC")).add(ds.getBigDecimal("VII3")).add(ds.getBigDecimal("VII4")).add(ds.getBigDecimal("VII5")));
  }
  public String getVII_PREST() {
    return rdu.dataFormatter(ds.getTimestamp("POC_I_PREST"));
  }
//     return ifNul(ds.getBigDecimal("POC_I_PREST"));
  
  public BigDecimal getPOV(){
    return ds.getBigDecimal("POV");
  }
  
  public BigDecimal getPRED(){
    return ds.getBigDecimal("PRED");
  }
  
  public BigDecimal getUST(){
    return ds.getBigDecimal("UST");
  }

  public String getNAPOMENA(){
    return fPDV.getNapomena();
  }
}