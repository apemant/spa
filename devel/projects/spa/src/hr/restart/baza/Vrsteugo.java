/****license*****************************************************************
**   file: Vrsteugo.java
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



public class Vrsteugo extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Vrsteugo Vrsteugoclass;

  QueryDataSet vrug = new raDataSet();

  Column vrugLOKK = new Column();
  Column vrugAKTIV = new Column();
  Column vrugCVRUGO = new Column();
  Column vrugOPIS = new Column();

  public static Vrsteugo getDataModule() {
    if (Vrsteugoclass == null) {
      Vrsteugoclass = new Vrsteugo();
    }
    return Vrsteugoclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vrug;
  }

  public Vrsteugo() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vrugLOKK.setCaption("Status zauzetosti");
    vrugLOKK.setColumnName("LOKK");
    vrugLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrugLOKK.setPrecision(1);
    vrugLOKK.setTableName("VRSTEUGO");
    vrugLOKK.setServerColumnName("LOKK");
    vrugLOKK.setSqlType(1);
    vrugLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrugLOKK.setDefault("N");
    vrugAKTIV.setCaption("Aktivan - neaktivan");
    vrugAKTIV.setColumnName("AKTIV");
    vrugAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrugAKTIV.setPrecision(1);
    vrugAKTIV.setTableName("VRSTEUGO");
    vrugAKTIV.setServerColumnName("AKTIV");
    vrugAKTIV.setSqlType(1);
    vrugAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrugAKTIV.setDefault("D");
    vrugCVRUGO.setCaption("Šifra");
    vrugCVRUGO.setColumnName("CVRUGO");
    vrugCVRUGO.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrugCVRUGO.setPrecision(13);
    vrugCVRUGO.setRowId(true);
    vrugCVRUGO.setTableName("VRSTEUGO");
    vrugCVRUGO.setServerColumnName("CVRUGO");
    vrugCVRUGO.setSqlType(1);
    vrugOPIS.setCaption("Opis");
    vrugOPIS.setColumnName("OPIS");
    vrugOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrugOPIS.setPrecision(50);
    vrugOPIS.setTableName("VRSTEUGO");
    vrugOPIS.setServerColumnName("OPIS");
    vrugOPIS.setSqlType(1);
    vrugOPIS.setWidth(30);
    vrug.setResolver(dm.getQresolver());
    vrug.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Vrsteugo", null, true, Load.ALL));
 setColumns(new Column[] {vrugLOKK, vrugAKTIV, vrugCVRUGO, vrugOPIS});
  }

  public void setall() {

    ddl.create("Vrsteugo")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cvrugo", 13, true)
       .addChar("opis", 50)
       .addPrimaryKey("cvrugo");


    Naziv = "Vrsteugo";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
