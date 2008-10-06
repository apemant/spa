/****license*****************************************************************
**   file: Tablice.java
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



public class Tablice extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Tablice Tabliceclass;

  QueryDataSet tab = new QueryDataSet();

  Column tabLOKK = new Column();
  Column tabAKTIV = new Column();
  Column tabCTAB = new Column();
  Column tabIMETAB = new Column();
  Column tabKLASATAB = new Column();
  Column tabOPISTAB = new Column();
  Column tabAPP = new Column();

  public static Tablice getDataModule() {
    if (Tabliceclass == null) {
      Tabliceclass = new Tablice();
    }
    return Tabliceclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return tab;
  }

  public Tablice() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    tabLOKK.setCaption("Status zauzetosti");
    tabLOKK.setColumnName("LOKK");
    tabLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    tabLOKK.setPrecision(1);
    tabLOKK.setTableName("TABLICE");
    tabLOKK.setServerColumnName("LOKK");
    tabLOKK.setSqlType(1);
    tabLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    tabLOKK.setDefault("N");
    tabAKTIV.setCaption("Aktivan - neaktivan");
    tabAKTIV.setColumnName("AKTIV");
    tabAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    tabAKTIV.setPrecision(1);
    tabAKTIV.setTableName("TABLICE");
    tabAKTIV.setServerColumnName("AKTIV");
    tabAKTIV.setSqlType(1);
    tabAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    tabAKTIV.setDefault("D");
    tabCTAB.setCaption("Oznaka");
    tabCTAB.setColumnName("CTAB");
    tabCTAB.setDataType(com.borland.dx.dataset.Variant.INT);
    tabCTAB.setPrecision(10);
    tabCTAB.setRowId(true);
    tabCTAB.setTableName("TABLICE");
    tabCTAB.setServerColumnName("CTAB");
    tabCTAB.setSqlType(4);
    tabIMETAB.setCaption("Ime");
    tabIMETAB.setColumnName("IMETAB");
    tabIMETAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    tabIMETAB.setPrecision(20);
    tabIMETAB.setTableName("TABLICE");
    tabIMETAB.setServerColumnName("IMETAB");
    tabIMETAB.setSqlType(1);
    tabKLASATAB.setCaption("Klasa");
    tabKLASATAB.setColumnName("KLASATAB");
    tabKLASATAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    tabKLASATAB.setPrecision(50);
    tabKLASATAB.setTableName("TABLICE");
    tabKLASATAB.setServerColumnName("KLASATAB");
    tabKLASATAB.setSqlType(1);
    tabOPISTAB.setCaption("Opis");
    tabOPISTAB.setColumnName("OPISTAB");
    tabOPISTAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    tabOPISTAB.setPrecision(50);
    tabOPISTAB.setTableName("TABLICE");
    tabOPISTAB.setServerColumnName("OPISTAB");
    tabOPISTAB.setSqlType(1);
    tabAPP.setCaption("Aplikacija");
    tabAPP.setColumnName("APP");
    tabAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    tabAPP.setPrecision(10);
    tabAPP.setTableName("TABLICE");
    tabAPP.setServerColumnName("APP");
    tabAPP.setSqlType(1);
    tab.setResolver(dm.getQresolver());
    tab.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from Tablice", null, true, Load.ALL));
 setColumns(new Column[] {tabLOKK, tabAKTIV, tabCTAB, tabIMETAB, tabKLASATAB, tabOPISTAB, tabAPP});
  }

  public void setall() {

    ddl.create("Tablice")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("ctab", 6, true)
       .addChar("imetab", 20)
       .addChar("klasatab", 50)
       .addChar("opistab", 50)
       .addChar("app", 10)
       .addPrimaryKey("ctab");

    Naziv = "Tablice";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
