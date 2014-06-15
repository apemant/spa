/****license*****************************************************************
**   file: KnjigeUI.java
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



public class KnjigeUI extends KreirDrop implements DataModule {

  private static KnjigeUI inst = new KnjigeUI();

  QueryDataSet knjigeu = new raDataSet();
  QueryDataSet knjigei = new raDataSet();

  {
    createFilteredDataSet(knjigeu, "uraira = 'U'");
    createFilteredDataSet(knjigei, "uraira = 'I'");
  }

  public static KnjigeUI getDataModule() {
    return inst;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getKnjigeU() {
    return knjigeu;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKnjigeI() {
    return knjigei;
  }

  public boolean isAutoRefresh() {
    return true;
  }
}
