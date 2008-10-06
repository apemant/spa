/****license*****************************************************************
**   file: raDosDetailjmNI.java
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
import hr.restart.sisfun.frmParam;

public class raDosDetailjmNI extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "320"};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "20", "", "440", 
      "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "", 
      "1940", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "2440", 
      "", "3040", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
//  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "2440", 
//      "", "3840", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextKOL;
  public raReportElement TextKOL1;
  private String[] TextKOL1Props = new String[] {"KOL1", "", "", 
      "Number|false|1|309|3|3|true|3|false", "", "", "", "", "6140", "", "1160", "220", "", "", "", 
      "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
//"Number|false|1|309|3|3|true|3|false", "", "", "", "", "6300", "", "1160", "220", "", "", "", 
//"", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "5500", "", "620", 
      "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", "No"};
//  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "7580", "", "620", 
//      "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", "No"};
  public raReportElement TextKOL2;
  private String[] TextKOL2Props = new String[] {"KOL2", "", "", 
      "Number|false|1|309|3|3|true|3|false", "", "", "", "", "7400", "", "1700", "220", "", "", "", 
      "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
//  "Number|false|1|309|3|3|true|3|false", "", "", "", "", "8200", "", "1300", "220", "", "", "", 
//  "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  private String[] TextKOLProps = new String[] {
      /*"KOL"*/
      "=(if (> [KOL] 0) [KOL] \"\")"
      ,"", "", 
      "Number|false|1|309|3|3|true|3|false", "", "", "", "", "9120", "", "1700", "220", "", "", "", 
      "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
//private String[] TextKOLProps = new String[] {"KOL", "", "", 
//"Number|false|1|309|3|3|true|3|false", "", "", "", "", "9520", "", "1300", "220", "", "", "", 
//"", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};

  public raDosDetailjmNI(raReportTemplate owner) {
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
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextKOL1 = addModel(ep.TEXT, TextKOL1Props);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextKOL2 = addModel(ep.TEXT, TextKOL2Props);
  }

  private void modifyThis() {
    this.restoreDefaults();
    this.resizeElement(this.TextCART, Aut.getAut().getIzlazCARTwidth(), this.TextNAZART);
    String id = frmParam.getParam("robno", "ispDOS", "D",
    "Ispisati isporuèenu kolièinu na dostavnici (D,N)?");
    if (id.equalsIgnoreCase("N"))
      TextKOL.setControlSource("");
  }
}
