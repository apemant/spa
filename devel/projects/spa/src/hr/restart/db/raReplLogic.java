/****license*****************************************************************
**   file: raReplLogic.java
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


public class raReplLogic implements raReplicator {

  public raReplLogic() {
  }
  public boolean repl_1(raReplicate.ReplDataSet ds_from, raReplicate.ReplDataSet ds_to) {
/*
    if (ds_from.ds.getRowCount() == 0) {
      return true;
    }
    raPreparedStatement ps_exists = new raPreparedStatement(ds_to.imetab,raPreparedStatement.COUNT,ds_to.ds.getDatabase().getJdbcConnection());
    raPreparedStatement ps_insert = new raPreparedStatement(ds_to.imetab,raPreparedStatement.INSERT,ds_to.ds.getDatabase().getJdbcConnection());
    raPreparedStatement ps_update = new raPreparedStatement(ds_to.imetab,raPreparedStatement.UPDATE,ds_to.ds.getDatabase().getJdbcConnection());
    ds_from.ds.first();
    do {
      try {
        ps_exists.setKeys(ds_from.ds);
        if (ps_exists.isExist()) {
          ps_update.setKeys(ds_from.ds);
          ps_update.setValues(ds_from.ds);
          ps_update.execute();
        } else {
          ps_insert.setValues(ds_from.ds);
          ps_insert.execute();
        }
//FLAGOVI???
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }

    } while (ds_from.ds.next());

/*  // TEST*/
    System.out.println("ds_from.query = "+ds_from.ds.getQuery().getQueryString());
    System.out.println("url = "+ds_from.ds.getDatabase().getConnection().getConnectionURL());
    System.out.println("ds_from.tabla = "+ds_from.imetab);
    System.out.println("ds_from.nacinrepl = "+ds_from.nacinrepl);
    System.out.println("ds_from.iz = "+ds_from.iz);

    System.out.println("ds_to.query = "+ds_to.ds.getQuery().getQueryString());
    System.out.println("url = "+ds_to.ds.getDatabase().getConnection().getConnectionURL());
    System.out.println("ds_to.tabla = "+ds_to.imetab);
    System.out.println("ds_to.nacinrepl = "+ds_to.nacinrepl);
    System.out.println("ds_to.iz = "+ds_to.iz);
//*///ENDTEST
    return true;
  }
  public boolean repl_2(raReplicate.ReplDataSet ds_from, raReplicate.ReplDataSet ds_to) {
    /**@todo Implement this hr.restart.db.raReplicator method*/
    throw new java.lang.UnsupportedOperationException("Method repl_2() not yet implemented.");
  }
  public boolean repl_3(raReplicate.ReplDataSet ds_from, raReplicate.ReplDataSet ds_to) {
    /**@todo Implement this hr.restart.db.raReplicator method*/
    throw new java.lang.UnsupportedOperationException("Method repl_3() not yet implemented.");
  }
  public boolean repl_4(raReplicate.ReplDataSet ds_from, raReplicate.ReplDataSet ds_to) {
    /**@todo Implement this hr.restart.db.raReplicator method*/
    throw new java.lang.UnsupportedOperationException("Method repl_4() not yet implemented.");
  }
  public boolean repl_5(raReplicate.ReplDataSet ds_from, raReplicate.ReplDataSet ds_to) {
    /**@todo Implement this hr.restart.db.raReplicator method*/
    throw new java.lang.UnsupportedOperationException("Method repl_5() not yet implemented.");
  }
  public boolean repl_6(raReplicate.ReplDataSet ds_from, raReplicate.ReplDataSet ds_to) {
    /**@todo Implement this hr.restart.db.raReplicator method*/
    throw new java.lang.UnsupportedOperationException("Method repl_6() not yet implemented.");
  }
  public boolean repl_7(raReplicate.ReplDataSet ds_from, raReplicate.ReplDataSet ds_to) {
    /**@todo Implement this hr.restart.db.raReplicator method*/
    throw new java.lang.UnsupportedOperationException("Method repl_7() not yet implemented.");
  }
  public boolean repl_8(raReplicate.ReplDataSet ds_from, raReplicate.ReplDataSet ds_to) {
    /**@todo Implement this hr.restart.db.raReplicator method*/
    throw new java.lang.UnsupportedOperationException("Method repl_8() not yet implemented.");
  }
  public boolean repl_9(raReplicate.ReplDataSet ds_from, raReplicate.ReplDataSet ds_to) {
    /**@todo Implement this hr.restart.db.raReplicator method*/
    throw new java.lang.UnsupportedOperationException("Method repl_9() not yet implemented.");
  }
}