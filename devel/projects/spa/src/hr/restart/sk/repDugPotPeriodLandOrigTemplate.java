/****license*****************************************************************
**   file: repDugPotPeriodLandOrigTemplate.java
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

public abstract class repDugPotPeriodLandOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepZbirno", "",
     "", "15620", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "Landscape", "",
     "11880", "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"FirstLine", "", "Yes", "", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"FirstLine", "", "Yes", "Yes", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"FirstLine", "Before", "", "", "Yes", "",
     "Yes", "", "2300"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "20", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "",
     "14440", "400", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "12600", "400",
     "1820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60",
     "460", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextNASLOV;
  private String[] TextNASLOVProps = new String[] {"NASLOV", "", "", "", "", "", "Yes", "", "",
     "660", "15560", "660", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center",
     ""};
  public raReportElement TextPODNASLOV;
  private String[] TextPODNASLOVProps = new String[] {"PODNASLOV", "", "", "", "", "", "Yes", "", "",
     "1100", "15560", "600", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", "Center",
     ""};
  public raReportElement TextLABELMJESTO;
  private String[] TextLABELMJESTOProps = new String[] {"LABELMJESTO", "", "",
     "", "", "", "", "Yes", "20", "1500", "1000", "240", "", "", "", "", "", "",
     "Lucida Bright", "9", "Bold", "", "", "", ""};
  public raReportElement TextMJESTO;
  private String[] TextMJESTOProps = new String[] {"MJESTO", "", "", "", "", "",
     "", "Yes", "1040", "1500", "4380", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "",
     "", "", ""};
  public raReportElement TextKontoLab;
  private String[] TextKontoLabProps = new String[] {"KontoLab", "", "", "", "", "", "", "", "13640",
     "1500", "900", "240", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "", ""}; // "1720" was "1800"

  public raReportElement TextKonto;
  private String[] TextKontoProps = new String[] {"Konto", "", "", "", "", "", "Yes",
     "", "14560", "1500", "1000", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", // "1720" was "1800"
     "Right", "No"};
  
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"FirstLine", "", "", "", "Yes", "", "No",
     "Yes", "480"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "100", "15560", "0", "", "", ""};
  public raReportElement LabelH1;
  private String[] LabelH1Props = new String[] {"H1", "", "5660", "140", "1400", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelH2;
  private String[] LabelH2Props = new String[] {"H2", "", "7080", "140", "1400", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelH3;
  private String[] LabelH3Props = new String[] {"H3", "", "8500", "140", "1400", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "", "140", "840", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelH6;
  private String[] LabelH6Props = new String[] {"H6", "", "12760", "140", "1400", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelH7;
  private String[] LabelH7Props = new String[] {"H7", "", "14180", "140", "1400", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_partnera;
  private String[] LabelNaziv_partneraProps = new String[] {"Naziv partnera", "", "860", "140",
     "4780", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelH5;
  private String[] LabelH5Props = new String[] {"H5", "", "11340", "140", "1400", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelH4;
  private String[] LabelH4Props = new String[] {"H4", "", "9920", "140", "1400", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "400", "15560", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "380"};
  public raReportElement TextNAZPAR;
  private String[] TextNAZPARProps = new String[] {"NAZPAR", "", "", "Number|true|#.##0,00", "", "",
     "", "", "860", "", "4780", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "", ""};
  public raReportElement TextCOL3;
  private String[] TextCOL3Props = new String[] {"COL3", "", "", "Number|true|#.##0,00", "", "", "",
     "", "8500", "", "1400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCOL4;
  private String[] TextCOL4Props = new String[] {"COL4", "", "", "Number|true|#.##0,00", "", "", "",
     "", "9920", "", "1400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCOL6;
  private String[] TextCOL6Props = new String[] {"COL6", "", "", "Number|true|#.##0,00", "", "", "",
     "", "12760", "", "1400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCOL7;
  private String[] TextCOL7Props = new String[] {"COL7", "", "", "Number|true|#.##0,00", "", "", "",
     "", "14180", "", "1400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCOL5;
  private String[] TextCOL5Props = new String[] {"COL5", "", "", "Number|true|#.##0,00", "", "", "",
     "", "11340", "", "1400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCOL1;
  private String[] TextCOL1Props = new String[] {"COL1", "", "", "Number|true|#.##0,00", "", "", "",
     "", "5660", "", "1400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCOL2;
  private String[] TextCOL2Props = new String[] {"COL2", "", "", "Number|true|#.##0,00", "", "", "",
     "", "7080", "", "1400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCPAR;
  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "", "",
     "840", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection SectionFooter1;
  private String[] SectionFooter1Props = new String[] {"FirstLine", "", "", "", "Yes", "", "Yes",
     "1240"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "20", "15560", "0", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"COL6\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "12760", "60", "1420", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"COL5\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "11340", "60", "1420", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"COL3\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8500", "60", "1420", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(dsum \"COL2\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7080", "60", "1420", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(dsum \"COL1\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5660", "60", "1420", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {"=(dsum \"COL4\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9920", "60", "1420", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text8;
  private String[] Text8Props = new String[] {"=(dsum \"COL7\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "14180", "60", "1420", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {" UKUPNO", "", "", "60", "5580", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "300", "15560", "0", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "240"};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "13300",
     "", "2240", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;

  public repDugPotPeriodLandOrigTemplate() {
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
    LabelH1 = sect.addModel(ep.LABEL, LabelH1Props);
    LabelH2 = sect.addModel(ep.LABEL, LabelH2Props);
    LabelH3 = sect.addModel(ep.LABEL, LabelH3Props);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelH6 = sect.addModel(ep.LABEL, LabelH6Props);
    LabelNaziv_partnera = sect.addModel(ep.LABEL, LabelNaziv_partneraProps);
    LabelH5 = sect.addModel(ep.LABEL, LabelH5Props);
    LabelH4 = sect.addModel(ep.LABEL, LabelH4Props);
    LabelH7 = sect.addModel(ep.LABEL, LabelH7Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextNAZPAR = sect.addModel(ep.TEXT, TextNAZPARProps);
    TextCOL3 = sect.addModel(ep.TEXT, TextCOL3Props);
    TextCOL4 = sect.addModel(ep.TEXT, TextCOL4Props);
    TextCOL6 = sect.addModel(ep.TEXT, TextCOL6Props);
    TextCOL7 = sect.addModel(ep.TEXT, TextCOL7Props);
    TextCOL5 = sect.addModel(ep.TEXT, TextCOL5Props);
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
    Text5 = sect.addModel(ep.TEXT, Text5Props);
    Text6 = sect.addModel(ep.TEXT, Text6Props);
    Text8 = sect.addModel(ep.TEXT, Text8Props);
    LabelUKUPNO = sect.addModel(ep.LABEL, LabelUKUPNOProps);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    return sect;
  }

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text7 = sect.addModel(ep.TEXT, Text7Props);
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
