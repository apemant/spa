/****license*****************************************************************
**   file: Stmeskla.java
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
package hr.restart.baza;

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Stmeskla extends KreirDrop implements DataModule {

  private static Stmeskla inst = new Stmeskla();
  
  QueryDataSet StmesklaMES = new QueryDataSet();
  QueryDataSet StmesklaMEU = new QueryDataSet();
  QueryDataSet StmesklaMEI = new QueryDataSet();

  {
    createFilteredDataSet(StmesklaMES, "1=0");
    createFilteredDataSet(StmesklaMEU, "1=0");
    createFilteredDataSet(StmesklaMEI, "1=0");
  }

  public static Stmeskla getDataModule() {
    return inst;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMES() {
    return StmesklaMES;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMEU() {
    return StmesklaMEU;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMEI() {
    return StmesklaMEI;
  }

  
  protected void modifyColumn(Column c) {
    if (c.getColumnName().equals("NC") || c.getColumnName().equals("ZC") || c.getColumnName().equals("ZCUL")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "skladDec", 
          "2", "Broj decimala za skladišne cijene (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
  }
}
