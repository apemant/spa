/****license*****************************************************************
**   file: frmArtNap.java
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

import hr.restart.baza.Artnap;
import hr.restart.baza.dM;
import hr.restart.sisfun.Asql;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmArtNap extends raMasterDetail {

  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JPanel jpMasterMain;
  JPanel jpDetailMain = new JPanel();
  JPanel jpDetail;
  
  JLabel jlGrupa = new JLabel();
  JLabel jlCAN = new JLabel();
  JLabel jlNAZAN = new JLabel();
  JraTextField jraCAN = new JraTextField();
  JraTextField jraNAZAN = new JraTextField();
  JLabel jlText = new JLabel();
  JraTextField jraTEXTNAP = new JraTextField();
  JraCheckBox jbPM = new JraCheckBox();
  
//  private String deleteSQL;
//  private boolean unlock = false;


  protected QueryDataSet mast = new QueryDataSet() {
//    public void saveChanges() {
//      this.post();
//    }
    public boolean saveChangesSupported() {
      return false;
    }
  };


  public frmArtNap() {
    super(1,2);
    try {
      this.setMasterDeleteMode(DELDETAIL);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void beforeShowMaster() {
    mast.refresh();
//    refilterDetailSet();
    this.getDetailSet().open();
  }

  
  public void EntryPointMaster(char mode) {
    if (mode == 'N') {
      rcc.EnabDisabAll(jpMasterMain, true);
    }
    if (mode == 'I') {
      rcc.EnabDisabAll(jpMasterMain, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      rcc.EnabDisabAll(jpMasterMain, true);
      jraCAN.requestFocusLater();
    }
  }
  
  public boolean ValidacijaMaster(char mode) {
    if (mode == 'N' && MasterNotUnique()) {
      jraCAN.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Normativ ve\u0107 postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    mast.post();
    return true;
  }
  
/*  public boolean DeleteCheckMaster() {
    deleteSQL = "";
    this.refilterDetailSet();
    if (this.getDetailSet().rowCount() > 0 && !canDeleteMaster()) {
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Nije mogu\u0107e brisati zaglavlje dok se ne pobrišu stavke!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    } else {
      if (this.getDetailSet().rowCount() > 0) {
        String selSQL = this.CheckMasterKeySQLString();
        deleteSQL = "DELETE" + selSQL.substring(selSQL.indexOf("*") + 1);
      }
      return true;
    }
  }

  public void AfterDeleteMaster() {
    if (!deleteSQL.equals(""))
      vl.runSQL(deleteSQL);
  } */

 /* public void AfterAfterSaveMaster(char mode) {
    super.AfterAfterSaveMaster(mode);
    raDetail.setLockedMode('0');
  }*/

  public void ZatvoriOstaloDetail() {
    int row = this.getMasterSet().getRow();
      this.getMasterSet().refresh();
      raMaster.getJpTableView().fireTableDataChanged();
      this.getMasterSet().goToClosestRow(row);
  }

  public void SetFokusDetail(char mode) {
    jraTEXTNAP.requestFocusLater();
  }

  public boolean ValidacijaDetail(char mode) {
    if (Valid.getValid().isEmpty(jraTEXTNAP)) return false;
    
    getDetailSet().setString("CAN", mast.getString("CAN"));
    getDetailSet().setString("NAZAN", mast.getString("NAZAN"));
    return true;
  }

  protected boolean MasterNotUnique() {
    vl.execSQL("SELECT * FROM artnap WHERE can = '" + mast.getString("CAN") + "'");
    vl.RezSet.open();
    return (vl.RezSet.rowCount() > 0);
  }

  /*public void handleError(String msg) {
    JlrNavField errf;
    if (rpc.getParam().equals("CART")) errf = rpc.jrfCART;
    else if (rpc.getParam().equals("CART1")) errf = rpc.jrfCART1;
    else errf = rpc.jrfBC;

    rpc.EnabDisab(true);
    //rpc.setCART();
    EraseFields();
    errf.setText("");
    errf.setErrText(msg);
    errf.this_ExceptionHandling(new Exception());
    errf.setErrText(null);
  }*/


  public void detailSet_navigated(NavigationEvent e) {

  }
  
  public void masterSet_navigated(NavigationEvent e) {
    //new Throwable().printStackTrace();
  }
  
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
    createMain(mast);

    this.setMasterSet(mast);
    this.setNaslovMaster("Grupe napomena");
    this.setVisibleColsMaster(new int[] {0, 1});
    this.setMasterKey(new String[] {"CAN"});

    this.setDetailSet(Artnap.getDataModule().getFilteredDataSet("1=0"));
    this.setNaslovDetail("Napomene grupe");
    this.setVisibleColsDetail(new int[] {0,2,3});
    this.setDetailKey(new String[] {"CAN", "TEXTNAP"});

    jlGrupa.setText("Grupa napomena");
    jlCAN.setText("Šifra");
    jlNAZAN.setText("Naziv");
    jraCAN.setDataSet(mast);
    jraCAN.setColumnName("CAN");
    jraNAZAN.setDataSet(mast);
    jraNAZAN.setColumnName("NAZAN");
    
    jlText.setText("Tekst napomene");
    jraTEXTNAP.setColumnName("TEXTNAP");
    jraTEXTNAP.setDataSet(getDetailSet());
    
    jbPM.setDataSet(getDetailSet());
    jbPM.setColumnName("PM");
    jbPM.setSelectedDataValue("D");
    jbPM.setUnselectedDataValue("N");
    jbPM.setText(" Plus/minus ");
    jbPM.setHorizontalTextPosition(JLabel.LEADING);
    jbPM.setHorizontalAlignment(JLabel.TRAILING);
    
    JPanel master = new JPanel(new XYLayout(500, 70));
    master.add(jlGrupa, new XYConstraints(15, 30, -1, -1));
    master.add(jlCAN, new XYConstraints(151, 10, -1, -1));
    master.add(jraCAN, new XYConstraints(150, 30, 75, -1));
    master.add(jlNAZAN, new XYConstraints(231, 10, -1, -1));
    master.add(jraNAZAN, new XYConstraints(230, 30, 250, -1));
    
    JPanel detail = new JPanel(new XYLayout(520, 60));
    detail.add(jlText, new XYConstraints(15, 20, -1, -1));
    detail.add(jraTEXTNAP, new XYConstraints(150, 20, 200, -1));
    detail.add(jbPM, new XYConstraints(355, 20, 150, -1));

    SetPanels(master, detail, false);
  }
  
  public static void createMain(QueryDataSet ds) {
    //vl.execSQL(sql);
    ds.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dM.getDataModule().getDatabase1(),
      "SELECT can, MAX(nazan) as nazan "+
      "FROM artnap GROUP BY can"
    ));
    //part = vl.RezSet;
    ds.setColumns(new Column[] {
        Artnap.getDataModule().getColumn("CAN").cloneColumn(),
        Artnap.getDataModule().getColumn("NAZAN").cloneColumn()
    });

    ds.open();
    ds.setRowId("CAN", true);
    ds.setRowId("NAZAN", false);
    ds.setTableName("artnap_master");
  }
}
