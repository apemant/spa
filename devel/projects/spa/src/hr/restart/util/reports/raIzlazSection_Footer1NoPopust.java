/****license*****************************************************************
**   file: raIzlazSection_Footer1NoPopust.java
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

public class raIzlazSection_Footer1NoPopust extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "2260"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "100", "10820", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "9060", "160", "1760", "840", "Normal", 
     "Light Gray", "Solid", "Light Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelUkupno_bez_poreza;
  private String[] LabelUkupno_bez_porezaProps = new String[] {"Ukupno bez poreza", "", "6840", 
     "180", "2180", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement LabelREKAPITULACIJA_POREZA;
  private String[] LabelREKAPITULACIJA_POREZAProps = new String[] {"REKAPITULACIJA POREZA", "", 
     "640", "180", "5020", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", 
     "Center"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IPRODBP\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9100", "180", "1700", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", "No"};
  public raReportElement LabelUkupno_porez;
  private String[] LabelUkupno_porezProps = new String[] {"Ukupno porez", "", "6840", "460", "2180", 
     "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"%", "", "2340", "460", "720", "240", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Center"};
  public raReportElement LabelOsnovica;
  private String[] LabelOsnovicaProps = new String[] {"Osnovica", "", "3200", "460", "1300", "240", 
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement LabelPorez;
  private String[] LabelPorezProps = new String[] {"Porez", "", "4800", "460", "1300", "240", "", 
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement LabelGrupa;
  private String[] LabelGrupaProps = new String[] {"Grupa", "", "660", "460", "1200", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {
     "=(+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\"))", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9100", "460", "1700", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", "No"};
  public raReportElement TextPorezDepartmentIPRODBP;
  private String[] TextPorezDepartmentIPRODBPProps = new String[] {"PorezDepartmentIPRODBP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "3200", "700", "1300", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentPOR1;
  private String[] TextPorezDepartmentPOR1Props = new String[] {"PorezDepartmentPOR1", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "4800", "700", "1300", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentCPOR;
  private String[] TextPorezDepartmentCPORProps = new String[] {"PorezDepartmentCPOR", "", "", "", 
     "", "", "Yes", "", "660", "700", "1200", "220", "", "Light Gray", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentCrtica;
  private String[] TextPorezDepartmentCrticaProps = new String[] {"PorezDepartmentCrtica", "", "", 
     "", "", "", "Yes", "", "2000", "700", "180", "220", "", "Light Gray", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentUKUPOR;
  private String[] TextPorezDepartmentUKUPORProps = new String[] {"PorezDepartmentUKUPOR", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "2320", "700", "720", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelUkupno_s_porezom;
  private String[] LabelUkupno_s_porezomProps = new String[] {"Ukupno s porezom", "", "6840", "740", 
     "2180", "240", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {
     "=(+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\")))", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9100", "740", "1700", "240", "", "", 
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", "No"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "6880", "1020", "3940", "0", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "No", "420", "1180", "9540", "0", "", "", ""};
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "", "900", 
     "1240", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelSlovima_;
  private String[] LabelSlovima_Props = new String[] {"Slovima :", "", "", "1240", "840", "220", "", 
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};

  public raIzlazSection_Footer1NoPopust(raReportTemplate owner) {
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
    LabelUkupno_bez_poreza = addModel(ep.LABEL, LabelUkupno_bez_porezaProps);
    LabelREKAPITULACIJA_POREZA = addModel(ep.LABEL, LabelREKAPITULACIJA_POREZAProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    LabelUkupno_porez = addModel(ep.LABEL, LabelUkupno_porezProps);
    Label2 = addModel(ep.LABEL, Label2Props);
    LabelOsnovica = addModel(ep.LABEL, LabelOsnovicaProps);
    LabelPorez = addModel(ep.LABEL, LabelPorezProps);
    LabelGrupa = addModel(ep.LABEL, LabelGrupaProps);
    Text2 = addModel(ep.TEXT, Text2Props);
    TextPorezDepartmentIPRODBP = addModel(ep.TEXT, TextPorezDepartmentIPRODBPProps);
    TextPorezDepartmentPOR1 = addModel(ep.TEXT, TextPorezDepartmentPOR1Props);
    TextPorezDepartmentCPOR = addModel(ep.TEXT, TextPorezDepartmentCPORProps);
    TextPorezDepartmentCrtica = addModel(ep.TEXT, TextPorezDepartmentCrticaProps);
    TextPorezDepartmentUKUPOR = addModel(ep.TEXT, TextPorezDepartmentUKUPORProps);
    LabelUkupno_s_porezom = addModel(ep.LABEL, LabelUkupno_s_porezomProps);
    Text3 = addModel(ep.TEXT, Text3Props);
    Line2 = addModel(ep.LINE, Line2Props);
    Line3 = addModel(ep.LINE, Line3Props);
    TextSLOVIMA = addModel(ep.TEXT, TextSLOVIMAProps);
    LabelSlovima_ = addModel(ep.LABEL, LabelSlovima_Props);
  }

  private void modifyThis() {
  }
}
