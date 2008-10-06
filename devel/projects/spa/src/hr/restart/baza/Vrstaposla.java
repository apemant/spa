/****license*****************************************************************
**   file: Vrstaposla.java
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



public class Vrstaposla extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Vrstaposla Vrstaposlaclass;

  QueryDataSet vrstaposla = new QueryDataSet();

  Column vrstaposlaLOCK = new Column();
  Column vrstaposlaAKTIV = new Column();
  Column vrstaposlaCVRPOS = new Column();
  Column vrstaposlaNAZIV = new Column();
  Column vrstaposlaCVRPOSPRIP = new Column();

  public static Vrstaposla getDataModule() {
    if (Vrstaposlaclass == null) {
      Vrstaposlaclass = new Vrstaposla();
    }
    return Vrstaposlaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vrstaposla;
  }

  public Vrstaposla() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vrstaposlaLOCK.setCaption("Status zauzetosti");
    vrstaposlaLOCK.setColumnName("LOCK");
    vrstaposlaLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrstaposlaLOCK.setPrecision(1);
    vrstaposlaLOCK.setTableName("VRSTAPOSLA");
    vrstaposlaLOCK.setServerColumnName("LOCK");
    vrstaposlaLOCK.setSqlType(1);
    vrstaposlaLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrstaposlaLOCK.setDefault("N");
    vrstaposlaAKTIV.setCaption("Aktivan - neaktivan");
    vrstaposlaAKTIV.setColumnName("AKTIV");
    vrstaposlaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrstaposlaAKTIV.setPrecision(1);
    vrstaposlaAKTIV.setTableName("VRSTAPOSLA");
    vrstaposlaAKTIV.setServerColumnName("AKTIV");
    vrstaposlaAKTIV.setSqlType(1);
    vrstaposlaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrstaposlaAKTIV.setDefault("D");
    vrstaposlaCVRPOS.setCaption("Šifra vrste posla");
    vrstaposlaCVRPOS.setColumnName("CVRPOS");
    vrstaposlaCVRPOS.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrstaposlaCVRPOS.setPrecision(10);
    vrstaposlaCVRPOS.setRowId(true);
    vrstaposlaCVRPOS.setTableName("VRSTAPOSLA");
    vrstaposlaCVRPOS.setServerColumnName("CVRPOS");
    vrstaposlaCVRPOS.setSqlType(1);
    vrstaposlaNAZIV.setCaption("Naziv");
    vrstaposlaNAZIV.setColumnName("NAZIV");
    vrstaposlaNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrstaposlaNAZIV.setPrecision(100);
    vrstaposlaNAZIV.setTableName("VRSTAPOSLA");
    vrstaposlaNAZIV.setServerColumnName("NAZIV");
    vrstaposlaNAZIV.setSqlType(1);
    vrstaposlaNAZIV.setWidth(30);
    vrstaposlaCVRPOSPRIP.setCaption("Pripadnost vrsti posla");
    vrstaposlaCVRPOSPRIP.setColumnName("CVRPOSPRIP");
    vrstaposlaCVRPOSPRIP.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrstaposlaCVRPOSPRIP.setPrecision(10);
    vrstaposlaCVRPOSPRIP.setTableName("VRSTAPOSLA");
    vrstaposlaCVRPOSPRIP.setServerColumnName("CVRPOSPRIP");
    vrstaposlaCVRPOSPRIP.setSqlType(1);
    vrstaposla.setResolver(dm.getQresolver());
    vrstaposla.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Vrstaposla", null, true, Load.ALL));
    setColumns(new Column[] {vrstaposlaLOCK, vrstaposlaAKTIV, vrstaposlaCVRPOS, vrstaposlaNAZIV, vrstaposlaCVRPOSPRIP});
  }

  public void setall() {

    ddl.create("Vrstaposla")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cvrpos", 10, true)
       .addChar("naziv", 100)
       .addChar("cvrposprip", 10)
       .addPrimaryKey("cvrpos");


    Naziv = "Vrstaposla";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cvrpos"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
