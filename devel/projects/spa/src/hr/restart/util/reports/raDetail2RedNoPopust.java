/****license*****************************************************************
**   file: raDetail2RedNoPopust.java
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

public class raDetail2RedNoPopust extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "560"};
  public raReportElement TextFVC;
  private String[] TextFVCProps = new String[] {"FVC", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6520", "", "", "220", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "", 
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "5520", "", "960", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextFMC;
  private String[] TextFMCProps = new String[] {"FMC", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9380", "", "", "220", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "500", "", 
     "1560", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextPor1p2p3Naz;
  private String[] TextPor1p2p3NazProps = new String[] {"Por1p2p3Naz", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7980", "", "1380", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "Yes", 
     "2080", "", "3440", "440", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextNAZARText;
  private String[] TextNAZARTextProps = new String[] {"NAZARText", "", "", "", "", "", "Yes", "Yes", 
     "2080", "440", "7280", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "480", 
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextUKPOR3;
  private String[] TextUKPOR3Props = new String[] {"UKPOR3", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7980", "220", "1380", "220", "", "", 
     "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextIPRODBP;
  private String[] TextIPRODBPProps = new String[] {"IPRODBP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6520", "220", "", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextIPRODSP;
  private String[] TextIPRODSPProps = new String[] {"IPRODSP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9380", "220", "", "220", "", "", "", 
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "5520", "220", 
     "960", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", "No"};

  public raDetail2RedNoPopust(raReportTemplate owner) {
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
    TextFVC = addModel(ep.TEXT, TextFVCProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextFMC = addModel(ep.TEXT, TextFMCProps);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextPor1p2p3Naz = addModel(ep.TEXT, TextPor1p2p3NazProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextUKPOR3 = addModel(ep.TEXT, TextUKPOR3Props);
    TextIPRODBP = addModel(ep.TEXT, TextIPRODBPProps);
    TextIPRODSP = addModel(ep.TEXT, TextIPRODSPProps);
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextNAZARText = addModel(ep.TEXT, TextNAZARTextProps);
  }

  private void modifyThis() {
    boolean wide = frmParam.getParam("robno", "extNazartWide", "N", 
        "Široki dodatni opis artikla (D,N)?").equalsIgnoreCase("D");
    if (!wide) TextNAZARText.setWidth(TextNAZART.getWidth());
  }
}
