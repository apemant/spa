/****license*****************************************************************
**   file: repSaldoRUTemplate.java
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
package hr.restart.sk;

import hr.restart.util.reports.ReportModifier;
import hr.restart.util.reports.raElixirPropertyValues;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raSkKarticaSectionHeader;
import hr.restart.util.reports.raSkKarticaTotaliSectionFooter;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;
import hr.restart.util.reports.raUIPageFooter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repSaldoRUTemplate extends repSaldoRUOrigTemplate {

  public raReportSection createSectionHeader0() {
    return new raSkKarticaSectionHeader(this);
  }
  public raReportSection createSectionFooter0() {
    return new raSkKarticaTotaliSectionFooter(this);
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
  public raReportSection createPageFooter() {
    return new raUIPageFooter(this);
  }
  public repSaldoRUTemplate() {
    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void modifyThis() {
    raIspisKartica rik = raIspisKartica.getInstance(raIspisKartica.SINGLE);
    SectionHeader1.restoreDefaults();
    Detail.restoreDefaults();
    Sections.restoreDefaults();
    if (!rik.needTot()) {
      Section0.setProperty(ep.GROUP_FOOTER, raElixirPropertyValues.NO);
      Section2.setProperty(ep.GROUP_FOOTER, raElixirPropertyValues.NO);
    } else
      Section1.setProperty(ep.GROUP_FOOTER, raElixirPropertyValues.NO);

    if (!rik.needVrdok()) {
      SectionHeader1.deflateElement(LabelVrsta);
      Detail.deflateElement(TextVrsta);
    }
    if (!rik.needDosp()) {
      SectionHeader1.deflateElement(LabelDatum_dospjeca);
      Detail.deflateElement(TextDatDosp);
    }
    if (!rik.needIzvod()) {
      SectionHeader1.deflateElement(LabelBroj_izvoda);
      Detail.deflateElement(TextIzvod);
    }
    LabelU_K_U_P.setWidth(LabelNacin_placanja.getLeft() + LabelNacin_placanja.getWidth());
    Text1.setWidth(TextIznos.getWidth() + 20);
    Text1.setLeft(TextIznos.getLeft());
    Text2.setWidth(TextSaldo.getWidth() + 20);
    Text2.setLeft(TextSaldo.getLeft());
//    LabelBroj_izvoda.restoreDefaults();
  }
}