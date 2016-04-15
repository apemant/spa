package hr.restart.robno;

import hr.restart.util.reports.raElixirProperties;
import hr.restart.util.reports.raElixirPropertiesInstance;
import hr.restart.util.reports.raIzlazSectionFooterForCustom;
import hr.restart.util.reports.raReportElement;
import hr.restart.util.reports.raReportSection;
import hr.restart.util.reports.raReportTemplate;
import sg.com.elixir.reportwriter.xml.ModelFactory;

public abstract class repMeiOrigTemplate extends raReportTemplate {
  public raElixirProperties ep = raElixirPropertiesInstance.get();

  public raReportSection ReportTemplate;
  private String[] ReportTemplateProps = new String[] {"Report Template", "", "JDOrepMei", "", "", 
     "11520", "", "", "", "", "1.50"};
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
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(create-var-cache)", "", "", "", "", "", "", "", 
     "20", "", "1540", "320", "", "", "", "", "", "Red", "", "", "Bold", "", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(create-var-cache)", "", "", "", "", "", "", "", 
     "20", "", "1540", "320", "", "", "", "", "", "Red", "", "", "Bold", "", "", "", ""};
  public raReportSection PageHeader;
  public raReportSection SectionHeader0;
  private String[] SectionHeader0Props = new String[] {"BRDOK", "Before", "", "", "Yes", "", "Yes", 
     "", "2320"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "", 
     "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextFirstLine1;
  private String[] TextFirstLine1Props = new String[] {"FirstLine", "", "", "", "", "", "", "", "", 
     "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "", "", 
     "240", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine1;
  private String[] TextSecondLine1Props = new String[] {"SecondLine", "", "", "", "", "", "", "", 
     "", "240", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement LabelDatum_ispisa_;
  private String[] LabelDatum_ispisa_Props = new String[] {"Datum ispisa :", "", "8220", "240", 
     "1460", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextDatumIsp;
  private String[] TextDatumIspProps = new String[] {"DatumIsp", "", "", "", "", "", "", "", "9680", 
     "240", "1120", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "", 
     "480", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextThirdLine1;
  private String[] TextThirdLine1Props = new String[] {"ThirdLine", "", "", "", "", "", "", "", "", 
     "480", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement LabelMEDjUSKLADISNICA__IZLAZ;
  private String[] LabelMEDjUSKLADISNICA__IZLAZProps = new String[] {"ME\u0110USKLADIŠNICA - IZLAZ", 
     "", "7040", "1140", "4100", "320", "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", 
     "", "Center"};
  public raReportElement LabelIzlazno_skladiste;
  private String[] LabelIzlazno_skladisteProps = new String[] {"Izlazno skladišæe", "", "", "1140", 
     "", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextCSKLIZ;
  private String[] TextCSKLIZProps = new String[] {"CSKLIZ", "", "", "", "", "", "", "", "1480", 
     "1140", "840", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZCSKLIZ;
  private String[] TextNAZCSKLIZProps = new String[] {"NAZCSKLIZ", "", "", "", "", "", "", "", 
     "2340", "1140", "4060", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportElement TextCSKLUL;
  private String[] TextCSKLULProps = new String[] {"CSKLUL", "", "", "", "", "", "", "", "1480", 
     "1380", "840", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelUlazno_skladiste;
  private String[] LabelUlazno_skladisteProps = new String[] {"Ulazno skladište", "", "", "1380", 
     "", "240", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
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
  public raReportElement TextSgetDATDOK;
  private String[] TextSgetDATDOKProps = new String[] {"SgetDATDOK", "", "", "", "", "", "", "", 
     "1480", "1620", "1060", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportElement LabelDatum;
  private String[] LabelDatumProps = new String[] {"Datum", "", "", "1620", "", "240", "", "", "", 
     "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "7280", "1800", "3500", "0", "", "", ""};
  public raReportSection SectionHeader1;
  private String[] SectionHeader1Props = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", 
     "560"};
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "", "120", "10820", "0", "", "", ""};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "8980", "160", "1860", 
     "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRb;
  private String[] LabelRbProps = new String[] {"Rb", "", "", "160", "500", "240", "Normal", "Gray", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "520", "160", "1560", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv", "", "2100", "160", "6120", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "8240", "160", "720", "240", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "", "420", "10820", "0", "", "", ""};
  public raReportSection Detail;
  private String[] DetailProps = new String[] {"", "", "", "", "", "", "", "680"};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "540", "", 
     "1540", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "500", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "2140", 
     "", "6060", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "8240", "", "720", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", ""};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "9000", "", "1840", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
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

  public repMeiOrigTemplate() {
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
    Text2 = sect.addModel(ep.TEXT, Text2Props);
    return sect;
  }

  public raReportSection createPageHeader() {
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }

  public raReportSection createSectionHeader0() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 0), SectionHeader0Props);

    TextFirstLine = sect.addModel(ep.TEXT, TextFirstLineProps);
    TextFirstLine1 = sect.addModel(ep.TEXT, TextFirstLine1Props);
    TextSecondLine = sect.addModel(ep.TEXT, TextSecondLineProps);
    TextSecondLine1 = sect.addModel(ep.TEXT, TextSecondLine1Props);
    LabelDatum_ispisa_ = sect.addModel(ep.LABEL, LabelDatum_ispisa_Props);
    TextDatumIsp = sect.addModel(ep.TEXT, TextDatumIspProps);
    TextThirdLine = sect.addModel(ep.TEXT, TextThirdLineProps);
    TextThirdLine1 = sect.addModel(ep.TEXT, TextThirdLine1Props);
    LabelMEDjUSKLADISNICA__IZLAZ = sect.addModel(ep.LABEL, LabelMEDjUSKLADISNICA__IZLAZProps);
    LabelIzlazno_skladiste = sect.addModel(ep.LABEL, LabelIzlazno_skladisteProps);
    TextCSKLIZ = sect.addModel(ep.TEXT, TextCSKLIZProps);
    TextNAZCSKLIZ = sect.addModel(ep.TEXT, TextNAZCSKLIZProps);
    TextCSKLUL = sect.addModel(ep.TEXT, TextCSKLULProps);
    LabelUlazno_skladiste = sect.addModel(ep.LABEL, LabelUlazno_skladisteProps);
    TextNAZCSKLUL = sect.addModel(ep.TEXT, TextNAZCSKLULProps);
    Line1 = sect.addModel(ep.LINE, Line1Props);
    Line2 = sect.addModel(ep.LINE, Line2Props);
    Line3 = sect.addModel(ep.LINE, Line3Props);
    Label1 = sect.addModel(ep.LABEL, Label1Props);
    TextFormatBroj = sect.addModel(ep.TEXT, TextFormatBrojProps);
    LabelBroj = sect.addModel(ep.LABEL, LabelBrojProps);
    TextSgetDATDOK = sect.addModel(ep.TEXT, TextSgetDATDOKProps);
    LabelDatum = sect.addModel(ep.LABEL, LabelDatumProps);
    Line4 = sect.addModel(ep.LINE, Line4Props);
    return sect;
  }

  public raReportSection createSectionHeader1() {
    raReportSection sect = new raReportSection(template.getModel(ep.SECTION_HEADER + 1), SectionHeader1Props);

    Line5 = sect.addModel(ep.LINE, Line5Props);
    LabelKolicina = sect.addModel(ep.LABEL, LabelKolicinaProps);
    LabelRb = sect.addModel(ep.LABEL, LabelRbProps);
    LabelSifra = sect.addModel(ep.LABEL, LabelSifraProps);
    LabelNaziv = sect.addModel(ep.LABEL, LabelNazivProps);
    LabelJmj = sect.addModel(ep.LABEL, LabelJmjProps);
    Line6 = sect.addModel(ep.LINE, Line6Props);
    return sect;
  }

  public raReportSection createDetail() {
    raReportSection sect = new raReportSection(template.getModel(ep.DETAIL), DetailProps);

    TextCART = sect.addModel(ep.TEXT, TextCARTProps);
    TextRBR = sect.addModel(ep.TEXT, TextRBRProps);
    TextNAZART = sect.addModel(ep.TEXT, TextNAZARTProps);
    TextJM = sect.addModel(ep.TEXT, TextJMProps);
    TextKOL = sect.addModel(ep.TEXT, TextKOLProps);
    return sect;
  }

  public raReportSection createSectionFooter0() {
    return new raIzlazSectionFooterForCustom(this);
  }

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
