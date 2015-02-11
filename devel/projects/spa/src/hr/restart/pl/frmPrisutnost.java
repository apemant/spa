/****license*****************************************************************
**   file: frmPrisutnost.java
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
package hr.restart.pl;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Prisutobr;
import hr.restart.baza.Radnicipl;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raTableModifier;
import hr.restart.util.*;


public class frmPrisutnost extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  jpRadnicipl jpMaster;
  jpPrisRad jpDetail;
  raCalcPrimanja rcp = raCalcPrimanja.getRaCalcPrimanja();
  frmTableDataView all = new frmTableDataView(false, false, true);
  ArrayList allOut = new ArrayList();
  ArrayList allSums = new ArrayList();
  ArrayList allRad = new ArrayList();
  int allRbr = 0;

  String radnikFull;
  int maxDana;
  int god, mje;
  String[] key = new String[] {"CRADNIK"};
  String[] allkey = new String[] {"CRADNIK", "DAN", "CVRP", "GRPRIS"};
  
  HashDataSet vrprims;
  HashDataSet grprisut;
  
  raNavAction rnvShowOne  = new raNavAction("Prikaži raspored",raImages.IMGALIGNJUSTIFY,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      showOne();
    }
  };
  
  raNavAction rnvShowAll  = new raNavAction("Prikaži raspored svih radnika",raImages.IMGALIGNJUSTIFY,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      showAll();
    }
  };
   
  raNavAction rnvGrupe  = new raNavAction("Rekapitulacija po grupama",raImages.IMGHISTORY,KeyEvent.VK_F11) {
    public void actionPerformed(ActionEvent e) {
      showGrupe();
    }
  };
  
  raTableModifier weekend = new raTableModifier() {
  	private Variant v = new Variant();
		public boolean doModify() {
			 if (getTable() instanceof JraTable2) {
		      DataSet ds = ((JraTable2)getTable()).getDataSet();
		      if (ds.hasColumn("DTJE") != null) return true;
		    }
			 return false;
		}
		public void modify() {
			if (isSelected()) return;
			
			DataSet ds = ((JraTable2)getTable()).getDataSet();
    	ds.getVariant("DTJE", getRow(), v);
			if (v.getString().equals("Ned")) {
				renderComponent.setBackground(Aus.halfTone(getTable().getBackground(), Color.red, 0.40f));
			} else if (v.getString().equals("Sub")) {
				renderComponent.setBackground(Aus.halfTone(getTable().getBackground(), Color.red, 0.20f));
			}
		}
	};
  
  public frmPrisutnost() {
    super(1, 2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void beforeShowMaster() {
  	String corg = getPreSelect().getSelDataSet().getString("CORG");
  	if (!ld.raLocate(dm.getOrgpl(),"CORG",corg)) maxDana=31;
  	god = dm.getOrgpl().getShort("GODOBR");
    mje = dm.getOrgpl().getShort("MJOBR");
    
    Calendar cal = Calendar.getInstance();
    cal.setTime(Aus.createTimestamp(god, mje, 1));
    maxDana = cal.getActualMaximum(cal.DATE);
    System.out.println(god + "-" + mje + " = " + maxDana);
    
    if (ld.raLocate(dm.getVrstesif(), "VRSTASIF", "PLGP")) {
    	jpDetail.jlCsif.setText(dm.getVrstesif().getString("OPISVRSIF"));
    }
  }

  public void SetFokusMaster(char mode) {
  }
  public boolean ValidacijaMaster(char mode) {
    return false;
  }
  
  public void refilterDetailSet() {
  	super.refilterDetailSet();
  	if (ld.raLocate(dm.getRadnici(), "CRADNIK", getMasterSet().getString("CRADNIK"))) {
  		radnikFull = dm.getRadnici().getString("CRADNIK") + " - " +
  				dm.getRadnici().getString("IME") + " " + dm.getRadnici().getString("PREZIME");
  		setNaslovDetail("Prisutnost radnika " + radnikFull);
  	}
  }
  
  public void SetFokusDetail(char mode) {
  	if (mode == 'N') {
  		rcc.EnabDisabAll(jpDetail, true);
  		raDetail.restorePreviousValue(jpDetail.jlrCvrp);
  		jpDetail.jlrCvrp.forceFocLost();
  		raDetail.restorePreviousValue(jpDetail.jlrCgrup);
        jpDetail.jlrCgrup.forceFocLost();
        raDetail.restorePreviousValue(jpDetail.jraDan);
        getDetailSet().setShort("DAN", (short) (Aus.getNumber(jpDetail.jraDan.getText()) + 1));
        jpDetail.jraDan.requestFocusLater();
  	} else if (mode == 'I') {
  		rcc.EnabDisabAll(jpDetail, false);
  		rcc.setLabelLaF(jpDetail.jraSat, true);
  		rcc.setLabelLaF(jpDetail.jraIznos, true);
  		rcc.setLabelLaF(jpDetail.jraKol, true);
  		jpDetail.jraSat.requestFocus();
  	}
  }
  public boolean ValidacijaDetail(char mode) {
  	if (vl.isEmpty(jpDetail.jlrCgrup) || vl.isEmpty(jpDetail.jlrCvrp))
      return false;
  	
  	if (mode == 'N') {
  		if (Prisutobr.getDataModule().getRowCount(Condition.whereAllEqual(allkey, getDetailSet())) != 0) {
  			jpDetail.jlrCvrp.requestFocus();
  			JOptionPane.showMessageDialog(raDetail.getWindow(), "Zapis veæ postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
  			return false;
  		}
  		if (getDetailSet().getShort("DAN") > maxDana || getDetailSet().getShort("DAN") < 0) {
  			jpDetail.jraDan.requestFocus();
  			JOptionPane.showMessageDialog(raDetail.getWindow(), "Nepostojeæi dan!", "Greška", JOptionPane.ERROR_MESSAGE);
    		return false;
    	}
  	}
  	
    return true;
  }
  
  public int getMaxDate(){
    return maxDana;
  }
  
  void showGrupe() {
    StorageDataSet out = new StorageDataSet();
    out.setColumns(new Column[] {
      dM.createStringColumn("CSIF", jpDetail.jlCsif.getText(), 5),
      Prisutobr.getDataModule().getColumn("CVRP").cloneColumn(),
      Prisutobr.getDataModule().getColumn("SATI").cloneColumn(),
      Prisutobr.getDataModule().getColumn("IZNOS").cloneColumn()
    });
    out.getColumn("CVRP").setCaption("Vrsta primanja");
    out.open();
    String[] okey = {"CSIF", "CVRP"};
    
    HashMap sums = new HashMap();
    List data = loadPrisutnost(Condition.ident);   
    for (Iterator i = data.iterator(); i.hasNext(); ) {
      PrisData pd = (PrisData) i.next();
      String key = pd.grpris+"|"+pd.cvrp;
      PrisData sum = (PrisData) sums.get(key);
      if (sum == null) sums.put(key, pd);
      else {
        sum.sati = sum.sati.add(pd.sati);
        sum.iznos = sum.iznos.add(pd.iznos);
      }
    }

    for (Iterator i = sums.values().iterator(); i.hasNext(); ) {
      PrisData sum = (PrisData) i.next();
      out.insertRow(false);
      out.setString("CSIF", sum.grpris);
      out.setShort("CVRP", (short) sum.cvrp);
      out.setBigDecimal("SATI", sum.sati);
      out.setBigDecimal("IZNOS", sum.iznos);
    }

    frmTableDataView frm = new frmTableDataView();
    frm.setDataSet(out);
    frm.setSaveName("Pregled-prisutobr-grupe");
    frm.setVisibleCols(new int[] {1, 2, 3, 4});
    frm.setSums(new String[] {"SATI", "IZNOS"});
    frm.setTitle("Rekapitulacija plaæe po grupama - " + jpDetail.jlCsif.getText());
    frm.jp.addTableModifier(
       new hr.restart.swing.raTableColumnModifier("CVRP", new String[] {"CVRP", "NAZIV"}, dm.getVrsteprim()));
    raExtendedTable t = (raExtendedTable) frm.jp.getMpTable();
    t.addToGroup("CSIF", true, new String[] {"#", "CSIF", "NAZIV"}, jpDetail.jlrCgrup.getRaDataSet(), true);
    t.createSortDescriptor();
    frm.show();
    frm.resizeLater();
  }
  
  void reCalc() {
    if (getDetailSet().isNull("CVRP")) return;
    
    rcp.addCalcSet(getDetailSet(), "prisutobr");
    rcp.addCalcSet(getMasterSet(), "radnicipl");
    rcp.calcPrisut(getDetailSet());
    
  	/*DataSet ds = getDetailSet();
  	if (ds.rowCount() == 0) return;
  	
    int row = ds.getRow();
    raDetail.getJpTableView().getMpTable().stopFire();
    ds.enableDataSetEvents(false);
    try {
    	
    	QueryDataSet rads = Radnicipl.getDataModule().openTempSet(Condition.equal("CRADNIK", getMasterSet()));    	
    	QueryDataSet prim = Primanjaobr.getDataModule().openTempSet(Condition.equal("CRADNIK", rads));
    	int rbr = 0;
    	
    	for (ds.first(); ds.inBounds(); ds.next()) {
    		if (ld.raLocate(prim, "CVRP", "" + ds.getShort("CVRP"))) {
    			Aus.add(prim, "SATI", ds);
    			Aus.add(prim, "NETO", ds, "IZNOS");
    		} else {
    			prim.insertRow(false);
    			prim.setShort("RBR", (short) ++rbr);
    			prim.setShort("CVRP", ds.getShort("CVRP"));
    			prim.setString("CRADNIK", ds.getString("CRADNIK"));
    			prim.setString("CORG", rads.getString("CORG"));
    			prim.setBigDecimal("KOEF", Aus.one0.movePointRight(2));
    			prim.setBigDecimal("SATI", ds.getBigDecimal("SATI"));
    			prim.setBigDecimal("NETO", ds.getBigDecimal("IZNOS"));
    		}
    	}
    	
    	frmTableDataView frm = new frmTableDataView();
    	frm.setDataSet(prim);
    	frm.setSaveName("Pregled-prisutobr-prim");
    	frm.setVisibleCols(new int[] {1, 4, 8});
    	frm.setSums(new String[] {"SATI", "NETO"});
    	frm.setTitle("Primanja radnika " + radnikFull);
    	frm.jp.addTableModifier(
    	   new hr.restart.swing.raTableColumnModifier("CVRP", new String[] {"CVRP", "NAZIV"}, dm.getVrsteprim()));
    	frm.show();
    	frm.resizeLater();
    	
    } finally {Sati
    	ds.goToRow(row);
    	ds.enableDataSetEvents(true);
    	raDetail.getJpTableView().getMpTable().startFire();
    	raDetail.getJpTableView().fireTableDataChanged();
    }*/
  }
  
boolean findKum(List data, StorageDataSet out, StorageDataSet sums) {
    
    if (data.size() == 0) return false;
    
    Calendar cal = Calendar.getInstance();
    cal.set(cal.YEAR, god);
    cal.set(cal.MONTH, mje - 1);
    String[] dt = {"", "Ned", "Pon", "Uto", "Sri", "Èet", "Pet", "Sub"};
    
    sums.setColumns(new Column[] {
        dM.createStringColumn("PRIM", "Primanje", 50),
        dM.createColumn("SATI", "Sati", null, Variant.BIGDECIMAL, 2, 10, 2),
        dM.createColumn("IZNOS", "Iznos", null, Variant.BIGDECIMAL, 2, 10, 2)
    });
    sums.open();
    HashSum primsati = new HashSum();
    HashSum primiznos = new HashSum();
    
    HashSet grs = new HashSet();
    HashSum[] dani = new HashSum[maxDana];
    for (int i = 0; i < maxDana; i++)
        dani[i] = new HashSum();
    for (Iterator i = data.iterator(); i.hasNext(); ) {
      PrisData pd = (PrisData) i.next();
      grs.add(pd.grpris);
      if (pd.dan > maxDana) {
        System.out.println("Weird bug! dan = " + pd.dan);
        pd.dan = maxDana;
      }
      if (pd.sati != null && pd.sati.signum() > 0)
          dani[pd.dan - 1].add(pd.grpris, pd.sati);
        primsati.add(Integer.toString(pd.cvrp), pd.sati);
        primiznos.add(Integer.toString(pd.cvrp), pd.iznos);
    }
    
    Column[] cols = new Column[grs.size() + 3];
    cols[0] = dM.createIntColumn("DAN", "Dan");
    cols[1] = dM.createStringColumn("DTJE", "Tje", 3);
    cols[2] = dM.createColumn("UK", "Ukupno", null, Variant.BIGDECIMAL, 2, 6, 2);
    ArrayList lgr = new ArrayList(grs);
    Collections.sort(lgr);
    for (int i = 0; i < lgr.size(); i++)
        cols[i+3] = dM.createColumn((String) lgr.get(i), (String) lgr.get(i), null, Variant.BIGDECIMAL, 2, 6, 2);
    
    lgr.add("UK");
    
    out.setColumns(cols);
    out.open();
    
    for (int i = 0; i < maxDana; i++) {
        out.insertRow(false);
        out.setInt("DAN", i + 1);
        cal.set(cal.DATE, i + 1);
        out.setString("DTJE", dt[cal.get(cal.DAY_OF_WEEK)]);
        
        for (Iterator g = dani[i].iterator(); g.hasNext(); ) {
            String gr = (String) g.next();
            out.setBigDecimal(gr, (BigDecimal) dani[i].get(gr));
            Aus.add(out, "UK", gr);
        }
    }
    
    // primanja
    BigDecimal totalPrim = Aus.zero0;
    BigDecimal totalSati = Aus.zero0;
    for (Iterator i = primiznos.iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      if (primsati.getBigDecimal(key).signum() > 0) {
        insertSum(sums, key, primsati.getBigDecimal(key), primiznos.getBigDecimal(key));
        totalPrim = totalPrim.add(primiznos.getBigDecimal(key));
        totalSati = totalSati.add(primsati.getBigDecimal(key));
      }
    }
    insertSum(sums, "UKUPNO", totalSati, totalPrim);
    
    // naknade
    BigDecimal totalNak = Aus.zero0;
    for (Iterator i = primiznos.iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      if (primsati.getBigDecimal(key).signum() == 0 && primiznos.getBigDecimal(key).signum() > 0) {
        insertSum(sums, key, null, primiznos.getBigDecimal(key));
        totalNak = totalNak.add(primiznos.getBigDecimal(key));
      }
    }
    if (totalNak.signum() > 0)
      insertSum(sums, "NAKNADE", null, totalNak);
    
    // obustave
    BigDecimal totalOb = Aus.zero0;
    for (Iterator i = primiznos.iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      if (primsati.getBigDecimal(key).signum() == 0 && primiznos.getBigDecimal(key).signum() < 0) {
        insertSum(sums, key, null, primiznos.getBigDecimal(key));
        totalOb = totalOb.add(primiznos.getBigDecimal(key));
      }
    }
    if (totalOb.signum() > 0)
      insertSum(sums, "OBUSTAVE", null, totalOb);
    
    insertSum(sums, "", null, null);
    insertSum(sums, "SVEUKUPNO", totalSati, totalPrim.add(totalNak).add(totalOb));
    
    return true;
  }

  /*boolean findKum(DataSet ds, StorageDataSet out, StorageDataSet sums) {
    
    if (ds.rowCount() == 0) return false;
    
    Calendar cal = Calendar.getInstance();
    cal.set(cal.YEAR, god);
    cal.set(cal.MONTH, mje - 1);
    String[] dt = {"", "Ned", "Pon", "Uto", "Sri", "Èet", "Pet", "Sub"};
    
    sums.setColumns(new Column[] {
        dM.createStringColumn("PRIM", "Primanje", 50),
        dM.createColumn("SATI", "Sati", null, Variant.BIGDECIMAL, 2, 10, 2),
        dM.createColumn("IZNOS", "Iznos", null, Variant.BIGDECIMAL, 2, 10, 2)
    });
    sums.open();
    HashSum primsati = new HashSum(ds, "CVRP", "SATI");
    HashSum primiznos = new HashSum(ds, "CVRP", "IZNOS");
    
    HashSet grs = new HashSet();
    HashSum[] dani = new HashSum[maxDana];
    for (int i = 0; i < maxDana; i++)
        dani[i] = new HashSum(ds, "GRPRIS", "SATI");
    for (ds.first(); ds.inBounds(); ds.next()) {
        grs.add(ds.getString("GRPRIS"));
        int dan = ds.getShort("DAN");
        if (dan > maxDana) {
            System.out.println("Weird bug! dan = " + dan);
            dan = maxDana;
        }
        if (ds.getBigDecimal("SATI") != null && ds.getBigDecimal("SATI").signum() > 0)
          dani[dan - 1].add();
        primsati.add();
        primiznos.add();
    }
    
    Column[] cols = new Column[grs.size() + 3];
    cols[0] = dM.createIntColumn("DAN", "Dan");
    cols[1] = dM.createStringColumn("DTJE", "Tje", 3);
    cols[2] = dM.createColumn("UK", "Ukupno", null, Variant.BIGDECIMAL, 2, 6, 2);
    ArrayList lgr = new ArrayList(grs);
    Collections.sort(lgr);
    for (int i = 0; i < lgr.size(); i++)
        cols[i+3] = dM.createColumn((String) lgr.get(i), (String) lgr.get(i), null, Variant.BIGDECIMAL, 2, 6, 2);
    
    lgr.add("UK");
    
    out.setColumns(cols);
    out.open();
    
    for (int i = 0; i < maxDana; i++) {
        out.insertRow(false);
        out.setInt("DAN", i + 1);
        cal.set(cal.DATE, i + 1);
        out.setString("DTJE", dt[cal.get(cal.DAY_OF_WEEK)]);
        
        for (Iterator g = dani[i].iterator(); g.hasNext(); ) {
            String       gr = (String) g.next();
            out.setBigDecimal(gr, (BigDecimal) dani[i].get(gr));
            Aus.add(out, "UK", gr);
        }
    }
    
    // primanja
    BigDecimal totalPrim = Aus.zero0;
    BigDecimal totalSati = Aus.zero0;
    for (Iterator i = primiznos.iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      if (primsati.getBigDecimal(key).signum() > 0) {
        insertSum(sums, key, primsati.getBigDecimal(key), primiznos.getBigDecimal(key));
        totalPrim = totalPrim.add(primiznos.getBigDecimal(key));
        totalSati = totalSati.add(primsati.getBigDecimal(key));
      }
    }
    insertSum(sums, "UKUPNO", totalSati, totalPrim);
    
    // naknade
    BigDecimal totalNak = Aus.zero0;
    for (Iterator i = primiznos.iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      if (primsati.getBigDecimal(key).signum() == 0 && primiznos.getBigDecimal(key).signum() > 0) {
        insertSum(sums, key, null, primiznos.getBigDecimal(key));
        totalNak = totalNak.add(primiznos.getBigDecimal(key));
      }
    }
    if (totalNak.signum() > 0)
      insertSum(sums, "NAKNADE", null, totalNak);
    
    // obustave
    BigDecimal totalOb = Aus.zero0;
    for (Iterator i = primiznos.iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      if (primsati.getBigDecimal(key).signum() == 0 && primiznos.getBigDecimal(key).signum() < 0) {
        insertSum(sums, key, null, primiznos.getBigDecimal(key));
        totalOb = totalOb.add(primiznos.getBigDecimal(key));
      }
    }
    if (totalOb.signum() > 0)
      insertSum(sums, "OBUSTAVE", null, totalOb);
    
    insertSum(sums, "", null, null);
    insertSum(sums, "SVEUKUPNO", totalSati, totalPrim.add(totalNak).add(totalOb));
    
    return true;
  }*/
  
  void insertSum(StorageDataSet sums, String key, BigDecimal sati, BigDecimal iznos) {
    sums.insertRow(false);
    if (vrprims.has(key))
      sums.setString("PRIM", vrprims.get(key).getString("NAZIV"));
    else sums.setString("PRIM", key);
    if (sati != null) sums.setBigDecimal("SATI", sati);
    if (iznos != null) sums.setBigDecimal("IZNOS", iznos);
  }
  
  HashMap createPartition(DataSet radnici, List allData) {
    HashMap parts = new HashMap();
    
    HashSet rads = new HashSet();
    for (radnici.first(); radnici.inBounds(); radnici.next())
      rads.add(radnici.getString("CRADNIK"));
    
    for (Iterator i = allData.iterator(); i.hasNext(); ) {
      PrisData pd = (PrisData) i.next();
      if (rads.contains(pd.cradnik)) {
        List part = (List) parts.get(pd.cradnik);
        if (part == null) parts.put(pd.cradnik, part = new ArrayList());
        part.add(pd);
      }
    }
    
    return parts;
  }
  
  void showAll() {
    if (getMasterSet().rowCount() == 0) return;
    
    if (all.isShowing()) all.hide();
    
    vrprims = new HashDataSet(dm.getVrsteprim(), "CVRP");
    allOut.clear();
    allSums.clear();
    
    Calendar cal = Calendar.getInstance();
    cal.set(cal.YEAR, god);
    cal.set(cal.MONTH, mje - 1);
    String[] dt = {"", "Ned", "Pon", "Uto", "Sri", "Èet", "Pet", "Sub"};
    
    QueryDataSet radnici = Radnicipl.getDataModule().openTempSet(getPreSelect().getLastFilterQuery());
    List allData = null;
    if (radnici.rowCount() < Radnicipl.getDataModule().getRowCount() / 3)
      allData = loadPrisutnost(Condition.in("CRADNIK", radnici));
    else allData = loadPrisutnost(Condition.ident);
    
    HashMap parts = createPartition(radnici, allData);
    for (Iterator i = parts.keySet().iterator(); i.hasNext(); ) {
      String cradnik = (String) i.next();
      System.out.println("Radnik: " + cradnik);
      List data = (List) parts.get(cradnik);
      if (data.size() > 0) {
        StorageDataSet out = new StorageDataSet();
        StorageDataSet sums = new StorageDataSet();
        if (findKum(data, out, sums)) {
          allOut.add(out);
          out.first();
          allSums.add(sums);
          allRad.add(cradnik);
        }
      }
    }
    
    allRbr = 0;
    setColsAndSums(all, (StorageDataSet) allOut.get(allRbr));
    if (ld.raLocate(dm.getRadnici(), "CRADNIK", (String) allRad.get(allRbr))) {
      String radname = dm.getRadnici().getString("CRADNIK") + " - " +
              dm.getRadnici().getString("IME") + " " + dm.getRadnici().getString("PREZIME");
      all.setTitle("Prikaz primanja radnika " + radname);
    }
    setSummary(all, (StorageDataSet) allSums.get(allRbr)); 
    ((StorageDataSet) allOut.get(allRbr)).first();
    all.jp.getNavBar().getOptions()[0].setEnabled(false);
    all.jp.getNavBar().getOptions()[1].setEnabled(allRbr < allOut.size() - 1);
    all.show();
    all.resizeLater();
  }
  
  void previousAll() {
    --allRbr;
    commonAll();
  }
  
  void nextAll() {
    ++allRbr;
    commonAll();
  }
  
  void commonAll() {
    all.jp.getNavBar().getOptions()[0].setEnabled(allRbr > 0);
    all.jp.getNavBar().getOptions()[1].setEnabled(allRbr < allOut.size() - 1);
    
    HashMap defs = new HashMap();
    int viscols = all.jp.getMpTable().getColumnCount();
    int others = 0;
    for (int i = 0; i < viscols; i++) {
      String col = all.jp.getMpTable().getRealColumnName(i);
      if (col.equals("DAN") || col.equals("DTJE") || col.equals("UK"))
        defs.put(col, new Integer(all.jp.getMpTable().getColumnModel().getColumn(i).getWidth()));
      else others += all.jp.getMpTable().getColumnModel().getColumn(i).getWidth();
    }
    Point old = all.jp.getViewPosition();
    setColsAndSums(all, (StorageDataSet) allOut.get(allRbr));
    viscols = all.jp.getMpTable().getColumnCount();
    for (int i = viscols - 1; i >= 0; i--) {
      String col = all.jp.getMpTable().getRealColumnName(i);
      if (col.equals("DAN") || col.equals("DTJE") || col.equals("UK"))
        if (!defs.containsKey(col))
          all.jp.getMpTable().getColumnModel().removeColumn(
              all.jp.getMpTable().getColumnModel().getColumn(i));
    }
    viscols = all.jp.getMpTable().getColumnCount();
    for (int i = 0; i < viscols; i++) {
      String col = all.jp.getMpTable().getRealColumnName(i);
      if (col.equals("DAN") || col.equals("DTJE") || col.equals("UK")) {
        Integer w = (Integer) defs.get(col);
        if (w != null) {
          all.jp.getMpTable().getColumnModel().getColumn(i).setWidth(w.intValue());
          all.jp.getMpTable().getColumnModel().getColumn(i).setPreferredWidth(w.intValue());
        }
      } else {
        all.jp.getMpTable().getColumnModel().getColumn(i).setWidth(others / (viscols - defs.size()));
        all.jp.getMpTable().getColumnModel().getColumn(i).setPreferredWidth(others / (viscols - defs.size()));
      }
    }
    all.jp.getColumnsBean().resetColumnWidths();
    if (ld.raLocate(dm.getRadnici(), "CRADNIK", (String) allRad.get(allRbr))) {
      String radname = dm.getRadnici().getString("CRADNIK") + " - " +
              dm.getRadnici().getString("IME") + " " + dm.getRadnici().getString("PREZIME");
      all.setTitle("Prikaz primanja radnika " + radname);
    }
    setSummary(all, (StorageDataSet) allSums.get(allRbr));
    all.jp.setViewPosition(old);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        all.jp.getColumnsBean().focusCombo();
      }
    });
  }
  
  void setSummary(frmTableDataView frm, StorageDataSet sums) {
    raExtendedTable totalTab = new raExtendedTable();
    totalTab.setDataSet(sums);
    totalTab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    totalTab.setCellSelectionEnabled(false);
    totalTab.clearSelection();
    totalTab.setAlternateColor(false);
    totalTab.setBackground(frm.jp.getBackground());
    totalTab.stopFire();
    frm.jp.installSummary(totalTab, 10, false);
  }
  
  void setColsAndSums(frmTableDataView frm, StorageDataSet out) {
    String[] acols = out.getColumnNames(out.getColumnCount());
    String[] cols = new String[acols.length - 2];
    System.arraycopy(acols, 2, cols, 0, cols.length);
    
    if (frm.isShowing()) {
      frm.jp.setDataSetAndSums(out, cols);
    } else {
      frm.setDataSet(out);
      frm.setSums(cols);
    
      if (frm.jp.getColumnsBean().getStoredCols() != null &&
        frm.jp.getColumnsBean().getStoredCols().length() > 0) {
          String[] scols = new VarStr(frm.jp.getColumnsBean().getStoredCols()).split(',');
          ArrayList ncols = new ArrayList();
          int cn = 0, cw = 0, p;
          for (int i = 0; i < scols.length - 1; i++) {
              if (scols[i].startsWith("DAN") ||   
                      scols[i].startsWith("DTJE") ||
                      scols[i].startsWith("UK")) ncols.add(scols[i]);
              else if ((p = scols[i].indexOf(':')) > 0) {
                  ++cn;
                  cw += Aus.getNumber(scols[i].substring(p + 1));
              }
          }
          int avg = cols.length > 1 ? cw / (cols.length-1) : 100;
          for (int i = 1; i < cols.length; i++)
              ncols.add(cols[i] + ":" + avg);
          ncols.add(scols[scols.length - 1]);
          frm.jp.getColumnsBean().updateCols(VarStr.join(ncols, ',').toString());
      }
    }
  }
  
  void showOne() {
  	DataSet ds = getDetailSet();
  	if (ds.rowCount() == 0) return;
  	
  	List data = fillPrisutnost();
    StorageDataSet out = new StorageDataSet();
    StorageDataSet sums = new StorageDataSet();
    
    vrprims = new HashDataSet(dm.getVrsteprim(), "CVRP");

  	findKum(data, out, sums);
    String[] acols = out.getColumnNames(out.getColumnCount());
    String[] cols = new String[acols.length - 2];
    System.arraycopy(acols, 2, cols, 0, cols.length);
  	
    frmTableDataView frm = new frmTableDataView();
    frm.setSaveName("Pregled-prisutobr");
    setColsAndSums(frm, out);
    frm.setTitle(raDetail.getTitle());
    frm.jp.addTableModifier(weekend);
    setSummary(frm, sums);
    frm.setCounterEnabled(false);
    out.first();
    frm.show();
    frm.resizeLater();
  }
  
  private void jbInit() throws Exception {
    this.setMasterSet(dm.getRadnicipl());
    this.setNaslovMaster("Unos prisutnosti na radu");
    this.setVisibleColsMaster(new int[] {0, 1, 2, 3});
    this.setMasterKey(key);
    jpMaster = new jpRadnicipl(this.raMaster);
    this.setJPanelMaster(jpMaster);

    this.setDetailSet(dm.getPrisutobr());
    this.setNaslovDetail("Prisutnost");
    this.setVisibleColsDetail(new int[] {2, 1, 3, 4, 5});
    this.setDetailKey(key);
    set_kum_detail(true);
    stozbrojiti_detail(new String[] {"SATI", "IZNOS"});
    setnaslovi_detail(new String[] {"Sati", "Iznos"});

    raMaster.getJpTableView().setNoTablePanel(new frmRadnicipl.jpNoTableGetRadnici(raMaster.getJpTableView()));
    jpDetail = new jpPrisRad(this);
    this.setJPanelDetail(jpDetail);
    raMaster.getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier("CRADNIK", new String[] {"CRADNIK", "IME", "PREZIME"}, dm.getRadnici())
    );
    raMaster.setEditEnabled(false);
    raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[3],true);
    raMaster.addOption(rnvShowAll, 5, false);
    raMaster.addOption(rnvGrupe, 6, false);
    raDetail.getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier("CVRP", new String[] {"CVRP", "NAZIV"}, dm.getVrsteprim())
    );
    raDetail.addOption(rnvShowOne, 4, false);
    //raDetail.addOption(rnvReCalc, 5, false);
    
    all.setSaveName("Pregled-prisutobr-all");
    all.jp.addTableModifier(weekend);
    all.jp.setAutoPos(false);
    all.jp.rmKeyListener(all);
    all.jp.getNavBar().addOption(new raNavAction("Prethodni", raImages.IMGBACK, KeyEvent.VK_F3) {
      public void actionPerformed(ActionEvent e) {
        previousAll();
      }
    }, 0); 
    all.jp.getNavBar().addOption(new raNavAction("Sljedeæi", raImages.IMGFORWARD, KeyEvent.VK_F4) {
      public void actionPerformed(ActionEvent e) {
        nextAll();
      }
    }, 1);
    all.jp.initKeyListener(all);
    all.setCounterEnabled(false);
  }
  
  List loadPrisutnost(Condition cond) {
    List ret = new ArrayList();
    ResultSet rs = Util.openQuickSet("SELECT cradnik,cvrp,dan,grpris,sati,iznos FROM prisutobr WHERE " + cond);
    try {
      while (rs.next()) 
        ret.add(new PrisData(rs));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Util.closeQuickSet(rs);
    return ret;
  }
  
  List fillPrisutnost() {
    List ret = new ArrayList();
    DataSet ds = getDetailSet();
    if (ds.rowCount() == 0) return ret;
    int row = ds.getRow();
    raDetail.getJpTableView().getMpTable().stopFire();
    ds.enableDataSetEvents(false);
    try {
      for (ds.first(); ds.inBounds(); ds.next())
        ret.add(new PrisData(ds));
    } finally {
      ds.goToRow(row);
      ds.enableDataSetEvents(true);
      raDetail.getJpTableView().getMpTable().startFire();
    }
    return ret;
  }
  
  static class PrisData {
    String cradnik;
    int dan;
    int cvrp;
    String grpris;
    BigDecimal sati;
    BigDecimal iznos;
    public PrisData(ResultSet row) {
      try {
        cradnik = row.getString(1).trim();
        cvrp = row.getShort(2);
        dan = row.getShort(3);
        grpris = row.getString(4).trim();
        sati = new BigDecimal(row.getDouble(5)).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (row.wasNull()) sati = null;
        iznos = new BigDecimal(row.getDouble(6)).setScale(2, BigDecimal.ROUND_HALF_UP);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new RuntimeException("Error loading main query data");
      }
    }
    public PrisData(DataSet ds) {
      cradnik = ds.getString("CRADNIK");
      cvrp = ds.getShort("CVRP");
      dan = ds.getShort("DAN");
      grpris = ds.getString("GRPRIS");
      sati = ds.getBigDecimal("SATI");
      if (ds.isNull("SATI")) sati = null;
      iznos = ds.getBigDecimal("IZNOS");
    }
  }
}
