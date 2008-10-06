/****license*****************************************************************
**   file: raUslSectionFooter.java
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

public class raUslSectionFooter extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "2400"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "100", "10800", "0", "", "", ""};
  public raReportElement LabelUKUPNO;
  private String[] LabelUKUPNOProps = new String[] {"UKUPNO", "", "7280", "260", "1460", "200", "", 
     "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement LabelREKAPITULACIJA_POREZA;
  private String[] LabelREKAPITULACIJA_POREZAProps = new String[] {"REKAPITULACIJA POREZA", "", 
     "660", "260", "5020", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "", 
     "Center"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"IPRODBP\")", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8960", "260", "1860", "220", "", 
     "Gray", "", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", "No"};
  public raReportElement LabelPOREZ_22;
  private String[] LabelPOREZ_22Props = new String[] {"POREZ 22%", "", "7280", "520", "1460", "220", 
     "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {
     "=(+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\"))", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8960", "520", "1860", "220", "", "", 
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", "No"};
  public raReportElement LabelGrupa;
  private String[] LabelGrupaProps = new String[] {"Grupa", "", "680", "540", "1200", "240", "", "", 
     "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelPorez;
  private String[] LabelPorezProps = new String[] {"Porez", "", "4820", "540", "1300", "240", "", 
     "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement LabelOsnovica;
  private String[] LabelOsnovicaProps = new String[] {"Osnovica", "", "3220", "540", "1300", "240", 
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"%", "", "2360", "540", "720", "240", "", "", "", "", 
     "", "", "Lucida Bright", "8", "", "", "", "Center"};
  public raReportElement TextPorezDepartmentUKUPOR;
  private String[] TextPorezDepartmentUKUPORProps = new String[] {"PorezDepartmentUKUPOR", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "2340", "780", "720", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentCrtica;
  private String[] TextPorezDepartmentCrticaProps = new String[] {"PorezDepartmentCrtica", "", "", 
     "", "", "", "Yes", "", "2020", "780", "180", "220", "", "Light Gray", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentCPOR;
  private String[] TextPorezDepartmentCPORProps = new String[] {"PorezDepartmentCPOR", "", "", "", 
     "", "", "Yes", "", "680", "780", "1200", "220", "", "Light Gray", "", "", "", "", 
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentPOR1;
  private String[] TextPorezDepartmentPOR1Props = new String[] {"PorezDepartmentPOR1", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "4820", "780", "1300", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentIPRODBP;
  private String[] TextPorezDepartmentIPRODBPProps = new String[] {"PorezDepartmentIPRODBP", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "3220", "780", "1300", "220", "", 
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelSVEUKUPNO;
  private String[] LabelSVEUKUPNOProps = new String[] {"SVEUKUPNO", "", "7280", "800", "1460", 
     "220", "", "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {
     "=(+ (dsum \"IPRODBP\") (+ (+ (dsum \"POR1\") (dsum \"POR2\")) (dsum \"POR3\")))", "", "", 
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "8960", "800", "1860", "220", "", "", 
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Right", "No"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "7280", "1100", "3520", "0", "", "", ""};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "No", "400", "1180", "9540", "0", "", "", ""};
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "", "880", 
     "1300", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement LabelSlovima_;
  private String[] LabelSlovima_Props = new String[] {"Slovima :", "", "20", "1300", "840", "220", 
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};

  public raUslSectionFooter(raReportTemplate owner) {
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
    LabelUKUPNO = addModel(ep.LABEL, LabelUKUPNOProps);
    LabelREKAPITULACIJA_POREZA = addModel(ep.LABEL, LabelREKAPITULACIJA_POREZAProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    LabelPOREZ_22 = addModel(ep.LABEL, LabelPOREZ_22Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    LabelGrupa = addModel(ep.LABEL, LabelGrupaProps);
    LabelPorez = addModel(ep.LABEL, LabelPorezProps);
    LabelOsnovica = addModel(ep.LABEL, LabelOsnovicaProps);
    Label1 = addModel(ep.LABEL, Label1Props);
    TextPorezDepartmentUKUPOR = addModel(ep.TEXT, TextPorezDepartmentUKUPORProps);
    TextPorezDepartmentCrtica = addModel(ep.TEXT, TextPorezDepartmentCrticaProps);
    TextPorezDepartmentCPOR = addModel(ep.TEXT, TextPorezDepartmentCPORProps);
    TextPorezDepartmentPOR1 = addModel(ep.TEXT, TextPorezDepartmentPOR1Props);
    TextPorezDepartmentIPRODBP = addModel(ep.TEXT, TextPorezDepartmentIPRODBPProps);
    LabelSVEUKUPNO = addModel(ep.LABEL, LabelSVEUKUPNOProps);
    Text3 = addModel(ep.TEXT, Text3Props);
    Line2 = addModel(ep.LINE, Line2Props);
    Line3 = addModel(ep.LINE, Line3Props);
    TextSLOVIMA = addModel(ep.TEXT, TextSLOVIMAProps);
    LabelSlovima_ = addModel(ep.LABEL, LabelSlovima_Props);
  }

  private void modifyThis() {
  }
}
