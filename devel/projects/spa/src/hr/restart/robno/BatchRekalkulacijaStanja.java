package hr.restart.robno;

import hr.restart.util.raProcess;

public class BatchRekalkulacijaStanja extends raRekalkulacijaStanja {
  public static void start(String _cskl, String _god) {
    final BatchRekalkulacijaStanja rrr = new BatchRekalkulacijaStanja();
    rrr.cskl = _cskl;
    rrr.god = _god;
    rrr.vrzal = "N";
    if (raProcess.isRunning()) {
      rrr.startUpdateStanje();
    } else {
      raProcess.runChild(new Runnable() {
        public void run() {
          rrr.startUpdateStanje();
        }
      });
    }
  }
  
  public void startUpdateStanje() {
    super.startUpdateStanje();
    try {         
      tmpStanje.saveChanges();
      System.out.println("Rekalkulacija stanja USPJELA!!");
    } catch (Exception ex) {
      ex.printStackTrace();
      System.out.println("Snimanje stanja neuspjesno!!");
    }        
  }
}
