/****license*****************************************************************
**   file: repOtpisRobeExtendedVersionTemplate.java
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

import hr.restart.util.reports.raDetailVriOMV;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raSectionFooter0Sum;
import hr.restart.util.reports.raSectionHeader1Vri;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;

public class repOtpisRobeExtendedVersionTemplate extends repOMVTemplate {

  public repOtpisRobeExtendedVersionTemplate() {
    System.out.println("CLASS!!");
//    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "repOtpisRobeExtendedVersion");
//    this.addReportModifier(new ReportModifier() {
//      public void modify() {
//        modifyThis();
//      }
//    });
  }

  public raReportSection createSectionHeader1() {
    raSectionHeader1Vri rsh = new raSectionHeader1Vri(this);
    rsh.LabelSifra.setCaption(Aut.getAut().getCARTdependable("Šifra", "Oznaka", "Barcode"));
    return rsh;
  }

  public raReportSection createDetail() {
    raDetailVriOMV rdv = new raDetailVriOMV(this);
//    rdv.resizeElement(rdv.TextCART, Aut.getAut().getCARTdependable(900, 1780, 1780), rdv.TextCART);
    return rdv;
  }

  public raReportSection createSectionFooter0() {
    return new raSectionFooter0Sum(this);
  }

  public raReportSection createReportHeader() {
    return new raStandardReportHeader(this);
  }

  public raReportSection createReportFooter() {
    return new raStandardReportFooter(this);
  }

  public raReportSection createPageHeader() {
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }

//  public void modifyThis() {
//    this.LabelSifra.setCaption(Aut.getAut().getCARTdependable("Šifra", "Oznaka", "Barcode"));
//    this.SectionHeader1.resizeElement(this.LabelSifra, Aut.getAut().getCARTdependable(900, 1780, 1780), this.LabelNaziv);
//    this.Detail.resizeElement(this.TextCART, Aut.getAut().getCARTdependable(900, 1780, 1780), this.TextCART);
//  }
}