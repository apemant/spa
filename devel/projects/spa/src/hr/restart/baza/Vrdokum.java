/****license*****************************************************************
**   file: Vrdokum.java
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
import java.util.ResourceBundle;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Vrdokum extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Vrdokum vrdokumclass;
  dM dm  = dM.getDataModule();
  QueryDataSet vrdokumall = new raDataSet();
  QueryDataSet vrdokum = new raDataSet();
  QueryDataSet uvrdokum = new raDataSet();
  QueryDataSet ivrdokum = new raDataSet();

  Column vrdokumVRDOK = new Column();
  Column vrdokumNAZDOK = new Column();
  Column vrdokumVRSDOK = new Column();
  Column vrdokumTIPDOK = new Column();
  Column vrdokumKNJIZ = new Column();
  Column vrdokumAPP = new Column();
  Column vrdokumID = new Column();
  Column vrdokumISPLOGO = new Column();
  Column vrdokumPARAM = new Column();

  public static Vrdokum getDataModule() {
    if (vrdokumclass == null) {
      vrdokumclass = new Vrdokum();
    }
    return vrdokumclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return vrdokum;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAll() {
    return vrdokumall;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getUlazni() {
    return uvrdokum;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getIzlazni() {
    return ivrdokum;
  }


  public Vrdokum() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    vrdokumAPP.setCaption("Aplikacija");
    vrdokumAPP.setColumnName("APP");
    vrdokumAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrdokumAPP.setPrecision(10);
    vrdokumAPP.setTableName("VRDOKUM");
    vrdokumAPP.setSqlType(1);
    vrdokumAPP.setServerColumnName("APP");
    vrdokumNAZDOK.setCaption("Naziv");
    vrdokumNAZDOK.setColumnName("NAZDOK");
    vrdokumNAZDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrdokumNAZDOK.setPrecision(40);
    vrdokumNAZDOK.setTableName("VRDOKUM");
    vrdokumNAZDOK.setServerColumnName("NAZDOK");
    vrdokumNAZDOK.setSqlType(1);
    vrdokumNAZDOK.setWidth(30);
    vrdokumTIPDOK.setCaption("Tip");
    vrdokumTIPDOK.setColumnName("TIPDOK");
    vrdokumTIPDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrdokumTIPDOK.setDefault("O");
    vrdokumTIPDOK.setPrecision(2);
    vrdokumTIPDOK.setTableName("VRDOKUM");
    vrdokumTIPDOK.setServerColumnName("TIPDOK");
    vrdokumTIPDOK.setSqlType(1);
    vrdokumTIPDOK.setWidth(2);
    vrdokumVRSDOK.setCaption("Vrsta");
    vrdokumVRSDOK.setColumnName("VRSDOK");
    vrdokumVRSDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrdokumVRSDOK.setDefault("UI");
    vrdokumVRSDOK.setPrecision(2);
    vrdokumVRSDOK.setTableName("VRDOKUM");
    vrdokumVRSDOK.setServerColumnName("VRSDOK");
    vrdokumVRSDOK.setSqlType(1);
    vrdokumVRSDOK.setWidth(2);

    vrdokumKNJIZ.setCaption("Knjiženje");
    vrdokumKNJIZ.setColumnName("KNJIZ");
    vrdokumKNJIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrdokumKNJIZ.setDefault("D");
    vrdokumKNJIZ.setPrecision(1);
    vrdokumKNJIZ.setTableName("VRDOKUM");
    vrdokumKNJIZ.setServerColumnName("KNJIZ");
    vrdokumKNJIZ.setSqlType(1);
    vrdokumKNJIZ.setWidth(2);

    vrdokumVRDOK.setCaption("Kratica");
    vrdokumVRDOK.setColumnName("VRDOK");
    vrdokumVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrdokumVRDOK.setDefault("");
    vrdokumVRDOK.setRowId(true);
    vrdokumVRDOK.setPrecision(3);
    vrdokumVRDOK.setTableName("VRDOKUM");
    vrdokumVRDOK.setServerColumnName("VRDOK");
    vrdokumVRDOK.setSqlType(1);
    vrdokumVRDOK.setWidth(4);

    vrdokumID.setCaption("Šifra");
    vrdokumID.setColumnName("ID");
    vrdokumID.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrdokumID.setPrecision(2);
    vrdokumID.setTableName("VRDOKUM");
    vrdokumID.setServerColumnName("ID");
    vrdokumID.setSqlType(1);

    vrdokumISPLOGO.setCaption("Ispis loga");
    vrdokumISPLOGO.setColumnName("ISPLOGO");
    vrdokumISPLOGO.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrdokumISPLOGO.setPrecision(1);
    vrdokumISPLOGO.setDefault("D");
    vrdokumISPLOGO.setTableName("VRDOKUM");
    vrdokumISPLOGO.setServerColumnName("ISPLOGO");
    vrdokumISPLOGO.setSqlType(1);

    vrdokumPARAM.setCaption("Parametri");
    vrdokumPARAM.setColumnName("PARAM");
    vrdokumPARAM.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrdokumPARAM.setPrecision(20);
    vrdokumPARAM.setTableName("VRDOKUM");
    vrdokumPARAM.setServerColumnName("PARAM");
    vrdokumPARAM.setSqlType(1);

    vrdokum.setResolver(dm.getQresolver());
    vrdokum.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * " +
      "FROM VRDOKUM", null, true, Load.ALL));
 setColumns(new Column[] {vrdokumVRDOK, vrdokumNAZDOK, vrdokumVRSDOK, vrdokumTIPDOK, vrdokumKNJIZ, vrdokumAPP, vrdokumID, vrdokumISPLOGO, vrdokumPARAM});

    createFilteredDataSet(vrdokumall, "");
    createFilteredDataSet(uvrdokum, "vrsdok = 'U'");
    createFilteredDataSet(ivrdokum, "vrsdok = 'I'");
  }


  public void setall(){


    ddl.create("vrdokum")
       .addChar("vrdok", 3, true)
       .addChar("nazdok", 40)
       .addChar("vrsdok", 2)
       .addChar("tipdok", 2)
       .addChar("knjiz", 1, "D")
       .addChar("app", 10)
       .addChar("id", 2)
       .addChar("isplogo", 1, "D")
       .addChar("param", 20, "N")
       .addPrimaryKey("vrdok");

    Naziv="Vrdokum";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    NaziviIdx=new String[]{"ilokkshkonta","iaktivshkonta","icskl","inazskl"};

  */
  }
}