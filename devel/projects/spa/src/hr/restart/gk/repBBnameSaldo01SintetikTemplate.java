/****license*****************************************************************
**   file: repBBnameSaldo01SintetikTemplate.java
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

public class repBBnameSaldo01SintetikTemplate extends repBBnameSaldoOrigTemplate {

  public repBBnameSaldo01SintetikTemplate() {
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepBBnameSaldo01Sintetik");
    this.addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  public raReportSection createDetail() {
    return new raReportSection(template.getModel(ep.DETAIL), 
        new String[] {"", "", "", "", "", "", "", "0"});
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
  }

}
