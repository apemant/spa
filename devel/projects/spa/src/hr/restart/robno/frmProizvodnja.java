/****license*****************************************************************
**   file: frmProizvodnja.java
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

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Rnser;
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.raImages;
import hr.restart.util.raLoader;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;
import hr.restart.util.raProcess;
import hr.restart.util.startFrame;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;


public class frmProizvodnja extends frmRadniNalog {
  static frmProizvodnja frmRNL;

  jpProizvodnjaMaster jpMaster;
  jpProizvodnjaDetail jpDetail;

  QueryDataSet repQDSprijava = new QueryDataSet();
  QueryDataSet repQDSnorm = new QueryDataSet();
  raNavAction potob;

  frmTableDataView viewReq = new frmTableDataView(false, false, true);
  frmTableDataView viewReal = new frmTableDataView(false, false, true);
  
  StorageDataSet realVa = new StorageDataSet();
  
  frmRnser rns;
  
  public static frmProizvodnja getInstance() {
    return frmRNL;
  }

  public frmProizvodnja() {
    try {
      frmRNL = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public QueryDataSet getrepQDSprijava() {
    return repQDSprijava;
 }

  public QueryDataSet getrepQDSnorm() {
    return repQDSnorm;
  }


  private void setTitle() {
    VarStr title = new VarStr();
    if (status.equals("P")) title.append("Otvoreni radni");
    else if (status.equals("O")) title.append("Obra\u0111eni radni");
    else if (status.equals("Z")) title.append("Zatvoreni radni");
    else if (status.equals("F")) title.append("Neizdani radni");
    else title.append("Radni");
    title.append(" nalozi za proizvodnju  od ");
    title.append(rdu.dataFormatter(dfrom)).append(" do ").append(rdu.dataFormatter(dto));
    setNaslovMaster(title.toString());
  }

  public void beforeShowMaster() {
    copyPreselectValues();
    setTitle();
    forceTotalExpand = frmParam.getParam("robno", "totalExpand", "N", 
        "Rastaviti normativ kod proizvodnje do kraja? (D/N)").equalsIgnoreCase("D");
  }
  
  protected void initNewMaster() {
    super.initNewMaster();
    this.getMasterSet().setString("SERPR", "P");
  }

  protected void setIzmjenaEntry() {
    jpMaster.setIzmjena();
    jpMaster.jtaRadovi.setText(this.getMasterSet().getString("OPIS"));
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      jpMaster.jlrCpar.requestFocus();
    } else {
      jpMaster.jpMasterHeader.SetDefTextDOK(mode);
      jpMaster.jtaRadovi.setText(this.getMasterSet().getString("OPIS"));
      if (mode == 'I') {
        jpMaster.jtaRadovi.requestFocus();
      }
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jlrCpar) || vl.isEmpty(jpMaster.jraDatum))
      return false;
    String opis = jpMaster.jtaRadovi.getText().substring(0,
        Math.min(200, jpMaster.jtaRadovi.getText().length()));
    this.getMasterSet().setString("OPIS", opis);
    if (mode == 'N')
      ValidacijaNoviMaster();
    return true;
  }

  public void beforeShowDetail() {
    setNaslovDetail("Stavke radnog naloga "+this.getMasterSet().getString("CRADNAL"));
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'N')
      jpDetail.EraseFields();
    if (mode == 'I')
      jpDetail.rpc.EnabDisab(false);
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      initNewDetail(this.getDetailSet());
      jpDetail.rpc.setCART();
    } else if (mode == 'I') {
      jpDetail.jraKol.requestFocus();
    }
  }

  public boolean ValidacijaDetail(char mode) {
    if (jpDetail.rpc.getCART().trim().length() == 0) {
      JOptionPane.showMessageDialog(this.jpDetail,"Obavezan unos Artikla!","Greška",
                                    JOptionPane.ERROR_MESSAGE);
      jpDetail.rpc.EnabDisab(true);
      jpDetail.rpc.setCART();
      return false;
    }
    int cart = Aus.getAnyNumber(jpDetail.rpc.getCART());
    if (!raVart.isNorma(cart) || !raVart.isStanje(cart)) {
    //if (!Aut.getAut().artTipa(Aut.getAut().getNumber(jpDetail.rpc.getCART()), "PU")) {
      JOptionPane.showMessageDialog(this.jpDetail,"Artikl nije proizvod!",
                  "Greška", JOptionPane.ERROR_MESSAGE);
      jpDetail.rpc.EnabDisab(true);
      jpDetail.rpc.setCART();
      return false;
    }
    if (vl.isEmpty(jpDetail.jraKol)) return false;
    if (mode == 'N')
      ValidacijaNoviDetail();
    return true;
  }

  public void AfterSaveDetail(char mode) {
    if (mode == 'N') {
      jpDetail.EraseFields();
      jpDetail.rpc.EnabDisab(true);
    }
  }

  public boolean ValDPEscapeDetail(char mode) {
    if (mode == 'N' && jpDetail.rpcLostFocus) {
      jpDetail.rpc.EnabDisab(true);
      jpDetail.EraseFields();
      jpDetail.rpc.setCART();
      return false;
    }
    return true;
  }

  public boolean rpcOut() {
    int cart = Aus.getAnyNumber(jpDetail.rpc.getCART());
    if (!raVart.isNorma(cart) || !raVart.isStanje(cart)) {
    //if (!Aut.getAut().artTipa(Aut.getAut().getNumber(jpDetail.rpc.getCART()), "PU")) {
      jpDetail.EraseFields();
      Aut.getAut().handleRpcErr(jpDetail.rpc, "Artikl nije proizvod!");
      return false;
    }
    jpDetail.EnableFields();
    jpDetail.jraKol.requestFocus();
    return true;
  }

  private void fisp() {
    String qstr = "SELECT * FROM RN where cradnal = '"+this.getMasterSet().getString("CRADNAL")+"'";
    repQDSprijava.close();
    repQDSprijava.closeStatement();
    repQDSprijava.setQuery(new QueryDescriptor(dm.getDatabase1(),qstr));
    repQDSprijava.open();

    String qstr2 = "SELECT * FROM stdoki WHERE vrdok = 'RNL' AND cradnal = '"+this.getMasterSet().getString("CRADNAL")+"'";
    repQDSnorm.close();
    repQDSnorm.closeStatement();
    repQDSnorm.setQuery(new QueryDescriptor(dm.getDatabase1(),qstr2));
    repQDSnorm.open();
  }

  public void Funkcija_ispisa_master() {
    fisp();
    super.Funkcija_ispisa_master();
  }

  public void Funkcija_ispisa_detail() {
    fisp();
    super.Funkcija_ispisa_detail();
  }

  private void showNorm() {
    if (!getDetailSet().isEmpty()) {
      if (!frmNormat.showArt(getDetailSet().getInt("CART"))) {
        JOptionPane.showMessageDialog(jpDetail, "Artikl nije normiran!", "Greška",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public void checkMasterActions() {
    boolean enabde = getMasterSet().getString("STATUS").equalsIgnoreCase("P");
    //potob.setEnabled(!getMasterSet().getString("STATUS").equalsIgnoreCase("Z"));
    raMaster.getNavBar().getStandardOption(raNavBar.ACTION_UPDATE).setEnabled(enabde);
    raMaster.getNavBar().getStandardOption(raNavBar.ACTION_DELETE).setEnabled(enabde);
    potob.setEnabled(getMasterSet().rowCount() > 0);
  }
  
  void fillRequirements(StorageDataSet store, DataSet ds) {
    for (ds.first(); ds.inBounds(); ds.next()) {
      DataSet exp = Aut.getAut().expandArt(ds, forceTotalExpand);
      if (exp == null) addToExpanded(store, ds, ds.getString("CSKL"));
      else 
        for (exp.first(); exp.inBounds(); exp.next())
          addToExpanded(store, exp, ds.getString("CSKL"));
    }
  }
  
  String[] reqc = {"CART", "CART1", "BC", "NAZART", "JM", "KOL"};
  void showRequirementsMaster() {
    if (getMasterSet().getRowCount() == 0) return;
    
    if (getMasterSet().getString("STATUS").equals("Z") || getMasterSet().getString("STATUS").equals("O")) {
    	showRealReq();
    	return;
    }
    
    final StorageDataSet reqs = new StorageDataSet();
    String[] coln = {"CART", "CART1", "BC", "NAZART", "JM", "KOL", "NC", "INAB", "CSKL"};
    Column[] cols = new Column[coln.length];
    for (int i = 0; i < cols.length; i++)
      cols[i] = (Column) dm.getStdoki().getColumn(coln[i]).clone();
    reqs.setColumns(cols);
    reqs.open();
    
    raProcess.runChild(raMaster.getWindow(), new Runnable() {
      public void run() {
        Condition sel = raMaster.getSelectCondition();
        if (sel == null) sel = Condition.equal("CRADNAL", getMasterSet());
        if (sel != Condition.none) {
          DataSet ds = stdoki.getDataModule().getTempSet(
              sel.and(Condition.equal("VRDOK", "RNL")));
          raProcess.openScratchDataSet(ds);
          fillRequirements(reqs, ds);
          ds.close();
        } else {
          int row = getMasterSet().getRow();
          raMaster.getJpTableView().enableEvents(false);

          for (getMasterSet().first(); getMasterSet().inBounds(); getMasterSet().next())
            if (raMaster.getSelectionTracker().isSelected(getMasterSet())) {
              DataSet ds = stdoki.getDataModule().getTempSet(
                  Condition.equal("CRADNAL", getMasterSet()).and(
                      Condition.equal("VRDOK", "RNL")));
              raProcess.openScratchDataSet(ds);
              fillRequirements(reqs, ds);
              ds.close();
            }
          getMasterSet().goToRow(row);
          raMaster.getJpTableView().enableEvents(true);
        }
        
        String[] cols = {"GOD", "CART"};
        String god = vl.getKnjigYear("robno");
        System.out.println(god);
        for (reqs.first(); reqs.inBounds(); reqs.next()) {
          raProcess.checkClosing();
          int cart = reqs.getInt("CART");
          DataSet st = Stanje.getDataModule().getTempSet(Condition.whereAllEqual(cols, 
              new Object[] {god, new Integer(cart)}));
          st.open();
          if (st.rowCount() > 0) {
            if (!ld.raLocate(st, "CSKL", reqs.getString("CSKL"))) st.first();
            reqs.setBigDecimal("NC", st.getBigDecimal("NC"));
          } else {
            DataSet art = Artikli.getDataModule().getTempSet(Condition.equal("CART", cart));
            art.open();
            reqs.setBigDecimal("NC", art.getBigDecimal("NC"));
          }
          reqs.setBigDecimal("INAB", rut.multiValue(reqs.getBigDecimal("KOL"), 
              reqs.getBigDecimal("NC")));
        }
      }
    });
    
    if (!raProcess.isCompleted()) return;
    
    viewReq.setDataSet(reqs);
    viewReq.setSums(new String[] {"INAB"});
    viewReq.setSaveName("Pregled-potrosnje");
    viewReq.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    viewReq.setTitle("Prikaz ukupne potrošnje materijala  za oznaèene radne naloge");
    viewReq.setVisibleCols(new int[] {Aut.getAut().getCARTdependable(0, 1, 2), 3, 4, 5, 6, 7});
    
    viewReq.show();
  }
  
  void showRealReq() {
  	final StorageDataSet reqs = new StorageDataSet();
  	
  	final String[] coln = {"CART", "CART1", "BC", "NAZART", "JM", "KOL", "NC", "INAB"};
    Column[] cols = new Column[coln.length + 1];
    for (int i = 1; i < cols.length; i++)
      cols[i] = (Column) dm.getStdoki().getColumn(coln[i - 1]).clone();
    cols[0] = dM.createStringColumn("VRA", "Vrsta", 1);
    reqs.setColumns(cols);
    reqs.open();
    
    raProcess.runChild(raMaster.getWindow(), new Runnable() {
      public void run() {
        Condition sel = Condition.equal("CRADNAL", getMasterSet()).and(Condition.equal("VRDOK", "RNL").not());
        
        DataSet ds = stdoki.getDataModule().getTempSet(sel);
        raProcess.openScratchDataSet(ds);
        for (ds.first(); ds.inBounds(); ds.next()) {
        	if (ld.raLocate(reqs, "CART1", ds.getString("CART1"))) {
        		Aus.add(reqs, "KOL", ds);
        		Aus.add(reqs, "INAB", ds);
        	} else {
        		reqs.insertRow(false);
        		reqs.setString("VRA", "M");
        		dM.copyColumns(ds, reqs, coln);
        	}
        }
        ds.close();
        raProcess.checkClosing();
        
        DataSet rn = Rnser.getDataModule().getTempSet(Condition.equal("CRADNAL", getMasterSet()));
        raProcess.openScratchDataSet(rn);
        for (rn.first(); rn.inBounds(); rn.next()) {
        	if (ld.raLocate(reqs, "CART1", rn.getString("CART1"))) {
        		Aus.add(reqs, "KOL", rn);
        		Aus.add(reqs, "INAB", rn, "VRI");
        	} else {
        		reqs.insertRow(false);
        		reqs.setString("VRA", "P");
        		Aut.getAut().copyArtFields(reqs, rn);
        		Aus.set(reqs, "KOL", rn);
        		Aus.set(reqs, "NC", rn, "ZC");
        		Aus.set(reqs, "INAB", rn, "VRI");
        	}
        }
        rn.close();
      }
    });
    
    if (!raProcess.isCompleted()) return;
    
    
    viewReal.setDataSet(reqs);
    viewReal.setSums(new String[] {"INAB"});
    viewReal.setSaveName("Pregled-potrosnje-stvarne");
    viewReal.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    viewReal.setTitle("Prikaz stvarne potrošnje materijala i usluga  za radni nalog " + getMasterSet().getString("CRADNAL"));
    viewReal.setVisibleCols(new int[] {Aut.getAut().getCARTdependable(1, 2, 3), 4, 5, 6, 7, 8});    
  	raExtendedTable t = (raExtendedTable) viewReal.jp.getMpTable();
    t.addToGroup("VRA", true, new String[] {"#", "NAZVRA"}, realVa, true);
    
    viewReal.show();
  }
  
  void showRequirements() {
    StorageDataSet reqs = new StorageDataSet();
    String[] coln = {"CART", "CART1", "BC", "NAZART", "JM", "KOL", "CSKL"};
    Column[] cols = new Column[coln.length];
    for (int i = 0; i < cols.length; i++)
      cols[i] = (Column) dm.getStdoki().getColumn(coln[i]).clone();
    reqs.setColumns(cols);
    reqs.open();
    int row = getDetailSet().getRow();
    raDetail.getJpTableView().enableEvents(false);
    fillRequirements(reqs, getDetailSet());
    getDetailSet().goToRow(row);
    raDetail.getJpTableView().enableEvents(true);
    viewReq.setDataSet(reqs);
    
    viewReq.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    viewReq.setTitle("Prikaz ukupne potrošnje materijala  za radni nalog "+
        getMasterSet().getString("CRADNAL"));
    viewReq.setVisibleCols(new int[] {Aut.getAut().getCARTdependable(0, 1, 2), 3, 4, 5});
    viewReq.show();
  }
  
  void addToExpanded(StorageDataSet expanded, DataSet art, String cskl) {
    if (!ld.raLocate(expanded, "CART", Integer.toString(art.getInt("CART")))) {
      expanded.last();
      expanded.insertRow(false);
      dM.copyColumns(art, expanded, reqc);
    } else expanded.setBigDecimal("KOL", 
        expanded.getBigDecimal("KOL").add(art.getBigDecimal("KOL")));
    expanded.setString("CSKL", cskl);
    expanded.post();
  }
  
  private void jbInit() throws Exception {
    this.setMasterSet(dm.getRNpro());

    this.setVisibleColsMaster(new int[] {3, 13, 20});
    this.setMasterKey(Util.mkey);
    jpMaster = new jpProizvodnjaMaster(this);
    this.setJPanelMaster(jpMaster);
    raMaster.getJpTableView().addTableModifier(new raTableColumnModifier(
        "CPAR", new String[] {"CPAR", "NAZPAR"}, dm.getPartneri()));

    header = jpMaster.jpMasterHeader;

    this.setDetailSet(dm.getStRnlPro());
    this.setUserCheck(false);

    this.setVisibleColsDetail(new int[] {4, Aut.getAut().getCARTdependable(5,6,7), 8, 9, 11});
    this.setDetailKey(Util.dkey);
    jpDetail = new jpProizvodnjaDetail(this);
    this.setJPanelDetail(jpDetail);
    jpDetail.initRpcart();
//                                         "hr.restart.robno.repRadniNalog","hr.restart.robno.repRadniNalog","RadniNalog","Ispis prijave radnog naloga"
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repRadniNalog2","hr.restart.robno.repRadniNalog2","RadniNalog2","Ispis prijave radnog naloga");
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repObracunRadnogNaloga3","Ispis obra\u0111enog radnog naloga",42);
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repObracunRadnogNaloga3","Ispis obra\u0111enog radnog naloga",42);

    this.raMaster.addOption(potob = new raNavAction("Potvrda / zatvaranje", raImages.IMGIMPORT, KeyEvent.VK_F7) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        Obradi();
      }
    },4);
    this.raMaster.addOption(new raNavAction("Pregled materijala", raImages.IMGMOVIE, KeyEvent.VK_F8) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        showRequirementsMaster();
      }
    },5);

    raDetail.addOption(new raNavAction("Sastav normativa", raImages.IMGHISTORY, KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        showNorm();
      }
    }, 3);
    raDetail.addOption(new raNavAction("Pregled materijala", raImages.IMGMOVIE, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        showRequirements();
      }
    }, 4);
    raDetail.addOption(new raNavAction("Radne operacije", raImages.IMGCOMPOSEMAIL, KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
      	rns.setStav(getMasterSet().getString("CRADNAL"), getDetailSet().getInt("RBSID"));
        startFrame.getStartFrame().showFrame(rns);
      }
    }, 5);
    raMaster.removeRnvCopyCurr();
    raDetail.removeRnvCopyCurr();
    
    viewReq.setSize(640, 400);
    viewReq.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 200);
    
    realVa.setColumns(new Column[] {
    		dM.createStringColumn("VRA", "Vrsta", 1),
    		dM.createStringColumn("NAZVRA", "Naziv", 50),
    });
    realVa.open();
    realVa.insertRow(false);
    realVa.setString("VRA", "M");
    realVa.setString("NAZVRA", "Utrošeni materijal");
    realVa.insertRow(false);
    realVa.setString("VRA", "P");
    realVa.setString("NAZVRA", "Radne operacije");

    this.raMaster.installSelectionTracker("CRADNAL");
    this.setMasterDeleteMode(DELDETAIL);
    
    rns = (frmRnser) startFrame.getStartFrame().showFrame("hr.restart.robno.frmRnser", 0, "Pregled radnih operacija", false);
  }
}
