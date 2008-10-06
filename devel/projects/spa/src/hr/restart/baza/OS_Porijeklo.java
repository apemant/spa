/****license*****************************************************************
**   file: OS_Porijeklo.java
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



public class OS_Porijeklo extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Porijeklo OS_Porijekloclass;

  QueryDataSet ospor = new raDataSet();

  Column osporLOKK = new Column();
  Column osporAKTIV = new Column();
  Column osporCPORIJEKLO = new Column();
  Column osporNAZPORIJEKLA = new Column();

  public static OS_Porijeklo getDataModule() {
    if (OS_Porijekloclass == null) {
      OS_Porijekloclass = new OS_Porijeklo();
    }
    return OS_Porijekloclass;
  }

  public QueryDataSet getQueryDataSet() {
    return ospor;
  }

  public OS_Porijeklo() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osporLOKK.setCaption("Status zauzetosti");
    osporLOKK.setColumnName("LOKK");
    osporLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osporLOKK.setPrecision(1);
    osporLOKK.setTableName("OS_PORIJEKLO");
    osporLOKK.setServerColumnName("LOKK");
    osporLOKK.setSqlType(1);
    osporLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osporLOKK.setDefault("N");
    osporAKTIV.setCaption("Aktivan - neaktivan");
    osporAKTIV.setColumnName("AKTIV");
    osporAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osporAKTIV.setPrecision(1);
    osporAKTIV.setTableName("OS_PORIJEKLO");
    osporAKTIV.setServerColumnName("AKTIV");
    osporAKTIV.setSqlType(1);
    osporAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osporAKTIV.setDefault("D");
    osporCPORIJEKLO.setCaption("Šifra");
    osporCPORIJEKLO.setColumnName("CPORIJEKLO");
    osporCPORIJEKLO.setDataType(com.borland.dx.dataset.Variant.STRING);
    osporCPORIJEKLO.setPrecision(2);
    osporCPORIJEKLO.setRowId(true);
    osporCPORIJEKLO.setTableName("OS_PORIJEKLO");
    osporCPORIJEKLO.setServerColumnName("CPORIJEKLO");
    osporCPORIJEKLO.setSqlType(1);
    osporNAZPORIJEKLA.setCaption("Naziv");
    osporNAZPORIJEKLA.setColumnName("NAZPORIJEKLA");
    osporNAZPORIJEKLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osporNAZPORIJEKLA.setPrecision(40);
    osporNAZPORIJEKLA.setTableName("OS_PORIJEKLO");
    osporNAZPORIJEKLA.setServerColumnName("NAZPORIJEKLA");
    osporNAZPORIJEKLA.setSqlType(1);
    osporNAZPORIJEKLA.setWidth(30);
    ospor.setResolver(dm.getQresolver());
    ospor.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Porijeklo", null, true, Load.ALL));
 setColumns(new Column[] {osporLOKK, osporAKTIV, osporCPORIJEKLO, osporNAZPORIJEKLA});
  }

  public void setall() {

    ddl.create("OS_Porijeklo")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cporijeklo", 2, true)
       .addChar("nazporijekla", 40)
       .addPrimaryKey("cporijeklo");


    Naziv = "OS_Porijeklo";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
