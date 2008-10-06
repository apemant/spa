/****license*****************************************************************
**   file: KamUpl.java
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



public class KamUpl extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static KamUpl KamUplclass;

  QueryDataSet kul = new raDataSet();

  Column kulKNJIG = new Column();
  Column kulCPAR = new Column();
  Column kulBROJDOK = new Column();
  Column kulRBR = new Column();
  Column kulDATDOK = new Column();
  Column kulIZNOS = new Column();
  Column kulCRACUNA = new Column();

  public static KamUpl getDataModule() {
    if (KamUplclass == null) {
      KamUplclass = new KamUpl();
    }
    return KamUplclass;
  }

  public QueryDataSet getQueryDataSet() {
    return kul;
  }

  public KamUpl() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    kulKNJIG.setCaption("Knjigovodstvo");
    kulKNJIG.setColumnName("KNJIG");
    kulKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    kulKNJIG.setPrecision(12);
    kulKNJIG.setRowId(true);
    kulKNJIG.setTableName("KAMUPL");
    kulKNJIG.setServerColumnName("KNJIG");
    kulKNJIG.setSqlType(1);
    kulCPAR.setCaption("Partner");
    kulCPAR.setColumnName("CPAR");
    kulCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    kulCPAR.setPrecision(6);
    kulCPAR.setRowId(true);
    kulCPAR.setTableName("KAMUPL");
    kulCPAR.setServerColumnName("CPAR");
    kulCPAR.setSqlType(4);
    kulCPAR.setWidth(6);
    kulBROJDOK.setCaption("Broj uplate");
    kulBROJDOK.setColumnName("BROJDOK");
    kulBROJDOK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kulBROJDOK.setPrecision(50);
    kulBROJDOK.setRowId(true);
    kulBROJDOK.setTableName("KAMUPL");
    kulBROJDOK.setServerColumnName("BROJDOK");
    kulBROJDOK.setSqlType(1);
    kulBROJDOK.setWidth(30);
    kulRBR.setCaption("Rbr");
    kulRBR.setColumnName("RBR");
    kulRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    kulRBR.setPrecision(6);
    kulRBR.setRowId(true);
    kulRBR.setTableName("KAMUPL");
    kulRBR.setServerColumnName("RBR");
    kulRBR.setSqlType(4);
    kulRBR.setWidth(6);
    kulDATDOK.setCaption("Datum dokumenta");
    kulDATDOK.setColumnName("DATDOK");
    kulDATDOK.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    kulDATDOK.setPrecision(19);
    kulDATDOK.setDisplayMask("dd-MM-yyyy");
//    kulDATDOK.setEditMask("dd-MM-yyyy");
    kulDATDOK.setTableName("KAMUPL");
    kulDATDOK.setServerColumnName("DATDOK");
    kulDATDOK.setSqlType(93);
    kulDATDOK.setWidth(10);
    kulIZNOS.setCaption("Iznos uplate");
    kulIZNOS.setColumnName("IZNOS");
    kulIZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    kulIZNOS.setPrecision(17);
    kulIZNOS.setScale(2);
    kulIZNOS.setDisplayMask("###,###,##0.00");
    kulIZNOS.setDefault("0");
    kulIZNOS.setTableName("KAMUPL");
    kulIZNOS.setServerColumnName("IZNOS");
    kulIZNOS.setSqlType(2);
    kulCRACUNA.setCaption("Klju\u010D pokrivenog ra\u010Duna");
    kulCRACUNA.setColumnName("CRACUNA");
    kulCRACUNA.setDataType(com.borland.dx.dataset.Variant.STRING);
    kulCRACUNA.setPrecision(80);
    kulCRACUNA.setTableName("KAMUPL");
    kulCRACUNA.setServerColumnName("CRACUNA");
    kulCRACUNA.setSqlType(1);
    kulCRACUNA.setWidth(30);
    kul.setResolver(dm.getQresolver());
    kul.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from KamUpl", null, true, Load.ALL));
 setColumns(new Column[] {kulKNJIG, kulCPAR, kulBROJDOK, kulRBR, kulDATDOK, kulIZNOS, kulCRACUNA});
  }

  public void setall() {

    ddl.create("KamUpl")
       .addChar("knjig", 12, true)
       .addInteger("cpar", 6, true)
       .addChar("brojdok", 50, true)
       .addInteger("rbr", 6, true)
       .addDate("datdok")
       .addFloat("iznos", 17, 2)
       .addChar("cracuna", 80)
       .addPrimaryKey("knjig,cpar,brojdok,rbr");


    Naziv = "KamUpl";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"brojdok", "rbr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
