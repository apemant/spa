/****license*****************************************************************
**   file: frmJedMj.java
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
import hr.restart.util.raSifraNaziv;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class frmJedMj extends raSifraNaziv {
  hr.restart.baza.dM dm;
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();

  public frmJedMj() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    this.setRaDataSet(dm.getJedmj());
    this.setRaColumnSifra("JM");
    this.setRaColumnNaziv("NAZJM");
    this.setRaText("Jedinica mjere");
  }
  public boolean DeleteCheck() {
    if (util.isDeleteable("ARTIKLI", "JM", dm.getJedmj().getString("JM"), util.MOD_STR)==false)
      return false;
    if (util.chkIsDeleteable("ARTIKLI", "JMPAK", dm.getJedmj().getString("JM"), util.MOD_STR)==false)
      return false;
    if (util.chkIsDeleteable("DOB_ART", "JMPAK", dm.getJedmj().getString("JM"), util.MOD_STR)==false)
      return false;
    if (util.isDeleteable("KUP_ART", "JMPAK", dm.getJedmj().getString("JM"), util.MOD_STR)==false)
      return false;
    return true;
  }
}