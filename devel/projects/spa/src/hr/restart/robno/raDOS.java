/****license*****************************************************************
**   file: raDOS.java
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

import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.db.raPreparedStatement;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raTransaction;

import java.awt.Color;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jb.util.TriStateProperty;

public class raDOS extends raIzlazTemplate  {
    
      BigDecimal kolizmjena = Aus.zero2;
      BigDecimal koldel = Aus.zero2;
      boolean isDeleteUsluga = false;

	  public void zamraciMaster(DataSet ds){
      }
	  public void zamraciDetail(DataSet ds){

		ds.getColumn("CRADNAL").setVisible(TriStateProperty.FALSE);
		ds.getColumn("UPRAB").setVisible(TriStateProperty.FALSE);
		ds.getColumn("UIRAB").setVisible(TriStateProperty.FALSE);
		ds.getColumn("UPZT").setVisible(TriStateProperty.FALSE);
		ds.getColumn("UIZT").setVisible(TriStateProperty.FALSE);
		ds.getColumn("FC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("INETO").setVisible(TriStateProperty.FALSE);
		ds.getColumn("FVC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IPRODBP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("POR1").setVisible(TriStateProperty.FALSE);
		ds.getColumn("POR2").setVisible(TriStateProperty.FALSE);
		ds.getColumn("POR3").setVisible(TriStateProperty.FALSE);
		ds.getColumn("FMC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IPRODSP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("NC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("INAB").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IMAR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("VC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IBP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IPOR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("MC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("ZC").setVisible(TriStateProperty.FALSE);
		ds.getColumn("ISP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("IRAZ").setVisible(TriStateProperty.FALSE);
		ds.getColumn("BRPRI").setVisible(TriStateProperty.FALSE);
		ds.getColumn("RBRPRI").setVisible(TriStateProperty.FALSE);
		ds.getColumn("PPOR1").setVisible(TriStateProperty.FALSE);
		ds.getColumn("PPOR2").setVisible(TriStateProperty.FALSE);
		ds.getColumn("PPOR3").setVisible(TriStateProperty.FALSE);
		ds.getColumn("CARTNOR").setVisible(TriStateProperty.FALSE);
		ds.getColumn("FMCPRP").setVisible(TriStateProperty.FALSE);
		ds.getColumn("REZKOL").setVisible(TriStateProperty.FALSE);
		ds.getColumn("VEZA").setVisible(TriStateProperty.FALSE);
		ds.getColumn("ID_STAVKA").setVisible(TriStateProperty.FALSE);

	}
	  
	public void initialiser(){
	    what_kind_of_dokument = "DOS";
        
	}

	public void MyaddIspisMaster(){
	    raMaster.getRepRunner().addReport("hr.restart.robno.repDOS","hr.restart.robno.repOTP","DOS","Dostavnica");
	    raMaster.getRepRunner().addReport("hr.restart.robno.repDOS2","hr.restart.robno.repOTP","DOS2","Dostavnica s dvije jedinice mjere");
	    raMaster.getRepRunner().addReport("hr.restart.robno.repDOS3","hr.restart.robno.repOTP","DOS3","Dostavnica naruèeno / isporuèeno");
	    raMaster.getRepRunner().addReport("hr.restart.robno.repDOS4","hr.restart.robno.repOTP","DOS4","Dostavnica s dvije jedinice mjere naruèeno / isporuèeno");
        raMaster.getRepRunner().addReport("hr.restart.robno.repDOS6","hr.restart.robno.repOTPss","DOS6","Dostavnica sa šifrom kupca, dvije jedinice mjere naruèeno / isporuèeno");
        raMaster.getRepRunner().addReport("hr.restart.robno.repDOS7","hr.restart.robno.repOTPcp","DOS7","Dostavnica s dvije jedinice mjere, cijenom i popustom");
        raMaster.getRepRunner().addReport("hr.restart.robno.repDOS8","hr.restart.robno.repOTPss","DOS8","Dostavnica sa šifrom kupca, dvije jedinice mjere i cijenom");
        if (hr.restart.sisfun.frmParam.getParam("robno","IspisGetroROTs","N","Stavke ispisa sadržavaju i ispis za Getro",true).equals("D")){
          raMaster.getRepRunner().addReport("hr.restart.robno.repDOSGetro","hr.restart.robno.repRacuniPnP","DosGetro","Dostavnica za Getro");
        }
/*	    raMaster.getRepRunner().addReport("hr.restart.robno.repOTPvri","hr.restart.robno.repOTPvri","OTPvri","Otpremnica vrijednosna");
	    raMaster.getRepRunner().addReport("hr.restart.robno.repMxOTP","Matri\u010Dni ispis otpremnice");
*/	    
	  }
	  public void MyaddIspisDetail(){
	    raDetail.getRepRunner().addReport("hr.restart.robno.repDOS","hr.restart.robno.repOTP","DOS","Dostavnica");
	    raDetail.getRepRunner().addReport("hr.restart.robno.repDOS2","hr.restart.robno.repOTP","DOS2","Dostavnica dvije jedinice mjere");
	    raDetail.getRepRunner().addReport("hr.restart.robno.repDOS3","hr.restart.robno.repOTP","DOS3","Dostavnica naruèeno / isporuèeno");
	    raDetail.getRepRunner().addReport("hr.restart.robno.repDOS4","hr.restart.robno.repOTP","DOS4","Dostavnica dvije jedinice mjere naruèeno / isporuèeno");
        raDetail.getRepRunner().addReport("hr.restart.robno.repDOS6","hr.restart.robno.repOTPss","DOS6","Dostavnica sa šifrom kupca, dvije jedinice mjere naruèeno / isporuèeno");
        raDetail.getRepRunner().addReport("hr.restart.robno.repDOS7","hr.restart.robno.repOTPcp","DOS7","Dostavnica s dvije jedinice mjere, cijenom i popustom");
        raDetail.getRepRunner().addReport("hr.restart.robno.repDOS8","hr.restart.robno.repOTPss","DOS8","Dostavnica sa šifrom kupca, dvije jedinice mjere i cijenom");
        if (hr.restart.sisfun.frmParam.getParam("robno","IspisGetroROTs","N","Stavke ispisa sadržavaju i ispis za Getro",true).equals("D")){
          raDetail.getRepRunner().addReport("hr.restart.robno.repDOSGetro","hr.restart.robno.repRacuniPnP","DosGetro","Dostavnica za Getro");
        }
/*	    raDetail.getRepRunner().addReport("hr.restart.robno.repOTPvri","hr.restart.robno.repOTPvri","OTPvri","Otpremnica vrijednosna");
	    raDetail.getRepRunner().addReport("hr.restart.robno.repMxOTP","Matri\u010Dni ispis otpremnice");
*/	    
	  }
		public void RestPanelSetup() {
//			DP.addRestOnlyKol();
			DP.addRestOnlyKol2();
		}
	  
	  public void ConfigViewOnTable(){
	    this.setVisibleColsMaster(new int[] {4,5,6,31,32,34}); // Requested by Mladen (Siniša)
	    setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,9,10});	    
	  }
	  
	  public boolean checkAccess() {
	    if (!super.checkAccess()) return false;
	    if (getMasterSet().getString("STATIRA").equals("P")) {
          setUserCheckMsg(
                  "Korisnik ne može promijeniti dokument jer je prenesen u drugi dokument !",
                  false);
          return false;
        }
	    return true;
	  }

	  
	  public raDOS() {
	    setPreSel((jpPreselectDoc) presDOS.getPres());
	    master_titel = "Dostavnice";
	    detail_titel_mno = "Stavke dostavnice";
	    detail_titel_jed = "Stavka dostavnice";
	    zamraciMaster(dm.getZagDos());
	    zamraciDetail(dm.getStOtp());
	    setMasterSet(dm.getZagDos());
	    setDetailSet(dm.getStOtp());
	    MP.BindComp();
	    DP.BindComp();
	    this.raMaster.getJpTableView().addTableModifier(new DosColorModifier());
	    ConfigViewOnTable();
	  }
	  
	  public void SetFokusMaster(char mode) {
	    if (mode=='N') {
	      MP.panelBasic.jrbPartner.setSelected(true);
	      MP.panelBasic.partnerSelected(true);
	      MP.panelBasic.jrbKupac.setSelected(false);
	      MP.panelBasic.kupacSelected(false);
	      pressel.copySelValues();
	      SetFocusNovi();
	    } else if (mode=='I') {
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
	    } else if (mode=='B') {
	      if (getMasterSet().getInt("CPAR") != 0) {
	        MP.panelBasic.jrbPartner.setSelected(true);
	      } else {//if (getMasterSet().getInt("CKUPAC") != 0) {
	        MP.panelBasic.jrbKupac.setSelected(true);
	      }
	      MP.panelBasic.enableAll(false);
	    }
	    findBRDOK();
	  }
	  
	  public boolean LocalValidacijaMaster(){
	    if (MP.panelBasic.jrbPartner.isSelected()) {
	      if (val.isEmpty(MP.panelBasic.jrfCPAR))  return false;
	      return true;
	    }
//	    if (MP.panelBasic.jrbKupac.isSelected()) {
//	      if (val.isEmpty(MP.panelBasic.rpku.jraPrezime))  return false;
//	      return true;
//	    }
	    return true;
	  }

	  public void SetFokusDetail(char mode) {
	      
	      kolizmjena = Aus.zero2;
	      if (mode =='I'){
	          kolizmjena = getDetailSet().getBigDecimal("KOL");
	      
	      }
//System.out.println("kolizmjena ="+kolizmjena);	      
	      super.SetFokusDetail(mode);
	  }
	  
		public boolean doWithSaveDetail(char mode) {

			if (isDeleteUsluga) {
			    isDeleteUsluga = false;
			    val.recountDataSet(this.raDetail,"RBR",delRbr,false);
                
			    return true;
			}
		    if (isUslugaOrTranzit() && mode !='B') return true;
		    if (mode == 'N') {
				getDetailSet().setString("ID_STAVKA",
						raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
								"vrdok", "god", "brdok", "rbsid" }, "stdoki"));
				raTransaction.saveChanges(getDetailSet());
			}
			if (mode=='I' || mode =='N') {
			    extraStanje(mode);
		        AST.gettrenSTANJE().setBigDecimal("KOLSKLADIZ",
		                AST.gettrenSTANJE().getBigDecimal("KOLSKLADIZ").add(getDetailSet().getBigDecimal("KOL")));
		        AST.gettrenSTANJE().setBigDecimal("KOLSKLADIZ",
		                AST.gettrenSTANJE().getBigDecimal("KOLSKLADIZ").subtract(kolizmjena));
		        AST.gettrenSTANJE().setBigDecimal("KOLSKLAD",
		                AST.gettrenSTANJE().getBigDecimal("KOLSKLADUL").subtract(AST.gettrenSTANJE().getBigDecimal("KOLSKLADIZ")));
		        
			}
			if (mode=='B'){
			    val.recountDataSet(this.raDetail,"RBR",delRbr,false);
			}
            raTransaction.saveChanges(getDetailSet());
            raTransaction.saveChanges(AST.gettrenSTANJE()); //FIXME moguc problem sa stanjem...
			
			return true;
		}	  	
		
		
		
		public boolean doBeforeSaveDetail(char mode) {

		    if (mode == 'B') {
//System.out.println("1");		        
				if (!isDeleteUsluga) {
//System.out.println("2---kolsklad="+AST.gettrenSTANJE().getBigDecimal("KOLSKLAD"));				    
		        AST.gettrenSTANJE().setBigDecimal("KOLSKLADIZ",
		                AST.gettrenSTANJE().getBigDecimal("KOLSKLADIZ").subtract(koldel));
		        AST.gettrenSTANJE().setBigDecimal("KOLSKLAD",
		                AST.gettrenSTANJE().getBigDecimal("KOLSKLADUL").subtract(AST.gettrenSTANJE().getBigDecimal("KOLSKLADIZ")));
//System.out.println("3---kolsklad="+AST.gettrenSTANJE().getBigDecimal("KOLSKLAD"));
				}
				koldel = Aus.zero2;
			}
			return true;
		}

		public boolean DeleteCheckDetail() {
	        extraStanje('B');
	        isDeleteUsluga = isUslugaOrTranzit();
	        koldel = getDetailSet().getBigDecimal("KOL");
	        delRbr = getDetailSet().getShort("RBR");
	        return true;
		    
		}
		
	  
	  public void extraStanje(char mode){
//	  	if (mode !='N') return;
		if (!isUslugaOrTranzit()) {
			AST.findStanjeUnconditional(
					getDetailSet().getString("GOD"),
					getDetailSet().getString("CSKL"),
					getDetailSet().getInt("CART"));
ST.prn(AST.gettrenSTANJE());			
			boolean nemaGa = AST.gettrenSTANJE() == null
			|| AST.gettrenSTANJE().getRowCount() == 0; 
			if (nemaGa) {
//System.out.println("nemaGa");
				AST.gettrenSTANJE().insertRow(false);
				AST.gettrenSTANJE().setString("GOD",
						getMasterSet().getString("GOD"));
				AST.gettrenSTANJE().setString("CSKL",
						getDetailSet().getString("CSKL"));
				AST.gettrenSTANJE().setInt("CART",
						getDetailSet().getInt("CART"));
				nulaStanje(AST.gettrenSTANJE());
				raTransaction.saveChanges(AST.gettrenSTANJE());
			}
		}
	  
	  }
	  public boolean ValidacijaStanje() {
	  	return true;
	  }
	  
	  public boolean ValidacijaLimit(java.math.BigDecimal oldvalue,
          java.math.BigDecimal newvalue) {
      //if (checkLimit) {
          lD.raLocate(dm.getPartneri(), new String[] { "CPAR" },
                  new String[] { String
                          .valueOf(getMasterSet().getInt("CPAR")) });
          if (dm.getPartneri().getString("STATUS").equalsIgnoreCase("C")) {
            javax.swing.JOptionPane.showMessageDialog(null, "Partneru je zabranjeno fakturiranje!", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
          }

          java.math.BigDecimal limit = dm.getPartneri().getBigDecimal(
                  "LIMKRED");
          if (limit.doubleValue() != 0 && dm.getPartneri().getString("STATUS").equalsIgnoreCase("B")) {
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
      //}
      return true;
	 }
	  
	  private BigDecimal calculateSaldo(java.math.BigDecimal saldo,
          java.math.BigDecimal oldvalue, java.math.BigDecimal newvalue) {

      saldo = saldo.subtract(oldvalue);
      saldo = saldo.add(newvalue);
      return saldo;
	  }

	  public boolean DodatnaValidacijaDetail() {
        if (getDetailSet().getBigDecimal("KOL1").signum() == 0) {
          // dod kol = 0
          lookupData.getlookupData().raLocate(dm.getArtikli(), "CART", 
              Integer.toString(getDetailSet().getInt("CART")));
          if (dm.getArtikli().getBigDecimal("BRJED").signum() > 0)
            getDetailSet().setBigDecimal("KOL1", getDetailSet().getBigDecimal("KOL")
                .divide(dm.getArtikli().getBigDecimal("BRJED"), 3, BigDecimal.ROUND_HALF_UP));
        }
        if (getDetailSet().getString("STATUS").equals("P")) {
          JOptionPane.showMessageDialog(raDetail.getWindow(),
                  "Stavka je veæ prenešena u drugi dokument !",
                  "Greška", javax.swing.JOptionPane.ERROR_MESSAGE);
          return false;
        }
	    return true;
	  }

	  public void RestPanelMPSetup(){
//	    MP.setupTwo();
	  }
	  public boolean ValidacijaMasterExtend(){
	    MP.panelBasic.rpku.updateRecords();
	    return true;
	  }

  class DosColorModifier extends raTableModifier {
    Variant v = new Variant();
    Variant d = new Variant();
    boolean provjeraDetaila = false;

    StorageDataSet detkeys = new StorageDataSet();

    String[] pk;

    public DosColorModifier() {
      provjeraDetaila = hr.restart.sisfun.frmParam.getParam("robno","bojaNoDetail","N","Obilježavanje praznog detaila - USPORAVA").equalsIgnoreCase("N");
      if (!provjeraDetaila) {
        pk = doki.getDataModule().pkey;
        for (int i = 0; i < pk.length; i++) {
          detkeys.addColumn(doki.getDataModule().getColumn(pk[i]).cloneColumn());
        }
        detkeys.addColumn(stdoki.getDataModule().getColumn("RBR").cloneColumn());
        detkeys.open();
      }
    }

    public boolean doModify() {
      if (getTable() instanceof JraTable2) {
        JraTable2 tab = (JraTable2) getTable();
        if (tab.getDataSet().getRowCount() > 0 && tab.getDataSet().hasColumn("STATIRA") != null) {
          tab.getDataSet().getVariant("STATIRA", this.getRow(), v);

          // FIXME optimizacija koda...

          if (!provjeraDetaila) {
            detkeys.emptyAllRows();
            for (int i = 0; i < pk.length; i++) {
              tab.getDataSet().getVariant(pk[i], this.getRow(), d);
              detkeys.setVariant(pk[i], d);
            }
            detkeys.setShort("RBR", (short) 1);
            detkeys.post();
          }
          return (v.getString().equals("N") || v.getString().equals("P"));
        }
      }
      return false;
    }

    private raPreparedStatement counter = new raPreparedStatement("stdoki", raPreparedStatement.COUNT);

    public void modify() {
      Color colorS = hr.restart.swing.raColors.yellow;// Color.green.darker().darker();
      Color colorN = hr.restart.swing.raColors.red;// Color.red;
      JComponent jRenderComp = (JComponent) renderComponent;
      counter.setKeys(detkeys);

      boolean ima;
      if (!provjeraDetaila) {
        ima = counter.isExist();
      } else {
        ima = true;
      }

      if (isSelected()) {
        if (v.getString().equals("P")) {
          jRenderComp.setBackground(colorS);
          jRenderComp.setForeground(Color.black);
        } else {
          jRenderComp.setBackground(getTable().getSelectionBackground());
          jRenderComp.setForeground(getTable().getSelectionForeground());
        }
        if (!ima) {
          jRenderComp.setBackground(colorN);
          jRenderComp.setForeground(Color.black);
        }
      } else {
        if (v.getString().equals("P")) {
          jRenderComp.setForeground(Color.yellow.darker().darker());
        } else {
          jRenderComp.setForeground(getTable().getForeground());
        }
        if (!ima) {
          jRenderComp.setForeground(Color.red);
        }
      }
    }
  }


}