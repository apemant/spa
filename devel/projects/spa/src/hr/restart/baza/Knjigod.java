/****license*****************************************************************
**   file: Knjigod.java
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



public class Knjigod extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Knjigod Knjigodclass;

  QueryDataSet kg = new raDataSet();

  Column kgLOKK = new Column();
  Column kgAKTIV = new Column();
  Column kgCORG = new Column();
  Column kgAPP = new Column();
  Column kgGOD = new Column();
  Column kgSTATRADA = new Column();

  public static Knjigod getDataModule() {
    if (Knjigodclass == null) {
      Knjigodclass = new Knjigod();
    }
    return Knjigodclass;
  }

  public QueryDataSet getQueryDataSet() {
    return kg;
  }

  public Knjigod() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    kgLOKK.setCaption("Status zauzetosti");
    kgLOKK.setColumnName("LOKK");
    kgLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kgLOKK.setPrecision(1);
    kgLOKK.setTableName("KNJIGOD");
    kgLOKK.setServerColumnName("LOKK");
    kgLOKK.setSqlType(1);
    kgLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kgLOKK.setDefault("N");
    kgAKTIV.setCaption("Aktivan - neaktivan");
    kgAKTIV.setColumnName("AKTIV");
    kgAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    kgAKTIV.setPrecision(1);
    kgAKTIV.setTableName("KNJIGOD");
    kgAKTIV.setServerColumnName("AKTIV");
    kgAKTIV.setSqlType(1);
    kgAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kgAKTIV.setDefault("D");
    kgCORG.setCaption("Knjigovodstvo");
    kgCORG.setColumnName("CORG");
    kgCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    kgCORG.setPrecision(12);
    kgCORG.setRowId(true);
    kgCORG.setTableName("KNJIGOD");
    kgCORG.setServerColumnName("CORG");
    kgCORG.setSqlType(1);
    kgAPP.setCaption("Aplikacija");
    kgAPP.setColumnName("APP");
    kgAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    kgAPP.setPrecision(10);
    kgAPP.setRowId(true);
    kgAPP.setTableName("KNJIGOD");
    kgAPP.setServerColumnName("APP");
    kgAPP.setSqlType(1);
    kgGOD.setCaption("Godina");
    kgGOD.setColumnName("GOD");
    kgGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    kgGOD.setPrecision(4);
    kgGOD.setRowId(true);
    kgGOD.setTableName("KNJIGOD");
    kgGOD.setServerColumnName("GOD");
    kgGOD.setSqlType(1);
    kgSTATRADA.setCaption("Status");
    kgSTATRADA.setColumnName("STATRADA");
    kgSTATRADA.setDataType(com.borland.dx.dataset.Variant.STRING);
    kgSTATRADA.setPrecision(1);
    kgSTATRADA.setTableName("KNJIGOD");
    kgSTATRADA.setServerColumnName("STATRADA");
    kgSTATRADA.setSqlType(1);
    kgSTATRADA.setDefault("N");
    kg.setResolver(dm.getQresolver());
    kg.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Knjigod", null, true, Load.ALL));
    setColumns(new Column[] {kgLOKK, kgAKTIV, kgCORG, kgAPP, kgGOD, kgSTATRADA});
  }

  public void setall() {

    ddl.create("Knjigod")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addChar("app", 10, true)
       .addChar("god", 4, true)
       .addChar("statrada", 1, "N")
       .addPrimaryKey("corg,app,god");


    Naziv = "Knjigod";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
