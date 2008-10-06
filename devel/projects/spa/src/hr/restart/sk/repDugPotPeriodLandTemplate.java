/****license*****************************************************************
**   file: repDugPotPeriodLandTemplate.java
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
import hr.restart.util.reports.raReportElement;
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

public class repDugPotPeriodLandTemplate extends repDugPotPeriodLandOrigTemplate {
  public raReportSection createReportHeader() {
    return new raStandardReportHeader(this);
  }
  public raReportSection createReportFooter() {
    return new raStandardReportFooter(this);
  }
  public raReportSection createPageHeader() {
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }
  public repDugPotPeriodLandTemplate() {
    this.addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }
  
  private void checkUpl(raReportElement head, raReportElement data, raReportElement sum) {
    if (upOpenRac.getInstance().isUpl()) {
      head.setCaption("Otv. uplate");
      data.setControlSource("UPL");
      sum.setControlSource("=(dsum \"UPL\")");
    }
  }
  
  private void modifyThis() {
    upOpenRac up = upOpenRac.getInstance();
    SectionHeader1.restoreDefaults();
    Detail.restoreDefaults();
    SectionFooter1.restoreDefaults();
    int pers = up.getPeriods();
    if (up.isUpl()) ++pers;
    if (pers < 5) {
      SectionHeader1.deflateElement(LabelH7);
      Detail.deflateElement(TextCOL7);
      SectionFooter1.deflateElement(Text8);
    }
    if (pers < 4) {
      SectionHeader1.deflateElement(LabelH6);
      Detail.deflateElement(TextCOL6);
      SectionFooter1.deflateElement(Text1);
    }
    if (up.isUpl()) --pers;
    int p1 = up.getPeriod1();
    int p2 = up.getPeriod2();
    int p3 = up.getPeriod3();
    int p4 = up.getPeriod4();
    LabelH1.setCaption("Otv. raèuni");
    LabelH2.setCaption(up.dospPer ? "Nedospjelo" : "Dospjelo");
    LabelH3.setCaption((up.dospPer ? "Do " : "Za ")+p1+" dana");
    if (up.isKum()) LabelH4.setCaption("Za "+p2+" dana");
    else LabelH4.setCaption(""+(p1+1)+" - "+p2+" dana");
    if (pers == 2) {
      LabelH4.setCaption("Preko "+p1+" dana");
      checkUpl(LabelH5, TextCOL5, Text2);
    } else if (pers == 3) {
      LabelH5.setCaption("Preko "+p2+" dana");
      checkUpl(LabelH6, TextCOL6, Text1);
    } else if (pers == 4) {
      if (up.isKum()) LabelH5.setCaption("Za "+p3+" dana");
      else LabelH5.setCaption(""+(p2+1)+" - "+p3+" dana");
      LabelH6.setCaption("Preko "+p3+" dana");
      checkUpl(LabelH7, TextCOL7, Text8);
    } else {
      if (up.isKum()) LabelH5.setCaption("Za "+p3+" dana");
      else LabelH5.setCaption(""+(p2+1)+" - "+p3+" dana");
      if (up.isKum()) LabelH6.setCaption("Za "+p4+" dana");
      else LabelH6.setCaption(""+(p3+1)+" - "+p4+" dana");
      LabelH7.setCaption("Preko "+p4+" dana");
    }
  }
}