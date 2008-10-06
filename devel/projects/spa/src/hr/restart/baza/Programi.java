/****license*****************************************************************
**   file: Programi.java
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



public class Programi extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Programi Programiclass;

  QueryDataSet prog = new raDataSet();

  Column progLOKK = new Column();
  Column progAKTIV = new Column();
  Column progCPROG = new Column();
  Column progOPISPROG = new Column();
  Column progAPP = new Column();

  public static Programi getDataModule() {
    if (Programiclass == null) {
      Programiclass = new Programi();
    }
    return Programiclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return prog;
  }

  public Programi() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    progLOKK.setCaption("Status zauzetosti");
    progLOKK.setColumnName("LOKK");
    progLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    progLOKK.setPrecision(1);
    progLOKK.setTableName("PROGRAMI");
    progLOKK.setServerColumnName("LOKK");
    progLOKK.setSqlType(1);
    progLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    progLOKK.setDefault("N");
    progAKTIV.setCaption("Aktivan - neaktivan");
    progAKTIV.setColumnName("AKTIV");
    progAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    progAKTIV.setPrecision(1);
    progAKTIV.setTableName("PROGRAMI");
    progAKTIV.setServerColumnName("AKTIV");
    progAKTIV.setSqlType(1);
    progAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    progAKTIV.setDefault("D");
    progCPROG.setCaption("Oznaka");
    progCPROG.setColumnName("CPROG");
    progCPROG.setDataType(com.borland.dx.dataset.Variant.STRING);
    progCPROG.setPrecision(20);
    progCPROG.setRowId(true);
    progCPROG.setTableName("PROGRAMI");
    progCPROG.setServerColumnName("CPROG");
    progCPROG.setSqlType(1);
    progOPISPROG.setCaption("Naziv");
    progOPISPROG.setColumnName("OPISPROG");
    progOPISPROG.setDataType(com.borland.dx.dataset.Variant.STRING);
    progOPISPROG.setPrecision(50);
    progOPISPROG.setTableName("PROGRAMI");
    progOPISPROG.setServerColumnName("OPISPROG");
    progOPISPROG.setSqlType(1);
    progAPP.setCaption("Aplikacija");
    progAPP.setColumnName("APP");
    progAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    progAPP.setPrecision(10);
    progAPP.setTableName("PROGRAMI");
    progAPP.setServerColumnName("APP");
    progAPP.setRowId(true);
    progAPP.setSqlType(1);
    prog.setResolver(dm.getQresolver());
    prog.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from Programi", null, true, Load.ALL));
 setColumns(new Column[] {progLOKK, progAKTIV, progCPROG, progOPISPROG, progAPP});
  }

  public void setall() {

    ddl.create("Programi")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cprog", 20, true)
       .addChar("opisprog", 50)
       .addChar("app", 10, true)
       .addPrimaryKey("cprog,app");


    Naziv = "Programi";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cprog"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
