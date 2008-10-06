/****license*****************************************************************
**   file: Sender.java
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
 * Created on Dec 7, 2004
 *
 */
package hr.restart.db.replication.ws;

import hr.restart.util.sysoutTEST;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * @author dgradecak
 *
 */
public class Sender {

    static private Logger log = null;
    private Properties jdbcProps = null;
    private String driver = null;
    private String dbUrl = null;
    
    private Connection conn = null;
    
    private PreparedStatement ps = null;
    private PreparedStatement psLog = null;
    
    public Sender(){
        
    }
    
    public void init(){
        System.out.println("INIT .....");
                
        log = Logger.getLogger(Sender.class);
        log.debug("Initializing ...");
        
        	try {
                Class.forName(getDriver());
                log.debug("Driver "+getDriver()+" loaded");
   
                conn = DriverManager.getConnection(getDbUrl(),getJdbcProps());
                log.debug("JDBC Connection ok");
                
                log.debug("Creating a prepared statement");
                ps = conn.prepareStatement("select log_txid, commit_id from ra_commit_log where sender_id <> ? and commit_id > ? order by commit_id");
                psLog = conn.prepareStatement("select log_sql from ra_log where log_txid = ?");
                log.debug("PreparedStatement created");
                
            } catch (ClassNotFoundException e) {
                log.fatal("ClassNotFoundException : "+e.getMessage());
                throw new InstantiationError(e.getMessage());
            } catch (SQLException e) {
                log.fatal("SQLException : "+e.getMessage());
                throw new InstantiationError(e.getMessage());
            }                    
        
        log.debug("Initialization ok");
    }
    
    synchronized public String [] execute(String id, int last, String [] tables) throws SQLException {

        
        System.out.println("EXECUTE .....");
        
        log.debug("Executing ...");
        List list = new ArrayList();
        
        try {
            log.debug("Setting the connection to autocommit false mode");
            conn.setAutoCommit(false);
            log.debug("Autocommit false ok");

            log.debug("Executing a query");         
            ps.setString(1, id);
            ps.setInt(2, last);
            ResultSet rs = ps.executeQuery();            
            log.debug("Query exectued");            
            
            System.out.println("RETRIEVING ALL .....");
            
            if(rs.next()){
                System.out.println("RETRIEVING ALL AFTER .....");
                
	            String txId = rs.getString("log_txid");	            
	            
	            log.debug("Executing a query");
	            psLog.setString(1, txId);
	            ResultSet rsLog = psLog.executeQuery();
	            log.debug("Query exectued");	           	           		            
	            
	            boolean logged = false;
	            while(rsLog.next()){
	                //System.out.println(rs.getString("commit_id"));
	                String txt = rsLog.getString("log_sql");
	                
	                boolean toLog = analyze(txt, tables);
	                System.out.println(toLog);
	                if(toLog){	                    
	                    list.add(txt);
	                    logged = true;
	                }
	            }
	            if(logged)
	                list.add(0,rs.getString("commit_id"));
            }
            
	        log.debug("Preparing to commit");
	        conn.commit();
            log.debug("Commit ok");
                        
        } catch (SQLException e) {
            log.debug("Rollbacking the transaction");
            conn.rollback();
            log.debug("Rollback ok");
            throw e; 
        } finally {
//            if(statement != null){
//                log.debug("Closing the statement");
//                statement.close();
//                log.debug("Statement closed");
//            }
            /*if(conn != null)                
                conn.close();            
                */
        }
        
        log.debug("Execute done");
        return (String[])list.toArray(new String[list.size()]);
    }
    /**
     * @return Returns the jdbcProps.
     */
    public Properties getJdbcProps() {
        return jdbcProps;
    }
    /**
     * @param jdbcProps The jdbcProps to set.
     */
    public void setJdbcProps(Properties jdbcProps) {
        this.jdbcProps = jdbcProps;
    }
    /**
     * @return Returns the dbUrl.
     */
    public String getDbUrl() {
        return dbUrl;
    }
    /**
     * @param dbUrl The dbUrl to set.
     */
    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }
    /**
     * @return Returns the driver.
     */
    public String getDriver() {
        return driver;
    }
    /**
     * @param driver The driver to set.
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }
    
    /**
     * 
     * @return false if not in list, otherwise true
     */
    private boolean analyze(String sql, String [] tables){
                
        StringTokenizer stk = new StringTokenizer(sql, " ");//first separate with the blank spaces
        
        String elem = stk.nextToken();

        System.out.println("TO ANALYZE : "+elem.toUpperCase());
        log.debug("TO ANALYZE : "+elem.toUpperCase());
     
        List listTables = Arrays.asList(tables);
        
        if(elem.toUpperCase().equals("INSERT")){
            elem = stk.nextToken();
            elem = stk.nextToken().toUpperCase();
            
            StringTokenizer stk2 = new StringTokenizer(elem, "("); //then separate with (
            elem = stk2.nextToken();                         
            
            return listTablesContains(elem, listTables);
            
        }else if (elem.toUpperCase().equals("UPDATE")){
            elem = stk.nextToken().toUpperCase();            
            return listTablesContains(elem, listTables);
            
        }else if (elem.toUpperCase().equals("DELETE")){
            elem = stk.nextToken();            
            elem = stk.nextToken().toUpperCase();
            return listTablesContains(elem, listTables);
            
        }else{
            log.fatal("TO ANALYZE : "+elem.toUpperCase());   
            throw new IllegalArgumentException("That should never happen. There is an invalid SQL operation");
        }
                
        //return false;
    }
    private boolean listTablesContains(String elem, List listTables) {
        
        System.out.println(listTables);
        
        boolean contains = listTables.contains(elem);        
        
        return contains;                  
      }
}
