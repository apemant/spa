package hr.restart.pos;

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
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCpar;
import hr.restart.swing.raDateRange;
import hr.restart.swing.raExtendedTable;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raUpitLite;

public class upOdjava extends raUpitLite {
	
	hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  dM dm = hr.restart.baza.dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  
  TableDataSet tds = new TableDataSet();
  
  JPanel mainPanel = new JPanel();
  XYLayout mainXYLayout = new XYLayout();
  
  raComboBox pj = new raComboBox();
  
  jpCpar jpc = new jpCpar();
  
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  JLabel jlDatum = new JLabel();
  
  frmTableDataView ret;
  
  public upOdjava() {
  	try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    mainXYLayout.setWidth(640);
    mainXYLayout.setHeight(125);
    this.setJPan(mainPanel);
    mainPanel.setLayout(mainXYLayout);
    
    tds.setColumns(new Column[] {
    		dm.createIntColumn("CPAR", "Dobavljaè"),
        dm.createTimestampColumn("pocDatum", "Poèetni datum"),
        dm.createTimestampColumn("zavDatum", "Krajnji datum"),
        });
    tds.open();
    
    pj.setRaItems(new String[][] {
    		{"Robna kuæa \"Vesna\"", "1"},
    		{"Robna kuæa \"Tena\"", "2"},
    		{"Robna kuæa \"Pierre\"", "3"},
    });
    
    jpc.bind(tds);
    
    jlDatum.setText("Datum (od-do)");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);

    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(tds);
    
    new raDateRange(jtfPocDatum, jtfZavDatum);
  
    mainPanel.add(new JLabel("Prodajno mjesto"), new XYConstraints(15, 25, -1, -1));
    mainPanel.add(pj, new XYConstraints(150, 25, 210, -1));
    mainPanel.add(jpc, new XYConstraints(0, 50, -1, -1));
    mainPanel.add(jlDatum,   new XYConstraints(15, 75, -1, -1));
    mainPanel.add(jtfPocDatum, new XYConstraints(150, 75, 100, -1));
    mainPanel.add(jtfZavDatum, new XYConstraints(255, 75, 100, -1));
  }

  public void componentShow() {
    tds.open();
    tds.setTimestamp("pocDatum", ut.getFirstDayOfMonth(vl.getToday()));
    tds.setTimestamp("zavDatum", vl.getToday());
    jpc.clear();
    jpc.focusCpar();
  }

	public void firstESC() {

	}
	
	public boolean Validacija() {
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
		
		boolean pop = frmParam.getParam("pos", "popKom" + jpc.getCpar(), "N", 
					"Oduzeti popust sa POS-a za komisiju " + jpc.getCpar() + " (D,N)").equalsIgnoreCase("D");
		
		String dod = pop ? ", stdoki.veza " : "";
		
		String cskl = pj.getDataValue() + "05" + pj.getDataValue();
		String q = "SELECT stdoki.cart1, stdoki.kol, stdoki.nc, stdoki.inab" + dod + " from doki,stdoki "+
		"WHERE " + Util.getUtil().getDoc("doki", "stdoki") + " and stdoki.vrdok='IZD' and  " +
		Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(Condition.equal("CSKL", cskl)).qualified("doki");
		System.out.println(q);
		DataSet ds = Aus.q(q);
		
		DataSet dsp = null;
		if (pop) {
			String qp = "SELECT stdoki.uprab,stdoki.id_stavka from doki,stdoki "+
				"WHERE " + Util.getUtil().getDoc("doki", "stdoki") + " and stdoki.vrdok='POS' and  " +
				Condition.between("DATDOK", tds, "pocDatum", "zavDatum").and(Condition.equal("CSKL", cskl)).qualified("doki");
			System.out.println(qp);
			dsp = Aus.q(qp);
		}
		
		ds.setSort(new SortDescriptor(new String[] {"CART1"}));
    
    StorageDataSet res = new StorageDataSet();
    res.setColumns(new Column[] {
    		dM.createIntColumn("CPAR", "Dobavljaè"),
    		dM.createStringColumn("CART1", "Šifra", 20),
        dM.createStringColumn("NAZART", "Naziv artikla", 100),
        dM.createStringColumn("JM", "Jm", 3),
        dM.createBigDecimalColumn("KOL", "Odjava kol.", 3),
        dM.createBigDecimalColumn("NC", "Cijena", 2),
        dM.createBigDecimalColumn("INAB", "Iznos", 2)
    });
    res.open();
    String cart = "";
    for (ds.first(); ds.inBounds(); ds.next()) {
    	if (!ds.getString("CART1").equals(cart)) {
  			ld.raLocate(dm.getArtikli(), "CART1", cart = ds.getString("CART1"));
  			if (!jpc.isEmpty() && dm.getArtikli().getInt("CPAR") != jpc.getCpar()) continue;
  			
  			res.insertRow(false);
  			res.setInt("CPAR", dm.getArtikli().getInt("CPAR"));
  			res.setString("CART1", cart);
  			res.setString("NAZART", dm.getArtikli().getString("NAZART"));
  			res.setString("JM", dm.getArtikli().getString("JM"));
    	} else if (!res.getString("CART1").equals(cart)) continue;
    	if (pop && ld.raLocate(dsp, "ID_STAVKA", ds.getString("VEZA")))
    		Aus.mul(ds, "INAB", Aus.one0.subtract(dsp.getBigDecimal("UPRAB").movePointLeft(2)));
    	Aus.add(res, "KOL", ds);
    	Aus.add(res, "INAB", ds);
    	if (res.getBigDecimal("KOL").signum() != 0)
    	  Aus.div(res, "NC", "INAB", "KOL");
    }
    
    res.setSort(new SortDescriptor(new String[] {"CPAR", "CART1"}));
  	
  	ret = new frmTableDataView();
    ret.setDataSet(res);
    ret.setSums(new String[] {"INAB"});
    ret.setSaveName("Pregled-odjava-pos");
    ret.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    ret.setTitle("Odjava komisione prodaje  od " + 
        Aus.formatTimestamp(tds.getTimestamp("pocDatum")) + " do " +
        Aus.formatTimestamp(tds.getTimestamp("zavDatum")));
    ret.setVisibleCols(new int[] {1,2,3,4,5,6});
    raExtendedTable t = (raExtendedTable) ret.jp.getMpTable();
    t.setForcePage(true);
  	t.addToGroup("CPAR", true, new String[] {"#", "NAZPAR", "#\n", "ADR", "#,", "PBR", "MJ", "#, OIB", "OIB"}, 
    		dm.getPartneri(), true);
    
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
		return false;
	}

}

