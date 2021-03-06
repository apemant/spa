/****license*****************************************************************
**   file: repRobnoMiniSaldak.java
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

import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

public class repRobnoMiniSaldakAll implements raReportData {
  DataSet ds;
  raRobnoMiniSaldak rms = raRobnoMiniSaldak.getInstance();
  repMemo rm = repMemo.getrepMemo();
  Valid vl = Valid.getValid();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();

  public repRobnoMiniSaldakAll() {
//    pokazSal = 0.0;
    //ds = rms.getQDS();
    ds = rms.getDataSet();
    if (rms.getJPTV().getSelectionTracker().countSelected() > 0) {
      DataSet old = ds;
      ds = ((StorageDataSet) ds).cloneDataSetStructure();
      ds.open();
      for (old.first(); old.inBounds(); old.next())
        if (rms.getJPTV().getSelectionTracker().isSelected(old)) {
          ds.insertRow(false);
          old.copyTo(ds);
        }
    }    
    ru.setDataSet(ds);
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    ds = null;
    rms.getJPTV().destroySelectionView();
  }

  public String getCSKL(){
    if (rms.getCSKL().equals("")) return "-   ";
    return rms.getCSKL();
  }

  public String getNAZSKL(){
    if (lookupData.getlookupData().raLocate(dm.getSklad(),"CSKL",getCSKL()))
      return dm.getSklad().getString("NAZSKL");
    else
      return "Sva skladi�ta";
  }

  public String getCORG(){
    if (rms.getCORG().equals("")) return "-   ";
    return rms.getCORG();
  }

  public String getNAZORG(){
    if (lookupData.getlookupData().raLocate(dm.getOrgstruktura(),"CORG",getCORG()))
      return dm.getOrgstruktura().getString("NAZIV");
    else
      return "Sve org. jedinice";
  }

  public String getKLJUC(){
     return ds.getString("KLJUC");
  }

  public String getPNBZ2(){
     return ds.getString("PNBZ2");
  }

  public String getDVO(){
     return rdu.dataFormatter(ds.getTimestamp("DVO"));
  }

  public String getDATDOSP(){
     return rdu.dataFormatter(ds.getTimestamp("DATDOSP"));
  }

  public int getCPAR(){
    return ds.getInt("CPAR");
  }
  
  public int getPartner(){
    return ds.getInt("CPAR");
  }

  public String getNAZPAR(){
    if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+""))
      return dm.getPartneri().getString("NAZPAR");
    else
      return "";
  }
  
  public String getNAZPARL(){
    if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+""))
      return "\n"+ dm.getPartneri().getString("NAZPAR");
    else
      return "";
  }

  public String getMBPAR(){
    if (lookupData.getlookupData().raLocate(dm.getPartneri(),"CPAR",ds.getInt("CPAR")+""))
      return dm.getPartneri().getString("MB");
    else
      return "";
  }

  public String getMjestoIpbrPARTNERA() {
    if (lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", String.valueOf(ds.getInt("CPAR"))))
      return dm.getPartneri().getInt("PBR")+" "+dm.getPartneri().getString("MJ");
    else
      return "";
  }

  public String getAdresaPARTNERA() {
    if (lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR", String.valueOf(ds.getInt("CPAR"))))
      return dm.getPartneri().getString("ADR");
    else
      return "";
  }

  public String getCPARNAZPAR(){
    return vl.maskString(getCPAR()+"",' ',5) +" "+getNAZPAR();
  }

  public double getUIRAC(){
     return ds.getBigDecimal("UIRAC").doubleValue();
  }

  public double getPLATITI(){
     return ds.getBigDecimal("PLATITI").doubleValue();
  }

  public double getSALDO(){
//    pokazSal = pokazSal+ ds.getBigDecimal("SALDO").doubleValue();
     return ds.getBigDecimal("SALDO").doubleValue();
  }

  private String getStatus(){
    if (rms.getStatus().equalsIgnoreCase("SVI")) return "Svi ";
    if (rms.getStatus().equalsIgnoreCase("DA")) return "Pla�eni ";
    return "Nepla�eni ";
  }

  private String getDospjece(){
    if (rms.getDospjelo().equalsIgnoreCase("DA")) return "dospjeli ";
    if (rms.getDospjelo().equalsIgnoreCase("NE")) return "nedospjeli ";
    return "";
  }

  public String getStatusDospjece(){
    return "("+getStatus()+getDospjece()+"ra�uni)\n"+rdu.dataFormatter(rms.pocDat())+" - "+rdu.dataFormatter(rms.zavDat());
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

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}
