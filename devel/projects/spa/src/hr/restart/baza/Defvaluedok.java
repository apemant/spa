/****license*****************************************************************
**   file: Defvaluedok.java
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



public class Defvaluedok extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Defvaluedok Defvaluedokclass;

  QueryDataSet dvd = new raDataSet();

  Column dvdVRDOK = new Column();
  Column dvdKLJUC = new Column();
  Column dvdDEFVALUE = new Column();

  public static Defvaluedok getDataModule() {
    if (Defvaluedokclass == null) {
      Defvaluedokclass = new Defvaluedok();
    }
    return Defvaluedokclass;
  }

  public QueryDataSet getQueryDataSet() {
    return dvd;
  }

  public Defvaluedok() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    dvdVRDOK.setCaption("Vrsta dokumenta");
    dvdVRDOK.setColumnName("VRDOK");
    dvdVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    dvdVRDOK.setPrecision(3);
    dvdVRDOK.setRowId(true);
    dvdVRDOK.setTableName("DEFVALUEDOK");
    dvdVRDOK.setServerColumnName("VRDOK");
    dvdVRDOK.setSqlType(1);
    dvdKLJUC.setCaption("Kolona");
    dvdKLJUC.setColumnName("KLJUC");
    dvdKLJUC.setDataType(com.borland.dx.dataset.Variant.STRING);
    dvdKLJUC.setPrecision(10);
    dvdKLJUC.setRowId(true);
    dvdKLJUC.setTableName("DEFVALUEDOK");
    dvdKLJUC.setServerColumnName("KLJUC");
    dvdKLJUC.setSqlType(1);
    dvdDEFVALUE.setCaption("Default vrijednost");
    dvdDEFVALUE.setColumnName("DEFVALUE");
    dvdDEFVALUE.setDataType(com.borland.dx.dataset.Variant.STRING);
    dvdDEFVALUE.setPrecision(20);
    dvdDEFVALUE.setTableName("DEFVALUEDOK");
    dvdDEFVALUE.setServerColumnName("DEFVALUE");
    dvdDEFVALUE.setSqlType(1);
    dvd.setResolver(dm.getQresolver());
    dvd.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Defvaluedok", null, true, Load.ALL));
 setColumns(new Column[] {dvdVRDOK, dvdKLJUC, dvdDEFVALUE});
  }

  public void setall() {

    ddl.create("Defvaluedok")
       .addChar("vrdok", 3, true)
       .addChar("kljuc", 10, true)
       .addChar("defvalue", 20)
       .addPrimaryKey("vrdok,kljuc");


    Naziv = "Defvaluedok";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
