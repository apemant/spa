package hr.restart.util.reports;

public class raDosGetroDetail extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "540"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "8320", "", "760", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", "No"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(if (= [ImaPor2] 1) (put-var \"tpor2\" 1) \"\")", 
     "", "", "", "No", "", "", "", "2640", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(if (= [ImaPor3] 1) (put-var \"tpor3\" 1) \"\")", 
     "", "", "", "No", "", "", "", "3120", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "380", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(if (= [ImaPor1] 1) (put-var \"tpor1\" 1) \"\")", 
     "", "", "", "No", "", "", "", "2300", "", "0", "0", "", "", "", "", "", "", "", "", "", "", "", 
     "", ""};
  public raReportElement TextBCART;
  private String[] TextBCARTProps = new String[] {"BCART", "", "", "", "", "", "", "", "2060", "", 
     "1400", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextCARTPAR;
  private String[] TextCARTPARProps = new String[] {"CARTPAR", "", "", "", "", "", "", "", "440", 
     "", "860", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "1320", "", 
     "720", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZARTPAR;
  private String[] TextNAZARTPARProps = new String[] {"NAZARTPAR", "", "", "", "", "", "Yes", "", 
     "3480", "", "4820", "460", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "9100", "", "1020", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextAKT;
  private String[] TextAKTProps = new String[] {"AKT", "", "", "", "", "", "", "", "10140", "120", 
     "680", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZARText;
  private String[] TextNAZARTextProps = new String[] {"NAZARText", "", "", "", "", "", "Yes", "Yes", 
     "1300", "220", "8100", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextBCPAK;
  private String[] TextBCPAKProps = new String[] {"BCPAK", "", "", "", "", "", "", "", "2060", 
     "240", "1400", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "500", "10820", "0", "", "", ""};

  public raDosGetroDetail(raReportTemplate owner) {
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
    TextJM = addModel(ep.TEXT, TextJMProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    Text3 = addModel(ep.TEXT, Text3Props);
    TextBCART = addModel(ep.TEXT, TextBCARTProps);
    TextCARTPAR = addModel(ep.TEXT, TextCARTPARProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextNAZARTPAR = addModel(ep.TEXT, TextNAZARTPARProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextAKT = addModel(ep.TEXT, TextAKTProps);
    TextNAZARText = addModel(ep.TEXT, TextNAZARTextProps);
    TextBCPAK = addModel(ep.TEXT, TextBCPAKProps);
    Line1 = addModel(ep.LINE, Line1Props);
  }

  private void modifyThis() {
  }
}
