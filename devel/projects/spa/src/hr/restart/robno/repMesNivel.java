package hr.restart.robno;

import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;


public class repMesNivel implements raReportData {
  _Main main;
  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  repMemo rpm = repMemo.getrepMemo();
  DataSet ds; // = reportsQuerysCollector.getRQCModule().getQueryDataSet();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  String[] colname = new String[] {""};
  repUtil ru = repUtil.getrepUtil();
  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  
  private static boolean isMC;
  
  public repMesNivel() {
    ds = reportsQuerysCollector.getRQCModule().getQueryDataSet();
    lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", ds.getString("CSKLUL"));
    if (dm.getSklad().getString("VRZAL").equals("M")) isMC = true;
    else isMC = false;
  }
  
  public raReportData getRow(int i) {
    ds.goToRow(i);    
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ru.setDataSet(null);
    ds = null;
  }
  
  public String getCSKL() {
    return ds.getString("CSKLUL");
  }
  public String getNAZSKL(){
    lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", ds.getString("CSKLUL"));
    return dm.getSklad().getString("NAZSKL");
  }
  
  public int getBRDOK(){
    return ds.getInt("BRDOK");
  }
  
  public String getDATDOK() {
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }
  
  public short getRBR() {
    return ds.getShort("RBR");
  }
  public String getCART() {
    return Aut.getAut().getCARTdependable(ds);
  }
  public String getNAZART() {
    return ds.getString("NAZART");
  }
  
  public String getJM() {
    return ds.getString("JM");
  }

  public BigDecimal getKOL() {
    return ds.getBigDecimal("SKOL");
  }
  
  public String getFormatBroj(){
    ru.setDataSet(ds);
    return ru.getFormatBrojME();
  }
  public String getFirstLine(){
    return rpm.getFirstLine();
  }
  
  public String getSecondLine(){
    return rpm.getSecondLine();
  }
  public String getThirdLine(){
    return rpm.getThirdLine();
  }

  public String getDatumIsp(){
    return rdu.dataFormatter(val.getToday());
  }
  
  /// NIVELACIJA - PORAVNANJE

  public BigDecimal getSTARACIJENA(){
    if (isMC) return ds.getBigDecimal("SMC");
    return ds.getBigDecimal("SVC");
  }

  public BigDecimal getNOVACIJENA(){
    if (isMC) return ds.getBigDecimal("MC");
    return ds.getBigDecimal("VC");
  }

  public double getPORAV(){
//    return ((getSTARACIJENA().subtract(getNOVACIJENA())).multiply(getKOL())).doubleValue();
     return ds.getBigDecimal("PORAV").doubleValue();
  }
}
