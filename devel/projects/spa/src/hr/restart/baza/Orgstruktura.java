/****license*****************************************************************
**   file: Orgstruktura.java
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

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.Load;
import com.borland.dx.sql.dataset.QueryDataSet;


public class Orgstruktura  extends KreirDrop implements DataModule {
  //ResourceBundle dmRes = ResourceBundle.getBundle("hr.restart.baza.dmRes");
  private static Orgstruktura Orgstrukturaclass;
  //dM dm  = dM.getDataModule();
  QueryDataSet orgstruktura = new raDataSet();
  QueryDataSet orgstrukturaaktiv = new raDataSet();
  QueryDataSet knjig = new raDataSet();
  /*Column orgsLOKK = new Column();
  Column orgsAKTIV = new Column();
  Column orgsCORG = new Column();
  Column orgsNAZIV = new Column();

  Column orgsMJESTO = new Column();
  Column orgsADRESA = new Column();
  Column orgsHPBROJ = new Column();
  Column orgsZIRO = new Column();
  Column orgsPRIPADNOST = new Column();
  Column orgsNALOG = new Column();*/
  public static Orgstruktura getDataModule() {
    if (Orgstrukturaclass == null) {
      Orgstrukturaclass = new Orgstruktura();
    }
    return Orgstrukturaclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return orgstruktura;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getAktiv() {
    return orgstrukturaaktiv;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getKnjig() {
    return knjig;
  }

  public Orgstruktura() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {

/*
    orgsNALOG.setCaption(dmRes.getString("orgsNALOG_caption"));
    orgsNALOG.setColumnName("NALOG");
    orgsNALOG.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgsNALOG.setDefault("0");
    orgsNALOG.setPrecision(1);
    orgsNALOG.setTableName("ORGSTRUKTURA");
    orgsNALOG.setSqlType(1);
    orgsNALOG.setServerColumnName("NALOG");
    orgsPRIPADNOST.setCaption("Pripadnost");
    orgsPRIPADNOST.setColumnName("PRIPADNOST");
    orgsPRIPADNOST.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgsPRIPADNOST.setPrecision(12);
    orgsPRIPADNOST.setTableName("ORGSTRUKTURA");
    orgsPRIPADNOST.setSqlType(1);
    orgsPRIPADNOST.setServerColumnName("PRIPADNOST");
    orgsZIRO.setCaption(dmRes.getString("orgsZIRO_caption"));
    orgsZIRO.setColumnName("ZIRO");
    orgsZIRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgsZIRO.setPrecision(40);
    orgsZIRO.setTableName("ORGSTRUKTURA");
    orgsZIRO.setSqlType(1);
    orgsZIRO.setServerColumnName("ZIRO");
    orgsHPBROJ.setCaption(dmRes.getString("orgsHPBROJ_caption"));
    orgsHPBROJ.setColumnName("HPBROJ");
    orgsHPBROJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgsHPBROJ.setPrecision(5);
    orgsHPBROJ.setTableName("ORGSTRUKTURA");
    orgsHPBROJ.setSqlType(1);
    orgsHPBROJ.setServerColumnName("HPBROJ");
    orgsADRESA.setCaption(dmRes.getString("orgsADRESA_caption"));
    orgsADRESA.setColumnName("ADRESA");
    orgsADRESA.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgsADRESA.setPrecision(50);
    orgsADRESA.setTableName("ORGSTRUKTURA");
    orgsADRESA.setSqlType(1);
    orgsADRESA.setServerColumnName("ADRESA");
    orgsMJESTO.setCaption(dmRes.getString("orgsMJESTO_caption"));
    orgsMJESTO.setColumnName("MJESTO");
    orgsMJESTO.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgsMJESTO.setPrecision(30);
    orgsMJESTO.setTableName("ORGSTRUKTURA");
    orgsMJESTO.setSqlType(1);
    orgsMJESTO.setServerColumnName("MJESTO");
    orgsNAZIV.setCaption(dmRes.getString("orgsNAZIV_caption"));
    orgsNAZIV.setColumnName("NAZIV");
    orgsNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgsNAZIV.setPrecision(100);
    orgsNAZIV.setTableName("ORGSTRUKTURA");
    orgsNAZIV.setServerColumnName("NAZIV");
    orgsNAZIV.setSqlType(1);
    orgsNAZIV.setWidth(30);
    orgsCORG.setCaption(dmRes.getString("orgsCORG_caption"));
    orgsCORG.setColumnName("CORG");
    orgsCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgsCORG.setPrecision(12);
    orgsCORG.setRowId(true);
    orgsCORG.setTableName("ORGSTRUKTURA");
    orgsCORG.setWidth(5);
    orgsCORG.setServerColumnName("CORG");
    orgsCORG.setSqlType(1);
    orgsAKTIV.setCaption(dmRes.getString("orgsAKTIV_caption"));
    orgsAKTIV.setColumnName("AKTIV");
    orgsAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgsAKTIV.setDefault("D");
    orgsAKTIV.setPrecision(1);
    orgsAKTIV.setTableName("ORGSTRUKTURA");
    orgsAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    orgsAKTIV.setServerColumnName("AKTIV");
    orgsAKTIV.setSqlType(1);
    orgsLOKK.setCaption("Lokk");
    orgsLOKK.setColumnName("LOKK");
    orgsLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    orgsLOKK.setDefault("N");
    orgsLOKK.setPrecision(1);
    orgsLOKK.setTableName("ORGSTRUKTURA");
    orgsLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    orgsLOKK.setServerColumnName("LOKK");
    orgsLOKK.setSqlType(1);

    orgstruktura.setResolver(dm.getQresolver());
    orgstruktura.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "SELECT * " +
      "FROM ORGSTRUKTURA", null, true, Load.ALL));
 setColumns(new Column[] {orgsLOKK, orgsAKTIV, orgsCORG, orgsNAZIV, orgsMJESTO, orgsADRESA,
                                          orgsHPBROJ, orgsZIRO, orgsPRIPADNOST, orgsNALOG});*/

    initModule();
    
    createFilteredDataSet(orgstrukturaaktiv, "aktiv = 'D'");
    createFilteredDataSet(knjig, "aktiv = 'D' AND corg = pripadnost");
  }

//  public void setall(){

    /*SqlDefTabela = "create table Orgstruktura " +
      "(lokk char(1) CHARACTER SET WIN1250 default 'N'," +
      "aktiv char(1) CHARACTER SET WIN1250 default 'D'," +
      "corg CHAR(12) CHARACTER SET WIN1250 NOT NULL," +
      "naziv CHAR(50) CHARACTER SET WIN1250," +
      "mjesto CHAR(30) CHARACTER SET WIN1250,"+
      "adresa CHAR(50) CHARACTER SET WIN1250,"+
      "hpbroj CHAR(5) CHARACTER SET WIN1250,"+
      "ziro CHAR(40) CHARACTER SET WIN1250,"+
      "pripadnost CHAR(12) CHARACTER SET WIN1250,"+
      "nalog CHAR(1) CHARACTER SET WIN1250,"+
      "Primary Key (corg))";*/

/*    ddl.create("orgstruktura")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addChar("naziv", 100)
       .addChar("mjesto", 30)
       .addChar("adresa", 50)
       .addChar("hpbroj", 5)
       .addChar("ziro", 40)
       .addChar("pripadnost", 12)
       .addChar("nalog", 1)
       .addPrimaryKey("corg");

    Naziv="Orgstruktura";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
*/
    /*
    NaziviIdx=new String[]{"ilokkorgstruktura","iaktivorgstruktura","icorg"};

    DefIndex= new String[] {CommonTable.SqlDefIndex+NaziviIdx[0] +" on Orgstruktura (lokk)" ,
                            CommonTable.SqlDefIndex+NaziviIdx[1] +" on Orgstruktura (aktiv)" ,
                            CommonTable.SqlDefUniqueIndex+NaziviIdx[2] +" on Orgstruktura (corg)"} ;*/
  //}
}