/****license*****************************************************************
**   file: Repgk.java
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



public class Repgk extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Repgk Repgkclass;

  QueryDataSet repgk = new QueryDataSet();

  Column repgkLOKK = new Column();
  Column repgkAKTIV = new Column();
  Column repgkCREPGK = new Column();
  Column repgkTITLE = new Column();
  Column repgkHEADER1 = new Column();
  Column repgkHEADER2 = new Column();
  Column repgkHEADER3 = new Column();
  Column repgkPARAMS = new Column();

  public static Repgk getDataModule() {
    if (Repgkclass == null) {
      Repgkclass = new Repgk();
    }
    return Repgkclass;
  }

  public QueryDataSet getQueryDataSet() {
    return repgk;
  }

  public Repgk() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    repgkLOKK.setCaption("Status zauzetosti");
    repgkLOKK.setColumnName("LOKK");
    repgkLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    repgkLOKK.setPrecision(1);
    repgkLOKK.setTableName("REPGK");
    repgkLOKK.setServerColumnName("LOKK");
    repgkLOKK.setSqlType(1);
    repgkLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    repgkLOKK.setDefault("N");
    repgkAKTIV.setCaption("Aktivan - neaktivan");
    repgkAKTIV.setColumnName("AKTIV");
    repgkAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    repgkAKTIV.setPrecision(1);
    repgkAKTIV.setTableName("REPGK");
    repgkAKTIV.setServerColumnName("AKTIV");
    repgkAKTIV.setSqlType(1);
    repgkAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    repgkAKTIV.setDefault("D");
    repgkCREPGK.setCaption("Oznaka");
    repgkCREPGK.setColumnName("CREPGK");
    repgkCREPGK.setDataType(com.borland.dx.dataset.Variant.INT);
    repgkCREPGK.setRowId(true);
    repgkCREPGK.setTableName("REPGK");
    repgkCREPGK.setServerColumnName("CREPGK");
    repgkCREPGK.setSqlType(4);
    repgkCREPGK.setWidth(8);
    repgkTITLE.setCaption("Naslov");
    repgkTITLE.setColumnName("TITLE");
    repgkTITLE.setDataType(com.borland.dx.dataset.Variant.STRING);
    repgkTITLE.setPrecision(200);
    repgkTITLE.setTableName("REPGK");
    repgkTITLE.setServerColumnName("TITLE");
    repgkTITLE.setSqlType(1);
    repgkTITLE.setWidth(30);
    repgkHEADER1.setCaption("Zaglavlje 1. iznosa");
    repgkHEADER1.setColumnName("HEADER1");
    repgkHEADER1.setDataType(com.borland.dx.dataset.Variant.STRING);
    repgkHEADER1.setPrecision(50);
    repgkHEADER1.setTableName("REPGK");
    repgkHEADER1.setServerColumnName("HEADER1");
    repgkHEADER1.setSqlType(1);
    repgkHEADER1.setWidth(30);
    repgkHEADER2.setCaption("Zaglavlje 2. iznosa");
    repgkHEADER2.setColumnName("HEADER2");
    repgkHEADER2.setDataType(com.borland.dx.dataset.Variant.STRING);
    repgkHEADER2.setPrecision(50);
    repgkHEADER2.setTableName("REPGK");
    repgkHEADER2.setServerColumnName("HEADER2");
    repgkHEADER2.setSqlType(1);
    repgkHEADER2.setWidth(30);
    repgkHEADER3.setCaption("Zaglavlje 3. iznosa");
    repgkHEADER3.setColumnName("HEADER3");
    repgkHEADER3.setDataType(com.borland.dx.dataset.Variant.STRING);
    repgkHEADER3.setPrecision(50);
    repgkHEADER3.setTableName("REPGK");
    repgkHEADER3.setServerColumnName("HEADER3");
    repgkHEADER3.setSqlType(1);
    repgkHEADER3.setWidth(30);
    repgkPARAMS.setCaption("Parametri");
    repgkPARAMS.setColumnName("PARAMS");
    repgkPARAMS.setDataType(com.borland.dx.dataset.Variant.STRING);
    repgkPARAMS.setPrecision(20);
    repgkPARAMS.setTableName("REPGK");
    repgkPARAMS.setServerColumnName("PARAMS");
    repgkPARAMS.setSqlType(1);
    repgk.setResolver(dm.getQresolver());
    repgk.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Repgk", null, true, Load.ALL));
    setColumns(new Column[] {repgkLOKK, repgkAKTIV, repgkCREPGK, repgkTITLE, repgkHEADER1, repgkHEADER2, repgkHEADER3, repgkPARAMS});
  }

  public void setall() {

    ddl.create("Repgk")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("crepgk", 8, true)
       .addChar("title", 200)
       .addChar("header1", 50)
       .addChar("header2", 50)
       .addChar("header3", 50)
       .addChar("params", 20)
       .addPrimaryKey("crepgk");


    Naziv = "Repgk";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"crepgk"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
