/****license*****************************************************************
**   file: raOSPageHeader001.java
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

public class raOSPageHeader001 extends raReportSection {

  private String[] thisProps = new String[] {"", "", "460"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "0", "15660", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "13280", "40", "2400", "340", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "10840", "40", "2420", "340", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label3;
  private String[] Label3Props = new String[] {"", "", "8400", "40", "2420", "340", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label4;
  private String[] Label4Props = new String[] {"", "", "1940", "40", "6440", "340", "Normal", 
     "Gray", "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Label5;
  private String[] Label5Props = new String[] {"", "", "", "40", "1920", "340", "Normal", "Gray", 
     "", "", "", "", "", "", "", "", "", ""};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(put-var \"row\" (+(get-var \"row\") 1))", "", "", 
     "", "No", "", "", "", "2580", "80", "5640", "80", "", "", "", "", "", "", "", "", "", "", "", 
     "", ""};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv", "", "1980", "100", "6360", "220", "", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelOsnovica_vrijednost;
  private String[] LabelOsnovica_vrijednostProps = new String[] {"Osnovica vrijednost", "", "8420", 
     "100", "2360", "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelIspravak_vrijednost;
  private String[] LabelIspravak_vrijednostProps = new String[] {"Ispravak vrijednost", "", "10860", 
     "100", "2340", "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelSadasnja_vrijednost;
  private String[] LabelSadasnja_vrijednostProps = new String[] {"Sadašnja vrijednost", "", "13320", 
     "100", "2360", "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement TextLabelaListe;
  private String[] TextLabelaListeProps = new String[] {"LabelaListe", "", "", "", "", "", "", "", 
     "40", "100", "1880", "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center", ""};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "400", "15660", "0", "", "", ""};

  public raOSPageHeader001(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.PAGE_HEADER));
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
    Label2 = addModel(ep.LABEL, Label2Props);
    Label3 = addModel(ep.LABEL, Label3Props);
    Label4 = addModel(ep.LABEL, Label4Props);
    Label5 = addModel(ep.LABEL, Label5Props);
    Text1 = addModel(ep.TEXT, Text1Props);
    LabelNaziv = addModel(ep.LABEL, LabelNazivProps);
    LabelOsnovica_vrijednost = addModel(ep.LABEL, LabelOsnovica_vrijednostProps);
    LabelIspravak_vrijednost = addModel(ep.LABEL, LabelIspravak_vrijednostProps);
    LabelSadasnja_vrijednost = addModel(ep.LABEL, LabelSadasnja_vrijednostProps);
    TextLabelaListe = addModel(ep.TEXT, TextLabelaListeProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
