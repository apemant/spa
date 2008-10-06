/****license*****************************************************************
**   file: Plizv.java
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



public class Plizv extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Plizv Plizvclass;

  QueryDataSet plizv = new raDataSet();

  Column plizvLOKK = new Column();
  Column plizvAKTIV = new Column();
  Column plizvCIZV = new Column();
  Column plizvOPIS = new Column();
  Column plizvPARAMETRI = new Column();

  public static Plizv getDataModule() {
    if (Plizvclass == null) {
      Plizvclass = new Plizv();
    }
    return Plizvclass;
  }

  public QueryDataSet getQueryDataSet() {
    return plizv;
  }

  public Plizv() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    plizvLOKK.setCaption("Status zauzetosti");
    plizvLOKK.setColumnName("LOKK");
    plizvLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    plizvLOKK.setPrecision(1);
    plizvLOKK.setTableName("PLIZV");
    plizvLOKK.setServerColumnName("LOKK");
    plizvLOKK.setSqlType(1);
    plizvLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    plizvLOKK.setDefault("N");
    plizvAKTIV.setCaption("Aktivan - neaktivan");
    plizvAKTIV.setColumnName("AKTIV");
    plizvAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    plizvAKTIV.setPrecision(1);
    plizvAKTIV.setTableName("PLIZV");
    plizvAKTIV.setServerColumnName("AKTIV");
    plizvAKTIV.setSqlType(1);
    plizvAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    plizvAKTIV.setDefault("D");
    plizvCIZV.setCaption("Oznaka");
    plizvCIZV.setColumnName("CIZV");
    plizvCIZV.setDataType(com.borland.dx.dataset.Variant.SHORT);
    plizvCIZV.setPrecision(4);
    plizvCIZV.setRowId(true);
    plizvCIZV.setTableName("PLIZV");
    plizvCIZV.setServerColumnName("CIZV");
    plizvCIZV.setSqlType(5);
    plizvCIZV.setWidth(4);
    plizvOPIS.setCaption("Opis");
    plizvOPIS.setColumnName("OPIS");
    plizvOPIS.setDataType(com.borland.dx.dataset.Variant.STRING);
    plizvOPIS.setPrecision(30);
    plizvOPIS.setTableName("PLIZV");
    plizvOPIS.setServerColumnName("OPIS");
    plizvOPIS.setSqlType(1);
    plizvPARAMETRI.setCaption("Parametri");
    plizvPARAMETRI.setColumnName("PARAMETRI");
    plizvPARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    plizvPARAMETRI.setPrecision(20);
    plizvPARAMETRI.setTableName("PLIZV");
    plizvPARAMETRI.setServerColumnName("PARAMETRI");
    plizvPARAMETRI.setSqlType(1);
    plizvPARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    plizv.setResolver(dm.getQresolver());
    plizv.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Plizv", null, true, Load.ALL));
 setColumns(new Column[] {plizvLOKK, plizvAKTIV, plizvCIZV, plizvOPIS, plizvPARAMETRI});
  }

  public void setall() {

    ddl.create("Plizv")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addShort("cizv", 4, true)
       .addChar("opis", 30)
       .addChar("parametri", 20)
       .addPrimaryKey("cizv");


    Naziv = "Plizv";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
