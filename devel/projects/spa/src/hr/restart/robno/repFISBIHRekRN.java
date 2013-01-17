package hr.restart.robno;

import hr.restart.util.Valid;

import com.borland.dx.sql.dataset.QueryDataSet;

public class repFISBIHRekRN extends repFISBIHRN {
  protected void handleResponse(String sLRN) {
    //don't care for response
  }
  public String getFBR() {
    return Valid.getValid().maskZeroInteger(new Integer(repQC.caller.getMasterSet().getInt("FBR")), 6);
  }
  public String getKolicina(QueryDataSet ds2) {
    return "-"+super.getKolicina(ds2);
  }
}
