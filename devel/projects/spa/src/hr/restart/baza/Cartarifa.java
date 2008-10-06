/****license*****************************************************************
**   file: Cartarifa.java
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



public class Cartarifa extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Cartarifa Cartarifaclass;

  QueryDataSet cartarifa = new QueryDataSet();

  Column cartarifaLOCK = new Column();
  Column cartarifaAKTIV = new Column();
  Column cartarifaCTG = new Column();
  Column cartarifaCUGOVOR = new Column();
  Column cartarifaKVOTA = new Column();
  Column cartarifaSTOPA1 = new Column();
  Column cartarifaSTOPA2 = new Column();

  public static Cartarifa getDataModule() {
    if (Cartarifaclass == null) {
      Cartarifaclass = new Cartarifa();
    }
    return Cartarifaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return cartarifa;
  }

  public Cartarifa() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    cartarifaLOCK.setCaption("Status zauzetosti");
    cartarifaLOCK.setColumnName("LOCK");
    cartarifaLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartarifaLOCK.setPrecision(1);
    cartarifaLOCK.setTableName("CARTARIFA");
    cartarifaLOCK.setServerColumnName("LOCK");
    cartarifaLOCK.setSqlType(1);
    cartarifaLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cartarifaLOCK.setDefault("N");
    cartarifaAKTIV.setCaption("Aktivan - neaktivan");
    cartarifaAKTIV.setColumnName("AKTIV");
    cartarifaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartarifaAKTIV.setPrecision(1);
    cartarifaAKTIV.setTableName("CARTARIFA");
    cartarifaAKTIV.setServerColumnName("AKTIV");
    cartarifaAKTIV.setSqlType(1);
    cartarifaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cartarifaAKTIV.setDefault("D");
    cartarifaCTG.setCaption("Šifra tarifne grupe");
    cartarifaCTG.setColumnName("CTG");
    cartarifaCTG.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartarifaCTG.setPrecision(10);
    cartarifaCTG.setRowId(true);
    cartarifaCTG.setTableName("CARTARIFA");
    cartarifaCTG.setServerColumnName("CTG");
    cartarifaCTG.setSqlType(1);
    cartarifaCUGOVOR.setCaption("Šifra ugovora");
    cartarifaCUGOVOR.setColumnName("CUGOVOR");
    cartarifaCUGOVOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartarifaCUGOVOR.setPrecision(4);
    cartarifaCUGOVOR.setRowId(true);
    cartarifaCUGOVOR.setTableName("CARTARIFA");
    cartarifaCUGOVOR.setServerColumnName("CUGOVOR");
    cartarifaCUGOVOR.setSqlType(1);
    cartarifaKVOTA.setCaption("Carinska kvota");
    cartarifaKVOTA.setColumnName("KVOTA");
    cartarifaKVOTA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cartarifaKVOTA.setPrecision(17);
    cartarifaKVOTA.setScale(3);
    cartarifaKVOTA.setDisplayMask("###,###,##0.000");
    cartarifaKVOTA.setDefault("0");
    cartarifaKVOTA.setTableName("CARTARIFA");
    cartarifaKVOTA.setServerColumnName("KVOTA");
    cartarifaKVOTA.setSqlType(2);
    cartarifaSTOPA1.setCaption("Stopa 1");
    cartarifaSTOPA1.setColumnName("STOPA1");
    cartarifaSTOPA1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cartarifaSTOPA1.setPrecision(17);
    cartarifaSTOPA1.setScale(2);
    cartarifaSTOPA1.setDisplayMask("###,###,##0.00");
    cartarifaSTOPA1.setDefault("0");
    cartarifaSTOPA1.setTableName("CARTARIFA");
    cartarifaSTOPA1.setServerColumnName("STOPA1");
    cartarifaSTOPA1.setSqlType(2);
    cartarifaSTOPA2.setCaption("Stopa 2");
    cartarifaSTOPA2.setColumnName("STOPA2");
    cartarifaSTOPA2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cartarifaSTOPA2.setPrecision(17);
    cartarifaSTOPA2.setScale(2);
    cartarifaSTOPA2.setDisplayMask("###,###,##0.00");
    cartarifaSTOPA2.setDefault("0");
    cartarifaSTOPA2.setTableName("CARTARIFA");
    cartarifaSTOPA2.setServerColumnName("STOPA2");
    cartarifaSTOPA2.setSqlType(2);
    cartarifa.setResolver(dm.getQresolver());
    cartarifa.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Cartarifa", null, true, Load.ALL));
    setColumns(new Column[] {cartarifaLOCK, cartarifaAKTIV, cartarifaCTG, cartarifaCUGOVOR, cartarifaKVOTA, cartarifaSTOPA1, cartarifaSTOPA2});
  }

  public void setall() {

    ddl.create("Cartarifa")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("ctg", 10, true)
       .addChar("cugovor", 4, true)
       .addFloat("kvota", 17, 3)
       .addFloat("stopa1", 17, 2)
       .addFloat("stopa2", 17, 2)
       .addPrimaryKey("ctg,cugovor");


    Naziv = "Cartarifa";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"ctg", "cugovor"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
