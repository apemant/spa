/****license*****************************************************************
**   file: raLocalTransaction.java
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

import hr.restart.baza.Refresher;

import java.sql.Connection;

public abstract class raLocalTransaction extends raAbstractTransaction {
	
  public raLocalTransaction() {
    super();
  }

  public raLocalTransaction(Connection[] cons) {
    super(cons);
  }

  public raLocalTransaction(com.borland.dx.sql.dataset.Database[] dbs) {
    super(dbs);
  }

  public raLocalTransaction(com.borland.dx.sql.dataset.QueryDataSet[] qds) {
    super(qds);
  }

  private boolean startTrans() {
    if (databases != null) {
      return raTransaction.startTransaction(databases);
    } else if (connections != null) {
      return raTransaction.startTransaction(connections);
    }
    return false;
  }

  private boolean commitTrans() {
    if (databases != null) {
      return raTransaction.commitTransaction(databases);
    } else if (connections != null) {
      return raTransaction.commitTransaction(connections);
    }
    return false;
  }

  private boolean rollbackTrans() {
    if (databases != null) {
      return raTransaction.rollbackTransaction(databases);
    } else if (connections != null) {
      return raTransaction.rollbackTransaction(connections);
    }
    return false;
  }

  public abstract boolean transaction() throws Exception;

  private void throwException(String txt) throws Exception {
//    new Throwable(txt).printStackTrace();
    throw new Exception(txt);
  }
  public boolean execTransaction() {
//sysoutTEST ST = new sysoutTEST(false);
//System.out.println("conns = "+connections);
//if (connections!=null) ST.prn(connections);
//System.out.println("dbs = "+databases);
//if (databases!=null) ST.prn(databases);
    try {
      Refresher.postpone();
      if (!startTrans()) throwException("startTransaction nije uspio!!");
      if (!transaction()) throwException("transaction() nije uspio!!");
      if (!commitTrans()) throwException("commitTransaction nije uspio");
      return true;
    }
    catch (Exception ex) {
//System.out.println("ex = "+ex);//      ex.printStackTrace();
//System.out.println("rollbacking .......");
      ex.printStackTrace();
      rollbackTrans();
      lastException = ex;
      return false;
    }
  }

  /**
   * Snima dataset bez da commita transakciju.
   * To je, nažalost, jedini na\u010Din da se snimi QueryDataset bez da odmah commita transakciju
   * @param qds Querydataset koji se snima
   */
  public void saveChanges(com.borland.dx.sql.dataset.QueryDataSet qds) {
    raTransaction.saveChanges(qds);
  }
}