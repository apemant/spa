/****license*****************************************************************
**   file: raSQLSetComm.java
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

public class raSQLSetComm implements java.io.Serializable {

  private java.util.Properties connProperties;
  private String query;
  private String[] colNames;
  private java.util.Hashtable rows;
  private java.sql.ResultSetMetaData metadata;
  private String exceptionMsg;
  private String tableName;
  private String[] keys;
  private String identifier;
  private int lastRowIndex;
  private int firstRowIndex;
  private int rowCount;
  private String method;//open, saveChanges, moreData, finalize

  /**
   * Konstruira prazan objekt - za exceptione
   * c = new raSQLSetComm();
   * c.setExceptionMsg(String);
   */
  public raSQLSetComm() {

  }
  /**
   * Kada lokalni valueSet trazi open na server
   * @param qry query koji treba izvrsiti na bazi
   * @param clientInfo informacija o clientu = hr.restart.start.getClient().getConnection().getID()
   */
  public raSQLSetComm(String qry, String clientInfo) {
    this(qry,null,clientInfo);
  }

  /**
   * Kada lokalni valueSet trazi open na server
   * @param qry query koji treba izvrsiti na bazi
   * @param connProps informacija o konekciji; tu trebaju biti url, user, password, eventualno charSet itd., ako je connProps=null otvara se konekcija prema restart.properties na serveru
   * @param clientInfo informacija o clientu = hr.restart.start.getClient().getConnection().getID()
   */
  public raSQLSetComm(String qry, java.util.Properties connProps, String clientInfo) {
    setQuery(qry);
    setMethod(raSQLSetInvoker.OPEN);
    setConnProperties(connProps);
    setIdentifier(clientInfo);
  }
  /**
   * Kada se uspjesno izvrsi set.open() instancirati tako i nakon toga namjestiti identifier i poslati clientu
   * @param set raFastSQLSet ciji query se izvrsio na serveru
   */
  public raSQLSetComm(raFastSQLSet set) {
    if (set==null) return;
    setQuery(set.getQuery());
    setColNames(set.getColNames());
    setRows(set.getRows());
//    setMetadata(set.getMetadata());
    setTableName(set.getTableName());
    setKeys(set.getKeys());
    setRowCount(set.getRowCount());
    setLastRowIndex(set.getLastRowIndex());
    setFirstRowIndex(set.getFirstRowIndex());
  }

  /**
   * Kada client valueSet zatrazi snimanje promjena salje ovakav objekt
   * @param rws promjenjeni redovi
   * @param ident identifier kojeg je client dobio nakon opena
   */
  public raSQLSetComm(java.util.Hashtable rws,String ident) {
    setRows(rws);
    setIdentifier(ident);
    setMethod(raSQLSetInvoker.SAVECHANGES);
  }

  public void setConnProperties(java.util.Properties conProps) {
    connProperties = conProps;
  }

  public java.util.Properties getConnProperties() {
    return connProperties;
  }

  public void setMethod(String m) {
    method = m;
  }

  public String getMethod() {
    return method;
  }

  public String getQuery() {
    return query;
  }
  public void setQuery(String query) {
    this.query = query;
  }
  public void setColNames(String[] colNames) {
    this.colNames = colNames;
  }
  public String[] getColNames() {
    return colNames;
  }
  public void setRows(java.util.Hashtable rows) {
    this.rows = rows;
  }
  public java.util.Hashtable getRows() {
    return rows;
  }
  public void setMetadata(java.sql.ResultSetMetaData metadata) {
    this.metadata = metadata;
  }
  public java.sql.ResultSetMetaData getMetadata() {
    return metadata;
  }
  public void setExceptionMsg(String exceptionMsg) {
    this.exceptionMsg = exceptionMsg;
  }
  public String getExceptionMsg() {
    return exceptionMsg;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  public String getTableName() {
    return tableName;
  }
  public void setKeys(String[] keys) {
    this.keys = keys;
  }
  public String[] getKeys() {
    return keys;
  }
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
  public String getIdentifier() {
    return identifier;
  }
  public int getLastRowIndex() {
//    return Integer.parseInt(lastRowIndex);
    return lastRowIndex;
  }
  public void setLastRowIndex(int idx) {
//    lastRowIndex = idx+"";
    lastRowIndex = idx;
System.out.println("set lastRowIndex to "+lastRowIndex);
  }
  public int getFirstRowIndex() {
//    return Integer.parseInt(firstRowIndex);
    return firstRowIndex;
  }
  public void setFirstRowIndex(int idx) {
//    firstRowIndex = idx+"";
    firstRowIndex = idx;
System.out.println("set firstRowIndex to "+firstRowIndex);
  }

  public int getRowCount() {
//    return Integer.parseInt(rowCount);
    return rowCount;
  }
  public void setRowCount(int rowCnt) {
//    rowCount = rowCnt+"";
    rowCount = rowCnt;
System.out.println("set rowCount to "+rowCount);
  }
}