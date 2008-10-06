/****license*****************************************************************
**   file: repSpecKredZB.java
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

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repSpecKredZB implements sg.com.elixir.reportwriter.datasource.IDataProvider{
  _Main main;
  frmSpecKred fSK = frmSpecKred.getFrmSpecKred();
  DataSet ds = fSK.getQdsZB();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  repUtil ru = repUtil.getrepUtil();
  repMemo rpm = repMemo.getrepMemo();
  String[] colname = new String[] {""};


  public repSpecKredZB() {
//     ru.setDataSet(ds);
  }

  public repSpecKredZB(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    {
      ds.open();
//      sysoutTEST ST = new sysoutTEST(false);
//      ST.prn(ds);
//      rowCount= ds.getRowCount();
    }
    int indx=0;
    public Object nextElement() {
      return new repSpecKredZB(indx++);
    }
    public boolean hasMoreElements() {
      return (indx < ds.getRowCount());
    }
    };
  }
//******************** M O J I  P O D A C I ******************************

  public String getObracunLab()
  {
//    String mj = frmSpecKred.getFrmSpecKred().fieldSet.getShort("MJESECOD")+"";
//    String god = frmSpecKred.getFrmSpecKred().fieldSet.getShort("GODINAOD")+"";
//    String rbr = frmSpecKred.getFrmSpecKred().fieldSet.getShort("RBROD")+"";
//    String mjdo = frmSpecKred.getFrmSpecKred().fieldSet.getShort("MJESECDO")+"";
//    String goddo = frmSpecKred.getFrmSpecKred().fieldSet.getShort("GODINADO")+"";
//    String rbrdo = frmSpecKred.getFrmSpecKred().fieldSet.getShort("RBRDO")+"";

    String mj = fSK.fieldSet.getShort("MJESECOD")+"";
    String god = fSK.fieldSet.getShort("GODINAOD")+"";
    String rbr = fSK.fieldSet.getShort("RBROD")+"";
    String mjdo = fSK.fieldSet.getShort("MJESECDO")+"";
    String goddo = fSK.fieldSet.getShort("GODINADO")+"";
    String rbrdo = fSK.fieldSet.getShort("RBRDO")+"";


    if(fSK.getRepMode()=='A')
      return ("Obra\u010Dun za "+mj+"-"+god+" do " + mjdo+"-"+goddo+", rbr "+rbr+"-"+rbrdo);
    return ("Obra\u010Dun za "+mj+"-"+god+"/"+rbr);

  }

  public String getcVrOdb()
  {
    return ds.getShort("CVRODB")+"";
  }

  public String fake()
  {
    return "";
  }

  public String getOpisCVRODB()
  {
    String opis = "";
    short cvrodb = ds.getShort("CVRODB");
    lookupData.getlookupData().raLocate(dm.getVrsteodb(), new String[] {"CVRODB"}, new String[] {""+cvrodb});
    return dm.getVrsteodb().getString("OPISVRODB");
  }

  public double getObrIznos()
  {
    return ds.getBigDecimal("OBRIZNOS").doubleValue();
  }

  public double getObrSaldo()
  {
    return ds.getBigDecimal("SALDO").doubleValue();
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

