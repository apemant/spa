/****license*****************************************************************
**   file: repRekDopPorPORPRI01.java
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

public class repRekDopPorPORPRI01 implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmRekDopPor frdp = frmRekDopPor.getFrmRekDopPor();
  DataSet ds = frdp.getRepQDSPorPri();
//  DataSet priset = frdp.getRepQdsPri();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repRekDopPorPORPRI01() {
  }

  public repRekDopPorPORPRI01(int idx) {
    if (idx == 0){
//      sysoutTEST ST = new sysoutTEST(false);
//      ST.prn(ds);
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

        return new repRekDopPorPORPRI01(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getCOPCINE(){
    return ds.getString("COPCINE");
  }

  public String getOPISVRODB(){
    String Opcina ="";
    ru.setDataSet(ds);
    Opcina = ru.getSomething(new String[] {"COPCINE"},dm.getOpcine(),"NAZIVOP").getString();
    return Opcina;
  }

  public BigDecimal getPOR1(){
     return ds.getBigDecimal("POR1");
  }

  public BigDecimal getPOR2(){
     return ds.getBigDecimal("POR2");
  }

  public BigDecimal getPOR3(){
     return ds.getBigDecimal("POR3");
  }

  public BigDecimal getPOR4(){
     return ds.getBigDecimal("POR4");
  }

  public BigDecimal getPORUK(){
     return ds.getBigDecimal("PORUK");
  }

  public BigDecimal getOBRSTOPA(){
     return ds.getBigDecimal("OBRSTOPA");
  }

  public BigDecimal getOBROSN(){
     return ds.getBigDecimal("OBROSN");
  }

  public BigDecimal getOBRIZNOS(){
     return ds.getBigDecimal("OBRIZNOS");
  }

  public BigDecimal getUKUPNOPOREZPRIREZ(){
    return ds.getBigDecimal("UKUPNO");
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
    if (frdp.getFlag().equals("O")) return rdu.dataFormatter(frdp.getDatumIspl());
    return rdu.dataFormatter(vl.getToday());
  }
  public String getLabelIsp(){
    if (frdp.getFlag().equals("O")) return "Datum isplate:";
    return "Datum ispisa:";
  }
  public String getPODNASLOV(){
    return frdp.getNaNivou();
  }
  public String getNADNASLOV(){
    return frdp.getObracun();
  }
//  public String getDatumIsplate(){
//    return rdu.dataFormatter(frmRekObr.getFrmRekObr().getDatumIspl());
//  }
}