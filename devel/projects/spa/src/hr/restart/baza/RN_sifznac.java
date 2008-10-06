/****license*****************************************************************
**   file: RN_sifznac.java
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

public class RN_sifznac extends KreirDrop implements DataModule {

  private static RN_sifznac RNsifznacclass;
  dM dm  = dM.getDataModule();

  QueryDataSet RNsifznac = new raDataSet();
  Column RNsifznacLOKK = new Column();
  Column RNsifznacAKTIV = new Column();
  Column RNsifznacCVRSUBJ = new Column();
  Column RNsifznacCZNAC = new Column();
  Column RNsifznacVRIZNAC = new Column();
  Column RNsifznacOPIS = new Column();

  public static RN_sifznac getDataModule() {
    if (RNsifznacclass == null) {
      RNsifznacclass = new RN_sifznac();
    }
    return RNsifznacclass;
  }

  public RN_sifznac(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void setall(){

    ddl.create("RN_sifznac")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("cvrsubj", 3, true)
       .addShort("cznac", 3, true)
       .addChar("vriznac", 50, true)
       .addChar("opis", 50)
       .addPrimaryKey("cvrsubj,cznac,vriznac");

    Naziv="RN_sifznac";

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
    RNsifznacAKTIV.setCaption("Aktiv");
    RNsifznacAKTIV.setColumnName("AKTIV");
    RNsifznacAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNsifznacAKTIV.setDefault("D");
    RNsifznacAKTIV.setPrecision(1);
    RNsifznacAKTIV.setTableName("RN_sifznac");
    RNsifznacAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNsifznacAKTIV.setSqlType(1);
    RNsifznacAKTIV.setServerColumnName("AKTIV");

    RNsifznacLOKK.setCaption("Lokk");
    RNsifznacLOKK.setColumnName("LOKK");
    RNsifznacLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNsifznacLOKK.setDefault("N");
    RNsifznacLOKK.setPrecision(1);
    RNsifznacLOKK.setTableName("RN_sifznac");
    RNsifznacLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNsifznacLOKK.setSqlType(1);
    RNsifznacLOKK.setServerColumnName("LOKK");

    RNsifznacOPIS.setCaption("Opis vrijednosti");
    RNsifznacOPIS.setColumnName("OPIS");
    RNsifznacOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNsifznacOPIS.setPrecision(50);
    RNsifznacOPIS.setTableName("RN_sifznac");
    RNsifznacOPIS.setSqlType(1);
    RNsifznacOPIS.setServerColumnName("OPIS");

    RNsifznacVRIZNAC.setCaption("Vrijednost");
    RNsifznacVRIZNAC.setColumnName("VRIZNAC");
    RNsifznacVRIZNAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNsifznacVRIZNAC.setPrecision(50);
    RNsifznacVRIZNAC.setRowId(true);
    RNsifznacVRIZNAC.setTableName("RN_sifznac");
    RNsifznacVRIZNAC.setSqlType(1);
    RNsifznacVRIZNAC.setServerColumnName("VRIZNAC");

    RNsifznacCZNAC.setCaption("Podatak");
    RNsifznacCZNAC.setColumnName("CZNAC");
    RNsifznacCZNAC.setDataType(com.borland.dx.dataset.Variant.SHORT);
    RNsifznacCZNAC.setPrecision(3);
    RNsifznacCZNAC.setRowId(true);
    RNsifznacCZNAC.setTableName("RN_sifznac");
    RNsifznacCZNAC.setSqlType(5);
    RNsifznacCZNAC.setServerColumnName("CZNAC");

    RNsifznacCVRSUBJ.setCaption("Vrsta subjekta");
    RNsifznacCVRSUBJ.setColumnName("CVRSUBJ");
    RNsifznacCVRSUBJ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    RNsifznacCVRSUBJ.setPrecision(3);
    RNsifznacCVRSUBJ.setRowId(true);
    RNsifznacCVRSUBJ.setTableName("RN_sifznac");
    RNsifznacCVRSUBJ.setSqlType(5);
    RNsifznacCVRSUBJ.setServerColumnName("CVRSUBJ");

    RNsifznac.setResolver(dm.getQresolver());
    RNsifznac.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from RN_sifznac", null, true, Load.ALL));
    setColumns(new Column[] {RNsifznacLOKK, RNsifznacAKTIV, RNsifznacCVRSUBJ, RNsifznacCZNAC, RNsifznacVRIZNAC, RNsifznacOPIS});

  }
  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return RNsifznac;
  }

}