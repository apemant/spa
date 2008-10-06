/****license*****************************************************************
**   file: raRadniNalogObradaDetail.java
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

public class raRadniNalogObradaDetail extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "340"};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "720", "", 
     "1460", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "2220", 
     "", "4340", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextVC;
  private String[] TextVCProps = new String[] {"VC", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "8420", "", "1200", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "6560", "", "620", 
     "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", ""};
  public raReportElement TextIZNOS;
  private String[] TextIZNOSProps = new String[] {"IZNOS", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9640", "", "1200", "240", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(get-var \"H1\")", "", "", "", "", "", "", "", 
     "160", "", "540", "240", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "7200", "", "1200", "240", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(put-var \"H1\" (+ (get-var \"H1\") 1))", "", "", 
     "", "No", "", "", "", "2800", "40", "1620", "140", "", "", "", "", "", "", "", "", "", "", "", 
     "", ""};

  public raRadniNalogObradaDetail(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.DETAIL));
    this.setDefaults(thisProps);

    addElements();

    final raReportTemplate ownerFF = owner;

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis(ownerFF);
      }
    });
  }

  private void addElements() {
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextVC = addModel(ep.TEXT, TextVCProps);
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextIZNOS = addModel(ep.TEXT, TextIZNOSProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    Text2 = addModel(ep.TEXT, Text2Props);
  }

  private void modifyThis(raReportTemplate owner) {
    resizeElement(this.TextCART, hr.restart.robno.Aut.getAut().getIzlazCARTwidth(), this.TextNAZART);
    if ((owner instanceof hr.restart.robno.repObracunRadnogNalogaTemplate)||
        (owner instanceof hr.restart.robno.repStavkeRadnogNalogaTemplate)){
      deflateElement(TextVC); //,TextNAZART);
      deflateElement(TextIZNOS); //,TextNAZART);
    }
  }
}
