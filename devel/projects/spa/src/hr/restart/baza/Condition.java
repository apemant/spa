/****license*****************************************************************
**   file: Condition.java
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
package hr.restart.baza;

import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.Variant;

/**
 * <p>Title: Condition</p>
 * <p>Description: Klasa za izolaciju SQL uvjeta (WHERE <x>).
 * Koristi se u pozivima metoda klase KreirDrop, npr.
 * KreiDrop.setFilter(QueryDataSet, Condition).<p>
 * Uvjet se definira pozivima statickih metoda definiranih u
 * ovoj klasi, npr.
 * <pre>
 * Condition.equal("CART", 6)
 * Condition.where("APP", Condition.NOT_EQUAL, "robno").
 *   and(Condition.between("DATDOK", ut.getFirstDayOfMonth(), vl.getToday()))
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public abstract class Condition {

  /**
   * Koliko maksimalno smije biti clanova u in (cl1, cl2, cl3, ... clN)
   * prije nego presjece sa OR polje in (clN+1, clN+2 ...)
   */
  public static final int MAXINQUERY = 200;
  
  /**
   * Konstanta koja oznacava operaciju =.
   */
  public static final int EQUAL = 0;

  /**
   * Konstanta koja oznacava operaciju !=.
   */
  public static final int NOT_EQUAL = 1;

  /**
   * Konstanta koja oznacava operaciju <.
   */
  public static final int BEFORE = 2;

  /**
   * Konstanta koja oznacava operaciju <.
   */
  public static final int LESS_THAN = 2;

  /**
   * Konstanta koja oznacava operaciju >.
   */
  public static final int AFTER = 3;

  /**
   * Konstanta koja oznacava operaciju >.
   */
  public static final int GREATER_THAN = 3;

  /**
   * Konstanta koja oznacava operaciju <=.
   */
  public static final int LESS_OR_EQUAL = 4;
  
  /**
   * Konstanta koja oznacava operaciju <=.
   */
  public static final int TILL = 4;

  /**
   * Konstanta koja oznacava operaciju >=.
   */
  public static final int GREATER_OR_EQUAL = 5;

  /**
   * Konstanta koja oznacava operaciju >=.
   */
  public static final int FROM = 5;

  /**
   * Konstanta koja oznacava operaciju BETWEEN.
   */
  public static final int IN_RANGE = 6;

  /**
   * Konstanta koja oznacava operaciju IN.
   */
  public static final int IN_LIST = 7;
  
  
  static final int RAW = -1;

  public static final String NOT_OP = "NOT ";
  public static final String AND_OP = " AND ";
  public static final String OR_OP = " OR ";

  protected static final String[] ops = new String[] {"=", "!=", "<", ">", "<=", ">="};

  protected static final int SIMPLE = -1;
  protected static final int NOT = 0;
  protected static final int AND = 1;
  protected static final int OR = 2;

  protected static final String[] opc = new String[] {NOT_OP, AND_OP, OR_OP};

  public static final Condition none = new SimpleCondition();
  public static final Condition ident = new SimpleCondition("1=1");
  public static final Condition nil = new SimpleCondition("1=0");

  protected Condition() {}
  
  public static Condition raw(String cond) {
    return new SimpleCondition(cond);
  }

  public static Condition whereAllEqual(String[] cols, ReadRow ds) {
    Condition con = Condition.equal(cols[0], ds);
    for (int i = 1; i < cols.length; i++)
      con = con.and(Condition.equal(cols[i], ds));
    return con;
  }
  
  public static Condition whereAllEqual(String[] cc, ReadRow ds, String[] dc) {
    Condition con = Condition.equal(cc[0], ds, dc[0]);
    for (int i = 1; i < cc.length; i++)
      con = con.and(Condition.equal(cc[i], ds, dc[0]));
    return con;
  }

  public static Condition whereAllEqual(String[] cols, Object[] vals) {
    Condition con = Condition.where(cols[0], EQUAL, vals[0]);
    for (int i = 1; i < cols.length; i++)
      con = con.and(Condition.where(cols[i], EQUAL, vals[i]));
    return con;
  }

  public static Condition in(String colname, DataSet ds) {
    return Condition.in(colname, ds, colname);
  }

  public static Condition in(String colname, DataSet ds, String dsColname) {
    int i = 0;
    Object arr;
    ds.open();
    switch (ds.getColumn(dsColname).getDataType()) {
      case Variant.STRING:
        arr = new String[ds.rowCount()];
        for (ds.first(); ds.inBounds(); ds.next(), i++)
          Array.set(arr, i, ds.getString(dsColname));
        return Condition.in(colname, arr);
      case Variant.INT:
        arr = new int[ds.rowCount()];
        for (ds.first(); ds.inBounds(); ds.next(), i++)
          Array.setInt(arr, i, ds.getInt(dsColname));
        return Condition.in(colname, arr);
      case Variant.SHORT:
        arr = new short[ds.rowCount()];
        for (ds.first(); ds.inBounds(); ds.next(), i++)
          Array.setShort(arr, i, ds.getShort(dsColname));
        return Condition.in(colname, arr);
    }
    throw new UnsupportedOperationException("Condition.in() ne podrzava "+
      Variant.typeName(ds.getColumn(dsColname).getDataType()));
  }
  
  public static Condition in(String colname, String inlist) {
    return in(colname, new VarStr(inlist).split());
  }

  public static Condition in(String colname, Object array) {
    if (!array.getClass().isArray())
      throw new UnsupportedOperationException("Condition.in() zahtjeva niz kao drugi parametar");
    if (Array.getLength(array) == 0)
      return nil;
    if (Array.getLength(array) == 1)
      return new SimpleCondition(EQUAL, colname, Array.get(array, 0));
    return new SimpleCondition(IN_LIST, colname, array);
  }

  public static Condition where(String colname, int condition, Object value) {
    return new SimpleCondition(condition, colname, value);
  }

  public static Condition where(String colname, int condition, String value) {
    return new SimpleCondition(condition, colname, value);
  }

  public static Condition where(String colname, int condition, int value) {
    return new SimpleCondition(condition, colname, new Integer(value));
  }

  public static Condition where(String colname, int condition, short value) {
    return new SimpleCondition(condition, colname, new Short(value));
  }

  public static Condition where(String colname, int condition, BigDecimal value) {
    return new SimpleCondition(condition, colname, value);
  }

  public static Condition where(String colname, int condition, Timestamp value) {
    return new SimpleCondition(condition, colname, value);
  }

  public static Condition where(String colname, int condition, double value) {
    return new SimpleCondition(condition, colname, new BigDecimal(value));
  }

  public static Condition where(String colname, int condition, ReadRow ds) {
    switch (ds.getColumn(colname).getDataType()) {
      case Variant.STRING:
        return where(colname, condition, ds.getString(colname));
      case Variant.INT:
        return where(colname, condition, ds.getInt(colname));
      case Variant.SHORT:
        return where(colname, condition, ds.getShort(colname));
      case Variant.BIGDECIMAL:
        return where(colname, condition, ds.getBigDecimal(colname));
      case Variant.TIMESTAMP:
        return where(colname, condition, ds.getTimestamp(colname));
      default:
        throw new UnsupportedOperationException("Condition.where() ne podrzava "+
          Variant.typeName(ds.getColumn(colname).getDataType()));
    }
  }
  
  public static Condition emptyString(String colname) {
    return where(colname, EQUAL, "");
  }
  
  public static Condition anyString(String colname) {
    return where(colname, NOT_EQUAL, "").andNotNull();
  }
  
  public static Condition emptyString(String colname, boolean key) {
    if (key) return Condition.emptyString(colname);
    return where(colname, EQUAL, "").or(where(colname, EQUAL, (String) null));
  }

  public static Condition equal(String colname, String value) {
    return where(colname, EQUAL, value);
  }

  public static Condition equal(String colname, int value) {
    return where(colname, EQUAL, value);
  }

  public static Condition equal(String colname, short value) {
    return where(colname, EQUAL, value);
  }
  
  public static Condition equal(String colname, ReadRow ds) {
    return equal(colname, ds, colname);
  }

  public static Condition equal(String colname, ReadRow ds, String dsc) {
    switch (ds.getColumn(dsc).getDataType()) {
      case Variant.STRING:
        return where(colname, EQUAL, ds.getString(dsc));
      case Variant.INT:
        return where(colname, EQUAL, ds.getInt(dsc));
      case Variant.SHORT:
        return where(colname, EQUAL, ds.getShort(dsc));
      case Variant.TIMESTAMP:
        return where(colname, EQUAL, ds.getTimestamp(dsc));
      default:
        throw new UnsupportedOperationException("Condition.equal() ne podrzava "+
          Variant.typeName(ds.getColumn(dsc).getDataType()));
    }
  }
  
  public static Condition diff(String colname, String value) {
    return where(colname, NOT_EQUAL, value);
  }

  public static Condition diff(String colname, int value) {
    return where(colname, NOT_EQUAL, value);
  }

  public static Condition diff(String colname, short value) {
    return where(colname, NOT_EQUAL, value);
  }

  public static Condition diff(String colname, ReadRow ds) {
    switch (ds.getColumn(colname).getDataType()) {
      case Variant.STRING:
        return where(colname, NOT_EQUAL, ds.getString(colname));
      case Variant.INT:
        return where(colname, NOT_EQUAL, ds.getInt(colname));
      case Variant.SHORT:
        return where(colname, NOT_EQUAL, ds.getShort(colname));
      case Variant.TIMESTAMP:
        return where(colname, NOT_EQUAL, ds.getTimestamp(colname));
      default:
        throw new UnsupportedOperationException("Condition.diff() ne podrzava "+
          Variant.typeName(ds.getColumn(colname).getDataType()));
    }
  }
  
  public static Condition till(String colname, Timestamp toValue) {
    return new SimpleCondition(TILL, colname, Util.getUtil().getLastSecondOfDay(toValue));
  }
  
  public static Condition till(String colname, ReadRow ds) {
    return till(colname, ds.getTimestamp(colname));
  }
  
  public static Condition from(String colname, Timestamp fromValue) {
    return new SimpleCondition(FROM, colname, Util.getUtil().getFirstSecondOfDay(fromValue));
  }
  
  public static Condition from(String colname, ReadRow ds) {
    return from(colname, ds.getTimestamp(colname));
  }
  
  public static Condition on(String colname, Timestamp day) {
    return new SimpleCondition(colname, day, day);
  }
  
  public static Condition on(String colname, ReadRow ds) {
    return on(colname, ds.getTimestamp(colname));
  }

  public static Condition between(String colname, 
            ReadRow ds, String colFrom, String colTo) {
    switch (ds.getColumn(colFrom).getDataType()) {
      case Variant.TIMESTAMP:
        return new SimpleCondition(colname, 
            ds.getTimestamp(colFrom), ds.getTimestamp(colTo));
      case Variant.INT:
        return new SimpleCondition(colname, 
            new Integer(ds.getInt(colFrom)), new Integer(ds.getInt(colTo)));
      case Variant.BIGDECIMAL:
        return new SimpleCondition(colname, 
            ds.getBigDecimal(colFrom), ds.getBigDecimal(colTo));
      case Variant.STRING:
        return new SimpleCondition(colname, 
            ds.getString(colFrom), ds.getString(colTo));
      case Variant.SHORT:
        return new SimpleCondition(colname, 
            new Short(ds.getShort(colFrom)), new Short(ds.getShort(colTo)));
      default:
        throw new UnsupportedOperationException("Condition.between() ne podrzava "+
          Variant.typeName(ds.getColumn(colname).getDataType()));
    }
  }
  
  public static Condition between(String colname, Timestamp fromValue, Timestamp toValue) {
    return new SimpleCondition(colname, fromValue, toValue);
  }

  public static Condition between(String colname, Object fromValue, Object toValue) {
    return new SimpleCondition(colname, fromValue, toValue);
  }

  public static Condition between(String colname, int fromValue, int toValue) {
    return new SimpleCondition(colname, new Integer(fromValue), new Integer(toValue));
  }

  public static Condition between(String colname, short fromValue, short toValue) {
    return new SimpleCondition(colname, new Short(fromValue), new Short(toValue));
  }

  public static Condition between(String colname, double fromValue, double toValue) {
    return new SimpleCondition(colname, new BigDecimal(fromValue), new BigDecimal(toValue));
  }

  public static Condition isNull(String colname) {
    return new SimpleCondition(EQUAL, colname, null);
  }
  
  public static Condition notNull(String colname) {
    return new SimpleCondition(NOT_EQUAL, colname, null);
  }
  
  public Condition andNotNull(String colname) {
    return new CompoundCondition(this, AND, notNull(colname));
  }
  
  public Condition orNull(String colname) {
    return new CompoundCondition(this, OR, isNull(colname));
  }
  
  public abstract Condition andNotNull();
  
  public abstract Condition orNull();
  
  abstract int getType();
  
  public Condition and(Condition other) {
    if (other == none) return this;
    else if (this == none) return other;
    else return new CompoundCondition(this, AND, other);
  }
  public Condition or(Condition other) {
    if (other == none) return this;
    else if (this == none) return other;
    else return new CompoundCondition(this, OR, other);
  }
  public Condition not() {
    if (this == none)
      throw new UnsupportedOperationException("Negacija nul uvjeta");
    if (this instanceof CompoundCondition)
      if (((CompoundCondition) this).type == NOT)
        throw new UnsupportedOperationException("Dvostruka negacija");
    return new CompoundCondition(this, NOT, null);
  }

  public abstract Condition qualified(String table);
  
  public boolean equals(Object other) {
    if (other instanceof Condition)
      return toString().equals(other.toString());
    return false;
  }

  public static void main(String[] args) {
    System.out.println(Condition.equal("CART", 5));
    System.out.println(Condition.equal("CORG", "11").
                       and(Condition.between("CART", 1, 1000)));

    Condition c = Condition.equal("CORG", "1").and(Condition.equal("CPAR", 1)).
                       and(Condition.equal("VRDOK", "PRK")).and(Condition.equal("RBR", 1));

    System.out.println(c);
    System.out.println(Condition.between("DATDOK",
      Util.getUtil().getFirstDayOfYear(),
      Valid.getValid().getToday()));

    System.out.println(Condition.in("VRDOK", new String[] {"ROT", "GOT"}));
    System.out.println(Condition.in("BRDOK", new int[] {1,2,3,4,5,6}).not());
    System.out.println(Condition.in("VRDOK", new String[] {"ROT"}));
  }
}


class SimpleCondition extends Condition {

  private String del;
  private String column;
  private String table;

  private Object val;

  private int condition;

  public SimpleCondition() {}
  
  protected SimpleCondition(SimpleCondition copy, int nc) {
  	condition = nc;
  	table = copy.table;
  	del = copy.del;
  	val = copy.val;
  	column = copy.column;
  }
  
  protected SimpleCondition(String raw) {
    val = raw;
    condition = RAW;
  }

  protected SimpleCondition(int cond, String colname, Object value) {
    condition = cond;
    column = colname;
    val = value;
    del = (value instanceof String || value instanceof Timestamp ||
           value instanceof String[] || (value instanceof Object[] && 
              Array.get(value, 0) instanceof String)) ? "'" : "";
  }

  protected SimpleCondition(String colname, Timestamp value, Timestamp evalue) {
    column = colname;
    condition = IN_RANGE;
    val = new Object[] {Util.getUtil().getFirstSecondOfDay(value),
                        Util.getUtil().getLastSecondOfDay(evalue)};
    del = "'";
  }

  protected SimpleCondition(String colname, Object value, Object evalue) {
    column = colname;
    condition = IN_RANGE;
    val = new Object[] {value, evalue};
    del = (value instanceof String) ? "'" : "";
  }
  
  public Condition andNotNull() {
    return new CompoundCondition(this, AND, notNull(column)).forceBrackets();
  }
  
  public Condition orNull() {
    return new CompoundCondition(this, OR, isNull(column)).forceBrackets();
  }
  
  public Condition not() {  	
  	if (condition == EQUAL) return new SimpleCondition(this, NOT_EQUAL);
  	if (condition == NOT_EQUAL) return new SimpleCondition(this, EQUAL);
  	if (condition == LESS_THAN) return new SimpleCondition(this, GREATER_OR_EQUAL);
  	if (condition == LESS_OR_EQUAL) return new SimpleCondition(this, GREATER_THAN);
  	if (condition == GREATER_OR_EQUAL) return new SimpleCondition(this, LESS_THAN);
  	if (condition == GREATER_THAN) return new SimpleCondition(this, LESS_OR_EQUAL);
  	return super.not();
  }
  
  int getType() {
    return SIMPLE;
  }

  private String createList(VarStr st) {
    st.append(" IN (");
    boolean overflow = false;
    int l = Array.getLength(val);
    for (int i = 0, n = 0; i < l; i++, n++) {
      if (n > MAXINQUERY) {
        String qual = (table == null)?"":table+".";
        st.chop().append(')').append(" OR ").append(qual).append(column).append(" IN (");
        n = 0;
        overflow = true;
      }
      st.append(del).append(Array.get(val, i)).append(del).append(',');
    }
    st.chop().append(')');
    if (overflow) st.insert(0, '(').append(')');
//    System.err.println("*** Condition.in (l=" +l+")*** "+st.toString());
    return st.toString();
  }

  public Condition qualified(String table) {
    this.table = table;
    return this;
  }

  public String toString() {
    if (condition == RAW) return "(" + val + ")";
    if (column == null) return "";
    VarStr ret = new VarStr(column);
    if (table != null) ret.insert(0, '.').insert(0, table);
    if (condition == EQUAL && val == null)
      return ret.append(" IS NULL").toString();
    else if (condition == NOT_EQUAL && val == null)
      return ret.append(" IS NOT NULL").toString();
    else if (condition == IN_RANGE)
      return ret.append(" BETWEEN ").append(del).
          append(((Object[]) val)[0]).append(del).append(" AND ").
          append(del).append(((Object[]) val)[1]).append(del).toString();
    else if (condition == IN_LIST) return createList(ret);
    else return ret.append(ops[condition]).append(del).
      append(val).append(del).toString();
  }
}


class CompoundCondition extends Condition {

  private Condition first, second;
  private boolean brackets;
  protected int type;

  public CompoundCondition(Condition c1, int operator, Condition c2) {
    first = c1;
    second = c2;
    type = operator;
  }

  public Condition qualified(String table) {
    first.qualified(table);
    if (second != null) second.qualified(table);
    return this;
  }
  
  public Condition andNotNull() {
    throw new UnsupportedOperationException("CompoundCondition doesn't support implicit notNull");
  }
  
  public Condition orNull() {
    throw new UnsupportedOperationException("CompoundCondition doesn't support implicit isNull");    
  }
  
  int getType() {
    return type;
  }
  
  Condition forceBrackets() {
    brackets = true;
    return this;
  }

  public String toString() {
    VarStr ret = new VarStr();
    if (type == NOT) return ret.append(opc[NOT]).append(first).toString();
    if (first.getType() <= type) ret.append(first);
    else ret.append('(').append(first).append(')');
    ret.append(opc[type]);
    if (second.getType() <= type) ret.append(second);
    else ret.append('(').append(second).append(')');
    if (brackets) ret.insert(0, '(').append(')');
    return ret.toString();
  }
}
