/****license*****************************************************************
**   file: JDBCResource.java
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
/**
 * TODO: U execute name je dodan al se ne logira
 */


package hr.restart.db.replication.dump;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;

import org.apache.log4j.Logger;
import org.enhydra.jdbc.standard.StandardXADataSource;

/**
 * @author dgradecak
 *
 */
public class JDBCResource implements IXAResource {

    private Properties jdbcProperties = null;
    
    private XAConnection xaJdbcConnection = null;
    private Connection jdbcConnection = null;
        
    private Logger log = null;
    
    /**
     * 
     * @throws SQLException
     */
    public JDBCResource(Properties props) throws SQLException{
        setJdbcProperties(props);
    }
    
    public void init() throws SQLException{
        
        log = Logger.getLogger(JDBCResource.class);
        
        StandardXADataSource xdsSQL = new StandardXADataSource();				
		xdsSQL.setDriverName((String)getJdbcProperties().get("driver"));						
		xdsSQL.setUrl((String)getJdbcProperties().get("url"));
		xdsSQL.setUser((String)getJdbcProperties().get("user"));
		xdsSQL.setPassword((String)getJdbcProperties().get("password"));									
		xaJdbcConnection = xdsSQL.getXAConnection();
		
		jdbcConnection = xaJdbcConnection.getConnection();
		jdbcConnection.setTransactionIsolation(java.sql.Connection.TRANSACTION_REPEATABLE_READ);
    }
    /* (non-Javadoc)
     * @see hr.restart.db.replication.dump.IXAResource#getXAResource()
     */
    public XAResource getXAResource() throws RuntimeException{
        XAResource r = null;
        
        try {
            r = xaJdbcConnection.getXAResource();
        } catch (SQLException e) {            
            throw new RuntimeException(e.getMessage());
        }
        
       return r;
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
    /* (non-Javadoc)
     * @see hr.restart.db.replication.dump.IJDBCXAResource#getQuery()
     */
    public boolean execute(String name, String text[]) throws RuntimeException {
        boolean ret = false;
        
        List list = Arrays.asList(text);
        
        try {                        
            
            Statement st = jdbcConnection.createStatement();
            
            Iterator iterator = list.iterator();
            while(iterator.hasNext()){
                String elem = (String)iterator.next();
                System.out.println("RESOURCE "+elem);
                ret = st.execute(elem);                                
            }
            ret = true;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }                        
        
        return ret;
    }
}
