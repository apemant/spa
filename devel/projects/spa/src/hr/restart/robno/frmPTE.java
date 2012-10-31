/****license*****************************************************************
**   file: frmPTE.java
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


/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class frmPTE extends frmUlazTemplate {
  hr.restart.robno.jpUlazMaster jpMaster = new hr.restart.robno.jpUlazMaster(this);
  hr.restart.robno.jpUlazDetail jpDetail = new hr.restart.robno.jpUlazDetail(this);

  {
    dm.getDokuPTE().open();
    dm.getStdokuPTE().open();
  }
  public frmPTE() {
    prSTAT='D';
    vrDok="PTE";
    masterTitle="Povratnice - tereæenja";
    detailTitle="Stavke povratnice - tereæenja";
    jpp=presPTE.getPres();
    setJPanelMaster(jpMaster);
    setJPanelDetail(jpDetail);
    setMasterSet(dm.getDokuPTE());
    setDetailSet(dm.getStdokuPTE());
    jpMaster.setDataSet(getMasterSet());
    jpDetail.setDataSet(getDetailSet(), getMasterSet());
    //jpMaster.jtabs.setEnabledAt(1, false);
    raDetail.addOption(rnvKartica, 4, false);
    
    raMaster.getRepRunner().addReport("hr.restart.robno.repPovratnicaTerecenje", "hr.restart.robno.repPovratnicaTerecenje", "PovratnicaTerecenje", "Povratnica - tereæenje");
//    raMaster.getRepRunner().addReport("hr.restart.robno.repPovratnicaTerecenjeKalkulacija", "hr.restart.robno.repPovratnicaTerecenje", "PovTerKalk", "Povratnica - tereæenje kalkulacija");

    raDetail.getRepRunner().addReport("hr.restart.robno.repPovratnicaTerecenje", "hr.restart.robno.repPovratnicaTerecenje", "PovratnicaTerecenje", "Povratnica - tereæenje");
//    raDetail.getRepRunner().addReport("hr.restart.robno.repPovratnicaTerecenjeKalkulacija", "hr.restart.robno.repPovratnicaTerecenje", "PovTerKalk", "Povratnica - tereæenje kalkulacija");
    
//    raMaster.getRepRunner().addReport("hr.restart.robno.repPovratnicaTerecenje","Povratnica
// - tereæenje",2);
//    raDetail.getRepRunner().addReport("hr.restart.robno.repPovratnicaTerecenje","Povratnica - tereæenje",2);
//    raMaster.getRepRunner().addReport("hr.restart.robno.repPovratnicaTerecenje","hr.restart.robno.repPovratnicaTerecenje","PovratnicaTerecenje","Povratnica - tereæenje");
//    raDetail.getRepRunner().addReport("hr.restart.robno.repPovratnicaTerecenje","hr.restart.robno.repPovratnicaTerecenje","PovratnicaTerecenje","Povratnica - tereæenje");
    jpMaster.jpGetVal.setRaDataSet(getMasterSet());
  }
  public void SetFokusMaster(char mode) {
  	super.SetFokusMaster(mode);
    if (mode == 'I' && getDetailSet().getRowCount() > 0)
      jpMaster.jpGetVal.setValutaEditable(false);
    else jpMaster.jpGetVal.setValutaEditable(true);
    super.SetFokusMaster(mode);
    if (mode=='N') {
      getMasterSet().setTimestamp("DATDOK", presPTE.getPres().getSelRow().getTimestamp("DATDOK-to"));
      getMasterSet().setTimestamp("DVO", presPTE.getPres().getSelRow().getTimestamp("DATDOK-to"));
      presPTE.getPres().copySelValues();
    }
    jpMaster.initPanel(mode);
  }
  public void SetFokusDetail(char mode) {
    super.SetFokusDetail(mode);
    jpDetail.findVirtualFields(mode);
    if (mode == 'I') jpDetail.rpcart.EnabDisab(false);
    if (mode=='N') jpDetail.rpcart.setCART();
  }
  public void EntryPointDetail(char mode) {
    super.EntryPointDetail(mode);
    jpDetail.disableDefFields();
  }
  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jrfCPAR))
      return false;
    return super.ValidacijaMaster(mode);
  }
  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jtfKOL))
      return false;
    if (vl.isEmpty(jpDetail.jtfDC))
      return false;
    if (vl.isEmpty(jpDetail.jtfVC))
      return false;
    if (vl.isEmpty(jpDetail.jtfMC))
      return false;
    if (!dlgSerBrojevi.getdlgSerBrojevi().findSB(jpDetail.rpcart, getDetailSet(), 'U', mode)) {
      return false;
    }
    dm.getStdokuPTE().setBigDecimal("KOL", dm.getStdokuPTE().getBigDecimal("KOL").negate());
    jpDetail.kalkulacija(0);
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
        getDetailSet().setBigDecimal("MAR", main.nul);
        jpDetail.tds.setBigDecimal("POR", main.nul);
        jpDetail.kalkulacija(1);
        jpDetail.disableUnosFields(true, 'P');
        jpDetail.rpcart.setCART();
        jpDetail.findSTANJE(' ');
        jpDetail.rpcart.SetDefFocus();
        return false;
      }
    }
    else {
      return true;
    }
  }
}