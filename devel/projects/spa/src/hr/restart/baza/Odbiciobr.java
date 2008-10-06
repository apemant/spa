/****license*****************************************************************
**   file: Odbiciobr.java
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



public class Odbiciobr extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Odbiciobr Odbiciobrclass;

  QueryDataSet odbobr = new QueryDataSet();

  Column odbobrLOKK = new Column();
  Column odbobrAKTIV = new Column();
  Column odbobrCRADNIK = new Column();
  Column odbobrCVRP = new Column();
  Column odbobrRBR = new Column();
  Column odbobrCVRODB = new Column();
  Column odbobrCKEY = new Column();
  Column odbobrCKEY2 = new Column();
  Column odbobrRBRODB = new Column();
  Column odbobrOBROSN = new Column();
  Column odbobrOBRSTOPA = new Column();
  Column odbobrOBRIZNOS = new Column();
  Column odbobrGLAVNICA = new Column();
  Column odbobrSALDO = new Column();

  public static Odbiciobr getDataModule() {
    if (Odbiciobrclass == null) {
      Odbiciobrclass = new Odbiciobr();
    }
    return Odbiciobrclass;
  }

  public QueryDataSet getQueryDataSet() {
    return odbobr;
  }

  public Odbiciobr() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    odbobrLOKK.setCaption("Status zauzetosti");
    odbobrLOKK.setColumnName("LOKK");
    odbobrLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbobrLOKK.setPrecision(1);
    odbobrLOKK.setTableName("ODBICIOBR");
    odbobrLOKK.setServerColumnName("LOKK");
    odbobrLOKK.setSqlType(1);
    odbobrLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    odbobrLOKK.setDefault("N");
    odbobrAKTIV.setCaption("Aktivan - neaktivan");
    odbobrAKTIV.setColumnName("AKTIV");
    odbobrAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbobrAKTIV.setPrecision(1);
    odbobrAKTIV.setTableName("ODBICIOBR");
    odbobrAKTIV.setServerColumnName("AKTIV");
    odbobrAKTIV.setSqlType(1);
    odbobrAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    odbobrAKTIV.setDefault("D");
    odbobrCRADNIK.setCaption("Radnik");
    odbobrCRADNIK.setColumnName("CRADNIK");
    odbobrCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbobrCRADNIK.setPrecision(6);
    odbobrCRADNIK.setRowId(true);
    odbobrCRADNIK.setTableName("ODBICIOBR");
    odbobrCRADNIK.setServerColumnName("CRADNIK");
    odbobrCRADNIK.setSqlType(1);
    odbobrCVRP.setCaption("Vrsta primanja");
    odbobrCVRP.setColumnName("CVRP");
    odbobrCVRP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odbobrCVRP.setPrecision(3);
    odbobrCVRP.setRowId(true);
    odbobrCVRP.setTableName("ODBICIOBR");
    odbobrCVRP.setServerColumnName("CVRP");
    odbobrCVRP.setSqlType(5);
    odbobrCVRP.setWidth(3);
    odbobrRBR.setCaption("Redni broj");
    odbobrRBR.setColumnName("RBR");
    odbobrRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odbobrRBR.setPrecision(3);
    odbobrRBR.setRowId(true);
    odbobrRBR.setTableName("ODBICIOBR");
    odbobrRBR.setServerColumnName("RBR");
    odbobrRBR.setSqlType(5);
    odbobrRBR.setWidth(3);
    odbobrRBR.setDefault("1");
    odbobrCVRODB.setCaption("Vrsta odbitka");
    odbobrCVRODB.setColumnName("CVRODB");
    odbobrCVRODB.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odbobrCVRODB.setPrecision(4);
    odbobrCVRODB.setRowId(true);
    odbobrCVRODB.setTableName("ODBICIOBR");
    odbobrCVRODB.setServerColumnName("CVRODB");
    odbobrCVRODB.setSqlType(5);
    odbobrCVRODB.setWidth(4);
    odbobrCKEY.setCaption("Klju\u010D nivoa odbitka");
    odbobrCKEY.setColumnName("CKEY");
    odbobrCKEY.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbobrCKEY.setPrecision(30);
    odbobrCKEY.setRowId(true);
    odbobrCKEY.setTableName("ODBICIOBR");
    odbobrCKEY.setServerColumnName("CKEY");
    odbobrCKEY.setSqlType(1);
    odbobrCKEY.setWidth(30);
    odbobrCKEY2.setCaption("Klju\u010D 2");
    odbobrCKEY2.setColumnName("CKEY2");
    odbobrCKEY2.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbobrCKEY2.setPrecision(30);
    odbobrCKEY2.setRowId(true);
    odbobrCKEY2.setTableName("ODBICIOBR");
    odbobrCKEY2.setServerColumnName("CKEY2");
    odbobrCKEY2.setSqlType(1);
    odbobrCKEY2.setWidth(30);
    odbobrRBRODB.setCaption("Redni broj odbitka");
    odbobrRBRODB.setColumnName("RBRODB");
    odbobrRBRODB.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odbobrRBRODB.setPrecision(4);
    odbobrRBRODB.setRowId(true);
    odbobrRBRODB.setTableName("ODBICIOBR");
    odbobrRBRODB.setServerColumnName("RBRODB");
    odbobrRBRODB.setSqlType(5);
    odbobrRBRODB.setWidth(4);
    odbobrOBROSN.setCaption("Osnovica za obra\u010Dun");
    odbobrOBROSN.setColumnName("OBROSN");
    odbobrOBROSN.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odbobrOBROSN.setPrecision(17);
    odbobrOBROSN.setScale(2);
    odbobrOBROSN.setDisplayMask("###,###,##0.00");
    odbobrOBROSN.setDefault("0");
    odbobrOBROSN.setTableName("ODBICIOBR");
    odbobrOBROSN.setServerColumnName("OBROSN");
    odbobrOBROSN.setSqlType(2);
    odbobrOBROSN.setDefault("0");
    odbobrOBRSTOPA.setCaption("Stopa");
    odbobrOBRSTOPA.setColumnName("OBRSTOPA");
    odbobrOBRSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odbobrOBRSTOPA.setPrecision(17);
    odbobrOBRSTOPA.setScale(2);
    odbobrOBRSTOPA.setDisplayMask("###,###,##0.00");
    odbobrOBRSTOPA.setDefault("0");
    odbobrOBRSTOPA.setTableName("ODBICIOBR");
    odbobrOBRSTOPA.setServerColumnName("OBRSTOPA");
    odbobrOBRSTOPA.setSqlType(2);
    odbobrOBRSTOPA.setDefault("0");
    odbobrOBRIZNOS.setCaption("Obra\u010Dunati iznos");
    odbobrOBRIZNOS.setColumnName("OBRIZNOS");
    odbobrOBRIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odbobrOBRIZNOS.setPrecision(17);
    odbobrOBRIZNOS.setScale(2);
    odbobrOBRIZNOS.setDisplayMask("###,###,##0.00");
    odbobrOBRIZNOS.setDefault("0");
    odbobrOBRIZNOS.setTableName("ODBICIOBR");
    odbobrOBRIZNOS.setServerColumnName("OBRIZNOS");
    odbobrOBRIZNOS.setSqlType(2);
    odbobrOBRIZNOS.setDefault("0");
    odbobrGLAVNICA.setCaption("Glavnica");
    odbobrGLAVNICA.setColumnName("GLAVNICA");
    odbobrGLAVNICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odbobrGLAVNICA.setPrecision(17);
    odbobrGLAVNICA.setScale(2);
    odbobrGLAVNICA.setDisplayMask("###,###,##0.00");
    odbobrGLAVNICA.setDefault("0");
    odbobrGLAVNICA.setTableName("ODBICIOBR");
    odbobrGLAVNICA.setServerColumnName("GLAVNICA");
    odbobrGLAVNICA.setSqlType(2);
    odbobrGLAVNICA.setDefault("0");
    odbobrSALDO.setCaption("Ostatak duga");
    odbobrSALDO.setColumnName("SALDO");
    odbobrSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odbobrSALDO.setPrecision(17);
    odbobrSALDO.setScale(2);
    odbobrSALDO.setDisplayMask("###,###,##0.00");
    odbobrSALDO.setDefault("0");
    odbobrSALDO.setTableName("ODBICIOBR");
    odbobrSALDO.setServerColumnName("SALDO");
    odbobrSALDO.setSqlType(2);
    odbobrSALDO.setDefault("0");
    odbobr.setResolver(dm.getQresolver());
    odbobr.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Odbiciobr", null, true, Load.ALL));
 setColumns(new Column[] {odbobrLOKK, odbobrAKTIV, odbobrCRADNIK, odbobrCVRP, odbobrRBR, odbobrCVRODB, odbobrCKEY, odbobrCKEY2, odbobrRBRODB,
        odbobrOBROSN, odbobrOBRSTOPA, odbobrOBRIZNOS, odbobrGLAVNICA, odbobrSALDO});
  }

  public void setall() {

    ddl.create("Odbiciobr")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
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
       .addFloat("glavnica", 17, 2)
       .addFloat("saldo", 17, 2)
       .addPrimaryKey("cradnik,cvrp,rbr,cvrodb,ckey,ckey2,rbrodb");


    Naziv = "Odbiciobr";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cradnik", "rbr", "ckey", "ckey2", "rbrodb"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
