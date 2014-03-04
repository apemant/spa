package hr.restart.pos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raInputDialog;
import hr.restart.swing.raNumberMask;
import hr.restart.swing.raOptionDialog;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;


public class frmSumaPos {
  
	static String oldpj = "1";
  public static void show() {
    JPanel pan = new JPanel(new XYLayout(400, 75));
    JraTextField num = new JraTextField();
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
    num.setHorizontalAlignment(JLabel.TRAILING);
    new raTextMask(num, 5, false, raTextMask.DIGITS);
    pan.add(new JLabel("Zbrojiti zadnjih raèuna"), new XYConstraints(15,15,-1,-1));
    pan.add(num, new XYConstraints(300, 15, 85, -1));
    pan.add(new JLabel("Prodajno mjesto"), new XYConstraints(15,40,-1,-1));
    pan.add(pj, new XYConstraints(175, 40, 210, -1));
    pj.setDataValue(oldpj);
    
    raInputDialog od = new raInputDialog();
    if (!od.show(null, pan, "Zbroj raèuna")) return;
    
    int racs = Aus.getNumber(num.getText());
    if (racs <= 0 || racs >= 100) return;
    int tracs = racs;
    
    DataSet ds = Aus.q("SELECT pos.cskl,pos.brdok,pos.sysdat," +
    	  "rate.cnacpl as cnacpl, rate.cbanka, rate.irata " +
    	  "FROM pos,rate WHERE pos.cskl = rate.cskl " +
    	  "AND pos.vrdok = rate.vrdok AND pos.god = rate.god " +
    	  "AND pos.brdok = rate.brdok AND pos.cprodmj = pos.cprodmj " +
    	  "and pos.cskl like '" + oldpj + "%' " +
    	  "and " + Condition.from("SYSDAT", new Timestamp(
    	      System.currentTimeMillis() - 1000*3600*6)).qualified("pos"));
    
    ds.setSort(new SortDescriptor(new String[] {"SYSDAT"}));
    
    Map plac = new HashMap();
    Map skl = new HashMap();
    
    int brdok = -1;
    String cskl = "";
    BigDecimal total = Aus.zero2;
    
    for (ds.last(); ds.inBounds(); ds.prior()) {
      if (brdok != ds.getInt("BRDOK") || 
          !ds.getString("CSKL").equals(cskl)) {
        if (--racs < 0) break;
        brdok = ds.getInt("BRDOK");
        cskl = ds.getString("CSKL");
      }
      String nacpl = ds.getString("CNACPL");
      if (ds.getString("CBANKA").length() > 0)
        nacpl = nacpl + "|" + ds.getString("CBANKA");
      BigDecimal rata = ds.getBigDecimal("IRATA");
      total = total.add(rata);
      
      BigDecimal olds = (BigDecimal) skl.get(cskl);
      if (olds == null) olds = rata;
      else olds = olds.add(rata);
      skl.put(cskl, olds);
      
      BigDecimal oldn = (BigDecimal) plac.get(nacpl);
      if (oldn == null) oldn = rata;
      else oldn = oldn.add(rata);
      plac.put(nacpl, oldn);
    }
    
    StorageDataSet data = new StorageDataSet();
    for (Iterator i = skl.keySet().iterator(); i.hasNext(); ) {
      cskl = (String) i.next();
      data.addColumn(dM.createBigDecimalColumn("CSKL_"+cskl, 2));
    }
    for (Iterator i = plac.keySet().iterator(); i.hasNext(); ) {
      String nacpl = (String) i.next();
      data.addColumn(dM.createBigDecimalColumn("NACPL_"+nacpl, 2));
    }
    data.addColumn(dM.createBigDecimalColumn("UKUPNO", 2));
    data.open();
    data.insertRow(false);
    
    int height = skl.size() * 25 + plac.size() * 25 + 100;
    lookupData ld = lookupData.getlookupData();
    dM dm = dM.getDataModule();
    
    JPanel result = new JPanel(new XYLayout(500, height));
    int y = 20;
    for (Iterator i = skl.keySet().iterator(); i.hasNext(); ) {
      cskl = (String) i.next();
      String nazskl = cskl;
      if (ld.raLocate(dm.getSklad(), "CSKL", cskl)) {
        nazskl = nazskl + " " + dm.getSklad().getString("NAZSKL");
      }
      result.add(new JLabel(nazskl), new XYConstraints(15, y, -1, -1));
      JraTextField tf = new JraTextField();
      tf.setDataSet(data);
      tf.setColumnName("CSKL_"+cskl);
      tf.setProtected(true);
      data.setBigDecimal("CSKL_"+cskl, (BigDecimal) skl.get(cskl));
      result.add(tf, new XYConstraints(385, y, 100, -1));
      
      y += 25;
    }
    y += 10;
    for (Iterator i = plac.keySet().iterator(); i.hasNext(); ) {
      String nac = (String) i.next();
      String nacpl = nac;
      String bank = "";
      if (nacpl.indexOf('|') >= 0) {
        bank = nacpl.substring(nacpl.indexOf('|') + 1);
        nacpl = nacpl.substring(0, nacpl.indexOf('|'));
      }
      String naznac = nacpl;
      if (ld.raLocate(dm.getNacpl(), "CNACPL", nacpl)) {
        naznac = naznac + " " + dm.getNacpl().getString("NAZNACPL");
      }
      if (bank.length() > 0 && ld.raLocate(
          dm.getKartice(), "CBANKA", bank)) {
        naznac =  naznac + " - " + dm.getKartice().getString("NAZIV"); 
      }
      result.add(new JLabel(naznac), new XYConstraints(15, y, -1, -1));
      JraTextField tf = new JraTextField();
      tf.setDataSet(data);
      tf.setColumnName("NACPL_"+nac);
      tf.setProtected(true);
      data.setBigDecimal("NACPL_"+nac, (BigDecimal) plac.get(nac));
      result.add(tf, new XYConstraints(385, y, 100, -1));
      
      y += 25;
    }
    y += 10;
    result.add(new JLabel("UKUPNO"), new XYConstraints(15, y, -1, -1));
    final JraTextField uk = new JraTextField();
    uk.setDataSet(data);
    uk.setColumnName("UKUPNO");
    uk.setProtected(true);
    data.setBigDecimal("UKUPNO", total);
    result.add(uk, new XYConstraints(385, y, 100, -1));
    
    raInputDialog out = new raInputDialog() {
      protected void beforeShow() {
        uk.requestFocusLater();
      }
    };
    if (y > 500) {
      JPanel psc = new JPanel(new BorderLayout());
      JraScrollPane sc = new JraScrollPane(result);
      sc.setPreferredSize(new Dimension(525, 400));
      psc.add(sc);
      result.scrollRectToVisible(new Rectangle(0, y - 1, 1, 1));
      result = psc;
      
    }
    out.show(null, result, "Zbroj zadnjih " + tracs + " raèuna");
  }
  
}
