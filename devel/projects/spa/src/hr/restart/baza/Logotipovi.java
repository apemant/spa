/****license*****************************************************************
**   file: Logotipovi.java
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

public class Logotipovi extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Logotipovi Logotipoviclass;
  dM dm  = dM.getDataModule();
  QueryDataSet Logotipovi = new raDataSet();
  QueryDataSet Logotipoviaktiv = new raDataSet();
//  Column logoLOKK = new Column();
//  Column logoACTIV = new Column();
//  Column logoCORG = new Column();
//  Column logoNAZIVLOG = new Column();
//  Column logoMJESTO = new Column();
//  Column logoPBR = new Column();
//  Column logoADRESA = new Column();
//  Column logoZIRO = new Column();
//  Column logoMATBROJ = new Column();
//  Column logoSIFDJEL = new Column();
//  Column logoPORISP = new Column();
//  Column logoFAX = new Column();
//  Column logoTEL1 = new Column();
//  Column logoTEL2 = new Column();
//  Column logoEMAIL = new Column();
//  Column logoURL = new Column();
//  Column logoIDIH = new Column();
//  Column logoIDIF = new Column();
//  Column logoTOPM = new Column();
//  Column logoBOTM = new Column();

  public static Logotipovi getDataModule() {
    if (Logotipoviclass == null) {
      Logotipoviclass = new Logotipovi();
    }
    return Logotipoviclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return Logotipovi;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return Logotipoviaktiv;
  }

  public Logotipovi() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
  /*  logoBOTM.setCaption("Donja margina");
    logoBOTM.setColumnName("BOTTOMMARGIN");
    logoBOTM.setDataType(com.borland.dx.dataset.Variant.INT);
    logoBOTM.setPrecision(6);
    logoBOTM.setDefault("0");
    logoBOTM.setTableName("LOGOTIPOVI");
    logoBOTM.setSqlType(4);
    logoBOTM.setServerColumnName("BOTTOMMARGIN");

    logoTOPM.setCaption("Gornja margina");
    logoTOPM.setColumnName("TOPMARGIN");
    logoTOPM.setDataType(com.borland.dx.dataset.Variant.INT);
    logoTOPM.setPrecision(6);
    logoTOPM.setDefault("0");
    logoTOPM.setTableName("LOGOTIPOVI");
    logoTOPM.setSqlType(4);
    logoTOPM.setServerColumnName("TOPMARGIN");

    logoFOOTER2.setCaption("Footer linija 2");
    logoFOOTER2.setColumnName("FOOTER2");
    logoFOOTER2.setDataType(com.borland.dx.dataset.Variant.STRING);
    logoFOOTER2.setPrecision(80);
    logoFOOTER2.setTableName("LOGOTIPOVI");
    logoFOOTER2.setSqlType(1);
    logoFOOTER2.setServerColumnName("FOOTER2");

    logoFOOTER1.setCaption("Footer linija 1");
    logoFOOTER1.setColumnName("FOOTER1");
    logoFOOTER1.setDataType(com.borland.dx.dataset.Variant.STRING);
    logoFOOTER1.setPrecision(80);
    logoFOOTER1.setTableName("LOGOTIPOVI");
    logoFOOTER1.setSqlType(1);
    logoFOOTER1.setServerColumnName("FOOTER1"); */

//    logoIDIH.setCaption("Zaglavlje logotipa izlaznih dokumenata");
//    logoIDIH.setColumnName("IDIH");
//    logoIDIH.setDataType(com.borland.dx.dataset.Variant.INT);
//    logoIDIH.setSqlType(4);
//    logoIDIH.setTableName("LOGOTIPOVI");
//    logoIDIH.setServerColumnName("IDIH");
//
//    logoIDIF.setCaption("Podnožje logotipa izlaznih dokumenata");
//    logoIDIF.setColumnName("IDIF");
//    logoIDIF.setDataType(com.borland.dx.dataset.Variant.INT);
//    logoIDIF.setSqlType(4);
//    logoIDIF.setTableName("LOGOTIPOVI");
//    logoIDIF.setServerColumnName("IDIF");
//
//    logoTEL2.setCaption("Telefon 2");
//    logoTEL2.setColumnName("TEL2");
//    logoTEL2.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoTEL2.setPrecision(20);
//    logoTEL2.setTableName("LOGOTIPOVI");
//    logoTEL2.setSqlType(1);
//    logoTEL2.setServerColumnName("TEL2");
//    logoTEL1.setCaption("Telefon 1");
//    logoTEL1.setColumnName("TEL1");
//    logoTEL1.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoTEL1.setPrecision(20);
//    logoTEL1.setTableName("LOGOTIPOVI");
//    logoTEL1.setSqlType(1);
//    logoTEL1.setServerColumnName("TEL1");
//    logoFAX.setCaption("Fax");
//    logoFAX.setColumnName("FAX");
//    logoFAX.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoFAX.setPrecision(20);
//    logoFAX.setTableName("LOGOTIPOVI");
//    logoFAX.setSqlType(1);
//    logoFAX.setServerColumnName("FAX");
//    logoPORISP.setCaption("Porezna ispostava");
//    logoPORISP.setColumnName("PORISP");
//    logoPORISP.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoPORISP.setPrecision(50);
//    logoPORISP.setTableName("LOGOTIPOVI");
//    logoPORISP.setSqlType(1);
//    logoPORISP.setServerColumnName("PORISP");
//    logoSIFDJEL.setCaption("Šifra djelatnosti");
//    logoSIFDJEL.setColumnName("SIFDJEL");
//    logoSIFDJEL.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoSIFDJEL.setPrecision(20);
//    logoSIFDJEL.setTableName("LOGOTIPOVI");
//    logoSIFDJEL.setSqlType(1);
//    logoSIFDJEL.setServerColumnName("SIFDJEL");
//    logoMATBROJ.setCaption("Mati\u010Dni broj");
//    logoMATBROJ.setColumnName("MATBROJ");
//    logoMATBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoMATBROJ.setPrecision(13);
//    logoMATBROJ.setTableName("LOGOTIPOVI");
//    logoMATBROJ.setSqlType(1);
//    logoMATBROJ.setServerColumnName("MATBROJ");
//    logoZIRO.setCaption("Žiro ra\u010Dun");
//    logoZIRO.setColumnName("ZIRO");
//    logoZIRO.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoZIRO.setPrecision(40);
//    logoZIRO.setTableName("LOGOTIPOVI");
//    logoZIRO.setSqlType(1);
//    logoZIRO.setServerColumnName("ZIRO");
//    logoADRESA.setCaption("Adresa");
//    logoADRESA.setColumnName("ADRESA");
//    logoADRESA.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoADRESA.setPrecision(50);
//    logoADRESA.setTableName("LOGOTIPOVI");
//    logoADRESA.setSqlType(1);
//    logoADRESA.setServerColumnName("ADRESA");
//    logoPBR.setCaption("Poštanski broj");
//    logoPBR.setColumnName("PBR");
//    logoPBR.setDataType(com.borland.dx.dataset.Variant.INT);
//    logoPBR.setTableName("LOGOTIPOVI");
//    logoPBR.setSqlType(4);
//    logoPBR.setServerColumnName("PBR");
//    logoMJESTO.setCaption("Mjesto");
//    logoMJESTO.setColumnName("MJESTO");
//    logoMJESTO.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoMJESTO.setPrecision(30);
//    logoMJESTO.setTableName("LOGOTIPOVI");
//    logoMJESTO.setSqlType(1);
//    logoMJESTO.setServerColumnName("MJESTO");
//    logoNAZIVLOG.setCaption("Naziv");
//    logoNAZIVLOG.setColumnName("NAZIVLOG");
//    logoNAZIVLOG.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoNAZIVLOG.setPrecision(50);
//    logoNAZIVLOG.setTableName("LOGOTIPOVI");
//    logoNAZIVLOG.setSqlType(1);
//    logoNAZIVLOG.setServerColumnName("NAZIVLOG");
//    logoCORG.setCaption("Organizacijska jedinica");
//    logoCORG.setColumnName("CORG");
//    logoCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoCORG.setPrecision(12);
//    logoCORG.setRowId(true);
//    logoCORG.setTableName("LOGOTIPOVI");
//    logoCORG.setSqlType(1);
//    logoCORG.setServerColumnName("CORG");
//    logoACTIV.setColumnName("AKTIV");
//    logoACTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoACTIV.setDefault("D");
//    logoACTIV.setPrecision(1);
//    logoACTIV.setTableName("LOGOTIPOVI");
//    logoACTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    logoACTIV.setSqlType(1);
//    logoACTIV.setServerColumnName("AKTIV");
//    logoLOKK.setColumnName("LOKK");
//    logoLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoLOKK.setDefault("N");
//    logoLOKK.setPrecision(1);
//    logoLOKK.setTableName("LOGOTIPOVI");
//    logoLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    logoLOKK.setSqlType(1);
//    logoLOKK.setServerColumnName("LOKK");
////
//    logoEMAIL.setCaption("e-mail");
//    logoEMAIL.setColumnName("EMAIL");
//    logoEMAIL.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoEMAIL.setPrecision(30);
//    logoEMAIL.setTableName("LOGOTIPOVI");
//    logoEMAIL.setSqlType(1);
//    logoEMAIL.setServerColumnName("EMAIL");
//
//    logoURL.setCaption("URL");
//    logoURL.setColumnName("URL");
//    logoURL.setDataType(com.borland.dx.dataset.Variant.STRING);
//    logoURL.setPrecision(30);
//    logoURL.setTableName("LOGOTIPOVI");
//    logoURL.setSqlType(1);
//    logoURL.setServerColumnName("URL");
////
//    Logotipovi.setResolver(dm.getQresolver());
//    Logotipovi.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from logotipovi", null, true, Load.ALL));
// setColumns(new Column[] {logoLOKK, logoACTIV, logoCORG, logoNAZIVLOG, logoMJESTO, logoPBR, logoADRESA, logoZIRO, logoMATBROJ, logoSIFDJEL, logoPORISP,
//        logoFAX, logoTEL1, logoTEL2, logoEMAIL, logoURL, logoIDIH, logoIDIF});

    initModule();
    createFilteredDataSet(Logotipoviaktiv, "aktiv = 'D'");
  }

// public void setall(){
//
//    /*SqlDefTabela = "create table Logotipovi " +
//      "(lokk char(1) CHARACTER SET WIN1250 default 'N'," + // status zauzetosti
//      "aktiv char(1) CHARACTER SET WIN1250 default 'D'," + //da li se pokazuje u reportima ili je memorandum
//      "corg CHAR(12) CHARACTER SET WIN1250 NOT NULL," + //Šifra knjigovodstva za logotip
//      "nazivlog char(50) CHARACTER SET WIN1250," +
//      "mjesto char(30) CHARACTER SET WIN1250 , " + //Mjesto
//      "pbr numeric(5,0), "+ //PT Broj
//      "adresa char(50) CHARACTER SET WIN1250," +//Adresa
//      "ziro char(40) CHARACTER SET WIN1250,"+
//      "matbroj char(13) CHARACTER SET WIN1250,"+
//      "sifdjel char(20) CHARACTER SET WIN1250,"+
//      "porisp char(50) CHARACTER SET WIN1250,"+      // naziv nadležne por. ispostave
//      "fax char(20) CHARACTER SET WIN1250,"+
//      "tel1 char(20) CHARACTER SET WIN1250,"+
//      "tel2 char(20) CHARACTER SET WIN1250,"+
//      "email char(30) CHARACTER SET WIN1250,"+
//      "url char(30) CHARACTER SET WIN1250,"+
//      "Primary Key (corg))" ;
//*/
//    ddl.create("logotipovi")
//       .addChar("lokk", 1, "N")
//       .addChar("aktiv", 1, "D")
//       .addChar("corg", 12, true)
//       .addChar("nazivlog", 50)
//       .addChar("mjesto", 30)
//       .addInteger("pbr", 5)
//       .addChar("adresa", 50)
//       .addChar("ziro", 40)
//       .addChar("matbroj", 13)
//       .addChar("sifdjel", 20)
//       .addChar("porisp", 50)
//       .addChar("fax", 20)
//       .addChar("tel1", 20)
//       .addChar("tel2", 20)
//       .addChar("email", 30)
//       .addChar("url", 30)
//       .addInteger("idih", 6)
//       .addInteger("idif", 6)
//       .addPrimaryKey("corg");
//
//    Naziv="Logotipovi";
//
//    SqlDefTabela = ddl.getCreateTableString();
//
//    String[] idx = new String[] {};
//    String[] uidx = new String[] {};
//    DefIndex = ddl.getIndices(idx, uidx);
//    NaziviIdx = ddl.getIndexNames(idx, uidx);
//
///*
//    NaziviIdx=new String[]{"ilokklogotipovi","iaktivlogotipovi","icorglogotipovi"};
//
//    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Logotipovi (lokk)" ,
//                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Logotipovi (aktiv)" ,
//                            CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Logotipovi (corg)"} ;
//                            */
//  }
}