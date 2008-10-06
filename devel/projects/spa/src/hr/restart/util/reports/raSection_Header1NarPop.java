/****license*****************************************************************
**   file: raSection_Header1NarPop.java
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

public class raSection_Header1NarPop extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", "440"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "60", "10800", "0", "", "", ""};
  public raReportElement LabelIznos;
  private String[] LabelIznosProps = new String[] {"Iznos", "", "9640", "100", "1180", "240", 
     "Normal", "-4275514", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelJm;
  private String[] LabelJmProps = new String[] {"Jm", "", "5760", "100", "480", "240", "Normal", 
     "-4275514", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement Label_pop;
  private String[] Label_popProps = new String[] {"% pop", "", "8740", "100", "880", "240", 
     "Normal", "-4275514", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "7520", "100", "1200", "240", 
     "Normal", "-4275514", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv_artikla;
  private String[] LabelNaziv_artiklaProps = new String[] {"Naziv artikla", "", "1800", "100", 
     "3940", "240", "Normal", "-4275514", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", 
     "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "420", "100", "1360", "240", 
     "Normal", "-4275514", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelRb;
  private String[] LabelRbProps = new String[] {"Rb", "", "", "100", "400", "240", "Normal", 
     "-4275514", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "6260", "100", "1240", 
     "240", "Normal", "-4275514", "", "", "", "", "Lucida Bright", "9", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "360", "10800", "0", "", "", ""};

  public raSection_Header1NarPop(raReportTemplate owner) {
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
    LabelIznos = addModel(ep.LABEL, LabelIznosProps);
    LabelJm = addModel(ep.LABEL, LabelJmProps);
    Label_pop = addModel(ep.LABEL, Label_popProps);
    LabelCijena = addModel(ep.LABEL, LabelCijenaProps);
    LabelNaziv_artikla = addModel(ep.LABEL, LabelNaziv_artiklaProps);
    LabelSifra = addModel(ep.LABEL, LabelSifraProps);
    LabelRb = addModel(ep.LABEL, LabelRbProps);
    LabelKolicina = addModel(ep.LABEL, LabelKolicinaProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
