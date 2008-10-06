/****license*****************************************************************
**   file: Vrstaprometa.java
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



public class Vrstaprometa extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Vrstaprometa Vrstaprometaclass;

  QueryDataSet vrstaprometa = new QueryDataSet();

  Column vrstaprometaLOCK = new Column();
  Column vrstaprometaAKTIV = new Column();
  Column vrstaprometaCVRPROM = new Column();
  Column vrstaprometaNAZIV = new Column();

  public static Vrstaprometa getDataModule() {
    if (Vrstaprometaclass == null) {
      Vrstaprometaclass = new Vrstaprometa();
    }
    return Vrstaprometaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vrstaprometa;
  }

  public Vrstaprometa() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vrstaprometaLOCK.setCaption("Status zauzetosti");
    vrstaprometaLOCK.setColumnName("LOCK");
    vrstaprometaLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrstaprometaLOCK.setPrecision(1);
    vrstaprometaLOCK.setTableName("VRSTAPROMETA");
    vrstaprometaLOCK.setServerColumnName("LOCK");
    vrstaprometaLOCK.setSqlType(1);
    vrstaprometaLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrstaprometaLOCK.setDefault("N");
    vrstaprometaAKTIV.setCaption("Aktivan - neaktivan");
    vrstaprometaAKTIV.setColumnName("AKTIV");
    vrstaprometaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrstaprometaAKTIV.setPrecision(1);
    vrstaprometaAKTIV.setTableName("VRSTAPROMETA");
    vrstaprometaAKTIV.setServerColumnName("AKTIV");
    vrstaprometaAKTIV.setSqlType(1);
    vrstaprometaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrstaprometaAKTIV.setDefault("D");
    vrstaprometaCVRPROM.setCaption("Šifra vrste prometa");
    vrstaprometaCVRPROM.setColumnName("CVRPROM");
    vrstaprometaCVRPROM.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrstaprometaCVRPROM.setPrecision(10);
    vrstaprometaCVRPROM.setRowId(true);
    vrstaprometaCVRPROM.setTableName("VRSTAPROMETA");
    vrstaprometaCVRPROM.setServerColumnName("CVRPROM");
    vrstaprometaCVRPROM.setSqlType(1);
    vrstaprometaNAZIV.setCaption("Naziv");
    vrstaprometaNAZIV.setColumnName("NAZIV");
    vrstaprometaNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrstaprometaNAZIV.setPrecision(80);
    vrstaprometaNAZIV.setTableName("VRSTAPROMETA");
    vrstaprometaNAZIV.setServerColumnName("NAZIV");
    vrstaprometaNAZIV.setSqlType(1);
    vrstaprometaNAZIV.setWidth(30);
    vrstaprometa.setResolver(dm.getQresolver());
    vrstaprometa.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Vrstaprometa", null, true, Load.ALL));
    setColumns(new Column[] {vrstaprometaLOCK, vrstaprometaAKTIV, vrstaprometaCVRPROM, vrstaprometaNAZIV});
  }

  public void setall() {

    ddl.create("Vrstaprometa")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cvrprom", 10, true)
       .addChar("naziv", 80)
       .addPrimaryKey("cvrprom");


    Naziv = "Vrstaprometa";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cvrprom"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
