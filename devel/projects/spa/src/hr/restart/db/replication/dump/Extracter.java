/****license*****************************************************************
**   file: Extracter.java
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
 * Created on Nov 12, 2004
 *
 */
package hr.restart.db.replication.dump;

import hr.restart.util.sysoutTEST;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.sql.XAConnection;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.apache.log4j.Logger;
import org.enhydra.jdbc.standard.StandardXADataSource;

/**
 * @author dgradecak
 *
 * TODO: rules po tablicama
 */
public class Extracter {

    private String replicate = "none";
    private boolean repl = false;
    private Properties jdbcProperties = null;
    
    private XAConnection xaJdbcConnection = null;
    private Connection jdbcConnection = null;
    
    private TransactionManager transactionManager = null;
    private Transaction transaction = null;
    
    private Logger log = null;
    
    private IResource resource = null;
    
    private PreparedStatement update = null;
    
    private ArrayList listTables = null;
    
    private String id = null;
    
    
    //        
    private Statement statementCommits = null;  
    private PreparedStatement statementLogs = null;
    //
    
    public Extracter(Properties props){
        setJdbcProperties(props);
    }
    
    public void init() throws SQLException{        
        
        log = Logger.getLogger(Extracter.class);
        
        StandardXADataSource xdsSQL = new StandardXADataSource();	
        
		xdsSQL.setDriverName((String)getJdbcProperties().get("driver"));						
		xdsSQL.setUrl((String)getJdbcProperties().get("url"));
		xdsSQL.setUser((String)getJdbcProperties().get("user"));
		xdsSQL.setPassword((String)getJdbcProperties().get("password"));	
		
		xaJdbcConnection = xdsSQL.getXAConnection();

        jdbcConnection = xaJdbcConnection.getConnection();		
		jdbcConnection.setTransactionIsolation(java.sql.Connection.TRANSACTION_REPEATABLE_READ);
		
    }
    
    synchronized private void beginTransaction() throws SQLException{
        
        try {
            
            
            transactionManager.begin();                        
            transaction = transactionManager.getTransaction();           
            
            enlistResources();

        } catch (IllegalStateException e) {
            rollbackTransaction();                        
            throw new SQLException(e.getMessage());
        } catch (NotSupportedException e) {
            rollbackTransaction();
            throw new SQLException(e.getMessage());
        } catch (SystemException e) {
            rollbackTransaction();
            throw new SQLException(e.getMessage());
        } catch (RuntimeException e){
            rollbackTransaction();
            throw new SQLException(e.getMessage());
        }
    }
    
    synchronized private void commitTransaction() throws RuntimeException{
        
        try {
            transaction.commit();
            log.debug("commiting the transaction");
        } catch (SecurityException e) {            
            rollbackTransaction();            
            throw new RuntimeException(e.getMessage());
        } catch (RollbackException e) {
            rollbackTransaction();
            throw new RuntimeException(e.getMessage());
        } catch (HeuristicMixedException e) {
            rollbackTransaction();
            throw new RuntimeException(e.getMessage());
        } catch (HeuristicRollbackException e) {
            rollbackTransaction();
            throw new RuntimeException(e.getMessage());
        } catch (SystemException e) {
            rollbackTransaction();
            throw new RuntimeException(e.getMessage());
        }                      
    }
    
    synchronized private void rollbackTransaction() throws RuntimeException{
        
        try {
            transaction.rollback();
            log.debug("rollbacking the transaction");
        } catch (IllegalStateException e) {
            throw new RuntimeException(e.getMessage());
        } catch (SystemException e) {
            throw new RuntimeException(e.getMessage());
        }     
    }
    
    synchronized private boolean enlistResources() throws RuntimeException{
        
        boolean ret = false;
        
	        try {	            
	            //enlist all resources
	            ret = transaction.enlistResource(xaJdbcConnection.getXAResource());
	            
	            if((getResource() != null) && (getResource() instanceof IXAResource))
	                ret = ret & transaction.enlistResource(((IXAResource)getResource()).getXAResource());	            	            
	            
	            
	            update = jdbcConnection.prepareStatement("update ra_commit_log set commit_status = '1' where log_txid = ?");	    		
	    		
	            //statementCommits = jdbcConnection.createStatement();
	            //statementLogs = jdbcConnection.prepareStatement("SELECT * from ra_log where log_txid = ? order by log_id");
	            
	            
	            log.debug("resources enlisted");
	        } catch (IllegalStateException e) {

	            throw new RuntimeException(e.getMessage());
	        } catch (RollbackException e) {

	            throw new RuntimeException(e.getMessage());
	        } catch (SystemException e) {

	            throw new RuntimeException(e.getMessage());	            
	        } catch (SQLException e) {

	            throw new RuntimeException(e.getMessage());
	        }
        
        return ret;
    }
    /**
     * @return Returns the jdbcProperties.
     */
    private Properties getJdbcProperties() {
        return jdbcProperties;
    }
    /**
     * @param jdbcProperties The jdbcProperties to set.
     */
    private void setJdbcProperties(Properties jdbcProperties) {
        this.jdbcProperties = jdbcProperties;
    }
    
    public void setTransactionManager(String tm) throws InstantiationException{
        
        
        if(transactionManager != null)
            throw new InstantiationException("The TransactionManager has been set already");
        
            try {
                transactionManager = (TransactionManager)Class.forName(tm).newInstance();
                //log.debug("the transaction manager is set (class): "+tm);
            } catch (InstantiationException e) {
                throw new InstantiationException(e.getMessage());
            } catch (IllegalAccessException e) {
                throw new InstantiationException(e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new InstantiationException(e.getMessage());
            }        
    }        
    
    private ResultSet retrieveCommits() throws SQLException{

	        ResultSet rs = statementCommits.executeQuery("SELECT * FROM ra_commit_log where commit_status='0' order by commit_id");	        	        
        return rs;
    }
    
    private ResultSet retrieveLogs(String txid) throws SQLException{
        
        statementLogs.setString(1, txid);
        ResultSet rs = statementLogs.executeQuery();
        
        return rs;
    }
    
    synchronized public void extract() throws SQLException{
                                
        	try {
        	    log.debug("begining the extraction");
        	            	    
        	    statementCommits = jdbcConnection.createStatement();        	    
	            statementLogs = jdbcConnection.prepareStatement("SELECT * from ra_log where log_txid = ? order by log_id");
        	    
                ResultSet rs = retrieveCommits();
                
                List listCommits = new ArrayList();
                
                while(rs.next()){                    
                    listCommits.add(rs.getString("log_txid"));
                }
      
                // testing
                boolean logged = false;
                                
                for( int i = 0; i<listCommits.size(); i++){
                    //beginTransaction();
                    //enlistResources();
                    
                    logged = false;
                    
                    
                    ResultSet logs = retrieveLogs((String)(listCommits.get(i)));
                    
                    List listLogs = new ArrayList();
                    while(logs.next()){
                        listLogs.add(logs.getString(2));
                    }
                                       
                    ArrayList toLog = new ArrayList();
                    
                    for(int j = 0; j< listLogs.size(); j ++){
                        
                        
                        String l = (String)listLogs.get(j);//logs.getString(2);                                                                                     
                        
                        boolean loggable = analyze(l);
                        
                        if(loggable){
	                        // write to the resource or call the WEB SERVICE
	                        if(getResource() != null){
	                            
	                            
//	                             if( ! getResource().execute(toLog)){
//	                                 throw new RuntimeException("the resource could not execute the operation");
//	                             }
	                                 
	                             toLog.add(l);
	                             System.out.println(l);
	                             
	                             logged = true;	                             
		                            
	                             log.debug("to log : "+l);	                             
	                        }else	 
	                            log.warn("The statement '"+l+"' should be logged but there is not loggable resources");
                        }else
                            log.debug("the statement '" +l+"' is not loggable");
                    }
                    
                    if(logged){
                        beginTransaction();
                        
                        if( ! getResource().execute( getId() ,(String []) toLog.toArray (new String[toLog.size()]) )){
                            
                            throw new RuntimeException("the resource could not execute the operation");
                        }
                        log.debug("LOG DONE");
                        
                        //update the commit                                                
                        String id = (String)listCommits.get(i);//rs.getString("log_txid");
                        update.setString(1, id);                        
                        update.execute();
                        
                                                
                        log.debug("updating commit status for transaction id : "+id);
                        //break;
                        
                        commitTransaction();
                    }
                    //commitTransaction();
                }
                
                                  
                log.debug("extract done");
                
            } catch (Throwable e) {
                e.printStackTrace();
                log.debug(e.getMessage());
                rollbackTransaction();
            }
    }
    /**
     * @return Returns the resource.
     */
    public IResource getResource() {
        return resource;
    }
    /**
     * @param resource The resource to set.
     */
    public void setResource(IResource resource){
        if(this.resource != null)
            throw new RuntimeException("Can not change the resource value because it is already done!");
        
        this.resource = resource;
    }
    /**
     * @return Returns the listTables.
     */
    public ArrayList getListTables() {
        return listTables;
    }
    /**
     * @param listTables The listTables to set.
     */
    public void setListTables(ArrayList listTables) {
        
        ArrayList x = new ArrayList();
        
        Iterator i = listTables.iterator();
        
        while(i.hasNext()){
            String s = (String)i.next();
            
            x.add(s.toUpperCase());
        }
        
        
        this.listTables = x;        
    }
    
    /**
     * 
     * @return false if not in list, otherwise true
     */
    private boolean analyze(String sql){
                
        StringTokenizer stk = new StringTokenizer(sql, " ");//first separate with the blank spaces
        
        String elem = stk.nextToken();

//        System.out.println("TO ANALYZE : "+elem.toUpperCase());
        log.debug("TO ANALYZE : "+elem.toUpperCase());
     
        
        if(elem.toUpperCase().equals("INSERT")){
            elem = stk.nextToken();
            elem = stk.nextToken().toUpperCase();
            
            StringTokenizer stk2 = new StringTokenizer(elem, "("); //then separate with (
            elem = stk2.nextToken(); 
            
            return listTablesContains(elem);
            
        }else if (elem.toUpperCase().equals("UPDATE")){
            elem = stk.nextToken().toUpperCase();            
            return listTablesContains(elem);
            
        }else if (elem.toUpperCase().equals("DELETE")){
            elem = stk.nextToken();            
            elem = stk.nextToken().toUpperCase();
            return listTablesContains(elem);
            
        }else{
            log.fatal("TO ANALYZE : "+elem.toUpperCase());   
            throw new IllegalArgumentException("That should never happen. There is an invalid SQL operation");
        }
                
        //return false;
    }
    private boolean listTablesContains(String elem) {
      boolean contains = listTables.contains(elem);
      if (repl) {
        return !contains;
      } else {
        return contains;
      }
    }

    public String getReplicate() {
      return replicate;
    }
    public void setReplicate(String replicate) {
      if (!replicate.toUpperCase().equals("NONE") && !replicate.toUpperCase().equals("ALL")) {
        throw new IllegalArgumentException("invalid replicate tag in dump.xml"); 
      }
      this.replicate = replicate;
      repl = replicate.toUpperCase().equals("ALL");
    }
    /**
     * @return Returns the clientName.
     */
    public String getId() {
        return id;
    }
    /**
     * @param clientName The clientName to set.
     */
    public void setId(String clientName) {
        this.id = clientName;
    }
}
