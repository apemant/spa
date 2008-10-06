/****license*****************************************************************
**   file: repDNR.java
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

import java.math.BigDecimal;
import java.sql.Date;

import com.borland.dx.dataset.DataSet;

public class repDNR implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmDNR frDNR = frmDNR.getInstance();
  DataSet ds = frDNR.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repDNR() {
    ru.setDataSet(ds);
  }

  public repDNR(int idx) {
//    if (idx == 0){
//    }
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
        ds.open();
      }
      int indx=0;
      public Object nextElement() {

        return new repDNR(indx++);
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
    return frDNR.getKnjNaziv();
  }

  public String getMATBR(){
    return frDNR.getKnjMatbroj();
  }

  public String getADRESA(){
    return frDNR.getKnjAdresa() + " " + frDNR.getKnjHpBroj() + " " + frDNR.getKnjMjesto();
  }

  public String getPOSLOPRIMAC_IME_PREZIME(){
     return ds.getString("IME")+" "+ds.getString("PREZIME");
  }

  public String getPosloprimac_adresa(){
     return ds.getString("ADRESA");
  }

  public String getDATUMISPLATE(){
     return rdu.dataFormatter(ds.getTimestamp("DATUMISPL"));
  }

  public Date getDATUMISPLATEDT(){
     return Date.valueOf(ds.getTimestamp("DATUMISPL").toString().substring(0,10));
  }

  public BigDecimal getBRUTO(){
     return ds.getBigDecimal("BRUTO");
  }

  public BigDecimal getDOPRINOSI(){
     return ds.getBigDecimal("DOPRINOSI");
  }

  public BigDecimal getDOP1(){
     return ds.getBigDecimal("DOP1");
  }

  public BigDecimal getDOP2(){
     return ds.getBigDecimal("DOP2");
  }

  public BigDecimal getOSIG(){
     return ds.getBigDecimal("OSIG");
  }

  public BigDecimal getISKNEOP(){
     return ds.getBigDecimal("ISKNEOP");
  }

  public BigDecimal getPOROSN(){
     return ds.getBigDecimal("POROSN");
  }

  public BigDecimal getPORIPRIR(){
     return ds.getBigDecimal("PORIPRIR");
  }

  public String getJMBG(){
     return ds.getString("JMBG");
  }
}
