/****license*****************************************************************
**   file: raPOS.java
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

import java.awt.event.KeyEvent;

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

public class raPOS extends raIzlazTemplate  {
//  hr.restart.util.startFrame SF;
//  QueryDataSet stanjeforChange = new QueryDataSet();
  
  frmTableDataView viewReq = new frmTableDataView(false, false, true);

  public void initialiser(){
    what_kind_of_dokument = "POS";
  }

  public void MyaddIspisMaster(){
    raMaster.getRepRunner().addReport("hr.restart.robno.repPOS","Razduženje maloprodaje",2);
  }

  public void MyaddIspisDetail(){
    raDetail.getRepRunner().addReport("hr.restart.robno.repPOS","Razduženje maloprodaje",2);
  }
  public raPOS() {
    setPreSel((jpPreselectDoc) presPOS.getPres());
    addButtons(true,false);
    master_titel = "Razduženja maloprodaje";
    detail_titel_mno = "Stavke razduženja maloprodaje";
    detail_titel_jed = "Stavka razduženja maloprodaje";
    setMasterSet(dm.getZagPos());
    setDetailSet(dm.getStDokiPos());
    raDetail.addOption(rnvDellAllStav, 3);
    this.raMaster.addOption(new raNavAction("Pregled materijala", raImages.IMGMOVIE, KeyEvent.VK_F8) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        showRequirementsMaster();
      }
    },4);
    MP.BindComp();
    DP.BindComp();
  }

  public void revive() {
    super.revive();
    try {
      raTransaction.runSQL("UPDATE pos SET status='N', rdok='' WHERE " +
          Condition.equal("RDOK", key4delZag));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void brisiVezu() {
    // empty
  }
  
  public void cskl2csklart() {
    // empty
  }
  
  public void keyActionMaster() {
   frmPos2POS.razdMP(getPreSelect().getSelRow(), getMasterSet(), getDetailSet());
     if (frmPos2POS.getFrmpos().errors==null){
       if (frmPos2POS.getFrmpos().isCancelPress()) return;
       frmPos2POS.getFrmpos().saveAll();
       //raMaster.getJpTableView().fireTableDataChanged();
       afterOKSC();
     }
     else {
       getDetailSet().refresh();
       int res = JOptionPane.showConfirmDialog(raMaster.getWindow(), 
           new raMultiLineMessage(frmPos2POS.getFrmpos().errors +
                "\n\nDetaljni prikaz grešaka?",
  	            SwingConstants.LEADING), "Greška", 
  	            JOptionPane.ERROR_MESSAGE,
  	            JOptionPane.OK_CANCEL_OPTION);
       if (res == JOptionPane.OK_OPTION)
         SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            frmTableDataView errs = new frmTableDataView();
            errs.setTitle("Popis artikala s nedovoljnom zalihom");
            errs.setSaveName("view-transfer");
            errs.setDataSet(frmPos2POS.getFrmpos().errorSet);
            errs.jp.getMpTable().setAutoResizeMode(
                JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            errs.show();
          }
        });
     }
  }
  
  String[] reqc = {"CART", "CART1", "BC", "NAZART", "JM", "KOL"};
  void showRequirementsMaster() {
    if (getMasterSet().getRowCount() == 0) return;
    
    final StorageDataSet reqs = stdoki.getDataModule().getScopedSet(
    "CSKL CART CART1 BC NAZART JM KOL NC INAB KOL1 KOL2");
    reqs.open();
    reqs.getColumn("KOL1").setCaption("Stanje");
    reqs.getColumn("KOL2").setCaption("Rezultat");
    
    raProcess.runChild(raMaster.getWindow(), new Runnable() {
      public void run() {
        if (raMaster.getSelectCondition() == null) {
          DataSet ds = stdoki.getDataModule().getTempSet(
              Condition.whereAllEqual(Util.mkey, getMasterSet()));
          raProcess.openScratchDataSet(ds);
          fillRequirements(reqs, ds);
          ds.close();
        } else {
          int row = getMasterSet().getRow();
          raMaster.getJpTableView().enableEvents(false);

          for (getMasterSet().first(); getMasterSet().inBounds(); getMasterSet().next())
            if (raMaster.getSelectionTracker().isSelected(getMasterSet())) {
              DataSet ds = stdoki.getDataModule().getTempSet(
                  Condition.whereAllEqual(Util.mkey, getMasterSet()));
              raProcess.openScratchDataSet(ds);
              fillRequirements(reqs, ds);
              ds.close();
            }
          getMasterSet().goToRow(row);
          raMaster.getJpTableView().enableEvents(true);
        }
        
        String[] cols = {"GOD", "CART"};
        String god = val.getKnjigYear("robno");
        System.out.println(god);
        for (reqs.first(); reqs.inBounds(); reqs.next()) {
          raProcess.checkClosing();
          int cart = reqs.getInt("CART");
          DataSet st = Stanje.getDataModule().getTempSet(Condition.whereAllEqual(cols, 
              new Object[] {god, new Integer(cart)}));
          st.open();
          if (st.rowCount() > 0) {
            if (!lD.raLocate(st, "CSKL", reqs.getString("CSKL"))) st.first();
            else Aus.set(reqs, "KOL1", st, "KOL");
            reqs.setBigDecimal("NC", st.getBigDecimal("NC"));
          } else {
            DataSet art = Artikli.getDataModule().getTempSet(Condition.equal("CART", cart));
            art.open();
            reqs.setBigDecimal("NC", art.getBigDecimal("NC"));
          }
          Aus.sub(reqs, "KOL2", "KOL1", "KOL");
          reqs.setBigDecimal("INAB", util.multiValue(reqs.getBigDecimal("KOL"), 
              reqs.getBigDecimal("NC")));
        }
      }
    });
    
    if (!raProcess.isCompleted()) return;
    
    viewReq.setDataSet(reqs);
    viewReq.setSums(new String[] {"INAB"});
    viewReq.setSaveName("Pregled-razd");
    viewReq.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    viewReq.setTitle("Prikaz ukupne potrošnje materijala  za oznaèena razduženja");
    viewReq.setVisibleCols(new int[] {0, Aut.getAut().getCARTdependable(1, 2, 3), 4, 5, 6, 7, 8});
    viewReq.show();
  }
  
  void fillRequirements(StorageDataSet store, DataSet ds) {
    for (ds.first(); ds.inBounds(); ds.next()) {
      DataSet exp = Aut.getAut().expandArt(ds, true);
      if (exp == null) addToExpanded(store, ds, ds.getString("CSKLART"));
      else 
        for (exp.first(); exp.inBounds(); exp.next())
          addToExpanded(store, exp, ds.getString("CSKLART"));
    }
  }
  
  void addToExpanded(StorageDataSet expanded, DataSet art, String cskl) {
    if (!lD.raLocate(expanded, "CART", Integer.toString(art.getInt("CART")))) {
      expanded.last();
      expanded.insertRow(false);
      dM.copyColumns(art, expanded, reqc);
    } else expanded.setBigDecimal("KOL", 
        expanded.getBigDecimal("KOL").add(art.getBigDecimal("KOL")));
    expanded.setString("CSKL", cskl.startsWith("#") ? cskl.substring(1) : cskl);
    expanded.post();
  }
}