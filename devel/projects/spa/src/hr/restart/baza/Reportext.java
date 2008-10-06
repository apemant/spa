/****license*****************************************************************
**   file: Reportext.java
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



public class Reportext extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Reportext Reportextclass;

  QueryDataSet rex = new raDataSet();

  Column rexIME = new Column();
  Column rexNASLOV = new Column();
  Column rexURL = new Column();
  Column rexAPP = new Column();

  public static Reportext getDataModule() {
    if (Reportextclass == null) {
      Reportextclass = new Reportext();
    }
    return Reportextclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rex;
  }

  public Reportext() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    rexIME.setCaption("Ime");
    rexIME.setColumnName("IME");
    rexIME.setDataType(com.borland.dx.dataset.Variant.STRING);
    rexIME.setPrecision(12);
    rexIME.setRowId(true);
    rexIME.setTableName("REPORTEXT");
    rexIME.setServerColumnName("IME");
    rexIME.setSqlType(1);
    rexNASLOV.setCaption("Izvještaj");
    rexNASLOV.setColumnName("NASLOV");
    rexNASLOV.setDataType(com.borland.dx.dataset.Variant.STRING);
    rexNASLOV.setPrecision(80);
    rexNASLOV.setTableName("REPORTEXT");
    rexNASLOV.setServerColumnName("NASLOV");
    rexNASLOV.setSqlType(1);
    rexNASLOV.setWidth(40);
    rexURL.setCaption("URL");
    rexURL.setColumnName("URL");
    rexURL.setDataType(com.borland.dx.dataset.Variant.STRING);
    rexURL.setPrecision(150);
    rexURL.setTableName("REPORTEXT");
    rexURL.setServerColumnName("URL");
    rexURL.setSqlType(1);
    rexURL.setWidth(40);
    rexAPP.setCaption("Aplikacija");
    rexAPP.setColumnName("APP");
    rexAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    rexAPP.setPrecision(12);
    rexAPP.setTableName("REPORTEXT");
    rexAPP.setServerColumnName("APP");
    rexAPP.setSqlType(1);
    rex.setResolver(dm.getQresolver());
    rex.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Reportext", null, true, Load.ALL));
 setColumns(new Column[] {rexIME, rexNASLOV, rexURL, rexAPP});
  }

  public void setall() {

    ddl.create("Reportext")
       .addChar("ime", 12, true)
       .addChar("naslov", 80)
       .addChar("url", 150)
       .addChar("app", 12)
       .addPrimaryKey("ime");


    Naziv = "Reportext";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
