/****license*****************************************************************
**   file: RSPeriodarh.java
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



public class RSPeriodarh extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static RSPeriodarh RSPeriodarhclass;

  QueryDataSet rsparh = new QueryDataSet();

  Column rsparhGODOBR = new Column();
  Column rsparhMJOBR = new Column();
  Column rsparhRBROBR = new Column();
  Column rsparhCRADNIK = new Column();
  Column rsparhRBR = new Column();
  Column rsparhRSOO = new Column();
  Column rsparhODDANA = new Column();
  Column rsparhDODANA = new Column();
  Column rsparhJMBG = new Column();
  Column rsparhCOPCINE = new Column();
  Column rsparhRSINV = new Column();
  Column rsparhRSB = new Column();
  Column rsparhRSZ = new Column();
  Column rsparhSATI = new Column();
  Column rsparhBRUTO = new Column();
  Column rsparhBRUTOMJ = new Column();
  Column rsparhMIO1 = new Column();
  Column rsparhMIO1MJ = new Column();
  Column rsparhMIO2 = new Column();
  Column rsparhMIO2MJ = new Column();
  Column rsparhZO = new Column();
  Column rsparhZOMJ = new Column();
  Column rsparhZAPOS = new Column();
  Column rsparhZAPOSMJ = new Column();
  Column rsparhPREMOS = new Column();
  Column rsparhOSODB = new Column();
  Column rsparhPOREZ = new Column();
  Column rsparhPOREZMJ = new Column();
  Column rsparhPRIREZ = new Column();
  Column rsparhPRIREZMJ = new Column();
  Column rsparhNETOPK = new Column();
  Column rsparhMJESEC = new Column();
  Column rsparhGODINA = new Column();
  Column rsparhIDENTIFIKATOR = new Column();
  Column rsparhVRSTAUPL = new Column();
  Column rsparhVROBV = new Column();

  public static RSPeriodarh getDataModule() {
    if (RSPeriodarhclass == null) {
      RSPeriodarhclass = new RSPeriodarh();
    }
    return RSPeriodarhclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rsparh;
  }

  public RSPeriodarh() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    rsparhGODOBR.setCaption("Godina obra\u010Duna");
    rsparhGODOBR.setColumnName("GODOBR");
    rsparhGODOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rsparhGODOBR.setPrecision(4);
    rsparhGODOBR.setRowId(true);
    rsparhGODOBR.setTableName("RSPERIODARH");
    rsparhGODOBR.setServerColumnName("GODOBR");
    rsparhGODOBR.setSqlType(5);
    rsparhGODOBR.setWidth(4);
    rsparhMJOBR.setCaption("Mjesec obra\u010Duna");
    rsparhMJOBR.setColumnName("MJOBR");
    rsparhMJOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rsparhMJOBR.setPrecision(2);
    rsparhMJOBR.setRowId(true);
    rsparhMJOBR.setTableName("RSPERIODARH");
    rsparhMJOBR.setServerColumnName("MJOBR");
    rsparhMJOBR.setSqlType(5);
    rsparhMJOBR.setWidth(2);
    rsparhRBROBR.setCaption("Redni broj obra\u010Duna");
    rsparhRBROBR.setColumnName("RBROBR");
    rsparhRBROBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rsparhRBROBR.setPrecision(3);
    rsparhRBROBR.setRowId(true);
    rsparhRBROBR.setTableName("RSPERIODARH");
    rsparhRBROBR.setServerColumnName("RBROBR");
    rsparhRBROBR.setSqlType(5);
    rsparhRBROBR.setWidth(3);
    rsparhCRADNIK.setCaption("Radnik");
    rsparhCRADNIK.setColumnName("CRADNIK");
    rsparhCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rsparhCRADNIK.setPrecision(6);
    rsparhCRADNIK.setRowId(true);
    rsparhCRADNIK.setTableName("RSPERIODARH");
    rsparhCRADNIK.setServerColumnName("CRADNIK");
    rsparhCRADNIK.setSqlType(1);
    rsparhRBR.setCaption("Rbr");
    rsparhRBR.setColumnName("RBR");
    rsparhRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    rsparhRBR.setPrecision(6);
    rsparhRBR.setRowId(true);
    rsparhRBR.setTableName("RSPERIODARH");
    rsparhRBR.setServerColumnName("RBR");
    rsparhRBR.setSqlType(4);
    rsparhRBR.setWidth(6);
    rsparhRSOO.setCaption("Osnova osiguranja");
    rsparhRSOO.setColumnName("RSOO");
    rsparhRSOO.setDataType(com.borland.dx.dataset.Variant.STRING);
    rsparhRSOO.setPrecision(5);
    rsparhRSOO.setTableName("RSPERIODARH");
    rsparhRSOO.setServerColumnName("RSOO");
    rsparhRSOO.setSqlType(1);
    rsparhODDANA.setCaption("Od");
    rsparhODDANA.setColumnName("ODDANA");
    rsparhODDANA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rsparhODDANA.setPrecision(2);
    rsparhODDANA.setTableName("RSPERIODARH");
    rsparhODDANA.setServerColumnName("ODDANA");
    rsparhODDANA.setSqlType(5);
    rsparhODDANA.setWidth(2);
    rsparhDODANA.setCaption("Do");
    rsparhDODANA.setColumnName("DODANA");
    rsparhDODANA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rsparhDODANA.setPrecision(2);
    rsparhDODANA.setTableName("RSPERIODARH");
    rsparhDODANA.setServerColumnName("DODANA");
    rsparhDODANA.setSqlType(5);
    rsparhDODANA.setWidth(2);
    rsparhJMBG.setCaption("JMBG");
    rsparhJMBG.setColumnName("JMBG");
    rsparhJMBG.setDataType(com.borland.dx.dataset.Variant.STRING);
    rsparhJMBG.setPrecision(13);
    rsparhJMBG.setTableName("RSPERIODARH");
    rsparhJMBG.setServerColumnName("JMBG");
    rsparhJMBG.setSqlType(1);
    rsparhCOPCINE.setCaption("Op\u0107ina");
    rsparhCOPCINE.setColumnName("COPCINE");
    rsparhCOPCINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    rsparhCOPCINE.setPrecision(3);
    rsparhCOPCINE.setTableName("RSPERIODARH");
    rsparhCOPCINE.setServerColumnName("COPCINE");
    rsparhCOPCINE.setSqlType(1);
    rsparhRSINV.setCaption("Invalidnost");
    rsparhRSINV.setColumnName("RSINV");
    rsparhRSINV.setDataType(com.borland.dx.dataset.Variant.STRING);
    rsparhRSINV.setPrecision(5);
    rsparhRSINV.setTableName("RSPERIODARH");
    rsparhRSINV.setServerColumnName("RSINV");
    rsparhRSINV.setSqlType(1);
    rsparhRSB.setCaption("Beneficirani staž");
    rsparhRSB.setColumnName("RSB");
    rsparhRSB.setDataType(com.borland.dx.dataset.Variant.STRING);
    rsparhRSB.setPrecision(5);
    rsparhRSB.setTableName("RSPERIODARH");
    rsparhRSB.setServerColumnName("RSB");
    rsparhRSB.setSqlType(1);
    rsparhRSZ.setCaption("Oznaka zdravstvenog osiguranja");
    rsparhRSZ.setColumnName("RSZ");
    rsparhRSZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    rsparhRSZ.setPrecision(5);
    rsparhRSZ.setTableName("RSPERIODARH");
    rsparhRSZ.setServerColumnName("RSZ");
    rsparhRSZ.setSqlType(1);
    rsparhSATI.setCaption("Sati");
    rsparhSATI.setColumnName("SATI");
    rsparhSATI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhSATI.setPrecision(17);
    rsparhSATI.setScale(2);
    rsparhSATI.setDisplayMask("###,###,##0.00");
    rsparhSATI.setDefault("0");
    rsparhSATI.setTableName("RSPERIODARH");
    rsparhSATI.setServerColumnName("SATI");
    rsparhSATI.setSqlType(2);
    rsparhSATI.setDefault("0");
    rsparhBRUTO.setCaption("Bruto upla\u0107eni");
    rsparhBRUTO.setColumnName("BRUTO");
    rsparhBRUTO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhBRUTO.setPrecision(17);
    rsparhBRUTO.setScale(2);
    rsparhBRUTO.setDisplayMask("###,###,##0.00");
    rsparhBRUTO.setDefault("0");
    rsparhBRUTO.setTableName("RSPERIODARH");
    rsparhBRUTO.setServerColumnName("BRUTO");
    rsparhBRUTO.setSqlType(2);
    rsparhBRUTO.setDefault("0");
    rsparhBRUTOMJ.setCaption("Bruto obra\u010Dunati");
    rsparhBRUTOMJ.setColumnName("BRUTOMJ");
    rsparhBRUTOMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhBRUTOMJ.setPrecision(17);
    rsparhBRUTOMJ.setScale(2);
    rsparhBRUTOMJ.setDisplayMask("###,###,##0.00");
    rsparhBRUTOMJ.setDefault("0");
    rsparhBRUTOMJ.setTableName("RSPERIODARH");
    rsparhBRUTOMJ.setServerColumnName("BRUTOMJ");
    rsparhBRUTOMJ.setSqlType(2);
    rsparhBRUTOMJ.setDefault("0");
    rsparhMIO1.setCaption("MIO 1 stup upla\u0107eno");
    rsparhMIO1.setColumnName("MIO1");
    rsparhMIO1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhMIO1.setPrecision(17);
    rsparhMIO1.setScale(2);
    rsparhMIO1.setDisplayMask("###,###,##0.00");
    rsparhMIO1.setDefault("0");
    rsparhMIO1.setTableName("RSPERIODARH");
    rsparhMIO1.setServerColumnName("MIO1");
    rsparhMIO1.setSqlType(2);
    rsparhMIO1.setDefault("0");
    rsparhMIO1MJ.setCaption("MIO 1 stup obra\u010Dunat");
    rsparhMIO1MJ.setColumnName("MIO1MJ");
    rsparhMIO1MJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhMIO1MJ.setPrecision(17);
    rsparhMIO1MJ.setScale(2);
    rsparhMIO1MJ.setDisplayMask("###,###,##0.00");
    rsparhMIO1MJ.setDefault("0");
    rsparhMIO1MJ.setTableName("RSPERIODARH");
    rsparhMIO1MJ.setServerColumnName("MIO1MJ");
    rsparhMIO1MJ.setSqlType(2);
    rsparhMIO1MJ.setDefault("0");
    rsparhMIO2.setCaption("MIO 2 stup upla\u0107en");
    rsparhMIO2.setColumnName("MIO2");
    rsparhMIO2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhMIO2.setPrecision(17);
    rsparhMIO2.setScale(2);
    rsparhMIO2.setDisplayMask("###,###,##0.00");
    rsparhMIO2.setDefault("0");
    rsparhMIO2.setTableName("RSPERIODARH");
    rsparhMIO2.setServerColumnName("MIO2");
    rsparhMIO2.setSqlType(2);
    rsparhMIO2.setDefault("0");
    rsparhMIO2MJ.setCaption("MIO 2 stup obra\u010Dunat");
    rsparhMIO2MJ.setColumnName("MIO2MJ");
    rsparhMIO2MJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhMIO2MJ.setPrecision(17);
    rsparhMIO2MJ.setScale(2);
    rsparhMIO2MJ.setDisplayMask("###,###,##0.00");
    rsparhMIO2MJ.setDefault("0");
    rsparhMIO2MJ.setTableName("RSPERIODARH");
    rsparhMIO2MJ.setServerColumnName("MIO2MJ");
    rsparhMIO2MJ.setSqlType(2);
    rsparhMIO2MJ.setDefault("0");
    rsparhZO.setCaption("Zdravstveno os. upla\u0107eno");
    rsparhZO.setColumnName("ZO");
    rsparhZO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhZO.setPrecision(17);
    rsparhZO.setScale(2);
    rsparhZO.setDisplayMask("###,###,##0.00");
    rsparhZO.setDefault("0");
    rsparhZO.setTableName("RSPERIODARH");
    rsparhZO.setServerColumnName("ZO");
    rsparhZO.setSqlType(2);
    rsparhZO.setDefault("0");
    rsparhZOMJ.setCaption("Zdravstveno os. obra\u010Dunato");
    rsparhZOMJ.setColumnName("ZOMJ");
    rsparhZOMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhZOMJ.setPrecision(17);
    rsparhZOMJ.setScale(2);
    rsparhZOMJ.setDisplayMask("###,###,##0.00");
    rsparhZOMJ.setDefault("0");
    rsparhZOMJ.setTableName("RSPERIODARH");
    rsparhZOMJ.setServerColumnName("ZOMJ");
    rsparhZOMJ.setSqlType(2);
    rsparhZOMJ.setDefault("0");
    rsparhZAPOS.setCaption("Zapošljavanje upla\u0107eno");
    rsparhZAPOS.setColumnName("ZAPOS");
    rsparhZAPOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhZAPOS.setPrecision(17);
    rsparhZAPOS.setScale(2);
    rsparhZAPOS.setDisplayMask("###,###,##0.00");
    rsparhZAPOS.setDefault("0");
    rsparhZAPOS.setTableName("RSPERIODARH");
    rsparhZAPOS.setServerColumnName("ZAPOS");
    rsparhZAPOS.setSqlType(2);
    rsparhZAPOS.setDefault("0");
    rsparhZAPOSMJ.setCaption("Zapošljavanje obra\u010Dunato");
    rsparhZAPOSMJ.setColumnName("ZAPOSMJ");
    rsparhZAPOSMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhZAPOSMJ.setPrecision(17);
    rsparhZAPOSMJ.setScale(2);
    rsparhZAPOSMJ.setDisplayMask("###,###,##0.00");
    rsparhZAPOSMJ.setDefault("0");
    rsparhZAPOSMJ.setTableName("RSPERIODARH");
    rsparhZAPOSMJ.setServerColumnName("ZAPOSMJ");
    rsparhZAPOSMJ.setSqlType(2);
    rsparhZAPOSMJ.setDefault("0");
    rsparhPREMOS.setCaption("Premije osiguranja");
    rsparhPREMOS.setColumnName("PREMOS");
    rsparhPREMOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhPREMOS.setPrecision(17);
    rsparhPREMOS.setScale(2);
    rsparhPREMOS.setDisplayMask("###,###,##0.00");
    rsparhPREMOS.setDefault("0");
    rsparhPREMOS.setTableName("RSPERIODARH");
    rsparhPREMOS.setServerColumnName("PREMOS");
    rsparhPREMOS.setSqlType(2);
    rsparhPREMOS.setDefault("0");
    rsparhOSODB.setCaption("Osobni odbitak");
    rsparhOSODB.setColumnName("OSODB");
    rsparhOSODB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhOSODB.setPrecision(17);
    rsparhOSODB.setScale(2);
    rsparhOSODB.setDisplayMask("###,###,##0.00");
    rsparhOSODB.setDefault("0");
    rsparhOSODB.setTableName("RSPERIODARH");
    rsparhOSODB.setServerColumnName("OSODB");
    rsparhOSODB.setSqlType(2);
    rsparhOSODB.setDefault("0");
    rsparhPOREZ.setCaption("Porez upla\u0107eni");
    rsparhPOREZ.setColumnName("POREZ");
    rsparhPOREZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhPOREZ.setPrecision(17);
    rsparhPOREZ.setScale(2);
    rsparhPOREZ.setDisplayMask("###,###,##0.00");
    rsparhPOREZ.setDefault("0");
    rsparhPOREZ.setTableName("RSPERIODARH");
    rsparhPOREZ.setServerColumnName("POREZ");
    rsparhPOREZ.setSqlType(2);
    rsparhPOREZ.setDefault("0");
    rsparhPOREZMJ.setCaption("Porez obra\u010Dunati");
    rsparhPOREZMJ.setColumnName("POREZMJ");
    rsparhPOREZMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhPOREZMJ.setPrecision(17);
    rsparhPOREZMJ.setScale(2);
    rsparhPOREZMJ.setDisplayMask("###,###,##0.00");
    rsparhPOREZMJ.setDefault("0");
    rsparhPOREZMJ.setTableName("RSPERIODARH");
    rsparhPOREZMJ.setServerColumnName("POREZMJ");
    rsparhPOREZMJ.setSqlType(2);
    rsparhPOREZMJ.setDefault("0");
    rsparhPRIREZ.setCaption("Prirez upla\u0107eni");
    rsparhPRIREZ.setColumnName("PRIREZ");
    rsparhPRIREZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhPRIREZ.setPrecision(17);
    rsparhPRIREZ.setScale(2);
    rsparhPRIREZ.setDisplayMask("###,###,##0.00");
    rsparhPRIREZ.setDefault("0");
    rsparhPRIREZ.setTableName("RSPERIODARH");
    rsparhPRIREZ.setServerColumnName("PRIREZ");
    rsparhPRIREZ.setSqlType(2);
    rsparhPRIREZ.setDefault("0");
    rsparhPRIREZMJ.setCaption("Prirez obra\u010Dunati");
    rsparhPRIREZMJ.setColumnName("PRIREZMJ");
    rsparhPRIREZMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhPRIREZMJ.setPrecision(17);
    rsparhPRIREZMJ.setScale(2);
    rsparhPRIREZMJ.setDisplayMask("###,###,##0.00");
    rsparhPRIREZMJ.setDefault("0");
    rsparhPRIREZMJ.setTableName("RSPERIODARH");
    rsparhPRIREZMJ.setServerColumnName("PRIREZMJ");
    rsparhPRIREZMJ.setSqlType(2);
    rsparhPRIREZMJ.setDefault("0");
    rsparhNETOPK.setCaption("Neto");
    rsparhNETOPK.setColumnName("NETOPK");
    rsparhNETOPK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rsparhNETOPK.setPrecision(17);
    rsparhNETOPK.setScale(2);
    rsparhNETOPK.setDisplayMask("###,###,##0.00");
    rsparhNETOPK.setDefault("0");
    rsparhNETOPK.setTableName("RSPERIODARH");
    rsparhNETOPK.setServerColumnName("NETOPK");
    rsparhNETOPK.setSqlType(2);
    rsparhNETOPK.setDefault("0");
    rsparhMJESEC.setCaption("Mjesec");
    rsparhMJESEC.setColumnName("MJESEC");
    rsparhMJESEC.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rsparhMJESEC.setPrecision(2);
    rsparhMJESEC.setTableName("RSPERIODARH");
    rsparhMJESEC.setServerColumnName("MJESEC");
    rsparhMJESEC.setSqlType(5);
    rsparhMJESEC.setWidth(2);
    rsparhGODINA.setCaption("Godina");
    rsparhGODINA.setColumnName("GODINA");
    rsparhGODINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rsparhGODINA.setPrecision(4);
    rsparhGODINA.setTableName("RSPERIODARH");
    rsparhGODINA.setServerColumnName("GODINA");
    rsparhGODINA.setSqlType(5);
    rsparhGODINA.setWidth(4);
    rsparhIDENTIFIKATOR.setCaption("Identifikator");
    rsparhIDENTIFIKATOR.setColumnName("IDENTIFIKATOR");
    rsparhIDENTIFIKATOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    rsparhIDENTIFIKATOR.setPrecision(4);
    rsparhIDENTIFIKATOR.setTableName("RSPERIODARH");
    rsparhIDENTIFIKATOR.setServerColumnName("IDENTIFIKATOR");
    rsparhIDENTIFIKATOR.setSqlType(1);
    rsparhVRSTAUPL.setCaption("Vrsta uplate");
    rsparhVRSTAUPL.setColumnName("VRSTAUPL");
    rsparhVRSTAUPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    rsparhVRSTAUPL.setPrecision(2);
    rsparhVRSTAUPL.setTableName("RSPERIODARH");
    rsparhVRSTAUPL.setServerColumnName("VRSTAUPL");
    rsparhVRSTAUPL.setSqlType(1);
    rsparhVROBV.setCaption("Vrsta obveznika");
    rsparhVROBV.setColumnName("VROBV");
    rsparhVROBV.setDataType(com.borland.dx.dataset.Variant.STRING);
    rsparhVROBV.setPrecision(5);
    rsparhVROBV.setTableName("RSPERIODARH");
    rsparhVROBV.setServerColumnName("VROBV");
    rsparhVROBV.setSqlType(1);
    rsparh.setResolver(dm.getQresolver());
    rsparh.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from RSPeriodarh", null, true, Load.ALL));
 setColumns(new Column[] {rsparhGODOBR, rsparhMJOBR, rsparhRBROBR, rsparhCRADNIK, rsparhRBR, rsparhRSOO, rsparhODDANA, rsparhDODANA, rsparhJMBG,
        rsparhCOPCINE, rsparhRSINV, rsparhRSB, rsparhRSZ, rsparhSATI, rsparhBRUTO, rsparhBRUTOMJ, rsparhMIO1, rsparhMIO1MJ, rsparhMIO2, rsparhMIO2MJ, rsparhZO,
        rsparhZOMJ, rsparhZAPOS, rsparhZAPOSMJ, rsparhPREMOS, rsparhOSODB, rsparhPOREZ, rsparhPOREZMJ, rsparhPRIREZ, rsparhPRIREZMJ, rsparhNETOPK, rsparhMJESEC,
        rsparhGODINA, rsparhIDENTIFIKATOR, rsparhVRSTAUPL, rsparhVROBV});
  }

  public void setall() {

    ddl.create("RSPeriodarh")
       .addShort("godobr", 4, true)
       .addShort("mjobr", 2, true)
       .addShort("rbrobr", 3, true)
       .addChar("cradnik", 6, true)
       .addInteger("rbr", 6, true)
       .addChar("rsoo", 5)
       .addShort("oddana", 2)
       .addShort("dodana", 2)
       .addChar("jmbg", 13)
       .addChar("copcine", 3)
       .addChar("rsinv", 5)
       .addChar("rsb", 5)
       .addChar("rsz", 5)
       .addFloat("sati", 17, 2)
       .addFloat("bruto", 17, 2)
       .addFloat("brutomj", 17, 2)
       .addFloat("mio1", 17, 2)
       .addFloat("mio1mj", 17, 2)
       .addFloat("mio2", 17, 2)
       .addFloat("mio2mj", 17, 2)
       .addFloat("zo", 17, 2)
       .addFloat("zomj", 17, 2)
       .addFloat("zapos", 17, 2)
       .addFloat("zaposmj", 17, 2)
       .addFloat("premos", 17, 2)
       .addFloat("osodb", 17, 2)
       .addFloat("porez", 17, 2)
       .addFloat("porezmj", 17, 2)
       .addFloat("prirez", 17, 2)
       .addFloat("prirezmj", 17, 2)
       .addFloat("netopk", 17, 2)
       .addShort("mjesec", 2)
       .addShort("godina", 4)
       .addChar("identifikator", 4)
       .addChar("vrstaupl", 2)
       .addChar("vrobv", 5)
       .addPrimaryKey("godobr,mjobr,rbrobr,cradnik,rbr");


    Naziv = "RSPeriodarh";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"mjobr", "rbrobr", "cradnik", "rbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
