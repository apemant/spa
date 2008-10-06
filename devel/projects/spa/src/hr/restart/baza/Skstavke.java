/****license*****************************************************************
**   file: Skstavke.java
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



public class Skstavke extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Skstavke Skstavkeclass;

  QueryDataSet skstavke = new raDataSet();
  QueryDataSet skstavkeskcover = new raDataSet();
  QueryDataSet skstavkeskbase = new raDataSet();

  /*Column skstavkeLOKK = new Column();
  Column skstavkeAKTIV = new Column();
  Column skstavkeKNJIG = new Column();
  Column skstavkeCPAR = new Column();
  Column skstavkeVRDOK = new Column();
  Column skstavkeBROJKONTA = new Column();
  Column skstavkeBROJDOK = new Column();
  Column skstavkeBROJIZV = new Column();
  Column skstavkeCORG = new Column();
  Column skstavkeCAGENT = new Column();
  Column skstavkeDATUMKNJ = new Column();
  Column skstavkeDATDOK = new Column();
  Column skstavkeDATDOSP = new Column();
  Column skstavkeEXTBRDOK = new Column();
  Column skstavkeCNACPL = new Column();
  Column skstavkeOZNVAL = new Column();
  Column skstavkeTECAJ = new Column();
  Column skstavkeOPIS = new Column();
  Column skstavkeID = new Column();
  Column skstavkeIP = new Column();
  Column skstavkePOKRIVENO = new Column();
  Column skstavkeSSALDO = new Column();
  Column skstavkeSALDO = new Column();
  Column skstavkeKAMSTOPA = new Column();
  Column skstavkeCSKSTAVKE = new Column();
  Column skstavkeCGKSTAVKE = new Column();
  Column skstavkeCKNJIGE = new Column();
  Column skstavkeDATPRI = new Column();
  Column skstavkeDATUNOS = new Column();
  Column skstavkePVID = new Column();
  Column skstavkePVIP = new Column();
  Column skstavkePVSSALDO = new Column();
  Column skstavkePVSALDO = new Column();
  Column skstavkePVPOK = new Column();
  Column skstavkeRSALDO = new Column();
  Column skstavkeZIRO = new Column();
  Column skstavkeSTAVKA = new Column();
  Column skstavkeCSKL = new Column();
  Column skstavkePOZIV = new Column();*/

  public static Skstavke getDataModule() {
    if (Skstavkeclass == null) {
      Skstavkeclass = new Skstavke();
    }
    return Skstavkeclass;
  }

  public QueryDataSet getQueryDataSet() {
    return skstavke;
  }

  public QueryDataSet getSkcover() {
    return skstavkeskcover;
  }

  public QueryDataSet getSkbase() {
    return skstavkeskbase;
  }

  public Skstavke() {
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
    /*skstavkeLOKK.setCaption("Status zauzetosti");
    skstavkeLOKK.setColumnName("LOKK");
    skstavkeLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeLOKK.setPrecision(1);
    skstavkeLOKK.setTableName("SKSTAVKE");
    skstavkeLOKK.setServerColumnName("LOKK");
    skstavkeLOKK.setSqlType(1);
    skstavkeLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skstavkeLOKK.setDefault("N");
    skstavkeAKTIV.setCaption("Aktivan - neaktivan");
    skstavkeAKTIV.setColumnName("AKTIV");
    skstavkeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeAKTIV.setPrecision(1);
    skstavkeAKTIV.setTableName("SKSTAVKE");
    skstavkeAKTIV.setServerColumnName("AKTIV");
    skstavkeAKTIV.setSqlType(1);
    skstavkeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skstavkeAKTIV.setDefault("D");
    skstavkeKNJIG.setCaption("Knjigovodstvo");
    skstavkeKNJIG.setColumnName("KNJIG");
    skstavkeKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeKNJIG.setPrecision(12);
    skstavkeKNJIG.setRowId(true);
    skstavkeKNJIG.setTableName("SKSTAVKE");
    skstavkeKNJIG.setServerColumnName("KNJIG");
    skstavkeKNJIG.setSqlType(1);
    skstavkeCPAR.setCaption("Partner");
    skstavkeCPAR.setColumnName("CPAR");
    skstavkeCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    skstavkeCPAR.setPrecision(6);
    skstavkeCPAR.setRowId(true);
    skstavkeCPAR.setTableName("SKSTAVKE");
    skstavkeCPAR.setServerColumnName("CPAR");
    skstavkeCPAR.setSqlType(4);
    skstavkeCPAR.setWidth(6);
    skstavkeVRDOK.setCaption("VD");
    skstavkeVRDOK.setColumnName("VRDOK");
    skstavkeVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeVRDOK.setPrecision(3);
    skstavkeVRDOK.setRowId(true);
    skstavkeVRDOK.setTableName("SKSTAVKE");
    skstavkeVRDOK.setServerColumnName("VRDOK");
    skstavkeVRDOK.setSqlType(1);
    skstavkeVRDOK.setWidth(5);
    skstavkeBROJKONTA.setCaption("Konto");
    skstavkeBROJKONTA.setColumnName("BROJKONTA");
    skstavkeBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeBROJKONTA.setPrecision(8);
    skstavkeBROJKONTA.setRowId(true);
    skstavkeBROJKONTA.setTableName("SKSTAVKE");
    skstavkeBROJKONTA.setServerColumnName("BROJKONTA");
    skstavkeBROJKONTA.setSqlType(1);
    skstavkeBROJDOK.setCaption("Broj dokumenta");
    skstavkeBROJDOK.setColumnName("BROJDOK");
    skstavkeBROJDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeBROJDOK.setPrecision(50);
    skstavkeBROJDOK.setRowId(true);
    skstavkeBROJDOK.setTableName("SKSTAVKE");
    skstavkeBROJDOK.setServerColumnName("BROJDOK");
    skstavkeBROJDOK.setSqlType(1);
    skstavkeBROJDOK.setWidth(20);
    skstavkeBROJIZV.setCaption("Broj izvoda");
    skstavkeBROJIZV.setColumnName("BROJIZV");
    skstavkeBROJIZV.setDataType(com.borland.dx.dataset.Variant.INT);
    skstavkeBROJIZV.setPrecision(6);
    skstavkeBROJIZV.setRowId(true);
    skstavkeBROJIZV.setTableName("SKSTAVKE");
    skstavkeBROJIZV.setServerColumnName("BROJIZV");
    skstavkeBROJIZV.setSqlType(4);
    skstavkeBROJIZV.setWidth(6);
    skstavkeCORG.setCaption("Org. jedinica");
    skstavkeCORG.setColumnName("CORG");
    skstavkeCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeCORG.setPrecision(12);
    skstavkeCORG.setTableName("SKSTAVKE");
    skstavkeCORG.setServerColumnName("CORG");
    skstavkeCORG.setSqlType(1);
    skstavkeCAGENT.setCaption("Agent");
    skstavkeCAGENT.setColumnName("CAGENT");
    skstavkeCAGENT.setDataType(com.borland.dx.dataset.Variant.INT);
    skstavkeCAGENT.setPrecision(6);
    skstavkeCAGENT.setTableName("SKSTAVKE");
    skstavkeCAGENT.setServerColumnName("CAGENT");
    skstavkeCAGENT.setSqlType(4);
    skstavkeCAGENT.setWidth(6);
    skstavkeDATUMKNJ.setCaption("Datum knjiženja");
    skstavkeDATUMKNJ.setColumnName("DATUMKNJ");
    skstavkeDATUMKNJ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skstavkeDATUMKNJ.setPrecision(8);
    skstavkeDATUMKNJ.setDisplayMask("dd-MM-yyyy");
//    skstavkeDATUMKNJ.setEditMask("dd-MM-yyyy");
    skstavkeDATUMKNJ.setTableName("SKSTAVKE");
    skstavkeDATUMKNJ.setServerColumnName("DATUMKNJ");
    skstavkeDATUMKNJ.setSqlType(93);
    skstavkeDATUMKNJ.setWidth(10);
    skstavkeDATDOK.setCaption("Datum");
    skstavkeDATDOK.setColumnName("DATDOK");
    skstavkeDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skstavkeDATDOK.setPrecision(8);
    skstavkeDATDOK.setDisplayMask("dd-MM-yyyy");
//    skstavkeDATDOK.setEditMask("dd-MM-yyyy");
    skstavkeDATDOK.setTableName("SKSTAVKE");
    skstavkeDATDOK.setServerColumnName("DATDOK");
    skstavkeDATDOK.setSqlType(93);
    skstavkeDATDOK.setWidth(10);
    skstavkeDATDOSP.setCaption("Datun dospje\u0107a");
    skstavkeDATDOSP.setColumnName("DATDOSP");
    skstavkeDATDOSP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skstavkeDATDOSP.setPrecision(8);
    skstavkeDATDOSP.setDisplayMask("dd-MM-yyyy");
//    skstavkeDATDOSP.setEditMask("dd-MM-yyyy");
    skstavkeDATDOSP.setTableName("SKSTAVKE");
    skstavkeDATDOSP.setServerColumnName("DATDOSP");
    skstavkeDATDOSP.setSqlType(93);
    skstavkeDATDOSP.setWidth(10);
    skstavkeEXTBRDOK.setCaption("Dodatni broj dokumenta");
    skstavkeEXTBRDOK.setColumnName("EXTBRDOK");
    skstavkeEXTBRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeEXTBRDOK.setPrecision(15);
    skstavkeEXTBRDOK.setTableName("SKSTAVKE");
    skstavkeEXTBRDOK.setServerColumnName("EXTBRDOK");
    skstavkeEXTBRDOK.setSqlType(1);
    skstavkeCNACPL.setCaption("Na\u010Din pla\u0107anja");
    skstavkeCNACPL.setColumnName("CNACPL");
    skstavkeCNACPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeCNACPL.setPrecision(3);
    skstavkeCNACPL.setTableName("SKSTAVKE");
    skstavkeCNACPL.setServerColumnName("CNACPL");
    skstavkeCNACPL.setSqlType(1);
    skstavkeOZNVAL.setCaption("Valuta");
    skstavkeOZNVAL.setColumnName("OZNVAL");
    skstavkeOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeOZNVAL.setPrecision(3);
    skstavkeOZNVAL.setTableName("SKSTAVKE");
    skstavkeOZNVAL.setServerColumnName("OZNVAL");
    skstavkeOZNVAL.setSqlType(1);
    skstavkeTECAJ.setCaption("Te\u010Daj");
    skstavkeTECAJ.setColumnName("TECAJ");
    skstavkeTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstavkeTECAJ.setPrecision(17);
    skstavkeTECAJ.setScale(6);
    skstavkeTECAJ.setDisplayMask("###,###,##0.000000");
    skstavkeTECAJ.setDefault("0");
    skstavkeTECAJ.setTableName("SKSTAVKE");
    skstavkeTECAJ.setServerColumnName("TECAJ");
    skstavkeTECAJ.setSqlType(2);
    skstavkeOPIS.setCaption("Opis stavke");
    skstavkeOPIS.setColumnName("OPIS");
    skstavkeOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeOPIS.setPrecision(100);
    skstavkeOPIS.setTableName("SKSTAVKE");
    skstavkeOPIS.setServerColumnName("OPIS");
    skstavkeOPIS.setSqlType(1);
    skstavkeOPIS.setWidth(30);
    skstavkeID.setCaption("Duguje");
    skstavkeID.setColumnName("ID");
    skstavkeID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstavkeID.setPrecision(17);
    skstavkeID.setScale(2);
    skstavkeID.setDisplayMask("###,###,##0.00");
    skstavkeID.setDefault("0");
    skstavkeID.setTableName("SKSTAVKE");
    skstavkeID.setServerColumnName("ID");
    skstavkeID.setSqlType(2);
    skstavkeID.setDefault("0");
    skstavkeIP.setCaption("Potražuje");
    skstavkeIP.setColumnName("IP");
    skstavkeIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstavkeIP.setPrecision(17);
    skstavkeIP.setScale(2);
    skstavkeIP.setDisplayMask("###,###,##0.00");
    skstavkeIP.setDefault("0");
    skstavkeIP.setTableName("SKSTAVKE");
    skstavkeIP.setServerColumnName("IP");
    skstavkeIP.setSqlType(2);
    skstavkeIP.setDefault("0");
    skstavkePOKRIVENO.setCaption("Pokriven");
    skstavkePOKRIVENO.setColumnName("POKRIVENO");
    skstavkePOKRIVENO.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkePOKRIVENO.setPrecision(1);
    skstavkePOKRIVENO.setTableName("SKSTAVKE");
    skstavkePOKRIVENO.setServerColumnName("POKRIVENO");
    skstavkePOKRIVENO.setSqlType(1);
    skstavkePOKRIVENO.setDefault("N");
    skstavkeSSALDO.setCaption("Iznos");
    skstavkeSSALDO.setColumnName("SSALDO");
    skstavkeSSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstavkeSSALDO.setPrecision(17);
    skstavkeSSALDO.setScale(2);
    skstavkeSSALDO.setDisplayMask("###,###,##0.00");
    skstavkeSSALDO.setDefault("0");
    skstavkeSSALDO.setTableName("SKSTAVKE");
    skstavkeSSALDO.setServerColumnName("SSALDO");
    skstavkeSSALDO.setSqlType(2);
    skstavkeSSALDO.setDefault("0");
    skstavkeSALDO.setCaption("Saldo");
    skstavkeSALDO.setColumnName("SALDO");
    skstavkeSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstavkeSALDO.setPrecision(17);
    skstavkeSALDO.setScale(2);
    skstavkeSALDO.setDisplayMask("###,###,##0.00");
    skstavkeSALDO.setDefault("0");
    skstavkeSALDO.setTableName("SKSTAVKE");
    skstavkeSALDO.setServerColumnName("SALDO");
    skstavkeSALDO.setSqlType(2);
    skstavkeSALDO.setDefault("0");
    skstavkeKAMSTOPA.setCaption("Stopa kamata");
    skstavkeKAMSTOPA.setColumnName("KAMSTOPA");
    skstavkeKAMSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstavkeKAMSTOPA.setPrecision(5);
    skstavkeKAMSTOPA.setScale(2);
    skstavkeKAMSTOPA.setDisplayMask("###,###,##0.00");
    skstavkeKAMSTOPA.setDefault("0");
    skstavkeKAMSTOPA.setTableName("SKSTAVKE");
    skstavkeKAMSTOPA.setServerColumnName("KAMSTOPA");
    skstavkeKAMSTOPA.setSqlType(2);
    skstavkeKAMSTOPA.setDefault("0");
    skstavkeCSKSTAVKE.setCaption("KNJIG-CPAR-STAVKA-CSKL-VRDOK-BROJDOK-BROJIZV");
    skstavkeCSKSTAVKE.setColumnName("CSKSTAVKE");
    skstavkeCSKSTAVKE.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeCSKSTAVKE.setPrecision(90);
    skstavkeCSKSTAVKE.setTableName("SKSTAVKE");
    skstavkeCSKSTAVKE.setServerColumnName("CSKSTAVKE");
    skstavkeCSKSTAVKE.setSqlType(1);
    skstavkeCSKSTAVKE.setWidth(30);
    skstavkeCSKSTAVKE.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skstavkeCGKSTAVKE.setCaption("CNALOGA+RBS gkstavke");
    skstavkeCGKSTAVKE.setColumnName("CGKSTAVKE");
    skstavkeCGKSTAVKE.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeCGKSTAVKE.setPrecision(38);
    skstavkeCGKSTAVKE.setTableName("SKSTAVKE");
    skstavkeCGKSTAVKE.setServerColumnName("CGKSTAVKE");
    skstavkeCGKSTAVKE.setSqlType(1);
    skstavkeCGKSTAVKE.setWidth(30);
    skstavkeCKNJIGE.setCaption("Knjiga");
    skstavkeCKNJIGE.setColumnName("CKNJIGE");
    skstavkeCKNJIGE.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeCKNJIGE.setPrecision(5);
    skstavkeCKNJIGE.setTableName("SKSTAVKE");
    skstavkeCKNJIGE.setServerColumnName("CKNJIGE");
    skstavkeCKNJIGE.setSqlType(1);
    skstavkeDATPRI.setCaption("Datum primitka");
    skstavkeDATPRI.setColumnName("DATPRI");
    skstavkeDATPRI.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skstavkeDATPRI.setPrecision(8);
    skstavkeDATPRI.setDisplayMask("dd-MM-yyyy");
//    skstavkeDATPRI.setEditMask("dd-MM-yyyy");
    skstavkeDATPRI.setTableName("SKSTAVKE");
    skstavkeDATPRI.setServerColumnName("DATPRI");
    skstavkeDATPRI.setSqlType(93);
    skstavkeDATPRI.setWidth(10);
    skstavkeDATUNOS.setCaption("Datum unosa");
    skstavkeDATUNOS.setColumnName("DATUNOS");
    skstavkeDATUNOS.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skstavkeDATUNOS.setPrecision(8);
    skstavkeDATUNOS.setDisplayMask("dd-MM-yyyy");
//    skstavkeDATUNOS.setEditMask("dd-MM-yyyy");
    skstavkeDATUNOS.setTableName("SKSTAVKE");
    skstavkeDATUNOS.setServerColumnName("DATUNOS");
    skstavkeDATUNOS.setSqlType(93);
    skstavkeDATUNOS.setWidth(10);
    skstavkePVID.setCaption("Dugovni iznos u valuti");
    skstavkePVID.setColumnName("PVID");
    skstavkePVID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstavkePVID.setPrecision(17);
    skstavkePVID.setScale(2);
    skstavkePVID.setDisplayMask("###,###,##0.00");
    skstavkePVID.setDefault("0");
    skstavkePVID.setTableName("SKSTAVKE");
    skstavkePVID.setServerColumnName("PVID");
    skstavkePVID.setSqlType(2);
    skstavkePVID.setDefault("0");
    skstavkePVIP.setCaption("Potražni iznos u valuti");
    skstavkePVIP.setColumnName("PVIP");
    skstavkePVIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstavkePVIP.setPrecision(17);
    skstavkePVIP.setScale(2);
    skstavkePVIP.setDisplayMask("###,###,##0.00");
    skstavkePVIP.setDefault("0");
    skstavkePVIP.setTableName("SKSTAVKE");
    skstavkePVIP.setServerColumnName("PVIP");
    skstavkePVIP.setSqlType(2);
    skstavkePVIP.setDefault("0");
    skstavkePVSSALDO.setCaption("Iznos u valuti");
    skstavkePVSSALDO.setColumnName("PVSSALDO");
    skstavkePVSSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstavkePVSSALDO.setPrecision(17);
    skstavkePVSSALDO.setScale(2);
    skstavkePVSSALDO.setDisplayMask("###,###,##0.00");
    skstavkePVSSALDO.setDefault("0");
    skstavkePVSSALDO.setTableName("SKSTAVKE");
    skstavkePVSSALDO.setServerColumnName("PVSSALDO");
    skstavkePVSSALDO.setSqlType(2);
    skstavkePVSSALDO.setDefault("0");
    skstavkePVSALDO.setCaption("Saldo u valuti");
    skstavkePVSALDO.setColumnName("PVSALDO");
    skstavkePVSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstavkePVSALDO.setPrecision(17);
    skstavkePVSALDO.setScale(2);
    skstavkePVSALDO.setDisplayMask("###,###,##0.00");
    skstavkePVSALDO.setDefault("0");
    skstavkePVSALDO.setTableName("SKSTAVKE");
    skstavkePVSALDO.setServerColumnName("PVSALDO");
    skstavkePVSALDO.setSqlType(2);
    skstavkePVSALDO.setDefault("0");
    skstavkePVPOK.setCaption("Pokriven u valuti");
    skstavkePVPOK.setColumnName("PVPOK");
    skstavkePVPOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkePVPOK.setPrecision(1);
    skstavkePVPOK.setTableName("SKSTAVKE");
    skstavkePVPOK.setServerColumnName("PVPOK");
    skstavkePVPOK.setSqlType(1);
    skstavkePVPOK.setDefault("N");
    skstavkeRSALDO.setCaption("Pokriveno");
    skstavkeRSALDO.setColumnName("RSALDO");
    skstavkeRSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstavkeRSALDO.setPrecision(17);
    skstavkeRSALDO.setScale(2);
    skstavkeRSALDO.setDisplayMask("###,###,##0.00");
    skstavkeRSALDO.setDefault("0");
    skstavkeRSALDO.setTableName("SKSTAVKE");
    skstavkeRSALDO.setServerColumnName("RSALDO");
    skstavkeRSALDO.setSqlType(2);
    skstavkeRSALDO.setDefault("0");
    skstavkeZIRO.setCaption("Žiro ra\u010Dun");
    skstavkeZIRO.setColumnName("ZIRO");
    skstavkeZIRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeZIRO.setPrecision(40);
    skstavkeZIRO.setTableName("SKSTAVKE");
    skstavkeZIRO.setServerColumnName("ZIRO");
    skstavkeZIRO.setSqlType(1);
    skstavkeZIRO.setWidth(30);
    skstavkeSTAVKA.setCaption("Stavka sheme");
    skstavkeSTAVKA.setColumnName("STAVKA");
    skstavkeSTAVKA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    skstavkeSTAVKA.setPrecision(2);
    skstavkeSTAVKA.setTableName("SKSTAVKE");
    skstavkeSTAVKA.setServerColumnName("STAVKA");
    skstavkeSTAVKA.setSqlType(5);
    skstavkeSTAVKA.setWidth(2);
    skstavkeCSKL.setCaption("Vrsta sheme");
    skstavkeCSKL.setColumnName("CSKL");
    skstavkeCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkeCSKL.setPrecision(12);
    skstavkeCSKL.setTableName("SKSTAVKE");
    skstavkeCSKL.setServerColumnName("CSKL");
    skstavkeCSKL.setSqlType(1);
    skstavkePOZIV.setCaption("Poziv na broj");
    skstavkePOZIV.setColumnName("POZIV");
    skstavkePOZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstavkePOZIV.setPrecision(50);
    skstavkePOZIV.setTableName("SKSTAVKE");
    skstavkePOZIV.setServerColumnName("POZIV");
    skstavkePOZIV.setSqlType(1);
    skstavkePOZIV.setWidth(20);
    skstavke.setResolver(dm.getQresolver());
    skstavke.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Skstavke", null, true, Load.ALL));
 setColumns(new Column[] {skstavkeLOKK, skstavkeAKTIV, skstavkeKNJIG, skstavkeCPAR, skstavkeVRDOK, skstavkeBROJKONTA, skstavkeBROJDOK,
        skstavkeBROJIZV, skstavkeCORG, skstavkeCAGENT, skstavkeDATUMKNJ, skstavkeDATDOK, skstavkeDATDOSP, skstavkeEXTBRDOK, skstavkeCNACPL, skstavkeOZNVAL,
        skstavkeTECAJ, skstavkeOPIS, skstavkeID, skstavkeIP, skstavkePOKRIVENO, skstavkeSSALDO, skstavkeSALDO, skstavkeKAMSTOPA, skstavkeCSKSTAVKE,
        skstavkeCGKSTAVKE, skstavkeCKNJIGE, skstavkeDATPRI, skstavkeDATUNOS, skstavkePVID, skstavkePVIP, skstavkePVSSALDO, skstavkePVSALDO, skstavkePVPOK,
        skstavkeRSALDO, skstavkeZIRO, skstavkeSTAVKA, skstavkeCSKL, skstavkePOZIV});
*/
    createFilteredDataSet(skstavkeskcover, "1=0");
    createFilteredDataSet(skstavkeskbase, "1=0");
  }

/*  public void setall() {

    ddl.create("Skstavke")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addInteger("cpar", 6, true)
       .addChar("vrdok", 3, true)
       .addChar("brojkonta", 8, true)
       .addChar("brojdok", 50, true)
       .addInteger("brojizv", 6, true)
       .addChar("corg", 12)
       .addInteger("cagent", 6)
       .addDate("datumknj")
       .addDate("datdok")
       .addDate("datdosp")
       .addChar("extbrdok", 15)
       .addChar("cnacpl", 3)
       .addChar("oznval", 3)
       .addFloat("tecaj", 17, 6)
       .addChar("opis", 100)
       .addFloat("id", 17, 2)
       .addFloat("ip", 17, 2)
       .addChar("pokriveno", 1, "N")
       .addFloat("ssaldo", 17, 2)
       .addFloat("saldo", 17, 2)
       .addFloat("kamstopa", 5, 2)
       .addChar("cskstavke", 90)
       .addChar("cgkstavke", 38)
       .addChar("cknjige", 5)
       .addDate("datpri")
       .addDate("datunos")
       .addFloat("pvid", 17, 2)
       .addFloat("pvip", 17, 2)
       .addFloat("pvssaldo", 17, 2)
       .addFloat("pvsaldo", 17, 2)
       .addChar("pvpok", 1, "N")
       .addFloat("rsaldo", 17, 2)
       .addChar("ziro", 40)
       .addShort("stavka", 2)
       .addChar("cskl", 12)
       .addChar("poziv", 50)
       .addPrimaryKey("knjig,cpar,vrdok,brojkonta,brojdok,brojizv");


    Naziv = "Skstavke";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"brojdok"};
    String[] uidx = new String[] {"cskstavke"};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }*/
}
