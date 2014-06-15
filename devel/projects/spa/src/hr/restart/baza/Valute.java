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

import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Valute extends KreirDrop implements DataModule {
  
	private static Valute inst = new Valute();
  
	QueryDataSet valuteaktiv = new raDataSet();
  QueryDataSet valutestrane = new raDataSet();

  {
    createFilteredDataSet(valuteaktiv, "aktiv = 'D'");
    createFilteredDataSet(valutestrane, "strval = 'D' AND aktiv = 'D'");
  }
  
  public static Valute getDataModule() {
    return inst;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return valuteaktiv;
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getStrane() {
    return valutestrane;
  }

  public boolean isAutoRefresh() {
    return true;
  }
}
