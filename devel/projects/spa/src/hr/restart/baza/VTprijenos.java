/****license*****************************************************************
**   file: VTprijenos.java
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



public class VTprijenos extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static VTprijenos VTprijenosclass;

  QueryDataSet vtprijenos = new raDataSet();

  Column vtprijenosKEYSRC = new Column();
  Column vtprijenosKEYDEST = new Column();

  public static VTprijenos getDataModule() {
    if (VTprijenosclass == null) {
      VTprijenosclass = new VTprijenos();
    }
    return VTprijenosclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return vtprijenos;
  }

  public VTprijenos() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vtprijenosKEYSRC.setCaption("Klju\u010D izvornog dokumenta");
    vtprijenosKEYSRC.setColumnName("KEYSRC");
    vtprijenosKEYSRC.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtprijenosKEYSRC.setPrecision(60);
    vtprijenosKEYSRC.setRowId(true);
    vtprijenosKEYSRC.setTableName("VTPRIJENOS");
    vtprijenosKEYSRC.setServerColumnName("KEYSRC");
    vtprijenosKEYSRC.setSqlType(1);
    vtprijenosKEYDEST.setCaption("Klju\u010D ciljnog dokumenta");
    vtprijenosKEYDEST.setColumnName("KEYDEST");
    vtprijenosKEYDEST.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtprijenosKEYDEST.setPrecision(60);
    vtprijenosKEYDEST.setRowId(true);
    vtprijenosKEYDEST.setTableName("VTPRIJENOS");
    vtprijenosKEYDEST.setServerColumnName("KEYDEST");
    vtprijenosKEYDEST.setSqlType(1);
    vtprijenos.setResolver(dm.getQresolver());
    vtprijenos.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from VTprijenos", null, true, Load.ALL));
 setColumns(new Column[] {vtprijenosKEYSRC, vtprijenosKEYDEST});
  }

  public void setall() {

    ddl.create("VTprijenos")
       .addChar("keysrc", 60, true)
       .addChar("keydest", 60, true)
       .addPrimaryKey("keysrc,keydest");


    Naziv = "VTprijenos";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {/*"keysrc", "keydest"*/};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
