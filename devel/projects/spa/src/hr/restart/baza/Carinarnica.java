/****license*****************************************************************
**   file: Carinarnica.java
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



public class Carinarnica extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carinarnica Carinarnicaclass;

  QueryDataSet carinarnica = new QueryDataSet();

  Column carinarnicaLOCK = new Column();
  Column carinarnicaAKTIV = new Column();
  Column carinarnicaCCAR = new Column();
  Column carinarnicaNAZIV = new Column();

  public static Carinarnica getDataModule() {
    if (Carinarnicaclass == null) {
      Carinarnicaclass = new Carinarnica();
    }
    return Carinarnicaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return carinarnica;
  }

  public Carinarnica() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carinarnicaLOCK.setCaption("Status zauzetosti");
    carinarnicaLOCK.setColumnName("LOCK");
    carinarnicaLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carinarnicaLOCK.setPrecision(1);
    carinarnicaLOCK.setTableName("CARINARNICA");
    carinarnicaLOCK.setServerColumnName("LOCK");
    carinarnicaLOCK.setSqlType(1);
    carinarnicaLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carinarnicaLOCK.setDefault("N");
    carinarnicaAKTIV.setCaption("Aktivan - neaktivan");
    carinarnicaAKTIV.setColumnName("AKTIV");
    carinarnicaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carinarnicaAKTIV.setPrecision(1);
    carinarnicaAKTIV.setTableName("CARINARNICA");
    carinarnicaAKTIV.setServerColumnName("AKTIV");
    carinarnicaAKTIV.setSqlType(1);
    carinarnicaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carinarnicaAKTIV.setDefault("D");
    carinarnicaCCAR.setCaption("Šifra carinarnice");
    carinarnicaCCAR.setColumnName("CCAR");
    carinarnicaCCAR.setDataType(com.borland.dx.dataset.Variant.STRING);
    carinarnicaCCAR.setPrecision(20);
    carinarnicaCCAR.setRowId(true);
    carinarnicaCCAR.setTableName("CARINARNICA");
    carinarnicaCCAR.setServerColumnName("CCAR");
    carinarnicaCCAR.setSqlType(1);
    carinarnicaNAZIV.setCaption("Naziv carinarnice");
    carinarnicaNAZIV.setColumnName("NAZIV");
    carinarnicaNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carinarnicaNAZIV.setPrecision(80);
    carinarnicaNAZIV.setTableName("CARINARNICA");
    carinarnicaNAZIV.setServerColumnName("NAZIV");
    carinarnicaNAZIV.setSqlType(1);
    carinarnicaNAZIV.setWidth(30);
    carinarnica.setResolver(dm.getQresolver());
    carinarnica.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carinarnica", null, true, Load.ALL));
    setColumns(new Column[] {carinarnicaLOCK, carinarnicaAKTIV, carinarnicaCCAR, carinarnicaNAZIV});
  }

  public void setall() {

    ddl.create("Carinarnica")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("ccar", 20, true)
       .addChar("naziv", 80)
       .addPrimaryKey("ccar");


    Naziv = "Carinarnica";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"ccar"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
