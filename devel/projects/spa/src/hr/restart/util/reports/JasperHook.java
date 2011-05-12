package hr.restart.util.reports;

import net.sf.jasperreports.engine.design.JasperDesign;


public interface JasperHook {
  void adjustDesign(String reportName, JasperDesign design);
}
