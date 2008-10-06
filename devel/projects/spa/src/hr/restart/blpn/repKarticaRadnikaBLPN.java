/****license*****************************************************************
**   file: repKarticaRadnikaBLPN.java
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
import hr.restart.util.Valid;
import hr.restart.util.lookupData;

import java.sql.Date;

import com.borland.dx.dataset.DataSet;

public class repKarticaRadnikaBLPN implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  ispKarticaRadnikaBLPN ikra = ispKarticaRadnikaBLPN.getinstance();
  DataSet ds = ikra.getRepStDS();
  lookupData ld = lookupData.getlookupData();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repKarticaRadnikaBLPN() {
    ru.setDataSet(ds);
  }

  public repKarticaRadnikaBLPN(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
      }
      int indx=0;
      public Object nextElement() {

        return new repKarticaRadnikaBLPN(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getCPN(){
     return ds.getString("CPN");
  }

  public String getCRADNIK(){
     return ikra.getCradnik();
  }

  public String getIME(){
    ld.raLocate(dm.getRadnici(), "CRADNIK", ikra.getCradnik());
     return dm.getRadnici().getString("IME").concat(" " + dm.getRadnici().getString("PREZIME"));
  }

  public String getPREZIME(){
    ld.raLocate(dm.getRadnici(), "CRADNIK", ikra.getCradnik());
     return dm.getRadnici().getString("PREZIME").concat(" " + dm.getRadnici().getString("IME"));
  }

  public String getDATUM(){
     return rdu.dataFormatter(ds.getTimestamp("DATUM"));
  }

  public Date getDatumDT(){
     return Date.valueOf(ds.getTimestamp("DATUM").toString().substring(0,10));
  }

  public double getIZDATAK(){
     return ds.getBigDecimal("IZDATAK").doubleValue();
  }

  public double getPRIMITAK(){
     return ds.getBigDecimal("PVPRIMITAK").doubleValue();
  }
  
  public double getPVIZDATAK(){
    return ds.getBigDecimal("PVIZDATAK").doubleValue();
  }

  public double getPVPRIMITAK(){
    return ds.getBigDecimal("PRIMITAK").doubleValue();
  }
  
  public String getOZNVAL(){
    return ds.getString("OZNVAL");
  }

  public String getNAZVAL(){
    ld.raLocate(dm.getValute(), "OZNVAL", ds.getString("OZNVAL"));
    return dm.getValute().getString("NAZVAL");
  }

  public String getOPIS(){
    ld.raLocate(dm.getShkonta(), new String[] {"VRDOK","CSKL","STAVKA"},new String[] {ds.getString("VRDOK"), ds.getString("CSKL"), ds.getString("STAVKA")});
     return dm.getShkonta().getString("OPIS");
  }

  public String getVRDOK(){
     return ds.getString("VRDOK");
  }

  public String getZaPeriod(){
    String _od = rdu.dataFormatter(ikra.dateRangeOd());
    String _do = rdu.dataFormatter(ikra.dateRangeDo());
    String cega = "";
    if (ikra.jrbSve.isSelected()) cega = "Troškovi blagajne i putnih naloga\n";
    else if (ikra.jrbBL.isSelected()) cega = "Troškovi blagajne\n";
    else if (ikra.jrbPN.isSelected()) cega = "Troškovi putnih naloga\n";
    return cega.concat("u periodu od ".concat(_od.concat(" do ".concat(_do))));
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
}