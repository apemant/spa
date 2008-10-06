/****license*****************************************************************
**   file: repDugPotPeriodTemplate.java
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
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repDugPotPeriodTemplate extends repDugPotPeriodOrigTemplate {
  public raReportSection createReportHeader() {
    return new raStandardReportHeader(this);
  }
  public raReportSection createReportFooter() {
    return new raStandardReportFooter(this);
  }
  public raReportSection createPageHeader() {
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }
  public repDugPotPeriodTemplate() {
    this.addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }
  private void modifyThis() {
    upOpenRac up = upOpenRac.getInstance();
    SectionHeader1.restoreDefaults();
    Detail.restoreDefaults();
    SectionFooter1.restoreDefaults();
    int pers = up.getPeriods();
    /*SectionHeader1.deflateElement(LabelH4);
    Detail.deflateElement(TextCOL4);
    SectionFooter1.deflateElement(Text1);*/
    if (up.isUpl()) {
      LabelH3.setCaption(up.dospPer ? "Dospjelo" : "Nedospjelo");
      LabelH4.setCaption("Otv. uplate");
      TextCOL4.setControlSource("UPL");
      Text1.setControlSource("=(dsum \"UPL\")");
    } else if (pers < 2) {
      SectionHeader1.deflateElement(LabelH4);
      Detail.deflateElement(TextCOL4);
      SectionFooter1.deflateElement(Text1);
      LabelH3.setCaption(up.dospPer ? "Dospjelo" : "Nedospjelo");
    } else {
      LabelH3.setCaption((up.dospPer ? "Do " : "Za ")+up.getPeriod1()+" dana");
      LabelH4.setCaption("Preko "+up.getPeriod1()+" dana");
    }
    LabelH2.setCaption(up.dospPer ? "Nedospjelo" : "Dospjelo");
    LabelH1.setCaption("Otv. Raèuni");
    if (up.jpk.getKonto() != null && up.jpk.getKonto().length() > 0) {
      
    }
  }
}