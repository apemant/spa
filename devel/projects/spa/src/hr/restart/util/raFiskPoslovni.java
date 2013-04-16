package hr.restart.util;

import java.sql.Timestamp;

import hr.apis_it.fin._2012.types.f73.PoslovniProstorType;
import hr.apis_it.fin._2012.types.f73.PoslovniProstorZahtjev;
import hr.restart.baza.dM;
import hr.restart.pos.presBlag;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raFiskPoslovni extends raUpitLite {
  
  dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  lookupData ld = lookupData.getlookupData();

  JPanel jp = new JPanel();
  //raCommonClass rcc = new raCommonClass();
  XYLayout lay = new XYLayout();
  //JLabel jlCORG = new JLabel();
  JlrNavField jtCORG = new JlrNavField() {
    public void after_lookUp() {
      jtCORG_after_lookUp();
    }
  };
  JraButton jbGetKnjig = new JraButton();
  
  JraTextField jraOznaka = new JraTextField();
  JraTextField jraOIB = new JraTextField();
  JraTextField jraUlica = new JraTextField();
  JraTextField jraBroj = new JraTextField();
  JraTextField jraBrojDod = new JraTextField();
  JlrNavField jlrPBR = new JlrNavField();
  JlrNavField jlrMJ = new JlrNavField();
  JraButton jbGetMj = new JraButton();
  JraTextField jraOpcina = new JraTextField();
  JraTextField jraOstalo = new JraTextField();
  JraTextField jraRadVri = new JraTextField();
  JraTextField jraDatum = new JraTextField();
  JraCheckBox jcbZatvori = new JraCheckBox();
  
  StorageDataSet ds = new StorageDataSet();
  
  
  public raFiskPoslovni() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  
  public void componentShow() {
    jtCORG.requestFocusLater();
    ds.setString("OZNAKA", "");
    //ds.setString("OZNAKA", presBlag.getFiskPP());
  }
  
  //createPoslovniProstor("53204444499", "PP1", "Medjimurska", "21", null, 
  //  "10000", "Zagreb", null, null, "Pon-Pet 09:00-17:00", new Timestamp(System.currentTimeMillis()),false)
  
  
  private void jbInit() throws Exception {
    ds.setColumns(new Column[] {
        dM.createStringColumn("CORG", "Org. jedinica", 12),
        dM.createStringColumn("OZNAKA", "Oznaka", 20),
        dM.createStringColumn("OIB", "OIB", 20),
        dM.createStringColumn("ULICA", "Ulica", 100),
        dM.createStringColumn("BROJ", "Broj", 10),
        dM.createStringColumn("BROJDOD", "Dodatak", 30),
        dM.createIntColumn("PBR", "Poštanski broj"),
        dM.createStringColumn("MJ", "Mjesto", 50),
        dM.createStringColumn("OPCINA", "Opæina", 50),
        dM.createStringColumn("OSTALO", "Ostalo", 50),
        dM.createStringColumn("RADVRI", "Radno vrijeme", 100),
        dM.createTimestampColumn("DATUM", "Datum poèetka primjene")
    });
    ds.open();
    ds.insertRow(false);
    
    //jlCORG.setText("Org. jedinica");
    jtCORG.setColumnName("CORG");
    jtCORG.setDataSet(ds);
    jtCORG.setRaDataSet(dm.getOrgstruktura());
    jtCORG.setVisCols(new int[] {0,1});
    jtCORG.setSearchMode(0);
    jtCORG.setNavButton(jbGetKnjig);
    
    jraOznaka.setDataSet(ds);
    jraOznaka.setColumnName("OZNAKA");
    
    jraOIB.setDataSet(ds);
    jraOIB.setColumnName("OIB");
    
    jraUlica.setDataSet(ds);
    jraUlica.setColumnName("ULICA");
    
    jraBroj.setDataSet(ds);
    jraBroj.setColumnName("BROJ");
    
    jraBrojDod.setDataSet(ds);
    jraBrojDod.setColumnName("BROJDOD");
    
    jraOpcina.setDataSet(ds);
    jraOpcina.setColumnName("OPCINA");
    
    jraOstalo.setDataSet(ds);
    jraOstalo.setColumnName("OSTALO");
    
    jraRadVri.setDataSet(ds);
    jraRadVri.setColumnName("RADVRI");
    
    jraDatum.setDataSet(ds);
    jraDatum.setColumnName("DATUM");

    jlrPBR.setSearchMode(-1);
    jlrPBR.setColumnName("PBR");
    jlrPBR.setDataSet(ds);
    jlrPBR.setColNames(new String[] {"NAZMJESTA"});
    jlrPBR.setTextFields(new javax.swing.text.JTextComponent[] {jlrMJ});
    jlrPBR.setVisCols(new int[] {0, 1, 2});
    jlrPBR.setRaDataSet(dm.getMjesta());
    jlrPBR.setNavButton(jbGetMj);
    jlrPBR.setFocusLostOnShow(false);
    jlrPBR.setAfterLookUpOnClear(false);
    jlrPBR.setSearchMode(1);

    jlrMJ.setSearchMode(-1);
    jlrMJ.setColumnName("MJ");
    jlrMJ.setNavProperties(jlrPBR);
    jlrMJ.setNavColumnName("NAZMJESTA");
    jlrMJ.setDataSet(ds);
    jlrMJ.setFocusLostOnShow(false);
    jlrMJ.setAfterLookUpOnClear(false);
    jlrMJ.setSearchMode(1);
    
    jcbZatvori.setText(" Zatvori poslovni prostor ");
    jcbZatvori.setHorizontalAlignment(JLabel.TRAILING);
    jcbZatvori.setHorizontalTextPosition(JLabel.LEADING);
    
    lay.setWidth(560);
    lay.setHeight(200);
    this.setJPan(jp);
    jp.setLayout(lay);
    
    jp.add(new JLabel("Org. jedinica"), new XYConstraints(15, 20, -1, -1));
    jp.add(jtCORG, new XYConstraints(150, 20, 100, -1));
    jp.add(jbGetKnjig, new XYConstraints(255, 20, 21, 21));
    
    jp.add(new JLabel("Oznaka / OIB"), new XYConstraints(15, 45, -1, -1));
    jp.add(jraOznaka, new XYConstraints(150, 45, 100, -1));
    jp.add(jraOIB, new XYConstraints(255, 45, 145, -1));
    
    jp.add(new JLabel("Ulica i broj"), new XYConstraints(15, 70, -1, -1));
    jp.add(jraUlica, new XYConstraints(150, 70, 250, -1));
    jp.add(jraBroj, new XYConstraints(405, 70, 50, -1));
    jp.add(jraBrojDod, new XYConstraints(460, 70, 50, -1));
    
    jp.add(new JLabel("Mjesto i opæina"), new XYConstraints(15, 95, -1, -1));
    jp.add(jlrPBR, new XYConstraints(150, 95, 100, -1));
    jp.add(jlrMJ, new XYConstraints(255, 95, 145, -1));
    jp.add(jraOpcina, new XYConstraints(405, 95, 105, -1));
    jp.add(jbGetMj, new XYConstraints(515, 95, 21, 21));
    
    jp.add(new JLabel("Radno vrijeme"), new XYConstraints(15, 120, -1, -1));
    jp.add(jraRadVri, new XYConstraints(150, 120, 360, -1));
    
    jp.add(new JLabel("Datum"), new XYConstraints(15, 145, -1, -1));
    jp.add(jraDatum, new XYConstraints(150, 145, 100, -1));
    
    jp.add(jcbZatvori, new XYConstraints(280, 145, 230, -1));
    
  }
  

  public void firstESC() {
    /*jtCORG.setText("");
    jraOznaka.setText("");
    jraOIB.setText("");
    jraUlica.setText("");
    jraBroj.setText("");
    jraBrojDod.setText("");
    jlrPBR.setText("");
    jlrMJ.setText("");
    jraOpcina.setText("");
    jraOstalo.setText("");
    jraRadVri.setText("");
    jraDatum.setText("");*/
    ds.deleteAllRows();
    ds.insertRow(false);
  }
  
  public boolean Validacija() {
    if (vl.isEmpty(jraOznaka) || vl.isEmpty(jraOIB) || vl.isEmpty(jraUlica) || 
        vl.isEmpty(jraBroj) || vl.isEmpty(jlrPBR) || vl.isEmpty(jlrMJ) || 
        vl.isEmpty(jraRadVri) || vl.isEmpty(jraDatum)) return false;
    
    boolean zat = jcbZatvori.isSelected();
    if (JOptionPane.showConfirmDialog(jp, "Želite li " + (zat ? "ZATVORITI" : "prijaviti") + " poslovni prostor " + jraOznaka.getText() + "?", 
          "Prijava PP", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return false;
    
    return true;
  }
  
  //public PoslovniProstorType createPoslovniProstor(String oib, String oznPoslProstora, String ulica, String kucnibroj, 
  //    String kucnibrojdodatak, String brojposte, String naselje, String opcina, String ostaliTipPP, 
  //    String radnoVrijeme, Timestamp datumPocetkaPrimjene, boolean zatvaranje) {


  boolean isOk = false;
  public void okPress() {    
    isOk = false;
    try {
      isOk = presBlag.getFis("RAC", jtCORG.getText()).fiskaliziraj(
          presBlag.getFis("RAC", jtCORG.getText()).createPoslovniProstorZahtjev(
              presBlag.getFis("RAC", jtCORG.getText()).createZaglavlje(ds.getTimestamp("DATUM"), null),
              presBlag.getFis("RAC", jtCORG.getText()).createPoslovniProstor(
                  ds.getString("OIB"), ds.getString("OZNAKA"),
                  ds.getString("ULICA"), ds.getString("BROJ"),
                  isNull("BROJDOD"), jlrPBR.getText(), 
                  ds.getString("MJ"), isNull("OPCINA"), isNull("OSTALO"),
                  ds.getString("RADVRI"), ds.getTimestamp("DATUM"), 
                  jcbZatvori.isSelected()
              )));
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      isOk = false;
    }
    
  }
  
  public void afterOKPress() {
    if (isOk) {
      JOptionPane.showMessageDialog(jp, "Prijava poslovnog prostora upješna!", "Fiskalizacija", JOptionPane.INFORMATION_MESSAGE);
      /*if (!presBlag.isSkladOriented()) {
        if (lookupData.getlookupData().raLocate(dm.getParametri(),
            new String[] {"APP","PARAM"},new String[] {"robno", "fiskPP"})) {
          if (dm.getParametri().getString("VRIJEDNOST").length() == 0) {
            dm.getParametri().setString("VRIJEDNOST", ds.getString("OZNAKA"));
            dm.getParametri().saveChanges();
          }
        }
        IntParam.setTag("robno.fiskPP", ds.getString("OZNAKA"));
      }*/
    } else {
      JOptionPane.showMessageDialog(jp, "Prijava poslovnog prostora NIJE upješna!", "Fiskalizacija", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  String isNull(String col) {
    if (ds.getString(col).length() == 0) return null;
    return ds.getString(col);
  }

  public boolean runFirstESC() {
    return jtCORG.getText().length() > 0;
  }
  
  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return false;
  }
  
  public void jtCORG_after_lookUp() {
    if (jraOznaka.getText().length() > 0) return;
    ds.setString("OZNAKA", presBlag.findOJ("RAC", jtCORG.getText()).getString("FPP"));
    if (ld.raLocate(dm.getLogotipovi(), "CORG", jtCORG.getText())) {
      ds.setString("OIB", dm.getLogotipovi().getString("OIB"));
      ds.setInt("PBR", dm.getLogotipovi().getInt("PBR"));
      ds.setString("MJ", dm.getLogotipovi().getString("MJESTO"));
      String adr = dm.getLogotipovi().getString("ADRESA");
      String[] parts = new VarStr(adr).split();
      if (Aus.isDigit(parts[parts.length - 1])) {
        ds.setString("ULICA", new VarStr(adr).chop(parts[parts.length - 1].length() + 1).toString());
        ds.setString("BROJ", parts[parts.length - 1]);
      } else ds.setString("ULICA", adr);
    } 
    /*if (ld.raLocate(dm.getOrgstruktura(), "CORG", jtCORG.getText())) {
      ds.setString("OZNAKA", dm.getOrgstruktura().getString("))
    }*/
  }
}
