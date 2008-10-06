/****license*****************************************************************
**   file: repUnRealDoc.java
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

public class repUnRealDoc implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  repMemo rpm = repMemo.getrepMemo();
//  frmUnRealDoc upk = frmUnRealDoc.getfrmUnRealDoc();
  FrmNerealiziraniDok upk = FrmNerealiziraniDok.getInstance();
  DataSet ds = upk.getQds();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  
  public repUnRealDoc() {
  }

  public repUnRealDoc(int idx) {
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

        return new repUnRealDoc(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }
  public void close() {}

  public String getNAZPAR()
  {
    ru.setDataSet(ds);
    colname[0] = "CPAR";
    return ( ru.getSomething(colname,dm.getPartneri() ,"NAZPAR").toString());
//    return ds.getString("NAZPAR");
   //return "OVO JE proba ispisa nekog dugaèkog naziva dužine50";
  }

  public int getCPAR()
  {
    return ds.getInt("CPAR");
    //return 2000000;
  }

  public int getBRDOK()
  {
    return ds.getInt("BRDOK");
   // return 200000;
  }

  public String getDATDOK()
  {
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }

  public String getUIRAC()
  {
    return ds.getBigDecimal("UIRAC").toString();
  }

  public String getVRDOK()
  {
    return ds.getString("VRDOK");
  }

  public String getCSKL()
  {
    return ds.getString("CSKL");
  }

  public String getNAZSKL()
  {
    ru.setDataSet(ds);
    colname[0] = "CSKL";
    return ( ru.getSomething(colname,dm.getSklad() ,"NAZSKL").toString());
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


  public String getDatumIsp()
  {
    return rdu.dataFormatter(val.getToday());
  }
}