/****license*****************************************************************
**   file: Replinfo.java
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



public class Replinfo extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Replinfo Replinfoclass;

  QueryDataSet rin = new QueryDataSet();

  Column rinIMETAB = new Column();
  Column rinKEYTAB = new Column();
  Column rinRBR_URL = new Column();
  Column rinREP_FLAG = new Column();
  Column rinDATPROM = new Column();

  public static Replinfo getDataModule() {
    if (Replinfoclass == null) {
      Replinfoclass = new Replinfo();
    }
    return Replinfoclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rin;
  }

  public Replinfo() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    rinIMETAB.setCaption("Naziv tablice");
    rinIMETAB.setColumnName("IMETAB");
    rinIMETAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    rinIMETAB.setPrecision(20);
    rinIMETAB.setRowId(true);
    rinIMETAB.setTableName("REPLINFO");
    rinIMETAB.setServerColumnName("IMETAB");
    rinIMETAB.setSqlType(1);
    rinKEYTAB.setCaption("Klju\u010D tablice");
    rinKEYTAB.setColumnName("KEYTAB");
    rinKEYTAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    rinKEYTAB.setPrecision(50);
    rinKEYTAB.setRowId(true);
    rinKEYTAB.setTableName("REPLINFO");
    rinKEYTAB.setServerColumnName("KEYTAB");
    rinKEYTAB.setSqlType(1);
    rinKEYTAB.setWidth(30);
    rinRBR_URL.setCaption("Index remote URL-a");
    rinRBR_URL.setColumnName("RBR_URL");
    rinRBR_URL.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rinRBR_URL.setPrecision(2);
    rinRBR_URL.setRowId(true);
    rinRBR_URL.setTableName("REPLINFO");
    rinRBR_URL.setServerColumnName("RBR_URL");
    rinRBR_URL.setSqlType(5);
    rinRBR_URL.setWidth(2);
    rinREP_FLAG.setCaption("Status replikacije");
    rinREP_FLAG.setColumnName("REP_FLAG");
    rinREP_FLAG.setDataType(com.borland.dx.dataset.Variant.STRING);
    rinREP_FLAG.setPrecision(1);
    rinREP_FLAG.setTableName("REPLINFO");
    rinREP_FLAG.setServerColumnName("REP_FLAG");
    rinREP_FLAG.setSqlType(1);
    rinDATPROM.setCaption("Datum promjene");
    rinDATPROM.setColumnName("DATPROM");
    rinDATPROM.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    rinDATPROM.setDisplayMask("dd-MM-yyyy");
//    rinDATPROM.setEditMask("dd-MM-yyyy");
    rinDATPROM.setTableName("REPLINFO");
    rinDATPROM.setServerColumnName("DATPROM");
    rinDATPROM.setSqlType(93);
    rinDATPROM.setWidth(10);
    rin.setResolver(dm.getQresolver());
    rin.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Replinfo", null, true, Load.ALL));
 setColumns(new Column[] {rinIMETAB, rinKEYTAB, rinRBR_URL, rinREP_FLAG, rinDATPROM});
  }

  public void setall() {

    ddl.create("Replinfo")
       .addChar("imetab", 20, true)
       .addChar("keytab", 50, true)
       .addShort("rbr_url", 2, true)
       .addChar("rep_flag", 1)
       .addDate("datprom")
       .addPrimaryKey("imetab,keytab,rbr_url");


    Naziv = "Replinfo";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"imetab", "keytab"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
