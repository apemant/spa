/****license*****************************************************************
**   file: Virmani.java
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



public class Virmani extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Virmani Virmaniclass;

  QueryDataSet virmani = new raDataSet();

  Column virmaniLOKK = new Column();
  Column virmaniAKTIV = new Column();
  Column virmaniAPP = new Column();
  Column virmaniKNJIG = new Column();
  Column virmaniCKEY = new Column();
  Column virmaniRBR = new Column();
  Column virmaniJEDZAV = new Column();
  Column virmaniNATERET = new Column();
  Column virmaniSVRHA = new Column();
  Column virmaniUKORIST = new Column();
  Column virmaniBRRACNT = new Column();
  Column virmaniNACIZV = new Column();
  Column virmaniPNBZ1 = new Column();
  Column virmaniPNBZ2 = new Column();
  Column virmaniSIF1 = new Column();
  Column virmaniSIF2 = new Column();
  Column virmaniSIF3 = new Column();
  Column virmaniBRRACUK = new Column();
  Column virmaniPNBO1 = new Column();
  Column virmaniPNBO2 = new Column();
  Column virmaniIZNOS = new Column();
  Column virmaniMJESTO = new Column();
  Column virmaniDATUMIZV = new Column();
  Column virmaniDATUMPR = new Column();

  public static Virmani getDataModule() {
    if (Virmaniclass == null) {
      Virmaniclass = new Virmani();
    }
    return Virmaniclass;
  }

  public QueryDataSet getQueryDataSet() {
    return virmani;
  }

  public Virmani() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    virmaniLOKK.setCaption("Status zauzetosti");
    virmaniLOKK.setColumnName("LOKK");
    virmaniLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniLOKK.setPrecision(1);
    virmaniLOKK.setTableName("VIRMANI");
    virmaniLOKK.setServerColumnName("LOKK");
    virmaniLOKK.setSqlType(1);
    virmaniLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    virmaniLOKK.setDefault("N");
    virmaniAKTIV.setCaption("Aktivan - neaktivan");
    virmaniAKTIV.setColumnName("AKTIV");
    virmaniAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniAKTIV.setPrecision(1);
    virmaniAKTIV.setTableName("VIRMANI");
    virmaniAKTIV.setServerColumnName("AKTIV");
    virmaniAKTIV.setSqlType(1);
    virmaniAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    virmaniAKTIV.setDefault("D");
    virmaniAPP.setCaption("Aplikacija");
    virmaniAPP.setColumnName("APP");
    virmaniAPP.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniAPP.setPrecision(10);
    virmaniAPP.setRowId(true);
    virmaniAPP.setTableName("VIRMANI");
    virmaniAPP.setServerColumnName("APP");
    virmaniAPP.setSqlType(1);
    virmaniAPP.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    virmaniKNJIG.setCaption("Knjigovodstvo");
    virmaniKNJIG.setColumnName("KNJIG");
    virmaniKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniKNJIG.setPrecision(12);
    virmaniKNJIG.setRowId(true);
    virmaniKNJIG.setTableName("VIRMANI");
    virmaniKNJIG.setServerColumnName("KNJIG");
    virmaniKNJIG.setSqlType(1);
    virmaniCKEY.setCaption("Identifikator");
    virmaniCKEY.setColumnName("CKEY");
    virmaniCKEY.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniCKEY.setPrecision(30);
    virmaniCKEY.setRowId(true);
    virmaniCKEY.setTableName("VIRMANI");
    virmaniCKEY.setServerColumnName("CKEY");
    virmaniCKEY.setSqlType(1);
    virmaniCKEY.setWidth(30);
    virmaniRBR.setCaption("RBR");
    virmaniRBR.setColumnName("RBR");
    virmaniRBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    virmaniRBR.setPrecision(4);
    virmaniRBR.setRowId(true);
    virmaniRBR.setTableName("VIRMANI");
    virmaniRBR.setServerColumnName("RBR");
    virmaniRBR.setSqlType(5);
    virmaniRBR.setWidth(4);
    virmaniJEDZAV.setCaption("Jedinica zavoda");
    virmaniJEDZAV.setColumnName("JEDZAV");
    virmaniJEDZAV.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniJEDZAV.setPrecision(20);
    virmaniJEDZAV.setTableName("VIRMANI");
    virmaniJEDZAV.setServerColumnName("JEDZAV");
    virmaniJEDZAV.setSqlType(1);
    virmaniNATERET.setCaption("Na teret ra\u010Duna");
    virmaniNATERET.setColumnName("NATERET");
    virmaniNATERET.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniNATERET.setPrecision(100);
    virmaniNATERET.setTableName("VIRMANI");
    virmaniNATERET.setServerColumnName("NATERET");
    virmaniNATERET.setSqlType(1);
    virmaniNATERET.setWidth(30);
    virmaniSVRHA.setCaption("Svrha doznake");
    virmaniSVRHA.setColumnName("SVRHA");
    virmaniSVRHA.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniSVRHA.setPrecision(100);
    virmaniSVRHA.setTableName("VIRMANI");
    virmaniSVRHA.setServerColumnName("SVRHA");
    virmaniSVRHA.setSqlType(1);
    virmaniSVRHA.setWidth(30);
    virmaniUKORIST.setCaption("U korist ra\u010Duna");
    virmaniUKORIST.setColumnName("UKORIST");
    virmaniUKORIST.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniUKORIST.setPrecision(100);
    virmaniUKORIST.setTableName("VIRMANI");
    virmaniUKORIST.setServerColumnName("UKORIST");
    virmaniUKORIST.setSqlType(1);
    virmaniUKORIST.setWidth(30);
    virmaniBRRACNT.setCaption("Broj ra\u010Duna na teret");
    virmaniBRRACNT.setColumnName("BRRACNT");
    virmaniBRRACNT.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniBRRACNT.setPrecision(30);
    virmaniBRRACNT.setTableName("VIRMANI");
    virmaniBRRACNT.setServerColumnName("BRRACNT");
    virmaniBRRACNT.setSqlType(1);
    virmaniBRRACNT.setWidth(30);
    virmaniNACIZV.setCaption("Na\u010Din izvrš");
    virmaniNACIZV.setColumnName("NACIZV");
    virmaniNACIZV.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniNACIZV.setPrecision(2);
    virmaniNACIZV.setTableName("VIRMANI");
    virmaniNACIZV.setServerColumnName("NACIZV");
    virmaniNACIZV.setSqlType(1);
    virmaniPNBZ1.setCaption("Poziv na broj (zaduž.) 1");
    virmaniPNBZ1.setColumnName("PNBZ1");
    virmaniPNBZ1.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniPNBZ1.setPrecision(2);
    virmaniPNBZ1.setTableName("VIRMANI");
    virmaniPNBZ1.setServerColumnName("PNBZ1");
    virmaniPNBZ1.setSqlType(1);
    virmaniPNBZ2.setCaption("Poziv na broj (zaduž.) 2");
    virmaniPNBZ2.setColumnName("PNBZ2");
    virmaniPNBZ2.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniPNBZ2.setPrecision(30);
    virmaniPNBZ2.setTableName("VIRMANI");
    virmaniPNBZ2.setServerColumnName("PNBZ2");
    virmaniPNBZ2.setSqlType(1);
    virmaniPNBZ2.setWidth(30);
    virmaniSIF1.setCaption("Šifra 1");
    virmaniSIF1.setColumnName("SIF1");
    virmaniSIF1.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniSIF1.setPrecision(2);
    virmaniSIF1.setTableName("VIRMANI");
    virmaniSIF1.setServerColumnName("SIF1");
    virmaniSIF1.setSqlType(1);
    virmaniSIF2.setCaption("Šifra 2");
    virmaniSIF2.setColumnName("SIF2");
    virmaniSIF2.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniSIF2.setPrecision(2);
    virmaniSIF2.setTableName("VIRMANI");
    virmaniSIF2.setServerColumnName("SIF2");
    virmaniSIF2.setSqlType(1);
    virmaniSIF3.setCaption("Šifra 3");
    virmaniSIF3.setColumnName("SIF3");
    virmaniSIF3.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniSIF3.setPrecision(2);
    virmaniSIF3.setTableName("VIRMANI");
    virmaniSIF3.setServerColumnName("SIF3");
    virmaniSIF3.setSqlType(1);
    virmaniBRRACUK.setCaption("Broj ra\u010Duna u korist");
    virmaniBRRACUK.setColumnName("BRRACUK");
    virmaniBRRACUK.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniBRRACUK.setPrecision(30);
    virmaniBRRACUK.setTableName("VIRMANI");
    virmaniBRRACUK.setServerColumnName("BRRACUK");
    virmaniBRRACUK.setSqlType(1);
    virmaniBRRACUK.setWidth(30);
    virmaniPNBO1.setCaption("Poziv na broj (odobr.) 1");
    virmaniPNBO1.setColumnName("PNBO1");
    virmaniPNBO1.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniPNBO1.setPrecision(2);
    virmaniPNBO1.setTableName("VIRMANI");
    virmaniPNBO1.setServerColumnName("PNBO1");
    virmaniPNBO1.setSqlType(1);
    virmaniPNBO2.setCaption("Poziv na broj (odobr.) 2");
    virmaniPNBO2.setColumnName("PNBO2");
    virmaniPNBO2.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniPNBO2.setPrecision(30);
    virmaniPNBO2.setTableName("VIRMANI");
    virmaniPNBO2.setServerColumnName("PNBO2");
    virmaniPNBO2.setSqlType(1);
    virmaniPNBO2.setWidth(30);
    virmaniIZNOS.setCaption("Iznos");
    virmaniIZNOS.setColumnName("IZNOS");
    virmaniIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    virmaniIZNOS.setPrecision(17);
    virmaniIZNOS.setScale(2);
    virmaniIZNOS.setDisplayMask("###,###,##0.00");
    virmaniIZNOS.setDefault("0");
    virmaniIZNOS.setTableName("VIRMANI");
    virmaniIZNOS.setServerColumnName("IZNOS");
    virmaniIZNOS.setSqlType(2);
    virmaniMJESTO.setCaption("Mjesto");
    virmaniMJESTO.setColumnName("MJESTO");
    virmaniMJESTO.setDataType(com.borland.dx.dataset.Variant.STRING);
    virmaniMJESTO.setPrecision(30);
    virmaniMJESTO.setTableName("VIRMANI");
    virmaniMJESTO.setServerColumnName("MJESTO");
    virmaniMJESTO.setSqlType(1);
    virmaniMJESTO.setWidth(30);
    virmaniDATUMIZV.setCaption("Datum izvršenja");
    virmaniDATUMIZV.setColumnName("DATUMIZV");
    virmaniDATUMIZV.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    virmaniDATUMIZV.setPrecision(8);
    virmaniDATUMIZV.setDisplayMask("dd-MM-yyyy");
//    virmaniDATUMIZV.setEditMask("dd-MM-yyyy");
    virmaniDATUMIZV.setTableName("VIRMANI");
    virmaniDATUMIZV.setServerColumnName("DATUMIZV");
    virmaniDATUMIZV.setSqlType(93);
    virmaniDATUMIZV.setWidth(10);
    virmaniDATUMPR.setCaption("Datum predaje");
    virmaniDATUMPR.setColumnName("DATUMPR");
    virmaniDATUMPR.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    virmaniDATUMPR.setPrecision(8);
    virmaniDATUMPR.setDisplayMask("dd-MM-yyyy");
//    virmaniDATUMPR.setEditMask("dd-MM-yyyy");
    virmaniDATUMPR.setTableName("VIRMANI");
    virmaniDATUMPR.setServerColumnName("DATUMPR");
    virmaniDATUMPR.setSqlType(93);
    virmaniDATUMPR.setWidth(10);
    virmani.setResolver(dm.getQresolver());
    virmani.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Virmani", null, true, Load.ALL));
 setColumns(new Column[] {virmaniLOKK, virmaniAKTIV, virmaniAPP, virmaniKNJIG, virmaniCKEY, virmaniRBR, virmaniJEDZAV, virmaniNATERET, virmaniSVRHA,
        virmaniUKORIST, virmaniBRRACNT, virmaniNACIZV, virmaniPNBZ1, virmaniPNBZ2, virmaniSIF1, virmaniSIF2, virmaniSIF3, virmaniBRRACUK, virmaniPNBO1,
        virmaniPNBO2, virmaniIZNOS, virmaniMJESTO, virmaniDATUMIZV, virmaniDATUMPR});
  }

  public void setall() {

    ddl.create("Virmani")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("app", 10, true)
       .addChar("knjig", 12, true)
       .addChar("ckey", 30, true)
       .addShort("rbr", 4, true)
       .addChar("jedzav", 20)
       .addChar("nateret", 100)
       .addChar("svrha", 100)
       .addChar("ukorist", 100)
       .addChar("brracnt", 30)
       .addChar("nacizv", 2)
       .addChar("pnbz1", 2)
       .addChar("pnbz2", 30)
       .addChar("sif1", 2)
       .addChar("sif2", 2)
       .addChar("sif3", 2)
       .addChar("brracuk", 30)
       .addChar("pnbo1", 2)
       .addChar("pnbo2", 30)
       .addFloat("iznos", 17, 2)
       .addChar("mjesto", 30)
       .addDate("datumizv")
       .addDate("datumpr")
       .addPrimaryKey("app,knjig,ckey,rbr");


    Naziv = "Virmani";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"ckey", "rbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
