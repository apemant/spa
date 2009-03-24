/****license*****************************************************************
**   file: Kolone.java
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
package hr.restart.sisfun;
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;


public class Kolone {

  private static Kolone Koloneclass;

  StorageDataSet kolone = new StorageDataSet();

  Column koloneIMEKOL = new Column();
  Column koloneOPIS = new Column();
  Column koloneTIP = new Column();
//  Column koloneDULJINA = new Column();
  Column koloneSIRINA = new Column();
  Column koloneSQLTIP = new Column();
  Column koloneTABLICA = new Column();

  public static Kolone getDataModule() {
    if (Koloneclass == null) {
      Koloneclass = new Kolone();
    }
    return Koloneclass;
  }

  public StorageDataSet getDataSet() {
    return kolone;
  }

  public Kolone() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    koloneIMEKOL.setCaption("Ime");
    koloneIMEKOL.setColumnName("IMEKOL");
    koloneIMEKOL.setDataType(com.borland.dx.dataset.Variant.STRING);
    koloneIMEKOL.setPrecision(32);
    koloneIMEKOL.setRowId(true);
    koloneIMEKOL.setTableName("KOLONE");
    koloneIMEKOL.setServerColumnName("IMEKOL");
    koloneIMEKOL.setSqlType(1);
    koloneIMEKOL.setWidth(12);
    koloneOPIS.setCaption("Opis");
    koloneOPIS.setColumnName("OPIS");
    koloneOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    koloneOPIS.setPrecision(80);
    koloneOPIS.setTableName("KOLONE");
    koloneOPIS.setServerColumnName("OPIS");
    koloneOPIS.setSqlType(1);
    koloneOPIS.setWidth(30);
    koloneTIP.setCaption("Tip");
    koloneTIP.setColumnName("TIP");
    koloneTIP.setDataType(com.borland.dx.dataset.Variant.STRING);
    koloneTIP.setPrecision(20);
    koloneTIP.setTableName("KOLONE");
    koloneTIP.setServerColumnName("TIP");
    koloneTIP.setSqlType(1);
    koloneTIP.setWidth(13);
//    koloneDULJINA.setCaption("Duljina");
//    koloneDULJINA.setColumnName("DULJINA");
//    koloneDULJINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
//    koloneDULJINA.setPrecision(4);
//    koloneDULJINA.setTableName("KOLONE");
//    koloneDULJINA.setServerColumnName("DULJINA");
//    koloneDULJINA.setSqlType(5);
//    koloneDULJINA.setWidth(7);
    koloneSIRINA.setCaption("Širina");
    koloneSIRINA.setColumnName("SIRINA");
    koloneSIRINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    koloneSIRINA.setPrecision(4);
    koloneSIRINA.setTableName("KOLONE");
    koloneSIRINA.setServerColumnName("SIRINA");
    koloneSIRINA.setSqlType(5);
    koloneSIRINA.setWidth(7);
    koloneSQLTIP.setCaption("Sql tip");
    koloneSQLTIP.setColumnName("SQLTIP");
    koloneSQLTIP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    koloneSQLTIP.setPrecision(4);
    koloneSQLTIP.setTableName("KOLONE");
    koloneSQLTIP.setServerColumnName("SQLTIP");
    koloneSQLTIP.setSqlType(5);
    koloneSQLTIP.setWidth(7);
    koloneTABLICA.setCaption("Tablica");
    koloneTABLICA.setColumnName("TABLICA");
    koloneTABLICA.setDataType(com.borland.dx.dataset.Variant.STRING);
    koloneTABLICA.setPrecision(32);
    koloneTABLICA.setRowId(true);
    koloneTABLICA.setTableName("KOLONE");
    koloneTABLICA.setServerColumnName("TABLICA");
    koloneTABLICA.setSqlType(1);
    koloneTABLICA.setWidth(10);

    kolone.setColumns(new Column[] {koloneIMEKOL, koloneOPIS, koloneTIP,/* koloneDULJINA,*/ koloneSIRINA, koloneSQLTIP, koloneTABLICA});
  }
}


