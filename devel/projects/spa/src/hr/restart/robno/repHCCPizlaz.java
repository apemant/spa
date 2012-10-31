package hr.restart.robno;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;


public class repHCCPizlaz implements raReportData {

  DataSet ds;
  lookupData ld = lookupData.getlookupData();
  
  
  public repHCCPizlaz() {
    ds = frmIzvHCCPu.getInstance().getTds();
    ds.open();
  }
  
  public void close() {
    ds = null;
  }

  public raReportData getRow(int i) {
    ds.goToRow(i);
    return this;
  }

  public int getRowCount() {
    return ds.getRowCount();
  }
  
  public String getSECT() {
    return "1";
  }

  public String getKNJIG() {
    return "\"MOJA ZEMLJA\" d.o.o.\n Zagreb, Hondlova 2 lokacija:" +
            "\nZagreb, II Resnièki gaj b.b.";
  }
  
  public String getOBRAZ() {
    return "Obrazac br. 18";
  }
  
  public String getNASLOV() {
    return "Predpristupne mjere";
  }
  
  public String getPODNASLOV() {
    return "Kontrola dobavljaèa i kupaca";
  }
  
  public String getODGOVORAN() {
    return "Odgovorna osoba: poslovoða";
  }
  
  public String getADR() {
    if (ds.getInt("PJ")>0)
      return ds.getString("NAZPJ");
    return "";
  }
  
  public int getRBR() {
    return ds.getRow() + 1;
  }
  
  public String getDATDOK() {
    return Aus.formatTimestamp(ds.getTimestamp("DATDOK"));
  }
  
  public String getNAZART() {
    return ds.getString("NAZART");
  }
  
  public BigDecimal getKOL() {
    return ds.getBigDecimal("KOL");
  }
  
  public String getNAZPAR() {
    return ds.getString("NAZPAR");
  }
  
  public String getIZGLED() {
    return "";
  }
  
  public String getBRDOK() {
    return Integer.toString(ds.getInt("BRDOK"));
  }
  
  public String getLOT() {
    return ds.getString("LOT");
  }
  
  public String getTEMP() {
    return "";
  }
  
  public String getREG() {
    return ds.getString("CREG");
  }
}
