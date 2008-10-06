/****license*****************************************************************
**   file: Repldef.java
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



public class Repldef extends KreirDrop implements DataModule {

  dM dm  = dM.getDataModule();
  private static Repldef Repldefclass;

  QueryDataSet rd = new QueryDataSet();

  Column rdBATCH_INDEX = new Column();
  Column rdIMETAB = new Column();
  Column rdRBR_URL = new Column();
  Column rdRBR_URL_U = new Column();
  Column rdNACINREP = new Column();
  Column rdSLIJED = new Column();
  Column rdCLASSNAME = new Column();

  public static Repldef getDataModule() {
    if (Repldefclass == null) {
      Repldefclass = new Repldef();
    }
    return Repldefclass;
  }

  public QueryDataSet getQueryDataSet() {
    return rd;
  }

  public Repldef() {
    try {
      modules.put(this.getClass().getName(), this);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    rdBATCH_INDEX.setCaption("Identifikator");
    rdBATCH_INDEX.setColumnName("BATCH_INDEX");
    rdBATCH_INDEX.setDataType(com.borland.dx.dataset.Variant.STRING);
    rdBATCH_INDEX.setPrecision(20);
    rdBATCH_INDEX.setRowId(true);
    rdBATCH_INDEX.setTableName("REPLDEF");
    rdBATCH_INDEX.setServerColumnName("BATCH_INDEX");
    rdBATCH_INDEX.setSqlType(1);
    rdIMETAB.setCaption("Naziv tablice");
    rdIMETAB.setColumnName("IMETAB");
    rdIMETAB.setDataType(com.borland.dx.dataset.Variant.STRING);
    rdIMETAB.setPrecision(20);
    rdIMETAB.setRowId(true);
    rdIMETAB.setTableName("REPLDEF");
    rdIMETAB.setServerColumnName("IMETAB");
    rdIMETAB.setSqlType(1);
    rdRBR_URL.setCaption("URL – iz");
    rdRBR_URL.setColumnName("RBR_URL");
    rdRBR_URL.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rdRBR_URL.setRowId(true);
    rdRBR_URL.setTableName("REPLDEF");
    rdRBR_URL.setServerColumnName("RBR_URL");
    rdRBR_URL.setSqlType(5);
    rdRBR_URL.setWidth(2);
    rdRBR_URL.setDefault("0");
    rdRBR_URL_U.setCaption("URL – u");
    rdRBR_URL_U.setColumnName("RBR_URL_U");
    rdRBR_URL_U.setDataType(com.borland.dx.dataset.Variant.SHORT);
    rdRBR_URL_U.setTableName("REPLDEF");
    rdRBR_URL_U.setServerColumnName("RBR_URL_U");
    rdRBR_URL_U.setSqlType(5);
    rdRBR_URL_U.setWidth(2);
    rdRBR_URL_U.setDefault("0");
    rdNACINREP.setCaption("Na\u010Din replikacije");
    rdNACINREP.setColumnName("NACINREP");
    rdNACINREP.setDataType(com.borland.dx.dataset.Variant.STRING);
    rdNACINREP.setPrecision(1);
    rdNACINREP.setTableName("REPLDEF");
    rdNACINREP.setServerColumnName("NACINREP");
    rdNACINREP.setSqlType(1);
    rdNACINREP.setDefault("1");
    rdSLIJED.setCaption("Redoslijed");
    rdSLIJED.setColumnName("SLIJED");
    rdSLIJED.setDataType(com.borland.dx.dataset.Variant.INT);
    rdSLIJED.setTableName("REPLDEF");
    rdSLIJED.setServerColumnName("SLIJED");
    rdSLIJED.setSqlType(4);
    rdSLIJED.setWidth(6);
    rdSLIJED.setDefault("0");
    rdCLASSNAME.setCaption("Ime klase");
    rdCLASSNAME.setColumnName("CLASSNAME");
    rdCLASSNAME.setDataType(com.borland.dx.dataset.Variant.STRING);
    rdCLASSNAME.setPrecision(40);
    rdCLASSNAME.setTableName("REPLDEF");
    rdCLASSNAME.setServerColumnName("CLASSNAME");
    rdCLASSNAME.setSqlType(1);
    rdCLASSNAME.setWidth(30);
    rd.setResolver(dm.getQresolver());
    rd.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from Repldef", null, true, Load.ALL));
    setColumns(new Column[] {rdBATCH_INDEX, rdIMETAB, rdRBR_URL, rdRBR_URL_U, rdNACINREP, rdSLIJED, rdCLASSNAME});
  }

  public void setall() {

    ddl.create("Repldef")
       .addChar("batch_index", 20, true)
       .addChar("imetab", 20, true)
       .addShort("rbr_url", 2, true)
       .addShort("rbr_url_u", 2)
       .addChar("nacinrep", 1, "1")
       .addInteger("slijed", 6)
       .addChar("classname", 40)
       .addPrimaryKey("batch_index,imetab,rbr_url");


    Naziv = "Repldef";

    SqlDefTabela = ddl.getCreateTableString();

    String[] idx = new String[] {"batch_index", "imetab"};
    String[] uidx = new String[] {};
    DefIndex = ddl.getIndices(idx, uidx);
    NaziviIdx = ddl.getIndexNames(idx, uidx);
  }
}
