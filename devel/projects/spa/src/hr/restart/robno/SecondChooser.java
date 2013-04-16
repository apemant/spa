/****license*****************************************************************
**   file: SecondChooser.java
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
import hr.restart.baza.Partneri;
import hr.restart.baza.VTprijenos;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraDialog;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.Aus;
import hr.restart.util.LinkClass;
import hr.restart.util.OKpanel;
import hr.restart.util.lookupData;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.raTwoTableChooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

public class SecondChooser extends JraDialog {

	/*
	 * polje tmp_prijenos string 40 tmp_kolicina bigdecimal treba ih pripremiti
	 * za prijenos dokumenata po stavkama
	 */

	private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

	private raTwoTableChooser rTTC = new raTwoTableChooser(this);

	private QueryDataSet LijeviSet = new QueryDataSet();

	private QueryDataSet tmpKupArt = new QueryDataSet();

	private QueryDataSet tmpCijenik = new QueryDataSet();

	private QueryDataSet DesniSet = new QueryDataSet();

	private StorageDataSet StavkeSet = new StorageDataSet();

	private QueryDataSet findStavkeSet = new QueryDataSet();

	private QueryDataSet ZaglavljeSet = new QueryDataSet();

	private QueryDataSet ZaglavljeSetTmp = new QueryDataSet();

	private QueryDataSet RabatiSetTmp = new QueryDataSet();

	private QueryDataSet ZavtrSetTmp = new QueryDataSet();

	private allSelect aSS = new allSelect();

	private raControlDocs rCD = new raControlDocs();

	private DataSet docDs;

	private String[] keys;

	private Variant tmpVarijant = new Variant();

	private raIzlazTemplate rIT;

	private raKalkulBDDoc rKD = new raKalkulBDDoc();

	private TypeDoc TD = TypeDoc.getTypeDoc();

	private LinkClass lc = LinkClass.getLinkClass();

	private allStanje AST = allStanje.getallStanje();

	private allPorezi ALP = allPorezi.getallPorezi();

	private String selected = "";

	private raPrenosVT rPVT = new raPrenosVT();

	private hr.restart.util.lookupData lD = hr.restart.util.lookupData
			.getlookupData();

	private java.math.BigDecimal uirac = new java.math.BigDecimal(0.00);

	private boolean bprennormativ = false;

	private boolean bpitaozanormativ = false;
    
    private boolean rnlOpis = false;
    
    private boolean transOtp = false;
    private boolean transPnbz = false;
    
    private boolean copySkladParam = false;
    private boolean copySklad = false;

	private boolean isMultipleDocs = false;
	
	private boolean allowMinus = false;
	
	private boolean storno = false;

	OKpanel okpanel = new OKpanel() {
		public void jBOK_actionPerformed() {
			okSelect();
		}

		public void jPrekid_actionPerformed() {
			cancelSelect();
		}
	};

	public void setSelected(String odabrano) {
	  selected = raIzlazTemplate.checkStorno(odabrano);
	  storno = !selected.equals(odabrano);
/*	  if (odabrano.equals("SRT")) {
	    odabrano = "ROT";
	    storno = true;
	  } else if (odabrano.equals("SPD")) {
        odabrano = "PRD";
        storno = true;
      } else storno = false;
	  selected = odabrano;*/
	}

	public void okSelect() {

		/**
		 * @todo napraviti status Z u radni nalog unijeti broj racuna u polje
		 *       CFAKTURE (concat cskl,vrdok,god,brdok)
		 */

	  Artikli.getDataModule().fixSort();
		isMultipleDocs = false;
        directRNL = false;
        fixDOS = false;
        bprennormativ = false;
        rnlOpis = frmParam.getParam("robno", "rnlTransOpis", "N",
            "Prenijeti opis radnog naloga na raèun (D,N)?").equals("D");
        
        transOtp = frmParam.getParam("robno", "transBrdokOtp", "D",
            "Prenijeti broj otpremnice na raèun (D,N)").equals("D");
        
        transPnbz = frmParam.getParam("robno", "transPoziv", "D",
        "Prenijeti poziv na broj s ponude na raèun (D,N)").equals("D");
        
        copySkladParam = frmParam.getParam("robno", "copySkladPrice", "N",
             "Prepisati skladišne cijene kod prijenosa (D,N)").equals("D");
        
        copyDatum = frmParam.getParam("robno", "copyOtpDatum", "N",
        		"Prepisati datum s otpremnice kod prebacivanja u raèun (D,N)").equals("D");
        
		if (DesniSet.getRowCount() > 0) {
			if (checkZaglavlje()) {
				findStavke();
				if (checkStavke()) {
					raLocalTransaction rLT = new raLocalTransaction() {
						public boolean transaction() {
							addZaglavlje();
							addStavke();
							//cancelSelect();
							return saveChangeInSecondChooser();
					}};				
					if (!rLT.execTransaction()){
						JOptionPane.showMessageDialog(this, "Prijenos nije uspio !",
								"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
						cancelSelect();
					} else {
						// mora biti u posebnoj transakciji jer mora izažurirati veæ izažurirano stanje
						
						/*raLocalTransaction rLTrezer = new raLocalTransaction() {
							public boolean transaction() {
								rijesirezervaciju();
								return true;
						}};				

						rLTrezer.execTransaction();*/
						dm.getSynchronizer().markAsDirty("vttext");
						
						cancelSelect();
						afterOK();
					}
					
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Nema podataka za prijenos !",
					"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	public void afterOK() {
	}
    
    private void fixDosp() {
      if (!rIT.getMasterSet().isNull("CPAR") && rIT.getMasterSet().getInt("CPAR") > 0 &&
          (rIT.getMasterSet().isNull("DATDOSP") || 
             raDateUtil.getraDateUtil().DateDifference(
              rIT.getMasterSet().getTimestamp("DATDOK"),
              rIT.getMasterSet().getTimestamp("DATDOSP")) == 0)) {
        QueryDataSet tmpPar = Partneri.getDataModule().getTempSet(
          "cpar=" + rIT.getMasterSet().getInt("CPAR"));
        tmpPar.open();
        if (tmpPar.rowCount() > 0) {
          // && !MP.panelBasic.jrfCPAR.getText().equals("")) {
          rIT.getMasterSet().setShort("DDOSP", tmpPar.getShort("DOSP"));
          rIT.jtfDVO_focusLost(null);
        }
      }
    }

	public void addZaglavlje() {
		System.out.println("rIT.getMasterSet() "
				+ rIT.getMasterSet().getString("CUSER"));
		if (ZaglavljeSet.getString("VRDOK").equals("RNL")) {
          boolean kupac = ZaglavljeSetTmp.getString("KUPPAR").equalsIgnoreCase("K");
			if (TD.isGOTGRN(rIT.getMasterSet().getString("VRDOK"))) {
				if (ZaglavljeSet.isNull("CKUPAC")) {
					rIT.getMasterSet().setAssignedNull("CKUPAC");
				} else {
                  if (kupac) rIT.getMasterSet().setInt("CKUPAC", ZaglavljeSet.getInt("CKUPAC"));
                  else {
                    DataSet par = Partneri.getDataModule().getTempSet(
                        Condition.equal("CPAR", ZaglavljeSet.getInt("CKUPAC")));
                    par.open();
                    if (par.rowCount() == 1)
                      rIT.getMasterSet().setInt("CKUPAC", par.getInt("CKUPAC"));
                  }
				}
			} else {
				if (!kupac) {
					if (ZaglavljeSet.isNull("CKUPAC")) {
						rIT.getMasterSet().setAssignedNull("CPAR");
					} else {
						rIT.getMasterSet().setInt("CPAR", ZaglavljeSet.getInt("CKUPAC"));
					}
				} else {
                  DataSet par = Partneri.getDataModule().getTempSet(
                      Condition.equal("CKUPAC", ZaglavljeSet));
                  par.open();
                  if (par.rowCount() == 1) {
                    rIT.getMasterSet().setInt("CPAR", par.getInt("CPAR"));
                    fixDosp();
                  } else if (ZaglavljeSet.isNull("CPAR")) {
						rIT.getMasterSet().setAssignedNull("CPAR");
					} else {
						rIT.getMasterSet().setInt("CPAR", ZaglavljeSet.getInt("CPAR"));
                        fixDosp();
					}
				}
			}
			rIT.getMasterSet().setString("CRADNAL", ZaglavljeSetTmp.getString("CRADNAL"));
		} else {
			//System.out.println("JEDAN");
			if (rIT.getMasterSet().isAssignedNull("CPAR")
					|| rIT.getMasterSet().getInt("CPAR") == 0) {
				if (ZaglavljeSet.isAssignedNull("CPAR")
						|| ZaglavljeSet.getInt("CPAR") == 0) {
					rIT.getMasterSet().setAssignedNull("CPAR");
				} else {
					//System.out.println("DVA");
					rIT.getMasterSet().setInt("CPAR",
							ZaglavljeSet.getInt("CPAR"));
                    fixDosp();                      
				}
			}
			if (rIT.getMasterSet().isAssignedNull("CKUPAC")
					|| rIT.getMasterSet().getInt("CKUPAC") == 0) {
				if (ZaglavljeSet.isAssignedNull("CKUPAC")
						|| -ZaglavljeSet.getInt("CKUPAC") == 0) {
					rIT.getMasterSet().setAssignedNull("CKUPAC");
				} else {
					rIT.getMasterSet().setInt("CKUPAC",
							ZaglavljeSet.getInt("CKUPAC"));
				}
			}
		}
		if (!(ZaglavljeSet.isAssignedNull("CKO") || ZaglavljeSet.getInt("CKO") == 0)) {
			rIT.getMasterSet().setInt("CKO", ZaglavljeSet.getInt("CKO"));
		}
		if (!(ZaglavljeSet.isAssignedNull("PJ") || ZaglavljeSet.getInt("PJ") == 0)) {
			rIT.getMasterSet().setInt("PJ", ZaglavljeSet.getInt("PJ"));
		}
		
		if (rIT.getMasterSet().getString("VRDOK").equalsIgnoreCase("PON")) {
		  if (rIT.isOJ) rIT.getMasterSet().setString("PARAM", "OJ");
		  else if (rIT.isMaloprodajnaKalkulacija) rIT.getMasterSet().setString("PARAM", "K");
		}
		
		if (rIT.getMasterSet().getString("VRDOK").equalsIgnoreCase("PRD") && rIT.isMaloprodajnaKalkulacija) {
		  rIT.getMasterSet().setString("PARAM", "K");
		}
        
		if (ZaglavljeSet.getString("VRDOK").equalsIgnoreCase("NKU") &&
		    rIT.getMasterSet().getString("VRDOK").equalsIgnoreCase("DOS")) {
		  rIT.getMasterSet().setString("BRNARIZ", ZaglavljeSet.getString("BRNARIZ"));
          if (!ZaglavljeSet.isNull("DATNARIZ"))
            rIT.getMasterSet().setTimestamp("DATNARIZ", ZaglavljeSet.getTimestamp("DATNARIZ"));
		}
		
        if ((ZaglavljeSet.getString("VRDOK").equalsIgnoreCase("DOS")
        		|| ZaglavljeSet.getString("VRDOK").equalsIgnoreCase("OTP"))
            && (rIT.getMasterSet().getString("VRDOK").equalsIgnoreCase("RAC") 
            || rIT.getMasterSet().getString("VRDOK").equalsIgnoreCase("ROT")
            || rIT.getMasterSet().getString("VRDOK").equalsIgnoreCase("POD"))) {
          rIT.getMasterSet().setString("BRNARIZ", ZaglavljeSet.getString("BRNARIZ"));
          if (!ZaglavljeSet.isNull("DATNARIZ"))
            rIT.getMasterSet().setTimestamp("DATNARIZ", ZaglavljeSet.getTimestamp("DATNARIZ"));
          if (ZaglavljeSet.getString("VRDOK").equalsIgnoreCase("DOS") 
              || transOtp) {
            rIT.getMasterSet().setString("BRDOKIZ", repUtil.getFormatBroj(ZaglavljeSet));

            if (ZaglavljeSet.getString("VRDOK").equalsIgnoreCase("DOS")) {
                if (frmParam.getParam("robno", "copyDOSbrdok", "N",
                     "Prenijeti broj izlaznog dokumenta s DOS-a (D,N)").equalsIgnoreCase("D"))
                  rIT.getMasterSet().setString("BRDOKIZ", ZaglavljeSet.getString("BRDOKIZ"));
                rIT.getMasterSet().setString("OPIS", ZaglavljeSet.getString("OPIS"));
            }
            rIT.getMasterSet().setTimestamp("DATDOKIZ", ZaglavljeSet.getTimestamp("DATDOK"));
          }
          rIT.getMasterSet().setString("CRADNAL", ZaglavljeSet.getString("CRADNAL"));
        }

     if (copyDatum && ZaglavljeSet.getString("VRDOK").equalsIgnoreCase("OTP")
 				&& rIT.getMasterSet().getString("VRDOK").equalsIgnoreCase("RAC")) {
    	 dM.copyColumns(ZaglavljeSet, rIT.getMasterSet(), new String[] {"DATDOK", "DVO", "DATDOSP"});
     }
     if (fixDOS) {
       dM.copyColumns(ZaglavljeSet, rIT.getMasterSet(), new String[] {"DVO", "DATDOSP"});
     }
        
		if (transPnbz && ZaglavljeSet.rowCount() == 1 && ZaglavljeSet.getString("VRDOK").equalsIgnoreCase("PON")
				&& (rIT.getMasterSet().getString("VRDOK").equalsIgnoreCase(
						"RAC") || rIT.getMasterSet().getString("VRDOK")
						.equalsIgnoreCase("ROT"))) {
			rIT.getMasterSet().setString("PNBZ2",
					ZaglavljeSet.getString("PNBZ2"));
			if (!(ZaglavljeSet.isAssignedNull("CAGENT") || ZaglavljeSet
					.getInt("CAGENT") == 0)) {
				rIT.getMasterSet().setInt("CAGENT",
						ZaglavljeSet.getInt("CAGENT"));
			}
		}
		
        if (!ZaglavljeSet.isNull("DATUG"))
          rIT.getMasterSet().setTimestamp("DATUG", 
              ZaglavljeSet.getTimestamp("DATUG"));
        if (!ZaglavljeSet.isNull("CUG"))
          rIT.getMasterSet().setString("CUG", 
              ZaglavljeSet.getString("CUG"));

		if (rIT.raMaster.getMode() != 'I') {
			rIT.doBeforeSaveMaster(rIT.raMaster.getMode());
		}
	}

	public boolean checkZaglavlje() {

		boolean zavratiti = true;
		if (DesniSet.getRowCount() > 1) {
			if (!TD.isMnogostrukPrijenos(DesniSet.getString("VRDOK"))) {
				JOptionPane.showMessageDialog(this,
						"Može se prenijeti samo jedan "
								+ DesniSet.getString("VRDOK") + " dokument!",
						"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		// ovo jos treba malo prepraviti
		int cpar = -1;
		int ckupac = -1;
		String cshrab = "-1";
		String cshzt = "-1";
		//    boolean zavratiti = true;
		ZaglavljeSet.open();
		//ZaglavljeSetTmp.open();
		DesniSet.open();
		ZaglavljeSet.deleteAllRows();
		ZaglavljeSet.enableDataSetEvents(false);
		//ZaglavljeSetTmp.enableDataSetEvents(false);
		DesniSet.enableDataSetEvents(false);
		ArrayList al = new ArrayList();
		for (DesniSet.first(); DesniSet.inBounds(); DesniSet.next()) {
			al.add(new Integer(DesniSet.getInt("BRDOK")));
		}
		aSS.ispisDA = true;
		ZaglavljeSetTmp = hr.restart.util.Util.getNewQueryDataSet(aSS
				.getQuery4ZaglavljeSet(docDs.getTableName(), DesniSet
						.getString("CSKL"), DesniSet.getString("GOD"), DesniSet
						.getString("VRDOK"), al), true);
		/*
		 * System.out.println("ZaglavljeSetTmp.getInt(CPAR) " +
		 * ZaglavljeSetTmp.getInt("CPAR"));
		 * System.out.println("ZaglavljeSetTmp.isAssignedNull(CPAR) " +
		 * ZaglavljeSetTmp.isAssignedNull("CPAR"));
		 * System.out.println("rIT.getMasterSet().getInt(CPAR) " +
		 * rIT.getMasterSet().getInt("CPAR"));
		 * System.out.println("rIT.getMasterSet().isAssignedNull(CPAR)" +
		 * rIT.getMasterSet().isAssignedNull("CPAR"));
		 */

		if ((ZaglavljeSetTmp.isAssignedNull("CPAR") || ZaglavljeSetTmp
				.getInt("CPAR") == 0)
				&& (ZaglavljeSetTmp.isAssignedNull("CKUPAC") || ZaglavljeSetTmp
						.getInt("CKUPAC") == 0)) {
			if ((rIT.getMasterSet().isAssignedNull("CPAR") || rIT
					.getMasterSet().getInt("CPAR") == 0)
					&& (rIT.getMasterSet().isAssignedNull("CKUPAC") || rIT
							.getMasterSet().getInt("CKUPAC") == 0)) {
				if (!(rIT.getMasterSet().getString("VRDOK").equalsIgnoreCase(
						"GRN") || rIT.getMasterSet().getString("VRDOK")
						.equalsIgnoreCase("GOT"))) {
					JOptionPane.showMessageDialog(
									this,
									"Partner ili kupac na ovom dokumentu nije upisan !",
									"Greška",
									javax.swing.JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
        
        // provjera zamjenskog ureðaja
        if (ZaglavljeSetTmp.getString("VRDOK").equals("RNL") &&
            ZaglavljeSetTmp.getString("ZAMJENA").equals("D")) {
          if (JOptionPane.showConfirmDialog(this, 
              "Za nalog " + ZaglavljeSetTmp.getString("CRADNAL") + 
              " izdan je zamjenski artikl. Nastaviti?", "Zamjenski artikl",
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) !=
                JOptionPane.OK_OPTION) return false;
        }
        
        // ako se radi o prijenosu neizdanog radnog naloga, postavi flag
        // za direktnu ekspanziju naloga u racun. (ab.f)
        directRNL = (ZaglavljeSetTmp.getString("VRDOK").equals("RNL") &&
            ZaglavljeSetTmp.getString("STATUS").equals("P"));
        
        // provjeri treba li rjesavati skladisnu kolicinu za ovaj prijenos
        
        fixDOS = ("ROT".equalsIgnoreCase(rIT.what_kind_of_dokument)
            || "OTP".equalsIgnoreCase(rIT.what_kind_of_dokument)
            || "POD".equalsIgnoreCase(rIT.what_kind_of_dokument))
            && ZaglavljeSetTmp.getString("VRDOK").equalsIgnoreCase("DOS");
        
        /*if (!fixDOS && "OTP".equalsIgnoreCase(rIT.what_kind_of_dokument) &&
            ZaglavljeSetTmp.getString("VRDOK").equalsIgnoreCase("RAC")) {
          QueryDataSet vtpr = VTprijenos.getDataModule().getTempSet(
              Condition.equal("KEYDEST", rCD.getKey(ZaglavljeSetTmp, "doku")));
          vtpr.open();
          if (vtpr.rowCount() > 0 && vtpr.getString("KEYSRC").indexOf("-DOS-") >= 0)
            fixDOS = true;
        }*/
        if (fixDOS) System.out.println("We have a FIXDOS!");

		aSS.ispisDA = false;

		for (ZaglavljeSetTmp.first(); ZaglavljeSetTmp.inBounds(); ZaglavljeSetTmp
				.next()) {
			ZaglavljeSet.insertRow(true);
			copyDS2DS(ZaglavljeSetTmp, ZaglavljeSet);
		}
		return zavratiti;
	}

	public void setajColone() {
		Column[] kolone = dm.getDoki().getColumns();
		for (int i = 0; i < kolone.length; i++) {
			ZaglavljeSetTmp.addColumn((Column) kolone[i].clone());
			ZaglavljeSet.addColumn((Column) kolone[i].clone());
		}
	}

	public boolean handleMultiple() {
		HashMap hm = new HashMap();
		StorageDataSet tmpSDS = new StorageDataSet();
		Column[] cols = StavkeSet.getColumns();
		for (int i = 0; i < cols.length; i++) {
			tmpSDS.addColumn((Column) cols[i].clone());
		}
		tmpSDS.open();
		for (StavkeSet.first(); StavkeSet.inBounds(); StavkeSet.next()) {
			if (hm.containsKey(String.valueOf(StavkeSet.getInt("CART")))) {
				if (lD.raLocate(tmpSDS, "CART", String.valueOf(StavkeSet
						.getInt("CART")))) {
					if (TD.isDocSklad(StavkeSet.getString("VRDOK"))) {
						if (StavkeSet.getBigDecimal("ZC").compareTo(
								tmpSDS.getBigDecimal("ZC")) != 0) {
							JOptionPane.showMessageDialog(
											null,
											"Cijene zalihe na dokumentima nisu identiène. Prenesite svaki dokument posebno ",
											"Greška",
											javax.swing.JOptionPane.ERROR_MESSAGE);
							return false;
						}
					}
					tmpSDS.setBigDecimal("KOL", tmpSDS.getBigDecimal("KOL")
							.add(StavkeSet.getBigDecimal("KOL")));
					StavkeSet.emptyRow();
				} else {
					tmpSDS.insertRow(true);
					dm.copyColumns(StavkeSet, tmpSDS);
					StavkeSet.emptyRow();
				}
			} else {
				hm.put(String.valueOf(StavkeSet.getInt("CART")), String
						.valueOf(StavkeSet.getInt("CART")));
			}
		}

		for (tmpSDS.first(); tmpSDS.inBounds(); tmpSDS.next()) {
			StavkeSet.insertRow(true);
			dm.copyColumns(tmpSDS, StavkeSet);
		}
		return true;
	}

	// totalni hack - ekspanzija normativa. Tomo, ovo treba srediti na
	// puno bolji nacin (ab.f) my ass
	boolean needNorm, first;
    
    // jos malo hackova - ekspanzija radnih naloga direktno u racune
    boolean directRNL;
    
    // hackomania: flag da se rjesava kalkulirana kolicina iz skladisne:
    // kod prijenosa DOS-a u ROT ili OTP,  
    // ILI
    // kod prijenosa RAC-a u OTP, ako je RAC prethodno nastao iz DOS-a.
    boolean fixDOS;
    
    boolean copyDatum;

	StorageDataSet expanded = null;

	DataRow realrow;

	String[] cc = { "CART", "CART1", "BC", "NAZART", "JM", "KOL" };

	void initNormExpansion() {
		needNorm = (selected.equals("POS") || selected.equals("RAC") || selected.equals("GRN"))
				&& (rIT.what_kind_of_dokument.equals("IZD") || rIT.what_kind_of_dokument
						.equals("OTP"));
		
		if (frmParam.getParam("robno", "rastavIZD", "D", "Rastaviti normativ na izdatnici (D,N)?").equalsIgnoreCase("N"))
		  needNorm = false;
		
        if (directRNL && !bprennormativ) needNorm = true;

		//  hack *HARDCODED*
    realrow = new DataRow(StavkeSet);
    if (needNorm) {
      expanded = new StorageDataSet();
      expanded.setColumns(StavkeSet.cloneColumns());
      expanded.open();
      for (StavkeSet.first(); StavkeSet.inBounds(); StavkeSet.next()) {
        DataSet exp = Aut.getAut().expandArt(StavkeSet, true);
        if (exp == null) addToExpanded(StavkeSet, StavkeSet);
        else 
          for (exp.first(); exp.inBounds(); exp.next())
            addToExpanded(exp, StavkeSet);
      }
      expanded.first();
    }
    first = true;
    StavkeSet.first();
	}
  
  private void addToExpanded(DataSet art, DataSet full) {
    if (!lD.raLocate(expanded, "CART", Integer.toString(art.getInt("CART")))) {
      expanded.last();
      expanded.insertRow(false);
      dM.copyColumns(full, expanded);
      dM.copyColumns(art, expanded, cc);
    } else expanded.setBigDecimal("KOL", 
        expanded.getBigDecimal("KOL").add(art.getBigDecimal("KOL")));
    expanded.post();
  }
  
  void negate(DataSet ds) {
    String[] neg = {"KOL", "UIRAB", "UIZT", "INETO","IPRODBP","POR1","POR2","POR3",
        "IPRODSP","INAB","IMAR","IBP","IPOR","ISP", "IRAZ","UIPOR","KOL1","KOL2","RINAB"};
    
    for (int i = 0; i < neg.length; i++)
      ds.setBigDecimal(neg[i], ds.getBigDecimal(neg[i]).negate());
  }

	boolean getNextRealRow() {
    if (needNorm) {
      if (!expanded.inBounds()) return false;
      dM.copyColumns(expanded, realrow);
      expanded.next();
    } else {
      if (!StavkeSet.inBounds()) return false;
      if (storno) negate(StavkeSet);
      dM.copyColumns(StavkeSet, realrow);
      if (first) {
          first = false;
          return true;
      }
      if (!StavkeSet.next()) return false;
      if (storno) negate(StavkeSet);
      dM.copyColumns(StavkeSet, realrow);
    }
       
		return true;
	}

	String formatKol(ReadRow kolrow) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(3);
		return nf.format(kolrow.getBigDecimal("KOL").doubleValue());
	}

	public boolean checkStavke() {

		boolean returnValue = true;
		//		if (!handleMultiple())
		//			return false;
		if (StavkeSet.getRowCount() == 0) {
			JOptionPane.showMessageDialog(null,
					"Dokument nema stavaka, nema se što prenijeti!", "Greška",
					javax.swing.JOptionPane.ERROR_MESSAGE);
			returnValue = false;
		} else if (TD.isDocSklad(rIT.what_kind_of_dokument)) {
			StavkeSet.enableDataSetEvents(false);
			initNormExpansion(); // ab.f ekspanzija normativa
			String errors = null;
			int errnum = 0;
			final StorageDataSet errorSet = new StorageDataSet();
			errorSet.setColumns(new Column[] {
			   dm.getArtikli().getColumn("CART").cloneColumn(),
			   dm.getArtikli().getColumn("CART1").cloneColumn(),
			   dm.getArtikli().getColumn("BC").cloneColumn(),
			   dm.getArtikli().getColumn("NAZART").cloneColumn(),
			   dM.createStringColumn("OPIS", "Opis", 50),
			   dM.createBigDecimalColumn("KOL", "Kolièina", 3),
			   dM.createBigDecimalColumn("KOLS", "Stanje", 3)
			});
			errorSet.setTableName("errors");
			errorSet.open();

			while (getNextRealRow()) {
				if (!lD.raLocate(dm.getArtikli(), "CART", Integer.toString(realrow.getInt("CART")))) {
          returnValue = false;
          errors = null;
					JOptionPane.showMessageDialog(null, "Artikl '"
							+ realrow.getInt("CART") + "' ne postoji !?! ",
							"Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
					break;
				}
				if (raVart.isStanje(dm.getArtikli())) {
					AST.findStanjeUnconditional(realrow.getString("GOD"), rIT
							.getMasterSet().getString("CSKL"), realrow
							.getInt("CART"));
					AST.gettrenSTANJE().enableDataSetEvents(false);
					if (AST.gettrenSTANJE().rowCount() != 1 ||
					    AST.gettrenSTANJE().getBigDecimal("KOL").compareTo(
							realrow.getBigDecimal("KOL")) < 0) {
						returnValue = false;
						
						errorSet.insertRow(false);
						dM.copyColumns(realrow, errorSet, 
						    new String[] {"CART", "CART1", "BC", "NAZART", "KOL"});
						errorSet.setString("OPIS", 
						    (AST.gettrenSTANJE().rowCount() == 1 ?
						    "Nedovoljna kolièina" : "Nema zapisa") +
						    " na skladištu " + realrow.getString("CSKL"));
						errorSet.setBigDecimal("KOLS",
						    AST.gettrenSTANJE().rowCount() == 1 ?
						        AST.gettrenSTANJE().getBigDecimal("KOL") :
						          Aus.zero3);
						errorSet.post();
						
						if (errors == null)
							errors = "Nedovoljna zaliha za prijenos artikla!";
						
						if (++errnum <= 5)
						errors = errors + "\nArtikl " + realrow.getInt("CART") +
								" (" + realrow.getString("NAZART") +
								(AST.gettrenSTANJE().rowCount() == 1 ?
								 ")\n-  kolièina na skladištu " +
								 realrow.getString("CSKL") + ": " +
								 formatKol(AST.gettrenSTANJE()) :
								 ")\n-  nema zapisa na skladištu " +
								 realrow.getString("CSKL")) +
								"\n-  kolièina za razdužiti: " +
								formatKol(realrow);
					}
				} else if ((selected.equals("POS") || selected.equals("RAC") || selected.equals("GRN")) &&
                    !raVart.isUsluga(dm.getArtikli())) {
				  returnValue = false;
                  
                  errorSet.insertRow(false);
                  dM.copyColumns(realrow, errorSet, 
                      new String[] {"CART", "CART1", "BC", "NAZART", "KOL"});
                  errorSet.setString("OPIS", "Pogrešni artikl na razduženju za skladište " + realrow.getString("CSKL"));
                  errorSet.setBigDecimal("KOLS", Aus.zero3);
                  errorSet.post();
                  
                  if (errors == null)
                      errors = "Greške kod prijenosa!";
                  
                  if (++errnum <= 5)
                  errors = errors + "\nArtikl " + realrow.getInt("CART") +
                          " (" + realrow.getString("NAZART") + ") - nema na stanju";
				}
			}
			if (errors != null) {
			  if (errnum > 5) errors = errors + "\n ( ... još " + 
		          Aus.getNum(errnum - 5, "greška", "greške", "grešaka") + " ... )";
			    if (allowMinus) {
			      int res = JOptionPane.showConfirmDialog(this, 
			          new raMultiLineMessage(
  						errors + "\n\nOtiæi ipak u minus?", 
                          SwingConstants.LEADING), "Greška",
  						JOptionPane.ERROR_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
                  if (res == JOptionPane.OK_OPTION) returnValue = true;
			    } else {
			      int res = JOptionPane.showConfirmDialog(this,  
                      new raMultiLineMessage(errors +
                          "\n\nDetaljni prikaz grešaka?",
                          SwingConstants.LEADING), "Greška",
                          JOptionPane.ERROR_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
			      if (res == JOptionPane.OK_OPTION) {
			        cancelSelect();
			        SwingUtilities.invokeLater(new Runnable() {
                      public void run() {
                        frmTableDataView errs = new frmTableDataView();
                        errs.setTitle("Popis artikala s nedovoljnom zalihom");
                        errs.setSaveName("view-transfer");
                        errs.setDataSet(errorSet);
                        errs.jp.getMpTable().setAutoResizeMode(
                            JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
                        errs.show();
                      }
                    });
			      }
			    }
            }
		}

		return returnValue;
	}

	private boolean setToNull = false; // (ab.f) racun na nulu za servis u

	// garantnom roku

	public boolean addStavke() {
		String keykey = "";
		String keyVeza = "";
		uirac = uirac.valueOf(0, 2);
		StavkeSet.enableDataSetEvents(false);
		short brstavke = 1;
		int brsid = 1;
		QueryDataSet qdsrbr = hr.restart.util.Util.getNewQueryDataSet(
				"select max(rbr) as lrbr from stdoki " + "where cskl='"
						+ rIT.getMasterSet().getString("CSKL")
						+ "' and vrdok='"
						+ rIT.getMasterSet().getString("VRDOK") + "' and god='"
						+ rIT.getMasterSet().getString("GOD") + "' and brdok="
						+ rIT.getMasterSet().getInt("BRDOK"), true);
		if (qdsrbr.getRowCount() != 0) {
			brstavke = (short) (qdsrbr.getShort("lrbr") + 1);
			QueryDataSet qdsbrsid = hr.restart.util.Util.getNewQueryDataSet(
					"select max(rbsid) as lrbsid from stdoki " + "where cskl='"
							+ rIT.getMasterSet().getString("CSKL")
							+ "' and vrdok='"
							+ rIT.getMasterSet().getString("VRDOK")
							+ "' and god='"
							+ rIT.getMasterSet().getString("GOD")
							+ "' and brdok="
							+ rIT.getMasterSet().getInt("BRDOK"), true);
			brsid = qdsbrsid.getInt("lrbsid") + 1;
		}

		if (StavkeSet.getRowCount() == 0)
			return true;
		rIT.getDetailSet().refresh();

		initNormExpansion(); // ab.f ekspanzija normativa

		
		while (getNextRealRow()) { // ab.f
System.out.println(StavkeSet.getInt("CARt"));		    
			if (needNorm) {
				dM.copyColumns(realrow, StavkeSet);
				// TODO: ako je artikl tipa usluge slucajno zalutao ovdje
				// kod razduzenja pos-a, preskoci ga
                if ((selected.equals("POS") || selected.equals("RAC") || selected.equals("GRN")) &&
                    !raVart.isStanje(StavkeSet.getInt("CART")))
                    //Aut.getAut().artTipa(StavkeSet.getInt("CART"), "U")) 
                      continue;
			}

			//for (StavkeSet.first(); StavkeSet.inBounds(); StavkeSet.next()) {
			setToNull = false; // ab.f
			rIT.getDetailSet().insertRow(true);
			copyCommon(brstavke, brsid);
			resetSkladPart();
			rKD.stavka.Init();
			rKD.stanje.Init();

			if (TD.isDocSklad(rIT.what_kind_of_dokument)) {
				if (!recalcStavkaIStanje()) return false;
				resetFinancPart();
			}
			
			// GRN i RAC uvijek rezerviraju kolièinu (ako csklart postoji)
			// ops: OSIM ako se rade iz veæ postojeæe otpremnice, koja je
			// kolièinu veæ razdužila!!!
			if (rIT.isOJ && !rIT.getDetailSet().getString("REZKOL").equals("D") &&
			      !selected.equals("OTP") && !selected.equals("IZD") &&
					rIT.getDetailSet().getString("CSKLART").length() > 0) {
				if (!AST.findStanjeFor(rIT.getDetailSet(), true)) {
					AST.gettrenSTANJE().insertRow(false);
					AST.gettrenSTANJE().setString("GOD", rIT.getDetailSet().getString("GOD"));
					AST.gettrenSTANJE().setString("CSKL", rIT.getDetailSet().getString("CSKLART"));
					AST.gettrenSTANJE().setInt("CART", rIT.getDetailSet().getInt("CART"));
  				rIT.nulaStanje(AST.gettrenSTANJE());
				}
				Aus.add(AST.gettrenSTANJE(), "KOLREZ", rIT.getDetailSet(), "KOL");
				rIT.getDetailSet().setString("REZKOL", "D");
				raTransaction.saveChanges(AST.gettrenSTANJE());
			}
			/*
			if (TD.isDocRezerviraKol(StavkeSet.getString("VRDOK"))
					&& StavkeSet.getString("REZKOL").equalsIgnoreCase("D")) {
				if (!rIT.isUslugaOrTranzit(rIT.getDetailSet().getInt("CART"))) {
					AST.findStanjeUnconditional(rIT.getDetailSet().getString(
							"GOD"), rIT.getDetailSet().getString("CSKLART"),
							rIT.getDetailSet().getInt("CART"));

					AST.gettrenSTANJE().setBigDecimal("KOLREZ",
							AST.gettrenSTANJE().getBigDecimal("KOLREZ")
									.subtract(StavkeSet.getBigDecimal("KOL")));
					
					try {
						raTransaction.saveChanges(AST.gettrenSTANJE());
					} catch (Exception ex) {
						ex.printStackTrace();
						return false;
					}
				}
			}
            
*/			
			copySklad = false;
			if (TD.isDocFinanc(rIT.what_kind_of_dokument))
                getRabatFromPartner();
  
			if (TD.isDocFinanc(rIT.what_kind_of_dokument)
					&& TD.isDocFinanc(docDs.getString("VRDOK"))) {
				copyFinancPart();
			} else if (TD.isDocFinanc(rIT.what_kind_of_dokument)
					&& TD.isDocSklad(docDs.getString("VRDOK"))) {
			  copySklad = copySkladParam;
				calcFinancPart();
			} else if (TD.isDocFinanc(rIT.what_kind_of_dokument)
					&& TD.isDocRNL(docDs.getString("VRDOK"))) {
				setToNull = ZaglavljeSetTmp.getString("GARANC").equalsIgnoreCase("D");
				calcFinancPart();
			} else if (("ROT".equalsIgnoreCase(rIT.what_kind_of_dokument) ||
			          "POD".equalsIgnoreCase(rIT.what_kind_of_dokument) ||
                      "RAC".equalsIgnoreCase(rIT.what_kind_of_dokument)) 
                    && StavkeSet.getString("VRDOK").equalsIgnoreCase("DOS")) {
				calcFinancPart();
			}
			addShemaRabat();
			addShemaZavtr();
			keykey = rCD.getKey(rIT.getDetailSet(), new String[] { "CSKL",
					"VRDOK", "GOD", "BRDOK", "RBSID" }, "stdoki");
			keyVeza = rCD.getKey(StavkeSet, new String[] { "CSKL", "VRDOK",
					"GOD", "BRDOK", "RBSID" }, "stdoki");
			rIT.getDetailSet().setString("ID_STAVKA", keykey);
			if (!rIT.getDetailSet().getString("VRDOK").equals("PON") &&
			    !rIT.getDetailSet().getString("VRDOK").equals(StavkeSet.getString("VRDOK"))) {
    			rIT.getDetailSet().setString("VEZA", keyVeza);
    			if (lD.raLocate(findStavkeSet, new String[] { "CSKL", "VRDOK",
    					"GOD", "BRDOK", "RBSID" }, new String[] {
    					StavkeSet.getString("CSKL"), StavkeSet.getString("VRDOK"),
    					StavkeSet.getString("GOD"),
    					String.valueOf(StavkeSet.getInt("BRDOK")),
    					String.valueOf(StavkeSet.getInt("RBSID")) })) {
    				findStavkeSet.setString("VEZA", keykey);
    				findStavkeSet.setString("STATUS", "P");
    				if (raIzlazTemplate.isNabDirect() &&
    						(StavkeSet.getString("VRDOK").equalsIgnoreCase("RAC") ||
    						 StavkeSet.getString("VRDOK").equalsIgnoreCase("GRN") ||
    						 StavkeSet.getString("VRDOK").equalsIgnoreCase("POS"))
    						&& TD.isDocSklad(rIT.what_kind_of_dokument)) {
    					Aus.add(findStavkeSet, "RINAB", rIT.getDetailSet(), "INAB");
    					if (findStavkeSet.getBigDecimal("KOL").signum() != 0)
    						Aus.div(findStavkeSet, "RNC", "RINAB", "KOL");
    				}
    			}
			}
			
			if (raIzlazTemplate.isNabDirect() &&
					(rIT.getDetailSet().getString("VRDOK").equalsIgnoreCase("RAC") ||
					 rIT.getDetailSet().getString("VRDOK").equalsIgnoreCase("GRN")) &&
					 TD.isDocSklad(StavkeSet.getString("VRDOK"))) {
				Aus.set(rIT.getDetailSet(), "RNC", StavkeSet, "NC");
				Aus.set(rIT.getDetailSet(), "RINAB", StavkeSet, "INAB");
			}
			if (raIzlazTemplate.isNabDirect() &&
                (rIT.getDetailSet().getString("VRDOK").equalsIgnoreCase("ROT") ||
                 rIT.getDetailSet().getString("VRDOK").equalsIgnoreCase("GOT") ||
                 rIT.getDetailSet().getString("VRDOK").equalsIgnoreCase("POD"))) {
              Aus.set(rIT.getDetailSet(), "RNC", "NC");
              Aus.set(rIT.getDetailSet(), "RINAB", "INAB");
            }

			addVTtext(StavkeSet, keykey);
			uirac = uirac.add(rIT.getDetailSet().getBigDecimal("IPRODSP"));
			brstavke++;
			brsid++;
		}
		
		if (!rIT.getMasterSet().getString("VRDOK").equals(ZaglavljeSetTmp.getString("VRDOK")))
		  srediPreneseno();
		rIT.getMasterSet().setBigDecimal("UIRAC",
				rIT.getMasterSet().getBigDecimal("UIRAC").add(uirac));
		return true;
	}

	/**
	 * @param ss
	 *            DataSet
	 * @param keynew
	 *            novi kljuc
	 */

	public void addVTtext(DataSet ss, String keynew) {
		String key = rCD.getKey(ss, new String[] { "CSKL", "VRDOK", "GOD",
				"BRDOK", "RBSID" }, "stdoki");
		QueryDataSet what = hr.restart.util.Util.getNewQueryDataSet(
				"SELECT * from vttext where ckey='" + key + "'", true);
		if (what.getRowCount() != 0) {
			dm.getVTText().open();
			dm.getVTText().insertRow(false);
			dm.getVTText().setString("CKEY", keynew);
			dm.getVTText().setString("TEXTFAK", what.getString("TEXTFAK"));
		}
	}

	public void findRabat(short rbr) {
		RabatiSetTmp.close();
		RabatiSetTmp.closeStatement();
		RabatiSetTmp.setQuery(new QueryDescriptor(dm.getDatabase1(), aSS
				.getQuery4RabatiSet(StavkeSet.getString("CSKL"), StavkeSet
						.getString("GOD"), StavkeSet.getString("VRDOK"),
						StavkeSet.getInt("BRDOK"), rbr)));
		RabatiSetTmp.open();
		RabatiSetTmp.first();
	}

	public void addRabat(DataSet ds, short rbr) {
		if (RabatiSetTmp.getRowCount() != 0) {
			do {
				dm.getVtrabat().insertRow(true);
				if (ds == null) {
					dm.getVtrabat().setString("CSKL",
							rIT.getDetailSet().getString("CSKL"));
					dm.getVtrabat().setString("VRDOK",
							rIT.getDetailSet().getString("VRDOK"));
					dm.getVtrabat().setString("GOD",
							rIT.getDetailSet().getString("GOD"));
					dm.getVtrabat().setInt("BRDOK",
							rIT.getDetailSet().getInt("BRDOK"));
					dm.getVtrabat().setShort("RBR",(short)
							rIT.getDetailSet().getInt("RBSID"));
				} else {
					dm.getVtrabat().setString("CSKL", ds.getString("CSKL"));
					dm.getVtrabat().setString("VRDOK", ds.getString("VRDOK"));
					dm.getVtrabat().setString("GOD", ds.getString("GOD"));
					dm.getVtrabat().setInt("BRDOK", ds.getInt("BRDOK"));
					dm.getVtrabat().setShort("RBR", rbr);
				}
				dm.getVtrabat().setShort("LRBR", RabatiSetTmp.getShort("LRBR"));
				dm.getVtrabat().setString("CRAB",
						RabatiSetTmp.getString("CRAB"));
				dm.getVtrabat().setBigDecimal("PRAB",
						RabatiSetTmp.getBigDecimal("PRAB"));
				dm.getVtrabat().setBigDecimal("IRAB",
						RabatiSetTmp.getBigDecimal("IRAB"));
				dm.getVtrabat().setString("RABNARAB",
						RabatiSetTmp.getString("RABNARAB"));

			} while (RabatiSetTmp.next());
		}
	}

	public void addShemaRabat() {

		findRabat(StavkeSet.getShort("RBR"));
		addRabat(null, (short) 0);
	}

	public void findZavtr(short rbr) {
		ZavtrSetTmp = hr.restart.util.Util.getNewQueryDataSet(aSS
				.getQuery4ZavtrSet(StavkeSet.getString("CSKL"), StavkeSet
						.getString("GOD"), StavkeSet.getString("VRDOK"),
						StavkeSet.getInt("BRDOK"), rbr), true);
		ZavtrSetTmp.first();
	}

	public void addZavtr(DataSet ds, short rbr) {
		if (ZavtrSetTmp.getRowCount() != 0) {
			do {
				dm.getVtzavtr().insertRow(true);
				if (ds == null) {
					dm.getVtzavtr().setString("CSKL",
							rIT.getDetailSet().getString("CSKL"));
					dm.getVtzavtr().setString("VRDOK",
							rIT.getDetailSet().getString("VRDOK"));
					dm.getVtzavtr().setString("GOD",
							rIT.getDetailSet().getString("GOD"));
					dm.getVtzavtr().setInt("BRDOK",
							rIT.getDetailSet().getInt("BRDOK"));
					dm.getVtzavtr().setShort("RBR",
							rIT.getDetailSet().getShort("RBSID"));
				} else {
					dm.getVtzavtr().setString("CSKL", ds.getString("CSKL"));
					dm.getVtzavtr().setString("VRDOK", ds.getString("VRDOK"));
					dm.getVtzavtr().setString("GOD", ds.getString("GOD"));
					dm.getVtzavtr().setInt("BRDOK", ds.getInt("BRDOK"));
					dm.getVtzavtr().setShort("RBR", rbr);
				}
				dm.getVtzavtr().setShort("LRBR", ZavtrSetTmp.getShort("LRBR"));
				dm.getVtzavtr().setString("CZT", ZavtrSetTmp.getString("CZT"));
				dm.getVtzavtr().setBigDecimal("PZT",
						ZavtrSetTmp.getBigDecimal("PZT"));
				dm.getVtzavtr().setBigDecimal("IZT",
						ZavtrSetTmp.getBigDecimal("IZT"));
				dm.getVtzavtr().setBigDecimal("UIPRPOR",
						ZavtrSetTmp.getBigDecimal("UIPRPOR"));
				dm.getVtzavtr().setString("ZTNAZT",
						ZavtrSetTmp.getString("ZTNAZT"));
				dm.getVtzavtr().setString("BROJKONTA",
						ZavtrSetTmp.getString("BROJKONTA"));

			} while (ZavtrSetTmp.next());
		}
	}

	public void addShemaZavtr() {
		findZavtr(StavkeSet.getShort("RBR"));
		addZavtr(null, (short) 0);
	}

	public void srediPreneseno() {

		//System.out.println("rIT.getMasterSet().getTableName()
		// "+rIT.getMasterSet().getTableName());

		String keydest = rPVT.makeKey(rIT.getMasterSet().getTableName(), rIT
				.getMasterSet().getString("CSKL"), rIT.getMasterSet()
				.getString("VRDOK"), rIT.getMasterSet().getString("GOD"), rIT
				.getMasterSet().getInt("BRDOK"));

		String keysrc = "";
		for (ZaglavljeSetTmp.first(); ZaglavljeSetTmp.inBounds(); ZaglavljeSetTmp
				.next()) {
			if (TD.isDocRNL(ZaglavljeSetTmp.getString("VRDOK"))) {
				ZaglavljeSetTmp.setString("STATUS", directRNL ? "F" : "Z");
                if (!directRNL) ZaglavljeSetTmp.setTimestamp("DATUMZ", 
                    rIT.getMasterSet().getTimestamp("DATDOK"));
                else ZaglavljeSetTmp.setTimestamp("DATUMO", 
                    rIT.getMasterSet().getTimestamp("DATDOK"));
                
                ZaglavljeSetTmp.setString("CUSEROBRAC", raUser.getInstance().getUser());
                if (rnlOpis)
                  rIT.getMasterSet().setString("OPIS", ZaglavljeSetTmp.getString("OPIS"));
        rIT.getMasterSet().setString("BRNARIZ", ZaglavljeSetTmp.getString("BRNAR"));
        rIT.getMasterSet().setTimestamp("DATRADNAL",
            ZaglavljeSetTmp.getTimestamp("DATDOK"));
				rIT.getMasterSet().setTimestamp("DVO", directRNL ?
                    rIT.getMasterSet().getTimestamp("DATDOK") :
						ZaglavljeSetTmp.getTimestamp("DATUMO"));
				ZaglavljeSetTmp.setString("CFAKTURE", rIT.getMasterSet()
						.getString("CSKL") + "-"
						+ rIT.getMasterSet().getString("VRDOK") 	+ "-"
						+ rIT.getMasterSet().getString("GOD")+ "-"
						+ String.valueOf(rIT.getMasterSet().getInt("BRDOK")));
			} else {
			  
			  if (!rIT.getMasterSet().getString("VRDOK").equals("PON"))
			  	ZaglavljeSetTmp.setString("STATIRA", "P");
			  
			  if (rIT.getMasterSet().getString("VRDOK").equals("IZD") &&
			  		ZaglavljeSetTmp.getString("VRDOK").equals("POS")) {
			  	for (findStavkeSet.first(); findStavkeSet.inBounds(); findStavkeSet.next())
			  		if (!findStavkeSet.getString("STATUS").equals("P")) {
			  			if (findStavkeSet.getString("CSKLART").equals(rIT.getMasterSet().getString("CSKL")))
			  				findStavkeSet.setString("STATUS", "P");
			  			else ZaglavljeSetTmp.setString("STATIRA", "N");
			  		}
			  }
				
				if ((ZaglavljeSetTmp.getString("VRDOK").equals("OTP") &&
				    "RAC GRN".indexOf(rIT.what_kind_of_dokument) >= 0) ||
				    ("RAC GRN".indexOf(ZaglavljeSetTmp.getString("VRDOK")) >= 0 &&
				        rIT.what_kind_of_dokument.equals("OTP")))
				  rIT.getMasterSet().setString("STATIRA", "P");
			}

			//System.out.println("ZaglavljeSetTmp.getTableName() " +
			// ZaglavljeSetTmp.getTableName());
			if (!rIT.getMasterSet().getString("VRDOK").equals("PON")) {
    			keysrc = rPVT.makeKey(ZaglavljeSetTmp.getTableName(),
    					ZaglavljeSetTmp.getString("CSKL"), ZaglavljeSetTmp
    							.getString("VRDOK"), ZaglavljeSetTmp
    							.getString("GOD"), ZaglavljeSetTmp.getInt("BRDOK"));
                QueryDataSet vtpr = VTprijenos.getDataModule().getTempSet("1=0");
                vtpr.open();
    			rPVT.InsertLink(vtpr, "KEYSRC", keysrc, "KEYDEST",
    					keydest);
			}
		}
	}

	public void cancelSelect() {
      if (isVisible()) dispose();
		//setVisible(false);
	}

	public void setUpClass(raIzlazTemplate rIT) {
		this.rIT = rIT;
	}

	public void show() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2
				- getHeight() / 2);
		super.show();
	}

	public void setDataSet(DataSet ds) {
		this.docDs = ds;
	}
    
    public void setDataSetKey(String[] fields) {
      setDataSetKey(fields, new String[0]);
    }

	public void setDataSetKey(String[] fields, String[] cols) {

		keys = fields;
		if (docDs == null)
			System.err.println("Prvo pozovi metodu setDataSet(DataSet ds)");
		else {
			Column[] cols_left = new Column[keys.length + cols.length];
			Column[] cols_right = new Column[keys.length + cols.length];
			for (int i = 0; i < keys.length; i++) {
				cols_left[i] = (Column) docDs.getColumn(keys[i]).clone();
				cols_right[i] = (Column) docDs.getColumn(keys[i]).clone();
			}
            for (int i = 0; i < cols.length; i++) {
                cols_left[i + keys.length] = (Column) docDs.getColumn(cols[i]).clone();
                cols_right[i + keys.length] = (Column) docDs.getColumn(cols[i]).clone();
            }
			LijeviSet.close();
			DesniSet.close();
			LijeviSet.setColumns(cols_left);
			DesniSet.setColumns(cols_right);
		}
		LijeviSet.open();
		DesniSet.open();
        LijeviSet.setTableName(selected);
        DesniSet.setTableName(selected);
		rTTC.setLeftDataSet(LijeviSet);
		rTTC.setRightDataSet(DesniSet);
		rTTC.initialize();
	}

	public QueryDataSet getLeftSet() {
		return LijeviSet;
	}

	public QueryDataSet getRightSet() {
		return DesniSet;
	}

	public SecondChooser(String title) {

		initStavke();
		setajColone();
		setModal(true);
		setTitle(title);
		rTTC.rnvSave.setVisible(false);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(rTTC, BorderLayout.CENTER);
		this.getContentPane().add(okpanel, BorderLayout.SOUTH);
		okpanel.registerOKPanelKeys(rTTC);
	}

	public void initialise() {

		if (docDs == null)
			System.err.println("Prvo pozovi metodu setDataSet(DataSet ds)");
		else {
		    allowMinus = frmParam.getParam("robno", "SCallowMinus", "N",
		        "Dopustiti odlaženje u minus kod prijenosa dokumenata (D,N)?")
		        .equalsIgnoreCase("D");
		  
			docDs.open();
			LijeviSet.open();
			DesniSet.open();
			docDs.enableDataSetEvents(false);
			LijeviSet.enableDataSetEvents(false);
			DesniSet.enableDataSetEvents(false);
			docDs.first();
			LijeviSet.emptyAllRows();
			DesniSet.emptyAllRows();
			if (docDs.getRowCount() != 0) {
				do {
					LijeviSet.insertRow(true);
					for (int i = 0; i < LijeviSet.getColumnCount(); i++) {
						docDs.getVariant(LijeviSet.getColumn(i).getColumnName(), tmpVarijant);
						LijeviSet.setVariant(LijeviSet.getColumn(i).getColumnName(), tmpVarijant);
					}

				} while (docDs.next());
			}
			docDs.enableDataSetEvents(true);
			LijeviSet.enableDataSetEvents(true);
			DesniSet.enableDataSetEvents(true);
		}
	}
    
    public void simTrans() {
      rTTC.actionLtoR();
      isMultipleDocs = false;
      directRNL = false;
      fixDOS = false;
      bprennormativ = false;
      ZaglavljeSetTmp = doki.getDataModule().getTempSet("1=0");
      ZaglavljeSetTmp.open();
    }

	public void initStavke() {

	  StavkeSet = stdoki.getDataModule().getReadonlySet();
		/*Column[] cols = dm.getStdoki().getColumns();
		for (int i = 0; i < cols.length; i++) {
			StavkeSet.addColumn((Column) cols[i].clone());
		}*/
	}

	public void findStavke() {

		StavkeSet.close();
		StavkeSet.open();
		StavkeSet.empty();
		for (DesniSet.first(); DesniSet.inBounds(); DesniSet.next()) {

			findStavkeSet = hr.restart.util.Util.getNewQueryDataSet(aSS
					.getQuery4rCD4findStavke(DesniSet.getString("god"),
							DesniSet.getString("cskl"), DesniSet
									.getString("vrdok"), DesniSet
									.getInt("brdok")), true);
            findStavkeSet.setSort(new SortDescriptor(new String[] {"RBR"}));

            bpitaozanormativ = false;
			if (findStavkeSet.getRowCount() != 0) {
				for (findStavkeSet.first(); findStavkeSet.inBounds(); findStavkeSet
						.next()) {
					if (selected.equals("RN")) {
						prenosStavkiRadnogNaloga();
					} else {
						if (selected.equals("POS") && rIT.what_kind_of_dokument.equals("IZD") &&
								!rIT.getMasterSet().getString("CSKL").equals(findStavkeSet.getString("CSKLART")))
							continue;
						StavkeSet.insertRow(true);
						hr.restart.baza.dM
								.copyColumns(findStavkeSet, StavkeSet);
					}
				}
			}
		}
	}

	public void prenosStavkiRadnogNaloga() {

	    int cart = findStavkeSet.getInt("CART");
		/*QueryDataSet tmpArt = hr.restart.util.Util.getNewQueryDataSet(
				"SELECT * FROM ARTIKLI WHERE CART = "
						+ findStavkeSet.getInt("CART"), true);*/

		if (raVart.isNorma(cart) && !bpitaozanormativ) {
			bprennormativ = (javax.swing.JOptionPane
					.showConfirmDialog(
							null,
							"Želite li prebaciti normirani artikl (Da) ili njegove dijelove (Ne)?",
							"Prijenos normativa u cjelini",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
			bpitaozanormativ = true;
		}

		if (raVart.isNorma(cart) && !bprennormativ && !directRNL) {
			String upit = "SELECT * from stdoki where vrdok='IZD' "
					+ "and cradnal='" + findStavkeSet.getString("CRADNAL")
					+ "' " + "and rbsrn=" + findStavkeSet.getInt("RBSID");
			QueryDataSet tmpStavke = hr.restart.util.Util.getNewQueryDataSet(
					upit, true);
            tmpStavke.setSort(new SortDescriptor(new String[] {"RBR"}));
			for (tmpStavke.first(); tmpStavke.inBounds(); tmpStavke.next()) {
				StavkeSet.insertRow(true);
				hr.restart.baza.dM.copyColumns(tmpStavke, StavkeSet);
			}
			String normquery = "select * from norme where cartnor=" +
					findStavkeSet.getInt("CART") + " and exists " +
					" (select * FROM artikli where " + 
					raVart.getStanjeCond().not() +
					" and artikli.cart=norme.cart)";
			QueryDataSet tmpUsluga = hr.restart.util.Util.getNewQueryDataSet(
					normquery, true);
			for (tmpUsluga.first(); tmpUsluga.inBounds(); tmpUsluga.next()) {
				StavkeSet.insertRow(true);
				hr.restart.baza.dM.copyColumns(findStavkeSet, StavkeSet);
				StavkeSet.setBigDecimal("KOL", StavkeSet.getBigDecimal("KOL")
						.multiply(tmpUsluga.getBigDecimal("KOL")));
				StavkeSet.setInt("CART", tmpUsluga.getInt("CART"));
				StavkeSet.setString("CART1", tmpUsluga.getString("CART1"));
				StavkeSet.setString("BC", tmpUsluga.getString("BC"));
				StavkeSet.setString("NAZART", tmpUsluga.getString("NAZART"));
				StavkeSet.setBigDecimal("KOL", StavkeSet.getBigDecimal("KOL")
						.multiply(tmpUsluga.getBigDecimal("KOL")));
			}
		} else {
			StavkeSet.insertRow(true);
			hr.restart.baza.dM.copyColumns(findStavkeSet, StavkeSet);
		}
	}

	public void copyCommon(short brstavke, int brsid) {

		rIT.getDetailSet().setString("CSKL",
				rIT.getMasterSet().getString("CSKL"));
		rIT.getDetailSet()
				.setString("GOD", rIT.getMasterSet().getString("GOD"));
		rIT.getDetailSet().setString("VRDOK",
				rIT.getMasterSet().getString("VRDOK"));
		rIT.getDetailSet().setInt("BRDOK", rIT.getMasterSet().getInt("BRDOK"));
		rIT.getDetailSet().setShort("RBR", brstavke);
		rIT.getDetailSet().setInt("RBSID", brsid);
		rIT.getDetailSet().setInt("CART", StavkeSet.getInt("CART"));
		rIT.getDetailSet().setString("CART1", StavkeSet.getString("CART1"));
		rIT.getDetailSet().setString("BC", StavkeSet.getString("BC"));
		rIT.getDetailSet().setString("NAZART", StavkeSet.getString("NAZART"));
		rIT.getDetailSet().setString("JM", StavkeSet.getString("JM"));
		rIT.getDetailSet().setBigDecimal("KOL", StavkeSet.getBigDecimal("KOL"));
		rIT.getDetailSet().setBigDecimal("KOL1",
				StavkeSet.getBigDecimal("KOL1"));
		rIT.getDetailSet().setBigDecimal("KOL2",
				StavkeSet.getBigDecimal("KOL2"));
		/*
		 * System.out.println("what_kind_of_dokument " +
		 * rIT.what_kind_of_dokument + "-" +
		 * TD.isDocFinanc(rIT.what_kind_of_dokument));
		 * 
		 * System.out.println("StavkeSet.getString(\"VRDOK\") " +
		 * StavkeSet.getString("VRDOK") + "-" +
		 * TD.isDocSklad(StavkeSet.getString("VRDOK")));
		 */
		
		
		rIT.getDetailSet().setString("REZKOL", StavkeSet.getString("REZKOL"));

		if (TD.isDocSklad(StavkeSet.getString("VRDOK"))
				&& TD.isDocFinanc(rIT.what_kind_of_dokument)) {
			rIT.getDetailSet().setString("STATUS", "P");
		}
		// HARDKOD zasad
		
		if (rIT.what_kind_of_dokument.equalsIgnoreCase("DOS") &&
		    StavkeSet.getString("VRDOK").equalsIgnoreCase("NKU")) {
		  rIT.getDetailSet().setBigDecimal("KOL2",
              StavkeSet.getBigDecimal("KOL"));
		  if (rIT.getDetailSet().getBigDecimal("KOL1").signum() == 0) {
	          // dod kol = 0
	          lookupData.getlookupData().raLocate(dm.getArtikli(), "CART", 
	              Integer.toString(rIT.getDetailSet().getInt("CART")));
	          if (dm.getArtikli().getBigDecimal("BRJED").signum() > 0)
	            rIT.getDetailSet().setBigDecimal("KOL1", rIT.getDetailSet().getBigDecimal("KOL")
	                .divide(dm.getArtikli().getBigDecimal("BRJED"), 3, BigDecimal.ROUND_HALF_UP));
	        }
		}

		if ((rIT.what_kind_of_dokument.equalsIgnoreCase("RAC") 
            || rIT.what_kind_of_dokument.equalsIgnoreCase("GRN"))
				&& (StavkeSet.getString("VRDOK").equalsIgnoreCase("PON")
                    || StavkeSet.getString("VRDOK").equalsIgnoreCase("RNL"))) {
			rIT.getDetailSet().setString("CSKLART",
					StavkeSet.getString("CSKLART"));
		} else if (TD.isDocOJ(rIT.what_kind_of_dokument)
				&& !TD.isDocOJ(StavkeSet.getString("VRDOK"))) {
			rIT.getDetailSet()
					.setString("CSKLART", StavkeSet.getString("CSKL"));
		} else {
			rIT.getDetailSet().setString("CSKLART",
					StavkeSet.getString("CSKLART"));
		}
	}

	public void copySkladPart() {

		rIT.getDetailSet().setBigDecimal("NC", StavkeSet.getBigDecimal("NC"));
		rIT.getDetailSet().setBigDecimal("INAB",
				StavkeSet.getBigDecimal("INAB"));
		rIT.getDetailSet().setBigDecimal("IMAR",
				StavkeSet.getBigDecimal("IMAR"));
		rIT.getDetailSet().setBigDecimal("VC", StavkeSet.getBigDecimal("VC"));
		rIT.getDetailSet().setBigDecimal("IBP", StavkeSet.getBigDecimal("IBP"));
		rIT.getDetailSet().setBigDecimal("IPOR",
				StavkeSet.getBigDecimal("IPOR"));
		rIT.getDetailSet().setBigDecimal("MC", StavkeSet.getBigDecimal("MC"));
		rIT.getDetailSet().setBigDecimal("ISP", StavkeSet.getBigDecimal("ISP"));
		rIT.getDetailSet().setBigDecimal("ZC", StavkeSet.getBigDecimal("ZC"));
		rIT.getDetailSet().setBigDecimal("IRAZ",
				StavkeSet.getBigDecimal("IRAZ"));
		rIT.getDetailSet().setString("BRPRI", StavkeSet.getString("BRPRI"));
		rIT.getDetailSet().setShort("RBRPRI", StavkeSet.getShort("RBRPRI"));

	}

	public void resetSkladPart() {

		java.math.BigDecimal Nula = java.math.BigDecimal.valueOf((long) 0);
		rIT.getDetailSet().setBigDecimal("NC", Nula);
		rIT.getDetailSet().setBigDecimal("INAB", Nula);
		rIT.getDetailSet().setBigDecimal("IMAR", Nula);
		rIT.getDetailSet().setBigDecimal("VC", Nula);
		rIT.getDetailSet().setBigDecimal("IBP", Nula);
		rIT.getDetailSet().setBigDecimal("IPOR", Nula);
		rIT.getDetailSet().setBigDecimal("MC", Nula);
		rIT.getDetailSet().setBigDecimal("ISP", Nula);
		rIT.getDetailSet().setBigDecimal("ZC", Nula);
		rIT.getDetailSet().setBigDecimal("IRAZ", Nula);
		rIT.getDetailSet().setString("BRPRI", "");
		rIT.getDetailSet().setShort("RBRPRI", (short) 0);

	}

    private void getRabatFromPartner() {
      if (lookupData.getlookupData().raLocate(dm.getPartneri(), "CPAR",
            Integer.toString(rIT.getMasterSet().getInt("CPAR"))))
          rIT.getDetailSet().setBigDecimal("UPRAB", dm.getPartneri().getBigDecimal("PRAB"));
    }
    
	public void copyFinancPart() {
	    dM.copyColumns(StavkeSet, rIT.getDetailSet(), 
	        new String[] {"UPRAB", "UIRAB", "UPZT", "UIZT", "FC",
	            "INETO", "FVC", "IPRODBP", "POR1", "POR2", "POR3",
	            "PPOR1", "PPOR2", "PPOR3", "UIPOR", "UPPOR", "FMC",
	            "IPRODSP", "FMCPRP"});
	}

	public void resetFinancPart() {
		java.math.BigDecimal Nula = java.math.BigDecimal.valueOf((long) 0);
		rIT.getDetailSet().setBigDecimal("UPRAB", Nula);
		rIT.getDetailSet().setBigDecimal("UIRAB", Nula);
		rIT.getDetailSet().setBigDecimal("UPZT", Nula);
		rIT.getDetailSet().setBigDecimal("UIZT", Nula);
		rIT.getDetailSet().setBigDecimal("FC", Nula);
		rIT.getDetailSet().setBigDecimal("INETO", Nula);
		rIT.getDetailSet().setBigDecimal("FVC", Nula);
		rIT.getDetailSet().setBigDecimal("IPRODBP", Nula);
		rIT.getDetailSet().setBigDecimal("POR1", Nula);
		rIT.getDetailSet().setBigDecimal("POR2", Nula);
		rIT.getDetailSet().setBigDecimal("POR3", Nula);
		rIT.getDetailSet().setBigDecimal("PPOR1", Nula);
		rIT.getDetailSet().setBigDecimal("PPOR2", Nula);
		rIT.getDetailSet().setBigDecimal("PPOR3", Nula);
		rIT.getDetailSet().setBigDecimal("FMC", Nula);
		rIT.getDetailSet().setBigDecimal("IPRODSP", Nula);
	}

	public void calcFinancPart() {

        boolean found = false;
                
        tmpCijenik = AST.getCijenik(rIT.what_kind_of_dokument,
            rIT.getMasterSet().getString("CSKL"),
            rIT.getMasterSet().getInt("CPAR"),
            rIT.getDetailSet().getInt("CART"));

        if (tmpCijenik != null) {
          rIT.getDetailSet().setBigDecimal("FC",
                tmpCijenik.getBigDecimal("VC"));
          rIT.getDetailSet().setBigDecimal("FVC",
                tmpCijenik.getBigDecimal("VC"));
          rIT.getDetailSet().setBigDecimal("FMC",
                tmpCijenik.getBigDecimal("MC"));
          found = true;
        }
        
        if (copySklad) {
          Aus.set(rIT.getDetailSet(), "FC", StavkeSet, "VC");
          Aus.set(rIT.getDetailSet(), "FVC", StavkeSet, "VC");
          Aus.set(rIT.getDetailSet(), "FMC", StavkeSet, "MC");
          found = true;
        }
        
        if (!found && rIT.getDetailSet().getString("CSKLART").length() > 0) {
           AST.findStanjeUnconditional(rIT.getDetailSet().getString("GOD"),
                    rIT.getDetailSet().getString("CSKLART"), rIT.getDetailSet()
                            .getInt("CART"));
            if (AST.gettrenSTANJE().getRowCount() != 0) {
                rIT.getDetailSet().setBigDecimal("FC",
                        AST.gettrenSTANJE().getBigDecimal("VC"));
                rIT.getDetailSet().setBigDecimal("FVC",
                        AST.gettrenSTANJE().getBigDecimal("VC"));
                rIT.getDetailSet().setBigDecimal("FMC",
                        AST.gettrenSTANJE().getBigDecimal("MC"));
                found = true;
           }
        }
        
        if (!found && TypeDoc.getTypeDoc().isDocSklad(rIT.what_kind_of_dokument)) {
          AST.findStanjeUnconditional(rIT.getDetailSet().getString("GOD"), rIT
                .getDetailSet().getString("CSKL"), rIT.getDetailSet().getInt("CART"));
          if (AST.gettrenSTANJE().getRowCount() != 0) {
  
              rIT.getDetailSet().setBigDecimal("FC",
                      AST.gettrenSTANJE().getBigDecimal("VC"));
              rIT.getDetailSet().setBigDecimal("FVC",
                      AST.gettrenSTANJE().getBigDecimal("VC"));
              rIT.getDetailSet().setBigDecimal("FMC",
                      AST.gettrenSTANJE().getBigDecimal("MC"));
              found = true;
          }
        }

		if (!found && lD.raLocate(dm.getArtikli(), "CART",
				Integer.toString(rIT.getDetailSet().getInt("CART")))) {
			rIT.getDetailSet().setBigDecimal("FC",
					dm.getArtikli().getBigDecimal("VC"));
			rIT.getDetailSet().setBigDecimal("FVC",
					dm.getArtikli().getBigDecimal("VC"));
			rIT.getDetailSet().setBigDecimal("FMC",
					dm.getArtikli().getBigDecimal("MC"));
		}

/*		String corg = rIT.pressel.getCORG();
		if (hr.restart.util.lookupData.getlookupData().raLocate(dm.getSklad(),
				new String[] { "CORG" }, new String[] { corg })) {

			AST.findStanjeUnconditional(rIT.getDetailSet().getString("GOD"), dm
					.getSklad().getString("CSKL"), rIT.getDetailSet().getInt(
					"CART"));
			if (AST.gettrenSTANJE().getRowCount() != 0) {

				rIT.getDetailSet().setBigDecimal("FC",
						AST.gettrenSTANJE().getBigDecimal("VC"));
				rIT.getDetailSet().setBigDecimal("FVC",
						AST.gettrenSTANJE().getBigDecimal("VC"));
				rIT.getDetailSet().setBigDecimal("FMC",
						AST.gettrenSTANJE().getBigDecimal("MC"));
			}
		}*/
		////////

		// ab.f
		if (setToNull) {
			setToNull = false;
			java.math.BigDecimal nula = new java.math.BigDecimal(0);
			rIT.getDetailSet().setBigDecimal("FC", nula);
			rIT.getDetailSet().setBigDecimal("FVC", nula);
			rIT.getDetailSet().setBigDecimal("FMC", nula);
		}

		lc.TransferFromDB2Class(rIT.getDetailSet(), rKD.stavka);
		ALP.findCPORART(rIT.getDetailSet().getInt("CART"));
		rKD.stavka.ppor1 = ALP.gettrenPOREZART().getBigDecimal("POR1");
		rKD.stavka.ppor2 = ALP.gettrenPOREZART().getBigDecimal("POR2");
		rKD.stavka.ppor3 = ALP.gettrenPOREZART().getBigDecimal("POR3");
		rKD.stavka.uppor = ALP.gettrenPOREZART().getBigDecimal("UKUPOR");
		tmpKupArt = AST.getKupArtAll(rIT.getMasterSet().getInt("CPAR"), true);
		if (tmpKupArt.getRowCount() != 0) {
			if (lD.raLocate(tmpKupArt, new String[] { "CART" },
					new String[] { String.valueOf(rIT.getDetailSet().getInt(
							"CART")) })) {
				rKD.stavka.uprab = tmpKupArt.getBigDecimal("PRAB");
			}
		}
        
        String rcskl = rIT.getMasterSet().getString("CSKL");
        if (TypeDoc.getTypeDoc().isDocOJ(rIT.what_kind_of_dokument))
          rcskl = rIT.getDetailSet().getString("CSKLART");
        
		rKD.KalkulacijaStavke(rIT.getMasterSet().getString("VRDOK"), 
            "KOL", 'N', rcskl, false);
		lc.TransferFromClass2DB(rIT.getDetailSet(), rKD.stavka);
	}

	public BigDecimal getRealRezKol(int cart,String cskl,String god){
		String rezkolstr="SELECT sum(kol) as kol FROM stdoki, doki WHERE doki.brdok=stdoki.brdok and "+
		"doki.vrdok=stdoki.vrdok and doki.god=stdoki.god and doki.cskl=stdoki.cskl and "+
		"stdoki.CART = "+cart+
        " AND ((stdoki.CSKL='"+cskl+"' AND (stdoki.CSKLART IS NULL OR stdoki.CSKLART='')) OR stdoki.CSKLART='"+
		cskl+"') AND stdoki.GOD='"+god+"' AND stdoki.vrdok='PON' AND stdoki.rezkol='D' AND "+
		"(stdoki.VEZA='' or stdoki.VEZA iS NULL) and doki.statira='N'";
		System.out.println("Pero qqqqqqqqqqqqqqqqqqqqqqqqqq :"+rezkolstr);
		return  hr.restart.util.Util.getNewQueryDataSet(rezkolstr, true).getBigDecimal("KOL");
		
	}
	
	public boolean recalcStavkaIStanje() {

	  if (!raVart.isStanje(rIT.getDetailSet().getInt("CART"))) return true;
		lc.TransferFromDB2Class(rIT.getDetailSet(), rKD.stavka);
		//		lc.setBDField("rezkol", hr.restart.sisfun.frmParam.getParam("robno",
		//				"rezkol"), rKD.stavka);
		AST.findStanjeUnconditional(rIT.getDetailSet().getString("GOD"), rIT
				.getDetailSet().getString("CSKL"), rIT.getDetailSet().getInt("CART"));
		
		if (AST.gettrenSTANJE().getRowCount() != 0) {
			lc.TransferFromDB2Class(AST.gettrenSTANJE(), rKD.stanje);
			rKD.setWhat_kind_of_document(rIT.what_kind_of_dokument);
			rKD.setVrzal(rIT.getDetailSet().getString("CSKL"));
			rKD.kalkSkladPart();
			rKD.KalkulacijaStanje(rIT.what_kind_of_dokument);
			System.out.println("vracam rezervu ");
			System.out.println(rIT.getDetailSet());
			rKD.VratiRezervu(selected);
			lc.TransferFromClass2DB(AST.gettrenSTANJE(), rKD.stanje);
			lc.TransferFromClass2DB(rIT.getDetailSet(), rKD.stavka);
			rCD.unosIzlaz(rIT.getDetailSet(), AST.gettrenSTANJE()); //???????
                        
			/*if (("ROT".equalsIgnoreCase(rIT.what_kind_of_dokument)
					|| "OTP".equalsIgnoreCase(rIT.what_kind_of_dokument))
					&& StavkeSet.getString("VRDOK").equalsIgnoreCase("DOS")) {*/
            if (fixDOS || ("OTP".equalsIgnoreCase(rIT.what_kind_of_dokument) && 
                StavkeSet.getString("VRDOK").equalsIgnoreCase("RAC") &&
                StavkeSet.getString("VEZA").indexOf("-DOS-") >= 0)) {
              
				System.out.println("(ROT ILI OTP) i DOS");
				AST.gettrenSTANJE().setBigDecimal(
						"KOLSKLADIZ",
						AST.gettrenSTANJE().getBigDecimal("KOLSKLADIZ")
								.subtract(StavkeSet.getBigDecimal("KOL")));
				AST.gettrenSTANJE().setBigDecimal(
						"KOLSKLAD",
						AST.gettrenSTANJE().getBigDecimal("KOLSKLADUL")
								.subtract(
										AST.gettrenSTANJE().getBigDecimal(
												"KOLSKLADIZ")));

			}
			try {
				raTransaction.saveChanges(AST.gettrenSTANJE());
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}

//			AST.gettrenSTANJE().saveChanges();
		} else {
			System.err.println("Prazno" + rIT.getDetailSet().getString("GOD")
					+ " " + rIT.getDetailSet().getString("CSKL") + " "
					+ rIT.getDetailSet().getInt("CART"));
		}
		return true;
	}

	public boolean saveChangeInSecondChooser() {
//		raLocalTransaction rLT = new raLocalTransaction() {
//			public boolean transaction() {
		
		 
		
		
				try {
					raTransaction.saveChanges(rIT.getMasterSet());
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}
/*				
				try {
					raTransaction.saveChanges(dm.getSeq());
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}
*/				
				try {
					raTransaction.saveChanges(ZaglavljeSetTmp);
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}

				try {
					raTransaction.saveChanges(findStavkeSet);
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}

				try {
					raTransaction.saveChanges(rIT.getDetailSet());
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}

				try {
					raTransaction.saveChanges(dm.getVtzavtr());
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}

				try {
					raTransaction.saveChanges(dm.getVtrabat());
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}
				try {
					raTransaction.saveChanges(dm.getVTText());
				} catch (Exception ex) {
					ex.printStackTrace();
					return false;

				}
				return true;
//			}
//		};

	}

	public void copyDS2DS(DataSet source, DataSet dest) {

		HashMap hm = new HashMap();
		for (int i = 0; i < source.getColumns().length; i++) {
			hm.put(source.getColumn(i).getColumnName(), source.getColumn(i)
					.getColumnName());
		}

		ArrayList al = new ArrayList();
		for (int i = 0; i < dest.getColumns().length; i++) {
			if (hm.containsKey(dest.getColumn(i).getColumnName())) {
				al.add(dest.getColumn(i).getColumnName());
			}
		}
		String[] polja_za_kopiranje = new String[al.size()];

		for (int i = 0; i < polja_za_kopiranje.length; i++) {
			polja_za_kopiranje[i] = (String) al.get(i);
		}
		dM.copyColumns(source, dest, polja_za_kopiranje);
		if (source.hasColumn("CUSER") != null) {
			System.out.println("USER "
					+ hr.restart.sisfun.raUser.getInstance().getUser());
			source.setString("CUSER", hr.restart.sisfun.raUser.getInstance()
					.getUser());
		}

		hm = null;
		al = null;
		polja_za_kopiranje = null;
	}
	public void rijesirezervaciju() {
		String sqlstanje ="SELECT * FROM STANJE WHERE EXISTS ("+ 
		"SELECT * FROM STDOKI WHERE CSKL='"+
		rIT.getMasterSet().getString("CSKL")+"' AND "+
		"VRDOK='"+rIT.getMasterSet().getString("VRDOK")+"' AND "+
		"GOD='"+rIT.getMasterSet().getString("GOD")+"' AND "+
		"BRDOK="+rIT.getMasterSet().getInt("BRDOK")+" "+
		"AND STANJE.CSKL=STDOKI.CSKLART AND STANJE.GOD=STDOKI.GOD "+
		"AND STANJE.CART=STDOKI.CART)"; 
System.out.println(sqlstanje);								
		
		QueryDataSet majstanje = hr.restart.util.Util.getNewQueryDataSet(
				sqlstanje);
		
		for (majstanje.first();majstanje.inBounds();majstanje.next()){
			majstanje.setBigDecimal("KOLREZ",getRealRezKol(majstanje.getInt("CART"),
					majstanje.getString("CSKL"),majstanje.getString("GOD")));
		}
		raTransaction.saveChanges(majstanje);
	}
}