/****license*****************************************************************
**   file: raIzlazOTPsifKupVriSectionHeader1.java
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

public class raIzlazOTPsifKupVriSectionHeader1 extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", "440"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "40", "10800", "0", "", "", ""};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "7160", "100", "1000", 
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"�ifra", "", "420", "100", "", "220", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "6620", "100", "520", "220", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "8180", "100", "1140", "220", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_artikla;
  private String[] LabelNaziv_artiklaProps = new String[] {"Naziv artikla", "", "3340", "100", 
     "3260", "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "100", "400", "220", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra_kupca;
  private String[] LabelSifra_kupcaProps = new String[] {"�ifra kupca", "", "1880", "100", "", 
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelVrijednost;
  private String[] LabelVrijednostProps = new String[] {"Vrijednost", "", "9340", "100", "1480", 
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "360", "10800", "0", "", "", ""};

  public raIzlazOTPsifKupVriSectionHeader1(raReportTemplate owner) {
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
    LabelKolicina = addModel(ep.LABEL, LabelKolicinaProps);
    LabelSifra = addModel(ep.LABEL, LabelSifraProps);
    LabelJmj = addModel(ep.LABEL, LabelJmjProps);
    LabelCijena = addModel(ep.LABEL, LabelCijenaProps);
    LabelNaziv_artikla = addModel(ep.LABEL, LabelNaziv_artiklaProps);
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
    LabelSifra_kupca = addModel(ep.LABEL, LabelSifra_kupcaProps);
    LabelVrijednost = addModel(ep.LABEL, LabelVrijednostProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
