/****license*****************************************************************
**   file: repPK.java
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
import hr.restart.robno.repUtil;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import sg.com.elixir.reportwriter.datasource.IDataProvider;

import com.borland.dx.dataset.DataSet;

public class repPK implements raReportData, IDataProvider {

  hr.restart.robno._Main main;
  frmPK frPK = frmPK.getPKInstance();
  DataSet ds = frPK.getRepSetList();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repPK() {
    ru.setDataSet(ds);
//    sysoutTEST ST = new sysoutTEST(false);
//    ST.prn(ds);
  }

  public repPK(int idx) {
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

        return new repPK(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
//    ds = null;
//    ru.setDataSet(null);
  }

  public String getCRADNIK(){
     return ds.getString("CRADNIK");
  }
  public String getSORTNIPOJAM() {
    return ds.getString("SORTNIPOJAM")+" "+getCRADNIK();
  }
  public String getNAZIV(){
    return frPK.getKnjNaziv();
  }

  public String getMATBR(){
    return frPK.getKnjMatbroj();
  }

  public String getADRESA(){
    return frPK.getKnjAdresa() + " " + frPK.getKnjHpBroj() + " " + frPK.getKnjMjesto();
  }

  public String getPosloprimacAdr()
  {
    String cRad = ds.getString("CRADNIK");
    lookupData.getlookupData().raLocate(dm.getAllRadnicipl(), new String[]{"CRADNIK"}, new String[]{cRad+""});
    return dm.getAllRadnicipl().getString("ADRESA");
  }

  public String getPosloprimac()
 {
   String cRad = ds.getString("CRADNIK");
   lookupData.getlookupData().raLocate(dm.getAllRadnici(), new String[]{"CRADNIK"}, new String[]{cRad+""});
   return dm.getAllRadnici().getString("IME")+" "+ dm.getAllRadnici().getString("PREZIME");
  }

  public double getBRUTO(){
     return ds.getBigDecimal("BRUTO").doubleValue();
  }

  public double getDOPRINOSI(){
     return ds.getBigDecimal("DOPRINOSI").doubleValue();
  }

  public double getISKNEOP(){
     return ds.getBigDecimal("ISKNEOP").doubleValue();
  }

  public double getPOROSN(){
     return ds.getBigDecimal("POROSN").doubleValue();
  }

  public double getPORIPRIR(){
     return ds.getBigDecimal("PORIPRIR").doubleValue();
  }

  public double getOSIG(){
     return ds.getBigDecimal("OSIG").doubleValue();
  }

  public String getJMBG(){
    String cRad = ds.getString("cradnik");
    lookupData.getlookupData().raLocate(dm.getAllRadnicipl(), new String[] {"CRADNIK"}, new String[] {""+cRad});
    return dm.getAllRadnicipl().getString(raObracunPL.isOIB()?"OIB":"JMBG");
  }

  public String getGodObr()
  {

    return "III. PODACI O PLAÆI, MIROVINI, DOPRINOSIMA, POREZU I PRIREZU U "+ds.getShort("GODOBR")+". GODINI"; //   IDENTIFIKATOR "+getIdentifikator();
  }

  public String getIdentifikator() {
    return "1";
  }

  public String getMJOBR()
  {
//    String mjobr = vl.maskString(ut.getMonth(ds.getTimestamp("DATUMISPL")),'0',2);
//    if(ds.getShort("MJOBR")>9)
      return ds.getString("MJISPL");
//    return mjobr;
  }

  public String getNaslov()
  {
    return "III. PODACI O PLA\u0106I, POREZU I PRIREZU ZA "+ds.getShort("GODOBR")+". GODINU (PODACI IZ OBRASCA RP)";
  }
  //ip
  public String getCOPCINE() {
    String op = ds.getString("COPCINE");
    try {
      return vl.maskZeroInteger(new Integer(op),3);      
    } catch (Exception e) {
      //e.printStackTrace();
      return op;
    }
  }
  //ip
  public double getNETO() {
    //return ds.getBigDecimal("NETOPK").doubleValue();
    BigDecimal bnet = ut.setScale(ds.getBigDecimal("BRUTO")
        .add(ds.getBigDecimal("DOPRINOSI").negate())
        .add(ds.getBigDecimal("OSIG").negate()) //??
        .add(ds.getBigDecimal("PORIPRIR").negate())
        .add(ds.getBigDecimal("HARACH").negate())
        ,2);
    return bnet.doubleValue();
  }
  //ip
  public double getDOHODAK() {
    BigDecimal bdoh = ut.setScale(ds.getBigDecimal("BRUTO")
      .add(ds.getBigDecimal("DOPRINOSI").negate())
      .add(ds.getBigDecimal("OSIG").negate()),2);
    return bdoh.doubleValue();
  }

  public double getHARACH() {
    return ds.getBigDecimal("HARACH").doubleValue();
  }
  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.getRowCount();
  }
}
