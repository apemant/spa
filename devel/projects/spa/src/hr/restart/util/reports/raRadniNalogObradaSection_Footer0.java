/****license*****************************************************************
**   file: raRadniNalogObradaSection_Footer0.java
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

public class raRadniNalogObradaSection_Footer0 extends raReportSection {

  private String[] thisProps = new String[] {"BRDOKRNL", "After", "", "", "Yes", "", "Yes", "2520"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "160", "20", "10680", "300", "Normal", "Gray",
     "", "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IZNOS\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9640", "60", "1200", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement LabelU_K_U_P_N_O;
  private String[] LabelU_K_U_P_N_OProps = new String[] {"U K U P N O", "", "160", "60", "1460",
     "220", "", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "160", "360", "10660", "0", "", "", ""};
  public raReportElement LabelNapomena_;
  private String[] LabelNapomena_Props = new String[] {"Napomena :", "", "160", "580", "1300",
     "280", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextNAZNAP;
  private String[] TextNAZNAPProps = new String[] {"NAZNAP", "", "", "", "", "", "Yes", "Yes",
     "160", "880", "10540", "580", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};

  public raRadniNalogObradaSection_Footer0(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_FOOTER + 0));
    this.setDefaults(thisProps);

    addElements();

    final raReportTemplate ownerFF = owner;

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis(ownerFF);
      }
    });
  }

  private void addElements() {
    Label1 = addModel(ep.LABEL, Label1Props);
    Text1 = addModel(ep.TEXT, Text1Props);
    LabelU_K_U_P_N_O = addModel(ep.LABEL, LabelU_K_U_P_N_OProps);
    Line1 = addModel(ep.LINE, Line1Props);
    LabelNapomena_ = addModel(ep.LABEL, LabelNapomena_Props);
    TextNAZNAP = addModel(ep.TEXT, TextNAZNAPProps);
  }

  private void modifyThis(raReportTemplate owner) {
    if ((owner instanceof hr.restart.robno.repObracunRadnogNalogaTemplate)||
        (owner instanceof hr.restart.robno.repStavkeRadnogNalogaTemplate)){
      Label1.setVisible(false);
      LabelU_K_U_P_N_O.setVisible(false);
      Text1.setVisible(false);
      Line1.setVisible(false);
//      removeMoElement(Label1);
//      deflateElement(LabelU_K_U_P_N_O);
//      deflateElement(Text1);
//      this.TextATTRIBVALUE.setProperty(raElixirProperties.WIDTH,""+width2);//"3880");
      this.LabelNapomena_.setProperty(raElixirProperties.TOP,"320");
      this.TextNAZNAP.setProperty(raElixirProperties.TOP,"620");
    }
  }
}
