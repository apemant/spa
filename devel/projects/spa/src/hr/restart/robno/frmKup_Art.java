/****license*****************************************************************
**   file: frmKup_Art.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmKup_Art extends raMasterFakeDetailArtikl {
  // pomo\u0107ne klase
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JlrNavField jrfNAZPAR = new JlrNavField();
  JraButton jbCPAR = new JraButton();
  JLabel jLabel7 = new JLabel();
  JPanel jpSel = new JPanel();
  JlrNavField jrfCPAR = new JlrNavField();
  XYLayout xYLayout2 = new XYLayout();
  JraTextField jtfDC = new JraTextField();
  JraTextField jtfTEZPAK = new JraTextField();
  JraTextField jtfBRJED = new JraTextField();
  JraTextField jtfJMPAK = new JraTextField();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jLabel6 = new JLabel();
  JraTextField jtfNAZPAK = new JraTextField();
  JraTextField jtfPRAB = new JraTextField();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel1 = new JLabel();
  JPanel jPanel2 = new JPanel();

  String[] key = new String[] {"CPAR"};

  public frmKup_Art() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'I') {
      rcc.EnabDisabAll(this.getJPanelMaster(), false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jrfCPAR.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jrfCPAR))
      return false;
    if (mode == 'N' && MasterNotUnique()) {
      jrfCPAR.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Dobavlja\u010D ve\u0107 postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
      //System.out.println(this.getMasterSet().rowCount());

      return false;
    }
    mast.post();
    return true;
  }

  public boolean canDeleteMaster() {
    return true;
  }

   public void SetFokusIzmjena() {
    jtfDC.requestFocus();
  }

  public void ClearFields() {
/*    jtfDC.setText("");
    jtfTEZPAK.setText("");
    jtfBRJED.setText("");
    jtfJMPAK.setText("");
    jtfNAZPAK.setText("");
    jtfPRAB.setText(""); */
    this.getDetailSet().setBigDecimal("VC", _Main.nul);
    this.getDetailSet().setBigDecimal("TEZPAK", _Main.nul);
    this.getDetailSet().setBigDecimal("BRJED", _Main.nul);
    this.getDetailSet().setBigDecimal("PRAB", _Main.nul);
    this.getDetailSet().setString("JMPAK", "");
    this.getDetailSet().setString("NAZPAK", "");
  }

  public String CheckMasterKeySQLString() {
    return "select * from kup_art where cpar = " + mast.getInt("CPAR");
  }

  private void setMasterData() {
    String sql = "SELECT MAX(kup_art.cpar) as cpar, MAX(partneri.nazpar) as nazpar "+
                  "FROM kup_art,partneri WHERE kup_art.cpar = partneri.cpar GROUP BY kup_art.cpar";
    //vl.execSQL(sql);
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(
      dM.getDataModule().getDatabase1(),sql
      ));
    //part = vl.RezSet;
    mast.setColumns(new Column[] {
      (Column) dm.getPartneri().getColumn("CPAR").clone(),
      (Column) dm.getPartneri().getColumn("NAZPAR").clone()
    });

    mast.open();
    mast.setRowId("CPAR", true);
    mast.setRowId("NAZPAR", false);
    mast.setTableName("Kup_art_master");
    //part.setRowId();
    //vl.RezSet = null;
  }

  private void jbInit() throws Exception {
    setMasterData();

    this.setMasterSet(mast);
    this.setNaslovMaster("Kupci");
    this.setVisibleColsMaster(new int[] {0,1});
    this.setMasterKey(key);

    this.setDetailSet(dm.getKup_art());
    this.setNaslovDetail("Artikli za kupce");
    this.setVisibleColsDetail(new int[] {Aut.getAut().getCARTdependable(1,2,3),4,5,6});
    this.setDetailKey(key);

    jpSel.setLayout(xYLayout2);
    jLabel7.setText("Poslovni partner");
    jbCPAR.setText("...");

    jrfCPAR.setDataSet(this.getMasterSet());
    jrfCPAR.setColumnName("CPAR");
    jrfCPAR.setColNames(new String[] {"NAZPAR"});
    jrfCPAR.setVisCols(new int[]{0,1});
    jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
    jrfCPAR.setRaDataSet(dm.getPartneri());
    jrfCPAR.setSearchMode(0);
    jrfCPAR.setNavButton(jbCPAR);

    jrfNAZPAR.setNavProperties(jrfCPAR);
    jrfNAZPAR.setDataSet(this.getMasterSet());
    jrfNAZPAR.setSearchMode(1);
    jrfNAZPAR.setColumnName("NAZPAR");

    xYLayout2.setWidth(580);
    xYLayout2.setHeight(60);
    jtfDC.setDataSet(dm.getKup_art());
    jtfDC.setColumnName("VC");
    jtfTEZPAK.setDataSet(dm.getKup_art());
    jtfTEZPAK.setColumnName("TEZPAK");
    jtfBRJED.setDataSet(dm.getKup_art());
    jtfBRJED.setColumnName("BRJED");
    jtfJMPAK.setDataSet(dm.getKup_art());
    jtfJMPAK.setColumnName("JMPAK");
    xYLayout1.setWidth(651);
    xYLayout1.setHeight(185);
    jLabel6.setText("Težina pakiranja");
    jtfNAZPAK.setDataSet(dm.getKup_art());
    jtfNAZPAK.setColumnName("NAZPAK");
    jtfPRAB.setDataSet(dm.getKup_art());
    jtfPRAB.setColumnName("PRAB");
    jLabel5.setText("Broj jedinica");
    jLabel4.setText("Jedinica mjere");
    jLabel3.setText("Naziv pakiranja");
    jLabel2.setText("Rabat");
    jLabel1.setText("Prodajna cijena");
    jPanel2.setLayout(xYLayout1);
    jpSel.add(jLabel7, new XYConstraints(15, 20, -1, -1));
    jpSel.add(jrfCPAR, new XYConstraints(150, 20, 100, -1));
    jpSel.add(jrfNAZPAR, new XYConstraints(260, 20, 275, -1));
    jpSel.add(jbCPAR, new XYConstraints(539, 20, 21, 21));
    jPanel2.add(jLabel1, new XYConstraints(15, 20, -1, -1));
    jPanel2.add(jLabel2, new XYConstraints(15, 45, -1, -1));
    jPanel2.add(jLabel3, new XYConstraints(15, 70, -1, -1));
    jPanel2.add(jLabel4, new XYConstraints(15, 95, -1, -1));
    jPanel2.add(jLabel5, new XYConstraints(15, 120, -1, -1));
    jPanel2.add(jtfDC, new XYConstraints(150, 20, 100, -1));
    jPanel2.add(jtfPRAB, new XYConstraints(150, 45, 100, -1));
    jPanel2.add(jtfNAZPAK, new XYConstraints(150, 70, 300, -1));
    jPanel2.add(jtfJMPAK, new XYConstraints(150, 95, 100, -1));
    jPanel2.add(jtfBRJED, new XYConstraints(150, 120, 100, -1));
    jPanel2.add(jtfTEZPAK, new XYConstraints(150, 145, 100, -1));
    jPanel2.add(jLabel6, new XYConstraints(15, 145, -1, -1));

    SetPanels(jpSel, jPanel2, true);
  }

  // inicijalizacija rapancarta. Mode = "DOH" postavlja mod na dohva\u0107anje (bez izmjene)
  protected void initRpcart() {
    //rpc.setGodina(hr.restart.util.Valid.getValid().findYear(dm.getDoku().getTimestamp("DATDOK")));
    //rpc.setCskl(dm.getStdoku().getString("CSKL"));
    rpc.setTabela(dm.getKup_art());
    rpc.setBorder(BorderFactory.createEtchedBorder());
    super.initRpcart();
    rpc.setAllowUsluga(true);
  }
}
