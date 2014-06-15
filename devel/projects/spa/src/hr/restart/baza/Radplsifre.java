package hr.restart.baza;

import com.borland.dx.sql.dataset.QueryDataSet;

public class Radplsifre extends KreirDrop {

  private static Radplsifre inst = new Radplsifre();
  
  
  public static Radplsifre getDataModule() {
    return inst;
  }


  public boolean isAutoRefresh() {
    return true;
  }
}
