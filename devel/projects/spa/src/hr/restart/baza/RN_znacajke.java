/****license*****************************************************************
**   file: RN_znacajke.java
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

public class RN_znacajke extends KreirDrop implements DataModule {

  private static RN_znacajke RNznacajkeclass;
  dM dm  = dM.getDataModule();

  QueryDataSet RNznacajke = new raDataSet();
  Column RNznacajkeLOKK = new Column();
  Column RNznacajkeAKTIV = new Column();
  Column RNznacajkeCZNAC = new Column();
  Column RNznacajkeCVRSUBJ = new Column();
  Column RNznacajkeZNACOPIS = new Column();
  Column RNznacajkeZNACTIP = new Column();
  Column RNznacajkeZNACSIF = new Column();
  Column RNznacajkeZNACREQ = new Column();
  Column RNznacajkeZNACDOH = new Column();

  public static RN_znacajke getDataModule() {
    if (RNznacajkeclass == null) {
      RNznacajkeclass = new RN_znacajke();
    }
    return RNznacajkeclass;
  }

  public RN_znacajke(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void setall(){


    ddl.create("RN_znacajke")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("cznac", 3, true)
       .addShort("cvrsubj", 3, true)
       .addChar("znacopis", 30)
       .addChar("znactip", 1)
       .addChar("znacsif", 1)
       .addChar("znacreq", 1)
       .addChar("znacdoh", 50)
       .addPrimaryKey("cznac,cvrsubj");

    Naziv="RN_znacajke";

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
    RNznacajkeAKTIV.setCaption("Aktiv");
    RNznacajkeAKTIV.setColumnName("AKTIV");
    RNznacajkeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNznacajkeAKTIV.setDefault("D");
    RNznacajkeAKTIV.setPrecision(1);
    RNznacajkeAKTIV.setTableName("RN_znacajke");
    RNznacajkeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNznacajkeAKTIV.setSqlType(1);
    RNznacajkeAKTIV.setServerColumnName("AKTIV");

    RNznacajkeLOKK.setCaption("Lokk");
    RNznacajkeLOKK.setColumnName("LOKK");
    RNznacajkeLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNznacajkeLOKK.setDefault("N");
    RNznacajkeLOKK.setPrecision(1);
    RNznacajkeLOKK.setTableName("RN_znacajke");
    RNznacajkeLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNznacajkeLOKK.setSqlType(1);
    RNznacajkeLOKK.setServerColumnName("LOKK");

    RNznacajkeCZNAC.setCaption("Podatak");
    RNznacajkeCZNAC.setColumnName("CZNAC");
    RNznacajkeCZNAC.setDataType(com.borland.dx.dataset.Variant.SHORT);
    RNznacajkeCZNAC.setPrecision(3);
    RNznacajkeCZNAC.setRowId(true);
    RNznacajkeCZNAC.setTableName("RN_znacajke");
    RNznacajkeCZNAC.setSqlType(5);
    RNznacajkeCZNAC.setServerColumnName("CZNAC");
    RNznacajkeCZNAC.setWidth(5);

    RNznacajkeCVRSUBJ.setCaption("Vrsta subjekta");
    RNznacajkeCVRSUBJ.setColumnName("CVRSUBJ");
    RNznacajkeCVRSUBJ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    RNznacajkeCVRSUBJ.setPrecision(3);
    RNznacajkeCVRSUBJ.setRowId(true);
    RNznacajkeCVRSUBJ.setTableName("RN_znacajke");
    RNznacajkeCVRSUBJ.setSqlType(5);
    RNznacajkeCVRSUBJ.setServerColumnName("CVRSUBJ");
    RNznacajkeCVRSUBJ.setWidth(5);

    RNznacajkeZNACOPIS.setCaption("Opis podatka");
    RNznacajkeZNACOPIS.setColumnName("ZNACOPIS");
    RNznacajkeZNACOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNznacajkeZNACOPIS.setPrecision(30);
    RNznacajkeZNACOPIS.setTableName("RN_znacajke");
    RNznacajkeZNACOPIS.setSqlType(1);
    RNznacajkeZNACOPIS.setServerColumnName("ZNACOPIS");
    RNznacajkeZNACOPIS.setWidth(30);

    RNznacajkeZNACTIP.setCaption("Tip zna\u010Dajke");
    RNznacajkeZNACTIP.setColumnName("ZNACTIP");
    RNznacajkeZNACTIP.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNznacajkeZNACTIP.setDefault("S");
    RNznacajkeZNACTIP.setPrecision(1);
    RNznacajkeZNACTIP.setTableName("RN_znacajke");
    RNznacajkeZNACTIP.setSqlType(1);
    RNznacajkeZNACTIP.setServerColumnName("ZNACTIP");

    RNznacajkeZNACSIF.setCaption("Šifrirana zna\u010Dajka");
    RNznacajkeZNACSIF.setColumnName("ZNACSIF");
    RNznacajkeZNACSIF.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNznacajkeZNACSIF.setPrecision(1);
    RNznacajkeZNACSIF.setTableName("RN_znacajke");
    RNznacajkeZNACSIF.setSqlType(1);
    RNznacajkeZNACSIF.setServerColumnName("ZNACSIF");

    RNznacajkeZNACREQ.setCaption("Obavezni unos");
    RNznacajkeZNACREQ.setColumnName("ZNACREQ");
    RNznacajkeZNACREQ.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNznacajkeZNACREQ.setPrecision(1);
    RNznacajkeZNACREQ.setTableName("RN_znacajke");
    RNznacajkeZNACREQ.setSqlType(1);
    RNznacajkeZNACREQ.setServerColumnName("ZNACREQ");
    
    RNznacajkeZNACDOH.setCaption("Dohvat iz dM");
    RNznacajkeZNACDOH.setColumnName("ZNACDOH");
    RNznacajkeZNACDOH.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNznacajkeZNACDOH.setPrecision(50);
    RNznacajkeZNACDOH.setTableName("RN_znacajke");
    RNznacajkeZNACDOH.setSqlType(1);
    RNznacajkeZNACDOH.setServerColumnName("ZNACDOH");
    RNznacajkeZNACDOH.setWidth(30);

    RNznacajke.setResolver(dm.getQresolver());
    RNznacajke.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from RN_znacajke", null, true, Load.ALL));
    setColumns(new Column[] {RNznacajkeLOKK, RNznacajkeAKTIV, RNznacajkeCZNAC, RNznacajkeCVRSUBJ, RNznacajkeZNACOPIS, RNznacajkeZNACTIP, RNznacajkeZNACSIF, RNznacajkeZNACREQ, RNznacajkeZNACDOH});

  }
  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return RNznacajke;
  }

}