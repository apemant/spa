/****license*****************************************************************
**   file: repOTPOrigTemplate.java
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

public abstract class repOTPOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "hr", "JDOrepOTP", "", "",
     "10780", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "520", "538.65", "", "Customize",
     "11900", "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"FormatBroj", "", "Yes", "Yes", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"FormatBroj", "", "Yes", "", "Each Value", "", ""};
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
  private String[] Line1Props = new String[] {"", "", "20", "40", "10800", "0", "", "", ""};
  public raReportElement LineD;
  private String[] LineDProps = new String[] {"", "", "20", "40", "10800", "0", "", "", ""};
  
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "8420", "100", "920", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "9360", "100", "1480",
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "480", "100", "1940", "220",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_artikla;
  private String[] LabelNaziv_artiklaProps = new String[] {"Naziv artikla", "", "2440", "100",
     "5960", "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "20", "100", "440", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "20", "360", "10800", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "240"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "8420", "", "920",
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "",
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "9360", "", "1480", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "20", "", "440",
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "2440", "",
     "5960", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextNAZARText;
  private String[] TextNAZARTextProps = new String[] {"NAZARText", "", "", "", "", "", "Yes", "Yes", "2440", "220",
     "5960", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "Yes"};
  
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "",
     "1940", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"BRDOK", "After", "", "", "Yes", "", "Yes",
     "360"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "7900", "", "1700", "100", "", "", "", "",
     "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "1360", "", "1220", "100", "", "", "", "",
     "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportSection PageFooter;
  public raReportSection ReportFooter;
  private String[] ReportFooterProps = new String[] {"", "", "", "No", "Yes", "", "", "0"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "",
     "", "20", "40", "40", "", "", "", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "",
     "2400", "100", "700", "680", "", "", "", "", "", "", "", "", "", "", "", "", ""};

  public repOTPOrigTemplate() {
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
    LabelJmj = sect.addModel(ep.LABEL, LabelJmjProps);
    LabelKolicina = sect.addModel(ep.LABEL, LabelKolicinaProps);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelNaziv_artikla = sect.addModel(ep.LABEL, LabelNaziv_artiklaProps);
    LabelRbr = sect.addModel(ep.LABEL, LabelRbrProps);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextJM = sect.addModel(ep.TEXT, TextJMProps);
    TextKOL = sect.addModel(ep.TEXT, TextKOLProps);
    TextRBR = sect.addModel(ep.TEXT, TextRBRProps);
    TextNAZART = sect.addModel(ep.TEXT, TextNAZARTProps);
    TextNAZARText = sect.addModel(ep.TEXT, TextNAZARTextProps);
    TextCART = sect.addModel(ep.TEXT, TextCARTProps);
    return sect;
  }
  

  public raReportElement TextNAPOMENAOPIS;
  private String[] TextNAPOMENAOPISProps = new String[] {"NAPOMENAOPIS", "", "", "", "", "", "Yes",
     "Yes", "", "200", "10580", "680", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "", ""};

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    LineD = sect.addModel(ep.LINE, LineDProps);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    TextNAPOMENAOPIS = sect.addModel(ep.TEXT, TextNAPOMENAOPISProps);
    return sect;
  }

  public abstract raReportSection createPageFooter();

  public raReportSection createReportFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_FOOTER), ReportFooterProps);

    Text2 = sect.addModel(ep.TEXT, Text2Props);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
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
