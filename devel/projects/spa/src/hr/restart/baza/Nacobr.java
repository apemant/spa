/****license*****************************************************************
**   file: Nacobr.java
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



public class Nacobr extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Nacobr Nacobrclass;

  QueryDataSet nacobr = new raDataSet();

  Column nacobrLOKK = new Column();
  Column nacobrAKTIV = new Column();
  Column nacobrCOBR = new Column();
  Column nacobrOPIS = new Column();
  Column nacobrFORMULA = new Column();
  Column nacobrUNSATI = new Column();
  Column nacobrUNKOEF = new Column();
  Column nacobrUNIZNOS = new Column();
  Column nacobrPARAMETRI = new Column();

  public static Nacobr getDataModule() {
    if (Nacobrclass == null) {
      Nacobrclass = new Nacobr();
    }
    return Nacobrclass;
  }

  public QueryDataSet getQueryDataSet() {
    return nacobr;
  }

  public Nacobr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    nacobrLOKK.setCaption("Status zauzetosti");
    nacobrLOKK.setColumnName("LOKK");
    nacobrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacobrLOKK.setPrecision(1);
    nacobrLOKK.setTableName("NACOBR");
    nacobrLOKK.setServerColumnName("LOKK");
    nacobrLOKK.setSqlType(1);
    nacobrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    nacobrLOKK.setDefault("N");
    nacobrAKTIV.setCaption("Aktivan - neaktivan");
    nacobrAKTIV.setColumnName("AKTIV");
    nacobrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacobrAKTIV.setPrecision(1);
    nacobrAKTIV.setTableName("NACOBR");
    nacobrAKTIV.setServerColumnName("AKTIV");
    nacobrAKTIV.setSqlType(1);
    nacobrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    nacobrAKTIV.setDefault("D");
    nacobrCOBR.setCaption("Oznaka");
    nacobrCOBR.setColumnName("COBR");
    nacobrCOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    nacobrCOBR.setPrecision(2);
    nacobrCOBR.setRowId(true);
    nacobrCOBR.setTableName("NACOBR");
    nacobrCOBR.setServerColumnName("COBR");
    nacobrCOBR.setSqlType(5);
    nacobrCOBR.setWidth(2);
    nacobrOPIS.setCaption("Opis");
    nacobrOPIS.setColumnName("OPIS");
    nacobrOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacobrOPIS.setPrecision(50);
    nacobrOPIS.setTableName("NACOBR");
    nacobrOPIS.setServerColumnName("OPIS");
    nacobrOPIS.setSqlType(1);
    nacobrFORMULA.setCaption("Formula izra\u010Duna");
    nacobrFORMULA.setColumnName("FORMULA");
    nacobrFORMULA.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacobrFORMULA.setPrecision(100);
    nacobrFORMULA.setTableName("NACOBR");
    nacobrFORMULA.setServerColumnName("FORMULA");
    nacobrFORMULA.setSqlType(1);
    nacobrUNSATI.setCaption("Unos sati");
    nacobrUNSATI.setColumnName("UNSATI");
    nacobrUNSATI.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacobrUNSATI.setPrecision(1);
    nacobrUNSATI.setTableName("NACOBR");
    nacobrUNSATI.setServerColumnName("UNSATI");
    nacobrUNSATI.setSqlType(1);
    nacobrUNSATI.setDefault("D");
    nacobrUNKOEF.setCaption("Unos koeficijenta");
    nacobrUNKOEF.setColumnName("UNKOEF");
    nacobrUNKOEF.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacobrUNKOEF.setPrecision(1);
    nacobrUNKOEF.setTableName("NACOBR");
    nacobrUNKOEF.setServerColumnName("UNKOEF");
    nacobrUNKOEF.setSqlType(1);
    nacobrUNKOEF.setDefault("D");
    nacobrUNIZNOS.setCaption("Unos iznosa");
    nacobrUNIZNOS.setColumnName("UNIZNOS");
    nacobrUNIZNOS.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacobrUNIZNOS.setPrecision(1);
    nacobrUNIZNOS.setTableName("NACOBR");
    nacobrUNIZNOS.setServerColumnName("UNIZNOS");
    nacobrUNIZNOS.setSqlType(1);
    nacobrUNIZNOS.setDefault("D");
    nacobrPARAMETRI.setCaption("Parametri");
    nacobrPARAMETRI.setColumnName("PARAMETRI");
    nacobrPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacobrPARAMETRI.setPrecision(20);
    nacobrPARAMETRI.setTableName("NACOBR");
    nacobrPARAMETRI.setServerColumnName("PARAMETRI");
    nacobrPARAMETRI.setSqlType(1);
    nacobrPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    nacobr.setResolver(dm.getQresolver());
    nacobr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Nacobr", null, true, Load.ALL));
 setColumns(new Column[] {nacobrLOKK, nacobrAKTIV, nacobrCOBR, nacobrOPIS, nacobrFORMULA, nacobrUNSATI, nacobrUNKOEF, nacobrUNIZNOS, nacobrPARAMETRI});
  }

  public void setall() {

    ddl.create("Nacobr")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("cobr", 2, true)
       .addChar("opis", 50)
       .addChar("formula", 100)
       .addChar("unsati", 1, "D")
       .addChar("unkoef", 1, "D")
       .addChar("uniznos", 1, "D")
       .addChar("parametri", 20)
       .addPrimaryKey("cobr");


    Naziv = "Nacobr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
