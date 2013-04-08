/****license*****************************************************************
**   file: raPRD.java
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


public class raPRD extends raIzlazTemplate  {

  public void initialiser() {
    what_kind_of_dokument = "PRD" ;
  }

  public void MyaddIspisMaster(){
    raMaster.getRepRunner().addReport("hr.restart.robno.repPredracuni","hr.restart.robno.repIzlazni","Predracuni","Raèun za predujam 1 red");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPredracuniV","hr.restart.robno.repIzlazni","Predracuni","Raèun za predujam 1 red u valuti");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPredracuni2","hr.restart.robno.repIzlazni","Predracuni2","Ra\u010Dun za predujam 2 red");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPredracuni2V","hr.restart.robno.repIzlazni","Predracuni2","Ra\u010Dun za predujam 2 red u valuti");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPredracuniValSingle",
        "hr.restart.robno.repIzlazni", "PredracuniValSingle", 
        "Raèun za predujam s valutom bez kolièina");
    raMaster.getRepRunner().addReport("hr.restart.robno.repProformaInvoice",
        "hr.restart.robno.repIzlazni","ProformaInvoice2","Proforma Invoice");
  }
  public void MyaddIspisDetail(){
    raDetail.getRepRunner().addReport("hr.restart.robno.repPredracuni","hr.restart.robno.repIzlazni","Predracuni","Raèun za predujam 1 red");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPredracuniV","hr.restart.robno.repIzlazni","Predracuni","Raèun za predujam 1 red u valuti");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPredracuni2","hr.restart.robno.repIzlazni","Predracuni2","Raèun za predujam 2 red");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPredracuni2V","hr.restart.robno.repIzlazni","Predracuni2","Raèun za predujam 2 red u valuti");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPredracuniValSingle",
        "hr.restart.robno.repIzlazni", "PredracuniValSingle", 
        "Raèun za predujam s valutom bez kolièina");
    raDetail.getRepRunner().addReport("hr.restart.robno.repProformaInvoice",
        "hr.restart.robno.repIzlazni","ProformaInvoice2","Proforma Invoice");
  }

  public raPRD() {
    setPreSel((jpPreselectDoc) presPRD.getPres());
    addButtons(true,true);
//    raMaster.addOption(rnvDellAll,3);
    raDetail.addOption(rnvDellAllStav,3);
    master_titel = "Ra\u010Duni za predujam";
    detail_titel_mno = "Stavke ra\u010Duna za predujam";
    detail_titel_jed = "Stavka ra\u010Duna za predujam";
    setMasterSet(dm.getZagPrd());
    setDetailSet(dm.getStPrd());
    raMaster.addOption(rnvFisk, 5, false);
    raDetail.addOption(rnvKartica,4, false);
//    set_kum_detail(false);
    MP.BindComp();
    DP.BindComp();
  }
/* ovo sam sredio u raIzlazTemplate
  public void UpdateStanje(){
  }
*/
  public boolean ValidacijaStanje(){
    return true ;
  }
  public boolean DodatnaValidacijaDetail() {

    if (val.isEmpty(DP.jtfKOL)) return false;
//    if (val.isEmpty(DP.jraFVC)) return false;
    if (val.isEmpty(DP.jraFC)) return false;
//    if (val.isEmpty(DP.jraFMC)) return false;
    if (manjeNula()) return false;
    return true;
  }

}