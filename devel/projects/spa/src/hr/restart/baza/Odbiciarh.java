/****license*****************************************************************
**   file: Odbiciarh.java
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



public class Odbiciarh extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Odbiciarh Odbiciarhclass;

  QueryDataSet odarh = new QueryDataSet();

  Column odarhLOKK = new Column();
  Column odarhAKTIV = new Column();
  Column odarhGODOBR = new Column();
  Column odarhMJOBR = new Column();
  Column odarhRBROBR = new Column();
  Column odarhCRADNIK = new Column();
  Column odarhCVRP = new Column();
  Column odarhRBR = new Column();
  Column odarhCVRODB = new Column();
  Column odarhCKEY = new Column();
  Column odarhCKEY2 = new Column();
  Column odarhRBRODB = new Column();
  Column odarhOBROSN = new Column();
  Column odarhOBRSTOPA = new Column();
  Column odarhOBRIZNOS = new Column();
  Column odarhNIVOODB = new Column();
  Column odarhTIPODB = new Column();
  Column odarhVRSTAOSN = new Column();
  Column odarhOSNOVICA = new Column();
  Column odarhCPOV = new Column();
  Column odarhPNB1 = new Column();
  Column odarhPNB2 = new Column();
  Column odarhIZNOS = new Column();
  Column odarhSTOPA = new Column();
  Column odarhDATPOC = new Column();
  Column odarhDATZAV = new Column();
  Column odarhGLAVNICA = new Column();
  Column odarhRATA = new Column();
  Column odarhSALDO = new Column();
  Column odarhSTAVKA = new Column();
  Column odarhPARAMETRI = new Column();

  public static Odbiciarh getDataModule() {
    if (Odbiciarhclass == null) {
      Odbiciarhclass = new Odbiciarh();
    }
    return Odbiciarhclass;
  }

  public QueryDataSet getQueryDataSet() {
    return odarh;
  }

  public Odbiciarh() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    odarhLOKK.setCaption("Status zauzetosti");
    odarhLOKK.setColumnName("LOKK");
    odarhLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhLOKK.setPrecision(1);
    odarhLOKK.setTableName("ODBICIARH");
    odarhLOKK.setServerColumnName("LOKK");
    odarhLOKK.setSqlType(1);
    odarhLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    odarhLOKK.setDefault("N");
    odarhAKTIV.setCaption("Aktivan - neaktivan");
    odarhAKTIV.setColumnName("AKTIV");
    odarhAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhAKTIV.setPrecision(1);
    odarhAKTIV.setTableName("ODBICIARH");
    odarhAKTIV.setServerColumnName("AKTIV");
    odarhAKTIV.setSqlType(1);
    odarhAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    odarhAKTIV.setDefault("D");
    odarhGODOBR.setCaption("Godina obra\u010Duna");
    odarhGODOBR.setColumnName("GODOBR");
    odarhGODOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odarhGODOBR.setPrecision(4);
    odarhGODOBR.setRowId(true);
    odarhGODOBR.setTableName("ODBICIARH");
    odarhGODOBR.setServerColumnName("GODOBR");
    odarhGODOBR.setSqlType(5);
    odarhGODOBR.setWidth(4);
    odarhMJOBR.setCaption("Mjesec obra\u010Duna");
    odarhMJOBR.setColumnName("MJOBR");
    odarhMJOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odarhMJOBR.setPrecision(2);
    odarhMJOBR.setRowId(true);
    odarhMJOBR.setTableName("ODBICIARH");
    odarhMJOBR.setServerColumnName("MJOBR");
    odarhMJOBR.setSqlType(5);
    odarhMJOBR.setWidth(2);
    odarhRBROBR.setCaption("Redni broj obra\u010Duna");
    odarhRBROBR.setColumnName("RBROBR");
    odarhRBROBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odarhRBROBR.setPrecision(3);
    odarhRBROBR.setRowId(true);
    odarhRBROBR.setTableName("ODBICIARH");
    odarhRBROBR.setServerColumnName("RBROBR");
    odarhRBROBR.setSqlType(5);
    odarhRBROBR.setWidth(3);
    odarhCRADNIK.setCaption("Radnik");
    odarhCRADNIK.setColumnName("CRADNIK");
    odarhCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhCRADNIK.setPrecision(6);
    odarhCRADNIK.setRowId(true);
    odarhCRADNIK.setTableName("ODBICIARH");
    odarhCRADNIK.setServerColumnName("CRADNIK");
    odarhCRADNIK.setSqlType(1);
    odarhCVRP.setCaption("Vrsta primanja");
    odarhCVRP.setColumnName("CVRP");
    odarhCVRP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odarhCVRP.setPrecision(3);
    odarhCVRP.setRowId(true);
    odarhCVRP.setTableName("ODBICIARH");
    odarhCVRP.setServerColumnName("CVRP");
    odarhCVRP.setSqlType(5);
    odarhCVRP.setWidth(3);
    odarhRBR.setCaption("Redni broj");
    odarhRBR.setColumnName("RBR");
    odarhRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odarhRBR.setPrecision(3);
    odarhRBR.setRowId(true);
    odarhRBR.setTableName("ODBICIARH");
    odarhRBR.setServerColumnName("RBR");
    odarhRBR.setSqlType(5);
    odarhRBR.setWidth(3);
    odarhRBR.setDefault("1");
    odarhCVRODB.setCaption("Vrsta odbitka");
    odarhCVRODB.setColumnName("CVRODB");
    odarhCVRODB.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odarhCVRODB.setPrecision(4);
    odarhCVRODB.setRowId(true);
    odarhCVRODB.setTableName("ODBICIARH");
    odarhCVRODB.setServerColumnName("CVRODB");
    odarhCVRODB.setSqlType(5);
    odarhCVRODB.setWidth(4);
    odarhCKEY.setCaption("Klju\u010D nivoa odbitka");
    odarhCKEY.setColumnName("CKEY");
    odarhCKEY.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhCKEY.setPrecision(30);
    odarhCKEY.setRowId(true);
    odarhCKEY.setTableName("ODBICIARH");
    odarhCKEY.setServerColumnName("CKEY");
    odarhCKEY.setSqlType(1);
    odarhCKEY2.setCaption("Klju\u010D 2");
    odarhCKEY2.setColumnName("CKEY2");
    odarhCKEY2.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhCKEY2.setPrecision(30);
    odarhCKEY2.setRowId(true);
    odarhCKEY2.setTableName("ODBICIARH");
    odarhCKEY2.setServerColumnName("CKEY2");
    odarhCKEY2.setSqlType(1);
    odarhRBRODB.setCaption("Redni broj odbitka");
    odarhRBRODB.setColumnName("RBRODB");
    odarhRBRODB.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odarhRBRODB.setPrecision(4);
    odarhRBRODB.setRowId(true);
    odarhRBRODB.setTableName("ODBICIARH");
    odarhRBRODB.setServerColumnName("RBRODB");
    odarhRBRODB.setSqlType(5);
    odarhRBRODB.setWidth(4);
    odarhOBROSN.setCaption("Osnovica za obra\u010Dun");
    odarhOBROSN.setColumnName("OBROSN");
    odarhOBROSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odarhOBROSN.setPrecision(17);
    odarhOBROSN.setScale(2);
    odarhOBROSN.setDisplayMask("###,###,##0.00");
    odarhOBROSN.setDefault("0");
    odarhOBROSN.setTableName("ODBICIARH");
    odarhOBROSN.setServerColumnName("OBROSN");
    odarhOBROSN.setSqlType(2);
    odarhOBROSN.setDefault("0");
    odarhOBRSTOPA.setCaption("Stopa");
    odarhOBRSTOPA.setColumnName("OBRSTOPA");
    odarhOBRSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odarhOBRSTOPA.setPrecision(17);
    odarhOBRSTOPA.setScale(2);
    odarhOBRSTOPA.setDisplayMask("###,###,##0.00");
    odarhOBRSTOPA.setDefault("0");
    odarhOBRSTOPA.setTableName("ODBICIARH");
    odarhOBRSTOPA.setServerColumnName("OBRSTOPA");
    odarhOBRSTOPA.setSqlType(2);
    odarhOBRSTOPA.setDefault("0");
    odarhOBRIZNOS.setCaption("Obra\u010Dunati iznos");
    odarhOBRIZNOS.setColumnName("OBRIZNOS");
    odarhOBRIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odarhOBRIZNOS.setPrecision(17);
    odarhOBRIZNOS.setScale(2);
    odarhOBRIZNOS.setDisplayMask("###,###,##0.00");
    odarhOBRIZNOS.setDefault("0");
    odarhOBRIZNOS.setTableName("ODBICIARH");
    odarhOBRIZNOS.setServerColumnName("OBRIZNOS");
    odarhOBRIZNOS.setSqlType(2);
    odarhOBRIZNOS.setDefault("0");
    odarhNIVOODB.setCaption("Nivo odbitka");
    odarhNIVOODB.setColumnName("NIVOODB");
    odarhNIVOODB.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhNIVOODB.setPrecision(4);
    odarhNIVOODB.setTableName("ODBICIARH");
    odarhNIVOODB.setServerColumnName("NIVOODB");
    odarhNIVOODB.setSqlType(1);
    odarhNIVOODB.setDefault("RA");
    odarhTIPODB.setCaption("Tip odbitka");
    odarhTIPODB.setColumnName("TIPODB");
    odarhTIPODB.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhTIPODB.setPrecision(1);
    odarhTIPODB.setTableName("ODBICIARH");
    odarhTIPODB.setServerColumnName("TIPODB");
    odarhTIPODB.setSqlType(1);
    odarhTIPODB.setDefault("S");
    odarhVRSTAOSN.setCaption("Vrsta osnovice");
    odarhVRSTAOSN.setColumnName("VRSTAOSN");
    odarhVRSTAOSN.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhVRSTAOSN.setPrecision(1);
    odarhVRSTAOSN.setTableName("ODBICIARH");
    odarhVRSTAOSN.setServerColumnName("VRSTAOSN");
    odarhVRSTAOSN.setSqlType(1);
    odarhVRSTAOSN.setDefault("3");
    odarhOSNOVICA.setCaption("Osnovica za obracun");
    odarhOSNOVICA.setColumnName("OSNOVICA");
    odarhOSNOVICA.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhOSNOVICA.setPrecision(1);
    odarhOSNOVICA.setTableName("ODBICIARH");
    odarhOSNOVICA.setServerColumnName("OSNOVICA");
    odarhOSNOVICA.setSqlType(1);
    odarhOSNOVICA.setDefault("0");
    odarhCPOV.setCaption("Oznaka povjerioca-virmana");
    odarhCPOV.setColumnName("CPOV");
    odarhCPOV.setDataType(com.borland.dx.dataset.Variant.INT);
    odarhCPOV.setPrecision(6);
    odarhCPOV.setTableName("ODBICIARH");
    odarhCPOV.setServerColumnName("CPOV");
    odarhCPOV.setSqlType(4);
    odarhCPOV.setWidth(6);
    odarhPNB1.setCaption("Poziv na broj 1");
    odarhPNB1.setColumnName("PNB1");
    odarhPNB1.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhPNB1.setPrecision(2);
    odarhPNB1.setTableName("ODBICIARH");
    odarhPNB1.setServerColumnName("PNB1");
    odarhPNB1.setSqlType(1);
    odarhPNB2.setCaption("Poziv na broj");
    odarhPNB2.setColumnName("PNB2");
    odarhPNB2.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhPNB2.setPrecision(22);
    odarhPNB2.setTableName("ODBICIARH");
    odarhPNB2.setServerColumnName("PNB2");
    odarhPNB2.setSqlType(1);
    odarhIZNOS.setCaption("Iznos");
    odarhIZNOS.setColumnName("IZNOS");
    odarhIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odarhIZNOS.setPrecision(17);
    odarhIZNOS.setScale(2);
    odarhIZNOS.setDisplayMask("###,###,##0.00");
    odarhIZNOS.setDefault("0");
    odarhIZNOS.setTableName("ODBICIARH");
    odarhIZNOS.setServerColumnName("IZNOS");
    odarhIZNOS.setSqlType(2);
    odarhIZNOS.setDefault("0");
    odarhSTOPA.setCaption("Stopa");
    odarhSTOPA.setColumnName("STOPA");
    odarhSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odarhSTOPA.setPrecision(9);
    odarhSTOPA.setScale(5);
    odarhSTOPA.setDisplayMask("###,###,##0.00000");
    odarhSTOPA.setDefault("0");
    odarhSTOPA.setTableName("ODBICIARH");
    odarhSTOPA.setServerColumnName("STOPA");
    odarhSTOPA.setSqlType(2);
    odarhSTOPA.setDefault("0");
    odarhDATPOC.setCaption("Po\u010Detak obustave");
    odarhDATPOC.setColumnName("DATPOC");
    odarhDATPOC.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    odarhDATPOC.setPrecision(8);
    odarhDATPOC.setDisplayMask("dd-MM-yyyy");
//    odarhDATPOC.setEditMask("dd-MM-yyyy");
    odarhDATPOC.setTableName("ODBICIARH");
    odarhDATPOC.setWidth(10);
    odarhDATPOC.setServerColumnName("DATPOC");
    odarhDATPOC.setSqlType(93);
    odarhDATZAV.setCaption("Završetak obustave");
    odarhDATZAV.setColumnName("DATZAV");
    odarhDATZAV.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    odarhDATZAV.setPrecision(8);
//    odarhDATZAV.setDisplayMask("dd-MM-yyyy");
//    odarhDATZAV.setEditMask("dd-MM-yyyy");
    odarhDATZAV.setTableName("ODBICIARH");
    odarhDATZAV.setWidth(10);
    odarhDATZAV.setServerColumnName("DATZAV");
    odarhDATZAV.setSqlType(93);
    odarhGLAVNICA.setCaption("Glavnica");
    odarhGLAVNICA.setColumnName("GLAVNICA");
    odarhGLAVNICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odarhGLAVNICA.setPrecision(17);
    odarhGLAVNICA.setScale(2);
    odarhGLAVNICA.setDisplayMask("###,###,##0.00");
    odarhGLAVNICA.setDefault("0");
    odarhGLAVNICA.setTableName("ODBICIARH");
    odarhGLAVNICA.setServerColumnName("GLAVNICA");
    odarhGLAVNICA.setSqlType(2);
    odarhGLAVNICA.setDefault("0");
    odarhRATA.setCaption("Rata");
    odarhRATA.setColumnName("RATA");
    odarhRATA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odarhRATA.setPrecision(17);
    odarhRATA.setScale(2);
    odarhRATA.setDisplayMask("###,###,##0.00");
    odarhRATA.setDefault("0");
    odarhRATA.setTableName("ODBICIARH");
    odarhRATA.setServerColumnName("RATA");
    odarhRATA.setSqlType(2);
    odarhRATA.setDefault("0");
    odarhSALDO.setCaption("Ostatak duga");
    odarhSALDO.setColumnName("SALDO");
    odarhSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odarhSALDO.setPrecision(17);
    odarhSALDO.setScale(2);
    odarhSALDO.setDisplayMask("###,###,##0.00");
    odarhSALDO.setDefault("0");
    odarhSALDO.setTableName("ODBICIARH");
    odarhSALDO.setServerColumnName("SALDO");
    odarhSALDO.setSqlType(2);
    odarhSALDO.setDefault("0");
    odarhSTAVKA.setCaption("Veza sa kontom CSKL=1 APP=pl VRDOK=PL");
    odarhSTAVKA.setColumnName("STAVKA");
    odarhSTAVKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhSTAVKA.setPrecision(8);
    odarhSTAVKA.setTableName("ODBICIARH");
    odarhSTAVKA.setServerColumnName("STAVKA");
    odarhSTAVKA.setSqlType(1);
    odarhPARAMETRI.setCaption("Parametri");
    odarhPARAMETRI.setColumnName("PARAMETRI");
    odarhPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    odarhPARAMETRI.setPrecision(20);
    odarhPARAMETRI.setTableName("ODBICIARH");
    odarhPARAMETRI.setServerColumnName("PARAMETRI");
    odarhPARAMETRI.setSqlType(1);
    odarhPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    odarh.setResolver(dm.getQresolver());
    odarh.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Odbiciarh", null, true, Load.ALL));
 setColumns(new Column[] {odarhLOKK, odarhAKTIV, odarhGODOBR, odarhMJOBR, odarhRBROBR, odarhCRADNIK, odarhCVRP, odarhRBR, odarhCVRODB, odarhCKEY,
        odarhCKEY2, odarhRBRODB, odarhOBROSN, odarhOBRSTOPA, odarhOBRIZNOS, odarhNIVOODB, odarhTIPODB, odarhVRSTAOSN, odarhOSNOVICA, odarhCPOV, odarhPNB1,
        odarhPNB2, odarhIZNOS, odarhSTOPA, odarhDATPOC, odarhDATZAV, odarhGLAVNICA, odarhRATA, odarhSALDO, odarhSTAVKA, odarhPARAMETRI});
  }

  public void setall() {

    ddl.create("Odbiciarh")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("godobr", 4, true)
       .addShort("mjobr", 2, true)
       .addShort("rbrobr", 3, true)
       .addChar("cradnik", 6, true)
       .addShort("cvrp", 3, true)
       .addShort("rbr", 3, true)
       .addShort("cvrodb", 4, true)
       .addChar("ckey", 30, true)
       .addChar("ckey2", 30, true)
       .addShort("rbrodb", 4, true)
       .addFloat("obrosn", 17, 2)
       .addFloat("obrstopa", 17, 2)
       .addFloat("obriznos", 17, 2)
       .addChar("nivoodb", 4, "RA")
       .addChar("tipodb", 1, "S")
       .addChar("vrstaosn", 1, "3")
       .addChar("osnovica", 1, "0")
       .addInteger("cpov", 6)
       .addChar("pnb1", 2)
       .addChar("pnb2", 22)
       .addFloat("iznos", 17, 2)
       .addFloat("stopa", 9, 5)
       .addDate("datpoc")
       .addDate("datzav")
       .addFloat("glavnica", 17, 2)
       .addFloat("rata", 17, 2)
       .addFloat("saldo", 17, 2)
       .addChar("stavka", 8)
       .addChar("parametri", 20)
       .addPrimaryKey("godobr,mjobr,rbrobr,cradnik,cvrp,rbr,cvrodb,ckey,ckey2,rbrodb");


    Naziv = "Odbiciarh";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"mjobr", "rbrobr", "cradnik","rbr", "ckey", "ckey2", "rbrodb"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
