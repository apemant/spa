/****license*****************************************************************
**   file: RN_subjekt.java
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

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class RN_subjekt extends KreirDrop implements DataModule {

  private static RN_subjekt RNsubclass;
  dM dm  = dM.getDataModule();

  QueryDataSet RNsub = new raDataSet();
  Column RNsubLOKK = new Column();
  Column RNsubAKTIV = new Column();
  Column RNsubCSUBRN = new Column();
  Column RNsubBROJ = new Column();
  Column RNsubCVRSUBJ = new Column();

  public static RN_subjekt getDataModule() {
    if (RNsubclass == null) {
      RNsubclass = new RN_subjekt();
    }
    return RNsubclass;
  }

  public RN_subjekt(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void setall(){


    ddl.create("RN_subjekt")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("csubrn", 20, true)
       .addChar("broj", 30)
       .addShort("cvrsubj", 3)
       .addPrimaryKey("csubrn");


    Naziv="RN_subjekt";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

/*
    DefIndex= new String[] {
              CommonTable.SqlDefIndex+"idokilokk on doki (lokk)",
              CommonTable.SqlDefIndex+"idokiaktiv on doki (aktiv)",
              CommonTable.SqlDefIndex+"idokicskl on doki (cskl)",
              CommonTable.SqlDefIndex+"idokivrdok on doki (vrdok)",
              CommonTable.SqlDefIndex+"idokibrdok on doki (brdok)",
              CommonTable.SqlDefIndex+"idokidatdok on doki (datdok)",
              CommonTable.SqlDefIndex+"idokicpar on doki (cpar)",
              CommonTable.SqlDefIndex+"idokipj on doki (pj)",
              CommonTable.SqlDefIndex+"idokicorg on doki (corg)",
              CommonTable.SqlDefIndex+"idokicnap on doki (cnap)",
              CommonTable.SqlDefUniqueIndex+"idokikey on doki (cskl,vrdok,god,brdok)"
               };

    NaziviIdx=new String[]{"idokilokk","idokiaktiv","idokicskl","idokivrdok","idokibrdok",
                           "idokidatdok","idokicpar","idokipj","idokicorg",
                           "idokicnap","idokikey" };

*/
  }
  private void jbInit() throws Exception {
    RNsubAKTIV.setCaption("Aktiv");
    RNsubAKTIV.setColumnName("AKTIV");
    RNsubAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNsubAKTIV.setDefault("D");
    RNsubAKTIV.setPrecision(1);
    RNsubAKTIV.setTableName("RN_subjekt");
    RNsubAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNsubAKTIV.setSqlType(1);
    RNsubAKTIV.setServerColumnName("AKTIV");

    RNsubLOKK.setCaption("Lokk");
    RNsubLOKK.setColumnName("LOKK");
    RNsubLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNsubLOKK.setDefault("N");
    RNsubLOKK.setPrecision(1);
    RNsubLOKK.setTableName("RN_subjekt");
    RNsubLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNsubLOKK.setSqlType(1);
    RNsubLOKK.setServerColumnName("LOKK");

    RNsubCSUBRN.setCaption("Subjekt");
    RNsubCSUBRN.setColumnName("CSUBRN");
    RNsubCSUBRN.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNsubCSUBRN.setPrecision(20);
    RNsubCSUBRN.setRowId(true);
    RNsubCSUBRN.setTableName("RN_subjekt");
    RNsubCSUBRN.setSqlType(1);
    RNsubCSUBRN.setServerColumnName("CSUBRN");

    RNsubBROJ.setCaption("Serijski broj");
    RNsubBROJ.setColumnName("BROJ");
    RNsubBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNsubBROJ.setPrecision(30);
    RNsubBROJ.setTableName("RN_subjekt");
    RNsubBROJ.setSqlType(1);
    RNsubBROJ.setServerColumnName("BROJ");

    RNsubCVRSUBJ.setCaption("Vrsta subjekta");
    RNsubCVRSUBJ.setColumnName("CVRSUBJ");
    RNsubCVRSUBJ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    RNsubCVRSUBJ.setPrecision(3);
    RNsubCVRSUBJ.setTableName("RN_subjekt");
    RNsubCVRSUBJ.setSqlType(5);
    RNsubCVRSUBJ.setServerColumnName("CVRSUBJ");

    RNsub.setResolver(dm.getQresolver());
    RNsub.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from RN_subjekt", null, true, Load.ALL));
    setColumns(new Column[] {RNsubLOKK, RNsubAKTIV, RNsubCSUBRN, RNsubBROJ, RNsubCVRSUBJ});

  }
  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return RNsub;
  }

}