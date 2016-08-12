package hr.restart.pl;

import hr.restart.robno.raDateUtil;
import hr.restart.robno.repMemo;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.reports.raReportData;

import com.borland.dx.dataset.DataSet;


public class repMPP1 implements raReportData {
  frmM4 fm4 = frmM4.getInstance();
  DataSet ds = fm4.getRep01Set();
  DataSet ps = fm4.getPodaciSet();

//  hr.restart.baza.dM dm= hr.restart.baza.dM.getDataModule();
  raDateUtil rdu = raDateUtil.getraDateUtil();
  Valid vl = Valid.getValid();

//  repUtil ru = repUtil.getrepUtil();
//  hr.restart.util.Util ut =  hr.restart.util.Util.getUtil();
  repMemo rm = repMemo.getrepMemo();
  
  
  public repMPP1() {
    // TODO Auto-generated constructor stub
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.rowCount();
  }

  public void close() {
    ds = null;
  }
  
  private String lead(String str, int chars) {
    if (str.length() < chars) return Aus.spc(chars - str.length()) + str;
    return str;
  }
  
  private String leadNum(String col, int chars) {
    return lead(Integer.toString(ds.getBigDecimal(col).movePointRight(2).intValue()), chars);
  }
  
  private String trail(String str, int chars) {
    if (str.length() < chars) return str + Aus.spc(chars - str.length());
    return str;
  }
  
  public String getMPGR() {
    return String.valueOf(ds.getRow());
  }
  
  public String getGOD() {
    return lead(String.valueOf(fm4.getGodina()), 4);
  }
  
  public String getOIB() {
    return trail(ds.getString("OIB"), 11);
  }
  
  public String getOSOB() {
    return trail(ds.getString("BRRADKNJ"), 11);
  }
  
  public String getJMBG() {
    return trail(ds.getString("JMBG"), 13);
  }
  
  public String getVRSTA() {
    return trail(fm4.getVrsta(), 2);
  }
  
  public String getUJED() {
    return "    ";
  }
  
  public String getREGBR() {
    return trail(fm4.getRBMIO(), 10);
  }
  
  public String getOIBPOSL() {
    return trail(rm.getLogoOIB(), 11);
  }
  
  public String getMATBROJ(){
    return trail(ps.getString("MATBROJ"), 8);
  }

  public String getMBPS(){
    return trail(ps.getString("SIFDJEL"), 4);
  }
  
  public String getNAZPOSL() {
    return rm.getOneLineNoOIB().toUpperCase();
  }
  
  public String getPREZ() {
    return trail(ds.getString("PREZIME").toUpperCase(), 19);
  }
  
  public String getIME() {
    return trail(ds.getString("IME").toUpperCase(), 19);
  }
  
  public String getOSMJD() {
    return lead(""+ds.getInt("MJ"),2) + "00".concat(ds.getString("DANA")).substring(ds.getString("DANA").length());
  }
  
  public String getOSNOVICA() {
    return leadNum("BRUTO", 10);
  }
  
  public String getSATIR() {
    return "    ";
  }
  
  public String getSATIB() {
    return "    ";
  }
  
  public String getIZNOS1() {
    if (ds.getBigDecimal("BRUTO1").signum() == 0) return lead("", 10); 
    return leadNum("BRUTO1", 10);
  }
  
  public String getIZNOS2() {
    if (ds.getBigDecimal("BRUTO2").signum() == 0) return lead("", 10);
    return leadNum("BRUTO2", 10);
  }
  
  public String getIZNOS3() {
    if (ds.getBigDecimal("BRUTO3").signum() == 0) return lead("", 10);
    return leadNum("BRUTO3", 10);
  }
  
  public String getMJ() {
    return ps.getString("MJESTO");
  }
  
  public String getDATISP() {
    return Aus.formatTimestamp(vl.getToday());
  }
  
  public String getT1MJD() {
    return "    ";
  }
  
  public String getT2MJD() {
    return "    ";
  }
  
  public String getT3MJD() {
    return "    ";
  }
  
  public String getT4MJD() {
    return "    ";
  }
  
  public String getCST1() {
    return "    ";
  }
  
  public String getCST2() {
    return "    ";
  }
  
  public String getCST3() {
    return "    ";
  }
  
  public String getCST4() {
    return "    ";
  }
  
  public String getOD1DM() {
    return "    ";
  }
  
  public String getOD2DM() {
    return "    ";
  }
  
  public String getOD3DM() {
    return "    ";
  }
  
  public String getOD4DM() {
    return "    ";
  }
  
  public String getDO1DM() {
    return "    ";
  }
  
  public String getDO2DM() {
    return "    ";
  }
  
  public String getDO3DM() {
    return "    ";
  }
  
  public String getDO4DM() {
    return "    ";
  }
  
  public String getDATUM() {
    return "        ";
  }
  
  public String getNASLOV() {
    return "PRIJAVA - PROMJENA PODATAKA O\nUTVRÐENOM OSIGURANJU I OSNOVICI";
  }
  
  public String getSTAZCAP() {
    return "UTVRÐENI STAŽ OSIGURANJA";
  }
}
