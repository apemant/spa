/****license*****************************************************************
**   file: FrmNerealiziraniDok.java
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
import hr.restart.util.raComboBox;
import hr.restart.util.raUpit;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author S.G.
 *
 * Started 2005.03.18
 * 
 */

public class FrmNerealiziraniDok extends raUpit {

  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  
  // DATA komponente

  TableDataSet tds = new TableDataSet();
  QueryDataSet repSet = null;
  
  // GUI components
  
  JPanel jpan = new JPanel();
  JPanel upper = new JPanel();
  
  raComboBox rcbVrstaDok = new raComboBox();
  raComboBox rcbStatusDok = new raComboBox();
  JraTextField jtfDatumDo = new JraTextField();
  JLabel jlDokDat = new JLabel("Dokumenti / Do datuma");

  JraButton jbCorg = new JraButton();
  JlrNavField jlrCorg = new JlrNavField();
  JlrNavField jlrNazOrg = new JlrNavField();
  
  
  rapancskl1 rpcskl = new rapancskl1(false,349);
  
  XYLayout mainLayout = new XYLayout();
  
  boolean noData = false;
  
  private static FrmNerealiziraniDok instanceOfMe = null;
  
  public static FrmNerealiziraniDok getInstance(){
    return instanceOfMe;
  }
  
  public FrmNerealiziraniDok(){
    try {
      dataInitialize();
      guiInitialize();
      instanceOfMe = this;
    } catch (Exception initializeException) {
      initializeException.printStackTrace();
    } 
  }

  //initialize graphics
  protected void guiInitialize() throws Exception {
    this.setJPan(jpan);
    mainLayout.setHeight(115);
    mainLayout.setWidth(650);
    jpan.setLayout(mainLayout);
    jpan.add(new JLabel("Šifra"), new XYConstraints(150,8,-1,-1));
    jpan.add(new JLabel("Naziv"), new XYConstraints(255,8,-1,-1));
    jpan.add(new JLabel("Org. jedinica"), new XYConstraints(15, 25, -1, -1));
    jpan.add(jlrCorg, new XYConstraints(150, 25, 100, -1));
    jpan.add(jlrNazOrg, new XYConstraints(255, 25, 350, -1));
    jpan.add(jbCorg, new XYConstraints(610, 25, 21, 21));
    jpan.add(rpcskl, new XYConstraints(0, 50, -1, -1));
    jpan.add(jlDokDat, new XYConstraints(15, 75, -1, -1));
    jpan.add(rcbStatusDok, new XYConstraints(150, 75, 170, -1));
    jpan.add(rcbVrstaDok, new XYConstraints(325, 75, 175, -1));
    jpan.add(jtfDatumDo, new XYConstraints(505, 75, 100, -1));
  }
  
  //initialize data
  private void dataInitialize() throws Exception {
    addReport("hr.restart.robno.repUnRealDoc", "Ispis nerealiziranih dokumenata", 5);
    tds.setColumns(new Column[] {
        dm.createStringColumn("CORG",8),
        dm.createStringColumn("VRDOK",3),
        dm.createStringColumn("STATUS",1),
        dm.createTimestampColumn("DATDO")
    });
    tds.open();
    
    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(tds);
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazOrg});
    jlrCorg.setVisCols(new int[] {0,1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbCorg);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String newk, String oldk) {
        jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });

    jlrNazOrg.setColumnName("NAZIV");
    jlrNazOrg.setNavProperties(jlrCorg);
    jlrNazOrg.setSearchMode(1);
    
    setVrstaDokumenata();

    rcbStatusDok.setRaDataSet(tds);
    rcbStatusDok.setColumnName("STATUS");
    rcbStatusDok.setRaItems(new String[][] {
        {"Svi dokumenti","A"},
        {"Dospjeli dokumenti","D"},
        {"Nedospjeli dokumenti","N"}
    });
    
    jtfDatumDo.setDataSet(tds);
    jtfDatumDo.setColumnName("DATDO");
    
//    rpcskl.setRaMode('S');
    rpcskl.setCSKL("");
    
    this.getJPTV().addTableModifier(new hr.restart.swing.raTableColumnModifier("CPAR", new String[] {
		"CPAR", "NAZPAR" }, new String[] { "CPAR" }, dm.getPartneri()) {
		public int getMaxModifiedTextLength() {
			return 27;
		}
	});
  }

  protected void setVrstaDokumenata() {
    rcbVrstaDok.setRaDataSet(tds);
    rcbVrstaDok.setColumnName("VRDOK");
    rcbVrstaDok.setRaItems(new String[][]{
        {"Narudžbe kupca","NKU"},
        {"Narudžbe dobavljaèu","NDO"},
        {"Raèun za predujam","PRD"},
        {"Ponude","PON"}
    });
  }

  public void okPress() {
//    System.out.println("UPIT : "+getSqlUpit()); //XDEBUG delete when no more needed
    
    repSet = ut.getNewQueryDataSet(getSqlUpit(),false);
    repSet.setColumns(new Column[]{
        dm.createStringColumn("CSKL","Skladište / OJ", 8),
//        (Column) dm.getDoki().getColumn("CSKL").clone(),
        (Column) dm.getDoki().getColumn("VRDOK").clone(),
        (Column) dm.getDoki().getColumn("GOD").clone(),
        (Column) dm.getDoki().getColumn("BRDOK").clone(),
        (Column) dm.getDoki().getColumn("CPAR").clone(),
        dm.createTimestampColumn("DATDOK","Datum"),
        dm.createTimestampColumn("DATDOSP","Dospjeæe"),
//        (Column) dm.getDoki().getColumn("DATDOK").clone(),
//        (Column) dm.getDoki().getColumn("DATDOSP").clone(),
        (Column) dm.getDoki().getColumn("UIRAC").clone()
    });
    
    repSet.getColumn("VRDOK").setVisible(0);
    repSet.getColumn("GOD").setVisible(0);
    
//    if (tds.getString("STATUS").equals("D") || tds.getString("STATUS").equals("N")) {
//      repSet.getColumn("DATDOK").setVisible(0);
//      repSet.getColumn("DATDOSP").setVisible(1);
//    } else {
//      repSet.getColumn("DATDOK").setVisible(1);
//      repSet.getColumn("DATDOSP").setVisible(0);
//    }
    
    repSet.open();
    
    

    if (repSet == null || repSet.rowCount() < 1) {
      noData=true;
      setNoDataAndReturnImmediately();
    }
    getJPTV().setDataSet(repSet);
    
//    sysoutTEST st = new sysoutTEST(false);
//    st.prn(repSet);
  }
  
  protected String getSqlUpit() {
    String dospjece = "and DOKI.DATDOK <= '"+ut.getLastSecondOfDay(tds.getTimestamp("DATDO"))+"' ";//"DATDOK";
    if (tds.getString("STATUS").equals("D")) dospjece = "and DOKI.DATDOSP <= '"+ut.getLastSecondOfDay(tds.getTimestamp("DATDO"))+"' ";//"DATDOSP";
    if (tds.getString("STATUS").equals("N")) dospjece = "and DOKI.DATDOSP > '"+ut.getLastSecondOfDay(tds.getTimestamp("DATDO"))+"' ";
    
    String cskl = "";
    
    if (tds.getString("CORG").equals("")){
      if (!rpcskl.getCSKL().equals("")){
       cskl = "and cskl = '"+rpcskl.getCSKL()+"' "; 
      }
    } else {
      if (rpcskl.getCSKL().equals("")){
        cskl = "and cskl = '"+tds.getString("CORG")+"' ";  
      } else {
        cskl = "and cskl in ('"+rpcskl.getCSKL()+"','"+tds.getString("CORG")+"') ";
      }
    }
    
    String upit = "select  DOKI.CSKL, DOKI.VRDOK, DOKI.GOD, DOKI.BRDOK, " +
    		"DOKI.CPAR, " +
    		"DOKI.DATDOK, DOKI.DATDOSP, DOKI.UIRAC " +
    		"from DOKI " +
    		"where DOKI.AKTIV='D' " + cskl +
    		"and DOKI.GOD = '"+ut.getYear(tds.getTimestamp("DATDO"))+"' " +
    		dospjece +
    		vrdok() + " order by doki.brdok";
    System.out.println("\n"+upit+"\n"); //XDEBUG delete when no more needed
    return upit;
  }
  
  protected String vrdok(){
    String vrsta = tds.getString("VRDOK");
    if (vrsta.equals("NKU")) return "AND DOKI.VRDOK = 'NKU' and doki.statira = 'N'";
    if (vrsta.equals("NDO")) return "AND DOKI.VRDOK = 'NDO' and doki.statira = 'N'";
    if (vrsta.equals("PON")) return "AND DOKI.VRDOK = 'PON' and doki.statira = 'N'";
    return "AND DOKI.VRDOK = 'PRD'"; 
  }

  public void firstESC() {
    rcc.EnabDisabAll(this.rpcskl, true);
    rcc.EnabDisabAll(jpan, true);
    if (noData) {
      jlrCorg.requestFocus();
      noData = false;
    } else {
      if (getJPTV().getDataSet() == null && !rpcskl.getCSKL().equals("")) {
        rpcskl.setCSKL("");
        rpcskl.jrfCSKL.requestFocus();
      } else if (getJPTV().getDataSet() == null && !tds.getString("CORG").equals("")) {
        jlrCorg.setText("");
        jlrCorg.emptyTextFields();
        jlrCorg.requestFocus();
      } if (getJPTV().getDataSet() != null) {
        getJPTV().setDataSet(null);
        if (rpcskl.getCSKL().equals(""))
          rpcskl.jrfCSKL.requestFocus();
        else if (tds.getString("CORG").equals(""))
          jlrCorg.requestFocus();
        else
          jlrCorg.requestFocus();
      }
    }
  }

  public boolean runFirstESC() {
    if (getJPTV().getDataSet() == null && !(!rpcskl.getCSKL().equals("") || !tds.getString("CORG").equals(""))) return false;
    return true;
  }

  public void componentShow() {
    showDefaultValues();
  }
  
  protected void showDefaultValues() {
    jlrCorg.setText(hr.restart.zapod.OrgStr.getOrgStr().getKNJCORG());
    jlrCorg.forceFocLost();
    rcc.EnabDisabAll(jpan, true);
    rpcskl.setCSKL("");
    tds.setTimestamp("DATDO", hr.restart.util.Valid.getValid().findDate(false, 0));
    getJPTV().setDataSet(null);
    rcbStatusDok.setSelectedIndex(0);
    rcbVrstaDok.setSelectedIndex(0);
    tds.setString("VRDOK","NKU");
    tds.setString("STATUS","A");
    rpcskl.jrfCSKL.requestFocus();
  }

  public DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }
  
  public boolean Validacija() {
    if (tds.getString("CORG").equals("") && rpcskl.getCSKL().equals("")){
      jlrCorg.requestFocus();
      JOptionPane.showMessageDialog(this.getJPan(),
          "Obavezan unos org. jedinice ili skladišta!","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
}