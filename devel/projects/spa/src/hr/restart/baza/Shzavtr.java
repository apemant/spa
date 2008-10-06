/****license*****************************************************************
**   file: Shzavtr.java
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

public class Shzavtr extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Shzavtr Shzavtrclass;
  dM dm  = dM.getDataModule();

  Column shzavtrLOKK = new Column();
  Column shzavtrAKTIV = new Column();
  Column shzavtrCSHZT = new Column();
  Column shzavtrNSHZT = new Column();
  Column shzavtrCZT1 = new Column();
  Column shzavtrCZT2 = new Column();
  Column shzavtrCZT3 = new Column();
  Column shzavtrCZT4 = new Column();
  Column shzavtrCZT5 = new Column();
  Column shzavtrZTPuk = new Column();
  Column shzavtrZTIuk = new Column();

  QueryDataSet shzavtr = new raDataSet();

  public static Shzavtr getDataModule() {
    if (Shzavtrclass == null) {
      Shzavtrclass = new Shzavtr();
    }
    return Shzavtrclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return shzavtr;
  }
  public Shzavtr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    shzavtrNSHZT.setCaption(dmRes.getString("shzavtrNSHZT_caption"));
    shzavtrNSHZT.setColumnName("NSHZT");
    shzavtrNSHZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    shzavtrNSHZT.setPrecision(20);
    shzavtrNSHZT.setTableName("SHZAVTR");
    shzavtrNSHZT.setWidth(30);
    shzavtrNSHZT.setSqlType(1);
    shzavtrNSHZT.setServerColumnName("NSHZT");
    shzavtrCSHZT.setCaption(dmRes.getString("shzavtrCSHZT_caption"));
    shzavtrCSHZT.setColumnName("CSHZT");
    shzavtrCSHZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    shzavtrCSHZT.setPrecision(3);
    shzavtrCSHZT.setRowId(true);
    shzavtrCSHZT.setTableName("SHZAVTR");
    shzavtrCSHZT.setWidth(5);
    shzavtrCSHZT.setSqlType(1);
    shzavtrCSHZT.setServerColumnName("CSHZT");
    shzavtrAKTIV.setCaption(dmRes.getString("shzavtrAKTIV_caption"));
    shzavtrAKTIV.setColumnName("AKTIV");
    shzavtrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    shzavtrAKTIV.setDefault("D");
    shzavtrAKTIV.setPrecision(1);
    shzavtrAKTIV.setTableName("SHZAVTR");
    shzavtrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    shzavtrAKTIV.setSqlType(1);
    shzavtrAKTIV.setServerColumnName("AKTIV");
    shzavtrLOKK.setCaption(dmRes.getString("shzavtrLOKK_caption"));
    shzavtrLOKK.setColumnName("LOKK");
    shzavtrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    shzavtrLOKK.setDefault("N");
    shzavtrLOKK.setPrecision(1);
    shzavtrLOKK.setTableName("SHZAVTR");
    shzavtrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    shzavtrLOKK.setSqlType(1);
    shzavtrLOKK.setServerColumnName("LOKK");
    shzavtrZTIuk.setCaption("Iznos zavisnih troškova");
    shzavtrZTIuk.setColumnName("ZTIuk");
    shzavtrZTIuk.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    shzavtrZTIuk.setDisplayMask("###,###,##0.00");
    shzavtrZTIuk.setDefault("0");
    shzavtrZTIuk.setPrecision(15);
    shzavtrZTIuk.setScale(2);
    shzavtrZTIuk.setTableName("SHZAVTR");
    shzavtrZTIuk.setWidth(10);
    shzavtrZTIuk.setSqlType(2);
    shzavtrZTIuk.setServerColumnName("ZTIuk");
    shzavtrZTPuk.setCaption("Posto zavisnih troškova");
    shzavtrZTPuk.setColumnName("ZTPuk");
    shzavtrZTPuk.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    shzavtrZTPuk.setDisplayMask("#,##0.00");
    shzavtrZTPuk.setDefault("0");
    shzavtrZTPuk.setPrecision(10);
    shzavtrZTPuk.setScale(2);
    shzavtrZTPuk.setTableName("SHZAVTR");
    shzavtrZTPuk.setSqlType(2);
    shzavtrZTPuk.setServerColumnName("ZTPuk");
    shzavtrCZT5.setCaption("Šifra zavisnog troška 5");
    shzavtrCZT5.setColumnName("CZT5");
    shzavtrCZT5.setDataType(com.borland.dx.dataset.Variant.STRING);
    shzavtrCZT5.setPrecision(2);
    shzavtrCZT5.setTableName("SHZAVTR");
    shzavtrCZT5.setSqlType(1);
    shzavtrCZT5.setServerColumnName("CZT5");
    shzavtrCZT4.setCaption("Šifra zavisnog troška 4");
    shzavtrCZT4.setColumnName("CZT4");
    shzavtrCZT4.setDataType(com.borland.dx.dataset.Variant.STRING);
    shzavtrCZT4.setPrecision(2);
    shzavtrCZT4.setTableName("SHZAVTR");
    shzavtrCZT4.setSqlType(1);
    shzavtrCZT4.setServerColumnName("CZT4");
    shzavtrCZT3.setCaption("Šifra zavisnog troška 3");
    shzavtrCZT3.setColumnName("CZT3");
    shzavtrCZT3.setDataType(com.borland.dx.dataset.Variant.STRING);
    shzavtrCZT3.setPrecision(2);
    shzavtrCZT3.setTableName("SHZAVTR");
    shzavtrCZT3.setSqlType(1);
    shzavtrCZT3.setServerColumnName("CZT3");
    shzavtrCZT2.setCaption("Šifra zavisnog troška 2");
    shzavtrCZT2.setColumnName("CZT2");
    shzavtrCZT2.setDataType(com.borland.dx.dataset.Variant.STRING);
    shzavtrCZT2.setPrecision(2);
    shzavtrCZT2.setTableName("SHZAVTR");
    shzavtrCZT2.setSqlType(1);
    shzavtrCZT2.setServerColumnName("CZT2");
    shzavtrCZT1.setCaption("Šifra zavisnog troška 1");
    shzavtrCZT1.setColumnName("CZT1");
    shzavtrCZT1.setDataType(com.borland.dx.dataset.Variant.STRING);
    shzavtrCZT1.setPrecision(2);
    shzavtrCZT1.setTableName("SHZAVTR");
    shzavtrCZT1.setSqlType(1);
    shzavtrCZT1.setServerColumnName("CZT1");
    shzavtr.setResolver(dm.getQresolver());
    shzavtr.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from shzavtr", null, true, Load.ALL));
 setColumns(new Column[] {shzavtrLOKK, shzavtrAKTIV, shzavtrCSHZT, shzavtrNSHZT, shzavtrCZT1, shzavtrCZT2, shzavtrCZT3, shzavtrCZT4,
        shzavtrCZT5, shzavtrZTPuk, shzavtrZTIuk});
  }

  public void setall(){

    /*SqlDefTabela = "create table shzavtr " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cshzt char(3) CHARACTER SET WIN1250 not null,"+
      "nshzt char(20) CHARACTER SET WIN1250 ,"+
      "czt1 char(2) CHARACTER SET WIN1250 ,"+ //Šifra zavisnog troška
      "czt2 char(2) CHARACTER SET WIN1250 ,"+ //Šifra zavisnog troška
      "czt3 char(2) CHARACTER SET WIN1250 ,"+ //Šifra zavisnog troška
      "czt4 char(2) CHARACTER SET WIN1250 ,"+ //Šifra zavisnog troška
      "czt5 char(2) CHARACTER SET WIN1250 ,"+ //Šifra zavisnog troška
      "Primary Key (cshzt))" ;*/

    ddl.create("shzavtr")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cshzt", 3, true)
       .addChar("nshzt", 20)
       .addChar("czt1", 2)
       .addChar("czt2", 2)
       .addChar("czt3", 2)
       .addChar("czt4", 2)
       .addChar("czt5", 2)
       .addFloat("ztpuk", 6, 2)
       .addFloat("ztiuk", 17, 2)
       .addPrimaryKey("cshzt");

    Naziv="Shzavtr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+"ishzavtrcshzt on shzavtr (cshzt)" };

    NaziviIdx=new String[]{"ishzavtrcshzt"};
*/
  }
}