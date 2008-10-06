/****license*****************************************************************
**   file: shrab.java
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

public class shrab extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static shrab shrabclass;
  dM dm  = dM.getDataModule();
  QueryDataSet shrab = new raDataSet();
  Column shrabLOKK = new Column();
  Column shrabAKTIV = new Column();
  Column shrabCSHRAB = new Column();
  Column shrabNSHRAB = new Column();
  Column shrabUPRAB = new Column();

  public static shrab getDataModule() {
    if (shrabclass == null) {
      shrabclass = new shrab();
    }
    return shrabclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return shrab;
  }
  public shrab() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    shrabUPRAB.setCaption("Posto rabata");
    shrabUPRAB.setColumnName("UPRAB");
    shrabUPRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    shrabUPRAB.setDisplayMask("###,###,##0.00");
    shrabUPRAB.setDefault("0");
    shrabUPRAB.setPrecision(10);
    shrabUPRAB.setScale(2);
    shrabUPRAB.setTableName("SHRAB");
    shrabUPRAB.setSqlType(2);
    shrabUPRAB.setServerColumnName("UPRAB");
    shrabNSHRAB.setCaption("Naziv");
    shrabNSHRAB.setColumnName("NSHRAB");
    shrabNSHRAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    shrabNSHRAB.setPrecision(50);
    shrabNSHRAB.setTableName("SHRAB");
    shrabNSHRAB.setWidth(30);
    shrabNSHRAB.setSqlType(1);
    shrabNSHRAB.setServerColumnName("NSHRAB");
    shrabCSHRAB.setCaption("Šifra");
    shrabCSHRAB.setColumnName("CSHRAB");
    shrabCSHRAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    shrabCSHRAB.setPrecision(3);
    shrabCSHRAB.setRowId(true);
    shrabCSHRAB.setTableName("SHRAB");
    shrabCSHRAB.setWidth(5);
    shrabCSHRAB.setSqlType(1);
    shrabCSHRAB.setServerColumnName("CSHRAB");
    shrabAKTIV.setColumnName("AKTIV");
    shrabAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    shrabAKTIV.setDefault("D");
    shrabAKTIV.setPrecision(1);
    shrabAKTIV.setTableName("SHRAB");
    shrabAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    shrabAKTIV.setSqlType(1);
    shrabAKTIV.setServerColumnName("AKTIV");
    shrabLOKK.setColumnName("LOKK");
    shrabLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    shrabLOKK.setDefault("N");
    shrabLOKK.setPrecision(1);
    shrabLOKK.setTableName("SHRAB");
    shrabLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    shrabLOKK.setSqlType(1);
    shrabLOKK.setServerColumnName("LOKK");
    shrab.setResolver(dm.getQresolver());
    shrab.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from shrab", null, true, Load.ALL));
 setColumns(new Column[] {shrabLOKK, shrabAKTIV, shrabCSHRAB, shrabNSHRAB, shrabUPRAB});
  }

 public void setall(){

    /*SqlDefTabela = "create table shrab " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cshrab char(3) not null , "+
      "nshrab char(50) character set win1250,"+
      "uprab numeric(6,2),"+ /// Ukupan postotak rabata
      "Primary Key (cshrab))" ;*/

    ddl.create("shrab")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cshrab", 3, true)
       .addChar("nshrab", 50)
       .addFloat("uprab", 6, 2)
       .addPrimaryKey("cshrab");

    Naziv="shrab";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+"ishrabkey on shrab (cshrab)" };

    NaziviIdx=new String[]{"ishrabkey" };
*/
  }
}