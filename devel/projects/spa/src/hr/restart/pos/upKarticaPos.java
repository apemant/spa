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
    
    mainPanel.add(rpcskl, new XYConstraints(0, 0, -1, -1));
    mainPanel.add(rpcart, new XYConstraints(0, 50, -1, -1));
    mainPanel.add(jlDatum,   new XYConstraints(15, 140, -1, -1));
    mainPanel.add(jtfPocDatum, new XYConstraints(150, 140, 100, -1));
    mainPanel.add(jtfZavDatum, new XYConstraints(255, 140, 100, -1));
  }
  
  public void componentShow() {
    tds.open();
    tds.setTimestamp("pocDatum", vl.getToday());
    tds.setTimestamp("zavDatum", vl.getToday());
    rpcart.EnabDisab(true);
    rpcart.clearFields();
    rpcskl.setDisab('N');
    rpcskl.setCSKL("");
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
  
  public boolean Validacija() {
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
  	String us = "SELECT doku.vrdok, doku.brdok, doku.datdok, stdoku.kol, stdoku.inab, stdoku.nc, stdoku.mc, stdoku.izad, stdoku.skol, stdoku.porav " +
  			"FROM doku, stdoku WHERE " + Util.getUtil().getDoc("doku", "stdoku") + " and doku.vrdok in ('PRK','PST','POR','PTE') and " +
  			Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
						Condition.equal("CSKL", rpcskl.getCSKL())).qualified("doku") + " and stdoku.cart = " + rpcart.getCART();
  	System.out.println(us);
  	DataSet du = Aus.q(us);
  	
  	String is = "SELECT doki.vrdok, doki.brdok, doki.datdok, stdoki.kol, stdoki.inab, stdoki.mc, stdoki.iraz, " +
  			"stdoki.uirab, stdoki.iprodbp, stdoki.iprodsp, stdoki.veza, stdoki.id_stavka FROM doki,stdoki WHERE " +
  			Util.getUtil().getDoc("doki", "stdoki") + " and doki.vrdok in ('POS','IZD','ROT','RAC','OTP','POD') and " +
  			Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
						Condition.equal("CSKL", rpcskl.getCSKL())).qualified("doki") + " and stdoki.cart = " + rpcart.getCART();
  	System.out.println(is);
  	DataSet di = Aus.q(is);
  	
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
  		
  		if (du.getString("VRDOK").equals("PRK") && du.getBigDecimal("PORAV").signum() != 0
  				|| du.getString("VRDOK").equals("POR")) {
  			res.insertRow(false);
    		res.setString("CORG", rpcskl.getCSKL());
    		res.setString("DOK", "POR-"+du.getInt("BRDOK"));
    		res.setTimestamp("DATDOK", du.getTimestamp("DATDOK"));
    		Aus.set(res, "KOLUL", du, "SKOL");
    		Aus.clear(res, "KOLIZ");
    		Aus.clear(res, "KOL");
    		Aus.set(res, "NC", du);
    		Aus.clear(res, "NABUL");
    		Aus.clear(res, "NABIZ");
    		Aus.set(res, "IZAD", du, "PORAV");
    		Aus.div(res, "MC", "IZAD", "KOLUL");
    		Aus.clear(res, "IRAZ");
    		Aus.set(res, "VRI", "IZAD");
    		Aus.clear(res, "POP");
    		Aus.clear(res, "NETO");
  		} 
  		if (!du.getString("VRDOK").equals("POR")) {
	  		res.insertRow(false);
	  		res.setString("CORG", rpcskl.getCSKL());
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
  	
  	HashMap inab = new HashMap();
  	HashMap iraz = new HashMap();
  	for (di.first(); di.inBounds(); di.next()) {
  		if ((di.getString("VRDOK").equals("IZD") || di.getString("VRDOK").equals("OTP"))
  				&& di.getString("VEZA").length() > 0) {
  			inab.put(di.getString("VEZA"), di.getBigDecimal("INAB"));
  			iraz.put(di.getString("VEZA"), di.getBigDecimal("IRAZ"));
  		}
  	}
  	for (di.first(); di.inBounds(); di.next()) {
  		if ((di.getString("VRDOK").equals("IZD") || di.getString("VRDOK").equals("OTP"))
  				&& di.getString("VEZA").length() > 0) continue;
  		
  		res.insertRow(false);
  		res.setString("CORG", rpcskl.getCSKL());
  		res.setString("DOK", di.getString("VRDOK")+"-"+di.getInt("BRDOK"));
  		res.setTimestamp("DATDOK", di.getTimestamp("DATDOK"));
  		Aus.clear(res, "KOLUL");
  		Aus.set(res, "KOLIZ", di, "KOL");
  		Aus.sub(res, "KOL", "KOLUL", "KOLIZ");
  		BigDecimal sn = (BigDecimal) inab.get(di.getString("ID_STAVKA"));
  		if (sn != null) {
  			res.setBigDecimal("NABIZ", sn);
  			Aus.div(res, "NC", "NABIZ", "KOL");
  		} else {
  			Aus.set(res, "NC", di);
  			Aus.set(res, "NABIZ", di, "INAB");
  		}
  		Aus.clear(res, "NABUL");
  		BigDecimal sm = (BigDecimal) iraz.get(di.getString("ID_STAVKA"));
  		if (sm != null) {
  			res.setBigDecimal("IRAZ", sm);
  			Aus.div(res, "MC", "IRAZ", "KOL");
  		} else {
  			Aus.set(res, "MC", di);
  			Aus.set(res, "IRAZ", di);
  		}
  		Aus.clear(res, "IZAD");
  		Aus.sub(res, "VRI", "IZAD", "IRAZ");
  		Aus.set(res, "POP", di, "UIRAB");
  		Aus.set(res, "NETO", di, "IPRODSP");
  	}
  	res.setSort(new SortDescriptor(new String[] {"DATDOK"}));
  	
  	ret = new frmTableDataView();
    ret.setDataSet(res);
    ret.setSums(new String[] {"KOLUL", "KOLIZ", "KOL", "NABUL", "NABIZ", "IZAD", "IRAZ", "VRI", "POP", "NETO"});
    ret.setSaveName("Pregled-kartica-pos");
    ret.jp.addTableModifier(new raTableRunningSum("KOL"));
    ret.jp.addTableModifier(new raTableRunningSum("VRI"));
    ret.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    ret.setTitle("Prikaz kartice artikla " + rpcart.getCART1() + " " + rpcart.getNAZART() + "  od " + 
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
