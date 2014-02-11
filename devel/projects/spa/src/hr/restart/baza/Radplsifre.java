package hr.restart.baza;

import com.borland.dx.sql.dataset.QueryDataSet;

public class Radplsifre extends KreirDrop {

  private static Radplsifre radplsifreclass;
  
  QueryDataSet radplsifre = new raDataSet();
  
  public static Radplsifre getDataModule() {
    if (radplsifreclass == null) {
      radplsifreclass = new Radplsifre();
    }
    return radplsifreclass;
  }

  public QueryDataSet getQueryDataSet() {
    return radplsifre;
  }

  public Radplsifre() {
    try {
      modules.put(this.getClass().getName(), this);
      initModule();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

}
