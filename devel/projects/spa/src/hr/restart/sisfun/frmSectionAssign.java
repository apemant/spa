/****license*****************************************************************
**   file: frmSectionAssign.java
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
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableValueModifier;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;


public class frmSectionAssign extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpSectionAssign jpDetail;

  String oldvr = "-";

  public frmSectionAssign() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPoint(char mode) {
    if (mode == 'I') {
      jpDetail.setEdit();
      jpDetail.setVrdokVisible(getRaQueryDataSet().getString("VRSEC").equals("FDD"));
    }
    jpDetail.jlrId.forceFocLost();
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      getRaQueryDataSet().setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG(false));
      jpDetail.jlrCorg.forceFocLost();
      jpDetail.jlrId.forceFocLost();
      jpDetail.rcbVrdokvr.setSelectedIndex(4);
      jpDetail.rcbVrdokvr.requestFocus();
    } else if (mode == 'I') {
      jpDetail.jlrOpis.requestFocus();
    } else {
      jpDetail.setVrdokVisible(getRaQueryDataSet().getString("VRSEC").equals("FDD"));
      jpDetail.jlrId.forceFocLost();
    }
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jlrOpis))
      return false;

    if (mode == 'N') {
      if (jpDetail.rcbVrdokvr.getSelectedIndex() == 4) {
        if (vl.notUnique(new JTextComponent[] {jpDetail.jlrCorg, jpDetail.jlrVrdok}))
          return false;
      } else {
        if (vl.chkExistsSQL(new Column[] {getRaQueryDataSet().getColumn("CORG"),
                                          getRaQueryDataSet().getColumn("VRSEC")},
                            new String[] {getRaQueryDataSet().getString("CORG"),
                                          getRaQueryDataSet().getString("VRSEC")})) {
          jpDetail.rcbVrdokvr.requestFocus();
          vl.showValidErrMsg(jpDetail.jlrCorg, 'U');
          return false;
        }
        getRaQueryDataSet().setString("VRDOK", "");
      }
    }

    System.out.println(getRaQueryDataSet());
    return true;
  }

//  public void raQueryDataSet_navigated(NavigationEvent e) {
//      jpDetail.dataChanged();
//    }
//  }

  private void jbInit() throws Exception {
    dm.getDiorep().open();
    this.setRaQueryDataSet(dm.getDiorep());
    this.setVisibleCols(new int[] {0, 1, 2, 3});
    jpDetail = new jpSectionAssign(this);
    jpDetail.BindComponents(this.getRaQueryDataSet());
    this.setRaDetailPanel(jpDetail);
    this.getJpTableView().addTableModifier(new raTableColumnModifier("ID",
        new String[] {"OPIS"}, jpDetail.jlrId.getRaDataSet()));
    this.getJpTableView().addTableModifier(new raTableColumnModifier("CORG",
        new String[] {"CORG", "NAZIV"}, dm.getOrgstruktura()));
    this.getJpTableView().addTableModifier(new raTableValueModifier("VRSEC",
        new String[] {"HID", "FID", "HUD", "FUD", "FDD"},
        new String[] {"Header izlaznih dokumenata",
                      "Footer izlaznih dokumenata",
                      "Header internih dokumenata",
                      "Footer internih dokumenata",
                      "Podnožje dokumenata tipa -"}
    ));
  }
}
