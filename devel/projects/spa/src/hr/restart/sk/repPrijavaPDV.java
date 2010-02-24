/****license*****************************************************************
**   file: repPrijavaPDV.java
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
package hr.restart.sk;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repPrijavaPDV implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmPDV fPDV = frmPDV.getInstance();
  DataSet ds = fPDV.getReportSet();


  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repPrijavaPDV() {
    ru.setDataSet(ds);
   }

  public repPrijavaPDV(int idx) {
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
      return new repPrijavaPDV(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }

  public void close() {
  }

  private String knjig(){
    return hr.restart.zapod.OrgStr.getKNJCORG();
  }

  public String getNAZIV(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return "\n"+dm.getLogotipovi().getString("NAZIVLOG");
  }

  public String getADRESA(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return dm.getLogotipovi().getString("ADRESA").concat("\n" + dm.getLogotipovi().getInt("PBR")).concat(" " + dm.getLogotipovi().getString("MJESTO"));
  }

  public String getPORISP(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return "\n"+dm.getLogotipovi().getString("PORISP");
  }

  public String getMB(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return dm.getLogotipovi().getString("MATBROJ");
  }
  
  public String getOIB(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return dm.getLogotipovi().getString("OIB");
  }

  public String getSIFDJEL(){
    ld.raLocate(dm.getLogotipovi(), new String[] {"CORG"}, new String[] {knjig()});
    return dm.getLogotipovi().getString("SIFDJEL");
  }

  public String getRAZDOBLJE(){
    String mjod = ut.getMonth(fPDV.getDatumOd());
    String mjdo = ut.getMonth(fPDV.getDatumDo());
    String danod = new java.util.StringTokenizer(rdu.dataFormatter(fPDV.getDatumOd()),".").nextToken();
    String dando = new java.util.StringTokenizer(rdu.dataFormatter(fPDV.getDatumDo()),".").nextToken();
    String god = ut.getYear(fPDV.getDatumOd());
    return "\nOD  " + danod + "  " + mjod + "  DO  " + dando + "  " + mjdo + "  GOD.  " + god;
  }

  public BigDecimal getUKUPNO(){
     return ds.getBigDecimal("UKUPNO_I_II");
  }

  public BigDecimal getI(){
     return ds.getBigDecimal("UKUPNO_I");
  }

  public BigDecimal getI1(){
     return ds.getBigDecimal("BEZ_POREZA");
  }

  public BigDecimal getI2(){
     return ds.getBigDecimal("UKUPNO_I2");
  }

  public BigDecimal getI21(){
     return ds.getBigDecimal("TUZEMNE");
  }
  
  public BigDecimal getI22(){
    return ds.getBigDecimal("PRIJEVOZ");
 }

  public BigDecimal getI23(){
     return ds.getBigDecimal("IZVOZNE");
  }
  
  public BigDecimal getI24(){
    return ds.getBigDecimal("OSTALO_I24");
  }

  public BigDecimal getI3(){
     return ds.getBigDecimal("STOPA_NULA");
  }

  public BigDecimal getIIv(){
     return ds.getBigDecimal("UKUPNO_II_V");
  }

  public BigDecimal getIIp(){
     return ds.getBigDecimal("UKUPNO_II_P");
  }

  public BigDecimal getII1v(){
     return ds.getBigDecimal("IZDANI_RACUNI_V");
  }

  public BigDecimal getII1p(){
     return ds.getBigDecimal("IZDANI_RACUNI_P");
  }

  public BigDecimal getII2v(){
     return ds.getBigDecimal("NEZARACUNANE_V");
  }

  public BigDecimal getII2p(){
     return ds.getBigDecimal("NEZARACUNANE_P");
  }

  public BigDecimal getII3v(){
     return ds.getBigDecimal("VLASTITA_POT_V");
  }

  public BigDecimal getII3p(){
     return ds.getBigDecimal("VLASTITA_POT_P");
  }

  public BigDecimal getII4v(){
     return ds.getBigDecimal("NENAP_IZVOZ_V");
  }

  public BigDecimal getII4p(){
     return ds.getBigDecimal("NENAP_IZVOZ_P");
  }

  public BigDecimal getII5v(){
     return ds.getBigDecimal("NAK_OSL_IZV_V");
  }

  public BigDecimal getII5p(){
     return ds.getBigDecimal("NAK_OSL_IZV_P");
  }

  public BigDecimal getIIIv(){
     return ds.getBigDecimal("UKUPNO_III_V");
  }

  public BigDecimal getIIIp(){
     return ds.getBigDecimal("UKUPNO_III_P");
  }

  public BigDecimal getIII1v(){
     return ds.getBigDecimal("PPOR_PR_RAC_V");
  }

  public BigDecimal getIII1p(){
     return ds.getBigDecimal("PPOR_PR_RAC_P");
  }

  public BigDecimal getIII2v(){
     return ds.getBigDecimal("PL_PPOR_UVOZ_V");
  }

  public BigDecimal getIII2p(){
     return ds.getBigDecimal("PL_PPOR_UVOZ_P");
  }

  public BigDecimal getIII3v(){
     return ds.getBigDecimal("PL_PPOR_USLUGE_V");
  }

  public BigDecimal getIII3p(){
     return ds.getBigDecimal("PL_PPOR_USLUGE_P");
  }

  public BigDecimal getIII4(){
     return ds.getBigDecimal("ISPRAVCI_PPORA");
  }

  public BigDecimal getIV(){
     return ds.getBigDecimal("POR_OBV");
  }

  public BigDecimal getV(){
     return ds.getBigDecimal("PO_PRETHOD_OBR");
  }

  public BigDecimal getVI(){
     return ds.getBigDecimal("RAZLIKA");
  }


}