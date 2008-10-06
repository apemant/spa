/****license*****************************************************************
**   file: Grupeusera.java
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

public class Grupeusera extends KreirDrop implements DataModule {

  private static Grupeusera grupeuseraclass;
  dM dm  = dM.getDataModule();
  QueryDataSet grupeusera = new raDataSet();
  Column grpusrLOKK = new Column();
  Column grpusrAKTIV = new Column();
  Column grpusrCGRPUSR = new Column();
  Column grpusrNAZIV = new Column();
  Column grpusrOGRANICEN = new Column();

  public static Grupeusera getDataModule() {
    if (grupeuseraclass == null) {
      grupeuseraclass = new Grupeusera();
    }
    return grupeuseraclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return grupeusera;
  }
  public Grupeusera() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setall(){

   /*SqlDefTabela = "create table Grupeusera " +
        "(lokk char(1) CHARACTER SET WIN1250 default 'N', "   + // Status zauzetosti
        "aktiv char(1) CHARACTER SET WIN1250 default 'D', "   + // Aktivan-neaktivan
        "cgrupeusera char(5) CHARACTER SET WIN1250 not null, "+ // Šifra
        "naziv  char(30) CHARACTER SET WIN1250, " +             // Naziv korisnika
        "Primary Key (cgrupeusera))" ;
*/
    ddl.create("grupeusera")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cgrupeusera", 5, true)
       .addChar("naziv", 30)
       .addChar("ogranicen", 1, "N")
       .addPrimaryKey("cgrupeusera");

    Naziv="Grupeusera";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    DefIndex= new String[] {
              CommonTable.SqlDefIndex+"igrupeuseralokk on grupeusera (lokk)",
              CommonTable.SqlDefIndex+"igrupeuseraaktiv on grupeusera (aktiv)",
              CommonTable.SqlDefUniqueIndex+"igrupeuseracusera on grupeusera (cgrupeusera)"};

    NaziviIdx=new String[]{"iuserilokk","iuseriaktiv","iusericusera","iusericgrpu"};
*/
  }
  private void jbInit() throws Exception {
    grpusrOGRANICEN.setColumnName("OGRANICEN");
    grpusrOGRANICEN.setDataType(com.borland.dx.dataset.Variant.STRING);
    grpusrOGRANICEN.setDefault("N");
    grpusrOGRANICEN.setPrecision(1);
    grpusrOGRANICEN.setTableName("GRUPEUSERA");
//    grpusrOGRANICEN.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    grpusrOGRANICEN.setServerColumnName("OGRANICEN");
    grpusrOGRANICEN.setSqlType(1);

    grpusrNAZIV.setCaption("Naziv");
    grpusrNAZIV.setColumnName("NAZIV");
    grpusrNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    grpusrNAZIV.setPrecision(30);
    grpusrNAZIV.setTableName("GRUPEUSERA");
    grpusrNAZIV.setWidth(30);
    grpusrNAZIV.setServerColumnName("NAZIV");
    grpusrNAZIV.setSqlType(1);
    grpusrCGRPUSR.setCaption("Šifra");
    grpusrCGRPUSR.setColumnName("CGRUPEUSERA");
    grpusrCGRPUSR.setDataType(com.borland.dx.dataset.Variant.STRING);
    grpusrCGRPUSR.setPrecision(5);
    grpusrCGRPUSR.setRowId(true);
    grpusrCGRPUSR.setTableName("GRUPEUSERA");
    grpusrCGRPUSR.setServerColumnName("CGRUPEUSERA");
    grpusrCGRPUSR.setSqlType(1);
    grpusrAKTIV.setColumnName("AKTIV");
    grpusrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    grpusrAKTIV.setDefault("D");
    grpusrAKTIV.setPrecision(1);
    grpusrAKTIV.setTableName("GRUPEUSERA");
    grpusrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    grpusrAKTIV.setServerColumnName("AKTIV");
    grpusrAKTIV.setSqlType(1);
    grpusrAKTIV.setDefault("D");
    grpusrLOKK.setColumnName("LOKK");
    grpusrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    grpusrLOKK.setDefault("N");
    grpusrLOKK.setPrecision(1);
    grpusrLOKK.setTableName("GRUPEUSERA");
    grpusrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    grpusrLOKK.setServerColumnName("LOKK");
    grpusrLOKK.setSqlType(1);
    grpusrLOKK.setDefault("N");
      grupeusera.setResolver(dm.getQresolver());
    grupeusera.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from grupeusera", null, true, Load.ALL));
 setColumns(new Column[] {grpusrLOKK, grpusrAKTIV, grpusrCGRPUSR, grpusrNAZIV, grpusrOGRANICEN});
  }

}