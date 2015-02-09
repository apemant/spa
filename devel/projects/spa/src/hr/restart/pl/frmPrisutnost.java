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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Primanjaobr;
import hr.restart.baza.Prisutobr;
import hr.restart.baza.Radnicipl;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;


public class frmPrisutnost extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  jpRadnicipl jpMaster;
  jpPrisRad jpDetail;
  raCalcPrimanja rcp = raCalcPrimanja.getRaCalcPrimanja();

  String radnikFull;
  int maxDana;
  int god, mje;
  String[] key = new String[] {"CRADNIK"};
  String[] allkey = new String[] {"CRADNIK", "DAN", "CVRP", "GRPRIS"};
  
  raNavAction rnvShowAll  = new raNavAction("Prikaži raspored",raImages.IMGALIGNJUSTIFY,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      showAll();
    }
  };
  
  raNavAction rnvReCalc  = new raNavAction("Izraèunaj primanja",raImages.IMGCOMPOSEMAIL,KeyEvent.VK_F7,KeyEvent.SHIFT_MASK) {
    public void actionPerformed(ActionEvent e) {
      reCalc();
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
  
  @Override
  public void beforeShowMaster() {
  	String corg = getPreSelect().getSelDataSet().getString("CORG");
  	if (!lookupData.getlookupData().raLocate(dm.getOrgpl(),"CORG",corg)) maxDana=31;
  	god = dm.getOrgpl().getShort("GODOBR");
    mje = dm.getOrgpl().getShort("MJOBR");
    
    Calendar cal = Calendar.getInstance();
    cal.setTime(Aus.createTimestamp(god, mje, 1));
    maxDana = cal.getActualMaximum(cal.DATE);
    System.out.println(god + "-" + mje + " = " + maxDana);
    
    if (lookupData.getlookupData().raLocate(dm.getVrstesif(), "VRSTASIF", "PLGP")) {
    	jpDetail.jlCsif.setText(dm.getVrstesif().getString("OPISVRSIF"));
    }
  }

  public void SetFokusMaster(char mode) {
  }
  public boolean ValidacijaMaster(char mode) {
    return false;
  }
  
  @Override
  public void refilterDetailSet() {
  	super.refilterDetailSet();
  	if (lookupData.getlookupData().raLocate(dm.getRadnici(), "CRADNIK", getMasterSet().getString("CRADNIK"))) {
  		radnikFull = dm.getRadnici().getString("CRADNIK") + " - " +
  				dm.getRadnici().getString("IME") + " " + dm.getRadnici().getString("PREZIME");
  		setNaslovDetail("Prisutnost radnika " + radnikFull);
  	}
  }
  
  public void SetFokusDetail(char mode) {
  	if (mode == 'N') {
  		rcc.EnabDisabAll(jpDetail, true);
  		jpDetail.jlrCvrp.emptyTextFields();
  		jpDetail.jlrCgrup.emptyTextFields();
  		jpDetail.jraDan.requestFocus();
  	} else if (mode == 'I') {
  		rcc.EnabDisabAll(jpDetail, false);
  		rcc.setLabelLaF(jpDetail.jraSat, true);
  		jpDetail.jraSat.requestFocus();
  	}
  	
  }
  public boolean ValidacijaDetail(char mode) {
  	if (vl.isEmpty(jpDetail.jlrCgrup) || vl.isEmpty(jpDetail.jraDan) || vl.isEmpty(jpDetail.jlrCvrp))
      return false;
  	
  	if (mode == 'N') {
  		if (Prisutobr.getDataModule().getRowCount(Condition.whereAllEqual(allkey, getDetailSet())) != 0) {
  			jpDetail.jlrCvrp.requestFocus();
  			JOptionPane.showMessageDialog(raDetail.getWindow(), "Zapis veæ postoji!", "Greška", JOptionPane.ERROR_MESSAGE);
  			return false;
  		}
  		if (getDetailSet().getShort("DAN") > maxDana) {
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
  
  void reCalc() {
  	DataSet ds = getDetailSet();
  	if (ds.rowCount() == 0) return;
  	
    int row = ds.getRow();
    raDetail.getJpTableView().getMpTable().stopFire();
    ds.enableDataSetEvents(false);
    try {
    	
    	QueryDataSet rads = Radnicipl.getDataModule().openTempSet(Condition.equal("CRADNIK", getMasterSet()));    	
    	QueryDataSet prim = Primanjaobr.getDataModule().openTempSet(Condition.equal("CRADNIK", rads));
    	prim.deleteAllRows();
    	prim.saveChanges();
    	int rbr = 0;
    	
    	for (ds.first(); ds.inBounds(); ds.next()) {
    		if (lookupData.getlookupData().raLocate(prim, "CVRP", "" + ds.getShort("CVRP"))) {
    			Aus.add(prim, "SATI", ds);
    		} else {
    			prim.insertRow(false);
    			prim.setShort("RBR", (short) ++rbr);
    			prim.setShort("CVRP", ds.getShort("CVRP"));
    			prim.setString("CRADNIK", ds.getString("CRADNIK"));
    			prim.setString("CORG", rads.getString("CORG"));
    			prim.setBigDecimal("KOEF", Aus.one0.movePointRight(2));
    			prim.setBigDecimal("SATI", ds.getBigDecimal("SATI"));
    		}
    	}
    	prim.saveChanges();
    	rcp.calcPrimanja(rads, prim, false);
    	rcp.clearCalcSets();
    	prim.refresh();
    	
    	frmTableDataView frm = new frmTableDataView();
    	frm.setDataSet(prim);
    	frm.setSaveName("Pregled-prisutobr-prim");
    	frm.setVisibleCols(new int[] {1, 4, 6});
    	frm.setSums(new String[] {"SATI", "BRUTO"});
    	frm.setTitle("Primanja radnika " + radnikFull);
    	frm.jp.addTableModifier(
    	   new hr.restart.swing.raTableColumnModifier("CVRP", new String[] {"CVRP", "NAZIV"}, dm.getVrsteprim()));
    	frm.show();
    	frm.resizeLater();
    	
    } finally {
    	ds.goToRow(row);
    	ds.enableDataSetEvents(true);
    	raDetail.getJpTableView().getMpTable().startFire();
    }
  }
  
  void showAll() {
  	DataSet ds = getDetailSet();
  	if (ds.rowCount() == 0) return;
  	
  	Calendar cal = Calendar.getInstance();
    cal.set(cal.YEAR, god);
    cal.set(cal.MONTH, mje - 1);
    String[] dt = {"", "Ned", "Pon", "Uto", "Sri", "Èet", "Pet", "Sub"};
  	
    int row = ds.getRow();
    raDetail.getJpTableView().getMpTable().stopFire();
    ds.enableDataSetEvents(false);
    try {
    	HashSet grs = new HashSet(); 
    	HashMap[] dani = new HashMap[maxDana];
    	for (int i = 0; i < maxDana; i++) 
    		dani[i] = new HashMap();
    	for (ds.first(); ds.inBounds(); ds.next()) {
    		grs.add(ds.getString("GRPRIS"));
    		int dan = ds.getShort("DAN");
    		if (dan > maxDana) {
    			System.out.println("Weird bug! dan = " + dan);
    			dan = maxDana;
    		}
    		if (ds.getBigDecimal("SATI") != null && ds.getBigDecimal("SATI").signum() > 0) {
    			BigDecimal sati = ds.getBigDecimal("SATI");
    			if (dani[dan - 1].containsKey(ds.getString("GRPRIS")))
    				sati = sati.add((BigDecimal) ds.getBigDecimal(ds.getString("GRPRIS"))); 
    			dani[dan - 1].put(ds.getString("GRPRIS"), sati);
    		}
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
    	
    	StorageDataSet out = new StorageDataSet();
    	out.setColumns(cols);
    	out.open();
    	
    	for (int i = 0; i < maxDana; i++) {
    		out.insertRow(false);
    		out.setInt("DAN", i + 1);
    		cal.set(cal.DATE, i + 1);
    		out.setString("DTJE", dt[cal.get(cal.DAY_OF_WEEK)]);
    		
    		for (Iterator g = dani[i].keySet().iterator(); g.hasNext(); ) {
    			String gr = (String) g.next();
    			out.setBigDecimal(gr, (BigDecimal) dani[i].get(gr));
    			Aus.add(out, "UK", gr);
    		}
    	}
    	
    	frmTableDataView frm = new frmTableDataView();
    	frm.setDataSet(out);
    	frm.setSaveName("Pregled-prisutobr");
    	frm.setSums((String[]) lgr.toArray(new String[lgr.size()]));
    	frm.setTitle(raDetail.getTitle());
    	frm.jp.addTableModifier(weekend);
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
    	int avg = cn > 0 ? cw / cn : 100;
    	for (int i = 0; i < lgr.size() - 1; i++)
    		ncols.add(lgr.get(i) + ":" + avg);
    	ncols.add(scols[scols.length - 1]);
    	frm.jp.getColumnsBean().updateCols(VarStr.join(ncols, ',').toString()); 
    	frm.show();
    	frm.resizeLater();
    } finally {
    	ds.goToRow(row);
    	ds.enableDataSetEvents(true);
    	raDetail.getJpTableView().getMpTable().startFire();
    }
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
    this.setVisibleColsDetail(new int[] {1, 2, 3, 4});
    this.setDetailKey(key);
    set_kum_detail(true);
    stozbrojiti_detail(new String[] {"SATI"});
    setnaslovi_detail(new String[] {"Sati"});

    raMaster.getJpTableView().setNoTablePanel(new frmRadnicipl.jpNoTableGetRadnici(raMaster.getJpTableView()));
    jpDetail = new jpPrisRad(this);
    this.setJPanelDetail(jpDetail);
    raMaster.getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier("CRADNIK", new String[] {"CRADNIK", "IME", "PREZIME"}, dm.getRadnici())
    );
    raMaster.setEditEnabled(false);
    raMaster.setEnabledNavAction(raMaster.getNavBar().getNavContainer().getNavActions()[3],true);
    raDetail.getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier("CVRP", new String[] {"CVRP", "NAZIV"}, dm.getVrsteprim())
    );
    raDetail.addOption(rnvShowAll, 4, false);
    raDetail.addOption(rnvReCalc, 5, false);
  }
}
