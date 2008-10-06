/****license*****************************************************************
**   file: Bankepl.java
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



public class Bankepl extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Bankepl Bankeplclass;

  QueryDataSet bank = new raDataSet();

  Column bankLOKK = new Column();
  Column bankAKTIV = new Column();
  Column bankCBANKE = new Column();
  Column bankNAZBANKE = new Column();
  Column bankCPOV = new Column();
  Column bankBRDOM = new Column();
  Column bankBRPOSL = new Column();
  Column bankPARAMETRI = new Column();

  public static Bankepl getDataModule() {
    if (Bankeplclass == null) {
      Bankeplclass = new Bankepl();
    }
    return Bankeplclass;
  }

  public QueryDataSet getQueryDataSet() {
    return bank;
  }

  public Bankepl() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    bankLOKK.setCaption("Status zauzetosti");
    bankLOKK.setColumnName("LOKK");
    bankLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankLOKK.setPrecision(1);
    bankLOKK.setTableName("BANKEPL");
    bankLOKK.setServerColumnName("LOKK");
    bankLOKK.setSqlType(1);
    bankLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    bankLOKK.setDefault("N");
    bankAKTIV.setCaption("Aktivan - neaktivan");
    bankAKTIV.setColumnName("AKTIV");
    bankAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankAKTIV.setPrecision(1);
    bankAKTIV.setTableName("BANKEPL");
    bankAKTIV.setServerColumnName("AKTIV");
    bankAKTIV.setSqlType(1);
    bankAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    bankAKTIV.setDefault("D");
    bankCBANKE.setCaption("Oznaka");
    bankCBANKE.setColumnName("CBANKE");
    bankCBANKE.setDataType(com.borland.dx.dataset.Variant.INT);
    bankCBANKE.setPrecision(6);
    bankCBANKE.setRowId(true);
    bankCBANKE.setTableName("BANKEPL");
    bankCBANKE.setServerColumnName("CBANKE");
    bankCBANKE.setSqlType(4);
    bankCBANKE.setWidth(6);
    bankNAZBANKE.setCaption("Naziv");
    bankNAZBANKE.setColumnName("NAZBANKE");
    bankNAZBANKE.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankNAZBANKE.setPrecision(40);
    bankNAZBANKE.setTableName("BANKEPL");
    bankNAZBANKE.setServerColumnName("NAZBANKE");
    bankNAZBANKE.setSqlType(1);
    bankCPOV.setCaption("Oznaka povjerioca-virmana");
    bankCPOV.setColumnName("CPOV");
    bankCPOV.setDataType(com.borland.dx.dataset.Variant.INT);
    bankCPOV.setPrecision(6);
    bankCPOV.setTableName("BANKEPL");
    bankCPOV.setServerColumnName("CPOV");
    bankCPOV.setSqlType(4);
    bankCPOV.setWidth(6);
    bankBRDOM.setCaption("Broj domicilne banke");
    bankBRDOM.setColumnName("BRDOM");
    bankBRDOM.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankBRDOM.setPrecision(10);
    bankBRDOM.setTableName("BANKEPL");
    bankBRDOM.setServerColumnName("BRDOM");
    bankBRDOM.setSqlType(1);
    bankBRPOSL.setCaption("Broj poslovnice banke");
    bankBRPOSL.setColumnName("BRPOSL");
    bankBRPOSL.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankBRPOSL.setPrecision(10);
    bankBRPOSL.setTableName("BANKEPL");
    bankBRPOSL.setServerColumnName("BRPOSL");
    bankBRPOSL.setSqlType(1);
    bankPARAMETRI.setCaption("Parametri");
    bankPARAMETRI.setColumnName("PARAMETRI");
    bankPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    bankPARAMETRI.setPrecision(20);
    bankPARAMETRI.setTableName("BANKEPL");
    bankPARAMETRI.setServerColumnName("PARAMETRI");
    bankPARAMETRI.setSqlType(1);
    bankPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    bank.setResolver(dm.getQresolver());
    bank.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Bankepl", null, true, Load.ALL));
 setColumns(new Column[] {bankLOKK, bankAKTIV, bankCBANKE, bankNAZBANKE, bankCPOV, bankBRDOM, bankBRPOSL, bankPARAMETRI});
  }

  public void setall() {

    ddl.create("Bankepl")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("cbanke", 6, true)
       .addChar("nazbanke", 40)
       .addInteger("cpov", 6)
       .addChar("brdom", 10)
       .addChar("brposl", 10)
       .addChar("parametri", 20)
       .addPrimaryKey("cbanke");


    Naziv = "Bankepl";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
