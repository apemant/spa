/****license*****************************************************************
**   file: raIzlazDetail2.java
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

public class raIzlazDetail2 extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "600"};
  public raReportElement TextARTIKL;
  private String[] TextARTIKLProps = new String[] {"ARTIKL", "", "", "", "", "", "", "", "500", "",
     "2320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "",
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "2840", "", "960", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextPor1p2p3Naz;
  private String[] TextPor1p2p3NazProps = new String[] {"Por1p2p3Naz", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7980", "", "1380", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextFMC;
  private String[] TextFMCProps = new String[] {"FMC", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9380", "", "", "220", "", "", "", "",
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextFVC;
  private String[] TextFVCProps = new String[] {"FVC", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6520", "", "", "220", "", "", "", "",
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "480",
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextUPRAB1;
  private String[] TextUPRAB1Props = new String[] {"UPRAB1", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5240", "", "1260", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextFC;
  private String[] TextFCProps = new String[] {"FC", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "3820", "", "1400",
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "2840", "220",
     "960", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Center", "No"};
  public raReportElement TextINETO;
  private String[] TextINETOProps = new String[] {"INETO", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "3820", "220", "1400", "220", "", "",
     "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "500",
     "220", "2320", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  
  public raReportElement TextNAZARText;
  private String[] TextNAZARTextProps = new String[] {"NAZARText", "", "", "", "", "", "Yes", "Yes", "500",
     "440", "8860", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  
  public raReportElement TextUKPOR3;
  private String[] TextUKPOR3Props = new String[] {"UKPOR3", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7980", "220", "1380", "220", "", "",
     "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextIPRODSP;
  private String[] TextIPRODSPProps = new String[] {"IPRODSP", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9380", "220", "", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextIPRODBP;
  private String[] TextIPRODBPProps = new String[] {"IPRODBP", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6520", "220", "", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextUIRAB;
  private String[] TextUIRABProps = new String[] {"UIRAB", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5240", "220", "1260", "220", "", "",
     "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};

  public raIzlazDetail2(raReportTemplate owner) {
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
    TextARTIKL = addModel(ep.TEXT, TextARTIKLProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
    TextPor1p2p3Naz = addModel(ep.TEXT, TextPor1p2p3NazProps);
    TextFMC = addModel(ep.TEXT, TextFMCProps);
    TextFVC = addModel(ep.TEXT, TextFVCProps);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextUPRAB1 = addModel(ep.TEXT, TextUPRAB1Props);
    TextFC = addModel(ep.TEXT, TextFCProps);
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextINETO = addModel(ep.TEXT, TextINETOProps);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
    TextUKPOR3 = addModel(ep.TEXT, TextUKPOR3Props);
    TextIPRODSP = addModel(ep.TEXT, TextIPRODSPProps);
    TextIPRODBP = addModel(ep.TEXT, TextIPRODBPProps);
    TextUIRAB = addModel(ep.TEXT, TextUIRABProps);
    TextNAZARText = addModel(ep.TEXT, TextNAZARTextProps);
  }

  private void modifyThis() {
    boolean wide = frmParam.getParam("robno", "extNazartWide", "N", 
          "Široki dodatni opis artikla (D,N)?").equalsIgnoreCase("D");
    if (!wide) TextNAZARText.setWidth(TextNAZART.getWidth());
    
    if (frmParam.getParam("robno", "detailBreak", "N",
      "Dopustiti prelamanje detail-a na izlaznim dokumentima (D,N)?").equalsIgnoreCase("D"))
      this.setProperty(ep.KEEP_TOGETHER, ev.NO);
    
    int decs = Aus.getNumber(frmParam.getParam("robno", "cijenaDec", 
        "2", "Broj decimala za cijenu na izlazu (2-4)").trim());
    if (decs < 2) decs = 2;
    if (decs > 4) decs = 4;
    TextFC.setProperty(ep.FORMAT, "Number|false|1|309|"+decs+"|"+decs+"|true|3|false");
    
    int decskol = Aus.getNumber(frmParam.getParam("robno", "kolDec", 
        "3", "Broj decimala za kolicinu na izlazu (2-4)").trim());
    if (decskol < 2) decskol = 2;
    if (decskol > 4) decskol = 4;
    TextKOL.setProperty(ep.FORMAT, "Number|false|1|309|"+decskol+"|"+decskol+"|true|3|false");
  }
}
