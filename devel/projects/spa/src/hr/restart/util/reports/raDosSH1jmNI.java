/****license*****************************************************************
**   file: raDosSH1jmNI.java
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

public class raDosSH1jmNI extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", "680"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "20", "40", "10800", "0", "", "", ""};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "20", "100", "440", "460", "Normal", "Gray", 
      "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "480", "100", "1940", "460", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  
  public raReportElement LabelNaziv_artikla;
  private String[] LabelNaziv_artiklaProps = new String[] {"Naziv artikla", "", "2440", "100", "3040", "460", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
//  private String[] LabelNaziv_artiklaProps = new String[] {"Naziv artikla", "", "2440", "100", "3840", "460", "Normal", 
//      "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKom;
  private String[] LabelKomProps = new String[] {"Kom", "", "6140", "100", "1260", "460", "Normal", 
      "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
//  private String[] LabelKomProps = new String[] {"Kom", "", "6300", "100", "1260", "460", "Normal", 
//      "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "5500", "100", "620", "460", "Normal", 
      "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
//  private String[] LabelJmjProps = new String[] {"Jmj", "", "7580", "100", "620", "460", "Normal", 
//      "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "7420", "100", "3420", 
      "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
//  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "8220", "100", "2800", 
//      "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaruceno;
//  "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
//  public raReportElement LabelNaruceno;
  private String[] LabelNarucenoProps = new String[] {"Naru\u010Deno", "", "7420", "340", "1700", 
      "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
//  private String[] LabelNarucenoProps = new String[] {"Naru\u010Deno", "", "8220", "340", "1300", 
//      "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIsporuceno;
  private String[] LabelIsporucenoProps = new String[] {"Isporu\u010Deno", "", "9140", "340", 
      "1700", "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
  "Center"};
//  private String[] LabelIsporucenoProps = new String[] {"Isporu\u010Deno", "", "9540", "340", 
//      "1300", "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
//  "Center"};
  
  
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "20", "580", "10800", "0", "", "", ""};

  public raDosSH1jmNI(raReportTemplate owner) {
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
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
    LabelNaziv_artikla = addModel(ep.LABEL, LabelNaziv_artiklaProps);
    LabelKom = addModel(ep.LABEL, LabelKomProps);
    LabelJmj = addModel(ep.LABEL, LabelJmjProps);
    LabelIsporuceno = addModel(ep.LABEL, LabelIsporucenoProps);
    LabelNaruceno = addModel(ep.LABEL, LabelNarucenoProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
    this.restoreDefaults();
    this.LabelSifra.setCaption(Aut.getAut().getIzlazCARTdep("Šifra", "Oznaka", "Barcode"));
    this.resizeElement(this.LabelSifra, Aut.getAut().getIzlazCARTwidth(), this.LabelNaziv_artikla);
    String komText = frmParam.getParam("robno", "komText", "Kom",
      "Tekst za ispisati na mjestu dodatne kolièine na dostavnici");
    LabelKom.setCaption(komText);
  }
}
