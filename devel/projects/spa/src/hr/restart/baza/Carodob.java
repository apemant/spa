/****license*****************************************************************
**   file: Carodob.java
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



public class Carodob extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carodob Carodobclass;

  QueryDataSet carodob = new QueryDataSet();

  Column carodobLOCK = new Column();
  Column carodobAKTIV = new Column();
  Column carodobCODOB = new Column();
  Column carodobNAZIV = new Column();

  public static Carodob getDataModule() {
    if (Carodobclass == null) {
      Carodobclass = new Carodob();
    }
    return Carodobclass;
  }

  public QueryDataSet getQueryDataSet() {
    return carodob;
  }

  public Carodob() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carodobLOCK.setCaption("Status zauzetosti");
    carodobLOCK.setColumnName("LOCK");
    carodobLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carodobLOCK.setPrecision(1);
    carodobLOCK.setTableName("CARODOB");
    carodobLOCK.setServerColumnName("LOCK");
    carodobLOCK.setSqlType(1);
    carodobLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carodobLOCK.setDefault("N");
    carodobAKTIV.setCaption("Aktivan - neaktivan");
    carodobAKTIV.setColumnName("AKTIV");
    carodobAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carodobAKTIV.setPrecision(1);
    carodobAKTIV.setTableName("CARODOB");
    carodobAKTIV.setServerColumnName("AKTIV");
    carodobAKTIV.setSqlType(1);
    carodobAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carodobAKTIV.setDefault("D");
    carodobCODOB.setCaption("Šifra carinskog odobrenja");
    carodobCODOB.setColumnName("CODOB");
    carodobCODOB.setDataType(com.borland.dx.dataset.Variant.STRING);
    carodobCODOB.setPrecision(10);
    carodobCODOB.setRowId(true);
    carodobCODOB.setTableName("CARODOB");
    carodobCODOB.setServerColumnName("CODOB");
    carodobCODOB.setSqlType(1);
    carodobNAZIV.setCaption("Naziv");
    carodobNAZIV.setColumnName("NAZIV");
    carodobNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carodobNAZIV.setPrecision(80);
    carodobNAZIV.setTableName("CARODOB");
    carodobNAZIV.setServerColumnName("NAZIV");
    carodobNAZIV.setSqlType(1);
    carodobNAZIV.setWidth(30);
    carodob.setResolver(dm.getQresolver());
    carodob.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carodob", null, true, Load.ALL));
    setColumns(new Column[] {carodobLOCK, carodobAKTIV, carodobCODOB, carodobNAZIV});
  }

  public void setall() {

    ddl.create("Carodob")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("codob", 10, true)
       .addChar("naziv", 80)
       .addPrimaryKey("codob");


    Naziv = "Carodob";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"codob"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
