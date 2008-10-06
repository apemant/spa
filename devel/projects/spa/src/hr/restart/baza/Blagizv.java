/****license*****************************************************************
**   file: Blagizv.java
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



public class Blagizv extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Blagizv Blagizvclass;

  QueryDataSet blagizv = new raDataSet();

  Column blagizvLOKK = new Column();
  Column blagizvAKTIV = new Column();
  Column blagizvKNJIG = new Column();
  Column blagizvCBLAG = new Column();
  Column blagizvOZNVAL = new Column();
  Column blagizvGODINA = new Column();
  Column blagizvBRIZV = new Column();
  Column blagizvDATOD = new Column();
  Column blagizvDATDO = new Column();
  Column blagizvUKPRIMITAK = new Column();
  Column blagizvUKIZDATAK = new Column();
  Column blagizvPRIJENOS = new Column();
  Column blagizvSALDO = new Column();
  Column blagizvUKSALDO = new Column();
  Column blagizvPVUKPRIMITAK = new Column();
  Column blagizvPVUKIZDATAK = new Column();
  Column blagizvPVPRIJENOS = new Column();
  Column blagizvPVSALDO = new Column();
  Column blagizvPVUKSALDO = new Column();
  Column blagizvSTATUS = new Column();

  public static Blagizv getDataModule() {
    if (Blagizvclass == null) {
      Blagizvclass = new Blagizv();
    }
    return Blagizvclass;
  }

  public QueryDataSet getQueryDataSet() {
    return blagizv;
  }

  public Blagizv() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    blagizvLOKK.setCaption("Status zauzetosti");
    blagizvLOKK.setColumnName("LOKK");
    blagizvLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagizvLOKK.setPrecision(1);
    blagizvLOKK.setTableName("BLAGIZV");
    blagizvLOKK.setServerColumnName("LOKK");
    blagizvLOKK.setSqlType(1);
    blagizvLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    blagizvLOKK.setDefault("N");
    blagizvAKTIV.setCaption("Aktivan - neaktivan");
    blagizvAKTIV.setColumnName("AKTIV");
    blagizvAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagizvAKTIV.setPrecision(1);
    blagizvAKTIV.setTableName("BLAGIZV");
    blagizvAKTIV.setServerColumnName("AKTIV");
    blagizvAKTIV.setSqlType(1);
    blagizvAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    blagizvAKTIV.setDefault("D");
    blagizvKNJIG.setCaption("Knjigovodstvo");
    blagizvKNJIG.setColumnName("KNJIG");
    blagizvKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagizvKNJIG.setPrecision(12);
    blagizvKNJIG.setRowId(true);
    blagizvKNJIG.setTableName("BLAGIZV");
    blagizvKNJIG.setServerColumnName("KNJIG");
    blagizvKNJIG.setSqlType(1);
    blagizvCBLAG.setCaption("Blagajna");
    blagizvCBLAG.setColumnName("CBLAG");
    blagizvCBLAG.setDataType(com.borland.dx.dataset.Variant.INT);
    blagizvCBLAG.setPrecision(6);
    blagizvCBLAG.setRowId(true);
    blagizvCBLAG.setTableName("BLAGIZV");
    blagizvCBLAG.setServerColumnName("CBLAG");
    blagizvCBLAG.setSqlType(4);
    blagizvCBLAG.setWidth(6);
    blagizvOZNVAL.setCaption("Oznaka valute");
    blagizvOZNVAL.setColumnName("OZNVAL");
    blagizvOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagizvOZNVAL.setPrecision(3);
    blagizvOZNVAL.setRowId(true);
    blagizvOZNVAL.setTableName("BLAGIZV");
    blagizvOZNVAL.setServerColumnName("OZNVAL");
    blagizvOZNVAL.setSqlType(1);
    blagizvGODINA.setCaption("Godina");
    blagizvGODINA.setColumnName("GODINA");
    blagizvGODINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    blagizvGODINA.setPrecision(4);
    blagizvGODINA.setRowId(true);
    blagizvGODINA.setTableName("BLAGIZV");
    blagizvGODINA.setServerColumnName("GODINA");
    blagizvGODINA.setSqlType(5);
    blagizvGODINA.setWidth(4);
    blagizvBRIZV.setCaption("Izvještaj");
    blagizvBRIZV.setColumnName("BRIZV");
    blagizvBRIZV.setDataType(com.borland.dx.dataset.Variant.INT);
    blagizvBRIZV.setPrecision(6);
    blagizvBRIZV.setRowId(true);
    blagizvBRIZV.setTableName("BLAGIZV");
    blagizvBRIZV.setServerColumnName("BRIZV");
    blagizvBRIZV.setSqlType(4);
    blagizvBRIZV.setWidth(6);
    blagizvDATOD.setCaption("Od");
    blagizvDATOD.setColumnName("DATOD");
    blagizvDATOD.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    blagizvDATOD.setPrecision(8);
    blagizvDATOD.setDisplayMask("dd-MM-yyyy");
//    blagizvDATOD.setEditMask("dd-MM-yyyy");
    blagizvDATOD.setTableName("BLAGIZV");
    blagizvDATOD.setServerColumnName("DATOD");
    blagizvDATOD.setSqlType(93);
    blagizvDATOD.setWidth(10);
    blagizvDATDO.setCaption("Do");
    blagizvDATDO.setColumnName("DATDO");
    blagizvDATDO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    blagizvDATDO.setPrecision(8);
    blagizvDATDO.setDisplayMask("dd-MM-yyyy");
//    blagizvDATDO.setEditMask("dd-MM-yyyy");
    blagizvDATDO.setTableName("BLAGIZV");
    blagizvDATDO.setServerColumnName("DATDO");
    blagizvDATDO.setSqlType(93);
    blagizvDATDO.setWidth(10);
    blagizvUKPRIMITAK.setCaption("Ukupno primitak");
    blagizvUKPRIMITAK.setColumnName("UKPRIMITAK");
    blagizvUKPRIMITAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagizvUKPRIMITAK.setPrecision(17);
    blagizvUKPRIMITAK.setScale(2);
    blagizvUKPRIMITAK.setDisplayMask("###,###,##0.00");
    blagizvUKPRIMITAK.setDefault("0");
    blagizvUKPRIMITAK.setTableName("BLAGIZV");
    blagizvUKPRIMITAK.setServerColumnName("UKPRIMITAK");
    blagizvUKPRIMITAK.setSqlType(2);
    blagizvUKPRIMITAK.setDefault("0");
    blagizvUKIZDATAK.setCaption("Ukupno izdatak");
    blagizvUKIZDATAK.setColumnName("UKIZDATAK");
    blagizvUKIZDATAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagizvUKIZDATAK.setPrecision(17);
    blagizvUKIZDATAK.setScale(2);
    blagizvUKIZDATAK.setDisplayMask("###,###,##0.00");
    blagizvUKIZDATAK.setDefault("0");
    blagizvUKIZDATAK.setTableName("BLAGIZV");
    blagizvUKIZDATAK.setServerColumnName("UKIZDATAK");
    blagizvUKIZDATAK.setSqlType(2);
    blagizvUKIZDATAK.setDefault("0");
    blagizvPRIJENOS.setCaption("Prijenos");
    blagizvPRIJENOS.setColumnName("PRIJENOS");
    blagizvPRIJENOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagizvPRIJENOS.setPrecision(17);
    blagizvPRIJENOS.setScale(2);
    blagizvPRIJENOS.setDisplayMask("###,###,##0.00");
    blagizvPRIJENOS.setDefault("0");
    blagizvPRIJENOS.setTableName("BLAGIZV");
    blagizvPRIJENOS.setServerColumnName("PRIJENOS");
    blagizvPRIJENOS.setSqlType(2);
    blagizvPRIJENOS.setDefault("0");
    blagizvSALDO.setCaption("Saldo");
    blagizvSALDO.setColumnName("SALDO");
    blagizvSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagizvSALDO.setPrecision(17);
    blagizvSALDO.setScale(2);
    blagizvSALDO.setDisplayMask("###,###,##0.00");
    blagizvSALDO.setDefault("0");
    blagizvSALDO.setTableName("BLAGIZV");
    blagizvSALDO.setServerColumnName("SALDO");
    blagizvSALDO.setSqlType(2);
    blagizvSALDO.setDefault("0");
    blagizvUKSALDO.setCaption("Ukupni saldo");
    blagizvUKSALDO.setColumnName("UKSALDO");
    blagizvUKSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagizvUKSALDO.setPrecision(17);
    blagizvUKSALDO.setScale(2);
    blagizvUKSALDO.setDisplayMask("###,###,##0.00");
    blagizvUKSALDO.setDefault("0");
    blagizvUKSALDO.setTableName("BLAGIZV");
    blagizvUKSALDO.setServerColumnName("UKSALDO");
    blagizvUKSALDO.setSqlType(2);
    blagizvUKSALDO.setDefault("0");
    blagizvPVUKPRIMITAK.setCaption("Ukupno primitak u dom.valuti");
    blagizvPVUKPRIMITAK.setColumnName("PVUKPRIMITAK");
    blagizvPVUKPRIMITAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagizvPVUKPRIMITAK.setPrecision(17);
    blagizvPVUKPRIMITAK.setScale(2);
    blagizvPVUKPRIMITAK.setDisplayMask("###,###,##0.00");
    blagizvPVUKPRIMITAK.setDefault("0");
    blagizvPVUKPRIMITAK.setTableName("BLAGIZV");
    blagizvPVUKPRIMITAK.setServerColumnName("PVUKPRIMITAK");
    blagizvPVUKPRIMITAK.setSqlType(2);
    blagizvPVUKPRIMITAK.setDefault("0");
    blagizvPVUKIZDATAK.setCaption("Ukupno izdatak u dom.valuti");
    blagizvPVUKIZDATAK.setColumnName("PVUKIZDATAK");
    blagizvPVUKIZDATAK.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagizvPVUKIZDATAK.setPrecision(17);
    blagizvPVUKIZDATAK.setScale(2);
    blagizvPVUKIZDATAK.setDisplayMask("###,###,##0.00");
    blagizvPVUKIZDATAK.setDefault("0");
    blagizvPVUKIZDATAK.setTableName("BLAGIZV");
    blagizvPVUKIZDATAK.setServerColumnName("PVUKIZDATAK");
    blagizvPVUKIZDATAK.setSqlType(2);
    blagizvPVUKIZDATAK.setDefault("0");
    blagizvPVPRIJENOS.setCaption("Prijenos u dom.valuti");
    blagizvPVPRIJENOS.setColumnName("PVPRIJENOS");
    blagizvPVPRIJENOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagizvPVPRIJENOS.setPrecision(17);
    blagizvPVPRIJENOS.setScale(2);
    blagizvPVPRIJENOS.setDisplayMask("###,###,##0.00");
    blagizvPVPRIJENOS.setDefault("0");
    blagizvPVPRIJENOS.setTableName("BLAGIZV");
    blagizvPVPRIJENOS.setServerColumnName("PVPRIJENOS");
    blagizvPVPRIJENOS.setSqlType(2);
    blagizvPVPRIJENOS.setDefault("0");
    blagizvPVSALDO.setCaption("Saldo u dom.valuti");
    blagizvPVSALDO.setColumnName("PVSALDO");
    blagizvPVSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagizvPVSALDO.setPrecision(17);
    blagizvPVSALDO.setScale(2);
    blagizvPVSALDO.setDisplayMask("###,###,##0.00");
    blagizvPVSALDO.setDefault("0");
    blagizvPVSALDO.setTableName("BLAGIZV");
    blagizvPVSALDO.setServerColumnName("PVSALDO");
    blagizvPVSALDO.setSqlType(2);
    blagizvPVSALDO.setDefault("0");
    blagizvPVUKSALDO.setCaption("Ukupni saldo u dom.valuti");
    blagizvPVUKSALDO.setColumnName("PVUKSALDO");
    blagizvPVUKSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    blagizvPVUKSALDO.setPrecision(17);
    blagizvPVUKSALDO.setScale(2);
    blagizvPVUKSALDO.setDisplayMask("###,###,##0.00");
    blagizvPVUKSALDO.setDefault("0");
    blagizvPVUKSALDO.setTableName("BLAGIZV");
    blagizvPVUKSALDO.setServerColumnName("PVUKSALDO");
    blagizvPVUKSALDO.setSqlType(2);
    blagizvPVUKSALDO.setDefault("0");
    blagizvSTATUS.setCaption("Status");
    blagizvSTATUS.setColumnName("STATUS");
    blagizvSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    blagizvSTATUS.setPrecision(1);
    blagizvSTATUS.setTableName("BLAGIZV");
    blagizvSTATUS.setServerColumnName("STATUS");
    blagizvSTATUS.setSqlType(1);
    blagizvSTATUS.setDefault("U");
    blagizv.setResolver(dm.getQresolver());
    blagizv.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Blagizv", null, true, Load.ALL));
 setColumns(new Column[] {blagizvLOKK, blagizvAKTIV, blagizvKNJIG, blagizvCBLAG, blagizvOZNVAL, blagizvGODINA, blagizvBRIZV, blagizvDATOD,
        blagizvDATDO, blagizvUKPRIMITAK, blagizvUKIZDATAK, blagizvPRIJENOS, blagizvSALDO, blagizvUKSALDO, blagizvPVUKPRIMITAK, blagizvPVUKIZDATAK,
        blagizvPVPRIJENOS, blagizvPVSALDO, blagizvPVUKSALDO, blagizvSTATUS});
  }

  public void setall() {

    ddl.create("Blagizv")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("knjig", 12, true)
       .addInteger("cblag", 6, true)
       .addChar("oznval", 3, true)
       .addShort("godina", 4, true)
       .addInteger("brizv", 6, true)
       .addDate("datod")
       .addDate("datdo")
       .addFloat("ukprimitak", 17, 2)
       .addFloat("ukizdatak", 17, 2)
       .addFloat("prijenos", 17, 2)
       .addFloat("saldo", 17, 2)
       .addFloat("uksaldo", 17, 2)
       .addFloat("pvukprimitak", 17, 2)
       .addFloat("pvukizdatak", 17, 2)
       .addFloat("pvprijenos", 17, 2)
       .addFloat("pvsaldo", 17, 2)
       .addFloat("pvuksaldo", 17, 2)
       .addChar("status", 1, "U")
       .addPrimaryKey("knjig,cblag,oznval,godina,brizv");


    Naziv = "Blagizv";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
