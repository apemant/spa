/****license*****************************************************************
**   file: raOdobrenje.java
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

import javax.swing.JOptionPane;

import hr.restart.sisfun.frmParam;
import hr.restart.swing.raColors;
import hr.restart.swing.raStatusColorModifier;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;

public class raOdobrenje extends raIzlazTemplate {
  
  raStatusColorModifier msc = new raStatusColorModifier("STATIRA", "N",
      raColors.green, Color.green.darker().darker());
  
  raNavAction rnvMasterFlag = new raNavAction("Podvrda povratnice",
      raImages.IMGPROPERTIES, java.awt.event.KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
      potvrda();
    }
  };

  public void initialiser(){
    what_kind_of_dokument = "ODB" ;
  }
  
  /*
    raMaster.getRepRunner().addReport("hr.restart.robno.repRac", "hr.restart.robno.repIzlazni", "Rac", ReportValuteTester.titleRAC1R);
  */
  
  public void MyaddIspisMaster(){
    raMaster.getRepRunner().addReport("hr.restart.robno.repOdobrenja", "hr.restart.robno.repIzlazni", "Odobrenja", "Ispis odobrenja");
    raMaster.getRepRunner().addReport("hr.restart.robno.repOdobrenjaV", "hr.restart.robno.repIzlazni", "Odobrenja", "Ispis odobrenja u valuti");
    raMaster.getRepRunner().addReport("hr.restart.robno.repOdobrenjaP", "hr.restart.robno.repRacuniPnP", "OdobrenjaPnP", "Ispis odobrenja s popustima");
    raMaster.getRepRunner().addReport("hr.restart.robno.repOdobrenjaPV", "hr.restart.robno.repRacuniPnP", "OdobrenjaPnP", "Ispis odobrenja s popustima u valuti");
  }
  public void MyaddIspisDetail(){
    raDetail.getRepRunner().addReport("hr.restart.robno.repOdobrenja", "hr.restart.robno.repIzlazni", "Odobrenja", "Ispis odobrenja");
    raDetail.getRepRunner().addReport("hr.restart.robno.repOdobrenjaV", "hr.restart.robno.repIzlazni", "Odobrenja", "Ispis odobrenja u valuti");
    raDetail.getRepRunner().addReport("hr.restart.robno.repOdobrenjaP", "hr.restart.robno.repRacuniPnP", "OdobrenjaPnP", "Ispis odobrenja s popustima");
    raDetail.getRepRunner().addReport("hr.restart.robno.repOdobrenjaPV", "hr.restart.robno.repRacuniPnP", "OdobrenjaPnP", "Ispis odobrenja s popustima u valuti");
//    raDetail.getRepRunner().addReport("hr.restart.robno.repOdobrenja", "Ispis odobrenja", 2);
  }
  
  public raOdobrenje() {
    setPreSel((jpPreselectDoc) presODB.getPres());
    isOJ = true;
    master_titel = "Odobrenja";
    detail_titel_mno = "Stavke odobrenja";
    detail_titel_jed = "Stavke odobrenja";
    raDetail.addOption(rnvKartica, 4, false);
    setMasterSet(dm.getZagOdb());
    setDetailSet(dm.getStOdb());
    raMaster.addOption(rnvFisk, 5, false);
    MP.BindComp();
    DP.BindComp();
    if (frmParam.getParam("robno", "racSklad", "D", 
        "Dodati skladi�te na RAC/PON (D,N)").equals("D")) {
      DP.rpcart.addSkladField(hr.restart.robno.Util.getSkladFromCorg());
    }
    DP.resizeDP();

    if (frmParam.getParam("robno", "verifyPOD", "N", "Verifikacija povratnica (D,N)?").equalsIgnoreCase("D")) {
      raMaster.getJpTableView().addTableModifier(msc);
      raMaster.addOption(rnvMasterFlag, 5, false);
    }
  }
  
  void potvrda() {
    if (getMasterSet().rowCount() == 0) return;
    
    if (isKnjigen()) {
      JOptionPane.showMessageDialog(raMaster.getWindow(), 
          "Dokument je ve� proknji�en!", "Gre�ka", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    boolean unc = getMasterSet().getString("STATIRA").equalsIgnoreCase("N");
    if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "�elite li " +
        (unc ? "potvrditi povratnicu" : "poni�titi potvrdu povratnice") + "?",
        "Potvrda povratnice", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
      return;
    
    getMasterSet().setString("STATIRA", unc ? "P" : "N");
    getMasterSet().saveChanges();
    raMaster.getJpTableView().fireTableDataChanged();
  }
  
  public void cskl2csklart() {
    // sprijeci kopiranje csklart
  }

  public boolean ValidacijaStanje(){
    return true ;
  }
}

/*
public class raOdobrenje extends frmTeretOdob {

  public void beforeShowMaster() {
    presel = (jpPreselectDoc) presODB.getPresODB();
    super.beforeShowMaster();
      this.setNaslovMaster("Odobrenja");
      this.setNaslovDetail("Stavke odobrenja");
      this.raMaster.getRepRunner().clearAllReports();
      this.raMaster.getRepRunner().addReport("hr.restart.robno.repOdobrenja", "Ispis svih odobrenja", 2);
      this.raDetail.getRepRunner().clearAllReports();
      this.raDetail.getRepRunner().addReport("hr.restart.robno.repOdobrenja", "Ispis odobrenja", 2);
  }
}
*/
