/****license*****************************************************************
**   file: NadzornaKnjiga.java
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



public class NadzornaKnjiga extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static NadzornaKnjiga NadzornaKnjigaclass;

  QueryDataSet nknj = new raDataSet();

  Column nknjLOKK = new Column();
  Column nknjAKTIV = new Column();
  Column nknjKNJIG = new Column();
  Column nknjGOD = new Column();
  Column nknjRBR = new Column();
  Column nknjDATUM = new Column();
  Column nknjBRSTR = new Column();
  Column nknjBRISPRAVE = new Column();
  Column nknjDATDOK = new Column();
  Column nknjCPAR = new Column();
  Column nknjVRIJEDNOST = new Column();
  Column nknjCPARPOSR = new Column();
  Column nknjPROVIZIJA = new Column();
  Column nknjDATUMPL = new Column();
  Column nknjIZNOSPL = new Column();
  Column nknjCNACPL = new Column();
  Column nknjBROJRJES = new Column();
  Column nknjDATUMRJES = new Column();
  Column nknjSTARIRBR = new Column();
  Column nknjSTARAGOD = new Column();
  Column nknjBROJJCD = new Column();
  Column nknjDATUMJCD = new Column();
  Column nknjZEMPODRIJETLA = new Column();

  public static NadzornaKnjiga getDataModule() {
    if (NadzornaKnjigaclass == null) {
      NadzornaKnjigaclass = new NadzornaKnjiga();
    }
    return NadzornaKnjigaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return nknj;
  }

  public NadzornaKnjiga() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    nknjLOKK.setCaption("Status zauzetosti");
    nknjLOKK.setColumnName("LOKK");
    nknjLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    nknjLOKK.setPrecision(1);
    nknjLOKK.setTableName("NADZORNAKNJIGA");
    nknjLOKK.setServerColumnName("LOKK");
    nknjLOKK.setSqlType(1);
    nknjLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    nknjLOKK.setDefault("N");
    nknjAKTIV.setCaption("Aktivan - neaktivan");
    nknjAKTIV.setColumnName("AKTIV");
    nknjAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    nknjAKTIV.setPrecision(1);
    nknjAKTIV.setTableName("NADZORNAKNJIGA");
    nknjAKTIV.setServerColumnName("AKTIV");
    nknjAKTIV.setSqlType(1);
    nknjAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    nknjAKTIV.setDefault("D");
    nknjKNJIG.setCaption("Knjigovodstvo");
    nknjKNJIG.setColumnName("KNJIG");
    nknjKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    nknjKNJIG.setPrecision(12);
    nknjKNJIG.setRowId(true);
    nknjKNJIG.setTableName("NADZORNAKNJIGA");
    nknjKNJIG.setServerColumnName("KNJIG");
    nknjKNJIG.setSqlType(1);
    nknjGOD.setCaption("Godina");
    nknjGOD.setColumnName("GOD");
    nknjGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    nknjGOD.setPrecision(4);
    nknjGOD.setRowId(true);
    nknjGOD.setTableName("NADZORNAKNJIGA");
    nknjGOD.setServerColumnName("GOD");
    nknjGOD.setSqlType(1);
    nknjRBR.setCaption("Redni broj unosa");
    nknjRBR.setColumnName("RBR");
    nknjRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    nknjRBR.setPrecision(8);
    nknjRBR.setRowId(true);
    nknjRBR.setTableName("NADZORNAKNJIGA");
    nknjRBR.setServerColumnName("RBR");
    nknjRBR.setSqlType(4);
    nknjRBR.setWidth(8);
    nknjDATUM.setCaption("Datum unosa");
    nknjDATUM.setColumnName("DATUM");
    nknjDATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    nknjDATUM.setPrecision(8);
    nknjDATUM.setDisplayMask("dd-MM-yyyy");
//    nknjDATUM.setEditMask("dd-MM-yyyy");
    nknjDATUM.setTableName("NADZORNAKNJIGA");
    nknjDATUM.setServerColumnName("DATUM");
    nknjDATUM.setSqlType(93);
    nknjDATUM.setWidth(10);
    nknjBRSTR.setCaption("Broj stranice");
    nknjBRSTR.setColumnName("BRSTR");
    nknjBRSTR.setDataType(com.borland.dx.dataset.Variant.INT);
    nknjBRSTR.setPrecision(8);
    nknjBRSTR.setTableName("NADZORNAKNJIGA");
    nknjBRSTR.setServerColumnName("BRSTR");
    nknjBRSTR.setSqlType(4);
    nknjBRSTR.setWidth(8);
    nknjBRISPRAVE.setCaption("Broj dokumenta – osnove pla\u0107anja");
    nknjBRISPRAVE.setColumnName("BRISPRAVE");
    nknjBRISPRAVE.setDataType(com.borland.dx.dataset.Variant.STRING);
    nknjBRISPRAVE.setPrecision(15);
    nknjBRISPRAVE.setTableName("NADZORNAKNJIGA");
    nknjBRISPRAVE.setServerColumnName("BRISPRAVE");
    nknjBRISPRAVE.setSqlType(1);
    nknjDATDOK.setCaption("Datum dokumenta – osnove pla\u0107anja");
    nknjDATDOK.setColumnName("DATDOK");
    nknjDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    nknjDATDOK.setPrecision(8);
    nknjDATDOK.setDisplayMask("dd-MM-yyyy");
//    nknjDATDOK.setEditMask("dd-MM-yyyy");
    nknjDATDOK.setTableName("NADZORNAKNJIGA");
    nknjDATDOK.setServerColumnName("DATDOK");
    nknjDATDOK.setSqlType(93);
    nknjDATDOK.setWidth(10);
    nknjCPAR.setCaption("Partner");
    nknjCPAR.setColumnName("CPAR");
    nknjCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    nknjCPAR.setPrecision(6);
    nknjCPAR.setTableName("NADZORNAKNJIGA");
    nknjCPAR.setServerColumnName("CPAR");
    nknjCPAR.setSqlType(4);
    nknjCPAR.setWidth(6);
    nknjVRIJEDNOST.setCaption("Ugovorena vrijednost posla");
    nknjVRIJEDNOST.setColumnName("VRIJEDNOST");
    nknjVRIJEDNOST.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    nknjVRIJEDNOST.setPrecision(17);
    nknjVRIJEDNOST.setScale(2);
    nknjVRIJEDNOST.setDisplayMask("###,###,##0.00");
    nknjVRIJEDNOST.setDefault("0");
    nknjVRIJEDNOST.setTableName("NADZORNAKNJIGA");
    nknjVRIJEDNOST.setServerColumnName("VRIJEDNOST");
    nknjVRIJEDNOST.setSqlType(2);
    nknjCPARPOSR.setCaption("Partner u \u010Dije ime je sklopljen posao");
    nknjCPARPOSR.setColumnName("CPARPOSR");
    nknjCPARPOSR.setDataType(com.borland.dx.dataset.Variant.INT);
    nknjCPARPOSR.setPrecision(6);
    nknjCPARPOSR.setTableName("NADZORNAKNJIGA");
    nknjCPARPOSR.setServerColumnName("CPARPOSR");
    nknjCPARPOSR.setSqlType(4);
    nknjCPARPOSR.setWidth(6);
    nknjPROVIZIJA.setCaption("Postotak provizije");
    nknjPROVIZIJA.setColumnName("PROVIZIJA");
    nknjPROVIZIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    nknjPROVIZIJA.setPrecision(17);
    nknjPROVIZIJA.setScale(2);
    nknjPROVIZIJA.setDisplayMask("###,###,##0.00");
    nknjPROVIZIJA.setDefault("0");
    nknjPROVIZIJA.setTableName("NADZORNAKNJIGA");
    nknjPROVIZIJA.setServerColumnName("PROVIZIJA");
    nknjPROVIZIJA.setSqlType(2);
    nknjDATUMPL.setCaption("Datum pla\u0107anja");
    nknjDATUMPL.setColumnName("DATUMPL");
    nknjDATUMPL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    nknjDATUMPL.setPrecision(8);
    nknjDATUMPL.setDisplayMask("dd-MM-yyyy");
//    nknjDATUMPL.setEditMask("dd-MM-yyyy");
    nknjDATUMPL.setTableName("NADZORNAKNJIGA");
    nknjDATUMPL.setServerColumnName("DATUMPL");
    nknjDATUMPL.setSqlType(93);
    nknjDATUMPL.setWidth(10);
    nknjIZNOSPL.setCaption("Iznos pla\u0107anja");
    nknjIZNOSPL.setColumnName("IZNOSPL");
    nknjIZNOSPL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    nknjIZNOSPL.setPrecision(17);
    nknjIZNOSPL.setScale(2);
    nknjIZNOSPL.setDisplayMask("###,###,##0.00");
    nknjIZNOSPL.setDefault("0");
    nknjIZNOSPL.setTableName("NADZORNAKNJIGA");
    nknjIZNOSPL.setServerColumnName("IZNOSPL");
    nknjIZNOSPL.setSqlType(2);
    nknjCNACPL.setCaption("Na\u010Din pla\u0107anja");
    nknjCNACPL.setColumnName("CNACPL");
    nknjCNACPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    nknjCNACPL.setPrecision(3);
    nknjCNACPL.setTableName("NADZORNAKNJIGA");
    nknjCNACPL.setServerColumnName("CNACPL");
    nknjCNACPL.setSqlType(1);
    nknjBROJRJES.setCaption("Oznaka rješenja / odobrenja");
    nknjBROJRJES.setColumnName("BROJRJES");
    nknjBROJRJES.setDataType(com.borland.dx.dataset.Variant.STRING);
    nknjBROJRJES.setPrecision(35);
    nknjBROJRJES.setTableName("NADZORNAKNJIGA");
    nknjBROJRJES.setServerColumnName("BROJRJES");
    nknjBROJRJES.setSqlType(1);
    nknjBROJRJES.setWidth(30);
    nknjDATUMRJES.setCaption("Datum rješenja / odobrenja");
    nknjDATUMRJES.setColumnName("DATUMRJES");
    nknjDATUMRJES.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    nknjDATUMRJES.setPrecision(8);
    nknjDATUMRJES.setDisplayMask("dd-MM-yyyy");
//    nknjDATUMRJES.setEditMask("dd-MM-yyyy");
    nknjDATUMRJES.setTableName("NADZORNAKNJIGA");
    nknjDATUMRJES.setServerColumnName("DATUMRJES");
    nknjDATUMRJES.setSqlType(93);
    nknjDATUMRJES.setWidth(10);
    nknjSTARIRBR.setCaption("Oznaka rednog broja iz prijašnjih nadzornih knjiga");
    nknjSTARIRBR.setColumnName("STARIRBR");
    nknjSTARIRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    nknjSTARIRBR.setPrecision(8);
    nknjSTARIRBR.setTableName("NADZORNAKNJIGA");
    nknjSTARIRBR.setServerColumnName("STARIRBR");
    nknjSTARIRBR.setSqlType(4);
    nknjSTARIRBR.setWidth(8);
    nknjSTARAGOD.setCaption("Godina prijašnje nadzorne knjige");
    nknjSTARAGOD.setColumnName("STARAGOD");
    nknjSTARAGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    nknjSTARAGOD.setPrecision(4);
    nknjSTARAGOD.setTableName("NADZORNAKNJIGA");
    nknjSTARAGOD.setServerColumnName("STARAGOD");
    nknjSTARAGOD.setSqlType(1);
    nknjBROJJCD.setCaption("Broj JCD");
    nknjBROJJCD.setColumnName("BROJJCD");
    nknjBROJJCD.setDataType(com.borland.dx.dataset.Variant.STRING);
    nknjBROJJCD.setPrecision(35);
    nknjBROJJCD.setTableName("NADZORNAKNJIGA");
    nknjBROJJCD.setServerColumnName("BROJJCD");
    nknjBROJJCD.setSqlType(1);
    nknjBROJJCD.setWidth(30);
    nknjDATUMJCD.setCaption("Datum JCD");
    nknjDATUMJCD.setColumnName("DATUMJCD");
    nknjDATUMJCD.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    nknjDATUMJCD.setPrecision(8);
    nknjDATUMJCD.setDisplayMask("dd-MM-yyyy");
//    nknjDATUMJCD.setEditMask("dd-MM-yyyy");
    nknjDATUMJCD.setTableName("NADZORNAKNJIGA");
    nknjDATUMJCD.setServerColumnName("DATUMJCD");
    nknjDATUMJCD.setSqlType(93);
    nknjDATUMJCD.setWidth(10);
    nknjZEMPODRIJETLA.setCaption("Zemlja podrijetla robe");
    nknjZEMPODRIJETLA.setColumnName("ZEMPODRIJETLA");
    nknjZEMPODRIJETLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    nknjZEMPODRIJETLA.setPrecision(3);
    nknjZEMPODRIJETLA.setTableName("NADZORNAKNJIGA");
    nknjZEMPODRIJETLA.setServerColumnName("ZEMPODRIJETLA");
    nknjZEMPODRIJETLA.setSqlType(1);
    nknj.setResolver(dm.getQresolver());
    nknj.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from NadzornaKnjiga", null, true, Load.ALL));
    setColumns(new Column[] {nknjLOKK, nknjAKTIV, nknjKNJIG, nknjGOD, nknjRBR, nknjDATUM, nknjBRSTR, nknjBRISPRAVE, nknjDATDOK, nknjCPAR, nknjVRIJEDNOST,
        nknjCPARPOSR, nknjPROVIZIJA, nknjDATUMPL, nknjIZNOSPL, nknjCNACPL, nknjBROJRJES, nknjDATUMRJES, nknjSTARIRBR, nknjSTARAGOD, nknjBROJJCD, nknjDATUMJCD,
        nknjZEMPODRIJETLA});
  }

  public void setall() {

    ddl.create("NadzornaKnjiga")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addChar("god", 4, true)
       .addInteger("rbr", 8, true)
       .addDate("datum")
       .addInteger("brstr", 8)
       .addChar("brisprave", 15)
       .addDate("datdok")
       .addInteger("cpar", 6)
       .addFloat("vrijednost", 17, 2)
       .addInteger("cparposr", 6)
       .addFloat("provizija", 17, 2)
       .addDate("datumpl")
       .addFloat("iznospl", 17, 2)
       .addChar("cnacpl", 3)
       .addChar("brojrjes", 35)
       .addDate("datumrjes")
       .addInteger("starirbr", 8)
       .addChar("staragod", 4)
       .addChar("brojjcd", 35)
       .addDate("datumjcd")
       .addChar("zempodrijetla", 3)
       .addPrimaryKey("knjig,god,rbr");


    Naziv = "NadzornaKnjiga";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
