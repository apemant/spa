/****license*****************************************************************
**   file: Vrshemek.java
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



public class Vrshemek extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Vrshemek Vrshemekclass;

  QueryDataSet vrsk = new raDataSet();

  Column vrskLOKK = new Column();
  Column vrskAKTIV = new Column();
  Column vrskCVRSK = new Column();
  Column vrskOPISVRSK = new Column();
  Column vrskVRDOK = new Column();
  Column vrskAPP = new Column();

  public static Vrshemek getDataModule() {
    if (Vrshemekclass == null) {
      Vrshemekclass = new Vrshemek();
    }
    return Vrshemekclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vrsk;
  }

  public Vrshemek() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vrskLOKK.setCaption("Status zauzetosti");
    vrskLOKK.setColumnName("LOKK");
    vrskLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrskLOKK.setPrecision(1);
    vrskLOKK.setTableName("VRSHEMEK");
    vrskLOKK.setServerColumnName("LOKK");
    vrskLOKK.setSqlType(1);
    vrskLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrskLOKK.setDefault("N");
    vrskAKTIV.setCaption("Aktivan - neaktivan");
    vrskAKTIV.setColumnName("AKTIV");
    vrskAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrskAKTIV.setPrecision(1);
    vrskAKTIV.setTableName("VRSHEMEK");
    vrskAKTIV.setServerColumnName("AKTIV");
    vrskAKTIV.setSqlType(1);
    vrskAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrskAKTIV.setDefault("D");
    vrskCVRSK.setCaption("Oznaka");
    vrskCVRSK.setColumnName("CVRSK");
    vrskCVRSK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrskCVRSK.setPrecision(12);
    vrskCVRSK.setRowId(true);
    vrskCVRSK.setTableName("VRSHEMEK");
    vrskCVRSK.setServerColumnName("CVRSK");
    vrskCVRSK.setSqlType(1);
    vrskOPISVRSK.setCaption("Naziv");
    vrskOPISVRSK.setColumnName("OPISVRSK");
    vrskOPISVRSK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrskOPISVRSK.setPrecision(50);
    vrskOPISVRSK.setTableName("VRSHEMEK");
    vrskOPISVRSK.setServerColumnName("OPISVRSK");
    vrskOPISVRSK.setSqlType(1);
    vrskOPISVRSK.setWidth(30);
    vrskVRDOK.setCaption("VD");
    vrskVRDOK.setColumnName("VRDOK");
    vrskVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrskVRDOK.setPrecision(3);
    vrskVRDOK.setTableName("VRSHEMEK");
    vrskVRDOK.setServerColumnName("VRDOK");
    vrskVRDOK.setSqlType(1);
    vrskVRDOK.setDefault("");
    vrskAPP.setCaption("Aplikacija");
    vrskAPP.setColumnName("APP");
    vrskAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrskAPP.setPrecision(10);
    vrskAPP.setRowId(true);
    vrskAPP.setTableName("VRSHEMEK");
    vrskAPP.setServerColumnName("APP");
    vrskAPP.setSqlType(1);
    vrsk.setResolver(dm.getQresolver());
    vrsk.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Vrshemek", null, true, Load.ALL));
 setColumns(new Column[] {vrskLOKK, vrskAKTIV, vrskCVRSK, vrskOPISVRSK, vrskVRDOK, vrskAPP});
  }

  public void setall() {

    ddl.create("Vrshemek")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cvrsk", 12, true)
       .addChar("opisvrsk", 50)
       .addChar("vrdok", 3)
       .addChar("app", 10, true)
       .addPrimaryKey("cvrsk,app");


    Naziv = "Vrshemek";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cvrsk"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
