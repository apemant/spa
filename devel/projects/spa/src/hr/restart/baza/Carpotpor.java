/****license*****************************************************************
**   file: Carpotpor.java
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



public class Carpotpor extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Carpotpor Carpotporclass;

  QueryDataSet carpotpor = new QueryDataSet();

  Column carpotporLOCK = new Column();
  Column carpotporAKTIV = new Column();
  Column carpotporCPOTPOR = new Column();
  Column carpotporNAZIV = new Column();

  public static Carpotpor getDataModule() {
    if (Carpotporclass == null) {
      Carpotporclass = new Carpotpor();
    }
    return Carpotporclass;
  }

  public QueryDataSet getQueryDataSet() {
    return carpotpor;
  }

  public Carpotpor() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    carpotporLOCK.setCaption("Status zauzetosti");
    carpotporLOCK.setColumnName("LOCK");
    carpotporLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpotporLOCK.setPrecision(1);
    carpotporLOCK.setTableName("CARPOTPOR");
    carpotporLOCK.setServerColumnName("LOCK");
    carpotporLOCK.setSqlType(1);
    carpotporLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carpotporLOCK.setDefault("N");
    carpotporAKTIV.setCaption("Aktivan - neaktivan");
    carpotporAKTIV.setColumnName("AKTIV");
    carpotporAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpotporAKTIV.setPrecision(1);
    carpotporAKTIV.setTableName("CARPOTPOR");
    carpotporAKTIV.setServerColumnName("AKTIV");
    carpotporAKTIV.setSqlType(1);
    carpotporAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    carpotporAKTIV.setDefault("D");
    carpotporCPOTPOR.setCaption("Šifra potvrde o porijeklu");
    carpotporCPOTPOR.setColumnName("CPOTPOR");
    carpotporCPOTPOR.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpotporCPOTPOR.setPrecision(10);
    carpotporCPOTPOR.setRowId(true);
    carpotporCPOTPOR.setTableName("CARPOTPOR");
    carpotporCPOTPOR.setServerColumnName("CPOTPOR");
    carpotporCPOTPOR.setSqlType(1);
    carpotporNAZIV.setCaption("Naziv");
    carpotporNAZIV.setColumnName("NAZIV");
    carpotporNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    carpotporNAZIV.setPrecision(120);
    carpotporNAZIV.setTableName("CARPOTPOR");
    carpotporNAZIV.setServerColumnName("NAZIV");
    carpotporNAZIV.setSqlType(1);
    carpotporNAZIV.setWidth(30);
    carpotpor.setResolver(dm.getQresolver());
    carpotpor.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Carpotpor", null, true, Load.ALL));
    setColumns(new Column[] {carpotporLOCK, carpotporAKTIV, carpotporCPOTPOR, carpotporNAZIV});
  }

  public void setall() {

    ddl.create("Carpotpor")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("cpotpor", 10, true)
       .addChar("naziv", 120)
       .addPrimaryKey("cpotpor");


    Naziv = "Carpotpor";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cpotpor"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
