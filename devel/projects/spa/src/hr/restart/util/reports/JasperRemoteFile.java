package hr.restart.util.reports;

import java.io.Serializable;

import net.sf.jasperreports.engine.JasperPrint;

public abstract class JasperRemoteFile implements Serializable {

  JasperPrint jasperPrint;
  
  public JasperRemoteFile(JasperPrint jasPrint) {
    jasperPrint = jasPrint;
  }
  
  public JasperPrint getJasperPrint() {
    return jasperPrint;
  }
  
  public void setJasperPrint(JasperPrint jasPrint) {
    this.jasperPrint = jasPrint;
  }
}
