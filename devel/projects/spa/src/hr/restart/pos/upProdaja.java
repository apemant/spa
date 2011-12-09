package hr.restart.pos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
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
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raExtendedTable;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raUpitLite;
import hr.restart.zapod.OrgStr;

public class upProdaja extends raUpitLite {

  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  dM dm = hr.restart.baza.dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  
  JPanel mainPanel = new JPanel();
  XYLayout mainXYLayout = new XYLayout();
  JLabel jlSkladiste = new JLabel();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  JLabel jlDatum = new JLabel();
  
  JraButton jbCSKL = new JraButton();
  JlrNavField jrfCSKL = new JlrNavField();
  JlrNavField jrfNAZSKL = new JlrNavField();
  
  TableDataSet tds = new TableDataSet();
  
  raComboBox izv = new raComboBox();
  JLabel jlIzv = new JLabel();
  
  frmTableDataView ret;
  
  public upProdaja() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    mainXYLayout.setWidth(590);
    mainXYLayout.setHeight(120);
    this.setJPan(mainPanel);
    mainPanel.setLayout(mainXYLayout);
    
    tds.setColumns(new Column[] {dm.createStringColumn("CORG","Prodajno mjesto",12),
        dm.createTimestampColumn("pocDatum", "Poèetni datum"),
        dm.createTimestampColumn("zavDatum", "Krajnji datum"),
        });
    tds.open();
    
    izv.setRaItems(new String[][] {
    		{"Prodaja po naèinu plaæanja", "N"},
    		{"Prodaja po artiklima", "A"},
    		{"Prodaja po artiklima i popustima", "A2"},
    		{"Prodaja po raèunima", "R"},
    		{"Ukupna prodaja po karticama", "U"},
    		{"Ukupna prodaja po naèinu plaæanja", "U2"}
    });
    
    jlSkladiste.setText("Prodajno mjesto");
    jlIzv.setText("Vrsta izvještaja");
    jlDatum.setText("Datum (od-do)");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);

    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(tds);
    
    jrfCSKL.setColumnName("CORG");
    jrfCSKL.setColNames(new String[] {"NAZIV"});
    jrfCSKL.setVisCols(new int[]{2,3});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setRaDataSet(OrgStr.getOrgStr().getOrgstrFromCurrKnjig());
    jrfCSKL.setDataSet(tds);
    jrfCSKL.setSearchMode(0);
    jrfCSKL.setNavButton(jbCSKL);
    jrfCSKL.setNavProperties(null);
    
    jrfNAZSKL.setColumnName("NAZIV");
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setSearchMode(1);
    
    mainPanel.add(jlSkladiste, new XYConstraints(15, 20, -1, -1));
    mainPanel.add(jrfCSKL, new XYConstraints(150, 20, 100, -1));
    mainPanel.add(jrfNAZSKL,  new XYConstraints(255, 20, 295, -1));
    mainPanel.add(jbCSKL,   new XYConstraints(555, 20, 21, 21));
    
    mainPanel.add(jlIzv,   new XYConstraints(15, 45, -1, -1));
    mainPanel.add(izv,   new XYConstraints(150, 45, 300, -1));
    
    mainPanel.add(jlDatum,   new XYConstraints(15, 75, -1, -1));
    mainPanel.add(jtfPocDatum, new XYConstraints(150, 75, 100, -1));
    mainPanel.add(jtfZavDatum, new XYConstraints(255, 75, 100, -1));
    
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jrfCSKL.setRaDataSet(OrgStr.getOrgStr().getOrgstrFromCurrKnjig());
      }
    });
  }
  
	
	public void componentShow() {
		tds.open();
        tds.setTimestamp("pocDatum", vl.getToday());
        tds.setTimestamp("zavDatum", vl.getToday());
       jrfCSKL.requestFocus();
	}

	public void firstESC() {
		
	}

	public void okPress() {
		if (izv.getSelectedIndex() == 0) {
			DataSet corgs = OrgStr.getOrgStr().getOrgstrAndKnjig(tds.getString("CORG"));
			
			String q = "SELECT rate.cskl, rate.cnacpl, rate.cbanka, rate.irata from pos,rate "+
	    						"WHERE " + Util.getUtil().getDoc("pos", "rate") + " and " +
	    						Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
	    						Condition.in("CSKL", corgs, "CORG")).qualified("pos");
			
	    DataSet ds = Aus.q(q.toString());
	    ds.setSort(new SortDescriptor(new String[] {"CSKL", "CNACPL", "CBANKA"}));
	    
	    StorageDataSet res = new StorageDataSet();
	    res.setColumns(new Column[] {
	    		dM.createStringColumn("CORG", "Dobavljaè", 12),
	        dM.createStringColumn("NACPL", "Naèin plaæanja", 50),
	        dM.createStringColumn("BANKA", "Kartièar", 50),
	        dM.createBigDecimalColumn("IRATA", "Iznos naplate")
	    });
	    res.open();
	    
	    String cnacpl = "", cbanka = "", cskl = "";
	    for (ds.first(); ds.inBounds(); ds.next()) {
	      if (!ds.getString("CSKL").equals(cskl) ||
	      		!ds.getString("CNACPL").equals(cnacpl) ||
	          !ds.getString("CBANKA").equals(cbanka)) {
	      	cskl = ds.getString("CSKL");
	        cnacpl = ds.getString("CNACPL");
	        cbanka = ds.getString("CBANKA");
	        res.insertRow(false);
	        ld.raLocate(dm.getNacpl(), "CNACPL", cnacpl);
	        res.setString("NACPL", cnacpl + " - " + dm.getNacpl().getString("NAZNACPL"));
	        res.setString("CORG", cskl);
	        if (cbanka.length() > 0) {
	          ld.raLocate(dm.getKartice(), "CBANKA", cbanka);
	          res.setString("BANKA", cbanka + " - " + dm.getKartice().getString("NAZIV"));
	        }
	      }
	      Aus.add(res, "IRATA", ds);
	    }
	    
	    ret = new frmTableDataView();
	    ret.setDataSet(res);
	    ret.setSums(new String[] {"IRATA"});
	    ret.setSaveName("Pregled-blag-plac");
	    ret.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	    ret.setTitle("Prikaz prometa po vrsti naplate  od " + 
	              Aus.formatTimestamp(tds.getTimestamp("pocDatum")) + " do " +
	              Aus.formatTimestamp(tds.getTimestamp("zavDatum")));
	    ret.setVisibleCols(new int[] {1, 2, 3});
	    raExtendedTable t = (raExtendedTable) ret.jp.getMpTable();
	    t.setForcePage(true);
	    t.addToGroup("CORG", true, new String[] {"#", "NAZIVLOG", "#\n", "ADRESA", "#,", "PBR", "MJESTO", "#, OIB", "OIB"}, 
	    		dM.getDataModule().getLogotipovi(), true);
		} else if (izv.getSelectedIndex() == 1) {
			DataSet corgs = OrgStr.getOrgStr().getOrgstrAndKnjig(tds.getString("CORG"));
			
			String q = "SELECT stpos.cskl, stpos.cart1, stpos.nazart, stpos.kol, stpos.mc, stpos.neto from pos,stpos "+
	    						"WHERE " + Util.getUtil().getDoc("pos", "stpos") + " and " +
	    						Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
	    						Condition.in("CSKL", corgs, "CORG")).qualified("pos");
			
	    DataSet ds = Aus.q(q.toString());
	    ds.setSort(new SortDescriptor(new String[] {"CSKL", "CART1"}));
	    
	    StorageDataSet res = new StorageDataSet();
	    res.setColumns(new Column[] {
	    		dM.createStringColumn("CORG", "Dobavljaè", 12),
	    		dM.createStringColumn("CART1", "Šifra", 20),
	        dM.createStringColumn("NAZART", "Naziv artikla", 100),
	        dM.createBigDecimalColumn("KOL", "Kolièina", 3),
	        dM.createBigDecimalColumn("MC", "Cijena", 2),
	        dM.createBigDecimalColumn("NETO", "Neto", 2)
	    });
	    res.open();
	    String cart = "", cskl="";
	    for (ds.first(); ds.inBounds(); ds.next()) {
	      if (!ds.getString("CSKL").equals(cskl) ||
	      		!ds.getString("CART1").equals(cart)) {
	      	cskl = ds.getString("CSKL");
	      	cart = ds.getString("CART1");
	        res.insertRow(false);
	        res.setString("CORG", cskl);
	        res.setString("CART1", cart);
	        res.setString("NAZART", ds.getString("NAZART"));
	        Aus.set(res, "MC", ds);
	      }
	      Aus.add(res, "KOL", ds);
	      Aus.add(res, "NETO", ds);
	    }
	    
	    ret = new frmTableDataView();
	    ret.setDataSet(res);
	    ret.setSums(new String[] {"NETO"});
	    ret.setSaveName("Pregled-blag-art");
	    ret.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	    ret.setTitle("Prikaz prometa po artiklima  od " + 
	              Aus.formatTimestamp(tds.getTimestamp("pocDatum")) + " do " +
	              Aus.formatTimestamp(tds.getTimestamp("zavDatum")));
	    ret.setVisibleCols(new int[] {1, 2, 3, 4, 5});
	    raExtendedTable t = (raExtendedTable) ret.jp.getMpTable();
	    t.setForcePage(true);
	    t.addToGroup("CORG", true, new String[] {"#", "NAZIVLOG", "#\n", "ADRESA", "#,", "PBR", "MJESTO", "#, OIB", "OIB"}, 
	    		dM.getDataModule().getLogotipovi(), true);
	    
		} else if (izv.getSelectedIndex() == 2) {
          DataSet corgs = OrgStr.getOrgStr().getOrgstrAndKnjig(tds.getString("CORG"));
          
          String q = "SELECT stpos.cskl, stpos.cart1, stpos.nazart, stpos.kol, stpos.mc, stpos.ppopust1, stpos.ukupno, stpos.neto from pos,stpos "+
                              "WHERE " + Util.getUtil().getDoc("pos", "stpos") + " and " +
                              Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
                              Condition.in("CSKL", corgs, "CORG")).qualified("pos");
          
      DataSet ds = Aus.q(q.toString());
      ds.setSort(new SortDescriptor(new String[] {"CSKL", "PPOPUST1", "CART1"}));
      
      StorageDataSet res = new StorageDataSet();
      res.setColumns(new Column[] {
          dM.createStringColumn("CORG", "Dobavljaè", 12),
          dM.createBigDecimalColumn("POP", "Popust", 2),
          dM.createStringColumn("CART1", "Šifra", 20),
          dM.createStringColumn("NAZART", "Naziv artikla", 100),
          dM.createBigDecimalColumn("KOL", "Kolièina", 3),
          dM.createBigDecimalColumn("MC", "Cijena", 2),
          dM.createBigDecimalColumn("UKUPNO", "Iznos", 2),
          dM.createBigDecimalColumn("POPUST", "Popust", 2),
          dM.createBigDecimalColumn("NETO", "Neto", 2)
      });
      res.open();
      String cart = "", cskl="";
      BigDecimal pop = Aus.one0.negate();
      for (ds.first(); ds.inBounds(); ds.next()) {
        if (!ds.getString("CSKL").equals(cskl) ||
            ds.getBigDecimal("PPOPUST1").compareTo(pop) != 0 ||
              !ds.getString("CART1").equals(cart)) {
          cskl = ds.getString("CSKL");
          cart = ds.getString("CART1");
          pop = ds.getBigDecimal("PPOPUST1");
          res.insertRow(false);
          res.setString("CORG", cskl);
          res.setString("CART1", cart);
          res.setString("NAZART", ds.getString("NAZART"));
          Aus.set(res, "POP", ds, "PPOPUST1");
          Aus.set(res, "MC", ds);
        }
        Aus.add(res, "KOL", ds);
        Aus.add(res, "UKUPNO", ds);
        Aus.add(res, "NETO", ds);
        Aus.sub(res, "POPUST", "UKUPNO", "NETO");
      }
      
      ret = new frmTableDataView();
      ret.setDataSet(res);
      ret.setSums(new String[] {"POPUST", "UKUPNO", "NETO"});
      ret.setSaveName("Pregled-blag-art-pop");
      ret.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
      ret.setTitle("Prikaz prometa po artiklima i popustima  od " + 
                Aus.formatTimestamp(tds.getTimestamp("pocDatum")) + " do " +
                Aus.formatTimestamp(tds.getTimestamp("zavDatum")));
      ret.setVisibleCols(new int[] {2, 3, 4, 5, 6, 7, 8});
      raExtendedTable t = (raExtendedTable) ret.jp.getMpTable();
      t.setForcePage(true);
      t.addToGroup("CORG", true, new String[] {"#", "NAZIVLOG", "#\n", "ADRESA", "#,", "PBR", "MJESTO", "#, OIB", "OIB", "#\n"}, 
              dM.getDataModule().getLogotipovi(), true);
      t.addToGroup("POP", true, new String[] {"#%"}, "", true);
	    
		} else if (izv.getSelectedIndex() == 3) {
		  boolean single = tds.getString("CORG").length() == 4;
          DataSet corgs = OrgStr.getOrgStr().getOrgstrAndKnjig(tds.getString("CORG"));
          
          String q = "SELECT pos.cskl,pos.brdok,pos.datdok,pos.ukupno,pos.neto,rate.cnacpl, " +
          		  "rate.cbanka, rate.irata from pos,rate WHERE " + Util.getUtil().getDoc("pos", "rate") + " and " +
                              Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
                              Condition.in("CSKL", corgs, "CORG")).qualified("pos");
          
      DataSet ds = Aus.q(q.toString());
      ds.setSort(new SortDescriptor(new String[] {"CSKL", "BRDOK"}));
      HashMap allnac = new HashMap();
      dm.getNacpl().open();
      for (dm.getNacpl().first(); dm.getNacpl().inBounds(); dm.getNacpl().next())
        if (!dm.getNacpl().getString("AKTIV").equals("N"))
          allnac.put(dm.getNacpl().getString("CNACPL"),
            dm.getNacpl().getString("NAZNACPL"));
      ArrayList ord = new ArrayList(allnac.keySet());
      Collections.sort(ord);
      if (ord.remove("G")) ord.add(0, "G");
      String sumcs = "UKUPNO IRAB NETO";
      int[] viscs = new int[5 + ord.size()];
      for (int i = 0; i < viscs.length; i++)
        viscs[i] = i + 1;
      
      StorageDataSet totalSet = new StorageDataSet();
      totalSet.setColumns(new Column[] {
          dM.createStringColumn("NACPL", "Naèin plaæanja", 50),
          dM.createBigDecimalColumn("IRATA", "Iznos naplate")
      });
      totalSet.getColumn("NACPL").setWidth(25);
      totalSet.getColumn("IRATA").setWidth(10);
      totalSet.open();
      
      StorageDataSet res = new StorageDataSet();
      res.setColumns(new Column[] {
              dM.createStringColumn("CORG", "Dobavljaè", 12),
              dM.createIntColumn("BRDOK", "Broj"),
          dM.createTimestampColumn("DATDOK", "Datum"),
          dM.createBigDecimalColumn("UKUPNO", "Iznos", 2),
          dM.createBigDecimalColumn("IRAB", "Popust", 2),
          dM.createBigDecimalColumn("NETO", "Neto", 2)
      });
      for (int i = 0; i < ord.size(); i++) {
        res.addColumn(dM.createBigDecimalColumn(
            (String) ord.get(i),
            (String) allnac.get(ord.get(i))));
        sumcs = sumcs + " " + (String) ord.get(i);
      }
      res.open();
      int brdok = -1;
      String cnacpl = "", cbanka = "";
      for (ds.first(); ds.inBounds(); ds.next()) {
        if (ds.getInt("BRDOK") != brdok) {
          brdok = ds.getInt("BRDOK");
          res.insertRow(false);
          res.setString("CORG", ds.getString("CSKL"));
          res.setInt("BRDOK", ds.getInt("BRDOK"));
          res.setTimestamp("DATDOK", ds.getTimestamp("DATDOK"));
          Aus.set(res, "UKUPNO", ds);
          Aus.set(res, "NETO", ds);
          Aus.sub(res, "IRAB", "UKUPNO", "NETO");
        }
        /*if (ds.getString("CBANKA").length() > 0)
          Aus.add(res, ds.getString("CBANKA"), ds, "IRATA");
        else */Aus.add(res, ds.getString("CNACPL"), ds, "IRATA");
        
        if (single) {
          if (!ds.getString("CNACPL").equals(cnacpl) ||
              !ds.getString("CBANKA").equals(cbanka)) {
            cnacpl = ds.getString("CNACPL");
            cbanka = ds.getString("CBANKA");
            
            ld.raLocate(dm.getNacpl(), "CNACPL", cnacpl);
            String nac = cnacpl + " - " + dm.getNacpl().getString("NAZNACPL");
            if (cbanka.length() > 0) nac = nac + " - " + cbanka;
            if (!ld.raLocate(totalSet, "NACPL", nac)) {
              totalSet.insertRow(false);
              totalSet.setString("NACPL", nac);
              Aus.clear(totalSet, "IRATA");
            }
          }
          Aus.add(totalSet, "IRATA", ds);
        }
      }
      
      ret = new frmTableDataView();
      ret.setDataSet(res);
      ret.setSums(new VarStr(sumcs).split());
      ret.setSaveName("Pregled-blag-rac");
      ret.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
      ret.setTitle("Prikaz prometa po raèunima  od " + 
                Aus.formatTimestamp(tds.getTimestamp("pocDatum")) + " do " +
                Aus.formatTimestamp(tds.getTimestamp("zavDatum")));
      ret.setVisibleCols(viscs);
      raExtendedTable t = (raExtendedTable) ret.jp.getMpTable();
      t.setForcePage(true);
      t.addToGroup("CORG", true, new String[] {"#", "NAZIVLOG", "#\n", "ADRESA", "#,", "PBR", "MJESTO", "#, OIB", "OIB"}, 
              dM.getDataModule().getLogotipovi(), true);
      
      if (single) {
        totalSet.setSort(new SortDescriptor(new String[] {"NACPL"}));
        raExtendedTable totalTab = new raExtendedTable();
        totalTab.setDataSet(totalSet);
        totalTab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        totalTab.setCellSelectionEnabled(false);
        totalTab.clearSelection();
        totalTab.setAlternateColor(false);
        totalTab.setBackground(ret.jp.getBackground());
        
        ret.jp.installSummary(totalTab, 10, false);
      }
      
      } else if (izv.getSelectedIndex() == 4) {
        DataSet corgs = OrgStr.getOrgStr().getOrgstrAndKnjig(tds.getString("CORG"));
        
        String q = "SELECT rate.cskl, rate.cnacpl, rate.cbanka, rate.irata from pos,rate "+
                            "WHERE " + Util.getUtil().getDoc("pos", "rate") + " and " +
                            Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
                            Condition.in("CSKL", corgs, "CORG")).qualified("pos");          
        
        DataSet ds = Aus.q(q);
        ds.setSort(new SortDescriptor(new String[] {"CNACPL", "CBANKA", "CSKL"}));
        
        StorageDataSet res = new StorageDataSet();
        res.setColumns(new Column[] {
            dM.createStringColumn("CORG", "Dobavljaè", 50),           
            dM.createBigDecimalColumn("IZNOS", "Iznos", 2),
            dM.createStringColumn("DUMMY", "", 50),
        });
        res.open();
        
        
        String cnac = "", cban = "", cskl = "";
        for (ds.first(); ds.inBounds(); ds.next()) {
          if (!ds.getString("CNACPL").equals(cnac) ||
            !ds.getString("CBANKA").equals(cban)) {
            cnac = ds.getString("CNACPL");
            cban = ds.getString("CBANKA");
            cskl = "";
            res.insertRow(false);
            res.setAssignedNull("IZNOS");
            res.insertRow(false);
            res.setAssignedNull("IZNOS");
            ld.raLocate(dm.getNacpl(), "CNACPL", cnac);
            String nac = cnac + " " + dm.getNacpl().getString("NAZNACPL");
            if (cban.length() > 0 &&
                ld.raLocate(dm.getKartice(), "CBANKA", cban)) {
              nac = nac + " - " + dm.getKartice().getString("NAZIV");
            }
            res.setString("CORG", nac);
          }
          if (!ds.getString("CSKL").equals(cskl)) {
            cskl = ds.getString("CSKL");
            res.insertRow(false);
            String naz = "";
            if (ld.raLocate(dm.getLogotipovi(), "CORG", cskl))
              naz = dm.getLogotipovi().getString("NAZIVLOG");
            else {
              ld.raLocate(dm.getOrgstruktura(), "CORG", cskl);
              if (ld.raLocate(dm.getLogotipovi(), "CORG", 
                  dm.getOrgstruktura().getString("PRIPADNOST")))
                naz = dm.getLogotipovi().getString("NAZIVLOG");
              else {
                ld.raLocate(dm.getSklad(), "CSKL", cskl);
                naz = dm.getSklad().getString("NAZSKL");
              }
            }
            if (naz.startsWith("Za:")) naz = naz.substring(4);
            res.setString("CORG", cskl + " " + naz);
          }
          Aus.add(res, "IZNOS", ds, "IRATA");
        }
        res.insertRow(false);
        res.setAssignedNull("IZNOS");
        
        ret = new frmTableDataView();
        ret.setDataSet(res);
        ret.setSums(new String[] {"IZNOS"});
        ret.setSaveName("Pregled-ukup-blag");
        ret.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        ret.setTitle("Prikaz ukupnog prometa po naèinima plaæanja  od " + 
                  Aus.formatTimestamp(tds.getTimestamp("pocDatum")) + " do " +
                  Aus.formatTimestamp(tds.getTimestamp("zavDatum")));
        ret.setVisibleCols(new int[] {0, 1, 2});
      } else if (izv.getSelectedIndex() == 5) {
        DataSet corgs = OrgStr.getOrgStr().getOrgstrAndKnjig(tds.getString("CORG"));
        
        String q = "SELECT rate.cskl, rate.cnacpl, rate.irata from pos,rate "+
                            "WHERE " + Util.getUtil().getDoc("pos", "rate") + " and " +
                            Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(
                            Condition.in("CSKL", corgs, "CORG")).qualified("pos");          
        
        DataSet ds = Aus.q(q);
        ds.setSort(new SortDescriptor(new String[] {"CNACPL", "CSKL"}));
        
        StorageDataSet res = new StorageDataSet();
        res.setColumns(new Column[] {
            dM.createStringColumn("CORG", "Dobavljaè", 50),           
            dM.createBigDecimalColumn("IZNOS", "Iznos", 2),
            dM.createStringColumn("DUMMY", "", 50),
        });
        res.open();
        
        
        String cnac = "", cskl = "";
        for (ds.first(); ds.inBounds(); ds.next()) {
          if (!ds.getString("CNACPL").equals(cnac)) {
            cnac = ds.getString("CNACPL");
            cskl = "";
            res.insertRow(false);
            res.setAssignedNull("IZNOS");
            res.insertRow(false);
            res.setAssignedNull("IZNOS");
            ld.raLocate(dm.getNacpl(), "CNACPL", cnac);
            String nac = cnac + " " + dm.getNacpl().getString("NAZNACPL");
            res.setString("CORG", nac);
          }
          if (!ds.getString("CSKL").equals(cskl)) {
            cskl = ds.getString("CSKL");
            res.insertRow(false);
            String naz = "";
            if (ld.raLocate(dm.getLogotipovi(), "CORG", cskl))
              naz = dm.getLogotipovi().getString("NAZIVLOG");
            else {
              ld.raLocate(dm.getOrgstruktura(), "CORG", cskl);
              if (ld.raLocate(dm.getLogotipovi(), "CORG", 
                  dm.getOrgstruktura().getString("PRIPADNOST")))
                naz = dm.getLogotipovi().getString("NAZIVLOG");
              else {
                ld.raLocate(dm.getSklad(), "CSKL", cskl);
                naz = dm.getSklad().getString("NAZSKL");
              }
            }
            if (naz.startsWith("Za:")) naz = naz.substring(4);
            res.setString("CORG", cskl + " " + naz);
          }
          Aus.add(res, "IZNOS", ds, "IRATA");
        }
        res.insertRow(false);
        res.setAssignedNull("IZNOS");
        
        ret = new frmTableDataView();
        ret.setDataSet(res);
        ret.setSums(new String[] {"IZNOS"});
        ret.setSaveName("Pregled-ukup-blag-nokart");
        ret.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        ret.setTitle("Prikaz ukupnog prometa po naèinima plaæanja  od " + 
                  Aus.formatTimestamp(tds.getTimestamp("pocDatum")) + " do " +
                  Aus.formatTimestamp(tds.getTimestamp("zavDatum")));
        ret.setVisibleCols(new int[] {0, 1, 2});
      }
		
	}
	
	protected void upitCompleted() {
		if (ret != null) ret.show();
		ret = null;
	}
	
	public void afterOKPress() {
		rcc.EnabDisabAll(mainPanel, true);
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
	
	public boolean Validacija() {
		if (vl.isEmpty(jrfCSKL)) return false;
		if (!Aus.checkDateRange(jtfPocDatum, jtfZavDatum)) return false;

		return true;
	}

	public boolean runFirstESC() { 
		return false;
	}

}
