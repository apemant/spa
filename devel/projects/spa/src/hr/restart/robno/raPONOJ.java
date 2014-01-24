/****license*****************************************************************
**   file: raPONOJ.java
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

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.VTText;
import hr.restart.baza.doki;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;
import hr.restart.swing.raTableUIModifier;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raTransaction;

public class raPONOJ extends raIzlazTemplate {
  
    raNavAction rnvZak = new raNavAction("Zakljuèavanje",
      raImages.IMGSENDMAIL, java.awt.event.KeyEvent.VK_F7, java.awt.event.KeyEvent.SHIFT_MASK) {
      public void actionPerformed(ActionEvent e) {
        keyZak();
      }
    };
    
    raNavAction rnvCopy = new raNavAction("Kopiranje",
        raImages.IMGCOPYCURR, java.awt.event.KeyEvent.VK_F2, java.awt.event.KeyEvent.SHIFT_MASK) {
        public void actionPerformed(ActionEvent e) {
          keyCopy();
        }
      };
    
      boolean copyHack = false;
      DataSet pon = null;
    
      void keyCopy() {
      if (getMasterSet().rowCount() == 0) return;
      
      if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Kopirati ponudu " + getMasterSet().getString("PNBZ2") + "?", "Kopiranje", 
          JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
      
      pon = doki.getDataModule().openTempSet(Condition.whereAllEqual(util.mkey, getMasterSet()));
      try {
        copyHack = true;
        raMaster.rnvAdd_action();
      } finally {
        copyHack = false;
      }

    }
    
    void keyZak() {
      DataSet ms = getMasterSet();
      if (ms.rowCount() == 0) return;
            
      if (ms.getString("STATKNJ").equalsIgnoreCase("P")) {
        if (!raUser.getInstance().isSuper()) {
          JOptionPane.showMessageDialog(raMaster.getWindow(), "Ponuda je veæ zakljuèana!", 
              "Zakljuèavanje", JOptionPane.INFORMATION_MESSAGE);
          return;
        }
        if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Ponuda je zakljuèana. Želite li je otkljuèati?", "Otkljuèavanje", 
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        ms.setString("STATKNJ", "N");
        ms.saveChanges();
        raMaster.getJpTableView().fireTableDataChanged();
        return;
      }
      
      if (!raUser.getInstance().isSuper() && !ms.getString("CUSER").equals(raUser.getInstance().getUser())) {
        JOptionPane.showMessageDialog(raMaster.getWindow(), "Samo vlastite ponude se mogu zakljuèati!", 
            "Zakljuèavanje", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      
      if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Želite li zakljuèati ponudu?", "Zakljuèivanje", 
          JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
      
      ms.setString("STATKNJ", "P");
      ms.saveChanges();
      raMaster.getJpTableView().fireTableDataChanged();
    }
    
    public void afterSetModeMaster(char oldMod,char newMod) {
      super.afterSetModeMaster(oldMod, newMod);
      if (copyHack && newMod == 'N') {
        copyHack = false;
        doOnFocusNovi(new Runnable() {
          public void run() {
            copyStavke(pon, true);
          }
        });
      }
    }
    
    public void afterCopyStavke() {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          //raMaster.getJpTableView().fireTableDataChanged();
          raMaster.getOKpanel().jPrekid_actionPerformed();
        }
      });
    }
    
    public boolean dodSaveCopyStavke() {
      try {
        DataSet vt = VTText.getDataModule().openTempSet(Condition.equal("CKEY", rCD.getKey(pon)));
        if (vt.rowCount() > 0) {
          QueryDataSet copy = VTText.getDataModule().openEmptySet();
          copy.insertRow(false);
          copy.setString("CKEY", rCD.getKey(getMasterSet()));
          copy.setString("TEXTFAK", vt.getString("TEXTFAK"));
          raTransaction.saveChanges(copy);
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }
    return true;
    }
    
	public void initialiser() {
		what_kind_of_dokument = "PON";
		bPonudaZaKupca = false;
	}

	public void MyaddIspisMaster() {

		raMaster.getRepRunner().addReport("hr.restart.robno.repPonuda",
				"hr.restart.robno.repIzlazni", "Ponuda", "Ponuda 1 red");
		raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaV",
				"hr.restart.robno.repIzlazni", "Ponuda",
				"Ponuda 1 red u valuti");
		raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaValSingle",
            "hr.restart.robno.repIzlazni", "PonudaValSingle", 
            "Ponuda s valutom bez kolièina");
		raMaster.getRepRunner().addReport("hr.restart.robno.repPonuda2",
				"hr.restart.robno.repIzlazni", "Ponuda2", "Ponuda 2 red");
		raMaster.getRepRunner().addReport("hr.restart.robno.repPonuda2V",
				"hr.restart.robno.repIzlazni", "Ponuda2",
				"Ponuda 2 red u valuti");
		raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaNop",
            "hr.restart.robno.repIzlazni", "PonudaNop", "Ponuda bez cijena stavki");
		raMaster.getRepRunner().addReport("hr.restart.robno.repPonudaGroup",
		    "hr.restart.robno.repGroupIzlazni", "PonudaGroup", "Ponuda po grupama artikala");
		raMaster.getRepRunner().addReport("hr.restart.robno.repOffer",
            "hr.restart.robno.repIzlazni","ProformaInvoice3","Offer");
		raMaster.getRepRunner().addReport("hr.restart.robno.repMxPON",
				"Matri\u010Dni ispis ponude");
		raMaster.getRepRunner().addReport("hr.restart.robno.repMxPONPop",
			"Matri\u010Dni ispis ponude s više popusta");
	}

	public void MyaddIspisDetail() {

		raDetail.getRepRunner().addReport("hr.restart.robno.repPonuda",
				"hr.restart.robno.repIzlazni", "Ponuda", "Ponuda 1 red");
		raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaV",
				"hr.restart.robno.repIzlazni", "Ponuda",
				"Ponuda 1 red u valuti");
		raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaValSingle",
            "hr.restart.robno.repIzlazni", "PonudaValSingle", 
            "Ponuda s valutom bez kolièina");
		raDetail.getRepRunner().addReport("hr.restart.robno.repPonuda2",
				"hr.restart.robno.repIzlazni", "Ponuda2", "Ponuda 2 red");
		raDetail.getRepRunner().addReport("hr.restart.robno.repPonuda2V",
				"hr.restart.robno.repIzlazni", "Ponuda2",
				"Ponuda 2 red u valuti");
		raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaNop",
            "hr.restart.robno.repIzlazni", "PonudaNop", "Ponuda bez cijena stavki");
		raDetail.getRepRunner().addReport("hr.restart.robno.repPonudaGroup",
            "hr.restart.robno.repGroupIzlazni", "PonudaGroup", "Ponuda po grupama artikala");
		raDetail.getRepRunner().addReport("hr.restart.robno.repOffer",
            "hr.restart.robno.repIzlazni","ProformaInvoice3","Offer");
		raDetail.getRepRunner().addReport("hr.restart.robno.repMxPON",
				"Matri\u010Dni ispis ponude");
		raDetail.getRepRunner().addReport("hr.restart.robno.repMxPONPop",
		"Matri\u010Dni ispis ponude s više popusta");

	}



	public void ConfigViewOnTable() {
		this.setVisibleColsMaster(new int[] { 4, 5, 6, 44, 34 }); 
// Requested
// by Mladen
// (Siniša)
		this.setVisibleColsDetail(new int[] { 4,
						Aut.getAut().getCARTdependable(5, 6, 7), 8, 11, 16, 12,
						19, 24 });
	}

	public raPONOJ() {
		isOJ=true;
//      isMaloprodajnaKalkulacija = true;
		setPreSel((jpPreselectDoc) presPONOJ.getPres());
		addButtons(true, true);
//		raMaster.addOption(rnvDellAll, 3);
		raDetail.addOption(rnvDellAllStav, 3);
		raDetail.addOption(rnvKartica, 5, false);
		master_titel = "Ponude";
		detail_titel_mno = "Stavke ponude";
		detail_titel_jed = "Stavka ponude";
		setMasterSet(dm.getZagPonOJ());
		setDetailSet(dm.getStPonKup());
		setMasterDeleteMode(DELDETAIL);
		rCD.setisNeeded(false);
		MP.BindComp();
		DP.BindComp();
		if (frmParam.getParam("robno", "racSklad", "D", 
          "Dodati skladište na RAC/PON (D,N)").equals("D")) {
          DP.rpcart.addSkladField(hr.restart.robno.Util.getSkladFromCorg());
        }
		DP.resizeDP();
		raDetail.addOption(rnvCopyPon, 6, false);
		
		raMaster.addOption(rnvCopy, 4, false);
		
		if (frmParam.getParam("robno", "ponZak", "N", "Dodati opciju za zakljuèavanje ponuda (D,N)").equalsIgnoreCase("D")) {
		
		  raMaster.addOption(rnvZak, 6, false);
		  raMaster.getJpTableView().addTableModifier(new ColorModifier());
		}

		this.setVisibleColsMaster(new int[] { 4, 5, 9 });
	}

	boolean gotovin = true;

	public boolean ValidacijaStanje() {
		return true;
	}
	
	public boolean checkStavkeDel() {
      return false;
    }

	public boolean DodatnaValidacijaDetail() {
		if (val.isEmpty(DP.jtfKOL))
			return false;
		//      if (val.isEmpty(DP.jraFMC)) return false;
		if (val.isEmpty(DP.jraFC))
			return false;
		if (manjeNula())
			return false;
		
		if (!isUslugaOrTranzit() && getDetailSet().getString("CSKLART").equalsIgnoreCase("") &&
		    getDetailSet().getString("REZKOL").equalsIgnoreCase("D")) {
		  DP.jtfKOL.requestFocus();
		  JOptionPane.showMessageDialog(raDetail.getWindow(), 
		        "Nije moguæe rezervirati kolièinu ako nije uneseno " +
		        "skladište!", "Rezervacija",
		  		JOptionPane.ERROR_MESSAGE);
		  return false;
	    }
		
		return isPriceToBig(true);
	}

	public boolean ValidacijaMasterExtend() {
		getMasterSet().setString("PARAM", "OJ");
		return true;
	}

	//  public void SetFocusIzmjenaExtends() {
	//     MP.panelBasic.rpku.jraCkupac.requestFocus();
	//  }
	/*
	 * public void SetFocusNoviExtends() {
	 * MP.panelBasic.jtfDATDOK.requestFocus(); }
	 */
	public boolean LocalValidacijaMaster() {
		return true;
	}

	public void RestPanelSetup() {
		DP.addRest();
		DP.instalRezervaciju();
	}
	
	public void SetFokusDetail(char mode) {
		super.SetFokusDetail(mode);
		if (mode=='N'){
			DP.setRezervacija();
//			if (hr.restart.sisfun.frmParam.getParam("robno", "rezkol",
//					"Rezerviranje kolièine D/N", "D").equalsIgnoreCase("D")) {
//				getDetailSet().setString("REZKOL", "D");
//				DP.jrtbRezervacija.setSelected(true);
//			} else {
//				getDetailSet().setString("REZKOL", "N");
//				DP.jrtbRezervacija.setSelected(false);
//			}			
		}
	}

	public void brisiRezervaciju() {
		if (!rezkoldel.equalsIgnoreCase("D")) return;		
		AST.findStanjeUnconditional(god4del,csklart4del,cart4del);
		boolean bSnimanje = !(AST.gettrenSTANJE() == null
				|| AST.gettrenSTANJE().getRowCount() == 0);
		if (bSnimanje) { 
			AST.gettrenSTANJE().setBigDecimal("KOLREZ", 
			AST.gettrenSTANJE().getBigDecimal("KOLREZ").
			subtract(rKD.stavkaold.kol));
			raTransaction.saveChanges(AST.gettrenSTANJE());
		}
	}
	
	public void cskl2csklart(){}
	

	public void dodajRezervaciju() {

		if (getDetailSet().getString("CSKLART").equalsIgnoreCase("")) return;
		if (!isUslugaOrTranzit()) {
			AST.findStanjeUnconditional(
					getDetailSet().getString("GOD"),
					getDetailSet().getString("CSKLART"),
					getDetailSet().getInt("CART"));
			boolean nemaGa = AST.gettrenSTANJE() == null
			|| AST.gettrenSTANJE().getRowCount() == 0; 
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
	}	
	
	public class ColorModifier extends raTableModifier {
	    Color colorS = Color.green.darker().darker();

	    public boolean doModify() {
	      Variant v = new Variant();
          ((JraTable2)getTable()).getDataSet().getVariant("STATKNJ",getRow(),v);
          return v.getString().equals("P");
	    }
	    public void modify() {
	      
	      JComponent jRenderComp = (JComponent)renderComponent;
	          jRenderComp.setForeground(colorS);

	    }
	}
}