/****license*****************************************************************
**   file: raSectionHeader0TF.java
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

import hr.restart.robno.repFISBIH;
import hr.restart.robno.repIzlazni;
import hr.restart.sisfun.frmParam;

public class raSectionHeader0TF extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "Before", "", "", "Yes", "", "Yes", "", 
     "2840"};
  public raReportElement LabelR_A_C_U_N;
  private String[] LabelR_A_C_U_NProps = new String[] {"\nR A \u010C U N", "", "7020", "", "4100", 
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
  public raReportElement Rectangle3;
  private String[] Rectangle3Props = new String[] {"", "300", "600", "4880", "1500", "", "", "", 
     "White", ""};
  /*public raReportElement Rectangle4;
  private String[] Rectangle4Props = new String[] {"", "280", "600", "4880", "1280", "", "", "", 
     "White", ""};*/
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
  private String[] LabelBrojProps = new String[] {"Broj", "", "5460", "700", "1740", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement LabelMjestoDatum;
  private String[] LabelMjestoDatumProps = new String[] {"Mjesto/datum izdavanja", "", "5200", "1040", "2000", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextLogoMjestoZarez;
  private String[] TextLogoMjestoZarezProps = new String[] {"LogoMjestoZarez", "", "", "", "", "", 
     "", "", "7280", "1040", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", 
     "", "", ""};
  public raReportElement TextSgetDATDOK;
  private String[] TextSgetDATDOKProps = new String[] {"SgetDATDOK", "", "hr_HR", 
     "Date|false|Short", "", "", "", "", "9600", "1040", "1240", "220", "", "", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextADR;
  private String[] TextADRProps = new String[] {"ADR", "", "", "", "", "", "", "", "640", "1120", 
     "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement LabelDatum_isporuke;
  private String[] LabelDatum_isporukeProps = new String[] {"Datum isporuke", "", "5460", "1260", 
     "1740", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextSgetDVO;
  private String[] TextSgetDVOProps = new String[] {"SgetDVO", "", "hr_HR", "Date|false|Short", "", 
     "", "", "", "9600", "1260", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextMJ;
  private String[] TextMJProps = new String[] {"MJ", "", "", "", "", "", "", "", "640", "1380", 
     "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement LabelDospijeceDatum;
  private String[] LabelDospijeceDatumProps = new String[] {"Dospije\u0107e/Datum", "", "5460", 
     "1480", "1740", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextSgetDATDOSP;
  private String[] TextSgetDATDOSPProps = new String[] {"SgetDATDOSP", "", "", "", "", "", "", "", 
     "9600", "1480", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};
  public raReportElement TextSgetDDOSP;
  private String[] TextSgetDDOSPProps = new String[] {"SgetDDOSP", "", "", "", "", "", "", "", 
     "7280", "1480", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  public raReportElement TextKONTOSOB;
  private String[] TextKONTOSOBProps = new String[] {"KONTOSOB", "", "", "", "", "", "Yes", "", 
     "640", "1640", "4360", "480", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement LabelNarudzba;
  private String[] LabelNarudzbaProps = new String[] {"Narudžba", "", "5460", "1700", "1740", "220", 
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextBRNARIZ;
  private String[] TextBRNARIZProps = new String[] {"BRNARIZ", "", "", "", "", "", "", "", "7280", 
     "1700", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextSgetDATNARIZ;
  private String[] TextSgetDATNARIZProps = new String[] {"SgetDATNARIZ", "", "", "", "", "", "", "", 
     "9600", "1700", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};
  public raReportElement TextTEL;
  private String[] TextTELProps = new String[] {"TEL", "", "", "", "", "", "", "", "320", "2520", //"1900", 
      "6000", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};  
//  private String[] TextTELProps = new String[] {"TEL", "", "", "", "", "", "", "", "640", "2520", 
//     "4320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement LabelIzlazni_dokument;
  private String[] LabelIzlazni_dokumentProps = new String[] {"Izlazni dokument", "", "5460", 
     "1920", "1740", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextBRDOKIZ;
  private String[] TextBRDOKIZProps = new String[] {"BRDOKIZ", "", "", "", "", "", "", "", "7280", 
     "1920", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextSgetDATDOKIZ;
  private String[] TextSgetDATDOKIZProps = new String[] {"SgetDATDOKIZ", "", "", "", "", "", "", "", 
     "9600", "1920", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "Right", "No"};
  public raReportElement LabelNacin_placanja;
  private String[] LabelNacin_placanjaProps = new String[] {"Na\u010Din pla\u0107anja", "", "5460", 
     "2140", "1740", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZNACPL;
  private String[] TextNAZNACPLProps = new String[] {"NAZNACPL", "", "", "", "", "", "", "", "7280", 
     "2140", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};

  public raReportElement LabelIsporuka;
  private String[] LabelIsporukaProps = new String[] {"Isporuka", "", "5460", "2360", "1740", "220", 
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextISPORUKA;
  private String[] TextISPORUKAProps = new String[] {"ISPORUKA", "", "", "", "", "", "Yes", "Yes", 
     "7280", "2360", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  
  public raReportElement TextLABDOD;
  private String[] TextLABDODProps = new String[] {"LABDOD", "", "", "", "", "", "Yes", "Yes", 
      "320", "2720", "1600", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", 
     ""};
  public raReportElement TextTEXTDOD;
  private String[] TextTEXTDODProps = new String[] {"TEXTDOD", "", "", "", "", "", "Yes", "Yes", 
      "2000", "2720", "3200", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     ""};
  
  
  public raReportElement TextMB;
  private String[] TextMBProps = new String[] {"MB", "", "", "", "", "", "", "", "640", p_getYzaKoloneUKucici(2140), 
     "3200", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextCPAR;
  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "3940", p_getYzaKoloneUKucici(2140), 
     "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelR1;
  private String[] LabelR1Props = new String[] {"R-1", "", "9640", "2600", "1240", "", "", "", "", 
     "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};
  public raReportElement LabelObrazac;
  private String[] LabelObrazacProps = new String[] {"Obrazac", "", "9480", "2600", "1000", "", "", 
     "", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};
  
//
//  private String[] thisProps = new String[] {"BRDOK", "Before", "", "", "Yes", "", "Yes", "", 
//     "2640"};
//  public raReportElement LabelR_A_C_U_N;
//  private String[] LabelR_A_C_U_NProps = new String[] {"\nR A \u010C U N", "", "7020", "", "4100", 
//     "600", "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "Center"};
//  public raReportElement Text1;
//  private String[] Text1Props = new String[] {"=(string-append \"\n\" [NAZPAR])", "", "", "", "", 
//     "", "Yes", "Yes", "640", "200", "4360", "840", "", "", "", "", "", "", "Lucida Bright", "", 
//     "Bold", "", "", "", ""};
//  public raReportElement Rectangle1;
//  private String[] Rectangle1Props = new String[] {"", "660", "240", "4160", "2180", "", "", "", 
//     "White", ""};
//  public raReportElement Rectangle2;
//  private String[] Rectangle2Props = new String[] {"", "320", "260", "4840", "2160", "Transparent", 
//     "", "", "", ""};
//  public raReportElement Rectangle3;
//  private String[] Rectangle3Props = new String[] {"", "280", "600", "4880", "1280", "", "", "", 
//     "White", ""};
//  public raReportElement Rectangle4;
//  private String[] Rectangle4Props = new String[] {"", "300", "600", "4860", "1500", "", "", "", 
//     "White", ""};
//  public raReportElement Line1;
//  private String[] Line1Props = new String[] {"", "", "7260", "620", "3500", "0", "", "", ""};
//  public raReportElement Line2;
//  private String[] Line2Props = new String[] {"", "", "7260", "620", "0", "320", "", "", ""};
//  public raReportElement Line3;
//  private String[] Line3Props = new String[] {"", "", "10760", "620", "0", "320", "", "", ""};
//  public raReportElement Label1;
//  private String[] Label1Props = new String[] {"", "", "7280", "660", "3460", "260", "Normal", 
//     "Light Gray", "Solid", "Light Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
//  public raReportElement TextFormatBroj;
//  private String[] TextFormatBrojProps = new String[] {"FormatBroj", "", "", "", "", "", "", "", 
//     "7280", "680", "3500", "220", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", 
//     "Center", "No"};
//  public raReportElement LabelBroj;
//  private String[] LabelBrojProps = new String[] {"Broj", "", "5760", "700", "", "220", "", "", "", 
//     "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
//  public raReportElement Line4;
//  private String[] Line4Props = new String[] {"", "", "7260", "940", "3500", "0", "", "", ""};
//  public raReportElement TextADR;
//  private String[] TextADRProps = new String[] {"ADR", "", "", "", "", "", "", "", "640", "1120", 
//     "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
//  public raReportElement TextLogoMjestoZarez;
//  private String[] TextLogoMjestoZarezProps = new String[] {"LogoMjestoZarez", "", "", "", "", "", 
//     "", "", "7280", "1260", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", 
//     "", "", ""};
//  public raReportElement TextSgetDATDOK;
//  private String[] TextSgetDATDOKProps = new String[] {"SgetDATDOK", "", "hr_HR", 
//     "Date|false|Short", "", "", "", "", "9600", "1260", "1240", "220", "", "", "", "", "", "", 
//     "Lucida Bright", "8", "", "", "", "Right", "No"};
//  public raReportElement LabelMjesto;
//  private String[] LabelMjestoProps = new String[] {"Mjesto", "", "5760", "1260", "", "220", "", "", 
//     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
//  public raReportElement TextMJ;
//  private String[] TextMJProps = new String[] {"MJ", "", "", "", "", "", "", "", "640", "1380", 
//     "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
//  public raReportElement TextSgetDATDOSP;
//  private String[] TextSgetDATDOSPProps = new String[] {"SgetDATDOSP", "", "", "", "", "", "", "", 
//     "9600", "1480", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
//     "Right", "No"};
//  public raReportElement TextSgetDDOSP;
//  private String[] TextSgetDDOSPProps = new String[] {"SgetDDOSP", "", "", "", "", "", "", "", 
//     "7280", "1480", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
//     ""};
//  public raReportElement LabelDospijece;
//  private String[] LabelDospijeceProps = new String[] {"Dospije\u0107e", "", "5760", "1480", "", 
//     "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
//  public raReportElement TextKONTOSOB;
//  private String[] TextKONTOSOBProps = new String[] {"KONTOSOB", "", "", "", "", "", "Yes", "", 
//     "640", "1640", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
//  public raReportElement TextSgetDATNARIZ;
//  private String[] TextSgetDATNARIZProps = new String[] {"SgetDATNARIZ", "", "", "", "", "", "", "", 
//     "9600", "1700", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
//     "Right", "No"};
//  public raReportElement TextBRNARIZ;
//  private String[] TextBRNARIZProps = new String[] {"BRNARIZ", "", "", "", "", "", "", "", "7280", 
//     "1700", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
//  public raReportElement LabelNarudzba;
//  private String[] LabelNarudzbaProps = new String[] {"Narudžba", "", "5760", "1700", "", "220", "", 
//     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
////  public raReportElement TextFAX;
////  private String[] TextFAXProps = new String[] {"FAX", "", "", "", "", "", "", "", "2840", "1900", 
////     "2160", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
//  public raReportElement TextTEL;
//  private String[] TextTELProps = new String[] {"TEL", "", "", "", "", "", "", "", "640", "1900", 
//     "4320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
//  public raReportElement TextSgetDATDOKIZ;
//  private String[] TextSgetDATDOKIZProps = new String[] {"SgetDATDOKIZ", "", "", "", "", "", "", "", 
//     "9600", "1920", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
//     "Right", "No"};
//  public raReportElement TextBRDOKIZ;
//  private String[] TextBRDOKIZProps = new String[] {"BRDOKIZ", "", "", "", "", "", "", "", "7280", 
//     "1920", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
//  public raReportElement LabelIzlazni_dokument;
//  private String[] LabelIzlazni_dokumentProps = new String[] {"Izlazni dokument", "", "5460", 
//     "1920", "1740", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
//  public raReportElement LabelNacin_placanja;
//  private String[] LabelNacin_placanjaProps = new String[] {"Na\u010Din pla\u0107anja", "", "5760", 
//     "2140", "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
//  public raReportElement TextCPAR;
//  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "3940", "2140", 
//     "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
//  public raReportElement TextMB;
//  private String[] TextMBProps = new String[] {"MB", "", "", "", "", "", "", "", "640", "2140", 
//     "3200", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
//  public raReportElement TextNAZNACPL;
//  private String[] TextNAZNACPLProps = new String[] {"NAZNACPL", "", "", "", "", "", "", "", "7280", 
//     "2140", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
//  public raReportElement LabelObrazac;
//  private String[] LabelObrazacProps = new String[] {"Obrazac", "", "9480", "2400", "1000", "", "", 
//     "", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};
//  public raReportElement LabelR1;
//  private String[] LabelR1Props = new String[] {"R-1", "", "9640", "2400", "1240", "", "", "", "", 
//     "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};

  public raSectionHeader0TF(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 0));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
    if (hr.restart.sisfun.frmParam.getParam("robno","ispTELFAXParROT","N","Ispis Telefona i faxa partnera na ROT-u u polju za adresu").
        equalsIgnoreCase("N")) {
      TextTEL.defaultAlterer().setVisible(false);
    }

    if (repIzlazni.vanizkuce.equalsIgnoreCase("X")) {
      TextMB.defaultAlterer().setVisible(false);
      TextCPAR.defaultAlterer().setVisible(false);
    }
 
    int prijemicano = 0;
    if (frmParam.getParam("robno", "ispJIRizd", "N", "Ispis fiskalnog identifikatora na izlaznim dokumentima (D/N)").equals("D")) {//fiskalni broj 
      raReportSection alt = defaultAltererSect();
      alt.setHeight(alt.getHeight()+220);
      alt.getView(LabelNacin_placanja, LabelR1).moveDown(220);
      
      raReportElement jirlab = copyToModify(LabelIzlazni_dokument);
      raReportElement jirtext = copyToModify(TextBRDOKIZ);
      jirlab.setCaption(repFISBIH.isFISBIH()?"Br.fiskalnog RN":"JIR");
      jirlab.setTop(jirlab.getTop() + 220 + prijemicano);
      jirtext.setControlSource("JIR");
      jirtext.setTop(jirtext.getTop() + 220 + prijemicano);
      prijemicano = prijemicano + 220;
    }
  }
  private String p_getYzaKoloneUKucici(int prevY) {
    if (repIzlazni.vanizkuce.equalsIgnoreCase("D")) return (prevY+600)+"";//"2720"
    return prevY+"";//"2120";
  } 
  
  private void addElements() {
    Rectangle2 = addModel(ep.RECTANGLE, Rectangle2Props);
    Rectangle3 = addModel(ep.RECTANGLE, Rectangle3Props);
    Rectangle1 = addModel(ep.RECTANGLE, Rectangle1Props);
    //Rectangle4 = addModel(ep.RECTANGLE, Rectangle4Props);
    LabelR_A_C_U_N = addModel(ep.LABEL, LabelR_A_C_U_NProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Line1 = addModel(ep.LINE, Line1Props);
    Line2 = addModel(ep.LINE, Line2Props);
    Line3 = addModel(ep.LINE, Line3Props);
    Label1 = addModel(ep.LABEL, Label1Props);
    TextFormatBroj = addModel(ep.TEXT, TextFormatBrojProps);
    LabelBroj = addModel(ep.LABEL, LabelBrojProps);
    Line4 = addModel(ep.LINE, Line4Props);
    TextADR = addModel(ep.TEXT, TextADRProps);
    TextLogoMjestoZarez = addModel(ep.TEXT, TextLogoMjestoZarezProps);
    TextSgetDATDOK = addModel(ep.TEXT, TextSgetDATDOKProps);
    LabelMjestoDatum = addModel(ep.LABEL, LabelMjestoDatumProps);
    TextMJ = addModel(ep.TEXT, TextMJProps);
    TextSgetDATDOSP = addModel(ep.TEXT, TextSgetDATDOSPProps);
    TextSgetDDOSP = addModel(ep.TEXT, TextSgetDDOSPProps);
    LabelDospijeceDatum = addModel(ep.LABEL, LabelDospijeceDatumProps);
    
    LabelDatum_isporuke = addModel(ep.LABEL, LabelDatum_isporukeProps);
    TextSgetDVO = addModel(ep.TEXT, TextSgetDVOProps);
    
    TextKONTOSOB = addModel(ep.TEXT, TextKONTOSOBProps);
    TextSgetDATNARIZ = addModel(ep.TEXT, TextSgetDATNARIZProps);
    TextBRNARIZ = addModel(ep.TEXT, TextBRNARIZProps);
    LabelNarudzba = addModel(ep.LABEL, LabelNarudzbaProps);
//    TextFAX = addModel(ep.TEXT, TextFAXProps);
    TextTEL = addModel(ep.TEXT, TextTELProps);
    TextSgetDATDOKIZ = addModel(ep.TEXT, TextSgetDATDOKIZProps);
    TextBRDOKIZ = addModel(ep.TEXT, TextBRDOKIZProps);
    LabelIzlazni_dokument = addModel(ep.LABEL, LabelIzlazni_dokumentProps);
    LabelNacin_placanja = addModel(ep.LABEL, LabelNacin_placanjaProps);
    TextCPAR = addModel(ep.TEXT, TextCPARProps);
    TextMB = addModel(ep.TEXT, TextMBProps);
    TextNAZNACPL = addModel(ep.TEXT, TextNAZNACPLProps);
    LabelIsporuka = addModel(ep.LABEL, LabelIsporukaProps);
    TextISPORUKA = addModel(ep.TEXT, TextISPORUKAProps);
    LabelObrazac = addModel(ep.LABEL, LabelObrazacProps);
    LabelR1 = addModel(ep.LABEL, LabelR1Props);
    
    TextLABDOD = addModel(ep.TEXT, TextLABDODProps);
    TextTEXTDOD = addModel(ep.TEXT, TextTEXTDODProps);
  }

  String last = "L";
  private void modifyThis() {
    String knjig = hr.restart.zapod.OrgStr.getKNJCORG(false);
    String r1 = hr.restart.sisfun.frmParam.getParam("robno", "izlazObr"+knjig,
        "R-1", "Vrsta obrasca ispisa raèuna za knjigovodstvo "+knjig);
    LabelR1.setCaption(r1);
    
    if (frmParam.getParam("robno", "ispisPJ", "D", "Ispis poslovne jedinice na ROT-u " +
            "(D-u adresi, I-kao isporuka, O-na oba mjesta, N-bez P.J.)").equalsIgnoreCase("I") ||
        frmParam.getParam("robno", "ispisPJ").equalsIgnoreCase("O"))
      LabelIsporuka.setCaption("Isporuka"); // LabelIsporuka.setCaption("");
    else LabelIsporuka.setCaption("");
    
    String now = hr.restart.sisfun.frmParam.getParam("robno", "ispProzor");
    if (!now.equals(last)) {
      last = now;
      if ("L".equalsIgnoreCase(now)) this.restoreDefaults();
      else {
        raReportSection left = this.getView(Rectangle2.getLeft(), Rectangle2.getTop(), Rectangle2.getRight(), Rectangle2.getBottom() + 400);
        raReportSection right1 = this.getView(LabelR_A_C_U_N, LabelR_A_C_U_N);
        raReportSection right2 = this.getView(LabelBroj, LabelR1);
        left.moveRightCm(9.5);
        right1.moveLeft(5760);
        right2.moveLeft(5760);
        Line3.setLeft(Line3.getLeft()-5760);
      }
    }
  }
}
