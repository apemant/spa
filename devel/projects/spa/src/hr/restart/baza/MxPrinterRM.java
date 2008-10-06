/****license*****************************************************************
**   file: MxPrinterRM.java
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



public class MxPrinterRM extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static MxPrinterRM MxPrinterRMclass;

  QueryDataSet mxprm = new raDataSet();

  Column mxprmCRM = new Column();
  Column mxprmOPIS = new Column();
  Column mxprmCPRINTER = new Column();
  Column mxprmPORT = new Column();
  Column mxprmLOCALCOM = new Column();
  Column mxprmREMOTECOM = new Column();

  public static MxPrinterRM getDataModule() {
    if (MxPrinterRMclass == null) {
      MxPrinterRMclass = new MxPrinterRM();
    }
    return MxPrinterRMclass;
  }

  public QueryDataSet getQueryDataSet() {
    return mxprm;
  }

  public MxPrinterRM() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    mxprmCRM.setCaption("Šifra");
    mxprmCRM.setColumnName("CRM");
    mxprmCRM.setDataType(com.borland.dx.dataset.Variant.INT);
    mxprmCRM.setPrecision(6);
    mxprmCRM.setRowId(true);
    mxprmCRM.setTableName("MXPRINTERRM");
    mxprmCRM.setServerColumnName("CRM");
    mxprmCRM.setSqlType(4);
    mxprmCRM.setWidth(6);
    mxprmOPIS.setCaption("Opis");
    mxprmOPIS.setColumnName("OPIS");
    mxprmOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprmOPIS.setPrecision(50);
    mxprmOPIS.setTableName("MXPRINTERRM");
    mxprmOPIS.setServerColumnName("OPIS");
    mxprmOPIS.setSqlType(1);
    mxprmOPIS.setWidth(30);
    mxprmCPRINTER.setCaption("Printer");
    mxprmCPRINTER.setColumnName("CPRINTER");
    mxprmCPRINTER.setDataType(com.borland.dx.dataset.Variant.INT);
    mxprmCPRINTER.setPrecision(6);
    mxprmCPRINTER.setTableName("MXPRINTERRM");
    mxprmCPRINTER.setServerColumnName("CPRINTER");
    mxprmCPRINTER.setSqlType(4);
    mxprmCPRINTER.setWidth(6);
    mxprmPORT.setCaption("Port");
    mxprmPORT.setColumnName("PORT");
    mxprmPORT.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprmPORT.setPrecision(100);
    mxprmPORT.setTableName("MXPRINTERRM");
    mxprmPORT.setServerColumnName("PORT");
    mxprmPORT.setSqlType(1);
    mxprmPORT.setWidth(30);
    mxprmLOCALCOM.setCaption("Naredba ispisa (local)");
    mxprmLOCALCOM.setColumnName("LOCALCOM");
    mxprmLOCALCOM.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprmLOCALCOM.setPrecision(100);
    mxprmLOCALCOM.setTableName("MXPRINTERRM");
    mxprmLOCALCOM.setServerColumnName("LOCALCOM");
    mxprmLOCALCOM.setSqlType(1);
    mxprmLOCALCOM.setWidth(30);
    mxprmREMOTECOM.setCaption("Naredba ispisa (remote)");
    mxprmREMOTECOM.setColumnName("REMOTECOM");
    mxprmREMOTECOM.setDataType(com.borland.dx.dataset.Variant.STRING);
    mxprmREMOTECOM.setPrecision(100);
    mxprmREMOTECOM.setTableName("MXPRINTERRM");
    mxprmREMOTECOM.setServerColumnName("REMOTECOM");
    mxprmREMOTECOM.setSqlType(1);
    mxprmREMOTECOM.setWidth(30);
    mxprm.setResolver(dm.getQresolver());
    mxprm.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from MxPrinterRM", null, true, Load.ALL));
 setColumns(new Column[] {mxprmCRM, mxprmOPIS, mxprmCPRINTER, mxprmPORT, mxprmLOCALCOM, mxprmREMOTECOM});
  }

  public void setall() {

    ddl.create("MxPrinterRM")
       .addInteger("crm", 6, true)
       .addChar("opis", 50)
       .addInteger("cprinter", 6)
       .addChar("port", 100)
       .addChar("localcom", 100)
       .addChar("remotecom", 100)
       .addPrimaryKey("crm");


    Naziv = "MxPrinterRM";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
