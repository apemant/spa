/****license*****************************************************************
**   file: Nalozi.java
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



public class Nalozi extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Nalozi Naloziclass;

  QueryDataSet nalozi = new raDataSet();

  Column naloziLOKK = new Column();
  Column naloziAKTIV = new Column();
  Column naloziCNALOGA = new Column();
  Column naloziKNJIG = new Column();
  Column naloziGOD = new Column();
  Column naloziCVRNAL = new Column();
  Column naloziRBR = new Column();
  Column naloziDATUMKNJ = new Column();
  Column naloziKONTRIZNOS = new Column();
  Column naloziID = new Column();
  Column naloziIP = new Column();
  Column naloziSTATUS = new Column();
  Column naloziDATUM = new Column();
  Column naloziPICK = new Column();
  Column naloziSALDO = new Column();

  public static Nalozi getDataModule() {
    if (Naloziclass == null) {
      Naloziclass = new Nalozi();
    }
    return Naloziclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return nalozi;
  }

  public Nalozi() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    naloziLOKK.setCaption("Status zauzetosti");
    naloziLOKK.setColumnName("LOKK");
    naloziLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    naloziLOKK.setPrecision(1);
    naloziLOKK.setTableName("NALOZI");
    naloziLOKK.setServerColumnName("LOKK");
    naloziLOKK.setSqlType(1);
    naloziLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    naloziLOKK.setDefault("N");
    naloziAKTIV.setCaption("Aktivan - neaktivan");
    naloziAKTIV.setColumnName("AKTIV");
    naloziAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    naloziAKTIV.setPrecision(1);
    naloziAKTIV.setTableName("NALOZI");
    naloziAKTIV.setServerColumnName("AKTIV");
    naloziAKTIV.setSqlType(1);
    naloziAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    naloziAKTIV.setDefault("D");
    naloziCNALOGA.setCaption("Broj naloga");
    naloziCNALOGA.setColumnName("CNALOGA");
    naloziCNALOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    naloziCNALOGA.setPrecision(30);
    naloziCNALOGA.setTableName("NALOZI");
    naloziCNALOGA.setServerColumnName("CNALOGA");
    naloziCNALOGA.setSqlType(1);
    naloziKNJIG.setCaption("Knjigovodstvo");
    naloziKNJIG.setColumnName("KNJIG");
    naloziKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    naloziKNJIG.setPrecision(12);
    naloziKNJIG.setRowId(true);
    naloziKNJIG.setTableName("NALOZI");
    naloziKNJIG.setServerColumnName("KNJIG");
    naloziKNJIG.setSqlType(1);
    naloziGOD.setCaption("Godina");
    naloziGOD.setColumnName("GOD");
    naloziGOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    naloziGOD.setPrecision(4);
    naloziGOD.setRowId(true);
    naloziGOD.setTableName("NALOZI");
    naloziGOD.setServerColumnName("GOD");
    naloziGOD.setSqlType(1);
    naloziCVRNAL.setCaption("Oznaka vrste naloga");
    naloziCVRNAL.setColumnName("CVRNAL");
    naloziCVRNAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    naloziCVRNAL.setPrecision(2);
    naloziCVRNAL.setRowId(true);
    naloziCVRNAL.setTableName("NALOZI");
    naloziCVRNAL.setServerColumnName("CVRNAL");
    naloziCVRNAL.setSqlType(1);
    naloziRBR.setCaption("RBR naloga");
    naloziRBR.setColumnName("RBR");
    naloziRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    naloziRBR.setPrecision(6);
    naloziRBR.setRowId(true);
    naloziRBR.setTableName("NALOZI");
    naloziRBR.setServerColumnName("RBR");
    naloziRBR.setSqlType(4);
    naloziRBR.setWidth(6);
    naloziDATUMKNJ.setCaption("Datum knjiženja");
    naloziDATUMKNJ.setColumnName("DATUMKNJ");
    naloziDATUMKNJ.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    naloziDATUMKNJ.setPrecision(8);
    naloziDATUMKNJ.setDisplayMask("dd-MM-yyyy");
//    naloziDATUMKNJ.setEditMask("dd-MM-yyyy");
    naloziDATUMKNJ.setTableName("NALOZI");
    naloziDATUMKNJ.setWidth(10);
    naloziDATUMKNJ.setServerColumnName("DATUMKNJ");
    naloziDATUMKNJ.setSqlType(93);
    naloziKONTRIZNOS.setCaption("Kontrolni iznos");
    naloziKONTRIZNOS.setColumnName("KONTRIZNOS");
    naloziKONTRIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    naloziKONTRIZNOS.setPrecision(17);
    naloziKONTRIZNOS.setScale(2);
    naloziKONTRIZNOS.setDisplayMask("###,###,##0.00");
    naloziKONTRIZNOS.setDefault("0");
    naloziKONTRIZNOS.setTableName("NALOZI");
    naloziKONTRIZNOS.setServerColumnName("KONTRIZNOS");
    naloziKONTRIZNOS.setSqlType(2);
    naloziKONTRIZNOS.setDefault("0");
    naloziID.setCaption("Iznos duguje");
    naloziID.setColumnName("ID");
    naloziID.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    naloziID.setPrecision(17);
    naloziID.setScale(2);
    naloziID.setDisplayMask("###,###,##0.00");
    naloziID.setDefault("0");
    naloziID.setTableName("NALOZI");
    naloziID.setServerColumnName("ID");
    naloziID.setSqlType(2);
    naloziID.setDefault("0");
    naloziIP.setCaption("Iznos potražuje");
    naloziIP.setColumnName("IP");
    naloziIP.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    naloziIP.setPrecision(17);
    naloziIP.setScale(2);
    naloziIP.setDisplayMask("###,###,##0.00");
    naloziIP.setDefault("0");
    naloziIP.setTableName("NALOZI");
    naloziIP.setServerColumnName("IP");
    naloziIP.setSqlType(2);
    naloziIP.setDefault("0");
    naloziSTATUS.setCaption("Status");
    naloziSTATUS.setColumnName("STATUS");
    naloziSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    naloziSTATUS.setPrecision(1);
    naloziSTATUS.setTableName("NALOZI");
    naloziSTATUS.setServerColumnName("STATUS");
    naloziSTATUS.setSqlType(1);
    naloziSTATUS.setDefault("");
    naloziDATUM.setCaption("Datum");
    naloziDATUM.setColumnName("DATUM");
    naloziDATUM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    naloziDATUM.setPrecision(8);
    naloziDATUM.setDisplayMask("dd-MM-yyyy");
//    naloziDATUM.setEditMask("dd-MM-yyyy");
    naloziDATUM.setTableName("NALOZI");
    naloziDATUM.setWidth(10);
    naloziDATUM.setServerColumnName("DATUM");
    naloziDATUM.setSqlType(93);
    naloziPICK.setCaption("Indikator za odabire");
    naloziPICK.setColumnName("PICK");
    naloziPICK.setDataType(com.borland.dx.dataset.Variant.STRING);
    naloziPICK.setPrecision(1);
    naloziPICK.setTableName("NALOZI");
    naloziPICK.setServerColumnName("PICK");
    naloziPICK.setSqlType(1);
    naloziPICK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    naloziSALDO.setCaption("Saldo");
    naloziSALDO.setColumnName("SALDO");
    naloziSALDO.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    naloziSALDO.setPrecision(17);
    naloziSALDO.setScale(2);
    naloziSALDO.setDisplayMask("###,###,##0.00");
    naloziSALDO.setDefault("0");
    naloziSALDO.setTableName("NALOZI");
    naloziSALDO.setServerColumnName("SALDO");
    naloziSALDO.setSqlType(2);
    naloziSALDO.setDefault("0");
    nalozi.setResolver(dm.getQresolver());
    nalozi.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from Nalozi", null, true, Load.ALL));
 setColumns(new Column[] {naloziLOKK, naloziAKTIV, naloziCNALOGA, naloziKNJIG, naloziGOD, naloziCVRNAL, naloziRBR, naloziDATUMKNJ, naloziKONTRIZNOS,
        naloziID, naloziIP, naloziSTATUS, naloziDATUM, naloziPICK, naloziSALDO});
  }

  public void setall() {

    ddl.create("Nalozi")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cnaloga", 30)
       .addChar("knjig", 12, true)
       .addChar("god", 4, true)
       .addChar("cvrnal", 2, true)
       .addInteger("rbr", 6, true)
       .addDate("datumknj")
       .addFloat("kontriznos", 17, 2)
       .addFloat("id", 17, 2)
       .addFloat("ip", 17, 2)
       .addChar("status", 1)
       .addDate("datum")
       .addChar("pick", 1)
       .addFloat("saldo", 17, 2)
       .addPrimaryKey("knjig,god,cvrnal,rbr");


    Naziv = "Nalozi";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"rbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
