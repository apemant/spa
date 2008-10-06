/****license*****************************************************************
**   file: Plosnprim.java
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



public class Plosnprim extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Plosnprim Plosnprimclass;

  QueryDataSet plosnprim = new raDataSet();

  Column plosnprimCVRP = new Column();
  Column plosnprimCOSN = new Column();

  public static Plosnprim getDataModule() {
    if (Plosnprimclass == null) {
      Plosnprimclass = new Plosnprim();
    }
    return Plosnprimclass;
  }

  public QueryDataSet getQueryDataSet() {
    return plosnprim;
  }

  public Plosnprim() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    plosnprimCVRP.setCaption("Oznaka vrste primanja");
    plosnprimCVRP.setColumnName("CVRP");
    plosnprimCVRP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    plosnprimCVRP.setPrecision(3);
    plosnprimCVRP.setRowId(true);
    plosnprimCVRP.setTableName("PLOSNPRIM");
    plosnprimCVRP.setServerColumnName("CVRP");
    plosnprimCVRP.setSqlType(5);
    plosnprimCVRP.setWidth(3);
    plosnprimCOSN.setCaption("Oznaka osnovice");
    plosnprimCOSN.setColumnName("COSN");
    plosnprimCOSN.setDataType(com.borland.dx.dataset.Variant.SHORT);
    plosnprimCOSN.setPrecision(2);
    plosnprimCOSN.setRowId(true);
    plosnprimCOSN.setTableName("PLOSNPRIM");
    plosnprimCOSN.setServerColumnName("COSN");
    plosnprimCOSN.setSqlType(5);
    plosnprimCOSN.setWidth(2);
    plosnprim.setResolver(dm.getQresolver());
    plosnprim.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Plosnprim", null, true, Load.ALL));
 setColumns(new Column[] {plosnprimCVRP, plosnprimCOSN});
  }

  public void setall() {

    ddl.create("Plosnprim")
       .addShort("cvrp", 3, true)
       .addShort("cosn", 2, true)
       .addPrimaryKey("cvrp,cosn");


    Naziv = "Plosnprim";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
