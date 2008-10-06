/****license*****************************************************************
**   file: repIspOSTemplate.java
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
package hr.restart.os;

import hr.restart.util.reports.raOSDetail01;
import hr.restart.util.reports.raOSPageHeader001;
import hr.restart.util.reports.raOSSectionFooter001;
import hr.restart.util.reports.raOSSectionFooter101;
import hr.restart.util.reports.raOSSectionHeader101;
import hr.restart.util.reports.raReportSection;

public class repIspOSTemplate extends repIspOSOrigTemplate {

  public repIspOSTemplate() {
    System.out.println("\nrepIspOSTemplate()\n");
  }
  public raReportSection createPageHeader() {
    return new raOSPageHeader001(this);
  }
  public raReportSection createSectionHeader1() {
    return new raOSSectionHeader101(this);
  }
  public raReportSection createDetail() {
    return new raOSDetail01(this);
  }
  public raReportSection createSectionFooter1() {
    return new raOSSectionFooter101(this);
  }
  public raReportSection createSectionFooter0() {
    return new raOSSectionFooter001(this);
  }
}