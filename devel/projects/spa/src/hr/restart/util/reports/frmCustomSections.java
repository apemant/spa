/****license*****************************************************************
**   file: frmCustomSections.java
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
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableValueModifier;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmCustomSections extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JraButton design = new JraButton();
  jpCustomSections jpDetail;

  StorageDataSet vrsec = new StorageDataSet();

  QueryDataSet sect;

  String key = "", dok, sec, corg;
  boolean input,inedit,saving = false;

  public frmCustomSections() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void vrdokChanged() {
    if (inedit) {
      findVrsecs(false);
      jpDetail.jlrVrsec.setText("");
      jpDetail.jlrOpis.setText("");
    }
  }

  public void findVrsecs(boolean all) {
    vrsec.empty();
    vrsec.open();
    List l = raReportDescriptor.getCustomSect(getRaQueryDataSet().getString("VRDOK"));
    Iterator i = (!all && l != null ? l : raReportDescriptor.getAllSections()).iterator();
    while (i.hasNext()) {
      String sect = (String) i.next();
      vrsec.insertRow(false);
      vrsec.setString("VRSEC", sect);
      vrsec.setString("OPIS", raReportDescriptor.getSectionDescription(sect));
    }
    vrsec.post();
//    sysoutTEST sys = new sysoutTEST(false);
//    sys.prn(vrsec);
  }

  public void afterSetMode(char oldm, char newm) {
    inedit = (newm == 'N');
    if (newm == 'B') saving = false;
    findVrsecs(!inedit);
  }

  public void EntryPoint(char mode) {
    // Disabla tekst komponente kljuca kod izmjene
    if (mode == 'I') {
      rcc.setLabelLaF(jpDetail.jlrCorg, false);
      rcc.setLabelLaF(jpDetail.jlrNaziv, false);
      rcc.setLabelLaF(jpDetail.jbSelCorg, false);
      rcc.setLabelLaF(jpDetail.jlrVrdok, false);
      rcc.setLabelLaF(jpDetail.jlrNazdok, false);
      rcc.setLabelLaF(jpDetail.jbSelVrdok, false);
      rcc.setLabelLaF(jpDetail.jlrVrsec, false);
      rcc.setLabelLaF(jpDetail.jlrOpis, false);
      rcc.setLabelLaF(jpDetail.jbSelVrsec, false);
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      key = "";
      jpDetail.jlrVrsec.forceFocLost();
      jpDetail.jlrCorg.forceFocLost();
      jpDetail.jlrVrdok.forceFocLost();
      jpDetail.jlrCorg.requestFocus();
      Logodat.getDataModule().setFilter(sect, "1 = 0");
      sect.open();
    } else raQueryDataSet_navigated(null);
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpDetail.jlrCorg) || vl.isEmpty(jpDetail.jlrVrdok) ||
        vl.isEmpty(jpDetail.jlrVrsec))
      return false;
    if (mode == 'N' && vl.notUnique(new JTextComponent[]
        {jpDetail.jlrCorg, jpDetail.jlrVrdok, jpDetail.jlrVrsec}))
      return false;
    getRaQueryDataSet().setShort("RBR", (short) 0);
    getRaQueryDataSet().setString("VRSTA", "C");
    saving = true;
    return true;
  }

  public boolean BeforeDelete() {
    corg = getRaQueryDataSet().getString("CORG");
    sec = getRaQueryDataSet().getString("VRSEC");
    dok = getRaQueryDataSet().getString("VRDOK");
    return true;
  }

  public boolean doWithSave(char mode) {
    if (mode != 'B') {
      saving = false;
      raTransaction.saveChanges(sect);
    } else {
      try {
        String q = "DELETE FROM logodat WHERE vrsta='C' AND corg='"+corg+
           "' AND vrsec='"+sec+"' AND vrdok='"+dok+"' AND rbr > 0";
        System.out.println(q);
        raTransaction.runSQL(q);
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }

  private String getKey() {
    return new VarStr(getRaQueryDataSet().getString("CORG")).append('-')
        .append(getRaQueryDataSet().getString("VRSEC")).append('-')
        .append(getRaQueryDataSet().getString("VRDOK")).toString();
  }

  public void invokeDesign() {
    if (vl.isEmpty(jpDetail.jlrCorg) || vl.isEmpty(jpDetail.jlrVrdok) ||
        vl.isEmpty(jpDetail.jlrVrsec)) return;
    if (input) {
      sect.open();
//      designer.inputElements();
      input = false;
    }
    raSectionDesigner.setKey(getRaQueryDataSet().getString("CORG"), "C",
                             getRaQueryDataSet().getString("VRSEC"),
                             getRaQueryDataSet().getString("VRDOK"));
    raSectionDesigner.show(this.getWindow(), jpDetail.jlrOpis.getText() + " - " +
                           jpDetail.jlrNazdok.getText(), getRaQueryDataSet(), sect);
//    designer.setTitle(jpDetail.jraOpis.getText().trim());
//    designer.show();
  }

  public void AfterSave(char mode) {
    if (mode == 'N') {
      key = "";
      raQueryDataSet_navigated(null);
    }
  }

  public void raQueryDataSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    if (!getKey().equals(key) && !saving) {
      key = getKey();
      input = true;
      Logodat.getDataModule().setFilter(sect,
         Condition.equal("VRSTA", "C").
         and(Condition.equal("CORG", getRaQueryDataSet().getString("CORG"))).
         and(Condition.equal("VRDOK", getRaQueryDataSet().getString("VRDOK"))).
         and(Condition.equal("VRSEC", getRaQueryDataSet().getString("VRSEC"))).
         and(Condition.where("RBR", Condition.GREATER_THAN, (short) 0)));
    }
  }

  private void jbInit() throws Exception {
    dm.getLogodatCustom().open();
    this.setRaQueryDataSet(dm.getLogodatCustom());
    this.setVisibleCols(new int[] {0,1,2}); /**@todo: Odrediti vidljive kolone */
    vrsec.setColumns(new Column[] {
      dM.createStringColumn("VRSEC", "Dio izvještaja", 3),
      dM.createStringColumn("OPIS", "Opis", 50)
    });
    vrsec.open();
    vrsec.getColumn("VRSEC").setWidth(6);

    sect = Logodat.getDataModule().getTempSet("1 = 0");
    sect.setSort(new SortDescriptor(new String[] {"RBR"}));
    jpDetail = new jpCustomSections(this);
    jpDetail.BindComponents(getRaQueryDataSet());
    this.getJpTableView().addTableModifier(
        new raTableValueModifier("VRSEC", raReportDescriptor.getSectionsMap()));
    this.getJpTableView().addTableModifier(
        new raTableColumnModifier("VRDOK", new String[] {"NAZDOK"}, dm.getVrdokum()));
    this.getJpTableView().addTableModifier(
        new raTableColumnModifier("CORG", new String[] {"CORG", "NAZIV"}, dm.getOrgstruktura()));
    this.setRaDetailPanel(jpDetail);

    this.getOKpanel().add(design, BorderLayout.WEST);
    design.setText("Dizajn");
    design.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        invokeDesign();
      }
    });
  }
}
