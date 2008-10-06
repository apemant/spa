/****license*****************************************************************
**   file: Menus.java
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



public class Menus extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Menus Menusclass;

  QueryDataSet md = new raDataSet();

  Column mdCMENU = new Column();
  Column mdSORT = new Column();
  Column mdPARENTCMENU = new Column();
  Column mdMENUTYPE = new Column();
  Column mdMETHOD = new Column();

  public static Menus getDataModule() {
    if (Menusclass == null) {
      Menusclass = new Menus();
    }
    return Menusclass;
  }

  public QueryDataSet getQueryDataSet() {
    return md;
  }

  public Menus() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    mdCMENU.setCaption("Sifra");
    mdCMENU.setColumnName("CMENU");
    mdCMENU.setDataType(com.borland.dx.dataset.Variant.STRING);
    mdCMENU.setPrecision(50);
    mdCMENU.setRowId(true);
    mdCMENU.setTableName("MENUS");
    mdCMENU.setServerColumnName("CMENU");
    mdCMENU.setSqlType(1);
    mdCMENU.setWidth(30);
    mdSORT.setCaption("Redoslijed");
    mdSORT.setColumnName("CSORT");
    mdSORT.setDataType(com.borland.dx.dataset.Variant.INT);
    mdSORT.setTableName("MENUS");
    mdSORT.setServerColumnName("CSORT");
    mdSORT.setSqlType(4);
    mdSORT.setWidth(6);
    mdPARENTCMENU.setCaption("Parent");
    mdPARENTCMENU.setColumnName("PARENTCMENU");
    mdPARENTCMENU.setDataType(com.borland.dx.dataset.Variant.STRING);
    mdPARENTCMENU.setPrecision(50);
    mdPARENTCMENU.setRowId(true);
    mdPARENTCMENU.setTableName("MENUS");
    mdPARENTCMENU.setServerColumnName("PARENTCMENU");
    mdPARENTCMENU.setSqlType(1);
    mdPARENTCMENU.setWidth(30);
    mdMENUTYPE.setCaption("Tip");
    mdMENUTYPE.setColumnName("MENUTYPE");
    mdMENUTYPE.setDataType(com.borland.dx.dataset.Variant.STRING);
    mdMENUTYPE.setPrecision(1);
    mdMENUTYPE.setDefault("I");
    mdMENUTYPE.setTableName("MENUS");
    mdMENUTYPE.setServerColumnName("MENUTYPE");
    mdMENUTYPE.setSqlType(1);
    mdMETHOD.setCaption("Metoda");
    mdMETHOD.setColumnName("METHOD");
    mdMETHOD.setDataType(com.borland.dx.dataset.Variant.STRING);
    mdMETHOD.setPrecision(80);
    mdMETHOD.setTableName("MENUS");
    mdMETHOD.setServerColumnName("METHOD");
    mdMETHOD.setSqlType(1);
    mdMETHOD.setWidth(30);
    md.setResolver(dm.getQresolver());
    md.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Menus", null, true, Load.ALL));
    setColumns(new Column[] {mdCMENU, mdSORT, mdPARENTCMENU, mdMENUTYPE, mdMETHOD});
  }

  public void setall() {

    ddl.create("Menus")
       .addChar("cmenu", 50, true)
       .addInteger("csort", 6)
       .addChar("parentcmenu", 50, true)
       .addChar("menutype", 1, "I")
       .addChar("method", 80)
       .addPrimaryKey("cmenu,parentcmenu");


    Naziv = "Menus";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"parentcmenu"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
