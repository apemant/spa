/****license*****************************************************************
**   file: repRekPrimCORG.java
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

public class repRekPrimCORG implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmRekPrim frkp = frmRekPrim.getFrmRekPrim();
  DataSet ds = frkp.getRepQDS02();
  DataSet ds2 = frkp.getRepQDS03();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repRekPrimCORG() {
    ru.setDataSet(ds);
  }

  public repRekPrimCORG(int idx) {
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

        return new repRekPrimCORG(indx++);
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

  public String getIMERADNIKA(){
    return frkp.getIme(ds.getString("CRADNIK"));
  }

  public String getCORG(){
    return ds.getString("CORG");
  }

  public String getNAZIVORG(){
    return frkp.getNazivOrg(ds.getString("CORG"));
  }

  public short getCVRP(){
    return ds.getShort("CVRP");
  }

  public String getCVRPNAZIV(){
    return frkp.getNazivVrsteRada(ds.getShort("CVRP"));
  }

  public BigDecimal getSATI(){
    return ds.getBigDecimal("SATI");
  }

  public BigDecimal getBRUTO(){
    return ds.getBigDecimal("BRUTO");
  }

  public BigDecimal getDOPRINOSI(){
    return ds.getBigDecimal("DOPRINOSI");
  }

  public BigDecimal getNETO(){
    return ds.getBigDecimal("NETO");
  }

  public double getUS(){
    return ds.getBigDecimal("SUMSATI").doubleValue();
  }

  public double getUB(){
    return ds.getBigDecimal("SUMBRUTO").doubleValue();
  }

  public double getUD(){
    return ds.getBigDecimal("SUMDOPRINOSI").doubleValue();
  }

  public double getUN(){
    return ds.getBigDecimal("SUMNETO").doubleValue();
  }

  public double getUUS(){
    return ds2.getBigDecimal("SUMSUMSATI").doubleValue();
  }

  public double getUUB(){
    return ds2.getBigDecimal("SUMSUMBRUTO").doubleValue();
  }

  public double getUUD(){
    return ds2.getBigDecimal("SUMSUMDOPR").doubleValue();
  }

  public double getUUN(){
    return ds2.getBigDecimal("SUMSUMNETO").doubleValue();
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
    if (frkp.getFlag().equals("O")) return rdu.dataFormatter(frkp.getDatumIspl());
    return rdu.dataFormatter(vl.getToday());
  }
  public String getLabelIsp(){
    if (frkp.getFlag().equals("O")) return "Datum isplate:";
    return "Datum ispisa:";
  }
  public String getPODNASLOV(){
    return frkp.getNaNivou();
  }
  public String getNADNASLOV(){
    return frkp.getObracun();
  }
}