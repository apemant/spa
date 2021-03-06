/****license*****************************************************************
**   file: frmPravaUser.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Pravauser;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raMatPodaci;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmPravaUser extends raMatPodaci {
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  JPanel jpMain = new JPanel();
  JLabel jlPravo = new JLabel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlOznaka = new JLabel();
  JLabel jlOpis = new JLabel();
  JlrNavField jnvOznaka = new JlrNavField();
  JlrNavField jnvOpis = new JlrNavField();
  JraButton jbSelOpis = new JraButton();
  Valid vl = Valid.getValid();
  dM dm;
  com.borland.dx.dataset.DataRow dr;
  JraCheckBox jcbOn = new JraCheckBox();
  JraCheckBox jcbOff = new JraCheckBox();

  String user;

  public frmPravaUser() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    dm = hr.restart.baza.dM.getDataModule();
//    this.setRaQueryDataSet(AButil.getAButil().getPravaUser());
    this.setRaQueryDataSet(dm.getPravauser());
    this.setVisibleCols(new int[] {1,2});
    jlPravo.setText("Pravo");
    jpMain.setLayout(xYLayout1);
    xYLayout1.setWidth(486);
    xYLayout1.setHeight(108);
    jlOznaka.setText("Oznaka");
    jlOpis.setText("Opis");
    jbSelOpis.setText("...");

    jnvOznaka.setColumnName("CPRAVA");
    jnvOznaka.setTextFields(new javax.swing.text.JTextComponent[] {jnvOpis});
    jnvOznaka.setColNames(new String[] {"OPIS"});
    jnvOznaka.setSearchMode(0);
    jnvOznaka.setDataSet(this.getRaQueryDataSet());
    jnvOznaka.setRaDataSet(dm.getPrava());
    jnvOznaka.setVisCols(new int[] {0,1,2,5});
    jnvOznaka.setNavButton(jbSelOpis);

    jnvOpis.setColumnName("OPIS");
    jnvOpis.setNavProperties(jnvOznaka);
    jnvOpis.setSearchMode(1);

    jcbOn.setText(" Odobri");
    jcbOff.setText(" Zabrani");

    jcbOn.setColumnName("POZ");
    jcbOn.setDataSet(this.getRaQueryDataSet());
    jcbOn.setSelectedDataValue("D");
    jcbOn.setUnselectedDataValue("N");

    jcbOff.setColumnName("POZ");
    jcbOff.setDataSet(this.getRaQueryDataSet());
    jcbOff.setSelectedDataValue("N");
    jcbOff.setUnselectedDataValue("D");

    jpMain.add(jlPravo,new XYConstraints(20, 45, -1, -1));
    jpMain.add(jlOznaka,new XYConstraints(135, 20, -1, -1));
    jpMain.add(jlOpis,new XYConstraints(186, 20, -1, -1));
    jpMain.add(jnvOznaka,new XYConstraints(120, 40, 60, -1));
    jpMain.add(jnvOpis,new XYConstraints(185, 40, 250, -1));
    jpMain.add(jbSelOpis,new XYConstraints(440, 40, 21, 21));
    jpMain.add(jcbOn, new XYConstraints(185, 65, -1, -1));
    jpMain.add(jcbOff, new XYConstraints(300, 65, -1, -1));

    this.getJpTableView().addTableModifier(
        new raTableColumnModifier("CPRAVA", new String[] {"CPRAVA", "OPIS"}, dm.getPrava()));

    this.setRaDetailPanel(jpMain);
  }

/*  public void initDataSet() {
    AButil.getAButil().getPravaUser();
    this.getJpTableView().fireTableDataChanged();

  } */

  public void setUser(String user) {
    this.user = user;
    Pravauser.getDataModule().setFilter(Condition.equal("CUSER", user));
    dm.getPravauser().open();
    this.getJpTableView().fireTableDataChanged();
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jnvOznaka.forceFocLost();
      jnvOznaka.requestFocus();
    }
    if (mode == 'I') {
      rcc.setLabelLaF(jnvOznaka, false);
      rcc.setLabelLaF(jnvOpis, false);
      rcc.setLabelLaF(jbSelOpis, false);
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jnvOznaka)) return false;
    if (mode == 'N') {
      if (vl.chkExistsSQL(new Column[] {getRaQueryDataSet().getColumn("CUSER"),
                                        getRaQueryDataSet().getColumn("CPRAVA")},
                          new String[] {user, jnvOznaka.getText()})) {
        vl.showValidErrMsg(jnvOznaka,'U');
        return false;
      }
      getRaQueryDataSet().setString("CUSER", user);
    }
//    dm.getPravauser().setShort("CPRAVA",Short.valueOf(jnvOznaka.getText()).shortValue());
//    dm.getPravauser().setString("POZ", this.getRaQueryDataSet().getString("POZ"));
    return true;
  }

/*  public void AfterSave(char mode) {
    initDataSet();
  }
  public boolean BeforeDelete() {
    dm.getPravauser().deleteRow();
    return true;
  }*/

/*  private boolean isRow() {
    return dm.getPravauser().getString("CUSER").equals(this.getRaQueryDataSet().getString("CUSER")) &&
           dm.getPravauser().getShort("CPRAVA") == this.getRaQueryDataSet().getShort("CPRAVA");
  }

  public void raQueryDataSet_navigated(NavigationEvent e) {
    if (isRow()) return;
    if (this.getRaQueryDataSet().getString("CUSER").equals("") ||
        this.getRaQueryDataSet().getShort("CPRAVA") == 0)
      return;
    dm.getPravauser().first();
    while (!isRow()) dm.getPravauser().next();
  } */
}
