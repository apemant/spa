/****license*****************************************************************
**   file: vshrab_rab.java
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

public class vshrab_rab extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static vshrab_rab vshrab_rabclass;
  dM dm  = dM.getDataModule();
  QueryDataSet vshrab_rab = new QueryDataSet();
  Column vshrab_rabLOKK = new Column();
  Column vshrab_rabAKTIV = new Column();
  Column vshrab_rabCSHRAB = new Column();
  Column vshrab_rabRBR = new Column();
  Column vshrab_rabCRAB = new Column();
  Column vshrab_rabPRAB = new Column();
  Column vshrab_rabRABNARAB = new Column();

  public static vshrab_rab getDataModule() {
    if (vshrab_rabclass == null) {
      vshrab_rabclass = new vshrab_rab();
    }
    return vshrab_rabclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return vshrab_rab;
  }

  public vshrab_rab() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    vshrab_rabRBR.setCaption("Rbr");
    vshrab_rabRBR.setColumnName("RBR");
    vshrab_rabRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vshrab_rabRBR.setRowId(true);
    vshrab_rabRBR.setTableName("VSHRAB_RAB");
    vshrab_rabRBR.setServerColumnName("RBR");
    vshrab_rabRBR.setSqlType(5);
    vshrab_rabCSHRAB.setCaption("Šifra sheme");
    vshrab_rabCSHRAB.setColumnName("CSHRAB");
    vshrab_rabCSHRAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    vshrab_rabCSHRAB.setPrecision(3);
    vshrab_rabCSHRAB.setRowId(true);
    vshrab_rabCSHRAB.setTableName("VSHRAB_RAB");
    vshrab_rabCSHRAB.setServerColumnName("CSHRAB");
    vshrab_rabCSHRAB.setSqlType(1);
    vshrab_rabCRAB.setCaption("Šifra rabata");
    vshrab_rabCRAB.setColumnName("CRAB");
    vshrab_rabCRAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    vshrab_rabCRAB.setPrecision(2);
    vshrab_rabCRAB.setTableName("VSHRAB_RAB");
    vshrab_rabCRAB.setServerColumnName("CRAB");
    vshrab_rabCRAB.setSqlType(1);
    vshrab_rabRABNARAB.setCaption("RnR");
    vshrab_rabRABNARAB.setColumnName("RABNARAB");
    vshrab_rabRABNARAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    vshrab_rabRABNARAB.setDefault("N");
    vshrab_rabRABNARAB.setPrecision(1);
    vshrab_rabRABNARAB.setTableName("VSHRAB_RAB");
    vshrab_rabRABNARAB.setServerColumnName("RABNARAB");
    vshrab_rabRABNARAB.setSqlType(1);
    vshrab_rabPRAB.setCaption("Posto rabata");
    vshrab_rabPRAB.setColumnName("PRAB");
    vshrab_rabPRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vshrab_rabPRAB.setDisplayMask("###,###,##0.00");
    vshrab_rabPRAB.setDefault("0");
    vshrab_rabPRAB.setPrecision(10);
    vshrab_rabPRAB.setScale(2);
    vshrab_rabPRAB.setTableName("VSHRAB_RAB");
    vshrab_rabPRAB.setServerColumnName("PRAB");
    vshrab_rabPRAB.setSqlType(2);
    vshrab_rabAKTIV.setColumnName("AKTIV");
    vshrab_rabAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vshrab_rabAKTIV.setDefault("D");
    vshrab_rabAKTIV.setPrecision(1);
    vshrab_rabAKTIV.setTableName("VSHRAB_RAB");
    vshrab_rabAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vshrab_rabAKTIV.setServerColumnName("AKTIV");
    vshrab_rabAKTIV.setSqlType(1);
    vshrab_rabLOKK.setColumnName("LOKK");
    vshrab_rabLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vshrab_rabLOKK.setDefault("N");
    vshrab_rabLOKK.setPrecision(1);
    vshrab_rabLOKK.setTableName("VSHRAB_RAB");
    vshrab_rabLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vshrab_rabLOKK.setServerColumnName("LOKK");
    vshrab_rabLOKK.setSqlType(1);
    vshrab_rab.setResolver(dm.getQresolver());
    vshrab_rab.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from vshrab_rab", null, true, Load.ALL));
 setColumns(new Column[] {vshrab_rabLOKK, vshrab_rabAKTIV, vshrab_rabCSHRAB, vshrab_rabRBR, vshrab_rabCRAB, vshrab_rabPRAB, vshrab_rabRABNARAB});
  }

  public void setall(){

    /*SqlDefTabela = "create table vshrab_rab " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cshrab char(3) not null , "+
      "rbr numeric(4,0) not null, " + // Redni broj stavke
      "crab  char(2) CHARACTER SET WIN1250 , "+
      "prab numeric(6,2),"+
      "rabnarab char(1) CHARACTER SET WIN1250 default 'N'," + // Rabat na rabat
       Primary Key (cshrab,rbr))" ; */

    ddl.create("vshrab_rab")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cshrab", 3, true)
       .addShort("rbr", 4, true)
       .addChar("crab", 2)
       .addFloat("prab", 6, 2)
       .addChar("rabnarab", 1, "N")
       .addPrimaryKey("cshrab,rbr");

    Naziv="vshrab_rab";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);


    /*
    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+"ivshrab_rabkey on vshrab_rab (cshrab,rbr)" };

    NaziviIdx=new String[]{"ivshrab_rabkey" }; */

   }
}