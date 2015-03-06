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

import hr.restart.baza.Condition;
import hr.restart.baza.Orgstruktura;
import hr.restart.baza.Prisutobr;
import hr.restart.baza.Radnici;
import hr.restart.baza.Radnicipl;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.*;
import hr.restart.util.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


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
  //HashDataSet kumulrad;
  HashMap kumulrad;
  
  raNavAction rnvShowOne  = new raNavAction("Prikaži raspored",raImages.IMGALIGNJUSTIFY,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      showOne();
    }
  };
  
  raNavAction rnvReCalc  = new raNavAction("Preraèunaj primanja",raImages.IMGPREFERENCES,KeyEvent.VK_F7,KeyEvent.SHIFT_MASK) {
    public void actionPerformed(ActionEvent e) {
      reCalcAll();
    }
  };
  
  raNavAction rnvShowAll  = new raNavAction("Prikaži raspored svih radnika",raImages.IMGALIGNJUSTIFY,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      showAll();
    }
  };
  
  raNavAction rnvPotpisList  = new raNavAction("Prikaži potpisnu listu",raImages.IMGMOVIE,KeyEvent.VK_F11) {
    public void actionPerformed(ActionEvent e) {
      showRadnici();
    }
  };
   
  /*raNavAction rnvGrupe  = new raNavAction("Rekapitulacija po grupama",raImages.IMGPREFERENCES,KeyEvent.VK_F8, KeyEvent.SHIFT_MASK) {
    public void actionPerformed(ActionEvent e) {
      showGrupe();
    }
  };*/
  
  raNavAction rnvRekap  = new raNavAction("Rekapitulacije",raImages.IMGHISTORY,KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
      showRekap();
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
	
	raTableModifier primmod = new raTableModifier() {
	  private Variant v = new Variant();
	  private VarStr s = new VarStr();
      public boolean doModify() {        
        JraTable2 jtab = (JraTable2)getTable();
        Column dsCol = jtab.getDataSetColumn(getColumn());
        if (dsCol == null) return false;
        String col = dsCol.getColumnName();
        if (col.equals("DAN") || col.equals("DTJE") || col.equals("UK")) return false;
        return true;
      }

      public void modify() {
        JraTable2 jtab = (JraTable2)getTable();
        String col = jtab.getDataSetColumn(getColumn()).getColumnName();
        jtab.getDataSet().getVariant("MOD", getRow(), v);
        s.clear().append(v);
        String mod = s.extract(col+":", "|");
        if (mod != null && mod.length() > 0) {
          ((JLabel) renderComponent).setText("(" + mod + ")  " + ((JLabel) renderComponent).getText());
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
  
  public void findDan() {
    int dan = getDetailSet().getShort("DAN");
    
    if (dan == 0) jpDetail.jlDanTj.setText("");
    else {
      Calendar cal = Calendar.getInstance();
      cal.set(cal.YEAR, god);
      cal.set(cal.MONTH, mje - 1);
      cal.set(cal.DATE, dan);
      
      jpDetail.jlDanTj.setText(Sih.dtj[cal.get(cal.DAY_OF_WEEK)]);
    }
  }
  
  public boolean isWeekend() {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.YEAR, god);
    cal.set(cal.MONTH, mje - 1);
    cal.set(cal.DATE, getDetailSet().getShort("DAN"));
    
    return cal.get(cal.DAY_OF_WEEK) == cal.SATURDAY || cal.get(cal.DAY_OF_WEEK) == cal.SUNDAY;
  }
  
  public void SetFokusDetail(char mode) {
  	if (mode == 'N') {
  		rcc.EnabDisabAll(jpDetail, true);
  		raDetail.restorePreviousValue(jpDetail.jlrCvrp);
  		jpDetail.jlrCvrp.forceFocLost();
  		raDetail.restorePreviousValue(jpDetail.jlrCgrup);
        jpDetail.jlrCgrup.forceFocLost();
        raDetail.restorePreviousValue(jpDetail.jraDan);
        do {
          getDetailSet().setShort("DAN", (short) (Aus.getNumber(jpDetail.jraDan.getText()) + 1));
        } while (getDetailSet().getShort("DAN") < maxDana && isWeekend());
        findDan();
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
  
  void showRadnici() {
    raProcess.runChild(raMaster.getWindow(), new Runnable() {
      public void run() {
        HashSet rads = Sih.createRads(Aus.getCorgInCond(getPreSelect().getSelRow().getString("CORG")));
        raProcess.yield(Sih.generatePotpisList(Condition.none, rads));
      }
    });
    
    if (!raProcess.isCompleted()) return;
    StorageDataSet out = (StorageDataSet) raProcess.getReturnValue();
    if (out == null) return;

    frmTableDataView frm = new frmTableDataView();
    frm.setDataSet(out);
    frm.setSaveName("Pregled-prisutobr-radnici-place");
    frm.setVisibleCols(new int[] {0, 1, 2, 3, 4, 5});
    frm.setSums(new String[] {"TOTAL", "PLISTA", "RAZLIKA"});
    frm.setTitle("POTPISNA LISTA  obraèun za " + mje + ". mjesec " + god +".");
    raExtendedTable t = (raExtendedTable) frm.jp.getMpTable();
    t.addSort("PRIME", true);
    t.createSortDescriptor();
    t.setDrawLines(true);
    frm.show();
    frm.resizeLater();
  }
 
  
  void showPrim(final Condition cond, final Condition arh, String title, String corg) {
    raProcess.runChild(raMaster.getWindow(), new Runnable() {
      public void run() {
        HashSet rads = Sih.createRads(cond);
        raProcess.yield(arh != null ? Sih.generateArhPrimanjaIzvj(arh, rads) : Sih.generatePrimanjaIzvj(Condition.none, rads));
      }
    });
    if (!raProcess.isCompleted()) return;
    StorageDataSet[] ret = (StorageDataSet[]) raProcess.getReturnValue();
    if (ret == null || ret[0] == null) return;
    
    StorageDataSet out = ret[0];
    StorageDataSet sums = ret[1];

    frmTableDataView frm = new frmTableDataView();
    frm.setDataSet(out);
    frm.setSaveName("Pregled-prisutobr-primanja");
    frm.setVisibleCols(new int[] {0, 1, 2, 3});
    frm.setSums(new String[] {"SATI", "IZNOS"});
    //frm.setTitle("Rekapitulacija plaæe po vrstama zarada" +
    //              "  obraèun za " + mje + ". mjesec " + god +".");
    frm.setTitle(title);
    raExtendedTable xt = (raExtendedTable) frm.jp.getMpTable();
    xt.addToGroup("CORG", true, new String[] {"#\nOrg. jedinica:       ", "CORG", "#  - ", "NAZIV"}, 
        Orgstruktura.getDataModule().openTempSet(Condition.equal("CORG", corg)), true);
    setSummary(frm, sums);    
    MyTableSyncAdapter sync = new MyTableSyncAdapter(frm.jp);
    frm.jp.getMpTable().addComponentListener(sync);
    frm.jp.getMpTable().getColumnModel().addColumnModelListener(sync);
    frm.setCounterEnabled(false);
    frm.show();
    frm.resizeLater();
    sync.alignLater();
  }
  
  static class MyTableSyncAdapter extends ComponentAdapter implements TableColumnModelListener {
    private raJPTableView jp;
    public MyTableSyncAdapter(raJPTableView owner) {
      this.jp = owner;
      
    } 
    public void componentResized(ComponentEvent e) {
      alignTables();
    }
    public void columnSelectionChanged(ListSelectionEvent e) { 
    }
    public void columnRemoved(TableColumnModelEvent e) {
      alignTables();
    }    
    public void columnMoved(TableColumnModelEvent e) {
      alignTables();
    }
    public void columnMarginChanged(ChangeEvent e) {
      alignTables();
    }
    public void columnAdded(TableColumnModelEvent e) {
      alignTables();
    }
    
    int defer = 0;
    public void alignLater() {
      defer = 0;
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          alignImpl();
        }
      });
    }
    
    void alignImpl() {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          if (++defer < 5) alignImpl();
          else alignTables();
        }
      });
    }
    
    void alignTables() {
      raExtendedTable src = (raExtendedTable) jp.getMpTable();
      raExtendedTable summ = (raExtendedTable) jp.getSummary();
      if (src.getDataSet() == null) return;
      
      int prim = 0, sati = 0, iznos = 0, wid = 0;
      
      for (int i = 0; i < src.getColumnCount(); i++) {
        wid = src.getColumnModel().getColumn(i).getWidth();
        if (src.getRealColumnName(i).equalsIgnoreCase("SATI")) sati = wid;
        else if (src.getRealColumnName(i).equalsIgnoreCase("IZNOS")) iznos = wid;
        else prim += wid;
      }
      for (int i = 0; i < summ.getColumnCount(); i++) {
        if (summ.getRealColumnName(i).equalsIgnoreCase("PRIM")) wid = prim;
        else if (summ.getRealColumnName(i).equalsIgnoreCase("SATI")) wid = sati;
        else if (summ.getRealColumnName(i).equalsIgnoreCase("IZNOS")) wid = iznos;
        else wid = 0;
        if (wid > 0) 
          summ.getColumnModel().getColumn(i).setPreferredWidth(wid);
      }
    }
  }
  
  void showGrupe(final Condition cond, final Condition arh, final String corg, String title) {
    raProcess.runChild(raMaster.getWindow(), new Runnable() {
      public void run() {
        HashSet rads = Sih.createRads(cond);
        if (corg == null)
          raProcess.yield(arh != null ?
              Sih.generateArhCorgGrupeIzvj(jpDetail.jlCsif.getText(), arh, rads) : 
                Sih.generateCorgGrupeIzvj(jpDetail.jlCsif.getText(), Condition.none, rads));
        raProcess.yield(arh != null ? 
            Sih.generateArhGrupeIzvj(jpDetail.jlCsif.getText(), arh, rads) : 
              Sih.generateGrupeIzvj(jpDetail.jlCsif.getText(), Condition.none, rads));
      }
    });
    
    if (!raProcess.isCompleted()) return;
    StorageDataSet out = (StorageDataSet) raProcess.getReturnValue();
    if (out == null) return;
    
    frmTableDataView frm = new frmTableDataView();
    frm.setDataSet(out);
    if (corg == null) {
      frm.setSaveName("Pregled-prisutobr-corg-grupe");
      frm.setVisibleCols(new int[] {2, 3, 4, 5});
    } else {
      frm.setSaveName("Pregled-prisutobr-grupe");
      frm.setVisibleCols(new int[] {1, 2, 3, 4});
    }
    frm.setSums(new String[] {"SATI", "IZNOS"});
    frm.setTitle(title);
    
    /*frm.jp.addTableModifier(
       new hr.restart.swing.raTableColumnModifier("CVRP", new String[] {"CVRP", "NAZIV"}, dm.getVrsteprim()));*/
    frm.jp.addTableModifier(new raTableModifier() {
      Column dsCol;
      Variant v = new Variant();
      public void modify() {
        dsCol.getDataSet().getVariant("UNC", getRow(), v);
        ((JLabel) this.renderComponent).setText(dsCol.format(v));
      }
      public boolean doModify() {
        if (getTable() instanceof JraTable2) {
          dsCol = ((JraTable2) getTable()).getDataSetColumn(getColumn());
          if (dsCol == null) return false;
          return dsCol.getColumnName().equalsIgnoreCase("IZNOS");
        }
        return false;
      }
    });
    raExtendedTable t = (raExtendedTable) frm.jp.getMpTable();
    String[] exts = {"PRIMANJA", "NAKNADE", "OBUSTAVE", "UKUPNO", 
    		"PLAÆA S LISTE", "DOPRINOSI I POREZI", corg == null ? "SVEUKUPNO [VALUE]" : "SVEUKUPNO"};
    if (corg == null) t.addToGroup("CORG", "NAZIV", dM.getDataModule().getOrgstruktura(), exts);
    else t.addToGroup("CORG", "#\nOrg. jedinica:    |CORG|NAZIV|#\n", 
        Orgstruktura.getDataModule().openTempSet(Condition.equal("CORG", corg)), exts);
    t.addToGroup("CSIF", "NAZIV", jpDetail.jlrCgrup.getRaDataSet());
    t.addSort("CVRP", true);
    t.createSortDescriptor();
    frm.setCounterEnabled(false);
//    if (corg == null) {
    	t.setForcePage(true);
    	t.setExtNumber(new ExtNumber() {
    		Variant v = new Variant();
				public double getNumber(raExtendedTable t, int row, int sn) {
					if (sn == 6) return getReal(t, row, "IZNOS");
					
					t.getDataSet().getVariant("CVRP", row, v);
					boolean plista = v.getShort() == Sih.cPrimPlista;
					boolean doppor = v.getShort() == Sih.cPrimDopPor;
					if (sn == 3)
						return doppor ? 0 : getReal(t, row, "IZNOS");					
					if (sn == 0 || sn == 1 || sn == 2) {
						if (doppor || plista) return 0;
						t.getDataSet().getVariant("SATI", row, v);
						boolean no = v.getBigDecimal().signum() == 0;
						t.getDataSet().getVariant("IZNOS", row, v);
						boolean poz = v.getBigDecimal().signum() > 0;
						if ((no && poz && sn == 1) || (no && !poz && sn == 2) || (!no && sn == 0)) 
							return v.getAsDouble();
						return 0;
					}
					if (sn == 4 && plista) return getReal(t, row, "UNC");
					if (sn == 5 && doppor) return getReal(t, row, "IZNOS");
					
					return 0;
				}
				public double getReal(raExtendedTable t, int row, String col) {
					t.getDataSet().getVariant(col, row, v);
					return v.getAsDouble();
				}
			});
    //}
    frm.show();
    frm.resizeLater();
  }
  
  void reCalcAll() {
    if (getDetailSet().rowCount() == 0) return;
    rcp.addCalcSet(getDetailSet(), "prisutobr");
    rcp.addCalcSet(getMasterSet(), "radnicipl");
    
    for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) 
      if (!getDetailSet().isNull("CVRP"))
        rcp.calcPrisut(getDetailSet());
    
    getDetailSet().saveChanges();
  }
  
  void reCalc() {
    if (getDetailSet().isNull("CVRP")) return;
    
    rcp.addCalcSet(getDetailSet(), "prisutobr");
    rcp.addCalcSet(getMasterSet(), "radnicipl");
    rcp.calcPrisut(getDetailSet());
  }
  
  HashMap createPartition(HashSet rads, List allData) {
    HashMap parts = new HashMap();
    
    for (Iterator i = allData.iterator(); i.hasNext(); ) {
      Sih.Data pd = (Sih.Data) i.next();
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
    
    allOut.clear();
    allSums.clear();
    allRad.clear();
    
    raProcess.runChild(raMaster.getWindow(), new Runnable() {
      public void run() {
        Sih.start();
        vrprims = new HashDataSet(dm.getVrsteprim(), "CVRP");
        QueryDataSet radnici = Radnicipl.getDataModule().getTempSet(getPreSelect().getLastFilterQuery());
        Sih.report("Otvaranje radnika...");
        raProcess.openScratchDataSet(radnici);
        HashSet rads = new HashSet();
        for (radnici.first(); radnici.inBounds(); radnici.next())
          rads.add(radnici.getString("CRADNIK"));
        radnici.close();

        List allData = Sih.loadPrisutobr(Condition.none, rads);
        
        ArrayList newOut = new ArrayList();
        ArrayList newSums = new ArrayList();
        ArrayList newRad = new ArrayList();
        
        Sih.report("Particioniranje...");
        HashMap parts = createPartition(rads, allData);
        
        Sih.report("Sortiranje...");
        ArrayList arad = new ArrayList(parts.keySet());
        Collections.sort(arad, new Comparator() {
          HashMap prime = new HashMap();
          {
            QueryDataSet rads = Radnici.getDataModule().getTempSet();
            raProcess.openScratchDataSet(rads);
            for (rads.first(); rads.inBounds(); rads.next())
              prime.put(rads.getString("CRADNIK"), myCol.getCollationKey(rads.getString("PREZIME") + " " + rads.getString("IME")));
            rads.close();
          }
          public int compare(Object o1, Object o2) {
            CollationKey pi1 = (CollationKey) prime.get(o1);
            CollationKey pi2 = (CollationKey) prime.get(o2);
            return pi1.compareTo(pi2);
          }
        });
        HashSum[] netodopp = Sih.loadNetoDopp(Condition.none, rads);
        Sih.report("Kumuliranje...");
        for (Iterator i = arad.iterator(); i.hasNext(); ) {
          String cradnik = (String) i.next();
          List data = (List) parts.get(cradnik);
          if (data.size() > 0) {
            raProcess.checkClosing();
            StorageDataSet[] ret = Sih.findKumRad(data, god, mje, vrprims, netodopp[0], netodopp[1]);
            if (ret[0].rowCount() > 0) {
              newOut.add(ret[0]);
              ret[0].first();
              newSums.add(ret[1]);
              newRad.add(cradnik);
            }
          }
        }
        raProcess.checkClosing();
        Sih.report("Finaliziranje...");
        allOut.clear();
        allOut.addAll(newOut);
        allSums.clear();
        allSums.addAll(newSums);
        allRad.clear();
        allRad.addAll(newRad);
        Sih.report("Gotovo.");
      }
    });

    if (!raProcess.isCompleted()) return;
    System.out.println("Prikaz:");
    
    if (allOut.size() == 0) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), "Nije unesen nijedan radnik!", "Greška", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    allRbr = 0;
    setColsAndSums(all, (StorageDataSet) allOut.get(allRbr));
    if (ld.raLocate(dm.getRadnici(), "CRADNIK", (String) allRad.get(allRbr))) {
      String radname = dm.getRadnici().getString("CRADNIK") + " - " +
              dm.getRadnici().getString("IME") + " " + dm.getRadnici().getString("PREZIME");
      all.setTitle("Prikaz primanja radnika " + radname + "  obraèun za " + mje + ". mjesec " + god +".");
    }
    all.setCounterText("  Radnik " + (allRbr + 1) + " od " + allOut.size());
    setSummary(all, (StorageDataSet) allSums.get(allRbr)); 
    ((StorageDataSet) allOut.get(allRbr)).first();
    all.jp.getNavBar().getOptions()[0].setEnabled(false);
    all.jp.getNavBar().getOptions()[1].setEnabled(allRbr < allOut.size() - 1);
    all.show();
    all.resizeLater();
  }
  
  void firstAll() {
    allRbr = 0;
    commonAll();
  }
  
  void previousAll() {
    --allRbr;
    commonAll();
  }
  
  void nextAll() {
    ++allRbr;
    commonAll();
  }
  
  void lastAll() {
    allRbr = allOut.size() - 1;
    commonAll();
  }
  
  void commonAll() {
    all.jp.getNavBar().getOptions()[0].setEnabled(allRbr > 0);
    all.jp.getNavBar().getOptions()[1].setEnabled(allRbr > 0);
    all.jp.getNavBar().getOptions()[2].setEnabled(allRbr < allOut.size() - 1);
    all.jp.getNavBar().getOptions()[3].setEnabled(allRbr < allOut.size() - 1);
    
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
    System.out.println("old pos: " + old);
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
      all.setTitle("Prikaz primanja radnika " + radname + "  obraèun za " + mje + ". mjesec " + god +".");
    }
    all.setCounterText("  Radnik " + (allRbr + 1) + " od " + allOut.size());
    setSummary(all, (StorageDataSet) allSums.get(allRbr));
    all.jp.setViewPosition(old);
    System.out.println("new pos " + all.jp.getViewPosition());
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
    String[] cols = new String[acols.length - 3];
    System.arraycopy(acols, 3, cols, 0, cols.length);
    
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
  	
  	Condition cond = Condition.equal("CRADNIK",  getMasterSet());
    
    vrprims = new HashDataSet(dm.getVrsteprim(), "CVRP");
    HashSum[] netodopp = Sih.loadNetoDopp(cond, null);
    
    StorageDataSet[] ret = Sih.findKumRad(Sih.loadPrisutobr(cond, null), god, mje, vrprims, netodopp[0], netodopp[1]);
    StorageDataSet out = ret[0];
    StorageDataSet sums = ret[1];

    String[] acols = out.getColumnNames(out.getColumnCount());
    String[] cols = new String[acols.length - 2];
    System.arraycopy(acols, 2, cols, 0, cols.length);
  	
    frmTableDataView frm = new frmTableDataView();
    frm.setSaveName("Pregled-prisutobr");
    setColsAndSums(frm, out);
    frm.setTitle(raDetail.getTitle() + "  obraèun za " + mje + ". mjesec " + god +".");
    frm.jp.addTableModifier(weekend);
    frm.jp.addTableModifier(primmod);
    setSummary(frm, sums);
    frm.setCounterEnabled(false);
    out.first();
    frm.show();
    frm.resizeLater();
  }
  
  raInputDialog dlg = new raInputDialog() {
    protected boolean checkOk() {
      if (Valid.getValid().isEmpty(jpc.corg)) return false;
      if (!jcbArh.isSelected()) return true;
      if (Valid.getValid().isEmpty(jraGod)) return false;
      int god = sds.getShort("GODOBR");
      if (god < 1990 || god > 2100) {
        jraGod.requestFocus();
        JOptionPane.showMessageDialog(jraGod, "Pogrešna godina!", "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (!jraMj.isEmpty()) {
        int mj = sds.getShort("MJOBR");
        if (mj < 1 || mj > 12) {
          jraGod.requestFocus();
          JOptionPane.showMessageDialog(jraGod, "Pogrešan mjesec!", "Greška", JOptionPane.ERROR_MESSAGE);
          return false;
        }
      }
      return true;
    };
  };
  JraPanel pan = new JraPanel();
  StorageDataSet sds = new StorageDataSet();
  jpCorg jpc = new jpCorg(100, 350, true);
  raButtonGroup bg = new raButtonGroup();
  JraRadioButton jrbP = new JraRadioButton();
  JraRadioButton jrbG = new JraRadioButton();
  JraRadioButton jrbOJG = new JraRadioButton();
  JraCheckBox jcbArh = new JraCheckBox();
  JraTextField jraGod = new JraTextField();
  JraTextField jraMj = new JraTextField();
  
  void showRekap() {
    checkGodMj();
    if (jpc.corg.isEmpty())
      jpc.setCorg(getPreSelect().getSelRow().getString("CORG"));
    if (dlg.show(raMaster.getWindow(), pan, "Rekapitulacije")) {
      Condition arh = null;
      String ob = "  obraèun za " + mje + ". mjesec " + god + ".";
      if (jcbArh.isSelected()) {
        arh = jraMj.isEmpty() ? Condition.equal("GODOBR", sds) : Condition.whereAllEqual(new String[] {"GODOBR", "MJOBR"}, sds);
        ob = jraMj.isEmpty() ? "  obraèun za " + jraGod.getText() + ". godinu" : 
          "  obraèun za " + jraMj.getText() + ". mjesec " + jraGod.getText() + ".";
      }
      System.out.println(jpc.getCondition());
      if (jrbP.isSelected()) {
        showPrim(jpc.getCondition(), arh, "Rekapitulacija plaæe po vrstama zarada" + ob, jpc.getCorg()); 
      } else if (jrbG.isSelected()) {
        showGrupe(jpc.getCondition(), arh, jpc.getCorg(), "Rekapitulacija plaæe po grupama - " + jpDetail.jlCsif.getText() + ob);
      } else if (jrbOJG.isSelected()) {
        showGrupe(jpc.getCondition(), arh, null, "Rekapitulacija plaæe po org. jedinicama" + ob);
      }
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
    raMaster.addOption(rnvRekap, 6, false);
    //raMaster.addOption(rnvGrupe, 7, false);
    raMaster.addOption(rnvPotpisList, 7, false);
    raDetail.getJpTableView().addTableModifier(
      new hr.restart.swing.raTableColumnModifier("CVRP", new String[] {"CVRP", "NAZIV"}, dm.getVrsteprim())
    );
    raDetail.addOption(rnvShowOne, 4, false);
    raDetail.addOption(rnvReCalc, 5, false);
    
    all.setSaveName("Pregled-prisutobr-all");
    all.jp.addTableModifier(weekend);
    all.jp.addTableModifier(primmod);
    all.jp.rmKeyListener(all);
    all.jp.getNavBar().addOption(new raNavAction("Prvi", raImages.IMGALLBACK, KeyEvent.VK_F3, KeyEvent.SHIFT_MASK) {
      public void actionPerformed(ActionEvent e) {
        firstAll();
      }
    }, 0);
    all.jp.getNavBar().addOption(new raNavAction("Prethodni", raImages.IMGBACK, KeyEvent.VK_F3) {
      public void actionPerformed(ActionEvent e) {
        previousAll();
      }
    }, 1); 
    all.jp.getNavBar().addOption(new raNavAction("Sljedeæi", raImages.IMGFORWARD, KeyEvent.VK_F4) {
      public void actionPerformed(ActionEvent e) {
        nextAll();
      }
    }, 2);
    all.jp.getNavBar().addOption(new raNavAction("Zadnji", raImages.IMGALLFORWARD, KeyEvent.VK_F4, KeyEvent.SHIFT_MASK) {
      public void actionPerformed(ActionEvent e) {
        lastAll();
      }
    }, 3);
    all.jp.initKeyListener(all);
    all.setCounterEnabled(false);
    all.jp.setAutoPos(false);
    
    pan.setLayout(new XYLayout(645, 100));
    sds.setColumns(new Column[] {
        Orgstruktura.getDataModule().getColumn("CORG").cloneColumn(),
        dM.createStringColumn("VRSTA", "Vrsta izvješæa", 1),
        dM.createStringColumn("ARH", "Arhiva", "N", 1),
        dM.createShortColumn("GODOBR", "Godina"),
        dM.createShortColumn("MJOBR", "Mjesec")
    });
    bg.setColumnName("VRSTA");
    bg.setDataSet(sds);
    bg.add(jrbP, " Po primanjima ", "P");
    bg.add(jrbG, " Po grupama ", "G");
    bg.add(jrbOJG, " Po jedinicama i grupama ", "O");
    bg.setHorizontalTextPosition(SwingConstants.TRAILING);
    bg.setHorizontalAlignment(SwingConstants.LEADING);
    bg.setSelected(jrbP);
    jcbArh.setText(" Dohvat arhive: ");
    jcbArh.setDataSet(sds);
    jcbArh.setColumnName("ARH");
    jcbArh.setSelectedDataValue("D");
    jcbArh.setUnselectedDataValue("N");
    jcbArh.setHorizontalTextPosition(SwingConstants.TRAILING);
    jcbArh.setHorizontalAlignment(SwingConstants.LEADING);
    jraGod.setDataSet(sds);
    jraGod.setColumnName("GODOBR");
    jraMj.setDataSet(sds);
    jraMj.setColumnName("MJOBR");
    
    pan.add(jpc, new XYConstraints(0, 20, -1, -1));
    pan.add(new JLabel("Vrsta izvješæa"), new XYConstraints(15, 45, -1, -1));
    pan.add(jrbP, new XYConstraints(150, 45, -1, -1));
    pan.add(jrbG, new XYConstraints(285, 45, -1, -1));
    pan.add(jrbOJG, new XYConstraints(405, 45, -1, -1));
    pan.add(jcbArh, new XYConstraints(150, 70, -1, -1));
    pan.add(jraGod, new XYConstraints(285, 70, 50, -1));
    pan.add(jraMj, new XYConstraints(340, 70, 50, -1));
    
    jcbArh.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        checkGodMj();
      }
    });
  }
  
  void checkGodMj() {
    rcc.setLabelLaF(jraGod, jcbArh.isSelected());
    rcc.setLabelLaF(jraMj, jcbArh.isSelected());
  }
  
  static Collator myCol = Collator.getInstance();
}
