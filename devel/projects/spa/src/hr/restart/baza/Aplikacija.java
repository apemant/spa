/****license*****************************************************************
**   file: Aplikacija.java
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

public class Aplikacija extends KreirDrop implements DataModule {

  private static Aplikacija aplclass;
  dM dm  = dM.getDataModule();
  QueryDataSet Aplikacija = new raDataSet();
  Column AplikacijaAPP = new Column();
  Column AplikacijaKLASA = new Column();
  Column AplikacijaInstalirana = new Column();
  Column AplikacijaOPIS = new Column();


  public static Aplikacija getDataModule() {
    if (aplclass == null) {
      aplclass = new Aplikacija();
    }
    return aplclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return Aplikacija;
  }

  public Aplikacija(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    AplikacijaOPIS.setCaption("Opis aplikacije");
    AplikacijaOPIS.setColumnName("OPIS");
    AplikacijaOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    AplikacijaOPIS.setPrecision(30);
    AplikacijaOPIS.setTableName("APLIKACIJA");
    AplikacijaOPIS.setSqlType(1);
    AplikacijaOPIS.setServerColumnName("OPIS");
    AplikacijaKLASA.setCaption("Klasa");
    AplikacijaKLASA.setColumnName("KLASA");
    AplikacijaKLASA.setDataType(com.borland.dx.dataset.Variant.STRING);
    AplikacijaKLASA.setPrecision(50);
    AplikacijaKLASA.setTableName("APLIKACIJA");
    AplikacijaKLASA.setSqlType(1);
    AplikacijaKLASA.setServerColumnName("KLASA");
    AplikacijaAPP.setCaption("Aplikacija");
    AplikacijaAPP.setColumnName("APP");
    AplikacijaAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    AplikacijaAPP.setPrecision(10);
    AplikacijaAPP.setRowId(true);
    AplikacijaAPP.setTableName("APLIKACIJA");
    AplikacijaAPP.setSqlType(1);
    AplikacijaAPP.setServerColumnName("APP");
    AplikacijaInstalirana.setCaption("Instalirana");
    AplikacijaInstalirana.setColumnName("INSTALIRANA");
    AplikacijaInstalirana.setDataType(com.borland.dx.dataset.Variant.STRING);
    AplikacijaInstalirana.setDefault("D");
    AplikacijaInstalirana.setPrecision(1);
    AplikacijaInstalirana.setTableName("APLIKACIJA");
    AplikacijaInstalirana.setSqlType(1);
    AplikacijaInstalirana.setServerColumnName("INSTALIRANA");
    Aplikacija.setResolver(dm.getQresolver());
    Aplikacija.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from Aplikacija", null, true, Load.ALL));
 setColumns(new Column[] {AplikacijaAPP, AplikacijaKLASA, AplikacijaInstalirana, AplikacijaOPIS});
  }

 public void setall(){

    /*SqlDefTabela = "create table Aplikacija " +
      "(app char(10) CHARACTER SET WIN1250 not null , " + //Status zauzetosti
      "klasa char(50) CHARACTER SET WIN1250 , " +
      "instalirana char(1)CHARACTER SET WIN1250 ," +
      "opis char(30)," +
      "Primary Key (app))" ;

    Naziv="Aplikacija";

    NaziviIdx=new String[]{"iAplikacijaKey"};

    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+NaziviIdx[0] +" on Aplikacija (app)" };*/

    ddl.create("aplikacija")
       .addChar("app", 10, true)
       .addChar("klasa", 50)
       .addChar("instalirana", 1)
       .addChar("opis", 30)
       .addPrimaryKey("app");

    Naziv = "Aplikacija";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[0];
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

  }
}