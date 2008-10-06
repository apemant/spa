/****license*****************************************************************
**   file: RaRobnoMiniSaldakSH1.java
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

public class RaRobnoMiniSaldakSH1 extends raReportSection {

  private String[] thisProps = new String[] {"FirstLine", "Before", "", "", "Yes", "", "", "Yes", 
     "2900"};
  public raReportElement TextFirstLine;
  private String[] TextFirstLineProps = new String[] {"FirstLine", "", "", "", "", "", "", "", "60", 
     "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextFirstLine1;
  private String[] TextFirstLine1Props = new String[] {"FirstLine", "", "", "", "", "", "", "", 
     "60", "", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextSecondLine;
  private String[] TextSecondLineProps = new String[] {"SecondLine", "", "", "", "", "", "", "", 
     "60", "240", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", 
     ""};
  public raReportElement TextSecondLine1;
  private String[] TextSecondLine1Props = new String[] {"SecondLine", "", "", "", "", "", "", "", 
     "60", "240", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", 
     ""};
  public raReportElement TextThirdLine;
  private String[] TextThirdLineProps = new String[] {"ThirdLine", "", "", "", "", "", "", "", "60", 
     "480", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement TextThirdLine1;
  private String[] TextThirdLine1Props = new String[] {"ThirdLine", "", "", "", "", "", "", "", 
     "60", "480", "5660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", 
     ""};
  public raReportElement LabelPRILOG_1;
  private String[] LabelPRILOG_1Props = new String[] {"PRILOG 1", "", "60", "820", "1320", "220", 
     "", "", "", "", "", "", "Lucida Bright", "", "", "", "", ""};
  public raReportElement TextNASLOV;
  private String[] TextNASLOVProps = new String[] {"NASLOV", "", "", "", "", "", "", "", "", "1060", 
     "10740", "660", "", "", "", "", "", "", "Lucida Bright", "14", "Bold", "", "", "Center", ""};
  public raReportElement TextStatusDospjece;
  private String[] TextStatusDospjeceProps = new String[] {"StatusDospjece", "", "", "", "", "", 
     "Yes", "", "", "1700", "10740", "340", "", "", "", "", "", "", "Lucida Bright", "11", "", "", 
     "", "Center", ""};
  public raReportElement LabelPartner;
  private String[] LabelPartnerProps = new String[] {"Partner", "", "", "2220", "1320", "220", "", 
     "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextCPAR;
  private String[] TextCPARProps = new String[] {"CPAR", "", "", "", "", "", "", "", "1340", "2220", 
     "660", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZPAR;
  private String[] TextNAZPARProps = new String[] {"NAZPAR", "", "", "", "", "", "", "", "2040", 
     "2220", "4520", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "2500", "10700", "0", "", "", ""};
  public raReportElement LabelIznos_uplata;
  private String[] LabelIznos_uplataProps = new String[] {"Iznos uplata", "", "7440", "2540", 
     "1640", "240", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", 
     "Center"};
  public raReportElement LabelDospijece;
  private String[] LabelDospijeceProps = new String[] {"Dospije\u0107e", "", "3980", "2540", "1780", 
     "240", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", 
     "Center"};
  public raReportElement LabelDVO;
  private String[] LabelDVOProps = new String[] {"DVO", "", "2180", "2540", "1780", "240", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelBroj_dokumenta;
  private String[] LabelBroj_dokumentaProps = new String[] {"Broj dokumenta", "", "", "2540", 
     "2160", "240", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", 
     "Center"};
  public raReportElement LabelSaldo;
  private String[] LabelSaldoProps = new String[] {"Saldo", "", "9100", "2540", "1640", "240", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelIznos_racuna;
  private String[] LabelIznos_racunaProps = new String[] {"Iznos ra\u010Duna", "", "5780", "2540", 
     "1640", "240", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", 
     "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "2800", "10700", "0", "", "", ""};

  public RaRobnoMiniSaldakSH1(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 1));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    TextFirstLine = addModel(ep.TEXT, TextFirstLineProps);
    TextFirstLine1 = addModel(ep.TEXT, TextFirstLine1Props);
    TextSecondLine = addModel(ep.TEXT, TextSecondLineProps);
    TextSecondLine1 = addModel(ep.TEXT, TextSecondLine1Props);
    TextThirdLine = addModel(ep.TEXT, TextThirdLineProps);
    TextThirdLine1 = addModel(ep.TEXT, TextThirdLine1Props);
    LabelPRILOG_1 = addModel(ep.LABEL, LabelPRILOG_1Props);
    TextNASLOV = addModel(ep.TEXT, TextNASLOVProps);
    TextStatusDospjece = addModel(ep.TEXT, TextStatusDospjeceProps);
    LabelPartner = addModel(ep.LABEL, LabelPartnerProps);
    TextCPAR = addModel(ep.TEXT, TextCPARProps);
    TextNAZPAR = addModel(ep.TEXT, TextNAZPARProps);
    Line1 = addModel(ep.LINE, Line1Props);
    LabelIznos_uplata = addModel(ep.LABEL, LabelIznos_uplataProps);
    LabelDospijece = addModel(ep.LABEL, LabelDospijeceProps);
    LabelDVO = addModel(ep.LABEL, LabelDVOProps);
    LabelBroj_dokumenta = addModel(ep.LABEL, LabelBroj_dokumentaProps);
    LabelSaldo = addModel(ep.LABEL, LabelSaldoProps);
    LabelIznos_racuna = addModel(ep.LABEL, LabelIznos_racunaProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
