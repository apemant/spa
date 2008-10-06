/****license*****************************************************************
**   file: presSalKonUR.java
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
package hr.restart.ur;

import hr.restart.baza.Condition;
import hr.restart.baza.KnjigeUI;
import hr.restart.sk.presSalKon;
import hr.restart.util.Valid;

import com.borland.dx.dataset.DataSet;

public class presSalKonUR extends presSalKon {
  DataSet knjigeUV;
  DataSet knjigeIV;
  public presSalKonUR(DataSet ds, String col, String uval, String ival) {
    super(ds, col, uval, ival);
  }

  public presSalKonUR(DataSet ds) {
    super(ds);
  }
  public boolean Validacija() {
    if (!super.Validacija()) return false;
    return !Valid.getValid().isEmpty(jlrCknjige);
  }
  
  protected DataSet getKnjigeU() {
    if (knjigeUV == null) {
      knjigeUV = KnjigeUI.getDataModule().getFilteredDataSet(
          Condition.whereAllEqual(new String[] {"uraira", "virtua"}, new String[] {"U","D"}));
    }
    return knjigeUV;
  }
  protected DataSet getKnjigeI() {
    if (knjigeIV == null) {
      knjigeIV = KnjigeUI.getDataModule().getFilteredDataSet(
          Condition.whereAllEqual(new String[] {"uraira", "virtua"}, new String[] {"I","D"}));
    }
    return knjigeIV;
  }

}
