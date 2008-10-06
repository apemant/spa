/****license*****************************************************************
**   file: jpMenus.java
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
package hr.restart.util.menus;

import hr.restart.baza.Condition;
import hr.restart.baza.Menus;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpMenus extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmMenus fMenus;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCmenu = new JLabel();
  JLabel jlCsort = new JLabel();
  JLabel jlMenutype = new JLabel();
  JLabel jlMethod = new JLabel();
  JLabel jlOpis = new JLabel();
  JLabel jlParentmenu = new JLabel();
  JraButton jbSelParentmenu = new JraButton();
  JraTextField jraCmenu = new JraTextField();
  JraTextField jraCsort = new JraTextField();
  raComboBox rcbMenutype = new raComboBox();
  JraTextField jraMethod = new JraTextField();
  JraTextField jraDescription = new JraTextField();
  JlrNavField jlrParentmenu = new JlrNavField();

  public jpMenus(frmMenus f) {
    try {
      fMenus = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraCmenu.setDataSet(ds);
    jraCsort.setDataSet(ds);
    rcbMenutype.setDataSet(ds);
    jraMethod.setDataSet(ds);    
    jlrParentmenu.setDataSet(ds);
  }
  
  public void init(char mode, String desc) {
    if (mode == 'I') {
      rcc.setLabelLaF(jraCmenu, false);
      rcc.setLabelLaF(jlrParentmenu, false);
      jraDescription.setText("");
    } else jraDescription.setText(desc);
  }
  
  public void setFocus(char mode) {
    if (mode == 'N') jraCmenu.requestFocus();
    else if (mode == 'I') jraDescription.requestFocus();
  }
  
  public boolean validate(char mode) {
    if (vl.isEmpty(jraCmenu) || vl.isEmpty(jraMethod) || vl.isEmpty(jraDescription))
      return false;
    if (mode == 'I' && vl.isEmpty(jraCsort)) 
      return false;
    return true;
  }
  
  public String getDescription() {
    return jraDescription.getText();
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(565);
    lay.setHeight(135);
    
    jlCmenu.setText("Šifra stavke");
    jlCsort.setText("Poredak");
    jlMenutype.setText("Tip");
    jlMethod.setText("Metoda");
    jlOpis.setText("Opis stavke");
    jlParentmenu.setText("Izbornik");
    jraCmenu.setColumnName("CMENU");
    jraCsort.setColumnName("CSORT");    
    jraMethod.setColumnName("METHOD");
    rcbMenutype.setColumnName("MENUTYPE");
    rcbMenutype.setRaItems(new String[][] {
        {"Stavka", "I"},
        {"Izbornik", "M"},
        {"Separator", "S"}
    });
    rcbMenutype.setSelectedIndex(0);

    jlrParentmenu.setColumnName("PARENTCMENU");
    jlrParentmenu.setVisCols(new int[] {0}); /**@todo: Dodati visible cols za lookup frame */
    jlrParentmenu.setSearchMode(0);
    jlrParentmenu.setNavColumnName("CMENU");
    jlrParentmenu.setRaDataSet(Menus.getDataModule().
        getFilteredDataSet(Condition.equal("MENUTYPE", "M")));
    jlrParentmenu.setNavButton(jbSelParentmenu);
    jlrParentmenu.setFocusLostOnShow(false);
    jlrParentmenu.setSearchMode(1);
    

    jpDetail.add(jbSelParentmenu, new XYConstraints(330, 45, 21, 21));
    jpDetail.add(jlCmenu, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlCsort, new XYConstraints(380, 45, -1, -1));
    jpDetail.add(jlMenutype, new XYConstraints(380, 20, -1, -1));
    jpDetail.add(jlMethod, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlParentmenu, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrParentmenu, new XYConstraints(150, 45, 175, -1));
    jpDetail.add(jraCmenu, new XYConstraints(150, 20, 175, -1));
    jpDetail.add(jraCsort, new XYConstraints(450, 45, 100, -1));
    jpDetail.add(rcbMenutype, new XYConstraints(450, 20, 100, -1));
    jpDetail.add(jraMethod, new XYConstraints(150, 70, 400, -1));
    jpDetail.add(jraDescription, new XYConstraints(150, 95, 400, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
