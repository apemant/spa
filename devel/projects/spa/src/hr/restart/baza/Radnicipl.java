/****license*****************************************************************
**   file: Radnicipl.java
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



public class Radnicipl extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Radnicipl Radniciplclass;

  QueryDataSet rpl = new raDataSet();
  QueryDataSet rplaktiv = new raDataSet();
/*
  Column rplLOKK = new Column();
  Column rplAKTIV = new Column();
  Column rplCRADNIK = new Column();
  Column rplCRADMJ = new Column();
  Column rplCSS = new Column();
  Column rplCVRO = new Column();
  Column rplCISPLMJ = new Column();
  Column rplCOPCINE = new Column();
  Column rplRSINV = new Column();
  Column rplRSOO = new Column();
  Column rplBRUTOSN = new Column();
  Column rplBRUTDOD = new Column();
  Column rplBRUTMR = new Column();
  Column rplBRUTUK = new Column();
  Column rplGODSTAZ = new Column();
  Column rplSTOPASTAZ = new Column();
  Column rplDATSTAZ = new Column();
  Column rplPODSTAZ = new Column();
  Column rplDATPODSTAZ = new Column();
  Column rplNACOBRB = new Column();
  Column rplKOEF = new Column();
  Column rplKOEFZAR = new Column();
  Column rplDATDOL = new Column();
  Column rplDATODL = new Column();
  Column rplDATREGRES = new Column();
  Column rplOLUK = new Column();
  Column rplOLOS = new Column();
  Column rplJMBG = new Column();
  Column rplBRRADKNJ = new Column();
  Column rplREGBRRK = new Column();
  Column rplREGBRMIO = new Column();
  Column rplBROSIGZO = new Column();
  Column rplZIJMBGZO = new Column();
  Column rplBROBVEZE = new Column();
  Column rplCLANOMF = new Column();
  Column rplRSB = new Column();
  Column rplRSZ = new Column();
  Column rplCORG = new Column();
  Column rplADRESA = new Column();
  Column rplBROJTEK = new Column();
  Column rplPARAMETRI = new Column();
*/
  public static Radnicipl getDataModule() {
    if (Radniciplclass == null) {
      Radniciplclass = new Radnicipl();
    }
    return Radniciplclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rpl;
  }

  public QueryDataSet getAktiv() {
    return rplaktiv;
  }

  public Radnicipl() {
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
    createFilteredDataSet(rplaktiv, "aktiv='D'");
    /*
    rplLOKK.setCaption("Status zauzetosti");
    rplLOKK.setColumnName("LOKK");
    rplLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplLOKK.setPrecision(1);
    rplLOKK.setTableName("RADNICIPL");
    rplLOKK.setServerColumnName("LOKK");
    rplLOKK.setSqlType(1);
    rplLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    rplLOKK.setDefault("N");
    rplAKTIV.setCaption("Aktivan - neaktivan");
    rplAKTIV.setColumnName("AKTIV");
    rplAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplAKTIV.setPrecision(1);
    rplAKTIV.setTableName("RADNICIPL");
    rplAKTIV.setServerColumnName("AKTIV");
    rplAKTIV.setSqlType(1);
    rplAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    rplAKTIV.setDefault("D");
    rplCRADNIK.setCaption("Mati\u010Dni broj");
    rplCRADNIK.setColumnName("CRADNIK");
    rplCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplCRADNIK.setPrecision(6);
    rplCRADNIK.setRowId(true);
    rplCRADNIK.setTableName("RADNICIPL");
    rplCRADNIK.setServerColumnName("CRADNIK");
    rplCRADNIK.setSqlType(1);
    rplCRADMJ.setCaption("Oznaka radnog mjesta");
    rplCRADMJ.setColumnName("CRADMJ");
    rplCRADMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplCRADMJ.setPrecision(6);
    rplCRADMJ.setTableName("RADNICIPL");
    rplCRADMJ.setServerColumnName("CRADMJ");
    rplCRADMJ.setSqlType(1);
    rplCSS.setCaption("Oznaka stru\u010Dne spreme");
    rplCSS.setColumnName("CSS");
    rplCSS.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplCSS.setPrecision(5);
    rplCSS.setTableName("RADNICIPL");
    rplCSS.setServerColumnName("CSS");
    rplCSS.setSqlType(1);
    rplCVRO.setCaption("Oznaka vrste radnog odnosa");
    rplCVRO.setColumnName("CVRO");
    rplCVRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplCVRO.setPrecision(6);
    rplCVRO.setTableName("RADNICIPL");
    rplCVRO.setServerColumnName("CVRO");
    rplCVRO.setSqlType(1);
    rplCISPLMJ.setCaption("Oznaka isplatnog mjesta");
    rplCISPLMJ.setColumnName("CISPLMJ");
    rplCISPLMJ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rplCISPLMJ.setPrecision(2);
    rplCISPLMJ.setTableName("RADNICIPL");
    rplCISPLMJ.setServerColumnName("CISPLMJ");
    rplCISPLMJ.setSqlType(5);
    rplCISPLMJ.setWidth(2);
    rplCOPCINE.setCaption("Op\u0107ina poreza");
    rplCOPCINE.setColumnName("COPCINE");
    rplCOPCINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplCOPCINE.setPrecision(3);
    rplCOPCINE.setTableName("RADNICIPL");
    rplCOPCINE.setServerColumnName("COPCINE");
    rplCOPCINE.setSqlType(1);
    rplRSINV.setCaption("Oznaka invalidnosti");
    rplRSINV.setColumnName("RSINV");
    rplRSINV.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplRSINV.setPrecision(5);
    rplRSINV.setTableName("RADNICIPL");
    rplRSINV.setServerColumnName("RSINV");
    rplRSINV.setSqlType(1);
    rplRSOO.setCaption("Osnova osiguranja");
    rplRSOO.setColumnName("RSOO");
    rplRSOO.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplRSOO.setPrecision(5);
    rplRSOO.setTableName("RADNICIPL");
    rplRSOO.setServerColumnName("RSOO");
    rplRSOO.setSqlType(1);
    rplBRUTOSN.setCaption("Bruto osnovni");
    rplBRUTOSN.setColumnName("BRUTOSN");
    rplBRUTOSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rplBRUTOSN.setPrecision(17);
    rplBRUTOSN.setScale(2);
    rplBRUTOSN.setDisplayMask("###,###,##0.00");
    rplBRUTOSN.setDefault("0");
    rplBRUTOSN.setTableName("RADNICIPL");
    rplBRUTOSN.setServerColumnName("BRUTOSN");
    rplBRUTOSN.setSqlType(2);
    rplBRUTOSN.setDefault("0");
    rplBRUTDOD.setCaption("Bruto dodatni");
    rplBRUTDOD.setColumnName("BRUTDOD");
    rplBRUTDOD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rplBRUTDOD.setPrecision(17);
    rplBRUTDOD.setScale(2);
    rplBRUTDOD.setDisplayMask("###,###,##0.00");
    rplBRUTDOD.setDefault("0");
    rplBRUTDOD.setTableName("RADNICIPL");
    rplBRUTDOD.setServerColumnName("BRUTDOD");
    rplBRUTDOD.setSqlType(2);
    rplBRUTDOD.setDefault("0");
    rplBRUTMR.setCaption("Bruto radni staž");
    rplBRUTMR.setColumnName("BRUTMR");
    rplBRUTMR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rplBRUTMR.setPrecision(17);
    rplBRUTMR.setScale(2);
    rplBRUTMR.setDisplayMask("###,###,##0.00");
    rplBRUTMR.setDefault("0");
    rplBRUTMR.setTableName("RADNICIPL");
    rplBRUTMR.setServerColumnName("BRUTMR");
    rplBRUTMR.setSqlType(2);
    rplBRUTMR.setDefault("0");
    rplBRUTUK.setCaption("Bruto ukupno");
    rplBRUTUK.setColumnName("BRUTUK");
    rplBRUTUK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rplBRUTUK.setPrecision(17);
    rplBRUTUK.setScale(2);
    rplBRUTUK.setDisplayMask("###,###,##0.00");
    rplBRUTUK.setDefault("0");
    rplBRUTUK.setTableName("RADNICIPL");
    rplBRUTUK.setServerColumnName("BRUTUK");
    rplBRUTUK.setSqlType(2);
    rplBRUTUK.setDefault("0");
    rplGODSTAZ.setCaption("Godine staža");
    rplGODSTAZ.setColumnName("GODSTAZ");
    rplGODSTAZ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rplGODSTAZ.setPrecision(2);
    rplGODSTAZ.setTableName("RADNICIPL");
    rplGODSTAZ.setServerColumnName("GODSTAZ");
    rplGODSTAZ.setSqlType(5);
    rplGODSTAZ.setWidth(2);
    rplGODSTAZ.setDefault("0");
    rplSTOPASTAZ.setCaption("Stopa za radni staž");
    rplSTOPASTAZ.setColumnName("STOPASTAZ");
    rplSTOPASTAZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rplSTOPASTAZ.setPrecision(9);
    rplSTOPASTAZ.setScale(5);
    rplSTOPASTAZ.setDisplayMask("###,###,##0.00000");
    rplSTOPASTAZ.setDefault("0");
    rplSTOPASTAZ.setTableName("RADNICIPL");
    rplSTOPASTAZ.setServerColumnName("STOPASTAZ");
    rplSTOPASTAZ.setSqlType(2);
    rplSTOPASTAZ.setDefault("0");
    rplDATSTAZ.setCaption("Datum postizanja pune godine staža");
    rplDATSTAZ.setColumnName("DATSTAZ");
    rplDATSTAZ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    rplDATSTAZ.setPrecision(8);
    rplDATSTAZ.setDisplayMask("dd-MM-yyyy");
//    rplDATSTAZ.setEditMask("dd-MM-yyyy");
    rplDATSTAZ.setTableName("RADNICIPL");
    rplDATSTAZ.setServerColumnName("DATSTAZ");
    rplDATSTAZ.setSqlType(93);
    rplDATSTAZ.setWidth(10);
    rplPODSTAZ.setCaption("Godine staža u poduze\u0107u");
    rplPODSTAZ.setColumnName("PODSTAZ");
    rplPODSTAZ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rplPODSTAZ.setPrecision(2);
    rplPODSTAZ.setTableName("RADNICIPL");
    rplPODSTAZ.setServerColumnName("PODSTAZ");
    rplPODSTAZ.setSqlType(5);
    rplPODSTAZ.setWidth(2);
    rplDATPODSTAZ.setCaption("Datum postizanja pune godine staža u poduze\u0107u");
    rplDATPODSTAZ.setColumnName("DATPODSTAZ");
    rplDATPODSTAZ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    rplDATPODSTAZ.setPrecision(8);
    rplDATPODSTAZ.setDisplayMask("dd-MM-yyyy");
//    rplDATPODSTAZ.setEditMask("dd-MM-yyyy");
    rplDATPODSTAZ.setTableName("RADNICIPL");
    rplDATPODSTAZ.setServerColumnName("DATPODSTAZ");
    rplDATPODSTAZ.setSqlType(93);
    rplDATPODSTAZ.setWidth(10);
    rplNACOBRB.setCaption("Na\u010Din obra\u010Duna bruta");
    rplNACOBRB.setColumnName("NACOBRB");
    rplNACOBRB.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplNACOBRB.setPrecision(1);
    rplNACOBRB.setTableName("RADNICIPL");
    rplNACOBRB.setServerColumnName("NACOBRB");
    rplNACOBRB.setSqlType(1);
    rplNACOBRB.setDefault("0");
    rplKOEF.setCaption("Koeficijent");
    rplKOEF.setColumnName("KOEF");
    rplKOEF.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rplKOEF.setPrecision(9);
    rplKOEF.setScale(5);
    rplKOEF.setDisplayMask("###,###,##0.00000");
    rplKOEF.setDefault("0");
    rplKOEF.setTableName("RADNICIPL");
    rplKOEF.setServerColumnName("KOEF");
    rplKOEF.setSqlType(2);
    rplKOEF.setDefault("0");
    rplKOEFZAR.setCaption("Koeficijent za zaradu");
    rplKOEFZAR.setColumnName("KOEFZAR");
    rplKOEFZAR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rplKOEFZAR.setPrecision(9);
    rplKOEFZAR.setScale(5);
    rplKOEFZAR.setDisplayMask("###,###,##0.00000");
    rplKOEFZAR.setDefault("0");
    rplKOEFZAR.setTableName("RADNICIPL");
    rplKOEFZAR.setServerColumnName("KOEFZAR");
    rplKOEFZAR.setSqlType(2);
    rplKOEFZAR.setDefault("0");
    rplDATDOL.setCaption("Datum zapošljavanja");
    rplDATDOL.setColumnName("DATDOL");
    rplDATDOL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    rplDATDOL.setPrecision(8);
    rplDATDOL.setDisplayMask("dd-MM-yyyy");
//    rplDATDOL.setEditMask("dd-MM-yyyy");
    rplDATDOL.setTableName("RADNICIPL");
    rplDATDOL.setServerColumnName("DATDOL");
    rplDATDOL.setSqlType(93);
    rplDATDOL.setWidth(10);
    rplDATODL.setCaption("Datum odlaska");
    rplDATODL.setColumnName("DATODL");
    rplDATODL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    rplDATODL.setPrecision(8);
    rplDATODL.setDisplayMask("dd-MM-yyyy");
//    rplDATODL.setEditMask("dd-MM-yyyy");
    rplDATODL.setTableName("RADNICIPL");
    rplDATODL.setServerColumnName("DATODL");
    rplDATODL.setSqlType(93);
    rplDATODL.setWidth(10);
    rplDATREGRES.setCaption("Datum regresa");
    rplDATREGRES.setColumnName("DATREGRES");
    rplDATREGRES.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    rplDATREGRES.setPrecision(8);
    rplDATREGRES.setDisplayMask("dd-MM-yyyy");
//    rplDATREGRES.setEditMask("dd-MM-yyyy");
    rplDATREGRES.setTableName("RADNICIPL");
    rplDATREGRES.setServerColumnName("DATREGRES");
    rplDATREGRES.setSqlType(93);
    rplDATREGRES.setWidth(10);
    rplOLUK.setCaption("Olakšica ukupno");
    rplOLUK.setColumnName("OLUK");
    rplOLUK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rplOLUK.setPrecision(9);
    rplOLUK.setScale(5);
    rplOLUK.setDisplayMask("###,###,##0.00000");
    rplOLUK.setDefault("0");
    rplOLUK.setTableName("RADNICIPL");
    rplOLUK.setServerColumnName("OLUK");
    rplOLUK.setSqlType(2);
    rplOLUK.setDefault("1");
    rplOLOS.setCaption("Osnovna olakšica");
    rplOLOS.setColumnName("OLOS");
    rplOLOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rplOLOS.setPrecision(9);
    rplOLOS.setScale(5);
    rplOLOS.setDisplayMask("###,###,##0.00000");
    rplOLOS.setDefault("0");
    rplOLOS.setTableName("RADNICIPL");
    rplOLOS.setServerColumnName("OLOS");
    rplOLOS.setSqlType(2);
    rplOLOS.setDefault("1");
    rplJMBG.setCaption("JMBG");
    rplJMBG.setColumnName("JMBG");
    rplJMBG.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplJMBG.setPrecision(13);
    rplJMBG.setTableName("RADNICIPL");
    rplJMBG.setServerColumnName("JMBG");
    rplJMBG.setSqlType(1);
    rplBRRADKNJ.setCaption("Broj radne knjižice");
    rplBRRADKNJ.setColumnName("BRRADKNJ");
    rplBRRADKNJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplBRRADKNJ.setPrecision(15);
    rplBRRADKNJ.setTableName("RADNICIPL");
    rplBRRADKNJ.setServerColumnName("BRRADKNJ");
    rplBRRADKNJ.setSqlType(1);
    rplREGBRRK.setCaption("Registarski broj obveznika iz radne knjižice");
    rplREGBRRK.setColumnName("REGBRRK");
    rplREGBRRK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplREGBRRK.setPrecision(15);
    rplREGBRRK.setTableName("RADNICIPL");
    rplREGBRRK.setServerColumnName("REGBRRK");
    rplREGBRRK.setSqlType(1);
    rplREGBRMIO.setCaption("Registarski broj MIO");
    rplREGBRMIO.setColumnName("REGBRMIO");
    rplREGBRMIO.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplREGBRMIO.setPrecision(15);
    rplREGBRMIO.setTableName("RADNICIPL");
    rplREGBRMIO.setServerColumnName("REGBRMIO");
    rplREGBRMIO.setSqlType(1);
    rplBROSIGZO.setCaption("Broj osigurane osobe iz zdravstvene kartice");
    rplBROSIGZO.setColumnName("BROSIGZO");
    rplBROSIGZO.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplBROSIGZO.setPrecision(15);
    rplBROSIGZO.setTableName("RADNICIPL");
    rplBROSIGZO.setServerColumnName("BROSIGZO");
    rplBROSIGZO.setSqlType(1);
    rplZIJMBGZO.setCaption("Znak ispred JMBG-a iz zdravstvene kartice");
    rplZIJMBGZO.setColumnName("ZIJMBGZO");
    rplZIJMBGZO.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplZIJMBGZO.setPrecision(1);
    rplZIJMBGZO.setTableName("RADNICIPL");
    rplZIJMBGZO.setServerColumnName("ZIJMBGZO");
    rplZIJMBGZO.setSqlType(1);
    rplBROBVEZE.setCaption("Broj obveze - zdravstvena kartica");
    rplBROBVEZE.setColumnName("BROBVEZE");
    rplBROBVEZE.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplBROBVEZE.setPrecision(15);
    rplBROBVEZE.setTableName("RADNICIPL");
    rplBROBVEZE.setServerColumnName("BROBVEZE");
    rplBROBVEZE.setSqlType(1);
    rplCLANOMF.setCaption("\u010Clan OMF (II stup)");
    rplCLANOMF.setColumnName("CLANOMF");
    rplCLANOMF.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplCLANOMF.setPrecision(1);
    rplCLANOMF.setTableName("RADNICIPL");
    rplCLANOMF.setServerColumnName("CLANOMF");
    rplCLANOMF.setSqlType(1);
    rplCLANOMF.setDefault("N");
    rplRSB.setCaption("Oznaka staža sa uve\u0107anim trajanjem");
    rplRSB.setColumnName("RSB");
    rplRSB.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplRSB.setPrecision(5);
    rplRSB.setTableName("RADNICIPL");
    rplRSB.setServerColumnName("RSB");
    rplRSB.setSqlType(1);
    rplRSB.setDefault("0");
    rplRSZ.setCaption("Oznaka zdravstvenog osiguranja");
    rplRSZ.setColumnName("RSZ");
    rplRSZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplRSZ.setPrecision(5);
    rplRSZ.setTableName("RADNICIPL");
    rplRSZ.setServerColumnName("RSZ");
    rplRSZ.setSqlType(1);
    rplCORG.setCaption("Org. Jedinica");
    rplCORG.setColumnName("CORG");
    rplCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplCORG.setPrecision(12);
    rplCORG.setTableName("RADNICIPL");
    rplCORG.setServerColumnName("CORG");
    rplCORG.setSqlType(1);
    rplADRESA.setCaption("Adresa");
    rplADRESA.setColumnName("ADRESA");
    rplADRESA.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplADRESA.setPrecision(50);
    rplADRESA.setTableName("RADNICIPL");
    rplADRESA.setServerColumnName("ADRESA");
    rplADRESA.setSqlType(1);
    rplADRESA.setWidth(30);
    rplBROJTEK.setCaption("Broj ra\u010Duna");
    rplBROJTEK.setColumnName("BROJTEK");
    rplBROJTEK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplBROJTEK.setPrecision(30);
    rplBROJTEK.setTableName("RADNICIPL");
    rplBROJTEK.setServerColumnName("BROJTEK");
    rplBROJTEK.setSqlType(1);
    rplBROJTEK.setWidth(30);
    rplPARAMETRI.setCaption("Parametri");
    rplPARAMETRI.setColumnName("PARAMETRI");
    rplPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    rplPARAMETRI.setPrecision(20);
    rplPARAMETRI.setTableName("RADNICIPL");
    rplPARAMETRI.setServerColumnName("PARAMETRI");
    rplPARAMETRI.setSqlType(1);
    rplPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    rpl.setResolver(dm.getQresolver());
    rpl.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Radnicipl", null, true, Load.ALL));
 setColumns(new Column[] {rplLOKK, rplAKTIV, rplCRADNIK, rplCRADMJ, rplCSS, rplCVRO, rplCISPLMJ, rplCOPCINE, rplRSINV, rplRSOO, rplBRUTOSN, rplBRUTDOD,
        rplBRUTMR, rplBRUTUK, rplGODSTAZ, rplSTOPASTAZ, rplDATSTAZ, rplPODSTAZ, rplDATPODSTAZ, rplNACOBRB, rplKOEF, rplKOEFZAR, rplDATDOL, rplDATODL,
        rplDATREGRES, rplOLUK, rplOLOS, rplJMBG, rplBRRADKNJ, rplREGBRRK, rplREGBRMIO, rplBROSIGZO, rplZIJMBGZO, rplBROBVEZE, rplCLANOMF, rplRSB, rplRSZ,
        rplCORG, rplADRESA, rplBROJTEK, rplPARAMETRI});
*/
  }
/*
  public void setall() {

    ddl.create("Radnicipl")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cradnik", 6, true)
       .addChar("cradmj", 6)
       .addChar("css", 5)
       .addChar("cvro", 6)
       .addShort("cisplmj", 2)
       .addChar("copcine", 3)
       .addChar("rsinv", 5)
       .addChar("rsoo", 5)
       .addFloat("brutosn", 17, 2)
       .addFloat("brutdod", 17, 2)
       .addFloat("brutmr", 17, 2)
       .addFloat("brutuk", 17, 2)
       .addShort("godstaz", 2)
       .addFloat("stopastaz", 9, 5)
       .addDate("datstaz")
       .addShort("podstaz", 2)
       .addDate("datpodstaz")
       .addChar("nacobrb", 1, "0")
       .addFloat("koef", 9, 5)
       .addFloat("koefzar", 9, 5)
       .addDate("datdol")
       .addDate("datodl")
       .addDate("datregres")
       .addFloat("oluk", 9, 5)
       .addFloat("olos", 9, 5)
       .addChar("jmbg", 13)
       .addChar("brradknj", 15)
       .addChar("regbrrk", 15)
       .addChar("regbrmio", 15)
       .addChar("brosigzo", 15)
       .addChar("zijmbgzo", 1)
       .addChar("brobveze", 15)
       .addChar("clanomf", 1, "N")
       .addChar("rsb", 5, "0")
       .addChar("rsz", 5)
       .addChar("corg", 12)
       .addChar("adresa", 50)
       .addChar("brojtek", 30)
       .addChar("parametri", 20)
       .addPrimaryKey("cradnik");


    Naziv = "Radnicipl";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
*/
}
