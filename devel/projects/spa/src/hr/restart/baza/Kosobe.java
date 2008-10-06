/****license*****************************************************************
**   file: Kosobe.java
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
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;


public class Kosobe extends KreirDrop implements DataModule {
  dM dm  = dM.getDataModule();
  private static Kosobe Kosobeclass;

  QueryDataSet kos = new raDataSet();

  public static Kosobe getDataModule() {
    if (Kosobeclass == null) {
      Kosobeclass = new Kosobe();
    }
    return Kosobeclass;
  }

  public QueryDataSet getQueryDataSet() {
    return kos;
  }

  public Kosobe() {
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
  }
}
