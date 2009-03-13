/****license*****************************************************************
**   file: repUpitPonudaTemplate.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.reports.ReportModifier;
import hr.restart.util.reports.raElixirPropertyValues;
import hr.restart.util.reports.raIzlazPageFooter;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raRPSectionHeader;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;



public class repUpitPonudaTemplate extends repOTPOrigTemplate {

  public raReportSection createSectionHeader0() {
    raRPSectionHeader sh = new raRPSectionHeader(this);
    String caption = "\nUPIT ZA PONUDU";
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, caption);
    sh.defaultAltererSect().getView(sh.LabelMjesto, sh.TextNAZFRA).
        deleteElementsPushDown(sh, new raReportElement[] {sh.LabelNacinplacanja,
        sh.LabelDospijece, sh.TextSgetDATDOSP, sh.TextSgetDDOSP, sh.TextNAZNACPL});
    sh.LabelR1.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelObrazac.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelNacinotpreme.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.TextNAZNAC.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelNarudzba.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.TextBRNARIZ.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.TextSgetDATNARIZ.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.LabelParitet.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
    sh.TextNAZFRA.setDefault(ep.VISIBLE, raElixirPropertyValues.NO);
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
  public repUpitPonudaTemplate() {
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
    TextNAZARText.setLeft(TextNAZART.getLeft());
    boolean wide = frmParam.getParam("robno", "extNazartWide", "N", 
        "Široki dodatni opis artikla (D,N)?").equalsIgnoreCase("D");
    if (!wide) TextNAZARText.setWidth(TextNAZART.getWidth());
    else TextNAZARText.setWidth(TextKOL.getLeft() - TextNAZARText.getLeft() - 20);
  }
}
