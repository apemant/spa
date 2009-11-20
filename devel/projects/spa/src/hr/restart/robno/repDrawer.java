package hr.restart.robno;

import java.util.StringTokenizer;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxRM;
import hr.restart.util.reports.mxReport;


public class repDrawer extends mxReport {

  dM dm = dM.getDataModule();
  lookupData lD =  lookupData.getlookupData();

  public void makeReport() {
    String mx = frmParam.getParam("sisfun", "printerRMcmnd", "1", "Radno mjesto", true);
    lD.raLocate(dm.getMxPrinterRM(), "CRM", mx);
    mxRM rm = new mxRM();
    rm.init(dm.getMxPrinterRM());
    setRM(rm);
      try {
        String str = frmParam.getParam("pos", "openDrawer", 
            "\\u0007", "Sekvenca za otvaranje ladice", true);
        StringTokenizer tok = new StringTokenizer(str,"\\u");
        char[] ret = new char[tok.countTokens()];
        int i=0;
        while (tok.hasMoreTokens()) {
          ret[i] = (char)Integer.parseInt(tok.nextToken(), 16);
          i++;
        }
        send(new String(ret));
        
      } catch (NumberFormatException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }
  
  void send(String seq) {
    if (!openFile()) return;
    file.write(seq);
    try {
      file.close();
    } catch (Exception e) {
      System.out.println("Unable to close file "+e);
    }
  }
}
