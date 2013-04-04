/****license*****************************************************************
**   file: raGRSectionHeader.java
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

public class raGRSectionHeader extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "Before", "", "", "Yes", "", "Yes", "",
     "1960"};
  public raReportElement TextSgetDATDOK;
  private String[] TextSgetDATDOKProps = new String[] {"SgetDATDOK", "", "hr_HR",
     "Date|false|Short", "", "", "", "", "9600", "60", "1200", "220", "", "", "", "", "", "",
     "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement LabelProdajno_mjesto;
  private String[] LabelProdajno_mjestoProps = new String[] {"Prodajno mjesto", "", "60", "60",
     "1580", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelMjesto;
  private String[] LabelMjestoProps = new String[] {"Mjesto/datum izdavanja", "", "6220", "60", "1580", "220", "",
     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextCSKL;
  private String[] TextCSKLProps = new String[] {"CSKL", "", "", "", "", "", "", "", "1560", "60",
     "780", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement TextLogoMjestoZarez;
  private String[] TextLogoMjestoZarezProps = new String[] {"LogoMjestoZarez", "", "", "", "", "",
     "", "", "7880", "60", "1720", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "",
     "", "", ""};
  public raReportElement TextRADNOMJESTO;
  private String[] TextRADNOMJESTOProps = new String[] {"RADNOMJESTO", "", "", "", "", "", "", "",
     "2440", "60", "3720", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "",
     ""};
  public raReportElement LabelNacin_otpreme;
  private String[] LabelNacin_otpremeProps = new String[] {"Na\u010Din otpreme", "", "6220", "260",
     "1580", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZNAC;
  private String[] TextNAZNACProps = new String[] {"NAZNAC", "", "", "", "", "", "", "", "7880",
     "260", "2820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextNAZKUPCA;
  private String[] TextNAZKUPCAProps = new String[] {"NAZKUPCA", "", "", "", "", "", "", "", "840",
     "300", "5080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(if (>[CKUPAC] 0) \"Kupac\" \"\")", "", "", "", "",
     "", "", "", "60", "300", "760", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "",
     "", "", ""};
  public raReportElement LabelParitet;
  private String[] LabelParitetProps = new String[] {"Paritet", "", "6220", "460", "1580", "220",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right"};
  public raReportElement TextNAZFRA;
  private String[] TextNAZFRAProps = new String[] {"NAZFRA", "", "", "", "", "", "", "", "7880",
     "460", "2820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextADR;
  private String[] TextADRProps = new String[] {"ADRKUPCA", "", "", "", "", "", "", "", "840", "500",
     "5080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(if (> [PBRKUPCA] 0) [PBRKUPCA] \"\")", "", "", "", "", "",
     "", "", "840", "700", "720", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "", ""};
  public raReportElement TextMJ;
  private String[] TextMJProps = new String[] {"MJKUPCA", "", "", "", "", "", "", "", "1580", "700",
     "4340", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelR1;
  private String[] LabelR1Props = new String[] {"R-1", "", "10280", "880", "520", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  
  public raReportElement TextJMBG;
  private String[] TextJMBGProps = new String[] {"JMBG", "", "", "", "", "", "", "", "840", "900",
     "5080", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelR_A_C_U;
  private String[] LabelR_A_C_UProps = new String[] {"\nR A \u010C U N", "", "", "980", "10840",
     "640", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center"};
  public raReportElement TextFormatBroj;
  private String[] TextFormatBrojProps = new String[] {"FormatBroj", "", "", "", "", "", "", "", "",
     "1600", "10840", "320", "", "", "", "", "", "", "Lucida Bright", "11", "Bold", "", "",
     "Center", "No"};

  public raGRSectionHeader(raReportTemplate owner) {
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
    TextSgetDATDOK = addModel(ep.TEXT, TextSgetDATDOKProps);
    LabelProdajno_mjesto = addModel(ep.LABEL, LabelProdajno_mjestoProps);
    LabelMjesto = addModel(ep.LABEL, LabelMjestoProps);
    TextCSKL = addModel(ep.TEXT, TextCSKLProps);
    TextLogoMjestoZarez = addModel(ep.TEXT, TextLogoMjestoZarezProps);
    TextRADNOMJESTO = addModel(ep.TEXT, TextRADNOMJESTOProps);
    LabelNacin_otpreme = addModel(ep.LABEL, LabelNacin_otpremeProps);
    TextNAZNAC = addModel(ep.TEXT, TextNAZNACProps);
    TextNAZKUPCA = addModel(ep.TEXT, TextNAZKUPCAProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    LabelParitet = addModel(ep.LABEL, LabelParitetProps);
    TextNAZFRA = addModel(ep.TEXT, TextNAZFRAProps);
    TextADR = addModel(ep.TEXT, TextADRProps);
    Text2 = addModel(ep.TEXT, Text2Props);
    TextMJ = addModel(ep.TEXT, TextMJProps);
    LabelR1 = addModel(ep.LABEL, LabelR1Props);
//    System.out.println("*********************************************");
//    System.out.println("*             DODAJEM OBRAZAC               *");
//    System.out.println("*********************************************");
//    LabelObrazac = addModel(ep.LABEL, LabelObrazacProps);
    TextJMBG = addModel(ep.TEXT, TextJMBGProps);
    LabelR_A_C_U = addModel(ep.LABEL, LabelR_A_C_UProps);
    TextFormatBroj = addModel(ep.TEXT, TextFormatBrojProps);
  }

  private void modifyThis() {
  }
}
