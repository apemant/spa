package hr.restart.robno;


public class repFISBIHizvjX extends repFISBIH {

  public repFISBIHizvjX() {
    super("X");
  }
  public void init() {
    super.init();
    setPrint("izvjX.INP");
  }
  public void fill() throws Exception {
    filelines.emptyAllRows();
    filelines.insertRow(false);
    filelines.setString("LINE", "X,1,______,_,__;");
    filelines.post();
  }
}
