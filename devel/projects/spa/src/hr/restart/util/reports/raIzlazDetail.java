/****license*****************************************************************
**   file: raIzlazDetail.java
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
import hr.restart.util.Aus;

public class raIzlazDetail extends raReportSection {

  private String[] thisProps = new String[] {"", "", "", "", "", "", "", "280"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(if (= [ImaPor1] 1) (put-var \"tpor1\" 1) \"\")",
     "", "", "", "No", "", "", "", "2300", "", "0", "0", "", "", "", "", "", "", "", "", "", "", "",
     "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(if (= [ImaPor3] 1) (put-var \"tpor3\" 1) \"\")",
     "", "", "", "No", "", "", "", "3120", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "",
     "", "", "", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(if (= [ImaPor2] 1) (put-var \"tpor2\" 1) \"\")",
     "", "", "", "No", "", "", "", "2640", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "",
     "", "", "", ""};
  public raReportElement TextRBR;
  private String[] TextRBRProps = new String[] {"RBR", "", "", "", "", "", "", "", "", "", "440",
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  
  public raReportElement TextUPRAB1;
  private String[] TextUPRAB1Props = new String[] {"UPRAB1", "", "",
     "Number|false|1|3|2|2|true|3|false", "", "", "", "", "8200", "", "620", "220", "", "", "", "",
     "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  
  public raReportElement TextPor1p2p3Naz;
  private String[] TextPor1p2p3NazProps = new String[] {"Por1p2p3Naz", "", "",
      "Number|false|1|3|2|2|true|3|false", "", "", "", "", "8840", "", "560", "220", "", "", "", "",
      "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  
  public raReportElement TextJM;
  private String[] TextJMProps = new String[] {"JM", "", "", "", "", "", "", "", "6480", "", "460",
     "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "No"};
  public raReportElement TextFC;
  private String[] TextFCProps = new String[] {"FC", "", "", "Number|false|1|309|2|2|true|3|false",
     "", "", "", "", "6960", "", "1220", "220", "", "", "", "", "", "", "Lucida Bright", "8", "",
     "", "", "Right", "No"};
  public raReportElement TextKOL;
  private String[] TextKOLProps = new String[] {"KOL", "", "",
     "Number|false|1|309|3|3|true|3|false", "", "", "", "", "5500", "", "960", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
/*  public raReportElement Text4;
  private String[] Text4Props = new String[] {
     "=(put-var \"H2\" (+ (get-var \"H2\")\n(if (= [ImaPor2] 0) 0 [IPRODBP]))\n                           )\n)\n",
     "", "", "", "No", "", "", "", "1820", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "",
     "", "", "", ""};*/
  public raReportElement TextNAZART;
  private String[] TextNAZARTProps = new String[] {"NAZART", "", "", "", "", "", "Yes", "", "1300", "",
     "4180", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "Yes"};
  
  public raReportElement TextNAZARText;
  private String[] TextNAZARTextProps = new String[] {"NAZARText", "", "", "", "", "", "Yes", "Yes", "1300", "220",
     "8100", "0", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", "Yes"};
/*  public raReportElement Text5;
  private String[] Text5Props = new String[] {
     "=(put-var \"H3\" (+ (get-var \"H3\")\n(if (= [ImaPor3] 0) 0. [IPRODBP])) ))\n", "", "", "",
     "No", "", "", "", "1280", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "", "", "", "",
     ""};*/
  public raReportElement TextCART;
  private String[] TextCARTProps = new String[] {"CART", "", "", "", "", "", "", "", "480", "",
     "800", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", "No"};
  public raReportElement TextINETO;
  private String[] TextINETOProps = new String[] {"INETOP", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9420", "", "1400", "220", "", "", "",
     "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
/*  public raReportElement Text6;
  private String[] Text6Props = new String[] {
     "=(put-var \"H1\" (+ (get-var \"H1\")\n(if (= [ImaPor1] 0) 0. [IPRODBP]))\n                           )\n)\n",
     "", "", "", "No", "", "", "", "780", "", "0", "0", "", "", "", "", "", "", "Arial", "9", "",
     "", "", "", ""}; */

  public raIzlazDetail(raReportTemplate owner) {
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
    Text2 = addModel(ep.TEXT, Text2Props);
    Text3 = addModel(ep.TEXT, Text3Props);
    TextRBR = addModel(ep.TEXT, TextRBRProps);
    TextUPRAB1 = addModel(ep.TEXT, TextUPRAB1Props);
    TextJM = addModel(ep.TEXT, TextJMProps);
    TextFC = addModel(ep.TEXT, TextFCProps);
    TextKOL = addModel(ep.TEXT, TextKOLProps);
//    Text4 = addModel(ep.TEXT, Text4Props);
    TextNAZART = addModel(ep.TEXT, TextNAZARTProps);
//    Text5 = addModel(ep.TEXT, Text5Props);
    TextCART = addModel(ep.TEXT, TextCARTProps);
    TextINETO = addModel(ep.TEXT, TextINETOProps);
//    Text6 = addModel(ep.TEXT, Text6Props);
    TextPor1p2p3Naz = addModel(ep.TEXT, TextPor1p2p3NazProps);
    TextNAZARText = addModel(ep.TEXT, TextNAZARTextProps);
  }

  private void modifyThis() {
    int wid = Aut.getAut().getIzlazCARTwidth();
    boolean ean = frmParam.getParam("robno", "barcodeBc", "N",
        "Iscrtavanje šifre artikla kao barkod (D,N)").equalsIgnoreCase("D");
    if (ean) {
      TextCART.setControlSource("EANCODE");
      TextCART.setHeight(220);
      setProperty(raElixirProperties.SHRINK,raElixirPropertyValues.NO);
    }
    System.out.println(wid);
    resizeElement(this.TextCART, wid, this.TextNAZART);
    TextNAZARText.setLeft(TextNAZART.getLeft());
    boolean wide = frmParam.getParam("robno", "extNazartWide", "N", 
        "Široki dodatni opis artikla (D,N)?").equalsIgnoreCase("D");
    if (!wide) TextNAZARText.setWidth(TextNAZART.getWidth());
    else TextNAZARText.setWidth(TextINETO.getLeft() - TextNAZARText.getLeft() - 20);
    
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
