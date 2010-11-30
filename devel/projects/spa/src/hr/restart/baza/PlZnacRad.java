/****license*****************************************************************
**   file: PlZnacRad.java
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
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

public class PlZnacRad extends KreirDrop implements DataModule {

  private static PlZnacRad PlZnacRadclass;

  QueryDataSet plZnacRad = new QueryDataSet();
  
  
  public static PlZnacRad getDataModule() {
    if (PlZnacRadclass == null) {
      PlZnacRadclass = new PlZnacRad();
    }
    return PlZnacRadclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return plZnacRad;
  }

  public PlZnacRad() {
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
  }
  
  //hack za jpCustomAttrib
  public QueryDataSet getTempSet(String filter) {
    String filter2 = filter + " AND AKTIV!='N' ORDER BY SRT";
    return super.getTempSet(filter2);
  }
}


/* old
package hr.restart.baza;
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;



public class PlZnacRad extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static PlZnacRad PlZnacRadclass;

  QueryDataSet pzr = new raDataSet();

  Column pzrCZNAC = new Column();
  Column pzrZNACOPIS = new Column();
  Column pzrZNACTIP = new Column();
  Column pzrZNACREQ = new Column();
  Column pzrZNACDOH = new Column();
  Column pzrDOHATTR = new Column();
  Column pzrDOHCOLS = new Column();

  public static PlZnacRad getDataModule() {
    if (PlZnacRadclass == null) {
      PlZnacRadclass = new PlZnacRad();
    }
    return PlZnacRadclass;
  }

  public QueryDataSet getQueryDataSet() {
    return pzr;
  }

  public PlZnacRad() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    pzrCZNAC.setCaption("Šifra");
    pzrCZNAC.setColumnName("CZNAC");
    pzrCZNAC.setDataType(com.borland.dx.dataset.Variant.SHORT);
    pzrCZNAC.setPrecision(4);
    pzrCZNAC.setRowId(true);
    pzrCZNAC.setTableName("PLZNACRAD");
    pzrCZNAC.setServerColumnName("CZNAC");
    pzrCZNAC.setSqlType(5);
    pzrCZNAC.setWidth(4);
    pzrZNACOPIS.setCaption("Opis");
    pzrZNACOPIS.setColumnName("ZNACOPIS");
    pzrZNACOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    pzrZNACOPIS.setPrecision(50);
    pzrZNACOPIS.setTableName("PLZNACRAD");
    pzrZNACOPIS.setServerColumnName("ZNACOPIS");
    pzrZNACOPIS.setSqlType(1);
    pzrZNACOPIS.setWidth(30);
    pzrZNACTIP.setCaption("Tip");
    pzrZNACTIP.setColumnName("ZNACTIP");
    pzrZNACTIP.setDataType(com.borland.dx.dataset.Variant.STRING);
    pzrZNACTIP.setPrecision(1);
    pzrZNACTIP.setTableName("PLZNACRAD");
    pzrZNACTIP.setServerColumnName("ZNACTIP");
    pzrZNACTIP.setSqlType(1);
    pzrZNACREQ.setCaption("Obavezan unos");
    pzrZNACREQ.setColumnName("ZNACREQ");
    pzrZNACREQ.setDataType(com.borland.dx.dataset.Variant.STRING);
    pzrZNACREQ.setPrecision(1);
    pzrZNACREQ.setTableName("PLZNACRAD");
    pzrZNACREQ.setServerColumnName("ZNACREQ");
    pzrZNACREQ.setSqlType(1);
    pzrZNACREQ.setDefault("N");
    pzrZNACDOH.setCaption("Dohvat");
    pzrZNACDOH.setColumnName("ZNACDOH");
    pzrZNACDOH.setDataType(com.borland.dx.dataset.Variant.STRING);
    pzrZNACDOH.setPrecision(1);
    pzrZNACDOH.setTableName("PLZNACRAD");
    pzrZNACDOH.setServerColumnName("ZNACDOH");
    pzrZNACDOH.setSqlType(1);
    pzrZNACDOH.setDefault("N");
    pzrDOHATTR.setCaption("Parametri dohvata");
    pzrDOHATTR.setColumnName("DOHATTR");
    pzrDOHATTR.setDataType(com.borland.dx.dataset.Variant.STRING);
    pzrDOHATTR.setPrecision(200);
    pzrDOHATTR.setTableName("PLZNACRAD");
    pzrDOHATTR.setServerColumnName("DOHATTR");
    pzrDOHATTR.setSqlType(1);
    pzrDOHATTR.setWidth(30);
    pzrDOHATTR.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pzrDOHCOLS.setCaption("Kolone dohvata");
    pzrDOHCOLS.setColumnName("DOHCOLS");
    pzrDOHCOLS.setDataType(com.borland.dx.dataset.Variant.STRING);
    pzrDOHCOLS.setPrecision(100);
    pzrDOHCOLS.setTableName("PLZNACRAD");
    pzrDOHCOLS.setServerColumnName("DOHCOLS");
    pzrDOHCOLS.setSqlType(1);
    pzrDOHCOLS.setWidth(30);
    pzrDOHCOLS.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    pzr.setResolver(dm.getQresolver());
    pzr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from PlZnacRad", null, true, Load.ALL));
    setColumns(new Column[] {pzrCZNAC, pzrZNACOPIS, pzrZNACTIP, pzrZNACREQ, pzrZNACDOH, pzrDOHATTR, pzrDOHCOLS});
  }

  public void setall() {

    ddl.create("PlZnacRad")
       .addShort("cznac", 4, true)
       .addChar("znacopis", 50)
       .addChar("znactip", 1)
       .addChar("znacreq", 1, "N")
       .addChar("znacdoh", 1, "N")
       .addChar("dohattr", 200)
       .addChar("dohcols", 100)
       .addPrimaryKey("cznac");


    Naziv = "PlZnacRad";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
*/