/****license*****************************************************************
**   file: franka.java
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

public class franka extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static franka frankaclass;
  dM dm  = dM.getDataModule();
  QueryDataSet franka = new raDataSet();
  QueryDataSet frankaaktiv = new raDataSet();

  Column frankaLOKK = new Column();
  Column frankaAKTIV = new Column();
  Column frankaCFRA = new Column();
  Column frankaNAZFRA = new Column();

  public static franka getDataModule() {
    if (frankaclass == null) {
      frankaclass = new franka();
    }
    return frankaclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return franka;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return frankaaktiv;
  }

  public franka() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    frankaNAZFRA.setCaption(dmRes.getString("frankaNAZFRA_caption"));
    frankaNAZFRA.setColumnName("NAZFRA");
    frankaNAZFRA.setDataType(com.borland.dx.dataset.Variant.STRING);
    frankaNAZFRA.setPrecision(30);
    frankaNAZFRA.setTableName("FRANKA");
    frankaNAZFRA.setWidth(30);
    frankaNAZFRA.setSqlType(1);
    frankaNAZFRA.setServerColumnName("NAZFRA");
    frankaCFRA.setCaption(dmRes.getString("frankaCFRA_caption"));
    frankaCFRA.setColumnName("CFRA");
    frankaCFRA.setDataType(com.borland.dx.dataset.Variant.STRING);
    frankaCFRA.setPrecision(3);
    frankaCFRA.setRowId(true);
    frankaCFRA.setTableName("FRANKA");
    frankaCFRA.setWidth(5);
    frankaCFRA.setSqlType(1);
    frankaCFRA.setServerColumnName("CFRA");
    frankaAKTIV.setCaption(dmRes.getString("frankaAKTIV_caption"));
    frankaAKTIV.setColumnName("AKTIV");
    frankaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    frankaAKTIV.setDefault("D");
    frankaAKTIV.setPrecision(1);
    frankaAKTIV.setTableName("FRANKA");
    frankaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    frankaAKTIV.setSqlType(1);
    frankaAKTIV.setServerColumnName("AKTIV");
    frankaLOKK.setCaption(dmRes.getString("frankaLOKK_caption"));
    frankaLOKK.setColumnName("LOKK");
    frankaLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    frankaLOKK.setDefault("N");
    frankaLOKK.setPrecision(1);
    frankaLOKK.setSchemaName("");
    frankaLOKK.setTableName("FRANKA");
    frankaLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    frankaLOKK.setSqlType(1);
    frankaLOKK.setServerColumnName("LOKK");
    franka.setResolver(dm.getQresolver());
    franka.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from franka", null, true, Load.ALL));
 setColumns(new Column[] {frankaLOKK, frankaAKTIV, frankaCFRA, frankaNAZFRA});

    createFilteredDataSet(frankaaktiv, "aktiv = 'D'");
  }

   public void setall(){

  /*  SqlDefTabela = "create table franka " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cfra char(3) character set win1250 not null,"+
      "nazfra char(30) character set win1250, " +
      "Primary Key (cfra))" ;
*/
    ddl.create("franka")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cfra", 3, true)
       .addChar("nazfra", 30)
       .addPrimaryKey("cfra");

    Naziv="franka";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

  /*
    NaziviIdx=new String[]{"ifrankakey" };

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on franka (cfra)"};
*/
  }
}