/****license*****************************************************************
**   file: repPKDisk_PL.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;

import com.borland.dx.dataset.DataSet;

public class repPKDisk_PL implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmPK frPK = frmPK.getPKInstance();
  DataSet ds = frPK.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  lookupData ld = lookupData.getlookupData();

  public repPKDisk_PL() {
    ru.setDataSet(ds);
  }

  public repPKDisk_PL(int idx) {
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

        return new repPKDisk_PL(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getCRADNIK(){
     return ds.getString("CRADNIK");
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

  public String getGodObr()
  {
    return "OBRAZAC IP za "+frPK.getGodPK(frPK.repSet)+". godinu";
  }
  public String getFake()
  {
    return "1";
  }

  public String getUser()
  {
    return raUser.getInstance().getImeUsera();
  }
  public String getTel()
  {
    String corg = OrgStr.getKNJCORG();//ds.getString("CORG");
    ld.raLocate(dm.getLogotipovi(), new String[]{"CORG"}, new String[]{corg});
    return dm.getLogotipovi().getString("TEL1");
  }

  public int getBrRad()
  {
//    return 1;
    return frPK.getPKInstance().brRad;
  }

  public String getPU()
  {
    String corg = OrgStr.getKNJCORG();
    ld.raLocate(dm.getLogotipovi(), new String[]{"CORG"}, new String[]{corg});
    return dm.getLogotipovi().getString("PORISP");
  }


  public String getDatumIsp()
  {
    return (frmParam.getParam("pl", "ippoprdat", "N","Ispisati datum sastavljanja na propratnom listu IP obrasca").equals("D"))?
        rdu.dataFormatter(vl.getToday()):"";
  }
}

