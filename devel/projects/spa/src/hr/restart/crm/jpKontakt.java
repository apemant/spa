/****license*****************************************************************
**   file: jpKontakt.java
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Condition;
import hr.restart.baza.Kanali;
import hr.restart.baza.Kontakti;
import hr.restart.baza.Kontosobe;
import hr.restart.baza.dM;
import hr.restart.sisfun.raUser;
import hr.restart.swing.ActionExecutor;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextArea;
import hr.restart.swing.JraTextField;
import hr.restart.swing.SharedFlag;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raTransaction;


public class jpKontakt extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JPanel podaci = new JPanel();
  JTabbedPane tabs = new JTabbedPane();
  JList povijest = new JList() {
    public void setEnabled(boolean enabled) {
      super.setEnabled(true);
    };
    public boolean isFocusTraversable() {
      return false;
    };
  };
  DefaultListModel model = new DefaultListModel();
  raComboBox rcbKanal = new raComboBox();
  JraTextField jraKanal = new JraTextField();
  JraTextField jraNaslov = new JraTextField() {
    public boolean maskCheck() {
      boolean ret = super.maskCheck();
      updateList(false);
      return ret;
    }
  };
  JlrNavField jlrAgent = new JlrNavField();
  JlrNavField jlrNazagent = new JlrNavField();
  JraButton jbGetAgent = new JraButton();
  JraTextField jraTrajanje = new JraTextField();
  JlrNavField jlrKosoba = new JlrNavField();
  JraButton jbGetKosoba = new JraButton();
  JraTextArea opis = new JraTextArea() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };
  
  JraButton btnNovi = new JraButton();
  JraButton btnObrisi = new JraButton();
  
  SharedFlag ticket = new SharedFlag();
  
  ActionExecutor execAdd = new ActionExecutor(ticket) {
      public void run() {
        tryEdit(false);
        addKontakt();
      }
  };
  
  ActionExecutor execDel = new ActionExecutor(ticket) {
      public void run() {
        tryEdit(true);
        delKontakt();
      }
  };
  
  QueryDataSet dsp;
  frmKontakti frm;
  
  public jpKontakt(frmKontakti owner) {
    frm = owner;
    dsp = Kontakti.getDataModule().getTempSet("1=0");
    dsp.open();
    
    opis.setLineWrap(true);
    opis.setWrapStyleWord(true);
    
    podaci.setLayout(new XYLayout(750, 310));
    
    jraKanal.setColumnName("INFOKANAL");
    jraKanal.setDataSet(dsp);
    jraNaslov.setColumnName("NASLOV");
    jraNaslov.setDataSet(dsp);
    jraTrajanje.setColumnName("TRAJANJE");
    jraTrajanje.setDataSet(dsp);
    opis.setColumnName("OPIS");
    opis.setDataSet(dsp);
    
    jlrAgent.setSearchMode(-1);
    jlrAgent.setColumnName("CUSER");
    jlrAgent.setDataSet(dsp);
    jlrAgent.setColNames(new String[] {"NAZIV"});
    jlrAgent.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazagent});
    jlrAgent.setVisCols(new int[] {0, 1});
    jlrAgent.setRaDataSet(dM.getDataModule().getUseri());
    jlrAgent.setNavButton(jbGetAgent);
    jlrAgent.setSearchMode(0);
    
    jlrNazagent.setSearchMode(-1);
    jlrNazagent.setColumnName("NAZIV");
    jlrNazagent.setNavProperties(jlrAgent);
    jlrNazagent.setFocusLostOnShow(false);
    jlrNazagent.setAfterLookUpOnClear(false);
    jlrNazagent.setSearchMode(1);
    
    jlrKosoba.setColumnName("KOSOBA");
    jlrKosoba.setDataSet(dsp);
    jlrKosoba.setNavColumnName("IME");
    jlrKosoba.setVisCols(new int[] {1, 5});
    jlrKosoba.setRaDataSet(Kontosobe.getDataModule().getFilteredDataSet(Condition.nil));
    jlrKosoba.setNavButton(jbGetKosoba);
    jlrKosoba.setFocusLostOnShow(false);
    jlrKosoba.setAfterLookUpOnClear(false);
    jlrKosoba.setSearchMode(1);
    
    rcbKanal.setRaColumn("CKANAL");
    rcbKanal.setRaItems(Kanali.getDataModule().getTempSet(), "CKANAL", "NAZIV");
    rcbKanal.setRaDataSet(dsp);
    
    povijest.setModel(model);
    povijest.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    povijest.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!needUpdate && !e.getValueIsAdjusting())
          fillData(povijest.getSelectedIndex());
      }
    });
    povijest.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (!needUpdate && e.getClickCount() == 2) {
          tryEdit(true);
          editKontakt();
        }
      }
    });
   
    povijest.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value,
          int index, boolean isSelected, boolean cellHasFocus) {
        // TODO Auto-generated method stub
        return super.getListCellRendererComponent(list, value, index, isSelected,
            false);
      }
    });
    
    
    JraScrollPane lsc = new JraScrollPane();
    lsc.setViewportView(povijest);
    lsc.setPreferredSize(new Dimension(295, 100));
    
    btnNovi.setText("Novi");
    btnNovi.setIcon(raImages.getImageIcon(raImages.IMGADD));
    btnNovi.setPreferredSize(new Dimension(90, 27));

    btnObrisi.setText("Obriši");
    btnObrisi.setIcon(raImages.getImageIcon(raImages.IMGDELETE));
    btnObrisi.setPreferredSize(new Dimension(90, 27));
    
    btnNovi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {    
        if (!needUpdate) execAdd.invoke();
      }
    });
    btnObrisi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!needUpdate) execDel.invoke();
      }
    });

    JPanel ubuttons = new JPanel(new GridLayout(1, 0));
    ubuttons.add(btnNovi);
    ubuttons.add(btnObrisi);
    
    JPanel lpan = new JPanel(new BorderLayout());
    lpan.add(lsc);
    lpan.add(ubuttons, BorderLayout.SOUTH);
    
    JraScrollPane osc = new JraScrollPane();
    osc.setViewportView(opis);
    osc.setPreferredSize(new Dimension(410, 125));
    //opis.setPostOnFocusLost(true);
    
    podaci.add(new JLabel("Povijest"), new XYConstraints(450, 5, -1, -1));
    podaci.add(lpan, new XYConstraints(445, 25, 290, 270));
    podaci.add(new JLabel("Naslov"), new XYConstraints(15, 25, -1, -1));
    podaci.add(jraNaslov, new XYConstraints(100, 25, 320, -1));
    podaci.add(new JLabel("Kanal"), new XYConstraints(15, 50, -1, -1));
    podaci.add(rcbKanal, new XYConstraints(100, 50, 150, -1));
    podaci.add(jraKanal, new XYConstraints(255, 50, 165, -1));
    podaci.add(new JLabel("Osoba"), new XYConstraints(15, 75, -1, -1));
    podaci.add(jlrKosoba, new XYConstraints(100, 75, 250, -1));
    podaci.add(jbGetKosoba, new XYConstraints(355, 75, 21, 21));
    podaci.add(new JLabel("Trajanje"), new XYConstraints(15, 100, -1, -1));
    podaci.add(jraTrajanje, new XYConstraints(100, 100, 250, -1));
    podaci.add(new JLabel("Agent"), new XYConstraints(15, 125, -1, -1));
    podaci.add(jlrAgent, new XYConstraints(100, 125, 100, -1));
    podaci.add(jlrNazagent, new XYConstraints(205, 125, 175, -1));
    podaci.add(jbGetAgent, new XYConstraints(385, 125, 21, 21));
    podaci.add(new JLabel("Napomene"), new XYConstraints(15, 150, -1, -1));
    podaci.add(osc, new XYConstraints(15, 175, 405, 120));
    
    frm.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F2) {
      public void keyAction() {
        if (btnNovi.isShowing() && btnNovi.isEnabled())
          addKontakt();
      }
    });
    
    frm.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F3) {
      public void keyAction() {
        if (btnObrisi.isShowing() && btnObrisi.isEnabled())
          delKontakt();
      }
    });
    
    tabs.addTab("Podaci o kontaktu", podaci);
    this.add(tabs);
    
  }
  

  public void SetFokus(char mode) {
    if (mode == 'I') {
      if (!update) addKontakt();
      else jraNaslov.requestFocusLater();
      update = false;
    } else if (mode == 'N') {
      needUpdate = true;
      lastc = -1;
      lazyUpdate();
      addKontakt();
    }
  }
    
  void fixButtons() {
    rcc.setLabelLaF(btnNovi, !needUpdate);
    rcc.setLabelLaF(btnObrisi, !needUpdate && model.size() > 0);
  }
  
  boolean novi = false;
  public void updateList(boolean force) {
    if (!novi) {
      for (int i = 0; i < model.size(); i++)
        if (((Kontakt) model.getElementAt(i)).uid == dsp.getInt("UID"))
          model.setElementAt(new Kontakt(dsp), i);
      return;
    }
    if (dsp.getString("NASLOV").trim().length() > 0) {
      novi = false;
      model.add(0, new Kontakt(dsp));
      povijest.setSelectedIndex(0);
    } else if (force) {
      novi = false;
      dsp.deleteRow();
      if (model.size() > 0 && povijest.getSelectedIndex() < 0) povijest.setSelectedIndex(0);
    }
  }
  
  void fillData(int idx) {
    if (idx < 0 || model.size() <= idx) return;
    updateList(true);
    Kontakt k = (Kontakt) model.get(idx);
    lookupData.getlookupData().raLocate(dsp, "UID", Integer.toString(k.uid));
    jlrAgent.forceFocLost();
    frm.partialMemory(podaci);
  }
  
  boolean update = false;
  void tryEdit(boolean upd) {
    update = upd;
    if (frm.getMode() == 'B')
      frm.rnvUpdate_action();
  }
  
  void addKontakt() {
    if (frm.getMode() == 'B' || novi) return;
    povijest.clearSelection();
    novi = true;
    dsp.insertRow(false);
    dsp.setString("CUSER", raUser.getInstance().getUser());
    dsp.setTimestamp("DATUM", new Timestamp(System.currentTimeMillis()));
    jlrAgent.forceFocLost();
    jlrKosoba.setRaDataSet(Kontosobe.getDataModule().getFilteredDataSet(Condition.equal("CKLIJENT", frm.getRaQueryDataSet())));
    if (rcbKanal.getSelectedIndex() < 0)
      rcbKanal.setSelectedIndex(0);
    rcbKanal.this_itemStateChanged();
    fixButtons();
    if (frm.getMode() == 'I')
      jraNaslov.requestFocusLater();
  }
  
  void editKontakt() {
    if (frm.getMode() == 'B') return;
    fixButtons();
    jraNaslov.requestFocusLater();
  }
  
  void delKontakt() {
    if (frm.getMode() == 'B') return;

    if (povijest.getSelectedIndex() >= 0 && povijest.getSelectedIndex() < model.size()) {
      if (JOptionPane.showConfirmDialog(this, "Obrisati podatke za kontakt?", 
          "Brisanje", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
      dsp.deleteRow();
      model.remove(povijest.getSelectedIndex());
      if (povijest.getSelectedIndex() >= model.size() || povijest.getSelectedIndex() < 0)
        povijest.setSelectedIndex(0);
      fixButtons();
    }
  }
  
  int lastc = -1;
  boolean needUpdate;
  public void lazyPopulateKO(boolean force) {
    if (force) lastc = -1027;
    if (frm.getRaQueryDataSet() == null || frm.getRaQueryDataSet().rowCount() == 0) {
      if (lastc == -1) return;
      startUpdate(-1, 0);
    } else if (frm.getRaQueryDataSet().getInt("CKLIJENT") != lastc) {
      startUpdate(frm.getRaQueryDataSet().getInt("CKLIJENT"), 250);
    }
  }
  
  Timer updater = null;
  public synchronized void startUpdate(int cklijent, int delay) {
    lastc = cklijent;
    needUpdate = true;
    fixButtons();
    if (updater != null) updater.stop();
    updater = new Timer(delay, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lazyUpdate();
      }
    });
    updater.setRepeats(false);
    updater.start();
  }
  
  synchronized Timer getUpdater() {
    return updater;
  }
  
  void lazyUpdate() {
    if (needUpdate) {
      Kontakti.getDataModule().setFilter(dsp, lastc == -1 ? Condition.nil : 
        Condition.equal("CKLIJENT", lastc).and(Condition.equal("STATUS", frmKampanje.CLOSED)));
      dsp.open();
      DefaultListModel nmod = new DefaultListModel();
      for (dsp.first(); dsp.inBounds(); dsp.next())
        nmod.addElement(new Kontakt(dsp));
      povijest.setModel(model = nmod);
      needUpdate = false;
      if (dsp.rowCount() > 0) povijest.setSelectedIndex(0);
      else povijest.clearSelection();
      
      Kontakt.minus = 0;
      fixButtons();
    }
  }
  
  public void saveChanges(char mode) {
    if (mode != 'B') {
      updateList(true);
      Valid.getValid().setSeqFilter("CRM-kontakti");
      int last = (int) dM.getDataModule().getSeq().getDouble("BROJ");
      for (dsp.first(); dsp.inBounds(); dsp.next())
        if (dsp.getInt("UID") < 0) {
          dsp.setInt("UID", ++last);
          dsp.setInt("CKLIJENT", frm.getRaQueryDataSet().getInt("CKLIJENT"));
          dsp.setString("STATUS", frmKampanje.CLOSED);
        }
      if (last != dM.getDataModule().getSeq().getDouble("BROJ")) {
        dM.getDataModule().getSeq().setDouble("BROJ", last);
        raTransaction.saveChanges(dM.getDataModule().getSeq());
      }
      raTransaction.saveChanges(dsp);
      fillData(povijest.getSelectedIndex());
    }
  }
  
  static class Kontakt {
    static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy. 'u' HH:mm");
    
    static int minus = 0;
    int uid;
    String naslov;
    Timestamp datum;
    
    public Kontakt(DataSet ds) {
      if (ds.isNull("UID")) {
        ds.setInt("UID", --minus);
        ds.post();
      }
      uid = ds.getInt("UID");
      naslov = ds.getString("NASLOV");
      datum = new Timestamp(ds.getTimestamp("DATUM").getTime());
    }
    
    public boolean equals(Object obj) {
      if (obj instanceof Kontakt)
        return ((Kontakt) obj).uid == uid;
      return false;
    }
    
    public int hashCode() {
      return uid;
    }
    
    public String toString() {
      if (naslov == null || naslov.length() == 0) return sdf.format(datum);
      return sdf.format(datum) + ", " + naslov;
    }
  }
}
