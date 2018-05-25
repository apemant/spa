/****license*****************************************************************
**   file: Cjenik.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;


public class Cjenik extends KreirDrop implements DataModule {

  private static Cjenik inst = new Cjenik();

  public static Cjenik getDataModule() {
    return inst;
  }

  public boolean isAutoRefresh() {
    return true;
  }
  
  protected void modifyColumn(Column c) {
    if (c.getColumnName().equals("VCKALDOM") || c.getColumnName().equals("VCKALVAL") ||
        c.getColumnName().equals("VC") || c.getColumnName().equals("MC")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "cijenaDec", 
          "2", "Broj decimala za cijenu na izlazu (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
  }
}
