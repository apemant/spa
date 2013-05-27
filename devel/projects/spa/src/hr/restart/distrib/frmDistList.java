package hr.restart.distrib;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import sun.awt.geom.AreaOp.AddOp;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Distart;
import hr.restart.baza.Distkal;
import hr.restart.baza.Distlist;
import hr.restart.baza.StDistkal;
import hr.restart.baza.StDistlist;
import hr.restart.baza.dM;
import hr.restart.robno.SanityCheck;
import hr.restart.robno.Util;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raUser;
import hr.restart.swing.raExtendedTable;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.util.reports.JTablePrintRun;

public class frmDistList extends raMasterDetail {
	raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
    
  jpDistListMaster jpMaster;
  jpDistListDetail jpDetail;
  
  raNavAction navOBR = new raNavAction("Obrada distribucijske liste",raImages.IMGMOVIE,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
    	realize();
    	    		
    }
  };

  public frmDistList() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public boolean isNewDetailNeeded() {
    return false;
 }
  
  public void SetFokusMaster(char mode) {
  	if (mode == 'N') {
  		getPreSelect().copySelValues();
  		getMasterSet().setTimestamp("DATUM", vl.getPresToday(getPreSelect().getSelRow(), "DATUM"));
  	}
  	if (getMasterSet().getString("SIFDIST").length() == 0)
  		jpMaster.jlrDist.requestFocus();
  	else jpMaster.jtfDATUM.requestFocus();
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jrfCSKL) || vl.isEmpty(jpMaster.jlrDist) || vl.isEmpty(jpMaster.jtfDATUM))
      return false;
    
    if (vl.isEmpty(jpMaster.rpc.jrfCART)) return false;
    
    getMasterSet().setInt("CART", Integer.parseInt(jpMaster.rpc.getCART()));
    
    if (mode == 'N') 
      if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Kreirati distribucijsku listu za distributera " +
    		getMasterSet().getString("SIFDIST") + " za dan " + Aus.formatTimestamp(getMasterSet().getTimestamp("DATUM")) + "?",
    				"Kreiranje distribucijske liste", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return false;
    return true;
  }
  
  public boolean DeleteCheckMaster() {
    if (getMasterSet().getString("STATUS").equals("D")) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Distribucijska lista je obraðena!",
          "Brisanje liste", JOptionPane.INFORMATION_MESSAGE);
      return false;
    }
  	seq = "DIST-" + getMasterSet().getString("GOD") + "-" + getMasterSet().getString("CSKL");
  	brdok = getMasterSet().getInt("BRDOK");
  	return Util.getUtil().checkSeq(seq, Integer.toString(getMasterSet().getInt("BRDOK")));
  }
  
  public boolean doBeforeSaveMaster(char mode) {
		if (mode == 'N') {
			cskl = getMasterSet().getString("CSKL");
			dat = getMasterSet().getTimestamp("DATUM");
			god = vl.findYear(dat);
			brdok = vl.findSeqInt("DIST-" + god + "-" + cskl);
			dist = getMasterSet().getString("SIFDIST");
			
			getMasterSet().setString("CUSER", raUser.getInstance().getUser());
			getMasterSet().setTimestamp("SYSDAT", vl.getToday());
			getMasterSet().setString("GOD", god);
			getMasterSet().setInt("BRDOK", brdok);
		}
		return true;
	}
  
  public boolean doWithSaveMaster(char mode) {
  	if (mode == 'B')
  	try {
  		Util.getUtil().delSeqCheck(seq, true, brdok); // / transakcija
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
  }
  
  
  public void AfterAfterSaveMaster(char mode) {
  	if (mode == 'N') {
  		raProcess.runChild(raMaster.getWindow(), new Runnable() {
				public void run() {
					generateDist();
				}
			});
  	}
  	super.AfterAfterSaveMaster(mode);
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
    } else if (mode == 'I') {
    } else {
    }
  }
  
  void updateColumn(DataSet ds, String col, String caption, int w) {
  	if (ds.hasColumn(col) != null) {
  		ds.hasColumn(col).setCaption(caption);
  		if (w == 0) ds.hasColumn(col).setVisible(0);
  		else ds.hasColumn(col).setWidth(w);
  	}
  }
  
  void addDiff(StorageDataSet diff, String naz, String prom, String opis) {
  	diff.insertRow(false);
		diff.setString("NAZIV", naz);
		diff.setString("PROM", prom);
		diff.setString("OPIS", opis);
  }
  
  public void Funkcija_ispisa_master() {
  	frmTableDataView view = new frmTableDataView();
  	StorageDataSet ds = Aus.q("SELECT " + getMasterSet().getInt("CART") +	" AS cart, '" + 
  			getMasterSet().getString("SIFDIST") + "' as CSIF, cdistart, rbr, naziv, adr, mj, kol from stdistlist "+
  			"WHERE " + Condition.whereAllEqual(new String[] {"CSKL", "GOD", "BRDOK"}, getMasterSet()));
  	updateColumn(ds, "CSKL", "", 0);
  	updateColumn(ds, "GOD", "", 0);
  	updateColumn(ds, "BRDOK", "", 0);
  	updateColumn(ds, "CDISTART", "", 0);
  	updateColumn(ds, "CSIF", "Distributer", 0);
  	updateColumn(ds, "CART", "Izdanje", 0);
  	updateColumn(ds, "RBR", "Rbr", 5);
  	updateColumn(ds, "NAZIV", "Primatelj", 20);
  	updateColumn(ds, "ADR", "Adresa", 40);
  	updateColumn(ds, "MJ", "Mjesto", 15);
  	updateColumn(ds, "KOL", "Kolièina", 8);
  	
  	view.setDataSet(ds);
  	
  	DataSet prev = findPrev(ds);
  	if (prev != null) {
  		StorageDataSet diff = new StorageDataSet();
  		diff.setColumns(new Column[] {
  				dM.createStringColumn("NAZIV", "Primatelj", 16),
  				dM.createStringColumn("PROM", "Promjena", 10),
  				dM.createStringColumn("OPIS", "Opis", 110),
  		});
  		diff.open();
  		diff.getColumn("OPIS").setWidth(90);
  		addDiff(diff, "POPIS PROMJENA", "","");
  		addDiff(diff, "", "", "");
  		
  		HashSet newc = new HashSet();
  		HashSet oldc = new HashSet();
  		for (ds.first(); ds.inBounds(); ds.next())
  			newc.add(new Integer(ds.getInt("CDISTART")));
  		
  		for (prev.first(); prev.inBounds(); prev.next())
  			oldc.add(new Integer(prev.getInt("CDISTART")));
  		
  		oldc.retainAll(newc);
  		for (prev.first(); prev.inBounds(); prev.next()) {
  			if (!oldc.contains(new Integer(prev.getInt("CDISTART"))))  
  				addDiff(diff, prev.getString("NAZIV"), "STOPIRAN", "");
  		}
  		
  		for (ds.first(); ds.inBounds(); ds.next()) {
  			if (!oldc.contains(new Integer(ds.getInt("CDISTART"))))
  				addDiff(diff, ds.getString("NAZIV"), "NOVI", "");
  			else if (ld.raLocate(prev, "CDISTART", Integer.toString(ds.getInt("CDISTART")))) {
  				if (!ds.getString("NAZIV").equals(prev.getString("NAZIV"))) 
  					addDiff(diff, ds.getString("NAZIV"), "Primatelj", prev.getString("NAZIV") + "  ->  " + ds.getString("NAZIV"));
  				if (!ds.getString("ADR").equals(prev.getString("ADR")))
  					addDiff(diff, ds.getString("NAZIV"), "Adresa", prev.getString("ADR") + "  ->  " + ds.getString("ADR"));
  				if (!ds.getString("MJ").equals(prev.getString("MJ")))
  					addDiff(diff, ds.getString("NAZIV"), "Mjesto", 
  							prev.getString("PBR") + " " + prev.getString("MJ") + "  ->  " + 
  							ds.getString("PBR") + " " + ds.getString("MJ"));
  				if (!ds.getBigDecimal("KOL").equals(prev.getBigDecimal("KOL")))
  					addDiff(diff, ds.getString("NAZIV"), "Kolièina", 
  							prev.getBigDecimal("KOL").stripTrailingZeros() + "  ->  " + 
  							ds.getBigDecimal("KOL").stripTrailingZeros());
  			}
  		}
  		
  		raExtendedTable td = new raExtendedTable();
  		td.setDataSet(diff);
  		td.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
  		view.jp.installSummary(td, 30, false);
  	}
  	
  	view.setTitle("Distribucijska lista za dan " + Aus.formatTimestamp(getMasterSet().getTimestamp("DATUM")));
    view.setVisibleCols(new int[] {0,1,2,3,4});
    raExtendedTable t = (raExtendedTable) view.jp.getMpTable();
    t.addToGroup("CSIF", true, new String[] {"#", "NAZIV"}, jpMaster.jlrDist.getRaDataSet(), true);
    t.addToGroup("CART", true, new String[] {"#", "NAZART"}, dm.getArtikli(), false);
    view.validate();
    view.print();
  }
  
  DataSet findPrev(DataSet ds) {
  	if (!ld.raLocate(dm.getArtikli(), "CART", Integer.toString(getMasterSet().getInt("CART")))) 
  		return null;
  	
  	String cgr = dm.getArtikli().getString("CGRART");
  	
    DataSet zag = Aus.q("SELECT * FROM distlist WHERE " +
    		Condition.whereAllEqual(new String[] {"CSKL", "GOD", "SIFDIST"}, getMasterSet()) +
    		" AND brdok < " + getMasterSet().getInt("BRDOK"));
    zag.setSort(new SortDescriptor(new String[] {"BRDOK"}));
    
    for (zag.last(); zag.inBounds(); zag.prior()) {
    	zag.getInt("CART");
    	if (ld.raLocate(dm.getArtikli(), "CART", Integer.toString(getMasterSet().getInt("CART"))))
    		if (dm.getArtikli().getString("CGRART").equals(cgr)) {
    			return Aus.q("SELECT " + getMasterSet().getInt("CART") +	" AS cart, '" + 
    	  			getMasterSet().getString("SIFDIST") + "' as CSIF, cdistart, rbr, naziv, adr, mj, kol from stdistlist "+
    	  			"WHERE " + Condition.whereAllEqual(new String[] {"CSKL", "GOD", "BRDOK"}, zag));
    		}
    	
    }
    return null;
  }

  public boolean ValidacijaDetail(char mode) {
    return true;
  }

  void realize() {
  	if (getMasterSet().rowCount() == 0) return;
  	boolean obr = getMasterSet().getString("STATUS").equals("D");
  	if (!obr) {
    	if (JOptionPane.showConfirmDialog(raDetail.getWindow(), "Ažurirati primatelje i skladište?",
    			"Realizacija", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
  	} else {
  		if (JOptionPane.showConfirmDialog(raDetail.getWindow(), "Poništiti obradu distribucije?",
    			"Realizacija", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
  	}

  	DataSet all = StDistlist.getDataModule().getTempSet(
  			Condition.whereAllEqual(new String[] {"CSKL", "GOD", "BRDOK"}, getMasterSet()));
  	all.open();
  	
  	QueryDataSet dist = Distart.getDataModule().getTempSet();
  	dist.open();
  	
  	for (all.first(); all.inBounds(); all.next()) {
  		if (ld.raLocate(dist, "CDISTART", Integer.toString(all.getInt("CDISTART")))) {
  			if (obr) {
  				Aus.sub(dist, "KOLIZ", all, "KOL");
  				if (dist.getBigDecimal("KOLUL").compareTo(dist.getBigDecimal("KOLIZ")) > 0) {
  					dist.setString("AKTIV", "D");
  				}
  			} else {
  				Aus.add(dist, "KOLIZ", all, "KOL");
  				if (dist.getBigDecimal("KOLUL").compareTo(dist.getBigDecimal("KOLIZ")) <= 0) {
  					dist.setString("AKTIV", "N");
  				}
  			}
  		}
  	}
  	
  	getMasterSet().setString("STATUS", obr ? "N" : "D");
  	if (raTransaction.saveChangesInTransaction(new QueryDataSet[] {getMasterSet(), dist})) {
  		JOptionPane.showMessageDialog(raMaster.getWindow(), obr ? "Distribucija poništena!" : "Distribucija obraðena!", 
  				"Ažuriranje distribucije", JOptionPane.INFORMATION_MESSAGE);
  		raMaster.getJpTableView().fireTableDataChanged();
  	} else {
  		JOptionPane.showMessageDialog(raMaster.getWindow(), "Greška kod ažuriranja distribucije!", "Greška", JOptionPane.ERROR_MESSAGE);
  	}
  }


  Timestamp dat;
  String cskl, god, dist, seq;
  int brdok;
  void generateDist() {
  	if (!ld.raLocate(dm.getArtikli(), "CART", Integer.toString(getMasterSet().getInt("CART")))) {
  		JOptionPane.showMessageDialog(raMaster.getWindow(), "Pogrešan artikl za distribuciju!", "Greška", JOptionPane.ERROR_MESSAGE);
  		return;
  	}
  	
  	DataSet ds = Distart.getDataModule().getTempSet(Condition.equal("SIFDIST", dist).
  			and(Condition.equal("AKTIV", "D")).and(Condition.equal("CGRART", dm.getArtikli())));
  	ds.open();
  	
  	QueryDataSet st = StDistlist.getDataModule().getTempSet();
  	st.open();
  	
  	DataSet kal = StDistkal.getDataModule().getTempSet(Condition.between("DATISP", dat, dat));
  	kal.open();
  	
  	DataSet zk = Distkal.getDataModule().getTempSet();
  	zk.open();
  	
  	String[] cc = {"CDISTART", "CPAR", "NAZIV", "MJ", "ADR", "PBR", "TEL", "TELFAX", "EMADR", "KOL"};
  	
  	short rbr = 0;
  	for (ds.first(); ds.inBounds(); ds.next()) {
  		int ckal = ds.getInt("CDISTKAL");
  		boolean skip = false;
  		while (!skip) {
  			skip = ld.raLocate(kal, new String[] {"CDISTKAL", "FLAGADD"}, new String[] {Integer.toString(ckal), "N"});
  			if (!skip) {
  				if (ld.raLocate(kal, new String[] {"CDISTKAL", "FLAGADD"}, new String[] {Integer.toString(ckal), "D"})) break;
  				if (!ld.raLocate(zk, "CDISTKAL", Integer.toString(ckal))) skip = true;
  				else if (zk.getInt("CINHERITDISTKAL") > 0 && zk.getInt("CINHERITDISTKAL") != ckal)
  					ckal = zk.getInt("CINHERITDISTKAL");
  				else skip = true;
  			}
  		}
  		if (!skip) {
  			st.insertRow(false);
  			st.setString("CSKL", cskl);
  			st.setString("GOD", god);
  			st.setInt("BRDOK", brdok);
  			st.setShort("RBR", ++rbr);
  			st.setString("SIFDIST", dist);
  			dM.copyColumns(ds, st, cc);
  		}
  	}
  	
  	if (st.rowCount() == 0) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Ne postoji grupa artikla za distribuciju!", "Greška", JOptionPane.ERROR_MESSAGE);
  	} else {
    	st.saveChanges();
    	getDetailSet().refresh();
  	}
  }
  
  public void beforeShowMaster() {
  	jpMaster.initRpc();
  }
  
  private void jbInit() throws Exception {
    this.setMasterSet(Distlist.getDataModule().getQueryDataSet());
    this.setNaslovMaster("Distribucijske liste");
    this.setVisibleColsMaster(new int[] {3, 4, 5});
    this.setMasterKey(new String[] {"CSKL", "GOD", "BRDOK"});
    jpMaster = new jpDistListMaster(this);
    this.setJPanelMaster(jpMaster);
    raMaster.addOption(navOBR, 4, false);
    setMasterDeleteMode(DELDETAIL);

    this.setDetailSet(StDistlist.getDataModule().getQueryDataSet());
    this.setNaslovDetail("Stavke distribucijske liste"); /**@todo: Naslov detaila */
    this.setVisibleColsDetail(new int[] {3, 6, 8, 9, 7, 14});
    this.setDetailKey(new String[] {"CSKL", "GOD", "BRDOK", "RBR"});
    jpDetail = new jpDistListDetail(this);
    this.setJPanelDetail(jpDetail);
  }
}
