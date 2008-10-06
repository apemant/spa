/****license*****************************************************************
**   file: raOSPageHeader002.java
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

public class raOSPageHeader002 extends raReportSection {

  private String[] thisProps = new String[] {"", "", "380"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "0", "15640", "0", "", "", ""};
  public raReportElement LabelIspravak_vrijednost;
  private String[] LabelIspravak_vrijednostProps = new String[] {"Ispravak vrijednost", "", "10840",
     "40", "2420", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelSadasnja_vrijednost;
  private String[] LabelSadasnja_vrijednostProps = new String[] {"Sadašnja vrijednost", "", "13280",
     "40", "2400", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "", "40", "1920", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_organizacijske_jedinice;
  private String[] LabelNaziv_organizacijske_jediniceProps = new String[] {
     "Naziv organizacijske jedinice", "", "1940", "40", "6440", "240", "Normal", "Gray", "", "", "",
     "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Text1;
  private String[] Text1Props = new String[] {"=(put-var \"row\" (+(get-var \"row\") 1))", "", "",
     "", "No", "", "", "", "2220", "40", "5640", "80", "", "", "", "", "", "", "", "", "", "", "",
     "", ""};
  public raReportElement LabelOsnovica_vrijednost;
  private String[] LabelOsnovica_vrijednostProps = new String[] {"Osnovica vrijednost", "", "8400",
     "40", "2420", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "",
     "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "300", "15640", "0", "", "", ""};

  public raOSPageHeader002(raReportTemplate owner) {
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
    LabelSifra = addModel(ep.LABEL, LabelSifraProps);
    LabelNaziv_organizacijske_jedinice = addModel(ep.LABEL, LabelNaziv_organizacijske_jediniceProps);
    LabelIspravak_vrijednost = addModel(ep.LABEL, LabelIspravak_vrijednostProps);
    LabelSadasnja_vrijednost = addModel(ep.LABEL, LabelSadasnja_vrijednostProps);
    LabelOsnovica_vrijednost = addModel(ep.LABEL, LabelOsnovica_vrijednostProps);
    Text1 = addModel(ep.TEXT, Text1Props);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
