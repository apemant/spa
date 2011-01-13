/****license*****************************************************************
**   file: raDetailValSingle.java
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

public class raDetailValSingle extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "300"};
  public raReportElement TextFC;
  private String[] TextFCProps = new String[] {"IPRODBPV", "", "", "Number|false|1|309|2|2|true|3|false", 
     "", "", "", "", "6960", "", "1320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", 
     "", "", "Right", "No"};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "440", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextPor1p2p3Naz;
  private String[] TextPor1p2p3NazProps = new String[] {"Por1p2p3Naz", "", "", 
     "Number|false|1|3|2|2|true|3|false", "", "", "", "", "8300", "", "1080", "220", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextIPRODBP;
  private String[] TextIPRODBPProps = new String[] {"IPRODBP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9400", "", "1420", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "", 
     "800", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "1300", 
     "", "5640", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextNAZARText;
  private String[] TextNAZARTextProps = new String[] {"NAZARText", "", "", "", "", "", "Yes", "", "1300", 
     "220", "8080", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};

  public raDetailValSingle(raReportTemplate owner) {
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
    TextFC = addModel(ep.TEXT, TextFCProps);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextPor1p2p3Naz = addModel(ep.TEXT, TextPor1p2p3NazProps);
    TextIPRODBP = addModel(ep.TEXT, TextIPRODBPProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextNAZARText = addModel(ep.TEXT, TextNAZARTextProps);
  }

  private void modifyThis() {
    boolean wide = frmParam.getParam("robno", "extNazartWide", "N", 
        "Široki dodatni opis artikla (D,N)?").equalsIgnoreCase("D");
    if (!wide) TextNAZARText.setWidth(TextNAZART.getWidth());
  }
}