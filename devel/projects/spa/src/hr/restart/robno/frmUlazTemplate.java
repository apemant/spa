/****license*****************************************************************
**   file: frmUlazTemplate.java
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

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Stdoku;
import hr.restart.baza.VTZtr;
import hr.restart.baza.dM;
import hr.restart.sisfun.dlgErrors;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.raSaldaKonti;
import hr.restart.swing.JraButton;
import hr.restart.util.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>
 * Title: Robno poslovanje
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2000
 * </p>
 * <p>
 * Company: REST-ART
 * </p>
 * 
 * @author REST-ART development team
 * @version 1.0
 */

public class frmUlazTemplate extends raMasterDetail {
	hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);

	_Main main;

	//  public QueryDataSet qDSZT; //(dataset koji ima vrijednosti odabrane u
	// dlgDodZT)
	char cMode;

	QueryDataSet stanjeSet = new QueryDataSet();

	raDateUtil rdu = raDateUtil.getraDateUtil();

	java.math.BigDecimal oldVC = main.nul;

	java.math.BigDecimal oldMC = main.nul;

	java.math.BigDecimal oldKOL = main.nul;

	java.math.BigDecimal oldNAB = main.nul;

	java.math.BigDecimal oldZAD = main.nul;

	java.math.BigDecimal oldPOR = main.nul;

	java.math.BigDecimal oldMAR = main.nul;

	java.math.BigDecimal oldPORAV = main.nul;

	java.math.BigDecimal oldPORAVPOR = main.nul;

	java.math.BigDecimal oldPORAVMAR = main.nul;
	
	int oldCPAR = -1;
	int oldCART = -1;
	String oldBRRAC = "";

	hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

	hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();

	hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();

	raControlDocs rCD = new raControlDocs(); // dodao /.V radi provjera datuma i

	public TypeDoc TD = TypeDoc.getTypeDoc();
	
	lookupData lD = lookupData.getlookupData();
	// kalkulacija i bla bla

	Rbr rbr = Rbr.getRbr();

	short nStavka = 1; // redni broj stavke

	short delStavka; // redni broj stavke koja se briše

	boolean isFind; // Da li je pronaðen record na stanju
	
	boolean isTranzit;
	boolean isNar;
	boolean isMinusAllowed = false;

	boolean isDobArt; // Da li ima record u DOBART

	boolean enableZT; // ab.f
	
	public char prSTAT;  // prebacen umobolni static iz _Main

	//  boolean rowAddDelete = false; // Rade dodao za ekran ZT. Zašto? Pitaj ga!

	String srcString; // Koji je kljuè za traženje u SEQ

	String vrDok;

	String masterTitle;

	String detailTitle;

	hr.restart.robno.jpPreselectDoc jpp;

	java.sql.Timestamp dan;

	dlgErrors errs;

	public java.lang.String[] key = Util.mkey;

	java.math.BigDecimal oldIZNKALK = main.nul;
	
	raNavAction rnvKartica = new raNavAction("Kartica artikla",
        raImages.IMGMOVIE, java.awt.event.KeyEvent.VK_F11) {
      public void actionPerformed(ActionEvent e) {
        keyActionShowKartica();
      }
    };

	public frmUlazTemplate() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		//    qDSZT = new QueryDataSet();
		this.setUserCheck(true);
		this.stanjeSet.setResolver(dm.getQresolver());

		this.raMaster.getJpTableView().addTableModifier(
				new hr.restart.swing.raTableColumnModifier("CORG",
						new String[] { "CORG", "NAZIV" }, dm
								.getAllOrgstruktura()));
		this.raMaster.getJpTableView().addTableModifier(
				new hr.restart.swing.raTableColumnModifier("CPAR",
						new String[] { "CPAR", "NAZPAR" }, dm.getPartneri()));
        
        this.raMaster.installSelectionTracker("BRDOK");
        
        raDetail.removeRnvCopyCurr();
        
        isMinusAllowed = frmParam.getParam("robno", "allowMinusU", "N",
          "Dopustiti odlazak u minus na ulazima (D,N)?").equals("D");

		setMasterKey(key);
		System.out.println("setting master key for "+getClass().getName() + " " + Arrays.asList(key));
		setDetailKey(Util.dkey);
		setVisibleColsMaster(new int[] { 4, 5, 6, 8, 9 });
		setVisibleColsDetail(new int[] { 4,
				Aut.getAut().getCARTdependable(5, 6, 7), 8, 9, 10, 32, 33 });
		set_kum_detail(true);
		
		stozbrojiti_detail(new String[] { "IDOB", "IDOB_VAL", "IRAB", "IZT",
				"INAB", "IMAR", "IBP", "IPOR", "ISP", "IZAD", "DIOPORMAR",
				"DIOPORPOR", "PORAV" });
		setnaslovi_detail(new String[] { "Dobavljaèev iznos", "Valutni iznos",
				"Iznos popusta", "Iznos zavisih troškova", "Nabavni iznos",
				"Iznos RUC-a", "Iznos bez poreza", "Iznos poreza",
				"Iznos s porezom", "Iznos zaduženja", "Poravnanje marže",
				"Poravnanje poreza", "Poravnanje" });
		rCD.setisNeeded(hr.restart.sisfun.frmParam.getParam("robno",
				"kontKalk", "D",
				"Kontrola ispravosti redoslijeda unosa dokumenata")
				.equalsIgnoreCase("D"));
	}
	
	void keyActionShowKartica() {
      util.showKartica(this);
    }
	
	protected void tranzitDohvat() {
	  System.out.println("tranzit dohvat");
	}
	
	public void afterGetArtikl() {
	  System.out.println("after get artikl");
	}

	public void beforeShowMaster() {
	  String cskl = jpp.getSelRow().getString("CSKL");
      String additional = "";
      if (cskl.length() > 0) {
         if (TD.isCsklSklad(vrDok)) {
           if (lD.raLocate(dm.getSklad(), "CSKL", cskl))
             additional = "  skladište " + cskl + " - " + 
                 dm.getSklad().getString("NAZSKL");
         } else {
           if (lD.raLocate(dm.getOrgstruktura(), "CORG", cskl))
             additional = "  org. jedinica " + cskl + " - " +
                 dm.getOrgstruktura().getString("NAZIV");
         }
      }
      
      isTranzit = cskl.length() > 0 && 
          TD.isCsklSklad(vrDok) && 
          lD.raLocate(dm.getSklad(), "CSKL", cskl) &&
          dm.getSklad().getString("VRSKL").equals("Z");
    
		raMaster.setkum_tak(true);
		raMaster.setstozbrojiti(new String[] {"UINAB", "UIZT", "UIPRPOR", "UIKAL", "UIRAC", "PLATITI"});
      setNaslovMaster(masterTitle + additional);
		
	  isNar = frmParam.getParam("robno", "prkFromNar", "N",
	      "Omoguæiti prebacivanje narudžbe u primku").equalsIgnoreCase("D");
	}

	public void beforeShowDetail() {
		setNaslovDetail((detailTitle.concat(" br. ")
				+ getMasterSet().getString("VRDOK") + "-"
				+ getMasterSet().getString("CSKL").trim() + "/"
				+ getMasterSet().getString("GOD") + "-" + vl.maskZeroInteger(
				new Integer(getMasterSet().getInt("BRDOK")), 6)));
	}

  int checkings = 0;
	public void refilterDetailSet() {
    checkings = 0;
		super.refilterDetailSet();
		setNaslovDetail((detailTitle.concat(" br. ")
				+ getMasterSet().getString("VRDOK") + "-"
				+ getMasterSet().getString("CSKL").trim() + "/"
				+ getMasterSet().getString("GOD") + "-" + vl.maskZeroInteger(
				new Integer(getMasterSet().getInt("BRDOK")), 6)));
	}

	public void SetFokusMaster(char mode) {
		findOldMasterValues(mode);
		//    cMode=mode;
		if (mode == 'N') {
		}
	}

	public void SetFokusDetail(char mode) {
		cMode = mode;
		findOldValues(mode);
		if (mode == 'N') {
			getDetailSet().setString("CSKL", getMasterSet().getString("CSKL"));
			getDetailSet()
					.setString("VRDOK", getMasterSet().getString("VRDOK"));
			getDetailSet().setInt("BRDOK", getMasterSet().getInt("BRDOK"));
			getDetailSet().setString("GOD", getMasterSet().getString("GOD"));
		} else if (mode == 'I') {
			if (dm.getArtikli().getInt("CART") != getDetailSet().getInt("CART"))
				lD.raLocate(dm.getArtikli(), "CART", Integer.toString(getDetailSet().getInt("CART")));

			if (!lD.raLocate(dm.getPorezi(), "CPOR", dm.getArtikli().getString("CPOR"))){
	      System.err.println("Greška nisu naðeni porezi !!!!" + dm.getArtikli());
	    }
		}
	}

	public void EntryPointMaster(char mode) {
		//    cMode=mode;
		findUIZT();
		findOldMasterValues(mode);
	}

	//  public void EntryPointDetail(char mode) {
	//    if(qDSZT.isOpen())
	//      this.qDSZT.emptyAllRows();
	//  }
	//  public void AfterCancelDetail() {
	//    this.qDSZT.empty();
	//  }
	public boolean ValidacijaMaster(char mode) {
		getMasterSet().setTimestamp("SYSDAT", vl.findDate(true, 0));
		if (mode == 'N') {
			//      util.getBrojDokumenta(getMasterSet());
			getMasterSet().setString("CUSER",
					hr.restart.sisfun.raUser.getInstance().getUser());
		}
		int docExists = isDocumentAllreadyExist(getMasterSet().getString("BRRAC"),
            this.oldBRRAC,getMasterSet().getInt("CPAR"),this.oldCPAR);
	  	if (docExists == -1){
	        JOptionPane.showMessageDialog(this.getWindow(), "Broj raèuna "+
	        		getMasterSet().getString("BRRAC")+" za partnera " +
	        		getMasterSet().getInt("CPAR")+ " vaæ postoji u evidenciji robnog knjigovodstva. ",
	                "Greška", JOptionPane.ERROR_MESSAGE);
	        return false;
	  	}
	  	if (docExists == -2){
	        JOptionPane.showMessageDialog(this.getWindow(), "Broj raèuna "+
	        		getMasterSet().getString("BRRAC")+" za partnera " +
	        		getMasterSet().getInt("CPAR")+ " vaæ postoji u evidenciji saldakonti. ",
	                "Greška", JOptionPane.ERROR_MESSAGE);
	        return false;
	  	}


		return util.isDocSaveable(getMasterSet());
	}

	public boolean ValidacijaDetail(char mode) {
		if (mode == 'N') {
			findNSTAVKA();
			getDetailSet().setShort("RBR", nStavka);

		}

		isFind = findSTANJE();
		findIZAD();
		
		BigDecimal dk = getDetailSet().getBigDecimal("KOL");
		if (mode == 'I') dk = dk.subtract(oldKOL);
		if (isFind && !isTranzit && !isMinusAllowed && prSTAT!='K' && (mode == 'I' || dk.signum() < 0) && 
		    stanjeSet.getBigDecimal("KOL").compareTo(dk.negate()) < 0) {
          JOptionPane.showConfirmDialog(raDetail.getWindow(),
                "Nedovoljna kolièina na zalihi za smanjivanje ovom stavkom!",
                "Gre\u0161ka", JOptionPane.ERROR_MESSAGE);
          return false;
        }

		if (mode == 'N') {
			if (stanjeSet.getRowCount() == 0)
				return true;

			// neistestirano

			if (hr.restart.sisfun.frmParam.getParam("robno", "kontKalk", "D", "Kontrola ispravosti redoslijeda unosa dokumenata")
					.equalsIgnoreCase("D")) {
				if (!rCD.isDataKalkulforKalkulOK(getMasterSet().getTimestamp(
						"DATDOK"), stanjeSet.getString("TKAL"))) {
					JOptionPane
							.showConfirmDialog(
									null,
									"Datum kalkulacije je manji nego prethodne kalkulacije za ovaj artikl !",
									"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
									JOptionPane.ERROR_MESSAGE);
					return false;
				}
	
				if (!rCD.isDateIzlazOK(getMasterSet().getTimestamp("DATDOK"),
						stanjeSet.getString("TKAL"))) {
					JOptionPane
							.showConfirmDialog(
									null,
									"Datum je manji nego zadnji izlazni dokument za ovaj artikl !",
									"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
									JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		if (mode == 'I') {
			if (!isUpdateOrDeletePossible()) {
				JOptionPane
						.showConfirmDialog(
								null,
								"Ispravak nije dozvoljen. Postoji promet koji je napravljen po ovom dokumentu !",
								"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
								JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		SanityCheck.basicStdoku(getDetailSet());
		return true;
	}
	
	boolean updateMC, updateNC;
	public boolean checkArtCijene() {
	  updateMC = updateNC = false;
	  if (!frmParam.getParam("robno","updcijeneulaz","N",
          "Ažurirati cijenu na artiklu kod ulaza (D,N)").trim().equalsIgnoreCase("D")) return true;
	  if (frmParam.getParam("robno","dohMcPOS","AR",
        "Dohvat cijene na POS-u (AR,ST)").trim().equalsIgnoreCase("AR")) {
	    
	    if (lD.raLocate(dm.getArtikli(), "CART", getDetailSet().getInt("CART") + "") &&
	        Aus.comp(dm.getArtikli(), "MC", getDetailSet()) != 0) {
	      int opt = JOptionPane.showConfirmDialog(raDetail.getWindow(),
                  "Cijena s porezom se razlikuje od cijene na artiklu. Ažurirati cijenu na artiklu?",
                  "Promjena cijene", JOptionPane.YES_NO_CANCEL_OPTION);
	      if (opt == JOptionPane.CANCEL_OPTION) return false;
	      if (opt == JOptionPane.YES_OPTION) updateMC = true;
	    }
	  }
	  
	  if (frmParam.getParam("robno","updNCulaz","D",
          "Ažurirati nabavnu cijenu na artiklu kod ulaza (D,N)").trim().equalsIgnoreCase("D"))
	    updateNC = true;

	  return true;
	}

	//  public void AfterSaveDetail(char mode) {
	//    updateStanje('N');
	//  }
	//  public void AfterSaveMaster(char mode) {
	//  }
	public boolean DeleteCheckMaster() {
		srcString = util.getSeqString(getMasterSet());
		if (getDetailSet().rowCount() > 0) {
			JOptionPane.showConfirmDialog(null,
					"Nisu pobrisane stavke dokumenta !", "Gre\u0161ka",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!util.checkSeq(srcString, Integer.toString(getMasterSet().getInt(
				"BRDOK")))) {
			return false;
		}
		//    deleteDefZT();
		return true;
	}

	public boolean locateSklad() {
		dm.getSklad().open();
		// tomo update
		hr.restart.util.lookupData lD = hr.restart.util.lookupData
				.getlookupData();
		if (!lD.raLocate(dm.getSklad(), "CSKL", getMasterSet()
				.getString("CSKL"))) {
			JOptionPane.showMessageDialog(raDetail.getWindow(),
			    "Ne mogu pronaæi slog skladišta! ("+
			    getMasterSet().getString("CSKL")+")", "Greška",
			    JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public boolean localDodatniDetailCheck() {
		return true;
	}

	public boolean DeleteCheckDetail() {
		rCD.prepareFields(getDetailSet());
		isFind = findSTANJE();
		if (isFind && !isTranzit &&!isMinusAllowed && prSTAT!='K' && stanjeSet.getBigDecimal("KOL").
				compareTo(getDetailSet().getBigDecimal("KOL")) < 0) {
			JOptionPane.showConfirmDialog(raDetail.getWindow(),
					"Brisanje nije moguæe. Kolièina na stanju je manja od kolièine na ovoj stavci!",
					"Gre\u0161ka", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!isUpdateOrDeletePossible()) {
			JOptionPane.showConfirmDialog(raDetail.getWindow(),
							"Brisanje nije moguæe. Postoji promet koji je napravljen po ovom dokumentu !",
							"Gre\u0161ka", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!locateSklad()) return false;
		if (dm.getSklad().getString("VRZAL").trim().equals("V")) {
			if (getDetailSet().getBigDecimal("VC").compareTo(
					stanjeSet.getBigDecimal("VC")) != 0) {
				JOptionPane.showConfirmDialog(raDetail.getWindow(),
						"Brisanje nije moguæe: razli\u010Dite cijene !",
						"Gre\u0161ka", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		if (dm.getSklad().getString("VRZAL").trim().equals("M")) {
			if (getDetailSet().getBigDecimal("MC").compareTo(stanjeSet
					.getBigDecimal("MC")) != 0) {
				JOptionPane.showConfirmDialog(raDetail.getWindow(),
						"Brisanje nije moguæe: razli\u010Dite cijene: "
								+ getDetailSet().getBigDecimal("MC") + ", "
								+ stanjeSet.getBigDecimal("MC") + " !",
						"Gre\u0161ka", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		if (!dlgSerBrojevi.getdlgSerBrojevi().beforeDeleteSerBr(getDetailSet(),
				'U'))
			return false;
		oldKOL = getDetailSet().getBigDecimal("KOL");
		oldMAR = getDetailSet().getBigDecimal("IMAR");
		oldPOR = getDetailSet().getBigDecimal("IPOR");
		oldNAB = getDetailSet().getBigDecimal("INAB");
		oldZAD = getDetailSet().getBigDecimal("IZAD");
		oldPORAV = getDetailSet().getBigDecimal("PORAV");
		oldPORAVMAR = getDetailSet().getBigDecimal("DIOPORMAR");
		oldPORAVPOR = getDetailSet().getBigDecimal("DIOPORPOR");
		delStavka = getDetailSet().getShort("RBR");
		oldVC = getDetailSet().getBigDecimal("SVC");
		oldMC = getDetailSet().getBigDecimal("SMC");
		oldCART = getDetailSet().getInt("CART");
		//		return isFind;
		//if (!isFind)
		//	return false;
		//else
			return localDodatniDetailCheck();

	}

	//  public void AfterDeleteMaster() {
	//    util.delSeq(srcString);
	//  }
	public void AfterDeleteDetail() {
		System.out.println("AfterDelitDitejl");
		//-> handlanje zt stavki
		System.out.println("Pas: "
				+ getDetailSet().getBigDecimal("SVC").doubleValue());
		//    dm.getVtzavtr().getDatabase().executeStatement(rdUtil.getUtil().deleteExistingZavtrU(getMasterSet(),
		// delStavka));
		//    util.recalcRBR(getDetailSet(), delStavka);
		
		if (TD.isDocDiraZalihu(getMasterSet().getString("VRDOK")) &&
            raWebSync.active && raWebSync.isWeb(oldCART) && raWebSync.isWeb(getMasterSet().getString("CSKL"))) {
          raWebSync.updateStanje(oldCART, getMasterSet());
        }
	}
	
	public void AfterSaveDetail(char mode) {
	  // TODO Auto-generated method stub
	  if (TD.isDocDiraZalihu(getMasterSet().getString("VRDOK")) &&
          raWebSync.active && raWebSync.isWeb(oldCART) && raWebSync.isWeb(getMasterSet().getString("CSKL"))) {
        raWebSync.updateStanje(getDetailSet().getInt("CART"), getMasterSet());
      }
	}

	/*
	 * void insertVTZavtr(String cvar) { if (cvar.equals("")) { return; } String
	 * qStr = rdUtil.getUtil().getVTZavtrInsert(cvar); vl.execSQL(qStr);
	 * vl.RezSet.open(); vl.RezSet.first(); short lrbr =1; do { String sInsert =
	 * "insert into VTZAVTR values('"+vl.RezSet.getString("LOKK")+"',
	 * '"+vl.RezSet.getString("AKTIV")+ "',
	 * '"+getMasterSet().getString("CSKL")+"','"+getMasterSet().getString("VRDOK")+"',
	 * '"+getMasterSet().getString("GOD")+
	 * "',"+getMasterSet().getInt("BRDOK")+",0,"+lrbr+
	 * ",'"+vl.RezSet.getString("CZT")+"',"+vl.RezSet.getBigDecimal("PZT")+","+vl.RezSet.getBigDecimal("IZT")+
	 * ",0.00,'"+vl.RezSet.getString("ZTNAZT")+"',null)"; try {
	 * hr.restart.util.raTransaction.runSQL(sInsert); } catch
	 * (java.lang.Exception e) { e.printStackTrace(); } lrbr++;
	 * vl.RezSet.next(); } while(vl.RezSet.inBounds()); } public void
	 * saveDodZT() { DataRow dr; if(rowAddDelete==false) { qDSZT.close(); }
	 * if(qDSZT.isOpen()) { dr = new DataRow(qDSZT);
	 * dm.getVtzavtr().getDatabase().executeStatement(rdUtil.getUtil().deleteExistingZavtrU(getDetailSet())); }
	 * else {
	 * dm.getVtzavtr().getDatabase().executeStatement(rdUtil.getUtil().deleteExistingZavtrU(getDetailSet()));
	 * 
	 * String qStr = rdUtil.getUtil().getDodZTU((short)0,
	 * getDetailSet().getInt("BRDOK"), getMasterSet()); qDSZT.setQuery(new
	 * QueryDescriptor(dm.getDatabase1(), qStr)); qDSZT.open(); dr = new
	 * DataRow(qDSZT); } for (int i = 0; i < this.qDSZT.getRowCount(); i++) {
	 * qDSZT.getDataRow(i, dr); if(dr.getShort("RBR")==0) { dr.setShort("RBR",
	 * getDetailSet().getShort("RBR")); } try {
	 * hr.restart.util.raTransaction.runSQL(rdUtil.getUtil().updateZavtrU(dr)); }
	 * catch (java.lang.Exception e) { e.printStackTrace(); } } //
	 * dm.getVtzavtr().saveChanges(); qDSZT.empty(); qDSZT.close(); }
	 */
	/*
	 * void deleteDefZT() { QueryDataSet maxRBR = new QueryDataSet();
	 * maxRBR.setQuery(new QueryDescriptor(dm.getDatabase1(),
	 * rdUtil.getUtil().maxRBR(getMasterSet()))); maxRBR.open(); if
	 * (maxRBR.getShort("MAXRBR")==0) {
	 * dm.getVtzavtr().getDatabase().executeStatement(rdUtil.getUtil().deleteExistingZavtrU(getMasterSet(),
	 * (short)0)); dm.getVtzavtr().saveChanges(); } }
	 */
	void findDOBART() {
		dm.getDob_art().open();
		isDobArt = hr.restart.util.lookupData.getlookupData().raLocate(
				dm.getDob_art(),
				new com.borland.dx.dataset.DataSet[] { getMasterSet(),
						getDetailSet() },
				new java.lang.String[] { "CPAR", "CART" },
				new java.lang.String[] { "CPAR", "CART" });
        if (!isDobArt) {
          String defdobart = frmParam.getParam("robno", "defdobart", "N/A", "Oznaka dobavljaca od kojeg se vuku cijene ako ih ne nadje kod unesenog");
          if (!"N/A".equalsIgnoreCase(defdobart.trim())) {
            isDobArt = hr.restart.util.lookupData.getlookupData().raLocate(
                dm.getDob_art(),
                new java.lang.String[] { "CPAR", "CART" },
                new java.lang.String[] { defdobart, getDetailSet().getInt("CART")+"" });
System.out.println("Jel nadjen cjenik na partneru "+defdobart+"? "+isDobArt);
          }
        }
	}

	void saveDobArt() {
		if (!isDobArt) {
			dm.getDob_art().insertRow(false);
		}
		dm.getDob_art().setInt("CPAR", getMasterSet().getInt("CPAR"));
		dm.getDob_art().setInt("CART", getDetailSet().getInt("CART"));
		dm.getDob_art().setString("CART1", getDetailSet().getString("CART1"));
		dm.getDob_art().setString("BC", getDetailSet().getString("BC"));
		dm.getDob_art().setString("NAZART", getDetailSet().getString("NAZART"));
		dm.getDob_art().setBigDecimal("DC", getDetailSet().getBigDecimal("DC"));
		dm.getDob_art().setBigDecimal("PRAB",
				getDetailSet().getBigDecimal("PRAB"));
		dm.getDob_art().post();
		dm.getDob_art().saveChanges();
		//dm.getSynchronizer().propagateChanges(dm.getDob_art());
	}

	void findOldMasterValues(char mode) {
System.out.println("mode");		
		if (mode == 'I') {
			oldCPAR = getMasterSet().getInt("CPAR");
			oldBRRAC = getMasterSet().getString("BRRAC");
		} else {
			oldCPAR=-1;
			oldBRRAC = "";
		}
System.out.println("oldCPAR ="+oldCPAR);
System.out.println("oldBRRAC "+oldBRRAC);
	}
	
	void findOldValues(char mode) {
		if (mode == 'N') {
			oldKOL = main.nul;
			oldMAR = main.nul;
			oldPOR = main.nul;
			oldNAB = main.nul;
			oldZAD = main.nul;
			oldPORAV = main.nul;
			oldPORAVMAR = main.nul;
			oldPORAVPOR = main.nul;
			oldIZNKALK = main.nul;
		} else if (mode == 'I') {
			oldKOL = getDetailSet().getBigDecimal("KOL");
			oldMAR = getDetailSet().getBigDecimal("IMAR");
			oldPOR = getDetailSet().getBigDecimal("IPOR");
			oldNAB = getDetailSet().getBigDecimal("INAB");
			oldZAD = getDetailSet().getBigDecimal("IZAD");
			oldPORAV = getDetailSet().getBigDecimal("PORAV");
			oldPORAVMAR = getDetailSet().getBigDecimal("DIOPORMAR");
			oldPORAVPOR = getDetailSet().getBigDecimal("DIOPORPOR");
			oldIZNKALK = getDetailSet().getBigDecimal("INAB");
		}
	}

	void findNSTAVKA() {
		nStavka = rbr.vrati_rbr("STDOKU", getMasterSet().getString("CSKL"),
				getMasterSet().getString("VRDOK"), getMasterSet().getString(
						"GOD"), getMasterSet().getInt("BRDOK"));
	}
/*
	public void mylocateSklad(){
		dm.getSklad().open();
		hr.restart.util.lookupData.getlookupData().raLocate(dm.getSklad(),
				new String[] { "CSKL" },
				new String[] { getMasterSet().getString("CSKL") });
	}
*/	
	
	void findIZAD() {
		locateSklad();

		//    Util.getUtil().getSkladFromCorg().interactiveLocate(getMasterSet().getString("CSKL"),"CSKL",com.borland.dx.dataset.Locate.FIRST,
		// false);
		if (dm.getSklad().getString("VRZAL").trim().equals("N") ||
		    dm.getSklad().getString("VRZAL").trim().equals("F") ||
		    dm.getSklad().getString("VRZAL").trim().equals("L") ||
		    dm.getSklad().getString("VRZAL").trim().equals("H")) {
			getDetailSet().setBigDecimal("ZC",
					getDetailSet().getBigDecimal("NC"));
			getDetailSet().setBigDecimal("IZAD",
					getDetailSet().getBigDecimal("INAB"));
			getDetailSet().setBigDecimal("IMAR", util.nul);
			getDetailSet().setBigDecimal("IPOR", util.nul);
		} else if (dm.getSklad().getString("VRZAL").trim().equals("V")) {
			getDetailSet().setBigDecimal("ZC",
					getDetailSet().getBigDecimal("VC"));
			getDetailSet().setBigDecimal("IZAD",
					getDetailSet().getBigDecimal("IBP"));
			getDetailSet().setBigDecimal("IPOR", util.nul);
			if (isFind) {
				getDetailSet().setBigDecimal(
						"PORAV",
						util.sumValue(oldPORAV, util.multiValue(util
								.negateValue(stanjeSet.getBigDecimal("KOL"),
										oldKOL), util.negateValue(
								getDetailSet().getBigDecimal("VC"), stanjeSet
										.getBigDecimal("VC")))));
				getDetailSet().setBigDecimal("DIOPORMAR",
						getDetailSet().getBigDecimal("PORAV"));
			}
		} else if (dm.getSklad().getString("VRZAL").trim().equals("M")) {
			getDetailSet().setBigDecimal("ZC",
					getDetailSet().getBigDecimal("MC"));
			getDetailSet().setBigDecimal("IZAD",
					getDetailSet().getBigDecimal("ISP"));
			if (isFind) {
				//        getDetailSet().setBigDecimal("PORAV",
				// stanjeSet.getBigDecimal("KOL").multiply(getDetailSet().getBigDecimal("MC").add(stanjeSet.getBigDecimal("MC").negate())).setScale(2,
				// BigDecimal.ROUND_HALF_UP));
				getDetailSet().setBigDecimal(
						"PORAV",
						util.sumValue(oldPORAV, util.multiValue(util
								.negateValue(stanjeSet.getBigDecimal("KOL"),
										oldKOL), util.negateValue(
								getDetailSet().getBigDecimal("MC"), stanjeSet
										.getBigDecimal("MC")))));
				
				
				/*getDetailSet().setBigDecimal("DIOPORPOR", getDetailSet().getBigDecimal("PORAV").
				    divide(dm.getPorezi().getBigDecimal("UKUPOR").movePointLeft(2).add(Aus.one0), 2, BigDecimal.ROUND_HALF_UP));
				    multiply(
								dm.getPorezi().getBigDecimal("UKUNPOR"))
								.divide(main.sto, 1).setScale(2,
										BigDecimal.ROUND_HALF_UP));*/
				Aus.base(getDetailSet(), "DIOPORPOR", "PORAV", dm.getPorezi().getBigDecimal("UKUPOR"));
				
				Aus.sub(getDetailSet(), "DIOPORMAR", "PORAV", "DIOPORPOR");
				/*getDetailSet().setBigDecimal(
						"DIOPORMAR",
						getDetailSet().getBigDecimal("PORAV").add(
								getDetailSet().getBigDecimal("DIOPORPOR")
										.negate()).setScale(2,
								BigDecimal.ROUND_HALF_UP));*/
			}
		}
		if (isFind) { // ako postoji na stanju
			if (this.raDetail.getMode() == 'N') {
				getDetailSet().setBigDecimal("SKOL",
						stanjeSet.getBigDecimal("KOL"));
				getDetailSet().setBigDecimal("SVC",
						stanjeSet.getBigDecimal("VC"));
				getDetailSet().setBigDecimal("SMC",
						stanjeSet.getBigDecimal("MC"));
			}
		}
	}

	void updateStanje(char mode) {
		if (mode != 'B' && !raVart.isStanje(getDetailSet().getInt("CART"))) return;
		
		util.updateStanje(oldKOL, oldNAB, oldMAR, oldPOR, oldZAD, oldPORAVMAR,
				oldPORAVPOR, oldPORAV, mode, isFind, prSTAT, stanjeSet,
				getDetailSet());
		//    this.raDetail.getJpTableView().fireTableDataChanged();
	}

	boolean findSTANJE() {
		if (!raVart.isStanje(getDetailSet().getInt("CART"))) return false;
		
	  Aus.setFilter(stanjeSet, rdUtil.getUtil().
	          findStanje(getMasterSet().getString("CSKL"),
                        getDetailSet().getInt("CART"),
                        getMasterSet().getString("GOD")));
		stanjeSet.open();
		if (stanjeSet.getRowCount() > 0) {
			return true;
		}
		return false;
	}

	boolean findUIZT() {
		if (getMasterSet().getBigDecimal("UINAB").doubleValue() > 0) {
			return true;
		} else {
			getMasterSet().setBigDecimal("UPZT", main.nul);
			getMasterSet().setBigDecimal("UIZT", main.nul);
			return false;
		}
	}

	//TODO Ante molim te izbaci predselekciju gdje je god moguæe. Zbog ispisa
	// preko doubleclicka koji zaobilazi predselect

	public String PrepSql(boolean detail, boolean usecond) {
        String table = "doku.";
		String sqldodat = "";
		if (detail) {
			sqldodat = "and "+table+"cskl='" + getMasterSet().getString("CSKL") // was
					// jpp.getSelRow().getString("CSKL")
					+ "' and "+table+"vrdok='" + getMasterSet().getString("VRDOK") // was
					// jpp.getSelRow().getString("VRDOK")
//					+ "' and "+table+"brdok = '" + getMasterSet().getInt("BRDOK")
					+ "' and "+table+"god = '" + getMasterSet().getString("GOD") + "' ";
            
            Condition con = raMaster.getSelectCondition();
            if (con != null && con != Condition.none && usecond) {
                con.qualified("doku");
                sqldodat = sqldodat + " and " + con;

            } else {
                sqldodat = sqldodat + "and doku.brdok ="
                        + getMasterSet().getInt("BRDOK");
            }
            
		} else {
			if (!jpp.getSelRow().getString("CSKL").equals(""))
				sqldodat = "and "+table+"cskl='" + jpp.getSelRow().getString("CSKL")
						+ "' ";
			if (!jpp.getSelRow().getString("VRDOK").equals(""))
				sqldodat = sqldodat + "and "+table+"vrdok='"
						+ jpp.getSelRow().getString("VRDOK") + "' ";
			if (jpp.getSelRow().getInt("CPAR") != 0)
				sqldodat = sqldodat + "and "+table+"cpar="
						+ jpp.getSelRow().getInt("CPAR") + " ";
			if (!jpp.getSelRow().getTimestamp("DATDOK-from").equals("")) {
				sqldodat = sqldodat
						+ "and "+table+"datdok >= '"
						+ rdu.PrepDate(jpp.getSelRow().getTimestamp(
								"DATDOK-from"), true) + "' ";
			}
			if (!jpp.getSelRow().getTimestamp("DATDOK-to").equals("")) {
				sqldodat = sqldodat
						+ "and "+table+"datdok <= '"
						+ rdu.PrepDate(jpp.getSelRow()
								.getTimestamp("DATDOK-to"), false) + "' ";
			}
		}
		return sqldodat;
	}

	public boolean isDetailExist() {
		if (Stdoku.getDataModule().getRowCount(
				Condition.whereAllEqual(new String[] { "CSKL", "GOD", "VRDOK",
						"BRDOK" }, getMasterSet())) > 0)
			return true;
		else {
			javax.swing.JOptionPane.showMessageDialog(null,
					"Ne postoje stavke ovog dokumenta. Nemogu\u0107 ispis!",
					"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public void Funkcija_ispisa_master() {
		if (!isDetailExist())
			return;
		//    System.out.println("------------------------------------------------------------");
		//    System.out.println("PrepSql(true) " + PrepSql(true));
		//    System.out.println("vrDok " + vrDok);
		//    System.out.println("------------------------------------------------------------");

		reportsQuerysCollector.getRQCModule().ReSql(PrepSql(true,true), vrDok);
		super.Funkcija_ispisa_master();
	}

	public void Funkcija_ispisa_detail() {
		if (!isDetailExist())
			return;
		reportsQuerysCollector.getRQCModule().ReSql(PrepSql(true,false), vrDok);
		super.Funkcija_ispisa_detail();
	}

	// ab.f
	public void afterSetModeMaster(char oldm, char newm) {
		if (newm == 'B' && getJPanelMaster() instanceof jpUlazMaster)
			((jpUlazMaster) getJPanelMaster()).jpGetVal.disableDohvat();
	}

	public boolean doBeforeSaveMaster(char mode) {
		if (mode == 'N') {
			util.getBrojDokumenta(getMasterSet());
		}
		if (mode != 'B')
			SanityCheck.basicDoku(getMasterSet());
		return true;

	}

	public boolean doBeforeSaveDetail(char mode) {
		//System.out.println("doBeforeSaveDetail('"+mode+"')");
	  if (mode != 'B' && (updateMC || updateNC)) try {
	    if (lD.raLocate(dm.getArtikli(), "CART", getDetailSet().getInt("CART") + "")) {
	      if (updateMC) Aus.set(dm.getArtikli(), "MC", getDetailSet());
	      if (updateMC) Aus.set(dm.getArtikli(), "VC", getDetailSet());
	      if (updateNC) Aus.set(dm.getArtikli(), "NC", stanjeSet);
	      if (updateNC) Aus.set(dm.getArtikli(), "DC", getDetailSet());
	      hr.restart.util.raTransaction.saveChanges(dm.getArtikli());
	    }
	  } finally {
	    updateMC = updateNC = false;
	  }
		if (mode == 'N') {
            getDetailSet().setInt("RBSID", rbr.getRbsID(getDetailSet()));
            getDetailSet().setString(
                    "ID_STAVKA",
                    raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
                            "vrdok", "god", "brdok", "rbsid" }, "stdoku"));

            getMasterSet().setBigDecimal(
					"UIKAL",
					getMasterSet().getBigDecimal("UIKAL").add(
							getDetailSet().getBigDecimal("INAB")));
			
            raFLH.getInstance().ulazNoviFLH(getDetailSet());    /* FLH */
            
		} else if (mode == 'B') {
			System.out.println("UIKAL (prije) = "
					+ getMasterSet().getBigDecimal("UIKAL"));
			System.out.println("INAB  = " + oldNAB);
			getMasterSet().setBigDecimal("UIKAL",
					getMasterSet().getBigDecimal("UIKAL").subtract(oldNAB));
			System.out.println("UIKAL (poslije) = "
					+ getMasterSet().getBigDecimal("UIKAL"));
		} else if (mode == 'I') {
			System.out.println("UIKAL (prije) = "
					+ getMasterSet().getBigDecimal("UIKAL"));
			System.out.println("oldIZNKALK  = " + oldIZNKALK);

			getMasterSet().setBigDecimal(
					"UIKAL",
					getMasterSet().getBigDecimal("UIKAL").add(
							getDetailSet().getBigDecimal("INAB")).subtract(
							oldIZNKALK));
			System.out.println("UIKAL (poslije) = "
					+ getMasterSet().getBigDecimal("UIKAL"));
		}

		return true;
	}

	public boolean doWithSaveMaster(char mode) {
		if (mode == 'B') {
			util.delSeq(srcString, true);
		}

		return true;
	}

	public boolean doWithSaveDetail(char mode) {

		if ((isTranzit || isNar) && this instanceof frmPRK) 
		  if (!((frmPRK) this).updateRotStavka(mode)) return false;
		if (mode == 'B') {
			try {
				hr.restart.util.Valid.getValid().recountDataSet(raDetail,
						"RBR", delStavka, false);
				dlgSerBrojevi.getdlgSerBrojevi().deleteSerBr('U');
				/*
				 * System.out.println("Jebeno brianje: " + isFind + ", " +
				 * (oldVC.doubleValue() != 0) + " ," + oldVC.doubleValue());
				 */
				if (isFind && oldVC.signum() != 0 && oldMC.signum() != 0) {
					stanjeSet.setBigDecimal("VC", oldVC);
					stanjeSet.setBigDecimal("MC", oldMC);
				}
				if (raVart.isStanje(oldCART)) {
					getDetailSet().insertRow(true);
					updateStanje('B');
					getDetailSet().cancel();
					rCD.brisanjeKalkulacije(stanjeSet);
					hr.restart.util.raTransaction.saveChanges(stanjeSet);
				}
				hr.restart.util.raTransaction.saveChanges(getDetailSet());
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		} else if (raVart.isStanje(getDetailSet().getInt("CART"))) {
			try {
				System.out.println("apdajtStanje mod: " + mode);
				updateStanje(mode);
				// start TV update
				if (mode == 'N') {
					rCD.unosKalkulacije(getDetailSet(), stanjeSet);
					hr.restart.util.raTransaction.saveChanges(getDetailSet());
				} //else
				  
				stanjeSet.setTimestamp("DATZK", 
				      getMasterSet().getTimestamp("DATDOK"));
				// end TV update

				//        saveDodZT();
				dlgSerBrojevi.getdlgSerBrojevi().setTransactionActive(true);
				dlgSerBrojevi.getdlgSerBrojevi().TransactionSave();
				dlgSerBrojevi.getdlgSerBrojevi().returnOrgTransactionActive();

				hr.restart.util.raTransaction.saveChanges(stanjeSet);
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}
		if (mode != 'B') updateNCart();
		hr.restart.util.raTransaction.saveChanges(getMasterSet());
    checkings = 0;
		return true;
	}
	
	void updateNCart() {
	  QueryDataSet art = Artikli.getDataModule().getTempSet(
	      Condition.equal("CART", getDetailSet()));
	  art.open();
	  Aus.set(art, "NC", getDetailSet());
	  hr.restart.util.raTransaction.saveChanges(art);
	}

	public boolean checkAddEnabled() {
		return !Aut.getAut().isWrongKnjigYear(this, true);
	}

	public boolean checkAccess() {

		if (getMasterSet().getString("STATKNJ").equalsIgnoreCase("K")
				|| getMasterSet().getString("STATKNJ").equalsIgnoreCase("P")) {
			setUserCheckMsg(
					"Korisnik ne može promijeniti dokument jer je proknjižen !",
					false);
			return false;
		}
		if (isPrenesen()) {
			setUserCheckMsg(
					"Korisnik ne može promijeniti dokument jer je prenesen u ili iz druge baze !",
					false);
			return false;
		}
		if (isKPR()) {
			setUserCheckMsg(
					"Dokument je ušao u knjigu popisa i ne smije se mijenjati !!!",
					false);
			return false;
		}
		if (Aut.getAut().isWrongKnjigYear(this))
			return false;
		if (!checkValid())
			return false;
		restoreUserCheckMessage();
		return true;
	}

	public boolean isPrenesen() {
		return getMasterSet().getString("STATUS").equalsIgnoreCase("P");
	}

	public boolean checkValid() {
		return true;
	}

	public boolean isUpdateOrDeletePossible() {
	  if (raFLH.getInstance().provjeraFLH(getDetailSet())) return false;    /* FLH */
	  
	  if (!isFind) return true;
		stanjeSet.refresh();
		return rCD.testKalkulacije(getDetailSet(), stanjeSet);
	}

	public boolean isKPR() {
		return getMasterSet().getString("STAT_KPR").equalsIgnoreCase("D");
	}

	private void createErrorTrace() {
		String title = "Neispravni zbrojevi dokumenta  br. "
				+ getMasterSet().getString("VRDOK")
				+ "-"
				+ getMasterSet().getString("CSKL").trim()
				+ "/"
				+ getMasterSet().getString("GOD")
				+ "-"
				+ vl.maskZeroInteger(
						new Integer(getMasterSet().getInt("BRDOK")), 6);
		errs = new dlgErrors(raDetail.getWindow(), title, false);
		errs.setData(new Column[] {
				dM.createBigDecimalColumn("ZAG", "Zaglavlje"),
				dM.createBigDecimalColumn("STAV", "Stavke"),
				dM.createBigDecimalColumn("DIFF", "Razlika") });
	}

	/*private BigDecimal getSumUINAB() {
		raDetail.getJpTableView().enableEvents(false);
		int row = getDetailSet().getRow();
		BigDecimal sum = _Main.nul;
		for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet()
				.next()) {
			System.out.println(getDetailSet().getBigDecimal("IDOB"));
			System.out.println(getDetailSet().getBigDecimal("IRAB"));
			sum = sum.add(getDetailSet().getBigDecimal("IDOB").subtract(
					getDetailSet().getBigDecimal("IRAB")).setScale(2,
					BigDecimal.ROUND_HALF_UP));
		}
		getDetailSet().goToRow(row);
		raDetail.getJpTableView().enableEvents(true);
		return sum;
	}

	private BigDecimal getSumUIZT() {
		raDetail.getJpTableView().enableEvents(false);
		int row = getDetailSet().getRow();
		BigDecimal sum = _Main.nul;
		for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet()
				.next()) {
			System.out.println(getDetailSet().getBigDecimal("IZT"));
			sum = sum.add(getDetailSet().getBigDecimal("IZT")).setScale(2,
					BigDecimal.ROUND_HALF_UP);
		}
		getDetailSet().goToRow(row);
		raDetail.getJpTableView().enableEvents(true);
		return sum;
	}*/

	private int getZavtr(QueryDataSet ztr, HashMap zag, HashMap stav, HashMap ztopis) {
		int numztr = 0;
		if (ztr == null) {
    	  ztr = VTZtr.getDataModule().getTempSet(Condition.whereAllEqual(key, getMasterSet()));
          ztr.open();
		}
		zag.clear();
		stav.clear();
		ztopis.clear();
		for (ztr.first(); ztr.inBounds(); ztr.next()) {
			BigDecimal val = Aus.to2(ztr.getBigDecimal("IZT"));
			Short rbr = new Short(ztr.getShort("LRBR"));
			if (ztr.getShort("RBR") == 0)
				zag.put(rbr, val);
			else if (!stav.containsKey(rbr))
				stav.put(rbr, val);
			else
				stav.put(rbr, val.add((BigDecimal) stav.get(rbr)));
			if (rbr.intValue() > numztr)
				numztr = rbr.intValue();
			if (ztopis != null && !ztopis.containsKey(rbr)) {
				lookupData.getlookupData().raLocate(dm.getZtr(), "CZT",
						String.valueOf(ztr.getShort("CZT")));
				ztopis.put(rbr, "Zavisni trošak " + ztr.getShort("CZT") + " "
						+ dm.getZtr().getString("NZT"));
			}
		}
		return numztr;
	}

	public boolean checkZavtr() {
    if (++checkings == 3) return true;
		if (getDetailSet().getRowCount() == 0)
			return true;

		if (getMasterSet().getString("STATKNJ").equalsIgnoreCase("K")
				|| getMasterSet().getString("STATKNJ").equalsIgnoreCase("P"))
			return true;
		if (isPrenesen() || isKPR())
			return true;
		boolean wk = Aut.getAut().isWrongKnjigYear(this);
		restoreUserCheckMessage();
		if (wk)
			return true;

		if (errs != null && !errs.isDead())
			errs.hide();

		createErrorTrace();
		BigDecimal uinab = Aus.sum("IDOB", getDetailSet()).subtract(Aus.sum("IRAB",  getDetailSet()));
		BigDecimal uizt = Aus.sum("IZT", getDetailSet());
		System.out.println(uinab);
		System.out.println(getMasterSet().getBigDecimal("UINAB"));
		if (uinab.compareTo(Aus.to2(getMasterSet().getBigDecimal("UINAB"))) != 0)
			errs.addError("Ukupan neto iznos ulaznog ra\u010Duna",
					new BigDecimal[] {
							getMasterSet().getBigDecimal("UINAB"),
							uinab,
							getMasterSet().getBigDecimal("UINAB").subtract(
									uinab) });
		if (uizt.compareTo(Aus.to2(getMasterSet().getBigDecimal("UIZT"))) != 0)
			errs.addError("Ukupan iznos zavisnih troškova", new BigDecimal[] {
					getMasterSet().getBigDecimal("UIZT"), uizt,
					getMasterSet().getBigDecimal("UIZT").subtract(uizt) });

		HashMap ztopis = new HashMap();
		HashMap zag = new HashMap();
		HashMap stav = new HashMap();
		int numztr = getZavtr(null, zag, stav, ztopis);
		for (int i = 1; i <= numztr; i++) {
			Short rbr = new Short((short) i);
			BigDecimal iztZag = (BigDecimal) zag.get(rbr);
			BigDecimal iztStav = (BigDecimal) stav.get(rbr);
			if (iztZag.compareTo(iztStav) != 0)
				errs.addError((String) ztopis.get(rbr), new BigDecimal[] {
						iztZag, iztStav, iztZag.subtract(iztStav) });
		}
		if (errs.countErrors() > 0) {
			JraButton fix = new JraButton();
			fix.setText("Automatski popravak");
			fix.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (errs == null || errs.isDead()
							|| !errs.isActionButton(e.getSource()))
						return;
					automaticFix();
				}
			});
			errs.setActionButton(fix);
		}
		errs.check();
		return errs.countErrors() == 0;
	}
	
	public void recalcFromZtr(QueryDataSet zt) {
	  long row = getDetailSet().getInternalRow();
	  raDetail.getJpTableView().enableEvents(false);
	  try {
	    for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) {
	      findOldValues('I');
	      raProcess.checkClosing();

	      if (dm.getArtikli().getInt("CART") != getDetailSet().getInt("CART"))
	        lD.raLocate(dm.getArtikli(), "CART", Integer.toString(getDetailSet().getInt("CART")));

	      if (!lD.raLocate(dm.getPorezi(), "CPOR", dm.getArtikli().getString("CPOR"))){
	        System.err.println("Greška nisu naðeni porezi !!!!" + dm.getArtikli());
	      }

	      BigDecimal izt = Aus.zero2;
	      for (zt.first(); zt.inBounds(); zt.next())
	        if (zt.getShort("RBR") == getDetailSet().getShort("RBR"))
	          izt = izt.add(zt.getBigDecimal("IZT"));

	      getDetailSet().setBigDecimal("IZT", izt);
	      ((IZavtrHandler) this).getDetailPanel().rekalk = true;
	      ((IZavtrHandler) this).getDetailPanel().kalkulacija(8);
	      ((IZavtrHandler) this).getDetailPanel().rekalk = false;
	      isFind = findSTANJE();
	      findIZAD();
	      updateStanje('I');
	      raTransaction.saveChanges(getDetailSet());
	      raTransaction.saveChanges(stanjeSet);
	    }
	    getDetailSet().goToInternalRow(row);
	  } finally {
	    raDetail.getJpTableView().enableEvents(true);
	  }
	  raDetail.getJpTableView().fireTableDataChanged();
	}
	  
	
	private boolean autoRecalc(final int idx, final BigDecimal diff) {
	  raProcess.runChild(raDetail.getWindow(), new Runnable() {
	    public void run() {
	      final QueryDataSet ztr = VTZtr.getDataModule().getTempSet(
	          Condition.whereAllEqual(key, getMasterSet()));
	      raProcess.openScratchDataSet(ztr);
	      BigDecimal tot = Aus.zero2;
	      BigDecimal uinab = Aus.zero2;
	      double maxerr = Math.max(0.005 * getDetailSet().rowCount(), 0.04);
	      try {
	        long row = getDetailSet().getInternalRow();
	        raDetail.getJpTableView().enableEvents(false);
	        for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) {
	          boolean found = false;
	          for (ztr.first(); ztr.inBounds(); ztr.next()) 
	            if (ztr.getShort("RBR") == getDetailSet().getShort("RBR") && 
	            ztr.getShort("RBR") > 0 && ztr.getShort("LRBR") == idx) found = true;
	          if (found) uinab = uinab.add(Aus.minus(getDetailSet(), "IDOB", "IRAB"));
	        }
	        raProcess.checkClosing();
	        for (getDetailSet().first(); getDetailSet().inBounds() && diff.compareTo(tot) != 0; getDetailSet().next()) {
	          for (ztr.first(); ztr.inBounds(); ztr.next()) 
	            if (ztr.getShort("RBR") == getDetailSet().getShort("RBR") && 
	            ztr.getShort("RBR") > 0 && ztr.getShort("LRBR") == idx) {
	              BigDecimal inab = Aus.minus(getDetailSet(), "IDOB", "IRAB");
	              BigDecimal dio = inab.multiply(diff).divide(uinab, 2, BigDecimal.ROUND_HALF_UP);
	              tot = tot.add(dio);
	              if (tot.subtract(diff).abs().doubleValue() <= maxerr) {
	                dio = dio.add(diff.subtract(tot));
	                tot = diff;
	              }
	              Aus.add(ztr, "IZT",  dio);
	              Aus.percent(ztr, "PZT",  "IZT", inab);
	            }
	        }
	        getDetailSet().goToInternalRow(row);
	      } finally {
	        raDetail.getJpTableView().enableEvents(true);
	      }
	      raProcess.checkClosing();
	      raLocalTransaction trans = new raLocalTransaction() {
	        public boolean transaction() throws Exception {
	          raTransaction.saveChanges(ztr);
	          recalcFromZtr(ztr);
	          return true;
	        }
	      };
	      if (!trans.execTransaction()) {
	        refilterDetailSet();
	        if (trans.getLastException() instanceof ProcessInterruptException)
	          raProcess.checkClosing();
	        raProcess.fail();
	      }
	    }
	  });
	  if (raProcess.isFailed())
        JOptionPane.showMessageDialog(raDetail.getWindow(), "Rekalkulacija neuspješna!",
            "Upozorenje", JOptionPane.WARNING_MESSAGE);
	  return raProcess.isCompleted();
	}

	private boolean recalcAll() {
	  raProcess.runChild(raDetail.getWindow(), new Runnable() {
        public void run() {
          final QueryDataSet ztr = VTZtr.getDataModule().getTempSet(
              Condition.whereAllEqual(key, getMasterSet()));
          raProcess.openScratchDataSet(ztr);
          BigDecimal uinab = Aus.sum("IDOB", getDetailSet()).subtract(Aus.sum("IRAB",  getDetailSet()));
          getMasterSet().setBigDecimal("UINAB", uinab);
          
          HashMap pzts = new HashMap();
          HashMap npzts = new HashMap();
          for (ztr.first(); ztr.inBounds(); ztr.next()) {
            if (ztr.getShort("RBR") == 0) { 
              pzts.put(Short.valueOf(ztr.getShort("LRBR")), ztr.getBigDecimal("PZT"));
              Aus.percent(ztr, "PZT", "IZT", uinab);
              npzts.put(Short.valueOf(ztr.getShort("LRBR")), ztr.getBigDecimal("PZT"));
            }
          }
          raProcess.checkClosing();
          try {
            long row = getDetailSet().getInternalRow();
            raDetail.getJpTableView().enableEvents(false);
            for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) {
              BigDecimal inab = Aus.minus(getDetailSet(), "IDOB", "IRAB");
              for (ztr.first(); ztr.inBounds(); ztr.next()) {
                if (ztr.getShort("RBR") == getDetailSet().getShort("RBR") && ztr.getShort("RBR") > 0) {
                  BigDecimal oldpzt = (BigDecimal) pzts.get(Short.valueOf(ztr.getShort("LRBR")));
                  if (ztr.getBigDecimal("PZT").subtract(oldpzt).abs().doubleValue() < 0.1 &&
                      ztr.getBigDecimal("PZT").intValue() != ztr.getBigDecimal("PZT").doubleValue()) 
                    ztr.setBigDecimal("PZT", (BigDecimal) npzts.get(Short.valueOf(ztr.getShort("LRBR"))));
                    
                  Aus.percentage(ztr, "IZT", inab, "PZT");
                }
              }
            }
            getDetailSet().goToInternalRow(row);
          } finally {
            raDetail.getJpTableView().enableEvents(true);
          }
          raProcess.checkClosing();
          raLocalTransaction trans = new raLocalTransaction() {
            public boolean transaction() throws Exception {
              raTransaction.saveChanges(getMasterSet());
              raTransaction.saveChanges(ztr);
              recalcFromZtr(ztr);
              return true;
            }
          };
          if (!trans.execTransaction()) {
            refilterDetailSet();
            getMasterSet().refetchRow(getMasterSet());
            if (trans.getLastException() instanceof ProcessInterruptException)
              raProcess.checkClosing();
            raProcess.fail();
          }
        }
      });
	  if (raProcess.isFailed())
        JOptionPane.showMessageDialog(raDetail.getWindow(), "Rekalkulacija neuspješna!",
            "Upozorenje", JOptionPane.WARNING_MESSAGE);
	  return raProcess.isCompleted();
	}

	private void automaticFix() {
		errs.hide();
		BigDecimal uinab = Aus.sum("IDOB", getDetailSet()).subtract(Aus.sum("IRAB",  getDetailSet()));
		BigDecimal uinabz = getMasterSet().getBigDecimal("UINAB");
		double maxerr = Math.max(0.005 * getDetailSet().rowCount(), 0.04);
		       
		if (uinab.compareTo(uinabz) != 0) {
			if (uinab.subtract(uinabz).abs().divide(uinabz, 6,
					BigDecimal.ROUND_HALF_UP).doubleValue() < 0.0001
					|| uinab.subtract(uinabz).abs().doubleValue() < maxerr) {
				if (JOptionPane.showConfirmDialog(raDetail.getWindow(),
								"Suma stavki neto iznosa ulaznih ra\u010Duna ne odgovara iznosu na zaglavlju.\n"
										+ "Zanemariva razlika od "
										+ Aus.formatBigDecimal(uinab.subtract(uinabz).abs())
										+ " bit \u0107e korigirana "
										+ "na zaglavlju. U redu?", "Potvrda",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
					try {
						getMasterSet().setBigDecimal("UINAB", uinab);
						getMasterSet().saveChanges();
					} catch (Exception e) {
					}
				}
			} else {
			  if (JOptionPane.showConfirmDialog(raDetail.getWindow(),
                  "Suma stavki neto iznosa ulaznih ra\u010Duna "
                          + "zna\u010Dajno se razlikuje ("
                          + Aus.formatBigDecimal(uinab.subtract(uinabz).abs())
                          + ") od iznosa na zaglavlju.\n" +
                          "Želite li preraèunati sve stavke na ispravan postotak/iznos zavisnih troškova?", "Upozorenje",
                          JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION)
			      if (!recalcAll()) return;
				/*JOptionPane.showMessageDialog(raDetail.getWindow(),
								"Suma stavki neto iznosa ulaznih ra\u010Duna "
										+ "zna\u010Dajno se razlikuje od iznosa na zaglavlju.\nNe mogu obaviti automatsku korekciju!",
								"Upozorenje", JOptionPane.WARNING_MESSAGE);*/
			}
		}
		
		
		HashMap zag = new HashMap();
        HashMap stav = new HashMap();
        HashMap ztopis = new HashMap();
        int numztr = getZavtr(null, zag, stav, ztopis);
        for (int i = 1; i <= numztr; i++) {
          Short rbr = new Short((short) i);
          BigDecimal zagv = (BigDecimal) zag.get(rbr);
          BigDecimal diff = zagv.subtract((BigDecimal) stav.get(rbr)).abs();
          if (diff.doubleValue() >= maxerr && diff.divide(zagv, 6, BigDecimal.ROUND_HALF_UP).doubleValue() >= 0.0001) {
            if (JOptionPane.showConfirmDialog(raDetail.getWindow(),
               ztopis.get(rbr) + " na stavkama se znaèajno razlikuje (" + 
                     Aus.formatBigDecimal(diff) + ") od iznosa na zaglavlju.\n" +
               "Želite li razliku raspodijeliti na taj zavisni trošak proporcionalno po stavkama?", 
                  "Rekalkulacija troška", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                if (!autoRecalc(i, zagv.subtract((BigDecimal) stav.get(rbr)))) return;
            }
          }
        }
        
        
		BigDecimal uizt = Aus.to2(Aus.sum("IZT", getDetailSet()));
		BigDecimal uiztz = getMasterSet().getBigDecimal("UIZT");
		
		if (uizt.compareTo(uiztz) != 0 && uizt.subtract(uiztz).abs().divide(uiztz, 6, BigDecimal.ROUND_HALF_UP).doubleValue() >= 0.001
		    && uizt.subtract(uiztz).abs().doubleValue() >= maxerr*10) {
		  JOptionPane.showMessageDialog(
              raDetail.getWindow(),
              "Ukupna razlika zavisnih troškova je prevelika (" +
                  Aus.formatBigDecimal(uizt.subtract(uiztz).abs()) + ") za automatsku korekciju!",
              "Upozorenje", JOptionPane.WARNING_MESSAGE);
		  return;
		}
				
		BigDecimal max = _Main.nul;
		short mrbr = (short) 0;
		String nazart = "";
		QueryDataSet ztr = VTZtr.getDataModule().getTempSet(
            Condition.whereAllEqual(key, getMasterSet()));
        ztr.open();
        getZavtr(ztr, zag, stav, ztopis);
        long row = getDetailSet().getInternalRow();
        raDetail.getJpTableView().enableEvents(false);
        for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next())
			if (Aus.to2(Aus.minus(getDetailSet(), "IDOB", "IRAB")).compareTo(max) > 0) {
				boolean zok = true;
				for (ztr.first(); ztr.inBounds(); ztr.next()) {
				  if (ztr.getShort("RBR") == getDetailSet().getShort("RBR")) {
					BigDecimal izt = ztr.getBigDecimal("IZT");
					Short rbr = new Short(ztr.getShort("LRBR"));
					if (zag.containsKey(rbr) && stav.containsKey(rbr)) {
						BigDecimal diff = ((BigDecimal) zag.get(rbr)).subtract(
								(BigDecimal) stav.get(rbr)).abs();
						//System.out.println("rbr " + ztr.getShort("RBR") + " diff " + diff);
						if (diff.signum() != 0
								&& (izt.signum() == 0 || (diff.abs().doubleValue() >= maxerr &&
								    diff.divide(izt, 6, BigDecimal.ROUND_HALF_UP).doubleValue() >= 0.0001)))
							zok = false;
					}
				  }
				}
				if (zok) {
					max = Aus.to2(Aus.minus(getDetailSet(), "IDOB", "IRAB"));
					mrbr = getDetailSet().getShort("RBR");
					nazart = getDetailSet().getString("NAZART");
				}
			}
        getDetailSet().goToInternalRow(row);
		raDetail.getJpTableView().enableEvents(true);
		if (mrbr == 0) {
			JOptionPane.showMessageDialog(
							raDetail.getWindow(),
							"Ne mogu prona\u0107i odgovaraju\u0107u stavku primke za automatsku korekciju!",
							"Upozorenje", JOptionPane.WARNING_MESSAGE);
		} else {
			if (JOptionPane.showConfirmDialog(raDetail.getWindow(),
					"Razlika zavisnih troškova od " +
					    Aus.formatBigDecimal(uizt.subtract(uiztz).abs())
					    + " bit æe korigirana na stavci br. " + mrbr + "\n("
							+ nazart + "). U redu?", "Potvrda",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
			    row = getDetailSet().getInternalRow();
				raDetail.getJpTableView().enableEvents(false);

				for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet()
						.next())
					if (getDetailSet().getShort("RBR") == mrbr) {
					  
					  if (dm.getArtikli().getInt("CART") != getDetailSet().getInt("CART"))
		                lD.raLocate(dm.getArtikli(), "CART", Integer.toString(getDetailSet().getInt("CART")));

					  if (!lD.raLocate(dm.getPorezi(), "CPOR", dm.getArtikli().getString("CPOR"))){
		                 System.err.println("Greška nisu naðeni porezi !!!!" + dm.getArtikli());
		              }
					  
						BigDecimal inabn = Aus.to2(Aus.minus(getDetailSet(), "IDOB", "IRAB"));
						ztr = VTZtr.getDataModule().getTempSet(
								Condition.whereAllEqual(key, getMasterSet())
										+ " AND rbr = " + mrbr);
						ztr.open();
						BigDecimal izt = _Main.nul;
						for (ztr.first(); ztr.inBounds(); ztr.next()) {
							Short rbr = new Short(ztr.getShort("LRBR"));
							if (zag.containsKey(rbr) && stav.containsKey(rbr)) {
								BigDecimal diff = ((BigDecimal) zag.get(rbr))
										.subtract((BigDecimal) stav.get(rbr));
								Aus.add(ztr, "IZT",  diff);
								Aus.percent(ztr, "PZT",  "IZT",  inabn);
								izt = izt.add(ztr.getBigDecimal("IZT"))
										.setScale(2, BigDecimal.ROUND_HALF_UP);
							}
						}
						findOldValues('I');
						getDetailSet().setBigDecimal("IZT", izt);
						((IZavtrHandler) this).getDetailPanel().rekalk = true;
						((IZavtrHandler) this).getDetailPanel().kalkulacija(8);
						((IZavtrHandler) this).getDetailPanel().rekalk = false;
						isFind = findSTANJE();
						findIZAD();
						updateStanje('I');
						raTransaction.saveChangesInTransaction(new QueryDataSet[] {getDetailSet(), ztr, stanjeSet });
						break;
					}
				getDetailSet().goToInternalRow(row);
				raDetail.getJpTableView().enableEvents(true);
			}
		}
	}

	public int isDocumentAllreadyExist(
			String newBRRAC,
			String ooldBRRAC,
			int newCPAR,
			int ooldCPAR){
		
		if (newBRRAC.equalsIgnoreCase("")) return 0;
		if ("D".equalsIgnoreCase(frmParam.getParam("robno", "skipSkCheck", "N",
		    "Preskoèiti provjeru broja raèuna u modulu SK (D,N)"))) return 0;
		if (newBRRAC.equalsIgnoreCase(ooldBRRAC) && 
			newCPAR == ooldCPAR) return 0;

		if (raRobno.isDocumentExist(newBRRAC,newCPAR,true)){
			return -1;
		}
		if (raSaldaKonti.existingDocument(newCPAR,newBRRAC,true)){
			return -2;
		}			
		return 0;
		
	}


}