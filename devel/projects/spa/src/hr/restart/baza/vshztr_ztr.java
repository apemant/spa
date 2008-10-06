/****license*****************************************************************
**   file: vshztr_ztr.java
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

public class vshztr_ztr extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static vshztr_ztr vshztr_ztrclass;
  dM dm  = dM.getDataModule();
  QueryDataSet vshztr_ztr = new QueryDataSet();
  Column vshztr_ztrLOKK = new Column();
  Column vshztr_ztrAKTIV = new Column();
  Column vshztr_ztrCSHZT = new Column();
  Column vshztr_ztrRBR = new Column();
  Column vshztr_ztrCZT = new Column();
  Column vshztr_ztrPZT = new Column();
  Column vshztr_ztrIZT = new Column();
  Column vshztr_ztrZTNAZT = new Column();

  public static vshztr_ztr getDataModule() {
    if (vshztr_ztrclass == null) {
      vshztr_ztrclass = new vshztr_ztr();
    }
    return vshztr_ztrclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return vshztr_ztr;
  }

  public vshztr_ztr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    vshztr_ztrIZT.setCaption("Iznos zavisnog troška");
    vshztr_ztrIZT.setColumnName("IZT");
    vshztr_ztrIZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vshztr_ztrIZT.setDisplayMask("###,###,##0.00");
    vshztr_ztrIZT.setDefault("0");
    vshztr_ztrIZT.setPrecision(15);
    vshztr_ztrIZT.setScale(2);
    vshztr_ztrIZT.setTableName("VSHZTR_ZTR");
    vshztr_ztrIZT.setWidth(10);
    vshztr_ztrIZT.setServerColumnName("IZT");
    vshztr_ztrIZT.setSqlType(2);
    vshztr_ztrRBR.setCaption("Rbr");
    vshztr_ztrRBR.setColumnName("RBR");
    vshztr_ztrRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vshztr_ztrRBR.setPrecision(4);
    vshztr_ztrRBR.setRowId(true);
    vshztr_ztrRBR.setTableName("VSHZTR_ZTR");
    vshztr_ztrRBR.setWidth(4);
    vshztr_ztrRBR.setServerColumnName("RBR");
    vshztr_ztrRBR.setSqlType(5);
    vshztr_ztrCSHZT.setCaption("Šifra sheme");
    vshztr_ztrCSHZT.setColumnName("CSHZT");
    vshztr_ztrCSHZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    vshztr_ztrCSHZT.setPrecision(3);
    vshztr_ztrCSHZT.setRowId(true);
    vshztr_ztrCSHZT.setTableName("VSHZTR_ZTR");
    vshztr_ztrCSHZT.setServerColumnName("CSHZT");
    vshztr_ztrCSHZT.setSqlType(1);
    vshztr_ztrCZT.setCaption("Šifra zavisnog troška");
    vshztr_ztrCZT.setColumnName("CZT");
    vshztr_ztrCZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    vshztr_ztrCZT.setPrecision(2);
    vshztr_ztrCZT.setTableName("VSHZTR_ZTR");
    vshztr_ztrCZT.setServerColumnName("CZT");
    vshztr_ztrCZT.setSqlType(1);
    vshztr_ztrZTNAZT.setCaption("ZnZ");
    vshztr_ztrZTNAZT.setColumnName("ZTNAZT");
    vshztr_ztrZTNAZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    vshztr_ztrZTNAZT.setDefault("N");
    vshztr_ztrZTNAZT.setPrecision(1);
    vshztr_ztrZTNAZT.setTableName("VSHZTR_ZTR");
    vshztr_ztrZTNAZT.setServerColumnName("ZTNAZT");
    vshztr_ztrZTNAZT.setSqlType(1);
    vshztr_ztrPZT.setCaption("Posto zavisnog troška");
    vshztr_ztrPZT.setColumnName("PZT");
    vshztr_ztrPZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vshztr_ztrPZT.setDisplayMask("###,###,##0.00");
    vshztr_ztrPZT.setDefault("0");
    vshztr_ztrPZT.setPrecision(10);
    vshztr_ztrPZT.setScale(2);
    vshztr_ztrPZT.setTableName("VSHZTR_ZTR");
    vshztr_ztrPZT.setServerColumnName("PZT");
    vshztr_ztrPZT.setSqlType(2);
    vshztr_ztrAKTIV.setColumnName("AKTIV");
    vshztr_ztrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vshztr_ztrAKTIV.setDefault("D");
    vshztr_ztrAKTIV.setPrecision(1);
    vshztr_ztrAKTIV.setTableName("VSHZTR_ZTR");
    vshztr_ztrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vshztr_ztrAKTIV.setServerColumnName("AKTIV");
    vshztr_ztrAKTIV.setSqlType(1);
    vshztr_ztrLOKK.setColumnName("LOKK");
    vshztr_ztrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vshztr_ztrLOKK.setDefault("N");
    vshztr_ztrLOKK.setPrecision(1);
    vshztr_ztrLOKK.setTableName("VSHZTR_ZTR");
    vshztr_ztrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vshztr_ztrLOKK.setServerColumnName("LOKK");
    vshztr_ztrLOKK.setSqlType(1);
    vshztr_ztr.setResolver(dm.getQresolver());
    vshztr_ztr.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from vshztr_ztr", null, true, Load.ALL));
 setColumns(new Column[] {vshztr_ztrLOKK, vshztr_ztrAKTIV, vshztr_ztrCSHZT, vshztr_ztrRBR, vshztr_ztrCZT, vshztr_ztrPZT, vshztr_ztrIZT,
        vshztr_ztrZTNAZT});
  }

  public void setall(){

    /*SqlDefTabela = "create table vshztr_ztr " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cshrab char(3) not null , "+
      "rbr numeric(4,0) not null, " + // Redni broj stavke
      "crab  char(2) CHARACTER SET WIN1250 , "+
      "prab numeric(6,2),"+
      "rabnarab char(1) CHARACTER SET WIN1250 default 'N'," + // Rabat na rabat
       Primary Key (cshrab,rbr))" ; */

    ddl.create("vshztr_ztr")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cshzt", 3, true)
       .addShort("rbr", 4, true)
       .addChar("czt", 2)
       .addFloat("pzt", 6, 2)
       .addFloat("izt", 17, 2)
       .addChar("ztnazt", 1, "N")
       .addPrimaryKey("cshzt,rbr");

    Naziv="vshztr_ztr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);


    /*
    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+"ivshrab_rabkey on vshrab_rab (cshrab,rbr)" };

    NaziviIdx=new String[]{"ivshrab_rabkey" }; */

   }
}