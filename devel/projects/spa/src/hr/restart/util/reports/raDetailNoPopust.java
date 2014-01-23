/****license*****************************************************************
**   file: raDetailNoPopust.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

public class raDetailNoPopust extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "300"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {
     "=(put-var \"H2\" (+ (get-var \"H2\")\n(if (= [ImaPor2] 0) 0 [IPRODBP]))\n                           )\n)\n", 
     "", "", "", "No", "", "", "", "1820", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "6480", "", "460", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "5500", "", "960", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextFC;
  private String[] TextFCProps = new String[] {"FC", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "6960", "", "1220", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "440", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(if (= [ImaPor2] 1) (put-var \"tpor2\" 1) \"\")", 
     "", "", "", "No", "", "", "", "2640", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(if (= [ImaPor3] 1) (put-var \"tpor3\" 1) \"\")", 
     "", "", "", "No", "", "", "", "3120", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(if (= [ImaPor1] 1) (put-var \"tpor1\" 1) \"\")", 
     "", "", "", "No", "", "", "", "2300", "", "0", "0", "", "", "", "", "", "", "", "", "", "", "", 
     "", ""};
  public raReportElement TextPor1p2p3Naz;
  private String[] TextPor1p2p3NazProps = new String[] {"Por1p2p3Naz", "", "", 
     "Number|false|1|3|2|2|true|3|false", "", "", "", "", "8200", "", "1200", "220", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {
     "=(put-var \"H1\" (+ (get-var \"H1\")\n(if (= [ImaPor1] 0) 0. [IPRODBP]))\n                           )\n)\n", 
     "", "", "", "No", "", "", "", "780", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", 
     "", "", "", ""};
  public raReportElement TextIPRODBP;
  private String[] TextIPRODBPProps = new String[] {"IPRODBP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9420", "", "1400", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "", 
     "800", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text6;
  private String[] Text6Props = new String[] {
     "=(put-var \"H3\" (+ (get-var \"H3\")\n(if (= [ImaPor3] 0) 0. [IPRODBP])) ))\n", "", "", "", 
     "No", "", "", "", "1280", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", "", "", "", 
     ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "1300", 
     "", "4180", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextNAZARText;
  private String[] TextNAZARTextProps = new String[] {"NAZARText", "", "", "", "", "", "Yes", "", "1300", 
     "220", "8080", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};

  public raDetailNoPopust(raReportTemplate owner) {
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
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextFC = addModel(ep.TEXT, TextFCProps);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    Text2 = addModel(ep.TEXT, Text2Props);
    Text3 = addModel(ep.TEXT, Text3Props);
    Text4 = addModel(ep.TEXT, Text4Props);
    TextPor1p2p3Naz = addModel(ep.TEXT, TextPor1p2p3NazProps);
    Text5 = addModel(ep.TEXT, Text5Props);
    TextIPRODBP = addModel(ep.TEXT, TextIPRODBPProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    Text6 = addModel(ep.TEXT, Text6Props);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextNAZARText = addModel(ep.TEXT, TextNAZARTextProps);
  }

  private void modifyThis() {
    boolean wide = frmParam.getParam("robno", "extNazartWide", "N", 
        "Široki dodatni opis artikla (D,N)?").equalsIgnoreCase("D");
    if (!wide) TextNAZARText.setWidth(TextNAZART.getWidth());
    
    int decskol = Aus.getNumber(frmParam.getParam("robno", "kolDec", 
        "3", "Broj decimala za kolicinu na izlazu (0-4)").trim());
    if (decskol < 0) decskol = 0;
    if (decskol > 4) decskol = 4;
    TextKOL.setProperty(ep.FORMAT, "Number|false|1|309|"+decskol+"|"+decskol+"|true|3|false");
  }
}
