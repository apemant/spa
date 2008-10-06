/****license*****************************************************************
**   file: repIzdokOrigTemplate.java
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

public abstract class repIzdokOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepIzdok", "", "", 
     "16000", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "380.45700000000005", "340", "340.2", 
     "Landscape", "Customize", "11900", "16800", "", "", "0.0", "0.0", ""};
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
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"CSKL", "", "", "", "Yes", "", "", "Yes", 
     "880"};
  public raReportElement LabelR_A_Z_D_U_Z_E_N_J;
  private String[] LabelR_A_Z_D_U_Z_E_N_JProps = new String[] {"R A Z D U Ž E N J E", "", "2320", 
     "140", "6060", "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelP_R_O_D_A_J_A;
  private String[] LabelP_R_O_D_A_J_AProps = new String[] {"P R O D A J A", "", "8400", "140", 
     "6060", "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelSTVARNA;
  private String[] LabelSTVARNAProps = new String[] {"STVARNA", "", "14480", "140", "1620", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDokument;
  private String[] LabelDokumentProps = new String[] {"Dokument", "", "1080", "140", "1220", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "140", "1060", "460", "Normal", "Gray", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDatum;
  private String[] LabelDatumProps = new String[] {"Datum", "", "80", "260", "940", "240", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRuC;
  private String[] LabelRuCProps = new String[] {"RuC", "", "14480", "380", "1620", "220", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPOREZ;
  private String[] LabelPOREZProps = new String[] {"POREZ", "", "11440", "380", "1500", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelBroj;
  private String[] LabelBrojProps = new String[] {" Broj", "", "1600", "380", "700", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPOPUST;
  private String[] LabelPOPUSTProps = new String[] {"POPUST", "", "8400", "380", "1500", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {"UKUPNO", "", "12960", "380", "1500", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelVr;
  private String[] LabelVrProps = new String[] {"Vr.", "", "1080", "380", "500", "220", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRuC1;
  private String[] LabelRuC1Props = new String[] {"RuC", "", "3840", "380", "1500", "220", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPOREZ1;
  private String[] LabelPOREZ1Props = new String[] {"POREZ", "", "5360", "380", "1500", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelUKUPNO1;
  private String[] LabelUKUPNO1Props = new String[] {"UKUPNO", "", "6880", "380", "1500", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNABAVA;
  private String[] LabelNABAVAProps = new String[] {"NABAVA", "", "2320", "380", "1500", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPRIHOD;
  private String[] LabelPRIHODProps = new String[] {"PRIHOD", "", "9920", "380", "1500", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "", "620", "2300", "180", "Normal", "Gray", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label8__6__7;
  private String[] Label8__6__7Props = new String[] {"8 = 6 + 7", "", "12960", "620", "1500", "180", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label21;
  private String[] Label21Props = new String[] {"2", "", "3840", "620", "1500", "180", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label7;
  private String[] Label7Props = new String[] {"7", "", "11440", "620", "1500", "180", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label11;
  private String[] Label11Props = new String[] {"1", "", "2320", "620", "1500", "180", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label3;
  private String[] Label3Props = new String[] {"3", "", "5360", "620", "1500", "180", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label4__1__2__3;
  private String[] Label4__1__2__3Props = new String[] {"4 = 1 + 2 + 3", "", "6880", "620", "1500", 
     "180", "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label5;
  private String[] Label5Props = new String[] {"5", "", "8400", "620", "1500", "180", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label9__6__1;
  private String[] Label9__6__1Props = new String[] {"9 = 6 - 1", "", "14480", "620", "1620", "180", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label6;
  private String[] Label6Props = new String[] {"6", "", "9920", "620", "1500", "180", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "240"};
  public raReportElement TextIPRODSP;
  private String[] TextIPRODSPProps = new String[] {"IPRODSP", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "12960", "", "1500", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement TextVRDOK;
  private String[] TextVRDOKProps = new String[] {"VRDOK", "", "", "", "", "", "", "", "1080", "", 
     "500", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center", "No"};
  public raReportElement TextDATDOK;
  private String[] TextDATDOKProps = new String[] {"DATDOK", "", "", "", "", "", "", "", "", "", 
     "1060", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center", "No"};
  public raReportElement TextPorez;
  private String[] TextPorezProps = new String[] {"Porez", "", "", "Number|true|#.##0,00", "", "", 
     "", "", "11440", "", "1500", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", 
     "Right", "No"};
  public raReportElement TextIPRODBP;
  private String[] TextIPRODBPProps = new String[] {"IPRODBP", "", "", "Number|true|#.##0,00", "", 
     "", "", "", "9920", "", "1500", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", 
     "", "Right", "No"};
  public raReportElement TextBRDOK;
  private String[] TextBRDOKProps = new String[] {"BRDOK", "", "", "", "", "", "", "", "1600", "", 
     "700", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", "Right", "No"};
  public raReportElement TextIPOR;
  private String[] TextIPORProps = new String[] {"IPOR", "", "", "Number|true|#.##0,00", "", "", "", 
     "", "5360", "", "1500", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", 
     "Right", "No"};
  public raReportElement TextIZAR;
  private String[] TextIZARProps = new String[] {"IZAR", "", "", "Number|true|#.##0,00", "", "", "", 
     "", "14480", "", "1620", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", 
     "Right", "No"};
  public raReportElement TextUIRAB;
  private String[] TextUIRABProps = new String[] {"UIRAB", "", "", "Number|true|#.##0,00", "", "", 
     "", "", "8400", "", "1500", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", 
     "Right", "No"};
  public raReportElement TextIMAR;
  private String[] TextIMARProps = new String[] {"IMAR", "", "", "Number|true|#.##0,00", "", "", "", 
     "", "3840", "", "1500", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", 
     "Right", "No"};
  public raReportElement TextIRAZ;
  private String[] TextIRAZProps = new String[] {"IRAZ", "", "", "Number|true|#.##0,00", "", "", "", 
     "", "6880", "", "1500", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", 
     "Right", "No"};
  public raReportElement TextINAB;
  private String[] TextINABProps = new String[] {"INAB", "", "", "Number|true|#.##0,00", "", "", "", 
     "", "2320", "", "1500", "180", "", "", "", "", "", "", "Lucida Bright", "7", "", "", "", 
     "Right", "No"};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"CSKL", "After", "", "", "Yes", "", "Yes", 
     "380"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"IMAR\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "3840", "60", "1500", "240", "Normal", 
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", "No"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"IZAR\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "14480", "60", "1620", "240", "Normal", 
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", "No"};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(dsum \"IPRODSP\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "12960", "60", "1500", "240", "Normal", 
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", "No"};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(dsum \"IPOR\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5360", "60", "1500", "240", "Normal", 
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", "No"};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {"=(dsum \"IRAZ\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6880", "60", "1500", "240", "Normal", 
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", "No"};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {"=(dsum \"INAB\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "2320", "60", "1500", "240", "Normal", 
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", "No"};
  public raReportElement Text8;
  private String[] Text8Props = new String[] {"=(dsum \"UIRAB\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8400", "60", "1500", "240", "Normal", 
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", "No"};
  public raReportElement Text9;
  private String[] Text9Props = new String[] {"=(dsum \"Porez\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "11440", "60", "1500", "240", "Normal", 
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", "No"};
  public raReportElement Text10;
  private String[] Text10Props = new String[] {"=(dsum \"IPRODBP\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9920", "60", "1500", "240", "Normal", 
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", "No"};
  public raReportElement LabelU_K_U_P_N_O;
  private String[] LabelU_K_U_P_N_OProps = new String[] {" U K U P N O", "", "", "60", "2300", 
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "240"};
  public raReportElement Text11;
  private String[] Text11Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "12940", 
     "", "2700", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;
  private String[] ReportFooterProps = new String[] {"", "", "", "No", "Yes", "", "", "0"};
  public raReportElement Text12;
  private String[] Text12Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "", 
     "", "20", "1980", "380", "", "", "", "", "", "", "", "", "", "", "", "", ""};

  public repIzdokOrigTemplate() {
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

  public abstract raReportSection createSectionHeader0();

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    LabelR_A_Z_D_U_Z_E_N_J = sect.addModel(ep.LABEL, LabelR_A_Z_D_U_Z_E_N_JProps);
    LabelP_R_O_D_A_J_A = sect.addModel(ep.LABEL, LabelP_R_O_D_A_J_AProps);
    LabelSTVARNA = sect.addModel(ep.LABEL, LabelSTVARNAProps);
    LabelDokument = sect.addModel(ep.LABEL, LabelDokumentProps);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    LabelDatum = sect.addModel(ep.LABEL, LabelDatumProps);
    LabelRuC = sect.addModel(ep.LABEL, LabelRuCProps);
    LabelPOREZ = sect.addModel(ep.LABEL, LabelPOREZProps);
    LabelBroj = sect.addModel(ep.LABEL, LabelBrojProps);
    LabelPOPUST = sect.addModel(ep.LABEL, LabelPOPUSTProps);
    LabelUKUPNO = sect.addModel(ep.LABEL, LabelUKUPNOProps);
    LabelVr = sect.addModel(ep.LABEL, LabelVrProps);
    LabelRuC1 = sect.addModel(ep.LABEL, LabelRuC1Props);
    LabelPOREZ1 = sect.addModel(ep.LABEL, LabelPOREZ1Props);
    LabelUKUPNO1 = sect.addModel(ep.LABEL, LabelUKUPNO1Props);
    LabelNABAVA = sect.addModel(ep.LABEL, LabelNABAVAProps);
    LabelPRIHOD = sect.addModel(ep.LABEL, LabelPRIHODProps);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    Label8__6__7 = sect.addModel(ep.LABEL, Label8__6__7Props);
    Label21 = sect.addModel(ep.LABEL, Label21Props);
    Label7 = sect.addModel(ep.LABEL, Label7Props);
    Label11 = sect.addModel(ep.LABEL, Label11Props);
    Label3 = sect.addModel(ep.LABEL, Label3Props);
    Label4__1__2__3 = sect.addModel(ep.LABEL, Label4__1__2__3Props);
    Label5 = sect.addModel(ep.LABEL, Label5Props);
    Label9__6__1 = sect.addModel(ep.LABEL, Label9__6__1Props);
    Label6 = sect.addModel(ep.LABEL, Label6Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextIPRODSP = sect.addModel(ep.TEXT, TextIPRODSPProps);
    TextVRDOK = sect.addModel(ep.TEXT, TextVRDOKProps);
    TextDATDOK = sect.addModel(ep.TEXT, TextDATDOKProps);
    TextPorez = sect.addModel(ep.TEXT, TextPorezProps);
    TextIPRODBP = sect.addModel(ep.TEXT, TextIPRODBPProps);
    TextBRDOK = sect.addModel(ep.TEXT, TextBRDOKProps);
    TextIPOR = sect.addModel(ep.TEXT, TextIPORProps);
    TextIZAR = sect.addModel(ep.TEXT, TextIZARProps);
    TextUIRAB = sect.addModel(ep.TEXT, TextUIRABProps);
    TextIMAR = sect.addModel(ep.TEXT, TextIMARProps);
    TextIRAZ = sect.addModel(ep.TEXT, TextIRAZProps);
    TextINAB = sect.addModel(ep.TEXT, TextINABProps);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Text2 = sect.addModel(ep.TEXT, Text2Props);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    Text5 = sect.addModel(ep.TEXT, Text5Props);
    Text6 = sect.addModel(ep.TEXT, Text6Props);
    Text7 = sect.addModel(ep.TEXT, Text7Props);
    Text8 = sect.addModel(ep.TEXT, Text8Props);
    Text9 = sect.addModel(ep.TEXT, Text9Props);
    Text10 = sect.addModel(ep.TEXT, Text10Props);
    LabelU_K_U_P_N_O = sect.addModel(ep.LABEL, LabelU_K_U_P_N_OProps);
    return sect;
  }

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text11 = sect.addModel(ep.TEXT, Text11Props);
    return sect;
  }

  public raReportSection createReportFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_FOOTER), ReportFooterProps);

    Text12 = sect.addModel(ep.TEXT, Text12Props);
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
