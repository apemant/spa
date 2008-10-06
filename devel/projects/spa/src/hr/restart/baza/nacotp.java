/****license*****************************************************************
**   file: nacotp.java
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

public class nacotp extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static nacotp nacotpclass;
  dM dm  = dM.getDataModule();
  QueryDataSet nacotp = new raDataSet();
  QueryDataSet nacotpaktiv = new raDataSet();
  Column nacotpLOKK = new Column();
  Column nacotpAKTIV = new Column();
  Column nacotpCNAC = new Column();
  Column nacotpNAZNAC = new Column();

  public static nacotp getDataModule() {
    if (nacotpclass == null) {
      nacotpclass = new nacotp();
    }
    return nacotpclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return nacotp;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return nacotpaktiv;
  }

  public nacotp() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    nacotpNAZNAC.setCaption(dmRes.getString("nacotpNAZNAC_caption"));
    nacotpNAZNAC.setColumnName("NAZNAC");
    nacotpNAZNAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacotpNAZNAC.setPrecision(30);
    nacotpNAZNAC.setTableName("NACOTP");
    nacotpNAZNAC.setWidth(30);
    nacotpNAZNAC.setSqlType(1);
    nacotpNAZNAC.setServerColumnName("NAZNAC");
    nacotpCNAC.setCaption(dmRes.getString("nacotpCNAC_caption"));
    nacotpCNAC.setColumnName("CNAC");
    nacotpCNAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacotpCNAC.setPrecision(3);
    nacotpCNAC.setRowId(true);
    nacotpCNAC.setTableName("NACOTP");
    nacotpCNAC.setWidth(5);
    nacotpCNAC.setSqlType(1);
    nacotpCNAC.setServerColumnName("CNAC");
    nacotpAKTIV.setCaption(dmRes.getString("nacotpAKTIV_caption"));
    nacotpAKTIV.setColumnName("AKTIV");
    nacotpAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacotpAKTIV.setDefault("D");
    nacotpAKTIV.setPrecision(1);
    nacotpAKTIV.setTableName("NACOTP");
    nacotpAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    nacotpAKTIV.setSqlType(1);
    nacotpAKTIV.setServerColumnName("AKTIV");
    nacotpLOKK.setCaption(dmRes.getString("nacotpLOKK_caption"));
    nacotpLOKK.setColumnName("LOKK");
    nacotpLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacotpLOKK.setDefault("N");
    nacotpLOKK.setPrecision(1);
    nacotpLOKK.setTableName("NACOTP");
    nacotpLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    nacotpLOKK.setSqlType(1);
    nacotpLOKK.setServerColumnName("LOKK");
    nacotp.setResolver(dm.getQresolver());
    nacotp.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from nacotp", null, true, Load.ALL));
 setColumns(new Column[] {nacotpLOKK, nacotpAKTIV, nacotpCNAC, nacotpNAZNAC});

    createFilteredDataSet(nacotpaktiv, "aktiv = 'D'");
  }

   public void setall(){

    /*SqlDefTabela = "create table nacotp " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cnac char(3) character set win1250 not null,"+
      "naznac char(30) character set win1250, " +
      "Primary Key (cnac))" ; */

    ddl.create("nacotp")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cnac", 3, true)
       .addChar("naznac", 30)
       .addPrimaryKey("cnac");

    Naziv="nacotp";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

/*
    NaziviIdx=new String[]{"inacotpkey" };

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on nacotp (cnac)"};
*/
  }
}