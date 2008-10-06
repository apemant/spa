/****license*****************************************************************
**   file: raGRSectionFooter2Lines.java
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

public class raGRSectionFooter2Lines extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "4560"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "100", "10800", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "140", "3800", "240", "Normal", "Gray", 
     "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"UKPOR3\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7980", "140", "1380", "240", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"IPRODSP\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9380", "140", "", "240", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"IPRODBP\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6520", "140", "", "240", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(dsum \"UIRAB\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5240", "140", "1260", "240", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(dsum \"INETO\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "3820", "140", "1400", "240", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", "No"};
  public raReportElement LabelU_K_U_P_N_O;
  private String[] LabelU_K_U_P_N_OProps = new String[] {"U K U P N O", "", "80", "140", "1480", 
     "240", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "400", "10800", "0", "", "", ""};
  public raReportElement LabelREKAPITULACIJA_POREZA;
  private String[] LabelREKAPITULACIJA_POREZAProps = new String[] {"REKAPITULACIJA POREZA", "", 
     "220", "480", "5020", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", 
     "Center"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"%", "", "1920", "760", "720", "240", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Center"};
  public raReportElement LabelOsnovica;
  private String[] LabelOsnovicaProps = new String[] {"Osnovica", "", "2780", "760", "1300", "240", 
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement LabelPorez;
  private String[] LabelPorezProps = new String[] {"Porez", "", "4380", "760", "1300", "240", "", 
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement LabelGrupa;
  private String[] LabelGrupaProps = new String[] {"Grupa", "", "240", "760", "1200", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextPorezDepartmentIPRODBP;
  private String[] TextPorezDepartmentIPRODBPProps = new String[] {"PorezDepartmentIPRODBP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "2780", "1000", "1300", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentPOR1;
  private String[] TextPorezDepartmentPOR1Props = new String[] {"PorezDepartmentPOR1", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "4380", "1000", "1300", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentCPOR;
  private String[] TextPorezDepartmentCPORProps = new String[] {"PorezDepartmentCPOR", "", "", "", 
     "", "", "Yes", "", "240", "1000", "1200", "220", "", "Light Gray", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentCrtica;
  private String[] TextPorezDepartmentCrticaProps = new String[] {"PorezDepartmentCrtica", "", "", 
     "", "", "", "Yes", "", "1580", "1000", "180", "220", "", "Light Gray", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentUKUPOR;
  private String[] TextPorezDepartmentUKUPORProps = new String[] {"PorezDepartmentUKUPOR", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "1900", "1000", "720", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "No", "100", "1280", "6540", "0", "", "", ""};
  public raReportElement LabelREKAPITULACIJA_PLACANJA;
  private String[] LabelREKAPITULACIJA_PLACANJAProps = new String[] {"REKAPITULACIJA PLA\u0106ANJA", 
     "", "220", "1320", "6700", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", 
     "Center"};
  public raReportElement TextNACINPLACANJA;
  private String[] TextNACINPLACANJAProps = new String[] {"NACINPLACANJA", "", "", "", "", "", 
     "Yes", "Yes", "220", "1600", "4460", "220", "", "Light Gray", "", "", "", "", "Lucida Bright", 
     "8", "", "", "", "", ""};
  public raReportElement TextIZNOSRATE;
  private String[] TextIZNOSRATEProps = new String[] {"IZNOSRATE", "", "", "", "", "", "Yes", "Yes", 
     "5840", "1600", "1080", "220", "", "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", 
     "", "Right", ""};
  public raReportElement TextDATUMNAPLATE;
  private String[] TextDATUMNAPLATEProps = new String[] {"DATUMNAPLATE", "", "", "", "", "", "Yes", 
     "Yes", "4700", "1600", "1120", "220", "", "Light Gray", "", "", "", "", "Lucida Bright", "8", 
     "", "", "", "", ""};
  public raReportElement Line4;
  private String[] Line4Props = new String[] {"", "No", "140", "2200", "10560", "0", "", "", ""};
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "", "860", 
     "2260", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelSlovima_;
  private String[] LabelSlovima_Props = new String[] {"Slovima :", "", "", "2260", "840", "220", "", 
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};

  public raGRSectionFooter2Lines(raReportTemplate owner) {
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
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    Text3 = addModel(ep.TEXT, Text3Props);
    Text4 = addModel(ep.TEXT, Text4Props);
    Text5 = addModel(ep.TEXT, Text5Props);
    LabelU_K_U_P_N_O = addModel(ep.LABEL, LabelU_K_U_P_N_OProps);
    Line2 = addModel(ep.LINE, Line2Props);
    LabelREKAPITULACIJA_POREZA = addModel(ep.LABEL, LabelREKAPITULACIJA_POREZAProps);
    Label2 = addModel(ep.LABEL, Label2Props);
    LabelOsnovica = addModel(ep.LABEL, LabelOsnovicaProps);
    LabelPorez = addModel(ep.LABEL, LabelPorezProps);
    LabelGrupa = addModel(ep.LABEL, LabelGrupaProps);
    TextPorezDepartmentIPRODBP = addModel(ep.TEXT, TextPorezDepartmentIPRODBPProps);
    TextPorezDepartmentPOR1 = addModel(ep.TEXT, TextPorezDepartmentPOR1Props);
    TextPorezDepartmentCPOR = addModel(ep.TEXT, TextPorezDepartmentCPORProps);
    TextPorezDepartmentCrtica = addModel(ep.TEXT, TextPorezDepartmentCrticaProps);
    TextPorezDepartmentUKUPOR = addModel(ep.TEXT, TextPorezDepartmentUKUPORProps);
    Line3 = addModel(ep.LINE, Line3Props);
    LabelREKAPITULACIJA_PLACANJA = addModel(ep.LABEL, LabelREKAPITULACIJA_PLACANJAProps);
    TextNACINPLACANJA = addModel(ep.TEXT, TextNACINPLACANJAProps);
    TextIZNOSRATE = addModel(ep.TEXT, TextIZNOSRATEProps);
    TextDATUMNAPLATE = addModel(ep.TEXT, TextDATUMNAPLATEProps);
    Line4 = addModel(ep.LINE, Line4Props);
    TextSLOVIMA = addModel(ep.TEXT, TextSLOVIMAProps);
    LabelSlovima_ = addModel(ep.LABEL, LabelSlovima_Props);
  }

  private void modifyThis() {
  }
}
