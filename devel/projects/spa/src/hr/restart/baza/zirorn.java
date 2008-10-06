/****license*****************************************************************
**   file: zirorn.java
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
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;



public class zirorn extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static zirorn zirornclass;

  QueryDataSet zirorn = new raDataSet();
  QueryDataSet zirornaktiv = new raDataSet();

/*  Column zirornLOKK = new Column();
  Column zirornAKTIV = new Column();
  Column zirornCORG = new Column();
  Column zirornZIRO = new Column();
  Column zirornCVRNAL = new Column();
  Column zirornOZNVAL = new Column();
  Column zirornDEV = new Column();
  Column zirornBROJKONTA = new Column();
  Column zirornPROMET = new Column();
  Column zirornKONTOKOMP = new Column();
*/
  public static zirorn getDataModule() {
    if (zirornclass == null) {
      zirornclass = new zirorn();
    }
    return zirornclass;
  }

  public QueryDataSet getQueryDataSet() {
    return zirorn;
  }

  public QueryDataSet getAktiv() {
    return zirornaktiv;
  }

  public zirorn() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    initModule();
/*    zirornLOKK.setCaption("Status zauzetosti");
    zirornLOKK.setColumnName("LOKK");
    zirornLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    zirornLOKK.setPrecision(1);
    zirornLOKK.setTableName("ZIRORN");
    zirornLOKK.setServerColumnName("LOKK");
    zirornLOKK.setSqlType(1);
    zirornLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    zirornLOKK.setDefault("N");
    zirornAKTIV.setCaption("Aktivan - neaktivan");
    zirornAKTIV.setColumnName("AKTIV");
    zirornAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    zirornAKTIV.setPrecision(1);
    zirornAKTIV.setTableName("ZIRORN");
    zirornAKTIV.setServerColumnName("AKTIV");
    zirornAKTIV.setSqlType(1);
    zirornAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    zirornAKTIV.setDefault("D");
    zirornCORG.setCaption("Knjigovodstvo");
    zirornCORG.setColumnName("CORG");
    zirornCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    zirornCORG.setPrecision(12);
    zirornCORG.setRowId(true);
    zirornCORG.setTableName("ZIRORN");
    zirornCORG.setServerColumnName("CORG");
    zirornCORG.setSqlType(1);
    zirornZIRO.setCaption("Ra\u010Dun");
    zirornZIRO.setColumnName("ZIRO");
    zirornZIRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    zirornZIRO.setPrecision(40);
    zirornZIRO.setRowId(true);
    zirornZIRO.setTableName("ZIRORN");
    zirornZIRO.setServerColumnName("ZIRO");
    zirornZIRO.setSqlType(1);
    zirornCVRNAL.setCaption("Vrsta naloga");
    zirornCVRNAL.setColumnName("CVRNAL");
    zirornCVRNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    zirornCVRNAL.setPrecision(2);
    zirornCVRNAL.setTableName("ZIRORN");
    zirornCVRNAL.setServerColumnName("CVRNAL");
    zirornCVRNAL.setSqlType(1);
    zirornOZNVAL.setCaption("Valuta");
    zirornOZNVAL.setColumnName("OZNVAL");
    zirornOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    zirornOZNVAL.setPrecision(3);
    zirornOZNVAL.setTableName("ZIRORN");
    zirornOZNVAL.setServerColumnName("OZNVAL");
    zirornOZNVAL.setSqlType(1);
    zirornDEV.setCaption("Devizni");
    zirornDEV.setColumnName("DEV");
    zirornDEV.setDataType(com.borland.dx.dataset.Variant.STRING);
    zirornDEV.setPrecision(1);
    zirornDEV.setTableName("ZIRORN");
    zirornDEV.setServerColumnName("DEV");
    zirornDEV.setSqlType(1);
    zirornDEV.setDefault("N");
    zirornBROJKONTA.setCaption("Konto prometa");
    zirornBROJKONTA.setColumnName("BROJKONTA");
    zirornBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    zirornBROJKONTA.setPrecision(8);
    zirornBROJKONTA.setTableName("ZIRORN");
    zirornBROJKONTA.setServerColumnName("BROJKONTA");
    zirornBROJKONTA.setSqlType(1);
    zirornPROMET.setCaption("Stavka prometa");
    zirornPROMET.setColumnName("PROMET");
    zirornPROMET.setDataType(com.borland.dx.dataset.Variant.STRING);
    zirornPROMET.setPrecision(1);
    zirornPROMET.setTableName("ZIRORN");
    zirornPROMET.setServerColumnName("PROMET");
    zirornPROMET.setSqlType(1);
    zirornKONTOKOMP.setCaption("Konto kompenzacija");
    zirornKONTOKOMP.setColumnName("KONTOKOMP");
    zirornKONTOKOMP.setDataType(com.borland.dx.dataset.Variant.STRING);
    zirornKONTOKOMP.setPrecision(8);
    zirornKONTOKOMP.setTableName("ZIRORN");
    zirornKONTOKOMP.setServerColumnName("KONTOKOMP");
    zirornKONTOKOMP.setSqlType(1);
    zirorn.setResolver(dm.getQresolver());
    zirorn.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from zirorn", null, true, Load.ALL));
 setColumns(new Column[] {zirornLOKK, zirornAKTIV, zirornCORG, zirornZIRO, zirornCVRNAL, zirornOZNVAL, zirornDEV, zirornBROJKONTA, zirornPROMET, zirornKONTOKOMP});
*/
    createFilteredDataSet(zirornaktiv, "aktiv = 'D'");
  }

  /*public void setall() {

    ddl.create("zirorn")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addChar("ziro", 40, true)
       .addChar("cvrnal", 2)
       .addChar("oznval", 3)
       .addChar("dev", 1, "N")
       .addChar("brojkonta", 8)
       .addChar("promet", 1)
       .addChar("kontokomp", 8)
       .addPrimaryKey("corg,ziro");


    Naziv = "zirorn";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"ziro"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }*/
}
