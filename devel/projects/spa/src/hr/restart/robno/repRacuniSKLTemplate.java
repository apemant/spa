/****license*****************************************************************
**   file: repRacuniSKLTemplate.java
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

import hr.restart.util.reports.raDetailRacuniSKL;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raRPSectionHeaderROT;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raSectionFooter0RacuniSKL;
import hr.restart.util.reports.raSectionHeader1RacuniSKL;

public class repRacuniSKLTemplate extends repIzlazOrigTemplate {

  public repRacuniSKLTemplate() {
    System.out.println("repRacuniSKLTemplate");
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepRacuniSKL");
  }

  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this); // return new raIzlazPageHeader(this);
//    return createCustomSection("ROT", "H", super.createPageHeader());
  }

  public raReportSection createSectionHeader0() {
    raRPSectionHeaderROT sh = new raRPSectionHeaderROT(this);
    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nOTPREMNICA");
    sh.LabelR1.defaultAlterer().setVisible(false);
    sh.LabelObrazac.defaultAlterer().setVisible(false);
    return sh;

//    raRPSectionHeader sh = new raRPSectionHeader(this);
//    sh.LabelRACUNOTPREMNICA.setDefault(ep.CAPTION, "\nOTPREMNICA");
//    return sh;
  }

  public raReportSection createSectionHeader1() {
    return new raSectionHeader1RacuniSKL(this);
  }

  public raReportSection createDetail() {
    return new raDetailRacuniSKL(this);
  }

  public raReportSection createSectionFooter1() {
    return new raSectionFooter0RacuniSKL(this);
  }
}
