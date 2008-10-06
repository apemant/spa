/****license*****************************************************************
**   file: DriverProxy.java
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
 * Created on 07-Oct-2004
 *
 */

package hr.restart.db.replication.logging;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import javax.sql.XAConnection;
import javax.transaction.TransactionManager;

import org.apache.log4j.Logger;
import org.enhydra.jdbc.standard.StandardXADataSource;



/**
 * This is a driver implementation, that allows you to log and to debug 
 * every statement and prepared statements. This class can log into a stream 
 * the right query that has been executed, log4j is used.
 * 
 * It is in fact a proxy driver which works like every jdbc driver.
 * To use it you have to set the static variable REAL_DRIVER by using
 * DriverProxy.setDriverclass(YOUR_JDBC_DRIVER)
 * and then instanciate a DriverProxy object.
 * 
 * By default the log will be done with log4j, or you can setup another DB
 * where to log all the statements and preparedstatements. You can combine both.
 * 
 * @author gradecak
 *
 */
public class DriverProxy implements Driver {

    static String STATIC_DRIVER_CLASS = null; 
    
	private Driver driver = null;
	
	private String REAL_DRIVER = null;
	
	private String REAL_DB = null;
	
	private String LOG_DRIVER = null;
	private String LOG_DB = null;
	private String USER_LOG = null;
	private String PASS_LOG = null;
	
	
		
	private Connection connectionLOG = null;
	private Connection connectionSQL = null;	
	private TransactionManager transactionManager = null;
	private XAConnection xcSQL = null;
	private XAConnection xcLOG = null;
	private Logger log = null;

	
	private String transactionManagerClass = null;
	
	private String insert = null;
	private String commit = null;
	
	private String sender = null;
	
	// MORE backends
	//private Hashtable backendList = null;
	
	public DriverProxy() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {	    	    
	    this(STATIC_DRIVER_CLASS);
	}
	
	/**
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 * 
	 */	
	public DriverProxy(String realDriver) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

	    if(realDriver == null)
			throw new InstantiationException("The driver can not be null");
	    		
	    setDriverClass(realDriver);
	    
		log = Logger.getLogger(DriverProxy.class);

		setDriver((Driver)Class.forName(getDriverClass()).newInstance());
		
		Enumeration drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {			
			Driver d = (Driver) drivers.nextElement();
			
			if(d.getClass().getName().equals(getDriverClass())){
				DriverManager.deregisterDriver(d);
				log.debug("DriverProxy : driver removed - "+d.getClass().getName());
			}
		}
		
		
		//backendList = new Hashtable();
		
		DriverManager.registerDriver(this);
		log.debug("DriverProxy created");
	}
		
	/* (non-Javadoc)
	 * @see java.sql.Driver#acceptsURL(java.lang.String)
	 */
	public boolean acceptsURL(String url) throws SQLException {
		boolean ret = getDriver().acceptsURL(url);
		log.debug("DriverProxy.acceptsURL("+url+") == "+ret);
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
	 */	
	synchronized public Connection connect(String url, Properties info) throws SQLException {								    
	    
	    ConnectionProxy proxy = null;
	    try{
		setDataBaseName(url);
		
		Enumeration drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {			
			Driver d = (Driver) drivers.nextElement();
			
			if(d.getClass().getName().equals(DriverProxy.class.getName())){
				DriverManager.deregisterDriver(d);
				log.debug("DriverProxy : driver removed - "+d.getClass().getName());
			}
		}	
		DriverManager.registerDriver(getDriver());		
		log.debug("DriverProxy : driver registered - "+getDriver().getClass().getName());
		
			try {
                transactionManager = (TransactionManager)Class.forName(getTransactionManagerClass()).newInstance();
            } catch (InstantiationException e) {
                log.fatal(e);
                throw new SQLException(e.getMessage());
            } catch (IllegalAccessException e) {
                log.fatal(e);
                throw new SQLException(e.getMessage());
            } catch (ClassNotFoundException e) {
                log.fatal(e);
                throw new SQLException(e.getMessage());
            }							
							
			StandardXADataSource xdsSQL = new StandardXADataSource();				
			xdsSQL.setDriverName(getDriverClass());						
			xdsSQL.setUrl(getDataBaseName());
			xdsSQL.setUser((String)info.get("user"));
			xdsSQL.setPassword((String)info.get("password"));									
			xcSQL = xdsSQL.getXAConnection();
			connectionSQL = xcSQL.getConnection();
			connectionSQL.setTransactionIsolation(java.sql.Connection.TRANSACTION_REPEATABLE_READ);

			
			StandardXADataSource xdsLOG = new StandardXADataSource();			
			xdsLOG.setDriverName(getLogDriverClass());
			xdsLOG.setUrl(getLogDataBaseName());
			xdsLOG.setUser(getUserLOG());
			xdsLOG.setPassword(getPassLOG());						
			xcLOG = xdsLOG.getXAConnection();													
			connectionLOG = xcLOG.getConnection();			

				proxy = new ConnectionProxy(connectionSQL);
				proxy.setDriver(this);				
				proxy.setInsertStatement(getInsertStatement());
				proxy.setCommitStatement(getCommitStatement());

				
		log.debug("DriverProxy.connect("+getDataBaseName()+","+info+") == "+proxy);		
		
	    }catch(Throwable e){
	        e.printStackTrace();
	    }
		
		return proxy;
	}
	/* (non-Javadoc)
	 * @see java.sql.Driver#getMajorVersion()
	 */
	public int getMajorVersion() {
		int ret = getDriver().getMajorVersion();
		log.debug("DriverProxy.getMajorVersion() == "+ret);
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.Driver#getMinorVersion()
	 */
	public int getMinorVersion() {
		int ret = getDriver().getMinorVersion();
		log.debug("DriverProxy.getMinorVersion() == "+ret);
		return ret;
	}
	/* (non-Javadoc)
	 * @see java.sql.Driver#getPropertyInfo(java.lang.String, java.util.Properties)
	 */
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {

		DriverPropertyInfo[] infos = getDriver().getPropertyInfo(url, info);
		log.debug("DriverProxy.getPropertyInfo("+url+","+info+") == "+infos);
		return infos;
	}
	/* (non-Javadoc)
	 * @see java.sql.Driver#jdbcCompliant()
	 */
	public boolean jdbcCompliant()  {
		boolean ret = getDriver().jdbcCompliant();
		
		log.debug("DriverProxy.jdbcCompliant() == "+ret);
		
		return false;
	}
	/**
	 * @return Returns the driver.
	 */
	public Driver getDriver() {
		return driver;
	}
	/**
	 * @param driver The driver to set.
	 */
	private void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	public void setDriverClass(String name) throws IllegalAccessError{
		if(REAL_DRIVER != null)
			throw new IllegalAccessError("The driver is already set. It is not possible to change it again.");
		
		REAL_DRIVER = name;
	}
	
	public String getDriverClass(){
		return REAL_DRIVER;		
	}
	
	public void setLogDriverClass(String name) throws IllegalAccessError{
		if(LOG_DRIVER != null)
			throw new IllegalAccessError("The driver is already set. It is not possible to change it again.");
		
		LOG_DRIVER = name;
	}
	
	public String getLogDriverClass(){
		return LOG_DRIVER;		
	}
	
	public void setDataBaseName(String name) throws IllegalAccessError{	    	    
	    
		if(REAL_DB != null)
			throw new IllegalAccessError("The Database is already set. It is not possible to change it again.");
		
		REAL_DB = name;
	}
	
	public String getDataBaseName(){
		return REAL_DB;
	}
	
	public void setLogDataBaseName(String name) throws IllegalAccessError{
		if(LOG_DB != null)
			throw new IllegalAccessError("The Database is already set. It is not possible to change it again.");
		
		LOG_DB = name;
	}
	
	public String getLogDataBaseName(){
		return LOG_DB;
	}

	/**
	 * @return Returns the connectionLOG.
	 */
	protected Connection getConnectionLOG() {
		return connectionLOG;
	}
	/**
	 * @return Returns the connectionSQL.
	 */
	protected Connection getConnectionSQL() {
		return connectionSQL;
	}
	/**
	 * @return Returns the transactionManager.
	 */
	protected TransactionManager getTransactionManager() {
	    //transactionManager = new Current();
		return transactionManager;
	}
	/**
	 * @return Returns the xcLOG.
	 */
	protected XAConnection getXcLOG() {
		return xcLOG;
	}
	/**
	 * @return Returns the xcSQL.
	 */
	protected XAConnection getXcSQL() {	    
		return xcSQL;
	}

    /**
     * @return Returns the userSQL.
     */
    public String getUserLOG() {
        return USER_LOG;
    }
    /**
     * @param userSQL The userSQL to set.
     */
    public void setUserLOG(String user) {
        if( getConnectionLOG()!= null)
            throw new IllegalAccessError("The conenction is already done. It is not possible the user anymore.");
        
        USER_LOG = user;
    }
    /**
     * @return Returns the passSQL.
     */
    public String getPassLOG() {
        return PASS_LOG;
    }
    /**
     * @param passSQL The passSQL to set.
     */
    public void setPassLOG(String pass) {
        if(getConnectionLOG() != null)
            throw new IllegalAccessError("The conenction is opened already. It is not possible to change the password anymore.");
        
        PASS_LOG = pass;
    }
    
    static public void setJdbcDriverClass(String driverName){
        STATIC_DRIVER_CLASS = driverName;
    }
    /**
     * @param insert The insert to set.
     */
    synchronized public void setInsertStatement(String insert) {
        this.insert = insert;
    }
    
    synchronized public String getInsertStatement() {
        return this.insert;
    }
    /**
     * @return Returns the transactionManagerClass.
     */
    public String getTransactionManagerClass() {
        return transactionManagerClass;
    }
    /**
     * @param transactionManagerClass The transactionManagerClass to set.
     */
    public void setTransactionManagerClass(String transactionManagerClass) {
        this.transactionManagerClass = transactionManagerClass;
    }
    /**
     * @return Returns the commit.
     */
    public String getCommitStatement() {
        return commit;
    }
    /**
     * @param commit The commit to set.
     */
    public void setCommitStatement(String commit) {
        this.commit = commit;
    }
    
    /**
     * @return Returns the sender.
     */
    public String getSender() {
        return sender;
    }
    /**
     * @param sender The sender to set.
     */
    public void setSender(String s) {
        sender = s;
    }
}


