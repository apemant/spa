package hr.restart.pl;

import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;

public class repDiskRSm2010 extends repDiskRSm {
  public StringBuffer insertSlog0() {
    StringBuffer b = super.insertSlog0();
    b.replace(0, 12, "            ");
    String o = getOIB(repDiskRS.getOvlCLog());
    b.replace(12, 12+o.length(),o);
    return b;
  }
  public StringBuffer insertSlog3() {
    StringBuffer b = super.insertSlog3();
    String o = getOIB();
    b.replace(0, 12, "            ");
    b.replace(12, 12+o.length(), o);
    return b;
  }
  public StringBuffer insertSlog7() {
    StringBuffer b = super.insertSlog7();
    String o = getOIB();
    b.replace(0, 12, "            ");
    b.replace(12, 12+o.length(), o);
    return b;
  }
  public StringBuffer insertSlog9() {
    StringBuffer b = super.insertSlog9();
    String o = getOIB(repDiskRS.getOvlCLog());
    b.replace(0, 12, "            ");
    b.replace(12, 12+o.length(), o);
    return b;
  }
  public StringBuffer getSlog5(int rbr) {
    StringBuffer b= super.getSlog5(rbr);
    String oib = qds.getString("JMBG");
    String o = Valid.getValid().maskString(oib.trim(),' ',13);
    b.replace(5,18,o);
    return b;
  }
  public static String getOIB() {
    return getOIB(OrgStr.getKNJCORG());
  }
  public static String getOIB(String clog) {
    String cKnjig = clog;
    String oib = "";
    lookupData.getlookupData().raLocate(hr.restart.baza.dM.getDataModule().getLogotipovi(), new String[]{"CORG"}, new String[]{cKnjig});
    oib=hr.restart.baza.dM.getDataModule().getLogotipovi().getString("OIB");
    return Valid.getValid().maskString(oib.trim(),' ',13);  
  }
  
}
