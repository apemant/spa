/****license*****************************************************************
**   file: Carulaz.java
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
import com.borland.dx.sql.dataset.QueryDescriptor;



public class Carulaz extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carulaz Carulazclass;

  QueryDataSet carulaz = new QueryDataSet();

  Column carulazLOCK = new Column();
  Column carulazAKTIV = new Column();
  Column carulazCUSER = new Column();
  Column carulazID_ULAZ_ZAG = new Column();
  Column carulazCBRDOK = new Column();
  Column carulazDATDOK = new Column();
  Column carulazCPAR = new Column();
  Column carulazODOB = new Column();
  Column carulazDATODOB = new Column();
  Column carulazCSKL = new Column();
  Column carulazRACPRI = new Column();
  Column carulazBRNAZK = new Column();
  Column carulazDATNAZK = new Column();
  Column carulazJCD_CCAR_PRET = new Column();
  Column carulazJCD_BROJ_PRET = new Column();
  Column carulazJCD_DATUM_PRET = new Column();
  Column carulazJCD_CPP37_PRET = new Column();
  Column carulazJCD_CCAR_PROV = new Column();
  Column carulazJCD_BROJ_PROV = new Column();
  Column carulazJCD_DATUM_PROV = new Column();
  Column carulazJCD_CPP37_PROV = new Column();
  Column carulazCPAR1PP = new Column();
  Column carulazOZNVAL = new Column();
  Column carulazTECAJ = new Column();
  Column carulazBRUTOKG = new Column();
  Column carulazPRS = new Column();
  Column carulazODGOSOBA = new Column();
  Column carulazCOMMENT = new Column();

  public static Carulaz getDataModule() {
    if (Carulazclass == null) {
      Carulazclass = new Carulaz();
    }
    return Carulazclass;
  }

  public QueryDataSet getQueryDataSet() {
    return carulaz;
  }

  public Carulaz() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carulazLOCK.setCaption("Status zauzetosti");
    carulazLOCK.setColumnName("LOCK");
    carulazLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazLOCK.setPrecision(1);
    carulazLOCK.setTableName("CARULAZ");
    carulazLOCK.setServerColumnName("LOCK");
    carulazLOCK.setSqlType(1);
    carulazLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carulazLOCK.setDefault("N");
    carulazAKTIV.setCaption("Aktivan - neaktivan");
    carulazAKTIV.setColumnName("AKTIV");
    carulazAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazAKTIV.setPrecision(1);
    carulazAKTIV.setTableName("CARULAZ");
    carulazAKTIV.setServerColumnName("AKTIV");
    carulazAKTIV.setSqlType(1);
    carulazAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carulazAKTIV.setDefault("D");
    carulazCUSER.setCaption("Operater");
    carulazCUSER.setColumnName("CUSER");
    carulazCUSER.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazCUSER.setPrecision(15);
    carulazCUSER.setTableName("CARULAZ");
    carulazCUSER.setServerColumnName("CUSER");
    carulazCUSER.setSqlType(1);
    carulazID_ULAZ_ZAG.setCaption("Broja\u010D autonumber");
    carulazID_ULAZ_ZAG.setColumnName("ID_ULAZ_ZAG");
    carulazID_ULAZ_ZAG.setDataType(com.borland.dx.dataset.Variant.INT);
    carulazID_ULAZ_ZAG.setRowId(true);
    carulazID_ULAZ_ZAG.setTableName("CARULAZ");
    carulazID_ULAZ_ZAG.setServerColumnName("ID_ULAZ_ZAG");
    carulazID_ULAZ_ZAG.setSqlType(4);
    carulazID_ULAZ_ZAG.setWidth(8);
    carulazID_ULAZ_ZAG.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carulazCBRDOK.setCaption("Broj dokumenta");
    carulazCBRDOK.setColumnName("CBRDOK");
    carulazCBRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazCBRDOK.setPrecision(30);
    carulazCBRDOK.setTableName("CARULAZ");
    carulazCBRDOK.setServerColumnName("CBRDOK");
    carulazCBRDOK.setSqlType(1);
    carulazCBRDOK.setWidth(30);
    carulazDATDOK.setCaption("Datum dokumenta");
    carulazDATDOK.setColumnName("DATDOK");
    carulazDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    carulazDATDOK.setDisplayMask("dd-MM-yyyy");
//    carulazDATDOK.setEditMask("dd-MM-yyyy");
    carulazDATDOK.setTableName("CARULAZ");
    carulazDATDOK.setServerColumnName("DATDOK");
    carulazDATDOK.setSqlType(93);
    carulazDATDOK.setWidth(10);
    carulazCPAR.setCaption("Principal");
    carulazCPAR.setColumnName("CPAR");
    carulazCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    carulazCPAR.setTableName("CARULAZ");
    carulazCPAR.setServerColumnName("CPAR");
    carulazCPAR.setSqlType(4);
    carulazCPAR.setWidth(5);
    carulazODOB.setCaption("Odobrenje");
    carulazODOB.setColumnName("ODOB");
    carulazODOB.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazODOB.setPrecision(30);
    carulazODOB.setTableName("CARULAZ");
    carulazODOB.setServerColumnName("ODOB");
    carulazODOB.setSqlType(1);
    carulazODOB.setWidth(30);
    carulazDATODOB.setCaption("Datum odobrenja");
    carulazDATODOB.setColumnName("DATODOB");
    carulazDATODOB.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    carulazDATODOB.setDisplayMask("dd-MM-yyyy");
//    carulazDATODOB.setEditMask("dd-MM-yyyy");
    carulazDATODOB.setTableName("CARULAZ");
    carulazDATODOB.setServerColumnName("DATODOB");
    carulazDATODOB.setSqlType(93);
    carulazDATODOB.setWidth(10);
    carulazCSKL.setCaption("Skladište");
    carulazCSKL.setColumnName("CSKL");
    carulazCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazCSKL.setPrecision(12);
    carulazCSKL.setTableName("CARULAZ");
    carulazCSKL.setServerColumnName("CSKL");
    carulazCSKL.setSqlType(1);
    carulazRACPRI.setCaption("Ra\u010Dun principala");
    carulazRACPRI.setColumnName("RACPRI");
    carulazRACPRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazRACPRI.setPrecision(30);
    carulazRACPRI.setTableName("CARULAZ");
    carulazRACPRI.setServerColumnName("RACPRI");
    carulazRACPRI.setSqlType(1);
    carulazRACPRI.setWidth(30);
    carulazBRNAZK.setCaption("Broj nadzorne knjige");
    carulazBRNAZK.setColumnName("BRNAZK");
    carulazBRNAZK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazBRNAZK.setPrecision(30);
    carulazBRNAZK.setTableName("CARULAZ");
    carulazBRNAZK.setServerColumnName("BRNAZK");
    carulazBRNAZK.setSqlType(1);
    carulazBRNAZK.setWidth(30);
    carulazDATNAZK.setCaption("Datum nadzorna knjige");
    carulazDATNAZK.setColumnName("DATNAZK");
    carulazDATNAZK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    carulazDATNAZK.setDisplayMask("dd-MM-yyyy");
//    carulazDATNAZK.setEditMask("dd-MM-yyyy");
    carulazDATNAZK.setTableName("CARULAZ");
    carulazDATNAZK.setServerColumnName("DATNAZK");
    carulazDATNAZK.setSqlType(93);
    carulazDATNAZK.setWidth(10);
    carulazJCD_CCAR_PRET.setCaption("Šifra carinarnice (prethodni)");
    carulazJCD_CCAR_PRET.setColumnName("JCD_CCAR_PRET");
    carulazJCD_CCAR_PRET.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazJCD_CCAR_PRET.setPrecision(20);
    carulazJCD_CCAR_PRET.setTableName("CARULAZ");
    carulazJCD_CCAR_PRET.setServerColumnName("JCD_CCAR_PRET");
    carulazJCD_CCAR_PRET.setSqlType(1);
    carulazJCD_BROJ_PRET.setCaption("Broj JCD (prethodni)");
    carulazJCD_BROJ_PRET.setColumnName("JCD_BROJ_PRET");
    carulazJCD_BROJ_PRET.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazJCD_BROJ_PRET.setPrecision(30);
    carulazJCD_BROJ_PRET.setTableName("CARULAZ");
    carulazJCD_BROJ_PRET.setServerColumnName("JCD_BROJ_PRET");
    carulazJCD_BROJ_PRET.setSqlType(1);
    carulazJCD_BROJ_PRET.setWidth(30);
    carulazJCD_DATUM_PRET.setCaption("Datum JCD (prethodni)");
    carulazJCD_DATUM_PRET.setColumnName("JCD_DATUM_PRET");
    carulazJCD_DATUM_PRET.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    carulazJCD_DATUM_PRET.setDisplayMask("dd-MM-yyyy");
//    carulazJCD_DATUM_PRET.setEditMask("dd-MM-yyyy");
    carulazJCD_DATUM_PRET.setTableName("CARULAZ");
    carulazJCD_DATUM_PRET.setServerColumnName("JCD_DATUM_PRET");
    carulazJCD_DATUM_PRET.setSqlType(93);
    carulazJCD_DATUM_PRET.setWidth(10);
    carulazJCD_CPP37_PRET.setCaption("Šifra carinskog postupka");
    carulazJCD_CPP37_PRET.setColumnName("JCD_CPP37_PRET");
    carulazJCD_CPP37_PRET.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazJCD_CPP37_PRET.setPrecision(10);
    carulazJCD_CPP37_PRET.setTableName("CARULAZ");
    carulazJCD_CPP37_PRET.setServerColumnName("JCD_CPP37_PRET");
    carulazJCD_CPP37_PRET.setSqlType(1);
    carulazJCD_CCAR_PROV.setCaption("Šifra carinarnice (provozni)");
    carulazJCD_CCAR_PROV.setColumnName("JCD_CCAR_PROV");
    carulazJCD_CCAR_PROV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazJCD_CCAR_PROV.setPrecision(20);
    carulazJCD_CCAR_PROV.setTableName("CARULAZ");
    carulazJCD_CCAR_PROV.setServerColumnName("JCD_CCAR_PROV");
    carulazJCD_CCAR_PROV.setSqlType(1);
    carulazJCD_BROJ_PROV.setCaption("Broj JCD (provozni)");
    carulazJCD_BROJ_PROV.setColumnName("JCD_BROJ_PROV");
    carulazJCD_BROJ_PROV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazJCD_BROJ_PROV.setPrecision(30);
    carulazJCD_BROJ_PROV.setTableName("CARULAZ");
    carulazJCD_BROJ_PROV.setServerColumnName("JCD_BROJ_PROV");
    carulazJCD_BROJ_PROV.setSqlType(1);
    carulazJCD_BROJ_PROV.setWidth(30);
    carulazJCD_DATUM_PROV.setCaption("Datum JCD (provozni)");
    carulazJCD_DATUM_PROV.setColumnName("JCD_DATUM_PROV");
    carulazJCD_DATUM_PROV.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    carulazJCD_DATUM_PROV.setDisplayMask("dd-MM-yyyy");
//    carulazJCD_DATUM_PROV.setEditMask("dd-MM-yyyy");
    carulazJCD_DATUM_PROV.setTableName("CARULAZ");
    carulazJCD_DATUM_PROV.setServerColumnName("JCD_DATUM_PROV");
    carulazJCD_DATUM_PROV.setSqlType(93);
    carulazJCD_DATUM_PROV.setWidth(10);
    carulazJCD_CPP37_PROV.setCaption("Šifra carinskog postupka");
    carulazJCD_CPP37_PROV.setColumnName("JCD_CPP37_PROV");
    carulazJCD_CPP37_PROV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazJCD_CPP37_PROV.setPrecision(10);
    carulazJCD_CPP37_PROV.setTableName("CARULAZ");
    carulazJCD_CPP37_PROV.setServerColumnName("JCD_CPP37_PROV");
    carulazJCD_CPP37_PROV.setSqlType(1);
    carulazCPAR1PP.setCaption("Šifra pariteta za prvo podpolje");
    carulazCPAR1PP.setColumnName("CPAR1PP");
    carulazCPAR1PP.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazCPAR1PP.setPrecision(10);
    carulazCPAR1PP.setTableName("CARULAZ");
    carulazCPAR1PP.setServerColumnName("CPAR1PP");
    carulazCPAR1PP.setSqlType(1);
    carulazOZNVAL.setCaption("Valuta");
    carulazOZNVAL.setColumnName("OZNVAL");
    carulazOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazOZNVAL.setPrecision(3);
    carulazOZNVAL.setTableName("CARULAZ");
    carulazOZNVAL.setServerColumnName("OZNVAL");
    carulazOZNVAL.setSqlType(1);
    carulazTECAJ.setCaption("Iznos te\u010Daja");
    carulazTECAJ.setColumnName("TECAJ");
    carulazTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carulazTECAJ.setPrecision(17);
    carulazTECAJ.setScale(6);
    carulazTECAJ.setDisplayMask("###,###,##0.000000");
    carulazTECAJ.setDefault("0");
    carulazTECAJ.setTableName("CARULAZ");
    carulazTECAJ.setServerColumnName("TECAJ");
    carulazTECAJ.setSqlType(2);
    carulazBRUTOKG.setCaption("Bruto težina (kg)");
    carulazBRUTOKG.setColumnName("BRUTOKG");
    carulazBRUTOKG.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carulazBRUTOKG.setPrecision(17);
    carulazBRUTOKG.setScale(3);
    carulazBRUTOKG.setDisplayMask("###,###,##0.000");
    carulazBRUTOKG.setDefault("0");
    carulazBRUTOKG.setTableName("CARULAZ");
    carulazBRUTOKG.setServerColumnName("BRUTOKG");
    carulazBRUTOKG.setSqlType(2);
    carulazPRS.setCaption("Prijevozno sredstvo");
    carulazPRS.setColumnName("PRS");
    carulazPRS.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazPRS.setPrecision(80);
    carulazPRS.setTableName("CARULAZ");
    carulazPRS.setServerColumnName("PRS");
    carulazPRS.setSqlType(1);
    carulazPRS.setWidth(30);
    carulazODGOSOBA.setCaption("Odgovorna osoba");
    carulazODGOSOBA.setColumnName("ODGOSOBA");
    carulazODGOSOBA.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazODGOSOBA.setPrecision(80);
    carulazODGOSOBA.setTableName("CARULAZ");
    carulazODGOSOBA.setServerColumnName("ODGOSOBA");
    carulazODGOSOBA.setSqlType(1);
    carulazODGOSOBA.setWidth(30);
    carulazCOMMENT.setCaption("Komentar");
    carulazCOMMENT.setColumnName("COMMENT");
    carulazCOMMENT.setDataType(com.borland.dx.dataset.Variant.STRING);
    carulazCOMMENT.setPrecision(500);
    carulazCOMMENT.setTableName("CARULAZ");
    carulazCOMMENT.setServerColumnName("COMMENT");
    carulazCOMMENT.setSqlType(1);
    carulazCOMMENT.setWidth(30);
    carulaz.setResolver(dm.getQresolver());
    carulaz.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carulaz", null, true, Load.ALL));
    setColumns(new Column[] {carulazLOCK, carulazAKTIV, carulazCUSER, carulazID_ULAZ_ZAG, carulazCBRDOK, carulazDATDOK, carulazCPAR, carulazODOB, 
        carulazDATODOB, carulazCSKL, carulazRACPRI, carulazBRNAZK, carulazDATNAZK, carulazJCD_CCAR_PRET, carulazJCD_BROJ_PRET, carulazJCD_DATUM_PRET, 
        carulazJCD_CPP37_PRET, carulazJCD_CCAR_PROV, carulazJCD_BROJ_PROV, carulazJCD_DATUM_PROV, carulazJCD_CPP37_PROV, carulazCPAR1PP, carulazOZNVAL, 
        carulazTECAJ, carulazBRUTOKG, carulazPRS, carulazODGOSOBA, carulazCOMMENT});
  }

  public void setall() {

    ddl.create("Carulaz")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cuser", 15)
       .addInteger("id_ulaz_zag", 8, true)
       .addChar("cbrdok", 30)
       .addDate("datdok")
       .addInteger("cpar", 5)
       .addChar("odob", 30)
       .addDate("datodob")
       .addChar("cskl", 12)
       .addChar("racpri", 30)
       .addChar("brnazk", 30)
       .addDate("datnazk")
       .addChar("jcd_ccar_pret", 20)
       .addChar("jcd_broj_pret", 30)
       .addDate("jcd_datum_pret")
       .addChar("jcd_cpp37_pret", 10)
       .addChar("jcd_ccar_prov", 20)
       .addChar("jcd_broj_prov", 30)
       .addDate("jcd_datum_prov")
       .addChar("jcd_cpp37_prov", 10)
       .addChar("cpar1pp", 10)
       .addChar("oznval", 3)
       .addFloat("tecaj", 17, 6)
       .addFloat("brutokg", 17, 3)
       .addChar("prs", 80)
       .addChar("odgosoba", 80)
       .addChar("comment", 500)
       .addPrimaryKey("id_ulaz_zag");


    Naziv = "Carulaz";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"id_ulaz_zag"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
