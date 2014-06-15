/****license*****************************************************************
**   file: Vrdokum.java
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

public class Vrdokum extends KreirDrop implements DataModule {
  
	private static Vrdokum inst = new Vrdokum();
  
	QueryDataSet vrdokumall = new raDataSet();
  QueryDataSet uvrdokum = new raDataSet();
  QueryDataSet ivrdokum = new raDataSet();

  {
    createFilteredDataSet(vrdokumall, "");
    createFilteredDataSet(uvrdokum, "vrsdok = 'U'");
    createFilteredDataSet(ivrdokum, "vrsdok = 'I'");
  }

  public static Vrdokum getDataModule() {
    return inst;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getAll() {
    return vrdokumall;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getUlazni() {
    return uvrdokum;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getIzlazni() {
    return ivrdokum;
  }


  public boolean isAutoRefresh() {
    return true;
  }
}
