/****license*****************************************************************
**   file: raIzlazSectionHeader2RedNoPopust.java
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

public class raIzlazSectionHeader2RedNoPopust extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", "700"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "60", "10800", "0", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"%", "", "7980", "100", "1380", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena__Izn;
  private String[] LabelCijena__IznProps = new String[] {" Cijena / Izn.", "", "9380", "100", "", 
     "220", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "5520", "100", "960", 
     "220", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "100", "480", "460", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "500", "100", "1560", "460", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_artikla__usluge;
  private String[] LabelNaziv_artikla__uslugeProps = new String[] {"Naziv artikla / usluge", "", 
     "2080", "100", "3420", "460", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", 
     "Bold", "", "", "Center"};
  public raReportElement LabelCijena__Izn1;
  private String[] LabelCijena__Izn1Props = new String[] {" Cijena / Izn.", "", "6520", "100", "", 
     "220", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Labelbez_poreza;
  private String[] Labelbez_porezaProps = new String[] {"bez poreza", "", "6520", "340", "", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPorez;
  private String[] LabelPorezProps = new String[] {"Porez", "", "7980", "340", "1380", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Labels_porezom;
  private String[] Labels_porezomProps = new String[] {"s porezom", "", "9380", "340", "", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJm;
  private String[] LabelJmProps = new String[] {"Jm", "", "5520", "340", "960", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "580", "10800", "0", "", "", ""};

  public raIzlazSectionHeader2RedNoPopust(raReportTemplate owner) {
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
    Label1 = addModel(ep.LABEL, Label1Props);
    LabelCijena__Izn = addModel(ep.LABEL, LabelCijena__IznProps);
    LabelKolicina = addModel(ep.LABEL, LabelKolicinaProps);
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
    LabelSifra = addModel(ep.LABEL, LabelSifraProps);
    LabelNaziv_artikla__usluge = addModel(ep.LABEL, LabelNaziv_artikla__uslugeProps);
    LabelCijena__Izn1 = addModel(ep.LABEL, LabelCijena__Izn1Props);
    Labelbez_poreza = addModel(ep.LABEL, Labelbez_porezaProps);
    LabelPorez = addModel(ep.LABEL, LabelPorezProps);
    Labels_porezom = addModel(ep.LABEL, Labels_porezomProps);
    LabelJm = addModel(ep.LABEL, LabelJmProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
