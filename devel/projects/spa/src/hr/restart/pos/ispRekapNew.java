package hr.restart.pos;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Condition;
import hr.restart.baza.Sklad;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.raProcess;
import hr.restart.util.raUpitLite;
import hr.restart.zapod.OrgStr;


public class ispRekapNew extends raUpitLite {

  static ispRekapNew inst;
  
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  dM dm = hr.restart.baza.dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  
  QueryDataSet finalSet = new QueryDataSet();
  QueryDataSet porezSet = new QueryDataSet();
  
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
  
  BigDecimal pop;
  int minr, maxr;
  int delay;
  
  public ispRekapNew() {
    try {
      jbInit();
      inst = this;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public static ispRekapNew getInstance() {
    return inst;
  }
  
  public DataSet getPorezSet() {
    return porezSet;
  }
  
  public DataSet getFinalSet() {
    return finalSet;
  }
  
  public BigDecimal getPopust() {
    return pop;
  }
  
  public int getMinRac() {
    return minr;    
  }
  
  public int getMaxRac() {
    return maxr;    
  }
  
  public String getCSKL() {
    return tds.getString("CORG");
  }
  
  public Timestamp getPocDatum() {
    return tds.getTimestamp("pocDatum");
  }
  
  public Timestamp getZavDatum() {
    return tds.getTimestamp("zavDatum");
  }
  
  private void jbInit() throws Exception {
    mainXYLayout.setWidth(590);
    mainXYLayout.setHeight(100);
    this.setJPan(mainPanel);
    mainPanel.setLayout(mainXYLayout);
    
    finalSet.setColumns(new Column[]{dm.createStringColumn("CNACPL", 12),
        dm.createStringColumn("CBANKA",12),
        dm.createStringColumn("NACPL",30),
        dm.createBigDecimalColumn("IZNOS")});
    
    porezSet.setColumns(new Column[] {
        dm.createStringColumn("NAZIV", 30),
        dm.createBigDecimalColumn("PROMET"),
        dm.createBigDecimalColumn("OSNOVICA"),
        dm.createBigDecimalColumn("STOPA"),
        dm.createBigDecimalColumn("IZNOS")
    });
    
    tds.setColumns(new Column[] {dm.createStringColumn("CORG","Prodajno mjesto",12),
        dm.createTimestampColumn("pocDatum", "Poèetni datum"),
        dm.createTimestampColumn("zavDatum", "Krajnji datum"),
        });
    tds.open();
    
    jlSkladiste.setText("Prodajno mjesto");
    jlDatum.setText("Datum (od-do)");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);

    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(tds);
    
    new raDateRange(jtfPocDatum, jtfZavDatum);
    
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
    
    mainPanel.add(jlDatum,   new XYConstraints(15, 45, -1, -1));
    mainPanel.add(jtfPocDatum, new XYConstraints(150, 45, 100, -1));
    mainPanel.add(jtfZavDatum, new XYConstraints(255, 45, 100, -1));
    
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
    
    String sdel = hr.restart.sisfun.frmParam.getParam("pos", "delayRep", "500", 
    		"Koliko milisekundi da prièeka izmeðu 2 štampanja", true);
    delay = Aus.getAnyNumber(sdel);
    if (delay == 0) delay = 250;
  }

  public void firstESC() {
    if (!tds.getString("CORG").equals("")){
    	rcc.EnabDisabAll(mainPanel, true);
      jrfCSKL.setText("");
      jrfCSKL.emptyTextFields();
      jrfCSKL.requestFocus();
    }
  }
  
  public boolean Validacija() {
  	if (vl.isEmpty(jrfCSKL)) return false;
  	
  	if (tds.getString("CORG").length() < 4) {
  		if (JOptionPane.showConfirmDialog(this.getWindow(), "Želite li ispisati sve obraèune za ovu jedinicu?", 
  				"Višestruki ispis", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return false;
  		
  		String cskl = tds.getString("CORG");
  		raProcess.runChild(new Runnable() {
				public void run() {
					multiplePrint();
				}
  		});
  		tds.setString("CORG", cskl);
  		return false;  		
  	}
  	
  	return true;
  }
  
  void multiplePrint() {
		DataSet allc = OrgStr.getOrgStr().getOrgstrFromKnjig(tds.getString("CORG"));
		HashSet hc = new HashSet();
		for (allc.first(); allc.inBounds(); allc.next()) {
			hc.add(allc.getString("CORG"));
		}
		System.out.println(hc);
		
		String q = "SELECT cskl FROM pos WHERE pos.vrdok = 'GRC' and " +
    		Condition.between("DATDOK", tds, "pocDatum", "zavDatum").qualified("pos");
		QueryDataSet ds = Aus.q(q);
		ds.setSort(new SortDescriptor(new String[] {"CSKL"}));
		
		List css = new ArrayList();
		for (ds.first(); ds.inBounds(); ds.next())
			if (hc.remove(ds.getString("CSKL")))
				css.add(ds.getString("CSKL"));

		
		for (int i = 0; i < css.size(); i++) {
			tds.setString("CORG", (String) css.get(i));
			okPress();
			ispis();
			try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        // blah
      }
		}
  }

  public void okPress() {
    this.killAllReports();

    this.addReport("hr.restart.pos.repRekapNew", "Ispis matrièni");
    
    
    pop = Aus.zero2;
    minr = maxr = -1;
    
    porezSet.open();
    porezSet.empty();
    
    String porezString = 
      "SELECT stpos.brdok as brdok, stpos.ukupno as ukupno, stpos.neto as neto, " +
      "stpos.por1 as por1, stpos.por2 as por2 " +
      "FROM pos, Stpos "+
      "WHERE pos.cskl = stpos.cskl "+
      "AND pos.vrdok = stpos.vrdok "+
      "AND pos.god = stpos.god "+
      "AND pos.brdok = stpos.brdok "+
      "AND pos.cprodmj = stpos.cprodmj "+
      "and pos.vrdok = 'GRC' and " +
      Condition.equal("CSKL", tds, "CORG").and(Condition.between(
          "DATDOK", tds, "pocDatum", "zavDatum")).qualified("pos") + 
          
       " UNION ALL " +
       "SELECT stdoki.brdok as brdok, stdoki.iraz as ukupno, " +
       "stdoki.iprodsp as neto, stdoki.por1 as por1, stdoki.por2 as por2 " +
       "FROM doki,stdoki WHERE " + rut.getDoc("doki", "stdoki") +
       " and doki.vrdok = 'ROT' and "+
       Condition.equal("CSKL", tds, "CORG").and(Condition.between(
           "DATDOK", tds, "pocDatum", "zavDatum")).qualified("doki");
       

    System.out.println(porezString);
    QueryDataSet qdsp = Aus.q(porezString);
    qdsp.open();
    for (qdsp.first(); qdsp.inBounds(); qdsp.next()) {
      if (qdsp.getBigDecimal("POR1").signum() != 0) {
        if (!ld.raLocate(porezSet, "NAZIV", "PDV")) {
            porezSet.insertRow(false);
            porezSet.setString("NAZIV", "PDV");
          }
          Aus.add(porezSet, "PROMET", qdsp, "NETO");
          porezSet.setBigDecimal("STOPA", new BigDecimal(25));
          Aus.add(porezSet, "IZNOS", qdsp, "POR1");
          Aus.add(porezSet, "OSNOVICA", qdsp, "NETO");
          Aus.sub(porezSet, "OSNOVICA", qdsp, "POR1");
          Aus.sub(porezSet, "OSNOVICA", qdsp, "POR2");
      }
      if (qdsp.getBigDecimal("POR2").signum() != 0) {
          if (!ld.raLocate(porezSet, "NAZIV", "PNP")) {
            porezSet.insertRow(false);
            porezSet.setString("NAZIV", "PNP");
          }
          Aus.add(porezSet, "PROMET", qdsp, "NETO");
          porezSet.setBigDecimal("STOPA", new BigDecimal(3));
          Aus.add(porezSet, "IZNOS", qdsp, "POR2");
          Aus.add(porezSet, "OSNOVICA", qdsp, "NETO");
          Aus.sub(porezSet, "OSNOVICA", qdsp, "POR1");
          Aus.sub(porezSet, "OSNOVICA", qdsp, "POR2");
      }
      pop = pop.add(qdsp.getBigDecimal("UKUPNO")).subtract(qdsp.getBigDecimal("NETO"));
      if (minr < 0 || qdsp.getInt("BRDOK") < minr)
        minr = qdsp.getInt("BRDOK");
      if (maxr < 0 || qdsp.getInt("BRDOK") > maxr)
        maxr = qdsp.getInt("BRDOK");
    }
    
    finalSet.open();
    finalSet.empty();
    
    String upitString = "SELECT RATE.irata as irata, RATE.cbanka as cbanka, RATE.cnacpl as cnacpl "+
    "FROM pos, rate "+
    "WHERE pos.cskl = rate.cskl "+
    "AND pos.vrdok = rate.vrdok "+
    "AND pos.god = rate.god "+
    "AND pos.brdok = rate.brdok "+
    "AND pos.cprodmj = rate.cprodmj "+
    "and pos.vrdok = 'GRC' and "+ 
    Condition.equal("CSKL", tds, "CORG").and(Condition.between(
        "DATDOK", tds, "pocDatum", "zavDatum")).qualified("pos") + 
        
     " UNION ALL " +
     "SELECT stdoki.iprodsp as irata, '' as cbanka, 'VR' as cnacpl " +
     "FROM doki,stdoki WHERE " + rut.getDoc("doki", "stdoki") +
     " and doki.vrdok = 'ROT' and "+
     Condition.equal("CSKL", tds, "CORG").and(Condition.between(
         "DATDOK", tds, "pocDatum", "zavDatum")).qualified("doki");
         
    System.out.println(upitString);
    QueryDataSet qds = Aus.q(upitString);
    qds.open();
    
    if (qds.isEmpty()) setNoDataAndReturnImmediately();
    
    qds.setSort(new SortDescriptor(new String[] {"CNACPL", "CBANKA"}));
    
    String cnac = "";
    String cban = "";
    for (qds.first(); qds.inBounds(); qds.next()) {
      if (!qds.getString("CNACPL").equals(cnac) ||
          !qds.getString("CBANKA").equals(cban)) {
        cnac = qds.getString("CNACPL");
        if (cban.length() == 0 && qds.getString("CBANKA").length()>0) {
          finalSet.insertRow(false);
          finalSet.setString("CNACPL", cnac);
          finalSet.setString("CBANKA", cban);
          ld.raLocate(dm.getNacpl(), "CNACPL", cnac);
          finalSet.setString("NACPL", dm.getNacpl().getString("NAZNACPL"));
          Aus.clear(finalSet, "IZNOS");
        }
        cban = qds.getString("CBANKA");
        finalSet.insertRow(false);
        finalSet.setString("CNACPL", cnac);
        finalSet.setString("CBANKA", cban);
        if (cban.length() > 0) {
          ld.raLocate(dm.getKartice(), "CBANKA", cban);
          finalSet.setString("NACPL", "- " + dm.getKartice().getString("NAZIV"));
         } else {
          ld.raLocate(dm.getNacpl(), "CNACPL", cnac);
          finalSet.setString("NACPL", dm.getNacpl().getString("NAZNACPL"));
        }
        Aus.clear(finalSet, "IZNOS");
      }
      Aus.add(finalSet, "IZNOS", qds, "IRATA");
    }
  }
  
  public void addHooks() {
  	getRepRunner().setOneTimeDirectReport("hr.restart.pos.repRekapNew");
  }
  
  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }

  public boolean runFirstESC() {
    if (!tds.getString("CORG").equals("")) return true;
    return false;
  }
}
