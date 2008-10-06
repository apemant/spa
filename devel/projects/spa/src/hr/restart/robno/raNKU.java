/****license*****************************************************************
**   file: raNKU.java
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


public class raNKU extends raIzlazTemplate  {

  public void initialiser() {
    what_kind_of_dokument = "NKU";
  }

  public void MyaddIspisMaster(){
    raMaster.getRepRunner().addReport("hr.restart.robno.repNarudzba","hr.restart.robno.repIzlazni","Narudzba","Narudžba 1 red");
    raMaster.getRepRunner().addReport("hr.restart.robno.repNarudzba2","hr.restart.robno.repIzlazni","Narudzba2","Narudžba 2 red");
  }

  public void MyaddIspisDetail(){
    raDetail.getRepRunner().addReport("hr.restart.robno.repNarudzba","hr.restart.robno.repIzlazni","Narudzba","Narudžba 1 red");
    raDetail.getRepRunner().addReport("hr.restart.robno.repNarudzba2","hr.restart.robno.repIzlazni","Narudzba2","Narudžba 2 red");
  }

  public raNKU() {
    setPreSel((jpPreselectDoc) presNKU.getPres());
    raMaster.addOption(rnvDellAll,3);
    raDetail.addOption(rnvDellAllStav,3);

    master_titel = "Narudžbe kupaca";
    detail_titel_mno = "Stavke narudžbe kupca";
    detail_titel_jed = "Stavka narudžbe kupca";
    set_kum_detail(false);
    setMasterSet(dm.getZagNar());
    setDetailSet(dm.getStNar());
    MP.BindComp();
    DP.BindComp();
  }
  public void RestPanelSetup() {
  	DP.addRest();
		DP.instalRezervaciju();
	}
  
  public void SetFokusDetail(char mode) {
		super.SetFokusDetail(mode);
		if (mode=='N'){
			DP.setRezervacija();
		}
  }
  
/* ovo sam sredio u raIzlazTemplate
  public void UpdateStanje(){
  }
*/
  public boolean ValidacijaStanje(){
    return true ;
  }
}