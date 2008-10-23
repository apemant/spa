/****license*****************************************************************
**   file: ConnectionProxy.java
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
 * Created on 15-Sep-2004
 */
package hr.restart.db.replication.logging;

import hr.restart.util.VarStr;

import java.rmi.server.UID;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;


/**
 * @author gradecak
 *
 * A connection proxy that logs everything what is sent to the DB Connection.
 * Use setLogWriter(PrintWriter) to set the log destination 
 * 
 * When rollbacking or commiting the connection is automatically set on autocommit true
 */
class ConnectionProxy implements Connection{
    
    private UID uid = null; 
    //private ArrayList listStatements = null;    
    
  
    private Connection connection = null;    

    private DriverProxy driver = null;

    private Transaction transaction = null;
    private Logger log = null;
    private PreparedStatement statementLOG = null;
    private Connection connectionSQL = null;
    
	private boolean localAutoCommit = true;//default connection
	
	private /*final*/ String LOG_STRING = null;
	
	private /*final*/ String LOG_COMMIT_STRING = null;
  private int LOG_COMMIT_PARAMETERCOUNT;
          
    /**
	 * @param connection the connection to use

	 */
	public ConnectionProxy(Connection connection) {
	  	    
	    
		setConnection(connection);		
		//listStatements = new ArrayList();		
		log = Logger.getLogger(ConnectionProxy.class);

						
		log.debug("ConnectionProxy created");
	}        
	/* (non-Javadoc)
	 * @see java.sql.Connection#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		getConnection().clearWarnings();
		
		log.debug("ConnectionProxy.clearWarnings()");
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#close()
	 */
	public void close() throws SQLException {
		getConnection().close();		
		//listStatements.clear();
		
		log.debug("ConnectionProxy.close()");	
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#commit()
	 */
	synchronized public void commit() throws SQLException {

				
		try {		    
		    
		    try {
	            if((transaction.getStatus() != Status.STATUS_ACTIVE) ){	   
	                throw new SQLException("No transaction can be commited!");
	            }	            
	            
	        } catch (SystemException e) {
	            throw new SQLException(e.getMessage());
	        }
		    
			commitTransaction();

			setAutoCommit(true);

			//listStatements.clear();

		} catch (SecurityException e) {
						
		    try {
		        rollbackTransaction();
            } catch (IllegalStateException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SystemException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (NotSupportedException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (RollbackException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SQLException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            }
            log.fatal(e);
			throw new SQLException(e.getMessage());
		} catch (RollbackException e) {
		    try {
		        rollbackTransaction();
            } catch (IllegalStateException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SystemException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (NotSupportedException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (RollbackException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SQLException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            }
            log.fatal(e);
			throw new SQLException(e.getMessage());			
		} catch (HeuristicMixedException e) {
		    try {
		        rollbackTransaction();
            } catch (IllegalStateException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SystemException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (NotSupportedException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (RollbackException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SQLException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            }
            log.fatal(e);
			throw new SQLException(e.getMessage());
		} catch (HeuristicRollbackException e) {
		    try {
		        rollbackTransaction();
            } catch (IllegalStateException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SystemException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (NotSupportedException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (RollbackException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SQLException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            }
            log.fatal(e);
			throw new SQLException(e.getMessage());			
		} catch (SystemException e) {
		    try {
		        rollbackTransaction();
            } catch (IllegalStateException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SystemException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (NotSupportedException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (RollbackException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SQLException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            }
            log.fatal(e);
			throw new SQLException(e.getMessage());
		} catch (IllegalStateException e) {
		    try {
		        rollbackTransaction();
            } catch (IllegalStateException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SystemException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (NotSupportedException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (RollbackException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SQLException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            }
            log.fatal(e);
			throw new SQLException(e.getMessage());
        } catch (NotSupportedException e) {
            try {
		        rollbackTransaction();
            } catch (IllegalStateException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SystemException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (NotSupportedException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (RollbackException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SQLException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            }
            log.fatal(e);
			throw new SQLException(e.getMessage());
        } catch (SQLException e) {
            try {
		        rollbackTransaction();
            } catch (IllegalStateException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SystemException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (NotSupportedException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (RollbackException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            } catch (SQLException e1) {
                log.fatal(e1);
                throw new SQLException(e1.getMessage());
            }
            log.fatal(e);
            log.debug("Commit failed : "+e.getMessage());
			throw new SQLException(e.getMessage());
        }
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement()
	 */
	synchronized public Statement createStatement() throws SQLException {		
		Statement statement = getConnection().createStatement();
		
		//the proxy settings
		StatementProxy proxyStatement = new StatementProxy(statement);
		proxyStatement.setConnection(this);
		
		//listStatements.add(proxyStatement);
		
		log.debug("ConnectionProxy.createStatement() == "+proxyStatement);
		return proxyStatement;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
//	synchronized public Statement createStatement(int resultSetType,
//			int resultSetConcurrency, int resultSetHoldability)
//			throws SQLException {
//		Statement statement = getConnection().createStatement(resultSetType,resultSetConcurrency,resultSetHoldability);
//		
//		//the proxy settings
//		StatementProxy proxyStatement = new StatementProxy(statement);
//		proxyStatement.setConnection(this);
//		
//		//listStatements.add(proxyStatement);
//		
//		log.debug("ConnectionProxy.createStatement("+resultSetType+","+resultSetConcurrency+","+resultSetHoldability+") == "+proxyStatement);
//		
//		return proxyStatement;
//	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		Statement statement = getConnection().createStatement(resultSetType,resultSetConcurrency);
		
		//the proxy settings
		StatementProxy proxyStatement = new StatementProxy(statement);
		proxyStatement.setConnection(this);
		
		//listStatements.add(proxyStatement);
		
		log.debug("ConnectionProxy.createStatement("+resultSetType+","+resultSetConcurrency+") == "+proxyStatement);
		return proxyStatement;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getAutoCommit()
	 */
	public boolean getAutoCommit() throws SQLException {	    
		boolean ret = isLocalAutoCommit();
		
		log.debug("ConnectionProxy.getAutoCommit() == "+ret);
		
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getCatalog()
	 */
	public String getCatalog() throws SQLException {
		String catalog = getConnection().getCatalog();
		
		log.debug("ConnectionProxy.getCatalog() == "+catalog);
		
		return catalog;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getHoldability()
	 */
//	public int getHoldability() throws SQLException {
//		int hold = getConnection().getHoldability();
//		
//		log.debug("ConnectionProxy.getHoldability() == "+hold);
//		
//		return hold;
//	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getMetaData()
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
						
		DatabaseMetaData meta = getConnection().getMetaData();
		
		log.debug("ConnectionProxy.getMetaData() == "+meta);
		
		return meta;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	public int getTransactionIsolation() throws SQLException {
		int isol = getConnection().getTransactionIsolation();
		
		log.debug("ConnectionProxy.getIsolation() == "+isol);
		
		return isol;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getTypeMap()
	 */
	public Map getTypeMap() throws SQLException {
		Map map = getConnection().getTypeMap();
		
		log.debug("ConnectionProxy.getTypeMap() == "+map);
		
		return map;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		SQLWarning warnings = getConnection().getWarnings();
		
		log.debug("ConnectionProxy.getWarnings() == "+warnings);
		
		return warnings;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#isClosed()
	 */
	public boolean isClosed() throws SQLException {
		boolean ret = getConnection().isClosed();
		
		log.debug("ConnectionProxy.isClosed() == "+ret);
		
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#isReadOnly()
	 */
	public boolean isReadOnly() throws SQLException {
		boolean ret = getConnection().isReadOnly();
		
		log.debug("ConnectionProxy.isReadOnly() == "+ret);
		
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
	public String nativeSQL(String sql) throws SQLException {
		
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
				
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException { 
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	public CallableStatement prepareCall(String sql) throws SQLException {
		throw new UnsupportedOperationException("This operation is not supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
	 */
//	public PreparedStatement prepareStatement(String sql, int resultSetType,
//			int resultSetConcurrency, int resultSetHoldability)
//			throws SQLException {
//		PreparedStatement ret = getConnection().prepareStatement(sql,resultSetType,resultSetConcurrency,resultSetHoldability);		
//		PreparedStatementProxy proxy = new PreparedStatementProxy(ret,sql);
//		proxy.setConnection(this);		
//		
//		//listStatements.add(proxy);
//		
//		log.debug("ConnectionProxy.prepareStatement("+sql+","+resultSetType+","+resultSetConcurrency+","+resultSetHoldability+") == "+proxy);
//		return proxy;
//	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		PreparedStatement ret = getConnection().prepareStatement(sql,resultSetType,resultSetConcurrency);		
		PreparedStatementProxy proxy = new PreparedStatementProxy(ret,sql);
		proxy.setConnection(this);		
		
		//listStatements.add(proxy);
		
		log.debug("ConnectionProxy.prepareStatement("+sql+","+resultSetType+","+resultSetConcurrency+") == "+proxy);
		return proxy;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
//	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
//			throws SQLException {
//		PreparedStatement ret = getConnection().prepareStatement(sql,autoGeneratedKeys);		
//		PreparedStatementProxy proxy = new PreparedStatementProxy(ret,sql);
//		proxy.setConnection(this);		
//		
//		//listStatements.add(proxy);
//		
//		log.debug("ConnectionProxy.prepareStatement("+sql+","+autoGeneratedKeys+") == "+proxy);
//		return proxy;
//		
//	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
//	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
//			throws SQLException {
//		PreparedStatement ret = getConnection().prepareStatement(sql,columnIndexes);		
//		PreparedStatementProxy proxy = new PreparedStatementProxy(ret,sql);
//		proxy.setConnection(this);		
//		
//		//listStatements.add(proxy);
//		
//		log.debug("ConnectionProxy.prepareStatement("+sql+","+columnIndexes+") == "+proxy);
//		return proxy;
//	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
	 */
//	public PreparedStatement prepareStatement(String sql, String[] columnNames)
//			throws SQLException {
//		PreparedStatement ret = getConnection().prepareStatement(sql,columnNames);		
//		PreparedStatementProxy proxy = new PreparedStatementProxy(ret,sql);
//		proxy.setConnection(this);
//		
//		//listStatements.add(proxy);
//		
//		log.debug("ConnectionProxy.prepareStatement("+sql+","+columnNames+") == "+proxy);
//		return proxy;
//	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement ret = getConnection().prepareStatement(sql);		
		PreparedStatementProxy proxy = new PreparedStatementProxy(ret,sql);
		proxy.setConnection(this);		
		
		//listStatements.add(proxy);
		
		log.debug("ConnectionProxy.prepareStatement("+sql+") == "+proxy);
		return proxy;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
//	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
//		throw new UnsupportedOperationException("This operation is not supported");
//	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#rollback()
	 */
	synchronized public void rollback() throws SQLException {
		
		try {
			rollbackTransaction();
			
			setAutoCommit(true);
			
			//listStatements.clear();

		} catch (IllegalStateException e) {
		    log.fatal("Can not roll back the transaction : "+e);
			throw new SQLException(e.getMessage());
		} catch (SystemException e) {
		    log.fatal("Can not roll back the transaction : "+e);
			throw new SQLException(e.getMessage());
		} catch (NotSupportedException e) {
		    log.fatal("Can not roll back the transaction : "+e);
			throw new SQLException(e.getMessage());
        } catch (RollbackException e) {
            log.fatal("Can not roll back the transaction : "+e);
			throw new SQLException(e.getMessage());
        } catch (SQLException e) {
            log.fatal("Can not roll back the transaction : "+e);
			throw new SQLException(e.getMessage());
        }

		log.debug("ConnectionProxy.rollback()");
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	 */
//	public void rollback(Savepoint savepoint) throws SQLException {
//		throw new SQLException("This functionnality is not yet supported");
//	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	synchronized public void setAutoCommit(boolean autoCommit) throws SQLException {
	    
		if(transaction != null){
		    
			try {
	            if((transaction.getStatus() != Status.STATUS_NO_TRANSACTION) &&
	                    (transaction.getStatus() != Status.STATUS_COMMITTED) && 
	                    (transaction.getStatus() != Status.STATUS_ROLLEDBACK) ){	   
	                throw new SQLException("A transaction is currently in use! (Nested transactions). You should commit it before starting another.");
	            }	            
	            
	        } catch (SystemException e) {
	            throw new SQLException(e.getMessage());
	        }		
		}        
        
        setLocalAutoCommit(autoCommit);
        
        if(!isLocalAutoCommit())          
                beginTransaction();         
        		
		log.debug("ConnectionProxy.setAutoCommit("+autoCommit+")");
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setCatalog(java.lang.String)
	 */
	public void setCatalog(String catalog) throws SQLException {
		throw new SQLException("This functionnality is not yet supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setHoldability(int)
	 */
	public void setHoldability(int holdability) throws SQLException {
		throw new SQLException("This functionnality is not yet supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean readOnly) throws SQLException {
		throw new SQLException("This functionnality is not yet supported");
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint()
	 */
//	public Savepoint setSavepoint() throws SQLException {
//		throw new SQLException("This functionnality is not yet supported");		
//	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	 */
//	public Savepoint setSavepoint(String name) throws SQLException {
//		throw new SQLException("This functionnality is not yet supported");
//	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	public void setTransactionIsolation(int level) throws SQLException {
		throw new SQLException("This functionnality is not yet supported");
	}

	/**
	 * @return Returns the connection.
	 */
	private Connection getConnection() {
		return connection;
	}
	/**
	 * @param connection The connection to set.
	 */
	private void setConnection(Connection connection) {
		this.connection = connection;
	}
//	/**
//	 * @return Returns the dbURL.
//	 */
//	private String getDBURL() {
//		return dbURL;
//	}
//	/**
//	 * @param dbURL The dbURL to set.
//	 */
//	private void setDBURL(String dbURL) {
//		this.dbURL = dbURL;
//	}
//	/**
//	 * @return Returns the dbPassword.
//	 */
//	private String getDBPassword() {
//		return dbPassword;
//	}
//	/**
//	 * @param dbPassword The dbPassword to set.
//	 */
//	private void setDBPassword(String dbPassword) {
//		this.dbPassword = dbPassword;
//	}
//	/**
//	 * @return Returns the dbUser.
//	 */
//	private String getDBUser() {
//		return dbUser;
//	}
//	/**
//	 * @param dbUser The dbUser to set.
//	 */
//	private void setDBUser(String dbUser) {
//		this.dbUser = dbUser;
//	}
	/**
	 * @return Returns the driver.
	 */
	protected DriverProxy getDriver() {
		return driver;
	}
	/**
	 * @param driver The driver to set.
	 */
	protected void setDriver(DriverProxy driver) {
		this.driver = driver;
	}
	/**
	 * @return Returns the localAutoCommit.
	 */
	private boolean isLocalAutoCommit() {
		return localAutoCommit;
	}
	/**
	 * @param localAutoCommit The localAutoCommit to set.
	 */
	private void setLocalAutoCommit(boolean localAutoCommit) {
		this.localAutoCommit = localAutoCommit;
	}
//	/**
//	 * @param statementLog The statementLog to set.
//	 */
//	private void setStatementLog(Statement statementLog) {
//		this.statementLog = statementLog;
//	}
    /**
     * @return Returns the statementLOG.
     */
    private PreparedStatement getStatementLOG() {
        return statementLOG;
    }        
    
    synchronized protected void beginTransaction() throws SQLException{
        
        try {
            
            getDriver().getTransactionManager().begin();
                        
            transaction = getDriver().getTransactionManager().getTransaction();           
            

            if(!transaction.enlistResource(getDriver().getXcSQL().getXAResource()))
                throw new SQLException("It is not possible to enlist the resource");
                        

            if(!transaction.enlistResource(getDriver().getXcLOG().getXAResource()))
                throw new SQLException("It is not possible to enlist the resource");           
            
            uid = new UID();

        } catch (IllegalStateException e) {
            throw new SQLException(e.getMessage());
        } catch (NotSupportedException e) {
            throw new SQLException(e.getMessage());
        } catch (SystemException e) {
            throw new SQLException(e.getMessage());
        } catch (RollbackException e) {
            throw new SQLException(e.getMessage());
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }
    
    synchronized private void commitTransaction() throws SecurityException, HeuristicMixedException, HeuristicRollbackException, IllegalStateException, NotSupportedException, RollbackException, SystemException, SQLException{
                
        log.debug("COMMITING THE TRANSACTION");
        
//        StringBuffer sqlStatement = new StringBuffer();
//        
//        if(!listStatements.isEmpty()){
//            
//            Iterator iterator = listStatements.iterator();
//            
//            while(iterator.hasNext()){            
//                
//                IStatementProxy proxy = (IStatementProxy)iterator.next();                
//                String sql = proxy.getSqlLog();
//                
//                proxy.clearSqlLog(sql.length());
//                
//                sqlStatement.append(sql);
//                
//                log.debug("ELEMENT = "+sql);                
//            }            
//        }
//        
//        executeLOG(sqlStatement.toString());
                       
        executeLOGCommit();
        
        transaction.commit();                      
    }
    
    synchronized private void rollbackTransaction() throws IllegalStateException, NotSupportedException, RollbackException, SystemException, SQLException{
        transaction.rollback();     
    }
    
    public String toString(){
        StringBuffer ret = new StringBuffer();
        
        ret.append("The log statement : "+LOG_STRING);
        ret.append("The log commit statement : "+LOG_COMMIT_STRING);
        ret.append("\nThe connection : "+getConnection());
        ret.append("\nThe transaction :"+transaction);
        
        return ret.toString();
    }
    
    protected void setInsertStatement(String q){        
        LOG_STRING = q;
    }
    
    protected void setCommitStatement(String q){        
        LOG_COMMIT_STRING = q;
        if (LOG_COMMIT_STRING != null) {
          //count parameters 
          LOG_COMMIT_PARAMETERCOUNT = new VarStr(LOG_COMMIT_STRING).countOccurences("?"); 
        }
    }
    
//    protected void addStatement(IStatementProxy obj){
//        
//        if(listStatements.contains(obj))
//            return;
//        
//        listStatements.add(obj);
//    }
    
//    protected void removeStatement(IStatementProxy obj) throws RuntimeException{
//        
//		if(transaction != null){
//		    
//			try {
//	            if((transaction.getStatus() != Status.STATUS_NO_TRANSACTION) &&
//	                    (transaction.getStatus() != Status.STATUS_COMMITTED) && 
//	                    (transaction.getStatus() != Status.STATUS_ROLLEDBACK) ){	   
//	                throw new RuntimeException("Cannot be removed because it is within a transaction.");
//	            }	            
//	            
//	        } catch (SystemException e) {
//	            throw new RuntimeException(e.getMessage());
//	        }		
//		}    
//		
//        listStatements.remove(obj);
//    }
    
	protected void executeLOG(String sql) throws SQLException{
	    	    
		    //PreparedStatement st = this.getStatementLOG();	    
		    
	    	this.statementLOG = getDriver().getConnectionLOG().prepareStatement(LOG_STRING); // insert (?,timestamp,'t426'
	    
		    getStatementLOG().setString(1,sql);
		    getStatementLOG().setString(2,uid.toString());
		
		    getStatementLOG().execute();	    
			log.debug("StatementProxy --> executing LOG : "+sql);
			
	}
	
	protected void executeLOGCommit() throws SQLException{
	    	    	    
		this.statementLOG = getDriver().getConnectionLOG().prepareStatement(LOG_COMMIT_STRING);
		  
	    getStatementLOG().setString(1,uid.toString());
	    if (LOG_COMMIT_PARAMETERCOUNT > 1) {//zbog starih verzija bez SENDER_ID kolone u RA_COMMIT_LOG
	      getStatementLOG().setString(2, driver.getSender());
	    }
	    getStatementLOG().execute();	    
		log.debug("StatementProxy --> executing LOG : "+"commit");
		
	}
  public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }
  public int getHoldability() throws SQLException {
    // TODO Auto-generated method stub
    return 0;
  }
  public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }
  public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }
  public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }
  public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }
  public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    // TODO Auto-generated method stub
    
  }
  public void rollback(Savepoint savepoint) throws SQLException {
    // TODO Auto-generated method stub
    
  }
  public Savepoint setSavepoint() throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }
  public Savepoint setSavepoint(String name) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }
//  public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
//    // TODO Auto-generated method stub
//    
//  }	
public void setTypeMap(Map arg0) throws SQLException {
// TODO Auto-generated method stub

}   
}
