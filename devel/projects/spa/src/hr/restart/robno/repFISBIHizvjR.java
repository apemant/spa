package hr.restart.robno;


import java.text.SimpleDateFormat;

public class repFISBIHizvjR extends repFISBIH {

  public repFISBIHizvjR() {
    super("R");
  }
  public void init() {
    super.init();
    setPrint("izvjR.INP");
  }
  public void fill() throws Exception {
    SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
    String f_od = df.format(FISBIHIzvjestaji.getOD());
    String f_do = df.format(FISBIHIzvjestaji.getDO());
    filelines.emptyAllRows();
    filelines.insertRow(false);
    filelines.setString("LINE", "R,1,______,_,__;6;"+f_od+";"+f_do+";");
    filelines.post();
  }
}
