package hr.restart.pos;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.robno.Util;
import hr.restart.robno.rapancskl;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCpar;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raTableRunningSum;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raJPTableView;
import hr.restart.util.raUpitLite;
import hr.restart.util.raLoader;

public class upStanjePos extends raUpitLite {
	
	hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  dM dm = hr.restart.baza.dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  
  TableDataSet tds = new TableDataSet();
  
  JPanel mainPanel = new JPanel();
  XYLayout mainXYLayout = new XYLayout();
  
  rapancskl rpcskl = new rapancskl() {
    public void findFocusAfter() {
    	jpc.focusCpar();
    }
  };
  
  jpCpar jpc = new jpCpar();
  
  JraTextField jtfZavDatum = new JraTextField();
  JLabel jlDatum = new JLabel();
  
  frmTableDataView ret;
  
  public upStanjePos() {
  	try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    mainXYLayout.setWidth(640);
    mainXYLayout.setHeight(150);
    this.setJPan(mainPanel);
    mainPanel.setLayout(mainXYLayout);
    
    tds.setColumns(new Column[] {dm.createStringColumn("CSKL","Prodajno mjesto",12),
    		dm.createIntColumn("CART","Dobavljaè"),
    		dm.createTimestampColumn("pocDatum", "Poèetni datum"),
        dm.createTimestampColumn("zavDatum", "Krajnji datum")
        });
    tds.open();
    
    jlDatum.setText("Na dan");

    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(tds);
    
    rpcskl.setRaMode('S');
    jpc.bind(tds);
    
    mainPanel.add(rpcskl, new XYConstraints(0, 0, -1, -1));
    mainPanel.add(jpc, new XYConstraints(0, 55, -1, -1));
    mainPanel.add(jlDatum,   new XYConstraints(15, 115, -1, -1));
    mainPanel.add(jtfZavDatum, new XYConstraints(150, 115, 100, -1));
  }
  

	public void componentShow() {
    tds.setTimestamp("pocDatum", vl.getToday());
    tds.setTimestamp("zavDatum", vl.getToday());
    rpcskl.setDisab('N');
    rpcskl.setCSKL("");
    jpc.clear();
	}

	public void firstESC() {
    rpcskl.setDisab('N');
    rpcskl.setCSKL("");
	}
	
	public boolean Validacija() {
    if (rpcskl.getCSKL().length() == 0) {
      rpcskl.setCSKL("");
      JOptionPane.showConfirmDialog(getWindow(),"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    //if (!Aus.checkDateRange(jtfPocDatum, jtfZavDatum)) return false;
    return true; 
  }

	protected void upitCompleted() {
    if (ret != null) ret.show();
    ret = null;
  }

  public void afterOKPress() {
    rcc.EnabDisabAll(mainPanel, true);
  }

  public void okPress() {
    tds.setTimestamp("POCDATUM", ut.getYearBegin(vl.getKnjigYear("robno")));
    System.out.println(tds);
    
  	String us = "SELECT doku.vrdok, doku.datdok, stdoku.cart1, stdoku.kol, stdoku.inab, stdoku.nc, stdoku.mc, stdoku.izad, stdoku.porav " +
		"FROM doku, stdoku WHERE " + Util.getUtil().getDoc("doku", "stdoku") + " and " + upProdaja.uldok + " and " +
		Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
				Condition.equal("CSKL", rpcskl.getCSKL())).qualified("doku");
  	System.out.println(us);
  	DataSet du = Aus.q(us);
  	du.setSort(new SortDescriptor(new String[] {"CART1", "DATDOK"}));
  	
  	String is = "SELECT doki.vrdok, stdoki.cart1, stdoki.kol, stdoki.inab, stdoki.mc, stdoki.iraz, " +
		"stdoki.uirab, stdoki.iprodbp, stdoki.iprodsp, stdoki.veza, stdoki.id_stavka FROM doki,stdoki WHERE " +
		Util.getUtil().getDoc("doki", "stdoki") + " and " + upProdaja.izdok + " and " +
		Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
				Condition.equal("CSKL", rpcskl.getCSKL())).qualified("doki");
  	System.out.println(is);
  	DataSet di = Aus.q(is);
  	di.setSort(new SortDescriptor(new String[] {"CART1"}));
  	
  	String ps = "SELECT stpos.cart1, stpos.kol, stpos.mc, stpos.ukupno, stpos.neto FROM pos,stpos WHERE " +
		Util.getUtil().getDoc("pos", "stpos") + " and pos.status!='P' and " +
		Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
				Condition.equal("CSKL", rpcskl.getCSKL())).qualified("pos");
  	System.out.println(ps);
  	DataSet pi = Aus.q(ps);
  	
  	StorageDataSet res = new StorageDataSet();
    res.setColumns(new Column[] {
    		dM.createStringColumn("CORG", "Dobavljaè", 12),
    		dM.createIntColumn("CPAR", "Komisionar"),
    		dM.createStringColumn("CART1", "Artikl", 20),
    		dM.createStringColumn("NAZART", "Naziv", 75),
    		dM.createStringColumn("JM", "Jm", 3),
    		dM.createBigDecimalColumn("KOLUL", "Ulaz", 3),
    		dM.createBigDecimalColumn("KOLIZ", "Izlaz", 3),
    		dM.createBigDecimalColumn("KOL", "Kolièina", 3),
    		dM.createBigDecimalColumn("NC", "Nab. cijena", 2),
    		dM.createBigDecimalColumn("NABUL", "Nab. ulaz", 2),
    		dM.createBigDecimalColumn("NABIZ", "Nab. izlaz", 2),
    		dM.createBigDecimalColumn("NABVRI", "Nab. vrijednost", 2),
    		dM.createBigDecimalColumn("MC", "Cijena", 2),
    		dM.createBigDecimalColumn("IZAD", "Zaduženje", 2),
    		dM.createBigDecimalColumn("IRAZ", "Razduženje", 2),
    		dM.createBigDecimalColumn("VRI", "Vrijednost", 2),
    		dM.createBigDecimalColumn("POP", "Popust", 2),
    		dM.createBigDecimalColumn("NETO", "Utržak", 2),
    		dM.createBigDecimalColumn("ERR", "Razlika vrijednosti", 2),
    });
    res.open();
    String py = ut.getYear(tds.getTimestamp("pocDatum"));
    
    String cart = "";
  	for (du.first(); du.inBounds(); du.next()) {
  	  if (du.getString("VRDOK").equals("PST") && !ut.getYear(du.getTimestamp("DATDOK")).equals(py)) continue;
  	
  		if (!du.getString("CART1").equals(cart)) {
  			ld.raLocate(dm.getArtikli(), "CART1", cart = du.getString("CART1"));
  			if (!jpc.isEmpty() && dm.getArtikli().getInt("CPAR") != jpc.getCpar()) continue;
  			
  			res.insertRow(false);
  			res.setString("CORG", rpcskl.getCSKL());
  			res.setInt("CPAR", dm.getArtikli().getInt("CPAR"));
  			res.setString("CART1", cart);
  			res.setString("NAZART", dm.getArtikli().getString("NAZART"));
  			res.setString("JM", dm.getArtikli().getString("JM"));
  		} else if (!res.getString("CART1").equals(cart)) continue;
  		Aus.add(res, "KOLUL", du, "KOL");
  		Aus.add(res, "KOL", du);
  		Aus.add(res, "NABUL", du, "INAB");
  		Aus.add(res, "NABVRI", du, "INAB");
  		Aus.add(res, "IZAD", du);
  		Aus.add(res, "VRI", du, "IZAD");
  		Aus.add(res, "IZAD", du, "PORAV");
  		Aus.add(res, "VRI", du, "PORAV");
  		Aus.set(res, "MC", du);
  		if (res.getBigDecimal("KOL").abs().intValue() >=1)
  			Aus.div(res, "NC", "NABUL", "KOL");
  		
  		Aus.mul(res, "ERR", "KOL", "MC");
  		Aus.sub(res, "ERR", "VRI");
  	}
  	
  	HashMap inab = new HashMap();
  	HashMap iraz = new HashMap();
  	for (di.first(); di.inBounds(); di.next()) {
  		if ((di.getString("VRDOK").equals("IZD") || di.getString("VRDOK").equals("OTP"))
  				&& di.getString("VEZA").length() > 0) {
  			inab.put(di.getString("VEZA"), di.getBigDecimal("INAB"));
  			iraz.put(di.getString("VEZA"), di.getBigDecimal("IRAZ"));
  		}
  	}
  	
  	cart = "";
  	for (di.first(); di.inBounds(); di.next()) {
  		if ((di.getString("VRDOK").equals("IZD") || di.getString("VRDOK").equals("OTP"))
  				&& di.getString("VEZA").length() > 0) continue;
  		
  		if (!di.getString("CART1").equals(cart) && !ld.raLocate(res, "CART1", cart = di.getString("CART1"))) {
  			ld.raLocate(dm.getArtikli(), "CART1", cart = di.getString("CART1"));
  			if (!jpc.isEmpty() && dm.getArtikli().getInt("CPAR") != jpc.getCpar()) continue;
  			
  			res.insertRow(false);
  			res.setString("CORG", rpcskl.getCSKL());
  			res.setInt("CPAR", dm.getArtikli().getInt("CPAR"));
  			res.setString("CART1", cart);
  			res.setString("NAZART", dm.getArtikli().getString("NAZART"));
  			res.setString("JM", dm.getArtikli().getString("JM"));
  		} else if (!res.getString("CART1").equals(cart)) continue;
  		
  		Aus.add(res, "KOLIZ", di, "KOL");
  		Aus.sub(res, "KOL", di);
  		BigDecimal sn = (BigDecimal) inab.get(di.getString("ID_STAVKA"));
  		if (sn != null) {
  			Aus.add(res, "NABIZ", sn);
  		} else {
  			Aus.add(res, "NABIZ", di, "INAB");
  		}
  		BigDecimal sm = (BigDecimal) iraz.get(di.getString("ID_STAVKA"));
  		if (sm != null) {
  			Aus.add(res, "IRAZ", sm);
  		} else {
  			Aus.add(res, "IRAZ", di);
  		}
  		Aus.sub(res, "NABVRI", "NABUL", "NABIZ");
  		if (res.getBigDecimal("KOL").abs().intValue() >=1)
  		  Aus.div(res, "NC", "NABVRI", "KOL");
  		
  		Aus.sub(res, "VRI", "IZAD", "IRAZ");
  		Aus.add(res, "POP", di, "UIRAB");
  		Aus.add(res, "NETO", di, "IPRODSP");
  		
  		Aus.mul(res, "ERR", "KOL", "MC");
        Aus.sub(res, "ERR", "VRI");
  	}
  	
  	cart = "";
  	for (pi.first(); pi.inBounds(); pi.next()) {
  		if (!pi.getString("CART1").equals(cart) && !ld.raLocate(res, "CART1", cart = pi.getString("CART1"))) {
  			ld.raLocate(dm.getArtikli(), "CART1", cart = pi.getString("CART1"));
  			if (!jpc.isEmpty() && dm.getArtikli().getInt("CPAR") != jpc.getCpar()) continue;
  			
  			res.insertRow(false);
  			res.setString("CORG", rpcskl.getCSKL());
  			res.setInt("CPAR", dm.getArtikli().getInt("CPAR"));
  			res.setString("CART1", cart);
  			res.setString("NAZART", dm.getArtikli().getString("NAZART"));
  			res.setString("JM", dm.getArtikli().getString("JM"));
  		} else if (!res.getString("CART1").equals(cart)) continue;
  		
  		Aus.add(res, "KOLIZ", pi, "KOL");
  		Aus.sub(res, "KOL", pi);
  		
  		Aus.add(res, "POP", pi, "UKUPNO");
  		Aus.sub(res, "POP", pi, "NETO");
  		Aus.add(res, "NETO", pi);
  	}
  	
  	res.setSort(new SortDescriptor(new String[] {"CORG", "CPAR", "CART1"}));
  	
  	ret = new frmTableDataView() { 
  	  protected void doubleClick(raJPTableView jp2) {
  	    showKartica(jp2.getStorageDataSet().getString("CORG"),
  	        jp2.getStorageDataSet().getString("CART1"));
  	  }
  	};
    ret.setDataSet(res);
    ret.setSums(new String[] {"NABUL", "NABIZ", "IZAD", "IRAZ", "VRI", "POP", "NETO"});
    ret.setSaveName("Pregled-stanje-pos");
    ret.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    ret.setTitle("Prikaz stanja artikala  na dan " + 
              Aus.formatTimestamp(tds.getTimestamp("zavDatum")));
    ret.setVisibleCols(new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14});
    raExtendedTable t = (raExtendedTable) ret.jp.getMpTable();
    t.setForcePage(true);
    if (jpc.isEmpty())
    	t.addToGroup("CORG", true, new String[] {"#", "NAZIVLOG", "#\n", "ADRESA", "#,", "PBR", "MJESTO", "#, OIB", "OIB"}, 
    		dM.getDataModule().getLogotipovi(), true);
    else
    	t.addToGroup("CPAR", true, new String[] {"#", "NAZPAR", "#\n", "ADR", "#,", "PBR", "MJ", "#, OIB", "OIB"}, 
	    		dm.getPartneri(), true);
  }
  
  void showKartica(String corg, String cart1) {
    upKarticaPos ukp = (upKarticaPos) raLoader.load("hr.restart.pos.upKarticaPos");
    ukp.tds.setString("CSKL", corg);
    ld.raLocate(dm.getArtikli(), "CART1", cart1);
    ukp.tds.setInt("CART", dm.getArtikli().getInt("CART"));
    ukp.tds.setTimestamp("pocDatum", ut.getYearBegin(vl.getKnjigYear("robno")));
    ukp.tds.setTimestamp("zavDatum", tds.getTimestamp("zavDatum"));
    ukp.check = false;
    ukp.ok_action_thread();
  }
  
  public boolean isIspis() {
    return false;
  }

  public void ispis() {
    //
  }

  public boolean ispisNow() {
    return false;
  }

  public boolean runFirstESC() {
    return rpcskl.getCSKL().length()>0;
  }
}
