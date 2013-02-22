/****license*****************************************************************
**   file: raTeret.java
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

public class raTeret extends raIzlazTemplate {

  public void initialiser(){
    what_kind_of_dokument = "TER" ;
  }
  public void MyaddIspisMaster(){
    raMaster.getRepRunner().addReport("hr.restart.robno.repTerecenja", "hr.restart.robno.repIzlazni", "Terecenja", "Ispis tereæenja");
    raMaster.getRepRunner().addReport("hr.restart.robno.repTerecenjaV", "hr.restart.robno.repIzlazni", "Terecenja", "Ispis tereæenja u valuti");
//    raMaster.getRepRunner().addReport("hr.restart.robno.repTerecenja", "Ispis svih tere\u0107enja", 2);
  }

  public void MyaddIspisDetail(){
    raDetail.getRepRunner().addReport("hr.restart.robno.repTerecenja", "hr.restart.robno.repIzlazni", "Terecenja", "Ispis tereæenja");
    raDetail.getRepRunner().addReport("hr.restart.robno.repTerecenjaV", "hr.restart.robno.repIzlazni", "Terecenja", "Ispis tereæenja u valuti");
//    raDetail.getRepRunner().addReport("hr.restart.robno.repTerecenja", "Ispis tere\u0107enja", 2);
  }

  public raTeret() {

    setPreSel((jpPreselectDoc) presTER.getPres());
    master_titel = "Tere\u0107enja";
    detail_titel_mno = "Stavke tere\u0107enja";
    detail_titel_jed = "Stavke tere\u0107enja";
    raDetail.addOption(rnvKartica, 4, false);
    setMasterSet(dm.getZagTer());
    setDetailSet(dm.getStTer());
    raMaster.addOption(rnvFisk, 5, false);
    MP.BindComp();
    DP.BindComp();

  }

  public boolean ValidacijaStanje(){
    return true ;
  }

/*
public class raTeret extends frmTeretOdob {

  public void beforeShowMaster() {

      presel = (jpPreselectDoc) presTER.getPresTER();
      super.beforeShowMaster();
      this.setNaslovMaster("Tere\u0107enja");
      this.setNaslovDetail("Stavke tere\u0107enja");
      this.raMaster.getRepRunner().clearAllReports();
      this.raMaster.getRepRunner().addReport("hr.restart.robno.repTerecenja", "Ispis svih tere\u0107enja", 2);
      this.raDetail.getRepRunner().clearAllReports();
      this.raDetail.getRepRunner().addReport("hr.restart.robno.repTerecenja", "Ispis tere\u0107enja", 2);
  }
*/
}