/****license*****************************************************************
**   file: raIOS_OpomenaSH1.java
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

public class raIOS_OpomenaSH1 extends raReportSection {

  private String[] thisProps = new String[] {"CPAR", "Before", "", "", "Yes", "", "Yes", "", "2240"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "0", "10680", "0", "", "", ""};
  public raReportElement TextOneLine;
  private String[] TextOneLineProps = new String[] {"OneLine", "", "", "", "", "", "", "", "", "80", 
     "6400", "260", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", ""};
  public raReportElement LabelPRILOG_1;
  private String[] LabelPRILOG_1Props = new String[] {"PRILOG 1", "", "", "380", "4420", "240", "", 
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", ""};
  public raReportElement LabelIZVADAK;
  private String[] LabelIZVADAKProps = new String[] {"\nIZVADAK", "", "", "500", "10740", "600", "", 
     "", "", "", "", "", "Lucida Bright", "12", "Bold", "", "", "Center"};
  public raReportElement TextPODNASLOVPRILOGA;
  private String[] TextPODNASLOVPRILOGAProps = new String[] {"PODNASLOVPRILOGA", "", "", "", "", "", 
     "", "", "", "1080", "10740", "280", "", "", "", "", "", "", "Lucida Bright", "12", "Bold", "", 
     "", "Center", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "1520", "10680", "0", "", "", ""};
  public raReportElement TextPartnerText;
  private String[] TextPartnerTextProps = new String[] {"PartnerText", "", "", "", "", "", "Yes", 
     "", "1420", "1560", "9100", "260", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", 
     "", ""};
  public raReportElement TextLabelKUPDOB;
  private String[] TextLabelKUPDOBProps = new String[] {"LabelKUPDOB", "", "", "", "", "", "", "", 
     "", "1560", "1420", "260", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "", 
     ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "1840", "10680", "0", "", "", ""};
  public raReportElement LabelDVO;
  private String[] LabelDVOProps = new String[] {"DVO", "", "2120", "1880", "1800", "240", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSaldo_POTRAZUJE;
  private String[] LabelSaldo_POTRAZUJEProps = new String[] {"Saldo POTRAŽUJE", "", "8240", "1880", 
     "2460", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelSaldo_DUGUJE;
  private String[] LabelSaldo_DUGUJEProps = new String[] {"Saldo DUGUJE", "", "5760", "1880", 
     "2460", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelDospjece;
  private String[] LabelDospjeceProps = new String[] {"Dospje\u0107e", "", "3940", "1880", "1800", 
     "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelBroj_dokumenta;
  private String[] LabelBroj_dokumentaProps = new String[] {"Broj dokumenta", "", "", "1880", 
     "2100", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "", "", "2140", "10680", "0", "", "", ""};

  public raIOS_OpomenaSH1(raReportTemplate owner) {
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
    Line1 = addModel(ep.LINE, Line1Props);
    TextOneLine = addModel(ep.TEXT, TextOneLineProps);
    LabelPRILOG_1 = addModel(ep.LABEL, LabelPRILOG_1Props);
    LabelIZVADAK = addModel(ep.LABEL, LabelIZVADAKProps);
    TextPODNASLOVPRILOGA = addModel(ep.TEXT, TextPODNASLOVPRILOGAProps);
    Line2 = addModel(ep.LINE, Line2Props);
    TextPartnerText = addModel(ep.TEXT, TextPartnerTextProps);
    TextLabelKUPDOB = addModel(ep.TEXT, TextLabelKUPDOBProps);
    Line3 = addModel(ep.LINE, Line3Props);
    LabelDVO = addModel(ep.LABEL, LabelDVOProps);
    LabelSaldo_POTRAZUJE = addModel(ep.LABEL, LabelSaldo_POTRAZUJEProps);
    LabelSaldo_DUGUJE = addModel(ep.LABEL, LabelSaldo_DUGUJEProps);
    LabelDospjece = addModel(ep.LABEL, LabelDospjeceProps);
    LabelBroj_dokumenta = addModel(ep.LABEL, LabelBroj_dokumentaProps);
    Line4 = addModel(ep.LINE, Line4Props);
  }

  private void modifyThis() {
  }
}
