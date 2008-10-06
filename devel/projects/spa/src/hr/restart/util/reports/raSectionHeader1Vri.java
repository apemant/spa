/****license*****************************************************************
**   file: raSectionHeader1Vri.java
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

public class raSectionHeader1Vri extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "Yes", "480"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "6560", "100", "1380",
     "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelJm;
  private String[] LabelJmProps = new String[] {"Jm", "", "6060", "100", "480", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelVrijednost;
  private String[] LabelVrijednostProps = new String[] {"Vrijednost", "", "9340", "100", "1380",
     "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "7960", "100", "1360", "240",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv", "", "2160", "100", "3880", "240",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "420", "100", "1720", "240",
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelRb;
  private String[] LabelRbProps = new String[] {"Rb", "", "", "100", "400", "240", "Normal",
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};

  public raSectionHeader1Vri(raReportTemplate owner) {
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
    LabelKolicina = addModel(ep.LABEL, LabelKolicinaProps);
    LabelJm = addModel(ep.LABEL, LabelJmProps);
    LabelVrijednost = addModel(ep.LABEL, LabelVrijednostProps);
    LabelCijena = addModel(ep.LABEL, LabelCijenaProps);
    LabelNaziv = addModel(ep.LABEL, LabelNazivProps);
    LabelSifra = addModel(ep.LABEL, LabelSifraProps);
    LabelRb = addModel(ep.LABEL, LabelRbProps);
  }

  private void modifyThis() {
    this.LabelSifra.setCaption(hr.restart.robno.Aut.getAut().getIzlazCARTdep("Šifra", "Oznaka", "Barcode"));
    this.resizeElement(this.LabelSifra, hr.restart.robno.Aut.getAut().getIzlazCARTwidth(), this.LabelNaziv);
  }
}
