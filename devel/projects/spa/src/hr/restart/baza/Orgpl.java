/****license*****************************************************************
**   file: Orgpl.java
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



public class Orgpl extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Orgpl Orgplclass;

  QueryDataSet orgpl = new QueryDataSet();

  Column orgplLOKK = new Column();
  Column orgplAKTIV = new Column();
  Column orgplCORG = new Column();
  Column orgplCOPCINE = new Column();
  Column orgplNACOBRS = new Column();
  Column orgplNACOBRB = new Column();
  Column orgplSATIMJ = new Column();
  Column orgplOSNKOEF = new Column();
  Column orgplSATNORMA = new Column();
  Column orgplOOZO = new Column();
  Column orgplPODRUREDZO = new Column();
  Column orgplREGBRMIO = new Column();
  Column orgplREGBRZO = new Column();
  Column orgplPODMATBR = new Column();
  Column orgplCGRORG = new Column();
  Column orgplGODOBR = new Column();
  Column orgplMJOBR = new Column();
  Column orgplRBROBR = new Column();
  Column orgplDATUMISPL = new Column();
  Column orgplBROJDANA = new Column();
  Column orgplSTOPAK = new Column();
  Column orgplRSZ = new Column();
  Column orgplRSIND = new Column();
  Column orgplPARAMETRI = new Column();

  public static Orgpl getDataModule() {
    if (Orgplclass == null) {
      Orgplclass = new Orgpl();
    }
    return Orgplclass;
  }

  public QueryDataSet getQueryDataSet() {
    return orgpl;
  }

  public Orgpl() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    orgplLOKK.setCaption("Status zauzetosti");
    orgplLOKK.setColumnName("LOKK");
    orgplLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplLOKK.setPrecision(1);
    orgplLOKK.setTableName("ORGPL");
    orgplLOKK.setServerColumnName("LOKK");
    orgplLOKK.setSqlType(1);
    orgplLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    orgplLOKK.setDefault("N");
    orgplAKTIV.setCaption("Aktivan - neaktivan");
    orgplAKTIV.setColumnName("AKTIV");
    orgplAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplAKTIV.setPrecision(1);
    orgplAKTIV.setTableName("ORGPL");
    orgplAKTIV.setServerColumnName("AKTIV");
    orgplAKTIV.setSqlType(1);
    orgplAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    orgplAKTIV.setDefault("D");
    orgplCORG.setCaption("Org. Jedinica");
    orgplCORG.setColumnName("CORG");
    orgplCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplCORG.setPrecision(12);
    orgplCORG.setRowId(true);
    orgplCORG.setTableName("ORGPL");
    orgplCORG.setServerColumnName("CORG");
    orgplCORG.setSqlType(1);
    orgplCOPCINE.setCaption("Op\u0107ina sjedišta");
    orgplCOPCINE.setColumnName("COPCINE");
    orgplCOPCINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplCOPCINE.setPrecision(3);
    orgplCOPCINE.setTableName("ORGPL");
    orgplCOPCINE.setServerColumnName("COPCINE");
    orgplCOPCINE.setSqlType(1);
    orgplNACOBRS.setCaption("Na\u010Din obra\u010Duna satnice");
    orgplNACOBRS.setColumnName("NACOBRS");
    orgplNACOBRS.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplNACOBRS.setPrecision(1);
    orgplNACOBRS.setTableName("ORGPL");
    orgplNACOBRS.setServerColumnName("NACOBRS");
    orgplNACOBRS.setSqlType(1);
    orgplNACOBRS.setDefault("1");
    orgplNACOBRB.setCaption("Na\u010Din obra\u010Duna bruta");
    orgplNACOBRB.setColumnName("NACOBRB");
    orgplNACOBRB.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplNACOBRB.setPrecision(1);
    orgplNACOBRB.setTableName("ORGPL");
    orgplNACOBRB.setServerColumnName("NACOBRB");
    orgplNACOBRB.setSqlType(1);
    orgplNACOBRB.setDefault("1");
    orgplSATIMJ.setCaption("Sati za fiksnu satnicu");
    orgplSATIMJ.setColumnName("SATIMJ");
    orgplSATIMJ.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    orgplSATIMJ.setPrecision(17);
    orgplSATIMJ.setScale(2);
    orgplSATIMJ.setDisplayMask("###,###,##0.00");
    orgplSATIMJ.setDefault("0");
    orgplSATIMJ.setTableName("ORGPL");
    orgplSATIMJ.setServerColumnName("SATIMJ");
    orgplSATIMJ.setSqlType(2);
    orgplSATIMJ.setDefault("182");
    orgplOSNKOEF.setCaption("Osnovica za primjenu koeficijenta");
    orgplOSNKOEF.setColumnName("OSNKOEF");
    orgplOSNKOEF.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    orgplOSNKOEF.setPrecision(17);
    orgplOSNKOEF.setScale(6);
    orgplOSNKOEF.setDisplayMask("###,###,##0.000000");
    orgplOSNKOEF.setDefault("0");
    orgplOSNKOEF.setTableName("ORGPL");
    orgplOSNKOEF.setServerColumnName("OSNKOEF");
    orgplOSNKOEF.setSqlType(2);
    orgplOSNKOEF.setDefault("0");
    orgplSATNORMA.setCaption("Satnica za rad po normi");
    orgplSATNORMA.setColumnName("SATNORMA");
    orgplSATNORMA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    orgplSATNORMA.setPrecision(17);
    orgplSATNORMA.setScale(2);
    orgplSATNORMA.setDisplayMask("###,###,##0.00");
    orgplSATNORMA.setDefault("0");
    orgplSATNORMA.setTableName("ORGPL");
    orgplSATNORMA.setServerColumnName("SATNORMA");
    orgplSATNORMA.setSqlType(2);
    orgplSATNORMA.setDefault("0");
    orgplOOZO.setCaption("Osnova osiguranja - zdravstvena kartica");
    orgplOOZO.setColumnName("OOZO");
    orgplOOZO.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplOOZO.setPrecision(6);
    orgplOOZO.setTableName("ORGPL");
    orgplOOZO.setServerColumnName("OOZO");
    orgplOOZO.setSqlType(1);
    orgplPODRUREDZO.setCaption("Podru\u010Dni ured - zdravstvena kartica");
    orgplPODRUREDZO.setColumnName("PODRUREDZO");
    orgplPODRUREDZO.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplPODRUREDZO.setPrecision(3);
    orgplPODRUREDZO.setTableName("ORGPL");
    orgplPODRUREDZO.setServerColumnName("PODRUREDZO");
    orgplPODRUREDZO.setSqlType(1);
    orgplREGBRMIO.setCaption("Registarski broj MIO");
    orgplREGBRMIO.setColumnName("REGBRMIO");
    orgplREGBRMIO.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplREGBRMIO.setPrecision(15);
    orgplREGBRMIO.setTableName("ORGPL");
    orgplREGBRMIO.setServerColumnName("REGBRMIO");
    orgplREGBRMIO.setSqlType(1);
    orgplREGBRZO.setCaption("Registarski broj ZO");
    orgplREGBRZO.setColumnName("REGBRZO");
    orgplREGBRZO.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplREGBRZO.setPrecision(15);
    orgplREGBRZO.setTableName("ORGPL");
    orgplREGBRZO.setServerColumnName("REGBRZO");
    orgplREGBRZO.setSqlType(1);
    orgplPODMATBR.setCaption("Podbroj mati\u010Dnom broju poduze\u0107a");
    orgplPODMATBR.setColumnName("PODMATBR");
    orgplPODMATBR.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplPODMATBR.setPrecision(4);
    orgplPODMATBR.setTableName("ORGPL");
    orgplPODMATBR.setServerColumnName("PODMATBR");
    orgplPODMATBR.setSqlType(1);
    orgplCGRORG.setCaption("Oznaka grupe org. Jedinica");
    orgplCGRORG.setColumnName("CGRORG");
    orgplCGRORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplCGRORG.setPrecision(5);
    orgplCGRORG.setTableName("ORGPL");
    orgplCGRORG.setServerColumnName("CGRORG");
    orgplCGRORG.setSqlType(1);
    orgplGODOBR.setCaption("Godina obra\u010Duna");
    orgplGODOBR.setColumnName("GODOBR");
    orgplGODOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    orgplGODOBR.setPrecision(4);
    orgplGODOBR.setTableName("ORGPL");
    orgplGODOBR.setServerColumnName("GODOBR");
    orgplGODOBR.setSqlType(5);
    orgplGODOBR.setWidth(4);
    orgplMJOBR.setCaption("Mjesec obra\u010Duna");
    orgplMJOBR.setColumnName("MJOBR");
    orgplMJOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    orgplMJOBR.setPrecision(2);
    orgplMJOBR.setTableName("ORGPL");
    orgplMJOBR.setServerColumnName("MJOBR");
    orgplMJOBR.setSqlType(5);
    orgplMJOBR.setWidth(2);
    orgplRBROBR.setCaption("Redni broj obra\u010Duna");
    orgplRBROBR.setColumnName("RBROBR");
    orgplRBROBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    orgplRBROBR.setPrecision(3);
    orgplRBROBR.setTableName("ORGPL");
    orgplRBROBR.setServerColumnName("RBROBR");
    orgplRBROBR.setSqlType(5);
    orgplRBROBR.setWidth(3);
    orgplDATUMISPL.setCaption("Datum isplate");
    orgplDATUMISPL.setColumnName("DATUMISPL");
    orgplDATUMISPL.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    orgplDATUMISPL.setPrecision(8);
    orgplDATUMISPL.setDisplayMask("dd-MM-yyyy");
//    orgplDATUMISPL.setEditMask("dd-MM-yyyy");
    orgplDATUMISPL.setTableName("ORGPL");
    orgplDATUMISPL.setServerColumnName("DATUMISPL");
    orgplDATUMISPL.setSqlType(93);
    orgplDATUMISPL.setWidth(10);
    orgplBROJDANA.setCaption("Broj radnih dana u mjesecu");
    orgplBROJDANA.setColumnName("BROJDANA");
    orgplBROJDANA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    orgplBROJDANA.setPrecision(2);
    orgplBROJDANA.setTableName("ORGPL");
    orgplBROJDANA.setServerColumnName("BROJDANA");
    orgplBROJDANA.setSqlType(5);
    orgplBROJDANA.setWidth(2);
    orgplSTOPAK.setCaption("Stopa akontacije");
    orgplSTOPAK.setColumnName("STOPAK");
    orgplSTOPAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    orgplSTOPAK.setPrecision(17);
    orgplSTOPAK.setScale(2);
    orgplSTOPAK.setDisplayMask("###,###,##0.00");
    orgplSTOPAK.setDefault("0");
    orgplSTOPAK.setTableName("ORGPL");
    orgplSTOPAK.setServerColumnName("STOPAK");
    orgplSTOPAK.setSqlType(2);
    orgplSTOPAK.setDefault("0");
    orgplRSZ.setCaption("Oznaka zdravstvenog osiguranja");
    orgplRSZ.setColumnName("RSZ");
    orgplRSZ.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplRSZ.setPrecision(5);
    orgplRSZ.setTableName("ORGPL");
    orgplRSZ.setServerColumnName("RSZ");
    orgplRSZ.setSqlType(1);
    orgplRSZ.setDefault("1");
    orgplRSIND.setCaption("Identifikator RS");
    orgplRSIND.setColumnName("RSIND");
    orgplRSIND.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplRSIND.setPrecision(4);
    orgplRSIND.setTableName("ORGPL");
    orgplRSIND.setServerColumnName("RSIND");
    orgplRSIND.setSqlType(1);
    orgplPARAMETRI.setCaption("Parametri");
    orgplPARAMETRI.setColumnName("PARAMETRI");
    orgplPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgplPARAMETRI.setPrecision(20);
    orgplPARAMETRI.setTableName("ORGPL");
    orgplPARAMETRI.setServerColumnName("PARAMETRI");
    orgplPARAMETRI.setSqlType(1);
    orgplPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    orgpl.setResolver(dm.getQresolver());
    orgpl.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Orgpl", null, true, Load.ALL));
 setColumns(new Column[] {orgplLOKK, orgplAKTIV, orgplCORG, orgplCOPCINE, orgplNACOBRS, orgplNACOBRB, orgplSATIMJ, orgplOSNKOEF, orgplSATNORMA,
        orgplOOZO, orgplPODRUREDZO, orgplREGBRMIO, orgplREGBRZO, orgplPODMATBR, orgplCGRORG, orgplGODOBR, orgplMJOBR, orgplRBROBR, orgplDATUMISPL,
        orgplBROJDANA, orgplSTOPAK, orgplRSZ, orgplRSIND, orgplPARAMETRI});
  }

  public void setall() {

    ddl.create("Orgpl")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addChar("copcine", 3)
       .addChar("nacobrs", 1, "1")
       .addChar("nacobrb", 1, "1")
       .addFloat("satimj", 17, 2)
       .addFloat("osnkoef", 17, 6)
       .addFloat("satnorma", 17, 2)
       .addChar("oozo", 6)
       .addChar("podruredzo", 3)
       .addChar("regbrmio", 15)
       .addChar("regbrzo", 15)
       .addChar("podmatbr", 4)
       .addChar("cgrorg", 5)
       .addShort("godobr", 4)
       .addShort("mjobr", 2)
       .addShort("rbrobr", 3)
       .addDate("datumispl")
       .addShort("brojdana", 2)
       .addFloat("stopak", 17, 2)
       .addChar("rsz", 5, "1")
       .addChar("rsind", 4)
       .addChar("parametri", 20)
       .addPrimaryKey("corg");


    Naziv = "Orgpl";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
