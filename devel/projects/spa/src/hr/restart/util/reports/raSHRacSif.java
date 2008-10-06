package hr.restart.util.reports;

import hr.restart.robno.Aut;

public class raSHRacSif extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", "740"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "60", "10820", "0", "", "", ""};
  public raReportElement LabelIznos;
  private String[] LabelIznosProps = new String[] {"Iznos", "", "9600", "100", "1220", "460", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "6920", "100", "460", "460", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Label1;
  private String[] Label1Props = new String[] {"%", "", "8480", "100", "1100", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "100", "460", "460", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "6060", "100", "840", 
     "460", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelSifra;
  private String[] LabelSifraProps = new String[] {"Šifra", "", "480", "100", "800", "460", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelNaziv;
  private String[] LabelNazivProps = new String[] {"Naziv artikla", "", "2200", "100", "3840", "460", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelSifra_kupca;
  private String[] LabelSifra_kupcaProps = new String[] {"Šifra kupca", "", "1300", "100", "880", 
     "460", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "7400", "100", "1060", "460", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPor;
  private String[] LabelPorProps = new String[] {"Por", "", "9060", "340", "520", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPop;
  private String[] LabelPopProps = new String[] {"Pop", "", "8480", "340", "560", "220", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "580", "10820", "0", "", "", ""};

  public raSHRacSif(raReportTemplate owner) {
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
    LabelJmj = addModel(ep.LABEL, LabelJmjProps);
    Label1 = addModel(ep.LABEL, Label1Props);
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
    LabelKolicina = addModel(ep.LABEL, LabelKolicinaProps);
    LabelSifra = addModel(ep.LABEL, LabelSifraProps);
    LabelNaziv = addModel(ep.LABEL, LabelNazivProps);
    LabelSifra_kupca = addModel(ep.LABEL, LabelSifra_kupcaProps);
    LabelCijena = addModel(ep.LABEL, LabelCijenaProps);
    LabelPor = addModel(ep.LABEL, LabelPorProps);
    LabelPop = addModel(ep.LABEL, LabelPopProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
    this.restoreDefaults();
    this.LabelSifra.setCaption(Aut.getAut().getIzlazCARTdep("Šifra", "Oznaka", "Barcode"));
    this.resizeElement(this.LabelSifra, Aut.getAut().getIzlazCARTwidth(), this.LabelNaziv);
  }
}
