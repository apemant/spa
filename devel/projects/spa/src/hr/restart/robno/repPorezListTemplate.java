/****license*****************************************************************
**   file: repPorezListTemplate.java
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

public class repPorezListTemplate extends repPorezListOrigTemplate {

  public repPorezListTemplate() {
    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  public raReportSection createReportFooter() {
    return new raStandardReportFooter(this);
  }
  public raReportSection createPageHeader() {
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }
  public raReportSection createReportHeader() {
    return new raStandardReportHeader(this);
  }

  private void modifyThis() {
    this.SectionHeader1.restoreDefaults();
    if (upPorezList.getInstance().getSUI().equals("U")) {
      this.SectionHeader1.deflateElement(LabelPOREZ);
      this.Detail.deflateElement(TextOPOR);
      this.SectionFooter0.deflateElement(TextSUMOPOR);
      this.Line4.setTopCm(0.7407);
      remZpp();
    } else if (upPorezList.getInstance().getSUI().equals("I")) {
      this.SectionHeader1.deflateElement(LabelPRETPOREZ);
      this.Detail.deflateElement(TextPRETPOR);
      this.SectionFooter0.deflateElement(TextSUMPRETPOREZ);
      this.Line4.setTopCm(0.7407);
      remZpp();
    }
  }

  private void remZpp() {
    LabelZA_POVRATPLATITI.setVisible(false);
    TextRAZLIKAPLATIT.setVisible(false);
    TextRAZLIKAVRATITI.setVisible(false);
    Label1.setHeight(13 *20);
  }
}