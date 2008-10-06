/****license*****************************************************************
**   file: raSQLSet.java
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

public abstract class raSQLSet extends raValueSet {
  private String query;
  private java.sql.Connection connection;
  private java.sql.ResultSetMetaData metadata;
  private String tableName;
  private String keys[];
  private boolean saveChangesEnabled;

  public raSQLSet() {
  }
  public raSQLSet(String sqlQuery) {
    setQuery(sqlQuery);
  }
  public void finalize() throws Throwable {
    query = null;
    connection = null;
    metadata = null;
    tableName = null;
    keys = null;
    super.finalize();
  }

  public abstract void open() throws Exception;

  public abstract void saveChanges() throws Exception;

  public abstract boolean moreData() throws Exception;

/*
  informacija
*/

  public java.sql.ResultSetMetaData getMetadata() {
    return metadata;
  }

  public void setMetaData(java.sql.ResultSetMetaData mtd) {
    try {
      if (mtd instanceof raResultSetMetaData) {
        metadata = mtd;
      } else {
        metadata = new raResultSetMetaData(mtd);
      }
      getMetaDataInfo();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void getMetaDataInfo() throws Exception {
    String[] cols = new String[getMetadata().getColumnCount()];
    for (int i = 0; i < cols.length; i++) {
      cols[i] = getMetadata().getColumnName(i+1);
    }
    setColNames(cols);

    tableName = hr.restart.util.Valid.getTableName(query);
    if (tableName.indexOf(",")>0) {
      keys = null;
      return;
    }
    keys = raConnectionFactory.getKeyColumns(connection,tableName);
//    java.sql.ResultSet reskys = connection.getMetaData().getPrimaryKeys(null,null,tableName);
//    java.util.HashSet kys = new java.util.HashSet();
//    while (reskys.next()) {
//      kys.add(reskys.getString("COLUMN_NAME"));
//    };
//    keys = new String[kys.size()];
//    keys = (String[])kys.toArray(keys);
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String qry) {
    if (!qry.equalsIgnoreCase(query)) close();
    query = qry;
  }

  public void setConnection(java.sql.Connection connection) {
    this.connection = connection;
  }

  public java.sql.Connection getConnection() {
    return connection;
  }

  public String[] getKeys() {
    return keys;
  }

  public void setKeys(String[] kys) {
    keys = kys;
  }

  public boolean isSaveChangesEnabled() {
    return saveChangesEnabled;
  }

  public void setSaveChangesEnabled(boolean enab) {
    saveChangesEnabled = enab;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String name) {
    if (tableName==null) tableName = name;
  }

  public void checkForSave() throws Exception {
//    if (connection == null) throw new NullPointerException("Not connected");
    if (tableName == null) throw new NullPointerException("No table name, unable to save changes");
    if (getColNames() == null) throw new NullPointerException("Table has no columns, unable to save changes");
    if (keys == null) throw new NullPointerException("Table has no keys, unable to save changes");
  }

  public String getUpdateString() {
    String ret = "UPDATE ".concat(tableName).concat(" SET ");
    for (int i = 0; i < getColNames().length; i++) {
      ret = ret.concat(getColNames()[i]).concat(" = ?, ");
    }
    ret = ret.substring(0,ret.length()-2);
    return ret;
  }

  public String getDeleteString() {
    return "DELETE FROM ".concat(tableName);
  }

  public String getInsertString() {
    String ret = "INSERT INTO ".concat(tableName).concat(" VALUES(");
    for (int i = 0; i < getColNames().length; i++) {
      ret = ret.concat("?, ");
    }
    ret = ret.substring(0,ret.length()-2).concat(")");
    return ret;
  }

  public String getWhereString() {
    String ret = " WHERE ";
    for (int i = 0; i < keys.length; i++) {
      ret = ret.concat(tableName).concat(".").concat(keys[i]).concat(" = ? AND ");
    }
    ret = ret.substring(0,ret.length()-5);
    return ret;
  }
}