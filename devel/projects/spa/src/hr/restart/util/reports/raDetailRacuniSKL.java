/****license*****************************************************************
**   file: raDetailRacuniSKL.java
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

import hr.restart.robno.Aut;

public class raDetailRacuniSKL extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "940"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(if (= [ImaPor1] 1) (put-var \"tpor1\" 1) \"\")",
     "", "", "", "", "", "", "", "3580", "", "0", "0", "", "", "", "", "", "", "", "7", "", "", "",
     "", "No"};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "",
     "800", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "1300", "",
     "8060", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "",
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "9860", "", "960", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(if (= [ImaPor3] 1) (put-var \"tpor3\" 1) \"\")",
     "", "", "", "", "", "", "", "5260", "", "0", "0", "", "", "", "", "", "", "Arial", "7", "", "",
     "", "", "No"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "9380", "", "460",
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(if (= [ImaPor2] 1) (put-var \"tpor2\" 1) \"\")",
     "", "", "", "", "", "", "", "4280", "", "0", "0", "", "", "", "", "", "", "Arial", "7", "", "",
     "", "", "No"};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "460",
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
/*  public raReportElement Text4;
  private String[] Text4Props = new String[] {
     "=(put-var \"H2\" (+ (get-var \"H2\") (if (= [ImaPor2] 0) 0 [IPRODBP])))\n\n", "", "", "", "",
     "", "", "", "1200", "", "0", "0", "", "", "", "", "", "", "Arial", "7", "", "", "", "", "No"};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {
     "=(put-var \"H3\" (+ (get-var \"H3\") (if (= [ImaPor3] 0) 0 [IPRODBP])))", "", "", "", "", "",
     "", "", "2580", "", "0", "0", "", "", "", "", "", "", "Arial", "7", "", "", "", "", "No"};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {
     "=(put-var \"H1\" (+ (get-var \"H1\") (if (= [ImaPor1] 0) 0 [IPRODBP])))", "", "", "", "", "",
     "", "", "100", "", "0", "0", "", "", "", "", "", "", "Arial", "7", "", "", "", "", "No"};*/

  public raDetailRacuniSKL(raReportTemplate owner) {
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
    Text1 = addModel(ep.TEXT, Text1Props);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    Text2 = addModel(ep.TEXT, Text2Props);
    TextJM = addModel(ep.TEXT, TextJMProps);
    Text3 = addModel(ep.TEXT, Text3Props);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
//    Text4 = addModel(ep.TEXT, Text4Props);
//    Text5 = addModel(ep.TEXT, Text5Props);
//    Text6 = addModel(ep.TEXT, Text6Props);
  }

  private void modifyThis() {
    resizeElement(this.TextCART, Aut.getAut().getIzlazCARTwidth(), this.TextNAZART);
  }
}
