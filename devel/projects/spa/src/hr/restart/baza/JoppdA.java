package hr.restart.baza;

import com.borland.dx.sql.dataset.QueryDataSet;

public class JoppdA extends KreirDrop {

  private static JoppdA joppdaclass;
  
  QueryDataSet joppda = new raDataSet();
  
  public static JoppdA getDataModule() {
    if (joppdaclass == null) {
      joppdaclass = new JoppdA();
    }
    return joppdaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return joppda;
  }

  public JoppdA() {
    try {
      modules.put(this.getClass().getName(), this);
      initModule();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

}
