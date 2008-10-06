/****license*****************************************************************
**   file: repInvIspOS_V2OrigTemplate.java
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

public abstract class repInvIspOS_V2OrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepIspOS", "", "",
     "15420", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "Landscape", "",
     "11880", "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"FirstLine", "", "", "Yes", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"COrg", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"OblikListe", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section3;
  private String[] Section3Props = new String[] {"InvBr", "", "", "", "", "", ""};
  public raReportSection ReportHeader;
  private String[] ReportHeaderProps = new String[] {"", "", "", "", "Yes", "Yes", "", "2640"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "20", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "",
     "60", "240", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement LabelDatum_ispisa;
  private String[] LabelDatum_ispisaProps = new String[] {"Datum ispisa ", "", "12620", "420",
     "1820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60",
     "420", "5920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "",
     "14460", "420", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextCaption;
  private String[] TextCaptionProps = new String[] {"Caption", "", "", "", "", "", "", "", "",
     "920", "15660", "380", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center",
     ""};
  public raReportElement TextCaption2;
  private String[] TextCaption2Props = new String[] {"Caption2", "", "", "", "", "", "", "", "20",
     "1260", "15660", "340", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "Center",
     ""};
  public raReportElement Textcaption3;
  private String[] Textcaption3Props = new String[] {"caption3", "", "", "", "", "", "", "", "40",
     "1560", "15660", "340", "", "", "", "", "", "", "Lucida Bright", "11", "", "", "", "Center",
     ""};
  public raReportElement LabelStatus;
  private String[] LabelStatusProps = new String[] {"Status", "", "", "1960", "720", "240", "", "",
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextStatus;
  private String[] TextStatusProps = new String[] {"Status", "", "", "", "", "", "", "", "1060",
     "1960", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelPorijeklo;
  private String[] LabelPorijekloProps = new String[] {"Porijeklo", "", "", "2140", "1020", "240",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextPorijeklo;
  private String[] TextPorijekloProps = new String[] {"Porijeklo", "", "", "", "", "", "", "",
     "1060", "2140", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "",
     ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(create-var-cache)", "", "", "", "No", "", "", "",
     "13700", "2160", "1540", "320", "", "", "", "", "", "Red", "", "", "Bold", "", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(put-var \"temp1\" \"\")", "", "", "", "No", "", "",
     "", "13720", "2200", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(put-var \"temp3\" \"\")", "", "", "", "No", "", "",
     "", "13740", "2220", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(put-var \"row\" 0)", "", "", "", "No", "", "", "",
     "13740", "2220", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement TextRowNum;
  private String[] TextRowNumProps = new String[] {"RowNum", "", "", "", "No", "", "", "", "13700",
     "2220", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(put-var \"temp2\" \"\")", "", "", "", "No", "", "",
     "", "13760", "2240", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelAktivnost;
  private String[] LabelAktivnostProps = new String[] {"Aktivnost", "", "", "2340", "1020", "260",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextAktivnost;
  private String[] TextAktivnostProps = new String[] {"Aktivnost", "", "", "", "", "", "", "",
     "1060", "2340", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "",
     ""};
  public raReportSection PageHeader;
  private String[] PageHeaderProps = new String[] {"", "", "380"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "0", "15640", "0", "", "", ""};
  public raReportElement LabelNaziv_sredstva;
  private String[] LabelNaziv_sredstvaProps = new String[] {"Naziv sredstva", "", "1320", "40",
     "5660", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelDatum;
  private String[] LabelDatumProps = new String[] {"Datum", "", "7000", "40", "1380", "240",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelInv_broj;
  private String[] LabelInv_brojProps = new String[] {"Inv. broj", "", "", "40", "1300", "240",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSadasnja_vrijednost;
  private String[] LabelSadasnja_vrijednostProps = new String[] {"Sadašnja vrijednost", "", "13280",
     "40", "2400", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelIspravak_vrijednost;
  private String[] LabelIspravak_vrijednostProps = new String[] {"Ispravak vrijednost", "", "10840",
     "40", "2420", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelOsnovica_vrijednost;
  private String[] LabelOsnovica_vrijednostProps = new String[] {"Osnovica vrijednost", "", "8400",
     "40", "2420", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {"=(put-var \"row\" (+(get-var \"row\") 1))", "", "",
     "", "No", "", "", "", "2220", "40", "5640", "80", "", "", "", "", "", "", "", "", "", "", "",
     "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "300", "15640", "0", "", "", ""};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"COrg", "", "", "", "Yes", "", "Yes", "",
     "400"};
  public raReportElement TextNazOrg;
  private String[] TextNazOrgProps = new String[] {"NazOrg", "", "", "", "", "", "", "", "3800", "",
     "9620", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelOrg_jedinica;
  private String[] LabelOrg_jedinicaProps = new String[] {"Org. jedinica", "", "200", "", "1920",
     "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextCOrg;
  private String[] TextCOrgProps = new String[] {"COrg", "", "", "", "", "", "", "", "2280", "",
     "1400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "260", "15660", "0", "", "", ""};
  public raReportSection SectionHeader2;
  private String[] SectionHeader2Props = new String[] {"OblikListe", "", "", "", "Yes", "", "Yes",
     "", "380"};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "0", "15640", "0", "", "Gray", ""};
  public raReportElement TextLabelaListe;
  private String[] TextLabelaListeProps = new String[] {"LabelaListe", "", "", "", "", "", "", "",
     "1460", "40", "1920", "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "", ""};
  public raReportElement TextOblikListe;
  private String[] TextOblikListeProps = new String[] {"OblikListe", "", "", "", "", "", "", "",
     "3480", "40", "1920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement TextNazOblikListe;
  private String[] TextNazOblikListeProps = new String[] {"NazOblikListe", "", "", "", "", "", "",
     "", "5440", "40", "7340", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "", ""};
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "", "280", "15640", "0", "", "Gray", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "300"};
  public raReportElement TextInvBr;
  private String[] TextInvBrProps = new String[] {"InvBr", "", "", "", "", "", "", "", "", "",
     "1300", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {"=(put-var \"row\" (+(get-var \"row\") 1))", "", "",
     "", "No", "", "", "", "4580", "", "1080", "200", "", "", "", "", "", "", "", "", "", "", "",
     "", ""};
  public raReportElement TextSadVr;
  private String[] TextSadVrProps = new String[] {"SadVr", "", "", "Number|true|#.##0,00", "", "",
     "", "", "13280", "", "2400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextIspVr;
  private String[] TextIspVrProps = new String[] {"IspVr", "", "", "Number|true|#.##0,00", "", "",
     "", "", "10840", "", "2420", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextOsnVr;
  private String[] TextOsnVrProps = new String[] {"OsnVr", "", "", "Number|true|#.##0,00", "", "",
     "", "", "8400", "", "2420", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextDatumOS;
  private String[] TextDatumOSProps = new String[] {"DatumOS", "", "", "", "", "", "", "", "7000",
     "", "1380", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", ""};
  public raReportElement TextNazSredstva;
  private String[] TextNazSredstvaProps = new String[] {"NazSredstva", "", "", "", "", "", "", "",
     "1320", "", "5660", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportSection SectionFooter2;
  private String[] SectionFooter2Props = new String[] {"OblikListe", "", "", "", "Yes", "", "Yes",
     ""};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "", "0", "15640", "0", "", "Gray", ""};
  public raReportElement Text8;
  private String[] Text8Props = new String[] {"=(dsum\"IspVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "10900", "40", "2380", "240", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", ""};
  public raReportElement Text9;
  private String[] Text9Props = new String[] {"=(dsum\"OsnVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "8480", "40", "2380", "240", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", ""};
  public raReportElement TextOblikListe1;
  private String[] TextOblikListe1Props = new String[] {"OblikListe", "", "", "", "", "", "", "",
     "3500", "40", "1920", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement TextLabelaListe1;
  private String[] TextLabelaListe1Props = new String[] {"LabelaListe1", "", "", "", "", "", "", "",
     "1460", "40", "1920", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement LabelUkupno;
  private String[] LabelUkupnoProps = new String[] {"Ukupno", "", "580", "40", "840", "240", "", "",
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Text10;
  private String[] Text10Props = new String[] {"=(dsum\"SadVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "13320", "40", "2360", "240", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", ""};
  public raReportElement Line7;
  private String[] Line7Props = new String[] {"", "", "", "300", "15640", "0", "", "Gray", ""};
  public raReportSection SectionFooter1;
  private String[] SectionFooter1Props = new String[] {"COrg", "", "", "", "Yes", "", "Yes", "420"};
  public raReportElement Line8;
  private String[] Line8Props = new String[] {"", "", "", "0", "15660", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "No", "8400", "40", "2420", "240", "Normal",
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "No", "10840", "40", "2420", "240", "Normal",
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label3;
  private String[] Label3Props = new String[] {"", "No", "13280", "40", "2400", "240", "Normal",
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label4;
  private String[] Label4Props = new String[] {"", "No", "", "40", "8380", "240", "Normal", "Gray",
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Text11;
  private String[] Text11Props = new String[] {"=(dsum\"OsnVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "8360", "60", "2420", "240", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", "No"};
  public raReportElement Text12;
  private String[] Text12Props = new String[] {"=(dsum\"IspVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "10820", "60", "2420", "240", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", "No"};
  public raReportElement LabelUkupno_org_jedinica;
  private String[] LabelUkupno_org_jedinicaProps = new String[] {"Ukupno org. jedinica", "", "",
     "60", "2120", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Text13;
  private String[] Text13Props = new String[] {"=(dsum\"SadVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "13240", "60", "2420", "240", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", "No"};
  public raReportElement TextNazOrg1;
  private String[] TextNazOrg1Props = new String[] {"NazOrg", "", "", "", "", "", "", "", "3800",
     "60", "4380", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextCOrg1;
  private String[] TextCOrg1Props = new String[] {"COrg", "", "", "", "", "", "", "", "2300", "60",
     "1400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement Line9;
  private String[] Line9Props = new String[] {"", "", "", "300", "15660", "0", "", "", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"FirstLine", "After", "", "", "Yes", "",
     "Yes", "440"};
  public raReportElement Line10;
  private String[] Line10Props = new String[] {"", "", "", "0", "15660", "0", "", "", ""};
  public raReportElement Text14;
  private String[] Text14Props = new String[] {"=(dsum\"IspVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "10840", "40", "2400", "220", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", "No"};
  public raReportElement Text15;
  private String[] Text15Props = new String[] {"=(dsum\"SadVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "13300", "40", "2360", "220", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", "No"};
  public raReportElement Text16;
  private String[] Text16Props = new String[] {"=(dsum\"OsnVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "8400", "40", "2380", "220", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", "No"};
  public raReportElement Label5;
  private String[] Label5Props = new String[] {"", "", "13280", "40", "2400", "240", "Normal",
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label6;
  private String[] Label6Props = new String[] {"", "", "8400", "40", "2420", "240", "Normal",
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label7;
  private String[] Label7Props = new String[] {"", "", "", "40", "8380", "240", "Normal", "Gray",
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label8;
  private String[] Label8Props = new String[] {"", "", "10840", "40", "2420", "240", "Normal",
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelS_V_E_U_K_U_P_N_O;
  private String[] LabelS_V_E_U_K_U_P_N_OProps = new String[] {"S V E U K U P N O", "", "", "60",
     "1920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Line11;
  private String[] Line11Props = new String[] {"", "", "", "300", "15660", "0", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "280"};
  public raReportElement Text17;
  private String[] Text17Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "13360",
     "", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;

  public repInvIspOS_V2OrigTemplate() {
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

  public raReportSection createReportHeader() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_HEADER), ReportHeaderProps);

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    LabelDatum_ispisa = sect.addModel(ep.LABEL, LabelDatum_ispisaProps);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    TextCaption = sect.addModel(ep.TEXT, TextCaptionProps);
    TextCaption2 = sect.addModel(ep.TEXT, TextCaption2Props);
    Textcaption3 = sect.addModel(ep.TEXT, Textcaption3Props);
    LabelStatus = sect.addModel(ep.LABEL, LabelStatusProps);
    TextStatus = sect.addModel(ep.TEXT, TextStatusProps);
    LabelPorijeklo = sect.addModel(ep.LABEL, LabelPorijekloProps);
    TextPorijeklo = sect.addModel(ep.TEXT, TextPorijekloProps);
    Text1 = sect.addModel(ep.TEXT, Text1Props);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    TextRowNum = sect.addModel(ep.TEXT, TextRowNumProps);
    Text5 = sect.addModel(ep.TEXT, Text5Props);
    LabelAktivnost = sect.addModel(ep.LABEL, LabelAktivnostProps);
    TextAktivnost = sect.addModel(ep.TEXT, TextAktivnostProps);
    return sect;
  }

  public raReportSection createPageHeader() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_HEADER), PageHeaderProps);

    Line1 = sect.addModel(ep.LINE, Line1Props);
    LabelNaziv_sredstva = sect.addModel(ep.LABEL, LabelNaziv_sredstvaProps);
    LabelDatum = sect.addModel(ep.LABEL, LabelDatumProps);
    LabelInv_broj = sect.addModel(ep.LABEL, LabelInv_brojProps);
    LabelSadasnja_vrijednost = sect.addModel(ep.LABEL, LabelSadasnja_vrijednostProps);
    LabelIspravak_vrijednost = sect.addModel(ep.LABEL, LabelIspravak_vrijednostProps);
    LabelOsnovica_vrijednost = sect.addModel(ep.LABEL, LabelOsnovica_vrijednostProps);
    Text6 = sect.addModel(ep.TEXT, Text6Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    TextNazOrg = sect.addModel(ep.TEXT, TextNazOrgProps);
    LabelOrg_jedinica = sect.addModel(ep.LABEL, LabelOrg_jedinicaProps);
    TextCOrg = sect.addModel(ep.TEXT, TextCOrgProps);
    Line3 = sect.addModel(ep.LINE, Line3Props);
    return sect;
  }

  public raReportSection createSectionHeader2() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 2), SectionHeader2Props);

    Line4 = sect.addModel(ep.LINE, Line4Props);
    TextLabelaListe = sect.addModel(ep.TEXT, TextLabelaListeProps);
    TextOblikListe = sect.addModel(ep.TEXT, TextOblikListeProps);
    TextNazOblikListe = sect.addModel(ep.TEXT, TextNazOblikListeProps);
    Line5 = sect.addModel(ep.LINE, Line5Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextInvBr = sect.addModel(ep.TEXT, TextInvBrProps);
    Text7 = sect.addModel(ep.TEXT, Text7Props);
    TextSadVr = sect.addModel(ep.TEXT, TextSadVrProps);
    TextIspVr = sect.addModel(ep.TEXT, TextIspVrProps);
    TextOsnVr = sect.addModel(ep.TEXT, TextOsnVrProps);
    TextDatumOS = sect.addModel(ep.TEXT, TextDatumOSProps);
    TextNazSredstva = sect.addModel(ep.TEXT, TextNazSredstvaProps);
    return sect;
  }

  public raReportSection createSectionFooter2() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 2), SectionFooter2Props);

    Line6 = sect.addModel(ep.LINE, Line6Props);
    Text8 = sect.addModel(ep.TEXT, Text8Props);
    Text9 = sect.addModel(ep.TEXT, Text9Props);
    TextOblikListe1 = sect.addModel(ep.TEXT, TextOblikListe1Props);
    TextLabelaListe1 = sect.addModel(ep.TEXT, TextLabelaListe1Props);
    LabelUkupno = sect.addModel(ep.LABEL, LabelUkupnoProps);
    Text10 = sect.addModel(ep.TEXT, Text10Props);
    Line7 = sect.addModel(ep.LINE, Line7Props);
    return sect;
  }

  public raReportSection createSectionFooter1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 1), SectionFooter1Props);

    Line8 = sect.addModel(ep.LINE, Line8Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    Label3 = sect.addModel(ep.LABEL, Label3Props);
    Label4 = sect.addModel(ep.LABEL, Label4Props);
    Text11 = sect.addModel(ep.TEXT, Text11Props);
    Text12 = sect.addModel(ep.TEXT, Text12Props);
    LabelUkupno_org_jedinica = sect.addModel(ep.LABEL, LabelUkupno_org_jedinicaProps);
    Text13 = sect.addModel(ep.TEXT, Text13Props);
    TextNazOrg1 = sect.addModel(ep.TEXT, TextNazOrg1Props);
    TextCOrg1 = sect.addModel(ep.TEXT, TextCOrg1Props);
    Line9 = sect.addModel(ep.LINE, Line9Props);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Label5 = sect.addModel(ep.LABEL, Label5Props);
    Label6 = sect.addModel(ep.LABEL, Label6Props);
    Label7 = sect.addModel(ep.LABEL, Label7Props);
    Label8 = sect.addModel(ep.LABEL, Label8Props);
    LabelS_V_E_U_K_U_P_N_O = sect.addModel(ep.LABEL, LabelS_V_E_U_K_U_P_N_OProps);
    Line10 = sect.addModel(ep.LINE, Line10Props);
    Text14 = sect.addModel(ep.TEXT, Text14Props);
    Text15 = sect.addModel(ep.TEXT, Text15Props);
    Text16 = sect.addModel(ep.TEXT, Text16Props);
    Line11 = sect.addModel(ep.LINE, Line11Props);
    return sect;
  }

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text17 = sect.addModel(ep.TEXT, Text17Props);
    return sect;
  }

//  public abstract raReportSection createReportFooter();

  public void createReportStructure() {
    template = ModelFactory.getModel(ep.REPORT_TEMPLATE);
    ModelFactory.setCurrentReport(template);

    ReportTemplate = addSection(new raReportSection(template, ReportTemplateProps));

    PageSetup = addSection(new raReportSection(template.getModel(ep.PAGE_SETUP), PageSetupProps));
    Sections = addSection(createSections());

    ReportHeader = addSection(createReportHeader());
    PageHeader = addSection(createPageHeader());
    SectionHeader1 = addSection(createSectionHeader1());
    SectionHeader2 = addSection(createSectionHeader2());
    Detail = addSection(createDetail());
    SectionFooter2 = addSection(createSectionFooter2());
    SectionFooter1 = addSection(createSectionFooter1());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
//    ReportFooter = addSection(createReportFooter());
  }
}
