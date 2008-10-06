package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.StorageDataSet;

public class repNaljepnice implements raReportData {
  private static StorageDataSet ds;
  public repNaljepnice() {
  }
  public static void clearDataSet() {
    ds = null;
  }
  public static StorageDataSet getDataSet() {
    if (ds == null) initds();
    return ds;
  }
  private static void initds() {
    ds = new StorageDataSet();
    ds.addColumn(dM.createStringColumn("TXT1",200));
    ds.addColumn(dM.createStringColumn("TXT2",200));
    ds.addColumn(dM.createStringColumn("TXT3",200));
    ds.addColumn(dM.createStringColumn("TXT4",200));
    ds.addColumn(dM.createStringColumn("TXT5",200));
    ds.open();
  }
  public String getTXT1() {
    return getDataSet().getString("TXT1");
  }
  public String getTXT2() {
    return getDataSet().getString("TXT2");
  }
  public String getTXT3() {
    return getDataSet().getString("TXT3");
  }
  public String getTXT4() {
    return getDataSet().getString("TXT4");
  }
  public String getTXT5() {
    return getDataSet().getString("TXT5");
  }

  public raReportData getRow(int i) {
    getDataSet().goToRow(i);
    return this;
  }

  public int getRowCount() {
    return getDataSet().rowCount();
  }

  public void close() {
//    ds = null;
  }
}
