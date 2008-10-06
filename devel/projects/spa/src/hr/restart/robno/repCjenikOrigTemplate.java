/****license*****************************************************************
**   file: repCjenikOrigTemplate.java
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

public abstract class repCjenikOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepCjenik", "", 
     "", "10680", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"300", "380.2", "520", "538.65", "", "Customize", 
     "11900", "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"SECTION", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"SECTION", "", "Yes", "", "", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"CART", "", "", "", "", "", ""};
  public raReportSection ReportHeader;
  private String[] ReportHeaderProps = new String[] {"", "", "", "", "Yes", "", "", "0"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(create-var-cache)", "", "", "", "", "", "", "", 
     "40", "", "1560", "240", "", "", "", "", "", "Red", "", "8", "Bold", "", "", "", ""};
  public raReportSection PageHeader;
  private String[] PageHeaderProps = new String[] {"", "", "840"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60", 
     "", "5920", "260", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "", 
     "60", "240", "5920", "260", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", 
     ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60", 
     "480", "5920", "260", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9680", 
     "480", "1080", "260", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "8040", "480", 
     "1620", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"SECTION", "", "", "", "Yes", "", "Yes", "", 
     "1800"};
  public raReportElement LabelCJENIK;
  private String[] LabelCJENIKProps = new String[] {"CJENIK ", "", "20", "160", "10820", "360", "", 
     "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center"};
  public raReportElement TextNAZSKL;
  private String[] TextNAZSKLProps = new String[] {"NAZSKL", "", "", "", "", "", "", "", "2220", 
     "700", "5660", "260", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "", ""};
  public raReportElement TextCSKL;
  private String[] TextCSKLProps = new String[] {"CSKL", "", "", "", "", "", "", "", "1360", "700", 
     "840", "260", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(put-var \"rbr\" 1)", "", "", "", "No", "", "", "", 
     "8400", "700", "1080", "240", "", "", "", "", "", "", "", "8", "", "", "", "", ""};
  public raReportElement LabelSkladiste;
  private String[] LabelSkladisteProps = new String[] {"Skladište", "", "", "700", "1340", "260", 
     "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", ""};
  public raReportElement LabelPartner;
  private String[] LabelPartnerProps = new String[] {"Partner", "", "", "1000", "1340", "260", "", 
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", ""};
  public raReportElement TextNAZPAR;
  private String[] TextNAZPARProps = new String[] {"NAZPAR", "", "", "", "", "", "", "", "2220", 
     "1000", "5660", "260", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "", ""};
  public raReportElement TextCPAR;
  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "1360", "1000", 
     "840", "260", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"SECTION", "", "", "", "Yes", "", "", "Yes", 
     "740"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "120", "10820", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "10120", "160", "700", "480", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "", "160", "560", "480", "Normal", "Gray", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label3;
  private String[] Label3Props = new String[] {"", "", "8500", "160", "1600", "480", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label4;
  private String[] Label4Props = new String[] {"", "", "6880", "160", "1580", "480", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label5;
  private String[] Label5Props = new String[] {"", "", "6160", "160", "700", "480", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label6;
  private String[] Label6Props = new String[] {"", "", "2380", "160", "3760", "480", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label7;
  private String[] Label7Props = new String[] {"", "", "580", "160", "1780", "480", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement LabelCijena_s_porezom;
  private String[] LabelCijena_s_porezomProps = new String[] {"Cijena s porezom", "", "8500", "180", 
     "1600", "420", "", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena_bez_poreza;
  private String[] LabelCijena_bez_porezaProps = new String[] {" Cijena bez poreza", "", "6880", 
     "180", "1600", "420", "", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelJm;
  private String[] LabelJmProps = new String[] {"Jm", "", "6160", "280", "700", "240", "", "Gray", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelVal;
  private String[] LabelValProps = new String[] {"Valuta", "", "10120", "280", "700", "240", "", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "280", "560", "240", "", "Gray", "", 
     "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "580", "280", "1780", "240", "", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_artikla;
  private String[] LabelNaziv_artiklaProps = new String[] {"Naziv artikla", "", "2380", "280", 
     "3760", "240", "", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "660", "10820", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "No", "", "280"};
  public raReportElement TextVC;
  private String[] TextVCProps = new String[] {"VC", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "6880", "", "1600", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", 
     "", "", "Right", ""};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "6160", "", "700", 
     "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(get-var \"rbr\")", "", "", "", "", "", "", "", "", 
     "", "560", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextMC;
  private String[] TextMCProps = new String[] {"MC", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "8500", "", "1600", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", 
     "", "", "Right", ""};
  public raReportElement TextVAL;
  private String[] TextVALProps = new String[] {"VAL", "", "", "", "", "", "", "", "10160", "", 
     "660", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(put-var \"rbr\" (+ (get-var \"rbr\") 1))", "", "", 
     "", "No", "", "", "", "3740", "", "840", "140", "", "", "", "", "", "", "", "8", "", "", "", 
     "", ""};
  public raReportElement TextSIFRA;
  private String[] TextSIFRAProps = new String[] {"SIFRA", "", "", "", "", "", "", "", "580", "", 
     "1780", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "2380", "", 
     "3760", "220", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "240"};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {
     "=(if (> [Logo] 0) (string-append \"Stranica \" (page) \" od \" (pages)) \"\")", "", "", "", 
     "", "", "", "", "8120", "", "2700", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", ""};
  public raReportSection ReportFooter;
  private String[] ReportFooterProps = new String[] {"", "", "", "", "Yes", "", "", "0"};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "", 
     "1580", "60", "1620", "320", "", "", "", "", "", "", "", "8", "", "", "", "", ""};
  public raReportSection SectionFooter0;
  private String[] SectionFooter0Props = new String[] {"SECTION", "After", "", "", "Yes", "", "",
     "0"};
  
  public repCjenikOrigTemplate() {
    System.out.println("ITS NJU MI!!!");
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

  public raReportSection createPageHeader() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_HEADER), PageHeaderProps);

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    LabelDatum_ispisa_ = sect.addModel(ep.LABEL, LabelDatum_ispisa_Props);
    return sect;
  }

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    LabelCJENIK = sect.addModel(ep.LABEL, LabelCJENIKProps);
    TextNAZSKL = sect.addModel(ep.TEXT, TextNAZSKLProps);
    TextCSKL = sect.addModel(ep.TEXT, TextCSKLProps);
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    LabelSkladiste = sect.addModel(ep.LABEL, LabelSkladisteProps);
    LabelPartner = sect.addModel(ep.LABEL, LabelPartnerProps);
    TextNAZPAR = sect.addModel(ep.TEXT, TextNAZPARProps);
    TextCPAR = sect.addModel(ep.TEXT, TextCPARProps);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    Line1 = sect.addModel(ep.LINE, Line1Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    Label2 = sect.addModel(ep.LABEL, Label2Props);
    Label3 = sect.addModel(ep.LABEL, Label3Props);
    Label4 = sect.addModel(ep.LABEL, Label4Props);
    Label5 = sect.addModel(ep.LABEL, Label5Props);
    Label6 = sect.addModel(ep.LABEL, Label6Props);
    Label7 = sect.addModel(ep.LABEL, Label7Props);
    LabelCijena_s_porezom = sect.addModel(ep.LABEL, LabelCijena_s_porezomProps);
    LabelCijena_bez_poreza = sect.addModel(ep.LABEL, LabelCijena_bez_porezaProps);
    LabelJm = sect.addModel(ep.LABEL, LabelJmProps);
    LabelVal = sect.addModel(ep.LABEL, LabelValProps);
    LabelRbr = sect.addModel(ep.LABEL, LabelRbrProps);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelNaziv_artikla = sect.addModel(ep.LABEL, LabelNaziv_artiklaProps);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextVC = sect.addModel(ep.TEXT, TextVCProps);
    TextJM = sect.addModel(ep.TEXT, TextJMProps);
    Text3 = sect.addModel(ep.TEXT, Text3Props);
    TextMC = sect.addModel(ep.TEXT, TextMCProps);
    TextVAL = sect.addModel(ep.TEXT, TextVALProps);
    Text4 = sect.addModel(ep.TEXT, Text4Props);
    TextSIFRA = sect.addModel(ep.TEXT, TextSIFRAProps);
    TextNAZART = sect.addModel(ep.TEXT, TextNAZARTProps);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    return new raReportSection(template.getModel(ep.SECTION_FOOTER + 0), SectionFooter0Props);
  }

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text5 = sect.addModel(ep.TEXT, Text5Props);
    return sect;
  }

  public raReportSection createReportFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_FOOTER), ReportFooterProps);

    Text6 = sect.addModel(ep.TEXT, Text6Props);
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
