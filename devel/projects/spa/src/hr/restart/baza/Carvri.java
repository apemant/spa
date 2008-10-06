/****license*****************************************************************
**   file: Carvri.java
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



public class Carvri extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carvri Carvriclass;

  QueryDataSet carvri = new QueryDataSet();

  Column carvriLOCK = new Column();
  Column carvriAKTIV = new Column();
  Column carvriCVRI = new Column();
  Column carvriNAZIV = new Column();

  public static Carvri getDataModule() {
    if (Carvriclass == null) {
      Carvriclass = new Carvri();
    }
    return Carvriclass;
  }

  public QueryDataSet getQueryDataSet() {
    return carvri;
  }

  public Carvri() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carvriLOCK.setCaption("Status zauzetosti");
    carvriLOCK.setColumnName("LOCK");
    carvriLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carvriLOCK.setPrecision(1);
    carvriLOCK.setTableName("CARVRI");
    carvriLOCK.setServerColumnName("LOCK");
    carvriLOCK.setSqlType(1);
    carvriLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carvriLOCK.setDefault("N");
    carvriAKTIV.setCaption("Aktivan - neaktivan");
    carvriAKTIV.setColumnName("AKTIV");
    carvriAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carvriAKTIV.setPrecision(1);
    carvriAKTIV.setTableName("CARVRI");
    carvriAKTIV.setServerColumnName("AKTIV");
    carvriAKTIV.setSqlType(1);
    carvriAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carvriAKTIV.setDefault("D");
    carvriCVRI.setCaption("Šifra vrijednosti");
    carvriCVRI.setColumnName("CVRI");
    carvriCVRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    carvriCVRI.setPrecision(10);
    carvriCVRI.setRowId(true);
    carvriCVRI.setTableName("CARVRI");
    carvriCVRI.setServerColumnName("CVRI");
    carvriCVRI.setSqlType(1);
    carvriNAZIV.setCaption("Naziv");
    carvriNAZIV.setColumnName("NAZIV");
    carvriNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carvriNAZIV.setPrecision(80);
    carvriNAZIV.setTableName("CARVRI");
    carvriNAZIV.setServerColumnName("NAZIV");
    carvriNAZIV.setSqlType(1);
    carvriNAZIV.setWidth(30);
    carvri.setResolver(dm.getQresolver());
    carvri.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carvri", null, true, Load.ALL));
    setColumns(new Column[] {carvriLOCK, carvriAKTIV, carvriCVRI, carvriNAZIV});
  }

  public void setall() {

    ddl.create("Carvri")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cvri", 10, true)
       .addChar("naziv", 80)
       .addPrimaryKey("cvri");


    Naziv = "Carvri";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cvri"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
