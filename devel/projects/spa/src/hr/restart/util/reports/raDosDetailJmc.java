package hr.restart.util.reports;

import hr.restart.robno.Aut;
import hr.restart.sisfun.frmParam;

public class raDosDetailJmc extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "320"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "7000", "", "620", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", "No"};
  public raReportElement TextSSIF;
  private String[] TextSSIFProps = new String[] {"SSIF", "", "", "", "", "", "", "", "1840", "", 
     "1280", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "", 
     "1300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "20", "", "440", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextKOL2;
  private String[] TextKOL2Props = new String[] {"KOL2", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "8540", "", "1140", "220", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "No", "", "3140", 
     "", "3840", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"=(if (> [KOL] 0) [KOL] \"\")", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "9700", "", "1140", "220", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextKOL1;
  private String[] TextKOL1Props = new String[] {"KOL1", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "7640", "", "880", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};

  public raDosDetailJmc(raReportTemplate owner) {
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
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextSSIF = addModel(ep.TEXT, TextSSIFProps);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextKOL2 = addModel(ep.TEXT, TextKOL2Props);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextKOL1 = addModel(ep.TEXT, TextKOL1Props);
  }

  private void modifyThis() {
    this.restoreDefaults();
    this.resizeElement(this.TextCART, Aut.getAut().getIzlazCARTwidth(), this.TextNAZART);
    String id = frmParam.getParam("robno", "ispDOS", "D",
    "Ispisati isporuèenu kolièinu na dostavnici (D,N)?");
    if (id.equalsIgnoreCase("N"))
      TextKOL.setControlSource("");
  }
}
