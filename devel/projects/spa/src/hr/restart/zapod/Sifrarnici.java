/****license*****************************************************************
**   file: Sifrarnici.java
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
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.util.Aus;

import java.util.Hashtable;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Sifrarnici {

  private static Hashtable sifreSets = new java.util.Hashtable();

  protected Sifrarnici() {
  }

  public static StorageDataSet getSifre(String vrstasif) {
    if (!sifreSets.containsKey(vrstasif)) {
      QueryDataSet qds = new QueryDataSet();
      Aus.setFilter(qds, "select * from sifrarnici where vrstasif ='" + vrstasif + "'");      
      qds.setColumns(dM.getDataModule().getSifrarnici().cloneColumns());
      qds.getColumn("VRSTASIF").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
      qds.open();
      sifreSets.put(vrstasif,qds);
    }
    return (StorageDataSet)sifreSets.get(vrstasif);
  }
}