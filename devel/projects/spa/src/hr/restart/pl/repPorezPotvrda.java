/****license*****************************************************************
**   file: repPorezPotvrda.java
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
import java.sql.Date;

import com.borland.dx.dataset.DataSet;

public class repPorezPotvrda implements raReportData {

  hr.restart.robno._Main main;
  frmPOTVRDA frPT = frmPOTVRDA.getInstance();
  DataSet ds = frPT.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();
  private static String[] _JMBG;

  public repPorezPotvrda() {
    System.out.println("repPorezPotvrda");
    ds.open();
    ru.setDataSet(ds);
    _JMBG = new String[]{"","","","","","","","","","","","",""};
    olJmbg = "";
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    cepajJMBG();
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    ds = null;
  }

  public String getPodnaslov(){
    return "\nO ISPLAÆENIM DOHOCIMA, UPLAÆENOM POREZU PO ODBITKU I PRIREZU ZA "+ds.getShort("GODOBR")+". GODINU";
  }


  public String getCRADNIK(){
    return ds.getString("CRADNIK");
  }

  public String getIMEPREZIME(){
    String imeiprezime = "";
    if (lookupData.getlookupData().raLocate(dm.getAllRadnici(),"CRADNIK",ds.getString("CRADNIK")))
      imeiprezime = "\n"+dm.getAllRadnici().getString("IME")+ " " +dm.getAllRadnici().getString("PREZIME");
    return imeiprezime;
  }

  public String getADRESARADNIKA(){
    String adresa = "";
    if (lookupData.getlookupData().raLocate(dm.getAllRadnicipl(),"CRADNIK",ds.getString("CRADNIK")))
      adresa = "\n"+dm.getAllRadnicipl().getString("ADRESA");
    return adresa;
  }

  public Date getDATUMISPLATEDT(){
    return Date.valueOf(ds.getTimestamp("DATUMISPL").toString().substring(0,10));
  }

  public BigDecimal getBRUTO(){
    return ds.getBigDecimal("BRUTO");
  }

  public BigDecimal getPORUK(){
    return ds.getBigDecimal("PORUK");
  }

  public BigDecimal getPRIR(){
    return ds.getBigDecimal("PRIR");
  }

  public BigDecimal getPORIPRIR(){
    return ds.getBigDecimal("PORIPRIR");
  }

  public BigDecimal getDOPS(){
    return ds.getBigDecimal("DOPRINOSI");
  }

  public BigDecimal getNETOPK(){
    return ds.getBigDecimal("NETOPK");
  }

  public String getVRODN(){
    lookupData.getlookupData().raLocate(dm.getVrodn(),"CVRO",ds.getString("CVRO"));
    return dm.getVrodn().getString("NAZIVRO");
  }

  public String getFirstLine(){
    return rm.getFirstLine();
  }

  public String getEnterFirstLine(){
    return "\n"+rm.getFirstLine();
  }

  public String getSecondLine(){
    return rm.getSecondLine();
  }

  public String getThirdLine(){
    return rm.getThirdLine();
  }

  public String getLowerLines(){
    return "\n"+rm.getSecondLine()+", "+rm.getThirdLine();
  }

  public String getMJESTODATUM(){
    String mjd = "";
    if (lookupData.getlookupData().raLocate(dm.getOrgstruktura(),"CORG",frPT.getCorg())){
      if (!dm.getOrgstruktura().getString("MJESTO").equals("")){
        mjd = dm.getOrgstruktura().getString("MJESTO")+", "+rdu.dataFormatter(vl.getToday());
      } else {
        mjd = rdu.dataFormatter(vl.getToday());
      }
    }
    return mjd;
  }

  private static String olJmbg = "";

  private void cepajJMBG(){
    if (lookupData.getlookupData().raLocate(dm.getAllRadnicipl(),"CRADNIK",ds.getString("CRADNIK"))){
      if (!dm.getAllRadnicipl().getString("JMBG").equals(olJmbg)){
        /*String jmbg*/ olJmbg = dm.getAllRadnicipl().getString("JMBG");
        System.out.println("jmbg = "+olJmbg);
//        olJmbg = jmbg;
        for (int i=0;i<13;i++ ) {
          _JMBG[i] = olJmbg.substring(i,i+1);
        }
      }
    }
  }

  public String getJMBG01(){
    return _JMBG[0];
  }

  public String getJMBG02(){
    return _JMBG[1];
  }

  public String getJMBG03(){
    return _JMBG[2];
  }

  public String getJMBG04(){
    return _JMBG[3];
  }

  public String getJMBG05(){
    return _JMBG[4];
  }

  public String getJMBG06(){
    return _JMBG[5];
  }

  public String getJMBG07(){
    return _JMBG[6];
  }

  public String getJMBG08(){
    return _JMBG[7];
  }

  public String getJMBG09(){
    return _JMBG[8];
  }

  public String getJMBG10(){
    return _JMBG[9];
  }

  public String getJMBG11(){
    return _JMBG[10];
  }

  public String getJMBG12(){
    return _JMBG[11];
  }

  public String getJMBG13(){
    return _JMBG[12];
  }
}