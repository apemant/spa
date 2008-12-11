/****license*****************************************************************
**   file: FondSati.java
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


public class FondSati extends KreirDrop {

  private static FondSati fondSaticlass;
  
  QueryDataSet fondSati = new raDataSet();
  
  public static FondSati getDataModule() {
    if (fondSaticlass == null) {
      fondSaticlass = new FondSati();
    }
    return fondSaticlass;
  }

  public QueryDataSet getQueryDataSet() {
    return fondSati;
  }

  public FondSati() {
    try {
      modules.put(this.getClass().getName(), this);
      initModule();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}

//package hr.restart.baza;
//import com.borland.dx.dataset.Column;
//import com.borland.dx.dataset.DataModule;
//import com.borland.dx.sql.dataset.Load;
//import com.borland.dx.sql.dataset.QueryDataSet;
//import com.borland.dx.sql.dataset.QueryDescriptor;
//
//
//
//public class FondSati extends KreirDrop implements DataModule {
//
//  dM dm  = dM.getDataModule();
//  private static FondSati FondSaticlass;
//
//  QueryDataSet fosa = new raDataSet();
//
//  Column fosaKNJIG = new Column();
//  Column fosaGODINA = new Column();
//  Column fosaMJESEC = new Column();
//  Column fosaSATIRAD = new Column();
//  Column fosaSATIPRAZ = new Column();
//  Column fosaSATIUK = new Column();
//
//  public static FondSati getDataModule() {
//    if (FondSaticlass == null) {
//      FondSaticlass = new FondSati();
//    }
//    return FondSaticlass;
//  }
//
//  public QueryDataSet getQueryDataSet() {
//    return fosa;
//  }
//
//  public FondSati() {
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
//    fosaKNJIG.setCaption("Poduze\u0107e");
//    fosaKNJIG.setColumnName("KNJIG");
//    fosaKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
//    fosaKNJIG.setPrecision(12);
//    fosaKNJIG.setRowId(true);
//    fosaKNJIG.setTableName("FONDSATI");
//    fosaKNJIG.setServerColumnName("KNJIG");
//    fosaKNJIG.setSqlType(1);
//    fosaGODINA.setCaption("Godina");
//    fosaGODINA.setColumnName("GODINA");
//    fosaGODINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    fosaGODINA.setPrecision(4);
//    fosaGODINA.setRowId(true);
//    fosaGODINA.setTableName("FONDSATI");
//    fosaGODINA.setServerColumnName("GODINA");
//    fosaGODINA.setSqlType(5);
//    fosaGODINA.setWidth(4);
//    fosaMJESEC.setCaption("Mjesec");
//    fosaMJESEC.setColumnName("MJESEC");
//    fosaMJESEC.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    fosaMJESEC.setPrecision(2);
//    fosaMJESEC.setRowId(true);
//    fosaMJESEC.setTableName("FONDSATI");
//    fosaMJESEC.setServerColumnName("MJESEC");
//    fosaMJESEC.setSqlType(5);
//    fosaMJESEC.setWidth(2);
//    fosaSATIRAD.setCaption("Sati rada");
//    fosaSATIRAD.setColumnName("SATIRAD");
//    fosaSATIRAD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    fosaSATIRAD.setPrecision(17);
//    fosaSATIRAD.setScale(2);
//    fosaSATIRAD.setDisplayMask("###,###,##0.00");
//    fosaSATIRAD.setDefault("0");
//    fosaSATIRAD.setTableName("FONDSATI");
//    fosaSATIRAD.setServerColumnName("SATIRAD");
//    fosaSATIRAD.setSqlType(2);
//    fosaSATIRAD.setDefault("0");
//    fosaSATIPRAZ.setCaption("Sati praznika");
//    fosaSATIPRAZ.setColumnName("SATIPRAZ");
//    fosaSATIPRAZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    fosaSATIPRAZ.setPrecision(17);
//    fosaSATIPRAZ.setScale(2);
//    fosaSATIPRAZ.setDisplayMask("###,###,##0.00");
//    fosaSATIPRAZ.setDefault("0");
//    fosaSATIPRAZ.setTableName("FONDSATI");
//    fosaSATIPRAZ.setServerColumnName("SATIPRAZ");
//    fosaSATIPRAZ.setSqlType(2);
//    fosaSATIPRAZ.setDefault("0");
//    fosaSATIUK.setCaption("Sati ukupno");
//    fosaSATIUK.setColumnName("SATIUK");
//    fosaSATIUK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    fosaSATIUK.setPrecision(17);
//    fosaSATIUK.setScale(2);
//    fosaSATIUK.setDisplayMask("###,###,##0.00");
//    fosaSATIUK.setDefault("0");
//    fosaSATIUK.setTableName("FONDSATI");
//    fosaSATIUK.setServerColumnName("SATIUK");
//    fosaSATIUK.setSqlType(2);
//    fosaSATIUK.setDefault("0");
//    fosa.setResolver(dm.getQresolver());
//    fosa.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from FondSati", null, true, Load.ALL));
// setColumns(new Column[] {fosaKNJIG, fosaGODINA, fosaMJESEC, fosaSATIRAD, fosaSATIPRAZ, fosaSATIUK});
//  }
//
//  public void setall() {
//
//    ddl.create("FondSati")
//       .addChar("knjig", 12, true)
//       .addShort("godina", 4, true)
//       .addShort("mjesec", 2, true)
//       .addFloat("satirad", 17, 2)
//       .addFloat("satipraz", 17, 2)
//       .addFloat("satiuk", 17, 2)
//       .addPrimaryKey("knjig,godina,mjesec");
//
//
//    Naziv = "FondSati";
//
//    SqlDefTabela = ddl.getCreateTableString();
//
//    String[] idx = new String[] {};
//    String[] uidx = new String[] {};
//    DefIndex = ddl.getIndices(idx, uidx);
//    NaziviIdx = ddl.getIndexNames(idx, uidx);
//  }
//}
