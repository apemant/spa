package hr.restart.util.reports;

import net.sf.jasperreports.engine.design.JasperDesign;
import sg.com.elixir.reportwriter.xml.IModel;


public class JasperBuilder {

  private JasperBuilder() {
  }

  public static JasperDesign buildFromElixir(IModel er, JasperElixirData data) {
    return new ElixirToJasperConverter(er, data).getJasperDesign();
  }
}
