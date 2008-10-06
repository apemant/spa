/****license*****************************************************************
**   file: Transdata.java
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



public class Transdata extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Transdata Transdataclass;

  QueryDataSet transdata = new QueryDataSet();

  Column transdataIMETAB = new Column();
  Column transdataKOLONA = new Column();
  Column transdataSDAT = new Column();
  Column transdataNDAT = new Column();

  public static Transdata getDataModule() {
    if (Transdataclass == null) {
      Transdataclass = new Transdata();
    }
    return Transdataclass;
  }

  public QueryDataSet getQueryDataSet() {
    return transdata;
  }

  public Transdata() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    transdataIMETAB.setCaption("Tablica");
    transdataIMETAB.setColumnName("IMETAB");
    transdataIMETAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    transdataIMETAB.setPrecision(20);
    transdataIMETAB.setRowId(true);
    transdataIMETAB.setTableName("TRANSDATA");
    transdataIMETAB.setServerColumnName("IMETAB");
    transdataIMETAB.setSqlType(1);
    transdataKOLONA.setCaption("Kolona");
    transdataKOLONA.setColumnName("KOLONA");
    transdataKOLONA.setDataType(com.borland.dx.dataset.Variant.STRING);
    transdataKOLONA.setPrecision(20);
    transdataKOLONA.setRowId(true);
    transdataKOLONA.setTableName("TRANSDATA");
    transdataKOLONA.setServerColumnName("KOLONA");
    transdataKOLONA.setSqlType(1);
    transdataSDAT.setCaption("Stara vrijednost");
    transdataSDAT.setColumnName("SDAT");
    transdataSDAT.setDataType(com.borland.dx.dataset.Variant.STRING);
    transdataSDAT.setPrecision(20);
    transdataSDAT.setRowId(true);
    transdataSDAT.setTableName("TRANSDATA");
    transdataSDAT.setServerColumnName("SDAT");
    transdataSDAT.setSqlType(1);
    transdataNDAT.setCaption("Nova vrijednost");
    transdataNDAT.setColumnName("NDAT");
    transdataNDAT.setDataType(com.borland.dx.dataset.Variant.STRING);
    transdataNDAT.setPrecision(20);
    transdataNDAT.setTableName("TRANSDATA");
    transdataNDAT.setServerColumnName("NDAT");
    transdataNDAT.setSqlType(1);
    transdata.setResolver(dm.getQresolver());
    transdata.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Transdata", null, true, Load.ALL));
    setColumns(new Column[] {transdataIMETAB, transdataKOLONA, transdataSDAT, transdataNDAT});
  }

  public void setall() {

    ddl.create("Transdata")
       .addChar("imetab", 20, true)
       .addChar("kolona", 20, true)
       .addChar("sdat", 20, true)
       .addChar("ndat", 20)
       .addPrimaryKey("imetab,kolona,sdat");


    Naziv = "Transdata";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
