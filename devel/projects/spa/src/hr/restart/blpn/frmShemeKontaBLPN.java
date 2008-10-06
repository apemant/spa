/****license*****************************************************************
**   file: frmShemeKontaBLPN.java
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

import hr.restart.sisfun.frmShemeKonta;

public class frmShemeKontaBLPN extends frmShemeKonta {
  static frmShemeKontaBLPN frm = null;
  public frmShemeKontaBLPN() {
    super(frmVrskBLPN.getFrm().getRaQueryDataSet(), "Vrsta sheme", "CVRSK", "OPISVRSK", "blpn");
    frm = this;
  }
  public static frmShemeKontaBLPN getFrm() {
    if (frm == null) frm = new frmShemeKontaBLPN();
    return frm;
  }
}