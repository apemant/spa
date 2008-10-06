/****license*****************************************************************
**   file: frmPST.java
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

public class frmPST extends frmUlazTemplate {
  hr.restart.robno.jpUlazMasterSimple jpMaster = new hr.restart.robno.jpUlazMasterSimple();
  hr.restart.robno.jpUlazDetail jpDetail = new hr.restart.robno.jpUlazDetail(this);

  {
    dm.getDokuPST().open();
    dm.getStdokuPST().open();
  }
  public frmPST() {
    prSTAT='S';
    vrDok="PST";
    masterTitle="Poèetno stanje";
    detailTitle="Stavke poèetnog stanja";
    jpp=presPST.getPres();
    setJPanelMaster(jpMaster);
    setJPanelDetail(jpDetail);
    setMasterSet(dm.getDokuPST());
    setDetailSet(dm.getStdokuPST());
    jpMaster.setDataSet(getMasterSet());
    jpDetail.setDataSet(getDetailSet(), getMasterSet());
    raDetail.addOption(rnvKartica, 4, false);
    raDetail.getRepRunner().addReport("hr.restart.robno.repPocetnoStanje","Poèetno stanje - kolièine",2);
    raDetail.getRepRunner().addReport("hr.restart.robno.repPocetnoStanjeExtendedVersion","Poèetno stanje - vrijednosti",2);
    raDetail.getRepRunner().addReport("hr.restart.robno.repPocetnoStanjeMegablastVersion","Poèetno stanje - kalkulacije",2);
    raMaster.getRepRunner().addReport("hr.restart.robno.repPocetnoStanje","Poèetno stanje - kolièine",2);
    raMaster.getRepRunner().addReport("hr.restart.robno.repPocetnoStanjeExtendedVersion","Poèetno stanje - vrijednosti",2);
    raMaster.getRepRunner().addReport("hr.restart.robno.repPocetnoStanjeMegablastVersion","Poèetno stanje - kalkulacije",2);
  }
  public void SetFokusMaster(char mode) {
  	
    if (mode=='N') {
      getMasterSet().setTimestamp("DATDOK", presPST.getPres().getSelRow().getTimestamp("DATDOK-to"));
      presPST.getPres().copySelValues();
      jpMaster.jrfCSKL.forceFocLost();
    }
    jpMaster.jtfDATDOK.requestFocus();
    jpMaster.jtfDATDOK.selectAll();
    super.SetFokusMaster(mode);
  }
  public void SetFokusDetail(char mode) {
    super.SetFokusDetail(mode);
    jpDetail.findVirtualFields(mode);
    if (mode=='N') jpDetail.rpcart.setCART();

  }
  public void EntryPointDetail(char mode) {
    super.EntryPointDetail(mode);
    jpDetail.disableDefFields();
    if (mode == 'I') jpDetail.rpcart.EnabDisab(false);
  }
  public boolean ValidacijaMaster(char mode) {
    return (super.ValidacijaMaster(mode));
  }
  public boolean ValidacijaDetail(char mode) {
    if (vl.isEmpty(jpDetail.jtfKOL))
      return false;
    if (vl.isEmpty(jpDetail.jtfNC))
      return false;
    if (vl.isEmpty(jpDetail.jtfVC))
      return false;
    if (vl.isEmpty(jpDetail.jtfMC))
      return false;
    if (!dlgSerBrojevi.getdlgSerBrojevi().findSB(jpDetail.rpcart, getDetailSet(), 'U', mode)) {
      return false;
    }
    return super.ValidacijaDetail(mode);
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
        jpDetail.kalkulacija(0);
        jpDetail.disableUnosFields(true, 'P');
        jpDetail.rpcart.setCART();
        jpDetail.findSTANJE(' ');
//        jpDetail.rpcart.SetFocus(hr.restart.sisfun.frmParam.getParam("robno","focusCart"));
        return false;
      }
    }
    else {
      return true;
    }
  }
}