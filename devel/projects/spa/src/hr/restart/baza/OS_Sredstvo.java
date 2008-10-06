/****license*****************************************************************
**   file: OS_Sredstvo.java
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



public class OS_Sredstvo extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Sredstvo OS_Sredstvoclass;

  QueryDataSet ossr = new QueryDataSet();

  Column ossrLOKK = new Column();
  Column ossrAKTIV = new Column();
  Column ossrCORG = new Column();
  Column ossrCORG2 = new Column();
  Column ossrINVBROJ = new Column();
  Column ossrNAZSREDSTVA = new Column();
  Column ossrCOBJEKT = new Column();
  Column ossrCLOKACIJE = new Column();
  Column ossrCARTIKLA = new Column();
  Column ossrCPAR = new Column();
  Column ossrBROJKONTA = new Column();
  Column ossrCGRUPE = new Column();
  Column ossrCSKUPINE = new Column();
  Column ossrDATPROMJENE = new Column();
  Column ossrDATLIKVIDACIJE = new Column();
  Column ossrGODPROIZ = new Column();
  Column ossrPORIJEKLO = new Column();
  Column ossrCRADNIK = new Column();
  Column ossrNABVRIJED = new Column();
  Column ossrOSNPOCETAK = new Column();
  Column ossrOSNDUGUJE = new Column();
  Column ossrOSNPOTRAZUJE = new Column();
  Column ossrREVOSN = new Column();
  Column ossrISPPOCETAK = new Column();
  Column ossrISPDUGUJE = new Column();
  Column ossrISPPOTRAZUJE = new Column();
  Column ossrREVISP = new Column();
  Column ossrAMORTIZACIJA = new Column();
  Column ossrREVAMOR = new Column();
  Column ossrPAMORTIZACIJA = new Column();
  Column ossrDOKUMENT = new Column();
  Column ossrSALDO = new Column();
  Column ossrSTATUS = new Column();
  Column ossrDATNABAVE = new Column();
  Column ossrDATAKTIVIRANJA = new Column();
  Column ossrOSNOVICA = new Column();
  Column ossrISPRAVAK = new Column();
  Column ossrOLDCORG = new Column();
  Column ossrSTARISTATUS = new Column();
  Column ossrOJINV = new Column();

  public static OS_Sredstvo getDataModule() {
    if (OS_Sredstvoclass == null) {
      OS_Sredstvoclass = new OS_Sredstvo();
    }
    return OS_Sredstvoclass;
  }

  public QueryDataSet getQueryDataSet() {
    return ossr;
  }

  public OS_Sredstvo() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    ossrLOKK.setCaption("Status zauzetosti");
    ossrLOKK.setColumnName("LOKK");
    ossrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrLOKK.setPrecision(1);
    ossrLOKK.setTableName("OS_SREDSTVO");
    ossrLOKK.setServerColumnName("LOKK");
    ossrLOKK.setSqlType(1);
    ossrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ossrLOKK.setDefault("N");
    ossrAKTIV.setCaption("Aktivan - neaktivan");
    ossrAKTIV.setColumnName("AKTIV");
    ossrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrAKTIV.setPrecision(1);
    ossrAKTIV.setTableName("OS_SREDSTVO");
    ossrAKTIV.setServerColumnName("AKTIV");
    ossrAKTIV.setSqlType(1);
    ossrAKTIV.setDefault("D");
    ossrCORG.setCaption("Knigovodstvo");
    ossrCORG.setColumnName("CORG");
    ossrCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrCORG.setPrecision(12);
    ossrCORG.setTableName("OS_SREDSTVO");
    ossrCORG.setServerColumnName("CORG");
    ossrCORG.setSqlType(1);
    ossrCORG2.setCaption("OJ");
    ossrCORG2.setColumnName("CORG2");
    ossrCORG2.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrCORG2.setPrecision(12);
    ossrCORG2.setRowId(true);
    ossrCORG2.setTableName("OS_SREDSTVO");
    ossrCORG2.setServerColumnName("CORG2");
    ossrCORG2.setSqlType(1);
    ossrCORG2.setWidth(6);
    ossrINVBROJ.setCaption("IBroj");
    ossrINVBROJ.setColumnName("INVBROJ");
    ossrINVBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrINVBROJ.setPrecision(10);
    ossrINVBROJ.setRowId(true);
    ossrINVBROJ.setTableName("OS_SREDSTVO");
    ossrINVBROJ.setServerColumnName("INVBROJ");
    ossrINVBROJ.setSqlType(1);
    ossrNAZSREDSTVA.setCaption("Naziv");
    ossrNAZSREDSTVA.setColumnName("NAZSREDSTVA");
    ossrNAZSREDSTVA.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrNAZSREDSTVA.setPrecision(50);
    ossrNAZSREDSTVA.setTableName("OS_SREDSTVO");
    ossrNAZSREDSTVA.setServerColumnName("NAZSREDSTVA");
    ossrNAZSREDSTVA.setSqlType(1);
    ossrNAZSREDSTVA.setWidth(30);
    ossrCOBJEKT.setCaption("Objekt");
    ossrCOBJEKT.setColumnName("COBJEKT");
    ossrCOBJEKT.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrCOBJEKT.setPrecision(6);
    ossrCOBJEKT.setTableName("OS_SREDSTVO");
    ossrCOBJEKT.setServerColumnName("COBJEKT");
    ossrCOBJEKT.setSqlType(1);
    ossrCLOKACIJE.setCaption("Lokacija");
    ossrCLOKACIJE.setColumnName("CLOKACIJE");
    ossrCLOKACIJE.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrCLOKACIJE.setPrecision(12);
    ossrCLOKACIJE.setTableName("OS_SREDSTVO");
    ossrCLOKACIJE.setServerColumnName("CLOKACIJE");
    ossrCLOKACIJE.setSqlType(1);
    ossrCARTIKLA.setCaption("Artikl");
    ossrCARTIKLA.setColumnName("CARTIKLA");
    ossrCARTIKLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrCARTIKLA.setPrecision(6);
    ossrCARTIKLA.setTableName("OS_SREDSTVO");
    ossrCARTIKLA.setServerColumnName("CARTIKLA");
    ossrCARTIKLA.setSqlType(1);
    ossrCPAR.setCaption("Partner");
    ossrCPAR.setColumnName("CPAR");
    ossrCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    ossrCPAR.setPrecision(6);
    ossrCPAR.setTableName("OS_SREDSTVO");
    ossrCPAR.setServerColumnName("CPAR");
    ossrCPAR.setSqlType(4);
    ossrCPAR.setWidth(6);
    ossrBROJKONTA.setCaption("Konto");
    ossrBROJKONTA.setColumnName("BROJKONTA");
    ossrBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrBROJKONTA.setPrecision(8);
    ossrBROJKONTA.setTableName("OS_SREDSTVO");
    ossrBROJKONTA.setServerColumnName("BROJKONTA");
    ossrBROJKONTA.setSqlType(1);
    ossrCGRUPE.setCaption("Amor. grupa");
    ossrCGRUPE.setColumnName("CGRUPE");
    ossrCGRUPE.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrCGRUPE.setPrecision(6);
    ossrCGRUPE.setTableName("OS_SREDSTVO");
    ossrCGRUPE.setServerColumnName("CGRUPE");
    ossrCGRUPE.setSqlType(1);
    ossrCSKUPINE.setCaption("Reva. skupina");
    ossrCSKUPINE.setColumnName("CSKUPINE");
    ossrCSKUPINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrCSKUPINE.setPrecision(6);
    ossrCSKUPINE.setTableName("OS_SREDSTVO");
    ossrCSKUPINE.setServerColumnName("CSKUPINE");
    ossrCSKUPINE.setSqlType(1);
    ossrDATPROMJENE.setCaption("Datum promjene");
    ossrDATPROMJENE.setColumnName("DATPROMJENE");
    ossrDATPROMJENE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    ossrDATPROMJENE.setPrecision(8);
    ossrDATPROMJENE.setDisplayMask("dd-MM-yyyy");
//    ossrDATPROMJENE.setEditMask("dd-MM-yyyy");
    ossrDATPROMJENE.setTableName("OS_SREDSTVO");
    ossrDATPROMJENE.setServerColumnName("DATPROMJENE");
    ossrDATPROMJENE.setSqlType(93);
    ossrDATPROMJENE.setWidth(10);
    ossrDATLIKVIDACIJE.setCaption("Datum likvidacije");
    ossrDATLIKVIDACIJE.setColumnName("DATLIKVIDACIJE");
    ossrDATLIKVIDACIJE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    ossrDATLIKVIDACIJE.setPrecision(8);
    ossrDATLIKVIDACIJE.setDisplayMask("dd-MM-yyyy");
//    ossrDATLIKVIDACIJE.setEditMask("dd-MM-yyyy");
    ossrDATLIKVIDACIJE.setTableName("OS_SREDSTVO");
    ossrDATLIKVIDACIJE.setServerColumnName("DATLIKVIDACIJE");
    ossrDATLIKVIDACIJE.setSqlType(93);
    ossrDATLIKVIDACIJE.setWidth(10);
    ossrGODPROIZ.setCaption("Godina");
    ossrGODPROIZ.setColumnName("GODPROIZ");
    ossrGODPROIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrGODPROIZ.setPrecision(4);
    ossrGODPROIZ.setTableName("OS_SREDSTVO");
    ossrGODPROIZ.setServerColumnName("GODPROIZ");
    ossrGODPROIZ.setSqlType(1);
    ossrPORIJEKLO.setCaption("Porijeklo");
    ossrPORIJEKLO.setColumnName("PORIJEKLO");
    ossrPORIJEKLO.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrPORIJEKLO.setPrecision(2);
    ossrPORIJEKLO.setTableName("OS_SREDSTVO");
    ossrPORIJEKLO.setServerColumnName("PORIJEKLO");
    ossrPORIJEKLO.setSqlType(1);
    ossrCRADNIK.setCaption("Radnik");
    ossrCRADNIK.setColumnName("CRADNIK");
    ossrCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrCRADNIK.setPrecision(6);
    ossrCRADNIK.setTableName("OS_SREDSTVO");
    ossrCRADNIK.setServerColumnName("CRADNIK");
    ossrCRADNIK.setSqlType(1);
    ossrNABVRIJED.setCaption("Nabavna vrijednost");
    ossrNABVRIJED.setColumnName("NABVRIJED");
    ossrNABVRIJED.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrNABVRIJED.setPrecision(17);
    ossrNABVRIJED.setScale(2);
    ossrNABVRIJED.setDisplayMask("###,###,##0.00");
    ossrNABVRIJED.setDefault("0");
    ossrNABVRIJED.setTableName("OS_SREDSTVO");
    ossrNABVRIJED.setServerColumnName("NABVRIJED");
    ossrNABVRIJED.setSqlType(2);
    ossrNABVRIJED.setDefault("0");
    ossrOSNPOCETAK.setCaption("Osnovica Po\u010Detak");
    ossrOSNPOCETAK.setColumnName("OSNPOCETAK");
    ossrOSNPOCETAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrOSNPOCETAK.setPrecision(17);
    ossrOSNPOCETAK.setScale(2);
    ossrOSNPOCETAK.setDisplayMask("###,###,##0.00");
    ossrOSNPOCETAK.setDefault("0");
    ossrOSNPOCETAK.setTableName("OS_SREDSTVO");
    ossrOSNPOCETAK.setServerColumnName("OSNPOCETAK");
    ossrOSNPOCETAK.setSqlType(2);
    ossrOSNPOCETAK.setDefault("0");
    ossrOSNDUGUJE.setCaption("OsnovicaD");
    ossrOSNDUGUJE.setColumnName("OSNDUGUJE");
    ossrOSNDUGUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrOSNDUGUJE.setPrecision(17);
    ossrOSNDUGUJE.setScale(2);
    ossrOSNDUGUJE.setDisplayMask("###,###,##0.00");
    ossrOSNDUGUJE.setDefault("0");
    ossrOSNDUGUJE.setTableName("OS_SREDSTVO");
    ossrOSNDUGUJE.setServerColumnName("OSNDUGUJE");
    ossrOSNDUGUJE.setSqlType(2);
    ossrOSNDUGUJE.setDefault("0");
    ossrOSNPOTRAZUJE.setCaption("OsnovicaP");
    ossrOSNPOTRAZUJE.setColumnName("OSNPOTRAZUJE");
    ossrOSNPOTRAZUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrOSNPOTRAZUJE.setPrecision(17);
    ossrOSNPOTRAZUJE.setScale(2);
    ossrOSNPOTRAZUJE.setDisplayMask("###,###,##0.00");
    ossrOSNPOTRAZUJE.setDefault("0");
    ossrOSNPOTRAZUJE.setTableName("OS_SREDSTVO");
    ossrOSNPOTRAZUJE.setServerColumnName("OSNPOTRAZUJE");
    ossrOSNPOTRAZUJE.setSqlType(2);
    ossrOSNPOTRAZUJE.setDefault("0");
    ossrREVOSN.setCaption("Revalorizacija osnovice");
    ossrREVOSN.setColumnName("REVOSN");
    ossrREVOSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrREVOSN.setPrecision(17);
    ossrREVOSN.setScale(2);
    ossrREVOSN.setDisplayMask("###,###,##0.00");
    ossrREVOSN.setDefault("0");
    ossrREVOSN.setTableName("OS_SREDSTVO");
    ossrREVOSN.setServerColumnName("REVOSN");
    ossrREVOSN.setSqlType(2);
    ossrREVOSN.setDefault("0");
    ossrISPPOCETAK.setCaption("Ispravak po\u010Detak");
    ossrISPPOCETAK.setColumnName("ISPPOCETAK");
    ossrISPPOCETAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrISPPOCETAK.setPrecision(17);
    ossrISPPOCETAK.setScale(2);
    ossrISPPOCETAK.setDisplayMask("###,###,##0.00");
    ossrISPPOCETAK.setDefault("0");
    ossrISPPOCETAK.setTableName("OS_SREDSTVO");
    ossrISPPOCETAK.setServerColumnName("ISPPOCETAK");
    ossrISPPOCETAK.setSqlType(2);
    ossrISPPOCETAK.setDefault("0");
    ossrISPDUGUJE.setCaption("IspravakD");
    ossrISPDUGUJE.setColumnName("ISPDUGUJE");
    ossrISPDUGUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrISPDUGUJE.setPrecision(17);
    ossrISPDUGUJE.setScale(2);
    ossrISPDUGUJE.setDisplayMask("###,###,##0.00");
    ossrISPDUGUJE.setDefault("0");
    ossrISPDUGUJE.setTableName("OS_SREDSTVO");
    ossrISPDUGUJE.setServerColumnName("ISPDUGUJE");
    ossrISPDUGUJE.setSqlType(2);
    ossrISPDUGUJE.setDefault("0");
    ossrISPPOTRAZUJE.setCaption("IspravakP");
    ossrISPPOTRAZUJE.setColumnName("ISPPOTRAZUJE");
    ossrISPPOTRAZUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrISPPOTRAZUJE.setPrecision(17);
    ossrISPPOTRAZUJE.setScale(2);
    ossrISPPOTRAZUJE.setDisplayMask("###,###,##0.00");
    ossrISPPOTRAZUJE.setDefault("0");
    ossrISPPOTRAZUJE.setTableName("OS_SREDSTVO");
    ossrISPPOTRAZUJE.setServerColumnName("ISPPOTRAZUJE");
    ossrISPPOTRAZUJE.setSqlType(2);
    ossrISPPOTRAZUJE.setDefault("0");
    ossrREVISP.setCaption("Revalorizacija ispravka");
    ossrREVISP.setColumnName("REVISP");
    ossrREVISP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrREVISP.setPrecision(17);
    ossrREVISP.setScale(2);
    ossrREVISP.setDisplayMask("###,###,##0.00");
    ossrREVISP.setDefault("0");
    ossrREVISP.setTableName("OS_SREDSTVO");
    ossrREVISP.setServerColumnName("REVISP");
    ossrREVISP.setSqlType(2);
    ossrREVISP.setDefault("0");
    ossrAMORTIZACIJA.setCaption("Amortizacija");
    ossrAMORTIZACIJA.setColumnName("AMORTIZACIJA");
    ossrAMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrAMORTIZACIJA.setPrecision(17);
    ossrAMORTIZACIJA.setScale(2);
    ossrAMORTIZACIJA.setDisplayMask("###,###,##0.00");
    ossrAMORTIZACIJA.setDefault("0");
    ossrAMORTIZACIJA.setTableName("OS_SREDSTVO");
    ossrAMORTIZACIJA.setServerColumnName("AMORTIZACIJA");
    ossrAMORTIZACIJA.setSqlType(2);
    ossrAMORTIZACIJA.setDefault("0");
    ossrREVAMOR.setCaption("Amortizacija revalorizacije");
    ossrREVAMOR.setColumnName("REVAMOR");
    ossrREVAMOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrREVAMOR.setPrecision(17);
    ossrREVAMOR.setScale(2);
    ossrREVAMOR.setDisplayMask("###,###,##0.00");
    ossrREVAMOR.setDefault("0");
    ossrREVAMOR.setTableName("OS_SREDSTVO");
    ossrREVAMOR.setServerColumnName("REVAMOR");
    ossrREVAMOR.setSqlType(2);
    ossrREVAMOR.setDefault("0");
    ossrPAMORTIZACIJA.setCaption("Povišena amortizacija");
    ossrPAMORTIZACIJA.setColumnName("PAMORTIZACIJA");
    ossrPAMORTIZACIJA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrPAMORTIZACIJA.setPrecision(17);
    ossrPAMORTIZACIJA.setScale(2);
    ossrPAMORTIZACIJA.setDisplayMask("###,###,##0.00");
    ossrPAMORTIZACIJA.setDefault("0");
    ossrPAMORTIZACIJA.setTableName("OS_SREDSTVO");
    ossrPAMORTIZACIJA.setServerColumnName("PAMORTIZACIJA");
    ossrPAMORTIZACIJA.setSqlType(2);
    ossrPAMORTIZACIJA.setDefault("0");
    ossrDOKUMENT.setCaption("Dokument");
    ossrDOKUMENT.setColumnName("DOKUMENT");
    ossrDOKUMENT.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrDOKUMENT.setPrecision(50);
    ossrDOKUMENT.setTableName("OS_SREDSTVO");
    ossrDOKUMENT.setServerColumnName("DOKUMENT");
    ossrDOKUMENT.setSqlType(1);
    ossrDOKUMENT.setWidth(30);
    ossrSALDO.setCaption("Saldo");
    ossrSALDO.setColumnName("SALDO");
    ossrSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrSALDO.setPrecision(17);
    ossrSALDO.setScale(2);
    ossrSALDO.setDisplayMask("###,###,##0.00");
    ossrSALDO.setDefault("0");
    ossrSALDO.setTableName("OS_SREDSTVO");
    ossrSALDO.setServerColumnName("SALDO");
    ossrSALDO.setSqlType(2);
    ossrSALDO.setDefault("0");
    ossrSTATUS.setCaption("Status");
    ossrSTATUS.setColumnName("STATUS");
    ossrSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrSTATUS.setPrecision(1);
    ossrSTATUS.setRowId(true);
    ossrSTATUS.setTableName("OS_SREDSTVO");
    ossrSTATUS.setServerColumnName("STATUS");
    ossrSTATUS.setSqlType(1);
    ossrDATNABAVE.setCaption("Nabavljeno");
    ossrDATNABAVE.setColumnName("DATNABAVE");
    ossrDATNABAVE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    ossrDATNABAVE.setPrecision(8);
    ossrDATNABAVE.setDisplayMask("dd-MM-yyyy");
//    ossrDATNABAVE.setEditMask("dd-MM-yyyy");
    ossrDATNABAVE.setTableName("OS_SREDSTVO");
    ossrDATNABAVE.setServerColumnName("DATNABAVE");
    ossrDATNABAVE.setSqlType(93);
    ossrDATNABAVE.setWidth(10);
    ossrDATAKTIVIRANJA.setCaption("Aktivirano");
    ossrDATAKTIVIRANJA.setColumnName("DATAKTIVIRANJA");
    ossrDATAKTIVIRANJA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    ossrDATAKTIVIRANJA.setPrecision(8);
    ossrDATAKTIVIRANJA.setDisplayMask("dd-MM-yyyy");
//    ossrDATAKTIVIRANJA.setEditMask("dd-MM-yyyy");
    ossrDATAKTIVIRANJA.setTableName("OS_SREDSTVO");
    ossrDATAKTIVIRANJA.setServerColumnName("DATAKTIVIRANJA");
    ossrDATAKTIVIRANJA.setSqlType(93);
    ossrDATAKTIVIRANJA.setWidth(10);
    ossrOSNOVICA.setCaption("Osnovica");
    ossrOSNOVICA.setColumnName("OSNOVICA");
    ossrOSNOVICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrOSNOVICA.setPrecision(17);
    ossrOSNOVICA.setScale(2);
    ossrOSNOVICA.setDisplayMask("###,###,##0.00");
    ossrOSNOVICA.setDefault("0");
    ossrOSNOVICA.setTableName("OS_SREDSTVO");
    ossrOSNOVICA.setServerColumnName("OSNOVICA");
    ossrOSNOVICA.setSqlType(2);
    ossrOSNOVICA.setDefault("0");
    ossrISPRAVAK.setCaption("Ispravak");
    ossrISPRAVAK.setColumnName("ISPRAVAK");
    ossrISPRAVAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ossrISPRAVAK.setPrecision(17);
    ossrISPRAVAK.setScale(2);
    ossrISPRAVAK.setDisplayMask("###,###,##0.00");
    ossrISPRAVAK.setDefault("0");
    ossrISPRAVAK.setTableName("OS_SREDSTVO");
    ossrISPRAVAK.setServerColumnName("ISPRAVAK");
    ossrISPRAVAK.setSqlType(2);
    ossrISPRAVAK.setDefault("0");
    ossrOLDCORG.setCaption("Stara OJ");
    ossrOLDCORG.setColumnName("OLDCORG");
    ossrOLDCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrOLDCORG.setPrecision(12);
    ossrOLDCORG.setTableName("OS_SREDSTVO");
    ossrOLDCORG.setServerColumnName("OLDCORG");
    ossrOLDCORG.setSqlType(1);
    ossrOLDCORG.setWidth(6);
    ossrSTARISTATUS.setCaption("Stari status");
    ossrSTARISTATUS.setColumnName("STARISTATUS");
    ossrSTARISTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrSTARISTATUS.setPrecision(1);
    ossrSTARISTATUS.setTableName("OS_SREDSTVO");
    ossrSTARISTATUS.setServerColumnName("STARISTATUS");
    ossrSTARISTATUS.setSqlType(1);
    ossrSTARISTATUS.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ossrOJINV.setCaption("Ojin");
    ossrOJINV.setColumnName("OJINV");
    ossrOJINV.setDataType(com.borland.dx.dataset.Variant.STRING);
    ossrOJINV.setPrecision(22);
    ossrOJINV.setTableName("OS_SREDSTVO");
    ossrOJINV.setServerColumnName("OJINV");
    ossrOJINV.setSqlType(1);
    ossrOJINV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ossr.setResolver(dm.getQresolver());
    ossr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Sredstvo", null, true, Load.ALL));
 setColumns(new Column[] {ossrLOKK, ossrAKTIV, ossrCORG, ossrCORG2, ossrINVBROJ, ossrNAZSREDSTVA, ossrCOBJEKT, ossrCLOKACIJE, ossrCARTIKLA, ossrCPAR,
        ossrBROJKONTA, ossrCGRUPE, ossrCSKUPINE, ossrDATPROMJENE, ossrDATLIKVIDACIJE, ossrGODPROIZ, ossrPORIJEKLO, ossrCRADNIK, ossrNABVRIJED, ossrOSNPOCETAK,
        ossrOSNDUGUJE, ossrOSNPOTRAZUJE, ossrREVOSN, ossrISPPOCETAK, ossrISPDUGUJE, ossrISPPOTRAZUJE, ossrREVISP, ossrAMORTIZACIJA, ossrREVAMOR,
        ossrPAMORTIZACIJA, ossrDOKUMENT, ossrSALDO, ossrSTATUS, ossrDATNABAVE, ossrDATAKTIVIRANJA, ossrOSNOVICA, ossrISPRAVAK, ossrOLDCORG,
        ossrSTARISTATUS, ossrOJINV});
  }

  public void setall() {

    ddl.create("OS_Sredstvo")
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
       .addChar("cradnik", 6)
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
       .addChar("staristatus", 1)
       .addChar("ojinv", 22)
       .addPrimaryKey("corg2,invbroj,status");


    Naziv = "OS_Sredstvo";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"invbroj"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
