/****license*****************************************************************
**   file: Ugovori.java
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


public class Ugovori extends KreirDrop {

  private static Ugovori Ugovoriclass;
  
  QueryDataSet Ugovori = new raDataSet();
  QueryDataSet UgovoriAktiv = new raDataSet();
  public static Ugovori getDataModule() {
    if (Ugovoriclass == null) {
      Ugovoriclass = new Ugovori();
    }
    return Ugovoriclass;
  }

  public QueryDataSet getQueryDataSet() {
    return Ugovori;
  }
  
  public QueryDataSet getAktiv() {
    return UgovoriAktiv;
  }
  public Ugovori() {
    try {
      modules.put(this.getClass().getName(), this);
      initModule();
      createFilteredDataSet(UgovoriAktiv, "aktiv = 'D'");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}
//
//import com.borland.dx.dataset.*;
//import com.borland.dx.sql.dataset.*;
//
//public class Ugovori extends KreirDrop implements DataModule {
//
//  private static Ugovori ugovoriclass;
//  dM dm  = dM.getDataModule();
//  QueryDataSet ugovori = new raDataSet();
//  QueryDataSet ugovoriaktiv = new raDataSet();
//  Column ugovoriLOKK = new Column();
//  Column ugovoriAKTIV = new Column();
//  Column ugovoriCUGOVOR = new Column();
//  Column ugovoriCPAR = new Column();
//  Column ugovoriPJ = new Column();
//  Column ugovoriULOGA = new Column();
//  Column ugovoriDATUGOVOR = new Column();
//  Column ugovoriDANIDOSP = new Column();
//  Column ugovoriDANIKRROKPL = new Column();
//  Column ugovoriDANICASSCO = new Column();
//  Column ugovoriPOSTOCASSCO = new Column();
//  Column ugovoriIZNOS = new Column();
//  Column ugovoriOPIS = new Column();
//  Column ugovoriVRSTA = new Column();
//  Column ugovoriCVRUGO = new Column();
//  Column ugovoriTEXTFAK = new Column();
//  Column ugovoriCNAP = new Column();
//  Column ugovoriCORG = new Column();
//  Column ugovoriCFRA = new Column();
//  Column ugovoriCNACPL = new Column();
//  Column ugovoriCNAMJ = new Column();
//  Column ugovoriCNAC = new Column();  
//
//  public static Ugovori getDataModule() {
//    if (ugovoriclass == null) {
//      ugovoriclass = new Ugovori();
//    }
//    return ugovoriclass;
//  }
//
//  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
//    return ugovori;
//  }
//
//  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
//    return ugovoriaktiv;
//  }
//
//  public Ugovori() {
//    try {
//      modules.put(this.getClass().getName(), this);
//      jbInit();
//    }
//    catch(Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  public void setall(){
//
//    ddl.create("ugovori")
//       .addChar("lokk", 1, "N")
//       .addChar("aktiv", 1, "D")
//       .addChar("cugovor", 13, true)
//       .addInteger("cpar", 6)
//       .addInteger("pj", 6)
//       .addChar("uloga", 1)
//       .addDate("datugovor")
//       .addShort("danidosp", 3)
//       .addShort("danikrrokpl", 3)
//       .addShort("danicassco", 3)
//       .addFloat("postocassco", 5, 2)
//       .addFloat("iznos", 17, 2)
//       .addChar("opis", 50)
//       .addChar("vrsta", 1)
//       .addChar("cvrugo", 13)
//       .addChar("textfak", 250)
//       .addChar("cnap", 3)
//       .addChar("corg", 12)
//       .addChar("cfra", 3)
//       .addChar("cnacpl", 3)
//       .addChar("cnamj", 3)
//       .addChar("cnac", 3)
//       .addPrimaryKey("cugovor");
//
//    Naziv="Ugovori";
//
//    SqlDefTabela = ddl.getCreateTableString();
//
//    String[] idx = new String[] {"cpar","pj"};
//    String[] uidx = new String[] {};
//    DefIndex = ddl.getIndices(idx, uidx);
//    NaziviIdx = ddl.getIndexNames(idx, uidx);
//
//    /*
//    DefIndex= new String[] {
//              CommonTable.SqlDefIndex+"igrupeuseralokk on grupeusera (lokk)",
//              CommonTable.SqlDefIndex+"igrupeuseraaktiv on grupeusera (aktiv)",
//              CommonTable.SqlDefUniqueIndex+"igrupeuseracusera on grupeusera (cgrupeusera)"};
//
//    NaziviIdx=new String[]{"iuserilokk","iuseriaktiv","iusericusera","iusericgrpu"};
//*/
//  }
//  private void jbInit() throws Exception {
//    ugovoriCORG.setCaption("OJ");
//    ugovoriCORG.setColumnName("CORG");
//    ugovoriCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriCORG.setPrecision(12);
//    ugovoriCORG.setTableName("UGOVORI");
//    ugovoriCORG.setServerColumnName("CORG");
//    ugovoriCORG.setSqlType(1);
//
//    ugovoriTEXTFAK.setCaption("Tekst fakture");
//    ugovoriTEXTFAK.setColumnName("TEXTFAK");
//    ugovoriTEXTFAK.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriTEXTFAK.setPrecision(250);
//    ugovoriTEXTFAK.setTableName("UGOVORI");
//    ugovoriTEXTFAK.setServerColumnName("TEXTFAK");
//    ugovoriTEXTFAK.setWidth(30);
//    ugovoriTEXTFAK.setSqlType(1);
//
//    ugovoriCNAP.setCaption("Napomena");
//    ugovoriCNAP.setColumnName("CNAP");
//    ugovoriCNAP.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriCNAP.setPrecision(3);
//    ugovoriCNAP.setTableName("UGOVORI");
//    ugovoriCNAP.setWidth(5);
//    ugovoriCNAP.setSqlType(1);
//    ugovoriCNAP.setServerColumnName("CNAP");
//
//    ugovoriCVRUGO.setCaption("Vrsta ugovora");
//    ugovoriCVRUGO.setColumnName("CVRUGO");
//    ugovoriCVRUGO.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriCVRUGO.setPrecision(13);
//    ugovoriCVRUGO.setTableName("UGOVORI");
//    ugovoriCVRUGO.setServerColumnName("CVRUGO");
//    ugovoriCVRUGO.setSqlType(1);
//
//    ugovoriVRSTA.setCaption("Vrsta");
//    ugovoriVRSTA.setColumnName("VRSTA");
//    ugovoriVRSTA.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriVRSTA.setPrecision(1);
//    ugovoriVRSTA.setTableName("UGOVORI");
//    ugovoriVRSTA.setServerColumnName("VRSTA");
//    ugovoriVRSTA.setSqlType(1);
//    ugovoriOPIS.setCaption("Opis");
//    ugovoriOPIS.setColumnName("OPIS");
//    ugovoriOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriOPIS.setPrecision(50);
//    ugovoriOPIS.setTableName("UGOVORI");
//    ugovoriOPIS.setWidth(20);
//    ugovoriOPIS.setServerColumnName("OPIS");
//    ugovoriOPIS.setSqlType(1);
//    ugovoriIZNOS.setCaption("Iznos");
//    ugovoriIZNOS.setColumnName("IZNOS");
//    ugovoriIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    ugovoriIZNOS.setPrecision(17);
//    ugovoriIZNOS.setScale(2);
//    ugovoriIZNOS.setDisplayMask("###,###,##0.00");
//    ugovoriIZNOS.setDefault("0");
//    ugovoriIZNOS.setServerColumnName("IZNOS");
//    ugovoriIZNOS.setSqlType(2);
//    ugovoriIZNOS.setTableName("UGOVORI");
//    ugovoriPOSTOCASSCO.setCaption("Posto casa sconto");
//    ugovoriPOSTOCASSCO.setColumnName("POSTOCASSCO");
//    ugovoriPOSTOCASSCO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
//    ugovoriPOSTOCASSCO.setPrecision(5);
//    ugovoriPOSTOCASSCO.setScale(2);
//    ugovoriPOSTOCASSCO.setDisplayMask("###,###,##0.00");
//    ugovoriPOSTOCASSCO.setDefault("0");
//    ugovoriPOSTOCASSCO.setServerColumnName("POSTOCASSCO");
//    ugovoriPOSTOCASSCO.setSqlType(2);
//    ugovoriPOSTOCASSCO.setTableName("UGOVORI");
//    ugovoriDANICASSCO.setCaption("Dani casa sconto");
//    ugovoriDANICASSCO.setColumnName("DANICASSCO");
//    ugovoriDANICASSCO.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    ugovoriDANICASSCO.setPrecision(3);
//    ugovoriDANICASSCO.setServerColumnName("DANICASSCO");
//    ugovoriDANICASSCO.setDefault("0");
//    ugovoriDANICASSCO.setSqlType(5);
//    ugovoriDANICASSCO.setTableName("UGOVORI");
//    ugovoriDANIKRROKPL.setCaption("Rok pla\u0107anja");
//    ugovoriDANIKRROKPL.setColumnName("DANIKRROKPL");
//    ugovoriDANIKRROKPL.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    ugovoriDANIKRROKPL.setPrecision(3);
//    ugovoriDANIKRROKPL.setDefault("0");
//    ugovoriDANIKRROKPL.setServerColumnName("DANIKRROKPL");
//    ugovoriDANIKRROKPL.setSqlType(5);
//    ugovoriDANIKRROKPL.setTableName("UGOVORI");
//    ugovoriDANIDOSP.setCaption("Dani dospije\u0107a");
//    ugovoriDANIDOSP.setColumnName("DANIDOSP");
//    ugovoriDANIDOSP.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    ugovoriDANIDOSP.setPrecision(3);
//    ugovoriDANIDOSP.setServerColumnName("DANIDOSP");
//    ugovoriDANIDOSP.setDefault("0");
//    ugovoriDANIDOSP.setSqlType(5);
//    ugovoriDANIDOSP.setTableName("UGOVORI");
//    ugovoriDATUGOVOR.setCaption("Datum ugovora");
//    ugovoriDATUGOVOR.setColumnName("DATUGOVOR");
//    ugovoriDATUGOVOR.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
////    ugovoriDATUGOVOR.setEditMask("dd-MM-yyyy");
//    ugovoriDATUGOVOR.setDisplayMask("dd-MM-yyyy");
//    ugovoriDATUGOVOR.setWidth(10);
//    ugovoriDATUGOVOR.setServerColumnName("DATUGOVOR");
//    ugovoriDATUGOVOR.setSqlType(93);
//    ugovoriDATUGOVOR.setTableName("UGOVORI");
//    ugovoriULOGA.setColumnName("ULOGA");
//    ugovoriULOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriULOGA.setPrecision(1);
//    ugovoriULOGA.setWidth(3);
//    ugovoriULOGA.setServerColumnName("ULOGA");
//    ugovoriULOGA.setSqlType(1);
//    ugovoriULOGA.setTableName("UGOVORI");
//    ugovoriCPAR.setCaption("Partner");
//    ugovoriCPAR.setColumnName("CPAR");
//    ugovoriCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
//    ugovoriCPAR.setPrecision(6);
//    ugovoriCPAR.setServerColumnName("CPAR");
//    ugovoriCPAR.setSqlType(4);
//    ugovoriCPAR.setWidth(5);
//    ugovoriCPAR.setTableName("UGOVORI");
//    ugovoriPJ.setCaption("Poslovna jedinica");
//    ugovoriPJ.setColumnName("PJ");
//    ugovoriPJ.setDataType(com.borland.dx.dataset.Variant.INT);
//    ugovoriPJ.setTableName("UGOVORI");
//    ugovoriPJ.setServerColumnName("PJ");
//    ugovoriPJ.setSqlType(4);
//    ugovoriPJ.setWidth(4);
//    ugovoriCUGOVOR.setCaption("Oznaka");
//    ugovoriCUGOVOR.setColumnName("CUGOVOR");
//    ugovoriCUGOVOR.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriCUGOVOR.setPrecision(13);
//    ugovoriCUGOVOR.setRowId(true);
//    ugovoriCUGOVOR.setServerColumnName("CUGOVOR");
//    ugovoriCUGOVOR.setSqlType(1);
//    ugovoriCUGOVOR.setWidth(6);
//    ugovoriCUGOVOR.setTableName("UGOVORI");
//    ugovoriAKTIV.setCaption("Aktivan");
//    ugovoriAKTIV.setColumnName("AKTIV");
//    ugovoriAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriAKTIV.setDefault("D");
//    ugovoriAKTIV.setPrecision(1);
//    ugovoriAKTIV.setWidth(6);
//    ugovoriAKTIV.setServerColumnName("AKTIV");
//    ugovoriAKTIV.setSqlType(1);
//    ugovoriAKTIV.setTableName("UGOVORI");
//    ugovoriLOKK.setCaption("LOKK");
//    ugovoriLOKK.setColumnName("LOKK");
//    ugovoriLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriLOKK.setDefault("N");
//    ugovoriLOKK.setPrecision(1);
//    ugovoriLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
//    ugovoriLOKK.setServerColumnName("LOKK");
//    ugovoriLOKK.setSqlType(1);
//    ugovoriLOKK.setTableName("UGOVORI");
//    ugovoriCNAC.setCaption("Na\u010Din otpreme");
//    ugovoriCNAC.setColumnName("CNAC");
//    ugovoriCNAC.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriCNAC.setPrecision(3);
//    ugovoriCNAC.setTableName("UGOVORI");
//    ugovoriCNAC.setSqlType(1);
//    ugovoriCNAC.setServerColumnName("CNAC");
//    ugovoriCNAMJ.setCaption("Namjena robe");
//    ugovoriCNAMJ.setColumnName("CNAMJ");
//    ugovoriCNAMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriCNAMJ.setPrecision(3);
//    ugovoriCNAMJ.setTableName("UGOVORI");
//    ugovoriCNAMJ.setSqlType(1);
//    ugovoriCNAMJ.setServerColumnName("CNAMJ");
//    ugovoriCNACPL.setCaption("Na\\u10din pla\u0107anja");
//    ugovoriCNACPL.setColumnName("CNACPL");
//    ugovoriCNACPL.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriCNACPL.setPrecision(3);
//    ugovoriCNACPL.setTableName("UGOVORI");
//    ugovoriCNACPL.setSqlType(1);
//    ugovoriCNACPL.setServerColumnName("CNACPL");
//    ugovoriCFRA.setCaption("Paritet");
//    ugovoriCFRA.setColumnName("CFRA");
//    ugovoriCFRA.setDataType(com.borland.dx.dataset.Variant.STRING);
//    ugovoriCFRA.setPrecision(3);
//    ugovoriCFRA.setTableName("UGOVORI");
//    ugovoriCFRA.setSqlType(1);
//    ugovoriCFRA.setServerColumnName("CFRA");
//    
//    
//    
//    
//      ugovori.setResolver(dm.getQresolver());
//    ugovori.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select * from ugovori", null, true, Load.ALL));
// setColumns(new Column[] {ugovoriLOKK, ugovoriAKTIV, ugovoriCUGOVOR, ugovoriCPAR, ugovoriPJ, ugovoriULOGA, ugovoriDATUGOVOR,
//        ugovoriDANIDOSP, ugovoriDANIKRROKPL, ugovoriDANICASSCO, ugovoriPOSTOCASSCO, ugovoriIZNOS, ugovoriOPIS, ugovoriVRSTA, ugovoriCVRUGO,
//        ugovoriTEXTFAK, ugovoriCNAP, ugovoriCORG,ugovoriCFRA, ugovoriCNACPL, ugovoriCNAMJ, ugovoriCNAC});
//
//    createFilteredDataSet(ugovoriaktiv, "aktiv = 'D'");
//  }
//}
