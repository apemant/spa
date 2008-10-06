/****license*****************************************************************
**   file: repBBrekap01PocStTemplate.java
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
package hr.restart.gk;

import hr.restart.util.reports.ReportModifier;
import hr.restart.util.reports.raElixirPropertyValues;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;

public class repBBrekap01PocStTemplate extends repBBpromOrigTemplate {

  public repBBrekap01PocStTemplate() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepBBrekap01PocSt");
    this.addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  public raReportSection createReportHeader() {
    return  new raStandardReportHeader(this);
  }

  public raReportSection createReportFooter() {
    return  new raStandardReportFooter(this);
  }

  public raReportSection createPageHeader() {
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }
  
  public void modifyThis() {
    this.LabelBRUTO_BILANCA.setProperty(ep.CAPTION, "\nR E K A P I T U L A C I J A");
    
    this.Section2.setProperty(ep.GROUP_HEADER, raElixirPropertyValues.NO);
    this.Section3.setProperty(ep.GROUP_HEADER, raElixirPropertyValues.NO);
    this.Section4.setProperty(ep.GROUP_HEADER, raElixirPropertyValues.NO);
    
    this.Section2.setProperty(ep.GROUP_FOOTER, raElixirPropertyValues.NO);
    this.Section3.setProperty(ep.GROUP_FOOTER, raElixirPropertyValues.NO);
    this.Section4.setProperty(ep.GROUP_FOOTER, raElixirPropertyValues.NO);
    
    this.Text19.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IDALL\")");
    this.Text20.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IPALL\")");
    this.Text23.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IDALL\")");
    this.Text24.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IPALL\")");
    
    this.TextIP.setProperty(ep.CONTROL_SOURCE, "IPALL");
    this.TextID.setProperty(ep.CONTROL_SOURCE, "IDALL");
    this.TextSALPROM.setProperty(ep.CONTROL_SOURCE, "SALDO");
  }

}
