/****license*****************************************************************
**   file: raRacRnalSumSection_Footer2.java
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

public class raRacRnalSumSection_Footer2 extends raReportSection {

  private String[] thisProps = new String[] {"MatUslGrouping", "", "", "", "Yes", "", "Yes", /*"500"*/"400"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"/", "", "", "20", "10800", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "60", "10820", "280", "Normal", "Gray",
     "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {"UKUPNO", "", "200", "100", "920", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement TextRADOVIMATERIJAL;
  private String[] TextRADOVIMATERIJALProps = new String[] {"RADOVIMATERIJAL", "", "", "", "", "",
     "", "", "1120", "100", "3160", "220", "", "-1973791", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"INETO\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9420", /*"100"*/"60", "1400", "220", "", "",
     "", "", "", "", "Lucida Bright", "8",/* "Bold"*/"", "", "", "Right", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"/", "", "", "280", "10800", "0", "", "", ""};

  public raRacRnalSumSection_Footer2(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_FOOTER + 2));
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
//    Label1 = addModel(ep.LABEL, Label1Props);
//    LabelUKUPNO = addModel(ep.LABEL, LabelUKUPNOProps);
//    TextRADOVIMATERIJAL = addModel(ep.TEXT, TextRADOVIMATERIJALProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
