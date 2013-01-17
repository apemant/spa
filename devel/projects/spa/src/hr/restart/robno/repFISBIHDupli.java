package hr.restart.robno;


public class repFISBIHDupli extends repFISBIHRekRN {
 
  public void init() {
    super.init();
    setPrint("kopija.INP");
  }
  public void fill() throws Exception {
    filelines.emptyAllRows();
    filelines.insertRow(false);
    filelines.setString("LINE", "D,"+getLogickiBroj()+",______,_,__;1;1;"+getFBR()+";");
    filelines.post();
  }
}
