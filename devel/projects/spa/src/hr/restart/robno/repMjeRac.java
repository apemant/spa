package hr.restart.robno;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

import hr.restart.util.raLoader;
import hr.restart.util.reports.raReportData;


public class repMjeRac implements raReportData {

  private DataSet ds;
  
  public String getMJ() {
    return ds.getString("MJ");
  }
  
  public BigDecimal getIZNOSR() {
    return ds.getBigDecimal("IZNOSR");
  }
  
  public BigDecimal getIZNOSU() {
    return ds.getBigDecimal("IZNOSU");
  }
  
  public BigDecimal getSALDO() {
    return getIZNOSR().subtract(getIZNOSU());
  }
  
  public repMjeRac() {
    raRAC frm = (raRAC) raLoader.load("hr.restart.robno.raRAC");
    ds = frm.getMjeData();
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
}
