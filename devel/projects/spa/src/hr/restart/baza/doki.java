/****license*****************************************************************
**   file: doki.java
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
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class doki extends KreirDrop implements DataModule {

  private static doki dokiclass;

  QueryDataSet doki = new raDataSet();
  QueryDataSet zagPOS = new raDataSet();
  QueryDataSet zagGOT = new raDataSet();
  QueryDataSet zagPON = new raDataSet();
  QueryDataSet zagPONKup = new raDataSet();
  QueryDataSet zagPONOJ = new raDataSet();
  QueryDataSet zagPONPar = new raDataSet();
  QueryDataSet zagPOV = new raDataSet();
  QueryDataSet zagNAR = new raDataSet();
  QueryDataSet zagPRD = new raDataSet();
  QueryDataSet zagPRDkup = new raDataSet();
  QueryDataSet zagRAC = new raDataSet();
  QueryDataSet zagROT = new raDataSet();
  QueryDataSet zagOTP = new raDataSet();
  QueryDataSet zagDOS = new raDataSet();
  QueryDataSet zagIZD = new raDataSet();
  QueryDataSet zagPOD = new raDataSet();
  QueryDataSet zagPODKup = new raDataSet();
  QueryDataSet zagODB = new raDataSet();
  QueryDataSet zagTER = new raDataSet();
  QueryDataSet zagGRN = new raDataSet();
  QueryDataSet zagREV = new raDataSet();
  QueryDataSet zagPRE = new raDataSet();
  QueryDataSet zagNDO = new raDataSet();
  QueryDataSet zagINM = new raDataSet();
  QueryDataSet zagKON = new raDataSet();
  QueryDataSet zagZAH = new raDataSet();

  public static doki getDataModule() {
    if (dokiclass == null) {
      dokiclass = new doki();
    }
    return dokiclass;
  }
  public doki(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
//  public void setall(){

     /*SqlDefTabela = "create table Doki " +
        "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + //Status zauzetosti
        "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
        "cskl char(6) CHARACTER SET WIN1250 not null,"+ //Šifra skladišta
        "vrdok char(3) CHARACTER SET WIN1250 not null," +   //Vrsta dokumenta (OTP,PRI,..)
        "god char(4) CHARACTER SET WIN1250 not null," + // Godina zalihe
        "brdok numeric(6,0) not null , " + // Broj dokumenta
        "ui char(1) CHARACTER SET WIN1250 default 'U', "+   // Ulazni dokument
        "sysdat date ," + // Systemski datum na zahtjev Zagorca
        "datdok date ," + // Datum dokumenta
        "cpar numeric(6,0) , "+  // Šifra partnera
        "pj numeric (6,0), " + //Šifra poslovne jedinice
        "corg char(12) CHARACTER SET WIN1250," + // Šifra org. jedinice
        "cvrtr CHAR(12) CHARACTER SET WIN1250," + //Šifra vrsta troška
        "cug char(20) CHARACTER SET WIN1250," + //Broj ugovora
        "datug date," + //Datum ugovora
        "dvo date, " + // Datum DVO
        "ddosp numeric(4,0), " + // Dani dospije\u0107a
        "datdosp date," + //Datum dospije\u0107a
        "brdokiz char(20) CHARACTER SET WIN1250," + // Broj iz.dok. (otprem., dostavnica)
        "datdokiz date," + // Datum izlaznog dokumenta
        "brprd char(20) CHARACTER SET WIN1250,"+  // Broj predracuna ponude
        "datprd date ,"+   // Datum predracuna ponude
        "brnariz char(20)," + //Broj izlazne narudžbe
        "datnariz date ,"+   // Datum izlazne narudžbe
        "oznval char(3) CHARACTER SET WIN1250 ,"+ // valuta
        "tecaj numeric (12,6)," + // Te\u010Daj
        "brnal char(8) CHARACTER SET WIN1250," + // Broj naloga knjiženja u FINK
        "datknj date," + // Datum knjiženja
        "cradnal char(20) CHARACTER SET WIN1250," + // Broj radnog naloga
        "datradnal date," + // Datum radnog naloga
        "status char(1) CHARACTER SET WIN1250 default 'N',"+
        "statknj char(1) CHARACTER SET WIN1250 default 'N' ," + // Status knjiženja (N/P/D)
        "statpla char(1) CHARACTER SET WIN1250 default 'N' ," + // Status pla\u0107anja (N/D)
        "statira char(1) CHARACTER SET WIN1250 default 'N' ," + // Status prijenosa u IRU(N/D)
        "cfra char(3) CHARACTER SET WIN1250, " + //Frankatura
        "cnacpl char(3) CHARACTER SET WIN1250, " + // Na\u010Din pla\u0107anja
        "cnamj char(3) CHARACTER SET WIN1250, " + // Namjena robe
        "cnac char(3) CHARACTER SET WIN1250, " + // Na\u010Din otpreme robe
        "cnap char(3) CHARACTER SET WIN1250, " + // Šifra napomene
        "upzt numeric(6,2) ," + //Ukupno posto zavisnih troškova
        "cshzt char(3), " + //Šifra šeme zavisnih troškova
        "uprab numeric(6,2) , " + //Ukupno posto rabata
        "cshrab char(3) , " + // Šifra sheme rabata
        "uirac numeric(17,2),"  + // Ukupan iznos ra\u010Duna
        "opis char(200) CHARACTER SET WIN1250,"+
        "Primary Key (cskl,vrdok,god,brdok))" ;
   */
    /*ddl.create("doki")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cuser", 15)
       .addChar("cskl", 12, true)
       .addChar("vrdok", 3, true)
       .addChar("god", 4, true)
       .addInteger("brdok", 6, true)
       .addChar("ui", 1, "U")
       .addDate("sysdat")
       .addDate("datdok")
       .addInteger("cpar", 6)
       .addInteger("pj", 6)
       .addInteger("cagent", 6)
       .addInteger("ckupac", 6)
       .addChar("brpon", 20)
       .addDate("datpon")
       .addChar("corg", 12)
       .addChar("cvrtr", 12)
       .addChar("cug", 20)
       .addDate("datug")
       .addDate("dvo")
       .addShort("ddosp", 4)
       .addDate("datdosp")
       .addChar("brdokiz", 20)
       .addDate("datdokiz")
       .addChar("brprd", 20)
       .addDate("datprd")
       .addChar("brnariz", 40)
       .addDate("datnariz")
       .addChar("oznval", 3)
       .addFloat("tecaj", 12, 6)
       .addChar("brnal", 8)
       .addDate("datknj")
       .addChar("cradnal", 20)
       .addDate("datradnal")
       .addChar("status", 1, "N")
       .addChar("statknj", 1, "N")
       .addChar("statpla", 1, "N")
       .addChar("statira", 1, "N")
       .addChar("rezkol", 1, "N")
       .addChar("cfra", 3)
       .addChar("cnacpl", 3)
       .addChar("cnamj", 3)
       .addChar("cnac", 3)
       .addChar("cnap", 3)
       .addFloat("upzt", 6, 2)
       .addChar("cshzt", 3)
       .addFloat("uprab", 6, 2)
       .addChar("cshrab", 3)
       .addFloat("uirac", 17, 2)
       .addChar("opis", 200)
       .addShort("brrata", 3)
       .addFloat("platiti", 17, 2)
       .addFloat("uppopust", 6, 2)
       .addFloat("uipopust", 17, 2)
       .addChar("cradnik", 6)
       .addChar("ziro", 40)
       .addChar("pnbz2", 30)
       .addInteger("cko", 6)
       .addChar("param", 8)
       .addChar("stat_kpr", 1, "N")
       .addDate("datupl")//DATUPL
       .addChar("cblag", 6)
       .addFloat("provisp", 17, 2)
       .addFloat("provpost", 6, 2)
       .addPrimaryKey("cskl,vrdok,god,brdok");


    Naziv="doki";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"brdok", "datdok", "cpar"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);*/

/*
    DefIndex= new String[] {
              CommonTable.SqlDefIndex+"idokilokk on doki (lokk)",
              CommonTable.SqlDefIndex+"idokiaktiv on doki (aktiv)",
              CommonTable.SqlDefIndex+"idokicskl on doki (cskl)",
              CommonTable.SqlDefIndex+"idokivrdok on doki (vrdok)",
              CommonTable.SqlDefIndex+"idokibrdok on doki (brdok)",
              CommonTable.SqlDefIndex+"idokidatdok on doki (datdok)",
              CommonTable.SqlDefIndex+"idokicpar on doki (cpar)",
              CommonTable.SqlDefIndex+"idokipj on doki (pj)",
              CommonTable.SqlDefIndex+"idokicorg on doki (corg)",
              CommonTable.SqlDefIndex+"idokicnap on doki (cnap)",
              CommonTable.SqlDefUniqueIndex+"idokikey on doki (cskl,vrdok,god,brdok)"
               };

    NaziviIdx=new String[]{"idokilokk","idokiaktiv","idokicskl","idokivrdok","idokibrdok",
                           "idokidatdok","idokicpar","idokipj","idokicorg",
                           "idokicnap","idokikey" };

*/
  //}
  private void jbInit() throws Exception {
    initModule();
    /*dokiGOD.setCaption("Godina");
    dokiGOD.setColumnName("GOD");
    dokiGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiGOD.setPrecision(4);
    dokiGOD.setRowId(true);
    dokiGOD.setTableName("DOKI");
    dokiGOD.setSqlType(1);
    dokiGOD.setServerColumnName("GOD");

    dokiAKTIV.setCaption("Aktiv");
    dokiAKTIV.setColumnName("AKTIV");
    dokiAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiAKTIV.setDefault("D");
    dokiAKTIV.setPrecision(1);
    dokiAKTIV.setTableName("DOKI");
    dokiAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokiAKTIV.setSqlType(1);
    dokiAKTIV.setServerColumnName("AKTIV");

    dokiLOKK.setCaption("Lokk");
    dokiLOKK.setColumnName("LOKK");
    dokiLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiLOKK.setDefault("N");
    dokiLOKK.setPrecision(1);
    dokiLOKK.setTableName("DOKI");
    dokiLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokiLOKK.setSqlType(1);
    dokiLOKK.setServerColumnName("LOKK");

    dokiCSKL.setCaption("Skladište");
    dokiCSKL.setColumnName("CSKL");
    dokiCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCSKL.setPrecision(12);
    dokiCSKL.setRowId(true);
    dokiCSKL.setTableName("DOKI");
    dokiCSKL.setSqlType(1);
    dokiCSKL.setServerColumnName("CSKL");

    dokiVRDOK.setCaption("Vrsta");
    dokiVRDOK.setColumnName("VRDOK");
    dokiVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiVRDOK.setPrecision(3);
    dokiVRDOK.setRowId(true);
    dokiVRDOK.setTableName("DOKI");
    dokiVRDOK.setSqlType(1);
    dokiVRDOK.setWidth(4);
    dokiVRDOK.setServerColumnName("VRDOK");

    dokiCUSER.setCaption("Operater");
    dokiCUSER.setColumnName("CUSER");
    dokiCUSER.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCUSER.setPrecision(15);
    dokiCUSER.setTableName("DOKI");
    dokiCUSER.setServerColumnName("CUSER");
    dokiCUSER.setSqlType(1);

    dokiBRDOK.setCaption("Broj");
    dokiBRDOK.setColumnName("BRDOK");
    dokiBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    dokiBRDOK.setRowId(true);
    dokiBRDOK.setTableName("DOKI");
    dokiBRDOK.setWidth(3);
    dokiBRDOK.setSqlType(4);
    dokiBRDOK.setServerColumnName("BRDOK");

    dokiUI.setCaption("Ulaz - izlaz");
    dokiUI.setColumnName("UI");
    dokiUI.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiUI.setPrecision(1);
    dokiUI.setTableName("DOKI");
    dokiUI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokiUI.setSqlType(1);
    dokiUI.setServerColumnName("UI");
    dokiCSHRAB.setCaption("Shema popusta");
    dokiCSHRAB.setColumnName("CSHRAB");
    dokiCSHRAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCSHRAB.setPrecision(3);
    dokiCSHRAB.setTableName("DOKI");
    dokiCSHRAB.setSqlType(1);
    dokiCSHRAB.setServerColumnName("CSHRAB");
    dokiCSHZT.setCaption("Shema zavisnih troškova");
    dokiCSHZT.setColumnName("CSHZT");
    dokiCSHZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCSHZT.setPrecision(3);
    dokiCSHZT.setTableName("DOKI");
    dokiCSHZT.setSqlType(1);
    dokiCSHZT.setServerColumnName("CSHZT");
    dokiCNAP.setCaption("Napomena");
    dokiCNAP.setColumnName("CNAP");
    dokiCNAP.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCNAP.setPrecision(3);
    dokiCNAP.setTableName("DOKI");
    dokiCNAP.setSqlType(1);
    dokiCNAP.setServerColumnName("CNAP");
    dokiREZKOL.setCaption("REZKOL");
    dokiREZKOL.setColumnName("REZKOL");
    dokiREZKOL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiREZKOL.setDefault("N");
    dokiREZKOL.setPrecision(1);
    dokiREZKOL.setTableName("DOKI");
    dokiREZKOL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokiREZKOL.setSqlType(1);
    dokiREZKOL.setServerColumnName("REZKOL");

    dokiSTATIRA.setCaption("Obr");
    dokiSTATIRA.setColumnName("STATIRA");
    dokiSTATIRA.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiSTATIRA.setDefault("N");
    dokiSTATIRA.setPrecision(1);
    dokiSTATIRA.setTableName("DOKI");
//    dokiSTATIRA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokiSTATIRA.setSqlType(1);
    dokiSTATIRA.setServerColumnName("STATIRA");
    dokiSTATIRA.setWidth(0);
    dokiSTATPLA.setCaption("Pl");
    dokiSTATPLA.setColumnName("STATPLA");
    dokiSTATPLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiSTATPLA.setDefault("N");
    dokiSTATPLA.setPrecision(1);
    dokiSTATPLA.setTableName("DOKI");
//    dokiSTATPLA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokiSTATPLA.setSqlType(1);
    dokiSTATPLA.setServerColumnName("STATPLA");
    dokiSTATPLA.setWidth(0);
    dokiSTATKNJ.setAlignment(com.borland.dx.text.Alignment.CENTER | com.borland.dx.text.Alignment.MIDDLE);
//    dokiSTATKNJ.setCaption("Knjiženo");
    dokiSTATKNJ.setCaption("Knj");
    dokiSTATKNJ.setColumnName("STATKNJ");
    dokiSTATKNJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiSTATKNJ.setDefault("N");
    dokiSTATKNJ.setPrecision(1);
    dokiSTATKNJ.setTableName("DOKI");
//    dokiSTATKNJ.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokiSTATKNJ.setSqlType(1);
    dokiSTATKNJ.setServerColumnName("STATKNJ");
    dokiSTATKNJ.setWidth(0);
    dokiSTATUS.setCaption("Prn");
    dokiSTATUS.setColumnName("STATUS");
    dokiSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiSTATUS.setDefault("N");
    dokiSTATUS.setPrecision(1);
    dokiSTATUS.setTableName("DOKI");
//    dokiSTATUS.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokiSTATUS.setSqlType(1);
    dokiSTATUS.setServerColumnName("STATUS");
    dokiSTATUS.setWidth(0);
    
    dokiPROVISP.setCaption("Isplaæena provizija");
    dokiPROVISP.setColumnName("PROVISP");
    dokiPROVISP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokiPROVISP.setDisplayMask("###,###,##0.00");
    dokiPROVISP.setDefault("0");
    dokiPROVISP.setPrecision(15);
    dokiPROVISP.setScale(2);
    dokiPROVISP.setTableName("DOKI");    
    dokiPROVISP.setSqlType(2);
    dokiPROVISP.setServerColumnName("PROVISP");
    dokiPROVISP.setWidth(8);
        
    dokiBRNAL.setCaption("Broj naloga");
    dokiBRNAL.setColumnName("BRNAL");
    dokiBRNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiBRNAL.setPrecision(8);
    dokiBRNAL.setTableName("DOKI");
    dokiBRNAL.setSqlType(1);
    dokiBRNAL.setServerColumnName("BRNAL");
    dokiOZNVAL.setCaption("Oznaka valute");
    dokiOZNVAL.setColumnName("OZNVAL");
    dokiOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiOZNVAL.setPrecision(3);
    dokiOZNVAL.setTableName("DOKI");
    dokiOZNVAL.setSqlType(1);
    dokiOZNVAL.setServerColumnName("OZNVAL");
    dokiBRNARIZ.setCaption("Broj izlazne narudžbe");
    dokiBRNARIZ.setColumnName("BRNARIZ");
    dokiBRNARIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiBRNARIZ.setPrecision(40);
    dokiBRNARIZ.setTableName("DOKI");
    dokiBRNARIZ.setSqlType(1);
    dokiBRNARIZ.setServerColumnName("BRNARIZ");
    dokiBRPRD.setCaption("Broj predra\\u10duna");
    dokiBRPRD.setColumnName("BRPRD");
    dokiBRPRD.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiBRPRD.setPrecision(20);
    dokiBRPRD.setTableName("DOKI");
    dokiBRPRD.setSqlType(1);
    dokiBRPRD.setServerColumnName("BRPRD");
    dokiBRDOKIZ.setCaption("Broj izlaznog dokumenta");
    dokiBRDOKIZ.setColumnName("BRDOKIZ");
    dokiBRDOKIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiBRDOKIZ.setPrecision(20);
    dokiBRDOKIZ.setTableName("DOKI");
    dokiBRDOKIZ.setSqlType(1);
    dokiBRDOKIZ.setServerColumnName("BRDOKIZ");
    dokiDDOSP.setCaption("Dani dospije\u0107a");
    dokiDDOSP.setColumnName("DDOSP");
    dokiDDOSP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    dokiDDOSP.setDefault("0");
    dokiDDOSP.setTableName("DOKI");
    dokiDDOSP.setWidth(4);
    dokiDDOSP.setSqlType(5);
    dokiDDOSP.setServerColumnName("DDOSP");
    dokiCUG.setCaption("Ugovor");
    dokiCUG.setColumnName("CUG");
    dokiCUG.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCUG.setPrecision(20);
    dokiCUG.setTableName("DOKI");
    dokiCUG.setSqlType(1);
    dokiCUG.setServerColumnName("CUG");
    dokiCORG.setCaption("Organizacijska jedinica");
    dokiCORG.setColumnName("CORG");
    dokiCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCORG.setPrecision(12);
    dokiCORG.setTableName("DOKI");
    dokiCORG.setSqlType(1);
    dokiCORG.setServerColumnName("CORG");
    dokiCORG.setWidth(5);

    dokiDATPON.setCaption("Datum ponude");
    dokiDATPON.setColumnName("DATPON");
    dokiDATPON.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiDATPON.setDisplayMask("dd-MM-yyyy");
//    dokiDATPON.setEditMask("dd-MM-yyyy");
    dokiDATPON.setTableName("DOKI");
    dokiDATPON.setWidth(10);
    dokiDATPON.setSqlType(93);
    dokiDATPON.setServerColumnName("DATPON");

    dokiBRPON.setCaption("Broj ponude");
    dokiBRPON.setColumnName("BRPON");
    dokiBRPON.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiBRPON.setPrecision(20);
    dokiBRPON.setTableName("DOKI");
    dokiBRPON.setSqlType(1);
    dokiBRPON.setServerColumnName("BRPON");

    dokiCKUPAC.setCaption("Kupac");
    dokiCKUPAC.setColumnName("CKUPAC");
    dokiCKUPAC.setDataType(com.borland.dx.dataset.Variant.INT);
    dokiCKUPAC.setTableName("DOKI");
    dokiCKUPAC.setWidth(5);
    dokiCKUPAC.setServerColumnName("CKUPAC");
    dokiCKUPAC.setSqlType(4);
    dokiCAGENT.setCaption("Agent");
    dokiCAGENT.setColumnName("CAGENT");
    dokiCAGENT.setDataType(com.borland.dx.dataset.Variant.INT);
    dokiCAGENT.setTableName("DOKI");
    dokiCAGENT.setWidth(5);
    dokiCAGENT.setServerColumnName("CAGENT");
    dokiCAGENT.setSqlType(4);
    dokiPJ.setCaption("Poslovna jedinica");
    dokiPJ.setColumnName("PJ");
    dokiPJ.setDataType(com.borland.dx.dataset.Variant.INT);
    dokiPJ.setTableName("DOKI");
    dokiPJ.setSqlType(4);
    dokiPJ.setServerColumnName("PJ");
    dokiCPAR.setCaption("Partner");
    dokiCPAR.setColumnName("CPAR");
    dokiCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    dokiCPAR.setTableName("DOKI");
    dokiCPAR.setWidth(4);
    dokiCPAR.setSqlType(4);
    dokiCPAR.setServerColumnName("CPAR");

    dokiSYSDAT.setCaption("Datum");
    dokiSYSDAT.setColumnName("SYSDAT");
    dokiSYSDAT.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiSYSDAT.setDisplayMask("dd-MM-yyyy");
    dokiSYSDAT.setEditMask("dd-MM-yyyy");
    dokiSYSDAT.setTableName("DOKI");
    dokiSYSDAT.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokiSYSDAT.setWidth(5);
    dokiSYSDAT.setSqlType(93);
    dokiSYSDAT.setServerColumnName("SYSDAT");

    dokiCVRTR.setCaption("Vrsta troška");
    dokiCVRTR.setColumnName("CVRTR");
    dokiCVRTR.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCVRTR.setPrecision(12);
    dokiCVRTR.setTableName("DOKI");
    dokiCVRTR.setSqlType(1);
    dokiCVRTR.setServerColumnName("CVRTR");
    dokiCVRTR.setWidth(5);

    dokiDATRADNAL.setCaption("Datum radnog naloga");
    dokiDATRADNAL.setColumnName("DATRADNAL");
    dokiDATRADNAL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiDATRADNAL.setDisplayMask("dd-MM-yyyy");
//    dokiDATRADNAL.setEditMask("dd-MM-yyyy");
    dokiDATRADNAL.setTableName("DOKI");
    dokiDATRADNAL.setWidth(10);
    dokiDATRADNAL.setSqlType(93);
    dokiDATRADNAL.setServerColumnName("DATRADNAL");
    dokiCRADNAL.setCaption("Broj radnog naloga");
    dokiCRADNAL.setColumnName("CRADNAL");
    dokiCRADNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCRADNAL.setPrecision(20);
    dokiCRADNAL.setTableName("DOKI");
    dokiCRADNAL.setSqlType(1);
    dokiCRADNAL.setServerColumnName("CRADNAL");
    dokiCRADNAL.setWidth(8);
    dokiTECAJ.setCaption("Te\u010Daj");
    dokiTECAJ.setColumnName("TECAJ");
    dokiTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokiTECAJ.setDisplayMask("###,###,##0.000000");
    dokiTECAJ.setDefault("0");
    dokiTECAJ.setPrecision(15);
    dokiTECAJ.setScale(6);
    dokiTECAJ.setTableName("DOKI");
    dokiTECAJ.setSqlType(2);
    dokiTECAJ.setServerColumnName("TECAJ");
    dokiUPRAB.setCaption("Posto popusta");
    dokiUPRAB.setColumnName("UPRAB");
    dokiUPRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokiUPRAB.setDisplayMask("###,###,##0.00");
    dokiUPRAB.setDefault("0");
    dokiUPRAB.setPrecision(10);
    dokiUPRAB.setScale(2);
    dokiUPRAB.setTableName("DOKI");
    dokiUPRAB.setSqlType(2);
    dokiUPRAB.setServerColumnName("UPRAB");
    dokiUPZT.setCaption("Zavisni troškovi");
    dokiUPZT.setColumnName("UPZT");
    dokiUPZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokiUPZT.setDisplayMask("###,###,##0.00");
    dokiUPZT.setDefault("0");
    dokiUPZT.setPrecision(10);
    dokiUPZT.setScale(2);
    dokiUPZT.setTableName("DOKI");
    dokiUPZT.setSqlType(2);
    dokiUPZT.setServerColumnName("UPZT");
    dokiDATKNJ.setCaption("Datum knjiženja");
    dokiDATKNJ.setColumnName("DATKNJ");
    dokiDATKNJ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiDATKNJ.setDisplayMask("dd-MM-yyyy");
//    dokiDATKNJ.setEditMask("dd-MM-yyyy");
    dokiDATKNJ.setTableName("DOKI");
    dokiDATKNJ.setWidth(10);
    dokiDATKNJ.setSqlType(93);
    dokiDATKNJ.setServerColumnName("DATKNJ");
    dokiDATNARIZ.setCaption("Datum izlazne narudžbe");
    dokiDATNARIZ.setColumnName("DATNARIZ");
    dokiDATNARIZ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiDATNARIZ.setDisplayMask("dd-MM-yyyy");
//    dokiDATNARIZ.setEditMask("dd-MM-yyyy");
    dokiDATNARIZ.setTableName("DOKI");
    dokiDATNARIZ.setWidth(10);
    dokiDATNARIZ.setSqlType(93);
    dokiDATNARIZ.setServerColumnName("DATNARIZ");
    dokiDATPRD.setCaption("Datum predra\u010Duna");
    dokiDATPRD.setColumnName("DATPRD");
    dokiDATPRD.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiDATPRD.setDisplayMask("dd-MM-yyyy");
//    dokiDATPRD.setEditMask("dd-MM-yyyy");
    dokiDATPRD.setTableName("DOKI");
    dokiDATPRD.setWidth(10);
    dokiDATPRD.setSqlType(93);
    dokiDATPRD.setServerColumnName("DATPRD");
    dokiDATDOKIZ.setCaption("Datum izlaznog dokumenta");
    dokiDATDOKIZ.setColumnName("DATDOKIZ");
    dokiDATDOKIZ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiDATDOKIZ.setDisplayMask("dd-MM-yyyy");
//    dokiDATDOKIZ.setEditMask("dd-MM-yyyy");
    dokiDATDOKIZ.setTableName("DOKI");
    dokiDATDOKIZ.setWidth(10);
    dokiDATDOKIZ.setSqlType(93);
    dokiDATDOKIZ.setServerColumnName("DATDOKIZ");
    dokiDATDOSP.setCaption("Datum dospije\\u107a");
    dokiDATDOSP.setColumnName("DATDOSP");
    dokiDATDOSP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiDATDOSP.setDisplayMask("dd-MM-yyyy");
//    dokiDATDOSP.setEditMask("dd-MM-yyyy");
    dokiDATDOSP.setTableName("DOKI");
    dokiDATDOSP.setWidth(10);
    dokiDATDOSP.setSqlType(93);
    dokiDATDOSP.setServerColumnName("DATDOSP");
    dokiDVO.setCaption("Datum DVO");
    dokiDVO.setColumnName("DVO");
    dokiDVO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiDVO.setDisplayMask("dd-MM-yyyy");
//    dokiDVO.setEditMask("dd-MM-yyyy");
    dokiDVO.setTableName("DOKI");
    dokiDVO.setWidth(10);
    dokiDVO.setSqlType(93);
    dokiDVO.setServerColumnName("DVO");
    dokiDATUG.setCaption("Datum ugovora");
    dokiDATUG.setColumnName("DATUG");
    dokiDATUG.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiDATUG.setDisplayMask("dd-MM-yyyy");
//    dokiDATUG.setEditMask("dd-MM-yyyy");
    dokiDATUG.setTableName("DOKI");
    dokiDATUG.setWidth(10);
    dokiDATUG.setSqlType(93);
    dokiDATUG.setServerColumnName("DATUG");

    dokiDATDOK.setCaption("Datum");
    dokiDATDOK.setColumnName("DATDOK");
    dokiDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiDATDOK.setDisplayMask("dd-MM-yyyy");
//    dokiDATDOK.setEditMask("dd-MM-yyyy");
    dokiDATDOK.setTableName("DOKI");
    dokiDATDOK.setWidth(12);
    dokiDATDOK.setSqlType(93);
    dokiDATDOK.setServerColumnName("DATDOK");
    dokiCNAC.setCaption("Na\u010Din otpreme");
    dokiCNAC.setColumnName("CNAC");
    dokiCNAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCNAC.setPrecision(3);
    dokiCNAC.setTableName("DOKI");
    dokiCNAC.setSqlType(1);
    dokiCNAC.setServerColumnName("CNAC");
    dokiCNAMJ.setCaption("Namjena robe");
    dokiCNAMJ.setColumnName("CNAMJ");
    dokiCNAMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCNAMJ.setPrecision(3);
    dokiCNAMJ.setTableName("DOKI");
    dokiCNAMJ.setSqlType(1);
    dokiCNAMJ.setServerColumnName("CNAMJ");
    dokiCNACPL.setCaption("Na\\u10din pla\u0107anja");
    dokiCNACPL.setColumnName("CNACPL");
    dokiCNACPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCNACPL.setPrecision(3);
    dokiCNACPL.setTableName("DOKI");
    dokiCNACPL.setSqlType(1);
    dokiCNACPL.setServerColumnName("CNACPL");
    dokiCFRA.setCaption("Paritet");
    dokiCFRA.setColumnName("CFRA");
    dokiCFRA.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCFRA.setPrecision(3);
    dokiCFRA.setTableName("DOKI");
    dokiCFRA.setSqlType(1);
    dokiCFRA.setServerColumnName("CFRA");

    dokiUIRAC.setCaption("Iznos");
    dokiUIRAC.setColumnName("UIRAC");
    dokiUIRAC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokiUIRAC.setDisplayMask("###,###,##0.00");
    dokiUIRAC.setDefault("0");
    dokiUIRAC.setPrecision(15);
    dokiUIRAC.setScale(2);
    dokiUIRAC.setTableName("DOKI");
    dokiUIRAC.setSqlType(2);
    dokiUIRAC.setServerColumnName("UIRAC");
    dokiUIRAC.setWidth(8);

    dokiOPIS.setCaption("Opis");
    dokiOPIS.setColumnName("OPIS");
    dokiOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiOPIS.setPrecision(200);
    dokiOPIS.setTableName("DOKI");
    dokiOPIS.setSqlType(1);
    dokiOPIS.setServerColumnName("OPIS");

    dokiBRRATA.setCaption("Broj rata");
    dokiBRRATA.setColumnName("BRRATA");
    dokiBRRATA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    dokiBRRATA.setDefault("1");
    dokiBRRATA.setTableName("DOKI");
    dokiBRRATA.setWidth(4);
    dokiBRRATA.setSqlType(5);
    dokiBRRATA.setServerColumnName("BRRATA");

    dokiPLATITI.setCaption("Uplata");
    dokiPLATITI.setColumnName("PLATITI");
    dokiPLATITI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokiPLATITI.setDisplayMask("###,###,##0.00");
    dokiPLATITI.setDefault("0");
    dokiPLATITI.setPrecision(15);
    dokiPLATITI.setScale(2);
    dokiPLATITI.setTableName("DOKI");
    dokiPLATITI.setSqlType(2);
    dokiPLATITI.setServerColumnName("PLATITI");

    dokiUPPOPUST.setCaption("Posto popusta");
    dokiUPPOPUST.setColumnName("UPPOPUST");
    dokiUPPOPUST.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokiUPPOPUST.setDisplayMask("###,###,##0.00");
    dokiUPPOPUST.setDefault("0");
    dokiUPPOPUST.setPrecision(6);
    dokiUPPOPUST.setScale(2);
    dokiUPPOPUST.setTableName("DOKI");
    dokiUPPOPUST.setSqlType(2);
    dokiUPPOPUST.setServerColumnName("UPPOPUST");

    dokiUIPOPUST.setCaption("Iznos popusta");
    dokiUIPOPUST.setColumnName("UIPOPUST");
    dokiUIPOPUST.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokiUIPOPUST.setDisplayMask("###,###,##0.00");
    dokiUIPOPUST.setDefault("0");
    dokiUIPOPUST.setPrecision(15);
    dokiUIPOPUST.setScale(2);
    dokiUIPOPUST.setTableName("DOKI");
    dokiUIPOPUST.setSqlType(2);
    dokiUIPOPUST.setServerColumnName("UIPOPUST");

    dokiCRADNIK.setCaption("Radnik");
    dokiCRADNIK.setColumnName("CRADNIK");
    dokiCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCRADNIK.setPrecision(6);
    dokiCRADNIK.setTableName("DOKI");
    dokiCRADNIK.setServerColumnName("CRADNIK");
    dokiCRADNIK.setSqlType(1);

    dokiZIRO.setCaption("Žiro ra\u010Dun");
    dokiZIRO.setColumnName("ZIRO");
    dokiZIRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiZIRO.setPrecision(40);
    dokiZIRO.setTableName("DOKI");
    dokiZIRO.setServerColumnName("ZIRO");
    dokiZIRO.setSqlType(1);

    dokiPNBZ2.setCaption("Poziv na broj (zaduž.) 2");
    dokiPNBZ2.setColumnName("PNBZ2");
    dokiPNBZ2.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiPNBZ2.setPrecision(30);
    dokiPNBZ2.setTableName("DOKI");
    dokiPNBZ2.setServerColumnName("PNBZ2");
    dokiPNBZ2.setSqlType(1);

    dokiCKO.setCaption("Kontakt");
    dokiCKO.setColumnName("CKO");
    dokiCKO.setDataType(com.borland.dx.dataset.Variant.INT);
    dokiCKO.setTableName("DOKI");
    dokiCKO.setWidth(5);
    dokiCKO.setSqlType(4);
    dokiCKO.setServerColumnName("CKO");

    dokiPARAM.setCaption("Parametri");
    dokiPARAM.setColumnName("PARAM");
    dokiPARAM.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiPARAM.setPrecision(8);
    dokiPARAM.setTableName("DOKI");
    dokiPARAM.setServerColumnName("PARAM");
    dokiPARAM.setSqlType(1);

    dokiSTAT_KPR.setCaption("Stat KPR");
    dokiSTAT_KPR.setColumnName("STAT_KPR");
    dokiSTAT_KPR.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiSTAT_KPR.setPrecision(1);
    dokiSTAT_KPR.setDefault("N");
    dokiSTAT_KPR.setTableName("DOKI");
    dokiSTAT_KPR.setServerColumnName("STAT_KPR");
    dokiSTAT_KPR.setSqlType(1);

    dokiDATUPL.setCaption("Datum uplate");
    dokiDATUPL.setColumnName("DATUPL");
    dokiDATUPL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokiDATUPL.setDisplayMask("dd-MM-yyyy");
//    dokiDATUG.setEditMask("dd-MM-yyyy");
    dokiDATUPL.setTableName("DOKI");
    dokiDATUPL.setWidth(10);
    dokiDATUPL.setSqlType(93);
    dokiDATUPL.setServerColumnName("DATUPL");

    dokiCBLAG.setCaption("Blagajna");
    dokiCBLAG.setColumnName("CBLAG");
    dokiCBLAG.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokiCBLAG.setPrecision(6);
    dokiCBLAG.setTableName("DOKI");
    dokiCBLAG.setSqlType(1);
    dokiCBLAG.setServerColumnName("CBLAG");
    
    dokiPROVPOST.setCaption("Postotak naknade");
    dokiPROVPOST.setColumnName("PROVPOST");
    dokiPROVPOST.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokiPROVPOST.setDisplayMask("###,###,##0.00");
    dokiPROVPOST.setDefault("0");
    dokiPROVPOST.setPrecision(6);
    dokiPROVPOST.setScale(2);
    dokiPROVPOST.setTableName("DOKI");
    dokiPROVPOST.setSqlType(2);
    dokiPROVPOST.setServerColumnName("PROVPOST");

    doki.setResolver(dm.getQresolver());
    doki.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from doki", null, true, Load.ALL));
 setColumns(new Column[] {dokiLOKK, dokiAKTIV, dokiCUSER, dokiCSKL, dokiVRDOK, dokiGOD, dokiBRDOK, dokiUI, dokiSYSDAT, dokiDATDOK, dokiCPAR, dokiPJ,
        dokiCAGENT, dokiCKUPAC, dokiBRPON, dokiDATPON, dokiCORG, dokiCVRTR, dokiCUG, dokiDATUG, dokiDVO, dokiDDOSP, dokiDATDOSP, dokiBRDOKIZ, dokiDATDOKIZ, dokiBRPRD, dokiDATPRD, dokiBRNARIZ,
        dokiDATNARIZ, dokiOZNVAL, dokiTECAJ, dokiBRNAL, dokiDATKNJ, dokiCRADNAL, dokiDATRADNAL, dokiSTATUS, dokiSTATKNJ, dokiSTATPLA, dokiSTATIRA,
        dokiREZKOL, dokiCFRA, dokiCNACPL, dokiCNAMJ, dokiCNAC, dokiCNAP, dokiUPZT, dokiCSHZT, dokiUPRAB, dokiCSHRAB,dokiUIRAC, dokiOPIS,
        dokiBRRATA, dokiPLATITI, dokiUPPOPUST, dokiUIPOPUST, dokiCRADNIK, dokiZIRO, dokiPNBZ2, dokiCKO, dokiPARAM, dokiSTAT_KPR,dokiDATUPL,
        dokiCBLAG, dokiPROVISP, dokiPROVPOST
  });*/

    initClones();
  }

  private void initClones() {
    createFilteredDataSet(zagPOS, "1=0");
    createFilteredDataSet(zagGOT, "1=0");
    createFilteredDataSet(zagPON, "1=0");
    createFilteredDataSet(zagPONPar, "1=0");
    createFilteredDataSet(zagPONOJ, "1=0");
    createFilteredDataSet(zagPONKup, "1=0");
    createFilteredDataSet(zagPOD, "1=0");
    createFilteredDataSet(zagPODKup, "1=0");
    createFilteredDataSet(zagPOV, "1=0");
    createFilteredDataSet(zagNAR, "1=0");
    createFilteredDataSet(zagPRD, "1=0");
    createFilteredDataSet(zagPRDkup, "1=0");
    createFilteredDataSet(zagRAC, "1=0");
    createFilteredDataSet(zagROT, "1=0");
    createFilteredDataSet(zagOTP, "1=0");
    createFilteredDataSet(zagIZD, "1=0");
    createFilteredDataSet(zagPOD, "1=0");
    createFilteredDataSet(zagTER, "1=0");
    createFilteredDataSet(zagODB, "1=0");
    createFilteredDataSet(zagGRN, "1=0");
    createFilteredDataSet(zagREV, "1=0");
    createFilteredDataSet(zagPRE, "1=0");
    createFilteredDataSet(zagNDO, "1=0");
    createFilteredDataSet(zagINM, "1=0");
    createFilteredDataSet(zagDOS, "1=0");
    createFilteredDataSet(zagKON, "1=0");
    createFilteredDataSet(zagZAH, "1=0");
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return doki;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPos() {
    return zagPOS;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagInm() {
    return zagINM;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagGot() {
    return zagGOT;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPon() {
    return zagPON;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagPonKup() {
    return zagPONKup;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagPonOJ() {
    return zagPONOJ;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagPonPar() {
    return zagPONPar;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagPov() {
    return zagPOV;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagNar() {
    return zagNAR;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPrd() {
    return zagPRD;
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getZagPrdKup() {
    return zagPRDkup;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagRac() {
    return zagRAC;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagRot() {
    return zagROT;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagOtp() {
    return zagOTP;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagDos() {
    return zagDOS;
  }
  

  public com.borland.dx.sql.dataset.QueryDataSet getZagIzd() {
    return zagIZD;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagOdb() {
    return zagODB;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagTer() {
    return zagTER;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPod() {
    return zagPOD;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPodKup() {
    return zagPODKup;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getZagGrn() {
    return zagGRN;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagRev() {
    return zagREV;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagPre() {
    return zagPRE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getZagNdo() {
    return zagNDO;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagKon() {
	    return zagKON;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getZagZah() {
     return zagKON;
  }
}
