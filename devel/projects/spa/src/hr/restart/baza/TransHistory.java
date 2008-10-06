/****license*****************************************************************
**   file: TransHistory.java
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



public class TransHistory extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static TransHistory TransHistoryclass;

  QueryDataSet trh = new QueryDataSet();

  Column trhFILENAMECLIJENT = new Column();
  Column trhFILENAMESERVER = new Column();
  Column trhSTATUS = new Column();
  Column trhDATPRIJENOS = new Column();
  Column trhDATPOTVRDE = new Column();
  Column trhKOMENTAR = new Column();

  public static TransHistory getDataModule() {
    if (TransHistoryclass == null) {
      TransHistoryclass = new TransHistory();
    }
    return TransHistoryclass;
  }

  public QueryDataSet getQueryDataSet() {
    return trh;
  }

  public TransHistory() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    trhFILENAMECLIJENT.setCaption("Ime datoteke");
    trhFILENAMECLIJENT.setColumnName("FILENAMECLIENT");
    trhFILENAMECLIJENT.setDataType(com.borland.dx.dataset.Variant.STRING);
    trhFILENAMECLIJENT.setPrecision(100);
    trhFILENAMECLIJENT.setRowId(true);
    trhFILENAMECLIJENT.setTableName("TRANSHISTORY");
    trhFILENAMECLIJENT.setServerColumnName("FILENAMECLIENT");
    trhFILENAMECLIJENT.setSqlType(1);
    trhFILENAMESERVER.setCaption("Ime datoteke");
    trhFILENAMESERVER.setColumnName("FILENAMESERVER");
    trhFILENAMESERVER.setDataType(com.borland.dx.dataset.Variant.STRING);
    trhFILENAMESERVER.setPrecision(100);
    trhFILENAMESERVER.setTableName("TRANSHISTORY");
    trhFILENAMESERVER.setServerColumnName("FILENAMESERVER");
    trhFILENAMESERVER.setSqlType(1);
    trhSTATUS.setCaption("Status prenesenosti");
    trhSTATUS.setColumnName("STATUS");
    trhSTATUS.setDataType(com.borland.dx.dataset.Variant.STRING);
    trhSTATUS.setPrecision(1);
    trhSTATUS.setTableName("TRANSHISTORY");
    trhSTATUS.setServerColumnName("STATUS");
    trhSTATUS.setSqlType(1);
    trhSTATUS.setDefault("N");
    trhDATPRIJENOS.setCaption("Datum prijenosa");
    trhDATPRIJENOS.setColumnName("DATPRIJENOS");
    trhDATPRIJENOS.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    trhDATPRIJENOS.setDisplayMask("dd-MM-yyyy");
//    trhDATPRIJENOS.setEditMask("dd-MM-yyyy");
    trhDATPRIJENOS.setTableName("TRANSHISTORY");
    trhDATPRIJENOS.setServerColumnName("DATPRIJENOS");
    trhDATPRIJENOS.setSqlType(93);
    trhDATPRIJENOS.setWidth(10);
    trhDATPOTVRDE.setCaption("Datum potvrde");
    trhDATPOTVRDE.setColumnName("DATPOTVRDE");
    trhDATPOTVRDE.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    trhDATPOTVRDE.setDisplayMask("dd-MM-yyyy");
//    trhDATPOTVRDE.setEditMask("dd-MM-yyyy");
    trhDATPOTVRDE.setTableName("TRANSHISTORY");
    trhDATPOTVRDE.setServerColumnName("DATPOTVRDE");
    trhDATPOTVRDE.setSqlType(93);
    trhDATPOTVRDE.setWidth(10);
    trhKOMENTAR.setCaption("Komentar");
    trhKOMENTAR.setColumnName("KOMENTAR");
    trhKOMENTAR.setDataType(com.borland.dx.dataset.Variant.STRING);
    trhKOMENTAR.setPrecision(120);
    trhKOMENTAR.setTableName("TRANSHISTORY");
    trhKOMENTAR.setServerColumnName("KOMENTAR");
    trhKOMENTAR.setSqlType(1);
    trhKOMENTAR.setWidth(30);
    trh.setResolver(dm.getQresolver());
    trh.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from TransHistory", null, true, Load.ALL));
    setColumns(new Column[] {trhFILENAMECLIJENT, trhFILENAMESERVER, trhSTATUS, trhDATPRIJENOS, trhDATPOTVRDE, trhKOMENTAR});
  }

  public void setall() {

    ddl.create("TransHistory")
       .addChar("filenameclient", 100, true)
       .addChar("filenameserver", 100)
       .addChar("status", 1, "N")
       .addDate("datprijenos")
       .addDate("datpotvrde")
       .addChar("komentar", 120)
       .addPrimaryKey("filenameclient");


    Naziv = "TransHistory";

    SqlDefTabela = ddl.getCreateTableString();
    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
