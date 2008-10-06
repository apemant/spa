/****license*****************************************************************
**   file: repUslIzlazni.java
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
package hr.restart.robno;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.SortDescriptor;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repUslIzlazni extends repIzlazni {
  private int brdok = -1;
  public repUslIzlazni() {
    super(false);
    ds = raAutomatRac.getraAutomatRac().getQuery();
    ds.open();
    ds.setSort(new SortDescriptor(new String[] {"BRDOK", "RBR"}));
    ru.setDataSet(ds);
    repQC.rVR.rakapitulacija(ds);
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    if (brdok != ds.getInt("BRDOK")) {
      rekapPorez();
      brdok = ds.getInt("BRDOK");
    }
    return this;
  }

  public String getNAZART() {
    return ds.getString("TEXTFAK");
  }
  public String SgetDATUG() {
    return rdu.dataFormatter(ds.getTimestamp("DATUG"));
  }

  public String getRBRS() {
    return ds.getShort("RBR")+".";
  }
}
