package hr.restart.robno;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;

public interface WebSyncInterface {

  boolean isWeb(int cart);
  
  boolean isWeb(String cskl);
  
  void deletePartner(int part);
  
  void updatePartner(DataSet ds);
  
  void updateStanje(int cart, DataSet ms);
  
  void updateAllStanje();
  
  void updateStanje(String cart, int count);
  
  void updatePopust(String cpar, String cart, BigDecimal discount);
  
  void importDocs();
  
  void install(int delay);
}
