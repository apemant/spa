/****license*****************************************************************
**   file: Tmjesta.java
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



public class Tmjesta extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Tmjesta Tmjestaclass;

  QueryDataSet tm = new QueryDataSet();

  Column tmRBR = new Column();
  Column tmPBR = new Column();
  Column tmMJ = new Column();
  Column tmNAS = new Column();
  Column tmZUP = new Column();
  Column tmCZUP = new Column();

  public static Tmjesta getDataModule() {
    if (Tmjestaclass == null) {
      Tmjestaclass = new Tmjesta();
    }
    return Tmjestaclass;
  }

  public QueryDataSet getQueryDataSet() {
    return tm;
  }

  public Tmjesta() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    tmRBR.setCaption("Rbr");
    tmRBR.setColumnName("RBR");
    tmRBR.setDataType(com.borland.dx.dataset.Variant.INT);
    tmRBR.setPrecision(6);
    tmRBR.setRowId(true);
    tmRBR.setTableName("TMJESTA");
    tmRBR.setServerColumnName("RBR");
    tmRBR.setSqlType(4);
    tmRBR.setWidth(6);
    tmPBR.setCaption("Pbr");
    tmPBR.setColumnName("PBR");
    tmPBR.setDataType(com.borland.dx.dataset.Variant.INT);
    tmPBR.setPrecision(6);
    tmPBR.setTableName("TMJESTA");
    tmPBR.setServerColumnName("PBR");
    tmPBR.setSqlType(4);
    tmPBR.setWidth(6);
    tmMJ.setCaption("Mjesto");
    tmMJ.setColumnName("MJ");
    tmMJ.setDataType(com.borland.dx.dataset.Variant.STRING);
    tmMJ.setPrecision(50);
    tmMJ.setTableName("TMJESTA");
    tmMJ.setServerColumnName("MJ");
    tmMJ.setSqlType(1);
    tmMJ.setWidth(30);
    tmNAS.setCaption("Naselje");
    tmNAS.setColumnName("NAS");
    tmNAS.setDataType(com.borland.dx.dataset.Variant.STRING);
    tmNAS.setPrecision(50);
    tmNAS.setTableName("TMJESTA");
    tmNAS.setServerColumnName("NAS");
    tmNAS.setSqlType(1);
    tmNAS.setWidth(30);
    tmZUP.setCaption("Županija");
    tmZUP.setColumnName("ZUP");
    tmZUP.setDataType(com.borland.dx.dataset.Variant.STRING);
    tmZUP.setPrecision(50);
    tmZUP.setTableName("TMJESTA");
    tmZUP.setServerColumnName("ZUP");
    tmZUP.setSqlType(1);
    tmZUP.setWidth(30);
    tmCZUP.setCaption("Šifžup");
    tmCZUP.setColumnName("CZUP");
    tmCZUP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    tmCZUP.setPrecision(3);
    tmCZUP.setTableName("TMJESTA");
    tmCZUP.setServerColumnName("CZUP");
    tmCZUP.setSqlType(5);
    tmCZUP.setWidth(3);
    tm.setResolver(dm.getQresolver());
    tm.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Tmjesta", null, true, Load.ALL));
    setColumns(new Column[] {tmRBR, tmPBR, tmMJ, tmNAS, tmZUP, tmCZUP});
  }

  public void setall() {

    ddl.create("Tmjesta")
       .addInteger("rbr", 6, true)
       .addInteger("pbr", 6)
       .addChar("mj", 50)
       .addChar("nas", 50)
       .addChar("zup", 50)
       .addShort("czup", 3)
       .addPrimaryKey("rbr");


    Naziv = "Tmjesta";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
