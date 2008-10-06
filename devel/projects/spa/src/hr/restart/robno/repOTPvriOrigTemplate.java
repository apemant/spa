/****license*****************************************************************
**   file: repOTPvriOrigTemplate.java
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

public abstract class repOTPvriOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "hr", "JDOrepOTPvri", "",
     "", "10760", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "520", "538.65", "", "Customize",
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
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes",
     "440"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "40", "10800", "0", "", "", ""};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "100", "400", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_artikla;
  private String[] LabelNaziv_artiklaProps = new String[] {"Naziv artikla", "", "2160", "100",
     "3840", "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "7960", "100", "1360", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "6020", "100", "520", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "420", "100", "1720", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "6560", "100", "1380",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelVrijednost;
  private String[] LabelVrijednostProps = new String[] {"Vrijednost", "", "9340", "100", "1480",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "360", "10800", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "400"};
  public raReportElement TextIRAZ;
  private String[] TextIRAZProps = new String[] {"IRAZ", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9360", "", "1460", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextZC;
  private String[] TextZCProps = new String[] {"ZC", "", "", "Number|false|1|309|2|2|true|3|false",
     "", "", "", "", "7980", "", "1340", "220", "", "", "", "", "", "", "Lucida Bright", "8", "",
     "", "", "Right", ""};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "440", "",
     "1700", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "6060", "", "480",
     "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "",
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "6580", "", "1360", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "2180", "",
     "3820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "400",
     "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"BRDOK", "After", "", "", "Yes", "", "Yes",
     "1180"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "20", "10800", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "80", "10820", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"IRAZ\")", "", "",
     "Number|false|1|309|2|2|true|3|true", "", "", "", "", "9360", "100", "1460", "220", "",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement LabelU_K_U_P_N_O;
  private String[] LabelU_K_U_P_N_OProps = new String[] {"U K U P N O", "", "", "100", "1460",
     "220", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "340", "10800", "0", "", "", ""};
  public raReportSection PageFooter;
  public raReportSection ReportFooter;
  private String[] ReportFooterProps = new String[] {"", "", "", "No", "Yes", "", "", "0"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "",
     "", "20", "40", "40", "", "", "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "",
     "2400", "100", "700", "680", "", "", "", "", "", "", "", "", "", "", "", "", ""};

  public repOTPvriOrigTemplate() {
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

  public abstract raReportSection createPageHeader();

  public abstract raReportSection createSectionHeader0();

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    Line1 = sect.addModel(ep.LINE, Line1Props);
    LabelRbr = sect.addModel(ep.LABEL, LabelRbrProps);
    LabelNaziv_artikla = sect.addModel(ep.LABEL, LabelNaziv_artiklaProps);
    LabelCijena = sect.addModel(ep.LABEL, LabelCijenaProps);
    LabelJmj = sect.addModel(ep.LABEL, LabelJmjProps);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelKolicina = sect.addModel(ep.LABEL, LabelKolicinaProps);
    LabelVrijednost = sect.addModel(ep.LABEL, LabelVrijednostProps);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextIRAZ = sect.addModel(ep.TEXT, TextIRAZProps);
    TextZC = sect.addModel(ep.TEXT, TextZCProps);
    TextCART = sect.addModel(ep.TEXT, TextCARTProps);
    TextJM = sect.addModel(ep.TEXT, TextJMProps);
    TextKOL = sect.addModel(ep.TEXT, TextKOLProps);
    TextNAZART = sect.addModel(ep.TEXT, TextNAZARTProps);
    TextRBR = sect.addModel(ep.TEXT, TextRBRProps);
    return sect;
  }
  

  public raReportElement TextNAPOMENAOPIS;
  private String[] TextNAPOMENAOPISProps = new String[] {"NAPOMENAOPIS", "", "", "", "", "", "Yes",
     "Yes", "", "500", "10580", "680", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "", ""};

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Line3 = sect.addModel(ep.LINE, Line3Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    LabelU_K_U_P_N_O = sect.addModel(ep.LABEL, LabelU_K_U_P_N_OProps);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    TextNAPOMENAOPIS = sect.addModel(ep.TEXT, TextNAPOMENAOPISProps);
    return sect;
  }

  public abstract raReportSection createPageFooter();

  public raReportSection createReportFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_FOOTER), ReportFooterProps);

    Text3 = sect.addModel(ep.TEXT, Text3Props);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
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
