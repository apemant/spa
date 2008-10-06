/****license*****************************************************************
**   file: PVROdbCorg.java
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
import com.borland.dx.sql.dataset.QueryDataSet;

public class PVROdbCorg extends KreirDrop {

  private static PVROdbCorg PVROdbCorgclass;
  
  QueryDataSet pvoc = new raDataSet();
  
  public static PVROdbCorg getDataModule() {
    if (PVROdbCorgclass == null) {
      PVROdbCorgclass = new PVROdbCorg();
    }
    return PVROdbCorgclass;
  }

  public QueryDataSet getQueryDataSet() {
    return pvoc;
  }

  public PVROdbCorg() {
    try {
      modules.put(this.getClass().getName(), this);
      initModule();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}

//public class PVROdbCorg extends KreirDrop implements DataModule {
//
//  dM dm  = dM.getDataModule();
//  private static PVROdbCorg PVROdbCorgclass;
//
//  QueryDataSet pvoc = new raDataSet();
//
//  Column pvocCPOV = new Column();
//  Column pvocCORG = new Column();
//  Column pvocCVRODB = new Column();
//
//  public static PVROdbCorg getDataModule() {
//    if (PVROdbCorgclass == null) {
//      PVROdbCorgclass = new PVROdbCorg();
//    }
//    return PVROdbCorgclass;
//  }
//
//  public QueryDataSet getQueryDataSet() {
//    return pvoc;
//  }
//
//  public PVROdbCorg() {
//    try {
//      modules.put(this.getClass().getName(), this);
//      jbInit();
//    }
//    catch(Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  private void jbInit() throws Exception {
//    pvocCPOV.setCaption("Šifra povjerioca");
//    pvocCPOV.setColumnName("CPOV");
//    pvocCPOV.setDataType(com.borland.dx.dataset.Variant.INT);
//    pvocCPOV.setPrecision(6);
//    pvocCPOV.setRowId(true);
//    pvocCPOV.setTableName("PVRODBCORG");
//    pvocCPOV.setServerColumnName("CPOV");
//    pvocCPOV.setSqlType(4);
//    pvocCPOV.setWidth(6);
//    pvocCORG.setCaption("OJ");
//    pvocCORG.setColumnName("CORG");
//    pvocCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
//    pvocCORG.setPrecision(12);
//    pvocCORG.setRowId(true);
//    pvocCORG.setTableName("PVRODBCORG");
//    pvocCORG.setServerColumnName("CORG");
//    pvocCORG.setSqlType(1);
//    pvocCVRODB.setCaption("Vrsta odbitka");
//    pvocCVRODB.setColumnName("CVRODB");
//    pvocCVRODB.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    pvocCVRODB.setPrecision(4);
//    pvocCVRODB.setTableName("PVRODBCORG");
//    pvocCVRODB.setServerColumnName("CVRODB");
//    pvocCVRODB.setSqlType(5);
//    pvocCVRODB.setWidth(4);
//    pvoc.setResolver(dm.getQresolver());
//    pvoc.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from PVROdbCorg", null, true, Load.ALL));
// setColumns(new Column[] {pvocCPOV, pvocCORG, pvocCVRODB});
//  }
//
//  public void setall() {
//
//    ddl.create("PVROdbCorg")
//       .addInteger("cpov", 6, true)
//       .addChar("corg", 12, true)
//       .addShort("cvrodb", 4)
//       .addPrimaryKey("cpov,corg");
//
//
//    Naziv = "PVROdbCorg";
//
//    SqlDefTabela = ddl.getCreateTableString();
//
//    String[] idx = new String[] {};
//    String[] uidx = new String[] {};
//    DefIndex = ddl.getIndices(idx, uidx);
//    NaziviIdx = ddl.getIndexNames(idx, uidx);
//  }
//}
