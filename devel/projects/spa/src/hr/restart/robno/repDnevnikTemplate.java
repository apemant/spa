/****license*****************************************************************
**   file: repDnevnikTemplate.java
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
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repDnevnikTemplate extends repDnevnikOrigTemplate {
  public raReportSection createReportHeader() {
    return new raStandardReportHeader(this);
  }

  public raReportSection createPageHeader() {
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }

  public raReportSection createReportFooter() {
    return new raStandardReportFooter(this);
  }

  public repDnevnikTemplate() {

    this.addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }
  public void modifyThis() {
    boolean invis = hr.restart.sisfun.frmParam.getParam("robno","ispLogo","N","Ispis loga na dokumentima (D/N)").equals("N");
    this.SectionHeader1.restoreDefaults();
    this.Detail.restoreDefaults();
    this.SectionHeader0.getView(this.TextFirstLine, this.TextDatumIsp).setTransparent(invis);
    this.PageFooter.setTransparent(invis);


    this.LabelSifra.setCaption(Aut.getAut().getCARTdependable("Šifra", "Oznaka", "Barcode"));
    this.SectionHeader1.resizeElement(this.LabelSifra, Aut.getAut().getCARTdependable(900, 1780, 1780), this.LabelNaziv);
    this.Detail.resizeElement(this.TextSIFRA, Aut.getAut().getCARTdependable(900, 1780, 1780), this.TextNAZART);
  }
}
