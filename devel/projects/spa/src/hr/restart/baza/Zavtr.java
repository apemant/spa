/****license*****************************************************************
**   file: Zavtr.java
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


public class Zavtr  extends KreirDrop implements DataModule {

  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Zavtr Zavtrclass;
  dM dm  = dM.getDataModule();
  QueryDataSet zavtr = new raDataSet();
  QueryDataSet zavtraktiv = new raDataSet();

  Column zavtrLOKK = new Column();
  Column zavtrAKTIV = new Column();
  Column zavtrCZT = new Column();
  Column zavtrPZT = new Column();
  Column zavtrIZT = new Column();
  Column zavtrCPAR = new Column();
  Column zavtrZTNAZT = new Column();
  Column zavtrBROJKONTA = new Column();
  Column zavtrBROJKONTA2 = new Column();
  Column zavtrNZT = new Column();

  public static Zavtr getDataModule() {
    if (Zavtrclass == null) {
      Zavtrclass = new Zavtr();
    }
    return Zavtrclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return zavtr;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return zavtraktiv;
  }

  public Zavtr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    zavtrBROJKONTA2.setCaption("Konto za pretporez");
    zavtrBROJKONTA2.setColumnName("BROJKONTA2");
    zavtrBROJKONTA2.setDataType(com.borland.dx.dataset.Variant.STRING);
    zavtrBROJKONTA2.setPrecision(8);
    zavtrBROJKONTA2.setTableName("ZAVTR");
    zavtrBROJKONTA2.setSqlType(1);
    zavtrBROJKONTA2.setServerColumnName("BROJKONTA2");

    zavtrBROJKONTA.setCaption("Konto partnera");
    zavtrBROJKONTA.setColumnName("BROJKONTA");
    zavtrBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    zavtrBROJKONTA.setPrecision(8);
    zavtrBROJKONTA.setTableName("ZAVTR");
    zavtrBROJKONTA.setSqlType(1);
    zavtrBROJKONTA.setServerColumnName("BROJKONTA");
    zavtrZTNAZT.setCaption("ZT na ZT");
    zavtrZTNAZT.setColumnName("ZTNAZT");
    zavtrZTNAZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    zavtrZTNAZT.setDefault("N");
    zavtrZTNAZT.setPrecision(1);
    zavtrZTNAZT.setTableName("ZAVTR");
    zavtrZTNAZT.setSqlType(1);
    zavtrZTNAZT.setServerColumnName("ZTNAZT");
    zavtrCPAR.setCaption(dmRes.getString("zavtrCPAR_caption"));
    zavtrCPAR.setColumnName("CPAR");
    zavtrCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    zavtrCPAR.setTableName("ZAVTR");
    zavtrCPAR.setSqlType(4);
    zavtrCPAR.setServerColumnName("CPAR");
    zavtrIZT.setCaption(dmRes.getString("zavtrIZT_caption"));
    zavtrIZT.setColumnName("IZT");
    zavtrIZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    zavtrIZT.setDisplayMask("###,###,##0.00");
    zavtrIZT.setDefault("0");
    zavtrIZT.setPrecision(15);
    zavtrIZT.setScale(2);
    zavtrIZT.setTableName("ZAVTR");
    zavtrIZT.setSqlType(2);
    zavtrIZT.setServerColumnName("IZT");
    zavtrPZT.setCaption(dmRes.getString("zavtrPZT_caption"));
    zavtrPZT.setColumnName("PZT");
    zavtrPZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    zavtrPZT.setDisplayMask("#,##0.00");
    zavtrPZT.setDefault("0");
    zavtrPZT.setPrecision(10);
    zavtrPZT.setScale(2);
    zavtrPZT.setTableName("ZAVTR");
    zavtrPZT.setSqlType(2);
    zavtrPZT.setServerColumnName("PZT");
    zavtrNZT.setCaption("Naziv");
    zavtrNZT.setColumnName("NZT");
    zavtrNZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    zavtrNZT.setPrecision(50);
    zavtrNZT.setTableName("ZAVTR");
    zavtrNZT.setWidth(30);
    zavtrNZT.setServerColumnName("NZT");
    zavtrNZT.setSqlType(1);
    zavtrCZT.setCaption("\u0160ifra");
    zavtrCZT.setColumnName("CZT");
    zavtrCZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    zavtrCZT.setPrecision(5);
    zavtrCZT.setRowId(true);
    zavtrCZT.setTableName("ZAVTR");
    zavtrCZT.setWidth(5);
    zavtrCZT.setServerColumnName("CZT");
    zavtrCZT.setSqlType(1);
    zavtrAKTIV.setColumnName("AKTIV");
    zavtrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    zavtrAKTIV.setDefault("D");
    zavtrAKTIV.setPrecision(1);
    zavtrAKTIV.setTableName("ZAVTR");
    zavtrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    zavtrAKTIV.setServerColumnName("AKTIV");
    zavtrAKTIV.setSqlType(1);
    zavtrLOKK.setColumnName("LOKK");
    zavtrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    zavtrLOKK.setDefault("N");
    zavtrLOKK.setPrecision(1);
    zavtrLOKK.setTableName("ZAVTR");
    zavtrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    zavtrLOKK.setServerColumnName("LOKK");
    zavtrLOKK.setSqlType(1);
    zavtr.setResolver(dm.getQresolver());
    zavtr.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * FROM ZAVTR", null, true, Load.ALL));
 setColumns(new Column[] {zavtrLOKK, zavtrAKTIV, zavtrCZT, zavtrPZT, zavtrIZT, zavtrCPAR, zavtrZTNAZT, zavtrBROJKONTA, zavtrBROJKONTA2, zavtrNZT});

    createFilteredDataSet(zavtraktiv, "aktiv = 'D'");
  }

  public void setall(){

    /*SqlDefTabela = "create table Zavtr " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + //Status zauzetosti
      "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
      "czt char(2) CHARACTER SET WIN1250 not null,"+ //Šifra zavisnog troška
      "nzt char(50) CHARACTER SET WIN1250 , " + //Naziv zavisnog troška
      "pzt numeric(6,2) ," + // Posto zavisni troškovi
      "izt numeric(17,2) , " + // Iznos zavisni troškovi
      "cpar numeric(6,0),"+
      "ztnazt char(1) CHARACTER SET WIN1250 default 'N', " +
      "brojkonta CHAR(8) CHARACTER SET WIN1250 , " + //Status zauzetosti
      "Primary Key (czt))"; */

    ddl.create("zavtr")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("czt", 5, true)
       .addFloat("pzt", 6, 2)
       .addFloat("izt", 17, 2)
       .addInteger("cpar", 6)
       .addChar("ztnazt", 1, "N")
       .addChar("brojkonta", 8)
       .addChar("brojkonta2", 8)
       .addChar("nzt", 50)
       .addPrimaryKey("czt");

    Naziv="Zavtr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    NaziviIdx=new String[]{"ilokkZavtr","iaktivZavtr","iczt"};


    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Zavtr (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Zavtr (aktiv)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[0] +" on Zavtr (czt)"};
*/
  }
}

