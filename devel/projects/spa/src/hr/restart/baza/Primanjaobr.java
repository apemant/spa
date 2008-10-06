/****license*****************************************************************
**   file: Primanjaobr.java
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



public class Primanjaobr extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Primanjaobr Primanjaobrclass;

  QueryDataSet priobr = new QueryDataSet();

  Column priobrLOKK = new Column();
  Column priobrAKTIV = new Column();
  Column priobrCRADNIK = new Column();
  Column priobrCVRP = new Column();
  Column priobrRBR = new Column();
  Column priobrCORG = new Column();
  Column priobrSATI = new Column();
  Column priobrKOEF = new Column();
  Column priobrBRUTO = new Column();
  Column priobrDOPRINOSI = new Column();
  Column priobrNETO = new Column();
  Column priobrIRAZOD = new Column();
  Column priobrIRAZDO = new Column();

  public static Primanjaobr getDataModule() {
    if (Primanjaobrclass == null) {
      Primanjaobrclass = new Primanjaobr();
    }
    return Primanjaobrclass;
  }

  public QueryDataSet getQueryDataSet() {
    return priobr;
  }

  public Primanjaobr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    priobrLOKK.setCaption("Status zauzetosti");
    priobrLOKK.setColumnName("LOKK");
    priobrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    priobrLOKK.setPrecision(1);
    priobrLOKK.setTableName("PRIMANJAOBR");
    priobrLOKK.setServerColumnName("LOKK");
    priobrLOKK.setSqlType(1);
    priobrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    priobrLOKK.setDefault("N");
    priobrAKTIV.setCaption("Aktivan - neaktivan");
    priobrAKTIV.setColumnName("AKTIV");
    priobrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    priobrAKTIV.setPrecision(1);
    priobrAKTIV.setTableName("PRIMANJAOBR");
    priobrAKTIV.setServerColumnName("AKTIV");
    priobrAKTIV.setSqlType(1);
    priobrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    priobrAKTIV.setDefault("D");
    priobrCRADNIK.setCaption("Radnik");
    priobrCRADNIK.setColumnName("CRADNIK");
    priobrCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    priobrCRADNIK.setPrecision(6);
    priobrCRADNIK.setRowId(true);
    priobrCRADNIK.setTableName("PRIMANJAOBR");
    priobrCRADNIK.setServerColumnName("CRADNIK");
    priobrCRADNIK.setSqlType(1);
    priobrCVRP.setCaption("Vrsta primanja");
    priobrCVRP.setColumnName("CVRP");
    priobrCVRP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    priobrCVRP.setPrecision(3);
    priobrCVRP.setRowId(true);
    priobrCVRP.setTableName("PRIMANJAOBR");
    priobrCVRP.setServerColumnName("CVRP");
    priobrCVRP.setSqlType(5);
    priobrCVRP.setWidth(3);
    priobrRBR.setCaption("Redni broj");
    priobrRBR.setColumnName("RBR");
    priobrRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    priobrRBR.setPrecision(3);
    priobrRBR.setRowId(true);
    priobrRBR.setTableName("PRIMANJAOBR");
    priobrRBR.setServerColumnName("RBR");
    priobrRBR.setSqlType(5);
    priobrRBR.setWidth(3);
    priobrRBR.setDefault("1");
    priobrCORG.setCaption("Org. Jedinica u kojoj je obavljen rad");
    priobrCORG.setColumnName("CORG");
    priobrCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    priobrCORG.setPrecision(12);
    priobrCORG.setTableName("PRIMANJAOBR");
    priobrCORG.setServerColumnName("CORG");
    priobrCORG.setSqlType(1);
    priobrSATI.setCaption("Sati");
    priobrSATI.setColumnName("SATI");
    priobrSATI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    priobrSATI.setPrecision(17);
    priobrSATI.setScale(2);
    priobrSATI.setDisplayMask("###,###,##0.00");
    priobrSATI.setDefault("0");
    priobrSATI.setTableName("PRIMANJAOBR");
    priobrSATI.setServerColumnName("SATI");
    priobrSATI.setSqlType(2);
    priobrSATI.setDefault("0");
    priobrKOEF.setCaption("Koeficijent");
    priobrKOEF.setColumnName("KOEF");
    priobrKOEF.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    priobrKOEF.setPrecision(17);
    priobrKOEF.setScale(2);
    priobrKOEF.setDisplayMask("###,###,##0.00");
    priobrKOEF.setDefault("0");
    priobrKOEF.setTableName("PRIMANJAOBR");
    priobrKOEF.setServerColumnName("KOEF");
    priobrKOEF.setSqlType(2);
    priobrKOEF.setDefault("0");
    priobrBRUTO.setCaption("Bruto");
    priobrBRUTO.setColumnName("BRUTO");
    priobrBRUTO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    priobrBRUTO.setPrecision(17);
    priobrBRUTO.setScale(2);
    priobrBRUTO.setDisplayMask("###,###,##0.00");
    priobrBRUTO.setDefault("0");
    priobrBRUTO.setTableName("PRIMANJAOBR");
    priobrBRUTO.setServerColumnName("BRUTO");
    priobrBRUTO.setSqlType(2);
    priobrBRUTO.setDefault("0");
    priobrDOPRINOSI.setCaption("Odbici od bruta (doprinosi)");
    priobrDOPRINOSI.setColumnName("DOPRINOSI");
    priobrDOPRINOSI.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    priobrDOPRINOSI.setPrecision(17);
    priobrDOPRINOSI.setScale(2);
    priobrDOPRINOSI.setDisplayMask("###,###,##0.00");
    priobrDOPRINOSI.setDefault("0");
    priobrDOPRINOSI.setTableName("PRIMANJAOBR");
    priobrDOPRINOSI.setServerColumnName("DOPRINOSI");
    priobrDOPRINOSI.setSqlType(2);
    priobrDOPRINOSI.setDefault("0");
    priobrNETO.setCaption("Neto za oporetivanje");
    priobrNETO.setColumnName("NETO");
    priobrNETO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    priobrNETO.setPrecision(17);
    priobrNETO.setScale(2);
    priobrNETO.setDisplayMask("###,###,##0.00");
    priobrNETO.setDefault("0");
    priobrNETO.setTableName("PRIMANJAOBR");
    priobrNETO.setServerColumnName("NETO");
    priobrNETO.setSqlType(2);
    priobrNETO.setDefault("0");
    priobrIRAZOD.setCaption("Ispla\u0107eno za razdoblje od");
    priobrIRAZOD.setColumnName("IRAZOD");
    priobrIRAZOD.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    priobrIRAZOD.setPrecision(8);
    priobrIRAZOD.setDisplayMask("dd-MM-yyyy");
//    priobrIRAZOD.setEditMask("dd-MM-yyyy");
    priobrIRAZOD.setTableName("PRIMANJAOBR");
    priobrIRAZOD.setServerColumnName("IRAZOD");
    priobrIRAZOD.setSqlType(93);
    priobrIRAZOD.setWidth(10);
    priobrIRAZDO.setCaption("Ispla\u0107eno za razdoblje do");
    priobrIRAZDO.setColumnName("IRAZDO");
    priobrIRAZDO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    priobrIRAZDO.setPrecision(8);
    priobrIRAZDO.setDisplayMask("dd-MM-yyyy");
//    priobrIRAZDO.setEditMask("dd-MM-yyyy");
    priobrIRAZDO.setTableName("PRIMANJAOBR");
    priobrIRAZDO.setServerColumnName("IRAZDO");
    priobrIRAZDO.setSqlType(93);
    priobrIRAZDO.setWidth(10);
    priobr.setResolver(dm.getQresolver());
    priobr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Primanjaobr", null, true, Load.ALL));
 setColumns(new Column[] {priobrLOKK, priobrAKTIV, priobrCRADNIK, priobrCVRP, priobrRBR, priobrCORG, priobrSATI, priobrKOEF, priobrBRUTO,
        priobrDOPRINOSI, priobrNETO, priobrIRAZOD, priobrIRAZDO});
  }

  public void setall() {

    ddl.create("Primanjaobr")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cradnik", 6, true)
       .addShort("cvrp", 3, true)
       .addShort("rbr", 3, true)
       .addChar("corg", 12)
       .addFloat("sati", 17, 2)
       .addFloat("koef", 17, 2)
       .addFloat("bruto", 17, 2)
       .addFloat("doprinosi", 17, 2)
       .addFloat("neto", 17, 2)
       .addDate("irazod")
       .addDate("irazdo")
       .addPrimaryKey("cradnik,cvrp,rbr");


    Naziv = "Primanjaobr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cradnik", "rbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
