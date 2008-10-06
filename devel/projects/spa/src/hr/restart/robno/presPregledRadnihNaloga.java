/****license*****************************************************************
**   file: presPregledRadnihNaloga.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class presPregledRadnihNaloga extends PreSelect {
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JPanel jpPres = new JPanel();
  XYLayout xYLayout1 = new XYLayout();

  JlrNavField jlrNazvrsub = new JlrNavField();
  JlrNavField jlrVrsub = new JlrNavField();
  JraButton jbVrsub = new JraButton();
  JLabel jlVrsub = new JLabel();

  public presPregledRadnihNaloga() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus() {
    jlrVrsub.requestFocus();
  }

  public boolean Validacija() {
    if (vl.isEmpty(jlrVrsub)) return false;
//    jpPodaci.setFields(getSelRow().getShort("CVRSUBJ"));
//      xYLayout3.setHeight(emptyh + jpPodaci.getFieldCount() * 25 +
//         ((jpPodaci.getFieldCount() > 0) ? 10 : 0));

    return true;
  }

  private void jbInit() throws Exception {
    jpPres.setLayout(xYLayout1);
    xYLayout1.setWidth(535);
    xYLayout1.setHeight(60);

    jlVrsub.setText("Vrsta subjekta");

    jlrVrsub.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrVrsub.setColumnName("CVRSUBJ");
    jlrVrsub.setColNames(new String[] {"NAZVRSUBJ"});
    jlrVrsub.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazvrsub});
    jlrVrsub.setVisCols(new int[] {0,1});
    jlrVrsub.setSearchMode(0);
    jlrVrsub.setRaDataSet(dm.getRN_vrsub());
    jlrVrsub.setNavButton(jbVrsub);

    jlrNazvrsub.setNavProperties(jlrVrsub);
    jlrNazvrsub.setColumnName("NAZVRSUBJ");
    jlrNazvrsub.setSearchMode(1);

    jpPres.add(jlrNazvrsub, new XYConstraints(255, 20, 250, -1));
    jpPres.add(jlrVrsub, new XYConstraints(150, 20, 100, -1));
    jpPres.add(jbVrsub, new XYConstraints(510, 20, 21, 21));
    jpPres.add(jlVrsub, new XYConstraints(15, 20, -1, -1));

    this.setSelDataSet(dm.getRN_subjekt());
    this.setSelPanel(jpPres);
  }
}
