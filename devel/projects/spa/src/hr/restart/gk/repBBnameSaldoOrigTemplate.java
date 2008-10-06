/****license*****************************************************************
**   file: repBBnameSaldoOrigTemplate.java
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
package hr.restart.gk;

import hr.restart.util.reports.raElixirProperties;
import hr.restart.util.reports.raElixirPropertiesInstance;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raReportTemplate;
import sg.com.elixir.reportwriter.xml.ModelFactory;

public abstract class repBBnameSaldoOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepBrutoBilanca", 
     "", "", "10560", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "", "", "11880", 
     "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"FirstLine", "", "Yes", "Yes", "Each Value", "", 
     ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"Grouper", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"KLASE", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section3;
  private String[] Section3Props = new String[] {"KLASE2", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section4;
  private String[] Section4Props = new String[] {"KLASE3", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section5;
  private String[] Section5Props = new String[] {"BROJKONTA", "", "", "", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"FirstLine", "Before", "", "", "Yes", "", 
     "Yes", "", "2340"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60", 
     "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "", 
     "60", "220", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", 
     ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9620", 
     "400", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "7780", "400", 
     "1820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60", 
     "440", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement LabelBRUTO_BILANCA;
  private String[] LabelBRUTO_BILANCAProps = new String[] {"\nBRUTO BILANCA", "", "", "420", 
     "10740", "700", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center"};
  public raReportElement TextZaPeriod;
  private String[] TextZaPeriodProps = new String[] {"ZaPeriod", "", "", "", "", "", "", "", "", 
     "1100", "10740", "280", "", "", "", "", "", "", "Lucida Bright", "11", "Bold", "", "", 
     "Center", ""};
  public raReportElement TextNAZORGLAVA;
  private String[] TextNAZORGLAVAProps = new String[] {"NAZORGLAVA", "", "", "", "", "", "", "", 
     "3200", "1500", "7540", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportElement TextCORGLAVA;
  private String[] TextCORGLAVAProps = new String[] {"CORGLAVA", "", "", "", "", "", "", "", "2340", 
     "1500", "840", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelOrganizacijska_jedinica;
  private String[] LabelOrganizacijska_jedinicaProps = new String[] {"Organizacijska jedinica", "", 
     "", "1500", "2320", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Right"};
  public raReportElement TextVRBIL;
  private String[] TextVRBILProps = new String[] {"VRBIL", "", "", "", "", "", "Yes", "", "2340", 
     "1780", "8400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelVrsta_bilance;
  private String[] LabelVrsta_bilanceProps = new String[] {"Vrsta bilance", "", "", "1780", "2320", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZVAL;
  private String[] TextNAZVALProps = new String[] {"NAZVAL", "", "", "", "", "", "", "", "3200", 
     "2040", "2320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextCVAL;
  private String[] TextCVALProps = new String[] {"CVAL", "", "", "", "", "", "", "", "2340", "2040", 
     "840", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextTECAJ;
  private String[] TextTECAJProps = new String[] {"TECAJ", "", "", "", "", "", "", "", "6520", 
     "2040", "2320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelTecaj;
  private String[] LabelTecajProps = new String[] {"Te\u010Daj", "", "5680", "2040", "780", "220", 
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement LabelValuta;
  private String[] LabelValutaProps = new String[] {"Valuta", "", "", "2040", "2320", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"Grouper", "", "", "", "Yes", "Yes", "Yes", 
     "Yes", "840"};
  public raReportElement TextCORG;
  private String[] TextCORGProps = new String[] {"CORG", "", "", "", "", "", "", "", "2340", "", 
     "840", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZORG;
  private String[] TextNAZORGProps = new String[] {"NAZORG", "", "", "", "", "", "", "", "3200", "", 
     "4520", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextOJGR;
  private String[] TextOJGRProps = new String[] {"OJGR", "", "", "", "", "", "", "", "", "20", 
     "2320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "280", "10700", "0", "", "", ""};
  public raReportElement LabelBroj_konta;
  private String[] LabelBroj_kontaProps = new String[] {"Broj konta", "", "", "320", "1220", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSaldo;
  private String[] LabelSaldoProps = new String[] {"Saldo", "", "8820", "320", "1900", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_konta;
  private String[] LabelNaziv_kontaProps = new String[] {"Naziv konta", "", "1240", "320", "7560", 
     "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "580", "10700", "0", "", "", ""};
  public raReportSection SectionHeader2;
  private String[] SectionHeader2Props = new String[] {"KLASE", "", "", "", "Yes", "", "Yes", "", 
     "340"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "0", "10720", "0", "", "", ""};
  public raReportElement TextNAZIVKLASE;
  private String[] TextNAZIVKLASEProps = new String[] {"NAZIVKLASE", "", "", "", "", "", "", "", 
     "1240", "40", "7560", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     "No"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "8820", "40", "1900", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextRKLASE;
  private String[] TextRKLASEProps = new String[] {"RKLASE", "", "", "", "", "", "", "", "", "40", 
     "1220", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportSection SectionHeader3;
  private String[] SectionHeader3Props = new String[] {"KLASE2", "", "", "", "Yes", "", "Yes", "", 
     "320"};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "0", "10720", "0", "", "", ""};
  public raReportElement TextNAZIVKLASE2;
  private String[] TextNAZIVKLASE2Props = new String[] {"NAZIVKLASE2", "", "", "", "", "", "", "", 
     "1240", "20", "7560", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     "No"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "8820", "20", "1900", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextKLASE2;
  private String[] TextKLASE2Props = new String[] {"KLASE2", "", "", "", "", "", "", "", "", "20", 
     "1220", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportSection SectionHeader4;
  private String[] SectionHeader4Props = new String[] {"KLASE3", "", "", "", "Yes", "", "Yes", "", 
     "320"};
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "", "0", "10720", "0", "", "", ""};
  public raReportElement TextNAZIVKLASE3;
  private String[] TextNAZIVKLASE3Props = new String[] {"NAZIVKLASE3", "", "", "", "", "", "", "", 
     "1240", "40", "7560", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     "No"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "8820", "40", "1900", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextKLASE3;
  private String[] TextKLASE3Props = new String[] {"KLASE3", "", "", "", "", "", "", "", "", "40", 
     "1220", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "340"};
  public raReportElement TextNAZIVKONTA;
  private String[] TextNAZIVKONTAProps = new String[] {"NAZIVKONTA", "", "", "", "", "", "", "", 
     "1240", "", "7560", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextSALDO;
  private String[] TextSALDOProps = new String[] {"SALDO", "", "", "Number|true|#.##0,00", "", "", 
     "", "", "8820", "", "1900", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};
  public raReportElement TextBROJKONTA;
  private String[] TextBROJKONTAProps = new String[] {"BROJKONTA", "", "", "", "", "", "", "", "", 
     "", "1220", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportSection SectionFooter4;
  private String[] SectionFooter4Props = new String[] {"KLASE3", "", "", "", "Yes", "", "", "340"};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "", "0", "10720", "0", "", "", ""};
  public raReportElement TextKLASE31;
  private String[] TextKLASE31Props = new String[] {"KLASE3", "", "", "", "", "", "", "", "", "40", 
     "1220", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "8820", "40", "1900", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextNAZIVKLASE31;
  private String[] TextNAZIVKLASE31Props = new String[] {"NAZIVKLASE3", "", "", "", "", "", "", "", 
     "1240", "40", "7560", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     "No"};
  public raReportSection SectionFooter3;
  private String[] SectionFooter3Props = new String[] {"KLASE2", "", "", "", "Yes", "", "", "340"};
  public raReportElement Line7;
  private String[] Line7Props = new String[] {"", "", "", "0", "10720", "0", "", "", ""};
  public raReportElement TextKLASE21;
  private String[] TextKLASE21Props = new String[] {"KLASE2", "", "", "", "", "", "", "", "", "20", 
     "1220", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "8820", "20", "1900", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextNAZIVKLASE21;
  private String[] TextNAZIVKLASE21Props = new String[] {"NAZIVKLASE2", "", "", "", "", "", "", "", 
     "1240", "20", "7560", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     "No"};
  public raReportSection SectionFooter2;
  private String[] SectionFooter2Props = new String[] {"KLASE", "", "", "", "Yes", "", "", "340"};
  public raReportElement Line8;
  private String[] Line8Props = new String[] {"", "", "", "0", "10720", "0", "", "", ""};
  public raReportElement TextRKLASE1;
  private String[] TextRKLASE1Props = new String[] {"RKLASE", "", "", "", "", "", "", "", "", "40", 
     "1220", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "8820", "40", "1900", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextNAZIVKLASE1;
  private String[] TextNAZIVKLASE1Props = new String[] {"NAZIVKLASE", "", "", "", "", "", "", "", 
     "1240", "40", "7560", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     "No"};
  public raReportElement Line9;
  private String[] Line9Props = new String[] {"", "", "", "300", "10720", "0", "", "", ""};
  public raReportSection SectionFooter1;
  private String[] SectionFooter1Props = new String[] {"Grouper", "", "", "", "Yes", "", "Yes", 
     "520"};
  public raReportElement Line10;
  private String[] Line10Props = new String[] {"", "", "", "0", "10720", "0", "", "", ""};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {"UKUPNO", "", "", "40", "10720", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "8340", "60", "2380", "200", "", "", "", "", "", "", "Lucida Bright", "8", 
     "Bold", "", "", "Right", "No"};
  public raReportElement Line11;
  private String[] Line11Props = new String[] {"", "", "", "300", "10720", "0", "", "", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"FirstLine", "", "", "", "Yes", "", "", 
     "320"};
  public raReportElement Line12;
  private String[] Line12Props = new String[] {"", "", "", "0", "10720", "0", "", "", ""};
  public raReportElement LabelSVEUKUPNO;
  private String[] LabelSVEUKUPNOProps = new String[] {"SVEUKUPNO", "", "", "40", "10720", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextKumulativSALDO;
  private String[] TextKumulativSALDOProps = new String[] {"=(dsum \"SALDO\")", "", "", 
     "Number|true|#.##0,00", "", "", "", "", "8340", "60", "2380", "200", "", "", "", "", "", "", 
     "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement Line13;
  private String[] Line13Props = new String[] {"", "", "", "300", "10720", "0", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "260"};
  public raReportElement Text8;
  private String[] Text8Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "8440", 
     "", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;

  public repBBnameSaldoOrigTemplate() {
    createReportStructure();
    setReportProperties();
  }

  public raReportSection createSections() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTIONS));

    Section0 = sect.getModel(ep.SECTION + 0, Section0Props);
    Section1 = sect.getModel(ep.SECTION + 1, Section1Props);
    Section2 = sect.getModel(ep.SECTION + 2, Section2Props);
    Section3 = sect.getModel(ep.SECTION + 3, Section3Props);
    Section4 = sect.getModel(ep.SECTION + 4, Section4Props);
    Section5 = sect.getModel(ep.SECTION + 5, Section5Props);
    return sect;
  }

  public abstract raReportSection createReportHeader();

  public abstract raReportSection createPageHeader();

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    LabelDatum_ispisa_ = sect.addModel(ep.LABEL, LabelDatum_ispisa_Props);
    LabelBRUTO_BILANCA = sect.addModel(ep.LABEL, LabelBRUTO_BILANCAProps);
    LabelOrganizacijska_jedinica = sect.addModel(ep.LABEL, LabelOrganizacijska_jedinicaProps);
    LabelVrsta_bilance = sect.addModel(ep.LABEL, LabelVrsta_bilanceProps);
    LabelTecaj = sect.addModel(ep.LABEL, LabelTecajProps);
    LabelValuta = sect.addModel(ep.LABEL, LabelValutaProps);
    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextZaPeriod = sect.addModel(ep.TEXT, TextZaPeriodProps);
    TextNAZORGLAVA = sect.addModel(ep.TEXT, TextNAZORGLAVAProps);
    TextCORGLAVA = sect.addModel(ep.TEXT, TextCORGLAVAProps);
    TextVRBIL = sect.addModel(ep.TEXT, TextVRBILProps);
    TextNAZVAL = sect.addModel(ep.TEXT, TextNAZVALProps);
    TextCVAL = sect.addModel(ep.TEXT, TextCVALProps);
    TextTECAJ = sect.addModel(ep.TEXT, TextTECAJProps);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    LabelBroj_konta = sect.addModel(ep.LABEL, LabelBroj_kontaProps);
    LabelSaldo = sect.addModel(ep.LABEL, LabelSaldoProps);
    LabelNaziv_konta = sect.addModel(ep.LABEL, LabelNaziv_kontaProps);
    TextCORG = sect.addModel(ep.TEXT, TextCORGProps);
    TextNAZORG = sect.addModel(ep.TEXT, TextNAZORGProps);
    TextOJGR = sect.addModel(ep.TEXT, TextOJGRProps);
    Line1 = sect.addModel(ep.LINE, Line1Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createSectionHeader2() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 2), SectionHeader2Props);

    Line3 = sect.addModel(ep.LINE, Line3Props);
    TextNAZIVKLASE = sect.addModel(ep.TEXT, TextNAZIVKLASEProps);
    Text1 = sect.addModel(ep.TEXT, Text1Props);
    TextRKLASE = sect.addModel(ep.TEXT, TextRKLASEProps);
    return sect;
  }

  public raReportSection createSectionHeader3() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 3), SectionHeader3Props);

    Line4 = sect.addModel(ep.LINE, Line4Props);
    TextNAZIVKLASE2 = sect.addModel(ep.TEXT, TextNAZIVKLASE2Props);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    TextKLASE2 = sect.addModel(ep.TEXT, TextKLASE2Props);
    return sect;
  }

  public raReportSection createSectionHeader4() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 4), SectionHeader4Props);

    Line5 = sect.addModel(ep.LINE, Line5Props);
    TextNAZIVKLASE3 = sect.addModel(ep.TEXT, TextNAZIVKLASE3Props);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
    TextKLASE3 = sect.addModel(ep.TEXT, TextKLASE3Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextNAZIVKONTA = sect.addModel(ep.TEXT, TextNAZIVKONTAProps);
    TextSALDO = sect.addModel(ep.TEXT, TextSALDOProps);
    TextBROJKONTA = sect.addModel(ep.TEXT, TextBROJKONTAProps);
    return sect;
  }

  public raReportSection createSectionFooter4() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 4), SectionFooter4Props);

    Line6 = sect.addModel(ep.LINE, Line6Props);
    TextKLASE31 = sect.addModel(ep.TEXT, TextKLASE31Props);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    TextNAZIVKLASE31 = sect.addModel(ep.TEXT, TextNAZIVKLASE31Props);
    return sect;
  }

  public raReportSection createSectionFooter3() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 3), SectionFooter3Props);

    Line7 = sect.addModel(ep.LINE, Line7Props);
    TextKLASE21 = sect.addModel(ep.TEXT, TextKLASE21Props);
    Text5 = sect.addModel(ep.TEXT, Text5Props);
    TextNAZIVKLASE21 = sect.addModel(ep.TEXT, TextNAZIVKLASE21Props);
    return sect;
  }

  public raReportSection createSectionFooter2() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 2), SectionFooter2Props);

    Line8 = sect.addModel(ep.LINE, Line8Props);
    TextRKLASE1 = sect.addModel(ep.TEXT, TextRKLASE1Props);
    Text6 = sect.addModel(ep.TEXT, Text6Props);
    TextNAZIVKLASE1 = sect.addModel(ep.TEXT, TextNAZIVKLASE1Props);
    Line9 = sect.addModel(ep.LINE, Line9Props);
    return sect;
  }

  public raReportSection createSectionFooter1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 1), SectionFooter1Props);

    LabelUKUPNO = sect.addModel(ep.LABEL, LabelUKUPNOProps);
    Line10 = sect.addModel(ep.LINE, Line10Props);
    Text7 = sect.addModel(ep.TEXT, Text7Props);
    Line11 = sect.addModel(ep.LINE, Line11Props);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    LabelSVEUKUPNO = sect.addModel(ep.LABEL, LabelSVEUKUPNOProps);
    Line12 = sect.addModel(ep.LINE, Line12Props);
    TextKumulativSALDO = sect.addModel(ep.TEXT, TextKumulativSALDOProps);
    Line13 = sect.addModel(ep.LINE, Line13Props);
    return sect;
  }

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text8 = sect.addModel(ep.TEXT, Text8Props);
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
    SectionHeader2 = addSection(createSectionHeader2());
    SectionHeader3 = addSection(createSectionHeader3());
    SectionHeader4 = addSection(createSectionHeader4());
    Detail = addSection(createDetail());
    SectionFooter4 = addSection(createSectionFooter4());
    SectionFooter3 = addSection(createSectionFooter3());
    SectionFooter2 = addSection(createSectionFooter2());
    SectionFooter1 = addSection(createSectionFooter1());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
