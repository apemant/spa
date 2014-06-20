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
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;

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
    jp = new jpKlijent();
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
    if (mode != 'B') jp.jraNAZIV.requestFocus();
  }
  
  int navigate = -1;
  public void AfterCancel() {
    if (sims.isShowing()) sims.hide();
    jp.setColor();
    
    if (navigate >= 0) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          lookupData.getlookupData().raLocate(getRaQueryDataSet(), "CKLIJENT", Integer.toString(navigate));
          navigate = -1;
        }
      });
    }
  }
  
  public void beforeShow() {
    checkOib = frmParam.getParam("crm", "provjeriOib", "D",
                "Provjeriti je li upisan OIB za klijenta u crm (D,N)?").equalsIgnoreCase("D");
  }
  
  public void raQueryDataSet_navigated(NavigationEvent e) {
    jp.setColor();
  }
  
  void cancelAndNavigateTo(int cklijent) {
    if (sims.isShowing()) sims.hide();
    
    navigate = cklijent;
    getOKpanel().jPrekid_actionPerformed();
    
  }
  
  public boolean doBeforeSave(char mode) {
    if (mode == 'N') {
      Valid.getValid().execSQL("SELECT MAX(cklijent) as cklijent FROM klijenti");
      Valid.getValid().RezSet.open();
      getRaQueryDataSet().setInt("CKLIJENT", Valid.getValid().RezSet.getInt(0) + 1);
    }
    return true;
  }
  
  int del;
  public boolean DeleteCheck() {
    del = getRaQueryDataSet().getInt("CKLIJENT");
    return true;;
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
    
    raKlijentNames.getInstance().checkChanges();
    DataSet ret = raKlijentNames.getInstance().findSimilar(getRaQueryDataSet());
    if (ret != null && ret.rowCount() > 0) {
      sims.setDataSet((StorageDataSet) ret);
      sims.show();
      sims.resizeLater();
      return false;
    }
   
    return true;
  }
}