/****license*****************************************************************
**   file: Detrepgk.java
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



public class Detrepgk extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Detrepgk Detrepgkclass;

  QueryDataSet detrepgk = new QueryDataSet();

  Column detrepgkAKTIV = new Column();
  Column detrepgkCREPGK = new Column();
  Column detrepgkRBSREPGK = new Column();
  Column detrepgkDESCRIPTION = new Column();
  Column detrepgkKTO1 = new Column();
  Column detrepgkKTO2 = new Column();
  Column detrepgkKTO3 = new Column();
  Column detrepgkPARAMS = new Column();

  public static Detrepgk getDataModule() {
    if (Detrepgkclass == null) {
      Detrepgkclass = new Detrepgk();
    }
    return Detrepgkclass;
  }

  public QueryDataSet getQueryDataSet() {
    return detrepgk;
  }

  public Detrepgk() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    detrepgkAKTIV.setCaption("Aktivan - neaktivan");
    detrepgkAKTIV.setColumnName("AKTIV");
    detrepgkAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    detrepgkAKTIV.setPrecision(1);
    detrepgkAKTIV.setTableName("DETREPGK");
    detrepgkAKTIV.setServerColumnName("AKTIV");
    detrepgkAKTIV.setSqlType(1);
    detrepgkAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    detrepgkAKTIV.setDefault("D");
    detrepgkCREPGK.setCaption("Izvještaj");
    detrepgkCREPGK.setColumnName("CREPGK");
    detrepgkCREPGK.setDataType(com.borland.dx.dataset.Variant.INT);
    detrepgkCREPGK.setRowId(true);
    detrepgkCREPGK.setTableName("DETREPGK");
    detrepgkCREPGK.setServerColumnName("CREPGK");
    detrepgkCREPGK.setSqlType(4);
    detrepgkCREPGK.setWidth(8);
    detrepgkRBSREPGK.setCaption("Oznaka stavke izvještaja");
    detrepgkRBSREPGK.setColumnName("RBSREPGK");
    detrepgkRBSREPGK.setDataType(com.borland.dx.dataset.Variant.INT);
    detrepgkRBSREPGK.setRowId(true);
    detrepgkRBSREPGK.setTableName("DETREPGK");
    detrepgkRBSREPGK.setServerColumnName("RBSREPGK");
    detrepgkRBSREPGK.setSqlType(4);
    detrepgkRBSREPGK.setWidth(8);
    detrepgkDESCRIPTION.setCaption("Opis");
    detrepgkDESCRIPTION.setColumnName("DESCRIPTION");
    detrepgkDESCRIPTION.setDataType(com.borland.dx.dataset.Variant.STRING);
    detrepgkDESCRIPTION.setPrecision(200);
    detrepgkDESCRIPTION.setTableName("DETREPGK");
    detrepgkDESCRIPTION.setServerColumnName("DESCRIPTION");
    detrepgkDESCRIPTION.setSqlType(1);
    detrepgkDESCRIPTION.setWidth(30);
    detrepgkKTO1.setCaption("Formula za 1. iznos");
    detrepgkKTO1.setColumnName("KTO1");
    detrepgkKTO1.setDataType(com.borland.dx.dataset.Variant.STRING);
    detrepgkKTO1.setPrecision(200);
    detrepgkKTO1.setTableName("DETREPGK");
    detrepgkKTO1.setServerColumnName("KTO1");
    detrepgkKTO1.setSqlType(1);
    detrepgkKTO1.setWidth(30);
    detrepgkKTO2.setCaption("Formula za 2. iznos");
    detrepgkKTO2.setColumnName("KTO2");
    detrepgkKTO2.setDataType(com.borland.dx.dataset.Variant.STRING);
    detrepgkKTO2.setPrecision(200);
    detrepgkKTO2.setTableName("DETREPGK");
    detrepgkKTO2.setServerColumnName("KTO2");
    detrepgkKTO2.setSqlType(1);
    detrepgkKTO2.setWidth(30);
    detrepgkKTO3.setCaption("Formula za 3. iznos");
    detrepgkKTO3.setColumnName("KTO3");
    detrepgkKTO3.setDataType(com.borland.dx.dataset.Variant.STRING);
    detrepgkKTO3.setPrecision(200);
    detrepgkKTO3.setTableName("DETREPGK");
    detrepgkKTO3.setServerColumnName("KTO3");
    detrepgkKTO3.setSqlType(1);
    detrepgkKTO3.setWidth(30);
    detrepgkPARAMS.setCaption("Parametri");
    detrepgkPARAMS.setColumnName("PARAMS");
    detrepgkPARAMS.setDataType(com.borland.dx.dataset.Variant.STRING);
    detrepgkPARAMS.setPrecision(20);
    detrepgkPARAMS.setTableName("DETREPGK");
    detrepgkPARAMS.setServerColumnName("PARAMS");
    detrepgkPARAMS.setSqlType(1);
    detrepgk.setResolver(dm.getQresolver());
    detrepgk.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Detrepgk", null, true, Load.ALL));
    setColumns(new Column[] {detrepgkAKTIV, detrepgkCREPGK, detrepgkRBSREPGK, detrepgkDESCRIPTION, detrepgkKTO1, detrepgkKTO2, detrepgkKTO3, 
        detrepgkPARAMS});
  }

  public void setall() {

    ddl.create("Detrepgk")
       .addChar("aktiv", 1, "D")
       .addInteger("crepgk", 8, true)
       .addInteger("rbsrepgk", 8, true)
       .addChar("description", 200)
       .addChar("kto1", 200)
       .addChar("kto2", 200)
       .addChar("kto3", 200)
       .addChar("params", 20)
       .addPrimaryKey("crepgk,rbsrepgk");


    Naziv = "Detrepgk";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"crepgk", "rbsrepgk"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
