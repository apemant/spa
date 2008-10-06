/****license*****************************************************************
**   file: Kljucevi.java
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



public class Kljucevi extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Kljucevi Kljuceviclass;

  QueryDataSet klj = new raDataSet();

  Column kljCKEY = new Column();
  Column kljCUSER = new Column();
  Column kljIMETAB = new Column();
  Column kljKEYS = new Column();
  Column kljVALUES = new Column();
  Column kljDATUM = new Column();

  public static Kljucevi getDataModule() {
    if (Kljuceviclass == null) {
      Kljuceviclass = new Kljucevi();
    }
    return Kljuceviclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return klj;
  }

  public Kljucevi() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    kljCKEY.setCaption("Šifra klju\u010Da");
    kljCKEY.setColumnName("CKEY");
    kljCKEY.setDataType(com.borland.dx.dataset.Variant.INT);
    kljCKEY.setPrecision(6);
    kljCKEY.setRowId(true);
    kljCKEY.setTableName("KLJUCEVI");
    kljCKEY.setServerColumnName("CKEY");
    kljCKEY.setSqlType(4);
    kljCKEY.setWidth(6);
    kljCKEY.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kljCUSER.setCaption("Korisnik");
    kljCUSER.setColumnName("CUSER");
    kljCUSER.setDataType(com.borland.dx.dataset.Variant.STRING);
    kljCUSER.setPrecision(15);
    kljCUSER.setRowId(true);
    kljCUSER.setTableName("KLJUCEVI");
    kljCUSER.setServerColumnName("CUSER");
    kljCUSER.setSqlType(1);
    kljCUSER.setWidth(9);
    kljIMETAB.setCaption("Ime tablice");
    kljIMETAB.setColumnName("IMETAB");
    kljIMETAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    kljIMETAB.setPrecision(20);
    kljIMETAB.setTableName("KLJUCEVI");
    kljIMETAB.setServerColumnName("IMETAB");
    kljIMETAB.setSqlType(1);
    kljIMETAB.setWidth(10);
    kljKEYS.setCaption("Popis kolona klju\u010Da");
    kljKEYS.setColumnName("KL_KEYS");
    kljKEYS.setDataType(com.borland.dx.dataset.Variant.STRING);
    kljKEYS.setPrecision(100);
    kljKEYS.setTableName("KLJUCEVI");
    kljKEYS.setServerColumnName("KL_KEYS");
    kljKEYS.setSqlType(1);
    kljKEYS.setWidth(20);
    kljVALUES.setCaption("Popis vrijednosti klju\u010Da");
    kljVALUES.setColumnName("VALS");
    kljVALUES.setDataType(com.borland.dx.dataset.Variant.STRING);
    kljVALUES.setPrecision(100);
    kljVALUES.setTableName("KLJUCEVI");
    kljVALUES.setServerColumnName("VALS");
    kljVALUES.setSqlType(1);
    kljVALUES.setWidth(20);
    kljDATUM.setCaption("Datum unosa");
    kljDATUM.setColumnName("DATUM");
    kljDATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    kljDATUM.setPrecision(8);
    kljDATUM.setDisplayMask("dd-MM-yyyy");
//    kljDATUM.setEditMask("dd-MM-yyyy");
    kljDATUM.setTableName("KLJUCEVI");
    kljDATUM.setServerColumnName("DATUM");
    kljDATUM.setWidth(10);
    kljDATUM.setSqlType(93);
    klj.setResolver(dm.getQresolver());
    klj.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from Kljucevi", null, true, Load.ALL));
 setColumns(new Column[] {kljCKEY, kljCUSER, kljIMETAB, kljKEYS, kljVALUES, kljDATUM});
  }

  public void setall() {

    ddl.create("Kljucevi")
       .addInteger("ckey", 6, true)
       .addChar("cuser", 15, true)
       .addChar("imetab", 20)
       .addChar("kl_keys", 100)
       .addChar("vals", 100)
       .addDate("datum")
       .addPrimaryKey("ckey,cuser");


    Naziv = "Kljucevi";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
