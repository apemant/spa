/****license*****************************************************************
**   file: Parametri.java
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

public class Parametri extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Parametri Parametriclass;
  dM dm  = dM.getDataModule();
  Column parametriAPP = new Column();
  Column parametriPARAM = new Column();
  Column parametriOPISPAR = new Column();
  Column parametriVRIJEDNOST = new Column();
  Column parametriSISTEMSKI = new Column();
  QueryDataSet parametri = new raDataSet();
  QueryDataSet parametriloc = new raDataSet();
  QueryDataSet parametriglob = new raDataSet();

  public static Parametri getDataModule() {
    if (Parametriclass == null) {
      Parametriclass = new Parametri();
    }
    return Parametriclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getLocParametri() {
    return parametriloc;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getGlobParametri() {
    return parametriglob;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return parametri;
  }



  public Parametri() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    parametriVRIJEDNOST.setCaption("Defaultna vrijednost");
    parametriVRIJEDNOST.setColumnName("VRIJEDNOST");
    parametriVRIJEDNOST.setDataType(com.borland.dx.dataset.Variant.STRING);
    parametriVRIJEDNOST.setPrecision(200);
    parametriVRIJEDNOST.setWidth(12);
    parametriVRIJEDNOST.setTableName("PARAMETRI");
    parametriVRIJEDNOST.setSqlType(1);
    parametriVRIJEDNOST.setServerColumnName("VRIJEDNOST");
    parametriPARAM.setCaption("Parametar");
    parametriPARAM.setColumnName("PARAM");
    parametriPARAM.setDataType(com.borland.dx.dataset.Variant.STRING);
    parametriPARAM.setPrecision(25);
    parametriPARAM.setRowId(true);
    parametriPARAM.setTableName("PARAMETRI");
    parametriPARAM.setSqlType(1);
    parametriPARAM.setServerColumnName("PARAM");
    parametriAPP.setCaption("Aplikacija");
    parametriAPP.setColumnName("APP");
    parametriAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    parametriAPP.setPrecision(10);
    parametriAPP.setRowId(true);
    parametriAPP.setTableName("PARAMETRI");
    parametriAPP.setSqlType(1);
    parametriAPP.setServerColumnName("APP");
    parametriOPISPAR.setCaption("Opis parametara");
    parametriOPISPAR.setColumnName("OPISPAR");
    parametriOPISPAR.setDataType(com.borland.dx.dataset.Variant.STRING);
    parametriOPISPAR.setPrecision(100);
    parametriOPISPAR.setTableName("PARAMETRI");
    parametriOPISPAR.setWidth(20);
    parametriOPISPAR.setSqlType(1);
    parametriOPISPAR.setServerColumnName("OPISPAR");

    parametriSISTEMSKI.setColumnName("SISTEMSKI");
    parametriSISTEMSKI.setCaption("Vrsta");
    parametriSISTEMSKI.setDataType(com.borland.dx.dataset.Variant.STRING);
    parametriSISTEMSKI.setDefault("G");
    parametriSISTEMSKI.setPrecision(1);
    parametriSISTEMSKI.setTableName("PARAMETRI");
    parametriSISTEMSKI.setServerColumnName("SISTEMSKI");
    parametriSISTEMSKI.setSqlType(1);

    parametri.setResolver(dm.getQresolver());
    parametri.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from parametri", null, true, Load.ALL));
 setColumns(new Column[] {parametriAPP, parametriPARAM, parametriOPISPAR, parametriVRIJEDNOST, parametriSISTEMSKI});

    createFilteredDataSet(parametriloc, "sistemski = 'L'");
    createFilteredDataSet(parametriglob, "sistemski != 'S'");
  }

 public void setall(){

   /* SqlDefTabela = "create table Parametri " +
      "(app char(10) CHARACTER SET WIN1250 not null , " + //Status zauzetosti
      "param char(10) CHARACTER SET WIN1250 not null , " +
      "opispar char(30)," +
      "vrijednost char(5),"+
      "Primary Key (app,param))" ;*/

    ddl.create("parametri")
       .addChar("app", 10, true)
       .addChar("param", 25, true)
       .addChar("opispar", 100)
       .addChar("vrijednost", 200)
       .addChar("sistemski", 1, "G")
       .addPrimaryKey("app,param");

    Naziv="Parametri";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

/*    NaziviIdx=new String[]{"iappParametri","iparamParametri","iappparamParametri"};

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Parametri (app)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Parametri (param)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Parametri (app,param)" }; */
  }
}