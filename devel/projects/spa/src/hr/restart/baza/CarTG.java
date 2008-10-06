/****license*****************************************************************
**   file: CarTG.java
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



public class CarTG extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static CarTG CarTGclass;

  QueryDataSet cartg = new QueryDataSet();

  Column cartgLOCK = new Column();
  Column cartgAKTIV = new Column();
  Column cartgCTG = new Column();
  Column cartgNAZIV = new Column();
  Column cartgOPIS = new Column();
  Column cartgCJMCAROZN = new Column();
  Column cartgKVOTA = new Column();
  Column cartgSTOPA1 = new Column();
  Column cartgSTOPA2 = new Column();

  public static CarTG getDataModule() {
    if (CarTGclass == null) {
      CarTGclass = new CarTG();
    }
    return CarTGclass;
  }

  public QueryDataSet getQueryDataSet() {
    return cartg;
  }

  public CarTG() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    cartgLOCK.setCaption("Status zauzetosti");
    cartgLOCK.setColumnName("LOCK");
    cartgLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartgLOCK.setPrecision(1);
    cartgLOCK.setTableName("CARTG");
    cartgLOCK.setServerColumnName("LOCK");
    cartgLOCK.setSqlType(1);
    cartgLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cartgLOCK.setDefault("N");
    cartgAKTIV.setCaption("Aktivan - neaktivan");
    cartgAKTIV.setColumnName("AKTIV");
    cartgAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartgAKTIV.setPrecision(1);
    cartgAKTIV.setTableName("CARTG");
    cartgAKTIV.setServerColumnName("AKTIV");
    cartgAKTIV.setSqlType(1);
    cartgAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cartgAKTIV.setDefault("D");
    cartgCTG.setCaption("Tarifni broj");
    cartgCTG.setColumnName("CTG");
    cartgCTG.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartgCTG.setPrecision(10);
    cartgCTG.setRowId(true);
    cartgCTG.setTableName("CARTG");
    cartgCTG.setServerColumnName("CTG");
    cartgCTG.setSqlType(1);
    cartgNAZIV.setCaption("Naziv");
    cartgNAZIV.setColumnName("NAZIV");
    cartgNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartgNAZIV.setPrecision(80);
    cartgNAZIV.setTableName("CARTG");
    cartgNAZIV.setServerColumnName("NAZIV");
    cartgNAZIV.setSqlType(1);
    cartgNAZIV.setWidth(30);
    cartgOPIS.setCaption("Opis");
    cartgOPIS.setColumnName("OPIS");
    cartgOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartgOPIS.setPrecision(80);
    cartgOPIS.setTableName("CARTG");
    cartgOPIS.setServerColumnName("OPIS");
    cartgOPIS.setSqlType(1);
    cartgOPIS.setWidth(30);
    cartgCJMCAROZN.setCaption("Oznaka mjerne jedinice");
    cartgCJMCAROZN.setColumnName("CJMCAROZN");
    cartgCJMCAROZN.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartgCJMCAROZN.setPrecision(10);
    cartgCJMCAROZN.setTableName("CARTG");
    cartgCJMCAROZN.setServerColumnName("CJMCAROZN");
    cartgCJMCAROZN.setSqlType(1);
    cartgKVOTA.setCaption("Kvota");
    cartgKVOTA.setColumnName("KVOTA");
    cartgKVOTA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cartgKVOTA.setPrecision(17);
    cartgKVOTA.setScale(3);
    cartgKVOTA.setDisplayMask("###,###,##0.000");
    cartgKVOTA.setDefault("0");
    cartgKVOTA.setTableName("CARTG");
    cartgKVOTA.setServerColumnName("KVOTA");
    cartgKVOTA.setSqlType(2);
    cartgSTOPA1.setCaption("Stopa 1");
    cartgSTOPA1.setColumnName("STOPA1");
    cartgSTOPA1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cartgSTOPA1.setPrecision(17);
    cartgSTOPA1.setScale(3);
    cartgSTOPA1.setDisplayMask("###,###,##0.000");
    cartgSTOPA1.setDefault("0");
    cartgSTOPA1.setTableName("CARTG");
    cartgSTOPA1.setServerColumnName("STOPA1");
    cartgSTOPA1.setSqlType(2);
    cartgSTOPA2.setCaption("Stopa 2");
    cartgSTOPA2.setColumnName("STOPA2");
    cartgSTOPA2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    cartgSTOPA2.setPrecision(17);
    cartgSTOPA2.setScale(3);
    cartgSTOPA2.setDisplayMask("###,###,##0.000");
    cartgSTOPA2.setDefault("0");
    cartgSTOPA2.setTableName("CARTG");
    cartgSTOPA2.setServerColumnName("STOPA2");
    cartgSTOPA2.setSqlType(2);
    cartg.setResolver(dm.getQresolver());
    cartg.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from CarTG", null, true, Load.ALL));
    setColumns(new Column[] {cartgLOCK, cartgAKTIV, cartgCTG, cartgNAZIV, cartgOPIS, cartgCJMCAROZN, cartgKVOTA, cartgSTOPA1, cartgSTOPA2});
  }

  public void setall() {

    ddl.create("CarTG")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("ctg", 10, true)
       .addChar("naziv", 80)
       .addChar("opis", 80)
       .addChar("cjmcarozn", 10)
       .addFloat("kvota", 17, 3)
       .addFloat("stopa1", 17, 3)
       .addFloat("stopa2", 17, 3)
       .addPrimaryKey("ctg");


    Naziv = "CarTG";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"ctg"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
