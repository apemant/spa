/****license*****************************************************************
**   file: repPopListaOrigTemplate.java
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
import hr.restart.util.reports.raStandardReportHeader;
import sg.com.elixir.reportwriter.xml.ModelFactory;

public abstract class repPopListaOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepPopLista", "",
     "", "10600", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "", "", "11880",
     "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"CSKL", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"CSKL", "", "Yes", "", "", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"SORTER", "", "", "", "", "", ""};
  public raReportSection ReportHeader;
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"CSKL", "", "", "", "Yes", "", "Yes", "",
     "1740"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60",
     "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextFirstLine1;
  private String[] TextFirstLine1Props = new String[] {"FirstLine", "", "", "", "", "", "", "",
     "60", "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9620",
     "180", "1120", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "8160", "180",
     "1460", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
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
  public raReportElement LabelPOPISNA_LISTA_ZA_INVENTURU;
  private String[] LabelPOPISNA_LISTA_ZA_INVENTURUProps = new String[] {
     "\nPOPISNA LISTA ZA INVENTURU", "", "", "580", "10740", "640", "", "", "", "", "", "",
     "Lucida Bright", "14", "Bold", "", "", "Center"};
  public raReportElement TextCSKL;
  private String[] TextCSKLProps = new String[] {"CSKL", "", "", "", "", "", "", "", "920", "1460",
     "840", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZSKL;
  private String[] TextNAZSKLProps = new String[] {"NAZSKL", "", "", "", "", "", "", "", "1780",
     "1460", "4520", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextDatum;
  private String[] TextDatumProps = new String[] {"Datum", "", "", "", "", "", "", "", "9620",
     "1460", "1120", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelSkladiste;
  private String[] LabelSkladisteProps = new String[] {"Skladište", "", "", "1460", "940", "200",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement LabelDatum_inventure;
  private String[] LabelDatum_inventureProps = new String[] {"Datum inventure", "", "7640", "1460",
     "1980", "200", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportSection SectionHeader1;
  public raReportSection Detail;
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"CSKL", "", "", "", "Yes", "", "Yes", "2680"};
  public raReportElement LabelInventurna_komisija_;
  private String[] LabelInventurna_komisija_Props = new String[] {"Inventurna komisija :", "", "",
     "840", "10740", "280", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label1_2_3;
  private String[] Label1_2_3Props = new String[] {
     "\n\n\n\n1.                                                             \n\n\n2.                                                             \n\n\n3.                                                             ",
     "", "", "840", "10740", "1340", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Center"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "3660", "1400", "3660", "0", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "3660", "1760", "3660", "0", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "3660", "2120", "3660", "0", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "240"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "8020",
     "", "2700", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;

  public repPopListaOrigTemplate() {
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

  public raReportSection createReportHeader(){
    return new raStandardReportHeader(this);
  }

  public abstract raReportSection createPageHeader();

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextFirstLine1 = sect.addModel(ep.TEXT, TextFirstLine1Props);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    LabelDatum_ispisa_ = sect.addModel(ep.LABEL, LabelDatum_ispisa_Props);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    TextSecondLine1 = sect.addModel(ep.TEXT, TextSecondLine1Props);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextThirdLine1 = sect.addModel(ep.TEXT, TextThirdLine1Props);
    LabelPOPISNA_LISTA_ZA_INVENTURU = sect.addModel(ep.LABEL, LabelPOPISNA_LISTA_ZA_INVENTURUProps);
    TextCSKL = sect.addModel(ep.TEXT, TextCSKLProps);
    TextNAZSKL = sect.addModel(ep.TEXT, TextNAZSKLProps);
    TextDatum = sect.addModel(ep.TEXT, TextDatumProps);
    LabelSkladiste = sect.addModel(ep.LABEL, LabelSkladisteProps);
    LabelDatum_inventure = sect.addModel(ep.LABEL, LabelDatum_inventureProps);
    return sect;
  }

  public abstract raReportSection createSectionHeader1();

  public abstract raReportSection createDetail();

  public raReportSection createSectionFooter0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);

    LabelInventurna_komisija_ = sect.addModel(ep.LABEL, LabelInventurna_komisija_Props);
    Label1_2_3 = sect.addModel(ep.LABEL, Label1_2_3Props);
    Line1 = sect.addModel(ep.LINE, Line1Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    Line3 = sect.addModel(ep.LINE, Line3Props);
    return sect;
  }

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text1 = sect.addModel(ep.TEXT, Text1Props);
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
    SectionHeader0 = addSection(createSectionHeader0());
    SectionHeader1 = addSection(createSectionHeader1());
    Detail = addSection(createDetail());
    SectionFooter0 = addSection(createSectionFooter0());
    PageFooter = addSection(createPageFooter());
//    ReportFooter = addSection(createReportFooter());
  }
}
