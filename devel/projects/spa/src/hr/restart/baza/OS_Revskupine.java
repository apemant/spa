/****license*****************************************************************
**   file: OS_Revskupine.java
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



public class OS_Revskupine extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Revskupine OS_Revskupineclass;

  QueryDataSet osrev = new raDataSet();

  Column osrevLOKK = new Column();
  Column osrevAKTIV = new Column();
  Column osrevCSKUPINE = new Column();
  Column osrevNAZSKUPINE = new Column();
  Column osrevMJESEC = new Column();
  Column osrevKOEFICIJENT = new Column();
  Column osrevKOEFICIJENT2 = new Column();

  public static OS_Revskupine getDataModule() {
    if (OS_Revskupineclass == null) {
      OS_Revskupineclass = new OS_Revskupine();
    }
    return OS_Revskupineclass;
  }

  public QueryDataSet getQueryDataSet() {
    return osrev;
  }

  public OS_Revskupine() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osrevLOKK.setCaption("Status zauzetosti");
    osrevLOKK.setColumnName("LOKK");
    osrevLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osrevLOKK.setPrecision(1);
    osrevLOKK.setTableName("OS_REVSKUPINE");
    osrevLOKK.setServerColumnName("LOKK");
    osrevLOKK.setSqlType(1);
    osrevLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osrevLOKK.setDefault("N");
    osrevAKTIV.setCaption("Aktivan - neaktivan");
    osrevAKTIV.setColumnName("AKTIV");
    osrevAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osrevAKTIV.setPrecision(1);
    osrevAKTIV.setTableName("OS_REVSKUPINE");
    osrevAKTIV.setServerColumnName("AKTIV");
    osrevAKTIV.setSqlType(1);
    osrevAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osrevAKTIV.setDefault("D");
    osrevCSKUPINE.setCaption("Šifra");
    osrevCSKUPINE.setColumnName("CSKUPINE");
    osrevCSKUPINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osrevCSKUPINE.setPrecision(6);
    osrevCSKUPINE.setRowId(true);
    osrevCSKUPINE.setTableName("OS_REVSKUPINE");
    osrevCSKUPINE.setServerColumnName("CSKUPINE");
    osrevCSKUPINE.setSqlType(1);
    osrevNAZSKUPINE.setCaption("Naziv");
    osrevNAZSKUPINE.setColumnName("NAZSKUPINE");
    osrevNAZSKUPINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osrevNAZSKUPINE.setPrecision(50);
    osrevNAZSKUPINE.setTableName("OS_REVSKUPINE");
    osrevNAZSKUPINE.setServerColumnName("NAZSKUPINE");
    osrevNAZSKUPINE.setSqlType(1);
    osrevNAZSKUPINE.setWidth(30);
    osrevMJESEC.setCaption("Mjesec");
    osrevMJESEC.setColumnName("MJESEC");
    osrevMJESEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    osrevMJESEC.setPrecision(6);
    osrevMJESEC.setTableName("OS_REVSKUPINE");
    osrevMJESEC.setServerColumnName("MJESEC");
    osrevMJESEC.setSqlType(1);
    osrevKOEFICIJENT.setCaption("Koeficijent");
    osrevKOEFICIJENT.setColumnName("KOEFICIJENT");
    osrevKOEFICIJENT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osrevKOEFICIJENT.setPrecision(10);
    osrevKOEFICIJENT.setScale(2);
    osrevKOEFICIJENT.setDisplayMask("###,###,##0.00");
    osrevKOEFICIJENT.setDefault("0");
    osrevKOEFICIJENT.setTableName("OS_REVSKUPINE");
    osrevKOEFICIJENT.setServerColumnName("KOEFICIJENT");
    osrevKOEFICIJENT.setSqlType(2);
    osrevKOEFICIJENT.setDefault("0");
    osrevKOEFICIJENT.setWidth(8);
    osrevKOEFICIJENT2.setCaption("Koeficijent 2");
    osrevKOEFICIJENT2.setColumnName("KOEFICIJENT2");
    osrevKOEFICIJENT2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osrevKOEFICIJENT2.setPrecision(10);
    osrevKOEFICIJENT2.setScale(2);
    osrevKOEFICIJENT2.setDisplayMask("###,###,##0.00");
    osrevKOEFICIJENT2.setDefault("0");
    osrevKOEFICIJENT2.setTableName("OS_REVSKUPINE");
    osrevKOEFICIJENT2.setServerColumnName("KOEFICIJENT2");
    osrevKOEFICIJENT2.setSqlType(2);
    osrevKOEFICIJENT2.setDefault("0");
    osrevKOEFICIJENT2.setWidth(8);
    osrev.setResolver(dm.getQresolver());
    osrev.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Revskupine", null, true, Load.ALL));
 setColumns(new Column[] {osrevLOKK, osrevAKTIV, osrevCSKUPINE, osrevNAZSKUPINE, osrevMJESEC, osrevKOEFICIJENT, osrevKOEFICIJENT2});
  }

  public void setall() {

    ddl.create("OS_Revskupine")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskupine", 6, true)
       .addChar("nazskupine", 50)
       .addChar("mjesec", 6)
       .addFloat("koeficijent", 10, 2)
       .addFloat("koeficijent2", 10, 2)
       .addPrimaryKey("cskupine");


    Naziv = "OS_Revskupine";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
