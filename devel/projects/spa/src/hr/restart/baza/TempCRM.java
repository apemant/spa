/****license*****************************************************************
**   file: TempCRM.java
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



public class TempCRM extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static TempCRM TempCRMclass;

  QueryDataSet tc = new QueryDataSet();

  Column tcMB = new Column();

  public static TempCRM getDataModule() {
    if (TempCRMclass == null) {
      TempCRMclass = new TempCRM();
    }
    return TempCRMclass;
  }

  public QueryDataSet getQueryDataSet() {
    return tc;
  }

  public TempCRM() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    tcMB.setCaption("Matièni broj");
    tcMB.setColumnName("MB");
    tcMB.setDataType(com.borland.dx.dataset.Variant.STRING);
    tcMB.setPrecision(13);
    tcMB.setRowId(true);
    tcMB.setTableName("TEMPCRM");
    tcMB.setServerColumnName("MB");
    tcMB.setSqlType(1);
    tc.setResolver(dm.getQresolver());
    tc.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from TempCRM", null, true, Load.ALL));
    setColumns(new Column[] {tcMB});
  }

  public void setall() {

    ddl.create("TempCRM")
       .addChar("mb", 13, true)
       .addPrimaryKey("mb");


    Naziv = "TempCRM";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
