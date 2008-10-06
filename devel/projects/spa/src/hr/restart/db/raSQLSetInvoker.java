/****license*****************************************************************
**   file: raSQLSetInvoker.java
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

public class raSQLSetInvoker {
  raSQLSetFactory factory = raSQLSetFactory.getSQLSetFactory();
//  open, saveChanges, moreData, finalize
  public static String OPEN = "open";
  public static String SAVECHANGES = "saveChanges";
  public static String MOREDATA = "moreData";
  public static String FINALIZE = "finalize";
  public raSQLSetInvoker() {
  }
  public void invokeSetOperation(raSQLSetComm comm) {
    raSQLSetComm new_comm;
    if (comm == null) return;
    if (comm.getMethod() == null) {
      comm = null;
      return;
    }
    if (comm.getMethod().equals(OPEN)) {
      new_comm = factory.openSQLSet(comm.getQuery(),comm.getConnProperties(),comm.getIdentifier());
    } else if (comm.getMethod().equals(SAVECHANGES)) {
      new_comm = factory.saveSQLSet(comm.getRows(),comm.getIdentifier());
    } else if (comm.getMethod().equals(MOREDATA)) {
      new_comm  = factory.provideMoreSQLData(comm.getLastRowIndex(),comm.getIdentifier());
    } else if (comm.getMethod().equals(FINALIZE)) {
      factory.disposeSQLSet(comm.getIdentifier());
      new_comm = new raSQLSetComm();
    } else {
      try {
        java.lang.reflect.Method meth = factory.getClass().getMethod(comm.getMethod(),new Class[] {raSQLSetComm.class});
        Object ret = meth.invoke(factory,new Object[] {comm});
        if (ret instanceof raSQLSetComm) {
          new_comm = (raSQLSetComm)ret;
        } else {
          new_comm = new raSQLSetComm();
        }
      }
      catch (Exception ex) {
        new_comm = new raSQLSetComm();
        new_comm.setExceptionMsg(ex.toString());
      }
    }
    copyTo(new_comm,comm);
  }
  private void copyTo(raSQLSetComm from, raSQLSetComm to) {
    try {
      to.setColNames(from.getColNames());
    } catch (Exception ex) {
      to.setColNames(null);
    }
    try {
      to.setExceptionMsg(from.getExceptionMsg());
    } catch (Exception ex) {
      to.setExceptionMsg(null);
    }

    try {
      to.setIdentifier(from.getIdentifier());
    } catch (Exception ex) {
      to.setIdentifier(null);
    }

    try {
      to.setKeys(from.getKeys());
    } catch (Exception ex) {
      to.setKeys(null);
    }

    try {
      to.setLastRowIndex(from.getLastRowIndex());
    } catch (Exception ex) {
      to.setLastRowIndex(-1);
    }

    try {
      to.setFirstRowIndex(from.getFirstRowIndex());
    } catch (Exception ex) {
      to.setLastRowIndex(-1);
    }

    try {
      to.setRowCount(from.getRowCount());
    } catch (Exception ex) {
      to.setLastRowIndex(-1);
    }

    try {
      to.setMetadata(from.getMetadata());
    } catch (Exception ex) {
      to.setMetadata(null);
    }

    try {
      to.setMethod(from.getMethod());
    } catch (Exception ex) {
      to.setMethod(null);
    }

    try {
      to.setQuery(from.getQuery());
    } catch (Exception ex) {
      to.setQuery(null);
    }

    try {
      to.setRows(from.getRows());
    } catch (Exception ex) {
      to.setRows(null);
    }

    try {
      to.setTableName(from.getTableName());
    } catch (Exception ex) {
      to.setTableName(null);
    }
  }
}