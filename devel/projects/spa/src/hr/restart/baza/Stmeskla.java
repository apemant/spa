/****license*****************************************************************
**   file: Stmeskla.java
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
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Stmeskla extends KreirDrop implements DataModule {

  private static Stmeskla Stmesklaclass;
  dM dm  = dM.getDataModule();
  QueryDataSet Stmeskla = new QueryDataSet();
  QueryDataSet StmesklaMES = new QueryDataSet();
  QueryDataSet StmesklaMEU = new QueryDataSet();
  QueryDataSet StmesklaMEI = new QueryDataSet();

/*  Column stmesklaLOKK = new Column();
  Column stmesklaAKTIV = new Column();
  Column stmesklaCSKLIZ = new Column();
  Column stmesklaCSKLUL = new Column();
  Column stmesklaVRDOK = new Column();
  Column stmesklaGOD = new Column();
  Column stmesklaBRDOK = new Column();
  Column stmesklaRBR = new Column();
  Column stmesklaCART = new Column();
  Column stmesklaCART1 = new Column();
  Column stmesklaBC = new Column();
  Column stmesklaNAZART = new Column();
  Column stmesklaJM = new Column();
  Column stmesklaKOL = new Column();
  Column stmesklaNC = new Column();
  Column stmesklaVC = new Column();
  Column stmesklaMC = new Column();
  Column stmesklaZC = new Column();
  Column stmesklaZCUL = new Column();
  Column stmesklaSKOL = new Column();
  Column stmesklaSNC = new Column();
  Column stmesklaSVC = new Column();
  Column stmesklaSMC = new Column();
  Column stmesklaINABIZ = new Column();
  Column stmesklaINABUL = new Column();
  Column stmesklaIMARIZ = new Column();
  Column stmesklaIMARUL = new Column();
  Column stmesklaPMAR = new Column();
  Column stmesklaIPORIZ = new Column();
  Column stmesklaIPORUL = new Column();
  Column stmesklaPORAV = new Column();
  Column stmesklaDIOPORMAR = new Column();
  Column stmesklaDIOPORPOR = new Column();
  Column stmesklaZADRAZIZ = new Column();
  Column stmesklaZADRAZUL = new Column();
  Column stmesklaKOLFLH = new Column();
  Column stmesklaSKAL = new Column();
  Column stmesklaITKAL = new Column();
  Column stmesklaSITKAL = new Column();
  Column stmesklaSBSIZ = new Column();
  Column stmesklaRBSID = new Column();
  Column stmesklaKOL1 = new Column();
  Column stmesklaKOL2 = new Column();
*/
  public static Stmeskla getDataModule() {
    if (Stmesklaclass == null) {
      Stmesklaclass = new Stmeskla();
    }
    return Stmesklaclass;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return Stmeskla;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMES() {
    return StmesklaMES;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMEU() {
    return StmesklaMEU;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getMEI() {
    return StmesklaMEI;
  }

  public Stmeskla() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  protected void modifyColumn(Column c) {
    if (c.getColumnName().equals("NC") || c.getColumnName().equals("ZC") || c.getColumnName().equals("ZCUL")) {
      int scale = Aus.getNumber(frmParam.getParam("robno", "skladDec", 
          "2", "Broj decimala za skladišne cijene (2-4)").trim());
      c.setScale(scale);
      c.setPrecision(c.getPrecision() - 2 + scale);
      if (scale > 0 && scale < 8)
        c.setDisplayMask("###,###,##0."+hr.restart.util.Aus.string(scale, '0'));
    }
  }
  
  private void jbInit() throws Exception {
    initModule();
    
/*    stmesklaZCUL.setCaption("Ulazna cijena");
    stmesklaZCUL.setColumnName("ZCUL");
    stmesklaZCUL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaZCUL.setDisplayMask("###,###,##0.00");
    stmesklaZCUL.setDefault("0");
    stmesklaZCUL.setPrecision(15);
    stmesklaZCUL.setScale(2);
    stmesklaZCUL.setTableName("STMESKLA");
    stmesklaZCUL.setServerColumnName("ZCUL");
    stmesklaZCUL.setSqlType(2);
    stmesklaKOLFLH.setColumnName("KOLFLH");
    stmesklaKOLFLH.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaKOLFLH.setDisplayMask("###,###,##0.000");
    stmesklaKOLFLH.setDefault("0");
    stmesklaKOLFLH.setPrecision(15);
    stmesklaKOLFLH.setScale(3);
    stmesklaKOLFLH.setTableName("STMESKLA");
    stmesklaKOLFLH.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stmesklaKOLFLH.setServerColumnName("KOLFLH");
    stmesklaKOLFLH.setSqlType(2);
    stmesklaZADRAZUL.setCaption("Zaduženje ulaza");
    stmesklaZADRAZUL.setColumnName("ZADRAZUL");
    stmesklaZADRAZUL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaZADRAZUL.setDisplayMask("###,###,##0.00");
    stmesklaZADRAZUL.setDefault("0");
    stmesklaZADRAZUL.setPrecision(15);
    stmesklaZADRAZUL.setScale(2);
    stmesklaZADRAZUL.setTableName("STMESKLA");
    stmesklaZADRAZUL.setServerColumnName("ZADRAZUL");
    stmesklaZADRAZUL.setSqlType(2);
    stmesklaZADRAZIZ.setCaption("Razduženje izlaza");
    stmesklaZADRAZIZ.setColumnName("ZADRAZIZ");
    stmesklaZADRAZIZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaZADRAZIZ.setDisplayMask("###,###,##0.00");
    stmesklaZADRAZIZ.setDefault("0");
    stmesklaZADRAZIZ.setPrecision(15);
    stmesklaZADRAZIZ.setScale(2);
    stmesklaZADRAZIZ.setTableName("STMESKLA");
    stmesklaZADRAZIZ.setServerColumnName("ZADRAZIZ");
    stmesklaZADRAZIZ.setSqlType(2);
    stmesklaDIOPORPOR.setCaption("Dio poravnanja poreza");
    stmesklaDIOPORPOR.setColumnName("DIOPORPOR");
    stmesklaDIOPORPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaDIOPORPOR.setDisplayMask("###,###,##0.00");
    stmesklaDIOPORPOR.setDefault("0");
    stmesklaDIOPORPOR.setPrecision(15);
    stmesklaDIOPORPOR.setScale(2);
    stmesklaDIOPORPOR.setTableName("STMESKLA");
    stmesklaDIOPORPOR.setServerColumnName("DIOPORPOR");
    stmesklaDIOPORPOR.setSqlType(2);
    stmesklaDIOPORMAR.setCaption("Dio poravnanja marže");
    stmesklaDIOPORMAR.setColumnName("DIOPORMAR");
    stmesklaDIOPORMAR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaDIOPORMAR.setDisplayMask("###,###,##0.00");
    stmesklaDIOPORMAR.setDefault("0");
    stmesklaDIOPORMAR.setPrecision(15);
    stmesklaDIOPORMAR.setScale(2);
    stmesklaDIOPORMAR.setTableName("STMESKLA");
    stmesklaDIOPORMAR.setServerColumnName("DIOPORMAR");
    stmesklaDIOPORMAR.setSqlType(2);
    stmesklaPORAV.setCaption("Poravnanje");
    stmesklaPORAV.setColumnName("PORAV");
    stmesklaPORAV.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaPORAV.setDisplayMask("###,###,##0.00");
    stmesklaPORAV.setDefault("0");
    stmesklaPORAV.setPrecision(15);
    stmesklaPORAV.setScale(2);
    stmesklaPORAV.setTableName("STMESKLA");
    stmesklaPORAV.setServerColumnName("PORAV");
    stmesklaPORAV.setSqlType(2);
    stmesklaIPORUL.setCaption("Iznos poreza ulaza");
    stmesklaIPORUL.setColumnName("IPORUL");
    stmesklaIPORUL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaIPORUL.setDisplayMask("###,###,##0.00");
    stmesklaIPORUL.setDefault("0");
    stmesklaIPORUL.setPrecision(15);
    stmesklaIPORUL.setScale(2);
    stmesklaIPORUL.setTableName("STMESKLA");
    stmesklaIPORUL.setServerColumnName("IPORUL");
    stmesklaIPORUL.setSqlType(2);
    stmesklaIPORIZ.setCaption("Porez izlaza");
    stmesklaIPORIZ.setColumnName("IPORIZ");
    stmesklaIPORIZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaIPORIZ.setDisplayMask("###,###,##0.00");
    stmesklaIPORIZ.setDefault("0");
    stmesklaIPORIZ.setPrecision(15);
    stmesklaIPORIZ.setScale(2);
    stmesklaIPORIZ.setTableName("STMESKLA");
    stmesklaIPORIZ.setServerColumnName("IPORIZ");
    stmesklaIPORIZ.setSqlType(2);

    stmesklaPMAR.setCaption("Posto marže");
    stmesklaPMAR.setColumnName("PMAR");
    stmesklaPMAR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaPMAR.setDisplayMask("#,##0.00");
    stmesklaPMAR.setDefault("0");
    stmesklaPMAR.setPrecision(10);
    stmesklaPMAR.setScale(2);
    stmesklaPMAR.setTableName("STMESKLA");
    stmesklaPMAR.setWidth(8);
    stmesklaPMAR.setServerColumnName("PMAR");
    stmesklaPMAR.setSqlType(2);

    stmesklaIMARUL.setCaption("Marža ulaza");
    stmesklaIMARUL.setColumnName("IMARUL");
    stmesklaIMARUL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaIMARUL.setDisplayMask("###,###,##0.00");
    stmesklaIMARUL.setDefault("0");
    stmesklaIMARUL.setPrecision(15);
    stmesklaIMARUL.setScale(2);
    stmesklaIMARUL.setTableName("STMESKLA");
    stmesklaIMARUL.setServerColumnName("IMARUL");
    stmesklaIMARUL.setSqlType(2);
    stmesklaIMARIZ.setCaption("Marža izlaza");
    stmesklaIMARIZ.setColumnName("IMARIZ");
    stmesklaIMARIZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaIMARIZ.setDisplayMask("###,###,##0.00");
    stmesklaIMARIZ.setDefault("0");
    stmesklaIMARIZ.setPrecision(15);
    stmesklaIMARIZ.setScale(2);
    stmesklaIMARIZ.setTableName("STMESKLA");
    stmesklaIMARIZ.setServerColumnName("IMARIZ");
    stmesklaIMARIZ.setSqlType(2);
    stmesklaINABUL.setCaption("Nabavni iznos ulaza");
    stmesklaINABUL.setColumnName("INABUL");
    stmesklaINABUL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaINABUL.setDisplayMask("###,###,##0.00");
    stmesklaINABUL.setDefault("0");
    stmesklaINABUL.setPrecision(15);
    stmesklaINABUL.setScale(2);
    stmesklaINABUL.setTableName("STMESKLA");
    stmesklaINABUL.setServerColumnName("INABUL");
    stmesklaINABUL.setSqlType(2);
    stmesklaINABIZ.setCaption("Nabavni iznos izlaza");
    stmesklaINABIZ.setColumnName("INABIZ");
    stmesklaINABIZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaINABIZ.setDisplayMask("###,###,##0.00");
    stmesklaINABIZ.setDefault("0");
    stmesklaINABIZ.setPrecision(15);
    stmesklaINABIZ.setScale(2);
    stmesklaINABIZ.setTableName("STMESKLA");
    stmesklaINABIZ.setServerColumnName("INABIZ");
    stmesklaINABIZ.setSqlType(2);
    stmesklaSMC.setCaption("Stara cijena s porezom");
    stmesklaSMC.setColumnName("SMC");
    stmesklaSMC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaSMC.setDisplayMask("###,###,##0.00");
    stmesklaSMC.setDefault("0");
    stmesklaSMC.setPrecision(15);
    stmesklaSMC.setScale(2);
    stmesklaSMC.setTableName("STMESKLA");
    stmesklaSMC.setServerColumnName("SMC");
    stmesklaSMC.setSqlType(2);
    stmesklaSVC.setCaption("Stara cijena bez poreza");
    stmesklaSVC.setColumnName("SVC");
    stmesklaSVC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaSVC.setDisplayMask("###,###,##0.00");
    stmesklaSVC.setDefault("0");
    stmesklaSVC.setPrecision(15);
    stmesklaSVC.setScale(2);
    stmesklaSVC.setTableName("STMESKLA");
    stmesklaSVC.setServerColumnName("SVC");
    stmesklaSVC.setSqlType(2);
    stmesklaSNC.setCaption("Stara nabavna cijena");
    stmesklaSNC.setColumnName("SNC");
    stmesklaSNC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaSNC.setDisplayMask("###,###,##0.00");
    stmesklaSNC.setDefault("0");
    stmesklaSNC.setPrecision(15);
    stmesklaSNC.setScale(2);
    stmesklaSNC.setTableName("STMESKLA");
    stmesklaSNC.setServerColumnName("SNC");
    stmesklaSNC.setSqlType(2);
    stmesklaSKOL.setCaption("Stara koli\u010Dina");
    stmesklaSKOL.setColumnName("SKOL");
    stmesklaSKOL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaSKOL.setDisplayMask("###,###,##0.00");
    stmesklaSKOL.setDefault("0");
    stmesklaSKOL.setPrecision(15);
    stmesklaSKOL.setScale(3);
    stmesklaSKOL.setTableName("STMESKLA");
    stmesklaSKOL.setServerColumnName("SKOL");
    stmesklaSKOL.setSqlType(2);
    stmesklaZC.setCaption("Cijena");
    stmesklaZC.setColumnName("ZC");
    stmesklaZC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaZC.setDisplayMask("###,###,##0.00");
    stmesklaZC.setDefault("0");
    stmesklaZC.setPrecision(15);
    stmesklaZC.setScale(2);
    stmesklaZC.setTableName("STMESKLA");
    stmesklaZC.setServerColumnName("ZC");
    stmesklaZC.setSqlType(2);
    stmesklaMC.setCaption("Cijena s porezom");
    stmesklaMC.setColumnName("MC");
    stmesklaMC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaMC.setDisplayMask("###,###,##0.00");
    stmesklaMC.setDefault("0");
    stmesklaMC.setPrecision(15);
    stmesklaMC.setScale(2);
    stmesklaMC.setTableName("STMESKLA");
    stmesklaMC.setServerColumnName("MC");
    stmesklaMC.setSqlType(2);
    stmesklaVC.setCaption("Cijena bez poreza");
    stmesklaVC.setColumnName("VC");
    stmesklaVC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaVC.setDisplayMask("###,###,##0.00");
    stmesklaVC.setDefault("0");
    stmesklaVC.setPrecision(15);
    stmesklaVC.setScale(2);
    stmesklaVC.setTableName("STMESKLA");
    stmesklaVC.setServerColumnName("VC");
    stmesklaVC.setSqlType(2);
    stmesklaNC.setCaption("Nabavna cijena");
    stmesklaNC.setColumnName("NC");
    stmesklaNC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaNC.setDisplayMask("###,###,##0.00");
    stmesklaNC.setExportDisplayMask("");
    stmesklaNC.setDefault("0");
    stmesklaNC.setPrecision(15);
    stmesklaNC.setScale(2);
    stmesklaNC.setTableName("STMESKLA");
    stmesklaNC.setServerColumnName("NC");
    stmesklaNC.setSqlType(2);
    stmesklaKOL.setCaption("Koli\u010Dina");
    stmesklaKOL.setColumnName("KOL");
    stmesklaKOL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaKOL.setDisplayMask("###,###,##0.000");
    stmesklaKOL.setDefault("0");
    stmesklaKOL.setPrecision(15);
    stmesklaKOL.setScale(3);
    stmesklaKOL.setTableName("STMESKLA");
    stmesklaKOL.setServerColumnName("KOL");
    stmesklaKOL.setSqlType(2);

    stmesklaKOL1.setCaption("Pakiranja");
    stmesklaKOL1.setColumnName("KOL1");
    stmesklaKOL1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaKOL1.setDisplayMask("###,###,##0.000");
    stmesklaKOL1.setDefault("0");
    stmesklaKOL1.setPrecision(15);
    stmesklaKOL1.setScale(3);
    stmesklaKOL1.setTableName("STMESKLA");
    stmesklaKOL1.setServerColumnName("KOL1");
    stmesklaKOL1.setSqlType(2);

    stmesklaKOL2.setCaption("Poleta");
    stmesklaKOL2.setColumnName("KOL2");
    stmesklaKOL2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    stmesklaKOL2.setDisplayMask("###,###,##0.000");
    stmesklaKOL2.setDefault("0");
    stmesklaKOL2.setPrecision(15);
    stmesklaKOL2.setScale(3);
    stmesklaKOL2.setTableName("STMESKLA");
    stmesklaKOL2.setServerColumnName("KOL2");
    stmesklaKOL2.setSqlType(2);

    stmesklaJM.setCaption("Jm");
    stmesklaJM.setColumnName("JM");
    stmesklaJM.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaJM.setPrecision(3);
    stmesklaJM.setTableName("STMESKLA");
    stmesklaJM.setWidth(4);
    stmesklaJM.setServerColumnName("JM");
    stmesklaJM.setSqlType(1);
    stmesklaNAZART.setCaption("Naziv");
    stmesklaNAZART.setColumnName("NAZART");
    stmesklaNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaNAZART.setPrecision(50);
    stmesklaNAZART.setTableName("STMESKLA");
    stmesklaNAZART.setWidth(25);
    stmesklaNAZART.setServerColumnName("NAZART");
    stmesklaNAZART.setSqlType(1);
    stmesklaBC.setCaption("Barcode");
    stmesklaBC.setColumnName("BC");
    stmesklaBC.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaBC.setPrecision(20);
    stmesklaBC.setTableName("STMESKLA");
    stmesklaBC.setWidth(12);
    stmesklaBC.setServerColumnName("BC");
    stmesklaBC.setSqlType(1);
    stmesklaCART1.setCaption("Oznaka");
    stmesklaCART1.setColumnName("CART1");
    stmesklaCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaCART1.setPrecision(20);
    stmesklaCART1.setTableName("STMESKLA");
    stmesklaCART1.setWidth(13);
    stmesklaCART1.setServerColumnName("CART1");
    stmesklaCART1.setSqlType(1);
    stmesklaCART.setCaption("Šifra");
    stmesklaCART.setColumnName("CART");
    stmesklaCART.setDataType(com.borland.dx.dataset.Variant.INT);
    stmesklaCART.setTableName("STMESKLA");
    stmesklaCART.setWidth(4);
    stmesklaCART.setServerColumnName("CART");
    stmesklaCART.setSqlType(4);
    stmesklaRBR.setCaption("Rbr");
    stmesklaRBR.setColumnName("RBR");
    stmesklaRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    stmesklaRBR.setRowId(true);
    stmesklaRBR.setTableName("STMESKLA");
    stmesklaRBR.setWidth(4);
    stmesklaRBR.setServerColumnName("RBR");
    stmesklaRBR.setSqlType(5);
    stmesklaBRDOK.setCaption("Broj");
    stmesklaBRDOK.setColumnName("BRDOK");
    stmesklaBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    stmesklaBRDOK.setRowId(true);
    stmesklaBRDOK.setTableName("STMESKLA");
    stmesklaBRDOK.setWidth(7);
    stmesklaBRDOK.setServerColumnName("BRDOK");
    stmesklaBRDOK.setSqlType(4);
    stmesklaGOD.setCaption("Godina");
    stmesklaGOD.setColumnName("GOD");
    stmesklaGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaGOD.setPrecision(4);
    stmesklaGOD.setRowId(true);
    stmesklaGOD.setTableName("STMESKLA");
    stmesklaGOD.setServerColumnName("GOD");
    stmesklaGOD.setSqlType(1);
    stmesklaVRDOK.setCaption("Vrsta");
    stmesklaVRDOK.setColumnName("VRDOK");
    stmesklaVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaVRDOK.setPrecision(3);
    stmesklaVRDOK.setRowId(true);
    stmesklaVRDOK.setTableName("STMESKLA");
    stmesklaVRDOK.setServerColumnName("VRDOK");
    stmesklaVRDOK.setSqlType(1);
    stmesklaCSKLUL.setCaption("Ulazno skladište");
    stmesklaCSKLUL.setColumnName("CSKLUL");
    stmesklaCSKLUL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaCSKLUL.setPrecision(12);
    stmesklaCSKLUL.setRowId(true);
    stmesklaCSKLUL.setTableName("STMESKLA");
    stmesklaCSKLUL.setServerColumnName("CSKLUL");
    stmesklaCSKLUL.setSqlType(1);
    stmesklaCSKLIZ.setCaption("Izlazno skladište");
    stmesklaCSKLIZ.setColumnName("CSKLIZ");
    stmesklaCSKLIZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaCSKLIZ.setPrecision(12);
    stmesklaCSKLIZ.setRowId(true);
    stmesklaCSKLIZ.setTableName("STMESKLA");
    stmesklaCSKLIZ.setServerColumnName("CSKLIZ");
    stmesklaCSKLIZ.setSqlType(1);
    stmesklaAKTIV.setColumnName("AKTIV");
    stmesklaAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaAKTIV.setDefault("D");
    stmesklaAKTIV.setPrecision(1);
    stmesklaAKTIV.setTableName("STMESKLA");
    stmesklaAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stmesklaAKTIV.setServerColumnName("AKTIV");
    stmesklaAKTIV.setSqlType(1);
    stmesklaLOKK.setColumnName("LOKK");
    stmesklaLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaLOKK.setDefault("N");
    stmesklaLOKK.setPrecision(1);
    stmesklaLOKK.setTableName("STMESKLA");
    stmesklaLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stmesklaLOKK.setServerColumnName("LOKK");
    stmesklaLOKK.setSqlType(1);

    stmesklaSKAL.setCaption("Prethodni broj kalkulacije");
    stmesklaSKAL.setColumnName("SKAL");
    stmesklaSKAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaSKAL.setPrecision(52);
    stmesklaSKAL.setTableName("STMESKLA");
    stmesklaSKAL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stmesklaSKAL.setServerColumnName("SKAL");
    stmesklaSKAL.setSqlType(1);

    stmesklaITKAL.setCaption("Broj kalkulacije zadnjeg izlaza");
    stmesklaITKAL.setColumnName("ITKAL");
    stmesklaITKAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaITKAL.setPrecision(52);
    stmesklaITKAL.setTableName("STMESKLA");
    stmesklaITKAL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stmesklaITKAL.setServerColumnName("ITKAL");
    stmesklaITKAL.setSqlType(1);

    stmesklaSITKAL.setCaption("Broj prethodne kalkulacije izlaza");
    stmesklaSITKAL.setColumnName("SITKAL");
    stmesklaSITKAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    stmesklaSITKAL.setPrecision(52);
    stmesklaSITKAL.setTableName("STMESKLA");
    stmesklaSITKAL.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stmesklaSITKAL.setServerColumnName("SITKAL");
    stmesklaSITKAL.setSqlType(1);

    stmesklaSBSIZ.setCaption("Br stavki izlaza po zadnjem izl.");
    stmesklaSBSIZ.setColumnName("SBSIZ");
    stmesklaSBSIZ.setDataType(com.borland.dx.dataset.Variant.SHORT);
    stmesklaSBSIZ.setPrecision(4);
    stmesklaSBSIZ.setTableName("STMESKLA");
    stmesklaSBSIZ.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stmesklaSBSIZ.setServerColumnName("SBSIZ");
    stmesklaSBSIZ.setSqlType(5);

    stmesklaRBSID.setCaption("ID stavke");
    stmesklaRBSID.setColumnName("RBSID");
    stmesklaRBSID.setDataType(com.borland.dx.dataset.Variant.INT);
    stmesklaRBSID.setTableName("STMESKLA");
    stmesklaRBSID.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    stmesklaRBSID.setServerColumnName("RBSID");
    stmesklaRBSID.setSqlType(4);

  Stmeskla.setResolver(dm.getQresolver());
    Stmeskla.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from Stmeskla", null, true, Load.ALL));
 setColumns(new Column[] {stmesklaLOKK, stmesklaAKTIV, stmesklaCSKLIZ, stmesklaCSKLUL, stmesklaVRDOK, stmesklaGOD, stmesklaBRDOK,
        stmesklaRBR, stmesklaCART, stmesklaCART1, stmesklaBC, stmesklaNAZART, stmesklaJM, stmesklaKOL, stmesklaNC, stmesklaVC, stmesklaMC, stmesklaZC,
        stmesklaZCUL, stmesklaSKOL, stmesklaSNC, stmesklaSVC, stmesklaSMC, stmesklaINABIZ, stmesklaINABUL, stmesklaIMARIZ, stmesklaIMARUL, stmesklaPMAR,
        stmesklaIPORIZ, stmesklaIPORUL, stmesklaPORAV, stmesklaDIOPORMAR, stmesklaDIOPORPOR, stmesklaZADRAZIZ, stmesklaZADRAZUL, stmesklaKOLFLH,
        stmesklaSKAL, stmesklaITKAL, stmesklaSITKAL, stmesklaSBSIZ, stmesklaRBSID, stmesklaKOL1, stmesklaKOL2});
*/
    createFilteredDataSet(StmesklaMES, "1=0");
    createFilteredDataSet(StmesklaMEU, "1=0");
    createFilteredDataSet(StmesklaMEI, "1=0");
  }
}