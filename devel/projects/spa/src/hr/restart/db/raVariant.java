/****license*****************************************************************
**   file: raVariant.java
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
package hr.restart.db;

import hr.restart.util.VarStr;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.Variant;
public class raVariant {
/*
  private java.math.BigDecimal vBigDecimal;
  private boolean vBoolean;
  private byte vByte;
  private byte[] vByteArray;
  private java.sql.Date vDate;
  private double vDouble;
  private float vFloat;
  private int vInt;
  private long vLong;
  private short vShort;
  private String vString;
  private java.sql.Time vTime;
  private java.sql.Timestamp vTimestamp;
*/
  private static boolean debug = false;
  
  public static void setDebug(boolean debug) {
    raVariant.debug = debug;
  }

  protected raVariant() {
  }

  public static Object getDataSetValue(ReadRow ds, String colNm) {
    Column col = ds.hasColumn(colNm);
    if (col == null) return null;
    try {
      String methodName = "get".concat(getTypeInString(col));
      Method meth = ds.getClass().getMethod(methodName,new Class[] {String.class});
      return meth.invoke(ds,new Object[] {colNm});
    }
    catch (Exception ex) {
      if (debug) System.out.println("raVariant.getDataSetValue err:"+ex);
      return null;
    }
  }

  public static void setDataSetValue(ReadWriteRow ds,String colNm,Object value) {
    setDataSetValue(ds,colNm,value,getType(value));
  }

  public static void setDataSetValue(ReadWriteRow ds,String colNm,boolean value) {
    setDataSetValue(ds,colNm,new Boolean(value),boolean.class);
  }

  public static void setDataSetValue(ReadWriteRow ds,String colNm,byte value) {
    setDataSetValue(ds,colNm,new Byte(value),byte.class);
  }

  public static void setDataSetValue(ReadWriteRow ds,String colNm,double value) {
    setDataSetValue(ds,colNm,new Double(value),double.class);
  }

  public static void setDataSetValue(ReadWriteRow ds,String colNm,float value) {
    setDataSetValue(ds,colNm,new Float(value),float.class);
  }

  public static void setDataSetValue(ReadWriteRow ds,String colNm,int value) {
    setDataSetValue(ds,colNm,new Integer(value),int.class);
  }

  public static void setDataSetValue(ReadWriteRow ds,String colNm,long value) {
    setDataSetValue(ds,colNm,new Long(value),long.class);
  }

  public static void setDataSetValue(ReadWriteRow ds,String colNm,short value) {
    setDataSetValue(ds,colNm,new Short(value),short.class);
  }

  public static void setDataSetValue(ReadWriteRow ds,String colNm,Object value, Class type) {
    Column col = ds.hasColumn(colNm);
    if (col == null) return;
    String methodName = "set".concat(getTypeInString(col));
    try {
      Method meth = ds.getClass().getMethod(methodName,new Class[] {String.class,type});
      meth.invoke(ds,new Object[] {colNm,value});
    }
    catch (Exception ex) {
      if (debug) System.out.println("raVariant.setDataSetValue => "+methodName+"("+colNm+","+value+")"+" err:"+ex);
    }
  }
/**
 * ako je val instanca jednog od objekata koji zamjenjuje primitivne tipova vraca taj primitivni tip
 * ako nije vraca val.getClass(). npr getType(new Integer(7)) vraca int.class
 * @param val
 * @return
 */
  public static Class getType(Object val) {
    if (val == null) return null;
    if (val instanceof java.lang.Boolean) return boolean.class;
    if (val instanceof java.lang.Byte) return byte.class;
    if (val instanceof java.lang.Double) return double.class;
    if (val instanceof java.lang.Float) return float.class;
    if (val instanceof java.lang.Integer) return int.class;
    if (val instanceof java.lang.Long) return long.class;
    if (val instanceof java.lang.Short) return short.class;
    return val.getClass();
  }
  
  private static String getTypeInString(Column col) {
    Variant v=null;
    
    int dataType = col.getDataType();
    if (dataType == v.BIGDECIMAL) return "BigDecimal";
    if (dataType == v.BOOLEAN) return "Boolean";
    if (dataType == v.BYTE) return "Byte";
    if (dataType == v.DATE) return "Date";
    if (dataType == v.DOUBLE) return "Double";
    if (dataType == v.FLOAT) return "Float";
    if (dataType == v.INT) return "Int";
    if (dataType == v.LONG) return "Long";
    if (dataType == v.OBJECT) return "Object";
    if (dataType == v.SHORT) return "Short";
    if (dataType == v.STRING) return "String";
    if (dataType == v.TIME) return "Time";
    if (dataType == v.TIMESTAMP) return "Timestamp";
    return "";
  }
  
  /**
   * konvertira string val u objekt koji bi se mogao setirati u kolonu col
   * npr: convertFor("122.34", col) gdje je col.getDataType()==Variant.BIGDECIMAL
   * konstruira i vraca objekt new BigDecimal("122.34")
   * @param val
   * @param col
   * @return
   */
  public static Object convertFor(String val, Column col) {
    try {
      if (debug) System.out.println("Converting value "+val);
      String sclass = getTypeInString(col);
      if (sclass.equals("Int")) {
        sclass = "Integer";
      }
      if (sclass.equals("Integer") || sclass.equals("Short") || sclass.equals("Long")) {
        if (val.indexOf('.')>0) val = val.substring(0,val.indexOf('.'));        
      }
      String pckge = sclass.equals("BigDecimal")?"java.math.":
        (sclass.equals("Date")
            ||sclass.equals("Time")
            ||sclass.equals("Timestamp"))?"java.sql.":
              "java.lang.";
      Class[] clparams = new Class[] {String.class};
      Object[] clargs = new Object[] {val};
      Long l = null;
      if (pckge.equals("java.sql.")) {
        try {
          l = Long.valueOf(Timestamp.valueOf(val).getTime()+"");
        } catch (IllegalArgumentException e1) {
          if (debug) System.out.println("Not Timestamp");          
          try {
            l = Long.valueOf(Time.valueOf(val).getTime()+"");
          } catch (IllegalArgumentException e2) {
            if (debug) System.out.println("Not Time");          
            try {
              l = Long.valueOf(Date.valueOf(val).getTime()+"");
            } catch (IllegalArgumentException e3) {
              if (debug) System.out.println("Not date");          
              l = null;
            }
          }
        }
        if (l != null) {
          if (debug) System.out.println(val + " succesfuly converted to long "+l);
          clparams = new Class[] {long.class};
          clargs = new Object[] {l};
        }
      }
      try {
        if (debug) System.out.println("trying "+pckge+sclass+".valueOf("+clargs[0]+")");
        return Class.forName(pckge+sclass)
          .getMethod("valueOf", clparams)
          .invoke(null, clargs);
      } catch (Exception nosme) {
        if (debug) System.out.println("trying new "+pckge+sclass+"("+clargs[0]+")");        
        return Class.forName(pckge+sclass)
        .getConstructor(clparams)
        .newInstance(clargs);        
      }
/*      
*/
    } catch (Exception e) {
      e.printStackTrace();
      return val;
    }
  }
  
  /**
   * Kopira vrijednosti iz src u dest i to samo one koje postoje u nizu destdef 
   * iz kolona pod istim rednim brojem u src-u
   * npr: imamo <code>ReadRow s</code> koji sadrzi kolone "id", "oznaka", "naziv", "opis" 
   * i kopiramo ga u <code>ReadWriteRow d</code> koji sadrzi kolone "key", "name", "value".
   * Metodom copyTo(s, d, "a, key, name") ce se kopirati vrijednosti oznaka -> key, naziv -> name
   * @param src
   * @param dest
   * @param destdef
   */
  public static void copyTo(ReadRow src, ReadWriteRow dest, String destdef) {
    String[] destcols = new VarStr(destdef).split(',');
    String[] srccolumns = src.getColumnNames(src.getColumnCount());
    for (int x = 0; x < destcols.length; x++) {
      Column destcol = dest.hasColumn(destcols[x]); 
      if (destcol != null) {
        Object val = raVariant.convertFor(
            raVariant.getDataSetValue(src, srccolumns[x]).toString(), destcol);
        raVariant.setDataSetValue(dest, destcols[x], val);
      }
    }
  }
}