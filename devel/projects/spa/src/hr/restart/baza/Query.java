/****license*****************************************************************
**   file: Query.java
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

import java.util.ArrayList;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public abstract class Query {
  protected String[] aliases;

  static {
    dM.getDataModule().loadModules();
  }

  protected Query() {}

  public static Query allColumns() {
    return all();
  }

  public static Query all() {
    return new SingleQuery();
  }

  public static Query column(String col) {
    return new SingleQuery(new String[] {col});
  }

  public static Query columns(String[] cols) {
    return new SingleQuery(cols);
  }

//  public abstract Query table(String table);
//  public abstract Query tables(String table);
  public abstract Query join(String table);
  public abstract Query join(String table, String[] mainkeys, String[] joinkeys);

  public Query join(String table, String[] keys) {
    return join(table, keys, keys);
  }

  public Query join(String table, String key) {
    return join(table, new String[] {key});
  }

  public Query join(String table, String mainkey, String joinkey) {
    return join(table, new String[] {mainkey}, new String[] {joinkey});
  }

//  public abstract Query aliases() {
//  }

  public static void main(String[] args) {

//    Query.table("doki", "stdoki").join("artikli", "cart")
//        columns("cart,nazart,bc")
  }
}

class JoinCondition {
  private String tab1, tab2, k1, k2;

  public JoinCondition(String table1, String table2, String key1, String key2) {
    tab1 = table1.toLowerCase();
    tab2 = table2.toLowerCase();
    k1 = key1.toLowerCase();
    k2 = key2.toLowerCase();
  }

  public boolean equals(Object other) {
    if (other instanceof JoinCondition) {
      JoinCondition o = (JoinCondition) other;
      return k1.equalsIgnoreCase(o.k1) && k2.equalsIgnoreCase(o.k2) &&
          tab1.equalsIgnoreCase(o.tab1) && tab2.equalsIgnoreCase(o.tab2);
    } else return false;
  }

  public String toString() {
    return tab1 + "." + k1 + "=" + tab2 + "." + k2;
  }
}

class SingleQuery extends Query {
  private ArrayList columns, tables, joins;
  private Condition filter;

  private void checkTable(String table) {
    if (KreirDrop.getModuleByName(table) == null)
      throw new IllegalArgumentException("Non existant table '" + table + "'");
    if (tables.contains(table.toLowerCase()))
      throw new IllegalArgumentException("Duplicate table '" + table + "'");
  }

  private void addTable(String table) {
    checkTable(table);
    tables.add(table.toLowerCase());
  }

  private void addJoin(String t1, String t2, String k) {
    addJoin(t1, t2, k, k);
  }

  private void addJoin(String t1, String t2, String k1, String k2) {
    JoinCondition j = new JoinCondition(t1, t2, k1, k2);
    if (joins.contains(j))
      throw new IllegalArgumentException("Duplicate join condition '"+k1+"'");
    joins.add(j);
  }

  private String findJoinTable(String table, String mainkey, String joinkey) {
    Column col = KreirDrop.getModuleByName(table).getQueryDataSet().hasColumn(joinkey);
    if (col == null)
      throw new IllegalArgumentException("Table '"+table+"' doesn't have column '"+joinkey+"'");
    for (int i = 0; i < tables.size(); i++) {
      Column m = KreirDrop.getModuleByName((String) tables.get(i)).
                 getQueryDataSet().getColumn(mainkey);
      if (m != null && (m.isRowId() || col.isRowId()))
        return (String) tables.get(i);
    }
    return null;
  }

  private void findNaturalJoin(String table) {
    DataSet ds = KreirDrop.getModuleByName(table).getQueryDataSet();
    String[] cols = ds.getColumnNames(ds.getColumnCount());
    for (int i = 0; i < cols.length; i++) {
      String t = findJoinTable(table, cols[i], cols[i]);
      if (t != null) addJoin(t, table, cols[i]);
    }
  }

  public SingleQuery() {
    aliases = null;
  }

  public SingleQuery(String[] cols) {
    aliases = cols;
  }

  public Query join(String table) {
    checkTable(table);
    findNaturalJoin(table);
    addTable(table);
    return this;
  }

  public Query join(String table, String[] mainkeys, String[] joinkeys) {
    checkTable(table);
    for (int i = 0; i < joinkeys.length; i++) {
      String t = findJoinTable(table, mainkeys[i], joinkeys[i]);
      if (t == null)
        throw new IllegalArgumentException("Invalid join column '"+joinkeys[i]+"'");
      addJoin(t, table, mainkeys[i], joinkeys[i]);
    }
    addTable(table);
    return this;
  }

  public String toString() {
    return "";
  }
}
