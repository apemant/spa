/****license*****************************************************************
**   file: frmAkcije.java
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

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Akcije;
import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Stakcije;
import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raOptionDialog;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.*;
import hr.restart.util.Util;


public class frmAkcije extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  
  String[] mkey = {"CAK"};
  
  static frmAkcije frm;
  
  jpAkcijeMaster jpMaster;
  JPanel jpDetail;
  
  JLabel jlMC = new JLabel();
  JraTextField jraMC = new JraTextField();
  
  protected rapancart rpc = new rapancart() {
    public void metToDo_after_lookUp() {
      if (!rpcLostFocus && raDetail.getMode() == 'N') {
        rpcLostFocus = true;
        rpcOut();
      };
    }
  };

  protected boolean rpcLostFocus;
  
  raTwoTableChooser ttc = new raTwoTableChooser();
  raOptionDialog ttd = new raOptionDialog();
  
  StorageDataSet tcleft, tcright;
  
  public frmAkcije() {
    super(1,3);
    frm = this;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void beforeShowMaster() {
    this.getDetailSet().open();
    initRpcart();
  }
  
  public void EntryPointMaster(char mode) {
    if (mode == 'I')
      rcc.setLabelLaF(jpMaster.jraCAK, false);
  }
  
  public void SetFokusMaster(char mode) {
    if (mode == 'I')
      jpMaster.jraCAK.requestFocusLater();
    else if (mode == 'N')
      jpMaster.jraPOP.requestFocusLater();
  }
  
  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jraCAK) || 
        vl.isEmpty(jpMaster.jraNAZAK)) return false;
    
    if (jpMaster.jraDatum.isSelected()) {
      if (vl.isEmpty(jpMaster.jraDATOD) || 
          vl.isEmpty(jpMaster.jraDATDO)) return false;
      if (getMasterSet().getTimestamp("DATDO").before(Util.getUtil().
          getFirstSecondOfDay(getMasterSet().getTimestamp("DATOD")))) {
        jpMaster.jraDATOD.requestFocus();
        JOptionPane.showMessageDialog(raMaster.getWindow(),
          "Poèetni datum iza završnog!",
          "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    } else {
      if (vl.isEmpty(jpMaster.jraVRIOD) || 
          vl.isEmpty(jpMaster.jraVRIDO)) return false;
      if (getMasterSet().getInt("VRIOD") > 24) {
        jpMaster.jraVRIOD.requestFocus();
        JOptionPane.showMessageDialog(raMaster.getWindow(),
          "Pogrešna vrijednost za sat!",
          "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (getMasterSet().getInt("VRIDO") > 24) {
        jpMaster.jraVRIOD.requestFocus();
        JOptionPane.showMessageDialog(raMaster.getWindow(),
          "Pogrešna vrijednost za sat!",
          "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    return true;
  }
  
  protected void EraseFields() {
    rpcLostFocus = false;
    rcc.EnabDisabAll(jpDetail, false);
  }
  
  protected boolean rpcOut() {
    // provjeri nalazi li ve\u0107 u dokumentu isti artikl
    if (artNotUnique(rpc.getCART())) {
      EraseFields();
      Aut.getAut().handleRpcErr(rpc, "Artikl veæ u tablici!");
      return false;
    }
    rcc.EnabDisabAll(jpDetail, true);
    
    BigDecimal c = Aus.zero0;
    if (ld.raLocate(dm.getArtikli(), "CART", rpc.getCART())) {
      c = dm.getArtikli().getBigDecimal("MC");
    }
    getDetailSet().setBigDecimal("MC", c.multiply(Aus.one0.
        movePointRight(2).subtract(getMasterSet().getBigDecimal("PPOP"))).
        movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP));
    jraMC.requestFocusLater();
    return true;
  }
  
  protected boolean artNotUnique(String art) {
    vl.execSQL("SELECT * FROM stakcije WHERE cak='"+
        getMasterSet().getString("CAK") + "' and cart = " + art);
    vl.RezSet.open();
    return (vl.RezSet.rowCount() > 0);
  }
  
  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      rpc.setCART();
      EraseFields();
    } else if (mode == 'I' ){
      jraMC.requestFocus();
    }
  }
  
  public boolean ValidacijaDetail(char mode) {
    if (rpc.getCART().equals("")) {
      EraseFields();
      JOptionPane.showMessageDialog(this.jpDetail,"Obavezan unos Artikla!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      EraseFields();
      return false;
    }
    if (mode == 'N' && artNotUnique(rpc.getCART())) {
      EraseFields();
      JOptionPane.showMessageDialog(this.jpDetail,"Artikl veæ u tablici!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      EraseFields();
      rpc.setCART();
      return false;
    }
    
    return true;
  }
  
  public boolean ValDPEscapeDetail(char mode) {
    if (mode == 'N' && rpcLostFocus) {
      rpc.EnabDisab(true);
      rpc.setCART();
      EraseFields();
      return false;
    }
    return true;
  }
  
  public void AfterSaveDetail(char mode) {
    if (mode == 'N') {
      rpc.EnabDisab(true);
    }
  }
  
  void multiArt() {
    JPanel pan = new JPanel(new BorderLayout());
    pan.add(ttc);
    pan.add(ttd.getOkPanel(), BorderLayout.SOUTH);
    
    tcleft.empty();
    tcright.empty();
    DataSet art = dm.getArtikli();
    DataSet bef = Stakcije.getDataModule().getTempSet(
        Condition.equal("CAK", getMasterSet()));
    bef.open();
    HashSet old = new HashSet();
    for (bef.first(); bef.inBounds(); bef.next())
      old.add(new Integer(bef.getInt("CART")));
    
    String[] cc = {"CART", "CART1", "BC", "NAZART", "JM", "VRART", "CGRART"};
    for (art.first(); art.inBounds(); art.next())
      if (!old.contains(new Integer(art.getInt("CART")))) {
        tcleft.insertRow(false);
        dM.copyColumns(art, tcleft, cc);
        tcleft.post();
      }
        
    ttc.initialize();
    if (ttd.show(raDetail.getWindow(), pan, "Odabir artikala")) {
      
    }
  }
  
  private void jbInit() throws Exception {
    this.setUserCheck(true);
    this.setMasterSet(Akcije.getDataModule().getQueryDataSet());
    this.setNaslovMaster("Akcije i happy hour");
    this.setVisibleColsMaster(new int[] {0, 1, 2, 3, 4});
    this.setMasterKey(mkey);
    jpMaster = new jpAkcijeMaster(this);
    this.setJPanelMaster(jpMaster);
    this.setMasterDeleteMode(DELDETAIL);
    
    this.setDetailSet(Stakcije.getDataModule().getFilteredDataSet(Condition.nil));
    this.setNaslovDetail("Artikli akcije");
    this.setVisibleColsDetail(new int[] {1, 2});
    this.setDetailKey(new String[] {"CAK", "CART"});
    this.raDetail.getJpTableView().addTableModifier(
        new raTableColumnModifier("CART", 
            new String[] {Aut.getAut().getIndiCART(), "NAZART"}, 
            dm.getArtikli()));
    /*this.raDetail.addOption(new raNavAction("Dodaj artikle", raImages.IMGIMPORT, KeyEvent.VK_F7) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        multiArt();
      }
    },4);*/
    
    JPanel main = new JPanel();
    
    main.setLayout(new BorderLayout());
    main.add(rpc, BorderLayout.NORTH);
    
    jlMC.setText("Cijena");
    jraMC.setColumnName("MC");
    jraMC.setDataSet(getDetailSet());
    
    jpDetail = new JPanel(new XYLayout(400, 55));
    jpDetail.add(jlMC, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraMC, new XYConstraints(150, 20, 100, -1));
    
    main.add(jpDetail);
    
    this.setJPanelDetail(main);
    
    setupChooser();
  }
  
  void setupChooser() {
    tcleft = Artikli.getDataModule().getScopedSet("CART CART1 BC NAZART JM VRART CGRART");
    tcleft.setTableName("artikli-akcije");
    tcleft.open();
    tcright = Artikli.getDataModule().getScopedSet("CART CART1 BC NAZART JM VRART CGRART");
    tcright.setTableName("artikli-akcije");
    tcright.open();
    ttc.setLeftDataSet(tcleft);
    ttc.setRightDataSet(tcright);
    
  }
  
  protected void initRpcart() {
    rpc.setMode("DOH");
    rpc.setDefParam();
    rpc.InitRaPanCart();
    rpc.jrfCART.setDataSet(getDetailSet());
  }
}
