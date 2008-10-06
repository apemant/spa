/****license*****************************************************************
**   file: Grizvodb.java
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



public class Grizvodb extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Grizvodb Grizvodbclass;

  QueryDataSet griz = new QueryDataSet();

  Column grizCIZV = new Column();
  Column grizCGRIZV = new Column();
  Column grizCVRODB = new Column();

  public static Grizvodb getDataModule() {
    if (Grizvodbclass == null) {
      Grizvodbclass = new Grizvodb();
    }
    return Grizvodbclass;
  }

  public QueryDataSet getQueryDataSet() {
    return griz;
  }

  public Grizvodb() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    grizCIZV.setCaption("Izvještaj");
    grizCIZV.setColumnName("CIZV");
    grizCIZV.setDataType(com.borland.dx.dataset.Variant.SHORT);
    grizCIZV.setPrecision(4);
    grizCIZV.setRowId(true);
    grizCIZV.setTableName("GRIZVODB");
    grizCIZV.setServerColumnName("CIZV");
    grizCIZV.setSqlType(5);
    grizCIZV.setWidth(4);
    grizCGRIZV.setCaption("Grupa izvještaja");
    grizCGRIZV.setColumnName("CGRIZV");
    grizCGRIZV.setDataType(com.borland.dx.dataset.Variant.SHORT);
    grizCGRIZV.setPrecision(4);
    grizCGRIZV.setRowId(true);
    grizCGRIZV.setTableName("GRIZVODB");
    grizCGRIZV.setServerColumnName("CGRIZV");
    grizCGRIZV.setSqlType(5);
    grizCGRIZV.setWidth(4);
    grizCVRODB.setCaption("Vrsta odbitka");
    grizCVRODB.setColumnName("CVRODB");
    grizCVRODB.setDataType(com.borland.dx.dataset.Variant.SHORT);
    grizCVRODB.setPrecision(4);
    grizCVRODB.setRowId(true);
    grizCVRODB.setTableName("GRIZVODB");
    grizCVRODB.setServerColumnName("CVRODB");
    grizCVRODB.setSqlType(5);
    grizCVRODB.setWidth(4);
    griz.setResolver(dm.getQresolver());
    griz.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Grizvodb", null, true, Load.ALL));
 setColumns(new Column[] {grizCIZV, grizCGRIZV, grizCVRODB});
  }

  public void setall() {

    ddl.create("Grizvodb")
       .addShort("cizv", 4, true)
       .addShort("cgrizv", 4, true)
       .addShort("cvrodb", 4, true)
       .addPrimaryKey("cizv,cgrizv,cvrodb");


    Naziv = "Grizvodb";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
