/****license*****************************************************************
**   file: Cartrosotp.java
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



public class Cartrosotp extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Cartrosotp Cartrosotpclass;

  QueryDataSet cartrosotp = new QueryDataSet();

  Column cartrosotpLOCK = new Column();
  Column cartrosotpAKTIV = new Column();
  Column cartrosotpCTROSOTP = new Column();
  Column cartrosotpNAZIV = new Column();

  public static Cartrosotp getDataModule() {
    if (Cartrosotpclass == null) {
      Cartrosotpclass = new Cartrosotp();
    }
    return Cartrosotpclass;
  }

  public QueryDataSet getQueryDataSet() {
    return cartrosotp;
  }

  public Cartrosotp() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    cartrosotpLOCK.setCaption("Status zauzetosti");
    cartrosotpLOCK.setColumnName("LOCK");
    cartrosotpLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartrosotpLOCK.setPrecision(1);
    cartrosotpLOCK.setTableName("CARTROSOTP");
    cartrosotpLOCK.setServerColumnName("LOCK");
    cartrosotpLOCK.setSqlType(1);
    cartrosotpLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cartrosotpLOCK.setDefault("N");
    cartrosotpAKTIV.setCaption("Aktivan - neaktivan");
    cartrosotpAKTIV.setColumnName("AKTIV");
    cartrosotpAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartrosotpAKTIV.setPrecision(1);
    cartrosotpAKTIV.setTableName("CARTROSOTP");
    cartrosotpAKTIV.setServerColumnName("AKTIV");
    cartrosotpAKTIV.setSqlType(1);
    cartrosotpAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    cartrosotpAKTIV.setDefault("D");
    cartrosotpCTROSOTP.setCaption("Šifra troška otpreme");
    cartrosotpCTROSOTP.setColumnName("CTROSOTP");
    cartrosotpCTROSOTP.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartrosotpCTROSOTP.setPrecision(10);
    cartrosotpCTROSOTP.setRowId(true);
    cartrosotpCTROSOTP.setTableName("CARTROSOTP");
    cartrosotpCTROSOTP.setServerColumnName("CTROSOTP");
    cartrosotpCTROSOTP.setSqlType(1);
    cartrosotpNAZIV.setCaption("Naziv");
    cartrosotpNAZIV.setColumnName("NAZIV");
    cartrosotpNAZIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    cartrosotpNAZIV.setPrecision(80);
    cartrosotpNAZIV.setTableName("CARTROSOTP");
    cartrosotpNAZIV.setServerColumnName("NAZIV");
    cartrosotpNAZIV.setSqlType(1);
    cartrosotpNAZIV.setWidth(30);
    cartrosotp.setResolver(dm.getQresolver());
    cartrosotp.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Cartrosotp", null, true, Load.ALL));
    setColumns(new Column[] {cartrosotpLOCK, cartrosotpAKTIV, cartrosotpCTROSOTP, cartrosotpNAZIV});
  }

  public void setall() {

    ddl.create("Cartrosotp")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("ctrosotp", 10, true)
       .addChar("naziv", 80)
       .addPrimaryKey("ctrosotp");


    Naziv = "Cartrosotp";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"ctrosotp"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
