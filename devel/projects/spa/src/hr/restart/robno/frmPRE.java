/****license*****************************************************************
**   file: frmPRE.java
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

import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raTransaction;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class frmPRE extends frmUlazTemplate {

  boolean isforsave = false;
  jpMasterPanelPRE jpMaster;
  hr.restart.robno.jpUlazDetail jpDetail = new hr.restart.robno.jpUlazDetail(this,'P'){
    public void MYmetToDo_after_lookUp(){
      super.MYmetToDo_after_lookUp();
      MMYmetToDo_after_lookUp();
    }
  };
//  hr.restart.robno.frmVTZtr zt = new hr.restart.robno.frmVTZtr(this);  // (ab.f)
  private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  private creatorPRD creatorPRD = new creatorPRD();
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private lookupData ld = lookupData.getlookupData();
  private QueryDataSet stavkaRadnogNalogaZaBrisanje = null;
  private String id_stavka=null;
  private String veza=null;
  private DataSet stavkern;
  {
    dm.getDokuPRE().open();
    dm.getStdokuPRE().open();	
    dm.getVTPred().open();
  }
  public frmPRE() {
    jpMaster = new jpMasterPanelPRE(this);
    vrDok="PRE";
    masterTitle="Predatnice";
    detailTitle="Stavke predatnice";
    jpp=presPRE.getPres();
    setJPanelMaster(jpMaster);
    setJPanelDetail(jpDetail);
    setMasterSet(dm.getDokuPRE());
    setDetailSet(dm.getStdokuPRE());
    jpMaster.setDataSet(getMasterSet());
    jpDetail.setDataSet(getDetailSet(), getMasterSet());
    setVisibleColsMaster(new int[] {4,5,7,29});

    raDetail.getJpTableView().getNavBar().removeStandardOption(0);
    raDetail.addOption(rnvKartica, 3, false);
    raMaster.getRepRunner().addReport("hr.restart.robno.repPredatnica","Predatnica",2);
    raDetail.getRepRunner().addReport("hr.restart.robno.repPredatnica","Predatnica",2);

  }
  public void SetFokusMaster(char mode) {
    jpMaster.jpRN.init(getMasterSet().getString("CRADNAL"));
    super.SetFokusMaster(mode);
    if (mode=='N') {
      rcc.setLabelLaF(jpMaster.jrfCORG,true);
      rcc.setLabelLaF(jpMaster.jrfNAZORG,true);
      rcc.setLabelLaF(jpMaster.jbCORG,true);
      rcc.EnabDisabAll(jpMaster.jpRN,true);
      getMasterSet().setTimestamp("DATDOK", jpp.getSelRow().getTimestamp("DATDOK-to"));
      getMasterSet().setTimestamp("DVO", jpp.getSelRow().getTimestamp("DATDOK-to"));
      getMasterSet().setBigDecimal("RANDMAN", Aus.one0);
      jpp.copySelValues();
      jpMaster.jrfCORG.requestFocus();
    }
    else if (mode == 'I') {
      rcc.setLabelLaF(jpMaster.jrfCORG,false);
      rcc.setLabelLaF(jpMaster.jrfNAZORG,false);
      rcc.setLabelLaF(jpMaster.jbCORG,false);
      rcc.EnabDisabAll(jpMaster.jpRN,false);
      jpMaster.jrfCORG.requestFocus();
    }
    jpMaster.initPanel(mode);

  }
  public void SetFokusDetail(char mode) {
    super.SetFokusDetail(mode);
    if (mode=='N') {
//      getDetailSet().setBigDecimal("PZT", rdUtil.getUtil().getPrimka_ZT(jpMaster.getCSHZT()));
      getDetailSet().setBigDecimal("PZT", getMasterSet().getBigDecimal("UPZT")); // (ab.f)
      jpDetail.rpcart.setCART();
      rcc.EnabDisabAll(jpDetail.jpDetailCenter,false);
    }
    jpDetail.findVirtualFields(mode);
  }
  public void EntryPointDetail(char mode) {
    super.EntryPointDetail(mode);
    jpDetail.disableDefFields();
    if (mode == 'I') jpDetail.rpcart.EnabDisab(false);
  }
  public void MMYmetToDo_after_lookUp(){

    if (raDetail.getMode()=='N'){
      rcc.setLabelLaF(jpDetail.jtfKOL,true);
      rcc.setLabelLaF(jpDetail.jtfNC,true);
    }
    else if (raDetail.getMode()=='I'){
      rcc.setLabelLaF(jpDetail.jtfKOL,false);
      rcc.setLabelLaF(jpDetail.jtfNC,false);

      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          jpDetail.jtfVC.requestFocus();
        }
      });
    }
  }

  public boolean ValidacijaMaster(char mode) {
    isforsave = false;
    if (vl.isEmpty(jpMaster.jrfCORG))
      return false;
    if (!jpMaster.jpRN.Validacija()) return false;
    getMasterSet().setString("CRADNAL",jpMaster.jpRN.getCRADNAL());
    if (!jpMaster.jpRN.copyRNL()) {
      javax.swing.JOptionPane.showMessageDialog(null,
           "Nema stavaka za prenos radnog naloga !",
           "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!isforsave) {
      javax.swing.JOptionPane.showMessageDialog(null,
           "Radni nalog nije prenesen !",
           "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return super.ValidacijaMaster(mode);
  }

  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jtfKOL))
      return false;
    if (vl.isEmpty(jpDetail.jtfVC))
      return false;
    if (vl.isEmpty(jpDetail.jtfMC))
      return false;
//    super.saveDobArt();
    if (!dlgSerBrojevi.getdlgSerBrojevi().findSB(jpDetail.rpcart, getDetailSet(), 'U', mode)) {
      return false;
    }
    return super.ValidacijaDetail(mode);
  }

  public void AfterSaveDetail(char mode) {
    super.AfterSaveDetail(mode);
  }

  public boolean ValDPEscapeDetail(char mode) {
    if (mode=='N') {
      if (jpDetail.rpcart.getCART().trim().equals("")) {
        return true;
      }
      else {
        getDetailSet().setBigDecimal("DC", main.nul);
        getDetailSet().setBigDecimal("PRAB", main.nul);
        getDetailSet().setBigDecimal("PZT", main.nul);
        getDetailSet().setBigDecimal("PMAR", main.nul);
        getDetailSet().setBigDecimal("VC", main.nul);
        getDetailSet().setBigDecimal("MC", main.nul);
        getDetailSet().setBigDecimal("KOL", main.nul);
        jpDetail.kalkulacija(1);
        jpDetail.disableUnosFields(true, 'P');
        jpDetail.rpcart.setCART();
        jpDetail.findSTANJE(' ');
        return false;
      }
    }
    else {
      return true;
    }
  }

	public boolean DeleteCheckDetail() {
		// ako je status rn = 'Z' ne može se brisati
		id_stavka=getDetailSet().getString("ID_STAVKA");
		veza = getDetailSet().getString("VEZA");
		
		if (hr.restart.util.Util.getNewQueryDataSet("select status from rn where cradnal='"+getMasterSet().getString("CRADNAL")+"'",true).getString("STATUS").equalsIgnoreCase("Z")){
			JOptionPane
					.showConfirmDialog(
							null,
							"Brisanje nije dozvoljeno. Radni nalog po kojoj je nastala predatnica je zatvoren !",
							"Gre\u0161ka", JOptionPane.DEFAULT_OPTION,
							JOptionPane.ERROR_MESSAGE);
			id_stavka = null;
			veza=null;
			return false;
		}
		return super.DeleteCheckDetail();
	}
  
  public boolean doBeforeSaveMaster(char mode) {
		if (mode == 'N') {
			util.getBrojDokumenta(getMasterSet());
		}
		return true;
	}
  
	public boolean doWithSaveDetail(char mode) {
		if (mode=='B'){
			if (id_stavka != null && id_stavka.length() > 0 && ld.raLocate(dm.getVTPred(),"ID_STAVKA",id_stavka)){
				dm.getVTPred().deleteRow();
				raTransaction.saveChanges(dm.getVTPred());
			}
			if (veza != null && veza.length() > 0) {
    			stavkaRadnogNalogaZaBrisanje = hr.restart.util.Util.getNewQueryDataSet(
    					"SELECT * FROM STDOKI WHERE ID_STAVKA='"+veza+"'",true);
    			
    			if (stavkaRadnogNalogaZaBrisanje != null ||
    					stavkaRadnogNalogaZaBrisanje.getRowCount()!=0){
    			    stavkaRadnogNalogaZaBrisanje.setString("STATUS","N");
    				raTransaction.saveChanges(stavkaRadnogNalogaZaBrisanje);	
    			}
			}
			id_stavka = null;
			veza=null;
		}
		return true;
	}

	public boolean doBeforeSaveDetail(char mode) {

		
		if (mode=='B'){
			
		}
		return true;
	}
	
	
	
	
  public boolean doWithSaveMaster(char mode) {

    if (mode=='N' && isforsave) {
      try {

        if (creatorPRD.creatPRD(stavkern,getMasterSet().getString("CSKL"),
                        getMasterSet().getTimestamp("DATDOK"),jpMaster.jpRN.getCRADNAL(),getMasterSet().getBigDecimal("RANDMAN"),
                        getMasterSet().getInt("BRDOK"),getDetailSet())){
          raTransaction.saveChanges(getDetailSet());
          raTransaction.saveChanges(creatorPRD.getStanje());
          raTransaction.saveChanges(dm.getVTPred());
//          ST.prn(creatorPRD.getStavkeRN());
          raTransaction.saveChanges(creatorPRD.getStavkeRN());
        }
        return true;
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }
    } else if (mode == 'B') {
		util.delSeq(srcString, true);
	}

    
    return true;
  }

  public void deleteDefZT() {}   /// mora biti poradi neispravnog brisanja zaglavlja

  public void afterCancel(){
    isforsave = false;
  }

  public void afterOK(){
    getDetailSet().open();
    stavkern = null;
    stavkern = jpMaster.jpRN.getStavkeRNL();
    if (stavkern.getRowCount()==0) {
      isforsave = false;
      return;
    }
    else {
      isforsave=true;
    }

  }

  public void AfterAfterSaveMaster(char mode) {

    if (mode=='N' && !jpMaster.jpRN.getCRADNAL().equals("")) {
      raMaster.setLockedMode('I');
      jBStavke_actionPerformed(null);
    }
    else {
      super.AfterAfterSaveMaster(mode);
    }
  }

  public void saveDodZT() {}

}