/****license*****************************************************************
**   file: Primanjaarh.java
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



public class Primanjaarh extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Primanjaarh Primanjaarhclass;

  QueryDataSet prarh = new QueryDataSet();

  Column prarhLOKK = new Column();
  Column prarhAKTIV = new Column();
  Column prarhGODOBR = new Column();
  Column prarhMJOBR = new Column();
  Column prarhRBROBR = new Column();
  Column prarhCRADNIK = new Column();
  Column prarhCVRP = new Column();
  Column prarhRBR = new Column();
  Column prarhCORG = new Column();
  Column prarhSATI = new Column();
  Column prarhKOEF = new Column();
  Column prarhBRUTO = new Column();
  Column prarhDOPRINOSI = new Column();
  Column prarhNETO = new Column();
  Column prarhCOBR = new Column();
  Column prarhCOSN = new Column();
  Column prarhRSOO = new Column();
  Column prarhRNALOG = new Column();
  Column prarhREGRES = new Column();
  Column prarhCGRPRIM = new Column();
  Column prarhCVRPARH = new Column();
  Column prarhSTAVKA = new Column();
  Column prarhPARAMETRI = new Column();
  Column prarhIRAZOD = new Column();
  Column prarhIRAZDO = new Column();

  public static Primanjaarh getDataModule() {
    if (Primanjaarhclass == null) {
      Primanjaarhclass = new Primanjaarh();
    }
    return Primanjaarhclass;
  }

  public QueryDataSet getQueryDataSet() {
    return prarh;
  }

  public Primanjaarh() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    prarhLOKK.setCaption("Status zauzetosti");
    prarhLOKK.setColumnName("LOKK");
    prarhLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    prarhLOKK.setPrecision(1);
    prarhLOKK.setTableName("PRIMANJAARH");
    prarhLOKK.setServerColumnName("LOKK");
    prarhLOKK.setSqlType(1);
    prarhLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    prarhLOKK.setDefault("N");
    prarhAKTIV.setCaption("Aktivan - neaktivan");
    prarhAKTIV.setColumnName("AKTIV");
    prarhAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    prarhAKTIV.setPrecision(1);
    prarhAKTIV.setTableName("PRIMANJAARH");
    prarhAKTIV.setServerColumnName("AKTIV");
    prarhAKTIV.setSqlType(1);
    prarhAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    prarhAKTIV.setDefault("D");
    prarhGODOBR.setCaption("Godina obra\u010Duna");
    prarhGODOBR.setColumnName("GODOBR");
    prarhGODOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    prarhGODOBR.setPrecision(4);
    prarhGODOBR.setRowId(true);
    prarhGODOBR.setTableName("PRIMANJAARH");
    prarhGODOBR.setServerColumnName("GODOBR");
    prarhGODOBR.setSqlType(5);
    prarhGODOBR.setWidth(4);
    prarhMJOBR.setCaption("Mjesec obra\u010Duna");
    prarhMJOBR.setColumnName("MJOBR");
    prarhMJOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    prarhMJOBR.setPrecision(2);
    prarhMJOBR.setRowId(true);
    prarhMJOBR.setTableName("PRIMANJAARH");
    prarhMJOBR.setServerColumnName("MJOBR");
    prarhMJOBR.setSqlType(5);
    prarhMJOBR.setWidth(2);
    prarhRBROBR.setCaption("Redni broj obra\u010Duna");
    prarhRBROBR.setColumnName("RBROBR");
    prarhRBROBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    prarhRBROBR.setPrecision(3);
    prarhRBROBR.setRowId(true);
    prarhRBROBR.setTableName("PRIMANJAARH");
    prarhRBROBR.setServerColumnName("RBROBR");
    prarhRBROBR.setSqlType(5);
    prarhRBROBR.setWidth(3);
    prarhCRADNIK.setCaption("Radnik");
    prarhCRADNIK.setColumnName("CRADNIK");
    prarhCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    prarhCRADNIK.setPrecision(6);
    prarhCRADNIK.setRowId(true);
    prarhCRADNIK.setTableName("PRIMANJAARH");
    prarhCRADNIK.setServerColumnName("CRADNIK");
    prarhCRADNIK.setSqlType(1);
    prarhCVRP.setCaption("Vrsta primanja");
    prarhCVRP.setColumnName("CVRP");
    prarhCVRP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    prarhCVRP.setPrecision(3);
    prarhCVRP.setRowId(true);
    prarhCVRP.setTableName("PRIMANJAARH");
    prarhCVRP.setServerColumnName("CVRP");
    prarhCVRP.setSqlType(5);
    prarhCVRP.setWidth(3);
    prarhRBR.setCaption("Redni broj");
    prarhRBR.setColumnName("RBR");
    prarhRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    prarhRBR.setPrecision(3);
    prarhRBR.setRowId(true);
    prarhRBR.setTableName("PRIMANJAARH");
    prarhRBR.setServerColumnName("RBR");
    prarhRBR.setSqlType(5);
    prarhRBR.setWidth(3);
    prarhRBR.setDefault("1");
    prarhCORG.setCaption("Org. Jedinica u kojoj je obavljen rad");
    prarhCORG.setColumnName("CORG");
    prarhCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    prarhCORG.setPrecision(12);
    prarhCORG.setTableName("PRIMANJAARH");
    prarhCORG.setServerColumnName("CORG");
    prarhCORG.setSqlType(1);
    prarhSATI.setCaption("Sati");
    prarhSATI.setColumnName("SATI");
    prarhSATI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    prarhSATI.setPrecision(17);
    prarhSATI.setScale(2);
    prarhSATI.setDisplayMask("###,###,##0.00");
    prarhSATI.setDefault("0");
    prarhSATI.setTableName("PRIMANJAARH");
    prarhSATI.setServerColumnName("SATI");
    prarhSATI.setSqlType(2);
    prarhSATI.setDefault("0");
    prarhKOEF.setCaption("Koeficijent");
    prarhKOEF.setColumnName("KOEF");
    prarhKOEF.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    prarhKOEF.setPrecision(17);
    prarhKOEF.setScale(2);
    prarhKOEF.setDisplayMask("###,###,##0.00");
    prarhKOEF.setDefault("0");
    prarhKOEF.setTableName("PRIMANJAARH");
    prarhKOEF.setServerColumnName("KOEF");
    prarhKOEF.setSqlType(2);
    prarhKOEF.setDefault("0");
    prarhBRUTO.setCaption("Bruto");
    prarhBRUTO.setColumnName("BRUTO");
    prarhBRUTO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    prarhBRUTO.setPrecision(17);
    prarhBRUTO.setScale(2);
    prarhBRUTO.setDisplayMask("###,###,##0.00");
    prarhBRUTO.setDefault("0");
    prarhBRUTO.setTableName("PRIMANJAARH");
    prarhBRUTO.setServerColumnName("BRUTO");
    prarhBRUTO.setSqlType(2);
    prarhBRUTO.setDefault("0");
    prarhDOPRINOSI.setCaption("Odbici od bruta (doprinosi)");
    prarhDOPRINOSI.setColumnName("DOPRINOSI");
    prarhDOPRINOSI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    prarhDOPRINOSI.setPrecision(17);
    prarhDOPRINOSI.setScale(2);
    prarhDOPRINOSI.setDisplayMask("###,###,##0.00");
    prarhDOPRINOSI.setDefault("0");
    prarhDOPRINOSI.setTableName("PRIMANJAARH");
    prarhDOPRINOSI.setServerColumnName("DOPRINOSI");
    prarhDOPRINOSI.setSqlType(2);
    prarhDOPRINOSI.setDefault("0");
    prarhNETO.setCaption("Neto za oporetivanje");
    prarhNETO.setColumnName("NETO");
    prarhNETO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    prarhNETO.setPrecision(17);
    prarhNETO.setScale(2);
    prarhNETO.setDisplayMask("###,###,##0.00");
    prarhNETO.setDefault("0");
    prarhNETO.setTableName("PRIMANJAARH");
    prarhNETO.setServerColumnName("NETO");
    prarhNETO.setSqlType(2);
    prarhNETO.setDefault("0");
    prarhCOBR.setCaption("Na\u010Din obra\u010Duna");
    prarhCOBR.setColumnName("COBR");
    prarhCOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    prarhCOBR.setPrecision(2);
    prarhCOBR.setTableName("PRIMANJAARH");
    prarhCOBR.setServerColumnName("COBR");
    prarhCOBR.setSqlType(5);
    prarhCOBR.setWidth(2);
    prarhCOSN.setCaption("Oznaka osnovice iz koje se ra\u010Duna zarada");
    prarhCOSN.setColumnName("COSN");
    prarhCOSN.setDataType(com.borland.dx.dataset.Variant.SHORT);
    prarhCOSN.setPrecision(2);
    prarhCOSN.setTableName("PRIMANJAARH");
    prarhCOSN.setServerColumnName("COSN");
    prarhCOSN.setSqlType(5);
    prarhCOSN.setWidth(2);
    prarhRSOO.setCaption("Osnova osiguranja");
    prarhRSOO.setColumnName("RSOO");
    prarhRSOO.setDataType(com.borland.dx.dataset.Variant.STRING);
    prarhRSOO.setPrecision(5);
    prarhRSOO.setTableName("PRIMANJAARH");
    prarhRSOO.setServerColumnName("RSOO");
    prarhRSOO.setSqlType(1);
    prarhRNALOG.setCaption("Unos radnog naloga");
    prarhRNALOG.setColumnName("RNALOG");
    prarhRNALOG.setDataType(com.borland.dx.dataset.Variant.STRING);
    prarhRNALOG.setPrecision(1);
    prarhRNALOG.setTableName("PRIMANJAARH");
    prarhRNALOG.setServerColumnName("RNALOG");
    prarhRNALOG.setSqlType(1);
    prarhRNALOG.setDefault("N");
    prarhREGRES.setCaption("Primanje na godišnjoj osnovi (regres)");
    prarhREGRES.setColumnName("REGRES");
    prarhREGRES.setDataType(com.borland.dx.dataset.Variant.STRING);
    prarhREGRES.setPrecision(1);
    prarhREGRES.setTableName("PRIMANJAARH");
    prarhREGRES.setServerColumnName("REGRES");
    prarhREGRES.setSqlType(1);
    prarhCGRPRIM.setCaption("Grupa primanja");
    prarhCGRPRIM.setColumnName("CGRPRIM");
    prarhCGRPRIM.setDataType(com.borland.dx.dataset.Variant.STRING);
    prarhCGRPRIM.setPrecision(5);
    prarhCGRPRIM.setTableName("PRIMANJAARH");
    prarhCGRPRIM.setServerColumnName("CGRPRIM");
    prarhCGRPRIM.setSqlType(1);
    prarhCVRPARH.setCaption("Vrsta primanja u koju se dohva\u0107a iz arhive");
    prarhCVRPARH.setColumnName("CVRPARH");
    prarhCVRPARH.setDataType(com.borland.dx.dataset.Variant.SHORT);
    prarhCVRPARH.setPrecision(3);
    prarhCVRPARH.setTableName("PRIMANJAARH");
    prarhCVRPARH.setServerColumnName("CVRPARH");
    prarhCVRPARH.setSqlType(5);
    prarhCVRPARH.setWidth(3);
    prarhSTAVKA.setCaption("Veza sa kontom preko SHKONTA.VRDOK+CSKL+STAVKA -> CSKL=1(APPL=pl) VRDOK=PL");
    prarhSTAVKA.setColumnName("STAVKA");
    prarhSTAVKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    prarhSTAVKA.setPrecision(8);
    prarhSTAVKA.setTableName("PRIMANJAARH");
    prarhSTAVKA.setServerColumnName("STAVKA");
    prarhSTAVKA.setSqlType(1);
    prarhPARAMETRI.setCaption("Parametri");
    prarhPARAMETRI.setColumnName("PARAMETRI");
    prarhPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    prarhPARAMETRI.setPrecision(20);
    prarhPARAMETRI.setTableName("PRIMANJAARH");
    prarhPARAMETRI.setServerColumnName("PARAMETRI");
    prarhPARAMETRI.setSqlType(1);
    prarhPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    prarhIRAZOD.setCaption("Ispla\u0107eno za razdoblje od");
    prarhIRAZOD.setColumnName("IRAZOD");
    prarhIRAZOD.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    prarhIRAZOD.setPrecision(8);
    prarhIRAZOD.setDisplayMask("dd-MM-yyyy");
//    prarhIRAZOD.setEditMask("dd-MM-yyyy");
    prarhIRAZOD.setTableName("PRIMANJAARH");
    prarhIRAZOD.setServerColumnName("IRAZOD");
    prarhIRAZOD.setSqlType(93);
    prarhIRAZOD.setWidth(10);
    prarhIRAZDO.setCaption("Ispla\u0107eno za razdoblje do");
    prarhIRAZDO.setColumnName("IRAZDO");
    prarhIRAZDO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    prarhIRAZDO.setPrecision(8);
    prarhIRAZDO.setDisplayMask("dd-MM-yyyy");
//    prarhIRAZDO.setEditMask("dd-MM-yyyy");
    prarhIRAZDO.setTableName("PRIMANJAARH");
    prarhIRAZDO.setServerColumnName("IRAZDO");
    prarhIRAZDO.setSqlType(93);
    prarhIRAZDO.setWidth(10);
    prarh.setResolver(dm.getQresolver());
    prarh.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Primanjaarh", null, true, Load.ALL));
 setColumns(new Column[] {prarhLOKK, prarhAKTIV, prarhGODOBR, prarhMJOBR, prarhRBROBR, prarhCRADNIK, prarhCVRP, prarhRBR, prarhCORG, prarhSATI,
        prarhKOEF, prarhBRUTO, prarhDOPRINOSI, prarhNETO, prarhCOBR, prarhCOSN, prarhRSOO, prarhRNALOG, prarhREGRES, prarhCGRPRIM, prarhCVRPARH, prarhSTAVKA,
        prarhPARAMETRI, prarhIRAZOD, prarhIRAZDO});
  }

  public void setall() {

    ddl.create("Primanjaarh")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("godobr", 4, true)
       .addShort("mjobr", 2, true)
       .addShort("rbrobr", 3, true)
       .addChar("cradnik", 6, true)
       .addShort("cvrp", 3, true)
       .addShort("rbr", 3, true)
       .addChar("corg", 12)
       .addFloat("sati", 17, 2)
       .addFloat("koef", 17, 2)
       .addFloat("bruto", 17, 2)
       .addFloat("doprinosi", 17, 2)
       .addFloat("neto", 17, 2)
       .addShort("cobr", 2)
       .addShort("cosn", 2)
       .addChar("rsoo", 5)
       .addChar("rnalog", 1, "N")
       .addChar("regres", 1)
       .addChar("cgrprim", 5)
       .addShort("cvrparh", 3)
       .addChar("stavka", 8)
       .addChar("parametri", 20)
       .addDate("irazod")
       .addDate("irazdo")
       .addPrimaryKey("godobr,mjobr,rbrobr,cradnik,cvrp,rbr");


    Naziv = "Primanjaarh";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"mjobr", "rbrobr", "cradnik", "rbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
