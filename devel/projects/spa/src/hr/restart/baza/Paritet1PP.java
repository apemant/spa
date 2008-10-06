/****license*****************************************************************
**   file: Paritet1PP.java
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



public class Paritet1PP extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Paritet1PP Paritet1PPclass;

  QueryDataSet paritet1pp = new QueryDataSet();

  Column paritet1ppLOCK = new Column();
  Column paritet1ppAKTIV = new Column();
  Column paritet1ppCPAR1PP = new Column();
  Column paritet1ppNAZIV = new Column();

  public static Paritet1PP getDataModule() {
    if (Paritet1PPclass == null) {
      Paritet1PPclass = new Paritet1PP();
    }
    return Paritet1PPclass;
  }

  public QueryDataSet getQueryDataSet() {
    return paritet1pp;
  }

  public Paritet1PP() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    paritet1ppLOCK.setCaption("Status zauzetosti");
    paritet1ppLOCK.setColumnName("LOCK");
    paritet1ppLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    paritet1ppLOCK.setPrecision(1);
    paritet1ppLOCK.setTableName("PARITET1PP");
    paritet1ppLOCK.setServerColumnName("LOCK");
    paritet1ppLOCK.setSqlType(1);
    paritet1ppLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    paritet1ppLOCK.setDefault("N");
    paritet1ppAKTIV.setCaption("Aktivan - neaktivan");
    paritet1ppAKTIV.setColumnName("AKTIV");
    paritet1ppAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    paritet1ppAKTIV.setPrecision(1);
    paritet1ppAKTIV.setTableName("PARITET1PP");
    paritet1ppAKTIV.setServerColumnName("AKTIV");
    paritet1ppAKTIV.setSqlType(1);
    paritet1ppAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    paritet1ppAKTIV.setDefault("D");
    paritet1ppCPAR1PP.setCaption("Šifra pariteta za prvo podpolje");
    paritet1ppCPAR1PP.setColumnName("CPAR1PP");
    paritet1ppCPAR1PP.setDataType(com.borland.dx.dataset.Variant.STRING);
    paritet1ppCPAR1PP.setPrecision(10);
    paritet1ppCPAR1PP.setRowId(true);
    paritet1ppCPAR1PP.setTableName("PARITET1PP");
    paritet1ppCPAR1PP.setServerColumnName("CPAR1PP");
    paritet1ppCPAR1PP.setSqlType(1);
    paritet1ppNAZIV.setCaption("Naziv");
    paritet1ppNAZIV.setColumnName("NAZIV");
    paritet1ppNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    paritet1ppNAZIV.setPrecision(80);
    paritet1ppNAZIV.setTableName("PARITET1PP");
    paritet1ppNAZIV.setServerColumnName("NAZIV");
    paritet1ppNAZIV.setSqlType(1);
    paritet1ppNAZIV.setWidth(30);
    paritet1pp.setResolver(dm.getQresolver());
    paritet1pp.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Paritet1PP", null, true, Load.ALL));
    setColumns(new Column[] {paritet1ppLOCK, paritet1ppAKTIV, paritet1ppCPAR1PP, paritet1ppNAZIV});
  }

  public void setall() {

    ddl.create("Paritet1PP")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cpar1pp", 10, true)
       .addChar("naziv", 80)
       .addPrimaryKey("cpar1pp");


    Naziv = "Paritet1PP";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cpar1pp"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
