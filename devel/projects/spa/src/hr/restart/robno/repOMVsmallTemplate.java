/****license*****************************************************************
**   file: repOMVsmallTemplate.java
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

public abstract class repOMVsmallTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepOtpisRobe", "",
     "", "10680", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "", "", "11880",
     "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"BRDOK", "", "Yes", "", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"BRDOK", "", "Yes", "", "", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"RBR", "", "", "", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"BRDOK", "Before", "", "", "Yes", "", "Yes",
     "", "2080"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextFirstLine1;
  private String[] TextFirstLine1Props = new String[] {"FirstLine", "", "", "", "", "", "", "",
     "60", "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9620",
     "180", "1120", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "8160", "180",
     "1460", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement TextSecondLine1;
  private String[] TextSecondLine1Props = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60",
     "480", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextThirdLine1;
  private String[] TextThirdLine1Props = new String[] {"ThirdLine", "", "", "", "", "", "", "",
     "60", "480", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement LabelOTPIS_ROBE;
  private String[] LabelOTPIS_ROBEProps = new String[] {"\nOTPIS ROBE", "", "", "640", "10740",
     "640", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center"};
  public raReportElement TextFormatBroj;
  private String[] TextFormatBrojProps = new String[] {"FormatBroj", "", "", "", "", "", "", "", "",
     "1260", "10740", "280", "", "", "", "", "", "", "Lucida Bright", "11", "Bold", "", "",
     "Center", ""};
  public raReportElement LabelSkladiste;
  private String[] LabelSkladisteProps = new String[] {"Skladište", "", "", "1740", "960", "200",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextCSKL;
  private String[] TextCSKLProps = new String[] {"CSKL", "", "", "", "", "", "", "", "960", "1740",
     "840", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelDatum;
  private String[] LabelDatumProps = new String[] {"Datum", "", "8940", "1740", "680", "200", "",
     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZSKL;
  private String[] TextNAZSKLProps = new String[] {"NAZSKL", "", "", "", "", "", "", "", "1820",
     "1740", "4520", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextDATDOK;
  private String[] TextDATDOKProps = new String[] {"DATDOK", "", "", "", "", "", "", "", "9660",
     "1740", "1060", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "Yes",
     "420"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "8380", "100", "2340",
     "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelJm;
  private String[] LabelJmProps = new String[] {"Jm", "", "7760", "100", "600", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv", "", "2160", "100", "5580", "240",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "420", "100", "1720", "240",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelRb;
  private String[] LabelRbProps = new String[] {"Rb", "", "", "100", "400", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "840"};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "440", "",
     "1700", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "7760", "", "600",
     "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", ""};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "",
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "8420", "", "2300", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "2180", "",
     "5540", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "400",
     "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection SectionFooter0;
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "240"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "8020",
     "", "2700", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;

  public repOMVsmallTemplate() {
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

  public abstract raReportSection createReportHeader();

  public abstract raReportSection createPageHeader();

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextFirstLine1 = sect.addModel(ep.TEXT, TextFirstLine1Props);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    LabelDatum_ispisa_ = sect.addModel(ep.LABEL, LabelDatum_ispisa_Props);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    TextSecondLine1 = sect.addModel(ep.TEXT, TextSecondLine1Props);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextThirdLine1 = sect.addModel(ep.TEXT, TextThirdLine1Props);
    LabelOTPIS_ROBE = sect.addModel(ep.LABEL, LabelOTPIS_ROBEProps);
    TextFormatBroj = sect.addModel(ep.TEXT, TextFormatBrojProps);
    LabelSkladiste = sect.addModel(ep.LABEL, LabelSkladisteProps);
    TextCSKL = sect.addModel(ep.TEXT, TextCSKLProps);
    LabelDatum = sect.addModel(ep.LABEL, LabelDatumProps);
    TextNAZSKL = sect.addModel(ep.TEXT, TextNAZSKLProps);
    TextDATDOK = sect.addModel(ep.TEXT, TextDATDOKProps);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    LabelKolicina = sect.addModel(ep.LABEL, LabelKolicinaProps);
    LabelJm = sect.addModel(ep.LABEL, LabelJmProps);
    LabelNaziv = sect.addModel(ep.LABEL, LabelNazivProps);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelRb = sect.addModel(ep.LABEL, LabelRbProps);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextCART = sect.addModel(ep.TEXT, TextCARTProps);
    TextJM = sect.addModel(ep.TEXT, TextJMProps);
    TextKOL = sect.addModel(ep.TEXT, TextKOLProps);
    TextNAZART = sect.addModel(ep.TEXT, TextNAZARTProps);
    TextRBR = sect.addModel(ep.TEXT, TextRBRProps);
    return sect;
  }

//  public abstract raReportSection createSectionFooter0();

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);
    Text1 = sect.addModel(ep.TEXT, Text1Props);
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
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
