/****license*****************************************************************
**   file: repGrupArt.java
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

import hr.restart.util.Valid;

import com.borland.dx.dataset.DataSet;

public class repGrupArt implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  DataSet ds = hr.restart.baza.dM.getDataModule().getGrupart(); //-> tablica za koju radimo report
  Valid val = Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  int rowNo;


  public repGrupArt(){
  }

  public repGrupArt(int idx){
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

        return new repGrupArt(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {}

  public String getNAZGRART(){
    return ds.getString("NAZGRART");
  }

  public String getCGRART(){
    return ds.getString("CGRART");
  }

  public String getCGRARTPRIP(){
    return ds.getString("CGRARTPRIP");
  }


  public String getEQUAL(){
    if (ds.getString("CGRART").equals(ds.getString("CGRARTPRIP"))){
      return "";
    } else {
    String prip = ds.getString("CGRARTPRIP");
//    System.out.println(prip);
    ds.first();
    do
    if (prip.equals(ds.getString("CGRART"))) {
      return ds.getString("NAZGRART");
    } else {
      ds.next();
    }
    while (ds.inBounds());
    return "NEBULOZA: Pripadnost nije unesena";
    }
  }

  public short getRBR(){
    return ds.getShort("RBR");
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