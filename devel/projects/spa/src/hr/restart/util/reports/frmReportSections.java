/****license*****************************************************************
**   file: frmReportSections.java
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
package hr.restart.util.reports;

import hr.restart.baza.Condition;
import hr.restart.baza.Logodat;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.raTableValueModifier;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmReportSections extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  sysoutTEST sys = new sysoutTEST(false);

  jpReportSections jpDetail;
  JraButton design = new JraButton();
//  raSectionDesigner designer = null;

  QueryDataSet data;
  QueryDataSet sect;

  int id;
  boolean input;

  public frmReportSections() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

//  public void beforeShow() {
//    designer = (raSectionDesigner) startFrame.getStartFrame().
//               showFrame("hr.restart.util.reports.raSectionDesigner", 0, "", false);
//  }

  public void SetFokus(char mode) {
//    designer.setDataSets(data, sect);
//    designer.runOnExit(null);
    if (mode != 'B') {
      if (mode == 'N') Logodat.getDataModule().setFilter(sect, "1 = 0");
      jpDetail.jraOpis.requestFocus();
    };
  }

//  public void afterSetMode(char oldm, char newm) {
//    if (newm == 'B' && designer.isShowing()) {
//      designer.hide();
//    }
//    if (newm == 'B') input = true;
//  }

  public boolean Validacija(char mode) {
    if (!sect.isOpen()) sect.open();
//    if (designer.isShowing()) {
//      if (!designer.checkBounds()) return false;
//      designer.hide();
//    }
    if (mode == 'N') {
      vl.execSQL("SELECT MAX(id) AS maxid FROM logodat");
      vl.RezSet.open();
      data.setInt("ID", vl.RezSet.getInt("MAXID") + 1);
    }
//    designer.setSection(data.getString("VRDOK"), data.getString("VRSTA"));
//    designer.outputElements();
//    sys.prn(data);
//    sys.prn(sect);
    return true;
  }

  public boolean BeforeDelete() {
    id = data.getInt("ID");
    return true;
  }

  public boolean doWithSave(char mode) {
    if (mode != 'B') {
      raTransaction.saveChanges(sect);
    } else {
      try {
        raTransaction.runSQL("DELETE FROM logodat WHERE id = "+id+" AND rbr > 0");
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }

  public void invokeDesign() {
    if (input) {
      sect.open();
//      designer.inputElements();
      input = false;
    }
    raSectionDesigner.show(this.getWindow(), jpDetail.jraOpis.getText().trim(), data, sect);
//    designer.setTitle(jpDetail.jraOpis.getText().trim());
//    designer.show();
  }

  public void raQueryDataSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    if (data.getInt("ID") != id) {
      id = data.getInt("ID");
      input = true;
      Logodat.getDataModule().setFilter(sect,
         Condition.equal("ID", id).
         and(Condition.where("RBR", Condition.GREATER_THAN, (short) 0)));
    }
  }

  private void jbInit() throws Exception {
    data = Logodat.getDataModule().getFilteredDataSet("RBR = 0");
    sect = Logodat.getDataModule().getTempSet("1 = 0");
    sect.setSort(new SortDescriptor(new String[] {"RBR"}));
    this.setRaQueryDataSet(data);
    this.setVisibleCols(new int[] {1, 2});
    jpDetail = new jpReportSections(this);
    this.setRaDetailPanel(jpDetail);

    this.getJpTableView().addTableModifier(new raTableValueModifier("VRSTA",
        new String[] {"P", "L"}, new String[] {"Portrait", "Landscape"}));

    this.getOKpanel().add(design, BorderLayout.WEST);
    jpDetail.BindComponents(data);
    design.setText("Dizajn");
    design.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        invokeDesign();
      }
    });
//    raSectionDesigner.isAccepted();
  }
}

/*


*/
