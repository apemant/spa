/****license*****************************************************************
**   file: repDOSTemplate.java
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

import hr.restart.util.reports.ReportModifier;
import hr.restart.util.reports.raElixirPropertyValues;
import hr.restart.util.reports.raIzlazPageFooter;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raRPSectionHeader;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;



public class repDOSTemplate extends repOTPOrigTemplate {

  public raReportSection createSectionHeader0() {
    raRPSectionHeader sh = new raRPSectionHeader(this);
    String caption = "";
    caption = "\n" + hr.restart.sisfun.frmParam.getParam("robno","DOScap","D O S T A V N I C A", "Naslov na dostavnici");
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, caption);
    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
        deleteElementsPushDown(sh, new raReportElement[] {sh.LabelNacinplacanja,
        sh.LabelDospijece, sh.TextSgetDATDOSP, sh.TextSgetDDOSP, sh.TextNAZNACPL, 
        sh.LabelParitet, sh.TextNAZFRA});
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelObrazac.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelNacinotpreme.setDefault(ep.CAPTION, "Prijem robe");
    sh.TextNAZNAC.setDefault(ep.CONTROL_SOURCE, "BRDOKIZ");
    sh.setDefault(ep.SHRINK, raElixirPropertyValues.NO);
    
    //sh.setDefault(ep.HEIGHT, "2")
    return sh;
  }
  public raReportSection createReportHeader() {
    return new raStandardReportHeader(this);
  }
  public raReportSection createReportFooter() {
    return new raStandardReportFooter(this);
  }
  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this); // return new raIzlazPageHeader(this);
  }
  public raReportSection createPageFooter() {
    return new raIzlazPageFooter(this);
  }
  public repDOSTemplate() {
    this.addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }
  public void modifyThis() {
    this.SectionHeader1.restoreDefaults();
    this.Detail.restoreDefaults();
    this.LabelSifra.setCaption(Aut.getAut().getIzlazCARTdep("Šifra", "Oznaka", "Barcode"));
    this.SectionHeader1.resizeElement(this.LabelSifra, Aut.getAut().getIzlazCARTwidth(), this.LabelNaziv_artikla);
    this.Detail.resizeElement(this.TextCART, Aut.getAut().getIzlazCARTwidth(), this.TextNAZART);
  }
}
