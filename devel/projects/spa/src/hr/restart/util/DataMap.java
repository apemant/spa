package hr.restart.util;

import java.util.HashMap;
import java.util.Iterator;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;


public class DataMap {

  String[] key;
  HashMap map;
  DataSet data;
  VarStr s = new VarStr();
  Variant v = new Variant();
  
  public DataMap(DataSet ds, String[] keyCols) {
    key = keyCols;
    data = ds;
    map = new HashMap();
    
    for (ds.first(); ds.inBounds(); ds.next())
      map.put(getKey(ds), new Long(ds.getInternalRow()));
  }
  
  public String getKey(DataSet ds) {
    s.clear();
    for (int i = 0; i < key.length; i++) {
      ds.getVariant(key[i], v);
      s.append(v.toString()).append('-');
    }
    return s.chop().toString();
  }
  
  public boolean contains(DataSet other) {
    return map.containsKey(getKey(other));
  }
  
  public void remove(DataSet other) {
    map.remove(getKey(other));
  }
  
  public DataSet getRow(DataSet other) {
    data.goToInternalRow(((Long) map.get(getKey(other))).longValue());
    return data;
  }
  
  public boolean contains(String key) {
    return map.containsKey(key);
  }
  
  public void remove(String key) {
    map.remove(key);
  }
  
  public DataSet getRow(String key) {
    data.goToInternalRow(((Long) map.get(key)).longValue());
    return data;
  }
  
  public Iterator keyIterator() {
    return map.keySet().iterator();
  }
}
