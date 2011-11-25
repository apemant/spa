/****license*****************************************************************
**   file: nacpl.java
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

public class nacpl extends KreirDrop implements DataModule {
  //ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static nacpl nacplclass;
  // dM dm  = dM.getDataModule();
  QueryDataSet nacpl = new raDataSet();
  QueryDataSet nacplaktiv = new raDataSet();
  QueryDataSet nacplG = new raDataSet();
  QueryDataSet nacplB = new raDataSet();

  /*Column nacplLOKK = new Column();
  Column nacplAKTIV = new Column();
  Column nacplCNACPL = new Column();
  Column nacplNAZNACPL = new Column();
  Column nacplFL_CEK = new Column();
  Column nacplFL_KARTICA = new Column();
  Column nacplFL_GOT = new Column();
  Column nacplSALDAK = new Column();
  Column nacplPOP = new Column();
  Column nacplBROJKONTA = new Column();
  Column nacplVRDOK = new Column();*/
  
  public static nacpl getDataModule() {
    if (nacplclass == null) {
      nacplclass = new nacpl();
    }
    return nacplclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return nacpl;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return nacplaktiv;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getNacplG() {
    return nacplG;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getNacplB() {
    return nacplB;
  }

  public nacpl() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    /*nacplPOP.setCaption("Popust");
    nacplPOP.setColumnName("POPUSTI");
    nacplPOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    nacplPOP.setPrecision(10);
    nacplPOP.setScale(2);
    nacplPOP.setTableName("NACPL");
    nacplPOP.setDisplayMask("###,###,##0.00");
    nacplPOP.setDefault("0");
    nacplPOP.setWidth(8);
    nacplPOP.setSqlType(2);
    nacplPOP.setServerColumnName("POPUSTI");
    nacplFL_GOT.setCaption("Gotovina");
    nacplFL_GOT.setColumnName("FL_GOT");
    nacplFL_GOT.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacplFL_GOT.setPrecision(1);
    nacplFL_GOT.setTableName("NACPL");
    nacplFL_GOT.setSqlType(1);
    nacplFL_GOT.setServerColumnName("FL_GOT");
    nacplFL_KARTICA.setCaption("Kartica");
    nacplFL_KARTICA.setColumnName("FL_KARTICA");
    nacplFL_KARTICA.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacplFL_KARTICA.setPrecision(1);
    nacplFL_KARTICA.setTableName("NACPL");
    nacplFL_KARTICA.setSqlType(1);
    nacplFL_KARTICA.setServerColumnName("FL_KARTICA");
    nacplFL_CEK.setCaption("\u010Cek");
    nacplFL_CEK.setColumnName("FL_CEK");
    nacplFL_CEK.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacplFL_CEK.setPrecision(1);
    nacplFL_CEK.setTableName("NACPL");
    nacplFL_CEK.setSqlType(1);
    nacplFL_CEK.setServerColumnName("FL_CEK");

    nacplSALDAK.setCaption("Bezgotovinski");
    nacplSALDAK.setColumnName("SALDAK");
    nacplSALDAK.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacplSALDAK.setDefault("N");
    nacplSALDAK.setPrecision(1);
    nacplSALDAK.setTableName("NACPL");
    nacplSALDAK.setSqlType(1);
//    nacplSALDAK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    nacplSALDAK.setServerColumnName("SALDAK");

    nacplNAZNACPL.setCaption("Naziv");
    nacplNAZNACPL.setColumnName("NAZNACPL");
    nacplNAZNACPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacplNAZNACPL.setPrecision(30);
    nacplNAZNACPL.setTableName("NACPL");
    nacplNAZNACPL.setWidth(30);
    nacplNAZNACPL.setSqlType(1);
    nacplNAZNACPL.setServerColumnName("NAZNACPL");
    nacplCNACPL.setCaption("Šifra");
    nacplCNACPL.setColumnName("CNACPL");
    nacplCNACPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacplCNACPL.setPrecision(3);
    nacplCNACPL.setRowId(true);
    nacplCNACPL.setTableName("NACPL");
    nacplCNACPL.setWidth(5);
    nacplCNACPL.setSqlType(1);
    nacplCNACPL.setServerColumnName("CNACPL");
    nacplAKTIV.setCaption(dmRes.getString("nacplAKTIV_caption"));
    nacplAKTIV.setColumnName("AKTIV");
    nacplAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacplAKTIV.setDefault("D");
    nacplAKTIV.setPrecision(1);
    nacplAKTIV.setTableName("NACPL");
    nacplAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    nacplAKTIV.setSqlType(1);
    nacplAKTIV.setServerColumnName("AKTIV");
    nacplLOKK.setCaption(dmRes.getString("nacplLOKK_caption"));
    nacplLOKK.setColumnName("LOKK");
    nacplLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacplLOKK.setDefault("N");
    nacplLOKK.setPrecision(1);
    nacplLOKK.setTableName("NACPL");
    nacplLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    nacplLOKK.setSqlType(1);
    nacplLOKK.setServerColumnName("LOKK");
    nacplBROJKONTA.setCaption("Konto");
    nacplBROJKONTA.setColumnName("BROJKONTA");
    nacplBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacplBROJKONTA.setDefault("");
    nacplBROJKONTA.setPrecision(8);
    nacplBROJKONTA.setTableName("NACPL");
    nacplBROJKONTA.setServerColumnName("BROJKONTA");
    nacplBROJKONTA.setSqlType(1);
    nacplBROJKONTA.setWidth(8);
    nacplVRDOK.setCaption("Vrsta dokumenta");
    nacplVRDOK.setColumnName("VRDOK");
    nacplVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    nacplVRDOK.setPrecision(3);
    nacplVRDOK.setTableName("NACPL");
    nacplVRDOK.setServerColumnName("VRDOK");
    nacplVRDOK.setSqlType(1);

    
    nacpl.setResolver(dm.getQresolver());
    nacpl.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from nacpl", null, true, Load.ALL));
 setColumns(new Column[] {nacplLOKK, nacplAKTIV, nacplCNACPL, nacplNAZNACPL, nacplFL_CEK, nacplFL_KARTICA, nacplFL_GOT, nacplSALDAK, nacplPOP, nacplBROJKONTA, nacplVRDOK});
*/
  	initModule();
    createFilteredDataSet(nacplaktiv, "aktiv='D'");
    createFilteredDataSet(nacplG, "aktiv='D' AND saldak='N'");
    createFilteredDataSet(nacplB, "aktiv='D' AND saldak='D'");
  }

  //public void setall(){

    /*SqlDefTabela = "create table nacpl " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cnacpl char(3) character set win1250 not null,"+
      "naznacpl char(30) character set win1250, " +
      "fl_cek char(1) character set win1250,"+
      "fl_kartica char(1) character set win1250,"+
      "popusti numeric(6,2)," +
      "Primary Key (cnacpl))" ; */

/*    ddl.create("nacpl")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cnacpl", 3, true)
       .addChar("naznacpl", 30)
       .addChar("fl_cek", 1)
       .addChar("fl_kartica", 1)
       .addChar("fl_got", 1)
       .addChar("saldak", 1, "N")
       .addFloat("popusti", 6, 2)
       .addChar("brojkonta", 8)
       .addChar("vrdok", 3)
       .addPrimaryKey("cnacpl");

    Naziv="nacpl";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
*/
/*
    NaziviIdx=new String[]{"inacplkey" };

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on nacpl (cnacpl)"};
*/
//  }
}