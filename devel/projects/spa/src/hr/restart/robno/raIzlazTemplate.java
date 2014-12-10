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

import hr.apis_it.fin._2012.types.f73.PorezType;
import hr.apis_it.fin._2012.types.f73.RacunType;
import hr.apis_it.fin._2012.types.f73.RacunZahtjev;
import hr.restart.baza.*;
import hr.restart.pos.presBlag;
import hr.restart.sisfun.TextFile;
import hr.restart.sisfun.frmParam;
import hr.restart.sk.raSaldaKonti;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raInputDialog;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.swing.raOptionDialog;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.*;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


abstract public class raIzlazTemplate extends hr.restart.util.raMasterDetail {

	// boolean isAutomatikaBrisanjaSvega = false;

	boolean isOJ = false;

	protected String key4delZag;

	private String cuser = "";

	private int savebrdok = 0;

	private String domval;

	public boolean isMaloprodajnaKalkulacija = false;
	
	public boolean isPopustMC = false;
	public boolean isAllowArtChange = false;
	public boolean isAutoUcesce = false;
	
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
	String keyforRN = null, keyfordod;

	Integer Broj;

	short nStavka; // redni broj stavke

	public boolean checkLimit = false;
	public boolean dospLimit = false;

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
	
	boolean isAutoFixPor = false;
	
	boolean isTranzit = false;
	
	boolean isSingleKOL = false;
	
	boolean isMinusAllowed = false;
	
	boolean isDospDatdok = false;
	
	boolean minKolCheck = false;
	
	boolean sigKolCheck = false;
	
	boolean chStanjeRiG = false;
	
	boolean docBefDatKnj = false;

	
	boolean autoval = false;
	boolean autovali = false; 

	int lastCparNavigated = -1;

	short delRbr = -1;

	int oldCPAR = -1;

	boolean hideKup = false;
	boolean allowNabedit = false;

	// private String vrzal="";

	// ab.f za brisanje stavki izdatnica prenesenih iz RNL
	protected int delRbsrn, delRbsid;

	protected String delCradnal = "", delId = "";

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

	String defNacpl = "";
	String defcrab = "";

	String dodatak = "";

	String dodatakRN = "";
	
	String rezkol = "D";
	String rezkolst = "D";

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
	
	raNavAction rnvFisk = new raNavAction("Zakljuèavanje / fiskalizacija",
        raImages.IMGSENDMAIL, java.awt.event.KeyEvent.VK_F7, java.awt.event.KeyEvent.SHIFT_MASK) {
      public void actionPerformed(ActionEvent e) {
        keyFisk();
      }
    };
    
    raNavAction rnvAkcija = new raNavAction("Akcija da/ne",
        raImages.IMGTIP, java.awt.event.KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        akcijaToggle();
      }
    };
    
    raNavAction rnvCopyPon = new raNavAction("Prebacivanje stavaka s ponude", 
    		raImages.IMGSENDMAIL, KeyEvent.VK_UNDEFINED) {
    	public void actionPerformed(ActionEvent e) {
        selectDoc();
    	}
    };
    
    raNavAction rnvEDI = new raNavAction("Prebacivanje dokumenta putem EDI", 
        raImages.IMGEXPORT, KeyEvent.VK_UNDEFINED) {
      public void actionPerformed(ActionEvent e) {
        sendDoc();
      }
    };
    
    raNavAction rnvPop = new raNavAction("Promjena popusta na raèunu", 
        raImages.IMGHISTORY, KeyEvent.VK_UNDEFINED) {
      public void actionPerformed(ActionEvent e) {
        changeGlobalPopust();
      }
    };
    
    String akcijaPrefix = null;
    void checkAkcijaPrefix() {
      akcijaPrefix = frmParam.getParam("robno", "akcijaPrefiks", "",
          "Prefiks naziva artikla za artikle na akciji (prazno za onemogucavanje)");
    }
    
    
    public static RacunType getRacType(DataSet ms) {
      
      lookupData ld = lookupData.getlookupData();
      dM dm = dM.getDataModule();
            
      DataSet ds = stdoki.getDataModule().getTempSet(Condition.whereAllEqual(Util.mkey, ms));
      ds.open();

      //BigDecimal osnpdv = Aus.zero2;
      BigDecimal osnpnp = Aus.zero2;
      //BigDecimal izpdv = Aus.zero2;
      BigDecimal izpnp = Aus.zero2;
      BigDecimal tot = Aus.zero2;
      BigDecimal povn = Aus.zero2;
      //BigDecimal povs = new BigDecimal("0.5");
      
      BigDecimal pdv25 = new BigDecimal("25").setScale(2);
      
      HashMap izmap = new HashMap();
      HashMap osnmap = new HashMap();
      
    
      for (ds.first(); ds.inBounds(); ds.next()) {
        BigDecimal por1 = ds.getBigDecimal("POR1");
        BigDecimal por2 = ds.getBigDecimal("POR2");
        BigDecimal osn = ds.getBigDecimal("IPRODBP");
        ld.raLocate(dm.getArtikli(), new String[]{"CART"}, new String[] {String.valueOf(ds.getInt("CART"))});
        /*if ("D".equals(dm.getArtikli().getString("POV"))) {
          BigDecimal povni = povs.multiply(ds.getBigDecimal("KOL")).
            setScale(2, BigDecimal.ROUND_HALF_UP);
          povn = povn.add(povni);
          osn = osn.subtract(povni);
        }*/
        
        if (por1.signum() != 0) {
          BigDecimal post = ds.getBigDecimal("PPOR1").setScale(2);
          BigDecimal osnpdv = (BigDecimal) osnmap.get(post);
          BigDecimal izpdv = (BigDecimal) izmap.get(post);
          osnmap.put(post, osn.add(osnpdv != null ? osnpdv : Aus.zero0));
          izmap.put(post, por1.add(izpdv != null ? izpdv : Aus.zero0));
        }
        
        if (por2.signum() != 0) osnpnp = osnpnp.add(osn);
        izpnp = izpnp.add(por2);
        tot = tot.add(ds.getBigDecimal("IPRODSP"));
      }
      
      System.out.println(tot);
      System.out.println(osnmap);
      System.out.println(izmap);
      System.out.println(osnpnp + " " + izpnp);
      System.out.println(povn);
      System.out.println(osnmap.get(pdv25));
      System.out.println(izmap.get(pdv25));
      
      ld.raLocate(dm.getLogotipovi(), "CORG", OrgStr.getKNJCORG(false));
      String oibf = dm.getLogotipovi().getString("OIB");
      
      DataRow usr = ld.raLookup(dM.getDataModule().getUseri(),"CUSER", ms.getString("CUSER"));
      String oibu = usr.getString("OIB");
      
      /*if (!presBlag.isUserOriented()) {
        DataRow blag = ld.raLookup(dM.getDataModule().getBlagajnici(), "CBLAGAJNIK", ms.getString("CBLAGAJNIK"));
        if (blag != null && blag.getString("OIB").length() > 0) oibu = blag.getString("OIB");
      }*/
      
      String nacpl = ms.getString("CNACPL");
      DataSet rate = Rate.getDataModule().getTempSet(Condition.whereAllEqual(Util.mkey, ms));
      rate.open();
      if (rate.getRowCount() == 1)
        nacpl = rate.getString("CNACPL");
      else if (rate.getRowCount() > 1)
        nacpl = "O";
      
      if (nacpl.length() == 0 || nacpl.equalsIgnoreCase("G") || nacpl.equalsIgnoreCase("N"))
        nacpl = "G";
      else if (nacpl.equalsIgnoreCase("C") || nacpl.equalsIgnoreCase("È"))
        nacpl = "C";
      else if (nacpl.equalsIgnoreCase("K") || nacpl.startsWith("K"))
        nacpl = "K";
      else if (nacpl.equalsIgnoreCase("T") || nacpl.startsWith("V"))
        nacpl = "T";
      else nacpl = "O";

      RacunType rac = presBlag.getFis(ms).createRacun(
          oibf, //oib firme (Rest Art) NE PREPISUJ!!
          presBlag.isFiskPDV(ms), //da li je obveznik pdv-a 
          ms.getTimestamp("SYSDAT"), // datum i vrijeme kreiranja racuna
          presBlag.isFiskSep(ms) ? "N" : "P", // oznaka slijednosti
          ms.getInt("FBR"), // broj racuna 
          ms.getString("FPP"), // oznaka poslovne jedinice
          ms.getInt("FNU"), // oznaka naplatnog mjesta
          new BigDecimal(25), //stopa pdv-a 
          (BigDecimal) osnmap.get(pdv25), //osnovica za pdv
          (BigDecimal) izmap.get(pdv25), //iznos pdv-a
          new BigDecimal(3), //stopa pnp-a
          osnpnp, //osnovica za pnp
          izpnp, //iznos pnp 
          null, //naziv naknade - defaults to 'Povradna naknada' 
          povn, //iznos naknade
          tot, //ukupan iznos racuna
          nacpl,//nacin placanja
          oibu,//oib prodavatelja (Ja) NE PREPISUJ!!
          false //da li je naknadna dostava
       );
      
      if (izmap.size() > 1 || (izmap.size() == 1 && izmap.containsKey(pdv25))) {
        for (Iterator i = izmap.keySet().iterator(); i.hasNext(); ) {
          BigDecimal post = (BigDecimal) i.next();
          if (post.compareTo(pdv25) == 0) continue;
          PorezType por = presBlag.getFis(ms).getFisFactory().createPorezType();
          por.setStopa(post);
          por.setOsnovica((BigDecimal) osnmap.get(post));
          por.setIznos((BigDecimal) izmap.get(post));
          if (rac.getPdv() == null)
            rac.setPdv(presBlag.getFis(ms).getFisFactory().createPdvType());

          rac.getPdv().getPorez().add(por);
          System.out.println(por);
        }
      }
      
      return rac;
    }
    
    
    public static boolean fisk(DataSet ms) {
      if (presBlag.isFiskal(ms) && (!ms.getString("FOK").equals("D") || ms.getString("JIR").length() == 0)) {
        
        try {
          
          Timestamp datvri = new Timestamp(System.currentTimeMillis());
          RacunZahtjev zahtj = presBlag.getFis(ms).createRacunZahtjev(
            presBlag.getFis(ms).createZaglavlje(datvri, null), 
            getRacType(ms));
          
          String jir = presBlag.getFis(ms).fiskaliziraj(zahtj);
          if (jir != null && jir.length() > 0 && !jir.startsWith("ZKI") && !jir.startsWith("false"))
            ms.setString("JIR", jir);
          else ms.setString("JIR", "");
          ms.saveChanges();
          return ms.getString("JIR").length() > 0;
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      return false;
    }
    
    void multiFisk() {
      DataSet ms = getMasterSet();
      Object[] brdoks = raMaster.getSelectionTracker().getSelection();
      Arrays.sort(brdoks);
      
      String fiskForm = frmParam.getParam("robno", "fiskForm", "[FBR]-[FPP]-[FNU]",
      "Format fiskalnog broja izlaznog dokumenta na ispisu");
      
      String resetSysdat = frmParam.getParam("robno", "fiskDatum", "N",
  		"Postaviti datum kreiranja kod fiskalizacije (D,N,A)");
    
      if (what_kind_of_dokument.equalsIgnoreCase("GOT") || what_kind_of_dokument.equalsIgnoreCase("GRN") ||
          (what_kind_of_dokument.equalsIgnoreCase("PRD") && ms.getString("PARAM").equals("K"))) {
        if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li fiskalizirati odabrane raèune?", "Fiskalizacija", 
            JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        
        if (resetSysdat.equalsIgnoreCase("A")) {
       	 int response = JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li postaviti datum kreiranja raèuna (na sada)?", 
       			 "Fiskalizacija", JOptionPane.YES_NO_CANCEL_OPTION);
       	 if (response == JOptionPane.CANCEL_OPTION) return;
       	 if (response == JOptionPane.YES_OPTION) resetSysdat = "D";
        }
        
         int count = 0;
         for (int i = 0; i < brdoks.length; i++) {                      
           if (!lD.raLocate(ms, "BRDOK", brdoks[i].toString())) continue;
           
           DataSet tms = doki.getDataModule().getTempSet(Condition.whereAllEqual(Util.mkey,  ms));
           tms.open();           
           if (tms.getString("FOK").equals("D")) continue;
           
           String cOpis = presBlag.getSeqOpis(ms);
           getMasterSet().setInt("FBR", Valid.getValid().findSeqInt(cOpis, true, false));
           getMasterSet().setString("FPP", presBlag.getFiskPP(ms));
           getMasterSet().setString("FOK", "D");
           getMasterSet().setInt("FNU", presBlag.isFiskGot(ms) ? presBlag.getFiskNapG(ms) : presBlag.getFiskNap(ms));
           getMasterSet().setString("PNBZ2", Aus.formatBroj(ms, fiskForm));
           if (resetSysdat.equalsIgnoreCase("D")) getMasterSet().setTimestamp("SYSDAT", Valid.getValid().getToday());
           getMasterSet().saveChanges();
           boolean succ = fisk(ms);
           if (succ) ++ count;
         }
         raMaster.getJpTableView().fireTableDataChanged();
         JOptionPane.showMessageDialog(raMaster.getWindow(), "Fiskalizirano " + count + " raèuna.", "Greška", JOptionPane.INFORMATION_MESSAGE);
      } else if (what_kind_of_dokument.equalsIgnoreCase("ROT") || what_kind_of_dokument.equalsIgnoreCase("RAC") ||
          what_kind_of_dokument.equalsIgnoreCase("IZD") || what_kind_of_dokument.equalsIgnoreCase("POD") ||
          what_kind_of_dokument.equalsIgnoreCase("TER") || what_kind_of_dokument.equalsIgnoreCase("ODB") ||
          (what_kind_of_dokument.equalsIgnoreCase("PRD") && !ms.getString("PARAM").equals("K"))) {
        
        if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li zakljuèiti odabrane raèune?", "Fiskalizacija", 
            JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        
        if (resetSysdat.equalsIgnoreCase("A")) {
       	  int response = JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li postaviti datum kreiranja raèuna (na sada)?", 
       			 "Zakljuèivanje", JOptionPane.YES_NO_CANCEL_OPTION);
       	  if (response == JOptionPane.CANCEL_OPTION) return;
       	  if (response == JOptionPane.YES_OPTION) resetSysdat = "D";
        }
        
        int count = 0;
        for (int i = 0; i < brdoks.length; i++) {
          if (!lD.raLocate(ms, "BRDOK", brdoks[i].toString()) || ms.getString("FOK").equals("D")) continue;
          
          String cOpis = presBlag.getSeqOpis(ms);
          getMasterSet().setInt("FBR", Valid.getValid().findSeqInt(cOpis, true, false));
          getMasterSet().setString("FPP", presBlag.getFiskPP(ms));
          getMasterSet().setString("FOK", "D");
          getMasterSet().setInt("FNU", presBlag.getFiskNap(ms));
          getMasterSet().setString("PNBZ2", Aus.formatBroj(ms, fiskForm));
          if (resetSysdat.equalsIgnoreCase("D")) getMasterSet().setTimestamp("SYSDAT", Valid.getValid().getToday());
          getMasterSet().saveChanges();
          ++count;
        }
        raMaster.getJpTableView().fireTableDataChanged();
        JOptionPane.showMessageDialog(raMaster.getWindow(), "Zakljuèano " + count + " raèuna.", "Greška", JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(raMaster.getWindow(), "Dokument se ne može fiskalizirati!", "Greška", JOptionPane.ERROR_MESSAGE);
      }
      
    }
    
    
    public void keyFisk() {
      DataSet ms = getMasterSet();
      if (ms.rowCount() == 0) return;
      
      if (!presBlag.isFiskal(ms)) {
        JOptionPane.showMessageDialog(raMaster.getWindow(), "Nije ukljuèena fiskalizacija pripadajuæeg poslovnog prostora!", 
            "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }
      
      if (raMaster.getSelectionTracker().countSelected() > 0) {
        multiFisk();
        return;
      }
      
      DataSet tms = doki.getDataModule().getTempSet(Condition.whereAllEqual(Util.mkey,  ms));
      tms.open();
      
      if (tms.getString("FOK").equals("D")) {
        JOptionPane.showMessageDialog(raMaster.getWindow(), "Dokument je veæ zakljuèan!", "Greška", JOptionPane.ERROR_MESSAGE);
        return;
      }
      
      /*int nap = presBlag.getFiskNap();
      int napg = presBlag.getFiskNapG();*/
      String fiskForm = frmParam.getParam("robno", "fiskForm", "[FBR]-[FPP]-[FNU]",
        "Format fiskalnog broja izlaznog dokumenta na ispisu");
      
      String resetSysdat = frmParam.getParam("robno", "fiskDatum", "N",
      		"Postaviti datum kreiranja kod fiskalizacije (D,N,A)");
      
      if (what_kind_of_dokument.equalsIgnoreCase("GOT") || what_kind_of_dokument.equalsIgnoreCase("GRN") ||
          (what_kind_of_dokument.equalsIgnoreCase("PRD") && ms.getString("PARAM").equals("K"))) {
        if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li fiskalizirati raèun?", "Fiskalizacija", 
            JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        
         if (resetSysdat.equalsIgnoreCase("A")) {
        	 int response = JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li postaviti datum kreiranja raèuna (na sada)?", 
        			 "Fiskalizacija", JOptionPane.YES_NO_CANCEL_OPTION);
        	 if (response == JOptionPane.CANCEL_OPTION) return;
        	 if (response == JOptionPane.YES_OPTION) resetSysdat = "D";
         }
        
         String cOpis = presBlag.getSeqOpis(ms);
         getMasterSet().setInt("FBR", Valid.getValid().findSeqInt(cOpis, true, false));
         getMasterSet().setString("FPP", presBlag.getFiskPP(ms));
         getMasterSet().setString("FOK", "D");
         getMasterSet().setInt("FNU", presBlag.isFiskGot(ms) ? presBlag.getFiskNapG(ms) : presBlag.getFiskNap(ms));
         getMasterSet().setString("PNBZ2", Aus.formatBroj(ms, fiskForm));
         if (resetSysdat.equalsIgnoreCase("D")) getMasterSet().setTimestamp("SYSDAT", Valid.getValid().getToday());
         getMasterSet().saveChanges();
         boolean succ = fisk(ms);
         raMaster.getJpTableView().fireTableDataChanged();
         JOptionPane.showMessageDialog(raMaster.getWindow(), "Dokument " + ms.getString("PNBZ2") + 
             (succ ? " fiskaliziran!" : " zakljuèan!"), "Fiskalizacija", JOptionPane.INFORMATION_MESSAGE);
      } else if (what_kind_of_dokument.equalsIgnoreCase("ROT") || what_kind_of_dokument.equalsIgnoreCase("RAC") ||
          what_kind_of_dokument.equalsIgnoreCase("IZD") || what_kind_of_dokument.equalsIgnoreCase("POD") ||
          what_kind_of_dokument.equalsIgnoreCase("TER") || what_kind_of_dokument.equalsIgnoreCase("ODB") ||
          (what_kind_of_dokument.equalsIgnoreCase("PRD") && !ms.getString("PARAM").equals("K"))) {
      	
      	if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li zakljuèiti raèun?", "Zakljuèivanje", 
            JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
      	
        if (resetSysdat.equalsIgnoreCase("A")) {
       	  int response = JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li postaviti datum kreiranja raèuna (na sada)?", 
       			 "Zakljuèivanje", JOptionPane.YES_NO_CANCEL_OPTION);
       	  if (response == JOptionPane.CANCEL_OPTION) return;
       	  if (response == JOptionPane.YES_OPTION) resetSysdat = "D";
        }
        
        String cOpis = presBlag.getSeqOpis(ms);
        getMasterSet().setInt("FBR", Valid.getValid().findSeqInt(cOpis, true, false));
        getMasterSet().setString("FPP", presBlag.getFiskPP(ms));
        getMasterSet().setString("FOK", "D");
        getMasterSet().setInt("FNU", presBlag.getFiskNap(ms));
        getMasterSet().setString("PNBZ2", Aus.formatBroj(ms, fiskForm));
        if (resetSysdat.equalsIgnoreCase("D")) getMasterSet().setTimestamp("SYSDAT", Valid.getValid().getToday());
        getMasterSet().saveChanges();
        raMaster.getJpTableView().fireTableDataChanged();
        JOptionPane.showMessageDialog(raMaster.getWindow(), "Dokument " + ms.getString("PNBZ2") + " zakljuèan!", "Fiskalizacija", JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(raMaster.getWindow(), "Dokument se ne može fiskalizirati!", "Greška", JOptionPane.ERROR_MESSAGE);
      }
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
	
	raNavAction rnvFixPor = new raNavAction("Popravi ukupni porez",
        raImages.IMGPREFERENCES, java.awt.event.KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        fixRac(false);
      }
	};

	public void addButtons(boolean copy, boolean norma) {
		// if (copy) raMaster.addOption(rnvCopyDoce,4);
		if (norma)
			raDetail.addOption(rnvNormaArt, 4);
		raMaster.addOption(rnvFixPor, 5);
	}

	abstract public void initialiser();
	
	static boolean nabDirect;
	static {
		nabDirect = frmParam.getParam("robno" ,"nabDirect", "N",
				"Moguænost unosa nabavnog iznosa na raèunima (D,N)").equalsIgnoreCase("D");
	}
	
	public static boolean isNabDirect() {
		return nabDirect;
	}

	public raIzlazTemplate() {

		super(1, 3);
		initialiser();
		// setUserCheck(true);
		
		hideKup =  (what_kind_of_dokument.equals("GOT") ||
            what_kind_of_dokument.equals("GRN")) &&
            frmParam.getParam("robno", "kupacHack", "N",
                "Omoguæiti skrivanje kupca na gotovinskim raèunima (D,N)").equals("D");
		
		allowNabedit = (what_kind_of_dokument.equals("RAC") ||
		    what_kind_of_dokument.equals("ODB") ||
		    what_kind_of_dokument.equals("TER")) &&
		    frmParam.getParam("robno", "allowNabedit", "N",
		        "Omoguæiti izmjenu nabavne vrijednosti proknjiženih RAC (D,N)").equals("D"); 
		
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
        
        if (frmParam.getParam("robno", "megaEdi", "N",
            "Dodati opciju za export u megatrend (D,N)").
            equalsIgnoreCase("D"))
          if (what_kind_of_dokument.equals("RAC") ||
              what_kind_of_dokument.equals("ROT") ||
              what_kind_of_dokument.equals("POD") ||
              what_kind_of_dokument.equals("ODB") ||
              what_kind_of_dokument.equals("NKU") ||
              what_kind_of_dokument.equals("TER") ||
              what_kind_of_dokument.equals("DOS"))
            raMaster.addOption(rnvEDI, 5, false);
        
        if (what_kind_of_dokument.equals("RAC") ||
            what_kind_of_dokument.equals("ROT") ||
            what_kind_of_dokument.equals("GOT") ||
            what_kind_of_dokument.equals("GRN") ||
            what_kind_of_dokument.equals("PON"))
          raMaster.addOption(rnvPop, 5, true);
        
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
	    
	    isAllowArtChange = frmParam.getParam("robno", "allowCartChange", "D",
	          "Dopustiti promjenu artikla na ponudama (D,N)").equalsIgnoreCase("D");
	    
	    isAutoUcesce = frmParam.getParam("robno", "autoUcesce", "N",
            "Dodati automatski uèešæe kao plaæanje raèuna u mini-saldak (D,N)").equalsIgnoreCase("D");
	    
	    defcrab = hr.restart.sisfun.frmParam.getParam("robno", "defcrab", "1",
            "Predefinirana šifra rabata stavke");
	    
	    defNacpl = hr.restart.sisfun.frmParam.getParam("robno", "defNacpl",
            "", "Predefinirani naèin plaæanja");

	    isTranzit = !isOJ && cskl.length() > 0 && 
	        TD.isCsklSklad(what_kind_of_dokument) && 
	        lD.raLocate(dm.getSklad(), "CSKL", cskl) &&
	        dm.getSklad().getString("VRSKL").equals("Z");
	    
	    isSingleKOL = frmParam.getParam("robno", "singleKOL", "N",
            "Defaultna kolièina od 1 kom (D,N)?").equals("D");
	    
	    docBefDatKnj = frmParam.getParam("robno","docBefDatKnj","N",
	        "Dozvoliti izradu dokumenta u periodu koje je veæ knjižen (D,N) ").equalsIgnoreCase("N");
	    
	    rezkol = hr.restart.sisfun.frmParam.getParam("robno", "rezkol", "N",
	        "Automatsko rezerviranje kolièina putem ponude (D,N,O)");
	    
	    rezkolst = hr.restart.sisfun.frmParam.getParam("robno",
            "rezkol4Stanje", "O",
            "Kalkulacija stanja kod rezervacije (D/N/O)");
	    
	    isDospDatdok = hr.restart.sisfun.frmParam.getParam("robno", "dospDatdok", "N",
            "Dospjeæe se raèuna od datuma dokumenta (D,N)").equalsIgnoreCase("D");
	    
	    isMinusAllowed = frmParam.getParam("robno", "allowMinus", "N",
	        "Dopustiti odlazak u minus na izlazima (D,N)?").equals("D");
	    
	    isAutoFixPor = frmParam.getParam("robno", "autoFixPor", "N",
            "Automatska provjera ukupnog poreza (D,N)?").equals("D");
	    
	    minKolCheck = hr.restart.sisfun.frmParam.getParam("robno", "minkol", 
	        "Provjera minimalne kolièine na stanju (D,N)", "N").equalsIgnoreCase("D");
	    
	    sigKolCheck = hr.restart.sisfun.frmParam.getParam("robno", "sigkol", 
	        "Provjera signalne kolièine na stanju (D,N)", "N").equalsIgnoreCase("D");
	    
	    chStanjeRiG = hr.restart.sisfun.frmParam.getParam("robno", "chStanjeRiG", "N",
          "Provjera stanja kod GRN i RAC -a").equalsIgnoreCase("D");
	        
	    String av = frmParam.getParam("robno", "autoValuta", "N",
	          "Preraèunati iznos iz valute u kune na izlazima (N,D,A)");
	    
	    if (what_kind_of_dokument.equals("ROT") || what_kind_of_dokument.equals("RAC")) {
	      checkLimit = hr.restart.sisfun.frmParam.getParam("robno","checkLimit","D","Provjera limita kreditiranja (D,N)").equalsIgnoreCase("D");
	      dospLimit = hr.restart.sisfun.frmParam.getParam("robno","dospLimit","D",
	          "Limit kreditiranja uzima samo dospjele raèune (D,N)").equalsIgnoreCase("D");
	    }
	    
	    autoval = !av.equalsIgnoreCase("N");
	    autovali = av.equalsIgnoreCase("A");
	  
		setNaslovMaster(master_titel + additional);
		setNaslovDetail(detail_titel_mno);
		raMaster.setkum_tak(true);
		raMaster.setstozbrojiti(new String[] {"UIRAC", "PLATITI", "UIPOPUST", "PROVISP"});
		
		setupDod();
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
		
		if (getMasterSet().getString("FOK").equals("D")) {
		  javax.swing.JOptionPane.showConfirmDialog(raMaster.getWindow(),
              "Zakljuèani dokumenti se ne mogu brisati!", "Greška",
              javax.swing.JOptionPane.DEFAULT_OPTION,
              javax.swing.JOptionPane.ERROR_MESSAGE);
		  return false;
		}
		
		boolean returnValue = true;
		srcString = util.getSeqString(getMasterSet());
		returnValue = util.checkSeq(srcString, Integer.toString(getMasterSet()
				.getInt("BRDOK")));
		if (returnValue && checkStavkeDel()) {
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
	
	public boolean checkStavkeDel() {
	  return true;
	}

	public boolean doBeforeSaveDetail(char mode) {

		if (mode == 'N') {
			cskl2csklart();
			getDetailSet().setString("ID_STAVKA",
                raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
                        "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
			
			raFLH.getInstance().izlazNoviFLH(getDetailSet());  /* FLH */
			
			maintanceRabat(true);
		} else if (mode == 'I') {
			cskl2csklart();
			
			raFLH.getInstance().izlazIzmjenaFLH(getDetailSet());  /* FLH */
			
			maintanceRabat(false);
		} else if (mode == 'B') {
			// deleteRabat();
		  
		  raFLH.getInstance().izlazBrisanjeFLH(getMasterSet(), delId);  /* FLH */
		}
		
		if (nabDirect && (mode == 'N' || mode == 'I') &&
            (what_kind_of_dokument.equals("ROT") ||
             what_kind_of_dokument.equals("GOT") ||
             what_kind_of_dokument.equals("POD"))) {
            Aus.set(getDetailSet(), "RNC", "NC");
            Aus.set(getDetailSet(), "RINAB", "INAB");
         }
		
		if (mode != 'B') 
			SanityCheck.basicStdoki(getDetailSet());
		if (mode == 'N' && autoval || mode == 'I' && autovali) {
		  if (!raSaldaKonti.isDomVal(getMasterSet()))
		    recalcVal();
		}
		return true;
	}
	
	void recalcVal() {
	  BigDecimal jv = raSaldaKonti.getJedVal(getMasterSet().getString("OZNVAL"));
	  Aus.mul(getDetailSet(), "FC", getMasterSet(), "TECAJ");
	  Aus.div(getDetailSet(), "FC", jv);
	  
      lc.setBDField("FC", getDetailSet().getBigDecimal("FC"), rKD.stavka);
      Kalkulacija("FC");
      lc.TransferFromClass2DB(getDetailSet(), rKD.stavka);
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
			vtrabat.setString("CRAB", defcrab);
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
	  if (isRabatShema) initRab();
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

			if (isMaloprodajnaKalkulacija && !bPonudaZaKupca)
				osnovica = getDetailSet().getBigDecimal("FMCPRP").multiply(getDetailSet().getBigDecimal("KOL")).setScale(2, BigDecimal.ROUND_HALF_UP);
			else osnovica = getDetailSet().getBigDecimal("INETO");
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
		// poravnaj eventualne lipe na ukupni popust na stavci, zbog zaokruzenja
		if (sumarabat.compareTo(getDetailSet().getBigDecimal("UIRAB")) != 0)
		  Aus.addSub(vtrabat, "IRAB", getDetailSet(), "UIRAB", sumarabat);
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
			if (ut.sameDay(val.getToday(), getMasterSet().getTimestamp("DATDOK")))
			  getMasterSet().setTimestamp("DATDOK", getMasterSet().getTimestamp("SYSDAT"));
			
			getMasterSet().setString("PNBZ2",
					pnb.getPozivNaBroj(getMasterSet()));
			if (!extrasave()) return false;			
		} else if (mode == 'I') {
			getMasterSet().setString("CUSER", cuser);
			if (getMasterSet().getString("FOK").equals("N"))
			  getMasterSet().setString("PNBZ2", pnb.getPozivNaBroj(getMasterSet()));
		}
		if (mode != 'B') 
			SanityCheck.basicDoki(getMasterSet());
		if (mode != 'B' && isAutoUcesce) {
		  getMasterSet().setBigDecimal("PLATITI",  getMasterSet().getBigDecimal("UIU"));
		  
		  String ikey = raControlDocs.getKey(getMasterSet(), "doki");
          QueryDataSet upls = UplRobno.getDataModule().getTempSet(Condition.equal("CDOC", ikey).and(Condition.equal("RBR", (short) 1)));
          upls.open();
          if (upls.rowCount() > 0) {
            upls.setBigDecimal("IZNOS", getMasterSet().getBigDecimal("UIU"));
            upls.setTimestamp("DATUM", getMasterSet().getTimestamp("DATDOK"));
          } else if (getMasterSet().getBigDecimal("UIU").signum() != 0) {
            upls.insertRow(false);
            upls.setShort("RBR", (short) 1);
            upls.setString("CDOC", ikey);
            upls.setBigDecimal("IZNOS", getMasterSet().getBigDecimal("UIU"));
            upls.setTimestamp("DATUM", getMasterSet().getTimestamp("DATDOK"));
            upls.setString("NAP", "uèešæe");
            upls.post();
          }
          if (upls.rowCount() > 0)
            raTransaction.saveChanges(upls);
          
          upls = UplRobno.getDataModule().getTempSet(Condition.equal("CDOC", ikey));
          upls.open();
          
          getMasterSet().setBigDecimal("PLATITI", Aus.sum("IZNOS", upls));
          checkPlac();
		}
		saveDod(mode);
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
			}
			qdsprij.deleteAllRows();
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
			  val.runSQL("DELETE FROM uplrobno WHERE " + Condition.equal("CDOC", key4del));
            } catch (Exception ex) {
              ex.printStackTrace();
              return false;
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
			if (mode == 'I' && !getMasterSet().getString("CNAMJ").equals(changeNamj)) {
              boolean nopor = lD.raLocate(dm.getNamjena(), "CNAMJ", getMasterSet().getString("CNAMJ"))
                                && dm.getNamjena().getString("POREZ").equals("N");
              boolean noporold = lD.raLocate(dm.getNamjena(), "CNAMJ", changeNamj)
                                && dm.getNamjena().getString("POREZ").equals("N");
			  if (nopor != noporold) recalcPor();
            }
			
			
			// int rbr = getMasterSet().getRow();
			getMasterSet().refetchRow(getMasterSet());
			// lD.raLocate(getMasterSet(), "BRDOK", String.valueOf(savebrdok));
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
		if (newm == 'B' && MP != null && MP.panelBasic != null) {
			MP.panelBasic.jpgetval.disableDohvat();
			if (MP.panelBasic.rpku != null)
			  MP.panelBasic.rpku.setInedit(false);
		}
	}

	public void SetFocusMasterBefore() {
	}

    private Runnable proc;
    public void doOnFocusNovi(Runnable proc) {
        this.proc = proc;
    }
      
    int changeCpar;
    String changeNamj;
	public void SetFokusMaster(char mode) {
		SetFocusMasterBefore();
		// if (MP.panelBasic == null)

		// if (getMasterSet().getInt("CPAR")==0 ||
		// getMasterSet().isAssignedNull("CPAR")){
		// MP.panelDodatni.jrfKO.setRaDataSet(
		// Kosobe.getDataModule().getTempSet("1=0"));
		// }
		setupDod();
		
		if (MP.panelBasic != null && MP.panelBasic.rpku != null)
		  MP.panelBasic.rpku.setInedit(true);

		if (mode == 'N') {
			pressel.copySelValues();
        } else fillDod();
        if (mode == 'I') {
          changeCpar = getMasterSet().getInt("CPAR");  // ab.f
          changeNamj = getMasterSet().getString("CNAMJ");
        }

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
		
		if (MP.gotpar) {
  		  MP.panelBasic.jrbPartner.setSelected(true);
          MP.panelBasic.partnerSelected(true);
          MP.panelBasic.jrbKupac.setSelected(false);
          MP.panelBasic.kupacSelected(false);
		}

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
		
		if (MP.gotpar) {
  		  if (getMasterSet().getInt("CPAR") != 0) {
            MP.panelBasic.jrbPartner.setSelected(true);
            MP.panelBasic.partnerSelected(true);
            MP.panelBasic.jrbKupac.setSelected(false);
            MP.panelBasic.kupacSelected(false);
          } else { //if (getMasterSet().getInt("CKUPAC") != 0) {
            MP.panelBasic.jrbPartner.setSelected(false);
            MP.panelBasic.partnerSelected(false);
            MP.panelBasic.jrbKupac.setSelected(true);
            MP.panelBasic.kupacSelected(true);
          }
		}
		
		oldCPAR = getMasterSet().getInt("CPAR");
		MP.EnabDisabforChange(false);
		SetFocusIzmjenaExtends();

	}

	public boolean LocalValidacijaMaster() {
		if (!MP.gotpar && val.isEmpty(MP.panelBasic.jrfCPAR))
			return false;
		return true;
	}

	public boolean ValidacijaMasterExtend() {
		return true;
	}
	
	public boolean isDatumToday() {
	  if (frmParam.getParam("robno", "gotToday", "N", 
	      "Upozorenje da datum nije današnji na gotovinskim raèunima (D,N)").equalsIgnoreCase("D")) {
	    if (!ut.sameDay(val.getToday(), getMasterSet().getTimestamp("DATDOK"))) {
	      if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Datum nije današnji! Nastaviti svejedno?", 
	          "Provjera datuma", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return false;
	    }
	  }
	  
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
        if (docBefDatKnj) {
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
		keyfordod = repUtil.getFormatBroj(getMasterSet());
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
			delId = getDetailSet().getString("ID_STAVKA");
			delCART = getDetailSet().getInt("CART");
			delRbr = getDetailSet().getShort("RBR");
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
		restarted = false;
		if (mode == 'I') {
			DP.rpcart.findStanjeUnconditional();
			focusOffOn(mode == 'N');
			findCPOR(); // (ab.f)
			EntryDetail(mode);
			tmpIPRODSP = getDetailSet().getBigDecimal("IPRODSP");
			tmpIRAZ = getDetailSet().getBigDecimal("IRAZ");
			if (isSingleKOL && DP.jraFC.isShowing())
			  DP.jraFC.requestFocus();
			else DP.jtfKOL.requestFocus();
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
    			  nacPlDod();
    			  if (vcdec.signum() != 0)
    			    getMasterSet().setBigDecimal("UIU", getMasterSet().getBigDecimal("UIRAC").multiply(vcdec).
    			  		movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP));
    			  checkPlac();
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
					if (!isUslugaOrTranzit()) {
						retValue = UpdateStanje();
					}
				}
				if (retValue) {
					retValue = UpdateDoki();
					/*if (retValue) {
						retValue = addRabati();
						if (retValue) {
							retValue = addZavtr();
						}
					}*/
				}
			}

			// klju\u010Devi za upis i provjeru zadnje kalkulacije
			if (mode == 'N'
					&& (TD.isDocDiraZalihu(getDetailSet().getString("VRDOK")) || getDetailSet()
							.getString("VRDOK").equalsIgnoreCase("DOS"))) {
				nStavka = (short) (nStavka + 1);
				try {
					if (!isUslugaOrTranzit()) {
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
		    nacPlDod();
		    if (vcdec.signum() != 0)
		      getMasterSet().setBigDecimal("UIU", getMasterSet().getBigDecimal("UIRAC").multiply(vcdec).
			  		movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP));
		    checkPlac();
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
			if (!isUslugaOrTranzit()
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
		if (TD.isDocDiraZalihu(getMasterSet().getString("VRDOK")) && 
		    raWebSync.active && raWebSync.isWeb(getMasterSet().getString("CSKL")) && 
		    raWebSync.isWeb(getDetailSet().getInt("CART"))) {
          raWebSync.updateStanje(getDetailSet().getInt("CART"), getMasterSet());
        }
	}

	final public void AfterDeleteDetail() {
		if (getDetailSet().getRowCount() == 0) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					enableDetailNavBar();
				}
			});
		}
		
		if (TD.isDocDiraZalihu(getMasterSet().getString("VRDOK")) &&
		    raWebSync.active && raWebSync.isWeb(getMasterSet().getString("CSKL")) && raWebSync.isWeb(delCART)) {
		  raWebSync.updateStanje(delCART, getMasterSet());
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
		System.out.println("valdp");
		System.out.println(isAllowArtChange);
		System.out.println(what_kind_of_dokument);
		System.out.println(rKD.stavkaold.rezkol);
		System.out.println(getDetailSet().getString("REZKOL"));
		
		if (raDetail.getMode() == 'N' || (isAllowArtChange && what_kind_of_dokument.equals("PON") &&
		      !rKD.stavkaold.rezkol.equalsIgnoreCase("D") && !getDetailSet().getString("REZKOL").equalsIgnoreCase("D"))) {
			if (this.DP.rpcart.getCART().trim().equals("")) {
				return true;
			} else {
			  if (raDetail.getMode() != 'N') restarted = true;
				ClearAll();
				// rki.Clean();
				setupRabat();
				isRabatCallBefore = false;
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
		reportsQuerysCollector.getRQCModule().caller = this;
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

          if (isNabDirect() && allowNabedit)
            raDetail.setEnabledNavAction(raDetail.getNavBar().
                    getStandardOption(raNavBar.ACTION_UPDATE),true);

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
		if (lastLimitCpar != getMasterSet().getInt("CPAR")) lastLimit = true;
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
		if (isFisk()) {
          setUserCheckMsg(
                  "Korisnik ne može promijeniti dokument jer je zakljuèan / fiskaliziran !",
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
		if (isDospDatdok)
		  getMasterSet().setTimestamp("DATDOSP",
		      ut.addDays(getMasterSet().getTimestamp("DATDOK"), getMasterSet().getShort("DDOSP")));
		  
	}

	public void jtfDVO_focusLost(FocusEvent e) {
	  if (!isDospDatdok)
        getMasterSet().setTimestamp("DATDOSP",
            ut.addDays(getMasterSet().getTimestamp("DVO"), getMasterSet().getShort("DDOSP")));
	}

	public void jtfDDOSP_focusLost(FocusEvent e) {
	  getMasterSet().setTimestamp("DATDOSP",
          ut.addDays(getMasterSet().getTimestamp(isDospDatdok ? "DATDOK" : "DVO"), getMasterSet().getShort("DDOSP")));
	}

	public void jtfDATDOSP_focusLost(FocusEvent e) {

		getMasterSet().setShort("DDOSP",
				(short) Math.round(hr.restart.util.Util.getUtil()
						.getHourDifference(getMasterSet().getTimestamp(isDospDatdok ? "DATDOK" : "DVO"),
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
			if (what_kind_of_dokument.equals("RAC") && frmParam.getParam("robno", "noDospRac", "N",
			    "Ignorirati dane dospjeæa za RAC (D,N)").equalsIgnoreCase("D"))
			  getMasterSet().setShort("DDOSP", (short) 0);  
			else getMasterSet().setShort("DDOSP", tmpPar.getShort("DOSP"));
			if (isDospDatdok) jtfDATDOK_focusLost(null); 
			else jtfDVO_focusLost(null);
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

	void initRab() {
	  if (!isRabatCallBefore)
        getrDR().getMyDataSet();
    
	  isRabatCallBefore = true;
	}
	
	void jbRabat_actionPerformed(ActionEvent e) {
		initRab();
		getrDR().show();
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
	
	private SimpleDateFormat edif = new SimpleDateFormat("ddMMyyyy");
	
	private SimpleDateFormat pantf = new SimpleDateFormat("yyyyMMdd");
	
	private DecimalFormat d4 = new DecimalFormat("#.0000");
	private DecimalFormat d2 = new DecimalFormat("#.00");
	private DecimalFormat d0 = new DecimalFormat("#");
	
	void err(String txt) {
	  throw new RuntimeException("Greška! " + txt);
	}
	
	void sendDocImplP() {
	  DataSet ms = getMasterSet();
      
      DataSet ds = stdoki.getDataModule().getTempSet(
          Condition.whereAllEqual(Util.mkey, ms));
      ds.open();
      //BigDecimal iznos = Aus.sum("IPRODSP", ds);
      
      String vr = ms.getString("VRDOK");
      
      if (vr.equals("DOS")) {
        VarStr buf = new VarStr();
        
        buf.append("G");
        buf.append(getPadded(ms.getString("BRDOKIZ")+"", 20));
        
        
        DataSet logo = dm.getLogotipovi();
        if (!lD.raLocate(logo, "CORG", OrgStr.getKNJCORG(false)))
          err("Neispravni logotip.");
        DataSet zr = dm.getZirorn();
        if (!lD.raLocate(zr, "ZIRO", logo.getString("ZIRO")))
          err("Neispravni ziro u logotipu.");
        
        DataSet par = dm.getPartneri();
        if (!lD.raLocate(par, "CPAR", Integer.toString(ms.getInt("CPAR"))))
          err("Neispravni partner na dokumentu.");
        
        String cp = par.getInt("CPAR") + "";
        
        
        buf.append(getPadded(par.getString("GLN"), 35));
        buf.append(getPadded(par.getString("NAZPAR"), 35));
        
        buf.append(getPadded(logo.getString("GLN"), 35));
        buf.append(getPadded(par.getString("GLN"), 35));
        
        if (ms.getInt("PJ") > 0) {
          DataSet pj = Pjpar.getDataModule().getTempSet(
              Condition.equal("CPAR", ms).and(
                  Condition.equal("PJ", ms)));
          pj.open();
          if (pj.rowCount() == 0)
            err("Neispravna poslovna jedinica dokumenta.");
          
          buf.append(getPadded(pj.getString("GLN"), 35));
          buf.append(getPadded(pj.getString("NAZPJ"), 35));
        } else {
          buf.append(getPadded(par.getString("GLN"), 35));
          buf.append(getPadded(par.getString("NAZPAR"), 35));
        }
        
        buf.append(getPadded(logo.getString("GLN"), 35));
        buf.append(getPadded(logo.getString("NAZIVLOG"), 35));
        
        buf.append(getPadded(pantf.format(ms.getTimestamp("DATDOK")), 35));
        buf.append(getPadded(pantf.format(ms.getTimestamp("DVO")), 35));
        if (ms.isNull("DATNARIZ")) buf.append(getPadded("", 35));
        else buf.append(getPadded(pantf.format(ms.getTimestamp("DATNARIZ")), 35));
        buf.append(getPadded(ms.getString("BRNARIZ"), 35));
        buf.append(getPadded("", 70));
        buf.append(getRPadded(ds.rowCount()+"", 18));
        buf.append(getPadded("", 114));
        buf.append(getPadded("9", 3));
        buf.append(getPadded("", 248));
        buf.append("\n");
        
        buf.append("E");
        buf.append(getPadded(logo.getString("GLN"), 35));
        buf.append(getPadded(ms.getString("BRDOKIZ")+"", 20));
        buf.append(getPadded("1", 6));
        buf.append(getPadded("", 213));
        buf.append("\n");
        
        for (ds.first(); ds.inBounds(); ds.next()) {
          buf.append("S");
          buf.append(getPadded(logo.getString("GLN"), 35));
          buf.append(getPadded(ms.getString("BRDOKIZ")+"", 20));
          buf.append(getRPadded("1", 6));
          buf.append(getRPadded(ds.getShort("RBR")+"", 6));
          buf.append(getPadded("5", 3));
          buf.append(getPadded(ds.getString("CART1"), 35));
          buf.append(getPadded(ds.getString("NAZART"), 35));
          buf.append(getPadded("", 35));
          buf.append(getPadded(ds.getString("BC"), 35));
          buf.append(getPadded("SRV", 3));
          buf.append(getPadded(ds.getString("CART1"), 35));
          buf.append(getRPadded("0", 15));
          if (ds.getString("JM").equalsIgnoreCase("pak") || ds.getString("JM").equalsIgnoreCase("kom"))
            buf.append("PCE");
          else buf.append("KGM");
          buf.append(getRPadded(ds.getShort("RBR")+"", 6));
          buf.append(getRPadded(d2.format(ds.getBigDecimal("KOL")), 15));
          buf.append(getPadded("", 15));
          /*buf.append(getRPadded(d4.format(ds.getBigDecimal("FVC")), 15));
          buf.append(getRPadded(d4.format(ds.getBigDecimal("FMC")), 15));
          buf.append(getRPadded(d2.format(ds.getBigDecimal("PPOR1")), 10));*/
          buf.append(getPadded("", 40));
          
          buf.append(getPadded("", 105));
          
          /*buf.append(getRPadded(d2.format(ds.getBigDecimal("IPRODBP")), 15));
          buf.append(getRPadded(d2.format(ds.getBigDecimal("POR1")), 15));*/
          buf.append(getPadded("", 30));
          buf.append(getPadded("CU", 17));
          buf.append(getPadded("", 153));          
          buf.append("\n");
        }
        
        TextFile.setEncoding("Cp1250");
        String fname = ms.getString("BRDOKIZ") + ".des";
        TextFile tf = TextFile.write(fname);
        tf.out(buf.chop().split('\n'));
        tf.close();
        
        String comm = frmParam.getParam("sisfun", "ediPantCmd", "", "Komanda za kopiranje fajla na EDI server (# = datoteka)");
        if (comm.length() >0) {
          comm = new VarStr(comm).replace("#", fname).toString();
          
          
          try {
            Runtime.getRuntime().exec(comm);
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          
          DataSet save = doki.getDataModule().getTempSet(
              Condition.whereAllEqual(Util.mkey, ms));
          save.open();
          save.setString("STATUS", "P");
          save.saveChanges();
          
          ms.refetchRow(ms);

        JOptionPane.showMessageDialog(this, "Dokument poslan.",
            "Export", JOptionPane.INFORMATION_MESSAGE);
        }
      }
	}
	
	void sendDocImpl() {
	  DataSet ms = getMasterSet();
	  
	  DataSet ds = stdoki.getDataModule().getTempSet(
          Condition.whereAllEqual(Util.mkey, ms));
      ds.open();
      BigDecimal iznos = Aus.sum("IPRODSP", ds);
      
      String vr = ms.getString("VRDOK");
      
      if (vr.equals("RAC") || vr.equals("ROT") ||
          vr.equals("POD") || vr.equals("ODB") ||
          vr.equals("TER") || vr.equals("NKU")) {
        
        boolean nar = false;
        
        VarStr buf = new VarStr();
        buf.append("HD");
        if (vr.equals("RAC") || vr.equals("ROT"))
          buf.append(iznos.signum() >= 0 ? "01" : "03");
        else if (vr.equals("POD") || vr.equals("ODB"))
          buf.append("02");
        else if (vr.equals("TER"))
          buf.append("04");
        else if (vr.equals("NDO") || vr.equals("NKU")) {
          buf.append("10");
          nar = true;
        } else
          err("Kriva vrsta dokumenta.");
        
        buf.append(nar ? "   " : "R-1");
        
        DataSet logo = dm.getLogotipovi();
        if (!lD.raLocate(logo, "CORG", OrgStr.getKNJCORG(false)))
          err("Neispravni logotip.");
        DataSet zr = dm.getZirorn();
        if (!lD.raLocate(zr, "ZIRO", logo.getString("ZIRO")))
          err("Neispravni ziro u logotipu.");
        
        buf.append(getPadded(logo.getString("GLN"), 13));
        buf.append(getPadded(logo.getString("NAZIVLOG"), 35));
        buf.append(getPadded(logo.getString("MATBROJ"), 13));
        buf.append(getPadded(logo.getString("ADRESA"), 35));
        buf.append(getPadded(logo.getString("MJESTO"), 15));
        buf.append(getPadded("HR", 8));
        buf.append(getPadded(logo.getInt("PBR")+"", 5));
        
        DataSet par = dm.getPartneri();
        if (!lD.raLocate(par, "CPAR", Integer.toString(ms.getInt("CPAR"))))
          err("Neispravni partner na dokumentu.");
        
        String cp = par.getInt("CPAR") + "";
        
        if (nar) {
          buf.append(getPadded(logo.getString("GLN"), 13));
          buf.append(getPadded(logo.getString("NAZIVLOG"), 35));
          buf.append(getPadded(logo.getString("MATBROJ"), 13));
        } else {
          buf.append(getPadded("3859888798007", 13));
          buf.append(getPadded(par.getString("NAZPAR"), 35));
          buf.append(getPadded(par.getString("MB"), 13));
        }
        /*if (par.getString("GLN").length() > 0)
          buf.append(getPadded(par.getString("GLN"), 13));
        else 
          buf.append(getPadded(cp, 13));*/
        
        
        DataSet pj = par;
        
        if (ms.getInt("PJ") > 0) {
          pj = Pjpar.getDataModule().getTempSet(
              Condition.equal("CPAR", ms).and(
                  Condition.equal("PJ", ms)));
          pj.open();
          if (pj.rowCount() == 0)
            err("Neispravna poslovna jedinica dokumenta.");
          cp = cp + "-" + pj.getInt("PJ");
        }
        if (pj.getString("GLN").length() > 0) {
          buf.append(getPadded(pj.getString("GLN"), 13));
        } else {
          buf.append(getPadded(cp, 13));
        }
        
        if (nar) buf.append(getPadded("", 13+13+35+15+8+5));
        else {
          buf.append(getPadded(logo.getString("GLN"), 13));
          buf.append(getPadded("3855002103856", 13));        
          buf.append(getPadded(par.getString("ADR"), 35));
          buf.append(getPadded(par.getString("MJ"), 15));
          buf.append(getPadded("HR", 8));
          buf.append(getPadded(par.getInt("PBR")+"", 5));
        }
        
        buf.append(edif.format(ms.getTimestamp("DVO")));
        buf.append(getPadded("", 96));
        buf.append(getPadded("", 16));
        buf.append(getPadded(logo.getString("OIB"), 11));
        buf.append(getPadded(par.getString("OIB"), 11));
        buf.append(getPadded("", 78));
        buf.append("\n");
        
        buf.append("DA");        
        buf.append(getPadded(ms.getString("PNBZ2"), 16));
        buf.append(nar ? " " : "1");
        buf.append(edif.format(ms.getTimestamp("DVO")));
        
        if (nar) {
          buf.append(getPadded(ms.getString("BRNARIZ"), 20));
          buf.append(getPadded("", 20+20+40+13+4+13+13+3+13));
        } else {
          buf.append(getPadded(ms.getString("BRNARIZ"), 20));
          buf.append(getPadded(ms.getString("CUG"), 20));
          buf.append(getPadded(ms.getString("BRDOKIZ"), 20));
          buf.append(getPadded("", 40));
          
          buf.append(getNum(Aus.sum("IPRODBP", ds), 13));
          buf.append(getPadded(ds.getBigDecimal("PPOR1").intValue()+"", 4));
          buf.append(getNum(Aus.sum("POR1", ds), 13));
          buf.append(getNum(Aus.zero2, 13));
          buf.append("HRK");
          buf.append(getNum(Aus.sum("IPRODSP", ds), 13));
        }
        
        buf.append(getPadded("VIRMAN", 10));
        
        if (nar) {
          buf.append(getPadded("", 8+30+30+18+13+87));
        } else {
          buf.append(edif.format(ms.getTimestamp("DATDOSP")));
          buf.append(getPadded("00 "+ms.getString("PNBZ2"), 30));
          buf.append(getPadded(zr.getString("BANKA"), 30));
          buf.append(getPadded(logo.getString("ZIRO"), 18));
          buf.append(getNum(Aus.sum("POR1", ds), 13));
          buf.append(getPadded("", 87));
        }
        buf.append("\n");
        
        for (ds.first(); ds.inBounds(); ds.next()) {
          buf.append("IT");
          buf.append(getNum(ds.getShort("RBR"), 2));
          if (ds.getString("BC").length() == 13)
            buf.append(getPadded(ds.getString("BC"), 13));
          else if (ds.getString("BC").length() == 7) {
            if (lD.raLocate(dm.getArtikli(), "CART1", ds.getString("CART1")) &&
                dm.getArtikli().getString("BCKOL").length() == 13)
              buf.append(getPadded(dm.getArtikli().getString("BCKOL"), 13));
            else buf.append(getPadded("", 13));
          } else buf.append(getPadded("", 13));
          buf.append(getPadded(ds.getString("CART1"), 20));
          buf.append(getPadded(ds.getString("NAZART"), 35));
          if (ds.getString("JM").equalsIgnoreCase("kg"))
            buf.append("KG");
          else if (ds.getString("JM").equalsIgnoreCase("kom"))
            buf.append("KO");
          else if (ds.getString("JM").equalsIgnoreCase("l"))
            buf.append("LI");
          else err("Neispravna jedinica mjere.");
          BigDecimal kol = ds.getBigDecimal("KOL");
          while (kol.scale() > 0)
            try {
              kol = kol.setScale(kol.scale() - 1);
            } catch (RuntimeException e) {
              break;
            }
          buf.append(getPadded(Aus.formatBigDecimal2(kol.abs()), 8));
          buf.append(getNum(ds.getBigDecimal("FVC"), 13));
          
          if (nar) {
            buf.append(getPadded("", 13+4));
          } else {
            buf.append(getNum(ds.getBigDecimal("POR1"), 13));
            buf.append(getPadded(ds.getBigDecimal("PPOR1").intValue()+"", 4));
          }
          
          buf.append(getNum(ds.getBigDecimal("IPRODBP"), 13));
          buf.append(getPadded("", 100));
          buf.append("\n");
        }

        TextFile.setEncoding("UTF-8");
        String fname = "mz-"+ms.getInt("BRDOK")+"-"
            +(System.currentTimeMillis()/1000) + ".txt";
        TextFile tf = TextFile.write(fname);
        tf.out(buf.chop().split('\n'));
        tf.close();
        
        try {
          System.out.println("slanje ftp");
          com.jcraft.jsch.JSch j = new com.jcraft.jsch.JSch();
          j.setKnownHosts(IntParam.getTag("sftp.hosts"));
          com.jcraft.jsch.Session sess = j.getSession(
              IntParam.getTag("sftp.user"), 
              IntParam.getTag("sftp.addr"),
              Aus.getNumber(IntParam.getTag("sftp.port")));
          sess.setPassword(IntParam.getTag("sftp.pass"));
          sess.connect();
          System.out.println("spojeno");

          com.jcraft.jsch.Channel channel = sess.openChannel("sftp");
          channel.connect();

          com.jcraft.jsch.ChannelSftp sch = (com.jcraft.jsch.ChannelSftp) channel;
          
          System.out.println("chg dir");
          sch.cd("out");
          System.out.println("remote dir " + sch.pwd());
          System.out.println("saving "+fname);
          sch.put(fname, fname);
          sch.exit();
          
          sess.disconnect();
          
          System.out.println("disconnected");
        } catch (com.jcraft.jsch.JSchException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          throw new RuntimeException("Greška kod slanja ftp-om!");
        } catch (com.jcraft.jsch.SftpException e) {
          e.printStackTrace();
          throw new RuntimeException("Greška kod slanja ftp-om!");
        }
        
        DataSet save = doki.getDataModule().getTempSet(
            Condition.whereAllEqual(Util.mkey, ms));
        save.open();
        save.setString("STATUS", "P");
        save.saveChanges();
        
        ms.refetchRow(ms);
        
        JOptionPane.showMessageDialog(this, "Dokument poslan.",
            "Export", JOptionPane.INFORMATION_MESSAGE);
      }
	}
	
	public void sendDoc() {
	  DataSet ms = getMasterSet();
	  if (ms.getRowCount() == 0) return;
	    
	  if (ms.getString("STATUS").equals("P"))
	    if (JOptionPane.showConfirmDialog(this, 
	        "Dokument je veæ prenesen! Ponoviti?", "Prijenos",
	        JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
	      return;
	  
	  try {
	    if (ms.getInt("CPAR") == 114) sendDocImplP();
	    else sendDocImpl();
	  } catch (RuntimeException e) {
	    e.printStackTrace();
	    JOptionPane.showMessageDialog(this,
	        e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
	  }
	}
	
	private String getPadded(String orig, int chars) {
	  if (orig.length() > chars)
	    return orig.substring(0, chars);
	  if (orig.length() < chars)
	    return orig.concat(Aus.string(chars - orig.length(), ' '));
	  return orig;
	}
	
	private String getRPadded(String orig, int chars) {
	  orig = new VarStr(orig).replace(',', '.').toString();
      if (orig.length() > chars)
        return orig.substring(0, chars);
      if (orig.length() < chars)
        return Aus.string(chars - orig.length(), ' ').concat(orig);
      return orig;
    }
	
	private String getNum(int num, int chars) {
      String txt = Integer.toString(num);
      if (txt.length() > chars) 
        return txt.substring(0, chars);
      if  (txt.length() < chars)
        return Aus.string(chars - txt.length(), '0').concat(txt);
      return txt;
    }
	
	private String getNum(BigDecimal num, int chars) {
	  String txt = num.abs().unscaledValue().toString();
	  if (txt.length() > chars) 
	    return txt.substring(0, chars);
	  if  (txt.length() < chars)
	    return Aus.string(chars - txt.length(), '0').concat(txt);
	  return txt;
	}
	
	
		
	public void selectDoc() {
	  
	  if (!checkAccess()) {
        showUserCheckMsg(); 
	    return;
	  }
    
    DataSet pon = doki.getDataModule().getTempSet(
    		"vrdok='PON' and god>='" + (Aus.getNumber(val.findYear()) - 1) +
    		"' and ((param='OJ' and " + Condition.in("CSKL", OrgStr.getOrgStr().getOrgstrAndCurrKnjig(), "CORG") +
    		") or (param!='OJ' and " + Condition.in("CSKL", util.getSkladFromCorg()) + "))");
    pon.open();

    try {
      lD.saveName = "dohvat-pon-for-copy";
      List modif = new ArrayList();
      modif.add(new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, dm.getPartneri()));
      lD.modifiers = modif;
      lD.setLookMode(lookupData.INDIRECT);
      String[] ret = lD.lookUp(raDetail.getWindow(), pon,
          util.mkey, new String[] {"", "", "", ""}, new int[] {1,4,5,6,44});

      if (ret != null && ret[0] != null && ret[0].length() > 0 && lD.raLocate(pon, util.mkey, ret))
        copyStavke(pon, false);

    } finally {
      lD.saveName = null;
      lD.modifiers = null;
    }
  }
	
	void copyStavke(DataSet pon, boolean all) {
		
		DataSet single = doki.getDataModule().getTempSet(Condition.whereAllEqual(util.mkey, pon));
		single.open();
		
		final SecondChooser sc = new SecondChooser("") {
		  public void afterOK() {
		    afterCopyStavke();
		  }
		  public boolean saveChangeInSecondChooser() {
		    if (!dodSaveCopyStavke()) return false;
		    return super.saveChangeInSecondChooser();
		  }
		};
		sc.setSelected("PON");		
		sc.setUpClass(this);
		
		sc.setDataSet(single);
    String[] dods = {"DATDOK"};
    sc.setUpClass(this);
    sc.setDataSetKey(new String[] { "CSKL", "GOD", "VRDOK", "BRDOK" }, dods);
    sc.initialise();
    sc.simTrans();
    if (all) {
      sc.okSelect();
      return;
    }
    
    sc.findStavke();
		if (sc.checkStavke()) {
			raLocalTransaction rLT = new raLocalTransaction() {
				public boolean transaction() {
					sc.addStavke();
					//cancelSelect();
					return sc.saveChangeInSecondChooser();
			}};				
			if (!rLT.execTransaction()){
				JOptionPane.showMessageDialog(this, "Prijenos nije uspio !",
						"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
			} else {
				dm.getSynchronizer().markAsDirty("vttext");
				raMaster.getJpTableView().fireTableDataChanged();
				/*raDetail.refreshTable();
				getDetailSet().last();*/
			}
		}
	}
	
	public void afterCopyStavke() {
	  
	}
	
	public boolean dodSaveCopyStavke() {
	  return true;
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
				&& !isKupArtExist) {

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
					// TODO napuniti vt_....
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

	private boolean restarted = false;
	public void findCStanje() {

		if ((raDetail.getMode() == 'N' || restarted)
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
			
			if ((what_kind_of_dokument.equals("GOT") || what_kind_of_dokument.equals("GRN")) 
			    && getMasterSet().getInt("CKUPAC")>0 &&
                lD.raLocate(dm.getPartneri(), "CKUPAC", getMasterSet().getInt("CKUPAC")+""))
              cpar = dm.getPartneri().getInt("CPAR");
        
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
			
			if (cpar > 0 && (what_kind_of_dokument.equals("GOT") || what_kind_of_dokument.equals("ROT") ||
					what_kind_of_dokument.equals("RAC") || what_kind_of_dokument.equals("PON") || 
					what_kind_of_dokument.equals("POD") || what_kind_of_dokument.equals("ODB"))) {
				
				DataSet ds = Rabshema.getDataModule().getTempSet(Condition.equal("CPAR", cpar).and(
						Condition.equal("CART", getDetailSet())));
				ds.open();
				boolean allgr = false;
				boolean direct = ds.rowCount() > 0;
				if (!direct) {
				  ds = Rabshema.getDataModule().getTempSet(Condition.equal("CPAR", cpar).and(
				            Condition.equal("ALLGR", "D")));
				  ds.open();
				  lD.raLocate(dm.getArtikli(), "CART", Integer.toString(getDetailSet().getInt("CART")));
				  String gr = dm.getArtikli().getString("CGRART");
				  
				  for (ds.first(); ds.inBounds(); ds.next()) {
				    lD.raLocate(dm.getArtikli(), "CART", Integer.toString(ds.getInt("CART")));
				    if (dm.getArtikli().getString("CGRART").equals(gr)) {
				      allgr = true;
				      break;
				    }
				  }
				}
				
				if (direct || allgr) {
					BigDecimal bdvc = Aus.zero2, bdmc = Aus.zero2;
					if (!allgr) {
    					if (ds.getBigDecimal("VC").signum() > 0) {
    						bdvc = ds.getBigDecimal("VC");
    						bdmc = bdvc.add(bdvc.multiply(getDetailSet().getBigDecimal("UPPOR").movePointLeft(2)).setScale(2, BigDecimal.ROUND_HALF_UP));
    					} else if (ds.getBigDecimal("MC").signum() > 0) {
    						bdmc = ds.getBigDecimal("MC");
    						bdvc = bdmc.divide(getDetailSet().getBigDecimal("UPPOR").movePointLeft(2).add(Aus.one0), 2, BigDecimal.ROUND_HALF_UP);
    					}
    					if (ds.getBigDecimal("VC").signum() > 0 || ds.getBigDecimal("MC").signum() > 0) {
    						lc.setBDField("FC", bdvc, rKD.stavka);
    						lc.setBDField("FVC", bdvc, rKD.stavka);
    						lc.setBDField("FMC", bdmc, rKD.stavka);
    						lc.setBDField("FMCPRP", bdmc, rKD.stavka);
    						
    					}
					}
					lc.TransferFromClass2DB(getDetailSet(), rKD.stavka);
					
					if (ds.getString("CRAB1").length() > 0) {
						initRab();
						lD.raLocate(dm.getRabati(), "CRAB", ds.getString("CRAB1")); 
						getrDR().insertTempRow((short) 1, ds.getString("CRAB1"), dm.getRabati().getString("NRAB"),
											dm.getRabati().getBigDecimal("PRAB"), "N");
						if (ds.getString("CRAB2").length() > 0) {
							lD.raLocate(dm.getRabati(), "CRAB", ds.getString("CRAB2")); 
							getrDR().insertTempRow((short) 2, ds.getString("CRAB2"), dm.getRabati().getString("NRAB"),
												dm.getRabati().getBigDecimal("PRAB"), dm.getRabati().getString("RABNARAB"));
							if (ds.getString("CRAB3").length() > 0) {
								lD.raLocate(dm.getRabati(), "CRAB", ds.getString("CRAB3")); 
								getrDR().insertTempRow((short) 3, ds.getString("CRAB3"), dm.getRabati().getString("NRAB"),
													dm.getRabati().getBigDecimal("PRAB"), dm.getRabati().getString("RABNARAB"));
							}
						}
						getrDR().afterJob();
					}
				}
			}
			
			nacPlDod();
			if (vcinc.signum() > 0) {
			  BigDecimal mc = lc.getBDField("FMC", rKD.stavka);
              BigDecimal vc = lc.getBDField("FVC", rKD.stavka);
              vc = vc.multiply(Aus.one0.add(vcinc.movePointLeft(2))).
                    setScale(2, BigDecimal.ROUND_HALF_UP);
              mc = mc.multiply(Aus.one0.add(vcinc.movePointLeft(2))).
                    setScale(2, BigDecimal.ROUND_HALF_UP);
              lc.setBDField("FC", vc, rKD.stavka);
              lc.setBDField("FVC", vc, rKD.stavka);
              lc.setBDField("FMC", mc, rKD.stavka);
              lc.setBDField("FMCPRP", mc, rKD.stavka);
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
		if (how.equals("KOL") && nabDirect)
			Aus.mul(getDetailSet(), "RINAB", "RNC", "KOL");
	}
  
	public void nabKal(String kako) {
		if (raDetail.getMode() == 'B' || !nabDirect) return;
		
		if (kako.equals("RNC"))
			Aus.mul(getDetailSet(), "RINAB", "RNC", "KOL");
		else if (getDetailSet().getBigDecimal("KOL").signum() != 0) 
			Aus.div(getDetailSet(), "RNC", "RINAB", "KOL");
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

				if (minKolCheck && DP.rpcart.jrfCART.getRaDataSet().getBigDecimal(
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

				if (sigKolCheck && DP.rpcart.jrfCART.getRaDataSet().getBigDecimal(
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
  
      if (prep == null) {
        System.out.println("prep je null");
        prepareQuery(odabrano);
      } else {
        System.out.println("prep nijje null, wtf");
      }
      dcz.setSelected(odabrano);
      dcz.setDataSet(prep == null ? qDS : prep);
      String[] dods = (prep == null ? 
          qDS.hasColumn("CPAR") != null : 
            prep.hasColumn("CPAR") != null) ?
              new String[] {"DATDOK", "CPAR", "UIRAC", "BRDOKIZ", "BRNARIZ"} : 
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
	
	StorageDataSet dodfield = new StorageDataSet();
	StorageDataSet oldfield = new StorageDataSet();
	JPanel dodpanel;
	public void setupDod() {
	  DataSet dsd = dokidod.getDataModule().getTempSet(
	      Condition.equal("BRRAC", "LABEL"));
	  dsd.open();
	  if (dsd.rowCount() == 0) return;
	  
	  dodpanel = new JPanel(new XYLayout(520, 40 + 25*dsd.rowCount()));
	  dodfield = new StorageDataSet();
	  for (int i = 0; dsd.inBounds(); dsd.next(), i++) {
	    dodfield.addColumn(dM.createStringColumn(
	        "KOL"+dsd.getInt("RBS"), dsd.getString("VAL"), 50));
	    dodpanel.add(new JLabel(dsd.getString("VAL")), 
	        new XYConstraints(15, 20 + 25*i, -1, -1));
	    JraTextField txt = new JraTextField();
	    txt.setDataSet(dodfield);
	    txt.setColumnName("KOL"+dsd.getInt("RBS"));
	    dodpanel.add(txt, new XYConstraints(150, 20 + 25*i, 300, -1));
	  }
	  oldfield = new StorageDataSet();
	  oldfield.setColumns(dodfield.cloneColumns());
	  dodfield.open();
	  oldfield.open();
	}
	
	raOptionDialog dlg = new raOptionDialog();
	public void showDod() {
	  if (dodfield.getColumnCount() == 0) return;
	  
	  JPanel all = new JPanel(new BorderLayout());
	  all.add(dodpanel);
	  all.add(dlg.getOkPanel(), BorderLayout.SOUTH);
	  oldfield.copyTo(dodfield);
	  if (dlg.show(raMaster.getWindow(), all, "Dodatni podaci"))
	    dodfield.copyTo(oldfield);
	}
	
	void fillDod() {
	  if (dodfield.getColumnCount() == 0) return;
	  
	  DataSet dsd = dokidod.getDataModule().getTempSet(
          Condition.equal("BRRAC", repUtil.getFormatBroj(getMasterSet())));
	  dsd.open();
	  for (dsd.first(); dsd.inBounds(); dsd.next())
	    oldfield.setString("KOL" + dsd.getInt("RBS"), dsd.getString("VAL"));
	}
	
	void saveDod(char mode) {
	  if (dodfield.getColumnCount() == 0) return;
	  
	  String brrac = mode == 'B' ? keyfordod : 
	      repUtil.getFormatBroj(getMasterSet());
	  QueryDataSet dsd = dokidod.getDataModule().getTempSet(
	    mode == 'N' ? Condition.nil : Condition.equal("BRRAC", brrac));
	  dsd.open();
	  dsd.deleteAllRows();
	  raTransaction.saveChanges(dsd);
	  if (mode == 'B') return;
	  
	  for (int i = 0; i < oldfield.getColumnCount(); i++) {
	    dsd.insertRow(false);
	    dsd.setString("BRRAC", brrac);
	    dsd.setInt("RBS", Aus.getNumber(oldfield.getColumn(i).
	        getColumnName().substring(3)));
	    dsd.setString("VAL", oldfield.getString(i));
	  }
	  raTransaction.saveChanges(dsd);
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
	
	public static String checkStorno(String vrdok) {
	  if (vrdok.equals("SRT")) return "ROT";
	  if (vrdok.equals("SPD")) return "PRD";
	  if (vrdok.equals("SRC")) return "RAC";
	  if (vrdok.equals("SGT")) return "GOT";
	  if (vrdok.equals("SGR")) return "GRN";
	  return vrdok;
	}

	public void prepareQuery(String odabrano) {
	  odabrano = checkStorno(odabrano);
	  /*if (odabrano.equals("SRT")) odabrano = "ROT";
	  if (odabrano.equals("SPD")) odabrano = "PRD";
	  if (odabrano.equals("SRC")) odabrano = "RAC";
	  if (odabrano.equals("SGT")) odabrano = "GOT";
	  if (odabrano.equals("SGR")) odabrano = "GRN";*/
	  
        System.out.println("Odabrano "+odabrano);
        String year = val.findYear(pressel.getSelRow().
            getTimestamp("DATDOK-to"));
        
        boolean ally = frmParam.getParam("robno", "sc2god", "D",
            "Dopustiti dohvat dokumenata iz prošlih godina (D,N)", true).equals("D");
        
        boolean ponPon = frmParam.getParam("robno", "ponPon", "N",
            "Dopustiti dohvat prenešenih ponuda (D,N)", true).equals("D");
        
        String ky = Valid.getValid().getKnjigYear("robno");
        
        String yc = ky.equalsIgnoreCase(year) ? "god='"+year+"' and " : "god in ('"+
              year+"','"+ky+"') and ";
        
        if (ally) yc = "";
        

		if (odabrano.equals("RN")) {
            qDS = RN.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK DATDOK",
                aSS.getS4raCatchDocRN(year, pressel.getSelRow()
							.getString("CSKL"), dodatakRN));
            qDS.open();
		} else {

			dodatak = dodatak(odabrano);
			String upit = "";
            aSS.ispisDA = true;
            

            if (getMasterSet().getString("VRDOK").equalsIgnoreCase("PON")
                && odabrano.equalsIgnoreCase("PON")) {
              upit = yc+" vrdok= 'PON'" + dodatak
              + " and cskl in ('"
              + pressel.getSelRow().getString("CSKL") + "')"; // samo
            } else if ((getMasterSet().getString("VRDOK").equalsIgnoreCase("RAC") ||
			    getMasterSet().getString("VRDOK").equalsIgnoreCase("GRN"))
					&& odabrano.equalsIgnoreCase("PON")) {
				upit = (ponPon ? "statira!='X'" : "statira='N'") + " and "+yc+" vrdok= 'PON'" + dodatak
						+ " and cskl in ('"
						+ pressel.getSelRow().getString("CSKL") + "')"; // samo
			} else if ((getMasterSet().getString("VRDOK").equalsIgnoreCase(
					"IZD") && odabrano.equalsIgnoreCase("POS"))
					|| (getMasterSet().getString("VRDOK").equalsIgnoreCase(
                    "IZD") && odabrano.equalsIgnoreCase("RAC"))
					|| (getMasterSet().getString("VRDOK").equalsIgnoreCase(
							"OTP") && odabrano.equalsIgnoreCase("POS"))) {
				VarStr vs = new VarStr("");
				StorageDataSet stt = OrgStr.getOrgStr().getOrgstrAndCurrKnjig();
				for (stt.first(); stt.inBounds(); stt.next()) {
					vs.append("'").append(stt.getString("CORG")).append("',");
				}
				vs.chopRight(1);

				upit = "statira='N' and god = '"
						+ year + "' and vrdok= '" + odabrano.toUpperCase() + "'" + dodatak
						+ " and cskl in (" + vs + ")"; // samo
			} else if (getMasterSet().getString("VRDOK").equalsIgnoreCase("PRD") && 
			    bPonudaZaKupca && "PON|PRD".indexOf(odabrano) >= 0) {
			  upit = yc+" vrdok= '" + odabrano + "'" + dodatak + " and statira='N' and cskl in ('"
                + pressel.getSelRow().getString("CSKL") + "')"; // samo
			} else if (getMasterSet().getString("VRDOK").equals(odabrano)) {
			  if (TD.isDocOJ(getMasterSet().getString("VRDOK"))) {
			    upit = yc+" vrdok='"+odabrano+"'"+dodatak+" and (cskl in " + 
			        OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndCurrKnjig(), "cskl") + ")"; 
			  } else {
			    String cskl = pressel.getSelRow().getString("CSKL");
			    upit = yc+" vrdok='"+odabrano+"'"+dodatak+" and cskl in (select cskl from sklad where sklad.corg in ('"+cskl+"') "+
                    " or sklad.knjig in ('"+cskl+"')) ";
			  }
			} else if (TD.isDocFinanc(getMasterSet().getString("VRDOK"))
					&& !TD.isDocSklad(getMasterSet().getString("VRDOK"))) {
				upit = aSS.getS4raCatchDoc(yc, pressel.getSelRow()
						.getString("CSKL"), odabrano, dodatak, true);
			} else {
				upit = aSS.getS4raCatchDoc(yc, pressel.getSelRow()
						.getString("CSKL"), odabrano, dodatak);
			}

            qDS = doki.getDataModule().getTempSet("CSKL GOD VRDOK BRDOK DATDOK CPAR UIRAC BRDOKIZ BRNARIZ", upit);
            qDS.open();
            System.out.println(qDS.rowCount());
            System.out.println(qDS.getOriginalQueryString());
            
            System.out.println(upit);
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
	
	public boolean isFisk() {
      return getMasterSet().getString("FOK").equalsIgnoreCase("D") && 
         (getMasterSet().getString("JIR").length() > 0 || 
          getMasterSet().getString("VRDOK").equals("GOT") ||
          getMasterSet().getString("VRDOK").equals("GRN"));
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
	
	BigDecimal vcinc = Aus.zero2, vcdec = Aus.zero2;
	void nacPlDod() {
		if (lD.raLocate(dm.getNacpl(), new String[] { "CNACPL" },
				new String[] { getMasterSet().getString("CNACPL")})) {
			vcinc = dm.getNacpl().getBigDecimal("VCINC");
			vcdec = dm.getNacpl().getBigDecimal("VCDEC");
		} else {
		  vcinc = Aus.zero2;
		  vcdec = Aus.zero2;
		}
	}
	
	void checkPlac() {
	  getMasterSet().setString("STATPLA", 
	   (getMasterSet().getBigDecimal("UIRAC").compareTo(getMasterSet().getBigDecimal("PLATITI")) == 0) ? "D" : "N");
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
				|| descriptor.equals("hr.restart.robno.repPonuda2V") 
				|| descriptor.equals("hr.restart.robno.repNarDobV")
				|| descriptor.equals("hr.restart.robno.repOdobrenjaV")
				|| descriptor.equals("hr.restart.robno.repOdobrenjaPV")
				|| descriptor.equals("hr.restart.robno.repInvoice")
				|| descriptor.equals("hr.restart.robno.repOffer")
				|| descriptor.equals("hr.restart.robno.repProformaInvoice")
				|| descriptor.equals("hr.restart.robno.repPovratnicaOdobrenjeV")
				);

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
	
	public void changeGlobalPopust() {
	  refilterDetailSet();
	  if (getDetailSet().rowCount() == 0) {
	    JOptionPane.showMessageDialog(this.raMaster, "Ne postoje stavke ovog raèuna!",
            "Greška", JOptionPane.ERROR_MESSAGE);
        return;
	  }
	  String gr = hr.restart.sisfun.frmParam.getParam("robno", "defglobrab", "",
          "Predefinirana šifra rabata na raèunu");
	  if (gr == null || gr.length() == 0) {
	    JOptionPane.showMessageDialog(this.raMaster, "Potrebno je definirati parametar defglobrab prije dodavanja rabata na raèun!",
	        "Nedostaje parametar", JOptionPane.WARNING_MESSAGE);
	    return;
	  }
	  lD.raLocate(dm.getRabati(), "CRAB", gr);
	  String nr = dm.getRabati().getString("NRAB");
	  	  
	  QueryDataSet ds = hr.restart.util.Util
      .getNewQueryDataSet("SELECT * FROM vtrabat where cskl ='"
              + getDetailSet().getString("CSKL") + "' AND VRDOK='"
              + getDetailSet().getString("VRDOK") + "' AND GOD='"
              + getDetailSet().getString("GOD") + "' AND BRDOK="
              + getDetailSet().getInt("BRDOK"));
	  
	  BigDecimal old = Aus.zero2;
	  for (ds.first(); ds.inBounds(); ds.next())
	    if (ds.getString("CRAB").equals(gr)) old = ds.getBigDecimal("PRAB");
	  
	  
	  JPanel pan = new JPanel(new XYLayout(415, 50));
	   StorageDataSet tds = new StorageDataSet();
	        tds.setColumns(new Column[] {
	                dM.createBigDecimalColumn("PRAB", 2)
	        });
	    tds.open();
	    tds.setBigDecimal("PRAB", old);

	    JraTextField prab = new JraTextField();
	    prab.setColumnName("PRAB");
	    prab.setDataSet(tds);
	    
	    pan.add(new JLabel("Popust na raèun"), new XYConstraints(15,15,-1,-1));
	    pan.add(prab, new XYConstraints(300, 15, 100, -1));
	    
	    raInputDialog od = new raInputDialog();
	    if (!od.show(null, pan, "Dodavanje popusta")) return;
	  
	   BigDecimal pop = tds.getBigDecimal("PRAB");
	   if (pop.compareTo(old) == 0) return;
	   
	   
	   BigDecimal uirac = Aus.zero2;
       for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next()) {

           rKD.stavka.Init();
           rKD.stavkaold.Init();
           rKD.setWhat_kind_of_document(getDetailSet().getString("VRDOK"));
           
           setupRabat();
           getrDR().getMyDataSet();
           isRabatShema = true;
           short lrbr = 0;
           DataSet dpd = getrDR().getDPDataSet();
           for (dpd.first(); dpd.inBounds(); dpd.next()) {
             if (dpd.getString("CRAB").equals(gr)) {
               dpd.setString("RABNARAB", dpd.atFirst() ? "N" : "D");
               dpd.setBigDecimal("PRAB", pop);
               isRabatShema = false;               
             }
             if (dpd.getShort("LRBR") > lrbr) lrbr = dpd.getShort("LRBR");
           }
           if (isRabatShema) {
             getrDR().insertTempRow((short) (lrbr + 1), gr, nr, pop, lrbr == 0 ? "N" : "D");
             isRabatShema = false;
           }
           
           getrDR().sumaPopusta();
           lc.TransferFromDB2Class(getDetailSet(), rKD.stavka);
           lc.printAll(rKD.stavka);
                      
           rKD.stavka.uprab = getrDR().sp;
           if (isMaloprodajnaKalkulacija) {
               rKD.MaloprodajnaKalkulacija();
           } else {
               rKD.kalkFinancPart();
           }
           lc.TransferFromClass2DB(getDetailSet(), rKD.stavka);
           uirac = uirac.add(getDetailSet().getBigDecimal("IPRODSP"));
           maintanceRabat(false);
           
           raTransaction.saveChangesInTransaction(new QueryDataSet[] {getDetailSet(), vtrabat});
           
           lc.printAll(rKD.stavka);
       }

       getMasterSet().setBigDecimal("UIRAC", uirac);
       nacPlDod();
       if (vcdec.signum() != 0)
         getMasterSet().setBigDecimal("UIU", uirac.multiply(vcdec).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP));
       checkPlac();
       getMasterSet().saveChanges();
       ValidacijaPrijeIzlazaDetail();
       //raMaster.getJpTableView().fireTableDataChanged();
	}
	
	public boolean ValidacijaPrijeIzlazaDetail() {
	  if (!isAutoFixPor) return true;
	  fixRac(true);
	  return true;
	}
	
	void recalcPor() {
	  QueryDataSet ds = stdoki.getDataModule().openTempSet(Condition.whereAllEqual(Util.mkey, getMasterSet()));
	  if (ds.rowCount() == 0) return;
	  
	  if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li preraèunati porez na svim stavkama?",
	      "Promjena poreza", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
	  
	  boolean nopor = lD.raLocate(dm.getNamjena(), "CNAMJ", getMasterSet().getString("CNAMJ"))
          && dm.getNamjena().getString("POREZ").equals("N");
	  
	  BigDecimal uirac = Aus.zero2;
      for (ds.first(); ds.inBounds(); ds.next()) {

          rKD.stavka.Init();
          rKD.stavkaold.Init();
          rKD.setWhat_kind_of_document(ds.getString("VRDOK"));
          
          if (lD.raLocate(dm.getArtikli(), "CART", String.valueOf(ds.getInt("CART")))) {
            if (lD.raLocate(dm.getPorezi(), "CPOR", dm.getArtikli().getString("CPOR"))) {
              ds.setBigDecimal("PPOR1", dm.getPorezi().getBigDecimal("POR1"));
              ds.setBigDecimal("PPOR2", dm.getPorezi().getBigDecimal("POR2"));
              ds.setBigDecimal("PPOR3", dm.getPorezi().getBigDecimal("POR3"));
              ds.setBigDecimal("UPPOR", dm.getPorezi().getBigDecimal("UKUPOR"));
            }
          }
          if (nopor) {
            ds.setBigDecimal("PPOR1", Aus.zero2);
            /*ds.setBigDecimal("PPOR2", Aus.zero2);
            ds.setBigDecimal("PPOR3", Aus.zero2);*/
            ds.setBigDecimal("UPPOR", ds.getBigDecimal("PPOR2"));
          }
          
          lc.TransferFromDB2Class(ds, rKD.stavka);
          if (isMaloprodajnaKalkulacija) {
              rKD.MaloprodajnaKalkulacija();
          } else {
              rKD.kalkFinancPart();
          }
          lc.TransferFromClass2DB(ds, rKD.stavka);
          uirac = uirac.add(ds.getBigDecimal("IPRODSP"));
      }

      getMasterSet().setBigDecimal("UIRAC", uirac);
      nacPlDod();
      if (vcdec.signum() != 0)
        getMasterSet().setBigDecimal("UIU", uirac.multiply(vcdec).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP));
      checkPlac();

      if (raTransaction.saveChangesInTransaction(new QueryDataSet[] {ds, getMasterSet()}))
          getDetailSet().refresh();
      
	  JOptionPane.showMessageDialog(raMaster.getWindow(), "Porez rekalkuliran na svim stavkama.", 
	      "Promjena poreza", JOptionPane.INFORMATION_MESSAGE);
	  
	}

	public void popust4All() {

		String str = "SELECT * FROM STDOKI WHERE CSKL='"
				+ getMasterSet().getString("CSKL") + "' and vrdok='"
				+ getMasterSet().getString("VRDOK") + "' and god='"
				+ getMasterSet().getString("GOD") + "' and brdok="
				+ getMasterSet().getInt("BRDOK");

		QueryDataSet forallpopust = hr.restart.util.Util.getNewQueryDataSet(str, true);

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
		nacPlDod();
		if (vcdec.signum() != 0)
		  getMasterSet().setBigDecimal("UIU", uirac.multiply(vcdec).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP));
		checkPlac();
/*		raLocalTransaction rltpopustAllApply = new raLocalTransaction() {
			public boolean transaction() throws Exception {
				raTransaction.saveChanges(forallpopust);
				raTransaction.saveChanges(getMasterSet());
				return true;
			}
		};
*/		if (raTransaction.saveChangesInTransaction(new QueryDataSet[] {forallpopust, getMasterSet()}))
			getDetailSet().refresh();
		
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
		  String cdkey = rCD.getKey(DP.rpcart.jrfCART.getRaDataSet());
		  System.out.println("CKEY="+cdkey);
			QueryDataSet tmpVTTEXT = hr.restart.util.Util.getNewQueryDataSet(
					"SELECT * FROM vttext WHERE CKEY='" + cdkey + "'", true);
			
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
			if (dm.getPartneri().getString("STATUS").equalsIgnoreCase("C")) {
			  javax.swing.JOptionPane.showMessageDialog(null, "Partneru je zabranjeno fakturiranje!", "Greška", JOptionPane.ERROR_MESSAGE);
			  return false;
			}

			java.math.BigDecimal limit = dm.getPartneri().getBigDecimal(
					"LIMKRED");
			if (dm.getPartneri().getString("STATUS").equalsIgnoreCase("B")) {
				java.math.BigDecimal saldo = getSaldo();
				if (!checkLimit(limit, saldo, oldvalue, newvalue)) {
				  
					if (JOptionPane.showConfirmDialog(null,
							new raMultiLineMessage("Saldo dugovanja partnera "
									+ dm.getPartneri().getString("NAZPAR")
									+ " iznosi "
									+ calculateSaldo(saldo, oldvalue, newvalue)
											.setScale(2) + " kuna\nprelazi "
									+ "limit kreditiranja koji iznosi "
									+ dm.getPartneri().getBigDecimal("LIMKRED")
									+ " kuna! Nastaviti ipak?", JLabel.CENTER, 80), "Prekoraèenje limita kreditiranja",
									JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.ERROR_MESSAGE) != JOptionPane.YES_OPTION)
					return false;
				}
			}
		}
		return true;
	}

	private BigDecimal calculateSaldo(java.math.BigDecimal saldo,
			java.math.BigDecimal oldvalue, java.math.BigDecimal newvalue) {
	  
	  if (dospLimit && getMasterSet().getTimestamp("DATDOSP").after(
	      ut.getLastSecondOfDay(val.getToday()))) return saldo;

		saldo = saldo.subtract(oldvalue);
		saldo = saldo.add(newvalue);
		return saldo;
	}

	private BigDecimal calculateLimit(java.math.BigDecimal limit,
			java.math.BigDecimal saldo, java.math.BigDecimal oldvalue,
			java.math.BigDecimal newvalue) {
	  
	  if (dospLimit && getMasterSet().getTimestamp("DATDOSP").after(
          ut.getLastSecondOfDay(val.getToday()))) return limit.subtract(saldo);
	  
		BigDecimal zatrositi = Aus.zero2;
		zatrositi = limit.subtract(saldo);
		zatrositi = zatrositi.add(oldvalue);
		zatrositi = zatrositi.subtract(newvalue);
		return zatrositi;
	}
	
	private int lastLimitCpar = -1;
	private boolean lastLimit = true;

	public boolean checkLimit(java.math.BigDecimal limit,
			java.math.BigDecimal saldo, java.math.BigDecimal oldvalue,
			java.math.BigDecimal newvalue) {
	  if (!lastLimit && lastLimitCpar == getMasterSet().getInt("CPAR")) return true;
	  
	  lastLimitCpar = getMasterSet().getInt("CPAR");
	  lastLimit = !(calculateLimit(limit, saldo, oldvalue, newvalue).doubleValue() < 0);

		return lastLimit;

	}

	public java.math.BigDecimal getSaldo() {
		hr.restart.zapod.dlgTotalPromet.Results res = hr.restart.zapod.dlgTotalPromet
				.findPromet(getMasterSet().getInt("CPAR"));
		return dospLimit ? res.getDospSaldo() : res.getSaldo();
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

		if (chStanjeRiG) {
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
				
				if (rezkolst.equals("O")) {
					javax.swing.JOptionPane.showMessageDialog(raDetail
							.getWindow(), "Koristite rezervirane kolièine !",
							"Upozorenje",
							javax.swing.JOptionPane.WARNING_MESSAGE);
					return true;
				}
				if (rezkolst.equals("D")) {
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
    
  public void fixRac(boolean auto) {
    if (getMasterSet().getRowCount() == 0) return;
    
    if (!checkAccess()) {
      if (auto || !showUserCheckMsg()) return;
    }
    
    if (!auto) refilterDetailSet();
    
    if (getDetailSet().getRowCount() == 0) return;
    
    if (!TypeDoc.getTypeDoc().isDocFinanc(what_kind_of_dokument)) return;
    
    DataSet ds = getDetailSet();
    DataSet art = dm.getArtikli();
    boolean nopor = lD.raLocate(dm.getNamjena(), "CNAMJ", getMasterSet().getString("CNAMJ"))
                      && dm.getNamjena().getString("POREZ").equals("N");
    
    HashMap totals = new HashMap();
    
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (!lD.raLocate(art, "CART", Integer.toString(ds.getInt("CART"))))
        return;
      PorData pd = (PorData) totals.get(art.getString("CPOR"));
      if (pd == null) 
        totals.put(art.getString("CPOR"), pd = new PorData(nopor));
      pd.add(ds);
      pd.calc(art.getString("CPOR"));
    }
    BigDecimal err = Aus.zero2;
    for (Iterator i = totals.values().iterator(); i.hasNext(); ) {
      PorData pd = (PorData) i.next();
      err = err.add(pd.por1.subtract(pd.rpor1).abs());
      err = err.add(pd.por2.subtract(pd.rpor2).abs());
      err = err.add(pd.por3.subtract(pd.rpor3).abs());
    }
    if (err.signum() == 0) {
      if (!auto) JOptionPane.showMessageDialog(raMaster.getWindow(), 
          "Nema greške ukupnog poreza na ukupnoj osnovici.", "Poruka",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    if (JOptionPane.showConfirmDialog(raMaster.getWindow(),
        "Želite li ažurirati razliku od " + err.toString() + 
        " poreza na stavkama raèuna?", "Razlika", 
        JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
      return;
    BigDecimal uirac = Aus.zero2;
    for (ds.first(); ds.inBounds(); ds.next()) {
      lD.raLocate(art, "CART", Integer.toString(ds.getInt("CART")));
      PorData pd = (PorData) totals.get(art.getString("CPOR"));
      pd.check(ds);
      uirac = uirac.add(ds.getBigDecimal("IPRODSP"));
    }
    getMasterSet().setBigDecimal("UIRAC", uirac);
    nacPlDod();
    if (vcdec.signum() != 0)
      getMasterSet().setBigDecimal("UIU", uirac.multiply(vcdec).movePointLeft(2).setScale(2, BigDecimal.ROUND_HALF_UP));
    checkPlac();
    if (raTransaction.saveChangesInTransaction(
        new QueryDataSet[] {getMasterSet(), getDetailSet()})) 
      JOptionPane.showMessageDialog(raMaster.getWindow(), 
          "Raèun ažuriran.", "Poruka",
          JOptionPane.INFORMATION_MESSAGE);
    else JOptionPane.showMessageDialog(raMaster.getWindow(), 
        "Greška kod snimanja podataka!", "Greška",
        JOptionPane.ERROR_MESSAGE);
  }
  
  class PorData {
    public BigDecimal lipa = Aus.one0.movePointLeft(2);
    public BigDecimal osn, por1, por2, por3, tot;
    public BigDecimal rpor1, rpor2, rpor3;
    public BigDecimal ppor1, ppor2, ppor3;
    boolean nopor = false;
    public PorData() {
      osn = por1 = por2 = por3 = tot = Aus.zero2;
    }
    
    public PorData(boolean npor) {
      osn = por1 = por2 = por3 = tot = Aus.zero2;
      nopor = npor;
    }
    
    public void add(DataSet ds) {
      osn = osn.add(ds.getBigDecimal("IPRODBP"));
      por1 = por1.add(ds.getBigDecimal("POR1"));
      por2 = por2.add(ds.getBigDecimal("POR2"));
      por3 = por3.add(ds.getBigDecimal("POR3"));
      tot = tot.add(ds.getBigDecimal("IPRODSP"));
    }
    
    public void calc(String cpor) {
      DataSet por = dm.getPorezi();
      lD.raLocate(por, "CPOR", cpor);
      ppor1 = nopor ? Aus.zero0 : por.getBigDecimal("POR1");
      rpor1 = osn.multiply(ppor1).movePointLeft(2).
                  setScale(2, BigDecimal.ROUND_HALF_UP);
      ppor2 = /*nopor ? Aus.zero0 : */ por.getBigDecimal("POR2");
      rpor2 = osn.multiply(ppor2).movePointLeft(2).
                  setScale(2, BigDecimal.ROUND_HALF_UP);
      ppor3 = /*nopor ? Aus.zero0 : */ por.getBigDecimal("POR3");
      rpor3 = osn.multiply(ppor3).movePointLeft(2).
                  setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    public void check(DataSet ds) {
      int inc = comp(ds, "POR1", ppor1);
      if (por1.compareTo(rpor1) > 0 && inc < 0) {
        Aus.sub(ds, "POR1", lipa);
        Aus.sub(ds, "IPRODSP", lipa);
        por1 = por1.subtract(lipa);
      } else if (por1.compareTo(rpor1) < 0 && inc > 0) {
        Aus.add(ds, "POR1", lipa);
        Aus.add(ds, "IPRODSP", lipa);
        por1 = por1.add(lipa);
      }
      inc = comp(ds, "POR2", ppor2);
      if (por2.compareTo(rpor2) > 0 && inc < 0) {
        Aus.sub(ds, "POR2", lipa);
        Aus.sub(ds, "IPRODSP", lipa);
        por2 = por2.subtract(lipa);
      } else if (por2.compareTo(rpor2) < 0 && inc > 0) {
        Aus.add(ds, "POR2", lipa);
        Aus.add(ds, "IPRODSP", lipa);
        por2 = por2.add(lipa);
      }
      inc = comp(ds, "POR3", ppor3);
      if (por3.compareTo(rpor3) > 0 && inc < 0) {
        Aus.sub(ds, "POR3", lipa);
        Aus.sub(ds, "IPRODSP", lipa);
        por3 = por3.subtract(lipa);
      } else if (por3.compareTo(rpor3) < 0 && inc > 0) {
        Aus.add(ds, "POR3", lipa);
        Aus.add(ds, "IPRODSP", lipa);
        por3 = por3.add(lipa);
      }
    }
    
    int comp(DataSet ds, String por, BigDecimal ppor) {
      return ds.getBigDecimal("IPRODBP").multiply(ppor).
         movePointLeft(2).subtract(ds.getBigDecimal(por)).signum();
    }
  }

  public static boolean allowPriceChange() {
    return frmParam.getParam("robno", "priceChIzl", "N", "Dozvoliti izmjenu cijena na OTP, MEI, INM, OTR...(D/N)").equalsIgnoreCase("D");
  }
  
  public static boolean allowIznosChange() {
    return frmParam.getParam("robno", "iznosChange", "N", "Dapustiti izmjenu iznosa s porezom (D/N)").equalsIgnoreCase("D");
  }

}