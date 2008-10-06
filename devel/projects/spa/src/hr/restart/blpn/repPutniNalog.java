/****license*****************************************************************
**   file: repPutniNalog.java
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
package hr.restart.blpn;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;

public class repPutniNalog implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmPrijavaPN fpn = frmPrijavaPN.getFrmPrijavaPN();
  DataSet ds = fpn.getrep();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  public static double iznosGrupe;
  public static int rb;
  
  static int datumHack = -1; 

  public repPutniNalog() {
    ru.setDataSet(ds);
    
    String hack = frmParam.getParam("blpn", "datpn", "",
        "Datum prijave putnog naloga naštimati po datumu odlaska");
    if (hack.trim().length() == 0) datumHack = -1;
    else datumHack = Aus.getNumber(hack);
  }

  public repPutniNalog(int idx) {
    if(idx==0){
      rb = 0;
//      hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//      syst.prn(ds);
    }
    ds.goToRow(idx);
    ru.setDataSet(ds);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
      ds.open();
    }
    int indx=0;
    public Object nextElement() {

      return new repPutniNalog(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getDATUMODLASKA(){
    return rdu.dataFormatter(ds.getTimestamp("DATUMODL")).concat(".");
  }

  public String getRAZLOGPUTA(){
    return ds.getString("RAZLOGPUTA");
  }

  public String getMJESTA(){
    return ds.getString("MJESTA");
  }

  public BigDecimal getAKONTACIJA(){
    return ds.getBigDecimal("AKONTACIJA");
  }

  public String getCPN(){
    String cpn = ds.getString("CPN");
    return cpn;
  }

  public String getTRUDBENIK(){
    return fpn.getRadnika().toUpperCase();
  }

  public String getZVANJE(){
    dm.getRadnici().open();
    if(lookupData.getlookupData().raLocate(dm.getRadnici(),"CRADNIK",ds.getString("CRADNIK"))){
      return dm.getRadnici().getString("TITULA");
    }
    return "";
  }

  public String getRADNOMJESTO(){
    lookupData ld = lookupData.getlookupData();
    dm.getRadnici().open();
    dm.getRadnicipl().open();
    dm.getRadMJ().open();
    if (ld.raLocate(dm.getRadnicipl(),"CRADNIK",ds.getString("CRADNIK")) &&
        ld.raLocate(dm.getRadMJ(),"CRADMJ",dm.getRadnicipl().getString("CRADMJ"))){
      return dm.getRadMJ().getString("NAZIVRM");
    }
    return "";
  }

  public short getTRAJANJE(){
    return ds.getShort("TRAJANJE");
  }

  public String getPRIJEVOZ(){
    return fpn.getPrSr();
  }

  public String getVALUTA(){
    return fpn.getValuta();
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
  public String getUMJESTUDANA(){
    if (datumHack < 0)
      return getLogoMjesto() + ", dana " + getDatumIsp() + ".";
    
    Timestamp odl = ds.getTimestamp("DATUMODL");
    odl = Util.getUtil().addDays(odl, -datumHack);
    return getLogoMjesto() + ", dana " + rdu.dataFormatter(odl) + ".";
  }

  public String getCORG(){
    return ds.getString("CORG");
  }

  public String getNAZORG() {
     return ru.getSomething(new String[] {"CORG"},dm.getOrgstruktura(),"NAZIV").getString();
  }
}