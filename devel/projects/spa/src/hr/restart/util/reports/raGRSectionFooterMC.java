/****license*****************************************************************
**   file: raGRSectionFooterMC.java
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

public class raGRSectionFooterMC extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "After", "", "", "Yes", "", "Yes", "4400"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "0", "10800", "0", "", "Light Gray", ""};
  public raReportElement LabelREKAPITULACIJA_POREZA;
  private String[] LabelREKAPITULACIJA_POREZAProps = new String[] {"REKAPITULACIJA POREZA", "",
     "220", "100", "5020", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "",
     "Center"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "9340", "100", "1500", "820", "Normal",
     "Light Gray", "Solid", "Light Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelUkupno;
  private String[] LabelUkupnoProps = new String[] {"Ukupno", "", "7100", "100", "2180", "240", "",
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IPRODSP\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9340", "100", "1480", "240", "",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement LabelPopust;
  private String[] LabelPopustProps = new String[] {"Popust", "", "7100", "380", "2180", "240", "",
     "", "", "", "", "", "Lucida Bright", "9", "", "", "", "Right"};
  public raReportElement LabelGrupa;
  private String[] LabelGrupaProps = new String[] {"Grupa", "", "240", "380", "1200", "240", "", "",
     "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelPorez;
  private String[] LabelPorezProps = new String[] {"Porez", "", "4380", "380", "1300", "240", "",
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement LabelOsnovica;
  private String[] LabelOsnovicaProps = new String[] {"Osnovica", "", "2780", "380", "1300", "240",
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"%", "", "1920", "380", "720", "240", "", "", "", "",
     "", "", "Lucida Bright", "8", "", "", "", "Center"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"UIRAB\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9340", "380", "1480", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentUKUPOR;
  private String[] TextPorezDepartmentUKUPORProps = new String[] {"PorezDepartmentUKUPOR", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "1900", "620", "720", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentCrtica;
  private String[] TextPorezDepartmentCrticaProps = new String[] {"PorezDepartmentCrtica", "", "",
     "", "", "", "Yes", "", "1580", "620", "180", "220", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentCPOR;
  private String[] TextPorezDepartmentCPORProps = new String[] {"PorezDepartmentCPOR", "", "", "",
     "", "", "Yes", "", "240", "620", "1200", "220", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentPOR1;
  private String[] TextPorezDepartmentPOR1Props = new String[] {"PorezDepartmentPOR1", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "4380", "620", "1300", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentIPRODBP;
  private String[] TextPorezDepartmentIPRODBPProps = new String[] {"PorezDepartmentIPRODBP", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "2780", "620", "1300", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {
     "=(+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\")))", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9340", "660", "1480", "240", "", "",
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", ""};
  public raReportElement LabelSveukupno;
  private String[] LabelSveukupnoProps = new String[] {"Sveukupno", "", "7100", "660", "2180",
     "240", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "No", "100", "900", "6540", "0", "", "", ""};
  public raReportElement LabelREKAPITULACIJA_PLACANJA;
  private String[] LabelREKAPITULACIJA_PLACANJAProps = new String[] {"REKAPITULACIJA PLA\u0106ANJA",
     "", "220", "940", "6700", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "",
     "Center"};
  public raReportElement TextDATUMNAPLATE;
  private String[] TextDATUMNAPLATEProps = new String[] {"DATUMNAPLATE", "", "", "", "", "", "Yes",
     "Yes", "4700", "1220", "1120", "220", "", "Light Gray", "", "", "", "", "Lucida Bright", "8",
     "", "", "", "", ""};
  public raReportElement TextIZNOSRATE;
  private String[] TextIZNOSRATEProps = new String[] {"IZNOSRATE", "", "", "", "", "", "Yes", "Yes",
     "5840", "1220", "1080", "220", "", "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "",
     "", "Right", ""};
  public raReportElement TextNACINPLACANJA;
  private String[] TextNACINPLACANJAProps = new String[] {"NACINPLACANJA", "", "", "", "", "",
     "Yes", "Yes", "220", "1220", "4460", "220", "", "Light Gray", "", "", "", "", "Lucida Bright",
     "8", "", "", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "No", "140", "1820", "10560", "0", "", "", ""};
  public raReportElement LabelSlovima_;
  private String[] LabelSlovima_Props = new String[] {"Slovima :", "", "", "1880", "840", "220", "",
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "", "860",
     "1880", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextNAPOMENAOPIS;
  private String[] TextNAPOMENAOPISProps = new String[] {"NAPOMENAOPIS", "", "", "", "", "", "Yes",
     "Yes", "", "2280", "10440", "640", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "", ""};

  public raGRSectionFooterMC(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_FOOTER + 0));
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
    LabelREKAPITULACIJA_POREZA = addModel(ep.LABEL, LabelREKAPITULACIJA_POREZAProps);
    Label1 = addModel(ep.LABEL, Label1Props);
    LabelUkupno = addModel(ep.LABEL, LabelUkupnoProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    LabelPopust = addModel(ep.LABEL, LabelPopustProps);
    LabelGrupa = addModel(ep.LABEL, LabelGrupaProps);
    LabelPorez = addModel(ep.LABEL, LabelPorezProps);
    LabelOsnovica = addModel(ep.LABEL, LabelOsnovicaProps);
    Label2 = addModel(ep.LABEL, Label2Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    TextPorezDepartmentUKUPOR = addModel(ep.TEXT, TextPorezDepartmentUKUPORProps);
    TextPorezDepartmentCrtica = addModel(ep.TEXT, TextPorezDepartmentCrticaProps);
    TextPorezDepartmentCPOR = addModel(ep.TEXT, TextPorezDepartmentCPORProps);
    TextPorezDepartmentPOR1 = addModel(ep.TEXT, TextPorezDepartmentPOR1Props);
    TextPorezDepartmentIPRODBP = addModel(ep.TEXT, TextPorezDepartmentIPRODBPProps);
    Text3 = addModel(ep.TEXT, Text3Props);
    LabelSveukupno = addModel(ep.LABEL, LabelSveukupnoProps);
    Line2 = addModel(ep.LINE, Line2Props);
    LabelREKAPITULACIJA_PLACANJA = addModel(ep.LABEL, LabelREKAPITULACIJA_PLACANJAProps);
    TextDATUMNAPLATE = addModel(ep.TEXT, TextDATUMNAPLATEProps);
    TextIZNOSRATE = addModel(ep.TEXT, TextIZNOSRATEProps);
    TextNACINPLACANJA = addModel(ep.TEXT, TextNACINPLACANJAProps);
    Line3 = addModel(ep.LINE, Line3Props);
    LabelSlovima_ = addModel(ep.LABEL, LabelSlovima_Props);
    TextSLOVIMA = addModel(ep.TEXT, TextSLOVIMAProps);
    TextNAPOMENAOPIS = addModel(ep.TEXT, TextNAPOMENAOPISProps);
  }

  private void modifyThis() {
  }
}
