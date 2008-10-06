/****license*****************************************************************
**   file: raRadniNalogStatusDatumSection_Header1.java
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

public class raRadniNalogStatusDatumSection_Header1 extends raReportSection {

  private String[] thisProps = new String[] {"BRDOKRNL", "", "", "", "Yes", "No", "", "No", "360"};
  public raReportElement TextDATUMDOK;
  private String[] TextDATUMDOKProps = new String[] {"DATUMDOK", "", "", "", "", "", "Yes", "Yes",
     "9400", "", "1460", "260", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "",
     "Right", "No"};
  public raReportElement LabelStatus;
  private String[] LabelStatusProps = new String[] {"Status", "", "5080", "", "680", "260", "", "",
     "", "", "", "", "Lucida Bright", "9", "", "", "", ""};
//  public raReportElement LabelOpis;
//  private String[] LabelOpisProps = new String[] {"Opis", "", "240", "", "1300", "260", "", "", "",
//     "", "", "", "Lucida Bright", "9", "", "", "", ""};
  public raReportElement LabelDatum;
  private String[] LabelDatumProps = new String[] {"Datum", "", "8400", "", "880", "260", "", "",
     "", "", "", "", "Lucida Bright", "9", "", "", "", ""};
  public raReportElement TextSTATUS;
  private String[] TextSTATUSProps = new String[] {"STATUS", "", "", "", "", "", "Yes", "Yes",
     "5920", "", "1460", "260", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "",
     "No"};

  public raRadniNalogStatusDatumSection_Header1(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 1));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
    System.out.println("raRadniNalogStatusDatumSection_Header1 - done");
  }

  private void addElements() {
    TextDATUMDOK = addModel(ep.TEXT, TextDATUMDOKProps);
    LabelStatus = addModel(ep.LABEL, LabelStatusProps);
//    LabelOpis = addModel(ep.LABEL, LabelOpisProps);
    LabelDatum = addModel(ep.LABEL, LabelDatumProps);
    TextSTATUS = addModel(ep.TEXT, TextSTATUSProps);
  }

  private void modifyThis() {
  }
}
