/****license*****************************************************************
**   file: repURADod.java
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
package hr.restart.sk;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repURADod implements sg.com.elixir.reportwriter.datasource.IDataProvider {
  raIspisUraIra rui = raIspisUraIra.getInstance();
  DataSet ds = rui.getDataSet();
  public repURADod() {
  }

  public repURADod(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
    int indx=0;
      public Object nextElement() {
        return new repURADod(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public int getRbr() {
    return ds.getInt("RBS");
  }

//  public String getKNJIGA() {
//    return ds.getString("CKNJIGE");
//  }

  public String getKNJIGA() {
   return rui.getCKNJIGE();
 }

 public String getNAZIVKNJIGE() {
   return rui.getNAZKNJIGE();
  }

  public int getFake() {
    return ds.getInt("BROJ");
  }

  public String getBROJDOK() {
    return ds.getInt("RBS") == 0 ? "" : ds.getString("BROJDOK");
  }

  public String getDATUM() {
    return ds.getInt("RBS") == 0 ? "" : rui.rdu.dataFormatter(ds.getTimestamp("DATPRI"));
  }

  public String getNAZIV() {
    return ds.getString("OPISPAR");
  }

  public String getMB() {
    return ds.getInt("RBS") == 0 ? "" : ds.getString("MB");
  }

  public BigDecimal getKOL6() {
    return ds.getBigDecimal("KOLONA6");
  }

  public BigDecimal getKOL7() {
    return ds.getBigDecimal("KOLONA7");
  }

  public BigDecimal getKOL8() {
    return ds.getBigDecimal("KOLONA8");
  }

  public BigDecimal getKOL9() {
    return ds.getBigDecimal("KOLONA9");
  }

  public BigDecimal getKOL10() {
    return ds.getBigDecimal("KOLONA10");
  }

  public BigDecimal getKOL11() {
    return new BigDecimal("999999889.99");
  }

  public BigDecimal getKOL12() {

      return new BigDecimal("100300000.00");
  }

  public void close() {
  }
}