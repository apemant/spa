/****license*****************************************************************
**   file: Plosnovice.java
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



public class Plosnovice extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Plosnovice Plosnoviceclass;

  QueryDataSet plos = new raDataSet();

  Column plosLOKK = new Column();
  Column plosAKTIV = new Column();
  Column plosCOSN = new Column();
  Column plosOPIS = new Column();
  Column plosVRSTA = new Column();
  Column plosPARAMETRI = new Column();

  public static Plosnovice getDataModule() {
    if (Plosnoviceclass == null) {
      Plosnoviceclass = new Plosnovice();
    }
    return Plosnoviceclass;
  }

  public QueryDataSet getQueryDataSet() {
    return plos;
  }

  public Plosnovice() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    plosLOKK.setCaption("Status zauzetosti");
    plosLOKK.setColumnName("LOKK");
    plosLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    plosLOKK.setPrecision(1);
    plosLOKK.setTableName("PLOSNOVICE");
    plosLOKK.setServerColumnName("LOKK");
    plosLOKK.setSqlType(1);
    plosLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    plosLOKK.setDefault("N");
    plosAKTIV.setCaption("Aktivan - neaktivan");
    plosAKTIV.setColumnName("AKTIV");
    plosAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    plosAKTIV.setPrecision(1);
    plosAKTIV.setTableName("PLOSNOVICE");
    plosAKTIV.setServerColumnName("AKTIV");
    plosAKTIV.setSqlType(1);
    plosAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    plosAKTIV.setDefault("D");
    plosCOSN.setCaption("Oznaka");
    plosCOSN.setColumnName("COSN");
    plosCOSN.setDataType(com.borland.dx.dataset.Variant.SHORT);
    plosCOSN.setPrecision(2);
    plosCOSN.setRowId(true);
    plosCOSN.setTableName("PLOSNOVICE");
    plosCOSN.setServerColumnName("COSN");
    plosCOSN.setSqlType(5);
    plosCOSN.setWidth(2);
    plosOPIS.setCaption("Opis");
    plosOPIS.setColumnName("OPIS");
    plosOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    plosOPIS.setPrecision(30);
    plosOPIS.setTableName("PLOSNOVICE");
    plosOPIS.setServerColumnName("OPIS");
    plosOPIS.setSqlType(1);
    plosVRSTA.setCaption("Vrsta");
    plosVRSTA.setColumnName("VRSTA");
    plosVRSTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    plosVRSTA.setPrecision(1);
    plosVRSTA.setTableName("PLOSNOVICE");
    plosVRSTA.setServerColumnName("VRSTA");
    plosVRSTA.setSqlType(1);
    plosVRSTA.setDefault("1");
    plosPARAMETRI.setCaption("Parametri");
    plosPARAMETRI.setColumnName("PARAMETRI");
    plosPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    plosPARAMETRI.setPrecision(20);
    plosPARAMETRI.setTableName("PLOSNOVICE");
    plosPARAMETRI.setServerColumnName("PARAMETRI");
    plosPARAMETRI.setSqlType(1);
    plosPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    plos.setResolver(dm.getQresolver());
    plos.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Plosnovice", null, true, Load.ALL));
 setColumns(new Column[] {plosLOKK, plosAKTIV, plosCOSN, plosOPIS, plosVRSTA, plosPARAMETRI});
  }

  public void setall() {

    ddl.create("Plosnovice")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("cosn", 2, true)
       .addChar("opis", 30)
       .addChar("vrsta", 1, "1")
       .addChar("parametri", 20)
       .addPrimaryKey("cosn");


    Naziv = "Plosnovice";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
