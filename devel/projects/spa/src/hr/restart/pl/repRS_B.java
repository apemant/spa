/****license*****************************************************************
**   file: repRS_B.java
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
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.dlgRunReport;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;
import java.util.HashMap;

import com.borland.dx.dataset.DataSet;

public class repRS_B implements sg.com.elixir.reportwriter.datasource.IDataProvider, raReportData {

  hr.restart.robno._Main main;
  frmRS fRS = frmRS.getInstance();
  DataSet ds = fRS.getRepSetRS_B();
  DataSet hs = fRS.getHead();
  static HashMap hm = new HashMap();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  static int brojreda = 0;
  public repRS_B() {
    ru.setDataSet(ds);
  }

  public repRS_B(int idx) {
    if (idx == 0){
      if (!hm.isEmpty()){
        hm.clear();
      }
      rb=0;
    }
    ds.goToRow(idx);
    brojreda = idx;
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      int c;
    {
      ds.open();
    }
    int indx=0;
    public Object nextElement() {

      return new repRS_B(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }

  public void close() {
  }

  static int rb = 0;

  public int getRBR(){
    if (!hm.containsKey(ds.getString("CRADNIK"))){
      hm.put(ds.getString("CRADNIK"), "nazochan");
      return ++rb;
    }
    return 0;
  }

  public int getBRSTR(){
    return (brojreda/12)+1;
  }

  public String getMB(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
    if (dm.getLogotipovi().getString("MATBROJ").length() < 13)
      return Valid.getValid().maskZeroInteger(Integer.valueOf(dm.getLogotipovi().getString("MATBROJ")), 8);

    return dm.getLogotipovi().getString("MATBROJ");
  }
  public String getOIB(){
    if (ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {hr.restart.zapod.OrgStr.getKNJCORG()})) {
      return dm.getLogotipovi().getString("OIB");
    }
    return "";
  }
//  public String getJMBGVL(){
//    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
//    Integer mb = Integer.valueOf(dm.getLogotipovi().getString("MATBROJ"));
//    if (dm.getLogotipovi().getString("MATBROJ").length() == 13)
//      return dm.getLogotipovi().getString("MATBROJ");
//    return "";
//  }

  public String getCRADNIK(){
     return ds.getString("CRADNIK");
  }

  public String getNAZRAD(){
    String ime, prezime;
    ld.raLocate(dm.getAllRadnici(), new String[] {"CRADNIK"}, new String[] {ds.getString("CRADNIK")});
    ime = dm.getAllRadnici().getString("IME").toUpperCase();
    prezime = dm.getAllRadnici().getString("PREZIME").toUpperCase();
    return prezime.concat(" "+ime);
  }

  public String getJMBG(){
     return ds.getString("JMBG");
  }
  public short getGODINA(){
    return hs.getShort("GODINA");
  }


  public String getCOPCINE(){
    return raIzvjestaji.convertCopcineToRS(ds.getString("COPCINE"));
  }
  public String getCOPCR() {
    return raIzvjestaji.convertCopcineToRS(ds.getString("COPCINE"));//fRS.getOpcinaRada();
  }
  public String getOBROSN(){
//    System.out.println("vrsta uplate : " + fRS.getVrstaUplate());
    String oo, i, b, z, obrosn;
    oo = ds.getString("RSOO").concat("  ");
    i = ds.getString("RSINV").concat("  ");
    b = ds.getString("RSB").concat("  ");
    z = ds.getString("RSZ");
//    if (fRS.getVrstaUplate().equals("03")){
//      obrosn = oo.concat("0  0  1");
//    } else {
    if (!dlgRunReport.getCurrentDlgRunReport().getCurrentDescriptor().getName().startsWith("hr.restart.pl.repRS_B")) {
      obrosn = oo+b;
    } else {
      obrosn = oo+i+b+z;
    }
//    }
    return obrosn;
  }

  public String getOBRSAT(){
//    if (fRS.getVrstaUplate().equals("03")){
//      return "0000";
//    } else {
      int h = ds.getBigDecimal("SATI").multiply(new BigDecimal("10")).intValue();
      String h2 = String.valueOf(h);
      Integer h2o = Integer.valueOf(h2);
      return vl.maskZeroInteger(h2o, 4);
//    }
  }

  public String getIDENTIFIKATOR(){
    return hs.getString("IDENTIFIKATOR");
  }

  public String getODDANA(){
//    if (fRS.getVrstaUplate().equals("03")){
//      return (short) 0;
//    } else {
      //return ds.getShort("ODDANA");
    return Valid.getValid().maskString(ds.getShort("ODDANA")+"",'0',2);
//    }
  }

  public String getDODANA(){
//    if (fRS.getVrstaUplate().equals("03")){
//      return (short) 0;
//    } else {
      //return ds.getShort("DODANA");
    return Valid.getValid().maskString(ds.getShort("DODANA")+"",'0',2);
//    }
  }

  public BigDecimal getBRUTOMJ(){
     return ds.getBigDecimal("BRUTOMJ");
  }

  public BigDecimal getBRUTO(){
     return ds.getBigDecimal("BRUTO");
  }

  public BigDecimal getMIO1(){
     return ds.getBigDecimal("MIO1");
  }

  public BigDecimal getMIO2(){
     return ds.getBigDecimal("MIO2");
  }

  public BigDecimal getZO(){
     return hs.getShort("GODINA")>=2003?Aus.zero2:ds.getBigDecimal("ZO");
  }

  public BigDecimal getZAPOS(){
     return hs.getShort("GODINA")>=2003?Aus.zero2:ds.getBigDecimal("ZAPOS");
  }

  public BigDecimal getPOREZ(){
     return ds.getBigDecimal("POREZ");
  }

  public BigDecimal getPREMOS(){
//    if (fRS.getVrstaUplate().equals("03")){
//      return Aus.zero2;
//    } else {
      return ds.getBigDecimal("PREMOS");
//    }
  }

  public BigDecimal getOSODB(){
     return ds.getBigDecimal("OSODB");
  }

  public BigDecimal getPRIREZ(){
     return ds.getBigDecimal("PRIREZ");
  }

  public BigDecimal getNETOPK(){
     return ds.getBigDecimal("NETOPK");
  }

  public raReportData getRow(int i) {
    //ds.goToRow(i);
    return new repRS_B(i);
  }

  public int getRowCount() {
    return ds.getRowCount();
  }  
}