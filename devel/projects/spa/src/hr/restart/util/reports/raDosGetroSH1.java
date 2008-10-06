package hr.restart.util.reports;

public class raDosGetroSH1 extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", "760"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "60", "10820", "0", "", "", ""};
  public raReportElement LabelAkcija;
  private String[] LabelAkcijaProps = new String[] {"Akcija", "", "10140", "100", "680", "540", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelEAN_kod_art;
  private String[] LabelEAN_kod_artProps = new String[] {"EAN kod art.", "", "2060", "100", "1400", 
     "280", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "100", "420", "540", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelJed_mjere;
  private String[] LabelJed_mjereProps = new String[] {"Jed.\nmjere", "", "8320", "100", "760", 
     "540", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelGETRO_Sifra;
  private String[] LabelGETRO_SifraProps = new String[] {"GETRO\näifra", "", "440", "100", "860", 
     "540", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelOpis_artikla;
  private String[] LabelOpis_artiklaProps = new String[] {"Opis artikla", "", "3480", "100", "4820", 
     "540", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelMZ_Sifra;
  private String[] LabelMZ_SifraProps = new String[] {"MZ\näifra", "", "1320", "100", "720", "540", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "9100", "100", "1020", 
     "540", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelEAN_kod_pak;
  private String[] LabelEAN_kod_pakProps = new String[] {"EAN kod pak.", "", "2060", "360", "1400", 
     "280", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "660", "10820", "0", "", "", ""};

  public raDosGetroSH1(raReportTemplate owner) {
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
    LabelAkcija = addModel(ep.LABEL, LabelAkcijaProps);
    LabelEAN_kod_art = addModel(ep.LABEL, LabelEAN_kod_artProps);
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
    LabelJed_mjere = addModel(ep.LABEL, LabelJed_mjereProps);
    LabelGETRO_Sifra = addModel(ep.LABEL, LabelGETRO_SifraProps);
    LabelOpis_artikla = addModel(ep.LABEL, LabelOpis_artiklaProps);
    LabelMZ_Sifra = addModel(ep.LABEL, LabelMZ_SifraProps);
    LabelKolicina = addModel(ep.LABEL, LabelKolicinaProps);
    LabelEAN_kod_pak = addModel(ep.LABEL, LabelEAN_kod_pakProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
