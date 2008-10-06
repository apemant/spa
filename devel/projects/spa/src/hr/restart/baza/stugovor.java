/****license*****************************************************************
**   file: stugovor.java
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

public class stugovor extends KreirDrop {

  private static stugovor stugovorclass;
  
  QueryDataSet stugovor = new raDataSet();
  
  public static stugovor getDataModule() {
    if (stugovorclass == null) {
      stugovorclass = new stugovor();
    }
    return stugovorclass;
  }

  public QueryDataSet getQueryDataSet() {
    return stugovor;
  }

  public stugovor() {
    try {
      modules.put(this.getClass().getName(), this);
      initModule();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}


//import com.borland.dx.dataset.*;
//import com.borland.dx.sql.dataset.*;
//
//
//
//public class stugovor extends KreirDrop implements DataModule {
//
//  dM dm  = dM.getDataModule();
//  private static stugovor stugovorclass;
//
//  QueryDataSet stugovor = new QueryDataSet();
//
//  Column stugovorCUGOVOR = new Column();
//  Column stugovorRBR = new Column();
//  Column stugovorRBSID = new Column();
//  Column stugovorCART = new Column();
//  Column stugovorCART1 = new Column();
//  Column stugovorBC = new Column();
//  Column stugovorNAZART = new Column();
//  Column stugovorOZNVAL = new Column();
//  Column stugovorTECAJ = new Column();
//  Column stugovorJM = new Column();
//  Column stugovorKOL = new Column();
//  Column stugovorVAL_VC = new Column();
//  Column stugovorVAL_MC = new Column();
//  Column stugovorFC = new Column();
//  Column stugovorUPRAB = new Column();
//  Column stugovorUIRAB = new Column();
//  Column stugovorFVC = new Column();
//  Column stugovorUPPOR = new Column();
//  Column stugovorPOR1 = new Column();
//  Column stugovorPOR2 = new Column();
//  Column stugovorPOR3 = new Column();
//  Column stugovorIPRODBP = new Column();
//  Column stugovorFMC = new Column();
//  Column stugovorIPRODSP = new Column();
//  Column stugovorCSKLART = new Column();
//  Column stugovorMANIPULATIVNI = new Column();
//  Column stugovorIMINIZNOSBP = new Column();
//  Column stugovorSTATUSMAN = new Column();
//  Column stugovorSTATUSPON = new Column();
//  Column stugovorSTATUSRAC = new Column();
//  Column stugovorIZNOSRAC = new Column();
//  Column stugovorIZNOSRACSP = new Column();
//  Column stugovorPPOR1 = new Column();
//  Column stugovorPPOR2 = new Column();
//  Column stugovorPPOR3 = new Column();
//  
//  public static stugovor getDataModule() {
//    if (stugovorclass == null) {
//      stugovorclass = new stugovor();
//    }
//    return stugovorclass;
//  }
//
//  public QueryDataSet getQueryDataSet() {
//    return stugovor;
//  }
//
//  public stugovor() {
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
//    stugovorCUGOVOR.setCaption("Broj ugovora");
//    stugovorCUGOVOR.setColumnName("CUGOVOR");
//    stugovorCUGOVOR.setDataType(com.borland.dx.dataset.Variant.STRING);
//    stugovorCUGOVOR.setPrecision(13);
//    stugovorCUGOVOR.setRowId(true);
//    stugovorCUGOVOR.setTableName("STUGOVOR");
//    stugovorCUGOVOR.setServerColumnName("CUGOVOR");
//    stugovorCUGOVOR.setSqlType(1);
//    stugovorRBR.setCaption("Rbr");
//    stugovorRBR.setColumnName("RBR");
//    stugovorRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    stugovorRBR.setTableName("STUGOVOR");
//    stugovorRBR.setServerColumnName("RBR");
//    stugovorRBR.setSqlType(5);
//    stugovorRBR.setWidth(4);
//    stugovorRBSID.setCaption("ID_stavke");
//    stugovorRBSID.setColumnName("RBSID");
//    stugovorRBSID.setDataType(com.borland.dx.dataset.Variant.INT);
//    stugovorRBSID.setRowId(true);
//    stugovorRBSID.setTableName("STUGOVOR");
//    stugovorRBSID.setServerColumnName("RBSID");
//    stugovorRBSID.setSqlType(4);
//    stugovorRBSID.setWidth(6);
//    stugovorRBSID.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    stugovorCART.setCaption("Šifra");
//    stugovorCART.setColumnName("CART");
//    stugovorCART.setDataType(com.borland.dx.dataset.Variant.INT);
//    stugovorCART.setTableName("STUGOVOR");
//    stugovorCART.setServerColumnName("CART");
//    stugovorCART.setSqlType(4);
//    stugovorCART.setWidth(8);
//    stugovorCART1.setCaption("Oznaka");
//    stugovorCART1.setColumnName("CART1");
//    stugovorCART1.setDataType(com.borland.dx.dataset.Variant.STRING);
//    stugovorCART1.setPrecision(20);
//    stugovorCART1.setTableName("STUGOVOR");
//    stugovorCART1.setServerColumnName("CART1");
//    stugovorCART1.setSqlType(1);
//    stugovorBC.setCaption("Barcode");
//    stugovorBC.setColumnName("BC");
//    stugovorBC.setDataType(com.borland.dx.dataset.Variant.STRING);
//    stugovorBC.setPrecision(20);
//    stugovorBC.setTableName("STUGOVOR");
//    stugovorBC.setServerColumnName("BC");
//    stugovorBC.setSqlType(1);
//    stugovorNAZART.setCaption("Naziv");
//    stugovorNAZART.setColumnName("NAZART");
//    stugovorNAZART.setDataType(com.borland.dx.dataset.Variant.STRING);
//    stugovorNAZART.setPrecision(50);
//    stugovorNAZART.setTableName("STUGOVOR");
//    stugovorNAZART.setServerColumnName("NAZART");
//    stugovorNAZART.setSqlType(1);
//    stugovorNAZART.setWidth(30);
//    stugovorOZNVAL.setCaption("Valuta");
//    stugovorOZNVAL.setColumnName("OZNVAL");
//    stugovorOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
//    stugovorOZNVAL.setPrecision(3);
//    stugovorOZNVAL.setTableName("STUGOVOR");
//    stugovorOZNVAL.setServerColumnName("OZNVAL");
//    stugovorOZNVAL.setSqlType(1);
//
//    stugovorTECAJ.setCaption("Iznos te\u010Daja");
//    stugovorTECAJ.setColumnName("TECAJ");
//    stugovorTECAJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorTECAJ.setDisplayMask("###,###,###0.000000");
//    stugovorTECAJ.setDefault("0");
//    stugovorTECAJ.setPrecision(17);    
//    stugovorTECAJ.setScale(6);   
//    stugovorTECAJ.setTableName("STUGOVOR");
//    stugovorTECAJ.setSqlType(2);
//    stugovorTECAJ.setServerColumnName("TECAJ");
//
//    stugovorJM.setCaption("Jm");
//    stugovorJM.setColumnName("JM");
//    stugovorJM.setDataType(com.borland.dx.dataset.Variant.STRING);
//    stugovorJM.setPrecision(3);
//    stugovorJM.setWidth(5);
//    stugovorJM.setTableName("STUGOVOR");
//    stugovorJM.setServerColumnName("JM");
//    stugovorJM.setSqlType(1);
//    stugovorKOL.setCaption("Koli\u010Dina");
//    stugovorKOL.setColumnName("KOL");
//    stugovorKOL.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorKOL.setPrecision(17);
//    stugovorKOL.setScale(3);
//    stugovorKOL.setDisplayMask("###,###,##0.000");
//    stugovorKOL.setDefault("0");
//    stugovorKOL.setTableName("STUGOVOR");
//    stugovorKOL.setServerColumnName("KOL");
//    stugovorKOL.setSqlType(2);
//    stugovorVAL_VC.setCaption("Veleprodajna cijena");
//    stugovorVAL_VC.setColumnName("VAL_VC");
//    stugovorVAL_VC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorVAL_VC.setPrecision(17);
//    stugovorVAL_VC.setScale(4);
//    stugovorVAL_VC.setDisplayMask("###,###,##0.0000");
//    stugovorVAL_VC.setDefault("0");
//    stugovorVAL_VC.setTableName("STUGOVOR");
//    stugovorVAL_VC.setServerColumnName("VAL_VC");
//    stugovorVAL_VC.setSqlType(2);
//    stugovorVAL_MC.setCaption("Maloprodajna cijena");
//    stugovorVAL_MC.setColumnName("VAL_MC");
//    stugovorVAL_MC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorVAL_MC.setPrecision(17);
//    stugovorVAL_MC.setScale(4);
//    stugovorVAL_MC.setDisplayMask("###,###,##0.0000");
//    stugovorVAL_MC.setDefault("0");
//    stugovorVAL_MC.setTableName("STUGOVOR");
//    stugovorVAL_MC.setServerColumnName("VAL_MC");
//    stugovorVAL_MC.setSqlType(2);
//    stugovorFC.setCaption("Cijena");
//    stugovorFC.setColumnName("FC");
//    stugovorFC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorFC.setPrecision(17);
//    stugovorFC.setScale(2);
//    stugovorFC.setDisplayMask("###,###,##0.00");
//    stugovorFC.setDefault("0");
//    stugovorFC.setTableName("STUGOVOR");
//    stugovorFC.setServerColumnName("FC");
//    stugovorFC.setSqlType(2);
//    stugovorUPRAB.setCaption("Posto rabata");
//    stugovorUPRAB.setColumnName("UPRAB");
//    stugovorUPRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorUPRAB.setPrecision(8);
//    stugovorUPRAB.setScale(2);
//    stugovorUPRAB.setDisplayMask("###,###,##0.00");
//    stugovorUPRAB.setDefault("0");
//    stugovorUPRAB.setTableName("STUGOVOR");
//    stugovorUPRAB.setServerColumnName("UPRAB");
//    stugovorUPRAB.setSqlType(2);
//    stugovorUIRAB.setCaption("Iznos rabata");
//    stugovorUIRAB.setColumnName("UIRAB");
//    stugovorUIRAB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorUIRAB.setPrecision(17);
//    stugovorUIRAB.setScale(2);
//    stugovorUIRAB.setDisplayMask("###,###,##0.00");
//    stugovorUIRAB.setDefault("0");
//    stugovorUIRAB.setTableName("STUGOVOR");
//    stugovorUIRAB.setServerColumnName("UIRAB");
//    stugovorUIRAB.setSqlType(2);
//    stugovorFVC.setCaption("Cijena bez poreza");
//    stugovorFVC.setColumnName("FVC");
//    stugovorFVC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorFVC.setPrecision(17);
//    stugovorFVC.setScale(2);
//    stugovorFVC.setDisplayMask("###,###,##0.00");
//    stugovorFVC.setDefault("0");
//    stugovorFVC.setTableName("STUGOVOR");
//    stugovorFVC.setServerColumnName("FVC");
//    stugovorFVC.setSqlType(2);
//    stugovorUPPOR.setCaption("Posto poreza");
//    stugovorUPPOR.setColumnName("UPPOR");
//    stugovorUPPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorUPPOR.setPrecision(8);
//    stugovorUPPOR.setScale(2);
//    stugovorUPPOR.setDisplayMask("###,###,##0.00");
//    stugovorUPPOR.setDefault("0");
//    stugovorUPPOR.setTableName("STUGOVOR");
//    stugovorUPPOR.setServerColumnName("UPPOR");
//    stugovorUPPOR.setSqlType(2);
//    stugovorPOR1.setCaption("Porez 1");
//    stugovorPOR1.setColumnName("POR1");
//    stugovorPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorPOR1.setPrecision(17);
//    stugovorPOR1.setScale(2);
//    stugovorPOR1.setDisplayMask("###,###,##0.00");
//    stugovorPOR1.setDefault("0");
//    stugovorPOR1.setTableName("STUGOVOR");
//    stugovorPOR1.setServerColumnName("POR1");
//    stugovorPOR1.setSqlType(2);
//    stugovorPOR2.setCaption("Porez 2");
//    stugovorPOR2.setColumnName("POR2");
//    stugovorPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorPOR2.setPrecision(17);
//    stugovorPOR2.setScale(2);
//    stugovorPOR2.setDisplayMask("###,###,##0.00");
//    stugovorPOR2.setDefault("0");
//    stugovorPOR2.setTableName("STUGOVOR");
//    stugovorPOR2.setServerColumnName("POR2");
//    stugovorPOR2.setSqlType(2);
//    stugovorPOR3.setCaption("Porez 3");
//    stugovorPOR3.setColumnName("POR3");
//    stugovorPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorPOR3.setPrecision(17);
//    stugovorPOR3.setScale(2);
//    stugovorPOR3.setDisplayMask("###,###,##0.00");
//    stugovorPOR3.setDefault("0");
//    stugovorPOR3.setTableName("STUGOVOR");
//    stugovorPOR3.setServerColumnName("POR3");
//    stugovorPOR3.setSqlType(2);
//    stugovorIPRODBP.setCaption("Iznos bez poreza");
//    stugovorIPRODBP.setColumnName("IPRODBP");
//    stugovorIPRODBP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorIPRODBP.setPrecision(17);
//    stugovorIPRODBP.setScale(2);
//    stugovorIPRODBP.setDisplayMask("###,###,##0.00");
//    stugovorIPRODBP.setDefault("0");
//    stugovorIPRODBP.setTableName("STUGOVOR");
//    stugovorIPRODBP.setServerColumnName("IPRODBP");
//    stugovorIPRODBP.setSqlType(2);
//    stugovorFMC.setCaption("Cijena s porezom");
//    stugovorFMC.setColumnName("FMC");
//    stugovorFMC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorFMC.setPrecision(17);
//    stugovorFMC.setScale(2);
//    stugovorFMC.setDisplayMask("###,###,##0.00");
//    stugovorFMC.setDefault("0");
//    stugovorFMC.setTableName("STUGOVOR");
//    stugovorFMC.setServerColumnName("FMC");
//    stugovorFMC.setSqlType(2);
//    stugovorIPRODSP.setCaption("Iznos s porezom");
//    stugovorIPRODSP.setColumnName("IPRODSP");
//    stugovorIPRODSP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorIPRODSP.setPrecision(17);
//    stugovorIPRODSP.setScale(2);
//    stugovorIPRODSP.setDisplayMask("###,###,##0.00");
//    stugovorIPRODSP.setDefault("0");
//    stugovorIPRODSP.setTableName("STUGOVOR");
//    stugovorIPRODSP.setServerColumnName("IPRODSP");
//    stugovorIPRODSP.setSqlType(2);
//    stugovorCSKLART.setCaption("Skladište");
//    stugovorCSKLART.setColumnName("CSKLART");
//    stugovorCSKLART.setDataType(com.borland.dx.dataset.Variant.STRING);
//    stugovorCSKLART.setPrecision(12);
//    stugovorCSKLART.setTableName("STUGOVOR");
//    stugovorCSKLART.setServerColumnName("CSKLART");
//    stugovorCSKLART.setSqlType(1);
//    stugovorMANIPULATIVNI.setCaption("Manipulativni troškovi");
//    stugovorMANIPULATIVNI.setColumnName("MANIPULATIVNI");
//    stugovorMANIPULATIVNI.setDataType(com.borland.dx.dataset.Variant.STRING);
//    stugovorMANIPULATIVNI.setPrecision(50);
//    stugovorMANIPULATIVNI.setTableName("STUGOVOR");
//    stugovorMANIPULATIVNI.setServerColumnName("MANIPULATIVNI");
//    stugovorMANIPULATIVNI.setSqlType(1);
//    stugovorMANIPULATIVNI.setWidth(30);
//    stugovorIMINIZNOSBP.setCaption("Minimalni iznos bez poreza");
//    stugovorIMINIZNOSBP.setColumnName("IMINIZNOSBP");
//    stugovorIMINIZNOSBP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorIMINIZNOSBP.setPrecision(17);
//    stugovorIMINIZNOSBP.setScale(2);
//    stugovorIMINIZNOSBP.setDisplayMask("###,###,##0.00");
//    stugovorIMINIZNOSBP.setDefault("0");
//    stugovorIMINIZNOSBP.setTableName("STUGOVOR");
//    stugovorIMINIZNOSBP.setServerColumnName("IMINIZNOSBP");
//    stugovorIMINIZNOSBP.setSqlType(2);
//    stugovorSTATUSMAN.setCaption("Obra\u010Dunavanje man. troškovi");
//    stugovorSTATUSMAN.setColumnName("STATUSMAN");
//    stugovorSTATUSMAN.setDataType(com.borland.dx.dataset.Variant.STRING);
//    stugovorSTATUSMAN.setPrecision(1);
//    stugovorSTATUSMAN.setTableName("STUGOVOR");
//    stugovorSTATUSMAN.setServerColumnName("STATUSMAN");
//    stugovorSTATUSMAN.setSqlType(1);
//    stugovorSTATUSMAN.setDefault("N");
//    stugovorSTATUSPON.setCaption("Poništavanje varijabilnih troškova");
//    stugovorSTATUSPON.setColumnName("STATUSPON");
//    stugovorSTATUSPON.setDataType(com.borland.dx.dataset.Variant.STRING);
//    stugovorSTATUSPON.setPrecision(1);
//    stugovorSTATUSPON.setTableName("STUGOVOR");
//    stugovorSTATUSPON.setServerColumnName("STATUSPON");
//    stugovorSTATUSPON.setSqlType(1);  
//    stugovorSTATUSPON.setDefault("N");
//    stugovorSTATUSRAC.setCaption("Prikaz na slijedeæoj fakturi");
//    stugovorSTATUSRAC.setColumnName("STATUSRAC");
//    stugovorSTATUSRAC.setDataType(com.borland.dx.dataset.Variant.STRING);
//    stugovorSTATUSRAC.setPrecision(1);
//    stugovorSTATUSRAC.setTableName("STUGOVOR");
//    stugovorSTATUSRAC.setServerColumnName("STATUSRAC");
//    stugovorSTATUSRAC.setSqlType(1);  
//    stugovorSTATUSRAC.setDefault("D");
//    stugovorIZNOSRAC.setCaption("Iznos za raèun");
//    stugovorIZNOSRAC.setColumnName("IZNOSRAC");
//    stugovorIZNOSRAC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorIZNOSRAC.setPrecision(17);
//    stugovorIZNOSRAC.setScale(2);
//    stugovorIZNOSRAC.setDisplayMask("###,###,##0.00");
//    stugovorIZNOSRAC.setDefault("0");
//    stugovorIZNOSRAC.setTableName("STUGOVOR");
//    stugovorIZNOSRAC.setServerColumnName("IZNOSRAC");
//    stugovorIZNOSRAC.setSqlType(2);
//    stugovorIZNOSRACSP.setCaption("Iznos za raèun s porezom");
//    stugovorIZNOSRACSP.setColumnName("IZNOSRACSP");
//    stugovorIZNOSRACSP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorIZNOSRACSP.setPrecision(17);
//    stugovorIZNOSRACSP.setScale(2);
//    stugovorIZNOSRACSP.setDisplayMask("###,###,##0.00");
//    stugovorIZNOSRACSP.setDefault("0");
//    stugovorIZNOSRACSP.setTableName("STUGOVOR");
//    stugovorIZNOSRACSP.setServerColumnName("IZNOSRACSP");
//    stugovorIZNOSRACSP.setSqlType(2);
//    
//    stugovorPPOR1.setCaption("Posto poreza 1");
//    stugovorPPOR1.setColumnName("PPOR1");
//    stugovorPPOR1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorPPOR1.setDisplayMask("###,###,##0.00");
//    stugovorPPOR1.setDefault("0");
//    stugovorPPOR1.setPrecision(15);
//    stugovorPPOR1.setScale(2);
//    stugovorPPOR1.setTableName("STUGOVOR");
//    stugovorPPOR1.setWidth(14);
//    stugovorPPOR1.setSqlType(2);
//    stugovorPPOR1.setServerColumnName("PPOR1");
//
//    stugovorPPOR2.setCaption("Posto poreza 2");
//    stugovorPPOR2.setColumnName("PPOR2");
//    stugovorPPOR2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorPPOR2.setDisplayMask("###,###,##0.00");
//    stugovorPPOR2.setDefault("0");
//    stugovorPPOR2.setPrecision(15);
//    stugovorPPOR2.setScale(2);
//    stugovorPPOR2.setTableName("STUGOVOR");
//    stugovorPPOR2.setWidth(14);
//    stugovorPPOR2.setSqlType(2);
//    stugovorPPOR2.setServerColumnName("PPOR2");
//
//    stugovorPPOR3.setCaption("Posto poreza 3");
//    stugovorPPOR3.setColumnName("PPOR3");
//    stugovorPPOR3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    stugovorPPOR3.setDisplayMask("###,###,##0.00");
//    stugovorPPOR3.setDefault("0");
//    stugovorPPOR3.setPrecision(15);
//    stugovorPPOR3.setScale(2);
//    stugovorPPOR3.setTableName("STUGOVOR");
//    stugovorPPOR3.setWidth(14);
//    stugovorPPOR3.setSqlType(2);
//    stugovorPPOR3.setServerColumnName("PPOR3");
//
//
//    
//    stugovor.setResolver(dm.getQresolver());
//    stugovor.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from stugovor", null, true, Load.ALL));
//    setColumns(new Column[] {stugovorCUGOVOR, stugovorRBR, stugovorRBSID, stugovorCART, stugovorCART1, stugovorBC, stugovorNAZART, stugovorOZNVAL, 
//        stugovorTECAJ, stugovorJM, stugovorKOL, stugovorVAL_VC, stugovorVAL_MC, stugovorFC, stugovorUPRAB, stugovorUIRAB, stugovorFVC, stugovorUPPOR, 
//        stugovorPOR1, stugovorPOR2, stugovorPOR3, stugovorIPRODBP, stugovorFMC, stugovorIPRODSP, stugovorCSKLART, stugovorMANIPULATIVNI, stugovorIMINIZNOSBP, 
//        stugovorSTATUSMAN, stugovorSTATUSPON,stugovorSTATUSRAC,stugovorIZNOSRAC,stugovorIZNOSRACSP,stugovorPPOR1,stugovorPPOR2,stugovorPPOR3});
//  }
//
//  public void setall() {
//
//    ddl.create("stugovor")
//       .addChar("cugovor", 13, true)
//       .addShort("rbr", 4)
//       .addInteger("rbsid", 6, true)
//       .addInteger("cart", 8)
//       .addChar("cart1", 20)
//       .addChar("bc", 20)
//       .addChar("nazart", 50)
//       .addChar("oznval", 3)
//       .addFloat("tecaj", 17, 6)       
//       .addChar("jm", 3)
//       .addFloat("kol", 17, 3)
//       .addFloat("val_vc", 17, 4)
//       .addFloat("val_mc", 17, 4)
//       .addFloat("fc", 17, 2)
//       .addFloat("uprab", 8, 2)
//       .addFloat("uirab", 17, 2)
//       .addFloat("fvc", 17, 2)
//       .addFloat("uppor", 8, 2)
//       .addFloat("por1", 17, 2)
//       .addFloat("por2", 17, 2)
//       .addFloat("por3", 17, 2)
//       .addFloat("iprodbp", 17, 2)
//       .addFloat("fmc", 17, 2)
//       .addFloat("iprodsp", 17, 2)       
//       .addChar("csklart", 12)
//       .addChar("manipulativni", 50)
//       .addFloat("iminiznosbp", 17, 2)
//       .addChar("statusman", 1)
//       .addChar("statuspon", 1)
//       .addChar("statusrac", 1)
//       .addFloat("iznosrac", 17, 2)
//       .addFloat("iznosracsp", 17, 2)
//       .addFloat("ppor1", 8, 2)
//       .addFloat("ppor2", 8, 2)
//       .addFloat("ppor3", 8, 2)
//       .addPrimaryKey("cugovor,rbsid");
//
//
//    Naziv = "stugovor";
//
//    SqlDefTabela = ddl.getCreateTableString();
//
//    String[] idx = new String[] {"cugovor", "rbsid"};
//    String[] uidx = new String[] {};
//    DefIndex = ddl.getIndices(idx, uidx);
//    NaziviIdx = ddl.getIndexNames(idx, uidx);
//  }
//}
