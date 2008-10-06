/****license*****************************************************************
**   file: StIzvjPDV.java
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



public class StIzvjPDV extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static StIzvjPDV StIzvjPDVclass;

  QueryDataSet stizp = new QueryDataSet();

  Column stizpCIZ = new Column();
  Column stizpCKNJIGE = new Column();
  Column stizpCKOLONE = new Column();
  Column stizpURAIRA = new Column();

  public static StIzvjPDV getDataModule() {
    if (StIzvjPDVclass == null) {
      StIzvjPDVclass = new StIzvjPDV();
    }
    return StIzvjPDVclass;
  }

  public QueryDataSet getQueryDataSet() {
    return stizp;
  }

  public StIzvjPDV() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    stizpCIZ.setCaption("Oznaka stavke obr.PDV");
    stizpCIZ.setColumnName("CIZ");
    stizpCIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    stizpCIZ.setPrecision(10);
    stizpCIZ.setRowId(true);
    stizpCIZ.setTableName("STIZVJPDV");
    stizpCIZ.setServerColumnName("CIZ");
    stizpCIZ.setSqlType(1);
    stizpCKNJIGE.setCaption("Oznaka knjige");
    stizpCKNJIGE.setColumnName("CKNJIGE");
    stizpCKNJIGE.setDataType(com.borland.dx.dataset.Variant.STRING);
    stizpCKNJIGE.setPrecision(5);
    stizpCKNJIGE.setRowId(true);
    stizpCKNJIGE.setTableName("STIZVJPDV");
    stizpCKNJIGE.setServerColumnName("CKNJIGE");
    stizpCKNJIGE.setSqlType(1);
    stizpCKOLONE.setCaption("Broj kolone");
    stizpCKOLONE.setColumnName("CKOLONE");
    stizpCKOLONE.setDataType(com.borland.dx.dataset.Variant.SHORT);
    stizpCKOLONE.setPrecision(2);
    stizpCKOLONE.setRowId(true);
    stizpCKOLONE.setTableName("STIZVJPDV");
    stizpCKOLONE.setServerColumnName("CKOLONE");
    stizpCKOLONE.setSqlType(5);
    stizpCKOLONE.setWidth(2);
    stizpURAIRA.setCaption("Indikator URA/IRA");
    stizpURAIRA.setColumnName("URAIRA");
    stizpURAIRA.setDataType(com.borland.dx.dataset.Variant.STRING);
    stizpURAIRA.setPrecision(1);
    stizpURAIRA.setRowId(true);
    stizpURAIRA.setTableName("STIZVJPDV");
    stizpURAIRA.setServerColumnName("URAIRA");
    stizpURAIRA.setSqlType(1);
    stizp.setResolver(dm.getQresolver());
    stizp.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from StIzvjPDV", null, true, Load.ALL));
 setColumns(new Column[] {stizpCIZ, stizpCKNJIGE, stizpCKOLONE, stizpURAIRA});
  }

  public void setall() {

    ddl.create("StIzvjPDV")
       .addChar("ciz", 10, true)
       .addChar("cknjige", 5, true)
       .addShort("ckolone", 2, true)
       .addChar("uraira", 1, true)
       .addPrimaryKey("ciz,cknjige,ckolone,uraira");


    Naziv = "StIzvjPDV";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"ciz", "ckolone"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
