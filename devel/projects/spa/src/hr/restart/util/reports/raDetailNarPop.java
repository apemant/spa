/****license*****************************************************************
**   file: raDetailNarPop.java
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

public class raDetailNarPop extends raReportSection {

  private String[] thisProps = new String[] {"RBR", "", "", "", "", "", "", "340"};
  public raReportElement TextIBPNAR;
  private String[] TextIBPNARProps = new String[] {"IBPNAR", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9640", "", "1200", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "6260", "", "1240", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "400", 
     "220", "", "-4275514", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "1800", "", 
     "3940", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextNAZARText;
  private String[] TextNAZARTextProps = new String[] {"NAZARText", "", "", "", "", "", "Yes", "Yes", "1800", "220", 
     "7820", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextUPRAB;
  private String[] TextUPRABProps = new String[] {"UPRAB", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8740", "", "880", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextNC;
  private String[] TextNCProps = new String[] {"NC", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "7520", "", "1200", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", ""};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "420", "", 
     "1360", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "5760", "", "480", 
     "220", "", "-4275514", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};

  public raDetailNarPop(raReportTemplate owner) {
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
    TextIBPNAR = addModel(ep.TEXT, TextIBPNARProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextUPRAB = addModel(ep.TEXT, TextUPRABProps);
    TextNC = addModel(ep.TEXT, TextNCProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextNAZARText = addModel(ep.TEXT, TextNAZARTextProps);
  }

  private void modifyThis() {
    boolean wide = frmParam.getParam("robno", "extNazartWide", "N", 
        "Široki dodatni opis artikla (D,N)?").equalsIgnoreCase("D");
    if (!wide) TextNAZARText.setWidth(TextNAZART.getWidth());
  }
}
