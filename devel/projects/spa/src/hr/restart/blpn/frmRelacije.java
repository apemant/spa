package hr.restart.blpn;

import hr.restart.util.VarStr;

import javax.swing.JOptionPane;

public class frmRelacije extends frmPSredstva {
  
  private static frmRelacije frinst;
  public static frmRelacije getFRelacije() {
    if (frinst == null) new frmRelacije();
    return frinst;
  }
  
  public frmRelacije() {
    super("BLRC");
    setRaText("Relacija");
    initValue();
    frinst = this;
  }
  
  public boolean Validacija2(char mode) {
    if (new VarStr(getRaDataSet().getString("NAZIV")).indexOf("--",1) < 0) {
      JOptionPane.showMessageDialog(this.getWindow(), "U naziv je potrebno unijeti dva mjesta odvojena '--' (duplim minusom)");
      return false;
    }
    return super.Validacija2(mode);
  }
  public String getValueText() {
    return "Udaljenost u km";
  }
  public String getPCaption() {
    return "Udaljenost";
  }
}
