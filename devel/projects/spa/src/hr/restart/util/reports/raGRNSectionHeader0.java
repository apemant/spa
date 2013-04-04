/****license*****************************************************************
**   file: raGRNSectionHeader0.java
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
import hr.restart.robno.reportsQuerysCollector;
import hr.restart.sisfun.frmParam;

public class raGRNSectionHeader0 extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "Before", "", "", "Yes", "", "Yes", "", 
     "2640"};
  public raReportElement LabelR_A_C_U_N;
  private String[] LabelR_A_C_U_NProps = new String[] {"\nR A \u010C U N", "", "7020", "", "4100", 
     "600", "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "Center"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"NAZKUPCAL", "", "", "", "", 
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
  //public raReportElement Rectangle4;
  //private String[] Rectangle4Props = new String[] {"", "280", "600", "4880", "1280", "", "", "", 
  //     "White", ""};
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
  public raReportElement TextADRKUPCA;
  private String[] TextADRKUPCAProps = new String[] {"ADRKUPCA", "", "", "", "", "", "", "", "640", 
     "1120", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
  public raReportElement TextSgetDATDOK;
  private String[] TextSgetDATDOKProps = new String[] {"SgetDATDOK", "", "hr_HR", 
     "Date|false|Short", "", "", "", "", "9600", "1260", "1240", "220", "", "", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextLogoMjestoZarez;
  private String[] TextLogoMjestoZarezProps = new String[] {"LogoMjestoZarez", "", "", "", "", "", 
     "", "", "7280", "1260", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", 
     "", "", ""};
  public raReportElement LabelMjestoDatum;
  private String[] LabelMjestoDatumProps = new String[] {"Mjesto/datum izdavanja", "", "5760", "1060", "", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextPbrMjestoKupca;
  private String[] TextPbrMjestoKupcaProps = new String[] {"PbrMjestoKupca", "", "", "", "", "", "", 
     "", "640", "1380", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement TextSgetDVO;
  private String[] TextSgetDVOProps = new String[] {"SgetDVO", "", "hr_HR", "Date|false|Short", "", 
     "", "", "", "9600", "1480", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement LabelDatum_isporuke;
  private String[] LabelDatum_isporukeProps = new String[] {"Datum isporuke", "", "5760", 
     "1480", "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZNACPL;
  private String[] TextNAZNACPLProps = new String[] {"NAZNACPL", "", "", "", "", "", "", "", "7280", 
     "1700", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelNacin_placanja;
  private String[] LabelNacin_placanjaProps = new String[] {"Na\u010Din pla\u0107anja", "", "5760", 
     "1700", "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextTELKUPCA;
  private String[] TextTELKUPCAProps = new String[] {"TELKUPCA", "", "", "", "No", "", "", "", 
     "640", "1900", "4320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", 
     "No"};
  public raReportElement TextNAZNAC;
  private String[] TextNAZNACProps = new String[] {"NAZNAC", "", "", "", "", "", "", "", "7280", 
     "1920", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelNacin_otpreme;
  private String[] LabelNacin_otpremeProps = new String[] {"Na\u010Din otpreme", "", "5760", "1920", 
     "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextJMBG;
  private String[] TextJMBGProps = new String[] {"JMBG", "", "", "", "", "", "", "", "640", "2120", 
     "3200", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(if (> [CKUPAC] 0) [CKUPAC] \"\")", "", "", "", "", 
     "", "", "", "3940", "2120", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", ""};
  public raReportElement LabelParitet;
  private String[] LabelParitetProps = new String[] {"Paritet", "", "5760", "2140", "", "220", "", 
     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZFRA;
  private String[] TextNAZFRAProps = new String[] {"NAZFRA", "", "", "", "", "", "", "", "7280", 
     "2140", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelR1;
  private String[] LabelR1Props = new String[] {"R-1", "", "9640", "2400", "1240", "", "", "", "", 
     "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};
  public raReportElement LabelObrazac;
  private String[] LabelObrazacProps = new String[] {"Obrazac", "", "9480", "2400", "1000", "", "", 
     "", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};

//  private String[] thisProps = new String[] {"BRDOK", "Before", "", "", "Yes", "", "Yes", "", 
//     "2640"};
//  public raReportElement LabelR_A_C_U_N;
//  private String[] LabelR_A_C_U_NProps = new String[] {"\nR A \u010C U N", "", "7020", "", "4100", 
//     "600", "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "Center"};
//  public raReportElement Text1;
//  private String[] Text1Props = new String[] {"=(string-append \"\n\" [NAZKUPCA])", "", "", "", "", 
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
//  public raReportElement TextADRKUPCA;
//  private String[] TextADRKUPCAProps = new String[] {"ADRKUPCA", "", "", "", "", "", "", "", "640", 
//     "1120", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
//  public raReportElement TextMJKUPCA;
//  private String[] TextMJKUPCAProps = new String[] {"MJKUPCA", "", "", "", "", "", "", "", "640", 
//     "1380", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", ""};
//  public raReportElement LabelMjesto;
//  private String[] LabelMjestoProps = new String[] {"Mjesto", "", "5760", "1480", "", "220", "", "", 
//     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
//  public raReportElement TextLogoMjestoZarez;
//  private String[] TextLogoMjestoZarezProps = new String[] {"LogoMjestoZarez", "", "", "", "", "", 
//     "", "", "7280", "1480", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", 
//     "", "", ""};
//  public raReportElement TextSgetDATDOK;
//  private String[] TextSgetDATDOKProps = new String[] {"SgetDATDOK", "", "hr_HR", 
//     "Date|false|Short", "", "", "", "", "9600", "1480", "1240", "220", "", "", "", "", "", "", 
//     "Lucida Bright", "8", "", "", "", "Right", "No"};
//  public raReportElement TextNAZNACPL;
//  private String[] TextNAZNACPLProps = new String[] {"NAZNACPL", "", "", "", "", "", "", "", "7280", 
//     "1700", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
//  public raReportElement LabelNacin_placanja;
//  private String[] LabelNacin_placanjaProps = new String[] {"Na\u010Din pla\u0107anja", "", "5760", 
//     "1700", "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
////  public raReportElement TextFAX;
////  private String[] TextFAXProps = new String[] {"FAX", "", "", "", "", "", "", "", "2840", "1900", 
////     "2160", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
//  public raReportElement TextTEL;
//  private String[] TextTELProps = new String[] {"TEL", "", "", "", "", "", "", "", "640", "1900", 
//     "4320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
//  public raReportElement TextNAZNAC;
//  private String[] TextNAZNACProps = new String[] {"NAZNAC", "", "", "", "", "", "", "", "7280", 
//     "1920", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
//  public raReportElement LabelNacin_otpreme;
//  private String[] LabelNacin_otpremeProps = new String[] {"Na\u010Din otpreme", "", "5760", "1920", 
//     "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
//  public raReportElement TextCPAR;
//  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "3940", "2120", 
//     "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
//  public raReportElement TextJMBG;
//  private String[] TextJMBGProps = new String[] {"JMBG", "", "", "", "", "", "", "", "640", "2120", 
//     "3200", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
//  public raReportElement LabelParitet;
//  private String[] LabelParitetProps = new String[] {"Paritet", "", "5760", "2140", "", "220", "", 
//     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
//  public raReportElement TextNAZFRA;
//  private String[] TextNAZFRAProps = new String[] {"NAZFRA", "", "", "", "", "", "", "", "7280", 
//     "2140", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
//  public raReportElement LabelObrazac;
//  private String[] LabelObrazacProps = new String[] {"Obrazac", "", "9480", "2400", "1000", "", "", 
//     "", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};
//  public raReportElement LabelR1;
//  private String[] LabelR1Props = new String[] {"R-1", "", "9640", "2400", "1240", "", "", "", "", 
//     "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};

  public raGRNSectionHeader0(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 0));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
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
    TextADRKUPCA = addModel(ep.TEXT, TextADRKUPCAProps);
    TextPbrMjestoKupca = addModel(ep.TEXT, TextPbrMjestoKupcaProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    LabelMjestoDatum = addModel(ep.LABEL, LabelMjestoDatumProps);
    TextLogoMjestoZarez = addModel(ep.TEXT, TextLogoMjestoZarezProps);
    TextSgetDATDOK = addModel(ep.TEXT, TextSgetDATDOKProps);
    
    LabelDatum_isporuke = addModel(ep.LABEL, LabelDatum_isporukeProps);
    TextSgetDVO = addModel(ep.TEXT, TextSgetDVOProps);
    
    TextNAZNACPL = addModel(ep.TEXT, TextNAZNACPLProps);
    LabelNacin_placanja = addModel(ep.LABEL, LabelNacin_placanjaProps);
//    TextFAX = addModel(ep.TEXT, TextFAXProps);
    TextTELKUPCA = addModel(ep.TEXT, TextTELKUPCAProps);
    TextNAZNAC = addModel(ep.TEXT, TextNAZNACProps);
    LabelNacin_otpreme = addModel(ep.LABEL, LabelNacin_otpremeProps);
    Text2 = addModel(ep.TEXT, Text2Props);
    TextJMBG = addModel(ep.TEXT, TextJMBGProps);
    LabelParitet = addModel(ep.LABEL, LabelParitetProps);
    TextNAZFRA = addModel(ep.TEXT, TextNAZFRAProps);
    LabelObrazac = addModel(ep.LABEL, LabelObrazacProps);
    LabelR1 = addModel(ep.LABEL, LabelR1Props);
    int prijemicano = 0;
    if (frmParam.getParam("robno", "ispJIRizd", "N", "Ispis fiskalnog identifikatora na izlaznim dokumentima (D/N)").equals("D")) {//fiskalni broj 
      raReportSection alt = defaultAltererSect();
      alt.setHeight(alt.getHeight()+220);
//      alt.getView(LabelNacin_placanja, LabelR1).moveDown(220);
      
      raReportElement jirlab = copyToModify(LabelParitet);
      raReportElement jirtext = copyToModify(TextNAZFRA);
      jirlab.setCaption(repFISBIH.isFISBIH()?"Br.fiskal. RN":"JIR");
      jirlab.setTop(jirlab.getTop() + 220 + prijemicano);
      jirtext.setControlSource("JIR");
      jirtext.setTop(jirtext.getTop() + 220 + prijemicano);
      prijemicano = prijemicano + 220;
    }
  }

  String last = "L";
  private void modifyThis() {
    System.out.println("reportsQuerysCollector.getRQCModule().getQueryDataSet().getInt(\"CKUPAC\") = "+reportsQuerysCollector.getRQCModule().getQueryDataSet().getInt("CKUPAC"));
    if (reportsQuerysCollector.getRQCModule().getQueryDataSet().getInt("CKUPAC") == 0){
      System.out.println("ukidam R-1");
      this.LabelObrazac.setCaption("");
      this.LabelR1.setCaption("");
    } else {
      this.LabelR1.setCaption("R-1");
    }
  
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
