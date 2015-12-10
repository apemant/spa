package hr.restart.robno;

import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;

import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;


public class repPartNal implements raReportData {


  DataSet ds = raPartNal.getInstance().getQds();

  public repPartNal() {

  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.getRowCount();
  }

  public void close() {
    ds = null;
  }
  
  public String getNAZPARL() {
    return ds.getString("NAZPAR");
  }
  
  public String getADR() {
    return ds.getString("ADR");
  }
  
  public String getMJ() {
    if (ds.getInt("PBR") <= 0) return ds.getString("MJ"); 
    return ds.getInt("PBR") + " " + ds.getString("MJ");
  }
}
