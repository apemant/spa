/****license*****************************************************************
**   file: repRacRNOrigTemplate.java
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

public abstract class repRacRNOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "hr", "JDOrepIzlazni", "", 
     "", "10660", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"300", "380.2", "520", "538.65", "", "Customize", 
     "11900", "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"BRDOK", "", "Yes", "Yes", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"MatUslGrouping", "", "Yes", "Yes", "Each Value", 
     "", ""};
  public raReportSection ReportHeader;
  private String[] ReportHeaderProps = new String[] {"", "", "", "", "Yes", "", "", "0"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(create-var-cache)", "", "", "", "", "", "", "", "", 
     "", "1740", "320", "", "", "", "", "", "Red", "", "", "Bold", "", "", "", ""};
  public raReportSection PageHeader;
  private String[] PageHeaderProps = new String[] {"", "", "2060"};
  public raReportElement TextLogoNazivlog;
  private String[] TextLogoNazivlogProps = new String[] {"LogoNazivlog", "", "", "", "", "", "", "", 
     "", "", "10860", "420", "", "", "", "", "", "", "Lucida Bright", "18", "Bold", "", "", "", ""};
  public raReportElement TextLogoFullAdr;
  private String[] TextLogoFullAdrProps = new String[] {"LogoFullAdr", "", "", "", "", "", "", "", 
     "", "400", "10860", "280", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "", ""};
  public raReportElement TextTelFax;
  private String[] TextTelFaxProps = new String[] {"TelFax", "", "", "", "", "", "", "", "", "700", 
     "10860", "280", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {
     "=(if (> [LogoPotvrda] 0) \"\nŽiro ra\u010Dun\" \"\")", "", "", "", "", "", "Yes", "", "", 
     "760", "7400", "520", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "", ""};
  public raReportElement TextLogoMatbroj;
  private String[] TextLogoMatbrojProps = new String[] {"LogoMatbroj", "", "", "", "", "", "", "", 
     "4920", "1000", "2820", "280", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "", 
     ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(if (> [LogoPotvrda] 0) \", MB\" \"\")", "", "", "", 
     "", "", "", "", "4360", "1000", "620", "280", "", "", "", "", "", "", "Lucida Bright", "11", 
     "", "", "", "", ""};
  public raReportElement TextLogoZiro;
  private String[] TextLogoZiroProps = new String[] {"LogoZiro", "", "", "", "", "", "", "", "1280", 
     "1000", "3080", "280", "", "", "", "", "", "", "Lucida Bright", "11", "Bold", "", "", "", ""};
  public raReportSection SectionHeader0;
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"MatUslGrouping", "", "", "", "Yes", "No", 
     "", "Yes", ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "120", "10800", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "9420", "160", "1400", "240", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "", "160", "8180", "220", "Normal", "Gray", 
     "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label3;
  private String[] Label3Props = new String[] {"%", "", "8200", "160", "1200", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement TextRADOVIMATERIJAL;
  private String[] TextRADOVIMATERIJALProps = new String[] {"RADOVIMATERIJAL", "", "", "", "", "", 
     "", "", "", "160", "3160", "220", "", "-1973791", "", "", "", "", "Lucida Bright", "8", "Bold", 
     "", "", "", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(put-var \"H1\" 1)", "", "", "", "No", "", "", "", 
     "3540", "180", "2640", "140", "", "", "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelIznos;
  private String[] LabelIznosProps = new String[] {"Iznos", "", "9420", "280", "1400", "340", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label4;
  private String[] Label4Props = new String[] {"", "", "5380", "400", "1080", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv", "", "1300", "400", "4060", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPor;
  private String[] LabelPorProps = new String[] {"Por", "", "8780", "400", "620", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "480", "400", "800", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "400", "460", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "6960", "400", "1220", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "6480", "400", "460", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPop;
  private String[] LabelPopProps = new String[] {"Pop", "", "8200", "400", "560", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement TextKOLICINEJEDINICE;
  private String[] TextKOLICINEJEDINICEProps = new String[] {"KOLICINEJEDINICE", "", "", "", "", "", 
     "", "", "5380", "400", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", 
     "", "Center", "No"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "640", "10800", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "300"};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(if (= [ImaPor3] 1) (put-var \"tpor3\" 1) \"\")", 
     "", "", "", "No", "", "", "", "3120", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement TextFC;
  private String[] TextFCProps = new String[] {"FC", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "6960", "", "1220", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {"=(if (= [ImaPor2] 1) (put-var \"tpor2\" 1) \"\")", 
     "", "", "", "No", "", "", "", "2640", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement TextUPRAB1;
  private String[] TextUPRAB1Props = new String[] {"UPRAB1", "", "", 
     "Number|false|1|3|2|2|true|3|false", "", "", "", "", "8200", "", "560", "220", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {"=(get-var \"H1\")", "", "", "", "", "", "", "", "", 
     "", "440", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "5380", "", "1080", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "6480", "", "460", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "1300", 
     "", "4060", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "", 
     "800", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextINETO;
  private String[] TextINETOProps = new String[] {"INETOP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9420", "", "1400", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPor1p2p3Naz;
  private String[] TextPor1p2p3NazProps = new String[] {"Por1p2p3Naz", "", "", 
     "Number|false|1|3|2|2|true|3|false", "", "", "", "", "8780", "", "620", "220", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text8;
  private String[] Text8Props = new String[] {"=(if (= [ImaPor1] 1) (put-var \"tpor1\" 1) \"\")", 
     "", "", "", "No", "", "", "", "2300", "", "0", "0", "", "", "", "", "", "", "", "", "", "", "", 
     "", ""};
  public raReportElement Text9;
  private String[] Text9Props = new String[] {"=(put-var \"H1\" (+ (get-var \"H1\") 1))", "", "", 
     "", "No", "", "", "", "2800", "40", "1620", "140", "", "", "", "", "", "", "", "", "", "", "", 
     "", ""};
  public raReportSection SectionFooter1;
  private String[] SectionFooter1Props = new String[] {"MatUslGrouping", "", "", "", "Yes", "", "", 
     "260"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"/", "", "", "20", "10800", "0", "", "", ""};
  public raReportElement Text10;
  private String[] Text10Props = new String[] {"=(dsum \"INETO\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9420", "40", "1400", "200", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"BRDOK", "After", "", "", "Yes", "", "Yes", 
     "3420"};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "0", "10800", "0", "", "", ""};
  public raReportElement Text11;
  private String[] Text11Props = new String[] {"=(dsum \"INETO\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "60", "1700", "240", "", "", "", 
     "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement LabelUkupno;
  private String[] LabelUkupnoProps = new String[] {"Ukupno", "", "6860", "60", "2180", "240", "", 
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement Label5;
  private String[] Label5Props = new String[] {"", "", "9040", "60", "1780", "1360", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelREKAPITULACIJA_POREZA;
  private String[] LabelREKAPITULACIJA_POREZAProps = new String[] {"REKAPITULACIJA POREZA", "", 
     "440", "300", "5020", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", 
     "Center"};
  public raReportElement Text12;
  private String[] Text12Props = new String[] {"=(dsum \"UIRAB\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "340", "1700", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement LabelPopust;
  private String[] LabelPopustProps = new String[] {"Popust", "", "6860", "340", "2180", "240", "", 
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement LabelGrupa;
  private String[] LabelGrupaProps = new String[] {"Grupa", "", "460", "580", "1200", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelPorez;
  private String[] LabelPorezProps = new String[] {"Porez", "", "4600", "580", "1300", "240", "", 
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement LabelOsnovica;
  private String[] LabelOsnovicaProps = new String[] {"Osnovica", "", "3000", "580", "1300", "240", 
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement Label6;
  private String[] Label6Props = new String[] {"%", "", "2140", "580", "720", "240", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Center"};
  public raReportElement Text13;
  private String[] Text13Props = new String[] {"=(dsum \"IPRODBP\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "620", "1700", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement LabelUkupno_bez_poreza;
  private String[] LabelUkupno_bez_porezaProps = new String[] {"Ukupno bez poreza", "", "6860", 
     "620", "2180", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement TextPorezDepartmentUKUPOR;
  private String[] TextPorezDepartmentUKUPORProps = new String[] {"PorezDepartmentUKUPOR", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "2120", "820", "720", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentCrtica;
  private String[] TextPorezDepartmentCrticaProps = new String[] {"PorezDepartmentCrtica", "", "", 
     "", "", "", "Yes", "", "1800", "820", "180", "220", "", "", "", "", "", "", "Lucida Bright", 
     "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentCPOR;
  private String[] TextPorezDepartmentCPORProps = new String[] {"PorezDepartmentCPOR", "", "", "", 
     "", "", "Yes", "", "460", "820", "1200", "220", "", "", "", "", "", "", "Lucida Bright", "8", 
     "", "", "", "", ""};
  public raReportElement TextPorezDepartmentPOR1;
  private String[] TextPorezDepartmentPOR1Props = new String[] {"PorezDepartmentPOR1", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "4600", "820", "1300", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentIPRODBP;
  private String[] TextPorezDepartmentIPRODBPProps = new String[] {"PorezDepartmentIPRODBP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "3000", "820", "1300", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement Text14;
  private String[] Text14Props = new String[] {
     "=(+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\"))", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "900", "1700", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement LabelUkupno_porez;
  private String[] LabelUkupno_porezProps = new String[] {"Ukupno porez", "", "6860", "900", "2180", 
     "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement LabelUkupno_s_porezom;
  private String[] LabelUkupno_s_porezomProps = new String[] {"Ukupno s porezom", "", "6860", 
     "1180", "2180", "240", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Text15;
  private String[] Text15Props = new String[] {
     "=(+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\")))", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "1180", "1700", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "6840", "1480", "3960", "0", "", "", ""};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "No", "420", "1740", "9540", "0", "", "", ""};
  public raReportElement LabelSlovima_;
  private String[] LabelSlovima_Props = new String[] {"Slovima :", "", "", "1800", "840", "220", "", 
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "", "900", 
     "1800", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextNAPOMENAOPIS;
  private String[] TextNAPOMENAOPISProps = new String[] {"NAPOMENAOPIS", "", "", "", "", "", "Yes", 
     "Yes", "", "2220", "10580", "680", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "380"};
  public raReportElement Line7;
  private String[] Line7Props = new String[] {"", "", "20", "40", "10800", "0", "", "Light Gray", 
     ""};
  public raReportElement Text16;
  private String[] Text16Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "8120", 
     "120", "2700", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;
  private String[] ReportFooterProps = new String[] {"", "", "", "No", "Yes", "", "", "0"};
  public raReportElement Text17;
  private String[] Text17Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "", 
     "", "20", "1900", "300", "", "", "", "", "", "", "", "", "", "", "", "", ""};

  public repRacRNOrigTemplate() {
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

//  public raReportSection createPageHeader() {
//    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_HEADER), PageHeaderProps);
//
//    TextLogoNazivlog = sect.addModel(ep.TEXT, TextLogoNazivlogProps);
//    TextLogoFullAdr = sect.addModel(ep.TEXT, TextLogoFullAdrProps);
//    TextTelFax = sect.addModel(ep.TEXT, TextTelFaxProps);
//    Text2 = sect.addModel(ep.TEXT, Text2Props);
//    TextLogoMatbroj = sect.addModel(ep.TEXT, TextLogoMatbrojProps);
//    Text3 = sect.addModel(ep.TEXT, Text3Props);
//    TextLogoZiro = sect.addModel(ep.TEXT, TextLogoZiroProps);
//    return sect;
//  }

  public abstract raReportSection createSectionHeader0();

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    Line1 = sect.addModel(ep.LINE, Line1Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    Label3 = sect.addModel(ep.LABEL, Label3Props);
    TextRADOVIMATERIJAL = sect.addModel(ep.TEXT, TextRADOVIMATERIJALProps);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    LabelIznos = sect.addModel(ep.LABEL, LabelIznosProps);
    Label4 = sect.addModel(ep.LABEL, Label4Props);
    LabelNaziv = sect.addModel(ep.LABEL, LabelNazivProps);
    LabelPor = sect.addModel(ep.LABEL, LabelPorProps);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelRbr = sect.addModel(ep.LABEL, LabelRbrProps);
    LabelCijena = sect.addModel(ep.LABEL, LabelCijenaProps);
    LabelJmj = sect.addModel(ep.LABEL, LabelJmjProps);
    LabelPop = sect.addModel(ep.LABEL, LabelPopProps);
    TextKOLICINEJEDINICE = sect.addModel(ep.TEXT, TextKOLICINEJEDINICEProps);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    Text5 = sect.addModel(ep.TEXT, Text5Props);
    TextFC = sect.addModel(ep.TEXT, TextFCProps);
    Text6 = sect.addModel(ep.TEXT, Text6Props);
    TextUPRAB1 = sect.addModel(ep.TEXT, TextUPRAB1Props);
    Text7 = sect.addModel(ep.TEXT, Text7Props);
    TextKOL = sect.addModel(ep.TEXT, TextKOLProps);
    TextJM = sect.addModel(ep.TEXT, TextJMProps);
    TextNAZART = sect.addModel(ep.TEXT, TextNAZARTProps);
    TextCART = sect.addModel(ep.TEXT, TextCARTProps);
    TextINETO = sect.addModel(ep.TEXT, TextINETOProps);
    TextPor1p2p3Naz = sect.addModel(ep.TEXT, TextPor1p2p3NazProps);
    Text8 = sect.addModel(ep.TEXT, Text8Props);
    Text9 = sect.addModel(ep.TEXT, Text9Props);
    return sect;
  }

  public raReportSection createSectionFooter1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 1), SectionFooter1Props);

    Line3 = sect.addModel(ep.LINE, Line3Props);
    Text10 = sect.addModel(ep.TEXT, Text10Props);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Line4 = sect.addModel(ep.LINE, Line4Props);
    LabelUkupno = sect.addModel(ep.LABEL, LabelUkupnoProps);
    Label5 = sect.addModel(ep.LABEL, Label5Props);
    LabelREKAPITULACIJA_POREZA = sect.addModel(ep.LABEL, LabelREKAPITULACIJA_POREZAProps);
    LabelPopust = sect.addModel(ep.LABEL, LabelPopustProps);
    LabelGrupa = sect.addModel(ep.LABEL, LabelGrupaProps);
    LabelPorez = sect.addModel(ep.LABEL, LabelPorezProps);
    LabelOsnovica = sect.addModel(ep.LABEL, LabelOsnovicaProps);
    Label6 = sect.addModel(ep.LABEL, Label6Props);
    LabelUkupno_bez_poreza = sect.addModel(ep.LABEL, LabelUkupno_bez_porezaProps);
    TextPorezDepartmentUKUPOR = sect.addModel(ep.TEXT, TextPorezDepartmentUKUPORProps);
    TextPorezDepartmentCrtica = sect.addModel(ep.TEXT, TextPorezDepartmentCrticaProps);
    TextPorezDepartmentCPOR = sect.addModel(ep.TEXT, TextPorezDepartmentCPORProps);
    TextPorezDepartmentPOR1 = sect.addModel(ep.TEXT, TextPorezDepartmentPOR1Props);
    TextPorezDepartmentIPRODBP = sect.addModel(ep.TEXT, TextPorezDepartmentIPRODBPProps);
    LabelUkupno_porez = sect.addModel(ep.LABEL, LabelUkupno_porezProps);
    LabelUkupno_s_porezom = sect.addModel(ep.LABEL, LabelUkupno_s_porezomProps);
    Line5 = sect.addModel(ep.LINE, Line5Props);
    Line6 = sect.addModel(ep.LINE, Line6Props);
    LabelSlovima_ = sect.addModel(ep.LABEL, LabelSlovima_Props);
    TextSLOVIMA = sect.addModel(ep.TEXT, TextSLOVIMAProps);
    TextNAPOMENAOPIS = sect.addModel(ep.TEXT, TextNAPOMENAOPISProps);
    Text11 = sect.addModel(ep.TEXT, Text11Props);
    Text12 = sect.addModel(ep.TEXT, Text12Props);
    Text13 = sect.addModel(ep.TEXT, Text13Props);
    Text14 = sect.addModel(ep.TEXT, Text14Props);
    Text15 = sect.addModel(ep.TEXT, Text15Props);
    return sect;
  }

  public abstract raReportSection createPageFooter();

//  public raReportSection createPageFooter() {
//    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);
//
//    Line7 = sect.addModel(ep.LINE, Line7Props);
//    Text16 = sect.addModel(ep.TEXT, Text16Props);
//    return sect;
//  }

  public raReportSection createReportFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_FOOTER), ReportFooterProps);

    Text17 = sect.addModel(ep.TEXT, Text17Props);
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
    SectionFooter1 = addSection(createSectionFooter1());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
