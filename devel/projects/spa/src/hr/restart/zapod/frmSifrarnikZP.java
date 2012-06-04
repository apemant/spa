package hr.restart.zapod;

import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Query;
import hr.restart.baza.Sifrarnici;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raSifraNaziv;

public class frmSifrarnikZP extends raSifraNaziv {
  private String vrs;
  protected QueryDataSet sifraSet;
  private JraTextField jtfInvisibleVRSIF;
  
  protected frmSifrarnikZP() {}
  
  public frmSifrarnikZP(String vrstasif) {
    try {
      vrs = vrstasif;
      jInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public QueryDataSet getSifraSet() {
    return sifraSet;
  }
  
  private void jInit() {
    jtfInvisibleVRSIF = new JraTextField();
    //ili (QueryDataSet)hr.restart.zapod.Sifrarnici.getSifre(vrs) ?
    sifraSet = Sifrarnici.getDataModule().getFilteredDataSet(Condition.equal("VRSTASIF", vrs));
    sifraSet.getColumn("VRSTASIF").setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    beforeOpenSifraSet();
    sifraSet.open();
    this.setRaDataSet(sifraSet);
    this.setRaColumnSifra("CSIF");
    this.setRaColumnNaziv("NAZIV");
    this.setRaText("Šifra");
    jtfInvisibleVRSIF.setDataSet(sifraSet);
    jtfInvisibleVRSIF.setColumnName("VRSTASIF");
  }

  protected void beforeOpenSifraSet() {
    
  }

  public boolean Validacija(char mode) {
    if (mode == 'N') {
      getRaDataSet().setString("VRSTASIF", vrs);
      getRaDataSet().setString("CSIFPRIP", getRaDataSet().getString("CSIF"));
      jtfInvisibleVRSIF.setText(vrs);
      if (Valid.getValid().notUnique(new JTextComponent[] {jtfCSIFRA, jtfInvisibleVRSIF})) {
        return false;
      }
    }
    if (hr.restart.util.Valid.getValid().isEmpty(jtfNAZIV)) return false;
    getRaDataSet().setString("OPIS", getRaDataSet().getString("NAZIV"));
    return Validacija2(mode);
  }
}
