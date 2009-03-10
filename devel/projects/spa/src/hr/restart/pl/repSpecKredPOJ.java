/****license*****************************************************************
**   file: repSpecKredPOJ.java
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

import hr.restart.robno._Main;
import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.robno.repUtil;
import hr.restart.util.lookupData;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repSpecKredPOJ implements sg.com.elixir.reportwriter.datasource.IDataProvider{
  _Main main;
  DataSet ds = frmSpecKred.getFrmSpecKred().getQdsPOJ();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  String[] colname = new String[] {""};
  int  rowCount= 0;


  public repSpecKredPOJ() {
  }

  public repSpecKredPOJ(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
      ds.open();

      rowCount= ds.getRowCount();
    }
    int indx=0;
    public Object nextElement() {
      return new repSpecKredPOJ(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }
//******************** M O J I  P O D A C I ******************************

  public String getObracunLab()
  {
    String mj = frmSpecKred.getFrmSpecKred().fieldSet.getShort("MJESECOD")+"";
    String god = frmSpecKred.getFrmSpecKred().fieldSet.getShort("GODINAOD")+"";
    String rbr = frmSpecKred.getFrmSpecKred().fieldSet.getShort("RBROD")+"";
    String mjdo = frmSpecKred.getFrmSpecKred().fieldSet.getShort("MJESECDO")+"";
    String goddo = frmSpecKred.getFrmSpecKred().fieldSet.getShort("GODINADO")+"";
    String rbrdo = frmSpecKred.getFrmSpecKred().fieldSet.getShort("RBRDO")+"";

    if(frmSpecKred.getFrmSpecKred().getRepMode()=='A')
      return ("Obra\u010Dun za "+mj+"-"+god+" do " + mjdo+"-"+goddo+", rbr "+rbr+"-"+rbrdo);
    return ("Obra\u010Dun za "+mj+"-"+god+"/"+rbr);

  }

  public int getRowNum()
  {
    return rowCount;
  }

  public String getcVrOdb()
  {
    return ds.getShort("CVRODB")+"";
  }

  public String getOpisCVRODB()
  {
    String opis = "";
    short cvrodb = ds.getShort("CVRODB");
    lookupData.getlookupData().raLocate(dm.getVrsteodb(), new String[] {"CVRODB"}, new String[] {""+cvrodb});
    return dm.getVrsteodb().getString("OPISVRODB");
  }

  public String getSumLabela()
  {
    return "S V E U K U P N O";
  }

  public double getSuma()
  {
    return frmSpecKred.getFrmSpecKred().getSuma().doubleValue();
  }

  public double getSumaS()
  {
    return frmSpecKred.getFrmSpecKred().getSumaS().doubleValue();
  }
  
  public String getPartija() {
    short cvrodb = ds.getShort("CVRODB");
    String cradnik = ds.getString("CRADNIK");
    short rbrodb = ds.getShort("RBRODB");
    QueryDataSet partijaSet = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT PNB2 from odbici WHERE odbici.cvrodb = "+cvrodb
        +" AND odbici.CKEY = '"+cradnik+"' AND odbici.rbrodb = "+rbrodb);
    if (partijaSet.getRowCount() > 0) {
      return partijaSet.getString("PNB2");
    }
    return "";
  }


  public double getObrIznos()
  {
    return ds.getBigDecimal("OBRIZNOS").doubleValue();
  }

  public double getObrSaldo()
  {
    return ds.getBigDecimal("SALDO").doubleValue();
  }

  public String getCRadnik()
  {
    return ds.getString("CRADNIK");
  }

  public String getRadImePrez()
  {
//    String ime = "";
//    String prezime = "";
//    String cradnik = ds.getString("CRADNIK");
//    lookupData.getlookupData().raLocate(dm.getAllRadnici(), new String[] {"CRADNIK"}, new String[] {""+cradnik});
//    return dm.getAllRadnici().getString("IME")+" "+dm.getAllRadnici().getString("PREZIME");
    return ds.getString("PREZIME") + " " + ds.getString("IME");
  }

//*****************************************************************
  public void close() {
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
    return rdu.dataFormatter(frmRekObr.getFrmRekObr().getDatumIspl());
  }
}

