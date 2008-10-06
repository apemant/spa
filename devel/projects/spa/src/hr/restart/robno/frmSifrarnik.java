/****license*****************************************************************
**   file: frmSifrarnik.java
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
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmSifrarnik extends raMasterDetail {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JPanel jpMaster = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlVrsub = new JLabel();
  JLabel jlZnac = new JLabel();
  JlrNavField jlrVrsub = new JlrNavField();
  JlrNavField jlrZnac = new JlrNavField();
  JlrNavField jlrNazvrsub = new JlrNavField();
  JlrNavField jlrNaznac = new JlrNavField();
  JraButton jbVrsub = new JraButton();
  JraButton jbZnac = new JraButton();
  JPanel jpDetail = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JraTextField jraVri = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JLabel jlVri = new JLabel();
  JLabel jlOpis = new JLabel();

  String deleteSQL;
  String[] key = new String[] {"CVRSUBJ", "CZNAC"};
  QueryDataSet mast = new QueryDataSet() {
//    public void saveChanges() {
//      this.post();
//    }
    public boolean saveChangesSupported() {
      return false;
    }
  };

  public frmSifrarnik() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void beforeShowMaster() {
    mast.refresh();
    jlrZnac.getRaDataSet().refresh();
    this.getDetailSet().open();
  }

  public void EntryPointMaster(char mode) {
    rcc.EnabDisabAll(jpMaster, false);
    rcc.setLabelLaF(raMaster.getOKpanel().jBOK, false);

  }

//  public void SetFokusMaster(char mode) {
//    raMaster.getOKpanel().jPrekid_actionPerformed();
//  }

/*  public boolean DeleteCheckMaster() {
    deleteSQL = "";
    this.refilterDetailSet();
    if (this.getDetailSet().rowCount() > 0) {
      deleteSQL = "DELETE FROM RN_sifznac WHERE cvrsubj = " + mast.getShort("CVRSUBJ");

    }
    return true;
  }

  public void AfterDeleteMaster() {
    if (!deleteSQL.equals(""))
      vl.runSQL(deleteSQL);
  } */

  public void refilterDetailSet() {
    super.refilterDetailSet();
    this.setNaslovDetail("Popis vrijednosti podatka - "+mast.getString("ZNACOPIS"));
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
      rcc.setLabelLaF(jraVri, false);
    }
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      EraseFields();
      this.getDetailSet().setShort("CVRSUBJ", this.getMasterSet().getShort("CVRSUBJ"));
      this.getDetailSet().setShort("CZNAC", this.getMasterSet().getShort("CZNAC"));
      jraVri.requestFocus();
    } else if (mode == 'I' ){
      jraOpis.requestFocus();
    }
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jraVri)) {
      return false;
    }
    if (mode == 'N' && DetailNotUnique()) {
      jraVri.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Ista vrijednost ve\u0107 postoji u tablici!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

/*  public void AfterSaveDetail(char mode) {
    if (mode == 'N') {
      rpc.EnabDisab(true);
    }
   } */

  private void EraseFields() {
  }

  protected boolean DetailNotUnique() {
    vl.execSQL("SELECT * FROM RN_sifznac WHERE cvrsubj = "+mast.getShort("CVRSUBJ")+
               " AND cznac = "+mast.getShort("CZNAC")+
               " AND vriznac = '"+this.getDetailSet().getString("VRIZNAC")+"'");
    vl.RezSet.open();
    return (vl.RezSet.rowCount() > 0);
  }


  private void setMasterData() {
    String sql = "SELECT RN_sifznac.cvrsubj, MAX(RN_vrsub.nazvrsubj) as nazvrsubj, "+
                 "RN_sifznac.cznac, MAX(RN_znacajke.znacopis) as znacopis "+
                 "FROM RN_sifznac, RN_znacajke, RN_vrsub "+
                 "WHERE RN_sifznac.cznac = RN_znacajke.cznac "+
                 "AND RN_sifznac.cvrsubj = RN_vrsub.cvrsubj "+
                 "AND RN_sifznac.cvrsubj = RN_znacajke.cvrsubj "+
                 "GROUP BY RN_sifznac.cvrsubj, RN_sifznac.cznac";

    //vl.execSQL(sql);
    mast.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(
      dM.getDataModule().getDatabase1(),sql
      ));
    //part = vl.RezSet;

    mast.setColumns(new Column[] {
      (Column) dm.getRN_sifznac().getColumn("CVRSUBJ").clone(),
      (Column) dm.getRN_vrsub().getColumn("NAZVRSUBJ").clone(),
      (Column) dm.getRN_sifznac().getColumn("CZNAC").clone(),
      (Column) dm.getRN_znacajke().getColumn("ZNACOPIS").clone()
    });

    mast.open();
    mast.setRowId("CVRSUBJ", true);
    mast.setRowId("NAZVRSUBJ", false);
    mast.setRowId("CZNAC", true);
    mast.setRowId("ZNACOPIS", false);
    mast.setTableName("sifznac_master");
  }

  private void jbInit() throws Exception {

    setMasterData();

    this.setMasterSet(mast);
    this.setNaslovMaster("Šifrarnik podataka");
    this.setVisibleColsMaster(new int[] {1,3});
    this.setMasterKey(key);

    this.setDetailSet(dm.getRN_sifznac());
    this.setNaslovDetail("Popis vrijednosti");
    this.setVisibleColsDetail(new int[] {2,3});
    this.setDetailKey(key);

    this.setMasterDeleteMode(DELDETAIL);

    jpMaster.setLayout(xYLayout1);
    xYLayout1.setWidth(550);
    xYLayout1.setHeight(85);

    jlVrsub.setText("Vrsta subjekta");
    jlZnac.setText("Podatak");
    jbVrsub.setText("...");
    jbZnac.setText("...");

    jlrVrsub.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrVrsub.setColumnName("CVRSUBJ");
    jlrVrsub.setDataSet(mast);
    jlrVrsub.setColNames(new String[] {"NAZVRSUBJ"});
    jlrVrsub.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazvrsub});
    jlrVrsub.setVisCols(new int[] {0,1});
    jlrVrsub.setSearchMode(0);
    jlrVrsub.setRaDataSet(dm.getRN_vrsub());
    jlrVrsub.setNavButton(jbVrsub);

    jlrNazvrsub.setNavProperties(jlrVrsub);
    jlrNazvrsub.setDataSet(mast);
    jlrNazvrsub.setColumnName("NAZVRSUBJ");
    jlrNazvrsub.setSearchMode(1);

    jlrZnac.setHorizontalAlignment(SwingConstants.TRAILING);
    jlrZnac.setColumnName("CZNAC");
    jlrZnac.setDataSet(mast);
    jlrZnac.setColNames(new String[] {"ZNACOPIS"});
    jlrZnac.setTextFields(new javax.swing.text.JTextComponent[] {jlrNaznac});
    jlrZnac.setVisCols(new int[] {0,1});
    jlrZnac.setSearchMode(0);
    jlrZnac.setRaDataSet(getZnac());
    jlrZnac.setNavButton(jbZnac);

    jlrNaznac.setNavProperties(jlrZnac);
    jlrNaznac.setDataSet(mast);
    jlrNaznac.setColumnName("ZNACOPIS");
    jlrNaznac.setSearchMode(1);

    jpDetail.setLayout(xYLayout2);
    xYLayout2.setWidth(500);
    xYLayout2.setHeight(85);

    jlVri.setText("Vrijednost");
    jlOpis.setText("Opis vrijednosti");
    jraVri.setDataSet(this.getDetailSet());
    jraVri.setColumnName("VRIZNAC");
    jraOpis.setDataSet(this.getDetailSet());
    jraOpis.setColumnName("OPIS");

    jpMaster.add(jlVrsub, new XYConstraints(15, 20, -1, -1));
    jpMaster.add(jlZnac, new XYConstraints(15, 45, -1, -1));
    jpMaster.add(jlrVrsub, new XYConstraints(150, 20, 75, -1));
    jpMaster.add(jlrZnac, new XYConstraints(150, 45, 75, -1));
    jpMaster.add(jlrNaznac, new XYConstraints(230, 45, 275, -1));
    jpMaster.add(jlrNazvrsub, new XYConstraints(230, 20, 275, -1));
    jpMaster.add(jbVrsub, new XYConstraints(510, 20, 21, 21));
    jpMaster.add(jbZnac, new XYConstraints(510, 45, 21, 21));

    jpDetail.add(jraVri, new XYConstraints(150, 20, 330, -1));
    jpDetail.add(jraOpis, new XYConstraints(150, 45, 330, -1));
    jpDetail.add(jlVri, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 45, -1, -1));

//    raMaster.setEditEnabled(false);
    raMaster.disableAdd();

    this.setJPanelMaster(jpMaster);
    this.setJPanelDetail(jpDetail);
  }

  /*private QueryDataSet getVrsub() {
    vl.execSQL("SELECT cvrsubj, MAX(nazvrsubj) as nazvrsubj FROM RN_znacajke GROUP BY cvrsubj");
    vl.RezSet.refresh();
    return vl.RezSet;
  }*/

  private QueryDataSet getZnac() {
    vl.execSQL("SELECT cznac, MAX(znacopis) as znacopis FROM RN_znacajke GROUP BY cznac");
    vl.RezSet.open();
    return vl.RezSet;
  }
}
