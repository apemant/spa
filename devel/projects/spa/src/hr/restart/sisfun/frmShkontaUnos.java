/****license*****************************************************************
**   file: frmShkontaUnos.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmShkontaUnos extends raMasterDetail {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JraButton jbAdvanced = new JraButton();
  JraButton jbSql = new JraButton();
  
  JPanel jpMaster = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jlrApp = new JlrNavField();
  JlrNavField jlrNazDok = new JlrNavField();
  JlrNavField jlrDok = new JlrNavField();
  JraButton jbDok = new JraButton();
  JLabel jlDok = new JLabel();

  String[] key = new String[] {"VRDOK"};

  protected QueryDataSet mast = new QueryDataSet() {
//    public void saveChanges() {
//      this.post();
//    }
    public boolean saveChangesSupported() {
      return false;
    }
  };
  JPanel jpDetail = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JLabel jlStavka = new JLabel();
  JLabel jlKonto = new JLabel();
  JraTextField jraStavka = new JraTextField();
  JlrNavField jlrKonto = new JlrNavField() {
    public void after_lookUp() {
      updateKar();
    }
  };
  JraTextField jraNazStavka = new JraTextField();
  JraTextField jraPolje = new JraTextField();
  JlrNavField jlrNazKonto = new JlrNavField() {
    public void after_lookUp() {
      updateKar();
    }
  };
  JlrNavField jlrKar = new JlrNavField();
  JraButton jbKonto = new JraButton();
  JraComboBox jcbKar = new JraComboBox(new String[] {" ", "D", "P"});
  JLabel jlBroj = new JLabel();
  JLabel jlOpis = new JLabel();
  JLabel jlPolje = new JLabel();
  int oldstavka;


  public frmShkontaUnos() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'I')
      rcc.EnabDisabAll(jpMaster, false);
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jlrDok.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (jlrDok.getText().trim().equals("")) {
      jlrDok.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Obavezno izabrati vrstu dokumenta!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (mode == 'N' && MasterNotUnique()) {
      jlrDok.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Vrsta dokumenta ve\u0107 u tablici!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    getMasterSet().post();
    return true;
  }

/*  public boolean DeleteCheckMaster() {
    if (getDetailSet().rowCount() > 0) {
      JOptionPane.showMessageDialog(getJPanelMaster(),
         "Nije mogu\u0107e brisati zaglavlje dok se ne pobrišu stavke!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    } else
      return true;
  } */

  public void refilterDetailSet() {
    super.refilterDetailSet();
    this.setNaslovDetail("Stavke standardne sheme - " + mast.getString("VRDOK"));
  }

  public void ZatvoriOstaloDetail() {
    if (this.getDetailSet().rowCount() == 0) {
      this.getMasterSet().refresh();
//      ((hr.restart.swing.dataSetTableModel)raMaster.getJpTableView().getMpTable().getModel()).fireTableDataChanged();
      raMaster.getJpTableView().fireTableDataChanged();
    }
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'N') {
      EraseFields();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jraStavka, false);
      jlrKonto.forceFocLost();
    }
    updateKar();
    jcbKar.setSelectedItem(this.getDetailSet().getString("KARAKTERISTIKA"));
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      EraseFields();
      getDetailSet().setString("APP", mast.getString("APP"));
      jraStavka.requestFocus();
    } else if (mode == 'I' ){
      jraNazStavka.requestFocus();
      oldstavka = this.getDetailSet().getShort("STAVKA");
    }
    updateKar();
    jcbKar.setSelectedItem(this.getDetailSet().getString("KARAKTERISTIKA"));
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jraStavka))
      return false;
    if (vl.isEmpty(jlrKonto))
      return false;
    if (((String) jcbKar.getSelectedItem()).trim().equals("")) {
      jcbKar.requestFocus();
      JOptionPane.showMessageDialog(this.jpDetail,"Obavezan unos KARAKTERISTIKA!","Greška",
        JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if ((mode == 'N' || (mode == 'I' && oldstavka != this.getDetailSet().getShort("STAVKA"))) && stavkaNotUnique()) {
      jraStavka.requestFocus();
      JOptionPane.showMessageDialog(this.jpDetail,"Stavka ve\u0107 u tablici!","Greška",
        JOptionPane.ERROR_MESSAGE);
      return false;
    }
    //this.getDetailSet().setString("VRDOK", mast.getString("VRDOK"));
    this.getDetailSet().setString("KARAKTERISTIKA", (String) jcbKar.getSelectedItem());
    return true;
  }

  public void afterSetModeDetail(char oldm, char newm) {
    dlgUraIra.setEnabled(newm != 'B');
  }

  protected void EraseFields() {
    /*jraStavka.setText("");
    jraNazStavka.setText("");
    jlrKonto.setText(""); */
    jlrNazKonto.setText("");
    jcbKar.setSelectedIndex(0);

  }

  protected boolean stavkaNotUnique() {
    return (Asql.getStavkaDefshkonta(mast.getString("VRDOK"), jraStavka.getText()).rowCount() > 0);
  }

  protected boolean MasterNotUnique() {
    return (Asql.getStavkeDefshkonta(mast.getString("VRDOK")).rowCount() > 0);
  }

  private void updateKar() {
    jcbKar.removeAllItems();
    if (jlrKonto.getText().equals("") || !jlrKonto.isLastLookSuccessfull()) {
      jcbKar.addItem(" ");
      jcbKar.addItem("D");
      jcbKar.addItem("P");
    } else {
      if (jlrKar.getText().equals("O"))
        jcbKar.addItem(" ");
      if (jlrKar.getText().equals("O") || jlrKar.getText().equals("D"))
        jcbKar.addItem("D");
      if (jlrKar.getText().equals("O") || jlrKar.getText().equals("P"))
        jcbKar.addItem("P");
    }
    jcbKar.setSelectedIndex(0);
  }

  private void jbInit() throws Exception {

    Asql.createMasterDefshkonta(mast);

    this.setMasterSet(mast);
    this.setNaslovMaster("Standardne sheme kontiranja");
    this.setVisibleColsMaster(new int[] {0,1,2});
    this.setMasterKey(key);

    this.setDetailSet(dm.getDefshkonta());
    this.setNaslovDetail("Stavke standardne sheme");
    this.setVisibleColsDetail(new int[] {1,2,3,4,5});
    this.setJPanelDetail(jpDetail);
    this.setDetailKey(key);

    this.setMasterDeleteMode(DELDETAIL);

    JPanel adv = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
    jbAdvanced.setText("Knjiga URA/IRA");
    jbSql.setText("SQL dohvat");
    adv.add(jbAdvanced);
    adv.add(jbSql);
    
    raDetail.getOKpanel().add(adv, BorderLayout.WEST);

    xYLayout1.setWidth(500);
    xYLayout1.setHeight(60);
    jpMaster.setLayout(xYLayout1);

    jlDok.setText("Vrsta dokumenta");
    jlrDok.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrDok.setColumnName("VRDOK");
    jlrDok.setDataSet(mast);
    jlrDok.setColNames(new String[] {"NAZDOK", "APP"});
    jlrDok.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazDok, jlrApp});
    jlrDok.setVisCols(new int[] {0,1});
    jlrDok.setSearchMode(0);
    jlrDok.setRaDataSet(dm.getVrdokum());
    jlrDok.setNavButton(jbDok);

    jlrNazDok.setColumnName("NAZDOK");
    jlrNazDok.setDataSet(mast);
    jlrNazDok.setNavProperties(jlrDok);
    jlrNazDok.setSearchMode(1);

    jlrApp.setColumnName("APP");
    jlrApp.setNavProperties(jlrDok);
    jlrApp.setDataSet(mast);
    jlrApp.setVisible(false);
    jlrApp.setEnabled(false);

    jpDetail.setLayout(xYLayout2);
    xYLayout2.setWidth(600);
    xYLayout2.setHeight(110);
    jlStavka.setText("Stavka");
    jlKonto.setText("Broj konta");
    jbKonto.setText("...");
    jbDok.setText("...");

    jraStavka.setDataSet(this.getDetailSet());
    jraStavka.setColumnName("STAVKA");
    jraStavka.setHorizontalAlignment(SwingConstants.TRAILING);

    jraNazStavka.setDataSet(this.getDetailSet());
    jraNazStavka.setColumnName("OPIS");

    jraPolje.setDataSet(this.getDetailSet());
    jraPolje.setColumnName("POLJE");

    jlrKonto.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrKonto.setColumnName("BROJKONTA");
    jlrKonto.setDataSet(this.getDetailSet());
    jlrKonto.setColNames(new String[] {"NAZIVKONTA", "KARAKTERISTIKA"});
    jlrKonto.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazKonto, jlrKar});
    jlrKonto.setVisCols(new int[] {0,1});
    jlrKonto.setSearchMode(3);
    jlrKonto.setRaDataSet(dm.getKontaAnalitic());
    jlrKonto.setNavButton(jbKonto);

    jlrKar.setNavProperties(jlrKonto);
    jlrKar.setColumnName("KARAKTERISTIKA");
    jlrKar.setSearchMode(1);
    jlrKar.setVisible(false);
    jlrKar.setEnabled(false);

    jlrNazKonto.setNavProperties(jlrKonto);
    jlrNazKonto.setColumnName("NAZIVKONTA");
    jlrNazKonto.setSearchMode(1);

    jlBroj.setText("Broj");
    jlOpis.setText("Opis");
    jlPolje.setText("Kolona");

    jpMaster.add(jlrApp, new XYConstraints(10, 0, 5, -1));
    jpMaster.add(jlrNazDok, new XYConstraints(205, 20, 250, -1));
    jpMaster.add(jlrDok, new XYConstraints(150, 20, 50, -1));
    jpMaster.add(jlDok, new XYConstraints(15, 20, -1, -1));
    jpMaster.add(jbDok, new XYConstraints(460, 20, 21, 21));

    jpDetail.add(jlrKar, new XYConstraints(515, 45, 50, -1));
    jpDetail.add(jraStavka, new XYConstraints(150, 40, 75, -1));
    jpDetail.add(jlStavka, new XYConstraints(15, 40, -1, -1));
    jpDetail.add(jlKonto, new XYConstraints(15, 65, -1, -1));
    jpDetail.add(jlrKonto, new XYConstraints(150, 65, 75, -1));
    jpDetail.add(jraNazStavka, new XYConstraints(230, 40, 275, -1));
    jpDetail.add(jlrNazKonto, new XYConstraints(230, 65, 275, -1));
    jpDetail.add(jbKonto, new XYConstraints(510, 65, 21, 21));
    jpDetail.add(jcbKar, new XYConstraints(540, 65, 45, -1));
    jpDetail.add(jlBroj, new XYConstraints(150, 22, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(230, 22, -1, -1));
    jpDetail.add(jraPolje, new XYConstraints(510, 40, 75, -1));
    jpDetail.add(jlPolje, new XYConstraints(510, 22, -1, -1));

    this.setJPanelMaster(jpMaster);

    jcbKar.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==e.VK_F10) {
          raDetail.getOKpanel().jBOK_actionPerformed();
          e.consume();
        }
        if (e.getKeyCode()==e.VK_ESCAPE) {
          raDetail.getOKpanel().jPrekid_actionPerformed();
          e.consume();
        }
      }
    });

    jbAdvanced.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showAdvanced();
      }
    });
    
    jbSql.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showSql();
      }
    });
  }
  
  void showSql() {
    String ret = dlgSqlDohvat.showDlg(this.getWindow(), getDetailSet().getString("sqlcondition"));
    if (ret != null) {
      if (ret.length() > 5000) ret = ret.substring(0, 5000);
      getDetailSet().setString("sqlcondition", ret);
    }
  }

  void showAdvanced() {
    lookupData.getlookupData().raLocate(dm.getVrdokum(), "VRDOK", getMasterSet().getString("VRDOK"));
    if (dm.getVrdokum().getString("VRSDOK").equals("U")) {
      dlgUraIra.setUI(dlgUraIra.URA);
      dlgUraIra.open(raDetail);
    } else if (dm.getVrdokum().getString("VRSDOK").equals("I")) {
      dlgUraIra.setUI(dlgUraIra.IRA);
      dlgUraIra.open(raDetail);
    } else {
      JOptionPane.showMessageDialog(raDetail.getWindow(), "Pogrešna vrsta dokumenta!",
                                    "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
}
