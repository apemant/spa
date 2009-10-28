package hr.restart.robno;

import hr.restart.pos.frmMasterBlagajna;
import hr.restart.util.reports.mxRM;


public class repRacPOS extends repRacunPOS {
  public void setData() {
    master = frmMasterBlagajna.getAlterMaster();
    god =master.getString("GOD");
//    hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
//    st.prn(master);
    this.setDataSet(frmMasterBlagajna.getAlterDetail());
    
    String vc = frmMasterBlagajna.getRacDestination();
    lD.raLocate(dm.getMxPrinterRM(), "CRM", vc);
    mxRM rm = new mxRM();
    rm.init(dm.getMxPrinterRM());
    setRM(rm);
  }
}
