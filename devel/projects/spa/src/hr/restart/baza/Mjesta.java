/****license*****************************************************************
**   file: Mjesta.java
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



public class Mjesta extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Mjesta Mjestaclass;

  QueryDataSet mje = new raDataSet();
  QueryDataSet mjeaktiv = new raDataSet();

  Column mjeLOKK = new Column();
  Column mjeAKTIV = new Column();
  Column mjeCMJESTA = new Column();
  Column mjeNAZMJESTA = new Column();
  Column mjePBR = new Column();
  Column mjeCZUP = new Column();
  Column mjeCZEM = new Column();

  public static Mjesta getDataModule() {
    if (Mjestaclass == null) {
      Mjestaclass = new Mjesta();
    }
    return Mjestaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return mje;
  }

  public QueryDataSet getAktiv() {
    return mjeaktiv;
  }

  public Mjesta() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    mjeLOKK.setCaption("Status zauzetosti");
    mjeLOKK.setColumnName("LOKK");
    mjeLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    mjeLOKK.setPrecision(1);
    mjeLOKK.setTableName("MJESTA");
    mjeLOKK.setServerColumnName("LOKK");
    mjeLOKK.setSqlType(1);
    mjeLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    mjeLOKK.setDefault("N");
    mjeAKTIV.setCaption("Aktivan - neaktivan");
    mjeAKTIV.setColumnName("AKTIV");
    mjeAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    mjeAKTIV.setPrecision(1);
    mjeAKTIV.setTableName("MJESTA");
    mjeAKTIV.setServerColumnName("AKTIV");
    mjeAKTIV.setSqlType(1);
    mjeAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    mjeAKTIV.setDefault("D");
    mjeCMJESTA.setCaption("Šifra");
    mjeCMJESTA.setColumnName("CMJESTA");
    mjeCMJESTA.setDataType(com.borland.dx.dataset.Variant.INT);
    mjeCMJESTA.setPrecision(6);
    mjeCMJESTA.setRowId(true);
    mjeCMJESTA.setTableName("MJESTA");
    mjeCMJESTA.setServerColumnName("CMJESTA");
    mjeCMJESTA.setSqlType(4);
    mjeCMJESTA.setWidth(6);
    mjeNAZMJESTA.setCaption("Naziv");
    mjeNAZMJESTA.setColumnName("NAZMJESTA");
    mjeNAZMJESTA.setDataType(com.borland.dx.dataset.Variant.STRING);
    mjeNAZMJESTA.setPrecision(30);
    mjeNAZMJESTA.setTableName("MJESTA");
    mjeNAZMJESTA.setServerColumnName("NAZMJESTA");
    mjeNAZMJESTA.setSqlType(1);
    mjeNAZMJESTA.setWidth(30);
    mjePBR.setCaption("Poštanski broj");
    mjePBR.setColumnName("PBR");
    mjePBR.setDataType(com.borland.dx.dataset.Variant.INT);
    mjePBR.setPrecision(5);
    mjePBR.setTableName("MJESTA");
    mjePBR.setServerColumnName("PBR");
    mjePBR.setSqlType(4);
    mjePBR.setWidth(5);
    mjeCZUP.setCaption("Županija");
    mjeCZUP.setColumnName("CZUP");
    mjeCZUP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    mjeCZUP.setPrecision(3);
    mjeCZUP.setTableName("MJESTA");
    mjeCZUP.setServerColumnName("CZUP");
    mjeCZUP.setSqlType(5);
    mjeCZEM.setCaption("Zemlja");
    mjeCZEM.setColumnName("CZEM");
    mjeCZEM.setDataType(com.borland.dx.dataset.Variant.STRING);
    mjeCZEM.setPrecision(3);
    mjeCZEM.setTableName("MJESTA");
    mjeCZEM.setServerColumnName("CZEM");
    mjeCZEM.setSqlType(1);
    mje.setResolver(dm.getQresolver());
    mje.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Mjesta", null, true, Load.ALL));
    setColumns(new Column[] {mjeLOKK, mjeAKTIV, mjeCMJESTA, mjeNAZMJESTA, mjePBR, mjeCZUP, mjeCZEM});

    createFilteredDataSet(mjeaktiv, "aktiv='D'");
  }

  public void setall() {

    ddl.create("Mjesta")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addInteger("cmjesta", 6, true)
       .addChar("nazmjesta", 30)
       .addInteger("pbr", 5)
       .addShort("czup", 3)
       .addChar("czem", 3)
       .addPrimaryKey("cmjesta");


    Naziv = "Mjesta";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"cmjesta"};
    String[] uidx = new String[] {"cmjesta"};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
