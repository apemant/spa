/****license*****************************************************************
**   file: OS_Kontrola.java
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



public class OS_Kontrola extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Kontrola OS_Kontrolaclass;

  QueryDataSet oskon = new QueryDataSet();

  Column oskonLOKK = new Column();
  Column oskonAKTIV = new Column();
  Column oskonCORG = new Column();
  Column oskonPROM = new Column();
  Column oskonAMOR = new Column();
  Column oskonREVA = new Column();
  Column oskonDATUM = new Column();
  Column oskonMJESEC = new Column();
  Column oskonGODINA = new Column();

  public static OS_Kontrola getDataModule() {
    if (OS_Kontrolaclass == null) {
      OS_Kontrolaclass = new OS_Kontrola();
    }
    return OS_Kontrolaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return oskon;
  }

  public OS_Kontrola() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    oskonLOKK.setCaption("Status zauzetosti");
    oskonLOKK.setColumnName("LOKK");
    oskonLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskonLOKK.setPrecision(1);
    oskonLOKK.setTableName("OS_KONTROLA");
    oskonLOKK.setServerColumnName("LOKK");
    oskonLOKK.setSqlType(1);
    oskonLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    oskonLOKK.setDefault("N");
    oskonAKTIV.setCaption("Aktivan - neaktivan");
    oskonAKTIV.setColumnName("AKTIV");
    oskonAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskonAKTIV.setPrecision(1);
    oskonAKTIV.setTableName("OS_KONTROLA");
    oskonAKTIV.setServerColumnName("AKTIV");
    oskonAKTIV.setSqlType(1);
    oskonAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    oskonAKTIV.setDefault("D");
    oskonCORG.setCaption("OJ");
    oskonCORG.setColumnName("CORG");
    oskonCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskonCORG.setPrecision(12);
    oskonCORG.setRowId(true);
    oskonCORG.setTableName("OS_KONTROLA");
    oskonCORG.setServerColumnName("CORG");
    oskonCORG.setSqlType(1);
    oskonCORG.setWidth(6);
    oskonPROM.setCaption("Promjene");
    oskonPROM.setColumnName("PROM");
    oskonPROM.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskonPROM.setPrecision(1);
    oskonPROM.setTableName("OS_KONTROLA");
    oskonPROM.setServerColumnName("PROM");
    oskonPROM.setSqlType(1);
    oskonAMOR.setCaption("Amortizacija");
    oskonAMOR.setColumnName("AMOR");
    oskonAMOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskonAMOR.setPrecision(1);
    oskonAMOR.setTableName("OS_KONTROLA");
    oskonAMOR.setServerColumnName("AMOR");
    oskonAMOR.setSqlType(1);
    oskonREVA.setCaption("Revalorizacija");
    oskonREVA.setColumnName("REVA");
    oskonREVA.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskonREVA.setPrecision(1);
    oskonREVA.setTableName("OS_KONTROLA");
    oskonREVA.setServerColumnName("REVA");
    oskonREVA.setSqlType(1);
    oskonDATUM.setCaption("Datum");
    oskonDATUM.setColumnName("DATUM");
    oskonDATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    oskonDATUM.setPrecision(8);
    oskonDATUM.setDisplayMask("dd-MM-yyyy");
//    oskonDATUM.setEditMask("dd-MM-yyyy");
    oskonDATUM.setTableName("OS_KONTROLA");
    oskonDATUM.setServerColumnName("DATUM");
    oskonDATUM.setSqlType(93);
    oskonDATUM.setWidth(10);
    oskonMJESEC.setCaption("Mjesec");
    oskonMJESEC.setColumnName("MJESEC");
    oskonMJESEC.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskonMJESEC.setPrecision(2);
    oskonMJESEC.setTableName("OS_KONTROLA");
    oskonMJESEC.setServerColumnName("MJESEC");
    oskonMJESEC.setSqlType(1);
    oskonGODINA.setCaption("Godina");
    oskonGODINA.setColumnName("GODINA");
    oskonGODINA.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskonGODINA.setPrecision(4);
    oskonGODINA.setTableName("OS_KONTROLA");
    oskonGODINA.setServerColumnName("GODINA");
    oskonGODINA.setSqlType(1);
    oskon.setResolver(dm.getQresolver());
    oskon.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Kontrola", null, true, Load.ALL));
 setColumns(new Column[] {oskonLOKK, oskonAKTIV, oskonCORG, oskonPROM, oskonAMOR, oskonREVA, oskonDATUM, oskonMJESEC, oskonGODINA});
  }

  public void setall() {

    ddl.create("OS_Kontrola")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addChar("prom", 1)
       .addChar("amor", 1)
       .addChar("reva", 1)
       .addDate("datum")
       .addChar("mjesec", 2)
       .addChar("godina", 4)
       .addPrimaryKey("corg");


    Naziv = "OS_Kontrola";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
