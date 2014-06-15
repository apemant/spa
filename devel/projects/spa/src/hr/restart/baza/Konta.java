/****license*****************************************************************
**   file: Konta.java
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

public class Konta extends KreirDrop implements DataModule {
  
	private static Konta inst = new Konta();

  QueryDataSet kontaaktiv = new raDataSet();
  QueryDataSet kontaSin = new raDataSet();
  QueryDataSet kontaAna = new raDataSet();
  QueryDataSet kontaAnaP = new raDataSet();
  QueryDataSet kontaAnaD = new raDataSet();

  {
    createFilteredDataSet(kontaaktiv, "aktiv = 'D'");
    createFilteredDataSet(kontaSin, "aktiv = 'D' AND vrstakonta != 'A'");
    createFilteredDataSet(kontaAna, "aktiv = 'D' AND vrstakonta = 'A'");
    createFilteredDataSet(kontaAnaP, "aktiv = 'D' AND vrstakonta = 'A' AND (karakteristika = 'O' OR karakteristika = 'P')");
    createFilteredDataSet(kontaAnaD, "aktiv = 'D' AND vrstakonta = 'A' AND (karakteristika = 'O' OR karakteristika = 'D')");
  }
  
  public static Konta getDataModule() {
    return inst;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return kontaaktiv;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaSin() {
    return kontaSin;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaAna() {
    return kontaAna;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaAnaP() {
    return kontaAnaP;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaAnaD() {
    return kontaAnaD;
  }

  public boolean isAutoRefresh() {
    return true;
  }
}
