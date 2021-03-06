/****license*****************************************************************
**   file: raDosSH1NI.java
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

public class raDosSH1NI extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", "680"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "20", "40", "10800", "0", "", "", ""};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "20", "100", "440", "460", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"�ifra", "", "480", "100", "1940", "460", 
     "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};


  public raReportElement LabelNaziv_artikla;
  private String[] LabelNaziv_artiklaProps = new String[] {"Naziv artikla", "", "2440", "100", 
      "5060", "460", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
  "Center"};
public raReportElement LabelJmj;
private String[] LabelJmjProps = new String[] {"Jmj", "", "7500", "100", "660", "460", "Normal", 
"Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "8180", "100", "2620", 
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaruceno;
  private String[] LabelNarucenoProps = new String[] {"Naru\u010Deno", "", "8180", "340", "1300", 
     "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIsporuceno;
  private String[] LabelIsporucenoProps = new String[] {"Isporu\u010Deno", "", "9500", "340", 
     "1300", "220", "Normal", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  
  
  
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "580", "10800", "0", "", "", ""};

  public raDosSH1NI(raReportTemplate owner) {
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
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
    LabelSifra = addModel(ep.LABEL, LabelSifraProps);
    LabelNaziv_artikla = addModel(ep.LABEL, LabelNaziv_artiklaProps);
    LabelJmj = addModel(ep.LABEL, LabelJmjProps);
    LabelKolicina = addModel(ep.LABEL, LabelKolicinaProps);
    LabelNaruceno = addModel(ep.LABEL, LabelNarucenoProps);
    LabelIsporuceno = addModel(ep.LABEL, LabelIsporucenoProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
    this.restoreDefaults();
    this.LabelSifra.setCaption(Aut.getAut().getIzlazCARTdep("�ifra", "Oznaka", "Barcode"));
    this.resizeElement(this.LabelSifra, Aut.getAut().getIzlazCARTwidth(), this.LabelNaziv_artikla);
  }
}
