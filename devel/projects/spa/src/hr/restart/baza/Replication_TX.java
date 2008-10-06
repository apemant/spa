/****license*****************************************************************
**   file: Replication_TX.java
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



public class Replication_TX extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Replication_TX Replication_TXclass;

  QueryDataSet rtx = new QueryDataSet();

  Column rtxLAST_TX = new Column();

  public static Replication_TX getDataModule() {
    if (Replication_TXclass == null) {
      Replication_TXclass = new Replication_TX();
    }
    return Replication_TXclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rtx;
  }

  public Replication_TX() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    rtxLAST_TX.setCaption("Zadnja TX");
    rtxLAST_TX.setColumnName("LAST_TX");
    rtxLAST_TX.setDataType(com.borland.dx.dataset.Variant.INT);
    rtxLAST_TX.setRowId(true);
    rtxLAST_TX.setTableName("REPLICATION_TX");
    rtxLAST_TX.setServerColumnName("LAST_TX");
    rtxLAST_TX.setSqlType(4);
    rtxLAST_TX.setWidth(6);
    rtx.setResolver(dm.getQresolver());
    rtx.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Replication_TX", null, true, Load.ALL));
    setColumns(new Column[] {rtxLAST_TX});
  }

  public void setall() {

    ddl.create("Replication_TX")
       .addInteger("last_tx", 6, true)
       .addPrimaryKey("last_tx");


    Naziv = "Replication_TX";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
