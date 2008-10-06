/****license*****************************************************************
**   file: RN.java
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

public class RN extends KreirDrop implements DataModule {

  private static RN RNclass;
  dM dm  = dM.getDataModule();

  QueryDataSet RN = new raDataSet();
  QueryDataSet RNser = new raDataSet();
  QueryDataSet RNpro = new raDataSet();
  QueryDataSet RNp = new raDataSet();
  QueryDataSet RNo = new raDataSet();
  QueryDataSet RNz = new raDataSet();

/*  Column RNLOKK = new Column();
  Column RNAKTIV = new Column();
  Column RNVRDOK = new Column();
  Column RNCSKL = new Column();
  Column RNGOD = new Column();
  Column RNBRDOK = new Column();
  Column RNCKUPAC = new Column();
  Column RNCSUBRN = new Column();
  Column RNCVRSUBJ = new Column();
  Column RNCTEKST = new Column();
  Column RNOPIS = new Column();
  Column RNCSKUPART = new Column();
  Column RNSTATUS = new Column();
  Column RNCFAKTURE = new Column();
  Column RNCRADNAL = new Column();
  Column RNDATUMP = new Column();
  Column RNDATUMO = new Column();
  Column RNDATUMZ = new Column();
  Column RNCNAP1 = new Column();
  Column RNCNAP2 = new Column();
  Column RNBRNAR = new Column();
  Column RNBRUGOVORA = new Column();
  Column RNCOSIGUR = new Column();
  Column RNCUSEROTVORIO = new Column();
  Column RNCUSEROBRAC = new Column();
  Column RNCUSERPOSLOV = new Column();
  Column RNIZVRSENJE = new Column();
  Column RNSERPR = new Column();
  Column RNKUPPAR = new Column();
  Column RNGARANC = new Column();
*/

  public static RN getDataModule() {
    if (RNclass == null) {
      RNclass = new RN();
    }
    return RNclass;
  }

  public RN(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
/*  public void setall(){


    ddl.create("RN")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("vrdok", 3)
       .addChar("cskl", 12, true)
       .addChar("god", 4, true)
       .addInteger("brdok", 6, true)
       .addInteger("ckupac", 6)
       .addChar("csubrn", 20)
       .addShort("cvrsubj", 3)
       .addInteger("ctekst", 6)
       .addChar("opis", 200)
       .addChar("cskupart", 6)
       .addChar("status", 1)
       .addChar("cfakture", 30)
       .addChar("cradnal", 30)
       .addDate("datdok")
       .addDate("datumo")
       .addDate("datumz")
       .addChar("cnap1", 3)
       .addChar("cnap2", 3)
       .addChar("brnar", 30)
       .addChar("brugovora", 30)
       .addInteger("cpar", 6)
       .addChar("cuserotvorio", 15)
       .addChar("cuserobrac", 15)
       .addChar("cuserposlov", 15)
       .addChar("izvrsenje", 1)
       .addChar("serpr", 1)
       .addChar("kuppar", 1)
       .addChar("garanc", 1, "N")
       .addPrimaryKey("cskl,god,brdok");


    Naziv="RN";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"brdok", "csubrn", "cradnal"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);


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


  } */
  private void jbInit() throws Exception {
    initModule();
/*    RNGOD.setCaption("Godina");
    RNGOD.setColumnName("GOD");
    RNGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNGOD.setPrecision(4);
    RNGOD.setRowId(true);
    RNGOD.setTableName("RN");
    RNGOD.setSqlType(1);
    RNGOD.setServerColumnName("GOD");

    RNAKTIV.setCaption("Aktiv");
    RNAKTIV.setColumnName("AKTIV");
    RNAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNAKTIV.setDefault("D");
    RNAKTIV.setPrecision(1);
    RNAKTIV.setTableName("RN");
    RNAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNAKTIV.setSqlType(1);
    RNAKTIV.setServerColumnName("AKTIV");

    RNLOKK.setCaption("Lokk");
    RNLOKK.setColumnName("LOKK");
    RNLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNLOKK.setDefault("N");
    RNLOKK.setPrecision(1);
    RNLOKK.setTableName("RN");
    RNLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNLOKK.setSqlType(1);
    RNLOKK.setServerColumnName("LOKK");

    RNCSKL.setCaption("Org. jed.");
    RNCSKL.setColumnName("CSKL");
    RNCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNCSKL.setPrecision(12);
    RNCSKL.setRowId(true);
    RNCSKL.setTableName("RN");
    RNCSKL.setSqlType(1);
    RNCSKL.setServerColumnName("CSKL");

    RNVRDOK.setCaption("Vrsta");
    RNVRDOK.setColumnName("VRDOK");
    RNVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNVRDOK.setPrecision(3);
    RNVRDOK.setTableName("RN");
    RNVRDOK.setSqlType(1);
    RNVRDOK.setServerColumnName("VRDOK");

    RNCUSEROTVORIO.setCaption("Otvorio");
    RNCUSEROTVORIO.setColumnName("CUSEROTVORIO");
    RNCUSEROTVORIO.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNCUSEROTVORIO.setPrecision(15);
    RNCUSEROTVORIO.setTableName("RN");
    RNCUSEROTVORIO.setServerColumnName("CUSEROTVORIO");
    RNCUSEROTVORIO.setSqlType(1);

    RNCUSEROBRAC.setCaption("Obra\u010Dunao");
    RNCUSEROBRAC.setColumnName("CUSEROBRAC");
    RNCUSEROBRAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNCUSEROBRAC.setPrecision(15);
    RNCUSEROBRAC.setTableName("RN");
    RNCUSEROBRAC.setServerColumnName("CUSEROBRAC");
    RNCUSEROBRAC.setSqlType(1);

    RNCUSERPOSLOV.setCaption("Poslovo\u0111a");
    RNCUSERPOSLOV.setColumnName("CUSERPOSLOV");
    RNCUSERPOSLOV.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNCUSERPOSLOV.setPrecision(15);
    RNCUSERPOSLOV.setTableName("RN");
    RNCUSERPOSLOV.setServerColumnName("CUSERPOSLOV");
    RNCUSERPOSLOV.setSqlType(1);

    RNBRDOK.setCaption("Broj");
    RNBRDOK.setColumnName("BRDOK");
    RNBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    RNBRDOK.setRowId(true);
    RNBRDOK.setTableName("RN");
    RNBRDOK.setWidth(5);
    RNBRDOK.setSqlType(4);
    RNBRDOK.setServerColumnName("BRDOK");

    RNIZVRSENJE.setCaption("Izvršenje");
    RNIZVRSENJE.setColumnName("IZVRSENJE");
    RNIZVRSENJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNIZVRSENJE.setPrecision(1);
    RNIZVRSENJE.setTableName("RN");
    RNIZVRSENJE.setSqlType(1);
    RNIZVRSENJE.setServerColumnName("IZVRSENJE");

    RNSERPR.setCaption("Servis/Proizvodnja");
    RNSERPR.setColumnName("SERPR");
    RNSERPR.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNSERPR.setPrecision(1);
    RNSERPR.setTableName("RN");
    RNSERPR.setSqlType(1);
    RNSERPR.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNSERPR.setServerColumnName("SERPR");

    RNKUPPAR.setCaption("Kupac/partner");
    RNKUPPAR.setColumnName("KUPPAR");
    RNKUPPAR.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNKUPPAR.setPrecision(1);
    RNKUPPAR.setTableName("RN");
    RNKUPPAR.setSqlType(1);
    RNKUPPAR.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    RNKUPPAR.setServerColumnName("KUPPAR");

    RNGARANC.setCaption("Garancija");
    RNGARANC.setColumnName("GARANC");
    RNGARANC.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNGARANC.setPrecision(1);
    RNGARANC.setDefault("N");
    RNGARANC.setTableName("RN");
    RNGARANC.setSqlType(1);
    RNGARANC.setServerColumnName("GARANC");

    RNCSUBRN.setCaption("Subjekt");
    RNCSUBRN.setColumnName("CSUBRN");
    RNCSUBRN.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNCSUBRN.setPrecision(20);
    RNCSUBRN.setTableName("RN");
    RNCSUBRN.setSqlType(1);
    RNCSUBRN.setWidth(30);
    RNCSUBRN.setServerColumnName("CSUBRN");

    RNCVRSUBJ.setCaption("Vrsta subjekta");
    RNCVRSUBJ.setColumnName("CVRSUBJ");
    RNCVRSUBJ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    RNCVRSUBJ.setPrecision(3);
    RNCVRSUBJ.setTableName("RN");
    RNCVRSUBJ.setSqlType(5);
    RNCVRSUBJ.setServerColumnName("CVRSUBJ");

    RNCTEKST.setCaption("Šifra teksta");
    RNCTEKST.setColumnName("CTEKST");
    RNCTEKST.setDataType(com.borland.dx.dataset.Variant.INT);
    RNCTEKST.setTableName("RN");
    RNCTEKST.setSqlType(4);
    RNCTEKST.setServerColumnName("CTEKST");

    RNCSKUPART.setCaption("Normativ");
    RNCSKUPART.setColumnName("CSKUPART");
    RNCSKUPART.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNCSKUPART.setPrecision(6);
    RNCSKUPART.setTableName("RN");
    RNCSKUPART.setWidth(5);
    RNCSKUPART.setServerColumnName("CSKUPART");
    RNCSKUPART.setSqlType(1);

    RNCFAKTURE.setCaption("Šifra fakture");
    RNCFAKTURE.setColumnName("CFAKTURE");
    RNCFAKTURE.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNCFAKTURE.setPrecision(30);
    RNCFAKTURE.setTableName("RN");
    RNCFAKTURE.setSqlType(1);
    RNCFAKTURE.setServerColumnName("CFAKTURE");

    RNCRADNAL.setCaption("Šifra");
    RNCRADNAL.setColumnName("CRADNAL");
    RNCRADNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNCRADNAL.setPrecision(30);
    RNCRADNAL.setTableName("RN");
    RNCRADNAL.setSqlType(1);
    RNCRADNAL.setWidth(15);
    RNCRADNAL.setServerColumnName("CRADNAL");

    RNCOSIGUR.setCaption("Partner");
    RNCOSIGUR.setColumnName("CPAR");
    RNCOSIGUR.setDataType(com.borland.dx.dataset.Variant.INT);
    RNCOSIGUR.setPrecision(6);
    RNCOSIGUR.setTableName("RN");
    RNCOSIGUR.setSqlType(4);
    RNCOSIGUR.setServerColumnName("CPAR");

    RNCNAP1.setCaption("Napomena 1");
    RNCNAP1.setColumnName("CNAP1");
    RNCNAP1.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNCNAP1.setPrecision(3);
    RNCNAP1.setTableName("RN");
    RNCNAP1.setSqlType(1);
    RNCNAP1.setServerColumnName("CNAP1");

    RNCNAP2.setCaption("Napomena 2");
    RNCNAP2.setColumnName("CNAP2");
    RNCNAP2.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNCNAP2.setPrecision(3);
    RNCNAP2.setTableName("RN");
    RNCNAP2.setSqlType(1);
    RNCNAP2.setServerColumnName("CNAP2");

    RNSTATUS.setCaption("Status");
    RNSTATUS.setColumnName("STATUS");
    RNSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNSTATUS.setPrecision(1);
    RNSTATUS.setTableName("RN");
    RNSTATUS.setSqlType(1);
    RNSTATUS.setServerColumnName("STATUS");

    RNBRNAR.setCaption("Broj narudžbe");
    RNBRNAR.setColumnName("BRNAR");
    RNBRNAR.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNBRNAR.setPrecision(30);
    RNBRNAR.setTableName("RN");
    RNBRNAR.setSqlType(1);
    RNBRNAR.setServerColumnName("BRNAR");

    RNBRUGOVORA.setCaption("Broj izlazne narudžbe");
    RNBRUGOVORA.setColumnName("BRUGOVORA");
    RNBRUGOVORA.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNBRUGOVORA.setPrecision(30);
    RNBRUGOVORA.setTableName("RN");
    RNBRUGOVORA.setSqlType(1);
    RNBRUGOVORA.setServerColumnName("BRUGOVORA");

    RNDATUMP.setAlignment(com.borland.dx.text.Alignment.CENTER | com.borland.dx.text.Alignment.MIDDLE);
    RNDATUMP.setCaption("Datum otvaranja");
    RNDATUMP.setColumnName("DATDOK");
    RNDATUMP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    RNDATUMP.setDisplayMask("dd-MM-yyyy");
//    RNDATUMP.setEditMask("dd-MM-yyyy");
    RNDATUMP.setTableName("RN");
    RNDATUMP.setWidth(10);
    RNDATUMP.setSqlType(93);
    RNDATUMP.setServerColumnName("DATDOK");

    RNCKUPAC.setCaption("Vlasnik");
    RNCKUPAC.setColumnName("CKUPAC");
    RNCKUPAC.setDataType(com.borland.dx.dataset.Variant.INT);
    RNCKUPAC.setTableName("RN");
    RNCKUPAC.setWidth(20);
    RNCKUPAC.setServerColumnName("CKUPAC");
    RNCKUPAC.setSqlType(4);

    RNDATUMZ.setCaption("Datum zatvaranja");
    RNDATUMZ.setColumnName("DATUMZ");
    RNDATUMZ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    RNDATUMZ.setDisplayMask("dd-MM-yyyy");
//    RNDATUMZ.setEditMask("dd-MM-yyyy");
    RNDATUMZ.setTableName("RN");
    RNDATUMZ.setWidth(10);
    RNDATUMZ.setSqlType(93);
    RNDATUMZ.setServerColumnName("DATUMZ");

    RNDATUMO.setCaption("Datum obrade");
    RNDATUMO.setColumnName("DATUMO");
    RNDATUMO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    RNDATUMO.setDisplayMask("dd-MM-yyyy");
//    RNDATUMO.setEditMask("dd-MM-yyyy");
    RNDATUMO.setTableName("RN");
    RNDATUMO.setWidth(10);
    RNDATUMO.setSqlType(93);
    RNDATUMO.setServerColumnName("DATUMO");

    RNOPIS.setCaption("Opis");
    RNOPIS.setColumnName("OPIS");
    RNOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    RNOPIS.setPrecision(200);
    RNOPIS.setTableName("RN");
    RNOPIS.setSqlType(1);
    RNOPIS.setServerColumnName("OPIS");

    RN.setResolver(dm.getQresolver());
    RN.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from RN", null, true, Load.ALL));
 setColumns(new Column[] {RNLOKK, RNAKTIV, RNVRDOK, RNCSKL, RNGOD, RNBRDOK, RNCKUPAC, RNCSUBRN, RNCVRSUBJ, RNCTEKST, RNOPIS, RNCSKUPART, RNSTATUS, RNCFAKTURE,
        RNCRADNAL, RNDATUMP, RNDATUMO, RNDATUMZ, RNCNAP1, RNCNAP2, RNBRNAR, RNBRUGOVORA, RNCOSIGUR, RNCUSEROTVORIO , RNCUSEROBRAC, RNCUSERPOSLOV, RNIZVRSENJE,
        RNSERPR, RNKUPPAR, RNGARANC});
*/
    createFilteredDataSet(RNser, "serpr = 'S'");
    createFilteredDataSet(RNpro, "serpr = 'P'");
    createFilteredDataSet(RNp, "status = 'P'");
    createFilteredDataSet(RNo, "status = 'O'");
    createFilteredDataSet(RNz, "status = 'Z'");
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return RN;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRnser() {
    return RNser;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRnpro() {
    return RNpro;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRnp() {
    return RNp;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRno() {
    return RNo;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRnz() {
    return RNz;
  }
}

