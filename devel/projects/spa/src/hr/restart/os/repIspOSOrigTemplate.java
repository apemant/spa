/****license*****************************************************************
**   file: repIspOSOrigTemplate.java
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

import hr.restart.util.reports.raElixirProperties;
import hr.restart.util.reports.raElixirPropertiesInstance;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raReportTemplate;
import sg.com.elixir.reportwriter.xml.ModelFactory;

public abstract class repIspOSOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepIspOS", "", "", 
     "15600", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "Landscape", "", 
     "11880", "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"FirstLine", "", "", "Yes", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"COrg", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"OblikListe", "", "", "", "", "", ""};
  public raReportSection ReportHeader;
  private String[] ReportHeaderProps = new String[] {"", "", "", "", "Yes", "Yes", "", "2840"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "", 
     "", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "", "", 
     "240", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", 
     "14460", "420", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", ""};
  public raReportElement LabelDatum_ispisa;
  private String[] LabelDatum_ispisaProps = new String[] {"Datum ispisa ", "", "12620", "420", 
     "1820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "", 
     "480", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextCaption;
  private String[] TextCaptionProps = new String[] {"Caption", "", "", "", "", "", "", "", "", 
     "840", "15680", "420", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center", 
     ""};
  public raReportElement TextCaption2;
  private String[] TextCaption2Props = new String[] {"Caption2", "", "", "", "", "", "", "", "", 
     "1220", "15680", "340", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "Center", 
     ""};
  public raReportElement Textcaption3;
  private String[] Textcaption3Props = new String[] {"caption3", "", "", "", "", "", "", "", "", 
     "1560", "15680", "340", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "Center", 
     ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(put-var \"temp2\" \"\")", "", "", "", "No", "", "", 
     "", "9360", "1960", "1820", "240", "", "", "", "", "", "", "Lucida Bright", "6", "", "", "", 
     "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(put-var \"temp3\" \"\")", "", "", "", "No", "", "", 
     "", "11200", "1960", "1820", "240", "", "", "", "", "", "", "Lucida Bright", "6", "", "", "", 
     "", ""};
  public raReportElement TextStatus;
  private String[] TextStatusProps = new String[] {"Status", "", "", "", "", "", "", "", "1060", 
     "1960", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelStatus;
  private String[] LabelStatusProps = new String[] {"Status", "", "", "1960", "1020", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(create-var-cache)", "", "", "", "No", "", "", "", 
     "3360", "1960", "1680", "240", "", "", "", "", "", "Red", "Lucida Bright", "6", "Bold", "", "", 
     "", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(put-var \"row\" 0)", "", "", "", "No", "", "", "", 
     "5060", "1960", "1560", "240", "", "", "", "", "", "", "Lucida Bright", "6", "", "", "", "", 
     ""};
  public raReportElement TextRowNum;
  private String[] TextRowNumProps = new String[] {"RowNum", "", "", "", "No", "", "", "", "6640", 
     "1960", "860", "240", "", "", "", "", "", "", "Lucida Bright", "6", "", "", "", "", ""};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(put-var \"temp1\" \"\")", "", "", "", "No", "", "", 
     "", "7520", "1960", "1820", "240", "", "", "", "", "", "", "Lucida Bright", "6", "", "", "", 
     "", ""};
  public raReportElement TextPorijeklo;
  private String[] TextPorijekloProps = new String[] {"Porijeklo", "", "", "", "", "", "", "", 
     "1060", "2220", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportElement LabelPorijeklo;
  private String[] LabelPorijekloProps = new String[] {"Porijeklo", "", "", "2220", "1020", "240", 
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement LabelAktivnost;
  private String[] LabelAktivnostProps = new String[] {"Aktivnost", "", "", "2480", "1020", "240", 
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextAktivnost;
  private String[] TextAktivnostProps = new String[] {"Aktivnost", "", "", "", "", "", "", "", 
     "1060", "2480", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportSection PageHeader;
  public raReportSection SectionHeader1;
  public raReportSection Detail;
  public raReportSection SectionFooter1;
  public raReportSection SectionFooter0;
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "240"};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "13360", 
     "", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;
  private String[] ReportFooterProps = new String[] {"", "", "", "", "Yes", "", "", "0"};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "", 
     "4060", "360", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};

  public repIspOSOrigTemplate() {
    createReportStructure();
    setReportProperties();
  }

  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
    Section2 = sect.getModel(ep.SECTION + 2, Section2Props);
    return sect;
  }

  public raReportSection createReportHeader() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_HEADER), ReportHeaderProps);

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    LabelDatum_ispisa = sect.addModel(ep.LABEL, LabelDatum_ispisaProps);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextCaption = sect.addModel(ep.TEXT, TextCaptionProps);
    TextCaption2 = sect.addModel(ep.TEXT, TextCaption2Props);
    Textcaption3 = sect.addModel(ep.TEXT, Textcaption3Props);
    Text1 = sect.addModel(ep.TEXT, Text1Props);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    TextStatus = sect.addModel(ep.TEXT, TextStatusProps);
    LabelStatus = sect.addModel(ep.LABEL, LabelStatusProps);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    TextRowNum = sect.addModel(ep.TEXT, TextRowNumProps);
    Text5 = sect.addModel(ep.TEXT, Text5Props);
    TextPorijeklo = sect.addModel(ep.TEXT, TextPorijekloProps);
    LabelPorijeklo = sect.addModel(ep.LABEL, LabelPorijekloProps);
    LabelAktivnost = sect.addModel(ep.LABEL, LabelAktivnostProps);
    TextAktivnost = sect.addModel(ep.TEXT, TextAktivnostProps);
    return sect;
  }

  public abstract raReportSection createPageHeader();

  public abstract raReportSection createSectionHeader1();

  public abstract raReportSection createDetail();

  public abstract raReportSection createSectionFooter1();

  public abstract raReportSection createSectionFooter0();

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text6 = sect.addModel(ep.TEXT, Text6Props);
    return sect;
  }

  public raReportSection createReportFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_FOOTER), ReportFooterProps);

    Text7 = sect.addModel(ep.TEXT, Text7Props);
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
    SectionHeader1 = addSection(createSectionHeader1());
    Detail = addSection(createDetail());
    SectionFooter1 = addSection(createSectionFooter1());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
