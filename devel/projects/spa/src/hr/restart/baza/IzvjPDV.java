/****license*****************************************************************
**   file: IzvjPDV.java
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



public class IzvjPDV extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static IzvjPDV IzvjPDVclass;

  QueryDataSet izp = new raDataSet();

  Column izpCIZ = new Column();
  Column izpOPIS = new Column();

  public static IzvjPDV getDataModule() {
    if (IzvjPDVclass == null) {
      IzvjPDVclass = new IzvjPDV();
    }
    return IzvjPDVclass;
  }

  public QueryDataSet getQueryDataSet() {
    return izp;
  }

  public IzvjPDV() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    izpCIZ.setCaption("Oznaka stavke obr.PDV");
    izpCIZ.setColumnName("CIZ");
    izpCIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    izpCIZ.setPrecision(10);
    izpCIZ.setRowId(true);
    izpCIZ.setTableName("IZVJPDV");
    izpCIZ.setServerColumnName("CIZ");
    izpCIZ.setSqlType(1);
    izpOPIS.setCaption("Opis");
    izpOPIS.setColumnName("OPIS");
    izpOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    izpOPIS.setPrecision(100);
    izpOPIS.setTableName("IZVJPDV");
    izpOPIS.setServerColumnName("OPIS");
    izpOPIS.setSqlType(1);
    izpOPIS.setWidth(30);
    izp.setResolver(dm.getQresolver());
    izp.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from IzvjPDV", null, true, Load.ALL));
 setColumns(new Column[] {izpCIZ, izpOPIS});
  }

  public void setall() {

    ddl.create("IzvjPDV")
       .addChar("ciz", 10, true)
       .addChar("opis", 100)
       .addPrimaryKey("ciz");


    Naziv = "IzvjPDV";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
