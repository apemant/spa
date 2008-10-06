/****license*****************************************************************
**   file: Paritet3PP.java
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



public class Paritet3PP extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Paritet3PP Paritet3PPclass;

  QueryDataSet paritet3pp = new QueryDataSet();

  Column paritet3ppLOCK = new Column();
  Column paritet3ppAKTIV = new Column();
  Column paritet3ppCPAR3PP = new Column();
  Column paritet3ppNAZIV = new Column();

  public static Paritet3PP getDataModule() {
    if (Paritet3PPclass == null) {
      Paritet3PPclass = new Paritet3PP();
    }
    return Paritet3PPclass;
  }

  public QueryDataSet getQueryDataSet() {
    return paritet3pp;
  }

  public Paritet3PP() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    paritet3ppLOCK.setCaption("Status zauzetosti");
    paritet3ppLOCK.setColumnName("LOCK");
    paritet3ppLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    paritet3ppLOCK.setPrecision(1);
    paritet3ppLOCK.setTableName("PARITET3PP");
    paritet3ppLOCK.setServerColumnName("LOCK");
    paritet3ppLOCK.setSqlType(1);
    paritet3ppLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    paritet3ppLOCK.setDefault("N");
    paritet3ppAKTIV.setCaption("Aktivan - neaktivan");
    paritet3ppAKTIV.setColumnName("AKTIV");
    paritet3ppAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    paritet3ppAKTIV.setPrecision(1);
    paritet3ppAKTIV.setTableName("PARITET3PP");
    paritet3ppAKTIV.setServerColumnName("AKTIV");
    paritet3ppAKTIV.setSqlType(1);
    paritet3ppAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    paritet3ppAKTIV.setDefault("D");
    paritet3ppCPAR3PP.setCaption("Šifra pariteta za tre\u0107e podpolje");
    paritet3ppCPAR3PP.setColumnName("CPAR3PP");
    paritet3ppCPAR3PP.setDataType(com.borland.dx.dataset.Variant.STRING);
    paritet3ppCPAR3PP.setPrecision(10);
    paritet3ppCPAR3PP.setRowId(true);
    paritet3ppCPAR3PP.setTableName("PARITET3PP");
    paritet3ppCPAR3PP.setServerColumnName("CPAR3PP");
    paritet3ppCPAR3PP.setSqlType(1);
    paritet3ppNAZIV.setCaption("Naziv");
    paritet3ppNAZIV.setColumnName("NAZIV");
    paritet3ppNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    paritet3ppNAZIV.setPrecision(80);
    paritet3ppNAZIV.setTableName("PARITET3PP");
    paritet3ppNAZIV.setServerColumnName("NAZIV");
    paritet3ppNAZIV.setSqlType(1);
    paritet3ppNAZIV.setWidth(30);
    paritet3pp.setResolver(dm.getQresolver());
    paritet3pp.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Paritet3PP", null, true, Load.ALL));
    setColumns(new Column[] {paritet3ppLOCK, paritet3ppAKTIV, paritet3ppCPAR3PP, paritet3ppNAZIV});
  }

  public void setall() {

    ddl.create("Paritet3PP")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cpar3pp", 10, true)
       .addChar("naziv", 80)
       .addPrimaryKey("cpar3pp");


    Naziv = "Paritet3PP";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cpar3pp"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
