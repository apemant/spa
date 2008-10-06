/****license*****************************************************************
**   file: repER1.java
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

import com.borland.dx.dataset.DataSet;

public class repER1 implements sg.com.elixir.reportwriter.datasource.IDataProvider {

//  hr.restart.robno._Main main;
  frmER1 frer1 = frmER1.getInstance();
  DataSet ds = frer1.getRepSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repER1() {
    ru.setDataSet(ds);
  }


  public repER1(int idx) {
    if (idx == 0){
    }
//    System.out.println("row " + idx);
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {ds.open();}
      int indx=0;
      public Object nextElement() {
        return new repER1(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getIME(){
     return "\n".concat(ds.getString("IME"));
  }
  public String getPREZIME(){
     return "\n".concat(ds.getString("PREZIME"));
  }
  public String getJMBG(){
     return ds.getString("JMBG");
  }
  public String getBROBVEZE(){
     return ds.getString("BROBVEZE");
  }
  public String getBROSIGZO(){
     return ds.getString("BROSIGZO");
  }
  public String getNAZIV(){
    return "\n".concat(frer1.getKnjNaziv());
  }
  public String getDATUM_OD(){
    return rdu.dataFormatter(frer1.getDatOd());
  }
  public String getDATUM_DO(){
    return rdu.dataFormatter(frer1.getDatDo());
  }
  public String getZAMJESECE(){
    return frer1.getZaMjesece();
  }
  public String getPODRUREDZO(){
    return frer1.getPodrUred();
  }
  public BigDecimal getSATIMJESECNO(){
    return ds.getBigDecimal("SATI"); //frer1.getSatiMj();
  }
  public double getSUMSATIMJESECNO(){
    return frer1.getSumSatiMj();
  }
  public BigDecimal getPLACA(){
    return ds.getBigDecimal("BRUTO").add(ds.getBigDecimal("NAKNADE"));
  }
  public double getSUMPLACA(){
    return frer1.getSumPlaca();
  }
  public BigDecimal getNETOPK(){
     return ds.getBigDecimal("NETOPK");
  }
  public double getSUMNETOPK(){
    return frer1.getSumNetopk();
  }
  public double getNKPOSATUBRUTTO(){
    return frer1.getSumPlaca() / frer1.getSumSatiMj();
//    return (sumPlaca.doubleValue() / sumSatiMjesecno.doubleValue());
  }
  public double getNKPOSATUNETTO(){
    return frer1.getSumNetopk() / frer1.getSumSatiMj();
//    return (sumNetopk.doubleValue() / sumSatiMjesecno.doubleValue());
  }
  public String getCLANOMF(){
    if (ds.getString("CLANOMF").equals("N")) return "NE";
    return "DA";
  }
  public String getMJGODOBR(){
    short mmonth = ds.getShort("MJOBRDOH");
    String masked;
    if (mmonth  < 10) masked = "0".concat(String.valueOf(ds.getShort("MJOBRDOH")));
    else masked = String.valueOf(ds.getShort("MJOBRDOH"));
    String str = masked.concat("/".concat(String.valueOf(ds.getShort("GODOBRDOH"))));
    return str;
  }
  public int getSorter(){
    short mmonth = ds.getShort("MJOBRDOH");
    String masked;
    if (mmonth  < 10) masked = "0".concat(String.valueOf(ds.getShort("MJOBRDOH")));
    else masked = String.valueOf(ds.getShort("MJOBRDOH"));
    String str = String.valueOf(ds.getShort("GODOBRDOH")) + masked;
    return Integer.parseInt(str);
  }
  public String getMJESTO(){
    return frer1.getKnjMjesto();
  }
  public String getDANASNJIDATUM(){
    return rdu.dataFormatter(vl.getToday());
  }
  public String getUMJESTUDANA(){
    return getMJESTO().concat(", ").concat(getDANASNJIDATUM()).concat(" godine");
  }
  public BigDecimal getSATIPUNORV(){
    return ds.getBigDecimal("SATIPUNORV");
  }
  public double getSUMSATIPUNORV(){
    return frer1.getSumSatiPunoRV();
  }
  public BigDecimal getSATIDUZE(){
    return ds.getBigDecimal("SATIDUZE");
  }
  public double getSUMSATIDUZE(){
    return frer1.getSumSatiDuze();
  }
  public BigDecimal getSATIKRACE(){
     return ds.getBigDecimal("SATIKRACE");
  }
  public double getSUMSATIKRACE(){
    return frer1.getSumSatiKrace();
  }
  public BigDecimal getSATIBOL(){
     return ds.getBigDecimal("SATIBOL");
  }
  public double getSUMSATIBOL(){
    return frer1.getSumSatiBol();
  }
  public BigDecimal getFONDSATI(){
     return ds.getBigDecimal("FONDSATI");
  }
  public double getSUMFONDSATI(){
    return frer1.getSumFondSati();
  }
  public BigDecimal getALIKVOTNI(){
     return ds.getBigDecimal("ALIKVOTNI");
  }
  public double getSUMALIKVOTNI(){
    return frer1.getSumAlikvotni();
  }
}