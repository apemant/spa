/****license*****************************************************************
**   file: Receiver.java
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
 * Created on Dec 6, 2004
 *
 */
package hr.restart.db.replication.ws;

/**
 * @author dgradecak
 *
 */
//import hr.restart.db.replication.server.logging.DriverProxy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Receiver {

    
    private Properties props = null;
    private Connection conn = null;
    private String driver = null;
    private String dbUrl = null;
    private Logger log = null;

    public Receiver(){
        
    }
    
	public void init() throws InstantiationException{

	    log = Logger.getLogger(Receiver.class);
		try{
		    log.debug("Initializing ...");		   
		    
		    
		    	Class.forName(getDriver());
		    
			    conn = DriverManager.getConnection(getDbUrl(),getJdbcProps());
			    			    			    
			    
			    log.debug("Initialization done");
			    			    
			}catch(Exception e){			    
			    e.printStackTrace();
			    log.fatal(e.getMessage());
			    throw new InstantiationException(e.getMessage()); 
			}
	}
    
    synchronized public boolean execute(String txt[]) throws SQLException{
        
        conn.setAutoCommit(false);
        
        List list = Arrays.asList(txt);      
        
        log.debug("The string list : "+list);
        
        
        Statement statement = null;
        
        try {            
	        statement = conn.createStatement();            
            
            Iterator iterator = list.iterator();            
	   
            while(iterator.hasNext()){
                String x = (String)iterator.next();				    	
                
                
                //DriverProxy.setSender(name);
                statement.execute(x);
                log.debug("Exectued : "+x);
            }	   
            
            conn.commit();
            log.debug("Commit ok");
        } catch (SQLException e) {	
            conn.rollback();
            log.fatal("rollbacking : "+e.getMessage());            
            throw e;
        }finally{            
            if(statement != null){
                statement.close();
                log.debug("Closing the statement");
            }
            /*if(conn != null)                
                conn.close();            
		*/
        }
                                    
        log.debug("returning true");
        return true;
    }
    /**
     * @return Returns the props.
     */
    public Properties getJdbcProps() {
        return props;
    }
    /**
     * @param props The props to set.
     */
    public void setJdbcProps(Properties props) {
        this.props = props;
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
}