/****license*****************************************************************
**   file: frmVTZtrstav.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.VTZtr;
import hr.restart.baza.VTZtrt;
import hr.restart.baza.dM;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavBar;
import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class frmVTZtrstav extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpVTZtr jpDetail;

  boolean inedit, needUpdate, needRefresh = true;
  frmUlazTemplate frm;
  short dodkey;
  String[] ccols = {"lrbr", "czt", "cpar", "izt", "pzt", "prpor", 
                      "brrac", "datrac", "uldok", "datdosp"};

  public frmVTZtrstav() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void setMasterFrame(frmUlazTemplate fut) {
  	frm = fut;
  }

  public void needsRefresh(char mode) {
    needRefresh = true;
    if (needRefresh) {
      needRefresh = false;
      setNextDodKey();
      VTZtrt.getDataModule().setFilter("dodkey="+dodkey+" AND lrbr > 0");
      getRaQueryDataSet().open();
      if (mode == 'N')
        VTZtr.getDataModule().setFilter(Condition.whereAllEqual(frm.key, frm.getMasterSet())+" AND rbr=0");
      else
        VTZtr.getDataModule().setFilter(Condition.whereAllEqual(frm.key, frm.getMasterSet())+
                                        " AND rbr = "+frm.getDetailSet().getShort("RBR"));
      dm.getVTZtr().open();
      for (dm.getVTZtr().first(); dm.getVTZtr().inBounds(); dm.getVTZtr().next()) {
        getRaQueryDataSet().insertRow(false);
        getRaQueryDataSet().setShort("DODKEY", dodkey);
        dM.copyColumns(dm.getVTZtr(), getRaQueryDataSet(), ccols);
        if (mode == 'N') {
          Aus.mul(getRaQueryDataSet(), "IZT", getIVAL());
          Aus.div(getRaQueryDataSet(), "IZT", frm.getMasterSet().getBigDecimal("UINAB"));
        }
      }
      getRaQueryDataSet().saveChanges();
    }
  }

  public void kill() {
    removeTemp();
    if (isShowing()) rnvExit_action();
  }


  public void removeTemp() {
    if (dodkey>0)
      vl.runSQL("DELETE from vtztrt WHERE dodkey="+dodkey);
    dodkey=0;
  }

  public void beforeShow() {
    setEditEnabled(frm.raDetail.getMode() != 'B');
  }

  String stavq, renums;
  short delrbr;
  public void prepareSave() {
    delrbr = frm.getDetailSet().getShort("RBR");
    Condition c = Condition.whereAllEqual(frm.key, frm.getDetailSet());
    stavq = c + " AND rbr=" + delrbr;
    renums = c + " AND rbr>" + delrbr;
  }

  public void saveChanges(char mode) throws Exception {
    VTZtr.getDataModule().setFilter(stavq);
    dm.getVTZtr().open();
    dm.getVTZtr().deleteAllRows();
    if (mode != 'B') {
      QueryDataSet ds = VTZtrt.getDataModule().getTempSet(Condition.equal("DODKEY", dodkey));
      ds.open();
      for (ds.first(); ds.inBounds(); ds.next()) {
        if (ds.getShort("LRBR") == 0) continue;
        dm.getVTZtr().insertRow(false);
        dm.copyColumns(frm.getDetailSet(), dm.getVTZtr(), frm.key);
        dm.getVTZtr().setShort("RBR",  frm.getDetailSet().getShort("RBR"));
        dm.copyColumns(ds, dm.getVTZtr(), ccols);
      }
      ds.deleteAllRows();
      raTransaction.saveChanges(ds);
    }
    raTransaction.saveChanges(dm.getVTZtr());
    if (mode == 'B') {
      VTZtr.getDataModule().setFilter(renums);
      dm.getVTZtr().open();
      while (delrbr != 0) {
        short nextdel = 0, rbr;
        System.out.println("Renaming to "+delrbr);
        for (dm.getVTZtr().first(); dm.getVTZtr().inBounds(); dm.getVTZtr().next())
          if ((rbr = (short) (dm.getVTZtr().getShort("RBR") - 1)) == delrbr)
            dm.getVTZtr().setShort("RBR", rbr);
          else if (rbr > delrbr && (rbr < nextdel || nextdel == 0)) nextdel = rbr;
        raTransaction.saveChanges(dm.getVTZtr());
        delrbr = nextdel;
      }
    }
  }

  private void setNextDodKey() {
    do {
      vl.execSQL("SELECT MAX(dodkey) as maxkey FROM vtztrt");
      vl.RezSet.open();
      Variant v = new Variant();
      vl.RezSet.getVariant("MAXKEY", v);
      dodkey = (short) (v.getAsShort() + 1);
    } while (!vl.runSQL("INSERT INTO vtztrt(dodkey,lrbr) VALUES("+dodkey+",0)"));
  }

  public void updateZT() {
    if (inedit) needUpdate = true;
    else {
      needUpdate = false;
      calc.set("inab", getIVAL());
      performAllRows("IZT = inab % PZT");
      getRaQueryDataSet().saveChanges();
    }
  }

  public void afterSetMode(char oldm, char newm) {
    if (newm == 'B') {
      inedit = false;
      if (needUpdate) updateZT();
    }
  }

  private BigDecimal getIVAL() {
    return frm.getDetailSet().getBigDecimal("IDOB").subtract(
        frm.getDetailSet().getBigDecimal("IRAB"));
  }

  public void afterIZT() {
    Aus.percent(getRaQueryDataSet(), "PZT", "IZT", getIVAL());
  }

  public void afterPZT() {
    Aus.percentage(getRaQueryDataSet(), "IZT", getIVAL(), "PZT");
  }

  public void EntryPoint(char mode) {
    jpDetail.setupPanelForEntries();
  }

  public void SetFokus(char mode) {
    if (mode == 'I') {
      if (frm.raDetail.getMode() == 'N')
        jpDetail.jraPzt.requestFocus();
      else
        jpDetail.jraIzt.requestFocus();
      inedit = true;
    }
  }
  public boolean Validacija(char mode) {
    if (getRaQueryDataSet().getBigDecimal("IZT").signum() < 0) {
      jpDetail.jraIzt.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), "Iznos zavisnog troška ne smije biti negativan!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public boolean BeforeDelete() {
    return false;
  }

  public void AfterSave(char mode) {
    changedZT(true);
  }

  public void changedZT(boolean force) {
    calc.set("total", Aus.zero2);
    performAllRows("total += IZT");
    calc.runOn(frm.getDetailSet(), "IZT = total");
    if (force) ((IZavtrHandler) frm).getDetailPanel().kalkulacija(8);
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getVTZtrt());
    this.setVisibleCols(new int[] {1,2,3,4});
    jpDetail = new jpVTZtr(null, this);
    jpDetail.setupPanelForEntries();
    jpDetail.BindComponents(dm.getVTZtrt());
    this.setRaDetailPanel(jpDetail);
    this.getNavBar().removeStandardOptions(new int[]
        {raNavBar.ACTION_ADD, raNavBar.ACTION_DELETE, raNavBar.ACTION_TOGGLE_TABLE});
    removeRnvCopyCurr();
    this.getJpTableView().addTableModifier(
      new raTableColumnModifier("CZT", new String[] {"CZT", "NZT"}, dm.getZtr())
    );
    this.getJpTableView().addTableModifier(
      new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, dm.getPartneri())
    );

//    this.pack();
    startFrame.getStartFrame().centerFrame(this, 0, "Zavisni troškovi");
  }
}
