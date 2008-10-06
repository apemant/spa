/****license*****************************************************************
**   file: Grizvprim.java
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



public class Grizvprim extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Grizvprim Grizvprimclass;

  QueryDataSet grizvprim = new raDataSet();

  Column grizvprimCIZV = new Column();
  Column grizvprimCGRIZV = new Column();
  Column grizvprimCVRP = new Column();

  public static Grizvprim getDataModule() {
    if (Grizvprimclass == null) {
      Grizvprimclass = new Grizvprim();
    }
    return Grizvprimclass;
  }

  public QueryDataSet getQueryDataSet() {
    return grizvprim;
  }

  public Grizvprim() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    grizvprimCIZV.setCaption("Izvještaj");
    grizvprimCIZV.setColumnName("CIZV");
    grizvprimCIZV.setDataType(com.borland.dx.dataset.Variant.SHORT);
    grizvprimCIZV.setPrecision(4);
    grizvprimCIZV.setRowId(true);
    grizvprimCIZV.setTableName("GRIZVPRIM");
    grizvprimCIZV.setServerColumnName("CIZV");
    grizvprimCIZV.setSqlType(5);
    grizvprimCIZV.setWidth(4);
    grizvprimCGRIZV.setCaption("Grupa izvještaja");
    grizvprimCGRIZV.setColumnName("CGRIZV");
    grizvprimCGRIZV.setDataType(com.borland.dx.dataset.Variant.SHORT);
    grizvprimCGRIZV.setPrecision(4);
    grizvprimCGRIZV.setRowId(true);
    grizvprimCGRIZV.setTableName("GRIZVPRIM");
    grizvprimCGRIZV.setServerColumnName("CGRIZV");
    grizvprimCGRIZV.setSqlType(5);
    grizvprimCGRIZV.setWidth(4);
    grizvprimCVRP.setCaption("Vrsta primanja");
    grizvprimCVRP.setColumnName("CVRP");
    grizvprimCVRP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    grizvprimCVRP.setPrecision(3);
    grizvprimCVRP.setRowId(true);
    grizvprimCVRP.setTableName("GRIZVPRIM");
    grizvprimCVRP.setServerColumnName("CVRP");
    grizvprimCVRP.setSqlType(5);
    grizvprimCVRP.setWidth(3);
    grizvprim.setResolver(dm.getQresolver());
    grizvprim.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Grizvprim", null, true, Load.ALL));
 setColumns(new Column[] {grizvprimCIZV, grizvprimCGRIZV, grizvprimCVRP});
  }

  public void setall() {

    ddl.create("Grizvprim")
       .addShort("cizv", 4, true)
       .addShort("cgrizv", 4, true)
       .addShort("cvrp", 3, true)
       .addPrimaryKey("cizv,cgrizv,cvrp");


    Naziv = "Grizvprim";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
