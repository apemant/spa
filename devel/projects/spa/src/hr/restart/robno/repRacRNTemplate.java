/****license*****************************************************************
**   file: repRacRNTemplate.java
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

import hr.restart.util.reports.raIzlazPageFooter;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raRacRNSection_Header0;
import hr.restart.util.reports.raReportSection;

/**
 * @author S.G.
 *
 * Started 2005.04.25
 * 
 */

public class repRacRNTemplate extends repRacRNOrigTemplate {
  
  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this);
  }

  public raReportSection createSectionHeader0() {
    return new raRacRNSection_Header0(this);
  }
  
  public raReportSection createPageFooter() {
    return new raIzlazPageFooter(this);
  }

}
