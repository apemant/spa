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

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import com.borland.dx.dataset.DataRow;

import hr.restart.sisfun.frmParam;
import hr.restart.sk.dlgSplitAmount;
import hr.restart.util.Aus;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;


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
  
  raNavAction rnvSplit = new raNavAction("Razdvajanje stavke", raImages.IMGPAUSE, KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      splitStavka();
    }
  };

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
    
    raDetail.addOption(rnvSplit, 4, false);
    
  }
  
  public void beforeShowMaster() {
    jpDetail.onlySklad = frmParam.getParam("robno", "onlySkladArt", "N", 
        "Dopustiti samo artikle sa stanjem na ulazima (D,N)").equals("D");
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
  void splitStavka() {
    if (getDetailSet().rowCount() == 0) return;
    
    dlgSplitAmount dlg = null;
    if (this.getWindow() instanceof Frame)
      dlg= new dlgSplitAmount((Frame) this.getWindow());
    if (this.getWindow() instanceof Dialog)
      dlg = new dlgSplitAmount((Dialog) this.getWindow());
    dlg.setFields(3);
    BigDecimal result = dlg.performSplit("Kolièina", getDetailSet().getBigDecimal("KOL"));
    if (result != null) {
      int rowSel = getDetailSet().getRow();
      DataRow copy = new DataRow(getDetailSet());
      getDetailSet().copyTo(copy);
      getDetailSet().setBigDecimal("KOL", getDetailSet().getBigDecimal("KOL").subtract(result));
      String[] vals = {"INAB", "IMAR", "IBP", "IPOR", "ISP", "IZAD"};
      for (int i = 0; i < vals.length; i++) {
        getDetailSet().setBigDecimal(vals[i], getDetailSet().getBigDecimal(vals[i])
            .multiply(getDetailSet().getBigDecimal("KOL")).divide(copy.getBigDecimal("KOL"), 2, BigDecimal.ROUND_HALF_UP));
        copy.setBigDecimal(vals[i], copy.getBigDecimal(vals[i]).subtract(getDetailSet().getBigDecimal(vals[i])));
      }
      getDetailSet().last();
      getDetailSet().insertRow(false);
      copy.copyTo(getDetailSet());
      getDetailSet().setBigDecimal("KOL", result);
      findNSTAVKA();
      getDetailSet().setShort("RBR", nStavka);
      getDetailSet().setInt("RBSID", rbr.getRbsID(getDetailSet()));
      getDetailSet().setString(
              "ID_STAVKA",
              raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
                      "vrdok", "god", "brdok", "rbsid" }, "stdoku"));
      try {
        getDetailSet().saveChanges();
      } catch (Exception e) {
        getDetailSet().refresh();
        getDetailSet().goToClosestRow(rowSel);
      }      
    }    
  }
  public boolean ValidacijaDetail(char mode) {
    if (jpDetail.jtfKOL.getText().trim().length() == 0 && vl.isEmpty(jpDetail.jtfKOL))
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