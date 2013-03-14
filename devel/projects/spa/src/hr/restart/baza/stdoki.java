/****license*****************************************************************
**   file: stdoki.java
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
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

public class stdoki extends KreirDrop implements DataModule {

  private static stdoki stdokiclass;
  QueryDataSet stdoki = new QueryDataSet();
  QueryDataSet stGOT = new QueryDataSet();
  QueryDataSet stPON = new QueryDataSet();
  QueryDataSet stPONKup = new QueryDataSet();
  QueryDataSet stPONOJ = new QueryDataSet();
  QueryDataSet stPOV = new QueryDataSet();
  QueryDataSet stNAR = new QueryDataSet();
  QueryDataSet stPRD = new QueryDataSet();
  QueryDataSet stPRDkup = new QueryDataSet();
  QueryDataSet stRAC = new QueryDataSet();
  QueryDataSet stROT = new QueryDataSet();
  QueryDataSet stOTP = new QueryDataSet();
  QueryDataSet stDOS = new QueryDataSet();
  QueryDataSet stIZD = new QueryDataSet();
  QueryDataSet stPOS = new QueryDataSet();
  QueryDataSet stTER = new QueryDataSet();
  QueryDataSet stODB = new QueryDataSet();
  QueryDataSet stPOD = new QueryDataSet();
  QueryDataSet stPODKup = new QueryDataSet();
  QueryDataSet stRNLp = new QueryDataSet();
  QueryDataSet stRNLs = new QueryDataSet();
  QueryDataSet stGRN = new QueryDataSet();
  QueryDataSet stREV = new QueryDataSet();
  QueryDataSet stPRE = new QueryDataSet();
  QueryDataSet stNDO = new QueryDataSet();
  QueryDataSet stINM = new QueryDataSet();
  QueryDataSet stKON = new QueryDataSet();
  QueryDataSet stZAH = new QueryDataSet();

 /* Column stdokiLOKK = new Column();
  Column stdokiAKTIV = new Column();
  Column stdokiCSKL = new Column();
  Column stdokiVRDOK = new Column();
  Column stdokiGOD = new Column();
  Column stdokiBRDOK = new Column();
  Column stdokiRBR = new Column();
  Column stdokiCART = new Column();
  Column stdokiCART1 = new Column();
  Column stdokiBC = new Column();
  Column stdokiNAZART = new Column();
  Column stdokiJM = new Column();
  Column stdokiCRADNAL = new Column();
  Column stdokiKOL = new Column();
  Column stdokiUPRAB = new Column();
  Column stdokiUIRAB = new Column();
  Column stdokiUPZT = new Column();
  Column stdokiUIZT = new Column();
  Column stdokiFC = new Column();
  Column stdokiINETO = new Column();
  Column stdokiFVC = new Column();
  Column stdokiIPRODBP = new Column();
  Column stdokiPOR1 = new Column();
  Column stdokiPOR2 = new Column();
  Column stdokiPOR3 = new Column();
  Column stdokiFMC = new Column();
  Column stdokiIPRODSP = new Column();
  Column stdokiNC = new Column();
  Column stdokiINAB = new Column();
  Column stdokiIMAR = new Column();
  Column stdokiVC = new Column();
  Column stdokiIBP = new Column();
  Column stdokiIPOR = new Column();
  Column stdokiMC = new Column();
  Column stdokiISP = new Column();
  Column stdokiZC = new Column();
  Column stdokiIRAZ = new Column();
  Column stdokiBRPRI = new Column();
  Column stdokiRBRPRI = new Column();
  Column stdokiPPOR1 = new Column();
  Column stdokiPPOR2 = new Column();
  Column stdokiPPOR3 = new Column();
  Column stdokiCARTNOR = new Column();
  Column stdokiUIPOR = new Column();
  Column stdokiUPPOR = new Column();
  Column stdokiSTATUS = new Column();
  Column stdokiRBSRN = new Column();
  Column stdokiITKAL = new Column();
  Column stdokiSITKAL = new Column();
  Column stdokiSBSIZ = new Column();
  Column stdokiRBSID = new Column();
  Column stdokiFMCPRP = new Column();
  Column stdokiCSKLART = new Column();
  Column stdokiREZKOL = new Column();
  Column stdokiVEZA = new Column();
  Column stdokiIDSTAVKA = new Column();
  Column stdokiKOL1 = new Column();
  Column stdokiKOL2 = new Column();*/
  
  public static stdoki getDataModule() {
    if (stdokiclass == null) {
      stdokiclass = new stdoki();
    }
    return stdokiclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return stdoki;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStGot() {
    return stGOT;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPon() {
    return stPON;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPonKup() {
    return stPONKup;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPonOJ() {
    return stPONOJ;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPov() {
    return stPOV;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStNar() {
    return stNAR;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPrd() {
    return stPRD;
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getStPrdKup() {
    return stPRDkup;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRac() {
    return stRAC;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRot() {
    return stROT;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStOtp() {
    return stOTP;
  }
  
  public com.borland.dx.sql.dataset.QueryDataSet getStDos() {
    return stDOS;
  }  

  public com.borland.dx.sql.dataset.QueryDataSet getStIzd() {
    return stIZD;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPos() {
    return stPOS;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPod() {
    return stPOD;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStPodKup() {
    return stPODKup;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getStTer() {
    return stTER;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStOdb() {
    return stODB;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRnlPro() {
    return stRNLp;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRnlSer() {
    return stRNLs;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStGrn() {
    return stGRN;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStRev() {
    return stREV;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStPre() {
    return stPRE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStNdo() {
    return stNDO;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStInm() {
    return stINM;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStKon() {
    return stKON;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getStZah() {
    return stZAH;
  }

  public stdoki(){
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
    /*
    stdokiLOKK.setColumnName("LOKK");
    stdokiLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiLOKK.setDefault("N");
    stdokiLOKK.setPrecision(1);
    stdokiLOKK.setTableName("STDOKI");
    stdokiLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokiLOKK.setSqlType(1);
    stdokiLOKK.setServerColumnName("LOKK");

    stdokiAKTIV.setColumnName("AKTIV");
    stdokiAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiAKTIV.setDefault("D");
    stdokiAKTIV.setPrecision(1);
    stdokiAKTIV.setTableName("STDOKI");
    stdokiAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokiAKTIV.setSqlType(1);
    stdokiAKTIV.setServerColumnName("AKTIV");

    stdokiCSKL.setCaption("Skladište");
    stdokiCSKL.setColumnName("CSKL");
    stdokiCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiCSKL.setPrecision(12);
    stdokiCSKL.setRowId(true);
    stdokiCSKL.setTableName("STDOKI");
    stdokiCSKL.setSqlType(1);
    stdokiCSKL.setServerColumnName("CSKL");

    stdokiVRDOK.setCaption("Vrsta");
    stdokiVRDOK.setColumnName("VRDOK");
    stdokiVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiVRDOK.setPrecision(3);
    stdokiVRDOK.setRowId(true);
    stdokiVRDOK.setTableName("STDOKI");
    stdokiVRDOK.setSqlType(1);
    stdokiVRDOK.setServerColumnName("VRDOK");

    stdokiGOD.setCaption("Godina");
    stdokiGOD.setColumnName("GOD");
    stdokiGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiGOD.setPrecision(4);
    stdokiGOD.setRowId(true);
    stdokiGOD.setTableName("STDOKI");
    stdokiGOD.setServerColumnName("GOD");
    stdokiGOD.setSqlType(1);

    stdokiBRDOK.setCaption("Broj");
    stdokiBRDOK.setColumnName("BRDOK");
    stdokiBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    stdokiBRDOK.setRowId(true);
    stdokiBRDOK.setTableName("STDOKI");
    stdokiBRDOK.setWidth(7);
    stdokiBRDOK.setServerColumnName("BRDOK");
    stdokiBRDOK.setSqlType(4);


    stdokiRBR.setCaption("Rbr");
    stdokiRBR.setColumnName("RBR");
    stdokiRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    stdokiRBR.setRowId(true);
    stdokiRBR.setTableName("STDOKI");
    stdokiRBR.setWidth(4);
    stdokiRBR.setSqlType(5);
    stdokiRBR.setServerColumnName("RBR");

    stdokiCARTNOR.setCaption("Šifra normiranog artikla");
    stdokiCARTNOR.setColumnName("CARTNOR");
    stdokiCARTNOR.setDataType(com.borland.dx.dataset.Variant.INT);
    stdokiCARTNOR.setTableName("STDOKI");
    stdokiCARTNOR.setWidth(7);
    stdokiCARTNOR.setSqlType(4);
    stdokiCARTNOR.setServerColumnName("CARTNOR");

    stdokiCART.setCaption("Šifra");
    stdokiCART.setColumnName("CART");
    stdokiCART.setDataType(com.borland.dx.dataset.Variant.INT);
    stdokiCART.setTableName("STDOKI");
    stdokiCART.setWidth(7);
    stdokiCART.setSqlType(4);
    stdokiCART.setServerColumnName("CART");

    stdokiCART1.setCaption("Oznaka");
    stdokiCART1.setColumnName("CART1");
    stdokiCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiCART1.setPrecision(20);
    stdokiCART1.setTableName("STDOKI");
    stdokiCART1.setSqlType(1);
    stdokiCART1.setServerColumnName("CART1");
    stdokiCART1.setWidth(12);

    stdokiBC.setCaption("Barcode");
    stdokiBC.setColumnName("BC");
    stdokiBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiBC.setPrecision(20);
//    stdokiBC.setDefault(dm.cartDefault);
    stdokiBC.setTableName("STDOKI");
    stdokiBC.setSqlType(1);
    stdokiBC.setServerColumnName("BC");
    stdokiBC.setWidth(21);

    stdokiNAZART.setCaption("Naziv");
    stdokiNAZART.setColumnName("NAZART");
    stdokiNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiNAZART.setPrecision(50);
    stdokiNAZART.setTableName("STDOKI");
    stdokiNAZART.setWidth(25);
    stdokiNAZART.setSqlType(1);
    stdokiNAZART.setServerColumnName("NAZART");

    stdokiJM.setCaption("JM");
    stdokiJM.setColumnName("JM");
    stdokiJM.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiJM.setPrecision(3);
    stdokiJM.setTableName("STDOKI");
    stdokiJM.setWidth(4);
    stdokiJM.setSqlType(1);
    stdokiJM.setServerColumnName("JM");

    stdokiCRADNAL.setCaption("Radni nalog");
    stdokiCRADNAL.setColumnName("CRADNAL");
    stdokiCRADNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiCRADNAL.setPrecision(30);
    stdokiCRADNAL.setTableName("STDOKI");
    stdokiCRADNAL.setSqlType(1);
    stdokiCRADNAL.setServerColumnName("CRADNAL");

    stdokiKOL.setCaption("Koli\u010Dina");
    stdokiKOL.setColumnName("KOL");
    stdokiKOL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiKOL.setDisplayMask("###,###,##0.000");
    stdokiKOL.setDefault("0");
    stdokiKOL.setPrecision(15);
    stdokiKOL.setScale(3);
    stdokiKOL.setTableName("STDOKI");
    stdokiKOL.setWidth(10);
    stdokiKOL.setSqlType(2);
    stdokiKOL.setServerColumnName("KOL");

    stdokiKOL1.setCaption("Pakiranja");
    stdokiKOL1.setColumnName("KOL1");
    stdokiKOL1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiKOL1.setDisplayMask("###,###,##0.000");
    stdokiKOL1.setDefault("0");
    stdokiKOL1.setPrecision(15);
    stdokiKOL1.setScale(3);
    stdokiKOL1.setTableName("STDOKI");
    stdokiKOL1.setWidth(10);
    stdokiKOL1.setSqlType(2);
    stdokiKOL1.setServerColumnName("KOL1");

    stdokiKOL2.setCaption("Naruèeno");
    stdokiKOL2.setColumnName("KOL2");
    stdokiKOL2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiKOL2.setDisplayMask("###,###,##0.000");
    stdokiKOL2.setDefault("0");
    stdokiKOL2.setPrecision(15);
    stdokiKOL2.setScale(3);
    stdokiKOL2.setTableName("STDOKI");
    stdokiKOL2.setWidth(10);
    stdokiKOL2.setSqlType(2);
    stdokiKOL2.setServerColumnName("KOL2");

    stdokiUPRAB.setCaption("Pop (%)");
    stdokiUPRAB.setColumnName("UPRAB");
    stdokiUPRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiUPRAB.setDisplayMask("###,###,##0.00");
    stdokiUPRAB.setDefault("0");
    stdokiUPRAB.setPrecision(10);
    stdokiUPRAB.setScale(2);
    stdokiUPRAB.setTableName("STDOKI");
    stdokiUPRAB.setWidth(8);
    stdokiUPRAB.setSqlType(2);
    stdokiUPRAB.setServerColumnName("UPRAB");

    stdokiUIRAB.setCaption("Iznos popusta");
    stdokiUIRAB.setColumnName("UIRAB");
    stdokiUIRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiUIRAB.setDisplayMask("###,###,##0.00");
    stdokiUIRAB.setDefault("0");
    stdokiUIRAB.setPrecision(15);
    stdokiUIRAB.setScale(2);
    stdokiUIRAB.setTableName("STDOKI");
    stdokiUIRAB.setWidth(14);
    stdokiUIRAB.setSqlType(2);
    stdokiUIRAB.setServerColumnName("UIRAB");

    stdokiUPZT.setCaption("Posto zavisnih troškova");
    stdokiUPZT.setColumnName("UPZT");
    stdokiUPZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiUPZT.setDisplayMask("###,###,##0.00");
    stdokiUPZT.setDefault("0");
    stdokiUPZT.setPrecision(10);
    stdokiUPZT.setScale(2);
    stdokiUPZT.setTableName("STDOKI");
    stdokiUPZT.setWidth(8);
    stdokiUPZT.setSqlType(2);
    stdokiUPZT.setServerColumnName("UPZT");

    stdokiUIZT.setCaption("Iznos zavisnih troškova");
    stdokiUIZT.setColumnName("UIZT");
    stdokiUIZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiUIZT.setDisplayMask("###,###,##0.00");
    stdokiUIZT.setDefault("0");
    stdokiUIZT.setPrecision(15);
    stdokiUIZT.setScale(2);
    stdokiUIZT.setTableName("STDOKI");
    stdokiUIZT.setSqlType(2);
    stdokiUIZT.setServerColumnName("UIZT");

    stdokiFC.setCaption("Cijena");
    stdokiFC.setColumnName("FC");
    stdokiFC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiFC.setDisplayMask("###,###,##0.00");
    stdokiFC.setDefault("0");
    stdokiFC.setPrecision(15);
    stdokiFC.setScale(2);
    stdokiFC.setTableName("STDOKI");
    stdokiFC.setWidth(12);
    stdokiFC.setSqlType(2);
    stdokiFC.setServerColumnName("FC");

    stdokiINETO.setCaption("Neto iznos");
    stdokiINETO.setColumnName("INETO");
    stdokiINETO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiINETO.setDisplayMask("###,###,##0.00");
    stdokiINETO.setDefault("0");
    stdokiINETO.setPrecision(15);
    stdokiINETO.setScale(2);
    stdokiINETO.setTableName("STDOKI");
    stdokiINETO.setWidth(14);
    stdokiINETO.setSqlType(2);
    stdokiINETO.setServerColumnName("INETO");

    stdokiFVC.setCaption("Cijena bez poreza");
    stdokiFVC.setColumnName("FVC");
    stdokiFVC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiFVC.setDisplayMask("###,###,##0.00");
    stdokiFVC.setDefault("0");
    stdokiFVC.setPrecision(15);
    stdokiFVC.setScale(2);
    stdokiFVC.setTableName("STDOKI");
    stdokiFVC.setWidth(12);
    stdokiFVC.setSqlType(2);
    stdokiFVC.setServerColumnName("FVC");

    stdokiIPRODBP.setCaption("Bez poreza");
    stdokiIPRODBP.setColumnName("IPRODBP");
    stdokiIPRODBP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiIPRODBP.setDisplayMask("###,###,##0.00");
    stdokiIPRODBP.setDefault("0");
    stdokiIPRODBP.setPrecision(15);
    stdokiIPRODBP.setScale(2);
    stdokiIPRODBP.setTableName("STDOKI");
    stdokiIPRODBP.setWidth(14);
    stdokiIPRODBP.setSqlType(2);
    stdokiIPRODBP.setServerColumnName("IPRODBP");

    stdokiPOR1.setCaption("Porez 1");
    stdokiPOR1.setColumnName("POR1");
    stdokiPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiPOR1.setDisplayMask("###,###,##0.00");
    stdokiPOR1.setDefault("0");
    stdokiPOR1.setPrecision(15);
    stdokiPOR1.setScale(2);
    stdokiPOR1.setTableName("STDOKI");
    stdokiPOR1.setWidth(14);
    stdokiPOR1.setSqlType(2);
    stdokiPOR1.setServerColumnName("POR1");

    stdokiPOR2.setCaption("Porez 2");
    stdokiPOR2.setColumnName("POR2");
    stdokiPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiPOR2.setDisplayMask("###,###,##0.00");
    stdokiPOR2.setDefault("0");
    stdokiPOR2.setPrecision(15);
    stdokiPOR2.setScale(2);
    stdokiPOR2.setTableName("STDOKI");
    stdokiPOR2.setWidth(14);
    stdokiPOR2.setSqlType(2);
    stdokiPOR2.setServerColumnName("POR2");

    stdokiPOR3.setCaption("Porez 3");
    stdokiPOR3.setColumnName("POR3");
    stdokiPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiPOR3.setDisplayMask("###,###,##0.00");
    stdokiPOR3.setDefault("0");
    stdokiPOR3.setPrecision(15);
    stdokiPOR3.setScale(2);
    stdokiPOR3.setTableName("STDOKI");
    stdokiPOR3.setWidth(14);
    stdokiPOR3.setSqlType(2);
    stdokiPOR3.setServerColumnName("POR3");

    stdokiPPOR1.setCaption("Posto poreza 1");
    stdokiPPOR1.setColumnName("PPOR1");
    stdokiPPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiPPOR1.setDisplayMask("###,###,##0.00");
    stdokiPPOR1.setDefault("0");
    stdokiPPOR1.setPrecision(15);
    stdokiPPOR1.setScale(2);
    stdokiPPOR1.setTableName("STDOKI");
    stdokiPPOR1.setWidth(14);
    stdokiPPOR1.setSqlType(2);
    stdokiPPOR1.setServerColumnName("PPOR1");

    stdokiPPOR2.setCaption("Posto poreza 2");
    stdokiPPOR2.setColumnName("PPOR2");
    stdokiPPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiPPOR2.setDisplayMask("###,###,##0.00");
    stdokiPPOR2.setDefault("0");
    stdokiPPOR2.setPrecision(15);
    stdokiPPOR2.setScale(2);
    stdokiPPOR2.setTableName("STDOKI");
    stdokiPPOR2.setWidth(14);
    stdokiPPOR2.setSqlType(2);
    stdokiPPOR2.setServerColumnName("PPOR2");

    stdokiPPOR3.setCaption("Posto poreza 3");
    stdokiPPOR3.setColumnName("PPOR3");
    stdokiPPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiPPOR3.setDisplayMask("###,###,##0.00");
    stdokiPPOR3.setDefault("0");
    stdokiPPOR3.setPrecision(15);
    stdokiPPOR3.setScale(2);
    stdokiPPOR3.setTableName("STDOKI");
    stdokiPPOR3.setWidth(14);
    stdokiPPOR3.setSqlType(2);
    stdokiPPOR3.setServerColumnName("PPOR3");

    stdokiFMC.setCaption("Cijena s porezom");
    stdokiFMC.setColumnName("FMC");
    stdokiFMC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiFMC.setDisplayMask("###,###,##0.00");
    stdokiFMC.setDefault("0");
    stdokiFMC.setPrecision(15);
    stdokiFMC.setScale(2);
    stdokiFMC.setTableName("STDOKI");
    stdokiFMC.setWidth(12);
    stdokiFMC.setSqlType(2);
    stdokiFMC.setServerColumnName("FMC");

    stdokiFMCPRP.setCaption("Cijena");
    stdokiFMCPRP.setColumnName("FMCPRP");
    stdokiFMCPRP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiFMCPRP.setDisplayMask("###,###,##0.00");
    stdokiFMCPRP.setDefault("0");
    stdokiFMCPRP.setPrecision(15);
    stdokiFMCPRP.setScale(2);
    stdokiFMCPRP.setTableName("STDOKI");
    stdokiFMCPRP.setWidth(12);
    stdokiFMCPRP.setSqlType(2);
    stdokiFMCPRP.setServerColumnName("FMCPRP");

    stdokiIPRODSP.setCaption("S porezom");
    stdokiIPRODSP.setColumnName("IPRODSP");
    stdokiIPRODSP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiIPRODSP.setDisplayMask("###,###,##0.00");
    stdokiIPRODSP.setDefault("0");
    stdokiIPRODSP.setPrecision(15);
    stdokiIPRODSP.setScale(2);
    stdokiIPRODSP.setTableName("STDOKI");
    stdokiIPRODSP.setWidth(14);
    stdokiIPRODSP.setSqlType(2);
    stdokiIPRODSP.setServerColumnName("IPRODSP");

    stdokiNC.setCaption("Nabavna cijena");
    stdokiNC.setColumnName("NC");
    stdokiNC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiNC.setDisplayMask("###,###,##0.00");
    stdokiNC.setDefault("0");
    stdokiNC.setPrecision(15);
    stdokiNC.setScale(2);
    stdokiNC.setTableName("STDOKI");
    stdokiNC.setWidth(12);
    stdokiNC.setSqlType(2);
    stdokiNC.setServerColumnName("NC");

    stdokiINAB.setCaption("Nabavni iznos");
    stdokiINAB.setColumnName("INAB");
    stdokiINAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiINAB.setDisplayMask("###,###,##0.00");
    stdokiINAB.setDefault("0");
    stdokiINAB.setPrecision(15);
    stdokiINAB.setScale(2);
    stdokiINAB.setTableName("STDOKI");
    stdokiINAB.setWidth(14);
    stdokiINAB.setSqlType(2);
    stdokiINAB.setServerColumnName("INAB");

    stdokiIMAR.setCaption("Razlika u cijeni");
    stdokiIMAR.setColumnName("IMAR");
    stdokiIMAR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiIMAR.setDisplayMask("###,###,##0.00");
    stdokiIMAR.setDefault("0");
    stdokiIMAR.setPrecision(15);
    stdokiIMAR.setScale(2);
    stdokiIMAR.setTableName("STDOKI");
    stdokiIMAR.setWidth(14);
    stdokiIMAR.setSqlType(2);
    stdokiIMAR.setServerColumnName("IMAR");

    stdokiVC.setCaption("Prodajna cijena bez poreza");
    stdokiVC.setColumnName("VC");
    stdokiVC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiVC.setDisplayMask("###,###,##0.00");
    stdokiVC.setDefault("0");
    stdokiVC.setPrecision(15);
    stdokiVC.setScale(2);
    stdokiVC.setTableName("STDOKI");
    stdokiVC.setWidth(12);
    stdokiVC.setSqlType(2);
    stdokiVC.setServerColumnName("VC");

    stdokiIBP.setCaption("Iznos bez poreza");
    stdokiIBP.setColumnName("IBP");
    stdokiIBP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiIBP.setDisplayMask("###,###,##0.00");
    stdokiIBP.setDefault("0");
    stdokiIBP.setPrecision(15);
    stdokiIBP.setScale(2);
    stdokiIBP.setTableName("STDOKI");
    stdokiIBP.setWidth(14);
    stdokiIBP.setSqlType(2);
    stdokiIBP.setServerColumnName("IBP");

    stdokiIPOR.setCaption("Porezi");
    stdokiIPOR.setColumnName("IPOR");
    stdokiIPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiIPOR.setDisplayMask("###,###,##0.00");
    stdokiIPOR.setDefault("0");
    stdokiIPOR.setPrecision(15);
    stdokiIPOR.setScale(2);
    stdokiIPOR.setTableName("STDOKI");
    stdokiIPOR.setWidth(14);
    stdokiIPOR.setSqlType(2);
    stdokiIPOR.setServerColumnName("IPOR");

    stdokiMC.setCaption("Prodajna cijena s porezom");
    stdokiMC.setColumnName("MC");
    stdokiMC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiMC.setDisplayMask("###,###,##0.00");
    stdokiMC.setDefault("0");
    stdokiMC.setPrecision(15);
    stdokiMC.setScale(2);
    stdokiMC.setTableName("STDOKI");
    stdokiMC.setWidth(12);
    stdokiMC.setSqlType(2);
    stdokiMC.setServerColumnName("MC");

    stdokiISP.setCaption("Iznos s porezom");
    stdokiISP.setColumnName("ISP");
    stdokiISP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiISP.setDisplayMask("###,###,##0.00");
    stdokiISP.setDefault("0");
    stdokiISP.setPrecision(15);
    stdokiISP.setScale(2);
    stdokiISP.setTableName("STDOKI");
    stdokiISP.setWidth(14);
    stdokiISP.setSqlType(2);
    stdokiISP.setServerColumnName("ISP");

    stdokiZC.setCaption("Cijena zalihe");
    stdokiZC.setColumnName("ZC");
    stdokiZC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiZC.setDisplayMask("###,###,##0.00");
    stdokiZC.setDefault("0");
    stdokiZC.setPrecision(15);
    stdokiZC.setScale(2);
    stdokiZC.setTableName("STDOKI");
    stdokiZC.setWidth(12);
    stdokiZC.setSqlType(2);
    stdokiZC.setServerColumnName("ZC");

    stdokiIRAZ.setCaption("Razduženje");
    stdokiIRAZ.setColumnName("IRAZ");
    stdokiIRAZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiIRAZ.setDisplayMask("###,###,##0.00");
    stdokiIRAZ.setDefault("0");
    stdokiIRAZ.setPrecision(15);
    stdokiIRAZ.setScale(2);
    stdokiIRAZ.setTableName("STDOKI");
    stdokiIRAZ.setSqlType(2);
    stdokiIRAZ.setServerColumnName("IRAZ");

    stdokiBRPRI.setCaption("Broj primke s koje se skida FLH");
    stdokiBRPRI.setColumnName("BRPRI");
    stdokiBRPRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiBRPRI.setPrecision(7);
    stdokiBRPRI.setTableName("STDOKI");
    stdokiBRPRI.setSqlType(1);
    stdokiBRPRI.setServerColumnName("BRPRI");

    stdokiRBRPRI.setCaption("Redni broj stavke primke");
    stdokiRBRPRI.setColumnName("RBRPRI");
    stdokiRBRPRI.setDataType(com.borland.dx.dataset.Variant.SHORT);
    stdokiRBRPRI.setTableName("STDOKI");
    stdokiRBRPRI.setSqlType(5);
    stdokiRBRPRI.setServerColumnName("RBRPRI");

    stdokiUIPOR.setCaption("Ukupni iznos poreza");
    stdokiUIPOR.setColumnName("UIPOR");
    stdokiUIPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiUIPOR.setDisplayMask("###,###,##0.00");
    stdokiUIPOR.setDefault("0");
    stdokiUIPOR.setPrecision(15);
    stdokiUIPOR.setScale(2);
    stdokiUIPOR.setTableName("STDOKI");
    stdokiUIPOR.setWidth(14);
    stdokiUIPOR.setSqlType(2);
    stdokiUIPOR.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokiUIPOR.setServerColumnName("UIPOR");

    stdokiUPPOR.setCaption("Ukupni iznos poreza");
    stdokiUPPOR.setColumnName("UPPOR");
    stdokiUPPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokiUPPOR.setDisplayMask("###,###,##0.00");
    stdokiUPPOR.setDefault("0");
    stdokiUPPOR.setPrecision(15);
    stdokiUPPOR.setScale(2);
    stdokiUPPOR.setTableName("STDOKI");
    stdokiUPPOR.setWidth(14);
    stdokiUPPOR.setSqlType(2);
    stdokiUPPOR.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokiUPPOR.setServerColumnName("UPPOR");

    stdokiSTATUS.setColumnName("STATUS");
    stdokiSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiSTATUS.setDefault("N");
    stdokiSTATUS.setPrecision(1);
    stdokiSTATUS.setTableName("STDOKI");
    stdokiSTATUS.setSqlType(1);
    stdokiSTATUS.setServerColumnName("STATUS");

    stdokiRBSRN.setCaption("ID stavke RNL");
    stdokiRBSRN.setColumnName("RBSRN");
    stdokiRBSRN.setDataType(com.borland.dx.dataset.Variant.INT);
    stdokiRBSRN.setTableName("STDOKI");
    stdokiRBSRN.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokiRBSRN.setServerColumnName("RBSRN");
    stdokiRBSRN.setSqlType(4);

    stdokiITKAL.setCaption("Broj kalkulacije zadnjeg izlaza");
    stdokiITKAL.setColumnName("ITKAL");
    stdokiITKAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiITKAL.setPrecision(52);
    stdokiITKAL.setTableName("STDOKI");
    stdokiITKAL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokiITKAL.setServerColumnName("ITKAL");
    stdokiITKAL.setSqlType(1);

    stdokiSITKAL.setCaption("Br preth kalkulacije izlaza");
    stdokiSITKAL.setColumnName("SITKAL");
    stdokiSITKAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiSITKAL.setPrecision(52);
    stdokiSITKAL.setTableName("STDOKI");
    stdokiSITKAL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokiSITKAL.setServerColumnName("SITKAL");
    stdokiSITKAL.setSqlType(1);

    stdokiSBSIZ.setCaption("Br stavki izlaza po zadnjem izl.");
    stdokiSBSIZ.setColumnName("SBSIZ");
    stdokiSBSIZ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    stdokiSBSIZ.setPrecision(4);
    stdokiSBSIZ.setTableName("STDOKI");
    stdokiSBSIZ.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokiSBSIZ.setServerColumnName("SBSIZ");
    stdokiSBSIZ.setSqlType(5);

    stdokiRBSID.setCaption("ID stavke");
    stdokiRBSID.setColumnName("RBSID");
    stdokiRBSID.setDataType(com.borland.dx.dataset.Variant.INT);
    stdokiRBSID.setTableName("STDOKI");
    stdokiRBSID.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokiRBSID.setServerColumnName("RBSID");
    stdokiRBSID.setSqlType(4);

    stdokiCSKLART.setCaption("Skladište artikla");
    stdokiCSKLART.setColumnName("CSKLART");
    stdokiCSKLART.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiCSKLART.setPrecision(12);
    stdokiCSKLART.setTableName("STDOKI");
    stdokiCSKLART.setSqlType(1);
    stdokiCSKLART.setServerColumnName("CSKLART");

    stdokiREZKOL.setCaption("Rezervacija");
    stdokiREZKOL.setColumnName("REZKOL");
    stdokiREZKOL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiREZKOL.setDefault("N");
    stdokiREZKOL.setPrecision(1);
    stdokiREZKOL.setTableName("STDOKI");
    stdokiREZKOL.setSqlType(1);
    stdokiREZKOL.setServerColumnName("REZKOL");
    
    stdokiVEZA.setCaption("Veza");
    stdokiVEZA.setColumnName("VEZA");
    stdokiVEZA.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiVEZA.setPrecision(50);
    stdokiVEZA.setTableName("STDOKI");
    stdokiVEZA.setSqlType(1);
    stdokiVEZA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);    
    stdokiVEZA.setServerColumnName("VEZA");
    
    stdokiIDSTAVKA.setCaption("ID stavka");
    stdokiIDSTAVKA.setColumnName("id_stavka");
    stdokiIDSTAVKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokiIDSTAVKA.setPrecision(50);
    stdokiIDSTAVKA.setTableName("STDOKI");
    stdokiIDSTAVKA.setSqlType(1);
    stdokiIDSTAVKA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);    
    stdokiIDSTAVKA.setServerColumnName("id_stavka");
    
    

    stdoki.setResolver(dm.getQresolver());
    stdoki.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
           "select * from stdoki", null, true, Load.ALL));
    setColumns(new Column[] {stdokiLOKK, stdokiAKTIV, stdokiCSKL, stdokiVRDOK, stdokiGOD, stdokiBRDOK, stdokiRBR, stdokiCART, stdokiCART1,
        stdokiBC, stdokiNAZART, stdokiJM, stdokiCRADNAL, stdokiKOL, stdokiUPRAB, stdokiUIRAB, stdokiUPZT, stdokiUIZT, stdokiFC, stdokiINETO, stdokiFVC, stdokiIPRODBP,
        stdokiPOR1, stdokiPOR2, stdokiPOR3, stdokiFMC, stdokiIPRODSP, stdokiNC, stdokiINAB, stdokiIMAR, stdokiVC, stdokiIBP, stdokiIPOR, stdokiMC,
        stdokiISP, stdokiZC, stdokiIRAZ, stdokiBRPRI, stdokiRBRPRI, stdokiPPOR1, stdokiPPOR2, stdokiPPOR3, stdokiCARTNOR, stdokiUIPOR, stdokiUPPOR,
        stdokiSTATUS, stdokiRBSRN, stdokiITKAL, stdokiSITKAL, stdokiSBSIZ, stdokiRBSID, stdokiFMCPRP, stdokiCSKLART,stdokiREZKOL,stdokiVEZA,
		stdokiIDSTAVKA, stdokiKOL1, stdokiKOL2});
*/
    initClones();
  }
  
  protected void modifyColumn(Column c) {
    if (c.getColumnName().equals("FC") || c.getColumnName().equals("FVC") ||
        c.getColumnName().equals("FMC") || c.getColumnName().equals("FMCPRP")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "cijenaDec", 
          "2", "Broj decimala za cijenu na izlazu (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
    if (c.getColumnName().equals("NC") || c.getColumnName().equals("ZC")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "skladDec", 
          "2", "Broj decimala za skladišne cijene (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
  }

  private void initClones() {
    createFilteredDataSet(stGOT, "1=0");
    createFilteredDataSet(stPON, "1=0");
    createFilteredDataSet(stPONKup, "1=0");
    createFilteredDataSet(stPONOJ, "1=0");
    createFilteredDataSet(stNAR, "1=0");
    createFilteredDataSet(stPRD, "1=0");
    createFilteredDataSet(stPRDkup, "1=0");
    createFilteredDataSet(stRAC, "1=0");
    createFilteredDataSet(stROT, "1=0");
    createFilteredDataSet(stOTP, "1=0");
    createFilteredDataSet(stIZD, "1=0");
    createFilteredDataSet(stPOS, "1=0");
    createFilteredDataSet(stPOD, "1=0");
    createFilteredDataSet(stPODKup, "1=0");
    createFilteredDataSet(stTER, "1=0");
    createFilteredDataSet(stODB, "1=0");
    createFilteredDataSet(stRNLp, "1=0");
    createFilteredDataSet(stRNLs, "1=0");
    createFilteredDataSet(stGRN, "1=0");
    createFilteredDataSet(stREV, "1=0");
    createFilteredDataSet(stPRE, "1=0");
    createFilteredDataSet(stNDO, "1=0");
    createFilteredDataSet(stPOV, "1=0");
    createFilteredDataSet(stINM, "1=0");
    createFilteredDataSet(stDOS, "1=0");
    createFilteredDataSet(stKON, "1=0");
    createFilteredDataSet(stZAH, "1=0");
  }


 /*public void setall(){

     SqlDefTabela = "create table stdoki " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + //Status zauzetosti
      "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
      "cskl char(6) CHARACTER SET WIN1250 not null,"+ //Šifra skladišta
      "vrdok char(3) CHARACTER SET WIN1250 not null," +   //Vrsta dokumenta (OTP,PRI,..)
      "god char(4) CHARACTER SET WIN1250 not null," + // Godina zalihe
      "brdok numeric(6,0) not null , " + // Broj dokumenta
      "rbr numeric(4,0) not null, " + // Redni broj stavke
      "cart numeric(6,0) , "+   // Šifra artikl broj\u010Danik
      "cart1 char(20) character set win1250 ," +  // Šifra artikl - alpha dodatna
      "bc char(20) character set win1250 ,"+ // Barcode
      "nazart char(50) CHARACTER SET WIN1250 , " + // Naziv artikla
      "jm char(3) CHARACTER SET WIN1250 , " + // Jedinica mjere
      "kol  numeric(17,3) ," + // Koli\u010Dina
      "uprab numeric(6,2)," + //ukupno posto rabata
      "uirab numeric(17,2)," + //ukupan iznos rabata
      "upzt numeric(6,2)," + // Posto ukupnih zavisni troškovi
      "uizt numeric(17,2) ," + // Iznos ukupnih zavisni troškovi
      "fc   numeric(12,2) ," + // Fakturna cijena bez poreza
      "ineto numeric(17,2),"  + // Iznos bez poreza
      "fvc numeric(12,2) ," + // Prodajna cijena bez poreza
      "iprodbp numeric(17,2) ," + // Prodajni iznos bez poreza
      "por1  numeric(17,2) ," + // Porez 1 (npr. PDV)
      "por2  numeric(17,2) ," + // Porez 2 (npr. na potrošnju 3%
      "por3  numeric(17,2) ," + // Porez 3
      "fmc numeric(17,2)," + //Prodajna cijena s porezom
      "iprodsp numeric(17,2)," + // Iznos s porezom
      "nc   numeric(12,4) ," + // Nabavna cijena
      "inab numeric(17,2)," +  // Iznos nabavni
      "imar  numeric(17,2), " + // Iznos marze
      "vc   numeric(12,2) ," + // Prodajna cijena bez poreza
      "ibp numeric(17,2) , "+ // Iznos bez poreza
      "ipor numeric(17,2),"  + // Iznos poreza
      "mc   numeric(12,2) , " + // Prodajna cijena s porezom
      "isp  numeric(17,2),"  + // Iznos s porezom
      "zc   numeric(12,4),"  + // Cijena zalihe
      "iraz numeric(17,2),"  + // Iznos zalihe
      "brpri char(7) CHARACTER SET WIN1250 ," +// Broj primke s koje se skida FLH
      "rbrpri numeric(4,0),"+ //Redni broj stavke primke
      "Primary Key (cskl,vrdok,brdok,rbr))" ; 

    ddl.create("stdoki")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskl", 12, true)
       .addChar("vrdok", 3, true)
       .addChar("god", 4, true)
       .addInteger("brdok", 6, true)
       .addShort("rbr", 4, true)
       .addInteger("cart", 6)
       .addChar("cart1", 20)
       .addChar("bc", 20)
       .addChar("nazart", 50)
       .addChar("jm", 3)
       .addChar("cradnal", 30)
       .addFloat("kol", 17, 3)
       .addFloat("uprab", 6, 2)
       .addFloat("uirab", 17, 2)
       .addFloat("upzt", 6, 2)
       .addFloat("uizt", 17, 2)
       .addFloat("fc", 12, 2)
       .addFloat("ineto", 17, 2)
       .addFloat("fvc", 12, 2)
       .addFloat("iprodbp", 17, 2)
       .addFloat("por1", 17, 2)
       .addFloat("por2", 17, 2)
       .addFloat("por3", 17, 2)
       .addFloat("fmc", 17, 2)
       .addFloat("iprodsp", 17, 2)
       .addFloat("nc", 12, 2)
       .addFloat("inab", 17, 2)
       .addFloat("imar", 17, 2)
       .addFloat("vc", 12, 2)
       .addFloat("ibp", 17, 2)
       .addFloat("ipor", 17, 2)
       .addFloat("mc", 12, 2)
       .addFloat("isp", 17, 2)
       .addFloat("zc", 12, 2)
       .addFloat("iraz", 17, 2)
       .addChar("brpri", 7)
       .addShort("rbrpri", 4)
       .addFloat("ppor1", 8, 2)
       .addFloat("ppor2", 8, 2)
       .addFloat("ppor3", 8, 2)
       .addInteger("cartnor", 6)
       .addFloat("uipor", 17, 2)
       .addFloat("uppor", 8, 2)
       .addChar("status", 1, "N")
       .addInteger("rbsrn", 6)
       .addChar("itkal", 52)
       .addChar("sitkal", 52)
       .addShort("sbsiz", 4)
       .addInteger("rbsid", 6)
       .addFloat("fmcprp", 17, 2)
       .addChar("csklart", 12)
       .addChar("rezkol", 1, "N")
	   .addChar("veza", 50)
	   .addChar("id_stavka", 50)	   
       .addFloat("kol1", 17, 3)
       .addFloat("kol2", 17, 3)
       .addPrimaryKey("cskl,vrdok,god,brdok,rbr");

    Naziv="stdoki";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cskl,vrdok,god,brdok",
        "cart", "cradnal", "veza", "id_stavka"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);


    DefIndex= new String[] {CommonTable.SqlDefIndex+"istdokilokk on stdoki (lokk)",
      CommonTable.SqlDefIndex+"istdokiaktiv on stdoki (aktiv)",
      CommonTable.SqlDefIndex+"istdokicskl on stdoki (cskl)",
      CommonTable.SqlDefIndex+"istdokivrdok on stdoki (vrdok)",
      CommonTable.SqlDefIndex+"istdokibrdok on stdoki (brdok)",
      CommonTable.SqlDefIndex+"istdokirbr on stdoki (rbr)",
      CommonTable.SqlDefIndex+"istdokicart on stdoki (cart)",
      CommonTable.SqlDefIndex+"istdokinazart on stdoki (nazart)",
      CommonTable.SqlDefUniqueIndex+"istdokikey on stdoki (cskl,vrdok,brdok,rbr)"};

    NaziviIdx=new String[]{"istdokilokk",
    "istdokiaktiv","istdokicskl","istdokivrdok","istdokibrdok",
    "istdokirbr","istdokicart","istdokinazart","istdokikey" };

  } */
}