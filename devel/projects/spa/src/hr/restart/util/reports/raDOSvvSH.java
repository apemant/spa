/****license*****************************************************************
**   file: raDOSvvSH.java
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

import hr.restart.robno.Aut;
import hr.restart.sisfun.frmParam;

public class raDOSvvSH extends raReportSection {

  private String[] thisProps = new String[] {"FormatBroj", "", "", "", "Yes", "", "", "Yes", "460"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "60", "10780", "0", "", "", ""};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "8660", "100", "1080", 
     "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelSif;
  private String[] LabelSifProps = new String[] {"Šifra", "", "420", "100", 
     "1380", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", 
     "Center"};
  public raReportElement LabelSifra_artikla;
  private String[] LabelSifra_artiklaProps = new String[] {"Šifra artikla", "", "1820", "100", 
     "1720", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", 
     "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "9760", "100", "1040", "240", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_artikla;
  private String[] LabelNaziv_artiklaProps = new String[] {"Naziv artikla", "", "3560", "100", 
     "3680", "240", "Normal", "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", 
     "Center"};
  public raReportElement LabelRb;
  private String[] LabelRbProps = new String[] {"Rb", "", "", "100", "400", "240", "Normal", "Gray", 
     "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelKom;
  private String[] LabelKomProps = new String[] {"Kom", "", "7760", "100", "880", "240", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelJm;
  private String[] LabelJmProps = new String[] {"Jm", "", "7260", "100", "480", "240", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "360", "10780", "0", "", "", ""};

  public raDOSvvSH(raReportTemplate owner) {
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
    LabelSif = addModel(ep.LABEL, LabelSifProps);
    LabelSifra_artikla = addModel(ep.LABEL, LabelSifra_artiklaProps);
    LabelCijena = addModel(ep.LABEL, LabelCijenaProps);
    LabelNaziv_artikla = addModel(ep.LABEL, LabelNaziv_artiklaProps);
    LabelRb = addModel(ep.LABEL, LabelRbProps);
    LabelKom = addModel(ep.LABEL, LabelKomProps);
    LabelJm = addModel(ep.LABEL, LabelJmProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
    this.restoreDefaults();
    this.LabelSifra_artikla.setCaption(Aut.getAut().getIzlazCARTdep("Šifra", "Oznaka", "Barcode"));
    this.resizeElement(this.LabelSifra_artikla, Aut.getAut().getIzlazCARTwidth(), this.LabelNaziv_artikla);
    String komText = frmParam.getParam("robno", "komText", "Kom",
      "Tekst za ispisati na mjestu dodatne kolièine na dostavnici");
    LabelKom.setCaption(komText);
  }
}
