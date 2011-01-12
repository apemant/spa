/****license*****************************************************************
**   file: Valute.java
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
import java.util.ResourceBundle;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Valute extends KreirDrop implements DataModule {
  private static Valute Valuteclass;
  dM dm  = dM.getDataModule();
  QueryDataSet valute = new raDataSet();
  QueryDataSet valuteaktiv = new raDataSet();
  QueryDataSet valutestrane = new raDataSet();

  public static Valute getDataModule() {
    if (Valuteclass == null) {
      Valuteclass = new Valute();
    }
    return Valuteclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return valute;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return valuteaktiv;
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getStrane() {
    return valutestrane;
  }

  public Valute() {
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
    
    createFilteredDataSet(valuteaktiv, "aktiv = 'D'");
    createFilteredDataSet(valutestrane, "strval = 'D' AND aktiv = 'D'");
  }
}