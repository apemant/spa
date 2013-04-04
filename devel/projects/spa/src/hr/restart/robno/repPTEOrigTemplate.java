/****license*****************************************************************
**   file: repPTEOrigTemplate.java
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
import hr.restart.util.reports.raNewDefaultPageHeader;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raReportTemplate;
import sg.com.elixir.reportwriter.xml.ModelFactory;

public abstract class repPTEOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "hr", 
     "JDOrepPovratnicaTerecenje", "", "", "10620", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"300", "380.2", "520", "538.65", "", "Customize", 
     "11900", "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"BRDOK", "", "Yes", "Yes", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"BRDOK", "", "Yes", "", "Each Value", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"RBR", "", "", "", "Each Value", "", ""};
  public raReportSection ReportHeader;
  private String[] ReportHeaderProps = new String[] {"", "", "", "", "Yes", "", "", "0"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(create-var-cache)", "", "", "", "", "", "", "", 
     "9240", "", "1540", "320", "", "", "", "", "", "Red", "", "", "Bold", "", "", "", ""};
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"BRDOK", "Before", "", "", "Yes", "No", "", 
     "", "2700"};
  public raReportElement LabelPOVRATNICATERECENJE;
  private String[] LabelPOVRATNICATERECENJEProps = new String[] {"\nPOVRATNICA-TERE\u0106ENJE", "", 
     "7020", "", "4100", "620", "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", 
     "Center"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"NAZPARL", "", "", "", "", 
     "", "Yes", "Yes", "640", "200", "4360", "840", "", "", "", "", "", "", "Lucida Bright", "", 
     "Bold", "", "", "", ""};
  public raReportElement Rectangle1;
  private String[] Rectangle1Props = new String[] {"", "660", "240", "4160", "2180", "", "", "", 
     "White", ""};
  public raReportElement Rectangle2;
  private String[] Rectangle2Props = new String[] {"", "320", "260", "4840", "2160", "Transparent", 
     "", "", "", ""};
  public raReportElement Rectangle3;
  private String[] Rectangle3Props = new String[] {"", "300", "600", "4860", "1500", "", "", "", 
     "White", ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "10800", "620", "0", "320", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "7240", "620", "0", "320", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "7260", "620", "3540", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "7280", "660", "3500", "260", "Normal", 
     "Light Gray", "Solid", "Light Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement TextFormatBroj;
  private String[] TextFormatBrojProps = new String[] {"FormatBroj", "", "", "", "", "", "", "", 
     "7280", "680", "3500", "220", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", 
     "Center", "No"};
  public raReportElement LabelBroj;
  private String[] LabelBrojProps = new String[] {"Broj", "", "5760", "700", "", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "7260", "940", "3540", "0", "", "", ""};
  public raReportElement TextADR;
  private String[] TextADRProps = new String[] {"ADR", "", "", "", "", "", "", "", "640", "1120", 
     "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextMJ;
  private String[] TextMJProps = new String[] {"MJ", "", "", "", "", "", "", "", "640", "1380", 
     "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement LabelMjesto;
  private String[] LabelMjestoProps = new String[] {"Mjesto/datum izdavanja", "", "5440", "1460", "1760", "220", "", 
     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextLogoMjestoZarez;
  private String[] TextLogoMjestoZarezProps = new String[] {"LogoMjestoZarez", "", "", "", "", "", 
     "", "", "7280", "1460", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", 
     "", "", ""};
  public raReportElement TextSgetDATDOK;
  private String[] TextSgetDATDOKProps = new String[] {"SgetDATDOK", "", "hr_HR", 
     "Date|false|Short", "", "", "", "", "9600", "1460", "1240", "220", "", "", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextKONTOSOB;
  private String[] TextKONTOSOBProps = new String[] {"KONTOSOB", "", "", "", "", "", "No", "", "640", 
     "1640", "4360", "480", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextDATDOKUL;
  private String[] TextDATDOKULProps = new String[] {"DATDOKUL", "", "", "", "", "", "", "", "9600", 
     "1700", "1240", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextBRDOKUL;
  private String[] TextBRDOKULProps = new String[] {"BRDOKUL", "", "", "", "", "", "", "", "7280", 
     "1700", "2300", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelUlazni_dokument;
  private String[] LabelUlazni_dokumentProps = new String[] {"Ulazni dokument", "", "5440", "1700", 
     "1760", "200", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextBRRAC;
  private String[] TextBRRACProps = new String[] {"BRRAC", "", "", "", "", "", "", "", "7280", 
     "1920", "2300", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextDATRAC;
  private String[] TextDATRACProps = new String[] {"DATRAC", "", "", "", "", "", "", "", "9600", 
     "1920", "1240", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelRacun;
  private String[] LabelRacunProps = new String[] {"Ra\u010Dun", "", "5440", "1920", "1760", "200", 
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextCPAR;
  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "3940", "2120", 
     "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextMB;
  private String[] TextMBProps = new String[] {"MB", "", "", "", "", "", "", "", "640", "2120", 
     "3200", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", 
     "680"};
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "", "60", "10800", "0", "", "", ""};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv", "", "1800", "100", "4600", "460", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "480", "100", "1300", "460", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "6420", "100", "960", 
     "460", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "100", "460", "460", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIznos;
  private String[] LabelIznosProps = new String[] {"Iznos", "", "9120", "100", "1700", "460", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "7400", "100", "460", "460", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "7880", "100", "1220", "460", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "", "580", "10800", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "300"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {
     "=(put-var \"H2\" (+ (get-var \"H2\")\n(if (= [ImaPor2] 0) 0 [IPRODBP]))\n                           )\n)\n", 
     "", "", "", "No", "", "", "", "1820", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "7400", "", "460", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", "No"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "6420", "", "960", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNC;
  private String[] TextNCProps = new String[] {"NC", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "7880", "", "1220", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "440", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(if (= [ImaPor2] 1) (put-var \"tpor2\" 1) \"\")", 
     "", "", "", "No", "", "", "", "2640", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(if (= [ImaPor3] 1) (put-var \"tpor3\" 1) \"\")", 
     "", "", "", "No", "", "", "", "3120", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {"=(if (= [ImaPor1] 1) (put-var \"tpor1\" 1) \"\")", 
     "", "", "", "No", "", "", "", "2300", "", "0", "0", "", "", "", "", "", "", "", "", "", "", "", 
     "", ""};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {
     "=(put-var \"H1\" (+ (get-var \"H1\")\n(if (= [ImaPor1] 0) 0. [IPRODBP]))\n                           )\n)\n", 
     "", "", "", "No", "", "", "", "780", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement TextINAB;
  private String[] TextINABProps = new String[] {"INAB", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "", "1700", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "", 
     "1300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text8;
  private String[] Text8Props = new String[] {
     "=(put-var \"H3\" (+ (get-var \"H3\")\n(if (= [ImaPor3] 0) 0. [IPRODBP])) ))\n", "", "", "", 
     "No", "", "", "", "1280", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", "", "", "", 
     ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "1800", 
     "", "4600", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"BRDOK", "After", "", "", "Yes", "", "Yes", 
     "4260"};
  public raReportElement Line7;
  private String[] Line7Props = new String[] {"", "", "", "60", "10800", "0", "", "", ""};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "9040", "120", "1780", "820", "Normal", 
     "Light Gray", "Solid", "Light Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Text9;
  private String[] Text9Props = new String[] {"=(dsum \"INAB\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "140", "1700", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", "No"};
  public raReportElement LabelUkupno;
  private String[] LabelUkupnoProps = new String[] {"Ukupno", "", "6640", "140", "2400", "240", "", 
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement TextPRPOR;
  private String[] TextPRPORProps = new String[] {"PRPOR", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "400", "1700", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", "No"};
  public raReportElement LabelPretporez;
  private String[] LabelPretporezProps = new String[] {"Pretporez", "", "6640", "400", "2400", 
     "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement LabelSveukupno;
  private String[] LabelSveukupnoProps = new String[] {"Sveukupno", "", "6640", "680", "2400", 
     "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement TextUKUPNO;
  private String[] TextUKUPNOProps = new String[] {"UKUPNO", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "680", "1700", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", "No"};
  public raReportElement Line8;
  private String[] Line8Props = new String[] {"", "", "6660", "960", "4160", "0", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "380"};
  public raReportElement Line9;
  private String[] Line9Props = new String[] {"", "", "20", "40", "10800", "0", "", "Light Gray", 
     ""};
  public raReportElement Text10;
  private String[] Text10Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "8120", 
     "120", "2700", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;
  private String[] ReportFooterProps = new String[] {"", "", "", "No", "Yes", "", "", "0"};
  public raReportElement Text11;
  private String[] Text11Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "", 
     "", "20", "1980", "380", "", "", "", "", "", "", "", "", "", "", "", "", ""};

  public repPTEOrigTemplate() {
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

    Text1 = sect.addModel(ep.TEXT, Text1Props);
    return sect;
  }

  public raReportSection createPageHeader() {
    return new raNewDefaultPageHeader(this); // return new raIzlazPageHeader(this);
  }

//  public abstract raReportSection createPageHeader();

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    LabelPOVRATNICATERECENJE = sect.addModel(ep.LABEL, LabelPOVRATNICATERECENJEProps);
    Rectangle2 = sect.addModel(ep.RECTANGLE, Rectangle2Props);
    Rectangle1 = sect.addModel(ep.RECTANGLE, Rectangle1Props);
    Rectangle3 = sect.addModel(ep.RECTANGLE, Rectangle3Props);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    TextFormatBroj = sect.addModel(ep.TEXT, TextFormatBrojProps);
    LabelBroj = sect.addModel(ep.LABEL, LabelBrojProps);
    TextADR = sect.addModel(ep.TEXT, TextADRProps);
    TextMJ = sect.addModel(ep.TEXT, TextMJProps);
    LabelMjesto = sect.addModel(ep.LABEL, LabelMjestoProps);
    TextLogoMjestoZarez = sect.addModel(ep.TEXT, TextLogoMjestoZarezProps);
    TextSgetDATDOK = sect.addModel(ep.TEXT, TextSgetDATDOKProps);
    TextKONTOSOB = sect.addModel(ep.TEXT, TextKONTOSOBProps);
    TextDATDOKUL = sect.addModel(ep.TEXT, TextDATDOKULProps);
    TextBRDOKUL = sect.addModel(ep.TEXT, TextBRDOKULProps);
    LabelUlazni_dokument = sect.addModel(ep.LABEL, LabelUlazni_dokumentProps);
    TextBRRAC = sect.addModel(ep.TEXT, TextBRRACProps);
    TextDATRAC = sect.addModel(ep.TEXT, TextDATRACProps);
    LabelRacun = sect.addModel(ep.LABEL, LabelRacunProps);
    TextCPAR = sect.addModel(ep.TEXT, TextCPARProps);
    TextMB = sect.addModel(ep.TEXT, TextMBProps);
    Line1 = sect.addModel(ep.LINE, Line1Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    Line3 = sect.addModel(ep.LINE, Line3Props);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    Line5 = sect.addModel(ep.LINE, Line5Props);
    LabelNaziv = sect.addModel(ep.LABEL, LabelNazivProps);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelKolicina = sect.addModel(ep.LABEL, LabelKolicinaProps);
    LabelRbr = sect.addModel(ep.LABEL, LabelRbrProps);
    LabelIznos = sect.addModel(ep.LABEL, LabelIznosProps);
    LabelJmj = sect.addModel(ep.LABEL, LabelJmjProps);
    LabelCijena = sect.addModel(ep.LABEL, LabelCijenaProps);
    Line6 = sect.addModel(ep.LINE, Line6Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    Text3 = sect.addModel(ep.TEXT, Text3Props);
    TextJM = sect.addModel(ep.TEXT, TextJMProps);
    TextKOL = sect.addModel(ep.TEXT, TextKOLProps);
    TextNC = sect.addModel(ep.TEXT, TextNCProps);
    TextRBR = sect.addModel(ep.TEXT, TextRBRProps);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    Text5 = sect.addModel(ep.TEXT, Text5Props);
    Text6 = sect.addModel(ep.TEXT, Text6Props);
    Text7 = sect.addModel(ep.TEXT, Text7Props);
    TextINAB = sect.addModel(ep.TEXT, TextINABProps);
    TextCART = sect.addModel(ep.TEXT, TextCARTProps);
    Text8 = sect.addModel(ep.TEXT, Text8Props);
    TextNAZART = sect.addModel(ep.TEXT, TextNAZARTProps);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Line7 = sect.addModel(ep.LINE, Line7Props);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    Text9 = sect.addModel(ep.TEXT, Text9Props);
    LabelUkupno = sect.addModel(ep.LABEL, LabelUkupnoProps);
    TextPRPOR = sect.addModel(ep.TEXT, TextPRPORProps);
    LabelPretporez = sect.addModel(ep.LABEL, LabelPretporezProps);
    LabelSveukupno = sect.addModel(ep.LABEL, LabelSveukupnoProps);
    TextUKUPNO = sect.addModel(ep.TEXT, TextUKUPNOProps);
    Line8 = sect.addModel(ep.LINE, Line8Props);
    return sect;
  }

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Line9 = sect.addModel(ep.LINE, Line9Props);
    Text10 = sect.addModel(ep.TEXT, Text10Props);
    return sect;
  }

  public raReportSection createReportFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_FOOTER), ReportFooterProps);

    Text11 = sect.addModel(ep.TEXT, Text11Props);
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
