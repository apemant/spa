/****license*****************************************************************
**   file: repNalozi.java
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

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

/*public class repNalog {

  public repNalog() {
  }
}*/



import com.borland.dx.dataset.DataSet;

public class repNalozi implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  hr.restart.robno._Main main;



  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  hr.restart.robno.raDateUtil rdu = hr.restart.robno.raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  hr.restart.robno.repUtil ru = hr.restart.robno.repUtil.getrepUtil();
  hr.restart.robno.repMemo rpm = hr.restart.robno.repMemo.getrepMemo();
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  DataSet ds = frmNalozi.getRepMasterSet();

  public repNalozi() {
    ru.setDataSet(ds);
  }

  public repNalozi(int idx) {

    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
      }
      int indx=0;
      public Object nextElement() {

        return new repNalozi(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public double getIP()
  {
    return ds.getBigDecimal("IP").doubleValue();
  }

  public double getID()
  {
    return ds.getBigDecimal("ID").doubleValue();
  }

  public String getCNaloga()
  {
    return ds.getString("CNALOGA");
  }

  public double getSaldo()
  {
    return ds.getBigDecimal("SALDO").doubleValue();
  }

  public double getKonIzn()
  {
    return ds.getBigDecimal("KONTRIZNOS").doubleValue();
  }

  public String getStatus()
  {
    String status = "";
    if(ds.getString("STATUS").equals("S"))
      status = "Spreman";
    else if(ds.getString("STATUS").equals("N"))
      status = "Nespreman";
    else
      status = "Knjižen";
    return status;
  }
  public String getHeaderStatus()
  {
    String status = "";
    if(frmNalozi.getFrmNalozi().getPreSelect().getSelRow().getString("STATUS").equals("S"))
      status = "Spremni";
    else if(frmNalozi.getFrmNalozi().getPreSelect().getSelRow().getString("STATUS").equals("N"))
      status = "Nespremni";
    else if(frmNalozi.getFrmNalozi().getPreSelect().getSelRow().getString("STATUS").equals("K"))
      status = "Knjiženi";
    else
      status = "Svi";
    return status;
  }

  public String getDatKnj()
  {
    return rdu.dataFormatter(ds.getTimestamp("DATUMKNJ"));
  }

   public String getCaption()
  {
    return getPocDat() + " do " +getZavDat();
  }

  public String getPocDat()
  {
    String date = frmNalozi.getFrmNalozi().getPreSelect().getSelRow().getTimestamp("DATUMKNJ-from").toString();
    String year = date.substring(0,4);
    String month = date.substring(5,7);
    String day = date.substring(8,10);

    return day+"."+month+"."+year+".";
  }

  public String getZavDat()
  {
    String date = frmNalozi.getFrmNalozi().getPreSelect().getSelRow().getTimestamp("DATUMKNJ-to").toString();
    String year = date.substring(0,4);
    String month = date.substring(5,7);
    String day = date.substring(8,10);

    return day+"."+month+"."+year+".";
  }

  public String getVrNal()
  {
    return frmNalozi.getFrmNalozi().getPreSelect().getSelRow().getString("CVRNAL");
  }

  public String getNazVrNal()
  {
    ru.setDataSet(ds);
    colname[0] = "CVRNAL";
    String rez = ru.getSomething(colname,dm.getVrstenaloga(),"opisvrnal").toString().trim();
    return rez;
  }

  public String getFake()
  {
    return "a";
  }

  public String getFirstLine(){
    return rpm.getFirstLine();
  }

  public String getSecondLine(){
    return rpm.getSecondLine();
  }

  public String getThirdLine(){
    return rpm.getThirdLine();
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(val.getToday());
  }

}
