/****license*****************************************************************
**   file: Usersklad.java
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



public class Usersklad extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Usersklad Userskladclass;

  QueryDataSet uskl = new raDataSet();

  Column usklCUSER = new Column();
  Column usklCSKL = new Column();

  public static Usersklad getDataModule() {
    if (Userskladclass == null) {
      Userskladclass = new Usersklad();
    }
    return Userskladclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return uskl;
  }

  public Usersklad() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    usklCUSER.setCaption("Korisnik");
    usklCUSER.setColumnName("CUSER");
    usklCUSER.setDataType(com.borland.dx.dataset.Variant.STRING);
    usklCUSER.setPrecision(15);
    usklCUSER.setRowId(true);
    usklCUSER.setTableName("USERSKLAD");
    usklCUSER.setServerColumnName("CUSER");
    usklCUSER.setSqlType(1);
    usklCSKL.setCaption("Skladište");
    usklCSKL.setColumnName("CSKL");
    usklCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    usklCSKL.setPrecision(12);
    usklCSKL.setTableName("USERSKLAD");
    usklCSKL.setServerColumnName("CSKL");
    usklCSKL.setSqlType(1);
    uskl.setResolver(dm.getQresolver());
    uskl.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from Usersklad", null, true, Load.ALL));
 setColumns(new Column[] {usklCUSER, usklCSKL});
  }

  public void setall() {

    ddl.create("Usersklad")
       .addChar("cuser", 15, true)
       .addChar("cskl", 12)
       .addPrimaryKey("cuser");


    Naziv = "Usersklad";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
