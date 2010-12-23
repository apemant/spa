/****license*****************************************************************
**   file: frmReportx.java
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
package hr.restart.sisfun;

import hr.restart.baza.Artnap;
import hr.restart.baza.Repxdata;
import hr.restart.baza.Repxhead;
import hr.restart.baza.dM;
import hr.restart.robno.Rbr;
import hr.restart.sisfun.Asql;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextArea;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmReportx extends raMasterDetail {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JPanel jpMasterMain;
  JPanel jpDetailMain = new JPanel();
  JPanel jpDetail;
  
  JraTextField jraCREP = new JraTextField();
  JraTextField jraNAZREP = new JraTextField();
  
  raComboBox rcbTip = new raComboBox();
  
  JraButton jbSelApp = new JraButton();
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrApp = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  
  JraTextField jraOpis = new JraTextField();
//  JraTextField jraRbr = new JraTextField();
  JraTextField jraCell = new JraTextField() {
    public void valueChanged() {
      fillXY();
    }
  };
  JraTextField jraRed = new JraTextField();
  JraTextField jraKol = new JraTextField();
  raComboBox rcbTipdat = new raComboBox();
  JEditorPane data = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };
  JraScrollPane vp = new JraScrollPane();
  
  public frmReportx() {
    try {
      this.setMasterDeleteMode(DELDETAIL);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'N') {
      rcc.setLabelLaF(jraCREP, true);
    } else if (mode == 'I') {
      rcc.setLabelLaF(jraCREP, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jraCREP.requestFocusLater();
    } else if (mode == 'I') {
      jraNAZREP.requestFocusLater();
    }
  }
  
  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jraCREP) || vl.isEmpty(jraNAZREP)) return false;
    if (mode == 'N' && vl.notUnique(jraCREP)) return false;
    return true;
  }
  
  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      data.setText("");
      jraCell.setText("");
      rcbTipdat.this_itemStateChanged();
    }
    if (mode == 'N' || mode == 'I') jraOpis.requestFocusLater();
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jraOpis) || vl.isEmpty(jraKol)
        || vl.isEmpty(jraRed)) return false;
    getDetailSet().setString("DATA", data.getText());
    return true;
  }
  
  public boolean doBeforeSaveDetail(char mode) {
    if (mode == 'N') {
      DataSet ds = Aus.q("SELECT MAX(rbr) as mrbr FROM repxdata " +
      		"WHERE crep=" + getDetailSet().getInt("CREP"));
      if (ds.isEmpty()) getDetailSet().setInt("RBR", 1); 
      else getDetailSet().setInt("RBR", ds.getInt("MRBR") + 1);
    }
    return true;
  }

  
  void fillXY() {
    if (jraCell.isEmpty()) return;
    
    String def = jraCell.getText();
    boolean alpha = true;
    int rbeg = 0;
    for (int i = 0; i < def.length(); i++) {
      char c = def.charAt(i);
      if ((alpha && (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')) ||
          (i > 0 && c >= '0' && c <= '9')) {
        if (c >= '0' && c <= '9' && alpha) {
          alpha = false;
          rbeg = i; 
        }
      } else return;
    }
    if (alpha) return;
    int x = 0;
    for (int i = 0; i < rbeg; i++)
      x = x * 26 + Character.toUpperCase(def.charAt(i)) - 'A' + 1;
    getDetailSet().setInt("KOL", x);
    getDetailSet().setInt("RED", Integer.parseInt(def.substring(rbeg)));
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        data.requestFocus();
      }
    });
  }
  
  public void detailSet_navigated(NavigationEvent e) {
    data.setText(getDetailSet().getString("DATA"));
    if (!getDetailSet().isNull("KOL") && 
        !getDetailSet().isNull("RED")) {
      int kol = getDetailSet().getInt("KOL");
      String ks = "";
      while (kol > 0) {
        ks = (char) ('A' + (kol % 26) - 1) + ks;
        kol = kol / 26;
      }
      jraCell.setText(ks + getDetailSet().getInt("RED"));
    }
  }
  
  /*public void masterSet_navigated(NavigationEvent e) {
  }*/
  
  public void SetPanels(JPanel master, JPanel detail, boolean detailBorder) {
    jpDetail = detail;
    if (detailBorder)
      jpDetail.setBorder(BorderFactory.createEtchedBorder());
    jpDetailMain.setLayout(new BorderLayout());
    jpDetailMain.add(jpDetail, BorderLayout.CENTER);
    this.setJPanelDetail(jpDetailMain);

    jpMasterMain = master;
    this.setJPanelMaster(jpMasterMain);
  }
  
  private void jbInit() throws Exception {

    this.setMasterSet(Repxhead.getDataModule().copyDataSet());
    this.setNaslovMaster("XLS izvještaji");
    this.setVisibleColsMaster(new int[] {0,1,2,3});
    this.setMasterKey(new String[] {"CREP"});

    this.setDetailSet(Repxdata.getDataModule().getTempSet("1=0"));
    this.setNaslovDetail("Definicija izvještaja");
    this.setVisibleColsDetail(new int[] {1,2,5,6});
    this.setDetailKey(new String[] {"CREP", "RBR"});
    
    JPanel mast = new JPanel(new XYLayout(570, 120));
    
    rcbTip.setRaItems(new String[][] {
        {"Glavna knjiga", "G"}
    }
    );
    rcbTip.setRaColumn("TIP");
    rcbTip.setRaDataSet(getMasterSet());
    
    jraCREP.setColumnName("CREP");
    jraCREP.setDataSet(getMasterSet());
    jraNAZREP.setColumnName("NAZREP");
    jraNAZREP.setDataSet(getMasterSet());
    
    jlrApp.setColumnName("APP");
    jlrApp.setDataSet(getMasterSet());
    jlrApp.setColNames(new String[] {"OPIS"});
    jlrApp.setTextFields(new JTextComponent[] {jlrOpis});
    jlrApp.setVisCols(new int[] {0, 1});
    jlrApp.setSearchMode(0);
    jlrApp.setRaDataSet(dm.getAplikacija());
    jlrApp.setNavButton(jbSelApp);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrApp);
    jlrOpis.setSearchMode(1);
    
    
    mast.add(new JLabel("Izvještaj"), new XYConstraints(15, 30, -1, -1));
    mast.add(new JLabel("Šifra"), new XYConstraints(150, 12, 75, -1));
    mast.add(new JLabel("Naziv"), new XYConstraints(230, 12, -1, -1));
    mast.add(jraCREP, new XYConstraints(150, 30, 75, -1));
    mast.add(jraNAZREP, new XYConstraints(230, 30, 300, -1));
    mast.add(new JLabel("Aplikacija"), new XYConstraints(15, 55, -1, -1));
    mast.add(jlrApp, new XYConstraints(150, 55, 75, -1));
    mast.add(jlrOpis, new XYConstraints(230, 55, 300, -1));
    mast.add(jbSelApp, new XYConstraints(535, 55, 21, 21));
    mast.add(new JLabel("Vrsta"), new XYConstraints(15, 80, -1, -1));
    mast.add(rcbTip, new XYConstraints(150, 80, 205, -1));
    
    JPanel det = new JPanel(new XYLayout(565, 300));
    
    jraOpis.setColumnName("OPIS");
    jraOpis.setDataSet(getDetailSet());
    jraKol.setColumnName("KOL");
    jraKol.setDataSet(getDetailSet());
    jraRed.setColumnName("RED");
    jraRed.setDataSet(getDetailSet());
    
    rcbTipdat.setRaItems(new String[][] {
        {"Tekstualni", "S"},
        {"Cjelobrojni", "I"},
        {"Decimalni, 2 decimale", "2"},
        {"Decimalni, 3 decimale", "3"},
        {"Datumski", "D"}
    });
    rcbTipdat.setRaColumn("TIP");
    rcbTipdat.setRaDataSet(getDetailSet());
    
    data.setFont(new JTextArea().getFont());

    vp.setViewportView(data);
    
    det.add(new JLabel("Opis stavke"), new XYConstraints(15, 20, -1, -1));
    det.add(jraOpis, new XYConstraints(150, 20, 400, -1));
    det.add(new JLabel("Pozicija"), new XYConstraints(15, 45, -1, -1));
    det.add(jraCell, new XYConstraints(150, 45, 100, -1));
    det.add(new JLabel("(X,Y)"), new XYConstraints(340, 45, -1, -1));
    det.add(jraKol, new XYConstraints(395, 45, 75, -1));
    det.add(jraRed, new XYConstraints(475, 45, 75, -1));
    det.add(new JLabel("Tip podatka"), new XYConstraints(15, 70, -1, -1));
    det.add(rcbTipdat, new XYConstraints(150, 70, 205, -1));
    det.add(new JLabel("Definicija"), new XYConstraints(15, 95, -1, -1));
    det.add(vp, new XYConstraints(150, 95, 400, 185));
    
    SetPanels(mast, det, false);
  }
}
