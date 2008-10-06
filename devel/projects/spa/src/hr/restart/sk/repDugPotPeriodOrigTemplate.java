/****license*****************************************************************
**   file: repDugPotPeriodOrigTemplate.java
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
package hr.restart.sk;

import hr.restart.util.reports.raElixirProperties;
import hr.restart.util.reports.raElixirPropertiesInstance;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raReportTemplate;
import sg.com.elixir.reportwriter.xml.ModelFactory;

public abstract class repDugPotPeriodOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepZbirno", "",
     "", "10680", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "", "", "11880",
     "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"FirstLine", "", "Yes", "", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"FirstLine", "", "Yes", "Yes", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"FirstLine", "Before", "", "", "Yes", "",
     "Yes", "", "2500"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "20", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9620",
     "400", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "7780", "400",
     "1820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60",
     "460", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextNASLOV;
  private String[] TextNASLOVProps = new String[] {"NASLOV", "", "", "", "", "", "Yes", "", "",
     "660", "10740", "660", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center",
     ""};
  public raReportElement TextPODNASLOV;
  private String[] TextPODNASLOVProps = new String[] {"PODNASLOV", "", "", "", "", "", "Yes", "", "",
     "1100", "10740", "600", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", "Center",
     ""};
  public raReportElement TextLABELMJESTO;
  private String[] TextLABELMJESTOProps = new String[] {"LABELMJESTO", "", "",
     "", "", "", "", "Yes", "", "1500", "1000", "240", "", "", "", "", "", "",
     "Lucida Bright", "9", "Bold", "", "", "", ""};
  public raReportElement TextMJESTO;
  private String[] TextMJESTOProps = new String[] {"MJESTO", "", "", "", "", "",
     "", "Yes", "1040", "1500", "4380", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "",
     "", "", ""};
  public raReportElement TextKontoLab;
  private String[] TextKontoLabProps = new String[] {"KontoLab", "", "", "", "", "", "", "", "8660",
     "1500", "900", "240", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "", ""}; // "1720" was "1800"

  public raReportElement TextKonto;
  private String[] TextKontoProps = new String[] {"Konto", "", "", "", "", "", "Yes",
     "", "9640", "1500", "1000", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", // "1720" was "1800"
     "Right", "No"};
  
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"FirstLine", "", "", "", "Yes", "", "No",
     "Yes", "480"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "80", "10640", "0", "", "", ""};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "", "120", "780", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_partnera;
  private String[] LabelNaziv_partneraProps = new String[] {"Naziv partnera", "", "800", "120",
     "3940", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelH1;
  private String[] LabelH1Props = new String[] {"H1", "", "4760", "120", "1460", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelH2;
  private String[] LabelH2Props = new String[] {"H2", "", "6240", "120", "1460", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelH4;
  private String[] LabelH4Props = new String[] {"H4", "", "9200", "120", "1460", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelH3;
  private String[] LabelH3Props = new String[] {"H3", "", "7720", "120", "1460", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "380", "10640", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "380"};
  public raReportElement TextNAZPAR;
  private String[] TextNAZPARProps = new String[] {"NAZPAR", "", "", "Number|true|#.##0,00", "", "",
     "", "", "800", "", "3940", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "", ""};
  public raReportElement TextCOL3;
  private String[] TextCOL3Props = new String[] {"COL3", "", "", "Number|true|#.##0,00", "", "", "",
     "", "7720", "", "1460", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCOL4;
  private String[] TextCOL4Props = new String[] {"COL4", "", "", "Number|true|#.##0,00", "", "", "",
     "", "9200", "", "1460", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCOL1;
  private String[] TextCOL1Props = new String[] {"COL1", "", "", "Number|true|#.##0,00", "", "", "",
     "", "4760", "", "1460", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCOL2;
  private String[] TextCOL2Props = new String[] {"COL2", "", "", "Number|true|#.##0,00", "", "", "",
     "", "6240", "", "1460", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCPAR;
  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "", "",
     "780", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection SectionFooter1;
  private String[] SectionFooter1Props = new String[] {"FirstLine", "", "", "", "Yes", "", "Yes",
     "520"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "20", "10640", "0", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"COL4\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9200", "60", "1480", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"COL1\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "4760", "60", "1480", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"COL2\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6240", "60", "1480", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(dsum \"COL3\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7720", "60", "1480", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {" UKUPNO", "", "", "60", "4740", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "300", "10640", "0", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "420"};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "8440",
     "120", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;

  public repDugPotPeriodOrigTemplate() {
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

  public abstract raReportSection createPageHeader();

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    LabelDatum_ispisa_ = sect.addModel(ep.LABEL, LabelDatum_ispisa_Props);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextNASLOV = sect.addModel(ep.TEXT, TextNASLOVProps);
    TextPODNASLOV = sect.addModel(ep.TEXT, TextPODNASLOVProps);
    TextLABELMJESTO = sect.addModel(ep.TEXT, TextLABELMJESTOProps);
    TextMJESTO = sect.addModel(ep.TEXT, TextMJESTOProps);
    TextKontoLab = sect.addModel(ep.TEXT, TextKontoLabProps);
    TextKonto = sect.addModel(ep.TEXT, TextKontoProps);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    Line1 = sect.addModel(ep.LINE, Line1Props);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelNaziv_partnera = sect.addModel(ep.LABEL, LabelNaziv_partneraProps);
    LabelH1 = sect.addModel(ep.LABEL, LabelH1Props);
    LabelH2 = sect.addModel(ep.LABEL, LabelH2Props);
    LabelH4 = sect.addModel(ep.LABEL, LabelH4Props);
    LabelH3 = sect.addModel(ep.LABEL, LabelH3Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextNAZPAR = sect.addModel(ep.TEXT, TextNAZPARProps);
    TextCOL3 = sect.addModel(ep.TEXT, TextCOL3Props);
    TextCOL4 = sect.addModel(ep.TEXT, TextCOL4Props);
    TextCOL1 = sect.addModel(ep.TEXT, TextCOL1Props);
    TextCOL2 = sect.addModel(ep.TEXT, TextCOL2Props);
    TextCPAR = sect.addModel(ep.TEXT, TextCPARProps);
    return sect;
  }

  public raReportSection createSectionFooter1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 1), SectionFooter1Props);

    Line3 = sect.addModel(ep.LINE, Line3Props);
    Text1 = sect.addModel(ep.TEXT, Text1Props);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    LabelUKUPNO = sect.addModel(ep.LABEL, LabelUKUPNOProps);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    return sect;
  }

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text5 = sect.addModel(ep.TEXT, Text5Props);
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
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
