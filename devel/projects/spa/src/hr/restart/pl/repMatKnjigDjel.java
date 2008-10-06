/****license*****************************************************************
**   file: repMatKnjigDjel.java
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
import hr.restart.baza.dM;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.StorageDataSet;

public class repMatKnjigDjel implements raReportData {

  /** @todo  moraš napravit statilèki geter u klasi u kojoj instanciraš ovaj ispis, i tu ga dohvatit */

  StorageDataSet ds = frmEvidencijaDjel.getInstance().getReportSet(); /** @todo ovdje preko statièkog getera dohvatiš željeni dataset.... */

  public repMatKnjigDjel() {
//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
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

  public String getCORG(){
    return ds.getString("CORG");
  }

  public String get1T(){
     return ds.getString("CRADNIK");
  }

  public String get1K(){
     return "";
  }

  public String get1R(){
     return "";
  }

  public String get2IMEPREZIME(){
     return ds.getString("PREZIME")+" "+ds.getString("IME");
  }

  public String get2SEX(){
     return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9022);
  }

  public String get2IMEOCAIMAJKE(){
     return ds.getString("IMEOCA")+", "+raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9023);
  }

  public String getJMBG(){
     return ds.getString("JMBG");
  }

  public String get4_1(){
     return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9041);
  }

  public String get4_2(){
     return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9042);
  }

  public String get4_3(){
     String pbr = raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9043);
     if (!lookupData.getlookupData().raLocate(dM.getDataModule().getAllMjesta(),"PBR",pbr)) return "";
     String grad = dM.getDataModule().getAllMjesta().getString("NAZMJESTA");
     return pbr + " " + grad;
  }

  public String get4_4(){
     if (get4_3().equals("")) return "";
     if (lookupData.getlookupData().raLocate(dM.getDataModule().getAllZpZemlje(),"CZEM",
        dM.getDataModule().getAllMjesta().getString("CZEM")) ) {

         return dM.getDataModule().getAllZpZemlje().getString("NAZIVZEM");
     } else return "";
  }

  public String get5_1(){
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9051);
  }

  public String get5_2(){
     String pbr = raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9052);
     if (!lookupData.getlookupData().raLocate(dM.getDataModule().getAllMjesta(),"PBR",pbr)) return "";
     String grad = dM.getDataModule().getAllMjesta().getString("NAZMJESTA");
     return pbr + " " + grad;
  }

  public String get5_3(){
     if (get5_2().equals("")) return "";
     if (lookupData.getlookupData().raLocate(dM.getDataModule().getAllZpZemlje(),"CZEM",
        dM.getDataModule().getAllMjesta().getString("CZEM")) ) {

         return dM.getDataModule().getAllZpZemlje().getString("NAZIVZEM");
     } else return "";
  }

  public String get6_1(){
     if (lookupData.getlookupData().raLocate(dM.getDataModule().getAllOrgstruktura(),"CORG",ds.getString("CORG"))) {
       return dM.getDataModule().getAllOrgstruktura().getString("MJESTO");
     } else return "";
  }

  public String get6_2(){
//     return ds.getString("");
    return get6_1();
  }

  public String get6_3(){
     return "Hrvatska";
  }

  public String get7(){
     return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9070);
  }

  public String get8_1(){
     return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9081);
  }

  public String get8_2(){
     return ds.getString("CSS");
  }

  public String get8_3(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9083);
  }

  public String get8_4(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9084);
  }

  public String get8_5(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9085);
  }

  public String get9(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9090);
  }

  public String get10(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9100);
  }

  public String get11(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9110);
  }

  public String get12(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9120);
  }

  public String get13(){
     //return ds.getString("");
    lookupData.getlookupData().raLocate(dM.getDataModule().getAllOrgstruktura(),"CORG",hr.restart.zapod.OrgStr.getKNJCORG());
    return dM.getDataModule().getAllOrgstruktura().getString("NAZIV");
  }

  public String get14(){
     //return ds.getString("");
    if (lookupData.getlookupData().raLocate(dM.getDataModule().getRadMJ(),"CRADMJ",ds.getString("CRADMJ")))
      return dM.getDataModule().getRadMJ().getString("NAZIVRM");
    else return "";
  }

  public String get15(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9150);
  }

  public String get16(){
     //return ds.getString("");
    if (lookupData.getlookupData().raLocate(dM.getDataModule().getVrodn(),"CVRO",ds.getString("CVRO")))
      return dM.getDataModule().getVrodn().getString("NAZIVRO");
    else return "";

  }

  public String get17_1(){
     return ds.getString("BRRADKNJ");
  }

  public String get17_2(){
     return ds.getString("REGBRRK");
  }

  public String get17_3(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9173);
  }

  public String get18(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9180);
  }

  public String get19_1(){
    //System.out.println("DATDOL = "+ds.getString("DATDOL"));
     return raIzvjestaji.formatDate(ds.getTimestamp("DATDOL"));
  }

  public String get19_2(){
  //   return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9192);
  }

  public String get20Datum(){
     //return ds.getString("DATODL");
    return raIzvjestaji.formatDate(ds.getTimestamp("DATODL"));
  }

  public String get20Razlog(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9202);
  }

  public String get21(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9210);
  }

  public String get22(){
//     return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9220);
  }

  public String get23(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9230);
  }

  public String get24(){
     //return ds.getString("");
    return raIzvjestaji.getCustomData_radnici(ds.getString("CRADNIK"),(short)9001,(short)9240);
  }
}