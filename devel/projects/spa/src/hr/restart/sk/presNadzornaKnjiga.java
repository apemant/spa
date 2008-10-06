/****license*****************************************************************
**   file: presNadzornaKnjiga.java
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
package hr.restart.sk;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class presNadzornaKnjiga extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlGod = new JLabel();
  JraTextField jraGod = new JraTextField();
  JraTextField jraKnjig = new JraTextField();

  public presNadzornaKnjiga() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus() {
    jraGod.requestFocus();
    jraKnjig.getDataSet().setString("KNJIG", OrgStr.getKNJCORG());
  }

  public boolean Validacija() {
    if (vl.isEmpty(jraGod))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setSelDataSet(dm.getNadzornaKnjiga());
    jpDetail.setLayout(lay);
    lay.setWidth(265);
    lay.setHeight(60);

    jlGod.setText("Godina");
    jraGod.setColumnName("GOD");
    jraGod.setDataSet(getSelDataSet());
    jraKnjig.setColumnName("KNJIG");
    jraKnjig.setDataSet(getSelDataSet());


    jpDetail.add(jlGod, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraGod, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraKnjig, new XYConstraints(550, 20, 100, -1));
		jraKnjig.setVisible(false);
    jraKnjig.setEnabled(false);

    this.setSelPanel(jpDetail);
  }
}
