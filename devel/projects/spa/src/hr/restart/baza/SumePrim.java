/****license*****************************************************************
**   file: SumePrim.java
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



public class SumePrim extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static SumePrim SumePrimclass;

  QueryDataSet spr = new raDataSet();

  Column sprCVRP = new Column();
  Column sprCSUME = new Column();

  public static SumePrim getDataModule() {
    if (SumePrimclass == null) {
      SumePrimclass = new SumePrim();
    }
    return SumePrimclass;
  }

  public QueryDataSet getQueryDataSet() {
    return spr;
  }

  public SumePrim() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    sprCVRP.setCaption("Oznaka vrste primanja");
    sprCVRP.setColumnName("CVRP");
    sprCVRP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    sprCVRP.setPrecision(3);
    sprCVRP.setRowId(true);
    sprCVRP.setTableName("SUMEPRIM");
    sprCVRP.setServerColumnName("CVRP");
    sprCVRP.setSqlType(5);
    sprCVRP.setWidth(3);
    sprCSUME.setCaption("Oznaka sume");
    sprCSUME.setColumnName("CSUME");
    sprCSUME.setDataType(com.borland.dx.dataset.Variant.INT);
    sprCSUME.setPrecision(5);
    sprCSUME.setRowId(true);
    sprCSUME.setTableName("SUMEPRIM");
    sprCSUME.setServerColumnName("CSUME");
    sprCSUME.setSqlType(4);
    sprCSUME.setWidth(5);
    spr.setResolver(dm.getQresolver());
    spr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from SumePrim", null, true, Load.ALL));
 setColumns(new Column[] {sprCVRP, sprCSUME});
  }

  public void setall() {

    ddl.create("SumePrim")
       .addShort("cvrp", 3, true)
       .addInteger("csume", 5, true)
       .addPrimaryKey("cvrp,csume");


    Naziv = "SumePrim";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
