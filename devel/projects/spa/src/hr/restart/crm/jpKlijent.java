/****license*****************************************************************
**   file: jpKlijent.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Kontosobe;
import hr.restart.baza.dM;
import hr.restart.swing.ActionExecutor;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.SharedFlag;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpKlijent extends JPanel {
  
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JraTextField jraNAZIV = new JraTextField();
  JraTextField jraADR = new JraTextField();
  
  JlrNavField jlrPBR = new JlrNavField();
  JlrNavField jlrMJ = new JlrNavField();
  JlrNavField jlrCMJ = new JlrNavField();
  JlrNavField jlrCZUP = new JlrNavField();
  JraTextField jraTEL = new JraTextField();
  JraTextField jraTELFAX = new JraTextField();
  JraTextField jraEMADR = new JraTextField();
  JraTextField jraWEBADR = new JraTextField();
  JraTextField jraMB = new JraTextField();
  JraTextField jraOIB = new JraTextField();
  
  JraTextField jraOSOBA = new JraTextField() {
    public boolean maskCheck() {
      boolean ret = super.maskCheck();
      updateList(false);
      return ret;
    }
  };
  JraTextField jraULOGA = new JraTextField() {
    public boolean maskCheck() {
      boolean ret = super.maskCheck();
      updateList(false);
      return ret;
    }
  };
  JraTextField jraOADR = new JraTextField();
  JraTextField jraOTEL = new JraTextField();
  JraTextField jraOMOB = new JraTextField();
  JraTextField jraOEMADR = new JraTextField();
  
  JlrNavField jlrOPBR = new JlrNavField();
  JlrNavField jlrOMJ = new JlrNavField();
  JlrNavField jlrOCMJ = new JlrNavField();
  JlrNavField jlrOCZUP = new JlrNavField();
  
  
  JLabel dispCol = new JLabel();
  Color defColor = dispCol.getBackground();
  raComboBox rcbStatus = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      setColor();
    }
  };
  raComboBox rcbSegment = new raComboBox();
  JraButton jbGetMj = new JraButton();
  JraButton jbOGetMj = new JraButton(); 
  
  
  
  JPanel podaci = new JPanel();
  JPanel kosobe = new JPanel();
  
  JTabbedPane tabs = new JTabbedPane();
  JList kos = new JList() {
    public void setEnabled(boolean enabled) {
      super.setEnabled(true);
    };
  };
  DefaultListModel model = new DefaultListModel();
  
  JraButton btnNovi = new JraButton();
  JraButton btnObrisi = new JraButton();
  
  SharedFlag ticket = new SharedFlag();
  
  ActionExecutor execAdd = new ActionExecutor(ticket) {
      public void run() {
        tryEdit();
        addOsoba();
      }
  };
  
  ActionExecutor execDel = new ActionExecutor(ticket) {
      public void run() {
        tryEdit();
        delOsoba();
      }
  };
  
  QueryDataSet dsko;
  raMatPodaci frm;
  
  public jpKlijent(raMatPodaci owner) {
    this.frm = owner;
    
    dsko = Kontosobe.getDataModule().getTempSet("1=0");
    dsko.open();
    
    podaci.setLayout(new XYLayout(750, 185));
    jraNAZIV.setColumnName("NAZIV");
    jraADR.setColumnName("ADR");
    jraMB.setColumnName("MB");
    jraOIB.setColumnName("OIB");
    jraTEL.setColumnName("TEL");
    jraTELFAX.setColumnName("TELFAX");
    jraEMADR.setColumnName("EMADR");
    jraWEBADR.setColumnName("WEBADR");
    
    dispCol.setOpaque(true);
    
    jlrPBR.setSearchMode(-1);
    jlrPBR.setColumnName("PBR");
    
    jlrPBR.setColNames(new String[] {"CMJESTA", "NAZMJESTA", "CZUP"});
    jlrPBR.setTextFields(new javax.swing.text.JTextComponent[] {jlrCMJ, jlrMJ, jlrCZUP});
    jlrPBR.setVisCols(new int[] {0, 1, 2});
    jlrPBR.setRaDataSet(dM.getDataModule().getMjesta());
    jlrPBR.setNavButton(jbGetMj);
    jlrPBR.setFocusLostOnShow(false);
    jlrPBR.setAfterLookUpOnClear(false);
    jlrPBR.setSearchMode(1);

    jlrMJ.setSearchMode(-1);
    jlrMJ.setColumnName("MJ");
    jlrMJ.setNavProperties(jlrPBR);
    jlrMJ.setNavColumnName("NAZMJESTA");
    
    jlrMJ.setFocusLostOnShow(false);
    jlrMJ.setAfterLookUpOnClear(false);
    jlrMJ.setSearchMode(1);

    jlrCMJ.setSearchMode(-1);
    jlrCMJ.setColumnName("CMJESTA");
    jlrCMJ.setNavProperties(jlrPBR);
    jlrCMJ.setVisible(false);
    jlrCMJ.setEnabled(false);
    jlrCMJ.setFocusLostOnShow(false);
    jlrCMJ.setAfterLookUpOnClear(false);

    jlrCZUP.setSearchMode(-1);
    jlrCZUP.setColumnName("CZUP");
    jlrCZUP.setNavProperties(jlrPBR);
    jlrCZUP.setVisible(false);
    jlrCZUP.setEnabled(false);
    jlrCZUP.setFocusLostOnShow(false);
    jlrCZUP.setAfterLookUpOnClear(false);
    
    rcbStatus.setRaColumn("SID");
    rcbStatus.setRaItems(dM.getDataModule().getKlijentStat(), "SID", "NAZIV");
    
    rcbSegment.setRaColumn("CSEG");
    rcbSegment.setRaItems(dM.getDataModule().getSegmentacija(), "CSEG", "NAZIV");
        
    podaci.add(new JLabel("Naziv"), new XYConstraints(15, 20, -1, -1));
    podaci.add(jraNAZIV, new XYConstraints(100, 20, 320, -1));
    podaci.add(new JLabel("Adresa"), new XYConstraints(15, 45, -1, -1));
    podaci.add(jraADR, new XYConstraints(100, 45, 320, -1));
    
    podaci.add(new JLabel("Mjesto"), new XYConstraints(15, 70, -1, -1));
    podaci.add(jlrPBR,   new XYConstraints(100, 70, 75, -1));
    podaci.add(jlrMJ,   new XYConstraints(180, 70, 200, -1));    
    podaci.add(jbGetMj,   new XYConstraints(385, 70, 21, 21));
    
    podaci.add(new JLabel("OIB"), new XYConstraints(15, 95, -1, -1));
    podaci.add(jraOIB, new XYConstraints(100, 95, 225, -1));
    podaci.add(new JLabel("Telefon"), new XYConstraints(15, 120, -1, -1));
    podaci.add(jraTEL, new XYConstraints(100, 120, 225, -1));
    podaci.add(new JLabel("E-mail"), new XYConstraints(15, 145, -1, -1));
    podaci.add(jraEMADR, new XYConstraints(100, 145, 225, -1));
    
    podaci.add(dispCol, new XYConstraints(555, 20, 180, 21));
    podaci.add(new JLabel("Status"), new XYConstraints(445, 45, -1, -1));
    podaci.add(rcbStatus, new XYConstraints(555, 45, 180, 21));
    podaci.add(new JLabel("Segmentacija"), new XYConstraints(445, 70, -1, -1));
    podaci.add(rcbSegment, new XYConstraints(555, 70, 180, 21));
    podaci.add(new JLabel("Matièni broj"), new XYConstraints(445, 95, -1, -1));
    podaci.add(jraMB, new XYConstraints(555, 95, 180, -1));
    podaci.add(new JLabel("Fax"), new XYConstraints(445, 120, -1, -1));
    podaci.add(jraTELFAX, new XYConstraints(555, 120, 180, -1));
    podaci.add(new JLabel("Web"), new XYConstraints(445, 145, -1, -1));
    podaci.add(jraWEBADR, new XYConstraints(555, 145, 180, -1));
    
    kos.setModel(model);
    kos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    kos.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting())
          fillData(kos.getSelectedIndex());
      }
    });
    kos.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          tryEdit();
          editOsoba();
        }
      }
    });
   
    kos.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value,
          int index, boolean isSelected, boolean cellHasFocus) {
        return super.getListCellRendererComponent(list, value, index, isSelected, false);
      }
    });
    
    JPanel lpan = new JPanel(new BorderLayout());
    JraScrollPane lsc = new JraScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    lsc.setViewportView(kos);
    lpan.add(lsc);
    lsc.setPreferredSize(new Dimension(325, 100));
    
    btnNovi.setText("Novi");
    btnNovi.setIcon(raImages.getImageIcon(raImages.IMGADD));
    btnNovi.setPreferredSize(new Dimension(90, 27));

    btnObrisi.setText("Obriši");
    btnObrisi.setIcon(raImages.getImageIcon(raImages.IMGDELETE));
    btnObrisi.setPreferredSize(new Dimension(90, 27));

    JPanel ubuttons = new JPanel(new GridLayout(1, 0));
    ubuttons.add(btnNovi);
    ubuttons.add(btnObrisi);
    
    lpan.add(ubuttons, BorderLayout.SOUTH);
    btnNovi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {    
        execAdd.invoke();
      }
    });
    btnObrisi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        execDel.invoke();
      }
    });

    JPanel split = new JPanel(new BorderLayout());
    split.add(lpan);
    split.add(kosobe, BorderLayout.EAST);
    /*split.setLeftComponent(lpan);
    split.setRightComponent(kosobe);
    split.setResizeWeight(1.00);*/
    
    kosobe.setLayout(new XYLayout(425, 185));
    
    jraOSOBA.setColumnName("IME");
    jraULOGA.setColumnName("ULOGA");
    jraOADR.setColumnName("ADR");
    jraOTEL.setColumnName("TEL");
    jraOMOB.setColumnName("MOB");
    jraOEMADR.setColumnName("EMADR");
    
    jlrOPBR.setSearchMode(-1);
    jlrOPBR.setColumnName("PBR");
    
    jlrOPBR.setColNames(new String[] {"CMJESTA", "NAZMJESTA", "CZUP"});
    jlrOPBR.setTextFields(new javax.swing.text.JTextComponent[] {jlrOCMJ, jlrOMJ, jlrOCZUP});
    jlrOPBR.setVisCols(new int[] {0, 1, 2});
    jlrOPBR.setRaDataSet(dM.getDataModule().getMjesta());
    jlrOPBR.setNavButton(jbOGetMj);
    jlrOPBR.setFocusLostOnShow(false);
    jlrOPBR.setAfterLookUpOnClear(false);
    jlrOPBR.setSearchMode(1);

    jlrOMJ.setSearchMode(-1);
    jlrOMJ.setColumnName("MJ");
    jlrOMJ.setNavProperties(jlrOPBR);
    jlrOMJ.setNavColumnName("NAZMJESTA");
    
    jlrOMJ.setFocusLostOnShow(false);
    jlrOMJ.setAfterLookUpOnClear(false);
    jlrOMJ.setSearchMode(1);

    jlrOCMJ.setSearchMode(-1);
    jlrOCMJ.setColumnName("CMJESTA");
    jlrOCMJ.setNavProperties(jlrOPBR);
    jlrOCMJ.setVisible(false);
    jlrOCMJ.setEnabled(false);
    jlrOCMJ.setFocusLostOnShow(false);
    jlrOCMJ.setAfterLookUpOnClear(false);

    jlrOCZUP.setSearchMode(-1);
    jlrOCZUP.setColumnName("CZUP");
    jlrOCZUP.setNavProperties(jlrOPBR);
    jlrOCZUP.setVisible(false);
    jlrOCZUP.setEnabled(false);
    jlrOCZUP.setFocusLostOnShow(false);
    jlrOCZUP.setAfterLookUpOnClear(false);

    kosobe.add(new JLabel("Osoba"), new XYConstraints(15, 20, -1, -1));
    kosobe.add(jraOSOBA, new XYConstraints(100, 20, 306, -1));
    kosobe.add(new JLabel("Uloga"), new XYConstraints(15, 45, -1, -1));
    kosobe.add(jraULOGA, new XYConstraints(100, 45, 306, -1));
    kosobe.add(new JLabel("Adresa"), new XYConstraints(15, 70, -1, -1));
    kosobe.add(jraOADR, new XYConstraints(100, 70, 306, -1));
    kosobe.add(new JLabel("Mjesto"), new XYConstraints(15, 95, -1, -1));
    kosobe.add(jlrOPBR,   new XYConstraints(100, 95, 75, -1));
    kosobe.add(jlrOMJ,   new XYConstraints(180, 95, 200, -1));    
    kosobe.add(jbOGetMj,   new XYConstraints(385, 95, 21, 21));
    kosobe.add(new JLabel("E-mail"), new XYConstraints(15, 120, -1, -1));
    kosobe.add(jraOEMADR, new XYConstraints(100, 120, 306, -1));
    kosobe.add(new JLabel("Tel / mob"), new XYConstraints(15, 145, -1, -1));
    kosobe.add(jraOTEL, new XYConstraints(100, 145, 151, -1));
    kosobe.add(jraOMOB, new XYConstraints(256, 145, 150, -1));
    
    if (frm instanceof frmKlijenti) {
      frm.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F2) {
        public void keyAction() {
          if (btnNovi.isShowing() && btnNovi.isEnabled())
            addOsoba();
        }
      });
      
      frm.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F3) {
        public void keyAction() {
          if (btnObrisi.isShowing() && btnObrisi.isEnabled())
            delOsoba();
        }
      });
    }
        
    tabs.addTab("Klijent", podaci);
    tabs.addTab("Kontakt osobe", split);
    
    tabs.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        if (kosobe.isShowing()) displayKO();
        else updateList(true);
      }
    });
    
    this.add(tabs);
  }
    
  public void BindComponents(DataSet ds) {
    jraNAZIV.setDataSet(ds);
    jraADR.setDataSet(ds);
    jraMB.setDataSet(ds);
    jraOIB.setDataSet(ds);
    jraTEL.setDataSet(ds);
    jraTELFAX.setDataSet(ds);
    jraEMADR.setDataSet(ds);
    jraWEBADR.setDataSet(ds);
    rcbStatus.setDataSet(ds);
    rcbSegment.setDataSet(ds);
    
    jlrPBR.setDataSet(ds);
    jlrMJ.setDataSet(ds);

    jraOSOBA.setDataSet(dsko);
    jraULOGA.setDataSet(dsko);
    jraOADR.setDataSet(dsko);
    jraOTEL.setDataSet(dsko);
    jraOMOB.setDataSet(dsko);
    jraOEMADR.setDataSet(dsko);
    jlrOPBR.setDataSet(dsko);
    jlrOMJ.setDataSet(dsko);    
  }
  
  public void SetFokus(char mode) {
    if (mode != 'B') {
      if (kosobe.isShowing()) displayKO();
      if (model.size() == 0)
        rcc.EnabDisabAll(kosobe, false);
      fixButtons();
      if (frm instanceof frmKlijenti || frm.getMode() == 'N') 
        jraNAZIV.requestFocus();
    }
  }
  
  void fixButtons() {
    rcc.setLabelLaF(btnNovi, true);
    rcc.setLabelLaF(btnObrisi, model.size() > 0);
  }
  
  boolean novi = false;
  void updateList(boolean force) {
    if (!novi) {
      for (int i = 0; i < model.size(); i++)
        if (((Kosoba) model.getElementAt(i)).cosobe == dsko.getInt("COSOBE"))
          model.setElementAt(new Kosoba(dsko), i);
      return;
    }
    if (dsko.getString("IME").trim().length() > 0) {
      novi = false;
      model.addElement(new Kosoba(dsko));
      kos.setSelectedIndex(model.size() - 1);
    } else if (force) {
      novi = false;
      dsko.deleteRow();
      if (model.size() > 0) kos.setSelectedIndex(0);
    }
    fixButtons();
  }
  
  void addOsoba() {
    if (frm.getMode() == 'B') return;
    kos.clearSelection();
    novi = true;
    dsko.insertRow(false);
    rcc.EnabDisabAll(kosobe, true);
    fixButtons();
    jraOSOBA.requestFocusLater();
  }
  
  void tryEdit() {
    if (frm.getMode() == 'B')
      frm.rnvUpdate_action();
  }
  
  void editOsoba() {
    if (frm.getMode() == 'B') return;
    fixButtons();
    jraOSOBA.requestFocusLater();
  }
  
  void delOsoba() {
    if (frm.getMode() == 'B') return;
    if (kos.getSelectedIndex() >= 0 && kos.getSelectedIndex() < model.size()) {
      dsko.deleteRow();
      model.remove(kos.getSelectedIndex());
      if (kos.getSelectedIndex() >= model.size() || kos.getSelectedIndex() < 0)
        kos.setSelectedIndex(model.size() - 1);
      fixButtons();
    }
  }
  
  int lastc = -1;
  boolean needUpdate;
  public void lazyPopulateKO(boolean force) {
    if (force) lastc = -1027;
    if (frm.getRaQueryDataSet() == null || frm.getRaQueryDataSet().rowCount() == 0) {
      if (lastc == -1) return;
      needUpdate = true;
      lastc = -1;
    } else if (frm.getRaQueryDataSet().getInt("CKLIJENT") != lastc) {
      lastc = frm.getRaQueryDataSet().getInt("CKLIJENT");
      needUpdate = true;
    }
  }
  
  public void displayKO() {
    if (needUpdate) {
      needUpdate = false;
      Kontosobe.getDataModule().setFilter(dsko, lastc == -1 ? Condition.nil : Condition.equal("CKLIJENT", lastc));
      dsko.open();
      Kosoba.minus = 0;
      model = new DefaultListModel();
      for (dsko.first(); dsko.inBounds(); dsko.next())
        model.addElement(new Kosoba(dsko));
      kos.setModel(model);
      if (model.size() > 0)
        kos.setSelectedIndex(0);
      fixButtons();
    }
  }
  
  public void saveChanges(char mode) {
    if (mode != 'B') {
      updateList(true);
      Valid.getValid().setSeqFilter("CRM-kontosobe");
      int last = (int) dM.getDataModule().getSeq().getDouble("BROJ");
      for (dsko.first(); dsko.inBounds(); dsko.next())
        if (dsko.getInt("COSOBE") < 0) {
          dsko.setInt("COSOBE", ++last);
          dsko.setInt("CKLIJENT", frm.getRaQueryDataSet().getInt("CKLIJENT"));
        }
      if (last != dM.getDataModule().getSeq().getDouble("BROJ")) {
        dM.getDataModule().getSeq().setDouble("BROJ", last);
        raTransaction.saveChanges(dM.getDataModule().getSeq());
      }
      raTransaction.saveChanges(dsko);
    }
  }
  
  void fillData(int idx) {
    if (idx < 0 || model.size() <= idx) return;
    updateList(true);
    Kosoba k = (Kosoba) model.get(idx);
    lookupData.getlookupData().raLocate(dsko, "COSOBE", Integer.toString(k.cosobe));
    frm.partialMemory(kosobe);
  }
  
  public void setColor() {
    DataSet ds = rcbStatus.getDataSet();
    if (ds.rowCount() == 0 || ds.getString("SID").equals(""))
      dispCol.setBackground(defColor);
    else dispCol.setBackground(raStatusColors.getInstance().getColor(rcbStatus.getDataValue())); 
  }
  
  private static class Kosoba {
    static int minus = 0;
    int cosobe;
    String ime;
    String uloga;

    
    public Kosoba(DataSet ds) {
      if (ds.isNull("COSOBE")) {
        ds.setInt("COSOBE", --minus);
        ds.post();
      }
      cosobe = ds.getInt("COSOBE");
      ime = ds.getString("IME");
      uloga = ds.getString("ULOGA");
    }
    
    public boolean equals(Object obj) {
      if (obj instanceof Kosoba)
        return ((Kosoba) obj).cosobe == cosobe;
      return false;
    }
    
    public int hashCode() {
      return cosobe;
    }
    
    public String toString() {
      if (uloga == null || uloga.length() == 0) return ime;
      return ime + ", " + uloga;
    }
  }
}
