package hr.restart.robno;

import sg.com.elixir.*;
import sg.com.elixir.reportwriter.xml.*;

import hr.restart.util.reports.*;

public abstract class repMesklaSpecOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "hr_HR", "JDOrepMeskla", 
     "", "", "10740", "", "", "", "", "1.50"};
  public raReportSection PageSetup;
  private String[] PageSetupProps = new String[] {"340", "567.0", "560", "567.0", "", "Customize", 
     "11960", "16820", "", "", "0.0", "0.0", ""};
  public raReportSection Sections;
  public raReportElement Section0;
  private String[] Section0Props = new String[] {"BRDOK", "", "Yes", "Yes", "", "", ""};
  public raReportElement Section1;
  private String[] Section1Props = new String[] {"BRDOK", "", "Yes", "", "", "", ""};
  public raReportElement Section2;
  private String[] Section2Props = new String[] {"RBR", "", "", "", "", "", ""};
  public raReportSection ReportHeader;
  private String[] ReportHeaderProps = new String[] {"", "", "", "", "Yes", "", "", "0"};
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"BRDOK", "Before", "", "", "Yes", "", "Yes", 
     "", "2100"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "", 
     "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextFirstLine1;
  private String[] TextFirstLine1Props = new String[] {"FirstLine", "", "", "", "", "", "", "", "", 
     "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "", "", 
     "240", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "8220", "240", 
     "1460", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9680", 
     "240", "1120", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextSecondLine1;
  private String[] TextSecondLine1Props = new String[] {"SecondLine", "", "", "", "", "", "", "", 
     "", "240", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "", 
     "480", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextThirdLine1;
  private String[] TextThirdLine1Props = new String[] {"ThirdLine", "", "", "", "", "", "", "", "", 
     "480", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement LabelMEDjUSKLADISNICA;
  private String[] LabelMEDjUSKLADISNICAProps = new String[] {"MEÐUSKLADIŠNICA", "", "7040", 
     "1140", "4100", "320", "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "Center"};
  public raReportElement LabelIzlazno_skladiste;
  private String[] LabelIzlazno_skladisteProps = new String[] {"Izlazno skladište", "", "", "1140", 
     "", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextCSKLIZ;
  private String[] TextCSKLIZProps = new String[] {"CSKLIZ", "", "", "", "", "", "", "", "1480", 
     "1140", "840", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZCSKLIZ;
  private String[] TextNAZCSKLIZProps = new String[] {"NAZCSKLIZ", "", "", "", "", "", "", "", 
     "2340", "1140", "4060", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportElement LabelUlazno_skladiste;
  private String[] LabelUlazno_skladisteProps = new String[] {"Ulazno skladište", "", "", "1380", 
     "", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextCSKLUL;
  private String[] TextCSKLULProps = new String[] {"CSKLUL", "", "", "", "", "", "", "", "1480", 
     "1380", "840", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZCSKLUL;
  private String[] TextNAZCSKLULProps = new String[] {"NAZCSKLUL", "", "", "", "", "", "", "", 
     "2340", "1380", "4060", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "7280", "1480", "0", "320", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "10780", "1480", "0", "320", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "7280", "1480", "3500", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "7320", "1520", "3440", "260", "Normal", 
     "Light Gray", "Solid", "Light Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement TextFormatBroj;
  private String[] TextFormatBrojProps = new String[] {"FormatBroj", "", "", "", "", "", "", "", 
     "7280", "1540", "3520", "240", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", 
     "Center", "No"};
  public raReportElement LabelBroj;
  private String[] LabelBrojProps = new String[] {"Broj", "", "6640", "1560", "580", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement LabelDatum;
  private String[] LabelDatumProps = new String[] {"Datum", "", "", "1620", "", "240", "", "", "", 
     "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextSgetDATDOK;
  private String[] TextSgetDATDOKProps = new String[] {"SgetDATDOK", "", "", "", "", "", "", "", 
     "1480", "1620", "1060", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "7280", "1800", "3500", "0", "", "", ""};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", 
     "480"};
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "", "60", "10780", "0", "", "", ""};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv", "", "3060", "100", "3880", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "500", "100", "1260", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "6960", "100", "560", "240", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "100", "480", "240", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKol;
  private String[] LabelKolProps = new String[] {"Kol", "", "7540", "100", "780", "240", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "9920", "100", "880", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena1;
  private String[] LabelCijena1Props = new String[] {"Cijena", "", "8340", "100", "880", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPop_;
  private String[] LabelPop_Props = new String[] {"Pop %", "", "9240", "100", "660", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelBarcode;
  private String[] LabelBarcodeProps = new String[] {"Barcode", "", "1780", "100", "1260", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "", "360", "10780", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "300"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "3060", 
     "", "3880", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "500", "", 
     "1260", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextFC;
  private String[] TextFCProps = new String[] {"FC", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "8340", "", "880", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", 
     "", "Right", ""};
  public raReportElement TextFCP;
  private String[] TextFCPProps = new String[] {"FCP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9920", "", "880", "200", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "20", "", "440", 
     "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPPOP;
  private String[] TextPPOPProps = new String[] {"PPOP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9240", "", "660", "200", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "7540", "", "780", "200", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "6960", "", "560", 
     "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", ""};
  public raReportElement TextBC;
  private String[] TextBCProps = new String[] {"BC", "", "", "", "", "", "", "", "1780", "", "1260", 
     "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportSection SectionFooter0;
  public raReportSection PageFooter;
  private String[] PageFooterProps = new String[] {"", "", "240"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {
     "=(string-append \"Stranica \" (page) \" od \" (pages))", "", "", "", "", "", "", "", "8120", 
     "", "2700", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportSection ReportFooter;
  private String[] ReportFooterProps = new String[] {"", "", "", "", "Yes", "", "", "0"};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(destroy-var-cache)", "", "", "", "", "", "", "", 
     "2380", "120", "700", "680", "", "", "", "", "", "", "", "", "", "", "", "", ""};

  public repMesklaSpecOrigTemplate() {
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
    return sect;
  }

  public abstract raReportSection createPageHeader();

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextFirstLine1 = sect.addModel(ep.TEXT, TextFirstLine1Props);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    LabelDatum_ispisa_ = sect.addModel(ep.LABEL, LabelDatum_ispisa_Props);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    TextSecondLine1 = sect.addModel(ep.TEXT, TextSecondLine1Props);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextThirdLine1 = sect.addModel(ep.TEXT, TextThirdLine1Props);
    LabelMEDjUSKLADISNICA = sect.addModel(ep.LABEL, LabelMEDjUSKLADISNICAProps);
    LabelIzlazno_skladiste = sect.addModel(ep.LABEL, LabelIzlazno_skladisteProps);
    TextCSKLIZ = sect.addModel(ep.TEXT, TextCSKLIZProps);
    TextNAZCSKLIZ = sect.addModel(ep.TEXT, TextNAZCSKLIZProps);
    LabelUlazno_skladiste = sect.addModel(ep.LABEL, LabelUlazno_skladisteProps);
    TextCSKLUL = sect.addModel(ep.TEXT, TextCSKLULProps);
    TextNAZCSKLUL = sect.addModel(ep.TEXT, TextNAZCSKLULProps);
    Line1 = sect.addModel(ep.LINE, Line1Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    Line3 = sect.addModel(ep.LINE, Line3Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    TextFormatBroj = sect.addModel(ep.TEXT, TextFormatBrojProps);
    LabelBroj = sect.addModel(ep.LABEL, LabelBrojProps);
    LabelDatum = sect.addModel(ep.LABEL, LabelDatumProps);
    TextSgetDATDOK = sect.addModel(ep.TEXT, TextSgetDATDOKProps);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    Line5 = sect.addModel(ep.LINE, Line5Props);
    LabelNaziv = sect.addModel(ep.LABEL, LabelNazivProps);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelJmj = sect.addModel(ep.LABEL, LabelJmjProps);
    LabelRbr = sect.addModel(ep.LABEL, LabelRbrProps);
    LabelKol = sect.addModel(ep.LABEL, LabelKolProps);
    LabelCijena = sect.addModel(ep.LABEL, LabelCijenaProps);
    LabelCijena1 = sect.addModel(ep.LABEL, LabelCijena1Props);
    LabelPop_ = sect.addModel(ep.LABEL, LabelPop_Props);
    LabelBarcode = sect.addModel(ep.LABEL, LabelBarcodeProps);
    Line6 = sect.addModel(ep.LINE, Line6Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextNAZART = sect.addModel(ep.TEXT, TextNAZARTProps);
    TextCART = sect.addModel(ep.TEXT, TextCARTProps);
    TextFC = sect.addModel(ep.TEXT, TextFCProps);
    TextFCP = sect.addModel(ep.TEXT, TextFCPProps);
    TextRBR = sect.addModel(ep.TEXT, TextRBRProps);
    TextPPOP = sect.addModel(ep.TEXT, TextPPOPProps);
    TextKOL = sect.addModel(ep.TEXT, TextKOLProps);
    TextJM = sect.addModel(ep.TEXT, TextJMProps);
    TextBC = sect.addModel(ep.TEXT, TextBCProps);
    return sect;
  }

  public abstract raReportSection createSectionFooter0();

  public raReportSection createPageFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.PAGE_FOOTER), PageFooterProps);

    Text3 = sect.addModel(ep.TEXT, Text3Props);
    return sect;
  }

  public raReportSection createReportFooter() {
    raReportSection sect = new raReportSection(template.getModel(ep.REPORT_FOOTER), ReportFooterProps);

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
