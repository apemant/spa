package hr.restart.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.Variant;


public class HashSum {
  HashMap map = new HashMap();
  DataSet ds;
  String keyCol, valCol;
  int keyType, valType;

  public HashSum() {
    //
  }
  
  public HashSum(DataSet ds, String keyCol, String valCol) {
    this.ds = ds;
    this.keyCol = keyCol;
    this.valCol = valCol;
    keyType = ds.getColumn(keyCol).getDataType();
    valType = ds.getColumn(valCol).getDataType();
  }

  public void set(DataSet ds, String keyCol, String valCol) {
    this.ds = ds;
    this.keyCol = keyCol;
    this.valCol = valCol;
    valType = ds.getColumn(valCol).getDataType();
  }
  
  public void set(DataSet ds, String valCol) {
    this.ds = ds;
    this.valCol = valCol;
    valType = ds.getColumn(valCol).getDataType();
  }
  
  public void set(DataSet ds) {
    this.ds = ds;
  }
  
  public void set(String valCol) {
    this.valCol = valCol;
    valType = ds.getColumn(valCol).getDataType();
  }
  
  protected Object getKey(ReadRow row) {
    if (keyType == Variant.STRING)
      return row.getString(keyCol);
    if (keyType == Variant.INT)
      return Integer.valueOf(row.getInt(keyCol));
    if (keyType == Variant.SHORT)
      return Integer.valueOf(row.getShort(keyCol));
    return null;
  }
  
  public void add(ReadRow row) {
    add(getKey(row), valCol);
  }
  
  public void add() {
    add(getKey(ds), valCol);
  }
  
  public void addVal(String valc) {
    add(getKey(ds), valc);
  }
  
  public BigDecimal get() {
    return get(getKey(ds));
  }
  
  public BigDecimal get(ReadRow row) {
    return get(getKey(row));
  }
  
  private void add(Object key, String valc) {
    if (valType == Variant.BIGDECIMAL)
      add(key, ds.getBigDecimal(valc));
    else if (valType == Variant.DOUBLE)
      add(key, ds.getDouble(valc));
    else if (valType == Variant.FLOAT)
      add(key, ds.getFloat(valc));
  }
  
  public void add(Object key, BigDecimal val) {
    BigDecimal old = (BigDecimal) map.get(key);
    if (old == null) map.put(key, val);
    else map.put(key, val == null ? old : val.add(old));
  }
  
  public void add(Object key, double val) {
    Double old = (Double) map.get(key);
    if (old == null) map.put(key, new Double(val));
    else map.put(key, new Double(val + old.doubleValue()));
  }
  
  public BigDecimal get(Object key) {
    return (BigDecimal) map.get(key);
  }
  
  public BigDecimal getBigDecimal(Object key) {
    return (BigDecimal) map.get(key);
  }
  
  public double getDouble(Object key) {
    return ((Double) map.get(key)).doubleValue();
  }
  
  public Iterator iterator() {
    return map.keySet().iterator();
  }
  
  public BigDecimal total() {
    BigDecimal tot = Aus.zero0;
    for (Iterator i = map.values().iterator(); i.hasNext(); )
      tot = tot.add((BigDecimal) i.next());
    return tot;
  }
  
  public double totalDouble() {
    double tot = 0;
    for (Iterator i = map.values().iterator(); i.hasNext(); )
      tot += ((Double) i.next()).doubleValue();
    return tot;
  }
}
