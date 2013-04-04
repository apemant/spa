/****license*****************************************************************
**   file: raRPSectionHeader.java
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
package hr.restart.util.reports;

import hr.restart.robno.repIzlazni;
import hr.restart.sisfun.frmParam;

// by S.G.

public class raRPSectionHeader extends raReportSection {

  public raReportElement TextTEL;
  private String[] TextTELProps = new String[] {"TEL", "", "", "", "", "", "", "", "320", "2520", //"1900", 
      "6000", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};

  public raReportElement Rectangle1;
  private String[] Rectangle1Props = new String[] {"", "660", "240", "4180", "2200", "", "", "",
    "White", ""};
  public raReportElement Rectangle2;
  private String[] Rectangle2Props = new String[] {"", "320", "260", "4840", "2160", "Transparent",
    "", "", "", ""};
  public raReportElement Rectangle3;
  private String[] Rectangle3Props = new String[] {"", "300", "600", "4880", "1500", "", "", "",
    "White", ""};
  public raReportElement TextKONTOSOB;
  private String[] TextKONTOSOBProps = new String[] {"KONTOSOB", "", "", "", "", "", "Yes", "", "640",
    "1640", "4360", "480", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextCPAR;
  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "3940", p_getYzaKoloneUKucici(2120),
    "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};

  private String[] thisProps = new String[] {"BRDOK", "Before", "", "", "Yes", "", "Yes", "",
     "2640"};
  public raReportElement TextSgetDATDOK;
  private String[] TextSgetDATDOKProps = new String[] {"SgetDATDOK", "", "hr_HR",
     "Date|false|Short", "", "", "", "", "9600", "1040", "1240", "220", "", "", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextLogoMjestoZarez;
  private String[] TextLogoMjestoZarezProps = new String[] {"LogoMjestoZarez", "", "", "", "", "",
     "", "", "7280", "1040", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "", ""};
  public raReportElement LabelMjesto;
  private String[] LabelMjestoProps = new String[] {"Mjesto/datum izdavanja", "", "5760", "1040", "1440", "220", "",
     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "10760", "620", "0", "340", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "7240", "620", "0", "340", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "7240", "620", "3540", "0", "", "", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "7240", "940", "3540", "0", "", "", ""};
  public raReportElement TextNAZPAR;
  private String[] TextNAZPARProps = new String[] {"NAZPARL", "", "",
    "", "", "", "Yes", "Yes", "640", "200", "4360", "840", "", "", "", "", "", "", "Lucida Bright",
    "", "Bold", "", "", "", ""};
  public raReportElement LabelDospijece;
  private String[] LabelDospijeceProps = new String[] {"Dospije\u0107e", "", "5760", "1260",
    "1440", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextSgetDDOSP;
  private String[] TextSgetDDOSPProps = new String[] {"SgetDDOSP", "", "", "", "", "", "",
    "", "7280", "1260", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
    "", ""};
  public raReportElement TextSgetDATDOSP;
  private String[] TextSgetDATDOSPProps = new String[] {"SgetDATDOSP", "", "", "", "", "", "",
    "", "9600", "1260", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
    "Right", "No"};
  public raReportElement TextBRNARIZ;
  private String[] TextBRNARIZProps = new String[] {"BRNARIZ", "", "", "", "", "", "", "", "7280",
     "1480", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelNarudzba;
  private String[] LabelNarudzbaProps = new String[] {"Narudžba", "", "5760", "1480", "1440", "220",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextSgetDATNARIZ;
  private String[] TextSgetDATNARIZProps = new String[] {"SgetDATNARIZ", "", "", "", "", "", "",
    "", "9600", "1480", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
    "Right", "No"};
  public raReportElement TextADR;
  private String[] TextADRProps = new String[] {"ADR", "", "", "", "", "", "", "", "640", "1120",//"860",
    "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement LabelNacinplacanja;
  private String[] LabelNacinplacanjaProps = new String[] {"Na\u010Din pla\u0107anja", "", "5760", "1700",
     "1440", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZNACPL;
  private String[] TextNAZNACPLProps = new String[] {"NAZNACPL", "", "", "", "", "", "", "", "7280",
     "1700", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextMJ;
  private String[] TextMJProps = new String[] {"MJ", "", "", "", "", "", "", "", "640", "1380", //"1120",
    "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextPBR;
//  private String[] TextPBRProps = new String[] {"=(if (> [PBR] 0) [PBR] \"\")", "", "", "", "", "", "", "", "740", "1000",
//     "720", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextNAZNAC;
  private String[] TextNAZNACProps = new String[] {"NAZNAC", "", "", "", "", "", "", "", "7280",
     "1920", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelNacinotpreme;
  private String[] LabelNacinotpremeProps = new String[] {"Na\u010Din otpreme", "", "5760", "1920",
     "1440", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZFRA;
  private String[] TextNAZFRAProps = new String[] {"NAZFRA", "", "", "", "", "", "", "", "7280",
     "2140", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelParitet;
  private String[] LabelParitetProps = new String[] {"Paritet", "", "5760", "2140", "1440", "220",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement LabelR1;
  private String[] LabelR1Props = new String[] {"R-1", "", "9640"/*"10280"*/, "2400", "1240"/*"425.25"*/, "", "", "",
     "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};
  
  public raReportElement LabelObrazac; 
  private String[] LabelObrazacProps = new String[] {"Obrazac", "",  "9490", "2400",  "1000", "", "", "", "", "", "", 
      "", "Lucida Bright", "7",     "Bold", "", "", "Right"};
  
  
  public raReportElement TextMB;
  private String[] TextMBProps = new String[] {"MB", "", "", "", "", "", "", "", "640", p_getYzaKoloneUKucici(2120),
    "3200", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
/*  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "", "5140", "1720", "0.0", "320", "", "", ""};
  public raReportElement Line6;
  private String[] Line6Props = new String[] {"", "", "320", "1720", "0.0", "320", "", "", ""};
  public raReportElement Line7;
  private String[] Line7Props = new String[] {"", "", "320", "2040", "280", "0.0", "", "", ""};
  public raReportElement Line8;
  private String[] Line8Props = new String[] {"", "", "4860", "2040", "280", "0.0", "", "", ""};*/
  public raReportElement LabelDUMMY;
  private String[] LabelDUMMYProps = new String[] {"", "", "5760", "620", "1440", "220", "",
    "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement LabelRACUNOTPREMNICA;
  private String[] LabelRACUNOTPREMNICAProps = new String[] {"\nRAÈUN-OTPREMNICA", "", "7030", "",
     "4100", "600", "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "Center"};
  public raReportElement LabelBROJ;
  private String[] LabelBROJProps = new String[] {"Broj", "", "5760", "700", "1440", "220", "",
    "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement LabelEMPTY;
  private String[] LabelEMPTYProps = new String[] {"", "", "7280", "660", /*"3500"*/"3460", "260", "Normal",
    "Light Gray", "Solid", "Light Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement TextFormatBroj;
  private String[] TextFormatBrojProps = new String[] {"FormatBroj", "", "", "", "", "", "", "", "7280",
     "680", "3500", "220", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "",
     "Center", "No"};

  String last = "L";
  public raRPSectionHeader(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 0));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
    if (frmParam.getParam("robno","ispTELFAXParROT","N","Ispis Telefona i faxa partnera na ROT-u u polju za adresu").
        equalsIgnoreCase("N")) {
      TextTEL.defaultAlterer().setVisible(false);
    }
    if (repIzlazni.vanizkuce.equalsIgnoreCase("X")) {
      TextMB.defaultAlterer().setVisible(false);
      TextCPAR.defaultAlterer().setVisible(false);
    }

  }
  private String p_getYzaKoloneUKucici(int prevY) {
    if (repIzlazni.vanizkuce.equalsIgnoreCase("D")) return (prevY+600)+"";//"2720"
    return prevY+"";//"2120";
  } 
  private void addElements() {
    TextTEL = addModel(ep.TEXT, TextTELProps);
    Rectangle2 = addModel(ep.RECTANGLE, Rectangle2Props);
    Rectangle1 = addModel(ep.RECTANGLE, Rectangle1Props);
    Rectangle3 = addModel(ep.RECTANGLE, Rectangle3Props);
    TextSgetDATDOK = addModel(ep.TEXT, TextSgetDATDOKProps);
    TextLogoMjestoZarez = addModel(ep.TEXT, TextLogoMjestoZarezProps);
    LabelMjesto = addModel(ep.LABEL, LabelMjestoProps);
    TextNAZPAR = addModel(ep.TEXT, TextNAZPARProps);
    LabelDospijece = addModel(ep.LABEL, LabelDospijeceProps);
    TextSgetDDOSP = addModel(ep.TEXT, TextSgetDDOSPProps);
    TextSgetDATDOSP = addModel(ep.TEXT, TextSgetDATDOSPProps);
    TextBRNARIZ = addModel(ep.TEXT, TextBRNARIZProps);
    LabelNarudzba = addModel(ep.LABEL, LabelNarudzbaProps);
    TextSgetDATNARIZ = addModel(ep.TEXT, TextSgetDATNARIZProps);
    TextADR = addModel(ep.TEXT, TextADRProps);
    LabelNacinplacanja = addModel(ep.LABEL, LabelNacinplacanjaProps);
    TextNAZNACPL = addModel(ep.TEXT, TextNAZNACPLProps);
    TextMJ = addModel(ep.TEXT, TextMJProps);
//    TextPBR = addModel(ep.TEXT, TextPBRProps);
    TextNAZNAC = addModel(ep.TEXT, TextNAZNACProps);
    LabelNacinotpreme = addModel(ep.LABEL, LabelNacinotpremeProps);
    TextNAZFRA = addModel(ep.TEXT, TextNAZFRAProps);
    LabelParitet = addModel(ep.LABEL, LabelParitetProps);
    LabelR1 = addModel(ep.LABEL, LabelR1Props);
//  System.out.println("*********************************************");
//  System.out.println("*             DODAJEM OBRAZAC               *");
//  System.out.println("*********************************************");
    LabelObrazac = addModel(ep.LABEL, LabelObrazacProps);
    TextMB = addModel(ep.TEXT, TextMBProps);
//    Line5 = addModel(ep.LINE, Line5Props);
//    Line6 = addModel(ep.LINE, Line6Props);
//    Line7 = addModel(ep.LINE, Line7Props);
//    Line8 = addModel(ep.LINE, Line8Props);
    LabelRACUNOTPREMNICA = addModel(ep.LABEL, LabelRACUNOTPREMNICAProps);
    LabelBROJ = addModel(ep.LABEL, LabelBROJProps);
    LabelEMPTY = addModel(ep.LABEL, LabelEMPTYProps);
    TextFormatBroj = addModel(ep.TEXT, TextFormatBrojProps);
    TextKONTOSOB = addModel(ep.TEXT, TextKONTOSOBProps);
    TextCPAR = addModel(ep.TEXT, TextCPARProps);
    Line1 = addModel(ep.LINE, Line1Props);
    Line2 = addModel(ep.LINE, Line2Props);
    Line3 = addModel(ep.LINE, Line3Props);
    Line4 = addModel(ep.LINE, Line4Props);
  }

  private void modifyThis() {
    String knjig = hr.restart.zapod.OrgStr.getKNJCORG(false);
    String r1 = hr.restart.sisfun.frmParam.getParam("robno", "izlazObr"+knjig,
        "R-1", "Vrsta obrasca ispisa raèuna za knjigovodstvo "+knjig);
    LabelR1.setCaption(r1);

    String now = hr.restart.sisfun.frmParam.getParam("robno", "ispProzor");
    if (!now.equals(last)) {
      last = now;
      if ("L".equalsIgnoreCase(now)) this.restoreDefaults();
      else {
        raReportSection left = this.getView(Rectangle2.getLeft(), Rectangle2.getTop(), Rectangle2.getRight(), Rectangle2.getBottom() + 400);
        raReportSection right1 = this.getView(LabelRACUNOTPREMNICA, LabelRACUNOTPREMNICA);
        raReportSection right2 = this.getView(LabelBROJ, LabelR1);
        left.moveRightCm(9.5);
        right1.moveLeft(5760);
        right2.moveLeft(5760);
        Line3.setLeft(Line3.getLeft()-5760);
        LabelParitet.setTextAlign(ep.LEFT);
        LabelNarudzba.setTextAlign(ep.LEFT);
        LabelNacinplacanja.setTextAlign(ep.LEFT);
        LabelNacinotpreme.setTextAlign(ep.LEFT);
        LabelMjesto.setTextAlign(ep.LEFT);
        LabelDospijece.setTextAlign(ep.LEFT);
        LabelBROJ.setTextAlign(ep.LEFT);
//        LabelR1.setLeft(Rectangle2.getLeft());
      }
    }
  }
}
