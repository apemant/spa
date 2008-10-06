/****license*****************************************************************
**   file: vtrabat.java
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

public class vtrabat extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static vtrabat vtrabatclass;
  dM dm  = dM.getDataModule();
  QueryDataSet vtrabat = new QueryDataSet();
  Column vtrabLOKK = new Column();
  Column vtrabAKTIV = new Column();
  Column vtrabCSKL = new Column();
  Column vtrabVRDOK = new Column();
  Column vtrabGOD = new Column();
  Column vtrabBRDOK = new Column();
  Column vtrabRBR = new Column();
  Column vtrabLRBR = new Column();
  Column vtrabCRAB = new Column();
  Column vtrabPRAB = new Column();
  Column vtrabIRAB = new Column();
  Column vtrabIRABNARAB = new Column();


 public static vtrabat getDataModule() {
    if (vtrabatclass == null) {
      vtrabatclass = new vtrabat();
    }
    return vtrabatclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return vtrabat;
  }
  public vtrabat() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    vtrabIRABNARAB.setCaption("RnR");
    vtrabIRABNARAB.setColumnName("RABNARAB");
    vtrabIRABNARAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtrabIRABNARAB.setPrecision(1);
    vtrabIRABNARAB.setTableName("VTRABAT");
    vtrabIRABNARAB.setServerColumnName("RABNARAB");
    vtrabIRABNARAB.setSqlType(1);
    vtrabIRAB.setCaption("Iznos rabata");
    vtrabIRAB.setColumnName("IRAB");
    vtrabIRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtrabIRAB.setDisplayMask("###,###,##0.00");
    vtrabIRAB.setDefault("0");
    vtrabIRAB.setPrecision(15);
    vtrabIRAB.setScale(2);
    vtrabIRAB.setTableName("VTRABAT");
    vtrabIRAB.setServerColumnName("IRAB");
    vtrabIRAB.setSqlType(2);
    vtrabPRAB.setCaption("Posto rabata");
    vtrabPRAB.setColumnName("PRAB");
    vtrabPRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vtrabPRAB.setDisplayMask("###,##0.00");
    vtrabPRAB.setDefault("0");
    vtrabPRAB.setPrecision(10);
    vtrabPRAB.setScale(2);
    vtrabPRAB.setTableName("VTRABAT");
    vtrabPRAB.setServerColumnName("PRAB");
    vtrabPRAB.setSqlType(2);
    vtrabCRAB.setCaption("Šifra rabata");
    vtrabCRAB.setColumnName("CRAB");
    vtrabCRAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtrabCRAB.setPrecision(2);
    vtrabCRAB.setTableName("VTRABAT");
    vtrabCRAB.setServerColumnName("CRAB");
    vtrabCRAB.setSqlType(1);
    vtrabLRBR.setCaption("Lrbr");
    vtrabLRBR.setColumnName("LRBR");
    vtrabLRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vtrabLRBR.setRowId(true);
    vtrabLRBR.setTableName("VTRABAT");
    vtrabLRBR.setServerColumnName("LRBR");
    vtrabLRBR.setSqlType(5);
    vtrabRBR.setCaption("Rbr");
    vtrabRBR.setColumnName("RBR");
    vtrabRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vtrabRBR.setRowId(true);
    vtrabRBR.setTableName("VTRABAT");
    vtrabRBR.setServerColumnName("RBR");
    vtrabRBR.setSqlType(5);
    vtrabBRDOK.setCaption("Broj");
    vtrabBRDOK.setColumnName("BRDOK");
    vtrabBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    vtrabBRDOK.setRowId(true);
    vtrabBRDOK.setTableName("VTRABAT");
    vtrabBRDOK.setServerColumnName("BRDOK");
    vtrabBRDOK.setSqlType(4);
    vtrabGOD.setCaption("Godina");
    vtrabGOD.setColumnName("GOD");
    vtrabGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtrabGOD.setPrecision(4);
    vtrabGOD.setRowId(true);
    vtrabGOD.setTableName("VTRABAT");
    vtrabGOD.setServerColumnName("GOD");
    vtrabGOD.setSqlType(1);
    vtrabVRDOK.setCaption("Vrsta");
    vtrabVRDOK.setColumnName("VRDOK");
    vtrabVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtrabVRDOK.setPrecision(3);
    vtrabVRDOK.setRowId(true);
    vtrabVRDOK.setTableName("VTRABAT");
    vtrabVRDOK.setServerColumnName("VRDOK");
    vtrabVRDOK.setSqlType(1);
    vtrabCSKL.setCaption("Skladište");
    vtrabCSKL.setColumnName("CSKL");
    vtrabCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtrabCSKL.setPrecision(12);
    vtrabCSKL.setRowId(true);
    vtrabCSKL.setTableName("VTRABAT");
    vtrabCSKL.setServerColumnName("CSKL");
    vtrabCSKL.setSqlType(1);
    vtrabAKTIV.setColumnName("AKTIV");
    vtrabAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtrabAKTIV.setDefault("D");
    vtrabAKTIV.setPrecision(1);
    vtrabAKTIV.setTableName("VTRABAT");
    vtrabAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vtrabAKTIV.setServerColumnName("AKTIV");
    vtrabAKTIV.setSqlType(1);
    vtrabLOKK.setColumnName("LOKK");
    vtrabLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vtrabLOKK.setDefault("N");
    vtrabLOKK.setPrecision(1);
    vtrabLOKK.setTableName("VTRABAT");
    vtrabLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vtrabLOKK.setServerColumnName("LOKK");
    vtrabLOKK.setSqlType(1);
    vtrabat.setResolver(dm.getQresolver());
    vtrabat.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from vtrabat", null, true, Load.ALL));
 setColumns(new Column[] {vtrabLOKK, vtrabAKTIV, vtrabCSKL, vtrabVRDOK, vtrabGOD, vtrabBRDOK, vtrabRBR, vtrabLRBR, vtrabCRAB, vtrabPRAB, vtrabIRAB, vtrabIRABNARAB});
  }

  public void setall(){

   /* SqlDefTabela = "create table vtrabat " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) character set win1250 default 'D',"  +
      "cskl char(6) CHARACTER SET WIN1250 not null,"+ //Šifra skladišta
      "vrdok char(3) CHARACTER SET WIN1250 not null," +   //Vrsta dokumenta (OTP,PRI,..)
      "god char(4) CHARACTER SET WIN1250 not null," + // Godina zalihe
      "brdok numeric(6,0) not null , " + // Broj dokumenta
      "rbr numeric(4,0) not null , " + // Redni broj stavke ako je 0 odnosi se na zaglavlje
      "lrbr numeric(4,0) not null, " + // Služi kao lokalni redni broj
      "crab char(1) CHARACTER SET WIN1250 , " +
      "prab numeric(6,2)," + //Posto rabata
      "irab numeric(17,2)," + //Iznos rabata
      "rabnarab char(1) CHARACTER SET WIN1250 , " +
      "Primary Key (cskl,vrdok,god,brdok,rbr,lrbr))" ;  */

    ddl.create("vtrabat")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskl", 12, true)
       .addChar("vrdok", 3, true)
       .addChar("god", 4, true)
       .addInteger("brdok", 6, true)
       .addShort("rbr", 4, true)
       .addShort("lrbr", 4, true)
       .addChar("crab", 2)
       .addFloat("prab", 6, 2)
       .addFloat("irab", 17, 2)
       .addChar("rabnarab", 1)
       .addPrimaryKey("cskl,vrdok,god,brdok,rbr,lrbr");

    Naziv="vtrabat";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

    /*
    NaziviIdx=new String[]{"ivtrabatkey"};

    DefIndex= new String[] {CommonTable.SqlDefUniqueIndex+NaziviIdx[0] +" on vtrabat (cskl,vrdok,god,brdok,rbr,lrbr)"};
*/
  }

}