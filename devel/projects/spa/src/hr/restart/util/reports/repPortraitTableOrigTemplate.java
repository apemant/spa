/****license*****************************************************************
**   file: repPortraitTableOrigTemplate.java
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
package hr.restart.util.reports;

import sg.com.elixir.reportwriter.xml.ModelFactory;

public abstract class repPortraitTableOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"", "", "JDOrepDynamicProvider", "", "",
     "10740", "", "", "", "", ""};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340.2", "380.2", "538.65", "538.65", "", "", "11880",
     "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"Logo", "", "Yes", "Yes", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"Logo", "", "Yes", "Yes", "Each Value", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  private String[] PageHeaderProps = new String[] {"", "", "920"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60",
     "480", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9600",
     "480", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(if (> [Logo] 0) \"Datum ispisa :\" \"\")", "", "",
     "", "", "", "", "", "7760", "480", "1820", "220", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "", ""};
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"Logo", "", "", "", "Yes", "No", "No", "No",
     "1100"};
  public raReportElement TextTitle;
  private String[] TextTitleProps = new String[] {"Title", "", "", "", "", "", "Yes", "", "", "",
     "10720", "760", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center", ""};
  public raReportElement TextSubTitle;
  private String[] TextSubTitleProps = new String[] {"SubTitle", "", "", "", "", "", "", "", "",
     "440", "10720", "600", "", "", "", "", "", "", "Lucida Bright", "11", "Bold", "", "", "Center",
     ""};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"Logo", "", "", "", "Yes", "No", "No",
    "Yes", "460"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "80", "0", "0", "", "", ""};
  public raReportElement TextHeaderValue0;
  private String[] TextHeaderValue0Props = new String[] {"HeaderValue0", "", "", "", "", "", "", "",
    "", "120", "1420", "240", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold",
    "", "", "Center", "No"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "380", "0", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "500"};
  public raReportElement TextDataValue0;
  private String[] TextDataValue0Props = new String[] {"DataValue0", "", "", "", "", "", "", "", "",
     "", "1420", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection SectionFooter1;
  private String[] SectionFooter1Props = new String[] {"Logo", "", "", "", "Yes", "No", "Yes",
     "500"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "20", "0", "0", "", "", ""};
  public raReportElement TextSumValue0;
  private String[] TextSumValue0Props = new String[] {"SumValue0", "", "", "", "", "", "", "", "",
     "60", "1420", "240", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Right", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "320", "0", "0", "", "", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"Logo", "", "", "", "Yes", "No", "Yes",
     ""};
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "", "20", "0", "0", "", "", ""};
  public raReportElement TextTSumValue0;
  private String[] TextTSumValue0Props = new String[] {"SumValue0", "", "", "", "", "", "", "", "",
     "60", "1420", "240", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Right", ""};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "", "320", "0", "0", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "480"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {
     "=(if (> [Logo] 0) (string-append \"Stranica \" (page) \" od \" (pages)) \"\")", "", "", "",
     "", "", "", "", "8000", "200", "2700", "220", "", "", "", "", "", "", "Lucida Bright", "8", "",
     "", "", "Right", ""};
  public raReportSection ReportFooter;

  public repPortraitTableOrigTemplate() {
    createReportStructure();
    setReportProperties();
  }

  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
    return sect;
  }

  public abstract raReportSection createReportHeader();

  public raReportSection createPageHeader() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_HEADER), PageHeaderProps);

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    Text1 = sect.addModel(ep.TEXT, Text1Props);
    return sect;
  }

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    TextTitle = sect.addModel(ep.TEXT, TextTitleProps);
    TextSubTitle = sect.addModel(ep.TEXT, TextSubTitleProps);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    Line1 = sect.addModel(ep.LINE, Line1Props);
    TextHeaderValue0 = sect.addModel(ep.TEXT, TextHeaderValue0Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextDataValue0 = sect.addModel(ep.TEXT, TextDataValue0Props);
    return sect;
  }

  public raReportSection createSectionFooter1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 1), SectionFooter1Props);

    Line3 = sect.addModel(ep.LINE, Line3Props);
    TextSumValue0 = sect.addModel(ep.TEXT, TextSumValue0Props);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Line5 = sect.addModel(ep.LINE, Line5Props);
    TextTSumValue0 = sect.addModel(ep.TEXT, TextTSumValue0Props);
    Line6 = sect.addModel(ep.LINE, Line6Props);
    return sect;
  }

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text2 = sect.addModel(ep.TEXT, Text2Props);
    return sect;
  }

  public abstract raReportSection createReportFooter();

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
    Detail = addSection(createDetail());
    SectionFooter1 = addSection(createSectionFooter1());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
