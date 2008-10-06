/****license*****************************************************************
**   file: MenuUpdater.java
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
 */
package hr.restart.util.menus;

import hr.restart.db.raVariant;
import hr.restart.util.Aus;
import hr.restart.util.FileHandler;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author andrej
 * 1. Dumpa iz baze menije u file koji bi se trebao naci u jar.u
 * 2. Provjerava da li odgovaraju podaci u bazi sa podacima u file-u iz t.1.
 * 3. Sinkronizira file iz t.1. sa tablicom menus
 */
public class MenuUpdater {
  private static Logger log = Logger.getLogger(MenuUpdater.class);
  private static String insert_into_statement = "INSERT INTO MENUS VALUES (";
  private static String menudumpfile = "menudump.sql";
  private static QueryDataSet _menus;
  private static HashSet changesset;
  protected MenuUpdater() {
  }
  /**
   * 
   * @return info
   */
  public static String dumpToFileSQL() {
    String info;
    MenuFactory.createMenuDataSet();
    QueryDataSet menus = MenuFactory._qmenus;
    String[] cols = menus.getColumnNames(menus.getColumnCount());
    String filecontent = "";
    //clean
    new File(menudumpfile).delete();
    
    for (menus.first(); menus.inBounds(); menus.next()) {
      String insert_into = new String(insert_into_statement);
      for (int i = 0; i < cols.length; i++) {
        insert_into = insert_into 
        	+ getNavodnik(menus, cols[i])
        	+ raVariant.getDataSetValue(menus,cols[i]).toString().trim()
        	+ getNavodnik(menus, cols[i])+",";
      }
      VarStr line = new VarStr(insert_into).replaceLast(",",")");
      filecontent = filecontent+line.toString()+"\n";
    }
    
    FileHandler.writeConverted(filecontent,menudumpfile,null);
    info = "Zapisano "+menus.getRowCount()+" slogova \n u file "+new File(menudumpfile).getAbsolutePath();
    if (log.isDebugEnabled()) {
      log.debug(info);
    }
    return info;
  }
  /**
   * 
   * @param mode 0-execute all statements, 1-test equality, 2-test and update differences
   * @return info
   */
  public static String processFileSQL(int mode) {
    String info = "";
    File f = Aus.findFileAnywhere(menudumpfile);
    changesset = null;
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      QueryDataSet menus = getMenuDataSet();
      menus.refresh();
      String[] cols = menus.getColumnNames(menus.getColumnCount());
      Statement stmt;
      stmt = menus.getDatabase().getJdbcConnection().createStatement();
      String line = reader.readLine();
      int i=0;
      while (line != null) {
        if (mode == 0) {//execute
          stmt.execute(line);
        } else if (mode == 1 || mode == 2) {//test
          testLine(line);  
        }
        i++;
        line = reader.readLine();
      }
      info = info + "\n "+i+" linija obraðeno";
      info = info + "\n "+getChangesSet().size()+" linija "+(mode == 0?"izvršeno":"razlièito");
      //update diffs
      if (mode == 2) {
        PreparedStatement updateStatement = menus.getDatabase().getJdbcConnection()
        .prepareStatement("UPDATE MENUS SET CSORT = ?, MENUTYPE= ?, METHOD = ? WHERE CMENU = ? AND PARENTCMENU = ?");//zbog hsql-a moram tako grrr ante
        int updcnt = 0;
        int addcnt = 0;
	      for (Iterator iter = getChangesSet().iterator(); iter.hasNext();) {
	        UpdateInfo element = (UpdateInfo) iter.next();
	        
	        if (element.isUpdated()) {
	          updateStatement.setInt(1, Integer.parseInt(element.getVals()[1]));
	          updateStatement.setString(2, element.getVals()[3]);
	          updateStatement.setString(3, element.getVals()[4]);
	          updateStatement.setString(4, element.getVals()[0]);
	          updateStatement.setString(5, element.getVals()[2]);
	          updateStatement.execute();
	          updcnt++;
	        } else {
	          stmt.execute(element.getLine());
	          addcnt++;
	        }
	        if (log.isDebugEnabled()) {
	          log.debug(element.getLine()+ " succesfully updated");
	        }
	      }
	      info = info + "\n " + updcnt + " promijenjeno";
	      info = info + "\n " + addcnt + " dodano";
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (log.isDebugEnabled()) {
      log.debug(info);
    }
    return info;
  }
  /**
   * @param line
   */
  private static void testLine(String line) {
    String strline = new VarStr(line)
    											.replace(insert_into_statement,"")
    											.replaceAll(")","")
    											.replaceAll("'","").toString();
    if (log.isDebugEnabled()) {
      log.debug("Testing "+strline);
    }
    StringTokenizer tok = new StringTokenizer(strline,",");
    QueryDataSet menus = getMenuDataSet();
    String[] cols = menus.getColumnNames(menus.getColumnCount());
    String[] vals = new String[cols.length];
    for (int i = 0; i < vals.length; i++) {
      try {
        vals[i] = tok.nextToken();
      } catch (NoSuchElementException e) {
        log.debug("NoSuchElementException: vjerojatno prazan method");
        vals[i] = "";
      }
    }
    if (!lookupData.getlookupData().raLocate(menus,cols,vals)) {
      boolean updated = lookupData.getlookupData().raLocate(menus,new String[]{cols[0],cols[2]},new String[]{vals[0],vals[2]});
      getChangesSet().add(new UpdateInfo(line,vals,updated));
      if (log.isDebugEnabled()) {
        log.debug(strline+" marked for "+(updated?"update":"adding"));
      }
    } else {
      if (log.isDebugEnabled()) {
        log.debug(strline+" is ok !");
      }
    }
  }
  /**
   * 
   */
  private static HashSet getChangesSet() {
    if (changesset == null) changesset = new HashSet();
    return changesset;
  }
  /**
   * @param menus
   * @param string
   * @return
   */
  private static String getNavodnik(QueryDataSet menus, String cn) {
    Column col = menus.getColumn(cn);
    if (!Valid.getValid().isNumeric(col)) return "'";
    return "";
  }
  
  private static QueryDataSet getMenuDataSet() {
    if (_menus == null) {
	    MenuFactory.createMenuDataSet();
	    _menus = MenuFactory._qmenus;
    }
    return _menus;
  }
  
  static String deleteAllMenus() {
    try {
      Statement stmt = getMenuDataSet().getDatabase().getJdbcConnection().createStatement();
      int aff = stmt.executeUpdate("DELETE FROM MENUS");
      return aff + "slogova obrisano"; 
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return e.getMessage();
    }
  }
  
 public static void main(String[] args) {
    //dumpToFileSQL();
    //addFromFileSQL();
  }
}
