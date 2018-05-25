/****license*****************************************************************
**   file: raKCISectionFooter.java
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

public class raKCISectionFooter extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "Yes", "Yes", "740"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "120", "10800", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "160", "10820", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement LabelU_K_U_P_N_O;
  private String[] LabelU_K_U_P_N_OProps = new String[] {"U K U P N O", "", "", "180", "1460",
     "220", "Normal", "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IRAZ\")", "", "",
     "Number|false|1|309|2|2|true|3|true", "", "", "", "", "9360", "180", "1460", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};

  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "420", "10800", "0", "", "", ""};

  public raKCISectionFooter(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_FOOTER + 1));
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
    Label1 = addModel(ep.LABEL, Label1Props);
    LabelU_K_U_P_N_O = addModel(ep.LABEL, LabelU_K_U_P_N_OProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
