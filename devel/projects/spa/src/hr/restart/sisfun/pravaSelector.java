/****license*****************************************************************
**   file: pravaSelector.java
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
package hr.restart.sisfun;
import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.PreSelect;

import javax.swing.JPanel;
public class pravaSelector {
  static pravaSelector pravaSel;
  PreSelect pres = new PreSelect();
  JPanel jpSel = new JPanel();
  JraTextField jtCGRUPEUSERA = new JraTextField();
  dM dm;

  public pravaSelector() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
    jtCGRUPEUSERA.setColumnName("CGRUPEUSERA");
    jtCGRUPEUSERA.setDataSet(dm.getGrupeusera());
    jpSel.add(jtCGRUPEUSERA, null);
    pres.setSelDataSet(dm.getGrupeusera());
    pres.setSelPanel(jpSel);
  }
  public void filtStavke(String cgrus) {
    pres.getSelRow().setString("CGRUPEUSERA",cgrus);
    pres.doSelect();
  }
  public static void showFilteredRows(String cgrus) {
    if (pravaSel == null) pravaSel = new pravaSelector();
    pravaSel.filtStavke(cgrus);
  }
}
