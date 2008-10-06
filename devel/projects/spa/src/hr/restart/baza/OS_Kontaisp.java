/****license*****************************************************************
**   file: OS_Kontaisp.java
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



public class OS_Kontaisp extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static OS_Kontaisp OS_Kontaispclass;

  QueryDataSet oski = new raDataSet();

  Column oskiLOKK = new Column();
  Column oskiAKTIV = new Column();
  Column oskiCORG = new Column();
  Column oskiBROJKONTA = new Column();
  Column oskiKONTOISP = new Column();
  Column oskiKONTODOB = new Column();
  Column oskiKONTOPOR = new Column();
  Column oskiKONTOAMOR = new Column();
  Column oskiKONTOSADVR = new Column();

  public static OS_Kontaisp getDataModule() {
    if (OS_Kontaispclass == null) {
      OS_Kontaispclass = new OS_Kontaisp();
    }
    return OS_Kontaispclass;
  }

  public QueryDataSet getQueryDataSet() {
    return oski;
  }

  public OS_Kontaisp() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    oskiLOKK.setCaption("Status zauzetosti");
    oskiLOKK.setColumnName("LOKK");
    oskiLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskiLOKK.setPrecision(1);
    oskiLOKK.setTableName("OS_KONTAISP");
    oskiLOKK.setServerColumnName("LOKK");
    oskiLOKK.setSqlType(1);
    oskiLOKK.setDefault("N");
    oskiAKTIV.setCaption("Aktivan - neaktivan");
    oskiAKTIV.setColumnName("AKTIV");
    oskiAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskiAKTIV.setPrecision(1);
    oskiAKTIV.setTableName("OS_KONTAISP");
    oskiAKTIV.setServerColumnName("AKTIV");
    oskiAKTIV.setSqlType(1);
    oskiAKTIV.setDefault("D");
    oskiCORG.setCaption("OJ");
    oskiCORG.setColumnName("CORG");
    oskiCORG.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskiCORG.setPrecision(12);
    oskiCORG.setRowId(true);
    oskiCORG.setTableName("OS_KONTAISP");
    oskiCORG.setServerColumnName("CORG");
    oskiCORG.setSqlType(1);
    oskiBROJKONTA.setCaption("Konto osnovice");
    oskiBROJKONTA.setColumnName("BROJKONTA");
    oskiBROJKONTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskiBROJKONTA.setPrecision(8);
    oskiBROJKONTA.setRowId(true);
    oskiBROJKONTA.setTableName("OS_KONTAISP");
    oskiBROJKONTA.setServerColumnName("BROJKONTA");
    oskiBROJKONTA.setSqlType(1);
    oskiKONTOISP.setCaption("Konto ispravka");
    oskiKONTOISP.setColumnName("KONTOISP");
    oskiKONTOISP.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskiKONTOISP.setPrecision(8);
    oskiKONTOISP.setTableName("OS_KONTAISP");
    oskiKONTOISP.setServerColumnName("KONTOISP");
    oskiKONTOISP.setSqlType(1);
    oskiKONTODOB.setCaption("Konto dobavlja\u010Da");
    oskiKONTODOB.setColumnName("KONTODOB");
    oskiKONTODOB.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskiKONTODOB.setPrecision(8);
    oskiKONTODOB.setTableName("OS_KONTAISP");
    oskiKONTODOB.setServerColumnName("KONTODOB");
    oskiKONTODOB.setSqlType(1);
    oskiKONTOPOR.setCaption("Konto poreza");
    oskiKONTOPOR.setColumnName("KONTOPOR");
    oskiKONTOPOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskiKONTOPOR.setPrecision(8);
    oskiKONTOPOR.setTableName("OS_KONTAISP");
    oskiKONTOPOR.setServerColumnName("KONTOPOR");
    oskiKONTOPOR.setSqlType(1);
    oskiKONTOAMOR.setCaption("Konto amortizacije");
    oskiKONTOAMOR.setColumnName("KONTOAMOR");
    oskiKONTOAMOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskiKONTOAMOR.setPrecision(8);
    oskiKONTOAMOR.setTableName("OS_KONTAISP");
    oskiKONTOAMOR.setServerColumnName("KONTOAMOR");
    oskiKONTOAMOR.setSqlType(1);
    oskiKONTOSADVR.setCaption("Konto sadašnje vr.");
    oskiKONTOSADVR.setColumnName("KONTOSADVR");
    oskiKONTOSADVR.setDataType(com.borland.dx.dataset.Variant.STRING);
    oskiKONTOSADVR.setPrecision(8);
    oskiKONTOSADVR.setTableName("OS_KONTAISP");
    oskiKONTOSADVR.setServerColumnName("KONTOSADVR");
    oskiKONTOSADVR.setSqlType(1);
    oski.setResolver(dm.getQresolver());
    oski.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from OS_Kontaisp", null, true, Load.ALL));
 setColumns(new Column[] {oskiLOKK, oskiAKTIV, oskiCORG, oskiBROJKONTA, oskiKONTOISP, oskiKONTODOB, oskiKONTOPOR, oskiKONTOAMOR, oskiKONTOSADVR});
  }

  public void setall() {

    ddl.create("OS_Kontaisp")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("corg", 12, true)
       .addChar("brojkonta", 8, true)
       .addChar("kontoisp", 8)
       .addChar("kontodob", 8)
       .addChar("kontopor", 8)
       .addChar("kontoamor", 8)
       .addChar("kontosadvr", 8)
       .addPrimaryKey("corg,brojkonta");


    Naziv = "OS_Kontaisp";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"brojkonta"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
