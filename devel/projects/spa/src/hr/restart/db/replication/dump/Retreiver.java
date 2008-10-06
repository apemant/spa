/****license*****************************************************************
**   file: Retreiver.java
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
package hr.restart.db.replication.dump;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.sql.XAConnection;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.enhydra.jdbc.standard.StandardXADataSource;

/**
 * @author dgradecak
 *
 */
public class Retreiver {

    private String id = null;
    
    private IRetriever resource = null;
    private Connection connSQL = null;
    private Connection connLOG = null;
    private Properties jdbcProperties = null;
    private Properties jdbcLogProperties = null;
    
    private XAConnection xcSQL = null;
	private XAConnection xcLOG = null;
	
	private String transactionManagerClass = null;
	private TransactionManager transactionManager = null;
	
	private List tables = null;
    
    public Retreiver(){
        
    }
    
    public void init(){                
        
        try {
            transactionManager = (TransactionManager)Class.forName(getTransactionManagerClass()).newInstance();

            StandardXADataSource xdsSQL = new StandardXADataSource();				
			xdsSQL.setDriverName((String)getJdbcProperties().get("driver"));						
			xdsSQL.setUrl((String)getJdbcProperties().get("url"));
			xdsSQL.setUser((String)getJdbcProperties().get("user"));
			xdsSQL.setPassword((String)getJdbcProperties().get("password"));						
			xcSQL = xdsSQL.getXAConnection();
			connSQL = xcSQL.getConnection();
			connSQL.setTransactionIsolation(java.sql.Connection.TRANSACTION_REPEATABLE_READ);			
            
			StandardXADataSource xdsLOG = new StandardXADataSource();				
			xdsLOG.setDriverName((String)getJdbcLogProperties().get("driver"));						
			xdsLOG.setUrl((String)getJdbcLogProperties().get("url"));
			xdsLOG.setUser((String)getJdbcLogProperties().get("user"));
			xdsLOG.setPassword((String)getJdbcLogProperties().get("password"));				
			xcLOG = xdsLOG.getXAConnection();			
			connLOG = xcLOG.getConnection();			
			connLOG.setTransactionIsolation(java.sql.Connection.TRANSACTION_REPEATABLE_READ);
			                    
        } catch (Throwable e) {
            e.printStackTrace();
            throw new IllegalAccessError(e.getMessage());
        }        
    }
    
    public boolean execute() throws SQLException{
        
        try {
            // uzmi zadnju transakciju iz baze            
            
            try {
                transactionManager.begin();
                
                //napravi statments
                
                try {
                    transactionManager.getTransaction().enlistResource(xcSQL.getXAResource());
                    transactionManager.getTransaction().enlistResource(xcLOG.getXAResource());
                } catch (RuntimeException e3) {
                    e3.printStackTrace();
                    throw e3;
                }            
            }catch (Throwable e) {
                e.printStackTrace();
                try {
                    transactionManager.rollback();                    
                } catch (IllegalStateException e3) {
                    e3.printStackTrace();
                    throw new RuntimeException(e3.getMessage());
                } catch (SecurityException e3) {
                    e3.printStackTrace();
                    throw new RuntimeException(e3.getMessage());
                } catch (SystemException e3) {
                    e3.printStackTrace();
                    throw new RuntimeException(e3.getMessage());
                }
                throw new IllegalAccessError(e.getMessage());
            }
            
            
            Statement st = connLOG.createStatement();
            ResultSet rs = st.executeQuery("select max(id) from last_tx");
            
            int max = 0;
            if(rs.next()){
                max = rs.getInt(1);
            }
                        
            //pozovi web servis
            String[] a = null;
            
            try {
                a = getResource().execute(getId(), max, (String [])getTables().toArray(new String[getTables().size()]));
                
                System.out.println(Arrays.asList(a));
            } catch (Throwable e) {
                e.printStackTrace();
                try {
                    transactionManager.rollback();                    
                } catch (Throwable e3) {
                    e3.printStackTrace();
                    throw new RuntimeException(e3.getMessage());
                }
                throw e;
            }       
                    
            List list = Arrays.asList(a);            
            Iterator it = list.iterator();

                        
            if(it.hasNext()){
                String x = (String)it.next();
	            if(max == 0){	                	                   
	                st.execute("insert into last_tx(id) values("+a[0]+")");
	            }else{
                    st.execute("update last_tx set id = "+a[0]);
	            }

	            Statement stSQL = connSQL.createStatement();
	            
	            while(it.hasNext()){       	            
	                x = (String)it.next();
	                //st.execute(x);
	                System.out.println(x);
	                stSQL.execute(x);
	            }
            }else{
                transactionManager.rollback();
                return false;
            }
            
            //ako je proslo dodaj u bazu i azuriraj zadnju transakciju
            

            try {
                transactionManager.commit(); // COMMIT
                
            
            } catch (Throwable e2) {
                e2.printStackTrace();
                try {
                    transactionManager.rollback();                    
                } catch (Throwable e3) {
                    e3.printStackTrace();
                    throw new RuntimeException(e3.getMessage());
                }
                throw new RuntimeException(e2.getMessage());
            }            
            
        } catch (Throwable e) {            
            e.printStackTrace();
            try {
                transactionManager.rollback();                    
            } catch (Throwable e3) {
                e3.printStackTrace();
                throw new RuntimeException(e3.getMessage());
            }
            throw new IllegalAccessError(e.getMessage());
        }

        return true;

    }
    /**
     * @return Returns the resource.
     */
    public IRetriever getResource() {
        return resource;
    }
    /**
     * @param resource The resource to set.
     */
    public void setResource(IRetriever resource) {
        this.resource = resource;
    }
    /**
     * @return Returns the jdbcProperties.
     */
    public Properties getJdbcProperties() {
        return jdbcProperties;
    }
    /**
     * @param jdbcProperties The jdbcProperties to set.
     */
    public void setJdbcProperties(Properties jdbcProperties) {
        this.jdbcProperties = jdbcProperties;
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
     * @return Returns the jdbcLogProperties.
     */
    public Properties getJdbcLogProperties() {
        return jdbcLogProperties;
    }
    /**
     * @param jdbcLogProperties The jdbcLogProperties to set.
     */
    public void setJdbcLogProperties(Properties jdbcLogProperties) {
        this.jdbcLogProperties = jdbcLogProperties;
    }
    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }
    
    public void setTables(List tables){
        this.tables = tables;
    }
    
    public List getTables(){
        return tables;
    }
}
