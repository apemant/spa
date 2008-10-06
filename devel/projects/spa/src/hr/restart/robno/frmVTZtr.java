/****license*****************************************************************
**   file: frmVTZtr.java
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
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavBar;
import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;


public class frmVTZtr extends raMatPodaci {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  int oldCPAR = -1;
  String oldBRRAC = "";


  boolean inedit, needUpdate, needRefresh = true;
  frmUlazTemplate frm;
  short dodkey;
  String[] ccols = {"lrbr", "czt", "cpar", "izt", "pzt", "prpor", 
                      "brrac", "datrac", "uldok", "datdosp"};
  String[] ecols = {"prpor", "brrac", "datrac", "uldok", "datdosp"};
  jpVTZtr jpDetail;

  public frmVTZtr() {
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

  public void needsRefresh() {
    needRefresh = true;
    if (needRefresh) {
      needRefresh = false;
      setNextDodKey();
      VTZtrt.getDataModule().setFilter("dodkey="+dodkey+" AND lrbr>0");
      getRaQueryDataSet().open();
      if (frm.raMaster.getMode() != 'N') {
        VTZtr.getDataModule().setFilter(Condition.whereAllEqual(frm.key, frm.getMasterSet())
                                        +" AND rbr = 0");
        dm.getVTZtr().open();
        for (dm.getVTZtr().first(); dm.getVTZtr().inBounds(); dm.getVTZtr().next()) {
          getRaQueryDataSet().insertRow(false);
          getRaQueryDataSet().setShort("DODKEY", dodkey);
          dM.copyColumns(dm.getVTZtr(), getRaQueryDataSet(), ccols);
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
    if (dodkey > 0)
      vl.runSQL("DELETE from vtztrt WHERE dodkey="+dodkey);
    dodkey = 0;
  }

  public void beforeShow() {
    boolean allow = frm.enableZT;
    setEditEnabled(allow);
    setEnabledNavAction(getNavBar().getStandardOption(raNavBar.ACTION_UPDATE), true);
    if (allow) enableAdd();
    else disableAdd();
  }

  Condition masterRow;
  public void prepareSave() {
    masterRow = Condition.whereAllEqual(frm.key, frm.getMasterSet());
  }

  public void saveChanges(char mode) throws Exception {
    DataSet tmp = getRaQueryDataSet();
    QueryDataSet zt = dm.getVTZtr(); 
    if (frm.enableZT || mode != 'I') {
      VTZtr.getDataModule().setFilter(masterRow+" AND rbr = 0");
      zt.open();
      zt.deleteAllRows();
      if (mode != 'B') {
        getJpTableView().enableEvents(false);
        for (tmp.first(); tmp.inBounds(); tmp.next()) {
          zt.insertRow(false);
          dm.copyColumns(frm.getMasterSet(), zt, frm.key);
          zt.setShort("RBR", (short) 0);
          dm.copyColumns(tmp, zt, ccols);
        }
        getJpTableView().enableEvents(true);
        raTransaction.runSQL("DELETE from vtztrt WHERE dodkey="+dodkey);
      }
      raTransaction.saveChanges(zt);
    } else {
      VTZtr.getDataModule().setFilter(masterRow);
      zt.open();
      getJpTableView().enableEvents(false);
      for (tmp.first(); tmp.inBounds(); tmp.next()) {
        for (zt.first(); zt.inBounds(); zt.next()) {
          if (tmp.getShort("LRBR") == zt.getShort("LRBR"))
            dm.copyColumns(tmp, zt, ccols);
        }
      }
      zt.post();
      getJpTableView().enableEvents(true);
      raTransaction.runSQL("DELETE from vtztrt WHERE dodkey="+dodkey);
      raTransaction.saveChanges(zt);
    }
  }

  public void updateZT() {
    if (inedit) needUpdate = true;
    else {
      needUpdate = false;
      int row = getRaQueryDataSet().getRow();
      getJpTableView().enableEvents(false);
      for (getRaQueryDataSet().first(); getRaQueryDataSet().inBounds(); getRaQueryDataSet().next())
        calcPZT();
      getRaQueryDataSet().goToRow(row);
      getJpTableView().enableEvents(true);
    }
  }

  public void afterSetMode(char oldm, char newm) {
    if (newm == 'B') {
      inedit = false;
      if (needUpdate) updateZT();
    }
  }

  public void calcPZT() {
    BigDecimal uinab = frm.getMasterSet().getBigDecimal("UINAB");
    if (uinab.signum() == 0)
      getRaQueryDataSet().setBigDecimal("PZT", _Main.nul);
    else
      getRaQueryDataSet().setBigDecimal("PZT", new BigDecimal(100 *
        getRaQueryDataSet().getBigDecimal("IZT").doubleValue() / uinab.doubleValue()));
  }

  public void calcIZT() {
    BigDecimal uinab = frm.getMasterSet().getBigDecimal("UINAB");
    getRaQueryDataSet().setBigDecimal("IZT", new BigDecimal(uinab.multiply(getRaQueryDataSet().
        getBigDecimal("PZT")).doubleValue() / 100).setScale(2, BigDecimal.ROUND_HALF_UP));
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

  private short getNextEntry() {
    vl.execSQL("SELECT MAX(lrbr) as maxrbr FROM vtztrt WHERE dodkey="+dodkey);
    vl.RezSet.open();
    Variant v = new Variant();
    vl.RezSet.getVariant("MAXRBR", v);
    return (short) (v.getAsShort() + 1);
  }

  public void afterCzt() {
    lookupData.getlookupData().raLocate(dm.getZtr(), "CZT", String.valueOf(getRaQueryDataSet().getShort("CZT")));
    if (dm.getZtr().getInt("CPAR") != 0)
      jpDetail.jlrCpar.setText(String.valueOf(dm.getZtr().getInt("CPAR")));
    else jpDetail.jlrCpar.setText("");
    jpDetail.jlrCpar.forceFocLost();
    jpDetail.setSkipCpar();
    jpDetail.setSkipPzt();
    getRaQueryDataSet().setBigDecimal("PZT", dm.getZtr().getBigDecimal("PZT"));
    calcIZT();
  }

  public void afterIZT() {
    calcPZT();
  }

  public void afterPZT() {
    calcIZT();
  }
  
  public void EntryPoint(char mode) {
    if (mode == 'I' && !frm.enableZT) {
      rcc.EnabDisabAll(jpDetail, false);
      rcc.setLabelLaF(jpDetail.jraPrpor, true);
      rcc.setLabelLaF(jpDetail.jraBrrac, true);
      rcc.setLabelLaF(jpDetail.jraDatrac, true);
      rcc.setLabelLaF(jpDetail.jraUldok, true);
      rcc.setLabelLaF(jpDetail.jraDatdosp, true);
    }
  }

  public void SetFokus(char mode) {
  	
    if (mode == 'N') {
      inedit = false;
      getRaQueryDataSet().setShort("DODKEY", dodkey);
      jpDetail.jlrCzt.setText("");
      jpDetail.jlrCzt.forceFocLost();
      jpDetail.jlrCpar.setText("");
      jpDetail.jlrNazpar.setText("");
      oldBRRAC="";
      oldCPAR=-1;

    } else if (mode=='I'){
    	oldBRRAC=getRaQueryDataSet().getString("BRRAC");
    	oldCPAR=getRaQueryDataSet().getInt("CPAR");
    }
    if (mode != 'B') {
      if (frm.enableZT) jpDetail.jlrCzt.requestFocus();
      else jpDetail.jraBrrac.requestFocus();
      inedit = true;
    }
  }
  
  
  public boolean Validacija(char mode) {
  	
  	if (frm.isDocumentAllreadyExist(getRaQueryDataSet().getString("BRRAC"),
  			oldBRRAC,getRaQueryDataSet().getInt("CPAR"),oldCPAR)==-1){
        JOptionPane.showMessageDialog(this.getWindow(), "Broj raèuna "+
        		getRaQueryDataSet().getString("BRRAC")+" za partnera " +
        		getRaQueryDataSet().getInt("CPAR")+ " vaæ postoji u evidenciji robnog knjigovodstva. ",
                "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
  	}
  	if (frm.isDocumentAllreadyExist(getRaQueryDataSet().getString("BRRAC"),
  			oldBRRAC,getRaQueryDataSet().getInt("CPAR"),oldCPAR)==-2){
        JOptionPane.showMessageDialog(this.getWindow(), "Broj raèuna "+
        		getRaQueryDataSet().getString("BRRAC")+" za partnera " +
        		getRaQueryDataSet().getInt("CPAR")+ " vaæ postoji u evidenciji saldakonti .",
                "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
  	}
  	
  	
    if (vl.isEmpty(jpDetail.jlrCzt))
      return false;
    if (getRaQueryDataSet().getBigDecimal("IZT").signum() < 0) {
      jpDetail.jraIzt.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Iznos zavisnog troška mora biti ve\u0107i od nule!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    if (getRaQueryDataSet().getBigDecimal("PRPOR").signum() < 0) {
      jpDetail.jraIzt.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Pretporez mora biti veæi od nule!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    if (getRaQueryDataSet().getBigDecimal("IZT").signum() == 0 &&
        getRaQueryDataSet().getBigDecimal("PRPOR").signum() == 0) {
      jpDetail.jraIzt.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Iznos zavisnog troška ili pretporez moraju biti veæi od nule!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    
    if (mode == 'N')
      getRaQueryDataSet().setShort("LRBR", getNextEntry());
//    System.out.println(getRaQueryDataSet());
    return true;
  }

  short oldRbr;
  public boolean BeforeDelete() {
    oldRbr = getRaQueryDataSet().getShort("LRBR");
    return true;
  }

  public boolean doWithSave(char mode) {
    if (mode == 'B') {
      vl.recountDataSet(this, "LRBR", oldRbr, false);
      raTransaction.saveChanges(getRaQueryDataSet());
    }
    return true;
  }

  public void AfterDelete() {
    changedZT();
  }

  public void AfterSave(char mode) {
    changedZT();
  }

  public void changedZT() {
    int row = getRaQueryDataSet().getRow();
    getJpTableView().enableEvents(false);
    BigDecimal total = _Main.nul;
    for (getRaQueryDataSet().first(); getRaQueryDataSet().inBounds(); getRaQueryDataSet().next())
      total = total.add(getRaQueryDataSet().getBigDecimal("IZT"));
    getRaQueryDataSet().goToRow(row);
    getJpTableView().enableEvents(true);
    frm.getMasterSet().setBigDecimal("UIZT", total);
    ((IZavtrHandler) frm).getMasterPanel().jtfUIZT_focusLost(null);
  }

  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getVTZtrt());
    this.setVisibleCols(new int[] {1,2,3,4});
    jpDetail = new jpVTZtr(this, null);
    jpDetail.BindComponents(dm.getVTZtrt());
    jpDetail.setupPanelForHeader();
    this.setRaDetailPanel(jpDetail);

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
