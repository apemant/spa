/****license*****************************************************************
**   file: Partneri.java
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

public class Partneri extends KreirDrop implements DataModule {

  private static Partneri inst = new Partneri();
  
  QueryDataSet partneriaktiv = new raDataSet();
  QueryDataSet partnerikup = new raDataSet();
  QueryDataSet partneridob = new raDataSet();
  QueryDataSet partnerioboje = new raDataSet();

  {
    createFilteredDataSet(partneriaktiv, "aktiv = 'D'");
    createFilteredDataSet(partnerikup, "aktiv = 'D' AND (uloga = 'K' OR uloga = 'O')");
    createFilteredDataSet(partneridob, "aktiv = 'D' AND (uloga = 'D' OR uloga = 'O')");
    createFilteredDataSet(partnerioboje, "aktiv = 'D' AND uloga = 'O'");
  }


  public static Partneri getDataModule() {
    return inst;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return partneriaktiv;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKup() {
    return partnerikup;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDob() {
    return partneridob;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOboje() {
    return partnerioboje;
  }

  public boolean isAutoRefresh() {
    return true;
  }
}
