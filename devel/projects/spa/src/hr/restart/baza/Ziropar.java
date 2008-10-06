/****license*****************************************************************
**   file: Ziropar.java
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



public class Ziropar extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Ziropar Ziroparclass;

  QueryDataSet zpar = new raDataSet();

  Column zparCPAR = new Column();
  Column zparZIRO = new Column();
  Column zparDEV = new Column();
  Column zparOZNVAL = new Column();

  public static Ziropar getDataModule() {
    if (Ziroparclass == null) {
      Ziroparclass = new Ziropar();
    }
    return Ziroparclass;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getQueryDataSet() {
    return zpar;
  }

  public Ziropar() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    zparCPAR.setCaption("Oznaka partnera");
    zparCPAR.setColumnName("CPAR");
    zparCPAR.setDataType(com.borland.dx.dataset.Variant.INT);
    zparCPAR.setPrecision(10);
    zparCPAR.setRowId(true);
    zparCPAR.setTableName("ZIROPAR");
    zparCPAR.setServerColumnName("CPAR");
    zparCPAR.setSqlType(4);
    zparCPAR.setWidth(10);
    zparZIRO.setCaption("Žiro ra\u010Dun");
    zparZIRO.setColumnName("ZIRO");
    zparZIRO.setDataType(com.borland.dx.dataset.Variant.STRING);
    zparZIRO.setPrecision(40);
    zparZIRO.setRowId(true);
    zparZIRO.setTableName("ZIROPAR");
    zparZIRO.setServerColumnName("ZIRO");
    zparZIRO.setSqlType(1);
    zparDEV.setCaption("Devizni");
    zparDEV.setColumnName("DEV");
    zparDEV.setDataType(com.borland.dx.dataset.Variant.STRING);
    zparDEV.setPrecision(1);
    zparDEV.setTableName("ZIROPAR");
    zparDEV.setServerColumnName("DEV");
    zparDEV.setSqlType(1);
    zparDEV.setDefault("N");
    zparOZNVAL.setCaption("Oznaka valute");
    zparOZNVAL.setColumnName("OZNVAL");
    zparOZNVAL.setDataType(com.borland.dx.dataset.Variant.STRING);
    zparOZNVAL.setPrecision(3);
    zparOZNVAL.setTableName("ZIROPAR");
    zparOZNVAL.setServerColumnName("OZNVAL");
    zparOZNVAL.setSqlType(1);
    zpar.setResolver(dm.getQresolver());
    zpar.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),"select * from Ziropar", null, true, Load.ALL));
 setColumns(new Column[] {zparCPAR, zparZIRO, zparDEV, zparOZNVAL});
  }

  public void setall() {

    ddl.create("Ziropar")
       .addInteger("cpar", 6, true)
       .addChar("ziro", 40, true)
       .addChar("dev", 1, "N")
       .addChar("oznval", 3)
       .addPrimaryKey("cpar,ziro");


    Naziv = "Ziropar";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {};
    String[] uidx = new String[] {"ziro"};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
