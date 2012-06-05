/****license*****************************************************************
**   file: repMemo.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Logotipovi;
import hr.restart.baza.dM;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.dlgGetKnjig;
import hr.restart.zapod.raKnjigChangeListener;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repMemo  {
  _Main main;

  dM dm= dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  DataSet ds = Logotipovi.getDataModule().
    getTempSet(Condition.equal("CORG", OrgStr.getKNJCORG(false)));

//  DataSet ds = Util.getNewQueryDataSet("select * from Logotipovi where corg='"+OrgStr.getKNJCORG()+"'",true);
//  dM.getDataModule().getLogotipovi();
  repUtil ru = repUtil.getrepUtil();
  String corg="";

  private static repMemo rpm ;

  public static repMemo getrepMemo(String corgC){
    if (rpm==null)
      rpm= new repMemo(false);
    if (rpm.ds.rowCount() == 0) {
      rpm.ds = Logotipovi.getDataModule().
      getTempSet(Condition.equal("CORG", OrgStr.getKNJCORG(false)));
      rpm.ds.open();
    }
    rpm.corg = corgC;
    rpm.posLogotip();
    return rpm;
  }

  public static repMemo getrepMemo(){
    return getrepMemo(dlgGetKnjig.getKNJCORG(false));
  }

  public void posLogotip() {
    if (!ld.raLocate(ds,new String[] {"CORG"},new String[] {corg})) {
      corg = dlgGetKnjig.getKNJCORG(false);
      ld.raLocate(ds,new String[] {"CORG"},new String[] {corg});
    }
  }
  public repMemo() {
    this(true);
  }
  protected repMemo(boolean poslogotip) {
    corg = OrgStr.getKNJCORG(false);
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String newk, String oldk) {
        corg = OrgStr.getKNJCORG(false);
        Logotipovi.getDataModule().setFilter((QueryDataSet) ds, Condition.equal("CORG", corg));
        ds.open();
      }
    });

    ds.open();
    if (poslogotip) posLogotip();
  }

  public boolean test(){
    return hr.restart.sisfun.frmParam.getParam("robno","ispLogo","N","Ispis loga na dokumentima (D/N)").equals("D");
  }

  public void close(){}

  public String getFirstLine(){
    return ds.getString("nazivlog");
  }
  public String getSecondLine(){
    return ds.getString("adresa");
  }
  public String getThirdLine(){
    return (ds.getInt("pbr") == 0 ? (ds.getString("OIB").length() == 0 ? "" : "OIB " + ds.getString("OIB")) :    
        ds.getInt("pbr") + " " + ds.getString("mjesto") + (ds.getString("OIB").length() == 0 ? "" :
    	", OIB " + ds.getString("OIB")));
  }
  
  public String getOneLine() {
    return ds.getString("nazivlog") + ", " + ds.getString("adresa") + (ds.getInt("pbr") == 0 ? "" : ", " +
      ds.getInt("pbr") + " " + ds.getString("mjesto")) +
      (ds.getString("OIB").length() == 0 ? "" : ", OIB " + ds.getString("OIB")); 
  }

  public String getLogoCorg(){return test()?ds.getString("corg"):"";}
  public String getLogoNazivlog(){return test()?ds.getString("nazivlog"):"";}
  public String getLogoMjesto(){return test()?ds.getString("mjesto"):"";}
  public String getLogoPbr(){return test()?String.valueOf(ds.getInt("pbr")):"";}
  public String getLogoAdresa(){return test()?ds.getString("adresa"):"";}
  public String getLogoZiro(){return   test()?ds.getString("ziro"):"";}
  public String getLogoMatbroj(){return test()?ds.getString("matbroj"):"";}
  public String getLogoOIB(){return test()?ds.getString("OIB"):"";}
  public String getLogoSifdjel(){return test()?ds.getString("sifdjel"):"";}
  public String getLogoPorisp(){return  test()?ds.getString("porisp"):"";}
  public String getLogoFax(){return test()?ds.getString("fax"):"";}
  public String getLogoTel1(){return test()?ds.getString("tel1"):"";}
  public String getLogoTel2(){return test()?ds.getString("tel2"):"";}
  public int getTopMargin(){return test()?0:ds.getInt("topmargin");}
//  public int getTopMargin(){return test()?ds.getInt("topmargin"):0;}
}