/****license*****************************************************************
**   file: raIzlazOTPVriMegabalstSectionHeader1.java
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

public class raIzlazOTPVriMegabalstSectionHeader1 extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", "680"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "20", "40", "10800", "0", "", "", ""};
  public raReportElement LabelVrijednost;
  private String[] LabelVrijednostProps = new String[] {"Vrijednost", "", "9340", "100", "1480", 
     "460", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "20", "100", "440", "460", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_artikla;
  private String[] LabelNaziv_artiklaProps = new String[] {"Naziv artikla", "", "1940", "100", 
     "5220", "460", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "480", "100", "", "220", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "7180", "100", "640", "460", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "7840", "100", "1480", 
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPakiranje;
  private String[] LabelPakiranjeProps = new String[] {"Pakiranje", "", "7840", "340", "1480", 
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra_kupca;
  private String[] LabelSifra_kupcaProps = new String[] {"Šifra kupca", "", "480", "340", "", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line3;
  private String[] Line3Props = new String[] {"", "", "", "580", "10800", "0", "", "", ""};

  public raIzlazOTPVriMegabalstSectionHeader1(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 1));
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
    LabelVrijednost = addModel(ep.LABEL, LabelVrijednostProps);
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
    LabelNaziv_artikla = addModel(ep.LABEL, LabelNaziv_artiklaProps);
    LabelSifra = addModel(ep.LABEL, LabelSifraProps);
    LabelJmj = addModel(ep.LABEL, LabelJmjProps);
    LabelKolicina = addModel(ep.LABEL, LabelKolicinaProps);
    LabelPakiranje = addModel(ep.LABEL, LabelPakiranjeProps);
    LabelSifra_kupca = addModel(ep.LABEL, LabelSifra_kupcaProps);
    Line3 = addModel(ep.LINE, Line3Props);
  }

  private void modifyThis() {
  }
}
