/****license*****************************************************************
**   file: frmRabatShema.java
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Rabshema;
import hr.restart.baza.dM;
import hr.restart.baza.rabati;
import hr.restart.sisfun.Asql;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raTransaction;


public class frmRabatShema extends raMasterFakeDetailArtikl {
	raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.util.lookupData ld = hr.restart.util.lookupData.getlookupData();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  static frmRabatShema frs;
  
  JPanel jpPres = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlPartner = new JLabel();
  JPanel jpRab = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  
  JlrNavField jlrPart = new JlrNavField();
  JlrNavField jlrNazPart = new JlrNavField();
  JraButton jbSelPart = new JraButton();
  
  JraTextField jraVC = new JraTextField();
  JraTextField jraMC = new JraTextField();
  
  JlrNavField jlrRAB1 = new JlrNavField();
  JlrNavField jlrNRAB1 = new JlrNavField();
  JlrNavField jlrIRAB1 = new JlrNavField();
  JraButton jbSelRab1 = new JraButton();
  
  JlrNavField jlrRAB2 = new JlrNavField();
  JlrNavField jlrNRAB2 = new JlrNavField();
  JlrNavField jlrIRAB2 = new JlrNavField();
  JraButton jbSelRab2 = new JraButton();
  
  JlrNavField jlrRAB3 = new JlrNavField();
  JlrNavField jlrNRAB3 = new JlrNavField();
  JlrNavField jlrIRAB3 = new JlrNavField();
  JraButton jbSelRab3 = new JraButton();
  
  JraCheckBox jcbGrupa = new JraCheckBox();
  
  JraCheckBox jcbAkcija = new JraCheckBox();
  JraTextField jraDATOD = new JraTextField();
  JraTextField jraDATDO = new JraTextField();
  
  JLabel jlOverride = new JLabel();
  JraCheckBox jcbUR1 = new JraCheckBox();
  JraCheckBox jcbUR2 = new JraCheckBox();
  JraCheckBox jcbUR3 = new JraCheckBox();
  
	
  String[] key = new String[] {"CPAR"};
  String[] keyd = new String[] {"CPAR", "CART"};
  
  public frmRabatShema(){
    try {
      frs = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmRabatShema getInstance() {
    return frs;
  }
  
  private void jbInit() throws Exception {
  	Asql.createMasterRab(mast);
  	dm.getRabati().open();
  	
  	this.setMasterSet(mast);
    this.setNaslovMaster("Rabati za partnere");
    this.setVisibleColsMaster(new int[] {0,1,2,3});
    this.setMasterKey(key);

    this.setDetailSet(Rabshema.getDataModule().getQueryDataSet());
    this.setNaslovDetail("Artikli s rabatima");
    this.setVisibleColsDetail(new int[] {Aut.getAut().getCARTdependable(1,2,3),4,6});
    this.setDetailKey(keyd);
    
    jlPartner.setText("Poslovni partner");
    xYLayout1.setWidth(576);
    xYLayout1.setHeight(110);
    jpPres.setLayout(xYLayout1);
    jbSelPart.setText("...");

    jlrPart.setColumnName("CPAR");
    jlrPart.setTextFields(new JTextComponent[] {jlrNazPart});
    jlrPart.setColNames(new String[] {"NAZPAR"});
    jlrPart.setSearchMode(0);
    jlrPart.setDataSet(this.getMasterSet());
    jlrPart.setRaDataSet(dm.getPartneri());
    jlrPart.setVisCols(new int[] {0,1});
    jlrPart.setNavButton(jbSelPart);

    jlrNazPart.setColumnName("NAZPAR");
    jlrNazPart.setNavProperties(jlrPart);
    jlrNazPart.setDataSet(this.getMasterSet());
    jlrNazPart.setSearchMode(1);
    
    jcbAkcija.setDataSet(this.getMasterSet());
    jcbAkcija.setColumnName("AKCIJA");
    jcbAkcija.setHorizontalTextPosition(SwingConstants.TRAILING);
    jcbAkcija.setHorizontalAlignment(SwingConstants.LEADING);
    jcbAkcija.setText(" Akcija (od - do) ");
    jcbAkcija.setSelectedDataValue("D");
    jcbAkcija.setUnselectedDataValue("N");
    
    jraDATOD.setColumnName("DATOD");
    jraDATOD.setDataSet(this.getMasterSet());
    jraDATDO.setColumnName("DATDO");
    jraDATDO.setDataSet(this.getMasterSet());
    
    jpRab.setLayout(xYLayout2);
    xYLayout2.setWidth(630);
    xYLayout2.setHeight(200);
    
    jraVC.setColumnName("VC");
    jraVC.setDataSet(this.getDetailSet());
    
    jraMC.setColumnName("MC");
    jraMC.setDataSet(this.getDetailSet());
    
    jlrRAB1.setColumnName("CRAB1");
    jlrRAB1.setNavColumnName("CRAB");
    jlrRAB1.setTextFields(new JTextComponent[] {jlrNRAB1, jlrIRAB1});
    jlrRAB1.setColNames(new String[] {"NRAB", "PRAB"});    
    jlrRAB1.setSearchMode(0);
    jlrRAB1.setDataSet(this.getDetailSet());
    jlrRAB1.setRaDataSet(rabati.getDataModule().copyDataSet());
    jlrRAB1.setVisCols(new int[] {0,1});
    jlrRAB1.setNavButton(jbSelRab1);
    
    jlrNRAB1.setColumnName("NRAB");
    jlrNRAB1.setNavProperties(jlrRAB1);
    //jlrNRAB1.setDataSet(this.getDetailSet());
    jlrNRAB1.setSearchMode(1);
    
    jlrIRAB1.setColumnName("PRAB");
    jlrIRAB1.setNavProperties(jlrRAB1);
    //jlrIRAB1.setDataSet(this.getDetailSet());
    jlrIRAB1.setSearchMode(1);
    
    jlrRAB2.setColumnName("CRAB2");
    jlrRAB2.setNavColumnName("CRAB");
    jlrRAB2.setTextFields(new JTextComponent[] {jlrNRAB2, jlrIRAB2});
    jlrRAB2.setColNames(new String[] {"NRAB", "PRAB"});
    jlrRAB2.setSearchMode(0);
    jlrRAB2.setDataSet(this.getDetailSet());
    jlrRAB2.setRaDataSet(rabati.getDataModule().copyDataSet());
    jlrRAB2.setVisCols(new int[] {0,1});
    jlrRAB2.setNavButton(jbSelRab2);
    
    jlrNRAB2.setColumnName("NRAB");
    jlrNRAB2.setNavProperties(jlrRAB2);
    //jlrNRAB2.setDataSet(this.getDetailSet());
    jlrNRAB2.setSearchMode(1);
    
    jlrIRAB2.setColumnName("PRAB");
    jlrIRAB2.setNavProperties(jlrRAB2);
    //jlrIRAB2.setDataSet(this.getDetailSet());
    jlrIRAB2.setSearchMode(1);
    
    jlrRAB3.setColumnName("CRAB3");
    jlrRAB3.setNavColumnName("CRAB");
    jlrRAB3.setTextFields(new JTextComponent[] {jlrNRAB3, jlrIRAB3});
    jlrRAB3.setColNames(new String[] {"NRAB", "PRAB"});
    jlrRAB3.setSearchMode(0);
    jlrRAB3.setDataSet(this.getDetailSet());
    jlrRAB3.setRaDataSet(rabati.getDataModule().copyDataSet());
    jlrRAB3.setVisCols(new int[] {0,1});
    jlrRAB3.setNavButton(jbSelRab3);
    
    jlrNRAB3.setColumnName("NRAB");
    jlrNRAB3.setNavProperties(jlrRAB3);
    //jlrNRAB3.setDataSet(this.getDetailSet());
    jlrNRAB3.setSearchMode(1);
    
    jlrIRAB3.setColumnName("PRAB");
    jlrIRAB3.setNavProperties(jlrRAB3);
    //jlrIRAB3.setDataSet(this.getDetailSet());
    jlrIRAB3.setSearchMode(1);
    
    jcbGrupa.setDataSet(this.getDetailSet());
    jcbGrupa.setColumnName("ALLGR");
    jcbGrupa.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbGrupa.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbGrupa.setText(" Popust za sve artikle iz grupe ");
    jcbGrupa.setSelectedDataValue("D");
    jcbGrupa.setUnselectedDataValue("N");
    
    jlOverride.setText("Vrijedi uvijek?");
    jlOverride.setHorizontalAlignment(SwingConstants.TRAILING);
    
    jcbUR1.setDataSet(this.getDetailSet());
    jcbUR1.setColumnName("UR1");
    jcbUR1.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbUR1.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbUR1.setText("");
    jcbUR1.setSelectedDataValue("D");
    jcbUR1.setUnselectedDataValue("N");
    
    jcbUR2.setDataSet(this.getDetailSet());
    jcbUR2.setColumnName("UR2");
    jcbUR2.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbUR2.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbUR2.setText("");
    jcbUR2.setSelectedDataValue("D");
    jcbUR2.setUnselectedDataValue("N");
    
    jcbUR3.setDataSet(this.getDetailSet());
    jcbUR3.setColumnName("UR3");
    jcbUR3.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbUR3.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbUR3.setText("");
    jcbUR3.setSelectedDataValue("D");
    jcbUR3.setUnselectedDataValue("N");
    
    
    jpPres.add(jlPartner, new XYConstraints(15, 20, -1, -1));
    jpPres.add(jlrPart, new XYConstraints(150, 20, 100, -1));
    jpPres.add(jlrNazPart, new XYConstraints(255, 20, 280, -1));
    jpPres.add(jbSelPart, new XYConstraints(540, 20, 21, 21));
    jpPres.add(jcbAkcija, new XYConstraints(15, 50, -1, -1));
    jpPres.add(jraDATOD, new XYConstraints(150, 50, 100, -1));
    jpPres.add(jraDATDO, new XYConstraints(255, 50, 100, -1));
    
    jpRab.add(jcbGrupa, new XYConstraints(200, 15, 385, -1));
    jpRab.add(new JLabel("Cijena bez poreza"),  new XYConstraints(300, 45, -1, -1));
    jpRab.add(jraVC, new XYConstraints(485, 45, 100, -1));
    jpRab.add(new JLabel("Cijena s porezom"),  new XYConstraints(300, 70, -1, -1));
    jpRab.add(jraMC, new XYConstraints(485, 70, 100, -1));
    
    jpRab.add(jlOverride, new XYConstraints(15, 70, 130, -1));
    
    jpRab.add(new JLabel("Rabat 1"),  new XYConstraints(15, 100, -1, -1));
    jpRab.add(jcbUR1, new XYConstraints(100, 100, 45, -1));
    jpRab.add(jlrRAB1, new XYConstraints(150, 100, 75, -1));
    jpRab.add(jlrNRAB1, new XYConstraints(230, 100, 250, -1));
    jpRab.add(jlrIRAB1, new XYConstraints(485, 100, 100, -1));
    jpRab.add(jbSelRab1, new XYConstraints(590, 100, 21, 21));
    jpRab.add(new JLabel("Rabat 2"),  new XYConstraints(15, 125, -1, -1));
    jpRab.add(jcbUR2, new XYConstraints(100, 125, 45, -1));
    jpRab.add(jlrRAB2, new XYConstraints(150, 125, 75, -1));
    jpRab.add(jlrNRAB2, new XYConstraints(230, 125, 250, -1));
    jpRab.add(jlrIRAB2, new XYConstraints(485, 125, 100, -1));
    jpRab.add(jbSelRab2, new XYConstraints(590, 125, 21, 21));
    jpRab.add(new JLabel("Rabat 3"),  new XYConstraints(15, 150, -1, -1));
    jpRab.add(jcbUR3, new XYConstraints(100, 150, 45, -1));
    jpRab.add(jlrRAB3, new XYConstraints(150, 150, 75, -1));
    jpRab.add(jlrNRAB3, new XYConstraints(230, 150, 250, -1));
    jpRab.add(jlrIRAB3, new XYConstraints(485, 150, 100, -1));
    jpRab.add(jbSelRab3, new XYConstraints(590, 150, 21, 21));
    
    this.SetPanels(jpPres, jpRab, true);
    
    jcbAkcija.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("klik!");
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            if (!getMasterSet().getString("AKCIJA").equals("D")) {
              getMasterSet().setAssignedNull("DATOD");
              getMasterSet().setAssignedNull("DATDO");
            }
            updAkcija();
          }
        });
      }
    });
    
    jcbGrupa.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            updGrupa();
          }
        });
      }
    });
  }
  
  public void ClearFields() {
  	this.getDetailSet().setBigDecimal("VC", _Main.nul);
  	this.getDetailSet().setBigDecimal("MC", _Main.nul);

  	jlrRAB1.emptyTextFields();
  	jlrRAB2.emptyTextFields();
  	jlrRAB3.emptyTextFields();
  }
  
  void updAkcija() {
    boolean akc = getMasterSet().getString("AKCIJA").equals("D");
    System.out.println("update " + akc);
    rcc.setLabelLaF(jraDATOD, akc);
    rcc.setLabelLaF(jraDATDO, akc);
    jlOverride.setVisible(akc);
    jcbUR1.setVisible(akc);
    jcbUR2.setVisible(akc);
    jcbUR3.setVisible(akc);
    jcbUR1.setEnabled(akc);
    jcbUR2.setEnabled(akc);
    jcbUR3.setEnabled(akc);
  }
  
  void updGrupa() {
    boolean gr = getDetailSet().getString("ALLGR").equals("D");
    rcc.setLabelLaF(jraVC, !gr);
    rcc.setLabelLaF(jraMC, !gr);
  }
  
  public void EntryPointMaster(char mode) {
    if (mode == 'I') {
      rcc.EnabDisabAll(this.getJPanelMaster(), false);
      rcc.setLabelLaF(jcbAkcija, true);
      updAkcija();
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jlrPart.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jlrPart))
      return false;
    if (mode == 'N' && MasterNotUnique()) {
      jlrPart.requestFocus();
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Partner ve� u tablici!", "Gre�ka", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (getMasterSet().getString("AKCIJA").equals("D")) {
      if (vl.isEmpty(jraDATOD) || vl.isEmpty(jraDATDO)) return false;
      if (getMasterSet().getTimestamp("DATDO").before(getMasterSet().getTimestamp("DATOD"))) {
        jraDATOD.requestFocus();
        JOptionPane.showMessageDialog(jraDATOD.getTopLevelAncestor(),
            "Po�etni datum je iza zavr�nog!", "Gre�ka", JOptionPane.ERROR_MESSAGE);
          return false;
      }
    }
    mast.post();
    refilterDetailSet();
    return true;
  }
  
  public boolean doWithSaveMaster(char mode) {
    if (mode == 'I') {
      String[] cc = {"AKCIJA", "DATOD", "DATDO"};
      for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) 
        dM.copyColumns(mast, getDetailSet(), cc);
      raTransaction.saveChanges(getDetailSet());
    }
    return true;
  }
  
  int delCart;
  public boolean DeleteCheckDetail() {
    delCart = getDetailSet().getInt("CART");
    return true;
  }
  
  public void AfterDeleteDetail() {
    if (raWebSync.active && raWebSync.isWeb(delCart)) {
      raWebSync.updatePopust(Integer.toString(getMasterSet().getInt("CPAR")), Integer.toString(delCart), Aus.zero0);
    }
  }
  
  public void AfterSaveDetail(char mode) {
    super.AfterSaveDetail(mode);
    if (raWebSync.active && raWebSync.isWeb(delCart)) {
      DataSet rab = rabati.getDataModule().getQueryDataSet();
      
      BigDecimal r1 = Aus.zero0;
      BigDecimal r2 = Aus.zero0;
      BigDecimal r3 = Aus.zero0;
      if (getDetailSet().getString("CRAB1").length() > 0 &&
          ld.raLocate(rab, "CRAB", getDetailSet().getString("CRAB1")))
        r1 = rab.getBigDecimal("PRAB");
      if (getDetailSet().getString("CRAB2").length() > 0 &&
          ld.raLocate(rab, "CRAB", getDetailSet().getString("CRAB1")))
        r2 = rab.getBigDecimal("PRAB");
      if (getDetailSet().getString("CRAB3").length() > 0 &&
          ld.raLocate(rab, "CRAB", getDetailSet().getString("CRAB1")))
        r3 = rab.getBigDecimal("PRAB");
      
      BigDecimal uk = Aus.one0.subtract(r1.movePointLeft(2)).
      multiply(Aus.one0.subtract(r2.movePointLeft(2))).
      multiply(Aus.one0.subtract(r3.movePointLeft(2)));
      
      raWebSync.updatePopust(Integer.toString(getMasterSet().getInt("CPAR")), Integer.toString(delCart),
          Aus.one0.subtract(uk).movePointRight(2).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
  }
  
  public boolean Validacija(char mode) {
    delCart = getDetailSet().getInt("CART");
    dM.copyColumns(getMasterSet(), getDetailSet(), new String[] {"AKCIJA", "DATOD", "DATDO"});
    return true;
  }
  
  public void SetFokusIzmjena() {
    updGrupa();
  	jraVC.requestFocus();
  }
  
  protected boolean rpcOut() {
  	if (!super.rpcOut()) return false;
  	
  	if (ld.raLocate(dm.getArtikli(), "CART", getDetailSet())) {
  	  Aus.set(getDetailSet(), "VC", dm.getArtikli());
  	  Aus.set(getDetailSet(), "MC", dm.getArtikli());
  	}
  	
  	jraVC.requestFocus();
  	return true;
  }

  public boolean canDeleteMaster() {
    return true;
  }
  
  public String CheckMasterKeySQLString() {
      return "select * from rabshema where " +
      "cpar = " + mast.getInt("CPAR");
  }
  
  public void masterSet_navigated(NavigationEvent e) { 
    super.masterSet_navigated(e);
    updAkcija();
  }
  
  protected void initRpcart() {
    /*rpc.setGodina(vl.findYear(vl.getToday()));
    rpc.setCskl(dm.getCjenik().getString("CSKL"));*/
    rpc.setTabela(Rabshema.getDataModule().getQueryDataSet());
    rpc.setBorder(BorderFactory.createEtchedBorder());
    super.initRpcart();
    rpc.setAllowUsluga(true);
    //rpc.setnextFocusabile(this.jraPromjena);
  }
  
}