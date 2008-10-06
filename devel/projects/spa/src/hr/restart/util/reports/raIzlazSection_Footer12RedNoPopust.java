/****license*****************************************************************
**   file: raIzlazSection_Footer12RedNoPopust.java
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

public class raIzlazSection_Footer12RedNoPopust extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "800"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "100", "10800", "0", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IPRODBP\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6520", "140", "", "240", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"IPRODSP\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9380", "140", "", "240", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"UKPOR3\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7980", "140", "1380", "240", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "140", "6500", "240", "Normal", "Gray", 
     "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement LabelU_K_U_P_N_O;
  private String[] LabelU_K_U_P_N_OProps = new String[] {"U K U P N O", "", "80", "140", "1480", 
     "280", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "400", "10800", "0", "", "", ""};
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "", "900", 
     "500", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelSlovima_;
  private String[] LabelSlovima_Props = new String[] {"Slovima :", "", "40", "500", "840", "220", 
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};

  public raIzlazSection_Footer12RedNoPopust(raReportTemplate owner) {
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
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    Text3 = addModel(ep.TEXT, Text3Props);
    Label1 = addModel(ep.LABEL, Label1Props);
    LabelU_K_U_P_N_O = addModel(ep.LABEL, LabelU_K_U_P_N_OProps);
    Line2 = addModel(ep.LINE, Line2Props);
    TextSLOVIMA = addModel(ep.TEXT, TextSLOVIMAProps);
    LabelSlovima_ = addModel(ep.LABEL, LabelSlovima_Props);
  }

  private void modifyThis() {
  }
}
