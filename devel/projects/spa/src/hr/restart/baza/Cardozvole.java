/****license*****************************************************************
**   file: Cardozvole.java
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



public class Cardozvole extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Cardozvole Cardozvoleclass;

  QueryDataSet cardozvole = new QueryDataSet();

  Column cardozvoleLOCK = new Column();
  Column cardozvoleAKTIV = new Column();
  Column cardozvoleCDOZ = new Column();
  Column cardozvoleNAZIV = new Column();

  public static Cardozvole getDataModule() {
    if (Cardozvoleclass == null) {
      Cardozvoleclass = new Cardozvole();
    }
    return Cardozvoleclass;
  }

  public QueryDataSet getQueryDataSet() {
    return cardozvole;
  }

  public Cardozvole() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    cardozvoleLOCK.setCaption("Status zauzetosti");
    cardozvoleLOCK.setColumnName("LOCK");
    cardozvoleLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardozvoleLOCK.setPrecision(1);
    cardozvoleLOCK.setTableName("CARDOZVOLE");
    cardozvoleLOCK.setServerColumnName("LOCK");
    cardozvoleLOCK.setSqlType(1);
    cardozvoleLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cardozvoleLOCK.setDefault("N");
    cardozvoleAKTIV.setCaption("Aktivan - neaktivan");
    cardozvoleAKTIV.setColumnName("AKTIV");
    cardozvoleAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardozvoleAKTIV.setPrecision(1);
    cardozvoleAKTIV.setTableName("CARDOZVOLE");
    cardozvoleAKTIV.setServerColumnName("AKTIV");
    cardozvoleAKTIV.setSqlType(1);
    cardozvoleAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cardozvoleAKTIV.setDefault("D");
    cardozvoleCDOZ.setCaption("Šifra dozvole");
    cardozvoleCDOZ.setColumnName("CDOZ");
    cardozvoleCDOZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardozvoleCDOZ.setPrecision(10);
    cardozvoleCDOZ.setRowId(true);
    cardozvoleCDOZ.setTableName("CARDOZVOLE");
    cardozvoleCDOZ.setServerColumnName("CDOZ");
    cardozvoleCDOZ.setSqlType(1);
    cardozvoleNAZIV.setCaption("Naziv");
    cardozvoleNAZIV.setColumnName("NAZIV");
    cardozvoleNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardozvoleNAZIV.setPrecision(100);
    cardozvoleNAZIV.setTableName("CARDOZVOLE");
    cardozvoleNAZIV.setServerColumnName("NAZIV");
    cardozvoleNAZIV.setSqlType(1);
    cardozvoleNAZIV.setWidth(30);
    cardozvole.setResolver(dm.getQresolver());
    cardozvole.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Cardozvole", null, true, Load.ALL));
    setColumns(new Column[] {cardozvoleLOCK, cardozvoleAKTIV, cardozvoleCDOZ, cardozvoleNAZIV});
  }

  public void setall() {

    ddl.create("Cardozvole")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cdoz", 10, true)
       .addChar("naziv", 100)
       .addPrimaryKey("cdoz");


    Naziv = "Cardozvole";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cdoz"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
