/****license*****************************************************************
**   file: frmEracun.java
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

import hr.restart.sisfun.EracException;
import hr.restart.sisfun.EracunUtils;
import hr.restart.swing.AktivColorModifier;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.swing.raInputDialog;
import hr.restart.util.Aus;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.raNavAction;
import hr.restart.util.startFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmEracun extends raFrame {
    
  raJPTableView jpTableView = new raJPTableView() {

    public void mpTable_killFocus(java.util.EventObject e) {

      //jTabPane.requestFocus();

    }

    public void mpTable_doubleClicked() {
      receive();
    }
    
    public void navBar_afterRefresh() {
      refresh();
      System.out.println("Refresh");
    }
    
    /*public boolean mpTable_allowRowChange(int oldrow, int newrow) {
      return allowRowChange(oldrow, newrow);
    }
    
    public void mpTable_rowChanged_ext(int oldrow, int newrow, boolean toggle, boolean extend) {
      raMatPodaci.this.mpTable_rowChanged(oldrow, newrow, toggle, extend);
    };

    public boolean validateSelection(ReadRow r) {
      return raMatPodaci.this.validateSelection(r);
    }*/
  };
  
  raNavAction rnvGet = new raNavAction("Preuzmi",
      raImages.IMGIMPORT, java.awt.event.KeyEvent.VK_F2) {
    public void actionPerformed(ActionEvent e) {
      receive();
    }
  };
  
  raNavAction rnvCancel = new raNavAction("Poništi",
      raImages.IMGSTOP, java.awt.event.KeyEvent.VK_F2) {
    public void actionPerformed(ActionEvent e) {
      cancel();
    }
  };
  
  raNavAction rnvSelect = new raNavAction("Odabir perioda",
      raImages.IMGZOOM, java.awt.event.KeyEvent.VK_F12) {
    public void actionPerformed(ActionEvent e) {
      refilter();
    }
  };
  
  raNavAction rnvIzlaz = new raNavAction("Zatvori",
      raImages.IMGX, java.awt.event.KeyEvent.VK_ESCAPE) {
    public void actionPerformed(ActionEvent e) {
      frmEracun.this.hide();
    }
  };
  
  JPanel fPan = new JPanel(new XYLayout(500, 90));
  JraComboBox inout = new JraComboBox(new String[] {"Primljeni dokumenti", "Poslani dokumenti"});
  JraCheckBox unread = new JraCheckBox("Samo novi");
  JraTextField jraDfrom = new JraTextField();
  JraTextField jraDto = new JraTextField();
  StorageDataSet datData;
  
  boolean inbox = true;
  Timestamp dfrom = null;
  Timestamp dto = null;
  
  public frmEracun() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    getContentPane().add(jpTableView);
    jpTableView.getNavBar().addOption(rnvGet);
    jpTableView.getNavBar().addOption(rnvCancel);
    jpTableView.getNavBar().addOption(rnvSelect);
    jpTableView.getNavBar().addOption(rnvIzlaz);
    jpTableView.getColumnsBean().setSaveSettings(true);
    jpTableView.getColumnsBean().setSaveName(getClass().getName());
    jpTableView.initKeyListener(this);
    
    rnvCancel.setEnabled(!inbox);
    
    datData = Aus.createSet("@DATFROM @DATTO");
    jraDfrom.setColumnName("DATFROM");
    jraDfrom.setDataSet(datData);
    jraDto.setColumnName("DATTO");
    jraDto.setDataSet(datData);
    
    unread.setHorizontalAlignment(SwingConstants.TRAILING);
    unread.setHorizontalTextPosition(SwingConstants.LEADING);
    
    inout.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        unread.setVisible(inout.getSelectedIndex() == 0);
        raCommonClass.getraCommonClass().setLabelLaF(jraDfrom, inout.getSelectedIndex() == 1 || !unread.isSelected());
        raCommonClass.getraCommonClass().setLabelLaF(jraDto, inout.getSelectedIndex() == 1 || !unread.isSelected());
        if (inout.getSelectedIndex() == 1 && (datData.isNull("DATFROM") || datData.isNull("DATTO"))) {
          datData.setTimestamp("DATFROM", hr.restart.util.Util.getUtil().getFirstDayOfYear());
          datData.setTimestamp("DATTO", hr.restart.util.Util.getUtil().getLastDayOfMonth());
        }
      }
    });
    
    unread.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        raCommonClass.getraCommonClass().setLabelLaF(jraDfrom, !unread.isSelected());
        raCommonClass.getraCommonClass().setLabelLaF(jraDto, !unread.isSelected());
        if (!unread.isSelected() && (datData.isNull("DATFROM") || datData.isNull("DATTO"))) {
          datData.setTimestamp("DATFROM", hr.restart.util.Util.getUtil().getFirstDayOfYear());
          datData.setTimestamp("DATTO", hr.restart.util.Util.getUtil().getLastDayOfMonth());
        } else if (unread.isSelected()) {
          datData.setUnassignedNull("DATFROM");
          datData.setUnassignedNull("DATTO");
        }
      }
    });
    
    fPan.add(new JLabel("Vrsta"), new XYConstraints(15, 20, -1, -1));
    fPan.add(inout, new XYConstraints(150, 20, 205, -1));
    fPan.add(unread, new XYConstraints(360, 20, 125, -1));
    fPan.add(new JLabel("Datum (od - do)"), new XYConstraints(15, 50, -1, -1));
    fPan.add(jraDfrom, new XYConstraints(150, 50, 100, -1));
    fPan.add(jraDto, new XYConstraints(255, 50, 100, -1));
    
    jpTableView.addTableModifier(new AktivColorModifier("STATUS", "30", null, null));
    
    new raDateRange(jraDfrom, jraDto);
  }
  
  public void show() {
    try {
      refresh();
      jpTableView.getColumnsBean().eventInit();
      if (!isShowing()) pack();
      super.show();
    } catch (EracException e) {
      JOptionPane.showMessageDialog(startFrame.getStartFrame(), e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);

    }
  }
  
  void refilter() {
    inout.setSelectedIndex(inbox ? 0 : 1);
    unread.setSelected(dfrom == null && dto == null);
    if (dfrom == null) datData.setUnassignedNull("DATFROM"); 
    else datData.setTimestamp("DATFROM", dfrom);
    if (dto == null) datData.setUnassignedNull("DATTO"); 
    else datData.setTimestamp("DATTO", dto);
    
    if (new raInputDialog().show(getContentPane(), fPan, "Pregled e-Raèuna")) {
      inbox = inout.getSelectedIndex() == 0;
      dfrom = datData.isNull("DATFROM") ? null : datData.getTimestamp("DATFROM");
      dto = datData.isNull("DATTO") ? null : datData.getTimestamp("DATTO");
      rnvCancel.setEnabled(!inbox);
      refresh();
    }
  }
  
  void cancel() {
    try {
      StorageDataSet ds = jpTableView.getStorageDataSet();
      if (ds.rowCount() == 0) return;
      if (ds.getInt("STATUS") == 40 || ds.getInt("STATUS") == 45) {
        JOptionPane.showMessageDialog(isShowing() ? getWindow() : startFrame.getStartFrame(), 
            "Raèun je veæ " + (ds.getInt("STATUS") == 45 ? "poništen!" : "dostavljen!"), "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (ds.getInt("STATUS") != 30 && ds.getInt("STATUS") != 50) {
        JOptionPane.showMessageDialog(isShowing() ? getWindow() : startFrame.getStartFrame(), 
            "Raèun još nije prihvaæen!", "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (JOptionPane.showConfirmDialog(isShowing() ? getWindow() : startFrame.getStartFrame(),
          "Jeste li sigurni da želite opozvati dokument?", "Poništavanje dokumenta",
          JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
      EracunUtils.getInstance().cancelDocument(ds.getInt("EID"));
      refresh();
    } catch (EracException e) {
      JOptionPane.showMessageDialog(isShowing() ? getWindow() : startFrame.getStartFrame(), e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  void receive() {
    try {
      StorageDataSet ds = jpTableView.getStorageDataSet();
      if (ds.rowCount() == 0) return;
      EracunUtils.getInstance().getDocument(ds, !inbox);
    } catch (EracException e) {
      JOptionPane.showMessageDialog(isShowing() ? getWindow() : startFrame.getStartFrame(), e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  void refresh() {
    try {
      if (!inbox) jpTableView.setDataSet((StorageDataSet) EracunUtils.getInstance().checkOutbox(dfrom, dto));
      else jpTableView.setDataSet((StorageDataSet) EracunUtils.getInstance().checkInbox(dfrom != null || dto != null, dfrom, dto));
    } catch (EracException e) {
      JOptionPane.showMessageDialog(isShowing() ? getWindow() : startFrame.getStartFrame(), e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
}
