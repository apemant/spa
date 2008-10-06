/****license*****************************************************************
**   file: repTemeljOrigTemplate.java
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

public abstract class repTemeljOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"", "", "JDOrepTemelj", "", "", "10760", "",
     "", "", "", ""};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340.2", "567.0", "567.0", "567.0", "", "",
     "11895.66", "16834.23", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"CSKL", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"CSKL", "", "Yes", "", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  private String[] PageHeaderProps = new String[] {"", "", "880"};
  public raReportElement LabelDatumispisa;
  private String[] LabelDatumispisaProps = new String[] {"Datum ispisa :", "", "7840", "480",
     "1820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9680",
     "480", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60",
     "480", "5920", "226.8", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5920", "226.8", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "", "5920", "226.8", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"CSKL", "", "", "", "Yes", "", "Yes", "",
     "1540"};
  public raReportElement LabelTEMELJNICA;
  private String[] LabelTEMELJNICAProps = new String[] {"TEMELJNICA ", "", "", "160", "10740",
     "380", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center"};
  public raReportElement LabelSkladiste;
  private String[] LabelSkladisteProps = new String[] {"Skladište", "", "80", "980", "1134.0",
     "283.5", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", ""};
  public raReportElement TextCSKL;
  private String[] TextCSKLProps = new String[] {"CSKL", "", "", "", "", "", "", "", "1220", "980",
     "850.5", "283.5", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement TextNAZSKL;
  private String[] TextNAZSKLProps = new String[] {"NAZSKL", "", "", "", "", "", "", "", "2080",
     "980", "5670.0", "283.5", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "", ""};
  public raReportElement TextRANGE;
  private String[] TextRANGEProps = new String[] {"RANGE", "", "", "", "", "", "", "", "", "520",
     "10760", "", "", "", "", "", "", "", "Lucida Bright", "", "", "", "", "Center", ""};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"CSKL", "", "", "", "Yes", "", "", "Yes",
     "680"};
  public raReportElement LabelKonto;
  private String[] LabelKontoProps = new String[] {"Konto", "", "0.0", "380", "1360.8", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNazivkonta;
  private String[] LabelNazivkontaProps = new String[] {"Naziv konta", "", "1380", "380", "5480",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPotrazuje;
  private String[] LabelPotrazujeProps = new String[] {"Potražuje", "", "8820", "380", "1920",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDuguje;
  private String[] LabelDugujeProps = new String[] {"Duguje", "", "6880", "380", "1920", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "300"};
  public raReportElement TextIZNOSPOT;
  private String[] TextIZNOSPOTProps = new String[] {"IZNOSPOT", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8820", "", "1920", "220", "", "", "",
     "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextIZNOSDUG;
  private String[] TextIZNOSDUGProps = new String[] {"IZNOSDUG", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6880", "", "1920", "220", "", "", "",
     "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextNAZIVKONTA;
  private String[] TextNAZIVKONTAProps = new String[] {"NAZIVKONTA", "", "", "", "", "", "", "",
     "1380", "", "5480", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextBROJKONTA;
  private String[] TextBROJKONTAProps = new String[] {"BROJKONTA", "", "", "", "", "", "", "", "",
     "", "1360", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"CSKL", "", "", "", "Yes", "", "", "500"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IZNOSDUG\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6880", "40", "1940", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement LabelUKUP;
  private String[] LabelUKUPProps = new String[] {" U K U P N O", "", "20", "40", "6840", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"IZNOSPOT\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8820", "40", "1940", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "300"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "8120",
     "", "2700", "226.8", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;

  public repTemeljOrigTemplate() {
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

    LabelDatumispisa = sect.addModel(ep.LABEL, LabelDatumispisaProps);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    return sect;
  }

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    LabelTEMELJNICA = sect.addModel(ep.LABEL, LabelTEMELJNICAProps);
    LabelSkladiste = sect.addModel(ep.LABEL, LabelSkladisteProps);
    TextCSKL = sect.addModel(ep.TEXT, TextCSKLProps);
    TextNAZSKL = sect.addModel(ep.TEXT, TextNAZSKLProps);
    TextRANGE = sect.addModel(ep.TEXT, TextRANGEProps);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    LabelKonto = sect.addModel(ep.LABEL, LabelKontoProps);
    LabelNazivkonta = sect.addModel(ep.LABEL, LabelNazivkontaProps);
    LabelPotrazuje = sect.addModel(ep.LABEL, LabelPotrazujeProps);
    LabelDuguje = sect.addModel(ep.LABEL, LabelDugujeProps);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextIZNOSPOT = sect.addModel(ep.TEXT, TextIZNOSPOTProps);
    TextIZNOSDUG = sect.addModel(ep.TEXT, TextIZNOSDUGProps);
    TextNAZIVKONTA = sect.addModel(ep.TEXT, TextNAZIVKONTAProps);
    TextBROJKONTA = sect.addModel(ep.TEXT, TextBROJKONTAProps);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Text1 = sect.addModel(ep.TEXT, Text1Props);
    LabelUKUP = sect.addModel(ep.LABEL, LabelUKUPProps);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    return sect;
  }

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text3 = sect.addModel(ep.TEXT, Text3Props);
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
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
