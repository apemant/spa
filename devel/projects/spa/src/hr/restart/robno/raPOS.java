/****license*****************************************************************
**   file: raPOS.java
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

import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Rate;
import hr.restart.baza.Sklad;
import hr.restart.baza.Stanje;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateMask;
import hr.restart.swing.raInputDialog;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.swing.raTextMask;
import hr.restart.util.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raPOS extends raIzlazTemplate  {
//  hr.restart.util.startFrame SF;
//  QueryDataSet stanjeforChange = new QueryDataSet();
  static dM dm = dM.getDataModule();
  
  frmTableDataView viewReq = new frmTableDataView(false, false, true);

  public void initialiser(){
    what_kind_of_dokument = "POS";
  }

  public void MyaddIspisMaster(){
    raMaster.getRepRunner().addReport("hr.restart.robno.repPOS","Razduženje maloprodaje",2);
  }

  public void MyaddIspisDetail(){
    raDetail.getRepRunner().addReport("hr.restart.robno.repPOS","Razduženje maloprodaje",2);
  }
  public raPOS() {
    isMaloprodajnaKalkulacija = true;
    setPreSel((jpPreselectDoc) presPOS.getPres());
    addButtons(true,false);
    master_titel = "Razduženja maloprodaje";
    detail_titel_mno = "Stavke razduženja maloprodaje";
    detail_titel_jed = "Stavka razduženja maloprodaje";
    setMasterSet(dm.getZagPos());
    setDetailSet(dm.getStDokiPos());
    setMasterDeleteMode(DELDETAIL);
    this.raMaster.addOption(new raNavAction("Pregled materijala", raImages.IMGMOVIE, KeyEvent.VK_F8) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        showRequirementsMaster();
      }
    },3,false);
    MP.BindComp();
    DP.BindComp();
  }

  public void revive() {
    super.revive();
    try {
      raTransaction.runSQL("UPDATE pos SET status='N', rdok='' WHERE " +
          Condition.equal("RDOK", key4delZag));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void brisiVezu() {
    // empty
  }
  
  public void cskl2csklart() {
    // empty
  }
  
  public void RestPanelSetup(){
    DP.addRestGRNGOT();
  }
  
  public void keyActionMaster() {
   frmPos2POS.razdMP(getPreSelect().getSelRow(), getMasterSet(), getDetailSet());
     if (frmPos2POS.getFrmpos().errors==null || frmParam.getParam("pos", "POScheck", "D",
            "Provjeriti artikle kod prijenosa GRC->POS (D,N)").equals("N")){
       if (frmPos2POS.getFrmpos().isCancelPress()) return;
       frmPos2POS.getFrmpos().saveAll();
       //raMaster.getJpTableView().fireTableDataChanged();
       afterOKSC();
     }
     else {
       getDetailSet().refresh();
       int res = JOptionPane.showConfirmDialog(raMaster.getWindow(), 
           new raMultiLineMessage(frmPos2POS.getFrmpos().errors +
                "\n\nDetaljni prikaz grešaka?",
  	            SwingConstants.LEADING), "Greška", 
  	            JOptionPane.ERROR_MESSAGE,
  	            JOptionPane.OK_CANCEL_OPTION);
       if (res == JOptionPane.OK_OPTION)
         SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            frmTableDataView errs = new frmTableDataView();
            errs.setTitle("Popis artikala s nedovoljnom zalihom");
            errs.setSaveName("view-transfer");
            errs.setDataSet(frmPos2POS.getFrmpos().errorSet);
            errs.jp.getMpTable().setAutoResizeMode(
                JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            errs.show();
          }
        });
     }
  }
  
  String[] reqc = {"CART", "CART1", "BC", "NAZART", "JM", "KOL"};
  void showRequirementsMaster() {
    if (getMasterSet().getRowCount() == 0) return;
    
    final StorageDataSet reqs = stdoki.getDataModule().getScopedSet(
    "CSKL CART CART1 BC NAZART JM KOL NC INAB KOL1 KOL2");
    reqs.open();
    reqs.getColumn("KOL1").setCaption("Stanje");
    reqs.getColumn("KOL2").setCaption("Rezultat");
    
    raProcess.runChild(raMaster.getWindow(), new Runnable() {
      public void run() {
        if (raMaster.getSelectCondition() == null) {
          DataSet ds = stdoki.getDataModule().getTempSet(
              Condition.whereAllEqual(Util.mkey, getMasterSet()));
          raProcess.openScratchDataSet(ds);
          fillRequirements(reqs, ds);
          ds.close();
        } else {
          int row = getMasterSet().getRow();
          raMaster.getJpTableView().enableEvents(false);

          for (getMasterSet().first(); getMasterSet().inBounds(); getMasterSet().next())
            if (raMaster.getSelectionTracker().isSelected(getMasterSet())) {
              DataSet ds = stdoki.getDataModule().getTempSet(
                  Condition.whereAllEqual(Util.mkey, getMasterSet()));
              raProcess.openScratchDataSet(ds);
              fillRequirements(reqs, ds);
              ds.close();
            }
          getMasterSet().goToRow(row);
          raMaster.getJpTableView().enableEvents(true);
        }
        
        String[] cols = {"GOD", "CART"};
        String god = val.getKnjigYear("robno");
        System.out.println(god);
        for (reqs.first(); reqs.inBounds(); reqs.next()) {
          raProcess.checkClosing();
          int cart = reqs.getInt("CART");
          DataSet st = Stanje.getDataModule().getTempSet(Condition.whereAllEqual(cols, 
              new Object[] {god, new Integer(cart)}));
          st.open();
          if (st.rowCount() > 0) {
            if (!lD.raLocate(st, "CSKL", reqs.getString("CSKL"))) st.first();
            else Aus.set(reqs, "KOL1", st, "KOL");
            reqs.setBigDecimal("NC", st.getBigDecimal("NC"));
          } else {
            DataSet art = Artikli.getDataModule().getTempSet(Condition.equal("CART", cart));
            art.open();
            reqs.setBigDecimal("NC", art.getBigDecimal("NC"));
          }
          Aus.sub(reqs, "KOL2", "KOL1", "KOL");
          reqs.setBigDecimal("INAB", util.multiValue(reqs.getBigDecimal("KOL"), 
              reqs.getBigDecimal("NC")));
        }
      }
    });
    
    if (!raProcess.isCompleted()) return;
    
    viewReq.setDataSet(reqs);
    viewReq.setSums(new String[] {"INAB"});
    viewReq.setSaveName("Pregled-razd");
    viewReq.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    viewReq.setTitle("Prikaz ukupne potrošnje materijala  za oznaèena razduženja");
    viewReq.setVisibleCols(new int[] {0, Aut.getAut().getCARTdependable(1, 2, 3), 4, 5, 6, 7, 8});
    viewReq.show();
  }
  
  void fillRequirements(StorageDataSet store, DataSet ds) {
    for (ds.first(); ds.inBounds(); ds.next()) {
      DataSet exp = Aut.getAut().expandArt(ds, true);
      if (exp == null) addToExpanded(store, ds, ds.getString("CSKLART"));
      else 
        for (exp.first(); exp.inBounds(); exp.next())
          addToExpanded(store, exp, ds.getString("CSKLART"));
    }
  }
  
  void addToExpanded(StorageDataSet expanded, DataSet art, String cskl) {
    if (!lD.raLocate(expanded, "CART", Integer.toString(art.getInt("CART")))) {
      expanded.last();
      expanded.insertRow(false);
      dM.copyColumns(art, expanded, reqc);
    } else expanded.setBigDecimal("KOL", 
        expanded.getBigDecimal("KOL").add(art.getBigDecimal("KOL")));
    expanded.setString("CSKL", cskl.startsWith("#") ? cskl.substring(1) : cskl);
    expanded.post();
  }
  
  static String oldpj = "1";
  static String err = "";
  static String csklCond = "";
  static boolean vhack, rep;
  static StorageDataSet tds = new StorageDataSet();
  static String lastCskl = null;
  static Timestamp lastDate = null;
  public static void zakljucak() {
    frmParam.getParam("robno", "razdCpar", "1", "Šifra partnera za razduženje blagajne");
    frmParam.getParam("robno", "razdTros", "01", "Šifra vrste troška razduženje blagajne");
    frmParam.getParam("robno", "razdRep", "02", "Šifra vrste troška razduženje blagajne za reprezentaciju");
    rep = frmParam.getParam("robno", "razdRepSep", "N", "Razdvojiti razduženje reprezentacije? (D,N)").equalsIgnoreCase("D");
    
    vhack = frmParam.getParam("pos", "vHack", "D", "Prikaz posebnih kategorija za Vezuv (D,N)").equalsIgnoreCase("D");
    
  	JPanel pan = new JPanel(new XYLayout(vhack ? 415 : 540, 75));
  	tds = new StorageDataSet();
  		tds.setColumns(new Column[] {
  				dM.createTimestampColumn("DATDOK"),
  				dM.createStringColumn("CSKL", 12),
  		});
  		tds.open();
  		if (lastCskl != null) tds.setString("CSKL", lastCskl);
  		tds.setTimestamp("DATDOK", Valid.getValid().getToday());
  		if (lastDate != null) tds.setTimestamp("DATDOK", hr.restart.util.Util.getUtil().addDays(lastDate, 1));
  			
    JraTextField dat = new JraTextField();
    dat.setColumnName("DATDOK");
    dat.setDataSet(tds);
    
    JLabel jlCskl = new JLabel();
    JlrNavField jlrCskl = new JlrNavField();
    JlrNavField jlrNazskl = new JlrNavField();
    JraButton jbSelCskl = new JraButton();
    
    jlCskl.setText("Skladište");
    jlrCskl.setColumnName("CSKL");
    jlrCskl.setDataSet(tds);
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl});
    jlrCskl.setVisCols(new int[] {0,1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(Util.getSkladFromCorg());
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);
    
  	raComboBox pj = new raComboBox() {
    	public void this_itemStateChanged() {
    		oldpj = getDataValue();
    	}
    };
    pj.setRaItems(new String[][] {
    		{"Robna kuæa \"Vesna I\"", "1"},
    		{"Robna kuæa \"Tena\"", "2"},
    		{"Robna kuæa \"Pierre\"", "3"},
    		{"Robna kuæa \"Vesna II\"", "4"}
    });
    dat.setHorizontalAlignment(JLabel.CENTER);
    new raDateMask(dat);
    if (vhack) {
      pan.add(new JLabel("Datum zakljuèka"), new XYConstraints(15,15,-1,-1));
      pan.add(dat, new XYConstraints(300, 15, 100, -1));
      pan.add(new JLabel("Prodajno mjesto"), new XYConstraints(15,40,-1,-1));
      pan.add(pj, new XYConstraints(190, 40, 210, -1));
      pj.setDataValue(oldpj);
    } else {
      pan.add(new JLabel("Datum zakljuèka"), new XYConstraints(15,15,-1,-1));
      pan.add(dat, new XYConstraints(150, 15, 100, -1));
      pan.add(jlCskl, new XYConstraints(15, 40, -1, -1));
      pan.add(jlrCskl, new XYConstraints(150, 40, 100, -1));
      pan.add(jlrNazskl, new XYConstraints(255, 40, 250, -1));
      pan.add(jbSelCskl, new XYConstraints(510, 40, 21, 21));
    }   
    
    raInputDialog od = new raInputDialog();
    if (!od.show(null, pan, "Zakljuèak blagajne")) return;
    
    lastCskl = tds.getString("CSKL");
    lastDate = tds.getTimestamp("DATDOK");
    
    Condition cond = Condition.till("DATDOK", tds).and(Condition.from("DATDOK", 
        hr.restart.util.Util.getUtil().getFirstDayOfYear(tds.getTimestamp("DATDOK"))));
    csklCond = vhack ? "m.cskl like '" + oldpj + "%'" : Condition.equal("CSKL", tds).qualified("m").toString();
    
    String q = "SELECT cskl, datdok from pos m WHERE " + 
        "status='N' AND uirac!=0 AND " + csklCond + " and " + cond;
    
    DataSet allpos = Aus.q(q);
    if (allpos.rowCount() == 0) {
      JOptionPane.showMessageDialog(null, "Nema nerazduženog prometa do tog datuma.", 
          "Zakljuèak", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    
    Timestamp min = hr.restart.util.Util.getUtil().
            getFirstSecondOfDay(tds.getTimestamp("DATDOK"));
    for (allpos.first(); allpos.inBounds(); allpos.next()) {
      if (allpos.getTimestamp("DATDOK").before(min))
        min = hr.restart.util.Util.getUtil().
          getFirstSecondOfDay(allpos.getTimestamp("DATDOK"));
    }
    if (min.before(hr.restart.util.Util.getUtil().
        getFirstSecondOfDay(tds.getTimestamp("DATDOK")))) {
      if (JOptionPane.showConfirmDialog(
          null, "Postoji nerazduženi promet od " + 
          Aus.formatTimestamp(min) + ".\nJeste li sigurni da ga želite "+
          "zakljuèiti s datumom " + Aus.formatTimestamp(tds.getTimestamp("DATDOK")) 
          + "?", "Promet", JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION)
        return;
    }
    
    raProcess.runChild(new Runnable() {		
			public void run() {
				if (!new raLocalTransaction(){
					
					public boolean transaction() throws Exception {
						if (vhack) perform();
						else totalRaz();
						return true;
					}
				
				}.execTransaction())
				  raProcess.fail();
				
			}
		});
    if (raProcess.isCompleted())
      JOptionPane.showMessageDialog(null, "Razduženje završeno.", 
          "Zakljuèak", JOptionPane.INFORMATION_MESSAGE);
    else if (raProcess.isFailed())
      JOptionPane.showMessageDialog(null, 
          new raMultiLineMessage(err, JLabel.LEADING), 
          "Zakljuèak", JOptionPane.ERROR_MESSAGE);
  }
  
  static StorageDataSet expanded = null;
  static String[] cc = { "CART", "CART1", "BC", "NAZART", "JM", "KOL" };
  
  static void initNormExpansion(StorageDataSet arts) {
      expanded = new StorageDataSet();
      expanded.setColumns(arts.cloneColumns());
      expanded.open();
      for (arts.first(); arts.inBounds(); arts.next()) {
        if (!rep) arts.setString("STATUS", "G");
        DataSet exp = Aut.getAut().expandArt(arts, true);
        if (exp == null)
          addToExpanded(arts, arts);
        else
          for (exp.first(); exp.inBounds(); exp.next())
            addToExpanded(exp, arts);
      }
      expanded.first();

  }
  
  static private void addToExpanded(DataSet art, DataSet full) {
    if (!lookupData.getlookupData().raLocate(expanded, new String[] {"STATUS","CART"}, 
        new String[] {full.getString("STATUS"), Integer.toString(art.getInt("CART"))})) {
      expanded.last();
      expanded.insertRow(false);
      dM.copyColumns(full, expanded);
      dM.copyColumns(art, expanded, cc);
    } else expanded.setBigDecimal("KOL", 
        expanded.getBigDecimal("KOL").add(art.getBigDecimal("KOL")));
    expanded.post();
  }
  
  
  public static void totalRaz() throws Exception {
    String cpar = frmParam.getParam("robno", "razdCpar", "1", "Šifra partnera za razduženje blagajne");
    //String tr = frmParam.getParam("robno", "razdTros", "01", "Šifra vrste troška razduženje blagajne");
    //String trr = frmParam.getParam("robno", "razdTrosRep", "02", "Šifra vrste troška razduženje blagajne za reprezentaciju");
    boolean cag = frmParam.getParam("pos", "cagentHack", "N", "Naèin plaæanja u cagent (D,N)").equals("D");
    
    lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", tds.getString("CSKL"));
    DataSet samec = Sklad.getDataModule().getTempSet(Condition.equal("CORG", dm.getSklad()));
    Condition sc = Condition.in("CSKL", samec);
    Condition dat = Condition.till("DATDOK", tds).and(Condition.from("DATDOK", 
        hr.restart.util.Util.getUtil().getFirstDayOfYear(tds.getTimestamp("DATDOK"))));
    
    StorageDataSet ds = getArtikliSet(dat);
    initNormExpansion(ds);
    
   
    QueryDataSet sta = Stanje.getDataModule().getTempSet(
        Condition.equal("GOD", Valid.getValid().findYear(tds.getTimestamp("DATDOK")))
        .and(sc).and(Condition.in("CART", expanded)));
    sta.open();
    
    QueryDataSet rzag = doki.getDataModule().getTempSet("1=0");
    rzag.open();
    
    QueryDataSet rst = stdoki.getDataModule().getTempSet("1=0");
    rst.open();
    
    QueryDataSet izag = doki.getDataModule().getTempSet("1=0");
    izag.open();
    
    QueryDataSet ist = stdoki.getDataModule().getTempSet("1=0");
    ist.open();
    
    hr.restart.util.LinkClass lc = hr.restart.util.LinkClass.getLinkClass();
    lookupData ld = lookupData.getlookupData();
  raKalkulBDDoc rKD = new raKalkulBDDoc();
  
    String[] icols = {"CART", "CART1", "BC", "NAZART", "JM", "KOL"};
    String[] cols = {"CART", "CART1", "BC", "NAZART", "JM", "KOL", 
        "UIRAB", "UPRAB", "FC", "INETO", "FVC", 
        "IPRODBP", "POR1", "POR2", "POR3", "FMC", "FMCPRP", 
        "IPRODSP", "PPOR1", "PPOR2", "PPOR3"};
    
    if (rep) ds.setSort(new SortDescriptor(new String[] {"STATUS", "CART", "REZKOL", "UPRAB", "BRDOK"}));
    else ds.setSort(new SortDescriptor(new String[] {"CART", "REZKOL", "UPRAB", "BRDOK"}));
    
    
    String cskl = "", vrzal = "", nacpl = "";
    int rbr = 0;
    for (ds.first(); ds.inBounds(); ds.next()) {
        if (cskl != ds.getString("CSKL") || (rep && !ds.getString("STATUS").equals(nacpl))) {
                        
            cskl = ds.getString("CSKL");
            nacpl = ds.getString("STATUS");
            ld.raLocate(dM.getDataModule().getSklad(), "CSKL", cskl);
            // vrzal = dM.getDataModule().getSklad().getString("VRZAL");
            
            rzag.insertRow(false);
            rzag.setString("CSKL", dM.getDataModule().getSklad().getString("CORG"));
            rzag.setString("VRDOK", "POS");
            rzag.setString("CUSER", raUser.getInstance().getUser());
            rzag.setTimestamp("DATDOK", tds.getTimestamp("DATDOK"));
            rzag.setTimestamp("DVO", tds.getTimestamp("DATDOK"));
            rzag.setTimestamp("DATDOSP", tds.getTimestamp("DATDOK"));
            rzag.setInt("CPAR", Aus.getNumber(cpar));
            Util.getUtil().getBrojDokumenta(rzag);
            rzag.setString("PNBZ2", raPozivNaBroj.getraPozivNaBrojClass().getPozivNaBroj(rzag));
            Aus.clear(rzag, "UIRAC");
            Condition crep = Condition.none;
            if (rep) {
              if (nacpl.equals("R")) crep = Condition.equal("CNACPL", "R");
              else crep = Condition.diff("CNACPL", "R");
            }
            
            if (cag && rep) rzag.setInt("CAGENT", nacpl.equals("R") ? 2 : 1); 
            raTransaction.runSQL("update pos set status='P', rdok='" + raControlDocs.getKey(rzag) +
                "' where "+ dat.and(crep).and(Condition.equal("CSKL", cskl).and(Condition.equal("STATUS", "N"))).qualified("pos"));

            /*izag.insertRow(false);
            izag.setString("CSKL", cskl);
            izag.setString("VRDOK", "IZD");
            izag.setTimestamp("DATDOK", tds.getTimestamp("DATDOK"));
            izag.setString("CORG", cskl);
            izag.setString("CVRTR", tr);
            Util.getUtil().getBrojDokumenta(izag);*/
            
            rbr = 0;
        }
        
        if (ds.getBigDecimal("KOL").signum() != 0) {
          
            Aus.add(rzag, "UIRAC", ds, "IPRODSP");
            rst.insertRow(false);
        dM.copyColumns(ds, rst, cols);
        rst.setString("CSKL", rzag.getString("CSKL"));
        rst.setString("VRDOK", rzag.getString("VRDOK"));
        rst.setString("GOD", rzag.getString("GOD"));
        rst.setInt("BRDOK", rzag.getInt("BRDOK"));
        rst.setShort("RBR", (short) ++rbr);
        rst.setInt("RBSID", rst.getShort("RBR"));
        if (ld.raLocate(sta, "CART", Integer.toString(ds.getInt("CART")))) {
          rst.setString("CSKLART", sta.getString("CSKL"));
          if (ld.raLocate(expanded, new String[] {"CSKL", "CART"}, new String[] {cskl, Integer.toString(ds.getInt("CART"))}))
            expanded.setString("CSKL", sta.getString("CSKL"));
        } else rst.setString("CSKLART", ds.getString("CSKL"));
        rst.setString("ID_STAVKA",
          raControlDocs.getKey(rst, new String[] { "cskl",
              "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
        rst.post();
        
        /*ist.insertRow(false);
        dM.copyColumns(ds, ist, icols);
        ist.setString("CSKL", izag.getString("CSKL"));
        ist.setString("VRDOK", izag.getString("VRDOK"));
        ist.setString("GOD", izag.getString("GOD"));
        ist.setInt("BRDOK", izag.getInt("BRDOK"));
        ist.setShort("RBR", (short) rbr);
        ist.setInt("RBSID", (int) ist.getShort("RBR"));
        ist.setString("ID_STAVKA",
          raControlDocs.getKey(ist, new String[] { "cskl",
              "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
        ist.setString("VEZA", rst.getString("ID_STAVKA"));
        
        ld.raLocate(sta, new String[] {"CSKL", "CART"}, new String[] {izag.getString("CSKL"), ds.getInt("CART")+""});
        Aus.sub(sta, "KOLREZ", ds, "KOL");
        
        
        rKD.stanje.Init();
      lc.TransferFromDB2Class(sta,rKD.stanje);
      rKD.stanje.sVrSklad=vrzal;
      rKD.stavkaold.Init();
      rKD.stavka.Init();
      rKD.stavka.kol = ds.getBigDecimal("KOL");
        
      rKD.KalkulacijaStavke("IZD","KOL",'N',cskl,false);
      rKD.KalkulacijaStanje("IZD");
      lc.TransferFromClass2DB(ist,rKD.stavka);
      lc.TransferFromClass2DB(sta,rKD.stanje);*/
      }
        
    }
    
    cskl = "";
    vrzal = "";
    nacpl = "";
    rbr = 0;
    
    if (rep) expanded.setSort(new SortDescriptor(new String[] {"CSKL", "STATUS", "CART"}));
    else expanded.setSort(new SortDescriptor(new String[] {"CSKL", "CART"}));
    
    for (expanded.first(); expanded.inBounds(); expanded.next()) {
      if (cskl != expanded.getString("CSKL") || (rep && !expanded.getString("STATUS").equals(nacpl))) {
        cskl = expanded.getString("CSKL");
        nacpl = expanded.getString("STATUS");
        ld.raLocate(dM.getDataModule().getSklad(), "CSKL", cskl);
        vrzal = dM.getDataModule().getSklad().getString("VRZAL");
        izag.insertRow(false);
        izag.setString("CSKL", cskl);
        izag.setString("CUSER", raUser.getInstance().getUser());
        izag.setString("VRDOK", "IZD");
        izag.setTimestamp("DATDOK", tds.getTimestamp("DATDOK"));
        izag.setString("CORG", dM.getDataModule().getSklad().getString("CORG"));
        
        String tr = frmParam.getParam("robno", "razdTros"+cskl, "01", "Šifra vrste troška razduženje blagajne za skladište "+cskl);
        String trr = frmParam.getParam("robno", "razdTrosRep"+cskl, "02", "Šifra vrste troška razduženje blagajne za reprezentaciju skl. "+cskl);
        
        izag.setString("CVRTR", rep && nacpl.equals("R") ? trr : tr);
        Util.getUtil().getBrojDokumenta(izag);
        
        rbr = 0;
      }
      
      if (expanded.getBigDecimal("KOL").signum() != 0) {
        ist.insertRow(false);
        dM.copyColumns(expanded, ist, icols);
        ist.setString("CSKL", izag.getString("CSKL"));
        ist.setString("VRDOK", izag.getString("VRDOK"));
        ist.setString("GOD", izag.getString("GOD"));
        ist.setInt("BRDOK", izag.getInt("BRDOK"));
        ist.setShort("RBR", (short) ++rbr);
        ist.setInt("RBSID", ist.getShort("RBR"));
        ist.setString("ID_STAVKA",
          raControlDocs.getKey(ist, new String[] { "cskl",
              "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
        //ist.setString("VEZA", rst.getString("ID_STAVKA"));
        
        ld.raLocate(sta, new String[] {"CSKL", "CART"}, new String[] {izag.getString("CSKL"), expanded.getInt("CART")+""});
        Aus.sub(sta, "KOLREZ", expanded, "KOL");
        
        rKD.stanje.Init();
        lc.TransferFromDB2Class(sta,rKD.stanje);
        rKD.stanje.sVrSklad=vrzal;
        rKD.stavkaold.Init();
        rKD.stavka.Init();
        rKD.stavka.kol = expanded.getBigDecimal("KOL");
        
        rKD.KalkulacijaStavke("IZD","KOL",'N',cskl,false);
        rKD.KalkulacijaStanje("IZD");
        lc.TransferFromClass2DB(ist,rKD.stavka);
        lc.TransferFromClass2DB(sta,rKD.stanje);
      }
    }
    raTransaction.saveChanges(rzag);
    raTransaction.saveChanges(rst);
    raTransaction.saveChanges(izag);
    raTransaction.saveChanges(ist);
    raTransaction.saveChanges(sta);
    raTransaction.saveChanges(dM.getDataModule().getSeq());
    
  }
  
  
  public static void perform() throws Exception {
  	String cpar = frmParam.getParam("robno", "razdCpar", "1", "Šifra partnera za razduženje blagajne");
  	String tr = frmParam.getParam("robno", "razdTros", "01", "Šifra vrste troška razduženje blagajne");
  	
  	Condition dat = Condition.till("DATDOK", tds).and(Condition.from("DATDOK", 
        hr.restart.util.Util.getUtil().getFirstDayOfYear(tds.getTimestamp("DATDOK"))));
  	DataSet ds = getArtikliSet(dat);
  	DataSet rate = getRate(dat);

		QueryDataSet rzag = doki.getDataModule().getTempSet("1=0");
		rzag.open();
		
		QueryDataSet rst = stdoki.getDataModule().getTempSet("1=0");
		rst.open();
		
		QueryDataSet izag = doki.getDataModule().getTempSet("1=0");
		izag.open();
		
		QueryDataSet ist = stdoki.getDataModule().getTempSet("1=0");
		ist.open();
		
		QueryDataSet rt = Rate.getDataModule().getTempSet("1=0");
        rt.open();
		
		QueryDataSet sta = Stanje.getDataModule().getTempSet("cskl like '" + oldpj + "%' and god='" + 
				Valid.getValid().findYear(tds.getTimestamp("DATDOK")) + "'");
		sta.open();
		
		hr.restart.util.LinkClass lc = hr.restart.util.LinkClass.getLinkClass();
		lookupData ld = lookupData.getlookupData();
	  raKalkulBDDoc rKD = new raKalkulBDDoc();
	  
	  String[] icols = {"CART", "CART1", "BC", "NAZART", "JM", "KOL"};
	  String[] cols = {"CART", "CART1", "BC", "NAZART", "JM", "KOL", 
        "UIRAB", "UPRAB", "FC", "INETO", "FVC", 
        "IPRODBP", "POR1", "POR2", "POR3", "FMC", "FMCPRP", 
        "IPRODSP", "PPOR1", "PPOR2", "PPOR3"};
		
		String cskl = "", vrzal = "";
		int rbr = 0;
		BigDecimal total = Aus.zero2;
		for (ds.first(); ds.inBounds(); ds.next()) {
			if (cskl != ds.getString("CSKL")) {
				
				if (rzag.rowCount() > 0) {
				  System.out.println("Fran: " + cskl + "  UIRAC " +
				      rzag.getBigDecimal("UIRAC") + "   IRATA " + total + 
				      (total.compareTo(rzag.getBigDecimal("UIRAC")) == 0
				          ? " ok" : " ERROR!"));
				  if (total.compareTo(rzag.getBigDecimal("UIRAC")) != 0) {
				    err = "Skladište " + cskl +": kriva suma rata!"+
				          "\nRaèuni: " + Aus.formatBigDecimal(rzag.getBigDecimal("UIRAC"))+
				          "\nRate: " + Aus.formatBigDecimal(total);
				    throw new Exception(err);
				  }
				}
				
				cskl = ds.getString("CSKL");
				ld.raLocate(dM.getDataModule().getSklad(), "CSKL", cskl);
				vrzal = dM.getDataModule().getSklad().getString("VRZAL");
				rzag.insertRow(false);
				rzag.setString("CSKL", cskl);
				rzag.setString("VRDOK", "POS");
				rzag.setTimestamp("DATDOK", tds.getTimestamp("DATDOK"));
				rzag.setTimestamp("DVO", tds.getTimestamp("DATDOK"));
				rzag.setTimestamp("DATDOSP", tds.getTimestamp("DATDOK"));
				rzag.setInt("CPAR", Aus.getNumber(cpar));
				Util.getUtil().getBrojDokumenta(rzag);
				Aus.clear(rzag, "UIRAC");
				raTransaction.runSQL("update pos set status='P', rdok='" + raControlDocs.getKey(rzag) +
            "' where "+ dat.and(Condition.equal("CSKL", cskl).and(Condition.equal("STATUS", "N"))).qualified("pos"));
				
				short rbs = 0;
				total = Aus.zero2;
				if (ld.raLocate(rate, "CSKL", cskl)) do {
				  rt.insertRow(false);
				  dM.copyColumns(rzag, rt, Util.mkey);
				  rt.setShort("RBR", ++rbs);
				  rt.setTimestamp("DATDOK", rzag.getTimestamp("DATDOK"));
				  rt.setTimestamp("DATUM", rzag.getTimestamp("DATDOK"));
                  dM.copyColumns(rate, rt);
                  rt.post();
                  total = total.add(rt.getBigDecimal("IRATA"));
				} while (rate.next() && rate.getString("CSKL").equals(cskl));
				
				izag.insertRow(false);
				izag.setString("CSKL", cskl);
				izag.setString("VRDOK", "IZD");
				izag.setTimestamp("DATDOK", tds.getTimestamp("DATDOK"));
				izag.setString("CORG", cskl);
				izag.setString("CVRTR", tr);
				Util.getUtil().getBrojDokumenta(izag);
				
				rbr = 0;
			}
			
			if (ds.getBigDecimal("KOL").signum() != 0) {
				Aus.add(rzag, "UIRAC", ds, "IPRODSP");
				rst.insertRow(false);
		  	dM.copyColumns(ds, rst, cols);
		    rst.setString("CSKL", rzag.getString("CSKL"));
		    rst.setString("VRDOK", rzag.getString("VRDOK"));
		    rst.setString("GOD", rzag.getString("GOD"));
		    rst.setInt("BRDOK", rzag.getInt("BRDOK"));
		    rst.setShort("RBR", (short) ++rbr);
		    rst.setInt("RBSID", rst.getShort("RBR"));
		    rst.setString("CSKLART", ds.getString("CSKL"));
		    rst.setString("ID_STAVKA",
	          raControlDocs.getKey(rst, new String[] { "cskl",
	              "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
		    rst.post();
		    
		    ist.insertRow(false);
		    dM.copyColumns(ds, ist, icols);
		    ist.setString("CSKL", izag.getString("CSKL"));
		    ist.setString("VRDOK", izag.getString("VRDOK"));
		    ist.setString("GOD", izag.getString("GOD"));
		    ist.setInt("BRDOK", izag.getInt("BRDOK"));
		    ist.setShort("RBR", (short) rbr);
		    ist.setInt("RBSID", ist.getShort("RBR"));
		    ist.setString("ID_STAVKA",
	          raControlDocs.getKey(ist, new String[] { "cskl",
	              "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
		    ist.setString("VEZA", rst.getString("ID_STAVKA"));
		    
		    ld.raLocate(sta, new String[] {"CSKL", "CART"}, new String[] {izag.getString("CSKL"), ds.getInt("CART")+""});
		    Aus.sub(sta, "KOLREZ", ds, "KOL");
		    
		    
		    rKD.stanje.Init();
	      lc.TransferFromDB2Class(sta,rKD.stanje);
	      rKD.stanje.sVrSklad=vrzal;
	      rKD.stavkaold.Init();
	      rKD.stavka.Init();
	      rKD.stavka.kol = ds.getBigDecimal("KOL");
		    
	      rKD.KalkulacijaStavke("IZD","KOL",'N',cskl,false);
	      rKD.KalkulacijaStanje("IZD");
	      lc.TransferFromClass2DB(ist,rKD.stavka);
	      lc.TransferFromClass2DB(sta,rKD.stanje);
      }
			
		}
		raTransaction.saveChanges(rzag);
		raTransaction.saveChanges(rst);
		raTransaction.saveChanges(izag);
		raTransaction.saveChanges(ist);
		raTransaction.saveChanges(sta);
		raTransaction.saveChanges(rt);
		raTransaction.saveChanges(dM.getDataModule().getSeq());
  }
  
  public static DataSet getRate(String q) {
    String[] cols = {"CSKL", "CNACPL", "CBANKA", "IRATA", "CPRODMJ"};
    StorageDataSet inter = Rate.getDataModule().getScopedSet(cols);       
    hr.restart.util.Util.fillReadonlyData(inter, q);
    inter.setSort(new SortDescriptor(
        new String[] {"CSKL", "CNACPL", "CBANKA"}));
    
    StorageDataSet group = Rate.getDataModule().getScopedSet(cols);
    group.open();
    
    String cskl = "";
    String cnacpl = "";
    String cbanka = "";
    for (inter.first(); inter.inBounds(); inter.next()) {
      if (!inter.getString("CSKL").equals(cskl) ||
          !inter.getString("CNACPL").equals(cnacpl) ||
          !inter.getString("CBANKA").equals(cbanka)) {
        group.insertRow(false);
        dM.copyColumns(inter, group, cols);
        cskl = inter.getString("CSKL");
        cnacpl = inter.getString("CNACPL");
        cbanka = inter.getString("CBANKA");
      } else {
        Aus.add(group, "IRATA", inter);
      }
    }
    return group;
  }
  
  static DataSet getRate(Condition cond) {
    String q =
      "select m.cskl, r.cnacpl, r.cbanka, r.irata, r.cprodmj "+
      "from pos m, rate r where " + Util.getUtil().getDoc("m", "r") +
      " AND m.status='N' AND " + csklCond + " and " + cond.qualified("m");
    
    return getRate(q);
  }
  
  static StorageDataSet getArtikliSet(Condition cond) {
    String status = vhack ? "m.status" : "m.cnacpl as status";
    VarStr q = new VarStr(
        "SELECT m.cskl, m.brdok, " + status + ", d.cart, d.cart1, d.bc, d.jm, d.nazart, " +
        "d.kol, d.rezkol, d.ipopust1+d.ipopust2 as uirab, " +
        "(d.ipopust1+d.ipopust2)/d.ukupno*100 as uprab, " +
        "(d.iznos-d.por1-d.por2-d.por3)/d.kol as fc, " +
        "(d.iznos-d.por1-d.por2-d.por3) as ineto, " +
        "(d.neto-d.por1-d.por2-d.por3)/d.kol as fvc, " +
        "(d.neto-d.por1-d.por2-d.por3) as iprodbp, " +
        "d.por1, d.por2, d.por3, d.neto/d.kol as fmc, d.mc as fmcprp, " +
        "d.neto as iprodsp, d.ppor1, d.ppor2, d.ppor3 " +
        "from pos m, stpos d WHERE " + Util.getUtil().getDoc("m", "d") +
        " AND m.status='N' AND d.iznos!=0 AND d.kol!=0 AND " + csklCond + " and "
    );
   
    q.append(cond.qualified("m"));
    System.out.println("sql: "+q);
    
    String[] cols = {"CSKL", "BRDOK", "CART", "CART1", "BC", "NAZART", "JM", "KOL", 
        "REZKOL", "UIRAB", "UPRAB", "FC", "INETO", "FVC", 
        "IPRODBP", "POR1", "POR2", "POR3", "FMC", "FMCPRP", 
        "IPRODSP", "PPOR1", "PPOR2", "PPOR3", "STATUS"};
    String[] sumc = {"KOL", "UIRAB", "INETO", "IPRODBP", 
          "POR1", "POR2", "POR3", "IPRODSP"};
    StorageDataSet inter = stdoki.getDataModule().getScopedSet(cols);
    inter.getColumn("STATUS").setPrecision(30);
    hr.restart.util.Util.fillReadonlyData(inter, q.toString());
    if (!vhack && rep) {
      for (inter.first(); inter.inBounds(); inter.next())
        inter.setString("STATUS", inter.getString("STATUS").equalsIgnoreCase("R") ? "R" : "G");
      inter.setSort(new SortDescriptor(
          new String[] {"CSKL", "STATUS", "CART", "REZKOL", "UPRAB", "BRDOK"}));
    } else inter.setSort(new SortDescriptor(
        new String[] {"CSKL", "CART", "REZKOL", "UPRAB", "BRDOK"}));
    
    StorageDataSet group = stdoki.getDataModule().getScopedSet(cols);
    group.open();
    
    String stat = "";
    String cskl = "";
    int cart = -999;
    String rezkol = "";
    BigDecimal uprab = Aus.zero2;
    for (inter.first(); inter.inBounds(); inter.next()) {
      if (!inter.getString("CSKL").equals(cskl) ||
      		inter.getInt("CART") != cart || 
          !inter.getString("REZKOL").equals(rezkol) ||
          inter.getBigDecimal("UPRAB").compareTo(uprab) != 0 ||
          (rep && !vhack && !inter.getString("STATUS").equals(stat))) {
        group.insertRow(false);
        dM.copyColumns(inter, group, cols);
        cskl = inter.getString("CSKL");
        cart = inter.getInt("CART");
        rezkol = inter.getString("REZKOL");
        uprab = inter.getBigDecimal("UPRAB");
        stat = inter.getString("STATUS");
      } else {
        for (int i = 0; i < sumc.length; i++)
          Aus.add(group, sumc[i], inter, sumc[i]);
      }
    }
    group.setSort(new SortDescriptor(new String[] {"CSKL", "BRDOK"}));
    return group;
  }
  
}