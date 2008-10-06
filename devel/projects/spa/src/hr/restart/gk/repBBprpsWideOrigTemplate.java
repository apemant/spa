/****license*****************************************************************
**   file: repBBprpsWideOrigTemplate.java
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

public abstract class repBBprpsWideOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepBrutoBilanca", 
     "", "", "15560", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "Landscape", "", 
     "11880", "16820", "", "", "0.0", "0.0", ""};
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
     "Yes", "", "2360"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60", 
     "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "", 
     "60", "220", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", 
     ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "12780", "320", 
     "1820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", 
     "14600", "320", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60", 
     "440", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement LabelBRUTO_BILANCA;
  private String[] LabelBRUTO_BILANCAProps = new String[] {"\nBRUTO BILANCA", "", "", "420", 
     "15680", "700", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center"};
  public raReportElement TextZaPeriod;
  private String[] TextZaPeriodProps = new String[] {"ZaPeriod", "", "", "", "", "", "", "", "", 
     "1100", "15680", "280", "", "", "", "", "", "", "Lucida Bright", "11", "Bold", "", "", 
     "Center", ""};
  public raReportElement LabelOrganizacijska_jedinica;
  private String[] LabelOrganizacijska_jedinicaProps = new String[] {"Organizacijska jedinica", "", 
     "", "1500", "2320", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Right"};
  public raReportElement TextCORGLAVA;
  private String[] TextCORGLAVAProps = new String[] {"CORGLAVA", "", "", "", "", "", "", "", "2340", 
     "1500", "840", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZORGLAVA;
  private String[] TextNAZORGLAVAProps = new String[] {"NAZORGLAVA", "", "", "", "", "", "", "", 
     "3200", "1500", "7540", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportElement TextVRBIL;
  private String[] TextVRBILProps = new String[] {"VRBIL", "", "", "", "", "", "Yes", "", "2340", 
     "1780", "8400", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelVrsta_bilance;
  private String[] LabelVrsta_bilanceProps = new String[] {"Vrsta bilance", "", "", "1780", "2320", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextCVAL;
  private String[] TextCVALProps = new String[] {"CVAL", "", "", "", "", "", "", "", "2340", "2040", 
     "840", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZVAL;
  private String[] TextNAZVALProps = new String[] {"NAZVAL", "", "", "", "", "", "", "", "3200", 
     "2040", "2320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextTECAJ;
  private String[] TextTECAJProps = new String[] {"TECAJ", "", "", "", "", "", "", "", "6520", 
     "2040", "2320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelValuta;
  private String[] LabelValutaProps = new String[] {"Valuta", "", "", "2040", "2320", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement LabelTecaj;
  private String[] LabelTecajProps = new String[] {"Te\u010Daj", "", "5680", "2040", "780", "220", 
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"Grouper", "", "", "", "Yes", "Yes", "Yes", 
     "Yes", "900"};
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
  private String[] Line1Props = new String[] {"", "", "", "260", "15660", "0", "", "", ""};
  public raReportElement LabelPromet;
  private String[] LabelPrometProps = new String[] {"Promet", "", "7480", "300", "6140", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "13640", "300", "2040", "480", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "", "300", "1280", "480", "Normal", "Gray", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPocetno_stanje;
  private String[] LabelPocetno_stanjeProps = new String[] {"Po\u010Detno stanje", "", "1300", 
     "300", "6160", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelSaldo;
  private String[] LabelSaldoProps = new String[] {"Saldo", "", "13640", "440", "2040", "", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelBroj_konta;
  private String[] LabelBroj_kontaProps = new String[] {"Broj konta", "", "", "440", "1280", "", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDuguje;
  private String[] LabelDugujeProps = new String[] {"Duguje", "", "7480", "560", "2020", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPotrazuje;
  private String[] LabelPotrazujeProps = new String[] {"Potražuje", "", "9520", "560", "2040", 
     "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPotrazuje1;
  private String[] LabelPotrazuje1Props = new String[] {"Potražuje", "", "3360", "560", "2040", 
     "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSaldo1;
  private String[] LabelSaldo1Props = new String[] {"Saldo", "", "11580", "560", "2040", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDuguje1;
  private String[] LabelDuguje1Props = new String[] {"Duguje", "", "1300", "560", "2040", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSaldo2;
  private String[] LabelSaldo2Props = new String[] {"Saldo", "", "5420", "560", "2040", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "820", "15660", "0", "", "", ""};
  public raReportSection SectionHeader2;
  private String[] SectionHeader2Props = new String[] {"KLASE", "", "", "", "Yes", "", "Yes", "", 
     "340"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "0", "15660", "0", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"SALPROM\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "11580", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"SALPS\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "5420", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement TextRKLASE;
  private String[] TextRKLASEProps = new String[] {"RKLASE", "", "", "", "", "", "", "", "", "40", 
     "1280", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"PSIP\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "3360", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "13640", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(dsum \"PSID\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "1300", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {"=(dsum \"IP\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "9520", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {"=(dsum \"ID\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "7460", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "260", "15660", "0", "", "", ""};
  public raReportSection SectionHeader3;
  private String[] SectionHeader3Props = new String[] {"KLASE2", "", "", "", "Yes", "", "Yes", "", 
     "300"};
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "", "0", "15660", "0", "", "", ""};
  public raReportElement TextKLASE2;
  private String[] TextKLASE2Props = new String[] {"KLASE2", "", "", "", "", "", "", "", "", "40", 
     "1280", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement Text8;
  private String[] Text8Props = new String[] {"=(dsum \"PSIP\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "3360", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text9;
  private String[] Text9Props = new String[] {"=(dsum \"IP\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "9520", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement Text10;
  private String[] Text10Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "13640", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text11;
  private String[] Text11Props = new String[] {"=(dsum \"ID\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "7460", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement Text12;
  private String[] Text12Props = new String[] {"=(dsum \"PSID\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "1300", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text13;
  private String[] Text13Props = new String[] {"=(dsum \"SALPROM\")", "", "", 
     "Number|true|#.##0,00", "", "", "", "", "11580", "40", "2040", "200", "", "", "", "", "", "", 
     "Lucida Bright", "7", "", "", "", "Right", "No"};
  public raReportElement Text14;
  private String[] Text14Props = new String[] {"=(dsum \"SALPS\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "5420", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportSection SectionHeader4;
  private String[] SectionHeader4Props = new String[] {"KLASE3", "", "", "", "Yes", "", "Yes", "", 
     "280"};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "", "0", "15660", "0", "", "", ""};
  public raReportElement Text15;
  private String[] Text15Props = new String[] {"=(dsum \"ID\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "7460", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement Text16;
  private String[] Text16Props = new String[] {"=(dsum \"PSID\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "1300", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text17;
  private String[] Text17Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "13640", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text18;
  private String[] Text18Props = new String[] {"=(dsum \"IP\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "9520", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement Text19;
  private String[] Text19Props = new String[] {"=(dsum \"PSIP\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "3360", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement TextKLASE3;
  private String[] TextKLASE3Props = new String[] {"KLASE3", "", "", "", "", "", "", "", "", "40", 
     "1280", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement Text20;
  private String[] Text20Props = new String[] {"=(dsum \"SALPROM\")", "", "", 
     "Number|true|#.##0,00", "", "", "", "", "11580", "40", "2040", "200", "", "", "", "", "", "", 
     "Lucida Bright", "7", "", "", "", "Right", "No"};
  public raReportElement Text21;
  private String[] Text21Props = new String[] {"=(dsum \"SALPS\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "5420", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "No", "No", "240"};
  public raReportElement TextPSID;
  private String[] TextPSIDProps = new String[] {"PSID", "", "", "Number|true|#.##0,00", "", "", "", 
     "", "1300", "", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", 
     "Right", "No"};
  public raReportElement TextSALDO;
  private String[] TextSALDOProps = new String[] {"SALDO", "", "", "Number|true|#.##0,00", "", "", 
     "", "", "13640", "", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", 
     "Right", "No"};
  public raReportElement TextSALPS;
  private String[] TextSALPSProps = new String[] {"SALPS", "", "", "Number|true|#.##0,00", "", "", 
     "", "", "5420", "", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", 
     "Right", "No"};
  public raReportElement TextSALPROM;
  private String[] TextSALPROMProps = new String[] {"SALPROM", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "11580", "", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement TextID;
  private String[] TextIDProps = new String[] {"ID", "", "", "Number|true|#.##0,00", "", "", "", "", 
     "7460", "", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", "Right", 
     "No"};
  public raReportElement TextIP;
  private String[] TextIPProps = new String[] {"IP", "", "", "Number|true|#.##0,00", "", "", "", "", 
     "9520", "", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", "Right", 
     "No"};
  public raReportElement TextPSIP;
  private String[] TextPSIPProps = new String[] {"PSIP", "", "", "Number|true|#.##0,00", "", "", "", 
     "", "3360", "", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", 
     "Right", "No"};
  public raReportElement TextBROJKONTA;
  private String[] TextBROJKONTAProps = new String[] {"BROJKONTA", "", "", "", "", "", "", "", "", 
     "", "1280", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportSection SectionFooter4;
  private String[] SectionFooter4Props = new String[] {"KLASE3", "", "", "", "Yes", "", "", "280"};
  public raReportElement Line7;
  private String[] Line7Props = new String[] {"", "", "", "0", "15660", "0", "", "", ""};
  public raReportElement Text22;
  private String[] Text22Props = new String[] {"=(dsum \"SALPS\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "5420", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement TextKLASE31;
  private String[] TextKLASE31Props = new String[] {"KLASE3", "", "", "", "", "", "", "", "", "40", 
     "1280", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement Text23;
  private String[] Text23Props = new String[] {"=(dsum \"SALPROM\")", "", "", 
     "Number|true|#.##0,00", "", "", "", "", "11580", "40", "2040", "200", "", "", "", "", "", "", 
     "Lucida Bright", "7", "", "", "", "Right", "No"};
  public raReportElement Text24;
  private String[] Text24Props = new String[] {"=(dsum \"PSIP\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "3360", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text25;
  private String[] Text25Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "13640", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text26;
  private String[] Text26Props = new String[] {"=(dsum \"PSID\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "1300", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text27;
  private String[] Text27Props = new String[] {"=(dsum \"IP\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "9520", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement Text28;
  private String[] Text28Props = new String[] {"=(dsum \"ID\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "7460", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportSection SectionFooter3;
  private String[] SectionFooter3Props = new String[] {"KLASE2", "", "", "", "Yes", "", "", "300"};
  public raReportElement Line8;
  private String[] Line8Props = new String[] {"", "", "", "0", "15660", "0", "", "", ""};
  public raReportElement Text29;
  private String[] Text29Props = new String[] {"=(dsum \"ID\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "7460", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement Text30;
  private String[] Text30Props = new String[] {"=(dsum \"IP\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "9520", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement Text31;
  private String[] Text31Props = new String[] {"=(dsum \"SALPS\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "5420", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text32;
  private String[] Text32Props = new String[] {"=(dsum \"PSID\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "1300", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text33;
  private String[] Text33Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "13640", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text34;
  private String[] Text34Props = new String[] {"=(dsum \"SALPROM\")", "", "", 
     "Number|true|#.##0,00", "", "", "", "", "11580", "40", "2040", "200", "", "", "", "", "", "", 
     "Lucida Bright", "7", "", "", "", "Right", "No"};
  public raReportElement Text35;
  private String[] Text35Props = new String[] {"=(dsum \"PSIP\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "3360", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement TextKLASE21;
  private String[] TextKLASE21Props = new String[] {"KLASE2", "", "", "", "", "", "", "", "", "40", 
     "1280", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportSection SectionFooter2;
  private String[] SectionFooter2Props = new String[] {"KLASE", "", "", "", "Yes", "", "", "300"};
  public raReportElement Line9;
  private String[] Line9Props = new String[] {"", "", "", "0", "15660", "0", "", "", ""};
  public raReportElement Text36;
  private String[] Text36Props = new String[] {"=(dsum \"PSID\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "1300", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text37;
  private String[] Text37Props = new String[] {"=(dsum \"ID\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "7460", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement Text38;
  private String[] Text38Props = new String[] {"=(dsum \"SALDO\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "13640", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text39;
  private String[] Text39Props = new String[] {"=(dsum \"IP\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "9520", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement Text40;
  private String[] Text40Props = new String[] {"=(dsum \"PSIP\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "3360", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement TextRKLASE1;
  private String[] TextRKLASE1Props = new String[] {"RKLASE", "", "", "", "", "", "", "", "", "40", 
     "1280", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement Text41;
  private String[] Text41Props = new String[] {"=(dsum \"SALPS\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "5420", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text42;
  private String[] Text42Props = new String[] {"=(dsum \"SALPROM\")", "", "", 
     "Number|true|#.##0,00", "", "", "", "", "11580", "40", "2040", "200", "", "", "", "", "", "", 
     "Lucida Bright", "7", "", "", "", "Right", "No"};
  public raReportElement Line10;
  private String[] Line10Props = new String[] {"", "", "", "260", "15660", "0", "", "", ""};
  public raReportSection SectionFooter1;
  private String[] SectionFooter1Props = new String[] {"Grouper", "", "", "", "Yes", "", "", 
     "280"};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {"UKUPNO", "", "", "", "15680", "240", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Text43;
  private String[] Text43Props = new String[] {"=(dsum \"SALDO\") ", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "13640", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", 
     "Bold", "", "", "Right", "No"};
  public raReportElement Text44;
  private String[] Text44Props = new String[] {"=(dsum \"PSIP\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "3360", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", 
     "Bold", "", "", "Right", "No"};
  public raReportElement Text45;
  private String[] Text45Props = new String[] {"=(dsum \"IP\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "9520", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "Bold", 
     "", "", "Right", "No"};
  public raReportElement Text46;
  private String[] Text46Props = new String[] {"=(dsum \"PSID\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "1300", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", 
     "Bold", "", "", "Right", "No"};
  public raReportElement Text47;
  private String[] Text47Props = new String[] {"=(dsum \"ID\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "7460", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "Bold", 
     "", "", "Right", "No"};
  public raReportElement Text48;
  private String[] Text48Props = new String[] {"=(dsum \"SALPS\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "5420", "40", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text49;
  private String[] Text49Props = new String[] {"=(dsum \"SALPROM\")", "", "", 
     "Number|true|#.##0,00", "", "", "", "", "11580", "40", "2040", "200", "", "", "", "", "", "", 
     "Lucida Bright", "7", "", "", "", "Right", "No"};
  public raReportElement Line11;
  private String[] Line11Props = new String[] {"", "", "", "260", "15660", "0", "", "", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"FirstLine", "", "", "", "Yes", "", "", 
     "340"};
  public raReportElement LabelSVEUKUPNO;
  private String[] LabelSVEUKUPNOProps = new String[] {"SVEUKUPNO", "", "", "", "15680", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Text50;
  private String[] Text50Props = new String[] {"=(dsum \"ID\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "7460", "20", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "Bold", 
     "", "", "Right", "No"};
  public raReportElement Text51;
  private String[] Text51Props = new String[] {"=(dsum \"SALPS\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "5420", "20", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "", 
     "", "", "Right", "No"};
  public raReportElement Text52;
  private String[] Text52Props = new String[] {"=(dsum \"IP\")", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "9520", "20", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", "Bold", 
     "", "", "Right", "No"};
  public raReportElement Text53;
  private String[] Text53Props = new String[] {"=(dsum \"SALPROM\")", "", "", 
     "Number|true|#.##0,00", "", "", "", "", "11580", "20", "2040", "200", "", "", "", "", "", "", 
     "Lucida Bright", "7", "", "", "", "Right", "No"};
  public raReportElement Text54;
  private String[] Text54Props = new String[] {"=(dsum \"PSID\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "1300", "20", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", 
     "Bold", "", "", "Right", "No"};
  public raReportElement Text55;
  private String[] Text55Props = new String[] {"=(dsum \"SALDO\") ", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "13640", "20", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", 
     "Bold", "", "", "Right", "No"};
  public raReportElement Text56;
  private String[] Text56Props = new String[] {"=(dsum \"PSIP\")", "", "", "Number|true|#.##0,00", 
     "", "", "", "", "3360", "20", "2040", "200", "", "", "", "", "", "", "Lucida Bright", "7", 
     "Bold", "", "", "Right", "No"};
  public raReportElement Line12;
  private String[] Line12Props = new String[] {"", "", "", "260", "15660", "0", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "260"};
  public raReportElement Text57;
  private String[] Text57Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "13420", 
     "", "2260", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;

  public repBBprpsWideOrigTemplate() {
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

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    LabelDatum_ispisa_ = sect.addModel(ep.LABEL, LabelDatum_ispisa_Props);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    LabelBRUTO_BILANCA = sect.addModel(ep.LABEL, LabelBRUTO_BILANCAProps);
    TextZaPeriod = sect.addModel(ep.TEXT, TextZaPeriodProps);
    LabelOrganizacijska_jedinica = sect.addModel(ep.LABEL, LabelOrganizacijska_jedinicaProps);
    TextCORGLAVA = sect.addModel(ep.TEXT, TextCORGLAVAProps);
    TextNAZORGLAVA = sect.addModel(ep.TEXT, TextNAZORGLAVAProps);
    TextVRBIL = sect.addModel(ep.TEXT, TextVRBILProps);
    LabelVrsta_bilance = sect.addModel(ep.LABEL, LabelVrsta_bilanceProps);
    TextCVAL = sect.addModel(ep.TEXT, TextCVALProps);
    TextNAZVAL = sect.addModel(ep.TEXT, TextNAZVALProps);
    TextTECAJ = sect.addModel(ep.TEXT, TextTECAJProps);
    LabelValuta = sect.addModel(ep.LABEL, LabelValutaProps);
    LabelTecaj = sect.addModel(ep.LABEL, LabelTecajProps);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    TextCORG = sect.addModel(ep.TEXT, TextCORGProps);
    TextNAZORG = sect.addModel(ep.TEXT, TextNAZORGProps);
    TextOJGR = sect.addModel(ep.TEXT, TextOJGRProps);
    Line1 = sect.addModel(ep.LINE, Line1Props);
    LabelPromet = sect.addModel(ep.LABEL, LabelPrometProps);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    LabelPocetno_stanje = sect.addModel(ep.LABEL, LabelPocetno_stanjeProps);
    LabelSaldo = sect.addModel(ep.LABEL, LabelSaldoProps);
    LabelBroj_konta = sect.addModel(ep.LABEL, LabelBroj_kontaProps);
    LabelDuguje = sect.addModel(ep.LABEL, LabelDugujeProps);
    LabelPotrazuje = sect.addModel(ep.LABEL, LabelPotrazujeProps);
    LabelPotrazuje1 = sect.addModel(ep.LABEL, LabelPotrazuje1Props);
    LabelSaldo1 = sect.addModel(ep.LABEL, LabelSaldo1Props);
    LabelDuguje1 = sect.addModel(ep.LABEL, LabelDuguje1Props);
    LabelSaldo2 = sect.addModel(ep.LABEL, LabelSaldo2Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createSectionHeader2() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 2), SectionHeader2Props);

    Line3 = sect.addModel(ep.LINE, Line3Props);
    Text1 = sect.addModel(ep.TEXT, Text1Props);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    TextRKLASE = sect.addModel(ep.TEXT, TextRKLASEProps);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    Text5 = sect.addModel(ep.TEXT, Text5Props);
    Text6 = sect.addModel(ep.TEXT, Text6Props);
    Text7 = sect.addModel(ep.TEXT, Text7Props);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    return sect;
  }

  public raReportSection createSectionHeader3() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 3), SectionHeader3Props);

    Line5 = sect.addModel(ep.LINE, Line5Props);
    TextKLASE2 = sect.addModel(ep.TEXT, TextKLASE2Props);
    Text8 = sect.addModel(ep.TEXT, Text8Props);
    Text9 = sect.addModel(ep.TEXT, Text9Props);
    Text10 = sect.addModel(ep.TEXT, Text10Props);
    Text11 = sect.addModel(ep.TEXT, Text11Props);
    Text12 = sect.addModel(ep.TEXT, Text12Props);
    Text13 = sect.addModel(ep.TEXT, Text13Props);
    Text14 = sect.addModel(ep.TEXT, Text14Props);
    return sect;
  }

  public raReportSection createSectionHeader4() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 4), SectionHeader4Props);

    Line6 = sect.addModel(ep.LINE, Line6Props);
    Text15 = sect.addModel(ep.TEXT, Text15Props);
    Text16 = sect.addModel(ep.TEXT, Text16Props);
    Text17 = sect.addModel(ep.TEXT, Text17Props);
    Text18 = sect.addModel(ep.TEXT, Text18Props);
    Text19 = sect.addModel(ep.TEXT, Text19Props);
    TextKLASE3 = sect.addModel(ep.TEXT, TextKLASE3Props);
    Text20 = sect.addModel(ep.TEXT, Text20Props);
    Text21 = sect.addModel(ep.TEXT, Text21Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextPSID = sect.addModel(ep.TEXT, TextPSIDProps);
    TextSALDO = sect.addModel(ep.TEXT, TextSALDOProps);
    TextSALPS = sect.addModel(ep.TEXT, TextSALPSProps);
    TextSALPROM = sect.addModel(ep.TEXT, TextSALPROMProps);
    TextID = sect.addModel(ep.TEXT, TextIDProps);
    TextIP = sect.addModel(ep.TEXT, TextIPProps);
    TextPSIP = sect.addModel(ep.TEXT, TextPSIPProps);
    TextBROJKONTA = sect.addModel(ep.TEXT, TextBROJKONTAProps);
    return sect;
  }

  public raReportSection createSectionFooter4() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 4), SectionFooter4Props);

    Line7 = sect.addModel(ep.LINE, Line7Props);
    Text22 = sect.addModel(ep.TEXT, Text22Props);
    TextKLASE31 = sect.addModel(ep.TEXT, TextKLASE31Props);
    Text23 = sect.addModel(ep.TEXT, Text23Props);
    Text24 = sect.addModel(ep.TEXT, Text24Props);
    Text25 = sect.addModel(ep.TEXT, Text25Props);
    Text26 = sect.addModel(ep.TEXT, Text26Props);
    Text27 = sect.addModel(ep.TEXT, Text27Props);
    Text28 = sect.addModel(ep.TEXT, Text28Props);
    return sect;
  }

  public raReportSection createSectionFooter3() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 3), SectionFooter3Props);

    Line8 = sect.addModel(ep.LINE, Line8Props);
    Text29 = sect.addModel(ep.TEXT, Text29Props);
    Text30 = sect.addModel(ep.TEXT, Text30Props);
    Text31 = sect.addModel(ep.TEXT, Text31Props);
    Text32 = sect.addModel(ep.TEXT, Text32Props);
    Text33 = sect.addModel(ep.TEXT, Text33Props);
    Text34 = sect.addModel(ep.TEXT, Text34Props);
    Text35 = sect.addModel(ep.TEXT, Text35Props);
    TextKLASE21 = sect.addModel(ep.TEXT, TextKLASE21Props);
    return sect;
  }

  public raReportSection createSectionFooter2() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 2), SectionFooter2Props);

    Line9 = sect.addModel(ep.LINE, Line9Props);
    Text36 = sect.addModel(ep.TEXT, Text36Props);
    Text37 = sect.addModel(ep.TEXT, Text37Props);
    Text38 = sect.addModel(ep.TEXT, Text38Props);
    Text39 = sect.addModel(ep.TEXT, Text39Props);
    Text40 = sect.addModel(ep.TEXT, Text40Props);
    TextRKLASE1 = sect.addModel(ep.TEXT, TextRKLASE1Props);
    Text41 = sect.addModel(ep.TEXT, Text41Props);
    Text42 = sect.addModel(ep.TEXT, Text42Props);
    Line10 = sect.addModel(ep.LINE, Line10Props);
    return sect;
  }

  public raReportSection createSectionFooter1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 1), SectionFooter1Props);

    LabelUKUPNO = sect.addModel(ep.LABEL, LabelUKUPNOProps);
    Text43 = sect.addModel(ep.TEXT, Text43Props);
    Text44 = sect.addModel(ep.TEXT, Text44Props);
    Text45 = sect.addModel(ep.TEXT, Text45Props);
    Text46 = sect.addModel(ep.TEXT, Text46Props);
    Text47 = sect.addModel(ep.TEXT, Text47Props);
    Text48 = sect.addModel(ep.TEXT, Text48Props);
    Text49 = sect.addModel(ep.TEXT, Text49Props);
    Line11 = sect.addModel(ep.LINE, Line11Props);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    LabelSVEUKUPNO = sect.addModel(ep.LABEL, LabelSVEUKUPNOProps);
    Text50 = sect.addModel(ep.TEXT, Text50Props);
    Text51 = sect.addModel(ep.TEXT, Text51Props);
    Text52 = sect.addModel(ep.TEXT, Text52Props);
    Text53 = sect.addModel(ep.TEXT, Text53Props);
    Text54 = sect.addModel(ep.TEXT, Text54Props);
    Text55 = sect.addModel(ep.TEXT, Text55Props);
    Text56 = sect.addModel(ep.TEXT, Text56Props);
    Line12 = sect.addModel(ep.LINE, Line12Props);
    return sect;
  }

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text57 = sect.addModel(ep.TEXT, Text57Props);
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
