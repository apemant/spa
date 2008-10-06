/****license*****************************************************************
**   file: OS_Vrpromjene.java
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



public class OS_Vrpromjene extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Vrpromjene OS_Vrpromjeneclass;

  QueryDataSet osvr = new QueryDataSet();

  Column osvrLOKK = new Column();
  Column osvrAKTIV = new Column();
  Column osvrCPROMJENE = new Column();
  Column osvrNAZPROMJENE = new Column();
  Column osvrTIPPROMJENE = new Column();

  public static OS_Vrpromjene getDataModule() {
    if (OS_Vrpromjeneclass == null) {
      OS_Vrpromjeneclass = new OS_Vrpromjene();
    }
    return OS_Vrpromjeneclass;
  }

  public QueryDataSet getQueryDataSet() {
    return osvr;
  }

  public OS_Vrpromjene() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osvrLOKK.setCaption("Status zauzetosti");
    osvrLOKK.setColumnName("LOKK");
    osvrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osvrLOKK.setPrecision(1);
    osvrLOKK.setTableName("OS_VRPROMJENE");
    osvrLOKK.setServerColumnName("LOKK");
    osvrLOKK.setSqlType(1);
    osvrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osvrLOKK.setDefault("N");
    osvrAKTIV.setCaption("Aktivan - neaktivan");
    osvrAKTIV.setColumnName("AKTIV");
    osvrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osvrAKTIV.setPrecision(1);
    osvrAKTIV.setTableName("OS_VRPROMJENE");
    osvrAKTIV.setServerColumnName("AKTIV");
    osvrAKTIV.setSqlType(1);
    osvrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osvrAKTIV.setDefault("D");
    osvrCPROMJENE.setCaption("Šifra");
    osvrCPROMJENE.setColumnName("CPROMJENE");
    osvrCPROMJENE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osvrCPROMJENE.setPrecision(3);
    osvrCPROMJENE.setRowId(true);
    osvrCPROMJENE.setTableName("OS_VRPROMJENE");
    osvrCPROMJENE.setServerColumnName("CPROMJENE");
    osvrCPROMJENE.setSqlType(1);
    osvrNAZPROMJENE.setCaption("Naziv");
    osvrNAZPROMJENE.setColumnName("NAZPROMJENE");
    osvrNAZPROMJENE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osvrNAZPROMJENE.setPrecision(50);
    osvrNAZPROMJENE.setTableName("OS_VRPROMJENE");
    osvrNAZPROMJENE.setServerColumnName("NAZPROMJENE");
    osvrNAZPROMJENE.setSqlType(1);
    osvrNAZPROMJENE.setWidth(30);
    osvrTIPPROMJENE.setCaption("Tip");
    osvrTIPPROMJENE.setColumnName("TIPPROMJENE");
    osvrTIPPROMJENE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osvrTIPPROMJENE.setPrecision(1);
    osvrTIPPROMJENE.setTableName("OS_VRPROMJENE");
    osvrTIPPROMJENE.setServerColumnName("TIPPROMJENE");
    osvrTIPPROMJENE.setSqlType(1);
    osvrTIPPROMJENE.setDefault("N");
    osvr.setResolver(dm.getQresolver());
    osvr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Vrpromjene", null, true, Load.ALL));
 setColumns(new Column[] {osvrLOKK, osvrAKTIV, osvrCPROMJENE, osvrNAZPROMJENE, osvrTIPPROMJENE});
  }

  public void setall() {

    ddl.create("OS_Vrpromjene")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cpromjene", 3, true)
       .addChar("nazpromjene", 50)
       .addChar("tippromjene", 1, "N")
       .addPrimaryKey("cpromjene");


    Naziv = "OS_Vrpromjene";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
