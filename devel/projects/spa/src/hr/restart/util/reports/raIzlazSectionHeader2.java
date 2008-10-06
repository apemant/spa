/****license*****************************************************************
**   file: raIzlazSectionHeader2.java
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

public class raIzlazSectionHeader2 extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "", "Yes", "Yes", "660"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "500", "100", "2320", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJm;
  private String[] LabelJmProps = new String[] {"Jm", "", "2840", "340", "1080", "220", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIznos;
  private String[] LabelIznosProps = new String[] {"Iznos", "", "3940", "340", "1280", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPopust;
  private String[] LabelPopustProps = new String[] {"Popust", "", "5240", "340", "1260", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"%", "", "5240", "100", "1260", "220", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijenaIzn;
  private String[] LabelCijenaIznProps = new String[] {" Cijena / Izn.", "", "6520", "100", "",
     "220", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", ""};
  public raReportElement Labelbezporeza;
  private String[] LabelbezporezaProps = new String[] {"bez poreza", "", "6520", "340", "", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"%", "", "7980", "100", "1380", "220", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPorez;
  private String[] LabelPorezProps = new String[] {"Porez", "", "7980", "340", "1380", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Labelsporezom;
  private String[] LabelsporezomProps = new String[] {"s porezom", "", "9380", "340", "", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijenaIzn2;
  private String[] LabelCijenaIzn2Props = new String[] {" Cijena / Izn.", "", "9380", "100", "",
     "220", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "3940", "100", "1280", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "2840", "100", "1080", "220",
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNazivartiklausluge;
  private String[] LabelNazivartiklauslugeProps = new String[] {"Naziv artikla / usluge", "", "500",
     "340", "2320", "220", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "",
     "", "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "100.0188", "480", "460", "Normal",
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};

  public raIzlazSectionHeader2(raReportTemplate owner) {
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
    LabelJm = addModel(ep.LABEL, LabelJmProps);
    LabelIznos = addModel(ep.LABEL, LabelIznosProps);
    LabelPopust = addModel(ep.LABEL, LabelPopustProps);
    Label1 = addModel(ep.LABEL, Label1Props);
    LabelCijenaIzn = addModel(ep.LABEL, LabelCijenaIznProps);
    Labelbezporeza = addModel(ep.LABEL, LabelbezporezaProps);
    Label2 = addModel(ep.LABEL, Label2Props);
    LabelPorez = addModel(ep.LABEL, LabelPorezProps);
    Labelsporezom = addModel(ep.LABEL, LabelsporezomProps);
    LabelCijenaIzn2 = addModel(ep.LABEL, LabelCijenaIzn2Props);
    LabelCijena = addModel(ep.LABEL, LabelCijenaProps);
    LabelKolicina = addModel(ep.LABEL, LabelKolicinaProps);
    LabelNazivartiklausluge = addModel(ep.LABEL, LabelNazivartiklauslugeProps);
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
  }

  private void modifyThis() {
    LabelSifra.setCaption(Aut.getAut().getIzlazCARTdep("Šifra", "Oznaka", "Barcode"));
  }
}
