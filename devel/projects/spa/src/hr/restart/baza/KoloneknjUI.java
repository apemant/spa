/****license*****************************************************************
**   file: KoloneknjUI.java
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
import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;



public class KoloneknjUI extends KreirDrop implements DataModule {

//  dM dm  = dM.getDataModule();
  private static KoloneknjUI KoloneknjUIclass;

  QueryDataSet kolknj = new raDataSet();
  QueryDataSet kolknjulazne = new raDataSet();
  QueryDataSet kolknjizlazne = new raDataSet();

/*  Column kolknjLOCK = new Column();
  Column kolknjAKTIV = new Column();
  Column kolknjCKOLONE = new Column();
  Column kolknjNAZIVKOLONE = new Column();
  Column kolknjURAIRA = new Column();
  Column kolknjDUGPOT = new Column(); */

  public static KoloneknjUI getDataModule() {
    if (KoloneknjUIclass == null) {
      KoloneknjUIclass = new KoloneknjUI();
    }
    return KoloneknjUIclass;
  }

  public QueryDataSet getQueryDataSet() {
    return kolknj;
  }

  public QueryDataSet getUlazne() {
    return kolknjulazne;
  }

  public QueryDataSet getIzlazne() {
    return kolknjizlazne;
  }

  public KoloneknjUI() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    /*kolknjLOCK.setCaption("Status zauzetosti");
    kolknjLOCK.setColumnName("LOCK");
    kolknjLOCK.setDataType(com.borland.dx.dataset.Variant.STRING);
    kolknjLOCK.setPrecision(1);
    kolknjLOCK.setTableName("KOLONEKNJUI");
    kolknjLOCK.setServerColumnName("LOCK");
    kolknjLOCK.setSqlType(1);
    kolknjLOCK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kolknjLOCK.setDefault("N");
    kolknjAKTIV.setCaption("Aktivan - neaktivan");
    kolknjAKTIV.setColumnName("AKTIV");
    kolknjAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    kolknjAKTIV.setPrecision(1);
    kolknjAKTIV.setTableName("KOLONEKNJUI");
    kolknjAKTIV.setServerColumnName("AKTIV");
    kolknjAKTIV.setSqlType(1);
    kolknjAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    kolknjAKTIV.setDefault("D");
    kolknjCKOLONE.setCaption("Oznaka");
    kolknjCKOLONE.setColumnName("CKOLONE");
    kolknjCKOLONE.setDataType(com.borland.dx.dataset.Variant.SHORT);
    kolknjCKOLONE.setRowId(true);
    kolknjCKOLONE.setTableName("KOLONEKNJUI");
    kolknjCKOLONE.setServerColumnName("CKOLONE");
    kolknjCKOLONE.setSqlType(5);
    kolknjCKOLONE.setWidth(4);
    kolknjNAZIVKOLONE.setCaption("Naziv");
    kolknjNAZIVKOLONE.setColumnName("NAZIVKOLONE");
    kolknjNAZIVKOLONE.setDataType(com.borland.dx.dataset.Variant.STRING);
    kolknjNAZIVKOLONE.setPrecision(50);
    kolknjNAZIVKOLONE.setTableName("KOLONEKNJUI");
    kolknjNAZIVKOLONE.setServerColumnName("NAZIVKOLONE");
    kolknjNAZIVKOLONE.setSqlType(1);
    kolknjNAZIVKOLONE.setWidth(30);
    kolknjURAIRA.setCaption("Ulazna/izlazna");
    kolknjURAIRA.setColumnName("URAIRA");
    kolknjURAIRA.setDataType(com.borland.dx.dataset.Variant.STRING);
    kolknjURAIRA.setPrecision(1);
    kolknjURAIRA.setRowId(true);
    kolknjURAIRA.setTableName("KOLONEKNJUI");
    kolknjURAIRA.setServerColumnName("URAIRA");
    kolknjURAIRA.setSqlType(1);
    kolknjDUGPOT.setCaption("Dugovna/potražna");
    kolknjDUGPOT.setColumnName("DUGPOT");
    kolknjDUGPOT.setDataType(com.borland.dx.dataset.Variant.STRING);
    kolknjDUGPOT.setPrecision(1);
    kolknjDUGPOT.setTableName("KOLONEKNJUI");
    kolknjDUGPOT.setServerColumnName("DUGPOT");
    kolknjDUGPOT.setSqlType(1);
    kolknj.setResolver(dm.getQresolver());
    kolknj.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from KoloneknjUI", null, true, Load.ALL));
    setColumns(new Column[] {kolknjLOCK, kolknjAKTIV, kolknjCKOLONE, kolknjNAZIVKOLONE, kolknjURAIRA, kolknjDUGPOT});
*/
    initModule();
    
    createFilteredDataSet(kolknjulazne, "URAIRA='U'");
    createFilteredDataSet(kolknjizlazne, "URAIRA='I'");
  }

  /*public void setall() {

    ddl.create("KoloneknjUI")
       .addChar("lock", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("ckolone", 2, true)
       .addChar("nazivkolone", 50)
       .addChar("uraira", 1, true)
       .addChar("dugpot", 1)
       .addPrimaryKey("ckolone,uraira");


    Naziv = "KoloneknjUI";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }*/
}
