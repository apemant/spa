/****license*****************************************************************
**   file: napomene.java
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

public class napomene extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static napomene napomeneclass;
  dM dm  = dM.getDataModule();
  QueryDataSet napomene = new raDataSet();
  QueryDataSet napomeneaktiv = new raDataSet();
  Column napomeneLOKK = new Column();
  Column napomeneAKTIV = new Column();
  Column napomeneCNAP = new Column();
  Column napomeneNAZNAP = new Column();

  public static napomene getDataModule() {
    if (napomeneclass == null) {
      napomeneclass = new napomene();
    }
    return napomeneclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return napomene;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return napomeneaktiv;
  }

  public napomene() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    napomeneNAZNAP.setCaption("Naziv");
    napomeneNAZNAP.setColumnName("NAZNAP");
    napomeneNAZNAP.setDataType(com.borland.dx.dataset.Variant.STRING);
    napomeneNAZNAP.setPrecision(200);
    napomeneNAZNAP.setTableName("NAPOMENE");
    napomeneNAZNAP.setWidth(30);
    napomeneNAZNAP.setSqlType(1);
    napomeneNAZNAP.setServerColumnName("NAZNAP");
    napomeneCNAP.setCaption("Šifra");
    napomeneCNAP.setColumnName("CNAP");
    napomeneCNAP.setDataType(com.borland.dx.dataset.Variant.STRING);
    napomeneCNAP.setPrecision(3);
    napomeneCNAP.setRowId(true);
    napomeneCNAP.setTableName("NAPOMENE");
    napomeneCNAP.setWidth(5);
    napomeneCNAP.setSqlType(1);
    napomeneCNAP.setServerColumnName("CNAP");
    napomeneAKTIV.setColumnName("AKTIV");
    napomeneAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    napomeneAKTIV.setDefault("D");
    napomeneAKTIV.setPrecision(1);
    napomeneAKTIV.setTableName("NAPOMENE");
    napomeneAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    napomeneAKTIV.setSqlType(1);
    napomeneAKTIV.setServerColumnName("AKTIV");
    napomeneLOKK.setColumnName("LOKK");
    napomeneLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    napomeneLOKK.setDefault("N");
    napomeneLOKK.setPrecision(1);
    napomeneLOKK.setTableName("NAPOMENE");
    napomeneLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    napomeneLOKK.setSqlType(1);
    napomeneLOKK.setServerColumnName("LOKK");
    napomene.setResolver(dm.getQresolver());
    napomene.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from napomene", null, true, Load.ALL));
 setColumns(new Column[] {napomeneLOKK, napomeneAKTIV, napomeneCNAP, napomeneNAZNAP});

    createFilteredDataSet(napomeneaktiv, "aktiv = 'D'");
  }

  public void setall(){

   /* SqlDefTabela = "create table napomene " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cnap char(3) character set win1250 not null, "+
      "naznap char(200) character set win1250,"+
      "Primary Key (cnap))" ;*/

    ddl.create("napomene")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cnap", 3, true)
       .addChar("naznap", 200)
       .addPrimaryKey("cnap");

    Naziv="napomene";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

/*
    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+"inapomenekey on napomene (cnap)" };

    NaziviIdx=new String[]{"inapomenekey" };
*/
  }
}