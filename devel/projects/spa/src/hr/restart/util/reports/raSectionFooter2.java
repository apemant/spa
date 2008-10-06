/****license*****************************************************************
**   file: raSectionFooter2.java
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

public class raSectionFooter2 extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "After", "", "", "Yes", "", "Yes", "3260"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "", "3920", "240", "Normal", "Gray",
    "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"UKPOR3\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7980", "", "1400", "240", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"IPRODSP\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9380", "", "1460", "240", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"IPRODBP\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6520", "", "1460", "240", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(dsum \"UIRAB\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5240", "", "1280", "240", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(dsum \"INETO\")", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "", "", "3940", "", "1300", "240", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement LabelU_K_U_P;
  private String[] LabelU_K_U_PProps = new String[] {"U K U P N O", "", "80", "", "1460", "200", "",
     "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raReportElement TextPorezDepartmentCPOR;
  private String[] TextPorezDepartmentCPORProps = new String[] {"PorezDepartmentCPOR", "", "", "",
     "", "", "Yes", "", "260", "380", "2300", "220", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentPOR1;
  private String[] TextPorezDepartmentPOR1Props = new String[] {"PorezDepartmentPOR1", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "5240", "380", "1400", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentIPRODBP;
  private String[] TextPorezDepartmentIPRODBPProps = new String[] {"PorezDepartmentIPRODBP", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "3680", "380", "1400", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentUKUPOR;
  private String[] TextPorezDepartmentUKUPORProps = new String[] {"PorezDepartmentUKUPOR", "", "",
     "", "", "", "Yes", "", "2860", "380", "720", "220", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentCrtica;
  private String[] TextPorezDepartmentCrticaProps = new String[] {"PorezDepartmentCrtica", "", "",
     "", "", "", "Yes", "", "2640", "380", "180", "220", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "No", "260", "680", "6540", "0", "", "", ""};
  public raReportElement TextNACINPLACANJA;
  private String[] TextNACINPLACANJAProps = new String[] {"NACINPLACANJA", "", "", "", "", "",
     "Yes", "Yes", "260", "800", "3840", "220", "", "", "", "", "", "", "Lucida Bright", "8", "",
     "", "", "", ""};
  public raReportElement TextDATUMNAPLATE;
  private String[] TextDATUMNAPLATEProps = new String[] {"DATUMNAPLATE", "", "", "", "", "", "Yes",
     "Yes", "4120", "800", "1120", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "", ""};
  public raReportElement TextIZNOSRATE;
  private String[] TextIZNOSRATEProps = new String[] {"IZNOSRATE", "", "", "", "", "", "Yes", "Yes",
     "5260", "800", "1580", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "Right", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "No", "340", "1300", "10020", "0", "", "", ""};
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "",
     "1140", "1480", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "",
     ""};
  public raReportElement LabelSlovima_;
  private String[] LabelSlovima_Props = new String[] {"Slovima :", "", "280", "1480", "840", "220",
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelNapomena_;
  private String[] LabelNapomena_Props = new String[] {"Napomena :", "", "280", "1900", "1300",
     "280", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextNAPOMENAOPIS;
  private String[] TextNAPOMENAOPISProps = new String[] {"NAPOMENAOPIS", "", "", "", "", "", "Yes",
     "Yes", "260", "2160", "10220", "380", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
     "", ""};
  public raReportElement LabelObradio;
  private String[] LabelObradioProps = new String[] {"Obradio", "", "1520", "2800", "1200", "280",
     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement LabelOdgovorna_osoba;
  private String[] LabelOdgovorna_osobaProps = new String[] {"Odgovorna osoba", "", "8060", "2800",
     "1700", "280", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};

  public raSectionFooter2(raReportTemplate owner) {
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
    Label1 = addModel(ep.LABEL, Label1Props);
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    Text3 = addModel(ep.TEXT, Text3Props);
    Text4 = addModel(ep.TEXT, Text4Props);
    Text5 = addModel(ep.TEXT, Text5Props);
    LabelU_K_U_P = addModel(ep.LABEL, LabelU_K_U_PProps);
    TextPorezDepartmentCPOR = addModel(ep.TEXT, TextPorezDepartmentCPORProps);
    TextPorezDepartmentPOR1 = addModel(ep.TEXT, TextPorezDepartmentPOR1Props);
    TextPorezDepartmentIPRODBP = addModel(ep.TEXT, TextPorezDepartmentIPRODBPProps);
    TextPorezDepartmentUKUPOR = addModel(ep.TEXT, TextPorezDepartmentUKUPORProps);
    TextPorezDepartmentCrtica = addModel(ep.TEXT, TextPorezDepartmentCrticaProps);
    Line1 = addModel(ep.LINE, Line1Props);
    TextNACINPLACANJA = addModel(ep.TEXT, TextNACINPLACANJAProps);
    TextDATUMNAPLATE = addModel(ep.TEXT, TextDATUMNAPLATEProps);
    TextIZNOSRATE = addModel(ep.TEXT, TextIZNOSRATEProps);
    Line2 = addModel(ep.LINE, Line2Props);
    TextSLOVIMA = addModel(ep.TEXT, TextSLOVIMAProps);
    LabelSlovima_ = addModel(ep.LABEL, LabelSlovima_Props);
    LabelNapomena_ = addModel(ep.LABEL, LabelNapomena_Props);
    TextNAPOMENAOPIS = addModel(ep.TEXT, TextNAPOMENAOPISProps);
    LabelObradio = addModel(ep.LABEL, LabelObradioProps);
    LabelOdgovorna_osoba = addModel(ep.LABEL, LabelOdgovorna_osobaProps);
  }

  private void modifyThis() {
  }
}
