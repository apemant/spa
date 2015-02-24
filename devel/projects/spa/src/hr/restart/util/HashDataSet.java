package hr.restart.util;

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
    if (keyCol != null)
      for (ds.first(); ds.inBounds(); ds.next()) {
        ds.getVariant(keyCol, ds.getRow(), v);
        index.put(v.toString(), new Integer(ds.getRow()));
      }
    else
      for (ds.first(); ds.inBounds(); ds.next()) {
        s.clear();
        for (int i = 0; i < keyCols.length; i++) {
          ds.getVariant(keyCols[i], ds.getRow(), v);
          s.append(v).append("|");
        }
        index.put(s.chop().toString(), new Integer(ds.getRow()));
      }
  }
  
  public boolean has(String key) {
    return index.containsKey(key);
  }
  
  public boolean has(String[] keys) {
    return has(VarStr.join(keys, '|').toString());
  }
  
  public boolean has(ReadRow src) {
    if (keyCol != null) return has(src, keyCol);
    return has(src, keyCols);
  }
  
  public boolean has(ReadRow src, String skeyCol) {
    src.getVariant(skeyCol, v);
    return has(v.toString());
  }
  
  public boolean has(ReadRow src, String[] skeyCols) {
    s.clear();
    for (int i = 0; i < skeyCols.length; i++) {
      src.getVariant(skeyCols[i], v);
      s.append(v).append("|");
    }
    return has(s.chop().toString());
  }
  
  public DataSet get() {
    return ds;
  }
  
  public DataSet get(String key) {
    Integer idx = (Integer) index.get(key);
    if (idx != null) ds.goToRow(idx.intValue());
    return ds;
  }
  
  public DataSet get(String[] keys) {
    return get(VarStr.join(keys, '|').toString());
  }
  
  public DataSet get(ReadRow src) {
    if (keyCol != null) return get(src, keyCol);
    return get(src, keyCols);
  }
  
  public DataSet get(ReadRow src, String skeyCol) {
    src.getVariant(skeyCol, v);
    return get(v.toString());
  }
  
  public DataSet get(ReadRow src, String[] skeyCols) {
    s.clear();
    for (int i = 0; i < keyCols.length; i++) {
      src.getVariant(keyCols[i], v);
      s.append(v).append("|");
    }
    return get(s.chop().toString());
  }
}
