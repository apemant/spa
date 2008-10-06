/****license*****************************************************************
**   file: frmVrskBLPN.java
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
package hr.restart.blpn;

import hr.restart.sisfun.frmVrsk;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;

public class frmVrskBLPN extends frmVrsk {
  private static frmVrskBLPN frm = null;
  public frmVrskBLPN() {
    super("blpn");
    frm = this;
  }
  public static frmVrskBLPN getFrm() {
    if (frm == null) frm = new frmVrskBLPN();
    return frm;
  }

  public boolean Validacija(char mod) {
    if (mod == 'N') {
      getRaQueryDataSet().setString("APP","blpn");
      com.borland.dx.dataset.Column[] keys = new com.borland.dx.dataset.Column[] {
        getRaQueryDataSet().getColumn("CVRSK"),getRaQueryDataSet().getColumn("APP")
      };
      String[] vals = new String[] {
        getRaQueryDataSet().getString("CVRSK"),getRaQueryDataSet().getString("APP")
      };
      if (Valid.getValid().chkExistsSQL(keys,vals)) {
        JraTextField jt = new JraTextField();
        jt.setColumnName("CVRSK");
        jt.setDataSet(getRaQueryDataSet());
        Valid.getValid().showValidErrMsg(jt,'U');
        return false;
      } else return true;
    } else return super.Validacija(mod);
  }

}