/****license*****************************************************************
**   file: repPregledVisakExtendedVersionTemplate.java
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

import hr.restart.util.reports.raDetailVriOMV;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raSectionFooter0Sum;
import hr.restart.util.reports.raSectionHeader1Vri;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;

public class repPregledVisakExtendedVersionTemplate extends repOMVTemplate {

  public repPregledVisakExtendedVersionTemplate() {
    System.out.println("F**KIN CLASS!! No01 - repPregledVisakExtendedVersionTemplate");
    this.ReportTemplate.setDefault(ep.RECORD_SOURCE, "JDOrepPregledVisakExtendedVersion");
    this.LabelOTPIS_ROBE.setDefault(ep.CAPTION, "\nINVENTURNI VIŠAK");
  }

  public raReportSection createSectionHeader1() {
    return new raSectionHeader1Vri(this);
  }

  public raReportSection createDetail() {
    raDetailVriOMV rdv = new raDetailVriOMV(this);
    return rdv;
  }

  public raReportSection createSectionFooter0() {
    return new raSectionFooter0Sum(this);
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
}