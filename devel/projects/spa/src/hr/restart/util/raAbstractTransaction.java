/****license*****************************************************************
**   file: raAbstractTransaction.java
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
package hr.restart.util;

import java.sql.Connection;
import java.util.HashSet;


public abstract class raAbstractTransaction {

  java.sql.Connection[] connections;
  com.borland.dx.sql.dataset.Database[] databases;
  
	protected Exception lastException = null;

  public raAbstractTransaction() {
    /** @todo sve baze iz connectionPool-a */

    connections = new Connection[] {hr.restart.baza.dM.getDataModule().getDatabase1().getJdbcConnection()};
    databases = new com.borland.dx.sql.dataset.Database[] {hr.restart.baza.dM.getDataModule().getDatabase1()};
  }

  public raAbstractTransaction(Connection[] cons) {
    connections = toConnections(cons);
  }

  public raAbstractTransaction(com.borland.dx.sql.dataset.Database[] dbs) {
    connections = toConnections(dbs);
    databases = toDataBases(dbs);
  }

  public raAbstractTransaction(com.borland.dx.sql.dataset.QueryDataSet[] qds) {
    connections = toConnections(qds);
    databases = toDataBases(qds);
  }

  public static Connection[] toConnections(java.sql.Connection[] conns) {
    HashSet conSet = new HashSet();
    for (int i = 0; i < conns.length; i++) {
      if (conns[i]!=null) conSet.add(conns[i]);
    }
    Connection[] r_conns = new Connection[conSet.size()];
    r_conns = (Connection[])conSet.toArray(r_conns);
    return r_conns;
  }

  public static Connection[] toConnections(com.borland.dx.sql.dataset.Database[] dbs) {
    HashSet conSet = new HashSet();
    for (int i = 0; i < dbs.length; i++) {
      if (dbs[i]!=null) conSet.add(dbs[i].getJdbcConnection());
    }
    Connection[] conns = new Connection[conSet.size()];
    conns = (Connection[])conSet.toArray(conns);
    return conns;
  }

  public static Connection[] toConnections(com.borland.dx.sql.dataset.QueryDataSet[] qds) {
    return toConnections(getDataBases(qds));
  }

  public static com.borland.dx.sql.dataset.Database[] toDataBases(com.borland.dx.sql.dataset.Database[] dbs) {
    HashSet dbSet = new HashSet();
    for (int i = 0; i < dbs.length; i++) {
      if (dbs[i]!=null) dbSet.add(dbs[i]);
    }
    com.borland.dx.sql.dataset.Database[] datbs = new com.borland.dx.sql.dataset.Database[dbSet.size()];
    datbs = (com.borland.dx.sql.dataset.Database[])dbSet.toArray(datbs);
    return datbs;
  }

  public static com.borland.dx.sql.dataset.Database[] toDataBases(com.borland.dx.sql.dataset.QueryDataSet[] qds) {
    return toDataBases(getDataBases(qds));
  }
  private static com.borland.dx.sql.dataset.Database[] getDataBases(com.borland.dx.sql.dataset.QueryDataSet[] qds) {
    com.borland.dx.sql.dataset.Database[] dbs = new com.borland.dx.sql.dataset.Database[qds.length];
    for (int i = 0; i < dbs.length; i++) {
      dbs[i] = qds[i].getDatabase();
    }
    return dbs;
  }
  
  public Exception getLastException() {
  	return lastException;
  }

  /**
   * starta transakciju implementiranu u public abstract boolean transaction() metodi sa pripadajucom logikom na zadanoj
   * ili defaultnoj bazi.
   * @return true ako je transakcija prosla, false ako nije
   */
  public abstract boolean execTransaction();

  public abstract boolean transaction() throws Exception;

}