/****license*****************************************************************
**   file: Carpostpp37.java
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



public class Carpostpp37 extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carpostpp37 Carpostpp37class;

  QueryDataSet carpostpp37 = new QueryDataSet();

  Column carpostpp37LOCK = new Column();
  Column carpostpp37AKTIV = new Column();
  Column carpostpp37CPP37 = new Column();
  Column carpostpp37NAZIV = new Column();

  public static Carpostpp37 getDataModule() {
    if (Carpostpp37class == null) {
      Carpostpp37class = new Carpostpp37();
    }
    return Carpostpp37class;
  }

  public QueryDataSet getQueryDataSet() {
    return carpostpp37;
  }

  public Carpostpp37() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carpostpp37LOCK.setCaption("Status zauzetosti");
    carpostpp37LOCK.setColumnName("LOCK");
    carpostpp37LOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpostpp37LOCK.setPrecision(1);
    carpostpp37LOCK.setTableName("CARPOSTPP37");
    carpostpp37LOCK.setServerColumnName("LOCK");
    carpostpp37LOCK.setSqlType(1);
    carpostpp37LOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carpostpp37LOCK.setDefault("N");
    carpostpp37AKTIV.setCaption("Aktivan - neaktivan");
    carpostpp37AKTIV.setColumnName("AKTIV");
    carpostpp37AKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpostpp37AKTIV.setPrecision(1);
    carpostpp37AKTIV.setTableName("CARPOSTPP37");
    carpostpp37AKTIV.setServerColumnName("AKTIV");
    carpostpp37AKTIV.setSqlType(1);
    carpostpp37AKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carpostpp37AKTIV.setDefault("D");
    carpostpp37CPP37.setCaption("Šifra carinskog postupka pp37");
    carpostpp37CPP37.setColumnName("CPP37");
    carpostpp37CPP37.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpostpp37CPP37.setPrecision(10);
    carpostpp37CPP37.setRowId(true);
    carpostpp37CPP37.setTableName("CARPOSTPP37");
    carpostpp37CPP37.setServerColumnName("CPP37");
    carpostpp37CPP37.setSqlType(1);
    carpostpp37NAZIV.setCaption("Naziv");
    carpostpp37NAZIV.setColumnName("NAZIV");
    carpostpp37NAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpostpp37NAZIV.setPrecision(100);
    carpostpp37NAZIV.setTableName("CARPOSTPP37");
    carpostpp37NAZIV.setServerColumnName("NAZIV");
    carpostpp37NAZIV.setSqlType(1);
    carpostpp37NAZIV.setWidth(30);
    carpostpp37.setResolver(dm.getQresolver());
    carpostpp37.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carpostpp37", null, true, Load.ALL));
    setColumns(new Column[] {carpostpp37LOCK, carpostpp37AKTIV, carpostpp37CPP37, carpostpp37NAZIV});
  }

  public void setall() {

    ddl.create("Carpostpp37")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cpp37", 10, true)
       .addChar("naziv", 100)
       .addPrimaryKey("cpp37");


    Naziv = "Carpostpp37";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cpp37"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
