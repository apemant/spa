/****license*****************************************************************
**   file: raOSSectionFooter002.java
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

public class raOSSectionFooter002 extends raReportSection {

  private String[] thisProps = new String[] {"FirstLine", "After", "", "", "Yes", "", "Yes", "420"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "0", "15660", "0", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum\"OsnVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "8400", "40", "2380", "220", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", "No"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "40", "8380", "240", "Normal", "Gray",
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "8400", "40", "2420", "240", "Normal",
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label3;
  private String[] Label3Props = new String[] {"", "", "10840", "40", "2420", "240", "Normal",
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label4;
  private String[] Label4Props = new String[] {"", "", "13280", "40", "2400", "240", "Normal",
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum\"SadVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "13300", "40", "2360", "220", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", "No"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum\"IspVr\")", "", "", "Number|true|#.##0,00",
     "", "", "", "", "10840", "40", "2400", "220", "", "", "", "", "", "", "Lucida Bright", "8",
     "Bold", "", "", "Right", "No"};
  public raReportElement LabelU_K_U_P_N_O;
  private String[] LabelU_K_U_P_N_OProps = new String[] {"U K U P N O", "", "", "60", "1920", "220",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "300", "15660", "0", "", "", ""};

  public raOSSectionFooter002(raReportTemplate owner) {
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
    Label1 = addModel(ep.LABEL, Label1Props);
    Label2 = addModel(ep.LABEL, Label2Props);
    Label3 = addModel(ep.LABEL, Label3Props);
    Label4 = addModel(ep.LABEL, Label4Props);
    LabelU_K_U_P_N_O = addModel(ep.LABEL, LabelU_K_U_P_N_OProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    Text3 = addModel(ep.TEXT, Text3Props);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
