/****license*****************************************************************
**   file: frmDob_Art.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.baza.dob_art;
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

public class frmDob_Art extends raMasterFakeDetailArtikl {
  // pomo\u0107ne klase
  raCommonClass rcc = raCommonClass.getraCommonClass();

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
  XYLayout xYLayout2 = new XYLayout();
  JlrNavField jrfCPAR = new JlrNavField();
  JlrNavField jrfNAZPAR = new JlrNavField();
  JLabel jLabel7 = new JLabel();
  JraButton jbCPAR = new JraButton();
  JPanel jpSel = new JPanel();

  String[] key = new String[] {"CPAR"};

  public frmDob_Art() {
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

  public void EntryPointDetail(char mod) {
    System.err.println(rpc.getTabela()+ "  " + rpc.getTabela().getRow());
    super.EntryPointDetail(mod);
    System.err.println(rpc.getTabela()+ "  " + rpc.getTabela().getRow());
  }

  public void SetFokusIzmjena() {
    System.err.println(rpc.getTabela()+ "  " + rpc.getTabela().getRow());
    jtfDC.requestFocus();
  }

  public void ClearFields() {
/*    jtfDC.setText("");
    jtfTEZPAK.setText("");
    jtfBRJED.setText("");
    jtfJMPAK.setText("");
    jtfNAZPAK.setText("");
    jtfPRAB.setText(""); */
    this.getDetailSet().setBigDecimal("DC", _Main.nul);
    this.getDetailSet().setBigDecimal("TEZPAK", _Main.nul);
    this.getDetailSet().setBigDecimal("BRJED", _Main.nul);
    this.getDetailSet().setBigDecimal("PRAB", _Main.nul);
    this.getDetailSet().setString("JMPAK", "");
    this.getDetailSet().setString("NAZPAK", "");
  }

  public String CheckMasterKeySQLString() {
    return "select * from dob_art where cpar = " + mast.getInt("CPAR");
  }

  private void setMasterData() {
    String sql = "SELECT dob_art.cpar, MAX(partneri.nazpar) as nazpar "+
                  "FROM dob_art,partneri WHERE dob_art.cpar = partneri.cpar GROUP BY dob_art.cpar";
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
    mast.setTableName("Dob_art_master");

    //part.setRowId();
    //vl.RezSet = null;
  }

  private void jbInit() throws Exception {
    setMasterData();

    this.setMasterSet(mast);
    this.setNaslovMaster("Dobavlja\u010Di");
    this.setVisibleColsMaster(new int[] {0,1});
    this.setMasterKey(key);

    this.setDetailSet(dob_art.getDataModule().getFilteredDataSet(Condition.nil));
    this.setNaslovDetail("Artikli dobavlja\u010Da");
    this.setVisibleColsDetail(new int[] {Aut.getAut().getCARTdependable(1,2,3),4,5,6});
    this.setDetailKey(key);

    jPanel2.setLayout(xYLayout1);
    jLabel1.setText("Dobavlja\u010Deva cijena");
    jLabel2.setText("Rabat");
    jLabel3.setText("Naziv pakiranja");
    jLabel4.setText("Jedinica mjere");
    jLabel5.setText("Broj jedinica");
    jtfPRAB.setDataSet(getDetailSet());
    jtfPRAB.setColumnName("PRAB");
    jtfNAZPAK.setDataSet(getDetailSet());
    jtfNAZPAK.setColumnName("NAZPAK");
    jLabel6.setText("Težina pakiranja");
    xYLayout1.setHeight(185);
    xYLayout1.setWidth(651);
    jtfJMPAK.setDataSet(getDetailSet());
    jtfJMPAK.setColumnName("JMPAK");
    jtfBRJED.setDataSet(getDetailSet());
    jtfBRJED.setColumnName("BRJED");
    jtfTEZPAK.setDataSet(getDetailSet());
    jtfTEZPAK.setColumnName("TEZPAK");
    jtfDC.setDataSet(getDetailSet());
    jtfDC.setColumnName("DC");
    xYLayout2.setWidth(580);
    xYLayout2.setHeight(60);

    jrfCPAR.setColumnName("CPAR");
    jrfCPAR.setDataSet(this.getMasterSet());
    jrfCPAR.setColNames(new String[] {"NAZPAR"});
    jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
    jrfCPAR.setVisCols(new int[]{0,1});
    jrfCPAR.setSearchMode(0);
    jrfCPAR.setRaDataSet(dm.getPartneri());
    jrfCPAR.setNavButton(jbCPAR);

    jrfNAZPAR.setNavProperties(jrfCPAR);
    jrfNAZPAR.setDataSet(this.getMasterSet());
    jrfNAZPAR.setColumnName("NAZPAR");
    jrfNAZPAR.setSearchMode(1);

    jLabel7.setText("Poslovni partner");
    jbCPAR.setText("...");
    jpSel.setLayout(xYLayout2);
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

    jpSel.add(jLabel7, new XYConstraints(15, 20, -1, -1));
    jpSel.add(jrfCPAR, new XYConstraints(150, 20, 100, -1));
    jpSel.add(jrfNAZPAR, new XYConstraints(260, 20, 275, -1));
    jpSel.add(jbCPAR, new XYConstraints(539, 20, 21, 21));

    this.SetPanels(jpSel, jPanel2, true);
  }

  // inicijalizacija rapancarta. Mode = "DOH" postavlja mod na dohva\u0107anje (bez izmjene)
  protected void initRpcart() {
    //rpc.setGodina(hr.restart.util.Valid.getValid().findYear(dm.getDoku().getTimestamp("DATDOK")));
    //rpc.setCskl(dm.getStdoku().getString("CSKL"));
    rpc.setTabela(getDetailSet());
    rpc.setBorder(BorderFactory.createEtchedBorder());
    super.initRpcart();
  }
}