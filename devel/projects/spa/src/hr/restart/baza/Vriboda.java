/****license*****************************************************************
**   file: Vriboda.java
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



public class Vriboda extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Vriboda Vribodaclass;

  QueryDataSet vriboda = new raDataSet();

  Column vribodaKNJIG = new Column();
  Column vribodaGODINA = new Column();
  Column vribodaMJOBR = new Column();
  Column vribodaBOD = new Column();

  public static Vriboda getDataModule() {
    if (Vribodaclass == null) {
      Vribodaclass = new Vriboda();
    }
    return Vribodaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return vriboda;
  }

  public Vriboda() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    vribodaKNJIG.setCaption("Poduze\u0107e");
    vribodaKNJIG.setColumnName("KNJIG");
    vribodaKNJIG.setDataType(com.borland.dx.dataset.Variant.STRING);
    vribodaKNJIG.setPrecision(12);
    vribodaKNJIG.setRowId(true);
    vribodaKNJIG.setTableName("VRIBODA");
    vribodaKNJIG.setServerColumnName("KNJIG");
    vribodaKNJIG.setSqlType(1);
    vribodaGODINA.setCaption("Godina");
    vribodaGODINA.setColumnName("GODINA");
    vribodaGODINA.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vribodaGODINA.setPrecision(4);
    vribodaGODINA.setRowId(true);
    vribodaGODINA.setTableName("VRIBODA");
    vribodaGODINA.setServerColumnName("GODINA");
    vribodaGODINA.setSqlType(5);
    vribodaGODINA.setWidth(4);
    vribodaMJOBR.setCaption("Mjesec");
    vribodaMJOBR.setColumnName("MJOBR");
    vribodaMJOBR.setDataType(com.borland.dx.dataset.Variant.SHORT);
    vribodaMJOBR.setPrecision(2);
    vribodaMJOBR.setRowId(true);
    vribodaMJOBR.setTableName("VRIBODA");
    vribodaMJOBR.setServerColumnName("MJOBR");
    vribodaMJOBR.setSqlType(5);
    vribodaMJOBR.setWidth(2);
    vribodaBOD.setCaption("Vrijednost boda");
    vribodaBOD.setColumnName("BOD");
    vribodaBOD.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    vribodaBOD.setPrecision(17);
    vribodaBOD.setScale(6);
    vribodaBOD.setDisplayMask("###,###,##0.000000");
    vribodaBOD.setDefault("0");
    vribodaBOD.setTableName("VRIBODA");
    vribodaBOD.setServerColumnName("BOD");
    vribodaBOD.setSqlType(2);
    vribodaBOD.setDefault("1");
    vriboda.setResolver(dm.getQresolver());
    vriboda.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Vriboda", null, true, Load.ALL));
 setColumns(new Column[] {vribodaKNJIG, vribodaGODINA, vribodaMJOBR, vribodaBOD});
  }

  public void setall() {

    ddl.create("Vriboda")
       .addChar("knjig", 12, true)
       .addShort("godina", 4, true)
       .addShort("mjobr", 2, true)
       .addFloat("bod", 17, 6)
       .addPrimaryKey("knjig,godina,mjobr");


    Naziv = "Vriboda";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"mjobr"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
