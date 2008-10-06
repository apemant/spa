/****license*****************************************************************
**   file: repRacRnalTemplate.java
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
import hr.restart.util.reports.raRacRnalSection_Header0;
import hr.restart.util.reports.raReportSection;

public class repRacRnalTemplate extends repRacZaRnalOrigTemplate {

  public repRacRnalTemplate() {
  }
//  public raReportSection createSectionFooter0() {
//    return new raIzlazSectionFooter(this);
//  }
  public raReportSection createSectionHeader0() {
    return new raRacRnalSection_Header0(this);
  }
  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this); // return new raIzlazPageHeader(this);
  }
//  public raReportSection createDetail() {
//    return new raIzlazDetail(this);
//  }
//  public raReportSection createSectionHeader1() {
//    return new raIzlazSectionHeaderLines(this);
//  }
//  public raReportSection createSectionFooter2() {
//    return new raRacRnalSumSection_Footer2(this);
//  }
  public raReportSection createPageFooter() {
    return new raIzlazPageFooter(this);
  }
}