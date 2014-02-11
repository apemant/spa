package hr.restart.baza;

import com.borland.dx.sql.dataset.QueryDataSet;

public class JoppdB extends KreirDrop {

  private static JoppdB joppdbclass;
  
  QueryDataSet joppdb = new raDataSet();
  
  public static JoppdB getDataModule() {
    if (joppdbclass == null) {
      joppdbclass = new JoppdB();
    }
    return joppdbclass;
  }

  public QueryDataSet getQueryDataSet() {
    return joppdb;
  }

  public JoppdB() {
    try {
      modules.put(this.getClass().getName(), this);
      initModule();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

}
