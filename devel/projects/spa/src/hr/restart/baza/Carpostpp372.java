/****license*****************************************************************
**   file: Carpostpp372.java
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



public class Carpostpp372 extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carpostpp372 Carpostpp372class;

  QueryDataSet carpostpp372 = new QueryDataSet();

  Column carpostpp372LOCK = new Column();
  Column carpostpp372AKTIV = new Column();
  Column carpostpp372CPP372 = new Column();
  Column carpostpp372NAZIV = new Column();

  public static Carpostpp372 getDataModule() {
    if (Carpostpp372class == null) {
      Carpostpp372class = new Carpostpp372();
    }
    return Carpostpp372class;
  }

  public QueryDataSet getQueryDataSet() {
    return carpostpp372;
  }

  public Carpostpp372() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carpostpp372LOCK.setCaption("Status zauzetosti");
    carpostpp372LOCK.setColumnName("LOCK");
    carpostpp372LOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpostpp372LOCK.setPrecision(1);
    carpostpp372LOCK.setTableName("CARPOSTPP372");
    carpostpp372LOCK.setServerColumnName("LOCK");
    carpostpp372LOCK.setSqlType(1);
    carpostpp372LOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carpostpp372LOCK.setDefault("N");
    carpostpp372AKTIV.setCaption("Aktivan - neaktivan");
    carpostpp372AKTIV.setColumnName("AKTIV");
    carpostpp372AKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpostpp372AKTIV.setPrecision(1);
    carpostpp372AKTIV.setTableName("CARPOSTPP372");
    carpostpp372AKTIV.setServerColumnName("AKTIV");
    carpostpp372AKTIV.setSqlType(1);
    carpostpp372AKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carpostpp372AKTIV.setDefault("D");
    carpostpp372CPP372.setCaption("Šifra carinskog postupka drugog pp37");
    carpostpp372CPP372.setColumnName("CPP372");
    carpostpp372CPP372.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpostpp372CPP372.setPrecision(10);
    carpostpp372CPP372.setRowId(true);
    carpostpp372CPP372.setTableName("CARPOSTPP372");
    carpostpp372CPP372.setServerColumnName("CPP372");
    carpostpp372CPP372.setSqlType(1);
    carpostpp372NAZIV.setCaption("Naziv");
    carpostpp372NAZIV.setColumnName("NAZIV");
    carpostpp372NAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpostpp372NAZIV.setPrecision(100);
    carpostpp372NAZIV.setTableName("CARPOSTPP372");
    carpostpp372NAZIV.setServerColumnName("NAZIV");
    carpostpp372NAZIV.setSqlType(1);
    carpostpp372NAZIV.setWidth(30);
    carpostpp372.setResolver(dm.getQresolver());
    carpostpp372.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carpostpp372", null, true, Load.ALL));
    setColumns(new Column[] {carpostpp372LOCK, carpostpp372AKTIV, carpostpp372CPP372, carpostpp372NAZIV});
  }

  public void setall() {

    ddl.create("Carpostpp372")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cpp372", 10, true)
       .addChar("naziv", 100)
       .addPrimaryKey("cpp372");


    Naziv = "Carpostpp372";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cpp372"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
