/****license*****************************************************************
**   file: OS_SI.java
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



public class OS_SI extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_SI OS_SIclass;

  QueryDataSet ossi = new QueryDataSet();

  Column ossiLOKK = new Column();
  Column ossiAKTIV = new Column();
  Column ossiCORG = new Column();
  Column ossiCORG2 = new Column();
  Column ossiINVBROJ = new Column();
  Column ossiNAZSREDSTVA = new Column();
  Column ossiCOBJEKT = new Column();
  Column ossiCLOKACIJE = new Column();
  Column ossiCARTIKLA = new Column();
  Column ossiCPAR = new Column();
  Column ossiBROJKONTA = new Column();
  Column ossiCGRUPE = new Column();
  Column ossiCSKUPINE = new Column();
  Column ossiDATPROMJENE = new Column();
  Column ossiDATLIKVIDACIJE = new Column();
  Column ossiGODPROIZ = new Column();
  Column ossiPORIJEKLO = new Column();
  Column ossiRADNIK = new Column();
  Column ossiNABVRIJED = new Column();
  Column ossiOSNPOCETAK = new Column();
  Column ossiOSNDUGUJE = new Column();
  Column ossiOSNPOTRAZUJE = new Column();
  Column ossiREVOSN = new Column();
  Column ossiISPPOCETAK = new Column();
  Column ossiISPDUGUJE = new Column();
  Column ossiISPPOTRAZUJE = new Column();
  Column ossiREVISP = new Column();
  Column ossiAMORTIZACIJA = new Column();
  Column ossiREVAMOR = new Column();
  Column ossiPAMORTIZACIJA = new Column();
  Column ossiDOKUMENT = new Column();
  Column ossiSALDO = new Column();
  Column ossiSTATUS = new Column();
  Column ossiDATNABAVE = new Column();
  Column ossiDATAKTIVIRANJA = new Column();
  Column ossiOSNOVICA = new Column();
  Column ossiISPRAVAK = new Column();
  Column ossiOLDCORG = new Column();
  Column ossiCRADNIK = new Column();
  Column ossiSTARISTATUS = new Column();
  Column ossiOJINV = new Column();

  public static OS_SI getDataModule() {
    if (OS_SIclass == null) {
      OS_SIclass = new OS_SI();
    }
    return OS_SIclass;
  }

  public QueryDataSet getQueryDataSet() {
    return ossi;
  }

  public OS_SI() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    ossiLOKK.setCaption("Status zauzetosti");
    ossiLOKK.setColumnName("LOKK");
    ossiLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiLOKK.setPrecision(1);
    ossiLOKK.setTableName("OS_SI");
    ossiLOKK.setServerColumnName("LOKK");
    ossiLOKK.setSqlType(1);
    ossiLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ossiLOKK.setDefault("N");
    ossiAKTIV.setCaption("Aktivan - neaktivan");
    ossiAKTIV.setColumnName("AKTIV");
    ossiAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiAKTIV.setPrecision(1);
    ossiAKTIV.setTableName("OS_SI");
    ossiAKTIV.setServerColumnName("AKTIV");
    ossiAKTIV.setSqlType(1);
    ossiAKTIV.setDefault("D");
    ossiCORG.setCaption("Knigovodstvo");
    ossiCORG.setColumnName("CORG");
    ossiCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiCORG.setPrecision(12);
    ossiCORG.setTableName("OS_SI");
    ossiCORG.setServerColumnName("CORG");
    ossiCORG.setSqlType(1);
    ossiCORG2.setCaption("OJ");
    ossiCORG2.setColumnName("CORG2");
    ossiCORG2.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiCORG2.setPrecision(12);
    ossiCORG2.setRowId(true);
    ossiCORG2.setTableName("OS_SI");
    ossiCORG2.setServerColumnName("CORG2");
    ossiCORG2.setSqlType(1);
    ossiCORG2.setWidth(6);
    ossiINVBROJ.setCaption("IBroj");
    ossiINVBROJ.setColumnName("INVBROJ");
    ossiINVBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiINVBROJ.setPrecision(10);
    ossiINVBROJ.setRowId(true);
    ossiINVBROJ.setTableName("OS_SI");
    ossiINVBROJ.setServerColumnName("INVBROJ");
    ossiINVBROJ.setSqlType(1);
    ossiNAZSREDSTVA.setCaption("Naziv");
    ossiNAZSREDSTVA.setColumnName("NAZSREDSTVA");
    ossiNAZSREDSTVA.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiNAZSREDSTVA.setPrecision(50);
    ossiNAZSREDSTVA.setTableName("OS_SI");
    ossiNAZSREDSTVA.setServerColumnName("NAZSREDSTVA");
    ossiNAZSREDSTVA.setSqlType(1);
    ossiNAZSREDSTVA.setWidth(30);
    ossiCOBJEKT.setCaption("Objekt");
    ossiCOBJEKT.setColumnName("COBJEKT");
    ossiCOBJEKT.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiCOBJEKT.setPrecision(6);
    ossiCOBJEKT.setTableName("OS_SI");
    ossiCOBJEKT.setServerColumnName("COBJEKT");
    ossiCOBJEKT.setSqlType(1);
    ossiCLOKACIJE.setCaption("Lokacija");
    ossiCLOKACIJE.setColumnName("CLOKACIJE");
    ossiCLOKACIJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiCLOKACIJE.setPrecision(12);
    ossiCLOKACIJE.setTableName("OS_SI");
    ossiCLOKACIJE.setServerColumnName("CLOKACIJE");
    ossiCLOKACIJE.setSqlType(1);
    ossiCARTIKLA.setCaption("Artikl");
    ossiCARTIKLA.setColumnName("CARTIKLA");
    ossiCARTIKLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiCARTIKLA.setPrecision(6);
    ossiCARTIKLA.setTableName("OS_SI");
    ossiCARTIKLA.setServerColumnName("CARTIKLA");
    ossiCARTIKLA.setSqlType(1);
    ossiCPAR.setCaption("Partner");
    ossiCPAR.setColumnName("CPAR");
    ossiCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    ossiCPAR.setPrecision(6);
    ossiCPAR.setTableName("OS_SI");
    ossiCPAR.setServerColumnName("CPAR");
    ossiCPAR.setSqlType(4);
    ossiCPAR.setWidth(6);
    ossiBROJKONTA.setCaption("Konto");
    ossiBROJKONTA.setColumnName("BROJKONTA");
    ossiBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiBROJKONTA.setPrecision(8);
    ossiBROJKONTA.setTableName("OS_SI");
    ossiBROJKONTA.setServerColumnName("BROJKONTA");
    ossiBROJKONTA.setSqlType(1);
    ossiCGRUPE.setCaption("Amor. grupa");
    ossiCGRUPE.setColumnName("CGRUPE");
    ossiCGRUPE.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiCGRUPE.setPrecision(6);
    ossiCGRUPE.setTableName("OS_SI");
    ossiCGRUPE.setServerColumnName("CGRUPE");
    ossiCGRUPE.setSqlType(1);
    ossiCSKUPINE.setCaption("Reva. skupina");
    ossiCSKUPINE.setColumnName("CSKUPINE");
    ossiCSKUPINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiCSKUPINE.setPrecision(6);
    ossiCSKUPINE.setTableName("OS_SI");
    ossiCSKUPINE.setServerColumnName("CSKUPINE");
    ossiCSKUPINE.setSqlType(1);
    ossiDATPROMJENE.setCaption("Datum promjene");
    ossiDATPROMJENE.setColumnName("DATPROMJENE");
    ossiDATPROMJENE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    ossiDATPROMJENE.setPrecision(8);
    ossiDATPROMJENE.setDisplayMask("dd-MM-yyyy");
//    ossiDATPROMJENE.setEditMask("dd-MM-yyyy");
    ossiDATPROMJENE.setTableName("OS_SI");
    ossiDATPROMJENE.setServerColumnName("DATPROMJENE");
    ossiDATPROMJENE.setSqlType(93);
    ossiDATPROMJENE.setWidth(10);
    ossiDATLIKVIDACIJE.setCaption("Datum likvidacije");
    ossiDATLIKVIDACIJE.setColumnName("DATLIKVIDACIJE");
    ossiDATLIKVIDACIJE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    ossiDATLIKVIDACIJE.setPrecision(8);
    ossiDATLIKVIDACIJE.setDisplayMask("dd-MM-yyyy");
//    ossiDATLIKVIDACIJE.setEditMask("dd-MM-yyyy");
    ossiDATLIKVIDACIJE.setTableName("OS_SI");
    ossiDATLIKVIDACIJE.setServerColumnName("DATLIKVIDACIJE");
    ossiDATLIKVIDACIJE.setSqlType(93);
    ossiDATLIKVIDACIJE.setWidth(10);
    ossiGODPROIZ.setCaption("Godina proizvodnje");
    ossiGODPROIZ.setColumnName("GODPROIZ");
    ossiGODPROIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiGODPROIZ.setPrecision(4);
    ossiGODPROIZ.setTableName("OS_SI");
    ossiGODPROIZ.setServerColumnName("GODPROIZ");
    ossiGODPROIZ.setSqlType(1);
    ossiPORIJEKLO.setCaption("Porijeklo");
    ossiPORIJEKLO.setColumnName("PORIJEKLO");
    ossiPORIJEKLO.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiPORIJEKLO.setPrecision(2);
    ossiPORIJEKLO.setTableName("OS_SI");
    ossiPORIJEKLO.setServerColumnName("PORIJEKLO");
    ossiPORIJEKLO.setSqlType(1);
    ossiRADNIK.setCaption("Radnik");
    ossiRADNIK.setColumnName("RADNIK");
    ossiRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiRADNIK.setPrecision(6);
    ossiRADNIK.setTableName("OS_SI");
    ossiRADNIK.setServerColumnName("RADNIK");
    ossiRADNIK.setSqlType(1);
    ossiNABVRIJED.setCaption("Nabavna vrijednost");
    ossiNABVRIJED.setColumnName("NABVRIJED");
    ossiNABVRIJED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiNABVRIJED.setPrecision(17);
    ossiNABVRIJED.setScale(2);
    ossiNABVRIJED.setDisplayMask("###,###,##0.00");
    ossiNABVRIJED.setDefault("0");
    ossiNABVRIJED.setTableName("OS_SI");
    ossiNABVRIJED.setServerColumnName("NABVRIJED");
    ossiNABVRIJED.setSqlType(2);
    ossiNABVRIJED.setDefault("0");
    ossiOSNPOCETAK.setCaption("Osnovica po\u010Detak");
    ossiOSNPOCETAK.setColumnName("OSNPOCETAK");
    ossiOSNPOCETAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiOSNPOCETAK.setPrecision(17);
    ossiOSNPOCETAK.setScale(2);
    ossiOSNPOCETAK.setDisplayMask("###,###,##0.00");
    ossiOSNPOCETAK.setDefault("0");
    ossiOSNPOCETAK.setTableName("OS_SI");
    ossiOSNPOCETAK.setServerColumnName("OSNPOCETAK");
    ossiOSNPOCETAK.setSqlType(2);
    ossiOSNPOCETAK.setDefault("0");
    ossiOSNDUGUJE.setCaption("OsnovicaD");
    ossiOSNDUGUJE.setColumnName("OSNDUGUJE");
    ossiOSNDUGUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiOSNDUGUJE.setPrecision(17);
    ossiOSNDUGUJE.setScale(2);
    ossiOSNDUGUJE.setDisplayMask("###,###,##0.00");
    ossiOSNDUGUJE.setDefault("0");
    ossiOSNDUGUJE.setTableName("OS_SI");
    ossiOSNDUGUJE.setServerColumnName("OSNDUGUJE");
    ossiOSNDUGUJE.setSqlType(2);
    ossiOSNDUGUJE.setDefault("0");
    ossiOSNPOTRAZUJE.setCaption("OsnovicaP");
    ossiOSNPOTRAZUJE.setColumnName("OSNPOTRAZUJE");
    ossiOSNPOTRAZUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiOSNPOTRAZUJE.setPrecision(17);
    ossiOSNPOTRAZUJE.setScale(2);
    ossiOSNPOTRAZUJE.setDisplayMask("###,###,##0.00");
    ossiOSNPOTRAZUJE.setDefault("0");
    ossiOSNPOTRAZUJE.setTableName("OS_SI");
    ossiOSNPOTRAZUJE.setServerColumnName("OSNPOTRAZUJE");
    ossiOSNPOTRAZUJE.setSqlType(2);
    ossiOSNPOTRAZUJE.setDefault("0");
    ossiREVOSN.setCaption("Revalorizacija osnovice");
    ossiREVOSN.setColumnName("REVOSN");
    ossiREVOSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiREVOSN.setPrecision(17);
    ossiREVOSN.setScale(2);
    ossiREVOSN.setDisplayMask("###,###,##0.00");
    ossiREVOSN.setDefault("0");
    ossiREVOSN.setTableName("OS_SI");
    ossiREVOSN.setServerColumnName("REVOSN");
    ossiREVOSN.setSqlType(2);
    ossiREVOSN.setDefault("0");
    ossiISPPOCETAK.setCaption("Ispravak po\u010Detak");
    ossiISPPOCETAK.setColumnName("ISPPOCETAK");
    ossiISPPOCETAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiISPPOCETAK.setPrecision(17);
    ossiISPPOCETAK.setScale(2);
    ossiISPPOCETAK.setDisplayMask("###,###,##0.00");
    ossiISPPOCETAK.setDefault("0");
    ossiISPPOCETAK.setTableName("OS_SI");
    ossiISPPOCETAK.setServerColumnName("ISPPOCETAK");
    ossiISPPOCETAK.setSqlType(2);
    ossiISPPOCETAK.setDefault("0");
    ossiISPDUGUJE.setCaption("IspravakD");
    ossiISPDUGUJE.setColumnName("ISPDUGUJE");
    ossiISPDUGUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiISPDUGUJE.setPrecision(17);
    ossiISPDUGUJE.setScale(2);
    ossiISPDUGUJE.setDisplayMask("###,###,##0.00");
    ossiISPDUGUJE.setDefault("0");
    ossiISPDUGUJE.setTableName("OS_SI");
    ossiISPDUGUJE.setServerColumnName("ISPDUGUJE");
    ossiISPDUGUJE.setSqlType(2);
    ossiISPDUGUJE.setDefault("0");
    ossiISPPOTRAZUJE.setCaption("IspravakP");
    ossiISPPOTRAZUJE.setColumnName("ISPPOTRAZUJE");
    ossiISPPOTRAZUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiISPPOTRAZUJE.setPrecision(17);
    ossiISPPOTRAZUJE.setScale(2);
    ossiISPPOTRAZUJE.setDisplayMask("###,###,##0.00");
    ossiISPPOTRAZUJE.setDefault("0");
    ossiISPPOTRAZUJE.setTableName("OS_SI");
    ossiISPPOTRAZUJE.setServerColumnName("ISPPOTRAZUJE");
    ossiISPPOTRAZUJE.setSqlType(2);
    ossiISPPOTRAZUJE.setDefault("0");
    ossiREVISP.setCaption("Revalorizacija ispravka");
    ossiREVISP.setColumnName("REVISP");
    ossiREVISP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiREVISP.setPrecision(17);
    ossiREVISP.setScale(2);
    ossiREVISP.setDisplayMask("###,###,##0.00");
    ossiREVISP.setDefault("0");
    ossiREVISP.setTableName("OS_SI");
    ossiREVISP.setServerColumnName("REVISP");
    ossiREVISP.setSqlType(2);
    ossiREVISP.setDefault("0");
    ossiAMORTIZACIJA.setCaption("Amortizacija");
    ossiAMORTIZACIJA.setColumnName("AMORTIZACIJA");
    ossiAMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiAMORTIZACIJA.setPrecision(17);
    ossiAMORTIZACIJA.setScale(2);
    ossiAMORTIZACIJA.setDisplayMask("###,###,##0.00");
    ossiAMORTIZACIJA.setDefault("0");
    ossiAMORTIZACIJA.setTableName("OS_SI");
    ossiAMORTIZACIJA.setServerColumnName("AMORTIZACIJA");
    ossiAMORTIZACIJA.setSqlType(2);
    ossiAMORTIZACIJA.setDefault("0");
    ossiREVAMOR.setCaption("Amortizacija revalorizacije");
    ossiREVAMOR.setColumnName("REVAMOR");
    ossiREVAMOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiREVAMOR.setPrecision(17);
    ossiREVAMOR.setScale(2);
    ossiREVAMOR.setDisplayMask("###,###,##0.00");
    ossiREVAMOR.setDefault("0");
    ossiREVAMOR.setTableName("OS_SI");
    ossiREVAMOR.setServerColumnName("REVAMOR");
    ossiREVAMOR.setSqlType(2);
    ossiREVAMOR.setDefault("0");
    ossiPAMORTIZACIJA.setCaption("Povišena amortizacija");
    ossiPAMORTIZACIJA.setColumnName("PAMORTIZACIJA");
    ossiPAMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiPAMORTIZACIJA.setPrecision(17);
    ossiPAMORTIZACIJA.setScale(2);
    ossiPAMORTIZACIJA.setDisplayMask("###,###,##0.00");
    ossiPAMORTIZACIJA.setDefault("0");
    ossiPAMORTIZACIJA.setTableName("OS_SI");
    ossiPAMORTIZACIJA.setServerColumnName("PAMORTIZACIJA");
    ossiPAMORTIZACIJA.setSqlType(2);
    ossiPAMORTIZACIJA.setDefault("0");
    ossiDOKUMENT.setCaption("Dokument");
    ossiDOKUMENT.setColumnName("DOKUMENT");
    ossiDOKUMENT.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiDOKUMENT.setPrecision(50);
    ossiDOKUMENT.setTableName("OS_SI");
    ossiDOKUMENT.setServerColumnName("DOKUMENT");
    ossiDOKUMENT.setSqlType(1);
    ossiDOKUMENT.setWidth(30);
    ossiSALDO.setCaption("Saldo");
    ossiSALDO.setColumnName("SALDO");
    ossiSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiSALDO.setPrecision(17);
    ossiSALDO.setScale(2);
    ossiSALDO.setDisplayMask("###,###,##0.00");
    ossiSALDO.setDefault("0");
    ossiSALDO.setTableName("OS_SI");
    ossiSALDO.setServerColumnName("SALDO");
    ossiSALDO.setSqlType(2);
    ossiSALDO.setDefault("0");
    ossiSTATUS.setCaption("Status");
    ossiSTATUS.setColumnName("STATUS");
    ossiSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiSTATUS.setPrecision(1);
    ossiSTATUS.setRowId(true);
    ossiSTATUS.setTableName("OS_SI");
    ossiSTATUS.setServerColumnName("STATUS");
    ossiSTATUS.setSqlType(1);
    ossiDATNABAVE.setCaption("Datum nabave");
    ossiDATNABAVE.setColumnName("DATNABAVE");
    ossiDATNABAVE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    ossiDATNABAVE.setPrecision(8);
    ossiDATNABAVE.setDisplayMask("dd-MM-yyyy");
//    ossiDATNABAVE.setEditMask("dd-MM-yyyy");
    ossiDATNABAVE.setTableName("OS_SI");
    ossiDATNABAVE.setServerColumnName("DATNABAVE");
    ossiDATNABAVE.setSqlType(93);
    ossiDATNABAVE.setWidth(10);
    ossiDATAKTIVIRANJA.setCaption("Datum aktiviranja");
    ossiDATAKTIVIRANJA.setColumnName("DATAKTIVIRANJA");
    ossiDATAKTIVIRANJA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    ossiDATAKTIVIRANJA.setPrecision(8);
    ossiDATAKTIVIRANJA.setDisplayMask("dd-MM-yyyy");
//    ossiDATAKTIVIRANJA.setEditMask("dd-MM-yyyy");
    ossiDATAKTIVIRANJA.setTableName("OS_SI");
    ossiDATAKTIVIRANJA.setServerColumnName("DATAKTIVIRANJA");
    ossiDATAKTIVIRANJA.setSqlType(93);
    ossiDATAKTIVIRANJA.setWidth(10);
    ossiOSNOVICA.setCaption("Osnovica");
    ossiOSNOVICA.setColumnName("OSNOVICA");
    ossiOSNOVICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiOSNOVICA.setPrecision(17);
    ossiOSNOVICA.setScale(2);
    ossiOSNOVICA.setDisplayMask("###,###,##0.00");
    ossiOSNOVICA.setDefault("0");
    ossiOSNOVICA.setTableName("OS_SI");
    ossiOSNOVICA.setServerColumnName("OSNOVICA");
    ossiOSNOVICA.setSqlType(2);
    ossiOSNOVICA.setDefault("0");
    ossiISPRAVAK.setCaption("Ispravak");
    ossiISPRAVAK.setColumnName("ISPRAVAK");
    ossiISPRAVAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossiISPRAVAK.setPrecision(17);
    ossiISPRAVAK.setScale(2);
    ossiISPRAVAK.setDisplayMask("###,###,##0.00");
    ossiISPRAVAK.setDefault("0");
    ossiISPRAVAK.setTableName("OS_SI");
    ossiISPRAVAK.setServerColumnName("ISPRAVAK");
    ossiISPRAVAK.setSqlType(2);
    ossiISPRAVAK.setDefault("0");
    ossiOLDCORG.setCaption("Stara OJ");
    ossiOLDCORG.setColumnName("OLDCORG");
    ossiOLDCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiOLDCORG.setPrecision(12);
    ossiOLDCORG.setTableName("OS_SI");
    ossiOLDCORG.setServerColumnName("OLDCORG");
    ossiOLDCORG.setSqlType(1);
    ossiOLDCORG.setWidth(6);
    ossiCRADNIK.setCaption("Radnik");
    ossiCRADNIK.setColumnName("CRADNIK");
    ossiCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiCRADNIK.setPrecision(6);
    ossiCRADNIK.setTableName("OS_SI");
    ossiCRADNIK.setServerColumnName("CRADNIK");
    ossiCRADNIK.setSqlType(1);
    ossiSTARISTATUS.setCaption("Stari status");
    ossiSTARISTATUS.setColumnName("STARISTATUS");
    ossiSTARISTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiSTARISTATUS.setPrecision(1);
    ossiSTARISTATUS.setTableName("OS_SI");
    ossiSTARISTATUS.setServerColumnName("STARISTATUS");
    ossiSTARISTATUS.setSqlType(1);
    ossiSTARISTATUS.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ossiOJINV.setCaption("Ojin");
    ossiOJINV.setColumnName("OJINV");
    ossiOJINV.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossiOJINV.setPrecision(22);
    ossiOJINV.setTableName("OS_SI");
    ossiOJINV.setServerColumnName("OJINV");
    ossiOJINV.setSqlType(1);
    ossiOJINV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ossi.setResolver(dm.getQresolver());
    ossi.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_SI", null, true, Load.ALL));
 setColumns(new Column[] {ossiLOKK, ossiAKTIV, ossiCORG, ossiCORG2, ossiINVBROJ, ossiNAZSREDSTVA, ossiCOBJEKT, ossiCLOKACIJE, ossiCARTIKLA, ossiCPAR,
        ossiBROJKONTA, ossiCGRUPE, ossiCSKUPINE, ossiDATPROMJENE, ossiDATLIKVIDACIJE, ossiGODPROIZ, ossiPORIJEKLO, ossiRADNIK, ossiNABVRIJED, ossiOSNPOCETAK,
        ossiOSNDUGUJE, ossiOSNPOTRAZUJE, ossiREVOSN, ossiISPPOCETAK, ossiISPDUGUJE, ossiISPPOTRAZUJE, ossiREVISP, ossiAMORTIZACIJA, ossiREVAMOR,
        ossiPAMORTIZACIJA, ossiDOKUMENT, ossiSALDO, ossiSTATUS, ossiDATNABAVE, ossiDATAKTIVIRANJA, ossiOSNOVICA, ossiISPRAVAK, ossiOLDCORG, ossiCRADNIK,
        ossiSTARISTATUS, ossiOJINV});
  }

  public void setall() {

    ddl.create("OS_SI")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12)
       .addChar("corg2", 12, true)
       .addChar("invbroj", 10, true)
       .addChar("nazsredstva", 50)
       .addChar("cobjekt", 6)
       .addChar("clokacije", 12)
       .addChar("cartikla", 6)
       .addInteger("cpar", 6)
       .addChar("brojkonta", 8)
       .addChar("cgrupe", 6)
       .addChar("cskupine", 6)
       .addDate("datpromjene")
       .addDate("datlikvidacije")
       .addChar("godproiz", 4)
       .addChar("porijeklo", 2)
       .addChar("radnik", 6)
       .addFloat("nabvrijed", 17, 2)
       .addFloat("osnpocetak", 17, 2)
       .addFloat("osnduguje", 17, 2)
       .addFloat("osnpotrazuje", 17, 2)
       .addFloat("revosn", 17, 2)
       .addFloat("isppocetak", 17, 2)
       .addFloat("ispduguje", 17, 2)
       .addFloat("isppotrazuje", 17, 2)
       .addFloat("revisp", 17, 2)
       .addFloat("amortizacija", 17, 2)
       .addFloat("revamor", 17, 2)
       .addFloat("pamortizacija", 17, 2)
       .addChar("dokument", 50)
       .addFloat("saldo", 17, 2)
       .addChar("status", 1, true)
       .addDate("datnabave")
       .addDate("dataktiviranja")
       .addFloat("osnovica", 17, 2)
       .addFloat("ispravak", 17, 2)
       .addChar("oldcorg", 12)
       .addChar("cradnik", 6)
       .addChar("staristatus", 1)
       .addChar("ojinv", 22)
       .addPrimaryKey("corg2,invbroj,status");


    Naziv = "OS_SI";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"invbroj"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
