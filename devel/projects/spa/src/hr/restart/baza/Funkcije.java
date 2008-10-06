/****license*****************************************************************
**   file: Funkcije.java
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



public class Funkcije extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Funkcije Funkcijeclass;

  QueryDataSet func = new raDataSet();

  Column funcLOKK = new Column();
  Column funcAKTIV = new Column();
  Column funcCFUNC = new Column();
  Column funcOPISFUNC = new Column();
  Column funcAPP = new Column();

  public static Funkcije getDataModule() {
    if (Funkcijeclass == null) {
      Funkcijeclass = new Funkcije();
    }
    return Funkcijeclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return func;
  }

  public Funkcije() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    funcLOKK.setCaption("Status zauzetosti");
    funcLOKK.setColumnName("LOKK");
    funcLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    funcLOKK.setPrecision(1);
    funcLOKK.setTableName("FUNKCIJE");
    funcLOKK.setServerColumnName("LOKK");
    funcLOKK.setSqlType(1);
    funcLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    funcLOKK.setDefault("N");
    funcAKTIV.setCaption("Aktivan - neaktivan");
    funcAKTIV.setColumnName("AKTIV");
    funcAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    funcAKTIV.setPrecision(1);
    funcAKTIV.setTableName("FUNKCIJE");
    funcAKTIV.setServerColumnName("AKTIV");
    funcAKTIV.setSqlType(1);
    funcAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    funcAKTIV.setDefault("D");
    funcCFUNC.setCaption("Oznaka");
    funcCFUNC.setColumnName("CFUNC");
    funcCFUNC.setDataType(com.borland.dx.dataset.Variant.STRING);
    funcCFUNC.setPrecision(20);
    funcCFUNC.setRowId(true);
    funcCFUNC.setTableName("FUNKCIJE");
    funcCFUNC.setServerColumnName("CFUNC");
    funcCFUNC.setSqlType(1);
    funcOPISFUNC.setCaption("Naziv");
    funcOPISFUNC.setColumnName("OPISFUNC");
    funcOPISFUNC.setDataType(com.borland.dx.dataset.Variant.STRING);
    funcOPISFUNC.setPrecision(50);
    funcOPISFUNC.setTableName("FUNKCIJE");
    funcOPISFUNC.setServerColumnName("OPISFUNC");
    funcOPISFUNC.setSqlType(1);
    funcAPP.setCaption("Aplikacija");
    funcAPP.setColumnName("APP");
    funcAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    funcAPP.setPrecision(10);
    funcAPP.setTableName("FUNKCIJE");
    funcAPP.setServerColumnName("APP");
    funcAPP.setRowId(true);
    funcAPP.setSqlType(1);
    func.setResolver(dm.getQresolver());
    func.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from Funkcije", null, true, Load.ALL));
 setColumns(new Column[] {funcLOKK, funcAKTIV, funcCFUNC, funcOPISFUNC, funcAPP});
  }

  public void setall() {

    ddl.create("Funkcije")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cfunc", 20, true)
       .addChar("opisfunc", 50)
       .addChar("app", 10, true)
       .addPrimaryKey("cfunc,app");


    Naziv = "Funkcije";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
