package hr.restart.util.reports;

public class raRacGetroSF1 extends raReportSection {

  private String[] thisProps = new String[] {"BRDOK", "", "", "", "Yes", "No", "", "Yes", "720"};
  public raReportElement Line1;
  private String[] Line1Props = new String[] {"", "", "", "60", "10820", "0", "", "", ""};
  public raReportElement LabelKolicina;
  private String[] LabelKolicinaProps = new String[] {"Koli\u010Dina", "", "6880", "100", "920", 
     "280", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelAkcija;
  private String[] LabelAkcijaProps = new String[] {"Akcija", "", "10240", "100", "580", "540", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelKolicina1;
  private String[] LabelKolicina1Props = new String[] {"Koli\u010Dina", "", "6880", "100", "920", 
     "280", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelRbr;
  private String[] LabelRbrProps = new String[] {"Rbr", "", "", "100", "380", "540", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelEAN_kod_art;
  private String[] LabelEAN_kod_artProps = new String[] {"EAN kod art.", "", "1800", "100", "1280", 
     "280", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelJmj;
  private String[] LabelJmjProps = new String[] {"Jmj", "", "6120", "100", "740", "280", "Normal", 
     "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelGETRO_Sifra;
  private String[] LabelGETRO_SifraProps = new String[] {"GETRO\näifra", "", "400", "100", "760", 
     "540", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelRabat_;
  private String[] LabelRabat_Props = new String[] {"Rabat (%)", "", "8600", "100", "800", "280", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelOpis_artikla;
  private String[] LabelOpis_artiklaProps = new String[] {"Opis artikla", "", "3100", "100", "3000", 
     "540", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelPov_nak;
  private String[] LabelPov_nakProps = new String[] {"Pov. nak.", "", "9420", "100", "800", "280", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelPDV_;
  private String[] LabelPDV_Props = new String[] {"PDV (%)", "", "7820", "100", "760", "280", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelMZ_Sifra;
  private String[] LabelMZ_SifraProps = new String[] {"MZ\näifra", "", "1180", "100", "600", "540", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelEAN_kod_pak;
  private String[] LabelEAN_kod_pakProps = new String[] {"EAN kod pak.", "", "1800", "360", "1280", 
     "280", "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", 
     "Center"};
  public raReportElement LabelPot_nak;
  private String[] LabelPot_nakProps = new String[] {"Pot. nak.", "", "9420", "360", "800", "280", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIznos;
  private String[] LabelIznosProps = new String[] {"Iznos", "", "8600", "380", "800", "260", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIznos1;
  private String[] LabelIznos1Props = new String[] {"Iznos", "", "7820", "380", "760", "260", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelIznos2;
  private String[] LabelIznos2Props = new String[] {"Iznos", "", "6880", "380", "920", "260", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement LabelCijena;
  private String[] LabelCijenaProps = new String[] {"Cijena", "", "6120", "380", "740", "260", 
     "Normal", "Gray", "Solid", "Gray", "", "", "Lucida Bright", "8", "Bold", "", "", "Center"};
  public raReportElement Line2;
  private String[] Line2Props = new String[] {"", "", "", "660", "10820", "0", "", "", ""};

  public raRacGetroSF1(raReportTemplate owner) {
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
    LabelAkcija = addModel(ep.LABEL, LabelAkcijaProps);
    LabelKolicina1 = addModel(ep.LABEL, LabelKolicina1Props);
    LabelRbr = addModel(ep.LABEL, LabelRbrProps);
    LabelEAN_kod_art = addModel(ep.LABEL, LabelEAN_kod_artProps);
    LabelJmj = addModel(ep.LABEL, LabelJmjProps);
    LabelGETRO_Sifra = addModel(ep.LABEL, LabelGETRO_SifraProps);
    LabelRabat_ = addModel(ep.LABEL, LabelRabat_Props);
    LabelOpis_artikla = addModel(ep.LABEL, LabelOpis_artiklaProps);
    LabelPov_nak = addModel(ep.LABEL, LabelPov_nakProps);
    LabelPDV_ = addModel(ep.LABEL, LabelPDV_Props);
    LabelMZ_Sifra = addModel(ep.LABEL, LabelMZ_SifraProps);
    LabelEAN_kod_pak = addModel(ep.LABEL, LabelEAN_kod_pakProps);
    LabelPot_nak = addModel(ep.LABEL, LabelPot_nakProps);
    LabelIznos = addModel(ep.LABEL, LabelIznosProps);
    LabelIznos1 = addModel(ep.LABEL, LabelIznos1Props);
    LabelIznos2 = addModel(ep.LABEL, LabelIznos2Props);
    LabelCijena = addModel(ep.LABEL, LabelCijenaProps);
    Line2 = addModel(ep.LINE, Line2Props);
  }

  private void modifyThis() {
  }
}
