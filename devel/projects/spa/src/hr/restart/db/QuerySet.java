/****license*****************************************************************
**   file: QuerySet.java
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


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class QuerySet {// implements ResultSet {
//
//  PropertyChangeSupport changeSupport;
//  ResultSet resultSet;
//
//  public QuerySet() {
//  }
//  public QuerySet(ResultSet rs) {
//    this();
//    setResultSet(rs);
//  }
//  public QuerySet(com.borland.dx.sql.dataset.QueryDataSet qds) {
//    String driver = qds.getDatabase().getConnection().getDriver();
//    String url = qds.getDatabase().getConnection().getConnectionURL();
//    String user = qds.getDatabase().getConnection().getUserName();
//    String pass = qds.getDatabase().getConnection().getPassword();
//    String query = qds.getQuery().getQueryString();
//    dataHandler.getConnection(url,user,pass,driver);
//    setResultSet(dataHandler.getResultSet(query));
//  }
//  public void setResultSet(ResultSet rs) {
//    resultSet = rs;
//    if (rs!=null) ColumnContainer.registerColumns(this);
//  }
////propertys (java.awt.Component)
//  public synchronized void addPropertyChangeListener(PropertyChangeListener propertychangelistener) {
//    if(propertychangelistener == null) return;
//    if(changeSupport == null)  changeSupport = new PropertyChangeSupport(this);
//    changeSupport.addPropertyChangeListener(propertychangelistener);
//  }
//
//  public synchronized void removePropertyChangeListener(PropertyChangeListener propertychangelistener) {
//    if(propertychangelistener == null)
//      return;
//    if(changeSupport == null) {
//      return;
//    } else {
//      changeSupport.removePropertyChangeListener(propertychangelistener);
//      return;
//    }
//  }
//
//  public synchronized void addPropertyChangeListener(String s, PropertyChangeListener propertychangelistener) {
//    if(propertychangelistener == null)  return;
//    if(changeSupport == null) changeSupport = new PropertyChangeSupport(this);
//    changeSupport.addPropertyChangeListener(s, propertychangelistener);
//  }
//
//  public synchronized void removePropertyChangeListener(String s, PropertyChangeListener propertychangelistener) {
//    if(propertychangelistener == null)  return;
//    if(changeSupport == null)  {
//      return;
//    } else {
//      changeSupport.removePropertyChangeListener(s, propertychangelistener);
//      return;
//    }
//  }
//
//  public void firePropertyChange(String s, Object oldVal, Object newVal)  {
//    PropertyChangeSupport propertychangesupport = changeSupport;
//    if(propertychangesupport == null) {
//      return;
//    } else {
//      propertychangesupport.firePropertyChange(s, oldVal, newVal);
//      return;
//    }
//  }
////  void f() {
////  java.beans.PropertyChangeListener l;
////    this.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
////    });
////  }

//  public boolean next() {
//    try {
//      return resultSet.next();
//    } catch (Exception e) {
//      e.printStackTrace();
//      return false;
//    }
//  }
//  public void close() {
//    try {
//      resultSet.close();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public boolean wasNull() {
//    try {
//      return resultSet.wasNull();
//    } catch (Exception e) {
//      e.printStackTrace();
//      return false;
//    }
//  }
////geteri
//  public String getString(int columnIndex) {
//    try {
//      return resultSet.getString(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public boolean getBoolean(int columnIndex) {
//    try {
//      return resultSet.getBoolean(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return false;
//    }
//  }
//  public byte getByte(int columnIndex) {
//    try {
//      return resultSet.getByte(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Byte.MIN_VALUE;
//    }
//  }
//  public short getShort(int columnIndex) {
//    try {
//      return resultSet.getShort(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Short.MIN_VALUE;
//    }
//  }
//  public int getInt(int columnIndex) {
//    try {
//      return resultSet.getInt(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Integer.MIN_VALUE;
//    }
//  }
//  public long getLong(int columnIndex) {
//    try {
//      return resultSet.getLong(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Long.MIN_VALUE;
//    }
//  }
//  public float getFloat(int columnIndex) {
//    try {
//      return resultSet.getFloat(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Float.MIN_VALUE;
//    }
//  }
//  public double getDouble(int columnIndex) {
//    try {
//      return resultSet.getDouble(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Double.MIN_VALUE;
//    }
//  }
//  public BigDecimal getBigDecimal(int columnIndex, int scale) {
//    try {
//      return resultSet.getBigDecimal(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public byte[] getBytes(int columnIndex) {
//    try {
//      return resultSet.getBytes(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Date getDate(int columnIndex) {
//    try {
//      return resultSet.getDate(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Time getTime(int columnIndex) {
//    try {
//      return resultSet.getTime(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Timestamp getTimestamp(int columnIndex) {
//    try {
//      return resultSet.getTimestamp(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public InputStream getAsciiStream(int columnIndex) {
//    try {
//      return resultSet.getAsciiStream(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public InputStream getUnicodeStream(int columnIndex) {
//    return null;
//  }
//  public InputStream getBinaryStream(int columnIndex) {
//    try {
//      return resultSet.getBinaryStream(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Object getObject(int columnIndex) {
//    try {
//      return resultSet.getObject(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Reader getCharacterStream(int columnIndex) {
//    try {
//      return resultSet.getCharacterStream(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public BigDecimal getBigDecimal(int columnIndex) {
//    try {
//      return resultSet.getBigDecimal(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Object getObject(int i, Map map) {
//    try {
//      return resultSet.getObject(i,map);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Ref getRef(int i) {
//    try {
//      return resultSet.getRef(i);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Blob getBlob(int i) {
//    try {
//      return resultSet.getBlob(i);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Clob getClob(int i) {
//    try {
//      return resultSet.getClob(i);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Array getArray(int i) {
//    try {
//      return resultSet.getArray(i);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Date getDate(int columnIndex, Calendar cal) {
//    try {
//      return resultSet.getDate(columnIndex,cal);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Time getTime(int columnIndex, Calendar cal) {
//    try {
//      return resultSet.getTime(columnIndex,cal);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Timestamp getTimestamp(int columnIndex, Calendar cal) {
//    try {
//      return resultSet.getTimestamp(columnIndex,cal);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Date getDate(String columnName, Calendar cal) {
//    try {
//      return resultSet.getDate(columnName,cal);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Time getTime(String columnName, Calendar cal) {
//    try {
//      return resultSet.getTime(columnName,cal);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Timestamp getTimestamp(String columnName, Calendar cal) {
//    try {
//      return resultSet.getTimestamp(columnName,cal);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Object getObject(String colName, Map map) {
//    try {
//      return resultSet.getObject(colName,map);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Ref getRef(String colName) {
//    try {
//      return resultSet.getRef(colName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Blob getBlob(String colName)  {
//    try {
//      return resultSet.getBlob(colName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Clob getClob(String colName)  {
//    try {
//      return resultSet.getClob(colName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Array getArray(String colName)  {
//    try {
//      return resultSet.getArray(colName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Object getObject(String columnName)  {
//    try {
//      return resultSet.getObject(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Reader getCharacterStream(String columnName)  {
//    try {
//      return resultSet.getCharacterStream(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public BigDecimal getBigDecimal(String columnName)  {
//    try {
//      return resultSet.getBigDecimal(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public String getString(String columnName)  {
//    try {
//      return resultSet.getString(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public boolean getBoolean(String columnName)  {
//    try {
//      return resultSet.getBoolean(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return false;
//    }
//  }
//  public byte getByte(String columnName)  {
//    try {
//      return resultSet.getByte(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Byte.MIN_VALUE;
//    }
//  }
//
//  public short getShort(String columnName)  {
//    try {
//      return resultSet.getShort(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Short.MIN_VALUE;
//    }
//  }
//
//  public int getInt(String columnName)  {
//    try {
//      return resultSet.getInt(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Integer.MIN_VALUE;
//    }
//  }
//
//  public long getLong(String columnName)  {
//    try {
//      return resultSet.getLong(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Long.MIN_VALUE;
//    }
//  }
//  public float getFloat(String columnName)  {
//    try {
//      return resultSet.getFloat(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Float.MIN_VALUE;
//    }
//  }
//  public double getDouble(String columnName)  {
//    try {
//      return resultSet.getDouble(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return Double.MIN_VALUE;
//    }
//  }
//  public BigDecimal getBigDecimal(String columnName, int scale)  {
//    try {
//      return resultSet.getBigDecimal(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public byte[] getBytes(String columnName)  {
//    try {
//      return resultSet.getBytes(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Date getDate(String columnName)  {
//    try {
//      return resultSet.getDate(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Time getTime(String columnName)  {
//    try {
//      return resultSet.getTime(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public Timestamp getTimestamp(String columnName)  {
//    try {
//      return resultSet.getTimestamp(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public InputStream getAsciiStream(String columnName)  {
//    try {
//      return resultSet.getAsciiStream(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
//  public InputStream getUnicodeStream(String columnName)  {
//    return null;
//  }
//  public InputStream getBinaryStream(String columnName)  {
//    try {
//      return resultSet.getBinaryStream(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return null;
//    }
//  }
////seteri
//  //updates
//  public void updateNull(int columnIndex)  {
//    try {
//      resultSet.updateNull(columnIndex);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  public void updateBoolean(int columnIndex, boolean x)  {
//    try {
//      resultSet.updateBoolean(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  public void updateByte(int columnIndex, byte x)  {
//    try {
//      resultSet.updateByte(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateShort(int columnIndex, short x)  {
//    try {
//      resultSet.updateShort(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateInt(int columnIndex, int x)  {
//    try {
//      resultSet.updateInt(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateLong(int columnIndex, long x)  {
//    try {
//      resultSet.updateLong(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateFloat(int columnIndex, float x)  {
//    try {
//      resultSet.updateFloat(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateDouble(int columnIndex, double x)  {
//    try {
//      resultSet.updateDouble(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateBigDecimal(int columnIndex, BigDecimal x)  {
//    try {
//      resultSet.updateBigDecimal(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateString(int columnIndex, String x)  {
//    try {
//      resultSet.updateString(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateBytes(int columnIndex, byte[] x)  {
//    try {
//      resultSet.updateBytes(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateDate(int columnIndex, Date x)  {
//    try {
//      resultSet.updateDate(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateTime(int columnIndex, Time x)  {
//    try {
//      resultSet.updateTime(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateTimestamp(int columnIndex, Timestamp x)  {
//    try {
//      resultSet.updateTimestamp(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateAsciiStream(int columnIndex, InputStream x, int length)  {
//    try {
//      resultSet.updateAsciiStream(columnIndex,x,length);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateBinaryStream(int columnIndex, InputStream x, int length)  {
//    try {
//      resultSet.updateBinaryStream(columnIndex,x,length);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateCharacterStream(int columnIndex, Reader x, int length)  {
//    try {
//      resultSet.updateCharacterStream(columnIndex,x,length);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateObject(int columnIndex, Object x, int scale)  {
//    try {
//      resultSet.updateObject(columnIndex,x,scale);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateObject(int columnIndex, Object x)  {
//    try {
//      resultSet.updateObject(columnIndex,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateNull(String columnName)  {
//    try {
//      resultSet.updateNull(columnName);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateBoolean(String columnName, boolean x)  {
//    try {
//      resultSet.updateBoolean(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateByte(String columnName, byte x)  {
//    try {
//      resultSet.updateByte(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateShort(String columnName, short x)  {
//    try {
//      resultSet.updateShort(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateInt(String columnName, int x)  {
//    try {
//      resultSet.updateInt(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateLong(String columnName, long x)  {
//    try {
//      resultSet.updateLong(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateFloat(String columnName, float x)  {
//    try {
//      resultSet.updateFloat(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateDouble(String columnName, double x)  {
//    try {
//      resultSet.updateDouble(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateBigDecimal(String columnName, BigDecimal x)  {
//    try {
//      resultSet.updateBigDecimal(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateString(String columnName, String x)  {
//    try {
//      resultSet.updateString(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateBytes(String columnName, byte[] x)  {
//    try {
//      resultSet.updateBytes(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateDate(String columnName, Date x)  {
//    try {
//      resultSet.updateDate(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateTime(String columnName, Time x)  {
//    try {
//      resultSet.updateTime(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateTimestamp(String columnName, Timestamp x)  {
//    try {
//      resultSet.updateTimestamp(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateAsciiStream(String columnName, InputStream x, int length)  {
//    try {
//      resultSet.updateAsciiStream(columnName,x,length);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateBinaryStream(String columnName, InputStream x, int length)  {
//    try {
//      resultSet.updateBinaryStream(columnName,x,length);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateCharacterStream(String columnName, Reader reader, int length)  {
//    try {
//      resultSet.updateCharacterStream(columnName,reader,length);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateObject(String columnName, Object x, int scale)  {
//    try {
//      resultSet.updateObject(columnName,x,scale);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void updateObject(String columnName, Object x)  {
//    try {
//      resultSet.updateObject(columnName,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  //
///*************************************************************/
//  public void setNull(String columnName)  {
//    try {
//      updateNull(columnName);
//      firePropertyChange(columnName.concat("_changed"),null,null);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setBoolean(String columnName, boolean x)  {
//    try {
//      Boolean oldVal = new Boolean(getBoolean(columnName));
//      Boolean newVal = new Boolean(x);
//      updateBoolean(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,newVal);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setByte(String columnName, byte x)  {
//    try {
//      Byte oldVal = new Byte(getByte(columnName));
//      Byte newVal = new Byte(x);
//      updateByte(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,newVal);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setShort(String columnName, short x)  {
//    try {
//      Short oldVal = new Short(getShort(columnName));
//      Short newVal = new Short(x);
//      updateShort(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,newVal);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setInt(String columnName, int x)  {
//    try {
//      Integer oldVal = new Integer(getInt(columnName));
//      Integer newVal = new Integer(x);
//      updateInt(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,newVal);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setLong(String columnName, long x)  {
//    try {
//      Long oldVal = new Long(getLong(columnName));
//      Long newVal = new Long(x);
//      updateLong(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,newVal);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setFloat(String columnName, float x)  {
//    try {
//      Float oldVal = new Float(getFloat(columnName));
//      Float newVal = new Float(x);
//      updateFloat(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,newVal);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setDouble(String columnName, double x)  {
//    try {
//      Double oldVal = new Double(getFloat(columnName));
//      Double newVal = new Double(x);
//      updateDouble(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,newVal);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setBigDecimal(String columnName, BigDecimal x)  {
//    try {
//      BigDecimal oldVal = getBigDecimal(columnName);
//      updateBigDecimal(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setString(String columnName, String x)  {
//    try {
//      String oldVal = getString(columnName);
//      updateString(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setBytes(String columnName, byte[] x)  {
//    try {
//      byte[] oldVal = getBytes(columnName);
//      updateBytes(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setDate(String columnName, Date x)  {
//    try {
//      Date oldVal = getDate(columnName);
//      updateDate(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setTime(String columnName, Time x)  {
//    try {
//      Time oldVal = getTime(columnName);
//      updateTime(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setTimestamp(String columnName, Timestamp x)  {
//    try {
//      Timestamp oldVal = getTimestamp(columnName);
//      updateTimestamp(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  public void setObject(String columnName, Object x)  {
//    try {
//      Object oldVal = getObject(columnName);
//      updateObject(columnName,x);
//      firePropertyChange(columnName.concat("_changed"),oldVal,x);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public void setValue(String columnName,Object x) {
//
//  }
///*************************************************************/
////end seteri
///*************************************************************/
//  public ColumnDef getColumn(String columnName) {
//    return ColumnContainer.getColumn(columnName);
//  }
///*************************************************************/
//  public SQLWarning getWarnings() throws SQLException {
//    return resultSet.getWarnings();
//  }
//  public void clearWarnings() throws SQLException {
//    resultSet.clearWarnings();
//  }
//  public String getCursorName() throws SQLException {
//    return resultSet.getCursorName();
//  }
//  public ResultSetMetaData getMetaData() throws SQLException {
//    return resultSet.getMetaData();
//  }
//  public int findColumn(String columnName) throws SQLException {
//    return resultSet.findColumn(columnName);
//  }
//  public boolean isBeforeFirst() throws SQLException {
//    return resultSet.isBeforeFirst();
//  }
//  public boolean isAfterLast() throws SQLException {
//    return resultSet.isAfterLast();
//  }
//  public boolean isFirst() throws SQLException {
//    return resultSet.isFirst();
//  }
//  public boolean isLast() throws SQLException {
//    return resultSet.isLast();
//  }
//  public void beforeFirst() throws SQLException {
//    resultSet.beforeFirst();
//  }
//  public void afterLast() throws SQLException {
//    resultSet.afterLast();
//  }
//  public boolean first() throws SQLException {
//    return resultSet.first();
//  }
//  public boolean last() throws SQLException {
//    return resultSet.last();
//  }
//  public int getRow() throws SQLException {
//    return resultSet.getRow();
//  }
//  public boolean absolute(int row) throws SQLException {
//    return resultSet.absolute(row);
//  }
//  public boolean relative(int rows) throws SQLException {
//    return resultSet.relative(rows);
//  }
//  public boolean previous() throws SQLException {
//    return resultSet.previous();
//  }
//  public void setFetchDirection(int direction) throws SQLException {
//    resultSet.setFetchDirection(direction);
//  }
//  public int getFetchDirection() throws SQLException {
//    return resultSet.getFetchDirection();
//  }
//  public void setFetchSize(int rows) throws SQLException {
//    resultSet.setFetchSize(rows);
//  }
//  public int getFetchSize() throws SQLException {
//    return resultSet.getFetchSize();
//  }
//  public int getType() throws SQLException {
//    return resultSet.getType();
//  }
//  public int getConcurrency() throws SQLException {
//    return resultSet.getConcurrency();
//  }
//  public boolean rowUpdated() throws SQLException {
//    return resultSet.rowUpdated();
//  }
//  public boolean rowInserted() throws SQLException {
//    return resultSet.rowInserted();
//  }
//  public boolean rowDeleted() throws SQLException {
//    return resultSet.rowDeleted();
//  }
//  public void insertRow() throws SQLException {
//    resultSet.insertRow();
//  }
//  public void updateRow() throws SQLException {
//    resultSet.updateRow();
//  }
//  public void deleteRow() throws SQLException {
//    resultSet.deleteRow();
//  }
//  public void refreshRow() throws SQLException {
//    resultSet.refreshRow();
//  }
//  public void cancelRowUpdates() throws SQLException {
//    resultSet.cancelRowUpdates();
//  }
//  public void moveToInsertRow() throws SQLException {
//    resultSet.moveToInsertRow();
//  }
//  public void moveToCurrentRow() throws SQLException {
//    resultSet.moveToCurrentRow();
//  }
//  public Statement getStatement() throws SQLException {
//    return resultSet.getStatement();
//  }
}