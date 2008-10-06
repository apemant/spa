/****license*****************************************************************
**   file: OS_Promjene.java
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



public class OS_Promjene extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Promjene OS_Promjeneclass;

  QueryDataSet osprom = new QueryDataSet();

  Column ospromLOKK = new Column();
  Column ospromAKTIV = new Column();
  Column ospromCORG = new Column();
  Column ospromCORG2 = new Column();
  Column ospromINVBROJ = new Column();
  Column ospromCPROMJENE = new Column();
  Column ospromDATPROMJENE = new Column();
  Column ospromOSNDUGUJE = new Column();
  Column ospromOSNPOTRAZUJE = new Column();
  Column ospromISPDUGUJE = new Column();
  Column ospromISPPOTRAZUJE = new Column();
  Column ospromOSNPOCETAK = new Column();
  Column ospromISPPOCETAK = new Column();
  Column ospromDATKNJIZENJA = new Column();
  Column ospromSTATUSKNJ = new Column();
  Column ospromCNALOGA = new Column();
  Column ospromRBR = new Column();
  Column ospromSTATUS = new Column();
  Column ospromDOKUMENT = new Column();
  Column ospromOPIS = new Column();
  Column ospromCPAR = new Column();
  Column ospromMJESECP = new Column();
  Column ospromSALDO = new Column();
  Column ospromOJINVBRB = new Column();
  Column ospromUIPRPOR = new Column();
  Column ospromOLDCORG = new Column();
  Column ospromOSNOVICA = new Column();
  Column ospromISPRAVAK = new Column();

  public static OS_Promjene getDataModule() {
    if (OS_Promjeneclass == null) {
      OS_Promjeneclass = new OS_Promjene();
    }
    return OS_Promjeneclass;
  }

  public QueryDataSet getQueryDataSet() {
    return osprom;
  }

  public OS_Promjene() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    ospromLOKK.setCaption("Status zauzetosti");
    ospromLOKK.setColumnName("LOKK");
    ospromLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromLOKK.setPrecision(1);
    ospromLOKK.setTableName("OS_PROMJENE");
    ospromLOKK.setServerColumnName("LOKK");
    ospromLOKK.setSqlType(1);
    ospromLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ospromLOKK.setDefault("N");
    ospromAKTIV.setCaption("Aktivan - neaktivan");
    ospromAKTIV.setColumnName("AKTIV");
    ospromAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromAKTIV.setPrecision(1);
    ospromAKTIV.setTableName("OS_PROMJENE");
    ospromAKTIV.setServerColumnName("AKTIV");
    ospromAKTIV.setSqlType(1);
    ospromAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ospromAKTIV.setDefault("D");
    ospromCORG.setCaption("Knjigovodstvo");
    ospromCORG.setColumnName("CORG");
    ospromCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromCORG.setPrecision(12);
    ospromCORG.setTableName("OS_PROMJENE");
    ospromCORG.setServerColumnName("CORG");
    ospromCORG.setSqlType(1);
    ospromCORG2.setCaption("OJ");
    ospromCORG2.setColumnName("CORG2");
    ospromCORG2.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromCORG2.setPrecision(12);
    ospromCORG2.setRowId(true);
    ospromCORG2.setTableName("OS_PROMJENE");
    ospromCORG2.setServerColumnName("CORG2");
    ospromCORG2.setSqlType(1);
    ospromCORG2.setWidth(6);
    ospromINVBROJ.setCaption("IBroj");
    ospromINVBROJ.setColumnName("INVBROJ");
    ospromINVBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromINVBROJ.setPrecision(10);
    ospromINVBROJ.setRowId(true);
    ospromINVBROJ.setTableName("OS_PROMJENE");
    ospromINVBROJ.setServerColumnName("INVBROJ");
    ospromINVBROJ.setSqlType(1);
    ospromCPROMJENE.setCaption("VP");
    ospromCPROMJENE.setColumnName("CPROMJENE");
    ospromCPROMJENE.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromCPROMJENE.setPrecision(3);
    ospromCPROMJENE.setTableName("OS_PROMJENE");
    ospromCPROMJENE.setServerColumnName("CPROMJENE");
    ospromCPROMJENE.setSqlType(1);
    ospromCPROMJENE.setWidth(4);
    ospromDATPROMJENE.setCaption("Datum");
    ospromDATPROMJENE.setColumnName("DATPROMJENE");
    ospromDATPROMJENE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    ospromDATPROMJENE.setPrecision(8);
    ospromDATPROMJENE.setDisplayMask("dd-MM-yyyy");
//    ospromDATPROMJENE.setEditMask("dd-MM-yyyy");
    ospromDATPROMJENE.setTableName("OS_PROMJENE");
    ospromDATPROMJENE.setServerColumnName("DATPROMJENE");
    ospromDATPROMJENE.setSqlType(93);
    ospromDATPROMJENE.setWidth(10);
    ospromOSNDUGUJE.setCaption("OsnovicaD");
    ospromOSNDUGUJE.setColumnName("OSNDUGUJE");
    ospromOSNDUGUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ospromOSNDUGUJE.setPrecision(17);
    ospromOSNDUGUJE.setScale(2);
    ospromOSNDUGUJE.setDisplayMask("###,###,##0.00");
    ospromOSNDUGUJE.setDefault("0");
    ospromOSNDUGUJE.setTableName("OS_PROMJENE");
    ospromOSNDUGUJE.setServerColumnName("OSNDUGUJE");
    ospromOSNDUGUJE.setSqlType(2);
    ospromOSNDUGUJE.setDefault("0");
    ospromOSNPOTRAZUJE.setCaption("OsnovicaP");
    ospromOSNPOTRAZUJE.setColumnName("OSNPOTRAZUJE");
    ospromOSNPOTRAZUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ospromOSNPOTRAZUJE.setPrecision(17);
    ospromOSNPOTRAZUJE.setScale(2);
    ospromOSNPOTRAZUJE.setDisplayMask("###,###,##0.00");
    ospromOSNPOTRAZUJE.setDefault("0");
    ospromOSNPOTRAZUJE.setTableName("OS_PROMJENE");
    ospromOSNPOTRAZUJE.setServerColumnName("OSNPOTRAZUJE");
    ospromOSNPOTRAZUJE.setSqlType(2);
    ospromOSNPOTRAZUJE.setDefault("0");
    ospromISPDUGUJE.setCaption("IspravakD");
    ospromISPDUGUJE.setColumnName("ISPDUGUJE");
    ospromISPDUGUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ospromISPDUGUJE.setPrecision(17);
    ospromISPDUGUJE.setScale(2);
    ospromISPDUGUJE.setDisplayMask("###,###,##0.00");
    ospromISPDUGUJE.setDefault("0");
    ospromISPDUGUJE.setTableName("OS_PROMJENE");
    ospromISPDUGUJE.setServerColumnName("ISPDUGUJE");
    ospromISPDUGUJE.setSqlType(2);
    ospromISPDUGUJE.setDefault("0");
    ospromISPPOTRAZUJE.setCaption("IspravakP");
    ospromISPPOTRAZUJE.setColumnName("ISPPOTRAZUJE");
    ospromISPPOTRAZUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ospromISPPOTRAZUJE.setPrecision(17);
    ospromISPPOTRAZUJE.setScale(2);
    ospromISPPOTRAZUJE.setDisplayMask("###,###,##0.00");
    ospromISPPOTRAZUJE.setDefault("0");
    ospromISPPOTRAZUJE.setTableName("OS_PROMJENE");
    ospromISPPOTRAZUJE.setServerColumnName("ISPPOTRAZUJE");
    ospromISPPOTRAZUJE.setSqlType(2);
    ospromISPPOTRAZUJE.setDefault("0");
    ospromOSNPOCETAK.setCaption("Osnovica po\u010Detak");
    ospromOSNPOCETAK.setColumnName("OSNPOCETAK");
    ospromOSNPOCETAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ospromOSNPOCETAK.setPrecision(17);
    ospromOSNPOCETAK.setScale(2);
    ospromOSNPOCETAK.setDisplayMask("###,###,##0.00");
    ospromOSNPOCETAK.setDefault("0");
    ospromOSNPOCETAK.setTableName("OS_PROMJENE");
    ospromOSNPOCETAK.setServerColumnName("OSNPOCETAK");
    ospromOSNPOCETAK.setSqlType(2);
    ospromOSNPOCETAK.setDefault("0");
    ospromISPPOCETAK.setCaption("Ispravak po\u010Detak");
    ospromISPPOCETAK.setColumnName("ISPPOCETAK");
    ospromISPPOCETAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ospromISPPOCETAK.setPrecision(17);
    ospromISPPOCETAK.setScale(2);
    ospromISPPOCETAK.setDisplayMask("###,###,##0.00");
    ospromISPPOCETAK.setDefault("0");
    ospromISPPOCETAK.setTableName("OS_PROMJENE");
    ospromISPPOCETAK.setServerColumnName("ISPPOCETAK");
    ospromISPPOCETAK.setSqlType(2);
    ospromISPPOCETAK.setDefault("0");
    ospromDATKNJIZENJA.setCaption("Datum knjiženja");
    ospromDATKNJIZENJA.setColumnName("DATKNJIZENJA");
    ospromDATKNJIZENJA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    ospromDATKNJIZENJA.setPrecision(8);
    ospromDATKNJIZENJA.setDisplayMask("dd-MM-yyyy");
//    ospromDATKNJIZENJA.setEditMask("dd-MM-yyyy");
    ospromDATKNJIZENJA.setTableName("OS_PROMJENE");
    ospromDATKNJIZENJA.setServerColumnName("DATKNJIZENJA");
    ospromDATKNJIZENJA.setSqlType(93);
    ospromDATKNJIZENJA.setWidth(10);
    ospromSTATUSKNJ.setCaption("Status knjiženja");
    ospromSTATUSKNJ.setColumnName("STATUSKNJ");
    ospromSTATUSKNJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromSTATUSKNJ.setPrecision(1);
    ospromSTATUSKNJ.setTableName("OS_PROMJENE");
    ospromSTATUSKNJ.setServerColumnName("STATUSKNJ");
    ospromSTATUSKNJ.setSqlType(1);
    ospromSTATUSKNJ.setDefault("N");
    ospromCNALOGA.setCaption("Broj naloga");
    ospromCNALOGA.setColumnName("CNALOGA");
    ospromCNALOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromCNALOGA.setPrecision(30);
    ospromCNALOGA.setTableName("OS_PROMJENE");
    ospromCNALOGA.setServerColumnName("CNALOGA");
    ospromCNALOGA.setSqlType(1);
    ospromCNALOGA.setWidth(30);
    ospromRBR.setCaption("Rbr");
    ospromRBR.setColumnName("RBR");
    ospromRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    ospromRBR.setPrecision(6);
    ospromRBR.setRowId(true);
    ospromRBR.setTableName("OS_PROMJENE");
    ospromRBR.setServerColumnName("RBR");
    ospromRBR.setSqlType(4);
    ospromRBR.setWidth(3);
    ospromSTATUS.setCaption("Status");
    ospromSTATUS.setColumnName("STATUS");
    ospromSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromSTATUS.setPrecision(1);
    ospromSTATUS.setRowId(true);
    ospromSTATUS.setTableName("OS_PROMJENE");
    ospromSTATUS.setServerColumnName("STATUS");
    ospromSTATUS.setSqlType(1);
    ospromDOKUMENT.setCaption("Dokument");
    ospromDOKUMENT.setColumnName("DOKUMENT");
    ospromDOKUMENT.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromDOKUMENT.setPrecision(50);
    ospromDOKUMENT.setTableName("OS_PROMJENE");
    ospromDOKUMENT.setServerColumnName("DOKUMENT");
    ospromDOKUMENT.setSqlType(1);
    ospromDOKUMENT.setWidth(30);
    ospromOPIS.setCaption("Opis");
    ospromOPIS.setColumnName("OPIS");
    ospromOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromOPIS.setPrecision(100);
    ospromOPIS.setTableName("OS_PROMJENE");
    ospromOPIS.setServerColumnName("OPIS");
    ospromOPIS.setSqlType(1);
    ospromOPIS.setWidth(30);
    ospromCPAR.setCaption("Partnera");
    ospromCPAR.setColumnName("CPAR");
    ospromCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    ospromCPAR.setPrecision(6);
    ospromCPAR.setTableName("OS_PROMJENE");
    ospromCPAR.setServerColumnName("CPAR");
    ospromCPAR.setSqlType(4);
    ospromCPAR.setWidth(6);
    ospromMJESECP.setCaption("Mjesec");
    ospromMJESECP.setColumnName("MJESECP");
    ospromMJESECP.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromMJESECP.setPrecision(2);
    ospromMJESECP.setTableName("OS_PROMJENE");
    ospromMJESECP.setServerColumnName("MJESECP");
    ospromMJESECP.setSqlType(1);
    ospromSALDO.setCaption("Saldo");
    ospromSALDO.setColumnName("SALDO");
    ospromSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ospromSALDO.setPrecision(17);
    ospromSALDO.setScale(2);
    ospromSALDO.setDisplayMask("###,###,##0.00");
    ospromSALDO.setDefault("0");
    ospromSALDO.setTableName("OS_PROMJENE");
    ospromSALDO.setServerColumnName("SALDO");
    ospromSALDO.setSqlType(2);
    ospromSALDO.setDefault("0");
    ospromOJINVBRB.setCaption("Ojin");
    ospromOJINVBRB.setColumnName("OJINVBRB");
    ospromOJINVBRB.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromOJINVBRB.setPrecision(1);
    ospromOJINVBRB.setTableName("OS_PROMJENE");
    ospromOJINVBRB.setServerColumnName("OJINVBRB");
    ospromOJINVBRB.setSqlType(1);
    ospromOJINVBRB.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    ospromUIPRPOR.setCaption("Pretporez");
    ospromUIPRPOR.setColumnName("UIPRPOR");
    ospromUIPRPOR.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ospromUIPRPOR.setPrecision(17);
    ospromUIPRPOR.setScale(2);
    ospromUIPRPOR.setDisplayMask("###,###,##0.00");
    ospromUIPRPOR.setDefault("0");
    ospromUIPRPOR.setTableName("OS_PROMJENE");
    ospromUIPRPOR.setServerColumnName("UIPRPOR");
    ospromUIPRPOR.setSqlType(2);
    ospromUIPRPOR.setDefault("0");
    ospromOLDCORG.setCaption("Stara OJ");
    ospromOLDCORG.setColumnName("OLDCORG");
    ospromOLDCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    ospromOLDCORG.setPrecision(12);
    ospromOLDCORG.setTableName("OS_PROMJENE");
    ospromOLDCORG.setServerColumnName("OLDCORG");
    ospromOLDCORG.setSqlType(1);
    ospromOLDCORG.setWidth(6);
    ospromOSNOVICA.setCaption("Osnovica");
    ospromOSNOVICA.setColumnName("OSNOVICA");
    ospromOSNOVICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ospromOSNOVICA.setPrecision(17);
    ospromOSNOVICA.setScale(2);
    ospromOSNOVICA.setDisplayMask("###,###,##0.00");
    ospromOSNOVICA.setDefault("0");
    ospromOSNOVICA.setTableName("OS_PROMJENE");
    ospromOSNOVICA.setServerColumnName("OSNOVICA");
    ospromOSNOVICA.setSqlType(2);
    ospromOSNOVICA.setDefault("0");
    ospromISPRAVAK.setCaption("Ispravak");
    ospromISPRAVAK.setColumnName("ISPRAVAK");
    ospromISPRAVAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    ospromISPRAVAK.setPrecision(17);
    ospromISPRAVAK.setScale(2);
    ospromISPRAVAK.setDisplayMask("###,###,##0.00");
    ospromISPRAVAK.setDefault("0");
    ospromISPRAVAK.setTableName("OS_PROMJENE");
    ospromISPRAVAK.setServerColumnName("ISPRAVAK");
    ospromISPRAVAK.setSqlType(2);
    ospromISPRAVAK.setDefault("0");
    osprom.setResolver(dm.getQresolver());
    osprom.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Promjene", null, true, Load.ALL));
 setColumns(new Column[] {ospromLOKK, ospromAKTIV, ospromCORG, ospromCORG2, ospromINVBROJ, ospromCPROMJENE, ospromDATPROMJENE, ospromOSNDUGUJE,
        ospromOSNPOTRAZUJE, ospromISPDUGUJE, ospromISPPOTRAZUJE, ospromOSNPOCETAK, ospromISPPOCETAK, ospromDATKNJIZENJA, ospromSTATUSKNJ, ospromCNALOGA,
        ospromRBR, ospromSTATUS, ospromDOKUMENT, ospromOPIS, ospromCPAR, ospromMJESECP, ospromSALDO, ospromOJINVBRB, ospromUIPRPOR, ospromOLDCORG,
        ospromOSNOVICA, ospromISPRAVAK});
  }

  public void setall() {

    ddl.create("OS_Promjene")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12)
       .addChar("corg2", 12, true)
       .addChar("invbroj", 10, true)
       .addChar("cpromjene", 3)
       .addDate("datpromjene")
       .addFloat("osnduguje", 17, 2)
       .addFloat("osnpotrazuje", 17, 2)
       .addFloat("ispduguje", 17, 2)
       .addFloat("isppotrazuje", 17, 2)
       .addFloat("osnpocetak", 17, 2)
       .addFloat("isppocetak", 17, 2)
       .addDate("datknjizenja")
       .addChar("statusknj", 1, "N")
       .addChar("cnaloga", 30)
       .addInteger("rbr", 6, true)
       .addChar("status", 1, true)
       .addChar("dokument", 50)
       .addChar("opis", 100)
       .addInteger("cpar", 6)
       .addChar("mjesecp", 2)
       .addFloat("saldo", 17, 2)
       .addChar("ojinvbrb", 1)
       .addFloat("uiprpor", 17, 2)
       .addChar("oldcorg", 12)
       .addFloat("osnovica", 17, 2)
       .addFloat("ispravak", 17, 2)
       .addPrimaryKey("corg2,invbroj,rbr,status");


    Naziv = "OS_Promjene";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"invbroj", "rbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
