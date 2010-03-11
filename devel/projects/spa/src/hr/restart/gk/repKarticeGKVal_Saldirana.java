/****license*****************************************************************
**   file: repKarticeGKVal_Saldirana.java
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
package hr.restart.gk;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;

import com.borland.dx.dataset.DataSet;

public class repKarticeGKVal_Saldirana implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;
  frmKarticeGKVal fkgk = frmKarticeGKVal.getInstance();
  DataSet ds = fkgk.getRepQDS();
  lookupData ld = lookupData.getlookupData();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  public static double iznosGrupe;
  public static int rb;

  public repKarticeGKVal_Saldirana() {
    ru.setDataSet(ds);
  }

  public repKarticeGKVal_Saldirana(int idx) {
    if(idx==0){
      rb = 0;
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

        return new repKarticeGKVal_Saldirana(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getNASLOV(){
    if(fkgk.isPrivremeno()) return "KONTO KARTICA U VALUTI - SALDIRANA - PRIVREMENA";
    return "KONTO KARTICA U VALUTI - SALDIRANA";
  }

  /*public String getDATDOK(){
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }
  public String getDATUMKNJ(){
    return rdu.dataFormatter(ds.getTimestamp("DATUMKNJ"));
  }*/
  public String getDatum()
  {
    if(fkgk.datdok)
      return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
    else
      return rdu.dataFormatter(ds.getTimestamp("DATUMKNJ"));
  }

  public String getDatumLab()
  {
    if(fkgk.datdok)
      return "Datum\ndokumenta";
    else
      return "Datum\nknjiženja";
  }
  public int getRBR(){
    return ds.getInt("RBR");
  }
  public double getID() {
    return ds.getBigDecimal("ID").doubleValue();
  }
  public double getIP() {
    return ds.getBigDecimal("IP").doubleValue();
  }
  public double getSALDO() {
    return ds.getBigDecimal("SALDO").doubleValue();
  }
  public String getCNALOGA() {
    return ds.getString("CNALOGA");
  }
  public String getCORG() {
    return ds.getString("CORG");
  }
  public String getOPIS() {
    return ds.getString("OPIS");
  }

  public String getOznVal()
  {
    System.out.println("VALUTA: " + fkgk.valutaOzn);
    return fkgk.valutaOzn;
  }

  public String getNazVal()
  {
    String cVal = fkgk.valutaOzn;
    ld.raLocate(dm.getValute(), new String[]{"OZNVAL"}, new String[]{cVal});
    return dm.getValute().getString("NAZVAL");
  }
  public String getCKON() {
    return fkgk.getCKON();
  }
  public String getNAZKON() {
    if (lookupData.getlookupData().raLocate(dm.getKonta(),"BROJKONTA",ds.getString("BROJKONTA"))) return dm.getKonta().getString("NAZIVKONTA");
    else return "";
  }
  public double getSREDTEC() {
    return ds.getBigDecimal("TECSRED").doubleValue();
  }
  public String getZaPeriod() {
    return rdu.dataFormatter(fkgk.getPocDatum()) + " do " + rdu.dataFormatter(fkgk.getZavDatum());
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