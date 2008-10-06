/****license*****************************************************************
**   file: repUldokTemplate.java
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

import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;
import hr.restart.util.reports.raUIPageFooter;
import hr.restart.util.reports.raUISectionHeader;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repUldokTemplate extends repUldokOrigTemplate {

  public raReportSection createSectionHeader0() {
    raUISectionHeader sect = new raUISectionHeader(this);
    sect.LabelPREGLED_IZLAZNIH_DOKUMENATA.setDefault(ep.CAPTION, "\nPREGLED ULAZNIH DOKUMENATA");
    return sect;
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

  public repUldokTemplate() {
  }
}
