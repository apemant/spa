/****license*****************************************************************
**   file: Gkstavkerad.java
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



public class Gkstavkerad extends KreirDrop implements DataModule {

  //dM dm  = dM.getDataModule();
  private static Gkstavkerad Gkstavkeradclass;

  QueryDataSet gkstrad = new QueryDataSet();

  /*Column gkstradLOKK = new Column();
  Column gkstradAKTIV = new Column();
  Column gkstradKNJIG = new Column();
  Column gkstradGOD = new Column();
  Column gkstradCVRNAL = new Column();
  Column gkstradRBR = new Column();
  Column gkstradRBS = new Column();
  Column gkstradBROJKONTA = new Column();
  Column gkstradCORG = new Column();
  Column gkstradCAGENT = new Column();
  Column gkstradDATUMKNJ = new Column();
  Column gkstradDATDOK = new Column();
  Column gkstradDATDOSP = new Column();
  Column gkstradBROJDOK = new Column();
  Column gkstradVRDOK = new Column();
  Column gkstradCPAR = new Column();
  Column gkstradEXTBRDOK = new Column();
  Column gkstradBROJIZV = new Column();
  Column gkstradCNACPL = new Column();
  Column gkstradOPIS = new Column();
  Column gkstradID = new Column();
  Column gkstradIP = new Column();
  Column gkstradPOKRIVENO = new Column();
  Column gkstradSALDO = new Column();
  Column gkstradCKOLONE = new Column();
  Column gkstradRBSRAC = new Column();
  Column gkstradCKNJIGE = new Column();
  Column gkstradURAIRA = new Column();
  Column gkstradGODMJ = new Column();
  Column gkstradTECAJ = new Column();
  Column gkstradDEVID = new Column();
  Column gkstradDEVIP = new Column();
  Column gkstradCNALOGA = new Column();
  Column gkstradOZNVAL = new Column();*/

  public static Gkstavkerad getDataModule() {
    if (Gkstavkeradclass == null) {
      Gkstavkeradclass = new Gkstavkerad();
    }
    return Gkstavkeradclass;
  }

  public QueryDataSet getQueryDataSet() {
    return gkstrad;
  }

  public Gkstavkerad() {
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
    /*gkstradLOKK.setCaption("Status zauzetosti");
    gkstradLOKK.setColumnName("LOKK");
    gkstradLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradLOKK.setPrecision(1);
    gkstradLOKK.setTableName("GKSTAVKERAD");
    gkstradLOKK.setServerColumnName("LOKK");
    gkstradLOKK.setSqlType(1);
    gkstradLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    gkstradLOKK.setDefault("N");
    gkstradAKTIV.setCaption("Aktivan - neaktivan");
    gkstradAKTIV.setColumnName("AKTIV");
    gkstradAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradAKTIV.setPrecision(1);
    gkstradAKTIV.setTableName("GKSTAVKERAD");
    gkstradAKTIV.setServerColumnName("AKTIV");
    gkstradAKTIV.setSqlType(1);
    gkstradAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    gkstradAKTIV.setDefault("D");
    gkstradKNJIG.setCaption("Knjigovodstvo");
    gkstradKNJIG.setColumnName("KNJIG");
    gkstradKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradKNJIG.setPrecision(12);
    gkstradKNJIG.setRowId(true);
    gkstradKNJIG.setTableName("GKSTAVKERAD");
    gkstradKNJIG.setServerColumnName("KNJIG");
    gkstradKNJIG.setSqlType(1);
    gkstradGOD.setCaption("Godina");
    gkstradGOD.setColumnName("GOD");
    gkstradGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradGOD.setPrecision(4);
    gkstradGOD.setRowId(true);
    gkstradGOD.setTableName("GKSTAVKERAD");
    gkstradGOD.setServerColumnName("GOD");
    gkstradGOD.setSqlType(1);
    gkstradCVRNAL.setCaption("Oznaka vrste naloga");
    gkstradCVRNAL.setColumnName("CVRNAL");
    gkstradCVRNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradCVRNAL.setPrecision(2);
    gkstradCVRNAL.setRowId(true);
    gkstradCVRNAL.setTableName("GKSTAVKERAD");
    gkstradCVRNAL.setServerColumnName("CVRNAL");
    gkstradCVRNAL.setSqlType(1);
    gkstradRBR.setCaption("RBR naloga");
    gkstradRBR.setColumnName("RBR");
    gkstradRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    gkstradRBR.setPrecision(8);
    gkstradRBR.setRowId(true);
    gkstradRBR.setTableName("GKSTAVKERAD");
    gkstradRBR.setServerColumnName("RBR");
    gkstradRBR.setSqlType(4);
    gkstradRBR.setWidth(8);
    gkstradRBS.setCaption("RBS");
    gkstradRBS.setColumnName("RBS");
    gkstradRBS.setDataType(com.borland.dx.dataset.Variant.INT);
    gkstradRBS.setPrecision(6);
    gkstradRBS.setRowId(true);
    gkstradRBS.setTableName("GKSTAVKERAD");
    gkstradRBS.setServerColumnName("RBS");
    gkstradRBS.setSqlType(4);
    gkstradRBS.setWidth(6);
    gkstradBROJKONTA.setCaption("Konto");
    gkstradBROJKONTA.setColumnName("BROJKONTA");
    gkstradBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradBROJKONTA.setPrecision(8);
    gkstradBROJKONTA.setTableName("GKSTAVKERAD");
    gkstradBROJKONTA.setServerColumnName("BROJKONTA");
    gkstradBROJKONTA.setSqlType(1);
    gkstradCORG.setCaption("Org. jedinica");
    gkstradCORG.setColumnName("CORG");
    gkstradCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradCORG.setPrecision(12);
    gkstradCORG.setTableName("GKSTAVKERAD");
    gkstradCORG.setServerColumnName("CORG");
    gkstradCORG.setSqlType(1);

    gkstradCAGENT.setCaption("Agent");
    gkstradCAGENT.setColumnName("CAGENT");
    gkstradCAGENT.setDataType(com.borland.dx.dataset.Variant.INT);
    gkstradCAGENT.setTableName("GKSTAVKERAD");
    gkstradCAGENT.setWidth(5);
    gkstradCAGENT.setServerColumnName("CAGENT");
    gkstradCAGENT.setSqlType(4);

    gkstradDATUMKNJ.setCaption("Datum knjiženja");
    gkstradDATUMKNJ.setColumnName("DATUMKNJ");
    gkstradDATUMKNJ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    gkstradDATUMKNJ.setPrecision(8);
    gkstradDATUMKNJ.setDisplayMask("dd-MM-yyyy");
//    gkstradDATUMKNJ.setEditMask("dd-MM-yyyy");
    gkstradDATUMKNJ.setTableName("GKSTAVKERAD");
    gkstradDATUMKNJ.setServerColumnName("DATUMKNJ");
    gkstradDATUMKNJ.setSqlType(93);
    gkstradDATUMKNJ.setWidth(10);
    gkstradDATDOK.setCaption("Datum dokumenta");
    gkstradDATDOK.setColumnName("DATDOK");
    gkstradDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    gkstradDATDOK.setPrecision(8);
    gkstradDATDOK.setDisplayMask("dd-MM-yyyy");
//    gkstradDATDOK.setEditMask("dd-MM-yyyy");
    gkstradDATDOK.setTableName("GKSTAVKERAD");
    gkstradDATDOK.setServerColumnName("DATDOK");
    gkstradDATDOK.setSqlType(93);
    gkstradDATDOK.setWidth(10);
    gkstradDATDOSP.setCaption("Datum dospje\u0107a");
    gkstradDATDOSP.setColumnName("DATDOSP");
    gkstradDATDOSP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    gkstradDATDOSP.setPrecision(8);
    gkstradDATDOSP.setDisplayMask("dd-MM-yyyy");
//    gkstradDATDOSP.setEditMask("dd-MM-yyyy");
    gkstradDATDOSP.setTableName("GKSTAVKERAD");
    gkstradDATDOSP.setServerColumnName("DATDOSP");
    gkstradDATDOSP.setSqlType(93);
    gkstradDATDOSP.setWidth(10);
    gkstradBROJDOK.setCaption("Broj dokumenta");
    gkstradBROJDOK.setColumnName("BROJDOK");
    gkstradBROJDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradBROJDOK.setPrecision(50);
    gkstradBROJDOK.setTableName("GKSTAVKERAD");
    gkstradBROJDOK.setServerColumnName("BROJDOK");
    gkstradBROJDOK.setSqlType(1);
    gkstradBROJDOK.setWidth(30);
    gkstradVRDOK.setCaption("Vrsta dokumenta");
    gkstradVRDOK.setColumnName("VRDOK");
    gkstradVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradVRDOK.setPrecision(3);
    gkstradVRDOK.setTableName("GKSTAVKERAD");
    gkstradVRDOK.setServerColumnName("VRDOK");
    gkstradVRDOK.setSqlType(1);
    gkstradCPAR.setCaption("Partner");
    gkstradCPAR.setColumnName("CPAR");
    gkstradCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    gkstradCPAR.setPrecision(6);
    gkstradCPAR.setTableName("GKSTAVKERAD");
    gkstradCPAR.setServerColumnName("CPAR");
    gkstradCPAR.setSqlType(4);
    gkstradCPAR.setWidth(6);
    gkstradEXTBRDOK.setCaption("Dodatni broj dokumenta");
    gkstradEXTBRDOK.setColumnName("EXTBRDOK");
    gkstradEXTBRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradEXTBRDOK.setPrecision(15);
    gkstradEXTBRDOK.setTableName("GKSTAVKERAD");
    gkstradEXTBRDOK.setServerColumnName("EXTBRDOK");
    gkstradEXTBRDOK.setSqlType(1);
    gkstradBROJIZV.setCaption("Broj izvoda");
    gkstradBROJIZV.setColumnName("BROJIZV");
    gkstradBROJIZV.setDataType(com.borland.dx.dataset.Variant.INT);
    gkstradBROJIZV.setPrecision(6);
    gkstradBROJIZV.setTableName("GKSTAVKERAD");
    gkstradBROJIZV.setServerColumnName("BROJIZV");
    gkstradBROJIZV.setSqlType(4);
    gkstradBROJIZV.setWidth(6);
    gkstradCNACPL.setCaption("Na\u010Din pla\u0107anja");
    gkstradCNACPL.setColumnName("CNACPL");
    gkstradCNACPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradCNACPL.setPrecision(3);
    gkstradCNACPL.setTableName("GKSTAVKERAD");
    gkstradCNACPL.setServerColumnName("CNACPL");
    gkstradCNACPL.setSqlType(1);
    gkstradOPIS.setCaption("Opis stavke");
    gkstradOPIS.setColumnName("OPIS");
    gkstradOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradOPIS.setPrecision(100);
    gkstradOPIS.setTableName("GKSTAVKERAD");
    gkstradOPIS.setServerColumnName("OPIS");
    gkstradOPIS.setSqlType(1);
    gkstradOPIS.setWidth(30);
    gkstradID.setCaption("Iznos duguje");
    gkstradID.setColumnName("ID");
    gkstradID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkstradID.setPrecision(17);
    gkstradID.setScale(2);
    gkstradID.setDisplayMask("###,###,##0.00");
    gkstradID.setDefault("0");
    gkstradID.setTableName("GKSTAVKERAD");
    gkstradID.setServerColumnName("ID");
    gkstradID.setSqlType(2);
    gkstradID.setDefault("0");
    gkstradIP.setCaption("Iznos potražuje");
    gkstradIP.setColumnName("IP");
    gkstradIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkstradIP.setPrecision(17);
    gkstradIP.setScale(2);
    gkstradIP.setDisplayMask("###,###,##0.00");
    gkstradIP.setDefault("0");
    gkstradIP.setTableName("GKSTAVKERAD");
    gkstradIP.setServerColumnName("IP");
    gkstradIP.setSqlType(2);
    gkstradIP.setDefault("0");
    gkstradPOKRIVENO.setCaption("Indikator pokrivenosti");
    gkstradPOKRIVENO.setColumnName("POKRIVENO");
    gkstradPOKRIVENO.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradPOKRIVENO.setPrecision(1);
    gkstradPOKRIVENO.setTableName("GKSTAVKERAD");
    gkstradPOKRIVENO.setServerColumnName("POKRIVENO");
    gkstradPOKRIVENO.setSqlType(1);
    gkstradPOKRIVENO.setDefault("N");
    gkstradSALDO.setCaption("Saldo stavke");
    gkstradSALDO.setColumnName("SALDO");
    gkstradSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkstradSALDO.setPrecision(17);
    gkstradSALDO.setScale(2);
    gkstradSALDO.setDisplayMask("###,###,##0.00");
    gkstradSALDO.setDefault("0");
    gkstradSALDO.setTableName("GKSTAVKERAD");
    gkstradSALDO.setServerColumnName("SALDO");
    gkstradSALDO.setSqlType(2);
    gkstradSALDO.setDefault("0");
    gkstradCKOLONE.setCaption("Broj kolone u knjizi");
    gkstradCKOLONE.setColumnName("CKOLONE");
    gkstradCKOLONE.setDataType(com.borland.dx.dataset.Variant.SHORT);
    gkstradCKOLONE.setPrecision(2);
    gkstradCKOLONE.setTableName("GKSTAVKERAD");
    gkstradCKOLONE.setServerColumnName("CKOLONE");
    gkstradCKOLONE.setSqlType(5);
    gkstradCKOLONE.setWidth(2);
    gkstradRBSRAC.setCaption("RBS pripadaju\u0107eg ra\u010Duna");
    gkstradRBSRAC.setColumnName("RBSRAC");
    gkstradRBSRAC.setDataType(com.borland.dx.dataset.Variant.INT);
    gkstradRBSRAC.setPrecision(8);
    gkstradRBSRAC.setTableName("GKSTAVKERAD");
    gkstradRBSRAC.setServerColumnName("RBSRAC");
    gkstradRBSRAC.setSqlType(4);
    gkstradRBSRAC.setWidth(8);
    gkstradCKNJIGE.setCaption("Knjiga");
    gkstradCKNJIGE.setColumnName("CKNJIGE");
    gkstradCKNJIGE.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradCKNJIGE.setPrecision(5);
    gkstradCKNJIGE.setTableName("GKSTAVKERAD");
    gkstradCKNJIGE.setServerColumnName("CKNJIGE");
    gkstradCKNJIGE.setSqlType(1);
    gkstradURAIRA.setCaption("Indikator URA/IRA");
    gkstradURAIRA.setColumnName("URAIRA");
    gkstradURAIRA.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradURAIRA.setPrecision(1);
    gkstradURAIRA.setTableName("GKSTAVKERAD");
    gkstradURAIRA.setServerColumnName("URAIRA");
    gkstradURAIRA.setSqlType(1);
    gkstradGODMJ.setCaption("Godina + mjesec");
    gkstradGODMJ.setColumnName("GODMJ");
    gkstradGODMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradGODMJ.setPrecision(6);
    gkstradGODMJ.setTableName("GKSTAVKERAD");
    gkstradGODMJ.setServerColumnName("GODMJ");
    gkstradGODMJ.setSqlType(1);
    gkstradTECAJ.setCaption("Te\u010Daj");
    gkstradTECAJ.setColumnName("TECAJ");
    gkstradTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkstradTECAJ.setPrecision(17);
    gkstradTECAJ.setScale(6);
    gkstradTECAJ.setDisplayMask("###,###,##0.000000");
    gkstradTECAJ.setDefault("0");
    gkstradTECAJ.setTableName("GKSTAVKERAD");
    gkstradTECAJ.setServerColumnName("TECAJ");
    gkstradTECAJ.setSqlType(2);
    gkstradTECAJ.setDefault("0");
    gkstradDEVID.setCaption("Dugovni iznos u valuti");
    gkstradDEVID.setColumnName("DEVID");
    gkstradDEVID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkstradDEVID.setPrecision(17);
    gkstradDEVID.setScale(2);
    gkstradDEVID.setDisplayMask("###,###,##0.00");
    gkstradDEVID.setDefault("0");
    gkstradDEVID.setTableName("GKSTAVKERAD");
    gkstradDEVID.setServerColumnName("DEVID");
    gkstradDEVID.setSqlType(2);
    gkstradDEVID.setDefault("0");
    gkstradDEVIP.setCaption("Potražni iznos u valuti");
    gkstradDEVIP.setColumnName("DEVIP");
    gkstradDEVIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    gkstradDEVIP.setPrecision(17);
    gkstradDEVIP.setScale(2);
    gkstradDEVIP.setDisplayMask("###,###,##0.00");
    gkstradDEVIP.setDefault("0");
    gkstradDEVIP.setTableName("GKSTAVKERAD");
    gkstradDEVIP.setServerColumnName("DEVIP");
    gkstradDEVIP.setSqlType(2);
    gkstradDEVIP.setDefault("0");
    gkstradCNALOGA.setCaption("Broj naloga");
    gkstradCNALOGA.setColumnName("CNALOGA");
    gkstradCNALOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradCNALOGA.setPrecision(30);
    gkstradCNALOGA.setTableName("GKSTAVKERAD");
    gkstradCNALOGA.setServerColumnName("CNALOGA");
    gkstradCNALOGA.setSqlType(1);
    gkstradCNALOGA.setWidth(30);

    gkstradOZNVAL.setCaption("Valuta");
    gkstradOZNVAL.setColumnName("OZNVAL");
    gkstradOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    gkstradOZNVAL.setPrecision(3);
    gkstradOZNVAL.setTableName("GKSTAVKERAD");
    gkstradOZNVAL.setServerColumnName("OZNVAL");
    gkstradOZNVAL.setSqlType(1);

    gkstrad.setResolver(dm.getQresolver());
    gkstrad.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Gkstavkerad", null, true, Load.ALL));
 setColumns(new Column[] {gkstradLOKK, gkstradAKTIV, gkstradKNJIG, gkstradGOD, gkstradCVRNAL, gkstradRBR, gkstradRBS, gkstradBROJKONTA, gkstradCORG, gkstradCAGENT,
        gkstradDATUMKNJ, gkstradDATDOK, gkstradDATDOSP, gkstradBROJDOK, gkstradVRDOK, gkstradCPAR, gkstradEXTBRDOK, gkstradBROJIZV, gkstradCNACPL, gkstradOPIS,
        gkstradID, gkstradIP, gkstradPOKRIVENO, gkstradSALDO, gkstradCKOLONE, gkstradRBSRAC, gkstradCKNJIGE, gkstradURAIRA, gkstradGODMJ, gkstradTECAJ,
        gkstradDEVID, gkstradDEVIP, gkstradCNALOGA, gkstradOZNVAL});*/

  }

  /*public void setall() {

    ddl.create("Gkstavkerad")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addChar("god", 4, true)
       .addChar("cvrnal", 2, true)
       .addInteger("rbr", 8, true)
       .addInteger("rbs", 6, true)
       .addChar("brojkonta", 8)
       .addChar("corg", 12)
       .addInteger("cagent", 6)
       .addDate("datumknj")
       .addDate("datdok")
       .addDate("datdosp")
       .addChar("brojdok", 50)
       .addChar("vrdok", 3)
       .addInteger("cpar", 6)
       .addChar("extbrdok", 15)
       .addInteger("brojizv", 6)
       .addChar("cnacpl", 3)
       .addChar("opis", 100)
       .addFloat("id", 17, 2)
       .addFloat("ip", 17, 2)
       .addChar("pokriveno", 1, "N")
       .addFloat("saldo", 17, 2)
       .addShort("ckolone", 2)
       .addInteger("rbsrac", 8)
       .addChar("cknjige", 5)
       .addChar("uraira", 1)
       .addChar("godmj", 6)
       .addFloat("tecaj", 17, 6)
       .addFloat("devid", 17, 2)
       .addFloat("devip", 17, 2)
       .addChar("cnaloga", 30)
       .addChar("oznval", 3)
       .addPrimaryKey("knjig,god,cvrnal,rbr,rbs");

    Naziv = "Gkstavkerad";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }*/
}
