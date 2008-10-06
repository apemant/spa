/****license*****************************************************************
**   file: vtzavtr.java
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


public class vtzavtr  extends KreirDrop implements DataModule {

  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static vtzavtr vtzavtrclass;
  dM dm  = dM.getDataModule();
  QueryDataSet vtzavtr = new QueryDataSet();

  Column vtzavLOKK = new Column();
  Column vtzavAKTIV = new Column();
  Column vtzavCSKL = new Column();
  Column vtzavVRDOK = new Column();
  Column vtzavGOD = new Column();
  Column vtzavBRDOK = new Column();
  Column vtzavRBR = new Column();
  Column vtzavLRBR = new Column();
  Column vtzavCZT = new Column();
  Column vtzavPZT = new Column();
  Column vtzavIZT = new Column();
  Column vtzavUIPRPOR = new Column();
  Column vtzavZTNAZT = new Column();
  Column vtzavBROJKONTA = new Column();

  public static vtzavtr getDataModule() {
    if (vtzavtrclass == null) {
      vtzavtrclass = new vtzavtr();
    }
    return vtzavtrclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return vtzavtr;
  }
  public vtzavtr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    vtzavBROJKONTA.setCaption("Konto partnera");
    vtzavBROJKONTA.setColumnName("BROJKONTA");
    vtzavBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtzavBROJKONTA.setPrecision(8);
    vtzavBROJKONTA.setTableName("VTZAVTR");
    vtzavBROJKONTA.setSqlType(1);
    vtzavBROJKONTA.setServerColumnName("BROJKONTA");

    vtzavZTNAZT.setCaption("ZnZ");
    vtzavZTNAZT.setColumnName("ZTNAZT");
    vtzavZTNAZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtzavZTNAZT.setPrecision(1);
    vtzavZTNAZT.setTableName("VTZAVTR");
    vtzavZTNAZT.setServerColumnName("ZTNAZT");
    vtzavZTNAZT.setSqlType(1);
    vtzavUIPRPOR.setCaption("Iznos pretporeza");
    vtzavUIPRPOR.setColumnName("UIPRPOR");
    vtzavUIPRPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtzavUIPRPOR.setDisplayMask("###,###,##0.00");
    vtzavUIPRPOR.setDefault("0");
    vtzavUIPRPOR.setPrecision(15);
    vtzavUIPRPOR.setScale(2);
    vtzavUIPRPOR.setTableName("VTZAVTR");
    vtzavUIPRPOR.setServerColumnName("UIPRPOR");
    vtzavUIPRPOR.setSqlType(2);

    vtzavIZT.setCaption("Iznos zavisnog troška");
    vtzavIZT.setColumnName("IZT");
    vtzavIZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtzavIZT.setDisplayMask("###,###,##0.00");
    vtzavIZT.setDefault("0");
    vtzavIZT.setPrecision(15);
    vtzavIZT.setScale(2);
    vtzavIZT.setTableName("VTZAVTR");
    vtzavIZT.setServerColumnName("IZT");
    vtzavIZT.setSqlType(2);
    vtzavPZT.setCaption("Posto zavisnog troška");
    vtzavPZT.setColumnName("PZT");
    vtzavPZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtzavPZT.setDisplayMask("###,##0.00");
    vtzavPZT.setDefault("0");
    vtzavPZT.setPrecision(10);
    vtzavPZT.setScale(2);
    vtzavPZT.setTableName("VTZAVTR");
    vtzavPZT.setServerColumnName("PZT");
    vtzavPZT.setSqlType(2);
    vtzavCZT.setCaption("Šifra zavisnog troška");
    vtzavCZT.setColumnName("CZT");
    vtzavCZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtzavCZT.setPrecision(5);
    vtzavCZT.setTableName("VTZAVTR");
    vtzavCZT.setServerColumnName("CZT");
    vtzavCZT.setSqlType(1);
    vtzavLRBR.setCaption("Lrbr");
    vtzavLRBR.setColumnName("LRBR");
    vtzavLRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vtzavLRBR.setRowId(true);
    vtzavLRBR.setTableName("VTZAVTR");
    vtzavLRBR.setServerColumnName("LRBR");
    vtzavLRBR.setSqlType(5);
    vtzavRBR.setCaption("Rbr");
    vtzavRBR.setColumnName("RBR");
    vtzavRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vtzavRBR.setRowId(true);
    vtzavRBR.setTableName("VTZAVTR");
    vtzavRBR.setServerColumnName("RBR");
    vtzavRBR.setSqlType(5);
    vtzavBRDOK.setCaption("Broj");
    vtzavBRDOK.setColumnName("BRDOK");
    vtzavBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    vtzavBRDOK.setRowId(true);
    vtzavBRDOK.setTableName("VTZAVTR");
    vtzavBRDOK.setServerColumnName("BRDOK");
    vtzavBRDOK.setSqlType(4);
    vtzavGOD.setCaption("Godina");
    vtzavGOD.setColumnName("GOD");
    vtzavGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtzavGOD.setPrecision(4);
    vtzavGOD.setRowId(true);
    vtzavGOD.setTableName("VTZAVTR");
    vtzavGOD.setServerColumnName("GOD");
    vtzavGOD.setSqlType(1);
    vtzavVRDOK.setCaption("Vrsta");
    vtzavVRDOK.setColumnName("VRDOK");
    vtzavVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtzavVRDOK.setPrecision(3);
    vtzavVRDOK.setRowId(true);
    vtzavVRDOK.setTableName("VTZAVTR");
    vtzavVRDOK.setServerColumnName("VRDOK");
    vtzavVRDOK.setSqlType(1);
    vtzavCSKL.setCaption("Skladište");
    vtzavCSKL.setColumnName("CSKL");
    vtzavCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtzavCSKL.setPrecision(12);
    vtzavCSKL.setRowId(true);
    vtzavCSKL.setTableName("VTZAVTR");
    vtzavCSKL.setServerColumnName("CSKL");
    vtzavCSKL.setSqlType(1);
    vtzavAKTIV.setColumnName("AKTIV");
    vtzavAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtzavAKTIV.setDefault("D");
    vtzavAKTIV.setPrecision(1);
    vtzavAKTIV.setTableName("VTZAVTR");
    vtzavAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vtzavAKTIV.setServerColumnName("AKTIV");
    vtzavAKTIV.setSqlType(1);
    vtzavLOKK.setColumnName("LOKK");
    vtzavLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtzavLOKK.setDefault("N");
    vtzavLOKK.setPrecision(1);
    vtzavLOKK.setTableName("VTZAVTR");
    vtzavLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vtzavLOKK.setServerColumnName("LOKK");
    vtzavLOKK.setSqlType(1);

    vtzavtr.setResolver(dm.getQresolver());
    vtzavtr.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from vtzavtr", null, true, Load.ALL));
 setColumns(new Column[] {vtzavLOKK, vtzavAKTIV, vtzavCSKL, vtzavVRDOK, vtzavGOD, vtzavBRDOK, vtzavRBR, vtzavLRBR, vtzavCZT, vtzavPZT, vtzavIZT,
                vtzavUIPRPOR, vtzavZTNAZT, vtzavBROJKONTA});
  }

  public void setall(){

    /*SqlDefTabela = "create table vtzavtr " +
      "(id_rbr numeric(12,0) not null, " + // Služi kao autonumber broja\u010D zbog primarnog klju\u010Da
      "cskl char(6) CHARACTER SET WIN1250 ,"+ //Šifra skladišta
      "vrdok char(3) CHARACTER SET WIN1250 ," +   //Vrsta dokumenta (OTP,PRI,..)
      "god char(4) CHARACTER SET WIN1250 not null," + // Godina zalihe
      "brdok numeric(6,0) not null , " + // Broj dokumenta
      "rbr numeric(4,0) , " + // Redni broj stavke
      "czt char(2) CHARACTER SET WIN1250 not null ," + // Šifra zavisnog troška
      "cpar numeric(6,0),"+ //Partner nije obligatan
      "pzt numeric(6,2)," + // Posto zavisni troškovi
      "izt numeric(17,2) ," + // Iznos zavisni troškovi
      "ztnazt char(1) CHARACTER SET WIN1250 , " +
      "Primary Key (id_rbr))" ; */

    ddl.create("vtzavtr")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskl", 12, true)
       .addChar("vrdok", 3, true)
       .addChar("god", 4, true)
       .addInteger("brdok", 6, true)
       .addShort("rbr", 4, true)
       .addShort("lrbr", 4, true)
       .addChar("czt", 5)
       .addFloat("pzt", 6, 2)
       .addFloat("izt", 17, 2)
       .addFloat("uiprpor", 17, 2)
       .addChar("ztnazt", 1)
       .addChar("brojkonta", 8)
       .addPrimaryKey("cskl,vrdok,god,brdok,rbr,lrbr");

    Naziv="vtzavtr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    NaziviIdx=new String[]{"ivtzavtrkey","ivtzavtrcskl","ivtzavtrvrdok","ivtzavtrbrdok","ivtzavtrrbr","ivtzavtrczt",
                            "ivtzavtrcpar"};

    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+NaziviIdx[0] +" on vtzavtr (id_rbr)",
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on vtzavtr (cskl)",
                            CommonTable.SqlDefIndex+NaziviIdx[2] +" on vtzavtr (vrdok)",
                            CommonTable.SqlDefIndex+NaziviIdx[3] +" on vtzavtr (brdok)",
                            CommonTable.SqlDefIndex+NaziviIdx[4] +" on vtzavtr (rbr)",
                            CommonTable.SqlDefIndex+NaziviIdx[5] +" on vtzavtr (czt)",
                            CommonTable.SqlDefIndex+NaziviIdx[6] +" on vtzavtr (cpar)"};
*/
  }
}