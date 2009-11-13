/****license*****************************************************************
**   file: Zupanije.java
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



public class Zupanije extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Zupanije Zupanijeclass;
  QueryDataSet zup = new raDataSet();

  Column zupLOKK = new Column();
  Column zupAKTIV = new Column();
  Column zupCZUP = new Column();
  Column zupNAZIVZUP = new Column();
  Column zupPARAMETRI = new Column();

  public static Zupanije getDataModule() {
    if (Zupanijeclass == null) {
      Zupanijeclass = new Zupanije();
    }
    return Zupanijeclass;
  }

  public QueryDataSet getQueryDataSet() {
    return zup;
  }

  public Zupanije() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    zupLOKK.setCaption("Status zauzetosti");
    zupLOKK.setColumnName("LOKK");
    zupLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    zupLOKK.setPrecision(1);
    zupLOKK.setTableName("ZUPANIJE");
    zupLOKK.setServerColumnName("LOKK");
    zupLOKK.setSqlType(1);
    zupLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    zupLOKK.setDefault("N");
    zupAKTIV.setCaption("Aktivan - neaktivan");
    zupAKTIV.setColumnName("AKTIV");
    zupAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    zupAKTIV.setPrecision(1);
    zupAKTIV.setTableName("ZUPANIJE");
    zupAKTIV.setServerColumnName("AKTIV");
    zupAKTIV.setSqlType(1);
    zupAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    zupAKTIV.setDefault("D");
    zupCZUP.setCaption("Oznaka");
    zupCZUP.setColumnName("CZUP");
    zupCZUP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    zupCZUP.setPrecision(3);
    zupCZUP.setRowId(true);
    zupCZUP.setTableName("ZUPANIJE");
    zupCZUP.setServerColumnName("CZUP");
    zupCZUP.setSqlType(5);
    zupCZUP.setWidth(5);
    zupNAZIVZUP.setCaption("Naziv");
    zupNAZIVZUP.setColumnName("NAZIVZUP");
    zupNAZIVZUP.setDataType(com.borland.dx.dataset.Variant.STRING);
    zupNAZIVZUP.setPrecision(50);
    zupNAZIVZUP.setTableName("ZUPANIJE");
    zupNAZIVZUP.setServerColumnName("NAZIVZUP");
    zupNAZIVZUP.setSqlType(1);
    zupNAZIVZUP.setWidth(30);
    zupPARAMETRI.setCaption("Parametri");
    zupPARAMETRI.setColumnName("PARAMETRI");
    zupPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    zupPARAMETRI.setPrecision(20);
    zupPARAMETRI.setTableName("ZUPANIJE");
    zupPARAMETRI.setServerColumnName("PARAMETRI");
    zupPARAMETRI.setSqlType(1);
    zupPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    zup.setResolver(dm.getQresolver());
    zup.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Zupanije", null, true, Load.ALL));
 setColumns(new Column[] {zupLOKK, zupAKTIV, zupCZUP, zupNAZIVZUP, zupPARAMETRI});
  }

  public void setall() {

    ddl.create("Zupanije")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("czup", 3, true)
       .addChar("nazivzup", 50)
       .addChar("parametri", 20)
       .addPrimaryKey("czup");


    Naziv = "Zupanije";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
