/****license*****************************************************************
**   file: Repgkdata.java
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



public class Repgkdata extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Repgkdata Repgkdataclass;

  QueryDataSet repgkdata = new QueryDataSet();

  Column repgkdataLOKK = new Column();
  Column repgkdataAKTIV = new Column();
  Column repgkdataCREPGK = new Column();
  Column repgkdataDATREPGK = new Column();
  Column repgkdataCOMMENT = new Column();
  Column repgkdataDAT1_FROM = new Column();
  Column repgkdataDAT1_TO = new Column();
  Column repgkdataDAT2_FROM = new Column();
  Column repgkdataDAT2_TO = new Column();
  Column repgkdataDAT3_FROM = new Column();
  Column repgkdataDAT3_TO = new Column();
  Column repgkdataPARAMS = new Column();

  public static Repgkdata getDataModule() {
    if (Repgkdataclass == null) {
      Repgkdataclass = new Repgkdata();
    }
    return Repgkdataclass;
  }

  public QueryDataSet getQueryDataSet() {
    return repgkdata;
  }

  public Repgkdata() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    repgkdataLOKK.setCaption("Status zauzetosti");
    repgkdataLOKK.setColumnName("LOKK");
    repgkdataLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    repgkdataLOKK.setPrecision(1);
    repgkdataLOKK.setTableName("REPGKDATA");
    repgkdataLOKK.setServerColumnName("LOKK");
    repgkdataLOKK.setSqlType(1);
    repgkdataLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    repgkdataLOKK.setDefault("N");
    repgkdataAKTIV.setCaption("Aktivan - neaktivan");
    repgkdataAKTIV.setColumnName("AKTIV");
    repgkdataAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    repgkdataAKTIV.setPrecision(1);
    repgkdataAKTIV.setTableName("REPGKDATA");
    repgkdataAKTIV.setServerColumnName("AKTIV");
    repgkdataAKTIV.setSqlType(1);
    repgkdataAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    repgkdataAKTIV.setDefault("D");
    repgkdataCREPGK.setCaption("Izvještaj");
    repgkdataCREPGK.setColumnName("CREPGK");
    repgkdataCREPGK.setDataType(com.borland.dx.dataset.Variant.INT);
    repgkdataCREPGK.setRowId(true);
    repgkdataCREPGK.setTableName("REPGKDATA");
    repgkdataCREPGK.setServerColumnName("CREPGK");
    repgkdataCREPGK.setSqlType(4);
    repgkdataCREPGK.setWidth(8);
    repgkdataDATREPGK.setCaption("Datum");
    repgkdataDATREPGK.setColumnName("DATREPGK");
    repgkdataDATREPGK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    repgkdataDATREPGK.setDisplayMask("dd-MM-yyyy");
//    repgkdataDATREPGK.setEditMask("dd-MM-yyyy");
    repgkdataDATREPGK.setRowId(true);
    repgkdataDATREPGK.setTableName("REPGKDATA");
    repgkdataDATREPGK.setServerColumnName("DATREPGK");
    repgkdataDATREPGK.setSqlType(93);
    repgkdataDATREPGK.setWidth(10);
    repgkdataCOMMENT.setCaption("Komentar");
    repgkdataCOMMENT.setColumnName("COMMENT");
    repgkdataCOMMENT.setDataType(com.borland.dx.dataset.Variant.STRING);
    repgkdataCOMMENT.setPrecision(200);
    repgkdataCOMMENT.setTableName("REPGKDATA");
    repgkdataCOMMENT.setServerColumnName("COMMENT");
    repgkdataCOMMENT.setSqlType(1);
    repgkdataCOMMENT.setWidth(30);
    repgkdataDAT1_FROM.setCaption("OD za 1.iznos");
    repgkdataDAT1_FROM.setColumnName("DAT1_FROM");
    repgkdataDAT1_FROM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    repgkdataDAT1_FROM.setDisplayMask("dd-MM-yyyy");
//    repgkdataDAT1_FROM.setEditMask("dd-MM-yyyy");
    repgkdataDAT1_FROM.setTableName("REPGKDATA");
    repgkdataDAT1_FROM.setServerColumnName("DAT1_FROM");
    repgkdataDAT1_FROM.setSqlType(93);
    repgkdataDAT1_FROM.setWidth(10);
    repgkdataDAT1_TO.setCaption("DO za 1.iznos");
    repgkdataDAT1_TO.setColumnName("DAT1_TO");
    repgkdataDAT1_TO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    repgkdataDAT1_TO.setDisplayMask("dd-MM-yyyy");
//    repgkdataDAT1_TO.setEditMask("dd-MM-yyyy");
    repgkdataDAT1_TO.setTableName("REPGKDATA");
    repgkdataDAT1_TO.setServerColumnName("DAT1_TO");
    repgkdataDAT1_TO.setSqlType(93);
    repgkdataDAT1_TO.setWidth(10);
    repgkdataDAT2_FROM.setCaption("OD za 2.iznos");
    repgkdataDAT2_FROM.setColumnName("DAT2_FROM");
    repgkdataDAT2_FROM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    repgkdataDAT2_FROM.setDisplayMask("dd-MM-yyyy");
//    repgkdataDAT2_FROM.setEditMask("dd-MM-yyyy");
    repgkdataDAT2_FROM.setTableName("REPGKDATA");
    repgkdataDAT2_FROM.setServerColumnName("DAT2_FROM");
    repgkdataDAT2_FROM.setSqlType(93);
    repgkdataDAT2_FROM.setWidth(10);
    repgkdataDAT2_TO.setCaption("DO za 2.iznos");
    repgkdataDAT2_TO.setColumnName("DAT2_TO");
    repgkdataDAT2_TO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    repgkdataDAT2_TO.setDisplayMask("dd-MM-yyyy");
//    repgkdataDAT2_TO.setEditMask("dd-MM-yyyy");
    repgkdataDAT2_TO.setTableName("REPGKDATA");
    repgkdataDAT2_TO.setServerColumnName("DAT2_TO");
    repgkdataDAT2_TO.setSqlType(93);
    repgkdataDAT2_TO.setWidth(10);
    repgkdataDAT3_FROM.setCaption("OD za 3.iznos");
    repgkdataDAT3_FROM.setColumnName("DAT3_FROM");
    repgkdataDAT3_FROM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    repgkdataDAT3_FROM.setDisplayMask("dd-MM-yyyy");
//    repgkdataDAT3_FROM.setEditMask("dd-MM-yyyy");
    repgkdataDAT3_FROM.setTableName("REPGKDATA");
    repgkdataDAT3_FROM.setServerColumnName("DAT3_FROM");
    repgkdataDAT3_FROM.setSqlType(93);
    repgkdataDAT3_FROM.setWidth(10);
    repgkdataDAT3_TO.setCaption("DO za 3.iznos");
    repgkdataDAT3_TO.setColumnName("DAT3_TO");
    repgkdataDAT3_TO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    repgkdataDAT3_TO.setDisplayMask("dd-MM-yyyy");
//    repgkdataDAT3_TO.setEditMask("dd-MM-yyyy");
    repgkdataDAT3_TO.setTableName("REPGKDATA");
    repgkdataDAT3_TO.setServerColumnName("DAT3_TO");
    repgkdataDAT3_TO.setSqlType(93);
    repgkdataDAT3_TO.setWidth(10);
    repgkdataPARAMS.setCaption("Parametri");
    repgkdataPARAMS.setColumnName("PARAMS");
    repgkdataPARAMS.setDataType(com.borland.dx.dataset.Variant.STRING);
    repgkdataPARAMS.setPrecision(20);
    repgkdataPARAMS.setTableName("REPGKDATA");
    repgkdataPARAMS.setServerColumnName("PARAMS");
    repgkdataPARAMS.setSqlType(1);
    repgkdata.setResolver(dm.getQresolver());
    repgkdata.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Repgkdata", null, true, Load.ALL));
    setColumns(new Column[] {repgkdataLOKK, repgkdataAKTIV, repgkdataCREPGK, repgkdataDATREPGK, repgkdataCOMMENT, repgkdataDAT1_FROM, 
        repgkdataDAT1_TO, repgkdataDAT2_FROM, repgkdataDAT2_TO, repgkdataDAT3_FROM, repgkdataDAT3_TO, repgkdataPARAMS});
  }

  public void setall() {

    ddl.create("Repgkdata")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("crepgk", 8, true)
       .addDate("datrepgk", true)
       .addChar("comment", 200)
       .addDate("dat1_from")
       .addDate("dat1_to")
       .addDate("dat2_from")
       .addDate("dat2_to")
       .addDate("dat3_from")
       .addDate("dat3_to")
       .addChar("params", 20)
       .addPrimaryKey("crepgk,datrepgk");


    Naziv = "Repgkdata";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"crepgk", "datrepgk"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
