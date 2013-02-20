/****license*****************************************************************
**   file: Stdoku.java
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

public class Stdoku extends KreirDrop implements DataModule {
  private static Stdoku stdokuclass;
  QueryDataSet stdoku = new QueryDataSet();
  QueryDataSet stdokuPST = new QueryDataSet();
  QueryDataSet stdokuPRI = new QueryDataSet();
  QueryDataSet stdokuPOR = new QueryDataSet();
  QueryDataSet stdokuPTE = new QueryDataSet();
  QueryDataSet stdokuPRK = new QueryDataSet();
  QueryDataSet stdokuKAL = new QueryDataSet();
  QueryDataSet stdokuPRE = new QueryDataSet();
  QueryDataSet stdokuINV = new QueryDataSet();

/*  Column stdokuLOKK = new Column();
  Column stdokuAKTIV = new Column();
  Column stdokuCSKL = new Column();
  Column stdokuVRDOK = new Column();
  Column stdokuGOD = new Column();
  Column stdokuBRDOK = new Column();
  Column stdokuRBR = new Column();
  Column stdokuCART = new Column();
  Column stdokuCART1 = new Column();
  Column stdokuBC = new Column();
  Column stdokuNAZART = new Column();
  Column stdokuJM = new Column();
  Column stdokuKOL = new Column();
  Column stdokuDC = new Column();
  Column stdokuDC_VAL = new Column();
  Column stdokuIDOB = new Column();
  Column stdokuIDOB_VAL = new Column();
  Column stdokuPRAB = new Column();
  Column stdokuIRAB = new Column();
  Column stdokuPZT = new Column();
  Column stdokuIZT = new Column();
  Column stdokuNC = new Column();
  Column stdokuPMAR = new Column();
  Column stdokuMAR = new Column();
  Column stdokuVC = new Column();
  Column stdokuPOR1 = new Column();
  Column stdokuPOR2 = new Column();
  Column stdokuPOR3 = new Column();
  Column stdokuMC = new Column();
  Column stdokuINAB = new Column();
  Column stdokuIMAR = new Column();
  Column stdokuIBP = new Column();
  Column stdokuIPOR = new Column();
  Column stdokuISP = new Column();
  Column stdokuZC = new Column();
  Column stdokuIZAD = new Column();
  Column stdokuKOLFLH = new Column();
  Column stdokuSKOL = new Column();
  Column stdokuSVC = new Column();
  Column stdokuSMC = new Column();
//  Column stdokuVCuk = new Column();
  Column stdokuDIOPORMAR = new Column();
  Column stdokuDIOPORPOR = new Column();
  Column stdokuPORAV = new Column();
  Column stdokuSTATUS = new Column();
  Column stdokuSKAL = new Column();
  Column stdokuRBSID = new Column();
  Column stdokuCSKLART = new Column();
  Column stdokuVEZA = new Column();  
  Column stdokuIDSTAVKA = new Column();
  Column stdokuKOL1 = new Column();
  Column stdokuKOL2 = new Column();*/
  
  public static Stdoku getDataModule() {
    if (stdokuclass == null) {
      stdokuclass = new Stdoku();
    }
    return stdokuclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return stdoku;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPST() {
    return stdokuPST;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPRI() {
    return stdokuPRI;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPOR() {
    return stdokuPOR;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPTE() {
    return stdokuPTE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPRK() {
    return stdokuPRK;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuKAL() {
    return stdokuKAL;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuPRE() {
    return stdokuPRE;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getStdokuINV() {
    return stdokuINV;
  }
  
  protected void modifyColumn(Column c) {
    if (c.getColumnName().equals("DC_VAL") || c.getColumnName().equals("IDOB_VAL")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "ulazValDec", 
          "2", "Broj decimala za valutne iznose na ulazu (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
    if (c.getColumnName().equals("DC") || c.getColumnName().equals("NC") || c.getColumnName().equals("ZC")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "skladDec", 
          "2", "Broj decimala za skladišne cijene (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
  }


  public Stdoku(){
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    /*stdokuIDOB_VAL.setColumnName("IDOB_VAL");
    stdokuIDOB_VAL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuIDOB_VAL.setDisplayMask("###,###,##0.00");
    stdokuIDOB_VAL.setDefault("0");
    stdokuIDOB_VAL.setPrecision(15);
    stdokuIDOB_VAL.setScale(2);
    stdokuIDOB_VAL.setTableName("STDOKU");
    stdokuIDOB_VAL.setWidth(14);
    stdokuIDOB_VAL.setSqlType(2);
    stdokuIDOB_VAL.setServerColumnName("IDOB_VAL");
    stdokuDC_VAL.setCaption("Dobavlja\u010Deva cijena u valuti");
    stdokuDC_VAL.setColumnName("DC_VAL");
    stdokuDC_VAL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuDC_VAL.setDisplayMask("###,###,##0.00");
    stdokuDC_VAL.setDefault("0");
    stdokuDC_VAL.setPrecision(15);
    stdokuDC_VAL.setScale(2);
    stdokuDC_VAL.setTableName("STDOKU");
    stdokuDC_VAL.setWidth(14);
    stdokuDC_VAL.setSqlType(2);
    stdokuDC_VAL.setServerColumnName("DC_VAL");

    stdokuLOKK.setColumnName("LOKK");
    stdokuLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuLOKK.setDefault("N");
    stdokuLOKK.setPrecision(1);
    stdokuLOKK.setTableName("STDOKU");
    stdokuLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokuLOKK.setServerColumnName("LOKK");
    stdokuLOKK.setSqlType(1);

    stdokuAKTIV.setColumnName("AKTIV");
    stdokuAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuAKTIV.setDefault("D");
    stdokuAKTIV.setPrecision(1);
    stdokuAKTIV.setTableName("STDOKU");
    stdokuAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokuAKTIV.setServerColumnName("AKTIV");
    stdokuAKTIV.setSqlType(1);

    stdokuCSKL.setCaption("Skladište");
    stdokuCSKL.setColumnName("CSKL");
    stdokuCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuCSKL.setPrecision(12);
    stdokuCSKL.setRowId(true);
    stdokuCSKL.setTableName("STDOKU");
    stdokuCSKL.setServerColumnName("CSKL");
    stdokuCSKL.setSqlType(1);

    stdokuCSKLART.setCaption("Skladište");
    stdokuCSKLART.setColumnName("CSKLART");
    stdokuCSKLART.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuCSKLART.setPrecision(12);
    stdokuCSKLART.setTableName("STDOKU");
    stdokuCSKLART.setServerColumnName("CSKLART");
    stdokuCSKLART.setSqlType(1);

    stdokuVRDOK.setCaption("Vrsta");
    stdokuVRDOK.setColumnName("VRDOK");
    stdokuVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuVRDOK.setPrecision(3);
    stdokuVRDOK.setRowId(true);
    stdokuVRDOK.setTableName("STDOKU");
    stdokuVRDOK.setWidth(4);
    stdokuVRDOK.setServerColumnName("VRDOK");
    stdokuVRDOK.setSqlType(1);

    stdokuGOD.setCaption("Godina");
    stdokuGOD.setColumnName("GOD");
    stdokuGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuGOD.setPrecision(4);
    stdokuGOD.setRowId(true);
    stdokuGOD.setTableName("STDOKU");
    stdokuGOD.setServerColumnName("GOD");
    stdokuGOD.setSqlType(1);

    stdokuBRDOK.setCaption("Broj");
    stdokuBRDOK.setColumnName("BRDOK");
    stdokuBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    stdokuBRDOK.setRowId(true);
    stdokuBRDOK.setTableName("STDOKU");
    stdokuBRDOK.setWidth(10);
    stdokuBRDOK.setServerColumnName("BRDOK");
    stdokuBRDOK.setSqlType(4);

    stdokuRBR.setCaption("Rbr");
    stdokuRBR.setColumnName("RBR");
    stdokuRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    stdokuRBR.setRowId(true);
    stdokuRBR.setTableName("STDOKU");
    stdokuRBR.setWidth(5);
    stdokuRBR.setServerColumnName("RBR");
    stdokuRBR.setSqlType(5);

    stdokuCART.setCaption("Šifra");
    stdokuCART.setColumnName("CART");
    stdokuCART.setDataType(com.borland.dx.dataset.Variant.INT);
    stdokuCART.setTableName("STDOKU");
    stdokuCART.setWidth(7);
    stdokuCART.setServerColumnName("CART");
    stdokuCART.setSqlType(4);

    stdokuCART1.setCaption("Oznaka");
    stdokuCART1.setColumnName("CART1");
    stdokuCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuCART1.setPrecision(20);
    stdokuCART1.setTableName("STDOKU");
    stdokuCART1.setSqlType(1);
    stdokuCART1.setServerColumnName("CART1");

    stdokuBC.setCaption("Barcode");
    stdokuBC.setColumnName("BC");
    stdokuBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuBC.setPrecision(20);
    stdokuBC.setTableName("STDOKU");
    stdokuBC.setSqlType(1);
    stdokuBC.setServerColumnName("BC");

    stdokuNAZART.setCaption("Naziv");
    stdokuNAZART.setColumnName("NAZART");
    stdokuNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuNAZART.setPrecision(50);
    stdokuNAZART.setTableName("STDOKU");
    stdokuNAZART.setWidth(25);
    stdokuNAZART.setSqlType(1);
    stdokuNAZART.setServerColumnName("NAZART");

    stdokuJM.setCaption("Jm");
    stdokuJM.setColumnName("JM");
    stdokuJM.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuJM.setPrecision(3);
    stdokuJM.setTableName("STDOKU");
    stdokuJM.setWidth(5);
    stdokuJM.setSqlType(1);
    stdokuJM.setServerColumnName("JM");

    stdokuKOL.setCaption("Kolièina");
    stdokuKOL.setColumnName("KOL");
    stdokuKOL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuKOL.setDisplayMask("###,###,##0.000");
    stdokuKOL.setDefault("0");
    stdokuKOL.setPrecision(15);
    stdokuKOL.setScale(3);
    stdokuKOL.setTableName("STDOKU");
    stdokuKOL.setWidth(10);
    stdokuKOL.setServerColumnName("KOL");
    stdokuKOL.setSqlType(2);

    stdokuKOL1.setCaption("Pakiranja");
    stdokuKOL1.setColumnName("KOL1");
    stdokuKOL1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuKOL1.setDisplayMask("###,###,##0.000");
    stdokuKOL1.setDefault("0");
    stdokuKOL1.setPrecision(15);
    stdokuKOL1.setScale(3);
    stdokuKOL1.setTableName("STDOKU");
    stdokuKOL1.setWidth(10);
    stdokuKOL1.setServerColumnName("KOL1");
    stdokuKOL1.setSqlType(2);

    stdokuKOL2.setCaption("Koleta");
    stdokuKOL2.setColumnName("KOL2");
    stdokuKOL2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuKOL2.setDisplayMask("###,###,##0.000");
    stdokuKOL2.setDefault("0");
    stdokuKOL2.setPrecision(15);
    stdokuKOL2.setScale(3);
    stdokuKOL2.setTableName("STDOKU");
    stdokuKOL2.setWidth(10);
    stdokuKOL2.setServerColumnName("KOL2");
    stdokuKOL2.setSqlType(2);

    stdokuDC.setCaption("Dobavlja\u010Deva cijena");
    stdokuDC.setColumnName("DC");
    stdokuDC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuDC.setDisplayMask("###,###,##0.00");
    stdokuDC.setDefault("0");
    stdokuDC.setPrecision(15);
    stdokuDC.setScale(2);
    stdokuDC.setTableName("STDOKU");
    stdokuDC.setWidth(12);
    stdokuDC.setServerColumnName("DC");
    stdokuDC.setSqlType(2);

    stdokuIDOB.setCaption("Dob. iznos");
    stdokuIDOB.setColumnName("IDOB");
    stdokuIDOB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuIDOB.setDisplayMask("###,###,##0.00");
    stdokuIDOB.setDefault("0");
    stdokuIDOB.setPrecision(15);
    stdokuIDOB.setScale(2);
    stdokuIDOB.setTableName("STDOKU");
    stdokuIDOB.setWidth(14);
    stdokuIDOB.setServerColumnName("IDOB");
    stdokuIDOB.setSqlType(2);

    stdokuPRAB.setCaption("Posto popusta");
    stdokuPRAB.setColumnName("PRAB");
    stdokuPRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuPRAB.setDisplayMask("##0.00");
    stdokuPRAB.setDefault("0");
    stdokuPRAB.setPrecision(10);
    stdokuPRAB.setScale(2);
    stdokuPRAB.setTableName("STDOKU");
    stdokuPRAB.setWidth(8);
    stdokuPRAB.setServerColumnName("PRAB");
    stdokuPRAB.setSqlType(2);

    stdokuIRAB.setCaption("Popust");
    stdokuIRAB.setColumnName("IRAB");
    stdokuIRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuIRAB.setDisplayMask("###,###,##0.00");
    stdokuIRAB.setDefault("0");
    stdokuIRAB.setPrecision(15);
    stdokuIRAB.setScale(2);
    stdokuIRAB.setTableName("STDOKU");
    stdokuIRAB.setWidth(14);
    stdokuIRAB.setServerColumnName("IRAB");
    stdokuIRAB.setSqlType(2);

    stdokuPZT.setCaption("Posto zavisnih troškova");
    stdokuPZT.setColumnName("PZT");
    stdokuPZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuPZT.setDisplayMask("#,##0.00");
    stdokuPZT.setDefault("0");
    stdokuPZT.setPrecision(15);
    stdokuPZT.setScale(7);
    stdokuPZT.setTableName("STDOKU");
    stdokuPZT.setWidth(8);
    stdokuPZT.setSqlType(2);
    stdokuPZT.setServerColumnName("PZT");

    stdokuIZT.setCaption("Troškovi");
    stdokuIZT.setColumnName("IZT");
    stdokuIZT.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuIZT.setDisplayMask("###,###,##0.00");
    stdokuIZT.setDefault("0");
    stdokuIZT.setPrecision(15);
    stdokuIZT.setScale(2);
    stdokuIZT.setTableName("STDOKU");
    stdokuIZT.setWidth(14);
    stdokuIZT.setSqlType(2);
    stdokuIZT.setServerColumnName("IZT");

    stdokuNC.setCaption("Nabavna cijena");
    stdokuNC.setColumnName("NC");
    stdokuNC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuNC.setDisplayMask("###,###,##0.00");
    stdokuNC.setDefault("0");
    stdokuNC.setPrecision(15);
    stdokuNC.setScale(2);
    stdokuNC.setTableName("STDOKU");
    stdokuNC.setWidth(12);
    stdokuNC.setServerColumnName("NC");
    stdokuNC.setSqlType(2);

    stdokuPMAR.setCaption("Posto RuC");
    stdokuPMAR.setColumnName("PMAR");
    stdokuPMAR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuPMAR.setDisplayMask("#,##0.00");
    stdokuPMAR.setDefault("0");
    stdokuPMAR.setPrecision(10);
    stdokuPMAR.setScale(2);
    stdokuPMAR.setTableName("STDOKU");
    stdokuPMAR.setWidth(8);
    stdokuPMAR.setServerColumnName("PMAR");
    stdokuPMAR.setSqlType(2);

    stdokuMAR.setCaption("Jed. RuC");
    stdokuMAR.setColumnName("MAR");
    stdokuMAR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuMAR.setDisplayMask("###,###,##0.00");
    stdokuMAR.setDefault("0");
    stdokuMAR.setPrecision(15);
    stdokuMAR.setScale(2);
    stdokuMAR.setTableName("STDOKU");
    stdokuMAR.setWidth(14);
    stdokuMAR.setServerColumnName("MAR");
    stdokuMAR.setSqlType(2);

    stdokuVC.setCaption("Cijena bez poreza");
    stdokuVC.setColumnName("VC");
    stdokuVC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuVC.setDisplayMask("###,###,##0.00");
    stdokuVC.setDefault("0");
    stdokuVC.setPrecision(15);
    stdokuVC.setScale(2);
    stdokuVC.setTableName("STDOKU");
    stdokuVC.setWidth(12);
    stdokuVC.setServerColumnName("VC");
    stdokuVC.setSqlType(2);

    stdokuPOR1.setCaption("Porez 1");
    stdokuPOR1.setColumnName("POR1");
    stdokuPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuPOR1.setDisplayMask("###,###,##0.00");
    stdokuPOR1.setDefault("0");
    stdokuPOR1.setPrecision(15);
    stdokuPOR1.setScale(2);
    stdokuPOR1.setTableName("STDOKU");
    stdokuPOR1.setWidth(14);
    stdokuPOR1.setSqlType(2);
    stdokuPOR1.setServerColumnName("POR1");

    stdokuPOR2.setCaption("Porez 2");
    stdokuPOR2.setColumnName("POR2");
    stdokuPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuPOR2.setDisplayMask("###,###,##0.00");
    stdokuPOR2.setDefault("0");
    stdokuPOR2.setPrecision(15);
    stdokuPOR2.setScale(2);
    stdokuPOR2.setTableName("STDOKU");
    stdokuPOR2.setWidth(14);
    stdokuPOR2.setSqlType(2);
    stdokuPOR2.setServerColumnName("POR2");

    stdokuPOR3.setCaption("Porez 3");
    stdokuPOR3.setColumnName("POR3");
    stdokuPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuPOR3.setDisplayMask("###,###,##0.00");
    stdokuPOR3.setDefault("0");
    stdokuPOR3.setPrecision(15);
    stdokuPOR3.setScale(2);
    stdokuPOR3.setTableName("STDOKU");
    stdokuPOR3.setWidth(14);
    stdokuPOR3.setSqlType(2);
    stdokuPOR3.setServerColumnName("POR3");

    stdokuMC.setCaption("Cijena s porezom");
    stdokuMC.setColumnName("MC");
    stdokuMC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuMC.setDisplayMask("###,###,##0.00");
    stdokuMC.setDefault("0");
    stdokuMC.setPrecision(15);
    stdokuMC.setScale(2);
    stdokuMC.setTableName("STDOKU");
    stdokuMC.setWidth(12);
    stdokuMC.setServerColumnName("MC");
    stdokuMC.setSqlType(2);

    stdokuINAB.setCaption("Nabavni iznos");
    stdokuINAB.setColumnName("INAB");
    stdokuINAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuINAB.setDisplayMask("###,###,##0.00");
    stdokuINAB.setDefault("0");
    stdokuINAB.setPrecision(15);
    stdokuINAB.setScale(2);
    stdokuINAB.setTableName("STDOKU");
    stdokuINAB.setWidth(14);
    stdokuINAB.setServerColumnName("INAB");
    stdokuINAB.setSqlType(2);

    stdokuIMAR.setCaption("RuC");
    stdokuIMAR.setColumnName("IMAR");
    stdokuIMAR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuIMAR.setDisplayMask("###,###,##0.00");
    stdokuIMAR.setDefault("0");
    stdokuIMAR.setPrecision(15);
    stdokuIMAR.setScale(2);
    stdokuIMAR.setTableName("STDOKU");
    stdokuIMAR.setWidth(14);
    stdokuIMAR.setServerColumnName("IMAR");
    stdokuIMAR.setSqlType(2);

    stdokuIBP.setCaption("Iznos bez poreza");
    stdokuIBP.setColumnName("IBP");
    stdokuIBP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuIBP.setDisplayMask("###,###,##0.00");
    stdokuIBP.setDefault("0");
    stdokuIBP.setPrecision(15);
    stdokuIBP.setScale(2);
    stdokuIBP.setTableName("STDOKU");
    stdokuIBP.setWidth(14);
    stdokuIBP.setServerColumnName("IBP");
    stdokuIBP.setSqlType(2);

    stdokuIPOR.setCaption("Porez");
    stdokuIPOR.setColumnName("IPOR");
    stdokuIPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuIPOR.setDisplayMask("###,###,##0.00");
    stdokuIPOR.setDefault("0");
    stdokuIPOR.setPrecision(15);
    stdokuIPOR.setScale(2);
    stdokuIPOR.setTableName("STDOKU");
    stdokuIPOR.setWidth(14);
    stdokuIPOR.setSqlType(2);
    stdokuIPOR.setServerColumnName("IPOR");

    stdokuISP.setCaption("Iznos s porezom");
    stdokuISP.setColumnName("ISP");
    stdokuISP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuISP.setDisplayMask("###,###,##0.00");
    stdokuISP.setDefault("0");
    stdokuISP.setPrecision(15);
    stdokuISP.setScale(2);
    stdokuISP.setTableName("STDOKU");
    stdokuISP.setWidth(14);
    stdokuISP.setServerColumnName("ISP");
    stdokuISP.setSqlType(2);

    stdokuZC.setCaption("Cijena");
    stdokuZC.setColumnName("ZC");
    stdokuZC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuZC.setDisplayMask("###,###,##0.00");
    stdokuZC.setDefault("0");
    stdokuZC.setPrecision(15);
    stdokuZC.setScale(2);
    stdokuZC.setTableName("STDOKU");
    stdokuZC.setWidth(12);
    stdokuZC.setServerColumnName("ZC");
    stdokuZC.setSqlType(2);

    stdokuIZAD.setCaption("Zaduženje");
    stdokuIZAD.setColumnName("IZAD");
    stdokuIZAD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuIZAD.setDisplayMask("###,###,##0.00");
    stdokuIZAD.setDefault("0");
    stdokuIZAD.setPrecision(15);
    stdokuIZAD.setScale(2);
    stdokuIZAD.setTableName("STDOKU");
    stdokuIZAD.setWidth(14);
    stdokuIZAD.setServerColumnName("IZAD");
    stdokuIZAD.setSqlType(2);

    stdokuKOLFLH.setCaption("Kolièina FLH");
    stdokuKOLFLH.setColumnName("KOLFLH");
    stdokuKOLFLH.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuKOLFLH.setDisplayMask("###,###,##0.000");
    stdokuKOLFLH.setDefault("0");
    stdokuKOLFLH.setPrecision(15);
    stdokuKOLFLH.setScale(3);
    stdokuKOLFLH.setTableName("STDOKU");
    stdokuKOLFLH.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokuKOLFLH.setWidth(10);
    stdokuKOLFLH.setServerColumnName("KOLFLH");
    stdokuKOLFLH.setSqlType(2);

    stdokuSKOL.setCaption("Stara kolièina");
    stdokuSKOL.setColumnName("SKOL");
    stdokuSKOL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuSKOL.setDisplayMask("###,###,##0.000");
    stdokuSKOL.setDefault("0");
    stdokuSKOL.setPrecision(15);
    stdokuSKOL.setScale(3);
    stdokuSKOL.setTableName("STDOKU");
    stdokuSKOL.setWidth(10);
    stdokuSKOL.setSqlType(2);
    stdokuSKOL.setServerColumnName("SKOL");

    stdokuSVC.setCaption("Stara cijena bez poreza");
    stdokuSVC.setColumnName("SVC");
    stdokuSVC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuSVC.setDisplayMask("###,###,##0.00");
    stdokuSVC.setDefault("0");
    stdokuSVC.setPrecision(15);
    stdokuSVC.setScale(2);
    stdokuSVC.setTableName("STDOKU");
    stdokuSVC.setWidth(12);
    stdokuSVC.setSqlType(2);
    stdokuSVC.setServerColumnName("SVC");

    stdokuSMC.setCaption("Stara cijena s porezom");
    stdokuSMC.setColumnName("SMC");
    stdokuSMC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuSMC.setDisplayMask("###,###,##0.00");
    stdokuSMC.setDefault("0");
    stdokuSMC.setPrecision(15);
    stdokuSMC.setScale(2);
    stdokuSMC.setTableName("STDOKU");
    stdokuSMC.setWidth(12);
    stdokuSMC.setSqlType(2);
    stdokuSMC.setServerColumnName("SMC");

    stdokuDIOPORMAR.setCaption("Dio poravnanja RuC-a");
    stdokuDIOPORMAR.setColumnName("DIOPORMAR");
    stdokuDIOPORMAR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuDIOPORMAR.setDisplayMask("###,###,##0.00");
    stdokuDIOPORMAR.setDefault("0");
    stdokuDIOPORMAR.setPrecision(15);
    stdokuDIOPORMAR.setScale(2);
    stdokuDIOPORMAR.setTableName("STDOKU");
    stdokuDIOPORMAR.setVisible(com.borland.jb.util.TriStateProperty.TRUE);
    stdokuDIOPORMAR.setWidth(14);
    stdokuDIOPORMAR.setServerColumnName("DIOPORMAR");
    stdokuDIOPORMAR.setSqlType(2);

    stdokuDIOPORPOR.setCaption("Dio poravnanja poreza");
    stdokuDIOPORPOR.setColumnName("DIOPORPOR");
    stdokuDIOPORPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuDIOPORPOR.setDisplayMask("###,###,##0.00");
    stdokuDIOPORPOR.setDefault("0");
    stdokuDIOPORPOR.setPrecision(15);
    stdokuDIOPORPOR.setScale(2);
    stdokuDIOPORPOR.setTableName("STDOKU");
    stdokuDIOPORPOR.setVisible(com.borland.jb.util.TriStateProperty.TRUE);
    stdokuDIOPORPOR.setWidth(14);
    stdokuDIOPORPOR.setServerColumnName("DIOPORPOR");
    stdokuDIOPORPOR.setSqlType(2);

    stdokuPORAV.setCaption("Poravnanje");
    stdokuPORAV.setColumnName("PORAV");
    stdokuPORAV.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stdokuPORAV.setDisplayMask("###,###,##0.00");
    stdokuPORAV.setDefault("0");
    stdokuPORAV.setPrecision(15);
    stdokuPORAV.setScale(2);
    stdokuPORAV.setTableName("STDOKU");
    stdokuPORAV.setVisible(com.borland.jb.util.TriStateProperty.TRUE);
    stdokuPORAV.setWidth(14);
    stdokuPORAV.setServerColumnName("PORAV");
    stdokuPORAV.setSqlType(2);

    stdokuSTATUS.setColumnName("STATUS");
    stdokuSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuSTATUS.setDefault("N");
    stdokuSTATUS.setPrecision(1);
    stdokuSTATUS.setTableName("STDOKU");
    stdokuSTATUS.setSqlType(1);
    stdokuSTATUS.setServerColumnName("STATUS");


    stdokuSKAL.setCaption("Prethodni broj kalkulacije");
    stdokuSKAL.setColumnName("SKAL");
    stdokuSKAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuSKAL.setPrecision(52);
    stdokuSKAL.setTableName("STDOKU");
    stdokuSKAL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokuSKAL.setServerColumnName("SKAL");
    stdokuSKAL.setSqlType(1);

    stdokuRBSID.setCaption("ID stavke");
    stdokuRBSID.setColumnName("RBSID");
    stdokuRBSID.setDataType(com.borland.dx.dataset.Variant.INT);
    stdokuRBSID.setTableName("STDOKU");
    stdokuRBSID.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stdokuRBSID.setServerColumnName("RBSID");
    stdokuRBSID.setSqlType(4);
    
    stdokuVEZA.setCaption("Veza");
    stdokuVEZA.setColumnName("VEZA");
    stdokuVEZA.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuVEZA.setPrecision(50);
    stdokuVEZA.setTableName("STDOKU");
    stdokuVEZA.setSqlType(1);
    stdokuVEZA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);    
    stdokuVEZA.setServerColumnName("VEZA");
    
    stdokuIDSTAVKA.setCaption("ID stavka");
    stdokuIDSTAVKA.setColumnName("id_stavka");
    stdokuIDSTAVKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    stdokuIDSTAVKA.setPrecision(50);
    stdokuIDSTAVKA.setTableName("STDOKU");
    stdokuIDSTAVKA.setSqlType(1);
    stdokuIDSTAVKA.setVisible(com.borland.jb.util.TriStateProperty.FALSE);    
    stdokuIDSTAVKA.setServerColumnName("id_stavka");


    stdoku.setResolver(dm.getQresolver());
    stdoku.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),
        "SELECT * from stdoku", null, true, Load.ALL));
 setColumns(new Column[] {stdokuLOKK, stdokuAKTIV, stdokuCSKL, stdokuVRDOK, stdokuGOD, stdokuBRDOK, stdokuRBR, stdokuCART, stdokuCART1,
        stdokuBC, stdokuNAZART, stdokuJM, stdokuKOL, stdokuDC, stdokuDC_VAL, stdokuIDOB, stdokuIDOB_VAL, stdokuPRAB, stdokuIRAB, stdokuPZT, stdokuIZT,
        stdokuNC, stdokuPMAR, stdokuMAR, stdokuVC, stdokuPOR1, stdokuPOR2, stdokuPOR3, stdokuMC, stdokuINAB, stdokuIMAR, stdokuIBP, stdokuIPOR,
        stdokuISP, stdokuZC, stdokuIZAD, stdokuKOLFLH, stdokuSKOL, stdokuSVC, stdokuSMC, stdokuDIOPORMAR, stdokuDIOPORPOR, stdokuPORAV,
        stdokuSTATUS, stdokuSKAL, stdokuRBSID, stdokuCSKLART, stdokuVEZA, stdokuIDSTAVKA, stdokuKOL1, stdokuKOL2});
*/ 
    initModule();
    initClones();
  }

  private void initClones() {
    createFilteredDataSet(stdokuPST, "1=0");
    createFilteredDataSet(stdokuPRI, "1=0");
    createFilteredDataSet(stdokuPOR, "1=0");
    createFilteredDataSet(stdokuPTE, "1=0");
    createFilteredDataSet(stdokuPRK, "1=0");
    createFilteredDataSet(stdokuKAL, "1=0");
    createFilteredDataSet(stdokuPRE, "1=0");
    createFilteredDataSet(stdokuINV, "1=0");
  }

  
/*  public void setall(){


    ddl.create("stdoku")
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
       .addFloat("kol", 17, 3)
       .addFloat("dc", 12, 2)
       .addFloat("dc_val", 12, 2)
       .addFloat("idob", 17, 2)
       .addFloat("idob_val", 17, 2)
       .addFloat("prab", 6, 2)
       .addFloat("irab", 17, 2)
       .addFloat("pzt", 17, 7)
       .addFloat("izt", 17, 2)
       .addFloat("nc", 12, 2)
       .addFloat("pmar", 6, 2)
       .addFloat("mar", 17, 2)
       .addFloat("vc", 12, 2)
       .addFloat("por1", 17, 2)
       .addFloat("por2", 17, 2)
       .addFloat("por3", 17, 2)
       .addFloat("mc", 12, 2)
       .addFloat("inab", 17, 2)
       .addFloat("imar", 17, 2)
       .addFloat("ibp", 17, 2)
       .addFloat("ipor", 17, 2)
       .addFloat("isp", 17, 2)
       .addFloat("zc", 12, 2)
       .addFloat("izad", 17, 2)
       .addFloat("kolflh", 17, 3)
       .addFloat("skol", 17, 3)
       .addFloat("svc", 12, 2)
       .addFloat("smc", 12, 2)
       .addFloat("diopormar", 17, 2)
       .addFloat("dioporpor", 17, 2)
       .addFloat("porav", 17, 2)
       .addChar("status", 1, "N")
       .addChar("skal", 52)
       .addInteger("rbsid", 6)
       .addChar("csklart", 12)
	   .addChar("veza", 50)
	   .addChar("id_stavka", 50)
       .addFloat("kol1", 17, 3)
       .addFloat("kol2", 17, 3)
       .addPrimaryKey("cskl,vrdok,god,brdok,rbr");

    Naziv="Stdoku";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cskl,vrdok,god,brdok",
        "cart", "veza", "id_stavka"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

  }*/
}