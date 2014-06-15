package hr.restart.baza;

import com.borland.dx.sql.dataset.QueryDataSet;

public class JoppdB extends KreirDrop {

  private static JoppdB inst = new JoppdB();
  
  
  public static JoppdB getDataModule() {
    return inst;
  }


  public boolean isAutoRefresh() {
    return true;
  }
}
