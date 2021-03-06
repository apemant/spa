/****license*****************************************************************
**   file: RN_vrsub.java
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



public class RN_vrsub extends KreirDrop implements DataModule {

  private static RN_vrsub inst = new RN_vrsub();

  QueryDataSet rnvsubunos = new raDataSet();

  {
    createFilteredDataSet(rnvsubunos, "");
  }

  public static RN_vrsub getDataModule() {
    return inst;
  }


  public QueryDataSet getUnos() {
    return rnvsubunos;
  }

  public boolean isAutoRefresh() {
    return true;
  }
}
