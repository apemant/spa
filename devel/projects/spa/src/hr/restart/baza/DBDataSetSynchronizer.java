/****license*****************************************************************
**   file: DBDataSetSynchronizer.java
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
 * Created on 2004.12.22
 *
 */
package hr.restart.baza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.borland.dx.dataset.DataSet;


/**
 * Implementacija IDataSetSynchronizera koja koristi tablicu na bazi
 * za provjeru azurnosti ostalih tablica. Radi brzine provjera
 * dataset-a vrsi se maksimalno jednom svakih 5 sekundi.
 * @author abf
 */
public class DBDataSetSynchronizer implements IDataSetSynchronizer {
  Map tables = new HashMap();
  Map localChanges = new HashMap();
  
  Connection con;
  PreparedStatement get;
  PreparedStatement set;
  PreparedStatement add;
  
  public DBDataSetSynchronizer() {
    // empty
  }
  
  private void createStatements() {
  	try {
  		con = dM.getTempConnection();
			get = con.prepareStatement("SELECT * FROM tablesync WHERE imetab = ?");
			set = con.prepareStatement("UPDATE tablesync SET serialnum = ? WHERE imetab = ?");
			add = con.prepareStatement("INSERT INTO tablesync(imetab, serialnum) VALUES (?, 1)");
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		throw new RuntimeException("Error accessing sync base");
  }
  
  private TableRow getTableRow(String table) {
  	for (int i = 0; i < 2; i++)
			try {
				if (con == null || con.isClosed()) createStatements();
				get.setString(1, table);
				TableRow tr = null;
				ResultSet rs = get.executeQuery();
				if (rs.next()) tr = new TableRow(rs.getString(1), rs.getInt(2));
				rs.close();
				return tr;
			} catch (SQLException e) {
				e.printStackTrace();
			}
  	
  	throw new RuntimeException("Error accessing sync base");
  }
  
  private void addTableRow(String table) {
  	for (int i = 0; i < 2; i++)
    	try {
    		if (con == null || con.isClosed()) createStatements();
    		add.setString(1, table);
    		add.executeUpdate();
    		return;
    	} catch (SQLException e) {
  			e.printStackTrace();
  		}
    	
    throw new RuntimeException("Error accessing sync base");
  }
  
  private void updateTableRow(TableRow row) {
  	for (int i = 0; i < 2; i++)
    	try {
    		if (con == null || con.isClosed()) createStatements();
    		set.setInt(1, row.serial);
    		set.setString(2, row.table);
    		set.executeUpdate();
    		return;
    	} catch (SQLException e) {
  			e.printStackTrace();
  		}
    	
    throw new RuntimeException("Error accessing sync base");
  }
  
  private String getTableName(DataSet ds) {
    String table = ds.getColumn(0).getTableName();
    if (table == null || table.length() == 0) {
      System.out.println("Column 0 in dataset has empty tablename");
      System.out.println(ds);
      System.out.println(ds.getColumn(0));
      return null;
    }
    return table.toUpperCase();
  }
  
  private TableRow getTableStatus(String table) {
    TableInfo ti = (TableInfo) localChanges.get(table);
    if (ti == null) localChanges.put(table, ti = new TableInfo(1));
    
    TableRow tr = getTableRow(table);
    if (tr == null) {
    	addTableRow(table);
      return null;
    } 
    ti.time = System.currentTimeMillis();
    ti.serial = tr.serial;
    return tr;
  }
  
  private boolean dataRecentlyChecked(DataSet ds, String table) {
    if (tables.containsKey(ds)) {
      TableInfo ti = (TableInfo) tables.get(ds);
      if (localChanges.containsKey(table)) {
        TableInfo tl = (TableInfo) localChanges.get(table);
        if (tl.time > ti.time || ti.serial != tl.serial) return false;
      }
      if (ti.time + 500 > System.currentTimeMillis()) return true;      
    }
    return false;
  }
  
  private void markAsFresh_impl(DataSet ds) {
    String table = getTableName(ds);
    if (table != null) {
    	TableRow tr = getTableStatus(table);
      tables.put(ds, new TableInfo(tr == null ? 1 : tr.serial));
    }
  }
  
  private boolean checkAndFreshen(DataSet ds) {
    boolean toRefresh = false;
    String table = getTableName(ds);
    if (table != null) {
      if (dataRecentlyChecked(ds, table)) return false;
      TableRow tr = getTableStatus(table);
      if (tr != null) {        
        TableInfo ti = (TableInfo) tables.get(ds);
        if (ti == null || ti.serial != tr.serial) {
          toRefresh = true;
          tables.put(ds, new TableInfo(tr.serial));
        } else ti.time = System.currentTimeMillis();
      } else tables.put(ds, new TableInfo(1));
    }
    return toRefresh;
  }
  
  public void synchronize(DataSet ds) {
    boolean toOpen = false;
    boolean toRefresh = false;
    synchronized (this) {
      try {      
        if (!ds.refreshSupported()) return;
        if (!ds.isOpen()) {
          toOpen = true;
          //ds.open();
          markAsFresh_impl(ds);
        } else toRefresh = checkAndFreshen(ds);      
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (toOpen || toRefresh) Refresher.postpone();
    if (toOpen) ds.open();
    else if (toRefresh) ds.refresh();
  }
    
  public synchronized void propagateChanges(DataSet ds) {
    try {
      if (!ds.refreshSupported()) return;
      String table = getTableName(ds);
      if (table != null) {
        int serial = 1;
        TableRow tr = getTableStatus(table);
        if (tr != null) {
          serial = tr.serial;
          serial = serial >= 999999 ? 2 : serial + 1;
          tr.serial = serial;
          updateTableRow(tr);
        }
        localChanges.put(table, new TableInfo(serial));
        tables.put(ds, new TableInfo(serial));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public synchronized void markAsDirty(String table) {
    try {
      table = table.toUpperCase();
      TableRow tr = getTableStatus(table);
      if (tr != null) {
        int serial = tr.serial;
        serial = serial >= 99999 ? 2 : serial + 1;
        tr.serial = serial;
        updateTableRow(tr);
        localChanges.put(table, new TableInfo(serial));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }  
  }
  
  public synchronized void markAsFresh(DataSet ds) {    
    try {      
      markAsFresh_impl(ds);      
    } catch (Exception e) {
      e.printStackTrace();
    }  
  }

  public synchronized int getSerialNumber(String table) {
    try {
      table = table.toUpperCase();
      TableInfo ti = (TableInfo) localChanges.get(table);
      if (ti != null && (ti.time + 500 > System.currentTimeMillis()))
        return ti.serial;
      
      TableRow tr = getTableStatus(table);
      if (tr != null) return tr.serial;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
  
  
  public void dumpEverything() {
    long now = System.currentTimeMillis();
    ArrayList dsets = new ArrayList(tables.keySet());
    Collections.sort(dsets, new Comparator() {
      public int compare(Object o1, Object o2) {
        return getTableName((DataSet) o1).compareTo(getTableName((DataSet) o2));
      }
    });
    for (int i = 0; i < dsets.size(); i++) {
      DataSet ds = (DataSet) dsets.get(i);
      TableInfo ti = (TableInfo) tables.get(ds);
      System.out.println(getTableName(ds) + ": "+ti.serial+
          " (checked "+((now - ti.time) / 1000)+" seconds ago)");
    }
    System.out.println("------------------");
    ArrayList tabs = new ArrayList(localChanges.keySet());
    Collections.sort(tabs);
    for (int i = 0; i < tabs.size(); i++) {
      String tab = (String) tabs.get(i);
      TableInfo ti = (TableInfo) localChanges.get(tab);
      System.out.println(tab + ": "+" changed "+((now - ti.time) / 1000)+" seconds ago)");
    }
  }
}

class TableRow {
	public String table;
	public int serial;
	
	public TableRow(String name, int num) {
		table = name;
		serial = num;
	}
}

class TableInfo {
  int serial;
  long time;
  public TableInfo(int serial) {
    this.serial = serial;
    time = System.currentTimeMillis();
  }
}
