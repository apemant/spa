/****license*****************************************************************
**   file: Telemark.java
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



public class Telemark extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Telemark Telemarkclass;

  QueryDataSet tel = new raDataSet();
  QueryDataSet telaktiv = new raDataSet();

//  Column telLOKK = new Column();
//  Column telAKTIV = new Column();
//  Column telCTEL = new Column();
//  Column telIME = new Column();

  public static Telemark getDataModule() {
    if (Telemarkclass == null) {
      Telemarkclass = new Telemark();
    }
    return Telemarkclass;
  }

  public QueryDataSet getQueryDataSet() {
    return tel;
  }

  public QueryDataSet getAktiv() {
    return telaktiv;
  }

  public Telemark() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    initModule();
//    telLOKK.setCaption("Lokk");
//    telLOKK.setColumnName("LOKK");
//    telLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
//    telLOKK.setPrecision(1);
//    telLOKK.setTableName("TELEMARK");
//    telLOKK.setServerColumnName("LOKK");
//    telLOKK.setSqlType(1);
//    telLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    telLOKK.setDefault("N");
//    telAKTIV.setCaption("Aktiv");
//    telAKTIV.setColumnName("AKTIV");
//    telAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
//    telAKTIV.setPrecision(1);
//    telAKTIV.setTableName("TELEMARK");
//    telAKTIV.setServerColumnName("AKTIV");
//    telAKTIV.setSqlType(1);
//    telAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    telAKTIV.setDefault("D");
//    telCTEL.setCaption("Sifra");
//    telCTEL.setColumnName("CTEL");
//    telCTEL.setDataType(com.borland.dx.dataset.Variant.INT);
//    telCTEL.setRowId(true);
//    telCTEL.setTableName("TELEMARK");
//    telCTEL.setServerColumnName("CTEL");
//    telCTEL.setSqlType(4);
//    telCTEL.setWidth(6);
//    telIME.setCaption("Ime i prezime");
//    telIME.setColumnName("IME");
//    telIME.setDataType(com.borland.dx.dataset.Variant.STRING);
//    telIME.setPrecision(50);
//    telIME.setTableName("TELEMARK");
//    telIME.setServerColumnName("IME");
//    telIME.setSqlType(1);
//    telIME.setWidth(30);
//    tel.setResolver(dm.getQresolver());
//    tel.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Telemark", null, true, Load.ALL));
//    setColumns(new Column[] {telLOKK, telAKTIV, telCTEL, telIME});

    createFilteredDataSet(telaktiv, "aktiv='D'");
  }

//  public void setall() {
//
//    ddl.create("Telemark")
//       .addChar("lokk", 1, "N")
//       .addChar("aktiv", 1, "D")
//       .addInteger("ctel", 6, true)
//       .addChar("ime", 50)
//       .addPrimaryKey("ctel");
//
//
//    Naziv = "Telemark";
//
//    SqlDefTabela = ddl.getCreateTableString();
//
//    String[] idx = new String[] {};
//    String[] uidx = new String[] {};
//    DefIndex = ddl.getIndices(idx, uidx);
//    NaziviIdx = ddl.getIndexNames(idx, uidx);
//  }
}
