/****license*****************************************************************
**   file: Pravagrus.java
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

public class Pravagrus extends KreirDrop implements DataModule {

  private static Pravagrus pravagrusclass;
  dM dm  = dM.getDataModule();
  QueryDataSet pravagrus = new QueryDataSet();
  Column pravagrusCGRPUSR = new Column();
  Column pravagrusCPRAVA = new Column();
  Column pravagrusPOZ = new Column();

  public static Pravagrus getDataModule() {
    if (pravagrusclass == null) {
      pravagrusclass = new Pravagrus();
    }
    return pravagrusclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return pravagrus;
  }
  public Pravagrus() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setall(){

    ddl.create("pravagrus")
       .addChar("cgrupeusera", 5, true)
       .addShort("cprava", 4, true)
       .addChar("poz", 1, "D")
       .addPrimaryKey("cgrupeusera,cprava");

    Naziv="Pravagrus";

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
    pravagrusCPRAVA.setCaption("Šifra prava");
    pravagrusCPRAVA.setColumnName("CPRAVA");
    pravagrusCPRAVA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    pravagrusCPRAVA.setPrecision(4);
    pravagrusCPRAVA.setRowId(true);
    pravagrusCPRAVA.setTableName("PRAVAGRUS");
    pravagrusCPRAVA.setServerColumnName("CPRAVA");
    pravagrusCPRAVA.setSqlType(5);
    pravagrusCGRPUSR.setAlignment(com.borland.dx.text.Alignment.RIGHT | com.borland.dx.text.Alignment.MIDDLE);
    pravagrusCGRPUSR.setCaption("Šifra grupe");
    pravagrusCGRPUSR.setColumnName("CGRUPEUSERA");
    pravagrusCGRPUSR.setDataType(com.borland.dx.dataset.Variant.STRING);
    pravagrusCGRPUSR.setPrecision(5);
    pravagrusCGRPUSR.setRowId(true);
    pravagrusCGRPUSR.setTableName("PRAVAGRUS");
    pravagrusCGRPUSR.setServerColumnName("CGRUPEUSERA");
    pravagrusCGRPUSR.setSqlType(1);

    pravagrusPOZ.setCaption("Daje / oduzima");
    pravagrusPOZ.setColumnName("POZ");
    pravagrusPOZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    pravagrusPOZ.setPrecision(1);
    pravagrusPOZ.setTableName("PRAVAGRUS");
    pravagrusPOZ.setServerColumnName("POZ");
    pravagrusPOZ.setSqlType(1);
    pravagrusPOZ.setDefault("D");

    pravagrus.setResolver(dm.getQresolver());
    pravagrus.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from pravagrus", null, true, Load.ALL));
 setColumns(new Column[] {pravagrusCGRPUSR, pravagrusCPRAVA, pravagrusPOZ});
  }
}