/****license*****************************************************************
**   file: raSectionFooter0Sum.java
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

public class raSectionFooter0Sum extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "340"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "", "10740", "280", "Normal", "Gray",
     "", "", "", "", "Lucida Bright", "", "Bold", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IRAZ\")", "", "",
     "Number|false|1|309|2|2|true|3|true", "", "", "", "", "9360", "40", "1340", "220", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement LabelU_K_U_P;
  private String[] LabelU_K_U_PProps = new String[] {"U K U P N O", "", "", "40", "1460", "220", "",
     "", "", "", "", "", "Lucida Bright", "", "Bold", "", "", ""};

  public raSectionFooter0Sum(raReportTemplate owner) {
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
    Label1 = addModel(ep.LABEL, Label1Props);
    Text1 = addModel(ep.TEXT, Text1Props);
    LabelU_K_U_P = addModel(ep.LABEL, LabelU_K_U_PProps);
  }

  private void modifyThis() {
  }
}
