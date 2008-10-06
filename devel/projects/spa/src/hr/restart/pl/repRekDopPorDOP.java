/****license*****************************************************************
**   file: repRekDopPorDOP.java
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
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repRekDopPorDOP implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmRekDopPor frdp = frmRekDopPor.getFrmRekDopPor();
  DataSet ds = frdp.getRepQDS();
  DataSet ss = frdp.getSumSet();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repRekDopPorDOP() {
    ru.setDataSet(ds);
  }

  public repRekDopPorDOP(int idx) {
    if (idx == 0){}
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
        ds.open();
      }
      int indx=0;
      public Object nextElement() {

        return new repRekDopPorDOP(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getCVRO(){
    return ds.getString("CVRO");
  }
  public short getCVRODB(){
    return ds.getShort("CVRODB");
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

  public String getOPISVRODB(){
    String NazivOdb ="";
    ru.setDataSet(ds);
    NazivOdb = ru.getSomething(new String[] {"CVRODB"},dm.getVrsteodb(),"OPISVRODB").getString();
    return NazivOdb;
  }
  public String getOPISVRRADODN(){
    String NazivRadOdn ="";
    ru.setDataSet(ds);
    NazivRadOdn = ru.getSomething(new String[] {"CVRO"},dm.getVrodn(),"NAZIVRO").getString();
    return NazivRadOdn;
  }
  public int getBROJRADNIKA(){
    return frdp.getBrojDjelatnika("LD");
  }

  public BigDecimal getSUM(){
      if(lookupData.getlookupData().raLocate(ss, new String[] {"CVRO"}, new String[] {ds.getString("CVRO")})){
        return ss.getBigDecimal("SUMIZNOS");
      }
      return new BigDecimal(0);
  }

  public double getSMASUMARUM(){
    BigDecimal suma = new BigDecimal(0);
    for (ss.first(); ss.inBounds(); ss.next()){
      suma = suma.add(ss.getBigDecimal("SUMIZNOS"));
    }
    return suma.doubleValue();
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
  public String getPODNASLOV(){
    return frdp.getNaNivou();
  }
  public String getNADNASLOV(){
    return frdp.getObracun();
  }
  public String getDatumIsp(){
    if (frdp.getFlag().equals("O")) return rdu.dataFormatter(frdp.getDatumIspl());
    return rdu.dataFormatter(vl.getToday());
  }
  public String getLabelIsp(){
    if (frdp.getFlag().equals("O")) return "Datum isplate:";
    return "Datum ispisa:";
  }
}