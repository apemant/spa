/****license*****************************************************************
**   file: repKarticaTemplate.java
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

public class repKarticaTemplate extends repKarticaOrigTemplate {

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
  public repKarticaTemplate() {
    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }
  private void modifyThis() {
    SectionFooter0.setProperty(ep.FORCE_NEW, "After");
  }
}