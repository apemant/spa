/****license*****************************************************************
**   file: repPregledVisak.java
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
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright Rest-Art(c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author unascribed
 * @version 1.0
 */

public class repPregledVisak implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  DataSet ds = frmPregledVisak.getInstance().getrepQDS();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();

  public repPregledVisak() {
  }

  public repPregledVisak(int idx) {
    ds.goToRow(idx);
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      {
        ds.open();
        ru.setDataSet(ds);
//        sysoutTEST syst = new sysoutTEST(false);
//        syst.prn(ds);
      }
      int indx=0;
      public Object nextElement() {

        return new repPregledVisak(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
  }

  public String getNAZART() {
    return ds.getString("NAZART");
  }

  public String getJM() {
    return ds.getString("JM");
  }

  public String getCSKL() {
    return ds.getString("CSKL");
  }

  public String getNAZSKL() {
    lookupData.getlookupData().raLocate(dm.getSklad(), new String[] {"CSKL"}, new String[] {ds.getString("CSKL")});
    return dm.getSklad().getString("NAZSKL");
  }

  public BigDecimal getKOL() {
    return ds.getBigDecimal("KOL");
  }

  public int getBRDOK(){
    return ds.getInt("BRDOK");
  }

  public int getRBR(){
    return ds.getShort("RBR");
  }

  public String getFirstLine(){
    return rm.getFirstLine();
  }

  public String getSecondLine(){
    return rm.getSecondLine();
  }

  public String getThirdLine(){
    return rm.getThirdLine();
  }

  public String getLogoMjesto(){
    return rm.getLogoMjesto();
  }

  public String getFormatBroj(){
    return ru.getFormatBroj();
  }

  public String getDATDOK() {
    return String.valueOf(rdu.dataFormatter(ds.getTimestamp("DATDOK")));
  }
  public String getDatumIsp(){
    return rdu.dataFormatter(vl.getToday());
  }

}