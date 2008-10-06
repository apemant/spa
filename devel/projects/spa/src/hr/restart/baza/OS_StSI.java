/****license*****************************************************************
**   file: OS_StSI.java
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



public class OS_StSI extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_StSI OS_StSIclass;

  QueryDataSet osst = new QueryDataSet();

  Column osstLOKK = new Column();
  Column osstAKTIV = new Column();
  Column osstCORG = new Column();
  Column osstCORG2 = new Column();
  Column osstINVBROJ = new Column();
  Column osstCPROMJENE = new Column();
  Column osstDATPROMJENE = new Column();
  Column osstOSNDUGUJE = new Column();
  Column osstOSNPOTRAZUJE = new Column();
  Column osstISPDUGUJE = new Column();
  Column osstISPPOTRAZUJE = new Column();
  Column osstOSNPOCETAK = new Column();
  Column osstISPPOCETAK = new Column();
  Column osstDATKNJIZENJA = new Column();
  Column osstCNALOGA = new Column();
  Column osstRBR = new Column();
  Column osstSTATUS = new Column();
  Column osstDOKUMENT = new Column();
  Column osstOPIS = new Column();
  Column osstCPAR = new Column();
  Column osstMJESECP = new Column();
  Column osstSALDO = new Column();
  Column osstOJINVBRB = new Column();

  public static OS_StSI getDataModule() {
    if (OS_StSIclass == null) {
      OS_StSIclass = new OS_StSI();
    }
    return OS_StSIclass;
  }

  public QueryDataSet getQueryDataSet() {
    return osst;
  }

  public OS_StSI() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    osstLOKK.setCaption("Status zauzetosti");
    osstLOKK.setColumnName("LOKK");
    osstLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstLOKK.setPrecision(1);
    osstLOKK.setTableName("OS_STSI");
    osstLOKK.setServerColumnName("LOKK");
    osstLOKK.setSqlType(1);
    osstLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osstLOKK.setDefault("N");
    osstAKTIV.setCaption("Aktivan - neaktivan");
    osstAKTIV.setColumnName("AKTIV");
    osstAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstAKTIV.setPrecision(1);
    osstAKTIV.setTableName("OS_STSI");
    osstAKTIV.setServerColumnName("AKTIV");
    osstAKTIV.setSqlType(1);
    osstAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osstAKTIV.setDefault("D");
    osstCORG.setCaption("Knjigovodstvo");
    osstCORG.setColumnName("CORG");
    osstCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstCORG.setPrecision(12);
    osstCORG.setTableName("OS_STSI");
    osstCORG.setServerColumnName("CORG");
    osstCORG.setSqlType(1);
    osstCORG2.setCaption("OJ");
    osstCORG2.setColumnName("CORG2");
    osstCORG2.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstCORG2.setPrecision(12);
    osstCORG2.setRowId(true);
    osstCORG2.setTableName("OS_STSI");
    osstCORG2.setServerColumnName("CORG2");
    osstCORG2.setSqlType(1);
    osstCORG2.setWidth(6);
    osstINVBROJ.setCaption("IBroj");
    osstINVBROJ.setColumnName("INVBROJ");
    osstINVBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstINVBROJ.setPrecision(10);
    osstINVBROJ.setRowId(true);
    osstINVBROJ.setTableName("OS_STSI");
    osstINVBROJ.setServerColumnName("INVBROJ");
    osstINVBROJ.setSqlType(1);
    osstCPROMJENE.setCaption("VP");
    osstCPROMJENE.setColumnName("CPROMJENE");
    osstCPROMJENE.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstCPROMJENE.setPrecision(3);
    osstCPROMJENE.setTableName("OS_STSI");
    osstCPROMJENE.setServerColumnName("CPROMJENE");
    osstCPROMJENE.setSqlType(1);
    osstCPROMJENE.setWidth(4);
    osstDATPROMJENE.setCaption("Datum");
    osstDATPROMJENE.setColumnName("DATPROMJENE");
    osstDATPROMJENE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osstDATPROMJENE.setPrecision(8);
    osstDATPROMJENE.setDisplayMask("dd-MM-yyyy");
//    osstDATPROMJENE.setEditMask("dd-MM-yyyy");
    osstDATPROMJENE.setTableName("OS_STSI");
    osstDATPROMJENE.setServerColumnName("DATPROMJENE");
    osstDATPROMJENE.setSqlType(93);
    osstDATPROMJENE.setWidth(10);
    osstOSNDUGUJE.setCaption("OsnovicaD");
    osstOSNDUGUJE.setColumnName("OSNDUGUJE");
    osstOSNDUGUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osstOSNDUGUJE.setPrecision(17);
    osstOSNDUGUJE.setScale(2);
    osstOSNDUGUJE.setDisplayMask("###,###,##0.00");
    osstOSNDUGUJE.setDefault("0");
    osstOSNDUGUJE.setTableName("OS_STSI");
    osstOSNDUGUJE.setServerColumnName("OSNDUGUJE");
    osstOSNDUGUJE.setSqlType(2);
    osstOSNDUGUJE.setDefault("0");
    osstOSNPOTRAZUJE.setCaption("OsnovicaP");
    osstOSNPOTRAZUJE.setColumnName("OSNPOTRAZUJE");
    osstOSNPOTRAZUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osstOSNPOTRAZUJE.setPrecision(17);
    osstOSNPOTRAZUJE.setScale(2);
    osstOSNPOTRAZUJE.setDisplayMask("###,###,##0.00");
    osstOSNPOTRAZUJE.setDefault("0");
    osstOSNPOTRAZUJE.setTableName("OS_STSI");
    osstOSNPOTRAZUJE.setServerColumnName("OSNPOTRAZUJE");
    osstOSNPOTRAZUJE.setSqlType(2);
    osstOSNPOTRAZUJE.setDefault("0");
    osstISPDUGUJE.setCaption("IspravakD");
    osstISPDUGUJE.setColumnName("ISPDUGUJE");
    osstISPDUGUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osstISPDUGUJE.setPrecision(17);
    osstISPDUGUJE.setScale(2);
    osstISPDUGUJE.setDisplayMask("###,###,##0.00");
    osstISPDUGUJE.setDefault("0");
    osstISPDUGUJE.setTableName("OS_STSI");
    osstISPDUGUJE.setServerColumnName("ISPDUGUJE");
    osstISPDUGUJE.setSqlType(2);
    osstISPDUGUJE.setDefault("0");
    osstISPPOTRAZUJE.setCaption("IspravakP");
    osstISPPOTRAZUJE.setColumnName("ISPPOTRAZUJE");
    osstISPPOTRAZUJE.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osstISPPOTRAZUJE.setPrecision(17);
    osstISPPOTRAZUJE.setScale(2);
    osstISPPOTRAZUJE.setDisplayMask("###,###,##0.00");
    osstISPPOTRAZUJE.setDefault("0");
    osstISPPOTRAZUJE.setTableName("OS_STSI");
    osstISPPOTRAZUJE.setServerColumnName("ISPPOTRAZUJE");
    osstISPPOTRAZUJE.setSqlType(2);
    osstISPPOTRAZUJE.setDefault("0");
    osstOSNPOCETAK.setCaption("Osnovica po\u010Detak");
    osstOSNPOCETAK.setColumnName("OSNPOCETAK");
    osstOSNPOCETAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osstOSNPOCETAK.setPrecision(17);
    osstOSNPOCETAK.setScale(2);
    osstOSNPOCETAK.setDisplayMask("###,###,##0.00");
    osstOSNPOCETAK.setDefault("0");
    osstOSNPOCETAK.setTableName("OS_STSI");
    osstOSNPOCETAK.setServerColumnName("OSNPOCETAK");
    osstOSNPOCETAK.setSqlType(2);
    osstOSNPOCETAK.setDefault("0");
    osstISPPOCETAK.setCaption("Ispravak po\u010Detak");
    osstISPPOCETAK.setColumnName("ISPPOCETAK");
    osstISPPOCETAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osstISPPOCETAK.setPrecision(17);
    osstISPPOCETAK.setScale(2);
    osstISPPOCETAK.setDisplayMask("###,###,##0.00");
    osstISPPOCETAK.setDefault("0");
    osstISPPOCETAK.setTableName("OS_STSI");
    osstISPPOCETAK.setServerColumnName("ISPPOCETAK");
    osstISPPOCETAK.setSqlType(2);
    osstISPPOCETAK.setDefault("0");
    osstDATKNJIZENJA.setCaption("Datum knjiženja");
    osstDATKNJIZENJA.setColumnName("DATKNJIZENJA");
    osstDATKNJIZENJA.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    osstDATKNJIZENJA.setPrecision(8);
    osstDATKNJIZENJA.setDisplayMask("dd-MM-yyyy");
//    osstDATKNJIZENJA.setEditMask("dd-MM-yyyy");
    osstDATKNJIZENJA.setTableName("OS_STSI");
    osstDATKNJIZENJA.setServerColumnName("DATKNJIZENJA");
    osstDATKNJIZENJA.setSqlType(93);
    osstDATKNJIZENJA.setWidth(10);
    osstCNALOGA.setCaption("Broj naloga");
    osstCNALOGA.setColumnName("CNALOGA");
    osstCNALOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstCNALOGA.setPrecision(30);
    osstCNALOGA.setTableName("OS_STSI");
    osstCNALOGA.setServerColumnName("CNALOGA");
    osstCNALOGA.setSqlType(1);
    osstCNALOGA.setWidth(30);
    osstRBR.setCaption("Rbr");
    osstRBR.setColumnName("RBR");
    osstRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    osstRBR.setPrecision(6);
    osstRBR.setRowId(true);
    osstRBR.setTableName("OS_STSI");
    osstRBR.setServerColumnName("RBR");
    osstRBR.setSqlType(4);
    osstRBR.setWidth(3);
    osstSTATUS.setCaption("Status");
    osstSTATUS.setColumnName("STATUS");
    osstSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstSTATUS.setPrecision(1);
    osstSTATUS.setRowId(true);
    osstSTATUS.setTableName("OS_STSI");
    osstSTATUS.setServerColumnName("STATUS");
    osstSTATUS.setSqlType(1);
    osstDOKUMENT.setCaption("Dokument");
    osstDOKUMENT.setColumnName("DOKUMENT");
    osstDOKUMENT.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstDOKUMENT.setPrecision(50);
    osstDOKUMENT.setTableName("OS_STSI");
    osstDOKUMENT.setServerColumnName("DOKUMENT");
    osstDOKUMENT.setSqlType(1);
    osstDOKUMENT.setWidth(30);
    osstOPIS.setCaption("Opis");
    osstOPIS.setColumnName("OPIS");
    osstOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstOPIS.setPrecision(100);
    osstOPIS.setTableName("OS_STSI");
    osstOPIS.setServerColumnName("OPIS");
    osstOPIS.setSqlType(1);
    osstOPIS.setWidth(30);
    osstCPAR.setCaption("Partner");
    osstCPAR.setColumnName("CPAR");
    osstCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    osstCPAR.setPrecision(6);
    osstCPAR.setTableName("OS_STSI");
    osstCPAR.setServerColumnName("CPAR");
    osstCPAR.setSqlType(4);
    osstCPAR.setWidth(6);
    osstMJESECP.setCaption("Mjesec");
    osstMJESECP.setColumnName("MJESECP");
    osstMJESECP.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstMJESECP.setPrecision(2);
    osstMJESECP.setTableName("OS_STSI");
    osstMJESECP.setServerColumnName("MJESECP");
    osstMJESECP.setSqlType(1);
    osstSALDO.setCaption("Saldo");
    osstSALDO.setColumnName("SALDO");
    osstSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    osstSALDO.setPrecision(17);
    osstSALDO.setScale(2);
    osstSALDO.setDisplayMask("###,###,##0.00");
    osstSALDO.setDefault("0");
    osstSALDO.setTableName("OS_STSI");
    osstSALDO.setServerColumnName("SALDO");
    osstSALDO.setSqlType(2);
    osstSALDO.setDefault("0");
    osstOJINVBRB.setCaption("Ojin");
    osstOJINVBRB.setColumnName("OJINVBRB");
    osstOJINVBRB.setDataType(com.borland.dx.dataset.Variant.STRING);
    osstOJINVBRB.setPrecision(1);
    osstOJINVBRB.setTableName("OS_STSI");
    osstOJINVBRB.setServerColumnName("OJINVBRB");
    osstOJINVBRB.setSqlType(1);
    osstOJINVBRB.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    osst.setResolver(dm.getQresolver());
    osst.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_StSI", null, true, Load.ALL));
 setColumns(new Column[] {osstLOKK, osstAKTIV, osstCORG, osstCORG2, osstINVBROJ, osstCPROMJENE, osstDATPROMJENE, osstOSNDUGUJE, osstOSNPOTRAZUJE,
        osstISPDUGUJE, osstISPPOTRAZUJE, osstOSNPOCETAK, osstISPPOCETAK, osstDATKNJIZENJA, osstCNALOGA, osstRBR, osstSTATUS, osstDOKUMENT, osstOPIS, osstCPAR,
        osstMJESECP, osstSALDO, osstOJINVBRB});
  }

  public void setall() {

    ddl.create("OS_StSI")
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
       .addChar("cnaloga", 30)
       .addInteger("rbr", 6, true)
       .addChar("status", 1, true)
       .addChar("dokument", 50)
       .addChar("opis", 100)
       .addInteger("cpar", 6)
       .addChar("mjesecp", 2)
       .addFloat("saldo", 17, 2)
       .addChar("ojinvbrb", 1)
       .addPrimaryKey("corg2,invbroj,rbr,status");


    Naziv = "OS_StSI";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"invbroj", "rbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
