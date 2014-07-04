/****license*****************************************************************
**   file: frmKlijenti.java
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
package hr.restart.crm;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.StorageDataSet;

import hr.restart.baza.Klijenti;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;

public class frmKlijenti extends raMatPodaci {
  
  Valid vl = Valid.getValid();
  
  jpKlijent jp;
  boolean forceSave;
  boolean checkOib;
  frmTableDataView sims = new frmTableDataView(false, false, true) {
    protected void doubleClick(hr.restart.util.raJPTableView jp2) {
      cancelAndNavigateTo(jp2.getStorageDataSet().getInt("CKLIJENT"));
    };
    protected void OKPress() {
      forceSave = true;
      getOKpanel().jBOK_actionPerformed();
    }
  };

  public frmKlijenti() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    this.setRaQueryDataSet(Klijenti.getDataModule().getFilteredDataSet(""));
    jp = new jpKlijent(this);
    jp.BindComponents(getRaQueryDataSet());
    this.setRaDetailPanel(jp);
    this.setVisibleCols(new int[] {0,1,2,10});
    sims.jp.getNavBar().addOption(new raNavAction("Prikaži", raImages.IMGSTAV, KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        cancelAndNavigateTo(sims.jp.getStorageDataSet().getInt("CKLIJENT"));
      }
    });
    sims.setTitle("Konflikti kod dodavanja");
    sims.setSaveName("Klijenti-konflikti");
    
    getJpTableView().addTableModifier(new raTableColumnModifier("SID", new String[] {"NAZIV"}, dM.getDataModule().getKlijentStat()));
    getJpTableView().addTableModifier(new raTableColumnModifier("CSEG", new String[] {"NAZIV"}, dM.getDataModule().getSegmentacija()));
    
    installSelectionTracker("CKLIJENT");
    
    disableScrollbars();
    raDataIntegrity.installFor(this);
    raKlijentNames.getInstance();
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      if (jp.rcbStatus.getSelectedIndex() < 0)
        jp.rcbStatus.setSelectedIndex(0);
      jp.rcbStatus.this_itemStateChanged();
      if (jp.rcbSegment.getSelectedIndex() < 0)
        jp.rcbSegment.setSelectedIndex(0);
      jp.rcbSegment.this_itemStateChanged();
    }
    if (mode != 'B') jp.SetFokus(mode);
  }
  
  int navigate = -1;
  public void AfterCancel() {
    if (sims.isShowing()) sims.hide();
    jp.setColor();
    jp.updateList(true);
    jp.lazyPopulateKO(true);
    if (jp.kosobe.isShowing()) jp.displayKO();
    
    if (navigate >= 0) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          lookupData.getlookupData().raLocate(getRaQueryDataSet(), "CKLIJENT", Integer.toString(navigate));
          navigate = -1;
        }
      });
    }
  }
  
  /*public void jBOK_action() {
    if (getMode() != 'B' && jp.isEditing()) jp.saveOsoba();
    else {
      jp.cancelEdit();
      super.jBOK_action();
    }
  }
  
  public void jPrekid_action() {
    if (getMode() != 'B' && jp.isEditing()) jp.cancelEdit();
    else {
      jp.cancelEdit();
      super.jPrekid_action();
    }
  }*/
  
  public void switchPanel(boolean prvi,boolean drugi) {
    super.switchPanel(prvi, drugi);
    jp.fixButtons();
  }
  
  
  /*public boolean ValDPEscape(char mode) {
    if (mode != 'B' && jp.isEditing()) {
      jp.cancelEdit();
      return false;
    }
    jp.cancelEdit();
    return true;
  }*/
  
  public void beforeShow() {
    checkOib = frmParam.getParam("crm", "provjeriOib", "D",
                "Provjeriti je li upisan OIB za klijenta u crm (D,N)?").equalsIgnoreCase("D");
  }
  
  public void raQueryDataSet_navigated(NavigationEvent e) {
    jp.setColor();
    jp.lazyPopulateKO(false);
    if (jp.kosobe.isShowing() && getMode() == 'B') jp.displayKO();
  }
  
  void cancelAndNavigateTo(int cklijent) {
    if (sims.isShowing()) sims.hide();
    
    navigate = cklijent;
    if (getMode() != 'B')
      getOKpanel().jPrekid_actionPerformed();
    else AfterCancel();    
  }
  
  public boolean doBeforeSave(char mode) {
    if (mode == 'N') {
      getRaQueryDataSet().setInt("CKLIJENT", Valid.getValid().findSeqInt("CRM-klijenti"));
    }
    return true;
  }
  
  public boolean doWithSave(char mode) {
    jp.saveChanges(mode);
    if (mode == 'B') {
      try {
        raTransaction.runSQL("DELETE FROM Kontosobe WHERE cklijent=" + del);
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }
  
  int del;
  public boolean DeleteCheck() {
    del = getRaQueryDataSet().getInt("CKLIJENT");
    
    Valid.getValid().execSQL("SELECT COUNT(*) FROM kontosobe WHERE cklijent=" + del);
    Valid.getValid().RezSet.open();
    int numO = Valid.getValid().getSetCount(Valid.getValid().RezSet, 0);
    Valid.getValid().execSQL("SELECT COUNT(*) FROM kontakti WHERE cklijent=" + del);
    Valid.getValid().RezSet.open();
    int numK = Valid.getValid().getSetCount(Valid.getValid().RezSet, 0);
    if (numO + numK == 0) return true;
    
    JOptionPane.showMessageDialog(this.getWindow(), "Potrebno je najprije obrisati kontakte i kontakt osobe klijenta.",
        "Brisanje nedopušteno", JOptionPane.ERROR_MESSAGE);
    
    return true;
  }
  
  public void AfterDelete() {
    raKlijentNames.getInstance().removeRow(del);
  }
  
  public void AfterSave(char mode) {
    raKlijentNames.getInstance().addRow(getRaQueryDataSet());
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jp.jraNAZIV))
      return false;
    if (checkOib && !forceSave && jp.jraOIB.isEmpty()) {
      if (JOptionPane.showConfirmDialog(this, "OIB nije upisan. Spremiti ipak?", 
          "Potvrda", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return false;
    }
    
    if (sims.isShowing()) sims.hide();
    if (forceSave) {
      forceSave = false;
      return true;
    }
    
    if (mode == 'N') {
      raKlijentNames.getInstance().checkChanges();
      DataSet ret = raKlijentNames.getInstance().findSimilar(getRaQueryDataSet());
      if (ret != null && ret.rowCount() > 0) {
        sims.setDataSet((StorageDataSet) ret);
        sims.show();
        sims.resizeLater();
        return false;
      }
    }
   
    return true;
  }
}