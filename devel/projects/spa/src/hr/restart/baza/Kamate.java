/****license*****************************************************************
**   file: Kamate.java
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



public class Kamate extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Kamate Kamateclass;

  QueryDataSet kam = new raDataSet();

  Column kamCKAM = new Column();
  Column kamOPIS = new Column();
  Column kamDATUM = new Column();
  Column kamSTOPA = new Column();
  Column kamVRSTA = new Column();
  Column kamDANI = new Column();

  public static Kamate getDataModule() {
    if (Kamateclass == null) {
      Kamateclass = new Kamate();
    }
    return Kamateclass;
  }

  public QueryDataSet getQueryDataSet() {
    return kam;
  }

  public Kamate() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    kamCKAM.setCaption("Šifra");
    kamCKAM.setColumnName("CKAM");
    kamCKAM.setDataType(com.borland.dx.dataset.Variant.INT);
    kamCKAM.setPrecision(6);
    kamCKAM.setRowId(true);
    kamCKAM.setTableName("KAMATE");
    kamCKAM.setServerColumnName("CKAM");
    kamCKAM.setSqlType(4);
    kamCKAM.setWidth(6);
    kamOPIS.setCaption("Opis");
    kamOPIS.setColumnName("OPIS");
    kamOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    kamOPIS.setPrecision(50);
    kamOPIS.setTableName("KAMATE");
    kamOPIS.setServerColumnName("OPIS");
    kamOPIS.setSqlType(1);
    kamOPIS.setWidth(30);
    kamDATUM.setCaption("Vrijedi od");
    kamDATUM.setColumnName("DATUM");
    kamDATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    kamDATUM.setPrecision(19);
    kamDATUM.setDisplayMask("dd-MM-yyyy");
//    kamDATUM.setEditMask("dd-MM-yyyy");
    kamDATUM.setRowId(true);
    kamDATUM.setTableName("KAMATE");
    kamDATUM.setServerColumnName("DATUM");
    kamDATUM.setSqlType(93);
    kamDATUM.setWidth(10);
    kamSTOPA.setCaption("Stopa");
    kamSTOPA.setColumnName("STOPA");
    kamSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kamSTOPA.setPrecision(8);
    kamSTOPA.setScale(2);
    kamSTOPA.setDisplayMask("###,###,##0.00");
    kamSTOPA.setDefault("0");
    kamSTOPA.setTableName("KAMATE");
    kamSTOPA.setServerColumnName("STOPA");
    kamSTOPA.setSqlType(2);
    kamSTOPA.setDefault("0");
    kamVRSTA.setCaption("Vrsta");
    kamVRSTA.setColumnName("VRSTA");
    kamVRSTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    kamVRSTA.setPrecision(1);
    kamVRSTA.setTableName("KAMATE");
    kamVRSTA.setServerColumnName("VRSTA");
    kamVRSTA.setSqlType(1);
    kamVRSTA.setDefault("G");
    kamDANI.setCaption("Za dana");
    kamDANI.setColumnName("DANI");
    kamDANI.setDataType(com.borland.dx.dataset.Variant.INT);
    kamDANI.setPrecision(6);
    kamDANI.setTableName("KAMATE");
    kamDANI.setServerColumnName("DANI");
    kamDANI.setSqlType(4);
    kamDANI.setWidth(6);
    kam.setResolver(dm.getQresolver());
    kam.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Kamate", null, true, Load.ALL));
 setColumns(new Column[] {kamCKAM, kamOPIS, kamDATUM, kamSTOPA, kamVRSTA, kamDANI});
  }

  public void setall() {

    ddl.create("Kamate")
       .addInteger("ckam", 6, true)
       .addChar("opis", 50)
       .addDate("datum", true)
       .addFloat("stopa", 8, 2)
       .addChar("vrsta", 1, "G")
       .addInteger("dani", 6)
       .addPrimaryKey("ckam,datum");


    Naziv = "Kamate";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
