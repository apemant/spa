/****license*****************************************************************
**   file: jedmj.java
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

public class jedmj extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static jedmj jedmjclass;
  dM dm  = dM.getDataModule();
  QueryDataSet jedmj = new raDataSet();
  QueryDataSet jedmjaktiv = new raDataSet();
  Column jedmjLOKK = new Column();
  Column jedmjAKTIV = new Column();
  Column jedmjJM = new Column();
  Column jedmjNAZJM = new Column();
  Column jedmjZNACDEC = new Column();

  public static jedmj getDataModule() {
    if (jedmjclass == null) {
      jedmjclass = new jedmj();
    }
    return jedmjclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return jedmj;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return jedmjaktiv;
  }

  public jedmj() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {

      jedmjZNACDEC.setCaption("Znaèajne decimale");
      jedmjZNACDEC.setColumnName("ZNACDEC");
      jedmjZNACDEC.setDataType(com.borland.dx.dataset.Variant.INT);
      jedmjZNACDEC.setTableName("JEDMJ");
      jedmjZNACDEC.setWidth(7);
      jedmjZNACDEC.setSqlType(4);
      jedmjZNACDEC.setServerColumnName("ZNACDEC");

      
      
    jedmjNAZJM.setCaption(dmRes.getString("jedmjNAZJM_caption"));
    jedmjNAZJM.setColumnName("NAZJM");
    jedmjNAZJM.setDataType(com.borland.dx.dataset.Variant.STRING);
    jedmjNAZJM.setPrecision(20);
    jedmjNAZJM.setTableName("JEDMJ");
    jedmjNAZJM.setWidth(30);
    jedmjNAZJM.setSqlType(1);
    jedmjNAZJM.setServerColumnName("NAZJM");
    jedmjJM.setCaption(dmRes.getString("jedmjJM_caption"));
    jedmjJM.setColumnName("JM");
    jedmjJM.setDataType(com.borland.dx.dataset.Variant.STRING);
    jedmjJM.setPrecision(3);
    jedmjJM.setRowId(true);
    jedmjJM.setTableName("JEDMJ");
    jedmjJM.setWidth(5);
    jedmjJM.setSqlType(1);
    jedmjJM.setServerColumnName("JM");
    jedmjAKTIV.setCaption(dmRes.getString("jedmjAKTIV_caption"));
    jedmjAKTIV.setColumnName("AKTIV");
    jedmjAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    jedmjAKTIV.setDefault("D");
    jedmjAKTIV.setPrecision(1);
    jedmjAKTIV.setTableName("JEDMJ");
    jedmjAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    jedmjAKTIV.setSqlType(1);
    jedmjAKTIV.setServerColumnName("AKTIV");
    jedmjLOKK.setCaption(dmRes.getString("jedmjLOKK_caption"));
    jedmjLOKK.setColumnName("LOKK");
    jedmjLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    jedmjLOKK.setDefault("N");
    jedmjLOKK.setPrecision(1);
    jedmjLOKK.setTableName("JEDMJ");
    jedmjLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    jedmjLOKK.setSqlType(1);
    jedmjLOKK.setServerColumnName("LOKK");
    jedmj.setResolver(dm.getQresolver());
    jedmj.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from jedmj", null, true, Load.ALL));
 setColumns(new Column[] {jedmjLOKK, jedmjAKTIV, jedmjJM, jedmjNAZJM,jedmjZNACDEC});

    createFilteredDataSet(jedmjaktiv, "aktiv = 'D'");
  }

  public void setall(){

    /*SqlDefTabela = "create table jedmj " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "jm char(3) character set win1250 not null, "+
      "nazjm char(20) character set win1250 ,"+
      "Primary Key (jm))" ;
*/
    ddl.create("jedmj")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("jm", 3, true)
       .addChar("nazjm", 20)
       .addInteger("znacdec", 7)
       .addPrimaryKey("jm");

    Naziv="jedmj";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);


  /*  NaziviIdx=new String[]{"ijmkey" };

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on jedmj (jm)"};
*/
  }
}