/****license*****************************************************************
**   file: repRadniNalogOrigTemplate.java
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
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raReportTemplate;
import sg.com.elixir.reportwriter.xml.ModelFactory;

public abstract class repRadniNalogOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "hr",
     "JDOrepObracunRadnogNaloga", "", "", "10780", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "520", "538.65", "", "Customize",
     "11900", "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"BRDOK", "", "Yes", "Yes", "Each Value", "", ""};
//  public raReportElement Section1;
//  private String[] Section1Props = new String[] {"BRDOKRNL", "", "Yes", "", "Each Value", "", ""};
//  public raReportElement Section2;
//  private String[] Section2Props = new String[] {"VRDOK", "Descending", "Yes", "Yes", "Each Value",
//     "", ""};
//  public raReportElement Section3;
//  private String[] Section3Props = new String[] {"JEDANBROJ", "", "Yes", "", "", "", ""};
//  public raReportElement Section4;
//  private String[] Section4Props = new String[] {"NAZART", "", "", "", "", "", ""};
  public raReportSection ReportHeader;
  private String[] ReportHeaderProps = new String[] {"", "", "", "", "Yes", "", "", "0"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(create-var-cache)", "", "", "", "", "", "", "",
     "9240", "", "1540", "320", "", "", "", "", "", "Red", "", "", "Bold", "", "", "", ""};
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
//  public raReportSection SectionHeader1;
//  public raReportSection SectionHeader2;
//  public raReportSection SectionHeader3;
  public raReportSection Detail;
//  public raReportSection SectionFooter2;
  public raReportSection SectionFooter0;
  public raReportSection PageFooter;
  public raReportSection ReportFooter;
  private String[] ReportFooterProps = new String[] {"", "", "", "No", "Yes", "", "", "0"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "",
     "", "20", "40", "40", "", "", "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "",
     "2400", "100", "700", "680", "", "", "", "", "", "", "", "", "", "", "", "", ""};

  public repRadniNalogOrigTemplate() {
    createReportStructure();
    setReportProperties();
  }

  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
//    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
//    Section2 = sect.getModel(ep.SECTION + 2, Section2Props);
//    Section3 = sect.getModel(ep.SECTION + 3, Section3Props);
//    Section4 = sect.getModel(ep.SECTION + 4, Section4Props);
    return sect;
  }

  public raReportSection createReportHeader() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_HEADER), ReportHeaderProps);

    Text1 = sect.addModel(ep.TEXT, Text1Props);
    return sect;
  }

  public abstract raReportSection createPageHeader();

  public abstract raReportSection createSectionHeader0();

//  public abstract raReportSection createSectionHeader1();
//
//  public abstract raReportSection createSectionHeader2();
//
//  public abstract raReportSection createSectionHeader3();

  public abstract raReportSection createDetail();

//  public abstract raReportSection createSectionFooter2();

  public abstract raReportSection createSectionFooter0();

  public abstract raReportSection createPageFooter();

  public raReportSection createReportFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_FOOTER), ReportFooterProps);

    Text2 = sect.addModel(ep.TEXT, Text2Props);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
    return sect;
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
//    SectionHeader1 = addSection(createSectionHeader1());
//    SectionHeader2 = addSection(createSectionHeader2());
//    SectionHeader3 = addSection(createSectionHeader3());
    Detail = addSection(createDetail());
//    SectionFooter2 = addSection(createSectionFooter2());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
