/****license*****************************************************************
**   file: TableSync.java
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
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;



public class TableSync extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static TableSync TableSyncclass;

  QueryDataSet tablesync = new QueryDataSet();

  Column tablesyncIMETAB = new Column();
  Column tablesyncSERIALNUM = new Column();

  public static TableSync getDataModule() {
    if (TableSyncclass == null) {
      TableSyncclass = new TableSync();
    }
    return TableSyncclass;
  }

  public QueryDataSet getQueryDataSet() {
    return tablesync;
  }

  public TableSync() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    tablesyncIMETAB.setCaption("Tablica");
    tablesyncIMETAB.setColumnName("IMETAB");
    tablesyncIMETAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    tablesyncIMETAB.setPrecision(20);
    tablesyncIMETAB.setRowId(true);
    tablesyncIMETAB.setTableName("TABLESYNC");
    tablesyncIMETAB.setServerColumnName("IMETAB");
    tablesyncIMETAB.setSqlType(1);
    tablesyncSERIALNUM.setCaption("Serijski broj izmjene");
    tablesyncSERIALNUM.setColumnName("SERIALNUM");
    tablesyncSERIALNUM.setDataType(com.borland.dx.dataset.Variant.INT);
    tablesyncSERIALNUM.setTableName("TABLESYNC");
    tablesyncSERIALNUM.setServerColumnName("SERIALNUM");
    tablesyncSERIALNUM.setSqlType(4);
    tablesyncSERIALNUM.setWidth(6);
    tablesync.setResolver(dm.getQresolver());
    tablesync.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from TableSync", null, true, Load.ALL));
    setColumns(new Column[] {tablesyncIMETAB, tablesyncSERIALNUM});
  }

  public void setall() {

    ddl.create("TableSync")
       .addChar("imetab", 20, true)
       .addInteger("serialnum", 6)
       .addPrimaryKey("imetab");


    Naziv = "TableSync";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
