/****license*****************************************************************
**   file: raPnPSectionHeaderLines.java
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

public class raPnPSectionHeaderLines extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", "680"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "60", "10820", "0", "", "", ""};
  public raReportElement Label_Poreza;
  private String[] Label_PorezaProps = new String[] {"% Poreza", "", "8200", "100", "1200", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "6960", "100", "1220", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIznos;
  private String[] LabelIznosProps = new String[] {"Iznos", "", "9420", "100", "1400", "460", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "100", "460", "460", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "5500", "100", "960", 
     "220", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv", "", "1940", "100", "3540", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "6480", "100", "460", "460", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "480", "100", "", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPakiranje;
  private String[] LabelPakiranjeProps = new String[] {"Pakiranje", "", "5500", "340", "960", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIznos_Pop;
  private String[] LabelIznos_PopProps = new String[] {"Iznos Pop.", "", "8200", "340", "1200", 
     "220", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelNaziv_popusta;
  private String[] LabelNaziv_popustaProps = new String[] {"Naziv popusta", "", "1940", "340", 
     "3540", "220", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement Label_Popusta;
  private String[] Label_PopustaProps = new String[] {"% Popusta", "", "6960", "340", "1220", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra_kupca;
  private String[] LabelSifra_kupcaProps = new String[] {"Šifra kupca", "", "480", "340", "", "220", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "580", "10820", "0", "", "", ""};

  public raPnPSectionHeaderLines(raReportTemplate owner) {
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
    Label_Poreza = addModel(ep.LABEL, Label_PorezaProps);
    LabelCijena = addModel(ep.LABEL, LabelCijenaProps);
    LabelIznos = addModel(ep.LABEL, LabelIznosProps);
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
    LabelKolicina = addModel(ep.LABEL, LabelKolicinaProps);
    LabelNaziv = addModel(ep.LABEL, LabelNazivProps);
    LabelJmj = addModel(ep.LABEL, LabelJmjProps);
    LabelSifra = addModel(ep.LABEL, LabelSifraProps);
    LabelPakiranje = addModel(ep.LABEL, LabelPakiranjeProps);
    LabelIznos_Pop = addModel(ep.LABEL, LabelIznos_PopProps);
    LabelNaziv_popusta = addModel(ep.LABEL, LabelNaziv_popustaProps);
    Label_Popusta = addModel(ep.LABEL, Label_PopustaProps);
    LabelSifra_kupca = addModel(ep.LABEL, LabelSifra_kupcaProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
