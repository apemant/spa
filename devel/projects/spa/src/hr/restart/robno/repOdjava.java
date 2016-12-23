package hr.restart.robno;


import hr.restart.baza.Partneri;
import hr.restart.util.Aus;

public class repOdjava extends repIzlazni {

  public String getTitle() {
    return "KOMISIJSKA ODJAVA";
  }
  
  public String getSubTitle() {
    return "zakljuèno do dana " + Aus.formatTimestamp(ds.getTimestamp("DVO"));
  }
  
  public String getNAZPARALL() {
    String cached = cache.getValue("NAZPARALL", "" + ds.getInt("CPAR"));
    if (cached != null) return cached;
    
    String np = "";
    
    if (Partneri.loc(ds)) {
      np = Partneri.get().getInt("CPAR") + "  " + Partneri.get().getString("NAZPAR");
      if (Partneri.get().getString("ADR").length() > 0) 
        np = np + ", " + Partneri.get().getString("ADR") + " " + Partneri.get().getString("MJ");
      if (Partneri.get().getString("OIB").length() > 0)
        np = np + ",  OIB " + Partneri.get().getString("OIB");
    }
        
    return cache.returnValue(np);
  }
   
}
