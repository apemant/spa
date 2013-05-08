package hr.restart.robno;

import com.borland.dx.sql.dataset.QueryDataSet;

public class repFISBIHRekRN extends repFISBIHRN {
  protected void handleResponse(String sLRN) {
    //don't care for response
  }
  public String getFBR() {
//    return Valid.getValid().maskZeroInteger(new Integer(repQC.caller.getMasterSet().getInt("FBR")), 6);
    return repQC.caller.getMasterSet().getInt("FBR")+"";
  }
//  public String getKolicina(QueryDataSet ds2) {
//    return "-"+super.getKolicina(ds2);
//  }
  public String getNacinPlacanja(QueryDataSet ds2) {
    return "";
  }
  public String get_I_line() {
    return "I,"+getLogickiBroj()+",______,_,__;0;"+getIznos(ds);
//    return "I,1,______,_,__;0;0.16";
  }
  public String get_G_line() {
    return "";
  }
}
