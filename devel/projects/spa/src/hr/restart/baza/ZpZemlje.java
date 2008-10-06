/****license*****************************************************************
**   file: ZpZemlje.java
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



public class ZpZemlje extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static ZpZemlje ZpZemljeclass;

  QueryDataSet zpz = new raDataSet();
  QueryDataSet zpzaktiv = new raDataSet();

  Column zpzLOKK = new Column();
  Column zpzAKTIV = new Column();
  Column zpzCZEM = new Column();
  Column zpzNAZIVZEM = new Column();
  Column zpzOZNZEM = new Column();
  Column zpzTRGZEM = new Column();

  public static ZpZemlje getDataModule() {
    if (ZpZemljeclass == null) {
      ZpZemljeclass = new ZpZemlje();
    }
    return ZpZemljeclass;
  }

  public QueryDataSet getQueryDataSet() {
    return zpz;
  }

  public QueryDataSet getAktiv() {
    return zpzaktiv;
  }

  public ZpZemlje() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    zpzLOKK.setCaption("Status zauzetosti");
    zpzLOKK.setColumnName("LOKK");
    zpzLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    zpzLOKK.setPrecision(1);
    zpzLOKK.setTableName("ZPZEMLJE");
    zpzLOKK.setServerColumnName("LOKK");
    zpzLOKK.setSqlType(1);
    zpzLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    zpzLOKK.setDefault("N");
    zpzAKTIV.setCaption("Aktivan - neaktivan");
    zpzAKTIV.setColumnName("AKTIV");
    zpzAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    zpzAKTIV.setPrecision(1);
    zpzAKTIV.setTableName("ZPZEMLJE");
    zpzAKTIV.setServerColumnName("AKTIV");
    zpzAKTIV.setSqlType(1);
    zpzAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    zpzAKTIV.setDefault("D");
    zpzCZEM.setCaption("Šifra");
    zpzCZEM.setColumnName("CZEM");
    zpzCZEM.setDataType(com.borland.dx.dataset.Variant.STRING);
    zpzCZEM.setPrecision(3);
    zpzCZEM.setRowId(true);
    zpzCZEM.setTableName("ZPZEMLJE");
    zpzCZEM.setServerColumnName("CZEM");
    zpzCZEM.setSqlType(1);
    zpzNAZIVZEM.setCaption("Naziv");
    zpzNAZIVZEM.setColumnName("NAZIVZEM");
    zpzNAZIVZEM.setDataType(com.borland.dx.dataset.Variant.STRING);
    zpzNAZIVZEM.setPrecision(50);
    zpzNAZIVZEM.setTableName("ZPZEMLJE");
    zpzNAZIVZEM.setServerColumnName("NAZIVZEM");
    zpzNAZIVZEM.setSqlType(1);
    zpzNAZIVZEM.setWidth(30);
    zpzOZNZEM.setCaption("Oznaka");
    zpzOZNZEM.setColumnName("OZNZEM");
    zpzOZNZEM.setDataType(com.borland.dx.dataset.Variant.STRING);
    zpzOZNZEM.setPrecision(3);
    zpzOZNZEM.setTableName("ZPZEMLJE");
    zpzOZNZEM.setServerColumnName("OZNZEM");
    zpzOZNZEM.setSqlType(1);
    zpzTRGZEM.setCaption("Trgova\u010Dka oznaka");
    zpzTRGZEM.setColumnName("TRGZEM");
    zpzTRGZEM.setDataType(com.borland.dx.dataset.Variant.STRING);
    zpzTRGZEM.setPrecision(2);
    zpzTRGZEM.setTableName("ZPZEMLJE");
    zpzTRGZEM.setServerColumnName("TRGZEM");
    zpzTRGZEM.setSqlType(1);
    zpz.setResolver(dm.getQresolver());
    zpz.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from ZpZemlje", null, true, Load.ALL));
    setColumns(new Column[] {zpzLOKK, zpzAKTIV, zpzCZEM, zpzNAZIVZEM, zpzOZNZEM, zpzTRGZEM});

    createFilteredDataSet(zpzaktiv, "aktiv='D'");
  }

  public void setall() {

    ddl.create("ZpZemlje")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("czem", 3, true)
       .addChar("nazivzem", 50)
       .addChar("oznzem", 3, true)
       .addChar("trgzem", 2, true)
       .addPrimaryKey("czem");


    Naziv = "ZpZemlje";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {"oznzem", "trgzem"};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
