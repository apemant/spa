/****license*****************************************************************
**   file: repStavkaRab.java
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

import com.borland.dx.dataset.DataSet;

public class repStavkaRab implements sg.com.elixir.reportwriter.datasource.IDataProvider
{

  _Main main;
//  DataSet ds = hr.restart.baza.dM.getDataModule().getShrab();
  DataSet ds = frmShemaRab.getQuery();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();

  public repStavkaRab() {
  }

  public repStavkaRab(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
        ru.setDataSet(ds);
      }
      int indx=0;
      public Object nextElement() {
        return new repStavkaRab(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  //RABATI.NRAB, VSHRAB_RAB.CRAB, VSHRAB_RAB.PRAB,SHRAB.CSHRAB,SHRAB.NSHRAB, RABATI.RABNARAB
  public String getNRAB()
  {
    return ds.getString("NRAB");
  }

  public String getCRAB()
  {
    return ds.getString("CRAB");
  }

  public String getPRAB()
  {
    return ds.getBigDecimal("PRAB").toString();
  }

  public String getCSHRAB()
  {
    return ds.getString("CSHRAB");
  }

  public String getNSHRAB()
  {
    return ds.getString("NSHRAB");
  }

/*  public String getRABNARAB()
  {
    return ds.getString("RABNARAB");
  }*/

   public String getRABNARAB()
  {
    if (ds.getString("RABNARAB").equals(""))
      return "N";
    else
      return ds.getString("RABNARAB");
  }

   public double getIRAB()
  {

    return ds.getBigDecimal("IRAB").doubleValue();
  }

  public String getUPRAB()
  {
    return ds.getBigDecimal("UPRAB").toString();
  }

  public int gerRBR()
  {
    return ds.getInt("RBR");
  }

  public int getDummy()
  {
    return 1;
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