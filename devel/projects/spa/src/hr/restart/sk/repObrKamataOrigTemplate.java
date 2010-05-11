/****license*****************************************************************
**   file: repObrKamataOrigTemplate.java
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

public abstract class repObrKamataOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"", "", "JDOrepObrKamata", "", "", "10720",
     "", "", "", "", ""};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "", "", "11880",
     "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"CPAR", "", "Yes", "Yes", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"CPAR", "", "Yes", "", "", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"RBR", "", "", "Yes", "", "", ""};
  public raReportElement Section3;
  private String[] Section3Props = new String[] {"LRBR", "", "", "", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"CPAR", "Before", "", "", "Yes", "", "",
     "Yes", "2400"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "20", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "7780", "400",
     "1820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9620",
     "400", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60",
     "460", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement LabelOBRACUN_KAMATA;
  private String[] LabelOBRACUN_KAMATAProps = new String[] {"\nOBRA\u010CUN  KAMATA", "", "", "740",
     "10700", "700", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center"};
  public raReportElement TextRANGE;
  private String[] TextRANGEProps = new String[] {"RANGE", "", "", "", "", "", "Yes", "", "", "1440",
     "10700", "260", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", "Center", ""};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "", "2060", "10700", "0", "", "", ""};
  public raReportElement LabelPartner_;
  private String[] LabelPartner_Props = new String[] {"Partner :", "", "", "2140", "1240", "220",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZPAR;
  private String[] TextNAZPARProps = new String[] {"NAZPAR", "", "", "", "", "", "", "", "1320",
     "2140", /*3900*/"9300", "500", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "Yes"};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"CPAR", "", "", "", "Yes", "", "", "Yes", "320"};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "0", "10700", "0", "", "", ""};
  public raReportElement LabelIznos_kamate;
  private String[] LabelIznos_kamateProps = new String[] {"Iznos kamate", "", "9400", "40", "1300",
     "200", "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Center"};
  public raReportElement LabelIznos_glavnice;
  private String[] LabelIznos_glavniceProps = new String[] {"Iznos osnovice", "", "7840", "40",
     "1540", "200", "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "", "",
     "Center"};
  public raReportElement LabelKoeficijent;
  private String[] LabelKoeficijentProps = new String[] {"Koeficijent", "", "6540", "40", "1280",
     "200", "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Center"};
  public raReportElement LabelStopa;
  private String[] LabelStopaProps = new String[] {" Stopa ", "", "5960", "40", "560", "200",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", ""};
  public raReportElement LabelKasni;
  private String[] LabelKasniProps = new String[] {"Kasni", "", "5440", "40", "500", "200", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", ""};
  public raReportElement LabelPlaceno;
  private String[] LabelPlacenoProps = new String[] {" Pla\u0107eno", "", "4400", "40", "1020", "200",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Center"};
  public raReportElement LabelDospjeva;
  private String[] LabelDospjevaProps = new String[] {" Dospjeva", "", "3360", "40", "1020", "200",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Center"};
  public raReportElement LabelBroj_uplate;
  private String[] LabelBroj_uplateProps = new String[] {"Broj uplate", "", "1680", "40", "1660",
     "200", "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Center"};
  public raReportElement LabelBroj_racuna;
  private String[] LabelBroj_racunaProps = new String[] {"Broj ra\u010Duna", "", "", "40", "1660",
     "200", "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Center"};
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "", "260", "10700", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "700"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(if (> [STOPA] 0.) [STOPA] \"\")", "", "",
     "Number|true|#.##0,00", "", "", "", "", "5960", "", "560", "200", "", "", "", "", "", "",
     "Lucida Bright", "7", "", "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(if (> [KOEF] 0.) [KOEF] \"\")", "", "",
     "Number|true|#.##0,0000000000", "", "", "", "", "6540", "", "1280", "200", "", "", "", "", "",
     "", "Lucida Bright", "7", "", "", "", "Right", ""};
  public raReportElement TextGLAVNICA;
  private String[] TextGLAVNICAProps = new String[] {"GLAVNICA", "", "", "Number|true|#.##0,00", "",
     "", "", "", "7840", "", "1540", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "",
     "", "Right", ""};
  public raReportElement TextKASNI;
  private String[] TextKASNIProps = new String[] {"KASNI", "", "", "", "", "", "", "", "5440", "",
     "500", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", "Right", ""};
  public raReportElement TextDATPLAC;
  private String[] TextDATPLACProps = new String[] {"DATPLAC", "", "", "", "", "", "", "", "4400",
     "", "1020", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center", ""};
  public raReportElement TextBROJUPL;
  private String[] TextBROJUPLProps = new String[] {"BROJUPL", "", "", "", "",
     "", "", "", "1680", "", "1660", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "",
     "", "", ""};
  public raReportElement TextDATDOSP;
  private String[] TextDATDOSPProps = new String[] {"DATDOSP", "", "", "", "", "", "", "", "3360",
     "", "1020", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center", ""};
  public raReportElement TextKAMATA;
  private String[] TextKAMATAProps = new String[] {"=(if (> [KAMATA] 0.) [KAMATA] \"\")", "", "", "Number|true|#.##0,00", "", "",
     "", "", "9400", "", "1300", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "",
     "Right", ""};
  public raReportElement TextBROJRAC;
  private String[] TextBROJRACProps = new String[] {"BROJRAC", "", "", "", "", "", "", "", "", "",
     "1660", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", "", ""};
  public raReportSection SectionFooter2;
  private String[] SectionFooter2Props = new String[] {"RBR", "", "", "", "Yes", "", "", "340"};
  public raReportElement Line7;
  private String[] Line7Props = new String[] {"", "", "7840", "40", "2820", "0", "", "", ""};
  public raReportElement LabelUkupno;
  private String[] LabelUkupnoProps = new String[] {" Ukupno", "", "7840", "60", "800", "200",
     "Normal", "Light Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"KAMATA\")", "", "",
     "Number|true|#.###.###.##0,00", "", "", "", "", "8640", "60", "2060", "220", "Normal",
     "Light Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right", ""};  
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "140", "7800", "0", "", "", ""};
  public raReportElement Line8;
  private String[] Line8Props = new String[] {"", "", "7840", "260", "2820", "0", "", "", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"CPAR", "", "", "", "Yes", "", "", "680"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "0", "10680", "0", "", "", ""};
  public raReportElement LabelUkupan_iznos_kamate;
  private String[] LabelUkupan_iznos_kamateProps = new String[] {"Ukupan iznos kamate ", "", "",
     "40", "8640", "200", "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "Bold", "",
     "", "Right"};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(dsum \"KAMATA\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "8640", "40", "2060", "220", "Normal", "Gray", "", "", "", "",
     "Lucida Bright", "7", "Bold", "", "", "Right", ""};
  public raReportElement TextNAPOM;
  private String[] TextNAPOMProps = new String[] {"NAPOM", "", "", "", "", "", "", "", "", "360",
      "7800", "240", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "260", "10680", "0", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "260"};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "8440",
     "", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;

  public repObrKamataOrigTemplate() {
    createReportStructure();
    setReportProperties();
  }

  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
    Section2 = sect.getModel(ep.SECTION + 2, Section2Props);
    Section3 = sect.getModel(ep.SECTION + 3, Section3Props);
    return sect;
  }

  public abstract raReportSection createReportHeader();

  public abstract raReportSection createPageHeader();

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);
    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    LabelDatum_ispisa_ = sect.addModel(ep.LABEL, LabelDatum_ispisa_Props);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    LabelOBRACUN_KAMATA = sect.addModel(ep.LABEL, LabelOBRACUN_KAMATAProps);
    TextRANGE = sect.addModel(ep.TEXT, TextRANGEProps);
    Line6 = sect.addModel(ep.LINE, Line6Props);
    LabelPartner_ = sect.addModel(ep.LABEL, LabelPartner_Props);
    TextNAZPAR = sect.addModel(ep.TEXT, TextNAZPARProps);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    Line4 = sect.addModel(ep.LINE, Line4Props);
    LabelIznos_kamate = sect.addModel(ep.LABEL, LabelIznos_kamateProps);
    LabelIznos_glavnice = sect.addModel(ep.LABEL, LabelIznos_glavniceProps);
    LabelKoeficijent = sect.addModel(ep.LABEL, LabelKoeficijentProps);
    LabelStopa = sect.addModel(ep.LABEL, LabelStopaProps);
    LabelKasni = sect.addModel(ep.LABEL, LabelKasniProps);
    LabelPlaceno = sect.addModel(ep.LABEL, LabelPlacenoProps);
    LabelDospjeva = sect.addModel(ep.LABEL, LabelDospjevaProps);
    LabelBroj_uplate = sect.addModel(ep.LABEL, LabelBroj_uplateProps);
    LabelBroj_racuna = sect.addModel(ep.LABEL, LabelBroj_racunaProps);
    Line5 = sect.addModel(ep.LINE, Line5Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    Text1 = sect.addModel(ep.TEXT, Text1Props);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    TextGLAVNICA = sect.addModel(ep.TEXT, TextGLAVNICAProps);
    TextKASNI = sect.addModel(ep.TEXT, TextKASNIProps);
    TextDATPLAC = sect.addModel(ep.TEXT, TextDATPLACProps);
    TextBROJUPL = sect.addModel(ep.TEXT, TextBROJUPLProps);
    TextDATDOSP = sect.addModel(ep.TEXT, TextDATDOSPProps);
    TextKAMATA = sect.addModel(ep.TEXT, TextKAMATAProps);
    TextBROJRAC = sect.addModel(ep.TEXT, TextBROJRACProps);
    return sect;
  }

  public raReportSection createSectionFooter2() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 2), SectionFooter2Props);

    Line7 = sect.addModel(ep.LINE, Line7Props);
    LabelUkupno = sect.addModel(ep.LABEL, LabelUkupnoProps);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
    Line1 = sect.addModel(ep.LINE, Line1Props);
    Line8 = sect.addModel(ep.LINE, Line8Props);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Line2 = sect.addModel(ep.LINE, Line2Props);
    LabelUkupan_iznos_kamate = sect.addModel(ep.LABEL, LabelUkupan_iznos_kamateProps);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    Line3 = sect.addModel(ep.LINE, Line3Props);
    TextNAPOM = sect.addModel(ep.TEXT, TextNAPOMProps);
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
    SectionFooter2 = addSection(createSectionFooter2());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
