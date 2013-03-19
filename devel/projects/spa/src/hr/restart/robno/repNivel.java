/****license*****************************************************************
**   file: repNivel.java
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

public class repNivel implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  _Main main;
  frmNivelacija fni = frmNivelacija.getInstance();
  DataSet ds = fni.getRepQDS();

  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo re = repMemo.getrepMemo();

  public repNivel() {
  }

  private static boolean isMC;

  public repNivel(int idx) {
    if(idx==0){
      lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", ds.getString("CSKL"));
      if (dm.getSklad().getString("VRZAL").equals("M")) isMC = true;
      else isMC = false;
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

        return new repNivel(indx++);
      }
      public boolean hasMoreElements() {
        return (indx < ds.getRowCount());
      }
    };
  }

  public void close() {
  }

  public int getBRDOK(){
    return ds.getInt("BRDOK");
  }

  public short getRBR(){
    return ds.getShort("RBR");
  }

  public String getCSKL(){
     return ds.getString("CSKL");
  }

  public String getNAZSKL(){
    lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", ds.getString("CSKL"));
    return dm.getSklad().getString("NAZSKL");
  }

  public String getCART(){
    return Aut.getAut().getCARTdependable(String.valueOf(ds.getInt("CART")),ds.getString("CART1"),ds.getString("BC"));
  }

  public String getNAZART(){
     return ds.getString("NAZART");
  }

  public String getJM(){
     return ds.getString("JM");
  }

  public BigDecimal getKOL(){
     return ds.getBigDecimal("SKOL");
  }
  
  public BigDecimal getSKOL(){
    return ds.getBigDecimal("SKOL");
 }

  public BigDecimal getSTARACIJENA(){
    if (isMC) return ds.getBigDecimal("SMC");
    return ds.getBigDecimal("SVC");
  }

  public BigDecimal getNOVACIJENA(){
    if (isMC) return ds.getBigDecimal("MC");
    return ds.getBigDecimal("VC");
  }

  public double getPORAV(){
     return ds.getBigDecimal("PORAV").doubleValue();
  }

  public String getDATDOK(){
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
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

  public String getFormatBroj(){
    return ru.getFormatBroj();
  }
}