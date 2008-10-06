/****license*****************************************************************
**   file: raGRSectionFooterLines.java
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

public class raGRSectionFooterLines extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "4220"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "100", "10820", "0", "", "", ""};
  public raReportElement LabelUkupno;
  private String[] LabelUkupnoProps = new String[] {"Ukupno", "", "7100", "180", "2180", "240", "", 
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "9340", "180", "1480", "1360", "Normal", 
     "Light Gray", "Solid", "Light Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelREKAPITULACIJA_POREZA;
  private String[] LabelREKAPITULACIJA_POREZAProps = new String[] {"REKAPITULACIJA POREZA", "", 
     "220", "180", "5020", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", 
     "Center"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"INETO\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9340", "180", "1480", "240", "", 
     "Gray", "", "Gray", "", "", "Lucida Bright", "9", "", "", "", "Right", "No"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"%", "", "1920", "460", "720", "240", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Center"};
  public raReportElement LabelOsnovica;
  private String[] LabelOsnovicaProps = new String[] {"Osnovica", "", "2780", "460", "1300", "240", 
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"UIRAB\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9340", "460", "1480", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", "No"};
  public raReportElement LabelPorez;
  private String[] LabelPorezProps = new String[] {"Porez", "", "4380", "460", "1300", "240", "", 
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement LabelGrupa;
  private String[] LabelGrupaProps = new String[] {"Grupa", "", "240", "460", "1200", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelPopust;
  private String[] LabelPopustProps = new String[] {"Popust", "", "7100", "460", "2180", "240", "", 
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement TextPorezDepartmentIPRODBP;
  private String[] TextPorezDepartmentIPRODBPProps = new String[] {"PorezDepartmentIPRODBP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "2780", "700", "1300", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentPOR1;
  private String[] TextPorezDepartmentPOR1Props = new String[] {"PorezDepartmentPOR1", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "4380", "700", "1300", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentCPOR;
  private String[] TextPorezDepartmentCPORProps = new String[] {"PorezDepartmentCPOR", "", "", "", 
     "", "", "Yes", "", "240", "700", "1200", "220", "", "Light Gray", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentCrtica;
  private String[] TextPorezDepartmentCrticaProps = new String[] {"PorezDepartmentCrtica", "", "", 
     "", "", "", "Yes", "", "1580", "700", "180", "220", "", "Light Gray", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentUKUPOR;
  private String[] TextPorezDepartmentUKUPORProps = new String[] {"PorezDepartmentUKUPOR", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "1900", "700", "720", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"IPRODBP\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9340", "740", "1480", "240", "", 
     "Gray", "", "Gray", "", "", "Lucida Bright", "9", "", "", "", "Right", "No"};
  public raReportElement LabelUkupno_bez_poreza;
  private String[] LabelUkupno_bez_porezaProps = new String[] {"Ukupno bez poreza", "", "7100", 
     "740", "2180", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "No", "100", "980", "6540", "0", "", "", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {
     "=(+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\"))", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9340", "1020", "1480", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", "No"};
  public raReportElement LabelREKAPITULACIJA_PLACANJA;
  private String[] LabelREKAPITULACIJA_PLACANJAProps = new String[] {"REKAPITULACIJA PLA\u0106ANJA", 
     "", "220", "1020", "6700", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", 
     "Center"};
  public raReportElement LabelUkupno_porez;
  private String[] LabelUkupno_porezProps = new String[] {"Ukupno porez", "", "7100", "1020", 
     "2180", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement TextDATUMNAPLATE;
  private String[] TextDATUMNAPLATEProps = new String[] {"DATUMNAPLATE", "", "", "", "", "", "Yes", 
     "Yes", "4700", "1300", "1120", "220", "", "Light Gray", "", "", "", "", "Lucida Bright", "8", 
     "", "", "", "", ""};
  public raReportElement TextIZNOSRATE;
  private String[] TextIZNOSRATEProps = new String[] {"IZNOSRATE", "", "", "", "", "", "Yes", "Yes", 
     "5840", "1300", "1080", "220", "", "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", 
     "", "Right", ""};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {
     "=(+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\")))", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9340", "1300", "1480", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", "No"};
  public raReportElement TextNACINPLACANJA;
  private String[] TextNACINPLACANJAProps = new String[] {"NACINPLACANJA", "", "", "", "", "", 
     "Yes", "Yes", "220", "1300", "4460", "220", "", "Light Gray", "", "", "", "", "Lucida Bright", 
     "8", "", "", "", "", ""};
  public raReportElement LabelUkupno_s_porezom;
  private String[] LabelUkupno_s_porezomProps = new String[] {"Ukupno s porezom", "", "7100", 
     "1300", "2180", "240", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "7120", "1580", "3700", "0", "", "", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "No", "140", "1900", "10560", "0", "", "", ""};
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "", "860", 
     "1960", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelSlovima_;
  private String[] LabelSlovima_Props = new String[] {"Slovima :", "", "", "1960", "840", "220", "", 
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};

  public raGRSectionFooterLines(raReportTemplate owner) {
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
    LabelUkupno = addModel(ep.LABEL, LabelUkupnoProps);
    Label1 = addModel(ep.LABEL, Label1Props);
    LabelREKAPITULACIJA_POREZA = addModel(ep.LABEL, LabelREKAPITULACIJA_POREZAProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Label2 = addModel(ep.LABEL, Label2Props);
    LabelOsnovica = addModel(ep.LABEL, LabelOsnovicaProps);
    Text2 = addModel(ep.TEXT, Text2Props);
    LabelPorez = addModel(ep.LABEL, LabelPorezProps);
    LabelGrupa = addModel(ep.LABEL, LabelGrupaProps);
    LabelPopust = addModel(ep.LABEL, LabelPopustProps);
    TextPorezDepartmentIPRODBP = addModel(ep.TEXT, TextPorezDepartmentIPRODBPProps);
    TextPorezDepartmentPOR1 = addModel(ep.TEXT, TextPorezDepartmentPOR1Props);
    TextPorezDepartmentCPOR = addModel(ep.TEXT, TextPorezDepartmentCPORProps);
    TextPorezDepartmentCrtica = addModel(ep.TEXT, TextPorezDepartmentCrticaProps);
    TextPorezDepartmentUKUPOR = addModel(ep.TEXT, TextPorezDepartmentUKUPORProps);
    Text3 = addModel(ep.TEXT, Text3Props);
    LabelUkupno_bez_poreza = addModel(ep.LABEL, LabelUkupno_bez_porezaProps);
    Line2 = addModel(ep.LINE, Line2Props);
    Text4 = addModel(ep.TEXT, Text4Props);
    LabelREKAPITULACIJA_PLACANJA = addModel(ep.LABEL, LabelREKAPITULACIJA_PLACANJAProps);
    LabelUkupno_porez = addModel(ep.LABEL, LabelUkupno_porezProps);
    TextDATUMNAPLATE = addModel(ep.TEXT, TextDATUMNAPLATEProps);
    TextIZNOSRATE = addModel(ep.TEXT, TextIZNOSRATEProps);
    Text5 = addModel(ep.TEXT, Text5Props);
    TextNACINPLACANJA = addModel(ep.TEXT, TextNACINPLACANJAProps);
    LabelUkupno_s_porezom = addModel(ep.LABEL, LabelUkupno_s_porezomProps);
    Line3 = addModel(ep.LINE, Line3Props);
    Line4 = addModel(ep.LINE, Line4Props);
    TextSLOVIMA = addModel(ep.TEXT, TextSLOVIMAProps);
    LabelSlovima_ = addModel(ep.LABEL, LabelSlovima_Props);    
  }

  private void modifyThis() {
  }
}
