/****license*****************************************************************
**   file: raRadniNalogObradaSection_Header2.java
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

public class raRadniNalogObradaSection_Header2 extends raReportSection {

  private String[] thisProps = new String[] {"VRDOK", "", "", "", "Yes", "No", "", "", "580"};
  public raReportElement TextRADOVIMATERIJAL;
  private String[] TextRADOVIMATERIJALProps = new String[] {"RADOVIMATERIJAL", "", "", "", "", "", 
     "", "", "140", "", "2200", "240", "", "-1973791", "", "", "", "", "Lucida Bright", "9", "Bold", 
     "", "", "", ""};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"", "", "140", "", "10700", "260", "Normal", "Gray", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label2;
  private String[] Label2Props = new String[] {"", "", "8380", "280", "1200", "260", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label3;
  private String[] Label3Props = new String[] {"", "", "7160", "280", "1200", "260", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label4;
  private String[] Label4Props = new String[] {"", "", "9600", "280", "1240", "260", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label5;
  private String[] Label5Props = new String[] {"", "", "6520", "280", "620", "260", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label6;
  private String[] Label6Props = new String[] {"", "", "2180", "280", "4320", "260", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label7;
  private String[] Label7Props = new String[] {"", "", "700", "280", "1460", "260", "Normal", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label8;
  private String[] Label8Props = new String[] {"", "", "140", "280", "540", "260", "Normal", "Gray", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv", "", "2220", "300", "4320", "220", "", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "7200", "300", "1200", 
     "220", "", "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIznos;
  private String[] LabelIznosProps = new String[] {"Iznos", "", "9640", "300", "1200", "220", "", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "8420", "300", "1200", "220", "", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "160", "300", "540", "220", "", "Gray", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJm;
  private String[] LabelJmProps = new String[] {"Jm", "", "6560", "300", "620", "220", "", "Gray", 
     "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "720", "300", "1460", "220", "", 
     "Gray", "", "", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "160", "560", "10660", "0", "", "", ""};

  public raRadniNalogObradaSection_Header2(raReportTemplate owner) {
    super(owner.template.getModel(raElixirProperties.SECTION_HEADER + 2));
    this.setDefaults(thisProps);

    addElements();

    final raReportTemplate ownerFF = owner;

    addReportModifier(new ReportModifier() {
      public void modify() {
        modifyThis(ownerFF);
      }
    });
  }

  private void addElements() {
    Label1 = addModel(ep.LABEL, Label1Props);
    Label2 = addModel(ep.LABEL, Label2Props);
    Label3 = addModel(ep.LABEL, Label3Props);
    Label4 = addModel(ep.LABEL, Label4Props);
    Label5 = addModel(ep.LABEL, Label5Props);
    Label6 = addModel(ep.LABEL, Label6Props);
    Label7 = addModel(ep.LABEL, Label7Props);
    Label8 = addModel(ep.LABEL, Label8Props);
    LabelNaziv = addModel(ep.LABEL, LabelNazivProps);
    LabelKolicina = addModel(ep.LABEL, LabelKolicinaProps);
    LabelIznos = addModel(ep.LABEL, LabelIznosProps);
    LabelCijena = addModel(ep.LABEL, LabelCijenaProps);
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
    LabelJm = addModel(ep.LABEL, LabelJmProps);
    LabelSifra = addModel(ep.LABEL, LabelSifraProps);
    TextRADOVIMATERIJAL = addModel(ep.TEXT, TextRADOVIMATERIJALProps);
    Line1 = addModel(ep.LINE, Line1Props);
  }

  private void modifyThis(raReportTemplate owner) {
    LabelSifra.setCaption(hr.restart.robno.Aut.getAut().getIzlazCARTdep("Šifra", "Oznaka", "Barcode"));
    resizeElement(this.LabelSifra, hr.restart.robno.Aut.getAut().getIzlazCARTwidth(), this.LabelNaziv);
    resizeElement(this.Label7, hr.restart.robno.Aut.getAut().getIzlazCARTwidth(), this.Label6);
    if ((owner instanceof hr.restart.robno.repObracunRadnogNalogaTemplate)||
        (owner instanceof hr.restart.robno.repStavkeRadnogNalogaTemplate)){
      deflateElement(LabelCijena); //,LabelNaziv);
      deflateElement(Label3);
      deflateElement(LabelIznos); //,LabelNaziv);
      deflateElement(Label4);
    }
  }
}
