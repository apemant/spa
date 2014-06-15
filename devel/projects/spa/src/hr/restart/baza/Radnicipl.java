/****license*****************************************************************
**   file: Radnicipl.java
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

public class Radnicipl extends KreirDrop implements DataModule {

  private static Radnicipl inst = new Radnicipl();

  QueryDataSet rplaktiv = new raDataSet();

  {
    createFilteredDataSet(rplaktiv, "aktiv='D'");
  }

  public static Radnicipl getDataModule() {
    return inst;
  }


  public QueryDataSet getAktiv() {
    return rplaktiv;
  }

  public boolean isAutoRefresh() {
    return true;
  }
}
