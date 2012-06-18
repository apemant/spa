/****license*****************************************************************
**   file: Doku.java
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
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;


public class Doku extends KreirDrop implements DataModule {

  private static Doku dokuclass;
  dM dm  = dM.getDataModule();
  QueryDataSet doku = new raDataSet();
  QueryDataSet dokuPST = new raDataSet();
  QueryDataSet dokuPRI = new raDataSet();
  QueryDataSet dokuPOR = new raDataSet();
  QueryDataSet dokuPTE = new raDataSet();
  QueryDataSet dokuPRK = new raDataSet();
  QueryDataSet dokuKAL = new raDataSet();
  QueryDataSet dokuPRE = new raDataSet();
  QueryDataSet dokuINV = new raDataSet();

  
  public static Doku getDataModule() {
    if (dokuclass == null) {
      dokuclass = new Doku();
    }
    return dokuclass;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return doku;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPST() {
    return dokuPST;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPRI() {
    return dokuPRI;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPOR() {
    return dokuPOR;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPTE() {
    return dokuPTE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPRK() {
    return dokuPRK;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuKAL() {
    return dokuKAL;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPRE() {
    return dokuPRE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuINV() {
    return dokuINV;
  }

  public Doku(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    initModule();
    
    initClones();
  }

  private void initClones() {
    createFilteredDataSet(dokuPST, "1=0");
    createFilteredDataSet(dokuPRI, "1=0");
    createFilteredDataSet(dokuPOR, "1=0");
    createFilteredDataSet(dokuPTE, "1=0");
    createFilteredDataSet(dokuPRK, "1=0");
    createFilteredDataSet(dokuKAL, "1=0");
    createFilteredDataSet(dokuPRE, "1=0");
    createFilteredDataSet(dokuINV, "1=0");
  }
}