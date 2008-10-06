/****license*****************************************************************
**   file: raUslDetail.java
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

public class raUslDetail extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "Yes", "360"};
  public raReportElement TextIPRODBP;
  private String[] TextIPRODBPProps = new String[] {"IPRODBP", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8960", "", "1860", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
//  public raReportElement Label1;
//  private String[] Label1Props = new String[] {".", "", "380", "", "120", "220", "", "", "", "", "",
//     "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBRS", "", "", "", "", "", "", "", "20", "", "440",
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(if (= [ImaPor1] 1) (put-var \"tpor1\" 1) \"\")",
     "", "", "", "No", "", "", "", "2300", "", "0", "0", "", "", "", "", "", "", "", "", "", "", "",
     "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(if (= [ImaPor3] 1) (put-var \"tpor3\" 1) \"\")",
     "", "", "", "No", "", "", "", "3120", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "",
     "", "", "", ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "480",
     "", "8480", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextNAZARText;
  private String[] TextNAZARTextProps = new String[] {"NAZARText", "", "", "", "", "", "Yes", "", "480",
     "220", "8480", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(if (= [ImaPor2] 1) (put-var \"tpor2\" 1) \"\")",
     "", "", "", "No", "", "", "", "2640", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "",
     "", "", "", ""};
/*  public raReportElement Text4;
  private String[] Text4Props = new String[] {
     "=(put-var \"H2\" (+ (get-var \"H2\")\n(if (= [ImaPor2] 0) 0 [IPRODBP]))\n                           )\n)\n",
     "", "", "", "No", "", "", "", "1820", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "",
     "", "", "", ""};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {
     "=(put-var \"H3\" (+ (get-var \"H3\")\n(if (= [ImaPor3] 0) 0. [IPRODBP])) ))\n", "", "", "",
     "No", "", "", "", "1280", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", "", "", "",
     ""};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {
     "=(put-var \"H1\" (+ (get-var \"H1\")\n(if (= [ImaPor1] 0) 0. [IPRODBP]))\n                           )\n)\n",
     "", "", "", "No", "", "", "", "780", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "",
     "", "", "", ""}; */

  public raUslDetail(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.DETAIL));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    TextIPRODBP = addModel(ep.TEXT, TextIPRODBPProps);
//    Label1 = addModel(ep.LABEL, Label1Props);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    Text3 = addModel(ep.TEXT, Text3Props);
//    Text4 = addModel(ep.TEXT, Text4Props);
//    Text5 = addModel(ep.TEXT, Text5Props);
//    Text6 = addModel(ep.TEXT, Text6Props);
    TextNAZARText = addModel(ep.TEXT, TextNAZARTextProps);
  }

  private void modifyThis() {
  }
}
