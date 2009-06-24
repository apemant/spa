/****license*****************************************************************
**   file: Stolovi.java
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

import com.borland.dx.sql.dataset.QueryDataSet;

public class Stolovi extends KreirDrop {

  private static Stolovi stoloviclass;
  
  QueryDataSet stolovi = new raDataSet();
  
  public static Stolovi getDataModule() {
    if (stoloviclass == null) {
      stoloviclass = new Stolovi();
    }
    return stoloviclass;
  }

  public QueryDataSet getQueryDataSet() {
    return stolovi;
  }

  public Stolovi() {
    try {
      modules.put(this.getClass().getName(), this);
      initModule();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}
