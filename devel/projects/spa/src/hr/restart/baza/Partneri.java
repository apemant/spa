/****license*****************************************************************
**   file: Partneri.java
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

public class Partneri extends KreirDrop implements DataModule {

  private static Partneri Partnericlass;
//  dM dm  = dM.getDataModule();
  QueryDataSet partneri = new raDataSet();
  QueryDataSet partneriaktiv = new raDataSet();
  QueryDataSet partnerikup = new raDataSet();
  QueryDataSet partneridob = new raDataSet();
  QueryDataSet partnerioboje = new raDataSet();

/*  Column parLOKK = new Column();
  Column parAKTIV = new Column();
  Column parCPAR = new Column();
  Column parNAZPAR = new Column();
  Column parMJ = new Column();
  Column parADRESA = new Column();
  Column parPBR = new Column();
  Column parMB = new Column();
  Column parCDJEL = new Column();
  Column parZR = new Column();
  Column parTEL = new Column();
  Column parTELFAX = new Column();
  Column parEMADR = new Column();
  Column parULOGA = new Column();
  Column parDI = new Column();
  Column parDOSP = new Column();
  Column parKO = new Column();
  Column parCGRPAR = new Column();
  Column parCAGENT = new Column();
  Column parPRAB = new Column();
  Column parSTATUS = new Column();
  Column parLIMKRED = new Column();
  Column parUGOVOR = new Column();
  Column parCZUP = new Column();
  Column parCMJESTA = new Column();
  Column parCTEL = new Column(); */


  public static Partneri getDataModule() {
    if (Partnericlass == null) {
      Partnericlass = new Partneri();
    }
    return Partnericlass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return partneri;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return partneriaktiv;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKup() {
    return partnerikup;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDob() {
    return partneridob;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getOboje() {
    return partnerioboje;
  }

  public Partneri() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    initModule();
    /*
    parLIMKRED.setCaption("Limit");
    parLIMKRED.setColumnName("LIMKRED");
    parLIMKRED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    parLIMKRED.setDisplayMask("###,###,##0.00");
    parLIMKRED.setDefault("0");
    parLIMKRED.setPrecision(15);
    parLIMKRED.setScale(2);
    parLIMKRED.setTableName("PARTNERI");
    parLIMKRED.setServerColumnName("LIMKRED");
    parLIMKRED.setSqlType(2);

    parSTATUS.setCaption("Status");
    parSTATUS.setColumnName("STATUS");
    parSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    parSTATUS.setDefault("A");
    parSTATUS.setPrecision(1);
    parSTATUS.setTableName("PARTNERI");
    parSTATUS.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    parSTATUS.setServerColumnName("STATUS");
    parSTATUS.setSqlType(1);

    parUGOVOR.setCaption("Ugovor");
    parUGOVOR.setColumnName("UGOVOR");
    parUGOVOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    parUGOVOR.setPrecision(20);
    parUGOVOR.setTableName("PARTNERI");
    parUGOVOR.setServerColumnName("UGOVOR");
    parUGOVOR.setSqlType(1);

    parPRAB.setCaption("Popust");
    parPRAB.setColumnName("PRAB");
    parPRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    parPRAB.setDisplayMask("##0.00");
    parPRAB.setDefault("0");
    parPRAB.setPrecision(10);
    parPRAB.setScale(2);
    parPRAB.setTableName("PARTNERI");
    parPRAB.setServerColumnName("PRAB");
    parPRAB.setSqlType(2);

    parCAGENT.setCaption("Agent");
    parCAGENT.setColumnName("CAGENT");
    parCAGENT.setDataType(com.borland.dx.dataset.Variant.INT);
    parCAGENT.setTableName("PARTNERI");
    parCAGENT.setSqlType(4);
    parCAGENT.setServerColumnName("CAGENT");

    parCGRPAR.setCaption("Grupa");
    parCGRPAR.setColumnName("CGRPAR");
    parCGRPAR.setDataType(com.borland.dx.dataset.Variant.STRING);
    parCGRPAR.setPrecision(10);
    parCGRPAR.setTableName("PARTNERI");
    parCGRPAR.setServerColumnName("CGRPAR");
    parCGRPAR.setSqlType(1);
    parKO.setCaption("Kontakt");
    parKO.setColumnName("KO");
    parKO.setDataType(com.borland.dx.dataset.Variant.STRING);
    parKO.setPrecision(50);
    parKO.setTableName("PARTNERI");
    parKO.setServerColumnName("KO");
    parKO.setSqlType(1);
    parDOSP.setCaption("Dani dospije\u0107a");
    parDOSP.setColumnName("DOSP");
    parDOSP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    parDOSP.setTableName("PARTNERI");
    parDOSP.setServerColumnName("DOSP");
    parDOSP.setSqlType(5);
    parDI.setCaption("Tuz / Ino");
    parDI.setColumnName("DI");
    parDI.setDataType(com.borland.dx.dataset.Variant.STRING);
    parDI.setDefault("D");
    parDI.setPrecision(1);
    parDI.setTableName("PARTNERI");
    parDI.setServerColumnName("DI");
    parDI.setSqlType(1);
    parULOGA.setCaption("Uloga");
    parULOGA.setColumnName("ULOGA");
    parULOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    parULOGA.setDefault("O");
    parULOGA.setPrecision(1);
    parULOGA.setTableName("PARTNERI");
    parULOGA.setServerColumnName("ULOGA");
    parULOGA.setSqlType(1);
    parEMADR.setCaption("E-mail");
    parEMADR.setColumnName("EMADR");
    parEMADR.setDataType(com.borland.dx.dataset.Variant.STRING);
    parEMADR.setPrecision(50);
    parEMADR.setTableName("PARTNERI");
    parEMADR.setServerColumnName("EMADR");
    parEMADR.setSqlType(1);
    parTELFAX.setCaption("Fax");
    parTELFAX.setColumnName("TELFAX");
    parTELFAX.setDataType(com.borland.dx.dataset.Variant.STRING);
    parTELFAX.setPrecision(20);
    parTELFAX.setTableName("PARTNERI");
    parTELFAX.setServerColumnName("TELFAX");
    parTELFAX.setSqlType(1);
    parTEL.setCaption("Telefon");
    parTEL.setColumnName("TEL");
    parTEL.setDataType(com.borland.dx.dataset.Variant.STRING);
    parTEL.setPrecision(40);
    parTEL.setTableName("PARTNERI");
    parTEL.setServerColumnName("TEL");
    parTEL.setSqlType(1);
    parZR.setCaption("\u017Diro ra\u010Dun");
    parZR.setColumnName("ZR");
    parZR.setDataType(com.borland.dx.dataset.Variant.STRING);
    parZR.setPrecision(40);
    parZR.setTableName("PARTNERI");
    parZR.setServerColumnName("ZR");
    parZR.setSqlType(1);
    parCDJEL.setCaption("Djelatnost");
    parCDJEL.setColumnName("CDJEL");
    parCDJEL.setDataType(com.borland.dx.dataset.Variant.STRING);
    parCDJEL.setPrecision(10);
    parCDJEL.setTableName("PARTNERI");
    parCDJEL.setServerColumnName("CDJEL");
    parCDJEL.setSqlType(1);
    parMB.setCaption("Mati\u010Dni broj");
    parMB.setColumnName("MB");
    parMB.setDataType(com.borland.dx.dataset.Variant.STRING);
    parMB.setPrecision(13);
    parMB.setTableName("PARTNERI");
    parMB.setServerColumnName("MB");
    parMB.setSqlType(1);
    parPBR.setCaption("Poštanski broj");
    parPBR.setColumnName("PBR");
    parPBR.setDataType(com.borland.dx.dataset.Variant.INT);
    parPBR.setTableName("PARTNERI");
    parPBR.setServerColumnName("PBR");
    parPBR.setSqlType(4);
    parADRESA.setCaption("Adresa");
    parADRESA.setColumnName("ADR");
    parADRESA.setDataType(com.borland.dx.dataset.Variant.STRING);
    parADRESA.setPrecision(40);
    parADRESA.setTableName("PARTNERI");
    parADRESA.setServerColumnName("ADR");
    parADRESA.setSqlType(1);
    parMJ.setCaption("Mjesto");
    parMJ.setColumnName("MJ");
    parMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    parMJ.setPrecision(30);
    parMJ.setTableName("PARTNERI");
    parMJ.setServerColumnName("MJ");
    parMJ.setSqlType(1);
    parNAZPAR.setCaption("Naziv");
    parNAZPAR.setColumnName("NAZPAR");
    parNAZPAR.setDataType(com.borland.dx.dataset.Variant.STRING);
    parNAZPAR.setPrecision(150);
    parNAZPAR.setTableName("PARTNERI");
    parNAZPAR.setServerColumnName("NAZPAR");
    parNAZPAR.setSqlType(1);
    parNAZPAR.setWidth(30);
    parCPAR.setCaption("\u0160ifra");
    parCPAR.setColumnName("CPAR");
    parCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    parCPAR.setDisplayMask("######");
    parCPAR.setRowId(true);
    parCPAR.setTableName("PARTNERI");
    parCPAR.setWidth(5);
    parCPAR.setServerColumnName("CPAR");
    parCPAR.setSqlType(4);
    parAKTIV.setColumnName("AKTIV");
    parAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    parAKTIV.setDefault("D");
    parAKTIV.setPrecision(1);
    parAKTIV.setTableName("PARTNERI");
    parAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    parAKTIV.setServerColumnName("AKTIV");
    parAKTIV.setSqlType(1);
    parLOKK.setColumnName("LOKK");
    parLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    parLOKK.setDefault("N");
    parLOKK.setPrecision(1);
    parLOKK.setTableName("PARTNERI");
    parLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    parLOKK.setServerColumnName("LOKK");
    parLOKK.setSqlType(1);

    parCMJESTA.setCaption("Šifra grada");
    parCMJESTA.setColumnName("CMJESTA");
    parCMJESTA.setDataType(com.borland.dx.dataset.Variant.INT);
    parCMJESTA.setTableName("PARTNERI");
    parCMJESTA.setSqlType(4);
    parCMJESTA.setServerColumnName("CMJESTA");

    parCZUP.setCaption("Županija");
    parCZUP.setColumnName("CZUP");
    parCZUP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    parCZUP.setPrecision(3);
    parCZUP.setTableName("ZUPANIJE");
    parCZUP.setServerColumnName("CZUP");
    parCZUP.setSqlType(5);

    parCTEL.setCaption("Telemarketer");
    parCTEL.setColumnName("CTEL");
    parCTEL.setDataType(com.borland.dx.dataset.Variant.INT);
    parCTEL.setTableName("PARTNERI");
    parCTEL.setSqlType(4);
    parCTEL.setServerColumnName("CTEL");
    
    partneri.setResolver(dm.getQresolver());
    partneri.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * " +
      "FROM PARTNERI", null, true, Load.ALL));
 setColumns(new Column[] {parLOKK, parAKTIV, parCPAR, parNAZPAR, parMJ, parADRESA, parPBR, parMB, parCDJEL, parZR, parTEL,
        parTELFAX, parEMADR, parULOGA, parDI, parDOSP, parKO, parCGRPAR, parCAGENT, parPRAB, parSTATUS, parLIMKRED, parUGOVOR,
     parCZUP, parCMJESTA, parCTEL});*/

    createFilteredDataSet(partneriaktiv, "aktiv = 'D'");
    createFilteredDataSet(partnerikup, "aktiv = 'D' AND (uloga = 'K' OR uloga = 'O')");
    createFilteredDataSet(partneridob, "aktiv = 'D' AND (uloga = 'D' OR uloga = 'O')");
    createFilteredDataSet(partnerioboje, "aktiv = 'D' AND uloga = 'O'");
  }
  //public void setall(){

    /*SqlDefTabela = "create table Partneri " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " +
      "aktiv char(1) CHARACTER SET WIN1250 default 'D', " +
      "cpar numeric(6,0) not null,"+
      "nazpar char(50) CHARACTER SET WIN1250," +
      "mj char(30) CHARACTER SET WIN1250 , " +
      "adr char(40) CHARACTER SET WIN1250 , " +
      "pbr numeric(5,0) ,"+
      "mb char(13) CHARACTER SET WIN1250 , " +
      "cdjel char(10) CHARACTER SET WIN1250 , "+
      "zr char(40) CHARACTER SET WIN1250 , "+
      "tel char(20) CHARACTER SET WIN1250 , "+
      "telfax char(20) CHARACTER SET WIN1250, "+
      "emadr char(30) CHARACTER SET WIN1250 , "+
      "uloga char(1) CHARACTER SET WIN1250 , "+
      "di char(1) CHARACTER SET WIN1250 , "+
      "dosp numeric(3,0) , "+
      "ko char(50) CHARACTER SET WIN1250 , "+
      "cgrpar char(10) CHARACTER SET WIN1250, "+
      "regija char(20) CHARACTER SET WIN1250, "+
      "cagent numeric(6,0) , "+
      "prab numeric(6,2)  , "+
      "ugovor char(20) CHARACTER SET WIN1250 ," +
      "status char(1) CHARACTER SET WIN1250 , "+
      "limkred numeric(17,2),"+
      "brojkonta CHAR(8) CHARACTER SET win1250, " + //Konto partnera
      "Primary Key (cpar))" ; */

    /*ddl.create("partneri")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("cpar", 6, true)
       .addChar("nazpar", 150)
       .addChar("mj", 30)
       .addChar("adr", 40)
       .addInteger("pbr", 5)
       .addChar("mb", 13)
       .addChar("cdjel", 10)
       .addChar("zr", 40)
       .addChar("tel", 40)
       .addChar("telfax", 20)
       .addChar("emadr", 50)
       .addChar("uloga", 1)
       .addChar("di", 1)
       .addShort("dosp", 3)
       .addChar("ko", 50)
       .addChar("cgrpar", 10)
       .addInteger("cagent", 6)
       .addFloat("prab", 6, 2)
       .addChar("status", 1)
       .addFloat("limkred", 17, 2)
       .addChar("ugovor", 20)
       .addShort("czup", 3)
       .addInteger("cmjesta", 6)
       .addInteger("ctel", 6)
       .addPrimaryKey("cpar");

    Naziv="Partneri";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"nazpar", "mb", "zr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);*/

    /*
    NaziviIdx=new String[]{"ilokkpar","iaktivpar","icpar","inazpar","imb","izr","icgrpar"};

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Partneri (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Partneri (aktiv)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Partneri (cpar)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[3] +" on Partneri (nazpar)",
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[4] +" on Partneri (mb)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[5] +" on Partneri (zr) ",
                            CommonTable.SqlDefIndex+NaziviIdx[6] +" on Partneri (cgrpar)"};

          */
  //}
}