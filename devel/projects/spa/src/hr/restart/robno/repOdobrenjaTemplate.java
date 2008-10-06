/****license*****************************************************************
**   file: repOdobrenjaTemplate.java
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

import hr.restart.util.reports.raIzlazDetail;
import hr.restart.util.reports.raIzlazSectionFooterLines;
import hr.restart.util.reports.raIzlazSectionHeaderLines;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raSectionHeader0TF;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */




public class repOdobrenjaTemplate extends repIzlazOrigTemplate {

  public raReportSection createSectionHeader0() {
//    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nR A \u010C U N");
//    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
//        deleteElementsPushDown(sh, new raReportElement[] {sh.LabelParitet, sh.TextNAZFRA});
//    long top = sh.LabelNacin_placanja.defaultAlterer().getTop();
//    raReportElement lab = sh.copyToModify(sh.LabelNarudzba);
//    lab.setCaption("Izlazni dokument");
//    lab.setTop(top);
//    lab.setLeft(lab.getLeft() - 300);
//    lab.setWidth(lab.getWidth() + 300);
//    raReportElement tx1 = sh.copyToModify(sh.TextBRNARIZ);
//    tx1.setControlSource("BRDOKIZ");
//    tx1.setTop(top);
//    raReportElement tx2 = sh.copyToModify(sh.TextSgetDATNARIZ);
//    tx2.setControlSource("SgetDATDOKIZ");
//    tx2.setTop(top);

    raSectionHeader0TF sh = new raSectionHeader0TF(this);
    sh.LabelR_A_C_U_N.defaultAlterer().setCaption("\nODOBRENJE");
    //zakaii:: sh.TextTEL.defaultAlterer().setVisible(false);
   
    
    return sh;
//    sh.LabelNacinplacanja.defaultAlterer().setTop(sh.LabelNacinotpreme.defaultAlterer().getTop());
//    sh.TextNAZNACPL.defaultAlterer().setTop(sh.TextNAZNAC.defaultAlterer().getTop());
//    sh.removeModels(new raReportElement[] {sh.LabelNacinotpreme, sh.TextNAZNAC});

  }

  public raReportSection createSectionHeader1() {
    return new raIzlazSectionHeaderLines(this); // return new raIzlazSectionHeader(this);
  }

  public raReportSection createDetail() {
    return new raIzlazDetail(this);
  }

  public raReportSection createSectionFooter1() {
    return new raIzlazSectionFooterLines(this); // return new raIzlazSectionFooter(this);
  }

  public repOdobrenjaTemplate() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepRac");
  }
}
/*




public class repOdobrenjaTemplate extends repOdobrenjaOrigTemplate {

  public raReportSection createReportHeader() {
    return new raStandardReportHeader(this);
  }

  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this); // return new raIzlazPageHeader(this);
  }

  public raReportSection createSectionHeader0() {
    raRPSectionHeader sh = new raRPSectionHeader(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nODOBRENJE");
    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
        deleteElementsPushDown(sh, new raReportElement[] {sh.LabelNacinotpreme,
        sh.LabelParitet, sh.TextNAZNAC, sh.TextNAZFRA});
    return sh;
  }

  public raReportSection createPageFooter() {
    return new raIzlazPageFooter(this);
  }

  public raReportSection createReportFooter() {
    return new raStandardReportFooter(this);
  }

  public repOdobrenjaTemplate() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepOdobrenja");
  } // ode zvjezdica frontsleš
}
*/