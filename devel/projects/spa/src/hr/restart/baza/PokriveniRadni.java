/****license*****************************************************************
**   file: PokriveniRadni.java
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



public class PokriveniRadni extends KreirDrop implements DataModule {

//  dM dm  = dM.getDataModule();
  private static PokriveniRadni PokriveniRadniclass;

  QueryDataSet pokrr = new raDataSet();

/*  Column pokrrLOKK = new Column();
  Column pokrrAKTIV = new Column();
  Column pokrrCRACUNA = new Column();
  Column pokrrCUPLATE = new Column();
  Column pokrrIZNOS = new Column();*/

  public static PokriveniRadni getDataModule() {
    if (PokriveniRadniclass == null) {
      PokriveniRadniclass = new PokriveniRadni();
    }
    return PokriveniRadniclass;
  }

  public QueryDataSet getQueryDataSet() {
    return pokrr;
  }

  public PokriveniRadni() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    /*pokrrLOKK.setCaption("Status zauzetosti");
    pokrrLOKK.setColumnName("LOKK");
    pokrrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    pokrrLOKK.setPrecision(1);
    pokrrLOKK.setTableName("POKRIVENIRADNI");
    pokrrLOKK.setServerColumnName("LOKK");
    pokrrLOKK.setSqlType(1);
    pokrrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pokrrLOKK.setDefault("N");
    pokrrAKTIV.setCaption("Aktivan - neaktivan");
    pokrrAKTIV.setColumnName("AKTIV");
    pokrrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    pokrrAKTIV.setPrecision(1);
    pokrrAKTIV.setTableName("POKRIVENIRADNI");
    pokrrAKTIV.setServerColumnName("AKTIV");
    pokrrAKTIV.setSqlType(1);
    pokrrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pokrrAKTIV.setDefault("D");
    pokrrCRACUNA.setCaption("Ra\u010Dun");
    pokrrCRACUNA.setColumnName("CRACUNA");
    pokrrCRACUNA.setDataType(com.borland.dx.dataset.Variant.STRING);
    pokrrCRACUNA.setPrecision(90);
    pokrrCRACUNA.setRowId(true);
    pokrrCRACUNA.setTableName("POKRIVENIRADNI");
    pokrrCRACUNA.setServerColumnName("CRACUNA");
    pokrrCRACUNA.setSqlType(1);
    pokrrCRACUNA.setWidth(30);
    pokrrCUPLATE.setCaption("Uplata");
    pokrrCUPLATE.setColumnName("CUPLATE");
    pokrrCUPLATE.setDataType(com.borland.dx.dataset.Variant.STRING);
    pokrrCUPLATE.setPrecision(90);
    pokrrCUPLATE.setRowId(true);
    pokrrCUPLATE.setTableName("POKRIVENIRADNI");
    pokrrCUPLATE.setServerColumnName("CUPLATE");
    pokrrCUPLATE.setSqlType(1);
    pokrrCUPLATE.setWidth(30);
    pokrrIZNOS.setCaption("Iznos");
    pokrrIZNOS.setColumnName("IZNOS");
    pokrrIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    pokrrIZNOS.setPrecision(17);
    pokrrIZNOS.setScale(2);
    pokrrIZNOS.setDisplayMask("###,###,##0.00");
    pokrrIZNOS.setDefault("0");
    pokrrIZNOS.setTableName("POKRIVENIRADNI");
    pokrrIZNOS.setServerColumnName("IZNOS");
    pokrrIZNOS.setSqlType(2);
    pokrr.setResolver(dm.getQresolver());
    pokrr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from PokriveniRadni", null, true, Load.ALL));
 setColumns(new Column[] {pokrrLOKK, pokrrAKTIV, pokrrCRACUNA, pokrrCUPLATE, pokrrIZNOS});*/
    initModule();
  }

/*  public void setall() {

    ddl.create("PokriveniRadni")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cracuna", 90, true)
       .addChar("cuplate", 90, true)
       .addFloat("iznos", 17, 2)
       .addPrimaryKey("cracuna,cuplate");


    Naziv = "PokriveniRadni";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cracuna", "cuplate"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }*/
}
