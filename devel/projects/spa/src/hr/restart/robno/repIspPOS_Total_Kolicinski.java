/****license*****************************************************************
**   file: repIspPOS_Total_Kolicinski.java
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

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repIspPOS_Total_Kolicinski implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  DataSet ds = ispPOS_Total.getispPOS_Total().getArtikliReportQDS();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();
  public static double iznosGrupe;
  public static double sumIzGrupe;

  public repIspPOS_Total_Kolicinski() {
  }

  public repIspPOS_Total_Kolicinski(int idx) {
    if(idx==0){
      sumIzGrupe = 0;
    }
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

        return new repIspPOS_Total_Kolicinski(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getPODNASLOV() {
    if (ispPOS_Total.getispPOS_Total().getRedOdDoDatuma()) {
      return "Razdoblje od "+ispPOS_Total.getispPOS_Total().tds.getTimestamp("pocDatum").toString().substring(0,10)+" do "+ispPOS_Total.getispPOS_Total().tds.getTimestamp("zavDatum").toString().substring(0,10);
    }
    return "Ra\u010Duni od "+String.valueOf(ispPOS_Total.getispPOS_Total().tds.getInt("pocBroj"))+". do "+String.valueOf(ispPOS_Total.getispPOS_Total().tds.getInt("zavBroj")+".");
  }

  public String getVRDOK() {
    return ds.getString("VRDOK");
  }

  public String getCART() {
    return Aut.getAut().getCARTdependable(""+ds.getInt("CART"), ds.getString("CART1"), ds.getString("BC"));
  }

  public String getNAZART() {
    return ds.getString("NAZART");
  }

  public String getJM() {
    return ds.getString("JM");
  }

  public BigDecimal getKOL() {
    return ds.getBigDecimal("KOL");
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

  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }
}