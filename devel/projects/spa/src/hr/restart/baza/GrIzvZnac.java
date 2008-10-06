/****license*****************************************************************
**   file: GrIzvZnac.java
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



public class GrIzvZnac extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static GrIzvZnac GrIzvZnacclass;

  QueryDataSet giz = new raDataSet();

  Column gizCIZV = new Column();
  Column gizCGRIZV = new Column();
  Column gizCZNAC = new Column();

  public static GrIzvZnac getDataModule() {
    if (GrIzvZnacclass == null) {
      GrIzvZnacclass = new GrIzvZnac();
    }
    return GrIzvZnacclass;
  }

  public QueryDataSet getQueryDataSet() {
    return giz;
  }

  public GrIzvZnac() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    gizCIZV.setCaption("Izvještaj");
    gizCIZV.setColumnName("CIZV");
    gizCIZV.setDataType(com.borland.dx.dataset.Variant.SHORT);
    gizCIZV.setPrecision(4);
    gizCIZV.setRowId(true);
    gizCIZV.setTableName("GRIZVZNAC");
    gizCIZV.setServerColumnName("CIZV");
    gizCIZV.setSqlType(5);
    gizCIZV.setWidth(4);
    gizCGRIZV.setCaption("Grupa izvještaja");
    gizCGRIZV.setColumnName("CGRIZV");
    gizCGRIZV.setDataType(com.borland.dx.dataset.Variant.SHORT);
    gizCGRIZV.setPrecision(4);
    gizCGRIZV.setRowId(true);
    gizCGRIZV.setTableName("GRIZVZNAC");
    gizCGRIZV.setServerColumnName("CGRIZV");
    gizCGRIZV.setSqlType(5);
    gizCGRIZV.setWidth(4);
    gizCZNAC.setCaption("Zna\u010Dajka");
    gizCZNAC.setColumnName("CZNAC");
    gizCZNAC.setDataType(com.borland.dx.dataset.Variant.SHORT);
    gizCZNAC.setPrecision(4);
    gizCZNAC.setRowId(true);
    gizCZNAC.setTableName("GRIZVZNAC");
    gizCZNAC.setServerColumnName("CZNAC");
    gizCZNAC.setSqlType(5);
    gizCZNAC.setWidth(4);
    giz.setResolver(dm.getQresolver());
    giz.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from GrIzvZnac", null, true, Load.ALL));
    setColumns(new Column[] {gizCIZV, gizCGRIZV, gizCZNAC});
  }

  public void setall() {

    ddl.create("GrIzvZnac")
       .addShort("cizv", 4, true)
       .addShort("cgrizv", 4, true)
       .addShort("cznac", 4, true)
       .addPrimaryKey("cizv,cgrizv,cznac");


    Naziv = "GrIzvZnac";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
