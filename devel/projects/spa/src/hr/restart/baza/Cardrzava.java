/****license*****************************************************************
**   file: Cardrzava.java
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



public class Cardrzava extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Cardrzava Cardrzavaclass;

  QueryDataSet cardrzava = new QueryDataSet();

  Column cardrzavaLOCK = new Column();
  Column cardrzavaAKTIV = new Column();
  Column cardrzavaCSIFDRV = new Column();
  Column cardrzavaCOZNDRV = new Column();
  Column cardrzavaNAZIV = new Column();

  public static Cardrzava getDataModule() {
    if (Cardrzavaclass == null) {
      Cardrzavaclass = new Cardrzava();
    }
    return Cardrzavaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return cardrzava;
  }

  public Cardrzava() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    cardrzavaLOCK.setCaption("Status zauzetosti");
    cardrzavaLOCK.setColumnName("LOCK");
    cardrzavaLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardrzavaLOCK.setPrecision(1);
    cardrzavaLOCK.setTableName("CARDRZAVA");
    cardrzavaLOCK.setServerColumnName("LOCK");
    cardrzavaLOCK.setSqlType(1);
    cardrzavaLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cardrzavaLOCK.setDefault("N");
    cardrzavaAKTIV.setCaption("Aktivan - neaktivan");
    cardrzavaAKTIV.setColumnName("AKTIV");
    cardrzavaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardrzavaAKTIV.setPrecision(1);
    cardrzavaAKTIV.setTableName("CARDRZAVA");
    cardrzavaAKTIV.setServerColumnName("AKTIV");
    cardrzavaAKTIV.setSqlType(1);
    cardrzavaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cardrzavaAKTIV.setDefault("D");
    cardrzavaCSIFDRV.setCaption("Šifra države");
    cardrzavaCSIFDRV.setColumnName("CSIFDRV");
    cardrzavaCSIFDRV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardrzavaCSIFDRV.setPrecision(3);
    cardrzavaCSIFDRV.setRowId(true);
    cardrzavaCSIFDRV.setTableName("CARDRZAVA");
    cardrzavaCSIFDRV.setServerColumnName("CSIFDRV");
    cardrzavaCSIFDRV.setSqlType(1);
    cardrzavaCOZNDRV.setCaption("Oznaka države");
    cardrzavaCOZNDRV.setColumnName("COZNDRV");
    cardrzavaCOZNDRV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardrzavaCOZNDRV.setPrecision(3);
    cardrzavaCOZNDRV.setTableName("CARDRZAVA");
    cardrzavaCOZNDRV.setServerColumnName("COZNDRV");
    cardrzavaCOZNDRV.setSqlType(1);
    cardrzavaNAZIV.setCaption("Naziv carinarnice");
    cardrzavaNAZIV.setColumnName("NAZIV");
    cardrzavaNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cardrzavaNAZIV.setPrecision(80);
    cardrzavaNAZIV.setTableName("CARDRZAVA");
    cardrzavaNAZIV.setServerColumnName("NAZIV");
    cardrzavaNAZIV.setSqlType(1);
    cardrzavaNAZIV.setWidth(30);
    cardrzava.setResolver(dm.getQresolver());
    cardrzava.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Cardrzava", null, true, Load.ALL));
    setColumns(new Column[] {cardrzavaLOCK, cardrzavaAKTIV, cardrzavaCSIFDRV, cardrzavaCOZNDRV, cardrzavaNAZIV});
  }

  public void setall() {

    ddl.create("Cardrzava")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("csifdrv", 3, true)
       .addChar("cozndrv", 3)
       .addChar("naziv", 80)
       .addPrimaryKey("csifdrv");


    Naziv = "Cardrzava";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"csifdrv"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
