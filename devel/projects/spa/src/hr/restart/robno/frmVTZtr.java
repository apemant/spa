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
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavBar;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

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
    /*boolean allow = frm.enableZT;
    setEditEnabled(allow);
    setEnabledNavAction(getNavBar().getStandardOption(raNavBar.ACTION_UPDATE), true);*/
    /*if (allow) enableAdd();
    else disableAdd();*/
  }

  Condition masterRow;
  public void prepareSave() {
    masterRow = Condition.whereAllEqual(frm.key, frm.getMasterSet());
    System.out.println(masterRow);
  }

  public void saveChanges(char mode) throws Exception {
    //DataSet tmp = getRaQueryDataSet();
    if (frm.enableZT || mode != 'I') {
      QueryDataSet zt = VTZtr.getDataModule().getTempSet(masterRow+" AND rbr = 0");
      zt.open();
      zt.deleteAllRows();
      
      if (mode != 'B') {
        QueryDataSet tmp = VTZtrt.getDataModule().getTempSet(Condition.equal("DODKEY", dodkey));
        tmp.open();
        //getJpTableView().enableEvents(false);
        for (tmp.first(); tmp.inBounds(); tmp.next()) {
          if (tmp.getShort("LRBR") == 0) continue;
          zt.insertRow(false);
          dm.copyColumns(frm.getMasterSet(), zt, frm.key);
          zt.setShort("RBR", (short) 0);
          dm.copyColumns(tmp, zt, ccols);
        }
        //getJpTableView().enableEvents(true);
        //raTransaction.runSQL("DELETE from vtztrt WHERE dodkey="+dodkey);
        tmp.deleteAllRows();
        raTransaction.saveChanges(tmp);
      }
      raTransaction.saveChanges(zt);
    } else {      
      raProcess.runChild(frm.raMaster.getWindow(), new Runnable() {
        public void run() {
          QueryDataSet tmp = VTZtrt.getDataModule().getTempSet(Condition.equal("DODKEY", dodkey));
          tmp.open();
          QueryDataSet zt = VTZtr.getDataModule().getTempSet(masterRow);
          zt.open();

          repairStructuralDiffs(tmp, zt);

          for (tmp.first(); tmp.inBounds(); tmp.next()) {
            for (zt.first(); zt.inBounds(); zt.next()) {
              if (tmp.getShort("LRBR") == zt.getShort("LRBR") && tmp.getShort("CZT") == zt.getShort("CZT"))
                dm.copyColumns(tmp, zt, ecols);
            }
          }
          zt.post();
          raTransaction.saveChanges(zt);
        }
      });
      if (!raProcess.isCompleted()) throw raProcess.getLastException();
      raTransaction.runSQL("DELETE from vtztrt WHERE dodkey="+dodkey);
    }
  }
  
  private TreeSet findDeleted(QueryDataSet tmp, QueryDataSet zt) {
    ArrayList rbrs = new ArrayList();
    for (zt.first(); zt.inBounds(); zt.next()) 
      if (zt.getShort("RBR") == 0) rbrs.add(Short.valueOf(zt.getShort("LRBR")));
    Collections.sort(rbrs);
    
    int del = 0;
    TreeSet delRbr = new TreeSet();
    
    for (int i = 0; i < rbrs.size(); i++)
    
      for (zt.first(); zt.inBounds(); zt.next()) 
        if (zt.getShort("RBR") == 0 && zt.getShort("LRBR") == ((Short) rbrs.get(i)).shortValue()) {
          boolean found = false;
          for (tmp.first(); tmp.inBounds(); tmp.next())
            if (tmp.getShort("LRBR") != 0 && tmp.getShort("LRBR")+del == zt.getShort("LRBR") &&
              tmp.getShort("CZT") == zt.getShort("CZT") &&
              tmp.getBigDecimal("PZT").compareTo(zt.getBigDecimal("PZT")) == 0 &&
              tmp.getBigDecimal("IZT").compareTo(zt.getBigDecimal("IZT")) == 0) found = true;
          
          if (!found) {
            ++del;
            delRbr.add(rbrs.get(i));
          }
        }
    return delRbr;
  }
  
  void repairStructuralDiffs(QueryDataSet tmp, QueryDataSet zt) {
    
    TreeSet delRbr = findDeleted(tmp, zt);
    
    if (delRbr.size() > 0) deleteZav(zt, delRbr);
    
    int oldcount = zt.getRowCount();
    for (tmp.first(); tmp.inBounds(); tmp.next()) 
      if (tmp.getShort("LRBR") != 0) {
        boolean found = false;
        for (zt.first(); zt.inBounds(); zt.next())
          if (zt.getShort("RBR") == 0 && tmp.getShort("LRBR") == zt.getShort("LRBR")) {
            found = true;
            if (tmp.getShort("CZT") != zt.getShort("CZT") ||
                tmp.getBigDecimal("PZT").compareTo(zt.getBigDecimal("PZT")) != 0 ||
                tmp.getBigDecimal("IZT").compareTo(zt.getBigDecimal("IZT")) != 0)
              new Throwable("Weird error!").printStackTrace();
          }
        if (!found) insertZav(tmp, zt);
      }    
    
    if (delRbr.size() > 0 || oldcount != zt.getRowCount())
      frm.recalcFromZtr(zt);
  }
  
  private void insertZav(QueryDataSet tmp, QueryDataSet zt) {
    zt.insertRow(false);
    dm.copyColumns(frm.getMasterSet(), zt, frm.key);
    zt.setShort("RBR", (short) 0);
    dm.copyColumns(tmp, zt, ccols);
    try {
      long row = frm.getDetailSet().getInternalRow();
      frm.raDetail.getJpTableView().enableEvents(false);
      for (frm.getDetailSet().first(); frm.getDetailSet().inBounds(); frm.getDetailSet().next()) {
        zt.insertRow(false);
        dm.copyColumns(frm.getMasterSet(), zt, frm.key);
        zt.setShort("RBR", frm.getDetailSet().getShort("RBR"));
        dm.copyColumns(tmp, zt, ccols);
        BigDecimal uinab = frm.getDetailSet().getBigDecimal("IDOB").subtract(frm.getDetailSet().getBigDecimal("IRAB"));
        Aus.percentage(zt, "IZT", uinab, "PZT");
        zt.post();
      }
      frm.getDetailSet().goToInternalRow(row);
    } finally {
      frm.raDetail.getJpTableView().enableEvents(true);
    }
    raTransaction.saveChanges(zt);
  }
  
  private void deleteZav(QueryDataSet zt, TreeSet delRbr) {
    for (zt.first(); zt.inBounds(); )
      if (delRbr.contains(Short.valueOf(zt.getShort("LRBR"))))
        zt.deleteRow();
      else zt.next();
    
    raTransaction.saveChanges(zt);
    
    short min = ((Short) delRbr.first()).shortValue();
    while (min > 0) {
      short next = 0;
      for (zt.first(); zt.inBounds(); zt.next())
        if (zt.getShort("RBR") == 0 && zt.getShort("LRBR") > min && 
          (next == 0 || zt.getShort("LRBR") < next)) next = zt.getShort("LRBR");
      
      if (next == 0) min = 0; 
      else {
        for (zt.first(); zt.inBounds(); zt.next())
          if (zt.getShort("LRBR") == next) zt.setShort("LRBR", min);
        raTransaction.saveChanges(zt);
        ++min;
      }
    }
  }
  
  private boolean missingZavtr(QueryDataSet tmp, QueryDataSet zt) {
    for (zt.first(); zt.inBounds(); zt.next()) {
      boolean found = zt.getShort("LRBR") == 0;
      for (tmp.first(); tmp.inBounds(); tmp.next())
        if (tmp.getShort("LRBR") == zt.getShort("LRBR") &&
            tmp.getShort("CZT") == zt.getShort("CZT"))
          found = true;
      if (!found) return true;
    }
    return false;
  }
  
  public boolean findStructuralDiffs() {
    QueryDataSet zt = VTZtr.getDataModule().getTempSet(Condition.whereAllEqual(frm.key, frm.getMasterSet()) + " AND rbr = 0");
    zt.open();
    QueryDataSet tmp = VTZtrt.getDataModule().getTempSet(Condition.equal("DODKEY", dodkey));
    tmp.open();
    if (missingZavtr(tmp, zt) || missingZavtr(zt,  tmp))
      return JOptionPane.showConfirmDialog(frm.raMaster.getWindow(), "Želite li preraèunati zavisne troškove na stavkama?",
            "Rekalkulacija", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION;
    
    return false;
  }

  public void updateZT() {
    if (inedit) needUpdate = true;
    else {
      needUpdate = false;
      calc.set("uinab", frm.getMasterSet().getBigDecimal("UINAB"));
      performAllRows("PZT = IZT,7 %% uinab");
      getRaQueryDataSet().saveChanges();
    }
  }

  public void afterSetMode(char oldm, char newm) {
    if (newm == 'B') {
      inedit = false;
      if (needUpdate) updateZT();
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
    //jpDetail.setSkipCpar();
    //jpDetail.setSkipPzt();
    Aus.set(getRaQueryDataSet(), "PZT", dm.getZtr());
    afterPZT();
    jpDetail.jraIzt.requestFocusLater();
  }

  public void afterIZT() {
    Aus.percent(getRaQueryDataSet(), "PZT", "IZT", frm.getMasterSet().getBigDecimal("UINAB"));
  }

  public void afterPZT() {
    Aus.percentage(getRaQueryDataSet(), "IZT", frm.getMasterSet().getBigDecimal("UINAB"), "PZT");
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
      if (frm.enableZT || mode == 'N') jpDetail.jlrCzt.requestFocus();
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
    calc.set("total", Aus.zero2);
    performAllRows("total += IZT");
    calc.runOn(frm.getMasterSet(), "UIZT = total");
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
