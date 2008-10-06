/****license*****************************************************************
**   file: Rate.java
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
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;


public class Rate extends KreirDrop implements DataModule {

  private static Rate rateclass;
  dM dm  = dM.getDataModule();
  QueryDataSet rate = new QueryDataSet();
  Column rateLOKK = new Column();
  Column rateAKTIV = new Column();
  Column rateCSKL = new Column();
  Column rateVRDOK = new Column();
  Column rateGOD = new Column();
  Column rateBRDOK = new Column();
  Column rateRBR = new Column();
  Column rateCNACPL = new Column();
  Column rateCBANKA = new Column();
  Column rateBANKA = new Column();
  Column rateBROJ_TRG = new Column();
  Column rateBROJ_CEK = new Column();
  Column rateBRRATA = new Column();
  Column rateDATUM = new Column();
  Column rateIRATA = new Column();
  Column rateDATDOK = new Column();
  Column rateVLASNIK = new Column();
  Column rateVRIDO = new Column();
  Column rateCPRODMJ = new Column();

  public static Rate getDataModule() {
    if (rateclass == null) {
      rateclass = new Rate();
    }
    return rateclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return rate;
  }
  public Rate() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setall(){

    /*SqlDefTabela = "create table Placrata " +
        "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + // Status zauzetosti
        "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
        "cskl char(6) CHARACTER SET WIN1250 not null,"+       // Šifra skladišta
        "vrdok char(3) CHARACTER SET WIN1250 not null," +     // Vrsta dokumenta (OTP,PRI,..)
        "god char(4) CHARACTER SET WIN1250 not null," +       // Godina zalihe
        "brdok numeric(6,0) not null , " +                    // Broj dokumenta
        "rbr numeric(4,0) not null, " +                       // Redni broj nacina pl unutar rate
        "cnacpl char(3) character set win1250 ,"+
        "cbanka char(4) CHARACTER SET WIN1250, "+    // Šifra
        "banka char(1) CHARACTER SET WIN1250, "+              // Flag banka gotovina www.askslinisa.negdje
        "broj_trg char(15) CHARACTER SET WIN1250, "+          // broj tekuceg racuna gradjana ili kartice
        "broj_cek char(15) CHARACTER SET WIN1250, "+          // broj tekuceg racuna \u010Deka ili slip-a
        "brrata numeric(3,0) ," +
        "datum date , " +
        "irata  numeric(17,2)," +                             //  Iznos dijela rate
        "Primary Key (cskl,vrdok,brdok,rbr))" ; */
    ddl.create("rate")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskl", 12, true)
       .addChar("vrdok", 3, true)
       .addChar("god", 4, true)
       .addInteger("brdok", 6, true)
       .addShort("rbr", 4, true)
       .addChar("cnacpl", 3)
       .addChar("cbanka", 4)
       .addChar("banka", 1)
       .addChar("broj_trg", 30)
       .addChar("broj_cek", 30)
       .addShort("brrata", 3)
       .addDate("datum")
       .addFloat("irata", 17, 2)
       .addDate("datdok")
       .addChar("vlasnik", 50)
       .addChar("vrijedido", 8)
       .addChar("cprodmj", 3, true)
       .addPrimaryKey("cskl,vrdok,god,brdok,cprodmj,rbr");

    Naziv="Rate";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

/*
    DefIndex= new String[] {
              CommonTable.SqlDefIndex+"iratelokk on Placrata (lokk)",
              CommonTable.SqlDefIndex+"iratektiv on Placrata (aktiv)",
              CommonTable.SqlDefUniqueIndex+"iratekey on Placrata (cskl,vrdok,god,brdok,rbr)"};

    NaziviIdx=new String[]{"iratelokk","iratektiv","iratekey"};
*/
  }

  private void jbInit() throws Exception {
    rateDATDOK.setCaption("Datum dokumenta");
    rateDATDOK.setColumnName("DATDOK");
    rateDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    rateDATDOK.setDisplayMask("dd-MM-yyyy");
//    rateDATDOK.setEditMask("dd-MM-yyyy");
    rateDATDOK.setTableName("RATE");
    rateDATDOK.setWidth(10);
    rateDATDOK.setServerColumnName("DATDOK");
    rateDATDOK.setSqlType(93);

    rateDATUM.setCaption("Datum");
    rateDATUM.setColumnName("DATUM");
    rateDATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    rateDATUM.setDisplayMask("dd-MM-yyyy");
//    rateDATUM.setEditMask("dd-MM-yyyy");
    rateDATUM.setTableName("RATE");
    rateDATUM.setWidth(10);
    rateDATUM.setServerColumnName("DATUM");
    rateDATUM.setSqlType(93);
    rateBRRATA.setCaption("Rata");
    rateBRRATA.setColumnName("BRRATA");
    rateBRRATA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rateBRRATA.setTableName("RATE");
    rateBRRATA.setServerColumnName("BRRATA");
    rateBRRATA.setSqlType(5);
    rateBROJ_CEK.setCaption("Broj \u010Deka");
    rateBROJ_CEK.setColumnName("BROJ_CEK");
    rateBROJ_CEK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateBROJ_CEK.setPrecision(30);
    rateBROJ_CEK.setTableName("RATE");
    rateBROJ_CEK.setServerColumnName("BROJ_CEK");
    rateBROJ_CEK.setSqlType(1);
    rateBROJ_TRG.setCaption("Broj ra\u010Duna");
    rateBROJ_TRG.setColumnName("BROJ_TRG");
    rateBROJ_TRG.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateBROJ_TRG.setPrecision(30);
    rateBROJ_TRG.setTableName("RATE");
    rateBROJ_TRG.setServerColumnName("BROJ_TRG");
    rateBROJ_TRG.setSqlType(1);
    rateBANKA.setCaption("Flag");
    rateBANKA.setColumnName("BANKA");
    rateBANKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateBANKA.setPrecision(1);
    rateBANKA.setTableName("RATE");
    rateBANKA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    rateBANKA.setServerColumnName("BANKA");
    rateBANKA.setSqlType(1);
    rateCBANKA.setCaption("Banka");
    rateCBANKA.setColumnName("CBANKA");
    rateCBANKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateCBANKA.setPrecision(4);
    rateCBANKA.setTableName("RATE");
    rateCBANKA.setServerColumnName("CBANKA");
    rateCBANKA.setSqlType(1);
    rateCNACPL.setCaption("Pla\u0107anje");
    rateCNACPL.setColumnName("CNACPL");
    rateCNACPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateCNACPL.setPrecision(3);
    rateCNACPL.setTableName("RATE");
    rateCNACPL.setServerColumnName("CNACPL");
    rateCNACPL.setSqlType(1);
    rateRBR.setCaption("Rbr");
    rateRBR.setColumnName("RBR");
    rateRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rateRBR.setRowId(true);
    rateRBR.setTableName("RATE");
    rateRBR.setServerColumnName("RBR");
    rateRBR.setSqlType(5);
    rateBRDOK.setCaption("Broj");
    rateBRDOK.setColumnName("BRDOK");
    rateBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    rateBRDOK.setRowId(true);
    rateBRDOK.setTableName("RATE");
    rateBRDOK.setServerColumnName("BRDOK");
    rateBRDOK.setSqlType(4);
    rateGOD.setCaption("Godina");
    rateGOD.setColumnName("GOD");
    rateGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateGOD.setPrecision(4);
    rateGOD.setRowId(true);
    rateGOD.setTableName("RATE");
    rateGOD.setServerColumnName("GOD");
    rateGOD.setSqlType(1);
    rateVRDOK.setCaption("Vrsta");
    rateVRDOK.setColumnName("VRDOK");
    rateVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateVRDOK.setPrecision(3);
    rateVRDOK.setRowId(true);
    rateVRDOK.setTableName("RATE");
    rateVRDOK.setServerColumnName("VRDOK");
    rateVRDOK.setSqlType(1);
    rateCSKL.setCaption("Skladište");
    rateCSKL.setColumnName("CSKL");
    rateCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateCSKL.setPrecision(12);
    rateCSKL.setRowId(true);
    rateCSKL.setTableName("RATE");
    rateCSKL.setServerColumnName("CSKL");
    rateCSKL.setSqlType(1);
    rateAKTIV.setColumnName("AKTIV");
    rateAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateAKTIV.setDefault("D");
    rateAKTIV.setPrecision(1);
    rateAKTIV.setTableName("RATE");
    rateAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    rateAKTIV.setServerColumnName("AKTIV");
    rateAKTIV.setSqlType(1);
    rateLOKK.setColumnName("LOKK");
    rateLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateLOKK.setDefault("N");
    rateLOKK.setPrecision(1);
    rateLOKK.setTableName("RATE");
    rateLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    rateLOKK.setServerColumnName("LOKK");
    rateLOKK.setSqlType(1);
    rateIRATA.setCaption("Iznos");
    rateIRATA.setColumnName("IRATA");
    rateIRATA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rateIRATA.setDisplayMask("###,###,##0.00");
    rateIRATA.setDefault("0");
    rateIRATA.setPrecision(15);
    rateIRATA.setScale(2);
    rateIRATA.setTableName("RATE");
    rateIRATA.setServerColumnName("IRATA");
    rateIRATA.setSqlType(2);

    rateVLASNIK.setCaption("Vlasnik");
    rateVLASNIK.setColumnName("VLASNIK");
    rateVLASNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateVLASNIK.setPrecision(50);
    rateVLASNIK.setTableName("RATE");
    rateVLASNIK.setServerColumnName("VLASNIK");
    rateVLASNIK.setSqlType(1);

    rateVRIDO.setCaption("Vrijedi do");
    rateVRIDO.setColumnName("VRIJEDIDO");
    rateVRIDO.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateVRIDO.setPrecision(8);
    rateVRIDO.setTableName("RATE");
    rateVRIDO.setServerColumnName("VRIJEDIDO");
    rateVRIDO.setSqlType(1);

    rateCPRODMJ.setCaption("Blagajna");
    rateCPRODMJ.setColumnName("CPRODMJ");
    rateCPRODMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    rateCPRODMJ.setPrecision(3);
    rateCPRODMJ.setRowId(true);
    rateCPRODMJ.setTableName("POS");
    rateCPRODMJ.setWidth(5);
    rateCPRODMJ.setServerColumnName("CPRODMJ");
    rateCPRODMJ.setSqlType(1);

    rate.setResolver(dm.getQresolver());
    rate.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from rate", null, true, Load.ALL));
 setColumns(new Column[] {rateLOKK, rateAKTIV, rateCSKL, rateVRDOK, rateGOD, rateBRDOK, rateRBR, rateCNACPL,
        rateCBANKA, rateBANKA, rateBROJ_TRG, rateBROJ_CEK, rateBRRATA, rateDATUM, rateIRATA, rateDATDOK,
        rateVLASNIK, rateVRIDO, rateCPRODMJ});
  }
}

