/****license*****************************************************************
**   file: raIzlazSectionFooter2Lines.java
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

public class raIzlazSectionFooter2Lines extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "4280"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "100", "10800", "0", "", "", ""};
  public raReportElement LabelU_K_U_P_N_O;
  private String[] LabelU_K_U_P_N_OProps = new String[] {"U K U P N O", "", "80", "150", "1480",
     "280", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
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
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "140", "3800", "240", "Normal", "Gray",
     "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "400", "10800", "0", "", "", ""};
  public raReportElement LabelREKAPITULACIJA_POREZA;
  private String[] LabelREKAPITULACIJA_POREZAProps = new String[] {"REKAPITULACIJA POREZA", "",
     "440", "580", "5020", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "",
     "Center"};
  public raReportElement LabelOsnovica;
  private String[] LabelOsnovicaProps = new String[] {"Osnovica", "", "3000", "860", "1300", "240",
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"%", "", "2140", "860", "720", "240", "", "", "", "",
     "", "", "Lucida Bright", "8", "", "", "", "Center"};
  public raReportElement LabelPorez;
  private String[] LabelPorezProps = new String[] {"Porez", "", "4600", "860", "1300", "240", "",
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement LabelGrupa;
  private String[] LabelGrupaProps = new String[] {"Grupa", "", "460", "860", "1200", "240", "", "",
     "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextPorezDepartmentCPOR;
  private String[] TextPorezDepartmentCPORProps = new String[] {"PorezDepartmentCPOR", "", "", "",
     "", "", "Yes", "", "460", "1100", "1200", "220", "", "", "", "", "", "", "Lucida Bright", "8",
     "", "", "", "", ""};
  public raReportElement TextPorezDepartmentPOR1;
  private String[] TextPorezDepartmentPOR1Props = new String[] {"PorezDepartmentPOR1", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "4600", "1100", "1300", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentIPRODBP;
  private String[] TextPorezDepartmentIPRODBPProps = new String[] {"PorezDepartmentIPRODBP", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "3000", "1100", "1300", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentUKUPOR;
  private String[] TextPorezDepartmentUKUPORProps = new String[] {"PorezDepartmentUKUPOR", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "2120", "1100", "720", "220", "", "",
     "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentCrtica;
  private String[] TextPorezDepartmentCrticaProps = new String[] {"PorezDepartmentCrtica", "", "",
     "", "", "", "Yes", "", "1800", "1100", "180", "220", "", "", "", "", "", "", "Lucida Bright",
     "8", "", "", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "No", "380", "1420", "10020", "0", "", "", ""};
  public raReportElement LabelSlovima_;
  private String[] LabelSlovima_Props = new String[] {"Slovima :", "", "280", "1580", "840", "220",
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "",
     "1140", "1580", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "",
     ""};
  
  public raIzlazSectionFooter2Lines(raReportTemplate owner) {
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
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    Text3 = addModel(ep.TEXT, Text3Props);
    Text4 = addModel(ep.TEXT, Text4Props);
    Text5 = addModel(ep.TEXT, Text5Props);
    Label1 = addModel(ep.LABEL, Label1Props);
    LabelU_K_U_P_N_O = addModel(ep.LABEL, LabelU_K_U_P_N_OProps);
    Line2 = addModel(ep.LINE, Line2Props);
    LabelREKAPITULACIJA_POREZA = addModel(ep.LABEL, LabelREKAPITULACIJA_POREZAProps);
    LabelOsnovica = addModel(ep.LABEL, LabelOsnovicaProps);
    Label2 = addModel(ep.LABEL, Label2Props);
    LabelPorez = addModel(ep.LABEL, LabelPorezProps);
    LabelGrupa = addModel(ep.LABEL, LabelGrupaProps);
    TextPorezDepartmentCPOR = addModel(ep.TEXT, TextPorezDepartmentCPORProps);
    TextPorezDepartmentPOR1 = addModel(ep.TEXT, TextPorezDepartmentPOR1Props);
    TextPorezDepartmentIPRODBP = addModel(ep.TEXT, TextPorezDepartmentIPRODBPProps);
    TextPorezDepartmentUKUPOR = addModel(ep.TEXT, TextPorezDepartmentUKUPORProps);
    TextPorezDepartmentCrtica = addModel(ep.TEXT, TextPorezDepartmentCrticaProps);
    Line3 = addModel(ep.LINE, Line3Props);
    LabelSlovima_ = addModel(ep.LABEL, LabelSlovima_Props);
    TextSLOVIMA = addModel(ep.TEXT, TextSLOVIMAProps);
  }

  private void modifyThis() {
  }
}
