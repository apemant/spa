/****license*****************************************************************
**   file: Skstavkerad.java
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



public class Skstavkerad extends KreirDrop implements DataModule {

  //dM dm  = dM.getDataModule();
  private static Skstavkerad Skstavkeradclass;

  QueryDataSet skstrad = new QueryDataSet();
  QueryDataSet skstradfake = new QueryDataSet();

  /*Column skstradLOKK = new Column();
  Column skstradAKTIV = new Column();
  Column skstradKNJIG = new Column();
  Column skstradCPAR = new Column();
  Column skstradSTAVKA = new Column();
  Column skstradCSKL = new Column();
  Column skstradVRDOK = new Column();
  Column skstradBROJDOK = new Column();
  Column skstradRBS = new Column();
  Column skstradBROJIZV = new Column();
  Column skstradCORG = new Column();
  Column skstradCAGENT = new Column();
  Column skstradDATUMKNJ = new Column();
  Column skstradDATDOK = new Column();
  Column skstradDATDOSP = new Column();
  Column skstradEXTBRDOK = new Column();
  Column skstradCNACPL = new Column();
  Column skstradOZNVAL = new Column();
  Column skstradTECAJ = new Column();
  Column skstradOPIS = new Column();
  Column skstradID = new Column();
  Column skstradIP = new Column();
  Column skstradPOKRIVENO = new Column();
  Column skstradSALDO = new Column();
  Column skstradCKOLONE = new Column();
  Column skstradCKNJIGE = new Column();
  Column skstradURAIRA = new Column();
  Column skstradCGKSTAVKE = new Column();
  Column skstradGODMJ = new Column();
  Column skstradDUGPOT = new Column();
  Column skstradDATPRI = new Column();
  Column skstradDATUNOS = new Column();
  Column skstradPVID = new Column();
  Column skstradPVIP = new Column();
  Column skstradZIRO = new Column();
  Column skstradBROJKONTA = new Column();
  Column skstradURUUID = new Column();*/

  public static Skstavkerad getDataModule() {
    if (Skstavkeradclass == null) {
      Skstavkeradclass = new Skstavkerad();
    }
    return Skstavkeradclass;
  }

  public QueryDataSet getQueryDataSet() {
    return skstrad;
  }

  public QueryDataSet getFake() {
    return skstradfake;
  }

  public Skstavkerad() {
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
/*    skstradLOKK.setCaption("Status zauzetosti");
    skstradLOKK.setColumnName("LOKK");
    skstradLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradLOKK.setPrecision(1);
    skstradLOKK.setTableName("SKSTAVKERAD");
    skstradLOKK.setServerColumnName("LOKK");
    skstradLOKK.setSqlType(1);
    skstradLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skstradLOKK.setDefault("N");
    skstradAKTIV.setCaption("Aktivan - neaktivan");
    skstradAKTIV.setColumnName("AKTIV");
    skstradAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradAKTIV.setPrecision(1);
    skstradAKTIV.setTableName("SKSTAVKERAD");
    skstradAKTIV.setServerColumnName("AKTIV");
    skstradAKTIV.setSqlType(1);
    skstradAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    skstradAKTIV.setDefault("D");
    skstradKNJIG.setCaption("Knjigovodstvo");
    skstradKNJIG.setColumnName("KNJIG");
    skstradKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradKNJIG.setPrecision(12);
    skstradKNJIG.setRowId(true);
    skstradKNJIG.setTableName("SKSTAVKERAD");
    skstradKNJIG.setServerColumnName("KNJIG");
    skstradKNJIG.setSqlType(1);
    skstradCPAR.setCaption("Partner");
    skstradCPAR.setColumnName("CPAR");
    skstradCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    skstradCPAR.setPrecision(6);
    skstradCPAR.setRowId(true);
    skstradCPAR.setTableName("SKSTAVKERAD");
    skstradCPAR.setServerColumnName("CPAR");
    skstradCPAR.setSqlType(4);
    skstradCPAR.setWidth(6);
    skstradSTAVKA.setCaption("Stavka sheme");
    skstradSTAVKA.setColumnName("STAVKA");
    skstradSTAVKA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    skstradSTAVKA.setPrecision(2);
    skstradSTAVKA.setRowId(true);
    skstradSTAVKA.setTableName("SKSTAVKERAD");
    skstradSTAVKA.setServerColumnName("STAVKA");
    skstradSTAVKA.setSqlType(5);
    skstradSTAVKA.setWidth(2);
    skstradCSKL.setCaption("Vrsta sheme");
    skstradCSKL.setColumnName("CSKL");
    skstradCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradCSKL.setPrecision(12);
    skstradCSKL.setRowId(true);
    skstradCSKL.setTableName("SKSTAVKERAD");
    skstradCSKL.setServerColumnName("CSKL");
    skstradCSKL.setSqlType(1);
    skstradVRDOK.setCaption("Vrsta dokumenta");
    skstradVRDOK.setColumnName("VRDOK");
    skstradVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradVRDOK.setPrecision(3);
    skstradVRDOK.setRowId(true);
    skstradVRDOK.setTableName("SKSTAVKERAD");
    skstradVRDOK.setServerColumnName("VRDOK");
    skstradVRDOK.setSqlType(1);
    skstradBROJDOK.setCaption("Broj dokumenta");
    skstradBROJDOK.setColumnName("BROJDOK");
    skstradBROJDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradBROJDOK.setPrecision(50);
    skstradBROJDOK.setRowId(true);
    skstradBROJDOK.setTableName("SKSTAVKERAD");
    skstradBROJDOK.setServerColumnName("BROJDOK");
    skstradBROJDOK.setSqlType(1);
    skstradBROJDOK.setWidth(20);
    skstradRBS.setCaption("RBS");
    skstradRBS.setColumnName("RBS");
    skstradRBS.setDataType(com.borland.dx.dataset.Variant.INT);
    skstradRBS.setPrecision(6);
    skstradRBS.setRowId(true);
    skstradRBS.setTableName("SKSTAVKERAD");
    skstradRBS.setServerColumnName("RBS");
    skstradRBS.setSqlType(4);
    skstradRBS.setWidth(6);
    skstradBROJIZV.setCaption("Broj izvoda");
    skstradBROJIZV.setColumnName("BROJIZV");
    skstradBROJIZV.setDataType(com.borland.dx.dataset.Variant.INT);
    skstradBROJIZV.setPrecision(6);
    skstradBROJIZV.setRowId(true);
    skstradBROJIZV.setTableName("SKSTAVKERAD");
    skstradBROJIZV.setServerColumnName("BROJIZV");
    skstradBROJIZV.setSqlType(4);
    skstradBROJIZV.setWidth(6);
    skstradCORG.setCaption("OJ");
    skstradCORG.setColumnName("CORG");
    skstradCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradCORG.setPrecision(12);
    skstradCORG.setTableName("SKSTAVKERAD");
    skstradCORG.setServerColumnName("CORG");
    skstradCORG.setSqlType(1);

    skstradCAGENT.setCaption("Agent");
    skstradCAGENT.setColumnName("CAGENT");
    skstradCAGENT.setDataType(com.borland.dx.dataset.Variant.INT);
    skstradCAGENT.setTableName("SKSTAVKERAD");
    skstradCAGENT.setWidth(5);
    skstradCAGENT.setServerColumnName("CAGENT");
    skstradCAGENT.setSqlType(4);

    skstradDATUMKNJ.setCaption("Datum knjiženja");
    skstradDATUMKNJ.setColumnName("DATUMKNJ");
    skstradDATUMKNJ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skstradDATUMKNJ.setPrecision(8);
    skstradDATUMKNJ.setDisplayMask("dd-MM-yyyy");
//    skstradDATUMKNJ.setEditMask("dd-MM-yyyy");
    skstradDATUMKNJ.setTableName("SKSTAVKERAD");
    skstradDATUMKNJ.setServerColumnName("DATUMKNJ");
    skstradDATUMKNJ.setSqlType(93);
    skstradDATUMKNJ.setWidth(10);
    skstradDATDOK.setCaption("Datum dokumenta");
    skstradDATDOK.setColumnName("DATDOK");
    skstradDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skstradDATDOK.setPrecision(8);
    skstradDATDOK.setDisplayMask("dd-MM-yyyy");
//    skstradDATDOK.setEditMask("dd-MM-yyyy");
    skstradDATDOK.setTableName("SKSTAVKERAD");
    skstradDATDOK.setServerColumnName("DATDOK");
    skstradDATDOK.setSqlType(93);
    skstradDATDOK.setWidth(10);
    skstradDATDOSP.setCaption("Datum dospje\u0107a");
    skstradDATDOSP.setColumnName("DATDOSP");
    skstradDATDOSP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skstradDATDOSP.setPrecision(8);
    skstradDATDOSP.setDisplayMask("dd-MM-yyyy");
//    skstradDATDOSP.setEditMask("dd-MM-yyyy");
    skstradDATDOSP.setTableName("SKSTAVKERAD");
    skstradDATDOSP.setServerColumnName("DATDOSP");
    skstradDATDOSP.setSqlType(93);
    skstradDATDOSP.setWidth(10);
    skstradEXTBRDOK.setCaption("Dodatni broj dokumenta");
    skstradEXTBRDOK.setColumnName("EXTBRDOK");
    skstradEXTBRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradEXTBRDOK.setPrecision(15);
    skstradEXTBRDOK.setTableName("SKSTAVKERAD");
    skstradEXTBRDOK.setServerColumnName("EXTBRDOK");
    skstradEXTBRDOK.setSqlType(1);
    skstradCNACPL.setCaption("Na\u010Din pla\u0107anja");
    skstradCNACPL.setColumnName("CNACPL");
    skstradCNACPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradCNACPL.setPrecision(3);
    skstradCNACPL.setTableName("SKSTAVKERAD");
    skstradCNACPL.setServerColumnName("CNACPL");
    skstradCNACPL.setSqlType(1);
    skstradOZNVAL.setCaption("Valuta");
    skstradOZNVAL.setColumnName("OZNVAL");
    skstradOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradOZNVAL.setPrecision(3);
    skstradOZNVAL.setTableName("SKSTAVKERAD");
    skstradOZNVAL.setServerColumnName("OZNVAL");
    skstradOZNVAL.setSqlType(1);
    skstradTECAJ.setCaption("Te\u010Daj");
    skstradTECAJ.setColumnName("TECAJ");
    skstradTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstradTECAJ.setPrecision(17);
    skstradTECAJ.setScale(6);
    skstradTECAJ.setDisplayMask("###,###,##0.000000");
    skstradTECAJ.setDefault("0");
    skstradTECAJ.setTableName("SKSTAVKERAD");
    skstradTECAJ.setServerColumnName("TECAJ");
    skstradTECAJ.setSqlType(2);
    skstradTECAJ.setDefault("0");
    skstradOPIS.setCaption("Opis stavke");
    skstradOPIS.setColumnName("OPIS");
    skstradOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradOPIS.setPrecision(100);
    skstradOPIS.setTableName("SKSTAVKERAD");
    skstradOPIS.setServerColumnName("OPIS");
    skstradOPIS.setSqlType(1);
    skstradOPIS.setWidth(30);
    skstradID.setCaption("Duguje");
    skstradID.setColumnName("ID");
    skstradID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstradID.setPrecision(17);
    skstradID.setScale(2);
    skstradID.setDisplayMask("###,###,##0.00");
    skstradID.setDefault("0");
    skstradID.setTableName("SKSTAVKERAD");
    skstradID.setServerColumnName("ID");
    skstradID.setSqlType(2);
    skstradID.setDefault("0");
    skstradIP.setCaption("Potražuje");
    skstradIP.setColumnName("IP");
    skstradIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstradIP.setPrecision(17);
    skstradIP.setScale(2);
    skstradIP.setDisplayMask("###,###,##0.00");
    skstradIP.setDefault("0");
    skstradIP.setTableName("SKSTAVKERAD");
    skstradIP.setServerColumnName("IP");
    skstradIP.setSqlType(2);
    skstradIP.setDefault("0");
    skstradPOKRIVENO.setCaption("Indikator pokrivenosti");
    skstradPOKRIVENO.setColumnName("POKRIVENO");
    skstradPOKRIVENO.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradPOKRIVENO.setPrecision(1);
    skstradPOKRIVENO.setTableName("SKSTAVKERAD");
    skstradPOKRIVENO.setServerColumnName("POKRIVENO");
    skstradPOKRIVENO.setSqlType(1);
    skstradPOKRIVENO.setDefault("N");
    skstradSALDO.setCaption("Saldo stavke");
    skstradSALDO.setColumnName("SALDO");
    skstradSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstradSALDO.setPrecision(17);
    skstradSALDO.setScale(2);
    skstradSALDO.setDisplayMask("###,###,##0.00");
    skstradSALDO.setDefault("0");
    skstradSALDO.setTableName("SKSTAVKERAD");
    skstradSALDO.setServerColumnName("SALDO");
    skstradSALDO.setSqlType(2);
    skstradSALDO.setDefault("0");
    skstradCKOLONE.setCaption("Broj kolone u knjizi");
    skstradCKOLONE.setColumnName("CKOLONE");
    skstradCKOLONE.setDataType(com.borland.dx.dataset.Variant.SHORT);
    skstradCKOLONE.setPrecision(2);
    skstradCKOLONE.setTableName("SKSTAVKERAD");
    skstradCKOLONE.setServerColumnName("CKOLONE");
    skstradCKOLONE.setSqlType(5);
    skstradCKOLONE.setWidth(2);
    skstradCKNJIGE.setCaption("Knjiga");
    skstradCKNJIGE.setColumnName("CKNJIGE");
    skstradCKNJIGE.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradCKNJIGE.setPrecision(5);
    skstradCKNJIGE.setTableName("SKSTAVKERAD");
    skstradCKNJIGE.setServerColumnName("CKNJIGE");
    skstradCKNJIGE.setSqlType(1);
    skstradURAIRA.setCaption("Indikator URA/IRA");
    skstradURAIRA.setColumnName("URAIRA");
    skstradURAIRA.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradURAIRA.setPrecision(1);
    skstradURAIRA.setTableName("SKSTAVKERAD");
    skstradURAIRA.setServerColumnName("URAIRA");
    skstradURAIRA.setSqlType(1);
    skstradCGKSTAVKE.setCaption("CNALOGA+RBS gkstavke");
    skstradCGKSTAVKE.setColumnName("CGKSTAVKE");
    skstradCGKSTAVKE.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradCGKSTAVKE.setPrecision(38);
    skstradCGKSTAVKE.setTableName("SKSTAVKERAD");
    skstradCGKSTAVKE.setServerColumnName("CGKSTAVKE");
    skstradCGKSTAVKE.setSqlType(1);
    skstradCGKSTAVKE.setWidth(30);
    skstradGODMJ.setCaption("Godina + mjesec");
    skstradGODMJ.setColumnName("GODMJ");
    skstradGODMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradGODMJ.setPrecision(6);
    skstradGODMJ.setTableName("SKSTAVKERAD");
    skstradGODMJ.setServerColumnName("GODMJ");
    skstradGODMJ.setSqlType(1);
    skstradDUGPOT.setCaption("Dugovna/potražna");
    skstradDUGPOT.setColumnName("DUGPOT");
    skstradDUGPOT.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradDUGPOT.setPrecision(1);
    skstradDUGPOT.setTableName("SKSTAVKERAD");
    skstradDUGPOT.setServerColumnName("DUGPOT");
    skstradDUGPOT.setSqlType(1);
    skstradDATPRI.setCaption("Datum primitka");
    skstradDATPRI.setColumnName("DATPRI");
    skstradDATPRI.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skstradDATPRI.setPrecision(8);
    skstradDATPRI.setDisplayMask("dd-MM-yyyy");
//    skstradDATPRI.setEditMask("dd-MM-yyyy");
    skstradDATPRI.setTableName("SKSTAVKERAD");
    skstradDATPRI.setServerColumnName("DATPRI");
    skstradDATPRI.setSqlType(93);
    skstradDATPRI.setWidth(10);
    skstradDATUNOS.setCaption("Datum unosa");
    skstradDATUNOS.setColumnName("DATUNOS");
    skstradDATUNOS.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    skstradDATUNOS.setPrecision(8);
    skstradDATUNOS.setDisplayMask("dd-MM-yyyy");
//    skstradDATUNOS.setEditMask("dd-MM-yyyy");
    skstradDATUNOS.setTableName("SKSTAVKERAD");
    skstradDATUNOS.setServerColumnName("DATUNOS");
    skstradDATUNOS.setSqlType(93);
    skstradDATUNOS.setWidth(10);
    skstradPVID.setCaption("Dugovni iznos u kunskoj protuvrijednosti");
    skstradPVID.setColumnName("PVID");
    skstradPVID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstradPVID.setPrecision(17);
    skstradPVID.setScale(2);
    skstradPVID.setDisplayMask("###,###,##0.00");
    skstradPVID.setDefault("0");
    skstradPVID.setTableName("SKSTAVKERAD");
    skstradPVID.setServerColumnName("PVID");
    skstradPVID.setSqlType(2);
    skstradPVID.setDefault("0");
    skstradPVIP.setCaption("Potražni iznos u kunskoj protuvrijednosti");
    skstradPVIP.setColumnName("PVIP");
    skstradPVIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    skstradPVIP.setPrecision(17);
    skstradPVIP.setScale(2);
    skstradPVIP.setDisplayMask("###,###,##0.00");
    skstradPVIP.setDefault("0");
    skstradPVIP.setTableName("SKSTAVKERAD");
    skstradPVIP.setServerColumnName("PVIP");
    skstradPVIP.setSqlType(2);
    skstradPVIP.setDefault("0");
    skstradZIRO.setCaption("Žiro ra\u010Dun");
    skstradZIRO.setColumnName("ZIRO");
    skstradZIRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradZIRO.setPrecision(40);
    skstradZIRO.setTableName("SKSTAVKERAD");
    skstradZIRO.setServerColumnName("ZIRO");
    skstradZIRO.setSqlType(1);
    skstradZIRO.setWidth(30);

    skstradBROJKONTA.setCaption("Konto");
    skstradBROJKONTA.setColumnName("BROJKONTA");
    skstradBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradBROJKONTA.setPrecision(8);
    skstradBROJKONTA.setTableName("SKSTAVKERAD");
    skstradBROJKONTA.setServerColumnName("BROJKONTA");
    skstradBROJKONTA.setSqlType(1);
    
    skstradURUUID.setCaption("UUID ur");
    skstradURUUID.setColumnName("URUUID");
    skstradURUUID.setDataType(com.borland.dx.dataset.Variant.STRING);
    skstradURUUID.setPrecision(16);
    skstradURUUID.setTableName("SKSTAVKERAD");
    skstradURUUID.setServerColumnName("URUUID");
    skstradURUUID.setSqlType(1);
    skstradURUUID.setVisible(com.borland.jb.util.TriStateProperty.FALSE);

    skstrad.setResolver(dm.getQresolver());
    skstrad.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Skstavkerad", null, true, Load.ALL));
 setColumns(new Column[] {skstradLOKK, skstradAKTIV, skstradKNJIG, skstradCPAR, skstradSTAVKA, skstradCSKL, skstradVRDOK, skstradBROJDOK,
        skstradRBS, skstradBROJIZV, skstradCORG, skstradCAGENT, skstradDATUMKNJ, skstradDATDOK, skstradDATDOSP, skstradEXTBRDOK, skstradCNACPL, skstradOZNVAL, skstradTECAJ,
        skstradOPIS, skstradID, skstradIP, skstradPOKRIVENO, skstradSALDO, skstradCKOLONE, skstradCKNJIGE, skstradURAIRA, skstradCGKSTAVKE, skstradGODMJ,
        skstradDUGPOT, skstradDATPRI, skstradDATUNOS, skstradPVID, skstradPVIP, skstradZIRO, skstradBROJKONTA, skstradURUUID});
*/
    createFilteredDataSet(skstradfake, "");
  }

  /*public void setall() {

    ddl.create("Skstavkerad")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addInteger("cpar", 6, true)
       .addShort("stavka", 2, true)
       .addChar("cskl", 12, true)
       .addChar("vrdok", 3, true)
       .addChar("brojdok", 50, true)
       .addInteger("rbs", 6, true)
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
       .addFloat("saldo", 17, 2)
       .addShort("ckolone", 2)
       .addChar("cknjige", 5)
       .addChar("uraira", 1)
       .addChar("cgkstavke", 38)
       .addChar("godmj", 6)
       .addChar("dugpot", 1)
       .addDate("datpri")
       .addDate("datunos")
       .addFloat("pvid", 17, 2)
       .addFloat("pvip", 17, 2)
       .addChar("ziro", 40)
       .addChar("brojkonta", 8)
       .addChar("uruuid", 16)
       .addPrimaryKey("knjig,cpar,stavka,cskl,vrdok,brojdok,rbs,brojizv");


    Naziv = "Skstavkerad";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }*/
}
