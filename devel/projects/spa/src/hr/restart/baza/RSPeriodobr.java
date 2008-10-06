/****license*****************************************************************
**   file: RSPeriodobr.java
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



public class RSPeriodobr extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static RSPeriodobr RSPeriodobrclass;

  QueryDataSet rspobr = new QueryDataSet();

  Column rspobrCRADNIK = new Column();
  Column rspobrRBR = new Column();
  Column rspobrRSOO = new Column();
  Column rspobrODDANA = new Column();
  Column rspobrDODANA = new Column();
  Column rspobrJMBG = new Column();
  Column rspobrCOPCINE = new Column();
  Column rspobrRSINV = new Column();
  Column rspobrRSB = new Column();
  Column rspobrRSZ = new Column();
  Column rspobrSATI = new Column();
  Column rspobrBRUTO = new Column();
  Column rspobrBRUTOMJ = new Column();
  Column rspobrMIO1 = new Column();
  Column rspobrMIO1MJ = new Column();
  Column rspobrMIO2 = new Column();
  Column rspobrMIO2MJ = new Column();
  Column rspobrZO = new Column();
  Column rspobrZOMJ = new Column();
  Column rspobrZAPOS = new Column();
  Column rspobrZAPOSMJ = new Column();
  Column rspobrPREMOS = new Column();
  Column rspobrOSODB = new Column();
  Column rspobrPOREZ = new Column();
  Column rspobrPOREZMJ = new Column();
  Column rspobrPRIREZ = new Column();
  Column rspobrPRIREZMJ = new Column();
  Column rspobrNETOPK = new Column();
  Column rspobrMJESEC = new Column();
  Column rspobrGODINA = new Column();
  Column rspobrIDENTIFIKATOR = new Column();
  Column rspobrVRSTAUPL = new Column();
  Column rspobrVROBV = new Column();

  public static RSPeriodobr getDataModule() {
    if (RSPeriodobrclass == null) {
      RSPeriodobrclass = new RSPeriodobr();
    }
    return RSPeriodobrclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rspobr;
  }

  public RSPeriodobr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    rspobrCRADNIK.setCaption("Radnik");
    rspobrCRADNIK.setColumnName("CRADNIK");
    rspobrCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspobrCRADNIK.setPrecision(6);
    rspobrCRADNIK.setRowId(true);
    rspobrCRADNIK.setTableName("RSPERIODOBR");
    rspobrCRADNIK.setServerColumnName("CRADNIK");
    rspobrCRADNIK.setSqlType(1);
    rspobrRBR.setCaption("Rbr");
    rspobrRBR.setColumnName("RBR");
    rspobrRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    rspobrRBR.setPrecision(6);
    rspobrRBR.setRowId(true);
    rspobrRBR.setTableName("RSPERIODOBR");
    rspobrRBR.setServerColumnName("RBR");
    rspobrRBR.setSqlType(4);
    rspobrRBR.setWidth(6);
    rspobrRSOO.setCaption("Osnova osiguranja");
    rspobrRSOO.setColumnName("RSOO");
    rspobrRSOO.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspobrRSOO.setPrecision(5);
    rspobrRSOO.setTableName("RSPERIODOBR");
    rspobrRSOO.setServerColumnName("RSOO");
    rspobrRSOO.setSqlType(1);
    rspobrODDANA.setCaption("Od");
    rspobrODDANA.setColumnName("ODDANA");
    rspobrODDANA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rspobrODDANA.setPrecision(2);
    rspobrODDANA.setTableName("RSPERIODOBR");
    rspobrODDANA.setServerColumnName("ODDANA");
    rspobrODDANA.setSqlType(5);
    rspobrODDANA.setWidth(2);
    rspobrDODANA.setCaption("Do");
    rspobrDODANA.setColumnName("DODANA");
    rspobrDODANA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rspobrDODANA.setPrecision(2);
    rspobrDODANA.setTableName("RSPERIODOBR");
    rspobrDODANA.setServerColumnName("DODANA");
    rspobrDODANA.setSqlType(5);
    rspobrDODANA.setWidth(2);
    rspobrJMBG.setCaption("JMBG");
    rspobrJMBG.setColumnName("JMBG");
    rspobrJMBG.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspobrJMBG.setPrecision(13);
    rspobrJMBG.setTableName("RSPERIODOBR");
    rspobrJMBG.setServerColumnName("JMBG");
    rspobrJMBG.setSqlType(1);
    rspobrCOPCINE.setCaption("Op\u0107ina");
    rspobrCOPCINE.setColumnName("COPCINE");
    rspobrCOPCINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspobrCOPCINE.setPrecision(3);
    rspobrCOPCINE.setTableName("RSPERIODOBR");
    rspobrCOPCINE.setServerColumnName("COPCINE");
    rspobrCOPCINE.setSqlType(1);
    rspobrRSINV.setCaption("Invalidnost");
    rspobrRSINV.setColumnName("RSINV");
    rspobrRSINV.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspobrRSINV.setPrecision(5);
    rspobrRSINV.setTableName("RSPERIODOBR");
    rspobrRSINV.setServerColumnName("RSINV");
    rspobrRSINV.setSqlType(1);
    rspobrRSB.setCaption("Beneficirani staž");
    rspobrRSB.setColumnName("RSB");
    rspobrRSB.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspobrRSB.setPrecision(5);
    rspobrRSB.setTableName("RSPERIODOBR");
    rspobrRSB.setServerColumnName("RSB");
    rspobrRSB.setSqlType(1);
    rspobrRSZ.setCaption("Oznaka zdravstvenog osiguranja");
    rspobrRSZ.setColumnName("RSZ");
    rspobrRSZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspobrRSZ.setPrecision(5);
    rspobrRSZ.setTableName("RSPERIODOBR");
    rspobrRSZ.setServerColumnName("RSZ");
    rspobrRSZ.setSqlType(1);
    rspobrSATI.setCaption("Sati");
    rspobrSATI.setColumnName("SATI");
    rspobrSATI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrSATI.setPrecision(17);
    rspobrSATI.setScale(2);
    rspobrSATI.setDisplayMask("###,###,##0.00");
    rspobrSATI.setDefault("0");
    rspobrSATI.setTableName("RSPERIODOBR");
    rspobrSATI.setServerColumnName("SATI");
    rspobrSATI.setSqlType(2);
    rspobrSATI.setDefault("0");
    rspobrBRUTO.setCaption("Bruto upla\u0107eni");
    rspobrBRUTO.setColumnName("BRUTO");
    rspobrBRUTO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrBRUTO.setPrecision(17);
    rspobrBRUTO.setScale(2);
    rspobrBRUTO.setDisplayMask("###,###,##0.00");
    rspobrBRUTO.setDefault("0");
    rspobrBRUTO.setTableName("RSPERIODOBR");
    rspobrBRUTO.setServerColumnName("BRUTO");
    rspobrBRUTO.setSqlType(2);
    rspobrBRUTO.setDefault("0");
    rspobrBRUTOMJ.setCaption("Bruto obra\u010Dunati");
    rspobrBRUTOMJ.setColumnName("BRUTOMJ");
    rspobrBRUTOMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrBRUTOMJ.setPrecision(17);
    rspobrBRUTOMJ.setScale(2);
    rspobrBRUTOMJ.setDisplayMask("###,###,##0.00");
    rspobrBRUTOMJ.setDefault("0");
    rspobrBRUTOMJ.setTableName("RSPERIODOBR");
    rspobrBRUTOMJ.setServerColumnName("BRUTOMJ");
    rspobrBRUTOMJ.setSqlType(2);
    rspobrBRUTOMJ.setDefault("0");
    rspobrMIO1.setCaption("MIO 1 stup upla\u0107eno");
    rspobrMIO1.setColumnName("MIO1");
    rspobrMIO1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrMIO1.setPrecision(17);
    rspobrMIO1.setScale(2);
    rspobrMIO1.setDisplayMask("###,###,##0.00");
    rspobrMIO1.setDefault("0");
    rspobrMIO1.setTableName("RSPERIODOBR");
    rspobrMIO1.setServerColumnName("MIO1");
    rspobrMIO1.setSqlType(2);
    rspobrMIO1.setDefault("0");
    rspobrMIO1MJ.setCaption("MIO 1 stup obra\u010Dunat");
    rspobrMIO1MJ.setColumnName("MIO1MJ");
    rspobrMIO1MJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrMIO1MJ.setPrecision(17);
    rspobrMIO1MJ.setScale(2);
    rspobrMIO1MJ.setDisplayMask("###,###,##0.00");
    rspobrMIO1MJ.setDefault("0");
    rspobrMIO1MJ.setTableName("RSPERIODOBR");
    rspobrMIO1MJ.setServerColumnName("MIO1MJ");
    rspobrMIO1MJ.setSqlType(2);
    rspobrMIO1MJ.setDefault("0");
    rspobrMIO2.setCaption("MIO 2 stup upla\u0107en");
    rspobrMIO2.setColumnName("MIO2");
    rspobrMIO2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrMIO2.setPrecision(17);
    rspobrMIO2.setScale(2);
    rspobrMIO2.setDisplayMask("###,###,##0.00");
    rspobrMIO2.setDefault("0");
    rspobrMIO2.setTableName("RSPERIODOBR");
    rspobrMIO2.setServerColumnName("MIO2");
    rspobrMIO2.setSqlType(2);
    rspobrMIO2.setDefault("0");
    rspobrMIO2MJ.setCaption("MIO 2 stup obra\u010Dunat");
    rspobrMIO2MJ.setColumnName("MIO2MJ");
    rspobrMIO2MJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrMIO2MJ.setPrecision(17);
    rspobrMIO2MJ.setScale(2);
    rspobrMIO2MJ.setDisplayMask("###,###,##0.00");
    rspobrMIO2MJ.setDefault("0");
    rspobrMIO2MJ.setTableName("RSPERIODOBR");
    rspobrMIO2MJ.setServerColumnName("MIO2MJ");
    rspobrMIO2MJ.setSqlType(2);
    rspobrMIO2MJ.setDefault("0");
    rspobrZO.setCaption("Zdravstveno os. upla\u0107eno");
    rspobrZO.setColumnName("ZO");
    rspobrZO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrZO.setPrecision(17);
    rspobrZO.setScale(2);
    rspobrZO.setDisplayMask("###,###,##0.00");
    rspobrZO.setDefault("0");
    rspobrZO.setTableName("RSPERIODOBR");
    rspobrZO.setServerColumnName("ZO");
    rspobrZO.setSqlType(2);
    rspobrZO.setDefault("0");
    rspobrZOMJ.setCaption("Zdravstveno os. obra\u010Dunato");
    rspobrZOMJ.setColumnName("ZOMJ");
    rspobrZOMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrZOMJ.setPrecision(17);
    rspobrZOMJ.setScale(2);
    rspobrZOMJ.setDisplayMask("###,###,##0.00");
    rspobrZOMJ.setDefault("0");
    rspobrZOMJ.setTableName("RSPERIODOBR");
    rspobrZOMJ.setServerColumnName("ZOMJ");
    rspobrZOMJ.setSqlType(2);
    rspobrZOMJ.setDefault("0");
    rspobrZAPOS.setCaption("Zapošljavanje upla\u0107eno");
    rspobrZAPOS.setColumnName("ZAPOS");
    rspobrZAPOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrZAPOS.setPrecision(17);
    rspobrZAPOS.setScale(2);
    rspobrZAPOS.setDisplayMask("###,###,##0.00");
    rspobrZAPOS.setDefault("0");
    rspobrZAPOS.setTableName("RSPERIODOBR");
    rspobrZAPOS.setServerColumnName("ZAPOS");
    rspobrZAPOS.setSqlType(2);
    rspobrZAPOS.setDefault("0");
    rspobrZAPOSMJ.setCaption("Zapošljavanje obra\u010Dunato");
    rspobrZAPOSMJ.setColumnName("ZAPOSMJ");
    rspobrZAPOSMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrZAPOSMJ.setPrecision(17);
    rspobrZAPOSMJ.setScale(2);
    rspobrZAPOSMJ.setDisplayMask("###,###,##0.00");
    rspobrZAPOSMJ.setDefault("0");
    rspobrZAPOSMJ.setTableName("RSPERIODOBR");
    rspobrZAPOSMJ.setServerColumnName("ZAPOSMJ");
    rspobrZAPOSMJ.setSqlType(2);
    rspobrZAPOSMJ.setDefault("0");
    rspobrPREMOS.setCaption("Premije osiguranja");
    rspobrPREMOS.setColumnName("PREMOS");
    rspobrPREMOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrPREMOS.setPrecision(17);
    rspobrPREMOS.setScale(2);
    rspobrPREMOS.setDisplayMask("###,###,##0.00");
    rspobrPREMOS.setDefault("0");
    rspobrPREMOS.setTableName("RSPERIODOBR");
    rspobrPREMOS.setServerColumnName("PREMOS");
    rspobrPREMOS.setSqlType(2);
    rspobrPREMOS.setDefault("0");
    rspobrOSODB.setCaption("Osobni odbitak");
    rspobrOSODB.setColumnName("OSODB");
    rspobrOSODB.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrOSODB.setPrecision(17);
    rspobrOSODB.setScale(2);
    rspobrOSODB.setDisplayMask("###,###,##0.00");
    rspobrOSODB.setDefault("0");
    rspobrOSODB.setTableName("RSPERIODOBR");
    rspobrOSODB.setServerColumnName("OSODB");
    rspobrOSODB.setSqlType(2);
    rspobrOSODB.setDefault("0");
    rspobrPOREZ.setCaption("Porez upla\u0107eni");
    rspobrPOREZ.setColumnName("POREZ");
    rspobrPOREZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrPOREZ.setPrecision(17);
    rspobrPOREZ.setScale(2);
    rspobrPOREZ.setDisplayMask("###,###,##0.00");
    rspobrPOREZ.setDefault("0");
    rspobrPOREZ.setTableName("RSPERIODOBR");
    rspobrPOREZ.setServerColumnName("POREZ");
    rspobrPOREZ.setSqlType(2);
    rspobrPOREZ.setDefault("0");
    rspobrPOREZMJ.setCaption("Porez obra\u010Dunati");
    rspobrPOREZMJ.setColumnName("POREZMJ");
    rspobrPOREZMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrPOREZMJ.setPrecision(17);
    rspobrPOREZMJ.setScale(2);
    rspobrPOREZMJ.setDisplayMask("###,###,##0.00");
    rspobrPOREZMJ.setDefault("0");
    rspobrPOREZMJ.setTableName("RSPERIODOBR");
    rspobrPOREZMJ.setServerColumnName("POREZMJ");
    rspobrPOREZMJ.setSqlType(2);
    rspobrPOREZMJ.setDefault("0");
    rspobrPRIREZ.setCaption("Prirez upla\u0107eni");
    rspobrPRIREZ.setColumnName("PRIREZ");
    rspobrPRIREZ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrPRIREZ.setPrecision(17);
    rspobrPRIREZ.setScale(2);
    rspobrPRIREZ.setDisplayMask("###,###,##0.00");
    rspobrPRIREZ.setDefault("0");
    rspobrPRIREZ.setTableName("RSPERIODOBR");
    rspobrPRIREZ.setServerColumnName("PRIREZ");
    rspobrPRIREZ.setSqlType(2);
    rspobrPRIREZ.setDefault("0");
    rspobrPRIREZMJ.setCaption("Prirez obra\u010Dunati");
    rspobrPRIREZMJ.setColumnName("PRIREZMJ");
    rspobrPRIREZMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrPRIREZMJ.setPrecision(17);
    rspobrPRIREZMJ.setScale(2);
    rspobrPRIREZMJ.setDisplayMask("###,###,##0.00");
    rspobrPRIREZMJ.setDefault("0");
    rspobrPRIREZMJ.setTableName("RSPERIODOBR");
    rspobrPRIREZMJ.setServerColumnName("PRIREZMJ");
    rspobrPRIREZMJ.setSqlType(2);
    rspobrPRIREZMJ.setDefault("0");
    rspobrNETOPK.setCaption("Neto");
    rspobrNETOPK.setColumnName("NETOPK");
    rspobrNETOPK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    rspobrNETOPK.setPrecision(17);
    rspobrNETOPK.setScale(2);
    rspobrNETOPK.setDisplayMask("###,###,##0.00");
    rspobrNETOPK.setDefault("0");
    rspobrNETOPK.setTableName("RSPERIODOBR");
    rspobrNETOPK.setServerColumnName("NETOPK");
    rspobrNETOPK.setSqlType(2);
    rspobrNETOPK.setDefault("0");
    rspobrMJESEC.setCaption("Mjesec");
    rspobrMJESEC.setColumnName("MJESEC");
    rspobrMJESEC.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rspobrMJESEC.setPrecision(2);
    rspobrMJESEC.setTableName("RSPERIODOBR");
    rspobrMJESEC.setServerColumnName("MJESEC");
    rspobrMJESEC.setSqlType(5);
    rspobrMJESEC.setWidth(2);
    rspobrGODINA.setCaption("Godina");
    rspobrGODINA.setColumnName("GODINA");
    rspobrGODINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rspobrGODINA.setPrecision(4);
    rspobrGODINA.setTableName("RSPERIODOBR");
    rspobrGODINA.setServerColumnName("GODINA");
    rspobrGODINA.setSqlType(5);
    rspobrGODINA.setWidth(4);
    rspobrIDENTIFIKATOR.setCaption("Identifikator");
    rspobrIDENTIFIKATOR.setColumnName("IDENTIFIKATOR");
    rspobrIDENTIFIKATOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspobrIDENTIFIKATOR.setPrecision(4);
    rspobrIDENTIFIKATOR.setTableName("RSPERIODOBR");
    rspobrIDENTIFIKATOR.setServerColumnName("IDENTIFIKATOR");
    rspobrIDENTIFIKATOR.setSqlType(1);
    rspobrVRSTAUPL.setCaption("Vrsta uplate");
    rspobrVRSTAUPL.setColumnName("VRSTAUPL");
    rspobrVRSTAUPL.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspobrVRSTAUPL.setPrecision(2);
    rspobrVRSTAUPL.setTableName("RSPERIODOBR");
    rspobrVRSTAUPL.setServerColumnName("VRSTAUPL");
    rspobrVRSTAUPL.setSqlType(1);
    rspobrVROBV.setCaption("Vrsta obveznika");
    rspobrVROBV.setColumnName("VROBV");
    rspobrVROBV.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspobrVROBV.setPrecision(5);
    rspobrVROBV.setTableName("RSPERIODOBR");
    rspobrVROBV.setServerColumnName("VROBV");
    rspobrVROBV.setSqlType(1);
    rspobr.setResolver(dm.getQresolver());
    rspobr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from RSPeriodobr", null, true, Load.ALL));
 setColumns(new Column[] {rspobrCRADNIK, rspobrRBR, rspobrRSOO, rspobrODDANA, rspobrDODANA, rspobrJMBG, rspobrCOPCINE, rspobrRSINV, rspobrRSB,
        rspobrRSZ, rspobrSATI, rspobrBRUTO, rspobrBRUTOMJ, rspobrMIO1, rspobrMIO1MJ, rspobrMIO2, rspobrMIO2MJ, rspobrZO, rspobrZOMJ, rspobrZAPOS, rspobrZAPOSMJ,
        rspobrPREMOS, rspobrOSODB, rspobrPOREZ, rspobrPOREZMJ, rspobrPRIREZ, rspobrPRIREZMJ, rspobrNETOPK, rspobrMJESEC, rspobrGODINA, rspobrIDENTIFIKATOR,
        rspobrVRSTAUPL, rspobrVROBV});
  }

  public void setall() {

    ddl.create("RSPeriodobr")
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
       .addPrimaryKey("cradnik,rbr");


    Naziv = "RSPeriodobr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cradnik", "rbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
