/****license*****************************************************************
**   file: RN_znacsub.java
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

public class RN_znacsub extends KreirDrop implements DataModule {

  private static RN_znacsub RNznacsubclass;
  dM dm  = dM.getDataModule();

  QueryDataSet RNznacsub = new raDataSet();

  Column RNznacsubCSUBRN = new Column();
  Column RNznacsubCVRSUBJ = new Column();
  Column RNznacsubCZNAC = new Column();
  Column RNznacsubVRIZNAC = new Column();
  Column RNznacsubCRADNAL = new Column();

  public static RN_znacsub getDataModule() {
    if (RNznacsubclass == null) {
      RNznacsubclass = new RN_znacsub();
    }
    return RNznacsubclass;
  }

  public RN_znacsub(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void setall(){


    ddl.create("RN_znacsub")
       .addChar("csubrn", 20, true)
       .addShort("cvrsubj", 3, true)
       .addShort("cznac", 3, true)
       .addChar("vriznac", 50)
       .addChar("cradnal", 30, true)
       .addPrimaryKey("csubrn,cvrsubj,cznac,cradnal");

    Naziv="RN_znacsub";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cradnal"};
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


    RNznacsubCSUBRN.setCaption("Subjekt");
    RNznacsubCSUBRN.setColumnName("CSUBRN");
    RNznacsubCSUBRN.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNznacsubCSUBRN.setPrecision(20);
    RNznacsubCSUBRN.setRowId(true);
    RNznacsubCSUBRN.setTableName("RN_znacsub");
    RNznacsubCSUBRN.setSqlType(1);
    RNznacsubCSUBRN.setServerColumnName("CSUBRN");

    RNznacsubCVRSUBJ.setCaption("Vrsta subjekta");
    RNznacsubCVRSUBJ.setColumnName("CVRSUBJ");
    RNznacsubCVRSUBJ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    RNznacsubCVRSUBJ.setPrecision(3);
    RNznacsubCVRSUBJ.setRowId(true);
    RNznacsubCVRSUBJ.setTableName("RN_znacsub");
    RNznacsubCVRSUBJ.setSqlType(5);
    RNznacsubCVRSUBJ.setServerColumnName("CVRSUBJ");

    RNznacsubVRIZNAC.setCaption("Vrijednost");
    RNznacsubVRIZNAC.setColumnName("VRIZNAC");
    RNznacsubVRIZNAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNznacsubVRIZNAC.setPrecision(50);
    RNznacsubVRIZNAC.setTableName("RN_znacsub");
    RNznacsubVRIZNAC.setSqlType(1);
    RNznacsubVRIZNAC.setServerColumnName("VRIZNAC");

    RNznacsubCZNAC.setCaption("Zna\u010Dajka");
    RNznacsubCZNAC.setColumnName("CZNAC");
    RNznacsubCZNAC.setDataType(com.borland.dx.dataset.Variant.SHORT);
    RNznacsubCZNAC.setPrecision(3);
    RNznacsubCZNAC.setRowId(true);
    RNznacsubCZNAC.setTableName("RN_znacsub");
    RNznacsubCZNAC.setSqlType(5);
    RNznacsubCZNAC.setServerColumnName("CZNAC");
    
    RNznacsubCRADNAL.setCaption("Radni nalog");
    RNznacsubCRADNAL.setColumnName("CRADNAL");
    RNznacsubCRADNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNznacsubCRADNAL.setPrecision(30);
    RNznacsubCRADNAL.setRowId(true);
    RNznacsubCRADNAL.setTableName("RN_znacsub");
    RNznacsubCRADNAL.setSqlType(1);
    RNznacsubCRADNAL.setWidth(15);
    RNznacsubCRADNAL.setServerColumnName("CRADNAL");

    RNznacsub.setResolver(dm.getQresolver());
    RNznacsub.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from RN_znacsub", null, true, Load.ALL));
    setColumns(new Column[] {RNznacsubCSUBRN,RNznacsubCVRSUBJ, RNznacsubCZNAC, RNznacsubVRIZNAC, RNznacsubCRADNAL});

  }
  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return RNznacsub;
  }
}