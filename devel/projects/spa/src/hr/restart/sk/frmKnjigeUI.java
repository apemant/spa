/****license*****************************************************************
**   file: frmKnjigeUI.java
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
package hr.restart.sk;

import hr.restart.baza.Vrshemek;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmKnjigeUI extends raMatPodaci {
  dM dm = dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlKnj = new JLabel();
  JraTextField jraKnj = new JraTextField();
  raButtonGroup bg = new raButtonGroup();
  JraRadioButton jrbURA = new JraRadioButton();
  JraRadioButton jrbIRA = new JraRadioButton();
  JraCheckBox jcbKnj = new JraCheckBox();
  JraCheckBox jcbVirt = new JraCheckBox();
  JLabel jlNazknj = new JLabel();
  JraTextField jraNazknj = new JraTextField();
  JLabel jlShema = new JLabel();
  JlrNavField jlrVrsk = new JlrNavField();
  JlrNavField jlrNazVrsk = new JlrNavField();
  JraButton jbSelShema = new JraButton();

  QueryDataSet shem;
  int lastvr = 0;
  boolean foc;

  public frmKnjigeUI() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus(char parm1) {
    if (parm1 == 'N') {
//      this.getRaQueryDataSet().setString("URAIRA", "I");
      bg.setSelected(jrbIRA);
      jraKnj.requestFocus();
    } else if (parm1 == 'I') {
      jraNazknj.requestFocus();
      rcc.setLabelLaF(jraKnj, false);
      jrbURA.setEnabled(false);
      jrbIRA.setEnabled(false);
    }
    if (parm1 != 'B') foc = true;
  }

  public boolean Validacija(char parm1) {
    if (vl.isEmpty(jraKnj)) return false;
    if (parm1 == 'N' && vl.chkExistsSQL(new Column[] {this.getRaQueryDataSet().getColumn("CKNJIGE"),
      this.getRaQueryDataSet().getColumn("URAIRA")}, new String[] {jraKnj.getText(), this.getRaQueryDataSet().getString("URAIRA")})) {
      jraKnj.requestFocus();
      JOptionPane.showMessageDialog(jpDetail, "Zapis ve\u0107 postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    foc = false;
    return true;
  }

  public void afterSetMode(char bef, char aft) {
    if (aft == 'B') foc = false;
  }

  private void jbInit() throws Exception {
    shem = Vrshemek.getDataModule().getFilteredDataSet("app = 'sk'");
    this.setRaQueryDataSet(dm.getKnjigeUI());
    this.setRaDetailPanel(jpDetail);
    this.setVisibleCols(new int[] {0,4});

    jpDetail.setLayout(xYLayout1);
    xYLayout1.setWidth(510);
    xYLayout1.setHeight(150);
    jlKnj.setText("Oznaka");
    jlNazknj.setText("Naziv");
    jlShema.setText("Shema knjiženja");
    jbSelShema.setText("...");

    bg.setColumnName("URAIRA");
    bg.setDataSet(this.getRaQueryDataSet());
    bg.add(jrbIRA, " Izlazna ", "I");
    bg.add(jrbURA, " Ulazna ", "U");

    jraKnj.setColumnName("CKNJIGE");
    jraKnj.setDataSet(this.getRaQueryDataSet());
    jraNazknj.setColumnName("NAZKNJIGE");
    jraNazknj.setDataSet(this.getRaQueryDataSet());
    
    jcbKnj.setColumnName("KNJIZENJE");
    jcbKnj.setDataSet(this.getRaQueryDataSet());
    jcbKnj.setSelectedDataValue("D");
    jcbKnj.setUnselectedDataValue("N");
    jcbKnj.setText(" Knjiženje raèuna u glavnu knjigu i salda konti ");
    jcbKnj.setHorizontalTextPosition(JLabel.LEADING);
    jcbKnj.setHorizontalAlignment(JLabel.TRAILING);
    
    jcbVirt.setColumnName("VIRTUA");
    jcbVirt.setDataSet(this.getRaQueryDataSet());
    jcbVirt.setSelectedDataValue("D");
    jcbVirt.setUnselectedDataValue("N");
    jcbVirt.setText(" Virtualna knjiga ");
    jcbVirt.setHorizontalTextPosition(JLabel.LEADING);
    jcbVirt.setHorizontalAlignment(JLabel.TRAILING);

    jlrVrsk.setColumnName("CSKL");
    jlrVrsk.setDataSet(this.getRaQueryDataSet());
    jlrVrsk.setSearchMode(0);
    jlrVrsk.setTextFields(new JTextField[] {jlrNazVrsk});
    jlrVrsk.setColNames(new String[] {"OPISVRSK"});
    jlrVrsk.setNavColumnName("CVRSK");
    jlrVrsk.setRaDataSet(shem);
    jlrVrsk.setVisCols(new int[] {0,1,2});
    jlrVrsk.setNavButton(jbSelShema);

    jlrNazVrsk.setColumnName("OPISVRSK");
    jlrNazVrsk.setNavProperties(jlrVrsk);
    jlrNazVrsk.setSearchMode(1);


    jpDetail.add(jlKnj, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraKnj, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jlNazknj, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jraNazknj, new XYConstraints(150, 45, 320, -1));
    jpDetail.add(jlShema, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlrVrsk, new XYConstraints(150, 70, 75, -1));
    jpDetail.add(jlrNazVrsk, new XYConstraints(230, 70, 240, -1));
    jpDetail.add(jrbIRA, new XYConstraints(275, 18, -1, -1));
    jpDetail.add(jrbURA, new XYConstraints(380, 18, -1, -1));
    jpDetail.add(jbSelShema, new XYConstraints(475, 70, 21, 21));
    jpDetail.add(jcbKnj, new XYConstraints(170, 95, 300, -1));
    jpDetail.add(jcbVirt, new XYConstraints(170, 118, 300, -1));
    
    jrbIRA.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (jrbIRA.isSelected()) iraSelected();
      }
    });

    jrbURA.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (jrbURA.isSelected()) uraSelected();
      }
    });
  }

  private void iraSelected() {
    if (lastvr != 1) {
      lastvr = 1;
      Vrshemek.getDataModule().setFilter(shem, "app = 'sk' AND vrdok = 'IRN'");
      shem.open();
      if (foc) {
        jlrVrsk.forceFocLost();
        bg.setSelected(jrbIRA);
      }
    }
  }

  private void uraSelected() {
    if (lastvr != 2) {
      lastvr = 2;
      Vrshemek.getDataModule().setFilter(shem, "app = 'sk' AND vrdok = 'URN'");
      shem.open();
      if (foc) {
        jlrVrsk.forceFocLost();
        bg.setSelected(jrbURA);
      }
    }
  }
}
