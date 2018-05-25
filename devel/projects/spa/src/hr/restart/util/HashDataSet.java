package hr.restart.util;

import hr.restart.baza.dM;

import java.util.HashMap;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.Variant;


public class HashDataSet {
  DataSet ds;
  String keyCol;
  String[] keyCols;
  HashMap index = new HashMap();
  VarStr s = new VarStr();
  Variant v = new Variant();
  
  int serial = -1;

  public HashDataSet(DataSet ds, String keyCol) {
    this.ds = ds;
    this.keyCol = keyCol;
    init();
  }
  
  public HashDataSet(DataSet ds, String[] keyCols) {
    this.ds = ds;
    this.keyCols = keyCols;
    init();
  }

  void init() {
    ds.open();
    if (ds.getTableName() != null) 
      serial = dM.getSynchronizer().getSerialNumber(ds.getTableName());
    if (keyCol != null)
      for (ds.first(); ds.inBounds(); ds.next()) 
        index.put(key(ds, ds.getRow(), keyCol), new Long(ds.getInternalRow()));
    else
      for (ds.first(); ds.inBounds(); ds.next())
        index.put(key(ds, ds.getRow(), keyCols), new Long(ds.getInternalRow()));
  }
  
  public void sync() {
    if (ds.getTableName() != null) {
      int now = dM.getSynchronizer().getSerialNumber(ds.getTableName());
      if (now != serial) {
        index.clear();
        init();
      }
    }
  }
  
  public String key(ReadRow src) {
    if (keyCol != null) return key(src, keyCol);
    return key(src, keyCols);
  }
  
  public String key(ReadRow src, String skeyCol) {
    src.getVariant(skeyCol, v);
    return v.toString();
  }
  
  public String key(ReadRow src, String[] skeyCols) {
  	s.clear();
    for (int i = 0; i < skeyCols.length; i++) {
      src.getVariant(skeyCols[i], v);
      s.append(v).append("-|-");
    }
    return s.chop(3).toString();
  }
  
  public String key(DataSet src, int row, String skeyCol) {
    src.getVariant(skeyCol, row, v);
    return v.toString();
  }
  
  public String key(DataSet src, int row, String[] skeyCols) {
  	s.clear();
    for (int i = 0; i < skeyCols.length; i++) {
      src.getVariant(skeyCols[i], row, v);
      s.append(v).append("-|-");
    }
    return s.chop(3).toString();
  }
  
  public boolean has(Object key) {
    return index.containsKey(key.toString());
  }
  
  public boolean has(String key) {
    return index.containsKey(key);
  }
  
  public boolean has(String[] keys) {
    return has(VarStr.join(keys, "-|-").toString());
  }
  
  public boolean has(ReadRow src) {
    return has(key(src));
  }
  
  public boolean has(ReadRow src, String skeyCol) {
  	return has(key(src, skeyCol));
  }
  
  public boolean has(ReadRow src, String[] skeyCols) {
    return has(key(src, skeyCols));
  }
  
  public boolean has(DataSet src, int row, String skeyCol) {
  	return has(key(src, row, skeyCol));
  }
  
  public boolean has(DataSet src, int row, String[] skeyCols) {
  	return has(key(src, row, skeyCols));
  }
  
  public DataSet get() {
    return ds;
  }
    
  public DataSet get(Object key) {
    return get(key.toString());
  }
  
  public DataSet get(String key) {
    Long idx = (Long) index.get(key);
    if (idx != null) ds.goToInternalRow(idx.longValue());
    return ds;
  }
  
  public DataSet get(String[] keys) {
    return get(VarStr.join(keys, "-|-").toString());
  }
  
  public DataSet get(ReadRow src) {
    return get(key(src));
  }
    
  public DataSet get(ReadRow src, String skeyCol) {
    return get(key(src, skeyCol));
  }
  
  public DataSet get(ReadRow src, String[] skeyCols) {
  	return get(key(src, skeyCols));
  }
  
  public DataSet get(DataSet src, int row, String skeyCol) {
  	return get(key(src, row, skeyCol));
  }
  
  public DataSet get(DataSet src, int row, String[] skeyCols) {
  	return get(key(src, row, skeyCols));
  }
  
  public boolean loc(Object key) {
    return loc(key.toString());
  }
  
  public boolean loc(String key) {
    Long idx = (Long) index.get(key);
    if (idx == null) return false;
    return ds.goToInternalRow(idx.longValue());
  }
  
  public boolean loc(String[] keys) {
    return loc(VarStr.join(keys, "-|-").toString());
  }
  
  public boolean loc(ReadRow src) {
    return loc(key(src));
  }
    
  public boolean loc(ReadRow src, String skeyCol) {
    return loc(key(src, skeyCol));
  }
  
  public boolean loc(ReadRow src, String[] skeyCols) {
    return loc(key(src, skeyCols));
  }
  
  public boolean loc(DataSet src, int row, String skeyCol) {
    return loc(key(src, row, skeyCol));
  }
  
  public boolean loc(DataSet src, int row, String[] skeyCols) {
    return loc(key(src, row, skeyCols));
  }
  
  public void dump() {
    System.out.println(index);
  }
}
