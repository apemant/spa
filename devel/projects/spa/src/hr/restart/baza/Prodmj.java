/****license*****************************************************************
**   file: Prodmj.java
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

public class Prodmj extends KreirDrop implements DataModule {

  private static Prodmj prodmjclass;
  dM dm  = dM.getDataModule();
  QueryDataSet prodmj = new raDataSet();
  Column prodmjLOKK = new Column();
  Column prodmjAKTIV = new Column();
  Column prodmjCPRODMJ = new Column();
  Column prodmjNPRODMJ = new Column();

  public static Prodmj getDataModule() {
    if (prodmjclass == null) {
      prodmjclass = new Prodmj();
    }
    return prodmjclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return prodmj;
  }
  public Prodmj() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void setall(){

    /*SqlDefTabela = "create table Prodmj " +
        "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + // Status zauzetosti
        "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
        "cprodmj char(2) CHARACTER SET WIN1250 not null, "+    // Šifra
        "nprodmj char(30) CHARACTER SET WIN1250, " +           // Naziv
        "Primary Key (cprodmj))" ; */

    ddl.create("prodmj")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cprodmj", 2, true)
       .addChar("nprodmj", 30)
       .addPrimaryKey("cprodmj");

    Naziv="Prodmj";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    DefIndex= new String[] {
              CommonTable.SqlDefIndex+"iprodmjlokk on Prodmj (lokk)",
              CommonTable.SqlDefIndex+"iprodmjaktiv on Prodmj (aktiv)",
              CommonTable.SqlDefUniqueIndex+"iprodmjcprodmj on Prodmj (cprodmj)"};

    NaziviIdx=new String[]{"iprodmjlokk","iprodmjaktiv","iprodmjcprodmj"};
*/
  }
  private void jbInit() throws Exception {
    prodmjNPRODMJ.setCaption("Naziv");
    prodmjNPRODMJ.setColumnName("NPRODMJ");
    prodmjNPRODMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    prodmjNPRODMJ.setPrecision(30);
    prodmjNPRODMJ.setTableName("PRODMJ");
    prodmjNPRODMJ.setServerColumnName("NPRODMJ");
    prodmjNPRODMJ.setSqlType(1);
    prodmjAKTIV.setColumnName("AKTIV");
    prodmjAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    prodmjAKTIV.setDefault("D");
    prodmjAKTIV.setPrecision(1);
    prodmjAKTIV.setTableName("PRODMJ");
    prodmjAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    prodmjAKTIV.setServerColumnName("AKTIV");
    prodmjAKTIV.setSqlType(1);
    prodmjLOKK.setColumnName("LOKK");
    prodmjLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    prodmjLOKK.setDefault("N");
    prodmjLOKK.setPrecision(1);
    prodmjLOKK.setTableName("PRODMJ");
    prodmjLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    prodmjLOKK.setServerColumnName("LOKK");
    prodmjLOKK.setSqlType(1);
    prodmjCPRODMJ.setCaption("Šifra");
    prodmjCPRODMJ.setColumnName("CPRODMJ");
    prodmjCPRODMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    prodmjCPRODMJ.setPrecision(2);
    prodmjCPRODMJ.setRowId(true);
    prodmjCPRODMJ.setTableName("PRODMJ");
    prodmjCPRODMJ.setServerColumnName("CPRODMJ");
    prodmjCPRODMJ.setSqlType(1);
      prodmj.setResolver(dm.getQresolver());
    prodmj.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from prodmj", null, true, Load.ALL));
 setColumns(new Column[] {prodmjLOKK, prodmjAKTIV, prodmjCPRODMJ, prodmjNPRODMJ});
  }



}