/****license*****************************************************************
**   file: rabati.java
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

public class rabati extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static rabati rabaticlass;
  dM dm  = dM.getDataModule();
  QueryDataSet rabati = new raDataSet();
  QueryDataSet rabatiaktiv = new raDataSet();
  Column rabatiLOKK = new Column();
  Column rabatiAKTIV = new Column();
  Column rabatiCRAB = new Column();
  Column rabatiPRAB = new Column();
  Column rabatiIRAB = new Column();
  Column rabatiRABNARAB = new Column();
  Column rabatiNRAB = new Column();

  public static rabati getDataModule() {
    if (rabaticlass == null) {
      rabaticlass = new rabati();
    }
    return rabaticlass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return rabati;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return rabatiaktiv;
  }

  public rabati() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    rabatiRABNARAB.setCaption("PnP");
    rabatiRABNARAB.setColumnName("RABNARAB");
    rabatiRABNARAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    rabatiRABNARAB.setPrecision(1);
    rabatiRABNARAB.setTableName("RABATI");
    rabatiRABNARAB.setSqlType(1);
    rabatiRABNARAB.setServerColumnName("RABNARAB");
    rabatiNRAB.setCaption("Naziv");
    rabatiNRAB.setColumnName("NRAB");
    rabatiNRAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    rabatiNRAB.setPrecision(50);
    rabatiNRAB.setTableName("RABATI");
    rabatiNRAB.setWidth(30);
    rabatiNRAB.setSqlType(1);
    rabatiNRAB.setServerColumnName("NRAB");
    rabatiCRAB.setCaption("Šifra");
    rabatiCRAB.setColumnName("CRAB");
    rabatiCRAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    rabatiCRAB.setPrecision(2);
    rabatiCRAB.setRowId(true);
    rabatiCRAB.setTableName("RABATI");
    rabatiCRAB.setWidth(5);
    rabatiCRAB.setSqlType(1);
    rabatiCRAB.setServerColumnName("CRAB");
    rabatiIRAB.setCaption("Iznos popusta");
    rabatiIRAB.setColumnName("IRAB");
    rabatiIRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rabatiIRAB.setDisplayMask("###,###,##0.00");
    rabatiIRAB.setDefault("0");
    rabatiIRAB.setPrecision(15);
    rabatiIRAB.setScale(2);
    rabatiIRAB.setTableName("RABATI");
    rabatiIRAB.setWidth(5);
    rabatiIRAB.setSqlType(2);
    rabatiIRAB.setServerColumnName("IRAB");
    rabatiIRAB.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    rabatiPRAB.setCaption("Posto popusta");
    rabatiPRAB.setColumnName("PRAB");
    rabatiPRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rabatiPRAB.setDisplayMask("#,##0.00");
    rabatiPRAB.setDefault("0");
    rabatiPRAB.setPrecision(10);
    rabatiPRAB.setScale(2);
    rabatiPRAB.setTableName("RABATI");
    rabatiPRAB.setWidth(5);
    rabatiPRAB.setSqlType(2);
    rabatiPRAB.setServerColumnName("PRAB");
    rabatiAKTIV.setColumnName("AKTIV");
    rabatiAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    rabatiAKTIV.setDefault("D");
    rabatiAKTIV.setPrecision(1);
    rabatiAKTIV.setTableName("RABATI");
    rabatiAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    rabatiAKTIV.setSqlType(1);
    rabatiAKTIV.setServerColumnName("AKTIV");
    rabatiLOKK.setColumnName("LOKK");
    rabatiLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rabatiLOKK.setDefault("N");
    rabatiLOKK.setPrecision(1);
    rabatiLOKK.setTableName("RABATI");
    rabatiLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    rabatiLOKK.setSqlType(1);
    rabatiLOKK.setServerColumnName("LOKK");
    rabati.setResolver(dm.getQresolver());
    rabati.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from rabati", null, true, Load.ALL));
 setColumns(new Column[] {rabatiLOKK, rabatiAKTIV, rabatiCRAB,rabatiNRAB, rabatiPRAB, rabatiIRAB, rabatiRABNARAB, });

    createFilteredDataSet(rabatiaktiv, "aktiv = 'D'");
  }

 public void setall(){

    /*SqlDefTabela = "create table rabati " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "crab char(2) not null, "+
      "nrab char(50) character set win1250,"+
      "prab numeric(6,2),"+
      "irab numeric(17,2),"+
      "rabnarab char(1)," + // Rabat na rabat
      "Primary Key (crab))" ;*/

    ddl.create("rabati")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("crab", 2, true)
	   .addChar("nrab", 50)
       .addFloat("prab", 6, 2)
       .addFloat("irab", 17, 2)
       .addChar("rabnarab", 1)       
       .addPrimaryKey("crab");

    Naziv="rabati";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

   /* DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+"irabatikey on rabati (crab)" };

    NaziviIdx=new String[]{"irabatikey" };
*/
  }
}