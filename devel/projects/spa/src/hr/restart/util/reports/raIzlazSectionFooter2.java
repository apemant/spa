/****license*****************************************************************
**   file: raIzlazSectionFooter2.java
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

public class raIzlazSectionFooter2 extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "4280"};

//
//  public raReportElement LabelSlovima;
//  private String[] LabelSlovimaProps = new String[] {"Slovima :", "", "", "1800", "840", "220", "",
//    "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelSlovima;
  private String[] LabelSlovimaProps = new String[] {"Slovima :", "", "280", "1300", "840", "220",
     "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextSLOVIMA;
  private String[] TextSLOVIMAProps = new String[] {"SLOVIMA", "", "", "", "", "", "Yes", "",
    "1140", "1300", "8500", "220", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "", ""};

  public raReportElement LabelNapomena;
  private String[] LabelNapomenaProps = new String[] {"Napomena :", "", "280", "1560", "1304.1",
    "283.5", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement TextNAPOMENAOPIS;
  private String[] TextNAPOMENAOPISProps = new String[] {"NAPOMENAOPIS", "", "", "", "", "", "Yes",
    "Yes", "280", "1820", "10200", "1160", "", "", "", "", "", "", "Lucida Bright", "8", "", "",
    "", "", ""};

//  public raReportElement TextNAPOMENAOPIS;
//  private String[] TextNAPOMENAOPISProps = new String[] {"NAPOMENAOPIS", "", "", "", "", "", "Yes",
//     "Yes", "", "2220", "10580", "680", "", "", "", "", "", "", "Lucida Bright", "8", "", "", "",
//     "", ""};


//  public raReportElement LabelOdgovornaosoba;
//  private String[] LabelOdgovornaosobaProps = new String[] {"Odgovorna osoba", "", "8060", "3340",
//     "1701.0", "283.5", "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  /*public raReportElement TextPorezDepartmentCrtica;
  private String[] TextPorezDepartmentCrticaProps = new String[] {"PorezDepartmentCrtica", "", "",
     "", "", "", "Yes", "", "2640", "380", "180", "220", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentUKUPOR;
  private String[] TextPorezDepartmentUKUPORProps = new String[] {"PorezDepartmentUKUPOR", "", "",
     "", "", "", "Yes", "", "2860", "380", "720", "220", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentIPRODBP;
  private String[] TextPorezDepartmentIPRODBPProps = new String[] {"PorezDepartmentIPRODBP", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "3680", "380", "1400", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentPOR1;
  private String[] TextPorezDepartmentPOR1Props = new String[] {"PorezDepartmentPOR1", "", "",
     "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "5240", "380", "1400", "220", "",
     "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentCPOR;
  private String[] TextPorezDepartmentCPORProps = new String[] {"PorezDepartmentCPOR", "", "", "",
     "", "", "Yes", "", "260", "380", "2300", "220", "", "Light Gray", "", "", "", "",
     "Lucida Bright", "8", "", "", "", "Right", ""};*/


  public raReportElement LabelREKAPITULACIJA_POREZA;
  private String[] LabelREKAPITULACIJA_POREZAProps = new String[] {"REKAPITULACIJA POREZA", "",
    "440", "300", "5020", "240", "", "", "", "", "", "", "Lucida Bright", "9", "", "", "",
    "Center"};
  public raReportElement TextPorezDepartmentUKUPOR;
  private String[] TextPorezDepartmentUKUPORProps = new String[] {"PorezDepartmentUKUPOR", "", "",
    "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "2120", "820", "720", "220", "",
    "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentCrtica;
  private String[] TextPorezDepartmentCrticaProps = new String[] {"PorezDepartmentCrtica", "", "",
    "", "", "", "Yes", "", "1800", "820", "180", "220", "", "", "", "", "", "",
    "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentCPOR;
  private String[] TextPorezDepartmentCPORProps = new String[] {"PorezDepartmentCPOR", "", "", "",
    "", "", "Yes", "", "460", "820", "1200", "220", "", "", "", "", "", "",
    "Lucida Bright", "8", "", "", "", "", ""};
  public raReportElement TextPorezDepartmentPOR1;
  private String[] TextPorezDepartmentPOR1Props = new String[] {"PorezDepartmentPOR1", "", "",
    "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "4600", "820", "1300", "220", "",
    "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement TextPorezDepartmentIPRODBP;
  private String[] TextPorezDepartmentIPRODBPProps = new String[] {"PorezDepartmentIPRODBP", "", "",
    "Number|false|1|309|2|2|true|3|false", "", "", "Yes", "", "3000", "820", "1300", "220", "",
    "Light Gray", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right", ""};
  public raReportElement LabelGrupa;
  private String[] LabelGrupaProps = new String[] {"Grupa", "", "460", "580", "1200", "240", "", "",
    "", "", "", "", "Lucida Bright", "8", "", "", "", ""};
  public raReportElement LabelPorez;
  private String[] LabelPorezProps = new String[] {"Porez", "", "4600", "580", "1300", "240", "",
    "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement LabelOsnovica;
  private String[] LabelOsnovicaProps = new String[] {"Osnovica", "", "3000", "580", "1300", "240",
    "", "", "", "", "", "", "Lucida Bright", "8", "", "", "", "Right"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"%", "", "2140", "580", "720", "240", "", "", "", "",
    "", "", "Lucida Bright", "8", "", "", "", "Center"};




  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(dsum \"INETO\")", "", "",
    "Number|false|1|309|2|2|true|3|false", "", "", "", "", "3940", "", "1300", "240", "Normal",
    "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text2;
  private String[] Text2Props = new String[] {"=(dsum \"UIRAB\")", "", "",
    "Number|false|1|309|2|2|true|3|false", "", "", "", "", "5240", "", "1280", "240", "Normal",
    "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text3;
  private String[] Text3Props = new String[] {"=(dsum \"IPRODBP\")", "", "",
    "Number|false|1|309|2|2|true|3|false", "", "", "", "", "6520", "", "1460", "240", "Normal",
    "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text4;
  private String[] Text4Props = new String[] {"=(dsum \"IPRODSP\")", "", "",
    "Number|false|1|309|2|2|true|3|false", "", "", "", "", "9380", "", "1460", "240", "Normal",
    "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
  public raReportElement Text5;
  private String[] Text5Props = new String[] {"=(dsum \"UKPOR3\")", "", "",
    "Number|false|1|309|2|2|true|3|false", "", "", "", "", "7980", "", "1400", "240", "Normal",
    "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Right", ""};
//  public raReportElement LabelObradio;
//  private String[] LabelObradioProps = new String[] {"Obradio", "", "1520", "3340", "1200", "283.5",
//     "", "", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "No", "340", "760", "10020", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "", "", "3920", "240", "Normal", "Gray",
    "Solid", "Gray", "", "", "Lucida Bright", "9", "Bold", "", "", "Right"};
  public raReportElement LabelUKUP;
  private String[] LabelUKUPProps = new String[] {"U K U P N O", "", "80", "0", "1460", "200", "",
    "", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", ""};
  public raIzlazSectionFooter2(raReportTemplate owner) {
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
    LabelNapomena = addModel(ep.LABEL, LabelNapomenaProps);
    LabelSlovima = addModel(ep.LABEL, LabelSlovimaProps);
//    LabelOdgovornaosoba = addModel(ep.LABEL, LabelOdgovornaosobaProps);
    LabelREKAPITULACIJA_POREZA = addModel(ep.LABEL, LabelREKAPITULACIJA_POREZAProps);
    LabelGrupa = addModel(ep.LABEL, LabelGrupaProps);
    LabelPorez = addModel(ep.LABEL, LabelPorezProps);
    Label2 = addModel(ep.LABEL, Label2Props);
    LabelOsnovica = addModel(ep.LABEL, LabelOsnovicaProps);
    TextPorezDepartmentCrtica = addModel(ep.TEXT, TextPorezDepartmentCrticaProps);
    TextPorezDepartmentUKUPOR = addModel(ep.TEXT, TextPorezDepartmentUKUPORProps);
    TextPorezDepartmentIPRODBP = addModel(ep.TEXT, TextPorezDepartmentIPRODBPProps);
    TextPorezDepartmentPOR1 = addModel(ep.TEXT, TextPorezDepartmentPOR1Props);
    TextPorezDepartmentCPOR = addModel(ep.TEXT, TextPorezDepartmentCPORProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Text2 = addModel(ep.TEXT, Text2Props);
    Text3 = addModel(ep.TEXT, Text3Props);
    Text4 = addModel(ep.TEXT, Text4Props);
    Text5 = addModel(ep.TEXT, Text5Props);
//    LabelObradio = addModel(ep.LABEL, LabelObradioProps);
    Line1 = addModel(ep.LINE, Line1Props);
    Label1 = addModel(ep.LABEL, Label1Props);
    LabelUKUP = addModel(ep.LABEL, LabelUKUPProps);
    TextNAPOMENAOPIS = addModel(ep.TEXT, TextNAPOMENAOPISProps);
    TextSLOVIMA = addModel(ep.TEXT, TextSLOVIMAProps);
  }

  private void modifyThis() {
  }
}