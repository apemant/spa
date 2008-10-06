package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.Vrart;
import hr.restart.baza.dM;
import hr.restart.util.lookupData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;


public class raVart {
  
  private static raVart inst = new raVart();
  
  private static dM dm = dM.getDataModule();
  private static lookupData ld = lookupData.getlookupData();
  
  private Map data;
  private int serial;
  
  private DataRow art;
  
  private raVart() {
  }
  
  public static raVart getInstance() {
    return inst;
  }
  
  public static void init() {
    readData(true);
  }
  
  public static boolean isStanje(String vrart) {
    return getData(vrart).stanje;
  }
  
  public static boolean isStanje(ReadRow art) {
    return getData(art.getString("VRART")).stanje;
  }
  
  public static boolean isStanje(int cart) {
    return isFlag(cart, 'S');
  }
  
  public static boolean isVarnaziv(String vrart) {
    return getData(vrart).varnaziv;
  }
  
  public static boolean isVarnaziv(ReadRow art) {
    return getData(art.getString("VRART")).varnaziv;
  }
  
  public static boolean isVarnaziv(int cart) {
    return isFlag(cart, 'V');
  }
  
  public static boolean isNorma(String vrart) {
    return getData(vrart).norma;
  }
  
  public static boolean isNorma(ReadRow art) {
    return getData(art.getString("VRART")).norma;
  }
  
  public static boolean isNorma(int cart) {
    return isFlag(cart, 'N');
  }
  
  public static boolean isUsluga(String vrart) {
    return getData(vrart).usluga;
  }
  
  public static boolean isUsluga(ReadRow art) {
    return getData(art.getString("VRART")).usluga;
  }
  
  public static boolean isUsluga(int cart) {
    return isFlag(cart, 'U');
  }
  
  public static Condition getStanjeCond() {
    return getFlagCond('S');
  }
  
  public static Condition getVarnazivCond() {
    return getFlagCond('V');
  }
  
  public static Condition getNormaCond() {
    return getFlagCond('N');
  }
  
  public static Condition getUslugaCond() {
    return getFlagCond('U');
  }
  
  private static boolean isFlag(int cart, char flag) {
    if (inst.art == null || inst.art.getInt("CART") != cart)
      inst.art = ld.raLookup(dm.getArtikli(), "CART", Integer.toString(cart));
    if (inst.art == null) return false;
    return getData(inst.art.getString("VRART")).isFlag(flag);
  }
    
  private static Condition getFlagCond(char flag) {
    checkData();
    
    List ret = new ArrayList();
    for (Iterator i = inst.data.keySet().iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      if (((Data) inst.data.get(key)).isFlag(flag))
        ret.add(key);
    }
    
    return Condition.in("vrart", ret.toArray()).qualified("artikli");
  }
  
  public static Condition getProizvodCond() {
    checkData();
    
    List ret = new ArrayList();
    for (Iterator i = inst.data.keySet().iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      Data d = (Data) inst.data.get(key);
      if (d.stanje && d.norma) ret.add(key);
    }
    
    return Condition.in("vrart", ret.toArray()).qualified("artikli");
  }
  
  private static Data getData(String vrart) {
    checkData();
    
    Data ret = (Data) inst.data.get(vrart);
    if (ret == null) 
      throw new RuntimeException("Nepostojeæa vrsta artikla: "+vrart);
    
    return ret;
  }
  
  private static void checkData() {
    if (inst.serial != dM.getSynchronizer().getSerialNumber("vrart"))
      readData(false);
  }
  
  private static void readData(boolean init) {
    DataSet ds = Vrart.getDataModule().getTempSet();
    Map m = new HashMap();
    
    Data.fix = false;
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next())
      if (ds.getString("AKTIV").equalsIgnoreCase("D"))
        m.put(ds.getString("CVRART"), new Data(ds));
    
    if (init && Data.fix) {
      try {
        ds.saveChanges();
        dM.getSynchronizer().markAsDirty("vrart");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    inst.data = m;
    inst.serial = dM.getSynchronizer().getSerialNumber("vrart");
  }
  
  private static class Data {
    public String vrart;
    public String naziv;
    boolean stanje, varnaziv, norma, usluga;
    
    public static boolean fix = false;
    
    public Data(ReadWriteRow row) {
      vrart = row.getString("CVRART");
      naziv = row.getString("NAZVRART");
      if (row.getString("STANJE").length() > 0)
        stanje = row.getString("STANJE").equalsIgnoreCase("D");
      else {
        stanje = vrart.equalsIgnoreCase("R") ||
                 vrart.equalsIgnoreCase("M") ||
                 vrart.equalsIgnoreCase("P");
        row.setString("STANJE", stanje ? "D" : "N");
        fix = true;
      }
      
      if (row.getString("VARNAZIV").length() > 0)
        varnaziv = row.getString("VARNAZIV").equalsIgnoreCase("D");
      else {
        varnaziv = vrart.equalsIgnoreCase("U") ||
                   vrart.equalsIgnoreCase("T");
        row.setString("VARNAZIV", varnaziv ? "D" : "N");
        fix = true;
      }
      
      if (row.getString("NORMA").length() > 0)
        norma = row.getString("NORMA").equalsIgnoreCase("D");
      else {
        norma = vrart.equalsIgnoreCase("P") ||
                vrart.equalsIgnoreCase("A");
        row.setString("NORMA", norma ? "D" : "N");
        fix = true;
      }
      
      if (row.getString("USLUGA").length() > 0)
        usluga = row.getString("USLUGA").equalsIgnoreCase("D");
      else {
        usluga = vrart.equalsIgnoreCase("U");
        row.setString("USLUGA", usluga ? "D" : "N");
        fix = true;
      }
    }
    
    public boolean isFlag(char flag) {
      if (flag == 'S') return stanje;
      if (flag == 'V') return varnaziv;
      if (flag == 'N') return norma;
      if (flag == 'U') return usluga;
      return true;
    }
  }
}
