/****license*****************************************************************
**   file: Detrepgkdata.java
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



public class Detrepgkdata extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Detrepgkdata Detrepgkdataclass;

  QueryDataSet detrepgkdata = new QueryDataSet();

  Column detrepgkdataAKTIV = new Column();
  Column detrepgkdataCREPGK = new Column();
  Column detrepgkdataRBSREPGK = new Column();
  Column detrepgkdataDATREPGK = new Column();
  Column detrepgkdataVAL1 = new Column();
  Column detrepgkdataVAL2 = new Column();
  Column detrepgkdataVAL3 = new Column();
  Column detrepgkdataPARAMS = new Column();

  public static Detrepgkdata getDataModule() {
    if (Detrepgkdataclass == null) {
      Detrepgkdataclass = new Detrepgkdata();
    }
    return Detrepgkdataclass;
  }

  public QueryDataSet getQueryDataSet() {
    return detrepgkdata;
  }

  public Detrepgkdata() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    detrepgkdataAKTIV.setCaption("Aktivan - neaktivan");
    detrepgkdataAKTIV.setColumnName("AKTIV");
    detrepgkdataAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    detrepgkdataAKTIV.setPrecision(1);
    detrepgkdataAKTIV.setTableName("DETREPGKDATA");
    detrepgkdataAKTIV.setServerColumnName("AKTIV");
    detrepgkdataAKTIV.setSqlType(1);
    detrepgkdataAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    detrepgkdataAKTIV.setDefault("D");
    detrepgkdataCREPGK.setCaption("Izvještaj");
    detrepgkdataCREPGK.setColumnName("CREPGK");
    detrepgkdataCREPGK.setDataType(com.borland.dx.dataset.Variant.INT);
    detrepgkdataCREPGK.setRowId(true);
    detrepgkdataCREPGK.setTableName("DETREPGKDATA");
    detrepgkdataCREPGK.setServerColumnName("CREPGK");
    detrepgkdataCREPGK.setSqlType(4);
    detrepgkdataCREPGK.setWidth(8);
    detrepgkdataRBSREPGK.setCaption("Oznaka stavke izvještaja");
    detrepgkdataRBSREPGK.setColumnName("RBSREPGK");
    detrepgkdataRBSREPGK.setDataType(com.borland.dx.dataset.Variant.INT);
    detrepgkdataRBSREPGK.setRowId(true);
    detrepgkdataRBSREPGK.setTableName("DETREPGKDATA");
    detrepgkdataRBSREPGK.setServerColumnName("RBSREPGK");
    detrepgkdataRBSREPGK.setSqlType(4);
    detrepgkdataRBSREPGK.setWidth(8);
    detrepgkdataDATREPGK.setCaption("Datum");
    detrepgkdataDATREPGK.setColumnName("DATREPGK");
    detrepgkdataDATREPGK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    detrepgkdataDATREPGK.setDisplayMask("dd-MM-yyyy");
//    detrepgkdataDATREPGK.setEditMask("dd-MM-yyyy");
    detrepgkdataDATREPGK.setRowId(true);
    detrepgkdataDATREPGK.setTableName("DETREPGKDATA");
    detrepgkdataDATREPGK.setServerColumnName("DATREPGK");
    detrepgkdataDATREPGK.setSqlType(93);
    detrepgkdataDATREPGK.setWidth(10);
    detrepgkdataVAL1.setCaption("Iznos 1");
    detrepgkdataVAL1.setColumnName("VAL1");
    detrepgkdataVAL1.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    detrepgkdataVAL1.setPrecision(17);
    detrepgkdataVAL1.setScale(2);
    detrepgkdataVAL1.setDisplayMask("###,###,##0.00");
    detrepgkdataVAL1.setDefault("0");
    detrepgkdataVAL1.setTableName("DETREPGKDATA");
    detrepgkdataVAL1.setServerColumnName("VAL1");
    detrepgkdataVAL1.setSqlType(2);
    detrepgkdataVAL1.setDefault("0");
    detrepgkdataVAL2.setCaption("Iznos 2");
    detrepgkdataVAL2.setColumnName("VAL2");
    detrepgkdataVAL2.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    detrepgkdataVAL2.setPrecision(17);
    detrepgkdataVAL2.setScale(2);
    detrepgkdataVAL2.setDisplayMask("###,###,##0.00");
    detrepgkdataVAL2.setDefault("0");
    detrepgkdataVAL2.setTableName("DETREPGKDATA");
    detrepgkdataVAL2.setServerColumnName("VAL2");
    detrepgkdataVAL2.setSqlType(2);
    detrepgkdataVAL2.setDefault("0");
    detrepgkdataVAL3.setCaption("Iznos 3");
    detrepgkdataVAL3.setColumnName("VAL3");
    detrepgkdataVAL3.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    detrepgkdataVAL3.setPrecision(17);
    detrepgkdataVAL3.setScale(2);
    detrepgkdataVAL3.setDisplayMask("###,###,##0.00");
    detrepgkdataVAL3.setDefault("0");
    detrepgkdataVAL3.setTableName("DETREPGKDATA");
    detrepgkdataVAL3.setServerColumnName("VAL3");
    detrepgkdataVAL3.setSqlType(2);
    detrepgkdataVAL3.setDefault("0");
    detrepgkdataPARAMS.setCaption("Parametri");
    detrepgkdataPARAMS.setColumnName("PARAMS");
    detrepgkdataPARAMS.setDataType(com.borland.dx.dataset.Variant.STRING);
    detrepgkdataPARAMS.setPrecision(20);
    detrepgkdataPARAMS.setTableName("DETREPGKDATA");
    detrepgkdataPARAMS.setServerColumnName("PARAMS");
    detrepgkdataPARAMS.setSqlType(1);
    detrepgkdata.setResolver(dm.getQresolver());
    detrepgkdata.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Detrepgkdata", null, true, Load.ALL));
    setColumns(new Column[] {detrepgkdataAKTIV, detrepgkdataCREPGK, detrepgkdataRBSREPGK, detrepgkdataDATREPGK, detrepgkdataVAL1, 
        detrepgkdataVAL2, detrepgkdataVAL3, detrepgkdataPARAMS});
  }

  public void setall() {

    ddl.create("Detrepgkdata")
       .addChar("aktiv", 1, "D")
       .addInteger("crepgk", 8, true)
       .addInteger("rbsrepgk", 8, true)
       .addDate("datrepgk", true)
       .addFloat("val1", 17, 2)
       .addFloat("val2", 17, 2)
       .addFloat("val3", 17, 2)
       .addChar("params", 20)
       .addPrimaryKey("crepgk,rbsrepgk,datrepgk");


    Naziv = "Detrepgkdata";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"crepgk", "rbsrepgk", "datrepgk"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
