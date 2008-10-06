/****license*****************************************************************
**   file: Vrstesif.java
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



public class Vrstesif extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Vrstesif Vrstesifclass;

  QueryDataSet vrs = new raDataSet();

  Column vrsLOKK = new Column();
  Column vrsAKTIV = new Column();
  Column vrsVRSTASIF = new Column();
  Column vrsOPISVRSIF = new Column();

  public static Vrstesif getDataModule() {
    if (Vrstesifclass == null) {
      Vrstesifclass = new Vrstesif();
    }
    return Vrstesifclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vrs;
  }

  public Vrstesif() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vrsLOKK.setCaption("Status zauzetosti");
    vrsLOKK.setColumnName("LOKK");
    vrsLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsLOKK.setPrecision(1);
    vrsLOKK.setTableName("VRSTESIF");
    vrsLOKK.setServerColumnName("LOKK");
    vrsLOKK.setSqlType(1);
    vrsLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrsLOKK.setDefault("N");
    vrsAKTIV.setCaption("Aktivan - neaktivan");
    vrsAKTIV.setColumnName("AKTIV");
    vrsAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsAKTIV.setPrecision(1);
    vrsAKTIV.setTableName("VRSTESIF");
    vrsAKTIV.setServerColumnName("AKTIV");
    vrsAKTIV.setSqlType(1);
    vrsAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    vrsAKTIV.setDefault("D");
    vrsVRSTASIF.setCaption("Vrsta");
    vrsVRSTASIF.setColumnName("VRSTASIF");
    vrsVRSTASIF.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsVRSTASIF.setPrecision(4);
    vrsVRSTASIF.setRowId(true);
    vrsVRSTASIF.setTableName("VRSTESIF");
    vrsVRSTASIF.setServerColumnName("VRSTASIF");
    vrsVRSTASIF.setSqlType(1);
    vrsOPISVRSIF.setCaption("Opis");
    vrsOPISVRSIF.setColumnName("OPISVRSIF");
    vrsOPISVRSIF.setDataType(com.borland.dx.dataset.Variant.STRING);
    vrsOPISVRSIF.setPrecision(30);
    vrsOPISVRSIF.setTableName("VRSTESIF");
    vrsOPISVRSIF.setServerColumnName("OPISVRSIF");
    vrsOPISVRSIF.setSqlType(1);
    vrs.setResolver(dm.getQresolver());
    vrs.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Vrstesif", null, true, Load.ALL));
 setColumns(new Column[] {vrsLOKK, vrsAKTIV, vrsVRSTASIF, vrsOPISVRSIF});
  }

  public void setall() {

    ddl.create("Vrstesif")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("vrstasif", 4, true)
       .addChar("opisvrsif", 30)
       .addPrimaryKey("vrstasif");


    Naziv = "Vrstesif";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
