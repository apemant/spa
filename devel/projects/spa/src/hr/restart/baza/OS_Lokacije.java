/****license*****************************************************************
**   file: OS_Lokacije.java
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



public class OS_Lokacije extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Lokacije OS_Lokacijeclass;

  QueryDataSet oslok = new raDataSet();

  Column oslokLOKK = new Column();
  Column oslokAKTIV = new Column();
  Column oslokCORG = new Column();
  Column oslokCOBJEKT = new Column();
  Column oslokCLOKACIJE = new Column();
  Column oslokNAZLOKACIJE = new Column();

  public static OS_Lokacije getDataModule() {
    if (OS_Lokacijeclass == null) {
      OS_Lokacijeclass = new OS_Lokacije();
    }
    return OS_Lokacijeclass;
  }

  public QueryDataSet getQueryDataSet() {
    return oslok;
  }

  public OS_Lokacije() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    oslokLOKK.setCaption("Status zauzetosti");
    oslokLOKK.setColumnName("LOKK");
    oslokLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    oslokLOKK.setPrecision(1);
    oslokLOKK.setTableName("OS_LOKACIJE");
    oslokLOKK.setServerColumnName("LOKK");
    oslokLOKK.setSqlType(1);
    oslokLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    oslokLOKK.setDefault("N");
    oslokAKTIV.setCaption("Aktivan - neaktivan");
    oslokAKTIV.setColumnName("AKTIV");
    oslokAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    oslokAKTIV.setPrecision(1);
    oslokAKTIV.setTableName("OS_LOKACIJE");
    oslokAKTIV.setServerColumnName("AKTIV");
    oslokAKTIV.setSqlType(1);
    oslokAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    oslokAKTIV.setDefault("D");
    oslokCORG.setCaption("OJ");
    oslokCORG.setColumnName("CORG");
    oslokCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    oslokCORG.setPrecision(12);
    oslokCORG.setRowId(true);
    oslokCORG.setTableName("OS_LOKACIJE");
    oslokCORG.setServerColumnName("CORG");
    oslokCORG.setSqlType(1);
    oslokCORG.setWidth(6);
    oslokCOBJEKT.setCaption("Objekt");
    oslokCOBJEKT.setColumnName("COBJEKT");
    oslokCOBJEKT.setDataType(com.borland.dx.dataset.Variant.STRING);
    oslokCOBJEKT.setPrecision(6);
    oslokCOBJEKT.setRowId(true);
    oslokCOBJEKT.setTableName("OS_LOKACIJE");
    oslokCOBJEKT.setServerColumnName("COBJEKT");
    oslokCOBJEKT.setSqlType(1);
    oslokCLOKACIJE.setCaption("Šifra");
    oslokCLOKACIJE.setColumnName("CLOKACIJE");
    oslokCLOKACIJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    oslokCLOKACIJE.setPrecision(6);
    oslokCLOKACIJE.setRowId(true);
    oslokCLOKACIJE.setTableName("OS_LOKACIJE");
    oslokCLOKACIJE.setServerColumnName("CLOKACIJE");
    oslokCLOKACIJE.setSqlType(1);
    oslokNAZLOKACIJE.setCaption("Naziv");
    oslokNAZLOKACIJE.setColumnName("NAZLOKACIJE");
    oslokNAZLOKACIJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    oslokNAZLOKACIJE.setPrecision(50);
    oslokNAZLOKACIJE.setTableName("OS_LOKACIJE");
    oslokNAZLOKACIJE.setServerColumnName("NAZLOKACIJE");
    oslokNAZLOKACIJE.setSqlType(1);
    oslokNAZLOKACIJE.setWidth(30);
    oslok.setResolver(dm.getQresolver());
    oslok.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Lokacije", null, true, Load.ALL));
 setColumns(new Column[] {oslokLOKK, oslokAKTIV, oslokCORG, oslokCOBJEKT, oslokCLOKACIJE, oslokNAZLOKACIJE});
  }

  public void setall() {

    ddl.create("OS_Lokacije")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addChar("cobjekt", 6, true)
       .addChar("clokacije", 6, true)
       .addChar("nazlokacije", 50)
       .addPrimaryKey("corg,cobjekt,clokacije");


    Naziv = "OS_Lokacije";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cobjekt", "clokacije"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
