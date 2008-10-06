/****license*****************************************************************
**   file: raRadniNalogSection_Footer0.java
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

public class raRadniNalogSection_Footer0 extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "After", "", "", "Yes", "", "Yes", "2680"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "160", "0", "10660", "0", "", "", ""};
  public raReportElement TextOPIS;
  private String[] TextOPISProps = new String[] {"OPIS", "", "", "", "", "", "Yes", "Yes", "240",
     "60", "10240", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "160", "300", "10660", "0", "", "", ""};
  public raReportElement LabelNapomena_;
  private String[] LabelNapomena_Props = new String[] {"Napomena :", "", "240", "720", "1300",
     "280", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextNAZNAP;
  private String[] TextNAZNAPProps = new String[] {"NAZNAP", "", "", "", "", "", "Yes", "Yes",
     "240", "1020", "10560", "200", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "",
     ""};
//  public raReportElement TextKORISNIK;
//  private String[] TextKORISNIKProps = new String[] {"KORISNIK", "", "", "", "", "", "Yes", "Yes",
//     "1460", "1480", "2920", "260", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "",
//     ""};
//  public raReportElement LabelZa_korisnika;
//  private String[] LabelZa_korisnikaProps = new String[] {"Za korisnika", "", "240", "1480", "1220",
//     "260", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", ""};
//  public raReportElement TextUSEROTVORIO;
//  private String[] TextUSEROTVORIOProps = new String[] {"USEROTVORIO", "", "", "", "", "", "Yes",
//     "Yes", "240", "1920", "1460", "260", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "",
//     "", "No"};

  public raRadniNalogSection_Footer0(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_FOOTER + 0));
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
    TextOPIS = addModel(ep.TEXT, TextOPISProps);
    Line2 = addModel(ep.LINE, Line2Props);
    LabelNapomena_ = addModel(ep.LABEL, LabelNapomena_Props);
    TextNAZNAP = addModel(ep.TEXT, TextNAZNAPProps);
//    TextKORISNIK = addModel(ep.TEXT, TextKORISNIKProps);
//    LabelZa_korisnika = addModel(ep.LABEL, LabelZa_korisnikaProps);
//    TextUSEROTVORIO = addModel(ep.TEXT, TextUSEROTVORIOProps);
  }

  private void modifyThis() {
    System.out.println("EM AJ MODIFAJING, OR WHAT!!!");
  }
}
