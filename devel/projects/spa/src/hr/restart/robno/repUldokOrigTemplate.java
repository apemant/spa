/****license*****************************************************************
**   file: repUldokOrigTemplate.java
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

public abstract class repUldokOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"", "", "JDOrepUldok", "", "", "15680", "",
     "", "", "", ""};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"300", "380.2", "538.65", "538.65", "Landscape",
     "Customize", "11900", "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"CSKL", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"CSKL", "", "Yes", "", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"CSKL", "", "", "", "Yes", "", "", "Yes",
     "880"};
  public raReportElement LabelZ_A_D_U;
  private String[] LabelZ_A_D_UProps = new String[] {"Z A D U Ž E N J E", "", "9920", "140", "5700",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelN_A_B_A;
  private String[] LabelN_A_B_AProps = new String[] {"N A B A V A", "", "2820", "140", "7080",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "140", "1460", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDokument;
  private String[] LabelDokumentProps = new String[] {"Dokument", "", "1480", "140", "1320", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelVrsta;
  private String[] LabelVrstaProps = new String[] {"Vrsta", "", "1480", "380", "680", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPRETPOREZ;
  private String[] LabelPRETPOREZProps = new String[] {" PRETPOREZ", "", "4340", "380", "1340",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPOREZ;
  private String[] LabelPOREZProps = new String[] {"POREZ", "", "12780", "380", "1320", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelTROSKOVI;
  private String[] LabelTROSKOVIProps = new String[] {"TROŠKOVI", "", "8560", "380", "1340", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNABAVA;
  private String[] LabelNABAVAProps = new String[] {" NABAVA", "", "9920", "380", "1500", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {"UKUPNO", "", "2820", "380", "1500", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRuC;
  private String[] LabelRuCProps = new String[] {"RuC", "", "11440", "380", "1320", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelUKUPNO1;
  private String[] LabelUKUPNO1Props = new String[] {"UKUPNO", "", "14120", "380", "1500", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelBroj;
  private String[] LabelBrojProps = new String[] {" Broj", "", "2180", "380", "620", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelDatum;
  private String[] LabelDatumProps = new String[] {"Datum", "", "", "380", "1460", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPOPUST;
  private String[] LabelPOPUSTProps = new String[] {"POPUST", "", "7220", "380", "1320", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelBEZ_POREZA;
  private String[] LabelBEZ_POREZAProps = new String[] {" BEZ POREZA", "", "5700", "380", "1500",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label6__3_;
  private String[] Label6__3_Props = new String[] {"6 = 3 - 4 + 5", "", "9920", "600", "1500",
     "200", "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label11;
  private String[] Label11Props = new String[] {"1", "", "2820", "600", "1500", "200", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label7;
  private String[] Label7Props = new String[] {"7", "", "11440", "600", "1320", "200", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"2", "", "4340", "600", "1340", "200", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label8;
  private String[] Label8Props = new String[] {"8", "", "12780", "600", "1320", "200", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label5;
  private String[] Label5Props = new String[] {"5", "", "8560", "600", "1340", "200", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label9__6_;
  private String[] Label9__6_Props = new String[] {"9 = 6 + 7 + 8", "", "14120", "600", "1500",
     "200", "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label4;
  private String[] Label4Props = new String[] {"4", "", "7220", "600", "1320", "200", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label3__1_;
  private String[] Label3__1_Props = new String[] {"3 = 1 - 2", "", "5700", "600", "1500", "200",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "7", "", "", "", "Center"};
  public raReportElement Label21;
  private String[] Label21Props = new String[] {"", "", "", "620", "2800", "180", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "No", "", "280"};
  public raReportElement TextBRDOK;
  private String[] TextBRDOKProps = new String[] {"BRDOK", "", "", "", "", "", "", "", "2180", "",
     "620", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextDATDOK;
  private String[] TextDATDOKProps = new String[] {"DATDOK", "", "", "", "", "", "", "", "", "",
     "1460", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", ""};
  public raReportElement TextUKUPNO;
  private String[] TextUKUPNOProps = new String[] {"UKUPNO", "", "", "Number|true|#.##0,00", "", "",
     "", "", "2820", "", "1500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextVRDOK;
  private String[] TextVRDOKProps = new String[] {"VRDOK", "", "", "", "", "", "", "", "1480", "",
     "680", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", ""};
  public raReportElement TextUIPRPOR;
  private String[] TextUIPRPORProps = new String[] {"UIPRPOR", "", "", "Number|true|#.##0,00", "",
     "", "", "", "4340", "", "1340", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "",
     "", "Right", ""};
  public raReportElement TextIDOB;
  private String[] TextIDOBProps = new String[] {"IDOB", "", "", "Number|true|#.##0,00", "", "", "",
     "", "5700", "", "1500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextIRAB;
  private String[] TextIRABProps = new String[] {"IRAB", "", "", "Number|true|#.##0,00", "", "", "",
     "", "7220", "", "1320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextIZT;
  private String[] TextIZTProps = new String[] {"IZT", "", "", "Number|true|#.##0,00", "", "", "",
     "", "8560", "", "1340", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextIMAR;
  private String[] TextIMARProps = new String[] {"IMAR", "", "", "Number|true|#.##0,00", "", "", "",
     "", "11440", "", "1320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextINAB;
  private String[] TextINABProps = new String[] {"INAB", "", "", "Number|true|#.##0,00", "", "", "",
     "", "9920", "", "1500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextIPOR;
  private String[] TextIPORProps = new String[] {"IPOR", "", "", "Number|true|#.##0,00", "", "", "",
     "", "12780", "", "1320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextIZAD;
  private String[] TextIZADProps = new String[] {"IZAD", "", "", "Number|true|#.##0,00", "", "", "",
     "", "14120", "", "1500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"CSKL", "After", "", "", "Yes", "", "",
     "380"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IPOR\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "12780", "60", "1340", "240", "Normal",
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"UIPRPOR\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "4340", "60", "1360", "240", "Normal",
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"IZAD\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "14120", "60", "1520", "240", "Normal",
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(dsum \"IDOB\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5700", "60", "1520", "240", "Normal",
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", ""};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(dsum \"IRAB\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7220", "60", "1340", "240", "Normal",
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", ""};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {"=(dsum \"INAB\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9920", "60", "1520", "240", "Normal",
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", ""};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {"=(dsum \"UKUPNO\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "2820", "60", "1520", "240", "Normal",
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", ""};
  public raReportElement Text8;
  private String[] Text8Props = new String[] {"=(dsum \"IZT\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8560", "60", "1360", "240", "Normal",
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", ""};
  public raReportElement Text9;
  private String[] Text9Props = new String[] {"=(dsum \"IMAR\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "11440", "60", "1340", "240", "Normal",
     "Gray", "", "", "2 pt", "", "Lucida Bright", "7", "Bold", "", "", "Right", ""};
  public raReportElement LabelU_K_U_P;
  private String[] LabelU_K_U_PProps = new String[] {" U K U P N O", "", "", "60", "2800", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportSection PageFooter;
  public raReportSection ReportFooter;

  public repUldokOrigTemplate() {
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

  public abstract raReportSection createSectionHeader0();

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    LabelZ_A_D_U = sect.addModel(ep.LABEL, LabelZ_A_D_UProps);
    LabelN_A_B_A = sect.addModel(ep.LABEL, LabelN_A_B_AProps);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    LabelDokument = sect.addModel(ep.LABEL, LabelDokumentProps);
    LabelVrsta = sect.addModel(ep.LABEL, LabelVrstaProps);
    LabelPRETPOREZ = sect.addModel(ep.LABEL, LabelPRETPOREZProps);
    LabelPOREZ = sect.addModel(ep.LABEL, LabelPOREZProps);
    LabelTROSKOVI = sect.addModel(ep.LABEL, LabelTROSKOVIProps);
    LabelNABAVA = sect.addModel(ep.LABEL, LabelNABAVAProps);
    LabelUKUPNO = sect.addModel(ep.LABEL, LabelUKUPNOProps);
    LabelRuC = sect.addModel(ep.LABEL, LabelRuCProps);
    LabelUKUPNO1 = sect.addModel(ep.LABEL, LabelUKUPNO1Props);
    LabelBroj = sect.addModel(ep.LABEL, LabelBrojProps);
    LabelDatum = sect.addModel(ep.LABEL, LabelDatumProps);
    LabelPOPUST = sect.addModel(ep.LABEL, LabelPOPUSTProps);
    LabelBEZ_POREZA = sect.addModel(ep.LABEL, LabelBEZ_POREZAProps);
    Label6__3_ = sect.addModel(ep.LABEL, Label6__3_Props);
    Label11 = sect.addModel(ep.LABEL, Label11Props);
    Label7 = sect.addModel(ep.LABEL, Label7Props);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    Label8 = sect.addModel(ep.LABEL, Label8Props);
    Label5 = sect.addModel(ep.LABEL, Label5Props);
    Label9__6_ = sect.addModel(ep.LABEL, Label9__6_Props);
    Label4 = sect.addModel(ep.LABEL, Label4Props);
    Label3__1_ = sect.addModel(ep.LABEL, Label3__1_Props);
    Label21 = sect.addModel(ep.LABEL, Label21Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextBRDOK = sect.addModel(ep.TEXT, TextBRDOKProps);
    TextDATDOK = sect.addModel(ep.TEXT, TextDATDOKProps);
    TextUKUPNO = sect.addModel(ep.TEXT, TextUKUPNOProps);
    TextVRDOK = sect.addModel(ep.TEXT, TextVRDOKProps);
    TextUIPRPOR = sect.addModel(ep.TEXT, TextUIPRPORProps);
    TextIDOB = sect.addModel(ep.TEXT, TextIDOBProps);
    TextIRAB = sect.addModel(ep.TEXT, TextIRABProps);
    TextIZT = sect.addModel(ep.TEXT, TextIZTProps);
    TextIMAR = sect.addModel(ep.TEXT, TextIMARProps);
    TextINAB = sect.addModel(ep.TEXT, TextINABProps);
    TextIPOR = sect.addModel(ep.TEXT, TextIPORProps);
    TextIZAD = sect.addModel(ep.TEXT, TextIZADProps);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Text1 = sect.addModel(ep.TEXT, Text1Props);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    Text5 = sect.addModel(ep.TEXT, Text5Props);
    Text6 = sect.addModel(ep.TEXT, Text6Props);
    Text7 = sect.addModel(ep.TEXT, Text7Props);
    Text8 = sect.addModel(ep.TEXT, Text8Props);
    Text9 = sect.addModel(ep.TEXT, Text9Props);
    LabelU_K_U_P = sect.addModel(ep.LABEL, LabelU_K_U_PProps);
    return sect;
  }

  public abstract raReportSection createPageFooter();

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
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
