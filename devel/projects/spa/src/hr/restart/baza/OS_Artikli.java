/****license*****************************************************************
**   file: OS_Artikli.java
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



public class OS_Artikli extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Artikli OS_Artikliclass;

  QueryDataSet osart = new raDataSet();

  Column osartLOKK = new Column();
  Column osartAKTIV = new Column();
  Column osartCARTIKLA = new Column();
  Column osartNAZARTIKLA = new Column();

  public static OS_Artikli getDataModule() {
    if (OS_Artikliclass == null) {
      OS_Artikliclass = new OS_Artikli();
    }
    return OS_Artikliclass;
  }

  public QueryDataSet getQueryDataSet() {
    return osart;
  }

  public OS_Artikli() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osartLOKK.setCaption("Status zauzetosti");
    osartLOKK.setColumnName("LOKK");
    osartLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osartLOKK.setPrecision(1);
    osartLOKK.setTableName("OS_ARTIKLI");
    osartLOKK.setServerColumnName("LOKK");
    osartLOKK.setSqlType(1);
    osartLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osartLOKK.setDefault("N");
    osartAKTIV.setCaption("Aktivan - neaktivan");
    osartAKTIV.setColumnName("AKTIV");
    osartAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osartAKTIV.setPrecision(1);
    osartAKTIV.setTableName("OS_ARTIKLI");
    osartAKTIV.setServerColumnName("AKTIV");
    osartAKTIV.setSqlType(1);
    osartAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osartAKTIV.setDefault("D");
    osartCARTIKLA.setCaption("Šifra");
    osartCARTIKLA.setColumnName("CARTIKLA");
    osartCARTIKLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osartCARTIKLA.setPrecision(6);
    osartCARTIKLA.setRowId(true);
    osartCARTIKLA.setTableName("OS_ARTIKLI");
    osartCARTIKLA.setServerColumnName("CARTIKLA");
    osartCARTIKLA.setSqlType(1);
    osartNAZARTIKLA.setCaption("Naziv");
    osartNAZARTIKLA.setColumnName("NAZARTIKLA");
    osartNAZARTIKLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osartNAZARTIKLA.setPrecision(50);
    osartNAZARTIKLA.setTableName("OS_ARTIKLI");
    osartNAZARTIKLA.setServerColumnName("NAZARTIKLA");
    osartNAZARTIKLA.setSqlType(1);
    osartNAZARTIKLA.setWidth(30);
    osart.setResolver(dm.getQresolver());
    osart.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Artikli", null, true, Load.ALL));
 setColumns(new Column[] {osartLOKK, osartAKTIV, osartCARTIKLA, osartNAZARTIKLA});
  }

  public void setall() {

    ddl.create("OS_Artikli")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cartikla", 6, true)
       .addChar("nazartikla", 50)
       .addPrimaryKey("cartikla");


    Naziv = "OS_Artikli";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
