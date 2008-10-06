/****license*****************************************************************
**   file: Kumulorgarh.java
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



public class Kumulorgarh extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Kumulorgarh Kumulorgarhclass;

  QueryDataSet koa = new raDataSet();

  Column koaLOKK = new Column();
  Column koaAKTIV = new Column();
  Column koaGODOBR = new Column();
  Column koaMJOBR = new Column();
  Column koaRBROBR = new Column();
  Column koaCORG = new Column();
  Column koaCVRO = new Column();
  Column koaKNJIG = new Column();
  Column koaSATI = new Column();
  Column koaBRUTO = new Column();
  Column koaDOPRINOSI = new Column();
  Column koaNETO = new Column();
  Column koaNEOP = new Column();
  Column koaISKNEOP = new Column();
  Column koaPOROSN = new Column();
  Column koaPOR1 = new Column();
  Column koaPOR2 = new Column();
  Column koaPOR3 = new Column();
  Column koaPOR4 = new Column();
  Column koaPOR5 = new Column();
  Column koaPORUK = new Column();
  Column koaPRIR = new Column();
  Column koaPORIPRIR = new Column();
  Column koaNETO2 = new Column();
  Column koaNAKNADE = new Column();
  Column koaNETOPK = new Column();
  Column koaKREDITI = new Column();
  Column koaNARUKE = new Column();
  Column koaDOPRPOD = new Column();
  Column koaCOPCINE = new Column();
  Column koaNACOBRS = new Column();
  Column koaNACOBRB = new Column();
  Column koaSATIMJ = new Column();
  Column koaOSNKOEF = new Column();
  Column koaSATNORMA = new Column();
  Column koaDATUMISPL = new Column();
  Column koaBROJDANA = new Column();
  Column koaSTOPAK = new Column();
  Column koaMINPL = new Column();
  Column koaMINOSDOP = new Column();
  Column koaOSNPOR1 = new Column();
  Column koaOSNPOR2 = new Column();
  Column koaOSNPOR3 = new Column();
  Column koaOSNPOR4 = new Column();
  Column koaOSNPOR5 = new Column();
  Column koaPARAMETRI = new Column();

  public static Kumulorgarh getDataModule() {
    if (Kumulorgarhclass == null) {
      Kumulorgarhclass = new Kumulorgarh();
    }
    return Kumulorgarhclass;
  }

  public QueryDataSet getQueryDataSet() {
    return koa;
  }

  public Kumulorgarh() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    koaLOKK.setCaption("Status zauzetosti");
    koaLOKK.setColumnName("LOKK");
    koaLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    koaLOKK.setPrecision(1);
    koaLOKK.setTableName("KUMULORGARH");
    koaLOKK.setServerColumnName("LOKK");
    koaLOKK.setSqlType(1);
    koaLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    koaLOKK.setDefault("N");
    koaAKTIV.setCaption("Aktivan - neaktivan");
    koaAKTIV.setColumnName("AKTIV");
    koaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    koaAKTIV.setPrecision(1);
    koaAKTIV.setTableName("KUMULORGARH");
    koaAKTIV.setServerColumnName("AKTIV");
    koaAKTIV.setSqlType(1);
    koaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    koaAKTIV.setDefault("D");
    koaGODOBR.setCaption("Godina obra\u010Duna");
    koaGODOBR.setColumnName("GODOBR");
    koaGODOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    koaGODOBR.setPrecision(4);
    koaGODOBR.setRowId(true);
    koaGODOBR.setTableName("KUMULORGARH");
    koaGODOBR.setServerColumnName("GODOBR");
    koaGODOBR.setSqlType(5);
    koaGODOBR.setWidth(4);
    koaMJOBR.setCaption("Mjesec obra\u010Duna");
    koaMJOBR.setColumnName("MJOBR");
    koaMJOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    koaMJOBR.setPrecision(2);
    koaMJOBR.setRowId(true);
    koaMJOBR.setTableName("KUMULORGARH");
    koaMJOBR.setServerColumnName("MJOBR");
    koaMJOBR.setSqlType(5);
    koaMJOBR.setWidth(2);
    koaRBROBR.setCaption("Redni broj obra\u010Duna");
    koaRBROBR.setColumnName("RBROBR");
    koaRBROBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    koaRBROBR.setPrecision(3);
    koaRBROBR.setRowId(true);
    koaRBROBR.setTableName("KUMULORGARH");
    koaRBROBR.setServerColumnName("RBROBR");
    koaRBROBR.setSqlType(5);
    koaRBROBR.setWidth(3);
    koaCORG.setCaption("Org. Jedinica");
    koaCORG.setColumnName("CORG");
    koaCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    koaCORG.setPrecision(12);
    koaCORG.setRowId(true);
    koaCORG.setTableName("KUMULORGARH");
    koaCORG.setServerColumnName("CORG");
    koaCORG.setSqlType(1);
    koaCVRO.setCaption("Vrsta rada");
    koaCVRO.setColumnName("CVRO");
    koaCVRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    koaCVRO.setPrecision(6);
    koaCVRO.setRowId(true);
    koaCVRO.setTableName("KUMULORGARH");
    koaCVRO.setServerColumnName("CVRO");
    koaCVRO.setSqlType(1);
    koaKNJIG.setCaption("Knjigovodstvo");
    koaKNJIG.setColumnName("KNJIG");
    koaKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    koaKNJIG.setPrecision(12);
    koaKNJIG.setTableName("KUMULORGARH");
    koaKNJIG.setServerColumnName("KNJIG");
    koaKNJIG.setSqlType(1);
    koaSATI.setCaption("Sati");
    koaSATI.setColumnName("SATI");
    koaSATI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaSATI.setPrecision(17);
    koaSATI.setScale(2);
    koaSATI.setDisplayMask("###,###,##0.00");
    koaSATI.setDefault("0");
    koaSATI.setTableName("KUMULORGARH");
    koaSATI.setServerColumnName("SATI");
    koaSATI.setSqlType(2);
    koaSATI.setDefault("0");
    koaBRUTO.setCaption("Bruto");
    koaBRUTO.setColumnName("BRUTO");
    koaBRUTO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaBRUTO.setPrecision(17);
    koaBRUTO.setScale(2);
    koaBRUTO.setDisplayMask("###,###,##0.00");
    koaBRUTO.setDefault("0");
    koaBRUTO.setTableName("KUMULORGARH");
    koaBRUTO.setServerColumnName("BRUTO");
    koaBRUTO.setSqlType(2);
    koaBRUTO.setDefault("0");
    koaDOPRINOSI.setCaption("Doprinosi");
    koaDOPRINOSI.setColumnName("DOPRINOSI");
    koaDOPRINOSI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaDOPRINOSI.setPrecision(17);
    koaDOPRINOSI.setScale(2);
    koaDOPRINOSI.setDisplayMask("###,###,##0.00");
    koaDOPRINOSI.setDefault("0");
    koaDOPRINOSI.setTableName("KUMULORGARH");
    koaDOPRINOSI.setServerColumnName("DOPRINOSI");
    koaDOPRINOSI.setSqlType(2);
    koaDOPRINOSI.setDefault("0");
    koaNETO.setCaption("Bruto po odbitku doprinosa");
    koaNETO.setColumnName("NETO");
    koaNETO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaNETO.setPrecision(17);
    koaNETO.setScale(2);
    koaNETO.setDisplayMask("###,###,##0.00");
    koaNETO.setDefault("0");
    koaNETO.setTableName("KUMULORGARH");
    koaNETO.setServerColumnName("NETO");
    koaNETO.setSqlType(2);
    koaNETO.setDefault("0");
    koaNEOP.setCaption("Neoporezivi dio - olakšica");
    koaNEOP.setColumnName("NEOP");
    koaNEOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaNEOP.setPrecision(17);
    koaNEOP.setScale(2);
    koaNEOP.setDisplayMask("###,###,##0.00");
    koaNEOP.setDefault("0");
    koaNEOP.setTableName("KUMULORGARH");
    koaNEOP.setServerColumnName("NEOP");
    koaNEOP.setSqlType(2);
    koaNEOP.setDefault("0");
    koaISKNEOP.setCaption("Iskorišteni neoporezivi dio");
    koaISKNEOP.setColumnName("ISKNEOP");
    koaISKNEOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaISKNEOP.setPrecision(17);
    koaISKNEOP.setScale(2);
    koaISKNEOP.setDisplayMask("###,###,##0.00");
    koaISKNEOP.setDefault("0");
    koaISKNEOP.setTableName("KUMULORGARH");
    koaISKNEOP.setServerColumnName("ISKNEOP");
    koaISKNEOP.setSqlType(2);
    koaISKNEOP.setDefault("0");
    koaPOROSN.setCaption("Porezna osnovica");
    koaPOROSN.setColumnName("POROSN");
    koaPOROSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaPOROSN.setPrecision(17);
    koaPOROSN.setScale(2);
    koaPOROSN.setDisplayMask("###,###,##0.00");
    koaPOROSN.setDefault("0");
    koaPOROSN.setTableName("KUMULORGARH");
    koaPOROSN.setServerColumnName("POROSN");
    koaPOROSN.setSqlType(2);
    koaPOROSN.setDefault("0");
    koaPOR1.setCaption("Porez 1");
    koaPOR1.setColumnName("POR1");
    koaPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaPOR1.setPrecision(17);
    koaPOR1.setScale(2);
    koaPOR1.setDisplayMask("###,###,##0.00");
    koaPOR1.setDefault("0");
    koaPOR1.setTableName("KUMULORGARH");
    koaPOR1.setServerColumnName("POR1");
    koaPOR1.setSqlType(2);
    koaPOR1.setDefault("0");
    koaPOR2.setCaption("Porez 2");
    koaPOR2.setColumnName("POR2");
    koaPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaPOR2.setPrecision(17);
    koaPOR2.setScale(2);
    koaPOR2.setDisplayMask("###,###,##0.00");
    koaPOR2.setDefault("0");
    koaPOR2.setTableName("KUMULORGARH");
    koaPOR2.setServerColumnName("POR2");
    koaPOR2.setSqlType(2);
    koaPOR2.setDefault("0");
    koaPOR3.setCaption("Porez 3");
    koaPOR3.setColumnName("POR3");
    koaPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaPOR3.setPrecision(17);
    koaPOR3.setScale(2);
    koaPOR3.setDisplayMask("###,###,##0.00");
    koaPOR3.setDefault("0");
    koaPOR3.setTableName("KUMULORGARH");
    koaPOR3.setServerColumnName("POR3");
    koaPOR3.setSqlType(2);
    koaPOR3.setDefault("0");
    koaPOR4.setCaption("Porez 4");
    koaPOR4.setColumnName("POR4");
    koaPOR4.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaPOR4.setPrecision(17);
    koaPOR4.setScale(2);
    koaPOR4.setDisplayMask("###,###,##0.00");
    koaPOR4.setDefault("0");
    koaPOR4.setTableName("KUMULORGARH");
    koaPOR4.setServerColumnName("POR4");
    koaPOR4.setSqlType(2);
    koaPOR4.setDefault("0");
    koaPOR5.setCaption("Porez 5 (invisible)");
    koaPOR5.setColumnName("POR5");
    koaPOR5.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaPOR5.setPrecision(17);
    koaPOR5.setScale(2);
    koaPOR5.setDisplayMask("###,###,##0.00");
    koaPOR5.setDefault("0");
    koaPOR5.setTableName("KUMULORGARH");
    koaPOR5.setServerColumnName("POR5");
    koaPOR5.setSqlType(2);
    koaPOR5.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    koaPOR5.setDefault("0");
    koaPORUK.setCaption("Porez ukupno");
    koaPORUK.setColumnName("PORUK");
    koaPORUK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaPORUK.setPrecision(17);
    koaPORUK.setScale(2);
    koaPORUK.setDisplayMask("###,###,##0.00");
    koaPORUK.setDefault("0");
    koaPORUK.setTableName("KUMULORGARH");
    koaPORUK.setServerColumnName("PORUK");
    koaPORUK.setSqlType(2);
    koaPORUK.setDefault("0");
    koaPRIR.setCaption("Prirez");
    koaPRIR.setColumnName("PRIR");
    koaPRIR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaPRIR.setPrecision(17);
    koaPRIR.setScale(2);
    koaPRIR.setDisplayMask("###,###,##0.00");
    koaPRIR.setDefault("0");
    koaPRIR.setTableName("KUMULORGARH");
    koaPRIR.setServerColumnName("PRIR");
    koaPRIR.setSqlType(2);
    koaPRIR.setDefault("0");
    koaPORIPRIR.setCaption("Porez i prirez");
    koaPORIPRIR.setColumnName("PORIPRIR");
    koaPORIPRIR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaPORIPRIR.setPrecision(17);
    koaPORIPRIR.setScale(2);
    koaPORIPRIR.setDisplayMask("###,###,##0.00");
    koaPORIPRIR.setDefault("0");
    koaPORIPRIR.setTableName("KUMULORGARH");
    koaPORIPRIR.setServerColumnName("PORIPRIR");
    koaPORIPRIR.setSqlType(2);
    koaPORIPRIR.setDefault("0");
    koaNETO2.setCaption("Iznos nakon oporezivanja");
    koaNETO2.setColumnName("NETO2");
    koaNETO2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaNETO2.setPrecision(17);
    koaNETO2.setScale(2);
    koaNETO2.setDisplayMask("###,###,##0.00");
    koaNETO2.setDefault("0");
    koaNETO2.setTableName("KUMULORGARH");
    koaNETO2.setServerColumnName("NETO2");
    koaNETO2.setSqlType(2);
    koaNETO2.setDefault("0");
    koaNAKNADE.setCaption("Naknade");
    koaNAKNADE.setColumnName("NAKNADE");
    koaNAKNADE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaNAKNADE.setPrecision(17);
    koaNAKNADE.setScale(2);
    koaNAKNADE.setDisplayMask("###,###,##0.00");
    koaNAKNADE.setDefault("0");
    koaNAKNADE.setTableName("KUMULORGARH");
    koaNAKNADE.setServerColumnName("NAKNADE");
    koaNAKNADE.setSqlType(2);
    koaNAKNADE.setDefault("0");
    koaNETOPK.setCaption("Iznos prije kredita");
    koaNETOPK.setColumnName("NETOPK");
    koaNETOPK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaNETOPK.setPrecision(17);
    koaNETOPK.setScale(2);
    koaNETOPK.setDisplayMask("###,###,##0.00");
    koaNETOPK.setDefault("0");
    koaNETOPK.setTableName("KUMULORGARH");
    koaNETOPK.setServerColumnName("NETOPK");
    koaNETOPK.setSqlType(2);
    koaNETOPK.setDefault("0");
    koaKREDITI.setCaption("Iznos kredita");
    koaKREDITI.setColumnName("KREDITI");
    koaKREDITI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaKREDITI.setPrecision(17);
    koaKREDITI.setScale(2);
    koaKREDITI.setDisplayMask("###,###,##0.00");
    koaKREDITI.setDefault("0");
    koaKREDITI.setTableName("KUMULORGARH");
    koaKREDITI.setServerColumnName("KREDITI");
    koaKREDITI.setSqlType(2);
    koaKREDITI.setDefault("0");
    koaNARUKE.setCaption("Za isplatu");
    koaNARUKE.setColumnName("NARUKE");
    koaNARUKE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaNARUKE.setPrecision(17);
    koaNARUKE.setScale(2);
    koaNARUKE.setDisplayMask("###,###,##0.00");
    koaNARUKE.setDefault("0");
    koaNARUKE.setTableName("KUMULORGARH");
    koaNARUKE.setServerColumnName("NARUKE");
    koaNARUKE.setSqlType(2);
    koaNARUKE.setDefault("0");
    koaDOPRPOD.setCaption("Doprinosi poduze\u0107a");
    koaDOPRPOD.setColumnName("DOPRPOD");
    koaDOPRPOD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaDOPRPOD.setPrecision(17);
    koaDOPRPOD.setScale(2);
    koaDOPRPOD.setDisplayMask("###,###,##0.00");
    koaDOPRPOD.setDefault("0");
    koaDOPRPOD.setTableName("KUMULORGARH");
    koaDOPRPOD.setServerColumnName("DOPRPOD");
    koaDOPRPOD.setSqlType(2);
    koaDOPRPOD.setDefault("0");
    koaCOPCINE.setCaption("Op\u0107ina sjedišta");
    koaCOPCINE.setColumnName("COPCINE");
    koaCOPCINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    koaCOPCINE.setPrecision(3);
    koaCOPCINE.setTableName("KUMULORGARH");
    koaCOPCINE.setServerColumnName("COPCINE");
    koaCOPCINE.setSqlType(1);
    koaNACOBRS.setCaption("Na\u010Din obra\u010Duna satnice");
    koaNACOBRS.setColumnName("NACOBRS");
    koaNACOBRS.setDataType(com.borland.dx.dataset.Variant.STRING);
    koaNACOBRS.setPrecision(1);
    koaNACOBRS.setTableName("KUMULORGARH");
    koaNACOBRS.setServerColumnName("NACOBRS");
    koaNACOBRS.setSqlType(1);
    koaNACOBRS.setDefault("1");
    koaNACOBRB.setCaption("Na\u010Din obra\u010Duna bruta");
    koaNACOBRB.setColumnName("NACOBRB");
    koaNACOBRB.setDataType(com.borland.dx.dataset.Variant.STRING);
    koaNACOBRB.setPrecision(1);
    koaNACOBRB.setTableName("KUMULORGARH");
    koaNACOBRB.setServerColumnName("NACOBRB");
    koaNACOBRB.setSqlType(1);
    koaNACOBRB.setDefault("1");
    koaSATIMJ.setCaption("Sati za fiksnu satnicu");
    koaSATIMJ.setColumnName("SATIMJ");
    koaSATIMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaSATIMJ.setPrecision(17);
    koaSATIMJ.setScale(2);
    koaSATIMJ.setDisplayMask("###,###,##0.00");
    koaSATIMJ.setDefault("0");
    koaSATIMJ.setTableName("KUMULORGARH");
    koaSATIMJ.setServerColumnName("SATIMJ");
    koaSATIMJ.setSqlType(2);
    koaSATIMJ.setDefault("182");
    koaOSNKOEF.setCaption("Osnovica za primjenu koeficijenta");
    koaOSNKOEF.setColumnName("OSNKOEF");
    koaOSNKOEF.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaOSNKOEF.setPrecision(17);
    koaOSNKOEF.setScale(6);
    koaOSNKOEF.setDisplayMask("###,###,##0.000000");
    koaOSNKOEF.setDefault("0");
    koaOSNKOEF.setTableName("KUMULORGARH");
    koaOSNKOEF.setServerColumnName("OSNKOEF");
    koaOSNKOEF.setSqlType(2);
    koaOSNKOEF.setDefault("0");
    koaSATNORMA.setCaption("Satnica za rad po normi");
    koaSATNORMA.setColumnName("SATNORMA");
    koaSATNORMA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaSATNORMA.setPrecision(17);
    koaSATNORMA.setScale(2);
    koaSATNORMA.setDisplayMask("###,###,##0.00");
    koaSATNORMA.setDefault("0");
    koaSATNORMA.setTableName("KUMULORGARH");
    koaSATNORMA.setServerColumnName("SATNORMA");
    koaSATNORMA.setSqlType(2);
    koaSATNORMA.setDefault("0");
    koaDATUMISPL.setCaption("Datum isplate");
    koaDATUMISPL.setColumnName("DATUMISPL");
    koaDATUMISPL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    koaDATUMISPL.setPrecision(8);
    koaDATUMISPL.setDisplayMask("dd-MM-yyyy");
//    koaDATUMISPL.setEditMask("dd-MM-yyyy");
    koaDATUMISPL.setTableName("KUMULORGARH");
    koaDATUMISPL.setWidth(10);
    koaDATUMISPL.setServerColumnName("DATUMISPL");
    koaDATUMISPL.setSqlType(93);
    koaBROJDANA.setCaption("Broj radnih dana u mjesecu");
    koaBROJDANA.setColumnName("BROJDANA");
    koaBROJDANA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    koaBROJDANA.setPrecision(2);
    koaBROJDANA.setTableName("KUMULORGARH");
    koaBROJDANA.setServerColumnName("BROJDANA");
    koaBROJDANA.setSqlType(5);
    koaBROJDANA.setWidth(2);
    koaSTOPAK.setCaption("stopa akontacije");
    koaSTOPAK.setColumnName("STOPAK");
    koaSTOPAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaSTOPAK.setPrecision(17);
    koaSTOPAK.setScale(2);
    koaSTOPAK.setDisplayMask("###,###,##0.00");
    koaSTOPAK.setDefault("0");
    koaSTOPAK.setTableName("KUMULORGARH");
    koaSTOPAK.setServerColumnName("STOPAK");
    koaSTOPAK.setSqlType(2);
    koaSTOPAK.setDefault("0");
    koaMINPL.setCaption("Minimalna pla\u0107a - olakšice");
    koaMINPL.setColumnName("MINPL");
    koaMINPL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaMINPL.setPrecision(17);
    koaMINPL.setScale(2);
    koaMINPL.setDisplayMask("###,###,##0.00");
    koaMINPL.setDefault("0");
    koaMINPL.setTableName("KUMULORGARH");
    koaMINPL.setServerColumnName("MINPL");
    koaMINPL.setSqlType(2);
    koaMINPL.setDefault("1250");
    koaMINOSDOP.setCaption("Minimalna osnovica za doprinose");
    koaMINOSDOP.setColumnName("MINOSDOP");
    koaMINOSDOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaMINOSDOP.setPrecision(17);
    koaMINOSDOP.setScale(2);
    koaMINOSDOP.setDisplayMask("###,###,##0.00");
    koaMINOSDOP.setDefault("0");
    koaMINOSDOP.setTableName("KUMULORGARH");
    koaMINOSDOP.setServerColumnName("MINOSDOP");
    koaMINOSDOP.setSqlType(2);
    koaMINOSDOP.setDefault("1800");
    koaOSNPOR1.setCaption("Osnovica za prvi porez");
    koaOSNPOR1.setColumnName("OSNPOR1");
    koaOSNPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaOSNPOR1.setPrecision(17);
    koaOSNPOR1.setScale(2);
    koaOSNPOR1.setDisplayMask("###,###,##0.00");
    koaOSNPOR1.setDefault("0");
    koaOSNPOR1.setTableName("KUMULORGARH");
    koaOSNPOR1.setServerColumnName("OSNPOR1");
    koaOSNPOR1.setSqlType(2);
    koaOSNPOR1.setDefault("2500");
    koaOSNPOR2.setCaption("Osnovica za drugi porez");
    koaOSNPOR2.setColumnName("OSNPOR2");
    koaOSNPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaOSNPOR2.setPrecision(17);
    koaOSNPOR2.setScale(2);
    koaOSNPOR2.setDisplayMask("###,###,##0.00");
    koaOSNPOR2.setDefault("0");
    koaOSNPOR2.setTableName("KUMULORGARH");
    koaOSNPOR2.setServerColumnName("OSNPOR2");
    koaOSNPOR2.setSqlType(2);
    koaOSNPOR2.setDefault("6250");
    koaOSNPOR3.setCaption("Osnovica za tre\u0107i porez");
    koaOSNPOR3.setColumnName("OSNPOR3");
    koaOSNPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaOSNPOR3.setPrecision(17);
    koaOSNPOR3.setScale(2);
    koaOSNPOR3.setDisplayMask("###,###,##0.00");
    koaOSNPOR3.setDefault("0");
    koaOSNPOR3.setTableName("KUMULORGARH");
    koaOSNPOR3.setServerColumnName("OSNPOR3");
    koaOSNPOR3.setSqlType(2);
    koaOSNPOR3.setDefault("0");
    koaOSNPOR4.setCaption("Osnovica za \u010Detvrti porez");
    koaOSNPOR4.setColumnName("OSNPOR4");
    koaOSNPOR4.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaOSNPOR4.setPrecision(17);
    koaOSNPOR4.setScale(2);
    koaOSNPOR4.setDisplayMask("###,###,##0.00");
    koaOSNPOR4.setDefault("0");
    koaOSNPOR4.setTableName("KUMULORGARH");
    koaOSNPOR4.setServerColumnName("OSNPOR4");
    koaOSNPOR4.setSqlType(2);
    koaOSNPOR4.setDefault("0");
    koaOSNPOR5.setCaption("Osnovica za peti porez");
    koaOSNPOR5.setColumnName("OSNPOR5");
    koaOSNPOR5.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    koaOSNPOR5.setPrecision(17);
    koaOSNPOR5.setScale(2);
    koaOSNPOR5.setDisplayMask("###,###,##0.00");
    koaOSNPOR5.setDefault("0");
    koaOSNPOR5.setTableName("KUMULORGARH");
    koaOSNPOR5.setServerColumnName("OSNPOR5");
    koaOSNPOR5.setSqlType(2);
    koaOSNPOR5.setDefault("0");
    koaPARAMETRI.setCaption("Parametri");
    koaPARAMETRI.setColumnName("PARAMETRI");
    koaPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    koaPARAMETRI.setPrecision(20);
    koaPARAMETRI.setTableName("KUMULORGARH");
    koaPARAMETRI.setServerColumnName("PARAMETRI");
    koaPARAMETRI.setSqlType(1);
    koaPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    koa.setResolver(dm.getQresolver());
    koa.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Kumulorgarh", null, true, Load.ALL));
 setColumns(new Column[] {koaLOKK, koaAKTIV, koaGODOBR, koaMJOBR, koaRBROBR, koaCORG, koaCVRO, koaKNJIG, koaSATI, koaBRUTO, koaDOPRINOSI, koaNETO,
        koaNEOP, koaISKNEOP, koaPOROSN, koaPOR1, koaPOR2, koaPOR3, koaPOR4, koaPOR5, koaPORUK, koaPRIR, koaPORIPRIR, koaNETO2, koaNAKNADE, koaNETOPK,
        koaKREDITI, koaNARUKE, koaDOPRPOD, koaCOPCINE, koaNACOBRS, koaNACOBRB, koaSATIMJ, koaOSNKOEF, koaSATNORMA, koaDATUMISPL, koaBROJDANA, koaSTOPAK,
        koaMINPL, koaMINOSDOP, koaOSNPOR1, koaOSNPOR2, koaOSNPOR3, koaOSNPOR4, koaOSNPOR5, koaPARAMETRI});
  }

  public void setall() {

    ddl.create("Kumulorgarh")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("godobr", 4, true)
       .addShort("mjobr", 2, true)
       .addShort("rbrobr", 3, true)
       .addChar("corg", 12, true)
       .addChar("cvro", 6, true)
       .addChar("knjig", 12)
       .addFloat("sati", 17, 2)
       .addFloat("bruto", 17, 2)
       .addFloat("doprinosi", 17, 2)
       .addFloat("neto", 17, 2)
       .addFloat("neop", 17, 2)
       .addFloat("iskneop", 17, 2)
       .addFloat("porosn", 17, 2)
       .addFloat("por1", 17, 2)
       .addFloat("por2", 17, 2)
       .addFloat("por3", 17, 2)
       .addFloat("por4", 17, 2)
       .addFloat("por5", 17, 2)
       .addFloat("poruk", 17, 2)
       .addFloat("prir", 17, 2)
       .addFloat("poriprir", 17, 2)
       .addFloat("neto2", 17, 2)
       .addFloat("naknade", 17, 2)
       .addFloat("netopk", 17, 2)
       .addFloat("krediti", 17, 2)
       .addFloat("naruke", 17, 2)
       .addFloat("doprpod", 17, 2)
       .addChar("copcine", 3)
       .addChar("nacobrs", 1, "1")
       .addChar("nacobrb", 1, "1")
       .addFloat("satimj", 17, 2)
       .addFloat("osnkoef", 17, 6)
       .addFloat("satnorma", 17, 2)
       .addDate("datumispl")
       .addShort("brojdana", 2)
       .addFloat("stopak", 17, 2)
       .addFloat("minpl", 17, 2)
       .addFloat("minosdop", 17, 2)
       .addFloat("osnpor1", 17, 2)
       .addFloat("osnpor2", 17, 2)
       .addFloat("osnpor3", 17, 2)
       .addFloat("osnpor4", 17, 2)
       .addFloat("osnpor5", 17, 2)
       .addChar("parametri", 20)
       .addPrimaryKey("godobr,mjobr,rbrobr,corg,cvro");


    Naziv = "Kumulorgarh";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"mjobr", "rbrobr",};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
