/****license*****************************************************************
**   file: Fizosobe.java
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



public class Fizosobe extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Fizosobe Fizosobeclass;

  QueryDataSet fizosobe = new QueryDataSet();

  Column fizosobeLOCK = new Column();
  Column fizosobeAKTIV = new Column();
  Column fizosobeCSIFIZO = new Column();
  Column fizosobeNAZIV = new Column();

  public static Fizosobe getDataModule() {
    if (Fizosobeclass == null) {
      Fizosobeclass = new Fizosobe();
    }
    return Fizosobeclass;
  }

  public QueryDataSet getQueryDataSet() {
    return fizosobe;
  }

  public Fizosobe() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    fizosobeLOCK.setCaption("Status zauzetosti");
    fizosobeLOCK.setColumnName("LOCK");
    fizosobeLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    fizosobeLOCK.setPrecision(1);
    fizosobeLOCK.setTableName("FIZOSOBE");
    fizosobeLOCK.setServerColumnName("LOCK");
    fizosobeLOCK.setSqlType(1);
    fizosobeLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    fizosobeLOCK.setDefault("N");
    fizosobeAKTIV.setCaption("Aktivan - neaktivan");
    fizosobeAKTIV.setColumnName("AKTIV");
    fizosobeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    fizosobeAKTIV.setPrecision(1);
    fizosobeAKTIV.setTableName("FIZOSOBE");
    fizosobeAKTIV.setServerColumnName("AKTIV");
    fizosobeAKTIV.setSqlType(1);
    fizosobeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    fizosobeAKTIV.setDefault("D");
    fizosobeCSIFIZO.setCaption("Šifra fizi\u010Dke osobe");
    fizosobeCSIFIZO.setColumnName("CSIFIZO");
    fizosobeCSIFIZO.setDataType(com.borland.dx.dataset.Variant.STRING);
    fizosobeCSIFIZO.setPrecision(10);
    fizosobeCSIFIZO.setRowId(true);
    fizosobeCSIFIZO.setTableName("FIZOSOBE");
    fizosobeCSIFIZO.setServerColumnName("CSIFIZO");
    fizosobeCSIFIZO.setSqlType(1);
    fizosobeNAZIV.setCaption("Naziv");
    fizosobeNAZIV.setColumnName("NAZIV");
    fizosobeNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    fizosobeNAZIV.setPrecision(80);
    fizosobeNAZIV.setTableName("FIZOSOBE");
    fizosobeNAZIV.setServerColumnName("NAZIV");
    fizosobeNAZIV.setSqlType(1);
    fizosobeNAZIV.setWidth(30);
    fizosobe.setResolver(dm.getQresolver());
    fizosobe.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Fizosobe", null, true, Load.ALL));
    setColumns(new Column[] {fizosobeLOCK, fizosobeAKTIV, fizosobeCSIFIZO, fizosobeNAZIV});
  }

  public void setall() {

    ddl.create("Fizosobe")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("csifizo", 10, true)
       .addChar("naziv", 80)
       .addPrimaryKey("csifizo");


    Naziv = "Fizosobe";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"csifizo"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
