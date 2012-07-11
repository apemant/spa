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
import hr.restart.robno.rapancart;
import hr.restart.robno.rapancskl;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raTableRunningSum;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raUpitLite;


public class upKarticaPos extends raUpitLite {

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
      rpcart.setCskl(rpcskl.getCSKL());
      rpcart.setGodina(vl.findYear(tds.getTimestamp("pocDatum")));
      if (rpcart.getCART().length() == 0) {
        rpcart.setDefParam();
        rpcart.setCART();
      }
    }
  };
  
  rapancart rpcart = new rapancart() {
    public void nextTofocus(){
      jtfPocDatum.requestFocus();
    }
  };
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  JLabel jlDatum = new JLabel();
  
  frmTableDataView ret;
  
  
  public upKarticaPos() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    mainXYLayout.setWidth(640);
    mainXYLayout.setHeight(175);
    this.setJPan(mainPanel);
    mainPanel.setLayout(mainXYLayout);
    
    tds.setColumns(new Column[] {dm.createStringColumn("CSKL","Prodajno mjesto",12),
        dm.createIntColumn("CART", "Artikl"),
        dm.createTimestampColumn("pocDatum", "Poèetni datum"),
        dm.createTimestampColumn("zavDatum", "Krajnji datum"),
        });
    tds.open();
    
    jlDatum.setText("Datum (od-do)");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);

    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(tds);
    
    new raDateRange(jtfPocDatum, jtfZavDatum);
    
    rpcart.setMode("DOH");
    rpcart.setBorder(null);
    rpcskl.setRaMode('S');
    rpcskl.setDataSet(tds);
    
    mainPanel.add(rpcskl, new XYConstraints(0, 0, -1, -1));
    mainPanel.add(rpcart, new XYConstraints(0, 50, -1, -1));
    mainPanel.add(jlDatum,   new XYConstraints(15, 140, -1, -1));
    mainPanel.add(jtfPocDatum, new XYConstraints(150, 140, 100, -1));
    mainPanel.add(jtfZavDatum, new XYConstraints(255, 140, 100, -1));
  }
  
  public void componentShow() {
    tds.open();
    tds.setTimestamp("pocDatum", ut.getYearBegin(vl.getKnjigYear("robno")));
    tds.setTimestamp("zavDatum", vl.getToday());
    rpcart.EnabDisab(true);
    rpcart.clearFields();
    rpcskl.setDisab('N');
    rpcskl.setCSKL("");
    check = true;
  }

  public void firstESC() {
    if (rpcart.getCART().length()>0) {
      rpcart.EnabDisab(true);
      rpcart.setCART();
      return;
    }
    rpcart.clearFields();
    rpcskl.setDisab('N');
    rpcskl.setCSKL("");
  }
  
  public boolean check = true;
  public boolean Validacija() {
    if (!check) return check = true;
    
    if (rpcskl.getCSKL().length() == 0) {
      rpcskl.setCSKL("");
      JOptionPane.showConfirmDialog(getWindow(),"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (rpcart.getCART().length() == 0) {
      rpcart.setCART();
      JOptionPane.showConfirmDialog(getWindow(),"Obavezan unos artikla !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!Aus.checkDateRange(jtfPocDatum, jtfZavDatum)) return false;
    tds.setInt("CART", Aus.getNumber(rpcart.getCART()));
    return true; 
  }
  
  protected void upitCompleted() {
    if (ret != null) ret.show();
    ret = null;
  }

  public void afterOKPress() {
    rcc.EnabDisabAll(mainPanel, true);
    check = true;
  }

  public void okPress() {
  	String us = "SELECT doku.vrdok, doku.brdok, doku.datdok, stdoku.kol, stdoku.inab, stdoku.nc, stdoku.mc, stdoku.izad, stdoku.skol, stdoku.porav " +
  			"FROM doku, stdoku WHERE " + Util.getUtil().getDoc("doku", "stdoku") + " and " + upProdaja.uldok + " and " +
  			Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
						Condition.equal("CSKL", tds)).qualified("doku").and(Condition.equal("CART", tds).qualified("stdoku"));
  	System.out.println(us);
  	DataSet du = Aus.q(us);
  	
  	String is = "SELECT doki.vrdok, doki.brdok, doki.datdok, stdoki.kol, stdoki.nc, stdoki.inab, stdoki.mc, stdoki.iraz, " +
  			"stdoki.uirab, stdoki.iprodbp, stdoki.iprodsp, stdoki.veza, stdoki.id_stavka FROM doki,stdoki WHERE " +
  			Util.getUtil().getDoc("doki", "stdoki") + " and " + upProdaja.izdok + " and doki.vrdok!='POS' and " +
  			Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
  			  Condition.equal("CSKL", tds)).qualified("doki").and(Condition.equal("CART", tds).qualified("stdoki"));
  	System.out.println(is);
  	DataSet di = Aus.q(is);
  	
  	String ps = "SELECT pos.brdok, pos.datdok, stpos.kol, stpos.mc, stpos.ukupno, stpos.neto, pos.rdok FROM pos,stpos WHERE " +
    Util.getUtil().getDoc("pos", "stpos") + " and " +
    Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
        Condition.equal("CSKL", tds)).qualified("pos").and(Condition.equal("CART", tds).qualified("stpos"));
  	System.out.println(ps);
    DataSet pi = Aus.q(ps);
  	
  	StorageDataSet res = new StorageDataSet();
    res.setColumns(new Column[] {
    		dM.createStringColumn("CORG", "Dobavljaè", 12),
    		dM.createStringColumn("DOK", "Dokument", 10),
    		dM.createTimestampColumn("DATDOK", "Datum"),
    		dM.createBigDecimalColumn("KOLUL", "Ulaz", 3),
    		dM.createBigDecimalColumn("KOLIZ", "Izlaz", 3),
    		dM.createBigDecimalColumn("KOL", "Kolièina", 3),
    		dM.createBigDecimalColumn("NC", "Nab. cijena", 2),
    		dM.createBigDecimalColumn("NABUL", "Nab. ulaz", 2),
    		dM.createBigDecimalColumn("NABIZ", "Nab. izlaz", 2),
    		dM.createBigDecimalColumn("MC", "Cijena", 2),
    		dM.createBigDecimalColumn("IZAD", "Zaduženje", 2),
    		dM.createBigDecimalColumn("IRAZ", "Razduženje", 2),
    		dM.createBigDecimalColumn("VRI", "Vrijednost", 2),
    		dM.createBigDecimalColumn("POP", "Popust", 2),
    		dM.createBigDecimalColumn("NETO", "Utržak", 2)
    });
    res.open();
    int py = Aus.getNumber(ut.getYear(tds.getTimestamp("pocDatum")));
    
  	for (du.first(); du.inBounds(); du.next()) {
  		if (du.getString("VRDOK").equals("PST") && Aus.getNumber(ut.getYear(du.getTimestamp("DATDOK")))>py) continue;
  		
  		if ((du.getString("VRDOK").equals("PRK") || du.getString("VRDOK").equals("PTE")) && du.getBigDecimal("PORAV").signum() != 0
  				|| du.getString("VRDOK").equals("POR")) {
  			res.insertRow(false);
    		res.setString("CORG", tds.getString("CSKL"));
    		res.setString("DOK", "POR-"+du.getInt("BRDOK"));
    		res.setTimestamp("DATDOK", du.getTimestamp("DATDOK"));
    		Aus.clear(res, "KOLUL");
    		Aus.clear(res, "KOLIZ");
    		Aus.clear(res, "KOL");
    		Aus.set(res, "NC", du);
    		Aus.clear(res, "NABUL");
    		Aus.clear(res, "NABIZ");
    		Aus.set(res, "IZAD", du, "PORAV");
    		Aus.set(res, "MC", du);
    		Aus.clear(res, "IRAZ");
    		Aus.set(res, "VRI", "IZAD");
    		Aus.clear(res, "POP");
    		Aus.clear(res, "NETO");
  		} 
  		if (!du.getString("VRDOK").equals("POR")) {
	  		res.insertRow(false);
	  		res.setString("CORG", tds.getString("CSKL"));
	  		res.setString("DOK", du.getString("VRDOK")+"-"+du.getInt("BRDOK"));
	  		res.setTimestamp("DATDOK", du.getTimestamp("DATDOK"));
	  		Aus.set(res, "KOLUL", du, "KOL");
	  		Aus.clear(res, "KOLIZ");
	  		Aus.set(res, "KOL", "KOLUL");
	  		Aus.set(res, "NC", du);
	  		Aus.set(res, "NABUL", du, "INAB");
	  		Aus.clear(res, "NABIZ");
	  		Aus.set(res, "MC", du);
	  		Aus.set(res, "IZAD", du);
	  		Aus.clear(res, "IRAZ");
	  		Aus.set(res, "VRI", "IZAD");
	  		Aus.clear(res, "POP");
	  		Aus.clear(res, "NETO");
  		}
  	}
  	
  	HashMap inc = new HashMap();
  	HashMap imc = new HashMap();
  	for (di.first(); di.inBounds(); di.next()) {
  		if ((di.getString("VRDOK").equals("IZD") || di.getString("VRDOK").equals("OTP"))
  				&& di.getString("VEZA").length() > 0) {
  		  String st = di.getString("VEZA");
          int last = st.lastIndexOf('-');
          if (last > 0) st = st.substring(0, last);
          last = st.lastIndexOf('-');
          if (last > 0) st = st.substring(0, last + 1);
          inc.put(st, di.getBigDecimal("NC"));
          imc.put(st, di.getBigDecimal("MC"));
  		  continue;
  		}
  		
  		res.insertRow(false);
  		res.setString("CORG", tds.getString("CSKL"));
  		res.setString("DOK", di.getString("VRDOK")+"-"+di.getInt("BRDOK"));
  		res.setTimestamp("DATDOK", di.getTimestamp("DATDOK"));
  		Aus.clear(res, "KOLUL");
  		Aus.set(res, "KOLIZ", di, "KOL");
  		Aus.sub(res, "KOL", "KOLUL", "KOLIZ");
		Aus.set(res, "NC", di);
		Aus.set(res, "NABIZ", di, "INAB");
		Aus.set(res, "MC", di);
		Aus.set(res, "IRAZ", di);
  		Aus.clear(res, "IZAD");
  		Aus.sub(res, "VRI", "IZAD", "IRAZ");
  		Aus.set(res, "POP", di, "UIRAB");
  		Aus.set(res, "NETO", di, "IPRODSP");
  	}
  	for (pi.first(); pi.inBounds(); pi.next()) {
  	  String rdok = pi.getString("RDOK");
  	  res.insertRow(false);
  	  res.setString("CORG", tds.getString("CSKL"));
  	  res.setString("DOK", "GRC-"+pi.getInt("BRDOK"));
  	  res.setTimestamp("DATDOK", pi.getTimestamp("DATDOK"));
  	  Aus.clear(res, "KOLUL");
  	  Aus.set(res, "KOLIZ", pi, "KOL");
  	  Aus.sub(res, "KOL", "KOLUL", "KOLIZ");
  	  
  	  BigDecimal nc = (BigDecimal) inc.get(rdok);
  	  if (nc == null) {
  	    Aus.clear(res, "NC");
  	    Aus.clear(res, "NABIZ");
  	  } else {
  	    res.setBigDecimal("NC", nc);
  	    Aus.mul(res, "NABIZ", "NC", "KOLIZ");
  	  }
  	  Aus.set(res, "MC", pi);
  	  BigDecimal mc = (BigDecimal) imc.get(rdok);
  	  if (mc == null) {
  	    Aus.clear(res, "IRAZ");
  	  } else {
  	    res.setBigDecimal("MC", mc);
  	    Aus.mul(res, "IRAZ", "MC", "KOLIZ");
  	  }
  	  
      Aus.clear(res, "IZAD");
      Aus.sub(res, "VRI", "IZAD", "IRAZ");
      Aus.set(res, "NETO", pi, "NETO");
      Aus.set(res, "POP", pi, "UKUPNO");
      Aus.sub(res, "POP", "NETO");
  	}
  	res.setSort(new SortDescriptor(new String[] {"DATDOK"}));
  	
  	ld.raLocate(dm.getArtikli(), "CART", tds.getInt("CART")+"");
  	
  	ret = new frmTableDataView();
    ret.setDataSet(res);
    ret.setSums(new String[] {"KOLUL", "KOLIZ", "KOL", "NABUL", "NABIZ", "IZAD", "IRAZ", "VRI", "POP", "NETO"});
    ret.setSaveName("Pregled-kartica-pos");
    ret.jp.addTableModifier(new raTableRunningSum("KOL"));
    ret.jp.addTableModifier(new raTableRunningSum("VRI"));
    ret.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    ret.setTitle("Prikaz kartice artikla " + dm.getArtikli().getString("CART1")
              + " " + dm.getArtikli().getString("NAZART") + "  od " + 
              Aus.formatTimestamp(tds.getTimestamp("pocDatum")) + " do " +
              Aus.formatTimestamp(tds.getTimestamp("zavDatum")));
    ret.setVisibleCols(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14});
    raExtendedTable t = (raExtendedTable) ret.jp.getMpTable();
    t.setForcePage(true);
    t.addToGroup("CORG", true, new String[] {"#", "NAZIVLOG", "#\n", "ADRESA", "#,", "PBR", "MJESTO", "#, OIB", "OIB"}, 
    		dM.getDataModule().getLogotipovi(), true);
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
