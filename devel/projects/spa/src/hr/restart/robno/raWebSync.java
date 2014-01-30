package hr.restart.robno;

import java.math.BigDecimal;

import hr.restart.baza.dM;
import hr.restart.help.MsgDispatcher;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

import com.borland.dx.dataset.DataSet;

public class raWebSync {
  
  public static boolean active = frmParam.getParam("sisfun", "webSync", "N", "Web sinkronizacija (D,N)").equals("D");
  
  static WebSyncInterface impl;
  
  static {
  	if (active)
			try {
				impl = (WebSyncInterface) Class.forName("hr.restart.robno.WebSyncImpl_binomNew").newInstance();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				active = false;
				e.printStackTrace();
			}
    
  }
  
  public static boolean isWeb(int cart) {
    return impl.isWeb(cart);
  }
  
  public static boolean isWeb(String cskl) {
  	return impl.isWeb(cskl);
  }
  
  public static void deletePartner(int part) {
    impl.deletePartner(part);
  }
  
  public static void updatePartner(DataSet ds) {
    impl.updatePartner(ds);
  }
  
  public static void updateStanje(int cart, DataSet ms) {
  	impl.updateStanje(cart, ms);
  }
  
  public static void updateAllStanje() {
    impl.updateAllStanje();
  }
  
  public static void updateStanje(String cart, int count) {
    impl.updateStanje(cart, count);
  }
  
  public static void updatePopust(String cpar, String cart, BigDecimal discount) {
    impl.updatePopust(cpar, cart, discount);
  }
  
  public static void importDocs() {
    impl.importDocs();
  }
  
  public static void main(String[] args) {
    
    MsgDispatcher.install(false);
    dM.getDataModule();
    
    if (impl == null)
    	try {
				impl = (WebSyncInterface) Class.forName("hr.restart.robno.WebSyncImpl_binom").newInstance();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				active = false;
				e.printStackTrace();
			}
    
    int spod = Aus.getNumber(hr.restart.util.IntParam.getTag("salepod.delay"));
    if (spod > 0)
      impl.install(spod);
   
    try {
      Thread.currentThread().join();
    } catch (InterruptedException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }
}
