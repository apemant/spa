/****license*****************************************************************
**   file: repOdobrenjaOrigTemplate.java
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

public abstract class repOdobrenjaOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"", "hr", "JDOrepOdobrenja", "", "", "10860",
     "", "", "", "", ""};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"300", "380.2", "538.65", "538.65", "",
     "Customize", "11907.0", "16839.899999999998", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"BRDOK", "", "Yes", "Yes", "Each Value", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"BRDOK", "", "Yes", "", "Each Value", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"RBR", "", "", "", "Each Value", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "Yes",
     "420"};
  public raReportElement LabelIznos;
  private String[] LabelIznosProps = new String[] {"Iznos", "", "9240", "100", "1580", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "480", "100", "800", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNazivusluge;
  private String[] LabelNazivuslugeProps = new String[] {"Naziv usluge", "", "1300", "100", "7920",
     "220", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "20", "100", "440", "226.8", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "320"};
  public raReportElement TextIRAZ;
  private String[] TextIRAZProps = new String[] {"IRAZ", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9240", "", "1580", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "20", "", "440",
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "1300", "",
     "7920", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "",
     "800", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"BRDOK", "After", "", "", "Yes", "", "Yes",
     "3300"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "", "9220", "240", "Normal", "Gray",
    "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "9240", "0.0", "1580", "240", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement LabelUKUP;
  private String[] LabelUKUPProps = new String[] {"U K U P N O", "", "7720", "20", "1460", "200",
     "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement LabelSlovima;
  private String[] LabelSlovimaProps = new String[] {"Slovima :", "", "280", "580", "840", "220",
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelObradio;
  private String[] LabelObradioProps = new String[] {"Obradio", "", "1520", "2980", "1200", "283.5",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement LabelNapomena;
  private String[] LabelNapomenaProps = new String[] {"Napomena :", "", "280", "1000", "1304.1",
     "283.5", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelOdgovornaosoba;
  private String[] LabelOdgovornaosobaProps = new String[] {"Odgovorna osoba", "", "8060", "2980",
     "1701.0", "283.5", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IRAZ\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9240", "", "1580", "240", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "", "", "1140",
     "580", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextNAPOMENAOPIS;
  private String[] TextNAPOMENAOPISProps = new String[] {"NAPOMENAOPIS", "", "", "", "", "", "Yes",
     "Yes", "280", "1260", "10200", "1160", "", "", "", "", "", "", "Lucida Bright", "8", "", "",
     "", "", ""};
  public raReportSection PageFooter;
  public raReportSection ReportFooter;

  public repOdobrenjaOrigTemplate() {
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

  public abstract raReportSection createSectionHeader0();

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    LabelIznos = sect.addModel(ep.LABEL, LabelIznosProps);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelNazivusluge = sect.addModel(ep.LABEL, LabelNazivuslugeProps);
    LabelRbr = sect.addModel(ep.LABEL, LabelRbrProps);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextIRAZ = sect.addModel(ep.TEXT, TextIRAZProps);
    TextRBR = sect.addModel(ep.TEXT, TextRBRProps);
    TextNAZART = sect.addModel(ep.TEXT, TextNAZARTProps);
    TextCART = sect.addModel(ep.TEXT, TextCARTProps);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    Label1 = sect.addModel(ep.LABEL, Label1Props);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    LabelUKUP = sect.addModel(ep.LABEL, LabelUKUPProps);
    LabelSlovima = sect.addModel(ep.LABEL, LabelSlovimaProps);
    LabelObradio = sect.addModel(ep.LABEL, LabelObradioProps);
    LabelNapomena = sect.addModel(ep.LABEL, LabelNapomenaProps);
    LabelOdgovornaosoba = sect.addModel(ep.LABEL, LabelOdgovornaosobaProps);
    Text1 = sect.addModel(ep.TEXT, Text1Props);
    TextSLOVIMA = sect.addModel(ep.TEXT, TextSLOVIMAProps);
    TextNAPOMENAOPIS = sect.addModel(ep.TEXT, TextNAPOMENAOPISProps);
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
