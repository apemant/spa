/****license*****************************************************************
**   file: Tecajevi.java
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

public class Tecajevi extends KreirDrop implements DataModule {

  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Tecajevi Tecajeviclass;
  dM dm  = dM.getDataModule();
  QueryDataSet tecajevi = new raDataSet();
  QueryDataSet tecajeviaktiv = new raDataSet();

  Column tecajeviLOKK = new Column();
  Column tecajeviAKTIV = new Column();
  Column tecajeviOZNVAL = new Column();
  Column tecajeviDATVAL = new Column();
  Column tecajeviTECKUP = new Column();
  Column tecajeviTECSRED = new Column();
  Column tecajeviTECPROD = new Column();


  public static Tecajevi getDataModule() {
    if (Tecajeviclass == null) {
      Tecajeviclass = new Tecajevi();
    }
    return Tecajeviclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return tecajevi;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return tecajeviaktiv;
  }

  public Tecajevi() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    tecajeviOZNVAL.setCaption("Oznaka");
    tecajeviOZNVAL.setColumnName("OZNVAL");
    tecajeviOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    tecajeviOZNVAL.setPrecision(3);
    tecajeviOZNVAL.setRowId(true);
    tecajeviOZNVAL.setTableName("TECAJEVI");
    tecajeviOZNVAL.setSqlType(1);
    tecajeviOZNVAL.setServerColumnName("OZNVAL");
    tecajeviTECPROD.setCaption(dmRes.getString("tecajeviTECPROD_caption"));
    tecajeviTECPROD.setColumnName("TECPROD");
    tecajeviTECPROD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    tecajeviTECPROD.setDisplayMask("###,###,###0.000000");
    tecajeviTECPROD.setDefault("0");
    tecajeviTECPROD.setPrecision(15);
    tecajeviTECPROD.setScale(6);
    tecajeviTECPROD.setTableName("TECAJEVI");
    tecajeviTECPROD.setSqlType(2);
    tecajeviTECPROD.setServerColumnName("TECPROD");
    tecajeviTECSRED.setCaption(dmRes.getString("tecajeviTECSRED_caption"));
    tecajeviTECSRED.setColumnName("TECSRED");
    tecajeviTECSRED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    tecajeviTECSRED.setDisplayMask("###,###,###0.000000");
    tecajeviTECSRED.setDefault("0");
    tecajeviTECSRED.setPrecision(15);
    tecajeviTECSRED.setScale(6);
    tecajeviTECSRED.setTableName("TECAJEVI");
    tecajeviTECSRED.setSqlType(2);
    tecajeviTECSRED.setServerColumnName("TECSRED");
    tecajeviTECKUP.setCaption(dmRes.getString("tecajeviTECKUP_caption"));
    tecajeviTECKUP.setColumnName("TECKUP");
    tecajeviTECKUP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    tecajeviTECKUP.setDisplayMask("###,###,##0.000000");
    tecajeviTECKUP.setDefault("0");
    tecajeviTECKUP.setPrecision(15);
    tecajeviTECKUP.setScale(6);
    tecajeviTECKUP.setTableName("TECAJEVI");
    tecajeviTECKUP.setSqlType(2);
    tecajeviTECKUP.setServerColumnName("TECKUP");
    tecajeviDATVAL.setCaption(dmRes.getString("tecajeviDATVAL_caption"));
    tecajeviDATVAL.setColumnName("DATVAL");
    tecajeviDATVAL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    tecajeviDATVAL.setDisplayMask("dd-MM-yyyy");
//    tecajeviDATVAL.setEditMask("dd-MM-yyyy");
    tecajeviDATVAL.setRowId(true);
    tecajeviDATVAL.setTableName("TECAJEVI");
    tecajeviDATVAL.setWidth(10);
    tecajeviDATVAL.setSqlType(93);
    tecajeviDATVAL.setServerColumnName("DATVAL");
    tecajeviAKTIV.setCaption(dmRes.getString("tecajeviAKTIV_caption"));
    tecajeviAKTIV.setPrecision(1);
    tecajeviAKTIV.setColumnName("AKTIV");
    tecajeviAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    tecajeviAKTIV.setDefault("D");
    tecajeviAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    tecajeviAKTIV.setSqlType(1);
    tecajeviAKTIV.setTableName("TECAJEVI");
    tecajeviAKTIV.setServerColumnName("AKTIV");
    tecajeviLOKK.setCaption("Lokk");
    tecajeviLOKK.setColumnName("LOKK");
    tecajeviLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    tecajeviLOKK.setPrecision(1);
    tecajeviLOKK.setDefault("N");
    tecajeviLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    tecajeviLOKK.setSqlType(1);
    tecajeviLOKK.setTableName("TECAJEVI");
    tecajeviLOKK.setServerColumnName("LOKK");
    tecajevi.setResolver(dm.getQresolver());
    tecajevi.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from tecajevi", null, true, Load.ALL));
 setColumns(new Column[] {tecajeviLOKK, tecajeviAKTIV, tecajeviOZNVAL, tecajeviDATVAL, tecajeviTECKUP, tecajeviTECSRED, tecajeviTECPROD});

    createFilteredDataSet(tecajeviaktiv, "aktiv = 'D'");
  }

  public void setall(){

    /*SqlDefTabela  = "create table Tecajevi " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "oznval char(3) CHARACTER SET WIN1250 not null,"+
      "datval date not null,"+
      "teckup numeric(17,6), " +
      "tecsred numeric(17,6), " +
      "tecprod numeric(17,6), " +
      "Primary Key (oznval,datval))" ; */

    ddl.create("tecajevi")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("oznval", 3, true)
       .addDate("datval", true)
       .addFloat("teckup", 17, 6)
       .addFloat("tecsred", 17, 6)
       .addFloat("tecprod", 17, 6)
       .addPrimaryKey("oznval,datval");

    Naziv="Tecajevi";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"datval"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);


    /*
    NaziviIdx=new String[]{"icvaluteTecajevi","idatumalTecajevi","ilokkTecajevi","iaktivTecajevi","iaktivkey"};


    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Tecajevi (oznval)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Tecajevi (datval)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[2] +" on Tecajevi (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[3] +" on Tecajevi (aktiv)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[4] +" on Tecajevi (oznval,datval)" };
  */
  }
}