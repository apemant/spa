/****license*****************************************************************
**   file: OS_Log.java
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



public class OS_Log extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Log OS_Logclass;

  QueryDataSet oslog = new QueryDataSet();

  Column oslogLOKK = new Column();
  Column oslogAKTIV = new Column();
  Column oslogCORG = new Column();
  Column oslogRBR = new Column();
  Column oslogDATOD = new Column();
  Column oslogDATDO = new Column();
  Column oslogSTATKNJ = new Column();
  Column oslogCNALOGA = new Column();

  public static OS_Log getDataModule() {
    if (OS_Logclass == null) {
      OS_Logclass = new OS_Log();
    }
    return OS_Logclass;
  }

  public QueryDataSet getQueryDataSet() {
    return oslog;
  }

  public OS_Log() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    oslogLOKK.setCaption("Status zauzetosti");
    oslogLOKK.setColumnName("LOKK");
    oslogLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    oslogLOKK.setPrecision(1);
    oslogLOKK.setTableName("OS_LOG");
    oslogLOKK.setServerColumnName("LOKK");
    oslogLOKK.setSqlType(1);
    oslogLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    oslogLOKK.setDefault("N");
    oslogAKTIV.setCaption("Aktivan - neaktivan");
    oslogAKTIV.setColumnName("AKTIV");
    oslogAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    oslogAKTIV.setPrecision(1);
    oslogAKTIV.setTableName("OS_LOG");
    oslogAKTIV.setServerColumnName("AKTIV");
    oslogAKTIV.setSqlType(1);
    oslogAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    oslogAKTIV.setDefault("D");
    oslogCORG.setCaption("OJ");
    oslogCORG.setColumnName("CORG");
    oslogCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    oslogCORG.setPrecision(12);
    oslogCORG.setRowId(true);
    oslogCORG.setTableName("OS_LOG");
    oslogCORG.setServerColumnName("CORG");
    oslogCORG.setSqlType(1);
    oslogRBR.setCaption("Rbr");
    oslogRBR.setColumnName("RBR");
    oslogRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    oslogRBR.setPrecision(6);
    oslogRBR.setRowId(true);
    oslogRBR.setTableName("OS_LOG");
    oslogRBR.setServerColumnName("RBR");
    oslogRBR.setSqlType(4);
    oslogRBR.setWidth(6);
    oslogDATOD.setCaption("Datum od");
    oslogDATOD.setColumnName("DATOD");
    oslogDATOD.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    oslogDATOD.setPrecision(8);
    oslogDATOD.setDisplayMask("dd-MM-yyyy");
//    oslogDATOD.setEditMask("dd-MM-yyyy");
    oslogDATOD.setTableName("OS_LOG");
    oslogDATOD.setServerColumnName("DATOD");
    oslogDATOD.setSqlType(93);
    oslogDATOD.setWidth(10);
    oslogDATDO.setCaption("Datum do");
    oslogDATDO.setColumnName("DATDO");
    oslogDATDO.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    oslogDATDO.setPrecision(8);
    oslogDATDO.setDisplayMask("dd-MM-yyyy");
//    oslogDATDO.setEditMask("dd-MM-yyyy");
    oslogDATDO.setTableName("OS_LOG");
    oslogDATDO.setServerColumnName("DATDO");
    oslogDATDO.setSqlType(93);
    oslogDATDO.setWidth(10);
    oslogSTATKNJ.setCaption("Knjiženo");
    oslogSTATKNJ.setColumnName("STATKNJ");
    oslogSTATKNJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    oslogSTATKNJ.setPrecision(1);
    oslogSTATKNJ.setTableName("OS_LOG");
    oslogSTATKNJ.setServerColumnName("STATKNJ");
    oslogSTATKNJ.setSqlType(1);
    oslogSTATKNJ.setDefault("N");
    oslogCNALOGA.setCaption("Broj naloga");
    oslogCNALOGA.setColumnName("CNALOGA");
    oslogCNALOGA.setDataType(com.borland.dx.dataset.Variant.STRING);
    oslogCNALOGA.setPrecision(30);
    oslogCNALOGA.setTableName("OS_LOG");
    oslogCNALOGA.setServerColumnName("CNALOGA");
    oslogCNALOGA.setSqlType(1);
    oslogCNALOGA.setWidth(30);
    oslog.setResolver(dm.getQresolver());
    oslog.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Log", null, true, Load.ALL));
 setColumns(new Column[] {oslogLOKK, oslogAKTIV, oslogCORG, oslogRBR, oslogDATOD, oslogDATDO, oslogSTATKNJ, oslogCNALOGA});
  }

  public void setall() {

    ddl.create("OS_Log")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addInteger("rbr", 6, true)
       .addDate("datod")
       .addDate("datdo")
       .addChar("statknj", 1, "N")
       .addChar("cnaloga", 30)
       .addPrimaryKey("corg,rbr");


    Naziv = "OS_Log";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"rbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
