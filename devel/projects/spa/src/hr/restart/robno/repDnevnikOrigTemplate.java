/****license*****************************************************************
**   file: repDnevnikOrigTemplate.java
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

public abstract class repDnevnikOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepDnevnik", "",
     "", "15620", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"300", "380.2", "520", "538.65", "Landscape",
     "Customize", "11900", "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"CSKL", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"CSKL", "", "Yes", "", "", "", ""};
  public raReportSection ReportHeader;
  private String[] ReportHeaderProps = new String[] {"", "", "", "", "Yes", "", "", "0"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(create-var-cache)", "", "", "", "", "", "", "",
     "9240", "", "1540", "320", "", "", "", "", "", "Red", "", "", "Bold", "", "", "", ""};
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"CSKL", "", "", "", "Yes", "", "Yes", "",
     "2200"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "12820", "480",
     "1740", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60",
     "480", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "",
     "14560", "480", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement LabelDNEVNIK_KNJIZENJA;
  private String[] LabelDNEVNIK_KNJIZENJAProps = new String[] {"\nDNEVNIK KNJIŽENJA", "", "20",
     "780", "15640", "720", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center"};
  public raReportElement TextRANGE;
  private String[] TextRANGEProps = new String[] {"RANGE", "", "", "", "", "", "", "", "", "1460",
     "15640", "260", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Center", ""};
  public raReportElement LabelSkladiste_;
  private String[] LabelSkladiste_Props = new String[] {"Skladište :", "", "", "1780", "", "240",
     "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement TextNAZSKL;
  private String[] TextNAZSKLProps = new String[] {"NAZSKL", "", "", "", "", "", "", "", "2980",
     "1780", "7360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextCSKL;
  private String[] TextCSKLProps = new String[] {"CSKL", "", "", "", "", "", "", "", "1460", "1780",
     "1460", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"CSKL", "", "", "", "Yes", "No", "", "Yes",
     "740"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "60", "15640", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "11060", "100", "1400", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "", "100", "1480", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDokument;
  private String[] LabelDokumentProps = new String[] {"Dokument", "", "1500", "100", "", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "8220", "100", "2820",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelVrijednost;
  private String[] LabelVrijednostProps = new String[] {"Vrijednost", "", "12480", "100", "3180",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelArtikl;
  private String[] LabelArtiklProps = new String[] {"Artikl", "", "2960", "100", "5240", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "11060", "340", "1400", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "2960", "340", "1800", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIzlaz;
  private String[] LabelIzlazProps = new String[] {"Izlaz", "", "14080", "340", "1580", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelVrsta;
  private String[] LabelVrstaProps = new String[] {" Vrsta", "", "1500", "340", "740", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv", "", "4780", "340", "3420", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDatum;
  private String[] LabelDatumProps = new String[] {"Datum", "", "", "340", "1480", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelBroj;
  private String[] LabelBrojProps = new String[] {" Broj", "", "2260", "340", "680", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelUlaz;
  private String[] LabelUlazProps = new String[] {"Ulaz", "", "12480", "340", "1580", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIzlaz1;
  private String[] LabelIzlaz1Props = new String[] {"Izlaz", "", "9640", "340", "1400", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelUlaz1;
  private String[] LabelUlaz1Props = new String[] {"Ulaz", "", "8220", "340", "1400", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "580", "15640", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "No", "", "280"};
  public raReportElement TextDATDOK;
  private String[] TextDATDOKProps = new String[] {"DATDOK", "", "", "", "", "", "", "", "", "",
     "1460", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Center", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {
     "=(if (or (> [KOLUL] 0.) (< [KOLUL] 0.)) [KOLUL] \"\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8200", "", "1400", "220", "", "", "",
     "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(if (or (> [IZAD] 0.) (< [IZAD] 0.)) [IZAD] \"\")",
     "", "", "Number|false|1|309|2|2|true|3|false", "", "", "", "", "12460", "", "1580", "220", "",
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {
     "=(if (or (> [KOLIZ] 0.) (< [KOLIZ] 0.)) [KOLIZ] \"\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9620", "", "1400", "220", "", "", "",
     "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextVRDOK;
  private String[] TextVRDOKProps = new String[] {"VRDOK", "", "", "", "", "", "", "", "1480", "",
     "740", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Center", ""};
  public raReportElement TextZC;
  private String[] TextZCProps = new String[] {"ZC", "", "", "Number|false|1|309|2|2|true|3|false",
     "", "", "", "", "11040", "", "1400", "220", "", "", "", "", "", "", "Lucida Bright", "9", "",
     "", "", "Right", ""};
  public raReportElement TextBRDOK;
  private String[] TextBRDOKProps = new String[] {"BRDOK", "", "", "", "", "", "", "", "2240", "",
     "680", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextSIFRA;
  private String[] TextSIFRAProps = new String[] {"SIFRA", "", "", "", "", "", "", "", "2940", "",
     "1800", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(if (or (> [IRAZ] 0.) (< [IRAZ] 0.)) [IRAZ] \"\")",
     "", "", "Number|false|1|309|2|2|true|3|false", "", "", "", "", "14060", "", "1580", "220", "",
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "4760", "",
     "3420", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"CSKL", "", "", "", "Yes", "", "", "760"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "120", "15640", "0", "", "", ""};
  public raReportElement LabelU_K_U_P_N_O;
  private String[] LabelU_K_U_P_N_OProps = new String[] {" U K U P N O", "", "", "160", "12440",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement TextSIRAZ;
  private String[] TextSIRAZProps = new String[] {"SIRAZ", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "14060", "160", "1620", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement TextSIZAD;
  private String[] TextSIZADProps = new String[] {"SIZAD", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "12460", "160", "1600", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement LabelS_A_L_D_O;
  private String[] LabelS_A_L_D_OProps = new String[] {" S A L D O", "", "", "400", "14040", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement TextSALDO;
  private String[] TextSALDOProps = new String[] {"SALDO", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "14060", "400", "1620", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "640", "15640", "0", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "240"};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "12940",
     "", "2700", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;
  private String[] ReportFooterProps = new String[] {"", "", "", "No", "Yes", "", "", "0"};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "",
     "", "20", "1980", "380", "", "", "", "", "", "", "", "", "", "", "", "", ""};

  public repDnevnikOrigTemplate() {
    createReportStructure();
    setReportProperties();
  }

  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
    return sect;
  }

  public raReportSection createReportHeader() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_HEADER), ReportHeaderProps);

    Text1 = sect.addModel(ep.TEXT, Text1Props);
    return sect;
  }

  public abstract raReportSection createPageHeader();

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    LabelDatum_ispisa_ = sect.addModel(ep.LABEL, LabelDatum_ispisa_Props);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    LabelDNEVNIK_KNJIZENJA = sect.addModel(ep.LABEL, LabelDNEVNIK_KNJIZENJAProps);
    TextRANGE = sect.addModel(ep.TEXT, TextRANGEProps);
    LabelSkladiste_ = sect.addModel(ep.LABEL, LabelSkladiste_Props);
    TextNAZSKL = sect.addModel(ep.TEXT, TextNAZSKLProps);
    TextCSKL = sect.addModel(ep.TEXT, TextCSKLProps);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    Line1 = sect.addModel(ep.LINE, Line1Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    LabelDokument = sect.addModel(ep.LABEL, LabelDokumentProps);
    LabelKolicina = sect.addModel(ep.LABEL, LabelKolicinaProps);
    LabelVrijednost = sect.addModel(ep.LABEL, LabelVrijednostProps);
    LabelArtikl = sect.addModel(ep.LABEL, LabelArtiklProps);
    LabelCijena = sect.addModel(ep.LABEL, LabelCijenaProps);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelIzlaz = sect.addModel(ep.LABEL, LabelIzlazProps);
    LabelVrsta = sect.addModel(ep.LABEL, LabelVrstaProps);
    LabelNaziv = sect.addModel(ep.LABEL, LabelNazivProps);
    LabelDatum = sect.addModel(ep.LABEL, LabelDatumProps);
    LabelBroj = sect.addModel(ep.LABEL, LabelBrojProps);
    LabelUlaz = sect.addModel(ep.LABEL, LabelUlazProps);
    LabelIzlaz1 = sect.addModel(ep.LABEL, LabelIzlaz1Props);
    LabelUlaz1 = sect.addModel(ep.LABEL, LabelUlaz1Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextDATDOK = sect.addModel(ep.TEXT, TextDATDOKProps);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    TextVRDOK = sect.addModel(ep.TEXT, TextVRDOKProps);
    TextZC = sect.addModel(ep.TEXT, TextZCProps);
    TextBRDOK = sect.addModel(ep.TEXT, TextBRDOKProps);
    TextSIFRA = sect.addModel(ep.TEXT, TextSIFRAProps);
    Text5 = sect.addModel(ep.TEXT, Text5Props);
    TextNAZART = sect.addModel(ep.TEXT, TextNAZARTProps);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Line3 = sect.addModel(ep.LINE, Line3Props);
    LabelU_K_U_P_N_O = sect.addModel(ep.LABEL, LabelU_K_U_P_N_OProps);
    TextSIRAZ = sect.addModel(ep.TEXT, TextSIRAZProps);
    TextSIZAD = sect.addModel(ep.TEXT, TextSIZADProps);
    LabelS_A_L_D_O = sect.addModel(ep.LABEL, LabelS_A_L_D_OProps);
    TextSALDO = sect.addModel(ep.TEXT, TextSALDOProps);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    return sect;
  }

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
    SectionHeader0 = addSection(createSectionHeader0());
    SectionHeader1 = addSection(createSectionHeader1());
    Detail = addSection(createDetail());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
