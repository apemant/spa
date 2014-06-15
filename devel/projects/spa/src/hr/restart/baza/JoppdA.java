package hr.restart.baza;

import com.borland.dx.sql.dataset.QueryDataSet;

public class JoppdA extends KreirDrop {

  private static JoppdA inst = new JoppdA();
  
  
  public static JoppdA getDataModule() {
    return inst;
  }


  public boolean isAutoRefresh() {
    return true;
  }
}
