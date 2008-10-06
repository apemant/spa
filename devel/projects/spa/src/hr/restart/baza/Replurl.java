/****license*****************************************************************
**   file: Replurl.java
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



public class Replurl extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Replurl Replurlclass;

  QueryDataSet rul = new QueryDataSet();

  Column rulRBR_URL = new Column();
  Column rulURL = new Column();
  Column rulDRIVER = new Column();
  Column rulUSR = new Column();
  Column rulPASS = new Column();

  public static Replurl getDataModule() {
    if (Replurlclass == null) {
      Replurlclass = new Replurl();
    }
    return Replurlclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rul;
  }

  public Replurl() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    rulRBR_URL.setCaption("Index");
    rulRBR_URL.setColumnName("RBR_URL");
    rulRBR_URL.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rulRBR_URL.setPrecision(2);
    rulRBR_URL.setRowId(true);
    rulRBR_URL.setTableName("REPLURL");
    rulRBR_URL.setServerColumnName("RBR_URL");
    rulRBR_URL.setSqlType(5);
    rulRBR_URL.setWidth(2);
    rulURL.setCaption("URL");
    rulURL.setColumnName("URL");
    rulURL.setDataType(com.borland.dx.dataset.Variant.STRING);
    rulURL.setPrecision(100);
    rulURL.setTableName("REPLURL");
    rulURL.setServerColumnName("URL");
    rulURL.setSqlType(1);
    rulURL.setWidth(30);
    rulDRIVER.setCaption("Driver");
    rulDRIVER.setColumnName("DRIVER");
    rulDRIVER.setDataType(com.borland.dx.dataset.Variant.STRING);
    rulDRIVER.setPrecision(50);
    rulDRIVER.setTableName("REPLURL");
    rulDRIVER.setServerColumnName("DRIVER");
    rulDRIVER.setSqlType(1);
    rulDRIVER.setWidth(30);
    rulUSR.setCaption("User");
    rulUSR.setColumnName("USR");
    rulUSR.setDataType(com.borland.dx.dataset.Variant.STRING);
    rulUSR.setPrecision(20);
    rulUSR.setTableName("REPLURL");
    rulUSR.setServerColumnName("USR");
    rulUSR.setSqlType(1);
    rulPASS.setCaption("Password");
    rulPASS.setColumnName("PASS");
    rulPASS.setDataType(com.borland.dx.dataset.Variant.STRING);
    rulPASS.setPrecision(20);
    rulPASS.setTableName("REPLURL");
    rulPASS.setServerColumnName("PASS");
    rulPASS.setSqlType(1);
    rul.setResolver(dm.getQresolver());
    rul.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Replurl", null, true, Load.ALL));
 setColumns(new Column[] {rulRBR_URL, rulURL, rulDRIVER, rulUSR, rulPASS});
  }

  public void setall() {

    ddl.create("Replurl")
       .addShort("rbr_url", 2, true)
       .addChar("url", 100)
       .addChar("driver", 50)
       .addChar("usr", 20)
       .addChar("pass", 20)
       .addPrimaryKey("rbr_url");


    Naziv = "Replurl";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
