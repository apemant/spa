/****license*****************************************************************
**   file: RN_tekstovi.java
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

public class RN_tekstovi extends KreirDrop implements DataModule {

  private static RN_tekstovi RNtekstclass;
  dM dm  = dM.getDataModule();

  QueryDataSet RNtekst = new raDataSet();
  Column RNtekstLOKK = new Column();
  Column RNtekstAKTIV = new Column();
  Column RNtekstCTEKST = new Column();
  Column RNtekstCSKUPART = new Column();
  Column RNtekstTEKST = new Column();

  public static RN_tekstovi getDataModule() {
    if (RNtekstclass == null) {
      RNtekstclass = new RN_tekstovi();
    }
    return RNtekstclass;
  }

  public RN_tekstovi(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void setall(){


    ddl.create("RN_tekstovi")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("ctekst", 6, true)
       .addChar("cskupart", 6)
       .addChar("tekst", 200)
       .addPrimaryKey("ctekst");


    Naziv="RN_tekstovi";

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
    RNtekstAKTIV.setCaption("Aktiv");
    RNtekstAKTIV.setColumnName("AKTIV");
    RNtekstAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNtekstAKTIV.setDefault("D");
    RNtekstAKTIV.setPrecision(1);
    RNtekstAKTIV.setTableName("RN_tekstovi");
    RNtekstAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNtekstAKTIV.setSqlType(1);
    RNtekstAKTIV.setServerColumnName("AKTIV");

    RNtekstLOKK.setCaption("Lokk");
    RNtekstLOKK.setColumnName("LOKK");
    RNtekstLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNtekstLOKK.setDefault("N");
    RNtekstLOKK.setPrecision(1);
    RNtekstLOKK.setTableName("RN_tekstovi");
    RNtekstLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNtekstLOKK.setSqlType(1);
    RNtekstLOKK.setServerColumnName("LOKK");

    RNtekstCSKUPART.setCaption("Skupina artikala");
    RNtekstCSKUPART.setColumnName("CSKUPART");
    RNtekstCSKUPART.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNtekstCSKUPART.setTableName("RN_tekstovi");
    RNtekstCSKUPART.setPrecision(6);
    RNtekstCSKUPART.setWidth(5);
    RNtekstCSKUPART.setSqlType(1);
    RNtekstCSKUPART.setServerColumnName("CSKUPART");

    RNtekstCTEKST.setCaption("Šifra");
    RNtekstCTEKST.setColumnName("CTEKST");
    RNtekstCTEKST.setDataType(com.borland.dx.dataset.Variant.INT);
    RNtekstCTEKST.setRowId(true);
    RNtekstCTEKST.setTableName("RN_tekstovi");
    RNtekstCTEKST.setSqlType(4);
    RNtekstCTEKST.setServerColumnName("CTEKST");

    RNtekstTEKST.setCaption("Tekst");
    RNtekstTEKST.setColumnName("TEKST");
    RNtekstTEKST.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNtekstTEKST.setPrecision(200);
    RNtekstTEKST.setTableName("RN_tekstovi");
    RNtekstTEKST.setSqlType(1);
    RNtekstTEKST.setServerColumnName("TEKST");


    RNtekst.setResolver(dm.getQresolver());
    RNtekst.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from RN_tekstovi", null, true, Load.ALL));
 setColumns(new Column[] {RNtekstLOKK, RNtekstAKTIV, RNtekstCTEKST, RNtekstCSKUPART, RNtekstTEKST});

  }
  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return RNtekst;
  }

}