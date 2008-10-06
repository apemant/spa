/****license*****************************************************************
**   file: raDOSPopCDetail.java
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

public class raDOSPopCDetail extends raReportSection {

  private String[] thisProps = new String[] {"RBR", "", "", "", "", "", "", "280"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "7660", "", "1080", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "6060", "", "480", 
     "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "440", "", 
     "1700", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextCJC;
  private String[] TextCJCProps = new String[] {"CJC", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8760", "", "1180", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextPPOP;
  private String[] TextPPOPProps = new String[] {"PPOP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9960", "", "840", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "400", 
     "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "", "", "2180", "", 
     "3820", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextKOL1;
  private String[] TextKOL1Props = new String[] {"KOL1", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "6560", "", "1080", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};

  public raDOSPopCDetail(raReportTemplate owner) {
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
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextCJC = addModel(ep.TEXT, TextCJCProps);
    TextPPOP = addModel(ep.TEXT, TextPPOPProps);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextKOL1 = addModel(ep.TEXT, TextKOL1Props);
  }

  private void modifyThis() {
    this.restoreDefaults();
    this.resizeElement(this.TextCART, Aut.getAut().getIzlazCARTwidth(), this.TextNAZART);
  }
}
