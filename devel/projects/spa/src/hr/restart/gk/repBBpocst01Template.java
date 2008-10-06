/****license*****************************************************************
**   file: repBBpocst01Template.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.reports.ReportModifier;
import hr.restart.util.reports.raElixirPropertyValues;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;

public class repBBpocst01Template extends repBBpromOrigTemplate {

  public repBBpocst01Template() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepBBpocst01");
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
    boolean prije = frmParam.getFrmParam().getParam("gk","brBilRslj","E","Redosljed klasa P - prije E - poslje").equals("P");
    
    this.Section2.setProperty(ep.GROUP_HEADER, prije ? raElixirPropertyValues.YES : raElixirPropertyValues.NO);
    this.Section3.setProperty(ep.GROUP_HEADER, prije ? raElixirPropertyValues.YES : raElixirPropertyValues.NO);
    this.Section4.setProperty(ep.GROUP_HEADER, prije ? raElixirPropertyValues.YES : raElixirPropertyValues.NO);
    
    this.Section2.setProperty(ep.GROUP_FOOTER, prije ? raElixirPropertyValues.NO : raElixirPropertyValues.YES);
    this.Section3.setProperty(ep.GROUP_FOOTER, prije ? raElixirPropertyValues.NO : raElixirPropertyValues.YES);
    this.Section4.setProperty(ep.GROUP_FOOTER, prije ? raElixirPropertyValues.NO : raElixirPropertyValues.YES);
    
    this.Text3.setProperty(ep.CONTROL_SOURCE, "=(dsum \"SALPS\")");
    this.Text6.setProperty(ep.CONTROL_SOURCE, "=(dsum \"SALPS\")");
    this.Text9.setProperty(ep.CONTROL_SOURCE, "=(dsum \"SALPS\")");
    this.Text10.setProperty(ep.CONTROL_SOURCE, "=(dsum \"SALPS\")");
    this.Text13.setProperty(ep.CONTROL_SOURCE, "=(dsum \"SALPS\")");
    this.Text16.setProperty(ep.CONTROL_SOURCE, "=(dsum \"SALPS\")");
    this.Text21.setProperty(ep.CONTROL_SOURCE, "=(dsum \"SALPS\")");
    this.Text22.setProperty(ep.CONTROL_SOURCE, "=(dsum \"SALPS\")");
    
    this.Text2.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IDALL\")");
    this.Text5.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IDALL\")");
    this.Text7.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IDALL\")");
    this.Text12.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IDALL\")");
    this.Text14.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IDALL\")");
    this.Text17.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IDALL\")");
    this.Text20.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IDALL\")");
    this.Text23.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IDALL\")");
    
    this.Text1.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IPALL\")");
    this.Text4.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IPALL\")");
    this.Text8.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IPALL\")");
    this.Text11.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IPALL\")");
    this.Text15.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IPALL\")");
    this.Text18.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IPALL\")");
    this.Text19.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IPALL\")");
    this.Text24.setProperty(ep.CONTROL_SOURCE, "=(dsum \"IPALL\")");
    
    this.TextIP.setProperty(ep.CONTROL_SOURCE, "IPALL");
    this.TextID.setProperty(ep.CONTROL_SOURCE, "IDALL");
  }

}
