/****license*****************************************************************
**   file: RSPeriod.java
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



public class RSPeriod extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static RSPeriod RSPeriodclass;

  QueryDataSet rsp = new QueryDataSet();

  Column rspCRADNIK = new Column();
  Column rspRBR = new Column();
  Column rspRSOO = new Column();
  Column rspODDANA = new Column();
  Column rspDODANA = new Column();
  Column rspCOPCINE = new Column();

  public static RSPeriod getDataModule() {
    if (RSPeriodclass == null) {
      RSPeriodclass = new RSPeriod();
    }
    return RSPeriodclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rsp;
  }

  public RSPeriod() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    rspCRADNIK.setCaption("Radnik");
    rspCRADNIK.setColumnName("CRADNIK");
    rspCRADNIK.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspCRADNIK.setPrecision(6);
    rspCRADNIK.setRowId(true);
    rspCRADNIK.setTableName("RSPERIOD");
    rspCRADNIK.setServerColumnName("CRADNIK");
    rspCRADNIK.setSqlType(1);
    rspRBR.setCaption("Rbr");
    rspRBR.setColumnName("RBR");
    rspRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    rspRBR.setPrecision(6);
    rspRBR.setRowId(true);
    rspRBR.setTableName("RSPERIOD");
    rspRBR.setServerColumnName("RBR");
    rspRBR.setSqlType(4);
    rspRBR.setWidth(6);
    rspRSOO.setCaption("Osnova osiguranja");
    rspRSOO.setColumnName("RSOO");
    rspRSOO.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspRSOO.setPrecision(5);
    rspRSOO.setTableName("RSPERIOD");
    rspRSOO.setServerColumnName("RSOO");
    rspRSOO.setSqlType(1);
    rspODDANA.setCaption("Od");
    rspODDANA.setColumnName("ODDANA");
    rspODDANA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rspODDANA.setPrecision(2);
    rspODDANA.setTableName("RSPERIOD");
    rspODDANA.setServerColumnName("ODDANA");
    rspODDANA.setSqlType(5);
    rspODDANA.setWidth(2);
    rspDODANA.setCaption("Do");
    rspDODANA.setColumnName("DODANA");
    rspDODANA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rspDODANA.setPrecision(2);
    rspDODANA.setTableName("RSPERIOD");
    rspDODANA.setServerColumnName("DODANA");
    rspDODANA.setSqlType(5);
    rspDODANA.setWidth(2);
    
    rspCOPCINE.setCaption("Op\u0107ina");
    rspCOPCINE.setColumnName("COPCINE");
    rspCOPCINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    rspCOPCINE.setPrecision(3);
    rspCOPCINE.setTableName("RSPERIOD");
    rspCOPCINE.setServerColumnName("COPCINE");
    rspCOPCINE.setSqlType(1);

    
    rsp.setResolver(dm.getQresolver());
    rsp.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from RSPeriod", null, true, Load.ALL));
 setColumns(new Column[] {rspCRADNIK, rspRBR, rspRSOO, rspODDANA, rspDODANA, rspCOPCINE});
  }

  public void setall() {

    ddl.create("RSPeriod")
       .addChar("cradnik", 6, true)
       .addInteger("rbr", 6, true)
       .addChar("rsoo", 5)
       .addShort("oddana", 2)
       .addShort("dodana", 2)
       .addChar("copcine", 3)
       .addPrimaryKey("cradnik,rbr");


    Naziv = "RSPeriod";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cradnik", "rbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
