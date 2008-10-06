/****license*****************************************************************
**   file: RN_znachint.java
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



public class RN_znachint extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static RN_znachint RN_znachintclass;

  QueryDataSet rnh = new raDataSet();

  Column rnhCVRSUBJ = new Column();
  Column rnhCHINT = new Column();
  Column rnhZHINT = new Column();

  public static RN_znachint getDataModule() {
    if (RN_znachintclass == null) {
      RN_znachintclass = new RN_znachint();
    }
    return RN_znachintclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rnh;
  }

  public RN_znachint() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    rnhCVRSUBJ.setCaption("Vrsta subjekta");
    rnhCVRSUBJ.setColumnName("CVRSUBJ");
    rnhCVRSUBJ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rnhCVRSUBJ.setPrecision(3);
    rnhCVRSUBJ.setRowId(true);
    rnhCVRSUBJ.setTableName("RN_ZNACHINT");
    rnhCVRSUBJ.setServerColumnName("CVRSUBJ");
    rnhCVRSUBJ.setSqlType(5);
    rnhCVRSUBJ.setWidth(3);
    rnhCHINT.setCaption("Šifra linije");
    rnhCHINT.setColumnName("CHINT");
    rnhCHINT.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rnhCHINT.setPrecision(3);
    rnhCHINT.setRowId(true);
    rnhCHINT.setTableName("RN_ZNACHINT");
    rnhCHINT.setServerColumnName("CHINT");
    rnhCHINT.setSqlType(5);
    rnhCHINT.setWidth(3);
    rnhZHINT.setCaption("Popis hintova");
    rnhZHINT.setColumnName("ZHINT");
    rnhZHINT.setDataType(com.borland.dx.dataset.Variant.STRING);
    rnhZHINT.setPrecision(100);
    rnhZHINT.setTableName("RN_ZNACHINT");
    rnhZHINT.setServerColumnName("ZHINT");
    rnhZHINT.setSqlType(1);
    rnhZHINT.setWidth(30);
    rnh.setResolver(dm.getQresolver());
    rnh.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from RN_znachint", null, true, Load.ALL));
    setColumns(new Column[] {rnhCVRSUBJ, rnhCHINT, rnhZHINT});
  }

  public void setall() {

    ddl.create("RN_znachint")
       .addShort("cvrsubj", 3, true)
       .addShort("chint", 3, true)
       .addChar("zhint", 100)
       .addPrimaryKey("cvrsubj,chint");


    Naziv = "RN_znachint";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
