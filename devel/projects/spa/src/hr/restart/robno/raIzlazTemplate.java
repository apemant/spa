/****license*****************************************************************
**   file: raIzlazTemplate.java
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
import hr.restart.baza.Kosobe;
import hr.restart.baza.Partneri;
import hr.restart.baza.RN;
import hr.restart.baza.VTprijenos;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.Aus;
import hr.restart.util.LinkClass;
import hr.restart.util.VarStr;
import hr.restart.util.raImages;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

abstract public class raIzlazTemplate extends hr.restart.util.raMasterDetail {

	// boolean isAutomatikaBrisanjaSvega = false;

	boolean isOJ = false;

	protected String key4delZag;

	private String cuser = "";

	private int savebrdok = 0;

	private String domval;

	public boolean isMaloprodajnaKalkulacija = false;
	
	public boolean isPopustMC = false;
	
	hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

	allStanje AST = allStanje.getallStanje();

	hr.restart.util.Valid val = hr.restart.util.Valid.getValid();

	hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
	
	hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

	raDateUtil rdu = raDateUtil.getraDateUtil();

	String what_kind_of_dokument = ""; // Odre\u0111uje koji tip dokumenta

	// radimo

	String detail_titel_jed = "";

	String detail_titel_mno = "";

	String master_titel = "";
	
	rajpIzlazMPTemplate MP;

	rajpIzlazDPTemplate DP;

	String srcString;
	String keyforRN = null;

	Integer Broj;

	short nStavka; // redni broj stavke

	public boolean checkLimit = false;

	Rbr rbr = Rbr.getRbr();

	String delCSKL = "";

	String delGOD = "";

	String delVRDOK = "";

	int delBRDOK = 0;

	String ckey = "";

	String idStavke = "";
	
	String idVeze = "";

	int delCART = 0;

	boolean haveSB = false;

	boolean isRabatCallBefore = false;

	boolean isZavtrCallBefore = false;

	boolean isRabatShema = true;

	boolean isKupArtExist = false;

	boolean isCijenikExist = false;

	boolean isZavtrShema = true;

	boolean isEndedCancel = false; // ovo je pokušaj ispravka greške

	boolean isMasterInitIspis = false;

	boolean isDetailInitIspis = false;

	boolean isUsluga4Delete = false;

	boolean bPonudaZaKupca = false;

	boolean isVTtext = false;

	boolean isVTtextzag = false;
	
	boolean isTranzit = false;
	
	boolean isMinusAllowed = false;

	int lastCparNavigated = -1;

	short delRbr = -1;

	int oldCPAR = -1;

	boolean isUsluga = false;
	boolean hideKup = false;

	// private String vrzal="";

	// ab.f za brisanje stavki izdatnica prenesenih iz RNL
	protected int delRbsrn, delRbsid;

	protected String delCradnal = "";

	java.math.BigDecimal Nula = new java.math.BigDecimal(0);

	raExtraDBManipulation rEDM = new raExtraDBManipulation();

	java.math.BigDecimal Sto = new java.math.BigDecimal(100);

	java.math.BigDecimal Tmp = new java.math.BigDecimal(0);

	allPorezi alp = allPorezi.getallPorezi();

	LinkClass lc = LinkClass.getLinkClass();

	raKalkulBDDoc rKD = new raKalkulBDDoc();

	public QueryDataSet vttext = null;

	public QueryDataSet vttextzag = null;

	public QueryDataSet vtrabat = null;

	private raPozivNaBroj pnb = raPozivNaBroj.getraPozivNaBrojClass();

	private raPrenosVT rPVT = null;

	String god4del;

	String cskl4del;

	int cart4del;

	String csklart4del;

	String key4del;

	String rezkoldel;

	public raPrenosVT getrPVT() {
		if (rPVT == null) {
			rPVT = new raPrenosVT();
		}
		return rPVT;
	}

	private String key4VeznaTabela = "";

	raDDodRab rDR = null;

	public raDDodRab getrDR() {
		if (rDR == null) {
			rDR = new raDDodRab((Frame) raDetail.getFrameOwner(), this,
					"Dodatni popust", true) {
				public void afterJob() {
					jpRabat_afterJob();
				}
			};
		}
		return rDR;
	}

	raDDodZT rDZT = null;

	public raDDodZT getrDZT() {
		if (rDZT == null) {
			rDZT = new raDDodZT(this, "Dodatni zavisni troškovi", true) {
				public void afterJob() {
					jpZatr_afterJob();
				}
			};
		}
		return rDZT;
	}

	hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);

	QueryDataSet tmpCijenik = new QueryDataSet();

	QueryDataSet tmpKupArt = new QueryDataSet();

	BigDecimal tmpNCfromArtikl = new BigDecimal(0);

	BigDecimal tmpVCfromArtikl = new BigDecimal(0);

	BigDecimal tmpMCfromArtikl = new BigDecimal(0);

	BigDecimal tmpIPRODSP = Aus.zero2;
	BigDecimal tmpIRAZ = Aus.zero2;

	hr.restart.util.lookupData lD = hr.restart.util.lookupData.getlookupData();

	public TypeDoc TD = TypeDoc.getTypeDoc();

	// public hr.restart.robno.jpPreselectDoc pressel = null; // sranje staro
	public jpPreselectDocWraper pressel = new jpPreselectDocWraper();

	QueryDataSet qDS;

	allSelect aSS = new allSelect();

	QueryDataSet qds4pjpar;

	raFilterPj rFP = new raFilterPj();

	raControlDocs rCD = new raControlDocs();

	String defNacpl = hr.restart.sisfun.frmParam.getParam("robno", "defNacpl",
			"", "Predefinirani naèin plaæanja");

	String dodatak = "";

	String dodatakRN = "";

	SecondChooser dcz = null;

	/*
	 * new SecondChooser("Odabir dokumenta za prijenos"){ public void afterOK(){
	 * afterOKSC(); } };
	 */
	raNavAction rnvCopyDoce2; // /makniti

	// raNavAction rnvCopyDoce = new raNavAction("Prijenos
	// dokumenta",raImages.IMGSENDMAIL,java.awt.event.KeyEvent.VK_F7) {
	// public void actionPerformed(ActionEvent e) {
	// keyActionMaster();
	// }
	// };
	raNavAction rnvNormaArt = new raNavAction("Normirani artikl",
			raImages.IMGHISTORY, java.awt.event.KeyEvent.VK_F7) {
		public void actionPerformed(ActionEvent e) {
			forNormArt();
		}
	};

	raNavAction rnvElementi = new raNavAction("Elementi", raImages.IMGVOLUME,
			java.awt.event.KeyEvent.VK_F11) {
		public void actionPerformed(ActionEvent e) {
			forElementi();
		}
	};

	raNavAction rnvDellAllStav = new raNavAction("Brisanje svih stavaka",
			raImages.IMGDELALL, java.awt.event.KeyEvent.VK_F3,
			java.awt.event.KeyEvent.CTRL_MASK) {
		public void actionPerformed(ActionEvent e) {
			keyActionDellAllStav();

		}
	};
	
	raNavAction rnvKartica = new raNavAction("Kartica artikla",
        raImages.IMGMOVIE, java.awt.event.KeyEvent.VK_F11) {
      public void actionPerformed(ActionEvent e) {
        keyActionShowKartica();
      }
	};
    
    raNavAction rnvAkcija = new raNavAction("Akcija da/ne",
        raImages.IMGTIP, java.awt.event.KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        akcijaToggle();
      }
    };
    
    String akcijaPrefix = null;
    void checkAkcijaPrefix() {
      akcijaPrefix = frmParam.getParam("robno", "akcijaPrefiks", "",
          "Prefiks naziva artikla za artikle na akciji (prazno za onemogucavanje)");
    }
    
    public void akcijaToggle() {
      if (akcijaPrefix == null || akcijaPrefix.length() == 0) return;
      if (getDetailSet().rowCount() == 0 || raDetail.getMode() != 'B') return;
      String oldNaz = getDetailSet().getString("NAZART");
      if (oldNaz.startsWith(akcijaPrefix))
        getDetailSet().setString("NAZART", oldNaz.substring(akcijaPrefix.length()).trim());
      else getDetailSet().setString("NAZART", akcijaPrefix + " " + oldNaz);
      try {
        getDetailSet().saveChanges();
      } catch (Exception e) {
        getDetailSet().setString("NAZART", oldNaz);
      }
      ((JraTable2) raDetail.getJpTableView().getMpTable()).revalidate();
      ((JraTable2) raDetail.getJpTableView().getMpTable()).repaint();
    }
    
    void keyActionShowKartica() {
      util.showKartica(this);
    }

	public void keyActionDellAllStav() {
		if (javax.swing.JOptionPane.showConfirmDialog(this,
				"Obrisati sve stavke ?", "Upit",
				javax.swing.JOptionPane.YES_NO_OPTION,
				javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION) {
			deleteAllDoc rAD = new deleteAllDoc();
			rAD.delStavke(getDetailSet());
			// int position =getMasterSet().getRow();
			// getMasterSet().refresh();
			getMasterSet().refetchRow(getMasterSet());
			raMaster.getJpTableView().fireTableDataChanged();
			getDetailSet().refresh();
			raDetail.getJpTableView().fireTableDataChanged();
			// getMasterSet().goToClosestRow(position);

		}
	}

	raNavAction rnvDellAll = new raNavAction("Brisanje više ra\u010Duna",
			raImages.IMGDELALL, java.awt.event.KeyEvent.VK_F3,
			java.awt.event.KeyEvent.SHIFT_MASK) {
		public void actionPerformed(ActionEvent e) {
			keyActionDellAll();
		}
	};

	public void keyActionDellAll() {
		deleteAllDoc rAD = new deleteAllDoc() {
			public void afterDelAll() {
				getMasterSet().refresh();
				raMaster.getJpTableView().fireTableDataChanged();
				getDetailSet().refresh();
				raDetail.getJpTableView().fireTableDataChanged();
			}
		};
		rAD.delZaglav(getMasterSet());
	}

	getNorArt gNA = new getNorArt() {
		public void myPressOK() {
			normativPresOK();
		}
	};

	hr.restart.swing.raTableColumnModifier TCM;

	hr.restart.swing.raTableColumnModifier TCM1;

	hr.restart.swing.raTableColumnModifier TCM2;

	hr.restart.swing.raTableColumnModifier TCMORGS;

	hr.restart.swing.raTableColumnModifier TCMOVrtr;

	public void addButtons(boolean copy, boolean norma) {
		// if (copy) raMaster.addOption(rnvCopyDoce,4);
		if (norma)
			raDetail.addOption(rnvNormaArt, 4);
	}

	abstract public void initialiser();

	public raIzlazTemplate() {

		super(1, 3);
		initialiser();
		// setUserCheck(true);
		
		hideKup =  (what_kind_of_dokument.equals("GOT") ||
            what_kind_of_dokument.equals("GRN")) &&
            frmParam.getParam("robno", "kupacHack", "N",
                "Omoguæiti skrivanje kupca na gotovinskim raèunima (D,N)").equals("D");
		
		setUserCheck(hr.restart.sisfun.frmParam
				.getParam("robno", "userCheck", "D",
						"Da li se provjerava korisnik kod izmjene dokumenta (D/N)")
				.equalsIgnoreCase("D"));
		lc.setUseBigDecimal(true);
		lc.setGreska(false);
		domval = hr.restart.util.Util.getNewQueryDataSet(
				"select oznval from VALUTE where strval='N' ", true).getString(
				"OZNVAL");

		OpenWhatWeNeed();

		TCM = new hr.restart.swing.raTableColumnModifier("CPAR", new String[] {
				"CPAR", "NAZPAR" }, new String[] { "CPAR" }, dm.getPartneri()) {
			public int getMaxModifiedTextLength() {
				return 27;
			}
		};

		TCM1 = new hr.restart.swing.raTableColumnModifier("PJ", new String[] {
				"PJ", "NAZPJ" }, new String[] { "CPAR", "PJ" }, dm.getPjpar());
		TCM2 = new hr.restart.swing.raTableColumnModifier("CKUPAC",
				new String[] { "CKUPAC", "IME", "PREZIME" },
				new String[] { "CKUPAC" }, dm.getKupci()) {
		  Variant var = new Variant();
		  public void modify() {
		    if (!hideKup) {
		      super.modify();
		      return;
		    }
		    ((JraTable2) getTable()).getDataSet().getVariant("AKTIV", getRow(), var);
		    if (var.getString().equals("D")) super.modify();
		    else setComponentText("");
		  }
		};
		TCM2.ostaliVeznici = " ";
		/*
		 * TCMORGS = new hr.restart.swing.raTableColumnModifier("CORG", new
		 * String [] {"CORG","NAZIV"}, new String[] {"CORG"},
		 * dm.getOrgstruktura()); /* TCMOVrtr = new
		 * hr.restart.swing.raTableColumnModifier("CVRTR", new String []
		 * {"CVRTR","NAZIV"}, new String[] {"CVRTR"}, dm.getVrtros());
		 */

		raMaster.removeRnvCopyCurr();
		raDetail.removeRnvCopyCurr();

		raMaster.getJpTableView().addTableModifier(TCM);
		raMaster.getJpTableView().addTableModifier(TCM1);
		raMaster.getJpTableView().addTableModifier(TCM2);

		raMaster.installSelectionTracker("BRDOK");
		// raMaster.getJpTableView().addTableModifier(TCMORGS);
		// raMaster.getJpTableView().addTableModifier(TCMOVrtr);
        
        checkAkcijaPrefix();
        if (akcijaPrefix != null && akcijaPrefix.length() > 0)
          raDetail.addOption(rnvAkcija, 4);
        
		setUpfrmDokIzlaz();
		rCD.setisNeeded(hr.restart.sisfun.frmParam.getParam("robno",
				"kontKalk", "D",
				"Kontrola ispravosti redoslijeda unosa dokumenata")
				.equalsIgnoreCase("D"));
		setKumulativ(); // Siniša
	}

	public void OpenWhatWeNeed() {
	}

	public void setUpfrmDokIzlaz() {

		this.setMasterKey(Util.mkey);
		this.setDetailKey(Util.dkey);
		raMaster.getNavBar().getColBean().setSaveSettings(true);
		RestSetup();
	}

	public void beforeShowMaster() {
	  
	  isPopustMC = frmParam.getParam("robno", "popustuFMC", "N",
	      "Popust s artikla uraèunati u FMC (D,N)").equalsIgnoreCase("D");
	  
	    String cskl = pressel.getSelRow().getString("CSKL");
	    String additional = "";
	    if (cskl.length() > 0) {
	       if (TD.isCsklSklad(what_kind_of_dokument) && !isOJ) {
	         if (lD.raLocate(dm.getSklad(), "CSKL", cskl))
	           additional = "  skladište " + cskl + " - " + 
	               dm.getSklad().getString("NAZSKL");
	       } else {
	         if (lD.raLocate(dm.getOrgstruktura(), "CORG", cskl))
	           additional = "  org. jedinica " + cskl + " - " +
	               dm.getOrgstruktura().getString("NAZIV");
	       }
	    }
	    
	    isTranzit = !isOJ && cskl.length() > 0 && 
	        TD.isCsklSklad(what_kind_of_dokument) && 
	        lD.raLocate(dm.getSklad(), "CSKL", cskl) &&
	        dm.getSklad().getString("VRSKL").equals("Z");
	    
	    isMinusAllowed = frmParam.getParam("robno", "allowMinus", "N",
	        "Dopustiti odlazak u minus na izlazima (D,N)?").equals("D");
	        
	        
	  
		setNaslovMaster(master_titel + additional);
		setNaslovDetail(detail_titel_mno);
	}

	public void RestPanelSetup() {
		DP.addRest();
	}

	public void RestPanelMPSetup() {
		// MP.setupOne();
	}

	public void RestSetup() {

		MP = new rajpIzlazMPTemplate(what_kind_of_dokument,
				(raIzlazTemplate) this);
		DP = new rajpIzlazDPTemplate(what_kind_of_dokument,
				(raIzlazTemplate) this);
		RestPanelMPSetup();
		RestPanelSetup();
		setNaslovMaster(master_titel);
		setNaslovDetail(detail_titel_mno);
		// this.setVisibleColsMaster(new int[] {4,5,6,7});
		ConfigViewOnTable();
		setJPanelMaster(MP);
		setJPanelDetail(DP);
	}

	public void ConfigViewOnTable() {
		this.setVisibleColsMaster(new int[] { 4, 5, 6, 44, 32, 34 }); // Requested
		// by
		// Mladen
		// (Siniša)
		this
				.setVisibleColsDetail(new int[] { 4,
						Aut.getAut().getCARTdependable(5, 6, 7), 8, 11, 16, 12,
						19, 24 });
	}

	/**
	 * overajda se
	 */
	public void keyAction4GOT() {
	}

	public void EntryPointMaster(char mode) {
        cpar = -1;
		if (mode == 'I') {
			initTmpDataSet();
		}
	}

	public boolean DeleteCheckMaster() {
		prepareOldMasterValues();
		if (!LocalDeleteCheckMaster())
			return false;
		boolean returnValue = true;
		srcString = util.getSeqString(getMasterSet());
		returnValue = util.checkSeq(srcString, Integer.toString(getMasterSet()
				.getInt("BRDOK")));
		if (returnValue) {
			if (!this.getDetailSet().isEmpty()) {
				javax.swing.JOptionPane.showConfirmDialog(raMaster.getWindow(),
						"Nisu pobrisane stavke dokumenta !", "Gre\u0161ka",
						javax.swing.JOptionPane.DEFAULT_OPTION,
						javax.swing.JOptionPane.ERROR_MESSAGE);
				returnValue = false;
			}
		}

		if (returnValue)
			key4VeznaTabela = raPrenosVT.makeKey(getMasterSet().getString(
					"CSKL"), getMasterSet().getString("VRDOK"), getMasterSet()
					.getString("GOD"), getMasterSet().getInt("BRDOK"));
		else
			key4VeznaTabela = "";

		if (returnValue) {
			delCSKL = getMasterSet().getString("CSKL");
			delVRDOK = getMasterSet().getString("VRDOK");
			delGOD = getMasterSet().getString("GOD");
			delBRDOK = getMasterSet().getInt("BRDOK");
		} else {
			delCSKL = "";
			delGOD = "";
			delVRDOK = "";
			delBRDOK = 0;
		}
		return returnValue;
	}

	public boolean LocalDeleteCheckMaster() {
		return true;
	}

	public boolean doBeforeSaveDetail(char mode) {

		if (mode == 'N') {
			cskl2csklart();
			getDetailSet().setString("ID_STAVKA",
                raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
                        "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
			maintanceRabat(true);
		} else if (mode == 'I') {
			cskl2csklart();
			maintanceRabat(false);
		} else if (mode == 'B') {
			// deleteRabat();
		}
		if (mode != 'B') 
			SanityCheck.basicStdoki(getDetailSet());
		return true;
	}

	public void oneRabat() {
		if (getDetailSet().getBigDecimal("UPRAB").doubleValue() != 0) {
			vtrabat.insertRow(false);
			vtrabat.setString("CSKL", getDetailSet().getString("CSKL"));
			vtrabat.setString("VRDOK", getDetailSet().getString("VRDOK"));
			vtrabat.setString("GOD", getDetailSet().getString("GOD"));
			vtrabat.setInt("BRDOK", getDetailSet().getInt("BRDOK"));
			vtrabat.setShort("RBR", (short) getDetailSet().getInt("RBSID"));
			vtrabat.setShort("LRBR", (short) 1);
			vtrabat.setString("CRAB", hr.restart.sisfun.frmParam.getParam(
					"robno", "defcrab", "1",
					"Predefinirana šifra rabata stavke"));
			vtrabat.setString("RABNARAB", "N");
			vtrabat.setBigDecimal("PRAB", getDetailSet()
							.getBigDecimal("UPRAB"));
			vtrabat.setBigDecimal("IRAB", getDetailSet()
							.getBigDecimal("UIRAB"));
		}
	}

	public BigDecimal sumaRabata(DataSet ds) {
		BigDecimal postorabat = Aus.zero2;
		BigDecimal tmppostorabat = Aus.zero2;
		BigDecimal sto = new BigDecimal("100.00");
		for (ds.first(); ds.inBounds(); ds.next()) {
			if (ds.getString("RABNARAB").equalsIgnoreCase("D")) {
				tmppostorabat = postorabat;
				tmppostorabat = (new BigDecimal("100.00").subtract(postorabat))
						.multiply(ds.getBigDecimal("PRAB"));
				tmppostorabat = tmppostorabat.divide(sto, 2,
						BigDecimal.ROUND_HALF_UP);
				postorabat = postorabat.add(tmppostorabat);

			} else {
				postorabat = postorabat.add(ds.getBigDecimal("PRAB"));
			}
		}
		return postorabat;
	}

	public void maintanceRabat(boolean novi) {
		vtrabat = hr.restart.util.Util
				.getNewQueryDataSet("SELECT * FROM vtrabat where cskl ='"
						+ getDetailSet().getString("CSKL") + "' AND VRDOK='"
						+ getDetailSet().getString("VRDOK") + "' AND GOD='"
						+ getDetailSet().getString("GOD") + "' AND BRDOK="
						+ getDetailSet().getInt("BRDOK") + " AND rbr ="
						+ getDetailSet().getInt("RBSID"));

		BigDecimal sumarabat = Aus.zero2;
		// BigDecimal postorabat = Aus.zero2;
		// BigDecimal tmppostorabat = Aus.zero2;
		BigDecimal osnovica = Aus.zero2;
		BigDecimal iznosrabat = Aus.zero2;
		BigDecimal sto = new BigDecimal("100.00");

		if (rDR == null || rDR.getDPDataSet().getRowCount() == 0) {
			if (vtrabat.getRowCount() == 0) {
				vtrabat.deleteAllRows();
				oneRabat();
				return;
			} else {
				if (sumaRabata(vtrabat).compareTo(
						getDetailSet().getBigDecimal("UPRAB")) != 0) {
					vtrabat.deleteAllRows();
					oneRabat();
				}
				return;
			}
		}

		vtrabat.deleteAllRows();
		/*
		 * for
		 * (rDR.getDPDataSet().first();rDR.getDPDataSet().inBounds();rDR.getDPDataSet().next()){
		 * if (rDR.getDPDataSet().getString("RABNARAB").equalsIgnoreCase("D")){
		 * tmppostorabat = postorabat; tmppostorabat = (new
		 * BigDecimal("100.00").subtract(postorabat)).multiply(
		 * rDR.getDPDataSet().getBigDecimal("PRAB")); tmppostorabat =
		 * tmppostorabat.divide(sto,2,BigDecimal.ROUND_HALF_UP); postorabat =
		 * postorabat.add(tmppostorabat); } else { postorabat =
		 * postorabat.add(rDR.getDPDataSet().getBigDecimal("PRAB")); } }
		 */

		if (sumaRabata(rDR.getDPDataSet()).compareTo(
				getDetailSet().getBigDecimal("UPRAB")) != 0) {
			oneRabat();
			return;
		}

		for (rDR.getDPDataSet().first(); rDR.getDPDataSet().inBounds(); rDR
				.getDPDataSet().next()) {
			vtrabat.insertRow(false);
			vtrabat.setString("CSKL", getDetailSet().getString("CSKL"));
			vtrabat.setString("VRDOK", getDetailSet().getString("VRDOK"));
			vtrabat.setString("GOD", getDetailSet().getString("GOD"));
			vtrabat.setInt("BRDOK", getDetailSet().getInt("BRDOK"));
			vtrabat.setShort("RBR", (short) getDetailSet().getInt("RBSID"));
			vtrabat.setShort("LRBR", rDR.getDPDataSet().getShort("LRBR"));
			vtrabat.setString("CRAB", rDR.getDPDataSet().getString("CRAB"));

			vtrabat.setString("RABNARAB", rDR.getDPDataSet().getString(
					"RABNARAB"));
			vtrabat.setBigDecimal("PRAB", rDR.getDPDataSet().getBigDecimal(
					"PRAB"));

			osnovica = getDetailSet().getBigDecimal("INETO");
			iznosrabat = Aus.zero2;
			if (rDR.getDPDataSet().getString("RABNARAB").equalsIgnoreCase("D")) {
				osnovica = osnovica.subtract(sumarabat);
			}
			iznosrabat = osnovica.multiply(rDR.getDPDataSet().getBigDecimal(
					"PRAB"));
			iznosrabat = iznosrabat.divide(new BigDecimal("100.00"), 2,
					BigDecimal.ROUND_HALF_UP);
			vtrabat.setBigDecimal("IRAB", iznosrabat);
			sumarabat = sumarabat.add(iznosrabat);
		}
	}

	public void deleteRabat() {
		String sqlquery = "SELECT * FROM vtrabat where cskl ='"
				+ getDetailSet().getString("CSKL") + "' AND VRDOK='"
				+ getDetailSet().getString("VRDOK") + "' AND GOD='"
				+ getDetailSet().getString("GOD") + "' AND BRDOK="
				+ getDetailSet().getInt("BRDOK") + " AND rbr ="
				+ getDetailSet().getInt("RBSID");
		vtrabat = hr.restart.util.Util.getNewQueryDataSet(sqlquery);

		vtrabat.deleteAllRows();
	}

	public boolean doBeforeSaveMaster(char mode) {

		if (mode == 'N') {
			util.getBrojDokumenta(getMasterSet());
			getMasterSet().setString("PNBZ2",
					pnb.getPozivNaBroj(getMasterSet()));
			if (!extrasave()) return false;
		} else if (mode == 'I') {
			getMasterSet().setString("CUSER", cuser);
		}
		if (mode != 'B') 
			SanityCheck.basicDoki(getMasterSet());
		return true;
	}

	public void revive() {
		// sreðivanje prijenosa
	  
		//VarStr filter = new VarStr();
		String[] kkey = rCD.getKeyColumns(getMasterSet().getTableName());
		
		
		/*for (int i = 0; i < kkey.length; i++) {
			if (i != kkey.length - 1) {
				filter.append(kkey[i]).append("||'-'||");
			} else {
				filter.append(kkey[i]).append("||'-'='");
			}
		}*/

		QueryDataSet qdsprij = VTprijenos.getDataModule().getTempSet(
				"KEYDEST='" + key4delZag + "'");
		qdsprij.open();
		if (qdsprij.getRowCount() != 0) {
			for (qdsprij.first(); qdsprij.inBounds(); qdsprij.next()) {
			    /*System.out.println(filter.toString() + qdsprij.getString("KEYSRC") + "'");
				QueryDataSet zaglavlja = doki.getDataModule().getTempSet(
						filter.toString() + qdsprij.getString("KEYSRC") + "'");
				zaglavlja.open();*/
			    String[] vals = new VarStr(qdsprij.getString("KEYSRC")).splitTrimmed('-');
			    QueryDataSet zaglavlja = doki.getDataModule().getTempSet(
			        kkey.length > vals.length ? Condition.nil :
			        Condition.whereAllEqual(kkey, vals));
			    zaglavlja.open();

				if (zaglavlja.getRowCount()>0) {
					if (zaglavlja.hasColumn("STATIRA") != null) {
						zaglavlja.setString("STATIRA", "N");
						raTransaction.saveChanges(zaglavlja);
					}
				} else {
					QueryDataSet radninal = RN.getDataModule().getTempSet("CFAKTURE='"
						+ keyforRN + "'");
					radninal.open();
System.out.println(radninal.getQuery().getQueryString());					
ST.prn(radninal);					
					if (radninal.getRowCount()>0) {
						if (radninal.hasColumn("STATUS") != null) {
						    System.out.println("DOBRO JE");
                            if (radninal.getString("STATUS").equalsIgnoreCase("Z"))
                              radninal.setString("STATUS", "O");
                            else radninal.setString("STATUS", "P");
							radninal.setString("CFAKTURE", "");
							raTransaction.saveChanges(radninal);
						}
					}
				}
				qdsprij.deleteRow();
			}
			raTransaction.saveChanges(qdsprij);
		}
	}

	final public boolean doWithSaveMaster(char mode) {

		savebrdok = getMasterSet().getInt("BRDOK");
		if (mode != 'B') {
			myprepStatement();
			if (vttextzag != null && isVTtextzag) {
				isVTtextzag = false;
				if (mode == 'N') {
					vttextzag.setString("CKEY", rCD.getKey(getMasterSet()));
				}
				raTransaction.saveChanges(vttextzag);
				raMaster.markChange("vttext");
			}
		}

		if (mode == 'B') { // Brisanje mastera
			revive();
			/*dm.getVTText().open();
			dm.getVTText().refresh();*/
			if (lD.raLocate(dm.getVTText(), new String[] { "CKEY" },
					new String[] { key4delZag })) {
				dm.getVTText().deleteRow();
				raTransaction.saveChanges(dm.getVTText());
				raMaster.markChange(dm.getVTText());
			}

			try {
				util.delSeqCheck(srcString, true, delBRDOK); // / transakcija
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}

			try {
				rEDM.DeleteVTrabat(delCSKL, delVRDOK, delGOD, delBRDOK, 0);
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
			try {
				rEDM.DeleteVTzavtr(delCSKL, delVRDOK, delGOD, delBRDOK, 0);
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
			try {
				getrPVT().DeleteLink(key4VeznaTabela);
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}
		// else if (mode == 'N') {
		// return extrasave();
		// }

		return true;
	}

	public boolean extrasave() {
		return true;
	}

	boolean insideAfterAfter = false;

	public void AfterAfterSaveMaster(char mode) {
		if (insideAfterAfter)
			return;
		try {
			insideAfterAfter = true;
			vttextzag = null;
			// int rbr = getMasterSet().getRow();
			getMasterSet().refresh();
			lD.raLocate(getMasterSet(), "BRDOK", String.valueOf(savebrdok));
			System.out.println("DOK " + getMasterSet().getString("VRDOK") + "-"
					+ getMasterSet().getInt("BRDOK"));
			// getMasterSet().goToRow(rbr);
			if (mode == 'N') {
				rEDM.InsertVTrabat(getMasterSet(), 0, getMasterSet().getString(
						"CSHRAB"));
				rEDM.InsertVTzavtr(getMasterSet(), 0, getMasterSet().getString(
						"CSHZT"));

			} else if (mode == 'I') {
				rEDM.DeleteVTrabat(getMasterSet(), (short) 0);
				rEDM.InsertVTrabat(getMasterSet(), 0, getMasterSet().getString(
						"CSHRAB"));
				rEDM.DeleteVTzavtr(getMasterSet(), (short) 0);
				rEDM.InsertVTzavtr(getMasterSet(), 0, getMasterSet().getString(
						"CSHZT"));
			}
		
			// UBER-HACK by ab.f (don't shoot me)
          if (mode == 'N' || (mode == 'I' && changeCpar != getMasterSet().getInt("CPAR"))) {
            if (getMasterSet().getString("VRDOK").equalsIgnoreCase("ROT") ||
                getMasterSet().getString("VRDOK").equalsIgnoreCase("RAC"))
              raUpdateCRM.addRac(getMasterSet().getString("CSKL"), getMasterSet().getInt("CPAR"));
            else if (getMasterSet().getString("VRDOK").equalsIgnoreCase("PON"))
              raUpdateCRM.addPon(getMasterSet().getString("CSKL"), getMasterSet().getInt("CPAR"));
          }
          superAfterAfterSaveMaster(mode);
      } finally {
        insideAfterAfter = false;
      }
	}

	public void superAfterAfterSaveMaster(char mode) {
		super.AfterAfterSaveMaster(mode);
	}

	// ab.f
	public void afterSetModeMaster(char oldm, char newm) {
		if (newm == 'B' && MP.panelBasic != null)
			MP.panelBasic.jpgetval.disableDohvat();
	}

	public void SetFocusMasterBefore() {
	}

    private Runnable proc;
    public void doOnFocusNovi(Runnable proc) {
        this.proc = proc;
    }
      
    int changeCpar;
	public void SetFokusMaster(char mode) {
		SetFocusMasterBefore();
		// if (MP.panelBasic == null)

		// if (getMasterSet().getInt("CPAR")==0 ||
		// getMasterSet().isAssignedNull("CPAR")){
		// MP.panelDodatni.jrfKO.setRaDataSet(
		// Kosobe.getDataModule().getTempSet("1=0"));
		// }

		if (mode == 'N') {
			pressel.copySelValues();
        }
        if (mode == 'I') changeCpar = getMasterSet().getInt("CPAR");  // ab.f

		if (MP.panelBasic != null) {
			MP.panelBasic.jpgetval.initJP(mode); // Dodao andrej 02-11-2001
			// 16:47
			MP.panelBasic.rpku.setAllowedUpdated(mode != 'B');
		} else {

		}

		// findBRDOK(); // premjestio ab.f
		if (mode == 'N') {
			SetFocusNovi();
		} else if (mode == 'I') {
			SetFocusIzmjena();
			initTmpDataSet();
		} else {
			initTmpDataSet();
		}
		findBRDOK(); // premjestio odavde: ab.f a ja vratio TV
        
        if (mode == 'N' && proc != null) {
          proc.run();
          proc = null;
        }
	}

	public void initTmpDataSet() {
		// SwingUtilities.
		MP.panelDodatni.jrfKO.setRaDataSet(Kosobe.getDataModule().getTempSet(
				Condition.equal("CPAR", getMasterSet().getInt("CPAR"))));

	}

	public void setPreSel(Object presel) {
		pressel.setPresel(presel);
	}

	public void BasicSetFocusNovi(boolean presssel) {
		if (presssel)
			pressel.copySelValues();
		getMasterSet().setShort("DDOSP", (short) 0);
		getMasterSet().setString("VRDOK", what_kind_of_dokument);
		getMasterSet().setTimestamp("DATDOK",
		    val.getPresToday(pressel.getSelRow()));
		getMasterSet().setTimestamp("DVO",
		    val.getPresToday(pressel.getSelRow()));
		getMasterSet().setTimestamp("DATDOSP",
		    val.getPresToday(pressel.getSelRow()));
		getMasterSet().setString("GOD",
				val.findYear(getMasterSet().getTimestamp("DATDOK")));
		getMasterSet().setString("CUSER",
				hr.restart.sisfun.raUser.getInstance().getUser());
		getMasterSet().setString("ZIRO", pnb.getZiroRN(getMasterSet()));
	}

	public void SetFocusNovi() {

		MP.panelDodatni.jrfKO.setRaDataSet(Kosobe.getDataModule().getTempSet(
				"1=0"));
		if (MP.panelBasicExt != null) {
			MP.panelBasicExt.jrfPJ.setText("");
			MP.panelBasicExt.jrfNAZPJ.setText("");
			MP.panelBasicExt.jtfPJOPIS.setText("");
		}
		vttextzag = null;

		// Dodao ab.f: resetirati zapamcenog partnera jer u suprotnom ne azurira
		// posto rabata kad se unesu dva racuna s istim partnerom zaredom.
		oldCPAR = -2;

		MP.EnabSetup();
		MP.setDefValue();
		defaultMasterData();
		BasicSetFocusNovi(false);
		SetFocusNoviExtends();
	}

	public void SetFocusIzmjena() {

		MP.EnabSetup();
		if (isStavkeExist())
			MP.EnabDisabifStavkeExist();
		oldCPAR = getMasterSet().getInt("CPAR");
		MP.EnabDisabforChange(false);
		SetFocusIzmjenaExtends();

	}

	public boolean LocalValidacijaMaster() {
		if (val.isEmpty(MP.panelBasic.jrfCPAR))
			return false;
		return true;
	}

	public boolean ValidacijaMasterExtend() {
		return true;
	}

	public boolean isKnjigDataOK() {

		String seqKnjig = "select max(datknj) as datknj from doki where "
				+ "vrdok='" + getMasterSet().getString("VRDOK") + "' and "
				+ "cskl='" + getMasterSet().getString("CSKL") + "' and god='"
				+ getMasterSet().getString("GOD") + "' and statknj='K'";
		QueryDataSet qdstmp = hr.restart.util.Util.getNewQueryDataSet(seqKnjig,
				true);
		if (qdstmp.getRowCount() == 0)
			return true;
     return !getMasterSet().getTimestamp("DATDOK").before(
         hr.restart.util.Util.getUtil().getFirstSecondOfDay(qdstmp.getTimestamp("DATKNJ")));
//		return rdu.isGrater(getMasterSet().getTimestamp("DATDOK"), qdstmp
//				.getTimestamp("DATKNJ"));
	}

	public boolean FirstPartValidDetail() {
		if (val.isEmpty(MP.panelBasic.jtfDATDOK))
			return false;
        if (frmParam.getParam("robno","docBefDatKnj","N","Dozvoliti izradu dokumenta u periodu koje je veæ knjižen ").equalsIgnoreCase("N")) {
		if (!isKnjigDataOK()) {
			javax.swing.JOptionPane.showMessageDialog(raMaster.getWindow(),
					"Datum u periodu koji je veæ knjižen !", "Greška",
					javax.swing.JOptionPane.ERROR_MESSAGE);
			MP.panelBasic.jtfDATDOK.requestFocus();
			return false;
		}
        }
		if (!ValidacijaMasterExtend())
			return false;
		return LocalValidacijaMaster();
	}

	public boolean ValidacijaMaster(char mode) {

		if (mode == 'I') {
			cuser = getMasterSet().getString("CUSER");
		}

		if (mode == 'N') {
			if (!ValidacijaLimit(new java.math.BigDecimal("0.00"),
					new java.math.BigDecimal("0.00")))
				return false;
		}
		setNull();
		if (FirstPartValidDetail()) {
			if (mode == 'N') {
				// getMasterSet().setString("ZIRO",pnb.getZiroRN(getMasterSet()));
				// getMasterSet().setString("rezkol",hr.restart.sisfun.frmParam.getParam("robno","rezkol"));
			}
			return true;
		} else
			return false;
	}

	public boolean LocalDeleteCheckDetail() {
		return true;
	}

	public void prepareOldMasterValues() {
		key4delZag = rCD.getKey(getMasterSet());
		keyforRN = getMasterSet().getString("CSKL")  + "-" + 
                   getMasterSet().getString("VRDOK") + "-" +
                   getMasterSet().getString("GOD")   + "-" + 
                   String.valueOf(getMasterSet().getInt("BRDOK"));
	}

	public void prepareOldDetailValues() {

		god4del = getDetailSet().getString("GOD");
		cskl4del = getDetailSet().getString("CSKL");
		cart4del = getDetailSet().getInt("CART");
		csklart4del = getDetailSet().getString("CSKLART");
		rezkoldel = getDetailSet().getString("REZKOL");
		key4del = rCD.getKey(getDetailSet());
		idStavke = getDetailSet().getString("ID_STAVKA");
		idVeze = getDetailSet().getString("VEZA");
	}

	public boolean DeleteCheckDetail() {

		prepareOldDetailValues();
		if (!LocalDeleteCheckDetail())
			return false;
		boolean returnValue = true;
		isUsluga4Delete = this.isUslugaOrTranzit();
		if (!isUsluga4Delete) {
			AST.findStanjeFor(getDetailSet(), isOJ);
			/*AST.findStanjeUnconditional(getDetailSet().getString("GOD"),
							getDetailSet().getString("CSKL"), getDetailSet()
									.getInt("CART"));*/
			rCD.prepareFields(getDetailSet());
			if (TD.isDocDiraZalihu(getDetailSet().getString("VRDOK"))) {
			  SanityCheck.stanjeArt(AST.gettrenSTANJE(), getDetailSet());
			  returnValue = rCD.testIzlaz4Del((DataSet) getDetailSet(), AST
						.gettrenSTANJE());
			}
		}
		if (returnValue) {
			delRbsrn = getDetailSet().getInt("RBSRN");
			delRbsid = getDetailSet().getInt("RBSID");
			delCradnal = getDetailSet().getString("CRADNAL");
			delCART = getDetailSet().getInt("CART");
			delRbr = getDetailSet().getShort("RBR");
			isUsluga = DP.rpcart.isUsluga();
			tmpIPRODSP = getDetailSet().getBigDecimal("IPRODSP");
			tmpIRAZ = getDetailSet().getBigDecimal("IRAZ");
			if (lD.raLocate(dm.getArtikli(), new String[] { "CART" },
					new String[] { getDetailSet().getInt("CART") + "" },
					com.borland.dx.dataset.Locate.CASE_INSENSITIVE)) {
				if (dm.getArtikli().getString("ISB").equals("D")) {
					haveSB = true;
					if (!dlgSerBrojevi.getdlgSerBrojevi().beforeDeleteSerBr(
							getDetailSet(), 'I')) {
						returnValue = false;
					}
				}
			}
			if (returnValue && !isUsluga4Delete) {
				rKD.stavka.Init();
				rKD.stavkaold.Init();
				// rKD.stavka.rezkol = getDetailSet().getString("REZKOL");
				lc.TransferFromDB2Class(getDetailSet(), rKD.stavkaold);
				lc.TransferFromDB2Class(AST.gettrenSTANJE(), rKD.stanje);
				rKD.KalkulacijaStanje(what_kind_of_dokument);
			}
		} else {
			javax.swing.JOptionPane.showMessageDialog(raDetail.getWindow(), rCD.errorMessage(),
					"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
			// raDM.jtfKOL.requestFocus();
		}
		ckey = "";
		if (returnValue) {
			ckey = rCD.getKey(getDetailSet());
		}
		if (returnValue)
			deleteRabat();

		return returnValue;
	}

	public boolean DeleteRabiZavtr() {
		boolean retValue = true;
		retValue = rEDM.DeleteVTrabat(getMasterSet(), delRbr);
		if (retValue)
			retValue = rEDM.DeleteVTzavtr(getMasterSet(), delRbr);
		return retValue;
	}

	public void SetFokusDetail(char mode) {

		DP.rpcart.enableNaziv();
		isRabatCallBefore = false;
		isZavtrCallBefore = false;
		if (mode == 'I') {
			DP.rpcart.findStanjeUnconditional();
			focusOffOn(mode == 'N');
			findCPOR(); // (ab.f)
			EntryDetail(mode);
			tmpIPRODSP = getDetailSet().getBigDecimal("IPRODSP");
			tmpIRAZ = getDetailSet().getBigDecimal("IRAZ");
			DP.jtfKOL.requestFocus();
			vttext = null;
		} else if (mode == 'N') {
			vttext = null;
			focusOffOn(mode == 'N');
			DP.rpcart.SetDefFocus();
			tmpIPRODSP = Aus.zero2;
			tmpIRAZ = Aus.zero2;
			EntryDetail(mode);
		} else if (mode == 'B') {
			DP.rpcart.setMode("B");
			DP.InitRaPanCartDP();
		}
	}

	void focusOffOn(boolean istina) {
		DP.rpcart.EnabDisab(istina);
		DP.setEnabledAll(!istina);
	}

	private void CopyCommonFieldsFromZaglavljeToStavke() {
		getDetailSet().setString("CSKL", getMasterSet().getString("CSKL"));
		getDetailSet().setString("VRDOK", getMasterSet().getString("VRDOK"));
		getDetailSet().setString("GOD", getMasterSet().getString("GOD"));
		getDetailSet().setInt("BRDOK", getMasterSet().getInt("BRDOK"));
	}

	public void EntryDetail(char mode) {

		isEndedCancel = false;
		if (mode == 'N') {
			findNSTAVKA();
			CopyCommonFieldsFromZaglavljeToStavke();
			getDetailSet().setShort("RBR", nStavka);
			getDetailSet().setInt("rbsid", rbr.getRbsID(getDetailSet()));
			rKD.stavka.Init();
			rKD.stavkaold.Init();
			rKD.stanje.Init();
			setupRabat();
			setupZavTr();
		} else if (mode == 'I') {

			setupRabat();
			setupZavTr();
			AST.findStanjeFor(getDetailSet(), isOJ);
			/*if (isOJ) {
				AST.findStanjeUnconditional(getDetailSet().getString("GOD"),
						getDetailSet().getString("CSKLART"), getDetailSet()
								.getInt("CART"));
			} else {
				AST.findStanjeUnconditional(getDetailSet().getString("GOD"),
						getDetailSet().getString("CSKL"), getDetailSet()
								.getInt("CART"));
			}*/
			DP.setEnabledAll(true);
			rKD.stanje.Init();
			lc.TransferFromDB2Class(getDetailSet(), rKD.stavka);
			lc.TransferFromDB2Class(getDetailSet(), rKD.stavkaold);
		}
	}

	/*public void SetMasterTitle() {
		if (raMaster.getMode() == 'N') {
			this.setNaslovMaster(master_titel);
		} else {
			this.setNaslovDetail((master_titel.concat(" br. ")
					+ getDetailSet().getString("VRDOK") + "-"
					+ getDetailSet().getString("CSKL").trim() + "/"
					+ getDetailSet().getString("GOD") + "-" + val
					.maskZeroInteger(
							new Integer(getDetailSet().getInt("BRDOK")), 6)));
		}
	}*/

	public void tabStateChangedDetail(int i) {
		if (i == 0) {
			setNaslovDetail((detail_titel_mno.concat(" br. ")
					+ getMasterSet().getString("VRDOK") + "-"
					+ getMasterSet().getString("CSKL").trim() + "/"
					+ getMasterSet().getString("GOD") + "-" + val
					.maskZeroInteger(
							new Integer(getMasterSet().getInt("BRDOK")), 6)));

		} else if (i == 1) {
			if (raDetail.getMode() == 'N') {
				setNaslovDetail((detail_titel_mno.concat(" br. ")
						+ getMasterSet().getString("VRDOK") + "-"
						+ getMasterSet().getString("CSKL").trim() + "/"
						+ getMasterSet().getString("GOD") + "-" + val
						.maskZeroInteger(new Integer(getMasterSet().getInt(
								"BRDOK")), 6)));
			} else {
				setNaslovDetail((detail_titel_jed.concat(" br. ")
						+ getDetailSet().getString("VRDOK") + "-"
						+ getDetailSet().getString("CSKL").trim() + "/"
						+ getDetailSet().getString("GOD") + "-" + val
						.maskZeroInteger(new Integer(getDetailSet().getInt(
								"BRDOK")), 6))
						+ "/" + new Integer(getDetailSet().getShort("RBR")));
			}
		}
	}

	public void SetDetailTitle(String something) {
		if (!something.equals("def")) {
			if (raDetail.getMode() == 'N') {
				setNaslovDetail((detail_titel_mno.concat(" br. ")
						+ getMasterSet().getString("VRDOK") + "-"
						+ getMasterSet().getString("CSKL").trim() + "/"
						+ getMasterSet().getString("GOD") + "-" + val
						.maskZeroInteger(new Integer(getMasterSet().getInt(
								"BRDOK")), 6)));
			} else {
				setNaslovDetail((detail_titel_jed.concat(" br. ")
						+ getDetailSet().getString("VRDOK") + "-"
						+ getDetailSet().getString("CSKL").trim() + "/"
						+ getDetailSet().getString("GOD") + "-" + val
						.maskZeroInteger(new Integer(getDetailSet().getInt(
								"BRDOK")), 6))
						+ "/" + new Integer(getDetailSet().getShort("RBR")));
			}
		} else {
			setNaslovDetail((detail_titel_mno.concat(" br. ")
					+ getMasterSet().getString("VRDOK") + "-"
					+ getMasterSet().getString("CSKL").trim() + "/"
					+ getMasterSet().getString("GOD") + "-" + val
					.maskZeroInteger(
							new Integer(getMasterSet().getInt("BRDOK")), 6)));
		}
	}

	public void AfterCancelDetail() {
		SetDetailTitle("def");
		isEndedCancel = true;
		int row = getDetailSet().getRow();
		getDetailSet().refresh();
		getDetailSet().goToClosestRow(row);
		vttext = null;

		// DP.rpcart.Clean();
	}

	/**
	 * ovo dolazi umjesto aftersavedetail jer se izvršava pod transakcijom !
	 * 
	 * @param mode -
	 *            vidi dokumentaciju od raMasterDetail
	 * @return - vidi dokumentaciju od raMasterDetail
	 */

	// public void recountDataSet(DataSet ds, String columnName, int deletedRB)
	// {
	// val.recountDataSet(ds,columnName,deletedRB,false,true);
	// }
	/*
	 * public boolean doBeforeSaveDetail(char mode){ }
	 */

	// ab.f za overridanje
	protected boolean AdditionalDeleteDetail() {
		return true;
	}

	public void brisiRezervaciju() {

	}

	public void brisiVezu() {
	  BigDecimal koliko = Aus.zero3;
      BigDecimal kolDOS = Aus.zero3;
	  if (idStavke != null && idStavke.trim().length() > 0) {
		QueryDataSet qds = stdoki.getDataModule().getTempSet(
		    Condition.equal("VEZA", idStavke));
		qds.open();
		for (qds.first(); qds.inBounds(); qds.next()) {
			qds.setString("VEZA", "");
			qds.setString("STATUS", "N");
			if (qds.getString("REZKOL").equalsIgnoreCase("D") &&
			    !TD.isDocDiraZalihu(qds.getString("VRDOK")))
			  koliko = koliko.add(qds.getBigDecimal("KOL"));
			if (TD.isDocDOS(qds.getString("VRDOK")))
				kolDOS = kolDOS.add(qds.getBigDecimal("KOL"));
		}
		if (qds.getRowCount() > 0)
			raTransaction.saveChanges(qds);
	  }
		
		if (isUsluga4Delete) return;

		if (!TD.isDocDiraZalihu(what_kind_of_dokument) &&
		    rKD.stavkaold.rezkol.equalsIgnoreCase("D"))
		  koliko = koliko.subtract(rKD.stavkaold.kol);
		
		System.out.println("koliko van ifa" + koliko);
		/*if (AST.gettrenSTANJE().getRowCount() != 1 ||
		    AST.gettrenSTANJE().getInt("CART") != delCART) {
			AST.findStanjeUnconditional(getMasterSet().getString("GOD"),
					getDetailSet().getString("CSKLART"), getDetailSet().getInt(
							"CART"));
		}*/

		if (AST.gettrenSTANJE().getRowCount() == 1 &&
		    AST.gettrenSTANJE().getInt("CART") == delCART) {
			Aus.add(AST.gettrenSTANJE(), "KOLREZ", koliko);
			Aus.add(AST.gettrenSTANJE(), "KOLSKLADIZ", kolDOS);
			Aus.sub(AST.gettrenSTANJE(), "KOLSKLAD", "KOLSKLADUL", "KOLSKLADIZ");
			System.out.println("koliko " + koliko);
			raTransaction.saveChanges(AST.gettrenSTANJE());
		} else System.out.println("pogrešno stanje?!?");
	}

	/*
	 * public void brisiRezervaciju() {
	 *  }
	 */
	public void dodajRezervaciju() {
      if (getDetailSet().getString("CSKLART").length() == 0 ||
          isUslugaOrTranzit() || raDetail.getMode() == 'B') return;
      if (!getDetailSet().getString("REZKOL").equalsIgnoreCase("D") &&
          (!rKD.stavkaold.rezkol.equalsIgnoreCase("D") ||
              raDetail.getMode() == 'N')) return;
      if (TD.isDocDiraZalihu(what_kind_of_dokument)) return;

      
      boolean nemaGa = !AST.findStanjeFor(getDetailSet(), isOJ);
      if (nemaGa) {
              AST.gettrenSTANJE().insertRow(false);
              AST.gettrenSTANJE().setString("GOD",
                      getMasterSet().getString("GOD"));
              AST.gettrenSTANJE().setString("CSKL",
                      getDetailSet().getString("CSKLART"));
              AST.gettrenSTANJE().setInt("CART",
                      getDetailSet().getInt("CART"));
              nulaStanje(AST.gettrenSTANJE());
          }

          lc.TransferFromDB2Class(AST.gettrenSTANJE(), rKD.stanje);
          if (raDetail.getMode()=='N'){
              if (!getDetailSet().getString("REZKOL").equalsIgnoreCase("D")) return;              
              rKD.stanje.kolrez = 
                  rKD.stanje.kolrez.add(rKD.stavka.kol);
          } else if (raDetail.getMode()=='I'){
              if (rKD.stavkaold.rezkol.equalsIgnoreCase("D")){
                  // vrati staru rezervaciju ako treba
                  rKD.stanje.kolrez = 
                      rKD.stanje.kolrez.subtract(rKD.stavkaold.kol);
              }
              if (getDetailSet().getString("REZKOL").equalsIgnoreCase("D")){
                  // stavi novu ako treba rezervaciju ako treba                   
                  rKD.stanje.kolrez = rKD.stanje.kolrez.add(rKD.stavka.kol);
              }
          }
          AST.gettrenSTANJE().setBigDecimal("KOLREZ",rKD.stanje.kolrez);
          if (nemaGa){
              AST.gettrenSTANJE().setBigDecimal("VC",getDetailSet().getBigDecimal("FC"));
              AST.gettrenSTANJE().setBigDecimal("MC",getDetailSet().getBigDecimal("FMCPRP"));             
          }
          raTransaction.saveChanges(AST.gettrenSTANJE());

    }   

	public void cskl2csklart() {
		getDetailSet().setString("CSKLART", getDetailSet().getString("CSKL"));
	}

	public boolean doWithSaveDetailBrisi() {
		boolean retValue = true;
		dm.getVTText().open();
		if (lD.raLocate(dm.getVTText(), new String[] { "CKEY" },
				new String[] { key4del })) {
			dm.getVTText().deleteRow();
			raTransaction.saveChanges(dm.getVTText());
			raDetail.markChange(dm.getVTText());
		}

		if (TD.isDocDiraZalihu(what_kind_of_dokument)) {
			if (!isUsluga4Delete) {
				lc.TransferFromClass2DB(AST.gettrenSTANJE(), rKD.stanje);
				rCD.brisanjeIzlaz(AST.gettrenSTANJE());
				raTransaction.saveChanges(AST.gettrenSTANJE());
			}
			isUsluga4Delete = false; // uvijek vrati na robu
		}
		//brisiRezervaciju();
		brisiVezu();
		if (haveSB) {
			prepDelete();
			dlgSerBrojevi.getdlgSerBrojevi().setTransactionActive(true);
			Object[] squels = dlgSerBrojevi.getdlgSerBrojevi()
					.getDeleteStringsforTransaction('I');
			for (int i = 0; i < squels.length; i++) {
				try {
					raTransaction.runSQL((String) squels[i]);
				} catch (Exception ex) {
					ex.printStackTrace();
					retValue = false;
					break;
				}
			}
			dlgSerBrojevi.getdlgSerBrojevi().returnOrgTransactionActive();
			haveSB = false;
		}
		// update master set-a
		if (retValue) {
			try {
			  if (TD.isDocFinanc(what_kind_of_dokument)) {
    			  Aus.sub(getMasterSet(), "UIRAC", tmpIPRODSP);
    				raTransaction.saveChanges(getMasterSet());
			  } else if (TD.isDocSklad(what_kind_of_dokument)) {
			    Aus.sub(getMasterSet(), "UIRAC", tmpIRAZ);
                raTransaction.saveChanges(getMasterSet());
			  }
			} catch (Exception ex) {
				ex.printStackTrace();
				retValue = false;
			}
		}
		// if (retValue)
		// retValue = DeleteRabiZavtr();
		if (retValue) {
			retValue = getrPVT().DeleteVTText(ckey);
			if (retValue) raDetail.markChange("VTtext");
		}
		if (retValue) {

			try {

				// DataSet tmpVtrabat =
				// rEDM.getVtrabat(getMasterSet().getString(
				// "CSKL"), getMasterSet().getString("VRDOK"),
				// getMasterSet().getString("GOD"), getMasterSet().getInt(
				// "BRDOK"));

				DataSet tmpVtzavtr = rEDM.getVtzavtr(getMasterSet().getString(
						"CSKL"), getMasterSet().getString("VRDOK"),
						getMasterSet().getString("GOD"), getMasterSet().getInt(
								"BRDOK"));
				raDetail.getJpTableView().enableEvents(false);
				val.recountDataSet(getDetailSet(), "rbr", delRbr, false); // i
				raDetail.getJpTableView().enableEvents(true);
				// ovo
				// je
				// sranje
				// za
				// vezne
				// tabele
				// val.recountDataSet(tmpVtrabat, "rbr", delRbr, false); //
				// vtrabat
				val.recountDataSet(tmpVtzavtr, "rbr", delRbr, false); // vtzavtr
				raTransaction.saveChanges(getDetailSet());
				if (vtrabat != null) {
					raTransaction.saveChanges(vtrabat);
					vtrabat = null;
				}

				// raTransaction.saveChanges((QueryDataSet) tmpVtrabat);
				raTransaction.saveChanges((QueryDataSet) tmpVtzavtr);
			} catch (Exception ex) {
				ex.printStackTrace();
				retValue = false;
			}
		}
		if (retValue)
			retValue = AdditionalDeleteDetail();
		return retValue;

	}

	public void extraStanje(char mode) {
	}

	public boolean doWithSaveDetail(char mode) {

		boolean retValue = true;

		extraStanje(mode);
		if (mode == 'N') {
			/*getDetailSet().setString(
					"ID_STAVKA",
					raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
							"vrdok", "god", "brdok", "rbsid" }, "stdoki"));
			raTransaction.saveChanges(getDetailSet());*/
			if (isTranzit)  {
			  getMasterSet().setString("STATIRA", "N");
		      raTransaction.saveChanges(getMasterSet());
			}
		}
		if (mode == 'I' || mode == 'N') {
			if (vtrabat != null) {
				raTransaction.saveChanges(vtrabat);
				vtrabat = null;
			}
		}

		if (mode == 'B') {
			doWithSaveDetailBrisi();
		} else {
			dlgSerBrojevi.getdlgSerBrojevi().setTransactionActive(true);
			dlgSerBrojevi.getdlgSerBrojevi().TransactionSave();
			dlgSerBrojevi.getdlgSerBrojevi().returnOrgTransactionActive();
			if (afterWish()) {
				if (TD.isDocDiraZalihu(getDetailSet().getString("VRDOK"))) {
					if (!DP.rpcart.isUsluga()) {
						retValue = UpdateStanje();
					}
				}
				if (retValue) {
					retValue = UpdateDoki();
					if (retValue) {
						retValue = addRabati();
						if (retValue) {
							retValue = addZavtr();
						}
					}
				}
			}

			// klju\u010Devi za upis i provjeru zadnje kalkulacije
			if (mode == 'N'
					&& (TD.isDocDiraZalihu(getDetailSet().getString("VRDOK")) || getDetailSet()
							.getString("VRDOK").equalsIgnoreCase("DOS"))) {
				nStavka = (short) (nStavka + 1);
				try {
					if (!DP.rpcart.isUsluga()) {
						lc.TransferFromClass2DB(AST.gettrenSTANJE(), rKD.stanje);
						rCD.unosIzlaz(getDetailSet(), AST.gettrenSTANJE()); // ???????
						raTransaction.saveChanges(getDetailSet());
						raTransaction.saveChanges(AST.gettrenSTANJE());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					retValue = false;
				}
			}

			if ((mode == 'N' || mode == 'I')) {
				dodajRezervaciju();
			}

			if (vttext != null && isVTtext) {
				isVTtext = false;
				raTransaction.saveChanges(vttext);
				raDetail.markChange("vttext");
			}
		}
		return retValue;
	}

	/**
	 * Metoda pridodaje iznos stavke iz stdoki na ukupni iznos u doki (kumulativ
	 * dokumenta)
	 * 
	 * @return ako je prošao vrati true
	 */
	public boolean UpdateDoki() {

		boolean retValue = true;
		try {
		  if (TD.isDocFinanc(what_kind_of_dokument)) {
		    Aus.addSub(getMasterSet(), "UIRAC",
		        getDetailSet(), "IPRODSP", tmpIPRODSP);
		    raTransaction.saveChanges(getMasterSet());
		  } else if (TD.isDocSklad(what_kind_of_dokument)) {
		    Aus.addSub(getMasterSet(), "UIRAC",
                getDetailSet(), "IRAZ", tmpIRAZ);
            raTransaction.saveChanges(getMasterSet());
		  }
		} catch (Exception e) {
			retValue = false;
		}

		return retValue;
	}

	/**
	 * Updatira stanje u ovisnosti od raznih stvari
	 * 
	 * @return ako je prošao vrati true
	 */
	public boolean UpdateStanje() {

		boolean retValue = true;

		try {
			if (!DP.rpcart.isUsluga()
					&& TypeDoc.getTypeDoc().isDocDiraZalihu(
							what_kind_of_dokument)) {
				lc.TransferFromClass2DB(AST.gettrenSTANJE(), rKD.stanje);
				raTransaction.saveChanges(AST.gettrenSTANJE());
			}
		} catch (Exception e) {
			retValue = false;
		}
		return retValue;
	}

	public void AfterSaveDetail(char mode) {
		vttext = null;
	}

	final public void AfterDeleteDetail() {
		if (getDetailSet().getRowCount() == 0) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					enableDetailNavBar();
				}
			});
		}
	}

	public boolean afterWish() {
		return true;
	}

	final public void EntryPointDetail(char mode) {
		DP.InitRaPanCartDP();
	}

	public boolean DodatnaValidacijaDetail() {

		if (val.isEmpty(DP.jtfKOL))
			return false;

		if (DP.jraFC.getDataSet().getBigDecimal(DP.jraFC.getColumnName())
				.compareTo(Aus.zero2) == 0) {

		}
		SanityCheck.basicStdoki(getDetailSet());
		/*
		 * if (val.isEmpty(DP.jraFC)) return false;
		 */
		return true;
	}

	public boolean ValidacijaDetail(char mode) {

		if (val.isEmpty(DP.rpcart.jrfCART))
			return false;
		if (ValidacijaStanje()) {
			if (!ValidacijaLimit(rKD.stavkaold.iprodsp, rKD.stavka.iprodsp))
				return false;
			if (!dlgSerBrojevi.getdlgSerBrojevi().findSB(DP.rpcart,
					getDetailSet(), 'I', mode)) {// 'I' - kao izlaz
				return false;
			}
			if (isNedozvoljenArtikl()) {
				javax.swing.JOptionPane.showMessageDialog(raDetail.getWindow(),
								"Za ovu vrstu dokumenta koristite nedozvoljen artikl !",
								"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} else {
			return false;
		}
      DP.rpcart.setExtraSklad(getDetailSet().getString("CSKLART"));

		return DodatnaValidacijaDetail();
	}

	public int nextRbr() {
		findNSTAVKA();
		return nStavka;
	}

	void findNSTAVKA() {
		nStavka = rbr.vrati_rbr("STDOKI", getMasterSet().getString("CSKL"),
				getMasterSet().getString("VRDOK"), getMasterSet().getString(
						"GOD"), getMasterSet().getInt("BRDOK"));
	}

	public boolean ValDPEscapeDetail(char mode) {

		vttext = null;
		if (raDetail.getMode() == 'N') {
			if (this.DP.rpcart.getCART().trim().equals("")) {
				return true;
			} else {
				ClearAll();
				// rki.Clean();
				setupRabat();
				setupZavTr();
				DP.setEnabledAll(false);
				DP.rpcart.EnabDisab(true);
				DP.rpcart.setCART();
				return false;
			}
		} else {
			return true;
		}
	}

	public String PrepSql(boolean detail, boolean usecond) {

		String sqldodat = "";

		if (detail) {

			sqldodat = "and doki.cskl='" + getMasterSet().getString("CSKL")
					+ "' " + "and doki.vrdok='"
					+ getMasterSet().getString("VRDOK") + "' "
					+ "and doki.god = '" + getMasterSet().getString("GOD")
					+ "' ";

			/*
			 * sqldodat="and
			 * doki.cskl='"+pressel.getSelRow().getString("CSKL")+"' "+ "and
			 * doki.vrdok='"+pressel.getSelRow().getString("VRDOK")+"' " + "and
			 * doki.god =
			 * '"+val.findYear(pressel.getSelRow().getTimestamp("DATDOK-to"))+"' "; //
			 * +"and doki.brdok ="+getMasterSet().getInt("BRDOK");
			 */
			Condition con = raMaster.getSelectCondition();
			if (con != null && con != Condition.none && usecond) {
				con.qualified("doki");
				sqldodat = sqldodat + " and " + con;

			} else if (con != Condition.none) {
				sqldodat = sqldodat + "and doki.brdok ="
						+ getMasterSet().getInt("BRDOK");
			}

		} else {
			if (!pressel.getSelRow().getString("CSKL").equals(""))
				sqldodat = "and doki.cskl='"
						+ pressel.getSelRow().getString("CSKL") + "' ";

			if (!pressel.getSelRow().getString("VRDOK").equals(""))
				sqldodat = sqldodat + "and doki.vrdok='"
						+ pressel.getSelRow().getString("VRDOK") + "' ";

			if (pressel.getSelRow().getInt("CPAR") != 0)
				sqldodat = sqldodat + "and doki.cpar="
						+ pressel.getSelRow().getInt("CPAR") + " ";

			if (!pressel.getSelRow().getTimestamp("DATDOK-from").equals("")) {
				sqldodat = sqldodat
						+ "and doki.datdok >= '"
						+ rdu.PrepDate(pressel.getSelRow().getTimestamp(
								"DATDOK-from"), true) + "' ";
			}

			if (!pressel.getSelRow().getTimestamp("DATDOK-to").equals("")) {
				sqldodat = sqldodat
						+ "and doki.datdok <= '"
						+ rdu.PrepDate(pressel.getSelRow().getTimestamp(
								"DATDOK-to"), false) + "' ";
			}
		}
		return sqldodat;
	}

	boolean bprepRunReport = false;

	public void prepRunReport() {
		bprepRunReport = true;
	}

	abstract public void MyaddIspisMaster();

	abstract public void MyaddIspisDetail();

	public void Funkcija_ispisa_master() {

		if (!isDetailExist())
			return;
		reportsQuerysCollector.getRQCModule().ReSql(PrepSql(true, true),
				what_kind_of_dokument);
		// reportsQuerysCollector.getRQCModule().ReSql(PrepSql(true),what_kind_of_dokument);
		// ST.prn(reportsQuerysCollector.getRQCModule().getQueryDataSet());
		if (!isMasterInitIspis) {
			isMasterInitIspis = true;
			MyaddIspisMaster();
		}
		if (!bprepRunReport)
			prepRunReport();
		super.Funkcija_ispisa_master();
	}

	public boolean isDetailExist() {

		// ?? ne sijecam se zasto koristim dm.getStdoki pretpostavljam zbog
		// gubitka
		// pokazivaca sloga stavke. Ne svidja mi se uglavnom ovo rijesenje ispod
		/*
		 * dm.getStdoki().open(); dm.getStdoki().refresh(); if
		 * (lD.raLocate(dm.getStdoki(),new String[]
		 * {"CSKL","GOD","VRDOK","BRDOK"}, new
		 * String[]{getMasterSet().getString("CSKL"),
		 * getMasterSet().getString("GOD"),getMasterSet().getString("VRDOK"),
		 * String.valueOf(getMasterSet().getInt("BRDOK"))})) { return true;
		 */
		if (stdoki.getDataModule().getRowCount(
				Condition.whereAllEqual(new String[] { "CSKL", "GOD", "VRDOK",
						"BRDOK" }, getMasterSet())) > 0)
			return true;
		else {
			javax.swing.JOptionPane.showMessageDialog(raDetail.getWindow(),
					"Ne postoje stavke ovog dokumenta. Nemogu\u0107 ispis!",
					"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public void Funkcija_ispisa_detail() {

		if (!isDetailExist())
			return;

		reportsQuerysCollector.getRQCModule().ReSql(PrepSql(true, false),
				what_kind_of_dokument);
		if (!isDetailInitIspis) {
			isDetailInitIspis = true;
			MyaddIspisDetail();
		}
		if (!bprepRunReport)
			prepRunReport();
		super.Funkcija_ispisa_detail();
	}

	void findBRDOK() {
		MP.rajpBrDok.SetDefTextDOK(this.raMaster.getMode());
	}

	public void changeCPAR() {
		if (raMaster.getMode() == 'N') {
			// if ((!MP.panelBasic.jrfCPAR.getText().equals(""))
			// && oldCPAR != getMasterSet().getInt("CPAR")) {
			if (getMasterSet().getInt("CPAR") != 0
					&& oldCPAR != getMasterSet().getInt("CPAR")) {
				QueryDataSet tmpPar = Partneri.getDataModule().getTempSet(
						"cpar=" + getMasterSet().getInt("CPAR"));
				tmpPar.open();
				oldCPAR = getMasterSet().getInt("CPAR");
				getMasterSet().setBigDecimal("UPRAB",
						tmpPar.getBigDecimal("prab"));
				tmpPar = null;
				getMasterSet().setString("CSHRAB", "");
			}
		}
	}

	/*
	 * public void enabdisabButtonDetail(boolean how) { for (int i = 0; i <
	 * raDetail.getEditNavActions().length; i++) {
	 * raDetail.getEditNavActions()[i].setLockEnabled(false);
	 * raDetail.getEditNavActions()[i].setEnabled(how);
	 * raDetail.getEditNavActions()[i].setLockEnabled(true); } }
	 * 
	 * public void enabdisabButtonMaster(boolean how) { for (int i = 0; i <
	 * raMaster.getEditNavActions().length; i++) { if
	 * (!(raMaster.getEditNavActions()[i].getIdentifier()
	 * .equalsIgnoreCase("Stavke") || raMaster.getEditNavActions()[i]
	 * .getIdentifier().equalsIgnoreCase("Novi"))) { // ||
	 * raMaster.getEditNavActions()[i]==rnvCopyDoce)) {
	 * raMaster.getEditNavActions()[i].setLockEnabled(false);
	 * raMaster.getEditNavActions()[i].setEnabled(how);
	 * raMaster.getEditNavActions()[i].setLockEnabled(true); } } }
	 * 
	 */

	public void enabdisabNavAction(raMatPodaci rmm, String[] izostavi,
			boolean kako) {

		boolean isOK = true;
		for (int i = 0; i < rmm.getEditNavActions().length; i++) {
			if (izostavi != null) {
				for (int j = 0; j < izostavi.length; j++) {
					if (rmm.getEditNavActions()[i].getIdentifier()
							.equalsIgnoreCase(izostavi[j])) {
						isOK = false;
					}
				}
			}
			if (isOK) {
				//rmm.getEditNavActions()[i].setLockEnabled(false);
				rmm.getEditNavActions()[i].setEnabled(kako);
				//rmm.getEditNavActions()[i].setLockEnabled(true);
			}
			isOK = true;
		}
	}

	public void enableMasterNavBar() {
		enabdisabNavAction(raMaster, null, true);
		if (!(checkAccess())) {
			enabdisabNavAction(raMaster, new String[] { "Novi", "Ispis",
					"Stavke" }, false);
			return;
		}
		if (getMasterSet().getRowCount() == 0) {
			enabdisabNavAction(raMaster, new String[] { "Novi" }, false);
		}
	}

	public void enableDetailNavBar() {
		enabdisabNavAction(raDetail, null, true);
		if (!(checkAccess())) {
			enabdisabNavAction(raDetail, new String[] { "Ispis" }, false);
			return;
		}
		if (getDetailSet().getRowCount() == 0) {
			enabdisabNavAction(raDetail, new String[] { "Novi" }, false);
		}

		if (isUserCheck()) {
			if (!hr.restart.sisfun.raUser.getInstance().isSuper()) {
				if (!hr.restart.sisfun.raUser.getInstance().getUser().equals(
						getMasterSet().getString("CUSER"))) {
					enabdisabNavAction(raDetail, new String[] { "Ispis" },
							false);
				}
			}
		}
	}

	public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent ne) {

		if (!getDetailSet().isOpen())
			getDetailSet().open();
		SetDetailTitle("def");
        if (raDetail.isShowing()) enableDetailNavBar();
        else enableMasterNavBar();
		
	}

	public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent ne) {

		lastCparNavigated = getMasterSet().getInt("CPAR");
		tmpCijenik = null;
		tmpKupArt = null;
		tmpKupArt = AST.getKupArtAll(getMasterSet().getInt("CPAR"), true);
		isKupArtExist = !tmpKupArt.isEmpty();
		DP.rpcart.enableNaziv();
		enableDetailNavBar();

	}

	public boolean checkAddEnabled() {
		return !Aut.getAut().isWrongKnjigYear(this, true);
	}
	
	/*
	 * public boolean checkAcces2() { if (raUser.getInstance().isSuper()) {
	 * return true; } if (super.isUserCheck()) { if
	 * (!hr.restart.sisfun.raUser.getInstance().getUser().equals(
	 * getMasterSet().getString("CUSER"))) return false; }
	 * 
	 * if (isKnjigen()) { return false; } if (isPrenesen()) { return false; } if
	 * (isKPR()) { return false; } if (Aut.getAut().isWrongKnjigYear(this))
	 * return false; return true; }
	 */

	public boolean checkAccess() {
		if (isKnjigen()) {
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

		restoreUserCheckMessage();
		return true;
	}

	public void findCjenik() {
		String oznval = "";
		if (getMasterSet().getString("OZNVAL").equalsIgnoreCase("")) {
			oznval = "OZNVAL ='" + domval + "'";
		} else {
			oznval = "OZNVAL ='" + getMasterSet().getString("OZNVAL") + "'";
		}
		String sql = "";

		if (TD.isDocOJ(getMasterSet().getString("VRDOK"))
				|| (this.what_kind_of_dokument.equalsIgnoreCase("PON") && getMasterSet()
						.getString("PARAM").equalsIgnoreCase("OJ"))) {
			sql = "select * from cjenik where cpar = "
					+ getMasterSet().getInt("CPAR") + " and corg='"
					+ getMasterSet().getString("CSKL") + "' AND CART="
					+ getDetailSet().getInt("CART") + " AND " + oznval;
System.out.println("findCjenik::isDocOJ :: "+sql);
			tmpCijenik = hr.restart.util.Util.getNewQueryDataSet(sql, true);
		} else {
			sql = "select * from cjenik where cpar = "
					+ getMasterSet().getInt("CPAR") + " and cskl='"
					+ getMasterSet().getString("CSKL") + "' AND " + "CART="
					+ getDetailSet().getInt("CART") + " AND " + oznval;
System.out.println("findCjenik::else :: "+sql);
            tmpCijenik = hr.restart.util.Util.getNewQueryDataSet(sql, true);
		}

		isCijenikExist = !tmpCijenik.isEmpty();

	}

	public void beforeShowDetail() {
		DP.InitRaPanCartDP();
        DP.rpcart.setExtraSklad(null);
	    if (getDetailSet().rowCount() > 0)
	       DP.rpcart.setExtraSklad(getDetailSet().getString("CSKLART"));
	}

	public void CORGafter_lookUp() {
	}

	public void SetFocusIzmjenaExtends() {
	}

	public void SetFocusNoviExtends() {
		jrfPJ_focusGained(null);
		if (MP.panelBasic.jrfCPAR.getText().equals("")) {
			MP.panelBasic.jrfCPAR.requestFocus();
		} else {
			MP.panelBasic.jrfCPAR.forceFocLost();
			after_lookUpCPAR();
			MP.panelBasic.jtfDATDOK.requestFocus();

		}
	}

	/**
	 * Kad iz datuma dokumenta izlazi provjera datuma i ra\u010Dunanje datuma
	 * dospije\u0107a prema danima *
	 * 
	 * @param e
	 *            ovo je fokusevent ako nema stavi null ne kontrolira se
	 */
	public void jtfDATDOK_focusLost(FocusEvent e) {

		getMasterSet().setTimestamp("DVO",
				getMasterSet().getTimestamp("DATDOK"));
		findBRDOK();
		jtfDVO_focusLost(null);
		MP.panelBasic.jpgetval.setTecajDate(getMasterSet().getTimestamp(
				"DATDOK"));
	}

	public void jtfDVO_focusLost(FocusEvent e) {
		java.util.Date Datum = new java.util.Date(getMasterSet().getTimestamp(
				"DVO").getTime());
		getMasterSet().setTimestamp(
				"DATDOSP",
				new java.sql.Timestamp(raDateUtil.getraDateUtil().addDate(
						Datum, (int) getMasterSet().getShort("DDOSP"))
						.getTime()));
	}

	public void jtfDDOSP_focusLost(FocusEvent e) {
		jtfDVO_focusLost(e);
	}

	public void jtfDATDOSP_focusLost(FocusEvent e) {

		getMasterSet().setShort(
				"DDOSP",
				(short) Math.round(hr.restart.util.Util.getUtil()
						.getHourDifference(getMasterSet().getTimestamp("DVO"),
								getMasterSet().getTimestamp("DATDOSP")) / 24.));
	}
    
    private int cpar=-1;

	public void after_lookUpCPAR() {

		if (MP.panelDodatni != null) {

			int cpara = -98765;
			try {
				cpara = Integer.valueOf(MP.panelBasic.jrfCPAR.getText())
						.intValue();
			} catch (Exception ex) {
				cpara = -98765;
			}

			QueryDataSet mypart = Partneri.getDataModule().getTempSet(
					Condition.equal("CPAR", cpara));
			mypart.open();
			// System.out.println("after_lookUpCPAR() MP.panelDodatni "+
			// mypart.getInt("CPAR"));
			int cagent = mypart.getInt("CAGENT");
			if (cagent != 0) {
				getMasterSet().setInt("CAGENT", cagent);
				MP.panelDodatni.jrfAgent.forceFocLost();
			} else {
				MP.panelDodatni.jrfAgent.setText("");
				MP.panelDodatni.jrfAgent.emptyTextFields();
			}
		}
		if (MP.panelBasicExt != null) {
			if (!MP.panelBasic.jrfCPAR.getText().equals("")) {
				jrfPJ_focusGained(null);
			}
		}

		if (raMaster.getMode() == 'N' && getMasterSet().getInt("CPAR") != 0 && getMasterSet().getInt("CPAR") != cpar) {
			QueryDataSet tmpPar = Partneri.getDataModule().getTempSet(
					"cpar=" + getMasterSet().getInt("CPAR"));
			tmpPar.open();

			// && !MP.panelBasic.jrfCPAR.getText().equals("")) {
			getMasterSet().setShort("DDOSP", tmpPar.getShort("DOSP"));
			jtfDVO_focusLost(null);
            cpar = getMasterSet().getInt("CPAR");
		}
		changeCPAR();

	}

	public void after_lookUpPJ() {
		if (MP.panelBasic.jrfCPAR.getText().equals("")) {

			MP.panelBasicExt.jrfPJ.setText("");
			MP.panelBasicExt.jrfNAZPJ.setText("");
			MP.panelBasicExt.jtfPJOPIS.setText("");

		} else if (MP.panelBasicExt.jrfPJ.getText().equals("")) {
			MP.panelBasicExt.jrfNAZPJ.setText("");
			MP.panelBasicExt.jtfPJOPIS.setText("");
		} else {
			lD.raLocate(qds4pjpar, "PJ", MP.panelBasicExt.jrfPJ.getText());// andrej
			// dodao
			MP.panelBasicExt.jtfPJOPIS.setText(qds4pjpar.getString("ADRPJ")
					+ ", " + qds4pjpar.getInt("PBRPJ") + " "
					+ qds4pjpar.getString("MJPJ"));
		}
	}

	public void jrfPJ_focusGained(FocusEvent e) {
		if (MP.panelBasicExt != null) {
			qds4pjpar = rFP.getClone(MP.panelBasic.jrfCPAR.getText());
			MP.panelBasicExt.jrfPJ.setRaDataSet((DataSet) qds4pjpar);
			MP.panelBasicExt.jrfNAZPJ.setRaDataSet((DataSet) qds4pjpar);
		}
	}

	/*
	 * public void MYafterGet_Val(){
	 * 
	 * if (MP.panelBasic.jpgetval.isValutaSelected()) {
	 * MP.panelBasic.jpgetval.setTecajDate(getMasterSet().getTimestamp("DATDOK"));
	 * getMasterSet().setBigDecimal("TECAJ",MP.panelBasic.jpgetval.getVarTecaj()); }
	 * else { getMasterSet().setBigDecimal("TECAJ",new BigDecimal("0.0000")); } }
	 */

	void jbRabat_actionPerformed(ActionEvent e) {
		if (!isRabatCallBefore)
			getrDR().getMyDataSet();
		getrDR().show();
		isRabatCallBefore = true;
	}

	public void jpRabat_afterJob() {

		getDetailSet().setBigDecimal("UPRAB", getrDR().sp);
		lc.setBDField("UPRAB", getrDR().sp, rKD.stavka);
		Kalkulacija("UPRAB");
		lc.TransferFromClass2DB(getDetailSet(), rKD.stavka);
		// lc.TransferFromClass2DB(rki.getDummySet(),rKD.stavka);

	}

	public void jpZatr_afterJob() {

		getDetailSet().setBigDecimal("UPZT", getrDZT().sp);
		lc.setBDField("UPZT", getrDZT().sp, rKD.stavka);
		Kalkulacija("UPZT");
		lc.TransferFromClass2DB(getDetailSet(), rKD.stavka);
		// lc.TransferFromClass2DB(rki.getDummySet(),rKD.stavka);
	}

	public boolean isStavkeExist() {
		super.refilterDetailSet();
		getDetailSet().last();
		return getDetailSet().isEmpty();
	}

	public void findRabat() {

		if (raDetail.getMode() == 'N') {
			if (lastCparNavigated != getMasterSet().getInt("CPAR")) {
				detailSet_navigated(null);
			}
			if (isKupArtExist) {
				if (lD.raLocate(tmpKupArt, new String[] { "CART" },
						new String[] { String.valueOf(getDetailSet().getInt(
								"CART")) })) {
					lc.setBDField("UPRAB", tmpKupArt.getBigDecimal("PRAB"),
							rKD.stavka);
				}
			} else {
				isKupArtExist = false;
			}
		} else {
			isKupArtExist = !getrDR().isShemaPodstaveExist(true);
		}
	}

	public boolean addRabati() {

		boolean forReturn = true;

		if (!getMasterSet().getString("CSHRAB").equals("")
				&& isKupArtExist == false) {

			if (getrDR().getDPDataSet().isEmpty() && raDetail.getMode() == 'N') {
				rEDM.InsertVTrabat(getDetailSet(), (int) getDetailSet()
						.getShort("RBR"), getMasterSet().getString("cshrab"));
			}

			else {

				rEDM.DeleteVTrabat(getDetailSet(), getDetailSet().getShort(
						"RBR"));
				dm.getVtrabat().open();
				getrDR().getDPDataSet().first();

				do {
					// TODO napuniti vt_.....
					dm.getVtrabat().insertRow(true);
					dm.getVtrabat().setString("LOKK", "N");
					dm.getVtrabat().setString("AKTIV", "D");
					dm.getVtrabat().setString("CSKL",
							getDetailSet().getString("CSKL"));
					dm.getVtrabat().setString("VRDOK",
							getDetailSet().getString("VRDOK"));
					dm.getVtrabat().setString("GOD",
							getDetailSet().getString("GOD"));
					dm.getVtrabat().setInt("BRDOK",
							getDetailSet().getInt("BRDOK"));
					dm.getVtrabat().setShort("RBR",
							getDetailSet().getShort("RBR"));
					dm.getVtrabat().setShort("LRBR",
							getrDR().getDPDataSet().getShort("LRBR"));
					dm.getVtrabat().setString("CRAB",
							getrDR().getDPDataSet().getString("CRAB"));
					dm.getVtrabat().setBigDecimal("PRAB",
							getrDR().getDPDataSet().getBigDecimal("PRAB"));
					dm.getVtrabat().setString("RABNARAB",
							getrDR().getDPDataSet().getString("RABNARAB"));

				} while (getrDR().getDPDataSet().next());
				dm.getVtrabat().saveChanges();
			}
		}
		return forReturn;
	}

	public boolean addZavtr() {

		boolean forReturn = true;
		if (!getMasterSet().getString("CSHRAB").equals("")) {

			if (getrDR().getDPDataSet().isEmpty() && raDetail.getMode() == 'N') {
				rEDM.InsertVTzavtr(getDetailSet(), (int) getDetailSet()
						.getShort("RBR"), getMasterSet().getString("cshzt"));
			}

			else {

				rEDM.DeleteVTzavtr(getDetailSet(), getDetailSet().getShort(
						"RBR"));
				dm.getVtzavtr().open();
				getrDR().getDPDataSet().first();

				do {
					// TODO napuniti vt_.....
					dm.getVtzavtr().insertRow(true);
					dm.getVtzavtr().setString("LOKK", "N");
					dm.getVtzavtr().setString("AKTIV", "D");
					dm.getVtzavtr().setString("CSKL",
							getDetailSet().getString("CSKL"));
					dm.getVtzavtr().setString("VRDOK",
							getDetailSet().getString("VRDOK"));
					dm.getVtzavtr().setString("GOD",
							getDetailSet().getString("GOD"));
					dm.getVtzavtr().setInt("BRDOK",
							getDetailSet().getInt("BRDOK"));
					dm.getVtzavtr().setShort("RBR",
							getDetailSet().getShort("RBR"));
					dm.getVtzavtr().setShort("LRBR",
							getrDZT().getDPDataSet().getShort("LRBR"));
					dm.getVtzavtr().setString("CZT",
							getrDZT().getDPDataSet().getString("CZT"));
					dm.getVtzavtr().setBigDecimal("PZT",
							getrDZT().getDPDataSet().getBigDecimal("PZT"));
					dm.getVtzavtr().setString("ZTNAZT",
							getrDZT().getDPDataSet().getString("ZTNAZT"));

				} while (getrDR().getDPDataSet().next());
				dm.getVtzavtr().saveChanges();
			}
		}
		return forReturn;
	}

	void jbZavtr_actionPerformed(ActionEvent e) {
		if (!isZavtrCallBefore)
			getrDZT().getMyDataSet();
		getrDZT().show();
		isZavtrCallBefore = true;
	}

	/**
	 * 
	 * Procedure za pregled stanja ... Cijenik za cijene KupArt za rabate findaj
	 * u
	 */

	public void findCStanje() {

		if (raDetail.getMode() == 'N'
				&& !DP.rpcart.jrfCART.getText().equals("") && !isEndedCancel) {
			// ///// zbog kolicine u novom nacinu
			dm.getArtikli().open();
			dm.getArtikli().enableDataSetEvents(false);
			lD.raLocate(dm.getArtikli(), new String[] { "CART" },
					new String[] { String
							.valueOf(getDetailSet().getInt("CART")) });
			getDetailSet().setString("JM", dm.getArtikli().getString("JM"));
			// //////////

			AST.findStanjeUnconditional(getDetailSet().getString("GOD"),
					getDetailSet().getString("CSKL"), getDetailSet().getInt(
							"CART"));

			// if (TD.isDocFinanc(getMasterSet().getString("VRDOK")) &&
			// !TD.isDocSklad(getMasterSet().getString("VRDOK")) &&
			// !getDetailSet().getString("CSKLART").equalsIgnoreCase("")){
			// AST.findStanjeUnconditional(getDetailSet().getString("GOD"),
			// getDetailSet().getString("CSKLART"),
			// getDetailSet().getInt("CART"));
			// }

			// ST.prn(AST.gettrenSTANJE());

			lc.setBDField("FC", DP.rpcart.findVC(), rKD.stavka);
			lc.setBDField("FVC", DP.rpcart.findVC(), rKD.stavka);
			lc.setBDField("FMC", DP.rpcart.findMC(), rKD.stavka);
			lc.setBDField("FMCPRP", DP.rpcart.findMC(), rKD.stavka);
			lc.setBDField("ZC", DP.rpcart.findZC(), rKD.stavka);

			// ako je u igri corg traži se prvo skladište koje pripada tom corgu
			// i s njega
			// se \u010Dupaju cijene

			if (TD.isDocOJ(getMasterSet().getString("VRDOK"))
					|| (this.what_kind_of_dokument.equalsIgnoreCase("PON") && getMasterSet()
							.getString("PARAM").equalsIgnoreCase("OJ"))) {

				if (!getDetailSet().getString("CSKLART").equalsIgnoreCase("")) {
					AST.findStanjeUnconditional(
							getDetailSet().getString("GOD"), getDetailSet()
									.getString("CSKLART"), getDetailSet()
									.getInt("CART"));

				} else if (hr.restart.util.lookupData.getlookupData().raLocate(
						dm.getSklad(), new String[] { "CORG" },
						new String[] { getMasterSet().getString("CSKL") })) {
					AST.findStanjeUnconditional(
							getMasterSet().getString("GOD"), dm.getSklad()
									.getString("CSKL"), getDetailSet().getInt(
									"CART"));
				}

				if (AST.gettrenSTANJE().getRowCount() != 0) {
					lc.setBDField("FC", AST.gettrenSTANJE()
									.getBigDecimal("VC"), rKD.stavka);
					lc.setBDField("FVC", AST.gettrenSTANJE()
							.getBigDecimal("VC"), rKD.stavka);
					lc.setBDField("FMC", AST.gettrenSTANJE()
							.getBigDecimal("MC"), rKD.stavka);
					lc.setBDField("FMCPRP", AST.gettrenSTANJE().getBigDecimal(
							"MC"), rKD.stavka);
				}
			}

			tmpCijenik = null;
			int cpar = getMasterSet().getInt("CPAR");
			if (cpar > 0) {
			  String doc = what_kind_of_dokument;
			  if (doc.equals("PON") && "OJ".equals(
			      getMasterSet().getString("PARAM")))
			    doc = "RAC";
			  tmpCijenik = AST.getCijenik(doc, 
	                getMasterSet().getString("CSKL"), cpar,
	                getDetailSet().getInt("CART"));
			}
			
			
//			findCjenik();
			//if (isCijenikExist) {
			if (tmpCijenik != null) {
				// && lD.raLocate(tmpCijenik, new String[] { "CART" },
				// new String[] { String.valueOf(getDetailSet()
				// .getInt("CART")) })) {
				if (domval == null) {
					domval = hr.restart.util.Util
							.getNewQueryDataSet(
									"select oznval from VALUTE where strval='N' ",
									true).getString("OZNVAL");
				}
				BigDecimal tecaj;
				if (getMasterSet().getString("OZNVAL").equalsIgnoreCase("")
						|| getMasterSet().getString("OZNVAL").equalsIgnoreCase(
								domval)) {
					tecaj = new BigDecimal("1.00");
				} else {
					tecaj = getMasterSet().getBigDecimal("TECAJ");
				}

				BigDecimal bdvc = tmpCijenik.getBigDecimal("VC")
						.multiply(tecaj);
				BigDecimal bdmc = tmpCijenik.getBigDecimal("MC")
						.multiply(tecaj);
				lc.setBDField("FC", bdvc, rKD.stavka);
				lc.setBDField("FVC", bdvc, rKD.stavka);
				lc.setBDField("FMC", bdmc, rKD.stavka);
				lc.setBDField("FMCPRP", bdmc, rKD.stavka);

				// lc.setBDField("FC", tmpCijenik.getBigDecimal("VC"),
				// rKD.stavka);
				// lc
				// .setBDField("FVC", tmpCijenik.getBigDecimal("VC"),
				// rKD.stavka);
				// lc
				// .setBDField("FMC", tmpCijenik.getBigDecimal("MC"),
				// rKD.stavka);
				// lc.setBDField("FMCPRP", tmpCijenik.getBigDecimal("MC"),
				// rKD.stavka);
			}

			/**
			 * Ovaj dio ako ne postoje cijena u cijeniku za dobavlja\u010Da ili
			 * ne postoje sa stanja kupi cijene defaultne koje su na Artiklu
			 */

			if (rKD.isEqualNula("stavka", "fmc")) {
				dm.getArtikli().open();
				dm.getArtikli().enableDataSetEvents(false);
				lD.raLocate(dm.getArtikli(), new String[] { "CART" },
						new String[] { String.valueOf(getDetailSet().getInt(
								"CART")) });

				lc.setBDField("FC", dm.getArtikli().getBigDecimal("VC"),
						rKD.stavka);
				lc.setBDField("FVC", dm.getArtikli().getBigDecimal("VC"),
						rKD.stavka);
				lc.setBDField("FMC", dm.getArtikli().getBigDecimal("MC"),
						rKD.stavka);
				lc.setBDField("FMCPRP", dm.getArtikli().getBigDecimal("MC"),
						rKD.stavka);
				lc.setBDField("ZC", dm.getArtikli().getBigDecimal("MC"),
						rKD.stavka);
				dm.getArtikli().enableDataSetEvents(true);

			}
			// //////////////////////////////////////////////////////////////////////////////
			// racuna li se popust odmah na MC
			if (isMaloprodajnaKalkulacija && isPopustMC) {
			  lD.raLocate(dm.getArtikli(), "CART",
			      Integer.toString(getDetailSet().getInt("CART")));
			  
			  BigDecimal pop = dm.getArtikli().getBigDecimal("PPOP");
			  if (pop.signum() != 0) {
			    BigDecimal mc = lc.getBDField("FMCPRP", rKD.stavka);
			    BigDecimal vc = lc.getBDField("FVC", rKD.stavka);
			    mc = mc.multiply(Aus.one0.subtract(pop.movePointLeft(2))).
			              setScale(2, BigDecimal.ROUND_HALF_UP);
			    vc = vc.multiply(Aus.one0.subtract(pop.movePointLeft(2))).
                          setScale(2, BigDecimal.ROUND_HALF_UP);
                
			    lc.setBDField("FC", vc, rKD.stavka);
                lc.setBDField("FVC", vc, rKD.stavka);
                lc.setBDField("FMC", mc, rKD.stavka);
                lc.setBDField("FMCPRP", mc, rKD.stavka);
			  }
			}
			
			findRabat();

			// lc.TransferFromClass2DB(rki.getDummySet(),rKD.stavka);
			lc.TransferFromClass2DB(getDetailSet(), rKD.stavka);
			if (getDetailSet().getBigDecimal("PPOR1").add(
					getDetailSet().getBigDecimal("PPOR2")).add(
					getDetailSet().getBigDecimal("PPOR3")).compareTo(
					Aus.zero2) == 0) {
				getDetailSet().setBigDecimal("FMC",
						getDetailSet().getBigDecimal("FVC"));
				getDetailSet().setBigDecimal("FMCPRP",
						getDetailSet().getBigDecimal("FVC"));
				lc.setBDField("FMC", getDetailSet().getBigDecimal("FVC"),
						rKD.stavka);
				lc.setBDField("FMCPRP", getDetailSet().getBigDecimal("FVC"),
						rKD.stavka);
			}
			rKD.stavkaold.Init();
			DP.setEnabledAll(true);
		} else {
		}
	}

	/**
	 * izvodi se prvo nakon rapancarta
	 */

	public void findCPOR() {

		if (lD.raLocate(dm.getNamjena(), new String[] { "CNAMJ" },
				new String[] { getMasterSet().getString("CNAMJ") })) {

			if (dm.getNamjena().getString("POREZ").equals("N")) {
				getDetailSet().setBigDecimal("PPOR1", BigDecimal.valueOf(0, 2));
				getDetailSet().setBigDecimal("PPOR2", BigDecimal.valueOf(0, 2));
				getDetailSet().setBigDecimal("PPOR3", BigDecimal.valueOf(0, 2));
				return; // (ab.f) 07-08-2002
			}
		}
		if (!DP.rpcart.jrfCART.getText().equals("")) {
			if (!lD.raLocate(dm.getArtikli(), new String[] { "CART" },
					new String[] { String
							.valueOf(getDetailSet().getInt("CART")) })) {

				javax.swing.JOptionPane.showMessageDialog(raDetail.getWindow(),
						"Ne postoji porezna grupa !", "Greška",
						javax.swing.JOptionPane.ERROR_MESSAGE);

			} else {

				dm.getArtikli().enableDataSetEvents(false);
				tmpNCfromArtikl = dm.getArtikli().getBigDecimal("NC");
				tmpVCfromArtikl = dm.getArtikli().getBigDecimal("VC");
				tmpMCfromArtikl = dm.getArtikli().getBigDecimal("MC");
				dm.getArtikli().enableDataSetEvents(true);
				if (lD.raLocate(dm.getPorezi(), new String[] { "CPOR" },
						new String[] { dm.getArtikli().getString("CPOR") })) {
					dm.getPorezi().enableDataSetEvents(false);

					getDetailSet().setBigDecimal("PPOR1",
							dm.getPorezi().getBigDecimal("POR1"));
					getDetailSet().setBigDecimal("PPOR2",
							dm.getPorezi().getBigDecimal("POR2"));
					getDetailSet().setBigDecimal("PPOR3",
							dm.getPorezi().getBigDecimal("POR3"));
					getDetailSet().setBigDecimal("UPPOR",
							dm.getPorezi().getBigDecimal("UKUPOR"));

					dm.getPorezi().enableDataSetEvents(true);
				}
			}
		} else {
			getDetailSet().setBigDecimal("PPOR1", BigDecimal.valueOf(0, 2));
			getDetailSet().setBigDecimal("PPOR2", BigDecimal.valueOf(0, 2));
			getDetailSet().setBigDecimal("PPOR3", BigDecimal.valueOf(0, 2));
			getDetailSet().setBigDecimal("UPPOR", BigDecimal.valueOf(0, 2));
		}

		lc.TransferFromDB2Class(getDetailSet(), rKD.stavka);
	}

	public void ClearAll() {
		rKD.stavka.Init();
		lc.TransferFromClass2DB(getDetailSet(), rKD.stavka);
		// lc.TransferFromClass2DB(rki.getDummySet(),rKD.stavka);
	}

	public void MyNextToFocus() {
		if (raDetail.getMode() != 'B')
			DP.setEnabledAll(true);
	}

	public void Kalkulacija(String how) {

		lc.TransferFromDB2Class(AST.gettrenSTANJE(), rKD.stanje);
		// rKD.setVrzal(vrzal);
		// rKD.stavka.rezkol = getMasterSet().getString("REZKOL");
		rKD.KalkulacijaStavke(what_kind_of_dokument, how, raDetail.getMode(),
				getMasterSet().getString("CSKL"), isMaloprodajnaKalkulacija);
		rKD.KalkulacijaStanje(what_kind_of_dokument);
		lc.TransferFromClass2DB(getDetailSet(), rKD.stavka);
	}
    
    public void Kalkulacija(JraTextField tmpF, String kako) {
      if (raDetail.getMode() == 'B') return;
      if (!DP.tmpText.equals(tmpF.getText())) {
        lc.setBDField(tmpF.getColumnName(), tmpF.getDataSet()
                .getBigDecimal(tmpF.getColumnName()), rKD.stavka);
        // if (TD.isGOTGRN(getMasterSet().getString("VRDOK"))) {
        // Kalkulacija("FMC");
        // } else {
        Kalkulacija(tmpF.getColumnName());
        // }
      }
    }

	public void Kalkulacija(FocusEvent e, String kako) {
        Kalkulacija((JraTextField) e.getComponent(), kako);
	}

	public void MfocusGained(FocusEvent e) {
		JraTextField tmpF = (JraTextField) e.getComponent();
		DP.tmpText = tmpF.getText();
	}
	
	public boolean ValidacijaStanje() {

		boolean isStanje = AST.findStanjeFor(getDetailSet(), isOJ);
		if (!isStanje && isTranzit) {
		  if (!isStanje) {
            AST.gettrenSTANJE().insertRow(false);
            dM.copyColumns(getDetailSet(), AST.gettrenSTANJE(), 
                    new String[] {"CSKL", "GOD", "CART"});
            nulaStanje(AST.gettrenSTANJE());
            AST.gettrenSTANJE().post();
            isStanje = true;
		  }
		}
		if (isStanje && !rCD.isDataKalkulOK(getMasterSet().getTimestamp("DATDOK"), 
				AST.gettrenSTANJE().getString("TKAL"))) {
			JOptionPane.showMessageDialog(raDetail.getWindow(),
							"Datum zadnje kalkulacije je veæi nego izlaznog dokumenta koji želite napraviti !",
							"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (raDetail.getMode() == 'I') {
			rCD.prepareFields(getDetailSet());
			if (!rCD.testIzlaz4Del((DataSet) getDetailSet(), AST
					.gettrenSTANJE())) {
				javax.swing.JOptionPane.showMessageDialog(raDetail.getWindow(), 
				    rCD.errorMessage(), "Greška",
						javax.swing.JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if (isStanje && !isUslugaOrTranzit()) {
    		rKD.stanje.sVrSklad = AST.VrstaZaliha();
    		// rKD.stavka.rezkol = getMasterSet().getString("REZKOL");
    		BigDecimal tmpBDD = Aus.zero2;
    
    		if (rKD.stanje.sVrSklad.equalsIgnoreCase("V")) {
    			if (getDetailSet().getBigDecimal("VC").compareTo(
    					getDetailSet().getBigDecimal("ZC")) != 0) {
    
    				JOptionPane.showMessageDialog(raDetail.getWindow(),
    								"Cijena zalihe je razlièita od veleprodajna cijene !!!",
    								"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
    				return false;
    			}
    
    		} else if (rKD.stanje.sVrSklad.equalsIgnoreCase("M")) {
    			if (getDetailSet().getBigDecimal("MC").compareTo(
    					getDetailSet().getBigDecimal("ZC")) != 0) {
    				JOptionPane.showMessageDialog(raDetail.getWindow(),
    								"Cijena zalihe je razlièita od maloprodajne cijene !!!",
    								"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
    				return false;
    			}
    		}
		}
		
		if (TD.isDocDiraZalihu(what_kind_of_dokument)) {
		  if (!isUslugaOrTranzit()) {
		    if (!isStanje) {
		      DP.jtfKOL.requestFocus();
              JOptionPane.showMessageDialog(raDetail.getWindow(),
                 "Artikla nema na stanju!", "Greška", 
                 JOptionPane.ERROR_MESSAGE);
              return false;
		    }
		    lc.TransferFromDB2Class(AST.gettrenSTANJE(), rKD.stanje);
	        rKD.stanje.sVrSklad = AST.VrstaZaliha();
				int i = rKD.TestStanje();
				if (isTranzit || isMinusAllowed) {
				  // nista
				} else if (i == -1) {
					DP.jtfKOL.requestFocus();
					JOptionPane.showMessageDialog(raDetail.getWindow(),
									"Koli\u010Dina je ve\u0107a nego koli\u010Dina na zalihi !",
									"Greška",
									javax.swing.JOptionPane.ERROR_MESSAGE);
					return false;
				} else if (i == -2) {
					DP.jtfKOL.requestFocus();
					String rezkol = hr.restart.sisfun.frmParam.getParam(
							"robno", "rezkol");
					if (!rezkol.equals("N")) {
						JOptionPane.showMessageDialog(raDetail.getWindow(),
										"Koristite rezervirane koli\u010Dine !",
										"Greška",
										javax.swing.JOptionPane.ERROR_MESSAGE);
					}
					if (rezkol.equals("D"))
						return false;
//					return true;
				}
				if (rKD.isEqualNula("stavka", "kol")) {
					DP.jtfKOL.requestFocus();
					JOptionPane.showMessageDialog(raDetail.getWindow(),
							"Koli\u010Dina mora biti ve\u0107a od nule !",
							"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
					return false;
				}

				if (hr.restart.sisfun.frmParam.getParam("robno", "minkol", "N")
						.equalsIgnoreCase("D")
						&& DP.rpcart.jrfCART.getRaDataSet().getBigDecimal(
								"MINKOL").doubleValue() != 0
						&& rKD.isKolStanjeManjeOd(DP.rpcart.jrfCART
								.getRaDataSet().getBigDecimal("MINKOL"))) {
					DP.jtfKOL.requestFocus();
					JOptionPane.showMessageDialog(raDetail.getWindow(),
									"Koli\u010Dina nakon unosa dokumenta je "
											+ rKD.getKolStanjeAfterMat()
											+ " "
											+ DP.rpcart.jrfCART.getRaDataSet()
													.getString("JM")
											+ " "
											+ " i pala je ispod dozvoljene minimalne koli\u010Dine koja iznosi "
											+ DP.rpcart.jrfCART.getRaDataSet()
													.getBigDecimal("MINKOL")
											+ DP.rpcart.jrfCART.getRaDataSet()
													.getString("JM") + " !!!! ",
									"Greška",
									javax.swing.JOptionPane.ERROR_MESSAGE);
					return false;
				}

				if (hr.restart.sisfun.frmParam.getParam("robno", "sigkol", "N")
						.equalsIgnoreCase("D")
						&& DP.rpcart.jrfCART.getRaDataSet().getBigDecimal(
								"SIGKOL").doubleValue() != 0
						&& rKD.isKolStanjeManjeOd(DP.rpcart.jrfCART
								.getRaDataSet().getBigDecimal("SIGKOL"))) {
					DP.jtfKOL.requestFocus();
					if (!(JOptionPane.showConfirmDialog(raDetail.getWindow(),
									"Koli\u010Dina nakon unosa dokumenta je "
											+ rKD.getKolStanjeAfterMat()
											+ " "
											+ DP.rpcart.jrfCART.getRaDataSet()
													.getString("JM")
											+ " "
											+ " i pala je ispod signalne koli\u010Dine koja iznosi "
											+ DP.rpcart.jrfCART.getRaDataSet()
													.getBigDecimal("SIGKOL")
											+ DP.rpcart.jrfCART.getRaDataSet()
													.getString("JM")
											+ " !!!! Želite li nastaviti ?",
									"Upit",
									javax.swing.JOptionPane.YES_NO_OPTION,
									javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION)) {
						return false;
					}
				}
			}
		}
		if (!(TD.isDocSklad(what_kind_of_dokument) || "DOS"
				.equalsIgnoreCase(what_kind_of_dokument))) {
			Kalkulacija("_NULARAZDUZ");
		}
		
		if (!isUslugaOrTranzit())
		  rKD.KalkulacijaStanje(what_kind_of_dokument);

		return true;
	}

	public void prepDelete() {
		rKD.stavka.Init();
		rKD.KalkulacijaStanje(what_kind_of_dokument);
	}

	/**
	 * Samo za razdužemnje maloprodaje
	 * 
	 * @param qds
	 *            dataset u kojem je maloprodajica
	 */
	public void metToDo_after_lookUp1(DataSet qds) {
	}

	public void setupRabat() {

		if (raDetail.getMode() == 'N') {
			getDetailSet().setBigDecimal("UPRAB",
					getMasterSet().getBigDecimal("UPRAB"));
			lc.setBDField("UPRAB", getMasterSet().getBigDecimal("UPRAB"),
					rKD.stavka);
		} else {
			findRabat();
		}
		if (!getMasterSet().getString("CSHRAB").equals("")) {
			isRabatShema = true;

		} else
			isRabatShema = false;
	}

	public void setupZavTr() {
		if (raDetail.getMode() == 'N') {
			getDetailSet().setBigDecimal("UPZT",
					getMasterSet().getBigDecimal("UPZT"));
			lc.setBDField("UPZT", getMasterSet().getBigDecimal("UPZT"),
					rKD.stavka);
		}
		if (!getMasterSet().getString("CSHZT").equals(""))
			isZavtrShema = true;
		else
			isZavtrShema = false;
	}

	public void keyActionMaster() {
		raOdabirDok rOD = new raOdabirDok((Frame) getJPanelMaster()
				.getTopLevelAncestor(), "Odabir dokumenta", true) {
			public void afterOKPress(String odabrano) {
				afterKeyActionPress(odabrano);
			}
		};
		rOD.DocumentiZaPrijenos(what_kind_of_dokument);
		rOD.show();
	}

	public boolean isNedozvoljenArtikl() {
		return false;
	}

	public void forceall_focuslost() {
		MP.forceall_focuslost();
	}

	public boolean isRabatHandle() {
		if (isRabatShema) {
			if (isKupArtExist) {
				if (lD.raLocate(tmpKupArt, new String[] { "CART" },
						new String[] { String.valueOf(getDetailSet().getInt(
								"CART")) })) {
					return false;
				} else
					return true;
			} else
				return isRabatShema;
		} else
			return isRabatShema;
	}

	/**
	 * Metoda koja se izvršava nokon ok na pritisak panela u odabiru rn-a
	 */
	public void afterCancel() {
	}

	/**
	 * Metoda koja se izvršava nokon cancel na pritisak panela u odabiru rn-a
	 */
	public void afterOK() {
	}

	/**
	 * za second choozer
	 * 
	 * @param odabrano
	 *            koji dokument je odabrati
	 */
	public void afterKeyActionPress(String odabrano) {
      invokeSC(odabrano, null);
	}
    
    public void invokeSC(String odabrano, DataSet prep) {
        if (dcz == null) {
          dcz = new SecondChooser("Odabir dokumenta za prijenos") {
              public void afterOK() {
                SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                    afterOKSC();
                  }
                });
              }
          };
      }
  
      if (prep == null) prepareQuery(odabrano);
      dcz.setSelected(odabrano);
      dcz.setDataSet(prep == null ? qDS : prep);
      String[] dods = (prep == null ? 
          qDS.hasColumn("CPAR") != null : 
            prep.hasColumn("CPAR") != null) ?
              new String[] {"DATDOK", "CPAR", "UIRAC"} : 
              new String[] {"DATDOK"};
      dcz.setUpClass(this);
      dcz.setDataSetKey(new String[] { "CSKL", "GOD", "VRDOK", "BRDOK" }, dods);
      dcz.initialise();
      if (prep == null) {
        dcz.pack();
        dcz.show();
      } else {
        dcz.simTrans();
        dcz.okSelect();
      }
    }

	public void afterOKSC() {
		// doWithSaveMaster('N');

		raMaster.getJpTableView().fireTableDataChanged();
		raMaster.setLockedMode('I');
		raMaster.getOKpanel().jPrekid_actionPerformed();
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            jBStavke_actionPerformed(null);
          }
        });
        
        // hack by ab.f - CRM automatika
        if (dm.isCRM() &&
            (getMasterSet().getString("VRDOK").equalsIgnoreCase("ROT") ||
            getMasterSet().getString("VRDOK").equalsIgnoreCase("RAC"))) {
          final int cpar = getMasterSet().getInt("CPAR");
          final String cskl = getMasterSet().getString("CSKL");
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              raUpdateCRM.addRac(cskl, cpar);
            }
          });
        }
	}

	public String dodatak(String odabrano) {
		String dodatakic = new String("");
		if (odabrano.equals("IZD")) {
			dodatakic = " and cradnal !='' and datradnal is null ";
		} else {
			String cp = MP.panelBasic.jrfCPAR.getText();
			if (cp.length() == 0) {
				dodatakic = "";// cp = " and 1=0";
			} else {
				dodatakic = " and (cpar=" + getMasterSet().getInt("CPAR")
						+ " or cpar is null) ";

			}
		}
		return dodatakic;
	}

	public void prepareQuery(String odabrano) {
        System.out.println("Odabrano "+odabrano);
        String year = val.findYear(pressel.getSelRow().
            getTimestamp("DATDOK-to"));
        
        boolean twoy = frmParam.getParam("robno", "sc2god", "D",
            "Dopustiti dohvat dokumenata iz prošle godine (D,N)", true).equals("D");
        String yc = !twoy ? "god='"+year+"'" : "god in ('"+
              year+"','"+(Aus.getNumber(year) - 1)+"')";
        

		if (odabrano.equals("RN")) {
            qDS = RN.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK DATDOK",
                aSS.getS4raCatchDocRN(year, pressel.getSelRow()
							.getString("CSKL"), dodatakRN));
            qDS.open();
		} else {

			dodatak = dodatak(odabrano);
			String upit = "";
            aSS.ispisDA = true;
            

			if ((getMasterSet().getString("VRDOK").equalsIgnoreCase("RAC") || getMasterSet()
					.getString("VRDOK").equalsIgnoreCase("GRN"))
					&& odabrano.equalsIgnoreCase("PON")) {
				upit = "statira='N' and "+yc+" and vrdok= 'PON'" + dodatak
						+ " and cskl in ('"
						+ pressel.getSelRow().getString("CSKL") + "')"; // samo
			} else if ((getMasterSet().getString("VRDOK").equalsIgnoreCase(
					"IZD") && odabrano.equalsIgnoreCase("POS"))
					|| (getMasterSet().getString("VRDOK").equalsIgnoreCase(
							"OTP") && odabrano.equalsIgnoreCase("POS"))) {
				VarStr vs = new VarStr("");
				StorageDataSet stt = OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
				for (stt.first(); stt.inBounds(); stt.next()) {
					vs.append("'").append(stt.getString("CORG")).append("',");
				}
				vs.chopRight(1);

				upit = "statira='N' and god = '"
						+ year + "' and vrdok= 'POS'" + dodatak
						+ " and cskl in (" + vs + ")"; // samo

			} else if (TD.isDocFinanc(getMasterSet().getString("VRDOK"))
					&& !TD.isDocSklad(getMasterSet().getString("VRDOK"))) {
				upit = aSS.getS4raCatchDoc(yc, pressel.getSelRow()
						.getString("CSKL"), odabrano, dodatak, true);
			} else {
				upit = aSS.getS4raCatchDoc(yc, pressel.getSelRow()
						.getString("CSKL"), odabrano, dodatak);
			}

            qDS = doki.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK DATDOK CPAR UIRAC", upit);
            qDS.open();
		}
	}

	private raCopyStavka rCS = raCopyStavka.getraCopyStavka();

	public void normativPresOK() {

		raStdokiMath rSM = new raStdokiMath();
		gNA.findAllSastojak();
		if (gNA.getSastojak().isEmpty()) {
			javax.swing.JOptionPane
					.showMessageDialog(
							null,
							"Ovaj se artikl ne može prenijeti jer nema razra\u0111en normativ !",
							"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
			return;
		}
		boolean bdocsklad = TD.isDocSklad(getMasterSet().getString("VRDOK"));
		if (bdocsklad) {
			if (!rCS.testStanje(gNA.getSastojak(), getMasterSet().getString(
					"GOD"), getMasterSet().getString("CSKL")))
				return;
		}
		rSM.initMathCommon(getMasterSet().getString("CSKL"), getMasterSet()
				.getString("VRDOK"), getMasterSet().getString("GOD"),
				getMasterSet().getString("CSHRAB"), getMasterSet()
						.getBigDecimal("UPRAB"), getMasterSet().getString(
						"CSHZT"), getMasterSet().getBigDecimal("UPZT"));

		gNA.getSastojak().first();
		getDetailSet().enableDataSetEvents(false);
		findNSTAVKA();
		do {
			getDetailSet().insertRow(true);
			CopyCommonFieldsFromZaglavljeToStavke();
			rSM.initMathCart(getDetailSet(), gNA.getSastojak().getInt("CART"),
					getMasterSet().getInt("CPAR"), gNA.getSastojak()
							.getBigDecimal("KOL"));
			if (TD.isDocSklad(getMasterSet().getString("VRDOK"))) {
				rSM.calcMathSklad();
			}
			if (TD.isDocFinanc(getMasterSet().getString("VRDOK"))) {
				rSM.calcMathFinanc();
			}
			getDetailSet().setInt("cartnor", getDetailSet().getInt("CARTNOR"));
			getDetailSet().setShort("rbr", nStavka);
			getDetailSet().setInt("rbsid", rbr.getRbsID(getDetailSet()));
			getDetailSet().saveChanges();
			/**
			 * @todo treba updatirati sranje dodati rajbate i zavisne troškove
			 *       updatirati zaglavlje
			 */
			nStavka++;

		} while (gNA.getSastojak().next());
		getDetailSet().enableDataSetEvents(true);
	}

	public void forNormArt() {
		gNA.show();
	}

	public void forElementi() {
		new raDocKalkulator().showPananel(getDetailSet(), raDetail
				.getLocation().getX(), raDetail.getLocation().getY(),
				(Frame) raDetail.getFrameOwner());
	}

	/**
	 * provjera da li je knjižen ili ne
	 */

	public boolean isKnjigen() {
		return getMasterSet().getString("STATKNJ").equalsIgnoreCase("K")
				|| getMasterSet().getString("STATKNJ").equalsIgnoreCase("P");
	}

	/**
	 * Status = T zna\u010Di da je dokument prenešen u centralnu bazu
	 * 
	 * @return
	 */
	public boolean isPrenesen() {
		return getMasterSet().getString("STATUS").equalsIgnoreCase("P");
	}

	public boolean isKPR() {
		return getMasterSet().getString("STAT_KPR").equalsIgnoreCase("D");
	}

	public void defFranka() {
		// franka
	  if (getMasterSet().getString("CFRA").trim().length() > 0) return;
	  
		String franka = hr.restart.sisfun.frmParam.getParam("robno",
				"defFranka", "", "Predefinirana šifra frankature");
		if (franka == null || franka.equalsIgnoreCase("")) {
			javax.swing.JOptionPane
					.showMessageDialog(
							raMaster.getWindow(),
							"Ne postoji parametar defFranka nuzan za izradu ovog dokumenta !",
							"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
		} else {
			if (lD.raLocate(dm.getFranka(), new String[] { "CFRA" },
					new String[] { franka })) {
				getMasterSet().setString("CFRA", franka);
			} else {
				javax.swing.JOptionPane.showMessageDialog(raMaster.getWindow(),
						"Neispravna vrijednost parametra defFranka ==" + franka
								+ " nuznog za izradu ovog dokumenta !",
						"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void defNacotp() {
		// nacotp
	  if (getMasterSet().getString("CNAC").trim().length() > 0) return;
	  
		String defNacotp = hr.restart.sisfun.frmParam.getParam("robno",
				"defNacotp", "", "Predefinirani naèin otpreme robe");
		if (defNacotp == null || defNacotp.equalsIgnoreCase("")) {
			javax.swing.JOptionPane
					.showMessageDialog(
							raMaster.getWindow(),
							"Ne postoji parametar defNacotp nuzan za izradu ovog dokumenta !",
							"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
		} else {
			if (lD.raLocate(dm.getNacotp(), new String[] { "CNAC" },
					new String[] { defNacotp })) {
				getMasterSet().setString("CNAC", defNacotp);
			} else {
				javax.swing.JOptionPane.showMessageDialog(raMaster.getWindow(),
						"Neispravna vrijednost parametra defNacotp =="
								+ defNacotp
								+ " nuznog za izradu ovog dokumenta !",
						"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void defNacpl() {
	  if (getMasterSet().getString("CNACPL").trim().length() > 0) return;

		if (defNacpl == null || defNacpl.equalsIgnoreCase("")) {
			javax.swing.JOptionPane
					.showMessageDialog(
							raMaster.getWindow(),
							"Ne postoji parametar defNacpl nuzan za izradu ovog dokumenta !",
							"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
		} else {
			if (lD.raLocate(dm.getNacpl(), new String[] { "CNACPL" },
					new String[] { defNacpl })) {
				getMasterSet().setString("CNACPL", defNacpl);
			} else {
				javax.swing.JOptionPane.showMessageDialog(raMaster.getWindow(),
						"Neispravna vrijednost parametra defNacpl =="
								+ defNacpl
								+ " nuznog za izradu ovog dokumenta !",
						"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void defNamjena() {
		// namjena
	  if (getMasterSet().getString("CNAMJ").trim().length() > 0) return;
	  
		String defNamjena = hr.restart.sisfun.frmParam.getParam("robno",
				"defNamjena", "", "Predefinirana namjena robe");
		if (defNamjena == null || defNamjena.equalsIgnoreCase("")) {
			javax.swing.JOptionPane
					.showMessageDialog(
							raMaster.getWindow(),
							"Ne postoji parametar defNamjena nuzan za izradu ovog dokumenta !",
							"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
		} else {
			if (lD.raLocate(dm.getNamjena(), new String[] { "CNAMJ" },
					new String[] { defNamjena })) {
				getMasterSet().setString("CNAMJ", defNamjena);
			} else {
				javax.swing.JOptionPane.showMessageDialog(raMaster.getWindow(),
						"Neispravna vrijednost parametra defNamjena =="
								+ defNamjena
								+ " nuznog za izradu ovog dokumenta !",
						"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void defaultMasterData() {

		defFranka();
		defNacotp();
		defNacpl();
		defNamjena();
		MP.forceall_focuslost();
	}

	public boolean manjeNula() {
		if (getDetailSet().getBigDecimal("FMC").doubleValue() < 0) {
			JOptionPane.showConfirmDialog(this.raDetail,
					"Cijena ne smije biti manja od 0 !!!!", "Gre\u0161ka",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
			return true;
		}
		return false;
	}

	public boolean isPriceToBig(boolean poruka) {

		if (getDetailSet().getBigDecimal("NC").doubleValue() > getDetailSet()
				.getBigDecimal("FMC").doubleValue()) {
			DP.jraFMC.requestFocus();
			if (poruka) {
				return (javax.swing.JOptionPane
						.showConfirmDialog(
								this,
								"Cijena s popustom manja je nego nabavna cijena !!!! Nastaviti ?",
								"Upit", javax.swing.JOptionPane.YES_NO_OPTION,
								javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION);
			}
			return false;
		}
		return true;
	}

	public static boolean isReportValute() {

		String descriptor = hr.restart.util.reports.dlgRunReport
				.getCurrentDlgRunReport().getCurrentDescriptor().getName();

		return (descriptor.equals("hr.restart.robno.repPredracuniV")
				|| descriptor.equals("hr.restart.robno.repRacuniV")
				|| descriptor.equals("hr.restart.robno.repPredracuni2V")
				|| descriptor.equals("hr.restart.robno.repRacuni2V")
				|| descriptor.equals("hr.restart.robno.repRac2V")
				|| descriptor.equals("hr.restart.robno.repRacV")
				|| descriptor.equals("hr.restart.robno.repPonudaV")
				|| descriptor.equals("hr.restart.robno.repPonuda2V") || descriptor
				.equals("hr.restart.robno.repNarDobV"));

	}

	private void setKumulativ() {
		set_kum_detail(true);
		stozbrojiti_detail(new String[] { "UIRAB", "UIZT", "INETO", "IPRODBP",
				"IPRODSP", "INAB", "IMAR", "IBP", "IPOR", "ISP", "IRAZ",
				"UIPOR" });
		setnaslovi_detail(new String[] { "Iznos rabata",
				"Iznos zavisih troškova", "Netto iznos", "Iznos bez poreza",
				"Iznos s porezom", "Razduženje nabavne vrijednosti",
				"Razduženje RUC-a", "Iznos bez poreza", "Razduženje poreza",
				"Iznos s porezom", "Iznos razaduženja", "Iznos poreza" });

	}

	void setNull() {
	}

	void setNull1() {
		if (MP.panelBasic != null
				&& MP.panelBasic.rpku.jraCkupac.getText().equalsIgnoreCase("")) {
			// getMasterSet().setAssignedNull("CKUPAC");
			getMasterSet().setInt("CKUPAC", 0);
		}
		if (MP.panelDodatni != null
				&& MP.panelDodatni.jrfAgent.getText().equalsIgnoreCase("")) {
			// getMasterSet().setAssignedNull("CAGENT");
			getMasterSet().setInt("CAGENT", 0);
		}
		if (MP.panelDodatni != null
				&& MP.panelDodatni.jrfKO.getText().equalsIgnoreCase("")) {
			// getMasterSet().setAssignedNull("CKO");
			getMasterSet().setInt("CKO", 0);
		}
	}

	boolean myprepStatement() {
		return true;
	}

	boolean myprepStatement1() {

		ArrayList al = new ArrayList();

		if (getMasterSet().getInt("CKUPAC") == 0) {
			al.add("ckupac = null");
		}
		if (getMasterSet().getInt("CAGENT") == 0) {
			al.add("cagent = null");
		}
		if (getMasterSet().getInt("CKO") == 0) {
			al.add("cko = null");
		}

		if (!al.isEmpty()) {
			VarStr prepSQL = new VarStr("UPDATE DOKI SET ");

			for (int i = 0; i < al.size(); i++) {
				prepSQL.append((String) al.get(i)).append(", ");
			}
			prepSQL.chopRight(2);
			prepSQL.append(" WHERE cskl='").append(
					getMasterSet().getString("CSKL")).append("' AND GOD='")
					.append(getMasterSet().getString("GOD")).append(
							"' AND VRDOK = '").append(
							getMasterSet().getString("VRDOK")).append(
							"' AND BRDOK = ").append(
							getMasterSet().getInt("BRDOK"));

			try {

				PreparedStatement JeboTiPasMaterZbogGreskeNekogaJaOvoMoramOvakoSjebat = raTransaction
						.getPreparedStatement(prepSQL.toString());
				return JeboTiPasMaterZbogGreskeNekogaJaOvoMoramOvakoSjebat
						.execute();
			} catch (SQLException ex) {
				ex.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private QueryDataSet forallpopust;

	public void popust4All() {

		String str = "SELECT * FROM STDOKI WHERE CSKL='"
				+ getMasterSet().getString("CSKL") + "' and vrdok='"
				+ getMasterSet().getString("VRDOK") + "' and god='"
				+ getMasterSet().getString("GOD") + "' and brdok="
				+ getMasterSet().getInt("BRDOK");

		forallpopust = hr.restart.util.Util.getNewQueryDataSet(str, true);

		if (forallpopust == null || forallpopust.getRowCount() == 0) {

			JOptionPane.showConfirmDialog(this.raMaster,
					"Na postoje stavke za ažuriranje dodatnih popusta !",
					"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		BigDecimal uirac = Aus.zero2;
		for (forallpopust.first(); forallpopust.inBounds(); forallpopust.next()) {

			rKD.stavka.Init();
			rKD.stavkaold.Init();
			rKD.setWhat_kind_of_document(forallpopust.getString("VRDOK"));
			lc.TransferFromDB2Class(forallpopust, rKD.stavka);
			lc.printAll(rKD.stavka);
			rKD.stavka.uprab = getMasterSet().getBigDecimal("UPRAB");
			if (isMaloprodajnaKalkulacija) {
				rKD.MaloprodajnaKalkulacija();
			} else {
				rKD.kalkFinancPart();
			}
			lc.TransferFromClass2DB(forallpopust, rKD.stavka);
			uirac = uirac.add(forallpopust.getBigDecimal("IPRODSP"));
			lc.printAll(rKD.stavka);
		}

		getMasterSet().setBigDecimal("UIRAC", uirac);
		raLocalTransaction rltpopustAllApply = new raLocalTransaction() {
			public boolean transaction() throws Exception {
				raTransaction.saveChanges(forallpopust);
				raTransaction.saveChanges(getMasterSet());
				return true;
			}
		};
		// privremeno zmrceno jer grijesi
		if (rltpopustAllApply.execTransaction()) {
			getDetailSet().refresh();
		}
		;
		afterOKSC();
	}

	/*
	 * public boolean updateTxtZag() { if (raMaster.getMode() == 'N') {
	 * 
	 * vttext = hr.restart.util.Util.getNewQueryDataSet( "SELECT * FROM vttext
	 * WHERE 1=0", true); vttext.insertRow(true); vttext.setString("CKEY",
	 * rCD.getKey(getMasterSet())); vttext.setString("TEXTFAK", ""); isVTtext =
	 * true; } return true; }
	 */
	public boolean updateTxt() {
		if (raDetail.getMode() == 'N') {
			QueryDataSet tmpVTTEXT = hr.restart.util.Util.getNewQueryDataSet(
					"SELECT * FROM vttext WHERE CKEY='"
							+ rCD.getKey(DP.rpcart.jrfCART.getRaDataSet())
							+ "'", true);
			if (tmpVTTEXT.getRowCount() > 0) {
				vttext = hr.restart.util.Util.getNewQueryDataSet(
						"SELECT * FROM vttext WHERE 1=0", true);
				vttext.insertRow(true);
				vttext.setString("CKEY", rCD.getKey(getDetailSet()));
				vttext.setString("TEXTFAK", tmpVTTEXT.getString("TEXTFAK"));
				isVTtext = true;
			}
		}
		return true;
	}

	// javax.swing.JOptionPane.showMessageDialog(null,
	// "Saldo dugovanja partnera "+dm.getPartneri().getString("NAZPAR")+" iznosi
	// "+limit + " i prelazi "+
	// "limit kreditiranja koji iznosi
	// "+dm.getPartneri().getBigDecimal("LIMIT"),
	// "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);

	public boolean ValidacijaLimit(java.math.BigDecimal oldvalue,
			java.math.BigDecimal newvalue) {
		if (checkLimit) {
			lD.raLocate(dm.getPartneri(), new String[] { "CPAR" },
					new String[] { String
							.valueOf(getMasterSet().getInt("CPAR")) });

			java.math.BigDecimal limit = dm.getPartneri().getBigDecimal(
					"LIMKRED");
			if (limit.doubleValue() != 0) {
				java.math.BigDecimal saldo = getSaldo();
				if (!checkLimit(limit, saldo, oldvalue, newvalue)) {
					javax.swing.JOptionPane.showMessageDialog(null,
							new raMultiLineMessage("Saldo dugovanja partnera "
									+ dm.getPartneri().getString("NAZPAR")
									+ " iznosi "
									+ calculateSaldo(saldo, oldvalue, newvalue)
											.setScale(2) + " kuna i prelazi "
									+ "limit kreditiranja koji iznosi "
									+ dm.getPartneri().getBigDecimal("LIMKRED")
									+ " kuna.!", JLabel.CENTER, 80), "Greška",
							javax.swing.JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		return true;
	}

	private BigDecimal calculateSaldo(java.math.BigDecimal saldo,
			java.math.BigDecimal oldvalue, java.math.BigDecimal newvalue) {

		saldo = saldo.subtract(oldvalue);
		saldo = saldo.add(newvalue);
		return saldo;
	}

	private BigDecimal calculateLimit(java.math.BigDecimal limit,
			java.math.BigDecimal saldo, java.math.BigDecimal oldvalue,
			java.math.BigDecimal newvalue) {
		BigDecimal zatrositi = Aus.zero2;
		zatrositi = limit.subtract(saldo);
		zatrositi = zatrositi.add(oldvalue);
		zatrositi = zatrositi.subtract(newvalue);
		return zatrositi;
	}

	public boolean checkLimit(java.math.BigDecimal limit,
			java.math.BigDecimal saldo, java.math.BigDecimal oldvalue,
			java.math.BigDecimal newvalue) {

		return !(calculateLimit(limit, saldo, oldvalue, newvalue).doubleValue() < 0);

	}

	public java.math.BigDecimal getSaldo() {
		hr.restart.zapod.dlgTotalPromet.Results res = hr.restart.zapod.dlgTotalPromet
				.findPromet(getMasterSet().getInt("CPAR"));
		return res.getSaldo();
	}

	public boolean isOtremniceExist() {
		return hr.restart.util.Util.getNewQueryDataSet(
				"select * from stdoki where cskl='"
						+ getMasterSet().getString("CSKL") + "' and vrdok='"
						+ getMasterSet().getString("VRDOK") + "' and god='"
						+ getMasterSet().getString("GOD") + "' and brdok="
						+ getMasterSet().getInt("BRDOK") + " and status='P'",
				true).getRowCount() != 0;

	}

	public boolean isCurrentOtrpremnicaExist() {
		return hr.restart.util.Util.getNewQueryDataSet(
				"select * from stdoki where cskl='"
						+ getMasterSet().getString("CSKL") + "' and vrdok='"
						+ getMasterSet().getString("VRDOK") + "' and god='"
						+ getMasterSet().getString("GOD") + "' and brdok="
						+ getMasterSet().getInt("BRDOK") + " and rbsid="
						+ getDetailSet().getInt("RBSID") + " and status='P'",
				true).getRowCount() != 0;
	}

	public void nulaStanje(QueryDataSet qdsstanje) {
		BigDecimal nula = Aus.zero2;
		qdsstanje.setBigDecimal("KOLPS", nula);
		qdsstanje.setBigDecimal("KOLUL", nula);
		qdsstanje.setBigDecimal("KOLIZ", nula);
		qdsstanje.setBigDecimal("KOLREZ", nula);
		qdsstanje.setBigDecimal("NABPS", nula);
		qdsstanje.setBigDecimal("MARPS", nula);
		qdsstanje.setBigDecimal("PORPS", nula);
		qdsstanje.setBigDecimal("VPS", nula);
		qdsstanje.setBigDecimal("NABUL", nula);
		qdsstanje.setBigDecimal("MARUL", nula);
		qdsstanje.setBigDecimal("PORUL", nula);
		qdsstanje.setBigDecimal("VUL", nula);
		qdsstanje.setBigDecimal("NABIZ", nula);
		qdsstanje.setBigDecimal("MARIZ", nula);
		qdsstanje.setBigDecimal("PORIZ", nula);
		qdsstanje.setBigDecimal("VIZ", nula);
		qdsstanje.setBigDecimal("KOL", nula);
		qdsstanje.setBigDecimal("ZC", nula);
		qdsstanje.setBigDecimal("VRI", nula);
		qdsstanje.setBigDecimal("NC", nula);
		qdsstanje.setBigDecimal("VC", nula);
		qdsstanje.setBigDecimal("MC", nula);
	}

	public boolean isUslugaOrTranzit() {
		return isUslugaOrTranzit(getDetailSet().getInt("CART"));
	}

	public boolean isUslugaOrTranzit(int cart) {
	  return !raVart.isStanje(cart);
/*		String vrart = hr.restart.util.Util.getNewQueryDataSet(
				"SELECT VRART FROM ARTIKLI WHERE CART=" + cart, true)
				.getString("VRART");
		return vrart.equalsIgnoreCase("U") || vrart.equalsIgnoreCase("T");
*/
	}

	public boolean testStanjeRACGRN() {

		if (getDetailSet().getString("CSKLART").equalsIgnoreCase(""))
			return true;
		BigDecimal old_kol = null;
		if (!raVart.isStanje(getDetailSet().getInt("CART"))) return true;
		/*String vrart = hr.restart.util.Util.getNewQueryDataSet(
				"SELECT VRART FROM artikli WHERE cart ="
						+ getDetailSet().getInt("CART"), true).getString(
				"VRART");
		if (vrart.equalsIgnoreCase("U") || vrart.equalsIgnoreCase("T"))
			return true;*/
		if (raDetail.getMode() == 'I') {
			old_kol = hr.restart.util.Util
					.getNewQueryDataSet(
							"SELECT KOL FROM STDOKI WHERE CSKL='"
									+ getDetailSet().getString("CSKL") + "' "
									+ "AND VRDOK='"
									+ getDetailSet().getString("VRDOK") + "' "
									+ "AND GOD='"
									+ getDetailSet().getString("GOD") + "' "
									+ "AND BRDOK="
									+ getDetailSet().getInt("BRDOK")
									+ "AND RBR="
									+ getDetailSet().getInt("RBSID"), true)
					.getBigDecimal("KOL");
		}

		if (old_kol != null
				&& getDetailSet().getBigDecimal("KOL").compareTo(old_kol) == 0) {
			return true;
		}

		if (hr.restart.sisfun.frmParam.getParam("robno", "chStanjeRiG", "N",
				"Provjera stanja kod GRN i RAC -a").equalsIgnoreCase("D")) {

			QueryDataSet qdsStanje = hr.restart.util.Util.getNewQueryDataSet(
					"select * from stanje where god='"
							+ getDetailSet().getString("GOD") + "' and cskl='"
							+ getDetailSet().getString("CSKLART")
							+ "' and cart=" + getDetailSet().getInt("CART"),
					true);
			if (qdsStanje == null || qdsStanje.getRowCount() == 0) {
				javax.swing.JOptionPane.showMessageDialog(null,
						"Ne postoji stanje za ovaj artikl !", "Greška",
						javax.swing.JOptionPane.ERROR_MESSAGE);
				return false;
			}

			BigDecimal stanjereal = qdsStanje.getBigDecimal("KOL").subtract(
					getDetailSet().getBigDecimal("KOL"));
			BigDecimal kolrezervirano = stanjereal.subtract(qdsStanje
					.getBigDecimal("KOLREZ"));

			if (stanjereal.doubleValue() < 0) {
				javax.swing.JOptionPane.showMessageDialog(null,
						"Nedovoljna zaliha artikla !", "Greška",
						javax.swing.JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (kolrezervirano.doubleValue() < 0) {
				String rezkol = hr.restart.sisfun.frmParam.getParam("robno",
						"rezkol4Stanje", "O",
						"Kalkulacija stanja kod rezervacije (D/N/O)");
				if (rezkol.equals("O")) {
					javax.swing.JOptionPane.showMessageDialog(raDetail
							.getWindow(), "Koristite rezervirane kolièine !",
							"Upozorenje",
							javax.swing.JOptionPane.WARNING_MESSAGE);
					return true;
				}
				if (rezkol.equals("D")) {
					javax.swing.JOptionPane.showMessageDialog(null,
							"Nedovoljna zaliha artikla !", "Greška",
							javax.swing.JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		return true;
	}

  public boolean isDosIzd() {
    return false;
  }

  public static boolean allowPriceChange() {
    return frmParam.getParam("robno", "priceChIzl", "N", "Dozvoliti izmjenu cijena na OTP, MEI, INM, OTR...(D/N)").equalsIgnoreCase("D");
  }

}