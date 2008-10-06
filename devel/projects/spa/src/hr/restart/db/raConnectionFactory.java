/****license*****************************************************************
**   file: raConnectionFactory.java
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
import hr.restart.baza.KreirDrop;
import hr.restart.baza.dM;
import hr.restart.util.VarStr;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class raConnectionFactory {
  private static Connection connection;
  private static java.util.Hashtable connections = new java.util.Hashtable();  

  public static java.sql.Connection openDefaultConnection() throws Exception {
    return getConnection(getRestArtConnectionProperties());
/*
    Class drvc = Class.forName(param.getTag("tip"));
    Driver drv = (Driver)drvc.newInstance();
    DriverManager.registerDriver(drv);
    connection = DriverManager.getConnection(param.getTag("url"),props);
    return connection;
*/
  }
  public static java.util.Properties getRestArtConnectionProperties() {
    hr.restart.util.IntParam param = null;
    java.util.Properties props = new java.util.Properties();
    props.setProperty("url",param.getTag("url"));
    props.setProperty("driver",param.getTag("tip"));
    props.setProperty("user",param.getTag("user"));
    props.setProperty("password",param.getTag("pass"));
    props.setProperty("charSet", "Cp1250"); //HC!!!
    return props;
  }
  public static java.sql.Connection getDMConnection() {
//    connection = hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection();
//    return connection;
    java.util.Properties cProps = new java.util.Properties();
    cProps.setProperty("url",hr.restart.baza.dM.getDataModule().getDatabase1().getConnection().getConnectionURL());
    cProps.setProperty("driver",hr.restart.baza.dM.getDataModule().getDatabase1().getConnection().getDriver());
    cProps.setProperty("user",hr.restart.baza.dM.getDataModule().getDatabase1().getConnection().getUserName());
    cProps.setProperty("password",hr.restart.baza.dM.getDataModule().getDatabase1().getConnection().getPassword());
    java.util.Properties dmProps = hr.restart.baza.dM.getDataModule().getDatabase1().getConnection().getProperties();
    if (dmProps !=null) {
      java.util.Enumeration pnames = dmProps.propertyNames();
      while (pnames.hasMoreElements()) {
        String pn = (String)pnames.nextElement();
        cProps.put(pn,dmProps.getProperty(pn));
      }
    }
    return getConnection(cProps);
  }

  public static Connection getConnection(java.util.Properties props) {
    connectionIndex cIdx = getConnectionIndex(props);
    if (cIdx == null) return null;
    return getConnection(cIdx);
  }

  public static Connection getConnection(String url, java.util.Properties connProps) {
    connectionIndex cIdx = getConnectionIndex(url,connProps);
    if (cIdx == null) return null;
    return getConnection(cIdx);
  }
  public static Connection getConnection(String url, String driver, java.util.Properties connProps) {
    connectionIndex cIdx = getConnectionIndex(url, driver, connProps);
    if (cIdx == null) return null;
    return getConnection(cIdx);
  }
  private static Connection getConnection(connectionIndex cIdx) {
    Object o = connections.get(cIdx.getIndex());
    if (o == null) {
      try {
        System.out.println("getting new connection "+cIdx.url);
        connection = DriverManager.getConnection(cIdx.url,cIdx.connProps);
        connections.put(cIdx.getIndex(),connection);
      }
      catch (SQLException ex) {
        connection = null;
      }
    } else {
      connection = (Connection)o;
    }
    return connection;
  }

  private static connectionIndex getConnectionIndex(String url, String driver, java.util.Properties props) {
    java.util.Properties newProps = (java.util.Properties)props.clone();
    newProps.setProperty("url",url);
    newProps.setProperty("driver",driver);
    return getConnectionIndex(newProps);
  }
  private static connectionIndex getConnectionIndex(String url, java.util.Properties props) {
    java.util.Properties newProps = (java.util.Properties)props.clone();
    newProps.setProperty("url",url);
    return getConnectionIndex(newProps);
  }
  private static connectionIndex getConnectionIndex(java.util.Properties props) {
    String driver = props.getProperty("driver");
    String url = props.getProperty("url");
    if (url == null) return null;
    if (getDriver(url) == null) {
      if (driver == null) return null;
      try {
        Class cdrv = Class.forName(driver);
        Driver drv = (Driver)cdrv.newInstance();
        DriverManager.registerDriver(drv);
      }
      catch (Exception ex) {
        return null;
      }
    }
    java.util.Properties connProps = new java.util.Properties();
    java.util.Enumeration propNames = props.propertyNames();
    while (propNames.hasMoreElements()) {
      String pn = (String)propNames.nextElement();
      if (!(pn.equals("url") || pn.equals("driver")))
        connProps.setProperty(pn,props.getProperty(pn));
    }
    return new connectionIndex(url,connProps);
  }

  private static Driver getDriver(String url) {
    try {
      return DriverManager.getDriver(url);
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * @deprecated
   * @return
   */
  public static Connection getConnection() {
    return connection;
  }

  public static String[] getKeyColumns(java.sql.Connection con,String tableName) {
    if (!dM.modulesLoaded) dM.getDataModule().loadModules();
  	KreirDrop kd = KreirDrop.getModuleByName(tableName);
    if (kd != null) {
      return kd.pkey;
    }
  		
    try {
      String[] keys;
      java.sql.ResultSet reskys = con.getMetaData().getPrimaryKeys(null,null,tableName);
      java.util.TreeMap kys = new java.util.TreeMap();
      while (reskys.next()) {
        kys.put(new Short(reskys.getShort("KEY_SEQ")),reskys.getString("COLUMN_NAME").toUpperCase());
      };
      keys = new String[kys.size()];
//      keys = (String[])kys.toArray(keys);
      Object[] mapKeys = kys.keySet().toArray();
      for (int i = 0; i < mapKeys.length; i++) {
        keys[i] = (String)kys.get(mapKeys[i]);
      }
      System.err.println("KEYS: "+VarStr.join(keys, ','));
      return keys;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }
  /**
   * VarStr.join(hr.restart.db.raConnectionFactory
   *    .getColumns(hr.restart.baza.dM.getDatabaseConnection(), "odbici"), ',');
   * @param con
   * @param tableName
   * @return
   */
  public static String[] getColumns(java.sql.Connection con,String tableName) {
    String[] cols = null;
    try {
      java.sql.ResultSet rescls = con.getMetaData().getColumns(null,null,tableName,null);
      java.util.TreeMap cls = new java.util.TreeMap();
      while (rescls.next()) {
        cls.put(new Integer(rescls.getInt("ORDINAL_POSITION")),rescls.getString("COLUMN_NAME").toUpperCase());
      };
      cols = new String[cls.size()];
//      keys = (String[])kys.toArray(keys);
      Object[] mapKeys = cls.keySet().toArray();
      for (int i = 0; i < mapKeys.length; i++) {
        cols[i] = (String)cls.get(mapKeys[i]);
      }
//      return cols;
    }
    catch (Exception ex) {
      cols = null;
    }
    if (cols!=null && cols.length > 0) return cols;
    else {
      if (!dM.modulesLoaded) dM.getDataModule().loadModules();
        KreirDrop kd = KreirDrop.getModuleByName(tableName);
      if (kd != null) {
        return kd.colnames;
      }
    }
    return null;
  }
}

class connectionIndex {
  String url;
  java.util.Properties connProps;
  String index;
  public connectionIndex(String _url, java.util.Properties _connProps) {
    url = _url;
    connProps = _connProps;
    makeIndex();
  }

  void makeIndex() {
    String ret = url;
    java.util.TreeSet sortProp = new java.util.TreeSet(connProps.values());
    for (java.util.Iterator i = sortProp.iterator(); i.hasNext(); ) {
      Object item = i.next();
      ret = ret.concat("<-o->").concat(item.toString());
    }
    index = ret;
  }

  public String getIndex() {
    return index;
  }

//  public boolean equals(Object obj) {
//System.out.println("connectionIndex.equals(Object obj)");
///** @todo kod get-a uopce ne ulazi ovdje ... rijesiti */
//    if (obj instanceof connectionIndex) {
//      connectionIndex compareIdx = (connectionIndex)obj;
//System.out.println("skasto compareidx");
//      if (!this.url.equals(compareIdx.url)) return false;
//System.out.println("url equals "+url);
//System.out.println("connProps su uvije iste ali ... ");
//      if (!this.connProps.equals(compareIdx.connProps)) return false; //hm?
//System.out.println("i sada su iste!!! ");
//      return true;
//    }
//    return false;
//  }
}
