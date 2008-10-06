/****license*****************************************************************
**   file: PlZnacRadData.java
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



public class PlZnacRadData extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static PlZnacRadData PlZnacRadDataclass;

  QueryDataSet plznacraddata = new raDataSet();

  Column plznacraddataCRADNIK = new Column();
  Column plznacraddataCZNAC = new Column();
  Column plznacraddataVRI = new Column();

  public static PlZnacRadData getDataModule() {
    if (PlZnacRadDataclass == null) {
      PlZnacRadDataclass = new PlZnacRadData();
    }
    return PlZnacRadDataclass;
  }

  public QueryDataSet getQueryDataSet() {
    return plznacraddata;
  }

  public PlZnacRadData() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    plznacraddataCRADNIK.setCaption("Radnik");
    plznacraddataCRADNIK.setColumnName("CRADNIK");
    plznacraddataCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    plznacraddataCRADNIK.setPrecision(6);
    plznacraddataCRADNIK.setRowId(true);
    plznacraddataCRADNIK.setTableName("PLZNACRADDATA");
    plznacraddataCRADNIK.setServerColumnName("CRADNIK");
    plznacraddataCRADNIK.setSqlType(1);
    plznacraddataCZNAC.setCaption("Zna\u010Dajka");
    plznacraddataCZNAC.setColumnName("CZNAC");
    plznacraddataCZNAC.setDataType(com.borland.dx.dataset.Variant.SHORT);
    plznacraddataCZNAC.setPrecision(4);
    plznacraddataCZNAC.setRowId(true);
    plznacraddataCZNAC.setTableName("PLZNACRADDATA");
    plznacraddataCZNAC.setServerColumnName("CZNAC");
    plznacraddataCZNAC.setSqlType(5);
    plznacraddataCZNAC.setWidth(4);
    plznacraddataVRI.setCaption("Vrijednost");
    plznacraddataVRI.setColumnName("VRI");
    plznacraddataVRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    plznacraddataVRI.setPrecision(50);
    plznacraddataVRI.setTableName("PLZNACRADDATA");
    plznacraddataVRI.setServerColumnName("VRI");
    plznacraddataVRI.setSqlType(1);
    plznacraddataVRI.setWidth(30);
    plznacraddata.setResolver(dm.getQresolver());
    plznacraddata.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from PlZnacRadData", null, true, Load.ALL));
    setColumns(new Column[] {plznacraddataCRADNIK, plznacraddataCZNAC, plznacraddataVRI});
  }

  public void setall() {

    ddl.create("PlZnacRadData")
       .addChar("cradnik", 6, true)
       .addShort("cznac", 4, true)
       .addChar("vri", 50)
       .addPrimaryKey("cradnik,cznac");


    Naziv = "PlZnacRadData";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
