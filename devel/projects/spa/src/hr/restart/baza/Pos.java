/****license*****************************************************************
**   file: Pos.java
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
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class Pos extends KreirDrop implements DataModule {

  private static Pos Posclass;
  dM dm  = dM.getDataModule();
  QueryDataSet Pos = new raDataSet();
  /*Column posLOKK = new Column();
  Column posAKTIV = new Column();
  Column posCSKL = new Column();
  Column posVRDOK = new Column();
  Column posGOD = new Column();
  Column posBRDOK = new Column();
  Column posSYSDAT = new Column();
  Column posDATDOK = new Column();
  Column posCPAR = new Column();
  Column posCPRODMJ = new Column();
  Column posUKUPNO = new Column();
  Column posIZNOS = new Column();
  Column posNETO = new Column();
  Column posUPPOPUST2 = new Column();
  Column posUIPOPUST1 = new Column();
  Column posUIPOPUST2 = new Column();
  Column posUPOREZ = new Column();
  Column posUIRAC = new Column();
  Column posBRRATA = new Column();
  Column posSTATUS = new Column();
  Column posCBLAGAJNIK = new Column();

//  Column posPPOPUST = new Column();

  Column posCNACPL = new Column();
  Column posCUSER = new Column();
  Column posCKUPAC = new Column();*/

  public static Pos getDataModule() {
    if (Posclass == null) {
      Posclass = new Pos();
    }
    return Posclass;
  }
  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return Pos;
  }

  public Pos() {
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
    /*posCKUPAC.setCaption("Kupac");
    posCKUPAC.setColumnName("CKUPAC");
    posCKUPAC.setDataType(com.borland.dx.dataset.Variant.INT);
    posCKUPAC.setPrecision(6);
    posCKUPAC.setTableName("POS");
    posCKUPAC.setWidth(5);
    posCKUPAC.setServerColumnName("CKUPAC");
    posCKUPAC.setSqlType(4);

    posBRRATA.setCaption("Broj rata");
    posBRRATA.setColumnName("BRRATA");
    posBRRATA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    posBRRATA.setDefault("1");
    posBRRATA.setTableName("POS");
    posBRRATA.setServerColumnName("BRRATA");
    posBRRATA.setSqlType(5);
    posCUSER.setCaption("Operater");
    posCUSER.setColumnName("CUSER");
    posCUSER.setDataType(com.borland.dx.dataset.Variant.STRING);
    posCUSER.setPrecision(15);
    posCUSER.setTableName("POS");
    posCUSER.setServerColumnName("CUSER");
    posCUSER.setSqlType(1);
    posUIPOPUST2.setCaption("Popust na dokumentu");
    posUIPOPUST2.setColumnName("UIPOPUST2");
    posUIPOPUST2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    posUIPOPUST2.setDisplayMask("###,###,##0.00");
    posUIPOPUST2.setDefault("0");
    posUIPOPUST2.setPrecision(15);
    posUIPOPUST2.setScale(2);
    posUIPOPUST2.setTableName("POS");
    posUIPOPUST2.setServerColumnName("UIPOPUST2");
    posUIPOPUST2.setSqlType(2);
    posUIPOPUST1.setCaption("Popust na stavkama");
    posUIPOPUST1.setColumnName("UIPOPUST1");
    posUIPOPUST1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    posUIPOPUST1.setDisplayMask("###,###,##0.00");
    posUIPOPUST1.setDefault("0");
    posUIPOPUST1.setPrecision(15);
    posUIPOPUST1.setScale(2);
    posUIPOPUST1.setTableName("POS");
    posUIPOPUST1.setServerColumnName("UIPOPUST1");
    posUIPOPUST1.setSqlType(2);
    posUPPOPUST2.setCaption("Posto popusta");
    posUPPOPUST2.setColumnName("UPPOPUST2");
    posUPPOPUST2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    posUPPOPUST2.setDisplayMask("###,###,##0.00");
    posUPPOPUST2.setDefault("0");
    posUPPOPUST2.setPrecision(10);
    posUPPOPUST2.setScale(2);
    posUPPOPUST2.setTableName("POS");
    posUPPOPUST2.setServerColumnName("UPPOPUST2");
    posUPPOPUST2.setSqlType(2);
    posCPRODMJ.setCaption("Blagajna");
    posCPRODMJ.setColumnName("CPRODMJ");
    posCPRODMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    posCPRODMJ.setPrecision(3);
    posCPRODMJ.setRowId(true);
    posCPRODMJ.setTableName("POS");
    posCPRODMJ.setWidth(5);
    posCPRODMJ.setServerColumnName("CPRODMJ");
    posCPRODMJ.setSqlType(1);
    posCNACPL.setCaption("Na\\u10din pla\u0107anja");
    posCNACPL.setColumnName("CNACPL");
    posCNACPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    posCNACPL.setPrecision(3);
    posCNACPL.setTableName("POS");
    posCNACPL.setServerColumnName("CNACPL");
    posCNACPL.setSqlType(1);
    posUPOREZ.setCaption("Ukupni porez");
    posUPOREZ.setColumnName("UPOREZ");
    posUPOREZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    posUPOREZ.setDisplayMask("###,###,##0.00");
    posUPOREZ.setDefault("0");
    posUPOREZ.setPrecision(15);
    posUPOREZ.setScale(2);
    posUPOREZ.setTableName("POS");
    posUPOREZ.setServerColumnName("UPOREZ");
    posUPOREZ.setSqlType(2);
    posCPAR.setCaption("Partner");
    posCPAR.setColumnName("CPAR");
    posCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    posCPAR.setTableName("POS");
    posCPAR.setWidth(7);
    posCPAR.setServerColumnName("CPAR");
    posCPAR.setSqlType(4);
    posDATDOK.setCaption("Datum");
    posDATDOK.setColumnName("DATDOK");
    posDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    posDATDOK.setDisplayMask("dd-MM-yyyy");
//    posDATDOK.setEditMask("dd-MM-yyyy");
    posDATDOK.setTableName("POS");
    posDATDOK.setWidth(10);
    posDATDOK.setServerColumnName("DATDOK");
    posDATDOK.setSqlType(93);
    posSYSDAT.setCaption("Sysdat");
    posSYSDAT.setColumnName("SYSDAT");
    posSYSDAT.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    posSYSDAT.setDisplayMask("dd-MM-yyyy");
    posSYSDAT.setEditMask("dd-MM-yyyy");
    posSYSDAT.setTableName("POS");
    posSYSDAT.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    posSYSDAT.setServerColumnName("SYSDAT");
    posSYSDAT.setSqlType(93);
    posBRDOK.setCaption("Broj");
    posBRDOK.setColumnName("BRDOK");
    posBRDOK.setDataType(com.borland.dx.dataset.Variant.INT);
    posBRDOK.setRowId(true);
    posBRDOK.setTableName("POS");
    posBRDOK.setWidth(7);
    posBRDOK.setServerColumnName("BRDOK");
    posBRDOK.setSqlType(4);
    posGOD.setCaption("Godina");
    posGOD.setColumnName("GOD");
    posGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    posGOD.setPrecision(4);
    posGOD.setRowId(true);
    posGOD.setTableName("POS");
    posGOD.setServerColumnName("GOD");
    posGOD.setSqlType(1);
    posVRDOK.setCaption("Vrsta");
    posVRDOK.setColumnName("VRDOK");
    posVRDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    posVRDOK.setPrecision(3);
    posVRDOK.setRowId(true);
    posVRDOK.setTableName("POS");
    posVRDOK.setServerColumnName("VRDOK");
    posVRDOK.setSqlType(1);
    posCSKL.setCaption("Skladište");
    posCSKL.setColumnName("CSKL");
    posCSKL.setDataType(com.borland.dx.dataset.Variant.STRING);
    posCSKL.setPrecision(12);
    posCSKL.setRowId(true);
    posCSKL.setTableName("POS");
    posCSKL.setWidth(4);
    posCSKL.setServerColumnName("CSKL");
    posCSKL.setSqlType(1);
    posSTATUS.setColumnName("STATUS");
    posSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    posSTATUS.setDefault("N");
    posSTATUS.setPrecision(1);
    posSTATUS.setTableName("POS");
    posSTATUS.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    posSTATUS.setSqlType(1);
    posSTATUS.setServerColumnName("STATUS");
    posCBLAGAJNIK.setCaption("Blagajnik");
    posCBLAGAJNIK.setColumnName("CBLAGAJNIK");
    posCBLAGAJNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    posCBLAGAJNIK.setPrecision(3);
    posCBLAGAJNIK.setTableName("POS");
    posCBLAGAJNIK.setServerColumnName("CBLAGAJNIK");
    posCBLAGAJNIK.setSqlType(1);
    posUIRAC.setCaption("Platiti");
    posUIRAC.setColumnName("UIRAC");
    posUIRAC.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    posUIRAC.setDisplayMask("###,###,##0.00");
    posUIRAC.setDefault("0");
    posUIRAC.setPrecision(15);
    posUIRAC.setScale(2);
    posUIRAC.setTableName("POS");
    posUIRAC.setSqlType(2);
    posUIRAC.setServerColumnName("UIRAC");
//    posPPOPUST.setColumnName("PPOPUST");
//    posPPOPUST.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    posPPOPUST.setDisplayMask("###,###,##0.00");
//    posPPOPUST.setDefault("0");
//    posPPOPUST.setPrecision(10);
//    posPPOPUST.setScale(2);
//    posPPOPUST.setTableName("POS");
//    posPPOPUST.setSqlType(2);
//    posPPOPUST.setServerColumnName("PPOPUST");
    posUKUPNO.setCaption("Ukupno");
    posUKUPNO.setColumnName("UKUPNO");
    posUKUPNO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    posUKUPNO.setDisplayMask("###,###,##0.00");
    posUKUPNO.setDefault("0");
    posUKUPNO.setPrecision(15);
    posUKUPNO.setScale(2);
    posUKUPNO.setTableName("POS");
    posUKUPNO.setSqlType(2);
    posUKUPNO.setServerColumnName("UKUPNO");

    posIZNOS.setCaption("Iznos");
    posIZNOS.setColumnName("IZNOS");
    posIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    posIZNOS.setDisplayMask("###,###,##0.00");
    posIZNOS.setDefault("0");
    posIZNOS.setPrecision(15);
    posIZNOS.setScale(2);
    posIZNOS.setTableName("POS");
    posIZNOS.setSqlType(2);
    posIZNOS.setServerColumnName("IZNOS");

    posNETO.setCaption("Neto");
    posNETO.setColumnName("NETO");
    posNETO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    posNETO.setDisplayMask("###,###,##0.00");
    posNETO.setDefault("0");
    posNETO.setPrecision(15);
    posNETO.setScale(2);
    posNETO.setTableName("POS");
    posNETO.setSqlType(2);
    posNETO.setServerColumnName("NETO");

    posAKTIV.setColumnName("AKTIV");
    posAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    posAKTIV.setDefault("D");
    posAKTIV.setPrecision(1);
    posAKTIV.setTableName("POS");
    posAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    posAKTIV.setSqlType(1);
    posAKTIV.setServerColumnName("AKTIV");
    posLOKK.setColumnName("LOKK");
    posLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    posLOKK.setDefault("N");
    posLOKK.setPrecision(1);
    posLOKK.setTableName("POS");
    posLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    posLOKK.setSqlType(1);
    posLOKK.setServerColumnName("LOKK");

    Pos.setResolver(dm.getQresolver());
    Pos.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from Pos", null, true, Load.ALL));
 setColumns(new Column[] {posLOKK, posAKTIV, posCSKL, posVRDOK, posGOD, posBRDOK, posSYSDAT, posDATDOK, posCPAR, posCPRODMJ,
            posUKUPNO, posIZNOS, posNETO, posUPPOPUST2, posUIPOPUST1, posUIPOPUST2, posUPOREZ, posUIRAC, posBRRATA,
            posSTATUS, posCNACPL, posCUSER, posCKUPAC,posCBLAGAJNIK});*/

   createFilteredDataSet(Pos,"1=0");
  }
/* public void setall(){


    ddl.create("pos")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cskl", 12, true)
       .addChar("vrdok", 3, true)
       .addChar("god", 4, true)
       .addInteger("brdok", 6, true)
       .addDate("sysdat")
       .addDate("datdok")
       .addInteger("cpar", 6)
       .addChar("cprodmj", 3, true)
       .addFloat("ukupno", 17, 2)
       .addFloat("iznos", 17, 2)
       .addFloat("neto", 17, 2)
       .addFloat("uppopust2", 6, 2)
       .addFloat("uipopust1", 17, 2)
       .addFloat("uipopust2", 17, 2)
       .addFloat("uporez", 17, 2)
       .addFloat("uirac", 17, 2)
       .addShort("brrata", 3)
       .addChar("status", 1)
       .addChar("cnacpl", 3)
       .addChar("cuser", 15)
       .addInteger("ckupac", 6)
       .addChar("cblagajnik", 3)
       .addPrimaryKey("cskl,vrdok,god,brdok,cprodmj");

    Naziv="Pos";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"brdok"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);

  }*/
}
