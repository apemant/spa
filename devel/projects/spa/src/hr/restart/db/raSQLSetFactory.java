/****license*****************************************************************
**   file: raSQLSetFactory.java
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

public class raSQLSetFactory {
  private java.util.Hashtable sets = new java.util.Hashtable();
  private String clientSeparator = "/#/";
  private int maxRowsToClient = 20; // -> properties
  private static raSQLSetFactory _this;
  private hr.restart.util.server.Server server;
  /**
   *   C L I E N T                            S   E   R   V   E   R
   * raClientSQLSet -> raSQLSetComm ->                                     <- raFastSQLSet <-
   * raClientSQLSet -> raSQLSetComm -> raSQLSetInvoker <- raSQLSetFactory <- raFastSQLSet <- java.sql.Connection
   * raClientSQLSet -> raSQLSetComm ->                                     <- raFastSQLSet <-
   *
   */
  protected raSQLSetFactory() {
    server = hr.restart.util.server.Server.getServer();
    server.addPropertyChangeListener(
      server.CL_DISCONNECTED,
      new java.beans.PropertyChangeListener() {
        public void propertyChange(java.beans.PropertyChangeEvent pcev) {
          disposeAllSQLSets((String)pcev.getNewValue());
        }
      }
    );
  }

  public static raSQLSetFactory getSQLSetFactory() {
    if (_this == null) _this = new raSQLSetFactory();
    return _this;
  }

  public raSQLSetComm openSQLSet(String query, java.util.Properties connProps, String identifier) {
    try {
      String ident = getSQLSet(identifier);
      raFastSQLSet set = (raFastSQLSet)sets.get(ident);
      set.setQuery(query);
      if (connProps != null) set.setConnection(raConnectionFactory.getConnection(connProps));
      set.open();
      raSQLSetComm comm = new raSQLSetComm(set);
      comm.setIdentifier(ident);
      return comm;
    }
    catch (Exception ex) {
      raSQLSetComm comm = new raSQLSetComm();
      comm.setExceptionMsg(ex.toString());
      return comm;
    }
  }

  public raSQLSetComm saveSQLSet(java.util.Hashtable rws,String identifier) {
    raSQLSetComm comm = new raSQLSetComm();
    try {
      raFastSQLSet set = (raFastSQLSet)sets.get(identifier);
      set.setRows(rws);
      set.saveChanges();
    }
    catch (Exception ex) {
      comm.setExceptionMsg(ex.toString());
    }
    return comm;
  }

  public raSQLSetComm provideMoreSQLData(int fromIdx, String identifier) {
    raSQLSetComm comm =  new raSQLSetComm();
    try {
      boolean noMore = false;
      raFastSQLSet set = (raFastSQLSet)sets.get(identifier);
      java.util.Hashtable moreRows = new java.util.Hashtable();
      java.util.Hashtable allRows = set.getRows();
      int toIdx = fromIdx + maxRowsToClient;
      if (toIdx > set.getRowCount()) {
        toIdx = set.getRowCount();
        noMore = true;
      }
      for (int i = fromIdx; i <= toIdx; i++) {
        moreRows.put(new Integer(i),allRows.get(new Integer(i)));
      }
      if (moreRows.size() > 0) {
        comm.setRows(moreRows);
        if (noMore) comm.setExceptionMsg("noMoreData");
        return comm;
      } else {
        comm.setExceptionMsg("noMoreData");
        return comm;
      }
    }
    catch (Exception ex) {
      comm.setExceptionMsg(ex.toString());
      return comm;
    }
  }

  public void disposeSQLSet(String identifier) {
    try {
      raFastSQLSet set = (raFastSQLSet)sets.remove(identifier);
      set.finalize();
      set = null;
    }
    catch (Throwable ex) {
      System.out.println(ex);
    }
  }
  public void disposeAllSQLSets(String clID) {
    java.util.Enumeration keys = sets.keys();
    while (keys.hasMoreElements()) {
      String ident = (String)keys.nextElement();
      if (ident.startsWith(clID.concat(clientSeparator))) {
        disposeSQLSet(ident);
      }
    }
  }
  private String getSQLSet(String identifier) throws Exception {
    raFastSQLSet sset;
    Object o = sets.get(identifier);
    if (o == null) {
      sset = new raFastSQLSet();
      String ident = getNewIdentifier(identifier,sset);
      sets.put(ident,sset);
      return ident;
    }
    return identifier;
  }
  private String getNewIdentifier(String identifier, raFastSQLSet sset) throws Exception {
    String ident;
    int ididx = identifier.indexOf(clientSeparator);
    if (ididx < 0) {
      ident = identifier;
    } else {
      ident = identifier.substring(0,ididx);
    }
    ident = ident.concat(clientSeparator).concat(sset.toString());
    return ident;
  }
}