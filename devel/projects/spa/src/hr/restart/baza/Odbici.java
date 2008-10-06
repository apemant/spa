/****license*****************************************************************
**   file: Odbici.java
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



public class Odbici extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Odbici Odbiciclass;

  QueryDataSet odbici = new raDataSet();

  Column odbiciLOKK = new Column();
  Column odbiciAKTIV = new Column();
  Column odbiciCVRODB = new Column();
  Column odbiciCKEY = new Column();
  Column odbiciCKEY2 = new Column();
  Column odbiciRBRODB = new Column();
  Column odbiciPNB1 = new Column();
  Column odbiciPNB2 = new Column();
  Column odbiciIZNOS = new Column();
  Column odbiciSTOPA = new Column();
  Column odbiciDATPOC = new Column();
  Column odbiciDATZAV = new Column();
  Column odbiciGLAVNICA = new Column();
  Column odbiciRATA = new Column();
  Column odbiciSALDO = new Column();
  Column odbiciSTAVKA = new Column();
  Column odbiciOZNVAL = new Column();
  Column odbiciPARAMETRI = new Column();

  public static Odbici getDataModule() {
    if (Odbiciclass == null) {
      Odbiciclass = new Odbici();
    }
    return Odbiciclass;
  }

  public QueryDataSet getQueryDataSet() {
    return odbici;
  }

  public Odbici() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    odbiciLOKK.setCaption("Status zauzetosti");
    odbiciLOKK.setColumnName("LOKK");
    odbiciLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbiciLOKK.setPrecision(1);
    odbiciLOKK.setTableName("ODBICI");
    odbiciLOKK.setServerColumnName("LOKK");
    odbiciLOKK.setSqlType(1);
    odbiciLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    odbiciLOKK.setDefault("N");
    odbiciAKTIV.setCaption("Aktivan - neaktivan");
    odbiciAKTIV.setColumnName("AKTIV");
    odbiciAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbiciAKTIV.setPrecision(1);
    odbiciAKTIV.setTableName("ODBICI");
    odbiciAKTIV.setServerColumnName("AKTIV");
    odbiciAKTIV.setSqlType(1);
    odbiciAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    odbiciAKTIV.setDefault("D");
    odbiciCVRODB.setCaption("Vrsta odbitka");
    odbiciCVRODB.setColumnName("CVRODB");
    odbiciCVRODB.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odbiciCVRODB.setPrecision(4);
    odbiciCVRODB.setRowId(true);
    odbiciCVRODB.setTableName("ODBICI");
    odbiciCVRODB.setServerColumnName("CVRODB");
    odbiciCVRODB.setSqlType(5);
    odbiciCVRODB.setWidth(4);
    odbiciCKEY.setCaption("Klju\u010D nivoa odbitka");
    odbiciCKEY.setColumnName("CKEY");
    odbiciCKEY.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbiciCKEY.setPrecision(30);
    odbiciCKEY.setRowId(true);
    odbiciCKEY.setTableName("ODBICI");
    odbiciCKEY.setServerColumnName("CKEY");
    odbiciCKEY.setSqlType(1);
    odbiciCKEY2.setCaption("Klju\u010D 2");
    odbiciCKEY2.setColumnName("CKEY2");
    odbiciCKEY2.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbiciCKEY2.setPrecision(30);
    odbiciCKEY2.setRowId(true);
    odbiciCKEY2.setTableName("ODBICI");
    odbiciCKEY2.setServerColumnName("CKEY2");
    odbiciCKEY2.setSqlType(1);
    odbiciRBRODB.setCaption("Redni broj odbitka");
    odbiciRBRODB.setColumnName("RBRODB");
    odbiciRBRODB.setDataType(com.borland.dx.dataset.Variant.SHORT);
    odbiciRBRODB.setPrecision(4);
    odbiciRBRODB.setRowId(true);
    odbiciRBRODB.setTableName("ODBICI");
    odbiciRBRODB.setServerColumnName("RBRODB");
    odbiciRBRODB.setSqlType(5);
    odbiciRBRODB.setWidth(4);
    odbiciPNB1.setCaption("Poziv na broj 1");
    odbiciPNB1.setColumnName("PNB1");
    odbiciPNB1.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbiciPNB1.setPrecision(2);
    odbiciPNB1.setTableName("ODBICI");
    odbiciPNB1.setServerColumnName("PNB1");
    odbiciPNB1.setSqlType(1);
    odbiciPNB2.setCaption("Poziv na broj 2");
    odbiciPNB2.setColumnName("PNB2");
    odbiciPNB2.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbiciPNB2.setPrecision(22);
    odbiciPNB2.setTableName("ODBICI");
    odbiciPNB2.setServerColumnName("PNB2");
    odbiciPNB2.setSqlType(1);
    odbiciIZNOS.setCaption("Iznos");
    odbiciIZNOS.setColumnName("IZNOS");
    odbiciIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odbiciIZNOS.setPrecision(17);
    odbiciIZNOS.setScale(2);
    odbiciIZNOS.setDisplayMask("###,###,##0.00");
    odbiciIZNOS.setDefault("0");
    odbiciIZNOS.setTableName("ODBICI");
    odbiciIZNOS.setServerColumnName("IZNOS");
    odbiciIZNOS.setSqlType(2);
    odbiciIZNOS.setDefault("0");
    odbiciSTOPA.setCaption("Stopa");
    odbiciSTOPA.setColumnName("STOPA");
    odbiciSTOPA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odbiciSTOPA.setPrecision(7);
    odbiciSTOPA.setScale(5);
    odbiciSTOPA.setDisplayMask("###,###,##0.00000");
    odbiciSTOPA.setDefault("0");
    odbiciSTOPA.setTableName("ODBICI");
    odbiciSTOPA.setServerColumnName("STOPA");
    odbiciSTOPA.setSqlType(2);
    odbiciSTOPA.setDefault("0");
    odbiciDATPOC.setCaption("Po\u010Detak obustave");
    odbiciDATPOC.setColumnName("DATPOC");
    odbiciDATPOC.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    odbiciDATPOC.setPrecision(8);
    odbiciDATPOC.setDisplayMask("dd-MM-yyyy");
//    odbiciDATPOC.setEditMask("dd-MM-yyyy");
    odbiciDATPOC.setTableName("ODBICI");
    odbiciDATPOC.setWidth(10);
    odbiciDATPOC.setServerColumnName("DATPOC");
    odbiciDATPOC.setSqlType(93);
    odbiciDATZAV.setCaption("Završetak obustave");
    odbiciDATZAV.setColumnName("DATZAV");
    odbiciDATZAV.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    odbiciDATZAV.setPrecision(8);
    odbiciDATZAV.setDisplayMask("dd-MM-yyyy");
//    odbiciDATZAV.setEditMask("dd-MM-yyyy");
    odbiciDATZAV.setTableName("ODBICI");
    odbiciDATZAV.setWidth(10);
    odbiciDATZAV.setServerColumnName("DATZAV");
    odbiciDATZAV.setSqlType(93);
    odbiciGLAVNICA.setCaption("Glavnica");
    odbiciGLAVNICA.setColumnName("GLAVNICA");
    odbiciGLAVNICA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odbiciGLAVNICA.setPrecision(17);
    odbiciGLAVNICA.setScale(2);
    odbiciGLAVNICA.setDisplayMask("###,###,##0.00");
    odbiciGLAVNICA.setDefault("0");
    odbiciGLAVNICA.setTableName("ODBICI");
    odbiciGLAVNICA.setServerColumnName("GLAVNICA");
    odbiciGLAVNICA.setSqlType(2);
    odbiciGLAVNICA.setDefault("0");
    odbiciRATA.setCaption("Rata");
    odbiciRATA.setColumnName("RATA");
    odbiciRATA.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odbiciRATA.setPrecision(17);
    odbiciRATA.setScale(2);
    odbiciRATA.setDisplayMask("###,###,##0.00");
    odbiciRATA.setDefault("0");
    odbiciRATA.setTableName("ODBICI");
    odbiciRATA.setServerColumnName("RATA");
    odbiciRATA.setSqlType(2);
    odbiciRATA.setDefault("0");
    odbiciSALDO.setCaption("Ostatak duga");
    odbiciSALDO.setColumnName("SALDO");
    odbiciSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    odbiciSALDO.setPrecision(17);
    odbiciSALDO.setScale(2);
    odbiciSALDO.setDisplayMask("###,###,##0.00");
    odbiciSALDO.setDefault("0");
    odbiciSALDO.setTableName("ODBICI");
    odbiciSALDO.setServerColumnName("SALDO");
    odbiciSALDO.setSqlType(2);
    odbiciSALDO.setDefault("0");
    odbiciSTAVKA.setCaption("Veza s kontom");
    odbiciSTAVKA.setColumnName("STAVKA");
    odbiciSTAVKA.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbiciSTAVKA.setPrecision(8);
    odbiciSTAVKA.setTableName("ODBICI");
    odbiciSTAVKA.setServerColumnName("STAVKA");
    odbiciSTAVKA.setSqlType(1);
    odbiciOZNVAL.setCaption("Valuta");
    odbiciOZNVAL.setColumnName("OZNVAL");
    odbiciOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbiciOZNVAL.setPrecision(3);
    odbiciOZNVAL.setTableName("ODBICI");
    odbiciOZNVAL.setServerColumnName("OZNVAL");
    odbiciOZNVAL.setSqlType(1);
    odbiciPARAMETRI.setCaption("Parametri");
    odbiciPARAMETRI.setColumnName("PARAMETRI");
    odbiciPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    odbiciPARAMETRI.setPrecision(20);
    odbiciPARAMETRI.setTableName("ODBICI");
    odbiciPARAMETRI.setServerColumnName("PARAMETRI");
    odbiciPARAMETRI.setSqlType(1);
    odbiciPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    odbici.setResolver(dm.getQresolver());
    odbici.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Odbici", null, true, Load.ALL));
 setColumns(new Column[] {odbiciLOKK, odbiciAKTIV, odbiciCVRODB, odbiciCKEY, odbiciCKEY2, odbiciRBRODB, odbiciPNB1, odbiciPNB2, odbiciIZNOS,
        odbiciSTOPA, odbiciDATPOC, odbiciDATZAV, odbiciGLAVNICA, odbiciRATA, odbiciSALDO, odbiciSTAVKA, odbiciOZNVAL, odbiciPARAMETRI});
  }

  public void setall() {

    ddl.create("Odbici")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("cvrodb", 4, true)
       .addChar("ckey", 30, true)
       .addChar("ckey2", 30, true)
       .addShort("rbrodb", 4, true)
       .addChar("pnb1", 2)
       .addChar("pnb2", 22)
       .addFloat("iznos", 17, 2)
       .addFloat("stopa", 7, 5)
       .addDate("datpoc")
       .addDate("datzav")
       .addFloat("glavnica", 17, 2)
       .addFloat("rata", 17, 2)
       .addFloat("saldo", 17, 2)
       .addChar("stavka", 8)
       .addChar("oznval", 3)
       .addChar("parametri", 20)
       .addPrimaryKey("cvrodb,ckey,ckey2,rbrodb");


    Naziv = "Odbici";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"ckey", "ckey2", "rbrodb"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
