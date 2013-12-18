/****license*****************************************************************
**   file: repIzlazGroupOrigTemplate.java
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

import hr.restart.util.reports.raElixirProperties;
import hr.restart.util.reports.raElixirPropertiesInstance;
import hr.restart.util.reports.raIzlazPageFooter;
import hr.restart.util.reports.raIzlazSectionFooterForCustom;
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raReportTemplate;
import hr.restart.util.reports.raStandardReportFooter;
import hr.restart.util.reports.raStandardReportHeader;
import sg.com.elixir.reportwriter.xml.ModelFactory;

public abstract class repIzlazGroupOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"", "hr", "", "", "", "10000",
     "", "", "", "", ""};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"300", "380.2", "538.65", "538.65", "",
     "Customize", "11907.0", "16839.899999999998", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"FormatBroj", "", "Yes", "Yes", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"FormatBroj", "", "Yes", "Yes", "Each Value", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"Group", "", "Yes", "No", "Each Value", "", ""};
  public raReportElement Section3;
  private String[] Section3Props = new String[] {"RBR", "", "", "", "Each Value", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  public raReportSection SectionHeader1;
  public raReportSection SectionHeader2;
  public raReportSection Detail;
  public raReportSection SectionFooter1;
  public raReportSection SectionFooter0;
  public raReportSection PageFooter;
  public raReportSection ReportFooter;

  public repIzlazGroupOrigTemplate() {
    createReportStructure();
    setReportProperties();
  }

  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
    Section2 = sect.getModel(ep.SECTION + 2, Section2Props);
    Section3 = sect.getModel(ep.SECTION + 3, Section3Props);
    return sect;
  }

  public raReportSection createReportHeader() {
    return new raStandardReportHeader(this);
  }

  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this); // return new raIzlazPageHeader(this);
  }

  public abstract raReportSection createSectionHeader0();

  public abstract raReportSection createSectionHeader1();
  
  public abstract raReportSection createSectionHeader2();

  public abstract raReportSection createDetail();

  public abstract raReportSection createSectionFooter1();
  
  public raReportSection createSectionFooter0() {
    return new raIzlazSectionFooterForCustom(this);    
  }

  public raReportSection createPageFooter() {
    return new raIzlazPageFooter(this);
  }

  public raReportSection createReportFooter() {
    return new raStandardReportFooter(this);
  }

  public void createReportStructure() {
    template = ModelFactory.getModel(ep.REPORT_TEMPLATE);
    ModelFactory.setCurrentReport(template);

    ReportTemplate = addSection(new raReportSection(template, ReportTemplateProps));

    PageSetup = addSection(new raReportSection(template.getModel(ep.PAGE_SETUP), PageSetupProps));
    Sections = addSection(createSections());

    ReportHeader = addSection(createReportHeader());
    PageHeader = addSection(createPageHeader());
    SectionHeader0 = addSection(createSectionHeader0());
    SectionHeader1 = addSection(createSectionHeader1());
    SectionHeader2 = addSection(createSectionHeader2());
    Detail = addSection(createDetail());
    SectionFooter1 = addSection(createSectionFooter1());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
