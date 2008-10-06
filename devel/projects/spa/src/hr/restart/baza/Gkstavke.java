/****license*****************************************************************
**   file: Gkstavke.java
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



public class Gkstavke extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Gkstavke Gkstavkeclass;

  QueryDataSet gkstavke = new QueryDataSet();

/*  Column gkstavkeLOKK = new Column();
  Column gkstavkeAKTIV = new Column();
  Column gkstavkeKNJIG = new Column();
  Column gkstavkeGOD = new Column();
  Column gkstavkeCVRNAL = new Column();
  Column gkstavkeRBR = new Column();
  Column gkstavkeRBS = new Column();
  Column gkstavkeBROJKONTA = new Column();
  Column gkstavkeCORG = new Column();
  Column gkstavkeDATUMKNJ = new Column();
  Column gkstavkeDATDOK = new Column();
  Column gkstavkeOPIS = new Column();
  Column gkstavkeID = new Column();
  Column gkstavkeIP = new Column();
  Column gkstavkeCPAR = new Column();
  Column gkstavkeCNALOGA = new Column();
  Column gkstavkeOZNVAL = new Column();
  Column gkstavkeTECAJ = new Column();
  Column gkstavkeDEVID = new Column();
  Column gkstavkeDEVIP = new Column(); */

  public static Gkstavke getDataModule() {
    if (Gkstavkeclass == null) {
      Gkstavkeclass = new Gkstavke();
    }
    return Gkstavkeclass;
  }

  public QueryDataSet getQueryDataSet() {
    return gkstavke;
  }

  public Gkstavke() {
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
    /*gkstavkeLOKK.setCaption("Status zauzetosti");
    gkstavkeLOKK.setColumnName("LOKK");
    gkstavkeLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstavkeLOKK.setPrecision(1);
    gkstavkeLOKK.setTableName("GKSTAVKE");
    gkstavkeLOKK.setServerColumnName("LOKK");
    gkstavkeLOKK.setSqlType(1);
    gkstavkeLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    gkstavkeLOKK.setDefault("N");
    gkstavkeAKTIV.setCaption("Aktivan - neaktivan");
    gkstavkeAKTIV.setColumnName("AKTIV");
    gkstavkeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstavkeAKTIV.setPrecision(1);
    gkstavkeAKTIV.setTableName("GKSTAVKE");
    gkstavkeAKTIV.setServerColumnName("AKTIV");
    gkstavkeAKTIV.setSqlType(1);
    gkstavkeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    gkstavkeAKTIV.setDefault("D");
    gkstavkeKNJIG.setCaption("Knjigovodstvo");
    gkstavkeKNJIG.setColumnName("KNJIG");
    gkstavkeKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstavkeKNJIG.setPrecision(12);
    gkstavkeKNJIG.setRowId(true);
    gkstavkeKNJIG.setTableName("GKSTAVKE");
    gkstavkeKNJIG.setServerColumnName("KNJIG");
    gkstavkeKNJIG.setSqlType(1);
    gkstavkeGOD.setCaption("Godina");
    gkstavkeGOD.setColumnName("GOD");
    gkstavkeGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstavkeGOD.setPrecision(4);
    gkstavkeGOD.setRowId(true);
    gkstavkeGOD.setTableName("GKSTAVKE");
    gkstavkeGOD.setServerColumnName("GOD");
    gkstavkeGOD.setSqlType(1);
    gkstavkeCVRNAL.setCaption("Oznaka vrste naloga");
    gkstavkeCVRNAL.setColumnName("CVRNAL");
    gkstavkeCVRNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstavkeCVRNAL.setPrecision(2);
    gkstavkeCVRNAL.setRowId(true);
    gkstavkeCVRNAL.setTableName("GKSTAVKE");
    gkstavkeCVRNAL.setServerColumnName("CVRNAL");
    gkstavkeCVRNAL.setSqlType(1);
    gkstavkeRBR.setCaption("RBR naloga");
    gkstavkeRBR.setColumnName("RBR");
    gkstavkeRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    gkstavkeRBR.setPrecision(8);
    gkstavkeRBR.setRowId(true);
    gkstavkeRBR.setTableName("GKSTAVKE");
    gkstavkeRBR.setServerColumnName("RBR");
    gkstavkeRBR.setSqlType(4);
    gkstavkeRBR.setWidth(8);
    gkstavkeRBS.setCaption("RBS");
    gkstavkeRBS.setColumnName("RBS");
    gkstavkeRBS.setDataType(com.borland.dx.dataset.Variant.INT);
    gkstavkeRBS.setPrecision(6);
    gkstavkeRBS.setRowId(true);
    gkstavkeRBS.setTableName("GKSTAVKE");
    gkstavkeRBS.setServerColumnName("RBS");
    gkstavkeRBS.setSqlType(4);
    gkstavkeRBS.setWidth(6);
    gkstavkeBROJKONTA.setCaption("Konto");
    gkstavkeBROJKONTA.setColumnName("BROJKONTA");
    gkstavkeBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstavkeBROJKONTA.setPrecision(8);
    gkstavkeBROJKONTA.setTableName("GKSTAVKE");
    gkstavkeBROJKONTA.setServerColumnName("BROJKONTA");
    gkstavkeBROJKONTA.setSqlType(1);
    gkstavkeCORG.setCaption("Org. jedinica");
    gkstavkeCORG.setColumnName("CORG");
    gkstavkeCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstavkeCORG.setPrecision(12);
    gkstavkeCORG.setTableName("GKSTAVKE");
    gkstavkeCORG.setServerColumnName("CORG");
    gkstavkeCORG.setSqlType(1);
    gkstavkeDATUMKNJ.setCaption("Datum knjiženja");
    gkstavkeDATUMKNJ.setColumnName("DATUMKNJ");
    gkstavkeDATUMKNJ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    gkstavkeDATUMKNJ.setPrecision(8);
    gkstavkeDATUMKNJ.setDisplayMask("dd-MM-yyyy");
//    gkstavkeDATUMKNJ.setEditMask("dd-MM-yyyy");
    gkstavkeDATUMKNJ.setTableName("GKSTAVKE");
    gkstavkeDATUMKNJ.setServerColumnName("DATUMKNJ");
    gkstavkeDATUMKNJ.setSqlType(93);
    gkstavkeDATUMKNJ.setWidth(10);
    gkstavkeDATDOK.setCaption("Datum dokumenta");
    gkstavkeDATDOK.setColumnName("DATDOK");
    gkstavkeDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    gkstavkeDATDOK.setPrecision(8);
    gkstavkeDATDOK.setDisplayMask("dd-MM-yyyy");
//    gkstavkeDATDOK.setEditMask("dd-MM-yyyy");
    gkstavkeDATDOK.setTableName("GKSTAVKE");
    gkstavkeDATDOK.setServerColumnName("DATDOK");
    gkstavkeDATDOK.setSqlType(93);
    gkstavkeDATDOK.setWidth(10);
    gkstavkeOPIS.setCaption("Opis stavke");
    gkstavkeOPIS.setColumnName("OPIS");
    gkstavkeOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstavkeOPIS.setPrecision(100);
    gkstavkeOPIS.setTableName("GKSTAVKE");
    gkstavkeOPIS.setServerColumnName("OPIS");
    gkstavkeOPIS.setSqlType(1);
    gkstavkeOPIS.setWidth(30);
    gkstavkeID.setCaption("Iznos duguje");
    gkstavkeID.setColumnName("ID");
    gkstavkeID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkstavkeID.setPrecision(17);
    gkstavkeID.setScale(2);
    gkstavkeID.setDisplayMask("###,###,##0.00");
    gkstavkeID.setDefault("0");
    gkstavkeID.setTableName("GKSTAVKE");
    gkstavkeID.setServerColumnName("ID");
    gkstavkeID.setSqlType(2);
    gkstavkeID.setDefault("0");
    gkstavkeIP.setCaption("Iznos potražuje");
    gkstavkeIP.setColumnName("IP");
    gkstavkeIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkstavkeIP.setPrecision(17);
    gkstavkeIP.setScale(2);
    gkstavkeIP.setDisplayMask("###,###,##0.00");
    gkstavkeIP.setDefault("0");
    gkstavkeIP.setTableName("GKSTAVKE");
    gkstavkeIP.setServerColumnName("IP");
    gkstavkeIP.setSqlType(2);
    gkstavkeIP.setDefault("0");
    gkstavkeCPAR.setCaption("Partner");
    gkstavkeCPAR.setColumnName("CPAR");
    gkstavkeCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    gkstavkeCPAR.setPrecision(6);
    gkstavkeCPAR.setTableName("GKSTAVKE");
    gkstavkeCPAR.setServerColumnName("CPAR");
    gkstavkeCPAR.setSqlType(4);
    gkstavkeCPAR.setWidth(6);
    gkstavkeCNALOGA.setCaption("Broj naloga");
    gkstavkeCNALOGA.setColumnName("CNALOGA");
    gkstavkeCNALOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstavkeCNALOGA.setPrecision(30);
    gkstavkeCNALOGA.setTableName("GKSTAVKE");
    gkstavkeCNALOGA.setServerColumnName("CNALOGA");
    gkstavkeCNALOGA.setSqlType(1);
    gkstavkeCNALOGA.setWidth(30);
    gkstavkeOZNVAL.setCaption("Valuta");
    gkstavkeOZNVAL.setColumnName("OZNVAL");
    gkstavkeOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstavkeOZNVAL.setPrecision(3);
    gkstavkeOZNVAL.setTableName("GKSTAVKE");
    gkstavkeOZNVAL.setServerColumnName("OZNVAL");
    gkstavkeOZNVAL.setSqlType(1);
    gkstavkeTECAJ.setCaption("Te\u010Daj");
    gkstavkeTECAJ.setColumnName("TECAJ");
    gkstavkeTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkstavkeTECAJ.setPrecision(17);
    gkstavkeTECAJ.setScale(6);
    gkstavkeTECAJ.setDisplayMask("###,###,##0.000000");
    gkstavkeTECAJ.setDefault("0");
    gkstavkeTECAJ.setTableName("GKSTAVKE");
    gkstavkeTECAJ.setServerColumnName("TECAJ");
    gkstavkeTECAJ.setSqlType(2);
    gkstavkeTECAJ.setDefault("0");
    gkstavkeDEVID.setCaption("Dugovni iznos u valuti");
    gkstavkeDEVID.setColumnName("DEVID");
    gkstavkeDEVID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkstavkeDEVID.setPrecision(17);
    gkstavkeDEVID.setScale(2);
    gkstavkeDEVID.setDisplayMask("###,###,##0.00");
    gkstavkeDEVID.setDefault("0");
    gkstavkeDEVID.setTableName("GKSTAVKE");
    gkstavkeDEVID.setServerColumnName("DEVID");
    gkstavkeDEVID.setSqlType(2);
    gkstavkeDEVID.setDefault("0");
    gkstavkeDEVIP.setCaption("Potražni iznos u valuti");
    gkstavkeDEVIP.setColumnName("DEVIP");
    gkstavkeDEVIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkstavkeDEVIP.setPrecision(17);
    gkstavkeDEVIP.setScale(2);
    gkstavkeDEVIP.setDisplayMask("###,###,##0.00");
    gkstavkeDEVIP.setDefault("0");
    gkstavkeDEVIP.setTableName("GKSTAVKE");
    gkstavkeDEVIP.setServerColumnName("DEVIP");
    gkstavkeDEVIP.setSqlType(2);
    gkstavkeDEVIP.setDefault("0");
    gkstavke.setResolver(dm.getQresolver());
    gkstavke.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Gkstavke", null, true, Load.ALL));
    setColumns(new Column[] {gkstavkeLOKK, gkstavkeAKTIV, gkstavkeKNJIG, gkstavkeGOD, gkstavkeCVRNAL, gkstavkeRBR, gkstavkeRBS, gkstavkeBROJKONTA,
        gkstavkeCORG, gkstavkeDATUMKNJ, gkstavkeDATDOK, gkstavkeOPIS, gkstavkeID, gkstavkeIP, gkstavkeCPAR, gkstavkeCNALOGA, gkstavkeOZNVAL, gkstavkeTECAJ,
        gkstavkeDEVID, gkstavkeDEVIP});*/
  }

  /*public void setall() {

    ddl.create("Gkstavke")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addChar("god", 4, true)
       .addChar("cvrnal", 2, true)
       .addInteger("rbr", 8, true)
       .addInteger("rbs", 6, true)
       .addChar("brojkonta", 8)
       .addChar("corg", 12)
       .addDate("datumknj")
       .addDate("datdok")
       .addChar("opis", 100)
       .addFloat("id", 17, 2)
       .addFloat("ip", 17, 2)
       .addInteger("cpar", 6)
       .addChar("cnaloga", 30)
       .addChar("oznval", 3)
       .addFloat("tecaj", 17, 6)
       .addFloat("devid", 17, 2)
       .addFloat("devip", 17, 2)
       .addPrimaryKey("knjig,god,cvrnal,rbr,rbs");


    Naziv = "Gkstavke";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }*/
}
