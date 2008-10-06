/****license*****************************************************************
**   file: repRacUslugaTemplate.java
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

import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raSectionHeader0TF;
import hr.restart.util.reports.raUslDetail;
import hr.restart.util.reports.raUslSectionFooter;
import hr.restart.util.reports.raUslSectionHeader;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repRacUslugaTemplate extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {
    raSectionHeader0TF sh = new raSectionHeader0TF(this);
//  zakaii::     sh.TextTEL.defaultAlterer().setVisible(false);
    
    
//    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nR A \u010C U N");
    sh.defaultAltererSect().getView(sh.LabelMjestoDatum, sh.TextNAZNACPL).
        deleteElementsPushDown(sh, new raReportElement[] {sh.LabelIzlazni_dokument, sh.TextBRDOKIZ,sh.TextSgetDATDOKIZ});
    
//    long top = sh.LabelNacin_placanja.defaultAlterer().getTop();
    
  sh.LabelNarudzba.defaultAlterer().setCaption("Ugovor");
  sh.TextBRNARIZ.defaultAlterer().setControlSource("CUG");
  sh.TextSgetDATNARIZ.defaultAlterer().setControlSource("SgetDATUG");
    
    /*if (hr.restart.sisfun.frmParam.getParam("robno","ispisTELFAX_Par","N","Ispis Telefona i faxa partnera na ROT-u u polju za adresu").
        equalsIgnoreCase("N")) {
      sh.TextTEL.defaultAlterer().setVisible(false);
//      sh.TextFAX.defaultAlterer().setVisible(false);
    }*/
//    sh.LabelNacinplacanja.defaultAlterer().setTop(sh.LabelNacinotpreme.defaultAlterer().getTop());
//    sh.TextNAZNACPL.defaultAlterer().setTop(sh.TextNAZNAC.defaultAlterer().getTop());
//    sh.removeModels(new raReportElement[] {sh.LabelNacinotpreme, sh.TextNAZNAC});

    return sh;
  }
  
//  public raReportSection createSectionHeader0() {
//    raRPSectionHeader sh = new raRPSectionHeader(this);
//    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nR A \u010C U N");
//    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
//        deleteElementsPushDown(sh, new raReportElement[] {sh.LabelNacinotpreme,
//        sh.LabelParitet, sh.TextNAZNAC, sh.TextNAZFRA});
//    sh.LabelNarudzba.defaultAlterer().setCaption("Ugovor");
//    sh.TextBRNARIZ.defaultAlterer().setControlSource("CUG");
//    sh.TextSgetDATNARIZ.defaultAlterer().setControlSource("SgetDATUG");
//    return sh;
//  }
  
  public raReportSection createSectionFooter1() {
    return new raUslSectionFooter(this);
  }
  public raReportSection createDetail() {
    /*return new raUslDetailShifra(this);*/return new raUslDetail(this);
  }
  public raReportSection createSectionHeader1() {
    /*return new raUslSectionHeaderShifra(this);*/return new raUslSectionHeader(this);
  }
}

