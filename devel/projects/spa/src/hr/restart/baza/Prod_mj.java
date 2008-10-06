/****license*****************************************************************
**   file: Prod_mj.java
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

public class Prod_mj extends KreirDrop implements DataModule {

  private static Prod_mj prod_mjclass;
  dM dm  = dM.getDataModule();
  QueryDataSet prod_mj = new raDataSet();
  Column prod_mjLOKK = new Column();
  Column prod_mjAKTIV = new Column();
  Column prod_mjCPRODMJ = new Column();
  Column prod_mjNAZPRODMJ = new Column();
  Column prod_mjCSKL = new Column();

  public static Prod_mj getDataModule() {
    if (prod_mjclass == null) {
      prod_mjclass = new Prod_mj();
    }
    return prod_mjclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return prod_mj;
  }
  public Prod_mj() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void setall(){

    ddl.create("prod_mj")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cprodmj", 3, true)
       .addChar("nazprodmj", 50)
       .addChar("cskl", 12)
       .addPrimaryKey("cprodmj");

    Naziv="Prod_mj";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    DefIndex= new String[] {
              CommonTable.SqlDefIndex+"iprod_mjlokk on prod_mj (lokk)",
              CommonTable.SqlDefIndex+"iprod_mjaktiv on prod_mj (aktiv)",
              CommonTable.SqlDefUniqueIndex+"iprod_mjcusera on prod_mj (cuser)",
              CommonTable.SqlDefIndex+"iprod_mjcgrpu on prod_mj (cgrupeusera)"};

    NaziviIdx=new String[]{"iprod_mjlokk","iprod_mjaktiv","iprod_mjcusera","iprod_mjcgrpu"};
*/
  }
  private void jbInit() throws Exception {
    prod_mjCSKL.setCaption("Skladište");
    prod_mjCSKL.setColumnName("CSKL");
    prod_mjCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    prod_mjCSKL.setPrecision(12);
    prod_mjCSKL.setTableName("PROD_MJ");
    prod_mjCSKL.setServerColumnName("CSKL");
    prod_mjCSKL.setSqlType(1);
    prod_mjNAZPRODMJ.setCaption("Naziv");
    prod_mjNAZPRODMJ.setColumnName("NAZPRODMJ");
    prod_mjNAZPRODMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    prod_mjNAZPRODMJ.setPrecision(50);
    prod_mjNAZPRODMJ.setTableName("PROD_MJ");
    prod_mjNAZPRODMJ.setServerColumnName("NAZPRODMJ");
    prod_mjNAZPRODMJ.setSqlType(1);
    prod_mjCPRODMJ.setCaption("Šifra");
    prod_mjCPRODMJ.setColumnName("CPRODMJ");
    prod_mjCPRODMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    prod_mjCPRODMJ.setPrecision(3);
    prod_mjCPRODMJ.setRowId(true);
    prod_mjCPRODMJ.setTableName("PROD_MJ");
    prod_mjCPRODMJ.setServerColumnName("CPRODMJ");
    prod_mjCPRODMJ.setSqlType(1);
    prod_mjAKTIV.setColumnName("AKTIV");
    prod_mjAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    prod_mjAKTIV.setDefault("D");
    prod_mjAKTIV.setPrecision(1);
    prod_mjAKTIV.setTableName("PROD_MJ");
    prod_mjAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    prod_mjAKTIV.setServerColumnName("AKTIV");
    prod_mjAKTIV.setSqlType(1);
    prod_mjLOKK.setColumnName("LOKK");
    prod_mjLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    prod_mjLOKK.setDefault("N");
    prod_mjLOKK.setPrecision(1);
    prod_mjLOKK.setTableName("PROD_MJ");
    prod_mjLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    prod_mjLOKK.setServerColumnName("LOKK");
    prod_mjLOKK.setSqlType(1);
      prod_mj.setResolver(dm.getQresolver());
    prod_mj.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from prod_mj", null, true, Load.ALL));
 setColumns(new Column[] {prod_mjLOKK, prod_mjAKTIV, prod_mjCPRODMJ, prod_mjNAZPRODMJ, prod_mjCSKL});
  }

}