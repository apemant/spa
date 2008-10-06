package hr.restart.util.reports;

public class raRacGetroDetail extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "520"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(if (= [ImaPor2] 1) (put-var \"tpor2\" 1) \"\")", 
     "", "", "", "No", "", "", "", "2640", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement TextVNAK;
  private String[] TextVNAKProps = new String[] {"VNAK", "", "", 
     "Number|false|1|3|2|2|true|3|false", "", "", "", "", "9420", "", "800", "220", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(if (= [ImaPor3] 1) (put-var \"tpor3\" 1) \"\")", 
     "", "", "", "No", "", "", "", "3120", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(if (= [ImaPor1] 1) (put-var \"tpor1\" 1) \"\")", 
     "", "", "", "No", "", "", "", "2300", "", "0", "0", "", "", "", "", "", "", "", "", "", "", "", 
     "", ""};
  public raReportElement TextBCART;
  private String[] TextBCARTProps = new String[] {"BCART", "", "", "", "", "", "", "", "1800", "", 
     "1280", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextSTRANASIFRA;
  private String[] TextSTRANASIFRAProps = new String[] {"CARTPAR", "", "", "", "", "", "", "", 
     "400", "", "760", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", 
     "No"};
  public raReportElement TextPor1p2p3Naz;
  private String[] TextPor1p2p3NazProps = new String[] {"Por1p2p3Naz", "", "", 
     "Number|false|1|3|2|2|true|3|false", "", "", "", "", "7820", "", "760", "220", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "1180", "", 
     "600", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZARTPAR;
  private String[] TextNAZARTPARProps = new String[] {"NAZARTPAR", "", "", "", "", "", "Yes", "", 
     "3100", "", "3000", "460", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "6880", "", "920", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "6120", "", "740", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextUPRAB1;
  private String[] TextUPRAB1Props = new String[] {"UPRAB1", "", "", 
     "Number|false|1|3|2|2|true|3|false", "", "", "", "", "8600", "", "800", "220", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "380", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextAKT;
  private String[] TextAKTProps = new String[] {"AKT", "", "", "", "", "", "", "", "10240", "120", 
     "580", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZARText;
  private String[] TextNAZARTextProps = new String[] {"NAZARText", "", "", "", "", "", "Yes", "Yes", 
     "1300", "220", "8100", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextTNAK;
  private String[] TextTNAKProps = new String[] {"TNAK", "", "", 
     "Number|false|1|3|2|2|true|3|false", "", "", "", "", "9420", "240", "800", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextUIRAB;
  private String[] TextUIRABProps = new String[] {"UIRAB", "", "", 
     "Number|false|1|3|2|2|true|3|false", "", "", "", "", "8600", "240", "800", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(+ (+ [POR1] [POR2]) [POR3])", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7820", "240", "760", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextINETO;
  private String[] TextINETOProps = new String[] {"INETO", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6880", "240", "920", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextFC;
  private String[] TextFCProps = new String[] {"FC", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "6120", "240", "740", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextBCPAK;
  private String[] TextBCPAKProps = new String[] {"BCPAK", "", "", "", "", "", "", "", "1800", 
     "240", "1280", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "480", "10820", "0", "", "", ""};

  public raRacGetroDetail(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.DETAIL));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    Text1 = addModel(ep.TEXT, Text1Props);
    TextVNAK = addModel(ep.TEXT, TextVNAKProps);
    Text2 = addModel(ep.TEXT, Text2Props);
    Text3 = addModel(ep.TEXT, Text3Props);
    TextBCART = addModel(ep.TEXT, TextBCARTProps);
    TextSTRANASIFRA = addModel(ep.TEXT, TextSTRANASIFRAProps);
    TextPor1p2p3Naz = addModel(ep.TEXT, TextPor1p2p3NazProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextNAZARTPAR = addModel(ep.TEXT, TextNAZARTPARProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextUPRAB1 = addModel(ep.TEXT, TextUPRAB1Props);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextAKT = addModel(ep.TEXT, TextAKTProps);
    TextNAZARText = addModel(ep.TEXT, TextNAZARTextProps);
    TextTNAK = addModel(ep.TEXT, TextTNAKProps);
    TextUIRAB = addModel(ep.TEXT, TextUIRABProps);
    Text4 = addModel(ep.TEXT, Text4Props);
    TextINETO = addModel(ep.TEXT, TextINETOProps);
    TextFC = addModel(ep.TEXT, TextFCProps);
    TextBCPAK = addModel(ep.TEXT, TextBCPAKProps);
    Line1 = addModel(ep.LINE, Line1Props);
  }

  private void modifyThis() {
  }
}
