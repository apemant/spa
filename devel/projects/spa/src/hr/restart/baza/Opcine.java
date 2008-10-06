/****license*****************************************************************
**   file: Opcine.java
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



public class Opcine extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Opcine Opcineclass;

  QueryDataSet opcine = new raDataSet();

  Column opcineLOKK = new Column();
  Column opcineAKTIV = new Column();
  Column opcineCOPCINE = new Column();
  Column opcineNAZIVOP = new Column();
  Column opcineCZUP = new Column();
  Column opcinePARAMETRI = new Column();

  public static Opcine getDataModule() {
    if (Opcineclass == null) {
      Opcineclass = new Opcine();
    }
    return Opcineclass;
  }

  public QueryDataSet getQueryDataSet() {
    return opcine;
  }

  public Opcine() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    opcineLOKK.setCaption("Status zauzetosti");
    opcineLOKK.setColumnName("LOKK");
    opcineLOKK.setDataType(com.borland.dx.dataset.Variant.STRING);
    opcineLOKK.setPrecision(1);
    opcineLOKK.setTableName("OPCINE");
    opcineLOKK.setServerColumnName("LOKK");
    opcineLOKK.setSqlType(1);
    opcineLOKK.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    opcineLOKK.setDefault("N");
    opcineAKTIV.setCaption("Aktivan - neaktivan");
    opcineAKTIV.setColumnName("AKTIV");
    opcineAKTIV.setDataType(com.borland.dx.dataset.Variant.STRING);
    opcineAKTIV.setPrecision(1);
    opcineAKTIV.setTableName("OPCINE");
    opcineAKTIV.setServerColumnName("AKTIV");
    opcineAKTIV.setSqlType(1);
    opcineAKTIV.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    opcineAKTIV.setDefault("D");
    opcineCOPCINE.setCaption("Oznaka");
    opcineCOPCINE.setColumnName("COPCINE");
    opcineCOPCINE.setDataType(com.borland.dx.dataset.Variant.STRING);
    opcineCOPCINE.setPrecision(3);
    opcineCOPCINE.setRowId(true);
    opcineCOPCINE.setTableName("OPCINE");
    opcineCOPCINE.setServerColumnName("COPCINE");
    opcineCOPCINE.setSqlType(1);
    opcineCOPCINE.setWidth(5);
    opcineNAZIVOP.setCaption("Naziv");
    opcineNAZIVOP.setColumnName("NAZIVOP");
    opcineNAZIVOP.setDataType(com.borland.dx.dataset.Variant.STRING);
    opcineNAZIVOP.setPrecision(50);
    opcineNAZIVOP.setTableName("OPCINE");
    opcineNAZIVOP.setServerColumnName("NAZIVOP");
    opcineNAZIVOP.setSqlType(1);
    opcineNAZIVOP.setWidth(30);
    opcineCZUP.setCaption("Oznaka županije");
    opcineCZUP.setColumnName("CZUP");
    opcineCZUP.setDataType(com.borland.dx.dataset.Variant.SHORT);
    opcineCZUP.setPrecision(3);
    opcineCZUP.setTableName("OPCINE");
    opcineCZUP.setServerColumnName("CZUP");
    opcineCZUP.setSqlType(5);
    opcineCZUP.setWidth(5);
    opcinePARAMETRI.setCaption("Parametri");
    opcinePARAMETRI.setColumnName("PARAMETRI");
    opcinePARAMETRI.setDataType(com.borland.dx.dataset.Variant.STRING);
    opcinePARAMETRI.setPrecision(20);
    opcinePARAMETRI.setTableName("OPCINE");
    opcinePARAMETRI.setServerColumnName("PARAMETRI");
    opcinePARAMETRI.setSqlType(1);
    opcinePARAMETRI.setVisible(com.borland.jb.util.TriStateProperty.FALSE);
    opcine.setResolver(dm.getQresolver());
    opcine.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Opcine", null, true, Load.ALL));
 setColumns(new Column[] {opcineLOKK, opcineAKTIV, opcineCOPCINE, opcineNAZIVOP, opcineCZUP, opcinePARAMETRI});
  }

  public void setall() {

    ddl.create("Opcine")
       .addChar("lokk", 1, "N")
       .addChar("aktiv", 1, "D")
       .addChar("copcine", 3, true)
       .addChar("nazivop", 50)
       .addShort("czup", 3)
       .addChar("parametri", 20)
       .addPrimaryKey("copcine");


    Naziv = "Opcine";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
