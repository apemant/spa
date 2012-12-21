package hr.restart.robno;

import com.borland.dx.dataset.DataSet;

import hr.restart.pos.frmMasterBlagajna;
import hr.restart.util.Aus;
import hr.restart.util.reports.mxRM;


public class repRacPOS extends repRacunPOS {
  public void setData() {
    master = frmMasterBlagajna.getAlterMaster();
    god =master.getString("GOD");
//    hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
//    st.prn(master);
    this.setDataSet(frmMasterBlagajna.getAlterDetail());
    
    String vc = frmMasterBlagajna.getRacDestination();
    if (vc == null || vc.length() == 0) {
      frmMasterBlagajna.setRacDestination();
      vc = frmMasterBlagajna.getRacDestination();
    }
    lD.raLocate(dm.getMxPrinterRM(), "CRM", vc);
    mxRM rm = new mxRM();
    rm.init(dm.getMxPrinterRM());
    setRM(rm);
  }
  
  protected String getStol() {
    try {
      DataSet ds = Aus.q("SELECT * FROM places WHERE id='" +
          master.getString("STOL")+"'");
      if (ds.rowCount() > 0)
        return ds.getString("NAME");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return master.getString("STOL");
  }
}
