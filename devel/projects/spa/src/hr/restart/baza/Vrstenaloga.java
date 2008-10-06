/****license*****************************************************************
**   file: Vrstenaloga.java
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



public class Vrstenaloga extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Vrstenaloga Vrstenalogaclass;

  QueryDataSet vrnal = new raDataSet();

  Column vrnalLOKK = new Column();
  Column vrnalAKTIV = new Column();
  Column vrnalCVRNAL = new Column();
  Column vrnalOPISVRNAL = new Column();

  public static Vrstenaloga getDataModule() {
    if (Vrstenalogaclass == null) {
      Vrstenalogaclass = new Vrstenaloga();
    }
    return Vrstenalogaclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return vrnal;
  }

  public Vrstenaloga() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vrnalLOKK.setCaption("Status zauzetosti");
    vrnalLOKK.setColumnName("LOKK");
    vrnalLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrnalLOKK.setPrecision(1);
    vrnalLOKK.setTableName("VRSTENALOGA");
    vrnalLOKK.setServerColumnName("LOKK");
    vrnalLOKK.setSqlType(1);
    vrnalLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrnalLOKK.setDefault("N");
    vrnalAKTIV.setCaption("Aktivan - neaktivan");
    vrnalAKTIV.setColumnName("AKTIV");
    vrnalAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrnalAKTIV.setPrecision(1);
    vrnalAKTIV.setTableName("VRSTENALOGA");
    vrnalAKTIV.setServerColumnName("AKTIV");
    vrnalAKTIV.setSqlType(1);
    vrnalAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrnalAKTIV.setDefault("D");
    vrnalCVRNAL.setCaption("Oznaka");
    vrnalCVRNAL.setColumnName("CVRNAL");
    vrnalCVRNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrnalCVRNAL.setPrecision(2);
    vrnalCVRNAL.setRowId(true);
    vrnalCVRNAL.setTableName("VRSTENALOGA");
    vrnalCVRNAL.setServerColumnName("CVRNAL");
    vrnalCVRNAL.setSqlType(1);
    vrnalOPISVRNAL.setCaption("Opis");
    vrnalOPISVRNAL.setColumnName("OPISVRNAL");
    vrnalOPISVRNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrnalOPISVRNAL.setPrecision(40);
    vrnalOPISVRNAL.setTableName("VRSTENALOGA");
    vrnalOPISVRNAL.setServerColumnName("OPISVRNAL");
    vrnalOPISVRNAL.setSqlType(1);
    vrnal.setResolver(dm.getQresolver());
    vrnal.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from Vrstenaloga", null, true, Load.ALL));
 setColumns(new Column[] {vrnalLOKK, vrnalAKTIV, vrnalCVRNAL, vrnalOPISVRNAL});
  }

  public void setall() {

    ddl.create("Vrstenaloga")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cvrnal", 2, true)
       .addChar("opisvrnal", 40)
       .addPrimaryKey("cvrnal");


    Naziv = "Vrstenaloga";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
