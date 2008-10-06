/****license*****************************************************************
**   file: Cardav.java
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



public class Cardav extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Cardav Cardavclass;

  QueryDataSet cardav = new QueryDataSet();

  Column cardavLOCK = new Column();
  Column cardavAKTIV = new Column();
  Column cardavCDAV = new Column();
  Column cardavNAZIV = new Column();

  public static Cardav getDataModule() {
    if (Cardavclass == null) {
      Cardavclass = new Cardav();
    }
    return Cardavclass;
  }

  public QueryDataSet getQueryDataSet() {
    return cardav;
  }

  public Cardav() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    cardavLOCK.setCaption("Status zauzetosti");
    cardavLOCK.setColumnName("LOCK");
    cardavLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardavLOCK.setPrecision(1);
    cardavLOCK.setTableName("CARDAV");
    cardavLOCK.setServerColumnName("LOCK");
    cardavLOCK.setSqlType(1);
    cardavLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cardavLOCK.setDefault("N");
    cardavAKTIV.setCaption("Aktivan - neaktivan");
    cardavAKTIV.setColumnName("AKTIV");
    cardavAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardavAKTIV.setPrecision(1);
    cardavAKTIV.setTableName("CARDAV");
    cardavAKTIV.setServerColumnName("AKTIV");
    cardavAKTIV.setSqlType(1);
    cardavAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cardavAKTIV.setDefault("D");
    cardavCDAV.setCaption("Šifra vrste davanja");
    cardavCDAV.setColumnName("CDAV");
    cardavCDAV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardavCDAV.setPrecision(10);
    cardavCDAV.setRowId(true);
    cardavCDAV.setTableName("CARDAV");
    cardavCDAV.setServerColumnName("CDAV");
    cardavCDAV.setSqlType(1);
    cardavNAZIV.setCaption("Naziv");
    cardavNAZIV.setColumnName("NAZIV");
    cardavNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardavNAZIV.setPrecision(80);
    cardavNAZIV.setTableName("CARDAV");
    cardavNAZIV.setServerColumnName("NAZIV");
    cardavNAZIV.setSqlType(1);
    cardavNAZIV.setWidth(30);
    cardav.setResolver(dm.getQresolver());
    cardav.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Cardav", null, true, Load.ALL));
    setColumns(new Column[] {cardavLOCK, cardavAKTIV, cardavCDAV, cardavNAZIV});
  }

  public void setall() {

    ddl.create("Cardav")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cdav", 10, true)
       .addChar("naziv", 80)
       .addPrimaryKey("cdav");


    Naziv = "Cardav";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cdav"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
