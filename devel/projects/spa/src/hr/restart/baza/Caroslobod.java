/****license*****************************************************************
**   file: Caroslobod.java
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



public class Caroslobod extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Caroslobod Caroslobodclass;

  QueryDataSet caroslobod = new QueryDataSet();

  Column caroslobodLOCK = new Column();
  Column caroslobodAKTIV = new Column();
  Column caroslobodCOSLO = new Column();
  Column caroslobodNAZIV = new Column();

  public static Caroslobod getDataModule() {
    if (Caroslobodclass == null) {
      Caroslobodclass = new Caroslobod();
    }
    return Caroslobodclass;
  }

  public QueryDataSet getQueryDataSet() {
    return caroslobod;
  }

  public Caroslobod() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    caroslobodLOCK.setCaption("Status zauzetosti");
    caroslobodLOCK.setColumnName("LOCK");
    caroslobodLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    caroslobodLOCK.setPrecision(1);
    caroslobodLOCK.setTableName("CAROSLOBOD");
    caroslobodLOCK.setServerColumnName("LOCK");
    caroslobodLOCK.setSqlType(1);
    caroslobodLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    caroslobodLOCK.setDefault("N");
    caroslobodAKTIV.setCaption("Aktivan - neaktivan");
    caroslobodAKTIV.setColumnName("AKTIV");
    caroslobodAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    caroslobodAKTIV.setPrecision(1);
    caroslobodAKTIV.setTableName("CAROSLOBOD");
    caroslobodAKTIV.setServerColumnName("AKTIV");
    caroslobodAKTIV.setSqlType(1);
    caroslobodAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    caroslobodAKTIV.setDefault("D");
    caroslobodCOSLO.setCaption("Šifra oslobo\u0111enja");
    caroslobodCOSLO.setColumnName("COSLO");
    caroslobodCOSLO.setDataType(com.borland.dx.dataset.Variant.STRING);
    caroslobodCOSLO.setPrecision(10);
    caroslobodCOSLO.setRowId(true);
    caroslobodCOSLO.setTableName("CAROSLOBOD");
    caroslobodCOSLO.setServerColumnName("COSLO");
    caroslobodCOSLO.setSqlType(1);
    caroslobodNAZIV.setCaption("Naziv");
    caroslobodNAZIV.setColumnName("NAZIV");
    caroslobodNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    caroslobodNAZIV.setPrecision(100);
    caroslobodNAZIV.setTableName("CAROSLOBOD");
    caroslobodNAZIV.setServerColumnName("NAZIV");
    caroslobodNAZIV.setSqlType(1);
    caroslobodNAZIV.setWidth(30);
    caroslobod.setResolver(dm.getQresolver());
    caroslobod.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Caroslobod", null, true, Load.ALL));
    setColumns(new Column[] {caroslobodLOCK, caroslobodAKTIV, caroslobodCOSLO, caroslobodNAZIV});
  }

  public void setall() {

    ddl.create("Caroslobod")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("coslo", 10, true)
       .addChar("naziv", 100)
       .addPrimaryKey("coslo");


    Naziv = "Caroslobod";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"coslo"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
