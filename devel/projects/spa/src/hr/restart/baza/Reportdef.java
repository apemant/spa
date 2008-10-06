/****license*****************************************************************
**   file: Reportdef.java
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



public class Reportdef extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Reportdef Reportdefclass;

  QueryDataSet rpd = new raDataSet();

  Column rpdID = new Column();
  Column rpdVRDOK = new Column();
  Column rpdIZLAZNI = new Column();
  Column rpdORIENT = new Column();

  public static Reportdef getDataModule() {
    if (Reportdefclass == null) {
      Reportdefclass = new Reportdef();
    }
    return Reportdefclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rpd;
  }

  public Reportdef() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    rpdID.setCaption("Jedinstveno ime izvještaja");
    rpdID.setColumnName("ID");
    rpdID.setDataType(com.borland.dx.dataset.Variant.STRING);
    rpdID.setPrecision(50);
    rpdID.setRowId(true);
    rpdID.setTableName("REPORTDEF");
    rpdID.setServerColumnName("ID");
    rpdID.setSqlType(1);
    rpdID.setWidth(30);
    rpdVRDOK.setCaption("Vrsta dokumenta");
    rpdVRDOK.setColumnName("VRDOK");
    rpdVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rpdVRDOK.setPrecision(3);
    rpdVRDOK.setTableName("REPORTDEF");
    rpdVRDOK.setServerColumnName("VRDOK");
    rpdVRDOK.setSqlType(1);
    rpdIZLAZNI.setCaption("Izlazni izvještaj");
    rpdIZLAZNI.setColumnName("IZLAZNI");
    rpdIZLAZNI.setDataType(com.borland.dx.dataset.Variant.STRING);
    rpdIZLAZNI.setPrecision(1);
    rpdIZLAZNI.setTableName("REPORTDEF");
    rpdIZLAZNI.setServerColumnName("IZLAZNI");
    rpdIZLAZNI.setSqlType(1);
    rpdIZLAZNI.setDefault("N");
    rpdORIENT.setCaption("Orijentacija");
    rpdORIENT.setColumnName("ORIENT");
    rpdORIENT.setDataType(com.borland.dx.dataset.Variant.STRING);
    rpdORIENT.setPrecision(1);
    rpdORIENT.setTableName("REPORTDEF");
    rpdORIENT.setServerColumnName("ORIENT");
    rpdORIENT.setSqlType(1);
    rpdORIENT.setDefault("P");
    rpd.setResolver(dm.getQresolver());
    rpd.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Reportdef", null, true, Load.ALL));
 setColumns(new Column[] {rpdID, rpdVRDOK, rpdIZLAZNI, rpdORIENT});
  }

  public void setall() {

    ddl.create("Reportdef")
       .addChar("id", 50, true)
       .addChar("vrdok", 3)
       .addChar("izlazni", 1, "N")
       .addChar("orient", 1, "P")
       .addPrimaryKey("id");


    Naziv = "Reportdef";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
