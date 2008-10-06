/****license*****************************************************************
**   file: raClientSQLSet.java
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

public class raClientSQLSet extends raSQLSet {
  String serverIdentifier = null;
  private java.util.Properties connectionProperties;

  public raClientSQLSet() {
  }

  public raClientSQLSet(String qry) {
    super(qry);
  }
  public void setConnectionProperties(java.util.Properties props) {
    connectionProperties = props;
  }
  public java.util.Properties getConnectionProperties() {
    if (connectionProperties!=null) return connectionProperties;
    connectionProperties = raConnectionFactory.getRestArtConnectionProperties();
    return connectionProperties;
  }
  public void open() throws java.lang.Exception {
    if (isOpen()) return;
    if (getQuery() == null) throw new NullPointerException("Query string is null!!");
    if (!hr.restart.start.isClientConnected()) throw new java.io.IOException("Not conected to client!!");
    hr.restart.util.client.Client client = hr.restart.start.getClient();

    raSQLSetComm comm = new raSQLSetComm(getQuery(),getConnectionProperties(),client.getConnection().getID());
    comm = (raSQLSetComm)client.remoteInvoke(comm,"invokeSetOperation","hr.restart.db.raSQLSetInvoker",new Object[] {comm});
//new raSQLSetInvoker().invokeSetOperation(comm); <- to ako nije konektan
    if (comm.getExceptionMsg() != null) throw new Exception(comm.getExceptionMsg());
    serverIdentifier = comm.getIdentifier();
    setColNames(comm.getColNames());
//    setMetadata(comm.getMetadata());
    setTableName(comm.getTableName());
    setKeys(comm.getKeys());
    setRows(comm.getRows());
    setRowCount(comm.getRowCount());
    setFirstRowIndex(comm.getFirstRowIndex());
    setLastRowIndex(comm.getLastRowIndex());
    setOpenFlag();
    try {
      checkForSave();
      setSaveChangesEnabled(true);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      setSaveChangesEnabled(false);
    }

  }
  public void saveChanges() throws java.lang.Exception {
    if (!isOpen()) return;
    if (!isSaveChangesEnabled()) return;
    if (!hr.restart.start.isClientConnected()) throw new java.io.IOException("Not conected to client!!");
    hr.restart.util.client.Client client = hr.restart.start.getClient();
    java.util.Hashtable changedRows = new java.util.Hashtable();
    java.util.Hashtable allRows = getRows();
    int j = 1;
    for (int i = 0; i < allRows.size(); i++) {
      raValueRow crow = (raValueRow)allRows.get(new Integer(i+1));
      if (crow.getState()!=' ') {
        changedRows.put(new Integer(j),crow);
        j++;
      }
    }
    if (changedRows.size() == 0) return;
    raSQLSetComm comm = new raSQLSetComm(changedRows,serverIdentifier);
    comm = (raSQLSetComm)client.remoteInvoke(comm,"invokeSetOperation","hr.restart.db.raSQLSetInvoker",new Object[] {comm});
    if (comm.getExceptionMsg() != null) {
      changedRows.clear();
      allRows = null;
      throw new Exception(comm.getExceptionMsg());
    }
    changedRows.clear();
    j = 1;
    for (int i = 0; i < allRows.size(); i++) {
      Integer iindex = new Integer(i+1);
      raValueRow crow = (raValueRow)allRows.get(iindex);
      if (crow.getState()!='D') {
        crow.setState(' ');
        changedRows.put(new Integer(j),crow);
        j++;
      }
    }
    setRows(changedRows);
  }
  public boolean moreData() throws Exception {
    /** @todo implement this fuckin' method */
    return false;
  }
  public void finalize() throws Throwable {
    if (!hr.restart.start.isClientConnected()) throw new java.io.IOException("Not conected to client!!");
    hr.restart.util.client.Client client = hr.restart.start.getClient();
    raSQLSetComm comm = new raSQLSetComm();
    comm.setMethod(raSQLSetInvoker.FINALIZE);
    comm.setIdentifier(serverIdentifier);
    comm = (raSQLSetComm)client.remoteInvoke(comm,"invokeSetOperation","hr.restart.db.raSQLSetInvoker",new Object[] {comm});
    super.finalize();
  }

}