/****license*****************************************************************
**   file: Useri.java
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

public class Useri extends KreirDrop implements DataModule {

  private static Useri usericlass;
  dM dm  = dM.getDataModule();
  QueryDataSet useri = new QueryDataSet();
  Column useriLOKK = new Column();
  Column useriAKTIV = new Column();
  Column useriCUSER = new Column();
  Column useriNAZIV = new Column();
  Column useriZAPORKA = new Column();
  Column useriCGRUSR = new Column();
  Column useriPARAM = new Column();
  Column useriSUPER = new Column();
  Column useriOGRANICEN = new Column();

  public static Useri getDataModule() {
    if (usericlass == null) {
      usericlass = new Useri();
    }
    return usericlass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return useri;
  }
  public Useri() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void setall(){

    /*SqlDefTabela = "create table Useri " +
        "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + // Status zauzetosti
        "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
        "cuser char(15) CHARACTER SET WIN1250 not null, "+     // Šifra
        "naziv  char(30) CHARACTER SET WIN1250, " +           // Naziv korisnika
        "zaporka char(15) CHARACTER SET WIN1250, " +          // Password
        "cgrupeusera char(5) CHARACTER SET WIN1250, "+        // Šifra grupe korisnika
        "param char(10) CHARACTER SET WIN1250, "+             // Dodatni parametar
        "Primary Key (cuser))" ; */

    ddl.create("useri")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cuser", 15, true)
       .addChar("naziv", 30)
       .addChar("zaporka", 15)
       .addChar("cgrupeusera", 5)
       .addChar("param", 10)
       .addChar("super", 1, "N")
       .addChar("ogranicen", 1, "G")
       .addPrimaryKey("cuser");

    Naziv="Useri";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cgrupeusera"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    DefIndex= new String[] {
              CommonTable.SqlDefIndex+"iuserilokk on useri (lokk)",
              CommonTable.SqlDefIndex+"iuseriaktiv on useri (aktiv)",
              CommonTable.SqlDefUniqueIndex+"iusericusera on useri (cuser)",
              CommonTable.SqlDefIndex+"iusericgrpu on useri (cgrupeusera)"};

    NaziviIdx=new String[]{"iuserilokk","iuseriaktiv","iusericusera","iusericgrpu"};
*/
  }
  private void jbInit() throws Exception {
    useriOGRANICEN.setColumnName("OGRANICEN");
    useriOGRANICEN.setDataType(com.borland.dx.dataset.Variant.STRING);
    useriOGRANICEN.setDefault("G");
    useriOGRANICEN.setPrecision(1);
    useriOGRANICEN.setTableName("USERI");
//    useriOGRANICEN.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    useriOGRANICEN.setServerColumnName("OGRANICEN");
    useriOGRANICEN.setSqlType(1);

    useriSUPER.setColumnName("SUPER");
    useriSUPER.setDataType(com.borland.dx.dataset.Variant.STRING);
    useriSUPER.setDefault("N");
    useriSUPER.setPrecision(1);
    useriSUPER.setTableName("USERI");
//    useriSUPER.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    useriSUPER.setServerColumnName("SUPER");
    useriSUPER.setSqlType(1);

    useriPARAM.setCaption("Parametri");
    useriPARAM.setColumnName("PARAM");
    useriPARAM.setDataType(com.borland.dx.dataset.Variant.STRING);
    useriPARAM.setPrecision(10);
    useriPARAM.setTableName("USERI");
    useriPARAM.setServerColumnName("PARAM");
    useriPARAM.setSqlType(1);
    useriCGRUSR.setCaption("Grupa");
    useriCGRUSR.setColumnName("CGRUPEUSERA");
    useriCGRUSR.setDataType(com.borland.dx.dataset.Variant.STRING);
    useriCGRUSR.setPrecision(5);
    useriCGRUSR.setTableName("USERI");
    useriCGRUSR.setServerColumnName("CGRUPEUSERA");
    useriCGRUSR.setSqlType(1);
    useriZAPORKA.setCaption("Zaporka");
    useriZAPORKA.setColumnName("ZAPORKA");
    useriZAPORKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    useriZAPORKA.setPrecision(15);
    useriZAPORKA.setTableName("USERI");
    useriZAPORKA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    useriZAPORKA.setServerColumnName("ZAPORKA");
    useriZAPORKA.setSqlType(1);
    useriNAZIV.setCaption("Naziv");
    useriNAZIV.setColumnName("NAZIV");
    useriNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    useriNAZIV.setPrecision(30);
    useriNAZIV.setTableName("USERI");
    useriNAZIV.setServerColumnName("NAZIV");
    useriNAZIV.setSqlType(1);
    useriCUSER.setCaption("Šifra");
    useriCUSER.setColumnName("CUSER");
    useriCUSER.setDataType(com.borland.dx.dataset.Variant.STRING);
    useriCUSER.setPrecision(15);
    useriCUSER.setRowId(true);
    useriCUSER.setTableName("USERI");
    useriCUSER.setServerColumnName("CUSER");
    useriCUSER.setSqlType(1);
    useriAKTIV.setColumnName("AKTIV");
    useriAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    useriAKTIV.setDefault("D");
    useriAKTIV.setPrecision(1);
    useriAKTIV.setTableName("USERI");
    useriAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    useriAKTIV.setServerColumnName("AKTIV");
    useriAKTIV.setSqlType(1);
    useriLOKK.setColumnName("LOKK");
    useriLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    useriLOKK.setDefault("N");
    useriLOKK.setPrecision(1);
    useriLOKK.setTableName("USERI");
    useriLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    useriLOKK.setServerColumnName("LOKK");
    useriLOKK.setSqlType(1);
      useri.setResolver(dm.getQresolver());
    useri.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from useri", null, true, Load.ALL));
 setColumns(new Column[] {useriLOKK, useriAKTIV, useriCUSER, useriNAZIV, useriZAPORKA, useriCGRUSR, useriPARAM, useriSUPER, useriOGRANICEN});
  }
}