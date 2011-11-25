/****license*****************************************************************
**   file: raIzlazSectionFooterLines.java
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

public class raIzlazSectionFooterLines extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "3420"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "100", "10810", "0", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "6860", "1570", "3950", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "9040", "160", "1780", "1380", "Normal",
     "Light Gray", "Solid", "Light Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelUkupno;
  private String[] LabelUkupnoProps = new String[] {"Ukupno", "", "6860", "180", "2180", "240", "",
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"INETO\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "180", "1700", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement LabelREKAPITULACIJA_POREZA;
  private String[] LabelREKAPITULACIJA_POREZAProps = new String[] {"REKAPITULACIJA POREZA", "",
     "640", "180", "5020", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "",
     "Center"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"UIRAB\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "460", "1700", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement LabelGrupa;
  private String[] LabelGrupaProps = new String[] {"Grupa", "", "660", "460", "1200", "240", "", "",
     "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelPopust;
  private String[] LabelPopustProps = new String[] {"Popust", "", "6860", "460", "2180", "240", "",
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement LabelPorez;
  private String[] LabelPorezProps = new String[] {"Porez", "", "4800", "460", "1300", "240", "",
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement LabelOsnovica;
  private String[] LabelOsnovicaProps = new String[] {"Osnovica", "", "3200", "460", "1300", "240",
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"%", "", "2340", "460", "720", "240", "", "", "", "",
     "", "", "Lucida Bright", "8", "", "", "", "Center"};
  public raReportElement TextPorezDepartmentUKUPOR;
  private String[] TextPorezDepartmentUKUPORProps = new String[] {"PorezDepartmentUKUPOR", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "2320", "700", "720", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentCrtica;
  private String[] TextPorezDepartmentCrticaProps = new String[] {"PorezDepartmentCrtica", "", "",
     "", "", "", "Yes", "", "2000", "700", "180", "220", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentCPOR;
  private String[] TextPorezDepartmentCPORProps = new String[] {"PorezDepartmentCPOR", "", "", "",
     "", "", "Yes", "", "660", "700", "1200", "220", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentPOR1;
  private String[] TextPorezDepartmentPOR1Props = new String[] {"PorezDepartmentPOR1", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "4800", "700", "1300", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentIPRODBP;
  private String[] TextPorezDepartmentIPRODBPProps = new String[] {"PorezDepartmentIPRODBP", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "3200", "700", "1300", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"IPRODBP\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "740", "1700", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", (getParametarBoldIBP() ? raElixirPropertyValues.BOLD : ""), "", "", "Right", ""};
  public raReportElement LabelUkupno_bez_poreza;
  private String[] LabelUkupno_bez_porezaProps = new String[] {"Ukupno bez poreza", "", "6860",
     "740", "2180", "240", "", "", "", "", "", "", "Lucida Bright", "9",(getParametarBoldIBP() ? raElixirPropertyValues.BOLD : ""), "", "", "Right"};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {
     "=(+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\"))", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "1020", "1700", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement LabelUkupno_porez;
  private String[] LabelUkupno_porezProps = new String[] {"Ukupno porez", "", "6860", "1020",
     "2180", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {
     "=(+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\")))", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "1300", "1700", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement LabelUkupno_s_porezom;
  private String[] LabelUkupno_s_porezomProps = new String[] {"Ukupno s porezom", "", "6860",
     "1300", "2180", "240", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "No", "420", "1740", "9540", "0", "", "", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "No", "660", "960", "5440", "0", "", "", ""};
  
  public raReportElement Text6;
  private String[] Text6Props = new String[] {
     "UIUlabel", "", "", "", "", "", "", "", "6860",
     "1660", "2180", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement Text7;
  private String[] Text7Props = new String[] {
     "UIU", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "1660", "1700", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement Text8;
  private String[] Text8Props = new String[] {
     "UIUlabel2", "", "",
     "", "", "", "", "", "6860", "1940", "2180", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement Text9;
  private String[] Text9Props = new String[] {
     "=(+ (- (dsum \"IPRODBP\") [UIU]) (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\")))", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9120", "1940", "1700", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  
  
  public raReportElement TextPOPNASLOV;
  private String[] TextPOPNASLOVProps = new String[] {"POPNASLOV", "", "", "",
     "", "", "Yes", "", "640", "1260", "5020", "240", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "9", "", "", "", "Center", ""};
  
  public raReportElement TextPOPPOST;
  private String[] TextPOPPOSTProps = new String[] {"POPPOST", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "2320", "1540", "720", "20", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPOPCRTICA;
  private String[] TextPOPCRTICAProps = new String[] {"POPCRTICA", "", "",
     "", "", "", "Yes", "", "2000", "1540", "180", "20", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPOPNAZ;
  private String[] TextPOPNAZProps = new String[] {"POPNAZ", "", "", "",
     "", "", "Yes", "", "660", "1540", "1200", "20", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPOPIZNOS;
  private String[] TextPOPIZNOSProps = new String[] {"POPIZNOS", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "4800", "1540", "1300", "20", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPOPOSN;
  private String[] TextPOPOSNProps = new String[] {"POPOSN", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "3200", "1540", "1300", "20", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  
  public raReportElement Line5;
  private String[] Line5Props = new String[] {"", "No", "660", "1600", "5440", "0", "", "", ""};
  
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "", "900",
     "1800", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelSlovima_;
  private String[] LabelSlovima_Props = new String[] {"Slovima :", "", "", "1800", "840", "220", "",
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};  

  public raIzlazSectionFooterLines(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_FOOTER + 1));
    this.setDefaults(thisProps);

    addElements();

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis();
      }
    });
  }

  private void addElements() {
    Line1 = addModel(ep.LINE, Line1Props);
    Label1 = addModel(ep.LABEL, Label1Props);
    LabelUkupno = addModel(ep.LABEL, LabelUkupnoProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    LabelREKAPITULACIJA_POREZA = addModel(ep.LABEL, LabelREKAPITULACIJA_POREZAProps);
    Text2 = addModel(ep.TEXT, Text2Props);
    LabelGrupa = addModel(ep.LABEL, LabelGrupaProps);
    LabelPopust = addModel(ep.LABEL, LabelPopustProps);
    LabelPorez = addModel(ep.LABEL, LabelPorezProps);
    LabelOsnovica = addModel(ep.LABEL, LabelOsnovicaProps);
    Label2 = addModel(ep.LABEL, Label2Props);
    TextPorezDepartmentUKUPOR = addModel(ep.TEXT, TextPorezDepartmentUKUPORProps);
    TextPorezDepartmentCrtica = addModel(ep.TEXT, TextPorezDepartmentCrticaProps);
    TextPorezDepartmentCPOR = addModel(ep.TEXT, TextPorezDepartmentCPORProps);
    TextPorezDepartmentPOR1 = addModel(ep.TEXT, TextPorezDepartmentPOR1Props);
    TextPorezDepartmentIPRODBP = addModel(ep.TEXT, TextPorezDepartmentIPRODBPProps);
    Text3 = addModel(ep.TEXT, Text3Props);
    LabelUkupno_bez_poreza = addModel(ep.LABEL, LabelUkupno_bez_porezaProps);
    Text4 = addModel(ep.TEXT, Text4Props);
    LabelUkupno_porez = addModel(ep.LABEL, LabelUkupno_porezProps);
    Text5 = addModel(ep.TEXT, Text5Props);
    Text6 = addModel(ep.TEXT, Text6Props);
    Text7 = addModel(ep.TEXT, Text7Props);
    Text8 = addModel(ep.TEXT, Text8Props);
    Text9 = addModel(ep.TEXT, Text9Props);
    LabelUkupno_s_porezom = addModel(ep.LABEL, LabelUkupno_s_porezomProps);
    Line2 = addModel(ep.LINE, Line2Props);
    Line3 = addModel(ep.LINE, Line3Props);
    Line4 = addModel(ep.LINE, Line4Props);
    TextPOPNASLOV = addModel(ep.TEXT, TextPOPNASLOVProps);
    TextPOPPOST = addModel(ep.TEXT, TextPOPPOSTProps);
    TextPOPCRTICA = addModel(ep.TEXT, TextPOPCRTICAProps);
    TextPOPNAZ = addModel(ep.TEXT, TextPOPNAZProps);
    TextPOPIZNOS = addModel(ep.TEXT, TextPOPIZNOSProps);
    TextPOPOSN = addModel(ep.TEXT, TextPOPOSNProps);
    Line5 = addModel(ep.LINE, Line5Props);
    TextSLOVIMA = addModel(ep.TEXT, TextSLOVIMAProps);
    LabelSlovima_ = addModel(ep.LABEL, LabelSlovima_Props);
  }
  
  protected boolean getParametarBoldIBP(){
    return frmParam.getFrmParam().getParam("robno","boldIBP","N","Bold iznos bez poreza N - ne, D - da").equalsIgnoreCase("D");
  }

  private void modifyThis() {
  }
}
