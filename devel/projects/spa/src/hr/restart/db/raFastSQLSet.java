/****license*****************************************************************
**   file: raFastSQLSet.java
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class raFastSQLSet extends raSQLSet {
  private PreparedStatement updstmt;
  private PreparedStatement insstmt;
  private PreparedStatement delstmt;

  public raFastSQLSet() {
  }
  public raFastSQLSet(String sqlQuery) {
    setQuery(sqlQuery);
  }
  public void finalize() throws Throwable {
    updstmt = null;
    insstmt = null;
    delstmt = null;
    super.finalize();
  }
  private void parseResultSet(java.sql.ResultSet resultSet) throws Exception {
//    rows.clear();
    clearAllRows();
    while (resultSet.next()) {
      addRow(resultSet);
    }
    first();
  }

  private void addRow(java.sql.ResultSet resultSet) throws Exception {
    raValueRow row = new raValueRow();
    Object[] vals = new Object[getMetadata().getColumnCount()];
    for (int i = 0; i < getMetadata().getColumnCount(); i++) {
      vals[i] = resultSet.getObject(i+1);
    }
    row.setValues(vals);
    addRow(row);
  }

  public void open() throws Exception {
    if (isOpen()) return;
    if (getQuery() == null) throw new IllegalArgumentException("query string must be set");
//    if (getConnection() == null) setConnection(raConnectionFactory.getConnection());
    if (getConnection() == null) setConnection(raConnectionFactory.openDefaultConnection());
    java.sql.ResultSet resultSet = getConnection().createStatement().executeQuery(getQuery());
    getInfo(resultSet);
    parseResultSet(resultSet);
    setOpenFlag();
  }

  public void saveChanges() throws Exception {
    if (!isSaveChangesEnabled()) return;
    hr.restart.util.raLocalTransaction transaction = new hr.restart.util.raLocalTransaction() {
      public boolean transaction() throws Exception {
        for (int i = 0; i < getRows().size(); i++) {
          executeStatement((raValueRow)getRows().get(new Integer(i+1)));
        }
        return true;
      }
    };
    boolean succ;
    if (getConnection().getAutoCommit()) { // nije startana transakcija
      succ = transaction.execTransaction();
    } else {
      try {
        succ = transaction.transaction();
      }
      catch (Exception ex) {
        succ = false;
      }
    }
    if (!succ) throw new SQLException("Neuspjela transakcija");
    refresh(); // najuniverzalnije rješenje jer kada mu client set posalje rows unutra su samo izmjenjeni rowovi
  }

  public boolean moreData() throws Exception {
    return false;
  }

  private void executeStatement(raValueRow row) throws Exception {
    try {
      PreparedStatement stmt;
      if (row.getState() == 'U') { //updatin'
        stmt = updstmt;
        //fill values
        fillStatement(stmt,getColNames(),row,1);
        //fill keys
        fillStatement(stmt,getKeys(),row,getColNames().length+1);
      } else if (row.getState() == 'A') { //addin'
        stmt = insstmt;
        //fill values
        fillStatement(stmt,getColNames(),row,1);
      } else if (row.getState() == 'D') { //deletin'
        stmt = delstmt;
        //fill keys
        fillStatement(stmt,getKeys(),row,1);
      } else return;
      stmt.execute();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

  private void fillStatement(PreparedStatement stmt, String[] columns, raValueRow row, int beginIndex) throws Exception {
    for (int i = 0; i < columns.length; i++) {
      int colidx = findColumn(columns[i]);
//System.out.println("for column "+columns[i]);
      int parIndex = i+beginIndex;
      Object valObject = row.getValue(colidx);
      int colType = getMetadata().getColumnType(colidx+1);
      int colScale = getMetadata().getScale(colidx+1);
      if (valObject == null) {
//System.out.println("stmt.setNull("+parIndex+","+colType);
        stmt.setNull(parIndex,colType);
      } else {
//System.out.println("stmt.setObject("+parIndex+","+valObject+","+colType+","+colScale);
        stmt.setObject(parIndex,valObject,colType,colScale);
      }
    }
  }
/*
  informacija
*/

  private void getInfo(ResultSet resultSet) throws Exception {
    setMetaData(resultSet.getMetaData());
    setSaveChangesEnabled(prepareStatemens());
  }

  private boolean prepareStatemens() {
    try {
      checkForSave();
      hr.restart.util.raTransaction trans = null;
      String whereString = getWhereString();
      String updateString = getUpdateString().concat(whereString);
      String insertString = getInsertString();
      String deleteString = getDeleteString().concat(whereString);
      updstmt = trans.getPreparedStatement(updateString,getConnection());
      insstmt = trans.getPreparedStatement(insertString,getConnection());
      delstmt = trans.getPreparedStatement(deleteString,getConnection());
//      System.out.println(updstmt+" "+updateString);
//      System.out.println(insstmt+" "+insertString);
//      System.out.println(delstmt+" "+deleteString);
      if (updstmt == null || insstmt == null || delstmt == null) throw new NullPointerException("Could not create statements for sql manipulation");
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }

}