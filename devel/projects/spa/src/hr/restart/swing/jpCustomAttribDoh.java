/****license*****************************************************************
**   file: jpCustomAttribDoh.java
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
package hr.restart.swing;

import hr.restart.util.VarStr;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.NoSuchElementException;

import com.borland.dx.dataset.DataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class jpCustomAttribDoh extends jpCustomAttrib {

  String attrDoh;
  String attrDohKol;
  DataSet atm;

  public void setDohvatCols(String doh, String dohKol) {
    attrDoh = doh;
    attrDohKol = dohKol;
  }

  private void locAttr(short cznac) {
    if (!ld.raLocate(atm = attrDm.getQueryDataSet(), attrKey, String.valueOf(cznac)))
      throw new NoSuchElementException("Nepostojeæa znaèajka "+cznac);
  }

  public DataSet getSifrarnikDataSet(short cvrsubj, short cznac) {
    try {
      locAttr(cznac);
      System.out.println(atm);
      String[] def = new VarStr(atm.getString(attrDoh)).split(';');
      Object singleton = null;
      Class dohClass = null;
      try {
        dohClass = Class.forName(def[0]);
      } catch (ClassNotFoundException e) {
        int lastDot = def[0].lastIndexOf('.');
        String cl = def[0].substring(0, lastDot);
        String get = def[0].substring(lastDot + 1);
        dohClass = Class.forName(cl);
        Method m = dohClass.getMethod(get, null);
        singleton = m.invoke(null, null);
      }
      Class[] paramTypes = new Class[def.length - 2];
      Object[] params = new Object[def.length - 2];
      for (int i = 0; i < def.length - 2; i++) {
        String[] cv = new VarStr(def[i + 2]).split(':');
        paramTypes[i] = findClass(cv[0]);
        params[i] = valueOf(paramTypes[i], cv[1]);
      }
      Method m = dohClass.getMethod(def[1], paramTypes);
      return (DataSet) m.invoke(singleton, params);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public int[] getSifrarnikVisibleCols(short cvrsubj, short cznac) {
    locAttr(cznac);
    String cols = new VarStr(atm.getString(attrDohKol)).split(';')[2];
    String[] cl = new VarStr(cols).split(',');
    int[] ic = new int[cl.length];
    for (int i = 0; i < ic.length; i++)
      ic[i] = Integer.parseInt(cl[i]);
    return ic;
  }

  public String getSifrarnikValue(short cvrsubj, short cznac) {
    locAttr(cznac);
    return new VarStr(atm.getString(attrDohKol)).split(';')[0];
  }

  public String getSifrarnikDesc(short cvrsubj, short cznac) {
    locAttr(cznac);
    return new VarStr(atm.getString(attrDohKol)).split(';')[1];
  }

  private Class findClass(String name) {
    Class[] classes = {String.class, Integer.TYPE, Character.TYPE, BigDecimal.class,
      Short.TYPE, Long.TYPE, Double.TYPE, Float.TYPE, Timestamp.class};
    for (int i = 0; i < classes.length; i++) {
      String cn = classes[i].getName();
      if (cn.substring(cn.lastIndexOf('.') + 1).equalsIgnoreCase(name))
        return classes[i];
    }
    return null;
  }

  private Object valueOf(Class c, String s) {
    try {
      if (c == String.class)
        return s;
      else if (c == Integer.TYPE)
        return Integer.valueOf(s);
      else if (c == Character.TYPE)
        return new Character(s.charAt(0));
      else if (c == BigDecimal.class)
        return new BigDecimal(s);
      else if (c == Short.TYPE)
        return Short.valueOf(s);
      else if (c == Long.TYPE)
        return Long.valueOf(s);
      else if (c == Double.TYPE)
        return Double.valueOf(s);
      else if (c == Float.TYPE)
        return Float.valueOf(s);
      else if (c == Timestamp.class)
        try {
          return Timestamp.valueOf(s);
        } catch (IllegalArgumentException e) {
          return new Timestamp(java.sql.Date.valueOf(s).getTime());
        }
      return null;
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }
}

