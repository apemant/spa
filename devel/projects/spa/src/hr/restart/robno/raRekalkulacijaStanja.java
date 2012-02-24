/****license*****************************************************************
**   file: raRekalkulacijaStanja.java
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
package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.Sklad;
import hr.restart.sisfun.dlgErrors;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.DataSetComparator;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raProcess;
import hr.restart.util.raUpitLite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raRekalkulacijaStanja extends raUpitLite {
  
  static Logger log = Logger.getLogger(raRekalkulacijaStanja.class);

	hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  
  lookupData ld = lookupData.getlookupData();

	QueryDataSet tmpStanje = new QueryDataSet();
  StorageDataSet oldStanje = new StorageDataSet();
  
  dlgErrors err;

	private raControlDocs rCD = new raControlDocs();

	private int cart = 0;

	hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

	java.math.BigDecimal Nula = new java.math.BigDecimal("0.00");

	String cskl = "2";

	String god = "2002";

	java.util.Date datDokumenta;

	JPanel jPanel1 = new JPanel();

	XYLayout xYLayout1 = new XYLayout();

	boolean isArtiklInUse = false;

	boolean isNewArt = false;

	rapancskl1 rpcskl = new rapancskl1() {
		void jbInitRest(boolean how) throws Exception {
			super.jbInitRest(how);
			this.xYLayout1.setWidth(640);
			remove(jbCSKL);
			remove(jrfNAZSKL);
			add(jrfNAZSKL, new XYConstraints(255, 25, 348, -1));
			add(jbCSKL, new XYConstraints(609, 25, 21, 21));
		}

		public void MYpost_after_lookUp() {
		}
	};

	JLabel tekst = new JLabel("Godina");

	JraTextField poljesnova = new JraTextField();
	
	JraCheckBox bit = new JraCheckBox();

	class myrapancart extends rapancart {
		public myrapancart(int i) {
			super(i);
		}

		public void Clear() {
			jrfCART.emptyTextFields();
			jrfCART1.emptyTextFields();
		}

		public void EnabDisabS(boolean how) {
			super.EnabDisab(how);
		}

		public void EnabDisab(boolean how) {
		}

		public void MYpost_after_lookUp() {
		}
	}

	myrapancart rpcart = new myrapancart(1);

	public raRekalkulacijaStanja() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void okPress() {
    if (err != null && !err.isDead()) err.hide();
		//if (ValidateUnos()) {
			//			raProcess.runChild(this, new Runnable() {
			//				public void run() {
			startUpdateStanje();
			//				}
			//			});
		//}
	}
  
  public boolean Validacija() {
    return ValidateUnos();
  }

	public boolean ValidateUnos() {
		if (rpcskl.jrfCSKL.getText().equals("")) {
			rpcskl.jrfCSKL.requestFocus();
			return false;		
    } else if (!ValGod()) {
			poljesnova.setText("");
			poljesnova.requestFocus();
			return false;
		} else {
			cskl = rpcskl.jrfCSKL.getText();
      if (!validateVrzal()) return false;
			god = poljesnova.getText();
			EnabDisab(false);
			return true;
		}
	}

	public boolean ValGod() {
		int pero = 0;
		try {
			pero = Integer.parseInt(poljesnova.getText());
		} catch (Exception e) {
			return false;
		}
		if (pero < 1900 || pero > 3900)
			return false;
		return true;
	}
  
  String vrzal;
  public boolean validateVrzal() {
    vrzal = null;
    DataSet qds = Sklad.getDataModule().getTempSet("VRZAL", Condition.equal("CSKL", cskl));
    qds.open();
    if (qds.getRowCount() == 1) {
      vrzal = qds.getString("VRZAL");
    } else if (qds.getRowCount() > 1) {
      throw new RuntimeException(
          "Postoji više slogova u tabeli skladište za cskl="+cskl);
    } else if (qds.getRowCount() == 0) {
      throw new RuntimeException(
          "Ne postoji slog u tabeli skladište za cskl="+cskl);
    }
    if (log.isDebugEnabled()) log.debug("vrzal=" + vrzal);    
    if (vrzal == null || vrzal.length() != 1 || "NVM".indexOf(vrzal) < 0) {
      rpcskl.jrfCSKL.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(),
          "Pogrešna vrsta zalihe ("+vrzal+") na skladištu "+cskl+"!", 
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  
	public void EnabDisab(boolean kako) {
		rpcskl.disabCSKL(kako);
		rpcart.EnabDisabS(kako);
		hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(
				poljesnova, kako);
		hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(
            bit, kako);
		//    hr.restart.util.raCommonClass.getraCommonClass().EnabDisabAll(okp,kako);
	}

	void jbInit() throws Exception {    	  
		rpcart.setMyAfterLookupOnNavigate(false);
		rpcart.setMode("DOH");
		rpcart.setnextFocusabile(poljesnova);
		rpcart.setBorder(null);
		xYLayout1.setWidth(645);
		xYLayout1.setHeight(145);
		jPanel1.setLayout(xYLayout1);
		jPanel1.setBorder(new javax.swing.border.EtchedBorder());
		rpcskl.setOverCaption(true);
		bit.setText("Provjeri samo kolièinu i vrijednost");
		bit.setHorizontalTextPosition(SwingConstants.LEADING);
		bit.setHorizontalAlignment(SwingConstants.TRAILING);
		bit.setSelected(true);
		poljesnova.setHorizontalAlignment(SwingConstants.CENTER);
		jPanel1.add(rpcskl, new XYConstraints(0, 0, -1, -1)); //(0, 0, -1, -1)
		jPanel1.add(rpcart, new XYConstraints(0, 40, -1, 75));
		jPanel1.add(tekst, new XYConstraints(15, 115, -1, -1)); //15, 50, -1,
		jPanel1.add(poljesnova, new XYConstraints(150, 115, 100, -1));
		jPanel1.add(bit, new XYConstraints(270, 115, 275, -1));
		this.setJPan(jPanel1);

	}

	public void cancel() {
		oslobodi();
		setVisible(false);
	}

	public void ESC_izlaz(KeyEvent e) {
		if (rpcskl.jrfCSKL.getText().equals(""))
			cancel();
		else {
			oslobodi();
		}
		e.consume();
	}

	public void oslobodi() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				EnabDisab(true);
				//rpcskl.Clear();
				//rpcart.Clear();
				rpcskl.jrfCSKL.requestFocus();
			}
		});
	}

	public void enableAllDataSetEvents(boolean kako) {
		tmpStanje.enableDataSetEvents(kako);
		//    tmpSkladStanjeArt.enableDataSetEvents(kako);
		//    tmpStdoki.enableDataSetEvents(kako);
		//    tmpStdoku.enableDataSetEvents(kako);
		//    tmpStmesklaUl.enableDataSetEvents(kako);
		//    tmpStmesklaIz.enableDataSetEvents(kako);

	}

	/*
	 * public void go_drugi() {
	 * 
	 * jProgressBar1.setValue(0); selectAllStanje();
	 * 
	 * jProgressBar2.setMaximum(1000); //
	 * jProgressBar2.setMaximum(tmpStdokuStanje.getRowCount()+ //
	 * tmpStdokiStanje.getRowCount()+ // tmpStanje.getRowCount());
	 * 
	 * InitDate(); jProgressBar1.setValue(0); clearAllStanje();
	 * jProgressBar1.setValue(0); kalkulStdoku(); jProgressBar1.setValue(0); //
	 * selectStdokiStanje(); // recalcStdokiStanje();
	 * 
	 * javax.swing.JOptionPane.showConfirmDialog(this,"Rekalkulacija stanja
	 * Završena !","Poruka",
	 * javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.DEFAULT_OPTION);
	 * jProgressBar1.setValue(0); jProgressBar2.setValue(0); oslobodi(); } int
	 * cart = 0;
	 */
	public void startUpdateStanje() {
		try {
			cart = Integer.parseInt(rpcart.jrfCART.getText());
		} catch (NumberFormatException ex) {
			cart = 0;
		}
		setMessage("Dohvat prethodnog stanja ...");
		selectAllStanje();    // uèitava kompletno stanje na skladištu 
    preCacheDocuments();   // kešira sve živo što je potrebno za rekalkulaciju
    tmpStanje.first();    
		int j = artList.length;
    
    // poruku baca na svakih 17 (ili manje) obraðenih artikala, jer se
    // u suprotnom previše vremena troši na puki repaint.
    int ev = Math.min(17, (int) Math.round(Math.sqrt(j)) + 1);
    
    setNewMessage("Obraðeno 0/" + j + " artikala");
    for (int i = 0; i < j; i++) {
      checkClosing(); 
      boolean isStanje = raVart.isStanje(artList[i]);
      int lastRow = tmpStanje.getRow();
      isNewArt = !ld.raLocate(tmpStanje, "CART", Integer.toString(artList[i]));
      
      // treba li artikl uopæe biti na stanju?
      if (!isStanje) {
      	// ako je od prije bio na stanju, makni ga
      	if (!isNewArt) tmpStanje.deleteRow();
      	continue;
      }
      
      // preCachedocuments je izmeðu ostalog napravio i kompletni popis artikala
      // koji se pojavljuju bilo na stanju, bilo u prometima. Ako neki od tih
      // artikala nedostaju na stanju, dodaje se novi slog.
      if (isNewArt) {
        tmpStanje.goToRow(lastRow);
        tmpStanje.insertRow(false);
        tmpStanje.setString("CSKL", cskl);
        tmpStanje.setString("GOD", god);
        tmpStanje.setInt("CART", artList[i]);
        tmpStanje.post();
        if (log.isDebugEnabled())
          log.debug("dodan slog na stanje, cart "+artList[i]);       
      }
			nullAllStanje(tmpStanje);
			try {
				updateStdoku(artList[i], tmpStanje);
				updateStmeu(artList[i], tmpStanje);
        
				findLastPrice(tmpStanje);        
				kalkulateSum(tmpStanje);
				
				updateStdoki(artList[i], tmpStanje);
				updateStmei(artList[i], tmpStanje);
				kalkulateSum(tmpStanje);
        kalkulateNC(tmpStanje);
        kalkulateZC(tmpStanje);
        updateStdoku4sklad(artList[i], tmpStanje);
        updateStdoki4sklad(artList[i], tmpStanje);
        

        if ((i+1) % ev == 1 || (i+1) == j)
          setMessage("Obraðeno " + (i+1) + "/" + j + " artikala");

			} catch (Exception ex) {
				ex.printStackTrace();
				raProcess.fail();
			}
		}
    // snimi kasnije, ne sada.
    /*try {
      setMessage("Snimanje ......");
		  raTransaction.saveChanges(tmpStanje);
		} catch (Exception ex) {
			ex.printStackTrace();
		}*/
	}
  
  void saveNewStanje() {
    raProcess.runChild(this.getWindow(), new Runnable() {
      public void run() {
        try {         
          tmpStanje.saveChanges();
        } catch (Exception ex) {
          ex.printStackTrace();
          raProcess.fail();
        }        
      }
    });
    if (raProcess.isFailed())
      JOptionPane.showMessageDialog(this.getWindow(),
          "Snimanje neuspješno !", "Greška", JOptionPane.ERROR_MESSAGE);
    else if (raProcess.isCompleted())
      JOptionPane.showMessageDialog(this.getWindow(),
          "Snimanje uspješno završeno!", "Informacija", JOptionPane.INFORMATION_MESSAGE);
  }

	public void afterOKPress() {
	  boolean imp = bit.isSelected();
    if (raProcess.isCompleted()) {
		  DataSetComparator dc = new DataSetComparator(true,false);
      DataSet result = !imp ? dc.compare(oldStanje, tmpStanje, "CART") :
            dc.compare(oldStanje, tmpStanje, 
                  new String[] {"CART"}, new String[] {"KOL", "VRI"});
      if (result.rowCount() == 0) {
        JOptionPane.showMessageDialog(this.getWindow(),
            "Trenutaèno stanje je ispravno !", "Nema greške", JOptionPane.INFORMATION_MESSAGE);        
      } else {
        err = new dlgErrors(this.getWindow(), "Razlike izmeðu tablice stanja i rekalkulacije", false);
        JButton popr = new JButton("Popravi!");
        popr.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {            
            saveNewStanje();
          }
        });
        if (!imp) err.setActionButton(popr);
        err.setData(result, dc.getOtherColumns());
        err.setColumnWidth(50);
        err.setSize(640, 400);        
        for (result.first(); result.inBounds(); result.next())
          err.addError(result.getString(dc.getMainColumn()), result);
        err.show();
      }
    } else if (raProcess.isFailed())
      JOptionPane.showMessageDialog(this.getWindow(),
          "Rekalkulacija stanja neuspješna !", "Greška", JOptionPane.ERROR_MESSAGE);        
		oslobodi();
	}

  // dohvaæa stanje artikla ukoliko je izabran, ili kompletno stanje na skladištu. 
	public void selectAllStanje() {
		if (cart != 0) {

			tmpStanje = hr.restart.util.Util.getNewQueryDataSet(
					"SELECT * FROM STANJE WHERE CSKL='" + cskl
							+ "' AND GOD='" + god + "' AND CART=" + cart, true);
		} else {
			tmpStanje = hr.restart.util.Util.getNewQueryDataSet(
					"SELECT * FROM STANJE WHERE CSKL='" + cskl
							+ "' AND GOD='" + god + "'", true);
      
		}
    
    // zapamti trenutaèno stanje
		oldStanje = new StorageDataSet();
    oldStanje.setColumns(tmpStanje.cloneColumns());
    oldStanje.open();
    for (tmpStanje.first(); tmpStanje.inBounds(); tmpStanje.next()) {
      oldStanje.insertRow(false);
      tmpStanje.copyTo(oldStanje);
      oldStanje.post();
    }    
	}

  // Tomo: èemu služi ova metoda, tj. zašto nije u klasi TypeDoc?
	public String getSkladDocs(boolean ulaz) {
		VarStr var = new VarStr();
		for (int i = 0; i < TypeDoc.araj_docs.length; i++) {
			if (TypeDoc.getTypeDoc().isDocSklad(TypeDoc.araj_docs[i])) {
				if ((ulaz && TypeDoc.getTypeDoc().isDocStdoku(
						TypeDoc.araj_docs[i]))
						|| (!ulaz && TypeDoc.getTypeDoc().isDocStdoki(
								TypeDoc.araj_docs[i]))) {
					var = var.append("'").append(TypeDoc.araj_docs[i]).append(
							"',");
				}
			}
		}
		var = var.chopRight(1);
		return var.toString();
	}
  
  // Keševi podataka potrebnih za rekalkulaciju: sume prometa po artiklima,
  // posebno za izlaze, meðuskladišnice ulazne i izlazne, te ulaze posebno
  // za poèetno stanje, kalkulacije i ostale ulaze.
  // Potrošnja memorije mislim da nije katastrofièna jer je potreban samo
  // jedan slog (deset kolona) po artiklu za svaki keš.
  QueryDataSet stdoki, stdoku,stdoki4sklad, stdoku4sklad, stdokuPs, stdokuSklart, stmeu, stmei;  
  private void preCacheDocuments() {
    String dod =  (cart != 0) ? " and cart="+cart : "";
    setMessage("Dohvat poèetnog stanja  ...");
    stdokuPs = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT cart, max(vrdok) as svrdok,sum(kol) as skol,sum(inab) as sinab,sum(imar) as simar,sum(ipor) as sipor,"
            + "sum(diopormar) as sdiopormar, sum(dioporpor) as sdioporpor, sum(porav) as sporav,"
            + "sum(izad) as sizad from stdoku where cskl='" + cskl + "' and god='" + god + "' " + dod
            + " and vrdok='PST' group by cart order by cart", false);
    openScratchDataSet(stdokuPs);    
    if (log.isDebugEnabled()) 
      log.debug(stdokuPs.getQuery().getQueryString() + " " + stdokuPs.getRowCount());
    
    setMessage("Dohvat ulaznih dokumenata ...");    
    stdoku = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT cart, max(vrdok) as svrdok,sum(kol) as skol,sum(inab) as sinab,sum(imar) as simar,sum(ipor) as sipor,"
            + "sum(diopormar) as sdiopormar, sum(dioporpor) as sdioporpor, sum(porav) as sporav,"
            + "sum(izad) as sizad from stdoku where cskl='" + cskl + "' and god='" + god + "' " + dod
            + " and vrdok in ('PRE','PRK','INV','POR','PTE') group by cart order by cart", false);
    openScratchDataSet(stdoku);
    if (log.isDebugEnabled()) 
      log.debug(stdoku.getQuery().getQueryString() + " " + stdoku.getRowCount());
    
    setMessage("Dohvat kalkulacija ...");
    stdokuSklart = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT cart, max(vrdok) as svrdok,sum(kol) as skol,sum(inab) as sinab,sum(imar) as simar,sum(ipor) as sipor,"
            + "sum(diopormar) as sdiopormar, sum(dioporpor) as sdioporpor, sum(porav) as sporav,"
            + "sum(izad) as sizad from stdoku where csklart='" + cskl + "' and god='" + god + "' " + dod
            + " and vrdok='KAL' group by cart order by cart", false);
    openScratchDataSet(stdokuSklart);
    if (log.isDebugEnabled()) 
      log.debug(stdokuSklart.getQuery().getQueryString() + " " + stdokuSklart.getRowCount());
    
    setMessage("Dohvat izlaznih dokumenata ...");
    stdoki =  hr.restart.util.Util.getNewQueryDataSet(
        "SELECT cart, sum(kol) as skol,sum(inab) as sinab,sum(imar) as simar,sum(ipor) as sipor,"
            + "sum(iraz) as siraz from stdoki where cskl='" + cskl + "' and god='" + god + "' " + dod 
            + " and vrdok in (" + getSkladDocs(false) + ") group by cart order by cart", false);
    openScratchDataSet(stdoki);
    if (log.isDebugEnabled()) 
      log.debug(stdoki.getQuery().getQueryString() + " " + stdoki.getRowCount());
    
    
    
    
    
    setMessage("Dohvat ulaznih meðuskladišnica ...");
    stmeu = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT cart, sum(kol) as skol,sum(inabul) as sinab, sum(imarul) as simar, sum(iporul) as sipor,"
            + "sum(diopormar) as sdiopormar, sum(dioporpor) as sdioporpor,"
            + "sum(porav) as sporav, sum(zadrazul) as sizad from stmeskla where csklul='"+cskl
            + "' and god='" + god + "' " +dod+" and vrdok in ('MES','MEU') group by cart order by cart", false);
    openScratchDataSet(stmeu);
    if (log.isDebugEnabled()) 
      log.debug(stmeu.getQuery().getQueryString() + " " + stmeu.getRowCount());
    
    setMessage("Dohvat izlaznih meðuskladišnica ...");
    stmei = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT cart, sum(kol) as skol,sum(inabiz) as sinab, sum(imariz) as simar,"
            + "sum(iporiz) as sipor, sum(zadraziz) as siraz from stmeskla where cskliz='"+cskl
            + "' and god='" + god + "' "+dod+" and vrdok in ('MES','MEI') group by cart order by cart", false);
    openScratchDataSet(stmei);
    if (log.isDebugEnabled()) 
      log.debug(stmei.getQuery().getQueryString() + " " + stmei.getRowCount());

    stdoki4sklad =  hr.restart.util.Util.getNewQueryDataSet(
            "SELECT cart, sum(kol) as skol,sum(inab) as sinab,sum(imar) as simar,sum(ipor) as sipor,"
                + "sum(iraz) as siraz from stdoki where cskl='" + cskl + "' and god='" + god + "' " + dod 
                + " and vrdok in ('DOS') and (veza is null or veza='') group by cart order by cart", false); // DEBUG ? 
    openScratchDataSet(stdoki4sklad);
    if (log.isDebugEnabled()) 
    	log.debug(stdoki4sklad.getQuery().getQueryString() + " " + stdoki4sklad.getRowCount());
    
    stdoku4sklad =  hr.restart.util.Util.getNewQueryDataSet(
    	                "SELECT cart, max(vrdok) as svrdok,sum(kol) as skol,sum(inab) as sinab,sum(imar) as simar,sum(ipor) as sipor,"
    	                + "sum(diopormar) as sdiopormar, sum(dioporpor) as sdioporpor, sum(porav) as sporav,"
    	                + "sum(izad) as sizad from stdoku where cskl='" + cskl + "' and god='" + god + "' " + dod
    	                + " and vrdok in ('PRI') and status not in ('P') group by cart order by cart", false);
    openScratchDataSet(stdoku4sklad);
    System.out.println(stdoku4sklad.getQuery().getQueryString());    
    if (log.isDebugEnabled()) 
    	log.debug(stdoku4sklad.getQuery().getQueryString() + " " + stdoku4sklad.getRowCount());
    // formira popis svih artikala bilo u stanju, bilo u prometima.
    prepareItemList();
    // kešira zadnje kalkulacije za svaki artikl po datumu te po polju TKAL u stanju
    prepareLastInputCache();
  }
  
  // dodaje sve CART-ove iz nekog dataseta u set
  private void populateItems(DataSet ds, Set items) {
    checkClosing();
    for (ds.first(); ds.inBounds(); ds.next())
      items.add(new Integer(ds.getInt("CART")));
  }
  
  // metoda formira popis artikala koji se pojavljuju bilo na stanju, bilo u prometima.
  // Straightforward: prolazi kroz sve spomenute datasetove i dodaje CART u java.util.Set.
  int[] artList;
  private void prepareItemList() {
    Set carts = new TreeSet();
    populateItems(tmpStanje, carts);
    populateItems(stdoku, carts);
    populateItems(stdoku4sklad, carts);
    populateItems(stdokuPs, carts);
    populateItems(stdokuSklart, carts);
    populateItems(stdoki, carts);
    populateItems(stdoki4sklad, carts);    
    populateItems(stmeu, carts);
    populateItems(stmei, carts);
    Iterator it = carts.iterator();
    artList = new int[carts.size()];    
    for (int i = 0; it.hasNext(); i++)
      artList[i] = ((Integer) it.next()).intValue();
    if (log.isDebugEnabled()) {
      log.debug("Ukupno artikala "+carts.size());
      log.debug("Artikala na stanju "+tmpStanje.rowCount());
    }
  }
  
  // helper metoda koja dodaje vrijednost 'val' vec postojeæoj vrijednosti
  // BigDecimal kolone 'col' u DataSetu 'stanje'.
  void updateColumn(DataSet stanje, String col, BigDecimal val) {
    stanje.setBigDecimal(col, stanje.getBigDecimal(col).add(val));
  }
  
  // helper metoda slièna ovoj gore, ali radi sa dvije BigDecimal vrijednosti,
  // onih u kolonama 'col1' i 'col2' DataSeta src.
  void updateColumn(DataSet stanje, String col, DataSet src, String col1, String col2) {
    stanje.setBigDecimal(col, stanje.getBigDecimal(col).
        add(src.getBigDecimal(col1)).add(src.getBigDecimal(col2)));
  }

	// helper metoda zajednièka za izlaze i izlazne meðuskladišnice,
  // ažurira izlazne kolone na stanju s vrijednostima iz keša, za tekuæi artikl.
  void updateIzlazCommon(DataSet stanje, DataSet src) {
//  	if (!src.getString("VRDOK").equalsIgnoreCase("DOS")) {
    updateColumn(stanje, "KOLIZ", src.getBigDecimal("SKOL"));
    updateColumn(stanje, "NABIZ", src.getBigDecimal("SINAB"));
    updateColumn(stanje, "MARIZ", src.getBigDecimal("SIMAR"));
    updateColumn(stanje, "PORIZ", src.getBigDecimal("SIPOR"));
    updateColumn(stanje, "VIZ", src.getBigDecimal("SIRAZ"));
//  	}
    updateSkladPartStanjeIzlaz(stanje,src);    
    
  }
  
  // helper metoda zajednièka za ulaze i ulazne meðuskladišnice,
  // ažurira ulazne kolone na stanju s vrijednostima iz keša, za tekuæi artikl.
  void updateUlazCommon(DataSet stanje, DataSet src) {
//  	if (!src.getString("VRDOK").equalsIgnoreCase("PRI")) {
    updateColumn(stanje, "KOLUL", src.getBigDecimal("SKOL"));
    updateColumn(stanje, "NABUL", src.getBigDecimal("SINAB"));
    updateColumn(stanje, "MARUL", src, "SIMAR", "SDIOPORMAR");
    updateColumn(stanje, "PORUL", src, "SIPOR", "SDIOPORPOR");
    updateColumn(stanje, "VUL", src, "SIZAD", "SPORAV");
//  	}
    updateSkladPartStanjeUlaz(stanje,src);    
  }
  
  // metoda updateStdoki sada je nešto kraæa nego prije, toènije
  // ima samo dva reda: ukoliko za tekuæi artikl postoji nešto
  // u kešu izlaznih dokumenata, ažurira izlazne kolone na stanju.
  public void updateStdoki(int art, DataSet stanje) {
    if (ld.raLocate(stdoki, "CART", Integer.toString(art)))
      updateIzlazCommon(stanje, stdoki);
  }

  public void updateStdoki4sklad(int art, DataSet stanje) {
    if (ld.raLocate(stdoki4sklad, "CART", Integer.toString(art)))
    	updateSkladPartStanjeIzlaz(stanje, stdoki4sklad);
	}
  public void updateStdoku4sklad(int art, DataSet stanje) {
    if (ld.raLocate(stdoku4sklad, "CART", Integer.toString(art)))
    	updateSkladPartStanjeUlaz(stanje, stdoku4sklad);
	}  
  
  public void updateSkladPartStanjeIzlaz(DataSet stanje, DataSet src) {
//  	if ("ROT".equalsIgnoreCase(src.getString("VRDOK")) ||
//  	  		"OTP".equalsIgnoreCase(src.getString("VRDOK"))){
//  		if (src.getString("VEZA").indexOf("DOS")>=0){
//  			return;
//  		}
//  	}
	stanje.setBigDecimal("KOLSKLADIZ",stanje.getBigDecimal("KOLSKLADIZ").add( 
			src.getBigDecimal("SKOL")));
  	stanje.setBigDecimal("KOLSKLAD",
			stanje.getBigDecimal("KOLSKLADUL").
			subtract(stanje.getBigDecimal("KOLSKLADIZ")));  	  		
  }
  public void updateSkladPartStanjeUlaz(DataSet stanje, DataSet src) {
//  	if ("KAL".equalsIgnoreCase(src.getString("VRDOK"))){
//  		if (src.getString("VEZA").indexOf("PRI")>=0){
//  			return;
//  		}
//  	}  	

		stanje.setBigDecimal("KOLSKLADUL",stanje.getBigDecimal("KOLSKLADUL").add( 
				src.getBigDecimal("SKOL")));
		stanje.setBigDecimal("KOLSKLAD",
				stanje.getBigDecimal("KOLSKLADUL").
				subtract(stanje.getBigDecimal("KOLSKLADIZ")));  	  		
  }  
  
  // metoda updateStdoku nešto je složenija, jer za ulaze imamo
  // tri razlièita keša: obièni ulazi, poèetno stanje i
  // kalkulacije s više skladišta.
	public void updateStdoku(int art, DataSet stanje) {		
    if (ld.raLocate(stdokuPs, "CART", Integer.toString(art))) {
      updateUlazCommon(stanje, stdokuPs);
			
      // za poèetno stanje osim ulaznih polja, postavi i polja poèetnog stanja.
  		stanje.setBigDecimal("KOLPS", stdokuPs.getBigDecimal("SKOL"));
  		stanje.setBigDecimal("KOLSKLADPS", stdokuPs.getBigDecimal("SKOL"));  		
	  	stanje.setBigDecimal("NABPS", stdokuPs.getBigDecimal("SINAB"));
			stanje.setBigDecimal("MARPS", stdokuPs.getBigDecimal("SIMAR"));
			stanje.setBigDecimal("PORPS", stdokuPs.getBigDecimal("SIPOR"));
			stanje.setBigDecimal("VPS", stdokuPs.getBigDecimal("SIZAD"));			
		}
    
    // ostala sva keša su trivijalna: ako tekuæi artikl postoji u danom kešu
    // ažuriraj ulazne kolone stanja za taj artikl.
    if (ld.raLocate(stdoku, "CART", Integer.toString(art)))
      updateUlazCommon(stanje, stdoku);
    
    if (ld.raLocate(stdokuSklart, "CART", Integer.toString(art)))
      updateUlazCommon(stanje, stdokuSklart);    
	}

  // no comment.
	public void updateStmeu(int art, DataSet stanje) {
    if (ld.raLocate(stmeu, "CART", Integer.toString(art)))
      updateUlazCommon(stanje, stmeu);
  }
  
  // ditto.
  public void updateStmei(int art, DataSet stanje) {
    if (ld.raLocate(stmei, "CART", Integer.toString(art)))
      updateIzlazCommon(stanje, stmei);
	}

  // malo reformatirano, èisto estetike radi. :)
	public void nullAllStanje(DataSet stanje) {
    String[] cols = {
        "KOLPS", "NABPS", "MARPS", "PORPS", "VPS", 
        "KOLUL", "NABUL", "MARUL", "PORUL", "VUL",
        "KOLIZ", "NABIZ", "MARIZ", "PORIZ", "VIZ",
        /*"NC", "VC", "MC", "ZC",*/ "KOLSKLADUL","KOLSKLADIZ","KOLSKLAD"
    };
    for (int i = 0; i < cols.length; i++)    
		  stanje.setBigDecimal(cols[i], Nula);
	}  

  // ova metoda praktièki nije više potrebna, ali nek stoji za sad.
	private boolean isGoodQds(DataSet stanje, DataSet qds) {
		if (qds == null || qds.getRowCount() == 0)
			return false;
		/*if (!stanje.getString("CSKL").equalsIgnoreCase(qds.getString("CSKL")))
			return false;
		if (!stanje.getString("GOD").equalsIgnoreCase(qds.getString("GOD")))
			return false;*/
		if (stanje.getInt("CART") != qds.getInt("CART"))
			return false;
		return true;
	}

  // metoda koja priprema keševe zadnjih kalkulacija za svaki artikl,
  // posebno za ulazne kalkulacije, posebno za ulaze meðuskladišnica.
  // Ujedno radi i keš svih dokumenata koji odgovaraju kljuèu TKAL
  // na svim artiklima dohvaæenog stanja.
  StorageDataSet lastStdoku, lastStmeu, tkalStdoku, tkalStmeu;
  void prepareLastInputCache() {
    String dod =  (cart != 0) ? " and cart="+cart : "";
    setNewMessage("Priprema zadnjih kalkulacija (meðuskladišnice) ...");
    QueryDataSet dsmeu = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT meskla.datdok,stmeskla.cart,stmeskla.nc,stmeskla.vc,stmeskla.mc,stmeskla.csklul,stmeskla.cskliz,stmeskla.vrdok," +
        "stmeskla.god,stmeskla.brdok,stmeskla.rbsid FROM meskla,stmeskla WHERE meskla.csklul = stmeskla.csklul " +
        "AND meskla.cskliz = stmeskla.cskliz AND meskla.vrdok = stmeskla.vrdok " +
        "AND meskla.god = stmeskla.god AND meskla.brdok = stmeskla.brdok " + dod +
        " AND stmeskla.csklul='" + cskl + "' AND stmeskla.vrdok in ('MES','MEU') AND stmeskla.god='" + god + "' order by 2,1", false);
    openScratchDataSet(dsmeu);
    dsmeu.setTableName("stmeskla");
     
    lastStmeu = new StorageDataSet();
    compactLastInputCache(dsmeu, lastStmeu);
    
    setMessage("Priprema zadnjih kalkulacija (ulazi) ...");
    QueryDataSet dsul = hr.restart.util.Util.getNewQueryDataSet(
        "SELECT doku.datdok,stdoku.cart,stdoku.nc,stdoku.vc,stdoku.mc,stdoku.cskl,stdoku.vrdok,stdoku.god,stdoku.brdok,stdoku.rbsid " +
        "FROM doku,stdoku WHERE doku.cskl = stdoku.cskl AND doku.vrdok = stdoku.vrdok " +
        "AND doku.god = stdoku.god AND doku.brdok = stdoku.brdok " + dod +
        " AND stdoku.cskl='" + cskl + "' AND stdoku.god='" + god + "' order by 2,1", false);
    openScratchDataSet(dsul);
    dsul.setTableName("stdoki");

    lastStdoku = new StorageDataSet();
    compactLastInputCache(dsul, lastStdoku);
    
    setMessage("Priprema zadnjih kalkulacija (stanje) ...");
    Set tkals = new HashSet();
    for (tmpStanje.first(); tmpStanje.inBounds(); tmpStanje.next())
      tkals.add(tmpStanje.getString("TKAL"));
    
    tkalStmeu = new StorageDataSet();
    fillTkalFromTable(dsmeu, tkalStmeu, tkals);
    
    tkalStdoku = new StorageDataSet();
    fillTkalFromTable(dsul, tkalStdoku, tkals);
  }
  
  // DataSet full sadrži sve slogove ulaza za sve artikle. Ova metoda za svaki
  // artikl ostavlja samo jedan slog, onaj sa zadnjim datumom, i rezultat sprema
  // u DataSet compact. Pretpostavlja da su slogovi sortirani najprije po
  // CART-u, a onda po datumu, u rastuæem poretku.
  void compactLastInputCache(StorageDataSet full, StorageDataSet compact) {    
    compact.setColumns(full.cloneColumns());    
    compact.open();
    int lastCart = -17;
    for (full.first(); full.inBounds(); full.next()) {
      checkClosing();
      if (full.getInt("CART") != lastCart) {
        lastCart = full.getInt("CART");
        compact.insertRow(false);
      }
      full.copyTo(compact);
      compact.post();      
    }
  }
  
  // DataSet full opet sadrži sve slogove ulaza za sve artikle. Metoda puni u DataSet
  // tkals samo one slogove koji odgovoraju jednom od kljuèeva TKAL iz Seta stanjeTkals.
  void fillTkalFromTable(StorageDataSet full, StorageDataSet tkals, Set stanjeTkals) {
    tkals.setColumns(full.cloneColumns());
    tkals.addColumn(tmpStanje.getColumn("TKAL").cloneColumn());
    tkals.open();
    for (full.first(); full.inBounds(); full.next()) {
      checkClosing();
      String hkey = rCD.getKey(full);
      if (stanjeTkals.contains(hkey)) {
        tkals.insertRow(false);
        full.copyTo(tkals);
        tkals.setString("TKAL", hkey);
        tkals.post();
      }
    }    
  }

  // metoda koja popunjava cijene na stanju s cijenama na zadnjoj kalkulaciji,
  // osim ako je timeusporedi non-null i po vremenu je iza zadnje kalukacije
  // naðene po ovom principu. (To je moguæe ako kljuè TKAL na stanju pokazuje
  // na neku kalkulaciju koja je iza zadnje naðene u prometima. Nije mi baš
  // jasno kada i u kojim okolnostima se to može dogoditi.)
	public void LastofTheLast(DataSet stanje, java.sql.Timestamp timeusporedi) {
    
    // pogledaj  postoji li tekuæi artikl u keševima ulaza i ulaznih meðuskladišnica.
	  boolean isPrimke = ld.raLocate(lastStdoku, "CART", Integer.toString(stanje.getInt("CART")));
    boolean isMeskla = ld.raLocate(lastStmeu, "CART", Integer.toString(stanje.getInt("CART")));

		//ST.prn(primke);
		//ST.prn(meskla);
		String who = "";

    // ovaj dio je malo èudno iskodiran, ali nisam ga htio mijenjati...
    // u principu, ako nema zadnje kalkulacije u meðuskladišnicama,
    // pretpostavi da je ima u primkama i obratno. Ako nema ni tamo
    // onda se cijene niti ne mogu postaviti.
		if (!isMeskla) {
			who = "P";
		}
		if (!isPrimke) {
			who = "M";
		}
		if (!isMeskla && !isPrimke) {
			who = "G";
		}

		// ako ima i primki i meðuskladišnica, pogledaj koja je zadnja po datumu.
		if (who.equalsIgnoreCase("")) {
			if (usporedbaDatuma(lastStdoku.getTimestamp("DATDOK"), lastStmeu
					.getTimestamp("DATDOK")) < 0) {
				who = "M";
			} else if (usporedbaDatuma(lastStdoku.getTimestamp("DATDOK"), lastStmeu
					.getTimestamp("DATDOK")) > 0) {
				who = "P";
			} else {
				// po slobodnoj procjeni uzeti cu da je primka zapravo zadnja
				// kalkulacija
				who = "P";
				/*
				 * System.out.println("Zbunjen sam za ovaj slucaj ->
				 * (cskl-god-cart) -> " + stanje.getString("CSKL")+"-"+
				 * stanje.getString("GOD")+"-"+ stanje.getInt("CART")); if
				 * (meskla.getBigDecimal("MC").compareTo(primke.getBigDecimal("MC"))
				 * !=0 ||
				 * meskla.getBigDecimal("VC").compareTo(primke.getBigDecimal("VC"))
				 * !=0){
				 * 
				 * QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet(
				 * "select
				 * doki.datdok,nc,vc,mc,stdoki.cskl,stdoki.vrdok,stdoki.god,stdoki.brdok,stdoki.rbsid
				 * from doki,stdoki "+ "WHERE doki.cskl = stdoki.cskl "+ "AND
				 * doki.vrdok = stdoki.vrdok "+ "AND doki.god = stdoki.god "+
				 * "AND doki.brdok = stdoki.brdok "+ "AND
				 * stdoki.cskl='"+stanje.getString("CSKL")+"' "+ "AND
				 * stdoki.god='"+stanje.getString("GOD")+"' "+ "AND
				 * stdoki.cart="+stanje.getInt("CART")+" "+ "and datdok >'"+"'
				 * order by datdok",true); } else { who="P"; }
				 */
			}
		}
		//System.out.println("who 2 = "+who);

    // ukoliko su cijene veæ postavljene uz pomoæ kljuèa TKAL na stanju,
    // pogledaj je li taj datum stvarno zadnji. Ako nije, onda ipak uzmi
    // zadnju kalkulaciju po datumu.
		if (timeusporedi != null) {
			if (who.equalsIgnoreCase("M")
					&& usporedbaDatuma(lastStmeu.getTimestamp("DATDOK"),
							timeusporedi) < 0) {
				who = "D";
			}
			if (who.equalsIgnoreCase("P")
					&& usporedbaDatuma(lastStdoku.getTimestamp("DATDOK"),
							timeusporedi) < 0) {
				who = "D";
			}
		}


		// postavi cijene i usput ažuriraj polje TKAL na stanju.
		if (who.equalsIgnoreCase("M")) {
			stanje.setBigDecimal("VC", lastStmeu.getBigDecimal("VC"));
			stanje.setBigDecimal("MC", lastStmeu.getBigDecimal("MC"));
			stanje.setString("TKAL", rCD.getKey(lastStmeu, new String[] {
					"cskliz", "csklul", "vrdok", "god", "brdok", "rbsid" },
					"stmeskla"));
			stanje.setTimestamp("DATZK", lastStmeu.getTimestamp("DATDOK"));
		} else if (who.equalsIgnoreCase("P")) {
			stanje.setBigDecimal("VC", lastStdoku.getBigDecimal("VC"));
			stanje.setBigDecimal("MC", lastStdoku.getBigDecimal("MC"));
			stanje.setString("TKAL", rCD.getKey(lastStdoku, new String[] { "cskl",
					"vrdok", "god", "brdok", "rbsid" }, "stdoku"));
			stanje.setTimestamp("DATZK", lastStdoku.getTimestamp("DATDOK"));
		}    
	}

  // usporeðuje dva timestampa ali ne gleda na vrijeme, nego samo cijeli dan.
	private int usporedbaDatuma(java.sql.Timestamp prvi,
			java.sql.Timestamp drugi) {
		Calendar cal_prvi = Calendar.getInstance();
		Calendar cal_drugi = Calendar.getInstance();
		cal_prvi.setTime(prvi);
		cal_drugi.setTime(drugi);
		if (cal_prvi.get(Calendar.YEAR) != cal_drugi.get(Calendar.YEAR)) {
			if (cal_prvi.get(Calendar.YEAR) < cal_drugi.get(Calendar.YEAR)) {
				return -1; //
			} else {
				return 1;
			}
		}
		if (cal_prvi.get(Calendar.MONTH) != cal_drugi.get(Calendar.MONTH)) {
			if (cal_prvi.get(Calendar.MONTH) < cal_drugi.get(Calendar.MONTH)) {
				return -1; //
			} else {
				return 1;
			}
		}
		if (cal_prvi.get(Calendar.DATE) != cal_drugi.get(Calendar.DATE)) {
			if (cal_prvi.get(Calendar.DATE) < cal_drugi.get(Calendar.DATE)) {
				return -1; //
			} else {
				return 1;
			}
		}
		return 0;
	}

  // traži zadnje cijene artikla, ili preko kljuèa TKAL u tablici stanje,
  // ili ruèno preko zadnje kalkulacije po datumu. Svi podaci potrebni
  // za rad su keširani tako da radi vrlo brzo (premda se može još
  // optimizirati no ne vidim svrhe...)
	public void findLastPrice(DataSet stanje) {
		String tkal = stanje.getString("TKAL");

    // ako nema kluèa TKAL ili je isti neispravan, ostaje jedino moguænost
    // traženja zadnje kalkulacije po datumu.
		if (tkal == null || tkal.equalsIgnoreCase("")) {
			LastofTheLast(stanje, null);
			return;
		}

		HashMap hm = rCD.getHashMapKey(tkal);
		if (hm == null) {
			LastofTheLast(stanje, null);
			return;
		}
		
		DataSet qds = null;
		if (hm.containsKey("CSKL")) {
			if (ld.raLocate(tkalStdoku, "TKAL", tkal))
        qds = tkalStdoku;
		} else {
      if (ld.raLocate(tkalStmeu, "TKAL", tkal))
        qds = tkalStmeu;			
		}
    
    // ako je kljuè TKAL dobar i pokazuje na neki slog bilo u kešu ulaza ili
    // ulaznih meðuskladišnica, popuni cijene iz tog sloga, ali svejedno
    // provjeri i zadnju kalkulaciju. Hmm, nekako mi se èini da tu ima
    // redundancije...
		if (isGoodQds(stanje, qds)) {
			//      stanje.setBigDecimal("NC",qds.getBigDecimal("NC"));
			stanje.setBigDecimal("VC", qds.getBigDecimal("VC"));
			stanje.setBigDecimal("MC", qds.getBigDecimal("MC"));
  		stanje.setTimestamp("DATZK", qds.getTimestamp("DATDOK"));
	
			LastofTheLast(stanje, stanje.getTimestamp("DATZK"));
		} else {
      // inaèe, opet ostaje samo potraga za zadnjom kalkulacijom...
			LastofTheLast(stanje, null);
		}
	}

  // ažurira ZC u ovisnosti od vrste zalihe skladišta.
	public void kalkulateZC(DataSet stanje) {
    if (log.isDebugEnabled()) 
      log.debug("KOL =" + stanje.getBigDecimal("KOL"));
		if (vrzal.equalsIgnoreCase("N")) {
			if (stanje.getBigDecimal("KOL").abs().doubleValue() < 0.999 || 
			    stanje.getBigDecimal("KOL").signum() != stanje.getBigDecimal("VRI").signum()) {
			  if (isNewArt) {
				if (stanje.getBigDecimal("KOLUL").compareTo(Nula) == 0) {				
				  stanje.setBigDecimal("ZC", Nula);
				} else  {
					stanje.setBigDecimal("ZC", stanje.getBigDecimal("NABUL").divide(
							stanje.getBigDecimal("KOLUL"), 2,
							BigDecimal.ROUND_HALF_UP));
				}
			  } else stanje.setBigDecimal("ZC", stanje.getBigDecimal("NC"));
			} else {
				stanje.setBigDecimal("ZC", stanje.getBigDecimal("VRI").divide(
						stanje.getBigDecimal("KOL"), 2,
						BigDecimal.ROUND_HALF_UP));
			}
		} else if (vrzal.equalsIgnoreCase("V")) {
			stanje.setBigDecimal("ZC", stanje.getBigDecimal("VC"));
		} else if (vrzal.equalsIgnoreCase("M")) {
			stanje.setBigDecimal("ZC", stanje.getBigDecimal("MC"));
		} else {
			throw new RuntimeException("Neispravna vrsta zaliha " + vrzal
					+ " za skladište =" + stanje.getString("CSKL"));
		}
	}

	public void kalkulateNC(DataSet stanje) {
		BigDecimal bd = Aus.zero2;
		if (stanje.getBigDecimal("KOL").abs().doubleValue() > 0.999 && 
		    stanje.getBigDecimal("KOL").signum() == stanje.getBigDecimal("NABUL").subtract(
                stanje.getBigDecimal("NABIZ")).signum()) {
			bd = stanje.getBigDecimal("NABUL").subtract(
					stanje.getBigDecimal("NABIZ"));
			bd = bd.divide(stanje.getBigDecimal("KOL"), 2,
					BigDecimal.ROUND_HALF_UP);
			stanje.setBigDecimal("NC", bd);
/*			
      System.out.println(stanje.getBigDecimal("NABUL"));
      System.out.println(stanje.getBigDecimal("NABIZ"));
      System.out.println(stanje);
*/      
		} else if (stanje.getBigDecimal("KOLUL").abs().doubleValue() > 0.999 
		    && (isNewArt || !vrzal.equalsIgnoreCase("N") ||
		        stanje.getBigDecimal("NC").signum() == 0)) {
			bd = stanje.getBigDecimal("NABUL");
			bd = bd.divide(stanje.getBigDecimal("KOLUL"), 2,
					BigDecimal.ROUND_HALF_UP);
			stanje.setBigDecimal("NC", bd);
		}
	}

	public void kalkulateSum(DataSet stanje) {
		stanje.setBigDecimal("KOL", stanje.getBigDecimal("KOLUL").subtract(
				stanje.getBigDecimal("KOLIZ")));
		stanje.setBigDecimal("VRI", stanje.getBigDecimal("VUL").subtract(
				stanje.getBigDecimal("VIZ")));
	}

	public void InitDate() {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.clear();
		cal.set(Integer.parseInt(god), cal.JANUARY, 1, 0, 0, 0);
		datDokumenta = cal.getTime();
	}

	public boolean runFirstESC() {
		return true;
	}

	public void firstESC() {
		cancel();
	}

	public void componentShow() {
		rpcskl.Clear();
		poljesnova.setText(hr.restart.util.Valid.getValid().findYear());
		rpcskl.jrfCSKL.requestFocus();
	}

	public boolean isIspis() {
		return false;
	}

	public boolean ispisNow() {
		return false;
	}
}