package hr.restart.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Variant;


public class HashSum {
  HashMap map = new HashMap();
  DataSet ds;
  Variant v = new Variant();
  String keyCol, valCol;

  public HashSum() {
    //
  }
  
  public HashSum(DataSet ds, String keyCol, String valCol) {
    this.ds = ds;
    this.keyCol = keyCol;
    this.valCol = valCol;
  }

  public void add() {
    ds.getVariant(keyCol, ds.getRow(), v);
    add(v.toString());
  }
  
  private void add(String key) {
    ds.getVariant(valCol, ds.getRow(), v);
    if (v.getType() == Variant.BIGDECIMAL)
      add(key, v.getBigDecimal());
    else if (v.getType() == Variant.DOUBLE)
      add(key, v.getDouble());
    else if (v.getType() == Variant.FLOAT)
      add(key, v.getAsDouble());
  }
  
  public void add(String key, BigDecimal val) {
    BigDecimal old = (BigDecimal) map.get(key);
    if (old == null) map.put(key, val);
    else map.put(key, val == null ? old : val.add(old));
  }
  
  public void add(String key, double val) {
    Double old = (Double) map.get(key);
    if (old == null) map.put(key, new Double(val));
    else map.put(key, new Double(val + old.doubleValue()));
  }
  
  public BigDecimal get(String key) {
    return (BigDecimal) map.get(key);
  }
  
  public BigDecimal getBigDecimal(String key) {
    return (BigDecimal) map.get(key);
  }
  
  public double getDouble(String key) {
    return ((Double) map.get(key)).doubleValue();
  }
  
  public Iterator iterator() {
    return map.keySet().iterator();
  }
}
