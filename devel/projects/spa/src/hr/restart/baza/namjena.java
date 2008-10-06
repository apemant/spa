/****license*****************************************************************
**   file: namjena.java
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


public class namjena extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static namjena namjenaclass;
  dM dm  = dM.getDataModule();
  QueryDataSet namjena = new raDataSet();
  QueryDataSet namjenaaktiv = new raDataSet();
  Column namjenaLOKK = new Column();
  Column namjenaAKTIV = new Column();
  Column namjenaCNAMJ = new Column();
  Column namjenaNAZNAMJ = new Column();
  Column namjenaPOREZ = new Column();
  public static namjena getDataModule() {
    if (namjenaclass == null) {
      namjenaclass = new namjena();
    }
    return namjenaclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return namjena;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return namjenaaktiv;
  }

  public namjena() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    namjenaCNAMJ.setCaption(dmRes.getString("namjenaCNAMJ_caption"));
    namjenaCNAMJ.setColumnName("CNAMJ");
    namjenaCNAMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    namjenaCNAMJ.setPrecision(3);
    namjenaCNAMJ.setRowId(true);
    namjenaCNAMJ.setTableName("NAMJENA");
    namjenaCNAMJ.setWidth(5);
    namjenaCNAMJ.setSqlType(1);
    namjenaCNAMJ.setServerColumnName("CNAMJ");
    namjenaAKTIV.setColumnName("AKTIV");
    namjenaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    namjenaAKTIV.setDefault("D");
    namjenaAKTIV.setPrecision(1);
    namjenaAKTIV.setTableName("NAMJENA");
    namjenaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    namjenaAKTIV.setSqlType(1);
    namjenaAKTIV.setServerColumnName("AKTIV");
    namjenaLOKK.setColumnName("LOKK");
    namjenaLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    namjenaLOKK.setDefault("N");
    namjenaLOKK.setPrecision(1);
    namjenaLOKK.setTableName("NAMJENA");
    namjenaLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    namjenaLOKK.setSqlType(1);
    namjenaLOKK.setServerColumnName("LOKK");

    namjenaNAZNAMJ.setCaption(dmRes.getString("namjenaNAZNAMJ_caption"));
    namjenaNAZNAMJ.setColumnName("NAZNAMJ");
    namjenaNAZNAMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    namjenaNAZNAMJ.setPrecision(30);
    namjenaNAZNAMJ.setTableName("NAMJENA");
    namjenaNAZNAMJ.setWidth(30);
    namjenaNAZNAMJ.setSqlType(1);
    namjenaNAZNAMJ.setServerColumnName("NAZNAMJ");

    namjenaPOREZ.setColumnName("POREZ");
    namjenaPOREZ.setCaption("Porez");
    namjenaPOREZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    namjenaPOREZ.setDefault("D");
    namjenaPOREZ.setPrecision(1);
    namjenaPOREZ.setTableName("NAMJENA");
    namjenaPOREZ.setSqlType(1);
    namjenaPOREZ.setServerColumnName("POREZ");

    namjena.setResolver(dm.getQresolver());
    namjena.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from namjena", null, true, Load.ALL));
 setColumns(new Column[] {namjenaLOKK, namjenaAKTIV, namjenaCNAMJ, namjenaNAZNAMJ, namjenaPOREZ});

    createFilteredDataSet(namjenaaktiv, "aktiv = 'D'");
  }

  public void setall(){

    /*SqlDefTabela = "create table namjena " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cnamj char(3) character set win1250 not null,"+
      "naznamj char(30) character set win1250, " +
      "Primary Key (cnamj))" ;*/

    ddl.create("namjena")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cnamj", 3, true)
       .addChar("naznamj", 30)
       .addChar("porez", 1, "D")
       .addPrimaryKey("cnamj");

    Naziv="namjena";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

/*
    NaziviIdx=new String[]{"inamjenakey" };

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on namjena (cnamj)"};
*/
  }
}