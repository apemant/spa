/****license*****************************************************************
**   file: frmKontakti.java
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

import hr.restart.baza.Klijenti;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raKeyAction;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.StorageDataSet;

public class frmKontakti extends raMatPodaci {
  
  Valid vl = Valid.getValid();
  
  jpKlijent jpk;
  jpKontakt jp;
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

  public frmKontakti() {
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
    jpk = new jpKlijent(this);
    jpk.BindComponents(getRaQueryDataSet());
    jp = new jpKontakt(this);
    
    JPanel detail = new JPanel(new BorderLayout());
    detail.add(jpk, BorderLayout.NORTH);
    detail.add(jp);
    
    this.setRaDetailPanel(detail);
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
    
    addKeyAction(new raKeyAction(KeyEvent.VK_F8) {
      public void keyAction() {
        if (JraTextField.currentFocus == jpk.jraNAZIV)
          checkSims();
      }
    });
    
    /*JraTable2 tab = getJpTableView().getMpTable();
    
    tab.setDefaultRenderer(Object.class, tab.new dataSetTableCellRenderer() {
      
      Color status = null;
      Variant v = new Variant();
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
          int column) {
        if (((JraTable2) table).getRealColumnName(column).equals("NAZIV")) {
          ((JraTable2) table).getDataSet().getVariant("SID", row, v);
          status = jpk.getColor(v.getString());
        } else status = null;
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
            row, column);
      }
      
      public Dimension getPreferredSize() {
        if (status == null) return super.getPreferredSize();
        
        Dimension dim = super.getPreferredSize();
        return new Dimension(dim.width + 25, dim.height);
      }
      protected void paintComponent(Graphics g) {
        if (status != null) g.translate(25, 0);
        super.paintComponent(g);
        if (status != null) {
          g.translate(-25, 0);
          Color old = g.getColor();
          g.setColor(status);
          int h = getHeight();
          g.fillRect(4, 4, 10, h - 8);
          g.setColor(old);
        }
      }
    });*/

    disableScrollbars();
    raDataIntegrity.installFor(this);
    raKlijentNames.getInstance();
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      if (jpk.rcbStatus.getSelectedIndex() < 0)
        jpk.rcbStatus.setSelectedIndex(0);
      jpk.rcbStatus.this_itemStateChanged();
      if (jpk.rcbSegment.getSelectedIndex() < 0)
        jpk.rcbSegment.setSelectedIndex(0);
      jpk.rcbSegment.this_itemStateChanged();
      
      if (jp.rcbKanal.getSelectedIndex() < 0)
        jp.rcbKanal.setSelectedIndex(0);
      jp.rcbKanal.this_itemStateChanged();
    }
    if (mode != 'B') jpk.SetFokus(mode);
    if (mode != 'B') jp.SetFokus(mode);
  }
  
  int navigate = -1;
  public void AfterCancel() {
    if (sims.isShowing()) sims.hide();
    jpk.setColor();
    jpk.updateList(true);
    jpk.lazyPopulateKO(true);
    if (jpk.kosobe.isShowing()) jpk.displayKO();
    jp.updateList(true);
    jp.lazyPopulateKO(true);
    
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
    if (getMode() != 'B' && jp.isEditing()) jp.saveKontakt();
    else if (getMode() != 'B' && jpk.isEditing()) jpk.saveOsoba();
    else {
      jp.cancelEdit();
      jpk.cancelEdit();
      super.jBOK_action();
    }
  }
  
  public void jPrekid_action() {
    if (getMode() != 'B' && jp.isEditing()) jp.cancelEdit();
    else if (getMode() != 'B' && jpk.isEditing()) jpk.cancelEdit();
    else {
      jp.cancelEdit();
      jpk.cancelEdit();
      super.jPrekid_action();
    }
  }*/
  
  public void switchPanel(boolean prvi,boolean drugi) {
    super.switchPanel(prvi, drugi);
    jp.fixButtons();
    jpk.fixButtons();
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
    jpk.setColor();
    jpk.lazyPopulateKO(false);
    if (jpk.kosobe.isShowing() && getMode() == 'B') jpk.displayKO();
    if (getMode() == 'B') jp.lazyPopulateKO(false);
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
    jpk.saveChanges(mode);
    jp.saveChanges(mode);
    if (mode == 'B') {
      try {
        raTransaction.runSQL("DELETE FROM Kontosobe WHERE cklijent=" + del);
        raTransaction.runSQL("DELETE FROM Kontakti WHERE cklijent=" + del);
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
    
    return false;
  }
  
  public void AfterDelete() {
    raKlijentNames.getInstance().removeRow(del);
  }
  
  public void AfterSave(char mode) {
    raKlijentNames.getInstance().addRow(getRaQueryDataSet());
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jpk.jraNAZIV))
      return false;
    if (checkOib && !forceSave && jpk.jraOIB.isEmpty()) {
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
  
  void checkSims() {
    if (sims.isShowing()) sims.hide();
    jpk.jraNAZIV.maskCheck();
    raKlijentNames.getInstance().checkChanges();
    DataSet ret = raKlijentNames.getInstance().findSimilar(getRaQueryDataSet());
    if (ret != null && ret.rowCount() > 0) {
      sims.setDataSet((StorageDataSet) ret);
      sims.show();
      sims.resizeLater();
    }
  }
  
  /*public boolean ValDPEscape(char mode) {
    if (mode != 'B' && jp.isChanged()) {
      jpk.tabs.setSelectedIndex(1);
      if (JOptionPane.showConfirmDialog(jpk.kosobe, "Promjene na podacima o kontaktu æe biti izgubljene. Svejedno prekinuti?", 
          "Podaci o kontaktu promjenjene", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
          return false;
    } else if (mode != 'B' && jpk.isChanged()) {
      jpk.tabs.setSelectedIndex(1);
      if (JOptionPane.showConfirmDialog(jpk.kosobe, "Promjene na kontakt osobama æe biti izgubljene. Svejedno prekinuti?", 
          "Kontakt osobe promjenjene", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
          return false;
    }
    return true;
  }*/
}