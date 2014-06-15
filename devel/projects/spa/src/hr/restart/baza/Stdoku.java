/****license*****************************************************************
**   file: Stdoku.java
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

public class Stdoku extends KreirDrop implements DataModule {
	
  private static Stdoku inst = new Stdoku();
  
  QueryDataSet stdokuPST = new QueryDataSet();
  QueryDataSet stdokuPRI = new QueryDataSet();
  QueryDataSet stdokuPOR = new QueryDataSet();
  QueryDataSet stdokuPTE = new QueryDataSet();
  QueryDataSet stdokuPRK = new QueryDataSet();
  QueryDataSet stdokuKAL = new QueryDataSet();
  QueryDataSet stdokuPRE = new QueryDataSet();
  QueryDataSet stdokuINV = new QueryDataSet();

  {
  	initClones();
  }
  
  public static Stdoku getDataModule() {
    return inst;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPST() {
    return stdokuPST;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPRI() {
    return stdokuPRI;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPOR() {
    return stdokuPOR;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPTE() {
    return stdokuPTE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPRK() {
    return stdokuPRK;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuKAL() {
    return stdokuKAL;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPRE() {
    return stdokuPRE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuINV() {
    return stdokuINV;
  }
  
  protected void modifyColumn(Column c) {
    if (c.getColumnName().equals("DC_VAL") || c.getColumnName().equals("IDOB_VAL")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "ulazValDec", 
          "2", "Broj decimala za valutne iznose na ulazu (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
    if (c.getColumnName().equals("DC") || c.getColumnName().equals("NC") || c.getColumnName().equals("ZC")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "skladDec", 
          "2", "Broj decimala za skladišne cijene (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
  }


  private void initClones() {
    createFilteredDataSet(stdokuPST, "1=0");
    createFilteredDataSet(stdokuPRI, "1=0");
    createFilteredDataSet(stdokuPOR, "1=0");
    createFilteredDataSet(stdokuPTE, "1=0");
    createFilteredDataSet(stdokuPRK, "1=0");
    createFilteredDataSet(stdokuKAL, "1=0");
    createFilteredDataSet(stdokuPRE, "1=0");
    createFilteredDataSet(stdokuINV, "1=0");
  }
}
