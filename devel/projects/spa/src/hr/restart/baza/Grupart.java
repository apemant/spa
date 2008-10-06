/****license*****************************************************************
**   file: Grupart.java
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
import java.util.ResourceBundle;

import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Grupart extends KreirDrop implements DataModule {
  ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Grupart Grupartclass;
  dM dm  = dM.getDataModule();
  QueryDataSet grupart = new raDataSet();
  QueryDataSet grupartaktiv = new raDataSet();
  
  /*Column grupartLOKK = new Column();
  Column grupartAKTIV = new Column();
  Column grupartCGRART = new Column();
  Column grupartCGRARTTPRIP = new Column();
  Column grupartNAZGRART = new Column();
  Column grupartPPOP = new Column();
  Column grupartSSORT = new Column();
  
  Column grupartCARINA = new Column();
  Column grupartTROSARINA = new Column();
  Column grupartKOMADNO = new Column();
  */
  public static Grupart getDataModule() {
    if (Grupartclass == null) {
      Grupartclass = new Grupart();
    }
    return Grupartclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return grupart;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return grupartaktiv;
  }

  public Grupart() {
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
	  /*
	grupartSSORT.setCaption("Sortno polje");
	grupartSSORT.setColumnName("SSORT");
	grupartSSORT.setDataType(com.borland.dx.dataset.Variant.INT);
	grupartSSORT.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
	grupartSSORT.setTableName("GRUPART");
	grupartSSORT.setServerColumnName("SSORT");
	grupartSSORT.setSqlType(4);
	grupartSSORT.setWidth(8);

    grupartCARINA.setCaption("Carina");
    grupartCARINA.setColumnName("CARINA");
    grupartCARINA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    grupartCARINA.setDisplayMask("###,###,##0.00");
    grupartCARINA.setDefault("0");
    grupartCARINA.setPrecision(10);
    grupartCARINA.setScale(2);
    grupartCARINA.setTableName("GRUPART");
    grupartCARINA.setServerColumnName("CARINA");
    grupartCARINA.setSqlType(2);
    

    grupartTROSARINA.setCaption("Trošarina");
    grupartTROSARINA.setColumnName("TROSARINA");
    grupartTROSARINA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    grupartTROSARINA.setDisplayMask("###,###,##0.00");
    grupartTROSARINA.setDefault("0");
    grupartTROSARINA.setPrecision(10);
    grupartTROSARINA.setScale(2);
    grupartTROSARINA.setTableName("GRUPART");
    grupartTROSARINA.setServerColumnName("TROSARINA");
    grupartTROSARINA.setSqlType(2);
    
    
    grupartKOMADNO.setCaption("Komadno");
    grupartKOMADNO.setColumnName("KOMADNO");
    grupartKOMADNO.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupartKOMADNO.setPrecision(1);
    grupartKOMADNO.setDefault("N");
    grupartKOMADNO.setTableName("GRUPART");
    grupartKOMADNO.setWidth(1);
    grupartKOMADNO.setServerColumnName("KOMADNO");
    grupartKOMADNO.setSqlType(1);
    
    
    grupartPPOP.setCaption("Posto popusta");
    grupartPPOP.setColumnName("PPOP");
    grupartPPOP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    grupartPPOP.setDisplayMask("###,###,##0.00");
    grupartPPOP.setDefault("0");
    grupartPPOP.setPrecision(10);
    grupartPPOP.setScale(2);
    grupartPPOP.setTableName("GRUPART");
    grupartPPOP.setServerColumnName("PPOP");
    grupartPPOP.setSqlType(2);
    grupartNAZGRART.setCaption("Naziv");
    grupartNAZGRART.setColumnName("NAZGRART");
    grupartNAZGRART.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupartNAZGRART.setPrecision(50);
    grupartNAZGRART.setTableName("GRUPART");
    grupartNAZGRART.setWidth(30);
    grupartNAZGRART.setServerColumnName("NAZGRART");
    grupartNAZGRART.setSqlType(1);
    grupartCGRARTTPRIP.setCaption("Pripadnost");
    grupartCGRARTTPRIP.setColumnName("CGRARTPRIP");
    grupartCGRARTTPRIP.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupartCGRARTTPRIP.setPrecision(10);
    grupartCGRARTTPRIP.setTableName("GRUPART");
    grupartCGRARTTPRIP.setWidth(5);
    grupartCGRARTTPRIP.setServerColumnName("CGRARTPRIP");
    grupartCGRARTTPRIP.setSqlType(1);
    grupartCGRART.setCaption("\u0160ifra");
    grupartCGRART.setColumnName("CGRART");
    grupartCGRART.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupartCGRART.setPrecision(10);
    grupartCGRART.setRowId(true);
    grupartCGRART.setTableName("GRUPART");
    grupartCGRART.setWidth(5);
    grupartCGRART.setServerColumnName("CGRART");
    grupartCGRART.setSqlType(1);
    grupartAKTIV.setColumnName("AKTIV");
    grupartAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupartAKTIV.setDefault("D");
    grupartAKTIV.setPrecision(1);
    grupartAKTIV.setTableName("GRUPART");
    grupartAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    grupartAKTIV.setServerColumnName("AKTIV");
    grupartAKTIV.setSqlType(1);
    grupartLOKK.setColumnName("LOKK");
    grupartLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    grupartLOKK.setDefault("N");
    grupartLOKK.setPrecision(1);
    grupartLOKK.setTableName("GRUPART");
    grupartLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    grupartLOKK.setServerColumnName("LOKK");
    grupartLOKK.setSqlType(1);
    grupart.setResolver(dm.getQresolver());
    grupart.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * FROM GRUPART", null, true, Load.ALL));
 setColumns(new Column[] {grupartLOKK, grupartAKTIV, grupartCGRART, grupartCGRARTTPRIP, grupartNAZGRART, grupartPPOP, grupartCARINA, grupartTROSARINA, grupartKOMADNO,grupartSSORT});
*/
    createFilteredDataSet(grupartaktiv, "aktiv = 'D'");
  }

// public void setall(){

   /* SqlDefTabela =  "create table Grupart " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N', " + //Status zauzetosti
      "aktiv char(1) CHARACTER SET WIN1250 default 'D', " + // Aktivan-neaktivan
      "cgrart char(10) CHARACTER SET WIN1250 not null,"+ //Grupa artikala podgrupa
      "cgrartprip char(10) CHARACTER SET WIN1250 , " + //Sifra grupe - pripadnosti
      "nazgrart char(50) CHARACTER SET WIN1250 , " + // Naziv grupe artikala
      "ppop numeric(5,2)," +         // Posto popusta
      "Primary Key (cgrart))" ;
*/
 /*   ddl.create("grupart")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cgrart", 10, true)
       .addChar("cgrartprip", 10 )
       .addChar("nazgrart", 50)
       .addFloat("ppop", 5, 2)
       .addFloat("carina", 5, 2)
       .addFloat("trosarina", 5, 2)
       .addChar("komadno", 1)
       .addInteger("ssort", 8)
       .addPrimaryKey("cgrart");

    Naziv="Grupart";
    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);*/
  /*
    NaziviIdx=new String[]{"ilokkgrupart","iaktivgrupart","icgrartgrupart","icgrartprip"};

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Grupart (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Grupart (aktiv)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Grupart (cgrart)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[3] +" on Grupart (cgrartprip)"};
  */
  //}
}
