/****license*****************************************************************
**   file: Konta_par.java
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

public class Konta_par extends KreirDrop implements DataModule {

  private static Konta_par kparclass;
  dM dm  = dM.getDataModule();
  QueryDataSet konta_par = new raDataSet();
  Column kparDOB_TUZ = new Column();
  Column kparDOB_INO = new Column();
  Column kparKUP_TUZ = new Column();
  Column kparKUP_INO = new Column();

  public static Konta_par getDataModule() {
    if (kparclass == null) {
      kparclass = new Konta_par();
    }
    return kparclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return konta_par;
  }
  public Konta_par() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setall(){

    ddl.create("konta_par")
       .addChar("dob_tuz", 8, true)
       .addChar("dob_ino", 8, true)
       .addChar("kup_tuz", 8, true)
       .addChar("kup_ino", 8, true)
       .addPrimaryKey("dob_tuz,dob_ino,kup_tuz,kup_ino");

    Naziv="Konta_par";

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
    kparDOB_TUZ.setCaption("Dobavlja\u010D tuzemni");
    kparDOB_TUZ.setColumnName("DOB_TUZ");
    kparDOB_TUZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    kparDOB_TUZ.setPrecision(8);
    kparDOB_TUZ.setRowId(true);
    kparDOB_TUZ.setTableName("KONTA_PAR");
    kparDOB_TUZ.setServerColumnName("DOB_TUZ");
    kparDOB_TUZ.setSqlType(1);

    kparDOB_INO.setCaption("Dobavlja\u010D inozemni");
    kparDOB_INO.setColumnName("DOB_INO");
    kparDOB_INO.setDataType(com.borland.dx.dataset.Variant.STRING);
    kparDOB_INO.setPrecision(8);
    kparDOB_INO.setRowId(true);
    kparDOB_INO.setTableName("KONTA_PAR");
    kparDOB_INO.setServerColumnName("DOB_INO");
    kparDOB_INO.setSqlType(1);

    kparKUP_TUZ.setCaption("Kupac tuzemni");
    kparKUP_TUZ.setColumnName("KUP_TUZ");
    kparKUP_TUZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    kparKUP_TUZ.setPrecision(8);
    kparKUP_TUZ.setRowId(true);
    kparKUP_TUZ.setTableName("KONTA_PAR");
    kparKUP_TUZ.setServerColumnName("KUP_TUZ");
    kparKUP_TUZ.setSqlType(1);

    kparKUP_INO.setCaption("Kupac inozemni");
    kparKUP_INO.setColumnName("KUP_INO");
    kparKUP_INO.setDataType(com.borland.dx.dataset.Variant.STRING);
    kparKUP_INO.setPrecision(8);
    kparKUP_INO.setRowId(true);
    kparKUP_INO.setTableName("KONTA_PAR");
    kparKUP_INO.setServerColumnName("KUP_INO");
    kparKUP_INO.setSqlType(1);

      konta_par.setResolver(dm.getQresolver());
    konta_par.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from konta_par\n", null, true, Load.ALL));
 setColumns(new Column[] {kparDOB_TUZ, kparDOB_INO, kparKUP_TUZ, kparKUP_INO});
  }
}
