/****license*****************************************************************
**   file: raPopListaSectionHeader1.java
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

public class raPopListaSectionHeader1 extends raReportSection {

  private String[] thisProps = new String[] {"CSKL", "", "", "", "Yes", "", "Yes", "Yes", "500"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "", "120", "2520", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv", "", "2540", "120", "4600", "240",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelJm;
  private String[] LabelJmProps = new String[] {"Jm", "", "7160", "120", "860", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelStvarno_stanje;
  private String[] LabelStvarno_stanjeProps = new String[] {"Stvarno stanje", "", "8040", "120",
     "2720", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "",
     "Center"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "380", "10760", "0", "", "", ""};

  public raPopListaSectionHeader1(raReportTemplate owner) {
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
    LabelSifra = addModel(ep.LABEL, LabelSifraProps);
    LabelNaziv = addModel(ep.LABEL, LabelNazivProps);
    LabelJm = addModel(ep.LABEL, LabelJmProps);
    LabelStvarno_stanje = addModel(ep.LABEL, LabelStvarno_stanjeProps);
    Line1 = addModel(ep.LINE, Line1Props);
  }

  private void modifyThis() {
    this.LabelSifra.setCaption(hr.restart.robno.Aut.getAut().getIzlazCARTdep("Šifra", "Oznaka", "Barcode"));
    this.resizeElement(this.LabelSifra, hr.restart.robno.Aut.getAut().getIzlazCARTdep(750, 1600, 1600), this.LabelNaziv);
//    resizeElement(this.LabelSifra, hr.restart.robno.Aut.getAut().getCARTdependable(750, 1400, 1400), this.LabelNaziv);
  }
}
