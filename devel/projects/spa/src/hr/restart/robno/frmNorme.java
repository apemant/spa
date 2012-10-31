/****license*****************************************************************
**   file: frmNorme.java
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
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;
import hr.restart.baza.norme;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.Asql;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raOptionDialog;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.util.startFrame;
import hr.restart.util.sysoutTEST;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmNorme extends raMasterFakeDetailArtikl {
  _Main ma;
//  raCommonClass rcc = raCommonClass.getraCommonClass();
//  Valid vl = Valid.getValid();
  
  lookupData ld = lookupData.getlookupData();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();

  JPanel jPanel1 = new JPanel();
  JLabel jlKol = new JLabel();
  XYLayout xYLayout1 = new XYLayout();
  JraTextField jraKol = new JraTextField();
  rapancart rpn = new rapancart();
  rapancskl1 rps = new rapancskl1(false, 250);
  raOptionDialog rod = new raOptionDialog();
//  raArtiklUnos rpn = new raArtiklUnos();

  frmRnus rns;
  
  frmTableDataView viewReq = new frmTableDataView(false, false, true);

  String[] key = new String[] {"CARTNOR"};
  
  private static frmNorme instanceOfMe = null;
  
  public static frmNorme getInstance(){
    if (instanceOfMe == null) instanceOfMe = new frmNorme();
    return instanceOfMe;
  }
  
  public frmNorme() {
    try {
      jbInit();
      instanceOfMe = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'N') {
      rpn.EnabDisab(true);
    }
    if (mode == 'I') {
      rpn.EnabDisab(false);
    }
  }

  public void SetFokusMaster(char mode) {
    /*System.out.println(this.getMasterSet());
    System.out.println(part);
    part.saveChanges(); */
    if (mode == 'N') {
      rpn.EnabDisab(true);
      rpn.setCART();
    } else {
      rpn.setCART(this.getMasterSet().getInt("CARTNOR"));
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (rpn.getCART().equals("")) {
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Potrebno je izabrati artikl!", "Greška", JOptionPane.ERROR_MESSAGE);
      rpn.setCART();
      return false;
    }
    mast.setInt("CARTNOR", Integer.valueOf(rpn.getCART()).intValue());
    if (mode == 'N' && MasterNotUnique()) {
      JOptionPane.showMessageDialog(this.getJPanelMaster(),
         "Normativ ve\u0107 postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
      rpn.setCART();
      return false;
    }
    mast.setInt("CART", mast.getInt("CARTNOR"));
    mast.setString("CART1", rpn.getCART1());
    mast.setString("BC", rpn.getBC());
    mast.setString("NAZART", rpn.getNAZART());
    mast.post();
    return true;
  }

  public boolean canDeleteMaster() {
    return true;
  }

  public void SetFokusIzmjena() {
    jraKol.requestFocus();
  }

  public boolean Validacija(char mode) {
    if (vl.isEmpty(jraKol)) return false;
    return true;
  }

  public void ClearFields() {
//    jraKol.setText("");
    this.getDetailSet().setBigDecimal("KOL", _Main.nul);
  }

  public String CheckMasterKeySQLString() {
    return "SELECT * FROM norme WHERE cartnor = " + mast.getInt("CARTNOR");
  }

  
  public void beforeShowDetail() {
    // TODO Auto-generated method stub
    super.beforeShowDetail();
  }
  
  public boolean ValidacijaPrijeIzlazaDetail() {
    // TODO Auto-generated method stub
    return super.ValidacijaPrijeIzlazaDetail();
  }

  private void jbInit() throws Exception {
    Asql.createMasterNorme(mast);

    this.setMasterSet(mast);
    this.setNaslovMaster("Normativi");
    this.setVisibleColsMaster(new int[] {Aut.getAut().getCARTdependable(1,2,3),4});
    this.setMasterKey(key);

    this.setDetailSet(dm.getNorme());
    this.setNaslovDetail("Stavke normativa");
    this.setVisibleColsDetail(new int[] {Aut.getAut().getCARTdependable(1,2,3),4,5,6});
    this.setDetailKey(key);

    jlKol.setToolTipText("");
    jlKol.setText("Koli\u010Dina");
    jraKol.setDataSet(this.getDetailSet());
    jraKol.setColumnName("KOL");
    jraKol.setHorizontalAlignment(SwingConstants.TRAILING);
    jPanel1.setLayout(xYLayout1);
    xYLayout1.setWidth(430);
    xYLayout1.setHeight(45);
    jPanel1.add(jlKol, new XYConstraints(15, 0, -1, -1));
    jPanel1.add(jraKol, new XYConstraints(150, 0, 100, -1));

    this.raMaster.addOption(new raNavAction("Pregled cijena", raImages.IMGMOVIE, KeyEvent.VK_F8) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        showRequirementsMaster();
      }
    },4,true);
    
    this.raMaster.addOption(new raNavAction("Nusproizvodi", raImages.IMGCOMPOSEMAIL, KeyEvent.VK_F7) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
      	rns.setOwner(getMasterSet().getInt("CARTNOR"));
        startFrame.getStartFrame().showFrame(rns);
      }
    },3,true);
    
    SetPanels(rpn, jPanel1, false);
    initRpn();
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repFormatNorme","hr.restart.robno.repFormatNorme","FormatNorme","Normirani artikli s normama");
    
    rns = (frmRnus) startFrame.getStartFrame().showFrame("hr.restart.robno.frmRnus", 0, "Pregled nusproizvoda", false);
  }

  protected void initRpcart() {
    //rpc.setGodina(hr.restart.util.Valid.getValid().findYear(dm.getDoku().getTimestamp("DATDOK")));
    //rpc.setCskl(dm.getStdoku().getString("CSKL"));
    rpc.setTabela(dm.getNorme());
    rpc.setMyAfterLookupOnNavigate(false);
    rpc.setBorder(null);
    super.initRpcart();
    rpc.setAllowUsluga(true);
  }

  private void initRpn() {
    rpn.setTabela(Aut.getAut().getFakeArtikl());
//    rpn.setTabela(mast);
    rpn.setMode("DOH");
    rpn.setDefParam();
    rpn.InitRaPanCart();
  }
  
  private QueryDataSet detailReportSet = dm.getNorme();
  private HashMap nazivljeNormativa = new HashMap();
  
  private void makeDetailReportSet(){
    nazivljeNormativa.clear();
    
    int[] cs = new int[mast.getRowCount()];
    
    Variant v = new Variant();
    for (int i = 0; i < mast.getRowCount(); i++) {
      mast.getVariant("CARTNOR", i, v);
      System.out.println(v);
      cs[i] = v.getAsInt();
      ld.raLocate(dm.getArtikli(), "CART", v.toString());
      nazivljeNormativa.put(v.toString(),dm.getArtikli().getString("NAZART"));
    }
    norme.getDataModule().setFilter(Condition.in("CARTNOR", cs));
    detailReportSet.open();
    
    /*QueryDataSet normativi = hr.restart.util.Util.getUtil().getNewQueryDataSet("SELECT cart, nazart FROM Artikli WHERE cart in (select distinct cartnor from norme)");
    for (normativi.first();normativi.inBounds();normativi.next()){
      nazivljeNormativa.put(normativi.getInt("CART")+"",normativi.getString("NAZART"));
      System.out.println(normativi.getString("NAZART")); //XDEBUG delete when no more needed
    }*/
    
    //sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
    //st.prn(detailReportSet);
  }
  
  String[] reqc = {"CART", "CART1", "BC", "NAZART", "JM", "KOL"};
  void showRequirementsMaster() {
    JPanel pan = new JPanel(new BorderLayout());
    JPanel p2 = new JPanel(new BorderLayout());
    p2.add(rps);
    p2.setBorder(BorderFactory.createEmptyBorder(10, 15, 20, 10));
    pan.add(p2);
    pan.add(rod.getOkPanel(), BorderLayout.SOUTH);
    if (!rod.show(raMaster.getWindow(), pan, "Izbor skladišta"))
      return;
    
    
    
    if (getMasterSet().getRowCount() == 0) return;
    
    final StorageDataSet reqs = stdoki.getDataModule().getScopedSet(
        "CSKL CART CART1 BC NAZART JM KOL NC INAB");
    reqs.open();
    
    raProcess.runChild(raMaster.getWindow(), new Runnable() {
      public void run() {
        DataSet exp = Aut.getAut().expandArt(
            getMasterSet().getInt("CARTNOR"), Aus.one0, true);
        if (exp == null) return;

        for (exp.first(); exp.inBounds(); exp.next())
           addToExpanded(reqs, exp, rps.getCSKL());
        
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
            reqs.setString("CSKL", st.getString("CSKL"));
          } else {
            DataSet art = Artikli.getDataModule().getTempSet(Condition.equal("CART", cart));
            art.open();
            reqs.setBigDecimal("NC", art.getBigDecimal("NC"));
            reqs.setString("CSKL", "artikl");
          }
          reqs.setBigDecimal("INAB", rut.multiValue(reqs.getBigDecimal("KOL"), 
              reqs.getBigDecimal("NC")));
        }
      }
    });
    
    if (!raProcess.isCompleted()) return;
    
    viewReq.setDataSet(reqs);
    viewReq.setSums(new String[] {"INAB"});
    viewReq.setSaveName("Pregled-normativa");
    viewReq.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    viewReq.setTitle("Prikaz ukupne potrošnje materijala normativa");
    viewReq.setVisibleCols(new int[] {0, Aut.getAut().getCARTdependable(1, 2, 3), 4, 5, 6, 7, 8});
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

  public void Funkcija_ispisa_master() {
    makeDetailReportSet();
    super.Funkcija_ispisa_master();
  }
  
  public QueryDataSet getRepSet(){
    return detailReportSet;
  }
  
  public HashMap getNaNorm() {
    return nazivljeNormativa;
  }
  
}
