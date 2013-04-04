/****license*****************************************************************
**   file: raGRSectionHeaderWin.java
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
import hr.restart.sisfun.frmParam;

public class raGRSectionHeaderWin extends raReportSection {

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
  public raReportElement TextPbrMjestoKupca;
  private String[] TextPbrMjestoKupcaProps = new String[] {"PbrMjestoKupca", "", "", "", "", "", "", 
     "", "640", "1380", "4360", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "", 
     ""};
  public raReportElement LabelMjestoDatum;
  private String[] LabelMjestoDatumProps = new String[] {"Mjesto/datum izdavanja", "", "5760", "1480", "", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextLogoMjestoZarez;
  private String[] TextLogoMjestoZarezProps = new String[] {"LogoMjestoZarez", "", "", "", "", "", 
     "", "", "7280", "1480", "2300", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", 
     "", "", ""};
  public raReportElement TextSgetDATDOK;
  private String[] TextSgetDATDOKProps = new String[] {"SgetDATDOK", "", "hr_HR", 
     "Date|false|Short", "", "", "", "", "9600", "1480", "1240", "220", "", "", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextSgetDVO;
  private String[] TextSgetDVOProps = new String[] {"SgetDVO", "", "hr_HR", "Date|false|Short", "", 
     "", "", "", "9600", "1700", "1240", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement LabelDatum_isporuke;
  private String[] LabelDatum_isporukeProps = new String[] {"Datum isporuke", "", "5760", "1700", 
     "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZNAC;
  private String[] TextNAZNACProps = new String[] {"NAZNAC", "", "", "", "", "", "", "", "7280", 
     "1920", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelNacin_otpreme;
  private String[] LabelNacin_otpremeProps = new String[] {"Na\u010Din otpreme", "", "5760", "1920", 
     "", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextCKUPAC;
  private String[] TextCKUPACProps = new String[] {"=(if (> [CKUPAC] 0) [CKUPAC] \"\")", "", "", "", "", "", "", "", "3940", 
     "2120", "1080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextJMBG;
  private String[] TextJMBGProps = new String[] {"JMBG", "", "", "", "", "", "", "", "640", "2120", 
     "3200", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelParitet;
  private String[] LabelParitetProps = new String[] {"Paritet", "", "5760", "2140", "", "220", "", 
     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZFRA;
  private String[] TextNAZFRAProps = new String[] {"NAZFRA", "", "", "", "", "", "", "", "7280", 
     "2140", "3020", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelObrazac;
  private String[] LabelObrazacProps = new String[] {"Obrazac", "", "9480", "2400", "1000", "", "", 
     "", "", "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};
  public raReportElement LabelR1;
  private String[] LabelR1Props = new String[] {"R-1", "", "9640", "2400", "1240", "", "", "", "", 
     "", "", "", "Lucida Bright", "7", "Bold", "", "", "Right"};

  public raGRSectionHeaderWin(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 0));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
    int prijemicano = 0;
    if (frmParam.getParam("robno", "ispJIRizd", "N", "Ispis fiskalnog identifikatora na izlaznim dokumentima (D/N)").equals("D")) {//fiskalni broj 
      raReportSection alt = defaultAltererSect();
      alt.setHeight(alt.getHeight()+220);
      //alt.getView(LabelNacin_placanja, LabelR1).moveDown(220);
      
      raReportElement jirlab = copyToModify(LabelParitet);
      raReportElement jirtext = copyToModify(TextNAZFRA);
      jirlab.setCaption(repFISBIH.isFISBIH()?"Br.fiskal.RN":"JIR");
      jirlab.setTop(jirlab.getTop() + 220 + prijemicano);
      jirtext.setControlSource("JIR");
      jirtext.setTop(jirtext.getTop() + 220 + prijemicano);
      prijemicano = prijemicano + 220;
    }
  }

  private void addElements() {
    LabelR_A_C_U_N = addModel(ep.LABEL, LabelR_A_C_U_NProps);
    Rectangle2 = addModel(ep.RECTANGLE, Rectangle2Props);
    Rectangle1 = addModel(ep.RECTANGLE, Rectangle1Props);
    Rectangle3 = addModel(ep.RECTANGLE, Rectangle3Props);
    Text1 = addModel(ep.TEXT, Text1Props);
//    Rectangle1 = addModel(ep.RECTANGLE, Rectangle1Props);
//    Rectangle2 = addModel(ep.RECTANGLE, Rectangle2Props);
//    Rectangle3 = addModel(ep.RECTANGLE, Rectangle3Props);
    Line1 = addModel(ep.LINE, Line1Props);
    Line2 = addModel(ep.LINE, Line2Props);
    Line3 = addModel(ep.LINE, Line3Props);
    Label1 = addModel(ep.LABEL, Label1Props);
    TextFormatBroj = addModel(ep.TEXT, TextFormatBrojProps);
    LabelBroj = addModel(ep.LABEL, LabelBrojProps);
    Line4 = addModel(ep.LINE, Line4Props);
    TextADRKUPCA = addModel(ep.TEXT, TextADRKUPCAProps);
    TextPbrMjestoKupca = addModel(ep.TEXT, TextPbrMjestoKupcaProps);
    LabelMjestoDatum = addModel(ep.LABEL, LabelMjestoDatumProps);
    TextLogoMjestoZarez = addModel(ep.TEXT, TextLogoMjestoZarezProps);
    TextSgetDATDOK = addModel(ep.TEXT, TextSgetDATDOKProps);
    TextSgetDVO = addModel(ep.TEXT, TextSgetDVOProps);
    LabelDatum_isporuke = addModel(ep.LABEL, LabelDatum_isporukeProps);
    TextNAZNAC = addModel(ep.TEXT, TextNAZNACProps);
    LabelNacin_otpreme = addModel(ep.LABEL, LabelNacin_otpremeProps);
    TextCKUPAC = addModel(ep.TEXT, TextCKUPACProps);
    TextJMBG = addModel(ep.TEXT, TextJMBGProps);
    LabelParitet = addModel(ep.LABEL, LabelParitetProps);
    TextNAZFRA = addModel(ep.TEXT, TextNAZFRAProps);
    LabelObrazac = addModel(ep.LABEL, LabelObrazacProps);
    LabelR1 = addModel(ep.LABEL, LabelR1Props);
  }

  private void modifyThis() {
  }
}
