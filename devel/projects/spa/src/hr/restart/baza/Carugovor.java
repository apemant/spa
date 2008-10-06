/****license*****************************************************************
**   file: Carugovor.java
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



public class Carugovor extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carugovor Carugovorclass;

  QueryDataSet carugovor = new QueryDataSet();

  Column carugovorLOCK = new Column();
  Column carugovorAKTIV = new Column();
  Column carugovorCUGOVOR = new Column();
  Column carugovorNAZIV = new Column();

  public static Carugovor getDataModule() {
    if (Carugovorclass == null) {
      Carugovorclass = new Carugovor();
    }
    return Carugovorclass;
  }

  public QueryDataSet getQueryDataSet() {
    return carugovor;
  }

  public Carugovor() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carugovorLOCK.setCaption("Status zauzetosti");
    carugovorLOCK.setColumnName("LOCK");
    carugovorLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carugovorLOCK.setPrecision(1);
    carugovorLOCK.setTableName("CARUGOVOR");
    carugovorLOCK.setServerColumnName("LOCK");
    carugovorLOCK.setSqlType(1);
    carugovorLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carugovorLOCK.setDefault("N");
    carugovorAKTIV.setCaption("Aktivan - neaktivan");
    carugovorAKTIV.setColumnName("AKTIV");
    carugovorAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carugovorAKTIV.setPrecision(1);
    carugovorAKTIV.setTableName("CARUGOVOR");
    carugovorAKTIV.setServerColumnName("AKTIV");
    carugovorAKTIV.setSqlType(1);
    carugovorAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carugovorAKTIV.setDefault("D");
    carugovorCUGOVOR.setCaption("Šifra ugovora");
    carugovorCUGOVOR.setColumnName("CUGOVOR");
    carugovorCUGOVOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    carugovorCUGOVOR.setPrecision(4);
    carugovorCUGOVOR.setRowId(true);
    carugovorCUGOVOR.setTableName("CARUGOVOR");
    carugovorCUGOVOR.setServerColumnName("CUGOVOR");
    carugovorCUGOVOR.setSqlType(1);
    carugovorNAZIV.setCaption("Naziv");
    carugovorNAZIV.setColumnName("NAZIV");
    carugovorNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carugovorNAZIV.setPrecision(80);
    carugovorNAZIV.setTableName("CARUGOVOR");
    carugovorNAZIV.setServerColumnName("NAZIV");
    carugovorNAZIV.setSqlType(1);
    carugovorNAZIV.setWidth(30);
    carugovor.setResolver(dm.getQresolver());
    carugovor.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carugovor", null, true, Load.ALL));
    setColumns(new Column[] {carugovorLOCK, carugovorAKTIV, carugovorCUGOVOR, carugovorNAZIV});
  }

  public void setall() {

    ddl.create("Carugovor")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cugovor", 4, true)
       .addChar("naziv", 80)
       .addPrimaryKey("cugovor");


    Naziv = "Carugovor";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cugovor"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
