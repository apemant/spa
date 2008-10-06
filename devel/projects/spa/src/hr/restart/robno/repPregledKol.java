/****license*****************************************************************
**   file: repPregledKol.java
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

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public class repPregledKol implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;

  repMemo rpm = repMemo.getrepMemo();

  upPregledKol upk = upPregledKol.getInstance();

  DataSet ds = upk.getQds();

  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

  raDateUtil rdu = raDateUtil.getraDateUtil();

  String[] colname = new String[]{""};

  repUtil ru = repUtil.getrepUtil();

  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();

  public repPregledKol() {}

  public repPregledKol(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
        ru.setDataSet(ds);
      }

      int indx = 0;

      public Object nextElement() {

        return new repPregledKol(indx++);
      }

      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {}

  public String getCSKL() {
    return upk.getCskl();
  }

  public String getNAZSKL() {
    return "";
  //    return ds.getString("NSK");
  }

  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
  }

  public String getNAZART() {
    return ds.getString("NAZART");
  }

  public String getStanjeCaption() {
    if (upk.getStanje() == 0)
      return "Trenutna";
    return "Trenutna - rezervirana";
  }

  public String getArtikliCaption() {
    if (upk.getArtikli() == 0)
      return "Signalna";
    return "Minimalna";
  }

  public BigDecimal getSTKOL() {
      return ds.getBigDecimal("A");
  }

  public String getCAPTION() {
    if (upk.getArtikli()==0)
      return "Popis artikala s kolièinom manjom od minimalne";
    return "Popis artikala s kolièinom manjom od signalne";
  }

  public BigDecimal getARKOL() {
      return ds.getBigDecimal("B");
  }

  public String getFirstLine() {
    return rpm.getFirstLine();
  }

  public String getSecondLine() {
    return rpm.getSecondLine();
  }

  public String getThirdLine() {
    return rpm.getThirdLine();
  }

  public String getDatumIsp() {
    return rdu.dataFormatter(val.getToday());
  }
}