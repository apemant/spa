/****license*****************************************************************
**   file: Carnacpl.java
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



public class Carnacpl extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carnacpl Carnacplclass;

  QueryDataSet carnacpl = new QueryDataSet();

  Column carnacplLOCK = new Column();
  Column carnacplAKTIV = new Column();
  Column carnacplCCARNACPL = new Column();
  Column carnacplNAZIV = new Column();

  public static Carnacpl getDataModule() {
    if (Carnacplclass == null) {
      Carnacplclass = new Carnacpl();
    }
    return Carnacplclass;
  }

  public QueryDataSet getQueryDataSet() {
    return carnacpl;
  }

  public Carnacpl() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carnacplLOCK.setCaption("Status zauzetosti");
    carnacplLOCK.setColumnName("LOCK");
    carnacplLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carnacplLOCK.setPrecision(1);
    carnacplLOCK.setTableName("CARNACPL");
    carnacplLOCK.setServerColumnName("LOCK");
    carnacplLOCK.setSqlType(1);
    carnacplLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carnacplLOCK.setDefault("N");
    carnacplAKTIV.setCaption("Aktivan - neaktivan");
    carnacplAKTIV.setColumnName("AKTIV");
    carnacplAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carnacplAKTIV.setPrecision(1);
    carnacplAKTIV.setTableName("CARNACPL");
    carnacplAKTIV.setServerColumnName("AKTIV");
    carnacplAKTIV.setSqlType(1);
    carnacplAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carnacplAKTIV.setDefault("D");
    carnacplCCARNACPL.setCaption("Šifra na\u010Dina pla\u0107anja ili osiguranja pla\u0107anja");
    carnacplCCARNACPL.setColumnName("CCARNACPL");
    carnacplCCARNACPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    carnacplCCARNACPL.setPrecision(10);
    carnacplCCARNACPL.setRowId(true);
    carnacplCCARNACPL.setTableName("CARNACPL");
    carnacplCCARNACPL.setServerColumnName("CCARNACPL");
    carnacplCCARNACPL.setSqlType(1);
    carnacplNAZIV.setCaption("Naziv");
    carnacplNAZIV.setColumnName("NAZIV");
    carnacplNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carnacplNAZIV.setPrecision(80);
    carnacplNAZIV.setTableName("CARNACPL");
    carnacplNAZIV.setServerColumnName("NAZIV");
    carnacplNAZIV.setSqlType(1);
    carnacplNAZIV.setWidth(30);
    carnacpl.setResolver(dm.getQresolver());
    carnacpl.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carnacpl", null, true, Load.ALL));
    setColumns(new Column[] {carnacplLOCK, carnacplAKTIV, carnacplCCARNACPL, carnacplNAZIV});
  }

  public void setall() {

    ddl.create("Carnacpl")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("ccarnacpl", 10, true)
       .addChar("naziv", 80)
       .addPrimaryKey("ccarnacpl");


    Naziv = "Carnacpl";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"ccarnacpl"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
