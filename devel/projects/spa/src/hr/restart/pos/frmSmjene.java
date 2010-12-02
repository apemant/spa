package hr.restart.pos;

import hr.restart.baza.Smjene;
import hr.restart.util.raSifraNaziv;


public class frmSmjene extends raSifraNaziv {
  
  public frmSmjene() {
    try {
        jbInit();
    }
    catch(Exception e) {
        e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    this.setRaDataSet(Smjene.getDataModule().getFilteredDataSet(""));
    this.setRaColumnSifra("CSMJENA");
    this.setRaColumnNaziv("NAZIV");
    this.setRaText("Smjena");
  }
}
