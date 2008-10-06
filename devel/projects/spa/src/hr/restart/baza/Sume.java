/****license*****************************************************************
**   file: Sume.java
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



public class Sume extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Sume Sumeclass;

  QueryDataSet sume = new QueryDataSet();

  Column sumeLOKK = new Column();
  Column sumeAKTIV = new Column();
  Column sumeCSUME = new Column();
  Column sumeOPIS = new Column();
  Column sumeVRSTA = new Column();
  Column sumePARAMETRI = new Column();

  public static Sume getDataModule() {
    if (Sumeclass == null) {
      Sumeclass = new Sume();
    }
    return Sumeclass;
  }

  public QueryDataSet getQueryDataSet() {
    return sume;
  }

  public Sume() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    sumeLOKK.setCaption("Status zauzetosti");
    sumeLOKK.setColumnName("LOKK");
    sumeLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    sumeLOKK.setPrecision(1);
    sumeLOKK.setTableName("SUME");
    sumeLOKK.setServerColumnName("LOKK");
    sumeLOKK.setSqlType(1);
    sumeLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    sumeLOKK.setDefault("N");
    sumeAKTIV.setCaption("Aktivan - neaktivan");
    sumeAKTIV.setColumnName("AKTIV");
    sumeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    sumeAKTIV.setPrecision(1);
    sumeAKTIV.setTableName("SUME");
    sumeAKTIV.setServerColumnName("AKTIV");
    sumeAKTIV.setSqlType(1);
    sumeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    sumeAKTIV.setDefault("D");
    sumeCSUME.setCaption("Oznaka");
    sumeCSUME.setColumnName("CSUME");
    sumeCSUME.setDataType(com.borland.dx.dataset.Variant.INT);
    sumeCSUME.setPrecision(5);
    sumeCSUME.setRowId(true);
    sumeCSUME.setTableName("SUME");
    sumeCSUME.setServerColumnName("CSUME");
    sumeCSUME.setSqlType(4);
    sumeCSUME.setWidth(5);
    sumeOPIS.setCaption("Opis");
    sumeOPIS.setColumnName("OPIS");
    sumeOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    sumeOPIS.setPrecision(30);
    sumeOPIS.setTableName("SUME");
    sumeOPIS.setServerColumnName("OPIS");
    sumeOPIS.setSqlType(1);
    sumeVRSTA.setCaption("Vrsta sume");
    sumeVRSTA.setColumnName("VRSTA");
    sumeVRSTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    sumeVRSTA.setPrecision(1);
    sumeVRSTA.setTableName("SUME");
    sumeVRSTA.setServerColumnName("VRSTA");
    sumeVRSTA.setSqlType(1);
    sumeVRSTA.setDefault("1");
    sumePARAMETRI.setCaption("Parametri");
    sumePARAMETRI.setColumnName("PARAMETRI");
    sumePARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    sumePARAMETRI.setPrecision(20);
    sumePARAMETRI.setTableName("SUME");
    sumePARAMETRI.setServerColumnName("PARAMETRI");
    sumePARAMETRI.setSqlType(1);
    sumePARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    sume.setResolver(dm.getQresolver());
    sume.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Sume", null, true, Load.ALL));
 setColumns(new Column[] {sumeLOKK, sumeAKTIV, sumeCSUME, sumeOPIS, sumeVRSTA, sumePARAMETRI});
  }

  public void setall() {

    ddl.create("Sume")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("csume", 5, true)
       .addChar("opis", 30)
       .addChar("vrsta", 1, "1")
       .addChar("parametri", 20)
       .addPrimaryKey("csume");


    Naziv = "Sume";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
