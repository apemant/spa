/****license*****************************************************************
**   file: PreparedStatementProxy.java
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
/*
 * Created on 11-Oct-2004
 *
 */
package hr.restart.db.replication.logging;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
//import java.sql.ParameterMetaData;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * @author gradecak
 *
 *TODO : when cancelling or deleting a statement or whatever it should be deleted from the LOG 
 * execution too.
 * 
 * FIXME: when looking for elements to set, the method is dangerous because it is impossible to 
 * have a word with ? in it.
 */
class PreparedStatementProxy implements PreparedStatement{

	private ConnectionProxy connection = null;
	private PreparedStatement preparedStatement = null;
	private String sql = null;
	private String[] parameters = null;
	private Logger log = null;
	
	//private StringBuffer sqlBuffer = null;
	
	/**
	 * 
	 */
	public PreparedStatementProxy(PreparedStatement preparedStatement, String sql) {		
		setPreparedStatement(preparedStatement);
		setSql(sql);				
		
		//sqlBuffer = new StringBuffer();
		
		log = Logger.getLogger(PreparedStatementProxy.class);

		//FIXME
		int cpt = 0;
		for(int i = 0; i<sql.length(); i++){
			if(sql.charAt(i) == '?')
				cpt ++;
		}
		
		parameters = new String[cpt];
		log.debug("PreparedStatementProxy created");
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#addBatch()
	 */
	public void addBatch() throws SQLException {
		throw new UnsupportedOperationException("This method is not implemented. According to the java.sql.Statement specification : A driver is not required to implement this method");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#clearParameters()
	 */
	public void clearParameters() throws SQLException {
		getPreparedStatement().clearParameters();
		log.debug("PreparedStatementProxy.clearParameters()");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#execute()
	 */
	public boolean execute() throws SQLException {
		
	    	    
	    if(getConnection().getAutoCommit())
		    ((ConnectionProxy)getConnection()).beginTransaction();	    	    
	    
		boolean ret = getPreparedStatement().execute();		
		
		String sql = prepareSql();
		
		//connection.executeLOG(sql);
		addSql(sql);
		
		if(getConnection().getAutoCommit()){
		    getConnection().commit();
		    log.info("logged : "+sql);									
		}	
						
		log.debug("PreparedStatementProxy.execute() == "+ret);
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#executeQuery()
	 */
	public ResultSet executeQuery() throws SQLException {
	    	    
		ResultSet ret = getPreparedStatement().executeQuery();		
		
		log.debug("PreparedStatementProxy.executeQuery()");
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#executeUpdate()
	 */
	public int executeUpdate() throws SQLException {
	    
	    if(getConnection().getAutoCommit())
		    ((ConnectionProxy)getConnection()).beginTransaction();
	    
		int ret = getPreparedStatement().executeUpdate();
		
		String sql = prepareSql();
		addSql(sql);
		
		//connection.executeLOG(sql);
		
		if(getConnection().getAutoCommit()){
		    getConnection().commit();
		    log.info("logged : "+sql);			
		}
		
		log.debug("PreparedStatementProxy.executeUpdate()");
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#getMetaData()
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		ResultSetMetaData ret = getPreparedStatement().getMetaData();
		log.debug("PreparedStatementProxy.getMetaData() == "+ret);		
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
//	public ParameterMetaData getParameterMetaData() throws SQLException {
//		ParameterMetaData ret = getPreparedStatement().getParameterMetaData();
//		log.debug("PreparedStatementProxy.getParameterMetaData() == "+ret);
//		return ret;
//	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
	 */
	public void setArray(int i, Array x) throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
	 */
	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
	 */
	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
    
		getPreparedStatement().setBigDecimal(parameterIndex, x);
		
		//setParameter(parameterIndex-1, x.toString());
		setParameter(parameterIndex-1,
		    ParameterAdapter.adaptNumberParameterValue(getPreparedStatement(), parameterIndex, x));
		
		log.debug("PreparedStatementProxy.setBigDecimal("+parameterIndex+","+x+")");
	}



  /* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
	 */
	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
	 */
	public void setBlob(int i, Blob x) throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBoolean(int, boolean)
	 */
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setByte(int, byte)
	 */
	public void setByte(int parameterIndex, byte x) throws SQLException {
		getPreparedStatement().setByte(parameterIndex, x);
		
		setParameter(parameterIndex-1, new Byte(x).toString());
		
		log.debug("PreparedStatementProxy.setInt("+parameterIndex+","+x+")");				
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBytes(int, byte[])
	 */
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
	 */
	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {		
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
	 */
	public void setClob(int i, Clob x) throws SQLException {		
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
	 */
	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
	 */
	public void setDate(int parameterIndex, Date x) throws SQLException {
		getPreparedStatement().setDate(parameterIndex, x);
		
		setParameter(parameterIndex-1, x.toString());
		
		log.debug("PreparedStatementProxy.setInt("+parameterIndex+","+x+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setDouble(int, double)
	 */
	public void setDouble(int parameterIndex, double x) throws SQLException {
		getPreparedStatement().setDouble(parameterIndex, x);
		
		setParameter(parameterIndex-1, new Double(x).toString());
		
		log.debug("PreparedStatementProxy.setDouble("+parameterIndex+","+x+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setFloat(int, float)
	 */
	public void setFloat(int parameterIndex, float x) throws SQLException {
		getPreparedStatement().setFloat(parameterIndex, x);
		
		setParameter(parameterIndex-1, new Float(x).toString());
		
		log.debug("PreparedStatementProxy.setFloat("+parameterIndex+","+x+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setInt(int, int)
	 */
	public void setInt(int parameterIndex, int x) throws SQLException {
		getPreparedStatement().setInt(parameterIndex, x);
		
		setParameter(parameterIndex-1, new Integer(x).toString());
		
		log.debug("PreparedStatementProxy.setInt("+parameterIndex+","+x+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setLong(int, long)
	 */
	public void setLong(int parameterIndex, long x) throws SQLException {
		getPreparedStatement().setLong(parameterIndex, x);
		
		setParameter(parameterIndex-1, new Long(x).toString());
		
		log.debug("PreparedStatementProxy.setLong("+parameterIndex+","+x+")");

	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
	 */
	public void setNull(int paramIndex, int sqlType, String typeName)
			throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setNull(int, int)
	 */
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
	 */
	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scale) throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
	 */
	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
	 */
	public void setObject(int parameterIndex, Object x) throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
	 */
	public void setRef(int i, Ref x) throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setShort(int, short)
	 */
	public void setShort(int parameterIndex, short x) throws SQLException {
		getPreparedStatement().setShort(parameterIndex, x);
		
		setParameter(parameterIndex-1, new Integer(x).toString());
		
		log.debug("PreparedStatementProxy.setShort("+parameterIndex+","+x+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setString(int, java.lang.String)
	 */
	public void setString(int parameterIndex, String x) throws SQLException {
		getPreparedStatement().setString(parameterIndex, x);
		
		setParameter(parameterIndex-1,"'"+x+"'");
		
		log.debug("PreparedStatementProxy.setString("+parameterIndex+","+x+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
	 */
	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
	 */
	public void setTime(int parameterIndex, Time x) throws SQLException {
		getPreparedStatement().setTime(parameterIndex, x);
		
		setParameter(parameterIndex-1, "'"+x.toString()+"'");
		
		log.debug("PreparedStatementProxy.setTime("+parameterIndex+","+x+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
	 */
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		getPreparedStatement().setTimestamp(parameterIndex, x);
		
		
		setParameter(parameterIndex-1, "'"+x.toString()+"'");
		
		log.debug("PreparedStatementProxy.setTimestamp("+parameterIndex+","+x+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int)
	 */
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
	 */
	public void setURL(int parameterIndex, URL x) throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	public void addBatch(String sql) throws SQLException {
		throw new UnsupportedOperationException("This method is not implemented. According to the java.sql.Statement specification : A driver is not required to implement this method");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#cancel()
	 */
	public void cancel() throws SQLException {
	    throw new UnsupportedOperationException("Not supported method");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#clearBatch()
	 */
	public void clearBatch() throws SQLException {
		throw new UnsupportedOperationException("This method is not implemented. According to the java.sql.Statement specification : A driver is not required to implement this method");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		getPreparedStatement().clearWarnings();
		log.debug("PreparedStatementProxy.clearWarnings()");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#close()
	 */
	public void close() throws SQLException {
	    //connection.removeStatement(this);
		getPreparedStatement().close();
		log.debug("PreparedStatementProxy.close()");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */
//	public boolean execute(String sql, int autoGeneratedKeys)
//			throws SQLException {
//		
//	    if(getConnection().getAutoCommit())
//		    ((ConnectionProxy)getConnection()).beginTransaction();
//	    
//		boolean ret = getPreparedStatement().execute(sql, autoGeneratedKeys);		
//		addSql(sql);
//		
//		//connection.executeLOG(sql);
//		
//		if(getConnection().getAutoCommit()){
//		    getConnection().commit();
//		    log.info("logged : "+sql);
//		}
//
//		
//		log.debug("PreparedStatementProxy.execute("+sql+","+autoGeneratedKeys+") == "+ret);		
//		return ret;
//	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
//	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
//	    
//	    if(getConnection().getAutoCommit())
//		    ((ConnectionProxy)getConnection()).beginTransaction();
//	    
//		boolean ret = getPreparedStatement().execute(sql, columnIndexes);		
//		addSql(sql);
//		
//		//connection.executeLOG(sql);
//		
//		if(getConnection().getAutoCommit()){
//		    getConnection().commit();
//		    log.info("logged : "+sql);
//		}
//		
//		log.debug("PreparedStatementProxy.execute("+sql+","+columnIndexes+") == "+ret);		
//		return ret;		
//	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */
//	public boolean execute(String sql, String[] columnNames)
//			throws SQLException {
//	    
//	    if(getConnection().getAutoCommit())
//		    ((ConnectionProxy)getConnection()).beginTransaction();
//	    
//		boolean ret = getPreparedStatement().execute(sql, columnNames);
//		addSql(sql);
//		
//		//connection.executeLOG(sql);	
//		
//		if(getConnection().getAutoCommit()){
//		    getConnection().commit();
//		    log.info("logged : "+sql);
//		}
//		
//		log.debug("PreparedStatementProxy.execute("+sql+","+columnNames+") == "+ret);
//		return ret;
//	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	public boolean execute(String sql) throws SQLException {
	    
	    if(getConnection().getAutoCommit())
		    ((ConnectionProxy)getConnection()).beginTransaction();
	    
		boolean ret = getPreparedStatement().execute(sql);		
		addSql(sql);
		
		//connection.executeLOG(sql);
		
		if(getConnection().getAutoCommit()){
		    getConnection().commit();
			log.info("logged : "+sql);
		}

		log.debug("PreparedStatementProxy.execute("+sql+") == "+ret);
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#executeBatch()
	 */
	public int[] executeBatch() throws SQLException {
		throw new UnsupportedOperationException("This method is not implemented. According to the java.sql.Statement specification : A driver is not required to implement this method");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#executeQuery(java.lang.String)
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
	    	    
		ResultSet ret = getPreparedStatement().executeQuery(sql);
								
		log.debug("PreparedStatementProxy.executeQuery("+sql+") == "+ret);				
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
	    
	    if(getConnection().getAutoCommit())
		    ((ConnectionProxy)getConnection()).beginTransaction();
	    
		int ret = getPreparedStatement().executeUpdate(sql);
		addSql(sql);
		//connection.executeLOG(sql);
		
		if(getConnection().getAutoCommit()){
		    getConnection().commit();
		    log.info("logged :"+sql);
		}
		
		log.debug("PreparedStatementProxy.executeUpdate("+sql+") == "+ret);				
		return ret;		
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */
//	public int executeUpdate(String sql, int[] columnIndexes)
//			throws SQLException {
//	    
//	    if(getConnection().getAutoCommit())
//		    ((ConnectionProxy)getConnection()).beginTransaction();
//	    
//		int ret = getPreparedStatement().executeUpdate(sql, columnIndexes);
//		addSql(sql);
//		//connection.executeLOG(sql);
//		
//		if(getConnection().getAutoCommit()){
//		    getConnection().commit();
//			log.info("logged :"+sql);
//		}
//		
//		log.debug("PreparedStatementProxy.executeUpdate("+sql+","+columnIndexes+") == "+ret);				
//		return ret;		
//	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
	 */
//	public int executeUpdate(String sql, String[] columnNames)
//			throws SQLException {
//	    
//	    if(getConnection().getAutoCommit())
//		    ((ConnectionProxy)getConnection()).beginTransaction();
//	    
//		int ret = getPreparedStatement().executeUpdate(sql, columnNames);
//		addSql(sql);
//		//connection.executeLOG(sql);
//		
//		if(getConnection().getAutoCommit()){
//		    getConnection().commit();
//		    log.info("logged :"+sql);
//		}
//		
//		log.debug("PreparedStatementProxy.executeUpdate("+sql+","+columnNames+") == "+ret);				
//		return ret;
//	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	public int executeUpdate(String sql) throws SQLException {
	    
	    if(getConnection().getAutoCommit())
		    ((ConnectionProxy)getConnection()).beginTransaction();
	    
		int ret = getPreparedStatement().executeUpdate(sql);
		addSql(sql);
		//connection.executeLOG(sql);
		
		if(getConnection().getAutoCommit()){
		    getConnection().commit();
		    log.info("logged :"+sql);
		}
		
		log.debug("PreparedStatementProxy.executeUpdate("+sql+") == "+ret);				
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		//Connection ret = this.connection;//getStatement().getConnection();
		
		log.debug("PreparedStatementProxy.getConnection() == "+connection);		
		return (Connection)connection;
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		int ret = getPreparedStatement().getFetchDirection();
		log.debug("PreparedStatementProxy.getFecthDirection() == "+ret);
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		int ret = getPreparedStatement().getFetchSize();
		log.debug("PreparedStatementProxy.getFecthSize() == "+ret);
		return ret; 
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
//	public ResultSet getGeneratedKeys() throws SQLException {
//		ResultSet ret = getPreparedStatement().getGeneratedKeys();
//		log.debug("PreparedStatementProxy.getGeneratedKeys() == "+ret);
//		return ret; 
//	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	public int getMaxFieldSize() throws SQLException {
		int ret = getPreparedStatement().getMaxFieldSize();
		log.debug("PreparedStatementProxy.getMaxFieldSize() == "+ret);
		return ret; 
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getMaxRows()
	 */
	public int getMaxRows() throws SQLException {
		int ret = getPreparedStatement().getMaxRows();
		log.debug("PreparedStatementProxy.getMaxRows() == "+ret);
		return ret; 
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getMoreResults()
	 */
	public boolean getMoreResults() throws SQLException {
		boolean ret = getPreparedStatement().getMoreResults();
		log.debug("PreparedStatementProxy.getMoreResults() == "+ret);
		return ret; 
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getMoreResults(int)
	 */
//	public boolean getMoreResults(int current) throws SQLException {
//		boolean ret = getPreparedStatement().getMoreResults(current);
//		log.debug("PreparedStatementProxy.getMoreResults("+current+") == "+ret);
//		return ret; 
//	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	public int getQueryTimeout() throws SQLException {
		int ret = getPreparedStatement().getQueryTimeout();
		log.debug("PreparedStatementProxy.getQueryTimeout() == "+ret);
		return ret; 
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSet()
	 */
	public ResultSet getResultSet() throws SQLException {
		ResultSet ret = getPreparedStatement().getResultSet();
		log.debug("PreparedStatementProxy.getResultSet() == "+ret);
		return ret; 
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	public int getResultSetConcurrency() throws SQLException {
		int ret = getPreparedStatement().getResultSetConcurrency();
		log.debug("PreparedStatementProxy.getResultSetConcurrency() == "+ret);
		return ret; 
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
//	public int getResultSetHoldability() throws SQLException {
//		int ret = getPreparedStatement().getResultSetHoldability();
//		log.debug("PreparedStatementProxy.getResultSetHoldability() == "+ret);
//		return ret; 
//	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetType()
	 */
	public int getResultSetType() throws SQLException {
		int ret = getPreparedStatement().getResultSetType();
		log.debug("PreparedStatementProxy.getResultSetType() == "+ret);
		return ret; 
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getUpdateCount()
	 */
	public int getUpdateCount() throws SQLException {
		int ret = getPreparedStatement().getUpdateCount();
		log.debug("PreparedStatementProxy.getUpdateCount() == "+ret);
		return ret; 
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		SQLWarning ret = getPreparedStatement().getWarnings();
		log.debug("PreparedStatementProxy.getWarnings() == "+ret);
		return ret; 
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#setCursorName(java.lang.String)
	 */
	public void setCursorName(String name) throws SQLException {
		getPreparedStatement().setCursorName(name);
		log.debug("PreparedStatementProxy.setCursorName("+name+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	public void setEscapeProcessing(boolean enable) throws SQLException {
		getPreparedStatement().setEscapeProcessing(enable);
		log.debug("PreparedStatementProxy.setEscapeProcessing("+enable+")");

	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	public void setFetchDirection(int direction) throws SQLException {
		getPreparedStatement().setFetchDirection(direction);
		log.debug("PreparedStatementProxy.setFetchDirection("+direction+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	public void setFetchSize(int rows) throws SQLException {
		getPreparedStatement().setFetchSize(rows);
		log.debug("PreparedStatementProxy.setFetchSize("+rows+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	public void setMaxFieldSize(int max) throws SQLException {
		getPreparedStatement().setMaxFieldSize(max);
		log.debug("PreparedStatementProxy.setMaxFieldSize("+max+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	public void setMaxRows(int max) throws SQLException {
		getPreparedStatement().setMaxRows(max);
		log.debug("PreparedStatementProxy.setMaxRows("+max+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	public void setQueryTimeout(int seconds) throws SQLException {
		getPreparedStatement().setQueryTimeout(seconds);
		log.debug("PreparedStatementProxy.setQueryTimeout("+seconds+")");
	}
	/**
	 * @return Returns the preparedStatement.
	 */
	private PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}
	/**
	 * @param preparedStatement The preparedStatement to set.
	 */
	private void setPreparedStatement(PreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
	}
	/**
	 * @return Returns the sql.
	 */
	private String getSql() {
		return sql;
	}
	/**
	 * @param sql The sql to set.
	 */
	private void setSql(String sql) {
		if(getSql() != null)
			return;
		
		this.sql = sql;
	}
	/**
	 * @return Returns the parameters.
	 */
	private String getParameter(int index) {
		return parameters[index];
	}
	/**
	 * @param parameters The parameters to set.
	 */
	private void setParameter(int index, String parameter) {
		parameters[index] = parameter;
	}
	
	/**
	 * 
	 * @return
	 */
	private String prepareSql(){
	    	    
		StringBuffer buf = new StringBuffer(getSql());
		int parameter = 0;
		
		for(int i = 0; i < buf.length(); i++){					    
	
		    //FIXME
			if(buf.charAt(i) == '?'){
		
			    
			    String tmp = getParameter(parameter);
			    if(tmp == null){
			        tmp = "NULL";
			        //throw new IllegalArgumentException("The argument "+parameter+" in the prepared statement is null.");
			        log.warn("The argument "+parameter+" in the prepared statement is null.");
			    }
			    
				buf.replace(i, i+1, tmp);
				parameter ++;
			}
		}
		
		return buf.toString();
	}
	/**
	 * @param connection The connection to set.
	 */
	synchronized public void setConnection(ConnectionProxy connection) {
		if(this.connection != null)
			throw new IllegalArgumentException("The connection cannot be setted because it is already setted");
		
		this.connection = connection;
	}
//    /* (non-Javadoc)
//     * @see hr.restart.db.replication.logging.IStatementProxy#getSqlLog()
//     */
//    public String getSqlLog() {
//        return sqlBuffer.toString();
//    }
//    
	private void addSql(String sql) throws SQLException{
	    connection.executeLOG(sql);
	}
//    /* (non-Javadoc)
//     * @see hr.restart.db.replication.logging.IStatementProxy#clearSqlLog()
//     */
//    public void clearSqlLog(int length) {
//        sqlBuffer.delete(0, length);
//    }

  public ParameterMetaData getParameterMetaData() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean execute(String sql, int[] columnIndexes) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean execute(String sql, String[] columnNames) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  public int executeUpdate(String sql, String[] columnNames) throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }

  public ResultSet getGeneratedKeys() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean getMoreResults(int current) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }

  public int getResultSetHoldability() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }
}
