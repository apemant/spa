/****license*****************************************************************
**   file: Pravauser.java
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

public class Pravauser extends KreirDrop implements DataModule {

  private static Pravauser pravauserclass;
  dM dm  = dM.getDataModule();
  QueryDataSet pravauser = new QueryDataSet();
  Column pravauserCUSER = new Column();
  Column pravauserCPRAVA = new Column();
  Column pravauserPOZ = new Column();

  public static Pravauser getDataModule() {
    if (pravauserclass == null) {
      pravauserclass = new Pravauser();
    }
    return pravauserclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return pravauser;
  }
  public Pravauser() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setall(){

    ddl.create("pravauser")
       .addChar("cuser", 15, true)
       .addShort("cprava", 4, true)
       .addChar("poz", 1, "D")
       .addPrimaryKey("cuser,cprava");

    Naziv="Pravauser";

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
    pravauserCPRAVA.setCaption("Šifra prava");
    pravauserCPRAVA.setColumnName("CPRAVA");
    pravauserCPRAVA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    pravauserCPRAVA.setPrecision(4);
    pravauserCPRAVA.setRowId(true);
    pravauserCPRAVA.setTableName("PRAVAUSER");
    pravauserCPRAVA.setServerColumnName("CPRAVA");
    pravauserCPRAVA.setSqlType(5);
    pravauserCUSER.setAlignment(com.borland.dx.text.Alignment.RIGHT | com.borland.dx.text.Alignment.MIDDLE);
    pravauserCUSER.setCaption("Šifra operatera");
    pravauserCUSER.setColumnName("CUSER");
    pravauserCUSER.setDataType(com.borland.dx.dataset.Variant.STRING);
    pravauserCUSER.setPrecision(15);
    pravauserCUSER.setRowId(true);
    pravauserCUSER.setTableName("PRAVAUSER");
    pravauserCUSER.setServerColumnName("CUSER");
    pravauserCUSER.setSqlType(1);

    pravauserPOZ.setCaption("Daje / oduzima");
    pravauserPOZ.setColumnName("POZ");
    pravauserPOZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    pravauserPOZ.setPrecision(1);
    pravauserPOZ.setTableName("PRAVAUSER");
    pravauserPOZ.setServerColumnName("POZ");
    pravauserPOZ.setSqlType(1);
    pravauserPOZ.setDefault("D");


      pravauser.setResolver(dm.getQresolver());
    pravauser.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from pravauser\n", null, true, Load.ALL));
 setColumns(new Column[] {pravauserCUSER, pravauserCPRAVA, pravauserPOZ});
  }
}
