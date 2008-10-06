/****license*****************************************************************
**   file: Carizlaz.java
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



public class Carizlaz extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carizlaz Carizlazclass;

  QueryDataSet carizlaz = new QueryDataSet();

  Column carizlazLOCK = new Column();
  Column carizlazAKTIV = new Column();
  Column carizlazCUSER = new Column();
  Column carizlazID_IZLAZ_ZAG = new Column();
  Column carizlazCBRDOK = new Column();
  Column carizlazDATDOK = new Column();
  Column carizlazCPAR = new Column();
  Column carizlazCSKL = new Column();
  Column carizlazRACPRI = new Column();
  Column carizlazOZNVAL = new Column();
  Column carizlazTECAJ = new Column();
  Column carizlazODGOSOBA = new Column();
  Column carizlazCOMMENT = new Column();

  public static Carizlaz getDataModule() {
    if (Carizlazclass == null) {
      Carizlazclass = new Carizlaz();
    }
    return Carizlazclass;
  }

  public QueryDataSet getQueryDataSet() {
    return carizlaz;
  }

  public Carizlaz() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carizlazLOCK.setCaption("Status zauzetosti");
    carizlazLOCK.setColumnName("LOCK");
    carizlazLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazLOCK.setPrecision(1);
    carizlazLOCK.setTableName("CARIZLAZ");
    carizlazLOCK.setServerColumnName("LOCK");
    carizlazLOCK.setSqlType(1);
    carizlazLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carizlazLOCK.setDefault("N");
    carizlazAKTIV.setCaption("Aktivan - neaktivan");
    carizlazAKTIV.setColumnName("AKTIV");
    carizlazAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazAKTIV.setPrecision(1);
    carizlazAKTIV.setTableName("CARIZLAZ");
    carizlazAKTIV.setServerColumnName("AKTIV");
    carizlazAKTIV.setSqlType(1);
    carizlazAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carizlazAKTIV.setDefault("D");
    carizlazCUSER.setCaption("Operater");
    carizlazCUSER.setColumnName("CUSER");
    carizlazCUSER.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazCUSER.setPrecision(15);
    carizlazCUSER.setTableName("CARIZLAZ");
    carizlazCUSER.setServerColumnName("CUSER");
    carizlazCUSER.setSqlType(1);
    carizlazID_IZLAZ_ZAG.setCaption("Broja\u010D autonumber");
    carizlazID_IZLAZ_ZAG.setColumnName("ID_IZLAZ_ZAG");
    carizlazID_IZLAZ_ZAG.setDataType(com.borland.dx.dataset.Variant.INT);
    carizlazID_IZLAZ_ZAG.setRowId(true);
    carizlazID_IZLAZ_ZAG.setTableName("CARIZLAZ");
    carizlazID_IZLAZ_ZAG.setServerColumnName("ID_IZLAZ_ZAG");
    carizlazID_IZLAZ_ZAG.setSqlType(4);
    carizlazID_IZLAZ_ZAG.setWidth(8);
    carizlazID_IZLAZ_ZAG.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carizlazCBRDOK.setCaption("Broj dokumenta");
    carizlazCBRDOK.setColumnName("CBRDOK");
    carizlazCBRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazCBRDOK.setPrecision(30);
    carizlazCBRDOK.setTableName("CARIZLAZ");
    carizlazCBRDOK.setServerColumnName("CBRDOK");
    carizlazCBRDOK.setSqlType(1);
    carizlazCBRDOK.setWidth(30);
    carizlazDATDOK.setCaption("Datum dokumenta");
    carizlazDATDOK.setColumnName("DATDOK");
    carizlazDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    carizlazDATDOK.setDisplayMask("dd-MM-yyyy");
//    carizlazDATDOK.setEditMask("dd-MM-yyyy");
    carizlazDATDOK.setTableName("CARIZLAZ");
    carizlazDATDOK.setServerColumnName("DATDOK");
    carizlazDATDOK.setSqlType(93);
    carizlazDATDOK.setWidth(10);
    carizlazCPAR.setCaption("Principal");
    carizlazCPAR.setColumnName("CPAR");
    carizlazCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    carizlazCPAR.setTableName("CARIZLAZ");
    carizlazCPAR.setServerColumnName("CPAR");
    carizlazCPAR.setSqlType(4);
    carizlazCPAR.setWidth(5);
    carizlazCSKL.setCaption("Skladište");
    carizlazCSKL.setColumnName("CSKL");
    carizlazCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazCSKL.setPrecision(12);
    carizlazCSKL.setTableName("CARIZLAZ");
    carizlazCSKL.setServerColumnName("CSKL");
    carizlazCSKL.setSqlType(1);
    carizlazRACPRI.setCaption("Ra\u010Dun principala");
    carizlazRACPRI.setColumnName("RACPRI");
    carizlazRACPRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazRACPRI.setPrecision(30);
    carizlazRACPRI.setTableName("CARIZLAZ");
    carizlazRACPRI.setServerColumnName("RACPRI");
    carizlazRACPRI.setSqlType(1);
    carizlazRACPRI.setWidth(30);
    carizlazOZNVAL.setCaption("Valuta");
    carizlazOZNVAL.setColumnName("OZNVAL");
    carizlazOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazOZNVAL.setPrecision(3);
    carizlazOZNVAL.setTableName("CARIZLAZ");
    carizlazOZNVAL.setServerColumnName("OZNVAL");
    carizlazOZNVAL.setSqlType(1);
    carizlazTECAJ.setCaption("Iznos te\u010Daja");
    carizlazTECAJ.setColumnName("TECAJ");
    carizlazTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    carizlazTECAJ.setPrecision(17);
    carizlazTECAJ.setScale(6);
    carizlazTECAJ.setDisplayMask("###,###,##0.000000");
    carizlazTECAJ.setDefault("0");
    carizlazTECAJ.setTableName("CARIZLAZ");
    carizlazTECAJ.setServerColumnName("TECAJ");
    carizlazTECAJ.setSqlType(2);
    carizlazODGOSOBA.setCaption("Odgovorna osoba");
    carizlazODGOSOBA.setColumnName("ODGOSOBA");
    carizlazODGOSOBA.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazODGOSOBA.setPrecision(80);
    carizlazODGOSOBA.setTableName("CARIZLAZ");
    carizlazODGOSOBA.setServerColumnName("ODGOSOBA");
    carizlazODGOSOBA.setSqlType(1);
    carizlazODGOSOBA.setWidth(30);
    carizlazCOMMENT.setCaption("Komentar");
    carizlazCOMMENT.setColumnName("COMMENT");
    carizlazCOMMENT.setDataType(com.borland.dx.dataset.Variant.STRING);
    carizlazCOMMENT.setPrecision(500);
    carizlazCOMMENT.setTableName("CARIZLAZ");
    carizlazCOMMENT.setServerColumnName("COMMENT");
    carizlazCOMMENT.setSqlType(1);
    carizlazCOMMENT.setWidth(30);
    carizlaz.setResolver(dm.getQresolver());
    carizlaz.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carizlaz", null, true, Load.ALL));
    setColumns(new Column[] {carizlazLOCK, carizlazAKTIV, carizlazCUSER, carizlazID_IZLAZ_ZAG, carizlazCBRDOK, carizlazDATDOK, carizlazCPAR, 
        carizlazCSKL, carizlazRACPRI, carizlazOZNVAL, carizlazTECAJ, carizlazODGOSOBA, carizlazCOMMENT});
  }

  public void setall() {

    ddl.create("Carizlaz")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cuser", 15)
       .addInteger("id_izlaz_zag", 8, true)
       .addChar("cbrdok", 30)
       .addDate("datdok")
       .addInteger("cpar", 5)
       .addChar("cskl", 12)
       .addChar("racpri", 30)
       .addChar("oznval", 3)
       .addFloat("tecaj", 17, 6)
       .addChar("odgosoba", 80)
       .addChar("comment", 500)
       .addPrimaryKey("id_izlaz_zag");


    Naziv = "Carizlaz";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"id_izlaz_zag"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
