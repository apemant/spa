
/****license*****************************************************************
**   file: Kumulradarh.java
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

import com.borland.dx.sql.dataset.QueryDataSet;

public class Kumulradarh extends KreirDrop {

  private static Kumulradarh Kumulradarhclass;
  
  QueryDataSet Kumulradarh = new raDataSet();
  
  public static Kumulradarh getDataModule() {
    if (Kumulradarhclass == null) {
      Kumulradarhclass = new Kumulradarh();
    }
    return Kumulradarhclass;
  }

  public QueryDataSet getQueryDataSet() {
    return Kumulradarh;
  }

  public Kumulradarh() {
    try {
      modules.put(this.getClass().getName(), this);
      initModule();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}
//package hr.restart.baza;
//import com.borland.dx.dataset.*;
//import com.borland.dx.sql.dataset.*;
//
//
//
//public class Kumulradarh extends KreirDrop implements DataModule {
//
//  dM dm  = dM.getDataModule();
//  private static Kumulradarh Kumulradarhclass;
//
//  QueryDataSet kra = new raDataSet();
//
//  Column kraLOKK = new Column();
//  Column kraAKTIV = new Column();
//  Column kraGODOBR = new Column();
//  Column kraMJOBR = new Column();
//  Column kraRBROBR = new Column();
//  Column kraCRADNIK = new Column();
//  Column kraSATI = new Column();
//  Column kraBRUTO = new Column();
//  Column kraDOPRINOSI = new Column();
//  Column kraNETO = new Column();
//  Column kraNEOP = new Column();
//  Column kraISKNEOP = new Column();
//  Column kraPOROSN = new Column();
//  Column kraPOR1 = new Column();
//  Column kraPOR2 = new Column();
//  Column kraPOR3 = new Column();
//  Column kraPOR4 = new Column();
//  Column kraPOR5 = new Column();
//  Column kraPORUK = new Column();
//  Column kraPRIR = new Column();
//  Column kraPORIPRIR = new Column();
//  Column kraNETO2 = new Column();
//  Column kraNAKNADE = new Column();
//  Column kraNETOPK = new Column();
//  Column kraKREDITI = new Column();
//  Column kraNARUKE = new Column();
//  Column kraCRADMJ = new Column();
//  Column kraCSS = new Column();
//  Column kraCVRO = new Column();
//  Column kraCISPLMJ = new Column();
//  Column kraCOPCINE = new Column();
//  Column kraRSINV = new Column();
//  Column kraRSOO = new Column();
//  Column kraBRUTOSN = new Column();
//  Column kraBRUTDOD = new Column();
//  Column kraBRUTMR = new Column();
//  Column kraBRUTUK = new Column();
//  Column kraGODSTAZ = new Column();
//  Column kraSTOPASTAZ = new Column();
//  Column kraDATSTAZ = new Column();
//  Column kraPODSTAZ = new Column();
//  Column kraDATPODSTAZ = new Column();
//  Column kraNACOBRB = new Column();
//  Column kraKOEF = new Column();
//  Column kraKOEFZAR = new Column();
//  Column kraOLUK = new Column();
//  Column kraOLOS = new Column();
//  Column kraCLANOMF = new Column();
//  Column kraCORG = new Column();
//  Column kraPARAMETRI = new Column();
//
//  public static Kumulradarh getDataModule() {
//    if (Kumulradarhclass == null) {
//      Kumulradarhclass = new Kumulradarh();
//    }
//    return Kumulradarhclass;
//  }
//
//  public QueryDataSet getQueryDataSet() {
//    return kra;
//  }
//
//  public Kumulradarh() {
//    try {
//      modules.put(this.getClass().getName(), this);
//      jbInit();
//    }
//    catch(Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  private void jbInit() throws Exception {
//    kraLOKK.setCaption("Status zauzetosti");
//    kraLOKK.setColumnName("LOKK");
//    kraLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraLOKK.setPrecision(1);
//    kraLOKK.setTableName("KUMULRADARH");
//    kraLOKK.setServerColumnName("LOKK");
//    kraLOKK.setSqlType(1);
//    kraLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    kraLOKK.setDefault("N");
//    kraAKTIV.setCaption("Aktivan - neaktivan");
//    kraAKTIV.setColumnName("AKTIV");
//    kraAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraAKTIV.setPrecision(1);
//    kraAKTIV.setTableName("KUMULRADARH");
//    kraAKTIV.setServerColumnName("AKTIV");
//    kraAKTIV.setSqlType(1);
//    kraAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    kraAKTIV.setDefault("D");
//    kraGODOBR.setCaption("Godina obra\u010Duna");
//    kraGODOBR.setColumnName("GODOBR");
//    kraGODOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    kraGODOBR.setPrecision(4);
//    kraGODOBR.setRowId(true);
//    kraGODOBR.setTableName("KUMULRADARH");
//    kraGODOBR.setServerColumnName("GODOBR");
//    kraGODOBR.setSqlType(5);
//    kraGODOBR.setWidth(4);
//    kraMJOBR.setCaption("Mjesec obra\u010Duna");
//    kraMJOBR.setColumnName("MJOBR");
//    kraMJOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    kraMJOBR.setPrecision(2);
//    kraMJOBR.setRowId(true);
//    kraMJOBR.setTableName("KUMULRADARH");
//    kraMJOBR.setServerColumnName("MJOBR");
//    kraMJOBR.setSqlType(5);
//    kraMJOBR.setWidth(2);
//    kraRBROBR.setCaption("Redni broj obra\u010Duna");
//    kraRBROBR.setColumnName("RBROBR");
//    kraRBROBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    kraRBROBR.setPrecision(3);
//    kraRBROBR.setRowId(true);
//    kraRBROBR.setTableName("KUMULRADARH");
//    kraRBROBR.setServerColumnName("RBROBR");
//    kraRBROBR.setSqlType(5);
//    kraRBROBR.setWidth(3);
//    kraCRADNIK.setCaption("Radnik");
//    kraCRADNIK.setColumnName("CRADNIK");
//    kraCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraCRADNIK.setPrecision(6);
//    kraCRADNIK.setRowId(true);
//    kraCRADNIK.setTableName("KUMULRADARH");
//    kraCRADNIK.setServerColumnName("CRADNIK");
//    kraCRADNIK.setSqlType(1);
//    kraSATI.setCaption("Sati");
//    kraSATI.setColumnName("SATI");
//    kraSATI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraSATI.setPrecision(17);
//    kraSATI.setScale(2);
//    kraSATI.setDisplayMask("###,###,##0.00");
//    kraSATI.setDefault("0");
//    kraSATI.setTableName("KUMULRADARH");
//    kraSATI.setServerColumnName("SATI");
//    kraSATI.setSqlType(2);
//    kraSATI.setDefault("0");
//    kraBRUTO.setCaption("Bruto");
//    kraBRUTO.setColumnName("BRUTO");
//    kraBRUTO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraBRUTO.setPrecision(17);
//    kraBRUTO.setScale(2);
//    kraBRUTO.setDisplayMask("###,###,##0.00");
//    kraBRUTO.setDefault("0");
//    kraBRUTO.setTableName("KUMULRADARH");
//    kraBRUTO.setServerColumnName("BRUTO");
//    kraBRUTO.setSqlType(2);
//    kraBRUTO.setDefault("0");
//    kraDOPRINOSI.setCaption("Doprinosi");
//    kraDOPRINOSI.setColumnName("DOPRINOSI");
//    kraDOPRINOSI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraDOPRINOSI.setPrecision(17);
//    kraDOPRINOSI.setScale(2);
//    kraDOPRINOSI.setDisplayMask("###,###,##0.00");
//    kraDOPRINOSI.setDefault("0");
//    kraDOPRINOSI.setTableName("KUMULRADARH");
//    kraDOPRINOSI.setServerColumnName("DOPRINOSI");
//    kraDOPRINOSI.setSqlType(2);
//    kraDOPRINOSI.setDefault("0");
//    kraNETO.setCaption("Bruto po odbitku doprinosa");
//    kraNETO.setColumnName("NETO");
//    kraNETO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraNETO.setPrecision(17);
//    kraNETO.setScale(2);
//    kraNETO.setDisplayMask("###,###,##0.00");
//    kraNETO.setDefault("0");
//    kraNETO.setTableName("KUMULRADARH");
//    kraNETO.setServerColumnName("NETO");
//    kraNETO.setSqlType(2);
//    kraNETO.setDefault("0");
//    kraNEOP.setCaption("Neoporezivi dio - olakšica");
//    kraNEOP.setColumnName("NEOP");
//    kraNEOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraNEOP.setPrecision(17);
//    kraNEOP.setScale(2);
//    kraNEOP.setDisplayMask("###,###,##0.00");
//    kraNEOP.setDefault("0");
//    kraNEOP.setTableName("KUMULRADARH");
//    kraNEOP.setServerColumnName("NEOP");
//    kraNEOP.setSqlType(2);
//    kraNEOP.setDefault("0");
//    kraISKNEOP.setCaption("Iskorišteni neoporezivi dio");
//    kraISKNEOP.setColumnName("ISKNEOP");
//    kraISKNEOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraISKNEOP.setPrecision(17);
//    kraISKNEOP.setScale(2);
//    kraISKNEOP.setDisplayMask("###,###,##0.00");
//    kraISKNEOP.setDefault("0");
//    kraISKNEOP.setTableName("KUMULRADARH");
//    kraISKNEOP.setServerColumnName("ISKNEOP");
//    kraISKNEOP.setSqlType(2);
//    kraISKNEOP.setDefault("0");
//    kraPOROSN.setCaption("Porezna osnovica");
//    kraPOROSN.setColumnName("POROSN");
//    kraPOROSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraPOROSN.setPrecision(17);
//    kraPOROSN.setScale(2);
//    kraPOROSN.setDisplayMask("###,###,##0.00");
//    kraPOROSN.setDefault("0");
//    kraPOROSN.setTableName("KUMULRADARH");
//    kraPOROSN.setServerColumnName("POROSN");
//    kraPOROSN.setSqlType(2);
//    kraPOROSN.setDefault("0");
//    kraPOR1.setCaption("Porez 1");
//    kraPOR1.setColumnName("POR1");
//    kraPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraPOR1.setPrecision(17);
//    kraPOR1.setScale(2);
//    kraPOR1.setDisplayMask("###,###,##0.00");
//    kraPOR1.setDefault("0");
//    kraPOR1.setTableName("KUMULRADARH");
//    kraPOR1.setServerColumnName("POR1");
//    kraPOR1.setSqlType(2);
//    kraPOR1.setDefault("0");
//    kraPOR2.setCaption("Porez 2");
//    kraPOR2.setColumnName("POR2");
//    kraPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraPOR2.setPrecision(17);
//    kraPOR2.setScale(2);
//    kraPOR2.setDisplayMask("###,###,##0.00");
//    kraPOR2.setDefault("0");
//    kraPOR2.setTableName("KUMULRADARH");
//    kraPOR2.setServerColumnName("POR2");
//    kraPOR2.setSqlType(2);
//    kraPOR2.setDefault("0");
//    kraPOR3.setCaption("Porez 3");
//    kraPOR3.setColumnName("POR3");
//    kraPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraPOR3.setPrecision(17);
//    kraPOR3.setScale(2);
//    kraPOR3.setDisplayMask("###,###,##0.00");
//    kraPOR3.setDefault("0");
//    kraPOR3.setTableName("KUMULRADARH");
//    kraPOR3.setServerColumnName("POR3");
//    kraPOR3.setSqlType(2);
//    kraPOR3.setDefault("0");
//    kraPOR4.setCaption("Porez 4");
//    kraPOR4.setColumnName("POR4");
//    kraPOR4.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraPOR4.setPrecision(17);
//    kraPOR4.setScale(2);
//    kraPOR4.setDisplayMask("###,###,##0.00");
//    kraPOR4.setDefault("0");
//    kraPOR4.setTableName("KUMULRADARH");
//    kraPOR4.setServerColumnName("POR4");
//    kraPOR4.setSqlType(2);
//    kraPOR4.setDefault("0");
//    kraPOR5.setCaption("Porez 5 (invisible)");
//    kraPOR5.setColumnName("POR5");
//    kraPOR5.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraPOR5.setPrecision(17);
//    kraPOR5.setScale(2);
//    kraPOR5.setDisplayMask("###,###,##0.00");
//    kraPOR5.setDefault("0");
//    kraPOR5.setTableName("KUMULRADARH");
//    kraPOR5.setServerColumnName("POR5");
//    kraPOR5.setSqlType(2);
//    kraPOR5.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    kraPOR5.setDefault("0");
//    kraPORUK.setCaption("Porez ukupno");
//    kraPORUK.setColumnName("PORUK");
//    kraPORUK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraPORUK.setPrecision(17);
//    kraPORUK.setScale(2);
//    kraPORUK.setDisplayMask("###,###,##0.00");
//    kraPORUK.setDefault("0");
//    kraPORUK.setTableName("KUMULRADARH");
//    kraPORUK.setServerColumnName("PORUK");
//    kraPORUK.setSqlType(2);
//    kraPORUK.setDefault("0");
//    kraPRIR.setCaption("Prirez");
//    kraPRIR.setColumnName("PRIR");
//    kraPRIR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraPRIR.setPrecision(17);
//    kraPRIR.setScale(2);
//    kraPRIR.setDisplayMask("###,###,##0.00");
//    kraPRIR.setDefault("0");
//    kraPRIR.setTableName("KUMULRADARH");
//    kraPRIR.setServerColumnName("PRIR");
//    kraPRIR.setSqlType(2);
//    kraPRIR.setDefault("0");
//    kraPORIPRIR.setCaption("Porez i prirez");
//    kraPORIPRIR.setColumnName("PORIPRIR");
//    kraPORIPRIR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraPORIPRIR.setPrecision(17);
//    kraPORIPRIR.setScale(2);
//    kraPORIPRIR.setDisplayMask("###,###,##0.00");
//    kraPORIPRIR.setDefault("0");
//    kraPORIPRIR.setTableName("KUMULRADARH");
//    kraPORIPRIR.setServerColumnName("PORIPRIR");
//    kraPORIPRIR.setSqlType(2);
//    kraPORIPRIR.setDefault("0");
//    kraNETO2.setCaption("Iznos nakon oporezivanja");
//    kraNETO2.setColumnName("NETO2");
//    kraNETO2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraNETO2.setPrecision(17);
//    kraNETO2.setScale(2);
//    kraNETO2.setDisplayMask("###,###,##0.00");
//    kraNETO2.setDefault("0");
//    kraNETO2.setTableName("KUMULRADARH");
//    kraNETO2.setServerColumnName("NETO2");
//    kraNETO2.setSqlType(2);
//    kraNETO2.setDefault("0");
//    kraNAKNADE.setCaption("Naknade");
//    kraNAKNADE.setColumnName("NAKNADE");
//    kraNAKNADE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraNAKNADE.setPrecision(17);
//    kraNAKNADE.setScale(2);
//    kraNAKNADE.setDisplayMask("###,###,##0.00");
//    kraNAKNADE.setDefault("0");
//    kraNAKNADE.setTableName("KUMULRADARH");
//    kraNAKNADE.setServerColumnName("NAKNADE");
//    kraNAKNADE.setSqlType(2);
//    kraNAKNADE.setDefault("0");
//    kraNETOPK.setCaption("Iznos prije kredita");
//    kraNETOPK.setColumnName("NETOPK");
//    kraNETOPK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraNETOPK.setPrecision(17);
//    kraNETOPK.setScale(2);
//    kraNETOPK.setDisplayMask("###,###,##0.00");
//    kraNETOPK.setDefault("0");
//    kraNETOPK.setTableName("KUMULRADARH");
//    kraNETOPK.setServerColumnName("NETOPK");
//    kraNETOPK.setSqlType(2);
//    kraNETOPK.setDefault("0");
//    kraKREDITI.setCaption("Iznos kredita");
//    kraKREDITI.setColumnName("KREDITI");
//    kraKREDITI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraKREDITI.setPrecision(17);
//    kraKREDITI.setScale(2);
//    kraKREDITI.setDisplayMask("###,###,##0.00");
//    kraKREDITI.setDefault("0");
//    kraKREDITI.setTableName("KUMULRADARH");
//    kraKREDITI.setServerColumnName("KREDITI");
//    kraKREDITI.setSqlType(2);
//    kraKREDITI.setDefault("0");
//    kraNARUKE.setCaption("Za isplatu");
//    kraNARUKE.setColumnName("NARUKE");
//    kraNARUKE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraNARUKE.setPrecision(17);
//    kraNARUKE.setScale(2);
//    kraNARUKE.setDisplayMask("###,###,##0.00");
//    kraNARUKE.setDefault("0");
//    kraNARUKE.setTableName("KUMULRADARH");
//    kraNARUKE.setServerColumnName("NARUKE");
//    kraNARUKE.setSqlType(2);
//    kraNARUKE.setDefault("0");
//    kraCRADMJ.setCaption("Oznaka radnog mjesta");
//    kraCRADMJ.setColumnName("CRADMJ");
//    kraCRADMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraCRADMJ.setPrecision(6);
//    kraCRADMJ.setTableName("KUMULRADARH");
//    kraCRADMJ.setServerColumnName("CRADMJ");
//    kraCRADMJ.setSqlType(1);
//    kraCSS.setCaption("Oznaka stru\u010Dne spreme");
//    kraCSS.setColumnName("CSS");
//    kraCSS.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraCSS.setPrecision(5);
//    kraCSS.setTableName("KUMULRADARH");
//    kraCSS.setServerColumnName("CSS");
//    kraCSS.setSqlType(1);
//    kraCVRO.setCaption("Oznaka vrste radnog odnosa");
//    kraCVRO.setColumnName("CVRO");
//    kraCVRO.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraCVRO.setPrecision(6);
//    kraCVRO.setTableName("KUMULRADARH");
//    kraCVRO.setServerColumnName("CVRO");
//    kraCVRO.setSqlType(1);
//    kraCISPLMJ.setCaption("Oznaka isplatnog mjesta");
//    kraCISPLMJ.setColumnName("CISPLMJ");
//    kraCISPLMJ.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    kraCISPLMJ.setPrecision(2);
//    kraCISPLMJ.setTableName("KUMULRADARH");
//    kraCISPLMJ.setServerColumnName("CISPLMJ");
//    kraCISPLMJ.setSqlType(5);
//    kraCISPLMJ.setWidth(2);
//    kraCOPCINE.setCaption("Op\u0107ina poreza");
//    kraCOPCINE.setColumnName("COPCINE");
//    kraCOPCINE.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraCOPCINE.setPrecision(3);
//    kraCOPCINE.setTableName("KUMULRADARH");
//    kraCOPCINE.setServerColumnName("COPCINE");
//    kraCOPCINE.setSqlType(1);
//    kraRSINV.setCaption("Oznaka invalidnosti");
//    kraRSINV.setColumnName("RSINV");
//    kraRSINV.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraRSINV.setPrecision(5);
//    kraRSINV.setTableName("KUMULRADARH");
//    kraRSINV.setServerColumnName("RSINV");
//    kraRSINV.setSqlType(1);
//    kraRSOO.setCaption("Osnova osiguranja");
//    kraRSOO.setColumnName("RSOO");
//    kraRSOO.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraRSOO.setPrecision(5);
//    kraRSOO.setTableName("KUMULRADARH");
//    kraRSOO.setServerColumnName("RSOO");
//    kraRSOO.setSqlType(1);
//    kraBRUTOSN.setCaption("Bruto osnovni");
//    kraBRUTOSN.setColumnName("BRUTOSN");
//    kraBRUTOSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraBRUTOSN.setPrecision(17);
//    kraBRUTOSN.setScale(2);
//    kraBRUTOSN.setDisplayMask("###,###,##0.00");
//    kraBRUTOSN.setDefault("0");
//    kraBRUTOSN.setTableName("KUMULRADARH");
//    kraBRUTOSN.setServerColumnName("BRUTOSN");
//    kraBRUTOSN.setSqlType(2);
//    kraBRUTOSN.setDefault("0");
//    kraBRUTDOD.setCaption("Bruto dodatni");
//    kraBRUTDOD.setColumnName("BRUTDOD");
//    kraBRUTDOD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraBRUTDOD.setPrecision(17);
//    kraBRUTDOD.setScale(2);
//    kraBRUTDOD.setDisplayMask("###,###,##0.00");
//    kraBRUTDOD.setDefault("0");
//    kraBRUTDOD.setTableName("KUMULRADARH");
//    kraBRUTDOD.setServerColumnName("BRUTDOD");
//    kraBRUTDOD.setSqlType(2);
//    kraBRUTDOD.setDefault("0");
//    kraBRUTMR.setCaption("Bruto radni staž");
//    kraBRUTMR.setColumnName("BRUTMR");
//    kraBRUTMR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraBRUTMR.setPrecision(17);
//    kraBRUTMR.setScale(2);
//    kraBRUTMR.setDisplayMask("###,###,##0.00");
//    kraBRUTMR.setDefault("0");
//    kraBRUTMR.setTableName("KUMULRADARH");
//    kraBRUTMR.setServerColumnName("BRUTMR");
//    kraBRUTMR.setSqlType(2);
//    kraBRUTMR.setDefault("0");
//    kraBRUTUK.setCaption("Bruto ukupno");
//    kraBRUTUK.setColumnName("BRUTUK");
//    kraBRUTUK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraBRUTUK.setPrecision(17);
//    kraBRUTUK.setScale(2);
//    kraBRUTUK.setDisplayMask("###,###,##0.00");
//    kraBRUTUK.setDefault("0");
//    kraBRUTUK.setTableName("KUMULRADARH");
//    kraBRUTUK.setServerColumnName("BRUTUK");
//    kraBRUTUK.setSqlType(2);
//    kraBRUTUK.setDefault("0");
//    kraGODSTAZ.setCaption("Godine staža");
//    kraGODSTAZ.setColumnName("GODSTAZ");
//    kraGODSTAZ.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    kraGODSTAZ.setPrecision(2);
//    kraGODSTAZ.setTableName("KUMULRADARH");
//    kraGODSTAZ.setServerColumnName("GODSTAZ");
//    kraGODSTAZ.setSqlType(5);
//    kraGODSTAZ.setWidth(2);
//    kraGODSTAZ.setDefault("0");
//    kraSTOPASTAZ.setCaption("Stopa za radni staž");
//    kraSTOPASTAZ.setColumnName("STOPASTAZ");
//    kraSTOPASTAZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraSTOPASTAZ.setPrecision(9);
//    kraSTOPASTAZ.setScale(5);
//    kraSTOPASTAZ.setDisplayMask("###,###,##0.00000");
//    kraSTOPASTAZ.setDefault("0");
//    kraSTOPASTAZ.setTableName("KUMULRADARH");
//    kraSTOPASTAZ.setServerColumnName("STOPASTAZ");
//    kraSTOPASTAZ.setSqlType(2);
//    kraSTOPASTAZ.setDefault("0");
//    kraDATSTAZ.setCaption("Datum postizanja pune godine staža");
//    kraDATSTAZ.setColumnName("DATSTAZ");
//    kraDATSTAZ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
//    kraDATSTAZ.setPrecision(8);
//    kraDATSTAZ.setDisplayMask("dd-MM-yyyy");
////    kraDATSTAZ.setEditMask("dd-MM-yyyy");
//    kraDATSTAZ.setTableName("KUMULRADARH");
//    kraDATSTAZ.setWidth(10);
//    kraDATSTAZ.setServerColumnName("DATSTAZ");
//    kraDATSTAZ.setSqlType(93);
//    kraPODSTAZ.setCaption("Godine staža u poduze\u0107u");
//    kraPODSTAZ.setColumnName("PODSTAZ");
//    kraPODSTAZ.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    kraPODSTAZ.setPrecision(2);
//    kraPODSTAZ.setTableName("KUMULRADARH");
//    kraPODSTAZ.setServerColumnName("PODSTAZ");
//    kraPODSTAZ.setSqlType(5);
//    kraPODSTAZ.setWidth(2);
//    kraDATPODSTAZ.setCaption("Datum postizanja pune godine staža u poduze\u0107u");
//    kraDATPODSTAZ.setColumnName("DATPODSTAZ");
//    kraDATPODSTAZ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
//    kraDATPODSTAZ.setPrecision(8);
////    kraDATPODSTAZ.setDisplayMask("dd-MM-yyyy");
////    kraDATPODSTAZ.setEditMask("dd-MM-yyyy");
//    kraDATPODSTAZ.setTableName("KUMULRADARH");
//    kraDATPODSTAZ.setWidth(10);
//    kraDATPODSTAZ.setServerColumnName("DATPODSTAZ");
//    kraDATPODSTAZ.setSqlType(93);
//    kraNACOBRB.setCaption("Na\u010Din obra\u010Duna bruta");
//    kraNACOBRB.setColumnName("NACOBRB");
//    kraNACOBRB.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraNACOBRB.setPrecision(1);
//    kraNACOBRB.setTableName("KUMULRADARH");
//    kraNACOBRB.setServerColumnName("NACOBRB");
//    kraNACOBRB.setSqlType(1);
//    kraNACOBRB.setDefault("0");
//    kraKOEF.setCaption("Koeficijent");
//    kraKOEF.setColumnName("KOEF");
//    kraKOEF.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraKOEF.setPrecision(9);
//    kraKOEF.setScale(5);
//    kraKOEF.setDisplayMask("###,###,##0.00000");
//    kraKOEF.setDefault("0");
//    kraKOEF.setTableName("KUMULRADARH");
//    kraKOEF.setServerColumnName("KOEF");
//    kraKOEF.setSqlType(2);
//    kraKOEF.setDefault("0");
//    kraKOEFZAR.setCaption("Koeficijent za zaradu");
//    kraKOEFZAR.setColumnName("KOEFZAR");
//    kraKOEFZAR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraKOEFZAR.setPrecision(9);
//    kraKOEFZAR.setScale(5);
//    kraKOEFZAR.setDisplayMask("###,###,##0.00000");
//    kraKOEFZAR.setDefault("0");
//    kraKOEFZAR.setTableName("KUMULRADARH");
//    kraKOEFZAR.setServerColumnName("KOEFZAR");
//    kraKOEFZAR.setSqlType(2);
//    kraKOEFZAR.setDefault("0");
//    kraOLUK.setCaption("Olakšica ukupno");
//    kraOLUK.setColumnName("OLUK");
//    kraOLUK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraOLUK.setPrecision(9);
//    kraOLUK.setScale(5);
//    kraOLUK.setDisplayMask("###,###,##0.00000");
//    kraOLUK.setDefault("0");
//    kraOLUK.setTableName("KUMULRADARH");
//    kraOLUK.setServerColumnName("OLUK");
//    kraOLUK.setSqlType(2);
//    kraOLUK.setDefault("1");
//    kraOLOS.setCaption("Osnovna olakšica");
//    kraOLOS.setColumnName("OLOS");
//    kraOLOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    kraOLOS.setPrecision(9);
//    kraOLOS.setScale(5);
//    kraOLOS.setDisplayMask("###,###,##0.00000");
//    kraOLOS.setDefault("0");
//    kraOLOS.setTableName("KUMULRADARH");
//    kraOLOS.setServerColumnName("OLOS");
//    kraOLOS.setSqlType(2);
//    kraOLOS.setDefault("1");
//    kraCLANOMF.setCaption("\u010Clan OMF (II stup)");
//    kraCLANOMF.setColumnName("CLANOMF");
//    kraCLANOMF.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraCLANOMF.setPrecision(1);
//    kraCLANOMF.setTableName("KUMULRADARH");
//    kraCLANOMF.setServerColumnName("CLANOMF");
//    kraCLANOMF.setSqlType(1);
//    kraCLANOMF.setDefault("N");
//    kraCORG.setCaption("Org. Jedinica");
//    kraCORG.setColumnName("CORG");
//    kraCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraCORG.setPrecision(12);
//    kraCORG.setTableName("KUMULRADARH");
//    kraCORG.setServerColumnName("CORG");
//    kraCORG.setSqlType(1);
//    kraPARAMETRI.setCaption("Parametri");
//    kraPARAMETRI.setColumnName("PARAMETRI");
//    kraPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
//    kraPARAMETRI.setPrecision(20);
//    kraPARAMETRI.setTableName("KUMULRADARH");
//    kraPARAMETRI.setServerColumnName("PARAMETRI");
//    kraPARAMETRI.setSqlType(1);
//    kraPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    kra.setResolver(dm.getQresolver());
//    kra.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Kumulradarh", null, true, Load.ALL));
// setColumns(new Column[] {kraLOKK, kraAKTIV, kraGODOBR, kraMJOBR, kraRBROBR, kraCRADNIK, kraSATI, kraBRUTO, kraDOPRINOSI, kraNETO, kraNEOP, kraISKNEOP,
//        kraPOROSN, kraPOR1, kraPOR2, kraPOR3, kraPOR4, kraPOR5, kraPORUK, kraPRIR, kraPORIPRIR, kraNETO2, kraNAKNADE, kraNETOPK, kraKREDITI, kraNARUKE,
//        kraCRADMJ, kraCSS, kraCVRO, kraCISPLMJ, kraCOPCINE, kraRSINV, kraRSOO, kraBRUTOSN, kraBRUTDOD, kraBRUTMR, kraBRUTUK, kraGODSTAZ, kraSTOPASTAZ,
//        kraDATSTAZ, kraPODSTAZ, kraDATPODSTAZ, kraNACOBRB, kraKOEF, kraKOEFZAR, kraOLUK, kraOLOS, kraCLANOMF, kraCORG, kraPARAMETRI});
//  }
//
//  public void setall() {
//
//    ddl.create("Kumulradarh")
//       .addChar("lokk", 1, "N")
//       .addChar("aktiv", 1, "D")
//       .addShort("godobr", 4, true)
//       .addShort("mjobr", 2, true)
//       .addShort("rbrobr", 3, true)
//       .addChar("cradnik", 6, true)
//       .addFloat("sati", 17, 2)
//       .addFloat("bruto", 17, 2)
//       .addFloat("doprinosi", 17, 2)
//       .addFloat("neto", 17, 2)
//       .addFloat("neop", 17, 2)
//       .addFloat("iskneop", 17, 2)
//       .addFloat("porosn", 17, 2)
//       .addFloat("por1", 17, 2)
//       .addFloat("por2", 17, 2)
//       .addFloat("por3", 17, 2)
//       .addFloat("por4", 17, 2)
//       .addFloat("por5", 17, 2)
//       .addFloat("poruk", 17, 2)
//       .addFloat("prir", 17, 2)
//       .addFloat("poriprir", 17, 2)
//       .addFloat("neto2", 17, 2)
//       .addFloat("naknade", 17, 2)
//       .addFloat("netopk", 17, 2)
//       .addFloat("krediti", 17, 2)
//       .addFloat("naruke", 17, 2)
//       .addChar("cradmj", 6)
//       .addChar("css", 5)
//       .addChar("cvro", 6)
//       .addShort("cisplmj", 2)
//       .addChar("copcine", 3)
//       .addChar("rsinv", 5)
//       .addChar("rsoo", 5)
//       .addFloat("brutosn", 17, 2)
//       .addFloat("brutdod", 17, 2)
//       .addFloat("brutmr", 17, 2)
//       .addFloat("brutuk", 17, 2)
//       .addShort("godstaz", 2)
//       .addFloat("stopastaz", 9, 5)
//       .addDate("datstaz")
//       .addShort("podstaz", 2)
//       .addDate("datpodstaz")
//       .addChar("nacobrb", 1, "0")
//       .addFloat("koef", 9, 5)
//       .addFloat("koefzar", 9, 5)
//       .addFloat("oluk", 9, 5)
//       .addFloat("olos", 9, 5)
//       .addChar("clanomf", 1, "N")
//       .addChar("corg", 12)
//       .addChar("parametri", 20)
//       .addPrimaryKey("godobr,mjobr,rbrobr,cradnik");
//
//
//    Naziv = "Kumulradarh";
//
//    SqlDefTabela = ddl.getCreateTableString();
//
//    String[] idx = new String[] {"mjobr", "rbrobr", "cradnik"};
//    String[] uidx = new String[] {};
//    DefIndex = ddl.getIndices(idx, uidx);
//    NaziviIdx = ddl.getIndexNames(idx, uidx);
//  }
//}
