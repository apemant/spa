/****license*****************************************************************
**   file: raDataFilter.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.util;

import hr.restart.robno.raDateUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.RowFilterResponse;
import com.borland.dx.dataset.Variant;


public abstract class raDataFilter implements RowFilterListener {
  
  static Util ut = Util.getUtil();
  static raDateUtil rdu = raDateUtil.getraDateUtil();
  
  public static final int EQUAL_TO = 1;
  public static final int GREATER_THAN = 2;
  public static final int AFTER = 2;
  public static final int LESS_THAN = 3;
  public static final int BEFORE = 3;
  public static final int CONTAINS = 4;
  public static final int BEGINS_WITH = 5;
  public static final int ENDS_WITH = 6;
  public static final int SAME_MONTH = 7;
  public static final int SAME_YEAR = 8;
  public static final int LONGER_THAN = 9;
  public static final int SHORTER_THAN = 10;
  
  public static final String[] ops = {"",
    "=", ">", "<", "*=*", "=*", "*=", "=M", "=Y", "\"\">", "\"\"<"
  };
  
  public static final String[] nops = {"",
    "\u2260", "\u2264", "\u2265", "*\u2260*", "\u2260*", 
    "*\u2260", "\u2260M", "\u2260Y", "\"\"\u2264", "\"\"\u2265"
  };
  
  public static final String[] opdescs = {"",
    "Jednako", 
    "Veæe od", 
    "Manje od", 
    "Sadrži tekst", 
    "Poèinje tekstom", 
    "Završava tekstom", 
    "Istog mjeseca", 
    "Iste godine", 
    "Dulje od", 
    "Kraæe od"
  };
  
  public static final String[] nopdescs = {"",
    "Nije jednako", 
    "Manje ili jednako", 
    "Veæe ili jednako", 
    "Ne sadrži tekst", 
    "Ne poèinje tekstom", 
    "Ne završava tekstom", 
    "Razlièitog mjeseca", 
    "Razlièite godine", 
    "Kraæe ili jednako", 
    "Dulje ili jednako"
  };
  
  public boolean isRow(ReadRow row) {
    return true;
  }
  
  public void filterRow(ReadRow row, RowFilterResponse resp) {
    if (isRow(row)) resp.add();
    else resp.ignore();
  }
  
  public static raDataFilter equal(ReadRow row, String col) {
    return new SimpleFilter(row, col, EQUAL_TO, false);
  }
  
  public static raDataFilter different(ReadRow row, String col) {
    return new SimpleFilter(row, col, EQUAL_TO, true);
  }
  
  public static raDataFilter greater(ReadRow row, String col) {
    return new SimpleFilter(row, col, GREATER_THAN, false);
  }
  
  public static raDataFilter less(ReadRow row, String col) {
    return new SimpleFilter(row, col, LESS_THAN, false);
  }
  
  public static raDataFilter lessEqual(ReadRow row, String col) {
    return new SimpleFilter(row, col, GREATER_THAN, true);
  }
  
  public static raDataFilter greaterEqual(ReadRow row, String col) {
    return new SimpleFilter(row, col, LESS_THAN, true);
  }
  
  public static raDataFilter contains(ReadRow row, String col) {
    return new SimpleFilter(row, col, CONTAINS, false);
  }
  
  public static raDataFilter beginsWith(ReadRow row, String col) {
    return new SimpleFilter(row, col, BEGINS_WITH, false);
  }
  
  public static raDataFilter endsWith(ReadRow row, String col) {
    return new SimpleFilter(row, col, ENDS_WITH, false);
  }
  
  public static raDataFilter notContains(ReadRow row, String col) {
    return new SimpleFilter(row, col, CONTAINS, true);
  }
  
  public static raDataFilter notBeginsWith(ReadRow row, String col) {
    return new SimpleFilter(row, col, BEGINS_WITH, true);
  }
  
  public static raDataFilter notEndsWith(ReadRow row, String col) {
    return new SimpleFilter(row, col, ENDS_WITH, true);
  }
  
  public static raDataFilter longer(ReadRow row, String col) {
    return new SimpleFilter(row, col, LONGER_THAN, false);
  }
  
  public static raDataFilter shorter(ReadRow row, String col) {
    return new SimpleFilter(row, col, SHORTER_THAN, false);
  }
  
  public static raDataFilter shorterEqual(ReadRow row, String col) {
    return new SimpleFilter(row, col, LONGER_THAN, true);
  }
  
  public static raDataFilter longerEqual(ReadRow row, String col) {
    return new SimpleFilter(row, col, SHORTER_THAN, true);
  }
  
  public static raDataFilter sameMonth(ReadRow row, String col) {
    return new SimpleFilter(row, col, SAME_MONTH, false);
  }
  
  public static raDataFilter sameYear(ReadRow row, String col) {
    return new SimpleFilter(row, col, SAME_YEAR, false);
  }
  
  public static raDataFilter differentMonth(ReadRow row, String col) {
    return new SimpleFilter(row, col, SAME_MONTH, true);
  }
  
  public static raDataFilter differentYear(ReadRow row, String col) {
    return new SimpleFilter(row, col, SAME_YEAR, true);
  }
  
  public raDataFilter copy() {
    return null;
  }
  
  public raDataFilter and(raDataFilter other) {
    return new CompoundFilter(this, other, true);
  }
  
  public raDataFilter or(raDataFilter other) {
    return new CompoundFilter(this, other, false);
  }
  
  public raDataFilter pack() {
    return this;
  }
  
  public boolean isSimple() {
    return !isCompound();
  }
  
  public int getOperator() {
    return 0;
  }

  public boolean isNot() {
    return false;
  }

  public String getColumn() {
    return null;
  }

  public Object getValue() {
    return null;
  }

  public int getDataType() {
    return 0;
  }
  
  public boolean operatorAllowed(int op) {
    return false;
  }
  
  public void setOperator(int op, boolean not) {
    //
  }
  
  public void setField(ReadRow row, String col) {
    //
  }
  
  public void putValue(ReadWriteRow row) {
    
  }
  
  public boolean isCompound() {
    return false;
  }
  
  public boolean isAnd() {
    return true;
  }
  
  public raDataFilter[] getSubfilters() {
    return null;
  }
  
  public void removeFilter(raDataFilter filt) {
    //
  }
  
  public void replaceFilter(raDataFilter oldf, raDataFilter newf) {
    //
  }
  
  public void setAnd(boolean and) {
    
  }
  
  public String store() {
  	return "";
  }
  
  public static raDataFilter parse(String str) {
  	if (str.startsWith("+") || str.startsWith("-"))
  		return new SimpleFilter(str);
  	return new CompoundFilter(str);
  }
  
  public boolean compatibleWith(ReadRow ds) {
  	return true;
  }
}

class SimpleFilter extends raDataFilter {
  private int op;
  private boolean not;
  private String col;
  private String cap;
  private String vals;
  private Object val;
  private int type;

  public SimpleFilter(ReadRow row, String col, int op, boolean not) {
    setField(row, col);
    setOperator(op, not);
  }
  
  public SimpleFilter(SimpleFilter copy) {
    this.op = copy.op;
    this.not = copy.not;
    this.col = copy.col;
    this.cap = copy.cap;
    this.val = copy.val;
    this.vals = copy.vals;
    this.type = copy.type;
  } 
  
  public SimpleFilter(String str) {
  	load(str);
  }
  
  public raDataFilter copy() {
    return new SimpleFilter(this);
  }
  
  public boolean isRow(ReadRow row) {
    if (type == Variant.STRING)
      return not ^ isFilter(vals, row.getString(col).toLowerCase());
    if (type == Variant.BIGDECIMAL)
      return not ^ isFilter((BigDecimal) val, row.getBigDecimal(col));
    if (type == Variant.TIMESTAMP)
      return not ^ isFilter((Timestamp) val, row.getTimestamp(col));
    if (type == Variant.INT)
      return not ^ isFilter(((Long) val).longValue(), row.getInt(col));
    if (type == Variant.SHORT)
      return not ^ isFilter(((Long) val).longValue(), row.getShort(col));
    if (type == Variant.LONG)
      return not ^ isFilter(((Long) val).longValue(), row.getLong(col));
    if (type == Variant.DOUBLE)
      return not ^ isFilter(((Double) val).longValue(), row.getDouble(col));
    if (type == Variant.FLOAT)
      return not ^ isFilter(((Double) val).longValue(), row.getFloat(col));
    return false;
  }
  
  public boolean compatibleWith(ReadRow ds) {
  	return ds.hasColumn(col) != null && ds.hasColumn(col).getDataType() == type;
  }
  
  private boolean isFilter(String v, String s) {
    switch (op) {
      case EQUAL_TO:
        return s.equals(v);
      case GREATER_THAN:
        return s.compareTo(v) > 0;
      case LESS_THAN:
        return s.compareTo(v) < 0;
      case CONTAINS:
        return s.indexOf(v) >= 0;
      case BEGINS_WITH:
        return s.startsWith(v);
      case ENDS_WITH:
        return s.endsWith(v);
      case LONGER_THAN:
        return s.length() > v.length();
      case SHORTER_THAN:
        return s.length() < v.length();
      default:
        return false;
    }
  }
    
  private boolean isFilter(BigDecimal v, BigDecimal s) {
    switch (op) {
      case EQUAL_TO:
        return s.compareTo(v) == 0;
      case GREATER_THAN:
        return s.compareTo(v) > 0;
      case LESS_THAN:
        return s.compareTo(v) < 0;
      default:
        return false;
    }
  }
    
  private boolean isFilter(Timestamp v, Timestamp s) {
    switch (op) {
      case EQUAL_TO:
        return ut.sameDay(s, v);
      case GREATER_THAN:
        return rdu.CompareDate(s, v) > 0;
      case LESS_THAN:
        return rdu.CompareDate(s, v) < 0;
      case SAME_MONTH:
        return ut.sameMonth(s, v);
      case SAME_YEAR:
        return ut.sameYear(s, v);
      default:
        return false;
    }
  }
  
  private boolean isFilter(long v, long s) {
    switch (op) {
      case EQUAL_TO:
        return s == v;
      case GREATER_THAN:
        return s > v;
      case LESS_THAN:
        return s < v;
      default:
        return false;
    }
  }
  
  private boolean isFilter(double v, double s) {
    switch (op) {
      case EQUAL_TO:
        return s == v;
      case GREATER_THAN:
        return s > v;
      case LESS_THAN:
        return s < v;
      default:
        return false;
    }
  }
  
  public void setOperator(int op, boolean not) {
    this.op = op;
    this.not = not;
  }

  public int getOperator() {
    return op;
  }
  
  public boolean operatorAllowed(int o) {
    switch (type) {
      case Variant.STRING:
        return (o >= 1 && o <= 6) || o == 9 || o == 10;
      case Variant.TIMESTAMP:
        return (o >= 1 && o <= 3) || o == 7 || o == 8;
      default:
        return (o >= 1 && o <= 3);
    }
  }
  
  public boolean isNot() {
    return not;
  }

  public void setField(ReadRow row, String col) {
    type = row.getColumn(col).getDataType();
    cap = row.getColumn(col).getCaption();
    if (cap == null || cap.length() == 0) cap = col;
    
    if (type == Variant.STRING)
      vals = ((String) (val = row.getString(col))).toLowerCase();
    else if (type == Variant.BIGDECIMAL)
      val = row.getBigDecimal(col);
    else if (type == Variant.TIMESTAMP)
      val = row.getTimestamp(col);
    else if (type == Variant.INT)
      val = new Long(row.getInt(col));
    else if (type == Variant.SHORT)
      val = new Long(row.getShort(col));
    else if (type == Variant.LONG)
      val = new Long(row.getLong(col));
    else if (type == Variant.DOUBLE)
      val = new Double(row.getDouble(col));
    else if (type == Variant.FLOAT)
      val = new Double(row.getFloat(col));
    
    this.col = col;
  }
  
  public void putValue(ReadWriteRow row) {
    if (type == Variant.STRING)
      row.setString(col, (String) val);
    else if (type == Variant.BIGDECIMAL)
      row.setBigDecimal(col, (BigDecimal) val);
    else if (type == Variant.TIMESTAMP)
      row.setTimestamp(col, (Timestamp) val);
    else if (type == Variant.INT)
      row.setInt(col, ((Long) val).intValue());
    else if (type == Variant.SHORT)
      row.setShort(col, ((Long) val).shortValue());
    else if (type == Variant.LONG)
      row.setLong(col, ((Long) val).longValue());
    else if (type == Variant.DOUBLE)
      row.setDouble(col, ((Double) val).doubleValue());
    else if (type == Variant.FLOAT)
      row.setFloat(col, ((Double) val).floatValue());
  }
  
  public String getColumn() {
    return col;
  }

  public Object getValue() {
    return val;
  }

  public int getDataType() {
    return type;
  }
  
  static String[] cspec = {"%", "|", "[", "]"};
  static String[] sspec = {"%pc", "%ln", "%ob", "%cb"};
  
  private VarStr cb = new VarStr();
  private String convertc(String orig) {
  	cb.clear().append(orig);
  	for (int i = 0; i < cspec.length; i++)
  		cb.replace(cspec[i], sspec[i]);
  	return cb.toString();
  }
  
  private String converts(String orig) {
  	cb.clear().append(orig);
  	for (int i = cspec.length - 1; i >= 0; i--)
  		cb.replace(sspec[i], cspec[i]);
  	return cb.toString();
  }
  
  public String store() {
  	buf.clear();
  	buf.append(not ? '-' : '+').append(cap).append('|');
  	buf.append(col).append('|').append(op).append('|');
  	buf.append(convertc(val.toString())).append('|').append(type);
  	return buf.toString();
  }
  
  void load(String str) {
  	not = str.startsWith("-");
  	str = converts(str.substring(1));
  	String[] parts = new VarStr(str).split('|');
  	cap = parts[0];
  	col = parts[1];
  	op = Integer.parseInt(parts[2]);
  	type = Integer.parseInt(parts[4]);
  	if (type == Variant.STRING)
      vals = ((String) (val = parts[3])).toLowerCase();
    else if (type == Variant.BIGDECIMAL)
      val = new BigDecimal(parts[3]);
    else if (type == Variant.TIMESTAMP)
      val = Timestamp.valueOf(parts[3]);
    else if (type == Variant.INT || type == Variant.SHORT || type == Variant.LONG)
      val = new Long(parts[3]);
    else if (type == Variant.DOUBLE || type == Variant.FLOAT)
      val = new Double(parts[3]);
  }

  private VarStr buf = new VarStr();
  public String toString() {
    buf.clear().append(cap).append(' ');
    buf.append(not ? nops[op] : ops[op]).append(' ');
    if (type == Variant.STRING)
      buf.append('"').append(val).append('"');
    else if (type == Variant.BIGDECIMAL)
      buf.append(Aus.formatBigDecimal((BigDecimal) val));
    else if (type == Variant.TIMESTAMP)
      buf.append(Aus.formatTimestamp((Timestamp) val));
    else if (type == Variant.DOUBLE || type == Variant.FLOAT)
      buf.append(Aus.formatFloat(((Double) val).floatValue()));
    else buf.append(val);
    return buf.toString();
  }
}

class CompoundFilter extends raDataFilter {
  private boolean and;
  private List sf = new ArrayList();
  
  public CompoundFilter(raDataFilter first, raDataFilter second, boolean and) {
    this.and = and;
    sf.add(first);
    sf.add(second);
  }
  
  public CompoundFilter(CompoundFilter copy) {
    this.and = copy.and;
    for (int i = 0; i < copy.sf.size(); i++)
      this.sf.add(((raDataFilter) copy.sf.get(i)).copy());
  }
  
  public CompoundFilter(String str) {
  	load(str);
  }
  
  public raDataFilter copy() {
    return new CompoundFilter(this);
  }
  
  public void addFilter(raDataFilter another) {
    sf.add(another);
  }
  
  public void removeFilter(raDataFilter filt) {
    sf.remove(filt);
  }
  
  public void replaceFilter(raDataFilter oldf, raDataFilter newf) {
    for (int i = 0; i < sf.size(); i++) {
      if (sf.get(i) == oldf)
        sf.set(i, newf);
    }
  }
  
  public boolean isCompound() {
    return true;
  }
  
  public raDataFilter[] getSubfilters() {
    return (raDataFilter[]) sf.toArray(new raDataFilter[sf.size()]);
  }
  
  public void setAnd(boolean and) {
    this.and = and;
  }
  
  public boolean isAnd() {
    return and;
  }
  
  public raDataFilter and(raDataFilter other) {
    if (!and) return super.and(other);
    addFilter(other);
    return this;
  }
  
  public raDataFilter or(raDataFilter other) {
    if (and) return super.or(other);
    addFilter(other);
    return this;
  }
  
  public raDataFilter pack() {
    if (sf.size() == 1) return (raDataFilter) sf.get(0);
    for (int i = 0; i < sf.size(); i++) {
      raDataFilter ch = (raDataFilter) sf.get(i);
      sf.set(i, ch = ch.pack());
      if (ch instanceof CompoundFilter &&
          and == ((CompoundFilter) ch).and) {
        sf.remove(i);
        sf.addAll(i, ((CompoundFilter) ch).sf);
      }
    }
    return this;
  }
  
  public boolean isRow(ReadRow row) {
    if (and) {
      for (int i = 0; i < sf.size(); i++)
        if (!((raDataFilter) sf.get(i)).isRow(row)) return false;
      return true;
    }
    for (int i = 0; i < sf.size(); i++)
      if (((raDataFilter) sf.get(i)).isRow(row)) return true;
    return false;
  }
  
  public boolean compatibleWith(ReadRow ds) {
  	for (int i = 0; i < sf.size(); i++)
  		if (!((raDataFilter) sf.get(i)).compatibleWith(ds)) 
  			return false;
  	return true;
  }
  
  private VarStr buf = new VarStr();
  public String store() {
  	buf.clear().append(and ? "and" : "or");
  	for (int i = 0; i < sf.size(); i++)
  		buf.append('[').append(((raDataFilter) sf.get(i)).store()).append(']');
  	return buf.toString();
  }
  
  void load(String str) {
  	and = str.startsWith("and");
  	int level = 0, beg = 0;
  	for (int i = 0; i < str.length(); i++) {
  		char ch = str.charAt(i);
  		if (ch == '[') if (++level == 1) beg = i;
  		if (ch == ']') if (--level == 0) 
  			addFilter(raDataFilter.parse(str.substring(beg + 1, i)));
  	}
  }
  
  public String toString() {
    if (!and) return "Bar jedan od uvjeta";
    if (sf.size() == 2) return "Oba navedena uvjeta";
    return "Svi navedeni uvjeti";
  }
}
