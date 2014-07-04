/****license*****************************************************************
**   file: dM.java
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
package hr.restart.baza;

import hr.restart.robno.raVart;
import hr.restart.sisfun.TextFile;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.IntParam;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.ConnectionDescriptor;
import com.borland.dx.sql.dataset.Database;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryResolver;

public class dM implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static dM myDM;
  
  private boolean isReplicating = false; 
  private String insertStatement = null;

  public static boolean modulesLoaded = false;
// Parametri iz fajla
  String conURL;// = paramReader.VratiSadrzajTaga("url");
  String conUSER;// = paramReader.VratiSadrzajTaga("user");
  String conPASS;// = paramReader.VratiSadrzajTaga("pass");
  String conTIP;// = paramReader.VratiSadrzajTaga("tip");
  
  String conLOGURL;// = paramReader.VratiSadrzajTaga("url");
  String conLOGUSER;// = paramReader.VratiSadrzajTaga("user");
  String conLOGPASS;// = paramReader.VratiSadrzajTaga("pass");
  String conLOGTIP;// = paramReader.VratiSadrzajTaga("tip");
//
  com.borland.dx.sql.dataset.QueryResolver qresolver = new com.borland.dx.sql.dataset.QueryResolver();
  hr.restart.util.IntParam paramReader = new hr.restart.util.IntParam();
  Database database1 = new Database();
  private static boolean refreshBegin = false;
  private static boolean safeMode = false;
  private static List tabledef;
  private static Map tabledefStart;
  
  static IDataSetSynchronizer sync = new DBDataSetSynchronizer();
  
  static IDataSetSynchronizer nosync = new IDataSetSynchronizer() {
  
    public void synchronize(DataSet ds) {
      // TODO Auto-generated method stub
  
    }
  
    public void propagateChanges(DataSet ds) {
      // TODO Auto-generated method stub
  
    }
  
    public void markAsFresh(DataSet ds) {
      // TODO Auto-generated method stub
  
    }
  
    public void markAsDirty(String table) {
      // TODO Auto-generated method stub
  
    }
  
    public int getSerialNumber(String table) {
      // TODO Auto-generated method stub
      return 0;
    }
  
  };
  
  static boolean isSync = true;

  public static Connection getDatabaseConnection() {
    return getDataModule().getDatabase1().getJdbcConnection();
  }
  
  public static Connection getTempConnection() {
  	return getDataModule().getNewConnection();
  }
  
  public static void setSynchronized(boolean synced) {
    isSync = synced;
  }
  
  public static IDataSetSynchronizer getSynchronizer() {
    return isSync ? sync : nosync;
  }
  
  public static boolean isMinimal() {
    return safeMode;
  }

  public static void setMinimalMode() {
    safeMode = true;
  }

  public static dM getDataModule() {
    if (myDM == null) {
//      new Throwable().printStackTrace();
      myDM = new dM();
      if (!safeMode) raVart.init();
//      myDM.initCart();
    } else if (!refreshBegin && !safeMode) {
      refreshBegin = true;
      int timeout;
      try {
        timeout = Integer.parseInt(hr.restart.util.IntParam.getTag("timeout"));
      }
      catch (Exception ex) {
        timeout = 120000;
        hr.restart.util.IntParam.setTag("timeout","120000");
      }
      try {
        if (timeout > 0)
          Refresher.getDataModule().begin(timeout);
      } catch (Exception e) {
        System.out.println("Refresher nije instaliran");
      }
      myDM.installCRMConnection();
      
    } else if (!safeMode) myDM.installCRMConnection();
    return myDM;
  }

  private boolean crmInstalled = false;
  private void installCRMConnection() {
    if (crmInstalled) return;
    crmInstalled = true;
    
    String crmDriver = frmParam.getParam("sisfun", "crmDriver", "", "Driver za CRM");
    crmURL = frmParam.getParam("sisfun", "crmURL", "", "Url za CRM bazu");
    crmUser = frmParam.getParam("sisfun", "crmUser", "sa", "User za CRM bazu");
    crmPass = frmParam.getParam("sisfun", "crmPass", "", "Password za CRM bazu");
    
    if (crmURL == null || crmURL.trim().length() == 0) {
    	crmURL = null;
    	return; 
    }

    try {
      System.out.println("CRM driver="+crmDriver);
      Class.forName(crmDriver);
      System.out.println(crmURL);
      System.out.println(crmUser);
      System.out.println(crmPass);
      Connection c = DriverManager.getConnection(crmURL, crmUser, crmPass);
      c.close();
    } catch (Exception e) {
      e.printStackTrace();
      crmURL = null;
    }
  }
    
  private boolean podInstalled = false;
  public void installPodConnection() {
    if (podInstalled) return;
    podInstalled = true;
    
    String podDriver = frmParam.getParam("sisfun", "podDriver", "", "Driver za Salepod");
    podURL = frmParam.getParam("sisfun", "podURL", "", "Url za Salepod bazu");
    podUser = frmParam.getParam("sisfun", "podUser", "root", "User za Salepod bazu");
    podPass = frmParam.getParam("sisfun", "podPass", "masterkey", "Password za Salepod bazu");
    
    if (podURL == null || podURL.trim().length() == 0) {
      podURL = null;
        return; 
    }

    try {
      System.out.println("Pod driver="+podDriver);
      Class.forName(podDriver);
      System.out.println(podURL);
      System.out.println(podUser);
      System.out.println(podPass);
      Connection c = DriverManager.getConnection(podURL, podUser, podPass);
      c.close();
    } catch (Exception e) {
      e.printStackTrace();
      podURL = null;
    }
    
    dodURL = frmParam.getParam("sisfun", "dodURL", "", "Url za dodatnu bazu Salepod bazu");
    
    if (dodURL == null || dodURL.trim().length() == 0) {
      dodURL = null;
        return; 
    }

    try {
      Connection c = DriverManager.getConnection(dodURL, conUSER, conPASS);
      c.close();
    } catch (Exception e) {
      e.printStackTrace();
      dodURL = null;
    }
  }

  public dM() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void readParams() {
    conURL = paramReader.VratiSadrzajTaga("url");
    conUSER = paramReader.VratiSadrzajTaga("user");
    conPASS = paramReader.VratiSadrzajTaga("pass");
    conTIP = paramReader.VratiSadrzajTaga("tip");
    
//    conLOGURL = paramReader.VratiSadrzajTaga("logurl");
//    conLOGUSER = paramReader.VratiSadrzajTaga("loguser");
//    conLOGPASS = paramReader.VratiSadrzajTaga("logpass");
//    conLOGTIP = paramReader.VratiSadrzajTaga("logtip");
    
//    String replpropsFileName;
//    try {
//      replpropsFileName = ftpVersionWorker.getVersionProperties().getProperty("locallib")+File.separator+"replication.properties";
//    } catch (Exception e) {
//      System.out.println(e.getMessage());
//      replpropsFileName = "lib"+File.separator+"replication.properties";
//    }
//    Properties replprops = FileHandler.getProperties(replpropsFileName);
    
    //should we do the replication or not
    //String tmpReplication = paramReader.VratiSadrzajTaga("replication").toLowerCase();
    
    String tmpReplication = paramReader.VratiSadrzajTaga("replication");
//    String loinst = "INSERT INTO ra_log (LOG_ID,LOG_TS,LOG_SQL) VALUES (gen_id(log_id, 1), CURRENT_TIMESTAMP, ?)";
    if(tmpReplication != null && tmpReplication.equals("true")){     
         // do the replication
         isReplicating = true;
         //insertStatement = paramReader.VratiSadrzajTaga("logInsertStatement");
         //insertStatement = replprops.getProperty("logInsertStatement", loinst);
         
         System.out.println("The replication is activated");
     }else{
         
         System.out.println("The replication is not used");
     }
    
//    replprops.setProperty("replication", tmpReplication);
//    replprops.setProperty("logInsertStatement", loinst);
//    FileHandler.storeProperties(replpropsFileName, replprops);
    
            
    
    if (conURL.equals("") && !safeMode) {
      hr.restart.util.Parametri_app parFrame = new hr.restart.util.Parametri_app(null);
      parFrame.pack();
      parFrame.setVisible(true);
      readParams();
    }
  }
  public void showParams() {
    hr.restart.util.Parametri_app parFrame = new hr.restart.util.Parametri_app(null);
    parFrame.pack();
    parFrame.setVisible(true);
  }

  private com.borland.dx.sql.dataset.ConnectionUpdateAdapter cua;

  public void reconnect() {
    database1.removeConnectionUpdateListener(cua);
    database1.closeConnection();
    setConnection();
    System.out.println("reconnected!");
    KreirDrop.refreshModules();
  }

  public boolean reconnectIfNeeded() {
    String newconURL = paramReader.VratiSadrzajTaga("url");
    String newconUSER = paramReader.VratiSadrzajTaga("user");
    String newconTIP = paramReader.VratiSadrzajTaga("tip");
    if (!newconURL.equals(conURL) || !newconUSER.equals(conUSER) || !newconTIP.equals(conTIP)) {
      reconnect();
      return true;
    } else return false;
  }

  String crmURL = null;
  String crmUser = null;
  String crmPass = null;
  
  public boolean isCRM() {
    return crmURL != null;
  }
  
  public Connection getCRMConnection() {
    try {
      return crmURL == null ? null : DriverManager.getConnection(crmURL, crmUser, crmPass);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  String podURL = null;
  String podUser = null;
  String podPass = null;
  
  String dodURL = null;
  
  public boolean isPod() {
    return podURL != null;
  }
  
  public Connection getPodConnection() {
    try {
      return podURL == null ? null : DriverManager.getConnection(podURL, podUser, podPass);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public Connection getDodConnection() {
    try {
      return dodURL == null ? null : DriverManager.getConnection(dodURL, conUSER, conPASS);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public Connection getNewConnection() {
  	try {
			Connection con = DriverManager.getConnection(conURL, conUSER, conPASS);
			con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error getting connection");
		}
  }
  
  void setConnection() {
    readParams();
    
    String driverUsed = null;
    
 	// TODO: ADD FOR TEST
	try {
      if(isReplicating){		        
        Class cdriverProxy = Class.forName("hr.restart.db.replication.logging.DriverProxy");
        cdriverProxy.getMethod("setJdbcDriverClass",new Class[] {String.class})
            .invoke(null, new Object[] {conTIP});
        //DriverProxy.setJdbcDriverClass(conTIP);
        final BeanFactory beanFactory = new ClassPathXmlApplicationContext("hr/restart/db/replication/logging/replication.xml");
            
        Driver proxy = (Driver)beanFactory.getBean("DriverProxy");
  		
  		//driverUsed = DriverProxy.class.getName();
        driverUsed = "hr.restart.db.replication.logging.DriverProxy";
  		
  		System.out.println("OK");
  	}
      else{
          driverUsed = conTIP;
  	}
			    					
	} catch (ClassNotFoundException e) {
      driverUsed = conTIP;
      e.printStackTrace();
	} catch (SecurityException e) {
      driverUsed = conTIP;
	   e.printStackTrace();
    } catch (NoSuchMethodException e) {
      driverUsed = conTIP;
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      driverUsed = conTIP;
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      driverUsed = conTIP;
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      driverUsed = conTIP;
      e.printStackTrace();
    } 
		
	//EOF
    setDatabaseDialect();
    
    try {
			Class.forName(driverUsed);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    database1.setConnection(new ConnectionDescriptor(conURL,conUSER,conPASS,false,
        driverUsed, Dialect.getConnectionProperties()));
    
   database1.setTransactionIsolation(java.sql.Connection.TRANSACTION_REPEATABLE_READ);
   System.out.println("trans "+database1.getTransactionIsolation());
   System.out.println("timeout "+qresolver.getResolverQueryTimeout());
   
   
   
//   java.sql.Connection c;
   database1.addConnectionUpdateListener(cua = new com.borland.dx.sql.dataset.ConnectionUpdateAdapter() {
     public void connectionClosed(com.borland.dx.sql.dataset.ConnectionUpdateEvent e) {
       System.out.println("Connection is CLOSED!!");
     }
   });
//   Refresher.getDataModule().begin(10000);
/*   try {
     java.sql.ResultSet rs = database1.getMetaData().getTables(null,
         null, null, new String[] {"TABLE"});
     while (rs.next())
       System.out.println(rs.getString("TABLE_NAME"));

   } catch (Exception e) {
     e.printStackTrace();
   } */
//  System.out.println("Connected!");
  }
//  int cartSize;
//  private void initCart() {
//    try {
//      cartSize = Integer.parseInt(hr.restart.sisfun.frmParam.getParam("robno", "cartSize", "20"));
//    } catch (Exception e) {}
//  }

  private void setDatabaseDialect() {
    Dialect definedDialect = null;
    try {
      String dcn = IntParam.getTag("dbdialect");
      if (!dcn.equals("")) {
        Class dc = Class.forName(dcn);
        Method im = dc.getMethod("getInstance",null);
        definedDialect = (Dialect)im.invoke(null,null);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (definedDialect == null) {
      //Dialect is not defined
      String driver = conTIP;   
      if (driver.equals("interbase.interclient.Driver"))
        Dialect.setCurrentDialect(InterbaseDialect.getInstance());
      else if (driver.equals("org.firebirdsql.jdbc.FBDriver"))
        Dialect.setCurrentDialect(FirebirdsqlDialect.getInstance());
      else if (driver.equals("com.mysql.jdbc.Driver"))
        Dialect.setCurrentDialect(MySQLDialect.getInstance());
      else if (driver.equals("org.hsqldb.jdbcDriver"))
        Dialect.setCurrentDialect(HsqlDialect.getInstance());
      else if (driver.equals("com.ibm.db2.jcc.DB2Driver")
          || driver.equals("org.apache.derby.jdbc.EmbeddedDriver") 
          || driver.equals("org.apache.derby.jdbc.ClientDriver"))
        Dialect.setCurrentDialect(Db2Dialect.getInstance());
      else if (driver.equals("org.h2.Driver")) 
          Dialect.setCurrentDialect(H2Dialect.getInstance());
      else if (driver.equals("org.postgresql.Driver")) 
        Dialect.setCurrentDialect(PostgresDialect.getInstance());
      else Dialect.setCurrentDialect(DefaultDialect.getInstance());
      
    } else {
      Dialect.setCurrentDialect(definedDialect);
    }
    System.out.println("Using dialect "+Dialect.getCurrentDialectClassName());
  }

  private void jbInit() throws Exception {
    com.borland.dx.dataset.Variant vrijeme = new com.borland.dx.dataset.Variant(com.borland.dx.dataset.Variant.TIMESTAMP);
    qresolver.setDatabase(database1);
    qresolver.setResolverQueryTimeout(0);
    qresolver.setUpdateMode(com.borland.dx.dataset.UpdateMode.KEY_COLUMNS);
    setConnection();
    String enc = database1.getConnection().getProperties().getProperty("charSet");
    if (enc != null)  hr.restart.sisfun.TextFile.setEncoding(enc);
    loadTableDefinitions();
  }
  
  /**
   * Mogucnost dodavanja tablica u letu 
   * @param resource
   */
  public static void addTableDefinition(InputStream resource) {
    try {
      loadTableDefinitions();
      TextFile tf = TextFile.read(resource);
      String line;
      while ((line = tf.in()) != null) {
        line = line.trim();
        tabledef.add(line);
        if (line.startsWith("$")) 
          tabledefStart.put(line.substring(1).toUpperCase(), new Integer(tabledef.size()));
      }
      tf.close();
      resource.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public String getDefLine(int i) {
    if (i >= tabledef.size()) return null;
    String line = (String) tabledef.get(i);
    return line.startsWith("$") ? null : line;
  }
  
  public int getTableOffset(String table) {
    table = table.toUpperCase();
    if (!tabledefStart.containsKey(table)) return -1;
    return ((Integer) tabledefStart.get(table)).intValue();
  }

  private static void loadTableDefinitions() {
    if (tabledef != null && tabledefStart != null) return; 
    try {
      InputStream is = null;
      try {
        is = ClassLoader.getSystemResourceAsStream("tabledef.txt");
      } catch (Exception e) {}
      if (is == null) is = ClassLoader.getSystemResourceAsStream("hr/restart/baza/tabledef.txt");

      TextFile tf = TextFile.read(is);
      tabledef = new ArrayList();
      tabledefStart = new HashMap();
      
      String line;
      while ((line = tf.in()) != null) {
        line = line.trim();
        tabledef.add(line);
        if (line.startsWith("$")) {
          Object old = tabledefStart.put(line.substring(1).toUpperCase(), new Integer(tabledef.size()));
          if (old != null) System.out.println("Duplicate "+line.substring(1).toUpperCase());
        }
      }
      tf.close();
      is.close();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(-1);
    }
  }

  public static Column createColumn(String name, String caption, String def, int type, int sqltype, int width, int scale) {
    Column c = new Column();
    c.setCaption(caption);
    c.setColumnName(name);
    c.setServerColumnName(name);
    c.setDataType(type);
    c.setSqlType(sqltype);
    c.setPrecision(width);
    if (width > 0) c.setWidth(width);
    if (type == Variant.STRING && width > 30)
      c.setWidth(30);
    if (def != null && def.length() > 0)
      c.setDefault(def);
    if (scale != -1) {
      c.setScale(scale);
      if (scale > 0 && scale < 8) {
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
      }
    }
    if (type == Variant.TIMESTAMP) {
      c.setDisplayMask("dd-MM-yyyy");
    }
    return c;
  }

//  public static Column[] getColumns(DataSet source, String[] colnames) {
//    Column[] result = new Column[colnames.length];
//    for (int i = 0; i < colnames.length; i++)
//      result[i] = source.getColumn(colnames[i]);
//    return result;
//  }

  public static String[] getDataValues(ReadRow data) {
    return getDataValues(data, data.getColumnNames(data.getColumnCount()));
  }

  public static String[] getDataValues(ReadRow data, String[] cols) {
    String[] result = new String[cols.length];
    Variant v = new Variant();
    for (int i = 0; i < cols.length; i++) {
      data.getVariant(cols[i], v);
      result[i] = v.toString();
    }
    return result;
  }

  private static BigDecimal scaled(BigDecimal orig, int sc) {
    return sc < 0 ? orig : orig.setScale(sc, BigDecimal.ROUND_HALF_UP);
  }
  
  public static boolean rowsEqual(ReadRow source, ReadRow dest) {
    return rowsEqual(source, dest, source.getColumnNames(source.getColumnCount()));
  }
  
  public static boolean rowsEqual(ReadRow source, ReadRow dest, String[] cols) {
    return compareColumns(source, dest, cols) == null;
  }
  
  public static String compareColumns(ReadRow source, ReadRow dest) {
    return compareColumns(source, dest, source.getColumnNames(source.getColumnCount()));
  }
  
  public static String compareColumns(ReadRow source, ReadRow dest, String[] cols) {
    for (int i = 0; i < cols.length; i++) {
      Column sc = source.hasColumn(cols[i]);
      Column dc = dest.hasColumn(cols[i]);
      if (sc == null || dc == null)
        throw new NullPointerException("Missing column: "+cols[i]);
      if (sc.getDataType() != dc.getDataType())
        throw new IllegalArgumentException("Invalid column type: "+cols[i]);
      switch (sc.getDataType()) {
        case Variant.STRING:
          if (!source.getString(cols[i]).equals(dest.getString(cols[i]))) 
            return cols[i];
          break;
        case Variant.INT:
          if (source.getInt(cols[i]) != dest.getInt(cols[i])) 
            return cols[i];
          break;
        case Variant.BIGDECIMAL:
          if (source.getBigDecimal(cols[i]).compareTo(dest.getBigDecimal(cols[i])) != 0) 
            return cols[i];
          break;
        case Variant.TIMESTAMP:
          if (source.getTimestamp(cols[i]).compareTo(dest.getTimestamp(cols[i])) != 0)
            return cols[i];
          break;
        case Variant.SHORT:
          if (source.getShort(cols[i]) != dest.getShort(cols[i])) 
            return cols[i];
          break;
        case Variant.DOUBLE:
          if (source.getDouble(cols[i]) != dest.getDouble(cols[i])) 
            return cols[i];
          break;
        case Variant.FLOAT:
          if (source.getFloat(cols[i]) != dest.getFloat(cols[i])) 
            return cols[i];
          break;
        case Variant.LONG:
          if (source.getLong(cols[i]) != dest.getLong(cols[i])) 
            return cols[i];
          break;
        default:
          throw new IllegalArgumentException("Invalid column type: "+cols[i]);
      }
    }
    return null;
  }

  public static void copyColumns(ReadRow source, ReadWriteRow dest, String[] cols) {
    for (int i = 0; i < cols.length; i++) {
      if (source.hasColumn(cols[i]) == null || dest.hasColumn(cols[i]) == null)
        throw new NullPointerException("Missing column: "+cols[i]);
      if (source.isNull(cols[i]) && dest.isNull(cols[i])) continue;
      Column dc = dest.getColumn(cols[i]);
      int dt = dc.getDataType();
      int st = source.getColumn(cols[i]).getDataType();
      if (source.isAssignedNull(cols[i]))
        dest.setAssignedNull(cols[i]);
      else if (source.isUnassignedNull(cols[i]))
        dest.setUnassignedNull(cols[i]);
      else switch (st) {
        case Variant.BIGDECIMAL:
          if (dt == Variant.BIGDECIMAL)
            dest.setBigDecimal(cols[i], scaled(source.getBigDecimal(cols[i]), dc.getScale()));
          else if (dt == Variant.FLOAT)
            dest.setFloat(cols[i], source.getBigDecimal(cols[i]).floatValue());
          else if (dt == Variant.DOUBLE)
            dest.setDouble(cols[i], source.getBigDecimal(cols[i]).doubleValue());
          else throw new UnsupportedOperationException("Incompatible column: "+cols[i]+
              " (source: "+Variant.typeName(st)+", dest: "+Variant.typeName(dt)+")");
          break;
        case Variant.FLOAT:
          if (dt == Variant.BIGDECIMAL)
            dest.setBigDecimal(cols[i], scaled(new BigDecimal((double) source.getFloat(cols[i])), dc.getScale()));
          else if (dt == Variant.FLOAT)
            dest.setFloat(cols[i], source.getFloat(cols[i]));
          else if (dt == Variant.DOUBLE)
            dest.setDouble(cols[i], (double) source.getFloat(cols[i]));
          else throw new UnsupportedOperationException("Incompatible column: "+cols[i]+
              " (source: "+Variant.typeName(st)+", dest: "+Variant.typeName(dt)+")");
          break;
        case Variant.DOUBLE:
          if (dt == Variant.BIGDECIMAL)
            dest.setBigDecimal(cols[i], scaled(new BigDecimal(source.getDouble(cols[i])), dc.getScale()));
          else if (dt == Variant.FLOAT)
            dest.setFloat(cols[i], (float) source.getDouble(cols[i]));
          else if (dt == Variant.DOUBLE)
            dest.setDouble(cols[i], source.getDouble(cols[i]));
          else throw new UnsupportedOperationException("Incompatible column: "+cols[i]+
              " (source: "+Variant.typeName(st)+", dest: "+Variant.typeName(dt)+")");
          break;
        case Variant.STRING:
          if (dt == Variant.STRING) {
            String src = source.getString(cols[i]);
            if (src.length() > dc.getPrecision() && dc.getPrecision() > 0)
              dest.setString(cols[i], src.substring(0, dc.getPrecision()));
            else dest.setString(cols[i], src);
          } else throw new UnsupportedOperationException("Incompatible column: "+cols[i]+
              " (source: "+Variant.typeName(st)+", dest: "+Variant.typeName(dt)+")");
          break;
        case Variant.TIMESTAMP:
          if (dt == Variant.TIMESTAMP)
            dest.setTimestamp(cols[i], source.getTimestamp(cols[i]));
          else throw new UnsupportedOperationException("Incompatible column: "+cols[i]+
              " (source: "+Variant.typeName(st)+", dest: "+Variant.typeName(dt)+")");
          break;
        case Variant.INT:
          if (dt == Variant.INT)
            dest.setInt(cols[i], source.getInt(cols[i]));
          else if (dt == Variant.LONG)
            dest.setLong(cols[i], (long) source.getInt(cols[i]));
          else throw new UnsupportedOperationException("Incompatible column: "+cols[i]+
              " (source: "+Variant.typeName(st)+", dest: "+Variant.typeName(dt)+")");
          break;
        case Variant.SHORT:
          if (dt == Variant.SHORT)
            dest.setShort(cols[i], source.getShort(cols[i]));
          else if (dt == Variant.INT)
            dest.setInt(cols[i], (int) source.getShort(cols[i]));
          else if (dt == Variant.LONG)
            dest.setLong(cols[i], (long) source.getShort(cols[i]));
          else throw new UnsupportedOperationException("Incompatible column: "+cols[i]+
              " (source: "+Variant.typeName(st)+", dest: "+Variant.typeName(dt)+")");
          break;
        case Variant.LONG:
          if (dt == Variant.LONG)
            dest.setLong(cols[i], source.getLong(cols[i]));
          else throw new UnsupportedOperationException("Incompatible column: "+cols[i]+
              " (source: "+Variant.typeName(st)+", dest: "+Variant.typeName(dt)+")");
          break;
      }
    }
  }
  
  public static StorageDataSet createScopedSet(ReadRow src, String[] columns) {
  	StorageDataSet ret = new StorageDataSet();
  	ret.setLocale(Aus.hr);
  	Column[] cols = new Column[columns.length];
  	for (int i = 0; i < cols.length; i++)
  		cols[i] = src.getColumn(columns[i]).cloneColumn();
  	ret.setColumns(cols);
  	return ret;
  }

  public static void copyColumns(ReadRow source, ReadWriteRow dest) {
    copyColumns(source, dest, source.getColumnNames(source.getColumnCount()));
  }

  public static void copyDestColumns(ReadRow source, ReadWriteRow dest) {
    copyColumns(source, dest, dest.getColumnNames(dest.getColumnCount()));
  }

  public static Column createStringColumn(String name, int precision) {
    return createColumn(name, name, "", Variant.STRING, 1, precision, -1);
  }

  public static Column createStringColumn(String name, String caption, int precision) {
    return createColumn(name, caption, "", Variant.STRING, 1, precision, -1);
  }

  public static Column createStringColumn(String name, String caption, String def, int precision) {
    return createColumn(name, caption, def, Variant.STRING, 1, precision, -1);
  }

  public static Column createIntColumn(String name) {
    return createColumn(name, name, "", Variant.INT, 4, 6, -1);
  }

  public static Column createIntColumn(String name, String caption) {
    return createColumn(name, caption, "", Variant.INT, 4, 6, -1);
  }

  public static Column createShortColumn(String name) {
    return createColumn(name, name, "", Variant.SHORT, 5, 4, -1);
  }

  public static Column createShortColumn(String name, String caption) {
    return createColumn(name, caption, "", Variant.SHORT, 5, 4, -1);
  }

  public static Column createTimestampColumn(String name) {
    return createColumn(name, name, "", Variant.TIMESTAMP, 93, 9, -1);
  }

  public static Column createTimestampColumn(String name, String caption) {
    return createColumn(name, caption, "", Variant.TIMESTAMP, 93, 9, -1);
  }

  public static Column createBigDecimalColumn(String name) {
    return createColumn(name, name, "0", Variant.BIGDECIMAL, 2, 10, 2);
  }

  public static Column createBigDecimalColumn(String name, int scale) {
    return createColumn(name, name, "0", Variant.BIGDECIMAL, 2, 10, scale);
  }


  public static Column createBigDecimalColumn(String name, String caption) {
    return createColumn(name, caption, "0", Variant.BIGDECIMAL, 2, 10, 2);
  }

  public static Column createBigDecimalColumn(String name, String caption, int scale) {
    return createColumn(name, caption, "0", Variant.BIGDECIMAL, 2, 10, scale);
  }

  public Database getDatabase1() {
    return database1;
  }

  public QueryDataSet getArtikli() {
    return Artikli.getDataModule().getAktiv();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getVTCartPart() {
    return VTCartPart.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getAktivVTCartPart() {
    return VTCartPart.getDataModule().getAktiv();
  }

  public QueryDataSet getArtikliRoba() {
    return Artikli.getDataModule().getArtikliRoba();
  }

  public QueryDataSet getAllArtikli() {
    return Artikli.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getDoku() {
    return Doku.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getGrupart() {
    return Grupart.getDataModule().getAktiv();
  }

  public QueryDataSet getAllGrupart() {
    return Grupart.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getPartneri() {
    return Partneri.getDataModule().getAktiv();
  }

  public QueryDataSet getPartneriKup() {
    return Partneri.getDataModule().getKup();
  }

  public QueryDataSet getPartneriDob() {
    return Partneri.getDataModule().getDob();
  }

  public QueryDataSet getPartneriOboje() {
    return Partneri.getDataModule().getOboje();
  }

  public QueryDataSet getAllPartneri() {
    return Partneri.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getPjpar() {
    return Pjpar.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getPorezi() {
    return Porezi.getDataModule().getAktiv();
  }

  public QueryDataSet getAllPorezi() {
    return Porezi.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getSklad() {
    return Sklad.getDataModule().getAktiv();
  }

  public QueryDataSet getAllSklad() {
    return Sklad.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getStanje() {
    return Stanje.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getStdoku() {
    return Stdoku.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getZavtr() {
    return Zavtr.getDataModule().getAktiv();
  }

  public QueryDataSet getAllZavtr() {
    return Zavtr.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAgenti() {
    return Agenti.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllAgenti() {
    return Agenti.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getSeq() {
    return SEQ.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOrgstruktura() {
    return Orgstruktura.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllOrgstruktura() {
    return Orgstruktura.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKnjig() {
    return Orgstruktura.getDataModule().getKnjig();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getTecajevi() {
    return Tecajevi.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllTecajevi() {
    return Tecajevi.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getValute() {
    return Valute.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllValute() {
    return Valute.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZirorn() {
    return zirorn.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllZirorn() {
    return zirorn.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDob_art() {
    return dob_art.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKup_art() {
    return kup_art.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getShrab() {
    return shrab.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRabati() {
    return rabati.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllRabati() {
    return rabati.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDoki() {
    return doki.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdoki() {
    return stdoki.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllKonta() {
    return Konta.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKonta() {
    return Konta.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaSintetic() {
    return Konta.getDataModule().getKontaSin();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaAnalitic() {
    return Konta.getDataModule().getKontaAna();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaDug() {
    return Konta.getDataModule().getKontaAnaD();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKontaPot() {
    return Konta.getDataModule().getKontaAnaP();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getSerbr() {
    return Serbr.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getJedmj() {
    return jedmj.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllJedmj() {
    return jedmj.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getNorme() {
    return norme.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getSortedNorme() {
    return norme.getDataModule().getSorted();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getShzavtr() {
    return Shzavtr.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getFranka() {
    return franka.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllFranka() {
    return franka.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getNamjena() {
    return namjena.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllNamjena() {
    return namjena.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getNacotp() {
    return nacotp.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllNacotp() {
    return nacotp.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getNacpl() {
    return nacpl.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllNacpl() {
    return nacpl.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getNacplG() {
    return nacpl.getDataModule().getNacplG();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getNacplB() {
    return nacpl.getDataModule().getNacplB();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getGlobParametri() {
    return Parametri.getDataModule().getGlobParametri();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getLocParametri() {
    return Parametri.getDataModule().getLocParametri();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getParametri() {
    return Parametri.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVtzavtr() {
    return vtzavtr.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVtrabat() {
    return vtrabat.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getNapomene() {
    return napomene.getDataModule().getAktiv();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getAllNapomene() {
    return napomene.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getAplikacija() {
    return Aplikacija.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getVrtros() {
    return Vrtros.getDataModule().getAktiv();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getAllVrtros() {
    return Vrtros.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryResolver getQresolver() {
    QueryResolver ret = new QueryResolver();
    ret.setDatabase(database1);
    ret.setResolverQueryTimeout(30);
    ret.setUpdateMode(com.borland.dx.dataset.UpdateMode.KEY_COLUMNS);
    return ret;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getLogotipovi() {
    return Logotipovi.getDataModule().getAktiv();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getAllLogotipovi() {
    return Logotipovi.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPos() {
      return Pos.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStpos() {
      return Stpos.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getShkonta() {
      return Shkonta.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getShkontaUnos() {
      return Shkonta.getDataModule().getAll();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getMeskla() {
      return Meskla.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMesklaMES() {
      return Meskla.getDataModule().getMES();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMesklaMEU() {
      return Meskla.getDataModule().getMEU();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMesklaMEI() {
      return Meskla.getDataModule().getMEI();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStmeskla() {
      return Stmeskla.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStmesklaMES() {
      return Stmeskla.getDataModule().getMES();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStmesklaMEU() {
      return Stmeskla.getDataModule().getMEU();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStmesklaMEI() {
      return Stmeskla.getDataModule().getMEI();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getBanke() {
      return Banke.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllBanke() {
      return Banke.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRate() {
      return Rate.getDataModule().getQueryDataSet();
  }

 /* public com.borland.dx.sql.dataset.QueryDataSet getPlacrata() {
      return Placrata.getDataModule().getQueryDataSet();
  } */

  public com.borland.dx.sql.dataset.QueryDataSet getProdmj() {
      return Prodmj.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getUseri() {
      return Useri.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getUgovori() {
      return Ugovori.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllUgovori() {
      return Ugovori.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getGrupeusera() {
      return Grupeusera.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getPravagrus() {
      return Pravagrus.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getPravauser() {
      return Pravauser.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getKartice() {
      return Kartice.getDataModule().getAktiv();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getAllKartice() {
      return Kartice.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getvshrab_rab() {
      return vshrab_rab.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getvshztr_ztr() {
      return vshztr_ztr.getDataModule().getQueryDataSet();
  }
 ///// za ispise
/*  public com.borland.dx.sql.dataset.QueryDataSet getQRacuni() {
      return reportsQuerysCollector.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getQMeskla() {
      return reportsQuerysCollector.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getQPrikalk() {
      return reportsQuerysCollector.getDataModule().getQueryDataSet();
  }
   public com.borland.dx.sql.dataset.QueryDataSet getQDnevnik() {
      return reportsQuerysCollector.getDataModule().getQueryDataSet();
  } */

  public com.borland.dx.sql.dataset.QueryDataSet getPrava() {
      return Prava.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCjenik() {
      return Cjenik.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getProd_mj() {
      return Prod_mj.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getSkupart() {
    return Skupart.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getInventura() {
    return Inventura.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getGruppart() {
    return Gruppart.getDataModule().getAktiv();
  }

  public QueryDataSet getAllGruppart() {
    return Gruppart.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getKupci() {
    return Kupci.getDataModule().getAktiv();
  }

  public QueryDataSet getAllKupci() {
    return Kupci.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getDefshkonta() {
    return Defshkonta.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getVrdokumRadni() {
    return Vrdokum.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getVrdokum() {
    return Vrdokum.getDataModule().getAll();
  }

  public QueryDataSet getVrdokumUlazni() {
    return Vrdokum.getDataModule().getUlazni();
  }

  public QueryDataSet getVrdokumIzlazni() {
    return Vrdokum.getDataModule().getIzlazni();
  }


  public QueryDataSet getKonta_par() {
    return Konta_par.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getTemelj() {
    return Temelj.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getRN() {
    return RN.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getRNser() {
    return RN.getDataModule().getRnser();
  }

  public QueryDataSet getRNpro() {
    return RN.getDataModule().getRnpro();
  }

  public QueryDataSet getRNp() {
    return RN.getDataModule().getRnp();
  }

  public QueryDataSet getRNo() {
    return RN.getDataModule().getRno();
  }

  public QueryDataSet getRNz() {
    return RN.getDataModule().getRnz();
  }

  public QueryDataSet getRN_subjekt() {
    return RN_subjekt.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getRN_znacsub() {
    return RN_znacsub.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getRN_sifznac() {
    return RN_sifznac.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getRN_znacajke() {
    return RN_znacajke.getDataModule().getQueryDataSet();
  }

  public QueryDataSet getRN_tekstovi() {
    return RN_tekstovi.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPos() {
    return doki.getDataModule().getZagPos();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagInm() {
    return doki.getDataModule().getZagInm();
  }


  public com.borland.dx.sql.dataset.QueryDataSet getZagGot() {
    return doki.getDataModule().getZagGot();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPon() {
    return doki.getDataModule().getZagPon();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPonKup() {
    return doki.getDataModule().getZagPonKup();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagPonOJ() {
    return doki.getDataModule().getZagPonOJ();
  }


  public com.borland.dx.sql.dataset.QueryDataSet getZagPonPar() {
    return doki.getDataModule().getZagPonPar();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagNar() {
    return doki.getDataModule().getZagNar();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPov() {
    return doki.getDataModule().getZagPov();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPrd() {
    return doki.getDataModule().getZagPrd();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getZagPrdKup() {
    return doki.getDataModule().getZagPrdKup();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagRac() {
    return doki.getDataModule().getZagRac();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagRot() {
    return doki.getDataModule().getZagRot();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagOtp() {
    return doki.getDataModule().getZagOtp();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getZagDos() {
    return doki.getDataModule().getZagDos();
  }


  public com.borland.dx.sql.dataset.QueryDataSet getZagIzd() {
    return doki.getDataModule().getZagIzd();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagTer() {
    return doki.getDataModule().getZagTer();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagOdb() {
    return doki.getDataModule().getZagOdb();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPod() {
    return doki.getDataModule().getZagPod();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagPodKup() {
    return doki.getDataModule().getZagPodKup();
  }


  public com.borland.dx.sql.dataset.QueryDataSet getZagGrn() {
    return doki.getDataModule().getZagGrn();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagRev() {
    return doki.getDataModule().getZagRev();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPre() {
    return doki.getDataModule().getZagPre();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagNdo() {
    return doki.getDataModule().getZagNdo();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagKon() { 
  	return doki.getDataModule().getZagKon();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getZagZah() { 
    return doki.getDataModule().getZagZah();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getStZah() { 
    return stdoki.getDataModule().getStZah();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getStDokiPos() {
    return stdoki.getDataModule().getStPos();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStGot() {
    return stdoki.getDataModule().getStGot();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPon() {
    return stdoki.getDataModule().getStPon();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStPonKup() {
    return stdoki.getDataModule().getStPonKup();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStPonOJ() {
    return stdoki.getDataModule().getStPonOJ();
  }


  public com.borland.dx.sql.dataset.QueryDataSet getStPov() {
    return stdoki.getDataModule().getStPov();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStNar() {
    return stdoki.getDataModule().getStNar();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPrd() {
    return stdoki.getDataModule().getStPrd();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getStPrdKup() {
    return stdoki.getDataModule().getStPrdKup();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRac() {
    return stdoki.getDataModule().getStRac();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRot() {
    return stdoki.getDataModule().getStRot();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStOtp() {
    return stdoki.getDataModule().getStOtp();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getStDos() {
    return stdoki.getDataModule().getStDos();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStIzd() {
    return stdoki.getDataModule().getStIzd();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPod() {
    return stdoki.getDataModule().getStPod();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStPodKup() {
    return stdoki.getDataModule().getStPodKup();
  }


  public com.borland.dx.sql.dataset.QueryDataSet getStTer() {
    return stdoki.getDataModule().getStTer();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStOdb() {
    return stdoki.getDataModule().getStOdb();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRnlPro() {
    return stdoki.getDataModule().getStRnlPro();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRnlSer() {
    return stdoki.getDataModule().getStRnlSer();
  }


  public com.borland.dx.sql.dataset.QueryDataSet getStGrn() {
    return stdoki.getDataModule().getStGrn();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRev() {
    return stdoki.getDataModule().getStRev();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPre() {
    return stdoki.getDataModule().getStPre();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStNdo() {
    return stdoki.getDataModule().getStNdo();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStInm() {
    return stdoki.getDataModule().getStInm();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStKon(){
	  return stdoki.getDataModule().getStKon();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPST() {
    return Doku.getDataModule().getDokuPST();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPRI() {
    return Doku.getDataModule().getDokuPRI();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPOR() {
    return Doku.getDataModule().getDokuPOR();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPTE() {
    return Doku.getDataModule().getDokuPTE();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPRK() {
    return Doku.getDataModule().getDokuPRK();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuKAL() {
    return Doku.getDataModule().getDokuKAL();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPRE() {
    return Doku.getDataModule().getDokuPRE();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuINV() {
    return Doku.getDataModule().getDokuINV();
  }


  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPST() {
    return Stdoku.getDataModule().getStdokuPST();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPRI() {
    return Stdoku.getDataModule().getStdokuPRI();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPOR() {
    return Stdoku.getDataModule().getStdokuPOR();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPTE() {
    return Stdoku.getDataModule().getStdokuPTE();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPRK() {
    return Stdoku.getDataModule().getStdokuPRK();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuKAL() {
    return Stdoku.getDataModule().getStdokuKAL();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPRE() {
    return Stdoku.getDataModule().getStdokuPRE();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuINV() {
    return Stdoku.getDataModule().getStdokuINV();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Artikli() {
    return OS_Artikli.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Amgrupe() {
    return OS_Amgrupe.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Kontrola() {
    return OS_Kontrola.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Lokacije() {
    return OS_Lokacije.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Obrada1() {
    return OS_Obrada1.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getOS_Obrada2() {
    return OS_Obrada2.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getOS_Obrada3() {
    return OS_Obrada3.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getOS_Obrada4() {
    return OS_Obrada4.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getOS_Obrada5() {
    return OS_Obrada5.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getOS_Promjene() {
    return OS_Promjene.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Revskupine() {
    return OS_Revskupine.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Sredstvo() {
    return OS_Sredstvo.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Vrpromjene() {
    return OS_Vrpromjene.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_SI() {
    return OS_SI.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_StSI() {
    return OS_StSI.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Porijeklo() {
    return OS_Porijeklo.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Metaobrada() {
    return OS_Metaobrada.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVrstenaloga() {
    return Vrstenaloga.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getNalozi() {
    return Nalozi.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getGkstavkerad() {
    return Gkstavkerad.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getGkstavke() {
    return Gkstavke.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getGkkumulativi() {
    return Gkkumulativi.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getSkstavkerad() {
    return Skstavkerad.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getMasterSkstavkerad() {
    return Skstavkerad.getDataModule().getFake();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getSkstavke() {
    return Skstavke.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getSkstavkeBase() {
    return Skstavke.getDataModule().getSkbase();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getSkstavkeCover() {
    return Skstavke.getDataModule().getSkcover();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getUIstavke() {
    return UIstavke.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPokriveni() {
    return Pokriveni.getDataModule().getQueryDataSet();
  }

/*  public com.borland.dx.sql.dataset.QueryDataSet getPokriveniRadni() {
    return Pokriveni.getDataModule().getRad();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPokriveniNew() {
    return Pokriveni.getDataModule().getNew();
  }*/

  public com.borland.dx.sql.dataset.QueryDataSet getSkkumulativi() {
    return Skkumulativi.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKnjigeUI() {
    return KnjigeUI.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKnjigeI() {
    return KnjigeUI.getDataModule().getKnjigeI();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKnjigeU() {
    return KnjigeUI.getDataModule().getKnjigeU();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVrshemek() {
    return Vrshemek.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getProgrami() {
    return Programi.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getFunkcije() {
    return Funkcije.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getTablice() {
    return Tablice.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getKljucevi() {
    return Kljucevi.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getUsersklad() {
    return Usersklad.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getKnjigod() {
    return Knjigod.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getKoloneknjUI() {
    return KoloneknjUI.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getUlazneKolone() {
    return KoloneknjUI.getDataModule().getUlazne();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getIzlazneKolone() {
    return KoloneknjUI.getDataModule().getIzlazne();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getShemevezeUI() {
    return ShemevezeUI.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZiropar() {
    return Ziropar.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getIzvodi() {
    return Izvodi.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getVTprijenos() {
    return VTprijenos.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllRadnici() {
    return Radnici.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRadnici() {
    return Radnici.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getSifrarnici() {
    return Sifrarnici.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVrstesif() {
    return Vrstesif.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRadMJ() {
    return RadMJ.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVrodn() {
    return Vrodn.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOpcine() {
    return Opcine.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZupanije() {
    return Zupanije.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPovjerioci() {
    return Povjerioci.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getBankepl() {
    return Bankepl.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getIsplMJ() {
    return IsplMJ.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVrsteodb() {
    return Vrsteodb.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOdbici() {
    return Odbici.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRadnicipl() {
    return Radnicipl.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllRadnicipl() {
    return Radnicipl.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOrgrad() {
    return Orgrad.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOrgpl() {
    return Orgpl.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getFondSati() {
    return FondSati.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVriboda() {
    return Vriboda.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getNacobr() {
    return Nacobr.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getSume() {
    return Sume.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getSumePrim() {
    return SumePrim.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPlosnovice() {
    return Plosnovice.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPlosnprim() {
    return Plosnprim.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPlizv() {
    return Plizv.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getGrupeizv() {
    return Grupeizv.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getGrizvprim() {
    return Grizvprim.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVrsteprim() {
    return Vrsteprim.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getParametripl() {
    return Parametripl.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllIniprim() {
    return Iniprim.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getIniprim() {
    return Iniprim.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPrimanjaobr() {
    return Primanjaobr.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOdbiciobr() {
    return Odbiciobr.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKumulrad() {
    return Kumulrad.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKumulorg() {
    return Kumulorg.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKumulorgarh() {
    return Kumulorgarh.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKumulradarh() {
    return Kumulradarh.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPrimanjaarh() {
    return Primanjaarh.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOdbiciarh() {
    return Odbiciarh.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getBlagajna() {
    return Blagajna.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getBlagizv() {
    return Blagizv.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStavblag() {
    return Stavblag.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStavkeblarh() {
    return Stavkeblarh.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPrisutobr() {
    return Prisutobr.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPrisutarh() {
    return Prisutarh.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVrart() {
    return Vrart.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZemlje() {
    return Zemlje.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPutniNalog() {
    return PutniNalog.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStavkepn() {
    return Stavkepn.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getParamblpn() {
    return Paramblpn.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPutnalarh() {
    return Putnalarh.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStavpnarh() {
    return Stavpnarh.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDefvaluedok() {
    return Defvaluedok.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZtr() {
    return Ztr.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVTZtr() {
    return VTZtr.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVTZtrt() {
    return VTZtrt.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRSPeriod() {
    return RSPeriod.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRSPeriodobr() {
    return RSPeriodobr.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRSPeriodarh() {
    return RSPeriodarh.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Objekt() {
    return OS_Objekt.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Log() {
    return OS_Log.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Arhiva() {
    return OS_Arhiva.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOS_Kontaisp() {
    return OS_Kontaisp.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVrsteugo() {
    return Vrsteugo.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVirmani() {
    return Virmani.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVTText() {
    return VTText.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getGrizvodb() {
    return Grizvodb.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getLogodat() {
    return Logodat.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getReportext() {
    return Reportext.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRepldef() {
    return Repldef.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getLogodatCustom() {
    return Logodat.getDataModule().getCustom();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getReplinfo() {
    return Replinfo.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getReplurl() {
    return Replurl.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDiorep() {
    return Diorep.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getMxPrinter() {
    return MxPrinter.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getMxPrinterRM() {
    return MxPrinterRM.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getMxDokument() {
    return MxDokument.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKamate() {
    return Kamate.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKamRac() {
    return KamRac.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKamUpl() {
    return KamUpl.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getIzvjPDV() {
    return IzvjPDV.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStIzvjPDV() {
    return StIzvjPDV.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKosobe() {
    return Kosobe.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPVROdbCorg() {
    return PVROdbCorg.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKPR() {
    return KPR.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPokriveniRadni() {
    return PokriveniRadni.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getReportdef() {
    return Reportdef.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRN_vrsub() {
    return RN_vrsub.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRN_vrsubUnos() {
    return RN_vrsub.getDataModule().getUnos();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getTransHistory() {
    return TransHistory.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZpZemlje() {
    return ZpZemlje.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAllZpZemlje() {
    return ZpZemlje.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getMjesta() {
    return Mjesta.getDataModule().getAktiv();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getNadzornaKnjiga() {
    return NadzornaKnjiga.getDataModule().getQueryDataSet();
  }


  public com.borland.dx.sql.dataset.QueryDataSet getAllMjesta() {
    return Mjesta.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPlZnacRad() {
    return PlZnacRad.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getPlZnacRadData() {
    return PlZnacRadData.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getGrIzvZnac() {
    return GrIzvZnac.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRN_znachint() {
    return RN_znachint.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getTransdata() {
    return Transdata.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVTrnl() {
    return VTRnl.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getBlagajnici() {
    return Blagajnici.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getMenus() {
    return Menus.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getTableSync() {
    return TableSync.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getTempCRM() {
    return TempCRM.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getVTPred() {
    return VTPred.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getReplication_TX() {
    return Replication_TX.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getstugovor() {
    return stugovor.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRepgk() {
    return Repgk.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDetrepgk() {
    return Detrepgk.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRepgkdata() {
    return Repgkdata.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDetrepgkdata() {
    return Detrepgkdata.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCarinarnica() {
    return Carinarnica.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCardrzava() {
    return Cardrzava.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getFizosobe() {
    return Fizosobe.getDataModule().getQueryDataSet();
  }
  

  public com.borland.dx.sql.dataset.QueryDataSet getParitet1PP() {
    return Paritet1PP.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getParitet3PP() {
    return Paritet3PP.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVrstaprometa() {
    return Vrstaprometa.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getJmcarina() {
    return Jmcarina.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getVrstaposla() {
    return Vrstaposla.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCarpotpor() {
    return Carpotpor.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCaroslobod() {
    return Caroslobod.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCarpostpp37() {
    return Carpostpp37.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCarpostpp372() {
    return Carpostpp372.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCartrosotp() {
    return Cartrosotp.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCarodob() {
    return Carodob.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCarvri() {
    return Carvri.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCardav() {
    return Cardav.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCarnacpl() {
    return Carnacpl.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCardozvole() {
    return Cardozvole.getDataModule().getQueryDataSet();
  }
    
  public com.borland.dx.sql.dataset.QueryDataSet getCarTG() {
    return CarTG.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getCarugovor() {
    return Carugovor.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCartarifa() {
    return Cartarifa.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCarulaz() {
    return Carulaz.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCarulazst() {
    return Carulazst.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCarizlaz() {
    return Carizlaz.getDataModule().getQueryDataSet();
  }

  public com.borland.dx.sql.dataset.QueryDataSet getCarizlazst() {
    return Carizlazst.getDataModule().getQueryDataSet();
  }
  
  
  public com.borland.dx.sql.dataset.QueryDataSet getTelemark() {
    return Telemark.getDataModule().getAktiv();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getAllTelemark() {
    return Telemark.getDataModule().getQueryDataSet();
  }
  public com.borland.dx.sql.dataset.QueryDataSet getTelehist() {
    return Telehist.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getVTCparHist() {
    return VTCparHist.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getTMjesta() {
    return Tmjesta.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getUrdok() {
    return Urdok.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getUrvrdok() {
    return Urvrdok.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getUrstat() {
    return Urstat.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getUrshist() {
    return Urshist.getDataModule().getQueryDataSet();
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getArtrans() {
    return Artrans.getDataModule().getQueryDataSet();
  }
  
  public QueryDataSet getKlijentStat() {
    return KlijentStat.getDataModule().getQueryDataSet();
  }
  
  public QueryDataSet getSegmentacija() {
    return Segmentacija.getDataModule().getQueryDataSet();
  }
  
  public QueryDataSet getKanali() {
    return Kanali.getDataModule().getQueryDataSet();
  }

  public void loadModules() {
    if (modulesLoaded) return;
    modulesLoaded = true;
    String[] allc = ConsoleCreator.getModuleClasses();
    
    for (int i = 0; i < allc.length; i++)
      try {
        Class.forName(allc[i]).newInstance();
      } catch (Exception e) {
        e.printStackTrace();
      }
    
    /*Method[] meth = dM.class.getMethods();
    try {
      for (int i = 0; i < meth.length; i++)
        if (meth[i].getReturnType().isAssignableFrom(QueryDataSet.class) &&
            meth[i].getName().startsWith("get") && meth[i].getParameterTypes().length == 0)
          meth[i].invoke(this, null);
    } catch (Exception e) {
      e.printStackTrace();
    }*/
  }

  public static String[] getAllDataSetGetters() {
    Method[] meth = dM.class.getMethods();
    ArrayList getters = new ArrayList();
    for (int i = 0; i < meth.length; i++)
      if (meth[i].getReturnType().isAssignableFrom(QueryDataSet.class) &&
          meth[i].getName().startsWith("get") && meth[i].getParameterTypes().length == 0)
        getters.add(meth[i].getName());
    Collections.sort(getters, new Comparator() {
      public int compare(Object o1, Object o2) {
        if (o1 instanceof String && o2 instanceof String)
          return ((String) o1).compareToIgnoreCase((String) o2);
        return 0;
      }
    });
    return (String[]) getters.toArray(new String[getters.size()]);
  }

  public static boolean isDataSetGetter(String name) {
    try {
      Method m = dM.class.getMethod(name, null);
      if (name.startsWith("get") && m.getReturnType().isAssignableFrom(QueryDataSet.class))
        return true;
    } catch (Exception e) {}
    return false;
  }
  
  protected static HashMap moduleNames = new HashMap(400);

  public static QueryDataSet getDataByName(String name) {
  	QueryDataSet ret = (QueryDataSet) moduleNames.get(name);
  	if (ret == null) 
  	try {
  		if (!name.startsWith("get")) name = "get" + name;
      Method m = dM.class.getMethod(name, null);
      if (m.getReturnType().isAssignableFrom(QueryDataSet.class))
      	moduleNames.put(name.substring(3), ret = (QueryDataSet) m.invoke(getDataModule(), null));
    } catch (Exception e) {}
    return ret;
  }
}

