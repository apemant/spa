/****license*****************************************************************
**   file: Pokriveni.java
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
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;



public class Pokriveni extends KreirDrop implements DataModule {

//  dM dm  = dM.getDataModule();
  private static Pokriveni Pokriveniclass;

  QueryDataSet pokr = new raDataSet();

/*  Column pokrLOKK = new Column();
  Column pokrAKTIV = new Column();
  Column pokrCRACUNA = new Column();
  Column pokrCUPLATE = new Column();
  Column pokrIZNOS = new Column();*/

  public static Pokriveni getDataModule() {
    if (Pokriveniclass == null) {
      Pokriveniclass = new Pokriveni();
    }
    return Pokriveniclass;
  }

  public QueryDataSet getQueryDataSet() {
    return pokr;
  }

  public Pokriveni() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    /*pokrLOKK.setCaption("Status zauzetosti");
    pokrLOKK.setColumnName("LOKK");
    pokrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    pokrLOKK.setPrecision(1);
    pokrLOKK.setTableName("POKRIVENI");
    pokrLOKK.setServerColumnName("LOKK");
    pokrLOKK.setSqlType(1);
    pokrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pokrLOKK.setDefault("N");
    pokrAKTIV.setCaption("Aktivan - neaktivan");
    pokrAKTIV.setColumnName("AKTIV");
    pokrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    pokrAKTIV.setPrecision(1);
    pokrAKTIV.setTableName("POKRIVENI");
    pokrAKTIV.setServerColumnName("AKTIV");
    pokrAKTIV.setSqlType(1);
    pokrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pokrAKTIV.setDefault("D");
    pokrCRACUNA.setCaption("Ra\u010Dun");
    pokrCRACUNA.setColumnName("CRACUNA");
    pokrCRACUNA.setDataType(com.borland.dx.dataset.Variant.STRING);
    pokrCRACUNA.setPrecision(90);
    pokrCRACUNA.setRowId(true);
    pokrCRACUNA.setTableName("POKRIVENI");
    pokrCRACUNA.setServerColumnName("CRACUNA");
    pokrCRACUNA.setSqlType(1);
    pokrCRACUNA.setWidth(30);
    pokrCUPLATE.setCaption("Uplata");
    pokrCUPLATE.setColumnName("CUPLATE");
    pokrCUPLATE.setDataType(com.borland.dx.dataset.Variant.STRING);
    pokrCUPLATE.setPrecision(90);
    pokrCUPLATE.setRowId(true);
    pokrCUPLATE.setTableName("POKRIVENI");
    pokrCUPLATE.setServerColumnName("CUPLATE");
    pokrCUPLATE.setSqlType(1);
    pokrCUPLATE.setWidth(30);
    pokrIZNOS.setCaption("Iznos");
    pokrIZNOS.setColumnName("IZNOS");
    pokrIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pokrIZNOS.setPrecision(17);
    pokrIZNOS.setScale(2);
    pokrIZNOS.setDisplayMask("###,###,##0.00");
    pokrIZNOS.setDefault("0");
    pokrIZNOS.setTableName("POKRIVENI");
    pokrIZNOS.setServerColumnName("IZNOS");
    pokrIZNOS.setSqlType(2);
    pokr.setResolver(dm.getQresolver());
    pokr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Pokriveni", null, true, Load.ALL));
 setColumns(new Column[] {pokrLOKK, pokrAKTIV, pokrCRACUNA, pokrCUPLATE, pokrIZNOS});*/
    initModule();
  }

  /*public void setall() {

    ddl.create("Pokriveni")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cracuna", 90, true)
       .addChar("cuplate", 90, true)
       .addFloat("iznos", 17, 2)
       .addPrimaryKey("cracuna,cuplate");


    Naziv = "Pokriveni";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cracuna", "cuplate"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }*/
}
