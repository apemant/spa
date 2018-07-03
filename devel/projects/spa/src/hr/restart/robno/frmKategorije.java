/****license*****************************************************************
**   file: frmKategorije.java
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

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Condition;
import hr.restart.baza.Kategorije;
import hr.restart.baza.dM;
import hr.restart.sisfun.raDataIntegrity;
import hr.restart.swing.JrCheckBox;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.XYPanel;
import hr.restart.swing.raInputDialog;
import hr.restart.util.DataTree;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;


public class frmKategorije extends raMatPodaci {
  
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.baza.dM dm = dM.getDataModule();
  
  JPanel jp = new JPanel();
  XYLayout lay = new XYLayout(570, 190);
  JPanel jpKATPRIP = new JPanel();
  JraTextField jtfNAZKAT = new JraTextField();
  JrCheckBox jcbKATPRIP = new JrCheckBox();
  JraCheckBox jcbAKTIV = new JraCheckBox();
  JraTextField jtfCKAT = new JraTextField();
  JraTextField jtfBCKAT = new JraTextField();
  XYLayout xYLayout2 = new XYLayout(540, 45);
  JlrNavField jrfCKAT = new JlrNavField();
  JlrNavField jrfCKATPRIPNAZ = new JlrNavField();
  JraButton jbKATPRIP = new JraButton();
  
  raNavAction rnvChgPrip = new raNavAction("Promijeni pripadnost",
      raImages.IMGSENDMAIL, java.awt.event.KeyEvent.VK_F11) {
      public void actionPerformed(ActionEvent e) {
        chgPrip();
      }
  };
  
  QueryDataSet kats;

  public frmKategorije() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    kats = hr.restart.baza.Kategorije.getDataModule().getFilteredDataSet("");
    this.setRaDetailPanel(jp);
    this.setRaQueryDataSet(dm.getAllKategorije());
    this.setVisibleCols(new int[] {0,1,2});
    jp.setLayout(lay);
    jpKATPRIP.setLayout(xYLayout2);
    jpKATPRIP.setBorder(BorderFactory.createTitledBorder(
          BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134)), "Pripadnost kategoriji"));
    
    jcbKATPRIP.setText("Top kategorija");
    jcbAKTIV.setText("Aktivan");

    jtfNAZKAT.setColumnName("NAZKAT");
    jtfNAZKAT.setDataSet(getRaQueryDataSet());
    jtfCKAT.setColumnName("CKAT");
    jtfCKAT.setDataSet(getRaQueryDataSet());
    jtfBCKAT.setColumnName("BCKAT");
    jtfBCKAT.setDataSet(getRaQueryDataSet());
    jcbKATPRIP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbKATPRIP_actionPerformed(e);
      }
    });
    jcbAKTIV.setUnselectedDataValue("N");
    jcbAKTIV.setSelectedDataValue("D");
    jcbAKTIV.setColumnName("AKTIV");
    jcbAKTIV.setDataSet(getRaQueryDataSet());
    jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbAKTIV.setSelected(true);

    jrfCKAT.setColumnName("CKATPRIP");
    jrfCKAT.setNavColumnName("CKAT");
    jrfCKAT.setDataSet(getRaQueryDataSet());
    jrfCKAT.setColNames(new String[] {"NAZKAT"});
    jrfCKAT.setVisCols(new int[]{0,1,2});
    jrfCKAT.setTextFields(new javax.swing.text.JTextComponent[] {jrfCKATPRIPNAZ});
    jrfCKAT.setRaDataSet(kats);
    jrfCKAT.setNavButton(jbKATPRIP);
    
    jrfCKATPRIPNAZ.setColumnName("NAZKAT");
    jrfCKATPRIPNAZ.setSearchMode(1);
    jrfCKATPRIPNAZ.setNavProperties(jrfCKAT);
    
    jp.add(new JLabel("Kategorija"), new XYConstraints(15, 35, -1, -1));
    jp.add(jtfCKAT, new XYConstraints(150, 35, 100, -1));
    jp.add(jtfNAZKAT, new XYConstraints(260, 35, 280, -1));
    jp.add(new JLabel("Vanjska šifra"), new XYConstraints(15, 60, -1, -1));
    jp.add(jtfBCKAT, new XYConstraints(150, 60, 210, -1));
    jp.add(jpKATPRIP, new XYConstraints(15, 115, -1, -1));
    jpKATPRIP.add(new JLabel("Kategorija"), new XYConstraints(15, 5, -1, -1));
    jpKATPRIP.add(jrfCKATPRIPNAZ, new XYConstraints(240, 5, 245, -1));
    jpKATPRIP.add(jrfCKAT, new XYConstraints(130, 5, 100, -1));
    jpKATPRIP.add(jbKATPRIP, new XYConstraints(489, 5, 21, 21));

    jp.add(jcbAKTIV, new XYConstraints(440, 15, 100, -1));
    jp.add(new JLabel("Šifra"), new XYConstraints(150, 15, -1, -1));
    jp.add(new JLabel("Naziv"), new XYConstraints(260, 15, -1, -1));
    jp.add(jcbKATPRIP, new XYConstraints(15, 90, -1, -1));
    
    addOption(rnvChgPrip,4,true);

    raDataIntegrity.installFor(this);
    
    installSelectionTracker("CKAT");
  }

  public boolean Validacija(char mode) {
    if (mode == 'N') {
      if (hr.restart.util.Valid.getValid().notUnique(jtfCKAT)) return false;
    }
    if (hr.restart.util.Valid.getValid().isEmpty(jtfNAZKAT)) return false;
    if (mode == 'N') {
      if (getRaQueryDataSet().getString("CKATPRIP").trim().equals("")) {
        getRaQueryDataSet().setString("CKATPRIP",
            getRaQueryDataSet().getString("CKAT"));
      }
    } else {
      if (jcbKATPRIP.isSelected()) {
        getRaQueryDataSet().setString("CKATPRIP",
            getRaQueryDataSet().getString("CKAT"));
      }
    }
    if (DataTree.isCircular(getRaQueryDataSet(), dm.getKategorije(), "CKAT", "CKATPRIP")) {
      jrfCKAT.requestFocus();
      JOptionPane.showMessageDialog(jrfCKAT,
          "Pripadnost stvara beskonaènu petlju!", "Greška",
          javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {
      jrfCKAT.setText("");
      jrfCKATPRIPNAZ.setText("");
      rcc.setLabelLaF(jtfCKAT, true);
      jtfCKAT.requestFocus();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jtfCKAT, false);
      jtfNAZKAT.requestFocus();
    }
  }

  public boolean DeleteCheck() {
    if (Kategorije.getDataModule().getRowCount(
        Condition.equal("CKATPRIP", getRaQueryDataSet().getString("CKAT"))
            .and(Condition.diff("CKAT", getRaQueryDataSet()))) > 0) {
      JOptionPane.showMessageDialog(getWindow(),
          "Brisanje nije moguæe jer ova kategorija ima podkategorije!", "Greška",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public void EntryPoint(char mode) {
    jrfCKAT.setText(getRaQueryDataSet().getString("CKATPRIP"));
    if (mode == 'I') {
      if (getRaQueryDataSet().getString("CKATPRIP").trim()
          .equals(getRaQueryDataSet().getString("CKAT").trim())) {
        jcbKATPRIP.setSelected(true);
      } else {
        jcbKATPRIP.setSelected(false);
      }
    } else {
      jcbKATPRIP.setSelected(true);
    }
    jcbKATPRIP_actionPerformed(null);
  }

  void jcbKATPRIP_actionPerformed(ActionEvent e) {
    if (jcbKATPRIP.isSelected()) {
      rcc.EnabDisabAll(this.jpKATPRIP, false);
      jrfCKAT.setText(getRaQueryDataSet().getString("CKAT"));
      jrfCKATPRIPNAZ.setText(getRaQueryDataSet().getString("NAZKAT"));
    } else {
      rcc.EnabDisabAll(this.jpKATPRIP, true);
      jrfCKAT.requestFocus();
    }
  }

  void chgPrip() {
    if (getSelectionTracker().countSelected() < 2) {
      JOptionPane.showMessageDialog(getWindow(),
          "Potrebno je oznaèiti bar dvije kategorije!", "Greška",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    raInputDialog dlg = new raInputDialog() {
      JlrNavField jlrPRIP;
      protected void init() {
        XYPanel pan = new XYPanel().label("Pripadnost")
            .nav("CKATPRIP:CKAT", kats, "NAZKAT").expand();
        setParams("Promjena pripadnosti kategorija", pan,
            jlrPRIP = pan.getNav("CKATPRIP"));
      }

      protected boolean checkOk() {
        return !Valid.getValid().isEmpty(jlrPRIP);
      }
    };
    if (dlg.show(getWindow())) {
      String cgnew = ((JlrNavField) dlg.getValue()).getText();

      String[] sel = (String[]) getSelectionTracker().getSelection();
      for (int i = 0; i < sel.length; i++)
        if (DataTree.isCircular(sel[i], cgnew, dm.getKategorije(), "CKAT", "CKATPRIP")) {
          lookupData.getlookupData().raLocate(getRaQueryDataSet(), "CKAT", sel[i]);
          JOptionPane.showMessageDialog(getWindow(),
              "Pripadnost bi stvorila beskonaènu petlju!", "Greška",
              javax.swing.JOptionPane.ERROR_MESSAGE);
          return;
        }

      try {
        getJpTableView().enableEvents(false);
        int row = getRaQueryDataSet().getRow();
        for (int i = 0; i < sel.length; i++)
          if (lookupData.getlookupData().raLocate(getRaQueryDataSet(),
              "CKAT", sel[i]))
            getRaQueryDataSet().setString("CKATPRIP", cgnew);

        getRaQueryDataSet().goToRow(row);
      } finally {
        getJpTableView().enableEvents(true);
      }
      getRaQueryDataSet().saveChanges();
      getJpTableView().fireTableDataChanged();
    }
  }
}
