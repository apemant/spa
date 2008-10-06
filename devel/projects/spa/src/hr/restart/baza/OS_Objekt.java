/****license*****************************************************************
**   file: OS_Objekt.java
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



public class OS_Objekt extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Objekt OS_Objektclass;

  QueryDataSet osob = new raDataSet();

  Column osobLOKK = new Column();
  Column osobAKTIV = new Column();
  Column osobCORG = new Column();
  Column osobCOBJEKT = new Column();
  Column osobNAZOBJEKT = new Column();

  public static OS_Objekt getDataModule() {
    if (OS_Objektclass == null) {
      OS_Objektclass = new OS_Objekt();
    }
    return OS_Objektclass;
  }

  public QueryDataSet getQueryDataSet() {
    return osob;
  }

  public OS_Objekt() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osobLOKK.setCaption("Status zauzetosti");
    osobLOKK.setColumnName("LOKK");
    osobLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osobLOKK.setPrecision(1);
    osobLOKK.setTableName("OS_OBJEKT");
    osobLOKK.setServerColumnName("LOKK");
    osobLOKK.setSqlType(1);
    osobLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osobLOKK.setDefault("N");
    osobAKTIV.setCaption("Aktivan - neaktivan");
    osobAKTIV.setColumnName("AKTIV");
    osobAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osobAKTIV.setPrecision(1);
    osobAKTIV.setTableName("OS_OBJEKT");
    osobAKTIV.setServerColumnName("AKTIV");
    osobAKTIV.setSqlType(1);
    osobAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osobAKTIV.setDefault("D");
    osobCORG.setCaption("OJ");
    osobCORG.setColumnName("CORG");
    osobCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    osobCORG.setPrecision(12);
    osobCORG.setRowId(true);
    osobCORG.setTableName("OS_OBJEKT");
    osobCORG.setServerColumnName("CORG");
    osobCORG.setSqlType(1);
    osobCORG.setWidth(7);
    osobCOBJEKT.setCaption("Šifra");
    osobCOBJEKT.setColumnName("COBJEKT");
    osobCOBJEKT.setDataType(com.borland.dx.dataset.Variant.STRING);
    osobCOBJEKT.setPrecision(6);
    osobCOBJEKT.setRowId(true);
    osobCOBJEKT.setTableName("OS_OBJEKT");
    osobCOBJEKT.setServerColumnName("COBJEKT");
    osobCOBJEKT.setSqlType(1);
    osobNAZOBJEKT.setCaption("Naziv");
    osobNAZOBJEKT.setColumnName("NAZOBJEKT");
    osobNAZOBJEKT.setDataType(com.borland.dx.dataset.Variant.STRING);
    osobNAZOBJEKT.setPrecision(50);
    osobNAZOBJEKT.setTableName("OS_OBJEKT");
    osobNAZOBJEKT.setServerColumnName("NAZOBJEKT");
    osobNAZOBJEKT.setSqlType(1);
    osobNAZOBJEKT.setWidth(30);
    osob.setResolver(dm.getQresolver());
    osob.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Objekt", null, true, Load.ALL));
 setColumns(new Column[] {osobLOKK, osobAKTIV, osobCORG, osobCOBJEKT, osobNAZOBJEKT});
  }

  public void setall() {

    ddl.create("OS_Objekt")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addChar("cobjekt", 6, true)
       .addChar("nazobjekt", 50)
       .addPrimaryKey("corg,cobjekt");


    Naziv = "OS_Objekt";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cobjekt"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
