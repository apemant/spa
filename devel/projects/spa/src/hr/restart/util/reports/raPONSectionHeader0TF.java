/****license*****************************************************************
**   file: raPONSectionHeader0TF.java
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

public class raPONSectionHeader0TF extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "Before", "", "", "Yes", "", "No", "", 
     "3240"};
  public raReportElement LabelP_O_N_U_D_A;
  private String[] LabelP_O_N_U_D_AProps = new String[] {"\nP O N U D A", "", "7020", "", "4100", 
     "600", "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "Center"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"NAZPARL", "", "", "", "", 
     "", "Yes", "Yes", "640", "200", "4360", "840", "", "", "", "", "", "", "Lucida Bright", "", 
     "Bold", "", "", "", ""};
  public raReportElement Rectangle1;
  private String[] Rectangle1Props = new String[] {"", "660", "240", "4160", "2200", "", "", "", 
     "White", ""};
  public raReportElement Rectangle2;
  private String[] Rectangle2Props = new String[] {"", "320", "260", "4840", "2160", "Transparent", 
     "", "", "", ""};
  /*public raReportElement Rectangle3;
  private String[] Rectangle3Props = new String[] {"", "280", "600", "4880", "1280", "", "", "", 
     "White", ""};*/
  public raReportElement Rectangle4;
  private String[] Rectangle4Props = new String[] {"", "300", "600", "4880", "1500", "", "", "", 
     "White", ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "10760", "620", "0", "340", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "7240", "620", "0", "340", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "7240", "620", "3540", "0", "", "", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "7240", "940", "3540", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "7280", "660", "3460", "260", "Normal", 
     "Light Gray", "Solid", "Light Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement TextFormatBroj;
  private String[] TextFormatBrojProps = new String[] {"FormatBroj", "", "", "", "", "", "", "", 
     "7280", "680", "3500", "220", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", 
     "Center", "No"};
  public raReportElement LabelBroj;
  private String[] LabelBrojProps = new String[] {"Broj", "", "5760", "700", "", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextLogoMjestoZarez;
  private String[] TextLogoMjestoZarezProps = new String[] {"LogoMjestoZarez", "", "", "", "", "", 
     "", "", "7280", "1040", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", 
     "", "", ""};
  public raReportElement TextSgetDATDOK;
  private String[] TextSgetDATDOKProps = new String[] {"SgetDATDOK", "", "hr_HR", 
     "Date|false|Short", "", "", "", "", "9600", "1040", "1240", "220", "", "", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement LabelMjesto;
  private String[] LabelMjestoProps = new String[] {"Mjesto", "", "5760", "1040", "", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextADR;
  private String[] TextADRProps = new String[] {"ADR", "", "", "", "", "", "", "", "640", "1120", 
     "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextSgetDATDOSP;
  private String[] TextSgetDATDOSPProps = new String[] {"SgetDATDOSP", "", "", "", "", "", "", "", 
     "9600", "1260", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};
  public raReportElement TextDDOSP;
  private String[] TextDDOSPProps = new String[] {"DDOSP", "", "", "", "", "", "", "", "7280", 
     "1260", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelOpcija_dana;
  private String[] LabelOpcija_danaProps = new String[] {"Opcija dana", "", "5760", "1260", "", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextMJ;
  private String[] TextMJProps = new String[] {"MJ", "", "", "", "", "", "", "", "640", "1380", 
     "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  
  public raReportElement TextSgetDATUG;
  private String[] TextSgetDATUGProps = new String[] {"SgetDATUG", "", "", "", "", "", "", "", 
     "9600", "1480", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};
  public raReportElement TextCUG;
  private String[] TextCUGProps = new String[] {"CUG", "", "", "", "", "", "", "", "7280", 
     "1480", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelUgovor;
  private String[] LabelUgovorProps = new String[] {"Ugovor", "", "5760", "1480", "", "220", "", 
     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  
  public raReportElement TextSgetDATNARIZ;
  private String[] TextSgetDATNARIZProps = new String[] {"SgetDATNARIZ", "", "", "", "", "", "", "", 
     "9600", "1700", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};
  public raReportElement TextBRNARIZ;
  private String[] TextBRNARIZProps = new String[] {"BRNARIZ", "", "", "", "", "", "", "", "7280", 
     "1700", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelNarudzba;
  private String[] LabelNarudzbaProps = new String[] {"Narudžba", "", "5760", "1700", "", "220", "", 
     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextKONTOSOB;
  private String[] TextKONTOSOBProps = new String[] {"KONTOSOB", "", "", "", "", "", "Yes", "", 
     "640", "1640", "4360", "480", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement LabelNacin_placanja;
  private String[] LabelNacin_placanjaProps = new String[] {"Na\u010Din pla\u0107anja", "", "5760", 
     "1920", "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZNACPL;
  private String[] TextNAZNACPLProps = new String[] {"NAZNACPL", "", "", "", "", "", "", "", "7280", 
     "1920", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
//  public raReportElement TextFAX;
//  private String[] TextFAXProps = new String[] {"FAX", "", "", "", "", "", "", "", "2840", "1900", 
//     "2160", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextTEL;
  private String[] TextTELProps = new String[] {"TEL", "", "", "", "", "", "", "", "320", "2520", //"1900", 
      "6000", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
//  private String[] TextTELProps = new String[] {"TEL", "", "", "", "", "", "", "", "640", "1900", 
//     "2160", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement LabelNacin_otpreme;
  private String[] LabelNacin_otpremeProps = new String[] {"Na\u010Din otpreme", "", "5760", "2140", 
     "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZNAC;
  private String[] TextNAZNACProps = new String[] {"NAZNAC", "", "", "", "", "", "", "", "7280", 
     "2140", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextCPAR;
  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "3940", p_getYzaKoloneUKucici(2120), 
     "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextMB;
  private String[] TextMBProps = new String[] {"MB", "", "", "", "", "", "", "", "640", p_getYzaKoloneUKucici(2120), 
     "3200", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelParitet;
  private String[] LabelParitetProps = new String[] {"Paritet", "", "5760", "2360", "", "220", "", 
     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZFRA;
  private String[] TextNAZFRAProps = new String[] {"NAZFRA", "", "", "", "", "", "", "", "7280", 
     "2360", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelIsporuka;
  private String[] LabelIsporukaProps = new String[] {"Isporuka", "", "5700", "2580", "1500", "220", 
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextISPORUKA;
  private String[] TextISPORUKAProps = new String[] {"ISPORUKA", "", "", "", "", "", "Yes", "Yes", 
     "7280", "2580", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportElement LabelObrazac;
  private String[] LabelObrazacProps = new String[] {"Obrazac", "", "9480", "2820", "1000", "", "", 
     "", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};
  public raReportElement LabelR1;
  private String[] LabelR1Props = new String[] {"R-1", "", "9640", "2820", "1240", "", "", "", "", 
     "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};

  public raPONSectionHeader0TF(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 0));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
    if (frmParam.getParam("robno","ispisTELFAX_Par","N","Ispis Telefona i faxa partnera na PON-u u polju za adresu").
        equalsIgnoreCase("N")) {
      TextTEL.defaultAlterer().setVisible(false);
//      sh.TextFAX.defaultAlterer().setVisible(false);
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
    Rectangle2 = addModel(ep.RECTANGLE, Rectangle2Props);
    //Rectangle3 = addModel(ep.RECTANGLE, Rectangle3Props);
    Rectangle1 = addModel(ep.RECTANGLE, Rectangle1Props);
    Rectangle4 = addModel(ep.RECTANGLE, Rectangle4Props);
    LabelP_O_N_U_D_A = addModel(ep.LABEL, LabelP_O_N_U_D_AProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Line1 = addModel(ep.LINE, Line1Props);
    Line2 = addModel(ep.LINE, Line2Props);
    Line3 = addModel(ep.LINE, Line3Props);
    Label1 = addModel(ep.LABEL, Label1Props);
    TextFormatBroj = addModel(ep.TEXT, TextFormatBrojProps);
    LabelBroj = addModel(ep.LABEL, LabelBrojProps);
    Line4 = addModel(ep.LINE, Line4Props);
    TextLogoMjestoZarez = addModel(ep.TEXT, TextLogoMjestoZarezProps);
    TextSgetDATDOK = addModel(ep.TEXT, TextSgetDATDOKProps);
    LabelMjesto = addModel(ep.LABEL, LabelMjestoProps);
    TextADR = addModel(ep.TEXT, TextADRProps);
    TextSgetDATDOSP = addModel(ep.TEXT, TextSgetDATDOSPProps);
    TextDDOSP = addModel(ep.TEXT, TextDDOSPProps);
    LabelOpcija_dana = addModel(ep.LABEL, LabelOpcija_danaProps);
    TextMJ = addModel(ep.TEXT, TextMJProps);
    TextSgetDATUG = addModel(ep.TEXT, TextSgetDATUGProps);
    TextCUG = addModel(ep.TEXT, TextCUGProps);
    LabelUgovor = addModel(ep.LABEL, LabelUgovorProps);
    TextSgetDATNARIZ = addModel(ep.TEXT, TextSgetDATNARIZProps);
    TextBRNARIZ = addModel(ep.TEXT, TextBRNARIZProps);
    LabelNarudzba = addModel(ep.LABEL, LabelNarudzbaProps);
    TextKONTOSOB = addModel(ep.TEXT, TextKONTOSOBProps);
    LabelNacin_placanja = addModel(ep.LABEL, LabelNacin_placanjaProps);
    TextNAZNACPL = addModel(ep.TEXT, TextNAZNACPLProps);
//    TextFAX = addModel(ep.TEXT, TextFAXProps);
    TextTEL = addModel(ep.TEXT, TextTELProps);
    LabelNacin_otpreme = addModel(ep.LABEL, LabelNacin_otpremeProps);
    TextNAZNAC = addModel(ep.TEXT, TextNAZNACProps);
    TextCPAR = addModel(ep.TEXT, TextCPARProps);
    TextMB = addModel(ep.TEXT, TextMBProps);
    LabelParitet = addModel(ep.LABEL, LabelParitetProps);
    TextNAZFRA = addModel(ep.TEXT, TextNAZFRAProps);
    LabelIsporuka = addModel(ep.LABEL, LabelIsporukaProps);
    TextISPORUKA = addModel(ep.TEXT, TextISPORUKAProps);
    LabelR1 = addModel(ep.LABEL, LabelR1Props);
    LabelObrazac = addModel(ep.LABEL, LabelObrazacProps);
  }
  
  String last = "L";
  private void modifyThis() {
    String knjig = hr.restart.zapod.OrgStr.getKNJCORG(false);
    String r1 = frmParam.getParam("robno", "izlazObr"+knjig,
        "R-1", "Vrsta obrasca ispisa raèuna za knjigovodstvo "+knjig);
    LabelR1.setCaption(r1);
    if (r1.length() == 0) LabelObrazac.setCaption("");
    
    if (frmParam.getParam("robno","ispisPJ","D", "Ispis poslovne jedinice na ROT-u " +
        "(D-u adresi, I-kao isporuka, O-na oba mjesta, N-bez P.J.)").equalsIgnoreCase("I") ||
        frmParam.getParam("robno","ispisPJ").equalsIgnoreCase("O"))
      LabelIsporuka.setCaption("Isporuka");  //LabelIsporuka.setCaption("");
    else LabelIsporuka.setCaption("");
    
    String now = hr.restart.sisfun.frmParam.getParam("robno", "ispProzor");
    if (!now.equals(last)) {
      last = now;
      if ("L".equalsIgnoreCase(now)) this.restoreDefaults();
      else {
        raReportSection left = this.getView(Rectangle2.getLeft(), Rectangle2.getTop(), Rectangle2.getRight(), Rectangle2.getBottom() + 400);
        raReportSection right1 = this.getView(LabelP_O_N_U_D_A, LabelP_O_N_U_D_A);
        raReportSection right2 = this.getView(LabelBroj, LabelR1);
        left.moveRightCm(9.5);
        right1.moveLeft(5760);
        right2.moveLeft(5760);
        Line1.setLeft(Line1.getLeft()-5760);
      }
    }
  }
}
