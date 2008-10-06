/****license*****************************************************************
**   file: Defshkonta.java
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



public class Defshkonta extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Defshkonta Defshkontaclass;

  QueryDataSet defshk = new raDataSet();

  Column defshkLOKK = new Column();
  Column defshkAKTIV = new Column();
  Column defshkVRDOK = new Column();
  Column defshkSTAVKA = new Column();
  Column defshkOPIS = new Column();
  Column defshkPOLJE = new Column();
  Column defshkBROJKONTA = new Column();
  Column defshkKARAKTERISTIKA = new Column();
  Column defshkAPP = new Column();
  Column defshkCKNJIGE = new Column();
  Column defshkCKOLONE = new Column();
  Column defshkSQLCONDITION = new Column();

  public static Defshkonta getDataModule() {
    if (Defshkontaclass == null) {
      Defshkontaclass = new Defshkonta();
    }
    return Defshkontaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return defshk;
  }

  public Defshkonta() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    defshkLOKK.setCaption("Status zauzetosti");
    defshkLOKK.setColumnName("LOKK");
    defshkLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    defshkLOKK.setPrecision(1);
    defshkLOKK.setTableName("DEFSHKONTA");
    defshkLOKK.setServerColumnName("LOKK");
    defshkLOKK.setSqlType(1);
    defshkLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    defshkLOKK.setDefault("N");
    defshkAKTIV.setCaption("Aktivan - neaktivan");
    defshkAKTIV.setColumnName("AKTIV");
    defshkAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    defshkAKTIV.setPrecision(1);
    defshkAKTIV.setTableName("DEFSHKONTA");
    defshkAKTIV.setServerColumnName("AKTIV");
    defshkAKTIV.setSqlType(1);
    defshkAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    defshkAKTIV.setDefault("D");
    defshkVRDOK.setCaption("Vrsta dokumenta");
    defshkVRDOK.setColumnName("VRDOK");
    defshkVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    defshkVRDOK.setPrecision(3);
    defshkVRDOK.setRowId(true);
    defshkVRDOK.setTableName("DEFSHKONTA");
    defshkVRDOK.setServerColumnName("VRDOK");
    defshkVRDOK.setSqlType(1);
    defshkSTAVKA.setCaption("RBS");
    defshkSTAVKA.setColumnName("STAVKA");
    defshkSTAVKA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    defshkSTAVKA.setPrecision(2);
    defshkSTAVKA.setRowId(true);
    defshkSTAVKA.setTableName("DEFSHKONTA");
    defshkSTAVKA.setServerColumnName("STAVKA");
    defshkSTAVKA.setSqlType(5);
    defshkSTAVKA.setWidth(2);
    defshkOPIS.setCaption("Opis");
    defshkOPIS.setColumnName("OPIS");
    defshkOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    defshkOPIS.setPrecision(40);
    defshkOPIS.setTableName("DEFSHKONTA");
    defshkOPIS.setServerColumnName("OPIS");
    defshkOPIS.setSqlType(1);
    defshkOPIS.setWidth(30);
    defshkPOLJE.setCaption("Polje");
    defshkPOLJE.setColumnName("POLJE");
    defshkPOLJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    defshkPOLJE.setPrecision(50);
    defshkPOLJE.setTableName("DEFSHKONTA");
    defshkPOLJE.setServerColumnName("POLJE");
    defshkPOLJE.setSqlType(1);
    defshkBROJKONTA.setCaption("Konto");
    defshkBROJKONTA.setColumnName("BROJKONTA");
    defshkBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    defshkBROJKONTA.setPrecision(8);
    defshkBROJKONTA.setTableName("DEFSHKONTA");
    defshkBROJKONTA.setServerColumnName("BROJKONTA");
    defshkBROJKONTA.setSqlType(1);
    defshkKARAKTERISTIKA.setCaption("Karakteristika");
    defshkKARAKTERISTIKA.setColumnName("KARAKTERISTIKA");
    defshkKARAKTERISTIKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    defshkKARAKTERISTIKA.setPrecision(1);
    defshkKARAKTERISTIKA.setTableName("DEFSHKONTA");
    defshkKARAKTERISTIKA.setServerColumnName("KARAKTERISTIKA");
    defshkKARAKTERISTIKA.setSqlType(1);
    defshkAPP.setCaption("Aplikacija");
    defshkAPP.setColumnName("APP");
    defshkAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    defshkAPP.setPrecision(10);
    defshkAPP.setTableName("DEFSHKONTA");
    defshkAPP.setServerColumnName("APP");
    defshkAPP.setSqlType(1);
    defshkCKNJIGE.setCaption("Knjiga");
    defshkCKNJIGE.setColumnName("CKNJIGE");
    defshkCKNJIGE.setDataType(com.borland.dx.dataset.Variant.STRING);
    defshkCKNJIGE.setPrecision(5);
    defshkCKNJIGE.setTableName("DEFSHKONTA");
    defshkCKNJIGE.setServerColumnName("CKNJIGE");
    defshkCKNJIGE.setSqlType(1);
    defshkCKOLONE.setCaption("Kolona");
    defshkCKOLONE.setColumnName("CKOLONE");
    defshkCKOLONE.setDataType(com.borland.dx.dataset.Variant.SHORT);
    defshkCKOLONE.setPrecision(2);
    defshkCKOLONE.setTableName("DEFSHKONTA");
    defshkCKOLONE.setServerColumnName("CKOLONE");
    defshkCKOLONE.setSqlType(5);
    defshkCKOLONE.setWidth(2);
    defshkSQLCONDITION.setCaption("SQL upit na bazu");
    defshkSQLCONDITION.setColumnName("SQLCONDITION");
    defshkSQLCONDITION.setDataType(com.borland.dx.dataset.Variant.STRING);
    defshkSQLCONDITION.setPrecision(3072);
    defshkSQLCONDITION.setTableName("SHKONTA");
    defshkSQLCONDITION.setServerColumnName("SQLCONDITION");
    defshkSQLCONDITION.setSqlType(1);
    defshkSQLCONDITION.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    defshkSQLCONDITION.setWidth(300);    
    defshk.setResolver(dm.getQresolver());
    defshk.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Defshkonta", null, true, Load.ALL));
 setColumns(new Column[] {defshkLOKK, defshkAKTIV, defshkVRDOK, defshkSTAVKA, defshkOPIS, defshkPOLJE, defshkBROJKONTA, defshkKARAKTERISTIKA,
        defshkAPP, defshkCKNJIGE, defshkCKOLONE,defshkSQLCONDITION});
  }

  public void setall() {

    ddl.create("Defshkonta")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("vrdok", 3, true)
       .addShort("stavka", 2, true)
       .addChar("opis", 40)
       .addChar("polje", 50)
       .addChar("brojkonta", 8)
       .addChar("karakteristika", 1)
       .addChar("app", 10)
       .addChar("cknjige", 5)
       .addShort("ckolone", 2)
	   .addChar("sqlcondition", 3072)
       .addPrimaryKey("vrdok,stavka");


    Naziv = "Defshkonta";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
