/****license*****************************************************************
**   file: Doku.java
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


public class Doku extends KreirDrop implements DataModule {

  private static Doku dokuclass;
  dM dm  = dM.getDataModule();
  QueryDataSet doku = new raDataSet();
  QueryDataSet dokuPST = new raDataSet();
  QueryDataSet dokuPRI = new raDataSet();
  QueryDataSet dokuPOR = new raDataSet();
  QueryDataSet dokuPTE = new raDataSet();
  QueryDataSet dokuPRK = new raDataSet();
  QueryDataSet dokuKAL = new raDataSet();
  QueryDataSet dokuPRE = new raDataSet();
  QueryDataSet dokuINV = new raDataSet();

  Column dokuLOKK = new Column();
  Column dokuAKTIV = new Column();
  Column dokuCSKL = new Column();
  Column dokuVRDOK = new Column();
  Column dokuCUSER = new Column();
  Column dokuGOD = new Column();
  Column dokuBRDOK = new Column();
  Column dokuUI = new Column();
  Column dokuSYSDAT = new Column();
  Column dokuDATDOK = new Column();
  Column dokuCPAR = new Column();
  Column dokuCORG = new Column();
  Column dokuBRRAC = new Column();
  Column dokuDATRAC = new Column();
  Column dokuDVO = new Column();
  Column dokuDDOSP = new Column();
  Column dokuDATDOSP = new Column();
  Column dokuBRDOKUL = new Column();
  Column dokuDATDOKUL = new Column();
  Column dokuBRNARUL = new Column();
  Column dokuDATNARUL = new Column();
  Column dokuOZNVAL = new Column();
  Column dokuTECAJ = new Column();
  Column dokuBRNAL = new Column();
  Column dokuDATKNJ = new Column();
  Column dokuSTATUS = new Column();
  Column dokuSTATKNJ = new Column();
  Column dokuSTATPLA = new Column();
  Column dokuSTATURA = new Column();
  Column dokuUINAB = new Column();
  Column dokuUIZT = new Column();
  Column dokuUPZT = new Column();
  Column dokuUIPRPOR = new Column();
  Column dokuCSHZT = new Column();
  Column dokuZIRO = new Column();
  Column dokuPNBZ2 = new Column();
  Column dokuOPIS = new Column();
  Column dokuCRADNAL = new Column();
  Column dokuPARAM = new Column();
  Column dokuSTAT_KPR = new Column();
  Column dokuCSKLAD = new Column();
  Column dokuUIKAL = new Column();  
  Column dokuUIRAC = new Column();  
  Column dokuPLATITI = new Column(); 
  Column dokuDEVIZN = new Column();
  Column dokuDATUPL = new Column();

  public static Doku getDataModule() {
    if (dokuclass == null) {
      dokuclass = new Doku();
    }
    return dokuclass;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return doku;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPST() {
    return dokuPST;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPRI() {
    return dokuPRI;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPOR() {
    return dokuPOR;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPTE() {
    return dokuPTE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPRK() {
    return dokuPRK;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuKAL() {
    return dokuKAL;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuPRE() {
    return dokuPRE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getDokuINV() {
    return dokuINV;
  }

  public Doku(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {

    dokuLOKK.setColumnName("LOKK");
    dokuLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuLOKK.setDefault("N");
    dokuLOKK.setPrecision(1);
    dokuLOKK.setTableName("DOKU");
    dokuLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokuLOKK.setServerColumnName("LOKK");
    dokuLOKK.setSqlType(1);

    dokuAKTIV.setColumnName("AKTIV");
    dokuAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuAKTIV.setDefault("D");
    dokuAKTIV.setPrecision(1);
    dokuAKTIV.setTableName("DOKU");
    dokuAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokuAKTIV.setServerColumnName("AKTIV");
    dokuAKTIV.setSqlType(1);

    dokuCSKL.setCaption("Skladište");
    dokuCSKL.setColumnName("CSKL");
    dokuCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuCSKL.setPrecision(12);
    dokuCSKL.setRowId(true);
    dokuCSKL.setTableName("DOKU");
    dokuCSKL.setServerColumnName("CSKL");
    dokuCSKL.setSqlType(1);

    dokuGOD.setCaption("Godina");
    dokuGOD.setColumnName("GOD");
    dokuGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuGOD.setPrecision(4);
    dokuGOD.setRowId(true);
    dokuGOD.setTableName("DOKU");
    dokuGOD.setServerColumnName("GOD");
    dokuGOD.setSqlType(1);

    dokuBRDOK.setCaption("Broj");
    dokuBRDOK.setColumnName("BRDOK");
    dokuBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    dokuBRDOK.setRowId(true);
    dokuBRDOK.setTableName("DOKU");
    dokuBRDOK.setWidth(8);
    dokuBRDOK.setServerColumnName("BRDOK");
    dokuBRDOK.setSqlType(4);

    dokuUIPRPOR.setCaption("Iznos pretporeza");
    dokuUIPRPOR.setColumnName("UIPRPOR");
    dokuUIPRPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokuUIPRPOR.setDisplayMask("###,###,##0.00");
    dokuUIPRPOR.setDefault("0");
    dokuUIPRPOR.setPrecision(15);
    dokuUIPRPOR.setScale(2);
    dokuUIPRPOR.setTableName("DOKU");
    dokuUIPRPOR.setServerColumnName("UIPRPOR");
    dokuUIPRPOR.setSqlType(2);
    dokuUPZT.setCaption("Posto zavisnih troškova");
    dokuUPZT.setColumnName("UPZT");
    dokuUPZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokuUPZT.setDisplayMask("#,##0.00");
    dokuUPZT.setDefault("0");
    dokuUPZT.setPrecision(17);
    dokuUPZT.setScale(7);
    dokuUPZT.setTableName("DOKU");
    dokuUPZT.setServerColumnName("UPZT");
    dokuUPZT.setSqlType(2);
    dokuUIZT.setCaption("Iznos zavisnih troškova");
    dokuUIZT.setColumnName("UIZT");
    dokuUIZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokuUIZT.setDisplayMask("###,###,##0.00");
    dokuUIZT.setDefault("0");
    dokuUIZT.setPrecision(15);
    dokuUIZT.setScale(2);
    dokuUIZT.setTableName("DOKU");
    dokuUIZT.setServerColumnName("UIZT");
    dokuUIZT.setSqlType(2);
    dokuUINAB.setCaption("Nabavni iznos");
    dokuUINAB.setColumnName("UINAB");
    dokuUINAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokuUINAB.setDisplayMask("###,###,##0.00");
    dokuUINAB.setDefault("0");
    dokuUINAB.setPrecision(15);
    dokuUINAB.setScale(2);
    dokuUINAB.setTableName("DOKU");
    dokuUINAB.setServerColumnName("UINAB");
    dokuUINAB.setSqlType(2);
    dokuSTATURA.setCaption("Stat. trans.");
    dokuSTATURA.setColumnName("STATURA");
    dokuSTATURA.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuSTATURA.setDefault("N");
    dokuSTATURA.setPrecision(1);
    dokuSTATURA.setTableName("DOKU");
    dokuSTATURA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokuSTATURA.setServerColumnName("STATURA");
    dokuSTATURA.setSqlType(1);
    dokuSTATPLA.setCaption("Stat. pla\u0107.");
    dokuSTATPLA.setColumnName("STATPLA");
    dokuSTATPLA.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuSTATPLA.setDefault("N");
    dokuSTATPLA.setPrecision(1);
    dokuSTATPLA.setTableName("DOKU");
    dokuSTATPLA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokuSTATPLA.setServerColumnName("STATPLA");
    dokuSTATPLA.setSqlType(1);
    dokuSTATKNJ.setCaption("Stat. knj.");
    dokuSTATKNJ.setColumnName("STATKNJ");
    dokuSTATKNJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuSTATKNJ.setDefault("N");
    dokuSTATKNJ.setPrecision(1);
    dokuSTATKNJ.setTableName("DOKU");
    dokuSTATKNJ.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokuSTATKNJ.setServerColumnName("STATKNJ");
    dokuSTATKNJ.setSqlType(1);
    dokuDATKNJ.setCaption("Datum knjiženja");
    dokuDATKNJ.setColumnName("DATKNJ");
    dokuDATKNJ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokuDATKNJ.setDisplayMask("dd-MM-yyyy");
//    dokuDATKNJ.setEditMask("dd-MM-yyyy");
    dokuDATKNJ.setTableName("DOKU");
    dokuDATKNJ.setWidth(10);
    dokuDATKNJ.setServerColumnName("DATKNJ");
    dokuDATKNJ.setSqlType(93);
    dokuBRNAL.setCaption("Nalog");
    dokuBRNAL.setColumnName("BRNAL");
    dokuBRNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuBRNAL.setPrecision(8);
    dokuBRNAL.setTableName("DOKU");
    dokuBRNAL.setServerColumnName("BRNAL");
    dokuBRNAL.setSqlType(1);
    dokuTECAJ.setCaption("Te\u010Daj");
    dokuTECAJ.setColumnName("TECAJ");
    dokuTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokuTECAJ.setDisplayMask("###,###,##0.000000");
    dokuTECAJ.setDefault("0");
    dokuTECAJ.setPrecision(15);
    dokuTECAJ.setScale(6);
    dokuTECAJ.setTableName("DOKU");
    dokuTECAJ.setServerColumnName("TECAJ");
    dokuTECAJ.setSqlType(2);
    dokuDATDOKUL.setCaption("Datum ulaznog dokumenta");
    dokuDATDOKUL.setColumnName("DATDOKUL");
    dokuDATDOKUL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokuDATDOKUL.setDisplayMask("dd-MM-yyyy");
//    dokuDATDOKUL.setEditMask("dd-MM-yyyy");
    dokuDATDOKUL.setTableName("DOKU");
    dokuDATDOKUL.setWidth(10);
    dokuDATDOKUL.setServerColumnName("DATDOKUL");
    dokuDATDOKUL.setSqlType(93);
    dokuBRDOKUL.setCaption("Broj ulaznog dokumenta");
    dokuBRDOKUL.setColumnName("BRDOKUL");
    dokuBRDOKUL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuBRDOKUL.setPrecision(20);
    dokuBRDOKUL.setTableName("DOKU");
    dokuBRDOKUL.setServerColumnName("BRDOKUL");
    dokuBRDOKUL.setSqlType(1);
    dokuDATDOSP.setCaption("Datum dospije\u0107a");
    dokuDATDOSP.setColumnName("DATDOSP");
    dokuDATDOSP.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokuDATDOSP.setDisplayMask("dd-MM-yyyy");
//    dokuDATDOSP.setEditMask("dd-MM-yyyy");
    dokuDATDOSP.setTableName("DOKU");
    dokuDATDOSP.setWidth(10);
    dokuDATDOSP.setServerColumnName("DATDOSP");
    dokuDATDOSP.setSqlType(93);
    dokuDDOSP.setCaption("Dani dospije\u0107a");
    dokuDDOSP.setColumnName("DDOSP");
    dokuDDOSP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    dokuDDOSP.setDefault("0");
    dokuDDOSP.setTableName("DOKU");
    dokuDDOSP.setServerColumnName("DDOSP");
    dokuDDOSP.setSqlType(5);
    dokuDVO.setCaption("DVO");
    dokuDVO.setColumnName("DVO");
    dokuDVO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokuDVO.setDisplayMask("dd-MM-yyyy");
//    dokuDVO.setEditMask("dd-MM-yyyy");
    dokuDVO.setTableName("DOKU");
    dokuDVO.setWidth(10);
    dokuDVO.setServerColumnName("DVO");
    dokuDVO.setSqlType(93);
    dokuBRRAC.setCaption("Broj ra\u010Duna");
    dokuBRRAC.setColumnName("BRRAC");
    dokuBRRAC.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuBRRAC.setPrecision(20);
    dokuBRRAC.setTableName("DOKU");
    dokuBRRAC.setServerColumnName("BRRAC");
    dokuBRRAC.setSqlType(1);
    dokuCORG.setCaption("Organizacijska jedinica");
    dokuCORG.setColumnName("CORG");
    dokuCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuCORG.setPrecision(12);
    dokuCORG.setTableName("DOKU");
    dokuCORG.setServerColumnName("CORG");
    dokuCORG.setSqlType(1);
    dokuCPAR.setCaption("Partner");
    dokuCPAR.setColumnName("CPAR");
    dokuCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    dokuCPAR.setTableName("DOKU");
    dokuCPAR.setServerColumnName("CPAR");
    dokuCPAR.setSqlType(4);
    dokuDATDOK.setCaption("Datum");
    dokuDATDOK.setColumnName("DATDOK");
    dokuDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokuDATDOK.setDisplayMask("dd-MM-yyyy");
//    dokuDATDOK.setEditMask("dd-MM-yyyy");
    dokuDATDOK.setTableName("DOKU");
    dokuDATDOK.setServerColumnName("DATDOK");
    dokuDATDOK.setWidth(10);
    dokuDATDOK.setSqlType(93);
    dokuUI.setCaption("Ul.");
    dokuUI.setColumnName("UI");
    dokuUI.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuUI.setDefault("U");
    dokuUI.setPrecision(1);
    dokuUI.setTableName("DOKU");
    dokuUI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokuUI.setServerColumnName("UI");
    dokuUI.setSqlType(1);

    dokuCUSER.setCaption("Operater");
    dokuCUSER.setColumnName("CUSER");
    dokuCUSER.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuCUSER.setPrecision(15);
    dokuCUSER.setTableName("DOKU");
    dokuCUSER.setServerColumnName("CUSER");
    dokuCUSER.setSqlType(1);

    dokuVRDOK.setCaption("Vrsta");
    dokuVRDOK.setColumnName("VRDOK");
    dokuVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuVRDOK.setPrecision(3);
    dokuVRDOK.setRowId(true);
    dokuVRDOK.setTableName("DOKU");
    dokuVRDOK.setWidth(4);
    dokuVRDOK.setServerColumnName("VRDOK");
    dokuVRDOK.setSqlType(1);
    dokuCSHZT.setCaption("Shema zavisnih troškova");
    dokuCSHZT.setColumnName("CSHZT");
    dokuCSHZT.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuCSHZT.setPrecision(3);
    dokuCSHZT.setTableName("DOKU");
    dokuCSHZT.setVisible(com.borland.jb.util.TriStateProperty.DEFAULT);
    dokuCSHZT.setSqlType(1);
    dokuCSHZT.setServerColumnName("CSHZT");

    dokuBRNARUL.setCaption("Broj narudžbe");
    dokuBRNARUL.setColumnName("BRNARUL");
    dokuBRNARUL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuBRNARUL.setPrecision(20);
    dokuBRNARUL.setTableName("DOKU");
    dokuBRNARUL.setSqlType(1);
    dokuBRNARUL.setServerColumnName("BRNARUL");
    dokuDATNARUL.setCaption("Datum narudžbe");
    dokuDATNARUL.setColumnName("DATNARUL");
    dokuDATNARUL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokuDATNARUL.setDisplayMask("dd-MM-yyyy");
//    dokuDATNARUL.setEditMask("dd-MM-yyyy");
    dokuDATNARUL.setTableName("DOKU");
    dokuDATNARUL.setWidth(10);
    dokuDATNARUL.setSqlType(93);
    dokuDATNARUL.setServerColumnName("DATNARUL");
    dokuSTATUS.setColumnName("STATUS");
    dokuSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuSTATUS.setDefault("N");
    dokuSTATUS.setPrecision(1);
    dokuSTATUS.setTableName("DOKU");
    dokuSTATUS.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokuSTATUS.setSqlType(1);
    dokuSTATUS.setServerColumnName("STATUS");

    dokuDATRAC.setCaption("Datum ra\u010Duna");
    dokuDATRAC.setColumnName("DATRAC");
    dokuDATRAC.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokuDATRAC.setDisplayMask("dd-MM-yyyy");
//    dokuDATRAC.setEditMask("dd-MM-yyyy");
    dokuDATRAC.setTableName("DOKU");
    dokuDATRAC.setWidth(10);
    dokuDATRAC.setSqlType(93);
    dokuDATRAC.setServerColumnName("DATRAC");
    dokuOZNVAL.setCaption("Oznaka valute");
    dokuOZNVAL.setColumnName("OZNVAL");
    dokuOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuOZNVAL.setPrecision(3);
    dokuOZNVAL.setTableName("DOKU");
    dokuOZNVAL.setSqlType(1);
    dokuOZNVAL.setServerColumnName("OZNVAL");

    dokuSYSDAT.setColumnName("SYSDAT");
    dokuSYSDAT.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokuSYSDAT.setDisplayMask("dd-MM-yyyy");
    dokuSYSDAT.setEditMask("dd-MM-yyyy");
    dokuSYSDAT.setTableName("DOKU");
    dokuSYSDAT.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    dokuSYSDAT.setSqlType(93);
    dokuSYSDAT.setServerColumnName("SYSDAT");

    dokuZIRO.setCaption("Žiro ra\u010Dun");
    dokuZIRO.setColumnName("ZIRO");
    dokuZIRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuZIRO.setPrecision(40);
    dokuZIRO.setTableName("DOKU");
    dokuZIRO.setServerColumnName("ZIRO");
    dokuZIRO.setSqlType(1);

    dokuPNBZ2.setCaption("Poziv na broj (zaduž.) 2");
    dokuPNBZ2.setColumnName("PNBZ2");
    dokuPNBZ2.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuPNBZ2.setPrecision(30);
    dokuPNBZ2.setTableName("DOKU");
    dokuPNBZ2.setServerColumnName("PNBZ2");
    dokuPNBZ2.setSqlType(1);

    dokuOPIS.setCaption("Opis");
    dokuOPIS.setColumnName("OPIS");
    dokuOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuOPIS.setPrecision(200);
    dokuOPIS.setTableName("DOKU");
    dokuOPIS.setSqlType(1);
    dokuOPIS.setServerColumnName("OPIS");

    dokuCRADNAL.setCaption("Broj radnog naloga");
    dokuCRADNAL.setColumnName("CRADNAL");
    dokuCRADNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuCRADNAL.setPrecision(20);
    dokuCRADNAL.setTableName("DOKU");
    dokuCRADNAL.setSqlType(1);
    dokuCRADNAL.setServerColumnName("CRADNAL");

    dokuPARAM.setCaption("Parametri");
    dokuPARAM.setColumnName("PARAM");
    dokuPARAM.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuPARAM.setPrecision(8);
    dokuPARAM.setTableName("DOKU");
    dokuPARAM.setServerColumnName("PARAM");
    dokuPARAM.setSqlType(1);

    dokuSTAT_KPR.setCaption("Stat KPR");
    dokuSTAT_KPR.setColumnName("STAT_KPR");
    dokuSTAT_KPR.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuSTAT_KPR.setPrecision(1);
    dokuSTAT_KPR.setDefault("N");
    dokuSTAT_KPR.setTableName("DOKU");
    dokuSTAT_KPR.setServerColumnName("STAT_KPR");
    dokuSTAT_KPR.setSqlType(1);

    dokuCSKLAD.setCaption("Stat KPR");
    dokuCSKLAD.setColumnName("CSKLAD");
    dokuCSKLAD.setDataType(com.borland.dx.dataset.Variant.STRING);
    dokuCSKLAD.setPrecision(12);
    dokuCSKLAD.setDefault("N");
    dokuCSKLAD.setTableName("DOKU");
    dokuCSKLAD.setServerColumnName("CSKLAD");
    dokuCSKLAD.setSqlType(1);
    
    dokuUIKAL.setCaption("Iznos kalk.");
    dokuUIKAL.setColumnName("UIKAL");
    dokuUIKAL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokuUIKAL.setDisplayMask("###,###,##0.00");
    dokuUIKAL.setDefault("0");
    dokuUIKAL.setPrecision(15);
    dokuUIKAL.setScale(2);
    dokuUIKAL.setTableName("DOKU");
    dokuUIKAL.setSqlType(2);
    dokuUIKAL.setServerColumnName("UIKAL");
    dokuUIKAL.setWidth(8);

    dokuUIRAC.setCaption("Iznos raèuna");
    dokuUIRAC.setColumnName("UIRAC");
    dokuUIRAC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokuUIRAC.setDisplayMask("###,###,##0.00");
    dokuUIRAC.setDefault("0");
    dokuUIRAC.setPrecision(15);
    dokuUIRAC.setScale(2);
    dokuUIRAC.setTableName("DOKU");
    dokuUIRAC.setSqlType(2);
    dokuUIRAC.setServerColumnName("UIRAC");
    dokuUIRAC.setWidth(8);

    dokuPLATITI.setCaption("Uplata");
    dokuPLATITI.setColumnName("PLATITI");
    dokuPLATITI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokuPLATITI.setDisplayMask("###,###,##0.00");
    dokuPLATITI.setDefault("0");
    dokuPLATITI.setPrecision(15);
    dokuPLATITI.setScale(2);
    dokuPLATITI.setTableName("DOKU");
    dokuPLATITI.setSqlType(2);
    dokuPLATITI.setServerColumnName("PLATITI");
    dokuPLATITI.setWidth(8);
    
    dokuDEVIZN.setCaption("Devizni iznos");
    dokuDEVIZN.setColumnName("DEVIZN");
    dokuDEVIZN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    dokuDEVIZN.setDisplayMask("###,###,##0.00");
    dokuDEVIZN.setDefault("0");
    dokuDEVIZN.setPrecision(15);
    dokuDEVIZN.setScale(2);
    dokuDEVIZN.setTableName("DOKU");
    dokuDEVIZN.setSqlType(2);
    dokuDEVIZN.setServerColumnName("DEVIZN");
    dokuDEVIZN.setWidth(8);
    
    dokuDATUPL.setCaption("Datum dospije\u0107a");
    dokuDATUPL.setColumnName("DATUPL");
    dokuDATUPL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    dokuDATUPL.setDisplayMask("dd-MM-yyyy");
    dokuDATUPL.setTableName("DOKU");
    dokuDATUPL.setWidth(10);
    dokuDATUPL.setServerColumnName("DATUPL");
    dokuDATUPL.setSqlType(93);


    doku.setResolver(dm.getQresolver());
    //doku.setSort(new com.borland.dx.dataset.SortDescriptor("", new String[] {"CSKL", "VRDOK", "BRDOK"}, new boolean[] {false, false, false, }, null, 0));
    doku.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * FROM DOKU", null, true, Load.ALL));
 setColumns(new Column[] {dokuLOKK, dokuAKTIV, dokuCSKL, dokuVRDOK, dokuCUSER, dokuGOD, dokuBRDOK, dokuUI, dokuSYSDAT, dokuDATDOK, dokuCPAR, dokuCORG,
        dokuBRRAC, dokuDATRAC, dokuDVO, dokuDDOSP, dokuDATDOSP, dokuBRDOKUL, dokuDATDOKUL, dokuBRNARUL, dokuDATNARUL, dokuOZNVAL, dokuTECAJ,
        dokuBRNAL, dokuDATKNJ, dokuSTATUS, dokuSTATKNJ, dokuSTATPLA, dokuSTATURA, dokuUINAB, dokuUIZT, dokuUPZT, dokuUIPRPOR, dokuCSHZT, dokuZIRO,
        dokuPNBZ2, dokuOPIS, dokuCRADNAL, dokuPARAM, dokuSTAT_KPR, dokuCSKLAD,dokuUIKAL,dokuUIRAC,dokuPLATITI,dokuDATUPL,dokuDEVIZN});

    initClones();
  }

  private void initClones() {
    createFilteredDataSet(dokuPST, "1=0");
    createFilteredDataSet(dokuPRI, "1=0");
    createFilteredDataSet(dokuPOR, "1=0");
    createFilteredDataSet(dokuPTE, "1=0");
    createFilteredDataSet(dokuPRK, "1=0");
    createFilteredDataSet(dokuKAL, "1=0");
    createFilteredDataSet(dokuPRE, "1=0");
    createFilteredDataSet(dokuPRE, "1=0");
  }


  public void setall(){

    /*SqlDefTabela =  "create table Doku " +
        "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + //Status zauzetosti
        "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
        "cskl char(6) CHARACTER SET WIN1250 not null,"+ //Šifra skladišta
        "vrdok char(3) CHARACTER SET WIN1250 not null," +   //Vrsta dokumenta (OTP,PRI,..)
        "god char(4) CHARACTER SET WIN1250 not null," + // Godina zalihe
        "brdok numeric(6,0) not null , " + // Broj dokumenta
        "ui char(1) CHARACTER SET WIN1250 default 'U', "+   // Ulazni dokument
        "sysdat date ," + // Systemski datum na zahtjev Zagorca
        "datdok date ," + // Datum dokumenta
        "cpar numeric(6,0) , "+  // Šifra partnera
        "corg char(12) CHARACTER SET WIN1250," + // Šifra org. jedinice
        "brrac char(20) CHARACTER SET WIN1250," + // Broj ulaznog racuna
        "datrac date ," + //Datum ulaznog ra\u010Duna
        "dvo date, " + // Datum DVO
        "ddosp numeric(4,0), " + // Dani dospije\u0107a
        "datdosp date," + //Datum dospije\u0107a
        "brdokul char(20) CHARACTER SET WIN1250,"+  //Broj ulaznog dokumenta
        "datdokul date," + // Datum ulaznog dokumenta
        "brnarul char(20) CHARACTER SET WIN1250,"+
        "datnarul date ,"+
        "oznval char(3) CHARACTER SET WIN1250 ,"+// valuta
        "tecaj numeric (12,6)," + // Te\u010Daj
        "brnal char(8) CHARACTER SET WIN1250," + // Broj naloga knjiženja u FINK
        "datknj date," + // Datum knjiženja
        "status char(1),"+
        "statknj char(1) CHARACTER SET WIN1250 default 'N' ," + // Status knjiženja (N/P/D)
        "statpla char(1) CHARACTER SET WIN1250 default 'N' ," + // Status pla\u0107anja (N/D)
        "statura char(1) CHARACTER SET WIN1250 default 'N' ," + // Status prijenosa u URU(N/D)
        "uinab numeric(17,2) , " + // Ukupni iznos nabavni
        "uizt numeric(17,2), " + // Ukupni iznos zavisni troškovi
        "upzt numeric(6,2), " + // Ukupni posto zavisni troškovi
        "uiprpor numeric(17,2),"   + // Ukupni iznos predporeza
        "cshzt char(3),"+
        "Primary Key (cskl,vrdok,god,brdok))" ;
*/

    ddl.create("doku")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskl", 12, true)
       .addChar("vrdok", 3, true)
       .addChar("cuser", 15)
       .addChar("god", 4, true)
       .addInteger("brdok", 6, true)
       .addChar("ui", 1, "U")
       .addDate("sysdat")
       .addDate("datdok")
       .addInteger("cpar", 6)
       .addChar("corg", 12)
       .addChar("brrac", 20)
       .addDate("datrac")
       .addDate("dvo")
       .addShort("ddosp", 4)
       .addDate("datdosp")
       .addChar("brdokul", 20)
       .addDate("datdokul")
       .addChar("brnarul", 20)
       .addDate("datnarul")
       .addChar("oznval", 3)
       .addFloat("tecaj", 12, 6)
       .addChar("brnal", 8)
       .addDate("datknj")
       .addChar("status", 1)
       .addChar("statknj", 1, "N")
       .addChar("statpla", 1, "N")
       .addChar("statura", 1, "N")
       .addFloat("uinab", 17, 2)
       .addFloat("uizt", 17, 2)
       .addFloat("upzt", 17, 7)
       .addFloat("uiprpor", 17, 2)
       .addChar("cshzt", 3)
       .addChar("ziro", 40)
       .addChar("pnbz2", 30)
       .addChar("opis", 200)
       .addChar("cradnal", 20)
       .addChar("param", 8)
       .addChar("stat_kpr", 1, "N")
       .addChar("csklad", 12)
	   .addFloat("uikal", 17, 2)
	   .addFloat("uirac", 17, 2)
	   .addFloat("platiti", 17, 2)	   
       .addDate("datupl")
       .addFloat("devizn", 17, 2)
       .addPrimaryKey("cskl,vrdok,god,brdok");

    Naziv="Doku";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {/*"brdok", "datdok"*/};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);


    /*

    NaziviIdx=new String[]{"ilokkdoku","iaktivdoku","icskldoku","ivrdokdoku","ibrdokdoku","ipkdoku"};




    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Doku (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Doku (aktiv)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[2] +" on Doku (cskl)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[3] +" on Doku (vrdok)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[4] +" on Doku (brdok)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[5] +" on Doku (cskl,vrdok,god,brdok)" };
*/
  }
}