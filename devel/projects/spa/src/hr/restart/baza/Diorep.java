/****license*****************************************************************
**   file: Diorep.java
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



public class Diorep extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Diorep Diorepclass;

  QueryDataSet dr = new raDataSet();

  Column drCORG = new Column();
  Column drVRSEC = new Column();
  Column drVRDOK = new Column();
  Column drID = new Column();

  public static Diorep getDataModule() {
    if (Diorepclass == null) {
      Diorepclass = new Diorep();
    }
    return Diorepclass;
  }

  public QueryDataSet getQueryDataSet() {
    return dr;
  }

  public Diorep() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    drCORG.setCaption("Org. jedinica");
    drCORG.setColumnName("CORG");
    drCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    drCORG.setPrecision(12);
    drCORG.setRowId(true);
    drCORG.setTableName("DIOREP");
    drCORG.setServerColumnName("CORG");
    drCORG.setSqlType(1);
    drVRSEC.setCaption("Dio izvještaja");
    drVRSEC.setColumnName("VRSEC");
    drVRSEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    drVRSEC.setPrecision(3);
    drVRSEC.setRowId(true);
    drVRSEC.setTableName("DIOREP");
    drVRSEC.setServerColumnName("VRSEC");
    drVRSEC.setSqlType(1);
    drVRDOK.setCaption("VD");
    drVRDOK.setColumnName("VRDOK");
    drVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    drVRDOK.setPrecision(3);
    drVRDOK.setRowId(true);
    drVRDOK.setTableName("DIOREP");
    drVRDOK.setServerColumnName("VRDOK");
    drVRDOK.setSqlType(1);
    drVRDOK.setWidth(4);
    drID.setCaption("Opis dizajna");
    drID.setColumnName("ID");
    drID.setDataType(com.borland.dx.dataset.Variant.INT);
    drID.setPrecision(6);
    drID.setTableName("DIOREP");
    drID.setServerColumnName("ID");
    drID.setSqlType(4);
    drID.setWidth(6);
    dr.setResolver(dm.getQresolver());
    dr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Diorep", null, true, Load.ALL));
 setColumns(new Column[] {drCORG, drVRSEC, drVRDOK, drID});
  }

  public void setall() {

    ddl.create("Diorep")
       .addChar("corg", 12, true)
       .addChar("vrsec", 3, true)
       .addChar("vrdok", 3, true)
       .addInteger("id", 6)
       .addPrimaryKey("corg,vrsec,vrdok");


    Naziv = "Diorep";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
