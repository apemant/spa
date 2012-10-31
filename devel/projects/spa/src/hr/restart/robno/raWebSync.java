package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.Stanje;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.rpc.ServiceException;

import com.borland.dx.dataset.DataSet;


public class raWebSync {
  
  public static Object lokk = new Object();
  
  public static String apiKey;
  
  public static boolean active = frmParam.getParam("sisfun", "webSync", "N", "Web sinkronizacija (D,N)").equals("D");
  
  public static Set carts = new HashSet();
  static Set sklads = new HashSet();
  
  static {
    loadParams();
  }
  
  
  static void loadParams() {
    try {
      
      apiKey = frmParam.getParam("sisfun", "webApikey", "", "ApiKey za web sync");
      
      DataSet ds = Aus.q("SELECT cart FROM stakcije WHERE cak='#web'");
      for (ds.first(); ds.inBounds(); ds.next())
        carts.add(new Integer(ds.getInt("CART")));
      
      String skl = frmParam.getParam("sisfun", "webSklads", "", "Skladišta za web sync");
      if (skl.indexOf(',') < 0) sklads.addAll(Arrays.asList(new VarStr(skl).split()));
      else sklads.addAll(Arrays.asList(new VarStr(skl).splitTrimmed(',')));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static boolean isWeb(int cart) {
    return carts.contains(new Integer(cart));
  }
  
  public static boolean isWeb(String cskl) {
    return sklads.contains(cskl);
  }
  
  public static Condition getSkladCond() {
    return Condition.in("CSKL", sklads.toArray()); 
  }

  public static void deletePartner(int part) {
    try {
      hr.binom.ErpSoap es = new hr.binom.ErpLocator().geterpSoap();
      
      int resp = es.deletePartner(apiKey, part + "");
            
      if (resp < 1) new Exception("Response " + resp).printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
  
  public static void updatePartner(DataSet ds) {
    try {
      hr.binom.ErpSoap es = new hr.binom.ErpLocator().geterpSoap();
      String ko = ds.getString("KO");
      String ime = ko.indexOf(' ') > 0 ? ko.substring(0, ko.indexOf(' ')).trim() : ko;
      String pre = ko.indexOf(' ') > 0 ? ko.substring(ko.indexOf(' ')).trim() : "";
      int resp = es.savePartner(apiKey, ds.getInt("CPAR") + "", ime, pre, ds.getString("ADR"), ds.getInt("PBR")+"", ds.getString("MJ"), 
          ds.getString("TEL"), ds.getString("EMADR"), ds.getString("NAZPAR"), ds.getString("OIB"), ds.getString("ADR"), ds.getString("AKTIV").equals("D"));
      if (resp < 1) new Exception("Response " + resp).printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
  
  public static void updateStanje(int cart, DataSet ms) {
    DataSet ds = Stanje.getDataModule().getTempSet(getSkladCond().
        and(Condition.equal("CART", cart)).and(Condition.equal("GOD", ms)));
    ds.open();
    BigDecimal tot = Aus.zero0;
    for (ds.first(); ds.inBounds(); ds.next()) {
      tot = tot.add(ds.getBigDecimal("KOL"));
    }
    updateStanje(Integer.toString(cart), tot.intValue());
  }
  
  public static void updateStanje(String cart, int count) {
    try {
      hr.binom.ErpSoap es = new hr.binom.ErpLocator().geterpSoap();
      
      int resp = es.saveInventoryCount(apiKey, cart, count);
                  
      if (resp < 1) new Exception("Response " + resp).printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
  
  public static void updatePopust(String cpar, String cart, BigDecimal discount) {
    try {
      hr.binom.ErpSoap es = new hr.binom.ErpLocator().geterpSoap();

      int resp = es.saveDiscount(apiKey, cpar, cart, discount);

      if (resp < 1) new Exception("Response " + resp).printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
  
  public static void importDocs() {
    synchronized (lokk) {
      importImpl();
    }
  }
  
  static void importImpl() {
    try {
      hr.binom.ErpSoap es = new hr.binom.ErpLocator().geterpSoap();

      hr.binom.OrderBase ob = es.getOrders(apiKey, -1);
      
      //if (resp < 1) new Exception("Response " + resp).printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
}
