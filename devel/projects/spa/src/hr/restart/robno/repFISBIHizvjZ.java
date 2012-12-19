package hr.restart.robno;


public class repFISBIHizvjZ extends repFISBIH {

  public repFISBIHizvjZ() {
    super("Z");
  }
  public void init() {
    super.init();
    setPrint("izvjZ.INP");
  }
  public void fill() throws Exception {
    filelines.emptyAllRows();
    filelines.insertRow(false);
    filelines.setString("LINE", "Z,1,______,_,__;");
    filelines.post();
  }
}
