/****license*****************************************************************
**   file: raPreparedStatement.java
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



import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;



public class raPreparedStatement {//implements PreparedStatement {

  private PreparedStatement statement;

  private String whereSql;

  private String updateSql;

  private String insertSql;

  private String tableName;

  private Connection connection;

  private String statementSql;

  private String[] columnNames;

  private String[] keyColumnNames;



  /**

   * kreira statement koji radi select count(*) from tabela where keys = ?, gdje je najsvrsishodnija

   * operacija raPrepareStatement.setKeys(ReadRow); if (raPrepareStatement.isExist()) ...

   */

  public static int COUNT = 0;



  /**

   * kreira statement sa update querijem

   */

  public static int UPDATE = 1;



  /**

   * kreira statement sa insert queryjem

   */

  public static int INSERT = 2;



  /**

   * kreira statement sa delete queryjem

   */

  public static int DELETE = 3;

  private int mode;



  /**

   * kreira statement koji sadzi query na tablici _tableName ovisno o parametru _mode u defaultnoj bazi

   * @param _tableName - ime tablice za query

   * @param _mode - moze biti COUNT,UPDATE,INSERT,DELETE

   */

  public raPreparedStatement(String _tableName, int _mode) {

    try {

//      init(_tableName,_mode,raConnectionFactory.openDefaultConnection());

      init(_tableName,_mode,hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection());

    }

    catch (Exception ex) {

      ex.printStackTrace();

    }

  }



  /**

   * kreira statement koji sadzi query na tablici _tableName ovisno o parametru _mode u zadanoj bazi

   * @param _tableName - ime tablice za query

   * @param _mode - moze biti COUNT,UPDATE,INSERT,DELETE

   * @param con - konekcija (baza)

   */

  public raPreparedStatement(String _tableName, int _mode, Connection con) {

    init(_tableName,_mode,con);

  }



  /**

   * uzima iz dataseta ime tablice, i bazu i zove konstruktor raPreparedStatement(String _tableName, int _mode, Connection con)

   * @param qds QueryDataSet za koji trebamo statement

   * @param _mode - moze biti COUNT,UPDATE,INSERT,DELETE

   */

  public raPreparedStatement(com.borland.dx.sql.dataset.QueryDataSet qds, int _mode) {

    init(hr.restart.util.Valid.getTableName(qds.getQuery().getQueryString()),_mode,qds.getDatabase().getJdbcConnection());

  }



  /**

   * uzima iz raSQLSeta ime tablice, i bazu i zove konstruktor raPreparedStatement(String _tableName, int _mode, Connection con)

   * @param qds raSQLSet za koji trebamo statement

   * @param _mode - moze biti COUNT,UPDATE,INSERT,DELETE

   */

  public raPreparedStatement(raSQLSet qds, int _mode) {

    init(hr.restart.util.Valid.getTableName(qds.getQuery()),_mode,qds.getConnection());

  }


  // ab.f SORRY ANDREJ: Bilo je neophodno :)
  // izdvojio sam ono sto mi treba u POSEBNU metodu tako
  // da nista drugo ne moram dirati. No morao sam napraviti
  // i prazni konstruktor (privatni, pa nema stete)

  private raPreparedStatement() {}

  public static raPreparedStatement createIndependentInsert(String _table, String[] cols) {
    try {
      raPreparedStatement stm = new raPreparedStatement();
      stm.tableName = _table;
      stm.mode = INSERT;
      stm.connection = hr.restart.baza.dM.getDatabaseConnection();
      stm.keyColumnNames = stm.getKeyColumnsDB(stm.connection, _table);
      stm.columnNames = cols;  // zbog ovog mi treba: nepotpuni insert ako se tablica izmijeni
      stm.makeStatmentSql();
      stm.makeStatement();
      return stm;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private String[] getKeyColumnsDB(Connection c, String t) {
    String[] ante = raConnectionFactory.getKeyColumns(c,t);
    for (int i = 0; i < ante.length; i++) {
      ante[i] = ante[i].toUpperCase();
    }
    return ante;
  }
  private String[] getColumnsDB(Connection c, String t) {
    String[] ante = raConnectionFactory.getColumns(c,t);
    for (int i = 0; i < ante.length; i++) {
      ante[i] = ante[i].toUpperCase();
    }
    return ante;
  }

  private void init(String _tableName, int _mode, Connection con) {

    try {

      mode = _mode;

      tableName = _tableName;

      connection = con;

      keyColumnNames = getKeyColumnsDB(con,tableName);//raConnectionFactory.getKeyColumns(con,tableName);

      columnNames = getColumnsDB(con, tableName);//raConnectionFactory.getColumns(con,tableName);

      makeStatmentSql();

      makeStatement();

    }

    catch (Exception ex) {

      ex.printStackTrace();

    }

  }

  private void makeStatement() throws Exception {

    statement = connection.prepareStatement(statementSql);

  }



  /**

   * puni u statement vrijednosti iz zadanog row-a, ali NE u WHERE dio

   * @param row zadani row

   */

  public void setValues(com.borland.dx.dataset.ReadRow row) {
//  debug
//    System.out.println("raPSSV: "+statementSql );
//    System.out.println("***********************");
//    for (int i = 0; i < columnNames.length; i++) {
//      System.out.println(columnNames[i]+" = "+raVariant.getDataSetValue(row,columnNames[i]));
//    }
//    System.out.println("********************END");
//    gubed

    set_jb(row,columnNames,false);

  }



  /**

   * puni u statement vrijednosti iz zadanog row-a u WHERE dio

   * @param row zadani row

   */

  public void setKeys(com.borland.dx.dataset.ReadRow row) {
//debug
//System.out.println("raPSSK: "+statementSql);
//System.out.println("***********************");
//for (int i = 0; i < keyColumnNames.length; i++) {
//  System.out.println(keyColumnNames[i]+" = "+raVariant.getDataSetValue(row,keyColumnNames[i]));
//}
//System.out.println("********************END");
//gubed
    set_jb(row,keyColumnNames,true);

  }

  public void setValues(raSQLSet row) {

    set_ra(row,columnNames,false);

  }

  public void setKeys(raSQLSet row) {

    set_ra(row,keyColumnNames,true);

  }



  private void set_jb(com.borland.dx.dataset.ReadRow row,String[] cols, boolean where) {
    for (int i = 0; i < cols.length; i++) {
      setValue(cols[i],raVariant.getDataSetValue(row,cols[i]), where);
    }

/*
    com.borland.dx.dataset.Variant v = new com.borland.dx.dataset.Variant();

    for (int i = 0; i < cols.length; i++) {

      try {

        row.getVariant(cols[i],v);

        setValue(cols[i],v.getAsObject(),where);

      }

      catch (Exception ex) {

//        if (!(ex instanceof com.borland.dx.dataset.DataSetException)) System.out.println("ex = "+ex);

      }

    }
*/
  }



  private void set_ra(raSQLSet row,String[] cols, boolean where) {

    for (int i = 0; i < cols.length; i++) {

      try {

        setValue(cols[i],row.getValue(cols[i]),where);

      }

      catch (Exception ex) {

//        System.out.println("ex = "+ex);

      }

    }

  }



  public boolean isExist() {

    try {

      if (mode != COUNT) throw new Exception("Not a COUNT type!");

      ResultSet set = executeQuery();

      set.next();

      return new Integer(set.getObject(1).toString()).intValue() > 0;

    }

    catch (Exception ex) {

      ex.printStackTrace();

      return true;

    }



  }

  private void makeStatmentSql() throws Exception {

    if (columnNames == null || keyColumnNames == null) {

      statementSql = null;

      throw new Exception("Cannot make statement!");

    }

    if (mode == COUNT) {

      statementSql = "SELECT count(*) FROM "

                   .concat(tableName)

                   .concat(getWhereSql());

    } else if (mode == UPDATE) {

      statementSql = "UPDATE "

                   .concat(tableName)

                   .concat(getUpdateSql())

                   .concat(getWhereSql());

    } else if (mode == INSERT) {

      statementSql = "INSERT INTO "

                   .concat(tableName)

                   .concat(getInsertSql());

    } else if (mode == DELETE) {

      statementSql = "DELETE FROM "

                   .concat(tableName)

                   .concat(getWhereSql());

    }
  }



  public String getStatementSql() {

    return statementSql;

  }



  private void makeWhereSql() {

    if (keyColumnNames == null) {

      whereSql = null;

      return;

    }

    whereSql = " WHERE ";

    for (int i = 0; i < keyColumnNames.length; i++) {

      whereSql = whereSql

               .concat(keyColumnNames[i])

               .concat(" = ? AND ");

    }

    whereSql = whereSql.substring(0,(whereSql.length()-4));

  }



  private void makeUpdateSql() {

    if (columnNames == null) {

      updateSql = null;

      return;

    }

    updateSql = " SET ";

    for (int i = 0; i < columnNames.length; i++) {

      updateSql = updateSql

        .concat(columnNames[i])

        .concat(" = ?, ");

    }

    updateSql = updateSql.substring(0,updateSql.length()-2);

  }



  private void makeInsertSql() {

    if (columnNames == null) {

      insertSql = null;

      return;

    }

    String insertSql_1 = " (";

    String insertSql_2 = " VALUES (";

    for (int i = 0; i < columnNames.length; i++) {

      insertSql_1 = insertSql_1.concat(columnNames[i]).concat(",");

      insertSql_2 = insertSql_2.concat("?,");

    }

    insertSql_1 = insertSql_1.substring(0,insertSql_1.length()-1).concat(") ");

    insertSql_2 = insertSql_2.substring(0,insertSql_2.length()-1).concat(") ");

    insertSql = insertSql_1.concat(insertSql_2);

  }



  public String getUpdateSql() {

    if (updateSql == null) makeUpdateSql();

    return updateSql;

  }



  public String getInsertSql() {

    if (insertSql == null) makeInsertSql();

    return insertSql;

  }



  public String getWhereSql() {

    if (whereSql == null) makeWhereSql();

    return whereSql;

  }



  public int getParameterIndex(String colName,boolean where) throws Exception {

    hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

    String columnName = colName.toUpperCase();

    int retIdx = 0;

    if (mode == COUNT) {

      retIdx = ut.indexOfArray(keyColumnNames,columnName)+1;

    } else if (mode == UPDATE) {

      if (where) {
//debug        
//System.out.println("ut.indexOfArray(keyColumnNames,"+columnName+") = "+ut.indexOfArray(keyColumnNames,columnName));
//for (int i = 0; i < keyColumnNames.length; i++) {
//  System.out.println("****** "+keyColumnNames[i]);
//}
//gubed
        retIdx = columnNames.length + ut.indexOfArray(keyColumnNames,columnName) + 1;
      }

      else retIdx = ut.indexOfArray(columnNames,columnName)+1;

    } else if (mode == INSERT) {

      retIdx = ut.indexOfArray(columnNames,columnName)+1;

    } else if (mode == DELETE) {

      retIdx = ut.indexOfArray(keyColumnNames,columnName)+1;

    }

    if (retIdx != 0) return retIdx;

    throw new Exception("Cannot retrieve parameter index");

  }



  public void setValue(String columnName, Object val, boolean where) {

    if (val instanceof Short) {

      setShort(columnName,((Short)val).shortValue(),where);

    } else if (val instanceof Integer) {

      setInt(columnName,((Integer)val).intValue(),where);

    } else if (val instanceof BigDecimal) {

      setBigDecimal(columnName,(BigDecimal)val,where);

    } else if (val instanceof String) {

      setString(columnName,(String)val,where);

    } else if (val instanceof Timestamp) {

      setTimestamp(columnName,(Timestamp)val,where);

    } else if (val instanceof Double) {
      setDouble(columnName, ((Double) val).doubleValue(),where);
    }

  }

  public void setDouble(String columnName,double x, boolean where) {

    try {

      setDouble(getParameterIndex(columnName,where),x);

    }

    catch (Exception ex) {

      ex.printStackTrace();

    }

  }


  public void setShort(String columnName,short x, boolean where) {

    try {

      setShort(getParameterIndex(columnName,where),x);

    }

    catch (Exception ex) {

      ex.printStackTrace();

    }

  }



  public void setInt(String columnName,int x, boolean where) {

    try {

      setInt(getParameterIndex(columnName,where),x);

    }

    catch (Exception ex) {

      ex.printStackTrace();

    }

  }



  public void setBigDecimal(String columnName,BigDecimal x, boolean where) {

    try {

      setBigDecimal(getParameterIndex(columnName,where),x);

    }

    catch (Exception ex) {

      ex.printStackTrace();

    }

  }



  public void setString(String columnName,String x, boolean where) {

    try {

      setString(getParameterIndex(columnName,where),x.trim());

    }

    catch (Exception ex) {
      System.out.println(ex+" "+columnName+" = "+x);
      ex.printStackTrace();

    }

  }



  public void setTimestamp(String columnName,Timestamp x, boolean where) {

    try {

      setTimestamp(getParameterIndex(columnName,where),x);

    }

    catch (Exception ex) {

      ex.printStackTrace();

    }

  }



  public ResultSet executeQuery() throws SQLException {

    return statement.executeQuery();

  }



  public int executeUpdate() throws SQLException {

    return statement.executeUpdate();

  }



  public void setNull(int parameterIndex, int sqlType) throws SQLException {

    statement.setNull(parameterIndex,sqlType);

  }



  public void setBoolean(int parameterIndex, boolean x) throws SQLException {

      statement.setBoolean(parameterIndex,x);

  }



  public void setByte(int parameterIndex, byte x) throws SQLException {

    statement.setByte(parameterIndex,x);

  }

  public void setShort(int parameterIndex, short x) throws SQLException {

    statement.setShort(parameterIndex,x);

  }

  public void setInt(int parameterIndex, int x) throws SQLException {

    statement.setInt(parameterIndex,x);

  }

  public void setLong(int parameterIndex, long x) throws SQLException {

    statement.setLong(parameterIndex,x);

  }

  public void setFloat(int parameterIndex, float x) throws SQLException {

    statement.setFloat(parameterIndex,x);

  }

  public void setDouble(int parameterIndex, double x) throws SQLException {

    statement.setDouble(parameterIndex,x);

  }

  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {

    statement.setBigDecimal(parameterIndex,x);

  }

  public void setString(int parameterIndex, String x) throws SQLException {
//System.out.println("RPSSETSTRING "+parameterIndex+", "+x);
    statement.setString(parameterIndex,x);

  }

  public void setBytes(int parameterIndex, byte[] x) throws SQLException {

    statement.setBytes(parameterIndex,x);

  }

  public void setDate(int parameterIndex, java.sql.Date x) throws SQLException {

    statement.setDate(parameterIndex,x);

  }

  public void setTime(int parameterIndex, Time x) throws SQLException {

    statement.setTime(parameterIndex,x);

  }

  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {

    statement.setTimestamp(parameterIndex,x);

  }

  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {

    statement.setAsciiStream(parameterIndex,x,length);

  }

  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {

//    statement.setUnicodeStream(parameterIndex,x,length);

  }

  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {

    statement.setBinaryStream(parameterIndex,x,length);

  }

  public void clearParameters() throws SQLException {

    statement.clearParameters();

  }

  public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {

    statement.setObject(parameterIndex,x,targetSqlType,scale);

  }

  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {

    statement.setObject(parameterIndex,x,targetSqlType);

  }

  public void setObject(int parameterIndex, Object x) throws SQLException {

    statement.setObject(parameterIndex,x);

  }

  public boolean execute() throws SQLException {

    return statement.execute();

  }

  public void addBatch() throws SQLException {

    statement.addBatch();

  }

  public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {

    statement.setObject(parameterIndex,reader,length);

  }

  public void setRef(int i, Ref x) throws SQLException {

    statement.setRef(i,x);

  }

  public void setBlob(int i, Blob x) throws SQLException {

    statement.setBlob(i,x);

  }

  public void setClob(int i, Clob x) throws SQLException {

    statement.setClob(i,x);

  }

  public void setArray(int i, Array x) throws SQLException {

    statement.setArray(i,x);

  }

  public ResultSetMetaData getMetaData() throws SQLException {

    return statement.getMetaData();

  }

  public void setDate(int parameterIndex, java.sql.Date x, Calendar cal) throws SQLException {

    statement.setDate(parameterIndex,x,cal);

  }

  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {

    statement.setTime(parameterIndex,x,cal);

  }

  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {

    statement.setTimestamp(parameterIndex,x,cal);

  }

  public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {

    statement.setNull(paramIndex,sqlType,typeName);

  }

  public ResultSet executeQuery(String sql) throws SQLException {

    return statement.executeQuery(sql);

  }

  public int executeUpdate(String sql) throws SQLException {

    return statement.executeUpdate(sql);

  }

  public void close() throws SQLException {

    statement.close();

  }

  public int getMaxFieldSize() throws SQLException {

    return statement.getMaxFieldSize();

  }

  public void setMaxFieldSize(int max) throws SQLException {

     statement.setMaxFieldSize(max);

  }

  public int getMaxRows() throws SQLException {

    return statement.getMaxRows();

  }

  public void setMaxRows(int max) throws SQLException {

    statement.setMaxRows(max);

  }

  public void setEscapeProcessing(boolean enable) throws SQLException {

    statement.setEscapeProcessing(enable);

  }

  public int getQueryTimeout() throws SQLException {

    return statement.getQueryTimeout();

  }

  public void setQueryTimeout(int seconds) throws SQLException {

    statement.setQueryTimeout(seconds);

  }

  public void cancel() throws SQLException {

    statement.cancel();

  }

  public SQLWarning getWarnings() throws SQLException {

    return statement.getWarnings();

  }

  public void clearWarnings() throws SQLException {

    statement.clearWarnings();

  }

  public void setCursorName(String name) throws SQLException {

    statement.setCursorName(name);

  }

  public boolean execute(String sql) throws SQLException {

    return statement.execute(sql);

  }

  public ResultSet getResultSet() throws SQLException {

    return statement.getResultSet();

  }

  public int getUpdateCount() throws SQLException {

    return statement.getUpdateCount();

  }

  public boolean getMoreResults() throws SQLException {

    return statement.getMoreResults();

  }

  public void setFetchDirection(int direction) throws SQLException {

    statement.setFetchDirection(direction);

  }

  public int getFetchDirection() throws SQLException {

    return statement.getFetchDirection();

  }

  public void setFetchSize(int rows) throws SQLException {

    statement.setFetchSize(rows);

  }

  public int getFetchSize() throws SQLException {

    return statement.getFetchSize();

  }

  public int getResultSetConcurrency() throws SQLException {

    return statement.getResultSetConcurrency();

  }

  public int getResultSetType() throws SQLException {

    return statement.getResultSetType();

  }

  public void addBatch(String sql) throws SQLException {

    statement.addBatch(sql);

  }

  public void clearBatch() throws SQLException {

    statement.clearBatch();

  }

  public int[] executeBatch() throws SQLException {

    return statement.executeBatch();

  }

  public Connection getConnection() throws SQLException {

    return statement.getConnection();

  }
  ///1.4
  /*
  public ParameterMetaData getParameterMetaData() {
    return null;
  }
  public void setURL(int i, java.net.URL url) {

  }
  public boolean execute(String f, String[] ff) {

  }
  public int executeUpdate(String f, String[] ff) {

  }
  public ResultSet getGeneratedKeys() {

  }
  */
}