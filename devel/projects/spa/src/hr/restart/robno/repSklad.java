/****license*****************************************************************
**   file: repSklad.java
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

public class repSklad implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  DataSet ds = hr.restart.baza.dM.getDataModule().getSklad();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();

  public repSklad() {
  }

  public repSklad(int idx) {
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

        return new repSklad(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getNAZSKL() {
    return ds.getString("NAZSKL");
  }

  public String getVRSKL()
  {
    String temp = ds.getString("VRSKL");
    if (temp.equals("S"))
      return "Skladište";
    else if (temp.equals("T"))
      return "Trgovina";
    else if (temp.equals("R"))
      return "Tranzit";
    return "";
  }

  public String getVRZAL()
  {
    String temp = ds.getString("VRZAL");

    if (temp.equals("N"))
      return "Prosjeèna nabavna cijena";
    else if (temp.equals("V"))
      return "Zadnja prod. cijena bez poreza";
    else if (temp.equals("M"))
      return "Zadnja prod. cijena s porezom";
    else if (temp.equals("F"))
      return "Nabavna cijena - metoda FIFO";
    else if (temp.equals("L"))
      return "Nabavna cijena - metoda LIFO";
    else if (temp.equals("H"))
      return "Nabavna cijena - metoda HIFO";

    return "";
  }

  public String getORGJED()
  {
    ru.setDataSet(ds);
    colname[0] = "CORG";
    String prvi = ds.getString("CORG").concat("-");
    String drugi = ru.getSomething(colname,dm.getOrgstruktura(),"NAZIV").toString();
    return (prvi.concat(drugi));
  }

  public String getTIPSKL()
  {
    String temp = ds.getString("TIPSKL");

    if (temp.equals("R"))
      return "Trgovaèka roba";
    else if (temp.equals("T"))
      return "Proizvod";

    return "";
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

