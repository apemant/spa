/****license*****************************************************************
**   file: ShemevezeUI.java
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



public class ShemevezeUI extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static ShemevezeUI ShemevezeUIclass;

  QueryDataSet shm = new raDataSet();

  Column shmLOKK = new Column();
  Column shmAKTIV = new Column();
  Column shmCSKL = new Column();
  Column shmVRDOK = new Column();
  Column shmSTAVKA1 = new Column();
  Column shmSTAVKA2 = new Column();
  Column shmSTOPA = new Column();
  Column shmINVERTNA = new Column();
  Column shmDODATNA = new Column();
  Column shmSTOPA2 = new Column();

  public static ShemevezeUI getDataModule() {
    if (ShemevezeUIclass == null) {
      ShemevezeUIclass = new ShemevezeUI();
    }
    return ShemevezeUIclass;
  }

  public QueryDataSet getQueryDataSet() {
    return shm;
  }

  public ShemevezeUI() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    shmLOKK.setCaption("Status zauzetosti");
    shmLOKK.setColumnName("LOKK");
    shmLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    shmLOKK.setPrecision(1);
    shmLOKK.setTableName("SHEMEVEZEUI");
    shmLOKK.setServerColumnName("LOKK");
    shmLOKK.setSqlType(1);
    shmLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    shmLOKK.setDefault("N");
    shmAKTIV.setCaption("Aktivan - neaktivan");
    shmAKTIV.setColumnName("AKTIV");
    shmAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    shmAKTIV.setPrecision(1);
    shmAKTIV.setTableName("SHEMEVEZEUI");
    shmAKTIV.setServerColumnName("AKTIV");
    shmAKTIV.setSqlType(1);
    shmAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    shmAKTIV.setDefault("D");
    shmCSKL.setCaption("Vrsta sheme");
    shmCSKL.setColumnName("CSKL");
    shmCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    shmCSKL.setPrecision(12);
    shmCSKL.setRowId(true);
    shmCSKL.setTableName("SHEMEVEZEUI");
    shmCSKL.setServerColumnName("CSKL");
    shmCSKL.setSqlType(1);
    shmVRDOK.setCaption("Vrsta dokumenta");
    shmVRDOK.setColumnName("VRDOK");
    shmVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    shmVRDOK.setPrecision(3);
    shmVRDOK.setRowId(true);
    shmVRDOK.setTableName("SHEMEVEZEUI");
    shmVRDOK.setServerColumnName("VRDOK");
    shmVRDOK.setSqlType(1);
    shmSTAVKA1.setCaption("Osnovica");
    shmSTAVKA1.setColumnName("STAVKA1");
    shmSTAVKA1.setDataType(com.borland.dx.dataset.Variant.SHORT);
    shmSTAVKA1.setPrecision(2);
    shmSTAVKA1.setRowId(true);
    shmSTAVKA1.setTableName("SHEMEVEZEUI");
    shmSTAVKA1.setServerColumnName("STAVKA1");
    shmSTAVKA1.setSqlType(5);
    shmSTAVKA1.setWidth(2);
    shmSTAVKA2.setCaption("Rezultat");
    shmSTAVKA2.setColumnName("STAVKA2");
    shmSTAVKA2.setDataType(com.borland.dx.dataset.Variant.SHORT);
    shmSTAVKA2.setPrecision(2);
    shmSTAVKA2.setRowId(true);
    shmSTAVKA2.setTableName("SHEMEVEZEUI");
    shmSTAVKA2.setServerColumnName("STAVKA2");
    shmSTAVKA2.setSqlType(5);
    shmSTAVKA2.setWidth(2);
    shmSTOPA.setCaption("Stopa");
    shmSTOPA.setColumnName("STOPA");
    shmSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    shmSTOPA.setPrecision(6);
    shmSTOPA.setScale(2);
    shmSTOPA.setDisplayMask("###,###,##0.00");
    shmSTOPA.setDefault("0");
    shmSTOPA.setTableName("SHEMEVEZEUI");
    shmSTOPA.setServerColumnName("STOPA");
    shmSTOPA.setSqlType(2);
    shmSTOPA.setDefault("0");
    shmINVERTNA.setCaption("Invertna");
    shmINVERTNA.setColumnName("INVERTNA");
    shmINVERTNA.setDataType(com.borland.dx.dataset.Variant.STRING);
    shmINVERTNA.setPrecision(1);
    shmINVERTNA.setTableName("SHEMEVEZEUI");
    shmINVERTNA.setServerColumnName("INVERTNA");
    shmINVERTNA.setSqlType(1);
    shmINVERTNA.setDefault("N");
    shmDODATNA.setCaption("Dodatna");
    shmDODATNA.setColumnName("DODATNA");
    shmDODATNA.setDataType(com.borland.dx.dataset.Variant.STRING);
    shmDODATNA.setPrecision(1);
    shmDODATNA.setTableName("SHEMEVEZEUI");
    shmDODATNA.setServerColumnName("DODATNA");
    shmDODATNA.setSqlType(1);
    shmDODATNA.setDefault("N");
    shmSTOPA2.setCaption("Dodatna stopa");
    shmSTOPA2.setColumnName("STOPA2");
    shmSTOPA2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    shmSTOPA2.setPrecision(6);
    shmSTOPA2.setScale(2);
    shmSTOPA2.setDisplayMask("###,###,##0.00");
    shmSTOPA2.setDefault("0");
    shmSTOPA2.setTableName("SHEMEVEZEUI");
    shmSTOPA2.setServerColumnName("STOPA2");
    shmSTOPA2.setSqlType(2);
    shmSTOPA2.setDefault("0");
    shm.setResolver(dm.getQresolver());
    shm.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from ShemevezeUI", null, true, Load.ALL));
    setColumns(new Column[] {shmLOKK, shmAKTIV, shmCSKL, shmVRDOK, shmSTAVKA1, shmSTAVKA2, shmSTOPA, shmINVERTNA, shmDODATNA, shmSTOPA2});
  }

  public void setall() {

    ddl.create("ShemevezeUI")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskl", 12, true)
       .addChar("vrdok", 3, true)
       .addShort("stavka1", 2, true)
       .addShort("stavka2", 2, true)
       .addFloat("stopa", 6, 2)
       .addChar("invertna", 1, "N")
       .addChar("dodatna", 1, "N")
       .addFloat("stopa2", 6, 2)
       .addPrimaryKey("cskl,vrdok,stavka1,stavka2");


    Naziv = "ShemevezeUI";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
