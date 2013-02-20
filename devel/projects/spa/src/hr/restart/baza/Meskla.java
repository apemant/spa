/****license*****************************************************************
**   file: Meskla.java
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
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class Meskla extends KreirDrop implements DataModule {

  private static Meskla Mesklaclass;
  dM dm  = dM.getDataModule();
  QueryDataSet Meskla = new raDataSet();
  QueryDataSet MesklaMES = new raDataSet();
  QueryDataSet MesklaMEU = new raDataSet();
  QueryDataSet MesklaMEI = new raDataSet();

  /*Column mesklaLOKK = new Column();
  Column mesklaAKTIV = new Column();
  Column mesklaCSKLIZ = new Column();
  Column mesklaCSKLUL = new Column();
  Column mesklaVRDOK = new Column();
  Column mesklaCUSER = new Column();
  Column mesklaGOD = new Column();
  Column mesklaBRDOK = new Column();
  Column mesklaUI = new Column();
  Column mesklaSYSDAT = new Column();
  Column mesklaDATDOK = new Column();
  Column mesklaOZNVAL = new Column();
  Column mesklaTECAJ = new Column();
  Column mesklaBRNAL = new Column();
  Column mesklaDATKNJ = new Column();
  Column mesklaBRNALU = new Column();
  Column mesklaDATKNJU = new Column();
  Column mesklaSTATUS = new Column();
  Column mesklaSTATKNJI = new Column();
  Column mesklaSTATKNJU = new Column();
  Column mesklaOPIS = new Column();
  Column mesklaPARAM = new Column();
  Column mesklaSTAT_KPR = new Column();*/


  public static Meskla getDataModule() {
    if (Mesklaclass == null) {
      Mesklaclass = new Meskla();
    }
    return Mesklaclass;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return Meskla;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMES() {
    return MesklaMES;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMEU() {
    return MesklaMEU;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMEI() {
    return MesklaMEI;
  }

  public Meskla() {
     try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    /*mesklaPARAM.setCaption("Parametri");
    mesklaPARAM.setColumnName("PARAM");
    mesklaPARAM.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaPARAM.setPrecision(8);
    mesklaPARAM.setTableName("MESKLA");
    mesklaPARAM.setServerColumnName("PARAM");
    mesklaPARAM.setSqlType(1);

    mesklaSTAT_KPR.setCaption("Stat KPR");
    mesklaSTAT_KPR.setColumnName("STAT_KPR");
    mesklaSTAT_KPR.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaSTAT_KPR.setPrecision(1);
    mesklaSTAT_KPR.setDefault("N");
    mesklaSTAT_KPR.setTableName("MESKLA");
    mesklaSTAT_KPR.setServerColumnName("STAT_KPR");
    mesklaSTAT_KPR.setSqlType(1);


    mesklaOPIS.setCaption("Opis");
    mesklaOPIS.setColumnName("OPIS");
    mesklaOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaOPIS.setPrecision(50);
    mesklaOPIS.setTableName("MESKLA");
    mesklaOPIS.setServerColumnName("Opis");
    mesklaOPIS.setSqlType(1);

    mesklaTECAJ.setCaption("Te\u010Daj");
    mesklaTECAJ.setColumnName("TECAJ");
    mesklaTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    mesklaTECAJ.setDisplayMask("###,##0.000000");
    mesklaTECAJ.setDefault("0");
    mesklaTECAJ.setPrecision(15);
    mesklaTECAJ.setScale(6);
    mesklaTECAJ.setTableName("MESKLA");
    mesklaTECAJ.setServerColumnName("TECAJ");
    mesklaTECAJ.setSqlType(2);
    mesklaOZNVAL.setCaption("Oznaka valute");
    mesklaOZNVAL.setColumnName("OZNVAL");
    mesklaOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaOZNVAL.setPrecision(3);
    mesklaOZNVAL.setTableName("MESKLA");
    mesklaOZNVAL.setServerColumnName("OZNVAL");
    mesklaOZNVAL.setSqlType(1);
    mesklaSTATKNJU.setCaption("Stat knj. ul.");
    mesklaSTATKNJU.setColumnName("STATKNJU");
    mesklaSTATKNJU.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaSTATKNJU.setDefault("N");
    mesklaSTATKNJU.setPrecision(1);
    mesklaSTATKNJU.setTableName("MESKLA");
    mesklaSTATKNJU.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    mesklaSTATKNJU.setServerColumnName("STATKNJU");
    mesklaSTATKNJU.setSqlType(1);
    mesklaSTATKNJI.setCaption("Stat. knj. iz");
    mesklaSTATKNJI.setColumnName("STATKNJI");
    mesklaSTATKNJI.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaSTATKNJI.setDefault("N");
    mesklaSTATKNJI.setPrecision(1);
    mesklaSTATKNJI.setTableName("MESKLA");
    mesklaSTATKNJI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    mesklaSTATKNJI.setServerColumnName("STATKNJI");
    mesklaSTATKNJI.setSqlType(1);
    mesklaSTATUS.setCaption("Status");
    mesklaSTATUS.setColumnName("STATUS");
    mesklaSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaSTATUS.setDefault("N");
    mesklaSTATUS.setPrecision(1);
    mesklaSTATUS.setTableName("MESKLA");
    mesklaSTATUS.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    mesklaSTATUS.setServerColumnName("STATUS");
    mesklaSTATUS.setSqlType(1);
    mesklaDATKNJ.setCaption("Datum knjiženja");
    mesklaDATKNJ.setColumnName("DATKNJ");
    mesklaDATKNJ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    mesklaDATKNJ.setDisplayMask("dd-MM-yyyy");
//    mesklaDATKNJ.setEditMask("dd-MM-yyyy");
    mesklaDATKNJ.setTableName("MESKLA");
    mesklaDATKNJ.setWidth(10);
    mesklaDATKNJ.setServerColumnName("DATKNJ");
    mesklaDATKNJ.setSqlType(93);
    mesklaBRNAL.setCaption("Nalog");
    mesklaBRNAL.setColumnName("BRNAL");
    mesklaBRNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaBRNAL.setPrecision(8);
    mesklaBRNAL.setTableName("MESKLA");
    mesklaBRNAL.setServerColumnName("BRNAL");
    mesklaBRNAL.setSqlType(1);
    mesklaDATKNJU.setCaption("Datum knjiženja ul");
    mesklaDATKNJU.setColumnName("DATKNJU");
    mesklaDATKNJU.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    mesklaDATKNJU.setDisplayMask("dd-MM-yyyy");
//    mesklaDATKNJ.setEditMask("dd-MM-yyyy");
    mesklaDATKNJU.setTableName("MESKLA");
    mesklaDATKNJU.setWidth(10);
    mesklaDATKNJU.setServerColumnName("DATKNJU");
    mesklaDATKNJU.setSqlType(93);
    mesklaBRNALU.setCaption("Nalog ulaz");
    mesklaBRNALU.setColumnName("BRNALU");
    mesklaBRNALU.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaBRNALU.setPrecision(8);
    mesklaBRNALU.setTableName("MESKLA");
    mesklaBRNALU.setServerColumnName("BRNALU");
    mesklaBRNALU.setSqlType(1);
    
    
    
    
    mesklaDATDOK.setCaption("Datum");
    mesklaDATDOK.setColumnName("DATDOK");
    mesklaDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    mesklaDATDOK.setDisplayMask("dd-MM-yyyy");
//    mesklaDATDOK.setEditMask("dd-MM-yyyy");
    mesklaDATDOK.setTableName("MESKLA");
    mesklaDATDOK.setWidth(10);
    mesklaDATDOK.setServerColumnName("DATDOK");
    mesklaDATDOK.setSqlType(93);
    mesklaSYSDAT.setCaption("Sysdat");
    mesklaSYSDAT.setColumnName("SYSDAT");
    mesklaSYSDAT.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    mesklaSYSDAT.setDisplayMask("dd-MM-yyyy");
    mesklaSYSDAT.setEditMask("dd-MM-yyyy");
    mesklaSYSDAT.setTableName("MESKLA");
    mesklaSYSDAT.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    mesklaSYSDAT.setWidth(9);
    mesklaSYSDAT.setServerColumnName("SYSDAT");
    mesklaSYSDAT.setSqlType(93);
    mesklaUI.setCaption("ui");
    mesklaUI.setColumnName("UI");
    mesklaUI.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaUI.setPrecision(1);
    mesklaUI.setTableName("MESKLA");
    mesklaUI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    mesklaUI.setServerColumnName("UI");
    mesklaUI.setSqlType(1);
    mesklaBRDOK.setCaption("Broj");
    mesklaBRDOK.setColumnName("BRDOK");
    mesklaBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    mesklaBRDOK.setRowId(true);
    mesklaBRDOK.setTableName("MESKLA");
    mesklaBRDOK.setWidth(7);
    mesklaBRDOK.setServerColumnName("BRDOK");
    mesklaBRDOK.setSqlType(4);
    mesklaGOD.setCaption("Godina");
    mesklaGOD.setColumnName("GOD");
    mesklaGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaGOD.setPrecision(4);
    mesklaGOD.setRowId(true);
    mesklaGOD.setTableName("MESKLA");
    mesklaGOD.setServerColumnName("GOD");
    mesklaGOD.setSqlType(1);
    mesklaVRDOK.setCaption("Vrsta");
    mesklaVRDOK.setColumnName("VRDOK");
    mesklaVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaVRDOK.setPrecision(3);
    mesklaVRDOK.setRowId(true);
    mesklaVRDOK.setTableName("MESKLA");
    mesklaVRDOK.setServerColumnName("VRDOK");
    mesklaVRDOK.setSqlType(1);
    mesklaCUSER.setCaption("Operater");
    mesklaCUSER.setColumnName("CUSER");
    mesklaCUSER.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaCUSER.setPrecision(15);
    mesklaCUSER.setTableName("MESKLA");
    mesklaCUSER.setServerColumnName("CUSER");
    mesklaCUSER.setSqlType(1);
    mesklaCSKLUL.setCaption("Ulazno skladište");
    mesklaCSKLUL.setColumnName("CSKLUL");
    mesklaCSKLUL.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaCSKLUL.setPrecision(12);
    mesklaCSKLUL.setRowId(true);
    mesklaCSKLUL.setTableName("MESKLA");
    mesklaCSKLUL.setServerColumnName("CSKLUL");
    mesklaCSKLUL.setSqlType(1);
    mesklaCSKLUL.setWidth(12);
    mesklaCSKLIZ.setCaption("Izlazno skladište");
    mesklaCSKLIZ.setColumnName("CSKLIZ");
    mesklaCSKLIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaCSKLIZ.setPrecision(12);
    mesklaCSKLIZ.setRowId(true);
    mesklaCSKLIZ.setTableName("MESKLA");
    mesklaCSKLIZ.setServerColumnName("CSKLIZ");
    mesklaCSKLIZ.setSqlType(1);
    mesklaCSKLIZ.setWidth(12);
    mesklaAKTIV.setColumnName("AKTIV");
    mesklaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaAKTIV.setDefault("D");
    mesklaAKTIV.setPrecision(1);
    mesklaAKTIV.setTableName("MESKLA");
    mesklaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    mesklaAKTIV.setServerColumnName("AKTIV");
    mesklaAKTIV.setSqlType(1);
    mesklaLOKK.setColumnName("LOKK");
    mesklaLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    mesklaLOKK.setDefault("N");
    mesklaLOKK.setPrecision(1);
    mesklaLOKK.setTableName("MESKLA");
    mesklaLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    mesklaLOKK.setServerColumnName("LOKK");
    mesklaLOKK.setSqlType(1);
    Meskla.setResolver(dm.getQresolver());
    Meskla.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from meskla", null, true, Load.ALL));
 setColumns(new Column[] {mesklaLOKK, mesklaAKTIV, mesklaCSKLIZ, mesklaCSKLUL, mesklaVRDOK, mesklaCUSER, mesklaGOD, mesklaBRDOK, mesklaUI, mesklaSYSDAT,
        mesklaDATDOK, mesklaOZNVAL, mesklaTECAJ, mesklaBRNAL, mesklaDATKNJ, mesklaSTATUS, mesklaSTATKNJI, mesklaSTATKNJU, mesklaOPIS, mesklaPARAM, mesklaSTAT_KPR,mesklaBRNALU, mesklaDATKNJU});
*/
    initModule();
    
    createFilteredDataSet(MesklaMES, "vrdok = 'MES'");
    createFilteredDataSet(MesklaMEU, "vrdok = 'MEU'");
    createFilteredDataSet(MesklaMEI, "vrdok = 'MEI'");
  }

  //public void setall(){

   /* SqlDefTabela =  "create table Meskla " +
        "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + //Status zauzetosti
        "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
        "cskliz char(6) CHARACTER SET WIN1250 not null,"+ //Šifra skladišta izlazna
        "csklul char(6) CHARACTER SET WIN1250 not null,"+ //Šifra skladišta ulazna
        "vrdok char(3) CHARACTER SET WIN1250 not null," +   //Vrsta dokumenta (MEU,MEI,..)
        "god char(4) CHARACTER SET WIN1250 not null," + // Godina zalihe
        "brdok numeric(6,0) not null , " + // Broj dokumenta
        "ui char(1) CHARACTER SET WIN1250 default 'U', "+   // Ulazni dokument
        "sysdat date ," + // Systemski datum na zahtjev Zagorca
        "datdok date ," + // Datum dokumenta
        "oznval char(3) CHARACTER SET WIN1250 ,"+// valuta
        "tecaj numeric (12,6)," + // Te\u010Daj
        "brnal char(8) CHARACTER SET WIN1250," + // Broj naloga knjiženja u FINK
        "datknj date," + // Datum knjiženja
        "status char(1) CHARACTER SET WIN1250,"+
        "statknji char(1) CHARACTER SET WIN1250 default 'N' ," + // Status knjiženja (N/P/D) izlaz
        "statknju char(1) CHARACTER SET WIN1250 default 'N' ," + // Status knjiženja (N/P/D) ulaz
        "Primary Key (cskliz,csklul,vrdok,god,brdok))" ;*/

    /*ddl.create("meskla")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskliz", 12, true)
       .addChar("csklul", 12, true)
       .addChar("vrdok", 3, true)
       .addChar("cuser", 15)
       .addChar("god", 4, true)
       .addInteger("brdok", 6, true)
       .addChar("ui", 1, "U")
       .addDate("sysdat")
       .addDate("datdok")
       .addChar("oznval", 3)
       .addFloat("tecaj", 12, 6)
       .addChar("brnal", 8)
       .addDate("datknj")
       .addChar("status", 1)
       .addChar("statknji", 1, "N")
       .addChar("statknju", 1, "N")
       .addChar("opis", 50)
       .addChar("param", 8)
       .addChar("stat_kpr", 1, "N")
       .addChar("brnalu", 8)
       .addDate("datknju")
       .addPrimaryKey("cskliz,csklul,vrdok,god,brdok");

    Naziv="Meskla";

    SqlDefTabela = ddl.getCreateTableString();*/

//    String[] idx = new String[] {/*"brdok"*/};
/*    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
*/
/*
    NaziviIdx=new String[]{"ilokkMeskla","iaktivMeskla","icsklulMeskla","icsklizMeskla",
                           "ivrdokMeskla","ibrdokMeskla","ipkMeskla"};

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Meskla (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Meskla (aktiv)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[2] +" on Meskla (csklul)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[3] +" on Meskla (cskliz)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[4] +" on Meskla (vrdok)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[5] +" on Meskla (brdok)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[6] +" on Meskla (csklul,cskliz,vrdok,god,brdok)" };
*/
//  }
}