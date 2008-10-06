/****license*****************************************************************
**   file: repPorezListOrigTemplate.java
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

public abstract class repPorezListOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepPorezList", "",
     "", "10580", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "", "", "11880",
     "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"FirstLine", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"CSKL", "", "Yes", "", "", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"DATUMDOK", "", "", "", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"FirstLine", "", "", "", "Yes", "", "Yes", "",
     "1560"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextFirstLine1;
  private String[] TextFirstLine1Props = new String[] {"FirstLine", "", "", "", "", "", "", "",
     "60", "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "8140", "180",
     "1460", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9600",
     "180", "1120", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
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
  public raReportElement TextNASLOV;
  private String[] TextNASLOVProps = new String[] {"NASLOV", "", "", "", "", "", "", "", "", "640",
     "10740", "660", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center", ""};
  public raReportElement LabelSkladiste;
  private String[] LabelSkladisteProps = new String[] {"Skladište", "", "", "1320", "940", "220",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextCSKL;
  private String[] TextCSKLProps = new String[] {"CSKL", "", "", "", "", "", "", "", "960", "1320",
     "840", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZSKL;
  private String[] TextNAZSKLProps = new String[] {"NAZSKL", "", "", "", "", "", "", "", "1820",
     "1320", "4520", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"CSKL", "Yes", "", "", "Yes", "No", "", "Yes",
     "560"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "100", "10720", "0", "", "", ""};
  public raReportElement LabelDatum;
  private String[] LabelDatumProps = new String[] {"Datum", "", "", "140", "1220", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelVrsta_i_broj_dokumenta;
  private String[] LabelVrsta_i_broj_dokumentaProps = new String[] {"Vrsta i broj dokumenta", "",
     "1240", "140", "2680", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9",
     "Bold", "", "", "Center"};
  public raReportElement LabelPOREZ;
  private String[] LabelPOREZProps = new String[] {"POREZ", "", "7340", "140", "1680", "240",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelBEZ_POREZA;
  private String[] LabelBEZ_POREZAProps = new String[] {"BEZ POREZA", "", "3940", "140", "1680",
     "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {"UKUPNO", "", "9040", "140", "1680", "240",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelPRETPOREZ;
  private String[] LabelPRETPOREZProps = new String[] {"PRETPOREZ", "", "5640", "140", "1680",
     "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "400", "10720", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "280"};
  public raReportElement TextOPOR;
  private String[] TextOPORProps = new String[] {"OPOR", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7340", "", "1680", "200", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPRETPOR;
  private String[] TextPRETPORProps = new String[] {"PRETPOR", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5640", "", "1680", "200", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextBEZPOR;
  private String[] TextBEZPORProps = new String[] {"BEZPOR", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "3940", "", "1680", "200", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextUKUPNO;
  private String[] TextUKUPNOProps = new String[] {"UKUPNO", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9040", "", "1680", "200", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextDATUMDOK;
  private String[] TextDATUMDOKProps = new String[] {"DATUMDOK", "", "", "Date|true|jj.nn.aaaa", "",
     "", "", "", "", "", "1220", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement TextBRDOK;
  private String[] TextBRDOKProps = new String[] {"BRDOK", "", "", "", "", "", "", "", "1240", "",
     "2680", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"FirstLine", "", "", "", "Yes", "", "Yes", "1400"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "100", "10720", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "140", "10740", "480", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement TextSUMOPOR;
  private String[] TextSUMOPORProps = new String[] {"SUMOPOR", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7340", "180", "1680", "220", "",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement TextSUMPRETPOREZ;
  private String[] TextSUMPRETPOREZProps = new String[] {"SUMPRETPOREZ", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5640", "180", "1680", "220", "",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement LabelU_K_U_P_N_O;
  private String[] LabelU_K_U_P_N_OProps = new String[] {"U K U P N O", "", "", "180", "3920",
     "220", "", "Gray", "", "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement TextSUMBEZPOR;
  private String[] TextSUMBEZPORProps = new String[] {"SUMBEZPOR", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "3940", "180", "1680", "220", "",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement TextSUMUKUPNO;
  private String[] TextSUMUKUPNOProps = new String[] {"SUMUKUPNO", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9040", "180", "1680", "220", "",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement TextRAZLIKAPLATIT;
  private String[] TextRAZLIKAPLATITProps = new String[] {"RAZLIKAPLATIT", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9040", "420", "1680", "200", "",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement TextRAZLIKAVRATITI;
  private String[] TextRAZLIKAVRATITIProps = new String[] {"RAZLIKAVRATITI", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7340", "420", "1680", "200", "",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement LabelZA_POVRATPLATITI;
  private String[] LabelZA_POVRATPLATITIProps = new String[] {"ZA POVRAT/PLATITI", "", "", "420",
     "7320", "200", "", "Gray", "", "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "640", "10720", "0", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "340"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "8040",
     "60", "2700", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;

  public repPorezListOrigTemplate() {
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
    LabelDatum_ispisa_ = sect.addModel(ep.LABEL, LabelDatum_ispisa_Props);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    TextSecondLine1 = sect.addModel(ep.TEXT, TextSecondLine1Props);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextThirdLine1 = sect.addModel(ep.TEXT, TextThirdLine1Props);
    TextNASLOV = sect.addModel(ep.TEXT, TextNASLOVProps);
    LabelSkladiste = sect.addModel(ep.LABEL, LabelSkladisteProps);
    TextCSKL = sect.addModel(ep.TEXT, TextCSKLProps);
    TextNAZSKL = sect.addModel(ep.TEXT, TextNAZSKLProps);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    Line1 = sect.addModel(ep.LINE, Line1Props);
    LabelDatum = sect.addModel(ep.LABEL, LabelDatumProps);
    LabelVrsta_i_broj_dokumenta = sect.addModel(ep.LABEL, LabelVrsta_i_broj_dokumentaProps);
    LabelPOREZ = sect.addModel(ep.LABEL, LabelPOREZProps);
    LabelBEZ_POREZA = sect.addModel(ep.LABEL, LabelBEZ_POREZAProps);
    LabelUKUPNO = sect.addModel(ep.LABEL, LabelUKUPNOProps);
    LabelPRETPOREZ = sect.addModel(ep.LABEL, LabelPRETPOREZProps);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextOPOR = sect.addModel(ep.TEXT, TextOPORProps);
    TextPRETPOR = sect.addModel(ep.TEXT, TextPRETPORProps);
    TextBEZPOR = sect.addModel(ep.TEXT, TextBEZPORProps);
    TextUKUPNO = sect.addModel(ep.TEXT, TextUKUPNOProps);
    TextDATUMDOK = sect.addModel(ep.TEXT, TextDATUMDOKProps);
    TextBRDOK = sect.addModel(ep.TEXT, TextBRDOKProps);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Line3 = sect.addModel(ep.LINE, Line3Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    TextSUMOPOR = sect.addModel(ep.TEXT, TextSUMOPORProps);
    TextSUMPRETPOREZ = sect.addModel(ep.TEXT, TextSUMPRETPOREZProps);
    LabelU_K_U_P_N_O = sect.addModel(ep.LABEL, LabelU_K_U_P_N_OProps);
    TextSUMBEZPOR = sect.addModel(ep.TEXT, TextSUMBEZPORProps);
    TextSUMUKUPNO = sect.addModel(ep.TEXT, TextSUMUKUPNOProps);
    TextRAZLIKAPLATIT = sect.addModel(ep.TEXT, TextRAZLIKAPLATITProps);
    TextRAZLIKAVRATITI = sect.addModel(ep.TEXT, TextRAZLIKAVRATITIProps);
    LabelZA_POVRATPLATITI = sect.addModel(ep.LABEL, LabelZA_POVRATPLATITIProps);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    return sect;
  }

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
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
    ReportFooter = addSection(createReportFooter());
  }
}
